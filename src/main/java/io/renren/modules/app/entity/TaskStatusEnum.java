package io.renren.modules.app.entity;

public enum TaskStatusEnum {

    published(0, "已发布"),

    received(1, "已领取"),

    submitted(2, "已提交"),

    completed(3, "已完成"),

    cancelled(4, "已取消");

    private int value;

    private String msg;

    TaskStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
