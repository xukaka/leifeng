package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @auther: Easy
 * @date: 19-4-20 17:42
 * @description:
 * * memberId:代表发送人，当前用户ID即可
 *      * toId：代表接受人，也就是不在线的用户Id
 *      * type:0代表私聊，1代表关注，2代表任务通知
 *      * status:0代表没有未读，1代表有未读
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
    @ApiModelProperty(value = "状态", example = "")
    @NotNull
    private int status;


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

    public int getStatus() { return status;}

    public void setStatus(int status) {  this.status = status; }

    @Override
    public String toString() {
        return "MessageTypeForm{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
