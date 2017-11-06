package com.depression.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.depression.model.SystemUserInfo;

class MyUserDetails implements UserDetails{

	private Collection<? extends GrantedAuthority> authorities;
	private String username;
	private String password;
	private boolean enable;
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return enable;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return enable;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return enable;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enable;
	}
	
}

public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	SystemUserInfoService userInfoService;

	@Override
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		MyUserDetails userDetails = new MyUserDetails();
		SystemUserInfo userInfo = userInfoService.getSystemUserInfo(arg0);
		if(userInfo==null) throw new UsernameNotFoundException("username not found");
		List<GrantedAuthority> gas = new ArrayList<GrantedAuthority>();
		gas.add(new SimpleGrantedAuthority("ROLE_APP"));
		userDetails.setAuthorities(gas);
		userDetails.setUsername(userInfo.getUsername());
		userDetails.setPassword(userInfo.getUserPsw());
		userDetails.setEnable(userInfo.getIsEnable()==0);
		return userDetails;
	}

}
