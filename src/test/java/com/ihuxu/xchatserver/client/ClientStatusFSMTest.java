package com.ihuxu.xchatserver.client;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class ClientStatusFSMTest {
    @Test public void traversingBFSBuild() {
    	ArrayList<Integer> result = ClientStatusFSM.getInstance().traversingBFS();
    	ArrayList<Integer> actual = new ArrayList<Integer>();

    	actual.add(ClientStatusFSM.CLIENT_STATUS_LEVEL_0_NOT_LOGGED);

    	actual.add(ClientStatusFSM.CLIENT_STATUS_LEVEL_1_LOGGED);
    	actual.add(ClientStatusFSM.CLIENT_STATUS_LEVEL_1_LOGIN_FAILED);
    	actual.add(ClientStatusFSM.CLIENT_STATUS_LEVEL_1_DISCONNECTED);

    	actual.add(ClientStatusFSM.CLIENT_STATUS_LEVEL_2_LOGGED_OUT);
    	actual.add(ClientStatusFSM.CLIENT_STATUS_LEVEL_2_DISCONNECTED);

    	System.out.println(ClientStatusFSM.CLIENT_STATUS_FSM);
    	assertEquals("traversingBFS failed. The tree data is:" + ClientStatusFSM.CLIENT_STATUS_FSM, actual, result);
    }
    
    @Test public void generateDepth() {
    	int depth = 0, number = 0, answer = 0;
    	answer = ClientStatusFSM.getInstance().generateByDepth(depth, number);
    	assertEquals("Not equal while generating with depth " + depth
    			+ ", number " + number, 0, answer);

    	depth = 1;
    	answer = ClientStatusFSM.getInstance().generateByDepth(depth, number);
    	assertEquals("Not equal while generating with depth " + depth
    			+ ", number " + number, 16777216, answer);

    	depth = 2;
    	answer = ClientStatusFSM.getInstance().generateByDepth(depth, number);
    	assertEquals("Not equal while generating with depth " + depth
    			+ ", number " + number, 33554432, answer);
    }
}