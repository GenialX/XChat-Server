package com.ihuxu.chatxserver.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    public final static String CLIENT_STATUS_FSM = "[{\"status\":0, \"desc\":\"未登录\",\"parent\":-1, \"next\":[{\"status\":72057594037927936, \"desc\":\"已登录\", \"parent\":0, \"next\":[{\"status\": 144115188075855872, \"desc\":\"登录失败\",\"parent\":0, \"next\":[]},{\"status\":144115188075855873, \"desc\":\"断开\",\"parent\":0, \"next\":[]}]},{\"status\": 72057594037927937, \"desc\":\"登录失败\",\"parent\":0, \"next\":[]},{\"status\": 72057594037927938, \"desc\":\"断开\",\"parent\":0, \"next\":[]}]}]";
    // For Level 0
	public final static long CLIENT_STATUS_LEVEL_0_NOT_LOGGED = 0;
	// For Level 1
	public final static long CLIENT_STATUS_LEVEL_1_LOGGED = 16777216;
	public final static long CLIENT_STATUS_LEVEL_1_LOGIN_FAILED = 16777217;
	public final static long CLIENT_STATUS_LEVEL_1_DISCONNECTED = 16777218;
	// For Level 2
	public final static long CLIENT_STATUS_LEVEL_2_LOGGED_OUT = 33554432;
	public final static long CLIENT_STATUS_LEVEL_2_DISCONNECTED = 33554433;
	
	private HashMap<Integer, ClientStatus> fsmHashMap = new HashMap<Integer, ClientStatus>();
	
	private ClientStatusFSM() {
		this.build();
	}
	
	/*
	 * 构建状态机 —— 树型数据结构.
	 */
	private void build() {
		JsonObject fsmObject = new JsonParser().parse(ClientStatusFSM.CLIENT_STATUS_FSM).getAsJsonObject();
		// parent node
		ClientStatus parent = new ClientStatus(-1);
		this.fsmHashMap.put(-1, parent);
		// current node
		ClientStatus current = new ClientStatus(fsmObject.get("status").getAsInt());
		this.fsmHashMap.put(current.status, current);
		// next nodes
		JsonObject nextNodes = fsmObject.getAsJsonObject("next");
		// search
		buildDFS(current, parent, nextNodes);
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
	private void buildDFS(ClientStatus current, ClientStatus parent, JsonObject nextNodes) {
		parent.addNext(current);
		current.parent = parent;
		if (nextNodes.isJsonArray() && nextNodes.getAsJsonArray().size() > 0) {
			Iterator<JsonElement> it = nextNodes.getAsJsonArray().iterator();
			while (it.hasNext()) {
				JsonObject nextObject = it.next().getAsJsonObject();
				ClientStatus next = new ClientStatus(nextObject.get("status").getAsInt());
				this.fsmHashMap.put(next.status, next);
				JsonObject nextNextNodes = nextObject.get("next").getAsJsonObject();
				buildDFS(next, current, nextNextNodes);
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
	private int getDepth(int status) {
		return status >> 24;
	}

	/**
	 * BFS遍历树型数据结构.
	 * 
	 * 目前仅供测试使用。
	 */
	public void traversingBFS() {
		ArrayList<ClientStatus> queue = new ArrayList<ClientStatus>();
		queue.add(this.fsmHashMap.get(-1));
		while (queue.size() > 0) {
			ClientStatus node = queue.get(0);
			System.out.println("Current Depth:" + getDepth(node.status));
			System.out.println("Status:" + node.status);
			while (this.fsmHashMap.get(node.status).next.isEmpty() == false) {
				 Iterator<ClientStatus> it = this.fsmHashMap.get(node.status).next.iterator();
				 while (it.hasNext()) {
					 queue.add(it.next());
				 }
			}
		}
	}
	
	public static ClientStatusFSM getInstance() {
		return LazyHolder.instance;
	}

	private static class LazyHolder {
		private final static ClientStatusFSM instance = new ClientStatusFSM();
	}
}