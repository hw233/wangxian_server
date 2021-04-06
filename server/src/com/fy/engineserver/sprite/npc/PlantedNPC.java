package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;

/**
 * 种植NPC 用于种植不同类型的植物
 * 
 * 
 * 
 * 
 */
public class PlantedNPC extends NPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2814304206445013789L;

	/**
	 * 没有成熟状态
	 */
	public static final int UN_RIPE_STATE = 0;
	/**
	 * 成熟种植者可以采摘状态
	 */
	public static final int RIPE_STATE_AND_PLANTPLAYER_ONLY = 1;
	/**
	 * 成熟所有人可以采摘状态
	 */
	public static final int RIPE_STATE_AND_ALLPLAYER_CAN = 2;
	/**
	 * 种植者id
	 */
	private long plantPlayerId;

	/**
	 * 种植时的等级
	 */
	private int plantLevel;

	/**
	 * 种植的时间
	 */
	private long plantTime;

	/**
	 * 成熟的时间点
	 */
	private long ripeTime;

	/**
	 * 种植者采摘的时长
	 */
	private long personalPickupLastingTime;

	/**
	 * 成熟后的形象
	 */
	private int afterRipeImage;

	/**
	 * 成长中的形象
	 */

	private int growingImage;

	/**
	 * 基本的形象
	 */
	private int basicImage;
	/**
	 * 成熟的需要的时间
	 */
	private long ripeLastingTime;
	/**
	 * 种植物的名称
	 */
	private String plantName;
	
	/**
	 * 种植NPC携带的物品名称数组，此数组中的物品必出一个
	 */
	protected String mainArticleName[];
	
	public String[] getMainArticleName() {
		return mainArticleName;
	}

	public void setMainArticleName(String[] mainArticleName) {
		this.mainArticleName = mainArticleName;
	}

	/**
	 * 伴生物品数组，如果能出伴生那么从这个数组中选择一个出现
	 */
	protected String associatedArticleNames[];
	/**
	 * 出伴生的概率
	 */
	protected double hasAssociatedPercent;

	/**
	 * 如果该属性没有值，那么associatedArticleNames随机出，
	 * 如果有值那么按照这个数组的值就是associatedArticleNames的几率
	 */
	protected int[] associatedPercentValues;


	private boolean isPlanted = false;

	public String[] getAssociatedArticleNames() {
		return associatedArticleNames;
	}

	public void setAssociatedArticleNames(String[] associatedArticleNames) {
		this.associatedArticleNames = associatedArticleNames;
	}

	public double getHasAssociatedPercent() {
		return hasAssociatedPercent;
	}

	public void setHasAssociatedPercent(double hasAssociatedPercent) {
		this.hasAssociatedPercent = hasAssociatedPercent;
	}

	public int[] getAssociatedPercentValues() {
		return associatedPercentValues;
	}

	public void setAssociatedPercentValues(int[] associatedPercentValues) {
		this.associatedPercentValues = associatedPercentValues;
	}

	public long getRipeLastingTime() {
		return ripeLastingTime;
	}

	public void setRipeLastingTime(long ripeLastingTime) {
		this.ripeLastingTime = ripeLastingTime;
	}

	public boolean isPlanted() {
		return isPlanted;
	}

	
	public void setPlanted(boolean isPlanted) {
		this.isPlanted = isPlanted;
	}

	public int getGrowingImage() {
		return growingImage;
	}

	public void setGrowingImage(int growingImage) {
		this.growingImage = growingImage;
	}

	public int getBasicImage() {
		return basicImage;
	}

	public void setBasicImage(int basicImage) {
		this.basicImage = basicImage;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public int getGrowupImage() {
		return growingImage;
	}

	public void setGrowupImage(int growupImage) {
		this.growingImage = growupImage;
	}

	private String fruitName;

	public int getAfterRipeImage() {
		return afterRipeImage;
	}

	public void setAfterRipeImage(int afterRipeImage) {
		this.afterRipeImage = afterRipeImage;
	}

	public String getFruitName() {
		return fruitName;
	}

	public void setFruitName(String fruitName) {
		this.fruitName = fruitName;
	}

	/**
	 * 现在的状态，通过此状态来改变相应的显示内容，0代表是空地，1代表成长期，2代表成熟期
	 */
	private int currentState = 0;

	public long getPlantPlayerId() {
		return plantPlayerId;
	}

	public void setPlantPlayerId(long plantPlayerId) {
		this.plantPlayerId = plantPlayerId;
	}

	

	public int getPlantLevel() {
		return plantLevel;
	}

	public void setPlantLevel(int plantLevel) {
		this.plantLevel = plantLevel;
	}

	public long getPlantTime() {
		return plantTime;
	}

	public void setPlantTime(long plantTime) {
		this.plantTime = plantTime;
	}

	public long getRipeTime() {
		return ripeTime;
	}

	public void setRipeTime(long ripeTime) {
		this.ripeTime = ripeTime;
	}

	public long getPersonalPickupLastingTime() {
		return personalPickupLastingTime;
	}

	public void setPersonalPickupLastingTime(long personalPickupLastingTime) {
		this.personalPickupLastingTime = personalPickupLastingTime;
	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
		if (this.isAlive() == false)
			return;
		if (this.isPlanted && heartBeatStartTime >= ripeTime) {
			// 设置成熟形象
//			if (this.getType() != (byte) afterRipeImage) {
//				this.setType((byte) afterRipeImage);
//				currentState = 2;
//			}
			// 已经到达枯萎时间，置为空地
//			if (heartBeatStartTime >= (ripeTime + personalPickupLastingTime)) {
//				this.setType((byte) basicImage);
//				currentState = 0;
//				this.isPlanted = false;
//			}
		}
		// 成长期
		else if (this.isPlanted && heartBeatStartTime < ripeTime) {
//			if (this.getType() != (byte) growingImage) {
//				this.setType((byte) growingImage);
//				currentState = 1;
//			}
		}
		// 空地
		else {
			// 没有种植时的形象
//			if (this.getType() != (byte) basicImage) {
//				this.setType((byte) basicImage);
//				currentState = 0;
//			}
		}

	}

	

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		PlantedNPC p = new PlantedNPC();
		p.cloneAllInitNumericalProperty(this);

		p.country = country;

		p.setnPCCategoryId(this.getnPCCategoryId());

		p.windowId = windowId;

		return p;
	}

}
