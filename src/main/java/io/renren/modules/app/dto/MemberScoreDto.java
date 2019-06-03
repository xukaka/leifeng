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
     * 任务id
     */
    private Long taskId;

    //任务标题
    private String taskTitle;
    /**
     * 分数
     */
    private Integer score;


    /**
     * 评论内容
     */
    private String content;

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
}
