package com.small.bdp.core.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class X2User implements UserDetails {

	private static final long serialVersionUID = 8416719507199200970L;

	private String userId;
	private String user;
	private String password;
	private int status;
	private Object userObject;
	private Map<String, Object> params = new HashMap<String, Object>();

	public X2User(String userId, String user, String password, int status) {
		super();
		this.userId = userId;
		this.user = user;
		this.password = password;
		this.status = status;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> gas = new ArrayList<GrantedAuthority>();
		gas.add(new SimpleGrantedAuthority("ROLE_USER"));
		return gas;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.user;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 0为启用，不为0则禁用。
		return status == 0;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	public Object getParam(String key) {
		return params.get(key);
	}

	public void putParam(String key, Object value) {
		this.params.put(key, value);
	}

}
