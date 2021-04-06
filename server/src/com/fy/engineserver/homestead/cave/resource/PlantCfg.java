package com.fy.engineserver.homestead.cave.resource;

import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;

/**
 * 植物
 * 
 * 
 */
public class PlantCfg implements FaeryConfig, Comparable<PlantCfg> {

	public static int[] color = { 0xFFFFFF, 0x13DF13, 0x278FED, 0x66079E, 0xF85121 };
	private int id;
	private String name;
	/** 粮食木材石料特殊 */
	private int type;
	/** 需要田地等级 */
	private int fieldLvNeed;
	/** 外形NPC ID */
	private int templetNpc;

	private String[] avataRaces = new String[COLORS.length];

	private String[] avataSex = new String[COLORS.length];
	/** 种植消耗 资源 */
	private ResourceCollection cost;
	/** 种植消耗 银子 */
	private long moneyCost;
	/** 产出类型 物品,经验,钱 */
	private int outputType;
	/** 产出物品名 */
	private String outputName;
	/** 各种颜色产出几率 */
	private double[] colorRate;
	/** 产出数量,最少的 */
	private Integer outputNum;
	private Integer[] outputNumArr;
	/** 产出物品颜色 */
	private Integer[] outputArticleColor;
	/** 成熟时间（秒） */
	private int grownUpTime;
	/** 更换形象时间比例 */
	private double changeOutShowRate;
	/** 每日种植次数限制 [-1]不限(每组) */
	private int dailyMaxTimes;
	/** 种植组 */
	private String plantGroup;
	/** 同时种植次数限制 [-1]不限 */
	private int atOneTimeMaxTimes;
	/** 基础兑换量(白色) */
	private int baseExchangeNum;

	private List<DropsArticle> excessArticle;

	private String avataA = "";
	private String avataB = "";

	private String des;

	/** 每个田地等级白色种植物产量 */
	private double[] everyFieldLevelOutput;

	public PlantCfg() {

	}

	public void init() {
		CaveNPC caveNPC = (CaveNPC) ((MemoryNPCManager) MemoryNPCManager.getNPCManager()).createNPC(templetNpc);
		if (caveNPC != null) {
			setAvataA(caveNPC.getAvatas()[1]);
			setAvataB(caveNPC.getAvatas()[caveNPC.getAvatas().length - 1]);
		} else {
			FaeryManager.logger.error("[加载 config] [NPC不存在:{}]", new Object[] { toString() });
		}

	}


	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public long changeOutshowTime() {
		return (long) (getChangeOutShowRate() * getGrownUpTime());
	}

	/**
	 * 获得一次摘取随机获得的物品
	 * @return
	 */
	public DropsArticle getDropsArticle() {
		return null;
	}

	/**
	 * 产出是否是兑换资源类果实
	 * @return
	 */
	public boolean outputIsConvertFruit() {
		return getType() != FRUIT_RES_SPECIAL;
	}

	/************************************************ getters and setters ************************************************/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFieldLvNeed() {
		return fieldLvNeed;
	}

	public void setFieldLvNeed(int fieldLvNeed) {
		this.fieldLvNeed = fieldLvNeed;
	}

	public String[] getAvataRaces() {
		return avataRaces;
	}

	public void setAvataRaces(String[] avataRaces) {
		this.avataRaces = avataRaces;
	}

	public String[] getAvataSex() {
		return avataSex;
	}

	public void setAvataSex(String[] avataSex) {
		this.avataSex = avataSex;
	}

	public ResourceCollection getCost() {
		return cost;
	}

	public void setCost(ResourceCollection cost) {
		this.cost = cost;
	}

	public int getOutputType() {
		return outputType;
	}

	public void setOutputType(int outputType) {
		this.outputType = outputType;
	}

	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	public double[] getColorRate() {
		return colorRate;
	}

	public void setColorRate(double[] colorRate) {
		this.colorRate = colorRate;
	}

	public Integer getOutputNum() {
		return outputNum;
	}

	public void setOutputNum(Integer outputNum) {
		this.outputNum = outputNum;
	}

	public Integer[] getOutputNumArr() {
		return outputNumArr;
	}

	public void setOutputNumArr(Integer[] outputNumArr) {
		this.outputNumArr = outputNumArr;
	}

	public int getGrownUpTime() {
		return grownUpTime;
	}

