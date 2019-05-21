package io.renren.modules.app.dto;

import java.io.Serializable;


public class ImTaskStatusNoticeDto implements Serializable {

    private Long id;

    private String from;

    //接收方id
    private Long toMemberId;
    /**
     * 接收方昵称
     */
    private String toMemberNiceName;

    /**
     * 任务id
     */
    private Long taskId;

    //任务标题
    private String taskTitle;

    /**
     * 内容
     */
    private String content;

    //创建时间
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getToMemberNiceName() {
        return toMemberNiceName;
    }

    public void setToMemberNiceName(String toMemberNiceName) {
        this.toMemberNiceName = toMemberNiceName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getToMemberId() {
        return toMemberId;
    }

    public void setToMemberId(Long toMemberId) {
        this.toMemberId = toMemberId;
    }
}
