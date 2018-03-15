package com.vanilla.register.zookeeper;

import com.vanilla.common.URL;
import com.vanilla.register.Registry;
import com.vanilla.register.RegistryFactory;
import com.vanilla.remoting.zookeeper.ZookeeperTransporter;

public class ZookeeperRegisryFactory implements RegistryFactory {

    private ZookeeperTransporter zookeeperTransporter;

    public void setZookeeperTransporter(ZookeeperTransporter zookeeperTransporter) {
        this.zookeeperTransporter = zookeeperTransporter;
    }
    
	@Override
	public Registry getRegistry(URL url) {
		// TODO Auto-generated method stub
		return new ZookeeperRegistry(url,zookeeperTransporter);
	}

}
