package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.dto.TaskBannerDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.dto.TaskOrderDto;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.entity.task.TaskOrderEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.form.TaskQueryForm;

import java.util.List;

/**
 * 任务微信支付订单
 */
public interface TaskOrderService extends IService<TaskOrderEntity> {




    //分页获取任务订单列表
    PageUtils<TaskOrderDto> getTaskOrders(String tradeState,PageWrapper page);

    //任务订单总金额
    long sumTotalFee(String tradeState);
}

