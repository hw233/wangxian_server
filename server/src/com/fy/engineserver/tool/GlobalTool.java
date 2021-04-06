package com.fy.engineserver.tool;

import com.fy.engineserver.activity.dice.DiceManager;
import com.fy.engineserver.activity.fairyBuddha.challenge.FairyChallengeManager;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyManager;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.cityfight.CityFightManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;

public class GlobalTool {
	/**
	 * 限制所有传送和被传送功能
	 * @param player
	 * @return
	 */
	public static String isLimitAllTrans(Player player) {
		String result = null;
		try {
			if (FairyRobberyManager.inst.isPlayerInRobbery(player)) {
				return Translate.渡劫中不可传送;
			}
//			if (DisasterManager.getInst().isPlayerInGame(player)) {
//				return Translate.金猴天灾不可用;
//			}
			if(DiceManager.getInstance().isDiceGame(player)){
				return Translate.骰子副本不能拉人;
			}
			if(WolfManager.getInstance().isWolfGame(player)){
				return Translate.正在副本中提示;
			}
//			if(XianLingManager.instance.inXianLingGame(player)){
//				result = Translate.正在副本中提示;
//			}
			if(player.getCurrentGame() != null && player.getCurrentGame().gi != null){
				if(player.getCurrentGame().gi.name != null && player.getCurrentGame().gi.name.equals("jiefengBOSStiaozhanditu")){
					result = Translate.正在副本中提示;
				}
				if(DownCityManager2.instance.inCityGame(player.getCurrentGame().gi.name)){
					result = Translate.正在副本中提示;
				}
			}
		} catch (Exception e) {
			Game.logger.warn("[限制玩家所有传送与被传送] [异常] [" + player.getLogString() + "]", e);
		}
		return result;
	}
	
	/**
	 * 判断玩家是否可以传送(限制除回城以外其他任何传送方式-包括使用拉令以及渡劫按钮)
	 * @param playerId
	 * @return
	 */
	public static String verifyMapTrans(long playerId) {
		String result = null;
		try {
			if(FairyChallengeManager.getInst().isPlayerChallenging(playerId)) {
				result = Translate.挑战仙尊限制功能;
			} else {
				Player p = PlayerManager.getInstance().getPlayer(playerId);
				result = isLimitAllTrans(p);
			}
		} catch(Exception e) {
			Game.logger.error("[地图传送限制] [异常] [playerId:" + playerId + "]", e);
		}
		return result;
	}
	public static String verifyTransByOther(long playerId) {
		return verifyTransByOther(playerId, null);
	}
	/**
	 * 判断玩家是否可以被传送(拉令、召集等不弹窗)
	 * @param playerId
	 * @param game  使用拉令的人所在地图
	 * @return
	 */
	public static String verifyTransByOther(long playerId, Game game) {
		String result = null;
		try {
			if(FairyChallengeManager.getInst().isPlayerChallenging(playerId)) {
				result = Translate.挑战仙尊限制功能;
			} else{
				Player p = PlayerManager.getInstance().getPlayer(playerId);
				result = isLimitAllTrans(p);
				if(p!=null && p.getCurrentGame() != null){
					if(SealManager.getInstance().isSealDownCity(p.getCurrentGame().gi.name)){
						result = Translate.封印副本不能使用此功能;
					}
					if(DiceManager.getInstance().isDiceGame(p)){
						result = Translate.骰子副本不能拉人;
					}
//					if (DisasterManager.getInst().isPlayerInGame(p)) {
//						return Translate.金猴天灾不可用;
//					}
					if(WolfManager.getInstance().isWolfGame(p)){
						result = Translate.正在副本中提示;
					}
//					if(XianLingManager.instance.inXianLingGame(p)){
//						result = Translate.对方正在副本中;
//					}
					if(p.getCurrentGame().gi != null){
						if(p.getCurrentGame().gi.name.equals("jiefengBOSStiaozhanditu")){
							result = Translate.正在副本中提示;
						}
					}
					if(p.getCurrentGame().gi != null){
						if(p.getCurrentGame().gi.name.equals("zhanmotianyu")){
							result = Translate.正在副本中提示;
						}
					}
					if(p.getCurrentGame().gi != null){
						if(p.getCurrentGame().gi.name.equals("bingfenghuanjing")){
							result = Translate.正在副本中提示;
						}
					}
					if(p.getCurrentGame().gi != null){
						if(DownCityManager2.instance.inCityGame(p.getCurrentGame().gi.name)){
							result = Translate.正在副本中提示;
						}
					}
				}
				if (game != null) {
					try {
						boolean inCityFight = false;
						for (String str : CityFightManager.战斗地图) {
							if (str.equalsIgnoreCase(game.gi.name)) {
								inCityFight = true;
								break;
							}
						}
						if (inCityFight && p.getLevel() <= 40) {
							return Translate.城战不允许传送; 
						}
					} catch (Exception e) {
						Game.logger.error("[地图传送限制] [异常2] [playerId:" + playerId + "]", e);
					}
				}
			}
		} catch(Exception e) {
			Game.logger.error("[地图传送限制] [异常] [playerId:" + playerId + "]", e);
		}
		return result;
	}

}
