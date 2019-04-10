package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.task.TaskAddressEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.TaskAddressForm;
import io.renren.modules.app.form.TaskForm;

import java.util.List;
import java.util.Map;

/**
 * 任务地址
 */
public interface TaskAddressService extends IService<TaskAddressEntity> {
    /**
     * 创建任务地址
     */
    void createAddress(Long creatorId,TaskAddressForm form);

    /**
     * 获取任务地址-根据id
     */
    TaskAddressEntity getAddress(Long id);

    /**
     * 获取用户的所有地址列表
     */
    List<TaskAddressEntity> getAddresses(Long creatorId);


    /**
     * 更新任务地址
     */
    void updateAddress(TaskAddressForm form);

    /**
     * 删除任务地址-逻辑删除
     */
    void deleteAddress(Long id);

}

