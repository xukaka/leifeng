package io.renren.modules.app.dao.pay;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.pay.MemberWalletRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户钱包交易记录
 */
@Mapper
public interface MemberWalletRecordDao extends BaseMapper<MemberWalletRecordEntity> {


}