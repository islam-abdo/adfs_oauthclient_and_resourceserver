package com.example.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.OAuth2AccessTokenSupport;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableOAuth2Sso
public class SimpleAdfsOAuthClientAppConfig extends WebSecurityConfigurerAdapter {

	private static Logger LOGGER = LoggerFactory.getLogger(SimpleAdfsOAuthClientAppConfig.class);

	@Autowired
	private ResourceServerProperties sso;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**").authorizeRequests().antMatchers("/", "/login**", "/webjars/**").permitAll().anyRequest()
				.authenticated().and().addFilterBefore(oauthFilter(), BasicAuthenticationFilter.class);
	}

	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	@Autowired
	OAuth2ProtectedResourceDetails resource;

	@Autowired
	RequestHelper requestHelper;

	private Filter oauthFilter() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		OAuth2ClientAuthenticationProcessingFilter oauthFilter = new OAuth2ClientAuthenticationProcessingFilter(
				"/login");
		// Set request factory for '/userinfo'
		LOGGER.info("token info uri {}, clientId {}", sso.getTokenInfoUri(), sso.getClientId());
		ResourceServerTokenServices userInfoTokenServices = new AdfsUserInfoTokenServices(sso.getTokenInfoUri(),
				sso.getClientId());
		oauthFilter.setTokenServices(userInfoTokenServices);

		OAuth2RestTemplate oauthTemplate = new OAuth2RestTemplate(resource, oauth2ClientContext);
		OAuth2AccessTokenSupport authAccessProvider = new AuthorizationCodeAccessTokenProvider();
		// Set request factory for '/oauth/token'
		//authAccessProvider.setRequestFactory(requestHelper.getRequestFactory());
		AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(
				Arrays.<AccessTokenProvider>asList((AuthorizationCodeAccessTokenProvider) authAccessProvider));
		oauthTemplate.setAccessTokenProvider(accessTokenProvider);
		//oauthTemplate.setRequestFactory(requestHelper.getRequestFactory());
		oauthFilter.setRestTemplate(oauthTemplate);
		return oauthFilter;
	}
}
