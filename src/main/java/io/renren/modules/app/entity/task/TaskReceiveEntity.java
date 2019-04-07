package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

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


    public TaskReceiveEntity() {
    }

    public TaskReceiveEntity(Long createTime, Long receiverId, Long taskId) {
        super(createTime);
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
}
