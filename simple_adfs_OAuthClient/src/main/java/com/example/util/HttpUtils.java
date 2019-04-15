package com.example.util;

import java.security.Principal;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpUtils {
	public <T> T sendRequest(String url, Class<T> responseType, String token, HttpMethod httpMethod) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("Bearer %s", token));
        //LOGGER.info("Setting bearer: {}", token);
        HttpEntity<Principal> request = new HttpEntity<>(headers);
        @SuppressWarnings("unchecked")
		T body = (T) restTemplate.exchange(url, httpMethod, request, String.class).getBody();
		return body;
	}
}
