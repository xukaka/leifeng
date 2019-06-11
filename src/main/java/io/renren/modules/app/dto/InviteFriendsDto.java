package io.renren.modules.app.dto;

import java.io.Serializable;

//邀请好友
public class InviteFriendsDto implements Serializable {
    //id
    private Long id;

    //好友id
    private Long friendMemberId ;
    private String friendMemberNickName ;//好友昵称
    private String friendMemberAvatar ;//好友头像
    private Integer friendMemberSex ;//好友性别
    private Integer experience;//获得经验值
    private Integer virtualCurrency;//获得虚拟币
    private Long createTime;//创建时间


    public String getFriendMemberNickName() {
        return friendMemberNickName;
    }

    public void setFriendMemberNickName(String friendMemberNickName) {
        this.friendMemberNickName = friendMemberNickName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFriendMemberId() {
        return friendMemberId;
    }

    public void setFriendMemberId(Long friendMemberId) {
        this.friendMemberId = friendMemberId;
    }

    public String getFriendMemberAvatar() {
        return friendMemberAvatar;
    }

    public void setFriendMemberAvatar(String friendMemberAvatar) {
        this.friendMemberAvatar = friendMemberAvatar;
    }

    public Integer getFriendMemberSex() {
        return friendMemberSex;
    }

    public void setFriendMemberSex(Integer friendMemberSex) {
        this.friendMemberSex = friendMemberSex;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
