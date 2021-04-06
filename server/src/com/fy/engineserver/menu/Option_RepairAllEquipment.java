package com.fy.engineserver.menu;

import java.util.ArrayList;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NOTIFY_EQUIPMENT_CHANGE_REQ;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;

/**
 * 修理全部装备
 * 
 * 
 *
 */
public class Option_RepairAllEquipment extends Option{

	public int repairType;
	private int calPrice(Player player){
		
		return 0;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		//修理全部
		if(repairType == 0){
			//修理当前装备
			EquipmentColumn ec = player.getEquipmentColumns();
//			EquipmentColumn ec2 = player.getEquipmentColumns();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			int price = 0;
			try {
				for (int i = 0; i < ec.getEquipmentIds().length; i++) {
					if(ec.getEquipmentIds()[i] > 0){
						ArticleEntity aee = aem.getEntity(ec.getEquipmentIds()[i]);
//						EquipmentEntity ee = (EquipmentEntity)aem.getEntity(ec.getEquipmentIds()[i]);
						if(aee instanceof EquipmentEntity) {
							price += am.getEquipmentEntityRepairPrice(player,(EquipmentEntity)aee);
						}
					}
				}
				Knapsack knapsack = player.getKnapsack_common();
				if(knapsack != null){
					for (int i = 0; i < knapsack.getCells().length; i++) {
						if(knapsack.getCells()[i] != null && knapsack.getCells()[i].entityId > 0 && knapsack.getCells()[i].count > 0){
							ArticleEntity ae = aem.getEntity(knapsack.getCells()[i].entityId);
							if(ae instanceof EquipmentEntity){
								price += am.getEquipmentEntityRepairPrice(player,(EquipmentEntity)ae);
							}
						}
					}
				}
				knapsack = player.getKnapsack_fangbao();
				if(knapsack != null){
					for (int i = 0; i < knapsack.getCells().length; i++) {
						if(knapsack.getCells()[i] != null && knapsack.getCells()[i].entityId > 0 && knapsack.getCells()[i].count > 0){
							ArticleEntity ae = aem.getEntity(knapsack.getCells()[i].entityId);
							if(ae instanceof EquipmentEntity){
								price += am.getEquipmentEntityRepairPrice(player,(EquipmentEntity)ae);
							}
						}
					}
				}
				if(price == 0){
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.装备无需修理);
					player.addMessageToRightBag(hreq);
					return;
				}else{
					if(player.getRepairDiscount() > 0) {
						float tempF = player.getRepairDiscount() / 100F;
						int tempDiscount = (int) (price * tempF);
						if (tempDiscount > 0 && tempDiscount < price) {
							price -= tempDiscount;
						}
					}
					if(!player.bindSilverEnough(price)){
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.绑银不足装备修理失败);
						player.addMessageToRightBag(hreq);
						return;
					}
					BillingCenter bc = BillingCenter.getInstance();
					bc.playerExpense(player, price, CurrencyType.BIND_YINZI, ExpenseReasonType.REPAIR, "修理");
					for (int i = 0; i < ec.getEquipmentIds().length; i++) {
						ec.repaire(i);
					}
					ArrayList<EquipmentEntity> eeList = new ArrayList<EquipmentEntity>();
					knapsack = player.getKnapsack_common();
					if(knapsack != null){
						for (int i = 0; i < knapsack.getCells().length; i++) {
							if(knapsack.getCells()[i] != null && knapsack.getCells()[i].entityId > 0 && knapsack.getCells()[i].count > 0){
								ArticleEntity ae = aem.getEntity(knapsack.getCells()[i].entityId);
								if(ae instanceof EquipmentEntity){
									Article a = am.getArticle(ae.getArticleName());
									if(a instanceof Equipment){
										((EquipmentEntity)ae).setDurability(((Equipment)a).getDurability());
										eeList.add((EquipmentEntity)ae);
									}
								}
							}
						}
					}
					knapsack = player.getKnapsack_fangbao();
					if(knapsack != null){
						for (int i = 0; i < knapsack.getCells().length; i++) {
							if(knapsack.getCells()[i] != null && knapsack.getCells()[i].entityId > 0 && knapsack.getCells()[i].count > 0){
								ArticleEntity ae = aem.getEntity(knapsack.getCells()[i].entityId);
								if(ae instanceof EquipmentEntity){
									Article a = am.getArticle(ae.getArticleName());
									if(a instanceof Equipment){
										((EquipmentEntity)ae).setDurability(((Equipment)a).getDurability());
										eeList.add((EquipmentEntity)ae);
									}
								}
							}
						}
					}
					if(!eeList.isEmpty()){
						com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] ees = new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[eeList.size()];
						for(int i = 0; i < ees.length; i++){
							ees[i] = CoreSubSystem.translate(eeList.get(i));
						}
						NOTIFY_EQUIPMENT_CHANGE_REQ req = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), ees);
						player.addMessageToRightBag(req);
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.装备修理成功);
					player.addMessageToRightBag(hreq);
					try {
						EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.装备修理次数, 1L});
						EventRouter.getInst().addEvent(evt);
					} catch (Exception e) {
						PlayerAimManager.logger.error("[目标系统] [统计玩家装备修理次数异常] [" + player.getLogString() + "]", e);
					}
					return;
				}
			} catch (Exception ex) {
				ArticleManager.logger.error("[修理装备异常] [{}] [{}] [{}]",new Object[]{player.getUsername(),player.getId(),player.getName()},ex);
			}
		}else{
			//修理当前装备
			EquipmentColumn ec = player.getEquipmentColumns();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			int price = 0;
			try {
				for (int i = 0; i < ec.getEquipmentIds().length; i++) {
					if(ec.getEquipmentIds()[i] > 0){
						ArticleEntity aee = aem.getEntity(ec.getEquipmentIds()[i]);
//						EquipmentEntity ee = (EquipmentEntity)aem.getEntity(ec.getEquipmentIds()[i]);
						if(aee instanceof EquipmentEntity) {
							price += am.getEquipmentEntityRepairPrice(player,(EquipmentEntity)aee);
						}
					}
				}
				if(price == 0){
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.装备无需修理);
					player.addMessageToRightBag(hreq);
					return;
				}else{
					if(player.getRepairDiscount() > 0) {
						float tempF = player.getRepairDiscount() / 100F;
						int tempDiscount = (int) (price * tempF);
						if (tempDiscount > 0 && tempDiscount < price) {
							price -= tempDiscount;
						}
					}
					if(!player.bindSilverEnough(price)){
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.绑银不足装备修理失败);
						player.addMessageToRightBag(hreq);
						return;
					}
					BillingCenter bc = BillingCenter.getInstance();
					bc.playerExpense(player, price, CurrencyType.BIND_YINZI, ExpenseReasonType.REPAIR, "修理");
					for (int i = 0; i < ec.getEquipmentIds().length; i++) {
						ec.repaire(i);
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.装备修理成功);
					player.addMessageToRightBag(hreq);
					return;
				}
			} catch (Exception ex) {
				ArticleManager.logger.error("[修理装备异常] [{}] [{}] [{}]",new Object[]{player.getUsername(),player.getId(),player.getName()},ex);
			}
		}
	}
	


	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_修理全部装备;
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
		Option_RepairAllEquipment op = new Option_RepairAllEquipment();
		op.setColor(getColor());
		op.setIconId(getIconId());
		
		int priceForAll =calPrice(p);
		
		if(p.getBindSilver() < priceForAll){
			op.setColor(0xff0000);
		}
		op.setText(getText() + Translate.text_5199+priceForAll+Translate.text_2305);
	
		op.oldGame = game.getGameInfo().getName();
		
		return op;
	}
	
	
	
	
}
