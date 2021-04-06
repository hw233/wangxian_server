package com.fy.engineserver.newtask.service;

import static com.fy.engineserver.datasource.language.Translate.*;

public interface TaskConfig {

	// 角色携带的任务上限
	int MAX_TASK_NUM = 25;

	// 跑环任务放弃惩罚时间
	long LOOPTASK_GIVEUP_PUNISH_TIME = 1000 * 5l;

	int noticeColor = 0xFF7200;

	// 任务类型
	/** 单次任务 */
	int TASK_TYPE_ONCE = 0;
	/** 日常任务 */
	int TASK_TYPE_DAILY = 1;
	/** 跑环任务 */
	int TASK_TYPE_LOOP = 2;
	// 任务显示类型
	/** 主线 */
	byte TASK_SHOW_TYPE_MAIN = 0;
	/** 支线 */
	byte TASK_SHOW_TYPE_SUB = 1;
	/** 境界 */
	byte TASK_SHOW_TYPE_BOURN = 2;
	/** 日常 */
	byte TASK_SHOW_TYPE_DAILY = 3;
	/** 其他 */
	byte TASK_SHOW_TYPE_OTHER = 4;

	String[] SHOW_NAMES = { "主线", "支线", "境界", "日常", "其他" };
	// 性别配置
	/** 性别不限 */
	int SEX_NON = -1;
	/** 男性角色only */
	int SEX_MALE = 0;
	/** 女性角色only */
	int SEX_FEMALE = 1;
	// 职业限制
	/** 职业限制 - 无限制 */
	int WORK_NON = -1;
	int WORK_A = 0;
	int WORK_B = 1;
	int WORK_C = 2;
	int WORK_D = 3;

	// 国家限制
	/** 国家限制-无限制 */
	int COUNTRY_NON = -1;
	int COUNTRY_A = 0;
	int COUNTRY_B = 1;
	int COUNTRY_C = 2;

	// 任务依赖关系类型
	/** 任务依赖关系-上一组完成一个 */
	int DEPEND_TYPE_ONE = 0;
	/** 任务依赖关系-上一组完成所有 */
	int DEPEND_TYPE_ALL = 1;

	// 限时任务类型
	/** 限时完成任务类型:自动完成 */
	int DEILVER_LIMIT_TYPE_OVER = 0;
	/** 限时完成任务类型:需要交付 */
	int DEILVER_LIMIT_TYPE_DELIVER = 1;
	/** 限时完成任务类型:自动完成且自动计算经验 */
	int DEILVER_LIMIT_TYPE_AUTO_DELIVER = 2;

	// 任务状态
	/** 任务状态-从未接取 */
	int TASK_STATUS_NEVER = -1;
	/** 任务状态-未领取[对于单次接则表示未接取,对于可重复接的任务表示已经完成] */
	int TASK_STATUS_NONE = 0;
	/** 任务状态-已接 */
	int TASK_STATUS_GET = 1;
	/** 任务状态-已达成 */
	int TASK_STATUS_COMPLETE = 2;
	/** 任务状态-已交付 */
	int TASK_STATUS_DEILVER = 3;
	/** 任务状态-已放弃 */
	int TASK_STATUS_GIVEUP = 4;
	/** 任务状态-已失败 */
	int TASK_STATUS_FAILED = 5;

	String[] taskStatus = { "未接取", "已接取", "已达成", "已交付", "已放弃", "已失败" };

	// 任务经验奖励类型
	/** 默认奖励方式--按配置奖励 */
	int EXP_PRIZE_DEFAULT = 0;
	/** 经验公式1 */
	int EXP_PRIZE_EXPRESSION1 = 1;
	/** 经验公式2 */
	int EXP_PRIZE_EXPRESSION2 = 2;
	/** 经验公式3 */
	int EXP_PRIZE_EXPRESSION3 = 3;

	// 玩家任务操作 客户端动作类型，0标识请求任务实体信息，1标识新接了一个任务，2标识交付了一个任务，3标识放弃了一个任务
	byte TASK_ACTION_QRY = 0;
	byte TASK_ACTION_ACCEPT = 1;
	byte TASK_ACTION_DELIVER = 2;
	byte TASK_ACTION_GIVEUP = 3;

	String[] TASK_ACTION_ARR = { "查询", "接取", "完成", "放弃" };
	// 接取任务实现限制类型
	/** 按天 */
	int TASK_TIME_LIMIT_DAY = 0;
	/** 按周 */
	int TASK_TIME_LIMIT_WEEK = 1;
	/** 按月 */
	int TASK_TIME_LIMIT_MONTH = 2;
	/** 时间段(每天时间段) */
	int TASK_TIME_LIMIT_INTERZONE_DAILY = 3;
	/** 时间段(时间区间) */
	int TASK_TIME_LIMIT_TIME_INTERZONE = 4;

	// 杀人任务 国家限制
	/** 不限制国籍 */
	int KILL_PLAYER_COUNTRY_LIMIT_NON = -1;
	/** 非本国家 */
	int KILL_PLAYER_COUNTRY_LIMIT_OTHER = 0;

	// 随机任务奖励 奖励个数方式
	/** 不随机个数 奖励全部 */
	int PRIZE_NUM_ALL = 0;
	/** 随机个数 */
	int PRIZE_NUM_RANDOM = 1;

	// 是否计算积分
	/** 不计算积分 */
	int TASK_COUNT_SCORE_NO = 0;
	/** 计算积分 */
	int TASK_COUNT_SCORE_YES = 1;

