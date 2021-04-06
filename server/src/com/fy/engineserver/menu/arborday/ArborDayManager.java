package com.fy.engineserver.menu.arborday;

import com.fy.engineserver.util.TimeTool;

/**
 * 植树节
 * 
 *
 */
public class ArborDayManager {
	public static long startTime = TimeTool.formatter.varChar19.parse("2013-03-12 00:00:00");
	public static long endTime = TimeTool.formatter.varChar19.parse("2013-03-17 00:00:00");
	
	public static boolean inCycle(long now) {
		return startTime <= now && startTime <= endTime;
	}
}
