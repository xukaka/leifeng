package io.renren.modules.app.dto;

import io.renren.modules.app.entity.member.Member;

import java.io.Serializable;

/**
 * 提现订单
 */
public class WithdrawalOrderDto implements Serializable {

    private Long id;
    //提现订单号
    private String outTradeNo;
    //提现用户
    private Member creator;
    //订单金额，单位为分
    private Long totalFee;
    //交易状态，SUCCESS—提现成功
    // CLOSED—已关闭,PAYERROR--支付失败，AUDIT--审核中
    private String tradeState;
    //附件描述信息
    private String attach;

    //创建时间
    private Long createTime;


    //钱包id
    private Long walletId;
    //提现二维码
    private String withdrawalRqCode;

    //二维码状态：0未验证，1有效 ，2无效
    private Integer rqCodeStatus;

    //二维码备注
    private String rqCodeRemark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Member getCreator() {
        return creator;
    }

    public void setCreator(Member creator) {
        this.creator = creator;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getWithdrawalRqCode() {
        return withdrawalRqCode;
    }

    public void setWithdrawalRqCode(String withdrawalRqCode) {
        this.withdrawalRqCode = withdrawalRqCode;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Integer getRqCodeStatus() {
        return rqCodeStatus;
    }

    public void setRqCodeStatus(Integer rqCodeStatus) {
        this.rqCodeStatus = rqCodeStatus;
    }

    public String getRqCodeRemark() {
        return rqCodeRemark;
    }

    public void setRqCodeRemark(String rqCodeRemark) {
        this.rqCodeRemark = rqCodeRemark;
    }
}
