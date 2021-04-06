package com.fy.engineserver.datasource.article.data.entity;

import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

/**
 * 法宝舍利实体
 * 
 *
 */
@SimpleEntity
public class MagicWeaponEntity extends PropsEntity{
	
	public static final long serialVersionUID = 2354554322333052L;

	public MagicWeaponEntity(){}
	
	public MagicWeaponEntity(long id){
		super(id);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		int size =  super.getSize();
		size += CacheSizes.sizeOfInt();		//leftUsingTimes
		return size;
	}
	

}
