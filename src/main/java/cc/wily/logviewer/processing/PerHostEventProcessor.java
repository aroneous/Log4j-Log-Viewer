package cc.wily.logviewer.processing;

import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import cc.wily.logviewer.entry.LogEntry;

/**
 * <p>
 * {@link EventProcessor} that processes events from a specific host and
 * forwards them to the central queue for processing.
 * </p>
 * 
 * <p>
 * This class currently has no per-thread state (only the shared destination
 * queue, which is thread safe), so it is created as a singleton. This should be
 * changed to prototype scope if additional state is added.
 * </p>
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
				LOG
						.info(
								"Thread interrupted while queueing event; retrying",
								e);
			}
		}
	}

	@Resource
	@Required
	public void setCentralQueue(BlockingQueue<LogEntry> queue) {
		this.queue = queue;
	}
}
