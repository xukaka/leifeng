package io.renren.modules.app.dao.member;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.member.MemberScoreEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户评分表
 */
@Mapper
public interface MemberScoreDao extends BaseMapper<MemberScoreEntity> {

}
