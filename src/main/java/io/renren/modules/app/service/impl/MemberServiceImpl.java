package io.renren.modules.app.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import io.renren.common.exception.RRException;
import io.renren.common.utils.*;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.setting.MemberCheckInDao;
import io.renren.modules.app.dao.setting.MemberDao;
import io.renren.modules.app.dao.setting.MemberFollowDao;
import io.renren.modules.app.dao.setting.MemberScoreDao;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.entity.im.ImGroupMemer;
import io.renren.modules.app.entity.setting.*;
import io.renren.modules.app.form.*;
import io.renren.modules.app.service.MemberAuthsService;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.utils.ReqUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//import com.github.qcloudsms.SmsSingleSender;
//import com.github.qcloudsms.SmsSingleSenderResult;


@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {
    private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    @Resource
    private MemberAuthsService memberAuthsService;
    @Resource
    private MemberFollowDao memberFollowDao;
    @Resource
    private MemberScoreDao memberScoreDao;
    @Resource
    private MemberCheckInDao memberCheckInDao;
    @Resource
    private RedisUtils redisUtils;

    @Value("${sms.appid}")
    private int appid;

    @Value("${sms.appkey}")
    private String appkey;

    @Value("${sms.templateId}")
    private int templateId;

    @Value("${sms.signName}")
    private String signName;

//    private ConcurrentHashMap<String, String> phoneCodeMap = new ConcurrentHashMap<>();

    @Override
    public PageUtils<MemberDto> searchMembers(MemberQueryForm form, PageWrapper page) {
        Map<String, Object> queryMap = getMemberQueryMap(form);
        List<MemberDto> members = baseMapper.searchMembers(queryMap, page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }
        int total = baseMapper.count(queryMap);
        setMemberTags(members);
        setMemberDistance(form, members);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }

    //设置用户技能标签
    private void setMemberTags(List<MemberDto> members) {
        for (MemberDto member : members) {
            List<String> tags = baseMapper.getMemberTags(member.getId());
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
    private void setMemberDistance(MemberQueryForm form, List<MemberDto> members) {
        for (MemberDto member : members) {
            long distance = 0L;
            if (form.getLatitude() != null && form.getLongitude() != null
                    && member.getLat() != null && member.getLng() != null) {
                distance = GeoUtils.getDistance(form.getLatitude(), form.getLongitude(), member.getLat(), member.getLng());
            }
            member.setDistance(distance);
        }
    }

    @Override
    public MemberDto getMember(Long curMemberId, Long memberId) {
        Member member = baseMapper.selectById(memberId);
        if (member != null) {
            MemberDto dto = new MemberDto();
            BeanUtils.copyProperties(member, dto);
            //是否关注
            boolean isFollowed = this.isFollowed(curMemberId, memberId);
            dto.setFollowed(isFollowed);
            return dto;
        }
        return null;

    }

    @Override
    public MemberDto getMember(Long memberId) {
        Member member = baseMapper.selectById(memberId);
        if (member != null) {
            MemberDto dto = new MemberDto();
            BeanUtils.copyProperties(member, dto);
            return dto;
        }
        return null;
    }

    @Override
    public void updateMember(MemberForm form) {
        ValidatorUtils.validateEntity(form);
        Member member = selectById(form.getId());
        if (member != null) {
            member.setNickName(form.getNickName());
            member.setAvatar(form.getAvatar());
            member.setSex(form.getSex());
            member.setSelfIntro(form.getSelfIntro());
            this.updateById(member);
        }
    }

    @Override
    public void wxUpdateMember(WxUserInfoForm userInfo) {
        redisUtils.set(RedisKeys.WX_PHONE + userInfo.getPhone(), userInfo, 70);//70s过期
        /*
        Member member = selectById(memberId);
        if (member != null) {
            if (StringUtils.isEmpty(member.getNickName())) {
                member.setNickName(userInfo.getNickName());
            }
            if (StringUtils.isEmpty(member.getAvatar())) {
                member.setAvatar(userInfo.getAvatarUrl());
            }
            if (member.getSex() == null) {
                member.setSex(userInfo.getGender());
            }
            this.updateById(member);
        }*/
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
    public void updateLocationNumber(Long memberId,LocationForm locationForm) {
        baseMapper.updateLocationNumber(memberId,locationForm);
    }

    @Override
    public void followMember(Long fromMemberId, Long toMemberId) {
        MemberFollowEntity follow = new MemberFollowEntity(DateUtils.now(), fromMemberId, toMemberId);
        memberFollowDao.insert(follow);
        redisUtils.addList("follow:" + toMemberId, fromMemberId);
        redisUtils.addList("follow-currentUser:" + fromMemberId, toMemberId);
    }

    @Override
    public void unfollowMember(Long fromMemberId, Long toMemberId) {
        Wrapper<MemberFollowEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("from_member_id", fromMemberId)
                .eq("to_member_id", toMemberId);
        memberFollowDao.delete(wrapper);
        redisUtils.delListKey("follow-currentUser:" + fromMemberId, toMemberId);
        redisUtils.delListKey("follow:" + toMemberId, fromMemberId);
    }

    @Override
    public PageUtils<MemberDto> getFollowMembers(Long fromMemberId, PageWrapper page) {
        List<MemberDto> members = memberFollowDao.getFollowMembers(fromMemberId, page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }
        int total = memberFollowDao.followCount(fromMemberId);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public PageUtils<MemberDto> getFansMembers(Long toMemberId, PageWrapper page) {
        List<MemberDto> members = memberFollowDao.getFansMembers(toMemberId, page);
        if (CollectionUtils.isEmpty(members)) {
            return new PageUtils<>();
        }
        int total = memberFollowDao.fansCount(toMemberId);
        return new PageUtils<>(members, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public boolean isFollowed(Long fromMemberId, Long toMemberId) {
        int count = memberFollowDao.isFollowed(fromMemberId, toMemberId);
        return count > 0;
    }


    @Override
    @Transactional
    public void score(Long judgerId, MemberScoreForm form) {
        ValidatorUtils.validateEntity(form);
        sendFlowers(judgerId, form);
        MemberScoreEntity score = new MemberScoreEntity();
        BeanUtils.copyProperties(form, score);
        score.setJudgerId(judgerId);
        score.setCreateTime(DateUtils.now());
        memberScoreDao.insert(score);

        //TODO 发送消息给被评分人
    }

    private void sendFlowers(Long judgerId, MemberScoreForm form) {
        Integer giveFlowerCount = form.getFlowerCount();
        if (giveFlowerCount == null) {
            return;
        }
        Member judger = selectById(judgerId);
        Integer judgerFlowerCount = judger.getFlowerCount();
        if (judgerFlowerCount < giveFlowerCount) {
            throw new RRException("鲜花数不足", 100);
        }
        baseMapper.incFlowerCount(judgerId, -giveFlowerCount);
        baseMapper.incFlowerCount(form.getMemberId(), giveFlowerCount);
    }

    @Override
    public void sendPhoneCode(String phoneNum) throws Exception {
        //随机生成4位验证码
        String code = String.valueOf((new Random().nextInt(8999) + 1000));
        String[] params = {code};
        SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
        SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNum,
                templateId, params, signName, "", "");
        logger.info("腾讯短信接口返回结果：" + JsonUtil.Java2Json(result));
        redisUtils.set(RedisKeys.PHONE_CODE_KEY + phoneNum, code, 100);//100s过期
//        phoneCodeMap.put(phoneNum, code);
    }

    @Override
    public boolean validatePhoneCode(String phoneNum, String code) {
//        logger.info("phoneCodeMap的内容为：" + JsonUtil.Java2Json(phoneCodeMap));
        String originCode = redisUtils.get(RedisKeys.PHONE_CODE_KEY + phoneNum);
        return StringUtils.equals(code, originCode);

    }

    /**
     * 签到
     *
     * @param memberId
     */
    @Override
    @Transactional
    public Map<String,Object> checkIn(Long memberId, Integer experience) {
        Map<String,Object> result = new HashMap<>();
        String checkedIn = redisUtils.get(RedisKeys.CHECK_IN + memberId);
        if (StringUtils.isEmpty(checkedIn) || !Boolean.valueOf(checkedIn)) {
            redisUtils.set(RedisKeys.CHECK_IN + memberId, true, DateUtils.secondsLeftToday());
            MemberCheckInEntity checkIn = new MemberCheckInEntity();
            checkIn.setMemberId(memberId);
            checkIn.setExperience(experience);
            memberCheckInDao.insert(checkIn);
            incMemberExperience(memberId, experience);

            result.put("checkInStatus",0);
            result.put("msg","checkIn success");
        }else {
            result.put("checkInStatus",1);
            result.put("msg","checkedIn");
        }
        return result;
    }

    //增加用户经验值
    private void incMemberExperience(Long memberId, Integer experience) {
        baseMapper.incMemberExperience(memberId, experience);
    }
}
