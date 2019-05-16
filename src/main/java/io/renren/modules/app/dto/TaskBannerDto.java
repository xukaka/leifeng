package io.renren.modules.app.dto;

import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.entity.task.TaskAddressEntity;
import io.renren.modules.app.entity.task.TaskEntity;

import java.io.Serializable;

/**
 * 任务banner返回信息
 */
public class TaskBannerDto implements Serializable {

    /**
     * 任务领取人
     */
    private Member receiver;
    /**
     * 任务信息
     */
    private TaskEntity task;

    /**
     * 任务地址
     */
    private TaskAddressEntity address;

    /**
     * 任务完成时间
     */
//    private Long completeTime;

    public Member getReceiver() {
        return receiver;
    }

    public void setReceiver(Member receiver) {
        this.receiver = receiver;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }


    public TaskAddressEntity getAddress() {
        return address;
    }

    public void setAddress(TaskAddressEntity address) {
        this.address = address;
    }
}
