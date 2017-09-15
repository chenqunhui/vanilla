package com.vanilla.boot.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vanilla.boot.TcpServer;
import com.vanilla.boot.netty.codec.HessianDecoder;
import com.vanilla.boot.netty.codec.HessianEncoder;
import com.vanilla.boot.netty.conf.NettyServerConfig;
import com.vanilla.boot.netty.handler.NettyConnectHandler;
import com.vanilla.boot.netty.handler.ServerHeartbeatListener;

public  class NettyTcpServer implements TcpServer{

	private Logger logger = Logger.getLogger(NettyTcpServer.class);
	
	private NettyServerConfig conf;
	
	private HandlerFactory handlerFactory;
	
	private FilterHandlerFactory  filterHandlerFactory;
	
	private boolean isStarted = false;
	
	private ServerBootstrap boot;
	
	
	public NettyTcpServer(NettyServerConfig conf, HandlerFactory handlerFactory,FilterHandlerFactory  filterFactory){
		this.conf = conf;
		this.handlerFactory = handlerFactory;
		this.filterHandlerFactory = filterFactory;
	}
	
	public void init() {
		if(null == conf){
			logger.error("push server start error,ServerConfig is null,check it!");
			throw new RuntimeException();
		}
		EventLoopGroup bossGroup = new NioEventLoopGroup(conf.getBossGroup());
		EventLoopGroup workerGroup = new NioEventLoopGroup(conf.getWorkerGroup());
		try {
			boot = new ServerBootstrap();
			boot.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
			/*if(null != conf && null != conf.getParentHandler()){
				boot.handler(conf.getParentHandler());
			}*/
			//.handler(new TcpServerHandler()) // 初始化就会执行
			//.childHandler(new TcpChildHandler())// 客户端成功connect后才执行
			//tcp option
			boot.option(ChannelOption.SO_BACKLOG, 128);// 设置阻塞队列的大小？与系统相关
			boot.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);//动态缓冲区
			boot.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);//使用内存池
			//handler
			boot.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast("connect",new NettyConnectHandler());
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
							
							//设置心跳读写超时时间
							pipeline.addLast("pong", new IdleStateHandler(0, 0, conf.getTickTime(),TimeUnit.MILLISECONDS));
							pipeline.addLast(new ServerHeartbeatListener(conf));
							if(null != filterHandlerFactory && null != filterHandlerFactory.getFilters() && filterHandlerFactory.getFilters().size()>0){
								List<ChannelInboundHandler> filters = filterHandlerFactory.getFilters();
								for(int i=0;i<filters.size();i++){
									pipeline.addLast("filter"+(i+1), filters.get(i));
								}
							}
							pipeline.addLast("handler" ,handlerFactory.getObject());
						}
					});
			ChannelFuture f = boot.bind(conf.getPort());
			//f.addListener((GenericFutureListener<? extends Future<? super Void>>) new ServerFutureListener());
			isStarted = true;
			logger.info("push sever start success,listen on port "+conf.getPort());
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void shutdown() {
		// TODO Auto-generated method stub
	}

	public boolean isShutdown() {
		return !isStarted;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

}
