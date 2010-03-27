package cc.wily.logviewer.connections;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cc.wily.test.net.LocalSocket;
import cc.wily.test.net.LocalSocketImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SocketListenerTests {
	@Resource
	private SocketListener socketListener;

	@Resource
	private Semaphore syncToken;

	private Socket socket;

	private ObjectOutputStream toSocketOOS;

	@Before
	public void init() throws IOException {
		org.springframework.util.Assert.notNull(socketListener);
		org.springframework.util.Assert.notNull(syncToken);

		LocalSocketImpl impl = new LocalSocketImpl();
		OutputStream toSocketStream = impl.getToSocketStream();
		toSocketOOS = new ObjectOutputStream(toSocketStream);
		socket = new LocalSocket(impl);
		socketListener.setSocket(socket);
		Thread listenerThread = new Thread(socketListener);

		listenerThread.start();
	}

	@SuppressWarnings("deprecation")
	private LoggingEvent createTestLoggingEvent() {
		Logger logger = Logger.getLogger(SocketListenerTests.class);
		return new LoggingEvent("my.test.category", logger, Priority.INFO,
				"I am a test LoggingEntry", null);

	}

	@Test
	public void testLoggingEventRead() throws IOException {
		LoggingEvent testEvent = createTestLoggingEvent();
		toSocketOOS.writeObject(testEvent);

		try {
			boolean signaled = syncToken.tryAcquire(10000,
					TimeUnit.MILLISECONDS);
			Assert.assertTrue(
					"Timed-out waiting for signal from EventProcessor",
					signaled);
		} catch (InterruptedException e) {
			Assert.fail("Interrupted waiting for signal from EventProcessor");
		}
	}
	
	@Test
	public void testTwoConsecutiveEvents() throws IOException {
		for (int i = 0; i < 2; i++) {
			LoggingEvent testEvent = createTestLoggingEvent();
			toSocketOOS.writeObject(testEvent);
		}

		try {
			boolean signaled = syncToken.tryAcquire(2, 10000,
					TimeUnit.MILLISECONDS);
			Assert.assertTrue(
					"Timed-out waiting for signals from EventProcessor",
					signaled);
		} catch (InterruptedException e) {
			Assert.fail("Interrupted waiting for signals from EventProcessor");
		}
	}

	@After
	public void teardown() throws IOException {
		socket.close();
	}
}
