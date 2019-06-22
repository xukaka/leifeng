package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.*;
import io.renren.common.validator.ValidatorUtils;
import io.renren.config.RabbitMQConfig;
import io.renren.modules.app.dao.member.MemberFollowDao;
import io.renren.modules.app.dao.task.TaskDao;
import io.renren.modules.app.dao.task.TaskReceiveDao;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.dto.TaskBannerDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.LikeTypeEnum;
import io.renren.modules.app.entity.TaskDifficultyEnum;
import io.renren.modules.app.entity.TaskStatusEnum;
import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.entity.member.MemberTagRelationEntity;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.pay.MemberWalletLogEntity;
import io.renren.modules.app.entity.task.*;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.form.TaskQueryForm;
import io.renren.modules.app.service.*;
import io.renren.modules.app.utils.WXPayConstants;
import io.renren.modules.app.utils.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private LikeService likeService;
    @Resource
    private TagService taskTagService;
    @Resource
    private TaskOrderService taskOrderService;
    @Resource
    private MemberTagRelationService memberTagRelationService;
    @Resource
    private MemberWalletService memberWalletService;
    @Resource
    private MemberWalletLogService memberWalletLogService;
    @Resource
    private MemberService memberService;
    @Resource
    private RabbitMqHelper rabbitMqHelper;
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private MemberFollowDao memberFollowDao;


    @Autowired
    private WechatPayService wxPayService;

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
            if (form.getLongitude() != null
                    && form.getLatitude() != null
                    && address.getLatitude() != null
                    && address.getLongitude() != null) {
                distance = GeoUtils.getDistance(form.getLatitude(), form.getLongitude(), address.getLatitude(), address.getLongitude());
            }
            task.setDistance(distance);
        }
    }

    private Map<String, Object> getTaskQueryMap(TaskQueryForm form) {
        Map<String, Object> queryMap = new HashMap<>();
        if (form.getCircleId() == null) {//区分圈任务和普通任务
            if (form.getLatitude() != null && form.getLongitude() != null && form.getRaidus() != null) {
                Map<String, Double> aroundMap = GeoUtils.getAround(form.getLatitude(), form.getLongitude(), form.getRaidus());
                queryMap.putAll(aroundMap);
            }
        } else {
            queryMap.put("circleId", form.getCircleId());
        }
        if (!StringUtils.isEmpty(form.getKeyword())) {
            queryMap.put("keyword", form.getKeyword());
        }
        if (!CollectionUtils.isEmpty(form.getTagIds())) {
            queryMap.put("tagIds", form.getTagIds());
        }
        if (form.getTaskDifficulty() != null) {
            TaskDifficultyEnum difficulty = form.getTaskDifficulty();
            queryMap.put("difficulty", difficulty.name());
            switch (difficulty) {
                case SIMPLE:
                    queryMap.put("minMoney", difficulty.getMinMoney());
                    queryMap.put("maxMoney", difficulty.getMaxMoney());
                    break;
                case NORMAL:
                    queryMap.put("minMoney", difficulty.getMinMoney());
                    queryMap.put("maxMoney", difficulty.getMaxMoney());
                    break;
                case DIFFICULT:
                    queryMap.put("minMoney", difficulty.getMinMoney());
                default:
                    break;
            }
        }
        return queryMap;
    }

    @Override
    public PageUtils<TaskDto> getPublishedTasks(Long publisherId,  TaskStatusEnum status,PageWrapper page) {
        List<TaskDto> tasks = baseMapper.getPublishedTasks(publisherId,status, page);
        if (CollectionUtils.isEmpty(tasks)) {
            return new PageUtils<>();
        }
        int total = baseMapper.publishCount(publisherId,status);
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
            boolean isReceived = existsRecevier(id, curMemberId);
            task.setReceived(isReceived);
            //是否点赞
            boolean isLiked = likeService.existsLiked(id, LikeTypeEnum.task, curMemberId);
            task.setLiked(isLiked);
            //是否评分
            boolean isScored = memberService.isScored(id);
            task.setScored(isScored);
        }
        return task;
    }


    /**
     * 是否领取（是否存在领取列表中）
     *
     * @param taskId
     * @param receiverId
     * @return
     */
    private boolean existsRecevier(Long taskId, Long receiverId) {
        Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("task_id", taskId)
                .eq("receiver_id", receiverId);
        int count = taskReceiveDao.selectCount(wrapper);
        return count > 0;
    }


    @Override
    @Transactional
    public Long createTask(Long creatorId, TaskForm form) {
        ValidatorUtils.validateEntity(form);
        TaskEntity task = new TaskEntity();
        BeanUtils.copyProperties(form, task);
        task.setCreatorId(creatorId);
        task.setStatus(TaskStatusEnum.notpay);
        task.setCreateTime(DateUtils.now());
        task.setExperience(calTaskExperience(form.getMoney()));
        task.setIntegralValue(calTaskIntegralValue(form.getMoney()));
        this.insert(task);
        addTaskImageRelation(task.getId(), form.getImageUrls());
        addTaskTagRelation(task.getId(), form.getTagIds());
        return task.getId();
    }

    //计算任务经验值
    private int calTaskExperience(int money) {
        int taskExperience = 0;
        if (money <= TaskDifficultyEnum.SIMPLE.getMaxMoney()) {
            taskExperience = 1;
        } else if (money <= TaskDifficultyEnum.NORMAL.getMaxMoney()) {
            taskExperience = 2;
        } else if (money <= TaskDifficultyEnum.DIFFICULT.getMaxMoney()) {
            taskExperience = 3;
        }
        return taskExperience;
    }

    //计算任务积分值:积分值按照金额(1元=100分)的0.001计算
    private int calTaskIntegralValue(int money) {
        return (int)Math.round(money *0.001);
    }

    @Override
    public void publishTask(Long taskId) {
        TaskEntity task = this.selectById(taskId);
        updateTaskStatus(taskId, TaskStatusEnum.published);

        ThreadPoolUtils.execute(() -> {
            if (task.getCircleId() != null) {
                //todo 推送给圈内所有人
            } else {
                //推送消息给关注我的所有人
                rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_DYNAMIC, ImMessageUtils.getDynamicMsg(task.getCreatorId(), "task", task.getId()));
            }
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
        if (task == null) {
            throw new RRException("任务不存在");
        }
        if (task.getStatus() == TaskStatusEnum.executing || task.getStatus() == TaskStatusEnum.submitted) {
            throw new RRException("当前任务状态,不可删除", 100);
        }
        task.setDeleted(true);
        this.updateById(task);
    }

    @Override
    public PageUtils<MemberDto> getTaskReceivers(Long taskId, PageWrapper page) {
        List<MemberDto> members = baseMapper.getTaskReceivers(taskId, page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }

        setChooseStatus(taskId, members);

        int total = baseMapper.receiverCount(taskId);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }

    //是否可指派
    private void setChooseStatus(Long taskId, List<MemberDto> members) {
        TaskEntity task = this.selectById(taskId);
        boolean isChoosed = (task.getStatus() != TaskStatusEnum.published);
        for (MemberDto dto : members) {
            dto.setChoosed(isChoosed);
        }
    }

    /**
     * 领取任务
     */
    @Override
    @Transactional
    public void receiveTask(Long receiverId, Long taskId) {
        boolean isReceiveable = isReceiveableTask(receiverId, taskId);
        if (!isReceiveable) {
            throw new RRException("刷新任务状态...", 100);
        }

        long curTime = DateUtils.now();
        TaskReceiveEntity receive = new TaskReceiveEntity(curTime, curTime, receiverId, taskId, TaskStatusEnum.received);
        taskReceiveDao.insert(receive);

        ThreadPoolUtils.execute(() -> {
            TaskEntity task = this.selectById(taskId);
            //推送消息给任务发布人
            rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_TASK, ImMessageUtils.getTaskMsg(receiverId, task.getCreatorId(), taskId, "领取"));
        });
    }


    @Override
    @Transactional
    public void chooseTaskReceiver(Long taskId, Long memberId, Long receiverId) {
        boolean isChooseable = isChooseableReceiver(taskId, receiverId);
        if (!isChooseable) {
            throw new RRException("任务已开始，不能选择人了", 100);
        }

        TaskReceiveEntity receive = new TaskReceiveEntity();
        receive.setStatus(TaskStatusEnum.choosed);
        Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("task_id", taskId)
                .eq("receiver_id", receiverId);
        Integer result = taskReceiveDao.update(receive, wrapper);
        if (result != null && result > 0) {
            updateTaskStatus(taskId, TaskStatusEnum.received);
            rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_TASK, ImMessageUtils.getTaskMsg(memberId, receiverId, taskId, "指派"));

        }

    }


    @Override
    @Transactional
    public void executeTask(Long taskId, Long receiverId) {
        boolean isExecutable = isExecutableTask(taskId, receiverId);
        if (!isExecutable) {
            throw new RRException("刷新任务状态...", 100);
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
            TaskEntity task = selectById(taskId);
            rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_TASK, ImMessageUtils.getTaskMsg(receiverId, task.getCreatorId(), taskId, "执行"));
        }
    }


    @Override
    @Transactional
    public void cancelTaskByReceiver(Long receiverId, Long taskId) {
        TaskDto task = baseMapper.getTask(taskId);
        Member receiver = task.getReceiver();

        if (receiver != null
                && receiverId.equals(receiver.getId())
                && (task.getStatus() != TaskStatusEnum.published && task.getStatus() != TaskStatusEnum.received)) {
            throw new RRException("任务执行中，不能取消", 100);
        }
        Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("task_id", taskId)
                .eq("receiver_id", receiverId);
        taskReceiveDao.delete(wrapper);

        if (receiver != null && receiverId.equals(receiver.getId())) {
            updateTaskStatus(taskId, TaskStatusEnum.published);
        }
        ThreadPoolUtils.execute(() -> {
            rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_TASK, ImMessageUtils.getTaskMsg(receiverId, task.getCreator().getId(), taskId, "取消"));
        });
    }


    @Override
    @Transactional
    public void cancelTaskByPublisher(Long publisherId, Long taskId) {
        TaskDto task = baseMapper.getTask(taskId);
        if (publisherId == null || !publisherId.equals(task.getCreator().getId())
                || (task.getStatus() != TaskStatusEnum.published && task.getStatus() != TaskStatusEnum.received)) {
            throw new RRException("任务执行中，不能取消", 100);
        }
        updateTaskStatus(taskId, TaskStatusEnum.cancelled);
        updateReceiverTaskStatus(taskId);
        refund(taskId);

        ThreadPoolUtils.execute(() -> {
            Wrapper<TaskReceiveEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("task_id", taskId);
            List<TaskReceiveEntity> taskReceives = taskReceiveDao.selectList(wrapper);
            if (!CollectionUtils.isEmpty(taskReceives)) {
                for (TaskReceiveEntity taskReceive : taskReceives) {
                    rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_TASK, ImMessageUtils.getTaskMsg(publisherId, taskReceive.getReceiverId(), taskId, "取消"));
                }
            }
        });

    }

    //退款
    private void refund(Long taskId) {
        TaskOrderEntity torder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        if (torder == null) {
            throw new RRException("任务订单未生成");
        }

        if (!WXPayConstants.SUCCESS.equals(torder.getTradeState())) {
            throw new RRException("任务订单状态异常：TradeState=" + torder.getTradeState());
        }

        try {
            String refundData = wxPayService.refundRequest(torder.getTransactionId(), taskId, String.valueOf(torder.getTotalFee()));
            logger.info("退款接口微信返回结果：{}", refundData);

            Map<String, String> map = WXPayUtil.xmlToMap(refundData);
            if (WXPayConstants.SUCCESS.equals(map.get("return_code"))) {
                if (WXPayConstants.SUCCESS.equals(map.get("result_code"))) {
                    torder.setTradeState(WXPayConstants.REFUND);
                    taskOrderService.updateById(torder);
                } else {
                    logger.info(map.get("err_code") + ":" + map.get("err_code_des"));
                }
            } else {
                logger.info(map.get("return_msg"));
            }
        } catch (Exception e) {
            throw new RRException(e.toString());
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
        TaskEntity task = selectById(taskId);

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
            rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_TASK, ImMessageUtils.getTaskMsg(receiverId, task.getCreatorId(), taskId, "提交"));
        }
    }

    @Override
    @Transactional
    public void completeTask(Long curMemberId, Long receiverId, Long taskId) {
        TaskEntity task = selectById(taskId);
        boolean isCompletable = isCompletableTask(receiverId, taskId);
        if (!isCompletable || !task.getCreatorId().equals(curMemberId)) {
            throw new RRException("任务状态异常");
        }

        task.setStatus(TaskStatusEnum.completed);
        task.setCompleteTime(DateUtils.now());
        Integer result = baseMapper.updateById(task);

        //校验支付流水
        TaskOrderEntity order = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        if (order != null && WXPayConstants.SUCCESS.equals(order.getTradeState())) {

            //记录钱包金额变动日志
            MemberWalletEntity wallet = memberWalletService.selectOne(new EntityWrapper<MemberWalletEntity>().eq("member_id", receiverId));
            MemberWalletLogEntity log = new MemberWalletLogEntity();
            log.setMemberId(receiverId);
            log.setChangeMoney(order.getTotalFee());
            log.setMoney(wallet.getMoney() + order.getTotalFee());
            log.setType("taskIncome");//任务入账
            log.setOutTradeNo(order.getOutTradeNo());
            log.setRemark("任务入账");
            log.setCreateTime(DateUtils.now());
            memberWalletLogService.insert(log);
            //领取人钱包金额增加
            memberWalletService.incMoney(receiverId, order.getTotalFee());

            taskOrderService.updateById(order);
        } else {
            logger.info("任务订单状态异常，请联系客服");
            throw new RRException("任务订单状态异常，请联系客服");
        }

        if (result != null && result > 0) {
            ThreadPoolUtils.execute(() -> {
                //任务完成数+1
                memberService.incTaskCompleteCount(receiverId, 1);
                //任务经验值增加
                memberService.incMemberExperience(receiverId,task.getExperience());
                //任务积分值增加
                memberService.incMemberIntegralValue(receiverId,task.getIntegralValue());

                rabbitMqHelper.sendMessage(RabbitMQConfig.IM_QUEUE_TASK, ImMessageUtils.getTaskMsg(curMemberId, receiverId, taskId, "确认完成"));
                //给领取人添加标签
                addTag2Member(receiverId, taskId);
            });
        }

    }

    //给任务领取人添加技能标签
    private void addTag2Member(Long receiverId, Long taskId) {
        List<TagEntity> tags = taskTagService.getTagsByTaskId(taskId);
        if (!CollectionUtils.isEmpty(tags)) {
            List<Long> tagIds = tags.stream().map(TagEntity::getId).collect(Collectors.toList());
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
                List<MemberTagRelationEntity> batchRelations = new ArrayList<>();
                for (Long tagId : tagIds) {
                    MemberTagRelationEntity relation = new MemberTagRelationEntity();
                    relation.setTagId(tagId);
                    relation.setMemberId(receiverId);
                    relation.setUsageCount(1);
                    relation.setCreateTime(DateUtils.now());
                    batchRelations.add(relation);
                }
                memberTagRelationService.insertBatch(batchRelations);
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

    //是否已支付
    private boolean isPayedTask(Long taskId) {
        TaskOrderEntity torder = taskOrderService.selectOne(new EntityWrapper<TaskOrderEntity>().eq("task_id", taskId));
        TaskEntity task = selectById(taskId);
        return WXPayConstants.SUCCESS.equals(torder.getTradeState()) && task.getStatus() == TaskStatusEnum.payed;
    }


    //更新任务状态
    public void updateTaskStatus(Long taskId, TaskStatusEnum status) {
        TaskEntity task = new TaskEntity();
        task.setStatus(status);
        Wrapper<TaskEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("id", taskId);
        baseMapper.update(task, wrapper);
    }


    @Override
    public void incCommentCount(Long taskId, Integer inc) {
        baseMapper.incCommentCount(taskId, inc);
    }

    @Override
    public void incLikeCount(Long taskId, Integer inc) {
        baseMapper.incLikeCount(taskId, inc);
    }

    @Override
    public void incViewCount(Long taskId, Integer inc) {
        baseMapper.incViewCount(taskId, inc);
    }


}