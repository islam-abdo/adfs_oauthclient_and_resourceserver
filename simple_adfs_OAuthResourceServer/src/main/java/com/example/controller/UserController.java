package com.example.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
	@RequestMapping("/api/user")
	public Principal user(Principal principal) {
		return principal;
	}

	@RequestMapping("/api/hi")
	public String hi(Principal principal) {
		return "Hi, " + principal.getName();
	}
}
