package com.vanilla.rpc;

import java.util.List;

import com.vanilla.rpc.exception.RpcException;

public interface InvokerFactory<T>{

	public Invoker<T> getInvoker();
	
	public List<Invoker<T>> existsInvoker();
}
