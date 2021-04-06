package com.fy.engineserver.sprite.horse;

import java.io.Serializable;

public class HorseMessForGM implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2659049946069583934L;
	private String horseName = "";
	private String [] equipmentNames = new String[0];
	private String [] skillNames = new String[0];
	private String pinzhi = "";
	private int costXueMai;		//消耗血脉
	private int nextXueMai;		//下一个等级需要的血脉值
	private int addXueMail;		//增加血脉
	public String[] getEquipmentNames() {
		return this.equipmentNames;
	}
	public void setEquipmentNames(String[] equipmentNames) {
		this.equipmentNames = equipmentNames;
	}
	public String[] getSkillNames() {
		return this.skillNames;
	}
	public void setSkillNames(String[] skillNames) {
		this.skillNames = skillNames;
	}
	public String getPinzhi() {
		return this.pinzhi;
	}
	public void setPinzhi(String pinzhi) {
		this.pinzhi = pinzhi;
	}
	public int getCostXueMai() {
		return this.costXueMai;
	}
	public void setCostXueMai(int costXueMai) {
		this.costXueMai = costXueMai;
	}
	public int getNextXueMai() {
		return this.nextXueMai;
	}
	public void setNextXueMai(int nextXueMai) {
		this.nextXueMai = nextXueMai;
	}
	public int getAddXueMail() {
		return this.addXueMail;
	}
	public void setAddXueMail(int addXueMail) {
		this.addXueMail = addXueMail;
	}
	public String getHorseName() {
		return this.horseName;
	}
	public void setHorseName(String horseName) {
		this.horseName = horseName;
	}
	
}
