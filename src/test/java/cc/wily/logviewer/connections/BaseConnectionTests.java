package cc.wily.logviewer.connections;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-app-config.xml")
public class BaseConnectionTests {
	private static Logger LOG = Logger.getLogger(BaseConnectionTests.class);

	private ConnectionListener listener;
	private Thread listenerThread;
	
	@Before
	public void setUp() {
		listenerThread = new Thread(listener);
		listenerThread.start();
	}
	
	@After
	public void tearDown() {
		try {
			listener.shutdown();
		} catch (IOException e1) {
			LOG.error("Failed to close ConnectionListener", e1);
		}
		try {
			listenerThread.join();
		} catch (InterruptedException e) {
			LOG.warn("Interrupted waiting for listener thread to die", e);
		}
	}
	
	@Autowired
	@Required
	public void setConnectionListener(ConnectionListener listener) {
		this.listener = listener;
	}
}
