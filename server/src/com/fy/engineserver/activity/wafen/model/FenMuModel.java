package com.fy.engineserver.activity.wafen.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel;
import com.fy.engineserver.activity.wafen.manager.WaFenManager;
import com.fy.engineserver.sprite.Player;

public class FenMuModel {
	/** 坟墓id */
	private int id;			//1
	/** 坟墓名 */
	private String name;		//1
	/** 共享类型 (0为个人拥有 1为全服共有) */
	private byte shareType;		//1
	/** 坑位总数 */
	private int totalNum;		//1
	/** 行宽 */
	private int width;	//1
	/** 列高 */
	private int height;		//1
	/** 开启次数 */
	private int[] openTimes4Cost;		//1
	/** 消耗物品类型(0银铲子 1金铲子 2琉璃铲子 3银铲子+金铲子) */
	private List<Integer[]> costType;	//1
	/** 消耗数量 */
	private List<Integer[]> costNum;	//1
	/** 必产出物品id */
	private List<Integer> waFenArticleId;	//1
	/** 坟墓可产出道具列表 (key=坟墓物品id)*/
	private LinkedHashMap<Integer, FenMuArticleModel> articleMap;
	/** 帮助信息 */
	private String helpInfo;
	
	@Override
	public String toString() {
		return "FenMuModel [id=" + id + ", name=" + name + ", shareType=" + shareType + ", totalNum=" + totalNum + ", width=" + width + ", height=" + height + ", openTimes4Cost=" + Arrays.toString(openTimes4Cost) + ", costType=" + costType + ", costNum=" + costNum + ", waFenArticleId=" + waFenArticleId + ", articleMap=" + articleMap + "]";
	}
	
	public int[] getCostChanziType () {
		List<Integer> list = new ArrayList<Integer>();
		for (Integer[] in : costType) {
			for (Integer o : in) {
				if (!list.contains(in)) {
					list.add(o);
				}
			}
		}
		int[] result = new int[list.size()];
		for (int i=0; i<list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	/**
	 * 根据挖坟次数获得消耗类型和数量
	 * @param times
	 * @return	0为消耗类型   1为对应数量  (null代表不可挖取)
	 */
	public List<Integer[]> getCostByTimes(int times) {
		if (times >= totalNum) {
			return null;
		}
		List<Integer[]> result = new ArrayList<Integer[]>();
		try {
			for (int i = 0; i < openTimes4Cost.length; i++) {
				if (openTimes4Cost[i] >= times) {
					result.add(costType.get(i));
					result.add(costNum.get(i));
				}
			}
		} catch (Exception e) {
			WaFenManager.logger.error("[挖坟活动] [挖坟取得挖取的物品id] [失败] [概率为null] [times:" + times + "]", e);
		}
		return result;
	}

	/**
	 * 
	 * @param type
	 *            使用铲子类型
	 * @param times
	 *            已经挖取的次数
	 * @param opened
	 *            已经挖取到的物品列表
	 * @return
	 */
	public FenMuArticleModel getResultByType(Player player, byte type, int times, List<FenDiModel> opened) {
		if (times >= totalNum) {
			return null;
		}
		try {
			Map<Integer, Integer> tempOpened = new HashMap<Integer, Integer>();
			for (FenDiModel fd : opened) {
				Integer a = tempOpened.get(fd.getArticleId());
				if (a == null) {
					a = 1;
				} else {
					a += 1;
				}
				tempOpened.put(fd.getArticleId(), a);
			}
			int totalProbabbly = 0;
			List<FenMuArticleModel> tempList = new ArrayList<FenMuArticleModel>();		//获取物品的list
			for (FenMuArticleModel fam : articleMap.values()) {
				Integer a = tempOpened.get(fam.getId());
				if (a != null && a >= fam.getMaxNum()) {
					continue;
				}
				if (fam.getProbabblyByTypeAndTimes(type, times) > 0) {
					totalProbabbly += fam.getProbabblyByTypeAndTimes(type, times);
					tempList.add(fam);
				}
			}
			FenMuArticleModel resultId = null;
			if (waFenArticleId != null && times >= (totalNum - waFenArticleId.size())) {
				for (Integer ii : waFenArticleId) {
					if (tempOpened.get(ii) == null) {
						resultId = articleMap.get(ii);
						break;
					}
				}
			}
			if (resultId == null) {
				int ran = player.random.nextInt(totalProbabbly);
				int tempProb = 0;
				for (int i=0; i<tempList.size(); i++) {
					tempProb+= tempList.get(i).getProbabblyByTypeAndTimes(type, times);
					if (tempProb >= ran) {
						resultId = tempList.get(i);
						break;
					}
				}
			}
			WaFenManager.logger.warn("[挖坟活动] [根据概率取得玩家获取的物品] [成功] [获取的物品:" + resultId + "] [coseType:" + type + "] [挖坟次数:" + times + "] [坟墓名:" + name + "] [" + player.getLogString() + "]");
			return resultId;
		} catch (Exception e) {
			WaFenManager.logger.error("[挖坟活动] [挖坟取得挖取的物品id] [失败] [概率为null] [type:" + type + "] [times:" + times + "] [opened:" + opened + "] [" + player.getLogString() + "]", e);
		}
		return null;
	}

	public int[] getOpenTimes4Cost() {
		return openTimes4Cost;
	}

	public void setOpenTimes4Cost(int[] openTimes4Cost) {
		this.openTimes4Cost = openTimes4Cost;
	}



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

	public byte getShareType() {
		return shareType;
	}

	public void setShareType(byte shareType) {
		this.shareType = shareType;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public List<Integer[]> getCostType() {
		return costType;
	}

	public void setCostType(List<Integer[]> costType) {
		this.costType = costType;
	}

	public List<Integer[]> getCostNum() {
		return costNum;
	}

	public void setCostNum(List<Integer[]> costNum) {
		this.costNum = costNum;
	}

	public List<Integer> getWaFenArticleId() {
		return waFenArticleId;
	}

	public void setWaFenArticleId(List<Integer> waFenArticleId) {
		this.waFenArticleId = waFenArticleId;
	}

	public LinkedHashMap<Integer, FenMuArticleModel> getArticleMap() {
		return articleMap;
	}

	public void setArticleMap(LinkedHashMap<Integer, FenMuArticleModel> articleMap) {
		this.articleMap = articleMap;
	}

	public String getHelpInfo() {
		return helpInfo;
	}

	public void setHelpInfo(String helpInfo) {
		this.helpInfo = helpInfo;
	}


}
