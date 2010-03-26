package cc.wily.logviewer.connections;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import cc.wily.logviewer.reader.SocketLogReader;

public class SocketListener implements Runnable {
	private static Logger LOG = Logger.getLogger(SocketListener.class);

//	private Socket socket;
	private SocketLogReader reader;

	public SocketListener(Socket socket) throws IOException {
//		this.socket = socket;
		reader = new SocketLogReader(socket);
	}

	public void run() {
		LoggingEvent event;
		while ((event = reader.nextEvent()) != null) {
			processEvent(event);
		}
	}

	protected void processEvent(LoggingEvent event) {
		
	}
}
