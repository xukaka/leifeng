package io.renren.modules.app.dto;

import java.io.Serializable;


public class FollowAndFansCountDto implements Serializable {
    //关注数
    private Integer followCount =0 ;


    //粉丝数
    private Integer fansCount=0;

    public FollowAndFansCountDto(Integer followCount, Integer fansCount) {
        this.followCount = followCount;
        this.fansCount = fansCount;
    }

    public Integer getFollowCount() {
        return followCount;
    }

    public void setFollowCount(Integer followCount) {
        this.followCount = followCount;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }
}
