package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 任务圈
 */
@TableName("t_task_circle")
public class TaskCircleEntity extends BaseEntity {

    /**
     * 创建人id
     */
    private Long creatorId;

    /**
     * 圈名称
     */
    private String name;

    /**
     * 圈头像
     */
    private String avatar;
    /**
     * 圈描述
     */
    private String description;

    /**
     * 是否需要审核
     */
    private Boolean needReview=false;

    /**
     * 成员数
     */
    private Integer memberCount = 0;

    /**
     * 任务数
     */
    private Integer taskCount = 0;

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getNeedReview() {
        return needReview;
    }

    public void setNeedReview(Boolean needReview) {
        this.needReview = needReview;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }
}
