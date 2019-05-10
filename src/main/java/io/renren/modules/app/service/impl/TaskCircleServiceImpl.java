package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.ThreadPoolUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.task.TaskCircleAuditDao;
import io.renren.modules.app.dao.task.TaskCircleDao;
import io.renren.modules.app.dao.task.TaskCircleMemberDao;
import io.renren.modules.app.dto.TaskCircleDto;
import io.renren.modules.app.entity.CircleAuditStatusEnum;
import io.renren.modules.app.entity.task.TaskCircleAuditEntity;
import io.renren.modules.app.entity.task.TaskCircleEntity;
import io.renren.modules.app.entity.task.TaskCircleMemberEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskCircleForm;
import io.renren.modules.app.form.TaskCircleUpdateForm;
import io.renren.modules.app.service.TaskCircleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TaskCircleServiceImpl extends ServiceImpl<TaskCircleDao, TaskCircleEntity> implements TaskCircleService {
    private final static Logger logger = LoggerFactory.getLogger(TaskCircleServiceImpl.class);


    @Resource
    private TaskCircleMemberDao taskCircleMemberDao;

    @Resource
    private TaskCircleAuditDao taskCircleAuditDao;

    @Override
    @Transactional
    public void createCircle(Long creatorId, TaskCircleForm form) {
        ValidatorUtils.validateEntity(form);
        TaskCircleEntity circle = new TaskCircleEntity();
        BeanUtils.copyProperties(form, circle);
        circle.setCreatorId(creatorId);
        circle.setCreateTime(DateUtils.now());
        this.insert(circle);
        //插入圈标签
        addCircleTagRelation(circle.getId(),form.getTagIds());
    }

    //添加圈-标签关系
    private void addCircleTagRelation(Long circleId, List<Long> tagIds) {
        if (!CollectionUtils.isEmpty(tagIds)) {
            baseMapper.insertCircleTagRelation(circleId, tagIds);
        }
    }



    @Override
    public TaskCircleDto getCircle(Long id) {
        TaskCircleDto circle = baseMapper.getCircle(id);
        return circle;
    }

    @Override
    @Transactional
    public void updateCircle(TaskCircleUpdateForm form) {
        ValidatorUtils.validateEntity(form);
        TaskCircleEntity circle = this.selectById(form.getId());
        if (circle != null) {
            circle.setAvatar(form.getAvatar());
            circle.setDescription(form.getDescription());
            circle.setName(form.getName());
            circle.setNeedReview(form.getNeedReview());
            this.updateById(circle);
            //更新圈的标签
            baseMapper.deleteCircleTagRelation(form.getId());
            baseMapper.insertCircleTagRelation(form.getId(),form.getTagIds());
        }
    }

    @Override
    public void dismissCircle(Long memberId,Long id) {
        TaskCircleEntity circle = this.selectById(id);
        if (circle != null && circle.getCreatorId().equals(memberId)) {
            circle.setDeleted(true);
            this.updateById(circle);
        }
    }

    @Override
    public PageUtils<TaskCircleDto> getCircles(String keyword, PageWrapper page) {
        List<TaskCircleDto> circles = baseMapper.getCircles(keyword, page);
        if (CollectionUtils.isEmpty(circles)) {
            return new PageUtils<>();
        }
        int total = baseMapper.count(keyword, page);
        return new PageUtils<>(circles, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public PageUtils<TaskCircleDto> getMyJoinedCircles(Long memberId, PageWrapper page) {
        List<TaskCircleDto> circles = baseMapper.getMyJoinedCircles(memberId, page);
        if (CollectionUtils.isEmpty(circles)) {
            return new PageUtils<>();
        }
        int total = baseMapper.myJoinedCount(memberId, page);
        return new PageUtils<>(circles, total, page.getPageSize(), page.getCurrPage());
    }


    @Override
    public Map<String, Object> joinCircle(Long memberId, Long circleId) {
        Map<String, Object> result = new HashMap<>();
        boolean exists = existsCircleMember(memberId, circleId);
        if (exists) {
            result.put("status", 2);
            result.put("msg", "你已是圈成员");
            return result;
        }
        TaskCircleEntity circle = baseMapper.selectById(circleId);
        if (circle.getNeedReview()) {
            //进入审核流程
            TaskCircleAuditEntity audit = new TaskCircleAuditEntity();
            audit.setApplicantId(memberId);
            audit.setAuditorId(circle.getCreatorId());
            audit.setCircleId(circleId);
            audit.setStatus(CircleAuditStatusEnum.UNAUDITED);
            audit.setCreateTime(DateUtils.now());
            taskCircleAuditDao.insert(audit);
            //TODO 推消息给圈主审核

            result.put("status", 1);
            result.put("msg", "已申请加入圈，待圈主审核");
            return result;
        } else {
            addCircleMember(circleId,memberId);
            //圈人数+1
            baseMapper.incCircleMemberCount(circleId,1);
            result.put("status", 0);
            result.put("msg", "加入圈成功");
            return result;

        }
    }

    @Override
    @Transactional
    public void exitCircle(Long memberId, Long circleId) {
        Wrapper<TaskCircleMemberEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id", memberId)
                .eq("circle_id", circleId);
        taskCircleMemberDao.delete(wrapper);
        //圈人数-1
        baseMapper.incCircleMemberCount(circleId,-1);
    }

    @Override
    @Transactional
    public void audit(Long auditId, CircleAuditStatusEnum status) {
        TaskCircleAuditEntity audit = taskCircleAuditDao.selectById(auditId);
        updateAuditStatus(auditId,status);
        if (status == CircleAuditStatusEnum.AGREED){
            addCircleMember(audit.getCircleId(),audit.getApplicantId());
            //圈人数+1
            baseMapper.incCircleMemberCount(audit.getCircleId(),1);
        }
        //TODO 推送消息给申请人 审核结果：同意/拒绝
    }

    //新增圈成员
    private void addCircleMember(Long circleId,Long memberId) {
        TaskCircleMemberEntity circleMember = new TaskCircleMemberEntity(DateUtils.now(), circleId, memberId);
        taskCircleMemberDao.insert(circleMember);
    }

    //更新审核状态
    private void updateAuditStatus(Long auditId, CircleAuditStatusEnum status){
        Wrapper<TaskCircleAuditEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("id", auditId);
        TaskCircleAuditEntity audit = new TaskCircleAuditEntity();
        audit.setStatus(status);
        taskCircleAuditDao.update(audit,wrapper);
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