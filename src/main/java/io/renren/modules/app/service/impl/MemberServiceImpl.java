package io.renren.modules.app.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.app.dao.setting.MemberDao;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.service.MemberAuthsService;
import io.renren.modules.app.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("MemberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {

	@Autowired
	private MemberAuthsService memberAuthsService;

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

}
