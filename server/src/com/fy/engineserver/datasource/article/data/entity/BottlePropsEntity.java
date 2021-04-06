package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.datasource.article.data.props.ArticleProperty;
import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

/**
 * 道具实体
 *
 */
@SimpleEntity
public class BottlePropsEntity extends PropsEntity{
	
	public static final long serialVersionUID = 235454754543052L;

	public BottlePropsEntity(){}
	
	public BottlePropsEntity(long id){
		super(id);
	}

	/**
	 * 宝瓶中的物品数组
	 */
	private ArticleProperty[] allArticles = new ArticleProperty[0];

	/**
	 * 打开宝瓶后得到的物品
	 */
	private ArticleProperty[] openedArticles = new ArticleProperty[0];
	
	/**
	 * 能开启的次数
	 */
	private int canOpenCount;

	private boolean open;

	public ArticleProperty[] getAllArticles() {
		return allArticles;
	}

	public void setAllArticles(ArticleProperty[] allArticles) {
		this.allArticles = allArticles;
		saveData("allArticles");
	}

	public ArticleProperty[] getOpenedArticles() {
		return openedArticles;
	}

	public void setOpenedArticles(ArticleProperty[] openedArticles) {
		this.openedArticles = openedArticles;
		saveData("openedArticles");
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
		saveData("open");
	}

	public int getCanOpenCount() {
		return canOpenCount;
	}

	public void setCanOpenCount(int canOpenCount) {
		this.canOpenCount = canOpenCount;
		saveData("canOpenCount");
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		int size =  super.getSize();
		size += CacheSizes.sizeOfInt();		//leftUsingTimes
		return size;
	}
	

}
