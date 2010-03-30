package cc.wily.logviewer.processing;

import java.util.concurrent.Semaphore;

import javax.annotation.Resource;

import cc.wily.logviewer.entry.LogEntry;

public class TestEventProcessor implements EventProcessor {
	@Resource
	private Semaphore syncToken;
	
	public void processEvent(LogEntry event) {
		if (event != null) {
			syncToken.release();
		}
	}

}
