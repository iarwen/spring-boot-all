package com.small.sbtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.small")
@ConfigurationProperties(prefix = "spring.application")
public class ApplicationMain {
	private static Logger logger = LoggerFactory.getLogger(ApplicationMain.class);

	private static String name;

	public static void main(String[] args) {
		SpringApplication.run(ApplicationMain.class, args);
		logger.debug(name + " started success");
	}

	public static void setName(String name) {
		ApplicationMain.name = name;
	}

}
