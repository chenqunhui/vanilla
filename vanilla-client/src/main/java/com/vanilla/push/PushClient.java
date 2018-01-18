package com.vanilla.push;

import com.vanilla.remoting.Client;
import com.vanilla.remoting.service.TestService;

import io.netty.channel.ChannelInboundHandler;

import java.io.File;
import java.lang.reflect.Proxy;

import com.vanilla.remoteing.netty.HandlerFactory;
import com.vanilla.remoteing.netty.NettyClient;
import com.vanilla.remoteing.netty.config.NettyClientConfig;



public class PushClient {
	public static void main(String[] args){
		final Client  client = new NettyClient(NettyClientConfig.defaultConfig(),new HandlerFactory(){
			@Override
			public ChannelInboundHandler getObject() {
				return new ClientHandler();
			}
		},null);
		
		TestService t = (TestService)ProxyFactory.getProxy(client, TestService.class);
		File log = new File("/Users/chenqunhui/Document/log.txt"); 
		while(true){
			Double param = Math.random();
			
			String rt = t.todo(String.valueOf(param));
			System.out.println("param ="+param+ " rt ="+rt);
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
		
		}
		

		
	}
	
	
	
	
	

}
