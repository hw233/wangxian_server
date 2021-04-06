package com.fy.engineserver.datasource.article.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

public abstract class ArticleEntityManager {

	public	static Logger log = LoggerFactory.getLogger(ArticleEntityManager.class.getName());

	public static final int CREATE_REASON_SPRITE_FLOP = 0;
	
	public static final int CREATE_REASON_SHOP_SELL = 1;
	
	public static final int CREATE_REASON_TASK_REWARD = 2;
	
	public static final int CREATE_REASON_PLAYER_CREATE = 3;
	
	public static final int CREATE_REASON_TASK_INIT = 4;
	
	public static final int CREATE_REASON_INITPLAYER = 5;
	
	public static final int CREATE_REASON_CAIJI = 6;
	
	public static final int CREATE_REASON_OTHER = 7;
	
	public static final int CREATE_REASON_GIVEN_BY_NPC = 8;
	
	public static final int CREATE_REASON_DISPATCH_DAILY_PACKAGE_PROPS = 9;
	
	public static final int CREATE_REASON_DISPATCH_LEVEL_PACKAGE_PROPS = 10;
	
	public static final int CREATE_REASON_ACTION_PRESENT = 11;
	
	public static final int CREATE_REASON_FORMULAPROPS_INFO = 12;
	
	public static final int CREATE_REASON_NEI_CE_GIFT = 13;
	
	public static final int CREATE_REASON_MARRIAGE= 14;
	
	public static final int CREATE_REASON_USE_PACKAGEPROPS= 15;
	
	public static final int CREATE_REASON_GMSEND= 16;
	
	public static final int CREATE_REASON_FINISH_APPRENTICESHIP= 17;
	
	public static final int CREATE_REASON_COMPENSATE_GONG_CE_LI_BAO_40= 18;
	
	public static final int CREATE_REASON_COMPOSE_ARTICLE = 19;
	
	public static final int CREATE_REASON_SEED_NPC_PICK = 20;
	
	public static final int CREATE_REASON_JIN_LING_AWARD = 21;
	
	public static final int CREATE_REASON_ZHONGQIULIWU = 22;
	
	public static final int CREATE_REASON_CROSSSERVER = 23;
	
	public static final int CREATE_REASON_IMPORT = 24;
	
	public static final int CREATE_REASON_BIWU_SIGNUP = 25;
	
	public static final int CREATE_REASON_BIWU_PRESENT = 26;
	
	public static final int CREATE_REASON_RETURN_PRESENT = 27;
	
	public static final int CREATE_REASON_COMPOSEBAOSHI = 28;
	
	public static final int CREATE_REASON_COMPOSELINGFU = 29;
	
	public static final int CREATE_REASON_PET_BREED = 30;
	
	public static final int CREATE_REASON_PET_MATING = 31;
	
	public static final int CREATE_REASON_GENGUDAN = 32;
	
	public static final int CREATE_REASON_摘星 = 33;
	
	public static final int CREATE_REASON_装备升级 = 34;
	
	public static final int CREATE_REASON_后台 = 35;

	public static final int CREATE_REASON_PICK_CAVE_PLANT = 36;
	
	public static final int CREATE_REASON_PET_SEAL = 37;
	public static final int CREATE_REASON_REQUESTBUY= 38;
	public static final int CREATE_REASON_CHUANGONGSHI= 39;
	public static final int CREATE_REASON_TEMP_ARTICLE= 40;
	public static final int CREATE_REASON_FATEACTIVITY= 41;
	
	public static final int CREATE_REASON_NEWPLAYER= 42;
	public static final int CREATE_REASON_COLLECTION = 43;
	public static final int CREATE_REASON_BOTTLE = 44;
	public static final int CREATE_REASON_CLIFFORD = 45;
	public static final int CREATE_REASON_silvercar = 46;
	public static final int CREATE_REASON_结义 = 47;
	public static final int CREATE_REASON_DRIVEPET = 48;
	public static final int CREATE_REASON_EXCHANGE_CONFRIMATION_CODE = 49;
	public static final int CREATE_REASON_huodong_libao = 50;
	public static final int CREATE_REASON_explore = 51;
	public static final int CREATE_REASON_merge_Article = 52;
	public static final int CREATE_REASON_ACHIEVEMENT = 53;
	public static final int CREATE_REASON_TOURNAMENT_REWARD = 54;
	public static final int CREATE_REASON_VIP = 55;
	
	public static final int CREATE_REASON_QIANCENGTA = 56;
	public static final int CREATE_REASON_JIAZUBOSS = 57;
	public static final int CREATE_REASON_LEVELUP = 57;
	public static final int 老玩家回归 = 58;
	public static final int CREATE_REASON_CHANNEL = 59;
	public static final int CREATE_REASON_PEOPLESEARCH = 60;
	public static final int 采花大盗 = 61;
	public static final int qq在线活动 = 62;
	public static final int 运营发放活动奖励 = 63;
	public static final int 运营活动_福泽天降祥瑞仙班 = 64;
	public static final int CREATE_REASON_DOWNCITY = 65;
	
