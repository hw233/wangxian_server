package com.xuanzhi.tools.statistics.depot;

import com.xuanzhi.tools.statistics.Dimension;
import com.xuanzhi.tools.statistics.StatData;

public class DepotRowData {

	public String yearMonth;
	public Dimension []ds;
	public StatData data;
	
	public DepotRowData(){
		
	}
	
	public DepotRowData(String yearMonth,Dimension []ds,StatData data){
		this.yearMonth = yearMonth;
		this.ds = ds;
		this.data = data;
	}
}
