package com.fy.engineserver.articleProtect;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"playerID"})
})
public class ArticleProtectData {

	@SimpleId
	private long id;
	
	@SimpleVersion
	private int versionAP;
	
	private long playerID;				//玩家ID
	
	private int articleType;			//宠物，装备，宝石
	
	private long articleID;				//物品ID(宠物存的是宠物ID)
	
	private int state;					//状态
	
	private long removeTime = -1;		//解除时间
	
	public ArticleProtectData() {}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setVersionAP(int versionAP) {
		this.versionAP = versionAP;
	}

	public int getVersionAP() {
		return versionAP;
	}

	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}

	public long getPlayerID() {
		return playerID;
	}

	public void setArticleType(int articleType) {
		this.articleType = articleType;
	}

	public int getArticleType() {
		return articleType;
	}

	public void setArticleID(long articleID) {
		this.articleID = articleID;
	}

	public long getArticleID() {
		return articleID;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setRemoveTime(long removeTime) {
		this.removeTime = removeTime;
	}

	public long getRemoveTime() {
		return removeTime;
	}
	
	public String getLogString(boolean isPlayerID) {
		StringBuffer sb = new StringBuffer("");
		if (isPlayerID) {
			sb.append(playerID).append(" ");
		}
		sb.append(articleType).append(" ").append(articleID).append(" ")
		.append(state).append(" ").append(removeTime);
		return sb.toString();
	}
	
}
