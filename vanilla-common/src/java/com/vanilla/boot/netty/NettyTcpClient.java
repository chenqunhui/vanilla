package com.vanilla.boot.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vanilla.boot.TcpClient;
import com.vanilla.boot.netty.codec.HessianDecoder;
import com.vanilla.boot.netty.codec.HessianEncoder;
import com.vanilla.boot.netty.conf.NettyClientConfig;
import com.vanilla.boot.netty.exception.UnConnectedException;
import com.vanilla.boot.netty.handler.ClientHeartbeatHandler;
import com.vanilla.boot.netty.handler.ServerNettyConnectHolder;

public class NettyTcpClient implements TcpClient,Runnable {

	private Logger logger= Logger.getLogger(NettyTcpClient.class);
	
	private Bootstrap m_bootstrap;
	
	private NettyClientConfig conf;
	
	private CountDownLatch countDown = new CountDownLatch(1); //初始化锁
	
	private boolean isActive = false; //链接状态
	
	private boolean isShutdown = false;
	
	private HandlerFactory handlerFactory;
	
	private FilterHandlerFactory  filterHandlerFactory;
	
	private ChannelFuture future;
	//自动重连
	private Thread thread = new Thread("NettyTcpClient-auto-reconnect-thread"){
		public void run() {
			while(true){
				if(!isActive && !isShutdown){
					logger.info("lost server " + conf.getHost()+":"+conf.getPort()+" for 10 senconds, reconnect it");
					connect();
				}
				try {
					Thread.sleep(10 * 1000L); // check every 10 seconds
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	};
	
	//异步初始化
	public void run() {
		init();
	}
	
	public NettyTcpClient(NettyClientConfig conf,HandlerFactory handlerFactory,FilterHandlerFactory  filterHandlerFactory){
		this.conf = conf;
		this.handlerFactory = handlerFactory;
		this.filterHandlerFactory = filterHandlerFactory;
		new Thread(this).start();//异步初始化
		try {
			countDown.await();
			thread.start();//自动重连
		} catch (InterruptedException e) {
			//ignor
		}
	}
	
	public void init() {
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
				
				//Decoder
				pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast("byteDecoder",new ByteArrayDecoder());
				pipeline.addLast("hessianDecoder",new HessianDecoder());
				//pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
				
				//Encoder
				pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
				pipeline.addLast("byteEncoder",new ByteArrayEncoder());
				pipeline.addLast("hessianEncoder",new HessianEncoder());
				//pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
				
				pipeline.addLast("heartbeat", new IdleStateHandler(0, 0, 5,TimeUnit.SECONDS));
				pipeline.addLast(new ClientHeartbeatHandler(NettyTcpClient.this));
				if(null != filterHandlerFactory && null != filterHandlerFactory.getFilters() && filterHandlerFactory.getFilters().size()>0){
					List<ChannelInboundHandler> filters = filterHandlerFactory.getFilters();
					for(int i=0;i<filters.size();i++){
						pipeline.addLast("filter"+(i+1), filters.get(i));
					}
				}
				pipeline.addLast("handler" ,handlerFactory.getObject());
			}
		});
		m_bootstrap = bootstrap;
		connect();
		countDown.countDown();
		try {
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void connect() {
		if(isActive){
			return;
		}
		synchronized(this){
			if(isActive){
				return;
			}
			try {
				future = m_bootstrap.connect(new InetSocketAddress(conf.getHost(),conf.getPort()));
				future.awaitUninterruptibly(100, TimeUnit.MILLISECONDS); // 100 ms
				if (!future.isSuccess()) {
					System.err.println("Error when try connecting to " + conf.getHost()+":"+conf.getPort());
					future.channel().close();
					return;
				} 
				isActive = true;
				logger.info("Connected to  server at " + conf.getHost()+":"+conf.getPort()+" success !");
			} catch (Throwable e) {
				logger.error("Error when connect server " + conf.getHost()+":"+conf.getPort(), e);
				if (future != null) {
					future.channel().close();;
				}
			}
		}
		
	}
	
	
	
	public void send(Object msg) {
		if(!isActive){
			throw new UnConnectedException("lost connect to "+ conf.getHost()+":"+conf.getPort());
		}
		future.channel().write(msg);
	}

	public void close() {
		isShutdown = true;
	}
	
	public void actived(){
		isActive = true;
	}
	
	public void inActived(){
		isActive = false;
	}
	
	public NettyClientConfig getConfig(){
		return conf;
	}
}
