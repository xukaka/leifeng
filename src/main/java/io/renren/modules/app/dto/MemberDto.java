package io.renren.modules.app.dto;

import io.renren.modules.app.entity.member.Member;

import java.util.List;

/**
 * 用户返回信息
 */
public class MemberDto extends Member {

    //是否关注
    private Boolean isFollowed;

    //距离
    private Long distance;

    //用户技能标签
    private List<String> tags;

    //是否可指派
    private Boolean isChoosed;


    public Long getDistance() {
        return distance;
    }


    public void setDistance(Long distance) {
        this.distance = distance;
    }


    public List<String> getTags() {
        return tags;
    }


    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getFollowed() {
        return isFollowed;
    }

    public void setFollowed(Boolean followed) {
        isFollowed = followed;
    }

    public Boolean getChoosed() {
        return isChoosed;
    }

    public void setChoosed(Boolean choosed) {
        isChoosed = choosed;
    }
}
