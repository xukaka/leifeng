package io.renren.modules.app.controller.setting;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.RabbitMqHelper;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.entity.setting.Member;
import io.renren.modules.app.form.LocationForm;
import io.renren.modules.app.form.MemberForm;
import io.renren.modules.app.form.MemberScoreForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    private RabbitMqHelper rabbitMqHelper;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/list")
    @ApiOperation("搜索用户列表-分页")
    public R searchMembers(@RequestParam String keyword, @RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<Member> members = memberService.searchMembers(keyword, page);
        return R.ok().put("result", members);
    }

    @Login
    @GetMapping("/detail")
    @ApiOperation("获取用户信息")
    public R getMember(@RequestParam("memberId") Long memberId) {
        Member member = memberService.getMember(memberId);
        return R.ok().put("result", member);
    }

    @Login
    @PutMapping("/update")
    @ApiOperation("更新用户信息")
    public R updateMember(@RequestBody MemberForm form) {
        memberService.updateMember(form);
        return R.ok();
    }

    @Login
    @PutMapping("/location")
    @ApiOperation("更新用户位置")
    public R updateLocation(LocationForm locationForm) {
        memberService.updateLocationNumber(locationForm);
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
//        rabbitMqHelper.sendMessage(RabbitMQConfig.QUEUE_NAME,"mq test 222");
        return R.ok().put("result", value);
    }

    @Login
    @PostMapping("/follow")
    @ApiOperation("关注用户")
    public R followMember(@RequestParam Long toMemberId) {
        memberService.followMember(ReqUtils.currentUserId(), toMemberId);
        return R.ok();
    }

    @Login
    @PostMapping("/unfollow")
    @ApiOperation("取消关注")
    public R unfollowMember(@RequestParam Long toMemberId) {
        memberService.unfollowMember(ReqUtils.currentUserId(), toMemberId);
        return R.ok();
    }

    @Login
    @PostMapping("/follow/list")
    @ApiOperation("分页获取关注的用户列表")
    public R getFollowMembers(@RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<Member> members = memberService.getFollowMembers(ReqUtils.currentUserId(), page);
        return R.ok().put("result", members);
    }


    @Login
    @PostMapping("/fans/list")
    @ApiOperation("分页获取粉丝用户列表")
    public R getFansMembers(@RequestParam Integer curPage, @RequestParam Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<Member> members = memberService.getFansMembers(ReqUtils.currentUserId(), page);
        return R.ok().put("result", members);
    }


    @Login
    @PostMapping("/score")
    @ApiOperation("用户评分")
    public R socre(@RequestBody MemberScoreForm form) {
        memberService.score(ReqUtils.currentUserId(), form);
        return R.ok();
    }

}
