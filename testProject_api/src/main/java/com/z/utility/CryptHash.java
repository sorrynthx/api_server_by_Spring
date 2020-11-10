package com.z.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptHash {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String getMD5(String strPlanText){
		return getEncryption(strPlanText, "MD5");
	}
 
	public String getSHA256(String strPlanText){
		return getEncryption(strPlanText, "SHA-256");
	}
	
	private String getEncryption(String strPlanText, String METHOD){
		String rtnData = "";
		
		try {
			MessageDigest md = MessageDigest.getInstance(METHOD);
			
			md.update(strPlanText.getBytes());
			byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for(byte byteTmp : byteData) {
            	sb.append(Integer.toString((byteTmp&0xff) + 0x100, 16).substring(1));
            }
            
            rtnData = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.info("getEncryption->" + e.getMessage());
			rtnData = "";
		}

		//logger.info("Encryption->" + rtnData);
		return rtnData;
	}
}
