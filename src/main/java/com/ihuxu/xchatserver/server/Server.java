package com.ihuxu.xchatserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.ihuxu.xchatserver.client.NotLoggedClient;
import com.ihuxu.xchatserver.client.NotLoggedClientPool;

public class Server {

	private ServerSocket serverSocket;

	public Server() {}

	public void start() {
		System.out.println("Server starting...");
		/** crontab **/
		ServerCrontab.checkClientSocket();

		/** listening **/
		try {
			serverSocket = new ServerSocket(1722);
			boolean go = true;
			while (go) {
			    try {
			        /** listening to the new socket **/
			        Socket socket = serverSocket.accept();

			        /** client thread **/
			        System.out.println("the recieved the serverSocket.");
			        NotLoggedClient client = new NotLoggedClient(socket);
			        try {
			            NotLoggedClientPool.getInstance().add(client);
			        } catch (Exception e) {
			            // Server is full, try later.
			            e.printStackTrace();
			            client = null;
			        }
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
	}

	public void restart() {
	}

}
