package com.sqage.stat.commonstat.manager;

import java.util.List;

public interface Stat_base_onlineManager {

	public List<String[]> getStat_base_online(String dateStr);
	public boolean addStat_base_online(String[] data);
}
