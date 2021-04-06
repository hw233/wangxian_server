package com.fy.engineserver.menu;

import java.util.HashMap;
import java.util.Iterator;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.entity.client.BagInfo4Client;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.UsingPropsAgent.PropsCategoryCoolDown;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.PROPS_CD_MODIFY_REQ;
import com.fy.engineserver.message.QUERY_KNAPSACK_RES;
import com.fy.engineserver.sprite.Player;
/**
 * 背包扩展功能
 * 
 * 
 *
 */
public class Option_Knapsack_ConfirmAddCell extends Option{

	public int knapsackIndex;
	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){

		Knapsack knapsack = player.getKnapsack_common();
		Cell[] cells = knapsack.getCells();
		int num = 0;
		if(cells != null){
			num = cells.length;
		}
		if(knapsackIndex <= 0 || knapsackIndex > Knapsack.MAX_CELL_NUM){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.请不要使用外挂);
			player.addMessageToRightBag(hreq);
			return;
		}
		if(num >= knapsackIndex){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.目前不提供缩包服务);
			player.addMessageToRightBag(hreq);
			return;
		}
		int count = knapsackIndex - num;
		int cost = Game.得到扩包所需银两(knapsackIndex, num);
		try{
			BillingCenter bc = BillingCenter.getInstance();
			bc.playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.EXPEND_KNAPSACK, "扩展背包");
		}catch(Exception ex){
			String des = Translate.银子不足;
			BillingCenter.银子不足时弹出充值确认框(player, Translate.银子不足是否去充值);
			if(ArticleManager.logger.isInfoEnabled())
				ArticleManager.logger.info("[背包扩展功能] [失败] [{}] [{}] [{}] [{}]", new Object[] {des, player.getUsername(), player.getName(), player.getId()});
			return;
		}
		player.getKnapsack_common().addCells(count);
		String description = Translate.translateString(Translate.扩展背包成功提示,new String[][]{{Translate.COUNT_1,knapsackIndex+""}});
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, description);
		player.addMessageToRightBag(hreq);
		if(ArticleManager.logger.isInfoEnabled())
			ArticleManager.logger.info("[背包扩展功能] [成功] [{}] [{}] [{}] [{}]", new Object[] {description, player.getUsername(), player.getName(), player.getId()});
		
		//发送包裹信息给客户端
		Knapsack[] sack = player.getKnapsacks_common();
		if (sack == null) {
			if(ArticleManager.logger.isInfoEnabled())
				ArticleManager.logger.info("[背包扩展功能] [返回包裹信息失败] [没有包裹] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getName(), player.getId() });
			return;
		}
		BagInfo4Client[] bagInfo4Client = new BagInfo4Client[sack.length];
		for (int i = 0; i < sack.length; i++) {
			knapsack = sack[i];
			bagInfo4Client[i] = new BagInfo4Client();
			bagInfo4Client[i].setBagtype((byte) i);
			if (i >= 3) {
				bagInfo4Client[i].setFangbaomax((short) 0);
			}
			if (knapsack != null) {
				Cell kcs[] = knapsack.getCells();
				long ids[] = new long[kcs.length];
				short counts[] = new short[kcs.length];
				for (int j = 0; j < kcs.length; j++) {
					if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
						ids[j] = kcs[j].getEntityId();
						counts[j] = (short) kcs[j].getCount();
					} else {
						ids[j] = -1;
						counts[j] = 0;
					}
				}
				bagInfo4Client[i].setEntityId(ids);
				bagInfo4Client[i].setCounts(counts);
			}
		}

		HashMap<String, PropsCategoryCoolDown> map = player.getUsingPropsAgent().getCooldownTable();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String categoryName = it.next();
			PropsCategoryCoolDown cd = map.get(categoryName);
			if (cd != null && cd.end >= com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 5000) {
				PROPS_CD_MODIFY_REQ req2 = new PROPS_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), player.getId(), categoryName, cd.start, (byte) 0);
				player.addMessageToRightBag(req2);
			}
		}
		ArticleManager am = ArticleManager.getInstance();
		QUERY_KNAPSACK_RES res = new QUERY_KNAPSACK_RES(GameMessageFactory.nextSequnceNum(), bagInfo4Client, am.getAllPropsCategory());
		player.addMessageToRightBag(res);
		if(AchievementManager.getInstance() != null){
			AchievementManager.getInstance().record(player, RecordAction.扩展背包格数, count);
		}
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
