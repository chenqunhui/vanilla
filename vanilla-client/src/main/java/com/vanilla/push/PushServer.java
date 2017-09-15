package com.vanilla.push;

import io.netty.channel.ChannelInboundHandler;

import com.vanilla.boot.TcpServer;
import com.vanilla.boot.netty.HandlerFactory;
import com.vanilla.boot.netty.NettyTcpServer;
import com.vanilla.boot.netty.conf.NettyServerConfig;

public class PushServer {

	public static void main(String[] args){
		TcpServer  server = new NettyTcpServer(NettyServerConfig.defaultConfig(),new HandlerFactory(){
			@Override
			public ChannelInboundHandler getObject() {
				return new PushClientHandler();
			}
		},null);
		server.init();
	}
}
