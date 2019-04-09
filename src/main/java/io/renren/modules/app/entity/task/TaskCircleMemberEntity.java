package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 任务圈成员
 */
@TableName("t_task_circle_member")
public class TaskCircleMemberEntity extends BaseEntity {

    /**
     * 圈id
     */
    private Long circleId;

    /**
     * 成员id
     */
    private Long memberId;


    public TaskCircleMemberEntity() {
    }

    public TaskCircleMemberEntity(Long createTime, Long circleId, Long memberId) {
        super(createTime);
        this.circleId = circleId;
        this.memberId = memberId;
    }

    public Long getCircleId() {
        return circleId;
    }

    public void setCircleId(Long circleId) {
        this.circleId = circleId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
