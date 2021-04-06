package com.fy.engineserver.cityfight.citydata;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.cityfight.CityFightManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class CityData {

	/**
	 * 实体的标识，所有物品实体都有唯一的标识
	 */
	@SimpleId
	long id;

	@SimpleVersion
	protected int version2;

	/**
	 * 中立城市的归属
	 * key为城市名,value为宗派id
	 */
	private Hashtable<String, Long> holdCity_0_country = new Hashtable<String, Long>();
	/**
	 * countrytype为1的城市的归属
	 * key为城市名,value为宗派id
	 */
	private Hashtable<String, Long> holdCity_1_country = new Hashtable<String, Long>();
	/**
	 * countrytype为2的城市的归属
	 * key为城市名,value为宗派id
	 */
	private Hashtable<String, Long> holdCity_2_country = new Hashtable<String, Long>();
	/**
	 * countrytype为3的城市的归属
	 * key为城市名,value为宗派id
	 */
	private Hashtable<String, Long> holdCity_3_country = new Hashtable<String, Long>();

	/**
	 * 中立城市的攻打申请
	 * key为城市名,value为宗派id
	 */
	private Hashtable<String, Long> attackCity_0_country = new Hashtable<String, Long>();
	/**
	 * 中立城市的攻打申请
	 * key为城市名,value为宗派id的list,报名截止时取骰子点数最高的存入attackCity_0_country
	 */
	private Hashtable<String, List<Long>> attackCity_0_country_map = new Hashtable<String, List<Long>>();
	/**
	 * countrytype为1的城市的攻打申请
	 * key为城市名,value为宗派id
	 */
	private Hashtable<String, Long> attackCity_1_country = new Hashtable<String, Long>();
	/**
	 * countrytype为1的城市的攻打申请
	 * key为城市名,value为宗派id,报名截止时取骰子点数最高的存入attackCity_1_country
	 */
	private Hashtable<String, List<Long>> attackCity_1_country_map = new Hashtable<String, List<Long>>();
	/**
	 * countrytype为2的城市的攻打申请
	 * key为城市名,value为宗派id
	 */
	private Hashtable<String, Long> attackCity_2_country = new Hashtable<String, Long>();
	/**
	 * countrytype为2的城市的攻打申请
	 * key为城市名,value为宗派id,报名截止时取骰子点数最高的存入attackCity_2_country
	 */
	private Hashtable<String, List<Long>> attackCity_2_country_map = new Hashtable<String, List<Long>>();
	/**
	 * countrytype为3的城市的攻打申请
	 * key为城市名,value为宗派id
	 */
	private Hashtable<String, Long> attackCity_3_country = new Hashtable<String, Long>();
	/**
	 * countrytype为3的城市的攻打申请
	 * key为城市名,value为宗派id,报名截止时取骰子点数最高的存入attackCity_3_country
	 */
	private Hashtable<String, List<Long>> attackCity_3_country_map = new Hashtable<String, List<Long>>();

	/**
	 * 今天领取国家城市奖励的宗派id
	 */
	private ArrayList<Long> todayTakeCountryReward = new ArrayList<Long>();

	/**
	 * 今天领取中立城市奖励的宗派id
	 */
	private ArrayList<Long> todayTakeZhongLiReward = new ArrayList<Long>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion2() {
		return version2;
	}

	public void setVersion2(int version2) {
		this.version2 = version2;
	}

	public Hashtable<String, Long> getHoldCity_0_country() {
		return holdCity_0_country;
	}

	public void setHoldCity_0_country(Hashtable<String, Long> holdCity_0Country) {
		holdCity_0_country = holdCity_0Country;
		setProperty("holdCity_0_country");
	}

	public Hashtable<String, Long> getHoldCity_1_country() {
		return holdCity_1_country;
	}

	public void setHoldCity_1_country(Hashtable<String, Long> holdCity_1Country) {
		holdCity_1_country = holdCity_1Country;
		setProperty("holdCity_1_country");
	}

	public Hashtable<String, Long> getHoldCity_2_country() {
		return holdCity_2_country;
	}

	public void setHoldCity_2_country(Hashtable<String, Long> holdCity_2Country) {
		holdCity_2_country = holdCity_2Country;
		setProperty("holdCity_2_country");
	}

	public Hashtable<String, Long> getHoldCity_3_country() {
		return holdCity_3_country;
	}

	public void setHoldCity_3_country(Hashtable<String, Long> holdCity_3Country) {
		holdCity_3_country = holdCity_3Country;
		setProperty("holdCity_3_country");
	}

	public Hashtable<String, Long> getAttackCity_0_country() {
		return attackCity_0_country;
	}

	public void setAttackCity_0_country(Hashtable<String, Long> attackCity_0Country) {
		attackCity_0_country = attackCity_0Country;
		setProperty("attackCity_0_country");
	}

	public Hashtable<String, Long> getAttackCity_1_country() {
		return attackCity_1_country;
	}

	public void setAttackCity_1_country(Hashtable<String, Long> attackCity_1Country) {
		attackCity_1_country = attackCity_1Country;
		setProperty("attackCity_1_country");
	}

	public Hashtable<String, Long> getAttackCity_2_country() {
		return attackCity_2_country;
	}

	public void setAttackCity_2_country(Hashtable<String, Long> attackCity_2Country) {
		attackCity_2_country = attackCity_2Country;
		setProperty("attackCity_2_country");
	}

	public Hashtable<String, Long> getAttackCity_3_country() {
		return attackCity_3_country;
	}

	public void setAttackCity_3_country(Hashtable<String, Long> attackCity_3Country) {
		attackCity_3_country = attackCity_3Country;
		setProperty("attackCity_3_country");
	}

	public ArrayList<Long> getTodayTakeCountryReward() {
		return todayTakeCountryReward;
	}

	public void setTodayTakeCountryReward(ArrayList<Long> todayTakeCountryReward) {
		this.todayTakeCountryReward = todayTakeCountryReward;
		setProperty("todayTakeCountryReward");
	}

	public ArrayList<Long> getTodayTakeZhongLiReward() {
		return todayTakeZhongLiReward;
	}

	public void setTodayTakeZhongLiReward(ArrayList<Long> todayTakeZhongLiReward) {
		this.todayTakeZhongLiReward = todayTakeZhongLiReward;
		setProperty("todayTakeZhongLiReward");
	}

	public Hashtable<String, List<Long>> getAttackCity_0_country_map() {
		return attackCity_0_country_map;
	}

	public void setAttackCity_0_country_map(Hashtable<String, List<Long>> attackCity_0CountryMap) {
		attackCity_0_country_map = attackCity_0CountryMap;
		setProperty("attackCity_0_country_map");
	}

	public Hashtable<String, List<Long>> getAttackCity_1_country_map() {
		return attackCity_1_country_map;
	}

	public void setAttackCity_1_country_map(Hashtable<String, List<Long>> attackCity_1CountryMap) {
		attackCity_1_country_map = attackCity_1CountryMap;
		setProperty("attackCity_1_country_map");
	}

	public Hashtable<String, List<Long>> getAttackCity_2_country_map() {
		return attackCity_2_country_map;
	}

	public void setAttackCity_2_country_map(Hashtable<String, List<Long>> attackCity_2CountryMap) {
		attackCity_2_country_map = attackCity_2CountryMap;
		setProperty("attackCity_2_country_map");
	}

	public Hashtable<String, List<Long>> getAttackCity_3_country_map() {
		return attackCity_3_country_map;
	}

	public void setAttackCity_3_country_map(Hashtable<String, List<Long>> attackCity_3CountryMap) {
		attackCity_3_country_map = attackCity_3CountryMap;
		setProperty("attackCity_3_country_map");
	}

	public void setProperty(String str) {
		CityFightManager cfm = CityFightManager.getInstance();
		if (cfm != null && cfm.em != null) {
			cfm.em.notifyFieldChange(this, str);
		}
	}
}
