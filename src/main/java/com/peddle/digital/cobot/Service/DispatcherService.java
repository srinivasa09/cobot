package com.peddle.digital.cobot.Service;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.peddle.digital.cobot.Util.ScriptUtil;
import com.peddle.digital.cobot.constants.TargetSystems;
import com.peddle.digital.cobot.model.Job;
import com.peddle.digital.cobot.repository.JobRepository;

import io.github.bonigarcia.wdm.WebDriverManager;


@Component
public class DispatcherService {

    final static Logger logger = Logger.getLogger(DispatcherService.class);

    @Autowired
    JobRepository jobRepository;

    @Autowired
    private Environment env;

    public void processJob(String content, String appJobID, Job job) throws Exception {

	JSONObject obj = new JSONObject(content);
	String targetSystem =null;

	if (obj.has("TargetSystemURL")) {
	    targetSystem = obj.getString("TargetSystemURL");
	}

	if(targetSystem != null && targetSystem.contains(TargetSystems.JIRA))
	{
	    String statusCallBackUrl = env.getProperty("cobot.status.callback.url");
	    com.cobot.testcases.JiraAddUserTest jira = new com.cobot.testcases.JiraAddUserTest();
	    jira.test(content, appJobID, statusCallBackUrl);
	}

	if(job.getScriptFileName() != null)
	{
	    WebDriverManager.firefoxdriver().setup();
	    String scriptsDir = env.getProperty("scripts.dir");
	    String scriptRootDir = env.getProperty("testcases.root");
	    
	    ScriptUtil.transformScriptFile(job.getScriptFileName(), scriptsDir, scriptRootDir);
	    ScriptUtil.compileScriptFile(job.getScriptFileName(), scriptRootDir);
	    ScriptUtil.runScriptFile(job.getScriptFileName(), content, scriptRootDir);
	}
    }
}
