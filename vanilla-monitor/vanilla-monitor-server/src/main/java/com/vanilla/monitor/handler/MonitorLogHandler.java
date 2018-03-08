package com.vanilla.monitor.handler;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vanilla.monitor.beans.MonitorLogJava;
import com.vanilla.remoting.spi.Message;
import com.vanilla.remoting.spi.MessageType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MonitorLogHandler extends ChannelInboundHandlerAdapter implements Runnable{

	private Logger logger  = Logger.getLogger(MonitorLogHandler.class);
	
	private static BlockingQueue<MonitorLogJava> m_queue = new LinkedBlockingQueue<MonitorLogJava>(10000);
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	if(msg instanceof MonitorLogJava){
    		MonitorLogJava message = (MonitorLogJava)msg;
            if(MessageType.MONIT_LOG.getCode() != message.getType()){ 
            	ctx.fireChannelRead(msg);
            	return;
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

	@Override
	public void run() {
		File logFile = new File("/data/monitor.log");
		while(true){
			try{
				MonitorLogJava javaLog = m_queue.take();
				System.out.println(javaLog.toString());
			}catch(Exception e){
				//TODO
			}
		}
	}
    
    
}
