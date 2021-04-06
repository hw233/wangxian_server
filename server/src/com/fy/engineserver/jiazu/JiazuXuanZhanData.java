package com.fy.engineserver.jiazu;

import java.util.Hashtable;

public class JiazuXuanZhanData {

	/**
	 * 家族id
	 */
	long id;
	
	/**
	 * 宣战家族的家族id及到期时间
	 */
	Hashtable<Long,Long> xuanzhanJiazuMap = new Hashtable<Long,Long>();
	
	public JiazuXuanZhanData(long id){
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Hashtable<Long, Long> getXuanzhanJiazuMap() {
		return xuanzhanJiazuMap;
	}

	public void setXuanzhanJiazuMap(Hashtable<Long, Long> xuanzhanJiazuMap) {
		this.xuanzhanJiazuMap = xuanzhanJiazuMap;
	}
	
}
