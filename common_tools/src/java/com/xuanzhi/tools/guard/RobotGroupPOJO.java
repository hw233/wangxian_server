package com.xuanzhi.tools.guard;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class RobotGroupPOJO {
	
	private String name;
	
	private List<RobotPOJO> robots = new ArrayList<RobotPOJO>();
	
	private long lastRefreshTime;
	
	public RobotGroupPOJO() {
	}

	public RobotGroupPOJO(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RobotPOJO> getRobots() {
		return robots;
	}

	public void setRobots(List<RobotPOJO> robots) {
		this.robots = robots;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}
	
	
}
