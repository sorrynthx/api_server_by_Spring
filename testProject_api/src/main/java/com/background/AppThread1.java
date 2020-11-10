package com.background;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppThread1 implements Runnable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
		
	private String threadName;
	JdbcConnection jdbcConnection = new JdbcConnection();
	
	public AppThread1(String threadName) {
		this.threadName = threadName;
	}
	
	@Override
	public void run() {
		logger.info(Thread.currentThread().getName() + " 의 " + threadName + " 이 시작되었습니다.");
		
		while (true) {
			logger.info("======= > " + threadName + " 스레드 실행중입니다......");
			
			try {
				// 10초 딜레이
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break; 	// Exception 발생 시, while문 탈출
			}
			
		}
	}

}
