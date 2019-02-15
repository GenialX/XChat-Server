package com.ihuxu.xchatserver.conf;

import java.util.concurrent.TimeUnit;

public interface ClientConfig {
    // Not logged Client Pool
    public static final int NOT_LOGGED_CLIENT_POOL_SET_CAPACITY = 16;
    public static final int NOT_LOGGED_CLIENT_POOL_INTERVAL = 1000; // Unit Milliseconds
}
