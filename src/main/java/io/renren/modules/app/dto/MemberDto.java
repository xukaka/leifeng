package io.renren.modules.app.dto;

import io.renren.modules.app.entity.setting.Member;

/**
 * 用户返回信息
 */
public class MemberDto extends Member {

    //是否关注
    private Boolean isFollowed;


    public Boolean getFollowed() {
        return isFollowed;
    }

    public void setFollowed(Boolean followed) {
        isFollowed = followed;
    }
}
