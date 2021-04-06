package com.fy.engineserver.activity.activeness;

public enum ActivenessType {
	寻宝(1, "寻宝"),
	偷砖(2, "偷砖"),
	刺探(3, "刺探"),
	斩妖降魔(4, "斩妖降魔"),
	幽冥幻域(5, "幽冥幻域"),
	福地洞天(6, "福地洞天"),
	灵兽岛(7, "灵兽岛"),
	封魔录(8, "封魔录"),
	聚灵阵(9, "聚灵阵"),
	异兽饲养(10, "异兽饲养"),
	家族押镖(11, "家族押镖"),
	每日登陆(12, "每日登陆"),
	仙缘论道(13, "仙缘论道"),
	传功授业(14, "传功授业"),
	采花大盗(15, "采花大盗"),
	挖宝(16, "挖宝"),
	神农(17, "神农"),
	答题(18, "答题"),
	武圣争夺战(19, "武圣争夺战"),
	通关本尊副本(20, "通关本尊副本"),
	通关元神副本(21, "通关元神副本"),
	平定四方(22, "平定四方"),
	个人押镖(23, "个人押镖"),
	灵矿争夺(25, "灵矿争夺"),
	城市争夺战(26, "城市争夺战"),
	商城购买道具(27, "商城购买道具"),
	强化装备(28, "强化装备"),
	强化宠物(29, "强化宠物"),
	圣兽阁(30, "圣兽阁"),
	彩世喊话(31, "彩世喊话"),
	原地复活(32, "原地复活"),
	宠物进阶(33, "宠物进阶"),
	祈福(34, "祈福"),
	击杀世界BOSS(35, "击杀世界BOSS"),
	偷紫色果实(36, "偷紫色果实"),
	放生千载难逢宝宝(37, "放生千载难逢宝宝"),
	每日签到(38, "每日签到");

	private int type;
	private String name;
	private ActivenessType(int type, String name) {
		this.type = type;
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
