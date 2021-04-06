package com.fy.engineserver.articleEnchant;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class EnchantAttr {
	/** 附魔id， 读表  增加对应属性 */
	private int enchantId;
	/** 灵性（耐久度，为0消失） */
	private int durable;
	/** 增加属性值（属性值配表为区间，附魔成功后需要记录具体值） */
	private int attrNum;
	/**
	 * 
	 * @param lostNum
	 * @return  true还有耐久可继续使用    false无耐久，损坏
 	 */
	public boolean lostDuable(int lostNum) {
		if (durable > lostNum) {
			durable -= lostNum;
			return true;
		} else {
			durable = 0;
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "EnchantAttr [enchantId=" + enchantId + ", durable=" + durable + ", attrNum=" + attrNum + "]";
	}

	public EnchantAttr(){}


	public EnchantAttr(int eId, int durable, int attNum) {
		this.enchantId = eId;
		this.durable = durable;
		this.attrNum = attNum;
	}
	
	public int getEnchantId() {
		return enchantId;
	}
	public void setEnchantId(int enchantId) {
		this.enchantId = enchantId;
	}
	public int getDurable() {
		return durable;
	}
	public void setDurable(int durable) {
		this.durable = durable;
	}
	public int getAttrNum() {
		return attrNum;
	}
	public void setAttrNum(int attrNum) {
		this.attrNum = attrNum;
	}

	
}
