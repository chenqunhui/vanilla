package com.vanilla.register.support;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.vanilla.common.URL;
import com.vanilla.register.Registry;
import com.vanilla.register.RegistryFactory;

public abstract class AbstractRegistryFactory implements RegistryFactory{

	private static final ReentrantLock LOCK = new ReentrantLock();
	
	private static ConcurrentHashMap<String,Registry> REGISTRIES = new ConcurrentHashMap<String,Registry>();
	@Override
	public Registry getRegistry(URL url) {
		// TODO Auto-generated method stub
		LOCK.lock();
		Registry exists;
		try{
			String key = url.toServiceString();
			exists = REGISTRIES.get(key);
			if(null == exists){
				REGISTRIES.putIfAbsent(key, createRegistry(url));
				exists = REGISTRIES.get(key);
			}
		}finally{
			LOCK.unlock();
		}
		return exists;
	}
	
	protected abstract Registry createRegistry(URL url);
}
