package io.renren.modules.app.form;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 朋友圈分享内容实体
 * @author xukaijun
 */
@ApiModel("发布的内容实体")
public class PublishMessageForm implements Serializable {

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
	 * 图片列表
	 */
	private String images;
	/**
	 * 地址
	 */
	private String location;
	/**
	 * 经度
	 */
	@ApiModelProperty("经度")
	private String lng;
	/**
	 * 纬度
	 */
	@ApiModelProperty("纬度")
	private String lat;

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

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
