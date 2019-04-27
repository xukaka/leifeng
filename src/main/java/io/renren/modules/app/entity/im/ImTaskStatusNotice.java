package io.renren.modules.app.entity.im;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 任务状态通知
 */
@TableName("t_im_task_status_notice")
public class ImTaskStatusNotice extends BaseEntity {
    /**
     * 通知发布人id
     */
    private Long fromMemberId;
    /**
     * 被通知人id
     */
    private Long toMemberId;

    /**
     * 扩展json
     */
    private String extrasJson;


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

    public String getExtrasJson() {
        return extrasJson;
    }

    public void setExtrasJson(String extrasJson) {
        this.extrasJson = extrasJson;
    }
}
