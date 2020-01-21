package com.learnbind.ai.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.learnbind.ai.model.SysRights;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysUsers;

public class UserBean extends SysUsers implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 登录用户的所有角色
	 */
	private List<SysRoles> roleList = new ArrayList<>();
	
	/**
	 * 登录用户的所有权限
	 */
	private List<SysRights> rightList = new ArrayList<>();
	
	
	public List<SysRoles> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SysRoles> roleList) {
		this.roleList = roleList;
	}

	public List<SysRights> getRightList() {
		return rightList;
	}

	public void setRightList(List<SysRights> rightList) {
		this.rightList = rightList;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("ADMIN"));
        return auths;
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
		return true;
	}

	@Override
	public String toString() {
		return "UserBean [roleList=" + roleList + ", rightList=" + rightList + ", getRoleList()=" + getRoleList()
				+ ", getRightList()=" + getRightList() + ", getAuthorities()=" + getAuthorities()
				+ ", isAccountNonExpired()=" + isAccountNonExpired() + ", isAccountNonLocked()=" + isAccountNonLocked()
				+ ", isCredentialsNonExpired()=" + isCredentialsNonExpired() + ", isEnabled()=" + isEnabled() + "]";
	}

}