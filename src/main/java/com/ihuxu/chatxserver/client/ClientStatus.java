package com.ihuxu.chatxserver.client;

import java.util.ArrayList;

/**
 * 客户端状态类.
 * 
 * 客户端状态机的描述元素。
 * 
 * @author GenialX
 */
public class ClientStatus {
	public int status = -1;
	public ClientStatus parent = null;
	public ArrayList<ClientStatus> next;
	
	public ClientStatus() {}

	public ClientStatus(int status) {
		this.status = status;
		this.parent = null;
	}

	public boolean addNext(ClientStatus e) {
		return next.add(e);
	}
}
