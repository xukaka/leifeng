package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.task.TaskTagEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务标签
 */
@Mapper
public interface TaskTagDao extends BaseMapper<TaskTagEntity> {
    List<TaskTagEntity> getTagsByTaskId(@Param("taskId") Long taskId);
}
