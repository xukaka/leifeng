package io.renren.modules.app.dto;

import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.entity.story.DiaryContentEntity;

import java.io.Serializable;
import java.util.List;


public class BannerDto implements Serializable {
    //横幅id
    private Long id;



    //标题
    private String title;

    //封面url
    private String coverUrl;


    //链接url
    private String linkUrl;

    //类型
    private BannerTypeEnum type;

    //创建时间
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public BannerTypeEnum getType() {
        return type;
    }

    public void setType(BannerTypeEnum type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
