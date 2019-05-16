package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.task.TagEntity;

import java.util.List;
import java.util.Map;

/**
 * 任务标签
 */
public interface TagService extends IService<TagEntity> {


    /**
     * 获取所有标签列表
     */
    List<TagEntity> getAllTags();

    /**
     * 创建标签
     */
    void createTag(String tagName);

    /**
     * 更新标签
     */
    void updateTag(Long tagId, String tagName);

    /**
     * 删除标签
     */
    void deleteTag(Long tagId);

    /**
     * 分页获取标签
     */
    PageUtils<TagEntity> getTags(Map<String,Object> pageMap);

    /**
     * 获取标签列表-根据任务id
     */
    List<TagEntity> getTagsByTaskId(Long taskId);


}

