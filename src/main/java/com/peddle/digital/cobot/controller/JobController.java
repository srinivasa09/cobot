package com.peddle.digital.cobot.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.io.IOUtils;
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

    @Autowired
    JobRepository jobRepository;
    
    @PostMapping("/job")
    public JobResponse createJob(HttpServletRequest servletRequest) throws IOException {
    	String content = IOUtils.toString(servletRequest.getReader());
    	
    	Job job = new Job();
    	job.setStatus(STATUS.IN_PROCESS.toString());
    	job.setJobStatusCode(STATUS.IN_PROCESS.getID());
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

//    @PostMapping("/job")
//    public Job createNote(@Valid @RequestBody Job note) {
//    	
//        return jobRepository.save(note);
//    }

    @GetMapping("/notes/{id}")
    public Job getNoteById(@PathVariable(value = "id") Long noteId) {
        return jobRepository.findById(noteId)
                .orElseThrow(() -> new com.peddle.digital.cobot.model.ResourceNotFoundException("Note", "id", noteId));
    }

    @PutMapping("/notes/{id}")
    public Job updateNote(@PathVariable(value = "id") Long noteId,
                                           @Valid @RequestBody Job noteDetails) {

        Job note = jobRepository.findById(noteId)
                .orElseThrow(() -> new com.peddle.digital.cobot.model.ResourceNotFoundException("Note", "id", noteId));

        //note.setTitle(noteDetails.getTitle());
        //note.setContent(noteDetails.getContent());

        Job updatedNote = jobRepository.save(note);
        return updatedNote;
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        Job note = jobRepository.findById(noteId)
                .orElseThrow(() -> new com.peddle.digital.cobot.model.ResourceNotFoundException("Note", "id", noteId));

        jobRepository.delete(note);

        return ResponseEntity.ok().build();
    }
}
