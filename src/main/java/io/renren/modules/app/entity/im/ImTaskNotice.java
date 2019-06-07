package io.renren.modules.app.entity.im;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 任务通知
 */
@TableName("t_im_task_notice")
public class ImTaskNotice extends BaseEntity {

    /**
     * 发送方id
     */
    private Long fromMemberId;
    private Long toMemberId;//接收方id

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 操作
     */
    private String operate;

    public Long getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(Long fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    public Long getToMemberId() {
        return toMemberId;
    }

    public void setToMemberId(Long toMemberId) {
        this.toMemberId = toMemberId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
