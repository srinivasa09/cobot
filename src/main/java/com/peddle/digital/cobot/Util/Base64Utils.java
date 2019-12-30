package com.peddle.digital.cobot.Util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;;

public class Base64Utils {
	
	final static Logger logger = Logger.getLogger(Base64Utils.class);

	public static String decode(String base64EncodedStr)
	{
		byte[] decoded = Base64.decodeBase64(base64EncodedStr);
		String decodedStr="";
		try
		{
			 decodedStr = new String(decoded, "UTF-8");
		}catch(Exception e)
		{
			logger.error("failed to decode Base64 String", e);
         e.printStackTrace();
		}

		return decodedStr;
	}
	
	public static void main(String s[])
	{
	
	}
}
