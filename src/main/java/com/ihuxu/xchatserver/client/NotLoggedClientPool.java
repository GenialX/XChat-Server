package com.ihuxu.xchatserver.client;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.ihuxu.xchatserver.conf.ClientConfig;

/**
 * ”未登录客户端“的线程池.
 * 
 * 单利模式（内部静态类方式 线程安全）
 * 
 * 功能：
 * 1. 提供“未登录客户端”线程的增加与删除操作
 * 2. 定时监控“客户端”的状态，如果状态非“未登录”状态，则将其移除，并添加到客户端状态机进行处理
 * 
 * @author GenialX
 */
public class NotLoggedClientPool extends Thread {
    private static class LazyHolder {
        private final static NotLoggedClientPool instance = new NotLoggedClientPool();
    }

    private NotLoggedClientPool() {}

    public static NotLoggedClientPool getInstance() {
        return LazyHolder.instance;
    }
    
    /**
     * 未登录的客户端容器.
     */
    private ConcurrentSkipListSet<NotLoggedClient> clients = new ConcurrentSkipListSet<NotLoggedClient>(new NotLoggedClientComparator());
    
    /**
     * 原子性计数器.
     */
    private AtomicInteger clientCount = new AtomicInteger();
    
    /**
     * 未登录客户端阈值.
     */
    private final int clientThreshold = ClientConfig.NOT_LOGGED_CLIENT_POOL_SET_CAPACITY;
    
    /**
     * 增加未登录客户端.
     * 
     * 确保当前的未登录客户端容器不会超过指定的阈值。
     * 
     * @param client
     * @return boolean
     * @throws Exception Throw exception while Server is Full.
     */
    public boolean add(NotLoggedClient client) throws Exception {
    	if (clientCount.incrementAndGet() > clientThreshold) {
    		clientCount.decrementAndGet();
    		throw new Exception("Server is full, try later.");
    	} else {
    		return clients.add(client);
    	}
    }
    
    /**
     * 删除指定客户端.
     * 
     * @param client
     * @return boolean
     */
    public boolean remove(NotLoggedClient client) {
        boolean result = clients.remove(client);
        clientCount.decrementAndGet();
        return result;
    }
    
    public void run() {
        NotLoggedClient client;
        Iterator<NotLoggedClient> it = clients.iterator();
        while (true) {
            // Traverse the clients
            while (it.hasNext()) {
                client = it.next();
                if (client.getStatus() != ClientStatusFSM.CLIENT_STATUS_LEVEL_0_NOT_LOGGED) {
                    ClientStatusFSM.getInstance().transfer(client);
                    remove(client);
                }
            }
            try {
                Thread.sleep(ClientConfig.NOT_LOGGED_CLIENT_POOL_INTERVAL);
            } catch (InterruptedException e) {
                Logger.getRootLogger().warn(e.getStackTrace());
            }
        }
    }
}