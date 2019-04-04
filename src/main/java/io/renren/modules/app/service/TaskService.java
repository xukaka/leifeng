package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.TaskForm;

import java.util.List;
import java.util.Map;

/**
 * 任务
 */
public interface TaskService extends IService<TaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取任务-根据id
     */
    TaskEntity getTask(Long id);

    /**
     * 创建任务
     * @param form
     */
    void createTask(TaskForm form);

    /**
     * 更新任务消息
     * @param form
     */
    void updateTask(TaskForm form);

    /**
     * 批量删除任务-逻辑删除
     * @param ids
     */
    void deleteTasks(Long[] ids);

    /**
     * 批量插入任务图片
     * @param taskId
     * @param imageUrls
     */
    void insertTaskImages(Long taskId, List<String> imageUrls);

    void insertTaskTags(Long taskId,List<Long> tagIds);

    void insertTaskNotifiedUsers(Long taskId,List<Long> userIds);
}