	enum TargetType {
		MONSTER((byte) 1, "杀确定的怪", true),
		MONSTER_LV((byte) 2, "杀某等级条件的怪", true),
		MONSTER_LV_PLAYER((byte) 3, "和人物等级段相关的怪", true),
		GET_ARTICLE((byte) 4, "得到物品"),
		USE_ARTICLE((byte) 5, "使用道具"),
		KILL_PLAYER((byte) 6, "杀人"),
		CHARGE((byte) 7, "充值"),
		DISCOVERY((byte) 8, "探索"),
		TASK_DELIVER((byte) 9, "其他任务完成"),
		CONVOY((byte) 10, "护送"),
		RELATION((byte) 11, "人际关系"),
		TALK_NPC((byte) 12, "谈话"),
		MONSTER_RANDOM_NUM((byte) 13, "杀随机数量的指定怪"),
		COLLECTION((byte) 14, "采集物品"),
		BUFF((byte) 15, "获得BUFF"),
		CAVE_BUILD((byte) 16, "建造庄园建筑"),
		CAVE_PLANT((byte) 17, "庄园种植"),
		CAVE_STEAL((byte) 18, "偷取果实"),
		CAVE_HARVEST((byte) 19, "收获果实"),
		GET_ARTICLE_AND_DELETE((byte) 20, "得到物品,且交付时扣除"), ;

		TargetType(byte index, String name) {
			this.index = index;
			this.name = name;

		}

		TargetType(byte index, String name, boolean bringExcess) {
			this(index, name);
			this.bringExcess = bringExcess;

		}

		/** 索引,唯一 ，后台配置用 */
		private byte index;
		/** 类型名 */
		private String name;
		/** 是否触发额外目标 */
		private boolean bringExcess;

		public byte getIndex() {
			return index;
		}

		public void setIndex(byte index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isBringExcess() {
			return bringExcess;
		}

		public void setBringExcess(boolean bringExcess) {
			this.bringExcess = bringExcess;
		}
	}

	enum PrizeType {
		EXP((byte) 1, 任务奖励_角色经验),
		ARTICLE((byte) 2, 任务奖励_物品),
		BIND_SILVER((byte) 3, 任务奖励_绑银),
		MONEY2((byte) 4, 任务奖励_金钱2),
		MONEY3((byte) 5, 任务奖励_金钱3),
		BUFF((byte) 6, 任务奖励_BUFF),
		TITLE((byte) 7, 任务奖励_称号),
		PRESTIGE((byte) 8, 任务奖励_声望),
		PNEUMA((byte) 9, 任务奖励_修法值),
		CONTRIBUTE((byte) 10, 任务奖励_贡献度),
		SKILL_POINT((byte) 11, 任务奖励_技能点),
		RANDOM_ARTICLE((byte) 12, 任务奖励_随机获得物品奖励),
		CLASSLEVEL((byte) 13, 任务奖励_灵气值),
		CAREER_ARTICLE((byte) 14, 任务奖励_分职业给予奖励),
		GONGXUN((byte) 15, 任务奖励_功勋),
		LILIAN((byte) 16, 任务奖励_历练),
		COUNTRY_RES((byte) 17, 任务奖励_国家资源), ;

		PrizeType(byte index, String name) {
			this.index = index;
			this.name = name;
		}

		private byte index;
		private String name;

		public byte getIndex() {
			return index;
		}

		public void setIndex(byte index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	// 影响任务接取的动作
	enum ModifyType {
		GRADE_UP(0, "等级提高", true),
		THEW_DOWN(1, "体力值减少", false),
		THEW_UP(2, "体力值增加", true),
		COUNTRY_OFFICIAL_DOWN(3, "国家职务下降", false),
		COUNTRY_OFFICIAL_UP(4, "国家职务提升", true),
		BOURN_UP(5, "境界提高", true),
		MARRIAGE_BREAK(6, "离婚", false),
		MARRIAGE_ADD(7, "结婚", true),
		TEACHE_SHIP_BREAK(8, "解除师徒关系", false),
		TEACHE_SHIP_ADD(9, "获得师徒关系", true),
		BROTHER_SHIP_BREAK(10, "解除结义关系", false),
		BROTHER_SHIP_ADD(11, "获得结义关系", true),
		TASK_DELIVER(12, "完成任务", true),
		TASK_GIVEUP(13, "放弃任务", true),
		IN_SEAL(14, "获得封印", true),
		UN_SEAL(15, "解除封印", false),
		GET_COLLECTION(16, "接受集合任务", true),
		DELIVER_COLLECTION(17, "完成集合任务", true),
		GET_GROUP(18, "接受组任务", true),
		DELIVER_GROUP(19, "完成组任务", true),
		JIAZU_GOT(20, "拥有家族", true),
		JIAZU_LOST(21, "失去家族", false),
		JIAZU_TITLE_MODIFY(22, "家族职务变化", true),
		JIAZU_TITLE_MODIFY_CURRENT(23, "家族职务变化后清除身上当前可接任务", false), 
		DAY_CHANGE(24, "跨天可接取", true),
		DAY_CHANGE_MODIFY_CURRENT(25, "跨天不可接取", false),;

		// 类型
		private int type;
		// 名字
		private String name;
		// 是否引起任务增多， 我们认为: 等级提高 境界提高 和达成社会关系可能增加任务数量 ,否则可能减少 ,即:不存在需要无某种社会关系的任务存在
		// 其实increase=false目前只需要一个,现在这样做是为了以后可能的优化
		private boolean increase;

		ModifyType(int type, String name, boolean increase) {
			this.type = type;
			this.name = name;
			this.increase = increase;

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

		public boolean isIncrease() {
			return increase;
		}

		public void setIncrease(boolean increase) {
			this.increase = increase;
		}
	}
}
