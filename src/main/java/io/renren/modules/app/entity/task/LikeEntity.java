package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;
import io.renren.modules.app.entity.LikeTypeEnum;

/**
 * 点赞
 */
@TableName("t_like")
public class LikeEntity extends BaseEntity {

    /**
     * 业务id：任务id/日记id
     */
    private Long businessId;

    /**
     * 点赞类型
     */
    private LikeTypeEnum type;
    /**
     * 点赞人id
     */
    private Long memberId;

    public LikeEntity() {
    }

    public LikeEntity(Long createTime, Long businessId, LikeTypeEnum type,Long memberId) {
        super(createTime);
        this.businessId = businessId;
        this.type = type;
        this.memberId = memberId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public LikeTypeEnum getType() {
        return type;
    }

    public void setType(LikeTypeEnum type) {
        this.type = type;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
