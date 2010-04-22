package cc.wily.logviewer.mvc.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cc.wily.logviewer.entry.LogEntry;
import cc.wily.util.CircularBuffer;

/**
 * Handles requests for the application welcome page.
 */
@Controller
@RequestMapping("/view")
public class ViewLogsController {
	private CircularBuffer<LogEntry> buffer;

	/**
	 * Simply selects the welcome view to render by returning void and relying
	 * on the default request-to-view-translator.
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ModelAttribute("entries")
	public List<LogEntry> view() {
		List<LogEntry> entries = buffer.snapshot();
		return entries;
	}

	@Resource
	@Required
	public void setRecentEntryBuffer(CircularBuffer<LogEntry> buffer) {
		this.buffer = buffer;
	}
}
