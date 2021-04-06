package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.sprite.Player;


/**
 * 
 * 银子道具
 * 
 */
public class MoneyProps extends Props{
	
	public int money;
	
	/**
	 * 0绑银，1银子  3工资
	 */
	public byte type;

	/**
	 * 使用金钱道具
	 * 
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		try{
			if(!super.use(game,player,ae)){
				return false;
			}
			if(type==2){
				long oldpoint = player.getChargePoints();
				player.setChargePoints(oldpoint+money);
				ArticleManager.logger.warn("[使用道具] [成功] ["+(this.getName()==null?"":this.getName())+"] ["+(this.getName_stat()==null?"":this.getName_stat())+"] [积分变化："+oldpoint+"-->"+player.getChargePoints()+"] [增加的积分:"+money+"] ["+player.getLogString()+"]");
			}else if (type == 3) {
				BillingCenter bc = BillingCenter.getInstance();
				bc.playerSaving(player, money, CurrencyType.GONGZI, SavingReasonType.MONEY_PROPS, "使用"+this.getName_stat());
			} else {
				BillingCenter bc = BillingCenter.getInstance();
				if(ae.isBinded()){
					bc.playerSaving(player, money, CurrencyType.BIND_YINZI, SavingReasonType.MONEY_PROPS, "使用"+this.getName_stat());
				}else{
					bc.playerSaving(player, money, CurrencyType.SHOPYINZI, SavingReasonType.MONEY_PROPS, "使用"+this.getName_stat());
				}
			}
			
		}catch(Exception ex){
			
		}
		return true;
	}
	
	/**
	 * 判断某个玩家是否可以使用此物品 子类可以重载此方法
	 * 返回null表示可以使用
	 * 返回字符串表示不能使用
	 * 字符串为不能使用的详细信息
	 * @param p
	 * @return
	 */
	public String canUse(Player p) {
		
		String resultStr = super.canUse(p);
		return resultStr;
	}
}
