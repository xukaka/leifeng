package io.renren.modules.app.entity;

public enum ParagraphTypeEnum {

    text(0, "文本"),

    image(1, "图片");
    private int value;

    private String msg;

    ParagraphTypeEnum(int value, String msg) {
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
