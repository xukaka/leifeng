/**
 * 
 */
package io.renren.common.io.command;

import io.renren.common.io.body.UserBody;
import io.renren.common.io.service.user.handler.UserLoginReqHandler;
import org.jim.common.ImAio;
import org.jim.common.ImPacket;
import org.jim.common.http.HttpConst;
import org.jim.common.http.HttpRequest;
import org.jim.common.packets.Command;
import org.jim.common.utils.JsonKit;
import org.jim.server.command.CommandManager;
import org.jim.server.command.handler.processor.handshake.WsHandshakeProcessor;
import org.tio.core.ChannelContext;


public class IoWsHandshakeProcessor extends WsHandshakeProcessor {

	@Override
	public void onAfterHandshaked(ImPacket packet, ChannelContext channelContext) throws Exception {
		UserLoginReqHandler loginHandler = (UserLoginReqHandler) CommandManager.getCommand(Command.COMMAND_LOGIN_REQ);
		HttpRequest request = (HttpRequest)packet;
		String memberId = request.getParams().get("memberId") == null ? null : (String) request.getParams().get("memberId")[0];
		String token = request.getParams().get("token") == null ? null : (String)request.getParams().get("token")[0];

		UserBody userBody = new UserBody(Long.parseLong(memberId),token);
		byte[] loginBytes = JsonKit.toJsonBytes(userBody);
		request.setBody(loginBytes);
		request.setBodyString(new String(loginBytes, HttpConst.CHARSET_NAME));
		ImPacket loginRespPacket = loginHandler.handler(request, channelContext);
		if(loginRespPacket != null){
			ImAio.send(channelContext, loginRespPacket);
		}
	}
}
