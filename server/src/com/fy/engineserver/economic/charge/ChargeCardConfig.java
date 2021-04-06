/**
 * 
 */
package com.fy.engineserver.economic.charge;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 代表一档充值
 */
public class ChargeCardConfig {


	private String id = "";
	//活动显示类型
	//0：默认什么都不显示，1：纯文字显示，2：首充送，3：额外送
	private int activityType;
	private String iconOrDesc = "";
	//1：固定金额(单位是文)
	//2：额外比例，百分比
	private int giveType;
	private long giveValue;
	//表头描述
	private String tableHead = "";
	private long startTime;
	private long endTime;
	private List<String> openServers = new ArrayList<String>();
	private List<String> openChannel = new ArrayList<String>();
	//推荐icon,留个口
	private String tuiJianIcon = "";	
	//充值可获得多少银子描述
	private String silverInfo = "";
	//充值的rmb描述eg：30元
	private String needChargeRmb = "";
	private String showInIcon = "";
	//客户端显示
	private String backageIcon = "";
	
	private String specConfigs = "";
	//1:首冲,2:额外送
	private int chargeType;
	
	public boolean canFirstCharge(Player p){
		if(chargeType == 1){
			return NewChongZhiActivityManager.instance.canFirstCharge(p,id);
		}
		return true;
	}
	
	
	public long getActivitySilve(long yinzi,Player player){
		long addYinZi = 0;
		if(giveType == 1){
			addYinZi = giveValue;
		}else if(giveType == 2){
			addYinZi = (long) (yinzi * ((double)(giveValue) / 100));	
		}
		ChargeManager.logger.warn("[获取充值活动赠送银子] [id:"+id+"] [增加类型:"+giveType+"] [增加银子:"+addYinZi+"] [yinzi:"+yinzi+"] ["+player.getLogString()+"]");
		return addYinZi;
	}
	
	public static void main(String[] args) {
		long addYinZi = 0;
		long yinzi = 250000;
		addYinZi = (long) (yinzi * ((double)(50) / 100));	
		System.out.println(addYinZi);
	}
	
	public boolean isEffect(String channel){
		if(SystemTime.currentTimeMillis() >= startTime && SystemTime.currentTimeMillis() < endTime){
			boolean serverFit = false;
			boolean channelFit = false;
			if(openServers == null || openServers.size() == 0 || openServers.contains(GameConstants.getInstance().getServerName())){
				serverFit = true;
			}
			if(openChannel == null || openChannel.size() == 0 || openChannel.contains(channel)){
				channelFit = true;
			}
			if(serverFit && channelFit){
				return true;
			}
		}else{
			NewChongZhiActivityManager.instance.cleanFirstChargeData();
		}
		
		return false;
	}
	
	public boolean isEffectDate(){
		if(SystemTime.currentTimeMillis() >= startTime && SystemTime.currentTimeMillis() < endTime){
			return true;
		}else{
			NewChongZhiActivityManager.instance.cleanFirstChargeData();
		}
		
		return false;
	}
	
	public String getSpecConfigs() {
		return specConfigs;
	}

	public void setSpecConfigs(String specConfigs) {
		this.specConfigs = specConfigs;
	}

	public String getShowInIcon() {
		return showInIcon;
	}

	public String getBackageIcon() {
		return backageIcon;
	}

	public int getChargeType() {
		return chargeType;
	}

	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}

	public void setBackageIcon(String backageIcon) {
		this.backageIcon = backageIcon;
	}

	public void setShowInIcon(String showInIcon) {
		this.showInIcon = showInIcon;
	}

	public String getTableHead() {
		return tableHead;
	}

	public void setTableHead(String tableHead) {
		this.tableHead = tableHead;
	}

	public int getGiveType() {
		return giveType;
	}

	public void setGiveType(int giveType) {
		this.giveType = giveType;
	}

	public long getGiveValue() {
		return giveValue;
	}

	public void setGiveValue(long giveValue) {
		this.giveValue = giveValue;
		if(giveType == 1){
			showInIcon = BillingCenter.得到带单位的银两(giveValue); //String.valueOf(giveValue / 1000) + "两";
		}else if(giveType == 2){
			showInIcon = String.valueOf((giveValue)) + "%";
		}
	}

	public String getIconOrDesc() {
		return iconOrDesc;
	}

	public void setIconOrDesc(String iconOrDesc) {
		this.iconOrDesc = iconOrDesc;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<String> getOpenServers() {
		return openServers;
	}

	public void setOpenServers(List<String> openServers) {
		this.openServers = openServers;
	}

	public List<String> getOpenChannel() {
		return openChannel;
	}

	public void setOpenChannel(List<String> openChannel) {
		this.openChannel = openChannel;
	}

	public String getSilverInfo() {
		return silverInfo;
	}
	public void setSilverInfo(String silverInfo) {
		this.silverInfo = silverInfo;
	}
	public String getNeedChargeRmb() {
		return needChargeRmb;
	}
	public void setNeedChargeRmb(String needChargeRmb) {
		this.needChargeRmb = needChargeRmb;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getActivityType() {
		return activityType;
	}
	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public String getTuiJianIcon() {
		return tuiJianIcon;
	}

	public void setTuiJianIcon(String tuiJianIcon) {
		this.tuiJianIcon = tuiJianIcon;
	}

	@Override
	public String toString() {
		return " [activityType=" + activityType
				+ ", backageIcon=" + backageIcon + ", endTime=" + endTime
				+ ", giveType=" + giveType + ", giveValue=" + giveValue
				+ ", iconOrDesc=" + iconOrDesc + ", id=" + id
				+ ", needChargeRmb=" + needChargeRmb + ", openChannel="
				+ openChannel + ", openServers=" + openServers
				+ ", showInIcon=" + showInIcon + ", silverInfo=" + silverInfo
				+ ", specConfigs=" + specConfigs + ", startTime=" + startTime
				+ ", tableHead=" + tableHead + ", tuiJianIcon=" + tuiJianIcon
				+ "]";
	}

	
}
