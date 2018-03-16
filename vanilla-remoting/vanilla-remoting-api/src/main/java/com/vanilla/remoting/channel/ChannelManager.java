package com.vanilla.remoting.channel;

import java.util.Set;

import com.vanilla.cluster.LoadBalance;
import com.vanilla.common.URL;
import com.vanilla.remoting.exchange.Exchange;

public interface ChannelManager extends Exchange{

	public  Channel createChannel(URL url);
	
	public Set<Channel> existsChannels();
	
	public void setLoadBalance(LoadBalance loadBalance);
}
