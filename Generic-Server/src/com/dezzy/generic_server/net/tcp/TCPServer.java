package com.dezzy.generic_server.net.tcp;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.dezzy.generic_server.log.LogWriter;
import com.dezzy.generic_server.net.TCPDataObject;
import com.dezzy.generic_server.net.TCPMessageListener;

/**
 * A single TCP connection.
 * 
 * @author Joe Desmond
 */
public final class TCPServer extends LogWriter implements Runnable, Closeable {
	/**
	 * TCP server socket
	 */
	private final ServerSocket serverSocket;
	
	/**
	 * Handles messages received from client
	 */
	private final TCPMessageListener messageListener;
	
	/**
	 * TCP port
	 */
	private final int port;
	
	/**
	 * True while the TCP server is still open and accepting connections
	 */
	private volatile boolean isOpen = true;
	
	/**
	 * Queue of messages to be sent to the client
	 */
	private final AbstractQueue<TCPDataObject> messageQueue = new ConcurrentLinkedQueue<TCPDataObject>();
	
	/**
	 * Creates a TCPServer and opens the server socket, but does not start the server. Connections will only be accepted when 
	 * {@link #run()} is called.
	 * 
	 * @param log output log
	 * @param _messageListener callback function to handle received messages
	 * @param _port TCP port to listen for connections on
	 * @throws IOException if there is a problem opening the {@link ServerSocket}
	 */
	public TCPServer(final PrintStream log, final TCPMessageListener _messageListener, final int _port) throws IOException {
		super(log);
		messageListener = _messageListener;
		port = _port;
		
		serverSocket = new ServerSocket(port);
	}
	
	@Override
	public void run() {		
		while (isOpen || !messageQueue.isEmpty()) {
			try (final Socket socket = serverSocket.accept();
				final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				final TCPReader reader = new TCPReader(log, messageListener, inputStream)) {
				
				final Thread readerThread = new Thread(reader, "Generic Server TCP Reader Thread");
				readerThread.start();
				
				final InetAddress address = socket.getInetAddress();
				printlnWithTimestamp("Connection accepted from " + address.getHostName());
				
				while (!messageQueue.isEmpty()) {
					final TCPDataObject data = messageQueue.remove();
					
					outputStream.writeObject(data);
					outputStream.flush();
				}				
			} catch (IOException e) {
				e.printStackTrace(log);
			}
		}
	}
	
	/**
	 * Puts a message onto the internal queue to be sent to the client.
	 * 
	 * @param data message
	 */
	public void send(final TCPDataObject data) {
		messageQueue.add(data);
	}
	
	@Override
	public void close() {
		isOpen = false;
	}
}
