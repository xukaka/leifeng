package io.renren.modules.app.entity.member;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 用户评分表
 */
@TableName("t_member_score")
public class MemberScoreEntity extends BaseEntity {

    /**
     * 评分人id
     */
    private Long judgerId;
    /**
     * 被评分用户id
     */
    private Long memberId;

    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 分数
     */
    private Integer score;



    /**
     * 评论内容
     */
    private String content;

    public MemberScoreEntity() {
    }

    public Long getJudgerId() {
        return judgerId;
    }

    public void setJudgerId(Long judgerId) {
        this.judgerId = judgerId;
    }

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
}
