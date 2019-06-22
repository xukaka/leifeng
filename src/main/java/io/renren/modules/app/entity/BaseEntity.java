package io.renren.modules.app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import io.renren.common.utils.DateUtils;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 创建时间
     */
    private Long createTime;

    public BaseEntity() {
    }

    public BaseEntity(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 是否删除
     */
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
