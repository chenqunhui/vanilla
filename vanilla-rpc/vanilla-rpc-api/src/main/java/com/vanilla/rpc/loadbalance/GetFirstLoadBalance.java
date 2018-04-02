package com.vanilla.rpc.loadbalance;

import java.util.List;

import com.vanilla.common.URL;
import com.vanilla.rpc.Invoker;
import com.vanilla.rpc.exception.RpcException;

public class GetFirstLoadBalance implements LoadBalance {

	@Override
	public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url) throws RpcException {
		// TODO Auto-generated method stub
		return invokers.get(0);
	}

}
