package com.background;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppThread2 implements Runnable {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
		
	private String threadName;
	private int cnt = 0;
	
	public AppThread2(String threadName) {
		this.threadName = threadName;
	}
	
	@Override
	public void run() {
		logger.info(Thread.currentThread().getName() + " 의 " + threadName + " 이 시작되었습니다.");
		
		while (true) {
			logger.info("======= > " + threadName + " 스레드 실행중입니다......");

			try {
				// 60초 딜레이
				Thread.sleep(60000);
				
				cnt++;
				
				if (cnt > 9) {
					Thread.interrupted();
					break;
				} 
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				break; 	// Exception 발생 시, while문 탈출
			}
			
		}
		
	}
}
