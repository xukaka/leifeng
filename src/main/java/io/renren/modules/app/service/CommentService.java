package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.CommentDto;
import io.renren.modules.app.entity.CommentTypeEnum;
import io.renren.modules.app.entity.task.CommentEntity;
import io.renren.modules.app.form.PageWrapper;

/**
 * 评论
 */
public interface CommentService extends IService<CommentEntity> {


    /**
     * 分页获取评论列表-根据任务id
     */
    PageUtils<CommentDto> getComments(Long businessId, CommentTypeEnum type,PageWrapper page);

    /**
     * 新增评论
     */
    void addComment(Long businessId, CommentTypeEnum type,Long commentatorId, String content);

    /**
     * 删除评论-逻辑删除
     */
    void deleteComment(Long id);


    /**
     * 新增评论回复
     * @param commentId 评论id
     * @param fromUserId 回复人id
     * @param toUserId 被回复人id
     * @param content 回复内容
     */
    void addCommentReply(Long commentId, Long fromUserId, Long toUserId,  String content);

    /**
     * 删除评论回复
     */
    void deleteCommentReply(Long replyId);


}

