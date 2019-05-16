package io.renren.common.io.body;

import org.jim.common.packets.Message;

/**
 * @auther: Easy
 * @date: 19-4-18 10:21
 * @description:
 */
public class UserBody extends Message {
    private Long memberId;
    private String token;
    public UserBody(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
