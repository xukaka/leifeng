package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskForm;

import java.util.List;
import java.util.Map;

/**
 * 任务
 */
public interface TaskService extends IService<TaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 分页获取发布的任务
     * @return
     */
    PageUtils<TaskDto> getPublishedTasks(Long publisherId, PageWrapper page);

    /**
     * 分页获取领取的任务
     * @return
     */
    PageUtils<TaskDto> getReceivedTasks(Long receiverId, PageWrapper page);


    /**
     * 获取任务-根据id
     */
    TaskDto getTask(Long id);

    /**
     * 创建任务
     */
    void createTask(Long creatorId,TaskForm form);

    /**
     * 更新任务
     */
    void updateTask(TaskForm form);

    /**
     * 批量删除任务-逻辑删除
     */
    void deleteTasks(Long[] ids);


    /**
     * 领取任务
     */
    void receiveTask(Long receiverId,Long taskId);

}

