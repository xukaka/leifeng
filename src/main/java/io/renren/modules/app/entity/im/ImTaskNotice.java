package io.renren.modules.app.entity.im;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 任务通知
 */
@TableName("t_im_task_notice")
public class ImTaskNotice extends BaseEntity {

    /**
     * 接收方id
     */
    private Long memberId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 操作
     */
    private String operate;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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
