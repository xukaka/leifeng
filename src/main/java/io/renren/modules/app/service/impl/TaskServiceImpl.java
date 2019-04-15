package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.*;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.task.TaskDao;
import io.renren.modules.app.dao.task.TaskReceiveDao;
import io.renren.modules.app.dto.TaskBannerDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.TaskDifficultyEnum;
import io.renren.modules.app.entity.TaskStatusEnum;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.task.TaskAddressEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.entity.task.TaskReceiveEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.form.TaskQueryForm;
import io.renren.modules.app.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {
    private final static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Resource
    private TaskReceiveDao taskReceiveDao;

    @Resource
    private RabbitMqHelper rabbitMqHelper;

    @Resource
    private RedisUtils redisUtils;

    private static final long TEN_MINUTES = 60 * 10;

  /*  @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TaskEntity> page = this.selectPage(
                new Query<TaskEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }*/

    @Override
    public List<TaskBannerDto> getTaskBanners() {
   /*     List<TaskBannerDto> banners = redisUtils.getList(RedisKeys.BANNER_KEY, TaskBannerDto.class);
        if (CollectionUtils.isEmpty(banners)) {
            banners = this.baseMapper.getTaskBanners();
            redisUtils.addList(RedisKeys.BANNER_KEY, banners, TEN_MINUTES);
        }
        return banners;*/

        List<TaskBannerDto>    banners = this.baseMapper.getTaskBanners();
        if (CollectionUtils.isEmpty(banners)) {
           return new ArrayList<>();

        }
        return banners;

    }

    @Override
    public PageUtils<TaskDto> searchTasks(TaskQueryForm form, PageWrapper page) {
        Map<String, Object> queryMap = getTaskQueryMap(form);
        List<TaskDto> tasks = this.baseMapper.searchTasks(queryMap, page);
        if (CollectionUtils.isEmpty(tasks)) {
            return new PageUtils<>();
        }

        setTastDistance(form, tasks);

        int total = this.baseMapper.count(queryMap);
        return new PageUtils<>(tasks, total, page.getPageSize(), page.getCurrPage());
    }

    //计算距离
    private void setTastDistance(TaskQueryForm form, List<TaskDto> tasks) {
        for (TaskDto task : tasks) {
            TaskAddressEntity address = task.getAddress();
            long  distance= GeoUtils.getDistance(form.getLatitude(), form.getLongitude(), address.getLatitude(), address.getLongitude());
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
        List<TaskDto> tasks = this.baseMapper.getPublishedTasks(publisherId, page);
        if (CollectionUtils.isEmpty(tasks)) {
            return new PageUtils<>();
        }
        int total = this.baseMapper.publishCount(publisherId);
        return new PageUtils<>(tasks, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public PageUtils<TaskDto> getReceivedTasks(Long receiverId, PageWrapper page) {
        List<TaskDto> tasks = this.baseMapper.getReceivedTasks(receiverId, page);
        if (CollectionUtils.isEmpty(tasks)) {
            return new PageUtils<>();
        }
        int total = this.baseMapper.receiveCount(receiverId);
        return new PageUtils<>(tasks, total, page.getPageSize(), page.getCurrPage());
    }


    @Override
    public TaskDto getTask(Long id) {
        TaskDto task = this.baseMapper.getTask(id);
        if (task != null) {
            task.setCurSystemTime(DateUtils.now());
        }
        return task;
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
        this.insert(task);
        this.addTaskImageRelation(task.getId(), form.getImageUrls());
        this.addTaskTagRelation(task.getId(), form.getTagIds());
        this.addTaskNotifiedUserRelation(task.getId(), form.getNotifiedUserIds());
    }


    @Override
    public void updateTask(TaskForm form) {
        ValidatorUtils.validateEntity(form);
        TaskEntity task = new TaskEntity();
        BeanUtils.copyProperties(form, task);
        this.updateById(task);
    }

    @Override
    public void deleteTask(Long id) {
        TaskEntity task = this.selectById(id);
        if (task == null || task.getStatus() == TaskStatusEnum.received || task.getStatus() == TaskStatusEnum.submitted) {
            throw new RRException("任务已领取");
        }
        task.setDeleted(true);
        this.updateById(task);
    }


    /**
     * 领取任务，返回领取人信息
     */
    @Override
    @Transactional
    public Member receiveTask(Long receiverId, Long taskId) {
        TaskReceiveEntity receive = new TaskReceiveEntity(DateUtils.now(), receiverId, taskId);
        TaskEntity task = this.selectById(taskId);
      /*  boolean is = task==null;
        boolean a = task.getStatus() != TaskStatusEnum.published;
        logger.info(is +","+","+task.getDeleted());*/
        if (task == null
                || task.getStatus() != TaskStatusEnum.published
                || task.getDeleted()) {
            throw new RRException("任务已被领取");
        }
        task.setStatus(TaskStatusEnum.received);
        this.updateById(task);
        taskReceiveDao.insert(receive);
        return taskReceiveDao.getReceiver(receive.getId());
    }

    @Override
    @Transactional
    public void submitTask(Long receiverId, Long taskId) {
        boolean isSubmitable = this.baseMapper.isSubmitableTask(receiverId, taskId);
        if (!isSubmitable) {
            throw new RRException("任务不可提交");
        }
        TaskEntity task = this.selectById(taskId);
        if (task != null) {
            task.setStatus(TaskStatusEnum.submitted);
            this.updateById(task);

            ThreadPoolUtils.execute(() -> {
                //TODO 发送消息给任务创建人
                rabbitMqHelper.sendMessage("test", "132");
            });
        }
    }

    @Override
    @Transactional
    public void completeTask(Long receiverId, Long taskId) {
        boolean isCompletable = this.baseMapper.isCompletableTask(receiverId, taskId);
        if (!isCompletable) {
            throw new RRException("任务不可完成");
        }
        TaskEntity task = this.selectById(taskId);
        if (task != null) {
            task.setStatus(TaskStatusEnum.completed);
            this.updateById(task);

            ThreadPoolUtils.execute(() -> {
                //TODO 发送消息给任务领取人
            });
        }
    }


/*

    @Override
    @Transactional
    public void completeTask(Long receiverId, Long taskId) {
        TaskReceiveEntity receive = new TaskReceiveEntity(DateUtils.now(), receiverId, taskId);
        TaskEntity task = this.selectById(taskId);
        if (task == null
                || task.getStatus() != 1//非领取状态
                || task.getDeleted()) {
            throw new RRException("任务已被领取");
        }
        task.setStatus(1);//设置为领取状态
        this.updateById(task);
        taskReceiveDao.insert(receive);
    }
*/


    //任务-图片关系
    private void addTaskImageRelation(Long taskId, List<String> imageUrls) {
        if (!CollectionUtils.isEmpty(imageUrls)) {
            this.baseMapper.insertTaskImageRelation(taskId, imageUrls);
        }
    }

    //任务-标签关系
    private void addTaskTagRelation(Long taskId, List<Long> tagIds) {
        if (!CollectionUtils.isEmpty(tagIds)) {
            this.baseMapper.insertTaskTagRelation(taskId, tagIds);
        }
    }


    //任务-提示用户关系
    private void addTaskNotifiedUserRelation(Long taskId, List<Long> userIds) {
        if (!CollectionUtils.isEmpty(userIds)) {
            this.baseMapper.insertTaskNotifiedUserRelation(taskId, userIds);
        }
    }

}