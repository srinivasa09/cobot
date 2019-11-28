package com.peddle.digital.cobot.Service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.peddle.digital.cobot.Util.Base64Utils;
import com.peddle.digital.cobot.constants.STATUS;
import com.peddle.digital.cobot.constants.TargetSystems;
import com.peddle.digital.cobot.model.Job;
import com.peddle.digital.cobot.repository.JobRepository;

@Component
public class DispatcherService {
	
    @Autowired
    JobRepository jobRepository;

	public void processJob(String content, String appJobID, Job job) throws Exception {

		JSONObject obj = new JSONObject(content);
		String targetSystem = obj.getString("TargetSystemURL");
		
		if(targetSystem.contains(TargetSystems.JIRA))
		{
			String adminCred = obj.getString("AdminCredentails");
			String newUser = obj.getString("NewUser");

			String decode = Base64Utils.decode(adminCred);
			String[] split = decode.split(":");

			if(split.length ==2)
			{
				String adminUser =  split[0];
				String adminPass =  split[1];
				if(adminUser== null ||  adminPass == null)
				{
					jobRepository.updateStatus(STATUS.Failed.toString(),job.getId());
				}
				else
				{
					 Map<String, Object> inputData = new HashMap<String, Object>();
					 inputData.put("url", targetSystem);
				     inputData.put("adminUserName", adminUser);
				     inputData.put("adminPassword", adminPass);
				     inputData.put("newUser", newUser);
				     inputData.put("jobId", appJobID);
					 com.cobot.testcases.JiraAddUserTest jira = new com.cobot.testcases.JiraAddUserTest();
					 jira.test(content,appJobID);
				}
			}
		}
		
	}
}
