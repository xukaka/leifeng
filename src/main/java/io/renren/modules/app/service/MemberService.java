package io.renren.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.form.MemberForm;
import io.renren.modules.app.form.PageWrapper;

/**
 * 用户
 * @author xukaijun
 */
public interface MemberService extends IService<Member> {
	/**
	 * 根据关键字(雷锋ID/昵称/手机号)搜索用户-分页
	 * @param keyword
	 * @param page
	 * @return
	 */
	PageUtils<Member> searchMembers(String keyword,PageWrapper page);

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
	 *
	 *
	 */
	void registerMemberWithAuth(Member member, MemberAuths auths);

	void updateLocationNumber(LocationForm locationForm);

	void sendPhoneCode(String phoneNum) throws Exception;

	boolean validatePhoneCode(String phoneNum, String code);
}
