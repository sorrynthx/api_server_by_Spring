package com.background;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledJob extends TimerTask {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	JdbcConnection jdbcConnection = new JdbcConnection();
	
	private int cnt = 0;
	private Timer jobScheduler;
	
	public ScheduledJob(Timer jobScheduler) {
		this.jobScheduler = jobScheduler;
	}
	
	@Override
	public void run() {
		
		logger.info("class ScheduledJob run()... " + new Date());
		
		// Mysql connection, testQuery, close
		try {
			
			if (cnt < 9) {
				
				jdbcConnection.getConnection();
				jdbcConnection.getTestQuery();
				jdbcConnection.closeConnection();
				
				cnt++;
				
			} else {
				// Timer 취소
				jobScheduler.cancel();	
			}
			
		} catch (Exception e) {
			logger.info("ScheduledJob Error : " + e);
		}
		
	}
	
}
