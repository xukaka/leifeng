package io.renren.modules.app.dto;

import io.renren.modules.app.entity.TaskStatusEnum;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.task.TaskAddressEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 任务返回信息
 */
public class TaskDto implements Serializable {

    /**
     * 任务id
     */
    private Long id;
    /**
     * 主题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 图片url列表
     */
    private List<String> imageUrls;
    /**
     * 标签名称
     */
    private List<String> tagNames;
    /**
     * 任务开始时间
     */
    private Long startTime;
    /**
     * 任务过期时间
     */
    private Long expireTime;
    /**
     * 当前当前系统时间
     */
    private Long curSystemTime;
    /**
     * 任务领取时间
     */
    private Long receiveTime;
    /**
     * 虚拟货币(雷锋币)
     */
    private Integer virtualCurrency;
    /**
     * 任务地址
     */
    private TaskAddressEntity address;
    /**
     * 距离，根据经纬度计算
     */
    private Long distance;
    /*
     * 任务创建人
     */
    private Member creator;
    /**
     * 是否关注任务创建人
     */
    private Boolean isFollowed = false;
    /**
     * 是否领取（是否在领取列表中）
     */
    private Boolean isReceived = false;
    /**
     * 任务领取人
     */
    private Member receiver;
    /**
     * 状态
     */
    private TaskStatusEnum status;

    /**
     * 评论数
     */
    private Integer commentCount;
    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 浏览数
     */
    private Integer viewCount = 0;
    /**
     * 创建时间
     */
    private Long createTime;

    public Boolean getFollowed() {
        return isFollowed;
    }

    public void setFollowed(Boolean followed) {
        isFollowed = followed;
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }


    public Long getCurSystemTime() {
        return curSystemTime;
    }

    public void setCurSystemTime(Long curSystemTime) {
        this.curSystemTime = curSystemTime;
    }

    public Integer getVirtualCurrency() {
        return virtualCurrency;
    }

    public void setVirtualCurrency(Integer virtualCurrency) {
        this.virtualCurrency = virtualCurrency;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public TaskAddressEntity getAddress() {
        return address;
    }

    public void setAddress(TaskAddressEntity address) {
        this.address = address;
    }

    public Member getCreator() {
        return creator;
    }

    public void setCreator(Member creator) {
        this.creator = creator;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getReceiver() {
        return receiver;
    }

    public void setReceiver(Member receiver) {
        this.receiver = receiver;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Boolean getReceived() {
        return isReceived;
    }

    public void setReceived(Boolean received) {
        isReceived = received;
    }
}
