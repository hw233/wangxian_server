package com.fy.engineserver.menu;

import java.util.Iterator;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.CONFIRM_FLY_TALENT_ADD_POINTS_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.talent.FlyTalentManager;
import com.fy.engineserver.talent.TalentData;
public class Option_Reset_Talent_Skills extends Option{

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		TalentData data = FlyTalentManager.getInstance().getTalentData(player.getId());
		if(data == null){
			data = new TalentData();
		}
		int hasAddPoints = 0;
		if(data.getPoints() != null){
			Iterator<Integer> it = data.getPoints().values().iterator();
			while(it.hasNext()){
				Integer p = it.next();
				if(p != null){
					hasAddPoints+=p;
				}
			}
		}
		
		if(hasAddPoints <= 0){
			player.sendError(Translate.重置加点失败);
			return;
		}
		
		int hasNums = player.getArticleEntityNum(Translate.仙婴天赋重置丹);
		if(hasNums <= 0){
			player.sendError(Translate.背包中没有仙婴天赋重置丹);
			return;
		}
		
		boolean result = player.removeArticle(Translate.仙婴天赋重置丹, "仙婴天赋重置丹");
		if(result){
			data.setXylevelA(0);
			data.getPoints().clear();
			data.setPoints(data.getPoints());
			player.taneltSkillTempAddPoints.clear();
			FlyTalentManager.getInstance().saveTalentData(player, data);
			player.sendError(Translate.重置加点);
			player.addMessageToRightBag(new CONFIRM_FLY_TALENT_ADD_POINTS_RES(GameMessageFactory.nextSequnceNum(), 1));
			return;
		}
		player.sendError(Translate.重置失败);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_OPEN_CANGKU;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
