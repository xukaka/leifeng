package io.renren.modules.app.dto;

import java.io.Serializable;

/**
 * 评分
 */
public class MemberScoreDto implements Serializable {

    private Long id;
    /**
     * 评分人id
     */
    private Long judgerId;
    private String judgerNickName;//评分人昵称
    private String judgerAvatar;//评分人头像
    private Integer judgerSex;//评分人性别


    /**
     * 接收人id（被评分人）
     */
    private Long receiverId;
    private String receiverNickName;//接收人昵称
    private String receiverAvatar;//接收人头像
    private Integer receiverSex;//接收人性别
    private Integer receiverExperience;//接收人经验值


    /**
     * 任务id
     */
    private Long taskId;

    //任务标题
    private String taskTitle;

    //任务金额
    private Integer taskMoney;

    //任务完成时间
    private Long taskCompleteTime;
    /**
     * 分数
     */
    private Integer score;


    /**
     * 评论内容
     */
    private String content;


    //创建时间
    private Long createTime;

    public Long getJudgerId() {
        return judgerId;
    }

    public void setJudgerId(Long judgerId) {
        this.judgerId = judgerId;
    }


    public String getJudgerNickName() {
        return judgerNickName;
    }

    public void setJudgerNickName(String judgerNickName) {
        this.judgerNickName = judgerNickName;
    }

    public String getJudgerAvatar() {
        return judgerAvatar;
    }

    public void setJudgerAvatar(String judgerAvatar) {
        this.judgerAvatar = judgerAvatar;
    }

    public Integer getJudgerSex() {
        return judgerSex;
    }

    public void setJudgerSex(Integer judgerSex) {
        this.judgerSex = judgerSex;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverNickName() {
        return receiverNickName;
    }

    public void setReceiverNickName(String receiverNickName) {
        this.receiverNickName = receiverNickName;
    }

    public String getReceiverAvatar() {
        return receiverAvatar;
    }

    public void setReceiverAvatar(String receiverAvatar) {
        this.receiverAvatar = receiverAvatar;
    }

    public Integer getReceiverSex() {
        return receiverSex;
    }

    public void setReceiverSex(Integer receiverSex) {
        this.receiverSex = receiverSex;
    }

    public Integer getReceiverExperience() {
        return receiverExperience;
    }

    public void setReceiverExperience(Integer receiverExperience) {
        this.receiverExperience = receiverExperience;
    }

    public Integer getTaskMoney() {
        return taskMoney;
    }

    public void setTaskMoney(Integer taskMoney) {
        this.taskMoney = taskMoney;
    }

    public Long getTaskCompleteTime() {
        return taskCompleteTime;
    }

    public void setTaskCompleteTime(Long taskCompleteTime) {
        this.taskCompleteTime = taskCompleteTime;
    }
}
