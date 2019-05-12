package io.renren.modules.app.dto;

import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.story.DiaryContentEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangshishui
 * @date 2019/4/26 0:49
 **/
public class DiaryDto implements Serializable {
    //日记id
    private Long id;

    //创建人
    private Member creator;
    //标题
    private String title;

    //日记内容
    private List<DiaryContentEntity> contents;
    //是否公开
    private Boolean isOpen;
    //浏览数
    private Integer viewCount;
    //评论数
    private Integer commentCount;
    //点赞数
    private Integer likeCount;
    //创建时间
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getCreator() {
        return creator;
    }

    public void setCreator(Member creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DiaryContentEntity> getContents() {
        return contents;
    }

    public void setContents(List<DiaryContentEntity> contents) {
        this.contents = contents;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
