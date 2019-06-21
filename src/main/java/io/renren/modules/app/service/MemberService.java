package io.renren.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.*;
import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.entity.member.MemberAuths;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.form.*;

import java.util.List;
import java.util.Map;

/**
 * 用户
 *
 * @author xukaijun
 */
public interface MemberService extends IService<Member> {
    /**
     * 根据条件-如关键字(雷锋ID/昵称/手机号)搜索用户-分页
     *
     * @param form 搜索条件
     * @param page
     * @return
     */
    PageUtils<MemberDto> searchMembers(MemberQueryForm form, PageWrapper page);

    /**
     * 获取用户信息-根据id
     *
     * @param memberId
     * @return
     */
    MemberDto getMember(Long curMemberId, Long memberId);


    MemberDto getMember(Long memberId);

    /**
     * 更新用户信息
     *
     * @param form
     */
    void updateMember(MemberForm form);

    /**
     * 更新用户信息-根据微信用户信息
     */
    void wxUpdateMember(WxUserInfoForm userInfo);

    /**
     * 同时写入member表和member_auths表
     */
    void registerMemberWithAuth(Member member, MemberWalletEntity wallet,MemberAuths auths);

    void updateLocationNumber(Long memberId,LocationForm locationForm);


    /**
     * 关注用户
     *
     * @param fromMemberId 关注用户id
     * @param toMemberId   被关注用户id
     */
    void followMember(Long fromMemberId, Long toMemberId);

    /**
     * 取消关注
     *
     * @param fromMemberId 关注用户id
     * @param toMemberId   被关注用户id
     */
    void unfollowMember(Long fromMemberId, Long toMemberId);

    /**
     * 分页获取关注的用户列表
     *
     * @param fromMemberId
     */
    PageUtils<MemberDto> getFollowMembers(Long fromMemberId, PageWrapper page);

    /**
     * 获取用户关注总数和fans总数
     */
    FollowAndFansCountDto getFollowAndFansCount(Long memberId);

    /**
     * 分页获取粉丝用户列表
     *
     * @param toMemberId
     */
    PageUtils<MemberDto> getFansMembers(Long toMemberId, PageWrapper page);

    /**
     * 是否关注
     *
     * @param fromMemberId
     * @param toMemberId
     * @return
     */
    boolean isFollowed(Long fromMemberId, Long toMemberId);

    //任务是否已评分
    boolean isScored(Long taskId);

    /**
     * 用户评分
     *
     * @param judgerId 评分人id
     * @param form
     */
    void score(Long judgerId, MemberScoreForm form);

    //获取任务评分详情
    MemberScoreDto getScore(Long taskId);

    //获取用户评分列表
    PageUtils<MemberScoreDto> getMemberScores(Long memberId, PageWrapper page);

    void sendPhoneCode(String phoneNum) throws Exception;

    boolean validatePhoneCode(String phoneNum, String code);

    /**
     * 签到
     * @param memberId
     */
    Map<String,Object> checkIn(Long memberId, Integer experience);


    /**
     *获取用户技能雷达图数据
     * @return
     */
    List<SkillRadarChartDto> getSkillRadarChart(Long memberId);

    //用户增加经验值和虚拟币
    void incMemberExperienceAndVirtualCurrency(Long memberId, Integer experience,Integer virtualCurrency);

    //分页获取邀请好友列表
    PageUtils<InviteFriendsDto> getInviteFriends(Long inviteMemberId, PageWrapper page);

    //统计经验值和虚拟币
    ExperienceAndVirtualCurrencyDto getTotalExperienceAndVirtualCurrency(Long inviteMemberId);


    //任务完成数+inc
    void incTaskCompleteCount(Long memberId, Integer inc);

    //获取用户评分面板
    ScoreBoardDto getScoreBoard(Long memberId);

    //添加邀请好友
    void addInviteFriends(Long inviteMemberId,Long friendMemberId,Integer experience,Integer virtualCurrency);

    //保存用户反馈
    void saveFeedback(Long curMemberId, String content);
}
