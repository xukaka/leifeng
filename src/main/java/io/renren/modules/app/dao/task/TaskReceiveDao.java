package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.task.TaskReceiveEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务领取
 */
@Mapper
public interface TaskReceiveDao extends BaseMapper<TaskReceiveEntity> {


}
