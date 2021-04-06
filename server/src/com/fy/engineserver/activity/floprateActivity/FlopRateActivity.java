package com.fy.engineserver.activity.floprateActivity;

import java.util.List;

import com.fy.engineserver.activity.BaseActivityInstance;

public class FlopRateActivity extends BaseActivityInstance{
	/**
	 * 额外增加的倍率
	 */
	private double extraRate;
	/**
	 * 地图限制(地图名用拼音)
	 */
	private List<String> limitMaps;
	
	public FlopRateActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}

	public void setOtherVar(double extraRate, List<String> limitMaps) {
		this.extraRate = extraRate;
		this.limitMaps = limitMaps;
	}
	
	public String isThisServerFit(String mapName) {
		if(mapName == null || mapName.isEmpty()) {
			return "传过来的地图为空";
		}
		String result = super.isThisServerFit();
		if(result == null && limitMaps != null && limitMaps.size() > 0) {
			result = "地图不匹配";
			for(String maps : limitMaps) {
				if(mapName.equals(maps)) {
					result = null;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "FlopRateActivity [extraRate=" + extraRate + ", limitMaps=" + limitMaps + "]";
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[掉率翻倍活动]");
		sb.append("<br>[翻倍地图:");
		for(int i=0; i<limitMaps.size(); i++) {
			sb.append(limitMaps.get(i));
			if(i < (limitMaps.size() - 1)) {
				sb.append("、");
			}
		}
		sb.append("]<br>[额外增加掉率倍数:" + extraRate + "]");
		return sb.toString();
	}

	public double getExtraRate() {
		return extraRate;
	}

	public void setExtraRate(double extraRate) {
		this.extraRate = extraRate;
	}

	public List<String> getLimitMaps() {
		return limitMaps;
	}

	public void setLimitMaps(List<String> limitMaps) {
		this.limitMaps = limitMaps;
	}

}
