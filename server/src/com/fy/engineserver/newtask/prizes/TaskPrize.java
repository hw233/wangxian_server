package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

/**
 * 任务奖励
 * 
 * 
 */
public class TaskPrize implements TaskConfig {

	/** 奖励类型 */
	private PrizeType prizeType;
	/** 奖励名字 [对于单一奖励,长度是1 对于多选奖励会有多个,totalNum表示多选奖励最多可以选择几个] */
	private String[] prizeName = new String[0];
	protected long[] prizeId = new long[0];
	/** 奖励数量 */
	private long[] prizeNum = new long[0];
	/** 奖励个数 */
	private int totalNum;

	private int[] prizeColor = new int[0];

	private byte prizeByteType;

	public TaskPrize() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 给玩家发送奖励
	 * @param player
	 * @param index选择奖励的index单一奖励就是null
	 * @return
	 */
	public void doPrize(Player player, int[] index, Task task) {
		TaskSubSystem.logger.error("[未处理的任务奖励:{}] [任务:{}]", new Object[] { getPrizeType().getName(), task.getName() });
	}

	public String toHtmlString(String cssClass) {
		return "";
	}

	public PrizeType getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(PrizeType prizeType) {
		this.prizeType = prizeType;
	}

	public String[] getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String[] prizeName) {
		this.prizeName = prizeName;
	}

	public long[] getPrizeNum() {
		return prizeNum;
	}

	public void setPrizeNum(long[] prizeNum) {
		this.prizeNum = prizeNum;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public boolean isSelectPrize() {
		return getTotalNum() > 0;
	}

	public byte getPrizeByteType() {
		return prizeByteType;
	}

	public void setPrizeByteType(byte prizeByteType) {
		this.prizeByteType = prizeByteType;
	}

	public int[] getPrizeColor() {
		return prizeColor;
	}

	public void setPrizeColor(int[] prizeColor) {
		this.prizeColor = prizeColor;
	}

	public long[] getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(long[] prizeId) {
		this.prizeId = prizeId;
	}

	public void initArticle(Task task) {

	}
}
