package com.ihuxu.xchatserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.ihuxu.xchatserver.client.NotLoggedClient;
import com.ihuxu.xchatserver.client.NotLoggedClientPool;

public class Server {

	private ServerSocket serverSocket;

	public Server() {}

	public void start() {
		Logger.getRootLogger().info("Server started...");
		/** CRONTAB **/
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
			        Logger.getRootLogger().info("Did recieve one new socket");
			        NotLoggedClient client = new NotLoggedClient(socket);
			        try {
			            if (NotLoggedClientPool.getInstance().add(client)) {
			                client.start();
			            } else {
			                Logger.getRootLogger().warn("The client has been added.");
			            }
			        } catch (Exception e) {
			            // Server is full, try later.
			            Logger.getRootLogger().warn(e.getStackTrace());
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
