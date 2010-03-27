package cc.wily.logviewer.processing;

import java.util.concurrent.Semaphore;

import javax.annotation.Resource;

import org.apache.log4j.spi.LoggingEvent;

public class TestEventProcessor implements EventProcessor {
	@Resource
	private Semaphore syncToken;
	
	public void processEvent(LoggingEvent event) {
		if (event != null) {
			syncToken.release();
		}
	}

}
