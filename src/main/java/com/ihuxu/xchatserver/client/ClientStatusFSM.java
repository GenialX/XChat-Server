package com.ihuxu.xchatserver.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 客户端状态机（单例模式）.
 * 
 * 功能：
 *  1. 根据 ClientStatusFSM.CLIENT_STATUS_FSM 以DFS方式构造树型数据结构。
 * 
 * @author GenialX
 */

public class ClientStatusFSM {
    // Client FSM
    public final static String CLIENT_STATUS_FSM = "{\"status\":0, \"desc\":\"未登录\",\"parent\":-1, \"next\":[{\"status\":16777216, \"desc\":\"已登录\", \"parent\":0, \"next\":[{\"status\": 33554432, \"desc\":\"登录失败\",\"parent\":0, \"next\":[]},{\"status\":33554433, \"desc\":\"断开\",\"parent\":0, \"next\":[]}]},{\"status\": 16777217, \"desc\":\"登录失败\",\"parent\":0, \"next\":[]},{\"status\": 16777218, \"desc\":\"断开\",\"parent\":0, \"next\":[]}]}";
    // For Level 0
	public final static int CLIENT_STATUS_LEVEL_0_NOT_LOGGED = 0;
	// For Level 1
	public final static int CLIENT_STATUS_LEVEL_1_LOGGED = 16777216;
	public final static int CLIENT_STATUS_LEVEL_1_LOGIN_FAILED = 16777217;
	public final static int CLIENT_STATUS_LEVEL_1_DISCONNECTED = 16777218;
	// For Level 2
	public final static int CLIENT_STATUS_LEVEL_2_LOGGED_OUT = 33554432;
	public final static int CLIENT_STATUS_LEVEL_2_DISCONNECTED = 33554433;
	
	private HashMap<Integer, ClientStatus> fsmHashMap = new HashMap<Integer, ClientStatus>();
	
	private ClientStatusFSM() {
		buildFSM();
	}
	
	/*
	 * 构建状态机 —— 树型数据结构.
	 */
	private void buildFSM() {
		JsonObject fsmObject = new JsonParser().parse(ClientStatusFSM.CLIENT_STATUS_FSM).getAsJsonObject();
		// parent node
		ClientStatus parent = new ClientStatus(-1);
		fsmHashMap.put(-1, parent);
		// current node
		ClientStatus current = new ClientStatus(fsmObject.get("status").getAsInt());
		fsmHashMap.put(current.status, current);
		// next nodes
		JsonArray nextNodes = fsmObject.getAsJsonArray("next");
		// search
		buildDFS(parent, current, nextNodes);
    }
	
	/**
	 * 递归构建状态机树型结构.
	 * 
	 * 1. 将当前节点追加到父亲节点的next中
	 * 2. 更改当前节点的parent为父亲节点
	 * 3. 遍历当前节点的next，以此往复递归搜索
	 * 
	 * @param current
	 * @param parent
	 * @param nextNodes
	 */
	private void buildDFS(ClientStatus parent, ClientStatus current, JsonArray nextNodes) {
		parent.addNext(current);
		current.parent = parent;
		if (nextNodes.size() > 0) {
			Iterator<JsonElement> it = nextNodes.iterator();
			while (it.hasNext()) {
				JsonObject nextObject = it.next().getAsJsonObject();
				ClientStatus next = new ClientStatus(nextObject.get("status").getAsInt());
				fsmHashMap.put(next.status, next);
				JsonArray nextNextNodes = nextObject.getAsJsonArray("next");
				buildDFS(current, next, nextNextNodes);
			}
		}
	}
	
	/**
	 * 获取当前节点的深度，深度从0开始累增.
	 * 
	 * @param status
	 * 
	 * @return Integer
	 */
	public int getDepth(int status) {
		return status >> 24;
	}
	
	public int generateByDepth(int depth, int number) {
		depth = depth << 24;
		return depth | (number & (Integer.MAX_VALUE >> 8));
	}

	/**
	 * BFS遍历树型数据结构.
	 * 
	 * 目前仅供测试使用。
	 */
	public ArrayList<Integer> traversingBFS() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<ClientStatus> queue = new ArrayList<ClientStatus>();
		queue.add(fsmHashMap.get(ClientStatusFSM.CLIENT_STATUS_LEVEL_0_NOT_LOGGED));
		while (queue.size() > 0 && queue.get(0) != null) {
			ClientStatus node = queue.get(0);
			queue.remove(0);
			result.add(node.status);
			if (fsmHashMap.get(node.status).next.isEmpty() == false) {
				 Iterator<ClientStatus> it = fsmHashMap.get(node.status).next.iterator();
				 while (it.hasNext()) {
					 queue.add(it.next());
				 }
			}
		}
		return result;
	}
	
	public static ClientStatusFSM getInstance() {
		return LazyHolder.instance;
	}

	private static class LazyHolder {
		private final static ClientStatusFSM instance = new ClientStatusFSM();
	}
}