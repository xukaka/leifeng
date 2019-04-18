package io.renren.modules.app.entity.setting;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * 注册的用户-标签关联关系
 */
@TableName("r_member_tag")
@ApiModel
public class MemberTagRelationEntity implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;
    //用户id
    private Long memberId;
    //标签id
    private Long tagId;

    //使用次数
    private Integer usageCount;


    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
