package com.fy.engineserver.jiazu2.manager;

public enum JiaZuLivenessType {
	家族任务(0.2f),
	家族押镖_白(5f),
	家族押镖_绿(10f),
	家族押镖_蓝(15f),
	家族押镖_紫(20f),
	家族押镖_橙(25f),
	家族远征_击杀(50f),
	家族远征_击退(25f),
	灵矿争夺_蓝矿(5f),
	灵矿争夺_紫矿(10f),
	灵矿争夺_橙矿(20f),
	家族上香_中档(1f),
	家族上香_高档(3f),
	世界boss_排名1(50f),
	世界boss_排名2(30f),
	世界boss_排名3(20f),
	世界boss_排名4_5(15f),
	世界boss_排名6_10(10f),
	世界boss_排名11_20(5f),
	世界boss_每个参与人员(0.5f);
	private float score;
	private JiaZuLivenessType(float s){
		score = s;
	}
	public float getScore() {
		return score;
	}
	
}
