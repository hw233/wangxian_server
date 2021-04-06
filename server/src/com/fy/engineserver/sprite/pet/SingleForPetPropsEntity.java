package com.fy.engineserver.sprite.pet;

import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

@SimpleEntity
public class SingleForPetPropsEntity extends PropsEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7550110122464015830L;

	
	public SingleForPetPropsEntity(){
		
	}
	
	public SingleForPetPropsEntity(long id){
		super(id);
	}
	
	
	/**
	 * 此简单道具要修改的属性，以及增加的值
	 * 修改都是加法运算
	 * 按照规定的顺序给玩家宠物修改属性
	 * 数组顺序:hp,exp,level
	 */
	@SimpleColumn(mysqlName="values2")
	protected long []values = new long[3];

	public long[] getValues() {
		return values;
	}

	public void setValues(long[] values) {
		this.values = values;
	}
	
	public void setExp(long exp){
		values[1] = exp;
		this.setDirty(true);
		PetManager.logger.warn("[产生经验丹] [id:"+this.getId()+"] [经验:"+exp+"]");
	}
	
	
}
