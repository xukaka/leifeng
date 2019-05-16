package io.renren.modules.app.entity.member;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * 用户反馈表
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 16:03:12
 */
@TableName("member_feedback")
public class MemberFeedback implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long memberId;
	/**
	 * 
	 */
	private String content;
	/**
	 * 
	 */
	private Long createTime;


	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Long getCreateTime() {
		return createTime;
	}
}
