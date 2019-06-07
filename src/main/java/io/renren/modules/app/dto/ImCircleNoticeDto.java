package io.renren.modules.app.dto;

import io.renren.modules.app.entity.CircleAuditStatusEnum;

import java.io.Serializable;


public class ImCircleNoticeDto implements Serializable {

    private Long id;
    //发送方id
    private Long memberId;
    private String memberNickName;//昵称
    private Integer memberSex;//性别
    private String memberAvatar;//头像
    /**
     * 圈id
     */
    private Long circleId;
    //圈名称
    private String circleName;
    //审核id
    private Long aduitId;
    //审核状态
    private CircleAuditStatusEnum aduitStatus;
    /**
     * 类型：join：申请加入，audit审核
     */
    private String type;

    //创建时间
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public Integer getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(Integer memberSex) {
        this.memberSex = memberSex;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public Long getCircleId() {
        return circleId;
    }

    public void setCircleId(Long circleId) {
        this.circleId = circleId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public Long getAduitId() {
        return aduitId;
    }

    public void setAduitId(Long aduitId) {
        this.aduitId = aduitId;
    }

    public CircleAuditStatusEnum getAduitStatus() {
        return aduitStatus;
    }

    public void setAduitStatus(CircleAuditStatusEnum aduitStatus) {
        this.aduitStatus = aduitStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
