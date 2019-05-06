package io.renren.modules.app.entity;


public enum CircleAuditStatusEnum {

    UNAUDITED("未审核"),
    AGREED("已同意"),
    REFUSED("已拒绝");

    private String message;


    CircleAuditStatusEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

