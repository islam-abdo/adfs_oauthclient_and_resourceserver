package com.example.controller;

import static org.springframework.http.HttpMethod.GET;

import java.security.Principal;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.util.HttpUtils;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	@Autowired
	OAuth2RestTemplate restTemplate;

	@Autowired
	HttpUtils httpUtils;

	@RequestMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}

	@RequestMapping("/hi")
	public String hi(Principal principal) {
		OAuth2Authentication oauth = (OAuth2Authentication) principal;
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> details = (LinkedHashMap<String, String>) oauth.getUserAuthentication().getDetails();
		return "Hi, " + details.get("display_name");
	}

	@RequestMapping("/hi2")
	public String hi2(Principal principal) {
		final String greeting = httpUtils.sendRequest("http://127.0.0.1:8082/api/hello", String.class,
				restTemplate.getOAuth2ClientContext().getAccessToken().getValue(), GET);

		System.out.println(greeting);
		return greeting;
	}
}
