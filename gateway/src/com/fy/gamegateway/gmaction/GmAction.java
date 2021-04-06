package com.fy.gamegateway.gmaction;

import java.util.Arrays;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"playerId"}),
	@SimpleIndex(members={"playerName"}),
	@SimpleIndex(members={"userName"}),
	@SimpleIndex(members={"actionType"}),
	@SimpleIndex(members={"actionTime"}),
	@SimpleIndex(members={"serverName"})
})
public class GmAction implements Cacheable {
	//禁言
	public static final int ACTION_FORBID_CHAT = 0;
	//清空银两
	public static final int ACTION_CLEAR_SILVER = 1;
	//补发银两
	public static final int ACTION_ADD_SILVER = 2;
	//补发旧道具
	public static final int ACTION_ADD_OLD_ARTICLE = 3;
	//补发新道具
	public static final int ACTION_ADD_NEW_ARTICLE = 4;
	//删除背包物品
	public static final int ACTION_REMOVE_ARTICLE = 5;
	//补发宠物
	public static final int ACTION_ADD_PET = 6;
	//修改密码
	public static final int ACTION_EDIT_PASSWD = 7;
	//封号
	public static final int ACTION_FORBID_ACCOUNT = 8;
	//解封
	public static final int ACTION_RELEASE_ACCOUNT = 9;
	//清空密保
	public static final int ACTION_CLEAR_MIBAO = 10;
	//清空授权
	public static final int ACTION_CLEAR_AUTHORIZATION = 11;
	//清空背包某一格
	public static final int ACTION_CLEAR_KNAPSACKCELL = 12;
	
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int version;
	
	/**
	 * 服务器
	 */
	private String serverName;
	
	/**
	 * 角色id
	 */
	private long playerId;
	
	/**
	 * 角色姓名
	 */
	private String playerName;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 操作类型
	 */
	private int actionType;
	
	/**
	 * 操作的原因
	 */
	private String reason;
	
	/**
	 * 操作人
	 */
	private String operator;
	
	/**
	 * 操作的时间
	 */
	private long actionTime;
	
	/**
	 * 操作对应的数量
	 */
	private long amount;
	
	/**
	 * 补发或者删除的物品信息
	 */
	private String articleInfo;
	
	/**
	 * 补发的宠物信息
	 */
	private String petInfo;
	
	private String[] otherInfos;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public long getActionTime() {
		return actionTime;
	}

	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getArticleInfo() {
		return articleInfo;
	}

	public void setArticleInfo(String articleInfo) {
		this.articleInfo = articleInfo;
	}

	public String getPetInfo() {
		return petInfo;
	}

	public void setPetInfo(String petInfo) {
		this.petInfo = petInfo;
	}

	public void setOtherInfos(String[] otherInfos) {
		this.otherInfos = otherInfos;
	}

	public String[] getOtherInfos() {
		return otherInfos;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}
	
	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(id).append("]");
		sb.append(" [sN:").append(serverName).append("]");
		sb.append(" [uN:").append(userName).append("]");
		sb.append(" [pID:").append(playerId).append("]");
		sb.append(" [pN:").append(playerName).append("]");
		sb.append(" [amount:").append(amount).append("]");
		sb.append(" [").append(articleInfo).append("]");
		sb.append(" [").append(petInfo).append("]");
		sb.append(" [").append(Arrays.toString(otherInfos)).append("]");
		sb.append(" [操作Type:").append(actionType).append("]");
		sb.append(" [操作人:").append(operator).append("]");
		sb.append(" [原因:").append(reason).append("]");
		return sb.toString();
	}

	@Override
	public int getSize() {
		return 1;
	}

}
