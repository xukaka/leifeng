package io.renren.modules.app.entity.im;

import com.baomidou.mybatisplus.annotations.TableId;
import io.renren.modules.app.entity.setting.Member;

/**
 * @auther: Easy
 * @date: 19-4-20 15:22
 * @description:
 */
public class ImHistoryMember {
    /**
     * 是否存在未读信息
     */
    int type;
    Member member;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
