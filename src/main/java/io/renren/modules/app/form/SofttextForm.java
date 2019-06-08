package io.renren.modules.app.form;

import io.renren.modules.app.entity.BannerTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("软文")
public class SofttextForm {

    @NotEmpty
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("来源方")
    @NotEmpty
    private String source;

    @NotEmpty
    @ApiModelProperty("链接url")
    private String linkUrl;

    @NotNull
    @ApiModelProperty("html内容")
    private String htmlContext;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getHtmlContext() {
        return htmlContext;
    }

    public void setHtmlContext(String htmlContext) {
        this.htmlContext = htmlContext;
    }
}
