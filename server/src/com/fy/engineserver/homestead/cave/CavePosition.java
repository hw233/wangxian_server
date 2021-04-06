package com.fy.engineserver.homestead.cave;

/**
 * 洞府位置信息
 * 
 * 
 */
public class CavePosition {

	private int index;
	private long faeryId;

	public CavePosition(long faeryId, int index) {
		this.faeryId = faeryId;
		this.index = index;

	}

	public final int getIndex() {
		return index;
	}

	public final void setIndex(int index) {
		this.index = index;
	}

	public final long getFaeryId() {
		return faeryId;
	}

	public final void setFaeryId(long faeryId) {
		this.faeryId = faeryId;
	}

	@Override
	public String toString() {
		return "CavePosition [index=" + index + ", faeryId=" + faeryId + "]";
	}
}
