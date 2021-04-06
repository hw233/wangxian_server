package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;

import com.sqage.stat.commonstat.entity.FuMo;

public interface FuMoManager {

	public ArrayList<FuMo> getBySql(String sql);
	
	public  boolean addUseFoMoList(ArrayList<FuMo> fuMoList);
	
	public  boolean addFinishFuMoList(ArrayList<FuMo> fuMoList);
	
	public ArrayList<String[]> getFuMoData(String sql,String [] strs);
	
}