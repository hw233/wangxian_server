package com.fy.engineserver.activity.fairyBuddha;

public class DefaultFairyNpcData {
	private byte career;
	private int npcId;
	private int pointX;
	private int pointY;
	private String mapName;
	private String title;
	private String horseCNName;

	public DefaultFairyNpcData(byte career, int npcId, int pointX, int pointY, String mapName, String title, String horseCNName) {
		this.career = career;
		this.npcId = npcId;
		this.pointX = pointX;
		this.pointY = pointY;
		this.mapName = mapName;
		this.title = title;
		this.horseCNName = horseCNName;
	}

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public int getPointX() {
		return pointX;
	}

	public void setPointX(int pointX) {
		this.pointX = pointX;
	}

	public int getPointY() {
		return pointY;
	}

	public void setPointY(int pointY) {
		this.pointY = pointY;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHorseCNName() {
		return horseCNName;
	}

	public void setHorseCNName(String horseCNName) {
		this.horseCNName = horseCNName;
	}

	@Override
	public String toString() {
		return "DefaultFairyNpcData [career=" + career + ", horseCNName=" + horseCNName + ", mapName=" + mapName + ", npcId=" + npcId + ", pointX=" + pointX + ", pointY=" + pointY + ", title=" + title + "]";
	}

}
