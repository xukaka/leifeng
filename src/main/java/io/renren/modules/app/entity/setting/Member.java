package io.renren.modules.app.entity.setting;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.task.TaskTagEntity;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

/**
 * 注册的用户表
 * @author xukaijun
 */
@TableName("member")
@ApiModel
public class Member implements Serializable {

    @TableId
    private Long id;
    //昵称
    private String nickName;
    //真实姓名
    private String realName;
    //头像
    private String avatar;
    //个性说明
    private String selfIntro;
    //等级
    private Integer level;
    //维度
    private Double lat;
    //经度
    private Double lng;

    //地址
    private String address;
    //性别0未知,1男，2女
    private Integer sex;
    //邮箱
    private String email;
    //电话
    private String mobile;
    //状态
    private Integer status=1;
    //鲜花数
    private Integer flowerCount = 0;
    //奖状数
    private Integer certificateCount = 0;
    //经验值
    private Integer experience = 0;
    //创建时间
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getFlowerCount() {
        return flowerCount;
    }

    public void setFlowerCount(Integer flowerCount) {
        this.flowerCount = flowerCount;
    }

    public Integer getCertificateCount() {
        return certificateCount;
    }

    public void setCertificateCount(Integer certificateCount) {
        this.certificateCount = certificateCount;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }
}
