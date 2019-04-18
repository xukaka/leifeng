package io.renren.common.io.service.user.handler;

import io.renren.common.io.body.UserBody;
import io.renren.common.io.service.user.processor.UserCmdProcessor;
import io.renren.common.io.service.user.resp.UserRespBody;
import org.apache.commons.collections4.CollectionUtils;
import org.jim.common.ImAio;
import org.jim.common.ImPacket;
import org.jim.common.ImSessionContext;
import org.jim.common.ImStatus;
import org.jim.common.message.MessageHelper;
import org.jim.common.packets.*;
import org.jim.common.protocol.IProtocol;
import org.jim.common.utils.ImKit;
import org.jim.common.utils.JsonKit;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.command.CommandManager;
import org.jim.server.command.handler.JoinGroupReqHandler;
import org.jim.server.command.handler.processor.login.LoginCmdProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

/**
 * @auther: Easy
 * @date: 19-4-18 13:31
 * @description:
 */
public class UserReqHandler  extends AbstractCmdHandler {
    private static Logger log = LoggerFactory.getLogger(UserReqHandler.class);

    public UserReqHandler() {
    }
@Override
    public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception {
        if (packet.getBody() == null) {
            Aio.remove(channelContext, "body is null");
            return null;
        } else {
            List<LoginCmdProcessor> loginProcessors = this.getProcessor(channelContext, LoginCmdProcessor.class);
            if (CollectionUtils.isEmpty(loginProcessors)) {
                log.info("登录失败,没有登录命令业务处理器!");
                Aio.remove(channelContext, "no login serviceHandler processor!");
                return null;
            } else {
                UserCmdProcessor loginServiceHandler = (UserCmdProcessor)loginProcessors.get(0);
                ImSessionContext imSessionContext = (ImSessionContext)channelContext.getAttribute();
                UserBody userReqBody = (UserBody) JsonKit.toBean(packet.getBody(), UserBody.class);
                UserRespBody userRespBody = loginServiceHandler.access(userReqBody, channelContext);
                if (userRespBody != null && userRespBody.getUser() != null) {
                    User user = userRespBody.getUser();
                    String userId = user.getId();
                    IProtocol protocol = ImKit.protocol((ByteBuffer)null, channelContext);
                    String terminal = protocol == null ? "" : protocol.name();
                    user.setTerminal(terminal);
                    imSessionContext.getClient().setUser(user);
                    ImAio.bindUser(channelContext, userId, this.imConfig.getMessageHelper().getBindListener());
                    this.bindUnbindGroup(channelContext, user);
                    loginServiceHandler.onSuccess(channelContext);
                    userRespBody.clear();
                    ImPacket loginRespPacket = new ImPacket(Command.COMMAND_LOGIN_RESP, userRespBody.toByte());
                    return loginRespPacket;
                } else {
                    log.info("登录失败, loginName:{}, password:{}", userReqBody.getMemberId());
                    if (userRespBody == null) {
                        userRespBody = new UserRespBody(Command.COMMAND_LOGIN_RESP, ImStatus.C10008);
                    }

                    userRespBody.clear();
                    ImPacket loginRespPacket = new ImPacket(Command.COMMAND_LOGIN_RESP, userRespBody.toByte());
                    ImAio.bSend(channelContext, loginRespPacket);
                    ImAio.remove(channelContext, "loginName and token is incorrect");
                    return null;
                }
            }
        }
    }

    public void bindUnbindGroup(ChannelContext channelContext, User user) throws Exception {
        String userId = user.getId();
        List<Group> groups = user.getGroups();
        if (groups != null) {
            boolean isStore = "on".equals(this.imConfig.getIsStore());
            MessageHelper messageHelper = null;
            List<String> groupIds = null;
            if (isStore) {
                messageHelper = this.imConfig.getMessageHelper();
                groupIds = messageHelper.getGroups(userId);
            }

            Iterator var8 = groups.iterator();

            while(var8.hasNext()) {
                Group group = (Group)var8.next();
                if (isStore && groupIds != null) {
                    groupIds.remove(group.getGroup_id());
                }

                ImPacket groupPacket = new ImPacket(Command.COMMAND_JOIN_GROUP_REQ, JsonKit.toJsonBytes(group));

                try {
                    JoinGroupReqHandler joinGroupReqHandler = (JoinGroupReqHandler) CommandManager.getCommand(Command.COMMAND_JOIN_GROUP_REQ, JoinGroupReqHandler.class);
                    joinGroupReqHandler.bindGroup(groupPacket, channelContext);
                } catch (Exception var12) {
                    log.error(var12.toString(), var12);
                }
            }

            if (isStore && groupIds != null) {
                var8 = groupIds.iterator();

                while(var8.hasNext()) {
                    String groupId = (String)var8.next();
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
