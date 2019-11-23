package com.peddle.digital.cobot.Util;

import org.apache.log4j.Logger;

import com.peddle.digital.cobot.constants.STATUS;
import com.peddle.digital.cobot.controller.JobResponse;
import com.peddle.digital.cobot.model.Job;

public class JobUtil {
	final static Logger logger = Logger.getLogger(JobUtil.class);
	public static final String JOB_PREFIX="JOB-";
	
	public static JobResponse updateResponse(Job Job,String appJobId)
	{
		JobResponse jobResponse = new JobResponse();
		if(Job == null)
		{
			jobResponse.setJobid(appJobId);
			jobResponse.setJobStatus(STATUS.UNKNOWN.toString());
			jobResponse.setJobStatusCode(STATUS.UNKNOWN.getID());
		}
		
		jobResponse.setJobid(JOB_PREFIX+Job.getId());
		jobResponse.setJobStatus(Job.getStatus().toString());
		jobResponse.setJobStatusCode(Job.getJobStatusCode());
		jobResponse.setJobUpdatedAt(Job.getUpdatedAt());
		
		return jobResponse;
	}
	
	public static String getAppJobIDFromDBJobID(Long dbJobId)
	{
		return JOB_PREFIX+dbJobId;
	}
	
	public static Long getDBJobIDFromAppJobID(String appJobId)
	{
		if( appJobId != null)
		{
			String arr[] =appJobId.split("-");
			if(arr.length!=2)
			{
				logger.error("invalid appJobId" +appJobId );
				return -1L;
			}
			return Long.parseLong(arr[1]);
		}
		else
		{
			logger.error("invalid appJobId" +appJobId );
			return -1L;
		}
	}
}