	public void setGrownUpTime(int grownUpTime) {
		this.grownUpTime = grownUpTime;
	}

	public double getChangeOutShowRate() {
		return changeOutShowRate;
	}

	public void setChangeOutShowRate(double changeOutShowRate) {
		this.changeOutShowRate = changeOutShowRate;
	}

	public int getDailyMaxTimes() {
		return dailyMaxTimes;
	}

	public void setDailyMaxTimes(int dailyMaxTimes) {
		this.dailyMaxTimes = dailyMaxTimes;
	}

	public int getAtOneTimeMaxTimes() {
		return atOneTimeMaxTimes;
	}

	public void setAtOneTimeMaxTimes(int atOneTimeMaxTimes) {
		this.atOneTimeMaxTimes = atOneTimeMaxTimes;
	}

	public final List<DropsArticle> getExcessArticle() {
		return excessArticle;
	}

	public final void setExcessArticle(List<DropsArticle> excessArticle) {
		this.excessArticle = excessArticle;
	}

	public int getTempletNpc() {
		return templetNpc;
	}

	public void setTempletNpc(int templetNpc) {
		this.templetNpc = templetNpc;
	}

	public String getAvataA() {
		return avataA;
	}

	public void setAvataA(String avataA) {
		this.avataA = avataA;
	}

	public String getAvataB() {
		return avataB;
	}

	public void setAvataB(String avataB) {
		this.avataB = avataB;
	}

	@Override
	public int compareTo(PlantCfg o) {
		return getFieldLvNeed() - o.getFieldLvNeed();
	}

	public int getBaseExchangeNum() {
		return baseExchangeNum;
	}

	public void setBaseExchangeNum(int baseExchangeNum) {
		this.baseExchangeNum = baseExchangeNum;
	}

	public double[] getEveryFieldLevelOutput() {
		return everyFieldLevelOutput;
	}

	public void setEveryFieldLevelOutput(double[] everyFieldLevelOutput) {
		this.everyFieldLevelOutput = everyFieldLevelOutput;
	}

	public String getPlantGroup() {
		return plantGroup;
	}

	public void setPlantGroup(String plantGroup) {
		this.plantGroup = plantGroup;
	}

	public Integer[] getOutputArticleColor() {
		return outputArticleColor;
	}

	public void setOutputArticleColor(Integer[] outputArticleColor) {
		this.outputArticleColor = outputArticleColor;
	}

	public int getCVipAddTimes(byte vipLevel) {
		if (plantGroup.trim().equals(Translate.摇钱树)) {

			if (vipLevel <= 2) {
				return 0;
			} else if (vipLevel >= 3 && vipLevel < 5) {
				return 1;
			} else {
				return 2;
			}
		} else if (plantGroup.trim().equals(Translate.经验树)) {
			if (vipLevel <= 1) {
				return 0;
			} else if (vipLevel >= 2 && vipLevel < 6) {
				return 1;
			} else {
				return 2;
			}

		}
		return 0;
	}

	public long getMoneyCost() {
		return moneyCost;
	}

	public void setMoneyCost(long moneyCost) {
		this.moneyCost = moneyCost;
	}

	@Override
	public String toString() {
		return "PlantCfg [id=" + id + ", name=" + name + ", type=" + type + ", fieldLvNeed=" + fieldLvNeed + ", templetNpc=" + templetNpc + ", avataRaces=" + Arrays.toString(avataRaces) + ", avataSex=" + Arrays.toString(avataSex) + ", cost=" + cost + ", moneyCost=" + moneyCost + ", outputType=" + outputType + ", outputName=" + outputName + ", colorRate=" + Arrays.toString(colorRate) + ", outputNum=" + outputNum + ", outputNumArr=" + Arrays.toString(outputNumArr) + ", outputArticleColor=" + Arrays.toString(outputArticleColor) + ", grownUpTime=" + grownUpTime + ", changeOutShowRate=" + changeOutShowRate + ", dailyMaxTimes=" + dailyMaxTimes + ", plantGroup=" + plantGroup + ", atOneTimeMaxTimes=" + atOneTimeMaxTimes + ", baseExchangeNum=" + baseExchangeNum + ", excessArticle=" + excessArticle + ", avataA=" + avataA + ", avataB=" + avataB + ", des=" + des + ", everyFieldLevelOutput=" + Arrays.toString(everyFieldLevelOutput) + "]";
	}

}
