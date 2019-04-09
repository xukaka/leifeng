package io.renren.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import com.github.qcloudsms.httpclient.HTTPException;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.form.LocationForm;

import java.io.IOException;

/**
 * 用户
 * @author xukaijun
 */
public interface MemberService extends IService<Member> {

	/**
	 * 同时写入member表和member_auths表
	 */
	void registerMemberWithAuth(Member member, MemberAuths auths);

	void updateLocationNumber(LocationForm locationForm);

	void sendPhoneCode(String phoneNum) throws Exception;

	boolean validatePhoneCode(String phoneNum, String code);
}
