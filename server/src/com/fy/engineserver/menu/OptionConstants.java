package com.fy.engineserver.menu;

import com.fy.engineserver.datasource.language.Translate;

public interface OptionConstants {

	//服务器功能定义
	public static final int SERVER_FUNCTION_未知功能 = 0;
	
	/**
	 * 需要额外数据，
	 */
	public static final int SERVER_FUNCTION_传送到某地 = 1;
	
	public static final int SERVER_FUNCTION_疗伤 = 2;
	
	public static final int SERVER_FUNCTION_设置复活点 = 3;
	
	public static final int SERVER_FUNCTION_进入商店 = 4;
	
	public static final int SERVER_FUNCTION_修理当前装备 = 5;
	
	public static final int SERVER_FUNCTION_修理全部装备 = 6;
	
	public static final int SERVER_FUNCTION_设置回城点 = 7;
	
	public static final int SERVER_FUNCTION_打开仓库 = 8;
	
	public static final int SERVER_FUNCTION_取消 = 9;
	
	public static final int SERVER_FUNCTION_同意一起护送 = 10;
	
	public static final int SERVER_FUNCTION_丢弃物品 = 11;
	
	public static final int SERVER_FUNCTION_使用绑定 = 12;
	
	public static final int SERVER_FUNCTION_使用离线经验道具 = 13;
	
	public static final int SERVER_FUNCTION_领取物品 = 14;
	
	public static final int SERVER_FUNCTION_变卖所有白装 = 15;
	
	public static final int SERVER_FUNCTION_领取今日礼包 = 16;
	
	public static final int SERVER_FUNCTION_领取级别限制礼包 = 17;
	
	public static final int SERVER_FUNCTION_掷骰子 = 18;
	
	public static final int SERVER_FUNCTION_传送到某地有等级限制 = 19;


	public static final int SERVER_FUNCTION_传送到战场 = 20;
	

	public static final int SERVER_FUNCTION_战场复活 = 21;
	public static final int SERVER_FUNCTION_进入副本 = 22;
	
	public static final int SERVER_FUNCTION_召唤队友 = 23;
	public static final int SERVER_FUNCTION_同意副本召唤 = 24;
	public static final int SERVER_FUNCTION_重置副本进度 = 25;
	public static final int SERVER_FUNCTION_重置副本进度并且进入副本 = 26;
	public static final int SERVER_FUNCTION_获取战场列表 = 28;
	public static final int SERVER_FUNCTION_进入战场 = 29;
	
	public static final int SERVER_FUNCTION_扩充仓库 = 30;
	
	public static final int SERVER_FUNCTION_显示城市信息 = 31;
	
	public static final int SERVER_FUNCTION_JOIN_GANG_BATTLE = 32;
	
	public static final int SERVER_FUNCTION_SIGN_UP_GANG_BATTLE = 33;
	
	public static final int SERVER_FUNCTION_BATTLE_BID = 34;
	
	public static final int SERVER_FUNCTION_ENTER_BATTLE = 35;
	
	public static final int SERVER_FUNCTION_BATTLE_LIVE = 36;
	
	public static final int SERVER_FUNCTION_BATTLE_LIVE_REFLASH = 37;

	public static final int SERVER_FUNCTION_续费 = 38;
	
	public static final int SERVER_FUNCTION_OPEN_GANG_WARE_HOUSE = 39;
	
	public static final int SERVER_FUNCTION_共享任务 = 40;
	
	public static final int SERVER_FUNCTION_领取每日神奇卡 = 41;
	
	public static final int SERVER_FUNCTION_领取内测礼品 = 42;
	
	public static final int SERVER_FUNCTION_领取跑环任务 = 43;
	
	public static final int SERVER_FUNCTION_转化为10人团队 = 44;
	
	public static final int SERVER_FUNCTION_转化为5人队伍 = 45;

	public static final int SERVER_FUNCTION_使用千里追风 = 46;
	
	public static final int SERVER_FUNCTION_SHOW_WAGE = 47;

	public static final int SERVER_FUNCTION_TAKE_WAGE = 48;

	public static final int SERVER_FUNCTION_提取付费邮件 = 49;
	
	public static final int SERVER_FUNCTION_TO_WED = 50;
	
	public static final int SERVER_FUNCTION_补偿玩家 = 51;
	
	public static final int SERVER_FUNCTION_发放补偿 = 52;
	
	public static final int SERVER_FUNCTION_TO_DIVORCE = 53;
	
