package com.fy.engineserver.activity.disaster.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.util.NeedBuildExtraData;

/**
 * 金猴天灾基础配置数据
 */
public class DisasterBaseModule implements NeedBuildExtraData{
	private String[] tempOpenRules;
	/** 活动开启时间规则 */
	private List<DisasterOpenModule> timeRules = new ArrayList<DisasterOpenModule>();
	/** 进入等级限制 */
	private int levelLimit;
	/** 游戏地图名 */
	private String mapName;
	private String[] tempBornPoints;
	/** 出生点坐标 */
	private List<Integer[]> bornPoints = new ArrayList<Integer[]>();
	/** 金猴NPCid */
	private int[] monkeyNPCId;
	private String[] tempMonkeyPoints;
	/** 对应金猴出生点坐标 */
	private List<Integer[]> monkeyPoints = new ArrayList<Integer[]>();
	private String[] tempFireNpcPoints;
	/** 内圈火圈NPC坐标点集合 */
	private List<Integer[]> fireNPCPoints = new ArrayList<Integer[]>();
	private String[] fireOuterPoints;
	/** 外圈火圈NPC坐标点集合 */
	private List<Integer[]> fireNPCOuterPoints = new ArrayList<Integer[]>();
	/** 第一阶段火圈NPCId */
	private int[] firstStepNPCId;
	/** 第二阶段火圈NPCId */
	private int[] secondStepNPCId;
	/** 第三阶段火圈NPCId */
	private int[] thirdStepNPCId;
	/** 经验桃坐标 */
	private String[] tempBoxPoints;
	private List<Integer[]> boxPoints = new ArrayList<Integer[]>();
	/** 外圈怪物坐标 */
	private String[] tempMonsterPoints;
	private List<Integer[]> monsterPoints = new ArrayList<Integer[]>();
	/** 外圈怪物坐标2 */
	private String[] tempMonsterPoints2;
	private List<Integer[]> monsterPoints2 = new ArrayList<Integer[]>();
	
	
	private String[] tempPoints;
	private List<Integer[]> secondMonsterPoints = new ArrayList<Integer[]>();
	/** 二三阶段npcid */
	private int[] tempNpcIds;
	
	@Override
	public void buildExtraData() throws Exception {
		// TODO Auto-generated method stub
		for (int i=0; i<tempOpenRules.length; i++) {
			timeRules.add(new DisasterOpenModule(tempOpenRules[i]));
		}
		for (int i=0; i<tempBornPoints.length; i++) {
			String[] str = tempBornPoints[i].split(",");
			bornPoints.add(new Integer[]{Integer.parseInt(str[0]), Integer.parseInt(str[1])});
		}
		for (int i=0; i<tempMonkeyPoints.length; i++) {
			String[] str = tempMonkeyPoints[i].split(",");
			monkeyPoints.add(new Integer[]{Integer.parseInt(str[0]), Integer.parseInt(str[1])});
		}
		for (int i=0; i<tempFireNpcPoints.length; i++) {
			String[] str = tempFireNpcPoints[i].split(",");
			fireNPCPoints.add(new Integer[]{Integer.parseInt(str[0]), Integer.parseInt(str[1])});
		}
		for (int i=0; i<tempBoxPoints.length; i++) {
			String[] str = tempBoxPoints[i].split(",");
			boxPoints.add(new Integer[]{Integer.parseInt(str[0]), Integer.parseInt(str[1])});
		}
		for (int i=0; i<fireOuterPoints.length; i++) {
			String[] str = fireOuterPoints[i].split(",");
			fireNPCOuterPoints.add(new Integer[]{Integer.parseInt(str[0]), Integer.parseInt(str[1])});
		}
		for (int i=0; i<tempMonsterPoints.length; i++) {
			String[] str = tempMonsterPoints[i].split(",");
			monsterPoints.add(new Integer[]{Integer.parseInt(str[0]), Integer.parseInt(str[1])});
		}
		for (int i=0; i<tempMonsterPoints2.length; i++) {
			String[] str = tempMonsterPoints2[i].split(",");
			monsterPoints2.add(new Integer[]{Integer.parseInt(str[0]), Integer.parseInt(str[1])});
		}
		for (int i=0; i<tempPoints.length; i++) {
			String[] str = tempPoints[i].split(",");
			secondMonsterPoints.add(new Integer[]{Integer.parseInt(str[0]), Integer.parseInt(str[1])});
		}
	}
	
