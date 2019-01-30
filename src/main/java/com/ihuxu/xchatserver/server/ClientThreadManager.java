package com.ihuxu.xchatserver.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.ihuxu.xchatserver.common.model.MessagePackage;

public class ClientThreadManager {
	
	private static HashMap<String, ClientThread> clientServerThreadHashMap = new HashMap<String, ClientThread>();
		
	public static ClientThread getClientThread(String key) throws Exception {
		if(ClientThreadManager.clientServerThreadHashMap.containsKey(key)) {
			return ClientThreadManager.clientServerThreadHashMap.get(key);
		} else {
			throw new Exception("There is not the ClientThread in the clientServerThreadHashMap variable.");
		}
	}
	
	public static ClientThread setClientThread(String key, ClientThread clientThread) {
		return ClientThreadManager.clientServerThreadHashMap.put(key, clientThread);
	}
	
	public static boolean addClientThread(String key, ClientThread clientThread) {
		if(!ClientThreadManager.clientServerThreadHashMap.containsKey(key)) {
			ClientThreadManager.clientServerThreadHashMap.put(key, clientThread);
			return true;
		}
		return false;
	}

	public static void removeClientThread(String key) {
		ClientThreadManager.clientServerThreadHashMap.remove(key);
	}

	public static int getClientThreadsCount() {
		return ClientThreadManager.clientServerThreadHashMap.size();
	}
	
	public static void cleanClientThreadsGarbage() {
		Iterator<Map.Entry<String, ClientThread>> iterator = ClientThreadManager.clientServerThreadHashMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, ClientThread> entry = iterator.next();
			String key = entry.getKey();
			try {
				if(ClientThreadManager.getClientThread(key).isListened() == false) {
					System.out.println("trash client thread key:" + key);
					ClientThreadManager.getClientThread(key).close();
					ClientThreadManager.removeClientThread(key);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
