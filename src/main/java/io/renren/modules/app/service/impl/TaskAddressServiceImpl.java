package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.task.TaskAddressDao;
import io.renren.modules.app.dao.task.TaskDao;
import io.renren.modules.app.entity.task.TaskAddressEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.TaskAddressForm;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.service.TaskAddressService;
import io.renren.modules.app.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class TaskAddressServiceImpl extends ServiceImpl<TaskAddressDao, TaskAddressEntity> implements TaskAddressService {
    private final static Logger logger = LoggerFactory.getLogger(TaskAddressServiceImpl.class);


    @Override
    public void createAddress(Long creatorId, TaskAddressForm form) {
        ValidatorUtils.validateEntity(form);
        TaskAddressEntity address = new TaskAddressEntity();
        BeanUtils.copyProperties(form, address);
        address.setCreatorId(creatorId);
        address.setCreateTime(DateUtils.now());
        this.insert(address);

    }

    @Override
    public TaskAddressEntity getAddress(Long id) {
        return this.selectById(id);
    }

    @Override
    public List<TaskAddressEntity> getAddresses(Long creatorId) {
        Wrapper<TaskAddressEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("creator_id", creatorId)
                .eq("deleted", false)
                .orderBy("create_time", false);
        List<TaskAddressEntity> addresses = selectList(wrapper);
        if (CollectionUtils.isEmpty(addresses)) {
            return new ArrayList<>();
        }
        return addresses;
    }

    @Override
    public void updateAddress(TaskAddressForm form) {
        ValidatorUtils.validateEntity(form);
        TaskAddressEntity address = new TaskAddressEntity();
        BeanUtils.copyProperties(form, address);
        updateById(address);
    }

    @Override
    public void deleteAddress(Long id) {
        TaskAddressEntity address = this.selectById(id);
        if (address != null) {
            address.setDeleted(true);
            updateById(address);
        }
    }
}