package com.small.sbtest;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.small.bdp.core.context.Context;
import com.small.bdp.core.context.IContext;
import com.small.sbtest.system.domain.UserInfo;
import com.small.sbtest.system.service.IUserService;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationMain.class)
@SpringBootApplication
public class AppTest {
	@Autowired
	private IUserService userService;

	@Test
	public void insert() {
		IContext ctx = Context.createDefaultContext();
		UserInfo usr = new UserInfo();
		usr.setName("cccc");
		usr.setEmail("3604545@email.com");
		userService.save(ctx, usr);
		assertNotNull(usr.getFid());
	}
}
