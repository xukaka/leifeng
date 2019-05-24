package io.renren.modules.app.dao.pay;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.pay.MemberWalletLogEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户钱包日志
 */
@Mapper
public interface MemberWalletLogDao extends BaseMapper<MemberWalletLogEntity> {

  //分页获取交易日志列表
  List<MemberWalletLogEntity> getLogs(@Param("memberId")Long memberId,@Param("page") PageWrapper page);

  //获取交易日志总数
  int count(@Param("memberId")Long memberId);
}