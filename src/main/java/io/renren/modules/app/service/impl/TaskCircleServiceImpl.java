package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.task.TaskCircleDao;
import io.renren.modules.app.dao.task.TaskCircleMemberDao;
import io.renren.modules.app.dto.TaskCircleDto;
import io.renren.modules.app.entity.task.TaskCircleEntity;
import io.renren.modules.app.entity.task.TaskCircleMemberEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskCircleForm;
import io.renren.modules.app.service.TaskCircleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class TaskCircleServiceImpl extends ServiceImpl<TaskCircleDao, TaskCircleEntity> implements TaskCircleService {
    private final static Logger logger = LoggerFactory.getLogger(TaskCircleServiceImpl.class);


    @Resource
    private TaskCircleMemberDao taskCircleMemberDao;

    @Override
    public void createCircle(Long creatorId, TaskCircleForm form) {
        ValidatorUtils.validateEntity(form);
        TaskCircleEntity circle = new TaskCircleEntity();
        BeanUtils.copyProperties(form, circle);
        circle.setCreatorId(creatorId);
        circle.setCreateTime(DateUtils.now());
        this.insert(circle);
    }

    @Override
    public TaskCircleDto getCircle(Long id) {
        TaskCircleDto circle = baseMapper.getCircle(id);
        return circle;
    }

    @Override
    public void updateCircle(TaskCircleForm form) {
        ValidatorUtils.validateEntity(form);
        TaskCircleEntity circle = this.selectById(form.getId());
        if (circle != null) {
            circle.setAvatar(form.getAvatar());
            circle.setDescription(form.getDescription());
            circle.setName(form.getName());
            circle.setNeedReview(form.getNeedReview());
            this.updateById(circle);
        }
    }

    @Override
    public void dismissCircle(Long id) {
        TaskCircleEntity circle = this.selectById(id);
        if (circle != null) {
            circle.setDeleted(true);
            this.updateById(circle);
        }
    }

    @Override
    public PageUtils<TaskCircleDto> getCircles(String circleName, PageWrapper page) {
        List<TaskCircleDto> circles = baseMapper.getCircles(circleName,page);
        if (CollectionUtils.isEmpty(circles)) {
            return new PageUtils<>();
        }
        int total = baseMapper.count(circleName,page);
        return new PageUtils<>(circles, total, page.getPageSize(), page.getCurrPage());
    }


    @Override
    public void joinCircle(Long currentUserId, Long circleId) {
        boolean exists = existsCircleMember(currentUserId, circleId);
        if (!exists) {
            TaskCircleMemberEntity circleMember = new TaskCircleMemberEntity(DateUtils.now(), circleId, currentUserId);
            taskCircleMemberDao.insert(circleMember);
        }
    }

    @Override
    public void exitCircle(Long currentUserId, Long circleId) {
        Wrapper<TaskCircleMemberEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id", currentUserId)
                .eq("circle_id", circleId);
        taskCircleMemberDao.delete(wrapper);
    }


    //成员是否已在圈中
    private boolean existsCircleMember(Long currentUserId, Long circleId) {
        Wrapper<TaskCircleMemberEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id", currentUserId)
                .eq("circle_id", circleId);
        int count = taskCircleMemberDao.selectCount(wrapper);
        return count > 0;
    }
}