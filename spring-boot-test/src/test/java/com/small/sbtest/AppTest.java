package com.small.sbtest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.small.bdp.core.context.Context;
import com.small.bdp.core.context.IContext;
import com.small.sbtest.system.domain.UserInfo;
import com.small.sbtest.system.service.IUserService;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationMain.class)
@WebAppConfiguration
@Rollback
public class AppTest {

	@Autowired
	protected WebApplicationContext wac;

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void insert() throws Exception {
		// 测试UserController
		RequestBuilder request = null;

		// 1、get查一下user列表，应该为空
		request = get("/user/")/*
								 * .with(user("admin").password("pass").roles("USER"
								 * , "ADMIN"))
								 */;
		ResultActions ra = mvc.perform(request);
		ra.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

		UserInfo usr = new UserInfo();
		usr.setName("cccc");
		usr.setEmail("3604545@email.com");

	}

	@Autowired
	@Qualifier("userServiceImpl")
	private IUserService userService;

	@Test
	public void insertbatch() throws Exception {
		List<UserInfo> usr = new ArrayList<UserInfo>();
		String email = UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
		for (int i = 0; i < 1000; i++) {
			UserInfo u = new UserInfo();
			String pre = email.concat("-").concat(i + "");
			u.setEmail(pre.concat("@126.com"));
			u.setName(pre);
			usr.add(u);
		}
		IContext ctx = Context.createDefaultContext();
		userService.batchPersist(ctx, usr);
	}
}
