package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.TaskBannerDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.form.TaskQueryForm;

import java.util.List;

/**
 * 任务
 */
public interface TaskService extends IService<TaskEntity> {

    /**
     * 获取任务完成banner列表（已完成任务滚动列表）
     * @return 最新完成时间top20
     */
    List<TaskBannerDto> getTaskBanners();
    /**
     * 分页搜索任务-根据搜索条件
     * @param form
     * @param page
     * @return
     */
    PageUtils<TaskDto> searchTasks(TaskQueryForm form, PageWrapper page);
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
     * 删除任务-逻辑删除
     */
    void deleteTask(Long id);

    /**
     * 领取任务
     */
    void receiveTask(Long receiverId,Long taskId);

    /**
     * 提交任务（申请完成任务）
     */
    void submitTask(Long receiverId,Long taskId);

    /**
     * 完成任务
     */
    void completeTask(Long receiverId,Long taskId);

}

