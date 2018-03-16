package com.vanilla.remoting.exchange;


import com.vanilla.remoting.RemotingException;
import com.vanilla.remoting.channel.Channel;

/**
 * 发送数据
 * @author chenqunhui
 *
 */
public interface Exchange {

	ResponseFuture send(Object request) throws RemotingException;
	
	ResponseFuture send(Object request,long timeoutInMillis) throws RemotingException;
	
	void asyncSend(Object request,ResponseCallback callback) throws RemotingException;
}
