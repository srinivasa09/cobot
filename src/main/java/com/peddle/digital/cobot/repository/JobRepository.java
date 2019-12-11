package com.peddle.digital.cobot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.peddle.digital.cobot.model.Job;

/**
 * @author Srinivasa Reddy Challa
 *
 */

@Repository
public interface JobRepository extends JpaRepository<Job, Long>,JobRepositoryCustom {
	
	Job findFirst1ByStatusAndRemoteAgentIPIsNotNull(String status);
	Job findFirst1ByStatusAndRemoteAgentIPIsNull(String status);
	//Job findByCustomerAndUsedOnIsNull(Customer);
	
}


