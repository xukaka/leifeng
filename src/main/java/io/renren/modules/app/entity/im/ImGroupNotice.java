package io.renren.modules.app.entity.im;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 组通知
 */
@TableName("t_im_group_notice")
public class ImGroupNotice extends BaseEntity {

    /**
     * 组id
     */
    private String groupId;
    //业务类型：task|diary
    private String businessType;
    private Long businessId;//业务id：任务、日记
    private String from;//发布方
    private String content;//发布内容

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
