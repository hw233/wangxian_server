package com.fy.engineserver.downcity.downcity3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.BOOTH_BATTLE_INFO_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
public class Option_Cost_Confirm extends Option {

	private int gType;
	
	public void doSelect(Game game, Player player) {
		CityPlayer city = BossCityManager.getInstance().getBattleInfos().get(player.getId());
		if(city == null){
			player.sendError(Translate.鼓励失败);
			return;
		}
		if(gType == 0){
			int currGuLi = (int)city.getPlayerPercent();
			Long cost = BossCityManager.getInstance().getpGuLi().get(currGuLi + 10);
			if(cost == null){
				player.sendError(Translate.鼓励已达最大);
				return;
			}
			if(BossCityManager.getInstance().getpGuLi().get(currGuLi) == null){
				player.sendError(Translate.鼓励比例配置错误);
				return;
			}
			
			try {
				BillingCenter.getInstance().playerExpense(player, cost.longValue() * 1000, CurrencyType.YINZI, ExpenseReasonType.副本鼓舞, "副本鼓舞");
				city.setPlayerPercent(city.getPlayerPercent() + 10);
				BossCityManager.logger.warn("[副本鼓舞] [个人鼓舞] [消费:{}] [比例:{}] [{}]",new Object[]{cost,city.getPlayerPercent(), player.getLogString()});
			} catch (NoEnoughMoneyException e) {
				//e.printStackTrace();
				player.sendError(Translate.元宝不足);
			} catch (BillFailedException e) {
				//e.printStackTrace();
				player.sendError(Translate.元宝不足);
			}
			
			
		}else if(gType == 1){
			int currGuLi = (int)city.getJiaZuPercent();
			Long cost = BossCityManager.getInstance().getjGuLi().get(currGuLi + 2);
			if(cost == null){
				player.sendError(Translate.鼓励已达最大);
				return;
			}
			if(BossCityManager.getInstance().getjGuLi().get(currGuLi) == null){
				player.sendError(Translate.鼓励比例配置错误);
				return;
			}
			try {
				BillingCenter.getInstance().playerExpense(player, cost.longValue() * 1000, CurrencyType.YINZI, ExpenseReasonType.副本鼓舞, "副本鼓舞");
				city.setJiaZuPercent(city.getJiaZuPercent() + 2);
				BossCityManager.logger.warn("[副本鼓舞] [家族鼓舞] [消费:{}] [比例:{}] [{}]",new Object[]{cost,city.getJiaZuPercent(), player.getLogString()});
			} catch (NoEnoughMoneyException e) {
				//e.printStackTrace();
				player.sendError(Translate.元宝不足);
			} catch (BillFailedException e) {
				//e.printStackTrace();
				player.sendError(Translate.元宝不足);
			}
		}
		
{
			
			int playerGuLi = 0;
			int jiaZuGuLi = 0;
			
			
			CityPlayer info = BossCityManager.getInstance().battleInfos.get(player.getId());
			CityJiaZu jiazuinfo = BossCityManager.getInstance().jiazuInfos.get(player.getJiazuId());
			if(info != null){
				playerGuLi = (int)info.getPlayerPercent();
			}
			if(jiazuinfo != null){
				jiaZuGuLi= (int)info.getJiaZuPercent();
			}
				
			int jiaZuJoins = 0;
			Set<JiazuMember> members = JiazuManager.getInstance().getJiazuMember(player.getJiazuId());
			if(members != null && members.size() > 0){
				for(JiazuMember member : members){
					if(member != null && BossCityManager.getInstance().battleInfos.get(member.getPlayerID()) != null){
						jiaZuJoins++;
					}
				}
			}
			
			String names [] = {""};
			long damages [] = {0};
			int rank = 0;
			long damage = 0;
			String name = "";
			
			if(BossCityManager.getInstance().battleInfos.size() > 0){
				if(gType == 0){
					CityPlayer[]  ds = BossCityManager.getInstance().battleInfos.values().toArray(new CityPlayer[]{});
					Arrays.sort(ds, new Comparator<CityPlayer>() {
						@Override
						public int compare(CityPlayer o1, CityPlayer o2) {
							return new Long(o2.getPlayerDamage()).compareTo(new Long(o1.getPlayerDamage()));
						}
					});
					int showNums = BossCityManager.getInstance().billboardShowNum;
					if(ds.length < BossCityManager.getInstance().billboardShowNum){
						showNums = ds.length;
					}
					if(showNums > 0){
//						CityPlayer[] showDatas = new CityPlayer[showNums];
						names = new String[showNums];
						damages = new long[showNums];
						for(int i=0;i<ds.length;i++){
							if(i < showNums){
//								showDatas[i] = ds[i];
								names[i] = ds[i].getPlayerName();
								damages[i] = ds[i].getPlayerDamage();
							}
							if(ds[i].getPlayerId() == player.getId()){
								rank = i + 1;
							}
						}
					}
					CityPlayer pinfo = BossCityManager.getInstance().battleInfos.get(player.getId());
					if(pinfo != null){
						damage = pinfo.getPlayerDamage();
						name = player.getName();
					}
				}
			}
			
			if(BossCityManager.getInstance().jiazuInfos.size() > 0){
				if(gType == 1){
					CityJiaZu[] ds = BossCityManager.getInstance().jiazuInfos.values().toArray(new CityJiaZu[]{});
					Arrays.sort(ds, new Comparator<CityJiaZu>() {
						@Override
						public int compare(CityJiaZu o1, CityJiaZu o2) {
							return new Long(o2.getJiaZuDamage()).compareTo(new Long(o1.getJiaZuDamage()));
						}
					});
					
					int showNums = BossCityManager.getInstance().billboardShowNum;
					if(ds.length < BossCityManager.getInstance().billboardShowNum){
						showNums = ds.length;
					}
					if(showNums > 0){
						CityJiaZu[] showDatas = new CityJiaZu[showNums];
						names = new String[showNums];
						damages = new long[showNums];
						for(int i=0;i<ds.length;i++){
							if(i < showNums){
								showDatas[i] = ds[i];
								names[i] = showDatas[i].getJiazuName();
								damages[i] = showDatas[i].getJiaZuDamage();
							}
							if(ds[i].getJiaZuId() == player.getJiazuId()){
								rank = i + 1;
							}
						}
					}
					CityJiaZu jinfo = BossCityManager.getInstance().jiazuInfos.get(player.getJiazuId());
					if(jinfo != null){
						damage = jinfo.getJiaZuDamage();
						name = jinfo.getJiazuName();
					}
				}
			}
			
			BOOTH_BATTLE_INFO_RES res = new BOOTH_BATTLE_INFO_RES(GameMessageFactory.nextSequnceNum(), gType, playerGuLi, jiaZuGuLi, jiaZuJoins, rank, damage, name, names, damages);
			player.addMessageToRightBag(res);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getgType() {
		return gType;
	}

	public void setgType(int gType) {
		this.gType = gType;
	}

}
