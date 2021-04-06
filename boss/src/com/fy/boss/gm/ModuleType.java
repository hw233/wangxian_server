package com.fy.boss.gm;

import java.util.ArrayList;
import java.util.List;

import com.fy.boss.gm.XmlModule;

public class ModuleType {
	private int id;
    private String name;
    private String memo;
    private List<XmlModule> modules = new ArrayList<XmlModule>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<XmlModule> getModules() {
		return modules;
	}
	public void setModules(List<XmlModule> modules) {
		this.modules = modules;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
    
  
}
