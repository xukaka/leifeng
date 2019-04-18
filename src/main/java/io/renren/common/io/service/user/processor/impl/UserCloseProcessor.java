package io.renren.common.io.service.user.processor.impl;

import io.renren.common.io.SocketServiceUtil;
import io.renren.common.io.service.user.processor.UserCloseServer;
import io.renren.common.utils.RedisUtils;
import org.jim.common.ImAio;
import org.jim.common.packets.CloseReqBody;
import org.jim.server.command.handler.CloseReqHandler;
import org.tio.core.ChannelContext;

/**
 * @auther: Easy
 * @date: 19-4-18 16:59
 * @description:
 */
public class UserCloseProcessor  implements UserCloseServer {
    @Override
    public void close(CloseReqBody closeReqBody, ChannelContext channelContext) {

        if(closeReqBody == null || closeReqBody.getUserid() == null){
            RedisUtils redisUtils = SocketServiceUtil.getBean(RedisUtils.class);
            redisUtils.delete("online:"+channelContext.getUserid());
            ImAio.remove(channelContext, "收到关闭请求");
        }else{
            String userId = closeReqBody.getUserid();
            RedisUtils redisUtils = SocketServiceUtil.getBean(RedisUtils.class);
            redisUtils.delete("online:"+userId);
            ImAio.remove(userId, "收到关闭请求!");
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
