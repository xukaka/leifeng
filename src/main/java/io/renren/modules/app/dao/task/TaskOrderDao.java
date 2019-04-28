package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.task.TaskOrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务微信支付订单
 */
@Mapper
public interface TaskOrderDao extends BaseMapper<TaskOrderEntity> {

}
