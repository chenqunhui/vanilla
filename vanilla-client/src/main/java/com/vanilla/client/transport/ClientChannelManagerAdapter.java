package com.vanilla.client.transport;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vanilla.client.conf.ClientConfig;
import com.vanilla.moniter.beans.MonitLogJava;
import com.vanilla.transport.ChannelHolder;
import com.vanilla.utils.IpUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;

public abstract class ClientChannelManagerAdapter implements ClientChannelManager{

	private Logger m_logger = Logger.getLogger(ClientChannelManagerAdapter.class);

	private ChannelHolder m_activeChannelHolder;    
	
	protected Bootstrap m_bootstrap;
	
	private InetSocketAddress m_serverAddress;
	
	protected volatile boolean isAvtive;//连接状态 
	
	protected volatile boolean isShutdown = false;
	
	public ClientChannelManagerAdapter(ClientConfig config){
		m_serverAddress = config.getServerAddress();
	}
	
	public void sync(){
		if(null != m_activeChannelHolder){
			try {
				m_activeChannelHolder.getChannelFuture().channel().closeFuture().sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		throw new RuntimeException("server init failed!");
	}
	
	@Override
	public void shutdown() {
		//停止异步重连的线程 
		isShutdown = true;
		close();
	}

	@Override
	public void run() {
		while(!isShutdown){
			if(!isAvtive){
				connect();
			}
			try {
				TimeUnit.MILLISECONDS.sleep(3000);
			} catch (InterruptedException e) {
				if(isShutdown){
					break;
				}
			}
		}
	}

    public void send(MonitLogJava log){
    	MonitLogProto.MonitLog.Builder builder1 = MonitLogProto.MonitLog.newBuilder();
    	builder1.setLevel(log.getLevel());
    	builder1.setLocalAddress(log.getLocalAddress());
    	builder1.setLoggerName(log.getLoggerName());
    	builder1.setMsg(log.getMsg());
    	builder1.setMsgId(log.getMsgId());
    	builder1.setThreadName(log.getThreadName());
    	builder1.setTimestamp(log.getTimestamp());
    	PushMessageProto.PushMessage.Builder builder = PushMessageProto.PushMessage.newBuilder();
    	builder.setLog(builder1);
    	builder.setType(MessageType.MONIT_LOG.getCode());
    	m_activeChannelHolder.writeAndFlush(builder.build());
    }
	
	public void connect(){
		ChannelFuture future = null ;
		try {
			m_logger.info("Connected to  server at " + IpUtils.getHostAndPort(m_serverAddress)+" start");
			future = m_bootstrap.connect(m_serverAddress);
			future.awaitUninterruptibly(100, TimeUnit.MILLISECONDS); // 100 ms

			if (!future.isSuccess()) {
				m_logger.error("Error when try connecting to " + IpUtils.getHostAndPort(m_serverAddress));
				closeChannel(future);
			} else {
				m_logger.info("Connected to server " + IpUtils.getHostAndPort(m_serverAddress)+" success!");
				m_activeChannelHolder = new ChannelHolder();
				m_activeChannelHolder.setChannelFuture(future);
				isAvtive = true;
			}
		} catch (Throwable e) {
			m_logger.error("Error when connect server " + IpUtils.getHostAndPort(m_serverAddress), e);
			if (future != null) {
				closeChannel(future);
			}
		}
	}
	
	
	private void closeChannel(ChannelFuture channel) {
		try {
			if (channel != null) {
				m_logger.info("close channel " + channel.channel().remoteAddress());
				channel.channel().close();
			}
		} catch (Exception e) {
			// ignore
		}
	}
	
	@Override
	public void close() {
		isAvtive = false;
		if(null != m_activeChannelHolder){
			try{
				m_activeChannelHolder.getChannelFuture().channel().close();
			}catch(Exception e){
				// ignore
			}
		}
	}
	
}
