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
}
