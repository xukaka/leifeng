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
    @NotBlank(message="手机号必填")
    private String phone;

    @ApiModelProperty(value = "注册类型wx:微信，phone：手机,暂不填",example = "wx")
    private String identityType;

    @ApiModelProperty(value = "登录账号，暂不用",example = "wx_open_id")
    private String identifier;

    @ApiModelProperty(value = "登录秘密凭证",example = "password")
    @NotBlank(message="登录密码凭证必填")
    private String credential;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
