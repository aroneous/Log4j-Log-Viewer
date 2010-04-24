package cc.wily.logviewer.presentation.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Viewer implements EntryPoint {

	public void onModuleLoad() {
		final Label label = new Label();
		label.setText("Hi there");

		RootPanel.get("labelContainer").add(label);
	}

}
