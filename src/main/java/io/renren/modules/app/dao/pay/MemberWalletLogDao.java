package io.renren.modules.app.dao.pay;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.pay.MemberWalletLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户钱包日志
 */
@Mapper
public interface MemberWalletLogDao extends BaseMapper<MemberWalletLogEntity> {

  }