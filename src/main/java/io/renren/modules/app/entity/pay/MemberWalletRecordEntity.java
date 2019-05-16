package io.renren.modules.app.entity.pay;

import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;
import io.renren.modules.app.entity.LikeTypeEnum;

/**
 * 用户钱包交易记录
 */
@TableName("t_member_wallet_record")
public class MemberWalletRecordEntity extends BaseEntity {

    //交易流水号，ymdHis+taskId
    private String outTradeNo;
    //支付方用户id，0系统充值
    private Long fromMemberId;
    //接收方用户id，0系统提现
    private Long toMemberId;
    //交易类型：1充值 2提现 3交易
    private Integer type=3;

    //交易金额,单位：分
    private Long money;
    ////支付方式 0待定 1支付宝 2微信 3银行卡 4余额
    private Integer payType=0;
    //备注信息
    private String remark;
    //支付状态 0待支付 -1失败 1成功
    private Integer payStatus=0;
    //交易时间
    private Long payTime;
    //收款状态 0待收款 -1失败 1成功
    private Integer fetchStatus=0;
    //收款时间
    private Long fetchTime;
    //对账状态 0未对账 1已对账
    private Integer checkStatus=0;
    //对账时间
    private Long checkTime;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Long getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(Long fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    public Long getToMemberId() {
        return toMemberId;
    }

    public void setToMemberId(Long toMemberId) {
        this.toMemberId = toMemberId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Integer getFetchStatus() {
        return fetchStatus;
    }

    public void setFetchStatus(Integer fetchStatus) {
        this.fetchStatus = fetchStatus;
    }

    public Long getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(Long fetchTime) {
        this.fetchTime = fetchTime;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }
}
