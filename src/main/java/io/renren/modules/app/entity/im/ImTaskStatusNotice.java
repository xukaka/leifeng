package io.renren.modules.app.entity.im;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 任务状态通知
 */
@TableName("t_im_task_status_notice")
public class ImTaskStatusNotice extends BaseEntity {

    /**
     * 发送方
     */
    private String from;
    /**
     * 接收方
     */
    private String to;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 内容
     */
    private String content;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
