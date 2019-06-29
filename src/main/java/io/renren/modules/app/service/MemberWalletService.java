package io.renren.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.app.dto.MemberWalletDto;
import io.renren.modules.app.entity.pay.MemberWalletEntity;

/**
 * 用户钱包
 */
public interface MemberWalletService extends IService<MemberWalletEntity> {

    /**
     * 用户金额增加+inc
     * @param memberId
     * @param inc
     */
    void incMoney(Long memberId,Long inc);

    //获取钱包信息
    MemberWalletDto getWallet(Long memberId);

    //设置提现二维码
    void withdrawalRqCode(Long curMemberId, Long walletId, String withdrawalRqCode);
    //设置提现二维码状态和备注
    void rqCodeStatusAndRemark(Long walletId, Integer rqCodeStatus, String rqCodeRemark);
}

