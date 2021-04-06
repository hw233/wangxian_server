package com.fy.gamebase.monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Monitor Item link
 * 
 * create on 2013年7月29日
 */
public class MILink {
	private static AtomicInteger idGen = new AtomicInteger(1000);
	public static Map<Integer, MILink> map = new HashMap<Integer, MILink>();
	public MILink(String href, String showText) {
		this.href = href;
		this.showText = showText;
		id = idGen.incrementAndGet(); 
		map.put(id, this);
	}
	public MILinkProc proc;
	public String href;
	public String showText;
	public int id;
}
