package io.renren.common.io.service.user.processor;

import io.renren.common.io.body.UserBody;
import io.renren.common.io.service.user.resp.UserRespBody;
import org.jim.common.packets.LoginReqBody;
import org.jim.common.packets.LoginRespBody;
import org.jim.server.command.handler.processor.CmdProcessor;
import org.tio.core.ChannelContext;

/**
 * @auther: Easy
 * @date: 19-4-18 11:08
 * @description:
 */
public interface UserLoginServer extends CmdProcessor {
    UserRespBody access(UserBody var1, ChannelContext var2);

    void onSuccess(ChannelContext var1);
}
