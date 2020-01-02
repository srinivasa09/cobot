package com.peddle.digital.cobot.Schedulers;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.peddle.digital.cobot.Service.DispatcherService;
import com.peddle.digital.cobot.Util.JobUtil;
import com.peddle.digital.cobot.constants.STATUS;
import com.peddle.digital.cobot.model.Job;
import com.peddle.digital.cobot.repository.JobRepository;

@Component
@EnableAsync
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ProcesRemoteRecords {
	
    @Autowired
    DispatcherService dispatcherService;
    
    @Autowired
    JobRepository jobRepository;
    
	@Autowired
	private Environment env;
	
	@Async
	@Scheduled(fixedRateString ="${remoteJobInterval}", initialDelay=1000)
    public void scheduleFixedRateTaskAsync() throws Exception {
		
		Job job = jobRepository.findFirst1ByStatusAndRemoteAgentIPIsNotNull(STATUS.Submitted.toString());
		if(job!=null)
		{
			jobRepository.updateStatus(STATUS.Inprocess.toString(),job.getId());
			String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
			
			
			CloseableHttpClient httpclient = HttpClients.createDefault();
			
			String scriptsDir = env.getProperty("scripts.dir");
			String agentPort = env.getProperty("agent.port");
			String scriptUpload = env.getProperty("agent.scriptUploadUrl");
			String statusCallBackUrl = env.getProperty("cobot.status.callback.url");
			
			File file = new File(scriptsDir+job.getScriptFileName());
			
			String remoteHostUrl="http://"+job.getRemoteAgentIP()+":"+agentPort+scriptUpload;
			
			FileInputStream input = new FileInputStream(file);
			byte[] byteArray = IOUtils.toByteArray(input);
			
			HttpEntity entity = MultipartEntityBuilder
					.create()
					.addTextBody("JobId", appJobID)
					.addTextBody("CallBackURL", statusCallBackUrl)
					.addTextBody("Body", job.getContent())
					.addTextBody("FileName", job.getScriptFileName())
					.addBinaryBody("File",byteArray, ContentType.create("application/octet-stream"), "filename")
					.build();

			HttpPost httpPost = new HttpPost(remoteHostUrl);
			httpPost.setEntity(entity);
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity result = response.getEntity();
			System.out.println(EntityUtils.toString(result));
			
		}
    }

}
