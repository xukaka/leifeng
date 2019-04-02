package io.renren.modules.app.entity.story;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 朋友圈分享内容实体表
 * @author xukaijun
 * @email 383635738@qq.com
 * @date 2019-03-02 09:38:43
 */
@TableName("lf_publish_message")
public class PublishMessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;
	/**
	 * 关联member表
	 */
	@NotBlank(message = "memberId不能为空")
	private Long memberId;

	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 图片
	 */
	private String images;
	/**
	 * 经度
	 */
	private String lng;
	/**
	 * 纬度
	 */
	private String lat;
	/**
	 * 地址定位
	 */
	private String location;
	/**
	 * 内容是分享过来的话，关联分享表
	 */
	private Long shareId;
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
	/**
	 * 设置：内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：图片
	 */
	public void setImages(String images) {
		this.images = images;
	}
	/**
	 * 获取：图片
	 */
	public String getImages() {
		return images;
	}
	/**
	 * 设置：地址定位
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * 获取：地址定位
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * 设置：
	 */
	public void setShareId(Long shareId) {
		this.shareId = shareId;
	}
	/**
	 * 获取：
	 */
	public Long getShareId() {
		return shareId;
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

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
