package com.ihuxu.xchatserver.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class AbstractClient extends Thread implements Client {
	private Socket socket;
	@SuppressWarnings("unused")
	private InputStream input;
	@SuppressWarnings("unused")
	private OutputStream output;
	private int status = -1;
	@SuppressWarnings("unused")
	private long connectTime = System.currentTimeMillis();
	
	public AbstractClient(Socket socket) {
		this.socket = socket;
	}
	
	public InputStream getInput() throws IOException {
		return socket.getInputStream();
	}
	
	public OutputStream getOutput() throws IOException {
		return socket.getOutputStream();
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public long getConnectTime() {
		return connectTime;
	}
	
	abstract public void run();
}
