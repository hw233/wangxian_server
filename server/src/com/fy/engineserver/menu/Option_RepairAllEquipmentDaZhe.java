package com.fy.engineserver.menu;

import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 修理全部装备
 * 
 * 
 *
 */
public class Option_RepairAllEquipmentDaZhe extends Option{
	/**
	 * 指定的声望名称
	 */
	String repName;
	
	/**
	 * 指定的声望每个级别对应的打折的描述
	 * 1=1.0,2=0.95,3=0.90,4=0.85
	 */
	String repLevel;
	
	public String getRepLevel() {
		return repLevel;
	}

	public void setRepLevel(String repLevel) {
		this.repLevel = repLevel;
		if(repLevel != null){
			repLevel = repLevel.trim();
			String[] vs = repLevel.split(",");
			for(int i = 0 ;i < vs.length && vs != null ; i ++){
				String[] ls = vs[i].split("=");
				if(ls != null && ls.length == 2){
					int level = 0;
					double discount = 0;
					
					try {
						level = Integer.parseInt(ls[0]);
					} catch (Exception e) {
//						Game.logger.error("[以打折价格修理所有装备配置出错] [级别配置错误] ["+ls[0]+"]");
						Game.logger.error("[以打折价格修理所有装备配置出错] [级别配置错误] [{}]", new Object[]{ls[0]});
						continue;
					}
					
					try {
						discount = Double.parseDouble(ls[1]);
					} catch (Exception e) {
//						Game.logger.error("[以打折价格修理所有装备配置出错] [折扣配置错误] ["+ls[1]+"]");
						Game.logger.error("[以打折价格修理所有装备配置出错] [折扣配置错误] [{}]", new Object[]{ls[1]});
						continue;
					}
					
					levelMap.put(level, discount);
				}else{
//					Game.logger.error("[以打折价格修理所有装备配置出错] [级别的打折数据配置错误] ["+vs[i]+"]");		
					Game.logger.error("[以打折价格修理所有装备配置出错] [级别的打折数据配置错误] [{}]", new Object[]{vs[i]});		
				}
			}
		}else{
			Game.logger.error("[以打折价格修理所有装备配置出错] [没有配置每个级别的打折数据]");
		}
	}

	public String getRepName() {
		return repName;
	}

	public void setRepName(String repName) {
		this.repName = repName;
	}
	Map<Integer , Double> levelMap = new HashMap<Integer, Double>();

	
	private int calPrice(Player player){
		
		return 0;
	}

	private int calDaZhePrice(Player player){
		
		return 0;
	}
	
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){}
	


	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_打折修理所有装备;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return "" ;
	}
	
	String oldGame = "";

	/**
	 * 为玩家copy一个选项出来，特殊的选项可以重载此方法
	 * 
	 * 如果传送，修改，治疗，可以根据玩家的具体信息来追加要花多少钱
	 * 
	 * @param p
	 * @return
	 */
	public Option copy(Game game,Player p){
		Option_RepairAllEquipmentDaZhe op = new Option_RepairAllEquipmentDaZhe();
		op.setColor(getColor());
		op.setIconId(getIconId());
		op.setRepName(this.repName);
		op.setRepLevel(this.repLevel);
		
		int priceForAll =calPrice(p);
		int discountPriceForAll = calDaZhePrice(p);
		
		if(p.getBindSilver() < priceForAll){
			op.setColor(0xff0000);
		}
		op.setText(getText() + Translate.text_5199+discountPriceForAll+Translate.text_2305 + (discountPriceForAll < priceForAll ? Translate.text_5392+(priceForAll-discountPriceForAll)+Translate.text_148 : "" ));
	
		op.oldGame = game.getGameInfo().getName();
		
		return op;
	}
	
}
