package com.vanilla.moniter.transport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import com.vanilla.client.conf.ClientConfig;
import com.vanilla.client.heartbeat.ClientHeartbeatHandler;
import com.vanilla.protocol.protobuf.PushMessageProto;
import com.vanilla.client.transport.ClientChannelManagerAdapter;
import com.vanilla.moniter.beans.MonitLogJava;
import com.vanilla.moniter.builders.Builder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class MoniterChannelManager extends ClientChannelManagerAdapter {
	
	private Logger logger = Logger.getLogger(MoniterChannelManager.class);
	
	private  ClientConfig config;   //客户端配置
	
	private LinkedBlockingQueue<MonitLogJava> m_queue = new LinkedBlockingQueue<MonitLogJava>(3000);
	
	private ExecutorService es = Executors.newFixedThreadPool(10);

	public MoniterChannelManager(ClientConfig config) {
		super(config);
		this.config = config;
	}
	
	/**
	 * 
	 * @param event
	 */
	public void add(LoggingEvent event){
		try {
			m_queue.offer(Builder.buildJavaLog(event),30,TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("log add to queue failed");
		}
	}

	@Override
	public void init(){
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
				pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
				pipeline.addLast("protobufDecoder",new ProtobufDecoder(PushMessageProto.PushMessage.getDefaultInstance()));
				pipeline.addLast("protobufEncoder",new ProtobufEncoder());
				pipeline.addLast("heartbeat", config.getHeartbeatConfig());
				pipeline.addLast("heartbeatRespHandler",new ClientHeartbeatHandler(config,MoniterChannelManager.this));
			}
		});
		m_bootstrap = bootstrap;
		connect();
		es.execute(new Runnable(){
			@Override
			public void run() {
				MonitLogJava log  = null;
				while(isAvtive&&!isShutdown){
					try {
						 log = m_queue.take();
						 send(log);
					} catch (InterruptedException e) {
						//退出
					}
				}
				
			}
			
		});
		
		new Thread(this).start();
	}

	@Override
	public String getName() {
		return "moniter-client";
	}

}
