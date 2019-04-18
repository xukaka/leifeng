package io.renren.common.io.service.user.handler;

import io.renren.common.io.service.user.processor.UserCloseServer;
import io.renren.common.io.service.user.processor.UserLoginServer;
import io.renren.common.io.service.user.processor.impl.UserCloseProcessor;
import io.renren.common.io.service.user.processor.impl.UserLoginProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.jim.common.ImAio;
import org.jim.common.ImPacket;
import org.jim.common.ImStatus;
import org.jim.common.packets.CloseReqBody;
import org.jim.common.packets.Command;
import org.jim.common.packets.RespBody;
import org.jim.common.utils.ImKit;
import org.jim.common.utils.JsonKit;
import org.jim.server.command.AbstractCmdHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;

import java.util.List;

/**
 * @auther: Easy
 * @date: 19-4-18 17:30
 * @description:
 */
public class UserCloseHandler extends AbstractCmdHandler{
    private static Logger log = LoggerFactory.getLogger(UserCloseHandler.class);

    @Override
    public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception
    {
        CloseReqBody closeReqBody = null;
        try{
            closeReqBody = JsonKit.toBean(packet.getBody(),CloseReqBody.class);
        }catch (Exception e) {
            //关闭请求消息格式不正确
            return ImKit.ConvertRespPacket(new RespBody(Command.COMMAND_CLOSE_REQ, ImStatus.C10020), channelContext);
        }
        List<UserCloseServer> userCloseCmdProcessors = this.getProcessor(channelContext, UserCloseServer.class);
        if(CollectionUtils.isEmpty(userCloseCmdProcessors)){
            log.info("登录失败,没有移除命令业务处理器!");
            Aio.remove(channelContext, "no login serviceHandler processor!");
            return null;
        }
        UserCloseServer userCloseServer = userCloseCmdProcessors.get(0);
        userCloseServer.close(closeReqBody,channelContext);

        return ImKit.ConvertRespPacket(new RespBody(Command.COMMAND_CLOSE_REQ, ImStatus.C10021), channelContext);
    }

    @Override
    public Command command() {
        return Command.COMMAND_CLOSE_REQ;
    }
}
