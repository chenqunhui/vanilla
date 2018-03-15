package com.vanilla.remoting.channel;

import com.vanilla.cluster.LoadBalance;
import com.vanilla.common.URL;
import com.vanilla.remoting.exchange.Exchange;

public interface ChannelManager extends Exchange{

	public  Channel createChannel(URL url);
	
	public void setLoadBalance(LoadBalance loadBalance);
}
