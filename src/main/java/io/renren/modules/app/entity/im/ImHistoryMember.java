package io.renren.modules.app.entity.im;

import io.renren.modules.app.entity.member.Member;

/**
 * @auther: Easy
 * @date: 19-4-20 15:22
 * @description:
 */
public class ImHistoryMember {
    /**
     * 是否存在未读信息
     */
    private int status;
    private Member member;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
