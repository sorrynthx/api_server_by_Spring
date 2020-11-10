package com.z.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyFunctions {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	static String allow_ip = "192.168.0.*   ,    192.*.0.255  ";

	// 외부 properties 읽기 (server.xml, context.xml)
	public Properties read_outConfig () {
		
		Context context = null;
		Properties properties = new Properties();
		try {
			context = (Context)(new InitialContext().lookup("java:/comp/env"));
			InputStream stream = new FileInputStream((String) context.lookup("location"));
			properties.load(new InputStreamReader(stream, "UTF-8"));
			
		} catch (NamingException e) {
			logger.info("read _outConfig NamingException Error : " + e);
		} catch (FileNotFoundException e) {
			logger.info("read _outConfig FileNotFoundException Error : " + e);
		} catch (UnsupportedEncodingException e) {
			logger.info("read _outConfig UnsupportedEncodingException Error : " + e);
		} catch (IOException e) {
			logger.info("read _outConfig IOException Error : " + e);
		} finally {
			if (context != null) context = null;
		}
		
		return properties;
	}
	
	// 내부 properties 읽기 (src/test/resources/ or src/main/java/config/)
	public Properties read_inConfig(){
		
		String in_resource = "config/config";
		Properties in_properties = new Properties();
		
		try {
			Reader reader = Resources.getResourceAsReader(in_resource);
			in_properties.load(reader);
			
		} catch (IOException e) {
			logger.info("inner config reading Error IOException : " + e);
		}
		
		return in_properties;
	}
	
	
	// ip확인
	public boolean ipCheck (HttpServletRequest HttpRequest) {
		
		String ip = HttpRequest.getHeader("X-Forwarded-For");
		//logger.info("> X-FORWARDED-FOR : " + ip);
		
		if (ip == null) {
			ip = HttpRequest.getHeader("Proxy-Client-IP");
			//logger.info("> Proxy-Client-IP : " + ip);
		}
		if (ip == null) {
			ip = HttpRequest.getHeader("WL-Proxy-Client-IP");
			//logger.info(">  WL-Proxy-Client-IP : " + ip);
		}
		if (ip == null) {
			ip = HttpRequest.getHeader("HTTP_CLIENT_IP");
			//logger.info("> HTTP_CLIENT_IP : " + ip);
		}
		if (ip == null) {
			ip = HttpRequest.getHeader("HTTP_X_FORWARDED_FOR");
			//logger.info("> HTTP_X_FORWARDED_FOR : " + ip);
		}
		if (ip == null) {
			ip = HttpRequest.getRemoteAddr();
			//logger.info("> getRemoteAddr : "+ip);
		}
		logger.info(" accssing IP Address : "+ip);
		
		// 접근한 ip가 allow_ip에 포함되는지 확인
		String[] ip_group = allow_ip.split(",");
		
		for (String allowIp : ip_group) {
			allowIp = allowIp.trim();
			
			// ip에서 * 발견 시, 0과 255로 변환
			if (allowIp.indexOf("*") > 0) {
				long lowValue = ipToLong(allowIp.replaceAll("\\*", "0"));
				long maxValue =  ipToLong(allowIp.replaceAll("\\*", "255"));
				long currentValue = ipToLong(ip);
				
				if (lowValue <= currentValue && lowValue <= maxValue) return true;
			} else {
				if (ipToLong(allowIp) == ipToLong(ip)) return true;
			}
		}
		
		return false;
	}
	
	// ip ---> Long 변환
	public long ipToLong(String ip) {

		long result = 0;
		String[] idx = ip.toString().split("\\.");
		
		for (int i=0; i<idx.length; i++) {
			result += Integer.parseInt(idx[i]) * (int) Math.pow(2, (24 - (i * 8)));
		}
		
		return result;
	}
	
	// 오늘 yyyy-MM-dd
	public String getDate() {
		SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
		return YYYYMMDD.format(new Date());
	}
	
	// 시리얼 번호 생성 : yyyyMMdd + milsec(5,12)
	public String getSerialNo() {
		
		SimpleDateFormat TODAY_YMD = new SimpleDateFormat("yyyyMMdd");
		
		// 0.01 초의 오차를 없애기 위해 0.01 초간 쉼
        try {
			Thread.sleep(10);
		} catch (InterruptedException e) { } 
        
        String serialNo = "" + System.currentTimeMillis();
        serialNo = serialNo.substring(5, 12);
        
        return TODAY_YMD.format(new Date()) + "_" + serialNo;
	}
	
	// 랜덤 번호 생성
	public String getRandom(int size) {
		String strTemp = "";
		
		for (int i = 0; i < size; i++) {
			Random rnd = new Random();
			strTemp += rnd.nextInt(9);
		}
		
		return strTemp;
	}
	
}
