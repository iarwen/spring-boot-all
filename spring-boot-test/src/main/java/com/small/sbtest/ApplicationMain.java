package com.small.sbtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.small")
public class ApplicationMain {
	private static Logger logger = LoggerFactory.getLogger(ApplicationMain.class);

	private static String APP_NAME = "我的app";

	@Bean
	public Object testBean(PlatformTransactionManager platformTransactionManager) {
		System.out.println(">>>>>>>>>>" + platformTransactionManager.getClass().getName());
		return new Object();
	}

	public static void main(String[] args) {
		SpringApplication.run(ApplicationMain.class, args);
		logger.debug(APP_NAME + " started success");
	}

}
