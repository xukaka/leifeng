package io.renren.modules.app.dao.member;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.dto.SkillRadarChartDto;
import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.form.LocationForm;
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



    //增加用户经验值
    void incMemberExperience(@Param("memberId")Long memberId, @Param("inc")Integer inc);

    //增加用户虚拟币
    void incMemberVirtualCurrency(@Param("memberId")Long memberId,@Param("inc")Integer inc);
    //增加用户积分值
    void incMemberIntegralValue(@Param("memberId")Long memberId,@Param("inc")Integer inc);

    //获取用户技能雷达图书数据
    List<SkillRadarChartDto> getSkillRadarChart(@Param("memberId")Long memberId);

    //任务完成数+inc
    void incTaskCompleteCount(@Param("memberId")Long memberId,@Param("inc") Integer inc);
}
