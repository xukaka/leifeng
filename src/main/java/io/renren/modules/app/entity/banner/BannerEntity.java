package io.renren.modules.app.entity.banner;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BannerTypeEnum;
import io.renren.modules.app.entity.BaseEntity;


@TableName("t_banner")
public class BannerEntity extends BaseEntity {


    //创建人id
    private Long creatorId;

    //标题
    private String title;

    //封面url
    private String coverUrl;


    //链接url
    private String linkUrl;

    //类型
    private BannerTypeEnum type;

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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
}
