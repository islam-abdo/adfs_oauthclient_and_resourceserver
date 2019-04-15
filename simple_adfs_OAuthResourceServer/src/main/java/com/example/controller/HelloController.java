package com.example.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HelloController {
	@RequestMapping("/api/hello")
	public String hi(Principal principal) {
		return "Hello from app2 ;)";
	}
}
