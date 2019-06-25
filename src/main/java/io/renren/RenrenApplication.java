package io.renren;

import cn.hutool.core.io.resource.ClassPathResource;
import com.jfinal.kit.PropKit;
import io.renren.common.io.command.IoWsHandshakeProcessor;
import io.renren.common.io.listener.ImDemoGroupListener;
import io.renren.common.io.service.group.UserGroupHandler;
import io.renren.common.io.service.user.handler.UserCloseHandler;
import io.renren.common.io.service.user.handler.UserLoginReqHandler;
import io.renren.common.io.service.user.processor.impl.UserCloseProcessor;
import io.renren.common.io.service.user.processor.impl.UserLoginProcessor;
import io.renren.datasources.DynamicDataSourceConfig;
import org.apache.commons.lang3.StringUtils;
import org.jim.common.ImConfig;
import org.jim.common.ImConst;
import org.jim.common.config.PropertyImConfigBuilder;
import org.jim.common.packets.Command;
import org.jim.server.ImServerStarter;
import org.jim.server.command.CommandManager;
import org.jim.server.command.handler.HandshakeReqHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.tio.core.ssl.SslConfig;

import java.io.InputStream;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@Import({DynamicDataSourceConfig.class})
public class RenrenApplication extends SpringBootServletInitializer {

	private static Logger logger = LoggerFactory.getLogger(RenrenApplication.class);
	public static void main(String[] args) {
		ImConfig imConfig = new PropertyImConfigBuilder("jim.properties").build();
		imConfig.setIsSSL("on");
		try {
			initSsl(imConfig);
		}catch (Exception e){
            logger.error("发生异常 msg={}","原因",e);
		}

		//初始化SSL;(开启SSL之前,你要保证你有SSL证书哦...)
		//设置群组监听器，非必须，根据需要自己选择性实现;
		imConfig.setImGroupListener(new ImDemoGroupListener());
		ImServerStarter imServerStarter = new ImServerStarter(imConfig );

		/*****************start 以下处理器根据业务需要自行添加与扩展，每个Command都可以添加扩展,此处为demo中处理**********************************/
		HandshakeReqHandler handshakeReqHandler = CommandManager.getCommand(Command.COMMAND_HANDSHAKE_REQ, HandshakeReqHandler.class);
		//添加自定义握手处理器;
		handshakeReqHandler.addProcessor(new IoWsHandshakeProcessor());
		UserLoginReqHandler userHandler = CommandManager.getCommand(Command.COMMAND_LOGIN_REQ, UserLoginReqHandler.class);
		//添加登录业务处理器;
		userHandler.addProcessor(new UserLoginProcessor());
		UserGroupHandler userGroupHandler = CommandManager.getCommand(Command.addAndGet("COMMAND_JOIN_USER_GROUP_REQ",22), UserGroupHandler.class);
//		userGroupHandler.addProcessor(new UserGroupProcessor());

		UserCloseHandler userCloseHandler = CommandManager.getCommand(Command.COMMAND_CLOSE_REQ,UserCloseHandler.class);
		userCloseHandler.addProcessor(new UserCloseProcessor());
		/*****************end *******************************************************************************************/
		try {
			imServerStarter.start();

		}catch (Exception e){
			logger.error("IM server start failed.");
		}
		SpringApplication.run(RenrenApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(RenrenApplication.class);
	}
	/**
	 * 开启SSL之前，你要保证你有SSL证书哦！
	 * @param imConfig
	 * @throws Exception
	 */
	private static void initSsl(ImConfig imConfig) throws Exception {
		if(ImConst.ON.equals(imConfig.getIsSSL())) {
			String keyStorePath = PropKit.get("jim.key.store.path");

			String keyStoreFile = keyStorePath;
			String trustStoreFile = keyStorePath;
			String keyStorePwd = PropKit.get("jim.key.store.pwd");
			if (StringUtils.isNotBlank(keyStoreFile) && StringUtils.isNotBlank(trustStoreFile)) {
				InputStream	keyStoreInputStream = new ClassPathResource(trustStoreFile).getStream();
				InputStream	trustStoreInputStream = new ClassPathResource(trustStoreFile).getStream();
				SslConfig sslConfig = SslConfig.forServer(keyStoreInputStream, trustStoreInputStream, keyStorePwd);
				imConfig.setSslConfig(sslConfig);
			}
		}}
}
