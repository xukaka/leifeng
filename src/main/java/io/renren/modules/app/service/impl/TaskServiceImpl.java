package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.controller.task.TaskController;
import io.renren.modules.app.dao.task.TaskDao;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service("taskService")
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {
    private final static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TaskEntity> page = this.selectPage(
                new Query<TaskEntity>(params).getPage(),
                new EntityWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public TaskEntity getTask(Long id) {
        return this.selectById(id);
    }

    @Override
    public void createTask(TaskForm form) {
        ValidatorUtils.validateEntity(form);
        TaskEntity task = new TaskEntity();
        BeanUtils.copyProperties(form, task);
        task.setCreateTime(DateUtils.now());
        this.insert(task);
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
        }
        this.updateBatchById(tasks);
    }

}