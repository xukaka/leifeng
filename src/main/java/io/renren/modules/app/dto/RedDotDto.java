package io.renren.modules.app.dto;

import java.io.Serializable;
import java.util.List;


public class RedDotDto implements Serializable {
   private Long memberId;
   private List<ChatRedDot> chatRedDotList;

   private boolean taskRedDotStatus;//任务通知红点状态

   private boolean dynamicRedDotStatus;//最新动态红点状态
   private boolean circleRedDotStatus;//雷锋圈红点状态

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

    public boolean isCircleRedDotStatus() {
        return circleRedDotStatus;
    }

    public void setCircleRedDotStatus(boolean circleRedDotStatus) {
        this.circleRedDotStatus = circleRedDotStatus;
    }
}
