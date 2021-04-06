package com.fy.engineserver.datasource.buff;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 人物身上的Buff，此Buff是需要被保存的
 * 
 * 
 * 
 * 
 */
@SimpleEmbeddable
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public class Buff {

	private static int seq_num = 0;
	private transient BuffTemplate template;

	private int seqId = seq_num++;

	/**
	 * 此buff的发起者
	 */
	private transient Fighter causer;

	/**
	 * 挂机使用的值，平时这个值对buff无意义
	 * 0为一般方式，1为挂机时使用的有益buff
	 */
	public byte usedType;

	private String templateName;

	private int level;

	private long startTime;

	private long invalidTime;

	private long heartBeatStartTime;

	private String description;

	private String iconId = "";

	/**
	 * 当buff没有group的时候，此值为-1
	 */
	private int groupId = -1;

	private int templateId;

	private byte bufferType;

	/**
	 * 是否要与客户端同步，如果设置为true，
	 * 那么种植此Buff的时候，服务器要通知客户端。
	 * 
	 * 否则不通知客户端。大部分buff是不需要通知客户端的，
	 * 比如技能增加自身的暴击率，增加自身的防御等。
	 */
	private boolean syncWithClient = false;

	/**
	 * 标记此Buff是有利的buff，还是有害的buff，
	 * 设置为true的时候，表示为有利
	 */
	private boolean advantageous = false;

	/**
	 * 战斗状态停止，true停止
	 */
	private boolean fightStop = false;

	/**
	 * 何种地图中可以使用BUFF 0:都可用 1:战场中可用，2: 战场外可用
	 */
	private transient byte canUseType;

	/**
	 * 是否永久，所谓永久是不依赖时间，而是依赖其他条件的
	 */
	private boolean forover = false;

	private boolean notSaveFlag = false;

	/**
	 * 有效时间已经过去了多少
	 */
	private int percentOfPassed = 0;
	
	private String[] limitMaps = null;

	public Buff() {
		
	}

	@Override
	protected void finalize() throws Throwable {
		
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getPercentOfPassed() {
		return percentOfPassed;
	}

	public void setPercentOfPassed(int percentOfPassed) {
		this.percentOfPassed = percentOfPassed;
	}

	public byte getBufferType() {
		return bufferType;
	}

	public void setBufferType(byte bufferType) {
		this.bufferType = bufferType;
	}

	public String getDescription() {
		if (description == null) return "no_description";
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BuffTemplate getTemplate() {
		return template;
	}

	public void setTemplate(BuffTemplate template) {
		this.template = template;
		this.templateId = template.getId();
		this.bufferType = template.getBuffType();
		this.templateName = template.getName();
		this.iconId = template.getIconId();
	}

	public String getTemplateName() {
		if (templateName == null) templateName = "";
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * buff的级别，从0开始
	 * @param l
	 */
	public void setLevel(int l) {
		level = l;
	}

	/**
	 * buff的级别，从0开始
	 * 级别，同一类型的Buff，高级别的Buff将替代低级别的Buff
	 * 替代的时候，低级别的buff的end方法将会被调用
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 失效时间
	 * @return
	 */
	public long getInvalidTime() {
		return invalidTime;
	}

	/**
	 * 失效时间
	 * @param t
	 */
	public void setInvalidTime(long t) {
		invalidTime = t;
	}

	public String getIconId() {
		return iconId;
	}

	public void setIconId(String iconName) {
		this.iconId = iconName;
	}

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner) {

	}

	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner) {

	}

	/**
	 * 判断buff是否还在有效期
	 * @return
	 */
	public boolean isValid() {
		return this.heartBeatStartTime < this.invalidTime;
	}

	/**
	 * 心跳函数，此心跳函数每1秒钟跳一次
	 */
	public void heartbeat(Fighter owner, long heartBeatStartTime, long interval, Game game) {
		this.heartBeatStartTime = heartBeatStartTime;
		// 处于战场地图，buff为普通地图buff
		if (owner.isInBattleField() && canUseType == 2) {
			this.invalidTime = 0;
		}
		if(owner instanceof Player){
//			if(CrossServerManager.getInstance().inPKGame((Player)owner) && canUseType == 2){
//				this.invalidTime = 0;
//			}
		}
		// 处于普通地图，buff为战场buff
		if (!owner.isInBattleField() && canUseType == 1) {
			this.invalidTime = 0;
		}
		if (this.fightStop) {
			if (owner instanceof Player) {
				if (((Player) owner).isFighting()) {
					this.invalidTime = 0;
				}
			}
		}
		if (limitMaps != null && limitMaps.length > 0) {
			try {
				if (game != null) {
					boolean b = false;
					for (String str : limitMaps) {
						if (game.gi.name.equalsIgnoreCase(str)) {
							b = true;
							break;
						}
					}
					if (!b) {
						this.invalidTime = 0;
					}
				}
			} catch (Exception e) {
				ArticleManager.logger.warn("[ 处理buff地图限制消失] [异常] [" + Arrays.toString(limitMaps) + "] [" + this.getTemplateName() + "] [" + owner.getId() + "] [" + owner.getName() + "]");
			}
		}
		if (this.invalidTime > this.startTime) this.percentOfPassed = (int) ((this.heartBeatStartTime - this.startTime) * 100 / (this.invalidTime - this.startTime));
		else this.percentOfPassed = 100;
	}

	public long getHeartBeatStartTime() {
		return heartBeatStartTime;
	}

	public void setHeartBeatStartTime(long heartBeatStartTime) {
		this.heartBeatStartTime = heartBeatStartTime;
	}

	public Fighter getCauser() {
		return causer;
	}

	public void setCauser(Fighter causer) {
		this.causer = causer;
	}

	public boolean isSyncWithClient() {
		return syncWithClient;
	}

	public void setSyncWithClient(boolean syncWithClient) {
		this.syncWithClient = syncWithClient;
	}

	public boolean isAdvantageous() {
		return advantageous;
	}

	public void setAdvantageous(boolean advantageous) {
		this.advantageous = advantageous;
	}

	public boolean isFightStop() {
		return fightStop;
	}

	public void setFightStop(boolean fightStop) {
		this.fightStop = fightStop;
	}

	public byte getCanUseType() {
		return canUseType;
	}

	public void setCanUseType(byte canUseType) {
		this.canUseType = canUseType;
	}

	public boolean isForover() {
		return forover;
	}

	public void setForover(boolean forover) {
		this.forover = forover;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqNum) {
		this.seqId = seqNum;
	}

	public boolean isNotSaveFlag() {
		return notSaveFlag;
	}

	public void setNotSaveFlag(boolean notSaveFlag) {
		this.notSaveFlag = notSaveFlag;
	}

	public byte getUsedType() {
		return usedType;
	}

	public void setUsedType(byte usedType) {
		this.usedType = usedType;
	}

	@Override
	public String toString() {
		return "Buff [seqId=" + seqId + ", usedType=" + usedType + ", templateName=" + templateName + ", level=" + level + ", startTime=" + startTime + ", invalidTime=" + invalidTime + ", heartBeatStartTime=" + heartBeatStartTime + ", description=" + description + ", iconId=" + iconId + ", groupId=" + groupId + ", templateId=" + templateId + ", bufferType=" + bufferType + ", syncWithClient=" + syncWithClient + ", advantageous=" + advantageous + ", fightStop=" + fightStop + ", forover=" + forover + ", notSaveFlag=" + notSaveFlag + ", percentOfPassed=" + percentOfPassed + "]";
	}

	public String[] getLimitMaps() {
		return limitMaps;
	}

	public void setLimitMaps(String[] limitMaps) {
		this.limitMaps = limitMaps;
	}
}
