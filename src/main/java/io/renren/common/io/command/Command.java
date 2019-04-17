package io.renren.common.io.command;

/**
 * @auther: Easy
 * @date: 19-4-18 14:08
 * @description:
 */
public enum Command  {
    /**
     * 请求cmd命令码 + 响应cmd命令码
     */
    USER_LOGIN_REQ(21, "好友申请请求"),
    USER_LOGIN_RES(22, "好友申请响应"),
    /**
     * 请求cmd命令码 + 响应cmd命令码
     */
    FRIENDS_APPLY_REQ(21, "好友申请请求"),
    FRIENDS_APPLY_RES(22, "好友申请响应"),

    FRIENDS_LIST_REQ(23, "好友列表请求"),
    FRIENDS_LIST_RES(24, "好友列表响应"),

    FRIENDS_APPLY_OPERATE_REQ(25, "好友申请状态处理请求"),
    FRIENDS_APPLY_OPERATE_RES(26, "好友申请状态处理响应"),

    CREATE_GROUP_REQ(27, "创建群组请求"),
    CREATE_GROUP_RES(28, "创建群组响应"),

    INNER_GROUP_MEMBER_REQ(29, "新增/删除组内人员请求"),
    INNER_GROUP_MEMBER_RES(30, "新增/删除组内人员响应"),


    /**
     * Other command.
     */
    OTHER(2, "我是返回信息");
    private int cmdCode;
    private String resMessage;

    Command(int resCode) {
        this.cmdCode = resCode;
    }

    Command(int resCode, String resMessage) {
        this.cmdCode = resCode;
        this.resMessage = resMessage;
    }

    /**
     * Gets res code.
     *
     * @return the res code
     */
    public int getResCode() {
        return cmdCode;
    }

    /**
     * Sets res code.
     *
     * @param resCode the res code
     */
    public void setResCode(int resCode) {
        this.cmdCode = resCode;
    }

    /**
     * Gets res message.
     *
     * @return the res message
     */
    public String getResMessage() {
        return resMessage;
    }

    /**
     * Sets res message.
     *
     * @param resMessage the res message
     */
    public void setResMessage(String resMessage) {
        this.resMessage = resMessage;
    }
}
