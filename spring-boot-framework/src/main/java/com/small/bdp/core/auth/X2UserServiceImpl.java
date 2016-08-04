package com.small.bdp.core.auth;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 多用户登陆服务处理
 * 
 * @author xiaojiao_li
 * 
 */
public class X2UserServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {

		X2User user = new X2User("aa", "admin", new Md5PasswordEncoder().encodePassword("admin", "admin"), 0);
		user.setUserObject(null);

		return user;
	}
}
