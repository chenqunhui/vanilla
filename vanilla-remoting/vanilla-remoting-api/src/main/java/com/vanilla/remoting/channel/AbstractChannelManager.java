package com.vanilla.remoting.channel;


import org.apache.log4j.Logger;

import com.vanilla.common.Constants;
import com.vanilla.remoting.RemotingException;
import com.vanilla.remoting.exchange.Request;
import com.vanilla.remoting.exchange.ResponseCallback;
import com.vanilla.remoting.exchange.ResponseFuture;
import com.vanilla.remoting.exchange.support.DefaultResponseFuture;

public abstract class AbstractChannelManager implements ChannelManager{

	private Logger logger = Logger.getLogger(AbstractChannelManager.class);
	
	protected abstract Channel selectChannel();
	//protected abstract void removeChannel(Channel channel);
	
	@Override
	public ResponseFuture send(Request message) throws RemotingException {
		return send(message,Constants.DEFAULT_TIMEOUT);
	}

	@Override
	public ResponseFuture send(Request reuqest, long timeout) throws RemotingException {
		Channel channel = selectChannel();
		if(null == channel){
			throw new RemotingException(null ,"no channel can be used !");
		}
		DefaultResponseFuture future = new DefaultResponseFuture(channel,reuqest,timeout);
		future.doSent();
		return future;
	}

	@Override
	public void asyncSend(Request reuqest, ResponseCallback callback) throws RemotingException{
		Channel channel = selectChannel();
		if(null == channel){
			throw new RemotingException(null ,"no channel can be used !");
		}
		new  DefaultResponseFuture(channel,reuqest,callback).doSent();
	}
}
