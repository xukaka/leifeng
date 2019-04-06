package io.renren.modules.app.dto;

import io.renren.modules.app.entity.setting.Member;

import java.io.Serializable;
import java.util.List;

/**
 * 任务评论返回信息
 */
public class TaskCommentDto implements Serializable {

    /**
     * 评论id
     */
    private Long id;
    /**
     * 任务id
     */
    private Long taskId;
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
    private List<TaskCommentReplyDto> replies;

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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public List<TaskCommentReplyDto> getReplies() {
        return replies;
    }

    public void setReplies(List<TaskCommentReplyDto> replies) {
        this.replies = replies;
    }
}
