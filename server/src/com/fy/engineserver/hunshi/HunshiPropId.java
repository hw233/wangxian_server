package com.fy.engineserver.hunshi;

import com.fy.engineserver.util.SimpleKey;

public class HunshiPropId {
	@SimpleKey
	private String hunshiCNName;
	private int[] mainPropId;
	private int[] extraPropId;
	private int extraJDPropId; // 运营需求，神隐功能用到的

	public HunshiPropId(String hunshiCNName, int[] mainPropId, int[] extraPropId, int extraJDPropId) {
		this.hunshiCNName = hunshiCNName;
		this.mainPropId = mainPropId;
		this.extraPropId = extraPropId;
		this.extraJDPropId = extraJDPropId;
	}

	public HunshiPropId() {
	}

	public String getHunshiCNName() {
		return hunshiCNName;
	}

	public void setHunshiCNName(String hunshiCNName) {
		this.hunshiCNName = hunshiCNName;
	}

	public int[] getMainPropId() {
		return mainPropId;
	}

	public void setMainPropId(int[] mainPropId) {
		this.mainPropId = mainPropId;
	}

	public int[] getExtraPropId() {
		return extraPropId;
	}

	public void setExtraPropId(int[] extraPropId) {
		this.extraPropId = extraPropId;
	}

	public int getExtraJDPropId() {
		return extraJDPropId;
	}

	public void setExtraJDPropId(int extraJDPropId) {
		this.extraJDPropId = extraJDPropId;
	}

}
