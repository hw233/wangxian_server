package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Player;

public class TaskPrizeOfBuff extends TaskPrize implements TaskConfig {

	private int[] prizeColor;

	private TaskPrizeOfBuff() {
		setPrizeType(PrizeType.BUFF);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(String[] prizeName, int[] prizeColor, int totalNum) {
		TaskPrizeOfBuff prize = new TaskPrizeOfBuff();
		prize.setPrizeName(prizeName);
		prize.setTotalNum(totalNum);
		prize.setPrizeColor(prizeColor);
		return prize;
	}

	public int[] getPrizeColor() {
		return prizeColor;
	}

	public void setPrizeColor(int[] prizeColor) {
		this.prizeColor = prizeColor;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		if (index == null) {// 选第一个奖励

		} else {// 根据选择奖励

		}
	}

	/**
	 * 是否是可选任务奖励
	 * @return
	 */
	public boolean choiceAble() {
		return getTotalNum() > 0;
	}

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer(getPrizeType().getName());
		sbf.append("\n--------\n");
		sbf.append(choiceAble() ? "可选奖励个数:" : "固定奖励").append(choiceAble() ? getTotalNum() : "").append("\n");
		for (int i = 0; i < getPrizeColor().length; i++) {
			sbf.append("颜色[").append(getPrizeColor()[i]).append("]名字 [").append(getPrizeName()[i]).append("]").append(i < getPrizeColor().length - 1 ? "【或者】" : "");
		}
		return sbf.toString();
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr>");
		sbf.append("<td>");
		sbf.append(getPrizeType().getName());
		sbf.append("</td>");
		sbf.append("<td>");
		sbf.append(choiceAble() ? "可选奖励个数:" : "固定奖励").append(choiceAble() ? getTotalNum() : "");
		for (int i = 0; i < getPrizeColor().length; i++) {
			sbf.append("颜色[").append(getPrizeColor()[i]).append("]名字 [").append(getPrizeName()[i]).append("]").append(i < getPrizeColor().length - 1 ? "【或者】" : "");
		}
		sbf.append("</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskPrize prize = createTaskPrize(new String[] { "B1", "B2" }, new int[] { 1, 2 }, 1);
	}
}
