package com.fy.engineserver.sprite.petdao;

import java.io.Serializable;

import com.fy.engineserver.sprite.Player;

/**
 * 副本进度
 * 
 *
 */
public class MiChengSpeed implements Serializable{
	
	private String name;	//副本名
	
	/**
	 * 1.进入，离开
	 * 2.进入，心跳（这种比较有效）
	 */
	private long continuetime;	//持续时间
	
	private long endtime;	//结束
	
	private int keyovernum;	//钥匙剩余数量
	
	private int keyrefreshnum;	//已经开出的钥匙数
	
	private int boxnum;
	
	private int boxovernum;
	
	private transient Player p;	//副本拥有者
	
	private long pid;
	
	public int STAT;	//副本状态(1:暂停-0:开始)
	
	/**
	 * 是否有效
	 * @return
	 */
	public boolean iseffective(){
		return continuetime>0 && STAT==0;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getContinuetime() {
		return continuetime;
	}

	public void setContinuetime(long continuetime) {
		this.continuetime = continuetime;
	}

	public int getKeyovernum() {
		return keyovernum;
	}

	public void setKeyovernum(int keyovernum) {
		this.keyovernum = keyovernum;
	}

	public int getKeyrefreshnum() {
		return keyrefreshnum;
	}

	public void setKeyrefreshnum(int keyrefreshnum) {
		this.keyrefreshnum = keyrefreshnum;
	}

	public Player getP() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}

	public int getBoxnum() {
		return boxnum;
	}

	public void setBoxnum(int boxnum) {
		this.boxnum = boxnum;
	}

	public int getBoxovernum() {
		return boxovernum;
	}

	public void setBoxovernum(int boxovernum) {
		this.boxovernum = boxovernum;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public int getSTAT() {
		return STAT;
	}

	public void setSTAT(int sTAT) {
		STAT = sTAT;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

}
