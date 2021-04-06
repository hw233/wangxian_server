package com.fy.engineserver.talent;

import java.util.HashMap;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class TalentData {

	@SimpleId
	private long id;								// playerId
	@SimpleVersion
	private int version;

	private long exp;			

	private long cdEndTime; 						// 仙婴附体cd结束时间

	private int canUsePoints; 						// 可用点数(暂时不用)
	
	private int xylevel = 1;						// 仙婴等级
	
	private int xylevelA = 0;						// 天赋增加的仙婴等级
	
	private long lastXLTime;						// 上次修炼时间

	private int xyButtonState;						// 仙婴附体按钮状态,1:任务完成
	
	private long nbEndTimes;						// 附体持续结束时间
	
	private long minusCDTimes;						//永久减去的cd时间
	private long useCDTimes;						//永久使用次数

	private HashMap<Integer, Integer> points = new HashMap<Integer, Integer>();
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getCdEndTime() {
		return cdEndTime;
	}

	public void setCdEndTime(long cdEndTime) {
		this.cdEndTime = cdEndTime;
		FlyTalentManager.em.notifyFieldChange(this, "cdEndTime");
	}

	public int getCanUsePoints() {
		return canUsePoints;
	}

	public void setCanUsePoints(int canUsePoints) {
		this.canUsePoints = canUsePoints;
		FlyTalentManager.em.notifyFieldChange(this, "canUsePoints");
	}
	
	public int getXylevel() {
		return xylevel;
	}

	public void setXylevel(int xylevel) {
		noticePlayerGetPoints(this.xylevel, xylevel);
		this.xylevel = xylevel;
		setXylevelA(xylevelA);
		FlyTalentManager.em.notifyFieldChange(this, "xylevel");
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
		FlyTalentManager.em.notifyFieldChange(this, "exp");
	}

	public HashMap<Integer, Integer> getPoints() {
		return points;
	}

	public void setPoints(HashMap<Integer, Integer> points) {
		this.points = points;
		FlyTalentManager.em.notifyFieldChange(this, "points");
	}

	public long getLastXLTime() {
		return lastXLTime;
	}

	public void setLastXLTime(long lastXLTime) {
		this.lastXLTime = lastXLTime;
		FlyTalentManager.em.notifyFieldChange(this, "lastXLTime");
	}

	public int getXyButtonState() {
		return xyButtonState;
	}

	public void setXyButtonState(int xyButtonState) {
		this.xyButtonState = xyButtonState;
		FlyTalentManager.em.notifyFieldChange(this, "xyButtonState");
	}

	public long getNbEndTimes() {
		return nbEndTimes;
	}

	public void setNbEndTimes(long nbEndTimes) {
		this.nbEndTimes = nbEndTimes;
		FlyTalentManager.em.notifyFieldChange(this, "nbEndTimes");
	}

	public int getXylevelA() {
		return xylevelA;
	}

	public void setXylevelA(int xylevelA) {
		this.xylevelA = xylevelA;
		int value = this.xylevel + this.xylevelA - FlyTalentManager.MAX_XIANYIN_LEVEL;
		if(value > 0){
			int newLevelA = xylevelA - value;
			if(newLevelA < 0){
				newLevelA = 0;
			}
			this.xylevelA = newLevelA;
		}
		FlyTalentManager.em.notifyFieldChange(this, "xylevelA");
	}

	public long getMinusCDTimes() {
		return minusCDTimes;
	}

	public void setMinusCDTimes(long minusCDTimes) {
		this.minusCDTimes = minusCDTimes;
		FlyTalentManager.em.notifyFieldChange(this, "minusCDTimes");
	}

	public long getUseCDTimes() {
		return useCDTimes;
	}

	public void setUseCDTimes(long useCDTimes) {
		this.useCDTimes = useCDTimes;
		FlyTalentManager.em.notifyFieldChange(this, "useCDTimes");
	}

	/**
	 * 通知玩家获取天赋点数
	 * @param oldLevel
	 * @param newLevel
	 */
	public void noticePlayerGetPoints(int oldLevel,int newLevel){
		if(oldLevel/FlyTalentManager.GET_ONE_POINT_NEED_LEVEL != newLevel/FlyTalentManager.GET_ONE_POINT_NEED_LEVEL){
			try {
				Player p = PlayerManager.getInstance().getPlayer(id);
				if(p != null){
					p.sendWinNotice(Translate.获得新的仙婴天赋点);
					p.sendError(Translate.获得新的仙婴天赋点);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String toString() {
		return "[canUsePoints=" + canUsePoints + ", cdEndTime="
				+ cdEndTime + ", exp=" + exp + ", id=" + id + ", lastXLTime="
				+ lastXLTime + ", minusCDTimes=" + minusCDTimes
				+ ", nbEndTimes=" + nbEndTimes + ", points=" + points
				+ ", useCDTimes=" + useCDTimes + ", xyButtonState="
				+ xyButtonState + ", xylevel=" + xylevel + ", xylevelA="
				+ xylevelA + "]";
	}


}
