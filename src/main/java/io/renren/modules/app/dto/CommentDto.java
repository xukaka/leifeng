package io.renren.modules.app.dto;

import io.renren.modules.app.entity.CommentTypeEnum;
import io.renren.modules.app.entity.setting.Member;

import java.io.Serializable;
import java.util.List;

/**
 * 评论返回信息
 */
public class CommentDto implements Serializable {

    /**
     * 评论id
     */
    private Long id;

    /**
     * 评论类型
     */
    private CommentTypeEnum type;
    /**
     * 业务id：任务id/日记id
     */
    private Long businessId;
    /**
     * 评论人
     */
    private Member commentator;


    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 评论回复列表
     */
    private List<CommentReplyDto> replies;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public CommentTypeEnum getType() {
        return type;
    }

    public void setType(CommentTypeEnum type) {
        this.type = type;
    }

    public Member getCommentator() {
        return commentator;
    }

    public void setCommentator(Member commentator) {
        this.commentator = commentator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CommentReplyDto> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentReplyDto> replies) {
        this.replies = replies;
    }
}
