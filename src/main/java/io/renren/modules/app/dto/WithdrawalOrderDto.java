package io.renren.modules.app.dto;

import io.renren.modules.app.entity.TaskStatusEnum;
import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.entity.task.TaskAddressEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 提现订单
 */
public class WithdrawalOrderDto implements Serializable {

    private Long id;
    //提现订单号
    private String outTradeNo;
    //提现用户
    private Member member;
    //订单金额，单位为分
    private Long totalFee;
    //交易状态，SUCCESS—提现成功
    // CLOSED—已关闭,PAYERROR--支付失败，AUDIT--审核中
    private String tradeState;
    //附件描述信息
    private String attach;
    //提现时间格式为yyyyMMddHHmmss
    private String timeEnd;

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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
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

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