	public static final int CREATE_REASON_BU_REQUEST = 66;
	public static final int CREATE_REASON_BU_1111 = 67;
	public static final int CREATE_REASON_BUCHANG = 68;
	
	public static final int CREATE_REASON_CHONGZHI_ACTIVITY = 69;
	public static final int 天神下凡 = 70;
	public static final int CREATE_REASON_XIAOFEI_ACTIVITY = 71;
	public static final int CREATE_REASON_MONEYBILLBOARD_ACTIVITY = 72;
	public static final int 末日活动 = 73;
	public static final int LIANXING_ACTIVITY = 74;
	public static final int LABA_ACTIVITY = 75;
	public static final int TENGXUN_WEEK_LIBAO = 76;
	public static final int 活动 = 77;
	public static final int 绿色服 = 78;
	public static final int 资源更新补偿 = 79;
	public static final int 活动棉花糖 = 80;
	public static final int 累计在线活动 = 81;
	public static final int KILL_WORLD_BOSS = 82;
	/**	 * 需要给客户端展示boss击杀奖励。	 */
	public static final int SHOW_WORLD_BOSS_REWARD = 83;
	public static final int WORLD_BOSS_WEEK_RANK_REWARD = 84;
	public static final int 仙缘论道 = 85;
	public static final int 宠物迷城 = 86;
	public static final int PET2技能抽取 = 87;
	public static final int PET2_PET_TO_DanYao = 88;
	public static final int DUJIE_CHENGGONG = 89;
	public static final int 挖宝 = 90;
	public static final int 维护补偿 = 91;
	public static final int UCDEC15ACTIVITY = 92;
	public static final int 恶魔广场 = 93;
	public static final int 新合成 = 94;
	public static final int 恶魔广场杀怪积分获取 = 95;
	public static final int 图纸合成临时装备 = 96;
	public static final int 仙尊投票 = 97;
	public static final int 仙尊膜拜 = 98;
	public static final int 仙尊答谢 = 99;
	public static final int 目标系统临时 = 100;
	public static final int 活动临时物品 = 101;
	public static final int 新坐骑碎片转换 = 102;
	public static final int 封印捐献物品 = 103;
	public static final int 副本结束转盘 = 104;
	public static final int 果实偷取额外产生 = 105;
	public static final int 挖坟活动获得 = 106;
	public static final int 活动奖励临时物品 = 107;
	public static final int 完成目标奖励 = 108;
	public static final int 仙尊奖励 = 109;
	public static final int 封印捐献奖励 = 110;
	public static final int 采集产出封印物品 = 111;
	public static final int 翅膀副本产出 = 112;
	public static final int 赛季奖励 = 113;
	public static final int 送花奖励 = 114;
	public static final int 月卡活动立返奖励 = 115;
	public static final int 月卡活动日返奖励 = 116;
	public static final int 极地寻宝活动获得 = 117;
	public static final int 商店充值获取商品 = 118;
	public static final int 每日转盘活动 = 119;
	public static final int 每日充值礼包活动 = 120;
	public static final int 猎命抽奖 = 121;
	public static final int 仙界宝箱 = 122;
	public static final int 宝箱大乱斗 = 123;
	public static final int 金猴天灾活动 = 124;
	public static final int 灵髓炼化 = 125;
	public static final int VIP转盘 = 126;
	public static final int 坐骑魂石 = 127;
	public static final int 小羊活动 = 128;
	public static final int 骰子活动物品 = 129;
	public static final int 骰子活动临时物品 = 130;
	public static final int 仙灵临时物品 = 131;
	public static final int 跨服周奖励 = 132;
	public static final int 消费排行榜 = 133;
	public static final int vip登录活动 = 134;
	public static final int boss副本 = 135;
	public static final int 家族远征 = 136;
	public static final int 七日登录奖励 = 137;
	public static final int 首冲奖励 = 138;
	public static final int 累计在线礼包 = 139;
	public static final int 幻境单人副本 = 140;
	public static final int 月卡每日奖励 = 141;
	public static final int 每日礼包购买 = 142;
	public static final int 副本掉落 = 143;
	public static final int 开服头7日额外送 = 144;
	public static final int 升级礼包 = 145;
	
	/*	 !!!!!!!!!!!!!注意，增加上面的值时，还要在下面这个数组增加对应内容！!!!!!!!!!!!!!!!!!!!!!!!	 */
	
