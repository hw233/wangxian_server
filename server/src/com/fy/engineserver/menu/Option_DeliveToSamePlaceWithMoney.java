package com.fy.engineserver.menu;

import java.util.Calendar;

import com.fy.engineserver.activity.vipExpActivity.VipExpActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.charge.CardFunction;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

/**
 * 传送到某地
 * 
 * 
 *
 */
public class Option_DeliveToSamePlaceWithMoney extends Option implements NeedCheckPurview{

	//目地地
	String destinationMapName = "";
	String destinationAreaName = Translate.出生点;
	
	/**
	 * 0不包含本国，1包含本国
	 */
	int containCurrentCountry = 1;
	
	//一次跳转的价格
	int priceForOneStep;
	
	int playerLevel;
	
	/**
	 * 地图类型，1为体力地图，进入体力地图，先扣1点体力，2为宠物岛地图，3为万宝地图
	 */
	int gameType;
	
	int country = -1;
	
	public int getContainCurrentCountry() {
		return containCurrentCountry;
	}

	public void setContainCurrentCountry(int containCurrentCountry) {
		this.containCurrentCountry = containCurrentCountry;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		
		if(player.getMainSoul().getGrade() < playerLevel){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.translateString(Translate.最少需要到级才能进入, new String[][]{{Translate.COUNT_1,((playerLevel<=220 ? (playerLevel + "") : (Translate.仙 + (playerLevel - 220))))}}));
			player.addMessageToRightBag(hreq);
			return;
		}
		boolean freeEnterDuoBao = player.ownMonthCard(CardFunction.每日免费进出福地洞天);
		if(!freeEnterDuoBao && priceForOneStep > 0 && player.getSilver() + player.getBindSilver() < priceForOneStep){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.translateString(Translate.传送需要银子你的银子不足, new String[][]{{Translate.COUNT_1,BillingCenter.得到带单位的银两(priceForOneStep)}}));
			player.addMessageToRightBag(hreq);
			return;
		}
		if(gameType == 1 && player.getVitality() <= 0){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.最少需要1点体力才能进入);
			player.addMessageToRightBag(hreq);
			return;
		}
		
		
		Calendar calendar3 = Calendar.getInstance();
		calendar3.setTimeInMillis(System.currentTimeMillis());
		int day = calendar3.get(Calendar.DAY_OF_YEAR);
		
		
		calendar3.setTimeInMillis(player.getLastInTiLiDiTuTime());
		int day1 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day == day1) {
			if(gameType == 1 && player.getInTiLiDiTuTime() >= Player.DITU_TILI_MAX_TIME){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.您今天进入地图时间已满不能进入了);
				player.addMessageToRightBag(hreq);
				return;
			}
		}else{
			player.setInTiLiDiTuTime(0);
			player.setLastInTiLiDiTuTime(System.currentTimeMillis());
		}

		calendar3.setTimeInMillis(player.getLastInPetDiTuTime());
		int day2 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day == day2) {
			if(gameType == 2 && player.getInPetDiTuTime() >= Player.DITU_PET_MAX_TIME){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.您今天进入地图时间已满不能进入了);
				player.addMessageToRightBag(hreq);
				return;
			}
		}else{
			player.setInPetDiTuTime(0);
			player.setLastInPetDiTuTime(System.currentTimeMillis());
		}

		calendar3.setTimeInMillis(player.getLastInWanBaoDiTuTime());
		int day3 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day == day3) {
			if(gameType == 3 && player.getInWanBaoDiTuTime() >= player.getMaxTimeOfSilverGame()){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.您今天进入地图时间已满不能进入了);
				player.addMessageToRightBag(hreq);
				return;
			}
		}else{
			player.setInWanBaoDiTuTime(0);
			player.setLastInWanBaoDiTuTime(System.currentTimeMillis());
		}
		
		//仙蒂宝库
		calendar3.setTimeInMillis(player.getLastInXianDIDiTuTime());
		int day4 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day == day4) {
			if(gameType == 4 && player.getInXianDiDiTuTime() >= Player.DITU_XIANDI_MAX_TIME){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.您今天进入地图时间已满不能进入了);
				player.addMessageToRightBag(hreq);
				return;
			}
		}else{
			player.setInXianDiDiTuTime(0);
			player.setLastInXianDIDiTuTime(System.currentTimeMillis());
		}
		
		//八卦仙阙
		calendar3.setTimeInMillis(player.getLastInBaGuaXianQueTime());
		int day5 = calendar3.get(Calendar.DAY_OF_YEAR);
		if (day == day5) {
			if(gameType == 5 && player.getInBaGuaXianQueTime() >= Player.DITU_BAGUAXIANQUE_MAX_TIME){
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.您今天进入地图时间已满不能进入了);
				player.addMessageToRightBag(hreq);
				return;
			}
		}else{
			player.setInBaGuaXianQueTime(0);
			player.setLastInBaGuaXianQueTime(System.currentTimeMillis());
		}
		
		GameManager gm = GameManager.getInstance();
		GameInfo gi = null;
		if(destinationMapName != null){
			gi = gm.getGameInfo(destinationMapName.trim());
		}

		MapArea area = null;

		if(gi != null && destinationAreaName != null){
			area = gi.getMapAreaByName(destinationAreaName.trim());
		}
		

		if(gi == null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5192+destinationMapName+Translate.text_5193);
			player.addMessageToRightBag(hreq);
		}else if(area == null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5192+destinationAreaName+Translate.text_5194);
			player.addMessageToRightBag(hreq);
		}else{

			BillingCenter bc = BillingCenter.getInstance();
			try{
				if(priceForOneStep > 0){
					int expenseType = -1;
					if(gameType == 2) {
						expenseType = VipExpActivityManager.shengshou_expends_activity;
					} else if (gameType == 3) {
						expenseType = VipExpActivityManager.duobao_expends_activity;
					}
					if(gameType == 3 && freeEnterDuoBao){
						GamePlayerManager.logger.warn("[月卡免费进入福地洞天] ["+player.getLogString()+"]");
					}else{
						bc.playerExpense(player, priceForOneStep, CurrencyType.BIND_YINZI, ExpenseReasonType.TRANSFER,"进入付费地图", expenseType);
					}
				}
				if(gameType == 1){
					player.updateLastInTiLiDiTuTime(System.currentTimeMillis(), 1);
					player.setVitality(player.getVitality() - 1);
					if (GamePlayerManager.logger.isWarnEnabled()) {
						GamePlayerManager.logger.warn("["+player.getLogString()+"]" + "[地图扣除体力] [扣除后:" + player.getVitality() + "]");
					}
				}
				if(country != -1){
					player.transferGameCountry = country;
				}
				TransportData td = new TransportData(0,0,0,0,destinationMapName.trim(),(int)(area.getX() + Math.random()*area.getWidth()),
						(int)(area.getY() + Math.random()*area.getHeight()));
				game.transferGame(player, td);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		
		}
	}
	
	@Override
	public Option copy(Game game, Player p) {
		if(containCurrentCountry == 0){
			if(game.country == country){
				return null;
			}
		}
		return super.copy(game, p);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_传送到某地;
	}

	public void setOId(int oid) {
	}
	
	
	public String getDestinationMapName() {
		return destinationMapName;
	}

	public void setDestinationMapName(String destinationMapName) {
		this.destinationMapName = destinationMapName;
	}


	public String getDestinationAreaName() {
		return destinationAreaName;
	}


	public void setDestinationAreaName(String destinationAreaName) {
		this.destinationAreaName = destinationAreaName;
	}


	public int getPriceForOneStep() {
		return priceForOneStep;
	}


	public void setPriceForOneStep(int priceForOneStep) {
		this.priceForOneStep = priceForOneStep;
	}
	
	public String toString(){
		return Translate.text_5197 + this.destinationMapName + ":" + this.destinationAreaName;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		return player.getLevel() >= playerLevel;
	}

}
