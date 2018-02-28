package com.vanilla.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


/**
 * 获取IP相关信息实用类
 * 
 */
public class IpUtils {

	/**
	 * 获取本机所有IP地址
	 * 
	 * @return
	 */
	public static List<String> getAllLocalHostIP() {
		final List<String> ipList = new ArrayList<String>();
		try {
			final Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			if (null != netInterfaces) { // 如果在此机器可以找到网络接口
				while (netInterfaces.hasMoreElements()) {
					NetworkInterface ni = netInterfaces.nextElement();
					Enumeration<InetAddress> addr = ni.getInetAddresses();
					while (addr.hasMoreElements()) {
						ip = addr.nextElement();
						if (null != ip && ip instanceof Inet4Address) {
							ipList.add(ip.getHostAddress());
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipList;
	}

	/**
	 * 获取本机IP地址
	 * 
	 * @return
	 */
	public static String getLocalHostIP() {
		String ipAddr = "0.0.0.0";
		final List<String> ipList = getAllLocalHostIP();
		for (String ip : ipList) {
			if (!ip.equalsIgnoreCase("127.0.0.1")) {
				ipAddr = ip;
				break;
			}
		}
		return ipAddr;
	}


	/**
	 * 获取物理网卡地址
	 * 
	 * @param host
	 *            ----> IP地址或主机名（IP不能为：127.0.0.1）
	 * @return
	 */
	public static String getMacAddress(String host) {
		String mac = "";
		final StringBuffer sb = new StringBuffer();

		try {
			final NetworkInterface ni = NetworkInterface
					.getByInetAddress(InetAddress.getByName(host));
			if (null != ni) {
				byte[] macs = ni.getHardwareAddress();
				for (int i = 0; i < macs.length; i++) {
					mac = Integer.toHexString(macs[i] & 0xFF);

					if (mac.length() == 1) {
						mac = '0' + mac;
					}

					sb.append(mac + "-");
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		if (sb.length() > 0) {
			mac = sb.toString();
			mac = mac.substring(0, mac.length() - 1);
		}
		return mac;
	}
	
	
	public static String getHostAndPort(InetSocketAddress address){
		if(null == address){
			return null;
		}
		return new StringBuilder().append(address.getHostName()).append(":").append(address.getPort()).toString();
	}
	
	
	public  static void main(String[] args){
		System.out.println(IpUtils.getLocalHostIP());
	}
}
