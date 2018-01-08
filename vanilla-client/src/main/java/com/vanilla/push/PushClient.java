package com.vanilla.push;

import io.netty.channel.ChannelInboundHandler;

import com.vanilla.boot.TcpClient;
import com.vanilla.boot.netty.HandlerFactory;
import com.vanilla.boot.netty.NettyTcpClient;
import com.vanilla.boot.netty.conf.NettyClientConfig;

public class PushClient {
	public static void main(String[] args){
		final TcpClient  client = new NettyTcpClient(NettyClientConfig.defaultConfig(),new HandlerFactory(){
			@Override
			public ChannelInboundHandler getObject() {
				return new PushClientHandler();
			}
		},null);
		while(true){
			try {
				client.send("q	rwqkr	w isfsjlfdaFSfsfasfSAFSAFSAFS"
						+ "t4t34t34hgsaddsfawewqfjklllllllllllllllllllllll");
				Thread.sleep(3000);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
	}
	
	

}
