package com.fy.engineserver.talent;

public class TalentPageInfo {

	
	private int level; 
	private int phyAttact; 
	private int magicAttact; 
	private int phyDefence; 
	private int magicDefence; 
	private int hp; 
	private long currExp;
	private long upgradeExp; 
	private String talentMess;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPhyAttact() {
		return phyAttact;
	}
	public void setPhyAttact(int phyAttact) {
		this.phyAttact = phyAttact;
	}
	public int getMagicAttact() {
		return magicAttact;
	}
	public void setMagicAttact(int magicAttact) {
		this.magicAttact = magicAttact;
	}
	public int getPhyDefence() {
		return phyDefence;
	}
	public void setPhyDefence(int phyDefence) {
		this.phyDefence = phyDefence;
	}
	public int getMagicDefence() {
		return magicDefence;
	}
	public void setMagicDefence(int magicDefence) {
		this.magicDefence = magicDefence;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public long getCurrExp() {
		return currExp;
	}
	public void setCurrExp(long currExp) {
		this.currExp = currExp;
	}
	public long getUpgradeExp() {
		return upgradeExp;
	}
	public void setUpgradeExp(long upgradeExp) {
		this.upgradeExp = upgradeExp;
	}
	public String getTalentMess() {
		return talentMess;
	}
	public void setTalentMess(String talentMess) {
		this.talentMess = talentMess;
	}
	@Override
	public String toString() {
		return "[currExp=" + currExp + ", hp=" + hp + ", level="
				+ level + ", magicAttact=" + magicAttact + ", magicDefence="
				+ magicDefence + ", phyAttact=" + phyAttact + ", phyDefence="
				+ phyDefence + ", talentMess=" + talentMess + ", upgradeExp="
				+ upgradeExp + "]";
	}
	
	
}
