package io.renren.modules.app.dao.setting;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberScoreEntity;
import io.renren.modules.app.form.LocationForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户评分表
 */
@Mapper
public interface MemberScoreDao extends BaseMapper<MemberScoreEntity> {

}
