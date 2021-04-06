package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeArticle;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntArticleExtraData;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model.ShouHunModel;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
/**
 * 猎命道具
 * @author Administrator
 *
 */
@SimpleEntity
public class HuntLifeArticleEntity extends ArticleEntity{
	private static final long serialVersionUID = 1L;
	/**
	 * 猎命道具额外增加  省去物品表增加字段 (需要再初始化article时加载额外信息)
	 */
	private transient HuntArticleExtraData extraData = null;
	
	public HuntLifeArticleEntity(long id){
		setId(id);
	}
	public HuntLifeArticleEntity(){}
	
	public int getLevel() {
		if (extraData == null) {
			return 1;
		} else {
			return extraData.getLevel();
		}
	}

	@Override
	public String getInfoShow(Player p) {			//需要根据额外信息修改泡泡
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		HuntLifeArticle a = (HuntLifeArticle) ArticleManager.getInstance().getArticle(this.getArticleName());
		int lv = 1;
		long exps = 0;
		int addAttrNum = 0;
		String addAttrDes = "";
		int addAttrNum2 = 0;
		String maxExp = "";
		if (extraData != null) {
			lv = extraData.getLevel();
			exps = extraData.getExps();
			if (lv < HuntLifeManager.兽魂满级) {
				ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(lv+1);
				addAttrNum2 = model.getAddAttrNums()[a.getAddAttrType()];
			}
		}
		ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(lv);
		maxExp = model.getExp4Up() + "";
		if (lv == HuntLifeManager.兽魂满级) {
			maxExp = model.getExp4Up() + "";
			exps = model.getExp4Up();
		}
		addAttrDes = HuntLifeManager.对应增加属性描述[a.getAddAttrType()];
		addAttrNum = model.getAddAttrNums()[a.getAddAttrType()];
		sb.append("<f size='28'>").append(Translate.等级).append(":").append(lv).append("</f>");
		sb.append("\n").append("<f color='0x0FD100' size='28'>").append("    ").append(addAttrDes).append(":").append(addAttrNum).append("</f>");
		if (addAttrNum2 > 0) {
			sb.append("\n").append("<f color='0xFF0000' size='28'>").append(Translate.下一级).append("</f>");
			sb.append("\n").append("<f color='0x0FD100' size='28'>").append("    ").append(addAttrDes).append(":").append(addAttrNum2).append("</f>");
		}
		sb.append("\n").append("<f size='28'>").append(Translate.经验).append(":").append(exps).append("/").append(maxExp).append("</f>");
		sb.append("\n").append("<f color='0xffff00' size='28'>").append(Translate.兽魂泡泡).append("</f>");
		return sb.toString();
	}
	@Override
	public int getColorType(){
		int lv = 1;
		int colorType = 0;
		if (extraData != null) {
			lv = extraData.getLevel();
		}
		if (lv < HuntLifeManager.等级对应物品颜色.length) {
			colorType = HuntLifeManager.等级对应物品颜色[lv-1];
		}
		return colorType;
	}
	
	
	public HuntArticleExtraData getExtraData() {
		return extraData;
	}
	public void setExtraData(HuntArticleExtraData extraData) {
		this.extraData = extraData;
	}
}
