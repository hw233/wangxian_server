package com.fy.engineserver.datasource.article.data.horseInlay.instance;

import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 坐骑装备孔
 */
@SimpleEntity
public class HorseEquInlay implements Cacheable, CacheListener{
	
	@SimpleId
	private long id;		//坐骑装备id
	@SimpleVersion
	private int version;
	/** 开孔情况  -1代表没开 */
	private int[] inlayColorType = new int[HorseEquInlayManager.MAX_INLAY_NUM];
	/**  神匣镶嵌情况*/
	private long[] inlayArticleIds = new long[HorseEquInlayManager.MAX_INLAY_NUM];

	@Override
	public void remove(int paramInt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long[] getInlayArticleIds() {
		return inlayArticleIds;
	}

	public void setInlayArticleIds(long[] inlayArticleIds) {
		this.inlayArticleIds = inlayArticleIds;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int[] getInlayColorType() {
		return inlayColorType;
	}

	public void setInlayColorType(int[] inlayColorType) {
		this.inlayColorType = inlayColorType;
	}

}
