package io.renren.modules.app.entity;

public enum LikeTypeEnum {
    task(0, "任务"),
    diary(1, "日记");

    private int value;

    private String msg;

    LikeTypeEnum(int value, String msg) {
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
