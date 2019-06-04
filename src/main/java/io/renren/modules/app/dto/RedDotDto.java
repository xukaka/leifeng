package io.renren.modules.app.dto;

import java.io.Serializable;
import java.util.List;


public class RedDotDto implements Serializable {
   private Long memberId;
   private List<ChatRedDot> chatRedDotList;

   private boolean taskRedDotStatus;

   private boolean dynamicRedDotStatus;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<ChatRedDot> getChatRedDotList() {
        return chatRedDotList;
    }

    public void setChatRedDotList(List<ChatRedDot> chatRedDotList) {
        this.chatRedDotList = chatRedDotList;
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
