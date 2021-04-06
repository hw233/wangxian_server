package com.xuanzhi.tools.guard;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class RobotGroup {
	
	private String name;
	
	private List<Robot> robots = new ArrayList<Robot>();
	
	private long lastRefreshTime;
	
	public RobotGroup() {
	}

	public RobotGroup(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Robot> getRobots() {
		return robots;
	}

	public void setRobots(List<Robot> robots) {
		this.robots = robots;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}
	
	
}
