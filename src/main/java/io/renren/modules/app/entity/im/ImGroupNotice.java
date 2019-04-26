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
    private Long groupId;

    /**
     * 扩展json
     */
    private String extrasJson;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getExtrasJson() {
        return extrasJson;
    }

    public void setExtrasJson(String extrasJson) {
        this.extrasJson = extrasJson;
    }
}
