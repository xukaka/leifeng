package io.renren.modules.app.controller.setting;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.*;
import io.renren.config.RabbitMQConfig;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.*;
import io.renren.modules.app.entity.member.Member;
import io.renren.modules.app.entity.member.MemberFeedback;
import io.renren.modules.app.form.*;
import io.renren.modules.app.service.MemberFeedbackService;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APP测试接口
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:47
 */
@RestController
@RequestMapping("/app/member")
@Api(tags = "用户接口")
public class MemberController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberFeedbackService memberFeedbackService;

    @Resource
    private RabbitMqHelper rabbitMqHelper;
    @Autowired
    private RedisUtils redisUtils;


    @PostMapping("/list")
    @ApiOperation("搜索用户列表-分页")
    public R searchMembers(@RequestBody MemberQueryForm form) {
        PageWrapper page = PageWrapperUtils.getPage(form.getCurPage(), form.getPageSize());
        PageUtils<MemberDto> members = memberService.searchMembers(form, page);
        return R.ok().put("result", members);
    }


    @Login
    @GetMapping("/detail")
    @ApiOperation("获取用户信息")
    public R getMember(Long memberId) {
        MemberDto member = memberService.getMember(ReqUtils.curMemberId(), memberId);
        return R.ok().put("result", member);
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("更新用户信息")
    public R updateMember(@RequestBody MemberForm form) {
        memberService.updateMember(form);
        return R.ok();
    }


    @PostMapping("/wxUpdate")
    @ApiOperation("微信更新用户信息(昵称，头像，性别)")
    public R wxUpdateMember(@RequestBody WxUserInfoForm userInfo) {
        memberService.wxUpdateMember(userInfo);
        return R.ok();
    }

    @Login
    @PostMapping("/location")
    @ApiOperation("更新用户位置")
    public R updateLocation(@RequestBody LocationForm locationForm) {
        memberService.updateLocationNumber(ReqUtils.curMemberId(), locationForm);
        return R.ok();
    }


    @GetMapping("/test/redis")
    @ApiOperation("redis连接测试")
    public R redis() {
        redisUtils.set("abc", "123321 redis connected.");
        String value = redisUtils.get("abc");
        return R.ok().put("result", value);
    }

    @GetMapping("/test/rabbitmq")
    @ApiOperation("rabbitmq消息发送-接收测试")
    public R rabbitmq() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "test get a rabbitmq messge");
        rabbitMqHelper.sendMessage(RabbitMQConfig.QUEUE_NAME, jsonObject.toJSONString());
        return R.ok().put("result", "rabbit send a babbitmq message.");
    }

    @Login
    @GetMapping("/follow")
    @ApiOperation("关注用户")
    public R followMember(Long toMemberId) {
        memberService.followMember(ReqUtils.curMemberId(), toMemberId);
        return R.ok();
    }

    @Login
    @GetMapping("/unfollow")
    @ApiOperation("取消关注")
    public R unfollowMember(Long toMemberId) {
        memberService.unfollowMember(ReqUtils.curMemberId(), toMemberId);
        return R.ok();
    }

    @Login
    @GetMapping("/follow/list")
    @ApiOperation("分页获取关注的用户列表")
    public R getFollowMembers(Integer curPage, Integer pageSize) {
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
        PageUtils<MemberDto> members = memberService.getFollowMembers(ReqUtils.curMemberId(), page);
        return R.ok().put("result", members);
    }


    @Login
    @GetMapping("/fans/list")
    @ApiOperation("分页获取粉丝用户列表")
    public R getFansMembers(Integer curPage, Integer pageSize) {
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
        PageUtils<MemberDto> members = memberService.getFansMembers(ReqUtils.curMemberId(), page);
        return R.ok().put("result", members);
    }


    @Login
    @PostMapping("/score")
    @ApiOperation("用户评分")
    public R socre(@RequestBody MemberScoreForm form) {
        memberService.score(ReqUtils.curMemberId(), form);
        return R.ok();
    }

    @GetMapping("/score/list")
    @ApiOperation("分页获取用户评分列表")
    public R getMemberScores(Long memberId, Integer curPage, Integer pageSize) {
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
        PageUtils<MemberScoreDto> scores = memberService.getMemberScores(memberId, page);
        return R.ok().put("result", scores);
    }

    @Login
    @PostMapping("/feedback/save")
    @ApiOperation("保存用户反馈")
    public R saveFeedback(String content) {
        memberService.saveFeedback(ReqUtils.curMemberId(), content);
        return R.ok();

    }

    @GetMapping("/skillRadarChart")
    @ApiOperation("用户技能雷达图")
    public R skillRadarChart(Long memberId) {
        List<SkillRadarChartDto> chart = memberService.getSkillRadarChart(memberId);
        return R.ok().put("result", chart);

    }

    @GetMapping("/scoreBoard")
    @ApiOperation("用户评分面板")
    public R getScoreBoard(Long memberId) {
        ScoreBoardDto board = memberService.getScoreBoard(memberId);
        return R.ok().put("result", board);

    }


    @Login
    @GetMapping("/getInviteFriends")
    @ApiOperation("分页获取邀请好友列表")
    public R getInviteFriends(Integer curPage, Integer pageSize) {
        PageWrapper page = PageWrapperUtils.getPage(curPage, pageSize);
        PageUtils<InviteFriendsDto> inviteFriends = memberService.getInviteFriends(ReqUtils.curMemberId(), page);
        return R.ok().put("result", inviteFriends);
    }


    @Login
    @GetMapping("/getTotalExperienceAndVirtualCurrency")
    @ApiOperation("统计获取的经验值和虚拟币")
    public R getTotalExperienceAndVirtualCurrency() {
        ExperienceAndVirtualCurrencyDto dto = memberService.getTotalExperienceAndVirtualCurrency(ReqUtils.curMemberId());
        return R.ok().put("result", dto);
    }


}
