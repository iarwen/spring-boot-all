package com.small.sbtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ApplicationMain {
	private static Logger logger = LoggerFactory.getLogger(ApplicationMain.class);

	private static String APP_NAME = "我的app";

	public static void main(String[] args) {
		logger.debug("starting app：" + APP_NAME);
		SpringApplication.run(ApplicationMain.class);
		logger.debug(APP_NAME + " started success");
	}
}
