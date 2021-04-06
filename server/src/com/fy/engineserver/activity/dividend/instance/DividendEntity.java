package com.fy.engineserver.activity.dividend.instance;

import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class DividendEntity implements Cacheable, CacheListener{

	@SimpleId
	private long id;		//playerId
	@SimpleVersion
	private int version;
	/** 玩家所购买的冲级红利信息 */
	
	@Override
	public void remove(int paramInt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
