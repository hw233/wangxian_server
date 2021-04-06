package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;

import com.sqage.stat.commonstat.entity.Npcinfo;

public interface NpcinfoManager {
	
	public  boolean addList(ArrayList<Npcinfo> NpcinfoList);

	public ArrayList<String[]> getNpcinfo(String sql, String[] columusEnums);
}
