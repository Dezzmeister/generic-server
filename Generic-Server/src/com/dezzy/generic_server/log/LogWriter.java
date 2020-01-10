package com.dezzy.generic_server.log;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Inherited by any class that needs to print to a log, whether it's a log file or {@link System#out}.
 * 
 * @author Joe Desmond
 */
public abstract class LogWriter {
	/**
	 * The log
	 */
	protected final PrintStream log;
	
	/**
	 * Date format used for timestamps
	 */
	private final SimpleDateFormat timestampFormat = new SimpleDateFormat("hh:mm:ss");
	
	/**
	 * Constructs a log that prints to the given {@link PrintStream}.
	 * 
	 * @param _log PrintStream
	 */
	protected LogWriter(final PrintStream _log) {
		log = _log;
	}
	
	/**
	 * Calls {@link PrintStream#println(Object)} with a timestamp prepended.
	 * 
	 * @param object object to be printed
	 */
	protected void printlnWithTimestamp(final Object object) {
		log.println(getTimestamp() + object);
	}
	
	/**
	 * Calls {@link PrintStream#print(Object)} with a timestamp prepended.
	 * 
	 * @param object object to be printed
	 */
	protected void printWithTimestamp(final Object object) {
		log.print(getTimestamp() + object);
	}
	
	/**
	 * Returns a timestamp of the format "[hh:mm:ss]\t".
	 * 
	 * @return timestamp
	 */
	private String getTimestamp() {
		final String time = timestampFormat.format(new Date());
		return "[" + time + "]\t";
	}
}
