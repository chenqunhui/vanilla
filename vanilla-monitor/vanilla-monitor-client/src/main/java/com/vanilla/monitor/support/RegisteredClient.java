package com.vanilla.monitor.support;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.vanilla.cluster.LoadBalance;
import com.vanilla.common.URL;
import com.vanilla.register.NotifyListener;
import com.vanilla.register.Registry;
import com.vanilla.register.RegistryFactory;
import com.vanilla.remoteing.netty.NettyChannelManager;
import com.vanilla.remoting.RemotingException;
import com.vanilla.remoting.channel.ChannelManager;

public class RegisteredClient extends LoadbalanceClient implements NotifyListener{

	private static Logger logger = Logger.getLogger(RegisteredClient.class);
	private URL subscribeUrl= new URL("zookeeper","monitor",2181);
	private Registry registry;
	private RegistryFactory registryFactory = new DefaultRegistryFactory();;

	
	public RegisteredClient(URL registryUrl,ChannelManager channelManager, LoadBalance loadBalance) {
		super(channelManager, loadBalance);
		registry = registryFactory.getRegistry(registryUrl);
		registry.subscribe(subscribeUrl, this);
	}

	
	public RegisteredClient(URL registryUrl,ChannelManager channelManager){
		super(channelManager, null);
		registry = registryFactory.getRegistry(registryUrl);
		registry.subscribe(subscribeUrl, this);
	}

	@Override
	public synchronized void notify(List<URL> urls) {
		if(logger.isDebugEnabled()){
			logger.debug("notify url is ["+JSON.toJSONString(urls)+"]");
		}
		if(null == urls || urls.isEmpty()){
			return;
		}
		Set<URL> caches = new HashSet<URL>();
		caches.addAll(urls);
		this.setServerURLs(caches);
	}
	
	
	public static void main(String[] args){
		URL registerUrl = new URL("zookeeper","127.0.0.1",2181);
		RegisteredClient client = new RegisteredClient(registerUrl,new NettyChannelManager());
		while(true){
			try {
				client.send("now is "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				
			} catch (RemotingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
