package com.fy.engineserver.activity.wolf;

public class WolfBattleInfo {

	private long id;
	private boolean dead;
	private long exps;
	//0:羊，1:狼
	private int battleType;
	private String name;
	private int career;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public long getExps() {
		return exps;
	}
	public void setExps(long exps) {
		this.exps = exps;
	}
	public int getBattleType() {
		return battleType;
	}
	public void setBattleType(int battleType) {
		this.battleType = battleType;
	}
	@Override
	public String toString() {
		return "WolfBattleInfo [battleType=" + battleType + ", career="
				+ career + ", dead=" + dead + ", exps=" + exps + ", id=" + id
				+ ", name=" + name + "]";
	}
	
}
