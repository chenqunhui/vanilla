package com.vanilla.push;

import com.vanilla.remoting.Client;
import io.netty.channel.ChannelInboundHandler;

import com.vanilla.remoteing.netty.HandlerFactory;
import com.vanilla.remoteing.netty.NettyClient;
import com.vanilla.remoteing.netty.config.NettyClientConfig;



public class PushClient {
	public static void main(String[] args){
		final Client  client = new NettyClient(NettyClientConfig.defaultConfig(),new HandlerFactory(){
			@Override
			public ChannelInboundHandler getObject() {
				return new PushClientHandler();
			}
		},null);
		while(true){
			try {
				client.send("++++++++++++++++++++++++++++++++++++++++++++++++"
						+ "");
			} catch(Exception e){
				e.printStackTrace();
			}
			
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		
	}
	
	

}
