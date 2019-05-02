package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 任务点赞
 */
@TableName("t_task_like")
public class TaskLikeEntity extends BaseEntity {

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 点赞人id
     */
    private Long memberId;

    public TaskLikeEntity() {
    }

    public TaskLikeEntity(Long createTime, Long taskId, Long memberId) {
        super(createTime);
        this.taskId = taskId;
        this.memberId = memberId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
