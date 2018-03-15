package com.vanilla.register.support;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.vanilla.common.URL;
import com.vanilla.common.utils.ConcurrentHashSet;
import com.vanilla.register.NotifyListener;
import com.vanilla.register.Registry;

public abstract class AbstractRegistry implements Registry {

	private URL registryUrl;
	
	private final ConcurrentMap<URL, Set<NotifyListener>> subscribed = new ConcurrentHashMap<URL, Set<NotifyListener>>();
	
    public URL getUrl() {
        return registryUrl;
    }

    protected void setUrl(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("registry url == null");
        }
        this.registryUrl = url;
    }
    
	public AbstractRegistry(URL url){
		setUrl(url);
	}
	
	public void subscribe(URL url,NotifyListener listener){
		if (url == null) {
            throw new IllegalArgumentException("unsubscribe url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("unsubscribe listener == null");
        }
		Set<NotifyListener> set = subscribed.get(url);
		if(null == set){
			subscribed.putIfAbsent(url, new ConcurrentHashSet<NotifyListener>());
			set = subscribed.get(url);
		}
		set.add(listener);
	}
	
	
	public void unsubscribe(URL url,NotifyListener listener){
		if (url == null) {
            throw new IllegalArgumentException("unsubscribe url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("unsubscribe listener == null");
        }
		Set<NotifyListener> set = subscribed.get(url);
		if(null == set){
			return;
		}
		set.remove(listener);
	}

}
