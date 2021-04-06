package com.fy.engineserver.datasource.article.manager;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.LeaveExpProps;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_ExchangeLeaveTimeToExp;
import com.fy.engineserver.menu.Option_UseBindEquipment;
import com.fy.engineserver.menu.Option_UseBindProps;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 道具使用代理，负责使用道具
 * 管理道具的读条和冷却
 * 
 * 并发送指令给服务器
 * 
 * 
 */
public class UsingPropsAgent {
//
	public static class PropsCategoryCoolDown {
		public long start;
		public long end;

		public PropsCategoryCoolDown(long start, long end) {
			this.start = start;
			this.end = end;
		}

	}

	java.util.HashMap<String, PropsCategoryCoolDown> cooldownTable = new java.util.HashMap<String, PropsCategoryCoolDown>();

	public java.util.HashMap<String, PropsCategoryCoolDown> getCooldownTable() {
		return cooldownTable;
	}

	Player owner;

	public UsingPropsAgent(Player owner) {
		this.owner = owner;
	}

	/**
	 * 使用背包中第几个单元格的道具或者装备
	 * 
	 * 如果对应的背包格为空，那么此方法直接返回
	 * 如果对应的背包格为装备，那么就装载此装备
	 * 如果对应的背包格为道具，那么就使用此道具，并记录cooldown
	 * @param g
	 * @param sack
	 * @param index
	 */
	public void use(long heartBeatStartTime, Game game, byte operation, int knapsackIndex, int index, int soulType) {
		Knapsack sack = null;
		if(knapsackIndex == 1){
			sack = owner.getPetKnapsack();
		}else{
			sack = owner.getKnapsack_common();
		}
		if (sack == null) {
//			ArticleManager.logger.warn("[use] [fail] [sack == null] [-] [player:" + owner.getId() + "] [" + owner.getName() + "] [operation:" + operation + "] [index:" + index + "] ");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[use] [fail] [sack == null] [-] [player:{}] [{}] [operation:{}] [index:{}] ", new Object[]{owner.getId(),owner.getName(),operation,index});
			return;
		}
		ArticleEntity ae = sack.getArticleEntityByCell(index);
		ArticleManager am = ArticleManager.getInstance();
		if (ae == null) {
//			ArticleManager.logger.warn("[use] [fail] [entity_not_exist] [-] [player:" + owner.getId() + "] [" + owner.getName() + "] [operation:" + operation + "] [index:" + index + "] ");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[use] [fail] [entity_not_exist] [-] [player:{}] [{}] [operation:{}] [index:{}] ", new Object[]{owner.getId(),owner.getName(),operation,index});
			return;
		}
		if (ae.getArticleName() == null) {
//			ArticleManager.logger.warn("[use] [fail] [物体存在，但没有物种名] [-] [player:" + owner.getId() + "] [" + owner.getName() + "] [operation:" + operation + "] [index:" + index + "] ");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[use] [fail] [物体存在，但没有物种名] [-] [player:{}] [{}] [operation:{}] [index:{}] ", new Object[]{owner.getId(),owner.getName(),operation,index});
			return;
		}
		Article a = am.getArticle(ae.getArticleName());
		if (a == null) {
//			ArticleManager.logger.warn("[use] [fail] [没有名为【" + ae.getArticleName() + "】的物品] [-] [player:" + owner.getId() + "] [" + owner.getName() + "] [operation:" + operation + "] [index:" + index + "] ");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[use] [fail] [没有名为【{}】的物品] [-] [player:{}] [{}] [operation:{}] [index:{}] ", new Object[]{ae.getArticleName(),owner.getId(),owner.getName(),operation,index});
			return;
		}
		if(ArticleManager.logger.isInfoEnabled()){
			ArticleManager.logger.info("[use] [trace1] [{}] [player:{}] [{}] [operation:{}] [index:{}]  [{}]", new Object[]{a.getName(),owner.getId(),owner.getName(),operation,index,ae});
		}
		
		if(MagicWeaponManager.logger.isDebugEnabled()) {
			MagicWeaponManager.logger.debug("[使用物品] [" + owner.getName() + "][" + (ae instanceof NewMagicWeaponEntity) + "]");
		}
		if (ae instanceof EquipmentEntity || ae instanceof NewMagicWeaponEntity) {
			if (a instanceof Equipment) {
				if (((Equipment) a).getBindStyle() == Article.BINDTYPE_USE) {
					if (!((EquipmentEntity) ae).isBinded()) {
						WindowManager mm = WindowManager.getInstance();
						MenuWindow mw = mm.createTempMenuWindow(600);
						mw.setDescriptionInUUB(Translate.text_3850);
						Option_UseBindEquipment option = new Option_UseBindEquipment();
						option.setIconId("133");
						option.setOwner(owner);
						option.setIndexOfKnapsack(index);
						option.setText(Translate.确定);
						Option[] options = new Option[] { option, new Option_Cancel() };
						mw.setOptions(options);
						CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						owner.addMessageToRightBag(creq);
						return;
					}
				}
				owner.useItemOfKnapsack(game, knapsackIndex, index, ae, soulType);
			} else if(ae instanceof NewMagicWeaponEntity) {
				owner.useItemOfKnapsack(game, knapsackIndex, index, ae, soulType);
			} else{
//				ArticleManager.logger.warn("[use] [fail] [[" + ((EquipmentEntity) ae).getArticleName() + "]不是装备] [-] [player:" + owner.getId() + "] [" + owner.getName() + "] [operation:" + operation + "] [index:" + index + "] ");
				if(ArticleManager.logger.isWarnEnabled())
					ArticleManager.logger.warn("[use] [fail] [[{}]不是装备] [-] [player:{}] [{}] [operation:{}] [index:{}] ", new Object[]{((EquipmentEntity) ae).getArticleName(),owner.getId(),owner.getName(),operation,index});
				return;
			}

		} else if (ae instanceof PropsEntity) {
//			if (owner.getDuelFieldState() > 0) {
//				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_3851);
//				owner.addMessageToRightBag(hreq);
//				return;
//			}
			PropsEntity pe = (PropsEntity) ae;
			if (isDuringCoolDown(pe, heartBeatStartTime)) {
				
				if(ae instanceof PetPropsEntity){
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.宠物道具正在冷却);
					owner.addMessageToRightBag(hreq);
				}else{
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.道具正在冷却);
					owner.addMessageToRightBag(hreq);
				}
//				ArticleManager.logger.warn("[use] [fail] [during_cool_down] [" + pe.getArticleName() + "] [player:" + owner.getId() + "] [" + owner.getName() + "] [operation:" + operation + "] [index:" + index + "] ");
				if(ArticleManager.logger.isWarnEnabled())
					ArticleManager.logger.warn("[use] [fail] [during_cool_down] [{}] [player:{}] [{}] [operation:{}] [index:{}] ", new Object[]{pe.getArticleName(),owner.getId(),owner.getName(),operation,index});
				
				return;
			}
			// 离线经验道具特殊操作
			if (a instanceof LeaveExpProps) {
				WindowManager mm = WindowManager.getInstance();
				MenuWindow mw = mm.createTempMenuWindow(600);
				mw.setDescriptionInUUB(Translate.text_3852 + owner.getTotalOfflineTime() + Translate.text_3853 + ae.getArticleName() + Translate.text_3854 + ((LeaveExpProps) a).getExchangeableOfflineTime() + Translate.text_3855 + ((LeaveExpProps) a).exchangeExp(owner.getLevel(), owner.getTotalOfflineTime()) + Translate.text_3856);
				Option_ExchangeLeaveTimeToExp option = new Option_ExchangeLeaveTimeToExp();
				option.setOwner(owner);
				option.setIndexOfKnapsack(index);
				option.setHeartBeatStartTime(heartBeatStartTime);
				option.setCooldownTable(cooldownTable);
				option.setText(Translate.确定);
				Option[] options = new Option[] { option, new Option_Cancel() };
				mw.setOptions(options);
				CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
				owner.addMessageToRightBag(creq);

				return;

			} else {
				if (a.getBindStyle() == Article.BINDTYPE_USE) {
					if (!ae.isBinded()) {
						WindowManager mm = WindowManager.getInstance();
						MenuWindow mw = mm.createTempMenuWindow(600);
						mw.setDescriptionInUUB(Translate.text_3858);
						Option_UseBindProps option = new Option_UseBindProps();
						option.setOwner(owner);
						option.setIndexOfKnapsack(index);
						option.setHeartBeatStartTime(heartBeatStartTime);
						option.setCooldownTable(cooldownTable);
						option.setText(Translate.确定);
						Option[] options = new Option[] { option, new Option_Cancel() };
						mw.setOptions(options);
						CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						owner.addMessageToRightBag(creq);
						return;
					} else {
						owner.useItemOfKnapsack(game, knapsackIndex, index, ae, soulType);
					}
				} else {
					owner.useItemOfKnapsack(game, knapsackIndex, index, ae, soulType);
				}
			}

		}
	}

	/**
	 * 续费
	 * @param g
	 * @param sack
	 * @param index
	 */
	public void deductMoneyForContinueUseArticle(Game game, Player player, int index) {
// GameServerManager gsm = GameServerManager.getInstance();
// if(gsm != null && gsm.isCrossServer(GameConstants.getInstance().getServerName())){
// HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translate.text_2991);
// player.addMessageToRightBag(error);
// ArticleManager.logger.warn("[续费] [失败] [user:"+player.getUsername()+"] ["+player.getName()+"] [playerId:"+player.getId()+"] [在跨服服务器，不能续费");
// return;
// }
// Knapsack sack = owner.getKnapsack();
// ArticleEntity ae = sack.getArticleEntityByCell(index);
// ArticleManager am = ArticleManager.getInstance();
// if(ae == null){
// ArticleManager.logger.warn("[续费] [fail] [entity_not_exist] [-] [player:"+owner.getId()+"] ["+owner.getName()+"][index:"+index+"]");
// return;
// }
// if(ae.getArticleName() == null){
// ArticleManager.logger.warn("[续费] [fail] [物体存在但物体的物种名为空] [-] [player:"+owner.getId()+"] ["+owner.getName()+"][index:"+index+"]");
// return;
// }
// Article a = am.getArticle(ae.getArticleName());
// if(a == null){
// ArticleManager.logger.warn("[续费] [fail] [没有名为【"+ae.getArticleName()+"】的物品] [-] [player:"+owner.getId()+"] ["+owner.getName()+"][index:"+index+"]");
// return;
// }
// if(!a.isHaveValidDays() || a.getMaxValidDays() == 0){
// ArticleManager.logger.warn("[续费] [fail] 【"+ae.getArticleName()+"】没有有效期限制 [-][player:"+owner.getId()+"] ["+owner.getName()+"][index:"+index+"]");
// return;
// }
// BillingCenter bc = BillingCenter.getInstance();
// if(bc != null){
// if(a.getValidNeedRuanbao() > 0){
// try {
// bc.playerExpense(player,a.getValidNeedRuanbao(),CurrencyType.RMB_YUANBAO,ExpenseReasonType.DEDUCT_CONTINUE_USE_ARTICLE,new String[]{a.getName(),""+a.getValidNeedRuanbao()});
// } catch (NoEnoughMoneyException e) {
// HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translate.text_3859+a.getValidNeedRuanbao()+Translate.translate.text_3766);
// player.addMessageToRightBag(error);
// return;
// } catch (BillFailedException e) {
// e.printStackTrace();
// return;
// }
// if(a.getActivationOption() == Article.ACTIVATION_OPTION_CREATE){
// ae.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+a.getMaxValidDays()*60000);
// //把给客户端发送消息的标记设置为false，这样在人物心跳里检测到该物体失效时会给客户端发消息
// ae.setSentMessageFlag(false);
// }else{
// ae.setInValidTime(0);
// //把给客户端发送消息的标记设置为false，这样在人物心跳里检测到该物体失效时会给客户端发消息
// ae.setSentMessageFlag(false);
// }
// long articleId = ae.getId();
// String description = "";
// if (ae instanceof EquipmentEntity) {
// description = AritcleInfoHelper.generate(player, (EquipmentEntity) ae);
// } else if (ae instanceof PropsEntity) {
// description = AritcleInfoHelper.generate(player, (PropsEntity) ae);
// } else {
// description = AritcleInfoHelper.generate(player, ae);
// }
// player.addMessageToRightBag( new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), articleId, description));
// HINT_REQ error = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translate.text_3860+a.getValidNeedRuanbao()+Translate.translate.text_3766);
// player.addMessageToRightBag(error);
// Collection<com.fy.engineserver.datasource.props.client.ArticleEntity> articleList = new ArrayList<com.fy.engineserver.datasource.props.client.ArticleEntity>();
// Collection<com.fy.engineserver.datasource.props.client.PropsEntity> propsList = new ArrayList<com.fy.engineserver.datasource.props.client.PropsEntity>();
// Collection<com.fy.engineserver.datasource.props.client.EquipmentEntity> equipmentList = new ArrayList<com.fy.engineserver.datasource.props.client.EquipmentEntity>();
// ArticleEntity entity = ae;
// if (entity instanceof PropsEntity) {
// com.fy.engineserver.datasource.props.client.PropsEntity p = CoreSubSystem.translate((PropsEntity) entity);
// if (p != null) {
// propsList.add(p);
// } else {
// ArticleManager.logger.warn("[续费物品查询] [" + entity.getArticleName() + "] [ID=" + entity.getId() + "] [道具物品转换失败][player:"+owner.getId()+"] ["+owner.getName()+"]");
// }
// } else if (entity instanceof EquipmentEntity) {
// com.fy.engineserver.datasource.props.client.EquipmentEntity p = CoreSubSystem.translate((EquipmentEntity) entity);
// if (p != null) {
// equipmentList.add(p);
// } else {
// ArticleManager.logger.warn("[续费物品查询] [" + entity.getArticleName() + "] [ID=" + entity.getId() + "] [装备物品转换失败][player:"+owner.getId()+"] ["+owner.getName()+"]");
// }
// } else {
// com.fy.engineserver.datasource.props.client.ArticleEntity p = CoreSubSystem.translate((ArticleEntity) entity);
// if (p != null) {
// articleList.add(p);
// } else {
// ArticleManager.logger.warn("[续费物品查询] [" + entity.getArticleName() + "] [ID=" + entity.getId() + "] [普通物品转换失败][player:"+owner.getId()+"] ["+owner.getName()+"]");
// }
// }
// player.addMessageToRightBag(new QUERY_ARTICLE_RES(
// GameMessageFactory.nextSequnceNum(),
// articleList.toArray(new com.fy.engineserver.datasource.props.client.ArticleEntity[articleList
// .size()]),
// propsList
// .toArray(new com.fy.engineserver.datasource.props.client.PropsEntity[propsList.size()]),
// equipmentList
// .toArray(new com.fy.engineserver.datasource.props.client.EquipmentEntity[equipmentList
// .size()])));
// }else{
// ArticleManager.logger.warn("[续费] [fail] [【"+ae.getArticleName()+"】的续费元宝为0] [-] [player:"+owner.getId()+"] ["+owner.getName()+"][index:"+index+"]");
// return;
// }
// }else{
// ArticleManager.logger.warn("[续费] [fail] [消费充值中心初始化失败] [-] [player:"+owner.getId()+"] ["+owner.getName()+"]");
// return;
// }
//
	}

	/**
	 * 续费提示信息
	 * @param g
	 * @param sack
	 * @param index
	 */
	public void giveMoneyForContinueUseArticle(long heartBeatStartTime, Game game, byte operation, int index) {
// Knapsack sack = owner.getKnapsack();
// ArticleEntity ae = sack.getArticleEntityByCell(index);
// ArticleManager am = ArticleManager.getInstance();
// if(ae == null){
// ArticleManager.logger.warn("[续费提示信息] [fail] [entity_not_exist] [-] [player:"+owner.getId()+"] ["+owner.getName()+"] [operation:"+operation+"] [index:"+index+"] [suit:"+suit+"]");
// return;
// }
// if(ae.getArticleName() == null){
// ArticleManager.logger.warn("[续费提示信息] [fail] [物体存在但物体的物种名为空] [-] [player:"+owner.getId()+"] ["+owner.getName()+"] [operation:"+operation+"] [index:"+index+"] [suit:"+suit+"]");
// return;
// }
// Article a = am.getArticle(ae.getArticleName());
// if(a == null){
// ArticleManager.logger.warn("[续费提示信息] [fail] [没有名为【"+ae.getArticleName()+"】的物品] [-] [player:"+owner.getId()+"] ["+owner.getName()+"] [operation:"+operation+"] [index:"+index+"] [suit:"+suit+"]");
// return;
// }
// if(!a.isHaveValidDays()){
// ArticleManager.logger.warn("[续费提示信息] [fail] [【"+ae.getArticleName()+"】没有时间限制] [-] [player:"+owner.getId()+"] ["+owner.getName()+"] [operation:"+operation+"] [index:"+index+"] [suit:"+suit+"]");
// return;
// }
//
// if(a.getInvalidAfterAction() == Article.INVALID_AFTER_CONTINUE){
// if(ae.getInValidTime() != 0 && ae.getInValidTime() < com.fy.engineserver.gametime.SystemTime.currentTimeMillis()){
// WindowManager mm = WindowManager.getInstance();
// MenuWindow mw = mm.createTempMenuWindow(600);
// mw.setDescriptionInUUB("【"+ae.getArticleName()+Translate.translate.text_3861+a.getValidNeedRuanbao()+Translate.translate.text_3862);
// Option_GiveMoneyForContinueUseArticle option = new Option_GiveMoneyForContinueUseArticle();
// option.setOwner(owner);
// option.setEntity(ae);
// option.setIndexOfKnapsack(index);
// mw.setOptions(new Option[]{option,new Option_UseCancel()});
//
// CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),
// mw.getId(),mw.getDescriptionInUUB(),new String[]{Translate.translate.text_68,Translate.translate.text_364});
// owner.addMessageToRightBag(req);
// return;
// }else{
// ArticleManager.logger.warn("[续费提示信息] [fail] [【"+ae.getArticleName()+"】还没有失效] [-] [player:"+owner.getId()+"] ["+owner.getName()+"] [operation:"+operation+"] [index:"+index+"] [suit:"+suit+"]");
// return;
// }
// }else{
// ArticleManager.logger.warn("[续费提示信息] [fail] [【"+ae.getArticleName()+"】失效后不能续费] [-] [player:"+owner.getId()+"] ["+owner.getName()+"] [operation:"+operation+"] [index:"+index+"] [suit:"+suit+"]");
// return;
// }
	}

	public long[] getCoolDownData(PropsEntity s, long heartBeatStartTime) {
	 ArticleManager am = ArticleManager.getInstance();
	 Props pp = (Props)am.getArticle(s.getArticleName());
	 PropsCategoryCoolDown pccd = (PropsCategoryCoolDown)cooldownTable.get(pp.getCategoryName());
	 if(pccd != null){
		 if( pccd.end > heartBeatStartTime && heartBeatStartTime >= pccd.start){
			 return new long[]{pccd.start, pccd.end};
		 }
	 }
		return null;
	}

	public boolean isDuringCoolDown(PropsEntity s, long heartBeatStartTime) {
		ArticleManager am = ArticleManager.getInstance();
		Props pp = (Props) am.getArticle(s.getArticleName());
		PropsCategoryCoolDown pccd = (PropsCategoryCoolDown) cooldownTable.get(pp.getCategoryName());
		if (pccd != null) {
			if (pccd.end > heartBeatStartTime && heartBeatStartTime >= pccd.start) {
				return true;
			}
		}
		return false;
	}
}
