package io.renren.modules.app.controller.setting;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.RabbitMqHelper;
import io.renren.common.utils.RedisUtils;
import io.renren.config.RabbitMQConfig;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.dto.TaskDto;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.entity.setting.MemberFeedback;
import io.renren.modules.app.entity.setting.MemberFriend;
import io.renren.modules.app.entity.story.PublishMessageEntity;
import io.renren.modules.app.form.*;
import io.renren.modules.app.service.MemberFeedbackService;
import io.renren.modules.app.service.MemberFriendService;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.ContentTooLongException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
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
@Api(tags = "用户信息接口")
public class MemberController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberFeedbackService memberFeedbackService;
    @Autowired
    private MemberFriendService memberFriendService;

    @Resource
    private RabbitMqHelper rabbitMqHelper;
    @Autowired
    private RedisUtils redisUtils;




    @PostMapping("/list")
    @ApiOperation("搜索用户列表-分页")
    public R searchMembers(@RequestBody MemberQueryForm form) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", form.getCurPage());
        pageMap.put("size", form.getPageSize());
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<MemberDto> members = memberService.searchMembers(form, page);
        return R.ok().put("result", members);
    }



    @Login
    @GetMapping("/detail")
    @ApiOperation("获取用户信息")
    public R getMember(@RequestParam("memberId") Long memberId) {
        MemberDto member = memberService.getMember(ReqUtils.currentUserId(),memberId);
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
        memberService.updateLocationNumber(ReqUtils.currentUserId(),locationForm);
        return R.ok();
    }

    @Login
    @GetMapping("/userId")
    @ApiOperation("获取用户memberID")
    public R userInfo(@RequestAttribute("userId") Integer userId) {
        return R.ok().put("result", userId);
    }

    @GetMapping("notToken")
    @ApiOperation("忽略Token验证测试")
    public R notToken() {

        return R.ok().put("result", "无需token也能访问。。。");
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
        jsonObject.put("msg","test get a rabbitmq messge");
        rabbitMqHelper.sendMessage(RabbitMQConfig.QUEUE_NAME, jsonObject.toJSONString());
        return R.ok().put("result", "rabbit send a babbitmq message.");
    }

    @Login
    @GetMapping("/follow")
    @ApiOperation("关注用户")
    public R followMember(@RequestParam Long toMemberId) {
        memberService.followMember(ReqUtils.currentUserId(), toMemberId);
        return R.ok();
    }

    @Login
    @GetMapping("/unfollow")
    @ApiOperation("取消关注")
    public R unfollowMember(@RequestParam Long toMemberId) {
        memberService.unfollowMember(ReqUtils.currentUserId(), toMemberId);
        return R.ok();
    }

    @Login
    @GetMapping("/follow/list")
    @ApiOperation("分页获取关注的用户列表")
    public R getFollowMembers(@RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<MemberDto> members = memberService.getFollowMembers(ReqUtils.currentUserId(), page);
        return R.ok().put("result", members);
    }


    @Login
    @GetMapping("/fans/list")
    @ApiOperation("分页获取粉丝用户列表")
    public R getFansMembers(@RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<MemberDto> members = memberService.getFansMembers(ReqUtils.currentUserId(), page);
        return R.ok().put("result", members);
    }


    @Login
    @PostMapping("/score")
    @ApiOperation("用户评分")
    public R socre(@RequestBody MemberScoreForm form) {
        memberService.score(ReqUtils.currentUserId(), form);
        return R.ok();
    }

    @GetMapping("/avatar/phone")
    @ApiOperation("根据手机号获取用户头像")
    public R getAvatarByPhone(@RequestParam String phone){
        if(StringUtils.isEmpty(phone)){
            return R.error(HttpStatus.SC_BAD_REQUEST,"手机号不为空");
        }
        String avatar = "";
        Member member = memberService.selectOne(new EntityWrapper<Member>().eq("mobile", phone));
        if(!ObjectUtils.isEmpty(member))
            avatar = member.getAvatar();

        return R.ok().put("result",avatar);
    }

    @Login
    @PostMapping("/feedback/save")
    @ApiOperation("用户反馈保存")
    public R feedbackSave(Long memberId , String content){
        if(memberId == null || memberId <=0){
            return R.error(HttpStatus.SC_BAD_REQUEST,"memberId不为空");
        }
        if(StringUtils.isEmpty(content)){
            return R.error(HttpStatus.SC_BAD_REQUEST,"content不为空");
        }
        MemberFeedback feedback = new MemberFeedback();
        feedback.setContent(content);
        feedback.setMemberId(memberId);
        feedback.setCreateTime(System.currentTimeMillis());
        memberFeedbackService.insert(feedback);
        return R.ok();
    }

    @PostMapping("/feedback/list")
    @ApiOperation("用户反馈列表")
    public R feedbackList(@RequestParam Map<String, Object> params){
        PageWrapper page = new PageWrapper(params);
        List<PublishMessageEntity> list = memberFeedbackService.getPage(page);
        return R.ok().put("result", list)
                .put("page",page.getCurrPage())
                .put("size",page.getPageSize());
    }

    @PostMapping("/friend/save")
    @ApiOperation("添加好友")
    public R friendSave(Long memberId, Long friendId){
        if(memberId == null && memberId.longValue()<=0){
            return R.error(HttpStatus.SC_BAD_REQUEST,"memberId不为空");
        }
        if(friendId == null && friendId.longValue()<=0){
            return R.error(HttpStatus.SC_BAD_REQUEST,"friendId不为空");
        }

        MemberFriend memberFriend = new MemberFriend();
        memberFriend.setMemberId(memberId);
        memberFriend.setFriendId(friendId);
        memberFriend.setCreateTime(System.currentTimeMillis());
        memberFriendService.insert(memberFriend);
        return R.ok();
    }
}
