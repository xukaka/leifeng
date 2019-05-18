package io.renren.modules.app.entity;

public enum BannerTypeEnum {
    home(0, "首页"),
    task(1, "任务");

    private int value;

    private String msg;

    BannerTypeEnum(int value, String msg) {
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
