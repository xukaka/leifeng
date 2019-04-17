package io.renren.modules.app.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
//import com.github.qcloudsms.SmsSingleSender;
//import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.GeoUtils;
import io.renren.common.utils.JsonUtil;
import io.renren.common.utils.PageUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.setting.MemberDao;
import io.renren.modules.app.dao.setting.MemberFollowDao;
import io.renren.modules.app.dao.setting.MemberScoreDao;
import io.renren.modules.app.dto.TaskCommentDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.TaskDifficultyEnum;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberAuths;
import io.renren.modules.app.entity.setting.MemberFollowEntity;
import io.renren.modules.app.entity.setting.MemberScoreEntity;
import io.renren.modules.app.entity.task.TaskAddressEntity;
import io.renren.modules.app.entity.task.TaskTagEntity;
import io.renren.modules.app.form.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


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

    private ConcurrentHashMap<String, String> phoneCodeMap = new ConcurrentHashMap<>();

    @Override
    public PageUtils<Member> searchMembers(MemberQueryForm form, PageWrapper page) {
        Map<String, Object> queryMap = getMemberQueryMap(form);
        List<Member> members = this.baseMapper.searchMembers(queryMap, page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }
        int total = this.baseMapper.count(queryMap);
        setMemberTags(members);
        setMemberDistance(form, members);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }

    //设置用户技能标签
    private void setMemberTags(List<Member> members) {
        for (Member member : members) {
            List<String> tags = this.baseMapper.getMemberTags(member.getId());
            member.setTags(tags);
        }
    }

    private Map<String, Object> getMemberQueryMap(MemberQueryForm form) {
        Map<String, Object> queryMap = new HashMap<>();
        if (!StringUtils.isEmpty(form.getKeyword())) {
            queryMap.put("keyword", form.getKeyword());
        }
        if (form.getLatitude() != null && form.getLongitude() != null && form.getRaidus() != null) {
            Map<String, Double> aroundMap = GeoUtils.getAround(form.getLatitude(), form.getLongitude(), form.getRaidus());
            queryMap.putAll(aroundMap);
        }
        return queryMap;
    }

    //计算距离
    private void setMemberDistance(MemberQueryForm form, List<Member> members) {
        for (Member member : members) {
            long distance = 0L;
            if (form.getLatitude() != null && form.getLongitude() != null
                    && member.getLat() != null && member.getLng() != null) {
                distance = GeoUtils.getDistance(form.getLatitude(), form.getLongitude(), member.getLat(), member.getLng());
            }
            member.setDistance(distance);
        }
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
        List<Member> members = memberFollowDao.getFollowMembers(fromMemberId, page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }
        int total = memberFollowDao.followCount(fromMemberId);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public PageUtils<Member> getFansMembers(Long toMemberId, PageWrapper page) {
        List<Member> members = memberFollowDao.getFansMembers(toMemberId, page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }
        int total = memberFollowDao.fansCount(toMemberId);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public boolean isFollowed(Long fromMemberId, Long toMemberId) {
        return memberFollowDao.isFollowed(fromMemberId, toMemberId);
    }


    @Override
    public void score(Long judgeId, MemberScoreForm form) {
        ValidatorUtils.validateEntity(form);
        MemberScoreEntity score = new MemberScoreEntity();
        BeanUtils.copyProperties(form, score);
        score.setCreateTime(DateUtils.now());
        memberScoreDao.insert(score);

        //TODO 发送消息给被评分人
    }

    @Override
    public void sendPhoneCode(String phoneNum) throws Exception {
        //随机生成4位验证码
        String code = String.valueOf((new Random().nextInt(8999) + 1000));
        String[] params = {code};
        SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
        SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNum,
                templateId, params, signName, "", "");
        System.out.println("腾讯短信接口返回结果：" + JsonUtil.Java2Json(result));
        phoneCodeMap.put(phoneNum, code);
    }

    @Override
    public boolean validatePhoneCode(String phoneNum, String code) {
        System.out.println("phoneCodeMap的内容为：" + JsonUtil.Java2Json(phoneCodeMap));
        String originCode = phoneCodeMap.get(phoneNum);
        if (!StringUtils.isEmpty(code) && code.equals(originCode))
            return true;
        return false;
    }


}
