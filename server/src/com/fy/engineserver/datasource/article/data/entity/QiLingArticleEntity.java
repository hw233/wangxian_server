package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.QiLingArticle;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

/**
 * 比武竞猜物品实体
 *
 */
@SimpleEntity
public class QiLingArticleEntity extends ArticleEntity{
	
	public static final long serialVersionUID = 2354547453543050L;
	
	public QiLingArticleEntity(long id){
		this.id = id;
	}
	public QiLingArticleEntity(){
		
	}

	/**
	 * 属性�
	 */
	public int propertyValue;

	/**
	 * 吞噬�
	 */
	public int tunshiValue;
	
	public int getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(int propertyValue) {
		this.propertyValue = propertyValue;
		saveData("propertyValue");
	}
	
	public int getTunshiValue() {
		return tunshiValue;
	}
	public void setTunshiValue(int tunshiValue) {
		this.tunshiValue = tunshiValue;
		saveData("tunshiValue");
	}
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		int size =  super.getSize();
		size += CacheSizes.sizeOfInt();		//leftUsingTimes
		return size;
	}
	
	/**
	 * 玩家获得物品详细介绍信息
	 * @return
	 */
	public String getInfoShow(Player player) {
		ArticleManager am = ArticleManager.getInstance();
		if(am == null){
			return Translate.物品 + ":"+articleName;
		}
		Article a = am.getArticle(articleName);
		QiLingArticle qiLing = (QiLingArticle)a;
		StringBuffer sb = new StringBuffer();
		
		if (this.isBinded()) {
			sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.已绑定).append("</f>");
		}		
		if(a.getUseMethod() != null && !a.getUseMethod().trim().equals("")){
			sb.append("\n<f color='0xFFFFFF' size='28'>").append(a.getUseMethod()).append("</f>");
		}
		if(a.getGetMethod() != null && !a.getGetMethod().trim().equals("")){
			sb.append("\n<f color='0xFFFFFF' size='28'>").append(a.getGetMethod()).append("</f>");
		}
		
		//属� 
		sb.append("\n<f color='0xFFFF00' size='28'>").append(QiLingArticle.propertysValuesStrings[qiLing.getQilingType()]).append(" +").append(getPropertyValue()).append("</f>");
		//上限
		sb.append("\n<f color='0xFFFFFF' size='28'>").append(QiLingArticle.propertysValuesStrings[qiLing.getQilingType()]).append(Translate.上限).append(":").append(qiLing.getMaxProperty(getColorType())).append("</f>");
		//魂量
		if (getColorType() <= 3) {
			sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.魂量).append(getTunshiValue()).append("/").append((int)(ArticleManager.魂值最大值[getColorType()] * qiLing.getQiLingHunLiangTimesFloat())).append("</f>");
		}else {
			sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.魂量).append("N").append("/").append("N").append("</f>");
		}
		
		return sb.toString();
	}

}
