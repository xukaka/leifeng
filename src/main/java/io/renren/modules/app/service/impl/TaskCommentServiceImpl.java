package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dao.task.TaskCommentDao;
import io.renren.modules.app.dao.task.TaskCommentReplyDao;
import io.renren.modules.app.dto.TaskCommentDto;
import io.renren.modules.app.dto.TaskCommentReplyDto;
import io.renren.modules.app.entity.task.TaskCommentEntity;
import io.renren.modules.app.entity.task.TaskCommentReplyEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.TaskCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TaskCommentServiceImpl extends ServiceImpl<TaskCommentDao, TaskCommentEntity> implements TaskCommentService {
    private final static Logger logger = LoggerFactory.getLogger(TaskCommentServiceImpl.class);

    @Resource
    private TaskCommentReplyDao taskCommentReplyDao;

    @Override
    public PageUtils<TaskCommentDto> getComments(Long taskId, PageWrapper page) {
        List<TaskCommentDto> comments = baseMapper.getComments(taskId, page);
        if (CollectionUtils.isEmpty(comments)) {
            return new PageUtils<>();
        }
        setCommentRepies(comments);
        int total = baseMapper.count(taskId);
        return new PageUtils<>(comments, total, page.getPageSize(), page.getCurrPage());
    }

    private void setCommentRepies(List<TaskCommentDto> comments) {
        for (TaskCommentDto comment : comments) {
            List<TaskCommentReplyDto> replies = getCommentReplies(comment.getId());
            comment.setReplies(replies);
        }
    }


    @Override
    public void addComment(Long taskId, Long commentatorId, String content) {
        checkCommentParam(taskId, commentatorId, content);
        TaskCommentEntity comment = new TaskCommentEntity(DateUtils.now(), commentatorId, taskId, content);
        this.insert(comment);
    }

    private void checkCommentParam(Long taskId, Long commentatorId, String content) {
        if (taskId == null)
            throw new RRException("taskId is null.");
        if (commentatorId == null)
            throw new RRException("commentatorId is null.");
        if (StringUtils.isEmpty(content))
            throw new RRException("content is null.");
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
    public void addCommentReply(Long commentId, Long fromUserId, Long toUserId, String content) {
        checkCommentReplyParam(commentId, fromUserId, toUserId, content);
        TaskCommentReplyEntity reply = new TaskCommentReplyEntity();
        reply.setCommentId(commentId);
        reply.setFromUserId(fromUserId);
        reply.setToUserId(toUserId);
        reply.setContent(content);
        reply.setCreateTime(DateUtils.now());
        taskCommentReplyDao.insert(reply);
    }

    private void checkCommentReplyParam(Long commentId, Long fromUserId, Long toUserId, String content) {
        if (commentId == null)
            throw new RRException("commentId is null.");
        if (fromUserId == null)
            throw new RRException("fromUserId is null.");
        if (toUserId == null)
            throw new RRException("toUserId is null.");
        if (StringUtils.isEmpty(content))
            throw new RRException("content is null.");
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