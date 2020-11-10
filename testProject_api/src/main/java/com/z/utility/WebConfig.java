package com.z.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebConfig {
	private Logger logger = LoggerFactory.getLogger(this.getClass());	
	static Properties properties;

	public WebConfig() {
		if (properties == null) {
			properties = new Properties();
		}
	}
	
	public String getValue(String key) {
		return properties.getProperty(key);
	}
	
	public void setValue(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public void initalize(String fileLocation) {
		logger.info("initalize - config file.");
		properties = new Properties();
		
		try {
			InputStream stream = new FileInputStream(fileLocation);
			properties.load(new InputStreamReader(stream, "UTF-8"));
		} catch (FileNotFoundException e) {
			logger.info("FileNotFoundException->" + e.getMessage());
		} catch (IOException e) {
			logger.info("IOException->" + e.getMessage());
		}
	}
}