	public static final int SERVER_FUNCTION_CONFIRM_TO_DIVORCE = 54;

	public static final int SERVER_FUNCTION_DROP_ARTICLE = 55;
	
	public static final int SERVER_FUNCTION_CONFIRM_TO_WED = 56;
	
	public static final int SERVER_FUNCTION_REFUSE_TO_WED = 57;
	
	public static final int SERVER_FUNCTION_AGREE_DIVORCE = 58;
	
	public static final int SERVER_FUNCTION_DISAGREE_DIVORCE = 59;
	
	public static final int SERVER_FUNCTION_CONFIRM_TO_LEAVE_MASTER = 60;
	
	public static final int SERVER_FUNCTION_CONFIRM_TO_BANISH_PRENTICE = 61;
	
	public static final int SERVER_FUNCTION_AGREE_TO_TAKE_PRENTICE = 62;
	
	public static final int SERVER_FUNCTION_DISAGREE_TO_TAKE_PRENTICE = 63;
	
	public static final int SERVER_FUNCTION_AGREE_TO_BECOME_PRENTICE = 64;
	
	public static final int SERVER_FUNCTION_DISAGREE_TO_BECOME_PRENTICE = 65;
	
	public static final int SERVER_FUNCTION_增加友好度 = 66;

	public static final int SERVER_FUNCTION_绑定 = 67;

	public static final int SERVER_FUNCTION_使用夫妻传送符 = 68;

	public static final int SERVER_FUNCTION_使用挖宝物品 = 69;
	
	public static final int SERVER_FUNCTION_补偿40级公测大礼包 = 70;
	
	public static final int SERVER_FUNCTION_打开物品合成页面 = 71;
	
	public static final int SERVER_FUNCTION_打折修理当前装备 = 72;
	
	public static final int SERVER_FUNCTION_打折修理所有装备 = 73;
	
	public static final int SERVER_FUNCTION_采摘 = 74;
	
	public static final int SERVER_FUNCTION_ANSWER_QUIZ = 75;
	
	public static final int SERVER_FUNCTION_QUIT_QUIZ = 76;
	
	public static final int SERVER_FUNCTION_ENROLL_QUIZ = 77;
	
	public static final int SERVER_FUNCTION_QUIZ_REWARDS = 78;
	
	public static final int SERVER_FUNCTION_卖商店 = 79;
	
	public static final int SERVER_FUNCTION_帮战报名 = 80;
	
	public static final int SERVER_FUNCTION_帮战查看报名情况 = 81;
	
	public static final int SERVER_FUNCTION_UPGRADE_GANG_WAREHOUSE = 82;
	
	public static final int SERVER_FUNCTION_CONFIRM_TO_UPGRADE_GANG_WAREHOUSE = 83;
	
	public static final int SERVER_FUNCTION_帮派约战 = 84;
	
	public static final int SERVER_FUNCTION_查询帮派约战 = 85;
	
	public static final int SERVER_FUNCTION_帮派约战_搜索帮派 = 86;
	
	public static final int SERVER_FUNCTION_帮派约战_选择帮派 = 87;
	
	public static final int SERVER_FUNCTION_帮派约战_输入赌注 = 88;
	
	public static final int SERVER_FUNCTION_帮派约战_是否确认 = 89;
	
	public static final int SERVER_FUNCTION_CONFIRM_TO_QUIT_GANG = 90;
	
	public static final int SERVER_FUNCTION_创建家园 = 91;
	
	public static final int SERVER_FUNCTION_创建家园_确认 = 92;
	
	public static final int SERVER_FUNCTION_进入家园 = 93;
	
	public static final int SERVER_FUNCTION_升级家园 = 94;
	
	public static final int SERVER_FUNCTION_升级家园_确认 = 95;

	public static final int SERVER_FUNCTION_家园等级打折修理当前装备 = 96;
	
	public static final int SERVER_FUNCTION_家园等级打折修理所有装备 = 97;
	
	public static final int SERVER_FUNCTION_帮主给所有人加BUFF = 98;
	
	public static final int SERVER_FUNCTION_CONFIRM_TO_KICKOUT_GANG_MEMBER = 99;
	
	public static final int SERVER_FUNCTION_使用触发任务道具 = 100;
	
	public static final int SERVER_FUNCTION_领取金翎奖 = 101;
	
	public static final int SERVER_FUNCTION_OPEN_MASTERS_LIST = 102;
	
	public static final int SERVER_FUNCTION_OPEN_PRENTICES_LIST = 103;
	
