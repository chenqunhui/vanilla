package com.vanilla.remoteing.netty.handler;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.vanilla.remoteing.netty.config.NettyServerConfig;
import com.vanilla.remoting.Server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 监听客户端心跳
 * @author chenqunhui
 *
 */
public class ServerHeartbeatListener extends ChannelInboundHandlerAdapter {

	private static Logger logger = Logger.getLogger(ServerHeartbeatListener.class);
	
	private AtomicInteger loss_connect_time = new AtomicInteger(0);
	
	private NettyServerConfig serverConfig;
	
	private Server server;
	
	public ServerHeartbeatListener(NettyServerConfig config,Server server){
		this.serverConfig = config;
		this.server = server;
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	    if (evt instanceof IdleStateEvent) {
	      IdleStateEvent event = (IdleStateEvent) evt;  
	      if (event.state() == IdleState.ALL_IDLE) {
	    	  int count = loss_connect_time.incrementAndGet();
	    	  if(logger.isDebugEnabled()){
	    		  logger.debug("Server has not get ping from cliet " + ctx.channel().remoteAddress()+" for "+ count * serverConfig.getTickTime()/1000+"s.");
	    	  }
	    	  if (count > serverConfig.getMaxDelayCount()) { 
	    		  if(logger.isDebugEnabled()){
		    		  logger.debug("Client "+ ctx.channel().remoteAddress()+"  has time out,close it!");
		    	  }
	              ctx.channel().close(); 
	              ctx.close();
	          }   
	        }
	    } else {
	    	super.userEventTriggered(ctx, evt);
	    }
	  }
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("ServerHeartbeatListener开始处理");
		if(server.isPing(msg)){
			if(logger.isDebugEnabled()){
				logger.debug("Heartbeat request from client "+ctx.channel().remoteAddress()+" : ping !");
			}
			loss_connect_time.decrementAndGet();
			ctx.writeAndFlush(server.pong());
		}	
		ctx.fireChannelRead(msg);
    }
	
}
