package com.background;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
 

/*
 *  서비스 실행 전에 @WebListener 어노테이션을 사용하면 web.xml에 등록하지 않고 자동으로 실행
 *  
 *  Gradle 추가
 *  providedCompile group: 'javax.servlet', name: 'javax.servlet-api', version: '3.1.0'
 *  
 * */

@WebListener
public class AppListener implements ServletContextListener {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
		
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		logger.info("AppListener Start API Server...");
		
		// 3초마다 실행
		Timer jobScheduler = new Timer(true);
		ScheduledJob job = new ScheduledJob(jobScheduler);
		jobScheduler.scheduleAtFixedRate(job, 1000, 3000);
				
		// 스레드 1
		Thread thread1 = new Thread(new AppThread1("test_thread1"));
		thread1.setDaemon(true);

		// 스레드 2		
		Thread thread2 = new Thread(new AppThread2("test_thread2"));
		thread2.setDaemon(true);
		
		try {
			// 10초뒤 출발
			Thread.sleep(10000);
			thread1.start();
			thread2.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("end before destroy...");
	}
	
}
