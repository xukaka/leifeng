package io.renren.modules.app.dto;

import java.io.Serializable;

/**
 * 用户钱包
 */
public class MemberWalletDto implements Serializable {

    private Long id;//钱包id

    private Long memberId;
    //昵称
    private String nickName;
    //头像
    private String avatar;
    private Integer sex;

    /**
     * 钱包余额
     */
    private Long money;

    //提现二维码
    private String withdrawalRqCode;


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

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

    public String getWithdrawalRqCode() {
        return withdrawalRqCode;
    }

    public void setWithdrawalRqCode(String withdrawalRqCode) {
        this.withdrawalRqCode = withdrawalRqCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
