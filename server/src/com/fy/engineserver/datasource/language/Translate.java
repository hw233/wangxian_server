package com.fy.engineserver.datasource.language;

import java.util.HashMap;

import com.fy.engineserver.newtask.service.TaskConfig;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 
 * 
 * 
 */
public class Translate {
	/**
	 * 玩家名字1
	 */
	@DonotTranslate
	public static final String PLAYER_NAME_1 = "@PLAYER_NAME_1@";// 玩家名字

	/**
	 * 玩家名字2
	 */
	@DonotTranslate
	public static final String PLAYER_NAME_2 = "@PLAYER_NAME_2@";// 玩家名字

	/**
	 * 玩家名字3
	 */
	@DonotTranslate
	public static final String PLAYER_NAME_3 = "@PLAYER_NAME_3@";// 玩家名字

	/**
	 * 玩家名字4
	 */
	@DonotTranslate
	public static final String PLAYER_NAME_4 = "@PLAYER_NAME_4@";// 玩家名字

	/**
	 * 玩家名字5
	 */
	@DonotTranslate
	public static final String PLAYER_NAME_5 = "@PLAYER_NAME_5@";// 玩家名字

	/**
	 * 玩家名字6
	 */
	@DonotTranslate
	public static final String PLAYER_NAME_6 = "@PLAYER_NAME_6@";// 玩家名字

	/**
	 * 玩家名字7
	 */
	@DonotTranslate
	public static final String PLAYER_NAME_7 = "@PLAYER_NAME_7@";// 玩家名字

	/**
	 * 玩家名字8
	 */
	@DonotTranslate
	public static final String PLAYER_NAME_8 = "@PLAYER_NAME_8@";// 玩家名字

	/**
	 * 玩家名字9
	 */
	@DonotTranslate
	public static final String PLAYER_NAME_9 = "@PLAYER_NAME_9@";// 玩家名字

	/**
	 * 玩家名字10
	 */
	@DonotTranslate
	public static final String PLAYER_NAME_10 = "@PLAYER_NAME_10@";// 玩家名字

	/**
	 * 物品名字1
	 */
	@DonotTranslate
	public static final String ARTICLE_NAME_1 = "@ARTICLE_NAME_1@";// 物品名字

	/**
	 * 物品名字2
	 */
	@DonotTranslate
	public static final String ARTICLE_NAME_2 = "@ARTICLE_NAME_2@";// 物品名字

	/**
	 * 物品名字3
	 */
	@DonotTranslate
	public static final String ARTICLE_NAME_3 = "@ARTICLE_NAME_3@";// 物品名字

	/**
	 * 物品名字4
	 */
	@DonotTranslate
	public static final String ARTICLE_NAME_4 = "@ARTICLE_NAME_4@";// 物品名字

	/**
	 * 物品名字5
	 */
	@DonotTranslate
	public static final String ARTICLE_NAME_5 = "@ARTICLE_NAME_5@";// 物品名字

	/**
	 * 物品名字6
	 */
	@DonotTranslate
	public static final String ARTICLE_NAME_6 = "@ARTICLE_NAME_6@";// 物品名字

	/**
	 * 物品名字7
	 */
	@DonotTranslate
	public static final String ARTICLE_NAME_7 = "@ARTICLE_NAME_7@";// 物品名字

	/**
	 * 物品名字8
	 */
	@DonotTranslate
	public static final String ARTICLE_NAME_8 = "@ARTICLE_NAME_8@";// 物品名字

	/**
	 * 物品名字9
	 */
	@DonotTranslate
	public static final String ARTICLE_NAME_9 = "@ARTICLE_NAME_9@";// 物品名字

	/**
	 * 物品名字10
	 */
	@DonotTranslate
	public static final String ARTICLE_NAME_10 = "@ARTICLE_NAME_10@";// 物品名字

	/**
	 * 物品id1
	 */
	@DonotTranslate
	public static final String ARTICLE_ID_1 = "@ARTICLE_ID_1@";// 物品id

	/**
	 * 物品id2
	 */
	@DonotTranslate
	public static final String ARTICLE_ID_2 = "@ARTICLE_ID_2@";// 物品id

	/**
	 * 物品id3
	 */
	@DonotTranslate
	public static final String ARTICLE_ID_3 = "@ARTICLE_ID_3@";// 物品id

	/**
	 * 物品id4
	 */
	@DonotTranslate
	public static final String ARTICLE_ID_4 = "@ARTICLE_ID_4@";// 物品id

	/**
	 * 物品id5
	 */
	@DonotTranslate
	public static final String ARTICLE_ID_5 = "@ARTICLE_ID_5@";// 物品id

	/**
	 * 级别1
	 */
	@DonotTranslate
	public static final String LEVEL_1 = "@LEVEL_1@";// 级别

	/**
	 * 级别2
	 */
	@DonotTranslate
	public static final String LEVEL_2 = "@LEVEL_2@";// 级别

	/**
	 * 级别3
	 */
	@DonotTranslate
	public static final String LEVEL_3 = "@LEVEL_3@";// 级别

	/**
	 * 级别4
	 */
	@DonotTranslate
	public static final String LEVEL_4 = "@LEVEL_4@";// 级别

	/**
	 * 级别5
	 */
	@DonotTranslate
	public static final String LEVEL_5 = "@LEVEL_5@";// 级别

	/**
	 * 资质1
	 */
	@DonotTranslate
	public static final String ENDOWMENTS_1 = "@ENDOWMENTS_1@";// 资质

	/**
	 * 资质2
	 */
	@DonotTranslate
	public static final String ENDOWMENTS_2 = "@ENDOWMENTS_2@";// 资质

	/**
	 * 资质3
	 */
	@DonotTranslate
	public static final String ENDOWMENTS_3 = "@ENDOWMENTS_3@";// 资质

	/**
	 * 资质4
	 */
	@DonotTranslate
	public static final String ENDOWMENTS_4 = "@ENDOWMENTS_4@";// 资质

	/**
	 * 资质5
	 */
	@DonotTranslate
	public static final String ENDOWMENTS_5 = "@ENDOWMENTS_5@";// 资质

	/**
	 * 位置1
	 */
	@DonotTranslate
	public static final String INDEX_1 = "@INDEX_1@";// 位置

	/**
	 * 位置2
	 */
	@DonotTranslate
	public static final String INDEX_2 = "@INDEX_2@";// 位置

	/**
	 * 位置3
	 */
	@DonotTranslate
	public static final String INDEX_3 = "@INDEX_3@";// 位置

	/**
	 * 位置4
	 */
	@DonotTranslate
	public static final String INDEX_4 = "@INDEX_4@";// 位置

	/**
	 * 位置5
	 */
	@DonotTranslate
	public static final String INDEX_5 = "@INDEX_5@";// 位置

	/**
	 * 百分比1
	 */
	@DonotTranslate
	public static final String PERCENT_1 = "@PERCENT_1@";// 百分比

	/**
	 * 百分比2
	 */
	@DonotTranslate
	public static final String PERCENT_2 = "@PERCENT_2@";// 百分比

	/**
	 * 百分比3
	 */
	@DonotTranslate
	public static final String PERCENT_3 = "@PERCENT_3@";// 百分比

	/**
	 * 百分比4
	 */
	@DonotTranslate
	public static final String PERCENT_4 = "@PERCENT_4@";// 百分比

	/**
	 * 百分比5
	 */
	@DonotTranslate
	public static final String PERCENT_5 = "@PERCENT_5@";// 百分比

	/**
	 * 数量1
	 */
	@DonotTranslate
	public static final String COUNT_1 = "@COUNT_1@";// 数量

	/**
	 * 数量2
	 */
	@DonotTranslate
	public static final String COUNT_2 = "@COUNT_2@";// 数量

	/**
	 * 数量3
	 */
	@DonotTranslate
	public static final String COUNT_3 = "@COUNT_3@";// 数量

	/**
	 * 数量4
	 */
	@DonotTranslate
	public static final String COUNT_4 = "@COUNT_4@";// 数量

	/**
	 * 数量5
	 */
	@DonotTranslate
	public static final String COUNT_5 = "@COUNT_5@";// 数量

	/**
	 * 技能名1
	 */
	@DonotTranslate
	public static final String SKILL_NAME_1 = "@SKILL_NAME_1@";// 技能名

	/**
	 * 技能名2
	 */
	@DonotTranslate
	public static final String SKILL_NAME_2 = "@SKILL_NAME_2@";// 技能名

	/**
	 * 技能名3
	 */
	@DonotTranslate
	public static final String SKILL_NAME_3 = "@SKILL_NAME_3@";// 技能名

	/**
	 * 技能名4
	 */
	@DonotTranslate
	public static final String SKILL_NAME_4 = "@SKILL_NAME_4@";// 技能名

	/**
	 * 技能名5
	 */
	@DonotTranslate
	public static final String SKILL_NAME_5 = "@SKILL_NAME_5@";// 技能名

	/**
	 * 颜色1
	 */
	@DonotTranslate
	public static final String COLOR_1 = "@COLOR_1@";// 颜色

	/**
	 * 颜色2
	 */
	@DonotTranslate
	public static final String COLOR_2 = "@COLOR_2@";// 颜色

	/**
	 * 颜色3
	 */
	@DonotTranslate
	public static final String COLOR_3 = "@COLOR_3@";// 颜色

	/**
	 * 颜色4
	 */
	@DonotTranslate
	public static final String COLOR_4 = "@COLOR_4@";// 颜色

	/**
	 * 颜色5
	 */
	@DonotTranslate
	public static final String COLOR_5 = "@COLOR_5@";// 颜色

	/**
	 * 字符串1
	 */
	@DonotTranslate
	public static final String STRING_1 = "@STRING_1@";// 字符串

	/**
	 * 字符串2
	 */
	@DonotTranslate
	public static final String STRING_2 = "@STRING_2@";// 字符串

	/**
	 * 字符串3
	 */
	@DonotTranslate
	public static final String STRING_3 = "@STRING_3@";// 字符串

	/**
	 * 字符串4
	 */
	@DonotTranslate
	public static final String STRING_4 = "@STRING_4@";// 字符串

	/**
	 * 字符串5
	 */
	@DonotTranslate
	public static final String STRING_5 = "@STRING_5@";// 字符串

	/**
	 * 字符串6
	 */
	@DonotTranslate
	public static final String STRING_6 = "@STRING_6@";// 字符串

	/**
	 * 字符串7
	 */
	@DonotTranslate
	public static final String STRING_7 = "@STRING_7@";// 字符串

	/**
	 * 字符串8
	 */
	@DonotTranslate
	public static final String STRING_8 = "@STRING_8@";// 字符串

	public static String VIP1体验卡 = "VIP1体验卡";
	public static String VIP2体验卡 = "VIP2体验卡";
	public static String VIP3体验卡 = "VIP3体验卡";
	public static String VIP4体验卡 = "VIP4体验卡";
	public static String VIP5体验卡 = "VIP5体验卡";
	public static String VIP6体验卡 = "VIP6体验卡";
	public static String VIP7体验卡 = "VIP7体验卡";

	public static String 空白 = "";

	public static String 白 = "白色";

	public static String 绿 = "绿色";

	public static String 蓝 = "蓝色";

	public static String 紫 = "紫色";

	public static String 完美紫 = "完美紫色";

	public static String 橙 = "橙色";

	public static String 金 = "金色";

	public static String 完美橙 = "完美橙色";

	public static String 绿宝石 = "绿宝石";
	public static String 橙宝石 = "橙宝石";
	public static String 橙晶石 = "橙晶石";
	public static String 黑金石 = "黑金石";
	public static String 蓝宝石 = "蓝宝石";
	public static String 红宝石 = "红宝石";
	public static String 白晶石 = "白晶石";
	public static String 黄宝石 = "黄宝石";
	public static String 紫晶石 = "紫晶石";

	public static String 酒 = "酒";
	public static String 封魔录 = "封魔录";
	public static String 押镖令 = "押镖令";
//	public static String 重置点数 = "重置点数";
	public static String 符咒 = "符咒";
	public static String 星辰 = "星辰";
	public static String 传音 = "传音";
	public static String 阵法材料 = "阵法材料";
	public static String 元气丹 = "修法丹";

	public static String 白玉泉 = "白玉泉";
	public static String 金浆醒 = "金浆醒";
	public static String 香桂郢酒 = "香桂郢酒";
	public static String 琼浆玉液 = "琼浆玉液";
	public static String 诸神玉液 = "诸神玉液";

	public static String 封魔录降魔 = "封魔录●降魔";
	public static String 封魔录逍遥 = "封魔录●逍遥";
	public static String 封魔录霸者 = "封魔录●霸者";
	public static String 封魔录朱雀 = "封魔录●朱雀";
	public static String 封魔录水晶 = "封魔录●水晶";
	public static String 封魔录倚天 = "封魔录●倚天";
	public static String 封魔录青虹 = "封魔录●青虹";
	public static String 封魔录赤霄 = "封魔录●赤霄";
	public static String 封魔录震天 = "封魔录●震天";
	public static String 封魔录天罡 = "封魔录●天罡";

	public static String 炼妖石 = "培养石";
	public static String 宠物内丹 = "育兽丹";

	public static String 田地令 = "田地令";
	public static String 鲁班令 = "鲁班令";
	public static String 加速令 = "加速令";

	public static String 兽灵 = "兽灵";
	public static String 精怪 = "精怪";
	public static String 神兽 = "神兽";
	public static String 仙魔 = "仙魔";

	public static String 高级古董 = "高级古董";
	public static String 普通古董 = "普通古董";

	public static String 经验果 = "经验果";
	public static String 财神果 = "财神果";
	public static String 祝福果 = "祝福果";
	public static String 灵草 = "灵草";
	public static String 灵花 = "灵花";
	public static String 灵果 = "灵果";

	public static String 圣物 = "圣物";
	public static String 蚕丝 = "蚕丝";
	public static String 翅膀图纸 = "翅膀图纸";

	public static String 缓回血量 = "缓回血量";
	public static String 缓回法力值 = "缓回法力值";
	public static String 瞬回药品 = "瞬回药品";
	public static String 增益药品 = "增益药品";
	public static String 增加经验 = "增加经验";

	public static String 宠物瞬回 = "宠物瞬回";
	public static String 宠物缓回 = "宠物缓回";
	public static String 宠物寿命 = "宠物寿命";
	public static String 宠物快乐 = "宠物快乐";
	public static String 出站宠物不可做此操作 = "出战宠物不可做此操作！";
	public static String 不能操作正在助战的宠物 = "不能操作正在助战的宠物";

	public static String 包裹 = "包裹";

	public static String 单组随机宝箱 = "单组随机宝箱";
	public static String 多组随机宝箱 = "多组随机宝箱";

	public static String 普通商品 = "普通商品";
	public static String 特产 = "特产";

	public static String 修罗坐骑 = "修罗坐骑";
	public static String 影魅坐骑 = "影魅坐骑";
	public static String 九黎坐骑 = "九黎坐骑";
	public static String 仙心坐骑 = "仙心坐骑";
	public static String 兽魁坐骑 = "兽魁坐骑";

	public static String 面具 = "面具";
	public static String 时装 = "时装";

	public static String 角色装备类 = "角色装备类";
	public static String 马匹装备类 = "马匹装备类";
	public static String 法宝类 = "法宝类";
	public static String 翅膀类 = "翅膀类";
	public static String 宝石类 = "宝石类";
	public static String 任务类 = "任务类";
	public static String 人物消耗品类 = "人物消耗品类";
	public static String 宠物消耗品类 = "宠物消耗品类";
	public static String 宠物消耗品 = "宠物消耗品";
	public static String 庄园消耗品 = "庄园消耗品";
	public static String 宠物蛋 = "宠物蛋";
	public static String 古董类 = "古董类";
	public static String 庄园果实类 = "庄园果实类";
	public static String 材料类 = "材料类";
	public static String 角色药品 = "角色药品";
	public static String 宠物药品 = "宠物药品";
	public static String 角色技能书类 = "角色技能书类";
	public static String 宠物技能书类 = "宠物技能书类";
	public static String 包裹类 = "包裹类";
	public static String 宝箱类 = "宝箱类";
	public static String 商品类 = "商品类";
	public static String 宠物类 = "宠物类";
	public static String 坐骑类 = "坐骑类";
	public static String 坐骑食物 = "坐骑食物";
	public static String 时装类 = "时装类";
	public static String 灵髓类 = "灵髓类";

	public static String 一级鉴定符 = "鉴定符";
	public static String 初级强化符 = "强化石";
	public static String 中级强化符 = "强化石";
	public static String 低级神练符 = "低级神炼符";
	public static String 高级神练符 = "高级神炼符";
	public static String 一级铭刻符 = "铭刻符";
	public static String 一级幸运符 = "一级幸运符";
	public static String 一级挖取符 = "一级挖取符";
	public static String 初级育兽丹 = "初级育兽丹";
	public static String 中级育兽丹 = "中级育兽丹";
	public static String 宠物强化物品 = "育兽丹";
	public static String 你的宠物击杀了敌对阵营的 = "你的宠物击杀了敌对阵营的[@STRING_1@]!";
	public static String 你被敌对国家的xx的宠物击杀 = "你被敌对国家的[@STRING_1@]的宠物]击杀！";

	public static String 绑定符 = "绑定符";

	public static String 装备绑定花费 = "消耗绑银：@COUNT_1@绑银。";
	public static String 提示装备绑定花费金币不足 = "绑定装备需要花费@COUNT_1@绑银，您的绑银不足。";

	public static String 精炼升级花费 = "花费：@COUNT_1@绑银。";
	public static String 提示精炼升级花费金币不足 = "装备精炼升级需要花费@COUNT_1@绑银，您的绑银不足。";
	public static String 绑银 = "绑银";
	public static String 银子 = "银子";
	
	public static String 扣除 = "扣除";

	public static String 武器 = "武器";
	public static String 头盔 = "头盔";
	public static String 护肩 = "护肩";
	public static String 胸 = "胸";
	public static String 护腕 = "护腕";
	public static String 腰带 = "腰带";
	public static String 靴子 = "靴子";
	public static String 首饰 = "首饰";
	public static String 项链 = "项链";
	public static String 戒指 = "戒指";
	public static String 翅膀 = "翅膀";

	public static String 面甲 = "面甲";
	public static String 颈甲 = "颈甲";
	public static String 体铠 = "体铠";
	public static String 鞍铠 = "鞍铠";
	public static String 蹄甲 = "蹄甲";

	public static String 装备 = "装备";

	public static String 空手 = "空手";
	public static String 刀 = "刀";
	public static String 剑 = "剑";
	public static String 棍 = "棍";
	public static String 匕首 = "匕首";
	public static String 弓 = "弓";

	public static String 灵髓宝石 = "灵髓";
	public static String 仙气葫芦 = "葫芦";
	/**
	 * 坐骑装备 "10金盔 ","11剑翅", "12腿甲","13配鞍", "14鳞甲"
	 */
	public static String 金盔 = "金盔 ";
	public static String 剑翅 = "剑翅";
	public static String 腿甲 = "腿甲";
	public static String 配鞍 = "配鞍";
	public static String 鳞甲 = "鳞甲";

	// 统计物品名字
	public static String 财神的绑银袋 = "财神的绑银袋";
	public static String 鉴定符 = "鉴定符";
	public static String 铭刻符 = "铭刻符";
	public static String 强化石 = "强化石";
	public static String 育兽丹 = "育兽丹";
	public static String 古董 = "古董";
	public static String 碎银子 = "碎银子";
	public static String 银两 = "银两";
	public static String 银块 = "银块";
	public static String 银条 = "银条";
	public static String 银锭 = "银锭";
	public static String 银砖 = "银砖";
	public static String 银票 = "银票";
	public static String 普通红包 = "普通红包";
	public static String 白银红包 = "白银红包";
	public static String 黄金红包 = "黄金红包";
	public static String 白金红包 = "白金红包";
	public static String 钻石红包 = "钻石红包";
	public static String 镖银白 = "镖银(白)";
	public static String 镖银绿 = "镖银(绿)";
	public static String 镖银蓝 = "镖银(蓝)";
	public static String 镖银紫 = "镖银(紫)";
	public static String 镖银橙 = "镖银(橙)";
	public static String 镖金白 = "镖金(白)";
	public static String 镖金绿 = "镖金(绿)";
	public static String 镖金蓝 = "镖金(蓝)";
	public static String 镖金紫 = "镖金(紫)";
	public static String 镖金橙 = "镖金(橙)";
	public static String 镖玉白 = "镖玉(白)";
	public static String 镖玉绿 = "镖玉(绿)";
	public static String 镖玉蓝 = "镖玉(蓝)";
	public static String 镖玉紫 = "镖玉(紫)";
	public static String 镖玉橙 = "镖玉(橙)";
	public static String 地狱狼王蛋 = "地狱狼王蛋";
	public static String 青丘妖狐蛋 = "青丘妖狐蛋";
	public static String 绝世花妖蛋 = "绝世花妖蛋";
	public static String 霸刀魔王蛋 = "霸刀魔王蛋";
	public static String 黑风熊精蛋 = "黑风熊精蛋";
	public static String 金甲游神蛋 = "金甲游神蛋";
	public static String 擎天牛神蛋 = "擎天牛神蛋";
	public static String 六耳猕猴蛋 = "六耳猕猴蛋";
	public static String 麒麟圣祖蛋 = "麒麟圣祖蛋";
	public static String 白虎战神蛋 = "白虎战神蛋";
	public static String 青龙斗神蛋 = "青龙斗神蛋";
	public static String 玄武水神蛋 = "玄武水神蛋";
	public static String 朱雀火神蛋 = "朱雀火神蛋";
	public static String 九阴魔蝎蛋 = "九阴魔蝎蛋";
	public static String 青翼蝠王蛋 = "青翼蝠王蛋";
	public static String 乱舞蝶妖蛋 = "乱舞蝶妖蛋";
	public static String 罗刹鬼王蛋 = "罗刹鬼王蛋";
	public static String 嗜血剑魂蛋 = "嗜血剑魂蛋";
	public static String 千年参妖蛋 = "千年参妖蛋";
	public static String 雷霆混元至圣蛋 = "雷霆混元至圣蛋";
	public static String 大龙人蛋 = "大龙人蛋";
	public static String 爱的炫舞 = "爱的炫舞";
	public static String 浪漫今生 = "浪漫今生";
	public static String 碧虚青鸾 = "碧虚青鸾";
	public static String 八卦仙蒲 = "八卦仙蒲";
	public static String 梦瞳仙鹤 = "梦瞳仙鹤";
	public static String 乾坤葫芦 = "乾坤葫芦";
	public static String 金极龙皇 = "金极龙皇";
	public static String 焚焰火扇 = "焚焰火扇";
	public static String 深渊魔章 = "深渊魔章";
	public static String 碧虚青鸾1天 = "碧虚青鸾(1天)";
	public static String 碧虚青鸾7天 = "碧虚青鸾(7天)";
	public static String 碧虚青鸾体验 = "碧虚青鸾(体验)";
	public static String 青鸾翎羽 = "青鸾翎羽";
	public static String 朱雀火羽 = "朱雀火羽";
	public static String 战火残片 = "战火残片";
	public static String 才子印记 = "才子印记";
	public static String 武圣印记 = "武圣印记";
	public static String 武圣印记碎片 = "武圣印记碎片";
	public static String 青龙精魂 = "青龙精魂";
	public static String 朱雀精魂 = "朱雀精魂";
	public static String 白虎精魂 = "白虎精魂";
	public static String 玄武精魂 = "玄武精魂";
	public static String 麒麟精魂 = "麒麟精魂";
	public static String 猛志固常在 = "猛志固常在";
	public static String 五岳倒为轻 = "五岳倒为轻";
	public static String 日曜贪狼盔 = "日曜贪狼盔";
	public static String 兽面吞火铠 = "兽面吞火铠";
	public static String 血魂肩铠 = "血魂肩铠";
	public static String 紫铖护手 = "紫铖护手";
	public static String 紫铖战靴 = "紫铖战靴";
	public static String 紫铖腰带 = "紫铖腰带";
	public static String 天启戒 = "天启戒";
	public static String 王天古玉 = "王天古玉";
	public static String 太极图 = "太极图";
	public static String 天地为一朝 = "天地为一朝";
	public static String 蚩尤瀑布碎 = "蚩尤瀑布碎";
	public static String 火曜破军盔 = "火曜破军盔";
	public static String 魔麟饕餮铠 = "魔麟饕餮铠";
	public static String 皇鳞肩铠 = "皇鳞肩铠";
	public static String 遁龙护手 = "遁龙护手";
	public static String 遁龙战靴 = "遁龙战靴";
	public static String 遁龙腰带 = "遁龙腰带";
	public static String 暝天戒 = "暝天戒";
	public static String 辟邪石玉 = "辟邪石玉";
	public static String 破天图 = "破天图";
	public static String 彩凤双飞翼 = "彩凤双飞翼";
	public static String 风萧易水寒 = "风萧易水寒";
	public static String 东极炫金罩 = "东极炫金罩";
	public static String 东极炫金衫 = "东极炫金衫";
	public static String 东极炫金肩 = "东极炫金肩";
	public static String 东极炫金手 = "东极炫金手";
	public static String 东极炫金鞋 = "东极炫金鞋";
	public static String 赤血 = "赤血";
	public static String 噬灵戒 = "噬灵戒";
	public static String 浮世印 = "浮世印";
	public static String 转空 = "转空";
	public static String 明月照九州 = "明月照九州";
	public static String 承影无形杀 = "承影无形杀";
	public static String 青冥海狱罩 = "青冥海狱罩";
	public static String 青冥海狱衫 = "青冥海狱衫";
	public static String 青冥海狱肩 = "青冥海狱肩";
	public static String 青冥海狱手 = "青冥海狱手";
	public static String 天妖灭魂鞋 = "天妖灭魂鞋";
	public static String 残雪 = "残雪";
	public static String 刺尨戒 = "刺尨戒";
	public static String 苍天印 = "苍天印";
	public static String 玄曦 = "玄曦";
	public static String 点睛龙破壁 = "点睛龙破壁";
	public static String 玄天斩灵洛 = "玄天斩灵洛";
	public static String 小北炎极头 = "小北炎极头";
	public static String 小北炎极袄 = "小北炎极袄";
	public static String 小北炎极垫肩 = "小北炎极垫肩";
	public static String 离火手套 = "离火手套";
	public static String 离火轻履 = "离火轻履";
	public static String 离火坠饰 = "离火坠饰";
	public static String 火熔晶 = "火熔晶";
	public static String 火熔项链 = "火熔项链";
	public static String 逆央镜 = "逆央镜";
	public static String 泼墨碧丹青 = "泼墨碧丹青";
	public static String 血晶摩诃惑 = "血晶摩诃惑";
	public static String 海水江崖头 = "海水江崖头";
	public static String 海水江崖袄 = "海水江崖袄";
	public static String 海水江崖垫肩 = "海水江崖垫肩";
	public static String 葵水手套 = "葵水手套";
	public static String 葵水轻履 = "葵水轻履";
	public static String 葵水坠饰 = "葵水坠饰";
	public static String 婆罗戒 = "婆罗戒";
	public static String 婆罗项链 = "婆罗项链";
	public static String 葵水境 = "葵水境";
	public static String 一岁一枯荣 = "一岁一枯荣";
	public static String 本来无一物 = "本来无一物";
	public static String 乾坤地理蛇兜 = "乾坤地理蛇兜";
	public static String 乾坤地理裙 = "乾坤地理裙";
	public static String 千幻蛛巾 = "千幻蛛巾";
	public static String 千幻蝎腕 = "千幻蝎腕";
	public static String 千幻道履 = "千幻道履";
	public static String 千幻腰带 = "千幻腰带";
	public static String 封魔戒 = "封魔戒";
	public static String 落魂钟 = "落魂钟";
	public static String 毒王鼎 = "毒王鼎";
	public static String 凤歌楚狂人 = "凤歌楚狂人";
	public static String 醉古堂剑扫 = "醉古堂剑扫";
	public static String 九黎太虚蛇兜 = "九黎太虚蛇兜";
	public static String 九黎太虚衫 = "九黎太虚衫";
	public static String 蛟龙蛛巾 = "蛟龙蛛巾";
	public static String 蛟龙蝎腕 = "蛟龙蝎腕";
	public static String 蛟龙木履 = "蛟龙木履";
	public static String 蛟龙腰带 = "蛟龙腰带";
	public static String 万魔戒 = "万魔戒";
	public static String 坤木蛊 = "坤木蛊";
	public static String 星罗盘 = "星罗盘";
	public static String 朱雀斧 = "朱雀斧";
	public static String 武圣禅杖 = "武圣禅杖";
	public static String 战火甲胄 = "战火甲胄";
	public static String 朱雀刺 = "朱雀刺";
	public static String 武圣轮 = "武圣轮";
	public static String 战火衫 = "战火衫";
	public static String 朱雀笔 = "朱雀笔";
	public static String 武圣剑 = "武圣剑";
	public static String 战火袄 = "战火袄";
	public static String 朱雀幡 = "朱雀幡";
	public static String 武圣杖 = "武圣杖";
	public static String 战火蟒衣 = "战火蟒衣";
	public static String 白玫瑰 = "白玫瑰";
	public static String 棒棒糖 = "棒棒糖";
	public static String 宝石仙袋 = "宝石仙袋";
	public static String 宝石仙袋2级 = "宝石仙袋(2级)";
	public static String 福星宝石袋4级 = "福星宝石袋(4级)";
	public static String 福星宝石袋5级 = "福星宝石袋(5级)";
	public static String 福星宝石袋6级 = "福星宝石袋(6级)";
	public static String 红玫瑰 = "红玫瑰";
	public static String 蓝色妖姬 = "蓝色妖姬";
	public static String 免罪金牌 = "免罪金牌";
	public static String 巧克力 = "巧克力";
	public static String 软糖 = "软糖";
	public static String 体力丹 = "体力丹";
	public static String 万里法宝鉴定符 = "大喇叭";
	public static String 香火 = "香火";
	public static String 新福星宝石袋2级 = "新福星宝石袋(2级)";

	public static String 通用 = "通用";
	public static String 修罗 = "修罗";
	public static String 影魅 = "影魅";
	public static String 仙心 = "仙心";
	public static String 九黎 = "九黎";
	public static String 兽魁 = "兽魁";

	public static String 等级 = "等级";
	public static String 星级 = "星级";
	public static String 炼化 = "精炼";
	public static String 耐久 = "耐久";

	public static String 查询装备鉴定请求错误回复 = "@ARTICLE_NAME_1@不是装备，不能进行鉴定。";

	public static String 查询装备铭刻请求错误回复 = "@ARTICLE_NAME_1@不是装备，不能进行铭刻。";

	public static String 查询装备绑定请求错误回复 = "@ARTICLE_NAME_1@不是装备，不能进行这种绑定。";

	public static String 此装备已经铭刻不需要执行此操作 = "此装备已经铭刻，不需要执行此操作。";

	public static String 此装备已经绑定不需要执行此操作 = "此装备已经绑定，不需要执行此操作。";

	public static String 此装备还没有精炼完成 = "此装备还没有精炼完成，不需要执行此操作。";

	public static String 查询装备摘星请求错误回复 = "@ARTICLE_NAME_1@不是装备，不能进行摘星。";

	public static String 查询装备装星请求错误回复 = "@ARTICLE_NAME_1@不是装备，不能进行附星。";

	public static String 此装备还没有星级不需要执行此操作 = "此装备没有星，不需要执行此操作。";

	public static String 摘星成功 = "恭喜您摘星成功。";

	public static String 摘星成功获得物品 = "恭喜您摘星成功，您获得了@ARTICLE_NAME_1@。";

	public static String 摘星成功但背包空间不足 = "摘星成功但背包空间不足，可以通过月光宝盒合成来进行清理。";

	public static String 摘星失败 = "很遗憾，摘星失败。";

	public static String 装星已经到最大值提示 = "您的装备星级已满，不用进行此操作。";

	public static String 装备只能够装此种星 = "您的装备现在的星级为@COUNT_1@只能装上@ARTICLE_NAME_1@。";

	public static String 只能是紫色以上装备才能精炼 = "只能是紫色(含)以上装备才能精炼";

	public static String 副装备镶嵌了宝石不能用于精炼 = "副装备镶嵌了宝石，不能用于精炼";

	public static String 副装备镶嵌了器灵不能用于精炼 = "副装备镶嵌了器灵，不能用于精炼";

	public static String 装备镶嵌了宝石不能升级 = "装备镶嵌了宝石不能升级，请先摘下宝石再进行升级操作";

	public static String 装备镶嵌了器灵不能升级 = "装备镶嵌了器灵不能升级，请先摘下器灵再进行升级操作";

	public static String 装备升级需要摘除当前星级 = "装备升级需要摘除当前星级";

	public static String 只能是紫色以上装备才能被精炼 = "只能是紫色(含)以上装备才能被精炼";

	public static String 主装备必须铭刻才能精炼 = "主装备必须铭刻才能精炼";

	public static String 不能放入同一件装备 = "不能放入同一件装备";

	public static String 只能是装备才能精炼 = "只能是装备才能精炼";

	public static String 只能是装备才能被精炼 = "只能是装备才能被精炼";

	public static String 只能是80级装备才能精炼 = "只能是80级(含)以上的装备才能精炼";

	public static String 只能是80级装备才能被精炼 = "只能是80级(含)以上的装备才能被精炼";

	public static String 装备精炼 = "装备精炼";

	public static String 装备精炼经验已满 = "装备精炼经验已满";

	public static String 镶嵌物品必须为宝石 = "镶嵌物品必须为宝石。";

	public static String 装备不能镶嵌或镶嵌位置错误 = "装备不能镶嵌或镶嵌位置错误。";

	public static String 镶嵌位置已有宝石 = "镶嵌位置已有宝石，需要取下该宝石才能镶嵌。";

	public static String 镶嵌宝石颜色和孔颜色不一致 = "镶嵌宝石颜色和孔颜色不一致。";

	public static String 镶嵌宝石颜色和孔颜色不一致描述 = "宝石颜色为@COLOR_1@，孔颜色为@COLOR_2@，颜色不一致不能镶嵌。";

	public static String 挖取宝石位置错误 = "挖取宝石位置错误。";

	public static String 挖取位置上没有宝石 = "挖取位置上没有宝石。";

	public static String 没有挖取道具 = "没有挖取道具。";

	public static String 没有挖取道具提示 = "挖取@ARTICLE_NAME_1@需要@ARTICLE_NAME_2@，您没有此物品！";

	public static String 请放入正确物品 = "请放入正确物品。";

	public static String 合成数量不对 = "物品数量不对";

	public static String 装备合成需要相同颜色的装备 = "装备合成需要相同颜色的装备。";

	public static String 装备合成需要主装备与副装备级别差小于30 = "装备合成需要主装备与副装备级别差小于30。";

	public static String 坐骑装备不能和人物装备合成 = "坐骑装备不能和人物装备合成";

	public static String chiBang不能合成 = "翅膀不能合成";

	public static String 坐骑装备只能合成到完美紫 = "坐骑装备只能合成到完美紫";

	public static String 有宝石镶嵌的装备不能合成 = "有宝石镶嵌的装备不能合成，可以把宝石挖取下来后再进行合成。";

	public static String 有器灵镶嵌的装备不能合成 = "有器灵镶嵌的装备不能合成，可以把器灵挖取下来后再进行合成。";

	public static String 物品已经不需要合成了 = "物品已经不需要合成了。";
	public static String 神匣道具为不可合成道具 = "神匣道具为不可合成道具.";

	public static String 删除物品失败 = "删除物品失败";

	public static String 必须为铭刻装备 = "炼星必须为铭刻装备。";

	public static String 金币不足 = "您的绑银不足。";

	public static String 背包空间不足 = "您的背包空间不足，可以通过月光宝盒合成来进行清理。";

	public static String 背包空间不足提示 = "@ARTICLE_NAME_1@背包空间不足。";

	public static String 需求金币提示 = "消耗绑银：@COUNT_1@绑银。";

	public static String 元宝不足 = "您的银子不足。";

	public static String 银子不足 = "您的银子不足。";
	public static String 越狱boss休息时间 = "越狱boss休息时间，请稍后再试。";

	public static String 银子不足提示 = "您的银子不足，需要@COUNT_1@银子。";

	public static String 请放入强化物品 = "请放入炼星物品。";

	public static String 背包中没有该物品 = "背包中没有@ARTICLE_NAME_1@。";

	public static String 该物品不是需要的物品 = "@ARTICLE_NAME_1@不是所需物品。";

	public static String 装备强化成功 = "炼星成功！@ARTICLE_NAME_1@当前星级为@LEVEL_1@";

	public static String 装备强化失败 = "很遗憾，@ARTICLE_NAME_1@炼星失败了。当前星级为@LEVEL_1@";

	public static String 装备羽化成功 = "羽化成功！@ARTICLE_NAME_1@当前羽化重数为@STRING_1@";

	public static String 装备羽化失败 = "很遗憾，@ARTICLE_NAME_1@羽化失败了。当前羽化重数为@STRING_1@";

	public static String 装备已经强化到了顶级 = "@ARTICLE_NAME_1@已经炼星到了最大级别，不能进行炼星。";

	public static String 装备已经强化到了上限 = "装备已经炼星到了最大级别，不能进行炼星。";
	public static String 装备已经羽化到了上限 = "装备已经羽化到了最大级别，不能进行羽化。";
	public static String 额外的需要材料 = "需精华羽化石@STRING_1@个！";
	public static String 强化需要材料 = "放入单件装备，再放入@STRING_1@，可对装备进行升星，升星后装备的威力将得到提升!";
	public static String 最大星描述 = "您现在的装备已经炼到了最大星，装备的威力达到了极限!";
	public static String 绑银使用已达到上限 = "银子不足，绑银使用已达到上限";
	public static String 所选宠物有正在宠物房挂机 = "所选宠物有正在宠物房挂机，不能执行此操作";
	public static String 此宠物正在宠物房挂机不能执行此操作 = "此宠物正在宠物房挂机，不能执行此操作";
	public static String 宠物无技能 = "无技能";
	public static String 宠物行为设置为xx = "宠物行为设置为@STRING_1@";
	public static String 宠物强化失败 = "很遗憾，@ARTICLE_NAME_1@炼星失败了。当前星级为@LEVEL_1@";
	public static String 宠物强化 = "宠物强化";
	public static String 宠物强化消费绑银不足消费银子 = "强化需要消费绑银@COUNT_1@，绑银不足，消费银子@COUNT_2@两";
	public static String 宠物鉴定 = "宠物鉴定";
	public static String 宠物合体 = "宠物合体";
	public static String 宠物封印失败 = "宠物封印失败";
	public static String 宠物封印失败背包已满 = "宠物封印失败,背包已满";
	public static String 宠物没有未分配的属性点 = "宠物没有未分配的属性点";
	public static String 玩家背包中没有宠物物品 = "玩家背包中没有宠物物品";
	public static String 繁殖确认成功 = "繁殖确认成功";

	public static String 翅膀不能 = "翅膀不能@STRING_1@!";
	public static String 羽化 = "羽化";
	public static String 羽化XX重 = "羽化@COUNT_1@重";

	public static String 你放生了宠物xx = "您放生了宠物@STRING_1@";
	public static String 宠物强化成功 = "恭喜您宠物幻化成功，当前星级为@LEVEL_1@";

	public static String 宠物已经强化到了顶级 = "@ARTICLE_NAME_1@已经炼星到了最大级别，不能进行炼星。";

	public static String 宠物已经强化到了上限 = "宠物已经炼星到了最大级别，不能进行炼星。";

	public static String 喂养宠物删除宠物食物失败 = "喂养宠物删除宠物食物失败";
	public static String 喂养宠物删除宠物瞬回道具失败 = "喂养宠物删除宠物瞬回道具失败";
	public static String 喂养宠物删除宠物持续回血道具失败 = "喂养宠物删除宠物持续回血道具失败";
	public static String 喂养宠物持续回血没有buff = "喂养宠物持续回血没有buff";
	public static String 喂养宠物持续回血此宠物不是出战状态 = "此宠物不是出战状态";
	public static String 此宠物快乐值以达到最高 = "此宠物快乐值已达到最高";
	public static String 此宠物寿命值以达到最高 = "此宠物寿命已达到最高";
	public static String 没有指定宠物不能使用 = "没有指定宠物,不能使用@STRING_1@";
	public static String 此宠物气血已经达到最大 = "此宠物气血已经达到最大";
	public static String 此宠物级别到了顶级 = "此宠物级别到了顶级";
	public static String petLevelOver220 = "级别大于等于%s的宠物不能吃经验丹";
	public static String 此宠物级别大于玩家级别5级 = "宠物级别大于玩家级别5级";
	public static String 宠物获得xx经验 = "宠物获得@COUNT_1@经验";
	public static String 宠物加点确定没有分配点 = "你对此宠物没有加点";
	public static String 宠物加点取消没有分配点 = "你对此宠物没有加点";
	public static String 取消宠物加点成功 = "取消宠物加点成功";
	public static String 宠物加点成功 = "宠物加点成功";
	public static String 宠物xx快乐值小于0召回了 = "宠物@STRING_1@快乐值小于0召回了";
	public static String 宠物xx寿命值小于0召回了 = "宠物@STRING_1@寿命值小于0召回了";

	public static String 绑银不足 = "您的绑银不足。";
	public static String 绑银不足提示 = "您的绑银不足，需要@COUNT_1@绑银。";
	public static String 需求绑银提示 = "消耗绑银：@COUNT_1@";

	public static String 该物品 = "该物品";

	public static String 鉴定成功装备资质已改变 = "鉴定成功，装备资质已改变。";

	public static String 鉴定成功装备资质已改变带物品名 = "鉴定成功，装备资质已改变。@ARTICLE_NAME_1@ @LEVEL_1@级(@ENDOWMENTS_1@)";

	public static String 铭刻完成 = "铭刻完成！";

	public static String 镶嵌完成 = "镶嵌完成！";

	public static String 绑定完成 = "绑定完成！";

	public static String 合成成功 = "合成成功！";

	public static String 镶嵌完成宝石位置信息 = "镶嵌完成！把@ARTICLE_NAME_1@镶嵌到了装备的第@INDEX_1@个插槽。";

	public static String 无鉴定 = "未鉴定";

	public static String 普通 = "普通";

	public static String 一般 = "一般";

	public static String 优质 = "优质";

	public static String 卓越 = "卓越";

	public static String 完美 = "完美";

	public static String 确定 = "确定";

	public static String 取消 = "取消";

	public static String 服务器选项 = "服务器选项";

	public static String 鉴定提示信息 = "鉴定提示信息";

	public static String 确定要开始鉴定吗 = "确定要开始鉴定吗？";

	public static String 确定要开始鉴定吗绑定的 = "您使用了绑定的@ARTICLE_NAME_1@，鉴定后@ARTICLE_NAME_2@会绑定，确定要开始鉴定吗？";

	public static String 确定要开始鉴定吗未鉴定 = "@ARTICLE_NAME_1@是第一次进行鉴定，此次鉴定不需要使用鉴定符，确定要开始鉴定吗？";

	public static String 铭刻提示信息 = "铭刻提示信息";

	public static String 确定要开始铭刻吗 = "确定要开始铭刻吗？";

	public static String 确定要开始铭刻吗绑定的 = "铭刻后@ARTICLE_NAME_1@会绑定，确定要开始铭刻吗？";

	public static String 装备绑定 = "装备绑定";

	public static String 绑定提示信息 = "绑定提示信息";

	public static String 确定要开始绑定吗 = "确定要开始绑定吗？";

	public static String 升级提示信息 = "升级提示信息";

	public static String 确定要开始升级装备吗 = "确定升级吗？";

	public static String 装备升级 = "装备升级";

	public static String 装备升级完成 = "装备升级成功！";

	public static String 翅膀合成成功 = "翅膀合成成功！";

	public static String 装备无需升级 = "装备无需升级！";

	public static String 装备无需精炼 = "装备无需精炼！";

	public static String 人物装备不能和坐骑装备混合精炼 = "人物装备不能和坐骑装备混合精炼";

	public static String 装备升级成功但背包满了 = "装备升级成功，但背包满了！你可以通过月光宝盒合成来进行清理";

	public static String 摘星提示信息 = "摘星提示信息";

	public static String 确定要开始摘星吗 = "摘星有一定几率失败，且摘下的星为绑定的，确定要开始摘星吗？";

	public static String 摘星 = "摘星";

	public static String 没有星星摘星 = "装备星级为零，不能进行摘星操作。";

	public static String 不可以摘星 = "羽化装备，不能进行摘星操作。";
	public static String 不可以附星 = "羽化装备，不能进行附星操作。";

	public static String 装星提示信息 = "附星提示信息";

	public static String 确定要开始装星吗 = "确定要开始附星吗？";

	public static String 确定要开始装星吗绑定的 = "附星后@ARTICLE_NAME_1@会绑定，确定要开始附星吗？";

	public static String 装星完成 = "附星完成。";

	public static String 精炼提示信息 = "精炼提示信息";

	public static String 确定要开始精炼吗 = "确定要开始精炼吗？";

	public static String 确定要开始精炼吗绑定的 = "精炼后@ARTICLE_NAME_1@会绑定，确定要开始精炼吗？";

	public static String 精炼成功 = "精炼成功";

	public static String 精炼成功提示 = "精炼成功，您的@ARTICLE_NAME_1@的精炼经验增长@COUNT_1@";

	public static String 镶嵌提示信息 = "镶嵌提示信息";

	public static String 确定要镶嵌吗 = "确定要将 @ARTICLE_NAME_1@ 镶嵌到 @ARTICLE_NAME_2@ 上吗？";

	public static String 确定要镶嵌吗绑定的 = "由于镶嵌的 @ARTICLE_NAME_1@ 是绑定的，镶嵌后装备也绑定！确定要将 @ARTICLE_NAME_1@ 镶嵌到 @ARTICLE_NAME_2@ 上吗？";

	public static String 挖取提示信息 = "挖取提示信息";

	public static String 确定要挖取吗 = "挖取@ARTICLE_NAME_1@需要消耗一个@ARTICLE_NAME_2@，确定吗？";

	public static String 服务器物品出现错误 = "服务器物品出现错误，请联系GM，谢谢！";

	public static String 服务器出现错误 = "服务器出现错误，请联系GM，谢谢！";

	public static String 宝石 = "宝石";

	public static String 挖取宝石回复 = "附件为您挖取的宝石，请注意查收。";

	public static String 强化提示信息 = "炼星提示信息";

	public static String 宠物强化确认信息 = "是否进行幻化？";

	public static String 强化不提示信息绑定用金币 = "炼星需要花费@COUNT_1@绑银，炼星成功率为@PERCENT_1@%，由于您使用了绑定的材料,炼星后装备会绑定，确定炼星吗？";

	// public static String 强化提示信息绑定用元宝 = "炼星需要花费@COUNT_1@银子，炼星成功率为@PERCENT_1@%，由于您使用了绑定的材料炼星@ARTICLE_NAME_1@，炼星后装备会绑定，确定炼星吗？";
	public static String 强化提示信息绑定用元宝 = "炼星需要花费@COUNT_1@银子，由于您使用了绑定的材料炼星@ARTICLE_NAME_1@，炼星后装备会绑定，确定炼星吗？";

	public static String 强化不提示信息用元宝 = "炼星需要花费@COUNT_1@银子，炼星成功率为@PERCENT_1@%，确定炼星吗？";

	public static String 合成提示信息 = "合成提示信息";

	public static String 合成消耗绑银绑定 = "合成消耗@COUNT_1@绑银，合成后的物品将绑定。";

	public static String 合成消耗元宝提示绑定信息 = "合成消耗@COUNT_1@银子，由于您使用了绑定的物品所以合成后的物品将绑定。";

	public static String 删除物品不成功 = "删除物品不成功，请联系GM！谢谢。";

	public static String 物品不能合成 = "物品不能合成";

	public static String 物品个数不足 = "物品个数不足";

	public static String 几率合成成功 = "%几率合成成功";

	public static String 请放入鉴定所需物品 = "请放入鉴定所需物品";

	public static String 只有装备可以鉴定 = "只有装备可以鉴定";

	public static String 装备铭刻需要放入装备和铭刻物品 = "装备铭刻需要放入装备和铭刻物品";

	public static String 请放入装备 = "请放入装备";
	public static String 羽化石 = "羽化石";
	public static String 级保底符 = "级保底符";
	public static String 保底符 = "级保底符";
	public static String 精华羽化石 = "精华羽化石";
	public static String 请放入羽化石 = "请放入羽化石！";
	public static String 请放入精华羽化石 = "请放入精华羽化石！";

	public static String 请放入装备和星星 = "请放入装备和星星";
	public static String 请放入星星 = "请放入星星";

	public static String 请放入主装备和副装备 = "请放入主装备和副装备";

	public static String 请放入装备和宝石 = "请放入装备和宝石";

	public static String 请输入正确数量 = "请输入正确数量";

	public static String 物品不存在 = "物品不存在";

	public static String 距离物品太远 = "距离物品太远";

	public static String 掉落时间太久 = "掉落时间太久";

	public static String 采集 = "采集";

	public static String 背包已满emile = "您的背包已满，可以通过月光宝盒合成来进行清理";

	public static String 种植 = "种植";

	public static String 精炼 = "精炼";

	public static String 一星 = "破碎星辰(1级)";
	public static String 二星 = "破碎星辰(2级)";
	public static String 三星 = "破碎星辰(3级)";
	public static String 四星 = "破碎星辰(4级)";
	public static String 五星 = "破碎星辰(5级)";
	public static String 六星 = "破碎星辰(6级)";
	public static String 七星 = "破碎星辰(7级)";
	public static String 八星 = "破碎星辰(8级)";
	public static String 九星 = "破碎星辰(9级)";
	public static String 十星 = "破碎星辰(10级)";
	public static String 十一星 = "破碎星辰(11级)";
	public static String 十二星 = "破碎星辰(12级)";
	public static String 十三星 = "破碎星辰(13级)";
	public static String 十四星 = "破碎星辰(14级)";
	public static String 十五星 = "破碎星辰(15级)";
	public static String 十六星 = "破碎星辰(16级)";
	public static String 十七星 = "破碎星辰(17级)";
	public static String 十八星 = "破碎星辰(18级)";
	public static String 十九星 = "破碎星辰(19级)";
	public static String 二十星 = "破碎星辰(20级)";
	public static String 二十一星 = "破碎星辰(21级)";
	public static String 二十二星 = "破碎星辰(22级)";
	public static String 二十三星 = "破碎星辰(23级)";
	public static String 二十四星 = "破碎星辰(24级)";
	public static String 二十五星 = "破碎星辰(25级)";
	public static String 二十六星 = "破碎星辰(26级)";
	public static String 二十七星 = "破碎星辰(27级)";
	public static String 二十八星 = "破碎星辰(28级)";
	public static String 二十九星 = "破碎星辰(29级)";
	public static String 三十星 = "破碎星辰(30级)";
	public static String 药品 = "药品";
	public static String 生活 = "生活";
	public static String 任务 = "任务";
	public static String 奇珍 = "奇珍";
	public static String 异宝 = "异宝";
	public static String 宠物 = "宠物";
	public static String 开天礼包 = "开天礼包";
	public static String 恭喜获得开天礼包 = "恭喜@STRING_1@ 在开天迎劫活动中抢购到了开天礼包*99";

	public static String 您已经将装备装备到身上 = "您已经将@STRING_1@装备到身上";
	public static String 您要装备的物品与背包中装备的不匹配 = "您要装备的物品与背包中@STRING_1@的不匹配";
	public static String 背包已经满了请先腾出空格来再卸载装备 = "背包已经满了，请先腾出空格来再卸载装备！";
	public static String 物品类型错误 = "物品类型错误，请联系GM，谢谢";
	public static String 您没有此物品1 = "您没有@STRING_1@";
	public static String 您没有将装备到身上服务器出现错误 = "您没有将@STRING_1@装备到身上，服务器出现错误";
	public static String 您使用了物品 = "您使用了@STRING_1@";
	public static String 您失去了物品 = "您失去了@STRING_1@x@COUNT_1@";

	public static String 装备强化系统广播详细 = "恭喜！@STRING_1@的@PLAYER_NAME_1@经历千辛万苦将@STRING_2@星级炼到@COUNT_1@。";
	public static String 装备强化系统广播详细_羽化 = "恭喜！@STRING_1@的@PLAYER_NAME_1@经历千辛万苦将@STRING_2@羽化到@STRING_3@。";
	public static String 恭喜你装备升级成功 = "恭喜您装备升级成功。";
	public static String 很遗憾装备没有升级成功 = "很遗憾装备没有升级成功。";
	public static String 很遗憾装备羽化失败 = "很遗憾装备没有羽化成功。";

	public static String 很遗憾宠物没有强化成功 = "很遗憾宠物没有炼星成功。";
	public static String 恭喜xx国的xx人经历千辛万苦将xx宠物炼到xx星等 = "恭喜！@STRING_1@的@PLAYER_NAME_1@经历千辛万苦将宠物@STRING_2@炼到@COUNT_1@星。";

	public static String 帐号名不匹配 = "角色上的帐号名与连接上的帐号名不匹配！";
	public static String 登录 = "登录";
	public static String 找不到对应的角色 = "找不到id对应的角色";
	public static String 你没有权限操作 = "你没有权限操作";
	public static String 发送失败 = "发送失败";
	public static String 操作成功 = "操作成功";
	public static String 家族资源不足 = "家族资源不足";
	public static String 异常 = "异常";
	public static String 任务完成度 = "任务完成度";
	public static String 恭喜获得城砖 = "恭喜@STRING_1@的@STRING_2@在@STRING_3@获得了@STRING_4@城砖";
	public static String 我不是你要找的人 = "我不是你要找的人!";
	public static String 恭喜你猜对了 = "恭喜你猜对了!";
	public static String 很可不是我 = "很可惜,不是我";
	public static String 扣除一分 = "扣除一分";
	public static String 当前分数 = "当前分数";
	public static String 你可以向已下知情人打探信息 = "你可以向已下知情人打探信息";
	public static String 已经获得情报 = "已经获得情报";
	public static String 你没有得到任何情报懒货快去打探消息 = "你没有得到任何情报.懒货,快去打探消息!";
	public static String 你已经找到了目标Ta就是 = "你已经找到了目标,Ta就是";
	public static String 你还没有找到目标据说Ta隐藏在本国的 = "你还没有找到目标,据说Ta隐藏在本国的";
	public static String 你当前的分数 = "你当前的分数";
	public static String 可获得经验 = "可获得经验";
	public static String 恭喜你完成了斩妖降魔 = "恭喜你完成了斩妖降魔";
	public static String 斩妖降魔 = "斩妖降魔";
	public static String 我猜就是你 = "我猜就是你!";
	public static String 说Ta是谁 = "说!Ta是谁?!";
	public static String 数据不存在 = "@INDEX_1@数据@INDEX_2@不存在";

	// 宠物部分
	public static String text_pet_09 = "道具不存在或者道具不是宠物道具!";
	public static String text_pet_12 = "变异宠物无法繁殖!";
	public static String text_pet_13 = "宠物不是一雌一雄，无法繁殖!";
	public static String text_pet_14 = "不是同一品种的宠物无法繁殖!";
	public static String text_pet_15 = "宠物没有剩余繁殖机会!";
	public static String text_pet_16 = "宠物不能是出战状态!";
	public static String 宠物繁殖必须要鉴定 = "宠物繁殖必须要鉴定，有宠物没有鉴定";
	public static String text_pet_18 = "繁殖会话已经结束了!";
	public static String text_pet_22 = "您没有此宠物道具!";
	public static String text_pet_23 = "宠物不存在!";
	public static String 不能操作别人的宠物 = "不能操作别人的宠物";
	public static String text_pet_25 = "此宠物已经鉴定过,您不能重复鉴定!";
	public static String text_pet_30 = "宠物道具不存在或不是宠物道具!";
	public static String text_pet_31 = "喂养宠物物品不存在";
	public static String text_pet_37 = "宠物不能为出战状态!";
	public static String text_炼妖 = "宠物炼化";
	public static String text_缺少炼妖物品 = "宠物炼化需要@ARTICLE_NAME_1@,你没有此类物品";
	public static String text_你还没有炼化 = "此宠物还没有炼化,不能进行替换";
	public static String text_炼化宠物成功 = "炼化宠物@ARTICLE_NAME_1@成功";
	public static String text_替换成功 = "替换成功";
	public static String text_炼化宠物必须是鉴定过的宠物 = "炼化的宠物必须是鉴定过的宠物,此宠物还没有鉴定过";
	public static String text_xx想跟你进行宠物繁殖你同意吗 = "@STRING_1@想跟你进行宠物繁殖,你同意吗";
	public static String text_xx拒绝了你的宠物繁殖请求 = "@STRING_1@拒绝了你的宠物繁殖请求";
	public static String text_你拒绝了xx的宠物繁殖请求 = "你拒绝了@STRING_1@的宠物繁殖请求";
	public static String 繁殖道具不存在或者道具不是宠物道具 = "道具不存在或者道具不是宠物道具";

	public static String 宠物操作银子不足提示 = "您的银子不足，需要@COUNT_1@两银子。";
	// public static String text_pet_38 = "宠物";

	public static String text_pet_39 = "被还童的宠物需为一代宠物!";
	public static String text_pet_41 = "没有喂养食物!";
	public static String text_pet_43 = "宠物快乐值过低，无法出战!";
	public static String text_pet_44 = "宠物寿命已尽!";
	public static String text_pet_45 = "您当前的等级不能召唤此宠物!";
	public static String text_pet_51 = "对方关闭了繁殖会话!";
	public static String 不是宠物 = "不是宠物";
	public static String 你已经在一个繁殖会话中 = "你已经在一个繁殖会话中";
	public static String 对方已经在一个繁殖会话中 = "对方已经在一个繁殖会话中";

	public static String 合体需要花费 = "合体需要花费@COUNT_1@绑银。";

	// public static String 合体需要花费 = "合体需要花费@COUNT_1@银子。";
	public static String 宠物绑定x技能花费 = "@COUNT_1@";
	public static String 宠物绑定技能花费 = "绑定技能需要花费@COUNT_1@两银子";

	public static String 必须都是宠物才能合体 = "必须都是宠物才能合体";
	public static String 出战的宠物不能合体 = "出战的宠物不能合体";

	public static String 宠物不是一代宠不能繁殖 = "不是一代宠物不能繁殖";
	public static String 副宠级别不能小于主宠级别 = "副宠携带等级不能小于主宠携带等级。";

	public static String 最多可以绑定3个技能 = "最多可以绑定3个技能";
	public static String 主宠没有绑定技能中的技能 = "主宠没有绑定技能中的技能";

	public static String 宠物合体需要花费 = "宠物合体需要花费@COUNT_1@两银子，您的银子不足。";

	public static String 宠物xx升到了xx级 = "宠物@STRING_1@升到了@COUNT_1@级";
	public static String 删除物品错误兑换失败 = "删除物品错误，兑换失败";
	public static String 本次兑换需要XX个你包里不够兑换失败 = "本次兑换需要@STRING_1@个，你包里不够，兑换失败";
	public static String 每天兑换XX次数只能增加XX次你今天已经完成不能在兑换 = "每天兑换@STRING_1@次数只能增加@COUNT_1@次，你今天已经完成，不能在兑换";

	public static String 宠物合体成功 = "宠物合体成功";
	public static String 宠物经验丹 = "宠物经验丹";
	public static String 玩家经验丹 = "玩家经验丹";
	public static String 宠物蛋孵化成功 = "宠物蛋孵化成功";
	public static String 孵化失败没有足够的金币 = "孵化失败：没有足够的绑银！";
	public static String 孵化失败宠物背包没有空格子 = "孵化失败：宠物背包没有空格子！";
	public static String 孵化失败级别不够携带孵化的宠物 = "孵化失败：级别不够携带孵化的宠物！";
	public static String 孵化失败 = "孵化失败！";
	public static String 改名成功 = "改名成功";
	public static String 玄兽挂机 = "玄兽挂机";
	public static String 您的宠物被谁杀死了 = "您的宠物被@PLAYER_NAME_1@杀死了";
	public static String 您杀死了谁的宠物 = "您杀死了@PLAYER_NAME_1@的宠物@STRING_1@";
	public static String text_money_enter_cave = "进入庄园";

	public static String 人不能为空 = "人不能为空";
	public static String 权限不足请成为国家官员后再使用此功能 = "权限不足，请成为国家官员后再使用此功能";
	public static String 成为国家官员一天后才能用此功能 = "成为国家官员一天后才能用此功能";
	public static String 今天使用次数已经用光 = "使用失败，此类功能一天只能使用@COUNT_1@次，你今天的使用次数已经达到";
	public static String 今天使用次数已经用光2 = "使用失败，此类功能一周只能使用@COUNT_1@次，你今天的使用次数已经达到";
	public static String 只有接了镖车才能使用此功能 = "只有接了镖车才能使用此功能";
	public static String 镖车已经投保了 = "镖车已经投保了";
	public static String 破烂车也来投保 = "破烂车也来投保?";
	public static String 家族镖车不允许投保 = "家族镖车不允许投保";
	public static String 某点以后不让投保 = "22点以后不让投保";
	public static String 托运 = "托运";
	public static String 请选择要托运到的地点 = "请选择要托运到的地点";
	public static String 托运地点 = "托运地点";
	public static String 囚禁 = "囚禁";
	public static String 请正确输入要囚禁人的名字 = "请正确输入要囚禁人的名字，提示字母区分大小写。";
	public static String 你被囚禁了 = "您被囚禁了！";
	public static String 你被囚禁了详细 = "您被@STRING_1@@PLAYER_NAME_1@囚禁了";
	public static String 某人被某人囚禁 = "@STRING_2@@PLAYER_NAME_2@被@STRING_1@@PLAYER_NAME_1@囚禁一小时！";
	public static String 某人被某人释放 = "@STRING_2@@PLAYER_NAME_2@被@STRING_1@@PLAYER_NAME_1@释放出狱。";
	public static String 某人被某人解禁 = "@STRING_2@@PLAYER_NAME_2@被@STRING_1@@PLAYER_NAME_1@解除禁言了。";
	public static String 请输入囚禁人名称 = "请输入囚禁人名称";
	public static String 您还能囚禁次 = "您还能囚禁@COUNT_1@次";
	public static String 不能囚禁其他国家的人 = "不能囚禁其他国家的人";
	public static String 不能囚禁自己 = "不能囚禁自己";
	public static String 囚禁buff名称 = "囚禁";
	public static String 囚禁地图名 = "jianyu";
	public static String 释放地图名 = "kunlunshengdian";
	public static String 释放地图名2 = "kunhuagucheng";
	public static String 国王令传送状态 = "国王令传送状态";
	public static String 此人已经被囚禁了 = "此人已经被囚禁了，等释放后再囚禁吧！";
	public static String 此人今天已经被囚禁两次 = "此人今天已经被囚禁两次，不能再这么折磨他了！";
	public static String 此人不在线不能囚禁 = "此人不在线不能囚禁";
	public static String 此人正在渡劫不能囚禁 = "此人正在渡劫不能囚禁";
	public static String 禁言 = "禁言";
	public static String 请输入禁言人名称 = "请输入禁言人名称";
	public static String 不能禁言其他国家的人 = "不能禁言其他国家的人";
	public static String 禁言buff名称 = "禁言";
	public static String 此人已经被禁言了 = "此人已经被禁言了，等解禁后再禁言吧！";
	public static String 不能禁言自己 = "不能禁言自己";
	public static String 你被禁言了 = "您被禁言了，只能使用私聊了";
	public static String 不能解禁自己 = "不能解禁自己";
	public static String 不能解禁其他国家的人 = "不能解禁其他国家的人";
	public static String 此人不在线不能解禁 = "此人不在线不能解禁";
	public static String 此人没有被禁言 = "此人没有被禁言";
	public static String 你被禁言了详细 = "您被@STRING_1@@PLAYER_NAME_1@禁言了一小时";
	public static String 某人被某人禁言 = "@STRING_2@@PLAYER_NAME_2@被@STRING_1@@PLAYER_NAME_1@禁言一小时！";
	public static String 此人不在线不能禁言 = "此人不在线不能禁言";
	public static String 释放 = "释放";
	public static String 不能释放其他国家的人 = "不能释放其他国家的人";
	public static String 不能释放自己 = "不能释放自己";
	public static String 此人不在线不能释放 = "此人不在线不能释放";
	public static String 此人没有被囚禁 = "此人没有被囚禁";
	public static String 请选择要释放的人 = "请选择要释放的人";
	public static String 确定要释放此人吗 = "你确定要释放@PLAYER_NAME_1@";
	public static String 确定要解禁此人吗 = "你确定要解禁@PLAYER_NAME_1@";
	public static String 确定要辞去官职吗 = "你确定要辞去官职吗？";
	public static String 您已经成功辞去官职 = "您已经成功辞去官职";
	public static String 辞官详细 = "@STRING_1@@PLAYER_NAME_1@经过深思熟虑决定卸甲归田！";
	public static String 权限不足请成为圣皇元帅宰相后再使用此功能 = "权限不足，请成为混元至圣司命天王司禄天王后再使用此功能";
	public static String 巡捕只能国王和元帅可以任命罢免 = "此官职只能混元至圣和司命天王可以任命罢免";
	public static String 御史大夫只能国王和宰相可以任命罢免 = "此官职只能混元至圣和司禄天王可以任命罢免";
	public static String 此官职只有国王可以任命罢免 = "此官职只有混元至圣可以任命罢免";
	public static String 此官职已经满人了 = "此官职已经满人了";
	public static String 任命 = "任命";
	public static String 任命成功 = "任命成功";
	public static String 你成功任命 = "您任命@PLAYER_NAME_1@为@STRING_1@";
	public static String 你被任命 = "您被@STRING_1@@PLAYER_NAME_1@任命为@STRING_2@";
	public static String 某人被某人任命 = "@STRING_1@@PLAYER_NAME_1@任命@PLAYER_NAME_2@为@STRING_2@";
	public static String 某人被某人罢免 = "@STRING_1@@PLAYER_NAME_1@把@STRING_2@@PLAYER_NAME_2@罢免了";
	public static String 请输入任命人名称 = "请输入任命人名称";
	public static String 不能任命其他国家的人 = "不能任命其他国家的人";
	public static String 您不能任命其他官员 = "您不能任命已经有官职的官员";
	public static String 确认任命吗 = "你确认任命@PLAYER_NAME_1@为@STRING_1@么";
	public static String 确认更改职位吗 = "你确认把@PLAYER_NAME_1@从@STRING_1@任命为@STRING_2@么";
	public static String 此官职没有这个人 = "此官职没有这个人";
	public static String 罢免 = "罢免";
	public static String 罢免成功 = "罢免成功";
	public static String 罢免成功详细 = "您将@STRING_1@@PLAYER_NAME_1@罢免了";
	public static String 你被罢免了 = "你被罢免了";
	public static String 你被罢免了详细 = "您被@STRING_1@@PLAYER_NAME_1@罢免了";
	public static String 确认罢免吗 = "你确认罢免@STRING_1@@PLAYER_NAME_1@么";
	public static String 不能任免自己 = "不能任免自己";
	public static String 不能罢免其他国家的人 = "不能罢免其他国家的人";
	public static String 请进行正确操作 = "请进行正确操作";
	public static String 此功能只有国王才有权使用 = "此功能只有混元至圣才有权使用";
	public static String 不需要给自己授勋 = "不需要给自己授勋";
	public static String 不能给其他国家的人授勋 = "不能给其他国家的人授勋";
	public static String 不能给没有官职的人授勋 = "不能给没有官职的人授勋";
	public static String 此人已经授勋 = "此人已经授勋";
	public static String 人数已满 = "人数已满";
	public static String 授勋人数已满 = "授勋人数已满";
	public static String 不需要给自己撤销授勋 = "不需要给自己撤销授勋";
	public static String 不能给其他国家的人撤销授勋 = "不能给其他国家的人撤销授勋";
	public static String 授勋列表中没有此人 = "授勋列表中没有此人";
	public static String 不需要给自己表彰 = "不需要给自己表彰";
	public static String 不能给其他国家的人表彰 = "不能给其他国家的人表彰";
	public static String 不能给没有官职的人表彰 = "不能给没有官职的人表彰";
	public static String 此人已经表彰 = "此人已经表彰";
	public static String 表彰人数已满 = "表彰人数已满";
	public static String 不需要给自己撤销表彰 = "不需要给自己撤销表彰";
	public static String 不能给其他国家的人撤销表彰 = "不能给其他国家的人撤销表彰";
	public static String 表彰列表中没有此人 = "表彰列表中没有此人";
	public static String 今天已经在NPC处投过票了 = "您今天已经主动投过票了";
	public static String 今天已经在玩家发起的投票中投过票了 = "您今天已经在玩家发起的投票中投过票了";
	public static String 您没有这个权限 = "您没有这个权限";
	public static String 支取资金 = "支取资金";
	public static String 请输入资金数 = "请输入资金数";
	public static String 请输入资金数金库详细 = "当前金库资金@COUNT_1@，请输入资金数";
	public static String 请输入资金数详细 = "当前竞标资金为@COUNT_1@，竞标税率为3%，竞标最低资金@COUNT_2@银子，请输入竞标资金";
	public static String 竞标资金必须高于当前竞标资金 = "竞标资金扣除税率后必须高于当前竞标资金";
	public static String 竞标成功如果有人超过您的竞标会有邮件通知 = "竞标成功，如果有人超过您的竞标金额，会把税后的竞标资金已邮件形式退还给您";
	public static String 您的竞标被别人超过 = "您的竞标被别人超过，税后的竞标资金已经通过邮件发送给您，请注意查收";
	public static String 超出每日支取上限 = "超出每日支取上限";
	public static String 国库里没有这么多钱 = "国库里没有这么多钱";
	public static String 今天已经发布过国运了 = "今天已经发布过国运了";
	public static String 发布国运时间不对 = "发布国运时间不对";
	public static String 今天已经发布过国探了 = "今天已经发布过国探了";
	public static String 发布国探时间不对 = "发布国探时间不对";
	public static String 竞标 = "竞标";
	public static String 竞标资金 = "竞标资金";
	public static String 追加资金 = "追加资金";
	public static String 镖车和你不在一张地图 = "镖车和你不在一张地图";
	public static String 镖车和你距离太远 = "镖车和你距离太远";
	public static String 镖车血量不满不能投保 = "镖车血量不满不能投保";
	public static String 国家不存在 = "国家不存在";
	public static String 镖局金钱不足 = "镖局金钱不足，不能进行投保";
	public static String 当前没有任何家族运营镖局 = "当前没有任何家族运营镖局";
	public static String 现在是系统维护时间请稍后再试 = "现在是系统维护时间(00:00-00:10)请稍后再试";
	public static String 今天已经发放过俸禄了 = "今天已经发放过俸禄了";
	public static String 您还不是国家官员 = "您还不是国家官员";
	public static String 权限不足请成为圣皇大司马大将军后在使用此功能 = "权限不足，请成为混元至圣文德大帝纯阳真圣后在使用此功能";
	public static String 权限不足请成为大将军元帅后在使用此功能 = "权限不足，请成为纯阳真圣司命天王后在使用此功能";
	public static String 召集令 = "召集令";
	public static String 召集令描述 = "是否召集国家成员？您还可以输入一句话";
	public static String 召集令详细描述免费 = "您今天第@COUNT_1@次使用召集令，本次使用免费。是否召集国家成员？您还可以输入一句话";
	public static String 召集令详细描述花钱 = "您今天第@COUNT_1@次使用召集令，本次使用消耗@COUNT_2@，是否召集国家成员？您还可以输入一句话";
	public static String 召唤 = "召唤";
	public static String 必须在本国地图才能使用 = "使用失败，必须在本国地图才能使用";
	public static String 正在召集你们是否前往 = "@STRING_1@正在召集你们，是否前往？该消息发布于@STRING_2@\n@STRING_1@对大家说：@STRING_3@";
	public static String 国王召集你们前往 = "@STRING_1@@PLAYER_NAME_1@使用技能召集你们，是否前往？该消息发布于@STRING_2@";
	public static String 人数已满不能传送 = "人数已满不能传送";
	public static String 发放俸禄最低资金 = "发放俸禄需要国库资金最少@COUNT_1@";
	public static String 俸禄发放成功 = "俸禄发放成功";
	public static String 只有特定官员才可以领取俸禄 = "只有特定官员才可以领取俸禄";
	public static String 没有俸禄可以领取 = "没有俸禄可以领取";
	public static String 领取俸禄成功 = "领取俸禄成功";
	public static String 领取俸禄成功详细 = "@STRING_1@@PLAYER_NAME_1@领取俸禄@COUNT_1@";
	public static String 国王发放俸禄详细 = "@STRING_1@@PLAYER_NAME_1@发放俸禄啦，膜拜ing";
	public static String 不在地图不能使用 = "不在地图不能使用该物品";
	public static String 只能在本国地图使用 = "只能在本国地图使用";
	public static String 您没有权限使用 = "您没有权限使用此物品";
	public static String 使用王者之印 = "使用混元至圣圣印";
	public static String 使用王者之印次数 = "这是您今天第@COUNT_1@次使用混元至圣圣印，您需要花费@COUNT_2@绑银";
	public static String 昆仑圣殿 = "昆仑圣殿";
	public static String 九州天成 = "九州天成";
	public static String 昆华古城 = "昆华古城";
	public static String 上州仙境 = "上州仙境";
	public static String 九州要塞 = "九州要塞";
	public static String 万法之源 = "万法之源";
	public static String 万法遗迹 = "万法遗迹";
	public static String 万法要塞 = "万法要塞";
	public static String 佳梦关 = "佳梦关";
	public static String 昆仑要塞 = "昆仑要塞";
	public static String 灭魔神域 = "灭魔神域";
	public static String 地狱魔渊 = "地狱魔渊";
	public static String 传送成功 = "传送成功";
	public static String 您目前状态不能使用王者之印 = "您当前状态不能使用混元至圣圣印";
	public static String 权限不足不能使用此功能 = "权限不足不能使用此功能";
	public static String 当前不能做此操作 = "您在被囚禁期间不能囚禁他人。";
	public static String 您有囚禁BUFF不能复活 = "囚禁期间死亡只能原地复活";
	public static String 公告 = "公告";
	public static String 天天活动 = "天天活动";
	public static String 请输入公告 = "请输入公告";
	public static String 公告发布成功 = "公告发布成功";
	public static String 灰名buff = "灰名";
	public static String 您输入的玩家名不存在 = "您输入的玩家名不存在";
	public static String 请正确输入要禁言人的名字 = "请正确输入要禁言人的名字";
	public static String 请输入数字 = "请输入数字";
	public static String 竞标时间不正确 = "每天的12点到23点为竞标时间";
	public static String 您还没有家族 = "您还没有家族";
	public static String 数目输入错误 = "数目输入错误";
	public static String 那个格子不存在 = "那个格子不存在";
	public static String 您不是族长不能操作 = "您不是族长不能操作";
	public static String 此格子上没有物品 = "此格子上没有物品";
	public static String 不能给非自己家族的玩家分配 = "不能给非自己家族的玩家分配";
	public static String 玩家不存在 = "玩家不存在";
	public static String 您不是家族族长 = "您不是家族族长";
	public static String 创建家族 = "创建家族";
	public static String 没有这个家族 = "没有这个家族";
	public static String 您的家族和这个家族正处于宣战状态 = "您的家族和这个家族正处于宣战状态";
	public static String 您的家族没有宗派 = "您的家族没有加入宗派";
	public static String 您的宗派没有占领任何城市 = "您的宗派没有占领任何城市";
	public static String 您的家族目前是最高竞标者 = "您的家族当前是最高竞标者，无需进行竞标。请随时关注系统消息，如果竞标被超越，我们会以邮件通知。";
	public static String 竞标资金太少详细 = "竞标银子不得少于@COUNT_1@";
	public static String 您的资金不足 = "您的银子不足";
	public static String 资金错误 = "您输入的银子错误";
	public static String 只有家族族长可以竞标 = "只有家族族长可以竞标";
	public static String 只有家族族长可以追加资金 = "只有家族族长可以追加资金";
	public static String 目前镖局没有家族运营 = "当前镖局没有家族运营";
	public static String 您的家族没有运营镖局 = "您的家族没有运营镖局";
	public static String 本期镖局没有家族竞标成功 = "@STRING_1@本期镖局没有家族竞标成功";
	public static String 恭喜家族获得镖局运营权 = "@STRING_2@恭喜@STRING_1@家族获得镖局运营权";
	public static String 某人发布了国家运镖 = "@STRING_1@@STRING_2@发布了国家运镖，国运期间运镖会有额外经验加成";
	public static String 国家国运开始 = "@STRING_1@的国运开始";
	public static String 某人发布了国家刺探 = "@STRING_1@@STRING_2@发布了国家刺探，国探期间刺探会有额外经验加成";
	public static String 国家国探开始 = "@STRING_1@的国探开始";
	public static String 没有此官职 = "没有此官职";
	public static String 王者霸气 = "王者霸气";
	public static String 王者霸气描述 = "将对敌人造成当前血量的@COUNT_1@的伤害，无视防御。";
	public static String 禁用国王专用技能 = "禁用混元至圣专用技能";
	public static String 御卫术 = "御卫术";
	public static String 免疫 = "免疫";
	public static String 加血 = "加血";
	public static String 加血百分比 = "加血百分比";
	public static String 国王技能不能升级 = "此技能不能升级";
	public static String 您现在不能用这个技能 = "您现在不能用这个技能";
	public static String 国王诞生详细 = "@STRING_1@至高无上的@STRING_2@产生了，@PLAYER_NAME_1@成为新的@STRING_2@，@STRING_1@必将在@PLAYER_NAME_1@的带领下走向辉煌，大家拭目以待！";
	public static String 国王更替详细 = "@STRING_1@@STRING_2@@PLAYER_NAME_2@被赶下台，新的@STRING_2@产生了，@PLAYER_NAME_1@成为新的@STRING_2@，@STRING_1@必将在@PLAYER_NAME_1@的带领下走向辉煌，大家拭目以待！";
	public static String 目前监狱里没有玩家 = "目前监狱里没有可释放的玩家";
	public static String 您现在被囚禁不能传送 = "您现在被囚禁不能传送";
	public static String 表彰详细 = "@STRING_1@@PLAYER_NAME_1@表彰了@STRING_2@@PLAYER_NAME_2@的辉煌战功！";
	public static String 授勋详细 = "@STRING_2@@PLAYER_NAME_2@被@STRING_1@@PLAYER_NAME_1@授予了勋章！";
	public static String 撤销表彰 = "@STRING_1@@PLAYER_NAME_1@撤销了对@STRING_2@@PLAYER_NAME_2@的表彰";
	public static String 撤销授勋 = "@STRING_1@@PLAYER_NAME_1@撤销了对@STRING_2@@PLAYER_NAME_2@的授勋";
	public static String 官员相关权利 = "官员相关权利";
	public static String 辞官 = "辞官";

	public static String 宠物道具正在冷却 = "哎呀，你点太快了";
	public static String 道具正在冷却 = "道具正在冷却";
	public static String 恭喜您获得了 = "恭喜您获得了@COUNT_1@x@STRING_1@";
	public static String 创建临时物品 = "创建临时物品";

	public static String 恭喜您境界提升 = "恭喜您境界提升";
	public static String 已经到达最大境界 = "已经到达最大境界";

	public static String 只有国王才能解开封印 = "只有混元至圣才能解开封印";
	public static String 您的国家现在还没有玩家满足解开封印的等级限制 = "您的国家现在还没有玩家满足解开封印的等级限制(本尊和元神均达到封印等级)";
	public static String 解开封印最早日期 = "解开封印最早日期为@STRING_1@";
	public static String 已经解开最大封印 = "已经解开最大封印，无需解封";
	public static String 无名氏 = "无名氏";
	public static String 开启封印广播 = "@PLAYER_NAME_1@经过不懈努力第一个达到开启封印等级，@STRING_1@@STRING_2@@PLAYER_NAME_2@开启等级封印，@STRING_1@荣耀无限";
	// 宗派
	public static String 恭喜创建者姓名成功建立宗派名称正式开宗立派广收门徒 = "恭喜@PLAYER_NAME_1@成功建立宗派@STRING_1@，正式开宗立派广收门徒 ";
	public static String 创建xx宗派成功 = "创建@STRING_1@宗派成功";
	public static String 宗派已经在修仙界除名 = "宗派@STRING_1@已经在修仙界除名";
	public static String 你还没有家族只有宗派族长才能占领 = "你还没有家族，只有宗派族长才能占领";
	public static String 你还没有宗派只有宗派族长才能占领 = "你还没有宗派，只有宗派族长才能占领";
	public static String 你不是宗派族长只有宗派族长才能占领 = "你不是宗派族长，只有宗派族长才能占领";
	public static String 身份不符请成为族长以后再来申请建立宗派 = "身份不符，请成为族长以后再来申请建立宗派吧。";
	public static String 请先离开您现在的宗派再来申请建立宗派 = "请先离开您现在的宗派再来申请建立宗派吧。";
	public static String 你已经是宗主不能在创建宗派 = "你已经是宗主,不能在创建宗派";
	public static String 宗派 = "宗派";
	public static String 创建宗派 = "创建宗派";
	public static String 创建 = "创建";
	public static String 只有宗主才能够邀请 = "只有宗主才能够邀请";
	public static String 对方已经加入了其他宗派无法邀请 = "对方已经加入了其他宗派,无法邀请";
	public static String 对方不是家族族长不能被邀请 = "对方不是家族族长,不能被邀请";
	public static String 对方跟你不是一个国家 = "对方跟你不是一个国家";
	public static String 对方没有家族 = "对方没有家族";
	public static String name含有违禁字符 = "名字含有违禁字符";
	public static String declaration含有违禁字符 = "宣言含有违禁字符";
	public static String 此宗派名字已经存在 = "此宗派名字已经存在";
	public static String 创建宗派成功 = "创建宗派成功";
	public static String 邀请加入宗派 = "邀请加入宗派";
	public static String xx邀请你加入宗派xx = "@PLAYER_NAME_1@邀请加入宗派@STRING_1@";
	public static String xx拒绝了你的加入宗派邀请 = "@PLAYER_NAME_1@拒绝了你的加入宗派邀请";
	public static String 加入宗派错误 = "加入宗派错误";
	public static String 宗派家族数量已达最大 = "宗派家族数量已达最大";
	public static String 解散宗派 = "解散宗派";
	public static String 密码提示 = "密码提示：@STRING_1@";
	public static String 俩次密码不一样 = "两次密码输入不一致";
	public static String 密码长度不符 = "密码长度不符";
	public static String 密码提示太长 = "密码提示太长";
	public static String 你确定要建立xx宗派吗 = "创建宗派需要@STRING_1@，你确定要建立@STRING_2@宗派吗？";
	public static String 你确定要解散宗派吗 = "你确定要解散宗派吗？请输入密码";
	public static String 宗派密码不正确 = "宗派密码不正确";
	public static String 不是宗主 = "不是宗主";
	public static String 国王不能解散宗派 = "混元至圣不能解散宗派 ";
	public static String 国王不能解散家族 = "混元至圣不能解散家族 ";
	public static String 不是家族族长 = "不是家族族长";
	public static String 你没有家族 = "你没有家族";
	public static String 你没有家族宗派 = "你没有家族宗派";
	public static String 离开宗派 = "离开";
	public static String 你的家族确定要离开xx宗派吗 = "你的家族确定要离开@STRING_1@宗派吗？";
	public static String 已经在宗派中 = "已经在宗派中";
	public static String 宗派名太长 = "宗派名太长";
	public static String 宗派宣言太长 = "宗派宣言太长";
	public static String 宗主不能离开 = "宗主不能离开";
	public static String 宗主不能被踢出 = "宗主不能被踢出";
	public static String xx把宗派禅让给了xx = "@PLAYER_NAME_1@把宗派禅让给了@PLAYER_NAME_2@";
	public static String 宗派宣言修改了 = "宗派宣言修改了";
	public static String 宗派禅让邮件通知 = "宗派禅让邮件通知";
	public static String 宗派禅让邮件内容 = "@PLAYER_NAME_1@把宗派禅让给你，密码是@STRING_1@，密码提示是@STRING_2@";
	public static String 禅让成功 = "禅让成功";
	public static String 不能禅让给宗主 = "不能禅让给宗主";
	public static String 不是同一宗派不能操作 = "不是同一宗派不能操作";
	public static String 加入本宗派 = "加入本宗派";
	public static String XX加入本宗派 = "@STRING_1@加入本宗派";
	public static String 解散了 = "解散了";
	public static String xx解散了 = "@STRING_1@解散了";
	public static String 离开本宗派 = "离开本宗派";
	public static String XX离开本宗派 = "@STRING_1@离开本宗派";
	public static String 离开了 = "离开了";
	public static String 离开了XX = "离开了@STRING_1@";
	public static String 你宗派中的人都不在线 = "你宗派中的人都不在线";
	public static String 你不是宗主只能宗主可以使用 = "你不是宗主，只能宗主可以使用";
	public static String 使用宗主令 = "使用宗主令";
	public static String 你不是宗主不能修改宗派名字 = "你不是宗主不能修改宗派名字";
	public static String 你的宗派不能修改名字 = "你的宗派不能修改名字";
	public static String 请输入你要修改的宗派名字 = "请输入你要修改的宗派名字";
	public static String 您输入的宗派名字含有禁用字符或敏感字符请重新输入 = "您输入的宗派名字含有禁用字符或敏感字符或已经存在此宗派名字,请重新输入";

	// 师徒
	public static String text_申请传功级别条件 = "只有等级满足80级才能发起传功";
	public static String text_领取传功石级别 = "只有等级满足80级才能领取";
	public static String text_你跟师傅不是一个国家 = "你跟师傅不是一个国家";
	public static String text_你跟徒弟不是一个国家 = "你跟徒弟不是一个国家";
	public static String text_你级别太低不能拜师 = "你级别太低不能拜师";
	public static String text_你级别太低不能发布拜师 = "你级别太低不能发布拜师";
	public static String text_你级别太高不能发布拜师 = "你级别太高不能发布拜师";
	public static String text_你级别太高不能拜师 = "你级别太高不能拜师";
	public static String text_你级别太低不能收徒 = "你级别太低不能收徒";
	public static String text_对方级别太低你不能拜师 = "对方级别太低你不能拜师";
	public static String text_对方级别太低不能收徒 = "对方级别太低不能收徒";
	public static String text_对方级别太高不能收徒 = "对方级别太高不能收徒";
	public static String text_已经是你的徒弟 = "已经是你的徒弟";
	public static String text_你已经是他的徒弟 = "你已经是他的徒弟";
	public static String text_已经有师傅 = "已经有师傅";
	public static String text_你徒弟个数已满 = "你徒弟个数已满";

	public static String text_发布收徒你不满足级别条件 = "你级别太低，不能发布收徒";
	public static String text_师傅的徒弟个数已满 = "徒弟个数已满";
	public static String text_逐徒惩罚不能发布 = "逐徒24小时内不能发布";
	public static String text_判师惩罚不能发布 = "叛师24小时内不能发布";

	public static String text_xx逐徒惩罚不能收徒 = "@PLAYER_NAME_1@逐徒24小时内不能收徒";
	public static String text_收徒申请 = "收徒申请";
	public static String text_xx判师惩罚不能拜师 = "@PLAYER_NAME_1@叛师24小时内不能拜师";
	public static String text_拜师申请 = "拜师申请";
	public static String text_没有师徒关系 = "没有师徒关系";
	public static String text_没有徒弟关系 = "没有徒弟";
	public static String text_没有师傅关系 = "没有师傅";
	public static String text_收徒发送成功 = "收徒发送成功";
	public static String text_拜师发送成功 = "拜师发送成功";
	public static String text_发布成功 = "发布成功";
	public static String text_取消发布成功 = "取消发布成功";
	public static String text_判师成功 = "叛师成功";
	public static String text_xx判师 = "@PLAYER_NAME_1@叛师";
	public static String text_逐出徒弟成功 = "逐出徒弟成功";
	public static String text_xx把你逐出 = "@PLAYER_NAME_1@把你逐出";

	public static String text_确定判师 = "确定叛师";
	public static String text_判师惩罚 = "叛师后24小时不能重新拜师，你确定要叛师";
	public static String text_判师没有惩罚 = "师傅已经三天没上线，你可以重新在找一个师傅(不受叛师惩罚)";
	public static String text_确定逐出徒弟 = "确定逐出徒弟";
	public static String text_逐出徒弟惩罚 = "逐出徒弟后24小时不能重新收徒，你确定要逐出徒弟";
	public static String text_逐出徒弟没有惩罚 = "徒弟已经三天没上线，你可以重新在找一个徒弟(不受逐徒惩罚)";

	public static String text_不能重复发送收徒申请 = "你已经发布了一条记录，不能重复发送收徒申请";
	public static String text_不能重复发送拜师申请 = "你已经发布了一条记录，不能重复发送拜师申请";
	public static String text_你还没发布收徒登记 = "你还没发布收徒登记";
	public static String text_你还没发布拜师登记 = "你还没发布拜师登记";
	public static String text_确定取消登记 = "确定取消登记";
	public static String text_师傅逐徒邮件 = "师傅逐徒邮件";
	public static String text_徒弟判师邮件 = "徒弟叛师邮件";
	public static String text_徒弟出师邮件 = "徒弟出师邮件";
	public static String text_不是师徒关系 = "不是师徒关系";
	public static String text_你还没有这个徒弟 = "你还没有这个徒弟";
	public static String text_你没有这个师傅 = "你没有这个师傅";

	public static String 无官职 = "无官职";
	public static String 国王 = "混元至圣";
	public static String 大司马 = "文德大帝";
	public static String 大将军 = "纯阳真圣";
	public static String 元帅 = "司命天王";
	public static String 宰相 = "司禄天王";
	public static String 巡捕_国王 = "度厄神君";
	public static String 巡捕_元帅 = "贪狼神君";
	public static String 御史大夫_国王 = "禄存神君";
	public static String 御史大夫_宰相 = "贞廉神君";
	public static String 御林卫队 = "九天巡守";
	public static String 护国公 = "武曲星君";
	public static String 辅国公 = "文曲星君";

	public static String 燃烧圣殿 = "燃烧圣殿";
	public static String 化外之境 = "化外之境";

	public static String 国家A名字 = "昆仑";//昊天
	public static String 国家B名字 = "九州";//无尘
	public static String 国家C名字 = "万法";//沧月
	public static String 中立 = "中立";

	public static String 周围 = "周围";
	public static String 世界 = "世界";
	public static String 彩世 = "彩世";
	public static String 私聊 = "私聊";
	public static String 家族 = "家族";
	public static String 宗族 = "宗族";
	public static String 国家 = "国家";
	public static String 队伍 = "队伍";
	public static String 好友 = "好友";
	public static String 群组 = "群组";
	public static String 战斗 = "战斗";
	public static String 世界聊天需要道具 = "世界聊天需要道具";
	public static String 您的语音对象已经不在线了 = "您的语音对象已经不在线了";
	public static String 世界聊天需要道具描述 = "在世界中喊话需要@ARTICLE_NAME_1@，您确定喊话吗？";
	public static String 彩世聊天需要道具 = "彩世聊天需要道具";
	public static String 彩世聊天需要道具描述 = "在彩世中喊话需要@ARTICLE_NAME_1@，您确定喊话吗？";
	public static String 喊话所需物品 = "千里传音符";
	public static String 彩世喊话所需道具 = "万里法宝鉴定符";
	public static String 没有喊话道具 = "没有喊话道具";
	public static String 没有喊话道具描述 = "在该频道中喊话需要@ARTICLE_NAME_1@，您没有该物品";
	public static String 您的角色处于锁定状态暂时不能使用邮箱 = "您的角色处于锁定状态，暂时不能使用邮箱！";
	public static String BUG自动报告 = "BUG自动报告";
	public static String 无此收信人 = "无此收信人";
	public static String 无标题 = "无标题";
	public static String 邮件金币或者付费价格错误 = "邮件银子或者付费价格错误！";
	public static String 数量错误 = "数量错误！";
	public static String 金币不够 = "绑银不够";
	public static String 邮件收费提示 = "邮件收费提示";
	public static String 发送邮件银子不足 = "发送邮件需要@COUNT_1@银子，您的银子不足";
	public static String 发送邮件绑银不足 = "发送带附件的邮件，每次需要扣除50两绑定银子！绑银不足会扣除银子";
	public static String 今日免费邮件次数已用完 = "今日免费邮件次数已用完，如继续发送邮件，手续费为每封@COUNT_1@文。";
	public static String 您发送邮件频率太过频繁请稍后再试 = "您发送邮件频率太过频繁，请稍后再试。";
	public static String 此邮件为付费邮件需要添加附件 = "此邮件为付费邮件，需要添加附件";
	public static String 不能给自己发送邮件 = "不能给自己发送邮件";
	public static String 发送确认 = "发送确认";
	public static String 邮件发送贵重物品确认 = "本次交易物品为贵重物品，您是否确定进行邮寄！";
	public static String 发送确认详细 = "发送确认详细";
	public static String 无 = "无";
	public static String 邮件中的物品错误 = "邮件中的物品错误";
	public static String 发送失败收件人邮箱已满 = "发送失败，收件人邮箱已满";
	public static String 您的邮箱已满不能接收新邮件请及时清理 = "您的邮箱已满，不能接收新邮件，请及时清理。";
	public static String 发送失败邮件附件中有两个格子指向背包中的同一个格子 = "发送失败，邮件附件中有两个格子指向背包中的同一个格子！";
	public static String 发送失败不能邮寄绑定的物品 = "发送失败，不能邮寄绑定的物品！";
	public static String 你的背包格中没有那么多物品 = "你的背包格中没有那么多物品";
	public static String 你的背包格中没有任何物品 = "你的背包格中没有任何物品";
	public static String 扣除金币时出错 = "扣除绑银时出错";
	public static String 发送失败删除背包物品失败 = "发送失败，删除背包物品失败！";
	public static String 返还金币时出错 = "返还绑银时出错";
	public static String 用户背包已满无法放置更多物品 = "用户背包已满，无法放置更多物品，可以通过月光宝盒合成来进行清理";
	public static String 创建邮件失败 = "创建邮件失败";
	public static String 邮件发送成功 = "邮件发送成功";
	public static String 邮件发送成功收费 = "邮件发送成功，收费@COUNT_1@两";
	public static String 系统 = "系统";
	public static String 未知 = "未知";
	public static String 您的邮箱已满清理后才能收新邮件 = "您的邮箱已满，清理后才能收新邮件!";
	public static String 付费提示 = "此邮件需要您支付@COUNT_1@银子，您的余额不足";
	public static String 提取附件失败扣费失败 = "提取附件失败:扣费失败";
	public static String 邮件回复 = "邮件回复";
	public static String 邮件回复主题 = "玩家[@PLAYER_NAME_1@]通过付费提取了您发送的邮件";
	public static String 邮件回复内容 = "玩家[@PLAYER_NAME_1@]通过付费提取了您发送的邮件，您获得@COUNT_1@银子";
	public static String 提取附件失败请联系管理员 = "提取附件失败，请联系管理员";
	public static String 普 = "普";
	public static String 商 = "商";
	public static String 仓 = "仓";
	public static String 医 = "医";
	public static String 修 = "修";
	public static String 武 = "武";
	public static String 车 = "车";
	public static String 卫 = "卫";
	public static String 地 = "地";
	public static String 银 = "银";
	public static String 绑 = "绑";
	public static String 工 = "工";
	public static String 驿 = "驿";
	public static String 采 = "采";
	public static String 门 = "门";
	public static String 战 = "战";
	public static String 拾 = "拾";
	public static String 活 = "活";
	public static String 洞 = "洞";
	public static String 刺 = "刺";
	public static String 守 = "守";
	public static String 护 = "护";
	public static String 镖 = "镖";
	public static String 你好 = "你好";
	public static String 错误 = "错误";
	public static String 斧 = "斧";
	public static String 杖 = "杖";
	public static String 禅杖 = "禅杖";
	public static String 笔 = "笔";
	public static String 幡 = "幡";
	public static String 轮 = "轮";
	public static String 镰刀 = "镰刀";
	public static String 权杖 = "权杖";
	public static String 活动 = "活动";
	public static String 内容查看 = "内容查看";

	public static String 封印赐福 = "封印赐福";

	public static String[] classlevel = new String[] { "炼气", "筑基", "开光", "金丹", "元婴", "出窍", "合体", "渡劫", "寂灭", "大乘", "真绝", "万法", "凡仙", "地仙", "天仙", "玄仙" };
	public static String[] 初级活动等级礼包 = new String[] { "公测10级奖励", "公测20级奖励", "公测30级奖励", "公测40级奖励", "公测50级奖励", "公测60级奖励", "公测70级奖励" };

	public static String 此物品不属于你 = "此物品不属于你";
	public static String 拾取物品 = "拾取物品";
	public static String 拾取物品提示 = "您拾取了@ARTICLE_NAME_1@";
	public static String 使用防爆背包 = "使用防爆背包";
	public static String 物品已经到期 = "物品已经到期";
	public static String 背包使用成功 = "背包使用成功";
	public static String 新包无法放下原有包的所有物品 = "新包无法放下原有包的所有物品，不能替换";
	public static String 使用防爆背包替换 = "新包将替换原有包，原有包将消失，替换吗？";
	public static String 境界不够不能使用 = "境界不够不能使用此物品，请去完成境界任务提升境界";
	public static String 级别太低不能使用 = "级别太低不能使用此物品";
	public static String 境界不够不能镶嵌 = "境界不够不能镶嵌";

	public static String 限时坐骑已到期将从坐骑列表中删除 = "限时坐骑@STRING_1@已到期，将从坐骑列表中删除";
	public static String 骑飞行坐骑 = "骑飞行坐骑";
	public static String 飞行状态下不能招出宠物 = "飞行状态下不能招出宠物";
	public static String 非战斗坐骑不能穿装备 = "非战斗坐骑不能穿装备";
	public static String 飞行坐骑不能穿装备 = "飞行坐骑不能穿装备,请将默认坐骑调整为非飞行坐骑再试!";
	public static String 这是坐骑装备你还没有设置默认坐骑 = "这是坐骑装备你还没有设置默认坐骑,不能使用";
	public static String 你已经将装备穿到xx上 = "你已经将装备穿到@STRING_1@上";
	public static String 不是本职业装备 = "不是@STRING_1@坐骑装备";
	public static String 坐骑等阶太低 = "坐骑等阶太低";
	public static String 不是坐骑装备 = "不是坐骑装备";
	public static String 职业不符 = "职业不符";
	public static String 这是坐骑装备 = "这是坐骑装备";
	public static String 只能全部调换 = "由于两个物品不是同一种物品，所以只能进行调换位置，不能进行合并操作";
	public static String 物品不能放入该包裹 = "物品不能放入该包裹";
	public static String 包裹中还有物品 = "包裹中还有物品，不能执行此操作";
	public static String 包裹已到期 = "包裹已到期，不能执行此操作";
	public static String 包裹已到期不能执行交换 = "包裹已到期，不能执行交换物品操作";
	public static String 仓库中的物品不能放入该包裹交换失败 = "仓库中的物品不能放入该包裹，物品交换失败";
	public static String 仓库中的物品不能放入该包裹 = "仓库中的物品不能放入该包裹";
	public static String 仓库 = "仓库";
	public static String 打开仓库 = "打开1号仓库";
	public static String 打开2仓库 = "打开2号仓库";
	public static String 密码 = "密码";
	public static String 密码错误请重新输入密码详细 = "输入密码错误第@COUNT_1@次";
	public static String 您输入的次数过多请稍后再试 = "您输入的次数过多请稍后再试！";
	public static String 您又输入错了 = "您又输入错了，为了您的仓库安全，系统已经将你的仓库锁定，请稍后片刻再试";
	public static String 设定密码成功 = "设定密码成功，您的密码是@STRING_1@，请妥善保存";
	public static String 设定密码不能为空 = "设定密码不能为空";
	public static String 密码长度最短位数 = "密码最短4位";
	public static String 密码长度最长位数 = "密码最长12位";
	public static String 设置密码 = "设置密码";
	public static String 修改密码 = "修改密码";
	public static String 原始密码错误 = "原始密码错误";
	public static String 清除密码成功 = "清除密码成功";
	public static String 密码输入错误 = "密码输入错误";
	public static String 扩展仓库物品 = "仓库扩展券";
	public static String 您的没有提升仓库空间的材料 = "您背包内尚未拥有仓库扩展券，无法扩展仓库。";
	public static String 仓库已经扩展到最大 = "仓库已经扩展到最大";
	public static String 扩展仓库详细提示 = "您要消耗一个@STRING_1@来拓展仓库空间吗？";
	public static String 扣除物品失败 = "扣除物品失败";
	public static String 增加仓库格子成功 = "您已经成功拓展了仓库空间，您现在的仓库空间为@COUNT_1@格。";
	public static String 仓库扩展 = "仓库1号扩展";
	public static String 仓库2扩展 = "仓库2号扩展";
	public static String 没有时装不能进行此操作 = "没有时装不能进行此操作";

	public static String 恶人 = "恶人";
	public static String 恶霸 = "恶霸";
	public static String 罪大恶极 = "罪大恶极";
	public static String 十恶不赦 = "十恶不赦";
	public static String 万恶之首 = "万恶之首";

	public static String 聚仙殿 = "聚仙殿";
	public static String 聚宝庄 = "聚宝庄";
	public static String 仙灵洞 = "仙灵洞";
	public static String 龙图阁 = "龙图阁";
	public static String 仙兽房 = "仙兽房";
	public static String 武器坊 = "武器坊";
	public static String 防具坊 = "防具坊";
	public static String 家族大旗 = "家族大旗";
	public static String 标志像青龙 = "标志像青龙";
	public static String 标志像白虎 = "标志像白虎";
	public static String 标志像朱雀 = "标志像朱雀";
	public static String 标志像玄武 = "标志像玄武";

	public static String 复活卡 = "复活丹";
	public static String 您没有这项技能 = "您没有这项技能";
	public static String 技能会自动学习或需要技能书 = "1级进阶技能需要使用技能书学习，其他1级技能玩家到等级后自动学会。学会后才能点击升级按钮";
	public static String 怒气技能需要技能书才能学习 = "怒气技能需要技能书才能学习";
	public static String 技能书学习限制 = "该技能不是一级技能，不需要使用技能书学习。";
	public static String 技能已经到达最高级 = "技能已经到达最高级。";
	public static String 您今天可使用的绑银不足 = "您今天可使用的绑银和银子不足。";
	public static String moneyPay = "（每次消耗银子%d两）";
	public static String 您没有足够的经验 = "您没有足够的经验。";
	public static String 您没有足够的技能点 = "您没有足够的技能点。";
	public static String 您没有足够的元气点 = "技能所需修法值不足，完成主线任务或者通过师徒活动可以获得大量修法值。";
	public static String 级别不足不能学习此技能 = "级别太低，不能学习此技能。";
	public static String 级别不足 = "级别太低。";
	public static String 系统配置错误 = "系统配置错误，请联系管理员。";
	public static String 系统配置错误1 = "系统配置错误，所需金钱设置错误。";
	public static String 系统配置错误2 = "系统配置错误，所需经验设置错误。";
	public static String 系统配置错误3 = "系统配置错误，所需级别设置错误。";
	public static String 系统配置错误4 = "系统配置错误，所需点数设置错误。";
	public static String 系统配置错误5 = "系统配置错误，所需元气设置错误。";
	public static String 学习技能成功 = "学习技能成功";
	public static String 学习技能成功详细 = "学习技能<@STRING_1@>@LEVEL_1@级成功";
	public static String 您不能学习这项技能 = "您不能学习这项技能";
	public static String 法术消耗点数 = "法术消耗：@COUNT_1@点";
	public static String 法术消耗豆数 = "技能消耗：@COUNT_1@豆";
	public static String 法术增加豆数 = "技能效果：增加@COUNT_1@豆";
	public static String 怒气消耗点数 = "怒气消耗：@COUNT_1@点";
	public static String 距离详细 = "距离：@COUNT_1@码";
	public static String 瞬发 = "瞬发";
	public static String 吟唱 = "吟唱";
	public static String 冷却 = "冷却";
	public static String 下一级 = "下一级：";
	public static String 怒气值不够 = "怒气值不够";
	public static String 兽魁豆不够 = "剩余豆不够";
	@DonotTranslate
	public static String 出生点1 = "出生点2";
	@DonotTranslate
	public static String 出生点2 = "出生点3";
	@DonotTranslate
	public static String 出生点3 = "出生点4";

	public static String 商品已经售完 = "商品已经售完";
	public static String 您不能再购买了 = "商品为限量商品，您购买数量已经到达上限";
	public static String 购买时间还没到 = "购买时间还没到或已过期";
	public static String 购买数量超过限制 = "购买数量超过限制";
	public static String 商品剩余个数 = "商品剩余@COUNT_1@个";
	public static String 个人购买剩余个数 = "个人购买剩余@COUNT_1@个";
	public static String 购买时间每天 = "购买时间每天@STRING_1@时到@STRING_2@时";
	public static String 星期一 = "星期一";
	public static String 星期二 = "星期二";
	public static String 星期三 = "星期三";
	public static String 星期四 = "星期四";
	public static String 星期五 = "星期五";
	public static String 星期六 = "星期六";
	public static String 星期日 = "星期日";
	public static String 购买时间星期 = "购买时间@STRING_1@@STRING_2@时到@STRING_3@@STRING_4@时";
	public static String 购买时间每月 = "购买时间每月@STRING_1@日到@STRING_2@日";
	public static String 购买时间定点 = "购买时间@STRING_1@月@STRING_2@日@STRING_3@时@STRING_4@分到@STRING_5@月@STRING_6@日@STRING_7@时@STRING_8@分";
	public static String 您购买了物品 = "您购买了@COUNT_1@x@STRING_1@";
	public static String 您查询的区域不存在 = "您查询的区域不存在";
	public static String 玩家到过的地图 = "玩家到过的地图";

	public static String 您接受了任务 = "您接受了@STRING_1@任务";
	public static String 在地图刷出奸细2 = "@PLAYER_NAME_1@发布了封印任务，在@STRING_1@地图上，出现了奸细，没有达到封印等级的人击败奸细可以得到丰厚的奖励";
	public static String 在地图刷出奸细1 = "@PLAYER_NAME_1@发布了封印任务，在@STRING_1@地图上，出现了奸细";
	public static String 封印任务 = "封印任务";
	public static String 恭喜你杀死奸细 = "恭喜您杀死奸细，因为您助人为乐，特奖励您25两银子和经验";

	public static String 您没有家族 = "您没有家族";
	public static String 您没有宗族 = "您没有宗族";
	public static String 您没有队伍 = "您没有队伍";

	public static String 装备修理成功 = "装备修理成功";
	public static String 装备无需修理 = "装备无需修理";
	public static String 修理 = "修理";
	public static String 绑银不足装备修理失败 = "绑银不足(或已达上限),银子不足装备修理失败";

	public static String 破防 = "破防";
	public static String 破防详细描述 = "增加破防@STRING_1@";
	public static String 精准 = "精准";
	public static String 精准详细描述 = "增加精准@STRING_1@";
	public static String 回复血量且降低速度 = "回复血量且降低速度";
	public static String 回复血量且降低速度详细描述 = "每@COUNT_1@秒回复@COUNT_2@hp且降低移动速度@STRING_1@";
	public static String 无敌且降低命中 = "无敌且降低命中";
	public static String 无敌且降低命中详细 = "无敌但降低@STRING_1@命中";
	public static String 增加闪避暴击百分比 = "增加闪避暴击百分比";
	public static String 增加闪避百分比详细 = "增加闪避@STRING_1@";
	public static String 降低闪避百分比详细 = "降低闪避@STRING_1@";
	public static String 增加暴击百分比详细 = "增加暴击@STRING_1@";
	public static String 降低暴击百分比详细 = "降低暴击@STRING_1@";
	public static String 增加双防攻强 = "增加双防攻强";
	public static String 增加双防百分比详细 = "增加双防@STRING_1@";
	public static String 降低双防百分比详细 = "降低双防@STRING_1@";
	public static String 增加命中百分比详细 = "增加命中@STRING_1@";
	public static String 降低命中百分比详细 = "降低命中@STRING_1@";
	public static String 增加攻强百分比详细 = "增加攻强@STRING_1@";
	public static String 降低攻强百分比详细 = "降低攻强@STRING_1@";
	public static String 您的经验已满 = "您的经验已满，请尽快使用，否则得不到经验";
	public static String 您的级别已达到限制 = "您当前的级别已经达到系统限制，不能升级";
	public static String 您的级别已达到封印限制 = "您当前的级别已经达到系统封印级别限制，不能升级";
	public static String 暂未开放更高等级 = "暂未开放更高等级，敬请期待!";
	public static String 您的元神等级不能超过本尊等级 = "您的元神等级不能超过本尊等级";
	public static String 恭喜您升级了 = "恭喜您升级了";
	public static String 某国某人为该国第一个到达封印级别的人国王可以在时间点后开启等级封印 = "@STRING_1@@PLAYER_NAME_1@为@STRING_1@第一个到达开启封印等级的人，@STRING_2@可以在@COUNT_1@年@COUNT_2@月@COUNT_3@日@COUNT_4@时@COUNT_5@分后开启等级封印";
	public static String 封印信息1 = "经过艰苦的修炼，您已经成为站在世界顶端的人！如今@STRING_1@想让你带领未被封印的玩家夺回被奸细抢走的@STRING_2@，你可以发布手谕此时奸细会出现在边疆。未被封印的玩家可以击杀奸细，你将获得@STRING_2@一个！";
	public static String 封印信息2 = "收集足够的@STRING_1@可以找@STRING_2@兑换以下奖励：";
	public static String 封印信息3 = "这是你今天的第@COUNT_1@次手谕，发布手谕需要@COUNT_3@银子，你今天还能发布@COUNT_2@次手谕";
	public static String 封印开启时间 = "当有玩家本尊和元神等级均达到封印等级时，在@COUNT_1@年@COUNT_2@月@COUNT_3@日@COUNT_4@时@COUNT_5@分后由@STRING_1@亲自开启等级封印";
	public static String 冥火令 = "冥火令";
	public static String 封印物品1 = "飞剑坐骑(1天)";
	public static String 封印物品2 = "飞剑坐骑(7天)";
	public static String 封印物品3 = "飞剑坐骑";
	public static String 太白金星 = "太白金星";

	public static String text_trade_000 = "当前区域不能摆摊出售物品!";
	public static String text_trade_001 = "你还太小了,快点升级吧!摆摊至少要等级达到:";
	public static String text_trade_002 = "无效请求!";
	public static String text_trade_003 = "TA已经收摊了~";
	public static String text_trade_004 = "所选的格子已经没有物品了,请重新选择";
	public static String text_trade_005 = "绑定物品不能交易!";
	public static String text_trade_006 = "物品数量不足!";
	public static String text_trade_007 = "你的钱不够!";
	public static String text_trade_008 = "没有足够的位置了!";
	public static String text_trade_009 = "数量异常!";
	public static String text_trade_010 = "价格异常!";
	public static String text_trade_011 = "物品不存在!";
	public static String text_trade_012 = "招牌长度太长";
	public static String text_trade_015 = "背包已满,可以通过月光宝盒合成来进行清理";
	public static String text_trade_016 = "摆摊";
	public static String text_trade_017 = "对方不在摆摊区摆摊，不能查看他的摊位";
	public static String text_trade_018 = "等级到20级才能查看他人摆摊";
	public static String text_trade_019 = "此摊位出售绑定物品，不能查看。";
	public static String text_trade_020 = "此摊位出售物品重复，不能查看。";
	public static String text_trade_021 = "玩家物品栏已满，交易无法进行";
	public static String text_trade_022 = "交易失败，银子不足";
	public static String text_trade_023 = "银子不能做此操作!";
	public static String 离线摆摊邮件标题 = "离线摆摊留言";
	public static String 离线摆摊邮件购买标题 = "离线摆摊出售成功";
	public static String 离线摆摊出错 = "离线摆摊失败";
	public static String 没开放离线摆摊功能 = "需要月卡才能激活离线摆摊功能";
	public static String 离线摆摊成功 = "离线摆摊成功";
	public static String 服务器配置错误 = "服务器配置错误，请联系GM";
	public static String 鼓励失败 = "鼓舞失败，副本已经结束";
	public static String 鼓励已达最大 = "鼓舞已达上限";
	public static String 该级别鼓励已经被抢啦 = "该级别鼓舞已经被抢啦";
	public static String 角色等级不满足 = "角色等级不满足";
	public static String 家族等级不满足 = "家族等级不满足";
	public static String 鼓励比例配置错误 = "鼓舞比例配置错误，请联系GM";
	public static String 当前鼓励比例 = "当前鼓励比例";
	public static String 下一级鼓励比例 = "下一级鼓舞+@STRING_1@,花费@STRING_2@";
	public static String boss挑战活动开启了 = "全民BOSS活动已经开启了，是否前往挑战?";
	public static String boss挑战活动结束了 = "全民BOSS活动已经结束了，副本将在5秒内关闭";
	public static String boss奖励 = "恭喜您获得世界BOSS伤害奖励！";
	public static String boss参与奖励 = "恭喜您获得世界BOSS参与奖励";
	public static String boss奖励内容 = "恭喜您在世界BOSS活动中，给BOSS造成伤害排名：@STRING_1@名，请查收附件领取奖励！";
	

	// 任务部分
	public static String text_task_001 = "你的任务列表已经满了";
	public static String text_task_002 = "性别不符";
	public static String text_task_003 = "国家不符";
	public static String text_task_004 = "职业不符";
	public static String text_task_005 = "你的体力值不足";
	public static String text_task_006 = "你的国家职务不符";
	public static String text_task_007 = "你不是这个家族的";
	public static String text_task_008 = "你的家族职务不符";
	public static String text_task_009 = "你的背包位置不足，不能接取任务，可以通过月光宝盒合成来进行清理";
	public static String text_task_010 = "不满足接取时间";
	public static String text_task_011 = "跑环任务放弃后一段时间不允许接取";
	public static String text_task_012 = "你已经接过这个任务了";
	public static String text_task_013 = "你已经接取了任务,不要重复接取";
	public static String text_task_014 = "你已经完成了周期内所有的任务";
	public static String text_task_015 = "前置任务没有完成,不能接取";
	public static String text_task_016 = "任务不存在!";
	public static String text_task_017 = "已经过了可交付时限!";
	public static String text_task_018 = "奖励选择错误";
	public static String text_task_021 = "等级不足,不能接取任务";
	public static String text_task_022 = "你还没有接取这个任务";
	public static String text_task_023 = "你的背包位置不足，不能交付任务，可以通过月光宝盒合成来进行清理";
	public static String text_task_024 = "主线任务不能放弃";
	public static String text_task_025 = "需要选择奖励!";
	public static String text_task_026 = "体力值不足不能接取任务!";
	public static String text_task_027 = "你已经接取了同类型的任务";
	public static String text_task_028 = "只有封印的高手才能接取任务";
	public static String text_task_029 = "你的境界不足，不能接取任务，请去完成境界任务提升境界";
	public static String text_task_030 = "<f>杀死</f>";
	public static String text_task_031 = "<f color='" + TaskConfig.noticeColor + "'>杀死怪物</f>";
	public static String text_task_032 = "<f>得到</f>";
	public static String text_task_036 = "你已经接取了同类任务,不能再接取了";
	public static String text_task_037 = "你已经有护送的NPC了";
	public static String text_task_038 = "由于你的努力, 终于获得了:@STRING_1@";
	public static String text_task_040 = "任务奖励";
	public static String text_task_041 = "恭喜你激活了体力值,并获得了@COUNT_1@点体力值!";
	public static String text_task_042 = "你接取的任务太多,暂时不能接取任务";
	public static String text_task_043 = "任务所需的@STRING_1@数量不足,需要@COUNT_1@个,已有@COUNT_2@个";
	public static String text_task_044 = "任务已过期,请重新刷新!";
	public static String text_task_045 = "正在采集...";
	public static String text_task_046 = "此任务不可放弃";

	// 求购
	public static String text_requestbuy_001 = "不能取消不是你自己的求购";
	public static String text_requestbuy_002 = "求购记录不存在";
	public static String text_requestbuy_003 = "求购";
	public static String text_requestbuy_004 = "求购物品";
	public static String text_requestbuy_005 = "恭喜你成功求购了:";
	public static String text_requestbuy_006 = "银子不足支付求购预存费用和求购收费";
	public static String text_requestbuy_007 = "求购结束";
	public static String text_requestbuy_008 = "求购结束退还您的预存费用";
	public static String text_requestbuy_009 = "需要快速出售的物品不存在";
	public static String text_requestbuy_010 = "绑定物品不能出售给求购";
	public static String text_requestbuy_011 = "出售物品数量不够";
	public static String text_requestbuy_012 = "求购人不存在，不能出售物品给这个求购";
	public static String text_requestbuy_013 = "快速出售物品成功，获得银子:";
	public static String text_requestbuy_014 = "每个人最多只能发布15条求购信息";
	public static String text_requestbuy_015 = "输入数量非法";
	public static String text_requestbuy_016 = "输入金钱非法";
	public static String text_requestbuy_017 = "求购价不能低于最高求购价的一半";
	public static String text_requestbuy_018 = "求购生成失败，请联系GM，并暂时不要使用此功能。";
	public static String text_requestbuy_019 = "求购成功";
	public static String text_requestbuy_020 = "取消求购";
	public static String text_requestbuy_021 = "您确定取消这条求购信息";
	public static String text_requestbuy_022 = "取消求购成功，得到银子";
	public static String text_requestbuy_024 = "出售物品";
	public static String text_requestbuy_025 = "您确定出售此物品";
	public static String text_requestbuy_026 = "VIP等级不足，不能使用此功能！";
	public static String text_requestbuy_027 = "快速出售物品成功，获得银票:";

	// 拍卖
	public static String text_auction_001 = "拍卖成功";
	public static String text_auction_002 = "拍卖失败";
	public static String text_auction_003 = "竞拍成功";
	public static String text_auction_004 = "取消拍卖";
	public static String text_auction_100 = "新建拍卖的竞拍价或求购价输入不正确";
	public static String text_auction_101 = "竞拍价必需大于当前竞拍价的10%";
	public static String text_auction_102 = "竞拍的拍卖纪录不存在，以被人竞拍。";
	public static String text_auction_103 = "不能拍卖自己的拍卖品";
	public static String text_auction_104 = "您已经是最高竞拍者";
	public static String text_auction_105 = "拍卖物品数量不正确";
	public static String text_auction_106 = "拍卖手续费不够";
	public static String text_auction_107 = "竞拍费不够";
	public static String text_auction_108 = "竞拍失败";
	public static String text_auction_109 = "竞拍被超过，退还您银子。";
	public static String text_auction_110 = "拍卖纪录以被取消";
	public static String text_auction_111 = "银子不足，不能取消拍卖";
	public static String text_auction_112 = "10级才能拍卖";
	public static String text_auction_113 = "20级才能拍卖";
	public static String text_auction_114 = "绑定物品不能拍卖";
	public static String text_auction_115 = "您一共只能拍卖15件商品";
	public static String text_auction_116 = "金钱输入不正确";
	public static String text_auction_117 = "此物品已经不足24小时就到期消失了，不能拍卖";
	public static String text_auction_118 = "拍卖生成失败，请联系GM，并暂时不要使用此功能。";
	public static String text_auction_119 = "10级才能买拍卖";
	public static String text_auction_120 = "20级才能买拍卖";
	public static String text_auction_121 = "拍卖者已经不存在了，不能买了";
	public static String text_auction_122 = "不能买绑定物品";
	public static String text_auction_123 = "价钱格式有问题";
	public static String text_auction_124 = "出价不能小于竞拍价";
	public static String text_auction_125 = "拍卖纪录已经不存在";
	public static String text_auction_126 = "拍卖纪录状态已经为结束或过期，不能取消";
	public static String text_auction_127 = "不能取消他人的拍卖纪录";
	public static String text_auction_128 = "取消此拍卖系统将收取";
	public static String text_auction_129 = "的违约金，\n您确定您要取消此拍卖吗?";
	public static String text_不足1小时 = "不足1小时";
	public static String text_小时 = "小时";

	public static String text_fight_noPetMating = "对方在战斗中，不能繁殖";
	public static String text_deal_001 = "交易一方进入战斗取消交易";
	public static String text_deal_002 = "您或对方正忙!";
	public static String text_deal_003 = "玩家物品栏已满，交易无法进行";
	public static String text_deal_004 = "绑定物品不能交易";
	public static String text_deal_005 = "您已经死亡，不能与他人交易";
	public static String text_deal_006 = "您在战斗状态中，不能与他人交易";
	public static String text_deal_007 = "您在摆摊状态下不能交易";
	public static String text_deal_008 = "20级之前不能交易";
	public static String text_deal_009 = "对方在摆摊不能与你交易";
	public static String text_deal_010 = "他已经死亡，不能与人交易";
	public static String text_deal_011 = "他在战斗状态中，不能与人交易";
	public static String text_deal_012 = "您不能与其他国家的人交易";
	public static String text_deal_013 = "交易对象距离太远";
	public static String text_deal_014 = "交易请求以过期";
	public static String text_deal_015 = "输入价格处输入的数额为您交易给对方的银子，请慎重填写价格";
	public static String text_deal_016 = "您或者对方还未锁定，不能同意交易！";
	public static String text_deal_017 = "交易已经不存在了";

	public static String text_marriage_001 = "已同意了你了求婚，快去准备婚礼";
	public static String text_marriage_002 = "拒绝了你的求婚";
	public static String text_marriage_003 = "婚礼举行失败";
	public static String text_marriage_004 = "由于未在婚礼开始时进入婚礼场景或婚礼开始的时候不在线，婚礼被迫停止，你们双方可以再去NPC那设定婚礼延期时间，但只有一次机会了，如果再出现婚礼将无法再举行了!";
	public static String text_marriage_005 = "您的婚礼将于";
	public static String text_marriage_006 = "分钟后举行!\n您现在进入婚礼场景吗？";
	public static String text_marriage_007 = "的婚礼将于";
	public static String text_marriage_008 = "与";
	public static String text_marriage_009 = "您的婚礼现在正式开始,请到NPC处交换戒指。";
	public static String text_marriage_010 = "的婚礼现在正式开始";
	public static String text_marriage_011 = "恭喜";
	public static String text_marriage_012 = "恩爱夫妻情无限，同贺";
	public static String text_marriage_013 = "，白首永相依。经历风雨同患难，时光流逝，真情永不变。";
	public static String text_marriage_014 = "您已结婚了";
	public static String text_marriage_015 = "您只能送花";
	public static String text_marriage_016 = "您只能送糖";
	public static String text_marriage_017 = "对方已结婚了";
	public static String text_marriage_018 = "对方不在线......，请等待对方上线后再设定婚礼相关。";
	public static String text_marriage_019 = "请等待对方选择婚礼规模";
	public static String text_marriage_020 = "先去结婚再来布置婚礼吧";
	public static String text_marriage_021 = "让求婚者来布置婚礼吧";
	public static String text_marriage_022 = "婚礼状态不正确，不能设置婚礼规模";
	public static String text_marriage_023 = "您还未结婚呢";
	public static String text_marriage_024 = "时间最少要1小时最大72小时";
	public static String text_marriage_025 = "对方确定婚礼在";
	public static String text_marriage_026 = "小时后举行，您同意吗?";
	public static String text_marriage_027 = "请您注意！婚礼将在";
	public static String text_marriage_028 = "小时后开始,请不要在缺席，只有这一次延迟";
	public static String text_marriage_029 = "对方不同意您设定的时间";
	public static String text_marriage_030 = "请让您的另一半来确定延迟时间";
	public static String text_marriage_031 = "您只能在婚礼仪式结束后，才能重新设定婚礼时间！";
	public static String text_marriage_032 = "请稍等对方在填写婚礼延期时间";
	public static String text_marriage_033 = "结婚后必须24小时才能离婚。";
	public static String text_marriage_034 = "此婚礼阶段不能离婚。";
	public static String text_marriage_035 = "您确定要结束这段婚姻吗?";
	public static String text_marriage_036 = "您确定要强制结束这段婚姻吗?需要花费:[银子]";
	public static String text_marriage_037 = "对方不在线";
	public static String text_marriage_038 = "确定要结束与您的这段婚姻，您愿意吗?";
	public static String text_marriage_039 = "对方拒绝离婚";
	public static String text_marriage_040 = "对方同意离婚，离婚成功";
	public static String text_marriage_041 = "强制离婚失败，银子不够。";
	public static String text_marriage_042 = "离婚成功";
	public static String text_marriage_043 = "已强制离婚";
	public static String text_marriage_044 = "离婚";
	public static String text_marriage_045 = "强制离婚，解除与您的婚姻关系。";
	public static String text_marriage_046 = "结婚信息不存在";
	public static String text_marriage_047 = "结婚场景不存在";
	public static String text_marriage_048 = "请在自己的结婚场景里取交换戒指";
	public static String text_marriage_049 = "您已经交换过戒指了";
	public static String text_marriage_050 = "婚礼还未开启，不能交换戒指";
	public static String text_marriage_051 = "请新娘新郎距离再近一些再交换戒指";
	public static String text_marriage_052 = "您的戒指呢?如果没保管好请去NPC那领取，需要一定银子。";
	public static String text_marriage_053 = "对方无结婚戒指，如果没保管好请去NPC那领取，需要一定银子";
	public static String text_marriage_054 = "您已经有结婚戒指了，请不要重复领取";
	public static String text_marriage_055 = "您背包满了，请清空一个格子";
	public static String 没有装备饰品 = "您的宠物没有装备饰品！";
	public static String text_marriage_056 = "您的银子不够";
	public static String text_marriage_057 = "的婚礼";
	public static String text_marriage_058 = "玩家不存在或者不在线";
	public static String text_marriage_059 = "同性不能结婚";
	public static String text_marriage_060 = "选择的送花或糖不存在";
	public static String text_marriage_061 = "你是我的心,你是我的肝,你是我四分之三 。";
	public static String text_marriage_062 = "手举";
	public static String text_marriage_063 = "朵";
	public static String text_marriage_064 = "满怀深情的对";
	public static String text_marriage_065 = "说：";
	public static String text_marriage_066 = "就像月亮有引力一样，人与人的相遇也有引力。我们的相遇是命中注定。让我们彼此相伴，共同走过今后的人生。";
	public static String text_marriage_067 = "手捧";
	public static String text_marriage_068 = "块";
	public static String text_marriage_069 = "鼓足勇气对";
	public static String text_marriage_070 = "不能对小于30级的玩家求婚";
	public static String text_marriage_071 = "您等级还不够30级不能求婚";
	public static String text_marriage_072 = "不能向其他国家的玩家求婚";
	public static String text_marriage_073 = "请选择要用来求婚的花";
	public static String text_marriage_074 = "你是要向";
	public static String text_marriage_075 = "求婚并送上";
	public static String text_marriage_076 = "以表示爱慕之意吗？";
	public static String text_marriage_077 = "您请求结婚太频繁了";
	public static String text_marriage_078 = "请求结婚需要";
	public static String text_marriage_079 = "银子,您钱不够";
	public static String text_marriage_080 = "请稍等，对方同意";
	public static String text_marriage_081 = "向您求婚,你愿意吗?";
	public static String text_marriage_082 = "婚礼规模选择错误";
	public static String text_marriage_083 = "您绑银不够";
	public static String text_marriage_084 = "您不用邀请您的配偶";
	public static String text_marriage_085 = "角色已邀请";
	public static String text_marriage_086 = "您不能邀请等级小于10的宾客。";
	public static String text_marriage_087 = "邀请人数已经达到上限";
	public static String text_marriage_088 = "被取消了参加婚礼的资格!";
	public static String text_marriage_089 = "取消了参加婚礼的资格!";
	public static String text_marriage_090 = "对方把你的好友";
	public static String text_marriage_091 = "对方已经选择宾客完成";
	public static String text_marriage_092 = "对方希望重新确定宾客";
	public static String text_marriage_093 = "结婚戒指";
	public static String text_marriage_094 = "这个戒指是在婚礼举行的时候交换戒指用的";
	public static String text_marriage_095 = "您的背包已满，结婚戒指已通过邮件发送，请注意查收";
	public static String text_marriage_096 = "结婚戒指已经放入背包，请妥善保管，举行婚礼的时候需要用到";
	public static String text_marriage_097 = "恭喜2位喜结良缘,婚礼将于";
	public static String text_marriage_098 = "小时候后开始!";
	public static String text_marriage_099 = "喜结良缘,婚礼将于";
	public static String text_marriage_100 = "和";
	public static String text_marriage_101 = "您的好友:";
	public static String text_marriage_102 = "结婚请帖";
	public static String text_marriage_103 = "恭喜结婚完成，结婚戒指您保留好";
	public static String text_marriage_104 = "接下来将每隔一段时间在您的身边刷新出一些礼品给邀请来的嘉宾";
	public static String text_marriage_105 = "的道侣";
	public static String text_marriage_106 = "对方退出了设置婚礼规模界面";
	public static String text_marriage_107 = "对方退出了选择宾客界面";
	public static String text_marriage_108 = "对方退出了设置婚礼举行时间界面";
	public static String text_marriage_109 = "同性不能送花或糖";
	public static String text_marriage_110 = "请选择送花类型";
	public static String text_marriage_111 = "请选择送糖类型";
	public static String text_marriage_112 = "请选择送的数目";
	public static String text_marriage_113 = "送";
	public static String text_marriage_114 = "花";
	public static String text_marriage_115 = "糖";
	public static String text_marriage_116 = "需要花费";
	public static String text_marriage_117 = "送给了";
	public static String text_marriage_118 = "您的结婚信息有问题";
	public static String text_marriage_119 = "使用比翼令出错，您的配偶不存在。";
	public static String text_marriage_120 = "比翼令";
	public static String text_marriage_121 = "您的配偶";
	public static String text_marriage_122 = "用比翼令召唤您过去。";
	public static String text_marriage_123 = "对方取消了您的召唤";
	public static String text_marriage_124 = "重新领取结婚戒指需要花费:[银子]";

	public static String text_marriage_125 = "您已经确定了婚礼相关设定";
	public static String text_marriage_婚礼场景以关闭婚礼结束 = "婚礼场景以关闭，婚礼结束。";
	public static String text_marriage_婚礼由于结婚双方未在线或不在结婚场景中婚礼结束 = "婚礼由于结婚双方未在线或不在结婚场景中，婚礼结束。";
	public static String text_marriage_您的另一半不在线请在她在线的时候布置 = "您的另一半不在线，请在她在线的时候布置";
	public static String text_marriage_结婚 = "结婚";
	public static String text_marriage_邀请角色不存在 = "邀请角色不存在";
	public static String text_marriage_爱的炫舞 = "玄天青金轮(60天)";
	public static String text_marriage_浪漫今生 = "玄天青金轮(60天)";
	public static String text_marriage_以 = "以";
	public static String text_年月日时分 = "yyyy年MM月dd日 HH时mm分";
	public static String text_marriage_同意 = "同意";
	public static String text_marriage_不同意 = "不同意";
	public static String text_marriage_愿意 = "愿意";
	public static String text_marriage_不愿意 = "不愿意";

	public static String text_qiancengta_001 = "您死亡了，实力不足挑战失败，请下次再来。";
	public static String text_qiancengta_002 = "时间到了您未挑战成功，请更加努力。";
	public static String text_qiancengta_003 = "隐藏层";
	public static String text_qiancengta_004 = "层";
	public static String text_qiancengta_005 = "您还有奖励未提取";
	public static String text_qiancengta_006 = ",您确定要刷新吗?";
	public static String text_qiancengta_106 = ",您确定要刷新<f color='0xffff00'>@STRING_1@-@STRING_2@</f>吗?";
	public static String text_qiancengta_007 = "刷新<f color='0xffff00'>@STRING_1@-@STRING_2@</f>需要花费";
	public static String text_qiancengta_008 = "状态不正确,不能自动爬塔";
	public static String text_qiancengta_009 = "道的下标不正确";
	public static String text_qiancengta_010 = "您还未打通这层";
	public static String text_qiancengta_011 = "本道已经达到最高层";
	public static String text_qiancengta_012 = "您此层已经手动爬塔过，不能再手动爬塔，请自动爬塔";
	public static String text_qiancengta_013 = "只能查看自己的爬塔奖励";
	public static String text_qiancengta_014 = "胜利条件:";
	public static String text_qiancengta_015 = "击杀";
	public static String text_qiancengta_017 = "未知";
	public static String text_qiancengta_018 = "击杀所有怪物。";
	public static String text_qiancengta_019 = "坚持到时间结束。";
	public static String text_qiancengta_020 = "失败条件:";
	public static String text_qiancengta_021 = "角色死亡。";
	public static String text_qiancengta_022 = "规定时间内未达到胜利条件。";
	public static String text_qiancengta_023 = "时间：";
	public static String text_qiancengta_024 = "不限";
	public static String text_qiancengta_025 = "分钟内";
	public static String text_qiancengta_026 = "您确定要免费刷新<f color='0xffff00'>@STRING_1@-@STRING_2@</f>吗？";
	public static String text_qiancengta_027 = "层奖励:";
	public static String text_qiancengta_背包或器灵背包已经满了请清理背包 = "背包或器灵背包已经满了，请清理背包。";
	public static String text_qiancengta_恭喜你开启隐藏层 = "恭喜你开启隐藏层！";
	public static String text_qiancengta_您还未打过这道塔不需要刷新 = "您还未打过这道塔，不需要刷新";
	public static String text_qiancengta_刷新超过今日最大上限 = "刷新超过今日最大上限";
	public static String text_qiancengta_刷新需要的银子不够 = "刷新需要的银子不够";
	public static String text_qiancengta_需要 = "需要";
	public static String text_qiancengta_级才能挑战 = "级才能挑战";
	public static String text_qiancengta_隐藏层未开启 = "隐藏层未开启";
	public static String text_qiancengta_爬塔数据不正确 = "爬塔数据不正确";
	public static String[] text_qiancengta_DaoNames = new String[] { "黄泉", "天玄", "阴浊", "阳清", "洪荒", "混沌" };
	public static String text_qiancengta_某难度道未开启 = "必须通过@STRING_1@难度的@STRING_3@道25层才能进入@STRING_2@难度。";
	public static String text_qiancengta_普通 = "普通";
	public static String text_qiancengta_困难 = "困难";
	public static String text_qiancengta_深渊 = "深渊";
	public static String text_qiancengta_难度不存在 = "难度不存在。";
	public static String text_qiancengta_深渊难度不能自动爬塔 = "深渊难度不能自动爬塔。";
	public static String text_qiancengta_开启其他难度塔提示 = "恭喜您已经成功开启@STRING_1@@STRING_2@模式!";
	public static String text_qiancengta_领取过首次奖励 = "您已经领取过本道的首通奖励了。";
	public static String text_qiancengta_您还未打通这道 = "您还未打通这道";
	public static String text_qiancengta_您还未打通上个难度的这道 = "您还未打通上个难度的这道,不能挑战此难度";
	public static String text_qiancengta_您还未打通上一道 = "您还未打通上一道,不能挑战此道";
	public static String text_qiancengta_VIP等级不够充值 = "VIP：可以额外<f color='0x00ff00'>增加刷新次数哦</f>,亲还等什么呢马上成为VIP吧。";

	public static String[] text_sifang_BossNames = new String[] { "圣兽青龙", "圣兽朱雀", "圣兽白虎", "圣兽玄武", "圣兽麒麟" };
	public static String[] text_fifang_infoNames = new String[] { "青龙", "朱雀", "白虎", "玄武", "麒麟" };
	public static String text_sifang_001 = "五方圣兽家族争霸战即将在";
	public static String text_sifang_002 = "分钟后进场，请相关人员做好准备";
	public static String text_sifang_003 = "五方圣兽争霸赛正式开始，大家为他们加油吧";
	public static String text_sifang_004 = "五方圣兽争霸战";
	public static String text_sifang_005 = "圣地争夺战已经可以进场，将于";
	public static String text_sifang_006 = "分钟后正式开始，请尽快进场";
	public static String text_sifang_007 = "您没有家族，不能做此操作";
	public static String text_sifang_008 = "报名还未开始或已经结束";
	public static String text_sifang_009 = "您不是族长，不能操作";
	public static String text_sifang_010 = "您还未报名参加呢";
	public static String text_sifang_011 = "他已经被选入了";
	public static String text_sifang_012 = "只能选择10个人参赛";
	public static String text_sifang_013 = "您没有在五方圣兽中";
	public static String text_sifang_014 = "您的家族没有报名此比赛";
	public static String text_sifang_015 = "您没有被邀请参加这个比赛";
	public static String text_sifang_016 = "还未开放进入";
	public static String text_sifang_017 = "您的家族没有占领这个圣地";
	public static String text_sifang_018 = "比赛正在进行中，不能进入圣地";
	public static String text_sifang_019 = "报名已经满了，只能有10个家族报名";
	public static String text_sifang_020 = "您家族等级不够不能参加，必需要2级";
	public static String text_sifang_021 = "家族资金不够，不能参加";
	public static String text_sifang_022 = "您的家族已经报名";
	public static String text_sifang_023 = "报名五方圣兽";
	public static String text_sifang_024 = "是否消耗";
	public static String text_sifang_025 = "家族资金参加五方圣兽";
	public static String text_sifang_026 = "的争夺战";
	public static String text_sifang_027 = "您的家族报名成功。消耗家族资金";
	public static String text_sifang_028 = "成功击杀";
	public static String text_sifang_029 = "，掉落物品已存放于家族仓库";
	public static String text_sifang_030 = "，请族长进行分配。";
	public static String text_sifang_031 = "恭喜";
	public static String text_sifang_032 = "家族获得";
	public static String text_sifang_033 = "圣地的控制权。";

	public static String text_boothsale_001 = "留言";
	public static String text_boothsale_002 = "交易记录";
	public static String text_boothsale_003 = "摊位信息不存在";
	public static String text_boothsale_004 = "您摆摊的物品发生变化，请重新打开摆摊界面摆摊";
	public static String text_boothsale_005 = "摆摊的东西不存在";
	public static String text_boothsale_006 = "摆摊的物品不能是绑定的";
	public static String text_boothsale_007 = "此物品和真实摆摊物品不一致，不能购买";
	public static String text_boothsale_008 = "绑定物品不能买";
	public static String text_boothsale_009 = " 你售给了";
	public static String text_boothsale_010 = "获得";
	public static String text_boothsale_011 = "您已经把这个物品放到摆摊上了";
	public static String text_boothsale_012 = "摆摊请不要移动";

	public static String text_肩负使命 = "仙门再临";
	public static String text_再救苍生 = "立誓成仙";
	public static String text_喜获神兵 = "神兵相助";
	public static String text_宝甲护身 = "攻守兼备";
	public static String text_支援同道 = "相助如花";
	public static String text_风林火山 = "牛刀小试";
	public static String text_证明实力 = "试练伊始";
	public static String text_锦囊妙计 = "继续前行";
	public static String text_一技傍身 = "青龙传法";
	public static String text_助拳修罗 = "白虎授药";
	public static String text_义救仙心 = "朱雀试练";
	public static String text_驰援影魅 = "协助玄武";
	public static String text_营救九黎 = "庚金之阵";
	public static String text_清理爪牙 = "乙木之阵";
	public static String text_美色诱惑 = "葵水之阵";
	public static String text_险成大错 = "离火梧桐";
	public static String text_恭候大驾 = "戌土指引";
	public static String text_剿灭狐妖 = "剿灭幻境";

	// 新职业
	public static String text_清理谷口 = "清理谷口";
	public static String text_宰牛用刀 = "宰牛用刀";
	public static String text_修复阵法 = "修复阵法";
	public static String text_禀明情况 = "禀明情况";
	public static String text_有备无患 = "有备无患";
	public static String text_喜得法术 = "喜得法术";
	public static String text_寻觅树灵 = "寻觅树灵";
	public static String text_寻找兽王 = "寻找兽王";
	public static String text_大展拳脚 = "大展拳脚";
	public static String text_援助乡邻 = "援助乡邻";
	public static String text_再收宝甲 = "再收宝甲";
	public static String text_收获神兵 = "收获神兵";
	public static String text_戾气渐染 = "戾气渐染";
	public static String text_清幽山谷 = "清幽山谷";

	public static String 挂机商店 = "挂机商店";
	public static String 地图不存在 = "地图不存在";
	public static String 指定的角色与链接角色不匹配 = "指定的角色与链接角色不匹配";
	public static String 地图中已经达到最大的人数 = "地图中已经达到最大的人数";
	public static String 新手村 = "新手村";

	public static String 提交异常稍后再试 = "提交异常,稍后再试";
	public static String 通信异常请稍后再试 = "通信异常,请稍后再试!";
	public static String 此设备今日给过多的帐号充过值请改日再充值 = "此设备今日给过多的帐号充过值，请改日再充值";
	public static String 您的等级过低请提升等级至10级后再进行充值 = "您的等级过低,请提升等级至10级后再进行充值";
	public static String 您已经到当前等级的充值上限请升级或者改日再充值 = "您已经到当前等级的充值上限，请升级或者改日再充值";

	public static String 您已经领取过此奖励 = "您已经领取过此奖励";
	public static String 您充值金额不够不能领取此奖励 = "您充值金额不够不能领取此奖励";
	public static String 等级不符 = "您的等级不符，快快升级吧！";
	public static String 合服奖励 = "恭喜您获得了奖励！";

	public static String 充值状态 = "充值状态:";
	public static String 订单编号 = "订单编号:";
	public static String 充值时间 = "充值时间:";
	public static String 非法操作 = "非法操作!";
	public static String 充值申请提交成功 = "充值申请提交成功!";
	public static String 无效的充值 = "无效的充值!";
	public static String 充值失败请联系GM = "充值失败，请联系GM";
	public static String 充值完成请稍候查询充值结果 = "充值完成，请稍候查询充值结果";
	public static String 无任何充值方式请联系GM = "无任何充值方式!请联系GM!";
	public static String 无匹的充值信息请联系GM = "无匹配充值信息!请联系GM!";
	public static String 暂不开放 = "暂不开放";
	public static String 设置成功 = "设置成功";
	public static String 设置失败 = "设置失败，未输入答案";
	public static String 时间不符 = "时间不符";
	public static String 不存在的一级分类 = "不存在的一级分类:";
	public static String 不存在的二级分类 = "不存在的二级分类:";
	public static String 系统中不存在任何成就 = "系统中不存在任何成就";
	public static String 无效的活动 = "无效的活动";
	public static String 你的背包位置不足不能领取 = "你的背包位置不足,不能领取";

	public static String 打探消息 = "打探消息";
	public static String 边去忙着呢 = "边去，忙着呢";
	public static String 您在五方圣兽活动中不能原地复活 = "您在五方圣兽活动中，不能原地复活";

	public static String 后台 = "后台";

	public static String PICK_CAVE_PLANT = "摘取庄园果实";
	public static String PET_SEAL = "宠物封印";

	public static String text_同等级的只能有一个坐骑 = "本尊或元神只能拥有一只陆地坐骑";
	public static String 已经有相同一个坐骑 = "已经有相同一个坐骑，不能在生成坐骑";
	public static String text_喂马一天只能喂养两次 = "一天只能喂养两次";
	public static String text_马体力值满 = "无需喂养";

	// 社交
	public static String 添加黑名单失败 = "添加黑名单失败，好友已达到上限";
	public static String 添加好友失败 = "添加好友失败，好友已达到上限";
	public static String text_设置个人资料成功 = "设置个人资料成功";
	public static String text_不是一个国家不能添加好友 = "不是一个国家不能添加好友";
	public static String text_xx已经关注了你你是否关注他 = "@PLAYER_NAME_1@已经关注了你，你是否关注他(她)";
	public static String text_你拒绝了关注xx = "你拒绝了关注";
	public static String 你拒绝了关注AA = "你拒绝了关注@STRING_1@";
	public static String text_不能操作自己 = "不能添加自己";
	public static String text_已经是好友 = "已经是好友";
	public static String text_关注好友成功 = "关注好友成功";
	public static String text_删除好友成功 = "删除好友成功";
	public static String text_没有这个好友 = "没有这个好友";
	public static String text_已经是黑名单 = "已经是黑名单";
	public static String text_添加黑名单成功 = "添加黑名单成功";
	public static String text_删除黑名单成功 = "删除黑名单成功";
	public static String text_删除仇人成功 = "删除仇人成功";
	public static String text_好友 = "好友";
	public static String text_结义兄弟 = "结义兄弟";
	public static String text_师傅 = "师傅";
	public static String text_徒弟 = "徒弟";
	public static String text_丈夫 = "丈夫";
	public static String text_妻子 = "妻子";
	public static String 关系xx名字xx上线了 = "您的@STRING_1@@STRING_2@上线了";
	public static String 关系xx名字xx下线了 = "您的@STRING_1@@STRING_2@下线了";

	// 聊天群组
	public static String text_拒绝了你的入群申请 = "@PLAYER_NAME_1@群拒绝了你的入群申请";

	// 组队
	public static String 队伍解散了 = "队伍解散了";
	public static String 组队进队方式设置为xx = "组队进队方式设置为:@STRING_1@";
	public static String 自动进队 = "自动进队";
	public static String 需要验证 = "需要验证";
	public static String 您邀请的玩家已离线 = "您邀请的玩家已离线";

	// 结义
	public static String 结义 = "结义";
	public static String 结义消耗 = "结义消耗";
	public static String 加入结义消耗 = "加入结义消耗";
	public static String text_确定加入结义 = "确定加入结义";
	public static String text_确定加入结义消耗提示 = "加入结义需要消耗@COUNT_1@两，你确定加入结义吗？";
	public static String text_队长确定结义 = "队长确定结义";
	public static String text_队长确定结义消耗提示 = "结义需要消耗@COUNT_1@两，你确定结义吗？";
	public static String text_你同意了结义 = "你同意了结义";
	public static String text_你结义中的人都不在线 = "你结义中的人都不在线或者暂时无法接受邀请";
	public static String text_你包里没有兄弟令 = "你没有兄弟令";
	public static String text_有人还未同意结义 = "有人还未同意结义";
	public static String text_结义确定 = "结义确定";
	public static String text_结义队伍发生变化 = "结义队伍发生变化";
	public static String text_结义要队长申请 = "必须是队长申请";
	public static String text_结义申请提示 = "您确定要和队伍中的朋友义结金兰吗？";
	public static String text_xxs义结金兰人送外号 = "@STRING_1@义结金兰，人送外号@STRING_2@！！";

	public static String 你还没有称号 = "你还没有称号";
	public static String text_结义消耗银子不足 = "结义需要花费@COUNT_1@两银子，您的银子不足。";
	public static String text_结义完成 = "结义完成";
	public static String text_结义名不能为空 = "结义名不能为空";
	public static String text_结义名太长 = "结义名太长";
	public static String text_个人称号不能为空 = "个人称号不能为空";
	public static String text_结义个人称号太长 = "结义个人称号太长";
	public static String text_结义个人称号有禁用字 = "结义个人称号有禁用字";
	public static String text_结义个人称号重复 = "结义个人称号重复";
	public static String text_结义名称有禁用字 = "结义名称有禁用字";
	public static String text_结义名称重复 = "结义名称重复";
	public static String text_你拒绝了结义 = "你拒绝了结义";
	public static String text_结义必须要在组队中 = "结义必须要在组队中";
	public static String xx结义者距离太远 = "@STRING_1@距离太远";
	public static String text_结义者距离太远 = "距离太远";
	public static String text_结义者有其他结义 = "有其他结义";
	public static String xx结义者有其他结义 = "@STRING_1@有其他结义";
	public static String text_结义者等级不够 = "等级不够 ";
	public static String xx结义者等级不够 = "@STRING_1@等级不够 ";
	public static String text_结义者不属于同一国家 = "不属于同一国家";

	public static String xx结义者不属于同一国家 = "@STRING_1@不属于同一国家";
	public static String text_等待其他兄弟确认 = "等待其他兄弟确认。。。。";
	public static String text_有兄弟还未考虑清楚 = "有兄弟还未考虑清楚，结义未完成";

	public static String text_队长正在为咱们起个响亮的名头请稍后 = "队长正在为咱们起个响亮的名头，请稍后";
	public static String text_xx结义还未考虑清楚结义失败 = "@STRING_1@还未考虑清楚结义失败";
	public static String text_退出结义 = "退出结义";
	public static String text_您确定要和兄弟姐妹绝交吗 = "兄弟一生一世，您确定要和兄弟/姐妹绝交吗？";
	public static String text_你离开了xx结义 = "你离开了@STRING_1@结义";
	public static String text_xx离开了结义 = "@STRING_1@离开了结义";
	public static String 结义解散了 = "结义解散了";
	public static String xx结义解散了 = "@STRING_1@结义解散了";
	public static String text_加入结义 = "加入结义";
	public static String text_xx想加入结义 = "@PLAYER_NAME_1@想加入结义";
	public static String text_成员退出结义邮件 = "成员退出结义邮件";
	public static String text_结义解散邮件 = "结义解散邮件";
	public static String text_使用了兄弟令 = "是否召集结义成员？你还可以输入一句:";
	public static String 兄弟令 = "兄弟令";
	public static String text_等待其他结义玩家同意 = "等待其他结义玩家同意";
	public static String text_xx加入了结义 = "@PLAYER_NAME_1@加入了结义";
	public static String text_你加入了结义 = "你加入了结义";
	public static String text_你还没有结义 = "你还没有结义";
	public static String text_你已经有结义 = "你已经有结义";
	public static String text_队伍中有两个结义 = "队伍中有两个结义";
	public static String text_结义玩家有不在线 = "结义玩家有不在线";
	public static String text_结义玩家人数已达最大 = "结义玩家人数最大是20个，已达到最大";
	public static String text_加入结义你没有队伍 = "你没有队伍";
	public static String text_加入结义队伍中没有结义 = "队伍中没有结义";
	public static String text_你同意xx加入结义 = "你同意@STRING_1@加入结义";
	public static String text_xx同意你加入结义 = "@STRING_1@同意你加入结义";
	public static String text_xx拒绝了xx加入结义 = "@STRING_1@拒绝了@STRING_2@加入结义";
	public static String text_你拒绝了xx加入结义 = "你拒绝了@STRING_1@加入结义";
	public static String text_xx拒绝了你加入结义 = "@STRING_1@拒绝了你加入结义";
	public static String 退出了结义 = "退出了结义";
	public static String xx退出了结义 = "@STRING_1@退出了结义";

	// 传功
	public static String text_包裹空间不足 = "包裹空间不足";
	public static String text_传功经验 = "传功经验";
	public static String text_一天只能领取一次 = "一天只能领取一次，你已经领取了";
	public static String text_得到传功石 = "得到传功石";
	public static String text_传功石 = "传功石";
	public static String text_传功申请 = "传功申请";
	public static String text_xx请求与你传功 = "@PLAYER_NAME_1@请求与你传功";
	public static String text_你拒绝了xx的传功请求 = "你拒绝了@PLAYER_NAME_1@的传功请求";
	public static String text_xx拒绝了你的传功请求 = "@PLAYER_NAME_1@拒绝了你的传功请求";
	public static String text_对方级别比你高不能传功 = "对方级别比你高,不能传功";
	public static String text_传功必须是同一个国家的俩个人 = "传功必须是同一个国家的俩个人";
	public static String text_自己正在传功 = "自己正在传功";
	public static String text_对方正在传功 = "对方正在传功";
	public static String text_每天只能传功三次你已经完成 = "每天只能传功三次，你已经完成";
	public static String text_对方今天已经接受过传功 = "对方今天已经接受过传功";
	public static String text_你没有传功石 = "你没有传功石";
	public static String text_对方级别比你低不能接受传功 = "对方级别比你低,不能接受传功";
	public static String text_每天只能传功三次对方已经完成 = "每天只能传功三次,对方已经完成";
	public static String text_自己今天已经接受过传功 = "自己今天已经接受过传功";
	public static String text_对方没有传功石 = "对方没有传功石";
	public static String text_自己红名不能进行传功 = "自己红名,不能进行传功";
	public static String text_对方红名不能进行传功 = "对方红名,不能进行传功";
	public static String text_对方红名不能接受传功 = "对方红名,不能接受传功";
	public static String text_自己红名不能接受传功 = "对方红名,不能接受传功";
	public static String text_传功必须要在指定区域自己不在指定区域 = "传功必须要在指定区域@STRING_1@,自己不在指定区域";
	public static String text_传功必须要在指定区域对方不在指定区域 = "传功必须要在指定区域@STRING_1@,对方不在指定区域";

	// 仙缘
	public static String text_仙缘经验 = "仙缘经验";
	public static String 对方活动已经开始 = "对方活动已经开始";
	public static String 对方活动已经完成 = "对方活动已经完成";
	public static String 包裹已满 = "包裹已满";
	public static String text_仙缘 = "等红烛燃尽,你就能完成仙缘任务了";
	public static String text_论道 = "等宴席结束,你就能完成论道任务了";
	public static String CREATE_REASON_FATEACTIVITY = "仙缘经验";
	public static String text_请求仙缘描述 = "人海茫茫,恭喜你找到有缘之人@PLAYER_NAME_1@";
	public static String 显示重选key = "@PLAYER_NAME_1@已接受你的邀请，请去@STRING_1@国新手出生地的凤栖梧桐下找她，并使用包裹里的@ARTICLE_NAME_1@</f><f>(用法:双击完成)</f>\n<f color='#ffff00'>注：每次点击【召唤仙友】都会消耗对应数量的仙缘论道令！";
	public static String 显示重选key2 = "@PLAYER_NAME_1@已接受你的邀请，请去@STRING_1@国新手出生地的凤栖梧桐下找他，并使用包裹里的@ARTICLE_NAME_1@</f><f>(用法:双击完成)</f>\n<f color='#ffff00'>注：每次点击【召唤仙友】都会消耗对应数量的仙缘论道令!";
	public static String 显示放弃key = "请到@STRING_1@国新手出生地的凤栖梧桐下跟@PLAYER_NAME_1@进行仙缘活动";
	public static String 显示放弃key2 = "请到@STRING_1@国新手出生地的凤栖梧桐下跟@PLAYER_NAME_1@进行论道活动";
	public static String 国内仙缘keyString = "人海茫茫，恭喜你找到有缘人@PLAYER_NAME_1@,你想送她什么？";
	public static String 国内仙缘LevelContent0 = "五彩香菱(15点体力)";
	public static String 国内仙缘LevelContent1 = "同心玉佩(60点体力)";
	public static String 国内仙缘LevelContent2 = "苏杭雅扇(100点体力，4倍经验)";
	public static String 国内仙缘RealInviteKeyString = "@PLAYER_NAME_1@邀请你做仙缘任务";
	public static String 国内论道LevelContent0 = "青铜酒樽(15点体力)";
	public static String 国内论道LevelContent1 = "稀世宝剑(60点体力)";
	public static String 国内论道LevelContent2 = "名贵古画(100点体力，4倍经验)";
	public static String 国内论道RealInviteKeyString = "@PLAYER_NAME_1@邀请你做论道任务";
	public static String 你同意xx论道请求去凤栖梧桐去找他 = "你同意了@PLAYER_NAME_1@的请求，请到凤栖梧桐去找他";
	public static String xx同意你论道请求去凤栖梧桐去找他 = "@PLAYER_NAME_1@同意了你的请求,请到凤栖梧桐去找他";

	public static String 你同意xx仙缘请求去凤栖梧桐去找她 = "你同意了@PLAYER_NAME_1@的请求，请到凤栖梧桐去找她";
	public static String xx同意你仙缘请求去凤栖梧桐去找她 = "@PLAYER_NAME_1@同意了你的请求,请到凤栖梧桐去找她";
	public static String xx同意你仙缘请求去凤栖梧桐去找她2 = "@STRING_1@同意了你的请求,并已到达凤栖梧桐下,请速到凤栖梧桐去找她";
	public static String 任务包裹已满 = "任务包裹已满";
	public static String 邀请方不在线 = "邀请方不在线";
	public static String 对方已经放弃此活动或已完成 = "对方已经放弃此活动或已完成";
	public static String 你正在进行活动 = "你正在进行活动";

	// 篝火
	public static String 您级别高于xx级不能使用 = "您级别高于@COUNT_1@级不能使用";

	// 寻宝
	public static String 寻宝 = "寻宝";
	public static String text_寻宝经验 = "寻宝经验";
	public static String text_寻宝描述 = "@PLAYER_NAME_1@要与你进行交换，你同意吗?";
	public static String text_兑换0 = "你没有可兑换的定海神珠左或真●定海神珠左";
	public static String text_兑换1 = "你没有可兑换的定海神珠右或真●定海神珠右";
	public static String text_兑换2 = "你没有可兑换的镇海神珠上或真●镇海神珠上";
	public static String text_兑换3 = "你没有可兑换的镇海神珠下或真●镇海神珠下";
	public static String text_兑换4 = "你没有可兑换的定海神珠或真●定海神珠";
	public static String text_兑换5 = "你没有可兑换的镇海神珠或真●镇海神珠";
	public static String 铲子 = "洛阳铲";
	public static String 藏宝图 = "藏宝图";
	public static String 国内寻宝物品 = "国内寻宝物品";
	public static String 国外寻宝物品 = "国外寻宝物品";

	// 组队
	public static String text_你拒绝了xx加入队伍 = "你拒绝了@STRING_1@加入队伍";
	public static String text_设置队伍分配规则错误 = "你不是队长不能设置分配规则";
	public static String text_设置队伍分配成功 = "设置队伍分配成功";
	public static String text_设置默认队伍分配成功 = "设置默认队伍分配成功";
	public static String text_队长把队伍的分配规则设置为xx = "队长把队伍的分配规则设置为:@STRING_1@";

	// 答题活动
	public static String 恭喜你在xx答题中获得的xx名次得到xx = "恭喜你在@STRING_1@答题中获得第@COUNT_1@名得到@ARTICLE_NAME_1@";
	public static String text_答题奖励 = "答题奖励";
	public static String text_答题经验 = "答题经验";
	public static String text_答题活动 = "答题活动";
	public static String text_参加答题活动 = "你是否参加本次答题活动?";
	public static String text_你是否接受答题活动 = "答题活动将在@STRING_1@分钟开始，你是否接受答题活动";
	public static String text_你是否接受答题活动秒开始 = "答题活动将在@STRING_1@秒后开始，你是否接受答题活动";
	public static String text_你是否接受答题活动正在进行 = "答题活动正在进行，你是否接受答题活动";
	public static String text_答题接受 = "接受";
	public static String text_答题拒绝 = "拒绝";
	public static String text_关闭答题活动 = "关闭答题活动";
	public static String text_答题活动已经结束 = "答题活动已经结束";
	public static String 恭喜玩家姓名获得本次答题的xx = "<f color='@COLOR_1@'>恭喜@PLAYER_NAME_1@获得本次答题的@STRING_1@，获得@ARTICLE_NAME_1@</f>";
	public static String 本次答题结束您获得了第xx名 = "本次答题结束，您获得了第@COUNT_1@名。";
	public static String 你同意了此次答题活动 = "你同意了此次答题活动";
	public static String 你拒绝了此次答题活动 = "你拒绝了此次答题活动";

	public static String 恭喜你获得答题额外奖励 = "恭喜您在额外开启的答题活动中答对%s道题,获得额外奖励";
	// 养蚕活动
	public static String text_feed_forluck = "兑换祝福丹";
	public static String text_feed_silvercar = "运镖";
	public static String text_体力值 = "耗费体力值";
	public static String text_peekAndBrick = "刺探和偷砖";
	public static String text_feed_silkworm_001 = "养蚕任务已发布!";
	public static String text_feed_silkworm_002 = "你没有抽到果子";
	public static String text_feed_silkworm_003 = "数据异常请联系GM:CODE=@STRING_1@";
	public static String text_feed_silkworm_004 = "只能在本家族操作!";
	public static String text_feed_silkworm_005 = "你没有这个物品!";
	public static String text_feed_silkworm_006 = "物品不足!";
	public static String text_feed_silkworm_007 = "每次只能兑换一个";
	public static String text_feed_silkworm_008 = "请放入正确的物品";
	public static String text_feed_silkworm_009 = "今天兑换次数过多!";
	public static String text_feed_silkworm_010 = "操作成功!";
	public static String text_feed_silkworm_012 = "你的物品不足,只有:@COUNT_1@个";
	public static String text_feed_silkworm_013 = "家族福利";
	public static String text_feed_silkworm_014 = "家族资金不足,不能发布养蚕任务,需要家族资金:@STRING_1@";
	public static String text_feed_silkworm_015 = "饲养任务开始";
	public static String text_feed_silkworm_016 = "@STRING_1@刚刚发布饲养任务.是否前往!";

	// 个人洞府
	public static String text_cave_001 = "操作成功";
	public static String text_cave_002 = "位置未找到";
	public static String text_cave_003 = "仙境不存在";
	public static String text_cave_004 = "国家不符";
	public static String text_cave_005 = "已经有人捷足先登了,再换个试试";
	public static String text_cave_006 = "你已经有仙府了,不能再申请了";
	public static String text_cave_007 = "你没有仙府";
	public static String text_cave_008 = "你的仙府没有在封印状态,不需要解封";
	public static String text_cave_009 = "异常!错误代码:c009";
	public static String text_cave_010 = "只能操作自己的洞府";
	public static String text_cave_011 = "建筑已经满级了";
	public static String text_cave_012 = "建筑不能升级";
	public static String text_cave_013 = "资源不足,不能操作";
	public static String text_cave_014 = "需要主建筑达到@STRING_1@级才可升级";
	public static String text_cave_015 = "建筑正在升级,不能操作";
	public static String text_cave_016 = "只能操作自己家的建筑";
	public static String text_cave_017 = "只能在土地上操作";
	public static String text_cave_018 = "该土地已经开垦了";
	public static String text_cave_019 = "田地数量达到上限了,需要主建筑达到@STRING_1@级";
	public static String text_cave_020 = "开垦土地所需的道具不足,需要@STRING_1@@STRING_2@个,已有@STRING_3@个";
	public static String text_cave_021 = "所选植物不存在";
	public static String text_cave_022 = "所选土地不存在";
	public static String text_cave_023 = "还有作物未收获";
	public static String text_cave_024 = "只能在土地上种植";
	public static String text_cave_025 = "土地等级不足 ";
	public static String text_cave_026 = "非法操作";
	public static String text_cave_027 = "你没有对应的果实";
	public static String text_cave_028 = "选择的物品不能兑换资源";
	public static String text_cave_029 = "异常!错误代码:c029";
	public static String text_cave_030 = "你的物品数量不足";
	public static String text_cave_031 = "资源已达到当前等级最高,不能再升级了";
	public static String text_cave_032 = "当前没有可用队列";
	public static String text_cave_033 = "非法操作";
	public static String text_cave_034 = "出战状态的宠物不能操作";
	public static String text_cave_035 = "挂机状态的宠物不能操作";
	public static String text_cave_036 = "仓库位已满";
	public static String text_cave_037 = "挂机位已满";
	public static String text_cave_038 = "只能操作宠物";
	public static String text_cave_039 = "宠物不存在";
	public static String text_cave_040 = "宠物不属于你";
	public static String text_cave_041 = "宠物没有在挂机";
	public static String text_cave_042 = "作物不存在";
	public static String text_cave_043 = "食物";
	public static String text_cave_044 = "木材";
	public static String text_cave_045 = "石料";
	public static String text_cave_046 = "特殊";
	public static String text_cave_047 = "仙府不存在";
	public static String text_cave_048 = "你的仙府在封印状态";
	public static String text_cave_050 = "果实还没有成熟";
	public static String text_cave_051 = "你摘的太多了,换一家坑吧";
	public static String text_cave_052 = "你太贪心了,给主人留点吧";
	public static String text_cave_053 = "你的背包满了，你可以通过月光宝盒合成来进行清理";
	public static String text_cave_054 = "您的仙府不在这里,无法回家";
	public static String text_cave_055 = "已经成熟不能取消";
	public static String text_cave_056 = "田地需要开垦才能升级";
	public static String text_cave_057 = "资源超过上限";
	public static String text_cave_058 = "超出最大同时种植上限";
	public static String text_cave_059 = "获得绑银:@STRING_1@";
	public static String text_cave_060 = "不是自己仙府不能操作";
	public static String text_cave_061 = "不能在自己家挂机";
	public static String text_cave_062 = "田地数量达到上限了,需要升级主建筑";
	public static String text_cave_063 = "资源已达到当前等级最高,升级仓库可再次升级资源储量";
	public static String text_cave_064 = "庄园建筑@STRING_1@-@STRING_2@级,升级成功.当前有空余队列,可升级其他建筑.";
	public static String text_cave_065 = "庄园粮食储量-@STRING_1@级,升级成功";
	public static String text_cave_066 = "庄园木材储量-@STRING_1@级,升级成功";
	public static String text_cave_067 = "庄园石料储量-@STRING_1@级,升级成功";
	public static String text_cave_068 = "粮食";
	public static String text_cave_069 = "木材";
	public static String text_cave_070 = "石料";
	@DonotTranslate
	public static String[] text_cave_071 = { text_cave_068, text_cave_069, text_cave_070 };
	public static String text_cave_072 = "驱赶挂机宠物";
	public static String text_cave_073 = "开门消耗绑银@STRING_1@";
	public static String text_cave_074 = "收获洞府摇钱树";
	public static String text_cave_075 = "任意果实";
	public static String text_cave_077 = "使用@STRING_1@增加@COUNT_1@个建筑队列,持续@COUNT_2@";
	public static String text_cave_078 = "使用@STRING_1@";
	public static String text_cave_079 = "增加队列执行中...请勿重复使用";
	public static String text_cave_080 = "你背包里没有@STRING_1@";
	public static String text_cave_1081 = "操作成功,队列增加剩余时间:@STRING_1@";
	public static String text_cave_082 = "增加队列已耗尽,赶快补充";
	public static String text_cave_083 = "剩余时间:";
	public static String text_cave_084 = "暂无空闲仙府!";
	public static String text_cave_085 = "该位置已被占用!";
	public static String text_cave_086 = "只能在自己国家的仙府使用";
	public static String text_cave_087 = "你成功的使用了@COUNT_1@个@STRING_1@开启了@STRING_2@";
	public static String text_cave_088 = "你的宠物栏已满!";
	public static String text_cave_089 = "不是你的宠物不能操作";
	public static String text_cave_090 = "所选宠物不存在";
	public static String text_cave_091 = "该位置还未开启";
	public static String text_cave_092 = "宠物不能在自己家挂机";
	public static String text_cave_093 = "你的等级太低需要@COUNT_1@级才能创建仙府";
	public static String text_cave_094 = "你今天种植这类树太多了,歇歇吧,明天再来";
	public static String text_cave_095 = "系统繁忙,创建失败,请稍后再试!";
	public static String text_cave_096 = "解封仙府成功,你的新仙府在@STRING_1@";
	public static String text_cave_097 = "暂无记录";
	public static String text_cave_098 = "你的仙府没有封印";
	public static String text_cave_099 = "你今天偷取的果实太多了,明天再偷吧!";
	public static String text_cave_100 = "无效的进度!";
	public static String text_cave_101 = "你没有@STRING_1@,不能操作,试试打开商城找找!";
	public static String text_cave_102 = "加速.";
	public static String text_cave_103 = "你确定使用一个@STRING_1@,加速@STRING_2@么?";
	public static String text_cave_104 = "开垦田地";
	public static String text_cave_105 = "你的@STRING_1@不足@COUNT_1@个，是否使用@COUNT_2@个@STRING_1@并消耗@STRING_3@银子补齐不足的@STRING_4@数量开启田地";
	public static String text_cave_106 = "你的@STRING_1@不足";
	public static String text_cave_107 = "你的银子不足";
	public static String text_cave_108 = "你的VIP等级不足,不能加速,需要VIP等级:@COUNT_1@.";
	public static String text_cave_109 = "洞府不存在";
	public static String text_cave_110 = "资源等级已满";
	public static String text_cave_111 = "<f>你确定要提升@STRING_1@等级到@COUNT_1@级?</f>\n消耗资源:@STRING_2@";
	public static String text_cave_112 = "开门消耗银子@STRING_1@";
	public static String 仓库等级不足 = "仓库等级不足";
	public static String 刷新失败 = "刷新失败";

	// 境界
	public static String text_bourn_001 = "刷新任务";
	public static String text_bourn_002 = "境界已达到最高级别";
	public static String text_bourn_003 = "经验不足不能升级";
	public static String text_bourn_004 = "等级不足不能升级";
	public static String text_bourn_005 = "任务未完成不能升级";
	public static String text_bourn_006 = "境界不足不能打坐，请去完成境界任务提升境界";
	public static String text_bourn_007 = "剩余打坐时间不足";
	public static String text_bourn_008 = "当前经验不足不能刷新任务";
	public static String text_bourn_009 = "今天完成任务已达到上限不能再刷新了";
	public static String text_bourn_010 = "你的银子不足,不能刷新";
	public static String text_bourn_011 = "<f color='0x78f4ff'>说明：</f>\n<f size='28'>1.每天可接受不同的杂念任务清除杂念从而获得修炼值。</f>\n<f size='28'>2.杂念修炼次数为1-5星，可通过消耗灵气或银子刷新修炼速度，完成的任务获得修炼值。</f>\n<f size='28'>3.在获得杂念任务时，有几率触发心魔任务。心魔任务为6星，接受后刷新心魔，限定时间</f>";
	public static String[] text_bourn_012 = { "<f size='28'>炼气化淤真神在，通晓阴阳镇鬼怪。依法锤炼之凡人，可度万世皆不坏。</f>", 
		"<f size='28'>百日筑基忌急躁，循序渐进起高楼。堵漏补缺静吐纳，退符祛病成大妙。</f>", 
		"<f size='28'>开光如玉出磨砺，心态澄明静冥想。慧眼存真站心魔，识得众生辨鱼龙。</f>", 
		"<f size='28'>内息凝化结金丹，通体脆弱畏风寒。聚清排浊造化起，欲出分身一线天。</f>", 
		"<f size='28'>日精月华汇中天，五行施法碎内丹。苦修觅得元神处，孵化元婴现世间。</f>", 
		"<f size='28'>神州魂游达四海，世间百态俱通灵。历尽沧桑观山河，守得云开见月明。</f>", 
		"<f size='28'>合体尚须问清浊，二气争锋分自我。寻得慧光破迷雾，弃恶扬善证摩诃。</f>", 
		"<f size='28'>求仙坎坷不踟蹰，人间正道有歧路。五雷轰顶如醍醐，金刚不坏登坦途。</f>", 
		"<f size='28'>无生无死亦无怖，有天有地又有仙。身居高处不胜寒，古往今来转瞬间。</f>", 
		"<f size='28'>天人交战阿修罗，鬼畜地狱生活佛。六道轮回飞升去，位列仙班谱传说。</f>", 
		"<f size='28'>一波未平一波起，万钧自有万钧力。闲看真绝云烟里，常笑风波有佳期。第十一层境界。看似真绝实乃万象未绝、万劫未绝，法亦尚未大成，仍需伏魔再度锤炼。</f>", 
		"<f size='28'>遍寻中天魔难除，踏尽玉宇修仙路。凡为有相皆虚妄，洞穿万法贵在无。第十二层境界。升仙入万法境也如同入无法境，万物万法皆如虚空，所见有相也皆无相。</f>", 
		"<f size='28'>人间几日变桑田，谁识神仙洞里天。短促共知有殊异，且须欢醉在生前。他时定是飞升去，化作凡仙一点青。第十三层境界。初入仙门，脱凡胎，换仙骨，逍遥自在天地间。</f>", 
		"<f size='28'>地仙者，为仙乘中之中乘，有神仙之才，无神仙之分，不悟大道，止于小乘或中乘之法，不克就正，不可见功，惟长生住世而不死于人间，第十四层境界。所谓不离于地者，此也。</f>", 
		"<f size='28'>修到形神俱妙，不受生死的拘束，解脱无累，随时随地可以散聚元神.天上人间，任意寄居。第十五层境界。负薪朝出卖，沽酒日西归。路人莫问归何处，穿入白云行翠微。</f>", 
		"<f size='28'>九仙之一。《云笈七签》卷三：“九仙者，第一上仙，二高仙，三火仙，四玄仙，五天仙，六真仙，七神仙，八灵仙，九至仙。第十六层境界。</f>", 
		"暂无描述", "暂无描述", "暂无描述", "暂无描述", "暂无描述", "暂无描述", "暂无描述", "暂无描述" };
	public static String text_bourn_013 = "任务星级已满,不要刷了,以免造成损失";
	public static String text_bourn_014 = "灵气值";
	public static String text_bourn_016 = "下一级别境界配置未找到";
	public static String 需要渡劫后才可升境界 = "需要先完成渡劫才能继续提升境界";
	public static String text_bourn_018 = "刷新任务成功,得到了@COUNT_1@星任务,消耗银子@STRING_1@";
	public static String text_bourn_019 = "刷新任务成功,得到了@COUNT_1@星任务,消耗经验@STRING_1@";
	public static String text_bourn_020 = "今天完成任务已达到上限不能再接取了";
	public static String text_bourn_021 = "你的VIP等级不足,不能用银子刷新,需要VIP等级:@COUNT_1@";
	public static String VIp绿钻 = "VIP绿钻";
	public static String VIp蓝钻 = "VIP蓝钻";
	public static String VIp紫钻 = "VIP紫钻";
	public static String VIp橙钻 = "VIP橙钻";
	public static String VIp皇冠 = "VIP皇冠";
	public static String 仙婴状态下不可更新跨服信息 = "仙婴状态下不可更新跨服信息";

	// 家族
	public static String text_jiazu_001 = "已经不在线!";
	public static String text_jiazu_002 = "族长召唤";
	public static String text_jiazu_003 = "俺来也";
	public static String text_jiazu_004 = "俺不去";
	public static String text_jiazu_005 = "目标区域不可达";
	public static String text_jiazu_006 = "你的银子不足,不能创建家族,所需银子@STRING_1@";
	public static String text_jiazu_007 = "你确定消耗@STRING_1@绑银和一个@STRING_2@,申请驻地吗?";
	public static String text_jiazu_008 = "申请家族";
	public static String text_jiazu_009 = "申请家族驻地";
	public static String text_jiazu_010 = "你的银子不足@STRING_1@两,不能申请驻地";
	public static String text_jiazu_011 = "申请驻地需要@STRING_1@,可以通过完成任务获得";
	public static String text_jiazu_012 = "你确定创建家族[@STRING_1@]么?需要绑银@STRING_2@";
	public static String text_jiazu_013 = "@STRING_1@与本家族志不同道不合，已离开!";
	public static String text_jiazu_014 = "你身上的金钱不足,需要@STRING_1@绑银";
	public static String text_jiazu_015 = "创建家族成功！";
	public static String text_jiazu_016 = "要创建的家族在这修仙界已经存在了。";
	public static String text_jiazu_017 = "你的家族已经在修仙界除名,请节哀!";
	public static String text_jiazu_018 = "你与@STRING_1@志不同道不合，你已经被移除出@STRING_2@了。";
	public static String text_jiazu_019 = "家族申请驻地，人数最少要达到@STRING_1@人，你的家族成员不足，不能申请。";
	public static String text_jiazu_020 = "驻地申请成功!";
	public static String text_jiazu_021 = "您已经离开了@STRING_1@";
	public static String text_jiazu_022 = "@STRING_1@正在召集你,是否前往？该消息发布于@STRING_2@";
	public static String text_jiazu_023 = "@STRING_1@被@STRING_2@逐出本族";
	public static String text_jiazu_024 = "建设任务已发布,欢迎大家积极参与";
	public static String text_jiazu_025 = "@STRING_1@举行工资仪式,是否前往？该消息发布于@STRING_2@";
	public static String text_jiazu_026 = "正在召唤大家去领工资!";
	public static String text_jiazu_028 = "每周一更新";
	public static String text_jiazu_029 = "批量发放将按照贡献工资发放,确定发放么?";
	public static String text_jiazu_030 = "目标不存在";
	public static String text_jiazu_031 = "你还没有家族";
	public static String text_jiazu_032 = "你没有发工资的权限";
	public static String text_jiazu_033 = "家族工资不足";
	public static String text_jiazu_034 = "ta已经领取过工资了";
	public static String text_jiazu_035 = "要发的工资超出限制";
	public static String text_jiazu_036 = "发送工资成功";
	public static String text_jiazu_037 = "批量发送工资成功";
	public static String text_jiazu_038 = "只能给在家族驻地的成员发工资";
	public static String text_jiazu_039 = "族长给你发了本周工资:@STRING_1@";
	public static String text_jiazu_040 = "你还没有加入家族";
	public static String text_jiazu_041 = "家族不存在";
	public static String text_jiazu_042 = "只有副族长才能操作";
	public static String text_jiazu_043 = "操作失败，现在已经申请了投票，不能再次申请。";
	public static String text_jiazu_044 = "申请成为族长";
	public static String text_jiazu_045 = "你确定要申请成为族长吗？此操作进行后，你所在的家族，将发起投票。";
	public static String text_jiazu_046 = "不能给自己任命职位";
	public static String text_jiazu_047 = "你没有权限进行这种操作。";
	public static String text_jiazu_048 = "密码不正确。";
	public static String text_jiazu_049 = "该职位没有空余!";
	public static String text_jiazu_050 = "你的家族权限改变了。";
	public static String text_jiazu_051 = "目标不存在。";
	public static String text_jiazu_052 = "你拒绝了@STRING_1@的申请。";
	public static String text_jiazu_053 = "家族@STRING_1@拒绝了你的申请。";
	public static String text_jiazu_054 = "家族邮件";
	public static String text_jiazu_055 = "群发邮件失败";
	public static String text_jiazu_056 = "家族人数已经达到了上限，接收失败。";
	public static String text_jiazu_057 = "@STRING_1@加入家族。";
	public static String text_jiazu_058 = "欢迎@STRING_1@加入家族，大家同舟共济振兴我族。";
	public static String text_jiazu_059 = "不能对自己操作。";
	public static String text_jiazu_060 = "你正在比武赛场，暂时不能操作!";
	public static String text_jiazu_061 = "您的角色处于锁定状态，暂时不能操作!";
	public static String text_jiazu_062 = "目标有家族";
	public static String text_jiazu_063 = "不同国家不能操作";
	public static String text_jiazu_064 = "@STRING_1@邀请你加入家族@STRING_2@";
	public static String text_jiazu_065 = "驻地不存在";
	public static String text_jiazu_066 = "您的等级还不能申请加入家族，请到@COUNT_1@级后再来吧。";
	public static String text_jiazu_067 = "请输入家族名字";
	public static String text_jiazu_068 = "你已经是@STRING_1@的成员了，请先离开家族再进行申请";
	public static String text_jiazu_069 = "你刚刚离开家族，需要等待24小时后才可申请。";
	public static String text_newjiazu_069 = "你刚刚离开家族，需要等待24小时后才可创建。";
	public static String text_newjiazu_001 = "对方刚刚离开家族，需要等待24小时后才可接收申请。";
	public static String text_jiazu_070 = "申请加入家族";
	public static String text_jiazu_071 = "你已经申请加入家族@STRING_1@,确定要替换为@STRING_2@么";
	public static String text_jiazu_072 = "您与@STRING_1@志不同道不合，您已经被移除出";
	public static String text_jiazu_073 = "建造成功";
	public static String text_jiazu_074 = "你不能开除比你职务高的人。";
	public static String text_jiazu_075 = "选择错误,请重新选择职位。";
	public static String text_jiazu_076 = "ta的的等级太低啦!。";
	public static String text_jiazu_077 = "目标和你不是一个国家,不能操作";

	public static String text_jiazu_082 = "未知原因,操作失败";
	public static String text_jiazu_083 = "你的家族已经拥有驻地,不需要重复申请";
	public static String text_jiazu_084 = "你输入的密码过长,请重新输入";
	public static String text_jiazu_085 = "你输入的密码提示过长,请重新输入";
	public static String text_jiazu_086 = "职位没有任何变化";
	public static String text_jiazu_087 = "您的家族宗指大于@COUNT_1@个汉字或含有禁用字符";
	public static String text_jiazu_088 = "今天召集次数已用尽!";
	public static String text_jiazu_089 = "兑换了";
	public static String text_jiazu_090 = "给家族贡献了";
	public static String text_jiazu_091 = "家族资金";
	public static String text_jiazu_092 = "灵脉值";
	public static String text_jiazu_093 = "召唤了";
	public static String text_jiazu_094 = "@STRING_1@正在升级中,不能操作";
	public static String text_jiazu_095 = "正在升级中,不能操作";
	public static String text_jiazu_096 = "暂无活动";
	public static String text_jiazu_097 = "@STRING_1@给@STRING_2@分配了<f color='@STRING_3@'>@STRING_4@</f>";
	public static String text_jiazu_098 = "获得经验:";
	public static String text_jiazu_099 = "你的银子不足以支付";
	public static String text_jiazu_100 = "你下手太慢了";
	public static String text_jiazu_101 = "Ta已经有家族了";
	public static String text_jiazu_102 = "不能任命比自己职务高的人";
	public static String text_jiazu_103 = "你的银子不足";
	public static String text_jiazu_104 = "你确定贡献@STRING_1@银子增加家族实力,并且增加自己@STRING_2@贡献度么?";
	public static String text_jiazu_105 = "我愿意";
	public static String text_jiazu_106 = "我小气";
	public static String text_jiazu_107 = "家族资金不足";
	public static String text_jiazu_108 = "@STRING_1@为家族捐献了银子@STRING_2@";
	public static String text_jiazu_109 = "BOSS位置未找到";
	public static String text_jiazu_110 = "你的VIP等级不足,不能捐钱,需要VIP等级:@COUNT_1@.";
	public static String text_jiazu_111 = "只有在家族驻地才能查看!";
	public static String text_jiazu_112 = "家族BOSS不存在!";
	public static String text_jiazu_113 = "离线";
	public static String text_jiazu_114 = "天";
	public static String text_jiazu_115 = "已将@STRING_1@逐出家族";
	public static String text_jiazu_116 = "离开家族";
	public static String text_jiazu_117 = "你确定要离开@STRING_1@家族吗?";
	public static String text_jiazu_118 = "申请驻地";
	public static String text_jiazu_119 = "你现在是混元至圣不能进行禅让";
	public static String text_jiazu_120 = "任命了副族长才可以进行禅让操作。";
	public static String text_jiazu_121 = "操作成功，你的权限改变了。";
	public static String text_jiazu_122 = "你的权限改变了，恭喜你成为家族族长。";
	public static String text_jiazu_123 = "解散家族";
	public static String text_jiazu_124 = "你确定要解散@STRING_1@家族吗？此操作进行后，家族仓库中的物品将消失。你所在的家族，将永远在修仙界中除名。";
	public static String text_jiazu_125 = "您还没有加入家族。";
	public static String text_jiazu_126 = "请先离开你现在的家族再来申请建立家族吧！";
	public static String text_jiazu_127 = "您的等级不足，请到20级以后再来申请建立家族吧！";
	public static String text_jiazu_128 = "您没有输入家族名!";
	public static String text_jiazu_129 = "您输入家族名称含有禁用字符或敏感字符，请重新输入";
	public static String text_jiazu_130 = "您输入的家族宗旨有禁用字符或敏感字符，请重新输入";
	public static String text_jiazu_131 = "创建家族";
	public static String text_jiazu_132 = "你不属于这个家族!";
	public static String text_jiazu_133 = "你没有升级建筑的权限";
	public static String text_jiazu_134 = "你的家族还没有建造这个建筑!";
	public static String text_jiazu_135 = "基础建筑不可以摧毁";
	public static String text_jiazu_136 = "密码错误。";
	public static String text_jiazu_137 = "任命成功。";
	public static String text_jiazu_138 = "家族已经有这个徽章了";
	public static String text_jiazu_139 = "所选徽章不存在";
	public static String text_jiazu_141 = "仗义疏财";
	public static String text_jiazu_142 = "您已经向@STRING_1@家族发出了申请，请等待该家族处理。";
	public static String text_jiazu_143 = "未查找到符合条件的家族";
	public static String text_jiazu_144 = "族长需要先禅让，才允许退出家族。";
	public static String text_jiazu_145 = "你没有输入驻地名。";
	public static String text_jiazu_146 = "你输入驻地名称太长，请重新输入。";
	public static String text_jiazu_147 = "你输入驻地名称含有禁用字符或敏感字信息，请重新输入。";
	public static String text_jiazu_148 = "你输入驻地名称在这修仙界已经存在，请重新输入。";
	public static String text_jiazu_149 = "只能禅让给本家族的副族长";
	public static String text_jiazu_150 = "您购买成功！";
	public static String text_jiazu_151 = "仙灵洞等级不足,不能使用这个徽章";
	public static String text_jiazu_152 = "该建筑已经达到最低等级,不能再降级了!";
	public static String text_jiazu_153 = "降级操作正在CD,距下一次可降级时间还差";
	public static String text_jiazu_154 = "降级完成";
	public static String text_jiazu_155 = "你所起的家族名重复,重新来个响亮的名字吧!";
	public static String text_jiazu_156 = "已经申请过了家族驻地,不用重复申请!";
	public static String text_jiazu_157 = "家族驻地申请成功!";
	public static String text_jiazu_158 = "操作成功";
	public static String text_jiazu_159 = "你没有降级建筑的权限";
	public static String text_jiazu_160 = "标志像只能建造一个";
	public static String text_jiazu_161 = "你的家族已经建造这个建筑!";
	public static String text_jiazu_162 = "家族资金或灵脉值不足!";
	public static String text_jiazu_163 = "同时只能有一个建筑升级:@STRING_1@正在升级!";
	public static String text_jiazu_164 = "你不属于这个家族!";
	public static String text_jiazu_165 = "建设任务开启";
	public static String text_jiazu_166 = "@STRING_1@刚刚发布建设任务@STRING_2@ @STRING_3@级.你是否前往?";
	public static String text_jiazu_167 = "你需要先升级主建筑!";
	public static String text_jiazu_168 = "主建筑升级需要其他基础建筑都达到主建筑的级别!";
	public static String text_jiazu_169 = "该建筑已经达到最高等级,不能再升级了!";
	public static String text_jiazu_170 = "您需要申请一块驻地";
	public static String text_jiazu_171 = "龙图阁LV1需升级为龙图阁LV2才可以进行此设置。";
	public static String text_jiazu_173 = "职位称号含有禁用字符或者长度过长";
	public static String text_jiazu_174 = "修改职位成功。";
	public static String text_jiazu_175 = "只能在国内操作!";
	public static String text_jiazu_176 = "召唤次数已经达到上限:@COUNT_1@次";
	public static String text_jiazu_177 = "BUFF 配置异常:@STRING_1@";
	public static String text_jiazu_178 = "发布@COUNT_1@级BUFF:@STRING_1@成功,消耗银子:@STRING_2@,持续:@STRING_3@";
	public static String text_jiazu_179 = "没有正在发布的任务";
	public static String text_jiazu_180 = "只能在自己的家族中操作";
	public static String text_jiazu_181 = "对方已经申请其它家族";
	public static String text_jiazu_182 = "您不是家族族长，不能做此操作";
	public static String text_jiazu_183 = "您的家族不能修改家族名字。";
	public static String text_jiazu_184 = "请输入您想修改的家族名字。";
	public static String 王城运镖 = "接取王城运镖";

	public static String text_achievement_001 = "恭喜你达成了成就:@STRING_1@,获得奖励:@STRING_2@";
	public static String text_achievement_002 = "恭喜你达成了成就:@STRING_1@,获得成就值:@COUNT_1@";
	public static String text_achievement_003 = "不存在的成就 ";
	public static String text_achievement_005 = "目标不存在";
	public static String text_achievement_006 = "无效的查询,目标未完成该成就";

	public static String 隐身且减少受到伤害详细 = "隐身且受到伤害降低@COUNT_1@%";
	public static String 隐身且减少受到伤害 = "隐身且减少受到伤害";
	public static String 将目标瞬间拉向自己并使目标晕眩 = "将目标瞬间拉向自己，并使目标晕眩。";
	public static String 冰箱 = "使自己处于冰冻状态，无法攻击，无法移动，不受伤害。";

	// 寻人
	public static String 系统暂未开放更多精彩敬请期待 = "系统暂未开放,更多精彩,敬请期待!";
	public static String 你已经接取了斩妖降魔 = "你已经接取了斩妖降魔";
	public static String 你今天的斩妖降魔均已完成明天再来吧 = "你今天的斩妖降魔均已完成,明天再来吧";
	public static String 当然经验巨多 = "当然,经验巨多";
	public static String 算了我没体力 = "算了,我没体力";
	public static String 没有匹配的记录 = "没有匹配的记录!";
	public static String 你的等级不足斩妖除魔 = "你的等级不足,等@COUNT_1@级再来,大量经验等着你!";
	public static String 你确定消耗体力进行斩妖降魔么 = "你确定消耗<f color='0xFF0000'>@COUNT_1@点</f>体力,进行斩妖降魔么?";
	public static String 你的体力不足斩妖除魔 = "你的体力不足,需要@COUNT_1@点";
	public static String 你可以经常向我询问寻人进度 = "你可以经常向我询问寻人进度";
	public static String 目前你没有任何目标要寻找 = "目前你没有任何目标要寻找";
	public static String 最新情报 = "<f color='0x33CC00'>最新情报,你的目标:</f>\n";
	public static String 当前获得的所有情报 = "当前获得的所有情报";
	public static String 斩妖提示 = "<f>你认定</f><f color='0xFFFF00'>@STRING_1@</f><f>是伪装的妖魔么?</f>\n<f>当前积分:(@COUNT_1@/@COUNT_2@)</f>\n友情提示:<f color='0xFF0000'>猜错一次扣1点积分</f>";
	public static String 恩就是他 = "恩.就是Ta";
	public static String 我再想想 = "厄.我再想想";

	// entity
	public static String 物品 = "物品";
	public static String 已绑定 = "已绑定";
	public static String 未绑定 = "未绑定";
	public static String 以锁魂 = "已锁魂";
	public static String 小时 = "小时";
	public static String 分钟 = "分钟";
	public static String 以高级锁魂 = "已高级锁魂";
	public static String 正在解魂 = "正在解魂";
	public static String 有效期 = "有效期";
	public static String 境界限定 = "境界限定";
	public static String 镶嵌境界限定 = "镶嵌境界限定";
	public static String 血量 = "血量";
	public static String 法力值 = "法力值";
	public static String 最大血量 = "最大血量";
	public static String 物理攻击 = "物理攻击";
	public static String 法术攻击 = "法术攻击";
	public static String 物理防御 = "物理防御";
	public static String 法术防御 = "法术防御";
	public static String 闪躲 = "闪躲";
	public static String 免暴 = "免暴";
	public static String 命中 = "命中";
	public static String 暴击 = "暴击";
	public static String 破甲 = "破甲";
	public static String 庚金攻击 = "庚金攻击";
	public static String 庚金抗性 = "庚金抗性";
	public static String 庚金减抗 = "庚金减抗";
	public static String 葵水攻击 = "葵水攻击";
	public static String 葵水抗性 = "葵水抗性";
	public static String 葵水减抗 = "葵水减抗";
	public static String 离火攻击 = "离火攻击";
	public static String 离火抗性 = "离火抗性";
	public static String 离火减抗 = "离火减抗";
	public static String 乙木攻击 = "乙木攻击";
	public static String 乙木抗性 = "乙木抗性";
	public static String 乙木减抗 = "乙木减抗";
	public static String 可增加快乐值 = "可增加快乐值";
	public static String 可增加寿命值 = "可增加寿命值";
	public static String 气血器灵 = "血量器灵";
	public static String 外攻器灵 = "物攻器灵";
	public static String 内攻器灵 = "法攻器灵";
	public static String 外防器灵 = "物防器灵";
	public static String 内防器灵 = "法防器灵";
	public static String 炎攻器灵 = "金攻器灵";
	public static String 冰攻器灵 = "水攻器灵";
	public static String 风攻器灵 = "木攻器灵";
	public static String 雷攻器灵 = "火攻器灵";
	public static String 炎防器灵 = "金防器灵";
	public static String 水防器灵 = "水防器灵";
	public static String 火防器灵 = "木防器灵";
	public static String 木防器灵 = "火防器灵";
	public static String 宝魂值 = "宝魂值";
	public static String 装备等级 = "装备等级";
	public static String 属性等级 = "属性等级";
	public static String 仙 = "仙";
	public static String 宠物性格 = "性格:";
	public static String 宠物限制 = "专属宠物:";
	public static String 的专属 = "的专属";
	public static String 已铭刻 = "已铭刻";
	public static String 未铭刻 = "未铭刻";
	public static String 等阶限定 = "等阶限定";
	public static String 耐久度 = "耐久度";
	public static String 附加属性随机 = "附加属性随机";
	public static String 宝石孔随机 = "宝石孔随机";
	public static String 有凹槽 = "有凹槽";
	public static String 第 = "第";
	public static String 孔 = "孔";
	public static String 装备资质 = "装备资质";
	public static String 基础属性 = "基础属性";
	public static String 附加属性 = "附加属性";
	public static String 铭刻加成 = "铭刻加成";
	public static String 炼星加成 = "炼星加成";
	public static String 羽化加成 = "羽化加成";
	public static String 星 = "星";
	public static String 全部属性 = "全部属性";
	public static String 装备精炼度 = "装备精炼度";
	public static String 技能名称 = "技能名称";
	public static String 消耗 = "消耗";
	public static String 释放距离 = "释放距离";
	public static String 码 = "码";
	public static String 描述 = "描述";
	public static String 方式 = "方式";
	public static String 器灵槽 = "器灵槽";
	public static String 道具 = "道具";
	public static String 使用等级 = "使用等级";
	public static String 可携带等级 = "可携带等级";
	public static String 一代 = "一代";
	public static String 二代 = "二代";
	public static String 级变异 = "级变异";
	public static String 繁殖次数 = "繁殖次数";
	public static String 稀有度 = "稀有度";
	public static String 成长率 = "成长率";
	public static String 品质 = "品质";
	public static String 宠物饰品 = "宠物饰品:";
	public static String 力量 = "力量";
	public static String 身法 = "身法";
	public static String 灵力 = "灵力";
	public static String 耐力 = "耐力";
	public static String 定力 = "定力";
	public static String 资质 = "资质";
	public static String 性格 = "性格";
	public static String 无技能 = "无技能";
	public static String 膂力 = "力量";
	public static String 悟性 = "悟性";
	public static String 筋骨 = "耐力";
	public static String 技能 = "技能";
	public static String 魂量 = "魂量";
	public static String 上限 = "上限";

	// 千层塔

	// 刺探和偷砖
	public static String text_peekAndBrick_001 = "偷砖成功";
	public static String text_peekAndBrick_002 = "刺探成功";
	public static String text_peekAndBrick_003 = "无间道么!?谁派你来偷自己国家的砖头的!?";
	public static String text_peekAndBrick_004 = "你有未交付的偷砖任务，交付了再来!!";
	public static String text_peekAndBrick_005 = "小样，作死啊，还刺探自己国家，诛你九族!";
	public static String text_peekAndBrick_006 = "刺探CD中。。。。还差@STRING_1@秒";
	public static String text_peekAndBrick_007 = "你还没有接取任务";
	public static String text_peekAndBrick_008 = ",获得";

	// 运镖
	public static String text_silverCar_001 = "你在护送状态下,不能接取任务!";
	public static String text_silverCar_002 = "任务异常!";
	public static String text_silverCar_003 = "任务配置异常";
	public static String text_silverCar_004 = "任务不存在";
	public static String text_silverCar_005 = "有了家族一天才能运镖两次";
	public static String text_silverCar_006 = "一天只能运镖两次";
	public static String text_silverCar_008 = "成功的将@STRING_1@换成了@STRING_2@";
	public static String text_silverCar_009 = "恭喜你获得家族运镖经验:@STRING_1@";
	public static String text_silverCar_010 = "你没有镖车";
	public static String text_silverCar_011 = "你的镖车已经被摧毁,不能再摇号了";
	public static String text_silverCar_013 = "你的镖银不足,需要:@STRING_1@";
	public static String text_silverCar_014 = "接取押镖任务成功,消耗镖车押金:@STRING_1@银子";
	public static String text_silverCar_015 = "你没有押镖任务所需物品:<f color='@STRING_1@'>@STRING_2@</f>";
	public static String text_silverCar_016 = "你的家族今天已经压过镖了,明天再来吧!";
	public static String text_silverCar_017 = "王城运镖";
	public static String text_silverCar_018 = "<f>家族运镖说明：</f>\n<f>家族族长接取家族押镖任务，召集家族成员前往边疆找王镖师！护送成功后所有前往成员均会获得经验和古董</f>";
	public static String text_silverCar_019 = "你的镖车离你太远了,先去把它找回!";
	public static String text_silverCar_020 = "此镖车的摇号次数已到达上限，不能进行摇号换车！";
	public static String text_silverCar_021 = "您的摇号次数已到达上限，不能进行摇号换车!";
	public static String text_silverCar_022 = "您已经换过了，不能进行摇号换车!";
	public static String text_silverCar_023 = "你猜猜看,换完了会是橙色的么";
	public static String text_silverCar_024 = "你权限不够不能接此任务。";

	// 神农
	public static String text_farming_001 = "恭喜你获得了:@ARTICLE_NAME_1@";

	// 祝福果//养蚕
	public static String 已达到最高级 = "已达到最高级";
	public static String 仙气 = "仙气";
	public static String text_forluck_001 = "他业已触碰过这尊丹炉的仙气了!";
	public static String text_forluck_002 = "丹炉里的仙气业已散尽,有缘下次聚首!";
	public static String text_forluck_003 = "只能分配给家族成员!";
	public static String text_forluck_004 = "你的权限不足";
	public static String text_forluck_005 = "只能在自己家族中操作";
	public static String text_forluck_006 = "祝福果树已经消失!";
	public static String text_forluck_007 = "别太贪心了，你已经摘过了!";
	public static String text_forluck_009 = "上次活动还未结束";
	public static String text_forluck_010 = "今天发布次数太多了";
	public static String text_forluck_011 = "没有空余位置了";
	public static String text_forluck_012 = "NPC不存在@STRING_1@";
	public static String text_forluck_014 = "发布养蚕活动";
	public static String text_forluck_015 = "您当前的银子不足以在发布任务，需要@STRING_1@";
	public static String text_forluck_017 = "开炉炼丹";
	public static String text_forluck_018 = "你的背包满了,可以通过月光宝盒合成来进行清理";
	public static String text_forluck_019 = "丹炉聚气中,请耐心等待,心急吃不着真丹药...";
	public static String text_forluck_020 = "分配成功";
	public static String text_forluck_021 = "炼丹成功,消耗家族资金@STRING_1@";
	public static String text_forluck_022 = "今日家族炼丹次数太多";

	public static String 随身商店 = "随身商店";

	// 新手
	public static String text_newPlayer_001 = "新手引导";

	public static String text_createArticle_reason_collection = "采集";

	public static String 人民币元宝 = "人民币元宝";
	public static String 非绑定银子 = "非绑定银子";
	public static String 绑定银子 = "绑定银子";
	public static String 其他 = "其他";
	public static String 帮派资金 = "帮派资金";
	public static String 工资 = "工资";
	public static String 资源 = "资源";
	public static String 物品兑换 = "物品兑换";
	public static String 商店银子 = "商店银子";
	public static String 活跃度 = "活跃度";

	public static String 家族宣战 = "家族宣战";
	public static String 请输入宣战家族的名字 = "请输入宣战家族的名字，请注意宣战要消耗@COUNT_1@银子";
	public static String 宣战 = "宣战";
	public static String 您输入的家族不存在 = "您输入的家族不存在";
	public static String 在这里输入宣战家族名 = "在这里输入宣战家族名";
	public static String 不能对自己家族宣战 = "不能对自己家族宣战";
	public static String 某家族对某家族宣战 = "@STRING_1@家族对@STRING_2@家族宣战，时间1小时";
	public static String 你家族对某家族宣战 = "你家族对@STRING_1@家族宣战，时间1小时";
	public static String 某家族对你家族宣战 = "@STRING_1@家族对你家族宣战，时间1小时";
	public static String 宝瓶 = "宝瓶";
	public static String 由于您的背包已满您得到的部分物品通过邮件发送 = "由于您的背包不能放下所有物品，因此一部分未放入背包中的物品通过邮件发送，您可以通过月光宝盒合成来进行清理背包";
	public static String 系统邮件提示 = "系统邮件提示";
	public static String 福缘石 = "香火";
	public static String 祈福需要石头 = "祈福需要@STRING_1@，您可以到商城购买";
	public static String 请遵守游戏规则不要使用外挂 = "请遵守游戏规则，不要使用外挂";
	public static String 不能选择查看过的位置 = "不能选择查看过的位置，请遵守游戏规则，不要使用外挂";
	public static String 您今天祈福次数已满 = "您今天祈福次数已满";
	public static String 您今天剩余祈福次数不足 = "您今天剩余祈福次数不足，不能使用全部开启，请单次开启";
	public static String 请选择重新祈福 = "请选择重新祈福";
	public static String 您没有足够的石头或银子 = "您没有足够的@STRING_1@来进行此项操作";
	public static String 祈福 = "祈福";
	public static String 运镖 = "家族运镖";
	public static String 扣费失败 = "扣费失败";
	public static String 祈福得到物品 = "您经过祈福得到@STRING_1@x@COUNT_1@";
	public static String 最少进行一次祈福才可以查看 = "最少进行一次祈福才可以查看";
	public static String 不能进行查看 = "不能进行查看";
	public static String 您得到了物品 = "您得到了@STRING_1@x@COUNT_1@";
	public static String 恭喜玩家幸运开出物品 = "恭喜玩家@PLAYER_NAME_1@在@STRING_1@内幸运的开出了@STRING_2@";
	public static String 您的等级不够 = "这么小就想学人家打架！回去历练到40再来吧！";
	public static String 每天17点前报名 = "每天17点前报名";
	public static String 您已经报过名了 = "您已经报过名了，今天不必重复报名";
	public static String 报名成功 = "报名成功";
	public static String 您的跨服数据正在更新中 = "您的跨服数据正在更新中，请稍候再试。";
	public static String 距离比赛开始还有 = "距离比赛开始还有@COUNT_1@秒";
	public static String 刺探 = "刺探";
	public static String 获得 = "获得";
	public static String 仙府 = "仙府";
	public static String 粮食 = "粮食";
	public static String 木材 = "木材";
	public static String 石料 = "石料";
	public static String 仙境 = "仙境";
	public static String 任务限时 = "任务限时";
	public static String 申请列表已满 = "@STRING_1@的申请列表已满!";
	public static String 玄兽丹 = "玄兽丹";
	public static String 不能升级 = "不能升级";
	public static String 族令 = "家族驻地申请函";
	public static String 鬼斧神工令 = "鬼斧神工令";
	public static String 自己 = "[自己]";
	public static String 宠物挂机中 = "[宠物挂机中...]";
	public static String 驱赶宠物 = "驱赶宠物";
	public static String 获得资源 = "获得资源";
	public static String 田地种植 = "田地种植-";
	public static String 田地 = "田地";
	public static String 摇钱树 = "摇钱树";
	public static String 经验树 = "经验树";
	public static String 级升级 = "级-升级";
	public static String 境界 = "境界";
	public static String 国运 = "国运";
	public static String 家族运镖 = "家族运镖";
	public static String 偷砖 = "偷砖";
	public static String 无胜利者 = "无胜利者";
	public static String 比武开始时给玩家上的buff = "定身";
	public static String 恭喜您赢得了比赛获得积分 = "恭喜您赢得了比赛，获得了@COUNT_1@积分";
	public static String 很遗憾您输了比赛获得积分 = "很遗憾您输了比赛，获得了@COUNT_1@积分";
	public static String 战场心跳线程 = "战场心跳线程";
	public static String 进入竞技场 = "进入比武场";
	public static String 进入竞技场提示 = "是否进入比武场?";
	public static String 战场已经过了进入时间 = "比武场已经过了进入时间";
	public static String 您没有比武 = "您目前没有比武";
	public static String 没有战场 = "没有比武场";
	public static String 恭喜您的比赛轮空获得积分 = "恭喜您的比赛轮空，直接获得了@COUNT_1@积分";
	public static String 竞技管理器还未启动 = "比武管理器还未启动，请联系客服";
	public static String 仙武大会1 = "xianwudahui";
	public static String 仙武大会2 = "xianwudahui";
	public static String 仙武大会3 = "xianwudahui";
	public static String 仙武大会4 = "xianwudahui";
	public static String 仙武大会5 = "xianwudahui";
	public static String 仙武大会6 = "xianwudahui";
	public static String 传送出比武场地图 = "kunlunshengdian";
	public static String 战场免费复活成功 = "战场免费复活成功";
	public static String 获胜者失败者 = "@PLAYER_NAME_1@在第@COUNT_1@回合比赛中战胜@PLAYER_NAME_2@";
	public static String 由于消极比赛双方均失败 = "由于消极比赛双方均失败";
	public static String 由于某人没到场 = "由于@PLAYER_NAME_1@没到场，本场比赛@PLAYER_NAME_2@获胜";
	public static String 双方都没到场 = "由于比赛双方都没到场，本场比赛双方都失败";
	public static String 某人先胜两局 = "@PLAYER_NAME_1@先胜两局，本场比赛@PLAYER_NAME_1@战胜@PLAYER_NAME_2@";
	public static String 由于消极比赛本场双方均失败 = "由于比赛双方在3局比赛中均没有胜出两场，所以本场比赛双方均失败";
	public static String 即将开始下一局比赛 = "即将开始第@COUNT_1@局比赛";
	public static String 每天最早申请时间 = "每天最早申请时间为每天@COUNT_1@时";
	public static String 每天最晚申请时间 = "每天最晚申请时间为每天@COUNT_1@时";
	public static String 您申请的矿不存在 = "您申请的矿不存在";
	public static String 您已经申请攻打了此矿 = "您已经申请攻打了此矿";
	public static String 此矿已经由别人申请攻打 = "此矿已经由别人申请攻打";
	public static String 申请攻打灵矿需要花费 = "申请攻打灵矿需要花费@COUNT_1@银子";
	public static String 矿战 = "矿战";
	public static String 您已经申请攻打另一处矿 = "申请攻打灵矿需要@COUNT_1@银子，由于您已经申请攻打了一个矿，如果申请攻打这个矿，将自动放弃之前申请的矿，确定吗？";
	public static String 申请攻打灵矿 = "申请攻打灵矿";
	public static String 申请攻打灵矿花费确认 = "申请攻打灵矿需要@COUNT_1@银子，确定吗？";
	public static String 现在不是开战时间 = "现在不是开战时间";
	public static String 占领矿需要等级达到45级 = "占领矿需要等级达到45级";
	public static String 不能占领 = "不能占领";
	public static String 你没有资格申请占领 = "你没有资格申请占领";
	public static String 申请占领处于冷却中 = "申请占领处于冷却中";
	public static String 村庄战 = "村庄战";
	public static String 占领中 = "占领中......";
	public static String 申请攻打灵矿成功 = "申请攻打灵矿成功";
	public static String 无需占领 = "这个矿没有守护方，无需占领，时间到后自动归攻击方所有";
	public static String 恭喜贵帮赢得村庄战的胜利 = "矿战结束，恭喜贵家族赢得矿战的胜利";
	public static String 贵帮申请了今天的村庄战 = "贵家族申请了今天@COUNT_1@时的矿战";
	public static String 某帮申请向贵帮发起村庄战 = "@STRING_1@家族向贵家族占领的矿发起了攻打申请，矿战将于@COUNT_1@时开启";
	public static String 贵家族防守失利 = "矿战结束，贵家族防守失利，矿战失败";
	public static String 恭喜贵帮赢得村庄战的胜利占有两个矿的处理 = "矿战结束，恭喜贵家族赢得矿战的胜利，由于贵家族以前占有一处灵矿，系统自动进行替换处理";
	public static String 某玩家的努力暂时占有灵矿 = "@PLAYER_NAME_1@奋力拼搏使得本家族获得了灵矿的暂时占有权";
	public static String 对方家族暂时占有灵矿 = "对方家族获得了灵矿的暂时占有权，咱们要努力啊";
	public static String 防守方暂时占领 = "防守方暂时占领";
	public static String 进攻方暂时占领 = "进攻方暂时占领";
	public static String 某某家族的矿 = "@STRING_1@家族的矿";
	public static String 醒醒这是你的矿 = "喂！醒醒，自家的矿就别打了";
	public static String 忙着呢别捣乱 = "忙着呢，这边没你什么事，别捣乱";
	public static String 现在是开战时间 = "现在是开战时间";
	public static String 您无权进行操作 = "您无权进行操作";
	public static String 家族不存在 = "家族不存在";
	public static String 家族等级大于5级才可以远征 = "需要家族等级大于等于1级才能开启远征";
	public static String boss已经被击杀 = "该级别的boss已经被击杀";
	public static String 远征活动即将结束 = "远征活动未开启，不能进入";
	public static String 远征活动配置错误 = "活动配置错误，请联系GM";
	public static String 远征活动副本配置错误 = "副本掉率配置错误，请联系GM";
	public static String 副本cd中 = "副本cd中，请15秒后再尝试";
	public static String 幻境副本一天只能参加一次 = "您今天已经参加过了";
	public static String 只有家族族长才可以改名 = "只有家族族长才可以改名";
	public static String 请输入灵矿名 = "请输入灵矿名";
	public static String 您的矿名更改 = "您的灵矿名字变更为@STRING_1@";
	public static String 只有家族族长才可以使用这个功能 = "只有家族族长才可以使用这个功能";
	public static String 您的家族已经领取过银子 = "您的家族已经领取过银子";
	public static String 占领矿给的钱 = "占领矿给的钱";
	public static String 您的家族已经领取过家族资金 = "您的家族已经领取过家族资金";
	public static String 您已经领取过绑银 = "您已经领取过绑银";
	public static String 您已经领取过BUFF = "您已经领取过BUFF";
	public static String 大矿_BUFF_NAME = "减速";
	public static String 中矿_BUFF_NAME = "减速";
	public static String 小矿_BUFF_NAME = "减速";
	public static String 灵矿操作 = "灵矿操作";
	public static String 灵矿操作详细 = "灵矿操作详细";
	public static String 修改灵矿名称 = "修改灵矿名称";
	public static String 族长领取银子 = "族长领取银子";
	public static String 族长领取家族资金 = "族长领取家族资金";
	public static String 族员领取绑银 = "族员领取绑银";
	public static String 族员领取BUFF = "族员领取BUFF";
	public static String 死了就老实点 = "死了就老实点，别乱点，让人以为诈尸了，吓着了你负责啊！";
	public static String 领取成功 = "领取成功";
	public static String 族长领取了银子 = "族长在灵矿处领取了@COUNT_1@银子";
	public static String 你领取了银子 = "你领取了@COUNT_1@银子";
	public static String 族长领取了家族资金 = "族长在灵矿处领取了@COUNT_1@家族资金";
	public static String 你领取了家族资金 = "你领取了@COUNT_1@家族资金";
	public static String 你领取了绑银 = "你领取了@COUNT_1@绑银";
	public static String 你领取了BUFF = "你领取了@STRING_1@BUFF";
	public static String 锭 = "锭";
	public static String 两 = "两";
	public static String 文 = "文";
	public static String 个 = "个";;
	public static String 争夺灵矿战已经开始 = "争夺灵矿战已经开始，请申请攻打灵矿的家族到相应的灵矿处占领";
	@DonotTranslate
	public static String 灵眼 = "灵眼";
	@DonotTranslate
	public static String 灵泉 = "灵泉";
	@DonotTranslate
	public static String 灵脉 = "灵脉";
	public static String 越狱看多了吧 = "越狱看多了吧";
	public static String 您的坐牢剩余时间 = "你还有@COUNT_1@分就可以出狱啦。ps:不想把牢底坐穿吗？";
	public static String 您现在可以点击离开选项离开监狱了 = "您现在可以点击离开选项离开监狱了。ps:想坐牢吗？";
	public static String 国王大人您要辞官的话会引起混乱的 = "万万使不得啊，失去您国家会混乱的";
	public static String 请输入支取资金数 = "请输入支取资金数";
	public static String 在这里输入 = "在这里输入";
	public static String 全体国民都点击确定到我这来 = "全体国民都点击确定到我身边来";
	public static String 您不能接取这个任务 = "您不能接取这个任务";
	public static String 当前星级套装 = "当前为@COUNT_1@星套装\n";
	public static String 减少对方金防等级 = "减少对方金防等级@COUNT_1@";
	public static String 减少对方水防等级 = "减少对方水防等级@COUNT_1@";
	public static String 减少对方火防等级 = "减少对方火防等级@COUNT_1@";
	public static String 减少对方木防等级 = "减少对方木防等级@COUNT_1@";
	public static String 下一级别套装为 = "下一级别套装为@COUNT_1@星套装\n";
	public static String 颜色套装 = "当前为@STRING_1@套装\n";
	public static String 下一级颜色套装 = "下一级别套装为@STRING_1@套装\n";
	public static String 挂机宠物 = "挂机宠物";
	public static String 您的角色处于锁定状态暂时不能参与交易 = "您的角色处于锁定状态，暂时不能参与交易！";
	public static String 您现在身处跨服服务器暂时不能参与交易 = "您现在身处跨服服务器，暂时不能参与交易！";
	public static String 您不能与自己交易 = "您不能与自己交易！";
	public static String 交易申请 = "您向@STRING_1@发出了交易请求";
	public static String 非法输入 = "非法输入!";
	public static String 余额不足 = "余额不足!";
	public static String[] 余额不足2 = new String[] { 金币不足, "银子不足!", "工资不足!", "资源不足", "帮派资金不足!", "历练值不足!", "功勋不足!", "文采值不足!", "物品不足!", "银票余额不足!", "活跃度不足!", "积分不足!", "修法值不足!" };

	public static String 您不是vip不能使用vip商店 = "您不是vip不能使用vip商店";
	public static String 商店不存在 = "对不起，服务器配置错误，商店[]不存在";
	public static String 类型错误 = "类型错误";
	public static String 元神已经存在不需要重复激活 = "元神已经存在不需要重复激活";
	public static String 等级不够激活元神 = "61级即可激活,赶快升级吧!";
	public static String 只能选择同性别的职业 = "只能选择同性别的职业!";
	public static String 已经是当前元神了 = "已经是当前元神了";
	public static String 切换操作CD中 = "切换操作CD中";
	public static String 要切换的元神不存在 = "要切换的元神不存在";
	public static String 你在骑乘状态不能切换元神 = "你在骑乘状态,不能切换元神";
	public static String 境界升级提示 = "<f color='@COUNT_1@'>恭喜@STRING_1@的@STRING_2@境界等级提升至@STRING_3@</f>";
	public static String 没有元神不能升级 = "没有元神，不能升级！";
	public static String 元神等级低于60 = "等级太低，需要达到60开启元神后，才能升级属性!";
	public static String 元神等级低于110 = "等级限制，需要达到110才能升级属性！";
	public static String 元神等级低于150 = "等级限制，需要达到150才能升级属性！";
	public static String 元神等级低于190 = "等级限制，需要达到190才能升级属性！";
	public static String 元神等级低于220 = "等级限制，需要达到220才能升级属性！";
	public static String 已达上限 = "已达上限";
	public static String 元婴丹 = "元婴丹";
	public static String 神婴丹 = "神婴丹";
	public static String 您背包的材料不足 = "您背包的@STRING_1@数量不足，升级需要消耗@COUNT_1@个！";

	public static String 目标不存在或不能攻击 = "目标不存在或不能攻击";
	public static String 观战方不能使用技能 = "观战方不能使用技能";
	public static String 非战斗时间不能使用技能 = "非战斗时间不能使用技能";
	public static String 渡劫中不能使用技能 = "渡劫中不能使用技能";
	public static String 不能开启其他模式 = "您还太小，40级以后才可以开启其他模式征战沙场";
	public static String 对方已把您加入黑名单您不能发出聊天信息 = "对方已把您加入黑名单，您不能发出聊天信息！";
	public static String 角色名称不合法 = "角色名称不合法";
	public static String 不能创建角色 = "角色名称不合法";
	public static String 增加物理攻击描述 = "增加物理攻击@COUNT_1@";
	public static String 降低物理攻击描述 = "降低物理攻击@COUNT_1@";
	public static String 增加物理防御描述 = "增加物理防御@COUNT_1@";
	public static String 降低物理防御描述 = "降低物理防御@COUNT_1@";
	public static String 增加法术攻击描述 = "增加法术攻击@COUNT_1@";
	public static String 降低法术攻击描述 = "降低法术攻击@COUNT_1@";
	public static String 增加法术防御描述 = "增加法术防御@COUNT_1@";
	public static String 降低法术防御描述 = "降低法术防御@COUNT_1@";
	public static String 增加物理攻击百分比描述 = "增加物理攻击@COUNT_1@%";
	public static String 降低物理攻击百分比描述 = "降低物理攻击@COUNT_1@%";
	public static String 增加物理防御百分比描述 = "增加物理防御@COUNT_1@%";
	public static String 降低物理防御百分比描述 = "降低物理防御@COUNT_1@%";
	public static String 增加法术攻击百分比描述 = "增加法术攻击@COUNT_1@%";
	public static String 降低法术攻击百分比描述 = "降低法术攻击@COUNT_1@%";
	public static String 增加法术防御百分比描述 = "增加法术防御@COUNT_1@%";
	public static String 降低法术防御百分比描述 = "降低法术防御@COUNT_1@%";
	public static String 对敌人造成反伤 = "对敌人造成@COUNT_1@%反伤";
	public static String 反伤 = "反伤";
	public static String 修理全部装备需要绑银 = "修理全部装备需要@COUNT_1@绑银，如果绑银不够将用银子代替";
	public static String 修理当前所穿装备需要绑银 = "修理当前所穿装备需要@COUNT_1@绑银，如果绑银不够将用银子代替";
	public static String 每多少秒增加血百分比 = "每@COUNT_1@秒回复气血上限的@COUNT_2@%血量";
	public static String 每多少秒减少血百分比 = "每@COUNT_1@秒减少气血上限的@COUNT_2@%血量";
	public static String 未识别的技能类型 = "未识别的技能类型:";
	public static String 增加血量上限描述 = "增加血量上限@COUNT_1@点";
	public static String 技能触发几率 = "<f size='28'>@STRING_1@性格触发几率</f><f color='0xfff600' size='28'>@COUNT_2@%</f><f size='28'>，其他性格触发几率</f><f color='0xfff600' size='28'>@COUNT_1@%</f>";
	public static String 法术防御增加 = "法术防御增加@COUNT_1@";
	public static String 法术攻击增加 = "法术攻击增加@COUNT_1@";
	public static String 物理防御增加 = "物理防御增加@COUNT_1@";
	public static String 外功攻击增加 = "物理攻击增加@COUNT_1@";
	public static String 累计增加血量上限描述 = "累计增加血量上限@COUNT_1@点";
	public static String 累计法术防御增加 = "累计法术防御增加@COUNT_1@";
	public static String 累计法术攻击增加 = "累计法术攻击增加@COUNT_1@";
	public static String 累计物理防御增加 = "累计物理防御增加@COUNT_1@";
	public static String 累计外功攻击增加 = "累计物理攻击增加@COUNT_1@";

	public static String gm请求你对反馈xx作出评价 = "gm请求你对反馈@STRING_1@作出评价，请您去评价";
	public static String gm回复了你的反馈XX = "gm回复了你的反馈@STRING_1@";
	public static String 玩家不在线不能发送评价 = "玩家不在线不能发送评价";
	public static String 玩家反馈主题为空 = "反馈主题不能为空";
	public static String 玩家反馈为空 = "反馈内容不能为空";
	public static String 反馈已关闭 = "反馈已关闭";
	public static String 反馈已关闭玩家不能在此反馈上反馈问题 = "反馈已关闭，不能在此反馈上反馈问题 ";
	public static String Gm还没有回复过反馈不能发送评价 = "Gm还没有回复过反馈不能发送评价";
	public static String Gm已经发送了评价 = "Gm已经发送了评价";
	public static String 没有这个反馈 = "没有这个反馈";
	public static String 不是自己提的反馈 = "不是自己提的反馈";
	public static String 玩家已经评价 = "玩家已经评价";
	public static String 反馈评价完成 = "反馈评价完成";
	public static String 反馈回复完成 = "反馈回复完成";
	public static String 你还没有反馈过消息 = "你还没有反馈过消息";
	public static String 反馈信息成功请耐心等待gm的回答 = "反馈信息成功，请耐心等待gm的回答";
	public static String 请不要使用外挂 = "请不要使用外挂";
	public static String 目前不提供缩包服务 = "目前不提供缩包服务";
	public static String 扩展背包成功提示 = "您已经成功将背包扩展到@COUNT_1@格";
	public static String 扩展到格子需要银子信息 = "背包扩展到@COUNT_1@格需要@COUNT_2@银子，确定吗？";

	public static String 您要锁魂的物品或锁魂符不存在 = "您要锁魂的物品或锁魂石不存在！";
	public static String 您要解魂的物品或解魂石不存在 = "您要解魂的物品或解魂精魄不存在！";
	public static String 您要解魂的装备上有锁魂物品 = "您要解魂的装备上镶嵌有锁魂宝石，不可执行此操作！";
	public static String 万灵榜装备不能锁魂 = "万灵榜装备不能锁魂。";
	public static String 锁魂符 = "锁魂石";
	public static String 高级锁魂符 = "高级锁魂石";
	public static String 解魂石 = "解魂精魄";
	public static String 红朝天龙 = "红朝天龙";
	public static String 如意锦囊 = "如意锦囊";
	public static String 易筋丹 = "易筋丹";
	public static String 鞭炮 = "鞭炮";
	public static String 青苹果 = "青苹果";
	public static String 水晶苹果 = "水晶苹果";
	public static String 天使之翼 = "天使之翼";
	public static String 您放入的不是锁魂符 = "您放入的不是锁魂石或高级锁魂石";
	public static String 您放入的不是解魂石 = "您放入的不是解魂精魄";
	public static String 您放入物品不能锁魂 = "您放入物品不能锁魂";
	public static String 您放入物品不能解魂 = "您放入物品不能解魂";
	public static String 您有物品解魂成功注意查看 = "您有物品解魂成功注意查看";
	public static String 锁魂物品不能做此操作 = "此物品已经被锁魂进入<f color='#f7e354'>保护状态</f>，为了保证您的财产不受损失，不能进行此操作。";
	public static String 锁魂物品不能邮寄 = "邮件中有物品已经被锁魂进入<f color='#f7e354'>保护状态</f>，为了保证您的财产不受损失，不能发送邮件。";
	public static String 高级锁魂物品不能做此操作 = "此物品已经被高级锁魂进入<f color='#f7e354'>冰冻状态</f>，为了保证您的财产不受损失，不能进行此操作。";
	public static String 解魂状态不能做此操作 = "此物品正在解魂状态，为了保证您的财产不受损失，不能进行此操作。";

	public static String text_codeactivity_001 = "活动都已结束,更多详情请参见论坛";
	public static String text_codeactivity_002 = "兑换激活码";

	public static String 装备耐久提示 = "您的@STRING_1@耐久还剩下@COUNT_1@点，请及时修理";
	public static String 原地复活需要绑银 = "原地复活需要@COUNT_1@绑银，您没有足够的银两";
	public static String 原地复活消耗50两绑银 = "原地复活消耗50两绑银，可用绑银不足会用银子代替";
	public static String 成功 = "成功";
	public static String 未到时间 = "未到时间";
	public static String 未知错误 = "未知错误";
	public static String 恭喜您获得等级礼包 = "恭喜您升级，获得@STRING_1@";
	public static String 恭喜你到达某级 = "恭喜你到达@COUNT_1@级，请前往邮件管理员处领取奖励邮件";
	public static String 使用后将替换身上已有的状态 = "使用后将替换身上已有的状态";
	public static String 您已经有了一个角色 = "您已经有了一个角色";
	public static String 进入某某区域 = "进入@STRING_1@";
	public static String 离开某某区域 = "离开@STRING_1@";
	public static String 您被某某杀死了 = "您被@PLAYER_NAME_1@杀死了";
	public static String 您被某某的宠物杀死了 = "您被@PLAYER_NAME_1@的宠物杀死了";
	public static String 您掉落了某某 = "您掉落了@STRING_1@";
	public static String 死亡提示邮件 = "死亡提示邮件";
	public static String 某级别某颜色装备名 = "@STRING_1@@STRING_2@(星级@STRING_3@)";
	public static String 某颜色物品名 = "@STRING_1@@STRING_2@";
	public static String 你的装备碎了 = "你的装备@STRING_1@由于没耐久了，破碎掉了，真遗憾啊";
	public static String 频道不存在 = "频道不存在";
	public static String 宝石不能卖店 = "宝石不能卖店";
	public static String 镶嵌了宝石的装备不能卖店 = "镶嵌了宝石的装备不能卖店";
	public static String 出售成功获得绑银 = "出售成功，获得@COUNT_1@绑银";
	public static String 排名未上榜 = "未上榜";
	public static String 你已经领过奖励了 = "你已经领过奖励了，给别人留点吧。";
	public static String 领取奖励成功 = "领取奖励成功";
	public static String 不是vip = "您不是VIP不能领取奖励";
	public static String 您已经领取了VIP奖励了 = "您已经领取了VIP@COUNT_1@的奖励";
	public static String 不能占领别国的城市 = "不能占领别国的城市";
	public static String 攻占城市需要交纳银子 = "攻占城市需要交纳@COUNT_1@银子";
	public static String 您已经占领了城市 = "您已经占领了城市，只能放弃了占领的城市才能申请占领其他城市";
	public static String 您已经申请攻占城市 = "您已经申请了攻占城市，不能再次申请";
	public static String 申请攻占城市 = "申请攻占城市";
	public static String 这个城市不能攻占 = "这个城市不能攻占";
	public static String 这个城市不是中立城市 = "这个城市不是中立城市";
	public static String 已经有人先申请了攻占这个城市 = "申请失败，已经有人先申请了攻占这个城市";
	public static String 恭喜您申请攻占成功 = "恭喜您申请攻占@STRING_1@成功";
	public static String 请与每日某点前申请 = "申请失败，请与每日@COUNT_1@时前申请";
	public static String 请与每日某点后申请 = "申请失败，请与每日1时后申请";
	public static String 需要繁荣度 = "申请失败，需要@COUNT_1@点繁荣度";
	public static String 攻占城市需要交纳银子繁荣度 = "攻占城市需要交纳@COUNT_1@银子，并且消耗@COUNT_2@点繁荣度";
	public static String A击败了B获得了城市 = "在城市争夺战中，@STRING_1@击败了@STRING_2@获得了@STRING_3@的管理权";
	public static String B失败了 = "在城市争夺战中，@STRING_2@攻打@STRING_3@失败";
	public static String A成功防守住了B的攻击 = "在城市争夺战中，@STRING_1@成功防守住了@STRING_2@的攻击，保住了@STRING_3@的管理权";
	public static String A胜利了获得了城市 = "在城市争夺战中，@STRING_1@胜利了，获得了@STRING_3@的管理权";
	public static String 只能在每天的时间领取奖励 = "只能在每天的@COUNT_1@时之前领取奖励";
	public static String 只有占领城市的宗派宗主才能领取奖励 = "只有占领城市的宗派宗主才能领取奖励";
	public static String 您已经领取了今日奖励 = "您已经领取了今日奖励";
	public static String 您领取了银子 = "您领取了城市奖励@STRING_1@银子";
	public static String 现在还不能进入地图 = "现在还不能进入地图";
	public static String 非国战状态不能在国外使用 = "非国战状态不能在国外使用";
	public static String 必须先申请王城 = "必须有宗派占领了@STRING_1@，才能申请攻占其他城市";
	public static String 中立城市必须二次封印开放后才能抢夺 = "中立城市必须二次封印开放后才能抢夺";
	public static String 您在这个国家没有占领城市 = "您在这个国家没有占领城市";
	public static String 您不是宗族族长不能执行此操作 = "您不是宗族族长不能执行此操作";
	public static String 申请放弃城市 = "申请放弃城市";
	public static String 您真的要放弃城市吗 = "您真的要放弃@STRING_1@吗？三思啊";
	public static String 确定放弃城市 = "确定放弃城市";
	public static String 宗派放弃城市 = "@STRING_1@放弃了对@STRING_3@@STRING_2@的占领，各位宗主快去申请占领吧，奖励丰厚，先到先得哦";
	public static String 被占领的城市 = "被占领的城市:";
	public static String 占领的宗派 = " 占领";
	public static String 被申请攻打的城市 = "今日申请攻打:";
	public static String 申请攻打的宗派 = " 申请攻打 ";
	public static String 城市信息一览 = "城市信息一览";
	public static String 城战信息 = "城战信息";
	public static String 某宗派挑战您所占领的城市 = "宗派@STRING_1@挑战你宗派所占领的城市@STRING_2@，城市争夺战将于@STRING_3@时@STRING_4@分开启，请相互通知，迎战宗派@STRING_1@";
	public static String 贵盟盟主申请了攻占城市 = "贵宗派宗主申请了攻占@STRING_1@，城市争夺战将于@STRING_2@时@STRING_3@分开启，请相互通知";
	public static String 某盟申请了攻占城市 = "@STRING_1@@STRING_2@宗派宗主申请了攻占@STRING_3@，城市争夺战将于@STRING_4@时@STRING_5@分开启";
	public static String 城市争夺战将于1分钟后开启 = "城市争夺战将于1分钟后开启，请参加的宗派做好准备，到报名的城战NPC处等待进入";
	public static String 城市争夺战将于2分钟后开启 = "城市争夺战将于2分钟后开启，请参加的宗派做好准备，到报名的城战NPC处等待进入";
	public static String 城市争夺战将于3分钟后开启 = "城市争夺战将于3分钟后开启，请参加的宗派做好准备，到报名的城战NPC处等待进入";
	public static String 城市争夺战开启 = "城市争夺战开启，请参加的宗派成员到报名NPC处进入";
	public static String 放弃国王 = "@STRING_1@放弃了@STRING_2@，@STRING_3@位置被剥夺，现在@STRING_4@@STRING_3@位置空缺，各位宗主快来抢夺吧";
	public static String 历练 = "历练";
	public static String 功勋 = "功勋";
	public static String 文采 = "文采";
	public static String 某国某人连斩 = "@STRING_1@@PLAYER_NAME_1@连续杀人达到@STRING_2@，不错哦!";
	public static String 某国某人连斩50 = "@STRING_1@@PLAYER_NAME_1@连续杀人达到@STRING_2@，好牛啊!";
	public static String 某国某人连斩100 = "@STRING_1@@PLAYER_NAME_1@连续杀人达到@STRING_2@，这就是实力的体现，无人能敌啊!";
	public static String 您的vip级别不足 = "您的vip级别不足，请参看vip说明";
	public static String 不能接取任务 = "不能接取任务";
	public static String 传送请求发送成功 = "传送请求发送成功";
	public static String 无敌buff = "无敌";
	public static String 支取失败 = "支取失败";
	public static String VIP商店 = "VIP商店";
	public static String 去充值 = "去充值";
	public static String 成为vip = "成为vip";
	public static String 每天18点19点期间不能使用此功能 = "该玩家为今日城战参与宗派成员，无法执行关押。";
	public static String 每天17点22点期间不能使用此功能 = "该玩家为今日国战参与成员，无法执行关押。";
	public static String 抢国战期间不能关人 = "抢国战期间不能关人";
	public static String 不能使用不在指定国家 = "不能使用，不在指定国家";
	public static String 寻宝成功 = "寻宝成功";
	public static String 不在指定地图上 = "不在指定地图上";
	public static String 获得xx = "获得@STRING_1@";
	public static String 只能在本国的npc处兑换 = "只能在本国的npc处兑换";
	public static String xx重新选择了有缘人进行活动 = "@STRING_1@重新选择了有缘人进行活动";
	public static String xx已经开始活动 = "@STRING_1@已经开始活动";
	public static String 请求过期xx选择了别人 = "请求过期，@STRING_1@选择了别人";
	public static String 刷新时间不到 = "刷新时间不到";
	public static String xx正在活动或今日活动次数已满 = "@STRING_1@正在活动或今日活动次数已满。";
	public static String xx不在线 = "@STRING_1@不在线";
	public static String 已经开始不能放弃 = "已经开始不能放弃";
	public static String 上次xx过期 = "上次@STRING_1@过期";
	public static String 今天布阵次数已够 = "今天布阵次数已够";
	public static String 今天添加灵石次数已够 = "今天添加灵石次数已够";
	public static String 还没放上交换物品 = "还没放上交换物品";
	public static String 你拒绝了xx = "你拒绝了@STRING_1@";
	public static String xx拒绝了你 = "@STRING_1@拒绝了你";
	public static String 第一名没有答题 = "第一名没有答题";
	public static String 答题活动已经完成 = "答题活动已经完成";
	public static String 本次答题结束 = "本次答题结束";
	public static String 本题答题时间结束 = "本题答题时间结束";
	public static String 首轮不能使用求助 = "首轮不能使用求助";
	public static String 第一名不能使用求助 = "第一名不能使用求助";
	public static String 获得文采值xx = "获得文采值@STRING_1@";
	public static String 求助最多只能使用xx = "求助最多只能使用@STRING_1@次";
	public static String 放大器最多只能使用xx = "放大器最多只能使用@STRING_1@次";
	public static String 现在还不能申请对方正在交换中 = "现在还不能申请，对方正在交换中";
	public static String 现在还不能申请你正在交换中 = "现在还不能申请，你正在交换中";
	public static String 可交换的俩种物品不匹配不可交换 = "可交换的俩种物品不匹配，不可交换";
	public static String 增加元气xx = "增加元气@STRING_1@";
	public static String 你已向xx发出传功申请 = "你已向@STRING_1@发出传功申请";
	public static String 反馈主题长度大于20字 = "反馈主题长度大于20字";
	public static String 反馈内容长度大于xx = "反馈内容长度大于@STRING_1@字";
	public static String 你反馈的内容是xx字超出了最大内容上限的200字 = "你反馈的内容是@STRING_1@字，超出了最大内容上限的200字";
	public static String 名字太长 = "名字太长";
	public static String 繁殖成功获得宠物蛋 = "繁殖成功，获得宠物蛋";
	public static String xx增加快乐值aa = "@STRING_1@增加快乐值@STRING_2@";
	public static String xx增加寿命值aa = "@STRING_1@增加寿命值@STRING_2@";
	public static String 这个宠物没有主人 = "这个宠物没有主人";
	public static String 未鉴定 = "未鉴定";
	public static String 辅宠 = "辅宠";
	public static String 强于主宠 = "强于主宠";
	public static String 合成后辅宠会消失你确定合体 = "合成后辅宠会消失，你确定合体吗？";
	public static String 合体确定 = "合体确定";
	public static String 初级 = "初级";
	public static String 中级 = "中级";
	public static String 高级 = "高级";
	public static String 特级 = "特级";
	public static String 终级 = "终级";
	public static String 今天已经领取 = "您今天已经领取过啦！";

	public static String 鸿天帝宝类型装备不能执行xx = "鸿天帝宝类型装备不能执行@STRING_1@操作";
	public static String 伏天古宝类型装备不能执行xx = "伏天古宝类型装备不能执行@STRING_1@操作";
	public static String 创建宗派花费锭 = "@COUNT_1@锭";
	public static String 你现在是混元至圣不能进行禅让 = "你现在是混元至圣不能进行禅让";
	public static String 你还没有宗派 = "你还没有宗派";
	public static String 踢出家族成功 = "踢出家族成功";
	public static String 你把宗派禅让给了xx = "你把宗派禅让给了@STRING_1@";
	public static String xx把宗派禅让给了你请查看邮件 = "@STRING_1@把宗派禅让给了你，请查看邮件";
	public static String 增加宗派繁荣度xx = "增加宗派繁荣度@STRING_1@点";
	public static String 你已经有结义 = "你已经有结义";
	public static String 获得称号xx = "获得称号@STRING_1@";
	public static String 此宠物是出战状态不能放生 = "此宠物是出战状态不能放生";
	public static String 宠物正在挂机不能出战 = "宠物正在挂机不能出战";
	public static String 您处于冰冻状态下不能使用坐骑 = "您处于冰冻状态下，不能使用坐骑";
	public static String 您处于战场中不能使用飞行坐骑 = "您处于战场中，不能使用飞行坐骑";
	public static String 兽形态不能使用飞行坐骑 = "兽形态不能使用飞行坐骑";
	public static String 国战活动中不能使用飞行坐骑 = "国战活动中，不能使用飞行坐骑";
	public static String 五方圣兽活动中不能使用飞行坐骑 = "五方圣兽活动中，不能使用飞行坐骑";
	public static String 渡劫中不能使用坐骑 = "渡劫中，不能使用坐骑";
	public static String 使用渡劫回魂丹 = "渡劫虚弱buff剩余时间:@STRING_1@小时@STRING_2@分@STRING_3@秒";
	public static String 渡劫虚弱buff消失 = "渡劫虚弱buff消失！";
	public static String 幽冥幻域活动中不能使用飞行坐骑 = "幽冥幻域活动中,不能使用飞行坐骑";
	public static String 迷城中不能使用飞行坐骑 = "圣兽阁中,不能使用飞行坐骑";
	public static String 召回了xx = "召回了@STRING_1@";
	public static String 召出了xx = "召出了@STRING_1@";
	public static String 不能直接删除好友有其他关系 = "不能直接删除好友，有其他关系";
	public static String 不能添加黑名单有其他关系 = "不能添加黑名单，有其他关系";
	public static String 不能给自己发 = "不能给自己发";
	public static String 已经是好友 = "已经是好友";
	public static String 发送添加好友请求 = "发送添加好友请求";
	public static String 对方不在线 = "对方不在线";
	public static String 对方拒绝了你的请求 = "对方拒绝了你的请求";
	public static String 包裹空间不够 = "包裹空间不够";
	public static String 飞行坐骑不能喂养 = "飞行坐骑不能喂养";
	public static String 非战斗坐骑不能喂养 = "非战斗坐骑不能喂养";
	public static String 喂养坐骑成功 = "喂养坐骑成功";
	public static String 正在进行仙缘活动不能使用飞行坐骑 = "正在进行仙缘活动不能使用飞行坐骑";
	public static String 正在进行论道活动不能使用飞行坐骑 = "正在进行论道活动不能使用飞行坐骑";
	public static String 没有有效的公告 = "没有有效的公告";
	public static String 已经领取了没有绑银可以领取 = "已经领取了，没有绑银可以领取";
	public static String 没有绑银可以领取 = "没有绑银可以领取";
	public static String 老玩家回归奖励 = "老玩家回归奖励";
	public static String 老玩家回归奖励描述 = "亲爱的老朋友,欢迎您回到飘渺寻仙曲,为了迎接您的回归，我们特为您准备的丰厚的大礼包，请去[昆仑圣殿NPC]<f color='0xffff00'>神仙姐姐</f>处填写您召回人的ID领取。\n填写召回ID为朋友赢取超级大礼!";
	public static String 老玩家回归奖励描述2 = "您已经召回@COUNT_1@位老朋友\n" + "您已获得@COUNT_2@次奖励\n" + "<f color='0xffff00'>获奖列表：</f>\n" + "每召回1位,送100两工资奖励\n" + "召回满5位,额外送100两工资+5个【2级宝石】\n" + "召回满10位,额外送200两工资+5个【3级宝石】\n" + "召回满20位,额外送500两工资+10个【3级宝石】\n" + "召回满50位,额外送2锭+30个【3级宝石】\n" + "如果您还没有领取召回id，请去[王城NPC]<f color='0xffff00'>神仙姐姐</f>处领取！";
	public static String 获得一个老玩家回归奖励xx已发送到您的邮箱 = "获得一个老玩家回归奖励@STRING_1@，已发送到您的邮箱";
	public static String 老玩家回归 = "老玩家回归";
	public static String 領取绑银xx = "領取绑银@STRING_1@";
	public static String 还没有找到有缘人请选择有缘人跟你一起进行活动 = "还没有找到有缘人，请选择有缘人跟你一起进行活动";
	public static String 距离太远 = "距离太远";
	public static String 对方不在凤栖梧桐区域 = "对方不在凤栖梧桐区域";
	public static String 你不在凤栖梧桐区域 = "你不在凤栖梧桐区域";
	public static String 对方不在指定地图 = "对方不在指定地图";
	public static String 不在指定地图 = "不在指定地图";
	public static String 对方不在指定国家 = "对方不在指定国家";
	public static String 你不在指定国家 = "你不在指定国家";
	public static String 还达不到使用条件 = "还达不到使用条件";
	public static String 没有对应的活动 = "没有对应的活动";
	public static String 限时道具已经到期 = "限时道具已经到期";
	public static String 你获得限时坐骑时间到就会自动消失 = "你获得限时坐骑，时间到就会自动消失";
	public static String 徒弟 = "徒弟";
	public static String 你已经达到xx级可以出师 = "你已经达到@STRING_1@级，可以出师";
	public static String 你的徒弟xx已经出师 = "你的徒弟@STRING_1@已经出师";
	public static String 师傅不存在 = "师傅不存在";
	public static String xx拒绝了你的交换请求 = "@STRING_1@拒绝了你的交换请求";
	public static String 你还没有家族 = "你还没有家族";
	public static String 你们家族还没有设置加柴的人不能加柴 = "你们家族还没有设置加柴的人，不能加柴";
	public static String 你不是加柴人不能加柴 = "你不是加柴人不能加柴";
	public static String 添加灵石成功 = "添加灵石成功";
	public static String 不是在你家族驻地 = "不是在你家族驻地";
	public static String 你还没有驻地 = "你还没有驻地";
	public static String 布阵成功 = "布阵成功";
	public static String 你还没有徒弟 = "你还没有徒弟";
	public static String 你的徒弟已经不存在了 = "你的徒弟已经不存在了";
	public static String 你还没有师傅 = "你还没有师傅";
	public static String 师傅添加成功 = "师傅添加成功";
	public static String 徒弟添加成功 = "徒弟添加成功";
	public static String xx拒绝了你的拜师请求 = "@STRING_1@拒绝了你的拜师请求";
	public static String 你拒绝了xx的拜师请求 = "你拒绝了@STRING_1@的拜师请求";
	public static String xx拒绝了你的收徒请求 = "@STRING_1@拒绝了你的收徒请求";
	public static String 你拒绝了xx的收徒请求 = "你拒绝了@STRING_1@的收徒请求";
	public static String 结义已经解散 = "结义已经解散";
	public static String 使用兄弟令完成请等待他们的到来 = "使用@STRING_1@完成，请等待他们的到来";
	public static String 使用了兄弟令需要把你召唤到他身边 = "使用了兄弟令,需要把你召唤到他(她)身边:";
	public static String xx使用了兄弟令需要把你召唤到他身边xx = "@STRING_1@使用了@STRING_3@,需要把你召唤到他(她)身边:@STRING_2@";
	public static String 你还没有结义 = "你还没有结义";
	public static String xx不在线不能传送 = "@STRING_1@不在线，不能传送";
	public static String xx在家族地图中你俩不是一个家族不能到达 = "@STRING_1@在家族地图中，你俩不是一个家族不能到达";
	public static String 你在家族地图中xx跟你不是一个家族不能到达 = "你在家族地图中，@STRING_1@跟你不是一个家族，不能到达";
	public static String 你放弃了传送 = "你放弃了传送";
	public static String 放弃了传送 = "放弃了传送";
	public static String XX放弃了传送 = "@STRING_1@放弃了传送";
	public static String 你包里没有宗主令 = "你包里没有宗主令";
	public static String 宗主令 = "宗主令";
	public static String 使用宗主令完成请等待他们的到来 = "使用宗主令完成，请等待他们的到来";
	public static String 使用了宗主令需要把你召唤到他身边 = "使用了宗主令,需要把你召唤到他(她)身边:";
	public static String xx使用了宗主令需要把你召唤到他身边xx = "@STRING_1@使用了宗主令,需要把你召唤到他(她)身边:@STRING_2@";
	public static String 俩次一样确认这样 = "俩次一样，确认这样。";
	public static String 宗主换了 = "宗主换了，不能传送";
	public static String 国内寻宝 = "国内寻宝";
	public static String 国外寻宝 = "国外寻宝";
	public static String 国内寻宝15体 = "国内寻宝(15体)";
	public static String 国外寻宝20体 = "国外寻宝(20体)";
	public static String 国内寻宝30体 = "国内寻宝(30体)";
	public static String 国外寻宝40体 = "国外寻宝(40体)";
	public static String 国内寻宝60体 = "国内寻宝(60体)";
	public static String 国外寻宝80体 = "国外寻宝(80体)";
	public static String 家族篝火实体 = "家族篝火实体";
	public static String 答题 = "答题";
	public static String 状元 = "状元";
	public static String 榜眼 = "榜眼";
	public static String 探花 = "探花";
	public static String 登录礼包 = "登录礼包";
	public static String 蓝钻会员VIP礼包 = "蓝钻会员VIP礼包";
	public static String 福星宝石袋3级 = "福星宝石袋(3级)";
	public static String 福星宝石袋2级 = "福星宝石袋(2级)";
	public static String 福星宝石袋1级 = "福星宝石袋(1级)";
	public static String 恭贺答题前三甲 = "恭贺答题前三甲";
	public static String 分别获得宝石袋奖励 = "分别获得宝石袋奖励";
	public static String 答题等待描述 = "<f>答题活动规则：</f>\n<f>1、每日10：00和17：00进行答题活动，确认参加后玩家可进入答题界面</f>\n<f>2、每题答题时间为25秒，玩家选择答案后不能再次选择，25秒内不进行操作则判定为答题错误</f>\n<f>3、每答对一题得1分并获得1点文采值，打错不得分且不获得文采值，每题结束后按积分多少进行排名，积分相同者按答题时间长短进行排名</f>\n<f>4、玩家点击帮助按钮，系统可自动帮助玩家跟随积分排行第一名的玩家进行选择</f>\n<f>5、玩家点击放大镜按钮，系统会自动帮助玩家跟随当前题目玩家最多选项进行选择</f>\n<f>6、玩家点击帮助或放大镜按钮后，不能手动进行答题操作</f>\n";
	public static String 反馈状态未处理 = "未处理";
	public static String 反馈状态等待 = "等待";
	public static String 反馈状态新 = "新";
	public static String 反馈状态关闭 = "关闭";
	public static String 反馈类型BUG = "BUG";
	public static String 反馈类型建议 = "建议";
	public static String 反馈类型投诉 = "投诉";
	public static String 反馈类型充值 = "充值";
	public static String 反馈类型其他 = "其他";
	public static String 角色已删除 = "角色已删除";
	public static String xx想收你为徒描述 = "@STRING_1@想收你为徒，职业：@STRING_2@，等级：@STRING_3@，境界：@STRING_4@";
	public static String xx想拜你为师描述 = "@STRING_1@想拜你为师，职业：@STRING_2@，等级：@STRING_3@，境界：@STRING_4@";
	public static String 全部 = "全部";
	public static String 国内仙缘15体 = "国内仙缘(15体)";
	public static String 国内仙缘30体 = "国内仙缘(30体)";
	public static String 国内仙缘60体 = "国内仙缘(60体)";
	public static String 国外仙缘20体 = "国外仙缘(20体)";
	public static String 国外仙缘40体 = "国外仙缘(40体)";
	public static String 国外仙缘80体 = "国外仙缘(80体)";
	public static String 国内论道15体 = "国内论道(15体)";
	public static String 国内论道30体 = "国内论道(30体)";
	public static String 国内论道60体 = "国内论道(60体)";
	public static String 国外论道20体 = "国外论道(20体)";
	public static String 国外论道40体 = "国外论道(40体)";
	public static String 国外论道80体 = "国外论道(80体)";
	public static String 老友一重礼 = "老友一重礼";
	public static String 老友二重礼 = "老友二重礼";
	public static String 老友三重礼 = "老友三重礼";
	public static String 初级炼妖石 = "初级培养石";
	public static String 中级炼妖石 = "中级培养石";
	public static String 高级炼妖石 = "高级培养石";
	public static String 跟随模式被动式 = "被动式";
	public static String 跟随模式主动式 = "主动式";
	public static String 跟随模式跟随式 = "跟随式";
	public static String 性别雄 = "雄";
	public static String 性别雌 = "雌";
	public static String 宠物职业外攻型 = "外攻型";
	public static String 宠物职业内攻型 = "内攻型";
	public static String 宠物职业敏捷型 = "敏捷型";
	public static String 宠物职业毒伤型 = "毒伤型";
	public static String 宠物性格忠诚 = "物攻宠";
	public static String 宠物性格精明 = "血防宠";
	public static String 宠物性格谨慎 = "法攻宠";
	public static String 宠物性格狡诈 = "暴伤宠";
	public static String 宠物品质普品 = "普品";
	public static String 宠物品质灵品 = "灵品";
	public static String 宠物品质仙品 = "仙品";
	public static String 宠物品质神品 = "神品";
	public static String 宠物品质圣品 = "圣品";
	public static String 宠物品质凡兽 = "凡兽";
	public static String 宠物品质灵兽 = "灵兽";
	public static String 宠物品质仙兽 = "仙兽";
	public static String 宠物品质神兽 = "神兽";
	public static String 宠物品质圣兽 = "圣兽";
	public static String 宠物稀有度随处可见 = "随处可见";
	public static String 宠物稀有度百里挑一 = "百里挑一";
	public static String 宠物稀有度千载难逢 = "千载难逢";
	public static String 宠物稀有度万年不遇 = "万年不遇";
	public static String 宠物终阶 = "终阶";
	public static String 宠物原始 = "原始";
	public static String 七情喜灵 = "七情喜灵";
	public static String 七情怒灵 = "七情怒灵";
	public static String 七情忧灵 = "七情忧灵";
	public static String 七情惧灵 = "七情惧灵";
	public static String 七情爱灵 = "七情爱灵";
	public static String 七情憎灵 = "七情憎灵";
	public static String 七情欲灵 = "七情欲灵";

	public static String 成长品质普通 = "普通";
	public static String 成长品质一般 = "一般";
	public static String 成长品质优秀 = "优秀";
	public static String 成长品质卓越 = "卓越";
	public static String 成长品质完美 = "完美";
	public static String 服务器风雪之巅 = "风雪之巅";
	public static String 服务器桃源仙境 = "桃源仙境";
	public static String 服务器昆仑圣殿 = "昆仑圣殿";
	public static String 服务器昆华古城 = "昆华古城";
	public static String 服务器玉幡宝刹 = "玉幡宝刹";
	public static String 服务器问天灵台 = "问天灵台";
	public static String 服务器百里沃野 = "百里沃野";
	public static String 服务器太华神山 = "太华神山";
	public static String 服务器燃烧圣殿 = "燃烧圣殿";
	public static String 服务器炼狱绝地 = "炼狱绝地";
	public static String 服务器东海龙宫 = "东海龙宫";
	public static String 服务器瑶台仙宫 = "瑶台仙宫";
	public static String 服务器峨嵋金顶 = "峨嵋金顶";
	public static String 服务器福地洞天 = "福地洞天";
	public static String 服务器黄金海岸 = "黄金海岸";
	public static String 服务器无极冰原 = "无极冰原";
	public static String 服务器云海冰岚 = "云海冰岚";
	public static String 服务器云波鬼岭 = "云波鬼岭";
	public static String 服务器鹊桥仙境 = "鹊桥仙境";
	public static String 服务器蓬莱仙阁 = "蓬莱仙阁";
	public static String 服务器国内本地测试 = "国内本地测试";
	public static String 宠物青龙 = "青龙";
	public static String 宠物玄武 = "玄武";
	public static String 宠物朱雀 = "朱雀";
	public static String 宠物白虎 = "白虎";
	public static String 宠物麒麟 = "麒麟";
	public static String xx国的xx实力超群运气绝佳获得了万灵榜xx的 = "@STRING_1@国的@STRING_2@，实力超群，运气绝佳获得了万灵榜@STRING_3@的";
	public static String 挂机勿扰 = "挂机勿扰";
	public static String 吃饭去了 = "吃饭去了";
	public static String 睡觉中 = "睡觉中";
	public static String 忙着呢 = "忙着呢";
	public static String 大 = "大";
	public static String 二 = "二";
	public static String 三 = "三";
	public static String 四 = "四";
	public static String 五 = "五";
	public static String 六 = "六";
	public static String 七 = "七";
	public static String 八 = "八";
	public static String 九 = "九";
	public static String 十 = "十";
	public static String 十一 = "十一";
	public static String 十二 = "十二";
	public static String 十三 = "十三";
	public static String 十四 = "十四";
	public static String 十五 = "十五";
	public static String 十六 = "十六";
	public static String 十七 = "十七";
	public static String 十八 = "十八";
	public static String 十九 = "十九";
	public static String 二十 = "二十";
	public static String 结义称号类型 = "结义";
	public static String 结义管理员npc = "结义管理员";
	public static String 创建宗派需要xx您银子不足 = "创建宗派需要@STRING_1@您银子不足";
	public static String 创建家族需要xx您银子不足 = "创建家族需要@STRING_1@您银子不足";
	public static String 空 = "空";
	public static String 坐骑一阶 = "一阶";
	public static String 坐骑二阶 = "二阶";
	public static String 坐骑三阶 = "三阶";
	public static String 坐骑四阶 = "四阶";
	public static String 坐骑五阶 = "五阶";
	public static String 坐骑六阶 = "六阶";
	public static String 坐骑七阶 = "七阶";
	public static String 坐骑八阶 = "八阶";
	public static String 坐骑九阶 = "九阶";
	public static String 坐骑十阶 = "十阶";
	public static String 坐骑十一阶 = "十一阶";
	public static String 坐骑十二阶 = "十二阶";
	public static String 坐骑十三阶 = "十三阶";
	public static String 坐骑十四阶 = "十四阶";
	public static String 坐骑一星 = "一星";
	public static String 坐骑二星 = "二星";
	public static String 坐骑三星 = "三星";
	public static String 坐骑四星 = "四星";
	public static String 坐骑五星 = "五星";
	public static String 坐骑六星 = "六星";
	public static String 坐骑七星 = "七星";
	public static String 坐骑八星 = "八星";
	public static String 坐骑九星 = "九星";
	public static String 坐骑十星 = "十星";
	public static String 天赋技能 = "天赋技能：";
	public static String 后天技能 = "后天技能：";
	public static String 先天技能 = "先天技能：";
	public static String 技能一 = "技能一：";
	public static String 技能二 = "技能二：";
	public static String 技能三 = "技能三：";
	public static String 技能四 = "技能四：";
	public static String 技能五 = "技能五：";
	public static String 技能六 = "技能六：";
	public static String 技能七 = "技能七：";
	public static String 技能八 = "技能八：";
	public static String 技能九 = "技能九：";
	public static String 技能十 = "技能十：";
	public static String 技能11 = "技能十一：";
	public static String 技能12 = "技能十二：";
	public static String 技能13 = "技能十三：";
	public static String 技能14 = "技能十四：";
	public static String 技能15 = "技能十五：";
	public static String 技能16 = "技能十六：";
	public static String 技能17 = "技能十七：";
	public static String 技能18 = "技能十八：";
	public static String 技能19 = "技能十九：";
	public static String 技能20 = "技能二十：";
	public static String 鉴定 = "鉴定";
	public static String 铭刻 = "铭刻";
	public static String 镶嵌 = "镶嵌";
	public static String 挖宝石 = "挖宝石";
	public static String 强化 = "强化";
	public static String 升级 = "升级";
	public static String 装星 = "装星";
	public static String 装备合成 = "装备合成";
	public static String 拾取到防爆包 = "拾取到防爆包";
	public static String 移动到防爆包 = "移动到防爆包";
	public static String 移动到仓库 = "移动到仓库";
	public static String 绑定 = "绑定";
	public static String 器灵附灵 = "装备附灵";
	public static String 器灵摘取 = "装备摘取";
	public static String 装备炼器 = "装备炼器";

	public static String 在线一重礼 = "在线礼第一重";
	public static String 在线二重礼 = "在线礼第二重";
	public static String 在线三重礼 = "在线礼第三重";
	public static String 在线四重礼 = "在线礼第四重";
	public static String qq在线活动标题 = "连续在线活动奖励";

	public static String 回归有礼礼包 = "回归礼包";

	public static String 祈福消耗银子提示 = "祈福消耗银子提示";
	public static String 香火不足是否消耗100两银票进行祈福 = "香火不足，是否消耗100两银票进行祈福，每日只提示一次";
	public static String 香火不足是否消耗100两银子进行祈福 = "香火不足，是否消耗100两银子进行祈福，每日只提示一次";
	public static String 香火不足是否消耗100两银子进行祈福详细提示1 = "香火不足，是否消耗@COUNT_1@个香火和@STRING_1@银子进行祈福，每日只提示一次";
	public static String 香火不足是否消耗100两银子进行祈福详细提示2 = "香火不足，是否消耗@STRING_1@银子进行祈福，每日只提示一次";
	public static String 某家族获得矿战胜利 = "恭喜@STRING_1@家族获得矿战胜利，可以次日在此领取奖励";
	public static String 家族取得灵矿战的胜利获得200武力值 = "家族取得灵矿战的胜利，获得200武力值";
	public static String 发言频率过快 = "发言频率过快";
	public static String 宗派获得管理权 = "恭喜@STRING_1@宗派获得@STRING_2@的管理权";
	public static String 镖局经营资金 = "镖局经营资金";
	public static String 这是昨天镖局经营资金 = "这是昨天镖局经营资金，请查收！";
	public static String 本镖局目前没有家族运营您可以为明天的运营竞标 = "本镖局目前没有家族运营，您可以为明天的运营竞标。";
	public static String 本镖局现在由某家族运营您可以为明天的运营竞标 = "本镖局现在由:@STRING_1@家族运营，您可以为明天的运营竞标。";
	public static String 您的竞标被他人超越附件中有税后金额请查收 = "您的竞标被他人超越，附件中有税后金额，请查收！";
	public static String 竞标被超越 = "竞标被超越";
	public static String 银子不足是否去充值 = "银子不足，是否去充值";
	public static String 积分不足是否去充值 = "非常抱歉，您的积分不够了，是否去充值？积分只有充值才会获得哦！";
	public static String 活跃度不足是否去充值 = "非常抱歉，您的活跃值不够了！赶快去做任务吧！";
	public static String 请输入镖局资金数详细 = "请输入资金，目前镖局拥有资金@COUNT_1@。投保所需支付的暂时违约金@COUNT_2@。您可以根据具体情况添加资金来维持镖局的正常运营";
	public static String 追加资金成功详细 = "追加资金成功，扣除税款后追加了@COUNT_1@银子，目前镖局拥有资金@COUNT_2@银子";
	public static String 投保成功详细 = "投保成功，花费@COUNT_1@银子，如果镖车被砸，交付任务后本镖局会以信件形式返还任务押金@COUNT_2@银子";
	public static String 您的镖车损毁详细 = "您的镖车损毁，镖局保护不利，赔付您@COUNT_1@银子。";
	public static String 镖局赔付 = "镖局赔付";
	public static String 任务押金归还详细 = "任务押金归还@COUNT_1@银子";
	public static String 您还能使用多少次 = " 您还能使用@COUNT_1@次";
	public static String 本地图禁止使用这个道具 = "本地图禁止使用@STRING_1@";
	public static String 绑银不足是否去充值 = "绑银不足，是否去充值";
	public static String 级 = "级";
	public static String 全部道具 = "全部道具";
	public static String 我想更快升级 = "我想更快升级";
	public static String 我想增强实力 = "我想增强实力";
	public static String 我想传情达意 = "我想传情达意";
	public static String _1级随身商店 = "1级随身商店";
	public static String _20级随身商店 = "20级随身商店";
	public static String _40级随身商店 = "40级随身商店";
	public static String _60级随身商店 = "60级随身商店";
	public static String _80级随身商店 = "80级随身商店";
	public static String _100级随身商店 = "100级随身商店";
	public static String _120级随身商店 = "120级随身商店";
	public static String _140级随身商店 = "140级随身商店";
	public static String _160级随身商店 = "160级随身商店";
	public static String _180级随身商店 = "180级随身商店";
	public static String _200级随身商店 = "200级随身商店";
	public static String _220级随身商店 = "220级随身商店";
	public static String _240级随身商店 = "240级随身商店";
	public static String _260级随身商店 = "260级随身商店";
	public static String _280级随身商店 = "280级随身商店";
	public static String _300级随身商店 = "300级随身商店";
	public static String VIP1商店 = "VIP1商店";
	public static String VIP2商店 = "VIP2商店";
	public static String VIP3商店 = "VIP3商店";
	public static String VIP4商店 = "VIP4商店";
	public static String VIP5商店 = "VIP5商店";
	public static String VIP6商店 = "VIP6商店";
	public static String VIP7商店 = "VIP7商店";
	public static String VIP8商店 = "VIP8商店";
	public static String VIP9商店 = "VIP9商店";
	public static String VIP10商店 = "VIP10商店";
	public static String VIP11商店 = "VIP11商店";
	public static String VIP12商店 = "VIP12商店";
	public static String VIP13商店 = "VIP13商店";
	public static String VIP14商店 = "VIP14商店";
	public static String VIP15商店 = "VIP15商店";
	public static String 成功领取vip奖励 = "成功领取@STRING_1@";

	public static String 限时抢购 = "限时抢购";
	public static String 积分抢购 = "积分抢购";
	public static String 全部商城 = "全部商城";
	public static String VIP商城 = "VIP商城";
	public static String 积分商城 = "积分商城";
	public static String 活跃商城 = "活跃商城";
	public static String 充值商城 = "充值商城";

	public static String 暂无充值记录 = "暂无充值记录!";

	public static String 你的充值已到账 = "你的充值已到账:@STRING_1@";

	public static String 你没有家族每天只能做一次 = "你没有家族,每天只能做一次!";
	public static String 接镖提示 = "使用:[@STRING_1@ @STRING_2@]接取押镖任务";

	public static String 个人运镖提示 = "个人运镖：\n押镖令颜色品质越高，接取押镖任务基础经验越多最高可获得2倍经验";

	public static String 开炉炼丹消耗银子 = "开炉炼丹消耗家族资金，丹炉颜色越高开出极品丹药的几率越高";
	public static String 炼丹炉 = "炼丹炉";
	public static String[] 炼丹炉银子 = new String[] { ",1.5锭家族资金", ",3.2锭家族资金", ",9锭家族资金", ",50锭家族资金", ",220锭家族资金" };
	public static String 炼丹炉银子2 = ",%s锭家族资金";

	public static String 目前你还没有接取斩妖降魔 = "目前你还没有接取斩妖降魔!";

	public static String 你还没有接取斩妖降魔 = "你还没有接取斩妖降魔!";

	public static String 星级已满不能刷新 = "星级已满,不能刷新";
	public static String 你的银子不足以刷新是否去充值 = "你的银子不足以刷新,是否去充值";
	public static String 你的银子不足是否去充值 = "你的银子不足,是否去充值";
	public static String 你的银子不足是否去充值_金额 = "你的银子不足@STRING_1@是否去充值";
	public static String 你没有选择炼妖石请选择炼妖石来炼妖 = "你没有选择培养液，请选择培养液来炼妖";

	public static String 八喜礼包 = "八喜礼包";
	public static String 八星礼包 = "八星礼包";

	public static String 坐骑名称 = "洪荒极皇录";
	public static String 坐骑名称半月 = "洪荒至尊录";
	public static String 当日鲜花 = "当日鲜花";
	public static String 当日糖果 = "当日糖果";
	public static String 恭喜您成为飘渺寻仙曲万人迷 = "恭喜您成为飘渺寻仙曲万人迷";
	public static String 恭喜您在xx排行榜中获得冠军 = "恭喜您在@STRING_1@排行榜中获得冠军，赢得拉风的风行坐骑。请注意查收附件！";

	public static String 玫瑰花束 = "玫瑰花束";
	public static String 飞行坐骑礼包30 = "飞行坐骑礼包(30天)";
	public static String 飞行坐骑礼包15 = "飞行坐骑礼包(15天)";
	public static String 飞行坐骑礼包7 = "飞行坐骑礼包(7天)";

	public static String 炼器所需物品 = "炼器符";
	public static String 玩家不能为空 = "玩家不能为空";
	public static String 装备不能为空 = "装备不能为空";
	public static String 装备必须放入到背包中 = "装备必须放入到背包中";
	public static String 装备没有器灵孔 = "装备没有器灵孔";
	public static String 器灵槽里还有器灵不能进行炼器 = "器灵槽里还有器灵不能进行炼器";
	public static String 没有炼器所需材料 = "没有炼器所需材料@STRING_1@";
	public static String 最多可以绑定3个器灵槽 = "最多可以绑定3个器灵槽";
	public static String 不能锁定装备器灵孔范围外的位置 = "不能锁定装备器灵孔范围外的位置";
	public static String 确定炼器信息 = "确定炼器信息";
	public static String 确定炼器信息吗 = "确定炼器信息吗？";
	public static String 您绑定了几个器灵槽共需花费 = "您绑定了@COUNT_1@个器灵槽，共需花费@COUNT_2@银子，确定开始炼器吗？";
	public static String 您绑定了几个器灵槽使用绑定物品因此装备绑定共需花费 = "您绑定了@COUNT_1@个器灵槽，共需花费@COUNT_2@银子，由于使用了绑定的@STRING_1@，因此装备将绑定，确定开始炼器吗？";
	public static String 您绑定了几个器灵槽 = "您没有绑定器灵槽，确定开始炼器吗？";
	public static String 您绑定了几个器灵槽使用绑定物品因此装备绑定 = "您没有绑定器灵槽，由于使用了绑定的@STRING_1@，因此装备将绑定，确定开始炼器吗？";
	public static String 炼器成功 = "炼器成功";
	public static String 物品不是器灵 = "物品不是器灵";
	public static String 主器灵不能为空 = "主器灵不能为空";
	public static String 经验洗练消耗描述 = "将消耗10589100经验进行洗练";
	public static String 银子洗练消耗描述 = "将消耗10两银子进行洗练";
	public static String 你没有这个物品 = "你没有这个物品";
	public static String 您的经验不足 = "您的经验不足";
	public static String 您的银子不足 = "您的银子不足";
	public static String 器灵属性变为 = "器灵@STRING_1@属性值变为@COUNT_1@";
	public static String 副器灵不能为空 = "副器灵不能为空";
	public static String 请正确放入物品 = "请正确放入物品";
	public static String 请放入器灵 = "请放入器灵";
	public static String 主器灵颜色不能低于副器灵颜色 = "主器灵颜色不能低于副器灵颜色";
	public static String 吞噬成功增加数值 = "吞噬成功增加@COUNT_1@点吞噬经验";
	public static String 镶嵌位置错误 = "镶嵌位置错误";
	public static String 类型不一致不能镶嵌到这个位置 = "类型不一致不能镶嵌到这个位置";
	public static String 这个位置已经镶嵌了器灵 = "这个位置已经镶嵌了器灵，请挖取后再进行镶嵌";
	public static String 镶嵌成功 = "镶嵌成功";
	public static String 挖取位置错误 = "挖取位置错误";
	public static String 这个位置上没有器灵 = "这个位置上没有器灵";
	public static String 挖取成功 = "挖取成功";
	public static String 挖取失败 = "挖取失败";
	public static String 您的器灵仓库中没有这个物品 = "您的器灵仓库中没有这个物品";
	public static String 您的背包中没有这个物品 = "您的背包中没有这个物品";
	public static String 器灵包已满 = "器灵包已满";
	public static String 恭喜你器灵升级了 = "恭喜你器灵升级了";
	public static String 主器灵已经升到最高级不需要吞噬了 = "主器灵已经升到最高级不需要吞噬了";
	public static String 这个器灵不能镶嵌在这种部位上 = "这个器灵不能镶嵌在这种部位上";
	public static String 不需要吞噬了 = "不需要吞噬了";
	public static String 没有器灵仓库 = "没有器灵仓库";
	public static String 器灵仓库出现问题 = "器灵仓库出现问题，请联系GM";
	public static String 炼器需要炼器符 = "炼器需要@STRING_1@";
	public static String 由于镶嵌物品绑定镶嵌后装备将绑定 = "由于镶嵌物品绑定，镶嵌后装备将绑定，确定镶嵌吗？";
	public static String 镶嵌提示 = "镶嵌提示";
	public static String 属性数值已经达到最大值不需要洗练了 = "属性数值已经达到这个颜色可以达到的最大值，不需要洗练了";
	public static String 器灵仓库中没有满足吞噬条件的器灵 = "器灵仓库中没有满足吞噬条件的器灵";
	public static String 吞噬提示 = "吞噬提示";
	public static String 是否将器灵融合 = "是否将@STRING_1@融合，@STRING_2@将获得@COUNT_1@魂值，确定吗？";
	public static String 全部吞噬提示 = "全部吞噬提示，主器灵吞噬过程中颜色提升将停止吞噬";
	public static String 是否将全部器灵融合 = "您想要@STRING_2@吞噬器灵仓库中的所有符合吞噬条件的器灵吗？";
	public static String 活动邮件内容 = "光棍节你想\"脱光\"吗？今天[当日鲜花榜]和[当日糖果榜]的冠军，且积分不低于9999即可获得极度拉风的飞行坐骑哦。快把鲜花和糖果送给心仪的TA，一起\"脱光\"吧！";
	public static String 恭喜xx国xx天降奇缘采得奇花异草橙色天人菊 = "恭喜@STRING_1@的@PLAYER_NAME_1@采花得到奇花异草橙色天人菊。";
	public static String 恭喜您获得经验礼包 = "恭喜您获得经验礼包";
	public static String 恭喜您在等级飞升系列活动中获得经验礼包 = "恭喜您在“等级飞升”系列活动中获得经验礼包，内含酒、贴、绑银等物品。请您查收附件！";

	public static String 怪物攻城活动刷怪 = "昆华古城！昆仑圣殿！灭魔神域！遭到妖兽袭击火速支援！";

	public static String 武圣争夺战半小时后一触即发您还没有报名报名参加吗 = "武圣争夺战半小时后一触即发，您还没有报名，报名参加吗？";

	public static String 家族白虎 = "家族白虎";
	public static String 家族青龙 = "家族青龙";
	public static String 家族朱雀 = "家族朱雀";
	public static String 家族玄武 = "家族玄武";
	public static String 奖励没录入 = "奖励没录入";
	public static String 任务没录入 = "任务没录入";

	public static String 定身 = "定身";
	public static String 眩晕 = "眩晕";
	public static String 定身且封魔 = "定身且封魔";
	public static String 火属性攻击增加描述 = "火属性攻击增加@COUNT_1@点";
	public static String 火属性攻击减少描述 = "火属性攻击减少@COUNT_1@点";
	public static String 冰属性攻击增加描述 = "冰属性攻击增加@COUNT_1@点";
	public static String 冰属性攻击减少描述 = "冰属性攻击减少@COUNT_1@点";
	public static String 风属性攻击增加描述 = "风属性攻击增加@COUNT_1@点";
	public static String 风属性攻击减少描述 = "风属性攻击减少@COUNT_1@点";
	public static String 雷属性攻击增加描述 = "雷属性攻击增加@COUNT_1@点";
	public static String 雷属性攻击减少描述 = "雷属性攻击减少@COUNT_1@点";
	public static String 火属性防御增加描述 = "火属性防御增加@COUNT_1@点";
	public static String 火属性防御减少描述 = "火属性防御减少@COUNT_1@点";
	public static String 冰属性防御增加描述 = "冰属性防御增加@COUNT_1@点";
	public static String 冰属性防御减少描述 = "冰属性防御减少@COUNT_1@点";
	public static String 风属性防御增加描述 = "风属性防御增加@COUNT_1@点";
	public static String 风属性防御减少描述 = "风属性防御减少@COUNT_1@点";
	public static String 雷属性防御增加描述 = "雷属性防御增加@COUNT_1@点";
	public static String 雷属性防御减少描述 = "雷属性防御减少@COUNT_1@点";
	public static String 减少对方火属性防御描述 = "减少对方火属性防御@COUNT_1@点";
	public static String 增加对方火属性防御描述 = "增加对方火属性防御@COUNT_1@点";
	public static String 减少对方冰属性防御描述 = "减少对方冰属性防御@COUNT_1@点";
	public static String 增加对方冰属性防御描述 = "增加对方冰属性防御@COUNT_1@点";
	public static String 减少对方风属性防御描述 = "减少对方风属性防御@COUNT_1@点";
	public static String 增加对方风属性防御描述 = "增加对方风属性防御@COUNT_1@点";
	public static String 减少对方雷属性防御描述 = "减少对方雷属性防御@COUNT_1@点";
	public static String 增加对方雷属性防御描述 = "增加对方雷属性防御@COUNT_1@点";
	public static String 减血 = "减血";
	public static String 撕裂描述 = "撕裂效果，每@COUNT_1@秒减少气血@COUNT_2@，只有当血量达到血上限后才会停止";
	public static String 如花的铃铛 = "如花的铃铛";
	public static String 如花的拨浪鼓 = "如花的拨浪鼓";
	public static String 牛魔王的戒尺 = "牛魔王的戒尺";
	public static String 牛魔王的藤条 = "牛魔王的藤条";
	public static String 舜的慧剑 = "舜的慧剑";
	public static String 舜的慧刀 = "舜的慧刀";
	public static String 太虚幻境 = "太虚幻境";
	public static String 太虚幻境元神 = "太虚幻境元神";
	public static String 玉虚迷境 = "玉虚迷境";
	public static String 玉虚迷境元神 = "玉虚迷境元神";
	public static String 清虚苦境 = "清虚苦境";
	public static String 清虚苦境元神 = "清虚苦境元神";
	public static String 恭喜你成功通关副本 = "恭喜你成功通关副本";
	public static String 恭喜你成功通关副本获得通关奖励 = "恭喜你成功通关副本，您的通关奖励已经通过邮件发送，请注意查收";
	public static String 恭喜通关副本成功 = "恭喜通关副本成功";
	public static String 附件为您通关副本得到的奖励 = "附件为您通关副本得到的奖励，请注意查收，祝您游戏愉快！";
	public static String 您没有队伍无法获得奖励 = "您没有队伍无法获得奖励";
	public static String 您所在的队伍副本不是这个副本无法获得奖励 = "您所在的队伍副本不是这个副本无法获得奖励";
	public static String 奈何绝境 = "奈何绝境";
	public static String 奈何绝境元神 = "奈何绝境元神";
	public static String 孟婆的迷魂汤 = "孟婆的迷魂汤";
	public static String 孟婆的忘情水 = "孟婆的忘情水";
	public static String 副本入口 = "副本入口";
	public static String 元神副本入口 = "元神副本入口";
	public static String 太虚幻境_元神111 = "111级太虚幻境(元神)";
	public static String 玉虚迷境_元神131 = "131级玉虚迷境(元神)";
	public static String 清虚苦境_元神151 = "151级清虚苦境(元神)";
	public static String 奈何绝境_元神171 = "171级奈何绝境(元神)";
	public static String 太虚幻境奖励 = "太虚幻境奖励";
	public static String 玉虚迷境奖励 = "玉虚迷境奖励";
	public static String 清虚苦境奖励 = "清虚苦境奖励";
	public static String 奈何绝境奖励 = "奈何绝境奖励";
	public static String 太虚幻境_元神奖励 = "太虚幻境(元神)奖励";
	public static String 玉虚迷境_元神奖励 = "玉虚迷境(元神)奖励";
	public static String 清虚苦境_元神奖励 = "清虚苦境(元神)奖励";
	public static String 奈何绝境_元神奖励 = "奈何绝境(元神)奖励";
	public static String 太虚幻境111 = "111级太虚幻境";
	public static String 玉虚迷境131 = "131级玉虚迷境";
	public static String 清虚苦境151 = "151级清虚苦境";
	public static String 奈何绝境171 = "171级奈何绝境";
	public static String 小队没有副本 = "小队没有副本";
	public static String 没有小队不能进入小队副本 = "没有小队不能进入小队副本";
	public static String 本尊级别不足 = "本尊级别不足";
	public static String 元神级别不足 = "元神级别不足";
	public static String 没有开启元神 = "没有开启元神";
	public static String 不是本尊不能进入本尊模式副本 = "不是本尊，不能进入本尊模式副本。";
	public static String 不是元神不能进入元神模式副本 = "不是元神，不能进入元神模式副本。";
	public static String 聊天文字太长 = "聊天文字太长";

	public static String 充值金额 = "充值金额";
	public static String 充值方式 = "充值方式";
	public static String 成就奖励 = "成就奖励";
	public static String 达成成就 = "恭喜你达成成就:@STRING_1@";
	public static String 恭喜达成成就 = "恭喜@STRING_1@达成了成就:@STRING_2@";
	public static String 只能在自己家族驻地操作 = "只能在自己家族驻地操作";
	public static String 角色不存在 = "角色不存在";
	public static String 无效的充值方式 = "无效充值方式";
	public static String 无效的充值行为 = "无效的充值行为";
	public static String 充值提交成功请稍后在充值记录中查询 = "充值提交成功,请稍后在充值记录中查询!";
	public static String 提交异常请联系GM = "提交异常,请联系GM";
	public static String 一键收获 = "一键收获";
	public static String 提升资源等级升级 = "提升资源等级升级-";
	public static String 偷取了一个 = " 偷取了一个 ";
	// ^.*='(.*)' max.*$ "\1", games.xml
	@DonotTranslate
	public static String[] allMapDisplayName = { "绝尘岭", "昆仑要塞秘境", "龙凤阁", "朱雀圣坛", "鬼都", "桐柏山", "仙府", "福地洞天一层", "太华神山", "青龙秘境", "福地洞天六层", "风陵渡", "幽海秘境", "无极冰原一层", "昆仑圣殿", "福地洞天五层", "福地洞天九层", "云波鬼岭", "福地洞天三层", "玉龙瀑", "福地洞天四层", "福地洞天八层", "清虚苦境", "太虚幻境_元神", "无极冰原八层", "宠物岛", "玲珑塔四层", "飘渺阁", "浮生若梦", "玲珑塔七层", "福地洞天十层", "太华之顶", "云梦泽", "奈何绝境元神", "千层塔四层", "家族地图", "东海龙宫", "无极冰原七层", "密室", "戮神墓地", "王城秘境", "朱雀秘境", "玉虚迷境_元神", "无极冰原三层", "千层塔六层", "福地洞天一层", "监狱", "封神台", "无极冰原二层", "永安秘境", "博浪沙", "问天灵台", "无极冰原六层", "玄武圣坛", "奈何绝境", "玲珑塔八层", "逍遥津", "挂机_山地", "白虎秘境", "黄金海岸", "地狱魔渊", "回声谷", "摩天崖", "玄武秘境", "佳梦关", "玉虚迷境元神", "无极冰原五层", "福地洞天二层", "福地洞天三层", "洞府", "福地洞天二层", "边疆", "神兽岛", "挂机_监狱", "迷踪林", "沧浪水", "福地洞天五层", "麒麟圣坛", "玲珑塔三层", "比翼奇缘", "王城密道", "玲珑塔六层", "圣兽岛", "东海之滨", "鹊桥仙境", "清虚苦境_元神", "青龙圣坛", "百草境", "玲珑塔五层", "白虎圣坛", "挂机_沙漠", "玲珑塔一层", "清虚苦境元神", "福地洞天七层", "炼狱绝地", "昆华古城", "小隐寺", "太虚幻境", "千层塔一层", "无极冰原四层", "玲珑塔九层", "麒麟秘境", "仙界御花园", "古城秘境", "福地洞天四层", "擂台", "桃源仙境", "玲珑塔二层", "福地洞天六层", "千层塔五层", "玲珑塔十层", "灭魔神域", "风雪之巅", "皇宫", "太虚幻境元神", "昆仑要塞", "燃烧圣殿", "飞升崖", "千层塔二层", "福地洞天七层", "化外之境", "挂机_沼泽", "玉虚迷境", "千层塔三层", "天虹云河", "落星玉台", "般若宝刹", "九重云霄", "普通圣兽阁", "豪华圣兽阁", "至尊圣兽阁", "鲜血魔窟", "鲜血祭坛", "鲜血之桥", "落月山谷", "清潭洞月" };
	public static String[] translateMapDisplayName = allMapDisplayName;
	// 所有区域名
	public static String 出生点 = "出生点";
	public static String 千层塔 = "千层塔";
	public static String 安全区 = "安全区";
	public static String 凤栖梧桐 = "凤栖梧桐";
	public static String 摆摊 = "摆摊";
	public static String 摆摊区 = "摆摊区";
	public static String 监狱 = "监狱";
	public static String 黄泉 = "黄泉";
	public static String 天玄 = "天玄";
	public static String 阴浊 = "阴浊";
	public static String 阳清 = "阳清";
	public static String 洪荒 = "洪荒";
	public static String 混沌 = "混沌";
	public static String 别人正在采这朵花你不能同时进行 = "别人正在采这朵花，你不能同时进行";
	public static String 一天最多只能采xx朵你今天采集数已满 = "一天最多只能采@COUNT_1@朵，你今天采集数已满";
	public static String 采花大盗 = "采花大盗";
	public static String 背包空间不足请整理背包在采 = "背包空间不足，请整理背包在采";
	public static String 你还没有同意采花不能采花 = "你还没有同意采花，不能采花";
	public static String 只有达到XX级才能采花 = "只有达到@COUNT_1@级才能采花";
	public static String 活动结束你不能再采花 = "活动结束，你不能再采花。";
	public static String 采花大盗活动已开启110级以上玩家可以参与活动时间 = "<采花大盗>活动已开启，110级以上玩家可以参与，需要消耗15点体力，活动地点：中立地区所有野外地图，活动时间：11:00-11:30；21:30-22:00。";
	public static String 半边莲 = "半边莲";
	public static String 报春花 = "报春花";
	public static String 艳山姜 = "艳山姜";
	public static String 白玉兰 = "白玉兰";
	public static String 天人菊 = "天人菊";
	public static String 草叶花种 = "草叶花种";
	public static String 采花活动结束 = "采花活动结束";
	public static String 活动还没开始请等待活动开始在参加 = "活动还没开始，请等待活动开始在参加。";
	public static String 赶紧去找花开始采花吧 = "赶紧去找花，开始采花吧。";
	public static String 您已参加了采花大盗活动 = "您已参加了采花大盗活动";
	public static String 你体力值不够需要15体力 = "你体力值不够，需要15体力";
	public static String 你级别不够需要110级以上才能开始 = "你级别不够，需要110级以上才能开始。";
	public static String 采花大盗活动 = "采花大盗活动";
	public static String 参与 = "参与";
	public static String 采花大盗活动已开启等级大于110级的玩家可以在灭魔神域百花仙子处参加活动 = "采花大盗活动已开启，等级大于110级的玩家可以在灭魔神域‘百花仙子’处参加活动。前往中立野外地区采集奇花异草，兑换海量经验。";
	public static String 采花大盗活动已结束持有奇花异草的玩家请及时前往灭魔神域百花仙子处兑换经验避免花朵枯萎 = "采花大盗活动已结束。持有奇花异草的玩家请及时前往灭魔神域‘百花仙子’处兑换经验，避免花朵枯萎。";
	public static String 没有这么多物品 = "没有这么多物品";
	public static String 经验池已满不能在兑换 = "经验池已满，不能在兑换";
	public static String 今天已采 = "今天已采:";
	public static String 今天已采xx一天允许采几个 = "今天已采@STRING_1@/@STRING_2@";
	public static String 地图出现了xx的花大家快去采吧 = "地图出现了@STRING_1@的花，大家快去采吧。";
	public static String 采花获得稀有种子 = "采花获得稀有种子";
	public static String 采花获得稀有种子xx = "采花获得稀有种子@STRING_1@";
	public static String 活动结束采集失败 = "活动结束，采集失败";
	public static String 采花得到一朵 = "采花得到一朵";
	public static String 采花得到一朵XX = "采花得到一朵@STRING_1@";
	public static String 你已到达凤栖梧桐请等待XX到达进行XX = "你已到达凤栖梧桐，请等待@STRING_1@到达，进行@STRING_2@";
	public static String 与你进行XX的XX已经到达凤栖梧桐 = "与你进行@STRING_1@的@STRING_2@已经到达凤栖梧桐";
	public static String 与你进行XX的XX已经到达XX凤栖梧桐 = "与你进行@STRING_1@的@STRING_2@已经到达@STRING_3@的凤栖梧桐";
	public static String 已经离开凤栖梧桐区域将不能得到经验 = "已经离开凤栖梧桐区域，将不能得到经验";
	public static String xx已经离开凤栖梧桐区域将不能得到经验 = "@STRING_1@已经离开凤栖梧桐区域，将不能得到经验";
	public static String 国内仙缘 = "国内仙缘";
	public static String 国外仙缘 = "国外仙缘";
	public static String 国内论道 = "国内论道";
	public static String 国外论道 = "国外论道";
	public static String 放弃了 = "放弃了";
	public static String 请重新选择有缘人 = ",请重新选择有缘人。";
	public static String XX放弃了xx请重新选择有缘人 = "@STRING_1@放弃了@STRING_2@,请重新选择有缘人。";
	public static String XX放弃了xx = "@STRING_1@放弃了@STRING_2@。";
	public static String 背包已满请整理再来 = "背包已满，请整理再来";
	public static String npc刷新这个npc已经消失 = "npc刷新，这个npc已经消失";
	public static String 天神领取成功 = "领取成功";
	public static String XX已经领取过了一天只能领取一次 = "@STRING_1@已经领取过了，一天只能领取一次";
	public static String 采花 = "采花";
	public static String 副本里不能使用 = "副本里不能使用";
	public static String 圣兽阁里不能使用 = "圣兽阁里不能使用";
	public static String xx灵气已经平稳xx出现 = "@STRING_1@灵气已经平稳，@STRING_2@出现";
	public static String XX国XX灵气已经平稳XX出现 = "@STRING_1@国@STRING_2@灵气已经平稳，@STRING_3@出现";
	public static String 地区天生异象灵气异动星辰曜青天 = "地区天生异象，灵气异动，星辰曜青天！疑似有异宝就要现世";
	public static String xx地区天生异象灵气异动星辰曜青天 = "@STRING_1@地区天生异象，灵气异动，星辰曜青天！疑似有异宝就要现世";
	public static String xx国xx地区天生异象灵气异动星辰曜青天 = "@STRING_1@国@STRING_2@地区天生异象，灵气异动，星辰曜青天！疑似有异宝就要现世";
	public static String 长虹贯日灵气混乱 = "长虹贯日，灵气混乱，海上生明月！疑似有异宝将要现世";
	public static String xx长虹贯日灵气混乱 = "@STRING_1@长虹贯日，灵气混乱，海上生明月！疑似有异宝将要现世";
	public static String xx国xx长虹贯日灵气混乱海上生明月 = "@STRING_1@国@STRING_2@长虹贯日，灵气混乱，海上生明月！疑似有异宝将要现世";
	public static String 遮天蔽日灵气暴乱 = "遮天蔽日，灵气暴乱，混沌临九天！10分钟后有异宝现世";
	public static String XX遮天蔽日灵气暴乱 = "@STRING_1@遮天蔽日，灵气暴乱，混沌临九天！10分钟后有异宝现世";
	public static String xx国xx遮天蔽日灵气暴乱 = "@STRING_1@国@STRING_2@遮天蔽日，灵气暴乱，混沌临九天！10分钟后有异宝现世";
	public static String 死亡掉落万灵榜装备 = "死亡掉落万灵榜装备";
	public static String 天榜 = "天榜";
	public static String 地榜 = "地榜";
	public static String 装备逃跑提示邮件 = "装备逃跑提示邮件";
	public static String 职业不匹配 = "职业不匹配";
	public static String 您的鸿天帝宝 = "您的鸿天帝宝";
	public static String 由于您长时间离开舍您而去 = "由于您长时间离开，舍您而去！请您不要过分悲伤，它还会再次出现寻找有缘之人";
	public static String 您的鸿天帝宝XX由于您长时间离开舍您而去 = "您的鸿天帝宝@STRING_1@由于您长时间离开，舍您而去！请您不要过分悲伤，它还会再次出现寻找有缘之人";

	public static String 不能向自己请求收徒 = "不能向自己请求收徒";
	public static String 不在线 = "不在线";
	public static String 不能向自己请求拜师 = "不能向自己请求拜师";
	public static String 叛出了师门 = "叛出了师门";
	public static String 把你逐出师门 = "把你逐出师门";
	public static String 保存记录失败 = "保存记录失败";
	public static String 系统错误 = "系统错误";
	public static String 的徒弟 = "的徒弟";
	public static String 级别达到出师出师成功 = "级别达到出师，出师成功";
	public static String 采花兑换经验你可以拖动格子内的物品或自动到此处进行经验兑换 = "采花兑换经验:你可以拖动格子内的物品或自动到此处进行经验兑换，花的颜色品质越高可兑换的经验越多。";
	public static String 你选择了XX做为你的有缘人将会替换原有有缘人 = "你选择了@STRING_1@做为你的有缘人，将会替换原有有缘人，请等待对方同意";
	public static String 你选择了XX做为你的有缘人请等待对方同意 = "你选择了@STRING_1@做为你的有缘人，请等待对方同意";
	public static String 活动正在进行中 = "活动正在进行中";
	public static String 等级大于XX不能接受传功 = "等级大于@COUNT_1@不能接受传功";
	public static String 对方等级小于xx级不能传功 = "对方等级小于@COUNT_1@级，不能传功";
	public static String 对方等级大于xx级不能传功 = "对方等级大于@COUNT_1@级，不能传功";
	public static String 传功石 = "传功石";
	public static String 小助手已经同意 = "小助手已经同意您的请求";
	public static String 小助手已经到达 = "小助手已经到达凤栖梧桐下";

	public static String 方舟通行证 = "方舟通行证";
	public static String 头等舱钥匙 = "头等舱钥匙";
	public static String 船长室钥匙 = "船长室钥匙";
	public static String 神秘钥匙 = "神秘钥匙";
	public static String 贡献卡 = "贡献卡";

	public static String 六一勋章碎片 = "六一勋章碎片";
	public static String 端午勋章碎片 = "端午勋章碎片";
	public static String 周年庆勋章碎片 = "周年庆勋章碎片";
	public static String 恭喜您获得了奖励 = "奖励已成功发到您的背包，请查看。";
	public static String 请收藏好 = "请收藏好该碎片，月末可以兑换神秘大礼！";
	public static String 六一专属稀有宠物碎片 = "六一专属稀有宠物碎片";
	public static String 魅力 = "魅力";
	public static String 大使技能 = "大师技能";
	public static String 鲜花活动 = "鲜花活动";
	public static String 糖果活动 = "糖果活动";
	public static String 当日棉花糖 = "当日棉花糖";
	public static String 孩子王大比拼 = "【活动】孩子王，大比拼";
	public static String 孩子王大比拼_tw = "六一快樂之仙童大比拼";
	public static String 孩子王大比拼_reward = "恭喜您在孩子王，大比拼中获得了第";
	public static String 孩子王大比拼_reward_tw = "恭喜您在仙童大比拼中獲得了第";
	public static String 当日棉花糖_reward2 = "名，请您提取您的奖励！";
	public static String 中获得了第 = "中获得了第";
	public static String 名 = "名";
	public static String 浪漫七夕 = "浪漫七夕——才子佳人天注定";
	public static String 浪漫七夕内容 = "记得那是在2012年岁末，虽然要鲜花糖果积分在9999分以上才可获飞行坐骑吧，但当时却上演一场比PK还余情未了全民‘蕾丝’‘搞基’的争夺战。特此，不需要9999积分了！不需要搞基了！活动期间，每日鲜花糖果排行榜前三的玩家可免费获得玫瑰花束+飞行坐骑，玫瑰花束？全世界都会知道你们搞基的内幕。。";

	public static String 老玩家回归活动邮件标题 = "飘渺寻仙曲回归征集令";
	public static String 九宫秘境大乐透 = "九宫秘境大乐透";
	public static String 老玩家回归活动邮件内容 = "欢迎回归精彩热闹的飘渺寻仙曲世界，特送上回归大礼祝仙友在修仙路途傲视群雄，奖励请查收附件，飘渺寻仙曲有你更精彩！";

	public static String 没有与召回ID相匹配的玩家 = "没有与召回ID:@STRING_1@相匹配的玩家，请认真填写您朋友提供给您的召回id，为您和朋友赢得奖励！";
	public static String 召回id不能是自己 = "召回id不能是自己！";
	public static String 召回id是由纯数字组成的 = "召回id是由纯数字组成的！";
	public static String 回归锦囊 = "回归锦囊";
	public static String 飘渺寻仙曲欢迎您回家_title = "飘渺寻仙曲欢迎您回家";
	public static String 飘渺寻仙曲欢迎您回家_content = "亲爱的老朋友，飘渺寻仙曲欢迎您回家,在这里有最真挚的朋友，在这里有最激情的岁月,在这里有最纯洁的友谊，在这里有最宁静的港湾,在这里飘渺寻仙曲还为您准备了“回归锦囊”，请查收附件";
	public static String 飘渺寻仙曲欢迎您回家请去邮件领取回归奖励 = "飘渺寻仙曲欢迎您回家，请去邮件领取回归奖励";
	public static String 召回5人奖励锦囊 = "召回5人奖励锦囊";
	public static String 召回10人奖励锦囊 = "召回10人奖励锦囊";
	public static String 召回20人奖励锦囊 = "召回20人奖励锦囊";
	public static String 召回50人奖励锦囊 = "召回50人奖励锦囊";
	public static String 恭喜您召回满_title = "恭喜您召回满@COUNT_1@位玩家获得额外奖励";
	public static String 恭喜您召回满_content = "恭喜您成功召唤回@COUNT_1@位老战友！飘渺寻仙曲额外为您准备了“@STRING_1@”，及@STRING_2@两工资，请查收附件！";
	public static String 恭喜您成功召回了1位战友_title = "恭喜您成功召回了1位战友";
	public static String 恭喜您成功召回了1位战友_content = "恭喜您成功召唤回1位老战友！飘渺寻仙曲为您发放了100两工资,请去神仙姐姐处<召回工资奖励商店>查看工资！";
	public static String 恭喜您成功召回一位老友请去邮件查收您的召回奖励 = "恭喜您成功召回一位老友，请去邮件查收您的召回奖励";
	public static String 活动介绍_title = "活动介绍";
	public static String 活动介绍_content = "<f color='0xffff00'>召回玩家奖励：</f>\n每召回1位,送100两工资奖励\n召回满5位,额外送100两工资+5个[2级宝石]\n召回满10位,额外送200两工资+5个[3级宝石]\n召回满20位,额外送500两工资+10个[3级宝石]\n召回满50位,额外送2锭+30个[3级宝石]\n<f color='0xffff00'>回归玩家奖励：</f>\n对应等级橙酒*5.对应等级橙色封魔录*5";
	public static String 获取找回id错误请您重新登录 = "获取找回id错误，请您重新登录！";
	public static String 您已经领取过召回id了您的召回 = "您已经领取过召回id了,您的召回id为：@STRING_1@";
	public static String 请您先填写您朋友提供给您的召回id为您和朋友赢得奖励后再领取召回id = "请您先填写您朋友提供给您的召回id为您和朋友赢得奖励后再领取召回id！";
	public static String 您的召回id = "您的召回id";
	public static String 您的召回id2 = "无兄弟不飘渺寻仙曲——寻找遗失的兄弟，您的召回id为：@STRING_1@,请妥善保存您的召回id，把该id告知被您召回的朋友，您朋友输入id就可以为您和您的朋友赢得奖励！";
	public static String 恭喜您成功获得了召回id请查看邮件 = "恭喜您成功获得了召回id，请查看邮件！";
	public static String 没有登录了的玩家才能填写召回id = "15天没有登录了的玩家才能填写召回id！";
	public static String 填写召回id = "填写召回id";
	public static String 请认真填写召回您朋友提供给您的召回id = "请认真填写召回您朋友提供给您的召回id。";

	public static String 诸天秘宝 = "诸天秘宝";
	public static String 狂欢大礼 = "狂欢大礼";
	public static String 新年贺礼 = "七彩流光秘宝";
	public static String 双蛋好礼 = "双蛋好礼";
	public static String 诸天秘宝描述 = "<f color='0x78f4ff'>@STRING_1@将在@STRING_2@分钟后从仙界星域降临到【@STRING_3@】的@STRING_4@，有缘者皆可得之，请各位仙友做好准备</f>";
	public static String 诸天秘宝降临 = "<f color='0x78f4ff'>@STRING_1@已经从仙界星域降临至修仙星域【@STRING_2@】的——@STRING_3@，有缘者皆可得之，请各位仙友前往寻找</f>";
	public static String 光棍福音 = "光棍福音";
	// public static String 诸天秘宝描述 = "<f color='0x78f4ff'>四大天王赐福 ，@STRING_1@分钟后-@STRING_2@-将有大量的“赐福礼包”，刷出各位仙友者皆可得之，请各位仙友做好准备，前往寻找</f>";
	// public static String 诸天秘宝降临 = "<f color='0x78f4ff'>四大天王赐福，大量的“赐福礼包”在-@STRING_1@-刷出各位仙友者皆可得之，请各位仙友做好准备，前往寻找</f>";
	public static String 开启宝物 = "开启宝物";
	public static String 光复节活动邮件标题 = "光复节";
	public static String 光复节活动邮件内容 = "恭喜您在光复节活动中获得了:@STRING_1@";
	public static String 人气女王 = "人气女王";
	public static String 人气王子 = "人气王子";
	public static String 每日广播 = "截至目前，玩家@STRING_1@在此次“@STRING_2@”活动中，名列鲜花榜/糖果榜首位，其他玩家要加把劲儿啦！";
	public static String 结束广播 = "热烈恭喜玩家@STRING_1@在“人气王”活动中赢得“@STRING_2@”称号！";
	public static String 大使技能结束广播 = "热烈恭喜玩家@STRING_1@在“大师技能”排行中中赢得“@STRING_2@”称号！";
	public static String 已经完成xx次 = "你今天已经完成@STRING_1@次, 明天再来吧";
	public static String 祈福广播开出物品 = "恭喜@STRING_1@的<f color='0x78f4ff'>@STRING_2@</f>在祈福中幸运开出@STRING_3@@STRING_4@</f>，仙友们一起恭喜他/她吧！";
	public static String 祈福广播开出物品2 = "恭喜@STRING_1@的<f color='0x78f4ff'>@STRING_2@</f>在祈福中幸运开出@STRING_3@仙友们一起恭喜他/她吧！";
	public static String 不能烧香 = "您的经验不足.不能烧香!";
	public static String 烧香成功 = "恭喜您烧香成功!消耗经验";
	public static String 不能点蜡 = "您的家族工资不足.不能点蜡!";
	public static String 点蜡成功 = "恭喜您点蜡成功!消耗工资";
	public static String 不能送元宝 = "所需物品不足!请到商城购买“元宝”道具!";
	public static String 送元宝成功 = "恭喜您送元宝成功!消耗";
	public static String 获得修法值 = "恭喜您获得修法值:@STRING_1@";
	public static String 小游戏游戏内提示内容 = "特殊奖励：10关小游戏完成后，评分满60分，将额外奖励玩家1000修法值。\n一、飘渺寻仙曲小游戏种类：\n1、消除\n2、记忆力\n3、接水管\n二、飘渺寻仙曲小游戏玩法：\n1、【消除玩法】\n【消除游戏】开始后，在规定时间内，将屏幕操作区内<f color='0x00ff00'>25个</f>图块中消除<f color='0x00ff00'>4对相同的图块</f>，即可过关。\n\n2、【记忆力玩法】\n【记忆力游戏】开始后，在规定时间内，要记住上方出现的【宝石】的颜色和顺序，确定后，宝石隐藏，中间一排为将要摆放的【宝石格子】，最下面一排的宝石为本游戏所有可能出现的【宝石】，可以选择这些【宝石】摆放到中间的格子里去.\n\n3、【接水管玩法】\n【接水管游戏】开始后,在规定时间内,在屏幕操作区的任意空白方块处进行放置水管<f color='0x00ff00'>（注:不用从右侧查看栏中进行拖拽）</f>,将后侧查看栏中的水管放在合适的地方,使水管连接并通水,连接通水的方块越多,越接近胜利。";
	public static String 小游戏勾玉活动 = "因为“无限复活”活动，您的勾玉未被扣除!";
	public static String 小游戏时间不对 = "还没到小游戏活动开启时间，不能领取小游戏活动!";
	public static String 不能领取小游戏 = "您的小游戏体验券不足，不能领取小游戏活动!";
	public static String 领取小游戏成功 = "恭喜您领取小游戏成功!消耗小游戏体验券@STRING_1@张";
	public static String 小游戏买命失败 = "您的经验不足，不能买命！";
	public static String 小游戏参与券 = "小游戏参与券";
	public static String 小游戏买命失败3 = "您当前勾玉充足，无需继续购买！";
	public static String 小游戏买命失败2 = "已经达到最大购买次数，无法继续购买！";
	public static String 小游戏买命成功 = "恭喜您买命成功，您的小游戏剩余生命数为@STRING_1@,今天还可以购买@STRING_2@次,其中VIP次数为@STRING_3@";
	public static String 继续小游戏 = "欢迎回归飘渺寻仙曲OL小游戏，继续小游戏<f color='#FF0000'>无需在消耗【小游戏参与券】</f>，您是否确认继续？";
	public static String 确认重置小游戏 = "亲爱的仙友,是否确认重置当前小游戏,重置后将会从下一次小游戏开始！";
	public static String 重置小游戏成功 = "重置小游戏成功！";
	public static String 不需要重置 = "亲爱的仙友,您当前为第一关,无法在进行重置！";
	public static String 不需要重置2 = "亲爱的仙友，您当前参与小游戏的次数已满5次,无法进行重置,请明日再来参加！";
	public static String 不需要重置3 = "亲爱的仙友,当前时间段已过参与小游戏时间,无法进行重置,请明日再来参加！";
	public static String 小游戏生命不足 = "您今天已经完成5轮小游戏，不能继续小游戏!";
	public static String 无法开启小游戏 = "您的今天已经完成50关小游戏，不能继续小游戏!";
	public static String 小游戏操作错误 = "小游戏操作错误！";
	public static String 小游戏操作成功 = "小游戏操作成功！";
	public static String 小游戏结束 = "恭喜您完成今天小游戏的全部关卡，欢迎明天再来！";
	public static String 小游戏标题 = "欢迎来到飘渺寻仙曲OL小游戏";
	public static String 拼图游戏标题 = "小游戏--拼图";
	public static String 记忆游戏标题 = "小游戏--记忆力";
	public static String 对对碰游戏标题 = "小游戏--消除";
	public static String 管道游戏标题 = "小游戏--接水管";
	public static String 小游戏规则 = "小游戏规则：\n\n1、每日在【元气大仙】处参加小游戏时，需消耗【小游戏参与券】*1，小游戏共分为5轮，每轮10小关；\n2、小游戏种类：记忆力游戏、对对碰游戏、接水管游戏；\n3、时间规定：参与小游戏的时间为<f color='0x00ff00'>每日0：00—23：00，23：00</f>后将无法接取小游戏，<f color='0x00ff00'>每日凌晨0：00点</f>系统进行重置；\n4、游戏难度：1-3关为简单难度，4-6关为中等难度，7-10关为困难难度；\n5、每天免费获得3条生命，游戏失败将被扣除一个勾玉。<f color='0xff0000'>注：勾玉可使用当前升级经验的10%购买，每多购买一次，经验消耗多5%</f>；\n6、每关小游戏通关后，会获得过关元气奖励和积分，累计十关的积分，如果<f color='0xff0000'>满足60分</f>，将会获得额外元气奖励并进行世界公告；\n7、如强制退出或者被官员拉令召唤，继续参加小游戏时，将会从下一关开始";
	public static String 小游戏规则2 = "小游戏种类与玩法：\n\n一、飘渺寻仙曲小游戏种类：\n1、消除\n2、记忆力\n3、接水管\n二、飘渺寻仙曲小游戏玩法：\n1、【消除玩法】\n【消除游戏】开始后，在规定时间内，将屏幕操作区内<f color='0x00ff00'>25个</f>图块中消除<f color='0x00ff00'>4对相同的图块</f>，即可过关。\n2、【记忆力玩法】\n【记忆力游戏】开始后，在规定时间内，要记住上方出现的【宝石】的颜色和顺序，确定后，宝石隐藏，中间一排为将要摆放的【宝石格子】，最下面一排的宝石为本游戏所有可能出现的【宝石】，可以选择这些【宝石】摆放到中间的格子里去。\n3、【接水管玩法】\n【接水管游戏】开始后，在规定时间内，在屏幕操作区的任意空白方块处进行放置水管<f color='0x00ff00'>（注:不用从右侧查看栏中进行拖拽）</f>，将后侧查看栏中的水管放在合适的地方，使水管连接并通水，连接通水的方块越多，越接近胜利。";
	public static String 记忆小游戏说明 = "还剩下 @STRING_1@题 记忆力结果 真聪明 @STRING_2@题。  真可惜 @STRING_3@题。 要努力 @STRING_4@题。";
	public static String 对对碰小游戏说明 = "@STRING_1@,@STRING_2@/4";
	public static String 提前结束小游戏1 = "恭喜您达到最高分，点击按钮进入下一关！";
	public static String 小游戏一轮完成 = "恭喜您已经完成10关小游戏，今天还可以完成@STRING_1@关！";
	public static String 小游戏全部完成 = "恭喜您已经完成今天所有小游戏，欢迎明天再来！";
	public static String 拼图移动错误 = "移动错误！";
	public static String 提前结束小游戏2 = "小游戏操作时间到!";
	public static String 记忆游戏下一轮 = "记忆游戏进入下一轮!";
	public static String 买命确认框 = "您是否消耗@STRING_1@经验兑换1个勾玉 确定要买命吗？";
	public static String 游戏生命不足弹框 = "您当前状态无法进入下一关，是否用当前升级经验兑换勾玉？";
	public static String 重新领取小游戏 = "接取小游戏活动<f color='#FFFF00'>需要消耗【小游戏参与券】</f>，是否确定？<f color='0xff0000'>可参加次数(@STRING_1@/5)</f>";
	public static String 重新领取小游戏2 = "亲爱的仙友，您是否<f color='#FFFF00'>消耗【小游戏参与券】</f>接取小游戏活动？<f color='0xff0000'>可参加次数(@STRING_1@/5)</f>";
	public static String 小游戏世界通告 = "恭喜@STRING_1@国家的@STRING_2@在小游戏活动中全部通关，当前小游戏积分满60分，作为额外奖励1000点修法值，各位仙友为他(她)欢呼吧！！";
	public static String 渡劫世界通告 = "恭喜@STRING_1@国家的@STRING_2@成功渡过@STRING_3@，各位仙友为他(她)欢呼吧！！";
	public static String 减免渡劫雷伤 = "减少雷击伤害";
	public static String 渡劫不允许用药 = "此劫不允许使用此物品";
	public static String 观礼飞升获得经验 = "因为观礼飞升，您获得了@STRING_1@点经验！";
	public static String 渡劫中不可以使用拉令 = "正在渡劫中，无法使用拉令！";
	public static String 减免渡劫雷伤描述 = "减少@STRING_1@次雷击@STRING_2@%伤害";
	public static String 渡劫换装失败 = "你正处于渡劫状态，不允许更换装备！";
	public static String 渡劫道具失败 = "此物品只有天劫中可以使用！";
	public static String 天劫拉人倒计时 = "天劫倒计时:";
	public static String 空岛大冒险结束倒计时 = "结束倒计时:";
	public static String 天劫功能开启倒计时 = "渡劫开启倒计时:";
	public static String 增防buff = "增加防御";
	public static String 增防buff描述 = "增强防御百分比";
	public static String 增强元素buff = "增加元素伤害";
	public static String 增强元素buff描述 = "增强元素伤害百分比";
	public static String 减少怪物伤害 = "改变怪物伤害";
	public static String 减少怪物伤害描述 = "改变怪物伤害百分比";
	public static String 只能元神挑战 = "此劫只能元神挑战";
	public static String 只能本尊挑战 = "此劫只能本尊挑战";
	public static String 元气 = " 元气";
	public static String 血石 = " 血石";
	public static String 血石不足 = "血石不足";
	public static String 个人战勋不满足 = "个人战勋不满足";
	public static String 您拒绝接受战队 = "您拒绝了接受该战队";
	public static String 比赛即将结束 = "比赛即将在30秒后结束";
	public static String XX拒绝接受战队 = "@STRING_1@拒绝了您的战队";
	public static String 减少怪物伤害buff描述 = "减少@STRING_1@伤害百分之@STRING_2@";
	public static String 增强风元素伤害 = "风元素伤害增加百分之@STRING_1@";
	public static String 增强火元素伤害 = "火元素伤害增加百分之@STRING_1@";
	public static String 增强雷元素伤害 = "雷元素伤害增加百分之@STRING_1@";
	public static String 增强冰元素伤害 = "冰元素伤害增加百分之@STRING_1@";
	public static String 增强风元素防御 = "风元素防御增加百分之@STRING_1@";
	public static String 增强火元素防御 = "火元素防御增加百分之@STRING_1@";
	public static String 增强雷元素防御 = "雷元素防御增加百分之@STRING_1@";
	public static String 增强冰元素防御 = "冰元素防御增加百分之@STRING_1@";
	public static String 增加所有元素伤害 = "增加一倍所有元素伤害";
	public static String 增加元素防御百分比 = "增加所有防御@STRING_1@%";
	public static String 增强物理防御 = "物理防御增加百分之@STRING_1@";
	public static String 增强法术防御 = "法术防御增加百分之@STRING_1@";
	public static String 渡劫中不能切换元神 = "您正在渡劫,不能切换元神";
	public static String 迷城中不能切换元神 = "圣兽阁中,不能切换元神";
	public static String 死亡不能切换元神 = "您已经死亡，不能切换元神";
	public static String 开启大师技能 = "开启大师技能:@STRING_1@";
	public static String 心法学习上限提高 = "所有心法学习上限提高:@STRING_1@";
	public static String 飞升确认 = "恭喜您通过千辛万苦的历练终于修得仙缘，您现在即可飞升仙界！";
	public static String 飞升成功 = "飞升成功";
	public static String 本尊等级不够 = "本尊等级不到220";
	public static String 元神等级不够 = "元神等级不足！";
	public static String 飞升需要经验 = "储存经验不满，不能飞升";
	public static String 心法美满 = "心法修炼等级不足";
	public static String 境界不够 = "境界不满足飞升条件";
	public static String 渡劫重数不够不够 = "未通过九重天劫";
	public static String 等级不够渡劫 = "本尊等级不到110";
	public static String 等级不够渡劫元神 = "元神等级不到110";
	public static String 渡过九重天劫 = "您已经成功通过了九重天劫，快赶往飞升台飞升吧！";
	public static String 渡过功能未开启 = "未到达开启时间！";
	public static String 渡劫封印 = "尚未开放此劫,敬请期待！";
	public static String 囚禁状态不可渡劫 = "囚禁状态不可渡劫！";
	public static String 渡劫开启 = "此功能110级开放！";
	public static String 渡劫胜利强制出场景剩余时间 = "退出渡劫";
	public static String 渡劫功能开启前提示 = "渡劫介绍\n1、天劫共有九重，每重都有不同的挑战.当每一重天劫激活时都会触发倒计时，当倒计时为0时会自动传送挑战者进入渡劫地图.\n2、天劫挑战模式\n生存模式、击杀模式、限时击杀、本尊挑战、元神挑战\n3、天劫奖励\n独有散仙称号、心法修炼上限、元婴丹、激活大师技能、飞升资格\n4、挑战失败\n当挑战者挑战失败后会获得虚弱状态，虚弱状态会叠加最高叠加4层，使用渡劫回魂丹可减少虚弱状态持续时间；";
	public static String 飞升后才能升级 = "需要飞升后才可以继续升级！";
	public static String 需飞升后完成赐予仙婴任务才可开启本系统 = "需飞升后完成赐予仙婴任务才可开启本系统";
	public static String 重复观礼飞升 = "您已经观看过此人飞升动画，无需再次观礼！";
	public static String 飞升后不需要再次渡劫 = "您已飞升成仙，不需要再经历天劫磨难！";
	public static String 组队中不可渡劫 = "组队中不可渡劫";
	public static String 已经飞升过了 = "您飞升过了，无需再次飞升！";
	public static String 飞升推送 = "恭喜@STRING_1@国@STRING_2@玩家飞升成功，每日前三次观礼飞升将获得额外经验，确定要观礼飞升吗？";
	public static String 降低治疗效果 = "降低治疗效果";
	public static String 棉花糖 = "棉花糖";
	public static String 情人糖 = "情人糖";
	public static String 洋气红包 = "洋气红包";
	public static String 情人节送花提示 = "@STRING_1@玩家收到@STRING_2@所赠@STRING_3@@COUNT_1@个,祝福有情人终成眷属";
	public static String 情人节送洋气红包提示 = "@STRING_1@玩家收到@STRING_2@所赠@STRING_3@@COUNT_1@个,快点求土豪送一个红包吧！";

	public static String 元素精华描述 = "抵挡@STRING_1@次伤害(除天雷)";
	// 恶魔广场相关翻译
	public static String 副本人数已满 = "副本人数已满";
	public static String 确认使用高级门票 = "副本所需门票不足，确定使用更高级的门票进入?";
	public static String 角色等级不足 = "角色等级不足";
	public static String 今天已经达到进入上限 = "今天已经达到进入副本上限，请明天再来";
	public static String 超出传送时间 = "超出传送时间";
	public static String 副本已开始 = "副本已开始";
	public static String 副本还没开始 = "副本还没开始，不允许进入!";
	public static String 副本将在XX天开启 = "副本尚未解封，解封时间最早为@STRING_1@年@STRING_2@月@STRING_3@日!";
	public static String 没有副本门票 = "没有门票，不可进入副本";
	public static String 怪物开启倒计时 = "怪物开启倒计时";
	public static String 下波怪物倒计时 = "下波怪物倒计时";
	public static String 天降宝箱倒计时 = "天降宝箱倒计时";
	public static String 清除怪物倒计时 = "清除怪物倒计时";
	public static String boss刷新倒计时 = "boss刷新倒计时";
	public static String 副本结束倒计时 = "副本结束倒计时";
	public static String 副本整体倒计时 = "副本结束时间";
	public static String XX怪物刷新了 = "@STRING_1@怪物出现了，大家速击杀怪物！";
	public static String XX宝箱刷新了 = "@STRING_1@天降于此地，大家速去寻找！";
	public static String 副本门票不足 = "副本门票不足";
	public static String 乾坤异宝 = "无相宝箱";
	public static String 阴阳异宝 = "太初宝箱";
	public static String 混元异宝 = "混沌宝箱";
	public static String 混沌异宝 = "无极宝箱";
	public static String 后天乾坤令 = "无相禁地令";
	public static String 先天阴阳令 = "太初禁地令";
	public static String 太极混元令 = "混沌禁地令";
	public static String 无极混沌令 = "无极禁地令";
	public static String 增加法宝自身属性 = "法宝自身属性";
	public static String 恶魔广场传送门已经出现 = "天魔禁地传送门出现在灭魔神域，请各位仙友速去查看!";
	public static String 恶魔广场传送门即将出现 = "天魔禁地传送门即将出现在灭魔神域，请各位仙友速去查看!";
	public static String 恶魔广不允许使用飞行坐骑 = "副本中不允许使用飞行坐骑!";
	public static String 恶魔广场中不允许使用此道具 = "恶魔广场中不允许使用此道具!";
	public static String 副本中不能囚禁 = "恶魔广场副本中不能囚禁!";
	public static String 恶魔广场副本介绍 = "恶魔广场副本介绍!";
	public static String 恶魔广场副本中不允许原地复活 = "您副本中原地复活次数已用尽，不可原地复活!";
	public static String 恶魔广场原地复活剩余次数 = "您副本可原地复活剩余次数:@STRING_1@";
	public static String 达到XX后开启 = "达到@STRING_1@后开启";
	public static String 法宝突破界面描述 = "放入法宝与进阶道具点击进阶，可提升法宝的最大品阶等级\n当紫、橙、金色法宝当前吞噬品阶等级达到最大时，可使用进阶道具进行法宝进阶，进阶失败后进阶道具会消失";

	public static String 渡劫中不允许使用此道具 = "渡劫中不允许使用此道具!";
	public static String 渡劫不允许组队 = "被邀请者正在渡劫中，不能邀请组队!";
	public static String 渡劫不允许组队2 = "队长正在渡劫，不能申请进入队伍!";
	public static String 队友正在战场 = "队友正在战场，不能申请进入队伍";

	// 合成相关翻译
	public static String 元神不符 = "元神类型不符！";
	public static String 合成类型不对 = "合成类型不对";
	public static String 合成所需材料不存在 = "合成所需材料不存在";
	public static String 请放入合成所需材料 = "请放入合成所需材料";
	public static String 合成花费类型不对 = "合成花费类型不对";
	public static String 服务器数据异常 = "服务器数据异常";
	public static String 放入材料不对 = "放入材料不对";
	public static String 放入材料数量不足 = "放入材料数量不足";
	public static String 背包材料数量不足 = "背包材料数量不足";
	public static String 不可合成 = "不可合成";
	public static String 合成失败 = "合成失败";
	public static String 合成获得物品 = "合成获得物品";
	public static String 恶魔广场积分 = "恶魔城堡杀怪积分奖励";
	public static String 合成后绑定 = "由于您使用了绑定材料，合成后物品将绑定，确认合成吗?";
	public static String 空岛提前退出 = "系统";
	public static String 空岛提前退出提示 = "由于您提前退出，只获得了基础经验奖励%s";
	public static String 空岛经验奖励 = "您在空岛大冒险中获得经验%s";

	public static String 灵气 = "灵气";
	// 仙尊相关
	public static String 需要飞升才可以挑战 = "需要飞升才可以挑战";
	public static String 组队状态不可挑战仙尊 = "组队状态不可挑战仙尊 !";
	public static String 挑战仙尊不允许切换元神 = "您正在挑战仙尊,不能切换元神";
	public static String 挑战仙尊限制功能 = "您正在挑战仙尊,不能使用此功能";
	public static String 对方正在挑战仙尊 = "对方正在挑战仙尊，无法传送。";
	public static String 对方正在献血试炼 = "对方正在鲜血试炼，无法传送。";
	public static String 被邀请者在挑战仙尊副本 = "您邀请的人正在挑战仙尊，暂时不能接受组队!";
	public static String 队长正在挑战仙尊 = "队长正在挑战仙尊，暂时无法接受组队申请!";
	public static String 挑战仙尊限制回城 = "您正在挑战仙尊,不能使用此类道具";
	public static String 限制地图囚禁 = "对方在限制地图内，不允许囚禁";
	public static String 挑战仙尊不允许使用飞行坐骑 = "您正在挑战仙尊，不能使用坐骑!";
	public static String 挑战时间已过 = "周日副本不开启：今日仙尊挑战不开放，请明天再来！";
	public static String 地图名为空 = "地图名为空,请联系GM！";
	public static String 竞选者已达上限 = "竞选者已达上限,下周您请赶早!";
	public static String 已挑战过 = "挑战成功，快去本职业仙尊处设置宣言吧（点击查看本期投票榜-设置宣言）";
	public static String 已挑战过2 = "您本尊或元神已通过挑战，本期内无法再次挑战";
	public static String 挑战者已达上限 = "挑战者已达上限,请您稍后再试!";
	public static String 挑战仙尊不可原地复活 = "挑战仙尊不可以使用原地复活!";
	public static String 副本中不可原地复活 = "副本中不可以使用原地复活!";
	public static String 挑战失败 = "很遗憾~挑战失败!";
	public static String 单人不能参加 = "个人无法参赛，请先邀请战队成员加入队伍";
	public static String 仙尊挑战进入提示 = "仙尊挑战不可使用坐骑和宠物!";

	public static String 限制地图不允许使用此道具 = "您正在限制地图，不允许使用此类道具。";
	public static String 限制地图不允许使用此功能 = "您正在限制地图，不允许使用此功能。";
	// 目标相关
	public static String 目标未达成 = "您还没有达成此目标!";
	public static String 恭喜达成目标 = "恭喜@STRING_1@达成了目标:@STRING_2@";
	public static String 恭喜达成目标2 = "恭喜@STRING_1@达成了目标:";
	public static String 章节积分不足 = "您的章节积分不足!";
	public static String 已领取奖励 = "您已经领取过此奖励了!";
	public static String 目标系统奖励邮件 = "仙录系统奖励物品";
	public static String 神魂大乱斗奖励物品 = "神魂大乱斗开启神魂获得物品";
	public static String 没有可领取的奖励 = "没有可领取的奖励";
	// 新坐骑
	public static String 自动学习坐骑技能 = "坐骑学会了<f color='%s'>%s</f>技能!";
	public static String 开启新技能格 = "开启第@COUNT_1@个技能格!";
	public static String 血脉不足 = "血脉不足!";
	public static String 飞行坐骑不可进行此操作 = "飞行坐骑不可进行此操作";
	public static String 颜色太低 = "您需要将坐骑品质提升后才可以开启血脉修炼上限!";
	public static String 已升至最大 = "已经达到最大星级!";
	public static String 继续升阶需要 = "已经达到最大星级，您可以使用@STRING_1@继续升级";
	public static String 物品不足 = "您没有足够的升级材料!";
	public static String 颜色已满 = "您的坐骑已经达到最高颜色品质了，不需要再次升级!";
	public static String 技能刷新免费次数用完 = "您的免费次数已经用尽，请使用物品刷新!";
	public static String 没有临时技能 = "您还没有可替换的技能，请刷新获取可替换的技能!";
	public static String 替换技能失败 = "替换技能失败!";
	public static String 该技能格还未开启 = "该技能格还未开启,升级坐骑阶级可开启更多技能格!";
	public static String 该坐骑已经有此技能 = "该坐骑已经有此技能,不能再次学习!";
	public static String 此技能已经达到最高级 = "此技能已经达到最高级,无需再次升级!";
	public static String 坐骑转换 = "由于坐骑系统优化，高阶坐骑被分解为培养道具常青草";
	public static String 坐骑碎片 = "常青草";
	public static String 刷新技能物品不足 = "没有刷新技能可使用的道具!";
	public static String 当前坐骑没有此技能 = "当前坐骑没有此技能，不可升级!";
	public static String 免费次数已用尽 = "免费次数已用尽,请明天再来!";
	public static String 高阶坐骑自动转换 = "坐骑系统改版，您的高阶坐骑将自动转换为碎片，用作坐骑培养!";
	public static String 果实偷取邮件title = "仙府偷取奖励";
	public static String 果实偷取邮件内容 = "恭喜您获得了一颗次品玉";
	public static String 获得额外物品 = "恭喜您获得额外奖励物品，请查收邮件!";
	public static String 偷果实buff = "@STRING_1@摘取仙府农作物可获取@STRING_2@";
	public static String 偷果实剩余次数 = "剩余可摘取次数:%d";
	public static String 坐骑培养扣除银子 = "扣除银子：%d两";
	// 副本转盘
	public static String 副本转盘 = "副本转盘!";
	public static String 可参与次数不足 = "可参与次数不足!";
	public static String 确认花钱转盘 = "此次抽奖会花费%d两银子，确定抽奖吗？";

	public static String 金猴天灾物品邮件标题 = "空岛大冒险奖励";
	public static String 金猴天灾物品邮件内容 = "空岛大冒险奖励";

	// 挖坟活动
	public static String 挖坟活动 = "星域寻宝活动";
	public static String 确认兑换铲子 = "检测到您身上有<f color='0xF3F349'>@COUNT_1@</f>个<f color='0xF3F349'>@STRING_1@</f>和<f color='0xF3F349'>@COUNT_2@</f>个<f color='0xF3F349'>@STRING_2@</f>，确定全部兑换?";
	public static String 请先兑换铲子 = "请先兑换铲子";
	public static String 使用银铲子 = "使用%s";
	public static String 使用金铲子 = "使用%s";
	public static String 使用哪种铲子 = "使用哪种铲子";
	public static String 活动未开启 = "活动未开启";
	public static String 本章活动未开启 = "本章寻宝活动未开启";
	public static String 等级不够无法打开界面 = "很抱歉，等级小于%s级的角色无法参与此活动。";
	public static String 已经挖过此坟坑 = "这个位置已经挖取过，挖其他地方去吧!";
	public static String 前一个坟墓没挖完 = "需要挖完前一章才可以挖取此位置!";
	public static String 铲子不足 = "铲子不足";
	public static String 十连挖需要金铲子数量 = ",十连挖一次需要<f color='0xF3F349'>@COUNT_1@</f>个<f color='0xF3F349'>@STRING_1@</f>";
	public static String 铲子类型错误 = "铲子类型错误";
	public static String 坟地索引不对 = "坟地索引不对";
	public static String 需要消耗琉璃铲子 = "需要消耗1个琉璃铲子,确定使用?";
	public static String 全服共有坟墓不可十连挖 = "全服寻宝不可十连挖";
	public static String 背包中没有物品 = "您没有%s";
	// public static String 背包中没有物品 = "您没有%s或%s";
	public static String 活动尚未开启 = "活动尚未开启";
	public static String 剩余格子不足10个 = "剩余格子不足10个";
	public static String 挖坟必产物品标题 = "%s必产物品";
	public static String 挖坟必产物品描述 = "挖完此章必定产出以下物品";
	public static String 挖坟帮助描述 = "挖坟帮助描述";
	public static String 十连挖成功 = "十连挖成功";
	public static String 十连挖二次确认 = "十连挖需要一次性消耗10倍的%s数量，是否继续？";
	public static String 挖坟世界公告 = "恭喜<f color='0xF3F349'>%s</f>玩家从<f color='%s'>%s</f>地图寻宝中获得<f color='%s'>%s</f>物品";
	public static String 本章已挖完 = "本章宝物已经挖完了，赶快前往下一章继续寻宝吧!";
	public static String 银铲子 = "银铲子";
	public static String 金铲子 = "金铲子";
	public static String 琉璃铲子 = "琉璃铲子";
	public static String 白银铲子 = "白银铲子";
	public static String 黄金铲子 = "黄金铲子";
	public static String 彩虹琉璃铲 = "彩虹琉璃铲";
	// 新称号
	public static String 称号到期自动删除 = "您的称号%s已经到期，系统自动删除!";
	public static String 称号已过期 = "您的称号已过期，无法设置为默认称号!";

	public static String 偷果实buff二次确认 = "您身上已经有鸿运效果，是否替换？";

	// 仙录新增
	public static String 家族建设任务 = "家族任务";
	public static String[] 家族任务2 = new String[] { "妖兽精血", "灵芝仙草", "赤萤魔矿" };
	public static String[] 称号成就1 = new String[] { "江湖小虾", "乾坤真仙", "元神尊者", "静心炼气师", "玄都法师", "宠行天下", "初涉江湖", "月华真人", "甲子真人", "酒神仪狄", "酒圣杜康", "博学者", "谍战专家", "百变神偷", "王牌镖师", "杀破狼", "封妖师", "镇魔人", "辣手杀神" };
	public static String[] 称号成就2 = new String[] { "一劫散仙", "二劫散仙", "三劫散仙", "四劫散仙", "五劫散仙", "六劫散仙", "七劫散仙", "不再相信爱情了", "飘渺寻仙曲酒鬼", "怪物猎人", "宠物孵化大师", "宠物炼妖大师", "大喜", "大悲", "青龙使者", "白虎使者", "朱雀使者", "玄武使者", "麒麟使者", "杀戮", "酒灵", "帖仙", "助人为乐" };
	public static String[] 称号成就3 = new String[] { "八劫散仙", "九劫散仙", "富可敌国", "鲁班大师", "万圣先师", "南斗星君", "木德星君", "无极真人", "善财真人", "三界斗神", "无极星君", "斗战胜佛", "金蛇圣皇", "羽翼仙人", "连斩传说", "星级战骑" };
	public static String[] 精英怪 = new String[] { "迷踪魔狼精英", "百鬼王精英", "镇塔将军精英", "陌路鬼(精英)", "古灵精怪(精英)", "执念障(精英)", "小钻风(精英)", "总钻风(精英)", "奔波霸(精英)", "霸波奔(精英)", "有来无去(精英)", "精细鬼(精英)", "伶俐虫(精英)", "巴山虎(精英)", "混元魔王(精英)", "狂禅魔尊(精英)", "熊山君(精英)", "寅将军(精英)", "特处士(精英)", "金顶巨妖(精英)", "独角鬼王(精英)", "噬魂死狂(精英)", "冤魂之主(精英)", "恶灵之尊(精英)" };
	public static String[] 境界怪 = new String[] { "九转元胎", "幻景心魔", "惑心鬼蜮", "乾坤魔童", "沧浪魅惑妖", "阴阳界灵", "碧水凌波鬼", "飞升险道魔", "极恶万鬼王", "暴戾刑天", "吞天噬地魔" };
	public static String[] 世界boss = new String[] { "七情蜘蛛魔", "无间平等王" };
	public static String[] 世界boss2 = new String[] { "九界佛皇", "红云老祖" };
	public static String[] 小试牛刀怪物 = new String[] { "黑风寨主", "槐木饲鬼", "白骨", "赤狼统领", "余紫剑", "金刚伏魔", "云岭巨人", "神魔之躯", "影之刺客", "幻海蜃王", "食骨巫王" };
	public static String[] 极限任务1 = new String[] { "风雪之巅(极)", "问天灵台(极)", "太华神山(极)", "太华之顶(极)", "化外之境(极)", "云波鬼岭(极)", "戮神墓地(极)", "地狱魔渊(极)", "鬼都(极)", "燃烧圣殿(极)", "炼狱绝地(极)", "东海之滨(极)", "东海龙宫(极)", "幽海秘境(极)" };
	public static String[] 极限任务2 = new String[] { "云梦泽(极)", "风陵渡(极)", "沧浪水(极)", "回声谷 (极)", "博浪沙(极)", "玉龙瀑(极)", "飞升崖(极)", "封神台(极)", "绝尘岭(极)" };
	public static String[] 极限任务4 = new String[] { "万花仙谷(极)", "风鬼冥地(极)", "火皇焚境(极)", "雷斗幻域(极)", "冰灵极岛(极)" };
	public static String[] 极限任务5 = new String[] { "福地洞天九层(极)", "福地洞天一层(极)", "福地洞天二层(极)", "福地洞天三层(极)", "福地洞天四层(极)", "福地洞天五层(极)", "福地洞天六层(极)", "福地洞天七层(极)", "福地洞天八层(极)" };
	public static String[] 极限任务6 = new String[] { "无极冰原八层(极)", "无极冰原一层(极)", "无极冰原二层(极)", "无极冰原三层(极)", "无极冰原四层(极)", "无极冰原五层(极)", "无极冰原六层(极)", "无极冰原七层(极)" };
	public static String[] 极限任务3 = new String[] { "天虹云河(极)", "落星玉台(极)", "般若宝刹(极)" };

	// 仙界技能
	public static String[] 免疫状态buff = new String[] { "减速", "定身", "晕眩" };
	public static String[] 免疫buff描述 = new String[] { "10秒内免疫减速", "10秒内免疫定身", "10秒内免疫晕眩", "免疫控制技能" };
	public static String 仙界被动技能描述 = "\n被玩家攻击时有%s几率触发免疫%s效果，免疫效果持续10秒，免疫效果触发最小间隔20秒";
	public static String 下一级概率 = "触发概率为:%s";
	public static String 被动技能 = "被动技能，瞬发\n";
	public static String[] 增加额外属性攻 = new String[] { "增加一定风属性攻及风属性减抗", "增加一定火属性攻及火属性减抗", "增加一定雷属性攻及雷属性减抗", "增加一定冰属性攻及冰属性减抗" };
	// 家族修改
	public static String 龙图阁等级不足 = "龙图阁等级不足，无法炼丹";
	public static String 金蛇圣皇 = "金蛇圣皇";
	public static String 没有家族或宗派 = "您还没有加入家族或宗派，无法使用。";
	public static String 家族喝酒经验加成 = "家族喝酒获得经验增加%s";
	public static String[] 家族酒称号 = new String[] { "", "家族喝酒1", "家族喝酒2", "家族喝酒3" };
	public static String 家族建筑等级不足 = "%s建筑等级不足,请提升建筑等级。";
	public static String[] 武器防具坊 = new String[] { "防具坊", "武器坊" };
	public static String 已达到最高等级 = "已经达到最高等级";
	public static String 修炼值不足 = "修炼值不足";
	public static String 家族等级太低不能强化镖车 = "家族等级太低不能强化镖车";
	public static String 家族镖车不存在 = "家族镖车不存在";
	public static String 镖车强化已达上限 = "镖车强化已达上限";
	public static String 镖车血量描述 = "增加镖车%s气血";
	public static String 镖车双防描述 = "增加镖车%s减伤";
	public static String 家族资金不足邮件title = "家族降级";
	public static String 家族维护邮件title = "家族维护费用";
	public static String 家族封印邮件title = "家族封印";
	public static String 家族封印邮件内容 = "亲爱的家族官员，您的家族已经被封印，如想开启家族封印，需补齐今日的家族维护费用，否则无法进行正常的家族活动！";
	public static String 家族维护邮件内容 = "亲爱的家族官员，本次家族维护费用为%s，次日0时将扣除转天的家族维护费用，请官员注意！";
	public static String 家族资金不足邮件内容 = "亲爱的家族官员，今日的家族资金不够次日的家族维护费用，次日0时如资金不够维护时，家族功能将被封印，封印后家族内只能进行饲养任务，其他功能活动将被封印，请官员注意！";
	public static String 家族降级邮件内容 = "亲爱的家族官员，您的家族由于剩余资源无法满足家族维护费用，家族主建筑聚仙阁被降级，如其他建筑等级超过主建筑等级，系统自动将其他建筑降为和主建筑等级相同，降级返还的资金和灵脉值将会已80%返还给家族内，请注意查看！";
	public static String 灵矿战开始 = "灵矿战开始,是否参加?";
	public static String 确认解封家族 = "解封家族需要消耗家族资金%s，是否确定？";
	public static String 增加修炼值 = "增加修炼值:%s\n增加家族资金:%s";
	public static String 当日祈福次数已达最大 = "今天上香次数已经用尽，请明天再来。";
	public static String 家族未被封印不需要解封 = "家族未被封印不需要解封";
	public static String 家族探索 = "家族探索";
	public static String 家族建筑降级确认 = "亲爱的家族官员，您是否确定要将【%s】进行降级操作吗？（建筑降级将不返还升级资源）";
	public static String 发布家族buff确认 = "亲爱的家族官员，您是否确定消耗【%s】进行发布家族BUFF的操作吗？";
	public static String 剩余祈福次数 = "今日上香次数剩余%s次。";
	public static String 任务不在列表内 = "任务不在列表内！";
	public static String 当前加成 = "当前加成：";
	public static String 收集宝石 = "收集宝石";
	public static String 击杀镖车提示 = "<f color='0xff8400'>【%s】</f>的<f color='0x00ff00'>【%s】</f>家族<f color='0xffff00'>【%s】</f>砸碎了<f color='0xff8400'>【%s】</f><f color='0x00ff00'>【%s】</f>家族的镖车。";
	public static String 杀死镖车获得奖励 = "您得到了%d家族贡献和%d点家族修炼值！";
	public static String 发布饲养任务 = "发布饲养任务(%d/%d)";
	public static String 家族封印提示 = "1、如家族资金不足维护费用时，家族主建筑降级1级\n2、如其他建筑的等级＞家族主建筑等级，其他建筑将会将至家族主建筑的等级\n3、降级返还的资金和灵脉值将会已80%返还给家族\n4、当家族降级到无法扣除维护费用时，家族会自动解散";
	public static String 解封家族发送消息 = "家族被【%s】解除封印，家族功能恢复正常！";
	public static String 家族资金 = "家族资金";
	public static String 灵脉值 = "灵脉值";
	public static String 家族人数 = "家族人数";
	public static String 获得修炼值 = "增加修炼值%d";
	public static String 获得家族资金 = "增加家族资金%s";
	public static String 家族资金不足解封 = "您当前家族资金不足无法解封您的家族！";
	public static String 家族资金超上限 = "家族资金超过上限，无法继续捐献";
	public static String 家族资金已超出上限 = "您当前<f color='0xff0000'>家族资金/灵脉值已超过上限</f>，无法继续增长.";
	public static String 资源超上限发布任务等确认提示 = "您当前<f color='0xff0000'>家族资金/灵脉值已超过上限</f>，无法继续增长，您是否确认要进行此操作！";
	public static String 家族维护标题 = "家族维护";
	public static String 家族维护通知 = "家族扣除了%s的维护费。 ";
	public static String 家族降级标题 = "家族维护,家族资金不足降级";
	public static String 家族降级通知 = "家族资金不足";
	public static String 家族封印等级 = "家族等级已达到封印等级，无法继续升级。";
	public static String 本尊与元神都可获得属性加成 = "本尊与元神都可获得属性加成";
	public static String 剩余可使用绑银 = "<f color='0xfff600'>今日剩余可使用绑银：%s</f>";
	public static String 家族没有驻地 = "亲爱的家族成员，家族内暂无家族驻地，无法进行捐献！";

	public static String 领取血石补偿 = "获得血石%d";

	public static String 今天通过boss获得经验上限 = "您今日通过地灵天书boss怪的经验次数已达上限，无法继续获得经验。";
	public static String 商店不可刷新 = "此商店物品不需要刷新";
	// 翅膀副本
	public static String 翅膀副本 = "守护之城副本";
	public static String 翅膀副本掉落邮件title = "副本奖励";
	public static String 翅膀副本掉落邮件内容 = "副本内容";
	public static String 正在副本中提示 = "副本中不能拉人!";
	public static String 对方正在副本中 = "对方正在副本中不能拉人!";
	public static String 正在副本中提示2 = "副本中，不可执行此操作！";
	public static String 队员进入副本确认 = "是否同意进入翅膀副本";
	public static String 副本中不可使用回城 = "副本中不可使用回城";
	public static String 当日副本次数已用尽 = "当日副本次数已用尽！";
	public static String XX副本次数已用尽 = "%s副本次数已用尽，无法进入副本!";
	public static String 确定单人进入副本 = "此副本难度较大，确定单人进入？您今天还可进入副本%s次！";
	public static String 可进入次数 = "剩余次数%s";
	public static String 队伍中有玩家正在蹲监狱 = "队伍中又玩家在监狱中，无法开启副本！";
	public static String 传出地图名 = "miemoshenyu";
	public static String XX使用技能XX = "%s使用了%s";
	public static String npc无敌 = "无敌";
	public static String npc回血 = "回血";
	public static String 第XX波怪 = "第%d波怪出现倒计时";
	public static String 副本中不允许切换PK模式 = "副本中不允许切换PK模式";
	public static String 副本中不允许解散队伍 = "副本中不允许解散队伍";
	public static String 副本中不允许离开队伍 = "副本中不允许离开队伍";
	public static String 翅膀副本奖励 = "翅膀副本奖励";
	public static String 恭喜您低于第X波 = "恭喜您成功抵御第%d波怪物.";
	public static String npc无敌剩余XX = "无敌,剩余%d秒";
	public static String 没有家族不能鼓舞 = "家族不存在，不能鼓舞";

	// 屏蔽信息
	public static String 该玩家已经屏蔽交易 = "该玩家已经屏蔽交易";
	public static String 该玩家已经屏蔽交换 = "该玩家已经屏蔽交换";
	public static String 该玩家已经屏蔽繁殖 = "该玩家已经屏蔽繁殖";
	public static String 该玩家已经屏蔽组队 = "申请的队伍队长或该玩家已经屏蔽组队！";

	// 新技能
	public static String 增加闪避比例 = "增加闪避%s";
	public static String 增加免暴比例 = "增加免暴%s";
	public static String 增加物攻比例 = "增加物攻%s";
	public static String 增加法攻比例 = "增加法攻%s";
	public static String 增加破甲比例 = "增加破甲%s";
	public static String 增加暴击比例 = "增加暴击%s";
	public static String 增加命中比例 = "增加命中%s";
	public static String 增加精准比例 = "增加精准%s";
	public static String 吸收伤害并转换成生命值 = "吸收并转换%s点伤害为自身生命值";
	public static String XX状态不能使用技能 = "当前状态下无法使用此技能";
	public static String 技能cd中 = "此技能正在冷却中";
	public static String 千层定身 = "千层定身";
	public static String 只有兽魁可以使用 = "此物品只有兽魁可以使用";
	public static String 需要进阶后使用 = "进阶后才可以使用此道具";
	// 附魔相关
	public static String[] 请输入新的名字 = new String[] { "请输入家族名字", "请输入宗派名字" };
	public static String 附魔 = "附魔";
	public static String 确认替换原有附魔 = "确认替换原有附魔？";
	public static String 锁定附魔消耗 = "锁定附魔需要消耗%s绑银(绑银不足自动消耗银子),是否确定锁定?";
	public static String 减少百分比受到伤害 = "减少%s%%受到的伤害";
	public static String 附魔材料不存在 = "附魔材料不存在-%s";
	public static String 不是附魔道具 = "放入材料不是附魔道具!";
	public static String 附魔装备不在背包中 = "附魔装备不在背包中！";
	public static String 只有装备可以附魔 = "只有装备可以附魔";
	public static String 装备等级太低 = "装备等级太低， 不能附魔";
	public static String 此类装备未开放附魔功能 = "此类装备未开放附魔功能";
	public static String 此装备不能使用这种附魔 = "装备不能使用此种附魔";
	public static String 灵性 = "灵性";
	public static String 附魔效果 = "附魔效果:";
	public static String 等级不足不开放 = "此功能110级以后开放";
	public static String 附魔成功 = "附魔成功！";
	public static String 需要装备等级 = "需要装备等级:%s";
	public static String 锁定附魔 = "锁定附魔";
	public static String 附魔已锁定 = "附魔已锁定";
	public static String 锁定成功 = "锁定附魔成功";
	public static String 解锁附魔成功 = "解锁附魔成功";
	public static String 消耗银子X = "消耗银子%s";
	public static String 附魔消失 = "附魔消失提示";
	public static String 附魔消失邮件标题 = "%s装备上的附魔已消耗完";
	public static String 低于10点通知 = "您的%s装备上的附魔灵性已经不足10点。";
	public static String 免疫控制 = "附魔免疫控制";
	public static String 免疫控制描述 = "附魔免疫控制描述";
	public static String 正在锁定 = "正在锁定";
	// 猎命
	public static String 没有可以替换的兽魂 = "没有可以替换的兽魂！";
	public static String 经验 = "经验";
	public static String 兽魂属性 = "兽魂属性";
	public static String 兽魂泡泡 = "兽魂可通过灭魔神域【帝君】处进行兽魂镶嵌";
	public static String 摘取失败 = "摘取失败";
	public static String 摘取成功 = "摘取成功";
	public static String 物品不在兽魂仓库 = "物品不在兽魂仓库中";
	public static String 等级不足 = "等级不足";
	public static String 主兽魂不在装备上 = "主兽魂不在装备上";
	public static String 兽魂已到满级 = "兽魂已到满级，无法继续吞噬";
	public static String 猎命抽奖确认 = "购买灵气石需要消耗%s银子，是否确定？(此提示每次登陆只有一次)";
	public static String 脱饰品确认 = "你确定要让【%s】携带【%s】吗？<f color='0xff0000'>（携带之后不能主动卸下，只能替换，被替换的道具也会消失哦）</f>";
	public static String 脱饰品确认2 = "你确定使用【%s】替换【%s】吗？<f color='0xff0000'>（替换后【%s】将会被替换消失掉哦！）</f>";
	public static String 只能吞噬同类型兽魂 = "只能吞噬同类型兽魂";
	public static String 满级兽魂不能被吞噬 = "满级兽魂不能被吞噬";
	public static String 兽魂抽奖邮件内容 = "背包不足邮件发送";
	public static String 兽魂仓库已满 = "兽魂仓库剩余空间不足";
	public static String[] 对应增加属性描述 = new String[] { "血量", "攻击", "物防", "法防", "暴击", "命中", "闪避", "破甲", "精准", "免暴" };
	public static String 抽奖世界公告 = "恭喜<f color='0xFFFF00'>%s</f>抽取到<f color='0xE6028D'>%s</f>";
	public static String[] 单抽描述 = new String[] { "获得灵气石(2000)", "<f color='0xFFFF00' size='28'>500两购买一次可获得额外道具(几率获得兽魂)</f>" };
	public static String[] 十连抽描述 = new String[] { "获得灵气石(20000)", "紫色道具", "<f color='0xFFFF00' size='28'>4888两购买十次可获得10个额外道具(大几率获得兽魂)</f>" };
	// 宝箱争夺
	public static String 开始世界提示 = "距离神魂大乱斗开启还有%s";
	public static String 宝箱乱斗中不能使用飞行坐骑 = "神魂大乱斗中不能使用飞行坐骑";
	public static String 拥有过多此类神魂 = "拥有过多此类神魂!";
	public static String 未到进入时间 = "未到进入时间！";
	public static String 进入碰撞区神魂掉落 = "进入碰撞区神魂掉落！";
	public static String 获得XX物品 = "获得%s";
	public static String 所有神魂被开完提示 = "所有<f color='0xff2cda'>神魂</f>都被<f color='0x31ff2c'>获取</f>，没有<f color='0x31ff2c'>获取</f>到<f color='0xff2cda'>神魂</f>的勇士下次再来吧！60秒后地图内会出现灵霄传送npc，勇士可通过灵霄传送回到灵霄天城！";
	public static String 刷新神魂提示1 = "【<f color='0x31ff2c'>仙者神魂</f>已出现尽快抢夺，只有10个哦！】";
	public static String 刷新神魂提示2 = "【<f color='0x2c76ff'>仙君神魂</f>已出现尽快抢夺，只有3个哦！】";
	public static String 刷新神魂提示3 = "【<f color='0xff2cda'>仙尊神魂</f>已出现尽快抢夺，只有1个哦！】";
	// 仙界渡劫
	public static String 渡劫中不可使用坐骑 = "渡劫中不能乘骑坐骑。";
	public static String 空岛大冒险中不可使用坐骑 = "空岛大冒险中不可使用坐骑。";
	public static String 渡劫中不可传送 = "渡劫中不能使用此功能。";
	public static String 金猴天灾不可用 = "特殊场景内不允许使用此功能。";
	public static String 渡劫中不可升级 = "渡劫中不可升级。";
	public static String 离开倒计时 = "离开倒计时";
	public static String 下个boss刷新倒计时 = "boss刷新倒计时";
	public static String 距离进入点太远 = "距离进入点太远";
	public static String 需要完成任务XX = "需要完成%s任务才可进入";
	public static String 需要灵气值满 = "灵气值不足，不可挑战";
	public static String 境界不满足 = "境界不满足，不可挑战";
	public static String 没有飞升不可传送 = "仙界boss需要飞升后才可使用传送功能。";
	public static String 邀请者进入限制地图 = "传送失败，邀请者已进入限制地图";

	public static String 未飞升不可出战飞升宠物 = "人物未飞升，不可出战飞升宠物！";

	public static String 等级不足不能参加城战 = "等级不足，不能参加城战！";

	// 所有区域名
	/**
	 * 根据传入的字符串数组组成这些key和value的Map
	 * 传进一段字符串s，根据map的key对字符串里的字符进行匹配，匹配上后就用map的value进行替换，返回替换后的字符串
	 * @param s
	 * @param keyValues
	 *            String keyValues[][] = new String[][]{{"PLAYER1","米佳"},{"LEVEL1","41"},{"ARTICLE1","馒头"}...}
	 * @return
	 * @throws Exception
	 */
	public static String translateString(String s, String[][] keyValues) {
		// long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			if (keyValues != null && keyValues.length > 0) {
				for (int i = 0; i < keyValues.length; i++) {
					if (keyValues[i][1] == null) keyValues[i][1] = "??";
					map.put(keyValues[i][0], keyValues[i][1]);
				}
			}
			StringBuffer sb = new StringBuffer();
			char cs[] = s.toCharArray();
			char dota = '@';
			int firstDota = -1;
			for (int i = 0; i < cs.length; i++) {
				if (firstDota < 0) {
					if (cs[i] == dota) {
						firstDota = i;
					} else {
						sb.append(cs[i]);
					}
				} else {
					if (cs[i] == dota) {
						String key = new String(cs, firstDota, i - firstDota + 1);
						String value = (String) map.get(key);
						if (value != null) {
							sb.append(value);
							firstDota = -1;
						} else {
							sb.append(key);
							firstDota = i;
						}
					}
				}
			}
			if (firstDota >= 0 && firstDota < cs.length) {
				sb.append(new String(cs, firstDota, cs.length - firstDota));
			}

			return sb.toString();
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < keyValues.length; i++) {
				sb.append("{" + StringUtil.arrayToString(keyValues[i], ",") + "}");
			}
			// Game.logger.error("Translate转换短语发生异常[" + s + "] [" + sb.toString() + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) + "]", e);
			return "";
		}
	}

	/**
	 * use another translate method instead
	 * @param s
	 * @param keys
	 * @param values
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static String translateString(String s, String[] keys, String values[]) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		if (keys != null && values != null) {
			for (int i = 0; i < keys.length; i++) {
				map.put(keys[i], values[i]);
			}
		}
		StringBuffer sb = new StringBuffer();
		char cs[] = s.toCharArray();
		char dota = '@';
		int firstDota = -1;
		for (int i = 0; i < cs.length; i++) {
			if (firstDota < 0) {
				if (cs[i] == dota) {
					firstDota = i;
				} else {
					sb.append(cs[i]);
				}
			} else {
				if (cs[i] == dota) {
					String key = new String(cs, firstDota, i - firstDota + 1);
					String value = (String) map.get(key);
					if (value != null) {
						sb.append(value);
						firstDota = -1;
					} else {
						sb.append(key);
						firstDota = i;
					}
				}
			}
		}
		if (firstDota >= 0 && firstDota < cs.length) {
			sb.append(new String(cs, firstDota, cs.length - firstDota));
		}

		return sb.toString();
	}

	public static String text_1 = "宝石";
	public static String text_2 = "玄晶";
	public static String text_3 = "金刚石";
	public static String text_4 = "装备";
	public static String text_5 = "简单道具";
	public static String text_6 = "buff道具";
	public static String text_7 = "任务道具";
	public static String text_9 = "武器";
	public static String text_10 = "头盔";
	public static String text_11 = "护肩";
	public static String text_12 = "衣服";
	public static String text_14 = "鞋子";
	public static String text_15 = "首饰";
	public static String text_16 = "项链";
	public static String text_17 = "戒指";
	public static String text_18 = "战斗中你不能那样做";
	public static String text_19 = "您的等级不够！";
	public static String text_20 = "物品正在冷却.";
	public static String text_21 = "物品已经用完.";
	public static String text_22 = "引导中不能使用道具.";
	public static String text_23 = "综";
	public static String text_24 = "世";
	public static String text_25 = "私";
	public static String text_26 = "组";
	public static String text_27 = "帮";
	public static String text_28 = "阵";
	public static String text_29 = "系";
	public static String text_30 = "系统";
	public static String text_31 = "无法继续跟随了！";
	public static String text_32 = "仙法不够,";
	public static String text_33 = "距离太远,";
	public static String text_34 = "目标已死亡,";
	public static String text_35 = "武器不匹配,";
	public static String text_36 = "目标不能攻击";
	public static String text_37 = "目标不可见";
	public static String text_38 = "未知原因";
	public static String text_39 = "技能不存在";
	public static String text_40 = "您处于眩晕状态，不能施法";
	public static String text_41 = "飞行状态下不能使用技能";
	public static String text_42 = "封魔状态下不能使用技能";
	public static String text_43 = "眩晕状态下不能使用此技能";
	public static String text_44 = "不可以对选中的目标施放技能";
	public static String text_45 = "主动";
	public static String text_46 = "被动";
	public static String text_47 = "光环";
	public static String text_48 = "分钟";
	public static String text_49 = "秒";
	public static String text_50 = "打断跟随！";
	public static String text_51 = "任务失败";
	public static String text_52 = "任务完成";
	public static String text_53 = "任务已接受";
	public static String text_54 = "任务已完成";
	public static String text_55 = "任务已放弃";
	public static String text_56 = "[日]";
	public static String text_57 = "[重]";
	public static String text_58 = "你对";
	public static String text_59 = "说:";
	public static String text_60 = "对你说:";
	public static String text_61 = "[任务:";
	public static String text_62 = "确定";
	public static String text_63 = "口";
	public static String text_67 = "您无法到达指定地点，请重新指定目的地！";
	public static String text_68 = "续费";
	public static String text_69 = "镶嵌";
	public static String text_72 = "法宝重铸";
	public static String text_97 = "男";
	public static String text_99 = "级";
	public static String text_101 = "\n需要";
	public static String text_103 = "技能";
	public static String text_105 = "未装备";
	public static String text_114 = "(第";
	public static String text_115 = "环)";
	public static String text_117 = "确认";
	public static String text_121 = "接受";
	public static String text_125 = "是";
	public static String text_126 = "否";
	public static String text_143 = "日";
	public static String text_146 = "小时";
	public static String text_147 = "分";
	public static String text_148 = "金币";
	public static String text_149 = "时间: ";
	public static String text_154 = "拍卖行";
	public static String text_158 = "我的拍卖";
	public static String text_165 = "上一页";
	public static String text_166 = "下一页";
	public static String text_173 = "等级降序";
	public static String text_174 = "当前价降序";
	public static String text_175 = "结束时间升序";
	public static String text_177 = "一口价升序";
	public static String text_178 = "一口价降序";
	public static String text_180 = "单价降序";
	public static String text_184 = "品质：";
	public static String text_185 = "等级：";
	public static String text_186 = "名称：";
	public static String text_187 = "排序：";
	public static String text_188 = "数量[";
	public static String text_189 = "起拍价:";
	public static String text_190 = "一口价:";
	public static String text_191 = "时间(天):";
	public static String text_192 = "税率:";
	public static String text_193 = "竞拍物品";
	public static String text_194 = "确认提交";
	public static String text_195 = "当前价: ";
	public static String text_196 = "一口价: ";
	public static String text_197 = "出价: ";
	public static String text_200 = "如果输入一口价就表示该物品能被[一口价]购买，并且一口价不能小于起拍价，请您重新输入！";
	public static String text_201 = "物品数量错误，请您重新输入！";
	public static String text_202 = "战场详情";
	public static String text_203 = "全部";
	public static String text_204 = "己方";
	public static String text_205 = "敌方";
	public static String text_206 = "名称";
	public static String text_207 = "杀人数";
	public static String text_208 = "荣誉";
	public static String text_215 = "进入战场";
	public static String text_217 = "战场排队";
	public static String text_222 = "(排队中)";
	public static String text_225 = "您未能进入排行榜";
	public static String text_230 = "充值";
	public static String text_231 = "帐号";
	public static String text_232 = "重复帐号";
	public static String text_233 = "类型";
	public static String text_234 = "卡号";
	public static String text_235 = "密码";
	public static String text_236 = "金额";
	public static String text_237 = "神州行";
	public static String text_238 = "联通";
	public static String text_239 = "帐号输入错误！";
	public static String text_240 = "卡号输入错误！";
	public static String text_241 = "密码输入错误！";
	public static String text_242 = "输入错误！";
	public static String text_243 = "信息已发送，请稍后查询账户余额。";
	public static String text_244 = "充值窗口";
	public static String text_245 = "元";
	public static String text_246 = "可兑换";
	public static String text_247 = "个元宝。";
	public static String text_248 = "您今天可充值的金额是：";
	public static String text_249 = "元，您本日已经充值了";
	public static String text_250 = "元，本月已经充值了";
	public static String text_251 = "元。";
	public static String text_252 = "您所选择的充值金额已经超过了限额，请您重新选择。";
	public static String text_253 = "卡号重复，请重新输入！";
	public static String text_254 = "请选择正确的面值！";
	public static String text_255 = "年";
	public static String text_256 = "月";
	public static String text_257 = "地区消息";
	public static String text_258 = "世界消息";
	public static String text_261 = "帮会消息";
	public static String text_263 = "系统消息";
	public static String text_264 = "聊天日志";
	public static String text_268 = "插入任务";
	public static String text_269 = "删除物品";
	public static String text_271 = "插入表情";
	public static String text_272 = "插入动作";
	public static String text_273 = "返回聊天";
	public static String text_274 = "发送内容：";
	public static String text_276 = "发送对象";
	public static String text_277 = "选择表情";
	public static String text_278 = "频道发言";
	public static String text_284 = "关闭";
	public static String text_288 = "不能与自己私聊";
	public static String text_289 = "已经插入道具：";
	public static String text_290 = "已经插入任务：";
	public static String text_291 = "您的";
	public static String text_298 = "确认合成";
	public static String text_299 = "材料不足";
	public static String text_300 = "邀请入会";
	public static String text_304 = "发送邮件";
	public static String text_305 = "交易";
	public static String text_308 = "加黑名单";
	public static String text_309 = "拜师收徒";
	public static String text_310 = "跟随";
	public static String text_313 = "删黑名单";
	public static String text_314 = "删除仇人";
	public static String text_315 = "千里追风";
	public static String text_316 = "逐出师门";
	public static String text_317 = "叛离师门";
	public static String text_318 = "帮派资金";
	public static String text_319 = "捐献";
	public static String text_320 = "更新";
	public static String text_321 = "记录";
	public static String text_322 = "玩家仓库";
	public static String text_323 = "帮派仓库";
	public static String text_324 = "仓库壹";
	public static String text_325 = "仓库贰";
	public static String text_326 = "仓库叁";
	public static String text_327 = "取出";
	public static String text_328 = "整理";
	public static String text_329 = "存入";
	public static String text_330 = "帮派捐献";
	public static String text_331 = "一元宝兑换";
	public static String text_332 = "帮会资金和";
	public static String text_333 = "个人贡献度。";
	public static String text_334 = "输入元宝数量：";
	public static String text_335 = "存取记录";
	public static String text_336 = "邮件列表";
	public static String text_337 = "删除邮件";
	public static String text_338 = "退回邮件";
	public static String text_339 = "阅读邮件";
	public static String text_340 = "新建邮件";
	public static String text_341 = "提取当前页邮件附件";
	public static String text_342 = "删除当前页已读邮件";
	public static String text_343 = "刷新列表";
	public static String text_346 = "您是否确定删除该邮件？";
	public static String text_347 = "\n邮件中有附件！！！";
	public static String text_348 = "1.未读的邮件不会被删除.\n2.带附件的邮件不会被删除.";
	public static String text_349 = "1.如果背包空间不足，将无法提取出所有附件.\n2.不提取付费邮件里的附件。";
	public static String text_350 = "编辑信件";
	public static String text_351 = "附件：";
	public static String text_352 = "输入错误";
	public static String text_355 = "附加道具";
	public static String text_356 = "附加金钱";
	public static String text_357 = "对方付费";
	public static String text_364 = "取消";
	public static String text_369 = "应付金额：";
	public static String text_370 = "无内容";
	public static String text_371 = "好友列表";
	public static String text_372 = "好友";
	public static String text_373 = "黑名单";
	public static String text_374 = "仇人";
	public static String text_375 = "编辑好友";
	public static String text_376 = "玩家名称";
	public static String text_377 = "添加好友";
	public static String text_378 = "(在线)";
	public static String text_380 = " 好感度：";
	public static String text_381 = "脱离帮派";
	public static String text_382 = "群发邮件";
	public static String text_384 = "踢人";
	public static String text_385 = "切换禁言";
	public static String text_386 = "更新公告";
	public static String text_387 = "任命";
	public static String text_388 = "禁言";
	public static String text_389 = "解除";
	public static String text_390 = "帮主";
	public static String text_391 = "副帮主";
	public static String text_392 = "精英";
	public static String text_394 = "姓名";
	public static String text_395 = "等级";
	public static String text_396 = "身份";
	public static String text_397 = "正在下载信息...";
	public static String text_398 = "你确定要退出帮会吗？";
	public static String text_399 = "如果帮主退出帮会，帮会将会被自动解散！";
	public static String text_400 = "该玩家的禁言状态已经改变！";
	public static String text_401 = "该玩家已被踢出帮会！";
	public static String text_402 = "该玩家的头衔已被改变！";
	public static String text_403 = "帮会公告已被更新！";
	public static String text_404 = "你已经离开这个帮会！";
	public static String text_405 = "帮会名称";
	public static String text_406 = "帮主名称";
	public static String text_408 = "帮会等级";
	public static String text_411 = "帮会公告";
	public static String text_412 = "公会成员";
	public static String text_413 = "帮派窗口";
	public static String text_414 = "你确定把";
	public static String text_415 = "踢出帮会吗？";
	public static String text_416 = "贡献度：";
	public static String text_419 = "提示";
	public static String text_421 = "客户端编号：";
	public static String text_422 = "\n北京宣治信息技术有限公司出品\n客服电话：\n010-59059336\n客服邮箱：\nqianlong@sqage.com\n网站：\nhttp://ql.sqage.com\n版本：";
	public static String text_423 = "\n神奇时代网络有限公司出品\n客服电话：\n010-59059336\n客服邮箱：\nqianlong@sqage.com\n网站：\nhttp://ql.sqage.com\n版本：";
	public static String text_424 = "要镶嵌的装备";
	public static String text_427 = "镶嵌至凹槽";
	public static String text_428 = " 凹槽";
	public static String text_429 = "空的凹槽";
	public static String text_430 = "镶嵌会覆盖原有宝石，确认吗？";
	public static String text_431 = "请选择宝石";
	public static String text_432 = "参与排行";
	public static String text_433 = "背包";
	public static String text_434 = "背包(";
	public static String text_435 = "丢弃";
	public static String text_436 = "使用";
	public static String text_437 = "插入";
	public static String text_438 = "添加";
	public static String text_439 = "拍卖";
	public static String text_440 = "融合法宝";
	public static String text_441 = "宝石镶嵌";
	public static String text_442 = "修理";
	public static String text_443 = "快捷1";
	public static String text_444 = "快捷2";
	public static String text_445 = "快捷3";
	public static String text_446 = "快捷4";
	public static String text_447 = "快捷5";
	public static String text_448 = "快捷6";
	public static String text_449 = "快捷7";
	public static String text_450 = "快捷8";
	public static String text_451 = "无法装备";
	public static String text_452 = "该物品已绑定";
	public static String text_453 = "请输入个数1~";
	public static String text_454 = "真的要丢弃物品吗？";
	public static String text_455 = "法宝";
	public static String text_456 = "灵符合成";
	public static String text_457 = "信息";
	public static String text_458 = "级（";
	public static String text_459 = "星级：";
	public static String text_460 = "重：";
	public static String text_461 = "力量：";
	public static String text_462 = "智力：";
	public static String text_463 = "敏捷：";
	public static String text_464 = "耐力：";
	public static String text_465 = "副法宝";
	public static String text_466 = "继承";
	public static String text_467 = "主法宝";
	public static String text_468 = "（已绑定）";
	public static String text_469 = "重";
	public static String text_472 = "绑定";
	public static String text_473 = "您没有";
	public static String text_479 = "请选择一个副法宝";
	public static String text_481 = "请选择一个法宝！";
	public static String text_482 = "区域地图";
	public static String text_483 = "强化装备";
	public static String text_484 = "装备打孔";
	public static String text_485 = "已接任务";
	public static String text_486 = "任务读取中";
	public static String text_487 = "组合技能";
	public static String text_488 = "设定技能";
	public static String text_489 = "取消设定";
	public static String text_490 = "拾取";
	public static String text_491 = "全部拾取";
	public static String text_492 = "详细模式(*)";
	public static String text_493 = "废话少说";
	public static String text_494 = "那就算了";
	public static String text_495 = "选择队员";
	public static String text_496 = "队伍不存在";
	public static String text_497 = "只能由队长分配";
	public static String text_502 = "属性";
	public static String text_503 = "声望";
	public static String text_505 = "成就";
	public static String text_511 = "经验：";
	public static String text_512 = "伤害：";
	public static String text_515 = "外防：";
	public static String text_516 = "内防：";
	public static String text_517 = "命中：";
	public static String text_518 = "闪避：";
	public static String text_519 = "跑速：";
	public static String text_520 = "暴击率：";
	public static String text_521 = "命中率：";
	public static String text_523 = "外功减伤率：";
	public static String text_524 = "内法减伤率：";
	public static String text_526 = "配偶: ";
	public static String text_527 = "未定义：";
	public static String text_529 = "[color=#00ff00]奖励称号：[/color]\n";
	public static String text_530 = "师徒关系";
	public static String text_531 = "徒弟";
	public static String text_532 = "朋友昵称:";
	public static String text_533 = "手机号:";
	public static String text_534 = "免费短信推荐";
	public static String text_535 = "请稍后....";
	public static String text_536 = "手机号码输入错误,请重新输入";
	public static String text_537 = "请输入朋友昵称";
	public static String text_538 = "装备重铸";
	public static String text_539 = "装备读取中";
	public static String text_540 = "系统设置";
	public static String text_541 = "游戏设置";
	public static String text_542 = "显示";
	public static String text_543 = "不显示";
	public static String text_544 = "小地图效果：";
	public static String text_545 = "全透明";
	public static String text_546 = "半透明";
	public static String text_547 = "不透明";
	public static String text_548 = "聊天字幕：";
	public static String text_549 = "文字清晰度：";
	public static String text_550 = "标准";
	public static String text_551 = "超清晰";
	public static String text_552 = "角色阴影：";
	public static String text_553 = "系统提示：";
	public static String text_554 = "组队提示：";
	public static String text_555 = "切磋提示：";
	public static String text_556 = "交易提示：";
	public static String text_557 = "帮会提示：";
	public static String text_558 = "队友的名称：";
	public static String text_559 = "NPC名称：";
	public static String text_560 = "怪物名称：";
	public static String text_561 = "友方玩家名称：";
	public static String text_562 = "敌对玩家名称：";
	public static String text_565 = "组队面板：";
	public static String text_566 = "是否显示称号：";
	public static String text_567 = "网络流量：";
	public static String text_570 = "较省";
	public static String text_573 = "2倍屏幕";
	public static String text_574 = "1.5倍屏幕";
	public static String text_576 = "风景模式：";
	public static String text_577 = "技能特效：";
	public static String text_578 = "震动特效：";
	public static String text_582 = "小";
	public static String text_583 = "大";
	public static String text_587 = "卖出";
	public static String text_588 = "点\n(等同于";
	public static String text_589 = "元)";
	public static String text_590 = "0元)";
	public static String text_591 = "买入价格";
	public static String text_592 = "元宝";
	public static String text_593 = "工资";
	public static String text_594 = "绑定元宝";
	public static String text_595 = "声望\n达到";
	public static String text_596 = "\n卖出价格";
	public static String text_597 = "\n不可装备";
	public static String text_606 = "选定队友";
	public static String text_607 = "敌对玩家";
	public static String text_608 = "选定NPC";
	public static String text_609 = "快速拾取";
	public static String text_610 = "开启聊天";
	public static String text_611 = "敌对目标";
	public static String text_612 = "屏蔽玩家";
	public static String text_613 = "战场信息";
	public static String text_614 = "设定道具";
	public static String text_615 = "设定功能";
	public static String text_616 = "切换目标";
	public static String text_617 = "此处可按顺序设置最多5个技能，连续按键即可顺序发出";
	public static String text_618 = "技能点：";
	public static String text_619 = "加点";
	public static String text_626 = "打孔";
	public static String text_630 = "请放入打孔材料";
	public static String text_631 = "放入材料过多";
	public static String text_633 = " 还可打孔";
	public static String text_634 = "次";
	public static String text_635 = "需要：";
	public static String text_636 = "要强化的装备";
	public static String text_637 = "强化材料";
	public static String text_638 = "提高升级几率";
	public static String text_671 = "交任务";
	public static String text_673 = "共享任务";
	public static String text_676 = "自由分配";
	public static String text_顺序分配 = "顺序分配";
	public static String text_690 = "队长";
	public static String text_973 = "您获得了";
	public static String text_975 = "蓝";
	public static String text_976 = "免疫";
	public static String text_977 = "吸收";
	public static String text_979 = "闪避";
	public static String text_1016 = "装备升级";
	public static String text_1037 = "帮派";
	public static String text_1052 = "开始";
	public static String text_1143 = "强壮";
	public static String text_1152 = "紫微宫";
	public static String text_1153 = "日月盟";
	public static String text_1154 = "少林";
	public static String text_1155 = "武当";
	public static String text_1156 = "峨嵋";
	public static String text_1157 = "明教";
	public static String text_1158 = "五毒";
	public static String text_1211 = "未知";
	public static String text_1218 = "潜龙";
	public static String text_1469 = "点";
	public static String text_1490 = "神奇时代";
	public static String text_1491 = "无";
	public static String text_1492 = "移动充值卡";
	public static String text_1493 = "联通一卡充";
	public static String text_1494 = "电信充值卡";
	public static String text_1495 = "短信";
	public static String text_1498 = "成功";
	public static String text_1582 = "短信充值";
	public static String text_1736 = "其他";
	public static String text_1743 = "阿拉希";
	public static String text_1744 = "5v5互杀";
	public static String text_1745 = "10v10互杀";
	public static String text_1746 = "大逃杀";
	public static String text_1747 = "互杀帮战";
	public static String text_1748 = "仙武大会1";
	public static String text_1749 = "仙武大会2";
	public static String text_1750 = "仙武大会3";
	public static String text_1751 = "仙武大会4";
	public static String text_1752 = "仙武大会5";
	public static String text_1753 = "仙武大会6";
	public static String text_1754 = "两仪门";
	public static String text_1755 = "楼兰古城";
	public static String text_1756 = "乱石河滩";
	public static String text_1757 = "塔林";
	public static String text_1758 = "一线天";
	public static String text_1759 = "紫禁之巅";
	public static String text_1775 = "您获得";
	public static String text_1776 = "点战场荣誉值";
	public static String text_1777 = "获胜方：";
	public static String text_1778 = "获胜";
	public static String text_1779 = "双方平局";
	public static String text_1780 = "平局";
	public static String text_1807 = "报名成功！";
	public static String text_1846 = "中立方";
	public static String text_1850 = "战场排队线程";
	public static String text_1851 = "指定的战场[";
	public static String text_1852 = "]不存在！";
	public static String text_1853 = "您处于跨服战场中，不能排队！";
	public static String text_1854 = "进入战场失败，排队已过期，请重新排队！";
	public static String text_1855 = "进入战场失败，战场已结束！";
	public static String text_1856 = "进入战场失败，您正在跳地图。。！";
	public static String text_1857 = "进入战场失败，您的级别不符合战场的要求";
	public static String text_1858 = "进入战场失败，您已死亡！";
	public static String text_1859 = "进入战场失败，您已掉线";
	public static String text_1860 = "进入战场失败，战场人数已满！";
	public static String text_1862 = "已开启，您是否进入？";
	public static String text_1863 = "进入";
	public static String text_1864 = "您已经副本排队中，不能排队！";
	public static String text_1865 = "您已经在战场中，不能排队！";
	public static String text_1866 = "您的级别不符合战场的要求，不能排队！";
	public static String text_1867 = "您不是对阵双方帮派的成员，不能排队！";
	public static String text_1868 = "您刚才从战场逃跑，";
	public static String text_1869 = "分钟后才能进入战场！";
	public static String text_1870 = "小于1分钟";
	public static String text_1871 = "帮战只能以个人的形式加入！";
	public static String text_1872 = "您不是队长，不能以队伍的方式排队！";
	public static String text_1873 = "您已经能够在战场中，不能排队！";
	public static String text_1874 = "队员";
	public static String text_1875 = "的级别不符合战场的要求，不能排队！";
	public static String text_1876 = "刚才从战场逃跑，";
	public static String text_1877 = "(队员:";
	public static String text_1878 = "你的队伍已进入[";
	public static String text_1879 = "]排队系统！";
	public static String text_1881 = "丛林野战";
	public static String text_1882 = "20级丛林野战";
	public static String text_1883 = "逃亡";
	public static String text_1884 = "丛林是紫薇宫和日月盟之间的主要战场之一。这里不仅是东西联络的重要阵地；传说这里还埋葬着一个巨大的宝藏，无论是探索者还是统治者都希望拥有；这里的战争充斥着欲望与贪婪";
	public static String text_1885 = "30级丛林野战";
	public static String text_1886 = "40级丛林野战";
	public static String text_1887 = "50级丛林野战";
	public static String text_1888 = "幽谷试炼";
	public static String text_1889 = "20级幽谷试炼";
	public static String text_1890 = "在九黎墟地宫的南方有着一处地方藏着神奇的盘古神器，此神器为日月盟与紫薇宫所知具有盘古留给下来神兵利器相关的重要情报；拥有它可以主宰一切";
	public static String text_1891 = "30级幽谷试炼";
	public static String text_1892 = "40级幽谷试炼";
	public static String text_1893 = "50级幽谷试炼";
	public static String text_1894 = "幽谷血战";
	public static String text_1895 = "20级幽谷血战";
	public static String text_1896 = "为了九黎墟地宫的神器情报，日月盟与紫薇宫不惜一切的发动了大规模的战斗，这是充满了权势的一场场厮杀";
	public static String text_1897 = "30级幽谷血战";
	public static String text_1898 = "40级幽谷血战";
	public static String text_1899 = "50级幽谷血战";
	public static String text_1900 = "仙武大会专用地图";
	public static String text_1901 = "经过整局激烈的战斗，[color=";
	public static String text_1902 = "[/color]的伤害量为[color=";
	public static String text_1903 = "[/color]，最终[color=";
	public static String text_1904 = "[/color]以微弱的优势战胜了[color=";
	public static String text_1910 = "[/color]击倒了[color=";
	public static String text_1911 = "[/color]，取得了比赛的胜利！";
	public static String text_1912 = "最终决赛 观众：";
	public static String text_1913 = "决赛阶段（败者组） 观众：";
	public static String text_1914 = "决赛阶段（胜者组） 观众：";
	public static String text_1915 = "预赛阶段 观众：";
	public static String text_1916 = " 胜利：";
	public static String text_1917 = "比赛结束";
	public static String text_1918 = "退出战场";
	public static String text_1919 = "[/color]放弃比赛，[color=";
	public static String text_1920 = "[/color]获得胜利！";
	public static String text_1931 = "A方：,B方：";
	public static String text_1932 = "A方上路近程兵";
	public static String text_1933 = "A方上路远程兵";
	public static String text_1934 = "A方中路近程兵";
	public static String text_1935 = "A方中路远程兵";
	public static String text_1936 = "A方下路近程兵";
	public static String text_1937 = "A方下路远程兵";
	public static String text_1938 = "B方上路近程兵";
	public static String text_1939 = "B方上路远程兵";
	public static String text_1940 = "B方中路近程兵";
	public static String text_1941 = "B方中路远程兵";
	public static String text_1942 = "B方下路近程兵";
	public static String text_1943 = "B方下路远程兵";
	public static String text_1944 = "近程兵";
	public static String text_1945 = "远程兵";
	public static String text_1946 = "攻城兵";
	public static String text_1948 = "主将";
	public static String text_1950 = "战场提升BUFF";
	public static String text_1951 = "上路前塔";
	public static String text_1952 = "上路中塔";
	public static String text_1953 = "上路后塔";
	public static String text_1954 = "中路前塔";
	public static String text_1955 = "中路中塔";
	public static String text_1956 = "中路后塔";
	public static String text_1957 = "下路前塔";
	public static String text_1958 = "下路中塔";
	public static String text_1959 = "下路后塔";
	public static String text_1960 = "主将护塔1";
	public static String text_1961 = "主将护塔2";
	public static String text_1962 = "上路近程军营";
	public static String text_1963 = "上路远程军营";
	public static String text_1964 = "中路近程军营";
	public static String text_1965 = "中路远程军营";
	public static String text_1966 = "下路近程军营";
	public static String text_1967 = "下路远程军营";
	public static String text_1968 = "温泉防御塔";
	public static String text_1969 = "测试战场将在10秒钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_1970 = "测试战场将在30秒钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_1971 = "测试战场将在1分钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_2003 = "战场结束，";
	public static String text_2004 = "获得了战场的胜利！";
	public static String text_2006 = "帮战";
	public static String text_2007 = "战场徽章";
	public static String text_2008 = "禁卫军团";
	public static String text_2009 = "天灾军团";
	public static String text_2010 = "云梦白果";
	public static String text_2011 = "不死之草";
	public static String text_2012 = "墨厘精石";
	public static String text_2013 = "疾浪古木";
	public static String text_2014 = "火流精石";
	public static String text_2015 = "沉香之木";
	public static String text_2016 = "树枝";
	public static String text_2017 = "力量手套";
	public static String text_2018 = "敏捷便鞋";
	public static String text_2019 = "智力斗篷";
	public static String text_2020 = "守护指环";
	public static String text_2021 = "环饰";
	public static String text_2022 = "巨人力量腰带";
	public static String text_2023 = "精灵皮鞋";
	public static String text_2024 = "大法师长袍";
	public static String text_2025 = "锁子甲";
	public static String text_2026 = "食人魔斧";
	public static String text_2027 = "欣欢之刃";
	public static String text_2028 = "魔力法仗";
	public static String text_2029 = "铁意头盔";
	public static String text_2030 = "力量护腕";
	public static String text_2031 = "幽灵系带";
	public static String text_2032 = "无用挂件";
	public static String text_2033 = "速度之鞋";
	public static String text_2034 = "动力鞋";
	public static String text_2035 = "相位鞋";
	public static String text_2036 = "远行鞋";
	public static String text_2037 = "动力鞋智力";
	public static String text_2038 = "动力鞋力量";
	public static String text_2039 = "动力鞋敏捷";
	public static String text_2040 = "相位鞋耐力";
	public static String text_2041 = "极限法球";
	public static String text_2042 = "掠夺之斧";
	public static String text_2043 = "鹰角弓";
	public static String text_2044 = "神秘法杖";
	public static String text_2045 = "板甲";
	public static String text_2046 = "坚韧球";
	public static String text_2047 = "米达斯之手";
	public static String text_2048 = "夜叉";
	public static String text_2049 = "散华";
	public static String text_2050 = "漩涡";
	public static String text_2051 = "散失之刃";
	public static String text_2052 = "支配头盔";
	public static String text_2053 = "疯狂面具";
	public static String text_2054 = "震魂石";
	public static String text_2055 = "梅肯斯姆";
	public static String text_2056 = "双刀";
	public static String text_2057 = "黯灭";
	public static String text_2058 = "狂战斧";
	public static String text_2059 = "水晶剑";
	public static String text_2060 = "黑皇权杖";
	public static String text_2061 = "幻影斧";
	public static String text_2062 = "洛萨之锋";
	public static String text_2063 = "林肯法球";
	public static String text_2064 = "圣剑";
	public static String text_2065 = "大炮";
	public static String text_2066 = "金箍棒";
	public static String text_2067 = "辉耀";
	public static String text_2068 = "魔龙之心";
	public static String text_2069 = "撒旦邪力";
	public static String text_2070 = "蓝杖";
	public static String text_2071 = "刷新球";
	public static String text_2072 = "羊刀";
	public static String text_2073 = "先锋盾";
	public static String text_2074 = "密法戒指";
	public static String text_2075 = "大电锤";
	public static String text_2076 = "血精石";
	public static String text_2077 = "希尔瓦之力";
	public static String text_2078 = "蝴蝶";
	public static String text_2258 = "号选手";
	public static String text_2305 = "金币)";
	public static String text_2308 = "日常更新排行榜";
	public static String text_2309 = "天下第一";
	public static String text_2310 = "荣耀帮会";
	public static String text_2311 = "至尊神器";
	public static String text_2312 = "战场名将";
	public static String text_2313 = "速刷狂人";
	public static String text_2314 = "嗜血杀神";
	public static String text_2315 = "在线达人";
	public static String text_2316 = "富甲天下";
	public static String text_2317 = "玩家";
	public static String text_2318 = "城市";
	public static String text_2319 = "宠物";
	public static String text_2320 = "名次";
	public static String text_2321 = "人数";
	public static String text_2322 = "胜绩";
	public static String text_2323 = "击杀总榜";
	public static String text_2324 = "击杀周榜";
	public static String text_2325 = "丛林野战胜绩";
	public static String text_2326 = "幽谷试炼胜绩";
	public static String text_2327 = "幽谷血战胜绩";
	public static String text_2328 = "耗时";
	public static String text_2329 = "队伍成员：\n";
	public static String text_2330 = "武器排行";
	public static String text_2331 = "头盔排行";
	public static String text_2332 = "护肩排行";
	public static String text_2333 = "衣服排行";
	public static String text_2334 = "护腕排行";
	public static String text_2335 = "鞋子排行";
	public static String text_2336 = "首饰排行";
	public static String text_2337 = "项链排行";
	public static String text_2338 = "戒指排行";
	public static String text_2339 = "更新武器排行榜";
	public static String text_2343 = "天下排行";
	public static String text_2344 = "少林排行";
	public static String text_2345 = "武当排行";
	public static String text_2346 = "峨眉排行";
	public static String text_2347 = "明教排行";
	public static String text_2348 = "五毒排行";
	public static String text_2349 = "帮会";
	public static String text_2350 = "资金";
	public static String text_2351 = "积分";
	public static String text_2352 = "帮会资金";
	public static String text_2353 = "帮会人数";
	public static String text_2354 = "帮会积分";
	public static String text_2355 = "收入";
	public static String text_2356 = "拍卖总榜";
	public static String text_2357 = "拍卖周榜";
	public static String text_2358 = "富可敌国";
	public static String text_2359 = "富甲一方";
	public static String text_2360 = "嗜血狂人";
	public static String text_2361 = "冷血杀手";
	public static String text_2362 = "职业杀手";
	public static String text_2363 = "天数";
	public static String text_2364 = "超时失效";
	public static String text_2365 = "缓存溢出";
	public static String text_2367 = "智力";
	public static String text_2368 = "耐力";
	public static String text_2369 = "力量";
	public static String text_2370 = "敏捷";
	public static String text_2371 = "生命值";
	public static String text_2372 = "法力值";
	public static String text_2373 = "法术攻击";
	public static String text_2374 = "物理攻击";
	public static String text_2375 = "每5秒仙法回复";
	public static String text_2376 = "每5秒生命回复";
	public static String text_2377 = "反伤";
	public static String text_2378 = "吸血";
	public static String text_2379 = "命中等级";
	public static String text_2380 = "闪避等级";
	public static String text_2381 = "暴击等级";
	public static String text_2382 = "移动速度";
	public static String text_2383 = "物理防御";
	public static String text_2384 = "法术防御";
	public static String text_2385 = "外功减伤";
	public static String text_2386 = "内法减伤";
	public static String text_2387 = "(物品等级*品质/200)+1";
	public static String text_2388 = "(物品等级*品质/120)+1";
	public static String text_2389 = "(物品等级*品质/60)+1";
	public static String text_2390 = "(物品等级*品质/50)+1";
	public static String text_2391 = "(物品等级*品质/150)+1";
	public static String text_2392 = "(物品等级*品质/30)+1";
	public static String text_2393 = "(物品等级*品质/20)+1";
	public static String text_2394 = "(物品等级*品质/180)+1";
	public static String text_2395 = "(物品等级*品质/25)+1";
	public static String text_2396 = "(物品等级*品质/12)+10";
	public static String text_2397 = "(物品等级*品质/5)+10";
	public static String text_2398 = "(物品等级*品质/3)+10";
	public static String text_2399 = "(物品等级*品质/2)+10";
	public static String text_2400 = "(物品等级*品质/15)+10";
	public static String text_2401 = "(物品等级*品质/8)+10";
	public static String text_2402 = "(物品等级*品质*0.34*0.08)+1";
	public static String text_2403 = "(物品等级*品质*0.34*0.2)+1";
	public static String text_2404 = "(物品等级*品质*0.34*0.66)+1";
	public static String text_2405 = "(物品等级*品质*0.34*0.80)+1";
	public static String text_2406 = "(物品等级*品质/5*0.08)+10";
	public static String text_2407 = "(物品等级*品质/5*0.2)+10";
	public static String text_2408 = "(物品等级*品质/5*0.66)+10";
	public static String text_2409 = "(物品等级*品质/5*0.80)+10";
	public static String text_2410 = "(物品等级*品质/40)+1";
	public static String text_2411 = "(物品等级*品质*15/200*0.3)+1";
	public static String text_2412 = "(物品等级*品质*15/120*0.3)+1";
	public static String text_2413 = "(物品等级*品质*15/36*0.3)+1";
	public static String text_2414 = "(物品等级*品质*15/36*0.5)+1";
	public static String text_2415 = "(物品等级*品质*15/200*0.2)+1";
	public static String text_2416 = "(物品等级*品质*15/120*0.2)+1";
	public static String text_2417 = "(物品等级*品质*15/36*0.2)+1";
	public static String text_2418 = "(物品等级*品质*15/36*0.4)+1";
	public static String text_2419 = "(物品等级*品质/80)+1";
	public static String text_2420 = "(物品等级*品质/90)+1";
	public static String text_2421 = "(物品等级*品质/6)+10";
	public static String text_2422 = "(物品等级*品质/4)+10";
	public static String text_2423 = "(物品等级*品质*15/200*0.1)+1";
	public static String text_2424 = "(物品等级*品质*15/120*0.1)+1";
	public static String text_2425 = "(物品等级*品质*15/36*0.1)+1";
	public static String text_2426 = "(物品等级*品质/35)+1";
	public static String text_2427 = "(物品等级*品质*部件系数/1500)+1";
	public static String text_2428 = "(物品等级*品质*部件系数/800)+1";
	public static String text_2429 = "(物品等级*品质*部件系数/400)+1";
	public static String text_2430 = "(物品等级*品质*部件系数/300)+1";
	public static String text_2431 = "(物品等级*品质*部件系数/1800)+1";
	public static String text_2432 = "(物品等级*品质*部件系数/600)+1";
	public static String text_2433 = "(物品等级*品质*部件系数/350)+1";
	public static String text_2434 = "(物品等级*品质*部件系数/200)+1";
	public static String text_2435 = "(物品等级*品质*部件系数/250)+1";
	public static String text_2436 = "(物品等级*品质*部件系数/120)+10";
	public static String text_2437 = "(物品等级*品质*部件系数/50)+10";
	public static String text_2438 = "(物品等级*品质*部件系数/30)+10";
	public static String text_2439 = "(物品等级*品质*部件系数/20)+10";
	public static String text_2440 = "(物品等级*品质*部件系数/95)+10";
	public static String text_2441 = "(物品等级*品质*部件系数/65)+10";
	public static String text_2442 = "(物品等级*品质*部件系数/25)+10";
	public static String text_2443 = "(物品等级*品质*0.02*0.06*部件系数)+1";
	public static String text_2444 = "(物品等级*品质*0.02*部件系数*0.12)+1";
	public static String text_2445 = "(物品等级*品质*0.02*部件系数*0.24)+1";
	public static String text_2446 = "(物品等级*品质*0.02*部件系数*0.32)+1";
	public static String text_2447 = "(物品等级*品质*部件系数/75*0.06)+10";
	public static String text_2448 = "(物品等级*品质*部件系数/75*0.12)+10";
	public static String text_2449 = "(物品等级*品质*部件系数/75*0.24)+10";
	public static String text_2450 = "(物品等级*品质*部件系数/75*0.32)+10";
	public static String text_2451 = "(物品等级*品质*部件系数/200*0.2)+1";
	public static String text_2452 = "(物品等级*品质*部件系数/120*0.2)+1";
	public static String text_2453 = "(物品等级*品质*部件系数/36*0.2)+1";
	public static String text_2454 = "(物品等级*品质*部件系数/36*0.3)+1";
	public static String text_2455 = "(物品等级*品质*部件系数/60)+10";
	public static String text_2456 = "(物品等级*品质*部件系数/40)+10";
	public static String text_2457 = "(物品等级*品质*部件系数/15)+10";
	public static String text_2458 = "(物品等级*品质*部件系数/200*0.1)+1";
	public static String text_2459 = "(物品等级*品质*部件系数/120*0.1)+1";
	public static String text_2460 = "(物品等级*品质*部件系数/36*0.1)+1";
	public static String text_2461 = "(物品等级*品质*部件系数/200*0.3)+1";
	public static String text_2462 = "(物品等级*品质*部件系数/120*0.3)+1";
	public static String text_2463 = "(物品等级*品质*部件系数/150)+1";
	public static String text_2464 = "(物品等级*品质*部件系数/36*0.5)+1";
	public static String text_2465 = "物品等级";
	public static String text_2466 = "品质";
	public static String text_2467 = "部件系数";
	public static String text_2468 = "脚本错误[";
	public static String text_2471 = "攻击强度";
	public static String text_2476 = "物理防御";
	public static String text_2477 = "法术防御";
	public static String text_2478 = "命中";
	public static String text_2479 = "暴击";
	public static String text_2485 = "战士";
	public static String text_2486 = "法师";
	public static String text_2487 = "牧师";
	public static String text_2488 = "刺客";
	public static String text_2489 = "术士";
	public static String text_2490 = "普通怪物";
	public static String text_2491 = "精英怪物";
	public static String text_2492 = "副本精英";
	public static String text_2493 = "人形";
	public static String text_2494 = "动物";
	public static String text_2495 = "猛兽";
	public static String text_2496 = "血量参数1";
	public static String text_2497 = "血量参数2";
	public static String text_2498 = "血量参数3";
	public static String text_2499 = "血量参数4";
	public static String text_2500 = "血量参数5";
	public static String text_2501 = "血量参数6";
	public static String text_2502 = "血量参数7";
	public static String text_2503 = "血量参数8";
	public static String text_2504 = "血量参数9";
	public static String text_2505 = "血量参数10";
	public static String text_2506 = "攻击速度1";
	public static String text_2507 = "攻击速度2";
	public static String text_2508 = "攻击速度3";
	public static String text_2509 = "攻击参数1";
	public static String text_2510 = "攻击参数2";
	public static String text_2511 = "攻击参数3";
	public static String text_2512 = "攻击参数4";
	public static String text_2513 = "攻击参数5";
	public static String text_2514 = "法力参数";
	public static String text_2515 = "防御参数1";
	public static String text_2516 = "防御参数2";
	public static String text_2517 = "防御参数3";
	public static String text_2518 = "防御参数4";
	public static String text_2519 = "血量";
	public static String text_2520 = "法力值";
	public static String text_2521 = "物理攻击上限";
	public static String text_2522 = "物理攻击下限";
	public static String text_2523 = "法术攻击上限";
	public static String text_2524 = "法术攻击下限";
	public static String text_2525 = "攻击速度";
	public static String text_2526 = "掉落经验";
	public static String text_2527 = "(血量参数1 + (等级-1)) * 20 * 血量参数2";
	public static String text_2528 = "(6 + (等级-1)) * 15 * 法力参数";
	public static String text_2529 = "(攻击参数1 + 等级 - 1) * 攻击速度1";
	public static String text_2530 = "(攻击参数1 + 等级 - 1) * 攻击速度1 * 0.9";
	public static String text_2531 = "10 + (等级-1) * 防御参数1";
	public static String text_2532 = "15 + (等级-1) * 10 ";
	public static String text_2533 = "(血量参数1 + (等级-1)) * 20 * 血量参数3";
	public static String text_2534 = "(攻击参数1 + 等级 - 1) * 攻击速度1 * 攻击参数2";
	public static String text_2535 = "(攻击参数1 + 等级 - 1) * 攻击速度1 * 攻击参数2 * 0.9";
	public static String text_2536 = "10 + (等级-1) * 防御参数3";
	public static String text_2537 = "(血量参数1 + (等级-1)) * 20 * 血量参数4";
	public static String text_2538 = "(攻击参数1 + 等级 - 1) * (攻击速度1-0.5) * 攻击参数3 ";
	public static String text_2539 = "(攻击参数1 + 等级 - 1) * (攻击速度1-0.5) * 攻击参数3 * 0.9";
	public static String text_2540 = "攻击速度1 - 0.5";
	public static String text_2541 = "(血量参数1 + (等级-1)) * 20 * 血量参数5";
	public static String text_2542 = "(攻击参数4 + 等级 - 1) * 攻击速度2";
	public static String text_2543 = "(攻击参数4 + 等级 - 1) * 攻击速度2 * 0.9";
	public static String text_2544 = "(血量参数1 + (等级-1)) * 20 * 血量参数6";
	public static String text_2545 = "(攻击参数4 + 等级 - 1) * 攻击速度2 * 攻击参数2";
	public static String text_2546 = "(攻击参数4 + 等级 - 1) * 攻击速度2 * 攻击参数2 * 0.9";
	public static String text_2547 = "(血量参数1 + (等级-1)) * 20 * 血量参数7";
	public static String text_2548 = "(攻击参数4 + 等级 - 1) *( 攻击速度2 - 0.5) * 攻击参数3";
	public static String text_2549 = "(攻击参数4 + 等级 - 1) *( 攻击速度2 - 0.5) * 攻击参数3 * 0.9";
	public static String text_2550 = "攻击速度2 - 0.5";
	public static String text_2551 = "10 + (等级-1) * 防御参数4";
	public static String text_2552 = "(血量参数1 + (等级-1)) * 20 * 血量参数8";
	public static String text_2553 = "(攻击参数5 + 等级 - 1) * 攻击速度3";
	public static String text_2554 = "(攻击参数5 + 等级 - 1) * 攻击速度3 * 0.9";
	public static String text_2555 = "10 + (等级-1) * 防御参数2";
	public static String text_2556 = "(血量参数1 + (等级-1)) * 20 * 血量参数9";
	public static String text_2557 = "(攻击参数5 + 等级 - 1) * 攻击速度3 * 攻击参数2";
	public static String text_2558 = "(攻击参数5 + 等级 - 1) * 攻击速度3 * 攻击参数2 * 0.9";
	public static String text_2559 = "(血量参数1 + (等级-1)) * 20 * 血量参数10";
	public static String text_2560 = "(攻击参数5 + 等级 - 1) * (攻击速度3-0.5) * 攻击参数3";
	public static String text_2561 = "(攻击参数5 + 等级 - 1) * (攻击速度3-0.5) * 攻击参数3 * 0.9";
	public static String text_2562 = "攻击速度3 - 0.5";
	public static String text_2570 = "移动";
	public static String text_2571 = "电信";
	public static String text_2579 = "发送内容包含非法信息!";
	public static String text_2581 = "您在此频道被禁言！";
	public static String text_2583 = "无此玩家或玩家不在线";
	public static String text_2584 = "对方已把您加入黑名单，您不能发生聊天信息！";
	public static String text_2585 = "玩家[";
	public static String text_2586 = "]的私聊频道已关闭!";
	public static String text_2587 = "]正在比武赛场!";
	public static String text_2588 = "比武战场中不能聊天！";
	public static String text_2613 = "帮战竞标失败";
	public static String text_2631 = "珠玉之虚无石";
	public static String text_2632 = "补偿玩家记录";
	public static String text_2633 = "[/color]消费";
	public static String text_2634 = "次，差价";
	public static String text_2637 = "中立阵营";
	public static String text_2638 = "炼狱绝地";
	public static String text_2640 = "炼狱绝地";
	public static String text_2644 = "回魂珠";
	public static String text_2689 = "地图信息为空";
	public static String text_2690 = "物品缺失";
	public static String text_2712 = "您的角色处于锁定状态，暂时不能参与交易！";
	public static String text_2713 = "您现在身处跨服服务器，暂时不能参与交易！";
	public static String text_2715 = "对方在比武赛场，暂时不能交易！";
	public static String text_2721 = "您现在身处跨服服务器，暂时不能使用仓库！";
	public static String text_2723 = "用户背包已满，无法放置更多物品";
	public static String text_2724 = "您的角色处于锁定状态，暂时不能使用邮箱！";
	public static String text_2725 = "您暂时不能使用邮箱！";
	public static String text_2727 = "/空/";
	public static String text_2728 = "物品id:";
	public static String text_2729 = "/数量:";
	public static String text_2730 = "cell为空,";
	public static String text_2731 = "邮件不存在！";
	public static String text_2732 = "邮件不属于你！";
	public static String text_2733 = "需要支付";
	public static String text_2734 = "金币，才能提取附件。确定提取附件吗？";
	public static String text_2737 = "您被对方加入了黑名单，无法发送邮件";
	public static String text_2749 = "】，您确定吗？";
	public static String text_2762 = "[退]";
	public static String text_2763 = "]退回了您的邮件。原邮件内容:\n";
	public static String text_2776 = "对不起，服务器配置错误，商店[";
	public static String text_2777 = "]并不存在";
	public static String text_2778 = "您的角色处于锁定状态，暂时不能购买商品！";
	public static String text_2779 = "您的角色处于锁定状态，暂时不能出售物品！";
	public static String text_2786 = "您的角色处于锁定状态，暂不能修改社会关系！";
	public static String text_2787 = "您现在身处跨服服务器，暂不能修改社会关系！";
	public static String text_2881 = "您现在身处跨服服务器，暂时不能参与装备排行！";
	public static String text_2882 = "您的装备已经加入到排行榜中，请到排行榜界面查看详细排名。";
	public static String text_2883 = "您的装备未能进入排行榜。";
	public static String text_2884 = "您现在身处跨服服务器，暂时不能进行充值！";
	public static String text_2885 = "您的等级还不到15级，不能用短信充值！";
	public static String text_2887 = "这个活动不存在！";
	public static String text_2888 = "流量统计";
	public static String text_2889 = "0小时";
	public static String text_2890 = "]在线时长[/color]: ";
	public static String text_2891 = "分钟\n";
	public static String text_2892 = "]接收流量[/color]: ";
	public static String text_2893 = "M/小时\n";
	public static String text_2894 = "]发送流量[/color]: ";
	public static String text_2895 = "]接收数据[/color]: ";
	public static String text_2896 = "字节\n";
	public static String text_2897 = "]发送数据[/color]: ";
	public static String text_2898 = "]总 时 长[/color]: ";
	public static String text_2899 = "]网络延迟[/color]: ";
	public static String text_2900 = "毫秒\n";
	public static String text_2901 = "关闭窗口";
	public static String text_2902 = "您已经屏蔽了周围所有的玩家，您将不能与别人进行交易，决斗等互动行为！！！";
	public static String text_2903 = "您现在正处于比武赛场中，不能进行称号操作！";
	public static String text_2908 = "您现在在比武赛场，不能进行战场操作！";
	public static String text_2911 = "你不在任何战场中！";
	public static String text_2983 = "古祭坛";
	public static String text_2984 = "紫微宫本阵";
	public static String text_2985 = "日月盟大营";
	public static String text_2986 = "文天祥化体";
	public static String text_2987 = "刘伯温分身";
	public static String text_2988 = "超时强制踢掉";
	public static String text_2989 = "您的角色处于锁定状态，暂时不能进行物品操作！";
	public static String text_2990 = "您的角色处于比武赛场，暂时不能进行物品操作！";
	public static String text_2991 = "在跨服服务器中，不能使用续费功能";
	public static String text_2992 = "使用无效，指定背包中的格子中没有任何东西！";
	public static String text_2993 = "您的角色处于比武赛场，暂时不能进行装备操作！";
	public static String text_2994 = "在跨服服务器中，不能使用";
	public static String text_3008 = "您的角色处于比武赛场，暂时不能进行宝石镶嵌！";
	public static String text_3009 = "您的角色处于锁定状态，暂时不能进行宝石镶嵌！";
	public static String text_3048 = "拾取失败，对应的NPC[";
	public static String text_3049 = "]不在当前地图！";
	public static String text_3050 = "拾取失败！";
	public static String text_3086 = "采摘";
	public static String text_3096 = "准备跳地图(";
	public static String text_3097 = "站立";
	public static String text_3098 = "战斗";
	public static String text_3099 = "死亡";
	public static String text_3100 = "僵直";
	public static String text_3101 = "寻路-无扰动";
	public static String text_3102 = "寻路-垂直扰动";
	public static String text_3103 = "寻路-水平扰动";
	public static String text_3104 = "寻路-对角线扰动";
	public static String text_3105 = "寻路-自适应扰动";
	public static String text_3106 = "Dijkstra算法";
	public static String text_3107 = "BFS算法";
	public static String text_3108 = "错误的类型{";
	public static String text_3109 = "新手村副本心跳线程-";
	public static String text_3136 = "暴击1";
	public static String text_3137 = "暴击2";
	public static String text_3138 = "影响技能执行结果的BUFF";
	public static String text_3139 = "增加暴击";
	public static String text_3140 = "增加爆击率多少百分比";
	public static String text_3141 = "增加暴击率";
	public static String text_3142 = "降低暴击率";
	public static String text_3143 = "血上限点数与暴击点数转换";
	public static String text_3144 = "增加血上限";
	public static String text_3145 = "降低血上限";
	public static String text_3146 = "降低暴击";
	public static String text_3147 = "增加暴击值";
	public static String text_3148 = "降低暴击值";
	public static String text_3149 = "封魔";
	public static String text_3150 = "封魔，不能使用主动技能";
	public static String text_3151 = "迟钝";
	public static String text_3152 = "降低命中率多少百分比和降低闪避率多少百分比";
	public static String text_3153 = "降低命中";
	public static String text_3154 = "降低闪避";
	public static String text_3155 = "抽蓝";
	public static String text_3156 = "战斗中吸取多少比例的血转变为蓝";
	public static String text_3157 = "吸收对敌人伤害的";
	public static String text_3158 = "%加到自己的仙法上";
	public static String text_3159 = "吸收对敌人的伤害加到自己的仙法上";
	public static String text_3160 = "抽血";
	public static String text_3161 = "战斗中吸取多少比例的血转变为自己的血";
	public static String text_3162 = "吸血，buff持续时间内，对敌人造成伤害的";
	public static String text_3163 = "%转化为自己的生命值";
	public static String text_3164 = "吸血，buff持续时间内，对敌人造成伤害的一定百分比转化为自己的生命值";
	public static String text_3165 = "增加仇恨比例";
	public static String text_3166 = "战斗中增加给敌人的仇恨值，每一点表示一个百分点";
	public static String text_3167 = "增加自己对敌人的仇恨值";
	public static String text_3168 = "仇恨第一";
	public static String text_3169 = "将自身放到仇恨列表的第一位";
	public static String text_3170 = "定身";
	public static String text_3173 = "战斗中给敌人造成";
	public static String text_3174 = "%反伤";
	public static String text_3176 = "护盾";
	public static String text_3177 = "一段时间内能吸收多少伤害";
	public static String text_3178 = "增加温泉经验";
	public static String text_3180 = "禁止进入战场";
	public static String text_3181 = "攻击强度点数与暴击点数转换";
	public static String text_3182 = "增加攻击强度";
	public static String text_3183 = "降低攻击强度";
	public static String text_3184 = "攻击强度和减少伤害百分比";
	public static String text_3185 = "受到伤害减少";
	public static String text_3186 = "受到伤害增加";
	public static String text_3187 = "受到伤害减少比例";
	public static String text_3188 = "攻击强度点数与血上限点数转换";
	public static String text_3189 = "攻击强度百分比与血上限百分比转换";
	public static String text_3190 = "增加怪物掉率";
	public static String text_3191 = "增加怪物掉率多少百分比";
	public static String text_3192 = "双倍掉落物品的概率";
	public static String text_3193 = "增加物品掉落率";
	public static String text_3194 = "降低怪物物品掉落率";
	public static String text_3195 = "增加怪物物品掉落率";
	public static String text_3196 = "增加怪物掉落金钱数量";
	public static String text_3197 = "增加怪物掉落金钱多少百分比";
	public static String text_3198 = "降低怪物掉落金钱数量";
	public static String text_3199 = "增加荣誉";
	public static String text_3200 = "降低荣誉";
	public static String text_3201 = "点伤害";
	public static String text_3202 = "法术攻击强度相关的护盾";
	public static String text_3203 = "产生一个护盾，护盾效果与施法者内法攻击强度相关";
	public static String text_3204 = "每5秒钟回血和法术攻击强度相关的护盾";
	public static String text_3205 = "每5秒钟恢复生命值";
	public static String text_3206 = "每5秒钟恢复生命值一定数量的点";
	public static String text_3207 = "闪避和法术攻击强度相关的护盾";
	public static String text_3208 = "增加闪避";
	public static String text_3209 = "速度及法术攻击强度相关的护盾";
	public static String text_3210 = "增加移动速度";
	public static String text_3211 = "降低移动速度";
	public static String text_3212 = "移动速度增加";
	public static String text_3213 = "每5秒钟回血和护盾";
	public static String text_3214 = "每5秒钟回血和一段时间内能吸收多少伤害";
	public static String text_3215 = "闪避和护盾";
	public static String text_3216 = "闪避和一段时间内能吸收多少伤害";
	public static String text_3217 = "速度及护盾";
	public static String text_3218 = "速度及一段时间内能吸收多少伤害";
	public static String text_3219 = "物理攻击强度相关的护盾";
	public static String text_3220 = "产生一个护盾，护盾效果与施法者外功攻击强度相关";
	public static String text_3221 = "每5秒钟回血和物理攻击强度相关的护盾";
	public static String text_3222 = "闪避和物理攻击强度相关的护盾";
	public static String text_3223 = "速度及外攻攻击强度相关的护盾";
	public static String text_3224 = "加蓝";
	public static String text_3225 = "每半秒钟恢复蓝多少点";
	public static String text_3226 = "每半秒钟恢复法力值";
	public static String text_3227 = "每半秒钟恢复法力值一定的点";
	public static String text_3228 = "每5秒钟恢复蓝多少点";
	public static String text_3229 = "每5秒钟恢复法力值";
	public static String text_3230 = "每5秒钟恢复法力值一定的点";
	public static String text_3231 = "每";
	public static String text_3232 = "秒增加";
	public static String text_3233 = "%法力值";
	public static String text_3234 = "秒减少";
	public static String text_3235 = "蓝上限";
	public static String text_3236 = "增加法力值上限";
	public static String text_3237 = "降低法力值上限";
	public static String text_3238 = "增加法力值上限百分比";
	public static String text_3239 = "降低爆击率多少百分比";
	public static String text_3240 = "降低整体输出伤害比例";
	public static String text_3241 = "降低整体输出伤害";
	public static String text_3242 = "增加整体输出伤害";
	public static String text_3243 = "降低外功攻击强度和内法攻击强度百分比";
	public static String text_3244 = "降低外功攻击强度";
	public static String text_3245 = "增加外功攻击强度";
	public static String text_3246 = "降低内法攻击强度";
	public static String text_3247 = "增加内法攻击强度";
	public static String text_3248 = "减蓝";
	public static String text_3249 = "法术攻击强度相关的减少法力值";
	public static String text_3250 = "秒减少法力值，与内法攻击强度相关";
	public static String text_3251 = "物理攻击强度相关的减少法力值";
	public static String text_3252 = "秒减少法力值，与外功攻击强度相关";
	public static String text_3253 = "减少力量";
	public static String text_3254 = "减少力量多少百分比";
	public static String text_3255 = "增加力量";
	public static String text_3256 = "减少力量值";
	public static String text_3257 = "降低命中率多少百分比";
	public static String text_3258 = "降低命中率";
	public static String text_3259 = "增加命中率";
	public static String text_3260 = "降低敏捷";
	public static String text_3261 = "降低敏捷百分比";
	public static String text_3262 = "增加敏捷";
	public static String text_3263 = "降低闪避率多少百分比";
	public static String text_3264 = "降低闪避率";
	public static String text_3265 = "增加闪避率";
	public static String text_3266 = "受到伤害减成比例";
	public static String text_3267 = "减速";
	public static String text_3268 = "降低速度多少百分比";
	public static String text_3269 = "移动速度降低";
	public static String text_3270 = "移动速度降低一定比例";
	public static String text_3271 = "降低耐力";
	public static String text_3272 = "降低耐力多少百分比";
	public static String text_3273 = "增加耐力";
	public static String text_3274 = "减血";
	public static String text_3275 = "点生命值";
	public static String text_3276 = "降低智力";
	public static String text_3277 = "降低智力多少百分比";
	public static String text_3278 = "降低智力值";
	public static String text_3279 = "增加智力值";
	public static String text_3280 = "加血";
	public static String text_3281 = "每半秒钟恢复血多少点";
	public static String text_3282 = "每半秒钟恢复生命值";
	public static String text_3283 = "每半秒钟恢复生命值一定数量的点";
	public static String text_3284 = "每5秒钟恢复血多少点";
	public static String text_3285 = "加血和蓝";
	public static String text_3286 = "每半秒钟恢复血和蓝各多少点";
	public static String text_3287 = "%生命值";
	public static String text_3288 = "血上限同时增加相应生命值";
	public static String text_3289 = "血上限增加点数，生命值增加点数";
	public static String text_3290 = "增加生命值上限";
	public static String text_3291 = "%，同时增加相应的生命值";
	public static String text_3292 = "降低生命值上限";
	public static String text_3293 = "增加生命值上限百分比及生命值点数";
	public static String text_3294 = "内法攻击强度相关的加血";
	public static String text_3295 = "加血，每";
	public static String text_3296 = "秒增加生命值，与内法攻击强度相关";
	public static String text_3297 = "点，增加生命值";
	public static String text_3298 = "增加生命值上限及生命值点数";
	public static String text_3299 = "加血蓝";
	public static String text_3300 = "，增加";
	public static String 增加2 = "增加:";
	public static String text_3301 = "点法力值";
	public static String text_3302 = "血上限";
	public static String text_3303 = "增加生命值上限百分比";
	public static String text_3304 = "物理攻击强度相关的加血";
	public static String text_3305 = "秒增加生命值，与外功攻击强度相关";
	public static String text_3306 = "增加经验";
	public static String text_3307 = "增加经验多少百分比";
	public static String text_3308 = "降低经验";
	public static String text_3309 = "蓝上限增加点数";
	public static String text_3310 = "立即使目标身上的某一种毒发作，瞬间产生这种毒的所有伤害";
	public static String text_3311 = "立即使目标身上的某一种毒发作，使目标受到这种毒的所有伤害。该毒必须为该施法者施加的";
	public static String text_3312 = "增加或减少力量多少百分比";
	public static String text_3313 = "降低力量";
	public static String text_3314 = "增加力量值";
	public static String text_3315 = "王者";
	public static String text_3316 = "增加或减少力量，敏捷，智力，耐力多少点速度多少百分比";
	public static String text_3317 = "增加速度";
	public static String text_3318 = "降低速度";
	public static String text_3319 = "对眩晕，中毒，虚弱，降速，迟钝，减攻等负面状态进行免疫和驱散";
	public static String text_3320 = "对眩晕，中毒，虚弱等负面状态进行免疫和驱散";
	public static String text_3321 = "增加命中率多少百分比";
	public static String text_3322 = "命中和暴击";
	public static String text_3323 = "增加命中和暴击多少百分比";
	public static String text_3324 = "增加命中";
	public static String text_3325 = "增加命中和暴击多少值";
	public static String text_3326 = "命中值";
	public static String text_3327 = "增加命中值";
	public static String text_3328 = "提高或降低敏捷百分比";
	public static String text_3329 = "增加物理防御和法术防御各多少百分点";
	public static String text_3330 = "增加物理防御";
	public static String text_3331 = "增加法术防御";
	public static String text_3332 = "驱散";
	public static String text_3333 = "驱散不利状态";
	public static String text_3334 = "驱散一个定省效果";
	public static String text_3335 = "驱散一个定身效果";
	public static String text_3336 = "驱散一个减速效果";
	public static String text_3337 = "驱散眩晕效果";
	public static String text_3338 = "驱散虚弱效果";
	public static String text_3339 = "驱散一个中毒效果";
	public static String text_3340 = "增加或降低闪避率多少百分比";
	public static String text_3343 = "闪避和暴击值";
	public static String text_3344 = "增加或降低闪避和暴击值";
	public static String text_3345 = "闪避值";
	public static String text_3346 = "增加闪避值";
	public static String text_3347 = "瞬间回满血";
	public static String text_3348 = "瞬间回满生命";
	public static String text_3349 = "瞬间加蓝";
	public static String text_3350 = "增加";
	public static String text_3351 = "瞬间减血";
	public static String text_3352 = "减少";
	public static String text_3353 = "瞬间加血";
	public static String text_3354 = "瞬间加血蓝";
	public static String text_3355 = "，减少";
	public static String text_3356 = "增加速度减少伤害";
	public static String text_3357 = "提高速度减伤多少百分比";
	public static String text_3358 = "增加整体输出伤害比例";
	public static String text_3359 = "增加或减少耐力多少百分比";
	public static String text_3360 = "增加或减少力量，敏捷，智力，耐力多少百分比";
	public static String text_3361 = "无敌";
	public static String text_3362 = "无敌，免疫一切攻击";
	public static String text_3363 = "免疫一切攻击，但自身攻击下降50%";
	public static String text_3364 = "眩晕";
	public static String text_3365 = "不能移动，不能使用攻击技能";
	public static String text_3366 = "增加多种属性";
	public static String text_3367 = "增加血物理攻击法术攻击物理防御法术防御多少百分比";
	public static String text_3368 = "增加你";
	public static String text_3369 = "%的生命值上限，";
	public static String text_3370 = "%的所有技能伤害";
	public static String text_3371 = "降低你";
	public static String text_3372 = "增加你的生命值上限和所有技能伤害";
	public static String text_3373 = "根据剩余血量百分比触发另一个buff";
	public static String text_3374 = "当血量小于";
	public static String text_3375 = "%时触发相应级别的\"";
	public static String text_3376 = "\"效果，持续";
	public static String text_3377 = "秒，同时";
	public static String text_3378 = "消失。";
	public static String text_3379 = "血上限增加点数";
	public static String text_3380 = "虚弱";
	public static String text_3381 = "降低物理防御和法术防御各多少百分点";
	public static String text_3382 = "降低物理防御";
	public static String text_3383 = "降低法术防御";
	public static String text_3384 = "一滴血";
	public static String text_3385 = "生命值变为1点";
	public static String text_3386 = "提高外功攻击强度和内法攻击强度百分比";
	public static String text_3387 = "提高速度多少百分比";
	public static String text_3388 = "增加或减少智力多少百分比";
	public static String text_3389 = "中毒";
	public static String text_3390 = "中毒，减少多少点血";
	public static String text_3391 = "中毒，每";
	public static String text_3392 = "法术攻击强度相关的中毒";
	public static String text_3393 = "秒减少生命值，与内法攻击强度相关";
	public static String text_3394 = "物理攻击强度相关的中毒";
	public static String text_3395 = "秒减少生命值，与外功攻击强度相关";
	public static String text_3396 = "中毒，减少生命值，与内法攻击强度相关";
	public static String text_3397 = "中毒，减少生命值，与外功攻击强度相关";
	public static String text_3398 = "产生一个护盾，吸收";
	public static String text_3399 = "且每5秒钟恢复生命值";
	public static String text_3400 = "且增加闪避";
	public static String text_3401 = "但降低闪避";
	public static String text_3402 = "并且增加移动速度";
	public static String text_3403 = "但降低移动速度";
	public static String text_3404 = "峨眉";
	public static String text_3405 = "少林寺";
	public static String text_3406 = "武当派";
	public static String text_3407 = "光明顶";
	public static String text_3408 = "护甲";
	public static String text_3409 = "躲避等级";
	public static String text_3410 = "韧性等级";
	public static String text_3411 = "生命上限";
	public static String text_3412 = "仙法上限";
	public static String text_3413 = "5秒回生命速度";
	public static String text_3414 = "5秒回仙法速度";
	public static String text_3415 = "半秒回生命速度";
	public static String text_3416 = "半秒回仙法速度";
	public static String text_3417 = "返伤";
	public static String text_3418 = "技能CD";
	public static String 闪避率 = "闪避率";
	public static String 暴击率 = "暴击率";
	public static String 命中率 = "命中率";
	public static String 半秒回血 = "半秒回血";
	public static String 五秒回血 = "五秒回血";
	public static String 五秒回血千分比 = "五秒回血千分比";
	public static String 百分比吸血 = "百分比吸血";
	public static String text_3419 = "返生命值";
	public static String text_3420 = "返法力值";
	public static String text_3421 = "幸运";
	public static String text_3422 = "0秒";
	public static String text_3425 = "触发任务道具";
	public static String text_3427 = "千里追风符";
	public static String text_3439 = "激活后可以使用";
	public static String text_3461 = "\n剩余使用时间:";
	public static String text_3462 = "\n物品已失效";
	public static String text_3463 = "\n激活后可以使用";
	public static String text_3464 = "\n已绑定";
	public static String text_3465 = "\n拾取绑定";
	public static String text_3469 = "怪物掉落";
	public static String text_3470 = "商店买卖";
	public static String text_3471 = "任务奖励";
	public static String text_3472 = "玩家制造";
	public static String text_3473 = "任务初始化";
	public static String text_3474 = "出生给予";
	public static String text_3475 = "采集";
	public static String text_3476 = "其他原因";
	public static String text_3477 = "NPC给予";
	public static String text_3478 = "领取当天礼包";
	public static String text_3479 = "领取级别礼包";
	public static String text_3480 = "活动奖励";
	public static String text_3481 = "配方合成信息用到";
	public static String text_3482 = "领取在线礼包";
	public static String text_3483 = "结婚";
	public static String text_3484 = "使用宝盒";
	public static String text_3485 = "GM发送";
	public static String text_3486 = "徒弟出师";
	public static String text_3487 = "补偿玩家消费40级公测礼包";
	public static String text_3488 = "合成物品";
	public static String text_3489 = "采集种子NPC";
	public static String text_3490 = "金翎奖";
	public static String text_3491 = "中秋礼品";
	public static String text_3492 = "跨服创建";
	public static String text_3493 = "合服导入";
	public static String text_3494 = "仙武大会报名纪念";
	public static String text_3495 = "比武奖励";
	public static String text_3496 = "回归奖励";
	public static String text_3497 = "怪物消失";
	public static String text_3498 = "任务奖励消除";
	public static String text_3499 = "玩家丢弃";
	public static String text_3500 = "物品合成";
	public static String text_3501 = "使用消耗";
	public static String text_3502 = "复活";
	public static String text_3503 = "使用千里追风";
	public static String text_3504 = "重置打孔次数";
	public static String text_3505 = "使用友好度道具";
	public static String text_3506 = "任务收取";
	public static String text_3507 = "挖宝石";
	public static String text_3508 = "合成灵符";
	public static String text_3516 = "矿石";
	public static String text_3517 = "木料";
	public static String text_3518 = "奇珍";
	public static String text_3519 = "珠宝";
	public static String text_3520 = "玉石";
	public static String text_3552 = "战斗状态下，不能洗点！";
	public static String text_3553 = "洗点成功，您需要重新分配技能点才能使用已设置的技能快捷键！";
	public static String text_3559 = "无法得到玩家地图";
	public static String text_3564 = "的人数已满，请稍后再试";
	public static String text_3570 = "您的级别不够，[";
	public static String text_3571 = "]需要等级";
	public static String text_3579 = "]拾取绑定[/color]";
	public static String text_3693 = "请选择要增加友好度的人";
	public static String text_3694 = "增加友好度";
	public static String text_3695 = "您通过掷骰子获得了/-1";
	public static String text_3696 = "通过掷骰子获得了/-1";
	public static String text_3697 = "您通过掷骰子赢得了/-1,您的背包已满，请先清空一些格子后再拾取";
	public static String text_3698 = "通过掷骰子赢得了/-1";
	public static String text_3699 = "所有人都放弃了/-1,你可以自由拾取了！";
	public static String text_3700 = "您放弃了/-1";
	public static String text_3701 = "放弃了/-1";
	public static String text_3702 = "您对/-1的需求点数为";
	public static String text_3703 = "对/-1的需求点数为";
	public static String text_3704 = "点数:";
	public static String text_3705 = "您对/-1的贪婪点数为";
	public static String text_3706 = "对/-1的贪婪点数为";
	public static String text_3707 = "玩家：";
	public static String text_3708 = "钱：";
	public static String text_3709 = "共享物品：";
	public static String text_3710 = "(已捡)";
	public static String text_3711 = "(未捡)";
	public static String text_3712 = "私有物品：";
	public static String text_3713 = "可以合成出下列物品的一件:\n";
	public static String text_3714 = "白";
	public static String text_3715 = "绿";
	public static String text_37151 = "蓝";
	public static String text_3716 = "紫";
	public static String text_3717 = "橙";
	public static String text_3724 = "周围没有作用对象，不需要使用";
	public static String text_3725 = "对您使用了";
	public static String text_3726 = "使用道具失败，BUFF[";
	public static String text_3727 = "]不存在，请联系管理员";
	public static String 限制地图使用 = "此物品限制使用地图，请到指定地图内使用.";
	public static String text_3728 = "已经存在一个更强的效果";
	public static String text_3729 = "使用后以自己为中心半径";
	public static String text_3730 = "范围内的怪物将:\n";
	public static String text_3731 = "范围内的敌对NPC将:\n";
	public static String text_3732 = "范围内的友方NPC将:\n";
	public static String text_3733 = "范围内的全部NPC将:\n";
	public static String text_3734 = "范围内的敌对玩家将:\n";
	public static String text_3735 = "范围内的友方玩家包括玩家自己将:\n";
	public static String text_3736 = "范围内的所有玩家将:\n";
	public static String text_3737 = "范围内的所有生物将:\n";
	public static String text_3738 = "，持续";
	public static String text_3739 = "您没有离线时间不能使用此道具";
	public static String text_3741 = "]使用绑定[/color]";
	public static String text_3743 = "]需要等级：";
	public static String text_3744 = "\n在非战斗状态下使用";
	public static String text_3745 = "]在";
	public static String text_3746 = "内只能使用一次[/color]";
	public static String text_3752 = "]已经到期，不能使用";
	public static String text_3753 = "您处于战场中，不能使用非战场道具[";
	public static String text_3754 = "您没有处于战场中，不能使用战场道具[";
	public static String text_3755 = "您处于战斗状态，不能使用[";
	public static String text_3756 = "您未到达使用[";
	public static String text_3757 = "]的地图区域，此物品需要到[";
	public static String text_3758 = "]地图的(";
	public static String text_3759 = ")坐标附近使用";
	public static String text_3760 = "此地图禁止骑坐骑.";
	public static String text_3761 = "此地图禁止飞行.";
	public static String text_3766 = "元宝。";
	public static String text_3775 = "未定义";
	public static String text_3776 = "食品";
	public static String text_3777 = "加血药品";
	public static String text_3778 = "加蓝药品";
	public static String text_3779 = "加血和蓝药品";
	public static String text_3780 = "传送符";
	public static String text_3781 = "坐骑";
	public static String text_3782 = "复活道具";
	public static String text_3783 = "洗点";
	public static String text_3784 = "背包扩展";
	public static String text_3785 = "离线经验";
	public static String text_3786 = "宝箱";
	public static String text_3787 = "随机宝箱";
	public static String text_3788 = "材料";
	public static String text_3789 = "配方";
	public static String text_3806 = "已经被采摘了";
	public static String text_3807 = "金疮药";
	public static String text_3808 = "您目前等级为";
	public static String text_3809 = "级，不能采摘30级以下的人种植的植物";
	public static String text_3810 = "级，不能采摘31级以上的人种植的植物";
	public static String text_3811 = "还没成熟呢";
	public static String text_3812 = "恭喜您获得了/-1";
	public static String text_3813 = "您栽培的植物已经被(";
	public static String text_3814 = ")采摘了";
	public static String text_3815 = ")采摘了，快去摘他/她的果实吧。";
	public static String text_3816 = "背包已满请清理后再采";
	public static String text_3817 = "你还不能采摘";
	public static String text_3818 = "栽培的植物";
	public static String text_3819 = "目前不能种植植物";
	public static String text_3820 = "]的区域，此物品需要到[";
	public static String text_3821 = "]区域中使用";
	public static String text_3822 = "]的地图，此物品需要到[";
	public static String text_3823 = "]地图中使用";
	public static String text_3824 = "您已经种植了植物，请收获后再种植";
	public static String text_3825 = "附近已经有人种植了植物，请换一个位置";
	public static String text_3826 = "您没有在地图中，不能使用";
	public static String text_3847 = "无法得到玩家复活点地图";
	public static String text_3848 = "您已经到最高级了，不能再使用";
	public static String text_3849 = "了";
	public static String text_3850 = "】装备绑定，你确定要装备吗？";
	public static String text_3851 = "您现在在比武赛场中，不能使用道具";
	public static String text_3852 = "你拥有";
	public static String text_3853 = "小时离线时间，【";
	public static String text_3854 = "】可以把";
	public static String text_3855 = "小时离线时间转化成为";
	public static String text_3856 = "经验，需要转化吗？";
	public static String text_3858 = "】使用绑定，你确定要使用吗？";
	public static String text_3859 = "续费失败，您没有";
	public static String text_3860 = "续费成功，扣除您";
	public static String text_3861 = "】续费需要";
	public static String text_3862 = "元宝，您确定续费吗？";
	public static String text_3865 = "攻击";
	public static String text_3866 = "冷却";
	public static String text_3867 = "结束";
	public static String text_3868 = "提高你所有技能的暴击率";
	public static String text_3869 = "使你所有技能的暴击率增加";
	public static String text_3870 = "提高你的闪避率";
	public static String text_3871 = "使你的闪避率增加";
	public static String text_3872 = "使你的法术防御提高一定比率";
	public static String text_3874 = "使你的法术防御提高一定值\n";
	public static String text_3875 = "使你的的内法伤害减伤比例增加";
	public static String text_3876 = "使你的物理防御提高一定比率\n";
	public static String text_3877 = "使你的外功伤害减伤比例增加";
	public static String text_3878 = "使你的耐力提高一定比率\n";
	public static String text_3879 = "使你的耐力值增加";
	public static String text_3882 = "使你的反伤提高一定比率";
	public static String text_3883 = "使你的反伤增加";
	public static String text_3884 = "使你的生命值上限和法力值上限提高一定比率";
	public static String text_3885 = "使你的生命值上限提高";
	public static String text_3886 = "%，法力值上限提高";
	public static String text_3887 = "使你的生命值基础恢复速度和法力值基础恢复速度提高一定数值";
	public static String text_3888 = "使你的生命值基础恢复速度提高";
	public static String text_3889 = "点，法力值基础恢复速度提高";
	public static String text_3890 = "使你的生命值基础恢复速度提高一定数值";
	public static String text_3891 = "使你的力量提高一定比率";
	public static String text_3892 = "使你的力量增加";
	public static String text_3893 = "使你的所有基本属性增加一定百分比";
	public static String text_3894 = "使你的所有基本属性增加";
	public static String text_3895 = "使你的命中和暴击等级";
	public static String text_3896 = "使你的命中率增加";
	public static String text_3897 = "%，暴击率增加";
	public static String text_3898 = "使你的命中等级提高一定数值";
	public static String text_3899 = "使你的敏捷提高一定比率";
	public static String text_3900 = "使你的敏捷增加";
	public static String text_3901 = "使你的移动速度增加一定百分比";
	public static String text_3902 = "使你的移动速度增加";
	public static String text_3903 = "使你的法力值上限提高";
	public static String text_3904 = "使你的法力值基础恢复速度提高一定数值";
	public static String text_3905 = "使你的法力值基础恢复速度提高";
	public static String text_3906 = "使你的法术防御值百分比提高一定比率";
	public static String text_3907 = "使你的法术防御值百分比增加";
	public static String text_3908 = "使你的耐力提高一定比率";
	public static String text_3909 = "使你的耐力增加";
	public static String text_3910 = "使你的法术防御和物理防御提高一定比率";
	public static String text_3911 = "使你的外功伤害减伤百分比增加";
	public static String text_3912 = "%，内法伤害减伤百分比增加";
	public static String text_3913 = "使你的物理防御值百分比提高一定比率";
	public static String text_3914 = "使你的物理防御值百分比增加";
	public static String text_3916 = "使你的外功伤害和内法伤害提高一定比率";
	public static String text_3917 = "使你的外功伤害上限提高";
	public static String text_3918 = " % 外功伤害下限限提高 ";
	public static String text_3919 = "%，内法伤害上限提高";
	public static String text_3920 = "% ，内法伤害下限提高";
	public static String text_3923 = "使你的智力提高一定比率";
	public static String text_3924 = "使你的智力提高";
	public static String text_3925 = "法力值不够,";
	public static String text_3926 = "目标不存在,";
	public static String text_3927 = "战斗状态中不能使用";
	public static String text_3928 = "检查通过";
	public static String text_3941 = "光环技能，瞬发\n";
	public static String text_3942 = "未识别的技能类型：";
	public static String text_3947 = "交易中心代理线程";
	public static String text_3952 = "达成交易时发生错误";
	public static String text_3953 = "取消了交易";
	public static String text_3954 = "我叫刘勇";
	public static String text_3955 = "我是大连人";
	public static String text_3956 = "公司叫蜂鸟科技";
	public static String text_3957 = "公司总部在北京";
	public static String text_3958 = "请选择下列选项：";
	public static String text_3959 = "个人介绍";
	public static String text_3960 = "公司介绍";
	public static String text_3961 = "退出对话";
	public static String text_3962 = "可自己重置";
	public static String text_3963 = "可系统重置";
	public static String text_3964 = "您已不满足进入此副本的条件，系统将在10秒后将你传送至你的复活点！";
	public static String text_3965 = "玩家已经不在队伍中";
	public static String text_3967 = "副本将在10秒钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_3968 = "副本将在30秒钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_3969 = "副本将在1分钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_3970 = "副本将在2分钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_3971 = "副本将在3分钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_3972 = "副本将在5分钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_3973 = "副本将在10分钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_3974 = "副本将在15分钟后关闭，所有玩家将被传送至各自的复活点！";
	public static String text_3975 = "时空幻境（勇士）";
	public static String text_3977 = "天鹰演武场（勇士）";
	public static String text_3979 = "北海极地（勇士）";
	public static String text_3985 = "副本心跳线程-";
	public static String text_3986 = "副本检查线程";
	public static String text_3987 = "华严寺";
	public static String text_3988 = "佛光普照，寺内恢复一片祥和。";
	public static String text_3989 = "华严寺（勇士）";
	public static String text_3990 = "华严寺exp";
	public static String text_3991 = "血妖塔";
	public static String text_3992 = "妖物被击退，塔内恢复平静。";
	public static String text_3993 = "血妖塔（勇士）";
	public static String text_3994 = "血妖塔exp";
	public static String text_3995 = "元军机密营地";
	public static String text_3996 = "元军机密已经到手，速速离开。";
	public static String text_3997 = "元军机密营地（勇士）";
	public static String text_3998 = "元军机密营地exp";
	public static String text_3999 = "雁荡山剿匪";
	public static String text_4000 = "匪徒被剿灭，雁荡山已经安全。";
	public static String text_4001 = "雁荡山剿匪（勇士）";
	public static String text_4002 = "雁荡山剿匪exp";
	public static String text_4003 = "幽谷死域";
	public static String text_4004 = "死域入口封闭，幽谷即将弥漫滔天雾气。";
	public static String text_4005 = "幽谷死域（勇士）";
	public static String text_4006 = "幽谷死域exp";
	public static String text_4007 = "上古龙墓";
	public static String text_4008 = "神龙即将觉醒，速退。";
	public static String text_4009 = "上古龙墓（勇士）";
	public static String text_4010 = "上古龙墓exp";
	public static String text_4011 = "天鹰演武场";
	public static String text_4012 = "武林之巅，武场已无停留必要。";
	public static String text_4013 = "天鹰演武场exp";
	public static String text_4014 = "北海极地";
	public static String text_4015 = "冰封万里，极光四射，天地间安静异常。";
	public static String text_4016 = "时空幻境";
	public static String text_4017 = "幻境入口打开，快穿越时空回去。";
	public static String text_4018 = "风水禁地";
	public static String text_4019 = "风水已经改变，禁地即将变得危险异常，快离开。";
	public static String text_4020 = "风水禁地（勇士）";
	public static String text_4021 = "德宁死牢";
	public static String text_4022 = "人已救出，敌人大军就快来了，快走。";
	public static String text_4023 = "德宁死牢（勇士）";
	public static String text_4024 = "德宁死狱";
	public static String text_4025 = "修罗族退回，封印两界入口，此地不必再多担心了。";
	public static String text_4026 = "德宁死狱（勇士）";
	public static String text_4027 = "迷之山谷";
	public static String text_4028 = "山谷中阳光普照，没了那些弥漫的雾气。";
	public static String text_4029 = "迷之山谷（勇士）";
	public static String text_4030 = "迷之山脉";
	public static String text_4031 = "山脉如龙，如不速退，将永远迷失在山脉之中。";
	public static String text_4032 = "迷之山脉（勇士）";
	public static String text_4033 = "反抗军营地";
	public static String text_4034 = "元军先锋队被歼灭，去下个地点准备元军下次冲击。";
	public static String text_4035 = "反抗军营地（勇士）";
	public static String text_4036 = "楼兰地宫";
	public static String text_4037 = "楼兰浮在云端，如不走将会被卷入时空乱流。";
	public static String text_4038 = "楼兰地宫（勇士）";
	public static String text_4039 = "神皇密道";
	public static String text_4040 = "密道里什么都没有了，留在这里只能被阴气袭击。";
	public static String text_4041 = "神皇密道（勇士）";
	public static String text_4042 = "查忘村坟场";
	public static String text_4043 = "超度了一切，这人间仙境中的魂灵们已经安息了。";
	public static String text_4044 = "查忘村坟场（勇士）";
	public static String text_4055 = "不接受";
	public static String text_4064 = "副本进度记录";
	public static String text_4113 = "师徒积分";
	public static String text_4114 = "帮派积分";
	public static String text_4115 = "充值积分";
	public static String text_4116 = "商店购买";
	public static String text_4118 = "玄晶合成";
	public static String text_4119 = "治疗";
	public static String text_4120 = "元宝交易所冻结";
	public static String text_4121 = "邮件转账";
	public static String text_4122 = "工会创建";
	public static String text_4123 = "付费提取邮件";
	public static String text_4124 = "配方合成";
	public static String text_4125 = "捐献帮派资金";
	public static String text_4126 = "扩充仓库";
	public static String text_4127 = "帮战投标";
	public static String text_4128 = "购买帮战物品";
	public static String text_4129 = "邮件创建失败返还金币";
	public static String text_4130 = "装备重置";
	public static String text_4131 = "帮战报名费用";
	public static String text_4132 = "创建或升级家园";
	public static String text_4133 = "帮会仓库升级";
	public static String text_4134 = "帮主给帮众加BUFF";
	public static String text_4135 = "充值积分转账";
	public static String text_4136 = "报名费";
	public static String text_4137 = "观看比赛";
	public static String text_4138 = "法宝重融合";
	public static String text_4139 = "出售物品";
	public static String text_4140 = "邮件获取";
	public static String text_4141 = "交易获取";
	public static String text_4142 = "拍卖获取";
	public static String text_4143 = "使用道具";
	public static String text_4144 = "金币交易所提取";
	public static String text_4145 = "收费邮件";
	public static String text_4146 = "增值平台充值";
	public static String text_4147 = "银行卡充值";
	public static String text_4148 = "每周工资";
	public static String text_4149 = "游戏点数充值";
	public static String text_4150 = "补偿玩家";
	public static String text_4151 = "战场奖励";
	public static String text_4152 = "问答奖励";
	public static String text_4153 = "回归角色奖励";
	public static String text_4154 = "徒弟充值";
	public static String text_4155 = "徒弟升级";
	public static String text_4156 = "徒弟拾取";
	public static String text_4157 = "使用荣誉果";
	public static String text_4158 = "帮派战场奖励";
	public static String text_4159 = "充值返还-时间规则";
	public static String text_4160 = "充值返还-升级规则";
	public static String text_4161 = "玩家充值积分";
	public static String text_4162 = "比武获得";
	public static String text_4213 = ",队列中无消息";
	public static String text_4214 = "[发送数据包出错] [不可能发生的异常] ";
	public static String text_4215 = "进入玩家";
	public static String text_4216 = "进入精灵";
	public static String text_4217 = "离开玩家";
	public static String text_4218 = "离开精灵";
	public static String text_4220 = "世界频道";
	public static String text_4223 = "GM提示";
	public static String text_4224 = "[发送弹框提示][";
	public static String text_4225 = "弹框成功！";
	public static String text_4226 = "弹框失败！";
	public static String text_4227 = "  于：【";
	public static String text_4228 = "禁言操作";
	public static String text_4229 = "潜龙称号列表.xls";
	public static String text_4230 = "被替换";
	public static String text_4231 = "到期";
	public static String text_4232 = "不符合条件";
	public static String text_4243 = "未知原因！";
	public static String text_4244 = "天兵神器";
	public static String text_4245 = "新科状元";
	public static String text_4276 = "光环技能";
	public static String text_4277 = "技能的图标图片路径";
	public static String text_4278 = "图标图片路径";
	public static String text_4279 = "技能的名称";
	public static String text_4280 = "技能描述信息";
	public static String text_4281 = "技能的描述信息";
	public static String text_4282 = "技能的最大级别";
	public static String text_4283 = "1级";
	public static String text_4284 = "2级";
	public static String text_4285 = "3级";
	public static String text_4286 = "4级";
	public static String text_4287 = "5级";
	public static String text_4288 = "作用范围，以自身为中心的一个矩形 矩形的宽度";
	public static String text_4289 = "矩形的宽度";
	public static String text_4290 = "作用范围，以自身为中心的一个矩形 矩形的高度";
	public static String text_4291 = "矩形的高度";
	public static String text_4292 = "范围的类型： 0 范围内的所有的队友，包括自己 1 范围内的所有的队友，中立方，和自己 2 范围内的所有的中立方 3 范围内的所有的敌方 4 范围内的所有的中立方，敌方";
	public static String text_4293 = "范围的类型";
	public static String text_4294 = "范围内的所有的队友，包括自己";
	public static String text_4295 = "范围内的所有的队友，中立方，和自己";
	public static String text_4296 = "范围内的所有的中立方";
	public static String text_4297 = "范围内的所有的敌方";
	public static String text_4298 = "范围内的所有的中立方，敌方";
	public static String text_4299 = "光环类型，此数值表示玩家脚下的光环 此值与Mapping编辑器中的下标对应，从0开始";
	public static String text_4300 = "光环类型";
	public static String text_4301 = "使用的Buff的名称";
	public static String text_4302 = "需要的职业发展线路点数，这是学习此技能的条件";
	public static String text_4303 = "职业发展线路点数";
	public static String text_4304 = "0点";
	public static String text_4305 = "1点";
	public static String text_4306 = "2点";
	public static String text_4307 = "3点";
	public static String text_4308 = "4点";
	public static String text_4309 = "5点";
	public static String text_4310 = "6点";
	public static String text_4311 = "7点";
	public static String text_4312 = "8点";
	public static String text_4313 = "9点";
	public static String text_4314 = "10点";
	public static String text_4315 = "11点";
	public static String text_4316 = "12点";
	public static String text_4317 = "13点";
	public static String text_4318 = "14点";
	public static String text_4319 = "15点";
	public static String text_4320 = "16点";
	public static String text_4321 = "17点";
	public static String text_4322 = "18点";
	public static String text_4323 = "19点";
	public static String text_4324 = "20点";
	public static String text_4325 = "21点";
	public static String text_4326 = "22点";
	public static String text_4327 = "23点";
	public static String text_4328 = "24点";
	public static String text_4329 = "25点";
	public static String text_4330 = "26点";
	public static String text_4331 = "27点";
	public static String text_4332 = "28点";
	public static String text_4333 = "29点";
	public static String text_4334 = "30点";
	public static String text_4335 = "31点";
	public static String text_4336 = "32点";
	public static String text_4337 = "33点";
	public static String text_4338 = "34点";
	public static String text_4339 = "35点";
	public static String text_4340 = "36点";
	public static String text_4341 = "37点";
	public static String text_4342 = "38点";
	public static String text_4343 = "39点";
	public static String text_4344 = "40点";
	public static String text_4345 = "41点";
	public static String text_4346 = "42点";
	public static String text_4347 = "43点";
	public static String text_4348 = "44点";
	public static String text_4349 = "45点";
	public static String text_4350 = "46点";
	public static String text_4351 = "47点";
	public static String text_4352 = "48点";
	public static String text_4353 = "49点";
	public static String text_4354 = "50点";
	public static String text_4355 = "51点";
	public static String text_4356 = "52点";
	public static String text_4357 = "53点";
	public static String text_4358 = "54点";
	public static String text_4359 = "55点";
	public static String text_4360 = "56点";
	public static String text_4361 = "57点";
	public static String text_4362 = "58点";
	public static String text_4363 = "59点";
	public static String text_4364 = "60点";
	public static String text_4365 = "61点";
	public static String text_4366 = "62点";
	public static String text_4367 = "63点";
	public static String text_4368 = "64点";
	public static String text_4369 = "65点";
	public static String text_4370 = "66点";
	public static String text_4371 = "67点";
	public static String text_4372 = "68点";
	public static String text_4373 = "69点";
	public static String text_4374 = "70点";
	public static String text_4375 = "71点";
	public static String text_4376 = "72点";
	public static String text_4377 = "73点";
	public static String text_4378 = "74点";
	public static String text_4379 = "75点";
	public static String text_4380 = "76点";
	public static String text_4381 = "77点";
	public static String text_4382 = "78点";
	public static String text_4383 = "79点";
	public static String text_4384 = "80点";
	public static String text_4385 = "81点";
	public static String text_4386 = "82点";
	public static String text_4387 = "83点";
	public static String text_4388 = "84点";
	public static String text_4389 = "85点";
	public static String text_4390 = "86点";
	public static String text_4391 = "87点";
	public static String text_4392 = "88点";
	public static String text_4393 = "89点";
	public static String text_4394 = "90点";
	public static String text_4395 = "91点";
	public static String text_4396 = "92点";
	public static String text_4397 = "93点";
	public static String text_4398 = "94点";
	public static String text_4399 = "95点";
	public static String text_4400 = "96点";
	public static String text_4401 = "97点";
	public static String text_4402 = "98点";
	public static String text_4403 = "99点";
	public static String text_4404 = "100点";
	public static String text_4405 = "每一个级别的技能，通过buffName确定要使用的Buff后，指定这个buff的级别，用于表示buff的威力";
	public static String text_4406 = "BUFF级别";
	public static String text_4407 = "被动技能修改主动技能";
	public static String text_4408 = "每个级别的描述";
	public static String text_4409 = "增加力量的百分比";
	public static String text_4410 = "增加敏捷百分比";
	public static String text_4411 = "增加耐力百分比";
	public static String text_4412 = "增加智力";
	public static String text_4413 = "增加智力百分比";
	public static String text_4414 = "会心一击百分比";
	public static String text_4415 = "增加躲避";
	public static String text_4416 = "躲避百分比";
	public static String text_4417 = "增加法防";
	public static String text_4418 = "法防百分比";
	public static String text_4419 = "增加法攻";
	public static String text_4420 = "道术攻击伤害力的上限百分比";
	public static String text_4421 = "道术攻击伤害力的下限百分比";
	public static String text_4422 = "增加生命和法力";
	public static String text_4423 = "角色的最大生命值百分比";
	public static String text_4424 = "角色的最大魔法值百分比";
	public static String text_4425 = "增加生命和法力恢复";
	public static String text_4426 = "生命力恢复速度，每五秒恢复一次";
	public static String text_4427 = "生命力恢复速度";
	public static String text_4428 = "魔法恢复速度，每五秒恢复一次";
	public static String text_4429 = "魔法恢复速度";
	public static String text_4430 = "增加生命恢复";
	public static String text_4431 = "增加命中和躲避";
	public static String text_4432 = "命中百分比";
	public static String text_4433 = "增加法力最大值";
	public static String text_4434 = "增加法力恢复速度";
	public static String text_4435 = "增加物防和法防";
	public static String text_4436 = "物理防御百分比 ";
	public static String text_4437 = "法防百分比 ";
	public static String text_4438 = "增加物防";
	public static String text_4439 = "增加物攻和法攻";
	public static String text_4440 = "物理攻击上限百分比 ";
	public static String text_4441 = "物理攻击下限百分比 ";
	public static String text_4442 = "道术攻击伤害力的上限百分比 ";
	public static String text_4443 = "道术攻击伤害力的下限百分比 ";
	public static String text_4444 = "增加物攻";
	public static String text_4445 = "无后效的平砍技能(不推荐使用)";
	public static String text_4446 = "普通攻击的有效距离";
	public static String text_4447 = "施法时间，在这段时间内，播放僵直动画，时间为毫秒，此时间为0时，不会播放僵直动画";
	public static String text_4448 = "施法时间";
	public static String text_4449 = "0.5秒";
	public static String text_4450 = "0.55秒";
	public static String text_4451 = "0.6秒";
	public static String text_4452 = "0.65秒";
	public static String text_4453 = "0.7秒";
	public static String text_4454 = "0.75秒";
	public static String text_4455 = "0.8秒";
	public static String text_4456 = "0.85秒";
	public static String text_4457 = "0.9秒";
	public static String text_4458 = "1秒";
	public static String text_4459 = "1.2秒";
	public static String text_4460 = "1.5秒";
	public static String text_4461 = "2秒";
	public static String text_4462 = "2.5秒";
	public static String text_4463 = "2.7秒";
	public static String text_4464 = "3秒";
	public static String text_4465 = "3.5秒";
	public static String text_4466 = "4秒";
	public static String text_4467 = "5秒";
	public static String text_4468 = "8秒";
	public static String text_4469 = "10秒";
	public static String text_4470 = "攻击动画时间，这段时间内，播放攻击动画，时间为毫秒";
	public static String text_4471 = "攻击动画时间";
	public static String text_4472 = "100毫秒";
	public static String text_4473 = "200毫秒";
	public static String text_4474 = "300毫秒";
	public static String text_4475 = "400毫秒";
	public static String text_4476 = "500毫秒";
	public static String text_4477 = "600毫秒";
	public static String text_4478 = "700毫秒";
	public static String text_4479 = "800毫秒";
	public static String text_4480 = "900毫秒";
	public static String text_4481 = "1000毫秒";
	public static String text_4482 = "1100毫秒";
	public static String text_4483 = "1200毫秒";
	public static String text_4484 = "1300毫秒";
	public static String text_4485 = "1400毫秒";
	public static String text_4486 = "1500毫秒";
	public static String text_4487 = "1600毫秒";
	public static String text_4488 = "1700毫秒";
	public static String text_4489 = "1800毫秒";
	public static String text_4490 = "1900毫秒";
	public static String text_4491 = "2000毫秒";
	public static String text_4492 = "2100毫秒";
	public static String text_4493 = "2200毫秒";
	public static String text_4494 = "2300毫秒";
	public static String text_4495 = "2400毫秒";
	public static String text_4496 = "2500毫秒";
	public static String text_4497 = "2600毫秒";
	public static String text_4498 = "2700毫秒";
	public static String text_4499 = "2800毫秒";
	public static String text_4500 = "2900毫秒";
	public static String text_4501 = "3000毫秒";
	public static String text_4502 = "3100毫秒";
	public static String text_4503 = "3200毫秒";
	public static String text_4504 = "3300毫秒";
	public static String text_4505 = "3400毫秒";
	public static String text_4506 = "3500毫秒";
	public static String text_4507 = "3600毫秒";
	public static String text_4508 = "3700毫秒";
	public static String text_4509 = "3800毫秒";
	public static String text_4510 = "3900毫秒";
	public static String text_4511 = "4000毫秒";
	public static String text_4512 = "4100毫秒";
	public static String text_4513 = "4200毫秒";
	public static String text_4514 = "4300毫秒";
	public static String text_4515 = "4400毫秒";
	public static String text_4516 = "4500毫秒";
	public static String text_4517 = "4600毫秒";
	public static String text_4518 = "4700毫秒";
	public static String text_4519 = "4800毫秒";
	public static String text_4520 = "4900毫秒";
	public static String text_4521 = "5000毫秒";
	public static String text_4522 = "冷却时间，这段时间内，人物状态会回到站立的状态";
	public static String text_4523 = "冷却时间";
	public static String text_4524 = "6秒";
	public static String text_4525 = "7秒";
	public static String text_4526 = "9秒";
	public static String text_4527 = "11秒";
	public static String text_4528 = "12秒";
	public static String text_4529 = "13秒";
	public static String text_4530 = "14秒";
	public static String text_4531 = "15秒";
	public static String text_4532 = "16秒";
	public static String text_4533 = "17秒";
	public static String text_4534 = "18秒";
	public static String text_4535 = "19秒";
	public static String text_4536 = "20秒";
	public static String text_4537 = "21秒";
	public static String text_4538 = "22秒";
	public static String text_4539 = "23秒";
	public static String text_4540 = "24秒";
	public static String text_4541 = "25秒";
	public static String text_4542 = "26秒";
	public static String text_4543 = "27秒";
	public static String text_4544 = "28秒";
	public static String text_4545 = "29秒";
	public static String text_4546 = "30秒";
	public static String text_4547 = "31秒";
	public static String text_4548 = "32秒";
	public static String text_4549 = "33秒";
	public static String text_4550 = "34秒";
	public static String text_4551 = "35秒";
	public static String text_4552 = "36秒";
	public static String text_4553 = "37秒";
	public static String text_4554 = "38秒";
	public static String text_4555 = "39秒";
	public static String text_4556 = "40秒";
	public static String text_4557 = "41秒";
	public static String text_4558 = "42秒";
	public static String text_4559 = "43秒";
	public static String text_4560 = "44秒";
	public static String text_4561 = "45秒";
	public static String text_4562 = "46秒";
	public static String text_4563 = "47秒";
	public static String text_4564 = "48秒";
	public static String text_4565 = "49秒";
	public static String text_4566 = "50秒";
	public static String text_4567 = "僵直动画的风格";
	public static String text_4568 = "攻击动画的风格";
	public static String text_4569 = "一个或多个出招时间，跟等级无关，请填入以逗号分隔的值，例如 100,200,300";
	public static String text_4570 = "出招时间";
	public static String text_4571 = "是否有武器限制";
	public static String text_4572 = "没有限制";
	public static String text_4573 = "有限制";
	public static String text_4574 = "武器具体的限制是什么";
	public static String text_4575 = "武器限制";
	public static String text_4576 = "攻击类型，表示物理攻击还是法术攻击 0 表示物理攻击 1 表示法术攻击";
	public static String text_4577 = "攻击类型";
	public static String text_4578 = "物理攻击";
	public static String text_4579 = "法术攻击";
	public static String text_4580 = "该技能用完之后，是否用普通攻击 还是继续用此技能攻击，主要区分 物理系攻击和法系攻击";
	public static String text_4581 = "继续用普通攻击";
	public static String text_4582 = "buff作用的对象：0 表示作用在目标身上 1 表示作用在施法者身上， 此属性对各个级别的技能有效";
	public static String text_4583 = "BUFF作用对象";
	public static String text_4584 = "作用于目标";
	public static String text_4585 = "作用于施法者";
	public static String text_4586 = "每一个级别的技能，通过buffName确定要使用的Buff后，指定这个buff持续的时间";
	public static String text_4587 = "BUFF持续时间";
	public static String text_4588 = "每一个级别的技能，通过buffName确定要使用的Buff后，指定这个buff产生的概率";
	public static String text_4589 = "BUFF产生概率";
	public static String text_4590 = "技能的伤害，跟等级相关";
	public static String text_4591 = "技能的伤害";
	public static String text_4592 = "冲锋技能";
	public static String text_4593 = "路径类型，0表示直线，1表示寻路出来的路径";
	public static String text_4594 = "路径类型";
	public static String text_4595 = "直线";
	public static String text_4596 = "寻路路径";
	public static String text_4597 = "距离目标点多远停下来";
	public static String text_4598 = "消耗的法力值，跟等级相关";
	public static String text_4599 = "消耗的法力值";
	public static String text_4600 = "给目标只加BUFF的技能";
	public static String text_4601 = "范围的类型：0 范围内的所有的队友，包括自己 1 范围内的所有的队友，中立方，和自己 2 范围内的所有的中立方 3 范围内的所有的敌方 4 范围内的所有的中立方，敌方";
	public static String text_4602 = "作用于选择的目标，目标必须是队友或中立方，目标必须在范围内";
	public static String text_4603 = "作用于选择的目标，目标必须是敌方，目标必须在范围内";
	public static String text_4604 = "作用于自己，无视范围";
	public static String text_4605 = "以玩家自身为中心的，一个矩形，此参数为矩形的宽度";
	public static String text_4606 = "矩形宽度";
	public static String text_4607 = "以玩家自身为中心的，一个矩形，此参数为矩形的高度";
	public static String text_4608 = "矩形高度";
	public static String text_4609 = "后效的类型，比如闪电，落雷等";
	public static String text_4610 = "后效的类型";
	public static String text_4611 = "后效持续的时间，为毫秒";
	public static String text_4612 = "后效持续的时间";
	public static String text_4613 = "后效持续的时间过后，爆炸持续的时间";
	public static String text_4614 = "爆炸持续的时间";
	public static String text_4615 = "矩阵落雷类型攻击";
	public static String text_4616 = "后效分布的类型：0 以自身为中心的正方形排列 1 以自身为中心的菱形排列 2 以目标为中心的正方形排列 3 以目标为中心的菱形排列";
	public static String text_4617 = "后效分布的类型";
	public static String text_4618 = "自身为中心的正方形排列";
	public static String text_4619 = "以自身为中心的菱形排列";
	public static String text_4620 = "目标为中心的正方形排列";
	public static String text_4621 = "目标为中心的菱形排列";
	public static String text_4622 = "后效排列之间间距的宽度";
	public static String text_4623 = "后效排列之间间距的高度";
	public static String text_4624 = "后效的个数，越多后效排列的约紧密";
	public static String text_4625 = "后效的个数";
	public static String text_4626 = "毫秒";
	public static String text_4627 = "攻击的有效距离";
	public static String text_4628 = "范围落雷攻击";
	public static String text_4629 = "范围的类型： 0 以施法者自身为中心的矩形，不包括自己 1 正前方的矩形，不包括自己";
	public static String text_4630 = "以施法者自身为中心的矩形，不包括自己";
	public static String text_4631 = "正前方的矩形，不包括自己";
	public static String text_4632 = "范围的宽度";
	public static String text_4633 = "范围的高度";
	public static String text_4634 = "最大攻击的人数 0表示没有限制，如果有限制 系统会选择距离最近的攻击";
	public static String text_4635 = "最大攻击的人数";
	public static String text_4636 = "系统选择距离最近的攻击";
	public static String text_4637 = "单个目标的落雷攻击";
	public static String text_4638 = "多方向火球类攻击";
	public static String text_4639 = "目标类型，0 表示对某个目标或者位置点进行攻击，无需配置后效方向 1 表示对周围环境进行攻击，需配置后效方向";
	public static String text_4640 = "目标类型";
	public static String text_4641 = "对某个目标或者位置点进行攻击，无需配置后效方向";
	public static String text_4642 = "对周围环境进行攻击，需配置后效方向";
	public static String text_4643 = "轨迹的类型，0表示直线，1表示追踪";
	public static String text_4644 = "轨迹的类型";
	public static String text_4645 = "追踪";
	public static String text_4646 = "飞行物体的飞行速度";
	public static String text_4647 = "飞行物体的飞行距离";
	public static String text_4648 = "攻击的宽度";
	public static String text_4649 = "每个后效的初始位置，如果有10个后效，那么此参数应该填10值 如果填5个值，那么后面5个值默认为0 每个数值的含义是：沿施法者面朝的方向，向前多少个像素，如果为负值，本质就是向后。";
	public static String text_4650 = "后效的初始位置";
	public static String text_4651 = "每个后效的初始位置，如果有10个后效，那么此参数应该填10值。如果填5个值，那么后面5个值默认为0 每个数值的含义是：沿施法者右侧的方向，向右多少个像素，如果为负值，本质就是向左。";
	public static String text_4652 = "某个后效的方向 按钟表分12个方向，取值为0~11，0标识12点钟方向，1表示1点钟方向，6表示6点钟方向..";
	public static String text_4653 = "后效的方向";
	public static String text_4654 = "后效能穿透的次数，0表示不能穿透";
	public static String text_4655 = "后效穿透次数";
	public static String text_4656 = "后效爆炸持续的时间";
	public static String text_4657 = "0毫秒";
	public static String text_4658 = "普通物理攻击";
	public static String text_4659 = "攻击的有效距离，单位为像素";
	public static String text_4660 = "攻击有效距离";
	public static String text_4661 = "30像素";
	public static String text_4662 = "50像素";
	public static String text_4663 = "70像素";
	public static String text_4664 = "90像素";
	public static String text_4665 = "110像素";
	public static String text_4666 = "130像素";
	public static String text_4667 = "150像素";
	public static String text_4668 = "180像素";
	public static String text_4669 = "200像素";
	public static String text_4670 = "武器具体的限制是什么，每一种普通攻击必须设置一个武器类型";
	public static String text_4671 = "适用的武器类型";
	public static String text_4672 = "技能较短描述";
	public static String text_4673 = "技能较短描述，用于显示空间较小的情况";
	public static String text_4674 = "技能树中的列号";
	public static String text_4675 = "LineupManager-副本排队线程";
	public static String text_4676 = "您已经在战场排队中，不能排队！";
	public static String text_4677 = "您不能进入此副本，不能排队！";
	public static String text_4678 = "您已经在排队中！";
	public static String text_4679 = "副本不存在，请重新选择！！！";
	public static String text_4680 = "您成功加入了副本队列！";
	public static String text_4681 = "您已经加入了副本队列，或没有可用的副本队列";
	public static String text_4682 = "进入副本";
	public static String text_4683 = "不进入";
	public static String text_4684 = "您排队的副本已经开启，是否进入副本？";
	public static String text_4685 = "队友[";
	public static String text_4686 = "]选择了";
	public static String text_4687 = "治疗者";
	public static String text_4688 = "防御者";
	public static String text_4689 = "进攻者";
	public static String text_4690 = "未读";
	public static String text_4691 = "已读";
	public static String text_4692 = "已标记删除";
	public static String text_4693 = "有附件未读";
	public static String text_4694 = "有附件已读";
	public static String text_4695 = "0/空/-1,";
	public static String text_4723 = "结婚确认";
	public static String text_4728 = "离婚确认";
	public static String text_4739 = "您的等级还不到";
	public static String text_4791 = "驱逐徒弟";
	public static String text_4795 = "收徒确认";
	public static String text_4797 = "拜师确认";
	public static String text_4819 = "错误提示，您提交的服务器功能[";
	public static String text_4820 = "]目前还没有实现！";
	public static String text_4821 = "未知功能";
	public static String text_4822 = "传送到某地";
	public static String text_4823 = "疗伤";
	public static String text_4824 = "设置复活点";
	public static String text_4825 = "进入商店";
	public static String text_4826 = "修理当前装备";
	public static String text_4827 = "修理全部装备";
	public static String text_4828 = "设置回城点";
	public static String text_4829 = "打开仓库";
	public static String text_4830 = "同意一起护送";
	public static String text_4831 = "丢弃物品";
	public static String text_4832 = "使用绑定";
	public static String text_4833 = "使用离线经验道具";
	public static String text_4834 = "领取物品";
	public static String text_4835 = "变卖所有白装";
	public static String text_4836 = "掷骰子";
	public static String text_4837 = "传送到某地有等级限制";
	public static String text_4838 = "战场复活";
	public static String text_4839 = "召唤队友";
	public static String text_4840 = "同意副本召唤";
	public static String text_4841 = "重置副本进度";
	public static String text_4842 = "重置副本进度并且进入副本";
	public static String text_4843 = "获取战场列表";
	public static String text_4844 = "显示城市信息";
	public static String text_4845 = "参与帮战";
	public static String text_4846 = "帮战竞标";
	public static String text_4847 = "竞标";
	public static String text_4848 = "战场直播";
	public static String text_4849 = "刷新战报";
	public static String text_4850 = "打开帮会仓库";
	public static String text_4851 = "领取每日神奇卡";
	public static String text_4852 = "领取内测礼品";
	public static String text_4853 = "领取跑环任务";
	public static String text_4854 = "转化为10人团队";
	public static String text_4855 = "转化为5人队伍";
	public static String text_4856 = "领取工资";
	public static String text_4857 = "领取";
	public static String text_4858 = "发放补偿";
	public static String text_4859 = "离婚";
	public static String text_4860 = "确认丢弃任务物品";
	public static String text_4861 = "拒绝结婚";
	public static String text_4862 = "同意离婚";
	public static String text_4863 = "拒绝离婚";
	public static String text_4864 = "收徒拒绝";
	public static String text_4865 = "拜师拒绝";
	public static String text_4866 = "使用夫妻传送符";
	public static String text_4867 = "使用挖宝物品";
	public static String text_4868 = "补偿40级公测大礼包";
	public static String text_4869 = "打开物品合成页面";
	public static String text_4870 = "打折修理当前装备";
	public static String text_4871 = "打折修理所有装备";
	public static String text_4872 = "答题";
	public static String text_4873 = "退出答题";
	public static String text_4874 = "报名答题";
	public static String text_4875 = "答题奖品";
	public static String text_4876 = "卖商店";
	public static String text_4877 = "每周帮战报名";
	public static String text_4878 = "帮战查看报名情况";
	public static String text_4879 = "升级帮会仓库";
	public static String text_4880 = "确认升级帮会仓库";
	public static String text_4881 = "帮派约战";
	public static String text_4882 = "查询帮派约战";
	public static String text_4883 = "帮派约战_搜索帮派";
	public static String text_4884 = "帮派约战_选择帮派";
	public static String text_4885 = "帮派约战_输入赌注";
	public static String text_4886 = "帮派约战_是否确认";
	public static String text_4887 = "退出帮会确认";
	public static String text_4888 = "创建家园";
	public static String text_4889 = "创建家园_确认";
	public static String text_4890 = "进入家园";
	public static String text_4891 = "升级家园";
	public static String text_4892 = "升级家园_确认";
	public static String text_4893 = "家园等级打折修理当前装备";
	public static String text_4894 = "家园等级打折修理所有装备";
	public static String text_4895 = "帮主给所有人加BUFF";
	public static String text_4896 = "帮会踢人确认";
	public static String text_4897 = "使用触发任务道具";
	public static String text_4898 = "领取金翎奖";
	public static String text_4899 = "打开师傅名单";
	public static String text_4900 = "打开徒弟名单";
	public static String text_4901 = "收徒登记";
	public static String text_4902 = "拜师登记";
	public static String text_4903 = "师徒列表翻页";
	public static String text_4904 = "师徒姓名";
	public static String text_4905 = "退出师徒名单";
	public static String text_4906 = "查看师徒积分";
	public static String text_4907 = "排队进入副本选择副本";
	public static String text_4908 = "排队进入副本选择角色";
	public static String text_4909 = "排队完成进入副本";
	public static String text_4910 = "排队副本列表";
	public static String text_4911 = "自动加点";
	public static String text_4912 = "领取当乐礼品";
	public static String text_4913 = "排队进入勇士副本选择副本";
	public static String text_4914 = "排队勇士副本列表";
	public static String text_4915 = "排队进入随机副本选择角色";
	public static String text_4916 = "查询帮会积分";
	public static String text_4917 = "查询帮会积分排行";
	public static String text_4918 = "战场列表";
	public static String text_4919 = "领取帮会活动新手礼品";
	public static String text_4920 = "查看充值积分";
	public static String text_4921 = "确认任务奖励";
	public static String text_4922 = "进入跨服地图";
	public static String text_4923 = "玩家改名";
	public static String text_4924 = "帮派改名";
	public static String text_4925 = "领取角色";
	public static String text_4926 = "玩家改名输入";
	public static String text_4927 = "玩家改名确认";
	public static String text_4928 = "帮派改名输入";
	public static String text_4929 = "帮派改名确认";
	public static String text_4930 = "老服务器补偿";
	public static String text_4931 = "合服补偿";
	public static String text_4932 = "设置高级密码";
	public static String text_4933 = "输入高级密码";
	public static String text_4934 = "输入新的高级密码";
	public static String text_4935 = "锁定角色";
	public static String text_4936 = "充值积分转账姓名输入";
	public static String text_4937 = "充值积分转账金额输入";
	public static String text_4938 = "角色唯一礼品";
	public static String text_4939 = "角色唯一礼品序列号输入";
	public static String text_4940 = "充值积分转账确认";
	public static String text_4941 = "输入推广员号码";
	public static String text_4942 = "发送邮件确认";
	public static String text_4943 = "进入跨服切磋地图";
	public static String text_4944 = "进入跨服阵营战";
	public static String text_4945 = "离开跨服";
	public static String text_4946 = "查看当天在线时间";
	public static String text_4947 = "仙武大会报名";
	public static String text_4948 = "比武信息查询";
	public static String text_4949 = "比武信息查询分页";
	public static String text_4950 = "比武进比武场";
	public static String text_4951 = "系统公告";
	public static String text_4952 = "退出比武赛场";
	public static String text_4953 = "领取比武彩票奖励";
	public static String text_4954 = "决赛投票搜索";
	public static String text_4955 = "决赛投票";
	public static String text_4956 = "决赛投票名单翻页";
	public static String text_4957 = "查看候选人信息";
	public static String text_4958 = "修理单件装备";
	public static String text_4959 = "打开邮箱";
	public static String text_4960 = "充值卡充值";
	public static String text_4961 = "充值记录";
	public static String text_4962 = "交易所";
	public static String text_4969 = "服务器错误，传送目标不存在！！！";
	public static String text_4970 = "服务器错误，进入副本失败！！！";
	public static String text_4971 = "这个问题您已经回答过了，不能再回答了！";
	public static String text_4972 = "恭喜您，答对了！";
	public static String text_4973 = "很遗憾，答错喽。";
	public static String text_4974 = "答题时间已过，您现在不能答题！";
	public static String text_4975 = "您没有参加这个活动！";
	public static String text_4976 = "的活动不存在！";
	public static String text_5029 = "两仪门地图人数已经到达上限，请稍后再试。";
	public static String text_5030 = "楼兰古城地图人数已经到达上限，请稍后再试。";
	public static String text_5031 = "乱石河滩地图人数已经到达上限，请稍后再试。";
	public static String text_5032 = "塔林地图人数已经到达上限，请稍后再试。";
	public static String text_5033 = "一线天地图人数已经到达上限，请稍后再试。";
	public static String text_5034 = "紫禁之巅地图人数已经到达上限，请稍后再试。";
	public static String text_5035 = "进入比武赛场";
	public static String text_5036 = "比武场地地图预览";
	public static String text_5057 = "比武报名";
	public static String text_5114 = "选择你要进入的副本";
	public static String text_5123 = "拒绝了您的拜师请求。";
	public static String text_5124 = "拒绝拜师";
	public static String text_5150 = "您不在队伍中，不能将队伍转化为5人团队！";
	public static String text_5151 = "您不是队长，不能将队伍转化为5人团队！";
	public static String text_5152 = "团队中的队员已超过5人，不能将团队转化为5人队伍！";
	public static String text_5153 = "团队已经转化为5人队伍，并且打怪时将获得额外经验奖励！";
	public static String text_5154 = "您不在队伍中，不能将队伍转化为10人团队！";
	public static String text_5155 = "您不是队长，不能将队伍转化为10人团队！";
	public static String text_5156 = "队伍中的队员已超过10人，不能将队伍转化为10人团队！";
	public static String text_5157 = "队伍已经转化为10人团队，团队没有额外打怪经验奖励！";
	public static String text_5158 = "您已经领取过补偿！！！";
	public static String text_5159 = "您没有领取40级公测礼包的记录！！！";
	public static String text_5160 = "补偿给您的物品[";
	public static String text_5161 = "]已经放到了背包里面，请查收！！！";
	public static String text_5162 = "服务器错误，领取补偿失败，请确认您的背包中有可用的格子或者联系客服！！！";
	public static String text_5163 = "您没有合成装备的记录！！！";
	public static String text_5164 = "您的消费项目并没有降价，不需要补偿！";
	public static String text_5165 = "补偿提示";
	public static String text_5166 = "我们将会补偿给您[color=";
	public static String text_5167 = "[/color]元宝，您的消费记录如下：\n";
	public static String text_5168 = "领取补偿";
	public static String text_5177 = "您的账号内没有超过5个角色，不能进行角色领取操作";
	public static String text_5178 = "账号不能为空";
	public static String text_5179 = "该账号不存在，请创建此账号后再输入";
	public static String text_5180 = "账号内已有5个或5个以上角色，不能进行角色领取操作";
	public static String text_5181 = "账号内没有可以领取的角色，不能进行角色领取操作";
	public static String text_5182 = "下面是可以领取的角色\n请选择角色进行领取";
	public static String text_5192 = "对不起，服务器上配置错误，[";
	public static String text_5193 = "]对应的地图不存在";
	public static String text_5194 = "]对应的地图区域不存在";
	public static String text_5197 = "传送到";
	public static String text_5199 = " (需要";
	public static String text_5208 = "系统已经补偿给您 ";
	public static String text_5209 = " 元宝，请检查！！！";
	public static String text_5210 = "系统错误，给您发放补偿失败，请联系游戏GM！！！";
	public static String text_5212 = "任务物品丢弃";
	public static String text_5213 = "您已经报过名了！";
	public static String text_5214 = "级，不能报名！";
	public static String text_5215 = "现在不是报名时间，不能报名！";
	public static String text_5231 = "@美丽的神话";
	public static String text_5232 = "@潜龙发布";
	public static String text_5233 = "@三服血战天下";
	public static String text_5234 = "@四服兄弟情深";
	public static String text_5236 = "请输入一个新名字：";
	public static String text_5242 = "这个名字已经被使用了！";
	public static String text_5243 = "您确认用“";
	public static String text_5245 = "名字不能为空！";
	public static String text_5260 = "提取付费邮件";
	public static String text_5262 = " A 方";
	public static String text_5263 = "B 方";
	public static String text_5264 = "请选择您要进入的战场";
	public static String text_5265 = "输入密码";
	public static String text_5266 = "请输入新密码：";
	public static String text_5267 = "解锁";
	public static String text_5268 = "锁定";
	public static String text_5269 = "成功！";
	public static String text_5270 = "密码错误！";
	public static String text_5271 = "设置失败！";
	public static String text_5272 = "输入的内容不能为空！";
	public static String text_5273 = "设置成功！";
	public static String text_5274 = "两次密码输入不匹配！";
	public static String text_5275 = "请再重复输入一次新密码：";
	public static String text_5292 = "玩家等级不足!";
	public static String text_5293 = "坦克";
	public static String text_5294 = "奶妈";
	public static String text_5295 = "输出";
	public static String text_5296 = "选择角色";
	public static String text_5297 = "人)";
	public static String text_5299 = "[/color]职业可以担当[color=";
	public static String text_5300 = "[/color]角色。";
	public static String text_5301 = "不适合担当";
	public static String text_5302 = "，请选择其他角色:\n";
	public static String text_5303 = "选择你在队伍中的角色";
	public static String text_5306 = "您尚未加入副本队列！";
	public static String text_5307 = "您离开了所有副本队列！";
	public static String text_5308 = "选择副本";
	public static String text_5309 = "副本列表";
	public static String text_5316 = "一服苍狼鬼阁";
	public static String text_5317 = "三服血战天下";
	public static String text_5318 = "四服兄弟情深";
	public static String text_5319 = "五服龙战八荒";
	public static String text_5320 = "六服紫月苍穹";
	public static String text_5321 = "此活动本服不开放！";
	public static String text_5322 = "领取礼品";
	public static String text_5323 = "请输入序列号";
	public static String text_5324 = "提交";
	public static String text_5327 = "领取金翎奖励品";
	public static String text_5328 = "角色锁定";
	public static String text_5329 = "现在您帐号下所有角色处于【非锁定状态】，如果您想【锁定所有角色】，请输入【高级密码】：";
	public static String text_5330 = "解锁角色";
	public static String text_5331 = "现在您帐号下所有角色处于【锁定状态】，如果您想【解锁所有角色】，请输入【高级密码】：";
	public static String text_5332 = "请先设置高级密码再锁定角色！";
	public static String text_5333 = "设置角色锁定失败！";
	public static String text_5334 = "设置角色锁定";
	public static String text_5349 = "老服补偿";
	public static String text_5353 = "昵称变更";
	public static String text_5354 = "您的名字不需要更改！";
	public static String text_5355 = "修改成功！您必须重新登录游戏才能生效！";
	public static String text_5356 = "这个名字不合法！";
	public static String text_5357 = "”作为您的新名字吗？";
	public static String text_5358 = "您的积分余额是：";
	public static String text_5359 = "分。";
	public static String text_5360 = "当天在线时间";
	public static String text_5361 = "您当天的在线时间是：";
	public static String text_5362 = "分钟。";
	public static String text_5368 = "师徒积分查询";
	public static String text_5369 = "您现在的师徒积分是：";
	public static String text_5370 = "打开系统公告";
	public static String text_5373 = "关闭此窗口";
	public static String text_5376 = "请回答问题";
	public static String text_5378 = "回答错误，请继续回答\n";
	public static String text_5380 = "您已经成功退出该活动！";
	public static String text_5381 = "您尚未参加这个活动！";
	public static String text_5382 = "您成功地领取了";
	public static String text_5383 = "游戏币！";
	public static String text_5384 = "经验！";
	public static String text_5385 = "您没有参加这个活动，或者已经领取过奖励了！";
	public static String text_5386 = "答题奖励";
	public static String text_5387 = "拒绝了您的求婚！";
	public static String text_5392 = "，节省了";
	public static String text_5409 = "请输入原密码：";
	public static String text_5410 = "设置高级密码失败！";
	public static String text_5421 = "回城点已设置为当前您所在的位置";
	public static String text_5422 = "复活点已设置为当前您所在的位置";
	public static String text_5428 = "输入推广码";
	public static String text_5429 = "请输入推广码：";
	public static String text_5438 = "您还没有组队，不能召唤队友进入副本！！！";
	public static String text_5439 = "队友召唤";
	public static String text_5440 = "传送到副本入口";
	public static String text_5441 = "，您的队友";
	public static String text_5442 = "邀请你一起进副本，您是否愿意？";
	public static String text_5449 = "再次输入账号";
	public static String text_5450 = "输入安置角色的账号";
	public static String text_5451 = "输入的账号不一致";
	public static String text_5452 = "确定要把";
	public static String text_5453 = "放到";
	public static String text_5454 = "账号下吗？";
	public static String text_5455 = "领取成功，";
	public static String text_5456 = "还继续领取吗？";
	public static String text_5457 = "继续";
	public static String text_5459 = "拒绝了您的收徒请求。";
	public static String text_5460 = "拒绝收徒";
	public static String text_5462 = "点到";
	public static String text_5467 = "积分转账";
	public static String text_5468 = "输入想要转入的角色名：";
	public static String text_5476 = "这个玩家不存在！";
	public static String text_5477 = "您想从账户中转出【";
	public static String text_5478 = "】充值积分到【";
	public static String text_5479 = "输入的数额不正确！";
	public static String text_5480 = "充值积分转账输入金额";
	public static String text_5481 = "输入想要转入的数额：";
	public static String text_5482 = "充值积分转账输入名字";
	public static String text_5489 = "治疗：价格";
	public static String text_5490 = "秒杀道具";
	public static String text_5491 = "请输入序列号：";
	public static String text_5492 = "角色唯一礼品输入序列号";
	public static String text_5507 = "临时窗口";
	public static String text_5508 = "大厦法进来看";
	public static String text_5509 = "错误提示，您选择的窗口[";
	public static String 窗口超时 = "该窗口请求超时，请重新选择";
	public static String text_5510 = "]服务器上不存在";
	public static String text_5511 = "错误提示，您选择窗口中的某个选项不存在";
	public static String text_5512 = "错误提示，您提交的选项类型为客户端的功能选项，服务器处理不了";
	public static String text_5513 = "错误提示，您提交的选项类型为为下一个窗口而非输入内容，服务器处理不了";
	public static String text_5514 = "错误提示，您选择窗口中的某个选项类型[";
	public static String text_5515 = "]目前还不支持";
	public static String text_5516 = "您好";
	public static String text_5517 = "，您选择的";
	public static String text_5518 = "对应的窗口服务器上不存在！";
	public static String text_5519 = "返回上一个窗口";
	public static String text_5520 = "用户名和密码不匹配";
	public static String text_5522 = "对应的链接没有登录";
	public static String text_5523 = "运营活动线程";
	public static String text_5532 = "当乐活动序列号管理";
	public static String text_5533 = "序列号非法";
	public static String text_5534 = "服务器配置错误，礼品不存在";
	public static String text_5535 = "序列号已被使用";
	public static String text_5536 = "17sy金陵奖记录";
	public static String text_5537 = "序列号已经使用";
	public static String text_5538 = "您已经使用过序列号，本次活动不能再使用";
	public static String text_5539 = "双倍经验（1小时）";
	public static String text_5540 = "内测礼包序列号领取";
	public static String text_5541 = "角色唯一礼品管理器";
	public static String text_5542 = "序列号已被使用！";
	public static String text_5543 = "您已经领取过礼品了，不能再领取了！";
	public static String text_5544 = "每天";
	public static String text_5545 = "每周几";
	public static String text_5546 = "固定日期";
	public static String text_5547 = "固定时间段";
	public static String text_5548 = "活动BUFF心跳线程";
	public static String text_5549 = ":待修改";
	public static String text_5550 = "每隔一段时间";
	public static String text_5551 = "活动BOSS心跳线程";
	public static String text_5552 = "老服务器补偿管理";
	public static String text_5564 = "大都声望";
	public static String text_5565 = "洛阳声望";
	public static String text_5566 = "西安声望";
	public static String text_5567 = "少林声望";
	public static String text_5568 = "武当声望";
	public static String text_5569 = "峨眉声望";
	public static String text_5570 = "明教声望";
	public static String text_5571 = "五毒声望";
	public static String text_5572 = "天鹰堡声望";
	public static String text_5573 = "百鸟神宫声望";
	public static String text_5574 = "八极门声望";
	public static String text_5575 = "冲天剑派声望";
	public static String text_5576 = "易门声望";
	public static String text_5577 = "朱元璋义军声望";
	public static String text_5578 = "仇视";
	public static String text_5579 = "冷漠";
	public static String text_5580 = "一般";
	public static String text_5581 = "友好";
	public static String text_5582 = "友善";
	public static String text_5583 = "崇敬";
	public static String text_5584 = "崇拜";
	public static String text_5585 = "问答活动报名已经开始，请大家到总坛或大都的<答题活动报名管理员>处报名，欢迎大家积极参与。";
	public static String text_5586 = "算了，我不想答题了。";
	public static String text_5587 = "您的当前得分：";
	public static String text_5588 = "题目";
	public static String text_5589 = "]第一名：";
	public static String text_5590 = " 得分：";
	public static String text_5591 = "]第二名：";
	public static String text_5592 = "]第三名：";
	public static String text_5593 = "答题结束，您的最终得分：";
	public static String text_5594 = "前";
	public static String text_5595 = "名的玩家会有额外奖品，请前";
	public static String text_5596 = "名的玩家到邮箱查收奖品。\n";
	public static String text_5597 = "您的排名是第";
	public static String text_5598 = "名。请您选择奖励：";
	public static String text_5599 = "金钱：";
	public static String text_5600 = "新科榜眼";
	public static String text_5601 = "新科探花";
	public static String text_5604 = "问答";
	public static String text_5605 = "问答线程";
	public static String text_5606 = "物品交换";
	public static String text_5615 = "时间：";
	public static String text_5616 = "每天：";
	public static String text_5617 = "分~";
	public static String text_5618 = "每周：";
	public static String text_5619 = "点~";
	public static String text_5620 = "每月：";
	public static String text_5621 = "日~";
	public static String text_5622 = "剩余总量：";
	public static String text_5623 = "件";
	public static String text_5624 = "你可购买：";
	public static String text_5625 = "您要购买的商品不存在";
	public static String text_5626 = "您输入的数量错误！";
	public static String text_5627 = "您一次购买数量超过单个商品最大限制！";
	public static String text_5628 = "您要购买的商品对应的物品【";
	public static String text_5629 = "】不存在";
	public static String text_5659 = "余额不足";
	public static String text_5660 = "计费失败，请稍后再试";
	public static String text_5662 = "对应背包格子中没有物品";
	public static String text_5663 = "对应背包格子中的物体没有对应的物种";
	public static String text_5664 = "此物品商人不需要！";
	public static String text_5666 = "此物品为贵重物品，您是否确认要卖给商店？";
	public static String text_5668 = "此回购功能还未实现！";
	public static String text_5669 = "周日";
	public static String text_5670 = "周一";
	public static String text_5671 = "周二";
	public static String text_5672 = "周三";
	public static String text_5673 = "周四";
	public static String text_5674 = "周五";
	public static String text_5675 = "周六";
	public static String text_5700 = "]把您加为了好友";
	public static String text_5738 = "当前副本进度被设置为不可重置状态，系统";
	public static String text_5739 = "小时后自动重置";
	public static String text_5740 = "当前副本进度被设置为不可重置状态，系统每天零点自动重置";
	public static String text_5741 = "没有时间限制";
	public static String text_5742 = "时间限制:";
	public static String text_5743 = "时";
	public static String text_5744 = "至";
	public static String text_5745 = "无时间限制";
	public static String text_5761 = "寻路失败，系统强制清除NPC";
	public static String text_5762 = "服务器配置错误，跑环任务[";
	public static String text_5763 = "您正在进行第";
	public static String text_5764 = "环的任务";
	public static String text_5765 = "您正在做此跑环任务";
	public static String text_5766 = "您已完成了此跑环任务";
	public static String text_5767 = "此跑环任务暂不开放";
	public static String text_5768 = "您的等级与跑环任务不匹配";
	public static String text_5769 = "[第";
	public static String text_5770 = "环]";
	public static String text_5771 = "此跑环任务配置错误";
	public static String text_5772 = "打怪获得经验";
	public static String text_5773 = "购买获得经验";
	public static String text_5774 = "任务获得经验";
	public static String text_5775 = "战场经验";
	public static String text_5776 = "其他方式";
	public static String text_5777 = "智力问答";
	public static String text_5778 = "增加金钱经验荣誉道具";
	public static String text_5779 = "篝火";
	public static String text_5780 = "回归玩家奖励";
	public static String text_5781 = "使用升级道具";
	public static String text_5823 = "你被敌对国家的[";
	public static String text_5824 = "]击杀！";
	public static String text_5825 = "你击杀了敌国的[";
	public static String text_5826 = "你被中立阵营的[";
	public static String text_5835 = "您选择的道具【";
	public static String text_5836 = "】系统不存在，使用无效！";
	public static String text_5837 = "您选择的是普通物品，不能被使用或者装备！";
	public static String text_5873 = "怪通知玩家停止移动";
	public static String text_5874 = "管理员";
	public static String text_5875 = "潜龙GM";
	public static String text_5876 = "潜龙管理员";
	public static String text_5877 = "工作人员";
	public static String text_5878 = "潜龙OL";
	public static String text_5879 = "潜龙online";
	public static String text_5880 = "活动专员";
	public static String text_5881 = "礼物发放员";
	public static String text_5882 = "潜龙客服";
	public static String text_5883 = "神奇时代客服";
	public static String text_5884 = "神奇时代gm";
	public static String text_5885 = "奖品发放员";
	public static String text_5886 = "官方";
	public static String text_5887 = "客服";
	public static String text_5888 = "公告";
	public static String text_5889 = "潛龍";
	public static String text_5890 = "潛龙";
	public static String text_5891 = "潜龍";
	public static String text_5892 = "管理員";
	public static String text_5893 = "活動專員";
	public static String text_5894 = "奖品发放員";
	public static String text_5895 = "系統";
	public static String text_5896 = "潜龙官方";
	public static String text_5897 = "官方管理员";
	public static String text_5898 = "官方GM";
	public static String text_5899 = "潜龙gm";
	public static String text_5900 = "官方gm";
	public static String text_5901 = "客户GM";
	public static String text_5902 = "版主";
	public static String text_5903 = "游戏GM";
	public static String text_5904 = "游戏客服";
	public static String text_5905 = "线上GM";
	public static String text_5906 = "线上客服";
	public static String text_5907 = "游戏专员";
	public static String text_5908 = "线上专员";
	public static String text_5909 = "奖品专员";
	public static String text_5910 = "奖励专员";
	public static String text_5911 = "在线GM";
	public static String text_5912 = "GM在线";
	public static String text_5915 = "请您选择正确的性别";
	public static String text_5919 = "新角色创建成功";
	public static String text_5920 = "未知错误导致创建失败";
	public static String text_5921 = "角色已经存在,请更换一个名称";
	public static String text_5922 = "用户还没有登录";
	public static String text_5923 = "账户现在处于锁定状态，不能删除角色!";
	public static String text_5924 = "您还没有登录，不能删除角色!";
	public static String text_5925 = "角色【";
	public static String text_5926 = "】不存在!";
	public static String text_5927 = "删除失败，用户名不匹配！";
	public static String text_5928 = "已删除角色【";
	public static String text_5929 = "】的所有信息";
	public static String text_5930 = "断网重连";
	public static String text_5931 = "重连成功";
	public static String text_5933 = "队长将物品规则修改为";
	public static String text_5934 = ",分配品级为";
	public static String text_5935 = "色装备";
	public static String text_5937 = "邀请的玩家不在线";
	public static String text_5938 = "邀请的玩家不是同阵营的玩家";
	public static String text_5940 = "已经在队伍中";
	public static String text_5941 = "的队伍已经满员";
	public static String text_5942 = "对方已经把您加入到黑名单中，不能申请加入队伍！";
	public static String 你已经把队伍队长xx加入黑名单不能申请入队 = "你已经把队伍队长@STRING_1@加入黑名单,不能申请入队";
	public static String 队伍队长已经把你加入了黑名单不能申请入队 = "队伍队长@STRING_1@已经把你加入了黑名单,不能申请入队";
	public static String text_5943 = "队长在跨服服务器上，不能申请加入队伍！";
	public static String text_5944 = "你无权发起邀请";
	public static String text_5945 = "不能邀请你自己";
	public static String text_5946 = "队伍已经满员";
	public static String text_5948 = "对方已经把您加入到黑名单中，不能邀请组队！";
	public static String 宝箱乱斗不可组队 = "正在神魂大乱斗中，不能邀请组队！";
	public static String 你已经把对方加入到黑名单中不能邀请组队 = "你已经把对方加入到黑名单中,不能邀请组队";
	public static String text_5949 = "对方在比武赛场中，不能邀请组队！";
	public static String text_5950 = "您在比武赛场中，不能邀请组队！";
	public static String text_5951 = "的队伍已经满员了！";
	public static String text_5952 = "拒绝加入队伍";
	public static String text_5953 = "您已加入其它队伍，请解散或者退出队伍后，在进行操作。";
	public static String text_5954 = "申请人【";
	public static String text_5955 = "】已经在别的队伍中";
	public static String text_5956 = "拒绝你加入队伍";
	public static String text_5957 = "战场击杀";
	public static String text_5958 = "战场击杀周报";
	public static String text_5959 = "战场荣誉";
	public static String text_5960 = "战场荣誉周报";
	public static String text_5961 = "收入周报";
	public static String text_5962 = "在线时间";
	public static String text_5963 = "在线天数";
	public static String text_5964 = "当前等级在线时间";
	public static String text_5965 = "决斗次数";
	public static String text_5966 = "决斗胜利次数";
	public static String text_5967 = "决斗失败次数";
	public static String text_5968 = "本周在线天数";
	public static String text_5969 = "上周在线天数";
	public static String text_5970 = "荣誉击杀";
	public static String text_5971 = "阿拉希胜利次数";
	public static String text_5972 = "5人混战胜利次数";
	public static String text_5973 = "10人混战胜利次数";
	public static String text_5974 = "离开帮会时间";
	public static String text_5975 = "创建时间";
	public static String text_5976 = "徒弟出师个数";
	public static String text_5977 = "每日时间限制:";
	public static String text_6066 = "每周时间限制:";
	public static String text_6067 = "登陆失败 [";
	public static String text_6068 = "] 编辑器 [";
	public static String text_6069 = "] 不存在";
	public static String text_6070 = "登陆成功 [";
	public static String text_6071 = "] 用户名[";
	public static String text_6072 = "] 用户名 [";
	public static String text_6073 = "] 没有权限";
	public static String text_6074 = "] 验证信息不匹配 编辑器 [";
	public static String text_6075 = "] 未知错误 ";
	public static String text_6080 = "商城-生活必备-必备道具";
	// 家族

	public static String text_6087 = "请先离开你现在的家族再来申请建立家族吧！";
	public static String text_6088 = "您的等级不足，请到20级以后再来申请建立家族吧！";
	public static String text_6089 = "您没有输入家族名!";
	public static String text_6095 = "您要创建的家族在这修仙界已经存在了。";
	public static String text_6096 = "创建家族成功！";
	public static String text_6097 = "恭喜你申请的[";
	public static String text_6098 = "]家族创建成功，可通过</f><f color='0xff8400'>[主菜单>社交>家族]</f><f size='28' color='0xFFFFFF'>查看和管理您的家族。";
	public static String text_6099 = "您的等级还不能申请加入家族，请到20级后再来吧。";
	public static String text_6100 = "您已经向";
	public static String text_6101 = "家族发出了申请，请等待该家族处理。";
	public static String text_6102 = "您已经是";
	public static String text_6103 = "成员了，请先离开家族再进行申请。";
	public static String text_6104 = "你在刚刚离开家族，需要等待24小时后才可申请加入家族。";
	public static String text_6109 = "家族人数已经达到了上限，接收失败。";
	public static String text_6112 = "成功";
	public static String text_6129 = "您还没有加入家族。";
	public static String text_6130 = "族长需要先禅让，才允许退出家族。";
	public static String text_6141 = "你没有输入驻地名。";
	public static String text_6145 = "你输入驻地名称在这修仙界已经存在，请重新输入。";
	public static String text_6158 = "你没有权限进行这种操作。";
	public static String text_6163 = "操作失败，现在已经申请了投票，不能再次申请。";
	public static String text_6164 = "你的副家族已经申请成为族长，请家族成员到驻地NPC处投票。";

	public static String text_6172 = "族长";
	public static String text_6173 = "副族长";
	public static String text_6174 = "执法长老";
	public static String text_6175 = "龙血长老";
	public static String text_6176 = "执事长老";
	public static String text_6177 = "家族精英";
	public static String text_6178 = "家族成员";

	public static String text_6186 = "你身上携带的黄金不足支付此操作。";
	public static String text_6189 = "操作成功，你已修改了家族徽章。";

	public static String text_6195 = "家族群发邮件失败！";
	public static String text_6199 = "家族不存在";

	public static String text_6307 = "你还没有加入家族";

	public static String text_6313 = "你所起的家族名重复,重新来个响亮的名字吧!";
	public static String text_6315 = "已经申请过了家族驻地,不用重复申请!";
	public static String text_6316 = "家族驻地线程-";

	public static String text_6337 = "达到";
	public static String text_6338 = "级";
	public static String text_6341 = "天";
	public static String text_6342 = "时";
	public static String text_6343 = "分";
	public static String text_6344 = "秒";

	public static String text_6349 = "家族邮件";
	public static String text_6357 = "驻地不存在";
	public static String 请回本国操作 = "一边去，请回本国操作";
	public static String text_6366 = "有新的家族申请,快去处理";
	public static String text_6367 = "申请加入家族";
	public static String text_6368 = "欢迎 ";
	public static String text_6369 = "加入本家族，大家同舟共济振兴我";
	public static String text_6370 = "目标不存在";
	public static String text_6372 = "你已经有家族了";
	public static String text_6379 = "你的家族已经在修仙界除名,请节哀";
	public static String text_6380 = "家族邮件";
	public static String 家族申请 = "家族申请";
	public static String 在家族喝酒有加成提示 = "";

	public static String 喝酒经验 = "喝酒经验";
	public static String 不在凤栖梧桐区域_无法召唤仙友 = "您不在凤栖梧桐区域内，无法召唤仙友前来与您一起参加活动！";
	public static String 仙友_在凤栖梧桐区域 = "您召唤的仙友已经在凤栖梧桐下！";
	public static String 仙友召唤您 = "您的仙友@STRING_1@，召唤您到他的身边与其一起参加仙缘/论道活动！";
	public static String 仙缘论道令碎片 = "仙缘论道令碎片";
	public static String 仙缘论道令 = "仙缘论道令";
	public static String 取消召唤 = "您取消了仙友的召唤请求！";
	public static String xx_拒绝了您的召唤 = "您的仙友@STRING_1@,拒绝了您的召唤！";
	public static String 请稍等 = "您的仙友@STRING_1@,可能现在有事，让您稍等1分钟！";
	public static String 取喝酒buff = "取喝酒buff";
	public static String 仙缘论道 = "仙缘论道";
	public static String 必须在指定的国家做 = "您所在的凤栖梧桐不是指定活动的凤栖梧桐！";
	public static String 仙缘论道令数量不足 = "仙缘论道令数量不足";
	public static String 仙友在监狱 = "您的仙友被关在监狱，不能召唤！";
	public static String 仙友在副本 = "您的仙友正在进行副本，不能召唤！";
	public static String 仙友在千层塔 = "您的仙友正在进行幽冥幻域，不能召唤！";
	public static String 仙友在战场 = "您的仙友正在进行战场，不能召唤！";
	public static String 仙友在迷城 = "圣兽阁中，不能召唤！";
	public static String 仙界不能传送220以下玩家 = "未飞升玩家不可被召唤到仙界！";
	public static String 仙友在比武 = "您的仙友正在比武，不能召唤！";
	public static String 没有可存储的喝酒BUFF = "没有可存储的喝酒BUFF！";
	public static String 删除已过期的buff = "删除已过期的buff！";
	public static String 存酒成功描述 = "亲爱的仙友，@STRING_1@【@STRING_2@】</f> 存放在这里，现在的剩余时间为 @STRING_3@ ，请您在三天之内进行领取，否则3天后会消失！取酒时会消耗您@STRING_4@两绑银，是否确定存入？";
	public static String 您之前已经存储过 = "您之前已经存储过，@STRING_1@【@STRING_2@】</f>，剩余时间: @STRING_3@ ,要替换成@STRING_4@【@STRING_5@】</f>吗？";
	public static String 存酒成功描述_没有消耗提示 = "亲爱的仙友，@STRING_1@【@STRING_2@】</f> 存放在这里，现在的剩余时间为 @STRING_3@ ，请您在三天之内进行领取，否则3天后会消失！";
	public static String 替换 = "替换";
	public static String 替换酒buff失败 = "替换酒buff失败!";
	public static String 您消耗了几个仙缘论道令 = "您消耗了@STRING_1@个仙缘论道令";
	public static String 恭喜好心人 = "恭喜好心人!";
	public static String 恭喜好心人内容 = "恭喜您在仙缘/论道中，帮助@STRING_1@完成了任务，特此奖励您一个仙缘论道令碎片，凑齐5个仙缘论道令碎片可换取一个仙缘论道令，请继续努力帮助其他需要帮助的人！";
	public static String 奖励已通过邮件的形式发送 = "奖励已通过邮件的形式发送，请注意查收！";
	public static String 没有可取的酒 = "删除已过期的喝酒buff,没有酒可取！";
	public static String 没有酒可取 = "没有酒可取,存储的酒可能已经过期删除！";
	public static String 身上已经有酒buff = "亲爱的玩家，您现在身上已经有@STRING_1@【@STRING_2@】</f>的状态,是否要领取存储在这里的@STRING_3@【@STRING_4@】</f>覆盖当前的状态吗？";
	public static String 等级超过不能使用 = "亲爱的仙友，您当前的等级已经超过道具使用等级，无法领取存放的喝酒状态！";
	public static String 替换成功 = "喝酒BUFF替换成功，您获得了@STRING_1@【@STRING_2@】</f>的效果！";

	// 191，211副本
	public static String 望乡灭境191 = "191级望乡灭境";
	public static String 酆都鬼境211 = "211级酆都鬼境";
	public static String 望乡灭境191_元神191 = "191级望乡灭境(元神)";
	public static String 酆都鬼境211_元神211 = "211级酆都鬼境(元神)";
	public static String 望乡灭境 = "望乡灭境";
	public static String 酆都鬼境 = "酆都鬼境";
	public static String 望乡灭境元神 = "望乡灭境元神";
	public static String 酆都鬼境元神 = "酆都鬼境元神";
	public static String 望乡灭境奖励 = "望乡灭境奖励";
	public static String 望乡灭境_元神奖励 = "望乡灭境(元神)奖励";
	public static String 酆都鬼境奖励 = "酆都鬼境奖励";
	public static String 酆都鬼境_元神奖励 = "酆都鬼境(元神)奖励";
	public static String 冥帅夺魂铃 = "冥帅夺魂铃";
	public static String 冥帅夺魂令 = "冥帅夺魂令";
	public static String 崔钰判官笔 = "崔钰判官笔";
	public static String 崔钰勾魂笔 = "崔钰勾魂笔";
	public static String 副本中不能使用飞行坐骑 = "副本中不能使用飞行坐骑！";
	public static String 无常冥帅 = "无常冥帅";
	public static String 无常冥帅元神 = "无常冥帅元神";
	public static String 判官崔钰 = "判官崔钰";
	public static String 判官崔钰元神 = "判官崔钰元神";

	public static String 百花锦囊 = "百花锦囊";
	public static String 仙蒲圣者 = "仙蒲圣者";
	public static String 乾坤仙人 = "乾坤仙人";
	public static String 驭鹤尊者 = "驭鹤尊者";
	public static String 成功驾驭八卦仙蒲 = "恭喜玩家@STRING_1@，成功驾驭八卦仙蒲，成为永恒的仙蒲圣者";
	public static String 成功驾驭乾坤葫芦 = "恭喜玩家@STRING_1@，成功驾驭八卦仙蒲，成为永恒的乾坤仙人";
	public static String 成功驾驭梦瞳仙鹤 = "恭喜玩家@STRING_1@，成功驾驭八卦仙蒲，成为永恒的驭鹤尊者";

	// 交易系统

	public static String composeBaoshi = "宝石合成";
	public static String composeLingfu = "灵符合成";
	public static String petbreed = "宠物孵化";
	public static String petmating = "宠物繁殖";
	public static String gengudan = "根骨丹";

	public static String pet_fight = "宠物在出战状态";

	public static String text_没有聊天组 = "无此聊天群组";

	public static String text_队长正在为咱们起个响亮的名头 = "队长正在为咱们起个响亮的名头，请稍后";
	public static String text_6381 = "登录活动";
	public static String text_6382 = "设置个人资料中爱好不合法！";
	public static String text_6383 = "设置个人资料中心情不合法！";
	public static String text_6384 = "设置个人资料中个人说明不合法！";
	public static String text_6385 = "宠物名不合法！";

	public static String 孵化倒计时 = "孵化倒计时";

	public static String 消失倒计时 = "消失倒计时";
	public static String 花费家族资金 = "花费家族资金:";
	public static String 发布了养蚕任务 = "发布了养蚕任务.";
	public static String 偷偷 = "偷...偷偷...偷偷";
	public static String 你的家族今天已经压过镖了明天再来吧 = "你的家族今天已经压过镖了,明天再来吧!";
	public static String 家族资金不足封印 = "家族已经被封印，无法使用此功能";

	public static String 需要花费银子 = "需要花费@STRING_1@家族资金，确定吗？";
	public static String 需要花费家族资金 = "需要花费家族资金@STRING_1@，确定吗？";
	public static String 尊敬的族长大人你希望帮助哪个族员领取祝福丹 = "尊敬的族长大人,你希望帮助哪个族员领取祝福丹?";
	public static String 每位族员只能在祝福树上摘取一颗祝福丹 = "每位族员只能在祝福树上摘取一颗祝福丹,摘取后,将会自动发放到背包.当祝福树上果实全部摘取完毕后,祝福果树将消失,并可重新种植.祝福果树的寿命为6个小时请尽快采摘O(∩_∩)O";

	public static String 家族title = "@STRING_1@的@STRING_2@";

	public static String 高级鲁班令 = "高级鲁班令";
	public static String s_喝酒 = "s_喝酒";
	public static String s_屠魔 = "s_屠魔";
	public static String s_祈福 = "s_祈福";
	public static String s_仙丹修炼 = "s_仙丹修炼";
	public static String s_空岛大冒险 = "s_空岛大冒险";
	public static String s_斩妖除魔 = "s_斩妖除魔";
	public static String 果园种植 = "果园种植";
	public static String 神农访仙树 = "神农-访仙树";
	public static String[] FIELD_INDEX_NAME = { "一", "二", "三", "四", "五", "六" };
	public static String[] FIELD_INDEX_NAME2 = { "一", "三", "五", "七", "八" };
	public static String[] CAVE_BUILDING_NAMES = { "逍遥居", "万宝库", "驭兽斋", "门牌", "法门", "田地1", "田地2", "田地3", "田地4", "田地5", "田地6" };
	public static String 发BUFF = "@STRING_1@发布了@COUNT_1@级BUFF:@STRING_2@持续:@STRING_3@";
	public static String 召唤BOSS = "@STRING_1@ 在驻地召唤了boss:@STRING_2@,,大家快来击杀!";
	public static String 忠诚 = "忠诚";
	public static String 精明 = "精明";
	public static String 谨慎 = "谨慎";
	public static String 狡诈 = "狡诈";

	public static String 任务奖励_角色经验 = "角色经验";
	public static String 任务奖励_物品 = "物品";
	public static String 任务奖励_绑银 = "绑银";
	public static String 任务奖励_金钱2 = "金钱2";
	public static String 任务奖励_金钱3 = "金钱3";
	public static String 任务奖励_BUFF = "BUFF";
	public static String 任务奖励_称号 = "称号";
	public static String 任务奖励_声望 = "声望";
	public static String 任务奖励_修法值 = "修法值";
	public static String 任务奖励_贡献度 = "贡献度";
	public static String 任务奖励_技能点 = "技能点";
	public static String 任务奖励_随机获得物品奖励 = "随机获得物品奖励";
	public static String 任务奖励_灵气值 = "灵气值";
	public static String 任务奖励_分职业给予奖励 = "分职业给予奖励";
	public static String 任务奖励_功勋 = "功勋值";
	public static String 任务奖励_历练 = "历练值";
	public static String 任务奖励_国家资源 = "国家资源";
	public static String 您的可用绑银不足消耗银子 = "您的可用绑银不足，消耗了@COUNT_1@银子";
	public static String 您的可用商城银子不足消耗银子 = "您的可用银票不足，消耗了@COUNT_1@银子";
	public static String 您的可用绑银不足消耗商城银子 = "您的可用绑银不足，消耗了@COUNT_1@银票";
	public static String 您的可用绑银不足消耗商城银子和银子 = "您的可用绑银不足，消耗了@COUNT_1@银票和@COUNT_2@银子";
	public static String 银票不能丢弃 = "银票不能丢弃";
	public static String 银票不能移动 = "银票不能移动";
	public static String 使用银子 = "使用银子";
	public static String 使用银票 = "使用银票";
	public static String 选择祈福使用的的货币 = "选择祈福使用的的货币";
	public static String 选择购买使用的货币 = "选择购买使用的货币";
	public static String 祈福选货币提示 = "选择祈福使用的货币,如果选择银票(会使用绑定香火，不使用非绑定香火)得到的物品绑定，在银票不够的时候会直接使用银子。\n";
	public static String 商城选货币提示 = "选择购买使用的货币,如果选择银票，在银票不够的时候会直接使用银子。\n";

	public static String 躁狂野牛 = "躁狂白虎";
	public static String 离火幼狼 = "疾风幼狼";
	public static String 日曜幼虎 = "日曜龙猫";
	public static String 幽毒幼蝎 = "幽毒蛮象";
	public static String 噩龏蚩奔 = "噩龏蚩奔";
	public static String 赤木魔狼 = "赤木魔狼";
	public static String 蜚魔妖虎 = "蜚魔妖虎";
	public static String 巫蛊魔蝎 = "巫蛊魔蝎";
	public static String 妖尨牛魔 = "妖尨牛魔";
	public static String 啸月苍狼 = "啸月苍狼";
	public static String 啸天魔虎 = "啸天魔虎";
	public static String 赤墨龙蝎 = "赤墨龙蝎";
	public static String 五色神牛 = "五色神牛";
	public static String 银狼至尊 = "银狼至尊";
	public static String 赤炎虎魁 = "赤炎虎魁";
	public static String 龙蟒蝎皇 = "龙蟒蝎皇";

	public static String 奖励坐骑2 = "奖励坐骑";
	public static String 奖励坐骑 = "奖励坐骑碎片";
	public static String 家族运镖奖励 = "家族运镖奖励";
	public static String 元神升级 = "元神升级";
	public static String 奖励坐骑_邮件 = "恭喜你元神达到@COUNT_1@级,获得：@STRING_1@@COUNT_2@个";
	public static String 武圣斗魁奖励 = "武圣斗魁奖励";
	public static String 武圣斗帝奖励 = "武圣斗帝奖励";
	public static String 武圣斗圣奖励 = "武圣斗圣奖励";
	public static String 武圣斗尊奖励 = "武圣斗尊奖励";
	public static String 武圣斗宗奖励 = "武圣斗宗奖励";
	public static String 武圣斗皇奖励 = "武圣斗皇奖励";
	public static String 武圣斗王奖励 = "武圣斗王奖励";
	public static String 武圣斗灵奖励 = "武圣斗灵奖励";
	public static String 武圣斗师奖励 = "武圣斗师奖励";
	public static String 武圣斗者奖励 = "武圣斗者奖励";
	public static String 武圣斗徒奖励 = "武圣斗徒奖励";
	public static String 二级分类酒 = "酒";
	public static String 兑换喝酒 = "喝酒";
	public static String 兑换封魔录 = "封魔录";
	public static String 玩家可使用一定数量的白玫瑰 = "玩家可使用一定数量的\"白玫瑰\"、\"蓝色妖姬\"、\"棒棒糖\"、\"巧克力\"道具兑换当天\"喝酒\"或\"使用封魔录\"次数，每天最多可增加2次喝酒和使用封魔录次数。";
	public static String 增加酒次数 = "增加酒次数";
	public static String 增加封魔录次数 = "增加封魔录次数";
	public static String 兑换活动还没有开始今天不能兑换 = "兑换活动还没有开始，今天不能兑换";
	public static String 当日第一次兑换 = "当日第一次兑换";
	public static String 已有一次兑换 = "已有一次兑换";
	public static String 炼狱神匣 = "炼狱神匣";
	public static String 炼狱宝匣 = "炼狱宝匣";
	public static String 宝宝 = "宝宝";
	public static String 精英 = "精英";
	public static String 火鸡大餐礼券 = "火鸡大餐礼券";
	public static String 进阶技能没有加点 = "进阶技能没有加点，不需要使用此道具";
	public static String 使用洗髓丹得到修法值 = "使用@STRING_1@成功，清空进阶技能，返还@COUNT_1@点修法值";

	// 天神“NPC名字”出现在“国家名”的“世界地图区域名称”，快去找他领取福利！
	public static String 天神XX出现在XX的区域 = "天神@STRING_1@将在5分钟出现在@STRING_2@的@STRING_3@，快去找他领取福利！";
	// 天神“NPC名字”出现在“国家名”的“地图名”，5分钟以后消失。快去领取福利吧！
	public static String 天神XX出现在XX的地图 = "天神@STRING_1@出现在@STRING_2@的@STRING_3@，5分钟以后消失。快去领取福利吧！";
	public static String 请您升到40级再来吧 = "请您升到40级再来吧";

	public static String 您的等级不足 = "您的等级不足,不能参加";
	public static String 活动还未开启 = "活动还未开启";
	public static String 摘取次数达到上限 = "你今天摘取的次数太多了,明天再来吧";

	public static String 国战控制面板说明 = "在规定时间内，击杀/防守@longxiangshidi@即可获得胜利！";
	public static String 国战防御 = "防御";
	public static String 国战撤退 = "撤退";
	public static String 国战攻击口号 = "弟兄们上啊，废了他们！";
	public static String 国战防御口号 = "守住守住别让他们冲破！";
	public static String 国战撤退口号 = "快撤快撤不然团灭了啊！";
	public static String 国战_边境复活点 = "边境复活点";
	public static String 国战_王城复活点 = "王城复活点";
	public static String 国战_龙象释帝正在被攻击 = "龙象释帝正在被攻击！";
	public static String 国战_播报1 = "<f color='0xf39808'>@player_country@的</f><f color='0xfe82e5'>@player_name@</f><f color='0xf39808'>已经勇不可当，连斩数达到</f><f color='0xfe82e5'>@liansheng_num@</f>";
	public static String 国战_播报2 = "<f color='0xf39808'>玩家</f><f color='0xfe82e5'>@player_name@</f><f color='0xf39808'>击杀了</f><f color='0xfe82e5'>@killed_country@</f><f color='0xf39808'>的@title_name@！</f>";
	public static String 国战_播报3 = "<f color='0xfe82e5'>@defender_name@</f><f color='0xf39808'>的@npc_name@被</f><f color='0xfe82e5'>@player_name@</f><f color='0xf39808'>击杀了，</f><f color='0xfe82e5'>@attacker_name@</f><f color='0xf39808'>占领了边关，可以此为凭借进一步攻击主城</f>";
	public static String 国战_播报4 = "<f color='0xfe82e5'>@attacker_name@</f><f color='0xf39808'>的@npc_name@被</f><f color='0xfe82e5'>@player_name@</f><f color='0xf39808'>击杀了，</f><f color='0xfe82e5'>@defender_name@</f><f color='0xf39808'>夺回了边关！</f>";
	public static String 国战_播报5 = "<f color='0xfe82e5'>@defender_name@</f><f color='0xf39808'>的@npc_name@被</f><f color='0xfe82e5'>@player_name@</f><f color='0xf39808'>击杀了，</f><f color='0xfe82e5'>@attacker_name@</f><f color='0xf39808'>占领了要塞，可以此为凭借进入密道进攻主城</f>";
	public static String 国战_播报6 = "<f color='0xfe82e5'>@attacker_name@</f><f color='0xf39808'>的@npc_name@被</f><f color='0xfe82e5'>@player_name@</f><f color='0xf39808'>击杀了，</f><f color='0xfe82e5'>@defender_name@</f><f color='0xf39808'>夺回了要塞！</f>";
	public static String 国战_播报7 = "<f color='0xfe82e5'>@defender_name@</f><f color='0xf39808'>的@npc_name@被</f><f color='0xfe82e5'>@player_name@</f><f color='0xf39808'>击杀了，</f><f color='0xfe82e5'>@attacker_name@</f><f color='0xf39808'>占领了主城龙象卫馆，可以此为凭借进一步攻击主城@longxiangshidi_name@</f>";
	public static String 国战_播报8 = "<f color='0xfe82e5'>@attacker_name@</f><f color='0xf39808'>的@npc_name@被</f><f color='0xfe82e5'>@player_name@</f><f color='0xf39808'>击杀了，</f><f color='0xfe82e5'>@defender_name@</f><f color='0xf39808'>夺回了主城龙象卫馆！</f>";
	public static String 国战_播报9 = "<f color='0xfe82e5'>@attacker_name@</f><f color='0xf39808'>的</f><f color='0xfe82e5'>@player_name@</f><f color='0xf39808'>击杀了</f><f color='0xfe82e5'>@defender_name@</f><f color='0xf39808'>的@longxiangweizhang_name@！守方主城危矣！</f>";
	public static String 国战_播报10 = "<f color='0xfe82e5'>@attacker_name@</f><f color='0xf39808'>的物资已经到达，混元至圣决定继续延长对@defender_name@发起进攻@yanchangshijian@分钟。</f>";
	public static String 国战_播报11 = "<f color='0xfe82e5'>@defender_name@</f><f color='0xf39808'>混元至圣花费大量天材地宝给@longxiangshidi_name@进行疗伤恢复了其@huifuxueliang@%的血量</f>";
	public static String 国战_播报12 = "国战已结束，@win_name@战胜了@lose_name@";
	public static String 国战_播报13 = "<f color='0xfe82e5'>@attacker_name@</f><f color='0xf39808'>对</f><f color='0xfe82e5'>@defender_name@</f><f color='0xf39808'>的战争@result@了！</f>";
	public static String 国战已结束 = "国战已经结束";
	public static String 玩家官职不够 = "玩家官职不够，不能进行此操作";
	public static String 已达使用次数上限 = "已达使用次数上限";
	public static String 资源不够 = "资源不够";
	public static String 国战信息 = "国战信息";
	public static String 是否立即前往国战前线 = "是否立即前往国战前线？";
	public static String 攻方 = "攻方";
	public static String 守方 = "守方";
	public static String 胜利 = "胜利";
	public static String 失败 = "失败";
	public static String 防守 = "防守";
	public static String 进攻 = "进攻";
	public static String 去防守 = "去防守@place_name@";
	public static String 去进攻 = "去进攻@place_name@";
	public static String 国战通知 = "@tongzhishijian@分钟后将进行与@country_name@的国战，请做好战斗准备";
	public static String 宣战时间错误 = "宣战时间错误";
	public static String 本国今日已经宣战 = "本国今日已经宣战";
	public static String 被宣战国今日不能再被宣战 = "被宣战国今日不能再被宣战";
	public static String 国战开始 = "<f color='0xfe82e5'>@attacker_name@</f><f color='0xf39808'>对</f><f color='0xfe82e5'>@defender_name@</f><f color='0xf39808'>的国战开启了！</f>";
	public static String 国战 = "国战";
	public static String 国战开始窗口 = "@country1@ vs @country2@的国战开启了，请大家前往战斗！";
	public static String 玩家等级不够 = "等级不够，不能参与国战";
	public static String 本国当前没有进行国战 = "本国当前没有进行国战";
	public static String 您的职位不够 = "您的职位不够，不能对他国宣战!";
	public static String 今日已宣战 = "今日已对@country@宣战，不能再对他国宣战!";
	public static String 宣战成功 = "您已经对@country@宣战成功，国战将在@time@开启！";
	public static String 不能再被宣战 = "该国今日不能再被宣战";
	public static String 宣战时间 = "请在当日@startTime@~@endTime@之间宣战";
	public static String 请选择宣战国家 = "请选择宣战国家";
	public static String 请选择您要宣战的国家 = "请选择您要宣战的国家";
	public static String 国战规则 = "【报名】\n每天0点-18点可以向其他国家宣战。（第二天进行战斗，有1天的备战时间） 。\n每天只能被一个国家宣战，也能对一个国家宣战。 \n两个国家两天内只能进行一次战争。\n最弱的一个国家2天内只能被宣战一次，第一名国家每天都能被宣战。\n只有混元至圣、纯阳真圣或司命天王才能发起，点击【神威天将】-【发起国战】提出申请。\n\n【国战规则】\n开始时间：\n@startTime@-@endTime@宣战，则第二天@startFightTime@国家正式战争开始，战斗时间为@guozhanTime@分钟。\n\n国战胜败条件：\n攻守双方的混元至圣、纯阳真圣或司命天王可以通过指挥界面进行战斗指挥，在@guozhanTime@分钟的战斗中击杀掉防守国家的【龙象释帝】即获得国战胜利！\n\n国战路线：\n参与国战的玩家通过3条路线抵达防守国家的王城。\n第一条：由边境→国家边关（昆仑：圣天之道，九州：九州天路，万法：法缘大道）→国家主城\n第二条：由边境→国家边关（昆仑：圣天之道，九州：九州天路，万法：法缘大道）→各个国家主城的密道→国家主城\n第三条：由边境→各个国家要塞→各个国家密道→国家主城\n\n国战复活点：\n在国家边关（昆仑：圣天之道，九州：九州天路，万法：法缘大道）、各国的要塞、国家主城分别由三个【空灵天兵】、【空玄天兵】、【龙象卫士】守卫，进攻方击杀守卫可占领其所在位置复活点。防守方可击杀进攻方守护NPC破坏进攻方复活点。\n没有任何复活点，进攻方在本国边境复活。\n防守国家复活在【龙象卫长】身边，如果【龙象卫长】被击杀，则防守方全部在王城默认复活点复活。\n\n国战资源：\n进攻方可使用国战资源进行拉人，延长国战时间。每次国战最多可拉3次人，可将本国玩家拉到当前的复活区；延长2次国战，每次15分钟；\n防守方可使用国战资源进行拉人，给龙象释帝加血。每次国战最多可拉3次人，可将本国玩家拉到到当前的复活区；给龙象释帝加2次血，每次15%；\n\n经验奖励：\n进攻方在国战开始后，在防守国地图内每占领一个【空灵天兵】、【空玄天兵】、【龙象卫士】守护的复活点，将获得更多经验；击杀了【龙象卫长】也会获得更多经验，击杀【龙象释帝】后获得国战胜利（只击杀【龙象释帝】只会获得基础经验）\n防守方在国战开始后，在本国地图内每守卫一个【空灵天兵】、【空玄天兵】、【龙象卫士】守护的复活点，将获得更多经验；保护【龙象卫长】未被击杀，也会获得更多经验，90分钟内【龙象释帝】未被击杀，获得国战胜利（只守护【龙象释帝】只会获得基础经验）\n\n注：国战状态死亡不掉落道具。";

	public static String 你没有所需道具 = "你没有所需道具:@STRING_1@";
	public static String 攻击年兽 = "你使用了:@STRING_1@,对年兽造成了@COUNT_1@点伤害";
	public static String 你获得了道具 = "恭喜你获得了:@STRING_1@";

	public static String 你的等级不符合活动要求 = "你的等级不符合活动要求";

	public static String 你在副本里不能切换元神 = "你在副本里,不能切换元神";
	public static String 路径不合法 = "路径不合法";
	public static String 黑 = "黑";
	public static String 红 = "红";
	public static String 黄 = "黄";
	public static String 使用成功您剩余罪恶值 = "使用@STRING_1@成功，您剩余罪恶值@COUNT_1@点";
	public static String 罪恶值必须大于0才能使用 = "罪恶值必须大于0才能使用";
	public static String 罪恶值必须大于X才能使用 = "罪恶值必须大于@COUNT_1@才能使用";
	public static String 使用成功您的体力值变为 = "使用@STRING_1@成功，您的体力值变为@COUNT_1@点";
	public static String 您的体力值已经够用了体力宝贵不要浪费 = "您的体力值已经够用了，体力宝贵，不要浪费";
	public static String 是锁魂的镶嵌会导致也锁魂 = "@STRING_1@是锁魂的，镶嵌到@STRING_2@会导致@STRING_2@也锁魂";
	public static String 一级 = "1级";
	public static String 二级 = "2级";
	public static String 三级 = "3级";
	public static String 四级 = "4级";
	public static String 五级 = "5级";
	public static String 六级 = "6级";
	public static String 七级 = "7级";
	public static String 八级 = "8级";
	public static String 九级 = "9级";
	public static String 此地图不能拉人 = "此地图不能拉人";
	public static String 副本里没有队伍 = "副本里没有队伍";
	public static String 多个队伍进入同一副本 = "多个队伍进入同一副本";
	public static String 没有小队不能发起副本 = "没有小队不能发起副本";
	public static String 只能是队长才能发起副本 = "只能是队长才能发起副本";
	public static String 队伍必须大于2人才能发起副本 = "队伍必须大于2人才能发起副本";
	public static String 队伍必须大于3人才能发起副本 = "队伍必须大于3人才能发起副本";
	public static String 没有物品 = "没有@STRING_1@。";
	public static String 今日进入次数已经达到次 = "今日进入次数已经达到@COUNT_1@次。";
	public static String 需要从准备状态退出才可以关闭窗口 = "需要从准备状态退出才可以关闭窗口";
	public static String 当全部队员都接受后方可由队长带领进入副本请您耐心等待 = ""; // 客户端没用，改为空,否则会出显示错
	public static String 最少需要到级才能进入 = "最少需要到@COUNT_1@级才能进入";
	public static String 传送需要银子你的银子不足 = "传送需要@COUNT_1@绑银，你的绑银不足";
	public static String 最少需要1点体力才能进入 = "最少需要1点体力才能进入";
	public static String 您今天进入地图时间已满不能进入了 = "您今天进入地图时间已满，不能进入了";
	public static String 进入付费地图 = "进入付费地图";
	public static String 你完成了任务快去找交付任务 = "你完成了任务[@STRING_1@]快去找[@STRING_2@]交付任务";
	public static String 年 = "年";
	public static String 月 = "月";
	public static String 日 = "日";
	public static String 时 = "时";
	public static String 分 = "分";
	public static String 国战状态下绑银不足或已达今日使用上限购买失败 = "国战状态下，绑银不足或已达今日使用上限，购买失败";
	public static String 绑银和银子都不足购买失败 = "绑银和银子都不足，购买失败";
	public static String 低于40级以下的玩家不能参加此活动 = "低于40级以下的玩家不能参加此活动";
	public static String 灵矿争夺战说明 = "灵矿争夺战说明";
	public static String 玩家击杀了要捉拿的获得了25两银子和经验 = "玩家(@STRING_1@)击杀了@STRING_2@要捉拿的@STRING_3@获得了25两银子和经验";
	public static String 火鸡肉 = "火鸡肉";
	public static String 酒票 = "酒票";
	public static String 帖券 = "帖券";
	public static String 火鸡蛋 = "火鸡蛋";
	public static String 感恩首充礼 = "感恩首充礼";
	public static String 火鸡大餐 = "火鸡大餐";
	public static String 蛮荒女皇蛋 = "蛮荒女皇蛋";
	public static String 玄瞳金晶蛋 = "玄瞳金晶蛋";
	public static String 九尾雪狐蛋 = "九尾雪狐蛋";
	public static String 绝品圣宠精魄 = "绝品圣宠精魄";
	public static String 仙鹤羽毛 = "仙鹤羽毛";
	public static String 飞行坐骑礼包3天 = "飞行坐骑礼包（3天）";
	public static String 炼器符 = "炼器符";
	public static String 一品元气真丹初级 = "一品元气真丹(初级)";
	public static String 二品元气真丹初级 = "二品元气真丹(初级)";
	public static String 三品元气真丹初级 = "三品元气真丹(初级)";
	public static String 四品元气真丹初级 = "四品元气真丹(初级)";
	public static String 六品元气真丹初级 = "六品元气真丹(初级)";
	public static String 一品元气真丹中级 = "一品元气真丹(中级)";
	public static String 二品元气真丹中级 = "二品元气真丹(中级)";
	public static String 三品元气真丹中级 = "三品元气真丹(中级)";
	public static String 四品元气真丹中级 = "四品元气真丹(中级)";
	public static String 五品元气真丹中级 = "五品元气真丹(中级)";
	public static String 六品元气真丹中级 = "六品元气真丹(中级)";
	public static String 一品元气玄丹初级 = "一品元气玄丹(初级)";
	public static String 二品元气玄丹初级 = "二品元气玄丹(初级)";
	public static String 三品元气玄丹初级 = "三品元气玄丹(初级)";
	public static String 四品元气玄丹初级 = "四品元气玄丹(初级)";
	public static String 五品元气玄丹初级 = "五品元气玄丹(初级)";
	public static String 六品元气玄丹初级 = "六品元气玄丹(初级)";
	public static String 一品元气玄丹中级 = "一品元气玄丹(中级)";
	public static String 二品元气玄丹中级 = "二品元气玄丹(中级)";
	public static String 三品元气玄丹中级 = "三品元气玄丹(中级)";
	public static String 四品元气玄丹中级 = "四品元气玄丹(中级)";
	public static String 五品元气玄丹中级 = "五品元气玄丹(中级)";
	public static String 六品元气玄丹中级 = "六品元气玄丹(中级)";
	public static String 混元伏魔鼎 = "混元伏魔鼎";
	public static String 混元镇邪鼎 = "混元镇邪鼎";
	public static String 混天封妖鼎 = "混天封妖鼎";
	public static String 混天至尊鼎 = "混天至尊鼎";
	public static String 洪荒古符 = "洪荒古符";
	public static String 混元伏魔残片 = "混元伏魔残片";
	public static String 混元镇邪残片 = "混元镇邪残片";
	public static String 混天封妖残片 = "混天封妖残片";
	public static String 混天至尊残片 = "混天至尊残片";
	public static String 洪荒古残卷 = "洪荒古残卷";
	public static String 楠木 = "楠木";
	public static String 铸铁 = "铸铁";
	public static String 胶黏剂 = "胶黏剂";
	public static String 元宵迎春锦囊 = "元宵迎春锦囊";
	public static String 元宵特惠礼包 = "元宵特惠礼包";
	public static String 高级海鲜大礼包 = "高级海鲜大礼包";
	public static String 超级海鲜大礼包 = "超级海鲜大礼包";
	public static String 盐烤青花鱼 = "盐烤青花鱼";
	public static String 海鲜烩饭 = "海鲜烩饭";
	public static String 焗烤龙虾 = "焗烤龙虾";
	public static String 惊涛鱼排 = "惊涛鱼排";
	public static String 香浓炖鱼 = "香浓炖鱼";
	public static String 秘制鲅鱼 = "秘制鲅鱼";
	public static String 红烧鲍翅燕 = "红烧鲍翅燕";
	public static String 鲍汁鸡腿菇 = "鲍汁鸡腿菇";
	public static String 碳烧元贝 = "碳烧元贝";
	public static String 干烧桂鱼 = "干烧桂鱼";
	public static String 海参鹅掌煲 = "海参鹅掌煲";
	public static String 盐烤青花鱼高级 = "盐烤青花鱼(高级)";
	public static String 海鲜烩饭高级 = "海鲜烩饭(高级)";
	public static String 焗烤龙虾高级 = "焗烤龙虾(高级)";
	public static String 惊涛鱼排高级 = "惊涛鱼排(高级)";
	public static String 香浓炖鱼高级 = "香浓炖鱼(高级)";
	public static String 秘制鲅鱼高级 = "秘制鲅鱼(高级)";
	public static String 红烧鲍翅燕高级 = "红烧鲍翅燕(高级)";
	public static String 鲍汁鸡腿菇高级 = "鲍汁鸡腿菇(高级)";
	public static String 碳烧元贝高级 = "碳烧元贝(高级)";
	public static String 干烧桂鱼高级 = "干烧桂鱼(高级)";
	public static String 海参鹅掌煲高级 = "海参鹅掌煲(高级)";
	public static String 头等舱金牌礼包普通 = "头等舱金牌礼包(普通)";
	public static String 头等舱银牌礼包普通 = "头等舱银牌礼包(普通)";
	public static String 头等舱铜牌礼包普通 = "头等舱铜牌礼包(普通)";
	public static String 头等舱击杀礼包普通 = "头等舱击杀礼包(普通)";
	public static String 头等舱参与礼包普通 = "头等舱参与礼包(普通)";
	public static String 船长室金牌礼包普通 = "船长室金牌礼包(普通)";
	public static String 船长室银牌礼包普通 = "船长室银牌礼包(普通)";
	public static String 船长室铜牌礼包普通 = "船长室铜牌礼包(普通)";
	public static String 船长室击杀礼包普通 = "船长室击杀礼包(普通)";
	public static String 船长室参与礼包普通 = "船长室参与礼包(普通)";
	public static String 头等舱金牌礼包优质 = "头等舱金牌礼包(优质)";
	public static String 头等舱银牌礼包优质 = "头等舱银牌礼包(优质)";
	public static String 头等舱铜牌礼包优质 = "头等舱铜牌礼包(优质)";
	public static String 头等舱击杀礼包优质 = "头等舱击杀礼包(优质)";
	public static String 头等舱参与礼包优质 = "头等舱参与礼包(优质)";
	public static String 船长室金牌礼包优质 = "船长室金牌礼包(优质)";
	public static String 船长室银牌礼包优质 = "船长室银牌礼包(优质)";
	public static String 船长室铜牌礼包优质 = "船长室铜牌礼包(优质)";
	public static String 船长室击杀礼包优质 = "船长室击杀礼包(优质)";
	public static String 船长室参与礼包优质 = "船长室参与礼包(优质)";
	public static String 头等舱金牌礼包顶级 = "头等舱金牌礼包(顶级)";
	public static String 头等舱银牌礼包顶级 = "头等舱银牌礼包(顶级)";
	public static String 头等舱铜牌礼包顶级 = "头等舱铜牌礼包(顶级)";
	public static String 头等舱击杀礼包顶级 = "头等舱击杀礼包(顶级)";
	public static String 头等舱参与礼包顶级 = "头等舱参与礼包(顶级)";
	public static String 船长室金牌礼包顶级 = "船长室金牌礼包(顶级)";
	public static String 船长室银牌礼包顶级 = "船长室银牌礼包(顶级)";
	public static String 船长室铜牌礼包顶级 = "船长室铜牌礼包(顶级)";
	public static String 船长室击杀礼包顶级 = "船长室击杀礼包(顶级)";
	public static String 船长室参与礼包顶级 = "船长室参与礼包(顶级)";
	public static String 圣诞袜子 = "圣诞袜子";
	public static String 圣诞Q宠饼干 = "圣诞Q宠饼干";
	public static String 圣诞礼盒 = "圣诞礼盒";
	public static String VIP1体验卡1天 = "VIP1体验卡(1天)";
	public static String VIP2体验卡1天 = "VIP2体验卡(1天)";
	public static String VIP3体验卡1天 = "VIP3体验卡(1天)";
	public static String VIP4体验卡1天 = "VIP4体验卡(1天)";
	public static String VIP5体验卡1天 = "VIP5体验卡(1天)";
	public static String VIP6体验卡1天 = "VIP6体验卡(1天)";
	public static String VIP7体验卡1天 = "VIP7体验卡(1天)";
	public static String 涅槃百宝囊 = "涅槃百宝囊";
	public static String 杀戮秘籍修罗 = "杀戮秘籍(修罗)";
	public static String 普度秘籍修罗 = "普度秘籍(修罗)";
	public static String 杀阵秘籍修罗 = "杀阵秘籍(修罗)";
	public static String 惊雷秘籍修罗 = "惊雷秘籍(修罗)";
	public static String 夺命秘籍修罗 = "夺命秘籍(修罗)";
	public static String 坠天秘籍仙心 = "坠天秘籍(仙心)";
	public static String 冰陵秘籍仙心 = "冰陵秘籍(仙心)";
	public static String 耀天秘籍仙心 = "耀天秘籍(仙心)";
	public static String 天诛秘籍仙心 = "天诛秘籍(仙心)";
	public static String 雪葬秘籍仙心 = "雪葬秘籍(仙心)";
	public static String 鬼幕秘籍影魅 = "鬼幕秘籍(影魅)";
	public static String 遁形秘籍影魅 = "遁形秘籍(影魅)";
	public static String 鬼刺秘籍影魅 = "鬼刺秘籍(影魅)";
	public static String 断喉秘籍影魅 = "断喉秘籍(影魅)";
	public static String 夺魂秘籍影魅 = "夺魂秘籍(影魅)";
	public static String 血祭秘籍九黎 = "血祭秘籍(九黎)";
	public static String 反噬秘籍九黎 = "反噬秘籍(九黎)";
	public static String 万蛊秘籍九黎 = "万蛊秘籍(九黎)";
	public static String 噬灵秘籍九黎 = "噬灵秘籍(九黎)";
	public static String 惊魂秘籍九黎 = "惊魂秘籍(九黎)";
	public static String 飞行坐骑碎片 = "飞行坐骑碎片";
	public static String 五品元气真丹初级 = "五品元气真丹(初级)";
	public static String 国探 = "国探";
	public static String 零文 = "0文";
	public static String 比武奖励银子1阶 = "银子：5锭";
	public static String 比武奖励银子2阶 = "银子：3.33锭";
	public static String 比武奖励银子3阶 = "银子：2.22锭";
	public static String 比武奖励银子4阶 = "银子：1.5锭~1锭";
	public static String 比武奖励银子5阶 = "银子：987两~691两";
	public static String 比武奖励银子6阶 = "银子：658两~446两";
	public static String 比武奖励银子7阶 = "银子：438两~223两";
	public static String 比武奖励银子8阶 = "银子：219两~110两";
	public static String 比武奖励银子9阶 = "银子：109两~55两";
	public static String 比武奖励银子10阶 = "银子：54两~27两";
	public static String 比武奖励银子11阶 = "银子：27两~5两";
	public static String 比武奖励银子12阶 = "银子：5两";
	public static String 装备碎了 = "装备碎了";

	public static String 恭喜你激活元神获得坐骑 = "恭喜你激活元神,获得坐骑@STRING_1@";
	public static String 恭喜你激活元神获得坐骑速去查看邮件 = "恭喜你激活元神,获得坐骑@STRING_1@,速去查看邮件";
	public static String 您已经进入疲劳期杀怪收益开始降低 = "您已经进入疲劳期，杀怪收益开始降低.";
	public static String 您已经进入疲劳期杀怪收益持续降低中 = "您已经进入疲劳期，杀怪收益持续降低中...";
	public static String 您已经进入疲劳期杀怪收益会越来越低休息会吧 = "您已经进入疲劳期，杀怪收益会越来越低，休息会吧";
	public static String 您已经进入疲劳期很久了杀怪收益降低已经很多了建议您去做一些活动 = "您已经进入疲劳期很久了，杀怪收益降低已经很多了，建议您去做一些活动";
	public static String 您已经进入疲劳期很久了杀怪快要没有收益了 = "您已经进入疲劳期很久了，杀怪快要没有收益了......";
	public static String 别打了杀怪没经验了本游戏活动很多建议您去做一些活动 = "别打了，杀怪没经验了，本游戏活动很多，建议您去做一些活动";
	public static String 伟大的到达150级开启了封印赐福 = "伟大的@STRING_1@到达150级开启了封印赐福！全服范围内等级未达110级的玩家将获得由@STRING_1@带给你们的封印赐福效果！";
	public static String 由于您到达了110级因此赐福BUFF效果消失 = "由于您到达了110级，因此赐福BUFF效果消失";
	public static String 某物品到期删除了 = "[@STRING_1@]到期删除了";
	public static String 某装备时间到期自动脱掉装备 = "@STRING_1@时间到期，自动脱掉装备";
	public static String 温馨提示您设置了屏蔽选项包括 = "温馨提示：您设置了屏蔽选项，包括";
	public static String 屏蔽所有玩家 = "【屏蔽所有玩家】";
	public static String 屏蔽本国玩家 = "【屏蔽本国玩家】";
	public static String 屏蔽聊天信息 = "【屏蔽聊天信息】";
	public static String 屏蔽交易信息 = "【屏蔽交易信息】";
	public static String 屏蔽繁殖信息 = "【屏蔽繁殖信息】";
	public static String 屏蔽交换信息 = "【屏蔽交换信息】";
	public static String 屏蔽组队信息 = "【屏蔽聊天信息】";
	public static String 您可以在设置中来修改 = "，您可以在【设置】中来修改。";
	public static String 剩余时间 = "剩余时间";
	public static String 红孩儿 = "红孩儿";
	public static String 舜 = "舜";
	public static String 如花 = "如花";
	public static String 红孩儿元神 = "红孩儿元神";
	public static String 舜元神 = "舜元神";
	public static String 如花元神 = "如花元神";
	public static String 孟婆 = "孟婆";
	public static String 孟婆元神 = "孟婆元神";
	public static String 十连斩 = "十连斩";
	public static String 五十连斩 = "五十连斩";
	public static String 一百连斩 = "一百连斩";
	public static String 一百五十连斩 = "一百五十连斩";
	public static String 二百连斩 = "二百连斩";
	public static String 三百连斩 = "三百连斩";
	public static String 四百连斩 = "四百连斩";
	public static String 五百连斩 = "五百连斩";
	public static String 六百连斩 = "六百连斩";
	public static String 七百连斩 = "七百连斩";
	public static String 八百连斩 = "八百连斩";
	public static String 九百连斩 = "九百连斩";
	public static String 千人斩 = "千人斩";
	public static String 万人斩 = "万人斩";
	public static String 十万人斩 = "十万人斩";
	public static String 百万人斩 = "百万人斩";
	public static String 千万人斩 = "千万人斩";
	public static String 亿人斩 = "亿人斩";

	public static String 礼花 = "礼花";
	public static String 恭喜获得物品 = "恭喜你获得物品@STRING_1@ X @COUNT_1@";

	public static String 心动之恋 = "心动之恋";
	public static String 水晶之恋 = "水晶之恋";
	public static String 倾城之恋 = "倾城之恋";
	public static String 同性不能赠送 = "同性不能赠送";
	public static String 不在线啊 = "@STRING_1@不在线";
	public static String 不是好友 = "@STRING_1@不是你的好友";
	public static String 心动之恋提示 = "<f color='0xFFFF00'>@STRING_1@</f>将<f color='0x0000FF'>心动之恋</f>送给了心爱的<f color='0xFFFF00'>@STRING_2@</f>，恭喜他们获得珍贵的情人节称号！";
	public static String 水晶之恋提示 = "<f color='0xFFFF00'>@STRING_1@</f>将<f color='0xE706EA'>水晶之恋</f>送给了心爱的<f color='0xFFFF00'>@STRING_2@</f>，恭喜他们纯如水晶的爱情得到了见证！";
	public static String 倾城之恋提示 = "<f color='0xFFFF00'>@STRING_1@</f>将<f color='0xFDA700'>倾城之恋</f>送给了心爱的<f color='0xFFFF00'>@STRING_2@</f>，这段倾国倾城的爱情，将永志不渝，千古传诵！";

	public static String 纯爱感言提示 = "<f color='0xFFFF00'>@STRING_1@</f>将<f color='0x0000FF'>【纯爱感言】</f>送给了心爱的<f color='0xFFFF00'>@STRING_2@</f>，他们纯洁的爱情意绵绵!";
	public static String 真爱宣言提示 = "<f color='0xFFFF00'>@STRING_1@</f>将<f color='0xE706EA'>【真爱宣言】</f>送给了心爱的<f color='0xFFFF00'>@STRING_2@</f>，他们无敌的真爱情深似海！";
	public static String 挚爱箴言提示 = "<f color='0xFFFF00'>@STRING_1@</f>将<f color='0xFDA700'>【挚爱箴言】</f>送给了心爱的<f color='0xFFFF00'>@STRING_2@</f>，他们情动天地感动了整个仙界！";

	public static String 年兽提示 = "年兽横行，现正在佳梦关霍乱人间，各路仙家快来消灭这妖孽！更有多种惊喜掉落，快来佳梦关灭妖吧！";

	public static String 物品不存在提示 = "物品不存在:@STRING_1@";
	public static String 物品不存在提示1 = "开启宝箱还缺少1枚@STRING_1@";

	public static String 兑换活动奖励 = "兑换活动奖励";

	public static String 获得兑换奖励 = "恭喜您兑换了@STRING_1@，物品已发送至邮箱内，请查看您的邮箱";

	public static String 标志像 = "标志像";

	public static String 鉴定已经完美了 = "鉴定已经完美了。";
	public static String 传奇鉴定符 = "传奇●鉴定符";
	public static String 传说鉴定符 = "传说●鉴定符";
	public static String 神话鉴定符 = "神话●鉴定符";
	public static String 永恒鉴定符 = "永恒●鉴定符";
	public static String 传奇 = "传奇";
	public static String 传说 = "传说";
	public static String 神话 = "神话";
	public static String 永恒 = "永恒";
	public static String 新鉴定说明1 = "<f color='0x0FD100'>装备鉴定品质达到完美时可使用该功能提升装备鉴定更高品质，提升基础属性加成</f>";
	public static String 新鉴定说明2 = "<f color='0x0FD100'>鉴定说明</f>：\n1.鉴定<f color='0xFF6000'>传奇</f>、<f color='0xFC3203'>传说</f>、<f color='0x6C03FC'>神话</f>、<f color='0xFF0000'>永恒</f>四个品阶时需要相应品阶的<f color='0x0FD100'>鉴定符</f>\n2.每次鉴定至少提升当前品阶<f color='0xFF0000'>1%属性加成</f>，最多提升该品阶属性加成<f color='0xFF0000'>上限</f>\n<f color='0x0FD100'>品阶升级说明</f>：\n1.当前品阶达到属性加成<f color='0xFF0000'>上限</f>时方可<f color='0xFF0000'>升级下个品阶</f>";
	public static String 鉴定确定绑定 = "您使用的绑定的鉴定符，鉴定会导致装备绑定，您确定吗?";
	public static String 非完美不能鉴定 = "您的装备不是完美品质，不能高级鉴定。";
	public static String 鉴定符不匹配 = "鉴定符不匹配";
	public static String 鉴定到最高 = "鉴定到最高";
	public static String 累计某种属性增加 = "累计@STRING_1@增加@COUNT_1@";
	public static String 某种属性增加 = "@STRING_1@增加@COUNT_1@";
	public static String 新心法等级限制提示 = "111级才能开启注魂功能";
	public static String 新心法只能学3个 = "注魂只能学3个";
	public static String 新新心法只能学10个 = "心法最多只能学10个";
	public static String 上一次心法没学满 = "上一层心法没学满";
	public static String 天劫未通过 = "通过天劫层数不足以学习下级心法";
	public static String 飞升心法 = "需要飞升以后才可以学习后续心法";

	public static String 合服功能关闭提示 = "将要合服，此功能暂时关闭。";

	/************************************************************************/
	/*************************** 2013-5-5韩服新增翻译 **************************/
	/************************************************************************/
	public static String 元神提示 = "<f color='0x33CC00' size='28'>元神提示：</f><f size='28'>成功开启元神后，本尊将获得</f>\n<f size='28'>元神</f><f color='0xFF0000' size='28'>20%</f><f size='28'>所有属性提升(元神出窍后元神</f>\n<f size='28'>同样获得本尊</f><f color='0xFF0000' size='28'>20%</f><f size='28'>所有属性提升)</f>";

	public static String 元神提示_元神 = "<f color='0x33CC00' size='16'>提示：</f><f size='16'>当前元神属性的</f><f color='0xFF0000' size='16'>@COUNT_1@%</f><f size='16'>作为本尊属性加成，</f>\n<f size='16'>下级元神属性的</f><f color='0xFF0000' size='16'>@COUNT_2@%</f><f size='16'>作为本尊属性加成。</f>";
	public static String 元神提示_元神_满 = "<f color='0x33CC00' size='16'>提示：</f><f size='16'>当前元神属性的</f><f color='0xFF0000' size='16'>@COUNT_1@%</f><f size='16'>作为本尊属性加成.</f>";
	public static String 元神提示_本尊 = "<f color='0x33CC00' size='16'>提示：</f><f size='16'>当前本尊属性的</f><f color='0xFF0000' size='16'>@COUNT_1@%</f><f size='16'>作为元神属性加成，</f>\n<f size='16'>下级本尊属性的</f><f color='0xFF0000' size='16'>@COUNT_2@%</f><f size='16'>作为元神属性加成。</f>";
	public static String 元神提示_本尊_满 = "<f color='0x33CC00' size='16'>提示：</f><f size='16'>当前本尊属性的</f><f color='0xFF0000' size='16'>@COUNT_1@%</f><f size='16'>作为元神属性加成.</f>";
	public static String 元神提示_未开启 = "<f color='0x33CC00' size='28'>提示：</f><f size='28'>本尊110级开启元神进阶功能。</f>";
	public static String 元神提示_元神为空 = "<f color='0x33CC00' size='28'>元神提示：</f><f size='28'>成功开启元神后，本尊将获得</f>\n<f size='28'>元神</f><f color='0xFF0000' size='28'>20%</f><f size='28'>所有属性提升(元神出窍后元神</f>\n<f size='28'>同样获得本尊</f><f color='0xFF0000' size='28'>20%</f><f size='28'>所有属性提升)</f>";
	public static String 恭喜您属性升级成功 = "恭喜您属性升级成功";
	public static String 属性升级失败 = "属性升级失败，请联系GM解决！";
	public static String 背包已满 = "背包已满，奖励已通过邮件发送，请注意查收。";
	public static String 初识元神 = "<f size='15'>初识元神</f>\n<f size='15'>60级20%</f>";
	public static String 心领神会 = "<f color='0x00fff00' size='15'>心领神会</f>\n<f size='15'>110级25%</f>";
	public static String 天衣无缝 = "<f color='0x05D7FD' size='15'>天衣无缝</f>\n<f size='15'>150级30%</f>";
	public static String 登峰造极 = "<f color='0xffE706EA' size='15'>登峰造极</f>\n<f size='15'>190级35%</f>";
	public static String 出神入化 = "<f color='0xffFDA700' size='15'>出神入化</f>\n<f size='15'>220级40%</f>";
	public static String 别有天地 = "<f color='0xffFDA700' size='15'>别有天地</f>\n<f size='15'>40%</f>";
	public static String 金丹换骨 = "<f color='0xffFDA700' size='15'>金丹换骨</f>\n<f size='15'>45%</f>";
	public static String 超凡入圣 = "<f color='0xffFDA700' size='15'>超凡入圣</f>\n<f size='15'>50%</f>";
	public static String 玄妙修神 = "<f color='0xffFDA700' size='15'>玄妙修神</f>\n<f size='15'>55%</f>";
	public static String 一念通天 = "<f color='0xffFDA700' size='15'>一念通天</f>\n<f size='15'>60%</f>";

	public static String 充值 = "充值";
	public static String 修改宗派宗旨 = "修改宗派宗旨";
	public static String 修改宗派宗旨_提示 = "修改宗派宗旨，当前宗旨：@STRING_1@";
	public static String 家族解散成功 = "家族解散成功!";
	public static String 挖墙脚等红杏 = "挖墙脚,等红杏";
	public static String 尚未开启 = "尚未开启";
	public static String 道不存在 = "道不存在";
	public static String 你已经参加了该活动 = "你已经参加了该活动";
	public static String 老用户积分福利 = "老用户积分福利";
	public static String 老用户积分内容 = "亲爱的用户，感谢您对飘渺寻仙曲长久以来的热爱与信赖，本次推出积分玩法，您目前的VIP等级是@STRING_1@，将获得@STRING_2@积分赠送体验福利。积分有效时间为365天，365天后所剩积分将会扣除50%。再次祝您游戏愉快！";
	public static String 恭喜获得老用户积分 = "亲爱的VIP玩家，恭喜您获得了游戏充值返还积分,您可以去积分商城购买积分商品！";
	public static String 级才能发布求购 = "20级才能发布求购";
	public static String 您确定发布此求购信息 = "您确定发布此求购信息";

	public static String 完成了建设任务当前进度 = "完成了建设任务,当前进度:";
	public static String 无宗派占领城市 = "无宗派占领城市";
	public static String 无宗派申请攻打城市 = "无宗派申请攻打城市";
	public static String 很抱歉您没有宗派无法为您找回密码 = "很抱歉，您没有宗派，无法为您找回密码。";
	public static String 很抱歉您非此宗派宗主无法为您找回密码 = "很抱歉，您非此宗派宗主，无法为您找回密码。";
	public static String 很抱歉您的密保答案错误请您重新填写 = "很抱歉，您的密保答案错误，请您重新填写!";
	public static String 您没有仓库密码 = "您没有仓库密码!";
	public static String 您的仓库密码是 = "您的仓库密码是:";
	public static String 很抱歉您家族没有密码 = "很抱歉，您家族没有密码，";
	public static String 很抱歉您没有家族无法为您找回密码 = "很抱歉，您没有家族，无法为您找回密码。";
	public static String 您的宗派密码是 = "您的宗派密码是:";
	public static String 很抱歉您非此家族族长无法为您找回密码 = "很抱歉，您非此家族族长，无法为您找回密码。";
	public static String 您的家族密码是 = "您的家族密码是:";
	public static String 请输入密保答案 = "请输入密保答案!";
	public static String 您还未设置密保 = "您还未设置密保！";
	public static String 您不在国战中 = "您不在国战中";
	public static String 您没有权利发布命令 = "您没有权利发布命令";
	public static String 国战资源不足 = "国战资源不足";
	public static String 您没有延时的权利 = "您没有延时的权利";
	public static String 延时次数已用完 = "延时次数已用完";
	public static String 您没有拉人权限 = "您没有拉人权限";
	public static String 拉人次数已用完您可以用道具行使额外拉人权利 = "拉人次数已用完，您可以用道具行使额外拉人权利";
	public static String 国战资源不足无法再拉人 = "国战资源不足，无法再拉人";
	public static String 治疗次数已用完 = "治疗次数已用完";
	public static String 您没有权利治疗 = "您没有权利治疗";
	public static String 暂无 = "暂无";
	public static String 没有上榜 = "没有上榜";
	public static String 第X名 = "第@COUNT_1@名";
	public static String[] 答题提示 = { "您需要正确回答以下问题", "您需要正确输入下面图片内容(不区分大小写)" };
	public static String 真实姓名 = "姓名";
	public static String 电话 = "联系电话";
	public static String QQ = "QQ";
	public static String 生日 = "生日";
	public static String 宝珠 = "宝珠";
	public static String 请更新最新的客户端再强化 = "请更新最新的客户端再强化!";

	public static String 不能继续回复 = "亲爱的玩家，关闭或者评分状态下不能继续回复！";
	public static String 反馈服务器返回失败 = "反馈服务器返回【玩家说话信息】失败！";
	public static String 内容不合法 = "您输入的内容不合法！";
	public static String 玩家评分失败 = "反馈服务器返回【玩家评分的信息】失败！";
	public static String 评分成功 = "评分成功，感谢您对飘渺寻仙曲的支持！";
	public static String 查看成功 = "反馈服务器返回【查看反馈的信息】失败！";
	public static String 查看评分状态 = "反馈服务器返回【查看评分状态的反馈的信息】失败！";
	public static String 提交新反馈信息 = "反馈服务器返回【提交新反馈信息】失败！";
	public static String 删除反馈信息 = "反馈服务器返回【删除反馈信息】失败！";
	public static String 首界面信息 = "反馈服务器返回【首界面信息】失败！";
	public static String 亲爱的玩家您好 = "亲爱的玩家您好";
	public static String 进行评价 = "感谢您使用GM反馈系统，请对为您服务的GM进行评价！";
	public static String 详细描述 = "亲爱的玩家您好，请详细描述您遇到的问题！";
	public static String 联系GM = "提交新反馈出错,通过邮件 sky@koramgame.com 联系GM！";
	public static String 删除成功 = "删除成功";
	public static String 当前排队问题数 = "当前排队问题数：";
	public static String 亲爱的玩家 = "亲爱的玩家您好！欢迎您使用飘渺寻仙曲客服反馈系统,您所提交的问题将加入至提交队列中排队，请您耐心等待.";

	public static String 第1天 = "第1天";
	public static String 第2天 = "第2天";
	public static String 第3天 = "第3天";
	public static String 第4天 = "第4天";
	public static String 第5天 = "第5天";
	public static String 第6天 = "第6天";
	public static String 第7天 = "第7天";
	public static String 用于兑换连登奖励 = "用于兑换连登奖励";
	public static String 用于获得10两银票 = "用于获得10两银票。";
	public static String 恭喜您获得了银块 = "恭喜您获得了银块";
	public static String 连登令旗 = "连登令旗";
	public static String 恭喜您获得了连登令旗 = "恭喜您获得了连登令旗";
	public static String 连续登录 = "连续登录@STRING_1@天";
	public static String 达到7日连登后 = "达到7日连登后，再连续登录，每天都可以领取最高奖励。\n奖励在【昆华古城】或【昆仑圣殿】连登使者处兑换。";
	public static String 连登活动奖励 = "连登活动奖励";
	public static String 活动期间 = "活动期间，只要玩家连续登陆天数符合要求，就可以领取到对应的奖励";

	public static String 圣兽阁余额不足 = "所需银子不足，不可进入圣兽阁!";
	public static String 迷城1只宠物提示 = "@STRING_1@被打开了，出现了一只宠物。";
	public static String 迷城2只宠物提示 = "@STRING_1@被打开后从中窜出了二只宠物。";
	public static String 迷城开启获得钥匙提示 = "箱子被打开了，恭喜您获得一把钥匙。";
	public static String 迷城开启获得锤子提示 = "罐子被打开了，恭喜您获得一把锤子。";
	public static String 进入迷城等级限制 = "您的等级不足，不可进入圣兽阁。";
	public static String 进入迷城提示 = "您需要花费@STRING_1@两进入@STRING_2@级的@STRING_3@,是否进入？";
	public static String 免费进入提示 = "马上进入？";
	public static String 进入迷城消耗券提示 = "您是否消耗背包中的[@STRING_1@]*1进入？";
	public static String 普通迷城 = "普通圣兽阁";
	public static String 豪华迷城 = "豪华圣兽阁";
	public static String 至尊迷城 = "至尊圣兽阁";
	public static String 打开箱子 = "正在打开箱子..";
	public static String 钥匙不足 = "圣兽阁钥匙不足，不能开启箱子！";
	public static String 锤子不足 = "圣兽阁锤子不足，不能开启罐子！";
	public static String 暂未开放 = "该圣兽阁暂未开放！";
	public static String 迷城不能使用回城 = "圣兽阁中不能使用回城！";
	public static String 锤子 = "锤子";
	public static String 钥匙 = "钥匙";
	public static String 罐子 = "罐子";
	public static String 数量 = "数量";
	public static String 宝箱 = "宝箱";
	public static String 倒计时 = "倒计时";
	public static String 圣兽阁体验券普 = "圣兽阁体验券(普)";
	public static String 圣兽阁体验券豪 = "圣兽阁体验券(豪）";
	public static String 圣兽阁体验券至 = "圣兽阁体验券(至)";
	public static String 圣兽阁中不能使用混元至圣 = "圣兽阁中不能使用混元至圣圣印";
	public static String 战场中不能使用混元至圣 = "鲜血试炼中不能使用此令牌";
	public static String 操作过快 = "您操作过快..";
	public static String 消耗了迷城体验卷 = "进入圣兽阁成功，您消耗了@STRING_1@";

	public static String 永久公告类型 = "永久公告类型";
	public static String 永久公告类型标题 = "永久公告类型标题";
	public static String 永久公告类型内容 = "永久公告类型内容";
	public static String 永久公告为空提示 = "没有可查看的公告！";

	public static String xxx的仙府 = "@STRING_1@ 的仙府";

	public static String 兑换成功 = "兑换成功";
	public static String 无效验证码 = "无效验证码";
	public static String 兑换次数过多 = "兑换次数过多";
	public static String 活动已过期 = "活动已过期";
	public static String 渠道不符合 = "渠道不符合";
	public static String 活动不匹配 = "活动不匹配";
	public static String 答题提示_a = "才能接取任务,并获得额外经验";

	public static String 打金网络限制提示 = "您由于打金工作室行为破坏游戏平衡而被限制本操作，如有疑问请联系GM";
	public static String VIP不用排队 = "您正在享受VIP服务无需排队";

	public static String[] tumotieName = new String[] { "封魔录●降魔(白)", "封魔录●降魔(绿)", "封魔录●降魔(蓝)", "封魔录●降魔(紫)", "封魔录●降魔(橙)", "封魔录●逍遥(白)", "封魔录●逍遥(绿)", "封魔录●逍遥(蓝)", "封魔录●逍遥(紫)", "封魔录●逍遥(橙)", "封魔录●霸者(白)", "封魔录●霸者(绿)", "封魔录●霸者(蓝)", "封魔录●霸者(紫)", "封魔录●霸者(橙)", "封魔录●朱雀(白)", "封魔录●朱雀(绿)", "封魔录●朱雀(蓝)", "封魔录●朱雀(紫)", "封魔录●朱雀(橙)", "封魔录●水晶(白)", "封魔录●水晶(绿)", "封魔录●水晶(蓝)", "封魔录●水晶(紫)", "封魔录●水晶(橙)", "封魔录●倚天(白)", "封魔录●倚天(绿)", "封魔录●倚天(蓝)", "封魔录●倚天(紫)", "封魔录●倚天(橙)", "封魔录●青虹(白)", "封魔录●青虹(绿)", "封魔录●青虹(蓝)", "封魔录●青虹(紫)", "封魔录●青虹(橙)", "封魔录●赤霄(白)", "封魔录●赤霄(绿)", "封魔录●赤霄(蓝)", "封魔录●赤霄(紫)", "封魔录●赤霄(橙)", "封魔录●震天(白)", "封魔录●震天(绿)", "封魔录●震天(蓝)", "封魔录●震天(紫)", "封魔录●震天(橙)", "封魔录●天罡(白)", "封魔录●天罡(绿)", "封魔录●天罡(蓝)", "封魔录●天罡(紫)", "封魔录●天罡(橙)", };

	public static String 需要 = "需要@STRING_1@";

	public static String 宠物寿命不足提示 = "亲您的宠物寿命点要没了，寿命小于0就不能出战了。赶快给你的宠物来点万兽丹啊，万兽丹商城有售哦！";

	public static String 橙色果子熟了 = "<f color='#@STRING_1@'>恭喜@STRING_2@ 的 @STRING_3@ 种出了@STRING_4@果实,手痒了么?</f>";

	public static String 很可惜你回答错误开动脑筋再想想 = "很可惜你回答错误,开动脑筋再想想";
	public static String 恭喜你回答正确获得了xx经验 = "恭喜你回答正确,获得了@COUNT_1@经验";
	public static String 您在XX的银子金额异常增长我们需要人工核实增长原因 = "您在@STRING_1@的银子金额异常增长,我们需要人工核实增长原因。";
	public static String 您此账号存在异常情况被临时封号 = "您此账号存在异常情况，需要人工核实，暂时不能登录。";
	public static String 防御成功 = "防御成功";
	public static String 攻城成功 = "攻城成功";
	public static String text_年月日 = "yyyy年MM月dd日";
	public static String 谁送给了谁多少朵什么花 = "@STRING_1@送给了@STRING_2@@COUNT_1@@STRING_3@@STRING_4@";
	public static String 您在XX的银子金额异常增长可以通过邮件toususqagecom投诉 = "您在@STRING_1@的银子金额异常增长。可以通过邮件(service@uaigames.com)投诉。";
	public static String 召唤大家去他身边 = "召唤大家去他身边!";
	public static String 昨 = "昨";
	public static String 较小几率 = "较小几率";
	public static String 一般几率 = "一般几率";
	public static String 较大几率 = "较大几率";
	public static String 很大几率 = "很大几率";
	public static String NOT_SYS_MAIL = "</f>\n<f color='0xff0000'>这不是飘渺寻仙曲官方邮件。请勿轻信中奖、返利、密保信息。警惕冒充官方的QQ、电话或网站。";
	public static String 喝橙酒广播 = "<f color= '0xff0000ff'>@STRING_1@的@STRING_2@</f><f color= '0xffFDA700'>开怀畅饮</f><f color= '0xffFDA700'>@STRING_3@</f><f color= '0xffFDA700'>，微醺间恍若进入仙境</f>";
	public static String 邮件标题或内容含有非法信息 = "邮件标题或内容含有非法信息";
	public static String 邮件格式错误缺少发件人收件人或者标题 = "邮件格式错误，缺少发件人、收件人或者标题";
	public static String 寻人提示_你已经找过我一次了 = "你已经找过我一次了，找我再多次也是没有用的";

	public static String 密保问题[] = { "您父亲的名字", "您母亲的名字", "您最爱人的名字", "您小学的名字", "您手机号的后四位", "您的出生日期", "您的真实姓名" };

	public static String 输入的新问题或新答案错误 = "输入的新问题或新答案错误";
	public static String 输入的新密码过长请重新输入 = "输入的新密码过长,请重新输入";
	public static String 输入的旧问题或旧答案错误 = "输入的旧问题或旧答案错误";
	public static String 新问题错误 = "新问题错误";
	public static String 找回仓库密码 = "找回仓库密码";
	public static String 找回战队密码 = "找回战队密码";
	public static String 输入密保提示 = "请输入您的密保答案\n密保问题:";
	public static String 找回家族密码 = "找回家族密码";
	public static String 您已经设置过密保不能再设置 = "您已经设置过密保，不能再设置！";

	public static String 刺探情报 = "刺探情报";

	public static String 镖车公告 = "恭喜@STRING_1@ 的 @STRING_2@得到了@STRING_3@镖车!";

	public static String 普通锁魂说明 = "普通锁魂:锁魂后不可交易，出售，丢弃，邮寄，摆摊，拍卖，不可被合成，吞噬。\n高级锁定:物品与宠物锁定后可正常使用和出战外，不可进行任何其他操作\n可锁魂物品: 装备、5级以上宝石、千载难逢宠物蛋。";
	public static String 解魂说明 = "物品解魂需要24小时后成功";
	public static String 确定锁魂 = "确定锁魂";
	public static String 确定锁魂普通 = "您确定要将@STRING_1@进行锁魂吗？锁魂后<f color='#f7e354'>此物品将进入保护状态，不可交易，出售，丢弃，邮寄，摆摊，拍卖，不可被合成，吞噬。！！！</f>";
	public static String 确定锁魂高级 = "您确定要将@STRING_1@进行高级锁魂吗？高级锁魂后<f color='#f7e354'>此物品将进入冰冻状态，不能进行除使用外的一切操作！！！</f>";
	public static String 确定解魂 = "确定解魂";
	public static String 您确定解魂 = "您确定解魂吗?";
	public static String 你的镖车正在遭受攻击 = "你的镖车正在遭受攻击!";
	public static String 是否购买绑定商品 = "此商品是绑定的,是否购买？";
	public static String 商品刷新 = "商品已经刷新，请重新打开商店购买";

	public static String 你的背包空间不足请整理后再使用 = "你的背包空间不足,请整理后再使用！";

	public static String 商店名字_历练兑换 = "历练兑换";
	public static String 千层塔领取首次领取奖励成功 = "幽冥幻域领取奖励成功，请去背包查收。";
	public static String 千层塔困难暂未开启 = "幽冥幻域困难，深渊模式暂不开放。";
	public static String 千层塔深渊暂未开启 = "幽冥幻域深渊模式暂不开放。";
	public static String 答题标题 = "反外挂答题";
	public static String 答题错误次数过多封号提示 = "反外挂答题错误或避而不答，请勿使用第三方软件。";
	public static String[] 答题Names = new String[] { "图片", "动画", "光效", "文字" };
	public static String 请选择下列是XX的选项 = "请选择下列是@STRING_1@的选项";
	public static String 请选择下列不是XX的选项 = "请选择下列不是@STRING_1@的选项";
	public static String 一致性答题说明 = "请选择下列<f color='0xffff00'>@STRING_1@</f>中相同的两个";
	public static String 经验扣除说明疲劳 = "获得经验@COUNT_1@(疲劳-@COUNT_2@)";
	public static String 经验扣除说明PK = "获得经验@COUNT_1@(恶名-@COUNT_2@)";
	public static String 经验扣除说明PK疲劳 = "获得经验@COUNT_1@(恶名-@COUNT_2@  疲劳-@COUNT_3@)";
	public static String 经验到0提示_1 = "由于疲劳和恶名值过高不获得经验。";
	public static String 经验到0提示_2 = "由于疲劳过高，今天不再获得经验。";
	public static String 经验说明 = "获得经验@COUNT_1@";
	public static String 您正在战斗状态中不能交换 = "您正在战斗状态中不能交换宝物。";
	public static String 对方正在战斗状态中不能交换 = "对方正在战斗状态中不能交换宝物。";
	public static String 您还未充值达到领取奖励要求 = "您还未充值达到领取奖励要求";
	public static String 您已经领取过此充值奖励 = "您已经领取过此充值奖励";
	public static String 您背包空间不够 = "您背包空间不够";
	public static String 领取奖励失败 = "领取奖励失败";
	public static String 购买奖励失败 = "购买奖励失败";
	public static String 恭喜您参与周累计充值活动领取奖励 = "恭喜您参与周累计充值活动,领取奖励成功，请去邮件查收。";
	public static String 恭喜您参与周累计充值活动购买奖励 = "恭喜您参与周累计充值活动,购买奖励成功，请去背包查收。";
	public static String 恭喜您参与月累计充值活动领取奖励 = "恭喜您参与月累计充值活动,领取奖励成功，请去邮件查收。";
	public static String 恭喜您参与月累计充值活动购买奖励 = "恭喜您参与月累计充值活动,购买奖励成功，请去背包查收。";
	public static String 恭喜玩家领取周月活动奖励 = "恭喜玩家@STRING_1@,领取@STRING_2@回馈奖励获得@STRING_3@";
	public static String 恭喜玩家购买周月活动奖励 = "恭喜玩家@STRING_1@,在@STRING_2@累计充值宝库中购买到超值道具@STRING_3@";
	public static String 周 = "周";

	public static String 恭喜你享受折扣 = "恭喜你享受折扣";
	public static String 折扣提示 = "是否消耗1个@STRING_1@ 享受折扣，原价@STRING_2@,打折后价格:@STRING_3@";

	public static String 召唤提示 = "只能在以下地图召唤:@STRING_1@";

	public static String 接取了家族押镖不能离开家族 = "接取了家族押镖不能离开家族";
	public static String 非宗主不能报名 = "您不是宗主不能报名";
	public static String 功能暂未开放 = "功能暂未开放，敬请期待";

	public static String 输入内容太长 = "你输入的内容太长，请重新输入";
	public static String 输入内容非法 = "你输入的内容非法，请重新输入";

	public static String 领奖邮件标题 = "活跃度@STRING_1@奖励";
	public static String 第一档邮件内容 = "恭喜您获得了活跃度@STRING_1@的奖励，奖励为：</f><f color='@STRING_2@'>@STRING_3@</f><f color='0xffffff'>，希望您继续努力，奖励多多，机会多多。";
	public static String 最后档邮件内容 = "恭喜您获得了活跃度@STRING_1@的奖励，奖励为：</f><f color='@STRING_2@'>@STRING_3@</f><f color='0xffffff'>，恭喜您，今日的终极大奖您已获得囊中，请继续加油。";
	public static String 倒数第二档邮件内容 = "恭喜您获得了活跃度@STRING_1@的奖励，奖励为：</f><f color='@STRING_2@'>@STRING_3@</f><f color='0xffffff'>，距离下次领奖还有@STRING_4@活跃度，请您继续努力，距离终极大奖就差一步了。";
	public static String 中间档邮件内容 = "恭喜您获得了活跃度@STRING_1@的奖励，奖励为：</f><f color='@STRING_2@'>@STRING_3@</f><f color='0xffffff'>，距离下次领奖还有@STRING_4@活跃度，请您继续努力。";
	public static String 活跃度领奖提示 = "亲爱的仙友，您领取了活跃度@STRING_1@的奖励，奖励为：<f color='@STRING_2@'>@STRING_3@*1</f>";
	public static String 领奖活跃度不足 = "亲爱的仙友，您现有的活跃度积分不满足领取条件，请继续加油！";
	public static String 抽奖邮件标题 = "活跃度大抽奖";
	public static String 抽奖邮件内容 = "恭喜您在活跃度大抽奖中抽到了</f>@STRING_1@<f color='0xffffff'>，手气不错哟，还有更多稀有道具等着您哟！";
	public static String 抽奖活跃度不足 = "亲爱的仙友，当您的积分达到60、80、100时，才可以进行活跃度大抽奖。";
	public static String 获得抽奖资格 = "恭喜您，在领取当前档位宝箱的同时，获得一次活跃度大抽奖的 机会，祝您好运！";
	public static String 今日抽奖次数已满 = "亲爱的仙友，您今日的抽奖次数已用尽，希望您明日的运气会更好。";
	public static String 当前抽奖次数已满 = "亲爱的仙友，您当前的抽奖次数已用尽，请您继续努力。";
	public static String 无NPC不能寻路 = "亲爱的仙友，此活动无需NPC，可自行完成此活动！";
	public static String 体力值不足 = "亲爱的仙友，您可能等级不足40级或身上的体力值不足10点，如您想继续完成，请在VIP商城购买体力丹(其他商城无法购买)";
	public static String 副本 = "奇迹副本";
	public static String 除妖 = "除妖";
	public static String 不在时间段 = "亲爱的仙友，此活动未到达活动时间，请您详细关注活动开启时间！";
	public static String 已签过到了 = "今天已经签过到了";
	public static String 签到领奖邮件标题 = "签到@STRING_1@天奖励";
	public static String 签到第一档邮件内容 = "恭喜您获得了签到@STRING_1@天的奖励，奖励为：</f><f color='@STRING_2@'>@STRING_3@</f><f color='0xffffff'>，希望您继续努力，奖励多多，机会多多。";
	public static String 签到最后档邮件内容 = "恭喜您获得了签到@STRING_1@天的奖励，奖励为：</f><f color='@STRING_2@'>@STRING_3@</f><f color='0xffffff'>，恭喜您，这个月的终极大奖您已获得囊中，请继续加油。";
	public static String 签到倒数第二档邮件内容 = "恭喜您获得了签到@STRING_1@天的奖励，奖励为：</f><f color='@STRING_2@'>@STRING_3@</f><f color='0xffffff'>，距离下次领奖还有@STRING_4@天，请您继续努力，距离终极大奖就差一步了。";
	public static String 签到中间档邮件内容 = "恭喜您获得了签到@STRING_1@天的奖励，奖励为：</f><f color='@STRING_2@'>@STRING_3@</f><f color='0xffffff'>，距离下次领奖还有@STRING_4@天，请您继续努力。";
	public static String 签到领奖提示 = "亲爱的仙友，您领取了签到@STRING_1@天的奖励，奖励为：<f color='@STRING_2@'>@STRING_3@*1</f>";
	public static String 签到天数不足 = "亲爱的仙友，您本月的签到天数不满足领取条件，请继续加油！";

	public static String 挖宝 = "挖宝";
	public static String 天录传送符 = "天录传送符";
	public static String 挖宝前往 = "是否前往@STRING_1@";
	public static String 中立刷怪 = "@STRING_1@在寻宝中遇到了影无踪，现正在@STRING_2@地图中逃窜，请各路仙人速去击杀！";
	public static String 兑换寻天宝录 = "@STRING_1@集齐了寻天残卷,换取了寻天宝录！";
	public static String 消耗银子提示 = "您接取了寻迹宝录,消耗银子@STRING_1@！";
	public static String 挖宝获得银子提示 = "恭喜您获得绑银@STRING_1@！";
	public static String 挖宝获得经验提示 = "恭喜您获得经验@STRING_1@！";

	public static String 不能洗魂 = "此技能等级为1级不用洗魂。";
	public static String 洗魂确认描述 = "你确定需要洗掉此技能等级，<f color='0xFF8400'>经验将不返还</f>。";
	public static String 缺少洗魂需要物品 = "您背包中没有洗魂需要的物品:@STRING_1@";
	public static String 洗髓精华 = "洗髓精华";
	public static String 未飞升不能切换 = "您还未飞升不能切换形象。";
	public static String 切换形象太过频繁 = "切换形象太过频繁。";
	public static String 召唤您的伙伴在限制地图 = "召唤您的伙伴在限制召唤地图,暂不前往！";
	public static String 降低治疗 = "降低治疗";
	public static String 目标受到的伤害提升 = "目标受到的伤害提升";
	public static String 合成成功了N次 = "您消耗了@STRING_1@个@STRING_2@,合成了@STRING_3@个@STRING_4@";
	public static String 合成失败了N次 = "合成失败,您消耗了@STRING_1@个@STRING_2@";
	public static String 连击次数 = "连击:";

	public static String 整理背包太过频繁 = "操作频率过快，请稍后再试！";

	public static String 普通喜酒礼包 = "紫色玉液礼包(绑)";
	public static String 白银喜酒礼包 = "紫色玉液礼包(绑)";
	public static String 黄金喜酒礼包 = "橙色玉液礼包(绑)";
	public static String 白金喜酒礼包 = "橙色玉液礼包(绑)";
	public static String 钻石喜酒礼包 = "七夕真戒指锦囊";

	public static String 渡劫称号 = "渡劫称号";

	public static String 离开 = "离开";
	public static String 暂留 = "暂留";
	public static String 渡劫虚弱 = "渡劫虚弱";
	public static String 护身罡气 = "护身罡气";
	public static String 护身真气 = "护身真气";
	public static String 光法阵 = "光法阵";
	public static String 元素精华 = "元素精华";
	public static String 元素之魂 = "元素之魂";
	public static String 强体丹 = "强体丹";
	public static String 心魔丹 = "心魔丹";
	public static String 火神玄魄 = "火神玄魄";
	public static String 火神玄魄倒计时 = "火神玄魄倒计时:";
	public static String 幻象镜 = "幻象镜";
	public static String 乱雪迷界 = "乱雪迷界";
	public static String 已经拥有此称号 = "已经拥有此称号";
	public static String 为爱癫狂 = "为爱癫狂";
	public static String 一往情深 = "一往情深";
	public static String 倾情天下 = "倾情天下";
	public static String 为爱癫狂buff描述 = "四属性攻击增加5000";
	public static String 一往情深buff描述 = "双防御各增加500";
	public static String 倾情天下buff描述 = "增加生命上限10000点";
	public static String 某人使用某物品 = "@STRING_1@使用了@STRING_2@";
	// 法宝相关
	public static String 法宝 = "法宝";
	public static String 元神限制 = "元神限制";
	public static String 本尊 = "本尊";
	public static String 元神 = "元神";
	public static String 未神识探查 = "未神识探查";
	public static String 已经吞噬最大 = "法宝已经达到最大级";
	public static String 需要法宝进阶后才能继续 = "需要到法宝进阶界面突破才能继续提升法宝阶级";
	public static String 需要道具才能继续提升法宝阶级 = "需要%s才能继续提升法宝阶级";
	public static String 紫色以上法宝才能操作 = "需要紫色以上法宝才可进行此操作。";
	public static String 需要突破才能继续吞噬升级法宝 = "需要突破后才能继续吞噬升级法宝。";
	public static String 满阶的法宝才可以突破 = "需要法宝到达%s才可继续突破。";
	public static String 法宝已突破至最高 = "此法宝已突破至最高，无法再继续突破。";
	public static String 隐藏属性 = "隐藏属性";
	public static String 法宝不能合成 = "法宝不能合成";
	public static String 此宠物不可使用悟性丹 = "此宠物不可使用@STRING_1@";
	public static String 悟性已满 = "悟性已满";
	public static String 镜像 = "镜像";
	public static String boss即将刷新 = "boss即将刷新!";
	public static String 四象火神 = "四象火神";
	public static String 十级之前不能打开商城 = "需要达到十级才能开启商城";
	public static String 未领悟 = "未领悟";
	public static String 每次消耗两 = "每次消耗银子@STRING_1@两";
	public static String 已达到满级 = "已达到满级";
	public static String 已激活 = "已激活";
	public static String 未激活 = "未激活";
	public static String 这里是大师技能描述 = "这里是大师技能描述";
	public static String 人地天 = "人地天";
	public static String 当前阶重 = "当前%s阶%d重";
	public static String 禁忌技能描述 = "禁忌技能描述";
	public static String 大师技能总重数达到120重激活1阶 = "大师技能总重数达到120重激活1阶";
	public static String 大师技能总重数达到300重激活2阶 = "大师技能总重数达到300重激活2阶";
	public static String 暂未开放敬请期待 = "暂未开放，敬请期待";
	public static String 悟性2 = "悟性：";
	public static String 恭喜您获得血脉值 = "恭喜您获得血脉值 %d";
	public static String 积分不足 = "积分不足";
	public static String 传送 = "传送";
	public static String 寻路 = "寻路";
	public static String 技 = "技";
	public static String 没有传送道具 = "没有传送道具";
	public static String 渡劫回魂丹 = "渡劫回魂丹";
	public static String 凡品 = "凡品";
	public static String 下品 = "下品";
	public static String 中品 = "中品";
	public static String 上品 = "上品";
	public static String 精品 = "精品";
	public static String 极品 = "极品";
	public static String 珍品 = "珍品";
	public static String 绝品 = "绝品";
	public static String 奇刃 = "奇刃";
	public static String 凡兵 = "凡兵";
	public static String 大道 = "大道";
	public static String 通玄 = "通玄";
	public static String 鸿钧 = "鸿钧";
	public static String 法宝混沌 = "混沌";
	public static String 请放入法宝 = "请放入法宝";
	public static String 请放入材料 = "请放入材料";
	public static String 背包中没有该材料 = "背包中没有该材料";
	public static String 无需补充灵气 = "无需补充灵气";
	public static String 法宝鉴定符 = "法宝鉴定符";
	public static String 请放入法宝鉴定符 = "请放入法宝鉴定符";
	public static String 法宝鉴定符数量不足 = "法宝鉴定符数量不足";
	public static String 阶级 = "阶级";
	public static String 法宝已经强化到了上限 = "法宝已经强化到了最大级别，不能进行强化。";
	public static String 法宝强化石 = "法宝强化石";
	public static String 法宝强化石白 = "法宝强化石(白)";
	public static String 法宝强化石绿 = "法宝强化石(绿)";
	public static String 法宝强化石蓝 = "法宝强化石(蓝)";
	public static String 法宝强化石紫 = "法宝强化石(紫)";
	public static String 法宝强化石橙 = "法宝强化石(橙)";
	public static String 法宝不能羽化携带等级不够2 = "需要法宝的属性等级达到220级才可以羽化！";
	public static String 法宝不能羽化携带等级不够 = "已达到最大等级";
	public static String 很遗憾法宝羽化失败 = "很遗憾法宝没有羽化成功。";
	public static String 消耗经验 = "获得经验";
	public static String 升级所需经验 = "满级剩余经验";
	public static String 银两消耗 = "银两消耗";
	public static String 吞噬法宝不存在 = "吞噬法宝不存在";
	public static String 被吞噬法宝不存在 = "请放入其他法宝";
	public static String 背包中没有该法宝 = "背包中没有该法宝";
	public static String 放入吞噬材料不对 = "放入吞噬材料不对";
	public static String 重铸符 = "法宝重铸符";
	public static String 天铸符 = "法宝天铸符";
	public static String 神铸符 = "法宝神铸符";
	public static String 次品灵石 = "法宝续灵石(低级)";
	public static String 中品灵石 = "法宝续灵石(中级)";
	public static String 极品灵石 = "法宝续灵石(高级)";
	public static String 冲灵描述 = "放入法宝与灵石后点击注入灵气，可恢复与灵石相对应的灵气 ";
	public static String 如果没有天铸符 = "如果没有 <f color='0xFF8400'>天铸符</f>,每次消耗200两";
	public static String 请放入隐藏属性材料 = "请放入法宝天铸符或者法宝重铸符！";
	public static String 背包没有天铸符 = "您背包中没有法宝天铸符或者法宝重铸符";
	public static String 属性重铸成功 = "隐藏属性重铸成功";
	public static String 背包没有法宝鉴定符 = "您背包没有法宝鉴定符";
	public static String 神识消耗说明 = "如果没有<f color='0xff0000ff'>法宝鉴定符</f>，每次消耗@STRING_1@两银子";
	public static String 神识说明 = "法宝神识后将开启基础属性，并且有几率直接激活1-2条隐藏属性。\n法宝的颜色越高，需要消耗的法宝鉴定符越多";
	public static String 神识成功 = "法宝神识成功";
	public static String 神识失败 = "法宝神识失败";
	public static String 吞噬成功 = "吞噬成功";
	public static String 吞噬失败 = "吞噬失败";
	public static String 已达激活上限 = "已达激活上限";
	public static String 该法宝没有隐藏属性 = "该法宝没有隐藏属性";
	public static String 刷新需要 = "刷新需要材料@STRING_1@";
	public static String 请放入法宝强化物品 = "请放入法宝强化物品";
	public static String 恭喜法宝升级成功 = "恭喜法宝升级成功";
	public static String 很遗憾法宝升级失败 = "很遗憾法宝没有升级成功。";
	public static String 抵抗攻击次数 = "抵抗攻击次数";
	public static String 请先激活再刷新 = "请先激活再刷新隐藏属性";
	public static String 请先选择要刷新的属性 = "请先选择要刷新的属性";
	public static String 是否要吞噬高颜色的法宝 = "是否要吞噬高颜色的法宝?";
	public static String 您是否要花费钱激活属性 = "您是否要花费@STRING_1@两银子进行属性激活？";
	public static String 请放入法宝强化石 = "请放入法宝强化石";
	public static String 请求强化描述 = "放入单件法宝，再放入@STRING_1@，可对法宝进行加持，加持后法宝的威力将得到提升!";
	public static String 激活成功 = "激活成功";
	public static String 不能吞噬 = "没有基础属性，不能吞噬";
	public static String 大师技能修炼点返还 = "大师技能修炼点返还";
	public static String 大师技能修炼点返还_ = "亲爱的玩家,新版本大师技能已经来临,为了您更好的体验,我们将您之前所用的修炼点数全额返回,更多的惊喜等待您体验!";
	public static String 是否刷星高颜色的属性 = "是否要刷新高颜色的属性？";
	public static String 客户端修改地图文件名字 = "客户端修改地图文件名字";
	public static String 玩家 = "玩家";
	public static String 基础 = "基础";
	public static String 你没有兑换所需的物品 = "你没有兑换所需的物品:@STRING_1@";
	public static String 你今天兑换已经超过了次数 = "你今天兑换已经超过了次数,明天再来吧";
	public static String 获取渡劫信息出错 = "获取渡劫信息出错";
	public static String 渡劫没开启 = "没开启渡劫";
	public static String 平台不符 = "平台不符";
	public static String 服务器不符 = "服务器不符";
	public static String 沉默说话 = "沉默说话";
	public static String 灵印 = "灵印";
	public static String 狼印 = "狼印";
	public static String 离火2 = "疾 风";
	public static String 千层减伤 = "千层减伤";
	public static String 伤害提升 = "伤害提升";
	public static String 初级反震 = "初级反震";
	public static String BOSS增加法术攻击强度 = "BOSS增加法术攻击强度";
	public static String 背包满通过邮件 = "恭喜您装备升级成功，由于您背包已满，新装备通过邮件的形式发送，请及时查收";
	public static String 完美橙装备才可以升级 = "完美橙装备才可以升级";
	public static String 套装效果 = "套装效果 ";
	public static String 套装属性 = "套装属性 ";
	public static String 内功 = "内功";
	public static String 外功 = "外功";

	//
	public static String 黑暗魔龙 = "黑爵魔龙";
	public static String 职业 = "职业";
	public static String 胜 = "胜";
	public static String 负 = "负";
	public static String 战队改名 = "战队改名";
	public static String 战队改名提示内容 = "您的战队与另一只战队重名，请更改战队名称";
	public static String 级别不满足JJC要求 = "赶快去升级吧，135级以上的玩家才可以参加鲜血试炼";
	public static String 余额不足不能创建战队 = "您的余额不足，不能创建新的战队";
	public static String 战队名为空 = "请填写您的新战队名称";
	public static String 战队不存在 = "战队不存在";
	public static String 队伍中的玩家非同一战队 = "队伍中的玩家非同一战队，无法参赛";
	public static String 战队不存在请创建 = "您当前没有战队无法参加，请先创建战队";
	public static String 您的战队密码是 = "您的战队密码是:@STRING_1@";
	public static String 不是队长不能操作 = "不是队长不能操作";
	public static String 选择要召回的战队类型 = "请选择要召回的战队类型";
	public static String 还没有战队信息 = "您还没有战队,不能领取";
	public static String 战队名已存在 = "该战队名称已经存在";
	public static String 新赛季已开启标题 = "新赛季已经开启";
	public static String 新赛季已开启上线通知 = "新赛季已经开始，您的战队战勋和个人战勋已经被重置。";
	public static String 赛季已结束上线通知 = "赛季已经结束，新的赛季开启时，您的战队战勋和个人战勋将被重置。";
	public static String 新赛季已开启内容 = "@STRING_1@已经开始，您的战队战勋和个人战勋已经被重置。";
	public static String 新赛季已结束标题 = "赛季已经结束";
	public static String 新赛季已结束内容 = "@STRING_1@已经结束，新的赛季开启时，您的战队战勋和个人战勋将被重置。";
	public static String 没有达到领取奖励的条件 = "没有达到领取奖励的条件";
	public static String 正在战场不能囚禁 = "玩家正在战场，不能被囚禁";
	public static String 战队密码格式不符 = "战队密码只能是英文字母和数字的组合";
	public static String 已经有该规模的战队 = "您已经有该规模的战队";
	public static String 战队名长度不符 = "战队名长度不符,战队名称由2-6个字母或汉字组成";
	public static String 战队创建成功 = "战队创建成功,快快招募队友试炼吧";
	public static String 战队创建包含特殊符合 = "战队创建失败,战队名含有特殊符号";
	public static String 战队改名包含特殊符合 = "战队改名失败,战队名含有特殊符号";
	public static String 战队密码长度不符 = "战队密码长度不符,密码由4-10个字符组成";
	public static String 战场中不能操作 = "战场中不能操作该功能";
	public static String 战场介绍说明 = "当您的等级到达135级时开启鲜血试炼功能，鲜血试炼是以战队形式进行的2v2,3v3,5v5的竞技玩法。";
	public static String 战队介绍说明 = "在您的等级到达135级时可以选择创建战队或者加入战队，以战队的形式参加鲜血试炼竞技活动。";
	public static String 您还没有战队 = "您还没有加入或者创建战队";
	public static String 战队中没有在线成员 = "战队中没有在线成员";
	public static String 查看的玩家没有战队 = "查看的玩家没有战队";
	public static String 该战队暂未开放 = "暂未开放,敬请期待";
	public static String 该战队暂未开放_ = "暂未开放---敬请期待";
	public static String 创建战队条件1 = "1:创建者等级≥135级";
	public static String 创建战队条件2 = "2:创建消耗银两:@STRING_1@";
	public static String 还没有好友不能招募 = "您还没有好友，不能招募";
	public static String 好友不在线不能招募 = "玩家不在线或者不存在，不能招募";
	public static String 不能招募自己 = "不能招募自己";
	public static String 不能招募非本国玩家 = "不能招募非本国玩家";
	public static String 不是队长不能招募 = "只有队长才可以招募进队";
	public static String 您还没有XX战队不能招募 = "您还没有创建@STRING_1@战队，不能招募队友。";
	public static String 您招募的队友已经有XX战队不能招募 = "您招募的队友已经有@STRING_1@战队不能招募。";
	public static String 招募 = "招募队友";
	public static String 退出战队 = "退出战队";
	public static String 创建战队 = "创建战队";
	public static String 请输入密码 = "请输入战队密码";
	public static String 请输入新密码 = "请输入新密码";
	public static String 密码设置成功 = "密码设置成功，请您妥善保管";
	public static String 没有权限进行密码管理 = "您没有权限进行密码管理";
	public static String 两次密码不一样 = "您输入的2次新密码不一致";
	public static String 密码不对 = "密码不对,请输入正确的密码";
	public static String 战队解散成功 = "战队解散成功";
	public static String 战队解散还有其他玩家 = "战队解散失败，还有其他队友在";
	public static String 招募内容 = "@STRING_1@招募您加入@STRING_2@竞技场，一起征战鲜血试炼?";
	public static String 退出战队提示 = "您确定退出战队?如果您退出战队后从新加入其他战队您在此战队的参赛场数将会清零!";
	public static String 创建战队确认提示 = "您即将创建@STRING_1@战队，将消耗银两@COUNT_1@,确定吗?";
	public static String JJC开放阶段 = "每天12:00-23:00玩家可到跨服npc,以战队名义进行匹配参赛";
	public static String 匹配的时候队伍有不是战队的人员 = "队伍中有不是战队的成员";
	public static String 某个队员进去限制地图匹配失败 = "队员@STRING_1@在限制地图,匹配失败";
	public static String 匹配的时候有队友在战场中 = "匹配失败，匹配的时候有队友在战场中";
	public static String 正在匹配战队 = "开始匹配战队，请您耐心等待";
	public static String 已经在匹配 = "您的战队已经在匹配中，请您耐心等待";
	public static String 匹配的时候有队友在战场 = "匹配失败，有队友在战场中";
	public static String 匹配的时候有战友在战场 = "匹配失败，战队中有队友在战场中";
	public static String 战斗中不能匹配 = "请不要重复匹配";
	public static String 战场中不能匹配 = "匹配失败，战场中不能匹配";
	public static String 渡劫中不能匹配 = "匹配失败，有玩家正在渡劫不能匹配";
	public static String 囚禁中不能匹配 = "匹配失败，有玩家被囚禁不能匹配";
	public static String 囚禁中不能退出比赛 = "囚禁中不能退出比赛";
	public static String 匹配的时候队伍成员不足 = "匹配失败，队伍人数不满足参赛规则的人数";
	public static String 匹配的时候队伍成员不在线 = "有队友不在线，不能匹配";
	public static String 匹配的时候战队成员为空 = "匹配异常，请联系GM";
	public static String 匹配的时候不是队长 = "匹配失败，不是小队队长";
	public static String JJC中不能离开队伍 = "鲜血试炼中不能离开队伍";
	public static String JJC中不能解散队伍 = "鲜血试炼中不能解散队伍";
	public static String 副本中不能解散队伍 = "副本中不能操作";
	public static String 队友副本中不能解散队伍 = "有队友在副本中,请稍后尝试";
	public static String jjc开启公告 = "鲜血试炼已开启，勇士们争夺属于我们的荣誉吧!";
	public static String jjc结束公告 = "鲜血试炼已结束，勇士们明天再战!";
	public static String JJC中不能踢出队友 = "鲜血试炼中不能踢出队友";
	public static String 不是队长不能踢出队友 = "您无权操作";
	public static String 匹配的时候战队成员离开 = "匹配失败，有玩家离开组队";
	public static String 踢人成功 = "踢人成功";
	public static String 踢人失败 = "踢人失败，请联系GM";
	public static String 被踢出战队 = "您已经被队长踢出战队";
	public static String 取消匹配成功 = "您已经取消了该次的匹配";
	public static String 取消匹配成功提示 = "您的队友@STRING_1@已经取消了该次的匹配";
	public static String 招募超过最大人数 = "队伍人数已满";
	public static String 没有找到战队成员信息 = "没有找到您在某个战队的信息";
	public static String 匹配只有自己的提示 = "大于5分钟";
	public static String 献血试炼 = "鲜血试炼";
	public static String 战队人数不满足 = "战队内人数不满足要求，请先邀请玩家加入战队再参加";
	public static String 战队在线人数不满足 = "战队内在线人数不满足要求";
	public static String 赛季不开不让进跨服 = "赛季未开启";
	public static String 消费排行说明 = "活动开始时间：@STRING_1@\n活动结束时间：@STRING_2@\n获得奖励条件：前50名玩家可以获得对应名字的物品奖励。\n活动排行查询：活动中玩家可以随时查看个人排名，活动结束后榜单会停留一周时间方便玩家查询。";
	public static String 当前队伍人数与参赛人数不符 = "当前队伍人数与参赛人数不符";
	public static String 每周血石结算 = "每周血石结算";
	public static String 每周血石结算失败战队不满足条件 = "每周血石结算失败，您所在的战队本周未满足最少参数场数20场";
	public static String 每周血石结算失败 = "每周血石结算失败，您本周总共在@STRING_1@参赛@COUNT_1@场，总场数不满足6场次";
	public static String 恭喜获得血石 = "恭喜您获得了@COUNT_1@血石";
	public static String 招募队友成功 = "招募队友成功";
	public static String 离开战队成功 = "离开战队成功";
	public static String XX玩家离开战队 = "@STRING_1@离开了战队";
	public static String 转交战队请求 = "转交战队请求已发出，等待对方接受";
	public static String 转交战队成功 = "接受战队成功，您现在是队长";
	public static String 转交成功 = "战队转交成功@STRING_1@是新的队长";
	public static String 转交的队友不在线 = "您要转交的队友不在线";
	public static String 进入战场失败 = "进入战场失败";
	public static String 您已经成功的加入了XX战队 = "您已经成功的加入了@STRING_1@战队";
	public static String 战斗已经开始 = "战斗已经开始，去战斗吧";
	public static String 小于XX分钟 = "小于@COUNT_1@分钟";
	public static String 此地图不可使用特殊传送 = "此地图不可使用特殊传送";
	public static String 此地图不可使用特殊传送2 = "对方处于特殊地图内，不允许被传送。";
	public static String 不可传送非本家族成员 = "不可传送非本家族成员进入此地图。";
	public static String 拒绝 = "拒绝";
	public static String 不能前往混元至圣 = "对方正在鲜血试炼，不能响应召唤";
	public static String 奖励发送成功_背包 = "奖励发送成功,请查看背包";
	public static String 奖励发送成功_邮件 = "奖励发送成功,请查看背包";
	public static String 冠军战队战勋等级不满足 = "战队战勋必须≥2300";
	public static String 个人参数场数不满足战队参数场数百分三十 = "个人赛季参数场数小于战队赛季参数场数30%";
	public static String 第1赛季 = "第1赛季";
	public static String 跨服不可操作 = "跨服中不能操作";
	public static String 跨服不可操作有战队成员在 = "有战队成员在跨服中，不能操作";
	public static String 只能跨服操作 = "只能在跨服操作";
	public static String 赛季奖励配置错误 = "服务器配置错误,请联系GM";
	public static String 赛季奖励还没开始 = "领奖时间还没开放";
	public static String 赛季奖励还没结束 = "本赛季正在进行无法领取";
	public static String 冠军战队成员才可以领取 = "您不是本赛季前十战队成员不能领取";
	public static String 冠军战队才可以领取 = "您不是本赛季前十战队不能领取";
	public static String 赛季还没开始 = "该赛季还没开始";
	public static String 领取赛季冠军奖励异常 = "本赛季没有数据";
	public static String 没有战队不能领取奖励 = "没有战队不能领取奖励";
	public static String 领取赛季冠军奖励异常2 = "本赛季没有数据.";
	public static String 没有数据 = "获取数据失败,请稍后再试";
	public static String 还没到领奖时间 = "还没到领奖时间";
	public static String 赛季刚开始 = "该赛季刚刚开始，还没数据";
	public static String 未进入排行 = "未进入排行";
	public static String 邀请已发出 = "邀请已发出";
	public static String 获取过去排行榜战队出错 = "获取该赛季排行榜战队数据出错";
	public static String 申请进入队伍失败 = "队友正在鲜血试炼，暂不能申请入队";
	public static String 领取条件 = "1.战队进入排行榜前十名\n2.战队战勋≥2300";
	public static String 拒绝加入你的战队 = "玩家@STRING_1@拒绝了你的招募.";
	public static String 战场中不能原地复活 = "战场中不能原地复活.";
	public static String 鲜血试炼中不能聊天 = "鲜血试炼中不能聊天.";
	public static String 鲜血试炼中不能操作 = "鲜血试炼中不能操作.";
	public static String 城战不允许传送 = "城战不允许传送";
	public static String 鲜血试炼中不能下马 = "鲜血试炼中不能下坐骑.";
	public static String 战场说明 = "<f color='0x78f4ff'>开启时间</f> \n每天8:00-21:00\n<f color='0x78f4ff'>参加说明</f> \n以战队小组形式（3人）进行参赛（小组组长可以是战队中任意一人）\n<f color='0x78f4ff'>规则说明</f> \n1：进入鲜血试炼地图后，<f color='0x3dfd2f'>角色</f>和<f color='0x3dfd2f'>宠物</f>在开始倒计时<f color='0x3dfd2f'>未结束时将会自动恢复生命值与法力值，</f>" + " 比赛正式<f color='0x3dfd2f'>开启后</f>所有<f color='0x3dfd2f'>角色</f>以及<f color='0x3dfd2f'>宠物</f>均<f color='0x3dfd2f'>不可使用任何药品以及buff药</f> \n2：鲜血试炼开启后每个<f color='0x3dfd2f'>角色死亡</f>后可选择<f color='0x3dfd2f'>退出比赛</f>或直至比赛结束，比赛进行中如<f color='0x3dfd2f'>宠物死亡</f>则不可在召唤本宠物以及其它宠物\n3：击杀敌方全部角色获胜，若比赛时间结束双方都有队员存活，则按照总伤害多的一方获胜！\n4：战队战勋相差300以上，高战勋的战队胜利后不会获得战勋奖励。\n<f color='0x78f4ff'>血石说明</f> \n1：每周三24点结算，结算时战队战勋越高获得血石越多\n2：战队发放血石条件，当前周参赛场数≥20\n" + "3：个人发放血石条件，当前周参赛场数≥6场\n如果个人战勋＜战队战勋50%则按照个人战勋发放血石\n<f color='0x78f4ff'>战队说明</f>\n创建条件:角色≥135级以及银两@STRING_1@\n<f color='0x78f4ff'>加入条件</f> \n角色≥135级\n<f color='0x78f4ff'>队员上限</f> \n3v3模式下的战队最多可招募5名队友（只可邀请本国家成员）\n赛季中解散战队，该战队将会从排行榜中消失\n<f color='0x78f4ff'>赛季说明</f>\n赛季时间:每90天为一个赛季\n" + "<f color='0x78f4ff'>赛季规则</f> \n新赛季开启时<f color='0x3dfd2f'>清除</f>上赛季所有战队参赛场数以及<f color='0x3dfd2f'>个人战勋</f>和<f color='0x3dfd2f'>战队战勋</f>\n";
	public static String 战场说明copy = "<f color='0x78f4ff'>开启时间</f> \n每天12:00-23:00\n<f color='0x78f4ff'>参加说明</f> \n以战队小组形式（3人）进行参赛（小组组长可以是战队中任意一人）\n<f color='0x78f4ff'>规则说明</f> \n1：进入鲜血试炼地图后，<f color='0x3dfd2f'>角色</f>和<f color='0x3dfd2f'>宠物</f>在开始倒计时<f color='0x3dfd2f'>未结束时将会自动恢复生命值与法力值，</f>" + " 比赛正式<f color='0x3dfd2f'>开启后</f>所有<f color='0x3dfd2f'>角色</f>以及<f color='0x3dfd2f'>宠物</f>均<f color='0x3dfd2f'>不可使用任何药品以及buff药</f> \n2：鲜血试炼开启后每个<f color='0x3dfd2f'>角色死亡</f>后可选择<f color='0x3dfd2f'>退出比赛</f>或直至比赛结束，比赛进行中如<f color='0x3dfd2f'>宠物死亡</f>则不可在召唤本宠物以及其它宠物\n3：击杀敌方全部角色获胜，若比赛时间结束双方都有队员存活，则按照总伤害多的一方获胜！\n4：战队战勋相差300以上，高战勋的战队胜利后不会获得战勋奖励。\n<f color='0x78f4ff'>血石说明</f> \n1：每周三24点结算，结算时战队战勋越高获得血石越多\n2：战队发放血石条件，当前周参赛场数≥20\n" + "3：个人发放血石条件，当前周参赛场数≥6场\n如果个人战勋＜战队战勋50%则按照个人战勋发放血石\n<f color='0x78f4ff'>战队说明</f>\n创建条件:角色≥135级以及银两@STRING_1@\n<f color='0x78f4ff'>加入条件</f> \n角色≥135级\n<f color='0x78f4ff'>队员上限</f> \n3v3模式下的战队最多可招募5名队友（只可邀请本国家成员）\n赛季中解散战队，该战队将会从排行榜中消失\n<f color='0x78f4ff'>赛季说明</f>\n赛季时间:每90天为一个赛季\n" + "<f color='0x78f4ff'>赛季规则</f> \n新赛季开启时<f color='0x3dfd2f'>清除</f>上赛季所有战队参赛场数以及<f color='0x3dfd2f'>个人战勋</f>和<f color='0x3dfd2f'>战队战勋</f>\n<f color='0x78f4ff'>赛季前十奖励</f> \n1：<f color='0x ffae00'>永久称号</f> \n2：<f color='0x ffae00'>永久飞行坐骑</f> \n" + "<f color='0x ffae00'>赛季前十奖励领取说明</f>\n<f color='0x78f4ff'>奖励领取时间</f> \n赛季结束后直至下赛季开启时间内均可领取\n<f color='0x78f4ff'>战队领取条件</f> \n1：赛季结束时排行榜<f color='0x ffae00'>前十名</f> \n2：战队战勋≥<f color='0x ffae00'>2300</f>\n<f color='0x78f4ff'>队员领取条件</f> \n1：<f color='0x3dfd2f'>前十</f>战队成员\n" + "2：赛季参赛场数≥战队赛季总参赛场数<f color='0x3dfd2f'>30%</f>\n3：个人战勋≥战队战勋50%\n";
	public static String 战血 = "战血";
	public static String 万鬼窟 = "万鬼窟";
	public static String 赛季结束不能参加 = "赛季结束不能参加";
	public static String 冰川时代 = "冰川时代";
	public static String 九黎之怒 = "九黎之怒";

	public static String 没有合成材料 = "请放入合成材料";
	public static String 没有活动配方 = "没有活动配方";
	public static String 恭喜合成成功_标题 = "恭喜您合成成功,请查收邮件";
	public static String 恭喜合成成功_内容 = "恭喜您合成成功,请查收附件。";
	public static String 配方材料不符合 = "请放入正确的配方材料";
	public static String 器灵槽里还有器灵不能进行合成 = "器灵槽里还有器灵不能进行合成";

	public static String 破封成功 = "经过众仙友共同努力，成功击杀暴炎邪帝，封印时间缩短@STRING_1@天,剩余封印天数为@STRING_2@天";
	public static String 封印首次击杀boss = "@STRING_1@挑战@STRING_2@成功，@STRING_3@等级封印时间缩短@STRING_4@天，剩余封印天数为@STRING_5@天";
	public static String 捐献材料描述_190 = "盘古现在需要时间来恢复灵气，捐献<f color='0xff0000ff'>玄天灵片</f>给盘古，可帮助盘古恢复灵气，捐献足够多的<f color='0xff0000ff'>玄天灵片</f>后方可帮助盘古恢复满灵气，灵气恢复满后可缩短封印时间\n<f color='0xff0000'>提示：</f>每捐献一个<f color='0xff0000ff'>玄天灵片</f>，将可获得一个<f color='0xff0000ff'>寻迹宝录</f>";
	public static String 捐献材料描述_190_2 = "<f color='0xff0000ff'>冰晶</f>捐献至总进度的20%、40%、60%、80%、100%时，赤焰恶魔将会被削弱\n<f color='0xff0000'>提示：</f>每捐献一个<f color='0xff0000ff'>冰晶</f>，将可获得一个<f color='0xff0000ff'>寻迹宝录</f>";
	public static String 捐献材料描述_220 = "<f color='0xff0000ff'>降灵符</f>捐献至总进度的20%、40%、60%、80%、100%时，将会对暴炎邪帝产生限制，使其无法释放部分技能\n<f color='0xff0000'>提示：</f>每捐献一个<f color='0xff0000ff'>降灵符</f>，将可获得一个<f color='0xff0000ff'>寻迹宝录</f>";
	public static String 捐献材料描述_220_2 = "<f color='0xff0000ff'>降灵符</f>捐献至总进度的20%、40%、60%、80%、100%时，将会对暴炎邪帝产生限制，使其攻击力下降\n<f color='0xff0000'>提示：</f>每捐献一个<f color='0xff0000ff'>降灵符</f>，将可获得一个<f color='0xff0000ff'>寻迹宝录</f>";
	public static String 该破封阶段没有捐献 = "该破封阶段没有捐献,请联系GM";
	public static String 破封任务配置错误 = "服务器配置错误，请联系GM";
	public static String 请放入破封材料 = "请放入该破封任务所需的材料";
	public static String 背包不存在破封材料 = "您背包没有破封材料";
	public static String 破封材料不存在 = "破封材料不存在";
	public static String 捐献成功 = "捐献成功";
	public static String 服务器封印需要大于220 = "服务器封印小于260，不能飞升！";
	public static String 破封野外boss公告 = "220级破封任务世界Boss<f color='0xff00ff00'>@STRING_1@</f>已出现，请众仙友速前往<f color='0xffFDA700'>@STRING_2@</f>击杀之";
	public static String boss挑战成功 = "boss挑战成功";
	public static String boss挑战失败 = "boss挑战失败";
	public static String 暴炎邪帝 = "暴炎邪帝";
	public static String 累计充值钱 = "累计充值@STRING_1@";
	public static String 累计消费钱 = "累计消费@STRING_1@";
	public static String 签到 = "签到";
	public static String 活跃 = "活跃";
	public static String 日常 = "日常";
	public static String 飞仙 = "指引";
	public static String 渡劫 = "渡劫";
	public static String 周年寻宝 = "周年寻宝";
	public static String 全服寻宝 = "全服寻宝";
	public static String 限时活动 = "限时活动";
	public static String 周月礼包 = "周月礼包";
	public static String 极地寻宝 = "极地寻宝";
	public static String 独角灵兽 = "独角灵兽";
	public static String 封印_TITLE1_150 = "第一阶段——击杀幻音魔妖";
	public static String 封印_TITLE2_150 = "第二阶段——击杀响尾蛇皇";
	public static String 封印_TITLE3_150 = "第三阶段——击杀炎火金刚";
	public static String 封印_CONTENT1_150 = "前往盘古处，与盘古对话，进入混乱异域，帮助盘古击杀<f color='0xff00ff00'>幻音魔妖</f>后，方可缩短封印时间\n<f color='0xff0000'>提示：</f>当本服内有仙友第一次成功击杀该BOSS时，将会缩短封印时间，并开启下一个阶段任务，每击杀一次BOSS，BOSS都会变得更虚弱，强力的仙友，快带着大家碾压BOSS吧！！！";
	public static String 封印_CONTENT2_150 = "前往盘古处，与盘古对话，进入混乱异域，帮助盘古击杀<f color='0xff00ff00'>响尾蛇皇</f>后，方可缩短封印时间\n<f color='0xff0000'>提示：</f>当本服内有仙友第一次成功击杀该BOSS时，将会缩短封印时间，并开启下一个阶段任务，每击杀一次BOSS，BOSS都会变得更虚弱，强力的仙友，快带着大家碾压BOSS吧！！！";
	public static String 封印_CONTENT3_150 = "前往盘古处，与盘古对话，进入混乱异域，帮助盘古击杀<f color='0xff00ff00'>炎火金刚</f>后，方可缩短封印时间\n<f color='0xff0000'>提示：</f>当本服内有仙友第一次成功击杀该BOSS时，将会缩短封印时间，并开启下一个阶段任务，每击杀一次BOSS，BOSS都会变得更虚弱，强力的仙友，快带着大家碾压BOSS吧！！！";
	public static String 封印_TITLE1_190 = "第一阶段——收集玄天灵片";
	public static String 封印_TITLE2_190 = "第二阶段——击杀堕落仙人";
	public static String 封印_TITLE3_190 = "第三阶段——击杀赤焰恶魔";
	public static String 封印_CONTENT1_190 = "盘古现在急需<f color='0xff0000ff'>玄天灵片</f>来恢复灵气，各位仙友如有<f color='0xff0000ff'>玄天灵片</f>，请速交给盘古，捐献满玄天灵片方可缩短封印时间\n<f color='0xff0000'>提示：</f>将<f color='0xff0000ff'>玄天灵片</f>交付给<f color='0xffFDA700'>盘古</f>，帮助其恢复灵气，可获得经验奖励哦！";
	public static String 封印_CONTENT2_190 = "前往盘古处，与盘古对话，进入混乱异域，帮助盘古击杀<f color='0xff00ff00'>堕落仙人</f>后，方可缩短封印时间\n<f color='0xff0000'>提示：当本服内有仙友第一次成功击杀该BOSS时，将会缩短封印时间，并开启下一个阶段任务</f>，小心<f color='0xff00ff00'>堕落仙人</f>的定身！！";
	public static String 封印_CONTENT3_190 = "前往盘古处，与盘古对话，进入混乱异域，帮助盘古击杀<f color='0xff00ff00'>赤焰恶魔</f>后，方可缩短封印时间\n<f color='0xff0000'>提示：当本服内有仙友第一次成功击杀该BOSS时，将会缩短封印时间，</f>帮助盘古收集<f color='0xff0000ff'>冰晶</f>可削弱<f color='0xff00ff00'>赤焰恶魔</f>的能力，每收集20%，将对其进行一次削弱";
	public static String 封印_TITLE1_220 = "第一阶段——启灵召唤准备阶段";
	public static String 封印_TITLE2_220 = "第二阶段——齐心恶斗暴炎邪帝";
	public static String 封印_CONTENT1_220 = "盘古将在10天之后召唤出残暴的<f color='0xff00ff00'>暴炎邪帝</f>，将其击杀可大幅缩短封印时间，但是残暴的<f color='0xff00ff00'>暴炎邪帝</f>又是可随便能被杀死之人？在这10天内，收集<f color='0xff0000ff'>降灵符</f>交给盘古，可削弱即将召唤出的<f color='0xff00ff00'>暴炎邪帝</f>的对应能力\n" + "<f color='0xff0000'>提示：</f>在盘古处，<f color='0xff0000ff'>降灵符</f>可捐至两种不同的宝箱中，一种为降灵箱，将<f color='0xff0000ff'>降灵符</f>捐在本宝箱中，将削弱<f color='0xff00ff00'>暴炎邪帝</f>的技能威力与种类；另一种为降力箱，将<f color='0xff0000ff'>降灵符</f>捐在本宝箱中，将削弱<f color='0xff00ff00'>暴炎邪帝</f>的基础属性。捐献进度每达到20%，将会对<f color='0xff00ff00'>暴炎邪帝</f>进行一次削弱";
	public static String 封印_CONTENT2_220 = "<f color='0xff00ff00'>暴炎邪帝</f>已出现，请众仙友速前往<f color='0xffFDA700'>风陵渡</f>击杀之\n<f color='0xff0000'>提示：</f>当本服内有仙友第一次成功击杀该BOSS时，将会缩短封印时间，本Boss每周将刷新一次，推荐至少20名仙友一同前往击杀";
	public static String 封印副本不能使用 = "封印副本不能使用：";
	public static String 封印副本中不能使用混元至圣 = "封印副本中不能使用混元至圣圣印";
	public static String 离开副本 = "离开副本";
	public static String 寻迹宝录 = "寻迹宝录";
	public static String 封印奖励_title = "恭喜您获得了封印捐献奖励";
	public static String 离开副本倒计时 = "即将离开副本";
	public static String 盘古 = "盘古";
	public static String 玩家正在进行封印副本挑战 = "玩家正在进行封印副本挑战,不能拉人";
	public static String 捐献已到上线 = "捐献已达上限";
	public static String 第一阶段自动提示_220 = "220级破封任务第一阶段已结束，封印时间缩短@STRING_1@天,剩余封印天数为@STRING_2@天";
	public static String 封印挑战不能骑鸟 = "封印挑战中不能使用飞行坐骑。";
	public static String 封印不能切换元神 = "封印挑战中，不能切换元神";
	public static String 破封任务开启 = "@STRING_1@等级破解封印任务已开启，请各位玩家前往灭魔神域-盘古处参与任务";
	public static String 捐献buff级别是0 = "当捐献进度达到20%，boss将受到削弱";
	public static String 捐献提示 = "@STRING_1@等级@STRING_2@阶段的捐献任务已完成，缩短封印时间@STRING_3@天,剩余封印天数为@STRING_4@天";
	public static String 古城传送符 = "古城传送符";
	public static String 王城传送符 = "王城传送符";
	public static String 天虹传送符 = "天虹传送符";
	public static String 仙府传送符 = "仙府传送符";
	public static String 击杀boss次数达上限 = "今天挑战次数已用尽，请明天再挑战（每天至多可以挑战5次）";
	public static String 破封任务结束 = "破封任务结束";
	public static String 封印副本不能使用此功能 = "封印副本不能使用此功能";
	public static String 必须铭刻才能操作 = "只有铭刻的装备才可以进行该操作";
	public static String 装备合成2个老装备提示 = "请放入正确的装备数量";
	public static String 法宝强化材料绑定提示 = "您的加持材料中有绑定材料，加持后的法宝将会绑定，是否加持？";
	public static String 装备合成材料绑定提示 = "您的合成材料中有绑定材料，合成后的道具将会绑定，是否合成？";
	public static String 法宝唤虚提示 = "法宝唤虚需要消耗银子%s,是否确定？";
	public static String 玄天灵片 = "玄天灵片";
	public static String 冰晶 = "冰晶";
	public static String 降灵符 = "降灵符";

	//
	public static String 膜拜成功 = "膜拜成功";

	/** 仙尊 */
	public static String 仙尊公告 = "恭喜<f color='0xFFFF00'>@STRING_1@</f>受众望所归，荣当<f color='0xffFDA700'>@STRING_2@</f>";
	// public static String 仙尊公告 = "恭喜玩家<f color='0xFFFF00'>@STRING_1@</f>当选<f color='0xffFDA700'>@STRING_2@</f>";
	public static String 获得仙尊坐骑标题 = "恭喜您获得了仙尊坐骑";
	public static String 获得仙尊坐骑内容 = "恭喜您获得了仙尊坐骑";
	public static String 仙尊窗口默认宣言 = "<f size='28'>仙尊，荣誉的象征，各职业中已飞升者均有机会参与仙尊的竞选，当选仙尊后，</f><f color='0xFFFF00' size='28'>将获得仙尊独有绚丽坐骑与称号，并在灭魔神域设立雕像，</f><f size='28'>想参与仙尊竞选的玩家，需先在各职业仙尊镜像处挑战仙尊，挑战成功后方可加入排行榜进行选举</f>";
	// public static String 设置宣言引导 = "挑战成功！您已获得@STRING_1@仙尊参选资格，可在灭魔神域@STRING_1@仙尊处设置宣言及查询投票，是否现在前往？";
	public static String 当前仙尊宣言 = "当前仙尊宣言";
	public static String 被膜拜次数 = "被膜拜次数";

	public static String 仙尊默认宣言 = "大家好！希望能够投我一票，我一定会为本职业争光！";
	public static String 设置宣言成功 = "设置成功";
	public static String 设置宣言失败 = "设置失败";

	public static String 投票奖励邮件标题 = "投票奖励";
	public static String 投票奖励邮件内容 = "感谢您为仙尊竞选者投票，特赠送此奖励，望对您有所帮助";
	public static String 投票成功提示 = "投票成功！您获得@STRING_1@经验.@STRING_2@绑银.@STRING_3@奖励物品已通过邮件发送";
	public static String 投票成功通知 = "投票成功！您获得@STRING_2@绑银.@STRING_3@奖励物品已通过邮件发送";
	public static String 已经投过票 = "您本期内已投过票，请下周再投~";
	public static String 不在投票榜内 = "您要投票的玩家不在投票榜内";
	public static String 不是投票时间 = "每周六23:59:59结束投票哦";
	public static String 无人参选 = "当前还未有玩家入榜，已飞升的玩家可去参与竞选哦！";
	public static String 上期无人参选 = "上期未有人参加本职业仙尊竞选，无需查看投票榜";
	public static String 非本职业不能投票 = "您非本职业玩家，无法参与投票";;
	public static String 等级不够不能投票 = "您等级未到40级，无法投票，加油练级~！";
	public static String 投票失败 = "投票失败！";
	public static String 膜拜 = "膜拜";

	public static String 非本职业不能膜拜 = "您非本职业玩家，无法进行膜拜";;
	public static String 等级不够不能膜拜 = "您等级未到40级，无法进行膜拜，加油练级~！";
	public static String 膜拜奖励邮件标题 = "膜拜奖励";
	public static String 膜拜奖励邮件内容 = "您成功膜拜了本职业仙尊，获得以下物品奖励";
	public static String 无仙尊提示 = "当前还未有玩家当选，无法膜拜，已飞升的玩家可去参与竞选哦！";
	public static String 膜拜成功提示 = "膜拜成功！您获得@STRING_1@经验.@STRING_2@绑银.@STRING_3@奖励物品已通过邮件发送";
	public static String 膜拜成功通知 = "膜拜成功！您获得@STRING_2@绑银.@STRING_3@奖励物品已通过邮件发送";
	public static String 已经膜拜过 = "您今天已经进行过膜拜，请明天再来膜拜~";
	public static String 二次确认 = "需支付@STRING_1@将奖励档次设置为@STRING_2@，是否确定支付并设置";
	public static String 设置膜拜奖励成功 = "设置成功！扣除您@STRING_1@银子，膜拜奖励级别提升为@STRING_2@";
	public static String 设置低档奖励 = "现在的膜拜奖励档次为@STRING_1@，本期内再次设置膜拜奖励只能设置高于此档次的奖励";
	public static String 是默认档 = "已是当前默认奖励,无需再次设置";
	public static String 是最高档 = "目前已是最高档次福利，无需再设置";

	public static String 出现异常 = "出现异常,请联系GM";
	public static String 无人投票 = "没有玩家为您投票,无需答谢哦";
	public static String 只有自己投票 = "只有自己投票,无需答谢哦";
	public static String 查看记录无人投票 = "暂时没有玩家为您投票哦";
	public static String 答谢确认 = "需支付@STRING_1@来购买礼包答谢为您投过票的人，是否确定支付并发送礼包？为您投过票的人，每人将获得@STRING_2@*@STRING_3@";
	public static String 已答谢过 = "本周期内您已进行过答谢，无需再次答谢！";
	public static String 答谢成功 = "答谢成功！扣除您@STRING_1@，为您投过票的玩家将获得@STRING_2@";
	public static String 答谢标题 = "仙尊答谢";
	public static String 答谢内容 = "本周仙尊@STRING_1@赠送于您，感谢您为他投了宝贵的一票！";
	public static String 不是仙尊 = "您不是仙尊或者职业不符";
	public static String 已有XX人答谢 = "已有@STRING_1@名玩家为您投票";
	public static String 是否答谢 = "本期内，有@STRING_1@名玩家为您投票，赠送他们答谢礼包需要花费@STRING_2@，是否答谢？";
	public static String 宣言含有非法或敏感字符 = "您设置的宣言含有非法或敏感字符,请重新输入";
	public static String 宣言跟之前相同 = "您的宣言跟之前相同";

	public static String 土地需要开垦才能操作 = "土地需要开垦才能操作";
	public static String 种植后才能操作哦 = "种植后才能操作哦";
	public static String 作物已经成熟不能操作 = "作物已经成熟不能操作";
	public static String 已经埋了炸弹 = "已经埋了炸弹";
	public static String 炸弹信息异常 = "炸弹信息异常";
	public static String 偷菜被炸 = "@STRING_1@预谋偷你的@STRING_2@,结果被炸成了乌眼儿青,炸弹有效力剩余@COUNT_1@.";
	public static String 放置炸弹 = "放置炸弹:@STRING_1@";
	public static String 放置炸弹成功 = "放置炸弹成功";
	public static String 增加队列时间超出最大限制 = "增加队列时间超出最大限制";
	public static String 仙府中不能使用飞行坐骑 = "仙府中不能使用飞行坐骑!";
	public static String 你还没有仙府 = "你还没有仙府";
	public static String 仙府被锁定 = "仙府被锁定";
	public static String 仙府被降级 = "仙友,你的资源不足,已经被降级";
	public static String 仙府被封印 = "仙府被封印";
	public static String 已经埋好了炸弹 = "已经埋好了炸弹:@STRING_1@";
	public static String 延长偷菜时间 = "延长偷菜时间";
	public static String 减少偷菜时间 = "减少偷菜时间";
	public static String 鲁班令到期 = "您的鲁班令已到期";
	public static String 仙府维护 = "仙友,你的仙府维护成功,所有建筑都可以正常提供服务了.消耗资源:";
	public static String 去解封仙府 = "去解封仙府";
	public static String 解封仙府提示 = "尊敬的仙友,由于你长期不在线,仙府已经封印,是否现在去解封仙府?";
	public static String 去创建仙府 = "去创建仙府";
	public static String 创建仙府提示 = "尊敬的仙友,你还没有仙府,仙府中有大量超值物品,是否现在去创建仙府?";
	public static String 果实成熟 = "果实成熟";
	public static String 银子不足不能种植 = "你的银子不足,不能种植";
	public static String 仙府开门 = "@STRING_1@打开了你的仙府大门,花费@STRING_2@", 你猜他会干什么;
	public static String 仙府关门 = "@STRING_2@关上了你的仙府大门,心满意足的走了";
	public static String _10级以下不能扩展背包 = "大侠，快放开那背包好嘛，10级以上才可以扩充背包哦";
	public static String 只能在自己国家使用 = "只能在自己国家使用";
	public static String 不能使用道具 = "不能使用@STRING_1@.";
	public static String 返还资源 = "返还资源";
	public static String 飞行坐骑不能进仙府 = "进入仙府需要将受到禁空阵法的影响，您是否进入仙府？";

	/** 周充值活动 */
	public static String 已经领取过奖励 = "您已经领取过此奖励。";
	public static String 未参加周充值活动 = "您没有满足充值活动要求,不能领取奖励。";
	public static String 今日充值大礼 = "今日充值大礼";
	public static String 累计充值大礼 = "累计@STRING_1@日充值大礼";
	public static String[] 多少日 = { "一", "二", "三", "四", "五", "六", "七", "八" };
	public static String 周充值活动结束 = "领取奖励失败，活动已经结束。";
	public static String 周充值不能领取活动奖励 = "领取奖励失败，您未满足领取条件。";
	public static String 周充值已经领取活动奖励 = "领取奖励失败，您已经领取过此活动礼包。";
	public static String 周充值未知错误 = "活动出现未知错误，请联系GM。";
	public static String 没有此类活动 = "当前没有此活动。";
	public static String 周充值领取成功 = "领取成功！请在邮件内收取道具！";

	/** 翅膀 */
	public static String 翅膀宝石孔描述 = "宝石插槽(宝石可直接镶嵌/摘取)";
	public static String 宝石加成 = "宝石加成:";
	public static String 镶嵌奖励 = "镶嵌奖励:";
	public static String 激活属性 = "激活或强化属性:";
	public static String 翅膀种类 = "翅膀种类:@STRING_1@号翅膀";
	public static String 几号翅膀 = "@STRING_1@号翅膀";
	public static String 奖励属性 = "奖励属性:";
	public static String 收集个数激活属性 = "收集@STRING_1@个+@STRING_2@@STRING_3@";
	public static String 不能镶嵌或镶嵌位置错误 = "不能镶嵌或镶嵌位置错误";
	public static String 请放入宝石 = "请放入宝石";
	public static String 光效宝石不在背包中 = "您的光效宝石不在背包中,请查看仓库或防爆包";
	public static String 光效宝石 = "光效宝石";
	public static String 光效宝石加成 = "+@STRING_1@@STRING_2@";
	public static String 无光效宝石出售 = "当前商城无此光效宝石出售";
	public static String 激活宝石孔 = "激活宝石孔";
	public static String 购买光效宝石确认 = "您尚未拥有此类宝石，该宝石价格为@STRING_1@，有效期为15天，是否进行购买并镶嵌？";
	public static String 此翅膀未收集 = "您尚未收集该翅膀";
	public static String 强化面板提示 = "<f color='0xFFFF00' size='28'>提示:羽化后翅膀形象将会发生改变</f>";
	public static String 拖入羽炼符 = "请将羽炼符拖入进行强化";
	public static String 已经强化到了上限 = "翅膀已经炼星到了最大级别，不能进行炼星。";
	public static String 已经羽化到了上限 = "翅膀已经羽化到了最大级别，不能进行羽化。";
	public static String 翅膀初级强化符 = "羽炼符";
	public static String 翅膀中级强化符 = "羽炼符";
	public static String 羽炼符 = "羽炼符";
	public static String 达到收集上限 = "此种翅膀已达收集上限";
	public static String 翅膀主界面说明 = "本界面属性需佩戴翅膀后才可激活，所有属性将会加成至人物面板\n<f color='0xFFFF00'>光效宝石孔：</f>\n本界面右上角的粉色宝石栏为光效宝石孔，可镶嵌光效宝石，镶嵌后翅膀将增加粒子光效，需佩戴翅膀后镶嵌\n<f color='0xFFFF00'>基础属性：</f>\n翅膀种类由1号至10号，共10种，收集不同的翅膀种类，将获得基础属性，收集种类越多，属性越强，具体收集规则与奖励请查看规则界面\n<f color='0xFFFF00'>奖励属性：</f>\n同种翅膀收集至3,7,15,30个时，都将获得奖励属性，每种翅膀奖励属性不同，具体请查看对应的翅膀泡泡\n不同的等级可收集的数量上限不同，110收集上限为3个，150可收集上限为7个，190可收集上限为15个，220可收集上限为30个\n<f color='0xFFFF00'>宝石孔：</f>\n初始默认开启一个宝石孔，随翅膀种类收集将开启其他宝石孔，具体规则请查看规则界面，每个宝石孔镶嵌后，将获得镶嵌奖励";
	public static String 翅膀收藏界面说明 = "本界面为翅膀收藏界面，可在本界面查看和试穿翅膀和光效宝石\n<f color='0xFFFF00'>翅膀栏：</f>可预览翅膀外观、收藏数量、奖励属性\n<f color='0xFFFF00'>光效宝石栏：</f>可预览所有种类的光效宝石以及增加的属性种类\n<f color='0xFFFF00'>佩戴：</f>选择翅膀后，可点击本按钮佩戴翅膀\n<f color='0xFFFF00'>羽化预览：</f>当翅膀面板羽化后，所有翅膀形象均将改变，按本按钮可预览翅膀羽化后的形象\n<f color='0xFFFF00'>快速镶嵌：</f>选择光效宝石后，可点击本按钮进行镶嵌，点击本按钮后将弹出该光效宝石的所有级别，选择级别后可镶嵌，若未拥有该类宝石，可快速购买并且镶嵌";
	public static String 翅膀规则界面说明 = "翅膀说明待修改";
	public static String 收集翅膀激活宝石孔 = "收集以下任意翅膀组合时将激活宝石插槽：";
	public static String 集齐十种翅膀 = "集齐十种翅膀";
	public static String 集齐十种翅膀奖励 = "奖励:强化双攻 双防 气血";
	public static String 或 = "或";
	public static String 总是奖励对职业最有利攻修 = "总是奖励对职业最有利攻修";
	public static String 未佩戴翅膀 = "您尚未佩戴翅膀";
	public static String 佩戴成功 = "佩戴成功";
	public static String 卸载成功 = "卸载成功";
	public static String 收藏提示 = "@STRING_1@翅膀已添加至收藏列表，请前往翅膀-收藏界面查看";
	public static String 当前光效宝石有效期最长 = "您已镶嵌此类光效宝石";
	public static String 挖取位置上没有光效宝石 = "挖取位置上没有光效宝石,或光效宝石已到期";
	public static String 翅膀带光圈 = "当翅膀面板羽化后，本翅膀将带有神之光环效果";
	public static String 职业攻 = "功修为";
	public static String 双防 = "双防御";
	public static String 请放入羽炼物品 = "请放入羽炼物品。";
	public static String 翅膀强化失败 = "很遗憾，@ARTICLE_NAME_1@炼星失败了。当前星级为@LEVEL_1@";
	public static String 未佩戴翅膀不能快速镶嵌 = "您尚未佩戴翅膀,请先佩带翅膀";
	public static String 光效宝石到期失效 = "您镶嵌的光效宝石已经到期删除";
	public static String 等级不够开启翅膀 = "您的等级不符，110级才可开启，快快升级吧！";
	public static String 请先结束摆摊 = "请先结束摆摊";
	public static String 摆摊中不能上坐骑 = "摆摊中不能上坐骑";
	public static String 坐骑上不能摆摊 = "坐骑上不能摆摊";
	public static String 你不能使用这个物品 = "你不能使用这个物品";
	public static String 活动还没开启 = "活动还没开启";
	public static String 此功能暂未开放 = "此功能暂未开放";
	public static String 元 = "元";
	public static String 恭喜您获得了月卡奖励 = "恭喜您获得了立卡奖励";
	public static String 恭喜您领取了日返奖励 = "恭喜您领取了日返奖励";
	public static String 您还没购买该卡的服务 = "您还没购买该卡的领取天数，不能领取";
	public static String 该卡的领取天数为0 = "该卡的可领取天数为0,不能领取";
	public static String 请选择正确的卡 = "请选择正确的卡种";
	public static String 月卡购买成功 = "成功的购买了:";
	public static String 暂时不能充值联系GM = "暂时不能充值,请联系GM";
	public static String 花xx元购买xx = "您需要充值@COUNT_1@元才能获得@STRING_2@";
	public static String 等级不足不能买月卡 = "等级不足，10级才能购买";
	public static String 神匣珠 = "神匣珠";
	public static String 从匣子内取出宝石背包满提示 = "您的背包已满，请查收附件";

	// 运营新活动需求
	public static String 极地寻宝活动 = "混元宝库活动";
	public static String 极地寻宝活动邮件内容 = "混元宝库奖励邮件内容";
	public static String 请选择正确的冲级红利 = "请选择正确的冲级红利商品";
	public static String 您无法购买红利 = "您的等级不满足购买此商品的条件。";
	public static String 转盘发送邮件title = "每日转盘活动";
	public static String 转盘发送邮件content = "每日转盘活动奖励";
	public static String 恭喜您获得冲级返利 = "因购买冲级红利，您获得了%s银子。";
	public static String 扣除银子 = "扣除银子%s";
	public static String 需要消耗银子是否确定 = "寻宝需要消耗银子%s,是否确定";
	public static String 购买次数需要消耗银子 = "购买转盘次数需要消耗银子%s,是否确定";
	public static String 需要充值XX才可购买 = "当日需要充值%s元才能购买！";
	public static String 抽奖次数用尽 = "今日抽奖次数已用尽!";

	public static String 兽形态不能挂机 = "兽形态不能挂机";
	public static String 学习变身后技能提示 = "您即将消耗人物经验<f color='0xfff600' size='28'>@COUNT_1@</f>和绑银<f color='0xfff600' size='28'>@STRING_1@</f>来升级该技能,您确认升级？";
	public static String 举报的玩家不存在 = "举报失败，您要举报的玩家不存在";
	public static String 举报失败 = "举报失败，请您联系客服";
	public static String 二十级以上的玩家才可以举报 = "举报失败，20级以上的玩家才可以举报";
	public static String 举报客户信息 = "如有游戏问题咨询请联系：6009889988";
	public static String 充值商城等级限制提示 = "等级到达70级后即可参与.";
	public static String 充值商城暂未开启 = "该商场暂未开启";
	public static String 用xx腾讯币兑换xx银两 = "您是否用@COUNT_1@腾讯币兑换@COUNT_2@银两?";
	public static String 用腾讯币兑换银两 = "充值成功,请用腾讯币兑换游戏银两";
	public static String 比武擂台 = "比武区域";
	public static String 镶嵌等级不符 = "您当前镶嵌的宝石等级不能大于首颗宝石的等级。";
	public static String 镶嵌顺序不符 = "您需要将相应属性的宝石放入首颗宝石孔，才可激活宝石匣，继续操作";
	public static String 镶嵌顺序不符2 = "请先往首孔镶嵌宝石试试哦(必须先镶嵌首孔宝石,才可激活宝石匣)";
	public static String 必须先拿掉副宝石 = "请先移除副宝石孔的宝石！";
	public static String 匣子中含有绑定宝石 = "由于镶嵌的 @ARTICLE_NAME_1@ 中有绑定的宝石，镶嵌后装备也绑定！确定要将 @ARTICLE_NAME_1@ 镶嵌到 @ARTICLE_NAME_2@ 上吗？";
	public static String 携带等级不满足飞升 = "需要宠物携带等级220";
	public static String 基础技能个数不满足飞升 = "需要宠物基础技能5个以上";
	public static String 天赋技能个数不满足飞升 = "需要宠物天赋技能5个以上";
	public static String 此宠物不可飞升 = "此宠物不可飞升";
	public static String 此宠物魂值不满 = "需要宠物妖魂值达到上限";
	public static String 易筋丹次数已达上限 = "易筋丹次数已达上限,请服用升华露";
	public static String 易筋丹还在cd中 = "易筋丹还没消化完全,请CD结束后再使用";
	public static String 背包中易筋丹数量不足 = "背包中易筋丹数量不足,需要@COUNT_1@个";
	public static String 消耗易筋丹XX个 = "消耗背包中 易筋丹*@COUNT_1@ 增加灵性潜能值？";
	public static String 清除易筋丹CD = "花费@STRING_1@银子清除易筋丹剩余消耗时间？";
	public static String 消耗易筋丹成功 = "恭喜您获得了@COUNT_1@灵性和@COUNT_2@点灵性潜能.";
	public static String 消耗易筋丹成功2 = "恭喜您获得了@COUNT_1@灵性";
	public static String 吃易筋丹成功 = "易筋丹正在消化中.";
	public static String 出战状态的宠物才能使用升华露 = "出战状态的宠物才能使用升华露";
	public static String 使用升华露异常 = "需要先吃易筋丹灵性达到50才可以使用";
	public static String 使用升华露灵性不够 = "灵性值大于50才可以使用";
	public static String 使用升华露次数达上限 = "使用升华露次数已达上限";
	public static String 灵性值已达上限 = "灵性值已达上限";
	public static String 灵性值100不可使用 = "灵性>100不可使用升华露";
	public static String 清除易经丹cd失败 = "已消化完毕";
	public static String 清除易经丹cd成功 = "清除消耗时间成功";
	public static String 需要灵性达到105 = "需要灵性达到105方可领悟飞升技能";
	public static String 反生丹 = "返生丹";
	public static String 顿悟丹 = "顿悟丹";
	public static String 是否使用顿悟丹领悟飞升技能 = "每使用一颗顿悟丹可领悟一次宠物飞升技能，已有的宠物飞升技能会消失.";
	public static String 背包中没有顿悟丹 = "背包中没有顿悟丹";
	public static String 背包中没有反生丹 = "背包中没有返生丹";
	public static String 领悟飞升技能成功 = "领悟飞升技能成功";
	public static String 使用反生丹成功 = "使用返生丹成功";
	public static String 飞升需要达到图鉴最高阶 = "宠物需要达到图鉴最高阶";
	public static String 使用反生丹 = "消耗返生丹灵性重置到50点";
	public static String 宠物飞升提示85 = "灵性达到50时转换为飞升形象";
	public static String 宠物飞升提示105 = "灵性达到105时可学习飞升技能";
	public static String 吃升华露成功描述 = "恭喜您获得了@COUNT_1@灵性，@COUNT_2@点灵性潜能.";
	public static String 吃升华露成功描述2 = "恭喜您获得了<f color='0xfff600'>@COUNT_1@</f>灵性，<f color='0xfff600'>@COUNT_2@</f>点灵性潜能.";
	public static String 吃升华露成功暴击描述 = "恭喜您获得了@COUNT_1@灵性@STRING_1@，@COUNT_2@点灵性潜能@STRING_2@";
	public static String 需要玩家飞升宠物才可以飞升 = "需要玩家飞升之后才可以进行宠物飞升";
	public static String 需要玩家飞升才可以使用 = "需要玩家飞升之后才可以使用此宠物";

	public static String 不能打开仙婴界面 = "需要玩家等级大于等于221";
	public static String 仙婴还未加点 = "仙婴天赋还未加点";
	public static String 仙婴加点已满 = "仙婴加点已满";
	public static String 仙婴已到最高等级 = "仙婴已到最高等级";
	public static String 经验兑换成功 = "恭喜您获得了@COUNT_1@仙婴经验";
	public static String 可兑换的仙婴为0 = "兑换仙婴经验为0,无法兑换";
	public static String 仙婴等级不能大于玩家等级 = "仙婴等级不得超过人物等级";
	public static String 今天已经修炼过了 = "今天已经修炼过了";
	public static String 没有剩余点数 = "没有可用点数";
	public static String 还没分配点数 = "本天赋已确定加点,无法撤销";
	public static String 天赋技能点已经加满 = "天赋点已经加满";
	public static String 取消加点 = "取消加点成功";
	public static String 重置加点 = "重置加点成功";
	public static String 重置失败 = "重置加点失败";
	public static String 重置加点失败 = "您还没有加点,无需重置";
	public static String 确认加点 = "加点成功";
	public static String 您还没有分配加点 = "您还没有分配天赋点";
	public static String cd中不能使用 = "仙婴附体还在cd中,无法使用";
	public static String 仙婴附体中不能操作 = "仙婴附体中不能操作";
	public static String 肩负使命 = "肩负使命";
	public static String 赐予仙婴 = "赐予仙婴";
	public static String 天赋提升等级 = "(+@COUNT_1@)";
	public static String 上一层天赋点数不够 = "前置点数未加够,当前天赋未激活";
	public static String 前置技能没加满 = "前置天赋没加满";
	public static String 还没开启仙婴天赋 = "还没开启仙婴天赋";
	public static String 使用次数已达上限 = "已使用过5颗,无法再使用本类丹药";
	public static String 减天赋cd道具使用成功 = "您的仙婴cd时间永久缩短@COUNT_1@分钟,当前是cd是@COUNT_2@分钟";
	public static String 您处于冰冻状态下不能使用 = "您处于冰冻状态下，不能使用";
	public static String 完美仙婴元魄 = "完美仙婴元魄";
	public static String 飞升后才能仙婴 = "需要飞升后才可以操作！";
	public static String 查看的玩家飞升后才能仙婴 = "查看的玩家飞升后才可以操作！";
	public static String 背包中没有仙婴天赋重置丹 = "背包中没有仙婴天赋重置丹";
	public static String 仙婴天赋重置丹 = "仙婴天赋重置丹";
	public static String 消耗仙婴天赋重置丹 = "需要消耗1个仙婴天赋重置丹，是否重置？";
	public static String 稳如泰山 = "稳如泰山";
	public static String 势不可挡 = "势不可挡";
	public static String 小于1分钟 = "小于1分钟";
	public static String 灵性点数 = "灵性点数";
	public static String 飞升技能 = "飞升技能";
	public static String 获得新的仙婴天赋点 = "恭喜您获得了新的仙婴天赋点";

	// 新增仙录
	public static String 特殊处理NPC对话弹窗1 = "中国古代神话中最令妖邪胆战且法力无边的四大神兽是青龙、白虎、朱雀、玄武四兽。玄武亦称玄冥 ，龟蛇合体，为水神，居北海，龟长寿，玄冥成了长生不老的象征，冥间亦在北方，故为北方之神。青龙白虎掌四方，朱雀玄武顺阴阳，而玄武又可通冥间问卜，因此玄武有别于其它三灵 ，被称为“真武大帝”，又叫“玄武祖师”是道教所奉之神。";
	public static String 特殊处理NPC对话弹窗2 = "心印经：“上药三品，神与气精。恍恍惚惚，杳杳冥冥。存无守有，顷刻而成。回风混合，百日功灵。默朝上帝，一纪飞升。智者易悟，昧者难行。履践天光，呼吸育清。出玄入牝，若亡若存。绵绵不绝，固蒂深根。人各有精，精合其神；神合其气，气合其真。不得其真，皆是强名。神能入石，神能飞形。”";
	public static String 特殊处理NPC对话弹窗3 = "北斗七星与天、地、人、时、音、律、星相配，称：“天枢为天，天璇为地，天玑为人，天权为时，玉衡为音，闿(开)阳为律，摇光为星。”并喊予其神性职能。";
	public static String 特殊处理NPC对话弹窗4 = "张元伯，名劭、字元伯，汝南人。生卒年不详，约东汉初年前后在世。少游太学，与山阳范式友善。临别于二年后某日到邵家拜母。至期，劭告母，设馔以代，式果至。登堂拜母，尽欢而别。";

	/** 仙界宝箱 */
	public static String 大量人物经验广播 = "恭喜@STRING_1@在仙界宝箱活动中获得了<f color='@STRING_2@'>大量@STRING_3@</f>";
	public static String 海量人物经验广播 = "恭喜@STRING_1@在仙界宝箱活动中获得了<f color='@STRING_2@'>海量@STRING_3@</f>";
	public static String 获得工资或元气点广播 = "恭喜@STRING_1@在仙界宝箱活动中获得了<f color='@STRING_2@'>@STRING_3@@STRING_4@</f>";
	public static String 获得物品广播 = "恭喜@STRING_1@在仙界宝箱活动中获得了<f color='@STRING_2@'>@STRING_3@</f>";
	public static String 获得经验工资或元气点 = "恭喜您获得了<f color='@STRING_2@'>@STRING_3@@STRING_4@</f>";
	public static String 获得物品 = "恭喜您获得了<f color='@STRING_2@'>@STRING_3@</f>";
	// public static String[] 宝箱祈福描述 = new String[] { "人物经验", "神兽碎片", "宠物经验", "道具" };
	public static String buff描述 = "宝箱祈福状态";
	public static String 别人正在开箱子你不能同时进行 = "别人正在开这个宝箱，你不能同时进行";
	public static String 宝箱消失 = "宝箱已消失或被别人打开";
	public static String 已达今日上限 = "已达到上限,明天再开吧";
	public static String 活动结束 = "活动已结束";
	public static String 祈福成功 = "祈福成功，开启仙界宝箱时获得@STRING_1@的几率增加";
	public static String 祈福buff名 = "仙界宝箱祈福";
	public static String 祈福buff描述 = "仙界宝箱祈福描述";
	public static String 花费银子开宝箱提示 = "您没有对应宝箱钥匙,确定要花费银子<f color='0xfff600'>@STRING_1@</f>开启吗?今天还可以开启<f color='0xfff600'>@STRING_2@</f>次";
	public static String 消耗钥匙开宝箱提示 = "您确定使用<f color='0xfff600'>@STRING_1@</f>开启吗?今天还可以开启<f color='0xfff600'>@STRING_2@</f>次";
	public static String 花费银子祈福提示 = "确定要花费银子<f color='0xfff600'>@STRING_1@</f>祈福吗";
	public static String 延迟邮件标题 = "补发道具";
	public static String 延迟邮件内容 = "您之前开启@STRING_1@获得的物品，未能正常获得，现为您补发道具";
	public static String 开宝箱失败 = "开宝箱失败";

	public static String 五方圣兽圣坛称号1 = "五方圣兽圣坛称号1";
	public static String 五方圣兽圣坛称号2 = "五方圣兽圣坛称号2";
	public static String 五方圣兽圣坛称号3 = "五方圣兽圣坛称号3";
	public static String 五方圣兽圣坛称号4 = "五方圣兽圣坛称号4";
	public static String 五方圣兽圣坛称号5 = "五方圣兽圣坛称号5";

	public static String 改名卡 = "改名卡";
	public static String 没有改名道具 = "您背包中没有改名卡";
	public static String 不是授权设备或授权不超过三天 = "抱歉,当前设备未授权或者设备授权不满三天,不能改名";
	public static String 角色名为空 = "您输入的角色名为空,不能改名";
	public static String 角色名已存在 = "角色名已存在";
	public static String 改名失败 = "改名失败";
	public static String 改名太频繁 = "您还有@STRING_1@可再次改名";

	public static String 洗炼说明 = "可消耗【洗炼符】对装备进行洗炼，附加属性条数随机获得。";
	public static String 洗炼成功 = "洗炼成功";
	public static String 洗炼符 = "洗炼符";
	public static String 未放入装备或者洗炼符 = "未放入装备或者洗炼符";
	public static String 查询装备洗炼请求错误回复 = "@ARTICLE_NAME_1@不是装备，不能进行洗炼。";
	public static String 确定要开始洗炼吗绑定的 = "您使用了绑定的@ARTICLE_NAME_1@，洗炼后@ARTICLE_NAME_2@会绑定，确定要开始洗炼吗？";
	public static String 橙色以下 = "洗炼符可洗炼橙色和完美橙装备，其他颜色装备无法洗炼";
	public static String 洗炼已满 = "洗炼已满";
	public static String 进阶成功 = "坐骑进阶成功";
	public static String 培养不足 = "坐骑培养不足，请先进行坐骑培养";
	public static String 不需要使用 = "当前坐骑不需要使用此道具";

	public static String 骰子活动开启通告信息 = "骰仙迷城活动入口已开启，仅持续3分钟，快来参加吧";
	public static String 骰子活动报名还没开始 = "活动还未开启";
	public static String 报名时间已经结束 = "报名时间已经结束";
	public static String 已经报名 = "您已经报名";
	public static String 前几名进去 = "前@COUNT_1@名进入下一层";
	public static String 刷boss倒计时 = "刷boss倒计时:";
	public static String 副本倒计时 = "副本倒计时:";
	public static String 骰子副本不能拉人 = "副本中不能操作";
	public static String 对方正在副本 = "对方正在副本，不能操作";
	public static String boss已刷新 = "boss已刷新，快去击杀";
	public static String 副本挑战失败 = "挑战失败,传出副本";
	public static String 骰子副本不能原地复活 = "骰子副本不能原地复活";
	public static String 当前人数不够 = "参与的玩家人数不足,副本关闭,将在3秒后传出副本";
	public static String 报名人数已达上限 = "报名人数已达上限";
	public static String 活动报名时间已经结束 = "活动已经结束";
	public static String 活动配置错误 = "活动配置错误";
	public static String 参与时间 = "星期三和星期五，21:30-21:55。22:00-22:25。";
	public static String 参与方式 = "在圣魂灵域寻找NPC骰仙参与活动。";
	public static String 进入仙居 = "活动开启后3分钟内进入，每次只能进入300人。";
	public static String 活动介绍 = "进入迷城击杀神秘Boss后进行骰子投掷，排名在半数之前的玩家进入下一层，最终剩下的一个人将获得最终奖励";

	public static String 投票确认 = "今日还未完成国家官员投票，是否投票！（投票后可领取奖励！）";
	public static String 投票福利提示 = "投票成功，恭喜您获得BUFF【野外打怪经验加成50%】。";
	public static String 矿战报名结果 = "本次矿战报名结果：";
	public static String 第一名 = "第一名：";
	public static String 第二名 = "第二名：";
	public static String 第三名 = "第三名：";
	public static String 骰子点数 = "@STRING_1@，骰子点数@COUNT_1@点";
	public static String 矿战排行失败 = "您本次矿战的排行为@COUNT_1@名，骰子点数为@COUNT_2@点，很抱歉，您的家族本次无缘矿战，下次祝您好运";
	public static String 矿战排行成功 = "您本次矿战的排行为@COUNT_1@名，骰子点数为@COUNT_2@点，恭喜您报名成功";
	public static String 矿战申请结果通知 = "本次获得挑战矿战的家族是：@STRING_1@家族，请其他家族继续努力，征战巅峰！";
	public static String 您已经申请攻打其它矿 = "您已经申请攻打其它矿";
	public static String 城战报名结果 = "本次城战报名结果：";
	public static String 城战排行失败 = "您本次城战的排行为@COUNT_1@名，骰子点数为@COUNT_2@点，很抱歉，您的家族本次无缘城战，下次祝您好运";
	public static String 城战排行成功 = "您本次城战的排行为@COUNT_1@名，骰子点数为@COUNT_2@点，恭喜您报名成功";
	public static String 城战申请结果通知 = "本次获得挑战城战的宗派是：@STRING_1@宗派，请其他宗派继续努力，征战巅峰！";
	public static String 野外打怪经验加成 = "野外打怪经验加成";

	public static String N级开启 = "@STRING_1@级开启";
	public static String 魂石不存在 = "要鉴定的魂石不存在!";
	public static String 材料不存在 = "鉴定魂石所需要的材料不存在!";
	public static String 魂石鉴定描述 = "<f color='0xffffff00'>魂石鉴定后将获得魂石基础属性，并有几率获得隐藏属性附加融合成功率，魂石颜色越高，需要消耗的</f>@STRING_1@<f color='0xffffff00'>越多</f>";
	public static String 魂石神隐洗炼提示 = "<f color='0xffffff00'>使用</f>@STRING_1@<f color='0xffffff00'>洗炼，将获得额外一条随机的附加融合率属性，当前魂石最多可神隐一次</f>";
	public static String 鉴定的提示 = "如果没有@STRING_1@，则每次消耗银子";
	public static String 带颜色的道具 = "<f color='@STRING_1@'>@STRING_2@</f>";
	public static String 魂石已鉴定过 = "此魂石已经鉴定过,您不能重复鉴定!";
	public static String 魂石已洗炼过 = "此魂石已洗炼过,您不能重复洗炼!";
	public static String 没有鉴定符 = "您没有魂石鉴定符,不能进行鉴定!";
	public static String 魂识符 = "魂识符";
	public static String 灵隐符 = "灵隐符";
	public static String 请放入鉴定符 = "请放入魂石鉴定符";
	public static String 材料错误 = "@STRING_1@错误，请重新放入";
	public static String 查询魂石鉴定请求错误回复 = "@ARTICLE_NAME_1@不是魂石，不能进行鉴定。";
	public static String 查询魂石洗炼请求错误回复 = "@ARTICLE_NAME_1@不是魂石，不能进行洗炼。";
	public static String 魂石鉴定需要放入魂石和材料 = "魂石鉴定需要放入魂石和@STRING_1@ ";
	public static String 鉴定成功 = "鉴定成功";
	public static String 基础融合率 = "基础融合率";
	public static String 附加融合率 = "附加融合率";
	public static String 总融合率 = "总融合率";
	public static String 已鉴定 = "已鉴定";
	public static String 未洗炼 = "未洗炼";
	public static String 凝神魂石 = "凝神魂石";
	public static String 随机带窍魂石 = "随机带窍魂石";
	public static String 未鉴定不能镶嵌 = "未鉴定不能镶嵌";
	public static String 未镶嵌魂石 = "未放入魂石，暂时无法查看";
	public static String 未鉴定不能合成 = "未魂识不能合成";
	public static String 未神隐不能合成 = "未神隐不能合成";
	public static String 魂石合成描述 = "<f color='0xffff0000'>注：</f><f color='0xffffff00'>使用相同颜色、名称的魂石可以合成更高颜色的魂石，融合成功率越高的魂石成功率越高，如融合失败，所有合成材料将消失！</f>";
	public static String 套装石合成描述 = "<f color='0xffff0000'>注：</f><f color='0xffffff00'>套装石拥有不同的套装属性技能，按照套装石公式所需魂石进行合成！如融合失败，所有合成材料将消失</f>";
	public static String 随机属性 = "(随机获得@COUNT_1@条属性)";
	public static String 魂石镶嵌描述 = "<f color='0xffff0000'>注：</f><f color='0xffffff00'>无窍魂石可镶嵌任意位置\n有窍魂石需要固定窍位进行镶嵌</f>";
	public static String 套装魂石镶嵌描述 = "<f color='0xffff0000'>注：</f><f color='0xffffff00'>镶嵌不同的套装石将获得不同的套装技能，不可重复佩戴</f>";
	public static String 不可重复镶嵌同类型套装魂石 = "不可重复镶嵌同类型套装魂石";
	public static String 请放入魂石 = "请放入魂石";
	public static String 不能挖取或挖取位置错误 = "不能挖取或挖取位置错误";
	public static String 挖取位置上没有魂石 = "挖取位置上没有魂石";
	public static String 人物N级开启 = "玩家达到@STRING_1@，进行开放";
	public static String 人物N级付费开启 = "当玩家等级达到@STRING_1@级，消耗@STRING_2@银子开启";
	public static String 是否付费开启 = "是否花费@STRING_1@银子开启此格？";
	public static String 无法回血 = "无法回血";
	public static String 窍 = "窍";
	public static String 对方未开启魂石 = "对方未开启魂石";
	public static String 套装技能 = "套装技能";
	public static String 神隐成功 = "神隐成功";

	public static String 等级太低不能报名 = "等级太低，需要达到@COUNT_1@才可以报名";
	public static String 队伍中不能报名 = "队伍中,不能报名";
	public static String 狼抓羊活动开启通知 = "天阙幻境活动即将在@COUNT_1@分钟后开启,请到灭魔神域天阙幻境npc处报名";
	public static String 家族远征活动即将开启通知 = "家族远征即将在@COUNT_1@分钟后开启,请到家族地图寻找家族总管npc处参加";
	public static String 家族远征活动开启通知 = "家族远征开启,请到家族地图寻找家族总管npc处参加";
	public static String 规则1 = "每场活动限时3分钟，且每场参与人数为5人";
	public static String 失败描述 = "大灰狼识破小羊咩咩0人，小羊咩咩全体被识破";
	public static String 成功描述 = "大灰狼识破小羊咩咩至少1人，小羊咩咩至少1人存活";
	public static String 封神描述 = "大灰狼识破小羊咩咩4人，小羊咩咩存活4人";
	public static String 单人 = "单人";
	public static String 次数 = "@COUNT_1@次";
	public static String 活动时间 = "每周2下午15:00-16:00";
	public static String 大灰狼 = "3分钟内从30只小羊中找出由玩家伪装而成的小羊";
	public static String 小绵羊 = "3分钟内保持不被大灰狼识破";
	public static String 取消报名成功 = "取消报名成功";
	public static String 匹配成功 = "匹配成功";
	public static String 战斗开始 = "战斗开始,狼来了,隐蔽起来";
	public static String 死亡信息 = "存活@COUNT_1@人,死亡@COUNT_2@人";
	public static String 副本时间到 = "副本时间到,传出场景";
	public static String 无效的目标 = "无效的目标";
	public static String 识破一只小羊 = "你识破了@STRING_1@,获得@COUNT_1@经验";
	public static String 通知识破一只小羊 = "@STRING_1@被大灰狼识破!";
	public static String 被识破 = "被大灰狼识破";
	public static String 神奇大宝箱已刷新 = "天阙大宝箱已刷新";
	public static String 不能使用技能 = "您目前不能使用该技能";
	public static String 被识破不能使用技能 = "您已被识破,不能使用技能";
	public static String 识破小羊 = "识破小羊咩咩@COUNT_1@只";
	public static String 存活小羊 = "存活小羊咩咩@COUNT_1@只";
	public static String 您的参与次数不足 = "您的参与次数不足";
	public static String 小羊活动不允许组队 = "被邀请者正在活动中，不能邀请组队!";
	public static String 小羊活动不允许被邀请 = "队长正在活动中，不能申请进入队伍!";
	public static String 参加活动限制回城 = "活动中,不能使用此类道具";
	public static String 匹配成功不能报名 = "活动中，不能报名";
	public static String 此副本不能回城 = "此副本不能回城";
	public static String 携带法宝不能报名 = "请您先脱下法宝后再参与活动，否则您将会成为优先击杀目标！";
	public static String 不能操作 = "您不能采集草";
	public static String 不能拾取 = "不能拾取";
	public static String 副本中不能使用坐骑 = "此副本中不能使用坐骑！";
	public static String 副本中不能召唤宠物 = "此副本中不能召唤宠物！";
	public static String 副本中不能组队 = "此副本中不能组队！";
	public static String 副本中不能聊天 = "此副本中不能聊天！";
	public static String 骰子奖励通过邮件发送 = "您背包已满,骰仙奖励通过邮件发送";
	public static String 此物品不可以采集 = "羊不能采集宝箱";
	public static String 狼信息 = "尽量捕杀小羊获得更多经验奖励！";
	public static String 羊信息 = "请尽量躲避大灰狼的追杀！";
	public static String 副本不能渡劫提示 = "您正在参与天阙活动,暂时不能渡劫";
	public static String 副本不能语音 = "该玩家正在参与活动，暂时无法接收您的信息，请稍后再试。";
	public static String 法宝报名弹框 = "您报名参加该活动后，建议您不要参加其他副本类活动，否则您将会被强制拉出<f color='" + TaskConfig.noticeColor + "'>（建议您先将法宝摘下否则您将会首先被追杀哦！）</f>";

	public static String 全天 = "全天";
	public static String 炼化二次确认 = "被炼化的物品中有高级灵髓宝石，是否确定炼化？";

	public static String 宠物官方名称 = "宠物官方名称";
	public static String 幻化等级 = "幻化（强星）等级";
	public static String 法宝加持等级 = "法宝加持等级";

	public static String 家族等级不足 = "家族等级不足";
	public static String 家族远征奖励 = "家族远征奖励";

	public static String 正在报名请等待 = "报名成功,请等待进入跨服战场";
	public static String 报名成功是否进入 = "报名成功，是否进入PVP专服？进入后请前往NPC武神使者处参与活动";
	public static String pk副本时间到 = "副本结束，5秒后将传出场景";
	public static String pk副本时间到2 = "副本时间到，15秒后将传出场景";
	public static String pk副本结束 = "副本结束，15秒后将传出场景";
	public static String 房间名不能为空 = "房间名不能为空";
	public static String 房间名不能相同 = "不能有同名房间";
	public static String 房间名含有特殊符号 = "房间名不能有特殊符号";
	public static String 房间名长度不符 = "房间名最长不超过10个字节";
	public static String 房间密码长度不符 = "密码最长不超过6个字节";
	public static String 踢出房间的玩家不存在 = "房间没有人，踢出失败";
	public static String 您被踢出房间 = "您被踢出房间";
	public static String 密码只能是6个字符之内的数字 = "密码只支持数据,不超过6个字节";
	public static String 玩家等级不足 = "赶快去升级吧，150级以上玩家才可以参加武神之巅";
	public static String 加入匹配队列中 = "正在匹配，请等候.";
	public static String 匹配成功进入pk场景 = "匹配成功，点开始战斗进入pk场景";
	public static String 房间不存在 = "房间已经不存在了";
	public static String 不能加入自己创建的房间 = "不能加入自己创建的房间";
	public static String 战斗开始了 = "战斗开始了";
	public static String 准备状态不能使用 = "准备阶段不能使用";
	public static String 人满了不能加入 = "人满了不能加入";
	public static String 您被踢出房间10秒内不能加入 = "您被踢出房间10秒内不能加入";
	public static String 开始战斗失败房间没人 = "房间里没人，不能开始战斗";
	public static String pk的玩家不在线 = "挑战的玩家不在线";
	public static String 挑战者还没准备 = "挑战的玩家还没准备";
	public static String 匹配成功没准备 = "赢得了比赛，对方在倒计时内没准备战斗";
	public static String 匹配成功没准备B = "输了比赛，在倒计时内没准备战斗";
	public static String 加入房间的房主不在线 = "加入房间的房主不在线";
	public static String 目前没有玩家匹配 = "您太强大了,尚未匹配到对手,请稍后再匹配";
	public static String 准备进入成功 = "准备成功，等待对方准备";
	public static String 对方死亡获得胜利 = "战胜了对方，获得胜利";
	public static String 你死了输掉了比赛 = "输掉了本场比赛";
	public static String 你死了输掉了比赛获得了积分 = "获得20武神积分";
	public static String 你赢了比赛获得了积分 = "获得50武神积分";
	public static String 不能重复创建房间 = "不能重复创建房间";
	public static String 武神兑换商店 = "武神兑换商店";
	public static String 报名 = "报名";
	public static String 跨服向原服发送积分报错 = "原服务器异常,将无法获得积分奖励,请回原服重新报名参与";
	public static String 积分不够 = "积分不够";
	public static String 比武倒计时 = "比武倒计时:";
	public static String 武神之巅 = "武神之巅";
	public static String 跨服中不能切换pk模式 = "跨服中不能切换pk模式";
	public static String 跨服积分周排行奖励 = "跨服积分周排行奖励";
	public static String 武神积分 = "本周武神积分";
	public static String 跨服将在5秒后关闭 = "活动结束，跨服将在5秒后关闭";
	public static String 请等待仙婴状态结束后再参与本活动 = "请等待仙婴状态结束后再参与本活动";
	public static String 武神至尊 = "武神至尊";
	public static String 准备状态下不能离开房间 = "准备状态下不能退出房间";
	public static String 武神至尊参加人数太多 = "当前参与人数过多，暂时无法参与，请稍后再报名";
	public static String 刷新跨服商店 = "是否消耗@COUNT_1@武神积分刷新(今日剩余刷新次数：@COUNT_2@次)";
	public static String 活动结束即将传出 = "活动结束，将在5秒后传出场景";
	public static String 对方掉线获得了成功 = "对方玩家掉线，您获得了胜利,5秒后传出场景";
	public static String 周奖励预览信息 = "<f>周奖励设定(需本周武神积分达到200分以上才可获得周奖励)</f>\n<f>第1名：</f>\n<f color='0xffFD6B00'>“武神至尊”彩色称号*1</f><f>(限时一周)，5200武神积分，神兽仙囊*3，蓝色玉液礼包(绑)*3</f>\n\n" + "<f>第2名：</f>\n<f>4900武神积分，神兽仙囊*2，蓝色玉液礼包(绑)*3</f>\n\n" + "<f>第3名：</f>\n<f>4700武神积分，神兽仙囊*1，蓝色玉液礼包(绑)*3</f>\n\n" + "<f>第4名：</f>\n<f>4400武神积分，蓝色玉液礼包(绑)*3</f>\n\n" + "<f>第5名：</f>\n<f>4200武神积分，蓝色玉液礼包(绑)*2</f>\n\n" + "<f>第6名：</f>\n<f>3900武神积分，蓝色玉液礼包(绑)*1</f>\n\n" + "<f>第7名：</f>\n<f>3600武神积分，蓝色玉液礼包(绑)*1</f>\n\n" + "<f>第8名：</f>\n<f>3400武神积分，蓝色玉液礼包(绑)*1</f>\n\n" + "<f>第9名：</f>\n<f>3100武神积分，蓝色玉液礼包(绑)*1</f>\n\n" + "<f>第10名：</f>\n<f>2800武神积分，蓝色玉液礼包(绑)*1</f>\n\n" + "<f>第11-20名：</f>\n<f>2600武神积分，绿色玉液礼包(绑)*3</f>\n\n" + "<f>第21-50名：</f>\n<f>2300武神积分，绿色玉液礼包(绑)*2</f>\n\n" + "<f>第51-100名：</f>\n<f>2100武神积分，绿色玉液礼包(绑)*1</f>\n\n" + "<f>100名以后：</f>\n<f>1800武神积分，白色玉液礼包(绑)*2</f>\n";
	public static String 跨服报名帮助信息 = "<f>武神之巅规则：</f>\n当进入后:\n<f>1.无法更替装备/翅膀/法宝等影响属性的道具</f>\n<f>2.无法切换元神</f>\n<f>3.无法更换出战宠物</f>\n<f>请在本服设置好以上道具及出战宠物后，再开始报名参与</f>\n报名需等待少许时间，报名成功后将会弹出提示框";
	public static String 一天只能刷新10次 = "今日已无剩余刷新次数，请明日再刷";
	public static String 跨服开启说明 = "<f>玩法说明：</f>\n" + "<f>本玩法将提供与其他服务器的高手切磋，一决高下的机会！(</f><f color='0xffFD6B00'>需≥150级方可参与本活动</f><f>)</f>\n" + "<f>在切磋的同时还可获得</f><f color='0x00ff00'>武神积分</f><f>，</f><f color='0x00ff00'>武神积分</f>可换购各种<f color='0xffFD6B00'>奇珍异宝！</f>\n" + "<f>参与本玩法，需在本服先进行</f><f color='0xffFD6B00'>报名</f><f>，报名需少许等待时间，报名成功后方可前往</f><f color='0xffFD6B00'>PVP专用服务器征战</f>\n" + "<f color='0xff0000'>注：</f><f>根据自身服务器封印等级，将前往</f><f color='0xffFD6B00'>不同的PVP服</f>\n" + "<f>如150封印等级所在服务器的玩家均前往150PVP服，在150PVP服无法遇到其他封印等级服的玩家（如，无法遇到220级封印等级服务器的玩家）</f>\n";
	public static String 跨服3v3开启说明 = "玩法说明\n<f>活动时间：</f>" + "@STRING_1@，\n活动说明：<f color='0xffFD6B00'>玩家每周进行跨服3V3比赛之后可以获得血石，在对应商店中使用血石可购买到多种物品</f>";
	public static String 跨服规则说明 = "<f>1.</f><f color='0x00ff00'>武神积分</f><f>可在各服的“武神传送使者”处</f><f color='0xffFD6B00'>兑换奖励</f>\n" + "<f>2.</f><f color='0xffFD6B00'>每天前五场匹配</f><f>可获得</f><f color='0x00ff00'>武神积分</f><f>（单场奖励 胜：50分；负：20分；未进场不得分）</f>\n" + "<f>3.</f><f>匹配次数为0后，仍可继续匹配，但不再获得积分奖励</f>\n" + "<f>4.</f><f>自由模式不会获得积分，也不会影响胜率及场数</f>\n" + "<f>5.</f><f>每周日晚10点结算，按照本周的武神积分排行发放</f><f color='0x00ff00'>周奖励</f><f>，排名越高，奖励越好</f>\n" + "<f>6.</f><f>需在</f><f color='0xffFD6B00'>本周内至少获得200分</f><f>才可获得</f><f color='0x00ff00'>周奖励</f>\n" + "<f>7.</f><f>武神排行榜</f><f color='0xffFD6B00'>只记录本周排行</f><f>，每周日结算后</f><f color='0xff0000'>清空</f>\n";

	/** 仙灵大会 */
	public static String 高级关卡只能挑战一次 = "高级关卡只能挑战一次";
	public static String 前面还有未开启的关卡 = "前面还有未开启的关卡";
	public static String 仙灵主面板帮助 = "<f>仙灵大会规则：</f>\n<f>1、仙灵大会中捕捉完成后不会获得宠物，只会获得积分；</f>\n<f>2、捕捉不同稀有度的宠物可获得不同的积分，普通(白色)：100分，普通精英(绿色)：130分，高级精英(蓝色)：150分，普通BOSS(紫色)：400分，高级BOSS(橙色)：1000分；</f>\n<f>3、关卡分为普通关卡和高级关卡，普通关卡可以随意进入，无进入次数限制；高级关卡必定遇到高级BOSS，但此关卡只可进入一次，一旦点击遇见后无法再次进入；</f>\n<f>4、仙灵大会中只可使用指定技能【捉妖灵葫】捕捉宠物，捕捉时，捕捉读条不会被打断，【捉妖灵葫】技能使用时需要消耗道具乾坤石，乾坤石可在商店进行购买；</f>\n<f>5、仙灵大会中只可使用系统提供的技能进行攻击，【怒血强攻】和【黑夜魔影】两个技能同样也需要消耗道具，烈血石和巫魔石可在商店进行购买；</f>\n<f>6、切记进入关卡前，补齐技能所需道具，否则无法进行捕捉；</f>\n<f>7、仙灵大会中的积分只用于仙灵大会的排名和积分奖励中，在每次活动结束后清除；</f>\n<f>8、捕宠积分排行榜每5分钟刷新一次，在活动结束后排行榜会进行最后一次的结算刷新，并在10分钟后陆续发放奖励；</f>\n<f>9、在活动结束后捕获的宠物不计入算积分；</f>\n<f>10、每次进入关卡消耗一点真气，真气每15分钟恢复一次，超出时继续累计；</f>\n<f>11、仙灵大会中可以使用buff或者积分卡来提升你的排名，buff和积分卡可同时使用，两种增益效果有效期为30分钟。</f>";
	public static String 仙灵排行榜帮助 = "<f>1、排行积分计算活动期间获得的总积分，进入跨服排行榜积分需在20000分以上；</f>\n<f>2、排行榜每5分钟刷新一次，在活动结束后进入5分钟结算时间，结算期间排行榜会刷新最后的排名；</f>\n<f>3、若排行积分相同，先达到的玩家排在前；</f>\n<f>4、排行跨服排行奖励在活动结束后以邮件形式发放；</f>\n<f>5、玩家最终排名奖励，根据跨服排名进行发放，本服排名暂时不参与奖励。</f>";
	public static String 遇到目标怪 = "您已遇到限时任务怪，请谨慎操作";
	public static String 限时任务描述 = "在限定时间内捕捉目标怪物即可领取以下奖励，限时任务可使用银子进行刷新，刷新后将重新计算剩余捕捉时间";
	public static String 仙灵限时任务 = "仙灵限时任务";
	public static String 限时任务未领奖 = "限时任务奖励还未领取，是否继续刷新？";
	public static String 银子刷新限时任务 = "当前没有免费次数，您确定花费@STRING_1@银子刷新吗？";
	public static String 购买真气二次提示 = "确定花费@STRING_1@购买10点真气？";
	public static String 激活buff二次提示1 = "确定花费@STRING_1@刷新BUFF？";
	public static String 退出副本二次提示 = "捕捉未结束，是否退出捕捉副本？";
	public static String 使用积分卡二次提示 = "<f>确定使用高级积分卡替换掉积分卡吗？</f>\n<f>（只可高级替换低级，低级不可替换高级积分卡）</f>";
	public static String 激活buff二次提示2 = "<f>确定花费@STRING_1@激活BUFF吗？</f>\n<f>注：有BUFF时，再次激活BUFF会顶替掉原有BUFF!</f>";
	public static String 购买真气描述 = "花费银子购买真气，购买真气超出上限时继续叠加，真气每15分钟恢复1点！";
	public static String 真气不足 = "真气不足";
	public static String 捕捉 = "我抓...";
	public static String 捕捉成功 = "捕捉成功";
	public static String 捕捉失败 = "捕捉失败";
	public static String 少于1点血 = "怪物当前血量少于1点血量，无法再使用【怒血狂攻】技能造成伤害";
	public static String 仙灵技能介绍 = "<f color='#ffff00' float='center' >                                        技能说明</f>\n \n<i iconId='zhuoyaoxiansuo' width='64' height='64' ></i><f color='#00ff00'  size='28' >【捉妖灵葫】</f><f color='#ffffff' size='28' >：有几率捕捉怪物，期间移动读条会被打断，捕捉成功获得积分。（每次消耗1个乾坤石）</f>\n \n<i iconId='nuxueqianggong' width='64' height='64' ></i><f  color='#00ff00'  size='28'  >【怒血强攻】</f><f color='#ffffff' size='28' >：此技能对怪物造成巨大的伤害，并使怪物剩余1点血量。（每次消耗1个烈血石）			</f>\n \n<i iconId='heiyemoying' width='64' height='64' ></i><f color='#00ff00'  size='28'>【黑夜魔影】</f><f color='#ffffff' size='28' >：此技能对怪物造成固定的伤害。（每次消耗1个巫魔石）</f>\n \n<i iconId='wandugongxin' width='64' height='64' ></i><f color='#00ff00'  size='28'>【万毒攻心】</f><f color='#ffffff' size='28' >：对怪物造成伤害，持续5秒。（不消耗道具）</f>";
	public static String 已领取积分奖励 = "本档积分奖励已领取，奖励请在邮件中提取！";
	public static String 使用成功 = "使用成功";
	public static String 已有该BUFF = "已有该BUFF";
	public static String 已有更高级BUFF = "已有同类型更高级BUFF";
	public static String 活动结束积分不计入排行 = "活动结束积分不计入排行";
	public static String 限时任务标题 = "仙灵大会任务奖励";
	public static String 限时任务内容 = "恭喜您完成仙灵大会限时任务，请您在邮件中领取奖励！";
	public static String 排行奖励标题 = "仙灵大会排行榜奖励";
	public static String 排行奖励内容 = "恭喜您获得本次仙灵大会的排行榜奖励，请领取邮件内的奖励，下次活动要再接再厉哟！";
	public static String 积分奖励标题 = "仙灵大会积分奖励";
	public static String 积分奖励内容 = "恭喜您领取了@STRING_1@积分奖励档位奖励，请您在邮件中领取相应奖励，希望您继续加油！";
	public static String 领奖成功 = "奖励已领取，请在邮件中提取！";
	public static String 没有保底符 = "您没有放入保底符，练星失败后可能会掉级， 您是否继续练星。";
	
	public static String 开启宝箱提示 = "开启宝箱还缺少1枚@STRING_1@";
	public static String 商城消费排行榜 = "商城消费排行榜";
	public static String 极地寻宝消费排行榜 = "极地寻宝消费排行榜";
	public static String 银两消费排行榜 = "银两消费排行榜";
	public static String 跨服队伍人数限制 = "队伍成员已经达到战队参加匹配上限";
	public static String 队伍已满 = "队伍已满,队伍成员已达战队参加匹配上限";
	public static String 跨服排行榜活动即将结束 = "@STRING_1@活动即将在@STRING_2@结束";
	public static String 已发送组队邀请 = "已发送组队邀请";

	public static String 级20后再打boss吧 = "20级后才可以挑战boss";
	public static String 家族不存在不能挑战 = "您没有家族，不可以挑战boss";
	public static String 副本已满 = "副本人数已达上限，请稍后再试";
	public static String 创建副本失败 = "进入副本失败，请联系GM";
	public static String 助战宠物升级材料 = "八卦石";
	public static String 助战宠物升级成功 = "恭喜您升级成功";
	public static String 助战宠物升级已达最大等级 = "升级已达上限";
	public static String 每周只能喝酒15次 = "每周只能喝酒15次";
	public static String 领取七日奖励成功 = "领取成功，奖励已通过邮件发送，请注意查收";
	public static String 已经领取七日奖励 = "已经领取过该奖励";
	public static String 七日登录奖励邮件 = "您已成功领取第@STRING_1@日登录奖励";
	public static String 查询登录公告失败 = "登录公告不存在@STRING_1@";
	public static String 十级以上的玩家才可以 = "10级以上的玩家才可以领取";
	public static String 七日宝箱配置错误 = "七日登录礼包配置错误,请联系GM";
	public static String 已经上阵 = "该宠物已经上阵";
	public static String 上阵成功 = "上阵成功";
	public static String 撤回成功 = "撤回成功";
	public static String 还没上阵 = "宠物还没有上阵";
	public static String 功能没开启 = "150级开放此功能，敬请期待！";
	public static String 助战栏满150 = "当前等级助阵宠物已满，请升级后在试试";
	public static String 助战栏满220 = "助阵宠物已满！";
	public static String 仙兽房已满 = "仙兽房已满";
	public static String 宠物已经在挂机中 = "宠物已经在挂机中";
	public static String 宠物挂机成功= "挂机成功";
	public static String 宠物召回成功= "宠物收回成功";
	public static String 家族中还没有宠物挂机= "家族中还没有宠物挂机";
	public static String 宠物挂机状态不正确= "宠物挂机状态不正确";
	public static String 祝福次数已达上限= "该宠物被祝福次数已达上限";
	public static String 您祝福次数已达上限= "您今日的祝福次数已达上限";
	public static String 祝福成功= "祝福成功";
	public static String 改宠物已经祝福过了= "您已经祝福过该宠物了";
	public static String 暂无可领取的奖励= "暂无可领取的奖励";
	public static String 不能祝福自己的宠物= "不能祝福自己的宠物";
	public static String 不能召唤正在助战的宠物= "不能召唤正在助战的宠物";
	public static String 助战中的宠物不能封印= "助战中的宠物不能操作";
	public static String 挂机中的宠物不能封印= "挂机中的宠物不能操作";
	public static String 宠物正在挂机= "宠物正在挂机,不能召唤";
	public static String 宠物正在挂机不能上阵= "宠物正在挂机,不能上阵";
	public static String 宠物正在挂机不能收回= "宠物正在挂机,不能收回";
	public static String 助战的宠物不能炼化= "助战的宠物不能炼化";
	public static String 助战的宠物不能合体= "助战的宠物不能合体";
	public static String 请组好队伍再来参加= "请组好队伍再来参加";
	public static String 你在组队状态= "您现在有队伍，不能参加";
	public static String 当前地图不允许进入副本= "当前地图不允许进入副本";
	public static String 队友当前地图不允许进入副本= "有小队成员的当前地图不允许进入";
	public static String 副本配置错误= "副本配置错误，请联系GM";
	public static String 副本等级不满足= "队伍中有玩家等级不满足进入条件,不能进入";
	public static String 副本创建失败= "副本创建失败，请联系GM";
	public static String 副本类型错误= "无效的副本类型，请联系GM";
	public static String 家族中有宠物正在挂机不能解散= "家族中有宠物正在挂机不能解散";
	public static String 有宠物正在挂机不能操作= "有宠物正在挂机不能操作";
	public static String 摘星帮助信息= "摘星帮助信息";
	public static String 摘星功能信息= "要求被转移装备为绑定装备；只保留原装备星级，被转移装备星级将被覆盖；法宝星级不可转移；根据转移不同星级装备收取银子数量不等";
	public static String 只能装备摘星= "只有装备才能摘星";
	public static String 装备摘星成功= "装备移星成功";
	public static String 该装备还没有炼星= "该装备还没有炼星";
	public static String 被转移的星小于转移的= "转移装备星级低于被转移装备";
	public static String 被转移的星已达最大= "被转移装备的星已达最大";
	public static String 要继承星的装备必须是绑定的= "要继承星的装备必须是绑定的";
	public static String 只能装备进行摘星操作= "只能装备进行摘星操作";
	public static String 特殊装备不能进行摘星操作= "特殊装备不能进行摘星操作";
	public static String 摘星提示标题= "摘星提示内容";
	public static String 摘星提示内容= "摘星提示内容";
	public static String 战斗状态不能进入= "您处于战斗状态，不能进入";
	public static String 队友战斗状态不能进入= "您有队友处于战斗状态，不能进入";
	public static String 确认开始摘星吗= "被转移装备星级会被覆盖，只保留转移装备星级，是否转移？";
	
	public static void main(String[] args) {
		// String test = "@STRING_1@@PLAYER_NAME_1@经过深思熟虑后，辞去了@STRING_2@一职，感谢@PLAYER_NAME_1@为@STRING_1@做出的贡献";
		// try {
		//
		// String rr = translateString(test, new String[][] { new String[] { Translate.STRING_2, null }, new String[] { Translate.PLAYER_NAME_1, "单挑" }, { Translate.STRING_1, "单涛"
		// } });
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
