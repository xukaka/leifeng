package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;
import io.renren.modules.app.entity.TaskStatusEnum;

/**
 * 任务表
 */
@TableName("t_task")
public class TaskEntity extends BaseEntity {

    /**
     * 主题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 任务开始时间
     */
    private Long startTime;
    /**
     * 任务过期时间
     */
    private Long expireTime;
    /**
     * 虚拟货币(雷锋币)
     */
    private Integer virtualCurrency;
    /**
     * 任务地址id
     */
    private Long addressId;
    /**
     * 创建者ID
     */
    private Long creatorId;
    /**
     * 点赞数
     */
    private Integer likeCount = 0;
    /**
     * 评论数
     */
    private Integer commentCount = 0;

    /**
     * 浏览数
     */
    private Integer viewCount = 0;
    /**
     * 状态
     */
    private TaskStatusEnum status;
    /**
     * 完成时间
     */
    private Long completeTime;


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

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getVirtualCurrency() {
        return virtualCurrency;
    }

    public void setVirtualCurrency(Integer virtualCurrency) {
        this.virtualCurrency = virtualCurrency;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }


    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }


    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }


}
