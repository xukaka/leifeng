package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 注册的用户表
 * @author xukaijun
 */
@ApiModel("用户表单")
public class MemberForm implements Serializable {

    @NotNull
    private Long id;
    //昵称
    private String nickName;
    //真实姓名
    private String realName;
    //头像
    @ApiModelProperty("头像图片url")
    private String avatar;
    //个性说明
    @ApiModelProperty("自我介绍")
    private String selfIntro;
    //地址
    private String address;
    //性别0未知,1男，2女
    @ApiModelProperty("性别0未知,1男，2女")
    private Integer sex;
    //邮箱
    private String email;
    //电话
    private String mobile;

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

}
