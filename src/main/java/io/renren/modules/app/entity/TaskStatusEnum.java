package io.renren.modules.app.entity;

public enum TaskStatusEnum {


    notpay("未支付"),

    payed("已支付"),

    published("已发布"),

    received("已领取"),
    choosed("已选择"),

    executing("执行中"),

    submitted("已提交"),

    completed("已完成"),

    cancelled("已取消");


    private String msg;

    TaskStatusEnum(String msg) {

        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
