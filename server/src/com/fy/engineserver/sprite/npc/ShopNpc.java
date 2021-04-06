package com.fy.engineserver.sprite.npc;


/**
 * 商店NPC
 * 
 *
 */
public class ShopNpc extends NPC{

	private static final long serialVersionUID = 1L;
	
	protected String shopName;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	/**
	 * clone出一个对象
	 */
	public Object clone() {
		ShopNpc p = new ShopNpc();
		p.cloneAllInitNumericalProperty(this);
		
		p.country = country;
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		
		p.shopName = shopName;
		p.windowId = windowId;
		p.id = nextId();
		return p;
	}
}
