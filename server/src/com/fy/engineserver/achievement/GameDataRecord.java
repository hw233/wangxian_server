package com.fy.engineserver.achievement;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostPersist;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 运行数据记录
 * 
 */
@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "playerId", "dataType" }), @SimpleIndex(members = { "dataType" }) })
public class GameDataRecord {
	@SimpleVersion
	private int version;
	@SimpleId
	private long id;
	/** 角色ID */
	private long playerId;
	/** 数据类型,目前是成就系统的各个Action的type */
	private int dataType;
	/** 已经完成的数量 */
	@SimpleColumn(saveInterval = 600)
	private long num;

	public GameDataRecord() {
		// TODO Auto-generated constructor stub
	}

	@SimplePostPersist
	public void notifySaved() {

	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public synchronized void addNum(long addNum) {
		if (addNum <= 0) {
			throw new IllegalArgumentException("[输入次数:" + addNum + "]");
		}
		setNum(num + addNum);
	}

	@Override
	public String toString() {
		return "GameDataRecord [version=" + version + ", id=" + id + ", playerId=" + playerId + ", dataType=" + dataType + ", num=" + num + "]";
	}
}
