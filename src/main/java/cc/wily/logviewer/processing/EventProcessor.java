package cc.wily.logviewer.processing;

import org.apache.log4j.spi.LoggingEvent;

public interface EventProcessor {
	/**
	 * Process an event
	 * 
	 * @param event
	 *            The event to process.
	 */
	void processEvent(LoggingEvent event);
}
