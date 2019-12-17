package com.peddle.digital.cobot.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;;

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
//		String decode = decode("bWFuYS5zYW50b3NoOTk5MUBnbWFpbC5jb206Rm9yZ290QDEyMzQ1");
//		String[] split = decode.split(":");
//		String jsonStr = "{\"TargetSystemURL\": \"https:\\/\\/riteams.atlassian.net\\/jira\\/your-work\",\"AdminCredentails\":\"bWFuYS5zYW50b3NoOTk5MUBnbWFpbC5jb206Rm9yZ290QDEyMzQ1\",\"NewUser\" : \"testuser10@gmail.com\"}";
//       
//		JSONObject obj = new JSONObject(jsonStr);
//		String pageName = obj.getString("TargetSystemURL");
//		String adminCred = obj.getString("AdminCredentails");
//		String newUser = obj.getString("NewUser");
//		
//		HashMap<String, String> map = new HashMap();
//		map.put("TargetSystemURL", pageName);
//		map.put("AdminUser", split[0]);
//		map.put("AdminPass", split[1]);
//		map.put("NewUser", newUser);
//
//        System.out.println(pageName);
//        System.out.println(adminCred);
//        System.out.println(newUser);
		
		String json = "{\"scriptFile\":\"test.java\",\"data\":[ \"Ford\", \"BMW\", \"Fiat\" ]}";
		
		JSONObject obj = new JSONObject(json);
		String scriptFile = obj.getString("scriptFile");
		 JSONArray jsonArray = obj.getJSONArray("data");
		 List<Object> list = jsonArray.toList();
		 List<String> data= new ArrayList<String> ();

		 for(Object a: list){
			 data.add(String.valueOf(a));
		    
		 }
		 System.out.println(data);
		 
		
	}
}
