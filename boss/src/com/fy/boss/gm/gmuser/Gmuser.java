package com.fy.boss.gm.gmuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fy.boss.gm.gmuser.Authority;
import com.xuanzhi.tools.text.StringUtil;

/**
 * Gmuser entity. @author MyEclipse Persistence Tools
 */

public class Gmuser implements java.io.Serializable {

	// Fields

	private Integer id;
	private String username;
	private String password;
	private String realname;
	private String gmname;
	private String props;
    private List<Authority> authos= new ArrayList<Authority>();
    private Map<String, String> properties;
	// Constructors

	/** default constructor */
	public Gmuser() {
	}

	/** full constructor */
	public Gmuser(String username, String password, String realname) {
		this.username = username;
		this.password = password;
		this.realname = realname;
	}
    
	// Property accessors


	@Override
	public String toString() {
		return "Gmuser [authos=" + authos + ", gmname=" + gmname + ", id=" + id
				+ ", password=" + password + ", realname=" + realname
				+ ", username=" + username + "]";
	}

	public String getUsername() {
		return this.username;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public List<Authority> getAuthos() {
		return authos;
	}

	public void setAuthos(List<Authority> authos) {
		this.authos = authos;
	}

	public String getGmname() {
		return gmname;
	}

	public void setGmname(String gmname) {
		this.gmname = gmname;
	}

	public String getProps() {
		return props;
	}

	public void setProps(String props) {
		this.props = props;
		if(props != null) {
			this.properties = StringUtil.stringToMap(props);
		}
	}
   
	public String getProperty(String key) {
		if(properties != null) {
			return properties.get(key);
		}
		return null;
	}
	
	public void setProperty(String key, String value) {
		if(properties == null) {
			properties = new HashMap<String,String>();
		}
		properties.put(key, value);
		props = StringUtil.mapToString(properties);
	}
	
	public Map<String,String> getProperties() {
		return properties;
	}
   
}
