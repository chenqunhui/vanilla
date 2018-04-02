package com.vanilla.remoteing.netty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vanilla.cluster.LoadBalance;
import com.vanilla.common.URL;
import com.vanilla.remoting.channel.AutoAckChannelManager;
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
import io.netty.handler.timeout.IdleStateHandler;

public class NettyChannelManager extends AutoAckChannelManager{

	private static Logger logger = Logger.getLogger(NettyChannelManager.class);
	private Bootstrap m_bootstrap;
	private LoadBalance loadBalance;
	
	
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
				
				 //Encoder
				 pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
				 pipeline.addLast("byteEncoder",new ByteArrayEncoder());
				 pipeline.addLast("hessianEncoder",new HessianEncoder());
				pipeline.addLast("heartbeat", new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS));
			}
		});
		m_bootstrap = bootstrap;
	}
	
	
	@Override
	public com.vanilla.remoting.channel.Channel createChannel(URL url) {
		ChannelFuture future = m_bootstrap.connect(url.getHost(),url.getPort());
		future.awaitUninterruptibly(1000, TimeUnit.MILLISECONDS); // 1000ms
		if (!future.isSuccess()) {
			logger.error("try connect to " + url.getHost() + ":" + url.getPort() + " error!");
			future.channel().close();
			return null;
		}
		Channel channel = future.channel();
		NettyChannel nettyChannel = NettyChannel.getOrAddChannel(channel, url);
		return nettyChannel;
	}

	@Override
	public void setLoadBalance(LoadBalance loadBalance) {
		this.loadBalance = loadBalance;
	}

	@Override
	protected com.vanilla.remoting.channel.Channel selectChannel() {
		if(null != loadBalance){
			//TODO how to use loadbalance loadBalance.select(urls, url);
		}
		List<NettyChannel> channels = new ArrayList<NettyChannel>();
		channels.addAll(NettyChannel.getExistsChannel());
		if(channels.isEmpty()){
			return null;
		}else if(channels.size() == 1){
			return channels.get(0);
		}else{
			return channels.get(new Random().nextInt(channels.size()));
		}
	}


	@Override
	public Set<com.vanilla.remoting.channel.Channel> existsChannels() {
		Set<com.vanilla.remoting.channel.Channel> channels = new HashSet<com.vanilla.remoting.channel.Channel>();
		channels.addAll(NettyChannel.getExistsChannel());
		return channels;
	}
}
