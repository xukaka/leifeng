package io.renren.modules.app.entity.pay;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

/**
 * 用户钱包日志
 */
@TableName("t_member_wallet_log")
public class MemberWalletLogEntity extends BaseEntity {

    private String  outTradeNo;//交易流水号

    private Long memberId;//用户id

    private Long changeMoney;//变动金额 增+ 减-
    private Long money;//变动后的金额

    private String remark;//备注

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(Long changeMoney) {
        this.changeMoney = changeMoney;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
