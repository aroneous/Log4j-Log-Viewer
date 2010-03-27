package cc.wily.logviewer.connections;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SocketConnectionTests extends BaseConnectionTests {
	@Resource
	private Object syncToken;
	
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
			synchronized(syncToken) {
				syncToken.wait(10000);
			}
		} catch (InterruptedException e) {
			Assert.fail("Interrupted waiting for SocketListener to notify");
		}
	}
}
