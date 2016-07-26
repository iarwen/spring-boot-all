package com.small.sbtest.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.small.bdp.core.context.Context;
import com.small.bdp.core.context.IContext;
import com.small.bdp.framework.exception.BizException;
import com.small.sbtest.system.domain.UserInfo;
import com.small.sbtest.system.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(path = "/index", method = { RequestMethod.GET })
	public String index(HttpServletRequest request) {
		return "hello.html";
	}

	@Autowired
	@Qualifier("userServiceImpl")
	private IUserService userService;

	@RequestMapping(path = "/save", method = { RequestMethod.POST })
	public UserInfo view(HttpServletRequest request, UserInfo user) {
		IContext ctx = Context.createDefaultContext();
		userService.save(ctx, user);
		throw new BizException("sdf");
	}

	@RequestMapping(path = "/{id}", method = { RequestMethod.GET })
	public UserInfo view(HttpServletRequest request, @PathVariable("id") String id) {
		logger.info("请求：" + id);
		IContext ctx = Context.createDefaultContext();
		return userService.findById(ctx, id);
	}

	@RequestMapping("/get-by-email")
	public @ResponseBody UserInfo getByEmail(HttpServletRequest request, String email) {
		IContext ctx = Context.createDefaultContext();
		UserInfo user = userService.findByFieldSingle(ctx, "email", email);
		if (user != null) {
			return user;
		}
		throw new BizException("nodata found");
	}
}
