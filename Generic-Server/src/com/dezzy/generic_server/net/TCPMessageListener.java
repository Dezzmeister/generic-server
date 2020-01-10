package com.dezzy.generic_server.net;

/**
 * Implemented by classes that should be notified when a TCPServer receives new data.
 * 
 * @author Joe Desmond
 */
public interface TCPMessageListener {
	
	/**
	 * Receives a message from a TCPServer. The message is deserialized into a {@link TCPDataObject} before being passed in.
	 * 
	 * @param data received data
	 */
	void onMessageReceived(final TCPDataObject data);
}
