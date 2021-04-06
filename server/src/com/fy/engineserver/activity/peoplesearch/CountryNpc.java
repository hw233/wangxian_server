package com.fy.engineserver.activity.peoplesearch;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.npc.NPC;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 一个包含国家,地图名字的NPC
 * 
 */
@SimpleEmbeddable
public class CountryNpc {
	/** NPC名字 */
	private String name;
	/** 所在地图名字 */
	private String mapName;
	/** 所在国家 */
	private int country;

	public CountryNpc() {
		// TODO Auto-generated constructor stub
	}

	public CountryNpc(String name, String mapName, int country) {
		super();
		this.name = name;
		this.mapName = mapName;
		this.country = country;
	}

	public CountryNpc(NPC npc, Game game) {
		this.name = npc.getName();
		this.mapName = npc.getGameCNName();
		this.country = game.country;
	}

	public boolean isSame(NPC npc) {
		if (npc.country != country) {
			return false;
		}
		if (!npc.getGameCNName().equals(mapName)) {
			return false;
		}
		if (!npc.getName().equals(name)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CountryNpc) {
			CountryNpc npc = (CountryNpc) obj;
			if (npc.name.equals(this.name) && npc.country == this.country && npc.mapName.equals(this.mapName)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "CountryNpc [name=" + name + ", mapName=" + mapName + ", country=" + country + "]";
	}
}
