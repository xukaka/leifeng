package io.renren.modules.app.dao.setting;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.task.TaskTagEntity;
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

    void updateLocationNumber(@Param("memberId")Long memberId,@Param("location") LocationForm locationForm);


    List<MemberDto> searchMembers(@Param("queryMap") Map<String, Object> queryMap, @Param("page")PageWrapper page);

    int count(@Param("queryMap") Map<String, Object> queryMap);

    List<String> getMemberTags(@Param("memberId") Long memberId);

    /**
     * 增加用户鲜花数
     * @param memberId 用户id
     * @param inc 增量
     */
    void incFlowerCount(@Param("memberId")Long memberId,@Param("inc")int inc);

    //增加用户经验值
    void incMemberExperience(@Param("memberId")Long memberId, @Param("experience")Integer experience);
}
