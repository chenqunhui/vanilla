/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vanilla.remoting.channel;

import java.net.InetSocketAddress;

import com.vanilla.common.URL;
import com.vanilla.remoting.RemotingException;
import com.vanilla.remoting.exchange.Exchange;
import com.vanilla.remoting.exchange.Request;

/**
 * Channel. (API/SPI, Prototype, ThreadSafe)
 *
 *
 *
 * @see com.alibaba.dubbo.remoting.Client
 * @see com.alibaba.dubbo.remoting.Server#getChannels()
 * @see com.alibaba.dubbo.remoting.Server#getChannel(InetSocketAddress)
 */
public interface Channel{
	/**
	 * get url.
	 *
	 * @return url
	 */
	URL getUrl();


	/**
	 * get local address.
	 *
	 * @return local address.
	 */
	InetSocketAddress getLocalAddress();

	/**
	 * get remote address.
	 *
	 * @return remote address.
	 */
	InetSocketAddress getRemoteAddress();

	boolean doConnect();
	/**
	 * is connected.
	 *
	 * @return connected
	 */
	boolean isConnected();

	/**
	 * has attribute.
	 *
	 * @param key
	 *            key.
	 * @return has or has not.
	 */
	boolean hasAttribute(String key);

	/**
	 * get attribute.
	 *
	 * @param key
	 *            key.
	 * @return value.
	 */
	Object getAttribute(String key);

	/**
	 * set attribute.
	 *
	 * @param key
	 *            key.
	 * @param value
	 *            value.
	 */
	void setAttribute(String key, Object value);

	/**
	 * remove attribute.
	 *
	 * @param key
	 *            key.
	 */
	void removeAttribute(String key);

	/**
	 * close the channel.
	 */
	void close();

	/**
	 * is closed.
	 *
	 * @return closed
	 */
	boolean isClosed();

	void send(Object message,long timeout) throws RemotingException;
}