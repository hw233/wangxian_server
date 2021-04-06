package com.fy.engineserver.economic;

import com.fy.engineserver.datasource.language.Translate;

public class SavingReasonType {
	public static final int SELL_ARTICLE = 0;
	
	public static final int MAIL_GET = 1;
	
	public static final int DEAL_GET = 2;
	
	public static final int AUCTION_GET = 3;

	public static final int USE_PROP_GET = 4;
	
	public static final int YD_SAVING = 5;
	
	public static final int LT_SAVING = 6;
	
	public static final int DX_SAVING = 7;
	
	public static final int EXCHANGE = 8;
	
	public static final int MONSTER_FLOP = 9;
	
	public static final int TASK_REWARD = 10;
	
	public static final int MAIL_EXPENSE = 11;
	
	public static final int VAS_SAVING = 12;
	
	public static final int BANK_CARD_SAVING = 13;
	
	public static final int GANG_CONTRIBUTE_FUND = 14;
	
	public static final int GANG_BIDDING_FAIL = 15;
	
	public static final int WEEKLY_WAGE = 16;
	
	public static final int MAIL_SEND_FAIL_RETURN_MONEY = 17;
	
	public static final int CMCC_GAME_POINT = 18;
	
	public static final int COMPENSATE_PLAYER = 19;
	
	public static final int BATTLE_FIELD_REWARD = 20;
	
	public static final int QUIZ_REWARD = 21;
	
	public static final int RETURN_PLAYER_PRESENT = 22;
	
	public static final int PRENTICE_SAVING = 23;
	
	public static final int PRENTICE_UPGRADE = 24;
	
	public static final int FINISH_APPRENTICESHIP = 25;
	
	public static final int PRENTICE_PICKUP = 26;
	
	public static final int USE_HONOR_FRUIT = 27;
	
	public static final int GANG_BATTLE_FIELD_REWARD = 28;
	
	public static final int CHARGE_BACK_TIME=29;
	
	public static final int CHARGE_BACK_UPGRADE=30;
	
	public static final int CHARGE=31;
	
	public static final int CHARGE_POINTS_TRANSFER=32;
	
	public static final int BIWU=33;
	
	public static final int BOOTH = 34;
	
	public static final int REQUESTBUY = 35;
	
	public static final int ORE = 36;
	
	public static final int CAVE_HAR = 37;
	
	public static final int TASK_PRIZE = 38;
	
	public static final int MONEY_PROPS = 39;
	
	public static final int CITY_FIGHT = 40;
	
	public static final int GUOZHAN = 41;
	
	public static final int TOURNAMENT = 42;
	
	public static final int GUOWANGZHIQUJINKU = 43;
	
	public static final int FENGLU = 44;
	
	public static final int JIANXI = 45;
	
	public static final int JIAOFUBIAOCHE = 46;
	public static final int CHARGE_MONEY = 47;
	public static final int LEVELUP = 48;
	
	public static final int REQUEST_CANCLE = 49;
	public static final int JIAZU_GONGZI = 50;
	public static final int TASK_PRIZE_LILIAN = 51;
	public static final int 答题奖励文采 = 52;
	public static final int 通知奖励 = 53;
	public static final int 充值活动奖励 = 54;
	public static final int 活动奖励 = 55;
	public static final int 兑换活动 = 56;
	public static final int ACTIVENESS = 57;
	public static final int 后台设置属性 = 58;
	public static final int 挖宝 = 59;
	public static final int 九宫补偿银子 = 60;
	public static final int 仙尊投票奖励 = 61;
	public static final int 仙尊膜拜奖励 = 62;
	public static final int 完成目标奖励 = 63;
	public static final int 米谷交易 = 64;
	public static final int 冲级返利活动 = 65;
	public static final int 充值送月卡活动 = 66;
	public static final int 商城充值送等级礼包 = 67;
	public static final int 充值获取礼包 = 68;
	
	
	public static final String REASON_DESP[] = new String[]{
		"出售物品",		//0
		"邮件获取",		//1
		"交易获取",		//2
		"拍卖获取",		//3
		"使用道具",		//4
		"移动充值卡",		//5
		"联通一卡充",		//6
		"电信充值卡",		//7
		"金币交易所提取",		//8
		"怪物掉落",		//9
		"任务奖励" ,		//10
		"收费邮件",		//11
		"增值平台充值", 		//12
		"银行卡充值",		//13
		"捐献帮派资金",		//14
		"帮战竞标失败",		//15
		"每周工资",		//16
		"邮件创建失败返还金币",		//17
		"游戏点数充值",		//18
		"补偿玩家",		//19
		"战场奖励",		//20
		"问答奖励",		//21
		"回归角色奖励",		//22
		"徒弟充值",		//23
		"徒弟升级",		//24
		"徒弟出师",		//25
		"徒弟拾取",		//26
		"使用荣誉果",		//27
		"帮派战场奖励",		//28
		"充值返还-时间规则",		//29
		"充值返还-升级规则",		//30
		"玩家充值积分",		//31
		"充值积分转账",		//32
		"比武获得", 		//33
		"摆摊", 	//34
		"求购", //35
		"占领矿给的钱" , 	//36
		"收获洞府摇钱树",	//37
		"任务奖励",	//38
		"金钱道具",					//39
		"占领城市给的钱",			//40
		"国战",						//41
		"竞技奖励银子",				//42
		"国王支取金库",				//43
		"领取俸禄",					//44
		"击杀奸细",					//45
		"交付镖车",					//46
		"充值",						//47
		"10级奖励银子",				//48
		"取消求购",					//49
		"家族工资",					//50
		"任务奖励历练",				//51
		"答题奖励文采",				//52
		"通知奖励",					//53
		"充值活动奖励",				//54
		"活动奖励",					//55
		"兑换活动",					//56
		"活动奖励活跃度",			//57
		"后台设置属性",				//58
		"挖宝",						//59
		"九宫补偿银子",				//60
		"仙尊投票奖励",				//61
		"仙尊膜拜奖励",				//62
		"完成目标奖励",				//63
		"米谷交易",						//64
		"冲级返利活动",						//65
		"充值送月卡活动",				//66
		"商城充值送等级礼包",			//67
		"充值获取礼包"				//68
		};
	
	public static String getSavingReason(int type) {
		try {
			return REASON_DESP[type];
		} catch(Exception e) {
			return Translate.text_1211;
		}
	}
	
	public static boolean isValidReasonType(int type) {
		try {
			return REASON_DESP[type] != null;
		} catch(Exception e) {
			return false;
		}
	}
}
