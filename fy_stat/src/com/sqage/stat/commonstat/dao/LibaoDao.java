package com.sqage.stat.commonstat.dao;

import java.util.ArrayList;

import com.sqage.stat.commonstat.entity.Libao;

public interface LibaoDao 
{

	public ArrayList<Libao> getBySql(String sql);
	
	public  boolean addList(ArrayList<Libao> libaoList);
	
	public ArrayList<String[]> getliBaoData(String sql,String [] strs);
	
}
