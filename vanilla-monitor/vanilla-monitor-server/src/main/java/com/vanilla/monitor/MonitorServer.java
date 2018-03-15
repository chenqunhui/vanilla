package com.vanilla.monitor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vanilla.common.URL;
import com.vanilla.monitor.beans.MonitorLogJava;
import com.vanilla.monitor.beans.proto.MonitorLogProto;
import com.vanilla.monitor.codec.MonitorLogCodec;
import com.vanilla.monitor.handler.MonitorLogHandler;
import com.vanilla.monitor.support.DefaultRegistryFactory;
import com.vanilla.register.Registry;
import com.vanilla.register.RegistryFactory;
import com.vanilla.remoteing.netty.NettyServer;
import com.vanilla.remoteing.netty.config.NettyServerConfig;
import com.vanilla.remoteing.netty.handler.ServerHeartbeatListener;
import com.vanilla.remoteing.netty.handler.ServerNettyConnectHolder;
import com.vanilla.remoting.Server;
import com.vanilla.remoting.spi.MessageType;
import com.vanilla.remoting.spi.codec.hessian.HessianDecoder;
import com.vanilla.remoting.spi.codec.hessian.HessianEncoder;
import com.vanilla.remoting.spi.codec.protobuf.MessageProto;
import com.vanilla.remoting.spi.codec.protobuf.ProtobufToMessageCodec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
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
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class MonitorServer implements Server{

	
	private Logger logger = Logger.getLogger(NettyServer.class);
	
	private NettyServerConfig conf;
	
	private boolean isStarted = false;
	
	private URL subscribeUrl= new URL("zookeeper","monitor",2181);
	
	private ServerBootstrap boot;
	
	public static void main(String[] args){
		NettyServerConfig config = NettyServerConfig.defaultConfig();
		config.setPort(21885);
		Server  server = new MonitorServer(config);
		RegistryFactory registryFactory = new DefaultRegistryFactory();
		URL registerUrl = new URL("zookeeper","127.0.0.1",2181);
		Registry registry = registryFactory.getRegistry(registerUrl);
		registry.register(new URL("netty4","monitor2181"+"/127.0.0.1",config.getPort()));
		server.init();
	}
	
	
	public MonitorServer(NettyServerConfig conf){
		this.conf = conf;
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
							pipeline.addLast("connect",new ServerNettyConnectHolder());
							//Decoder
							pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
							pipeline.addLast("byteDecoder",new ByteArrayDecoder());
							pipeline.addLast("hessianDecoder",new HessianDecoder());
							//pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//							pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
//							pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
//							pipeline.addLast("protobufDecoder",new ProtobufDecoder(MonitorLogProto.MonitorLog.getDefaultInstance()));
//							pipeline.addLast("protobufEncoder",new ProtobufEncoder());
							//pipeline.addLast("codec",new ProtobufToMessageCodec());
							//pipeline.addLast("codec",new MonitorLogCodec());
							//Encoder
							pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
							pipeline.addLast("byteEncoder",new ByteArrayEncoder());
							pipeline.addLast("hessianEncoder",new HessianEncoder());
							//pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
							//设置心跳读写超时时间
							//pipeline.addLast("heartbeat", new IdleStateHandler(0, 0, conf.getTickTime(),TimeUnit.MILLISECONDS));
							//pipeline.addLast(new ServerHeartbeatListener(conf,MonitorServer.this));
							pipeline.addLast("handler" ,new MonitorLogHandler());
							
						}
					});
			ChannelFuture f = boot.bind(conf.getPort());
			//f.addListener((GenericFutureListener<? extends Future<? super Void>>) new ServerFutureListener());
			isStarted = true;
			new Thread(new MonitorLogHandler()).start();//TODO处理异常消息
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


	@Override
	public boolean isPing(Object msg) {
		if(msg instanceof MonitorLogJava){
			MonitorLogJava log = (MonitorLogJava)msg;
			if(log.getType() == MessageType.HEARTBEAT.getCode()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}


	@Override
	public Object pong() {
		MonitorLogJava pong = new MonitorLogJava();
		pong.setMsg("pong");
		pong.setType(MessageType.HEARTBEAT_RESP.getCode());
		return pong;
	}
}
