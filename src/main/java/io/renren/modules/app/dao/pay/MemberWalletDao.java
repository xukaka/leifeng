package io.renren.modules.app.dao.pay;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.MemberWalletDto;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户钱包
 */
@Mapper
public interface MemberWalletDao extends BaseMapper<MemberWalletEntity> {

    void incMoney(@Param("memberId")Long memberId,@Param("inc")Long inc);

    MemberWalletDto getWallet(@Param("memberId")Long memberId);
  }