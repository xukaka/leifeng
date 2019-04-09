package io.renren.modules.app.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import io.renren.common.utils.JsonUtil;
import io.renren.modules.app.dao.setting.MemberDao;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.service.MemberAuthsService;
import io.renren.modules.app.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


@Service("MemberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {

	@Autowired
	private MemberAuthsService memberAuthsService;

	@Value("${sms.appid}")
	private int appid;

	@Value("${sms.appkey}")
	private String appkey;

	@Value("${sms.templateId}")
	private int templateId;

	@Value("${sms.signName}")
	private String signName;

	private ConcurrentHashMap<String,String> phoneCodeMap = new ConcurrentHashMap<>();

	@Override
	@Transactional
	public void registerMemberWithAuth(Member member, MemberAuths auths) {
		//插入用户基本信息
		insert(member);
		auths.setMemberId(member.getId());
		memberAuthsService.insert(auths);
	}

	@Override
	public void updateLocationNumber(LocationForm locationForm) {
		this.baseMapper.updateLocationNumber(locationForm);
	}

	@Override
	public void sendPhoneCode(String phoneNum) throws Exception {
		//随机生成4位验证码
		String code =String.valueOf((new Random().nextInt(8999) + 1000));
		String[] params = {code};
		SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
		SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNum,
					templateId, params, signName, "", "");
		System.out.println("腾讯短信接口返回结果："+JsonUtil.Java2Json(result));
        phoneCodeMap.put(phoneNum,code);
	}

	@Override
	public boolean validatePhoneCode(String phoneNum, String code) {
        System.out.println("phoneCodeMap的内容为："+JsonUtil.Java2Json(phoneCodeMap));
		String originCode = phoneCodeMap.get(phoneNum);
		if(!StringUtils.isEmpty(code) && code.equals(originCode))
			return true;
		return false;
	}

}
