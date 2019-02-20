package com.ihuxu.xchatserver.client;

import java.net.Socket;

import org.apache.log4j.Logger;

import com.ihuxu.xchatserver.conf.ClientConfig;

/**
 * 客户端线程.
 * 
 * 功能：
 *  1. 与”客户端“进行登录校验
 *  2. 更新当前“客户端”的状态
 *  
 * @author GenialX
 */
public class NotLoggedClient extends AbstractClient {

	public NotLoggedClient(Socket socket) {
		super(socket);
		setStatus(ClientStatusFSM.CLIENT_STATUS_LEVEL_0_NOT_LOGGED);
	}

	@Override
	public void run() {
	    while (true) {
	        byte[] message = getNextMessage();
	        if (message == null) {
	            try {
                    Thread.sleep(ClientConfig.NOT_LOGGED_CLIENT_INTERVAL);
                } catch (InterruptedException e) {
                    Logger.getRootLogger().warn(e.getStackTrace());
                }
	            continue;
	        }
	    }
	}
}
