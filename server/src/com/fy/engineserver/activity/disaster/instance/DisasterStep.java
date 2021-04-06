package com.fy.engineserver.activity.disaster.instance;

import com.fy.engineserver.activity.disaster.step.AbstractStep;
import com.fy.engineserver.activity.disaster.step.FirstStep;
import com.fy.engineserver.activity.disaster.step.SecondStep;
import com.fy.engineserver.activity.disaster.step.ThirdStep;
import com.fy.engineserver.core.Game;
/**
 * 金猴天灾活动阶段
 */
public enum DisasterStep {
	STEP_THIRD(0, 60000, null, new ThirdStep(), true),
	STEP_SECOND(0, 120000, DisasterStep.STEP_THIRD, new SecondStep(), true),
	STEP_FIRST(0, 120000, DisasterStep.STEP_SECOND, new FirstStep()),
	STEP_READY(0, 40000, DisasterStep.STEP_FIRST, null, false, true),
	
	;
	private int setp;		//0准备  1-3为第一到第三阶段
	private long durationTime;		//当前阶段持续时间
	private DisasterStep nextStep;
	private AbstractStep stepReal;//阶段需要实现的功能封装
	private boolean needRefreshBox = false;
	private boolean canEnter = false;
	
	private DisasterStep(int setp, long durationTime, DisasterStep nextStep, AbstractStep stepReal) {
		this.setp = setp;
		this.durationTime = durationTime;
		this.nextStep = nextStep;
		this.stepReal = stepReal;
	}
	private DisasterStep(int setp, long durationTime, DisasterStep nextStep, AbstractStep stepReal, boolean needRefreshBox) {
		this.setp = setp;
		this.durationTime = durationTime;
		this.nextStep = nextStep;
		this.stepReal = stepReal;
		this.needRefreshBox = needRefreshBox;
	}
	private DisasterStep(int setp, long durationTime, DisasterStep nextStep, AbstractStep stepReal, boolean needRefreshBox, boolean canEnter) {
		this.setp = setp;
		this.durationTime = durationTime;
		this.nextStep = nextStep;
		this.stepReal = stepReal;
		this.needRefreshBox = needRefreshBox;
		this.canEnter = canEnter;
	}

	/**
	 * 刷出火圈NPC
	 * @param game
	 */
	public void refreshNPC(Game game) {
		if (stepReal != null) {
			game.removeAllFireNpc();
			stepReal.refreshNPC(game);
		}
	}
	
	public int getSetp() {
		return setp;
	}
	public void setSetp(int setp) {
		this.setp = setp;
	}
	public DisasterStep getNextStep() {
		return nextStep;
	}
	public void setNextStep(DisasterStep nextStep) {
		this.nextStep = nextStep;
	}
	public AbstractStep getStepReal() {
		return stepReal;
	}
	public void setStepReal(AbstractStep stepReal) {
		this.stepReal = stepReal;
	}
	public long getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(long durationTime) {
		this.durationTime = durationTime;
	}

	public boolean isNeedRefreshBox() {
		return needRefreshBox;
	}

	public void setNeedRefreshBox(boolean needRefreshBox) {
		this.needRefreshBox = needRefreshBox;
	}
	public boolean isCanEnter() {
		return canEnter;
	}
	public void setCanEnter(boolean canEnter) {
		this.canEnter = canEnter;
	}
}
