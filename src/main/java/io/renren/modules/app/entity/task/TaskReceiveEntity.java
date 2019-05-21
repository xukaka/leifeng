package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;
import io.renren.modules.app.entity.TaskStatusEnum;

/**
 * 任务领取表
 */
@TableName("t_task_receive")
public class TaskReceiveEntity extends BaseEntity {

    /**
     * 领取人id
     */
    private Long receiverId;
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 任务状态
     */
    private TaskStatusEnum status;

    /**
     * 更新时间
     */
    private Long updateTime;


    public TaskReceiveEntity() {
    }

    public TaskReceiveEntity(Long createTime,Long updateTime, Long receiverId, Long taskId,TaskStatusEnum status) {
        super(createTime);
        this.updateTime = updateTime;
        this.status = status;
        this.receiverId = receiverId;
        this.taskId = taskId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
