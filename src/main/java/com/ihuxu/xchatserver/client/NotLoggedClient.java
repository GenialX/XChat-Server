package com.ihuxu.xchatserver.client;

import java.net.Socket;

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
		// TODO Auto-generated method stub
	}
}
