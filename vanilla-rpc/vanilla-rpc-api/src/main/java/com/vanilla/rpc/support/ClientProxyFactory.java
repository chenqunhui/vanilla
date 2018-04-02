package com.vanilla.rpc.support;

import com.vanilla.common.URL;

public interface ClientProxyFactory<Invoker> {

	public Invoker createInvoker(URL url);
}