	public static final int SERVER_FUNCTION_ADD_MASTERS_LIST = 104;
	
	public static final int SERVER_FUNCTION_ADD_PRENTICES_LIST = 105;
	
	public static final int SERVER_FUNCTION_MASTERS_BOARD_NEXT_PAGE = 106;
	
	public static final int SERVER_FUNCTION_MASTERS_BOARD_NAMES = 107;
	
	public static final int SERVER_FUNCTION_QUIT_MASTERS_BOARD = 108;
	
	public static final int SERVER_FUNCTION_QUERY_MASTER_FUNDS = 109;
	
	public static final int SERVER_FUNCTION_排队进入副本选择副本 = 110;
	
	public static final int SERVER_FUNCTION_排队进入副本选择角色 = 111;
	
	public static final int SERVER_FUNCTION_排队完成进入副本 = 112;
	
	public static final int SERVER_FUNCTION_排队副本列表 = 113;
	
	public static final int SERVER_FUNCTION_AUTO_ALLOCATE_SKILL_POINTS = 114;
	
	public static final int SERVER_FUNCTION_领取当乐礼品 = 115;

	public static final int SERVER_FUNCTION_排队进入勇士副本选择副本 = 116;
	
	public static final int SERVER_FUNCTION_排队勇士副本列表 = 117;
	
	public static final int SERVER_FUNCTION_排队进入随机副本选择角色 = 118;
	
	public static final int SERVER_FUNCTION_查询帮会积分 = 119;
	
	public static final int SERVER_FUNCTION_查询帮会积分排行 = 120;
	
	public static final int SERVER_FUNCTION_BATTLE_LIST = 121;
	
	public static final int SERVER_FUNCTION_战场排队 = 122;
	
	public static final int SERVER_FUNCTION_领取帮会活动新手礼品 = 123;
	
	public static final int SERVER_FUNCTION_QUERY_CHARGE_POINTS = 124;
	
	public static final int SERVER_FUNCTION_CONFIRM_TASK_REWARDS = 125;
	
	public static final int SERVER_FUNCTION_CONFIRM_UPGRADE_EQUIPMENT = 126;
	
	public static final int SERVER_FUNCTION_ENTER_CROSS_MAP = 127;

	public static final int SERVER_FUNCTION_PLAYER_RENAME = 128;
	
	public static final int SERVER_FUNCTION_GANG_RENAME = 129;
	
	public static final int SERVER_FUNCTION_领取角色 = 130;
	
	public static final int SERVER_FUNCTION_PLAYER_RENAME_INPUT = 131;
	
	public static final int SERVER_FUNCTION_PLAYER_RENAME_CONFIRM = 132;
	
	public static final int SERVER_FUNCTION_GANG_RENAME_INPUT = 133;
	
	public static final int SERVER_FUNCTION_GANG_RENAME_CONFIRM = 134;
	
	public static final int SERVER_FUNCTION_OLD_SERVER_COMPENSATE = 135;
	
	public static final int SERVER_FUNCTION_CONSOLIDATE_SERVER_COMPENSATE = 136;
	
	public static final int SERVER_FUNCTION_RESET_ADVANCED_PASSWORD = 137;
	
	public static final int SERVER_FUNCTION_INPUT_ADVANCED_PASSWORD = 138;
	
	public static final int SERVER_FUNCTION_INPUT_NEW_ADVANCED_PASSWORD = 139;
	
	public static final int SERVER_FUNCTION_LOCK_PLAYER = 140;
	
	public static final int SERVER_FUNCTION_TRANSFER_CHARGE_POINTS = 141;
	
	public static final int SERVER_FUNCTION_TRANSFER_CHARGE_POINTS_INPUT_NAME = 142;
	
	public static final int SERVER_FUNCTION_TRANSFER_CHARGE_POINTS_INPUT_AMOUNT = 143;
	
	public static final int SERVER_FUNCTION_UNIQUE_GIFT = 144;
	
	public static final int SERVER_FUNCTION_UNIQUE_GIFT_INPUT = 145;
	
	public static final int SERVER_FUNCTION_TRANSFER_CHARGE_POINTS_CONFIRM = 146;
	
	public static final int SERVER_FUNCTION_SPREADER_NUMBER = 147;
	
	public static final int SERVER_FUNCTION_SEND_MAIL_CONFIRM = 148;
	
	public static final int SERVER_FUNCTION_ENTER_CROSS_SERVER_QICUO = 149;
	
