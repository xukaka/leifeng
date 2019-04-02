package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.task.TaskEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务
 */
@Mapper
public interface TaskDao extends BaseMapper<TaskEntity> {
	
}
