package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.TaskOrderDto;
import io.renren.modules.app.entity.task.TaskOrderEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务微信支付订单
 */
@Mapper
public interface TaskOrderDao extends BaseMapper<TaskOrderEntity> {

    //分页获取任务订单列表
    List<TaskOrderDto> getTaskOrders(@Param("tradeState") String tradeState,@Param("page") PageWrapper page);

    //获取任务订单总数
    int count(@Param("tradeState") String tradeState);

    //任务订单总金额
    long sumTotalFee(@Param("tradeState") String tradeState);
}
