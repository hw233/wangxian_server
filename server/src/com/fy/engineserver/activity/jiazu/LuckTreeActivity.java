package com.fy.engineserver.activity.jiazu;

import java.util.Arrays;

import com.fy.engineserver.activity.BaseActivityInstance;
/**
 * 家族炼丹活动
 * @author Administrator
 *
 */
public class LuckTreeActivity extends BaseActivityInstance{
	/** 炼丹颜色 */
	private int[] colorTypes ;
	/** 对应开启炼丹需要龙图阁等级 */
	private int[] longtugeLevel;
	/** 对应需要的家族资金 */
	private long[] costJiazuMoney;
	
	public LuckTreeActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "LuckTreeActivity [colorTypes=" + Arrays.toString(colorTypes) + ", longtugeLevel=" + Arrays.toString(longtugeLevel) + ", costJiazuMoney=" + Arrays.toString(costJiazuMoney) + "]";
	}


	/**
	 * 根据龙图阁等级和颜色判断是否可以种植	
	 * @param longtuLevel
	 * @param colorType
	 * @return
	 */
	public boolean canPlant(int longtuLevel, int colorType) {
		if (longtugeLevel != null && longtugeLevel.length > 0) {
			for (int i=0; i<longtugeLevel.length; i++) {
				if (longtugeLevel[i] > longtuLevel) {
					continue;
				} else if (colorTypes[i] >= colorType) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 获取消耗的家族资金
	 * @param colorType
	 * @return -1代表活动不开启  走以前的正常流程
	 */
	public long getCostMoney(int colorType) {
		if (colorTypes == null || colorTypes.length <= 0) {
			return -1;
		}
		for (int i=0; i<colorTypes.length; i++) {
			if (colorTypes[i] == colorType) {
				return costJiazuMoney[i];
			}
		}
		return -1;
	}
	
	public void initOtherStr(String colorType, String levels, String costs) throws Exception {
		try {
			if (colorType.indexOf(",") >= 0) {
				String[] s1 = colorType.split(",");
				String[] s2 = levels.split(",");
				String[] s3 = costs.split(",");
				if (s1.length != s2.length || s1.length != s3.length) {
					throw new Exception("[炼丹颜色个数与龙图阁等级或消耗资金不对应] [" + s1 + "--" + s2 + "--" + s3);
				}
				colorTypes = new int[s1.length];
				longtugeLevel = new int[s2.length];
				costJiazuMoney = new long[s3.length];
				for (int i=0; i<s1.length; i++) {
					colorTypes[i] = Integer.parseInt(s1[i]);
					longtugeLevel[i] = Integer.parseInt(s2[i]);
					costJiazuMoney[i] = Long.parseLong(s3[i]);
				}
			} else {
				int a1 = Integer.parseInt(colorType);
				int a2 = Integer.parseInt(levels);
				long a3 = Long.parseLong(costs);
				colorTypes = new int[]{a1};
				longtugeLevel = new int[]{a2};
				costJiazuMoney = new long[]{a3};
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[家族炼丹活动] [炼丹颜色:" + Arrays.toString(colorTypes) + "] [龙图阁等级:" + Arrays.toString(longtugeLevel) + "]");
		sb.append("[消耗家族资金:" + Arrays.toString(costJiazuMoney) + "]");
		return sb.toString();
	}

	public int[] getColorTypes() {
		return colorTypes;
	}

	public void setColorTypes(int[] colorTypes) {
		this.colorTypes = colorTypes;
	}

	public int[] getLongtugeLevel() {
		return longtugeLevel;
	}

	public void setLongtugeLevel(int[] longtugeLevel) {
		this.longtugeLevel = longtugeLevel;
	}

	public long[] getCostJiazuMoney() {
		return costJiazuMoney;
	}

	public void setCostJiazuMoney(long[] costJiazuMoney) {
		this.costJiazuMoney = costJiazuMoney;
	}
	
	

}
