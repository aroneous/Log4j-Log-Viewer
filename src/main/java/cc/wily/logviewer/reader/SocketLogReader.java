package cc.wily.logviewer.reader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

public class SocketLogReader {
	private static Logger LOG = Logger.getLogger(SocketLogReader.class);
	private Socket socket;
	private ObjectInputStream ois;
	private boolean done = false;
	
	public SocketLogReader(Socket socket) throws IOException {
		this.socket = socket;
		ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
	}
	
	public LoggingEvent nextEvent() {
		if (done) {
			return null;
		}
		
		LoggingEvent event = null;
		
		while (event == null && !done) {
			try {
				Object obj = ois.readObject();
				if (obj instanceof LoggingEvent) {
					event = (LoggingEvent) obj;
				}
			} catch (InterruptedIOException e) {
				Thread.currentThread().interrupt();
				done = true;
			} catch (IOException e) {
				done = true;
			} catch (ClassNotFoundException e) {
				LOG.warn("Unexpected class received on socket", e);
			}
		}
		
		return event;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public void close() {
		try {
			ois.close();
		} catch (IOException e) {
			LOG.warn("Failed to close ObjectInputStream", e);
		}
		
		try {
			socket.close();
		} catch (InterruptedIOException e) {
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			LOG.warn("Failed to close Socket", e);
		}
	}
}
