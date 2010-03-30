package cc.wily.logviewer.processing;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import cc.wily.logviewer.entry.LogEntry;

/**
 * {@link EventProcessor} that processes events from a specific host and
 * forwards them to the central queue for processing.
 * 
 * @author aron
 */
@Component("perHostEventProcessor")
public class PerHostEventProcessor implements EventProcessor {
	private static Logger LOG = Logger.getLogger(PerHostEventProcessor.class);

	private BlockingQueue<LogEntry> queue;

	public void processEvent(LogEntry event) {
		boolean done = false;
		while (!done) {
			try {
				queue.put(event);
				done = true;
			} catch (InterruptedException e) {
				LOG.info("Thread interrupted while queueing event; retrying", e);
			}
		}
	}

	public void setCentralQueue(BlockingQueue<LogEntry> queue) {
		this.queue = queue;
	}
}
