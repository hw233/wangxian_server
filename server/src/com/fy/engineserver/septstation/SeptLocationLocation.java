package com.fy.engineserver.septstation;

import com.fy.engineserver.util.StringTool;

public class SeptLocationLocation implements Comparable<SeptLocationLocation> {
	/** 位置信息 */
	private int index;
	/** 所在地图 */
	private String mapName;
	/** 位置描述 */
	private String locationDes;
	/** 入口NPC */
	private String enterNpc;
	/** 商圈 */
	private String businessArea;
	/** 特产 */
	private String[][] speciality = new String[5][];

	private String specialityStr = "";

	public SeptLocationLocation() {

	}

	public SeptLocationLocation(int index, String mapName, String locationDes, String enterNpc, String businessArea, String[][] speciality) {
		this.index = index;
		this.mapName = mapName;
		this.locationDes = locationDes;
		this.enterNpc = enterNpc;
		this.businessArea = businessArea;
		this.speciality = speciality;
		if (this.speciality != null) {
			StringBuffer sbf = new StringBuffer();
			for (int i = 0; i < this.speciality.length - 1; i++) {
				String[] value = this.speciality[i];
				sbf.append(StringTool.array2String(value, ",")).append(",");
			}
			sbf.append(StringTool.array2String(this.speciality[this.speciality.length - 1], ","));
			specialityStr = sbf.toString();
		}
	}

	@Override
	public String toString() {
		return "[ index = " + index + ",mapName = " + mapName + ",locationDes = " + locationDes + ",enterNpc = " + enterNpc + ",businessArea = " + businessArea + ",*一级:" + StringTool.array2String(speciality[0], ",") + ",*二级:" + StringTool.array2String(speciality[1], ",") + ",*三级:" + StringTool.array2String(speciality[2], ",") + ",*四级:" + StringTool.array2String(speciality[3], ",") + ",*五级:" + StringTool.array2String(speciality[4], ",") + "]";
	}

	@Override
	public int compareTo(SeptLocationLocation o) {
		return 0;
	}

	/***************************************** getters and setters *****************************************/
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getLocationDes() {
		return locationDes;
	}

	public void setLocationDes(String locationDes) {
		this.locationDes = locationDes;
	}

	public String getEnterNpc() {
		return enterNpc;
	}

	public void setEnterNpc(String enterNpc) {
		this.enterNpc = enterNpc;
	}

	public String[][] getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String[][] speciality) {
		this.speciality = speciality;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}

	public String getSpecialityStr() {
		return specialityStr;
	}

	public void setSpecialityStr(String specialityStr) {
		this.specialityStr = specialityStr;
	}
}
