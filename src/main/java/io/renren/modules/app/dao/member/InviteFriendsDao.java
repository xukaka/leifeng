package io.renren.modules.app.dao.member;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.app.dto.ExperienceAndVirtualCurrencyDto;
import io.renren.modules.app.dto.InviteFriendsDto;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.dto.SkillRadarChartDto;
import io.renren.modules.app.entity.member.InviteFriendsEntity;
import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.form.PageWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 邀请好友
 */
@Mapper
public interface InviteFriendsDao extends BaseMapper<InviteFriendsEntity> {


    // 分页获取邀请好友列表
    List<InviteFriendsDto> getInviteFriends(@Param("inviteMemberId") Long inviteMemberId, @Param("page") PageWrapper page);
    //获取邀请好友总数
    int count(@Param("inviteMemberId") Long inviteMemberId);

    //统计经验值和虚拟币
    ExperienceAndVirtualCurrencyDto getTotalExperienceAndVirtualCurrency(@Param("inviteMemberId") Long inviteMemberId);
}
