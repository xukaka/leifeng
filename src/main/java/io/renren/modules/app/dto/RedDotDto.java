package io.renren.modules.app.dto;

import java.io.Serializable;


public class RedDotDto implements Serializable {
   private Long memberId;
   private boolean chatRedDotStatus;

   private boolean taskRedDotStatus;

   private boolean dynamicRedDotStatus;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public boolean isChatRedDotStatus() {
        return chatRedDotStatus;
    }

    public void setChatRedDotStatus(boolean chatRedDotStatus) {
        this.chatRedDotStatus = chatRedDotStatus;
    }

    public boolean isTaskRedDotStatus() {
        return taskRedDotStatus;
    }

    public void setTaskRedDotStatus(boolean taskRedDotStatus) {
        this.taskRedDotStatus = taskRedDotStatus;
    }

    public boolean isDynamicRedDotStatus() {
        return dynamicRedDotStatus;
    }

    public void setDynamicRedDotStatus(boolean dynamicRedDotStatus) {
        this.dynamicRedDotStatus = dynamicRedDotStatus;
    }
}
