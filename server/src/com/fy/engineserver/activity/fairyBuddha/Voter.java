package com.fy.engineserver.activity.fairyBuddha;

import java.io.Serializable;

/**
 * 投票者
 * 
 */
public class Voter implements Serializable{
	private long id; // 投票者id
	private String name;// 投票者角色名
	private String time;// 投票时间

	public Voter() {
	}

	public Voter(long id, String name, String time) {
		this.id = id;
		this.name = name;
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Voter [id=" + id + ", name=" + name + ", time=" + time + "]";
	}

}
