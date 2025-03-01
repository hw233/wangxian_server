package com.fy.engineserver.activity.fateActivity;

import java.util.Map;

import com.fy.engineserver.activity.fateActivity.base.FateActivity;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SEEM_HINT_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

@SimpleEntity
public class BeerActivityInCountry extends FateActivity {

	public BeerActivityInCountry(){
	
	}
	public BeerActivityInCountry(long id){
		super(id);
	}
	

	public boolean invitedAgree(Player player,FateActivity fa,int successNum,int level){

		PlayerManager pm = PlayerManager.getInstance();
		if(super.invitedAgree(player, fa, successNum, level)){
			Player invite;
			try {
				invite = pm.getPlayer(inviteId);
//				String key1 = "你同意了"+invite.getName()+"的请求，请到凤栖梧桐去找他";
				String key1 = Translate.translateString(Translate.你同意xx论道请求去凤栖梧桐去找他, new String[][]{{Translate.PLAYER_NAME_1,invite.getName()}});
				SEEM_HINT_RES res1 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), fa.getTemplate().getType(), key1);
				player.addMessageToRightBag(res1);
				
//				String keyString = player.getName()+"同意了你的请求,请到凤栖梧桐去找他";
				String keyString = Translate.translateString(Translate.xx同意你论道请求去凤栖梧桐去找他, new String[][]{{Translate.PLAYER_NAME_1,player.getName()}});
				SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), fa.getTemplate().getType(), keyString);
				invite.addMessageToRightBag(res);
				return true;
				
			} catch (Exception e) {
				FateManager.logger.error("[玩家同意活动异常] ["+player.getLogString()+"]",e);
			}
		}
		return false;
	}
	
	
	
//	public String getKeyString(Player invite,Player invited){
////		String keyString = "(国内论道)枭雄四起,八方豪杰，恭喜你找到有缘人"+invited.getName()+",你想送他什么？";
//		String keyString = Translate.translateString(Translate.国内论道keyString, new String[][]{{Translate.PLAYER_NAME_1,invited.getName()}});
//		return keyString;
//	}
//	
//	public String[] getLevelContent(Player invite,Player invited){
////		String[] levelContent = {"青铜酒樽(15点体力)","稀世宝剑(60点体力)","名贵古画(100点体力，4倍经验)"};
//		String[] levelContent = {Translate.国内论道LevelContent0,Translate.国内论道LevelContent1,Translate.国内论道LevelContent2};
//		return  levelContent;
//	}
	
	public String getRealInviteKeyString(Player invite,Player invited){
//		String key = invite.getName()+"邀请你做论道任务";
		String key = Translate.translateString(Translate.国内论道RealInviteKeyString, new String[][]{{Translate.PLAYER_NAME_1,invite.getName()}});
		return key;
	}
	
	public void addExp(Player invite,Player invited,boolean addexp1,boolean addexp2){
		
		if(addexp1){
			invite.addExp(getExp(invite), ExperienceManager.ADDEXP_REASON_Beer);
			CountryManager.getInstance().addExtraExp(invite, getExp(invite));
		}
		if(addexp2){
			invited.addExp(getExp(invited), ExperienceManager.ADDEXP_REASON_Beer);
		}
		if(FateManager.logger.isWarnEnabled()){
			FateManager.logger.warn("[国内论道加经验] [邀请人:"+(invite != null ?invite.getLogString() : "")+"] [被邀请人:"+(invited != null ?invited.getLogString():"")+"]");
		}
		
	}
	
	public long getExp(Player player){
		
		Map<Integer, Long> map = FateManager.getInstance().getExp1Map();
		if(map.get(player.getLevel()) != null){
			return map.get(player.getLevel());
		}else{
			return -1;
		}
	}
	
}
