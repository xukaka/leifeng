package io.renren.modules.app.dto;

import io.renren.modules.app.entity.setting.Member;

import java.io.Serializable;

/**
 * 任务评论回复返回信息
 */
public class TaskCommentReplyDto implements Serializable {
    /**
     * 回复id
     */
    private Long id;
    /**
     * 评论id
     */
    private Long commentId;
    /**
     * 回复人
     */
    private Member fromUser;
    /**
     * 被回复人
     */
    private Member toUser;

    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Member getFromUser() {
        return fromUser;
    }

    public void setFromUser(Member fromUser) {
        this.fromUser = fromUser;
    }

    public Member getToUser() {
        return toUser;
    }

    public void setToUser(Member toUser) {
        this.toUser = toUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


}
