package cc.wily.logviewer.processing;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cc.wily.logviewer.entry.LogEntry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class LogAggregatorTests {
	@Resource
	private BlockingQueue<LogEntry> centralQueue;
	
	@Resource
	private Semaphore syncToken;
	
	@Test
	public void testEvent() {
		LogEntry entry = new LogEntry();
		
		try {
			centralQueue.put(entry);
		} catch (InterruptedException e) {
			fail("Interrupted attempting to write to queue");
		}
		
		try {
			boolean received = syncToken.tryAcquire(10000, TimeUnit.MILLISECONDS);
			assertTrue("Timed out waiting for signal from event processor", received);
		} catch (InterruptedException e) {
			fail("Interrupted waiting for signal");
		}
	}
	
	@Test
	public void test10Events() {
		LogEntry entry = new LogEntry();
		
		try {
			for (int i = 0; i < 10; i++) {
				centralQueue.put(entry);
			}
		} catch (InterruptedException e) {
			fail("Interrupted attempting to write to queue");
		}
		
		try {
			boolean received = syncToken.tryAcquire(10, 10000, TimeUnit.MILLISECONDS);
			assertTrue("Timed out waiting for signal from event processor", received);
		} catch (InterruptedException e) {
			fail("Interrupted waiting for signal");
		}
	}
}
