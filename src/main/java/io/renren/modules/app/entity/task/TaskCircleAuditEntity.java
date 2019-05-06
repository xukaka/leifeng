package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;
import io.renren.modules.app.entity.CircleAuditStatusEnum;

/**
 * 任务圈审核表
 */
@TableName("t_task_circle_audit")
public class TaskCircleAuditEntity extends BaseEntity {

    /**
     * 圈id
     */
    private Long circleId;
    /**
     * 申请人id
     */
    private Long applicantId;
    /**
     * 审核人id
     */
    private Long auditorId;

    /**
     * 审核状态
     */
    private CircleAuditStatusEnum status;

    public Long getCircleId() {
        return circleId;
    }

    public void setCircleId(Long circleId) {
        this.circleId = circleId;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public Long getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Long auditorId) {
        this.auditorId = auditorId;
    }

    public CircleAuditStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CircleAuditStatusEnum status) {
        this.status = status;
    }
}
