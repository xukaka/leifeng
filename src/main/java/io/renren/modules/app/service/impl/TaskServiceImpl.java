package io.renren.modules.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.*;
import io.renren.common.validator.ValidatorUtils;
import io.renren.config.RabbitMQConfig;
import io.renren.modules.app.dao.task.TaskDao;
import io.renren.modules.app.dao.task.TaskReceiveDao;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.dto.TaskBannerDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.TaskDifficultyEnum;
import io.renren.modules.app.entity.TaskStatusEnum;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberTagRelationEntity;
import io.renren.modules.app.entity.task.TaskAddressEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.entity.task.TaskReceiveEntity;
import io.renren.modules.app.entity.task.TaskTagEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.form.TaskQueryForm;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.service.MemberTagRelationService;
import io.renren.modules.app.service.TaskService;
import io.renren.modules.app.service.TaskTagService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {
    private final static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Resource
    private TaskReceiveDao taskReceiveDao;
    @Resource
    private TaskTagService taskTagService;
    @Resource
    private MemberTagRelationService memberTagRelationService;
    @Resource
    private MemberService memberService;
    @Resource
    private RabbitMqHelper rabbitMqHelper;
    @Resource
    private RedisUtils redisUtils;

    private static final long EXPIRE = 60 * 10;//10分钟

    @Override
    public List<TaskBannerDto> getTaskBanners() {
        List<TaskBannerDto> banners = redisUtils.getList(RedisKeys.BANNER_KEY, TaskBannerDto.class);
        if (CollectionUtils.isEmpty(banners)) {
            banners = this.baseMapper.getTaskBanners();
            if (!CollectionUtils.isEmpty(banners)) {
                redisUtils.addList(RedisKeys.BANNER_KEY, banners, EXPIRE);
            }
        }
        return banners;
    }

    @Override
    public PageUtils<TaskDto> searchTasks(TaskQueryForm form, PageWrapper page) {
        Map<String, Object> queryMap = getTaskQueryMap(form);
        List<TaskDto> tasks = baseMapper.searchTasks(queryMap, page);
        if (CollectionUtils.isEmpty(tasks)) {
            return new PageUtils<>();
        }
        setTaskDistance(form, tasks);
        int total = baseMapper.count(queryMap);
        return new PageUtils<>(tasks, total, page.getPageSize(), page.getCurrPage());
    }

    //计算距离
    private void setTaskDistance(TaskQueryForm form, List<TaskDto> tasks) {
        for (TaskDto task : tasks) {
            TaskAddressEntity address = task.getAddress();
            long distance = 0L;
            if (form.getLongitude() != null && form.getLatitude() != null
                    && address.getLatitude() != null && address.getLongitude() != null) {
                distance = GeoUtils.getDistance(form.getLatitude(), form.getLongitude(), address.getLatitude(), address.getLongitude());
            }
            task.setDistance(distance);
        }
    }

    private Map<String, Object> getTaskQueryMap(TaskQueryForm form) {
        Map<String, Object> queryMap = new HashMap<>();
        if (!StringUtils.isEmpty(form.getKeyword())) {
            queryMap.put("keyword", form.getKeyword());
        }
        if (form.getLatitude() != null && form.getLongitude() != null && form.getRaidus() != null) {
            Map<String, Double> aroundMap = GeoUtils.getAround(form.getLatitude(), form.getLongitude(), form.getRaidus());
            queryMap.putAll(aroundMap);
        }
        if (!CollectionUtils.isEmpty(form.getTagIds())) {
            queryMap.put("tagIds", form.getTagIds());
        }
        if (form.getTaskDifficulty() != null) {
            TaskDifficultyEnum difficulty = form.getTaskDifficulty();
            queryMap.put("difficulty", difficulty.name());
            switch (difficulty) {
                case FREE:
                    queryMap.put("maxVirtualCurrency", difficulty.getMaxVirtualCurrency());
                    break;
                case SIMPLE:
                    queryMap.put("minVirtualCurrency", difficulty.getMinVirtualCurrency());
                    queryMap.put("maxVirtualCurrency", difficulty.getMaxVirtualCurrency());
                    break;
                case NORMAL:
                    queryMap.put("minVirtualCurrency", difficulty.getMinVirtualCurrency());
                    queryMap.put("maxVirtualCurrency", difficulty.getMaxVirtualCurrency());
                    break;
                case DIFFICULT:
                    queryMap.put("minVirtualCurrency", difficulty.getMinVirtualCurrency());
                default:
                    break;
            }
        }
        return queryMap;
    }

    @Override
    public PageUtils<TaskDto> getPublishedTasks(Long publisherId, PageWrapper page) {
        List<TaskDto> tasks = baseMapper.getPublishedTasks(publisherId, page);
        if (CollectionUtils.isEmpty(tasks)) {
            return new PageUtils<>();
        }
        int total = baseMapper.publishCount(publisherId);
        return new PageUtils<>(tasks, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public PageUtils<TaskDto> getReceivedTasks(Long receiverId, PageWrapper page) {
        List<TaskDto> tasks = baseMapper.getReceivedTasks(receiverId, page);
        if (CollectionUtils.isEmpty(tasks)) {
            return new PageUtils<>();
        }
        int total = baseMapper.receiveCount(receiverId);
        return new PageUtils<>(tasks, total, page.getPageSize(), page.getCurrPage());
    }


    @Override
    public TaskDto getTask(Long curMemberId, Long id) {
        TaskDto task = baseMapper.getTask(id);
        if (task != null) {
            //是否关注
            boolean isFollowed = memberService.isFollowed(curMemberId, task.getCreator().getId());
            task.setFollowed(isFollowed);
            //是否领取
            boolean isReceived = existsRecevier(id,curMemberId);
            task.setReceived(isReceived);
            task.setCurSystemTime(DateUtils.now());
        }
        return task;
    }

    /**
     * 是否领取（是否存在领取列表中）
     * @param taskId
     * @param receiverId
     * @return
     */
    private boolean existsRecevier(Long taskId,Long receiverId){
        Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("task_id", taskId)
                .eq("receiver_id", receiverId);
        int count =  taskReceiveDao.selectCount(wrapper);
        return count > 0;
    }

    @Override
    @Transactional
    public void createTask(Long creatorId, TaskForm form) {
        ValidatorUtils.validateEntity(form);
        TaskEntity task = new TaskEntity();
        BeanUtils.copyProperties(form, task);
        task.setCreatorId(creatorId);
        task.setStatus(TaskStatusEnum.published);
        task.setCreateTime(DateUtils.now());
        insert(task);
        addTaskImageRelation(task.getId(), form.getImageUrls());
        addTaskTagRelation(task.getId(), form.getTagIds());
      /*  addTaskNotifiedUserRelation(task.getId(), form.getNotifiedUserIds());
        if (!CollectionUtils.isEmpty(form.getNotifiedUserIds())) {
            //推送消息给被提醒用户
        }*/

        ThreadPoolUtils.execute(() -> {
            //推送消息给关注我的所有人
            /*MemberDto creator = memberService.getMember(creatorId);
            JSONObject extras = new JSONObject();
            extras.put("businessCode", "0");//0，发布任务
            extras.put("type", "task");//类型为任务
            extras.put("id", task.getId());
            extras.put("title", task.getTitle());
            extras.put("creatorId", creator.getId());
            extras.put("creatorAvatar", creator.getAvatar());
            extras.put("creatorNickName", creator.getNickName());
            extras.put("groupId", creator.getId());
            logger.info("推消息到关注我的组，extras=" + extras.toJSONString());
            rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_NAME, extras.toJSONString());*/
        });
    }


    @Override
    public void updateTask(TaskForm form) {
        ValidatorUtils.validateEntity(form);
        TaskEntity task = new TaskEntity();
        BeanUtils.copyProperties(form, task);
        updateById(task);
    }

    @Override
    public void deleteTask(Long id) {
        TaskEntity task = selectById(id);
        if (task == null || task.getStatus() == TaskStatusEnum.executing || task.getStatus() == TaskStatusEnum.submitted) {
            throw new RRException("当前任务状态,不可删除", 100);
        }
        task.setDeleted(true);
        updateById(task);
    }

    @Override
    public PageUtils<MemberDto> getTaskReceivers(Long taskId, PageWrapper page) {
        List<MemberDto> members = baseMapper.getTaskReceivers(taskId, page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }
        int total = baseMapper.receiverCount(taskId);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }

    /**
     * 领取任务
     */
    @Override
    @Transactional
    public void receiveTask(Long receiverId, Long taskId) {
        TaskEntity task = baseMapper.selectById(taskId);
        if (task.getStatus() == TaskStatusEnum.cancelled) {
            throw new RRException("任务已取消", 100);
        }
        boolean isReceiveable = isReceiveableTask(receiverId, taskId);
        if (!isReceiveable) {
            throw new RRException("任务已领取", 100);
        }

        long curTime = DateUtils.now();
        TaskReceiveEntity receive = new TaskReceiveEntity(curTime, receiverId, taskId);
        receive.setUpdateTime(curTime);
        receive.setStatus(TaskStatusEnum.published);

        logger.info("receive task:" + receive);
        taskReceiveDao.insert(receive);

        ThreadPoolUtils.execute(() -> {
            //推送消息给任务发布人
            /*MemberDto receiver = memberService.getMember(receiverId);
            JSONObject extras = new JSONObject();
            extras.put("businessCode", "1");//1，领取任务
            extras.put("taskId", task.getId());
            extras.put("taskTitle", task.getTitle());
            extras.put("taskCreatorId", task.getCreatorId());
            extras.put("taskReceiverId", receiver.getId());
            extras.put("taskReceiverAvatar", receiver.getAvatar());
            extras.put("taskReceiverNickName", receiver.getNickName());
            logger.info("推送消息给任务发布人，extras=" + extras.toJSONString());
            rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_NAME, extras.toJSONString());*/
        });
    }


    @Override
    @Transactional
    public void chooseTaskReceiver(Long taskId, Long receiverId) {
        boolean isChooseable = isChooseableReceiver(taskId, receiverId);
        if (!isChooseable) {
            throw new RRException("任务已开始，不能选择人了", 100);
        }

        TaskReceiveEntity receive = new TaskReceiveEntity();
        receive.setStatus(TaskStatusEnum.received);
        Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("task_id", taskId)
                .eq("receiver_id", receiverId);
        Integer result = taskReceiveDao.update(receive, wrapper);
        if (result != null && result > 0) {
            updateTaskStatus(taskId, TaskStatusEnum.received);

            ThreadPoolUtils.execute(() -> {
                //推送消息给任务接收人
                /*TaskDto task = baseMapper.getTask(taskId);
                JSONObject extras = new JSONObject();
                extras.put("businessCode", "2");//2，确认领取任务
                extras.put("taskId", task.getId());
                extras.put("taskTitle", task.getTitle());
                extras.put("taskCreatorId", task.getCreator().getId());
                extras.put("taskCreatorAvatar", task.getCreator().getAvatar());
                extras.put("taskCreatorNickName", task.getCreator().getNickName());
                extras.put("taskReceiverId", receiverId);
                logger.info("推送消息给任务接收人，extras=" + extras.toJSONString());
                rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_NAME, extras.toJSONString());*/
            });
        }


    }


    @Override
    @Transactional
    public void executeTask(Long taskId, Long receiverId) {
        TaskEntity task = selectById(taskId);
        if (task.getStatus()==TaskStatusEnum.cancelled){
            throw new RRException("任务已取消",100);
        }
        boolean isExecutable = isExecutableTask(taskId, receiverId);
        if (!isExecutable) {
            throw new RRException("任务不可执行", 100);
        }
        TaskReceiveEntity receive = new TaskReceiveEntity();
        receive.setStatus(TaskStatusEnum.executing);
        receive.setUpdateTime(DateUtils.now());
        Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("task_id", taskId)
                .eq("receiver_id", receiverId);
        Integer result = taskReceiveDao.update(receive, wrapper);
        if (result != null && result > 0) {
            updateTaskStatus(taskId, TaskStatusEnum.executing);

            ThreadPoolUtils.execute(() -> {
                //推送消息给任务发布人
                /*MemberDto receiver = memberService.getMember(receiverId);
                TaskDto taskDto = baseMapper.getTask(taskId);
                JSONObject extras = new JSONObject();
                extras.put("businessCode", "3");//执行任务
                extras.put("taskId", taskDto.getId());
                extras.put("taskTitle", taskDto.getTitle());
                extras.put("taskCreatorId", taskDto.getCreator().getId());
                extras.put("taskReceiverId", receiver.getId());
                extras.put("taskReceiverAvatar", receiver.getAvatar());
                extras.put("taskReceiverNickName", receiver.getNickName());
                logger.info("推送消息给任务发布人，extras=" + extras.toJSONString());
                rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_NAME, extras.toJSONString());*/
            });
        }
    }


    @Override
    @Transactional
    public void cancelTaskByReceiver(Long receiverId, Long taskId) {
        TaskDto task = baseMapper.getTask(taskId);
        Member receiver = task.getReceiver();

        if (receiverId == null ||receiver!=null || receiverId.equals(receiver.getId())
                || (task.getStatus() != TaskStatusEnum.published && task.getStatus() != TaskStatusEnum.received)) {
            throw new RRException("任务执行中，不能取消", 100);
        }
        Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("task_id", taskId)
                .eq("receiver_id", receiverId);
        taskReceiveDao.delete( wrapper);
        if (receiverId.equals(receiver.getId())) {
            updateTaskStatus(taskId, TaskStatusEnum.published);

            ThreadPoolUtils.execute(() -> {
                //推送消息给任务发布人
           /* MemberDto receiver = memberService.getMember(receiverId);
            JSONObject extras = new JSONObject();
            extras.put("businessCode", "6");//领取人取消任务
            extras.put("taskId", task.getId());
            extras.put("taskTitle", task.getTitle());
            extras.put("taskReceiverId", receiver.getId());
            extras.put("taskReceiverAvatar", receiver.getAvatar());
            extras.put("taskReceiverNickName", receiver.getNickName());
            extras.put("taskCreatorId", task.getCreator().getId());
            logger.info("推送消息给任务发布人，extras=" + extras.toJSONString());
            rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_NAME, extras.toJSONString());*/

            });
        }




    }


    @Override
    @Transactional
    public void cancelTaskByPublisher(Long publisherId, Long taskId) {
        TaskDto task = baseMapper.getTask(taskId);
        if (publisherId == null || !publisherId.equals(task.getCreator().getId())
                || (task.getStatus() != TaskStatusEnum.published && task.getStatus() != TaskStatusEnum.received)) {
            /*TaskReceiveEntity receive = new TaskReceiveEntity();
            receive.setStatus(TaskStatusEnum.cancelled);
            receive.setUpdateTime(DateUtils.now());
            Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("task_id", taskId)
                    .eq("receiver_id", task.getReceiver().getId());
            Integer result = taskReceiveDao.update(receive, wrapper);
            if (result != null && result > 0) {
            }*/
            throw new RRException("任务执行中，不能取消", 100);
        }
        updateTaskStatus(taskId, TaskStatusEnum.cancelled);
        updateReceiverTaskStatus(taskId);


        ThreadPoolUtils.execute(() -> {
            /*Member receiver = task.getReceiver();
            if (receiver != null) {
                //推送消息给任务领取人
                MemberDto creator = memberService.getMember(publisherId);
                JSONObject extras = new JSONObject();
                extras.put("businessCode", "7");//发布人取消任务
                extras.put("taskId", task.getId());
                extras.put("taskTitle", task.getTitle());
                extras.put("taskReceiverId", receiver.getId());
                extras.put("taskCreatorAvatar",creator.getAvatar());
                extras.put("taskCreatorNickName", creator.getNickName());
                extras.put("taskCreatorId", creator.getId());
                logger.info("推送消息给任务领取人，extras=" + extras.toJSONString());
                rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_NAME, extras.toJSONString());
            }
*/
        });

    }

    @Override
    public void republishTask(Long publisher, Long taskId) {
        TaskEntity task = baseMapper.selectById(taskId);
        if (task != null && publisher != null && publisher.equals(task.getCreatorId())){
            updateTaskStatus(taskId, TaskStatusEnum.published);
        }
    }

    //任务取消，设置领取人状态为发布
    private void updateReceiverTaskStatus(Long taskId) {
        TaskReceiveEntity receive = new TaskReceiveEntity();
        receive.setStatus(TaskStatusEnum.published);
        receive.setUpdateTime(DateUtils.now());
        Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("task_id", taskId);
        taskReceiveDao.update(receive, wrapper);
    }

    //任务是否可执行
    private boolean isExecutableTask(Long taskId, Long receiverId) {
        int count = baseMapper.isExecutableTask(taskId, receiverId);
        return count > 0;
    }

    @Override
    @Transactional
    public void submitTask(Long receiverId, Long taskId) {
        boolean isSubmitable = isSubmitableTask(receiverId, taskId);
        if (!isSubmitable) {
            throw new RRException("任务未执行，不可提交", 100);
        }

        TaskReceiveEntity receive = new TaskReceiveEntity();
        receive.setStatus(TaskStatusEnum.submitted);
        receive.setUpdateTime(DateUtils.now());
        Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("task_id", taskId)
                .eq("receiver_id", receiverId);
        Integer result = taskReceiveDao.update(receive, wrapper);
        if (result != null && result > 0) {
            updateTaskStatus(taskId, TaskStatusEnum.submitted);

            ThreadPoolUtils.execute(() -> {
                //推送消息给任务创建人
                /*MemberDto receiver = memberService.getMember(receiverId);
                TaskDto task = baseMapper.getTask(taskId);
                JSONObject extras = new JSONObject();
                extras.put("businessCode", "4");//提交任务
                extras.put("taskId", task.getId());
                extras.put("taskTitle", task.getTitle());
                extras.put("taskReceiverId", receiver.getId());
                extras.put("taskReceiverAvatar", receiver.getAvatar());
                extras.put("taskReceiverNickName", receiver.getNickName());
                extras.put("taskCreatorId", task.getCreator().getId());
                logger.info("推送消息给任务发布人，extras=" + extras.toJSONString());
                rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_NAME, extras.toJSONString());*/
            });
        }

    }

    @Override
    @Transactional
    public void completeTask(Long receiverId, Long taskId) {
        boolean isCompletable = isCompletableTask(receiverId, taskId);
        if (!isCompletable) {
            throw new RRException("任务不可完成");
        }

        Wrapper<TaskEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("id", taskId);
        TaskEntity task = new TaskEntity();
        task.setStatus(TaskStatusEnum.completed);
        task.setCompleteTime(DateUtils.now());
        Integer result = baseMapper.update(task, wrapper);
        if (result != null && result > 0) {
            ThreadPoolUtils.execute(() -> {
                //发送消息给任务领取人
                /*TaskDto taskDto = baseMapper.getTask(taskId);
                JSONObject extras = new JSONObject();
                extras.put("businessCode", "5");//确认任务完成
                extras.put("taskId", taskDto.getId());
                extras.put("taskTitle", taskDto.getTitle());
                extras.put("taskVirtualCurrency", taskDto.getVirtualCurrency());
                extras.put("taskCreatorId", taskDto.getCreator().getId());
                extras.put("taskCreatorAvatar", taskDto.getCreator().getAvatar());
                extras.put("taskCreatorNickName", taskDto.getCreator().getNickName());
                extras.put("taskReceiverId", receiverId);
                logger.info("发送消息给任务领取人，extras=" + extras.toJSONString());
                rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_NAME, extras.toJSONString());*/

                //给领取人添加标签
                addTag2Member(receiverId, taskId);
            });
        }

    }

    //给任务领取人添加技能标签
    private void addTag2Member(Long receiverId, Long taskId) {
        List<TaskTagEntity> tags = taskTagService.getTagsByTaskId(taskId);
        if (!CollectionUtils.isEmpty(tags)) {
            List<Long> tagIds = tags.stream().map(TaskTagEntity::getId).collect(Collectors.toList());
            Wrapper<MemberTagRelationEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("member_id", receiverId)
                    .in("tag_id", tagIds);
            List<MemberTagRelationEntity> relations = memberTagRelationService.selectList(wrapper);
            if (!CollectionUtils.isEmpty(relations)) {
                for (MemberTagRelationEntity relation : relations) {
                    relation.setUsageCount(relation.getUsageCount() + 1);
                    tagIds.remove(relation.getTagId());
                }
                memberTagRelationService.updateBatchById(relations);
            }
            if (!CollectionUtils.isEmpty(tagIds)) {
                List<MemberTagRelationEntity> batchRantions = new ArrayList<>();
                for (Long tagId : tagIds) {
                    MemberTagRelationEntity relation = new MemberTagRelationEntity();
                    relation.setTagId(tagId);
                    relation.setMemberId(receiverId);
                    relation.setUsageCount(1);
                    batchRantions.add(relation);
                }
                memberTagRelationService.insertBatch(batchRantions);
            }
        }
    }


    //任务-图片关系
    private void addTaskImageRelation(Long taskId, List<String> imageUrls) {
        if (!CollectionUtils.isEmpty(imageUrls)) {
            baseMapper.insertTaskImageRelation(taskId, imageUrls);
        }
    }

    //任务-标签关系
    private void addTaskTagRelation(Long taskId, List<Long> tagIds) {
        if (!CollectionUtils.isEmpty(tagIds)) {
            baseMapper.insertTaskTagRelation(taskId, tagIds);
        }
    }

    //任务-提示用户关系
/*    private void addTaskNotifiedUserRelation(Long taskId, List<Long> userIds) {
        if (!CollectionUtils.isEmpty(userIds)) {
            baseMapper.insertTaskNotifiedUserRelation(taskId, userIds);
        }
    }*/

    private boolean isReceiveableTask(Long receiverId, Long taskId) {
        int count = baseMapper.isReceiveableTask(receiverId, taskId);
        return count > 0;
    }

    private boolean isChooseableReceiver(Long taskId, Long receiverId) {
        int count = baseMapper.isChooseableReceiver(taskId, receiverId);
        return count > 0;
    }

    private boolean isSubmitableTask(Long receiverId, Long taskId) {
        int count = baseMapper.isSubmitableTask(receiverId, taskId);
        return count > 0;
    }

    private boolean isCompletableTask(Long receiverId, Long taskId) {
        int count = baseMapper.isCompletableTask(receiverId, taskId);
        return count > 0;
    }


    //更新任务状态
    private void updateTaskStatus(Long taskId, TaskStatusEnum status) {
        TaskEntity task = new TaskEntity();
        task.setStatus(status);
        Wrapper<TaskEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("id", taskId);
        baseMapper.update(task, wrapper);
    }
}