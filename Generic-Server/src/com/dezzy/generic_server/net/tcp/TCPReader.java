package com.dezzy.generic_server.net.tcp;

import java.io.Closeable;
import java.io.ObjectInputStream;
import java.io.PrintStream;

import com.dezzy.generic_server.log.LogWriter;
import com.dezzy.generic_server.net.TCPDataObject;
import com.dezzy.generic_server.net.TCPMessageListener;

/**
 * Handles incoming data on a separate {@link Thread} for a {@link TCPServer}.
 * 
 * @author Joe Desmond
 */
final class TCPReader extends LogWriter implements Runnable, Closeable {
	/**
	 * Message listener callback
	 */
	private final TCPMessageListener messageListener;
	
	/**
	 * TCP Socket input stream
	 */
	private final ObjectInputStream inputStream;
	
	/**
	 * True while the TCPReader is still accepting data
	 */
	private volatile boolean isOpen = true;
	
	/**
	 * Creates a TCPReader with the given output log, message listener callback, and {@link java.net.Socket Socket} input stream.
	 * 
	 * @param log print log
	 * @param _messageListener handler for received data
	 * @param _inputStream connection input
 	 */
	TCPReader(final PrintStream log, final TCPMessageListener _messageListener, final ObjectInputStream _inputStream) {
		super(log);
		messageListener = _messageListener;
		inputStream = _inputStream;
	}
	
	@Override
	public void run() {
		while (isOpen) {
			try {
				final TCPDataObject data = (TCPDataObject) inputStream.readObject();
				messageListener.onMessageReceived(data);
			} catch (Exception e) {
				e.printStackTrace(log);
			}
		}
	}
	
	@Override
	public void close() {
		isOpen = false;
	}
}
