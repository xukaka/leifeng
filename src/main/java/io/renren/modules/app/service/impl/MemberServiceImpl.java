package io.renren.modules.app.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.setting.MemberDao;
import io.renren.modules.app.dao.setting.MemberFollowDao;
import io.renren.modules.app.dto.TaskCommentDto;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.entity.setting.MemberFollowEntity;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.form.MemberForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.MemberAuthsService;
import io.renren.modules.app.service.MemberService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Service("MemberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {

    @Autowired
    private MemberAuthsService memberAuthsService;
    @Autowired
    private MemberFollowDao memberFollowDao;

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

    @Override
    public void followMember(Long fromMemberId, Long toMemberId) {
        MemberFollowEntity follow = new MemberFollowEntity(DateUtils.now(), fromMemberId, toMemberId);
        memberFollowDao.insert(follow);
    }

    @Override
    public void unfollowMember(Long fromMemberId, Long toMemberId) {
        Wrapper<MemberFollowEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("from_member_id", fromMemberId)
                .eq("to_member_id", toMemberId);
        memberFollowDao.delete(wrapper);
    }

    @Override
    public PageUtils<Member> getFollowMembers(Long fromMemberId, PageWrapper page) {
        List<Member> members = memberFollowDao.getFollowMembers(fromMemberId,page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }
        int total = memberFollowDao.followCount(fromMemberId);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public PageUtils<Member> getFansMembers(Long toMemberId, PageWrapper page) {
        List<Member> members = memberFollowDao.getFansMembers(toMemberId,page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }
        int total = memberFollowDao.fansCount(toMemberId);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }


}
