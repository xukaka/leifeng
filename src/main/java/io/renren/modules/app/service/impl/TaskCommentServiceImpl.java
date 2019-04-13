package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.task.TaskCommentDao;
import io.renren.modules.app.dao.task.TaskCommentReplyDao;
import io.renren.modules.app.dao.task.TaskDao;
import io.renren.modules.app.dto.TaskCommentDto;
import io.renren.modules.app.dto.TaskCommentReplyDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.story.MsgCommentEntity;
import io.renren.modules.app.entity.task.TaskCommentEntity;
import io.renren.modules.app.entity.task.TaskCommentReplyEntity;
import io.renren.modules.app.entity.task.TaskEntity;
import io.renren.modules.app.form.PageForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.form.TaskCommentReplyForm;
import io.renren.modules.app.form.TaskForm;
import io.renren.modules.app.service.TaskCommentService;
import io.renren.modules.app.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TaskCommentServiceImpl extends ServiceImpl<TaskCommentDao, TaskCommentEntity> implements TaskCommentService {
    private final static Logger logger = LoggerFactory.getLogger(TaskCommentServiceImpl.class);

    @Resource
    private TaskCommentReplyDao taskCommentReplyDao;

    @Override
    public PageUtils<TaskCommentDto> getComments(Long taskId, PageWrapper page) {
        List<TaskCommentDto> comments = this.baseMapper.getComments(taskId, page);
        if (CollectionUtils.isEmpty(comments)) {
            return new PageUtils<>();
        }
        setCommentRepies(comments);
        int total = this.baseMapper.count(taskId);
        return new PageUtils<>(comments, total, page.getPageSize(), page.getCurrPage());
    }

    private void setCommentRepies(List<TaskCommentDto> comments) {
        for (TaskCommentDto comment : comments){
            List<TaskCommentReplyDto> replies = getCommentReplies(comment.getId());
            comment.setReplies(replies);
        }
    }


    @Override
    public void addComment(Long taskId, Long commentatorId, String content) {
        TaskCommentEntity comment = new TaskCommentEntity(DateUtils.now(), commentatorId, taskId, content);
        this.insert(comment);
    }

    @Override
    public void deleteComment(Long id) {
        TaskCommentEntity comment = this.selectById(id);
        if (comment != null) {
            comment.setDeleted(true);
            this.updateById(comment);
        }
    }

    @Override
    public void addCommentReply(TaskCommentReplyForm form) {
        ValidatorUtils.validateEntity(form);
        TaskCommentReplyEntity reply = new TaskCommentReplyEntity();
        BeanUtils.copyProperties(form, reply);
        reply.setCreateTime(DateUtils.now());
        taskCommentReplyDao.insert(reply);
    }

    @Override
    public void deleteCommentReply(Long replyId) {
        TaskCommentReplyEntity reply = taskCommentReplyDao.selectById(replyId);
        if (reply != null) {
            reply.setDeleted(true);
            taskCommentReplyDao.deleteById(reply);
        }

    }


    private List<TaskCommentReplyDto> getCommentReplies(Long commentId) {
        List<TaskCommentReplyDto> replies = taskCommentReplyDao.getCommentReplies(commentId);
        if (CollectionUtils.isEmpty(replies)) {
            return new ArrayList<>();
        }
        return replies;
    }

}