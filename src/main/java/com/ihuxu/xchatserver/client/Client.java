package com.ihuxu.xchatserver.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Client {
	public InputStream getInput() throws IOException;
	public OutputStream getOutput() throws IOException;
	public void setStatus(int status);
	public int getStatus();
}
