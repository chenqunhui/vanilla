package com.vanilla.monitor.support;


import com.vanilla.cluster.LoadBalance;
import com.vanilla.remoting.channel.ChannelManager;

public class LoadbalanceClient extends AutoConnectClient{

	public LoadbalanceClient(ChannelManager channelManager,LoadBalance loadBalance) {
		super(channelManager);
		channelManager.setLoadBalance(loadBalance);
	}
	


}
