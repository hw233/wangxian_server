package com.fy.engineserver.worldmap;

import java.util.ArrayList;

/**
 * 地图上的怪物的一些简单信息，需要策划编辑，世界地图显示使用
 * 
 *
 */
public class MapSingleMonsterInfo{

	String[] monsterNames = new String[0];
	int[] monsterLv = new int[0];
	short[] monsterx = new short[0];
	short[] monstery = new short[0];
	
	String mapName = "";
	String displayMapName = "";
	ArrayList<String> monsterNamesList = new ArrayList<String>();
	ArrayList<Integer> monsterLvList = new ArrayList<Integer>();
	ArrayList<Short> monsterxList = new ArrayList<Short>();
	ArrayList<Short> monsteryList = new ArrayList<Short>();
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public String getDisplayMapName() {
		return displayMapName;
	}
	public void setDisplayMapName(String displayMapName) {
		this.displayMapName = displayMapName;
	}
	public String[] getMonsterNames() {
		return monsterNames;
	}
	public void setMonsterNames() {
		this.monsterNames = monsterNamesList.toArray(new String[0]);
	}
	public int[] getMonsterLv() {
		return monsterLv;
	}
	public void setMonsterLv() {
		this.monsterLv = new int[monsterLvList.size()];
		for(int i = 0; i < monsterLvList.size(); i++){
			this.monsterLv[i] = monsterLvList.get(i).intValue();
		}
	}
	public short[] getMonsterx() {
		return monsterx;
	}
	public void setMonsterx() {
		this.monsterx = new short[monsterxList.size()];
		for(int i = 0; i < monsterxList.size(); i++){
			this.monsterx[i] = monsterxList.get(i).shortValue();
		}
	}
	public short[] getMonstery() {
		return monstery;
	}
	public void setMonstery() {
		this.monstery = new short[monsteryList.size()];
		for(int i = 0; i < monsteryList.size(); i++){
			this.monstery[i] = monsteryList.get(i).shortValue();
		}
	}
	public ArrayList<String> getMonsterNamesList() {
		return monsterNamesList;
	}
	public void setMonsterNamesList(ArrayList<String> monsterNamesList) {
		this.monsterNamesList = monsterNamesList;
	}
	public ArrayList<Integer> getMonsterLvList() {
		return monsterLvList;
	}
	public void setMonsterLvList(ArrayList<Integer> monsterLvList) {
		this.monsterLvList = monsterLvList;
	}
	public ArrayList<Short> getMonsterxList() {
		return monsterxList;
	}
	public void setMonsterxList(ArrayList<Short> monsterxList) {
		this.monsterxList = monsterxList;
	}
	public ArrayList<Short> getMonsteryList() {
		return monsteryList;
	}
	public void setMonsteryList(ArrayList<Short> monsteryList) {
		this.monsteryList = monsteryList;
	}

}
