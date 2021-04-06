package com.fy.engineserver.smith;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 * @version 创建时间：Apr 26, 2013 11:07:28 AM
 * 
 */
public class SuspectStudio {
	
	List<MoneyRelationShip4Client> suspectList = new ArrayList<MoneyRelationShip4Client>();

	public List<MoneyRelationShip4Client> getSuspectList() {
		return suspectList;
	}

	public void setSuspectList(List<MoneyRelationShip4Client> suspectList) {
		this.suspectList = suspectList;
	}
	
	
}
