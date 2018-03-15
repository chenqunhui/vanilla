package com.vanilla.monitor.support;

import com.vanilla.common.URL;
import com.vanilla.register.Registry;
import com.vanilla.register.RegistryFactory;
import com.vanilla.register.zookeeper.ZookeeperRegistry;
import com.vanilla.remoting.zookeeper.curator.CuratorZookeeperTransporter;

public class DefaultRegistryFactory implements RegistryFactory {

	@Override
	public Registry getRegistry(URL url) {
		String protocol = url.getProtocol();
		if("zookeeper".equalsIgnoreCase(protocol)){
			return new ZookeeperRegistry(url ,new CuratorZookeeperTransporter());
		}
		return null;
	}
	

}
