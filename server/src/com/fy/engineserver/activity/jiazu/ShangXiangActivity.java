package com.fy.engineserver.activity.jiazu;

import java.util.Arrays;

import com.fy.engineserver.activity.BaseActivityInstance;

public class ShangXiangActivity extends BaseActivityInstance{

	private int[] shangxiangLevels;
	
	private long[] costMoneys;
	
	private int[] xiulianNums;
	
	
	@Override
	public String toString() {
		return "ShangXiangActivity [shangxiangLevels=" + Arrays.toString(shangxiangLevels) + ", costMoneys=" + Arrays.toString(costMoneys) + ", xiulianNums=" + Arrays.toString(xiulianNums) + "]";
	}

	public ShangXiangActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}
	
	public long getCostMoney(int level) {
		if (shangxiangLevels == null || shangxiangLevels.length <= 0) {
			return -1L;
		}
		for (int i=0; i<shangxiangLevels.length; i++) {
			if (shangxiangLevels[i] == level) {
				return costMoneys[i];
			}
		}
		return -1L;
	}
	
	public int getXiulianNum(int level) {
		if (shangxiangLevels == null || shangxiangLevels.length <= 0) {
			return -1;
		}
		for (int i=0; i<shangxiangLevels.length; i++) {
			if (shangxiangLevels[i] == level) {
				return xiulianNums[i];
			}
		}
		return -1;
	}
	
	
	public void initOtherStr(String shangxiang, String costs, String xiulian) throws Exception {
		if (shangxiang.indexOf(",") >= 0) {
			String[] s1 = shangxiang.split(",");
			String[] s2 = costs.split(",");
			String[] s3 = xiulian.split(",");
			if (s1.length != s2.length || s1.length != s3.length) {
				throw new Exception("[上香等级与修炼值数量或消耗资金不对应] [" + s1 + "--" + s2 + "--" + s3);
			}
			shangxiangLevels = new int[s1.length];
			costMoneys = new long[s1.length];
			xiulianNums = new int[s1.length];
			for (int i=0; i<s1.length; i++) {
				shangxiangLevels[i] = Integer.parseInt(s1[i]);
				costMoneys[i] = Long.parseLong(s2[i]);
				xiulianNums[i] = Integer.parseInt(s3[i]);
			}
		} else {
			int a1 = Integer.parseInt(shangxiang);
			long a2 = Long.parseLong(costs);
			int a3 = Integer.parseInt(xiulian);
			shangxiangLevels = new int[]{a1};
			costMoneys = new long[]{a2};
			xiulianNums = new int[]{a3};
		}
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[家族上香活动] [上香等级:" + Arrays.toString(shangxiangLevels) + "] [花费帮派资金:" + Arrays.toString(costMoneys) + "]");
		sb.append("[给予修炼值数:" + Arrays.toString(xiulianNums) + "]");
		return sb.toString(); 
	}

	public int[] getShangxiangLevels() {
		return shangxiangLevels;
	}

	public void setShangxiangLevels(int[] shangxiangLevels) {
		this.shangxiangLevels = shangxiangLevels;
	}

	public long[] getCostMoneys() {
		return costMoneys;
	}

	public void setCostMoneys(long[] costMoneys) {
		this.costMoneys = costMoneys;
	}

	public int[] getXiulianNums() {
		return xiulianNums;
	}

	public void setXiulianNums(int[] xiulianNums) {
		this.xiulianNums = xiulianNums;
	}
	
	

}
