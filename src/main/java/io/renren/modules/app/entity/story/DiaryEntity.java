package io.renren.modules.app.entity.story;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * @author huangshishui
 * @date 2019/4/18 22:49
 **/
@TableName("t_diary")
public class DiaryEntity extends BaseEntity {


    private Long creatorId;

    private String title;


    //是否私密
    private Boolean isPrivate = false;

    //    浏览数
    private Integer viewCount = 0;

    //评论数
    private Integer commentCount = 0;

    //点赞数
    private Integer likeCount = 0;

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

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}
