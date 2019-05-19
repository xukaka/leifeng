package io.renren.modules.app.entity.task;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.modules.app.entity.BaseEntity;

@TableName("t_withdrawal_order")
public class WithdrawalOrderEntity extends BaseEntity {

    //提现订单号
    private String outTradeNo;
    //关联用户id
    private Long memberId;
    //订单金额，单位为分
    private Long totalFee;
    //交易状态，SUCCESS—提现成功
    // CLOSED—已关闭,PAYERROR--支付失败，AUDIT--审核中
    private String tradeState;
    //附件描述信息
    private String attach;


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

}
