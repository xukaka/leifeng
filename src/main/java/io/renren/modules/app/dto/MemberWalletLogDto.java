package io.renren.modules.app.dto;

import java.io.Serializable;

/**
 * 钱包交易日志
 */
public class MemberWalletLogDto implements Serializable {

    private Long id;
    private String  outTradeNo;//交易流水号

    private Long memberId;//用户id

    private Long changeMoney;//变动金额 增+ 减-
    private Long money;//变动后的金额

    private String type;//日志类型：withdrawal提现，taskIncome任务入账

    private String tradeState;//交易状态

    private String remark;//备注

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
