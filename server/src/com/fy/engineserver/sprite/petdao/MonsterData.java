package com.fy.engineserver.sprite.petdao;

import java.io.Serializable;
import java.util.ArrayList;


public class MonsterData implements Serializable{

	private long  pid;	//当前怪物拥有人
	
	private ArrayList<Integer> monsterids; //怪物id集
	
	private ArrayList<Integer []> xys;		//怪物坐标

	public MonsterData(){}
	
	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public ArrayList<Integer> getMonsterids() {
		return monsterids;
	}

	public void setMonsterids(ArrayList<Integer> monsterids) {
		this.monsterids = monsterids;
	}

	public ArrayList<Integer[]> getXys() {
		return xys;
	}

	public void setXys(ArrayList<Integer[]> xys) {
		this.xys = xys;
	}
	
}
