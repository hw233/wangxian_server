package com.fy.engineserver.newtask.service;

/**
 * npc任务改变
 * 
 * 
 */
public class FunctionNpcModify {

	private long NPCId;
	private byte modifyType;
	private String[] modifyNames = new String[0];;
	private byte[] modifyTypes = new byte[0];
	private long[] modifyIds = new long[0];
	private int[] modifyGrades = new int[0];

	public FunctionNpcModify() {

	}

	public long getNPCId() {
		return NPCId;
	}

	public void setNPCId(long nPCId) {
		NPCId = nPCId;
	}

	public byte getModifyType() {
		return modifyType;
	}

	public void setModifyType(byte modifyType) {
		this.modifyType = modifyType;
	}

	public long[] getModifyIds() {
		return modifyIds;
	}

	public void setModifyIds(long[] modifyIds) {
		this.modifyIds = modifyIds;
	}

	public String[] getModifyNames() {
		return modifyNames;
	}

	public void setModifyNames(String[] modifyNames) {
		this.modifyNames = modifyNames;
	}

	public byte[] getModifyTypes() {
		return modifyTypes;
	}

	public void setModifyTypes(byte[] modifyTypes) {
		this.modifyTypes = modifyTypes;
	}

	public int[] getModifyGrades() {
		return modifyGrades;
	}

	public void setModifyGrades(int[] modifyGrades) {
		this.modifyGrades = modifyGrades;
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (obj instanceof FunctionNpcModify) {
	// FunctionNpcModify other = (FunctionNpcModify) obj;
	// if (NPCId == other.NPCId) {
	// if (modifyType == other.modifyType) {
	// if (modifyIds.length == other.modifyIds.length) {
	// boolean isSame = true;
	// for (int i = 0; i < modifyIds.length; i++) {
	// if (modifyIds[i] != other.modifyIds[i]) {
	// isSame = false;
	// break;
	// }
	// }
	// return isSame;
	// }
	// }
	// }
	// }
	// return false;
	// }
}
