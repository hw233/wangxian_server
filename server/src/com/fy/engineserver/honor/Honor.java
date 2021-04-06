package com.fy.engineserver.honor;

import com.fy.engineserver.sprite.Player;

public class Honor {

	// 唯一标识
	private int id = -1;

	// 分类，同一个分类的称号，只能拥有一个。
	private String sort = "";
	// 称号名
	private String name = "";

	// 过期时间
	private long expirationTime;

	private int color = 0xffffffff;

	private int bufid = -1;

	// 描述
	private String desp = "";

	// 记录获得称号的时间
	private long createTime;

	/**
	 * icon ID
	 */
	private String iconId = "";

	private String buffName = "";

	int sortId;

	boolean isUnique;

	public void doEquip(Player p) {}
	
	public void doUnEquip(Player p) {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getBufid() {
		return bufid;
	}

	public void setBufid(int bufid) {
		this.bufid = bufid;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getIconId() {
		return iconId;
	}

	public void setIconId(String iconId) {
		this.iconId = iconId;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
		if (this.buffName == null) {
			this.buffName = "";
		}
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	public boolean isUnique() {
		return isUnique;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

}
