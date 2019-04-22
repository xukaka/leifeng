package io.renren.common.io.service.group;

import com.google.common.collect.Sets;
import io.renren.common.io.body.UserBody;
import io.renren.common.io.body.UserGroupBody;
import org.apache.commons.collections4.CollectionUtils;
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
import org.jim.server.command.handler.processor.chat.ChatCmdProcessor;
import org.jim.server.command.handler.processor.chat.MsgQueueRunnable;
import org.jim.server.command.handler.processor.group.GroupCmdProcessor;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;

import java.util.List;

public class UserGroupHandler extends AbstractCmdHandler {
    public UserGroupHandler() {
    }

    public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception {
        if (packet.getBody() == null) {
            throw new Exception("body is null");
        } else {
            ImSessionContext imSessionContext = (ImSessionContext)channelContext.getAttribute();

            UserGroupBody userGroupBody = JsonKit.toBean(packet.getBody(), UserGroupBody.class);
            Group group = new Group(userGroupBody.getId(),userGroupBody.getId()+"通讯组");
            User user = ImAio.getUser(channelContext.getUserid());
            user.getGroups().add(group);
            //实际绑定之前执行处理器动作
            List<GroupCmdProcessor> groupCmdProcessors = this.getProcessor(channelContext, GroupCmdProcessor.class);
            //先定义为操作成功
            JoinGroupResult joinGroupResult = JoinGroupResult.JOIN_GROUP_RESULT_OK;
            JoinGroupRespBody joinGroupRespBody = new JoinGroupRespBody();
            //当有群组处理器时候才会去处理
            if(CollectionUtils.isNotEmpty(groupCmdProcessors)){
                GroupCmdProcessor groupCmdProcessor = groupCmdProcessors.get(0);
                joinGroupRespBody = groupCmdProcessor.join(group, channelContext);
                if (joinGroupRespBody == null || JoinGroupResult.JOIN_GROUP_RESULT_OK.getNumber() != joinGroupRespBody.getResult().getNumber()) {
                    RespBody joinRespBody = new RespBody(Command.COMMAND_JOIN_GROUP_RESP, ImStatus.C10012).setData(joinGroupRespBody);
                    ImPacket respPacket = ImKit.ConvertRespPacket(joinRespBody, channelContext);
                    return respPacket;
                }
            }
            imSessionContext.getClient().setUser(user);

            //处理完处理器内容后
            ImAio.bindGroup(channelContext, group.getGroup_id(),imConfig.getMessageHelper().getBindListener());

            //回一条消息，告诉对方进群结果
            joinGroupRespBody.setGroup(userGroupBody.getId());
            joinGroupRespBody.setResult(joinGroupResult);

            RespBody joinRespBody = new RespBody(Command.COMMAND_JOIN_GROUP_RESP,ImStatus.C10011).setData(joinGroupRespBody);
            ImPacket respPacket = ImKit.ConvertRespPacket(joinRespBody, channelContext);
            return respPacket;

        }
    }


    public Command command() {
        return Command.addAndGet("COMMAND_JOIN_USER_GROUP_REQ",22);
    }}