/**
 * 
 */
package com.fy.engineserver.honor;

/**
 * @author Administrator
 *
 */
public class HonorTemplete {
	
	//分类，同一个分类的称号，只能拥有一个。
	private String sort = "";
	
	int sortId;
	//称号名
	private String name="";
	
	private int color= 0xffffff ;
	
	private String buffName="";
	
	//描述
	private String desp="";
	
	private int iconId = -1;
	
	/**
	 * 是否是全服唯一的称号
	 */
	private boolean isUnique;
	
	/**
	 * 有效期，单位：天
	 */
	private int usefulLife;

	public boolean isUnique() {
		return isUnique;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	/**
	 * 
	 */
	public HonorTemplete() {
		// TODO Auto-generated constructor stub
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	public int getUsefulLife() {
		return usefulLife;
	}

	public void setUsefulLife(int usefulLife) {
		this.usefulLife = usefulLife;
	}

}
