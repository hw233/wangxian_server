package com.fy.engineserver.jiazu2.manager;

import java.util.Arrays;

public class BossConfig {

	private int level;
	private int id;
	private long hp;
	private String [] kill_reward1_10 = new String[]{};
	private String [] run_reward1_10 = new String[]{};
	private String kill_other_reward;
	private String run_other_reward;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getHp() {
		return hp;
	}
	public void setHp(long hp) {
		this.hp = hp;
	}
	public String[] getKill_reward1_10() {
		return kill_reward1_10;
	}
	public void setKill_reward1_10(String[] killReward1_10) {
		kill_reward1_10 = killReward1_10;
	}
	public String[] getRun_reward1_10() {
		return run_reward1_10;
	}
	public void setRun_reward1_10(String[] runReward1_10) {
		run_reward1_10 = runReward1_10;
	}
	public String getKill_other_reward() {
		return kill_other_reward;
	}
	public void setKill_other_reward(String killOtherReward) {
		kill_other_reward = killOtherReward;
	}
	public String getRun_other_reward() {
		return run_other_reward;
	}
	public void setRun_other_reward(String runOtherReward) {
		run_other_reward = runOtherReward;
	}
	@Override
	public String toString() {
		return "[hp=" + hp + ", id=" + id + ", kill_other_reward="
				+ kill_other_reward + ", kill_reward1_10="
				+ Arrays.toString(kill_reward1_10) + ", level=" + level
				+ ", run_other_reward=" + run_other_reward
				+ ", run_reward1_10=" + Arrays.toString(run_reward1_10) + "]";
	}
	
}
