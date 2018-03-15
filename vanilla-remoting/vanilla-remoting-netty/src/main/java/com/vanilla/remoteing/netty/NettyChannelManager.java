package com.vanilla.remoteing.netty;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vanilla.cluster.LoadBalance;
import com.vanilla.common.URL;
import com.vanilla.remoteing.netty.handler.ClientHeartbeatHandler;
import com.vanilla.remoting.channel.AbstractChannelManager;
import com.vanilla.remoting.spi.codec.hessian.HessianDecoder;
import com.vanilla.remoting.spi.codec.hessian.HessianEncoder;

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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyChannelManager extends AbstractChannelManager{

	private static Logger logger = Logger.getLogger(NettyChannelManager.class);
	private Bootstrap m_bootstrap;
	private LoadBalance loadBalance;
	private List<NettyChannel> channelList = new ArrayList<NettyChannel>();
	
	
	public NettyChannelManager(){
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
				 pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				 pipeline.addLast("byteDecoder",new ByteArrayDecoder());
				 pipeline.addLast("hessianDecoder",new HessianDecoder());
				 //pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
				
				 //Encoder
				 pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
				 pipeline.addLast("byteEncoder",new ByteArrayEncoder());
				 pipeline.addLast("hessianEncoder",new HessianEncoder());
				 //pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

//				pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
//				pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
//				pipeline.addLast("protobufDecoder",new ProtobufDecoder(MonitorLogProto.MonitorLog.getDefaultInstance()));
//				pipeline.addLast("protobufEncoder", new ProtobufEncoder());
				// pipeline.addLast("codec",new ProtobufToMessageCodec());
				//pipeline.addLast("codec", new MonitorLogCodec());
				pipeline.addLast("heartbeat", new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS));
		//		pipeline.addLast(new ClientHeartbeatHandler(conf, MonitorClient.this));
				// pipeline.addLast("handler" ,);
			}
		});
		m_bootstrap = bootstrap;
	}
	
	
	@Override
	public com.vanilla.remoting.channel.Channel createChannel(URL url) {
		ChannelFuture future = m_bootstrap.connect(url.getHost(),url.getPort());
		future.awaitUninterruptibly(100, TimeUnit.MILLISECONDS); // 100ms
		if (!future.isSuccess()) {
			logger.error("Error when try connect to " + url.getHost() + ":" + url.getPort() + " !");
			future.channel().close();
			return null;
		}
		Channel channel = future.channel();
		NettyChannel nettyChannel = NettyChannel.getOrAddChannel(channel, url);
		channelList.add(nettyChannel);
		return nettyChannel;
	}

	@Override
	public void setLoadBalance(LoadBalance loadBalance) {
		this.loadBalance = loadBalance;
	}

	@Override
	protected com.vanilla.remoting.channel.Channel selectChannel() {
		if(null != loadBalance){
			//loadBalance.select(urls, url);
		}
		if(channelList.size()>0){
			return channelList.get(channelList.size()-1);
		}
		return null;
	}

}
