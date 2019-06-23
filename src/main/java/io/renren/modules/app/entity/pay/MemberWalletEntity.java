package io.renren.modules.app.entity.pay;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 用户钱包交易记录
 */
@TableName("t_member_wallet")
public class MemberWalletEntity extends BaseEntity{

    private Long memberId;//用户id

    private Long money;//金额，单位为分

    private String salt;//32位密码随机干扰字符串
    private String payPassword;//支付密码
    private String realName;//提现用户真实姓名，不可更改
    private String idcard;//'身份证号
    private String openId;//提现微信openId

    private String withdrawalRqCode;//提现二维码

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWithdrawalRqCode() {
        return withdrawalRqCode;
    }

    public void setWithdrawalRqCode(String withdrawalRqCode) {
        this.withdrawalRqCode = withdrawalRqCode;
    }

    @Override
    public String toString() {
        return "MemberWalletEntity{" +
                "memberId=" + memberId +
                ", money=" + money +
                ", salt='" + salt + '\'' +
                ", payPassword='" + payPassword + '\'' +
                ", realName='" + realName + '\'' +
                ", idcard='" + idcard + '\'' +
                ", openId='" + openId + '\'' +
                '}';
    }
}
