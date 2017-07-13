package com.vanilla.push;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vanilla.client.conf.ClientConfig;
import com.vanilla.client.transport.io.RenameChannelManager;

public class VanillaPush {

	
    private static Logger logger = Logger.getLogger(VanillaPush.class);
	
	private int port;
	
	private static ApplicationContext applicationContext;
	
	public static ClientConfig config;   //客户端配置
	
	private static VanillaPush s_instance = new VanillaPush();

	private static volatile boolean s_init = false;
	
	private static RenameChannelManager manager;
	
	private VanillaPush(){
		
	}

	


	private static void checkAndInitialize() {
		if (!s_init) {
			synchronized (s_instance) {
				if (!s_init) {
					InetSocketAddress serverAdd = new InetSocketAddress(21881);
					manager  = new RenameChannelManager(serverAdd);
					logger.warn("Cat is lazy initialized!");
					s_init = true;
				}
			}
		}
	}
	
	private void initConfig(){
		applicationContext = new ClassPathXmlApplicationContext("spring-push-client.xml"); 
		//加载配置
		try{
			this.config = (ClientConfig)applicationContext.getBean("ClientConfig");
		}catch(NoSuchBeanDefinitionException e){
			logger.info("No serverConfig be found,init with default");
			this.config = new ClientConfig().defaultConfig();
		}
	}
	
	
	public static void main(String[] args){
		
		s_instance.initConfig();
		checkAndInitialize();
		try {
			manager.getM_activeChannelHolder().getChannelFuture().channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
