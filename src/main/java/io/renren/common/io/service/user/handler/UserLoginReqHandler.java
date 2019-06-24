package io.renren.common.io.service.user.handler;

import io.renren.common.io.body.UserBody;
import io.renren.common.io.service.user.processor.UserLoginServer;
import io.renren.common.io.service.user.resp.UserRespBody;
import org.apache.commons.collections4.CollectionUtils;
import org.jim.common.*;
import org.jim.common.message.MessageHelper;
import org.jim.common.packets.*;
import org.jim.common.protocol.IProtocol;
import org.jim.common.utils.ImKit;
import org.jim.common.utils.JsonKit;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.command.CommandManager;
import org.jim.server.command.handler.JoinGroupReqHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;

import java.util.List;

/**
 * @auther: Easy
 * @date: 19-4-18 13:31
 * @description:
 */
public class UserLoginReqHandler extends AbstractCmdHandler {
    private static Logger log = LoggerFactory.getLogger(UserLoginReqHandler.class);

    public UserLoginReqHandler() {
    }

    @Override
    public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception {
        if (packet.getBody() == null) {
            Aio.remove(channelContext, "body is null");
            return null;
        }
        List<UserLoginServer> userCmdProcessors = this.getProcessor(channelContext, UserLoginServer.class);
        if (CollectionUtils.isEmpty(userCmdProcessors)) {
            log.info("登录失败,没有登录命令业务处理器!");
            Aio.remove(channelContext, "no login serviceHandler processor!");
            return null;
        }
        UserLoginServer userLoginServer = userCmdProcessors.get(0);
        ImSessionContext imSessionContext = (ImSessionContext) channelContext.getAttribute();
        UserBody userBody = JsonKit.toBean(packet.getBody(), UserBody.class);

        UserRespBody userRespBody = userLoginServer.access(userBody, channelContext);
        if (userRespBody == null || userRespBody.getUser() == null) {
            log.info("登录失败, loginName:{}, password:{}", userBody.getMemberId());
            if (userRespBody == null) {
                userRespBody = new UserRespBody(Command.COMMAND_LOGIN_RESP, ImStatus.C10008);
            }
            userRespBody.clear();
            ImPacket loginRespPacket = new ImPacket(Command.COMMAND_LOGIN_RESP, userRespBody.toByte());
            ImAio.bSend(channelContext, loginRespPacket);
            ImAio.remove(channelContext, "loginName and token is incorrect");
            return null;
        }
        User user = userRespBody.getUser();
        String userId = user.getId();
        IProtocol protocol = ImKit.protocol(null, channelContext);
        String terminal = protocol == null ? "" : protocol.name();
        user.setTerminal(terminal);
        imSessionContext.getClient().setUser(user);
        ImAio.bindUser(channelContext, userId, imConfig.getMessageHelper().getBindListener());
        //初始化绑定或者解绑群组;
        bindUnbindGroup(channelContext, user);
        userLoginServer.onSuccess(channelContext);
        userRespBody.clear();
        ImPacket loginRespPacket = new ImPacket(Command.COMMAND_LOGIN_RESP, userRespBody.toByte());
        return loginRespPacket;
    }

    /**
     * 初始化绑定或者解绑群组;
     */
    public void bindUnbindGroup(ChannelContext channelContext, User user) throws Exception {
        String userId = user.getId();
        List<Group> groups = user.getGroups();
        if (groups != null) {
            boolean isStore = ImConst.ON.equals(imConfig.getIsStore());
            MessageHelper messageHelper = null;
            List<String> groupIds = null;
            if (isStore) {
                messageHelper = imConfig.getMessageHelper();
                groupIds = messageHelper.getGroups(userId);
            }
            //绑定群组
            for (Group group : groups) {
                if (isStore && groupIds != null) {
                    groupIds.remove(group.getGroup_id());
                }
                ImPacket groupPacket = new ImPacket(Command.COMMAND_JOIN_GROUP_REQ, JsonKit.toJsonBytes(group));
                try {
                    JoinGroupReqHandler joinGroupReqHandler = CommandManager.getCommand(Command.COMMAND_JOIN_GROUP_REQ, JoinGroupReqHandler.class);
                    joinGroupReqHandler.bindGroup(groupPacket, channelContext);
                } catch (Exception e) {
                    log.error(e.toString(), e);
                }
            }
            if (isStore && groupIds != null) {
                for (String groupId : groupIds) {
                    messageHelper.getBindListener().onAfterGroupUnbind(channelContext, groupId);
                }
            }
        }
    }

    @Override
    public Command command() {
        return Command.COMMAND_LOGIN_REQ;
    }
}
