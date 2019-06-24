package io.renren.common.io.service.user.processor.impl;

import io.renren.common.io.SocketServiceUtil;
import io.renren.common.io.body.UserBody;
import io.renren.common.io.service.user.processor.UserLoginServer;
import io.renren.common.io.service.user.resp.UserRespBody;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.app.dto.MemberDto;
import io.renren.modules.app.service.MemberService;
import org.jim.common.ImPacket;
import org.jim.common.ImSessionContext;
import org.jim.common.ImStatus;
import org.jim.common.cache.redis.RedisCache;
import org.jim.common.cache.redis.RedisCacheManager;
import org.jim.common.packets.Command;
import org.jim.common.packets.Group;
import org.jim.common.packets.User;
import org.jim.common.utils.JsonKit;
import org.jim.server.command.CommandManager;
import org.jim.server.command.handler.JoinGroupReqHandler;
import org.jim.server.command.handler.processor.login.LoginCmdProcessor;
import org.jim.server.helper.redis.RedisMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

import java.util.ArrayList;
import java.util.List;

import static org.jim.common.ImConst.GROUP;
import static org.jim.common.ImConst.USER;

/**
 * @auther: Easy
 * @date: 19-4-18 11:11
 * @description:
 */
@Component
public class UserLoginProcessor implements UserLoginServer {
    private RedisMessageHelper redisMessageHelper = new RedisMessageHelper();

    @Autowired
    private MemberService memberService;
    private RedisCache userCache = RedisCacheManager.getCache(USER);

    public User getUser(Long memberId) {
//        RedisUtils redisUtils = SocketServiceUtil.getBean(RedisUtils.class);
        User user = userCache.get(String.valueOf(memberId),User.class);
//        User user = redisUtils.get("user:" + memberId + ":info", User.class);
//        MemberService memberService = SocketServiceUtil.getBean(MemberService.class);
        //demo中用map，生产环境需要用cache
        if (user == null) {
            MemberDto member = memberService.getMember(memberId);
            if (member != null) {
                user = new User();
                user.setId(String.valueOf(member.getId()));
                user.setNick(member.getNickName());
                user.setAvatar(member.getAvatar());
//                user.setGroups(initGroups(user));
            }
        }/* else {
            List<Group> groups = redisMessageHelper.getAllGroupUsers(user.getId(), 2);
            user.setGroups(groups);
        }*/
        return user;


    }

    public List<Group> initGroups(User user) {
        //模拟的群组;正式根据业务去查数据库或者缓存;

        List<Group> groups = new ArrayList<>();
//        RedisUtils redisUtils = SocketServiceUtil.getBean(RedisUtils.class);
//        List<Integer> list = redisUtils.getList("follow-currentUser:" + user.getId());
        groups.add(new Group("100", "雷锋通讯组"));
//        if (list != null) {
//            for (Integer integer : list) {
//                groups.add(new Group(integer.toString(), integer + "通讯组"));
//            }
//        }
        return groups;
    }

    @Override
    public UserRespBody access(UserBody userBody, ChannelContext channelContext) {
        Long memberId = userBody.getMemberId();
        UserRespBody userRespBody;
        User user = getUser(memberId);
        if (user == null) {
            userRespBody = new UserRespBody(Command.COMMAND_LOGIN_RESP, ImStatus.C10008);
        } else {
            userRespBody = new UserRespBody(Command.COMMAND_LOGIN_RESP, ImStatus.C10007, user);
        }
        return userRespBody;
    }

    @Override
    public void onSuccess(ChannelContext channelContext) {
        ImSessionContext imSessionContext = (ImSessionContext) channelContext.getAttribute();
        User user = imSessionContext.getClient().getUser();
        if (user.getGroups() != null) {
            for (Group group : user.getGroups()) {//发送加入群组通知
                ImPacket groupPacket = new ImPacket(Command.COMMAND_JOIN_GROUP_REQ, JsonKit.toJsonBytes(group));
                try {
                    JoinGroupReqHandler joinGroupReqHandler = CommandManager.getCommand(Command.COMMAND_JOIN_GROUP_REQ, JoinGroupReqHandler.class);
                    joinGroupReqHandler.joinGroupNotify(groupPacket, channelContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean isProtocol(ChannelContext channelContext) {
        return true;
    }

    @Override
    public String name() {
        return "default";
    }



}
