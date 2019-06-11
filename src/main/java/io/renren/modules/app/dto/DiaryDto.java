package io.renren.modules.app.dto;

import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.entity.story.DiaryContentEntity;

import java.io.Serializable;
import java.util.List;


public class DiaryDto implements Serializable {
    //日记id
    private Long id;

    //创建人
    private Member creator;

    //头像
    private String avatar;
    //标题
    private String title;

    //描述
    private String description;

    //日记内容
    private List<DiaryContentEntity> contents;
    //是否私密
    private Boolean isPrivate;
    //浏览数
    private Integer viewCount;
    //评论数
    private Integer commentCount;
    //点赞数
    private Integer likeCount;
    /**
     * 是否点赞
     */
    private Boolean isLiked= false;
    //创建时间
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public String getAvatar() {

        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
