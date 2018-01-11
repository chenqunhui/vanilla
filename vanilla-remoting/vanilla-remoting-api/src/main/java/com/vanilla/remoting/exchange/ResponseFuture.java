package com.vanilla.remoting.exchange;

public interface ResponseFuture {

	Object get() throws RemotingException;
}
