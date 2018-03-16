package com.vanilla.remoting.channel;

import com.vanilla.remoting.RemotingException;
import com.vanilla.remoting.exchange.ResponseCallback;
import com.vanilla.remoting.exchange.ResponseFuture;
import com.vanilla.remoting.exchange.support.AutoAckResponseFuture;

public abstract class AutoAckChannelManager extends AbstractChannelManager{

	protected void  sendFail(){
		
	}
	
	protected void sendSuccess(){
		
	}
	
	@Override
	public ResponseFuture send(Object reuqest, long timeout) throws RemotingException {
		Channel channel = selectChannel();
		if(null == channel){
			throw new RemotingException(null ,"no channel can be used !");
		}
		AutoAckResponseFuture future = new AutoAckResponseFuture(channel,reuqest,timeout);
		future.doSend();
		if(future.isDone()){
			sendSuccess();
		}else{
			sendFail();
		}
		return future;
	}

	@Override
	public void asyncSend(Object message, ResponseCallback callback) throws RemotingException{
		if(null != callback){
			throw new RemotingException(null ,"AutoAck channel not support callback !");
		}
		Channel channel = selectChannel();
		if(null == channel){
			throw new RemotingException(null ,"no channel can be used !");
		}
		AutoAckResponseFuture future = new AutoAckResponseFuture(channel,message);
		future.doSend();
		if(future.isDone()){
			sendSuccess();
		}else{
			sendFail();
		}
	}

}
