package cc.wily.logviewer.connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ConnectionListener implements Runnable, ApplicationContextAware {
	private static Logger LOG = Logger.getLogger(ConnectionListener.class);

	private ServerSocket listener;

	private TaskExecutor executor;

	private ApplicationContext applicationContext;

	private boolean shutdown = false;

	private Thread connectionThread;

	@PostConstruct
	public void init() {
		connectionThread = new Thread(this);
		connectionThread.start();
	}

	@PreDestroy
	public void tearDown() {
		LOG.info("shutting down");
		try {
			shutdown();
		} catch (IOException e1) {
			LOG.error("Failed to close ConnectionListener", e1);
		}
		try {
			connectionThread.join();
		} catch (InterruptedException e) {
			LOG.warn("Interrupted waiting for listener thread to die", e);
		}
	}

	public void run() {
		while (true) {
			try {
				if (shutdown) {
					LOG.info("Connection listener shutting down");
					break;
				}
				Socket socket = listener.accept();
				LOG.debug("New connection received");
				handleNewConnection(socket);
			} catch (SocketException e) {
				LOG.info("Socket accept interrupted");
				// Assume we're being killed
			} catch (IOException e) {
				LOG.error("Error when listening for incoming connections", e);
			}
		}
	}

	protected void handleNewConnection(Socket socket) {
		try {
			SocketListener reader = getSocketListener();
			reader.setSocket(socket);
			executor.execute(reader);
		} catch (IOException e) {
			LOG.error("Failed to create SocketLogReader for "
					+ socket.getInetAddress().toString() + ":"
					+ socket.getPort() + "; bailing out", e);
			try {
				socket.close();
			} catch (IOException e1) {
				LOG.error("Failed to close socket", e1);
			}
		}
	}

	@Resource
	@Required
	public void setExecutor(TaskExecutor executor) {
		this.executor = executor;
	}

	public void shutdown() throws IOException {
		if (!shutdown) {
			shutdown = true;
			listener.close();
		}
	}

	/**
	 * Set the server socket to accept connections on. This socket should be
	 * already bound to a port.
	 * 
	 * @param listener
	 */
	@Resource
	@Required
	public void setServerSocket(ServerSocket listener) {
		this.listener = listener;
	}

	protected SocketListener getSocketListener() {
		return applicationContext.getBean(SocketListener.class);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
