package com.fy.engineserver.activity.farming;

import java.util.Arrays;

/**
 * 神农任务的果子配置
 * 
 */
public class FarmingFruit {
	private int id;
	private String name;
	private String[] taskName;

	public FarmingFruit(int id, String name, String[] taskName) {
		this.id = id;
		this.name = name;
		this.taskName = taskName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getTaskName() {
		return taskName;
	}

	public void setTaskName(String[] taskName) {
		this.taskName = taskName;
	}

	@Override
	public String toString() {
		return "FarmingFruit [id=" + id + ", name=" + name + ", taskName=" + Arrays.toString(taskName) + "]";
	}
}
