package com.vanilla.remoting.exchange;


import com.vanilla.remoting.RemotingException;
import com.vanilla.remoting.channel.Channel;

/**
 * 发送数据
 * @author chenqunhui
 *
 */
public interface Exchange {

	ResponseFuture send(Request request) throws RemotingException;
	
	ResponseFuture send(Request request,long timeoutInMillis) throws RemotingException;
	
	void asyncSend(Request request,ResponseCallback callback) throws RemotingException;
}
