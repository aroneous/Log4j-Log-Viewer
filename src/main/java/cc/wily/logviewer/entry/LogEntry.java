package cc.wily.logviewer.entry;

import java.net.InetAddress;

import org.apache.log4j.spi.LoggingEvent;

/**
 * <p>
 * Encapsulates data related to a log entry, including the log entry itself and
 * its source.
 * </p>
 * 
 * <p>
 * TODO: Make this logging framework-agnostic.
 * </p>
 * 
 * @author aron
 */
public class LogEntry {
	private LoggingEvent entry;
	private InetAddress host;
	private int port;
	
	public LogEntry() {
	}
	
	public LogEntry(LoggingEvent entry, InetAddress host, int port) {
		this.entry = entry;
		this.host = host;
		this.port = port;
	}

	public void setEntry(LoggingEvent entry) {
		this.entry = entry;
	}

	public LoggingEvent getEntry() {
		return entry;
	}

	public InetAddress getHost() {
		return host;
	}

	public void setHost(InetAddress host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
