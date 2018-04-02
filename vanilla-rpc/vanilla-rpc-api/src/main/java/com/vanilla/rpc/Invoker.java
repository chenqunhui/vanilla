package com.vanilla.rpc;

import java.lang.reflect.Method;

import com.vanilla.common.URL;
import com.vanilla.rpc.exception.RpcException;

public interface Invoker<T> {


    /**
     * get service interface.
     *
     * @return service interface.
     */
    Class<T> getInterface();
    
    /**
     * provider url
     * @return
     */
    URL getUrl();

    /**
     * invoke.
     *
     * @param proxy
     * @param method
     * @param args
     * @return Object
     * @throws RpcException
     */
    Object invoke(Object proxy, Method method, Object[] args) throws RpcException;
}
