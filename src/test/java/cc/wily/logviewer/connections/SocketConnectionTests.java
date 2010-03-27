package cc.wily.logviewer.connections;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SocketConnectionTests extends BaseConnectionTests {
	private static Logger LOG = Logger.getLogger(SocketConnectionTests.class);

	@Resource
	private Semaphore syncToken;
	
	@Test
	public void testConnectionAccept() throws IOException {
		Socket socket = new Socket();
		
		SocketAddress saddr = new InetSocketAddress(InetAddress.getLocalHost(), 4445);
		try {
			socket.connect(saddr, 10000);
		} catch (ConnectException e) {
			Assert.fail("Connection refused");
		} catch (SocketTimeoutException e) {
			Assert.fail("Timeout attempting to connect to SocketListener");
		}
		
		try {
			boolean signaled = syncToken.tryAcquire(10000, TimeUnit.MILLISECONDS);
			Assert.assertTrue("Timed out waiting for SocketListener to signal", signaled);
		} catch (InterruptedException e) {
			Assert.fail("Interrupted waiting for SocketListener to notify");
		}
	}
	
	@Test
	public void testMultipleConnectionAccept() throws IOException {
		
		SocketAddress saddr = new InetSocketAddress(InetAddress.getLocalHost(), 4445);
		try {
			for (int i = 0; i < 2; i++) {
				Socket socket = new Socket();
				socket.connect(saddr, 10000);
			}
		} catch (ConnectException e) {
			LOG.error("Socket error on connect attempt", e);
			Assert.fail("Connection refused");
		} catch (SocketTimeoutException e) {
			Assert.fail("Timeout attempting to connect to SocketListener");
		}
		
		try {
			boolean signaled = syncToken.tryAcquire(2, 10000, TimeUnit.MILLISECONDS);
			Assert.assertTrue("Timed out waiting for SocketListeners to signal", signaled);
		} catch (InterruptedException e) {
			Assert.fail("Interrupted waiting for SocketListeners to notify");
		}
	}
}
