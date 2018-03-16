package com.vanilla.register.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.vanilla.common.Constants;
import com.vanilla.common.URL;
import com.vanilla.common.utils.StringUtils;
import com.vanilla.register.NotifyListener;
import com.vanilla.register.support.FailoverRegistry;
import com.vanilla.register.support.RegistryException;
import com.vanilla.remoting.zookeeper.ChildListener;
import com.vanilla.remoting.zookeeper.StateListener;
import com.vanilla.remoting.zookeeper.ZookeeperClient;
import com.vanilla.remoting.zookeeper.ZookeeperTransporter;

public class ZookeeperRegistry extends FailoverRegistry {

	private final static Logger logger = Logger.getLogger(ZookeeperRegistry.class);
	private static final String ROOT = "/monitor";
	private final ConcurrentMap<URL, ConcurrentMap<NotifyListener, ChildListener>> zkListeners = new ConcurrentHashMap<URL, ConcurrentMap<NotifyListener, ChildListener>>();

	private ZookeeperClient zkClient;

	public ZookeeperRegistry(URL url, ZookeeperTransporter zookeeperTransporter) {
		super(url);
		zkClient = zookeeperTransporter.connect(url);
		zkClient.addStateListener(new StateListener() {
			public void stateChanged(int state) {
				if (state == RECONNECTED) {
					try {
						// recover();
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		});

	}

	@Override
	public void register(URL url) {
		try {
			zkClient.create(getPath(url), true);
		} catch (Throwable e) {
			throw new RegistryException(
					"Failed to register " + url + " to zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
		}

	}

	@Override
	public void unregister(URL url) {
		try {
			zkClient.delete(getPath(url));
		} catch (Throwable e) {
			throw new RegistryException(
					"Failed to unregister " + url + " to zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
		}

	}

	@Override
	public void subscribe(URL url, NotifyListener listener) {
		super.subscribe(url, listener);
		try {
			ConcurrentMap<NotifyListener, ChildListener> listeners = zkListeners.get(url);
			if (listeners == null) {
				zkListeners.putIfAbsent(url, new ConcurrentHashMap<NotifyListener, ChildListener>());
				listeners = zkListeners.get(url);
			}
			ChildListener zkListener = listeners.get(listener);
			if (zkListener == null) {
				listeners.putIfAbsent(listener, new ChildListener() {
					public void childChanged(String parentPath, List<String> currentChilds) {
						// ZookeeperRegistry.this.notify(url, listener,
						// toUrlsWithEmpty(url, parentPath, currentChilds));
						System.out.println("notify childChanged" + JSON.toJSONString(currentChilds));
						List<URL> urls = new ArrayList<URL>();
						currentChilds.forEach(t -> validateAndAdd(urls,t));
						listener.notify(urls);
					}
				});
				zkListener = listeners.get(listener);
			}
			zkClient.create(getSubcribePath(url), false);
			List<String> children = zkClient.addChildListener(getSubcribePath(url), zkListener);
			List<URL> urls = new ArrayList<URL>();
			children.forEach(t -> validateAndAdd(urls,t));
			listener.notify(urls);

		} catch (Throwable e) {
			throw new RegistryException(
					"Failed to unregister " + url + " to zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
		}

		//
	}

	@Override
	public void unsubscribe(URL url, NotifyListener listener) {
		super.unsubscribe(url, listener);
		ConcurrentMap<NotifyListener, ChildListener> listeners = zkListeners.get(url);
		if (listeners != null) {
			ChildListener zkListener = listeners.get(listener);
			if (zkListener != null) {
				zkClient.removeChildListener(getSubcribePath(url), zkListener);
			}
		}
	}

	private String getSubcribePath(URL url) {
		return ROOT;
	}

	private String getPath(URL url) {
		return ROOT + Constants.PATH_SEPARATOR + URL.encode(url.toFullString());
	}
     
	private void validateAndAdd(List<URL> urls,String child){
		try{
			String str = URL.decode(child);
			if(StringUtils.isEmpty(str)){
				return;
			}
			URL url = URL.valueOf(str);
			if(StringUtils.isEmpty(url.getHost())){
				logger.error("Child "+child +"has no host,cannot be used,ignor it!");
				return;
			}
			urls.add(URL.valueOf(URL.decode(child)));
		}catch(Exception e){
			logger.error("child "+child +"cannot be used,ignor it!");
		}
	}
}
