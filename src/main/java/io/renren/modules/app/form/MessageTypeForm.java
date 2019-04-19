package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @auther: Easy
 * @date: 19-4-20 17:42
 * @description:
 */
@ApiModel(value = "设置未读消息状态表单")
public class MessageTypeForm {
    @ApiModelProperty(value = "发送人ID", example = "")
    @NotNull
    private Long fromId;
    @ApiModelProperty(value = "接受人ID", example = "")
    @NotNull
    private  Long toId;
    @ApiModelProperty(value = "类型", example = "")
    @NotNull
    private int type;



    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
