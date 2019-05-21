package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dao.task.TaskOrderDao;
import io.renren.modules.app.dto.TaskOrderDto;
import io.renren.modules.app.entity.task.TaskOrderEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.TaskOrderService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class TaskOrderImpl extends ServiceImpl<TaskOrderDao, TaskOrderEntity> implements TaskOrderService {


    @Override
    public PageUtils<TaskOrderDto> getTaskOrders(String tradeState, PageWrapper page) {
        List<TaskOrderDto> orders = baseMapper.getTaskOrders( tradeState,page);
        if (CollectionUtils.isEmpty(orders)) {
            return new PageUtils<>();
        }
        int total = baseMapper.count(tradeState);
        return new PageUtils<>(orders, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public long sumTotalFee(String tradeState) {
       return baseMapper.sumTotalFee(tradeState);
    }
}
