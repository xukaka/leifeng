package io.renren.modules.app.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
//import com.github.qcloudsms.SmsSingleSender;
//import com.github.qcloudsms.SmsSingleSenderResult;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.JsonUtil;
import io.renren.common.utils.PageUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.setting.MemberDao;
import io.renren.modules.app.dao.setting.MemberFollowDao;
import io.renren.modules.app.dao.setting.MemberScoreDao;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.entity.setting.MemberFollowEntity;
import io.renren.modules.app.entity.setting.MemberScoreEntity;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.form.MemberForm;
import io.renren.modules.app.form.MemberScoreForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.MemberAuthsService;
import io.renren.modules.app.service.MemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


@Service("MemberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {

    @Resource
    private MemberAuthsService memberAuthsService;
    @Resource
    private MemberFollowDao memberFollowDao;
    @Resource
    private MemberScoreDao memberScoreDao;

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
    public PageUtils<Member> searchMembers(String keyword, PageWrapper page) {
        List<Member>  members = this.baseMapper.searchMembers(keyword,page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }
        int total = this.baseMapper.count(keyword);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }

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

    @Override
    public void score(Long judgeId, MemberScoreForm form) {
        ValidatorUtils.validateEntity(form);
        MemberScoreEntity score = new MemberScoreEntity();
        BeanUtils.copyProperties(form,score);
        score.setCreateTime(DateUtils.now());
        memberScoreDao.insert(score);

        //TODO 发送消息给被评分人
    }

   /* @Override
    public void sendPhoneCode(String phoneNum) throws Exception {
        //随机生成4位验证码
        String code =String.valueOf((new Random().nextInt(8999) + 1000));
        String[] params = {code};
        SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
        SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNum,
                templateId, params, signName, "", "");
        System.out.println("腾讯短信接口返回结果："+ JsonUtil.Java2Json(result));
        phoneCodeMap.put(phoneNum,code);
    }

    @Override
    public boolean validatePhoneCode(String phoneNum, String code) {
        System.out.println("phoneCodeMap的内容为："+JsonUtil.Java2Json(phoneCodeMap));
        String originCode = phoneCodeMap.get(phoneNum);
        if(!StringUtils.isEmpty(code) && code.equals(originCode))
            return true;
        return false;
    }*/

}
