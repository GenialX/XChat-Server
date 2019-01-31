package com.ihuxu.xchatserver.client;

import java.util.concurrent.LinkedBlockingQueue;

import com.ihuxu.xchatserver.conf.ClientConfig;

/**
 * ”未登录客户端“的线程池.
 * 
 * 单利模式（内部类方式 线程安全）
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
    
    /**
     * 未登录的客户端实例线程安全队列.
     */
    private static LinkedBlockingQueue<NotLoggedClient> clients = new LinkedBlockingQueue<NotLoggedClient>(ClientConfig.CLIENTS_QUEUE_CAPACITY);

    private NotLoggedClientPool() {}

    public static NotLoggedClientPool getInstance() {
        return LazyHolder.instance;
    }
    
    @SuppressWarnings("unused")
    /**
     * 检索并删除此队列的头，如有必要，等待元素可用.
     * 
     * @return
     * @throws InterruptedException
     */
	private NotLoggedClient take() throws InterruptedException {
        return clients.take();
    }
    
    /**
     * 检索但不删除此队列的头，如果此队列为空，则返回 null .
     * 
     * @return
     */
    private NotLoggedClient peek() {
    	return clients.peek();
    }
    
    /**
     * 如果可以在不超过队列的容量的情况下立即将其指定的元素插入到队列的尾部，
     * 如果队列已满，则返回 true和 false .
     * 
     * @param client
     * @return
     * @throws InterruptedException
     */
    public boolean offer(NotLoggedClient client) throws InterruptedException {
        return clients.offer(client);
    }
    
    public void run() {
        NotLoggedClient client;
        while (true) {
            client = peek();
            // there is not client in pool
            if (client == null) {
            	try {
					NotLoggedClientPool.sleep(ClientConfig.CLIENTS_POOL_INTERVAL);
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            } 
            // there is client in pool
            if (client.getStatus() != ClientStatusFSM.CLIENT_STATUS_LEVEL_0_NOT_LOGGED) {
            	ClientStatusFSM.getInstance().transfer(client);
            }
        }
    }
}
