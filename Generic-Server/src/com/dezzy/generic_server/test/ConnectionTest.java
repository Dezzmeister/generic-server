package com.dezzy.generic_server.test;

import java.io.IOException;

import com.dezzy.generic_server.net.TCPDataObject;
import com.dezzy.generic_server.net.TCPMessageListener;
import com.dezzy.generic_server.net.tcp.TCPServer;

public class ConnectionTest {

	public static void main(String[] args) throws IOException {
		final TCPMessageListener msgCallback = System.out::println;
		final TCPServer server = new TCPServer(System.out, msgCallback, 20777);
		final Thread serverThread = new Thread(server, "Generic Server Test Thread");
		serverThread.start();
		server.send(new TCPDataObject("test"));
	}
}
