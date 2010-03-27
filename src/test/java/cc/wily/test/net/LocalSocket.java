package cc.wily.test.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LocalSocket extends Socket {
	public LocalSocket(LocalSocketImpl impl) throws IOException {
		super(impl);
		
		// Set socket into a connected state...is a NOP on LocalSocketImpl
		connect(new InetSocketAddress(23456));
	}
}
