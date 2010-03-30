package cc.wily.logviewer.processing;

import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import cc.wily.logviewer.entry.LogEntry;

/**
 * Reads log entries off of the central queue and sends them to the central
 * event processor for handling.
 * 
 * @author aron
 */
@Component
public class LogAggregator implements Runnable {
	private static Logger LOG = Logger.getLogger(LogAggregator.class);

	private Thread aggregatorThread;

	private boolean shutdown = false;

	private EventProcessor centralProcessor;

	private BlockingQueue<LogEntry> queue;

	/**
	 * Start a main processing thread and execute myself in it.
	 */
	@PostConstruct
	public void init() {
		aggregatorThread = new Thread(this);
		aggregatorThread.start();
	}

	/**
	 * Shut down the aggreagator thread and wait for it to finish.
	 */
	@PreDestroy
	public void tearDown() {
		shutdown();
		try {
			aggregatorThread.join();
		} catch (InterruptedException e) {
			LOG.warn("Interrupted waiting for aggregator thread to end", e);
		}
	}

	/**
	 * Shut down the aggregator thread.
	 * 
	 * @return
	 */
	public void shutdown() {
		shutdown = true;
		aggregatorThread.interrupt();
	}

	public void run() {
		while (true) {
			try {
				if (shutdown) {
					LOG.info("Aggregator thread shutting down upon request");
					break;
				}
				LogEntry entry = queue.take();

				centralProcessor.processEvent(entry);
			} catch (InterruptedException e) {
				LOG
						.info("Aggregator thread interrupted while waiting for event");
			}

		}
	}

	@Resource
	@Required
	public void setCentralProcessor(EventProcessor centralProcessor) {
		this.centralProcessor = centralProcessor;
	}

	@Resource
	@Required
	public void setCentralQueue(BlockingQueue<LogEntry> queue) {
		this.queue = queue;
	}

}
