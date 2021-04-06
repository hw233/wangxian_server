package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.STAR_GO_RES;
import com.fy.engineserver.sprite.Player;

public class Option_EquipmentRemoveStar extends Option{

	long removeId;
	long addId;
	
	public long getRemoveId() {
		return removeId;
	}

	public void setRemoveId(long removeId) {
		this.removeId = removeId;
	}

	public long getAddId() {
		return addId;
	}

	public void setAddId(long addId) {
		this.addId = addId;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player p){
		ArticleEntity removeAe = ArticleEntityManager.getInstance().getEntity(removeId);
		ArticleEntity addAe = ArticleEntityManager.getInstance().getEntity(addId);
		if(removeAe instanceof EquipmentEntity && addAe instanceof EquipmentEntity){
			Article a = ArticleManager.getInstance().getArticle(removeAe.getArticleName());
			if(a == null || !(a instanceof Equipment)){
				ArticleManager.logger.warn("[装备摘星2] [确认失败：物品不存在] ["+removeAe.getArticleName()+"] ["+p.getLogString()+"]");
				return;
			}
			Article a2 = ArticleManager.getInstance().getArticle(addAe.getArticleName());
			if(a2 == null || !(a2 instanceof Equipment)){
				ArticleManager.logger.warn("[装备摘星2] [确认失败：物品不存在2] ["+addAe.getArticleName()+"] ["+p.getLogString()+"]");
				return;
			}
			EquipmentEntity eq = (EquipmentEntity)removeAe;
			EquipmentEntity eq2 = (EquipmentEntity)addAe;
			if(!addAe.isBinded()){
				p.sendError(Translate.要继承星的装备必须是绑定的);
				return;
			}
			
			int star = eq.getStar();
			int star2 = eq2.getStar();
			if(star <= 0){
				p.sendError(Translate.该装备还没有炼星);
				ArticleManager.logger.warn("[装备摘星2] [确认失败：装备没星] [star:"+star+"] ["+removeAe.getArticleName()+"] ["+p.getLogString()+"]");
				return;
			}
			if(star2 >= 20){
				p.sendError(Translate.被转移的星已达最大);
				return;
			}
			if(star2 >= star){
				p.sendError(Translate.被转移的星小于转移的);
				return;
			}
			long cost = ArticleManager.getInstance().costsilver[star - 1];
			if(p.getSilver() < cost){
				p.sendError(Translate.余额不足);
				return;
			}
			
			synchronized (p) {
				try {
					BillingCenter.getInstance().playerExpense(p, cost, CurrencyType.YINZI, ExpenseReasonType.装备摘星, "装备摘星2");
				} catch (Exception ex) {
					ex.printStackTrace();
					ArticleManager.logger.warn("装备摘星2] [扣费失败] [cost:"+cost+"] [" + p.getUsername() + "] [" + p.getId() + "] [" + p.getName() + "] [要转装备:" + (eq != null ? eq.getArticleName() : Translate.空白) + "] [" + (eq != null ? eq.getId() : Translate.空白) + "]", ex);
					return;
				}
				eq.setStar(0);
				eq2.setStar(star);
			}
			p.sendError(Translate.装备摘星成功);
			p.addMessageToRightBag(new STAR_GO_RES(GameMessageFactory.nextSequnceNum(),true));
			ArticleManager.logger.warn("装备摘星] [成功] [cost:"+cost+"] [" + p.getUsername() + "] [" + p.getId() + "] [" + p.getName() + "] [要转装备:" + (eq != null ? eq.getArticleName() : Translate.空白) + "] [[star:"+star+"]] [" + (eq != null ? eq.getId() : Translate.空白) + "] [star2:"+star2+"] ");
		}else{
			p.sendError(Translate.只能装备摘星);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_绑定;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
