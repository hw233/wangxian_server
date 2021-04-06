package com.xuanzhi.tools.authorize;

import java.util.ArrayList;

public class Platform {

	public static final int NO_PERMISSION = 0;
	public static final int PART_PERMISSION = 1;
	public static final int WHOLE_PERMISSION = 2;
	
	public static final String PERMISSION_NAMES[] = new String[]{"没有权限","部分权限","全部权限"};
	
	protected String id;
	protected String name;
	protected String url;
	
	protected ArrayList<Module> moduleList = new ArrayList<Module>();
	
	
	protected Platform(String id,String name,String url){
		this.id = id;
		this.name = name;
		this.url = url;
	}
	
	public Module getModule(String id){
		for(int i = 0 ; i < moduleList.size() ; i++){
			Module m = moduleList.get(i);
			if(m.id != null && m.id.equals(id)){
				return m;
			}
		}
		return null;
	}
	
	public boolean isModuleExists(String id){
		return (getModule(id) != null);
	}
	
	public Module[] getModules(){
		return moduleList.toArray(new Module[0]);
	}
	
	public Module addModule(String id,String name,String uri){
		Module m = getModule(id);
		if(m != null){
			if(name != null && !name.equals(m.name)){
				m.setName(name);
			}
			if(uri != null && !uri.equals(m.uri)){
				m.setUri(uri);
			}
			return m;
		}else{
			m = new Module(this,id,name,uri);
			moduleList.add(m);
			return m;
		}
	}
	
	public void removeModule(String id){
		Module m = getModule(id);
		if(m != null){
			moduleList.remove(m);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
