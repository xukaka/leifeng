package io.renren.modules.app.dao.setting;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.form.MemberQueryForm;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户表
 * @author xukaijun
 */
@Mapper
public interface MemberDao extends BaseMapper<Member> {

    void updateLocationNumber(@Param("location") LocationForm locationForm);


    List<Member> searchMembers(@Param("queryMap") Map<String, Object> queryMap, @Param("page")PageWrapper page);

    int count(@Param("queryMap") Map<String, Object> queryMap);
}
