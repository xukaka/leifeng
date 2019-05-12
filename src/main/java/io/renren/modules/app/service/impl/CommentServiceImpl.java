package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.ThreadPoolUtils;
import io.renren.modules.app.dao.task.CommentDao;
import io.renren.modules.app.dao.task.CommentReplyDao;
import io.renren.modules.app.dto.CommentDto;
import io.renren.modules.app.dto.CommentReplyDto;
import io.renren.modules.app.entity.CommentTypeEnum;
import io.renren.modules.app.entity.task.CommentEntity;
import io.renren.modules.app.entity.task.CommentReplyEntity;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.CommentService;
import io.renren.modules.app.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {
    private final static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Resource
    private CommentReplyDao commentReplyDao;
    @Resource
    private TaskService taskService;

    @Override
    public PageUtils<CommentDto> getComments(Long businessId,CommentTypeEnum type, PageWrapper page) {
        List<CommentDto> comments = baseMapper.getComments(businessId,type, page);
        if (CollectionUtils.isEmpty(comments)) {
            return new PageUtils<>();
        }
        setCommentRepies(comments);
        int total = baseMapper.count(businessId,type);
        return new PageUtils<>(comments, total, page.getPageSize(), page.getCurrPage());
    }

    private void setCommentRepies(List<CommentDto> comments) {
        for (CommentDto comment : comments) {
            List<CommentReplyDto> replies = getCommentReplies(comment.getId());
            comment.setReplies(replies);
        }
    }


    @Override
    public void addComment(Long businessId, CommentTypeEnum type,Long commentatorId, String content) {
        checkCommentParam(businessId,type, commentatorId, content);
        CommentEntity comment = new CommentEntity(DateUtils.now(), commentatorId, businessId,type, content);
        this.insert(comment);

        ThreadPoolUtils.execute(()->{
            //评论数 +1
            switch (type){
                case task:
                    taskService.incCommentCount(businessId,1);
                    break;
                case diary:
                    //TODO 日志评论数+1
                    break;
            }


        });
    }

    private void checkCommentParam(Long businessId, CommentTypeEnum type,Long commentatorId, String content) {
        if (businessId == null)
            throw new RRException("businessId is null.");
        if (type == null)
            throw new RRException("type is null.");
        if (commentatorId == null)
            throw new RRException("commentatorId is null.");
        if (StringUtils.isEmpty(content))
            throw new RRException("content is null.");
    }

    @Override
    public void deleteComment(Long id) {
        CommentEntity comment = this.selectById(id);
        if (comment != null) {
            comment.setDeleted(true);
            this.updateById(comment);
        }
    }

    @Override
    public void addCommentReply(Long commentId, Long fromUserId, Long toUserId, String content) {
        checkCommentReplyParam(commentId, fromUserId, toUserId, content);
        CommentReplyEntity reply = new CommentReplyEntity();
        reply.setCommentId(commentId);
        reply.setFromUserId(fromUserId);
        reply.setToUserId(toUserId);
        reply.setContent(content);
        reply.setCreateTime(DateUtils.now());
        commentReplyDao.insert(reply);
        ThreadPoolUtils.execute(()->{
            //评论数 +1
            CommentEntity comment= selectById(commentId);
            if (comment!=null){
                switch (comment.getType()){
                    case task:
                        taskService.incCommentCount(comment.getBusinessId(),1);
                        break;

                    case diary:
                        //TODO 日记评论数+1

                        break;
                }

            }
        });
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
        CommentReplyEntity reply = commentReplyDao.selectById(replyId);
        if (reply != null) {
            reply.setDeleted(true);
            commentReplyDao.deleteById(reply);
        }

    }


    private List<CommentReplyDto> getCommentReplies(Long commentId) {
        List<CommentReplyDto> replies = commentReplyDao.getCommentReplies(commentId);
        if (CollectionUtils.isEmpty(replies)) {
            return new ArrayList<>();
        }
        return replies;
    }

}