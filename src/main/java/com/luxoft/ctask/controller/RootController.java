package com.luxoft.ctask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
	@GetMapping("/")
	public String index(Model model){
	  return "uploadForm";
	}
}
