package com.xuanzhi.tools.timer;

public class ScheduleTasker {
	
	private String scheduleFile;
	private boolean openning;
	
	public void initialize() {
		long now = System.currentTimeMillis();
		if(openning) {
			Scheduler s = new Scheduler("Scheduler",scheduleFile);
			s.start();
		}
		System.out.println("["+this.getClass().getName()+"] ["+scheduleFile+"] [initialized] ["+(System.currentTimeMillis()-now)+"]");
	}

	public void setScheduleFile(String scheduleFile) {
		this.scheduleFile = scheduleFile;
	}

	public void setOpenning(boolean openning) {
		this.openning = openning;
	}
}
