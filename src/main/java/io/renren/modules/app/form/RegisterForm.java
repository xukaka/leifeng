/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 注册表单
 * @author xukaijun
 */
@ApiModel(value = "注册表单")
public class RegisterForm {
    @ApiModelProperty(value = "手机号",example = "13301242325")
    private String mobile;

    @ApiModelProperty(value = "登录账号",example = "nickname")
   // @NotBlank(message="登录的账号")
    private String identifier;

    @ApiModelProperty(value = "登录密码",example = "password")
    @NotBlank(message="登录的密码")
    private String credential;

    @ApiModelProperty(value = "昵称",example = "xukaka")
    private String nickName;

    @ApiModelProperty(value = "真实姓名",example = "徐卡卡")
    private String realName;

    @ApiModelProperty(value = "头像图片",example = "http://www.baidu.com")
    private String avatar;

    @ApiModelProperty(value = "个性签名",example = "hello world")
    private String selfIntro;

    @ApiModelProperty(value = "地址",example = "北京市")
    private String address;

    @ApiModelProperty(value = "性别0男，1女",example = "0")
    private String sex;

    @ApiModelProperty(value = "邮箱",example = "383635738@qq.com")
    private String email;

    @ApiModelProperty(value = "注册类型wx:微信，mobile：手机",example = "wx")
    @NotBlank(message = "注册类型")
    private String identityType;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    @Override
    public String toString() {
        return "RegisterForm{" +
                "mobile='" + mobile + '\'' +
                ", identifier='" + identifier + '\'' +
                ", nickName='" + nickName + '\'' +
                ", realName='" + realName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", selfIntro='" + selfIntro + '\'' +
                ", address='" + address + '\'' +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", identityType='" + identityType + '\'' +
                '}';
    }
}
