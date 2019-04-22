package io.renren.common.io.service.group;

import com.google.common.collect.Sets;
import io.renren.common.io.body.UserBody;
import io.renren.common.io.body.UserGroupBody;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jim.common.ImAio;
import org.jim.common.ImPacket;
import org.jim.common.ImSessionContext;
import org.jim.common.ImStatus;
import org.jim.common.message.MessageHelper;
import org.jim.common.packets.*;
import org.jim.common.utils.ChatKit;
import org.jim.common.utils.ImKit;
import org.jim.common.utils.JsonKit;
import org.jim.server.command.AbstractCmdHandler;

import org.tio.core.Aio;
import org.tio.core.ChannelContext;

import java.util.List;

public class UserGroupHandler extends AbstractCmdHandler {
    public UserGroupHandler() {
    }
    public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception {
        ImPacket joinGroupRespPacket = this.bindGroup(packet, channelContext);
        this.joinGroupNotify(packet, channelContext);
        return joinGroupRespPacket;
    }

    public void joinGroupNotify(ImPacket packet, ChannelContext channelContext) {
        ImSessionContext imSessionContext = (ImSessionContext)channelContext.getAttribute();
        User clientUser = ImAio.getUser(channelContext.getUserid());
        User notifyUser = new User(clientUser.getId(), clientUser.getNick());
        UserGroupBody userGroupBody = JsonKit.toBean(packet.getBody(), UserGroupBody.class);
        Group joinGroup = new Group(userGroupBody.getId(),userGroupBody.getId()+"通讯组");
        clientUser.getGroups().add(joinGroup);
         imSessionContext.getClient().setUser(clientUser);

        String groupId = joinGroup.getGroup_id();
        JoinGroupNotifyRespBody joinGroupNotifyRespBody = (new JoinGroupNotifyRespBody()).setGroup(groupId).setUser(notifyUser);
        RespBody notifyRespBody = new RespBody(Command.COMMAND_JOIN_GROUP_NOTIFY_RESP, joinGroupNotifyRespBody);
        ImPacket joinGroupNotifyrespPacket = new ImPacket(Command.COMMAND_JOIN_GROUP_NOTIFY_RESP, notifyRespBody.toByte());
        ImAio.sendToGroup(groupId, joinGroupNotifyrespPacket);
    }
    public ImPacket bindGroup(ImPacket packet, ChannelContext channelContext) throws Exception {
        ImSessionContext imSessionContext = (ImSessionContext)channelContext.getAttribute();

        if (packet.getBody() == null) {
            throw new Exception("body is null");
        } else {
            UserGroupBody userGroupBody = JsonKit.toBean(packet.getBody(), UserGroupBody.class);
            User clientUser = ImAio.getUser(channelContext.getUserid());
            Group joinGroup = new Group(userGroupBody.getId(),userGroupBody.getId()+"通讯组");
            clientUser.getGroups().add(joinGroup);
            String groupId = joinGroup.getGroup_id();
            imSessionContext.getClient().setUser(clientUser);

            if (StringUtils.isBlank(groupId)) {
                Aio.close(channelContext, "group is null when join group");
                return null;
            } else {
                ImAio.bindGroup(channelContext, groupId, this.imConfig.getMessageHelper().getBindListener());
                JoinGroupResult joinGroupResult = JoinGroupResult.JOIN_GROUP_RESULT_OK;
                JoinGroupRespBody joinGroupRespBody = new JoinGroupRespBody();
                joinGroupRespBody.setGroup(groupId);
                joinGroupRespBody.setResult(joinGroupResult);
                RespBody joinRespBody = (new RespBody(Command.COMMAND_JOIN_GROUP_RESP, ImStatus.C10011)).setData(joinGroupRespBody);
                ImPacket respPacket = ImKit.ConvertRespPacket(joinRespBody, channelContext);
                return respPacket;
            }
        }
    }
//    public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception {
//        if (packet.getBody() == null) {
//            throw new Exception("body is null");
//        } else {
//            ImSessionContext imSessionContext = (ImSessionContext)channelContext.getAttribute();
//
//            UserGroupBody userGroupBody = JsonKit.toBean(packet.getBody(), UserGroupBody.class);
//            Group group = new Group(userGroupBody.getId(),userGroupBody.getId()+"通讯组");
//            User user = ImAio.getUser(channelContext.getUserid());
//            user.getGroups().add(group);
//            //实际绑定之前执行处理器动作
//            List<GroupCmdProcessor> groupCmdProcessors = this.getProcessor(channelContext, GroupCmdProcessor.class);
//            //先定义为操作成功
//            JoinGroupResult joinGroupResult = JoinGroupResult.JOIN_GROUP_RESULT_OK;
//            JoinGroupRespBody joinGroupRespBody = new JoinGroupRespBody();
//            //当有群组处理器时候才会去处理
//            if(CollectionUtils.isNotEmpty(groupCmdProcessors)){
//                GroupCmdProcessor groupCmdProcessor = groupCmdProcessors.get(0);
//                joinGroupRespBody = groupCmdProcessor.join(group, channelContext);
//                if (joinGroupRespBody == null || JoinGroupResult.JOIN_GROUP_RESULT_OK.getNumber() != joinGroupRespBody.getResult().getNumber()) {
//                    RespBody joinRespBody = new RespBody(Command.COMMAND_JOIN_GROUP_RESP, ImStatus.C10012).setData(joinGroupRespBody);
//                    ImPacket respPacket = ImKit.ConvertRespPacket(joinRespBody, channelContext);
//                    return respPacket;
//                }
//            }
//            imSessionContext.getClient().setUser(user);
//
//            //处理完处理器内容后
//            ImAio.bindGroup(channelContext, group.getGroup_id(),imConfig.getMessageHelper().getBindListener());
//
//            //回一条消息，告诉对方进群结果
//            joinGroupRespBody.setGroup(userGroupBody.getId());
//            joinGroupRespBody.setResult(joinGroupResult);
//
//            RespBody joinRespBody = new RespBody(Command.COMMAND_JOIN_GROUP_RESP,ImStatus.C10011).setData(joinGroupRespBody);
//            ImPacket respPacket = ImKit.ConvertRespPacket(joinRespBody, channelContext);
//            return respPacket;
//
//        }
//    }


    public Command command() {
        return Command.addAndGet("COMMAND_JOIN_USER_GROUP_REQ",22);
    }}