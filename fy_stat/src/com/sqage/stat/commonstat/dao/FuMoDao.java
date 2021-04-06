package com.sqage.stat.commonstat.dao;

import java.util.ArrayList;

import com.sqage.stat.commonstat.entity.FuMo;

public interface FuMoDao 
{

	public ArrayList<FuMo> getBySql(String sql);
	
	public  boolean addUseFoMoList(ArrayList<FuMo> fuMoList);
	
	public  boolean addFinishFuMoList(ArrayList<FuMo> fuMoList);
	
	public ArrayList<String[]> getFuMoData(String sql,String [] strs);
	
}
