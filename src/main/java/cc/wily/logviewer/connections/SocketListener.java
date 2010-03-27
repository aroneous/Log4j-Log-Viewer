package cc.wily.logviewer.connections;

import java.io.IOException;
import java.net.Socket;

public interface SocketListener extends Runnable {
	void setSocket(Socket socket) throws IOException;
}
