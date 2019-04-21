package io.renren.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.form.*;
import io.renren.modules.app.utils.ReqUtils;

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
    void wxUpdateMember(Long memberId,String nickName,String avatar,Integer sex);

    /**
     * 同时写入member表和member_auths表
     */
    void registerMemberWithAuth(Member member, MemberAuths auths);

    void updateLocationNumber(LocationForm locationForm);


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

    /**
     * 用户评分
     *
     * @param judgerId 评分人id
     * @param form
     */
    void score(Long judgerId, MemberScoreForm form);

    void sendPhoneCode(String phoneNum) throws Exception;

    boolean validatePhoneCode(String phoneNum, String code);

}
