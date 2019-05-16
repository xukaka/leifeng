package io.renren.modules.app.dao.pay;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.CommentDto;
import io.renren.modules.app.entity.CommentTypeEnum;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.pay.MemberWalletRecordEntity;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户钱包
 */
@Mapper
public interface MemberWalletDao extends BaseMapper<MemberWalletEntity> {

    void incMoney(@Param("memberId")Long memberId,@Param("inc")Long inc);
  }