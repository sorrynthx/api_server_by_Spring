package com.z.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Functions {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat TODAY_YMD = new SimpleDateFormat("yyyyMMdd");
	static WebConfig config = new WebConfig();
	
	public String getDate() {
		return YYYYMMDD.format(new Date());
	}
	
	public String getSerialNo() {
		// 0.01 초의 오차를 없애기 위해 0.01 초간 쉼
        try {
			Thread.sleep(10);
		} catch (InterruptedException e) { } 
        
        String serialNo = "" + System.currentTimeMillis();
        serialNo = serialNo.substring(5, 12);
        
        return TODAY_YMD.format(new Date()) + "_" + serialNo;
	}
	
	public String getRandom(int size) {
		String strTemp = "";
		
		for (int i = 0; i < size; i++) {
			Random rnd = new Random();
			strTemp += rnd.nextInt(9);
		}
		
		return strTemp;
	}
	
	public String getExtension(String fileName) {
		int dotPosition = fileName.lastIndexOf('.');
		
		if (-1 != dotPosition && fileName.length() - 1 > dotPosition) {
			return fileName.substring(dotPosition + 1);
		} else {
			return "";
		}
	}
	
	public String getShortString(String orig, int size) {
		return getShortString(orig, size, "...");
	}
	
	public String getShortString(String orig, int size, String tails) {
		byte[] bString = orig.getBytes();
		int length = size;
		
		if (bString.length <= length) {
			return orig;
		} else {
			int mByteCount = 0;
			for (int i = 0; i < length; i++) {
				mByteCount += (bString[i] < 0) ? 1: 0;
			}
			
			if (mByteCount % 2 != 0) {
				length--;
			}
			
			return new String(bString, 0, length) + tails;
		}
	}

	public String callHttpPOST(String urlString, String reqString) {
		String output = "";
		try {

			StringEntity strAccessData = new StringEntity(reqString, "UTF-8");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(urlString);

			strAccessData.setContentType("application/json; charset=UTF-8");
			postRequest.setEntity(strAccessData);
			postRequest.setHeader("VSC-Id", config.getValue("vscID"));
			
			HttpResponse response = httpClient.execute(postRequest);
			
			if (response.getStatusLine().getStatusCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()), "UTF-8"));
			StringBuilder sb = new StringBuilder("");
			
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			output = sb.toString();

			httpClient.getConnectionManager().shutdown();
		} catch (Exception e) {
			
		}
		
		logger.info("output->" + output);
		return output;
	}
}
