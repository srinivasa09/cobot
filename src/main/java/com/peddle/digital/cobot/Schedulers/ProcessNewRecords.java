package com.peddle.digital.cobot.Schedulers;

import org.springframework.beans.factory.annotation.Autowired;
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
public class ProcessNewRecords {
	
    @Autowired
    DispatcherService dispatcherService;
    
    @Autowired
    JobRepository jobRepository;
	
	@Async
	@Scheduled(fixedRateString ="${newJobInterval}", initialDelay=1000)
    public void scheduleFixedRateTaskAsync() throws Exception {
		
		Job job = jobRepository.findFirst1ByStatus(STATUS.Submitted.toString());
		if(job!=null)
		{
			jobRepository.updateStatus(STATUS.Inprocess.toString(),job.getId());
			String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
			dispatcherService.processJob(job.getContent(), appJobID,job);
		}
    }
}
