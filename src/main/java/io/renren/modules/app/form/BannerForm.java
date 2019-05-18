package io.renren.modules.app.form;

import io.renren.modules.app.entity.BannerTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("横幅")
public class BannerForm {

    @NotEmpty
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("封面url")
    @NotEmpty
    private String coverUrl;

    @NotEmpty
    @ApiModelProperty("链接url")
    private String linkUrl;

    @NotEmpty
    @ApiModelProperty("类型")
    private BannerTypeEnum type;



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
}
