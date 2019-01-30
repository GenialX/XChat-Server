package com.ihuxu.xchatserver.controller;

import com.ihuxu.xchatserver.server.Server;

public class Bootstrap {
    public static void main(String []args) {
        Server server = new Server();
        server.start();
    }
}