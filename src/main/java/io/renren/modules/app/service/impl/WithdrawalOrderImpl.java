package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dao.task.WithdrawalOrderDao;
import io.renren.modules.app.dto.WithdrawalOrderDto;
import io.renren.modules.app.entity.task.WithdrawalOrderEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.MemberWalletLogService;
import io.renren.modules.app.service.MemberWalletService;
import io.renren.modules.app.service.TaskOrderService;
import io.renren.modules.app.service.WithdrawalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Service
public class WithdrawalOrderImpl extends ServiceImpl<WithdrawalOrderDao, WithdrawalOrderEntity> implements WithdrawalOrderService {

    @Autowired
    private MemberWalletService memberWalletService;
    @Autowired
    private MemberWalletLogService memberWalletLogService;
    @Autowired
    private TaskOrderService taskOrderService;

    @Override
    public PageUtils<WithdrawalOrderDto> getWithdrawalOrders(PageWrapper page) {
        List<WithdrawalOrderDto> orders = baseMapper.getWithdrawalOrders(page);
        if (CollectionUtils.isEmpty(orders)) {
            return new PageUtils<>();
        }
        int total = baseMapper.count();
        return new PageUtils<>(orders, total, page.getPageSize(), page.getCurrPage());
    }

}
