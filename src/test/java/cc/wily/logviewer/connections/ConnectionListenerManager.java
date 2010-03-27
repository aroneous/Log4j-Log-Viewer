package cc.wily.logviewer.connections;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class ConnectionListenerManager {
	private static Logger LOG = Logger.getLogger(ConnectionListenerManager.class);

	private ConnectionListener listener;
	private Thread listenerThread;
	
	@PostConstruct
	public void setUp() {
		LOG.info("starting up");
		listenerThread = new Thread(listener);
		listenerThread.start();
	}
	
	@PreDestroy
	public void tearDown() {
		LOG.info("shutting down");
		try {
			listener.shutdown();
		} catch (IOException e1) {
			LOG.error("Failed to close ConnectionListener", e1);
		}
		try {
			listenerThread.join();
		} catch (InterruptedException e) {
			LOG.warn("Interrupted waiting for listener thread to die", e);
		}
	}
	
	@Autowired
	@Required
	public void setConnectionListener(ConnectionListener listener) {
		this.listener = listener;
	}
}
