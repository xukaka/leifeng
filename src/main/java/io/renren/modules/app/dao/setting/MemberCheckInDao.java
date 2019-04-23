package io.renren.modules.app.dao.setting;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.setting.MemberCheckInEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户签到表
 */
@Mapper
public interface MemberCheckInDao extends BaseMapper<MemberCheckInEntity> {


}
