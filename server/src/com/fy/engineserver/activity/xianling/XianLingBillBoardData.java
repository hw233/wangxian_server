package com.fy.engineserver.activity.xianling;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 仙灵排行榜数据
 * @author Administrator
 * 
 */
@SimpleEntity
public class XianLingBillBoardData implements Serializable{
	private transient static final long serialVersionUID = 1L;
	@SimpleId
	private long id; // playerId
	@SimpleVersion
	private int version;
	@SimpleColumn(length = 50000)
	private Map<Integer, XLBillBoardData> xlDataMap = new HashMap<Integer, XLBillBoardData>(); // Map<activityId, XLBillBoardData>

	private transient int activityId;// 活动id
	// private long playerId;
	private transient String userName;
	private transient String playerName;
	private transient String serverName;
	private transient int crossServerRank; // 跨服排行
	private transient int score; // 积分
	private transient long lastUpdateTime;// 上次更新积分时间

	public XianLingBillBoardData() {
		// TODO Auto-generated constructor stub
	}
	public XianLingBillBoardData(Player p) {
		this.userName = p.getUsername();
		this.playerName = p.getName();
		this.serverName = GameConstants.getInstance().getServerName();
	}

	public XianLingBillBoardData(long id, Map<Integer, XLBillBoardData> xlDataMap) {
		super();
		this.id = id;
		this.xlDataMap = xlDataMap;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		XianLingManager.boardEm.notifyFieldChange(this, "id");
	}

	public Map<Integer, XLBillBoardData> getXlDataMap() {
		return xlDataMap;
	}

	public void setXlDataMap(Map<Integer, XLBillBoardData> xlDataMap) {
		this.xlDataMap = xlDataMap;
//		XianLingManager.boardEm.notifyFieldChange(this, "xlDataMap");
	}

	public int getCrossServerRank() {
		return crossServerRank;
	}

	public void setCrossServerRank(int crossServerRank) {
		this.crossServerRank = crossServerRank;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	
	public void setNotSaveVars(int key){
		if(xlDataMap.containsKey(key)){
			XLBillBoardData data = xlDataMap.get(key);
			if(data != null){
				this.setLastUpdateTime(data.getLastUpdateTime());
				this.setActivityId(key);
				this.setPlayerName(data.getPlayerName());
				this.setScore(data.getScore());
				this.setServerName(data.getServerName());
				this.setUserName(data.getUserName());
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		for(XLBillBoardData data:xlDataMap.values()){
			sbf.append(data.toString());
		}
		return "XianLingBillBoardData:id=" + id + ",activityId=" + activityId + ",userName=" + userName + ",playerName=" + playerName + ",serverName=" + serverName + ",crossServerRank=" + crossServerRank + ",score=" + score + ",lastUpdateTime=" + lastUpdateTime+",xlDataMap=["+sbf.toString()+"]";
	}
}
