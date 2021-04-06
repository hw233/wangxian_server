package com.xuanzhi.tools.authorize;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

public class User {
	static Logger logger = Logger.getLogger(UserManager.class);
	
	String name;
	String realName;
	String email;
	String password;
	Date createDate;
	Date passwordExpiredTime;
	
	protected ArrayList<Role> roleList = new ArrayList<Role>();
	
	protected Properties props = new Properties();
	
	protected User(String name,String realName,String email){
		this.name = name;
		this.realName = realName;
		this.email = email;
	}
	
	public Properties getProperties(){
		return props;
	}
	
	public boolean isRoleExists(Role r){
		return roleList.contains(r);
	}
	
	public Role[] getRoles(){
		return roleList.toArray(new Role[0]);
	}
	
	public void addRole(Role r){
		if(isRoleExists(r) == false){
			roleList.add(r);
			logger.warn("[addrole-to-user] ["+name+"] ["+realName+"] ["+r.getId()+"] ["+r.getName()+"]");
		}
	}
	
	public void removeRole(Role r){
		if(roleList.remove(r)){
			logger.warn("[removerole-from-user] ["+name+"] ["+realName+"] ["+r.getId()+"] ["+r.getName()+"]");
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}

	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getPasswordExpiredTime() {
		return passwordExpiredTime;
	}
	public void setPasswordExpiredTime(Date passwordExpiredTime) {
		this.passwordExpiredTime = passwordExpiredTime;
	}

}
