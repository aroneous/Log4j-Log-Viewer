package cc.wily.logviewer.connections;

import java.io.IOException;
import java.net.Socket;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cc.wily.logviewer.processing.EventProcessor;
import cc.wily.logviewer.reader.SocketLogReader;

@Component
@Scope("prototype")
public class SocketListenerImpl implements SocketListener {
	private static Logger LOG = Logger.getLogger(SocketListenerImpl.class);

	private Socket socket;
	private SocketLogReader reader;

	private EventProcessor eventProcessor;

	public void run() {
		LoggingEvent event;
		while ((event = reader.nextEvent()) != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Received event for " + socket.getInetAddress() + ":"
						+ socket.getPort());
			}
			eventProcessor.processEvent(event);
		}
	}

	/**
	 * Process an incoming event. The {@link #processEvent(LoggingEvent)}
	 * routine will be called on the event listener thread for the relevant
	 * connection.
	 * 
	 * @param eventProcessor
	 *            Event processor to use with this <code>SocketListener</code>
	 */
	@Resource
	@Required
	public void setEventProcessor(EventProcessor eventProcessor) {
		this.eventProcessor = eventProcessor;
	}
	
	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		reader = new SocketLogReader(socket);
	}
}
