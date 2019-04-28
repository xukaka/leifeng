package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.app.dao.task.TaskOrderDao;
import io.renren.modules.app.entity.task.TaskOrderEntity;
import io.renren.modules.app.service.TaskOrderService;
import org.springframework.stereotype.Service;


@Service("taskOrderService")
public class TaskOrderImpl extends ServiceImpl<TaskOrderDao, TaskOrderEntity> implements TaskOrderService {

}