	public Integer[] getPlayerBornPoint(Random ran) {
		int index = 0;
		if (bornPoints.size() > 1) {
			index = ran.nextInt(bornPoints.size());
		}
		return bornPoints.get(index);
	}
	/**
	 * 
	 * @param now
	 * @return 
	 */
	public boolean isDisasterOpen(long now) {
		for (DisasterOpenModule o : timeRules) {
			long[] temp = o.getOpenTime();
			if (now >= temp[0] && now < temp[1]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否需要发送世界提示
	 * @param now
	 * @param pretime
	 * @return
	 */
	public boolean needPrenotice(long now, long pretime) {
		for (DisasterOpenModule o : timeRules) {
			long[] temp = o.getOpenTime();
			if (now >= temp[1] || now >= temp[0]) {		//活动已结束或者已开启
				continue;
			}
			if ((temp[0] - pretime) <= now) {
				return true;
			}
		}
		return false;
	}
	
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public List<Integer[]> getBornPoints() {
		return bornPoints;
	}
	public void setBornPoints(List<Integer[]> bornPoints) {
		this.bornPoints = bornPoints;
	}
	public int[] getMonkeyNPCId() {
		return monkeyNPCId;
	}
	public void setMonkeyNPCId(int[] monkeyNPCId) {
		this.monkeyNPCId = monkeyNPCId;
	}
	public List<Integer[]> getMonkeyPoints() {
		return monkeyPoints;
	}
	public void setMonkeyPoints(List<Integer[]> monkeyPoints) {
		this.monkeyPoints = monkeyPoints;
	}

	public List<DisasterOpenModule> getTimeRules() {
		return timeRules;
	}

	public void setTimeRules(List<DisasterOpenModule> timeRules) {
		this.timeRules = timeRules;
	}
	public List<Integer[]> getFireNPCPoints() {
		return fireNPCPoints;
	}
	public void setFireNPCPoints(List<Integer[]> fireNPCPoints) {
		this.fireNPCPoints = fireNPCPoints;
	}
	public int[] getSecondStepNPCId() {
		return secondStepNPCId;
	}
	public void setSecondStepNPCId(int[] secondStepNPCId) {
		this.secondStepNPCId = secondStepNPCId;
	}
	public int[] getThirdStepNPCId() {
		return thirdStepNPCId;
	}
	public void setThirdStepNPCId(int[] thirdStepNPCId) {
		this.thirdStepNPCId = thirdStepNPCId;
	}
	public void setTempOpenRules(String[] tempOpenRules) {
		this.tempOpenRules = tempOpenRules;
	}
	public void setTempFireNpcPoints(String[] tempFireNpcPoints) {
		this.tempFireNpcPoints = tempFireNpcPoints;
	}
	public void setTempMonkeyPoints(String[] tempMonkeyPoints) {
		this.tempMonkeyPoints = tempMonkeyPoints;
	}
	public void setTempBornPoints(String[] tempBornPoints) {
		this.tempBornPoints = tempBornPoints;
	}

	public List<Integer[]> getBoxPoints() {
		return boxPoints;
	}

	public void setBoxPoints(List<Integer[]> boxPoints) {
		this.boxPoints = boxPoints;
	}

	public String[] getTempBoxPoints() {
		return tempBoxPoints;
	}

	public void setTempBoxPoints(String[] tempBoxPoints) {
		this.tempBoxPoints = tempBoxPoints;
	}

	public int[] getFirstStepNPCId() {
		return firstStepNPCId;
	}

	public void setFirstStepNPCId(int[] firstStepNPCId) {
		this.firstStepNPCId = firstStepNPCId;
	}

	public void setFireOuterPoints(String[] fireOuterPoints) {
		this.fireOuterPoints = fireOuterPoints;
	}

	public List<Integer[]> getFireNPCOuterPoints() {
		return fireNPCOuterPoints;
	}

	public void setFireNPCOuterPoints(List<Integer[]> fireNPCOuterPoints) {
		this.fireNPCOuterPoints = fireNPCOuterPoints;
	}

	public String[] getTempMonsterPoints() {
		return tempMonsterPoints;
	}

	public void setTempMonsterPoints(String[] tempMonsterPoints) {
		this.tempMonsterPoints = tempMonsterPoints;
	}

	public List<Integer[]> getMonsterPoints() {
		return monsterPoints;
	}

	public void setMonsterPoints(List<Integer[]> monsterPoints) {
		this.monsterPoints = monsterPoints;
	}

	public String[] getTempMonsterPoints2() {
		return tempMonsterPoints2;
	}

	public void setTempMonsterPoints2(String[] tempMonsterPoints2) {
		this.tempMonsterPoints2 = tempMonsterPoints2;
	}

	public List<Integer[]> getMonsterPoints2() {
		return monsterPoints2;
	}

	public void setMonsterPoints2(List<Integer[]> monsterPoints2) {
		this.monsterPoints2 = monsterPoints2;
	}

	public int[] getTempNpcIds() {
		return tempNpcIds;
	}

	public void setTempNpcIds(int[] tempNpcIds) {
		this.tempNpcIds = tempNpcIds;
	}

	public List<Integer[]> getSecondMonsterPoints() {
		return secondMonsterPoints;
	}

	public void setSecondMonsterPoints(List<Integer[]> secondMonsterPoints) {
		this.secondMonsterPoints = secondMonsterPoints;
	}

	public String[] getTempPoints() {
		return tempPoints;
	}

	public void setTempPoints(String[] tempPoints) {
		this.tempPoints = tempPoints;
	}
	
}
