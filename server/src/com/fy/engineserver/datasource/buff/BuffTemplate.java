package com.fy.engineserver.datasource.buff;


/**
 * Buff的模板，此模板可以创建出buff来 
 * 
 * 
 * 
 *
 */
public abstract class BuffTemplate {

	private transient String name;
	
	private transient int id;
	private transient int groupId;
	
	/**
	 * buff的类型，
	 * 所有的buff分为几种类型，比如 血瓶对应的buff，菜肴对应的buff，中毒buff，迟缓buff，定身buff，技能增加爆击率的buff等等
	 * 
	 * buff的分类是事先严格定义好的，同一类型的buff在人物的身上只能有一个
	 */
	private transient byte buffType;
	
	private transient String description;

	protected transient String iconId;

	/**
	 * 时间有效类型
	 * 分两种，一种是真实的时间类型，比如有效期30分钟，那么30分钟后就失效，无论在这30分钟内，是否在玩游戏
	 * 		  一种是游戏时间类型， 比如有效期30分钟，那么就是说在游戏里30分钟后失效，如果10分钟后下线，第二天上线，那么有效期还有20分钟
	 * 0 标识游戏时间
	 * 1 标识真实时间
	 * 2 标识下线或死亡就失效
	 */
	private transient byte timeType;
	
	/**
	 * 是否要与客户端同步，如果设置为true，
	 * 那么种植此Buff的时候，服务器要通知客户端。
	 * 
	 * 否则不通知客户端。大部分buff是不需要通知客户端的，
	 * 比如技能增加自身的暴击率，增加自身的防御等。
	 */
	private transient boolean syncWithClient = false;
	
	
	/**
	 * 标记此Buff是有利的buff，还是有害的buff，
	 * 设置为true的时候，表示为有利
	 */
	private transient boolean advantageous = false;
	
	/**
	 * 战斗状态停止，true停止
	 */
	private transient boolean fightStop = false;
	
	/**
	 * 何种地图中可以使用BUFF 0:战场外可用 ，1:战场中可用，2:都可用
	 */
	private transient byte canUseType;

	/**
	 * 挂机使用的值，平时这个值对buff无意义
	 * 0为一般方式，1为挂机时使用的有益buff
	 */
	public byte usedType;
	
	/**
	 * 清技能点不消失
	 */
	public boolean clearSkillPointNotdisappear;
	
	/**
	 * 死亡不消失
	 */
	public boolean deadNotdisappear;

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

	public byte getTimeType() {
		return timeType;
	}

	public void setTimeType(byte timeType) {
		this.timeType = timeType;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	/**
	 * buff的类型，
	 * 所有的buff分为几种类型，比如 血瓶对应的buff，菜肴对应的buff，中毒buff，迟缓buff，定身buff，技能增加爆击率的buff等等
	 * 
	 * buff的分类是事先严格定义好的，同一类型的buff在人物的身上只能有一个
	 * @return
	 */
	public byte getBuffType(){
		return buffType;
	}
	
	public void setBuffType(byte b){
		buffType = b;
	}

	public String getDescription(){
		return description;
	}
	
	public void setDescription(String s) {
		this.description = s;
	}

	public String getIconId() {
		return iconId;
	}

	public void setIconId(String iconName) {
		this.iconId = iconName;
	}

	/**
	 * 根据模板创建出Buff
	 * 
	 * @return
	 */
	public abstract Buff createBuff(int level);

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getUsedType() {
		return usedType;
	}

	public void setUsedType(byte usedType) {
		this.usedType = usedType;
	}

	public boolean isClearSkillPointNotdisappear() {
		return clearSkillPointNotdisappear;
	}

	public void setClearSkillPointNotdisappear(boolean clearSkillPointNotdisappear) {
		this.clearSkillPointNotdisappear = clearSkillPointNotdisappear;
	}

	public boolean isDeadNotdisappear() {
		return deadNotdisappear;
	}

	public void setDeadNotdisappear(boolean deadNotdisappear) {
		this.deadNotdisappear = deadNotdisappear;
	}
}
