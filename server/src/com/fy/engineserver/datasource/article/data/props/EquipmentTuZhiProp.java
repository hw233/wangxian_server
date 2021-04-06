package com.fy.engineserver.datasource.article.data.props;


import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.articleEnchant.model.EnchantArticle;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.UpgradeRule;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.message.GOD_EQUIPMENT_UPGRADE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;


public class EquipmentTuZhiProp extends Props{

	//规则名
	private String ruleName;

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if(game==null || player==null || ae==null){
			ArticleManager.logger.warn("[EquipmentTuZhiProp] [use] [fail] ["+(game==null?"null":"")+" "+(player==null?"null":"")+" "+(ae==null?"null":"")+"]");
			return false;
		}
		
		if(ruleName==null){
			ArticleManager.logger.warn("[EquipmentTuZhiProp] [use] [fail] [ruleName==null] [propname:"+ae.getArticleName()+"] ["+player.getLogString()+"]");
			return false;
		}
		
		List<UpgradeRule> rules = ArticleManager.rules;
		UpgradeRule rule = null;
		for(UpgradeRule r:rules){
			if(r.getRuleName().equals(ruleName)){
				rule = r;
				break;
			}
		}
		if(rule==null){
			ArticleManager.logger.warn("[EquipmentTuZhiProp] [use] [fail] [rule==null] [ruleName:"+ruleName+"] [propname:"+ae.getArticleName()+"] ["+player.getLogString()+"]");
			return false;
		}
		
		Article a = ArticleManager.getInstance().getArticle(rule.getNewEquipmentName());
		if(a==null){
			ArticleManager.logger.warn("[EquipmentTuZhiProp] [use] [fail] [a==null] [newname:"+(rule.getNewEquipmentName()==null?"null":rule.getNewEquipmentName())+"] [ruleName:"+ruleName+"] [propname:"+ae.getArticleName()+"] ["+player.getLogString()+"]");
			return false;
		}
		
		//被合成的装备是铭刻，新装备都是绑定
		ArticleEntity tempAe = null;
		try {
			tempAe = DefaultArticleEntityManager.getInstance().getTempEntity(rule.getNewEquipmentName(), true ,a.getColorType());
			if(tempAe==null){
				tempAe = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.图纸合成临时装备, player, rule.getNewEquipmentColor());
			}
			if(tempAe==null){
				ArticleManager.logger.warn("[EquipmentTuZhiProp] [use] [fail] [tempAe=null] [newname:"+(rule.getNewEquipmentName()==null?"null":rule.getNewEquipmentName())+"] [ruleName:"+ruleName+"] [propname:"+ae.getArticleName()+"] ["+player.getLogString()+"]");
				return false;
			}
			
			String icons [] = new String[rule.getMaterialNames().length];
			String messes[] = new String[rule.getMaterialNames().length];
			for(int i=0;i<icons.length;i++){
				Article aa = ArticleManager.getInstance().getArticle(rule.getMaterialNames()[i]);
				if(aa!=null){
					icons[i] = aa.getIconId();
					messes[i] = aa.getInfoShow();
				}
			}
			
			if(a instanceof EnchantArticle){
				tempAe.setBinded(false);
			}
			
			GOD_EQUIPMENT_UPGRADE_RES res = new GOD_EQUIPMENT_UPGRADE_RES(GameMessageFactory.nextSequnceNum(), ae.getId(), tempAe.getId(), rule.getMaterialNames(),icons,messes, rule.getMaterialNum(), (byte)rule.getTypename(), rule.getShowMess(), rule.getCostSilver());
			player.addMessageToRightBag(res);
			
			ArticleManager.logger.warn("[EquipmentTuZhiProp] [use] [succ] [物品名:"+ae.getArticleName()+"] [a:"+(a.getClass().getSimpleName())+"] [ruleName:"+ruleName+"] [nums:"+Arrays.toString(rule.getMaterialNum())+"] ["+player.getLogString()+"]");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;		
	}

	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	@Override
	public byte getArticleType() {
		return ARTICLE_TYPE_TUZHI;
	}
	@Override
	public String toString() {
		return "EquipmentTuZhiProp [ruleName=" + ruleName + ", name_stat=" + name_stat + ", iconId=" + iconId + ", price=" + price + "]";
	}
	
}
