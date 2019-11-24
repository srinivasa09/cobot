package com.peddle.digital.cobot.constants;

public enum STATUS{

	Submitted(100), 
	Inprocess(101),
	RetryInprogress(102),
	Success(200),
	TargetSystemUnrechable(500),
	Failed(501), 
	ExecutionTimedout(502),
	UNKNOWN(503); 

	private int id;

	STATUS(int id){
		this.id = id;
	}

	public int getID(){
		return id;
	}
}