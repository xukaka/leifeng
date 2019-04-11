package io.renren.modules.app.dto;

import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.task.TaskEntity;

import java.io.Serializable;

/**
 * 任务banner返回信息
 */
public class TaskBannerDto implements Serializable {

    /**
     * 任务完成人
     */
    private Member completer;
    /**
     * 任务信息
     */
    private TaskEntity task;

    /**
     * 任务完成时间
     */
    private Long completeTime;

    public Member getCompleter() {
        return completer;
    }

    public void setCompleter(Member completer) {
        this.completer = completer;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }
}
