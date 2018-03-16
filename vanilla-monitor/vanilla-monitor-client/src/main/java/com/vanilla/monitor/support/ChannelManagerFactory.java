package com.vanilla.monitor.support;

import com.vanilla.remoteing.netty.NettyChannelManager;
import com.vanilla.remoting.channel.ChannelManager;

public class ChannelManagerFactory {

	public static ChannelManager getChannelManager(String protocol){
		if(null == protocol){
			throw new NullPointerException("cant get channelManager,protocol is null");
		}
		if("netty4".equalsIgnoreCase(protocol)){
			return new NettyChannelManager();
		}
		return null;
	}
}
