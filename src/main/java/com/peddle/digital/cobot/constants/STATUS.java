package com.peddle.digital.cobot.constants;

public enum STATUS{

	IN_PROCESS(100), 
	BOT_INVOKED(101),
	RETRY_INPROGRESS(102),
	JOB_COMPLETED(200),
	TARGET_SYSTEM_UNREACHABLE(500),
	EXECUTION_FAILED(501), 
	EXECUTION_TIMEOUT(502),
	UNKNOWN(503); 

	private int id;

	STATUS(int id){
		this.id = id;
	}

	public int getID(){
		return id;
	}
}