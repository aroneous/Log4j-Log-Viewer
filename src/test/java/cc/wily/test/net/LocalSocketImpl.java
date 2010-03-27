package cc.wily.test.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.util.HashMap;
import java.util.Map;

public class LocalSocketImpl extends SocketImpl {
	private PipedInputStream socketInputStream;
	private PipedOutputStream toSocketStream;
	
	private PipedOutputStream socketOutputStream;
	private PipedInputStream fromSocketStream;
	
	private Map<Integer, Object> options;
	
	public LocalSocketImpl() throws IOException {
		toSocketStream = new PipedOutputStream();
		socketInputStream = new PipedInputStream(toSocketStream);
		
		fromSocketStream = new PipedInputStream();
		socketOutputStream = new PipedOutputStream(fromSocketStream);
		
		options = new HashMap<Integer, Object>();
		
		address = InetAddress.getLocalHost();
	}
	
	public OutputStream getToSocketStream() {
		return toSocketStream;
	}
	
	public InputStream getFromSocketStream() {
		return fromSocketStream;
	}

	@Override
	protected void accept(SocketImpl s) throws IOException {
		throw new UnsupportedOperationException("Can't accept connections");
	}

	@Override
	protected int available() throws IOException {
		return socketInputStream.available();
	}

	@Override
	protected void bind(InetAddress host, int port) throws IOException {
		throw new UnsupportedOperationException("Can't bind");
	}

	@Override
	protected void close() throws IOException {
		socketInputStream.close();
		socketOutputStream.close();
		
		fromSocketStream.close();
		toSocketStream.close();
	}

	@Override
	protected void connect(String host, int port) throws IOException {
	}

	@Override
	protected void connect(InetAddress address, int port) throws IOException {
	}

	@Override
	protected void connect(SocketAddress address, int timeout)
			throws IOException {
	}

	@Override
	protected void create(boolean stream) throws IOException {
	}

	@Override
	protected InputStream getInputStream() throws IOException {
		return socketInputStream;
	}

	@Override
	protected OutputStream getOutputStream() throws IOException {
		return socketOutputStream;
	}

	@Override
	protected void listen(int backlog) throws IOException {
		throw new UnsupportedOperationException("Can't listen");
	}

	@Override
	protected void sendUrgentData(int data) throws IOException {
		throw new UnsupportedOperationException("Can't send urgent data");
	}

	public Object getOption(int optID) throws SocketException {
		return options.get(optID);
	}

	public void setOption(int optID, Object value) throws SocketException {
		options.put(optID, value);
	}

}
