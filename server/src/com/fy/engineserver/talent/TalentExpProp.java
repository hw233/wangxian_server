package com.fy.engineserver.talent;

public class TalentExpProp {

	private String name;
	private int level;
	private long exp;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	@Override
	public String toString() {
		return "TalentExpProp [exp=" + exp + ", level=" + level + ", name="
				+ name + "]";
	}
	
}
