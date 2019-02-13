package com.ihuxu.xchatserver.client;

import java.util.Comparator;

public class NotLoggedClientComparator implements Comparator<NotLoggedClient> {
	@Override
	public int compare(NotLoggedClient o1, NotLoggedClient o2) {
		return o1.getConnectTime() <= o2.getConnectTime() ? 1 : -1;
	}
}