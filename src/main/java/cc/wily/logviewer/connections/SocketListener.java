package cc.wily.logviewer.connections;

import java.io.IOException;
import java.net.Socket;

public interface SocketListener extends Runnable {
	/**
	 * Set the socket to be read by this listener. This will be set by the
	 * {@link ConnectionListener}, and so should not be injected by the
	 * container.
	 * 
	 * @param socket
	 *            The {@link Socket} to set.
	 * @throws IOException
	 *             If configuration related to the socket fails.
	 */
	void setSocket(Socket socket) throws IOException;
}
