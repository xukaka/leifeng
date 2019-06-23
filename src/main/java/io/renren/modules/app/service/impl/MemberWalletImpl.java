package io.renren.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.app.dao.pay.MemberWalletDao;
import io.renren.modules.app.dto.MemberWalletDto;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.task.TaskReceiveEntity;
import io.renren.modules.app.service.MemberWalletService;
import org.springframework.stereotype.Service;

import java.awt.*;


@Service
public class MemberWalletImpl extends ServiceImpl<MemberWalletDao, MemberWalletEntity> implements MemberWalletService {


    @Override
    public void incMoney(Long memberId, Long inc) {
        baseMapper.incMoney(memberId,inc);
    }

    @Override
    public MemberWalletDto getWallet(Long memberId) {
        return baseMapper.getWallet(memberId);
    }

    @Override
    public void withdrawalRqCode(Long curMemberId, Long walletId, String withdrawalRqCode) {
        Wrapper<MemberWalletEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("member_id", curMemberId)
                .eq("id", walletId);
        MemberWalletEntity wallet = new MemberWalletEntity();
        wallet.setWithdrawalRqCode(withdrawalRqCode);
        baseMapper.update(wallet,wrapper);
    }
}
