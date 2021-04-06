package com.fy.engineserver.menu;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.TOURNAMENT_RANK_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tournament.data.OneTournamentData;
import com.fy.engineserver.tournament.data.TournamentRankDataClient;
import com.fy.engineserver.tournament.manager.TournamentManager;

/**
 * 比武排名
 * 
 * 
 *
 */
public class Option_Battle_BiWuRank extends Option {

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(player == null){
			return;
		}

		TournamentManager tm = TournamentManager.getInstance();

		//前50的排名
		List<TournamentRankDataClient> list = new ArrayList<TournamentRankDataClient>(0);
		String myRankInWeek = Translate.排名未上榜;
		int myPointInWeek = 0;
		int myWinsInWeek = 0;
		int myLostsInWeek = 0;
		if(tm != null){
			list = tm.得到本周按积分排名的前100名人的数据_客户端();
			if(list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					TournamentRankDataClient dc = list.get(i);
					if(dc != null && dc.getId() == player.getId()){
						myRankInWeek = (i+1)+"";
						break;
					}
				}
			}
			OneTournamentData optd = tm.得到玩家竞技数据(player.getId());
			if(optd != null){
				int cw = TournamentManager.得到一年中的第几个星期_周日并到上星期(System.currentTimeMillis());
				if(cw == optd.currentWeek){
					myPointInWeek = optd.currentTournamentPoint;
					myWinsInWeek = optd.currentWinCount;
					myLostsInWeek = optd.currentLostCount;
				}
			}
		}
		boolean isReward = tm.是否可以领取奖励(player);
		TournamentManager.logger.warn("[武圣争夺排名测试] [玩家:{}] [id:{}] [myRankInWeek:{}] [myPointInWeek:{}] [myWinsInWeek:{}] [myLostsInWeek:{}] [是否可以领取奖励:{}]",new Object[]{player.getName(),player.getId(),myRankInWeek,myPointInWeek,myWinsInWeek,myLostsInWeek,isReward});
		TOURNAMENT_RANK_REQ trr = new TOURNAMENT_RANK_REQ(GameMessageFactory.nextSequnceNum(), list.toArray(new TournamentRankDataClient[0]), myRankInWeek, myPointInWeek, myWinsInWeek, myLostsInWeek,isReward);
		player.addMessageToRightBag(trr);
	}
	
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doInput(Game game, Player player, String inputContent){}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_比武进比武场;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5035 ;
	}

}
