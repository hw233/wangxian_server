package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;

/**
 * 传送到某地
 * 
 * 
 *
 */
public class Option_DeliveToSamePlace extends Option implements NeedCheckPurview{

	//目地地
	String destinationMapName = "";
	String destinationAreaName = Translate.出生点;
	
	/**
	 * 0不包含本国，1包含本国
	 */
	int containCurrentCountry = 1;
	
	//一次跳转的价格
	int priceForOneStep;
	
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
		
		GameManager gm = GameManager.getInstance();
		GameInfo gi = null;
		if(this.getText().contains("新手城市传送")){
			destinationMapName = "";
			country = player.getCurrentGameCountry();
			destinationMapName = TransportData.getXinShouCityMap(country);
			if(destinationMapName == null || destinationMapName.isEmpty()){
				country = player.getCountry();
				player.transferGameCountry = country;
				destinationMapName = TransportData.getXinShouCityMap(player.getCountry());
			}
		}else if(this.getText().contains("国家主城传送")){
			destinationMapName = "";
			country = player.getCurrentGameCountry();
			destinationMapName = TransportData.getMainCityMap(country);
			if(destinationMapName == null || destinationMapName.isEmpty()){
				country = player.getCountry();
				player.transferGameCountry = country;
				destinationMapName = TransportData.getMainCityMap(player.getCountry());
			}
		}
		
		if(destinationMapName != null){
			gi = gm.getGameInfo(destinationMapName.trim());
		}

		MapArea area = null;

		if(gi != null && destinationAreaName != null){
			area = gi.getMapAreaByName(destinationAreaName.trim());
		}
		
		TaskManager.logger.warn("[npc传送] [按钮:"+this.getText()+"] [MapName:"+destinationMapName+"] [country:"+country+"] [pCountry:"+player.getCountry()+"] ["+player.transferGameCountry+"] ["+player.getLogString()+"]");
		if(gi == null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5192+destinationMapName+Translate.text_5193);
			player.addMessageToRightBag(hreq);
		}else if(area == null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5192+destinationAreaName+Translate.text_5194);
			player.addMessageToRightBag(hreq);
		}else{
			int k =  gm.getDinstanceFromA2B(player.getMapName(), gi.getMapName());
			if(k == -1){
				k = priceForOneStep;
			}else{
				k = (k+1) * priceForOneStep;
			}
	
//			BillingCenter bc = BillingCenter.getInstance();
			try{
//				bc.playerExpense(player, k, CurrencyType.GAME_MONEY, ExpenseReasonType.TRANSFER,new String[]{player.getMapName()+"-"+destinationMapName});
				
				if(country != -1){
					player.transferGameCountry = country;
				}
//				player.bcity = null;
				if(DownCityManager2.instance.cityMap.containsKey(new Long(player.getId()))){
					DownCityManager2.instance.cityMap.get(new Long(player.getId())).pMap().remove(new Long(player.getId()));
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

	@Override
	public boolean canSee(Player player) {
		if(this.getText().contains("边境")){
			if(player.getCurrentGameCountry() == country){
				return false;
			}
		}
		return true;
	}

}
