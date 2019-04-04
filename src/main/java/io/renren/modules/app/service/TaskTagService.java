package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.app.entity.task.TaskTagEntity;

import java.util.List;

/**
 * 任务标签
 */
public interface TaskTagService extends IService<TaskTagEntity> {


    /**
     * 获取所有任务标签列表
     */
    List<TaskTagEntity> getTaskTags();

    /**
     * 创建任务标签
     */
    void createTaskTag(String name);

    /**
     * 更新任务标签
     */
    void updateTaskTag(Long tagId, String name);



}

