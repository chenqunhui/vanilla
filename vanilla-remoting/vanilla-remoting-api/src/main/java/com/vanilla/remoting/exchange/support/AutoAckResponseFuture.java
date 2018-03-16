package com.vanilla.remoting.exchange.support;

import org.apache.log4j.Logger;

import com.vanilla.remoting.RemotingException;
import com.vanilla.remoting.channel.Channel;
import com.vanilla.remoting.exchange.ResponseFuture;


public class AutoAckResponseFuture implements ResponseFuture{

	private Logger logger = Logger.getLogger(AutoAckResponseFuture.class);
	private Channel channel;
	private Object msg;
	private long timeout =0;
	private boolean  isSent= false;
	
	public AutoAckResponseFuture(Channel channel,Object msg,long timeout){
		this.channel = channel;
		this.msg = msg;
		this.timeout = timeout;
	}
	
	public AutoAckResponseFuture(Channel channel,Object msg){
		this.channel = channel;
		this.msg = msg;
	}
	
	@Override
	public Object get() throws RemotingException {
		return null;
	}

	@Override
	public Object get(long timeoutInMillis) throws RemotingException {
		return null;
	}

	@Override
	public boolean isDone() {
		return isSent;
	}

	public void doSend() throws RemotingException {
		try{
			if(logger.isDebugEnabled()){
				logger.debug("channel "+channel.getLocalAddress()+"->"+channel.getRemoteAddress()+"send msg start.");
			}
			channel.send(msg, timeout);
			isSent = true;
			if(logger.isDebugEnabled()){
				logger.debug("channel "+channel.getLocalAddress()+"->"+channel.getRemoteAddress()+"send msg success.");
			}
		}catch(Exception e){
			if(logger.isDebugEnabled()){
				logger.debug("channel "+channel.getLocalAddress()+"->"+channel.getRemoteAddress()+"send msg error.",e);
			}
			try{
				channel.close();
			}catch(Exception e1){
				
			}
			throw e;
		}
	}
}
