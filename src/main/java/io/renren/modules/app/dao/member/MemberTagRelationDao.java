package io.renren.modules.app.dao.member;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.member.MemberTagRelationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户-标签关联关系
 */
@Mapper
public interface MemberTagRelationDao extends BaseMapper<MemberTagRelationEntity> {

}
