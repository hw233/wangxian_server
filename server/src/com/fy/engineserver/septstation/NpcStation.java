package com.fy.engineserver.septstation;

import java.util.ArrayList;
import java.util.List;

/**
 * NPC 在模板中的位置和索引 {家族驻地可建造的位置}
 * 
 * 
 */
public class NpcStation {

	private int x;
	private int y;
	private int index;
	private int builingType;// 可建造的建筑类型
	private int npcTempletId;// NPC模板ID
	private String buildingName;
	private String des;
	private String icon;
	private List<GroundNpcStation> groundNpcStation = new ArrayList<GroundNpcStation>();

	public NpcStation() {

	}

	public NpcStation(int x, int y, int index, int builingType, int npcTempletId, String buildingName, String des) {
		this.x = x;
		this.y = y;
		this.index = index;
		this.builingType = builingType;
		this.npcTempletId = npcTempletId;
		this.buildingName = buildingName;
		this.des = des;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getBuilingType() {
		return builingType;
	}

	public void setBuilingType(int builingType) {
		this.builingType = builingType;
	}

	public int getNpcTempletId() {
		return npcTempletId;
	}

	public void setNpcTempletId(int npcTempletId) {
		this.npcTempletId = npcTempletId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<GroundNpcStation> getGroundNpcStation() {
		return groundNpcStation;
	}

	public void setGroundNpcStation(List<GroundNpcStation> groundNpcStation) {
		this.groundNpcStation = groundNpcStation;
	}

	@Override
	public String toString() {
		return "NpcStation [x=" + x + ", y=" + y + ", index=" + index + ", builingType=" + builingType + ", npcTempletId=" + npcTempletId + ", buildingName=" + buildingName + ", des=" + des + ", icon=" + icon + ", groundNpcStation=" + groundNpcStation + "]";
	}
}
