package io.renren.modules.app.dto;

import java.io.Serializable;

/**
 * 校验金额
 */
public class MoneyCheckDto implements Serializable {

    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 总支出
     */
    private Long totalExpense;

    //总收入
    private Long totalIncome;

    //总余额
    private Long totalBalance;
    //校验状态：成功success，错误error
    private String checkStatus;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Long totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Long getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Long totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Long getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Long totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }
}
