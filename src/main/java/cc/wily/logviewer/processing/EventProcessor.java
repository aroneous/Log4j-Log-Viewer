package cc.wily.logviewer.processing;

import cc.wily.logviewer.entry.LogEntry;

public interface EventProcessor {
	/**
	 * Process an event
	 * 
	 * @param event
	 *            The event to process.
	 */
	void processEvent(LogEntry event);
}
