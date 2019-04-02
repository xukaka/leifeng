package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

import java.util.List;

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
     * 图片url列表
     */
    private List<String> imageUrls;

    /**
     * 标签ids
     */
    private List<Long> tagIds;
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
     * 被通知的用户id列表
     */
    private List<Long> notifiedUserIds;

    /**
     * 创建者ID
     */
    private Long creatorId;


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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
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


    public List<Long> getNotifiedUserIds() {
        return notifiedUserIds;
    }

    public void setNotifiedUserIds(List<Long> notifiedUserIds) {
        this.notifiedUserIds = notifiedUserIds;
    }
}
