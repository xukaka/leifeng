package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 登录表单
 * @author xukaijun
 */
@ApiModel(value = "登录表单")
public class LoginForm {
    @ApiModelProperty(value = "手机号",example = "13301242325")
    private String mobile;

    @ApiModelProperty(value = "注册类型wx:微信，mobile：手机",example = "wx")
    @NotBlank(message = "登录方式必填")
    private String identityType;

    @ApiModelProperty(value = "登录账号",example = "wx_open_id")
    @NotBlank(message="登录账号必填")
    private String identifier;

    @ApiModelProperty(value = "登录秘密凭证",example = "password")
    @NotBlank(message="登录密码凭证必填")
    private String credential;

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
}
