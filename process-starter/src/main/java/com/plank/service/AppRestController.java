package com.plank.service;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plank.process.server.service.IndicatorAlgo;

@Controller
public class AppRestController {

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/getdata")
	public String getSymbol(@RequestParam(value = "name") String name) {

		IndicatorAlgo indicatorAlgo = new IndicatorAlgo();
		return indicatorAlgo.getSymbol();
	}

	@PostMapping("/hello")
	public String sayHello(@RequestParam("name") String name, Model model) {
		model.addAttribute("name", name);
		return "hello";
	}
}
