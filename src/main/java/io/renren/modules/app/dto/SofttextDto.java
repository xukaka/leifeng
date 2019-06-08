package io.renren.modules.app.dto;

import io.renren.modules.app.entity.BannerTypeEnum;

import java.io.Serializable;


public class SofttextDto implements Serializable {
    //横幅id
    private Long id;


    //标题
    private String title;


    //链接url
    private String linkUrl;

    //html内容信息
    private String htmlContent;
    //来源方
    private String source ;

    //创建时间
    private Long createTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
