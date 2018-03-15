package com.vanilla.register.integration;

import java.util.List;
import java.util.Set;

import com.vanilla.cluster.support.AbstractDirectory;
import com.vanilla.common.URL;
import com.vanilla.register.NotifyListener;

public class RegistryDirectory extends AbstractDirectory implements NotifyListener{
	
	//可用server url
	private volatile Set<URL> cachedServerUrls;
	
	public Set<URL> getCachedServerUrls() {
		return cachedServerUrls;
	}

	public void setCachedServerUrls(Set<URL> cachedServerUrls) {
		this.cachedServerUrls = cachedServerUrls;
	}

	public RegistryDirectory(URL url) {
		super(url);
	}

	@Override
	public List<URL> list(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void notify(List<URL> urls) {
		
		
	}

}
