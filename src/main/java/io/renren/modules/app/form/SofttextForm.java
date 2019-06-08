package io.renren.modules.app.form;

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
    private String htmlContent;


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

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Override
    public String toString() {
        return "SofttextForm{" +
                "title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                '}';
    }
}
