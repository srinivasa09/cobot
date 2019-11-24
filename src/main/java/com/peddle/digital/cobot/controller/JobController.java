package com.peddle.digital.cobot.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.peddle.digital.cobot.Util.Base64Utils;
import com.peddle.digital.cobot.Util.JobUtil;
import com.peddle.digital.cobot.constants.STATUS;
import com.peddle.digital.cobot.model.Job;
import com.peddle.digital.cobot.repository.JobRepository;


/**
 * @author Srinivasa Reddy Challa
 *
 */


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class JobController {
	
	final static Logger logger = Logger.getLogger(JobController.class);

    @Autowired
    JobRepository jobRepository;
    
    @PostMapping("/job")
    public JobResponse createJob(HttpServletRequest servletRequest) throws IOException {
    	String content = IOUtils.toString(servletRequest.getReader());
    	
    	Job job = new Job();
    	job.setStatus(STATUS.Submitted.toString());
    	job.setJobStatusCode(STATUS.Submitted.getID());
    	job.setContent(content);
    	Job saveJob = jobRepository.save(job);
    	JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
    	return updateResponse;
    }
    
    @PostMapping("/job/status/{id}")
    public JobResponse getJobStatus(@PathVariable(value = "id") String appJobId) {
    	Long jobDBId = JobUtil.getDBJobIDFromAppJobID(appJobId);
    	Optional<Job> optionalJOb = jobRepository.findById(jobDBId);
    	JobResponse updateResponse;
    	if(optionalJOb.isPresent()) {
    		Job job = optionalJOb.get();
    		 updateResponse = JobUtil.updateResponse(job, null);
    	}
    	else
    	{
    		updateResponse = JobUtil.updateResponse(null, appJobId);
    	}
    	return updateResponse;
    }
    
    @PostMapping("/job/updatestatus")
    public ResponseEntity<?> updateJobStatus(@RequestBody Status jobStatus) {
    	logger.info("Recieved Job Update Rquest for JOB"  + jobStatus.jobId );
    	Long jobDBId = JobUtil.getDBJobIDFromAppJobID(jobStatus.getJobId());
    	jobRepository.updateStatusAndReason(jobStatus.getStatus(),jobStatus.getReason(), jobDBId);
    	return ResponseEntity.ok().build();
    }
}