	public static final int SERVER_FUNCTION_ENTER_CROSS_SERVER_CAMP_DEATH_MATCH = 150;
	
	public static final int SERVER_FUNCTION_LEAVE_CROSS_SERVER = 151;
	
	public static final int SERVER_FUNCTION_QUERY_CURRENT_DAY_ONLINE_TIME = 152;
	
	public static final int SERVER_FUNCTION_SIGN_UP_比武 = 153;
	
	public static final int SERVER_FUNCTION_比武信息查询 = 154;
	
	public static final int SERVER_FUNCTION_比武信息查询_IN_ONE_PAGE = 155;
	
	public static final int SERVER_FUNCTION_比武进比武场 = 156;
	
	public static final int SERVER_FUNCTION_QUERY_SYSTEM_ANNOUNCEMENT = 157;
	
	public static final int SERVER_FUNCTION_QUIT_DUEL_FIELD = 158;
	
	public static final int SERVER_FUNCTION_TAKE_BIWU_LOTTERY_REWARDS = 159;
	
	public static final int SERVER_FUNCTION_BIWU_BALLOT_SEARCH = 160;
	
	public static final int SERVER_FUNCTION_BIWU_BALLOT = 161;
	
	public static final int SERVER_FUNCTION_BIWU_BALLOT_LIST_PAGE = 162;
	
	public static final int SERVER_FUNCTION_BIWU_BALLOT_SHOW_PLAYER_INFO = 163;
	
	public static final int SERVER_FUNCTION_RESET_MAGIC_WEAPON = 164;
	
	public static final int SERVER_FUNCTION_CREATE_JIAZU=165;
	public static final int SERVER_FUNCTION_APPLY_JIAZU=166;
	public static final int SERVER_FUNCTION_LEAVE_JIAZU=167;
	public static final int SERVER_FUNCTION_CONFIRM_APPLY_JIAZU_BASE=168;
	public static final int SERVER_FUNCTION_CONFIRM_APPLY_MASTER=169;
	public static final int SERVER_FUNCTION_CONFIRM_DISMISS_JIAZU=170;
	public static final int SERVER_FUNCTION_CONFIRM_SETICON_JIAZU=171;
	public static final int SERVER_FUNCTION_CONFIRM_SALARY_CEREMONY_JIAZU=172;
	
	public static final int SERVER_FUNCTION_TUOYUN = 173;
	
	public static final int SERVER_FUNCTION_QIUJIN = 174;
	
	
	public static final int SERVER_FUNCTION_结义 = 23;
	
	public static final int SERVER_FUNCTION_加入聊天分组申请 = 176;
	public static final int SERVER_FUNCTION_系统发送结婚通知 = 177;
	
	public static final int SERVER_FUNCTION_OPEN_CANGKU = 178;
	
	public static final int SERVER_FUNCTION_师徒 = 179;
	
	public static final int SERVER_FUNCTION_领取奖励 = 180;
	
	public static final int SERVER_FUNCTION_永久公告 = 181;
	
	public static final int SERVER_FUNCTION_永久公告_TITLE = 182;
	
	public static final int SERVER_FUNCTION_永久公告_CONTENT = 183;
	

