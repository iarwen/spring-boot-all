package com.small.sbtest.service.impl;

import org.springframework.stereotype.Service;

import com.small.sbtest.domain.User;
import com.small.sbtest.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Override
	public User findById(long id) {
		User user = new User();
		user.setId(id);
		user.setName("zhang");
		return user;
	}

}
