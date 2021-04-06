package com.fy.engineserver.activity.village.data;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class VillageData {

	@SimpleId
	public long id = 1;
	
	@SimpleVersion
	protected int version2;

	/**
	 * 一个国家的矿藏贫富分布，0为小矿，1为中矿，2为大矿
	 * 对应一个国家的所有矿，一个国家有几个矿这个数组就多长，数值代表矿的贫富
	 * 这样在每周的贫富更替时只需要变化这个值，就可以实现矿的贫富变换
	 */
	public byte[] oreType;

	/**
	 * 每个国家各个矿NPC的数据
	 * key为国家id，value为这个国家的所有的矿的数组，数组下标和oreType对应，代表着矿的贫富
	 */
	public Hashtable<Byte,OreNPCData[]> countryOres = new Hashtable<Byte,OreNPCData[]>();
//	
//	/**
//	 * 每个国家各个矿都有哪些家族占有
//	 * key为国家id，value为这个国家占有矿的家族，数组下标和oreType对应，代表着矿的贫富
//	 */
//	@Basic(fetch=FetchType.EAGER)
//	Hashtable<Byte,long[]> countryHoldOres = new Hashtable<Byte,long[]>();
	
	/**
	 * 每个国家各个矿最终都有哪些家族攻打
	 */
	public Hashtable<Byte,long[]> countryPrepareToFightOres = new Hashtable<Byte,long[]>();
	/**
	 * 每个国家各个矿都有哪些家族申请攻打<国家,Map<oreType,List<家族Id>>>
	 */
	public Hashtable<Byte,Map<Integer,List<Long>>> prepareToFightJiazus = new Hashtable<Byte, Map<Integer,List<Long>>>();

	/**
	 * 每天领取银子的家族id，key为家族id，value为领取的钱
	 */
	public Hashtable<Long, Integer> everyDayGetSilverJiazuIdMap = new Hashtable<Long, Integer>();

	/**
	 * 每天领取家族资金的家族id，key为家族id，value为领取的家族资金
	 */
	@SimpleColumn(name="perDayZiJin")
	public Hashtable<Long, Integer> everyDayGetJiazuZiJinJiazuIdMap = new Hashtable<Long, Integer>();

	/**
	 * 每天领取绑银的玩家id，key为玩家id，value为领取的绑银
	 */
	@SimpleColumn(name="perDayBindSilver",length=100000)
	public Hashtable<Long, Integer> everyDayGetBindSilverPlayerIdMap = new Hashtable<Long, Integer>();

	/**
	 * 每天领取buff的玩家id，key为玩家id，value为领取的buff名字
	 */
	@SimpleColumn(name="perDayGetBuff",length=100000)
	public Hashtable<Long, String> everyDayGetBuffPlayerIdMap = new Hashtable<Long, String>();
	
	public boolean dirty;

	public byte[] getOreType() {
		return oreType;
	}

	public void setOreType(byte[] oreType) {
		this.oreType = oreType;
	}

	public Hashtable<Byte, OreNPCData[]> getCountryOres() {
		return countryOres;
	}

	public void setCountryOres(Hashtable<Byte, OreNPCData[]> countryOres) {
		this.countryOres = countryOres;
	}

	public Hashtable<Byte, long[]> getCountryPrepareToFightOres() {
		return countryPrepareToFightOres;
	}

	public void setCountryPrepareToFightOres(
			Hashtable<Byte, long[]> countryPrepareToFightOres) {
		this.countryPrepareToFightOres = countryPrepareToFightOres;
	}

	public Hashtable<Long, Integer> getEveryDayGetSilverJiazuIdMap() {
		return everyDayGetSilverJiazuIdMap;
	}

	public void setEveryDayGetSilverJiazuIdMap(
			Hashtable<Long, Integer> everyDayGetSilverJiazuIdMap) {
		this.everyDayGetSilverJiazuIdMap = everyDayGetSilverJiazuIdMap;
	}

	public Hashtable<Long, Integer> getEveryDayGetJiazuZiJinJiazuIdMap() {
		return everyDayGetJiazuZiJinJiazuIdMap;
	}

	public void setEveryDayGetJiazuZiJinJiazuIdMap(
			Hashtable<Long, Integer> everyDayGetJiazuZiJinJiazuIdMap) {
		this.everyDayGetJiazuZiJinJiazuIdMap = everyDayGetJiazuZiJinJiazuIdMap;
	}

	public Hashtable<Long, Integer> getEveryDayGetBindSilverPlayerIdMap() {
		return everyDayGetBindSilverPlayerIdMap;
	}

	public void setEveryDayGetBindSilverPlayerIdMap(
			Hashtable<Long, Integer> everyDayGetBindSilverPlayerIdMap) {
		this.everyDayGetBindSilverPlayerIdMap = everyDayGetBindSilverPlayerIdMap;
	}

	public Hashtable<Long, String> getEveryDayGetBuffPlayerIdMap() {
		return everyDayGetBuffPlayerIdMap;
	}

	public void setEveryDayGetBuffPlayerIdMap(
			Hashtable<Long, String> everyDayGetBuffPlayerIdMap) {
		this.everyDayGetBuffPlayerIdMap = everyDayGetBuffPlayerIdMap;
	}

	public Hashtable<Byte, Map<Integer, List<Long>>> getPrepareToFightJiazus() {
		return prepareToFightJiazus;
	}

	public void setPrepareToFightJiazus(Hashtable<Byte, Map<Integer, List<Long>>> prepareToFightJiazus) {
		this.prepareToFightJiazus = prepareToFightJiazus;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}
