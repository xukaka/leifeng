package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.WithdrawalOrderDto;
import io.renren.modules.app.entity.task.TaskOrderEntity;
import io.renren.modules.app.entity.task.WithdrawalOrderEntity;
import io.renren.modules.app.form.PageWrapper;

/**
 * 提现订单
 */
public interface WithdrawalOrderService extends IService<WithdrawalOrderEntity> {

    //分页获取提现订单列表
    PageUtils<WithdrawalOrderDto> getWithdrawalOrders(PageWrapper page);


}

