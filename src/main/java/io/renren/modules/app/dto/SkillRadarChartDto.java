package io.renren.modules.app.dto;

import io.renren.modules.app.entity.TaskStatusEnum;

import java.io.Serializable;

public class SkillRadarChartDto implements Serializable {

    //用户id
    private Long memberId;
    //标签id
    private Long tagId;
    //标签名称
    private String tagName;

    //使用次数
    private Integer usageCount;

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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }
}
