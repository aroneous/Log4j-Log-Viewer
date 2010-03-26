package cc.wily.logviewer.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/view")
public class ViewLogController {
	@RequestMapping(method = RequestMethod.GET)
	public void view() {
	}
}
