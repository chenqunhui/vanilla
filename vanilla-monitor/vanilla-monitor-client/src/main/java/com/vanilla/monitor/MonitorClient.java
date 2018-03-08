package com.vanilla.monitor;

import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vanilla.monitor.beans.MonitorLogJava;
import com.vanilla.monitor.beans.proto.MonitorLogProto;
import com.vanilla.monitor.codec.MonitorLogCodec;
import com.vanilla.remoteing.netty.NettyClient;
import com.vanilla.remoteing.netty.config.NettyClientConfig;
import com.vanilla.remoteing.netty.handler.ClientHeartbeatHandler;
import com.vanilla.remoting.Client;
import com.vanilla.remoting.exception.UnConnectedException;
import com.vanilla.remoting.exchange.Request;
import com.vanilla.remoting.exchange.ResponseFuture;
import com.vanilla.remoting.exchange.support.MyResponseFuture;
import com.vanilla.remoting.spi.MessageType;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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
import io.netty.handler.timeout.IdleStateHandler;

public class MonitorClient implements Client, Runnable {

	private Logger logger = Logger.getLogger(NettyClient.class);

	private Bootstrap m_bootstrap;

	private NettyClientConfig conf;

	private CountDownLatch countDown = new CountDownLatch(1); // 初始化锁

	private boolean isActive = false; // 链接状态

	private boolean isShutdown = false;

	private Channel channel;

	private BlockingQueue<MonitorLogJava> queue = new LinkedBlockingQueue<MonitorLogJava>(1000);

	public static void main(String[] args) {
		new MonitorClient(NettyClientConfig.defaultConfig());
	}

	// 自动重连
	private Thread autoReconnectThread = new Thread("MonitorClient-auto-reconnect-thread") {
		public void run() {
			while (true) {
				if (!isActive && !isShutdown) {
					logger.info(
							"lost server " + conf.getHost() + ":" + conf.getPort() + " for 10 senconds, reconnect it");
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

	// 异步发送
	private Thread asyncSendThread = new Thread("MonitorClient-async-send-thread") {
		public void run() {
			while (true) {
				try {
					if (isActive) {
						MonitorLogJava msg = queue.take();
						if (logger.isDebugEnabled()) {
							logger.debug("start send monitor log...");
						}
						channel.writeAndFlush(msg);
						if (logger.isDebugEnabled()) {
							logger.debug("send monitor log success...");
						}
					} else {
						logger.error("connect is not active,wait ...");
						try {
							Thread.sleep(3000l);
						} catch (InterruptedException e) {
						}
					}
				} catch (Exception e) {
					logger.error("async send monitor error .", e);
				}

			}
		}
	};

	// 异步初始化
	public void run() {
		init();
	}

	public MonitorClient(NettyClientConfig conf) {
		this.conf = conf;
		new Thread(this, "monitor-init-thread").start();// 异步初始化
		try {
			countDown.await();
			autoReconnectThread.start();// 自动重连
			asyncSendThread.start();// 异步发送
		} catch (InterruptedException e) {
			// ignor
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
				// Decoder
				// pipeline.addLast("frameDecoder",new
				// LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				// pipeline.addLast("byteDecoder",new ByteArrayDecoder());
				// pipeline.addLast("hessianDecoder",new HessianDecoder());
				// //pipeline.addLast("decoder", new
				// StringDecoder(CharsetUtil.UTF_8));
				//
				// //Encoder
				// pipeline.addLast("frameEncoder", new
				// LengthFieldPrepender(4));
				// pipeline.addLast("byteEncoder",new ByteArrayEncoder());
				// pipeline.addLast("hessianEncoder",new HessianEncoder());
				// pipeline.addLast("encoder", new
				// StringEncoder(CharsetUtil.UTF_8));

				pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
				pipeline.addLast("protobufDecoder",new ProtobufDecoder(MonitorLogProto.MonitorLog.getDefaultInstance()));
				pipeline.addLast("protobufEncoder", new ProtobufEncoder());
				// pipeline.addLast("codec",new ProtobufToMessageCodec());
				pipeline.addLast("codec", new MonitorLogCodec());
				pipeline.addLast("heartbeat", new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS));
				pipeline.addLast(new ClientHeartbeatHandler(conf, MonitorClient.this));
				// pipeline.addLast("handler" ,);
			}
		});
		m_bootstrap = bootstrap;
		connect();
	}

	public void connect() {
		if (isActive) {
			return;
		}
		synchronized (this) {
			if (isActive) {
				return;
			}
			ChannelFuture future = null;
			try {
				future = m_bootstrap.connect(new InetSocketAddress(conf.getHost(), conf.getPort()));
				future.awaitUninterruptibly(100, TimeUnit.MILLISECONDS); // 100ms
				if (!future.isSuccess()) {
					logger.error("Error when try connect to " + conf.getHost() + ":" + conf.getPort() + " !");
					future.channel().close();
					return;
				}
				isActive = true;
				channel = future.channel();
				logger.info("Connected to  server at " + conf.getHost() + ":" + conf.getPort() + " success !");
			} catch (Throwable e) {
				logger.error("Error when connect server " + conf.getHost() + ":" + conf.getPort(), e);
				if (future != null) {
					future.channel().close();
				}
			} finally {
				countDown.countDown();
				try {
					if (null != channel) {
						channel.closeFuture().sync();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void close() {
		isShutdown = true;
	}

	public void actived() {
		isActive = true;
	}

	public void inActived() {
		isActive = false;
	}

	public NettyClientConfig getConfig() {
		return conf;
	}

	public Object ping() {
		MonitorLogJava ping = new MonitorLogJava();
		ping.setMsg("ping");
		return ping;
	}

	public boolean isPong(Object msg) {
		if (msg instanceof MonitorLogJava) {
			MonitorLogJava log = (MonitorLogJava) msg;
			if (log.getType() == MessageType.HEARTBEAT_RESP.getCode()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public ResponseFuture send(MonitorLogJava msg, Request request) {
		try {
			if (!isActive) {
				throw new UnConnectedException("lost connect to " + conf.getHost() + ":" + conf.getPort());
			}
			ResponseFuture future = new MyResponseFuture(request, 3000);
			channel.writeAndFlush(msg);
			return future;
		} catch (Exception e) {
			logger.error("send monitor message error", e);
			return null;
		}
	}

	public void sendAsync(MonitorLogJava msg, Request request) {
		try {
			queue.offer(msg, 50, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.error("send async monitor message error", e);
		}

	}
}
