package io.renren.modules.app.entity.story;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * 订阅，关注表
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 09:38:43
 */
@TableName("lf_subscribe")
public class SubscribeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;
	/**
	 * 发起关注的用户
	 */
	private Long memberId;
	/**
	 * 被订阅的用户
	 */
	private Long subscriberId;
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
	 * 设置：发起关注的用户
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	/**
	 * 获取：发起关注的用户
	 */
	public Long getMemberId() {
		return memberId;
	}
	/**
	 * 设置：被订阅的用户
	 */
	public void setSubscriberId(Long subscriberId) {
		this.subscriberId = subscriberId;
	}
	/**
	 * 获取：被订阅的用户
	 */
	public Long getSubscriberId() {
		return subscriberId;
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
