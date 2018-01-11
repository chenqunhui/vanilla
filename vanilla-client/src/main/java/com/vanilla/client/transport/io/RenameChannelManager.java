package com.vanilla.client.transport.io;

import com.vanilla.client.heartbeat.ClientHeartbeatHandler;
import com.vanilla.transport.Task;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class RenameChannelManager implements Task{
	
	private Logger m_logger = Logger.getLogger(RenameChannelManager.class);

	private RenameChannelHolder m_activeChannelHolder;    
	
	private Bootstrap m_bootstrap;
	
	//private MessageQueue m_queue;
	
	private InetSocketAddress m_serverAddress;
	
	private boolean m_active = true; //链接状态
	
	private boolean isShutdown = false;
	
	public RenameChannelManager(InetSocketAddress serverAddress){
		m_serverAddress = serverAddress;
		EventLoopGroup group = new NioEventLoopGroup(1, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		
		});

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				/*pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
				pipeline.addLast("protobufDecoder",new ProtobufDecoder(PushMessageProto.PushMessage.getDefaultInstance()));
				pipeline.addLast("protobufEncoder",new ProtobufEncoder());*/
				pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast("byteDecoder",new ByteArrayDecoder());
				pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
				pipeline.addLast("byteEncoder",new ByteArrayEncoder());
				pipeline.addLast("heartbeat", new IdleStateHandler(0, 0, 5,TimeUnit.SECONDS));
//				pipeline.addLast(new ClientHeartbeatHandler(vanillaPush.config));
			}
		});
		m_bootstrap = bootstrap;
		
		RenameChannelHolder holder = initChannel();
		new Thread(this).start();
		if(null != holder){
			this.m_activeChannelHolder = holder;
		}
	}
	
	
	private RenameChannelHolder initChannel(){
		RenameChannelHolder holder = null;
		if (m_activeChannelHolder != null) {
			return m_activeChannelHolder;
		} else {
			holder = new RenameChannelHolder();
			ChannelFuture future = null ;
			try {
				future = m_bootstrap.connect(m_serverAddress);
				future.awaitUninterruptibly(100, TimeUnit.MILLISECONDS); // 100 ms

				if (!future.isSuccess()) {
					m_logger.error("Error when try connecting to " + m_serverAddress);
					closeChannel(future);
				} else {
					m_logger.info("Connected to push server at " + m_serverAddress);
					holder.setChannelFuture(future);
					return holder;
				}
			} catch (Throwable e) {
				m_logger.error("Error when connect server " + m_serverAddress.getAddress(), e);
				if (future != null) {
					closeChannel(future);
				}
			}
		}
		throw new Error("init netty channel failed.." );
	}
	
	private void closeChannel(ChannelFuture channel) {
		try {
			if (channel != null) {
				m_logger.info("close channel " + channel.channel().remoteAddress());
				m_active =false;
				channel.channel().close();
			}
		} catch (Exception e) {
			// ignore
		}
	}
	
	
	/**
	 * 自动重连
	 */
	public void run() {
		while (!m_active && !isShutdown) {
			m_logger.info("lost server " + m_serverAddress.getAddress()+" for 10 senconds, reconnect it");
			initChannel();
			try {
				Thread.sleep(10 * 1000L); // check every 10 seconds
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}


	public String getName() {
		// TODO 
		return null;
	}


	public void shutdown() {
		m_active = false;
		isShutdown = true;
	}


	public RenameChannelHolder getM_activeChannelHolder() {
		return m_activeChannelHolder;
	}


	public void setM_activeChannelHolder(RenameChannelHolder m_activeChannelHolder) {
		this.m_activeChannelHolder = m_activeChannelHolder;
	}
}
