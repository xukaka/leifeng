package io.renren.modules.app.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import io.renren.common.exception.RRException;
import io.renren.common.utils.*;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.dao.member.*;
import io.renren.modules.app.dto.*;
import io.renren.modules.app.entity.member.*;
import io.renren.modules.app.entity.pay.MemberWalletEntity;
import io.renren.modules.app.entity.task.TaskReceiveEntity;
import io.renren.modules.app.form.*;
import io.renren.modules.app.service.ImService;
import io.renren.modules.app.service.MemberAuthsService;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.service.MemberWalletService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {
    private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    @Resource
    private MemberAuthsService memberAuthsService;
    @Resource
    private ImService imService;
    @Resource
    private MemberFollowDao memberFollowDao;
    @Resource
    private MemberScoreDao memberScoreDao;
    @Resource
    private MemberCheckInDao memberCheckInDao;

    @Resource
    private InviteFriendsDao inviteFriendsDao;
    @Resource
    private MemberWalletService memberWalletService;

    @Resource
    private MemberFeedbackDao memberFeedbackDao;
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
            /*List<Map<String, Object>> followNotice = redisUtils.rangeByScore("followNotice:" + memberId, ImFollowNoticeStatus.class);
            if (!followNotice.isEmpty()) {
                MessageTypeForm messageTypeForm = new MessageTypeForm();
                messageTypeForm.setStatus(0);
                messageTypeForm.setType(1);
                messageTypeForm.setToId(member.getId());
                imService.setMessageType(messageTypeForm);
            }
            List<Map<String, Object>> taskNotice = redisUtils.rangeByScore("task:" + memberId, ImFollowNoticeStatus.class);
            if (!taskNotice.isEmpty()) {
                MessageTypeForm messageTypeForm = new MessageTypeForm();
                messageTypeForm.setStatus(0);
                messageTypeForm.setType(2);
                messageTypeForm.setToId(member.getId());
                imService.setMessageType(messageTypeForm);
            }*/
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
        Wrapper<Member> wrapper = new EntityWrapper<Member>().eq("id", form.getId());
        Member member = new Member();
        member.setNickName(form.getNickName());
        member.setAvatar(form.getAvatar());
        member.setSex(form.getSex());
        member.setSelfIntro(form.getSelfIntro());
        this.update(member,wrapper);
    }

    @Override
    public void wxUpdateMember(WxUserInfoForm userInfo) {
        Wrapper<Member> wrapper = new EntityWrapper<Member>().eq("id", userInfo.getMemberId());
        Member member = new Member();
        member.setAvatar(userInfo.getAvatarUrl());
        member.setNickName(userInfo.getNickName());
        member.setSex(userInfo.getGender());
        this.update(member,wrapper);
    }

    @Override
    @Transactional
    public void registerMemberWithAuth(Member member, MemberWalletEntity wallet, MemberAuths auths) {
        //插入用户基本信息
        this.insert(member);

        wallet.setMemberId(member.getId());
        memberWalletService.insert(wallet);
        auths.setMemberId(member.getId());
        memberAuthsService.insert(auths);
    }

    @Override
    public void updateLocationNumber(Long memberId, LocationForm locationForm) {
        baseMapper.updateLocationNumber(memberId, locationForm);
    }

    @Override
    public void followMember(Long fromMemberId, Long toMemberId) {
        MemberFollowEntity follow = new MemberFollowEntity(DateUtils.now(), fromMemberId, toMemberId);
        memberFollowDao.insert(follow);
//        redisUtils.addList("follow:" + toMemberId, fromMemberId);
//        redisUtils.addList("follow-currentUser:" + fromMemberId, toMemberId);
    }

    @Override
    public void unfollowMember(Long fromMemberId, Long toMemberId) {
        Wrapper<MemberFollowEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("from_member_id", fromMemberId)
                .eq("to_member_id", toMemberId);
        memberFollowDao.delete(wrapper);
//        redisUtils.delListKey("follow-currentUser:" + fromMemberId, toMemberId);
//        redisUtils.delListKey("follow:" + toMemberId, fromMemberId);
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
    public boolean isScored(Long taskId) {
        Wrapper<MemberScoreEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("task_id", taskId);
        int count = memberScoreDao.selectCount(wrapper);
        return count > 0;
    }

    @Override
    @Transactional
    public void score(Long judgerId, MemberScoreForm form) {
        ValidatorUtils.validateEntity(form);
        MemberScoreEntity score = new MemberScoreEntity();
        BeanUtils.copyProperties(form, score);
        score.setJudgerId(judgerId);
        score.setCreateTime(DateUtils.now());
        memberScoreDao.insert(score);
        //TODO 发送消息给被评分人
    }

    @Override
    public MemberScoreDto getScore(Long taskId) {
        return memberScoreDao.getScore(taskId);
    }

    @Override
    public PageUtils<MemberScoreDto> getMemberScores(Long memberId, PageWrapper page) {
        List<MemberScoreDto> scores = memberScoreDao.getMemberScores(memberId, page);
        if (CollectionUtils.isEmpty(scores)) {
            return new PageUtils<>();
        }
        int total = memberScoreDao.count(memberId);
        return new PageUtils<>(scores, total, page.getPageSize(), page.getCurrPage());
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
        redisUtils.set(RedisKeys.PHONE_CODE_KEY + phoneNum, code, 5 * 60);//5分钟过期
    }

    @Override
    public boolean validatePhoneCode(String phoneNum, String code) {
        String originCode = redisUtils.get(RedisKeys.PHONE_CODE_KEY + phoneNum);
        return StringUtils.equals(code, originCode);

    }

    /**
     * 签到
     */
    @Override
    @Transactional
    public Map<String, Object> checkIn(Long memberId, Integer experience) {
        Map<String, Object> result = new HashMap<>();
        String checkedIn = redisUtils.get(RedisKeys.CHECK_IN + memberId);
        if (StringUtils.isEmpty(checkedIn) || !Boolean.valueOf(checkedIn)) {
            redisUtils.set(RedisKeys.CHECK_IN + memberId, true, DateUtils.secondsLeftToday());
            MemberCheckInEntity checkIn = new MemberCheckInEntity(DateUtils.now(), memberId, experience);
            memberCheckInDao.insert(checkIn);
            ThreadPoolUtils.execute(() -> {
                //增加用户的经验值
                incMemberExperienceAndVirtualCurrency(memberId, experience, 0);
            });

            result.put("checkInStatus", 0);
            result.put("experience", experience);
            result.put("msg", "签到成功");
        } else {
            result.put("checkInStatus", 1);
            result.put("experience", 0);
            result.put("msg", "已签到");
        }
        return result;
    }


    //增加用户经验值和虚拟币
    @Override
    public void incMemberExperienceAndVirtualCurrency(Long memberId, Integer experience, Integer virtualCurrency) {
        baseMapper.incMemberExperienceAndVirtualCurrency(memberId, experience, virtualCurrency);
    }

    @Override
    public void incTaskCompleteCount(Long memberId, Integer inc) {
        baseMapper.incTaskCompleteCount(memberId, inc);
    }

    @Override
    public ScoreBoardDto getScoreBoard(Long memberId) {
        ScoreBoardDto board = memberScoreDao.getScoreBoard(memberId);
        if (board.getScoreTotalNum() == 0) {
            return board;
        }
        //计算综合评分
        double scoreAverage = (board.getFiveStarNum() * 5.0
                + board.getFourStarNum() * 4.0
                + board.getThreeStarNum() * 3.0
                + board.getTwoStarNum() * 2.0
                + board.getOneStarNum() * 1.0) / board.getScoreTotalNum();

        //保留小数点后一位
        double scoreFormat = new BigDecimal(scoreAverage).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        board.setScoreAverage(scoreFormat);
        return board;
    }

    @Override
    public void addInviteFriends(Long inviteMemberId, Long friendMemberId, Integer experience, Integer virtualCurrency) {
        InviteFriendsEntity invite = new InviteFriendsEntity();
        invite.setInviteMemberId(inviteMemberId);
        invite.setFriendMemberId(friendMemberId);
        invite.setExperience(experience);
        invite.setVirtualCurrency(virtualCurrency);
        invite.setCreateTime(DateUtils.now());
        inviteFriendsDao.insert(invite);
    }

    @Override
    public PageUtils<InviteFriendsDto> getInviteFriends(Long inviteMemberId, PageWrapper page) {
        List<InviteFriendsDto> inviteFriends = inviteFriendsDao.getInviteFriends(inviteMemberId, page);
        if (CollectionUtils.isEmpty(inviteFriends)) {
            return new PageUtils<>();
        }
        int total = inviteFriendsDao.count(inviteMemberId);
        return new PageUtils<>(inviteFriends, total, page.getPageSize(), page.getCurrPage());
    }

    @Override
    public ExperienceAndVirtualCurrencyDto getTotalExperienceAndVirtualCurrency(Long inviteMemberId) {
        ExperienceAndVirtualCurrencyDto dto = inviteFriendsDao.getTotalExperienceAndVirtualCurrency(inviteMemberId);
        if (dto == null) {
            dto = new ExperienceAndVirtualCurrencyDto();
        }
        return dto;
    }

    @Override
    public List<SkillRadarChartDto> getSkillRadarChart(Long memberId) {
        List<SkillRadarChartDto> chart = baseMapper.getSkillRadarChart(memberId);
        if (CollectionUtils.isEmpty(chart)) {
            return new ArrayList<>();
        }
        return chart;
    }


    @Override
    public void saveFeedback(Long memberId, String content) {
        MemberFeedback feedback = new MemberFeedback();
        feedback.setContent(content);
        feedback.setMemberId(memberId);
        feedback.setCreateTime(DateUtils.now());
        memberFeedbackDao.insert(feedback);
    }


}
