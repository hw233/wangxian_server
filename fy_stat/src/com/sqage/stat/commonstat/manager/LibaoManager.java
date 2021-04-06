package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;

import com.sqage.stat.commonstat.entity.Libao;

public interface LibaoManager {
    
	public ArrayList<Libao> getBySql(String sql);
	
	public  boolean addList(ArrayList<Libao> libaoList);
	
	public ArrayList<String[]> getliBaoData(String sql,String [] strs);
}
