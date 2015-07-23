package com.csg.demo.azure.insights;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("faq/")
public class FAQController {


	Logger logger = LogManager.getLogger();


	@RequestMapping(method = RequestMethod.GET)
	public String loadFaq(ModelMap model) {
		return "faq";
	}

}