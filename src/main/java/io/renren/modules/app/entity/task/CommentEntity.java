package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;
import io.renren.modules.app.entity.CommentTypeEnum;

/**
 * 评论表
 */
@TableName("t_comment")
public class CommentEntity extends BaseEntity {


    /**
     * 评论人id
     */
    private Long commentatorId;

    /**
     * 评论类型
     */
    private CommentTypeEnum type;

    /**
     * 业务id：任务id/日记id
     */
    private Long businessId;
    /**
     * 内容
     */
    private String content;

    public CommentEntity() {
    }

    public CommentEntity(Long createTime, Long commentatorId, Long businessId,CommentTypeEnum type, String content) {
        super(createTime);
        this.commentatorId = commentatorId;
        this.businessId = businessId;
        this.type = type;
        this.content = content;
    }

    public Long getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(Long commentatorId) {
        this.commentatorId = commentatorId;
    }

    public CommentTypeEnum getType() {
        return type;
    }

    public void setType(CommentTypeEnum type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
}
