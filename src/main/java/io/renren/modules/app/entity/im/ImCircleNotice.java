package io.renren.modules.app.entity.im;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 圈通知
 */
@TableName("t_im_circle_notice")
public class ImCircleNotice extends BaseEntity {

    //圈id
    private Long circleId;
    //审核id
    private Long auditId;
    //发送方id
    private Long fromMemberId;
    //接收方id
    private Long toMemberId;
    //类型：join:申请加入，audit:圈主审核
    private String type;

    public Long getCircleId() {
        return circleId;
    }

    public void setCircleId(Long circleId) {
        this.circleId = circleId;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Long getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(Long fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    public Long getToMemberId() {
        return toMemberId;
    }

    public void setToMemberId(Long toMemberId) {
        this.toMemberId = toMemberId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
