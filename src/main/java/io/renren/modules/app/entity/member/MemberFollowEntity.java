package io.renren.modules.app.entity.member;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 用户关注表
 */
@TableName("t_member_follow")
public class MemberFollowEntity extends BaseEntity {

    /**
     * 关注用户id
     */
    private Long fromMemberId;
    /**
     * 被关注用户id
     */
    private Long toMemberId;

    public MemberFollowEntity() {
    }

    public MemberFollowEntity(Long createTime, Long fromMemberId, Long toMemberId) {
        super(createTime);
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
    }

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
}
