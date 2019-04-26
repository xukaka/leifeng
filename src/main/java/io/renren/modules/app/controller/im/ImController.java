package io.renren.modules.app.controller.im;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.entity.im.ImGroupNotice;
import io.renren.modules.app.entity.im.ImHistoryMember;
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
    public R  getHistoryMember(Long memberId){
        logger.info("[ImController.info] 请求参数id={}",memberId);
//        redisUtils.zAdd("unread:"+memberId,11,0);
        List<ImHistoryMember> members = new ArrayList<>();
        List<Map> list =new ArrayList<>();
        list = redisUtils.rangeByScore("unread:"+memberId,ImHistoryMember.class);
        for(Map map :list){
            ImHistoryMember imHistoryMember =new ImHistoryMember();
            Double score = (Double) map.get("score");
            imHistoryMember.setType(score.intValue());
            imHistoryMember.setMember(memberService.getMember((Long) map.get("value")));
            members.add(imHistoryMember);
        }
        return R.ok().put("result", members);
    }
    @PostMapping(value="/setMessageType",consumes = "application/json")
    @ApiOperation("设置未读消息状态")
    /**
     * memberId:代表发送人，当前用户ID即可
     * toId：代表接受人，也就是不在线的用户Id
     * type:0代表没有未读消息，1代表存在未读消息
     */
    public R  setMessageType(@RequestBody MessageTypeForm messageTypeForm){
        logger.info("[ImController.info] 请求参数id={}",messageTypeForm.getFromId(),messageTypeForm.getToId());
        imService.setMessageType(messageTypeForm);
        return R.ok();
    }

    @Login
    @GetMapping(value="/notice/list")
    @ApiOperation("分页获取群组通知列表")
    public R  getGroupNotices(@RequestParam Integer curPage, @RequestParam Integer pageSize){
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("page", curPage);
        pageMap.put("size", pageSize);
        PageWrapper page = new PageWrapper(pageMap);
        PageUtils<ImGroupNotice> notices = imService.getGroupNotices(ReqUtils.currentUserId(), page);
        return R.ok().put("result", notices);
    }
}
