package com.peddle.digital.cobot.Service;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cobot.utils.JiraAddUserTest;
import com.peddle.digital.cobot.Util.Base64Utils;
import com.peddle.digital.cobot.constants.STATUS;
import com.peddle.digital.cobot.model.Job;
import com.peddle.digital.cobot.repository.JobRepository;

@Component
public class DispatcherService {
	
    @Autowired
    JobRepository jobRepository;

	public void processJob(String content, String appJobID, Job job) throws IOException {

		JSONObject obj = new JSONObject(content);
		String targetSystem = obj.getString("TargetSystemURL");
		if(targetSystem.contains("jira"))
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
					jobRepository.updateStatus(STATUS.EXECUTION_FAILED.toString(),job.getId());
				}
				else
				{
					JiraAddUserTest jira = new JiraAddUserTest();
					jira.test(split[0], split[1], newUser,targetSystem);
				}
			}
		}
	}
}
