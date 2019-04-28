package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author huangshishui
 * @date 2019/4/18 22:39
 **/
@ApiModel("英雄事迹")
public class DeedsForm {

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("英雄事迹类型  0代表管理员推送帖子  1代表")
    private String deedsType;

    @ApiModelProperty("事迹主题")
    private String title;

    @ApiModelProperty("事迹主要内容")
    private String context;

    @ApiModelProperty("图像地址：阿里云地址")
    private String imageUrl;

    @ApiModelProperty("文章是否公开 1 私密  2 所有人可见")
    private String isOpen;

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeedsType() {
        return deedsType;
    }

    public void setDeedsType(String deedsType) {
        this.deedsType = deedsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