	public static String CREATE_REASON_DESPS[] = new String[]{
		Translate.text_3469,
		Translate.text_3470,
		Translate.text_3471,
		Translate.text_3472,
		Translate.text_3473,
		Translate.text_3474,
		Translate.text_3475,
		Translate.text_3476,
		Translate.text_3477,
		Translate.text_3478,
		Translate.text_3479,
		Translate.text_3480,
		Translate.text_3481,
		Translate.text_3482,
		Translate.text_3483,
		Translate.text_3484,
		Translate.text_3485,
		Translate.text_3486,
		Translate.text_3487,
		Translate.text_3488,
		Translate.text_3489,
		Translate.text_3490,
		Translate.text_3491,
		Translate.text_3492,
		Translate.text_3493,
		Translate.text_3494,
		Translate.text_3495,
		Translate.text_3496,
		Translate.composeBaoshi,
		Translate.composeLingfu,
		Translate.petbreed,
		Translate.petmating,
		Translate.gengudan,
		Translate.摘星,
		Translate.装备升级,
		Translate.后台,
		Translate.PICK_CAVE_PLANT,
		Translate.PET_SEAL,
		Translate.text_requestbuy_003,
		Translate.text_传功石,
		Translate.创建临时物品,
		Translate.CREATE_REASON_FATEACTIVITY,
		Translate.text_newPlayer_001,
		Translate.text_createArticle_reason_collection,
		Translate.宝瓶,
		"祈福",
		Translate.运镖,
		Translate.结义,
		Translate.text_cave_072,
		Translate.text_codeactivity_002,
		"礼包",
		Translate.寻宝,
		"移动合并物品",
		"成就奖励",
		"竞技奖励",
		"VIP领取",
		"千层塔",
		"家族BOSS",
		"升级奖励坐骑",
		"老玩家回归",
		"渠道",
		"寻人",
		"采花大盗",
		"qq在线活动",
		"运营发放活动奖励",
		"福泽天降祥瑞仙班",
		"副本奖励",
		"求购系统补货",
		"11.11光棍活动",
		"补偿",
		"充值活动",
		"天神下凡",
		"消费活动",
		"金钱排行活动",
		"末日活动",
		"炼星20活动",
		"腊八粥活动",
		"腾讯蓝钻每周礼包",
		"活动",
		"绿色服",
		"资源更新补偿",
		"61活动棉花糖",
		"累计在线活动",
		"世界BOSS奖励",
		"世界BOSS奖励",
		"世界BOSS奖励",
		Translate.仙缘论道,
		"宠物迷城",
		"PET2技能抽取",
		"PET2_PET_TO_DanYao",
		"DUJIE_CHENGGONG",
		"挖宝",
		"维护补偿",
		"UC12月15充值返利活动",
		"恶魔广场显示用",
		"新合成",
		"恶魔广场杀怪积分获取",
		"图纸合成临时装备",
		"仙尊投票",
		"仙尊膜拜",
		"仙尊答谢",
		"目标系统显示用",
		"活动临时物品",
		"新坐骑碎片转换",
		"封印捐献物品",
		"副本结束转盘",
		"果实偷取额外产生",
		"挖坟活动获得",
		"活动奖励临时物品",
		"完成目标奖励",
		"仙尊奖励",
		"封印捐献奖励",
		"采集产出封印物品",
		"翅膀副本产出",
		"赛季奖励",
		"送花奖励",
		"月卡活动立返奖励",
		"月卡活动日返奖励",
		"极地寻宝活动获得",
		"商店充值获取商品",
		"每日转盘活动",
		"每日充值礼包活动",
		"猎命抽奖",
		"仙界宝箱",
		"宝箱大乱斗",
		"金猴天灾活动",
		"灵髓炼化",
		"坐骑魂石",
		"小羊活动",
		"骰子活动物品",
		"骰子活动临时物品",
		"仙灵临时物品",
		"跨服周奖励",
		"消费排行榜",
		"vip登录活动",
		"boss副本",
		"家族远征",
		"七日登录奖励",
		"首冲奖励",
		"累计在线礼包",
		"幻境单人副本",
		"月卡每日奖励",
		"每日礼包购买",
		"副本掉落",
		"开服头7日额外送",
		"升级礼包"
	};
	
	public static final int DELETE_REASON_SPRITE_DISAPPEAR = 0;
	
	public static final int DELETE_REASON_SHOP_BUY = 1;
	
	public static final int DELETE_REASON_TASK_REWARD = 2;
	
	public static final int DELETE_REASON_PLAYER_DELETE = 3;
	
	public static final int DELETE_REASON_OTHER = 4;
	
	public static final int DELETE_COMPOSE = 5;
	
	public static final int DELETE_UPGRADE = 6;
	
	public static final int DELETE_DRILL = 7;
	
	public static final int DELETE_USE = 8;
	
	public static final int DELETE_FUHUO = 9;
	
	public static final int DELETE_QIANLI = 10;

