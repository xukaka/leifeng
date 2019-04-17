package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.task.TaskTagEntity;
import io.renren.modules.app.form.PageWrapper;

import java.util.List;
import java.util.Map;

/**
 * 任务标签
 */
public interface TaskTagService extends IService<TaskTagEntity> {


    /**
     * 获取所有任务标签列表
     */
    List<TaskTagEntity> getAllTags();

    /**
     * 创建任务标签
     */
    void createTag(String tagName);

    /**
     * 更新任务标签
     */
    void updateTag(Long tagId, String tagName);

    /**
     * 删除任务标签
     */
    void deleteTag(Long tagId);

    /**
     * 分页获取任务标签
     * @param pageMap
     * @return
     */
    PageUtils<TaskTagEntity> getTags( Map<String,Object> pageMap);

    /**
     * 获取任务标签列表-根据任务id
     * @param taskId
     * @return
     */
    List<TaskTagEntity> getTagsByTaskId(Long taskId);


}

