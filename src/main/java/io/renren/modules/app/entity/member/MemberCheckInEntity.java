package io.renren.modules.app.entity.member;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 用户签到表
 */
@TableName("t_member_checkin")
public class MemberCheckInEntity extends BaseEntity {

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 经验值
     */
    private Integer experience;

    public MemberCheckInEntity() {
    }

    public MemberCheckInEntity(Long createTime, Long memberId, Integer experience) {
        super(createTime);
        this.memberId = memberId;
        this.experience = experience;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }
}