	public static final int DELETE_DRILL_RESET = 11;

	public static final int DELETE_FAVOR_PROPS = 12;

	public static final int DELETE_REASON_TASK_NEED = 13;
	
	public static final int DELETE_REASON_RESET_INLAY = 14;
	
	public static final int DELETE_REASON_COMPOSE_ARTICLE = 15;
	
	public static final int DELETE_REASON_MAGIC_TALISMAN_ARTICLE = 16;
	/**
	 * "怪物消失",
		"商店买卖",
		"任务奖励消除",
		"玩家丢弃",
		"其他原因",
		"物品合成",
		"装备升级",
		"打孔",
		"使用消耗",
		"复活",
		"使用千里追风",
		"重置打孔次数",
		"使用友好度道具",
		"任务收取",
		"挖宝石",
		"合成物品",
		"合成灵符"
	 */
	public static final String DELETE_REASON_DESPS[] = new String[]{
		Translate.text_3497,
		Translate.text_3470,
		Translate.text_3498,
		Translate.text_3499,
		Translate.text_3476,
		Translate.text_3500,
		Translate.text_1016,
		Translate.text_626,
		Translate.text_3501,
		Translate.text_3502,
		Translate.text_3503,
		Translate.text_3504,
		Translate.text_3505,
		Translate.text_3506,
		Translate.text_3507,
		Translate.text_3488,
		Translate.text_3508
	};
	
	
	protected static ArticleEntityManager self;
	
	public SimpleEntityManager<ArticleEntity> em;
	public static SimpleEntityManager<BaoShiXiaZiData> baoShiXiLianEM;
	
	public static ArticleEntityManager getInstance(){
		return self;
	}
	
	/**
	 * 获得一个实体，此方法返回的可以是ArticleEntity，也可以是EquipmentEntity或者PropsEntity
	 * 
	 * @param id
	 * @return
	 */
	public abstract ArticleEntity getEntity(long id);
	
	/**
	 * 获得一个实体，此方法返回的可以是从cache中的ArticleEntity，也可以是EquipmentEntity或者PropsEntity
	 * 
	 * @param id
	 * @return
	 */
	public abstract ArticleEntity getEntityInCache(long id);
	
	public abstract List<ArticleEntity> getEntityByIds(long ids[]);
	
	/**
	 * 通过物种名称获得一个物品，次情形只针对可堆叠的物品，可采用此种获取方式
	 * @param articleName
	 * @param binded
	 * @return
	 */
	public abstract ArticleEntity getEntity(String articleName, boolean binded, int color);
	
	/**
	 * referenceCount引用计数，此数据在创建的时候设置，当玩家每删除一个的时候，引用计数减少一，只有引用计数减少到0的时候，才可以真正的删除
	 * 从数据库删除物品规则
	 * 无时间不可堆叠的物品当且仅当引用计数为0的时候才进行删除
	 * 有时间不可堆叠的物品当且仅当引用计数为0的时候才进行删除
	 * 无时间可堆叠的物品不执行删除操作
	 * 有时间可堆叠的物品当且仅当引用计数为0的时候且过期后才进行删除
	 * @param a
	 * @return
	 * @throws Exception 
	 */
	public abstract ArticleEntity createEntity(Article a,boolean binded,int reason, Player player, int color, int referenceCount, boolean saveNow) throws Exception;
	
	public abstract ArticleEntity createEntity(Article a,boolean binded,int reason, Player player, int color, int referenceCount, int level, boolean saveNow) throws Exception;

	/**
	 * 创建一个临时普通物品，如果此物品为可重叠的物品，建议实现的时候，不要创建新的实例，
	 * 只需要返回原来的实例就可以。
	 * 因为可重叠的物品，不需要创建实例，所有人都指向此物品的实例就行。
	 * 
	 * @param a
	 * @return
	 */
	public abstract ArticleEntity createTempEntity(Article a,boolean binded,int reason, Player player, int color) throws Exception;
	
	/**
	 * 批量保存
	 * @param list
	 */
	public abstract void batchSaveEntity(List<ArticleEntity> list);

	/**
	 * 得到entity数量
	 * @return
	 */
	public abstract long getCount();
	
	/**
	 * 从cache中超时。
	 * @param ae
	 */
	public abstract void entityTimeoutFromCache(ArticleEntity ae,int type);
	
	/**
	 * 游戏回收物品，不涉及上层逻辑，仅仅更新物品abandoned属性
	 * @param a
	 * @param reason
	 * @param player
	 * @return
	 */
	public abstract void recycleEntity(ArticleEntity a,int reason, Player player);

	public abstract ArticleEntity getTempEntity(String articleName, boolean binded,
			int color);

	public abstract boolean putToRealSaveCache(ArticleEntity ae);
	
	public abstract void removeTransientATable(ArticleEntity ae);

}
