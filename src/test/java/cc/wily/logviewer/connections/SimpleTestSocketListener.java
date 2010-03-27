package cc.wily.logviewer.connections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.Socket;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

public class SimpleTestSocketListener implements SocketListener {
	private static Logger LOG = Logger.getLogger(SimpleTestSocketListener.class);
	
	@Resource
	private Object syncToken;

	private Socket socket;

	public void setSocket(Socket socket) throws IOException {
		assertNotNull(socket);
		assertTrue(socket.isConnected());
		this.socket = socket;
	}

	public void run() {
		LOG.info("Running SimpleTestSocketListener");
		synchronized(syncToken) {
			syncToken.notify();
		}
		try {
			socket.close();
		} catch (IOException e) {
			fail("Failed to close socket");
		}
	}

}
