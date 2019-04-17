package io.renren.common.io.service.user.processor;

import org.jim.common.packets.CloseReqBody;
import org.jim.common.packets.User;
import org.jim.server.command.handler.processor.CmdProcessor;
import org.tio.core.ChannelContext;

/**
 * @auther: Easy
 * @date: 19-4-18 17:04
 * @description:
 */
public interface UserCloseServer extends CmdProcessor {
    void close(CloseReqBody closeReqBody,ChannelContext channelContext);

}
