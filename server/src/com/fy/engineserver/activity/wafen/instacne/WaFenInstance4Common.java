package com.fy.engineserver.activity.wafen.instacne;

import java.io.Serializable;
import java.util.List;

import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel;

public class WaFenInstance4Common implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 挖过的次数 */
	private int times;
	/** 挖开的坟地 */
	private List<FenDiModel> fendiList;
	
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public List<FenDiModel> getFendiList() {
		return fendiList;
	}
	public void setFendiList(List<FenDiModel> fendiList) {
		this.fendiList = fendiList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
