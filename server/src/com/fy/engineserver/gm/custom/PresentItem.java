package com.fy.engineserver.gm.custom;

import java.util.ArrayList;
import java.util.List;

public class PresentItem {
	private String itemname;
	private String startDate;
    private String endDate;
    private String count;
    private List<PlayerItem> pis = new ArrayList<PlayerItem>();
    private List<String>  playids = new ArrayList<String>();
	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<PlayerItem> getPis() {
		return pis;
	}

	public void setPis(List<PlayerItem> pis) {
		this.pis = pis;
	}

	public List<String> getPlayids() {
		return playids;
	}

	public void setPlayids(List<String> playids) {
		this.playids = playids;
	}
    
	
}
