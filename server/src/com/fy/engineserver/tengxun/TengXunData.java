package com.fy.engineserver.tengxun;

public class TengXunData {
	
	public TengXunData (long playerID) {
		this.playerID = playerID;
		id = -1;
		sqq = false;
		sqqType = -1;
		sqqLevel = -1;
		gamevip = false;
		gamevipLevel = -1;
		yellowLevel = -1;
	}

	public long playerID;
	
	public long id;
	
	public boolean sqq;			//是否开通腾讯的超级QQ业务
	
	public int sqqType;			//超级QQ类型
	
	public int sqqLevel;		//超级QQ等级
	
	public boolean gamevip;		//是否开通魔钻
	
	public int gamevipLevel;	//魔钻等级
	
	public int yellowLevel;		//黄钻等级
}
