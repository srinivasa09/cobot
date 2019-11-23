package com.peddle.digital.cobot.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.Modifying;

import com.peddle.digital.cobot.model.Job;

public class JobRepositoryImpl implements JobRepositoryCustom {
	
    @PersistenceContext
    EntityManager entityManager;

	@Override
	@Transactional
	public int updateStatus(String status, Long id) {
		
		  Job job = entityManager.find(Job.class, id);
		  job.setStatus(status);
		  
		 
		
		return 0;
	}

	

}
