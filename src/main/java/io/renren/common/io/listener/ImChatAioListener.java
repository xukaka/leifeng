package io.renren.common.io.listener;

import org.jim.common.ImAio;
import org.jim.common.ImPacket;
import org.jim.common.ImSessionContext;
import org.jim.common.packets.*;
import org.jim.server.listener.ImGroupListener;
import org.jim.server.listener.ImServerAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

//聊天通道监听
public class ImChatAioListener extends ImServerAioListener {

	@Override
	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) {
		/*//发退出房间通知  COMMAND_EXIT_GROUP_NOTIFY_RESP
		ImSessionContext imSessionContext = (ImSessionContext)channelContext.getAttribute();
		ExitGroupNotifyRespBody exitGroupNotifyRespBody = new ExitGroupNotifyRespBody();
		exitGroupNotifyRespBody.setGroup(group);
		Client client = imSessionContext.getClient();
		if(client == null)
			return;
		User clientUser = client.getUser();
		if(clientUser == null)
			return;
		User notifyUser = new User(clientUser.getId(),clientUser.getNick());
		exitGroupNotifyRespBody.setUser(notifyUser);
		
		RespBody respBody = new RespBody(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP,exitGroupNotifyRespBody);
		ImPacket imPacket = new ImPacket(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP, respBody.toByte());
		ImAio.sendToGroup(group, imPacket);*/
		
	}
}
