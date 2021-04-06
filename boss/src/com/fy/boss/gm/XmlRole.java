package com.fy.boss.gm;

import java.util.List;

import com.fy.boss.gm.Module;

public class XmlRole {
	private String id;
	private String name;
	private List<String> servers;
	private List<Module> modules;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getServers() {
		return servers;
	}
	public void setServers(List<String> servers) {
		this.servers = servers;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Module> getModules() {
		return modules;
	}
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
}
