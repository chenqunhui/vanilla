package com.vanilla.moniter;



import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vanilla.client.conf.ClientConfig;
import com.vanilla.moniter.transport.MoniterChannelManager;

public class TcpMonitClient {

	private static Logger logger = Logger.getLogger(TcpMonitClient.class);
	
	private  ApplicationContext applicationContext;
	
	public  ClientConfig config;   //客户端配置
	
	private static TcpMonitClient s_instance = new TcpMonitClient();
	
	private MoniterChannelManager manager;
	
	private  volatile boolean s_init = false;
	
	public static TcpMonitClient getInstance(){
		s_instance.initialize();
		return s_instance;
	}
	
	private TcpMonitClient(){
		
	}
	public void add(LoggingEvent event){
		manager.add(event);
	}
	
	public static void main(String[] args){
		s_instance.initConfig();
		s_instance.initialize();
		s_instance.manager.sync();
	}
	
	public  void initialize() {
		if (!s_init) {
			synchronized (s_instance) {
				if (!s_init) {
					manager  = new MoniterChannelManager(config);
					manager.init();
					s_init = true;
				}
			}
		}
	}
	
	private void initConfig(){
		//加载配置
		try{
			applicationContext = new ClassPathXmlApplicationContext("spring-push-client.xml"); 
			this.config = (ClientConfig)applicationContext.getBean("ClientConfig");
		}catch(NoSuchBeanDefinitionException e){
			logger.info("No serverConfig be found,init with default");
			this.config = new ClientConfig().defaultConfig();
		}
	}
}
