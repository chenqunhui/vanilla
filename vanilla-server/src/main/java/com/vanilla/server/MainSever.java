package com.vanilla.server;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vanilla.protocol.protobuf.PushMessageProto;
import com.vanilla.server.config.ServerConfig;
import com.vanilla.server.heartbeat.ServerHeartbeatListener;
import com.vanilla.server.listener.ServerFutureListener;
import com.vanilla.server.moniter.TcpReceiver;
import com.vanilla.server.transport.ConnectHandler;
import com.vanilla.server.transport.ServerTranferHandler;

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
import io.netty.handler.codec.string.StringDecoder;
/*import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;*/
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 启动类
 * @author chenqunhui
 *
 */
public class MainSever {

	private  Logger logger = Logger.getLogger(MainSever.class);
	
	private MainSever(){
		
	}
	private static boolean isStarted = false;
	
	private static MainSever singleServer;
	
	private static ApplicationContext applicationContext;
	
	private static LinkedBlockingQueue<PushMessageProto.PushMessage> m_queue = new LinkedBlockingQueue<PushMessageProto.PushMessage>(30000);
	
	private ServerConfig config;
	
	public void listen(final ServerConfig conf) {
		if(null == conf){
			logger.error("push server start error,ServerConfig is null,check it!");
			throw new RuntimeException();
		}
		EventLoopGroup bossGroup = new NioEventLoopGroup(conf.getBossGroup());
		EventLoopGroup workerGroup = new NioEventLoopGroup(conf.getWorkerGroup());
		try {
			ServerBootstrap boot = new ServerBootstrap();
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
							pipeline.addLast("connect",new ConnectHandler());
							pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
							pipeline.addLast("byteDecoder",new ByteArrayDecoder());
							/*pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
							pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
							pipeline.addLast("protobufDecoder",new ProtobufDecoder(PushMessageProto.PushMessage.getDefaultInstance()));
							pipeline.addLast("protobufEncoder",new ProtobufEncoder());*/
							pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
							pipeline.addLast("byteEncoder",new ByteArrayEncoder());
							//pipeline.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
							//pipeline.addLast("msgDecoder", new MessageDecoder());
							//pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
							//pipeline.addLast("msgEecoder", new MessageEncoder());
							//设置心跳读写超时时间
							pipeline.addLast("pong", new IdleStateHandler(0, 0, config.getTickTime(),TimeUnit.MILLISECONDS));
							pipeline.addLast(new ServerHeartbeatListener(config));
							pipeline.addLast("monitlog", new TcpReceiver());
							pipeline.addLast("handler" ,new ServerTranferHandler());
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
	
	private void initConfig(){
		applicationContext = new ClassPathXmlApplicationContext("spring-push.xml"); 
		//加载配置
		try{
			this.config = (ServerConfig)applicationContext.getBean("serverConfig");
		}catch(NoSuchBeanDefinitionException e){
			logger.info("No serverConfig be found,init with default");
			this.config = new ServerConfig().defaultConfig();
		}
	}
	
	private void start(){
		this.listen(config);
	}

	private static MainSever buildMainServer(){
		synchronized(MainSever.class){
			if(null == singleServer)
			singleServer = new MainSever();
		}
		return singleServer;
	}
	/**
	 * 初始化
	 */
	public  void init(){
		logger.info("push server is starting ...");
		synchronized(MainSever.class){
			if(isStarted){
				logger.info("push server has started...");
				return;
			}
			singleServer.initConfig();
			singleServer.start();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		
		
		/*new Thread(new Runnable(){

			public void run() {
				while(true){
					try {
						TimeUnit.MILLISECONDS.sleep(1000l);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for(Entry<String,ChannelHandlerContext> entry : ConnectHandler.context_map.entrySet()){
						Channel ch = entry.getValue().channel();
						//System.out.println("send msg to client"+ChannelHanderUtils.getConnectKey(ctx)+" start...");
						entry.getValue().writeAndFlush("currentTime is"+new Date());
						//System.out.println("send msg to client"+ChannelHanderUtils.getConnectKey(ctx)+" end...");
					};
				}
			}
			
		}).start();*/
		
		MainSever.buildMainServer().init();
	}
}
