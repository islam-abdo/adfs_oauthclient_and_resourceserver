package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SimpleAdfsOAuthResourceServerAppConfig extends GlobalMethodSecurityConfiguration {

	@Value("${security.oauth2.resource.user-info-uri}")
	private String userInfoUri;

	@Value("${security.oauth2.client.client-id}")
	private String clientId;

	@Primary
	@Bean("userInfoTokenServices")
	public ResourceServerTokenServices userInfoTokenServices() {
		final ResourceServerTokenServices tokenService = new AdfsUserInfoTokenServices(userInfoUri, clientId);
		return tokenService;
	}

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}
}
