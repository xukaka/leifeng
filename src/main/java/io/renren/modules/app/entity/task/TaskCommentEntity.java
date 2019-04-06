package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 任务评论表
 */
@TableName("t_task_comment")
public class TaskCommentEntity extends BaseEntity {


    /**
     * 评论人id
     */
    private Long commentatorId;

    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 内容
     */
    private String content;

    public TaskCommentEntity() {
    }

    public TaskCommentEntity(Long createTime, Long commentatorId, Long taskId, String content) {
        super(createTime);
        this.commentatorId = commentatorId;
        this.taskId = taskId;
        this.content = content;
    }

    public Long getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(Long commentatorId) {
        this.commentatorId = commentatorId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
