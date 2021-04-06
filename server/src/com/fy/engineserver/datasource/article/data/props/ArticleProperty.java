package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.activity.shop.ActivityProp;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;


/**
 * 物品名以及值
 * 
 *
 */
@SimpleEmbeddable
public class ArticleProperty {

	/**
	 * 物品名称
	 */
	String articleName;

	String articleName_stat;
	/**
	 * 值为掉落几率值时，基数为10000
	 */
	int prob;
	
	/**
	 * 物品颜色数值
	 */
	int color;

	/**
	 * 宝箱中物品的个数
	 */
	int count = 1;
	
	/**
	 * 是否绑定
	 */
	boolean binded;
	
	/**
	 * 是否广播
	 */
	boolean isNotice;
	
	public ArticleProperty() {
		// TODO Auto-generated constructor stub
	}

	public String getArticleName_stat() {
		return articleName_stat;
	}

	public void setArticleName_stat(String articleName_stat) {
		this.articleName_stat = articleName_stat;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getProb() {
		return prob;
	}

	public void setProb(int prob) {
		this.prob = prob;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isBinded() {
		return binded;
	}

	public void setBinded(boolean binded) {
		this.binded = binded;
	}

	public boolean isNotice() {
		return isNotice;
	}

	public void setNotice(boolean isNotice) {
		this.isNotice = isNotice;
	}

	public ActivityProp toActivityProp() {
		ActivityProp ap = new ActivityProp(this.articleName_stat, this.color, this.count, this.binded);
		return ap;
	}

	@Override
	public String toString() {
		return "ArticleProperty [articleName=" + articleName + ", articleName_stat=" + articleName_stat + ", prob=" + prob + ", color=" + color + ", count=" + count + ", binded=" + binded + ", isNotice=" + isNotice + "]";
	}
}
