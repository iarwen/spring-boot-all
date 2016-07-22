package com.small.sbtest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.small.sbtest.domain.User;
import com.small.sbtest.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("userServiceImpl")
	private IUserService userService;

	@RequestMapping("/{id}")
	public User view(@PathVariable("id") Long id) {
		logger.info("请求：" + id);
		return userService.findById(id);
	}

}
