package com.peddle.digital.cobot.controller;

import java.util.Date;

public class JobResponse {
	
	String jobid;
	int JobStatusCode;
	String jobStatus;
	String jobStatusCause;
	Date jobUpdatedAt;
	
	
	public Date getJobUpdatedAt() {
		return jobUpdatedAt;
	}
	public void setJobUpdatedAt(Date jobUpdatedAt) {
		this.jobUpdatedAt = jobUpdatedAt;
	}
	public String getJobid() {
		return jobid;
	}
	public void setJobid(String jobid) {
		this.jobid = jobid;
	}
	public int getJobStatusCode() {
		return JobStatusCode;
	}
	public void setJobStatusCode(int jobStatusCode) {
		JobStatusCode = jobStatusCode;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getJobStatusCause() {
		return jobStatusCause;
	}
	public void setJobStatusCause(String jobStatusCause) {
		this.jobStatusCause = jobStatusCause;
	}
}
