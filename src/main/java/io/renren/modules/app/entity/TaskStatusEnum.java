package io.renren.modules.app.entity;

public enum TaskStatusEnum {

    published(0, "已发布"),

    received(1, "已领取"),

    executing(2, "执行中"),

    submitted(3, "已提交"),

    completed(4, "已完成"),

    cancelled(5, "已取消");

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