	public static final String [] SERVER_FUNCTION_NAMES = new String[]{
		Translate.text_4821,Translate.text_4822,Translate.text_4823,Translate.text_4824,Translate.text_4825,Translate.text_4826,Translate.text_4827,Translate.text_4828,Translate.text_4829
		,Translate.text_364,Translate.text_4830,Translate.text_4831,Translate.text_4832,Translate.text_4833 , Translate.text_4834 , Translate.text_4835,
		"","",

		Translate.text_4836 , Translate.text_4837,Translate.text_215,Translate.text_4838 , Translate.text_4682 , Translate.text_4839 , Translate.text_4840,
		Translate.text_4841 , Translate.text_4842 , Translate.text_4843 , Translate.text_215 , Translate.text_4126,

		Translate.text_4844,Translate.text_4845,Translate.text_4846,Translate.text_4847,Translate.text_215,Translate.text_4848,Translate.text_4849,Translate.text_68,Translate.text_4850,Translate.text_673

		, Translate.text_4851 , Translate.text_4852,Translate.text_4853,Translate.text_4854,Translate.text_4855,Translate.text_3503,Translate.text_4856,Translate.text_4857,"",Translate.text_3483,Translate.text_4150,Translate.text_4858,Translate.text_4859,
		Translate.text_4728,Translate.text_4860,Translate.text_4723,Translate.text_4861,Translate.text_4862,Translate.text_4863,Translate.text_317,Translate.text_4791,Translate.text_4795,Translate.text_4864,Translate.text_4797,Translate.text_4865,Translate.text_3694,Translate.text_472,Translate.text_4866,Translate.text_4867,
		Translate.text_4868,Translate.text_4869,Translate.text_4870,Translate.text_4871,Translate.text_3086,Translate.text_4872,Translate.text_4873,Translate.text_4874,Translate.text_4875,
		Translate.text_4876,Translate.text_4877,Translate.text_4878,Translate.text_4879,Translate.text_4880,
		Translate.text_4881,Translate.text_4882,Translate.text_4883,Translate.text_4884,Translate.text_4885,Translate.text_4886,Translate.text_4887,Translate.text_4888,Translate.text_4889,Translate.text_4890,Translate.text_4891,Translate.text_4892,
		Translate.text_4893,Translate.text_4894,Translate.text_4895,Translate.text_4896,Translate.text_4897,Translate.text_4898,Translate.text_4899,Translate.text_4900,Translate.text_4901,Translate.text_4902,Translate.text_4903,Translate.text_4904
		,Translate.text_4905,Translate.text_4906, Translate.text_4907, Translate.text_4908,Translate.text_4909,Translate.text_4910,Translate.text_4911,Translate.text_4912,Translate.text_4913,Translate.text_4914,Translate.text_4915,
		Translate.text_4916,Translate.text_4917,Translate.text_4918,Translate.text_217,Translate.text_4919,Translate.text_4920,Translate.text_4921,Translate.text_483,Translate.text_4922,Translate.text_4923,Translate.text_4924,Translate.text_4925,Translate.text_4926,Translate.text_4927,Translate.text_4928,Translate.text_4929,Translate.text_4930,Translate.text_4931
		,Translate.text_4932,Translate.text_4933,Translate.text_4934,Translate.text_4935,Translate.text_4135,Translate.text_4936,Translate.text_4937,Translate.text_4938,Translate.text_4939,Translate.text_4940
		,Translate.text_4941,Translate.text_4942,Translate.text_4943,Translate.text_4944,Translate.text_4945,Translate.text_4946,Translate.text_4947,Translate.text_4948,Translate.text_4949,Translate.text_4950,Translate.text_4951,Translate.text_4952
		,Translate.text_4953,Translate.text_4954,Translate.text_4955,Translate.text_4956,Translate.text_4957,Translate.text_72,Translate.打开仓库,Translate.永久公告类型,Translate.永久公告类型标题,Translate.永久公告类型内容
	};
	
	//客户端功能定义
	public static final int CLIENT_FUNCTION_CLOSE = 0;
	
	public static final int CLIENT_FUNCTION_AUCTION = 3;
	
	public static final int CLIENT_FUNCTION_REQUESTBUY = 4;
	
	public static final int CLIENT_FUNCTION_MARRIAGE = 22;			//结婚

	public static final int CLIENT_FUNCTION_ZONGPAI_DISMISS = 23;			//宗派解散
	
	public static final int CLIENT_FUNCTION_ARTICLEPROTECT = 24;			//锁魂
	public static final int CLIENT_FUNCTION_QUIT_CROSS_SERVER= 25;			//跨服

	
	public static final String [] CLIENT_FUNCTION_NAMES = new String[]{
		Translate.text_4821,Translate.text_4958,Translate.text_483,Translate.text_154,Translate.text_4959 , Translate.text_484, Translate.text_4960 , Translate.text_1582 , Translate.text_4961 , Translate.text_4962
	};
	

	 //元宝充值
	 int YUANBAOCHONGZHI = -100;
	 //热门抢购
	 int REMENQIANGGOU = -101;
	 //生活必备
	 int SHENGHUOBIBEI = -102;
	 //装备升级
	 int ZHUANGBEISHENGJI = -103;
	 //材料商店
	 int CAILIAOSHANGDIAN = -104;
	 //法宝商店
	 int MAGIC_WEAPON_SHANGDIAN = -105;
	 //工资商店
	 int GONGZISHANGDIAN = -106;

	 
	public static final int 客户端异宝仙阁菜单对应的标识[] = new int[]{-100,-101,-102,-103,-104,MAGIC_WEAPON_SHANGDIAN,-106};
	
	public static final int 客户端异宝仙阁菜单对应的窗口[] = new int[]{39,40,41,42,43,44,45};
}
