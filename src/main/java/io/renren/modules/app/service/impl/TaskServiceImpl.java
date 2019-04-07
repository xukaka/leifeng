package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.task.TaskDao;
import io.renren.modules.app.dao.task.TaskReceiveDao;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.entity.task.TaskReceiveEntity;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {
    private final static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);


    @Autowired
    private TaskReceiveDao taskReceiveDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TaskEntity> page = this.selectPage(
                new Query<TaskEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public TaskDto getTask(Long id) {
        TaskDto task = this.baseMapper.getTask(id);
        return task;
    }

    @Override
    @Transactional
    public void createTask(TaskForm form) {
        ValidatorUtils.validateEntity(form);
        TaskEntity task = new TaskEntity();
        BeanUtils.copyProperties(form, task);
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
    public void deleteTasks(Long[] ids) {
        Wrapper<TaskEntity> wrapper = new EntityWrapper<>();
        wrapper.in("id", Arrays.asList(ids));
        List<TaskEntity> tasks = this.selectList(wrapper);
        if (!CollectionUtils.isEmpty(tasks)) {
            for (TaskEntity task : tasks) {
                task.setDeleted(true);
            }
            this.updateBatchById(tasks);
        }
    }

    @Override
    @Transactional
    public void receiveTask(Long receiverId, Long taskId) {
        TaskReceiveEntity receive = new TaskReceiveEntity(DateUtils.now(), receiverId, taskId);
        TaskEntity task = this.selectById(taskId);
        if (task == null
                || task.getStatus() != 0//非发布状态
                || task.getDeleted()) {
            throw new RRException("任务已被领取");
        }
        task.setStatus(1);//设置为领取状态
        this.updateById(task);
        taskReceiveDao.insert(receive);
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