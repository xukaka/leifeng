package io.renren.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.entity.task.TaskTagEntity;
import io.renren.modules.app.form.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.redis.core.query.QueryUtils;

import java.util.List;

/**
 * 用户
 * @author xukaijun
 */
public interface MemberService extends IService<Member> {
	/**
	 * 根据条件-如关键字(雷锋ID/昵称/手机号)搜索用户-分页
	 * @param form 搜索条件
	 * @param page
	 * @return
	 */
	PageUtils<Member> searchMembers(MemberQueryForm form, PageWrapper page);

	/**
	 * 获取用户信息-根据id
	 * @param memberId
	 * @return
	 */
	Member getMember(Long memberId);

	/**
	 * 更新用户信息
	 * @param form
	 */
	void updateMember(MemberForm form);

	/**
	 * 同时写入member表和member_auths表
	 */
	void registerMemberWithAuth(Member member, MemberAuths auths);

	void updateLocationNumber(LocationForm locationForm);


	/**
	 * 关注用户
	 * @param fromMemberId 关注用户id
	 * @param toMemberId 被关注用户id
	 */
	void followMember(Long fromMemberId,Long toMemberId);

	/**
	 * 取消关注
	 * @param fromMemberId 关注用户id
	 * @param toMemberId 被关注用户id
	 */
	void unfollowMember(Long fromMemberId,Long toMemberId);

	/**
	 * 分页获取关注的用户列表
	 * @param fromMemberId
	 */
	PageUtils<Member> getFollowMembers(Long fromMemberId, PageWrapper page);

	/**
	 * 分页获取粉丝用户列表
	 * @param toMemberId
	 */
	PageUtils<Member> getFansMembers(Long toMemberId, PageWrapper page);

    /**
     * 是否关注
     * @param fromMemberId
     * @param toMemberId
     * @return
     */
    boolean isFollowed(Long fromMemberId,Long toMemberId);

	/**
	 * 用户评分
	 * @param judgeId 评分人id
	 * @param form
	 */
    void score(Long judgeId, MemberScoreForm form);

	void sendPhoneCode(String phoneNum) throws Exception;

	boolean validatePhoneCode(String phoneNum, String code);

//	//用户技能列表
//	List<TaskTagEntity> getMemberTags( Long memberId);
}
