package com.vanilla.register;

import com.vanilla.common.URL;

public interface Registry {

	void register(URL url);
	
	void unregister(URL url);
	
	void subscribe(URL url,NotifyListener listener);
	
	void unsubscribe(URL url,NotifyListener listener);
	
}
