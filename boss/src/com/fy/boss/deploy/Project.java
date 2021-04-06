package com.fy.boss.deploy;

import java.util.ArrayList;
import java.util.List;

public class Project {
	private String name;
	
	private String basedir;
	
	private List<Module> modules;
	
	private String game;
	
	@Override
	public String toString() {
		return "[basedir=" + basedir + ", game=" + game + ", name="
				+ name + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBasedir() {
		return basedir;
	}

	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public List<Module> getModules() {
		return modules;
	}

	public List<Module> getCommonModules() {
		List<Module> list = new ArrayList<Module>();
		for(int i=0; i<modules.size(); i++) {
			if(modules.get(i).isCommon()) {
				list.add(modules.get(i));
			}
		}
		return list;
	}
	
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
	public Module getModule(String name) {
		for(Module mod : modules) {
			if(mod.getName().equals(name)) {
				return mod;
			}
		}
		return null;
	}
	
	/**
	 * 获得本组的模块
	 * @param type
	 * @return
	 */
	public Module[] getModules(String type) {
		List<Module> mlist = new ArrayList<Module>();
		for(Module mod : modules) {
			if(mod.getType().equals(type)) {
				mlist.add(mod);
			}
		}
		return mlist.toArray(new Module[0]);
	}
	
	/**
	 * 获取所有模块分组
	 * @return
	 */
	public String[] getTypes() {
		List<String> mlist = new ArrayList<String>();
		for(Module mod : modules) {
			if(!mlist.contains(mod.getType())) {
				mlist.add(mod.getType());
			}
		}
		return mlist.toArray(new String[0]);
	}
	
}
