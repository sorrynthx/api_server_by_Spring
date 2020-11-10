package com.z.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebClient {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private int TIME_OUT = 16000;
	
	public String callHttpPOST(String urlString) {
		String output = "";
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		
		try {
//			logger.info("webClient : 1");
			RequestConfig reqConfig = RequestConfig.custom()
					.setSocketTimeout(TIME_OUT)
					.setConnectTimeout(TIME_OUT)
					.setConnectionRequestTimeout(TIME_OUT)
					.build();
//			logger.info("webClient : 2");
			httpClient = HttpClientBuilder.create()
					.setDefaultRequestConfig(reqConfig)
					.build();
//			logger.info("webClient : 3");
			HttpPost postRequest = new HttpPost(urlString);
			postRequest.setConfig(reqConfig);
			
			//strAccessData.setContentType("application/json; charset=UTF-8");
			postRequest.setHeader("Content-Type", "application/json; charset=UTF-8");
			postRequest.setHeader("Cache-Control", "no-cache");
//			logger.info("webClient : 4");
			response = httpClient.execute(postRequest);
//			logger.info("webClient : 5");
			int statusCode = response.getStatusLine().getStatusCode();
//			logger.info("statusCode ->"+statusCode);
			if (statusCode != 200) {
				output = "ERR_" + response.getStatusLine().getStatusCode();
				//throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			} else {
//				logger.info("webClient : 6");
				BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), "UTF-8"));
				StringBuilder sb = new StringBuilder("");
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
				output = sb.toString();
			}
		} catch (Exception e) {
			logger.info("callHttpPOST Error ->" + e.getMessage());
			
			//if (e.getMessage().indexOf("Connection refused") > 0) {
				// xrL4 서비스 이상으로 판단
//				config.setValue("vsc.url", config.getValue("vsc.url_backup"));
//				logger.info("VSC Server -> " + config.getValue("vsc.url"));
			//}
			
			output = "ERR_FAIL";
		} finally {
			if (response != null) {
				response = null;
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) { }
			}
		}
		
		//logger.info("output->" + output);
		return output;
	}
	
	public String callHttpPOST(String urlString, String reqString) {
		String output = "";
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		
		try {
			StringEntity strAccessData = new StringEntity(reqString, "UTF-8");
			RequestConfig reqConfig = RequestConfig.custom()
					.setSocketTimeout(TIME_OUT)
					.setConnectTimeout(TIME_OUT)
					.setConnectionRequestTimeout(TIME_OUT)
					.build();
			
			httpClient = HttpClientBuilder.create()
					.setDefaultRequestConfig(reqConfig)
					.build();
			
			HttpPost postRequest = new HttpPost(urlString);
			postRequest.setConfig(reqConfig);
			
			//strAccessData.setContentType("application/json; charset=UTF-8");
			postRequest.setEntity(strAccessData);
			postRequest.setHeader("Content-Type", "application/json; charset=UTF-8");
			postRequest.setHeader("Cache-Control", "no-cache");
			
			response = httpClient.execute(postRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode != 200) {
				output = "ERR_" + response.getStatusLine().getStatusCode();
				//throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), "UTF-8"));
				StringBuilder sb = new StringBuilder("");
				
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
				output = sb.toString();
			}
		} catch (Exception e) {
			logger.info("callHttpPOST Error ->" + e.getMessage());
			output = "ERR_FAIL";
			
		} finally {
			if (response != null) {
				response = null;
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) { }
			}
		}
		
		//logger.info("output->" + output);
		return output;
	}
	

}
