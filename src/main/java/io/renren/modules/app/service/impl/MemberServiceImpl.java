package io.renren.modules.app.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.setting.MemberDao;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.form.MemberForm;
import io.renren.modules.app.service.MemberAuthsService;
import io.renren.modules.app.service.MemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("MemberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {

    @Autowired
    private MemberAuthsService memberAuthsService;

    @Override
    public Member getMember(Long memberId) {
        return this.baseMapper.selectById(memberId);
    }

    @Override
    public void updateMember(MemberForm form) {
        ValidatorUtils.validateEntity(form);
        Member member = this.selectById(form.getId());
        if (member != null) {
            member.setNickName(form.getNickName());
            member.setAvatar(form.getAvatar());
            member.setSex(form.getSex());
            member.setSelfIntro(form.getSelfIntro());
            this.updateById(member);
        }
    }

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
