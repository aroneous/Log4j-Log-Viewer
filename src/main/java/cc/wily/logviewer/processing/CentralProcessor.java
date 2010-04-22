package cc.wily.logviewer.processing;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import cc.wily.logviewer.entry.LogEntry;

/**
 * Event processor that places events onto the 'recent events' circular buffer.
 * 
 * @author aron
 */
@Component("centralProcessor")
public class CentralProcessor implements EventProcessor {
	private List<LogEntry> buffer;

	public void processEvent(LogEntry event) {
		buffer.add(event);
	}

	@Resource
	@Required
	public void setRecentEntryBuffer(List<LogEntry> buffer) {
		this.buffer = buffer;
	}
}
