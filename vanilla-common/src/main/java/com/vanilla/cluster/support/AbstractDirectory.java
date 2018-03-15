package com.vanilla.cluster.support;

import java.util.List;

import org.apache.log4j.Logger;

import com.vanilla.cluster.Directory;
import com.vanilla.common.URL;

public abstract class AbstractDirectory implements Directory {

	private static final Logger logger = Logger.getLogger(AbstractDirectory.class);
	
	private final URL url;
	
	private volatile boolean destroyed = false;
	
	public AbstractDirectory(URL url){
		this.url = url;
	}
	
	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public void destroy() {
		destroyed = true;
	}


}
