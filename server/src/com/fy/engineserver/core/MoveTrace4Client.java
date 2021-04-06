package com.fy.engineserver.core;

import com.xuanzhi.tools.text.StringUtil;

public class MoveTrace4Client {
	private byte type;
	private long id;
	private long startTimestamp;
	private long destineTimestamp;
	private int speed;
	private short[] roadLen;
	private short[] pointsX;
	private short[] pointsY;
	private byte SpecialSkillType;

	public MoveTrace4Client() {
	}

	public MoveTrace4Client(byte type, long id, long destineTimestamp,
			short[] roadLen, short[] pointsX, short[] pointsY) {
		this.startTimestamp = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		this.type = type;
		this.id = id;
		this.destineTimestamp = destineTimestamp;
		this.roadLen = roadLen;
		this.pointsX = pointsX;
		this.pointsY = pointsY;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDestineTimestamp() {
		return destineTimestamp;
	}

	public void setDestineTimestamp(long destineTimestamp) {
		this.destineTimestamp = destineTimestamp;
	}

	public short[] getRoadLen() {
		return roadLen;
	}

	public void setRoadLen(short[] roadLen) {
		this.roadLen = roadLen;
	}

	public short[] getPointsX() {
		return pointsX;
	}

	public void setPointsX(short[] pointsX) {
		this.pointsX = pointsX;
	}

	public short[] getPointsY() {
		return pointsY;
	}

	public void setPointsY(short[] pointsY) {
		this.pointsY = pointsY;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long clientTimestamp) {
		this.startTimestamp = clientTimestamp;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public byte getSpecialSkillType() {
		return SpecialSkillType;
	}

	public void setSpecialSkillType(byte specialSkillType) {
		SpecialSkillType = specialSkillType;
	}

	@Override
	public String toString() {
		return "{id:"+id+"}{type:"+type+"}{speed:"+speed+"}{px:"+StringUtil.arrayToString(this.pointsX, ",")+"}{py:"+StringUtil.arrayToString(this.pointsY, ",")+"}{RL:"+StringUtil.arrayToString(roadLen, ",")+"}";
	}
}
