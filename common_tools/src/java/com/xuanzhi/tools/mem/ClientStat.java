package com.xuanzhi.tools.mem;

import javax.jdo.annotations.Index;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 */
@Entity
public class ClientStat {
	@Index
	private String clientName;
	
	private String collectionkey;
	
	private long createTime;
	
	private long num;
	
	private long size;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getCollectionkey() {
		return collectionkey;
	}

	public void setCollectionkey(String collectionkey) {
		this.collectionkey = collectionkey;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getLogStr() {
		return "{"+clientName+"}{"+collectionkey+"}{"+num+"}{"+size+"}";
	}
}
