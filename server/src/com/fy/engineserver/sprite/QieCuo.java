package com.fy.engineserver.sprite;

public class QieCuo {
	/**
	 * 记录切磋的对象，切磋的地点， 当玩家处于切磋的状态下，如果玩家离开切磋的范围， 系统会提示10秒后，自动判定为输
	 */
	private long qieCuoPlayerA = -1;
	private long qieCuoPlayerB = -1;

	private String qiecouMapName = null;
	
	private  int qiecouX = 0;
	private  int qiecouY = 0;
	
	public QieCuo() {}
	
	public long getQieCuoPlayerA() {
		return qieCuoPlayerA;
	}
	public void setQieCuoPlayerA(long qieCuoPlayerA) {
		this.qieCuoPlayerA = qieCuoPlayerA;
	}
	public long getQieCuoPlayerB() {
		return qieCuoPlayerB;
	}
	public void setQieCuoPlayerB(long qieCuoPlayerB) {
		this.qieCuoPlayerB = qieCuoPlayerB;
	}
	public String getQiecouMapName() {
		return qiecouMapName;
	}
	public void setQiecouMapName(String qiecouMapName) {
		this.qiecouMapName = qiecouMapName;
	}
	public int getQiecouX() {
		return qiecouX;
	}
	public void setQiecouX(int qiecouX) {
		this.qiecouX = qiecouX;
	}
	public int getQiecouY() {
		return qiecouY;
	}
	public void setQiecouY(int qiecouY) {
		this.qiecouY = qiecouY;
	}
	
	

}
