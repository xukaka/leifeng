package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 评论回复表
 */
@TableName("t_comment_reply")
public class CommentReplyEntity extends BaseEntity {


    /**
     * 回复人id
     */
    private Long fromUserId;

    /**
     * 被回复人id
     */
    private Long toUserId;

    /**
     * 评论id
     */
    private Long commentId;
    /**
     * 回复内容
     */
    private String content;

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
