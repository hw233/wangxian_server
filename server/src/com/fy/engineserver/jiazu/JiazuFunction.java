package com.fy.engineserver.jiazu;

/**
 * 家族功能 ：动作
 * 
 * 
 */
public enum JiazuFunction {
	/**
	 * 禅让
	 */
	resign("禅让"),
	/**
	 * 解散家族
	 */
	disband("解散家族"),
	/**
	 * 任命官员
	 */
	nominate("任命官员"),
	/**
	 * 驱除成员
	 */
	expel("驱除成员"),
	/**
	 * 同意/拒绝家族成员
	 */
	handle_join_request("同意/拒绝家族成员"),
	/**
	 * 升级家族为1级家族
	 */
	upgrade_jiazu("升级家族为1级家族"),
	/**
	 * 升级/修复 建筑
	 */
	upgrade_or_repair_construction("升级/修复 建筑"),
	/**
	 * 降级/毁灭 建筑
	 */
	downgrade_or_destroy_construction("降级/毁灭 建筑"),
	/**
	 * 开启/取消研究
	 */
	setup_or_abandon_research("开启/取消研究"),
	/**
	 * 发布商业任务
	 */
	publish_business_task("发布商业任务"),
	/**
	 * 发布建设任务
	 */
	publish_construct_task("发布建设任务"),
	/**
	 * 更改级别称号
	 */
	set_rank_name("更改级别称号"),
	/**
	 * 修改徽章
	 */
	set_icon("修改徽章"),
	/**
	 * 发送工资
	 */
	pay_salary("发送工资"),
	/**
	 * 开启商业路线
	 */
	setup_or_abandon_business_line("开启商业路线"),
	/**
	 * 开启运镖
	 */
	setup_transfer_goods("开启运镖"),
	/**
	 * 开启家族篝火
	 */
	setup_fire("开启家族篝火"),

	/**
	 * 开启家族孕育灵脉
	 */
	setup_construction_consume("开启家族孕育灵脉"),
	/**
	 * 种植家族祝福果
	 */
	plant_blessing_fruit("种植家族祝福果"),
	/**
	 * 召集家族成员
	 */
	summon_member("召集家族成员"),
	/**
	 * 使用家族令
	 */
	use_jiazu_token("使用家族驻地申请函"),
	/**
	 * 发起家族村庄占领
	 */
	setup_fight_against_village("发起家族村庄占领"),
	/**
	 * 发起家族战
	 */
	setup_fight_againt_jiazu("发起家族战"),
	/**
	 * 自由接取商人任务
	 */
	receive_business_task("自由接取商人任务"),
	/**
	 * 申请为族长
	 */
	request_master("申请为族长"),
	/**
	 * 离开家族
	 */
	leave_jiazu("离开家族"),
	/**
	 * 接收工资
	 */
	receive_salary("接收工资"),

	/**
	 * 修改家族徽章
	 */
	modify_slogan("修改家族徽章"),

	/**
	 * 发布养蚕活动
	 */
	release_silkworm("发布养蚕活动"),
	/**
	 * 发布祝福果活动
	 */
	release_forluck("发布祝福果活动"),
	/**
	 * 家族运镖
	 */
	release_silvercar("家族运镖"),
	/**
	 * 家族召唤
	 */
	release_callIn("家族召唤"),
	/**
	 * 发布家族标志像BUFF
	 */
	add_buff("发布家族标志像BUFF"),
	/**
	 * 召唤BOSS
	 */
	call_BOSS("召唤BOSS"), 
	/**
	 * 强化镖车
	 */
	stronger_BIAOCHE("强化镖车"), 
	/**
	 * 解封家族
	 * @param name
	 */
	release_JIAZU("解封家族");
	;

	private JiazuFunction(String name) {
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
