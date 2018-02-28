package com.vanilla.monitor.handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vanilla.remoting.spi.Message;
import com.vanilla.remoting.spi.MessageType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MonitorLogHandler extends ChannelInboundHandlerAdapter{

	private Logger logger  = Logger.getLogger(MonitorLogHandler.class);
	private static BlockingQueue<Message> m_queue = new LinkedBlockingQueue<Message>(10000);
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	System.out.println("MonitorLogHandler开始处理");
    	if(msg instanceof Message){
    		Message message = (Message)msg;
            if(MessageType.MONIT_LOG.getCode() != message.getType()){
            	ctx.fireChannelRead(msg);
            }
            try{
            	m_queue.offer(message,100,TimeUnit.MILLISECONDS);
            }catch(Exception e){
            	logger.error("queue is full...");
            }
            return ;
    	}
        ctx.fireChannelRead(msg);
        
    }
    
    
}
