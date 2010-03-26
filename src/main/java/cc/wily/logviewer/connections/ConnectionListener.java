package cc.wily.logviewer.connections;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ConnectionListener implements Runnable {
	private static Logger LOG = Logger.getLogger(ConnectionListener.class);

	ServerSocket listener;
	
	TaskExecutor executor;

	public void run() {
		while (true) {
			try {
				Socket socket = listener.accept();
				handleNewConnection(socket);
			} catch (InterruptedIOException e) {
				// Assume we're being killed
				break;
			} catch (IOException e) {
				LOG.error("Error when listening for incoming connections", e);
			}
		}
	}
	
	protected void handleNewConnection(Socket socket) {
		try {
			SocketListener reader = new SocketListener(socket);
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
}
