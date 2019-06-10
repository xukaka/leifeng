package io.renren.modules.app.entity.member;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 邀请好友表
 */
@TableName("t_invite_friends")
public class InviteFriendsEntity extends BaseEntity {

    private Long inviteMemberId;//邀请人id
    private Long friendMemberId;//好友id
    private Integer experience;//获得经验值
    private Integer virtualCurrency;//获得虚拟币



    public Long getInviteMemberId() {
        return inviteMemberId;
    }

    public void setInviteMemberId(Long inviteMemberId) {
        this.inviteMemberId = inviteMemberId;
    }

    public Long getFriendMemberId() {
        return friendMemberId;
    }

    public void setFriendMemberId(Long friendMemberId) {
        this.friendMemberId = friendMemberId;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getVirtualCurrency() {
        return virtualCurrency;
    }

    public void setVirtualCurrency(Integer virtualCurrency) {
        this.virtualCurrency = virtualCurrency;
    }
}
