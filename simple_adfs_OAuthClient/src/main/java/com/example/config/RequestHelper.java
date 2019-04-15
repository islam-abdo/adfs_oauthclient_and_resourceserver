package com.example.config;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestHelper {
	
	public RequestHelper() {

	}
	
    @Bean
    public Boolean disableSSLValidation() throws Exception {
        final SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, null);

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        return true;
    }

//	public HttpComponentsClientHttpRequestFactory getRequestFactory()
//			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
//
//		HttpClientBuilder httpClientBuilder = HttpClients.custom();
//
//		// Skip SSL validation based on condition
//		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
//
//		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
//				.build();
//		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
//
//		httpClientBuilder = httpClientBuilder.setSSLSocketFactory(csf);
//
//		CloseableHttpClient httpClient = httpClientBuilder.build();
//		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//		requestFactory.setHttpClient(httpClient);
//		return requestFactory;
//	}
}