package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_TALENT_BUTTON_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.talent.FlyTalentManager;
import com.fy.engineserver.talent.TalentData;

public class MinusSkillCdTimesProps extends Props {

	
	public long minusCDTimes;
	public int useTimes;
	
	@Override
	public String canUse(Player p) {
		if(super.canUse(p) != null){
			return super.canUse(p);
		}
		TalentData data = FlyTalentManager.getInstance().getTalentData(p.getId());
		if(data != null){
			if(data.getUseCDTimes() >= useTimes){
				return "使用次数已达上限";
			}
		}
		return null;
	}

	public byte getArticleType() {
		return ARTICLE_TYPE_SKILL_CD;
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if(minusCDTimes <= 0 || useTimes <= 0){
			ArticleManager.logger.warn("[使用道具] [失败:配置错误] [道具:"+(ae==null?"nul":ae.getArticleName())+"] [MinusSkillCdTimesProps] ["+player.getLogString()+"]");
			return false;
		}
		
		TalentData data = FlyTalentManager.getInstance().getTalentData(player.getId());
		if(data == null){
			player.sendError(Translate.还没开启仙婴天赋);
			return false;
		}
		
		if(player.flyState == 1){
			player.sendError(Translate.仙婴附体中不能操作);
			return false;
		}
		
		if((data.getCdEndTime() - data.getMinusCDTimes()) > System.currentTimeMillis()){
			player.sendError(Translate.cd中不能使用);
			return false;
		} 
		
		data.setMinusCDTimes(data.getMinusCDTimes() + minusCDTimes * 1000);
		data.setUseCDTimes(data.getUseCDTimes() + 1);
		FlyTalentManager.getInstance().saveTalentData(player, data);
		long currCDtime = (FlyTalentManager.TALENT_SKILL_CD_TIME - data.getMinusCDTimes())/1000;
		String mess = Translate.translateString(Translate.减天赋cd道具使用成功, new String[][] { { Translate.COUNT_1, String.valueOf(minusCDTimes/60)},{ Translate.COUNT_2, String.valueOf(currCDtime/60)}});
		player.sendError(mess);
		NOTICE_TALENT_BUTTON_RES res = new NOTICE_TALENT_BUTTON_RES(GameMessageFactory.nextSequnceNum(), 1,0, FlyTalentManager.TALENT_SKILL_CD_TIME-data.getMinusCDTimes());
		player.addMessageToRightBag(res);
		ArticleManager.logger.warn("[使用道具] [成功] [MinusSkillCdTimesProps] [道具:"+this.getName()+"] [减去总cd:"+data.getMinusCDTimes()+"] [使用总次数:"+data.getUseCDTimes()+"] ["+player.getName()+"]");
		return true;
	}


}
