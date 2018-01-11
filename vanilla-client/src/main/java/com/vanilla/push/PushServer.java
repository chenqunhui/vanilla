package com.vanilla.push;

import com.vanilla.remoteing.netty.HandlerFactory;
import com.vanilla.remoteing.netty.NettyServer;
import com.vanilla.remoteing.netty.config.NettyServerConfig;
import com.vanilla.remoting.Server;

import io.netty.channel.ChannelInboundHandler;


public class PushServer {

	public static void main(String[] args){
		Server  server = new NettyServer(NettyServerConfig.defaultConfig(),new HandlerFactory(){
			@Override
			public ChannelInboundHandler getObject() {
				return new PushClientHandler();
			}
		},null);
		server.init();
	}
}
