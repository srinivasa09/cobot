package com.peddle.digital.cobot.repository;



public interface JobRepositoryCustom {
	
	int updateStatus( String status,  Long id);

	int updateStatusAndReason(String status, String reason, Long id);
}
