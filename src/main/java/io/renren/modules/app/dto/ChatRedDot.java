package io.renren.modules.app.dto;

import java.io.Serializable;

//聊天用户红点状态列表
public class ChatRedDot implements Serializable {
   private Long memberId;

   private Boolean status=false;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
