package io.renren.modules.app.controller.im;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.entity.im.ImFollowNoticeStatus;
import io.renren.modules.app.entity.im.ImGroupNotice;
import io.renren.modules.app.entity.im.ImHistoryMember;
import io.renren.modules.app.entity.im.ImTaskStatusNotice;
import io.renren.modules.app.form.MessageTypeForm;
import io.renren.modules.app.form.PageWrapper;
import io.renren.modules.app.service.ImService;
import io.renren.modules.app.service.MemberService;
import io.renren.modules.app.utils.ReqUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther: Easy
 * @date: 19-4-20 14:03
 * @description:
 */
@RestController
@RequestMapping("/app/im")
@Api(tags = "IM")
public class ImController {
    private final static Logger logger = LoggerFactory.getLogger(ImController.class);

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ImService imService;


    @GetMapping("/historyMember")
    @ApiOperation("获取联系人历史列表")
    public R getHistoryMember(Long memberId, int type) {
        logger.info("[ImController.info] 请求参数id={}", memberId);
        if (type == 0) {
            List<ImHistoryMember> members = new ArrayList<>();
            List<Map<String,Object>> list = redisUtils.rangeByScore("unread:" + memberId, ImHistoryMember.class);
            System.out.println(list.isEmpty());
            if(!list.isEmpty()){
            for (Map<String,Object> map : list) {
                ImHistoryMember imHistoryMember = new ImHistoryMember();
                Double score = (Double) map.get("score");
                imHistoryMember.setStatus(score.intValue());
                if(map.get("value")!=null){
                    imHistoryMember.setMember(memberService.getMember(Long.parseLong(map.get("value").toString())));
                }
                members.add(imHistoryMember);
            }
            return R.ok().put("result", members);
            }else{
                return R.ok().put("result", "没有未读联系人");

            }
        } else if (type == 1) {
            List<ImFollowNoticeStatus> followStatus = new ArrayList<>();
            List<Map<String,Object>> list = redisUtils.rangeByScore("followNotice:" + memberId, ImFollowNoticeStatus.class);


            for (Map<String,Object> map : list) {
                ImFollowNoticeStatus imFollowNoticeStatus = new ImFollowNoticeStatus();
                Double score = (Double) map.get("score");
                imFollowNoticeStatus.setStatus(score.intValue());
                followStatus.add(imFollowNoticeStatus);
            }
            return R.ok().put("result", followStatus);
        } else if (type == 2) {
            List<ImFollowNoticeStatus> followStatus = new ArrayList<>();
            List<Map<String,Object>> list = redisUtils.rangeByScore("task:" + memberId, ImFollowNoticeStatus.class);

            for (Map<String,Object> map : list) {
                ImFollowNoticeStatus imFollowNoticeStatus = new ImFollowNoticeStatus();
                Double score = (Double) map.get("score");
                imFollowNoticeStatus.setStatus(score.intValue());
                followStatus.add(imFollowNoticeStatus);
            }
            return R.ok().put("result", followStatus);
        } else {
            return R.ok().put("result", "空的，出现了异常");

        }
    }

    @PostMapping(value = "/setMessageType")
    @ApiOperation("设置未读消息状态")
    /**
     * memberId:代表发送人，当前用户ID即可
     * toId：代表接受人，也就是不在线的用户Id
     * type:0代表私聊，1代表关注，2代表任务通知
     * status:0代表没有未读，1代表有未读
     */
    public R setMessageType(@RequestBody MessageTypeForm messageTypeForm) {
        logger.info("[ImController.info] 请求参数id={}", messageTypeForm.getFromId(), messageTypeForm.getToId());
        imService.setMessageType(messageTypeForm);
        return R.ok();
    }

    @Login
    @GetMapping(value = "/groupNotice/list")
    @ApiOperation("分页获取群组通知列表")
    public R getGroupNotices( Integer curPage,  Integer pageSize) {
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<ImGroupNotice> notices = imService.getGroupNotices(ReqUtils.curMemberId(), page);
        return R.ok().put("result", notices);
    }

}
