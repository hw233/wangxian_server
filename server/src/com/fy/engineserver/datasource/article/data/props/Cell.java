package com.fy.engineserver.datasource.article.data.props;

import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 格子，存在于各种容器内的格子，例如背包、仓库等等
 * 
 */
@SimpleEmbeddable
public class Cell{
	public long entityId = -1;
	
	public int count = 0;
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public long getEntityId() {
		return entityId;
	}

	public Cell() {
		
	}
	@Override
	protected void finalize() throws Throwable {
		
	}
	
	public void setEntityId(long entityId) {
		if(entityId == 0) {
//			Knapsack.logger.warn("[设置entityId] [值为0] \n" + com.xuanzhi.tools.text.StringUtil.getStackTrace(Thread.currentThread().getStackTrace()));
			if(Knapsack.logger.isWarnEnabled())
				Knapsack.logger.warn("[设置entityId] [值为0] \n{}", new Object[]{com.xuanzhi.tools.text.StringUtil.getStackTrace(Thread.currentThread().getStackTrace())});
		}
		this.entityId = entityId;
	}

	public boolean isEmpty () {
		return getEntityId() <= 0 || getCount() <= 0;
	}
	
	public int getSize() {
		int size = 0;
		size += CacheSizes.sizeOfObject();              // overhead of object
        size += CacheSizes.sizeOfLong();     //size of entityId
        size += CacheSizes.sizeOfInt();     //size of count
        return size;
	}
}
