package com.vanilla.register;

import com.vanilla.common.URL;

public interface Registry {

	URL getUrl();
	
	void setUrl(URL url);
	
	void register(URL url);
	
	void unregister(URL url);
	
	void subscribe(URL url,NotifyListener listener);
	
	void unsubscribe(URL url,NotifyListener listener);
	
}
