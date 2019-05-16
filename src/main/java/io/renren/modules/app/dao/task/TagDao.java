package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.task.TagEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务标签
 */
@Mapper
public interface TagDao extends BaseMapper<TagEntity> {
    List<TagEntity> getTagsByTaskId(@Param("taskId") Long taskId);
}
