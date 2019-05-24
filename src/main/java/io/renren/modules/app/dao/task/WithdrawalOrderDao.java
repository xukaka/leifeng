package io.renren.modules.app.dao.task;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.WithdrawalOrderDto;
import io.renren.modules.app.entity.task.TaskOrderEntity;
import io.renren.modules.app.entity.task.WithdrawalOrderEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 提现订单
 */
@Mapper
public interface WithdrawalOrderDao extends BaseMapper<WithdrawalOrderEntity> {

    //分页获取提现订单列表
    List<WithdrawalOrderDto> getWithdrawalOrders(@Param("page") PageWrapper page);
    //获取提现订单总数
    int count();
}
