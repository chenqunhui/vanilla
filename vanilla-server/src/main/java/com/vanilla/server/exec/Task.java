package com.vanilla.server.exec;

import io.netty.channel.ChannelHandlerContext;

public  class Task implements Runnable {

	private ChannelHandlerContext context;
	
	private String msg;
	
	public Task(ChannelHandlerContext context,String msg){
		this.context = context;
		this.msg = msg;
	}
	
	public void run() {
		try {
			Thread.sleep(3000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		context.channel().writeAndFlush("The length of msg from client is "+msg);
	}

	
}
