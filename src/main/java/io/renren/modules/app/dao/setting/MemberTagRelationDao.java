package io.renren.modules.app.dao.setting;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberTagRelationEntity;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户-标签关联关系
 */
@Mapper
public interface MemberTagRelationDao extends BaseMapper<MemberTagRelationEntity> {

}
