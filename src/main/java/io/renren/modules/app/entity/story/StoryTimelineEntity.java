package io.renren.modules.app.entity.story;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * 时间轴表，核心表
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 09:38:43
 */
@TableName("lf_story_timeline")
public class StoryTimelineEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;
	/**
	 * 关联member表
	 */
	private Long memberId;
	/**
	 * 关联朋友圈分享的消息
	 */
	private Long pmId;
	/**
	 * 是否是自己的，0否1是
	 */
	private Integer isOwn;
	/**
	 * 
	 */
	private Long createTime;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	/**
	 * 获取：
	 */
	public Long getMemberId() {
		return memberId;
	}

	public Long getPmId() {
		return pmId;
	}

	public void setPmId(Long pmId) {
		this.pmId = pmId;
	}

	/**
	 * 设置：是否是自己的，0否1是
	 */
	public void setIsOwn(Integer isOwn) {
		this.isOwn = isOwn;
	}
	/**
	 * 获取：是否是自己的，0否1是
	 */
	public Integer getIsOwn() {
		return isOwn;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Long getCreateTime() {
		return createTime;
	}
}
