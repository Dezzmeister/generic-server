package com.dezzy.generic_server.net;

import java.io.Serializable;

public final class TCPDataObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2001571904579886054L;
	
	private final String id;
	
	public TCPDataObject(final String _id) {
		id = _id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
