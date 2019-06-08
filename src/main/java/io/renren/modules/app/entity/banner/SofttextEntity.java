package io.renren.modules.app.entity.banner;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.entity.BaseEntity;

//软文
@TableName("t_softtext")
public class SofttextEntity extends BaseEntity {


    //标题
    private String title;

    //来源方
    private String source;

    //html富文本内容
    private String htmlContent;

    //浏览数
    private Integer viewCount=0;

    //url
    private String linkUrl;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
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

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}
