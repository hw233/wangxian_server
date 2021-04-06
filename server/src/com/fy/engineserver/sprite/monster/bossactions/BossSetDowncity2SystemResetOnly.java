package com.fy.engineserver.sprite.monster.bossactions;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.downcity.DownCityInfo;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;

public class BossSetDowncity2SystemResetOnly implements BossAction {
//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void doAction(Game game, BossMonster boss, Fighter target) {
		DownCity dc = game.getDownCity();
		if(dc == null){
			if(logger.isWarnEnabled())
				logger.warn("[BOSS执行动作] [设置副本重置方式为系统重置] [失败] [无法获得BOSS所在的副本进度]");
			return;
		}
		if(dc.getDownCityState() == 0){
			dc.setDownCityState((byte)1);
			Player[] ps = dc.getPlayersInGame();
			if(ps != null){
				for(Player p : ps){
					if(p != null){
						if(dc.getDi() != null && dc.getDi().getResetType() == DownCityInfo.RESET_TYPE_BY_SYSTEM){
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,Translate.text_5738+((dc.getEndTime() - com.fy.engineserver.gametime.SystemTime.currentTimeMillis())/3600000+1)+Translate.text_5739);
							p.addMessageToRightBag(hreq);
						}else if(dc.getDi() != null && dc.getDi().getResetType() == DownCityInfo.RESET_TYPE_BY_DAY){
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,Translate.text_5740);
							p.addMessageToRightBag(hreq);
						}
					}
				}
			}
		}
		if(logger.isWarnEnabled())
			logger.warn("[BOSS执行动作] [设置副本重置方式为系统重置] [成功]");
	}

	public int getActionId() {
		// TODO Auto-generated method stub
		return actionId;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	public boolean isExeAvailable(BossMonster boss, Fighter target) {
		// TODO Auto-generated method stub
		return true;
	}

}
