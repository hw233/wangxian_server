package com.fy.engineserver.datasource.article.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.clifford.manager.CliffordManager;
import com.fy.engineserver.activity.globallimit.manager.GlobalLimitManager;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.activity.vipExpActivity.VipExpActivityManager;
import com.fy.engineserver.animation.AnimationManager;
import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.articleEnchant.model.EnchantArticle;
import com.fy.engineserver.articleProtect.ArticleProtectData;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.billboard.special.SpecialEquipmentMappedStone;
import com.fy.engineserver.bourn.BournManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.concrete.ReadEquipmentExcelManager;
import com.fy.engineserver.datasource.article.data.articles.AiguilleArticle;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.BiWuJingCaiArticle;
import com.fy.engineserver.datasource.article.data.articles.ComposeArticle;
import com.fy.engineserver.datasource.article.data.articles.ComposeInterface;
import com.fy.engineserver.datasource.article.data.articles.ComposeOnlyChangeColorArticle;
import com.fy.engineserver.datasource.article.data.articles.ExchangeArticle;
import com.fy.engineserver.datasource.article.data.articles.HorseFoodArticle;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.articles.MaterialArticle;
import com.fy.engineserver.datasource.article.data.articles.PetFoodArticle;
import com.fy.engineserver.datasource.article.data.articles.PickFlowerArticle;
import com.fy.engineserver.datasource.article.data.articles.QiLingArticle;
import com.fy.engineserver.datasource.article.data.articles.QiLingDanArticle;
import com.fy.engineserver.datasource.article.data.articles.UpgradeArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity;
import com.fy.engineserver.datasource.article.data.equipextra.EquipStarManager;
import com.fy.engineserver.datasource.article.data.equipextra.costEnum.AddLuckyCostEnum;
import com.fy.engineserver.datasource.article.data.equipextra.instance.EquipExtraData;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.equipments.NewSuitEquipment;
import com.fy.engineserver.datasource.article.data.equipments.SuitEquipment;
import com.fy.engineserver.datasource.article.data.equipments.UpgradeRule;
import com.fy.engineserver.datasource.article.data.equipments.Weapon;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayEntityManager;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayManager;
import com.fy.engineserver.datasource.article.data.horseInlay.instance.HorseEquInlay;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponEatProps;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeArticle;
import com.fy.engineserver.datasource.article.data.props.ArticleProperty;
import com.fy.engineserver.datasource.article.data.props.ArticlesDropSet;
import com.fy.engineserver.datasource.article.data.props.AvataProps;
import com.fy.engineserver.datasource.article.data.props.BookProps;
import com.fy.engineserver.datasource.article.data.props.BottleProps;
import com.fy.engineserver.datasource.article.data.props.BrightInlayProps;
import com.fy.engineserver.datasource.article.data.props.CallSpriteProp;
import com.fy.engineserver.datasource.article.data.props.CareerPackageProps;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.ClearRedProps;
import com.fy.engineserver.datasource.article.data.props.ClearSkillPointsProps;
import com.fy.engineserver.datasource.article.data.props.CoupleTransferPaper;
import com.fy.engineserver.datasource.article.data.props.DigProps;
import com.fy.engineserver.datasource.article.data.props.DiscountCard;
import com.fy.engineserver.datasource.article.data.props.EquipmentTuZhiProp;
import com.fy.engineserver.datasource.article.data.props.ExploreProps;
import com.fy.engineserver.datasource.article.data.props.ExploreResourceMap;
import com.fy.engineserver.datasource.article.data.props.FateActivityProps;
import com.fy.engineserver.datasource.article.data.props.FavorProps;
import com.fy.engineserver.datasource.article.data.props.FireActivityProps;
import com.fy.engineserver.datasource.article.data.props.FireFlowerProps;
import com.fy.engineserver.datasource.article.data.props.FlopScheme;
import com.fy.engineserver.datasource.article.data.props.FormulaProps;
import com.fy.engineserver.datasource.article.data.props.HorseProps;
import com.fy.engineserver.datasource.article.data.props.HorseUpProps;
import com.fy.engineserver.datasource.article.data.props.HunshiProps;
import com.fy.engineserver.datasource.article.data.props.IntProperty;
import com.fy.engineserver.datasource.article.data.props.JingjieProps;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.KnapsackExpandProps;
import com.fy.engineserver.datasource.article.data.props.LastingForPetProps;
import com.fy.engineserver.datasource.article.data.props.LastingProps;
import com.fy.engineserver.datasource.article.data.props.Lasting_For_Compose_Props;
import com.fy.engineserver.datasource.article.data.props.LeaveExpProps;
import com.fy.engineserver.datasource.article.data.props.LevelRandomPackage;
import com.fy.engineserver.datasource.article.data.props.MagicWeaponProps;
import com.fy.engineserver.datasource.article.data.props.MinusSkillCdTimesProps;
import com.fy.engineserver.datasource.article.data.props.MoneyExpHonorProps;
import com.fy.engineserver.datasource.article.data.props.MoneyProps;
import com.fy.engineserver.datasource.article.data.props.MountProps;
import com.fy.engineserver.datasource.article.data.props.NPCProps;
import com.fy.engineserver.datasource.article.data.props.OpenInputProp;
import com.fy.engineserver.datasource.article.data.props.PackageProps;
import com.fy.engineserver.datasource.article.data.props.PetEggProps;
import com.fy.engineserver.datasource.article.data.props.PetProps;
import com.fy.engineserver.datasource.article.data.props.PetSkillProp;
import com.fy.engineserver.datasource.article.data.props.PetWuXingProp;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.data.props.PropsCategory;
import com.fy.engineserver.datasource.article.data.props.RandomPackageProps;
import com.fy.engineserver.datasource.article.data.props.ReliveProps;
import com.fy.engineserver.datasource.article.data.props.RobberyProps;
import com.fy.engineserver.datasource.article.data.props.SeedNPCProps;
import com.fy.engineserver.datasource.article.data.props.SingleForPetProps;
import com.fy.engineserver.datasource.article.data.props.SingleProps;
import com.fy.engineserver.datasource.article.data.props.TaskProps;
import com.fy.engineserver.datasource.article.data.props.TiliProps;
import com.fy.engineserver.datasource.article.data.props.TitleItemProp;
import com.fy.engineserver.datasource.article.data.props.TransferPaper;
import com.fy.engineserver.datasource.article.data.props.UpgradeProps;
import com.fy.engineserver.datasource.article.data.props.VariationPetProps;
import com.fy.engineserver.datasource.article.data.props.WangZheZhiYinProps;
import com.fy.engineserver.datasource.article.data.props.WingProps;
import com.fy.engineserver.datasource.article.data.props.ZhaoJiLingProps;
import com.fy.engineserver.datasource.article.data.soulpith.GourdArticle;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticle;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_ArticleComposeConfirm;
import com.fy.engineserver.menu.Option_BaodiStronger;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_Confirm_Xilian_Equipment;
import com.fy.engineserver.menu.Option_EquipmentDevelopConfirm;
import com.fy.engineserver.menu.Option_EquipmentInsertStarConfirm;
import com.fy.engineserver.menu.Option_EquipmentPickStarConfirm;
import com.fy.engineserver.menu.Option_EquipmentRemoveStar;
import com.fy.engineserver.menu.Option_EquipmentUpConfirm;
import com.fy.engineserver.menu.Option_Equipment_Merge_Sure;
import com.fy.engineserver.menu.Option_InlayConfirm;
import com.fy.engineserver.menu.Option_JianDingConfirm;
import com.fy.engineserver.menu.Option_StrongConfirm;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.articleProtect.Option_NewJianDing_OK;
import com.fy.engineserver.menu.disaster.Option_cancel;
import com.fy.engineserver.menu.qiling.Option_LianQiConfirm;
import com.fy.engineserver.menu.qiling.Option_QiLingInlayConfirm;
import com.fy.engineserver.menu.qiling.Option_TunshiConfirm;
import com.fy.engineserver.menu.quizActivity.Option_UnConfirm_Close;
import com.fy.engineserver.message.ARTICLE_COMPOSE_REQ;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.EQUIPMENT_BIND_REQ;
import com.fy.engineserver.message.EQUIPMENT_DEVELOPUP_REQ;
import com.fy.engineserver.message.EQUIPMENT_DEVELOP_REQ;
import com.fy.engineserver.message.EQUIPMENT_DEVELOP_RES;
import com.fy.engineserver.message.EQUIPMENT_INLAY_REQ;
import com.fy.engineserver.message.EQUIPMENT_INLAY_UUB_RES;
import com.fy.engineserver.message.EQUIPMENT_INSCRIPTION_REQ;
import com.fy.engineserver.message.EQUIPMENT_INSERTSTAR_REQ;
import com.fy.engineserver.message.EQUIPMENT_JIANDING_REQ;
import com.fy.engineserver.message.EQUIPMENT_OUTLAY_REQ;
import com.fy.engineserver.message.EQUIPMENT_PICKSTAR_REQ;
import com.fy.engineserver.message.EQUIPMENT_STRONG_REQ;
import com.fy.engineserver.message.EQUIPMENT_STRONG_RES;
import com.fy.engineserver.message.GOD_EQUIPMENT_UPGRADE_REQ;
import com.fy.engineserver.message.GOD_EQUIPMENT_UPGRADE_RES;
import com.fy.engineserver.message.GOD_EQUIPMENT_UPGRADE_SURE_REQ;
import com.fy.engineserver.message.GOD_EQUIPMENT_UPGRADE_SURE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.LIANQI_RES;
import com.fy.engineserver.message.NEW_EQUIPMENT_STRONG_REQ;
import com.fy.engineserver.message.NEW_EQUIPMENT_STRONG_RES;
import com.fy.engineserver.message.NEW_JIANDING_OK_REQ;
import com.fy.engineserver.message.NEW_JIANDING_OK_RES;
import com.fy.engineserver.message.NOTIFY_EQUIPMENT_CHANGE_REQ;
import com.fy.engineserver.message.PLAY_ANIMATION_REQ;
import com.fy.engineserver.message.QUERY_ARTICLE_COMPOSE_REQ;
import com.fy.engineserver.message.QUERY_ARTICLE_COMPOSE_RES;
import com.fy.engineserver.message.QUERY_ARTICLE_INFO_RES;
import com.fy.engineserver.message.QUERY_ARTICLE_RES;
import com.fy.engineserver.message.QUERY_EQUIPMENT_BIND_REQ;
import com.fy.engineserver.message.QUERY_EQUIPMENT_BIND_RES;
import com.fy.engineserver.message.QUERY_EQUIPMENT_INSCRIPTION_REQ;
import com.fy.engineserver.message.QUERY_EQUIPMENT_INSCRIPTION_RES;
import com.fy.engineserver.message.QUERY_EQUIPMENT_INSERTSTAR_REQ;
import com.fy.engineserver.message.QUERY_EQUIPMENT_INSERTSTAR_RES;
import com.fy.engineserver.message.QUERY_EQUIPMENT_JIANDING_REQ;
import com.fy.engineserver.message.QUERY_EQUIPMENT_JIANDING_RES;
import com.fy.engineserver.message.QUERY_EQUIPMENT_PICKSTAR_REQ;
import com.fy.engineserver.message.QUERY_EQUIPMENT_PICKSTAR_RES;
import com.fy.engineserver.message.QUERY_EQUIPMENT_QILING_RES;
import com.fy.engineserver.message.QUERY_EQUIPMENT_STRONG_REQ;
import com.fy.engineserver.message.QUERY_EQUIPMENT_STRONG_RES;
import com.fy.engineserver.message.QUERY_LIANQI_REQ;
import com.fy.engineserver.message.QUERY_LIANQI_RES;
import com.fy.engineserver.message.QUERY_NEW_EQUIPMENT_STRONG2_REQ;
import com.fy.engineserver.message.QUERY_NEW_EQUIPMENT_STRONG2_RES;
import com.fy.engineserver.message.QUERY_NEW_EQUIPMENT_STRONG3_REQ;
import com.fy.engineserver.message.QUERY_NEW_EQUIPMENT_STRONG3_RES;
import com.fy.engineserver.message.QUERY_NEW_EQUIPMENT_STRONG_REQ;
import com.fy.engineserver.message.QUERY_NEW_EQUIPMENT_STRONG_RES;
import com.fy.engineserver.message.QUERY_TUNSHI_REQ;
import com.fy.engineserver.message.QUERY_TUNSHI_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.QUERY_XILIAN_REQ;
import com.fy.engineserver.message.QUERY_XILIAN_RES;
import com.fy.engineserver.message.STAR_HELP_REQ;
import com.fy.engineserver.message.STAR_HELP_RES;
import com.fy.engineserver.message.STAR_MONEY_REQ;
import com.fy.engineserver.message.STAR_MONEY_RES;
import com.fy.engineserver.message.XILIAN_CONFIRM_REQ;
import com.fy.engineserver.message.XILIAN_CONFIRM_RES;
import com.fy.engineserver.message.XILIAN_EQUIPMENT_SURE_RES;
import com.fy.engineserver.message.XILIAN_PAGE_REQ;
import com.fy.engineserver.message.XILIAN_PAGE_RES;
import com.fy.engineserver.message.XILIAN_PUT_REQ;
import com.fy.engineserver.message.XILIAN_PUT_RES;
import com.fy.engineserver.message.XILIAN_REMOVE_REQ;
import com.fy.engineserver.message.XILIAN_REMOVE_RES;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.soulpith.SoulPithConstant;
import com.fy.engineserver.soulpith.SoulPithManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PropsUseRecord;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticle;
import com.fy.engineserver.sprite.pet2.Pet2SkillCalc;
import com.fy.engineserver.sprite.pet2.PetGrade;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ProbabilityUtils;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerForHadoop;
import com.xuanzhi.tools.transport.RequestMessage;

/**
 * 物品管理器，保存服务器中，
 * 所有的物品种类
 * 
 */
public class ArticleManager {

	// public static Logger logger = Logger.getLogger(ArticleManager.class);
	public static Logger logger = LoggerFactory.getLogger(ArticleManager.class);

	public static boolean isGive20Xin = true;
	public static long Give20Xin_ent_time = 0L;

	private static ArticleManager self;

	static Random rd = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	/**
	 * 排序
	 */
	public static final int SORT_NO = 1;

	public static final String 防爆包 = "防爆包";
	public static final String 普通背包 = "普通背包";
	public static final String 器灵背包 = "器灵背包";
	public static final String 仓库2号 = "2号仓库";
	public static final String 宠物背包 = "宠物背包";
	public static final String 仓库 = "仓库";
	public static final String From = "From";
	public static final String To = "To";

	public static boolean 使用物品日志规范 = true;

	/**
	 * 得到物品管理器本身
	 * @return
	 */
	public static ArticleManager getInstance() {
		return self;
	}

	public static String 羽化石 = Translate.羽化石;
	public static String 级保底符 = Translate.级保底符;
	public static String 精华羽化石 = Translate.精华羽化石;

	public static String XILIAN_MATERIAL = Translate.神匣珠;
	public static String RELIVE_ARTICLE_NAME = Translate.复活卡;
	/**
	 * 最大hp在基本属性数组中位置索引
	 */
	public static final int maxHP_Basic_Index = 0;

	/**
	 * 外功值在基本属性数组中位置索引
	 */
	public static final int phyAttack_Basic_Index = 1;

	/**
	 * 内法值在基本属性数组中位置索引
	 */
	public static final int magicAttack_Basic_Index = 2;

	/**
	 * 外防值在基本属性数组中位置索引
	 */
	public static final int phyDefence_Basic_Index = 3;

	/**
	 * 内防值在基本属性数组中位置索引
	 */
	public static final int magicDefence_Basic_Index = 4;

	/**
	 * 法力值在附加属性数组中位置索引
	 */
	public static final int maxMP_Addition_Index = 0;

	/**
	 * 破甲值在附加属性数组中位置索引
	 */
	public static final int breakDefence_Addition_Index = 1;

	/**
	 * 命中值在附加属性数组中位置索引
	 */
	public static final int hit_Addition_Index = 2;

	/**
	 * 闪躲值在附加属性数组中位置索引
	 */
	public static final int dodge_Addition_Index = 3;

	/**
	 * 精准值在附加属性数组中位置索引
	 */
	public static final int accurate_Addition_Index = 4;

	/**
	 * 会心一击在附加属性数组中位置索引
	 */
	public static final int criticalHit_Addition_Index = 5;

	/**
	 * 会心防御在附加属性数组中位置索引
	 */
	public static final int criticalDefence_Addition_Index = 6;

	/**
	 * 火攻值在附加属性数组中位置索引
	 */
	public static final int fireAttack_Addition_Index = 7;

	/**
	 * 冰攻值在附加属性数组中位置索引
	 */
	public static final int blizzardAttack_Addition_Index = 8;

	/**
	 * 风攻值在附加属性数组中位置索引
	 */
	public static final int windAttack_Addition_Index = 9;

	/**
	 * 雷攻值在附加属性数组中位置索引
	 */
	public static final int thunderAttack_Addition_Index = 10;

	/**
	 * 火抗值在附加属性数组中位置索引
	 */
	public static final int fireDefence_Addition_Index = 11;

	/**
	 * 冰抗值在附加属性数组中位置索引
	 */
	public static final int blizzardDefence_Addition_Index = 12;

	/**
	 * 风抗值在附加属性数组中位置索引
	 */
	public static final int windDefence_Addition_Index = 13;

	/**
	 * 雷抗值在附加属性数组中位置索引
	 */
	public static final int thunderDefence_Addition_Index = 14;

	/**
	 * 火减抗值在套装属性数组中位置索引
	 */
	public static final int fireIgnoreDefence_Suit_Index = 0;

	/**
	 * 冰减抗值在套装属性数组中位置索引
	 */
	public static final int blizzardIgnoreDefence_Suit_Index = 1;

	/**
	 * 风减抗值在套装属性数组中位置索引
	 */
	public static final int windIgnoreDefence_Suit_Index = 2;

	/**
	 * 雷减抗值在套装属性数组中位置索引
	 */
	public static final int thunderIgnoreDefence_Suit_Index = 3;

	/**
	 * 基本属性在装备所有属性中的开始位置
	 */
	public static final int basicPropertyInAllPropertyStartIndex = 0;

	/**
	 * 基本属性在装备所有属性中的结束位置
	 */
	public static final int basicPropertyInAllPropertyEndIndex = 4;

	/**
	 * 附加属性在装备所有属性中的开始位置
	 */
	public static final int addtionPropertyInAllPropertyStartIndex = 5;

	/**
	 * 附加属性在装备所有属性中的结束位置
	 */
	public static final int addtionPropertyInAllBluePropertyEndIndex = 11;

	/**
	 * 附加属性在装备所有属性中的结束位置
	 */
	public static final int addtionPropertyInAllPropertyEndIndex = 19;

	/**
	 * 套装属性在装备所有属性中的开始位置
	 */
	public static final int suitPropertyInAllPropertyStartIndex = 20;

	/**
	 * 套装属性在装备所有属性中的结束位置
	 */
	public static final int suitPropertyInAllPropertyEndIndex = 23;

	/**
	 * 基本属性数组长度
	 */
	public static final int basicPropertyArrayLength = 5;

	/**
	 * 附加属性数组长度
	 */
	public static final int additionPropertyArrayLength = 15;

	/**
	 * 套装属性数组长度
	 */
	public static final int suitPropertyArrayLength = 4;

	/**
	 * 镶嵌宝石颜色数组长度
	 */
	public static final int inlayColorArrayLength = 8;

	/**
	 * 星级显示最高10星，最小0星(数值从零开始)
	 */
	public static int starMaxValue = 20;

	public static final int 装备炼星需要羽化的等级 = 20;
	
	public static final int 神炼符需要强化等级 = 26;

	public static final int[] 每个等级消耗羽化石的数量 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10 };

	public static final String[] starNames = new String[] { Translate.一星, Translate.二星, Translate.三星, Translate.四星, Translate.五星, Translate.六星, Translate.七星, Translate.八星, Translate.九星, Translate.十星, Translate.十一星, Translate.十二星, Translate.十三星, Translate.十四星, Translate.十五星, Translate.十六星, Translate.十七星, Translate.十八星, Translate.十九星, Translate.二十星, Translate.二十一星, Translate.二十二星, Translate.二十三星, Translate.二十四星, Translate.二十五星, Translate.二十六星, Translate.二十七星, Translate.二十八星, Translate.二十九星, Translate.三十星 };

	public static final int strongMaterialMaxNumber = 4;

	/**
	 * 装备精炼时的升级经验
	 */
	public static final int developEXPUpValue = 100;

	/**
	 * 装备允许的最大资质(开始为0)
	 */
	public static final int maxEndowments = 5;
	/**
	 * 装备新资质，从1000开始
	 */
	public static final int newEndowments = 1000;

	/**
	 * 合成数量范围
	 */
	public static final int[] composeRange = new int[] { 3, 5 };

	/**
	 * 合成物品所需数量
	 */
	public static final int COMPOSE_NO = 5;

	/**
	 * 合成最小几率
	 */
	public static final double COMPOSE_MIN_PROBOBILITY = 20;

	/**
	 * 合成中等几率
	 */
	public static final double COMPOSE_MIDDLE_PROBOBILITY = 50;

	/**
	 * 合成最大几率
	 */
	public static final double COMPOSE_MAX_PROBOBILITY = 100;

	/**
	 * 合成的基本值
	 */
	public static final double COMPOSE_BASIC = 100;

	/**
	 * 合成提示信息
	 */
	public static final String[] COMPOSE_INFOS = new String[] { Translate.物品个数不足, Translate.物品个数不足, COMPOSE_MIN_PROBOBILITY + Translate.几率合成成功, COMPOSE_MIDDLE_PROBOBILITY + Translate.几率合成成功, COMPOSE_MAX_PROBOBILITY + Translate.几率合成成功 };

	public static final int 人物套装开启属性最少件数 = 10;

	public static final int 马匹套装开启属性最少件数 = 5;
	/**
	 * 颜色最大值，颜色指的是装备颜色
	 * 白,绿,蓝,紫,完美紫,橙装,完美橙
	 */
	public static final int equipmentColorMaxValue = 6;

	/**
	 * 颜色最大值，颜色指的是物品颜色
	 * 白绿蓝紫橙
	 */
	public static final int notEquipmentColorMaxValue = 4;

	/**
	 * 装备颜色 白装,淡绿装,深绿装,淡蓝装,深蓝装,淡紫装,深紫装,橙装
	 */
	public static final int equipment_color_白 = 0;
	public static final int equipment_color_绿 = 1;
	public static final int equipment_color_蓝 = 2;
	public static final int equipment_color_紫 = 3;
	public static final int equipment_color_完美紫 = 4;
	public static final int equipment_color_橙 = 5;
	public static final int equipment_color_完美橙 = 6;

	public static final String[] color_equipment_Strings = new String[] { Translate.白, Translate.绿, Translate.蓝, Translate.紫, Translate.完美紫, Translate.橙, Translate.完美橙 };

	public static final String[] color_article_Strings = new String[] { Translate.白, Translate.绿, Translate.蓝, Translate.紫, Translate.橙 };

	public static final String[] color_magicweapon_Strings = new String[] { Translate.白, Translate.绿, Translate.蓝, Translate.紫, Translate.橙, Translate.金 };

	public static final int COLOR_WHITE = 0xffffffff;// 白
	public static final int COLOR_GREEN = 0xff00ff00;// 绿
	public static final int COLOR_BLUE = 0xff009cff;// 蓝
	public static final int COLOR_PURPLE = 0xffE706EA;// 紫
	public static final int COLOR_PURPLE_2 = 0xffC905CC;// 完美紫
	public static final int COLOR_ORANGE = 0xffFDA700;// 橙
	public static final int COLOR_GOLD = 0xCDCD00;// 金
	public static final int COLOR_ORANGE_2 = 0xffFD6B00;// 完美橙
	public static final int COLOR_PINK = 0xffff75e6;// 粉
	public static final int[] color_equipment = new int[] { COLOR_WHITE, COLOR_GREEN, COLOR_BLUE, COLOR_PURPLE, COLOR_PURPLE_2, COLOR_ORANGE, COLOR_ORANGE_2 };
	public static final int[] color_article = new int[] { COLOR_WHITE, COLOR_GREEN, COLOR_BLUE, COLOR_PURPLE, COLOR_ORANGE };
	public static final int[] color_article_2 = new int[] { COLOR_WHITE, COLOR_GREEN, COLOR_BLUE, COLOR_PURPLE, COLOR_ORANGE , COLOR_GOLD};
	public static final int[] cave_color_article = new int[] { COLOR_WHITE, COLOR_GREEN, COLOR_BLUE, COLOR_PURPLE, COLOR_ORANGE};
	public static final int[] color_magicweapon = new int[] { COLOR_WHITE, COLOR_GREEN, COLOR_BLUE, COLOR_PURPLE, COLOR_ORANGE, COLOR_GOLD };
	public static final int[] color_enchat = new int[] { 0xffffffff, 0xff1dcd00, 0xff009cff, 0xffe6028d, 0xffff8400, 0xfffcff00 };

	public static final int[] pet_strong_color = new int[] { COLOR_WHITE, COLOR_GREEN, COLOR_BLUE, COLOR_PURPLE, COLOR_ORANGE };
	public static final int[] quiz_prize_color = new int[] { COLOR_ORANGE, COLOR_PURPLE, COLOR_BLUE };

	public static String[] 角色avatar光芒光效 = new String[] { "角色avatar光芒光效/淡白色光晕闪烁", "角色avatar光芒光效/浅绿色光晕闪烁", "角色avatar光芒光效/蓝色光芒闪烁", "角色avatar光芒光效/强紫色光芒闪烁", "角色avatar光芒光效/强烈橙色极光闪烁", "角色avatar光芒光效/强烈七彩极光闪烁", "角色avatar光芒光效/金色闪光", "角色avatar光芒光效/光晕闪烁" };

	public static Map<Integer, String> replaceicon = new HashMap<Integer, String>();
	static {
		replaceicon.put(new Integer(21), "shibang4");
		replaceicon.put(new Integer(22), "shibang3");
		replaceicon.put(new Integer(23), "shibang3");
		replaceicon.put(new Integer(24), "shibang3");
		replaceicon.put(new Integer(25), "shibang3");
		replaceicon.put(new Integer(26), "shibang1");
		replaceicon.put(new Integer(27), "shibang1");
		replaceicon.put(new Integer(28), "shibang1");
		replaceicon.put(new Integer(29), "shibang1");
		replaceicon.put(new Integer(30), "shibang1");
		replaceicon.put(new Integer(31), "shibang2");
	}

	protected String baoShiColorFile;

	public static List<UpgradeRule> rules = new ArrayList<UpgradeRule>();
	public static List<NewSuitEquipment> suits = new ArrayList<NewSuitEquipment>();
	public static List<BaoShiXiaZiData> baoShiXiaZiDatas = new ArrayList<BaoShiXiaZiData>();
	public static Map<String, double[]> baoShiClolors = new Hashtable<String, double[]>();

	public enum BindType {
		NOT_BIND,
		BIND,
		BOTH, ;
	}

	/** 坐骑升阶道具<坐骑星级，道具名> */
	public static Map<Integer, String> upRankLevelNeedArticle = new HashMap<Integer, String>();

	/**
	 * 根据装备颜色类型返回相应颜色名
	 * @param colorType
	 * @return
	 */
	public static String getColorString(Article article, int colorType) {
		if (article instanceof Equipment) {
			if (colorType >= 0 && colorType < color_equipment_Strings.length) {
				return color_equipment_Strings[colorType];
			}
		} else if (article instanceof MagicWeapon) {
			if (colorType >= 0 && colorType < color_magicweapon_Strings.length) {

				return color_magicweapon_Strings[colorType];
			}
		} else {
			if (colorType >= 0 && colorType < color_article_Strings.length) {
				return color_article_Strings[colorType];
			}
		}

		return null;
	}

	/**
	 * 根据装备颜色类型返回相应颜色色值
	 * @param colorType
	 * @return
	 */
	public static int getColorValue(Article article, int colorType) {
		if (article instanceof Equipment) {
			if (colorType >= 0 && colorType < color_equipment_Strings.length) {

				return color_equipment[colorType];
			}
		} else if (article instanceof MagicWeapon) {
			if (colorType >= 0 && colorType < color_magicweapon_Strings.length) {

				return color_magicweapon[colorType];
			}
		} else if (article instanceof EnchantArticle) {
			if (colorType >= 0 && colorType < color_enchat.length) {

				return color_enchat[colorType];
			}

		} else {
			if (colorType >= 0 && colorType < color_article_Strings.length) {
				return color_article[colorType];
			}
		}

		return COLOR_WHITE;
	}

	/**
	 * 根据镶嵌宝石的颜色种类得到颜色名
	 * @param inlay_color_type
	 * @return
	 */
	public static String getInlayColorString(int inlay_color_type) {
		if (inlay_color_type >= 0 && inlay_color_type < InlayArticle.inlayColorStrings.length) {
			return InlayArticle.inlayColorStrings[inlay_color_type];
		}
		return null;
	}

	public static final String[] endowments_str = new String[] { Translate.无鉴定, Translate.普通, Translate.一般, Translate.优质, Translate.卓越, Translate.完美 };

	/**
	 * 根据资质类型返回资质字符串
	 * @param endowments_type
	 * @return
	 */
	public static String getEndowmentsString(int endowments_type) {
		if (endowments_type >= 0 && endowments_type < endowments_str.length) {
			return endowments_str[endowments_type];
		}
		return null;
	}

	public static final String[] 装备资质品质颜色 = new String[] { "0xffffff", "0xffffff", "0x00ff00", "0x0000ff", "0xE706EA", "0xFDA700" };

	public static String getEndowmentsColor(int endowments_type) {
		if (endowments_type >= 0 && endowments_type < 装备资质品质颜色.length) {
			return 装备资质品质颜色[endowments_type];
		}
		return "0xffffff";
	}

	/**
	 * 根据宝石名字构造相应宝石，宝石的相关属性与名字里包含的字符串有关
	 */
	HashMap<String, int[]> 宝石名称对应的宝石属性位置 = new HashMap<String, int[]>();

	private void 初始化宝石名称对应的宝石属性位置() {
		宝石名称对应的宝石属性位置 = new HashMap<String, int[]>();
		宝石名称对应的宝石属性位置.put("lvbaoshi", new int[] { 0 });
		宝石名称对应的宝石属性位置.put("chengbaoshi", new int[] { 1 });
		宝石名称对应的宝石属性位置.put("chengjingshi", new int[] { 2 });
		宝石名称对应的宝石属性位置.put("heijinshi", new int[] { 3, 4, 5, 6 });
		宝石名称对应的宝石属性位置.put("lanbaoshi", new int[] { 7, 8, 9, 10 });
		宝石名称对应的宝石属性位置.put("hongbaoshi", new int[] { 11, 12, 13 });
		宝石名称对应的宝石属性位置.put("baijingshi", new int[] { 14, 15, 16 });
		宝石名称对应的宝石属性位置.put("huangbaoshi", new int[] { 17, 18, 19 });
		宝石名称对应的宝石属性位置.put("zijingshi", new int[] { 20, 21, 22 });

		宝石名称对应的宝石属性位置.put("hongbaoshi_fenyan", new int[] { 23, 24, 25 });
		宝石名称对应的宝石属性位置.put("hongbaoshi_fentian", new int[] { 26 });
		宝石名称对应的宝石属性位置.put("hongbaoshi_fenhuang", new int[] { 27 });
		宝石名称对应的宝石属性位置.put("hongbaoshi_fenrong", new int[] { 28 });
		宝石名称对应的宝石属性位置.put("baijingshi_hanbing", new int[] { 29, 30, 31 });
		宝石名称对应的宝石属性位置.put("baijingshi_hanshuang", new int[] { 32 });
		宝石名称对应的宝石属性位置.put("baijingshi_hanyu", new int[] { 33 });
		宝石名称对应的宝石属性位置.put("baijingshi_hanzhan", new int[] { 34 });
		宝石名称对应的宝石属性位置.put("zijingshi_leiting", new int[] { 35, 36, 37 });
		宝石名称对应的宝石属性位置.put("zijingshi_leiji", new int[] { 38 });
		宝石名称对应的宝石属性位置.put("zijingshi_leiming", new int[] { 39 });
		宝石名称对应的宝石属性位置.put("zijingshi_leizhu", new int[] { 40 });
		宝石名称对应的宝石属性位置.put("huangbaoshi_jifeng", new int[] { 41, 42, 43 });
		宝石名称对应的宝石属性位置.put("huangbaoshi_shuofeng", new int[] { 44 });
		宝石名称对应的宝石属性位置.put("huangbaoshi_kuangfeng", new int[] { 45 });
		宝石名称对应的宝石属性位置.put("huangbaoshi_baofeng", new int[] { 46 });
	}

	private int[] 得到宝石名称对应的宝石属性位置(String name) {
		for (String key : 宝石名称对应的宝石属性位置.keySet()) {
			// if (name.indexOf(key) >= 0) {
			// return 宝石名称对应的宝石属性位置.get(key);
			// }
			if (name.matches(key + "\\d+")) {
				return 宝石名称对应的宝石属性位置.get(key);
			}
		}

		return new int[0];
	}

	/**
	 * 二维数组
	 * 第一维为鉴定符数组下标
	 * 第二维为装备级别范围(MIN,MAX)
	 */
	public static final int[][] 装备鉴定级别对应的鉴定符数组下标 = new int[][] { { 0, 10 }, { 11, 50 }, { 51, 100 }, { 101, 150 }, { 151, 200 }, { 201, 300 } };

	public static final String[] 装备鉴定符数组 = new String[] { Translate.一级鉴定符, Translate.一级鉴定符, Translate.一级鉴定符, Translate.一级鉴定符, Translate.一级鉴定符, Translate.一级鉴定符 };

	/**
	 * 二维数组
	 * 第一维为铭刻符数组下标
	 * 第二维为装备级别范围(MIN,MAX)
	 */
	public static final int[][] 装备级别对应的铭刻符数组下标 = new int[][] { { 0, 10 }, { 11, 50 }, { 51, 100 }, { 101, 150 }, { 151, 200 }, { 201, 300 } };

	public static final String[] 装备铭刻符数组 = new String[] { Translate.一级铭刻符, Translate.一级铭刻符, Translate.一级铭刻符, Translate.一级铭刻符, Translate.一级铭刻符, Translate.一级铭刻符 };

	/**
	 * 二维数组
	 * 第一维为绑定符数组下标
	 * 第二维为装备级别范围(MIN,MAX)
	 */
	public static final int[][] 装备级别对应的绑定符数组下标 = new int[][] { { 0, 10 }, { 11, 50 }, { 51, 100 }, { 101, 150 }, { 151, 200 }, { 201, 300 } };
	public static final String[] 装备绑定符数组 = new String[] { Translate.绑定符, Translate.绑定符, Translate.绑定符, Translate.绑定符, Translate.绑定符, Translate.绑定符 };

	/**
	 * 二维数组
	 * 第一维为挖取数组下标
	 * 第二维为宝石级别范围(MIN,MAX)
	 */
	public static final int[][] 宝石级别对应的挖取符数组下标 = new int[][] { { 0, 10 }, { 11, 50 }, { 51, 100 }, { 101, 150 }, { 151, 200 }, { 201, 300 } };

	public static final String[] 宝石挖取符数组 = new String[] { Translate.一级挖取符, Translate.一级挖取符, Translate.一级挖取符, Translate.一级挖取符, Translate.一级挖取符, Translate.一级挖取符 };

	/**
	 * 二维数组
	 * 第一维为升级符数组下标
	 * 第二维为装备级别范围(MIN,MAX)
	 */
	public static final int[][] 装备级别对应的升级符数组下标 = new int[][] { { 0, 90 }, { 91, 300 } };

	public static final String[][] 装备强化符数组 = new String[][] { { Translate.初级强化符, Translate.中级强化符 }, { Translate.中级强化符 } };

	// public static final String[] 装备幸运符数组 = new String[]{Translate.一级幸运符,Translate.一级幸运符,Translate.一级幸运符,Translate.一级幸运符,Translate.一级幸运符,Translate.一级幸运符};

	/**
	 * 二维数组
	 * 第一维为升级符成功率数组下标
	 * 第二维为装备强化星级范围(MIN,MAX)
	 */
	public static final int[][] 装备星级对应的升级符成功率数组下标 = new int[][] { { 0, 0 }, { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 5, 5 }, { 6, 6 }, { 7, 7 }, { 8, 8 }, { 9, 9 }, { 10, 10 }, { 11, 11 }, { 12, 12 }, { 13, 13 }, { 14, 14 }, { 15, 15 }, { 16, 16 }, { 17, 17 }, { 18, 18 }, { 19, 19 }, { 20, 20 }, { 21, 21 }, { 22, 22 }, { 23, 23 }, { 24, 24 }, { 25, 25 }, { 26, 26 }, { 27, 27 }, { 28, 28 }, { 29, 29 }, { 30, 30 } };

	// 不同颜色强化符在不同星级时的强化成功率分子，分母为TOTAL_LUCK_VALUE
	public static final int[] 装备白色强化符成功率数组_new = new int[] { 1000000, 1000000, 444400, 250000, 160000, 111100, 81600, 62500, 49400, 40000, 33100, 27800, 23700, 20400, 17800, 15600, 13800, 12300, 11100, 10000, 10000, 800, 400, 200, 120, 60, 20, 12, 8, 4, 2, 1 };
	public static final int[] 装备绿色强化符成功率数组_new = new int[] { 1000000, 1000000, 1000000, 1000000, 640000, 444400, 326500, 250000, 197500, 160000, 132200, 111100, 94700, 81600, 71100, 62500, 55400, 49400, 44300, 40000, 40000, 3200, 1600, 800, 480, 240, 80, 48, 32, 16, 8, 4 };
	public static final int[] 装备蓝色强化符成功率数组_new = new int[] { 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 734700, 562500, 444400, 360000, 297500, 250000, 213000, 183700, 160000, 140600, 124600, 111100, 99700, 90000, 90000, 21600, 10800, 5400, 3240, 1620, 540, 324, 216, 108, 54, 27 };
	public static final int[] 装备紫色强化符成功率数组_new = new int[] { 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 790100, 640000, 528900, 444400, 378700, 326500, 284400, 250000, 221500, 197500, 177300, 160000, 160000, 102400, 51200, 25600, 15360, 7680, 2560, 1536, 1024, 512, 256, 128 };
	public static final int[] 装备橙色强化符成功率数组_new = new int[] { 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 826400, 694400, 591700, 510200, 444400, 390600, 346000, 308600, 277000, 250000, 250000, 200000, 100000, 50000, 30000, 15000, 5000, 3000, 2000, 1000, 500, 250 };

	public static final int[] 装备白色强化符成功率数组 = new int[] { 10000, 10000, 4444, 2500, 1600, 1111, 816, 625, 494, 400, 331, 278, 237, 204, 178, 156, 138, 123, 111, 100 };
	public static final int[] 装备绿色强化符成功率数组 = new int[] { 10000, 10000, 10000, 10000, 6400, 4444, 3265, 2500, 1975, 1600, 1322, 1111, 947, 816, 711, 625, 554, 494, 443, 400 };
	public static final int[] 装备蓝色强化符成功率数组 = new int[] { 10000, 10000, 10000, 10000, 10000, 10000, 7347, 5625, 4444, 3600, 2975, 2500, 2130, 1837, 1600, 1406, 1246, 1111, 997, 900 };
	public static final int[] 装备紫色强化符成功率数组 = new int[] { 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 7901, 6400, 5289, 4444, 3787, 3265, 2844, 2500, 2215, 1975, 1773, 1600 };
	public static final int[] 装备橙色强化符成功率数组 = new int[] { 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 8264, 6944, 5917, 5102, 4444, 3906, 3460, 3086, 2770, 2500 };

	/**
	 * 总的幸运基数
	 */
	public static int TOTAL_LUCK_VALUE = 1000000;
	public boolean appServerOpenNewStar = true;

	public String[] 根据装备级别得到所需鉴定符(int level) {
		for (int i = 0; i < 装备鉴定级别对应的鉴定符数组下标.length; i++) {
			if (level >= 装备鉴定级别对应的鉴定符数组下标[i][0] && level <= 装备鉴定级别对应的鉴定符数组下标[i][1]) {
				return new String[] { 装备鉴定符数组[i] };
			}
		}
		return new String[0];
	}

	public String[] 根据装备级别得到所需铭刻符(int level) {
		for (int i = 0; i < 装备级别对应的铭刻符数组下标.length; i++) {
			if (level >= 装备级别对应的铭刻符数组下标[i][0] && level <= 装备级别对应的铭刻符数组下标[i][1]) {
				return new String[] { 装备铭刻符数组[i] };
			}
		}
		return new String[0];
	}

	// public String[] 根据装备级别得到所需绑定符(int level){
	// for(int i = 0; i < 装备级别对应的绑定符数组下标.length; i++){
	// if(level >= 装备级别对应的绑定符数组下标[i][0] && level <= 装备级别对应的绑定符数组下标[i][1]){
	// return new String[]{装备绑定符数组[i]};
	// }
	// }
	// return new String[0];
	// }

	// public String[] 根据宝石级别得到所需挖取符(int level) {
	// for (int i = 0; i < 宝石级别对应的挖取符数组下标.length; i++) {
	// if (level >= 宝石级别对应的挖取符数组下标[i][0] && level <= 宝石级别对应的挖取符数组下标[i][1]) {
	// return new String[] { 宝石挖取符数组[i] };
	// }
	// }
	// return new String[0];
	// }

	public String[] 根据装备级别得到所需强化符(int level) {
		for (int i = 0; i < 装备级别对应的升级符数组下标.length; i++) {
			if (level >= 装备级别对应的升级符数组下标[i][0] && level <= 装备级别对应的升级符数组下标[i][1]) {
				return 装备强化符数组[i];
			}
		}
		return new String[0];
	}

	// public String[] 根据装备级别得到所需幸运符(int level){
	// for(int i = 0; i < 装备级别对应的升级符数组下标.length; i++){
	// if(level >= 装备级别对应的升级符数组下标[i][0] && level <= 装备级别对应的升级符数组下标[i][1]){
	// return new String[]{装备幸运符数组[i]};
	// }
	// }
	// return new String[0];
	// }

	/**
	 * 返回分子值，分母为TOTAL_LUCK_VALUE
	 * @param level
	 * @return
	 */
	public int[] 根据装备强化等级得到不同颜色强化符的成功率分子值(int level) {
		for (int i = 0; i < 装备星级对应的升级符成功率数组下标.length; i++) {
			if (level >= 装备星级对应的升级符成功率数组下标[i][0] && level <= 装备星级对应的升级符成功率数组下标[i][1]) {
				return new int[] { 装备白色强化符成功率数组[i], 装备绿色强化符成功率数组[i], 装备蓝色强化符成功率数组[i], 装备紫色强化符成功率数组[i], 装备橙色强化符成功率数组[i] };
			}
		}
		return new int[0];
	}

	public int[] 根据装备强化等级得到不同颜色强化符的成功率分子值_new(int level) {
		for (int i = 0; i < 装备星级对应的升级符成功率数组下标.length; i++) {
			if (level >= 装备星级对应的升级符成功率数组下标[i][0] && level <= 装备星级对应的升级符成功率数组下标[i][1]) {
				return new int[] { 装备白色强化符成功率数组_new[i], 装备绿色强化符成功率数组_new[i], 装备蓝色强化符成功率数组_new[i], 装备紫色强化符成功率数组_new[i], 装备橙色强化符成功率数组_new[i] };
			}
		}
		return new int[0];
	}

	/**
	 * 返回分子值，分母为TOTAL_LUCK_VALUE
	 * @param level
	 * @return
	 */
	// public int[] 根据装备强化星级得到幸运符的成功率分子值(int level){
	// for(int i = 0; i < 装备星级对应的升级符成功率数组下标.length; i++){
	// if(level >= 装备星级对应的升级符成功率数组下标[i][0] && level <= 装备星级对应的升级符成功率数组下标[i][1]){
	// return new int[]{装备幸运符成功率数组[i]};
	// }
	// }
	// return new int[0];
	// }

	public static final byte 物品一级分类_角色装备类 = 0;
	public static final byte 物品一级分类_马匹装备类 = 1;
	public static final byte 物品一级分类_角色法宝类 = 2;
	public static final byte 物品一级分类_角色翅膀类 = 3;
	public static final byte 物品一级分类_宝石类 = 4;
	public static final byte 物品一级分类_任务类 = 5;
	public static final byte 物品一级分类_人物消耗品类 = 6;
	public static final byte 物品一级分类_宠物消耗品 = 7;
	public static final byte 物品一级分类_庄园消耗品 = 8;
	public static final byte 物品一级分类_宠物蛋 = 9;
	public static final byte 物品一级分类_古董类 = 10;
	public static final byte 物品一级分类_庄园果实类 = 11;
	public static final byte 物品一级分类_材料类 = 12;
	public static final byte 物品一级分类_角色药品 = 13;
	public static final byte 物品一级分类_宠物药品 = 14;
	public static final byte 物品一级分类_角色技能书类 = 15;
	public static final byte 物品一级分类_宠物技能书类 = 16;
	public static final byte 物品一级分类_包裹类 = 17;
	public static final byte 物品一级分类_宝箱类 = 18;
	public static final byte 物品一级分类_商品类 = 19;
	public static final byte 物品一级分类_宠物类 = 20;
	public static final byte 物品一级分类_坐骑类 = 21;
	public static final byte 物品一级分类_坐骑食物 = 22;
	public static final byte 物品一级分类_时装类 = 23;
	public static final byte 物品一级分类_灵髓 = 24;

	public static final String[] 物品一级分类类名 = new String[] { Translate.角色装备类, Translate.马匹装备类, Translate.法宝类, Translate.翅膀类, Translate.宝石类, Translate.任务类, Translate.人物消耗品类, Translate.宠物消耗品类, Translate.庄园消耗品, Translate.宠物蛋, Translate.古董类, Translate.庄园果实类, Translate.材料类, Translate.角色药品, Translate.宠物药品, Translate.角色技能书类, Translate.宠物技能书类, Translate.包裹类, Translate.宝箱类, Translate.商品类, Translate.宠物类, Translate.坐骑类, 
		Translate.坐骑食物, Translate.时装类,Translate.灵髓类};

	public static final byte 物品二级分类_武器 = 0;
	public static final byte 物品二级分类_头 = 1;
	public static final byte 物品二级分类_肩 = 2;
	public static final byte 物品二级分类_胸 = 3;
	public static final byte 物品二级分类_护腕 = 4;
	public static final byte 物品二级分类_腰带 = 5;
	public static final byte 物品二级分类_鞋 = 6;
	public static final byte 物品二级分类_饰品 = 7;
	public static final byte 物品二级分类_项链 = 8;
	public static final byte 物品二级分类_戒指 = 9;
	public static final String[] 物品二级分类类名_角色装备类 = new String[] { Translate.武器, Translate.头盔, Translate.护肩, Translate.胸, Translate.护腕, Translate.腰带, Translate.靴子, Translate.首饰, Translate.项链, Translate.戒指 };

	public static final byte 物品二级分类_面甲 = 0;
	public static final byte 物品二级分类_颈甲 = 1;
	public static final byte 物品二级分类_体铠 = 2;
	public static final byte 物品二级分类_鞍铠 = 3;
	public static final byte 物品二级分类_蹄甲 = 4;
	public static final String[] 物品二级分类类名_马匹装备类 = new String[] { Translate.面甲, Translate.颈甲, Translate.体铠, Translate.鞍铠, Translate.蹄甲, };
	public static final String[] 物品二级分类类名_角色法宝类 = new String[0];
	public static final String[] 物品二级分类类名_角色翅膀类 = new String[0];
	
	public static final byte 物品二级分类_灵髓宝石 = 0;
	public static final byte 物品二级分类_仙气葫芦 = 1; 
	public static final String[] 物品二级分类类名_灵髓类 = new String[]{Translate.灵髓宝石, Translate.仙气葫芦};

	public static final byte 物品二级分类_绿宝石 = 0;
	public static final byte 物品二级分类_橙宝石 = 1;
	public static final byte 物品二级分类_橙晶石 = 2;
	public static final byte 物品二级分类_黑金石 = 3;
	public static final byte 物品二级分类_蓝宝石 = 4;
	public static final byte 物品二级分类_红宝石 = 5;
	public static final byte 物品二级分类_白晶石 = 6;
	public static final byte 物品二级分类_黄宝石 = 7;
	public static final byte 物品二级分类_紫晶石 = 8;
	public static final byte 物品二级分类_光效宝石 = 9;
	public static final String[] 物品二级分类类名_宝石类 = new String[] { Translate.绿宝石, Translate.橙宝石, Translate.橙晶石, Translate.黑金石, Translate.蓝宝石, Translate.红宝石, Translate.白晶石, Translate.黄宝石, Translate.紫晶石, Translate.光效宝石 };

	public static final String[] 物品二级分类类名_任务类 = new String[0];

	public static final byte 物品二级分类_封魔录 = 0;
	public static final byte 物品二级分类_酒 = 1;
	public static final byte 物品二级分类_押镖令 = 2;
	public static final byte 物品二级分类_元气丹 = 3;
	public static final String[] 物品二级分类类名_人物消耗品类 = new String[] {Translate.封魔录, Translate.酒, Translate.押镖令, Translate.元气丹 };

	public static final byte 物品二级分类_炼妖石 = 0;
	public static final byte 物品二级分类_宠物内丹 = 1;
	public static final String[] 物品二级分类类名_宠物消耗品 = new String[] { Translate.炼妖石, Translate.宠物内丹, };

	public static final byte 物品二级分类_田地令 = 0;
	public static final byte 物品二级分类_鲁班令 = 1;
	public static final byte 物品二级分类_加速令 = 2;
	public static final String[] 物品二级分类类名_庄园消耗品 = new String[] { Translate.田地令, Translate.鲁班令, Translate.加速令, };

	public static final byte 物品二级分类_兽灵 = 0;
	public static final byte 物品二级分类_精怪 = 1;
	public static final byte 物品二级分类_神兽 = 2;
	public static final byte 物品二级分类_仙魔 = 3;
	public static final String[] 物品二级分类类名_宠物蛋 = new String[] { Translate.兽灵, Translate.精怪, Translate.神兽, Translate.仙魔 };

	public static final byte 物品二级分类_高级古董 = 0;
	public static final byte 物品二级分类_普通古董 = 1;
	public static final String[] 物品二级分类类名_古董类 = new String[] { Translate.高级古董, Translate.普通古董, };

	public static final byte 物品二级分类_经验果 = 0;
	public static final byte 物品二级分类_祝福果 = 1;
	public static final byte 物品二级分类_灵草 = 2;
	public static final byte 物品二级分类_灵花 = 3;
	public static final byte 物品二级分类_灵果 = 4;
	public static final String[] 物品二级分类类名_庄园果实类 = new String[] { Translate.经验果, Translate.祝福果, Translate.灵草, Translate.灵花, Translate.灵果 };

	public static final byte 物品二级分类_圣物 = 0;
	public static final byte 物品二级分类_蚕丝 = 1;
	public static final byte 物品二级分类_翅膀图纸 = 2;
	public static final String[] 物品二级分类类名_材料类 = new String[] { Translate.圣物, Translate.蚕丝, Translate.翅膀图纸 };

	public static final byte 物品二级分类_缓回血量 = 0;
	public static final byte 物品二级分类_缓回法力值 = 1;
	public static final byte 物品二级分类_瞬回药品 = 2;
	public static final byte 物品二级分类_增益药品 = 3;
	public static final byte 物品二级分类_增加经验 = 4;
	public static final String[] 物品二级分类类名_角色药品 = new String[] { Translate.缓回血量, Translate.缓回法力值, Translate.瞬回药品, Translate.增益药品, Translate.增加经验 };

	public static final byte 物品二级分类_宠物瞬回 = 0;
	public static final byte 物品二级分类_宠物缓回 = 1;
	public static final byte 物品二级分类_宠物寿命 = 2;
	public static final byte 物品二级分类_宠物快乐 = 3;
	public static final String[] 物品二级分类类名_宠物药品 = new String[] { Translate.宠物瞬回, Translate.宠物缓回, Translate.宠物寿命, Translate.宠物快乐 };

	public static final byte 物品二级分类_职业_修罗 = 0;
	public static final byte 物品二级分类_职业_影魅 = 1;
	public static final byte 物品二级分类_职业_仙心 = 2;
	public static final byte 物品二级分类_职业_九黎 = 3;
	public static final String[] 物品二级分类类名_角色技能书类 = new String[] { Translate.修罗, Translate.影魅, Translate.仙心, Translate.九黎 };

	public static final String[] 物品二级分类类名_宠物技能书类 = new String[] {};

	public static final byte 物品二级分类_包裹 = 0;
	public static final String[] 物品二级分类类名_包裹类 = new String[] { Translate.包裹 };

	public static final byte 物品二级分类_单组随机宝箱 = 0;
	public static final byte 物品二级分类_多组随机宝箱 = 1;
	public static final String[] 物品二级分类类名_宝箱类 = new String[] { Translate.单组随机宝箱, Translate.多组随机宝箱, };

	public static final byte 物品二级分类_普通商品 = 0;
	public static final byte 物品二级分类_特产 = 1;
	public static final String[] 物品二级分类类名_商品类 = new String[] { Translate.普通商品, Translate.特产, };

	public static final String[] 物品二级分类类名_宠物类 = new String[] {};

	public static final byte 物品二级分类_修罗坐骑 = 0;
	public static final byte 物品二级分类_影魅坐骑 = 1;
	public static final byte 物品二级分类_九黎坐骑 = 2;
	public static final byte 物品二级分类_法尊坐骑 = 3;
	public static final String[] 物品二级分类类名_坐骑类 = new String[] { Translate.修罗坐骑, Translate.影魅坐骑, Translate.九黎坐骑, Translate.仙心坐骑 };

	public static final String[] 物品二级分类类名_坐骑食物 = new String[] {};
	public static final byte 物品二级分类_面具 = 0;
	public static final byte 物品二级分类_时装 = 1;
	public static final String[] 物品二级分类类名_时装类 = new String[] { Translate.面具, Translate.时装 };

	public static final String[][] 物品二级分类类名 = new String[][] { 物品二级分类类名_角色装备类, 物品二级分类类名_马匹装备类, 物品二级分类类名_角色法宝类, 物品二级分类类名_角色翅膀类, 物品二级分类类名_宝石类, 物品二级分类类名_任务类, 物品二级分类类名_人物消耗品类, 物品二级分类类名_宠物消耗品, 物品二级分类类名_庄园消耗品, 物品二级分类类名_宠物蛋, 物品二级分类类名_古董类, 物品二级分类类名_庄园果实类, 物品二级分类类名_材料类, 物品二级分类类名_角色药品, 物品二级分类类名_宠物药品, 物品二级分类类名_角色技能书类, 物品二级分类类名_宠物技能书类, 物品二级分类类名_包裹类, 物品二级分类类名_宝箱类, 物品二级分类类名_商品类, 物品二级分类类名_宠物类, 物品二级分类类名_坐骑类, 物品二级分类类名_坐骑食物, 物品二级分类类名_时装类 
		, 物品二级分类类名_灵髓类};

	public String[] 根据物品一级二级分类得到物品名称列表(String firstName, String secondName) {
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0, n = allArticles.length; i < n; i++) {
			if (allArticles[i].get物品一级分类() == null) {
				continue;
			}
			if (allArticles[i].get物品二级分类() == null) {
				continue;
			}
			if (allArticles[i].get物品一级分类().equals(firstName) && allArticles[i].get物品二级分类().equals(secondName)) {
				if(allArticles[i] instanceof InlayArticle){
					names.add(((InlayArticle)allArticles[i]).getShowName());
				}else{
					names.add(allArticles[i].getName());
				}
				
			}
		}
		return names.toArray(new String[0]);
	}

	/**
	 * 得到自动挂机的物品列表
	 * @param level
	 * @param firstName
	 * @param secondName
	 * @return
	 */
	public String[] getAutoHookPropName(int level, String firstName, String secondName) {
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0, n = allArticles.length; i < n; i++) {
			if (allArticles[i].get物品一级分类() == null) {
				continue;
			}
			if (allArticles[i].get物品二级分类() == null) {
				continue;
			}
			if(secondName != null && secondName.equals("封魔录")){
				secondName = "封魔录";
			}
			if (allArticles[i].get物品一级分类().equals(firstName) && allArticles[i].get物品二级分类().equals(secondName)) {
				if (allArticles[i] instanceof Props) {
					if (((Props) allArticles[i]).getLevelLimit() <= level) {
						names.add(allArticles[i].getName());
					}
				}
			}
		}
		return names.toArray(new String[0]);
	}

	/**
	 * 返回-1表示没有分类
	 * @param categoryName
	 * @return
	 */
	public static int 根据物品一级分类名得到一级分类数值(String categoryName) {
		for (int i = 0; i < 物品一级分类类名.length; i++) {
			String s = 物品一级分类类名[i];
			if (s.equals(categoryName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 返回-1表示没有分类
	 * @param categoryName
	 * @return
	 */
	public static int 根据物品二级分类名得到二级分类数值(String categoryName) {
		for (int i = 0; i < 物品二级分类类名.length; i++) {
			String ss[] = 物品二级分类类名[i];
			if (ss != null) {
				for (int j = 0; j < ss.length; j++) {
					String s = ss[j];
					if (s.equals(categoryName)) {
						return j;
					}
				}
			}
		}
		return -1;
	}

	public static Random random = new Random();

	/**
	 * 套装有职业之分，每个职业开启的属性顺序不一样
	 * 数组中第一维为职业，分别为
	 */
	public static int[][] suitPropertyIndexByCareer = new int[][] { { 0, 1, 2, 3 }, { 0, 2, 1, 3 }, { 0, 1, 3, 2 }, { 1, 0, 2, 3 }, { 1, 2, 0, 3 }, { 3, 2, 0, 1, }, { 3, 2, 1, 0 } };

	public static String[] knapsackNames = new String[] { Translate.装备, Translate.奇珍, Translate.异宝, Translate.任务, Translate.宠物 };

	public static String 得到背包名字(int knapsackIndex) {
		if (knapsackIndex >= 0 && knapsackIndex < knapsackNames.length) {
			return knapsackNames[knapsackIndex];
		}
		return "";
	}

	private final int[] pinzhixishu = new int[] { 10, 20, 30, 40, 50 };
	private final int touxishu = 15;
	private final int shouxishu = 10;
	// protected File articleDataFile;

	/**
	 * 掉落方案文件
	 */
	protected File flopSchemeFile;

	/**
	 * 物品文件版本信息
	 */
	protected float version = 1.0f;

	public static String encoding = "utf-8";

	/**
	 * 套装文件
	 */
	protected File suitEquipmentFile;

	// DefaultDiskCache articleDataCache = null;

	/**
	 * 物品的配置文件
	 */
	protected File configFile;

	public static final int 物品文件装备所在sheet = 0;// 第1个
	public static final int 物品文件套装所在sheet = 1;
	public static final int 物品文件宝石所在sheet = 2;
	public static final int 物品文件一般物品所在sheet = 3;
	public static final int 物品文件可合成仅仅改变颜色物品所在sheet = 4;
	public static final int 物品文件给玩家用的瞬间恢复药品所在sheet = 6;
	public static final int 物品文件给宠物用的瞬间恢复药品所在sheet = 7;
	public static final int 物品文件召唤宠物道具所在sheet = 8;
	public static final int 物品文件宠物蛋道具所在sheet = 9;
	public static final int 物品文件宠物食物道具所在sheet = 10;
	public static final int 物品文件buff类道具所在sheet = 11;
	public static final int 坐骑道具所在sheet = 12;
	public static final int 喂养坐骑物品所在sheet = 13;
	public static final int 道具种类所在sheet = 5;
	public static final int 物品文件防爆包道具所在sheet = 14;
	public static final int 宠物buff类道具所在sheet = 15;
	public static final int 洗髓丹道具所在sheet = 16;
	public static final int 技能书道具所在sheet = 17;
	public static final int 任务物品所在sheet = 18;
	public static final int 时装道具所在sheet = 19;
	public static final int 王者之印所在sheet = 20;
	public static final int 召集令所在sheet = 21;
	public static final int 宝箱所在sheet = 22;
	public static final int 随机宝箱所在sheet = 23;
	public static final int 境界所在sheet = 24;
	public static final int 仙缘活动所在sheet = 25;
	public static final int 可交换宝物所在sheet = 26;
	public static final int 寻宝道具所在sheet = 27;
	public static final int 特殊装备1所在sheet = 28;
	public static final int 特殊装备2所在sheet = 29;
	public static final int 混沌万灵榜对应宝石所在sheet = 30;
	public static final int 古董所在sheet = 31;
	public static final int 寻宝藏宝图所在sheet = 32;
	public static final int 触发任务道具所在Sheet = 33;
	public static final int 物品文件buff_可合成_类道具所在Sheet = 34;
	public static final int 金钱类道具所在Sheet = 35;
	public static final int 职业宝箱类道具所在Sheet = 36;
	public static final int 洗红名类道具所在Sheet = 37;
	public static final int 加体力类道具所在Sheet = 38;
	public static final int 采花大盗物品所在Sheet = 39;
	public static final int 器灵物品所在Sheet = 40;
	public static final int 等级随机礼包所在Sheet = 41;
	public static final int gainTitleItem_Sheet = 42;// 使用后可获得称号的页签

	public static final int 打折卡所在Sheet = 43;
	public static final int 器灵丹道具所在Sheet = 44; // 器灵丹 用来给器灵吞噬加魂量
	public static final int 召唤生物道具所在Sheet = 45; // 召唤生物所在的sheet
	public static final int 触发输入框道具所在Sheet = 46; // 召唤生物所在的sheet
	public static final int petSkillBook_Sheet = 47;// 宠物技能书页签
	public static final int robberyPorps_Sheet = 48;// 天劫可使用道具列表
	public static final int 挖宝道具所在Sheet = 49;// 挖宝道具列表
	public static final int 变异宠道具所在Sheet = 50;// 变异宠道具列表

	public static final int 法宝所在Sheet = 51;// 法宝
	public static final int 法宝吞噬道具所在Sheet = 52;// 法宝吞噬道具
	public static final int 宠物悟性丹所在Sheet = 53;
	public static final int 图纸合成规则所在Sheet = 54;
	public static final int 装备图纸所在Sheet = 55;
	public static final int 装备套装所在Sheet = 56;
	public static final int 翅膀所在Sheet = 57;
	public static final int 光效宝石所在Sheet = 58;
	public static final int 附魔道具所在Sheet = 59;
	public static final int 兽魂道具所在Sheet = 60;
	// public static final int 宠物灵性道具所在Sheet = 61;
	public static final int 减仙婴附体cd道具Sheet = 61;
	public static final int 坐骑扩阶道具Sheet = 62;
	public static final int 灵髓道具Sheet = 63;
	public static final int 仙气葫芦道具Sheet = 64;
	public static final int 坐骑魂石道具Sheet = 65;
	public static final int 宠物饰品道具Sheet = 66;

	public static final int 物品文件最大所在sheet = 66;

	/**
	 * 所有物品都具有的属性在excel中的位置
	 */
	public static final int 所有物品_物品名_列 = 0;
	public static final int 所有物品_物品名_列_STAT = 1;
	public static final int 所有物品_图标_列 = 2;
	public static final int 所有物品_放入哪个包裹_列 = 3;
	public static final int 所有物品_绑定方式_列 = 4;
	public static final int 所有物品_物品数值等级_列 = 5;
	public static final int 所有物品_物品颜色_列 = 6;
	public static final int 所有物品_可否重叠_列 = 7;
	public static final int 所有物品_重叠最大数量_列 = 8;
	public static final int 所有物品_物品一级分类_列 = 9;
	public static final int 所有物品_物品一级分类_列_STAT = 10;
	public static final int 所有物品_物品二级分类_列 = 11;
	public static final int 所有物品_物品二级分类_列_STAT = 12;
	public static final int 所有物品_故事_列 = 13;
	public static final int 所有物品_描述_列 = 14;
	public static final int 所有物品_使用方式_列 = 15;
	public static final int 所有物品_获取方式_列 = 16;
	public static final int 所有物品_有无有效期_列 = 17;
	public static final int 所有物品_激活方式_列 = 18;
	public static final int 所有物品_最大有效期_列 = 19;
	public static final int 所有物品_是否可卖_列 = 20;
	public static final int 所有物品_卖价_列 = 21;
	public static final int 所有物品_掉落NPC形象_列 = 22;
	/**
	 * 法宝属性所在excel中的位置
	 */
	public static final int 法宝_使用等级限制 = 23;
	public static final int 法宝_数值等级 = 24;
	public static final int 法宝_使用境界限制 = 25;
	public static final int 法宝_使用职业限制 = 26;
	public static final int 法宝_使用元神限制 = 27;
	public static final int 法宝_耐久 = 28;
	public static final int 法宝_进化后法宝名 = 29;
	public static final int 法宝_法宝npc = 30;

	/**
	 * 所有道具都具有的道具属性在excel中的位置
	 */
	public static final int 所有道具_道具类分类类名_列 = 23;
	public static final int 所有道具_道具类分类类名_列_STAT = 24;
	public static final int 所有道具_是否有使用次数限制_列 = 25;
	public static final int 所有道具_使用次数限制_列 = 26;
	public static final int 所有道具_使用后不消失_列 = 27;
	public static final int 所有道具_玩家等级限制_列 = 28;
	public static final int 所有道具_玩家境界限制_列 = 29;
	public static final int 所有道具_战斗状态限制_列 = 30;

	// 宝石sheet
	public static final int 宝石_宝石镶嵌颜色_列 = 23;
	public static final int 宝石_合成后的宝石_列 = 24;
	public static final int 宝石_合成后的宝石_列_STAT = 25;
	public static final int 宝石_境界限制 = 26;
	public static final int 宝石_类型 = 27;

	// 简单道具sheet
	public static final int 简单道具能修改的属性数 = 4;
	public static final int 简单道具_血_列 = 31;
	public static final int 简单道具_蓝_列 = 32;
	public static final int 简单道具_经验_列 = 33;
	public static final int 简单道具_元气_列 = 34;

	// 简单宠物道具sheet
	public static final int 简单宠物道具能修改的属性数 = 2;
	public static final int 简单宠物道具_血_列 = 31;
	public static final int 简单宠物道具_经验_列 = 32;

	// 宠物道具sheet
	public static final int 宠物道具_对应avata_race_列 = 31;
	public static final int 宠物道具_avata_sex_列 = 32;
	public static final int 宠物道具_title_列 = 33;
	public static final int 宠物道具对应的蛋的名称_title_列 = 34;
	public static final int 宠物道具对应的蛋的名称_title_列_STAT = 35;
	public static final int 宠物道具对应大小 = 36;
	public static final int 宠物道具对应颜色 = 37;
	public static final int 宠物道具是否透明 = 38;
	public static final int 宠物道具_宠物粒子效果 = 39;
	public static final int 宠物天生技能一 = 40;
	public static final int 宠物天生技能二 = 41;

	// 宠物蛋道具sheet
	public static final int 宠物蛋道具_对应孵化出来的宠物名称_列 = 31;
	public static final int 宠物蛋道具_对应孵化出来的宠物名称_列_STAT = 32;
	public static final int 宠物蛋道具_稀有度_列 = 33;
	public static final int 宠物蛋道具_可携带等级_列 = 34;
	public static final int 宠物蛋道具_职业倾向_列 = 35;
	public static final int 宠物蛋道具_性格_列 = 36;
	public static final int 宠物蛋道具_分类_列 = 37;// 0-普通宠,1-神宠(不在随机礼包出现)

	// 洗髓丹道具sheet
	public static final int 洗髓丹道具_返还修法值比例_列 = 31;

	// 防爆包道具sheet
	public static final int 防爆包道具_背包扩展数量_列 = 31;

	// 坐骑道具
	public static final int 是否是战斗坐骑 = 31;
	public static final int 是否是飞行坐骑 = 32;
	public static final int 坐骑道具_坐骑type = 33;
	public static final int 坐骑道具_等阶 = 34;
	public static final int 坐骑道具_速度 = 35;
	public static final int 坐骑道具_坐骑最大体力值 = 36;
	public static final int 坐骑道具_对应avata_列 = 37;
	public static final int 坐骑道具_粒子 = 38;
	public static final int 坐骑道具_是否特殊坐骑 = 39;

	// 喂养宠物物品
	// public static final int 喂养宠物物品_增加值_级别 = 20;
	public static final int 喂养宠物物品_类型 = 23;

	// 喂养坐骑物品
	public static final int 喂养坐骑物品_增加体力值 = 23;
	public static final int 喂养坐骑物品_类型 = 24;

	// 技能书id
	public static final int 技能书_技能id = 31;
	public static final int 技能书_技能等级 = 32;
	public static final int 技能书_技能分类 = 33;
	public static final int 技能书_性格 = 34;
	// 粒子光效id
	public static final int liziguangxiao = 31;
	public static final int buffname = 32;
	public static final int invalidtime = 33;

	// 时装道具
	public static final int 时装道具_avatasex = 31;
	public static final int 时装道具_type = 32;
	public static final int 时装道具_values = 33;
	public static final int 人时装还是坐骑时装 = 34;

	// 装备
	public static final int 装备名 = 0;
	public static final int 装备名_STAT = 1;
	public static final int 装备类型 = 2;
	public static final int 武器类型 = 3;
	public static final int 物品使用级别 = 4;
	public static final int 物品数值级别 = 5;
	public static final int 门派限制 = 6;
	public static final int 境界限制 = 7;
	public static final int 耐久 = 8;
	public static final int 是否可卖 = 9;
	public static final int 卖价 = 10;
	public static final int 掉落形象 = 11;
	public static final int 图标 = 12;
	public static final int 精炼后的装备 = 13;
	public static final int 精炼后的装备_STAT = 14;
	public static final int 装备avata = 15;
	public static final int 装备强化粒子光效 = 16;
	public static final int 装备故事 = 17;
	public static final int 装备技能 = 18;
	public static final int 装备有无有效期 = 19;
	public static final int 装备有效期激活方式 = 20;
	public static final int 装备最大有效期 = 21;

	/**
	 * 宝箱道具
	 */
	public static final int 宝箱道具_物品绑定标记 = 31;
	public static final int 宝箱道具_包裹中的物品名字 = 32;
	public static final int 宝箱道具_包裹中的物品名字_STAT = 33;
	public static final int 宝箱道具_包裹中的物品数量 = 34;
	public static final int 宝箱道具_包裹中的物品颜色 = 35;
	public static final int 宝箱道具_广播 = 36;

	/**
	 * 随机宝箱道具
	 */
	public static final int 随机宝箱道具_物品绑定标记 = 31;
	public static final int 随机宝箱道具_包裹中的物品名字 = 32;
	public static final int 随机宝箱道具_包裹中的物品名字_STAT = 33;
	public static final int 随机宝箱道具_包裹中的物品几率 = 34;
	public static final int 随机宝箱道具_包裹中的物品数量 = 35;
	public static final int 随机宝箱道具_包裹中的物品颜色 = 36;
	public static final int 随机宝箱道具_包裹中的物品广播 = 37;
	public static final int 随机宝箱道具_包裹中的物品广播_STAT = 38;
	public static final int 随机宝箱道具_消耗的道具名 = 39;
	public static final int 随机宝箱道具_消耗的道具名_STAT = 40;
	public static final int 随机宝箱道具_消耗的道具颜色 = 41;
	public static final int 随机宝箱道具_消耗的道具数量 = 42;
	public static final int 随机宝箱道具_使用时播放哪个粒子 = 43;
	public static final int 随机宝箱道具_播放粒子时长 = 44;
	public static final int 随机宝箱道具_播放位置上 = 45;
	public static final int 随机宝箱道具_播放位置左 = 46;
	public static final int 随机宝箱道具_播放位置前 = 47;
	public static final int 随机宝箱道具_光效可见类型 = 48;
	public static final int 随机宝箱道具_世界广播 = 49;

	/**
	 * buff道具
	 */
	public static final int buff道具_种植类型 = 31;
	public static final int buff道具_范围 = 32;
	public static final int buff道具_buff的名称 = 33;
	public static final int buff道具_buff的名称_STAT = 34;
	public static final int buff道具_buff级别 = 35;
	public static final int buff道具_buff持续的时间 = 36;
	public static final int buff道具_buff伴生物品名 = 37;

	/**
	 * 仙缘活动
	 */
	public static final int activity_type = 31;
	public static final int activity_avata = 32;

	/**
	 * 篝火活动
	 */

	public static final int 一天可以使用几次 = 27;
	public static final int 使用后产生的buffer名 = 28;
	public static final int fire_activity_avata = 29;

	/**
	 * 可交换宝物
	 */
	public static final int 可配对的交换物品 = 23;
	public static final int 可配对的交换物品_STAT = 24;
	public static final int 配对成功后生成的物品 = 25;
	public static final int 配对成功后生成的物品_STAT = 26;
	public static final int 宝物类型 = 27;
	public static final int 是否还用 = 28;

	/**
	 * 古董道具
	 */
	public static final int 古董道具_包裹中的物品名字 = 31;
	public static final int 古董道具_包裹中的物品名字_STAT = 32;
	public static final int 古董道具_掉落几率值 = 33;
	public static final int 古董道具_包裹中的物品颜色 = 34;
	public static final int 古董道具_物品的个数 = 35;
	public static final int 古董道具_物品绑定标记 = 36;

	/**
	 * 召集令
	 */
	public static final int 召集令道具_类型 = 31;
	public static final int 召集令道具_目标城市 = 32;
	public static final int 召集令道具_是否限制国家 = 33;

	/**
	 * 触发任务道具
	 */
	public static final int 触发任务道具_任务id = 31;
	public static final int 触发任务道具_类型 = 32;

	/**
	 * buff_可合成_道具
	 */
	public static final int buff_可合成_道具_buff的名称 = 31;
	public static final int buff_可合成_道具_buff的名称_STAT = 32;
	public static final int buff_可合成_道具_buff级别 = 33;
	public static final int buff_可合成_道具_buff持续的时间 = 34;
	public static final int 道具的使用上限 = 35;

	/**
	 * 金钱类道具
	 */
	public static final int 金钱类道具_钱 = 31;
	public static final int 金钱类道具_类型 = 32;
	/**
	 * 宝箱道具
	 */
	public static final int 职业包裹类道具_物品绑定标记 = 31;
	public static final int 职业包裹类道具_包裹中的物品名字 = 32;
	public static final int 职业包裹类道具_包裹中的物品名字_STAT = 33;
	public static final int 职业包裹类道具_物品的个数 = 34;
	public static final int 职业包裹类道具_包裹中的物品颜色 = 35;
	public static final int 职业包裹类道具_是否广播 = 52;
	public static final int 职业包裹类道具_广播 = 53;
	public static final int 职业包裹类道具_广播_STAT = 54;
	public static final int 职业包裹类道具_广播类型 = 55;

	/**
	 * 红名道具
	 */
	public static final int 洗红名道具_type = 31;
	public static final int 洗红名道具_value = 32;
	public static final int 洗红名道具_limit = 33;

	public static final int 增加的悟性丹值 = 31;
	public static final int 增加的悟性丹几率 = 32;
	public static final int 可用丹的宠物名 = 33;

	public static final int 仙装升级规则名 = 31;

	/**
	 * 体力道具
	 */
	public static final int 体力道具_value = 31;

	/**
	 * 使用后获得称号，id就是名称
	 */
	public static final int 称号道具_ID = 31;
	public static final int 称号道具统计_ID = 32;

	// 器灵丹道具
	public static final int 器灵丹道具_value = 23;

	/**
	 * 采花大盗物品
	 */
	public static final int 采花物品类型 = 23;
	public static final int 采花物品基础值 = 24;
	public static final int 采花物品类型描述 = 25;

	/**
	 * 烟花道具
	 */
	public static final int 烟花道具产生npcId = 27;
	public static final int 体力道具粒子名称 = 28;
	public static final int 体力道具持续时间 = 29;
	public static final int 位置 = 30;

	/**
	 * 烟花道具
	 */
	public static final int 器灵类型 = 23;
	public static final int 器灵装备限制 = 24;
	public static final int 器灵属性倍数 = 25;
	public static final int 器灵魂量倍数 = 26;

	/**
	 * 打折卡道具
	 */
	public static int 打折的道具名 = 23;
	public static int 打折的道具名_STAT = 24;
	public static int 打折的道具颜色 = 25;
	public static int 打折的折扣 = 26;
	public static int 打折要求购买的道具数量 = 27;
	public static int 打折的商店名 = 28;
	public static int 打折的商店名_STAT = 29;

	/**
	 * 变异宠道具
	 */
	public static final int 变异宠道具_petName = 31;
	public static final int 变异宠道具_petNameStat = 32;
	public static final int 变异宠道具_generation = 33;
	public static final int 变异宠道具_variation = 34;
	public static final int 变异宠道具_jichuSkills = 35;
	public static final int 变异宠道具_jichuLvs = 36;
	public static final int 变异宠道具_tianfuSkills = 37;
	// public static final int 变异宠道具_tianfuLvs = 38;
	public static final int 变异宠道具_openxiantian = 38;
	public static final int 变异宠道具_openhoutian = 39;

	/**
	 * 翅膀道具列
	 */
	public static final int 翅膀道具_kindId = 31;

	/**
	 * 坐骑扩阶道具列
	 */
	public static final int 坐骑扩阶道具_canUseLevel = 31;
	public static final int 坐骑扩阶道具_upLevel = 32;
	/**
	 * 坐骑魂石道具列
	 */
	public static final int 坐骑魂石道具_type = 31;
	public static final int 坐骑魂石道具_index = 32;

	/*
	 * 掉落集合文件
	 */
	protected File configDropSetFile;

	// protected HashMap<String,Article> articles = new HashMap<String,Article>();

	// 全部物品
	protected Article[] allArticles;

	private Map<String, Article> allArticleNameMap = new Hashtable<String, Article>();
	private Map<String, Article> allArticleCNNameMap = new Hashtable<String, Article>();

	public void setAllArticles(Article[] allArticles) {
		this.allArticles = allArticles;
	}

	// 全部武器
	protected Weapon[] allWeapons;

	// 全部刀
	protected Weapon[] allWeaponKnifes;

	// 全部剑
	protected Weapon[] allWeaponSwords;

	// 全部棍
	protected Weapon[] allWeaponSkicks;

	// 全部匕首
	protected Weapon[] allWeaponDaggers;

	// 全部弓
	protected Weapon[] allWeaponBows;

	// 全部头盔
	protected Equipment[] allEquipmentheads;

	// 全部护肩
	protected Equipment[] allEquipmentshoulders;

	// 全部衣服
	protected Equipment[] allEquipmentbodys;

	// 全部护手
	protected Equipment[] allEquipmenthands;

	// 全部鞋子
	protected Equipment[] allEquipmentfoots;

	// 全部首饰
	protected Equipment[] allEquipmentjewelrys;

	// 全部项链
	protected Equipment[] allEquipmentnecklace;

	// 全部戒指
	protected Equipment[] allEquipmentfingerring;

	// 全部非武器装备
	protected Equipment[] allEquipments;

	/**
	 * 唯一性装备
	 */
	public Equipment[] allSpecialEquipments1;

	/**
	 * 不唯一性装备
	 */
	public Equipment[] allSpecialEquipments2;

	// 全部简单道具
	protected SingleProps[] allSingleProps;

	// 全部红名道具
	protected ClearRedProps[] allClearRedProps;

	// 全部体力道具
	protected TiliProps[] allTiliProps;

	protected QiLingArticle[] allQiLingArticle;

	protected FireFlowerProps[] allFireFlowerProps;

	// 全部简单宠物道具
	protected SingleForPetProps[] allSingleForPetProps;

	// 全部召唤NPC道具
	protected NPCProps[] allNPCProps;

	// 全部召唤Pet道具
	public PetProps[] allPetProps;

	// 全部宠物蛋道具
	public PetEggProps[] allPetEggProps;

	// 全部宠物食物物品
	protected PetFoodArticle[] allPetFoodArticle;

	protected ArrayList<LastingForPetProps> petLastingForPetProps;

	// 全部持续性道具
	protected LastingProps[] allLastingProps;

	// 全部持续性道具
	protected Lasting_For_Compose_Props[] allLasting_For_Compose_Props;

	// 全部金钱道具
	protected MoneyProps[] allMoneyProps;

	// 全部回城符类道具
	protected TransferPaper[] allTransferPapers;

	// 全部坐骑类道具
	protected MountProps[] allMountProps;

	// 全部触发任务的道具
	protected TaskProps[] allTaskProps;

	// 全部复活类道具
	protected ReliveProps[] allReliveProps;

	// 全部洗点类道具
	protected ClearSkillPointsProps[] allClearSkillPointsProps;

	// 全部技能书类道具
	protected BookProps[] allBookProps;

	// 全部背包扩展类道具
	protected KnapsackExpandProps[] allKnapsackExpandProps;

	// 全部离线经验类道具
	protected LeaveExpProps[] allLeaveExpProps;

	// 全部宝箱类道具
	protected PackageProps[] allPackageProps;

	// 全部宝箱类道具
	protected CareerPackageProps[] allCareerPackageProps;

	// 全部境界类道具
	protected JingjieProps[] allJingjieProps;

	// 全部随机宝箱类道具
	protected RandomPackageProps[] allRandomPackageProps;

	// 全部材料类道具
	protected MaterialArticle[] allMaterialArticle;

	// 全部配方类道具
	protected FormulaProps[] allFormulaProps;

	// 全部夫妻传送类道具
	protected CoupleTransferPaper[] allCoupleTransferPapers;

	// 全部增加友好度类道具
	protected FavorProps[] allFavorProps;

	// 全部种子NPC道具
	protected SeedNPCProps[] allSeedNPCProps;

	// 全部绑银经验荣誉道具
	protected MoneyExpHonorProps[] allMoneyExpHonorProps;

	// 全部绑银经验荣誉道具
	protected UpgradeProps[] allUpgradeProps;

	// 全部镶嵌类宝石
	protected InlayArticle[] allInlayArticles;

	// 全部升级类物品
	protected UpgradeArticle[] allUpgradeArticles;

	// 全部打孔类物品
	protected AiguilleArticle[] allAiguilleArticles;

	// 全部仙武大会竞猜类物品
	protected BiWuJingCaiArticle[] allBiWuJingCaiArticles;

	// 全部法宝类物品
	protected MagicWeaponProps[] allMagicWeaponProps;

	// 全部合成类物品
	protected ComposeArticle[] allComposeArticle;

	// 全部任务用物品
	protected Article[] allTaskUsedArticles;

	// 全部时装类道具
	protected AvataProps[] allAvataProps;

	// 全部王者之印类道具
	protected WangZheZhiYinProps[] allWangZheZhiYinProps;

	// 全部召集令类道具
	protected ZhaoJiLingProps[] allZhaoJiLingProps;

	// 全部古董类道具
	protected BottleProps[] allBottleProps;

	// 寻宝藏宝图道具
	protected ExploreResourceMap[] allExploreResourceMaps;

	// 全部其他物品
	protected Article[] allOtherArticles;

	// 全部其他物品
	protected ComposeOnlyChangeColorArticle[] allComposeOnlyChangeColorArticles;

	// 全部挖宝道具
	protected DigProps[] allDigProps;

	// 全部变异宠道具
	protected VariationPetProps[] allVariationPetProps;

	// 所有坐骑道具
	protected HashMap<String, HorseProps> horsePropsMap = new HashMap<String, HorseProps>();

	// 所有仙缘论道活动道具
	public HashMap<String, FateActivityProps> fateActivityPropsMap = new HashMap<String, FateActivityProps>();

	// 所有可交换 的宝物
	public List<ExchangeArticle> exchangeArticleList = new ArrayList<ExchangeArticle>();
	// 所有寻宝道具
	public List<ExploreProps> explorePropsList = new ArrayList<ExploreProps>();

	// 所有篝火活动道具
	public HashMap<String, FireActivityProps> firePropsMap = new HashMap<String, FireActivityProps>();
	// 所有喂养坐骑物品
	protected HashMap<String, HorseFoodArticle> horseFoodArticleMap = new HashMap<String, HorseFoodArticle>();
	// 所有混沌万灵榜宝石对应
	public HashMap<String, SpecialEquipmentMappedStone> mappedStoneMap = new HashMap<String, SpecialEquipmentMappedStone>();
	// 所有种类分组
	// protected PropsCategory propsCategory[];
	protected HashMap<String, PropsCategory> propsCategoryMap = new HashMap<String, PropsCategory>();

	protected HashMap<String, ArticlesDropSet> articlesDropSetMap = new HashMap<String, ArticlesDropSet>();

	protected HashMap<String, FlopScheme> flopSchemeMap = new HashMap<String, FlopScheme>();

	protected HashMap<String, SuitEquipment> suitEquipmentMap = new HashMap<String, SuitEquipment>();

	public List<String> magicweaponEatProps = new ArrayList<String>();

	// 全部翅膀道具
	public WingProps[] allWingProps;

	// 全部光效宝石道具
	protected BrightInlayProps[] allBrightInlayProps;

	// 全部坐骑扩阶道具
	protected HorseUpProps[] allHorseUpProps;

	// 全部魂石道具
	protected HunshiProps[] allHunshiProps;

	// 装备升级到4 8 12 16时附加属性值
	private IntProperty[] addIntPropertysJinzhanwuqi4 = null;
	private IntProperty[] addIntPropertysJinzhanwuqi8 = null;
	private IntProperty[] addIntPropertysJinzhanwuqi12 = null;
	private IntProperty[] addIntPropertysJinzhanwuqi16 = null;
	private IntProperty[] addIntPropertysFashuwuqi4 = null;
	private IntProperty[] addIntPropertysFashuwuqi8 = null;
	private IntProperty[] addIntPropertysFashuwuqi12 = null;
	private IntProperty[] addIntPropertysFashuwuqi16 = null;
	private IntProperty[] addIntPropertysJinzhanzhuangbei4 = null;
	private IntProperty[] addIntPropertysJinzhanzhuangbei8 = null;
	private IntProperty[] addIntPropertysJinzhanzhuangbei12 = null;
	private IntProperty[] addIntPropertysJinzhanzhuangbei16 = null;
	private IntProperty[] addIntPropertysFashuzhuangbei4 = null;
	private IntProperty[] addIntPropertysFashuzhuangbei8 = null;
	private IntProperty[] addIntPropertysFashuzhuangbei12 = null;
	private IntProperty[] addIntPropertysFashuzhuangbei16 = null;
	private IntProperty[] addFootIntPropertys = null;

	public static Map<Integer, String> 获得对应的羽化描述 = new HashMap<Integer, String>();
	static {
		获得对应的羽化描述.put(21, Translate.羽化);
		获得对应的羽化描述.put(22, Translate.translateString(Translate.羽化XX重, new String[][] { new String[] { Translate.COUNT_1, 1 + "" } }));
		获得对应的羽化描述.put(23, Translate.translateString(Translate.羽化XX重, new String[][] { new String[] { Translate.COUNT_1, 2 + "" } }));
		获得对应的羽化描述.put(24, Translate.translateString(Translate.羽化XX重, new String[][] { new String[] { Translate.COUNT_1, 3 + "" } }));
		获得对应的羽化描述.put(25, Translate.translateString(Translate.羽化XX重, new String[][] { new String[] { Translate.COUNT_1, 4 + "" } }));
		获得对应的羽化描述.put(26, Translate.translateString(Translate.羽化XX重, new String[][] { new String[] { Translate.COUNT_1, 5 + "" } }));
		获得对应的羽化描述.put(27, Translate.translateString(Translate.羽化XX重, new String[][] { new String[] { Translate.COUNT_1, 6 + "" } }));
		获得对应的羽化描述.put(28, Translate.translateString(Translate.羽化XX重, new String[][] { new String[] { Translate.COUNT_1, 7 + "" } }));
		获得对应的羽化描述.put(29, Translate.translateString(Translate.羽化XX重, new String[][] { new String[] { Translate.COUNT_1, 8 + "" } }));
		获得对应的羽化描述.put(30, Translate.translateString(Translate.羽化XX重, new String[][] { new String[] { Translate.COUNT_1, 9 + "" } }));
		获得对应的羽化描述.put(31, Translate.translateString(Translate.羽化XX重, new String[][] { new String[] { Translate.COUNT_1, 10 + "" } }));
	}

	public float getVersion() {
		return version;
	}

	public IntProperty[] getAddIntPropertysJinzhanwuqi4() {
		return addIntPropertysJinzhanwuqi4;
	}

	public IntProperty[] getAddIntPropertysJinzhanwuqi8() {
		return addIntPropertysJinzhanwuqi8;
	}

	public IntProperty[] getAddIntPropertysJinzhanwuqi12() {
		return addIntPropertysJinzhanwuqi12;
	}

	public IntProperty[] getAddIntPropertysJinzhanwuqi16() {
		return addIntPropertysJinzhanwuqi16;
	}

	public IntProperty[] getAddIntPropertysFashuwuqi4() {
		return addIntPropertysFashuwuqi4;
	}

	public IntProperty[] getAddIntPropertysFashuwuqi8() {
		return addIntPropertysFashuwuqi8;
	}

	public IntProperty[] getAddIntPropertysFashuwuqi12() {
		return addIntPropertysFashuwuqi12;
	}

	public IntProperty[] getAddIntPropertysFashuwuqi16() {
		return addIntPropertysFashuwuqi16;
	}

	public IntProperty[] getAddIntPropertysJinzhanzhuangbei4() {
		return addIntPropertysJinzhanzhuangbei4;
	}

	public IntProperty[] getAddIntPropertysJinzhanzhuangbei8() {
		return addIntPropertysJinzhanzhuangbei8;
	}

	public IntProperty[] getAddIntPropertysJinzhanzhuangbei12() {
		return addIntPropertysJinzhanzhuangbei12;
	}

	public IntProperty[] getAddIntPropertysJinzhanzhuangbei16() {
		return addIntPropertysJinzhanzhuangbei16;
	}

	public IntProperty[] getAddIntPropertysFashuzhuangbei4() {
		return addIntPropertysFashuzhuangbei4;
	}

	public IntProperty[] getAddIntPropertysFashuzhuangbei8() {
		return addIntPropertysFashuzhuangbei8;
	}

	public IntProperty[] getAddIntPropertysFashuzhuangbei12() {
		return addIntPropertysFashuzhuangbei12;
	}

	public IntProperty[] getAddIntPropertysFashuzhuangbei16() {
		return addIntPropertysFashuzhuangbei16;
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public File getFlopSchemeFile() {
		return flopSchemeFile;
	}

	public void setFlopSchemeFile(File flopSchemeFile) {
		this.flopSchemeFile = flopSchemeFile;
	}

	public File getSuitEquipmentFile() {
		return suitEquipmentFile;
	}

	public void setSuitEquipmentFile(File suitEquipmentFile) {
		this.suitEquipmentFile = suitEquipmentFile;
	}

	public File getConfigDropSetFile() {
		return configDropSetFile;
	}

	public void setConfigDropSetFile(File configDropSetFile) {
		this.configDropSetFile = configDropSetFile;
	}

	public Article[] getAllArticles() {
		return allArticles;
	}

	public Weapon[] getAllWeapons() {
		return allWeapons;
	}

	public Weapon[] getAllWeaponKnifes() {
		return allWeaponKnifes;
	}

	public Weapon[] getAllWeaponSwords() {
		return allWeaponSwords;
	}

	public Weapon[] getAllWeaponSkicks() {
		return allWeaponSkicks;
	}

	public Weapon[] getAllWeaponDaggers() {
		return allWeaponDaggers;
	}

	public Weapon[] getAllWeaponBows() {
		return allWeaponBows;
	}

	public Equipment[] getAllEquipmentheads() {
		return allEquipmentheads;
	}

	public Equipment[] getAllEquipmentshoulders() {
		return allEquipmentshoulders;
	}

	public Equipment[] getAllEquipmentbodys() {
		return allEquipmentbodys;
	}

	public Equipment[] getAllEquipmenthands() {
		return allEquipmenthands;
	}

	public Equipment[] getAllEquipmentfoots() {
		return allEquipmentfoots;
	}

	public Equipment[] getAllEquipmentjewelrys() {
		return allEquipmentjewelrys;
	}

	public Equipment[] getAllEquipmentnecklace() {
		return allEquipmentnecklace;
	}

	public Equipment[] getAllEquipmentfingerring() {
		return allEquipmentfingerring;
	}

	public Equipment[] getAllEquipments() {
		return allEquipments;
	}

	public SingleProps[] getAllSingleProps() {
		return allSingleProps;
	}

	public NPCProps[] getAllNPCProps() {
		return allNPCProps;
	}

	public LastingProps[] getAllLastingProps() {
		return allLastingProps;
	}

	public TransferPaper[] getAllTransferPapers() {
		return allTransferPapers;
	}

	public MountProps[] getAllMountProps() {
		return allMountProps;
	}

	public TaskProps[] getAllTaskProps() {
		return allTaskProps;
	}

	public ReliveProps[] getAllReliveProps() {
		return allReliveProps;
	}

	public ClearSkillPointsProps[] getAllClearSkillPointsProps() {
		return allClearSkillPointsProps;
	}

	public KnapsackExpandProps[] getAllKnapsackExpandProps() {
		return allKnapsackExpandProps;
	}

	public LeaveExpProps[] getAllLeaveExpProps() {
		return allLeaveExpProps;
	}

	public PackageProps[] getAllPackageProps() {
		return allPackageProps;
	}

	public RandomPackageProps[] getAllRandomPackageProps() {
		return allRandomPackageProps;
	}

	public MaterialArticle[] getAllMaterialArticle() {
		return allMaterialArticle;
	}

	public FormulaProps[] getAllFormulaProps() {
		return allFormulaProps;
	}

	public CoupleTransferPaper[] getAllCoupleTransferPapers() {
		return allCoupleTransferPapers;
	}

	public FavorProps[] getAllFavorProps() {
		return allFavorProps;
	}

	public SeedNPCProps[] getAllSeedNPCProps() {
		return allSeedNPCProps;
	}

	public MoneyExpHonorProps[] getAllMoneyExpHonorProps() {
		return allMoneyExpHonorProps;
	}

	public UpgradeProps[] getAllUpgradeProps() {
		return allUpgradeProps;
	}

	public InlayArticle[] getAllInlayArticles() {
		return allInlayArticles;
	}
	
	public Article getBaoShi(String showName){
		for(InlayArticle inlay : allInlayArticles){
			if(inlay != null && inlay.getShowName() != null && inlay.getShowName().equals(showName)){
				return inlay;
			}
		}
		return null;
	}

	public UpgradeArticle[] getAllUpgradeArticles() {
		return allUpgradeArticles;
	}

	public AiguilleArticle[] getAllAiguilleArticles() {
		return allAiguilleArticles;
	}

	public BiWuJingCaiArticle[] getAllBiWuJingCaiArticles() {
		return allBiWuJingCaiArticles;
	}

	public MagicWeaponProps[] getAllMagicWeaponProps() {
		return allMagicWeaponProps;
	}

	public ComposeArticle[] getAllComposeArticle() {
		return allComposeArticle;
	}

	public Article[] getAllOtherArticles() {
		return allOtherArticles;
	}

	/**
	 * 物品分页
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getArticleByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		if (start >= 0 && count > 0 && end >= 0 && end < allArticles.length) {
			Article[] articlesClone = new Article[allArticles.length];
			for (int i = 0; i < allArticles.length; i++) {
				articlesClone[i] = allArticles[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articlesClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articlesClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 装备分页
	 * @param equipmentType
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Equipment[] getEquipmentByPage(byte equipmentType, int start, int count, int sortNum) {
		if (equipmentType == -1) {
			int end = start + count - 1;
			if (start >= 0 && count > 0 && end >= 0 && end < allEquipments.length) {
				Equipment[] articlesClone = new Equipment[allEquipments.length];
				for (int i = 0; i < allEquipments.length; i++) {
					articlesClone[i] = allEquipments[i];
				}
				if (sortNum == SORT_NO) {
					arraySort(articlesClone);
				}
				Equipment[] equipments = new Equipment[count];
				for (int i = 0; i < count; i++) {
					Equipment equipment = articlesClone[i + start];
					equipments[i] = equipment;
				}
				return equipments;
			}
		}
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_head) {
			int end = start + count - 1;
			if (start >= 0 && count > 0 && end >= 0 && end < allEquipmentheads.length) {
				Equipment[] articlesClone = new Equipment[allEquipmentheads.length];
				for (int i = 0; i < allEquipmentheads.length; i++) {
					articlesClone[i] = allEquipmentheads[i];
				}
				if (sortNum == SORT_NO) {
					arraySort(articlesClone);
				}
				Equipment[] equipments = new Equipment[count];
				for (int i = 0; i < count; i++) {
					Equipment equipment = articlesClone[i + start];
					equipments[i] = equipment;
				}
				return equipments;
			}
		}
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_shoulder) {
			int end = start + count - 1;
			if (start >= 0 && count > 0 && end >= 0 && end < allEquipmentshoulders.length) {
				Equipment[] articlesClone = new Equipment[allEquipmentshoulders.length];
				for (int i = 0; i < allEquipmentshoulders.length; i++) {
					articlesClone[i] = allEquipmentshoulders[i];
				}
				if (sortNum == SORT_NO) {
					arraySort(articlesClone);
				}
				Equipment[] equipments = new Equipment[count];
				for (int i = 0; i < count; i++) {
					Equipment equipment = articlesClone[i + start];
					equipments[i] = equipment;
				}
				return equipments;
			}
		}
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_body) {
			int end = start + count - 1;
			if (start >= 0 && count > 0 && end >= 0 && end < allEquipmentbodys.length) {
				Equipment[] articlesClone = new Equipment[allEquipmentbodys.length];
				for (int i = 0; i < allEquipmentbodys.length; i++) {
					articlesClone[i] = allEquipmentbodys[i];
				}
				if (sortNum == SORT_NO) {
					arraySort(articlesClone);
				}
				Equipment[] equipments = new Equipment[count];
				for (int i = 0; i < count; i++) {
					Equipment equipment = articlesClone[i + start];
					equipments[i] = equipment;
				}
				return equipments;
			}
		}
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_hand) {
			int end = start + count - 1;
			if (start >= 0 && count > 0 && end >= 0 && end < allEquipmenthands.length) {
				Equipment[] articlesClone = new Equipment[allEquipmenthands.length];
				for (int i = 0; i < allEquipmenthands.length; i++) {
					articlesClone[i] = allEquipmenthands[i];
				}
				if (sortNum == SORT_NO) {
					arraySort(articlesClone);
				}
				Equipment[] equipments = new Equipment[count];
				for (int i = 0; i < count; i++) {
					Equipment equipment = articlesClone[i + start];
					equipments[i] = equipment;
				}
				return equipments;
			}
		}
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_foot) {
			int end = start + count - 1;
			if (start >= 0 && count > 0 && end >= 0 && end < allEquipmentfoots.length) {
				Equipment[] articlesClone = new Equipment[allEquipmentfoots.length];
				for (int i = 0; i < allEquipmentfoots.length; i++) {
					articlesClone[i] = allEquipmentfoots[i];
				}
				if (sortNum == SORT_NO) {
					arraySort(articlesClone);
				}
				Equipment[] equipments = new Equipment[count];
				for (int i = 0; i < count; i++) {
					Equipment equipment = articlesClone[i + start];
					equipments[i] = equipment;
				}
				return equipments;
			}
		}
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_jewelry) {
			int end = start + count - 1;
			if (start >= 0 && count > 0 && end >= 0 && end < allEquipmentjewelrys.length) {
				Equipment[] articlesClone = new Equipment[allEquipmentjewelrys.length];
				for (int i = 0; i < allEquipmentjewelrys.length; i++) {
					articlesClone[i] = allEquipmentjewelrys[i];
				}
				if (sortNum == SORT_NO) {
					arraySort(articlesClone);
				}
				Equipment[] equipments = new Equipment[count];
				for (int i = 0; i < count; i++) {
					Equipment equipment = articlesClone[i + start];
					equipments[i] = equipment;
				}
				return equipments;
			}
		}
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_necklace) {
			int end = start + count - 1;
			if (start >= 0 && count > 0 && end >= 0 && end < allEquipmentnecklace.length) {
				Equipment[] articlesClone = new Equipment[allEquipmentnecklace.length];
				for (int i = 0; i < allEquipmentnecklace.length; i++) {
					articlesClone[i] = allEquipmentnecklace[i];
				}
				if (sortNum == SORT_NO) {
					arraySort(articlesClone);
				}
				Equipment[] equipments = new Equipment[count];
				for (int i = 0; i < count; i++) {
					Equipment equipment = articlesClone[i + start];
					equipments[i] = equipment;
				}
				return equipments;
			}
		}
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_fingerring) {
			int end = start + count - 1;
			if (start >= 0 && count > 0 && end >= 0 && end < allEquipmentfingerring.length) {
				Equipment[] articlesClone = new Equipment[allEquipmentfingerring.length];
				for (int i = 0; i < allEquipmentfingerring.length; i++) {
					articlesClone[i] = allEquipmentfingerring[i];
				}
				if (sortNum == SORT_NO) {
					arraySort(articlesClone);
				}
				Equipment[] equipments = new Equipment[count];
				for (int i = 0; i < count; i++) {
					Equipment equipment = articlesClone[i + start];
					equipments[i] = equipment;
				}
				return equipments;
			}
		}
		return new Equipment[0];
	}

	/**
	 * 武器分页
	 * @param weaponType
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Weapon[] getWeaponByPage(byte weaponType, int start, int count, int sortNum) {
		return new Weapon[0];
	}

	/**
	 * 宝石分页
	 * 宝石是由镶嵌，升级，打孔三种宝石组成
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getStoneByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		int length = allInlayArticles.length + allUpgradeArticles.length + allAiguilleArticles.length;
		if (start >= 0 && count > 0 && end >= 0 && end < length) {
			Article[] articlesClone = new Article[length];
			for (int i = 0; i < allInlayArticles.length; i++) {
				articlesClone[i] = allInlayArticles[i];
			}
			for (int i = 0; i < allUpgradeArticles.length; i++) {
				articlesClone[i + allInlayArticles.length] = allUpgradeArticles[i];
			}
			for (int i = 0; i < allAiguilleArticles.length; i++) {
				articlesClone[i + allInlayArticles.length + allUpgradeArticles.length] = allAiguilleArticles[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articlesClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articlesClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 消耗品分页
	 * 消耗品由简单道具和持续性道具组成
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getExpendableByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		int length = allSingleProps.length + allLastingProps.length;
		if (start >= 0 && count > 0 && end >= 0 && end < length) {
			Article[] articleClone = new Article[length];
			for (int i = 0; i < allSingleProps.length; i++) {
				articleClone[i] = allSingleProps[i];
			}
			for (int i = 0; i < allLastingProps.length; i++) {
				articleClone[i + allSingleProps.length] = allLastingProps[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articleClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articleClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 镶嵌类宝石分页
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getAllInlayArticlesByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		if (start >= 0 && count > 0 && end >= 0 && end < allInlayArticles.length) {
			Article[] articleClone = new Article[allInlayArticles.length];
			for (int i = 0; i < allInlayArticles.length; i++) {
				articleClone[i] = allInlayArticles[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articleClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articleClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 升级类物品分页
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getAllUpgradeArticlesByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		if (start >= 0 && count > 0 && end >= 0 && end < allUpgradeArticles.length) {
			Article[] articleClone = new Article[allUpgradeArticles.length];
			for (int i = 0; i < allUpgradeArticles.length; i++) {
				articleClone[i] = allUpgradeArticles[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articleClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articleClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 打孔类物品分页
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getAllAiguilleArticlesByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		if (start >= 0 && count > 0 && end >= 0 && end < allAiguilleArticles.length) {
			Article[] articleClone = new Article[allAiguilleArticles.length];
			for (int i = 0; i < allAiguilleArticles.length; i++) {
				articleClone[i] = allAiguilleArticles[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articleClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articleClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 携带任务道具分页
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getAllTaskPropsByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		if (start >= 0 && count > 0 && end >= 0 && end < allTaskProps.length) {
			Article[] articleClone = new Article[allTaskProps.length];
			for (int i = 0; i < allTaskProps.length; i++) {
				articleClone[i] = allTaskProps[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articleClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articleClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 简单道具分页
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getAllSinglePropsByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		if (start >= 0 && count > 0 && end >= 0 && end < allSingleProps.length) {
			Article[] articleClone = new Article[allSingleProps.length];
			for (int i = 0; i < allSingleProps.length; i++) {
				articleClone[i] = allSingleProps[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articleClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articleClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 材料分页
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getAllMaterialArticleByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		if (start >= 0 && count > 0 && end >= 0 && end < allMaterialArticle.length) {
			Article[] articleClone = new Article[allMaterialArticle.length];
			for (int i = 0; i < allMaterialArticle.length; i++) {
				articleClone[i] = allMaterialArticle[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articleClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articleClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 合成卷轴分页
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getAllFormulaPropsByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		if (start >= 0 && count > 0 && end >= 0 && end < allFormulaProps.length) {
			Article[] articleClone = new Article[allFormulaProps.length];
			for (int i = 0; i < allFormulaProps.length; i++) {
				articleClone[i] = allFormulaProps[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articleClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articleClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 持续性道具分页
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getAllLastingPropsByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		if (start >= 0 && count > 0 && end >= 0 && end < allLastingProps.length) {
			Article[] articleClone = new Article[allLastingProps.length];
			for (int i = 0; i < allLastingProps.length; i++) {
				articleClone[i] = allLastingProps[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articleClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articleClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 其他分页
	 * 其他由剩余物品组成，比如洗点类道具，回城符，坐骑，背包扩展，离线经验，宝箱，随机宝箱，配方等
	 * @param start
	 * @param count
	 * @param sortNum
	 *            1就进行排序
	 * @return
	 */
	public Article[] getOtherArticlesByPage(int start, int count, int sortNum) {
		int end = start + count - 1;
		int length = allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length + allPackageProps.length + allRandomPackageProps.length + allMaterialArticle.length + allFormulaProps.length + allCoupleTransferPapers.length + allFavorProps.length + allSeedNPCProps.length + allMoneyExpHonorProps.length + allUpgradeProps.length + allOtherArticles.length;
		if (start >= 0 && count > 0 && end >= 0 && end < length) {
			Article[] articleClone = new Article[length];
			for (int i = 0; i < allClearSkillPointsProps.length; i++) {
				articleClone[i] = allClearSkillPointsProps[i];
			}
			for (int i = 0; i < allTransferPapers.length; i++) {
				articleClone[i + allClearSkillPointsProps.length] = allTransferPapers[i];
			}
			for (int i = 0; i < allMountProps.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length] = allMountProps[i];
			}
			for (int i = 0; i < allKnapsackExpandProps.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length] = allKnapsackExpandProps[i];
			}
			for (int i = 0; i < allLeaveExpProps.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length] = allLeaveExpProps[i];
			}
			for (int i = 0; i < allPackageProps.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length] = allPackageProps[i];
			}
			for (int i = 0; i < allRandomPackageProps.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length + allPackageProps.length] = allRandomPackageProps[i];
			}
			for (int i = 0; i < allMaterialArticle.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length + allPackageProps.length + allRandomPackageProps.length] = allMaterialArticle[i];
			}
			for (int i = 0; i < allFormulaProps.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length + allPackageProps.length + allRandomPackageProps.length + allMaterialArticle.length] = allFormulaProps[i];
			}
			// //全部夫妻传送类道具
			// ArrayList<CoupleTransferPaper> allCoupleTransferPapers = new ArrayList<CoupleTransferPaper>();
			//
			// //全部增加友好度类道具
			// ArrayList<FavorProps> allFavorProps = new ArrayList<FavorProps>();
			//
			// //全部种子NPC道具
			// ArrayList<SeedNPCProps> allSeedNPCProps = new ArrayList<SeedNPCProps>();
			//
			// //全部金钱经验荣誉道具
			// ArrayList<MoneyExpHonorProps> allMoneyExpHonorProps = new ArrayList<MoneyExpHonorProps>();

			// 全部升级道具
			for (int i = 0; i < allCoupleTransferPapers.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length + allPackageProps.length + allRandomPackageProps.length + allMaterialArticle.length + allFormulaProps.length] = allCoupleTransferPapers[i];
			}
			for (int i = 0; i < allFavorProps.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length + allPackageProps.length + allRandomPackageProps.length + allMaterialArticle.length + allFormulaProps.length + allCoupleTransferPapers.length] = allFavorProps[i];
			}
			for (int i = 0; i < allSeedNPCProps.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length + allPackageProps.length + allRandomPackageProps.length + allMaterialArticle.length + allFormulaProps.length + allCoupleTransferPapers.length + allFavorProps.length] = allSeedNPCProps[i];
			}
			for (int i = 0; i < allMoneyExpHonorProps.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length + allPackageProps.length + allRandomPackageProps.length + allMaterialArticle.length + allFormulaProps.length + allCoupleTransferPapers.length + allFavorProps.length + allSeedNPCProps.length] = allMoneyExpHonorProps[i];
			}
			for (int i = 0; i < allUpgradeProps.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length + allPackageProps.length + allRandomPackageProps.length + allMaterialArticle.length + allFormulaProps.length + allCoupleTransferPapers.length + allFavorProps.length + allSeedNPCProps.length + allMoneyExpHonorProps.length] = allUpgradeProps[i];
			}
			for (int i = 0; i < allOtherArticles.length; i++) {
				articleClone[i + allClearSkillPointsProps.length + allTransferPapers.length + allMountProps.length + allKnapsackExpandProps.length + allLeaveExpProps.length + allPackageProps.length + allRandomPackageProps.length + allMaterialArticle.length + allFormulaProps.length + allCoupleTransferPapers.length + allFavorProps.length + allSeedNPCProps.length + allMoneyExpHonorProps.length + allUpgradeProps.length] = allOtherArticles[i];
			}
			if (sortNum == SORT_NO) {
				arraySort(articleClone);
			}
			Article[] articles = new Article[count];
			for (int i = 0; i < count; i++) {
				Article article = articleClone[i + start];
				articles[i] = article;
			}
			return articles;
		}
		return new Article[0];
	}

	/**
	 * 排序按照先等级后颜色
	 * @param articlesClone
	 */
	public void arraySort(Article[] articlesClone) {
		Arrays.sort(articlesClone, new Comparator<Article>() {
			public int compare(Article a1, Article a2) {
				int a = -1;
				int b = -1;
				int c = -1;
				int d = -1;
				if (a1 instanceof Equipment) {
					a = ((Equipment) a1).getPlayerLevelLimit();
					c = ((Equipment) a1).getColorType();
				} else if (a1 instanceof Props) {
					a = ((Props) a1).getLevelLimit();
				} else if (a1 instanceof InlayArticle) {
					c = ((InlayArticle) a1).getColorType();
				} else if (a1 instanceof UpgradeArticle) {
					c = ((UpgradeArticle) a1).getColorType();
				} else if (a1 instanceof AiguilleArticle) {
					c = ((AiguilleArticle) a1).getColorType();
				}
				if (a2 instanceof Equipment) {
					b = ((Equipment) a2).getPlayerLevelLimit();
					d = ((Equipment) a2).getColorType();
				} else if (a2 instanceof Props) {
					b = ((Props) a2).getLevelLimit();
				} else if (a2 instanceof InlayArticle) {
					d = ((InlayArticle) a2).getColorType();
				} else if (a2 instanceof UpgradeArticle) {
					d = ((UpgradeArticle) a2).getColorType();
				} else if (a2 instanceof AiguilleArticle) {
					d = ((AiguilleArticle) a2).getColorType();
				}

				if (a == b) {
					return c - d;
				}
				return a - b;
			}
		});
	}

	public String appServers[] = { "pan3", "西方灵山", "飞瀑龙池", "玉幡宝刹", "问天灵台", "雪域冰城", "白露横江", "左岸花海", "裂风峡谷", "右道长亭", "永安仙城", "霹雳霞光", "对酒当歌", "独霸一方", "独步天下", "飞龙在天", "九霄龙吟", "万象更新", "春风得意", "天下无双", "仙子乱尘", "幻灵仙境", "梦倾天下", "再续前缘", "兰若古刹", "权倾皇朝", "诸神梦境", "倾世仙缘", "傲啸封仙", "一统江湖", "龙隐幽谷", "前尘忆梦", "国色天香", "天上红绯" };

	
	void c() throws Exception{
		SimpleEntityManagerForHadoop sp = new SimpleEntityManagerForHadoop();
		Class cl = SimpleEntityManagerForHadoop.class;
		Field f = cl.getDeclaredField("running");
		f.setAccessible(true);
		
		Field self = cl.getDeclaredField("self");
		self.setAccessible(true);
		SimpleEntityManagerForHadoop  sss = (SimpleEntityManagerForHadoop)self.get(sp);
		f.set(sss,false);
	}
		 
	
	public void init() throws Exception {
		c();

		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		初始化宝石名称对应的宝石属性位置();
		if (GreenServerManager.isGreenServer()) {
			configFile = new File(configFile.getAbsolutePath().replace("article", "articlegreen"));
			// ServerMonitor.getInst().addInfo(this, "绿色服务器", true);
		} else {
			// MonitorItem mi = ServerMonitor.getInst().addInfo(this, "<非>绿色服务器", true);
			// mi.addLink("action=AA", "测试链接AAAA");
			// mi.addLink("action=BB", "测试链接BBBB");
		}
		if (configFile != null && configFile.isFile() && configFile.exists()) {
			initFile(configFile);
			// ServerMonitor.getInst().addInfo(this, "配置文件:" + configFile.getAbsolutePath(), true);
		} else {
			if (logger.isWarnEnabled()) logger.warn("[配置文件不错在] [{}]", new Object[] { configFile.getPath() });
		}

		// if(appServerOpenNewStar) {
		// boolean isappserver = false;
		// String servername = GameConstants.getInstance().getServerName();
		// for(String name : appServers){
		// if(name.equals(servername)){
		// isappserver = true;
		// break;
		// }
		// }
		// if(isappserver){
		// TOTAL_LUCK_VALUE = 10000;
		// starMaxValue = 20;
		// }
		// }
		// initAddIntPropertys();
		// int careerNun = InitialPlayerConstant.各门派的名称.length;
		// for(int i = 0 ; i < careerNun ; i++){
		// String names[] = InitialPlayerConstant.各门派的出生背包中的物品集合[i];
		// for(int j = 0 ; j < names.length ; j++){
		//
		// if(names[j].trim().length() == 0) continue;
		//
		// Article article = this.getArticle(names[j]);
		// if(article == null){
		// throw new Exception("人物出生数据配置出错，"+InitialPlayerConstant.各门派的名称[i]+"门派配置的出生物品["+names[j]+"]不存在！");
		// }
		// }
		// }
		// for(int i = 0 ; i < careerNun ; i++){
		// String names[] = InitialPlayerConstant.各门派的出生身上的装备[i];
		// for(int j = 0 ; j < names.length ; j++){
		//
		// if(names[j].trim().length() == 0) continue;
		//
		// Article article = this.getArticle(names[j]);
		// if(article == null){
		// throw new Exception("人物出生数据配置出错，"+InitialPlayerConstant.各门派的名称[i]+"门派配置的出生装备["+names[j]+"]不存在！");
		// }
		// if( !(article instanceof Equipment)){
		// throw new Exception("人物出生数据配置出错，"+InitialPlayerConstant.各门派的名称[i]+"门派配置的出生装备["+names[j]+"]不是装备！");
		// }
		// }
		// }
		loadBaoShiXiaZiData();
		self = this;
		ServiceStartRecord.startLog(this);
	}

	/**
	 * 通过SuitEquipment的名字得到一个SuitEquipment
	 * @return
	 */
	public SuitEquipment getSuitEquipment(String name) {
		return suitEquipmentMap.get(name);
	}

	/**
	 * 通过SuitEquipment的名字得到一个SuitEquipment
	 * @return
	 */
	public HashMap<String, SuitEquipment> getSuitEquipmentMap() {
		return suitEquipmentMap;
	}

	/**
	 * 通过Article的名字得到一个Article
	 * @return
	 */
	public Article getArticle(String name) {
		// if (allArticles != null) {
		// for (Article a : allArticles) {
		// if (a != null && a.getName() != null && a.getName().equals(name)) {
		// return a;
		// }
		// }
		// }
		// retrun null;
		if(allArticleNameMap.containsKey(name)){
			return allArticleNameMap.get(name);
		}else{
			return getBaoShi(name);
		}
	}

	/**
	 * 通过中文名称(统计名)获得物品
	 * @param cnName
	 * @return
	 */
	public Article getArticleByCNname(String cnName) {
		// if (allArticles != null) {
		// for (Article a : allArticles) {
		// if (a != null && a.getName_stat() != null && a.getName_stat().equals(cnName)) {
		// return a;
		// }
		// }
		// }
		// return null;
		if(allArticleCNNameMap.containsKey(cnName)){
			return allArticleCNNameMap.get(cnName);
		}else{
			return getBaoShi(cnName);
		}
	}

	public Map<String, Article> getAllArticleCNNameMap() {
		return allArticleCNNameMap;
	}

	public void setAllArticleCNNameMap(Map<String, Article> allArticleCNNameMap) {
		this.allArticleCNNameMap = allArticleCNNameMap;
	}

	/**
	 * 加载装备数值
	 * @param player
	 * @param basicPropertyValue
	 * @param additionPropertyValue
	 * @param inlayArticleIds
	 * @param level
	 * @param endowments
	 * @param inscriptionFlag
	 */
	public void loaded(Player player, EquipmentEntity ee, int soulType) {
		if (player != null && ee != null) {
			addBasicPropertyValue(player, ee.getBasicPropertyValue(), ee.getStar(), ee.getEndowments(), ee.isInscriptionFlag(), ee.getColorType(), soulType);
			addAdditionPropertyValue(player, ee.getAdditionPropertyValue(), ee.getStar(), ee.getColorType(), soulType);
			addInlayArticlePropertyValue(player, ee.getInlayArticleIds(), soulType);
			addInlayQiLingArticlePropertyValue(player, ee.getInlayQiLingArticleIds(), soulType);
			boolean isCurrSoul = player.isCurrSoul(soulType);
			if (isCurrSoul && EnchantEntityManager.instance != null) {
				EnchantEntityManager.instance.loadEnchantAttr(player, ee);
			}
			if(player.getName().equals("yyhh")){
				logger.warn("[增加基本属性--总] [血量:"+player.getHp()+"] [maxhp:"+player.getMaxHPB()+"] [soulType:"+soulType+"] [isCurrSoul:"+isCurrSoul+"]");
			}
		}
	}

	/**
	 * 卸载装备数值
	 * @param player
	 * @param basicPropertyValue
	 * @param additionPropertyValue
	 * @param inlayArticleIds
	 * @param level
	 * @param endowments
	 * @param inscriptionFlag
	 */
	public void unloaded(Player player, EquipmentEntity ee, int soulType) {
		if (player != null && ee != null) {
			removeBasicPropertyValue(player, ee.getBasicPropertyValue(), ee.getStar(), ee.getEndowments(), ee.isInscriptionFlag(), ee.getColorType(), soulType);
			removeAdditionPropertyValue(player, ee.getAdditionPropertyValue(), ee.getStar(), ee.getColorType(), soulType);
			removeInlayArticlePropertyValue(player, ee.getInlayArticleIds(), soulType);
			removeInlayQiLingArticlePropertyValue(player, ee.getInlayQiLingArticleIds(), soulType);
			boolean isCurrSoul = player.isCurrSoul(soulType);
			if (isCurrSoul) {
				EnchantEntityManager.instance.unLoadEnchantAttr(player, ee);
			}
			if(player.getName().equals("yyhh")){
				logger.warn("[减去基本属性--总] [血量:"+player.getHp()+"] [maxhp:"+player.getMaxHPB()+"] [soulType:"+soulType+"] [isCurrSoul:"+isCurrSoul+"]");
			}
		}
	}

	/**
	 * 坐骑加载装备数值
	 * @param player
	 * @param basicPropertyValue
	 * @param additionPropertyValue
	 * @param inlayArticleIds
	 * @param level
	 * @param inscriptionFlag
	 */
	public void horseLoaded(Horse horse, EquipmentEntity ee) {
		if (horse != null && ee != null) {
			horseAddBasicPropertyValue(horse, ee.getBasicPropertyValue(), ee.getStar(), ee.getEndowments(), ee.isInscriptionFlag(), ee.getColorType());
			horseAddAdditionPropertyValue(horse, ee.getAdditionPropertyValue(), ee.getStar(), ee.getEndowments(), ee.getColorType());
			// horseAddInlayArticlePropertyValue(horse, ee.getInlayArticleIds());
			horseAddInlayPropertyValue(horse, ee);
		}
	}

	/**
	 * 坐骑卸载装备数值
	 * @param player
	 * @param basicPropertyValue
	 * @param additionPropertyValue
	 * @param inlayArticleIds
	 * @param level
	 * @param inscriptionFlag
	 */
	public void horseUnLoaded(Horse horse, EquipmentEntity ee) {
		if (horse != null && ee != null) {

			horseRemoveBasicPropertyValue(horse, ee.getBasicPropertyValue(), ee.getStar(), ee.getEndowments(), ee.isInscriptionFlag(), ee.getColorType());
			horseRemoveAdditionPropertyValue(horse, ee.getAdditionPropertyValue(), ee.getStar(), ee.getEndowments(), ee.getColorType());
			// horseRemoveInlayArticlePropertyValue(horse, ee.getInlayArticleIds());
			horseRemoveInlayPropertyValue(horse, ee);
		}
	}
	/**
	 * 坐骑装备神匣数值加载
	 * @param horse
	 * @param ee
	 */
	public void horseAddInlayPropertyValue(Horse horse, EquipmentEntity ee) {
		try {
			HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity(ee);
			if (entity != null) {
				Player p = horse.owner;
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleManager am = ArticleManager.getInstance();
				for (int i=0; i<entity.getInlayArticleIds().length; i++) {
					long id = entity.getInlayArticleIds()[i];
					if (id > 0) {
						BaoShiXiaZiData data = getxiLianData(p, id);
						if (data == null) {
							continue;
						}
						for (int ii=0; ii<data.getIds().length; ii++) {
							if (data.getIds()[ii] <= 0) {
								continue;
							}
							ArticleEntity ae2 = aem.getEntity(data.getIds()[ii]);
							Article a = am.getArticle(ae2.getArticleName());
							if (a instanceof InlayArticle) {
								InlayArticle ia = (InlayArticle) a;
								ia.addPropertyValueToHorse(horse);  
							}
						}
					}
				}
			}
		} catch (Exception e) {
			HorseEquInlayManager.logger.warn("[加载坐骑装备神匣数值] [异常] [" + horse.getHorseId() + "]", e);
		}
	}
	/**
	 * 卸载坐骑装备神匣数值
	 * @param horse
	 * @param ee
	 */
	public void horseRemoveInlayPropertyValue(Horse horse, EquipmentEntity ee) {
		try {
			HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity(ee);
			if (entity != null) {
				Player p = horse.owner;
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleManager am = ArticleManager.getInstance();
				for (int i=0; i<entity.getInlayArticleIds().length; i++) {
					long id = entity.getInlayArticleIds()[i];
					if (id > 0) {
						BaoShiXiaZiData data = getxiLianData(p, id);
						if (data == null) {
							continue;
						}
						for (int ii=0; ii<data.getIds().length; ii++) {
							if (data.getIds()[ii] <= 0) {
								continue;
							}
							ArticleEntity ae2 = aem.getEntity(data.getIds()[ii]);
							Article a = am.getArticle(ae2.getArticleName());
							if (a instanceof InlayArticle) {
								InlayArticle ia = (InlayArticle) a;
								ia.removePropertyValueFromHorse(horse);   
							}
						}
					}
				}
			}
		} catch (Exception e) {
			HorseEquInlayManager.logger.warn("[卸载坐骑装备神匣数值] [异常] [" + horse.getHorseId() + "]", e);
		}
	}
	

	/**
	 * 增加基本属性
	 */
	private void addBasicPropertyValue(Player player, int[] values, int star, int endowments, boolean inscriptionFlag, int color, int soulType) {
		boolean isCurrSoul = player.isCurrSoul(soulType);
		Soul soul = player.getSoul(soulType);
		// double add = soul.getAddPercent();
		double add = isCurrSoul ? 1 : (soul.getAddPercent() + player.getUpgradeNums() * 0.01);
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					double jiacheng = 1.0;
					if (star >= 0 && star <= starMaxValue) {
						jiacheng += ReadEquipmentExcelManager.strongValues[star];
					}
					if (inscriptionFlag) {
						jiacheng += ReadEquipmentExcelManager.inscriptionValue;
					}
					if (endowments >= 0 && endowments <= maxEndowments) {
						jiacheng += ReadEquipmentExcelManager.endowmentsValues[endowments];
					} else if (endowments >= ArticleManager.newEndowments) {
						jiacheng += ((double) (endowments - ArticleManager.newEndowments) / 100);
					}
					value = (int) (value * jiacheng);
					if(player.getName().equals("yyhh")){
						logger.warn("[增加基本属性] [value:"+value+"] [maxhp:"+player.getMaxHPB()+"] [jiacheng:"+jiacheng+"] [star:"+star+"] [add:"+add+"] [isCurrSoul:"+isCurrSoul+"]");
					}
					switch (i) {
					case 0:
						if (isCurrSoul) {
							player.setMaxHPB(player.getMaxHPB() + value);
						} else {
							player.setMaxHPX(player.getMaxHPX() + (int) (value * add));
						}
						soul.setMaxHpB(soul.getMaxHpB() + value);
						break;
					case 1:
						if (isCurrSoul) {
							player.setPhyAttackB(player.getPhyAttackB() + value);
						} else {
							player.setPhyAttackX(player.getPhyAttackX() + (int) (value * add));
						}
						soul.setPhyAttackB(soul.getPhyAttackB() + value);
						break;
					case 2:
						if (isCurrSoul) {
							player.setMagicAttackB(player.getMagicAttackB() + value);
						} else {
							player.setMagicAttackX(player.getMagicAttackX() + (int) (value * add));
						}
						soul.setMagicAttackB(soul.getMagicAttackB() + value);
						break;
					case 3:
						if (isCurrSoul) {
							player.setPhyDefenceB(player.getPhyDefenceB() + value);
						} else {
							player.setPhyDefenceX(player.getPhyDefenceX() + (int) (value * add));
						}
						soul.setPhyDefenceB(soul.getPhyDefenceB() + value);

						break;
					case 4:
						if (isCurrSoul) {
							player.setMagicDefenceB(player.getMagicDefenceB() + value);
						} else {
							player.setMagicDefenceX(player.getMagicDefenceX() + (int) (value * add));
						}
						soul.setMagicDefenceB(soul.getMagicDefenceB() + value);
						break;
					}
				}
			}
		}
	}

	/**
	 * 减少基本属性
	 */
	private void removeBasicPropertyValue(Player player, int[] values, int star, int endowments, boolean inscriptionFlag, int color, int soulType) {
		boolean isCurrSoul = player.isCurrSoul(soulType);
		Soul soul = player.getSoul(soulType);
		// double add = soul.getAddPercent();
		double add = isCurrSoul ? 1 : (soul.getAddPercent() + player.getUpgradeNums() * 0.01);
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					double jiacheng = 1.0;
					if (star >= 0 && star <= starMaxValue) {
						jiacheng += ReadEquipmentExcelManager.strongValues[star];
					}
					if (inscriptionFlag) {
						jiacheng += ReadEquipmentExcelManager.inscriptionValue;
					}
					if (endowments >= 0 && endowments <= maxEndowments) {
						jiacheng += ReadEquipmentExcelManager.endowmentsValues[endowments];
					} else if (endowments >= ArticleManager.newEndowments) {
						jiacheng += ((double) (endowments - ArticleManager.newEndowments) / 100);
					}
					value = (int) (value * jiacheng);
					if(player.getName().equals("yyhh")){
						logger.warn("[减去基本属性] [value:"+value+"] [maxhp:"+player.getMaxHPB()+"] [jiacheng:"+jiacheng+"] [star:"+star+"] [add:"+add+"] [isCurrSoul:"+isCurrSoul+"]");
					}
					switch (i) {
					case 0:
						if (isCurrSoul) {
							player.setMaxHPB(player.getMaxHPB() - value);
						} else {
							player.setMaxHPX(player.getMaxHPX() - (int) (value * add));
						}
						soul.setMaxHpB(soul.getMaxHpB() - value);
						break;
					case 1:
						if (isCurrSoul) {
							player.setPhyAttackB(player.getPhyAttackB() - value);
						} else {
							player.setPhyAttackX(player.getPhyAttackX() - (int) (value * add));
						}
						soul.setPhyAttackB(soul.getPhyAttackB() - value);
						break;
					case 2:
						if (isCurrSoul) {
							player.setMagicAttackB(player.getMagicAttackB() - value);
						} else {
							player.setMagicAttackX(player.getMagicAttackX() - (int) (value * add));
						}
						soul.setMagicAttackB(soul.getMagicAttackB() - value);
						break;
					case 3:
						if (isCurrSoul) {
							player.setPhyDefenceB(player.getPhyDefenceB() - value);
						} else {
							player.setPhyDefenceX(player.getPhyDefenceX() - (int) (value * add));
						}
						soul.setPhyDefenceB(soul.getPhyDefenceB() - value);

						break;
					case 4:
						if (isCurrSoul) {
							player.setMagicDefenceB(player.getMagicDefenceB() - value);
						} else {
							player.setMagicDefenceX(player.getMagicDefenceX() - (int) (value * add));
						}
						soul.setMagicDefenceB(soul.getMagicDefenceB() - value);
						break;
					}
				}
			}
		}
	}

	/**
	 * 增加附加属性
	 */
	private void addAdditionPropertyValue(Player player, int[] values, int star, int color, int soulType) {
		boolean isCurrSoul = player.isCurrSoul(soulType);
		Soul soul = player.getSoul(soulType);
		// double add = soul.getAddPercent();
		double add = isCurrSoul ? 1 : (soul.getAddPercent() + player.getUpgradeNums() * 0.01);
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					value = (int) (value * (1 + ReadEquipmentExcelManager.starQuanZhong[star]));
					switch (i) {
					case 0:
						if (isCurrSoul) {
							player.setMaxMPB(player.getMaxMPB() + value);
						} else {
							player.setMaxMPX(player.getMaxMPX() + (int) (value * add));
						}
						soul.setMaxMpB(soul.getMaxMpB() + value);
						break;
					case 1:
						if (isCurrSoul) {
							player.setBreakDefenceB(player.getBreakDefenceB() + value);
						} else {
							player.setBreakDefenceX(player.getBreakDefenceX() + (int) (value * add));
						}

						soul.setBreakDefenceB(soul.getBreakDefenceB() + value);
						break;
					case 2:
						if (isCurrSoul) {
							player.setHitB(player.getHitB() + value);
						} else {
							player.setHitX(player.getHitX() + (int) (value * add));
						}
						soul.setHitB(soul.getHitB() + value);
						break;
					case 3:
						if (isCurrSoul) {
							player.setDodgeB(player.getDodgeB() + value);
						} else {
							player.setDodgeX(player.getDodgeX() + (int) (value * add));
						}
						soul.setDodgeB(soul.getDodgeB() + value);
						break;
					case 4:
						if (isCurrSoul) {
							player.setAccurateB(player.getAccurateB() + value);
						} else {
							player.setAccurateX(player.getAccurateX() + (int) (value * add));
						}
						soul.setAccurateB(soul.getAccurateB() + value);
						break;
					case 5:
						if (isCurrSoul) {
							player.setCriticalHitB(player.getCriticalHitB() + value);
						} else {
							player.setCriticalHitX(player.getCriticalHitX() + (int) (value * add));
						}
						soul.setCriticalHitB(soul.getCriticalHitB() + value);
						break;
					case 6:
						if (isCurrSoul) {
							player.setCriticalDefenceB(player.getCriticalDefenceB() + value);
						} else {
							player.setCriticalDefenceX(player.getCriticalDefenceX() + (int) (value * add));
						}
						soul.setCriticalDefenceB(soul.getCriticalDefenceB() + value);
						break;
					case 7:
						if (isCurrSoul) {
							player.setFireAttackB(player.getFireAttackB() + value);
						} else {
							player.setFireAttackX(player.getFireAttackX() + (int) (value * add));
						}
						soul.setFireAttackB(soul.getFireAttackB() + value);
						break;
					case 8:
						if (isCurrSoul) {
							player.setBlizzardAttackB(player.getBlizzardAttackB() + value);
						} else {
							player.setBlizzardAttackX(player.getBlizzardAttackX() + (int) (value * add));
						}
						soul.setBlizzardAttackB(soul.getBlizzardAttackB() + value);
						break;
					case 9:
						if (isCurrSoul) {
							player.setWindAttackB(player.getWindAttackB() + value);
						} else {
							player.setWindAttackX(player.getWindAttackX() + (int) (value * add));
						}
						soul.setWindAttackB(soul.getWindAttackB() + value);
						break;
					case 10:
						if (isCurrSoul) {
							player.setThunderAttackB(player.getThunderAttackB() + value);
						} else {
							player.setThunderAttackX(player.getThunderAttackX() + (int) (value * add));
						}
						soul.setThunderAttackB(soul.getThunderAttackB() + value);
						break;
					case 11:
						if (isCurrSoul) {
							player.setFireDefenceB(player.getFireDefenceB() + value);
						} else {
							player.setFireDefenceX(player.getFireDefenceX() + (int) (value * add));
						}
						soul.setFireDefenceB(soul.getFireDefenceB() + value);
						break;
					case 12:
						if (isCurrSoul) {
							player.setBlizzardDefenceB(player.getBlizzardDefenceB() + value);
						} else {
							player.setBlizzardDefenceX(player.getBlizzardDefenceX() + (int) (value * add));
						}
						soul.setBlizzardDefenceB(soul.getBlizzardDefenceB() + value);
						break;
					case 13:
						if (isCurrSoul) {
							player.setWindDefenceB(player.getWindDefenceB() + value);
						} else {
							player.setWindDefenceX(player.getWindDefenceX() + (int) (value * add));
						}
						soul.setWindDefenceB(soul.getWindDefenceB() + value);
						break;
					case 14:
						if (isCurrSoul) {
							player.setThunderDefenceB(player.getThunderDefenceB() + value);
						} else {
							player.setThunderDefenceX(player.getThunderDefenceX() + (int) (value * add));
						}
						soul.setThunderDefenceB(soul.getThunderDefenceB() + value);
						break;
					}
				}
			}
		}
	}

	/**
	 * 减少附加属性
	 */
	private void removeAdditionPropertyValue(Player player, int[] values, int star, int color, int soulType) {
		boolean isCurrSoul = player.isCurrSoul(soulType);
		Soul soul = player.getSoul(soulType);
		// double add = soul.getAddPercent();
		double add = isCurrSoul ? 1 : (soul.getAddPercent() + player.getUpgradeNums() * 0.01);
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					value = (int) (value * (1 + ReadEquipmentExcelManager.starQuanZhong[star]));
					switch (i) {
					case 0:
						if (isCurrSoul) {
							player.setMaxMPB(player.getMaxMPB() - value);
						} else {
							player.setMaxMPX(player.getMaxMPX() - (int) (value * add));
						}
						soul.setMaxMpB(soul.getMaxMpB() - value);
						break;
					case 1:
						if (isCurrSoul) {
							player.setBreakDefenceB(player.getBreakDefenceB() - value);
						} else {
							player.setBreakDefenceX(player.getBreakDefenceX() - (int) (value * add));
						}

						soul.setBreakDefenceB(soul.getBreakDefenceB() - value);
						break;
					case 2:
						if (isCurrSoul) {
							player.setHitB(player.getHitB() - value);
						} else {
							player.setHitX(player.getHitX() - (int) (value * add));
						}
						soul.setHitB(soul.getHitB() - value);
						break;
					case 3:
						if (isCurrSoul) {
							player.setDodgeB(player.getDodgeB() - value);
						} else {
							player.setDodgeX(player.getDodgeX() - (int) (value * add));
						}
						soul.setDodgeB(soul.getDodgeB() - value);
						break;
					case 4:
						if (isCurrSoul) {
							player.setAccurateB(player.getAccurateB() - value);
						} else {
							player.setAccurateX(player.getAccurateX() - (int) (value * add));
						}
						soul.setAccurateB(soul.getAccurateB() - value);
						break;
					case 5:
						if (isCurrSoul) {
							player.setCriticalHitB(player.getCriticalHitB() - value);
						} else {
							player.setCriticalHitX(player.getCriticalHitX() - (int) (value * add));
						}
						soul.setCriticalHitB(soul.getCriticalHitB() - value);
						break;
					case 6:
						if (isCurrSoul) {
							player.setCriticalDefenceB(player.getCriticalDefenceB() - value);
						} else {
							player.setCriticalDefenceX(player.getCriticalDefenceX() - (int) (value * add));
						}
						soul.setCriticalDefenceB(soul.getCriticalDefenceB() - value);
						break;
					case 7:
						if (isCurrSoul) {
							player.setFireAttackB(player.getFireAttackB() - value);
						} else {
							player.setFireAttackX(player.getFireAttackX() - (int) (value * add));
						}
						soul.setFireAttackB(soul.getFireAttackB() - value);
						break;
					case 8:
						if (isCurrSoul) {
							player.setBlizzardAttackB(player.getBlizzardAttackB() - value);
						} else {
							player.setBlizzardAttackX(player.getBlizzardAttackX() - (int) (value * add));
						}
						soul.setBlizzardAttackB(soul.getBlizzardAttackB() - value);
						break;
					case 9:
						if (isCurrSoul) {
							player.setWindAttackB(player.getWindAttackB() - value);
						} else {
							player.setWindAttackX(player.getWindAttackX() - (int) (value * add));
						}
						soul.setWindAttackB(soul.getWindAttackB() - value);
						break;
					case 10:
						if (isCurrSoul) {
							player.setThunderAttackB(player.getThunderAttackB() - value);
						} else {
							player.setThunderAttackX(player.getThunderAttackX() - (int) (value * add));
						}
						soul.setThunderAttackB(soul.getThunderAttackB() - value);
						break;
					case 11:
						if (isCurrSoul) {
							player.setFireDefenceB(player.getFireDefenceB() - value);
						} else {
							player.setFireDefenceX(player.getFireDefenceX() - (int) (value * add));
						}
						soul.setFireDefenceB(soul.getFireDefenceB() - value);
						break;
					case 12:
						if (isCurrSoul) {
							player.setBlizzardDefenceB(player.getBlizzardDefenceB() - value);
						} else {
							player.setBlizzardDefenceX(player.getBlizzardDefenceX() - (int) (value * add));
						}
						soul.setBlizzardDefenceB(soul.getBlizzardDefenceB() - value);
						break;
					case 13:
						if (isCurrSoul) {
							player.setWindDefenceB(player.getWindDefenceB() - value);
						} else {
							player.setWindDefenceX(player.getWindDefenceX() - (int) (value * add));
						}
						soul.setWindDefenceB(soul.getWindDefenceB() - value);
						break;
					case 14:
						if (isCurrSoul) {
							player.setThunderDefenceB(player.getThunderDefenceB() - value);
						} else {
							player.setThunderDefenceX(player.getThunderDefenceX() - (int) (value * add));
						}
						soul.setThunderDefenceB(soul.getThunderDefenceB() - value);
						break;
					}
				}
			}
		}
	}

	/**
	 * 将装备上镶嵌的宝石属性加载到人身上
	 * @param player
	 */
	private void addInlayArticlePropertyValue(Player player, long[] inlayArticleIds, int soulType) {
		if (inlayArticleIds != null && player != null) {
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			if (aem != null && am != null) {
				for (long inlayId : inlayArticleIds) {
					if (inlayId != -1) {
						ArticleEntity ae = aem.getEntity(inlayId);
						if (ae != null) {
							Article a = am.getArticle(ae.getArticleName());
							if (a instanceof InlayArticle) {
								InlayArticle xiazi = (InlayArticle) a;
								if (xiazi.getInlayType() > 1) {
									BaoShiXiaZiData data = getxiLianData(player, inlayId);
									if (data != null) {
										String names[] = data.getNames();
										if (names != null && names.length > 0) {
											for (String name : names) {
												if (name != null && !name.isEmpty()) {
													Article bs = am.getArticle(name);
													if (bs != null && bs instanceof InlayArticle) {
														((InlayArticle) bs).addPropertyValue(player, soulType);
													}
												}
											}
										}
									}
								} else {
									// TODO 属性未算
									xiazi.addPropertyValue(player, soulType);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 将装备上镶嵌的宝石属性从人身上卸载
	 * @param player
	 */
	private void removeInlayArticlePropertyValue(Player player, long[] inlayArticleIds, int soulType) {
		if (inlayArticleIds != null && player != null) {
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			if (aem != null && am != null) {
				for (long inlayId : inlayArticleIds) {
					if (inlayId != -1) {
						ArticleEntity ae = aem.getEntity(inlayId);
						if (ae != null) {
							Article a = am.getArticle(ae.getArticleName());
							if (a instanceof InlayArticle) {
								InlayArticle xiazi = (InlayArticle) a;
								if (xiazi.getInlayType() > 1) {
									BaoShiXiaZiData data = getxiLianData(player, inlayId);
									if (data != null) {
										String names[] = data.getNames();
										if (names != null && names.length > 0) {
											for (String name : names) {
												if (name != null && !name.isEmpty()) {
													Article bs = am.getArticle(name);
													if (bs != null && bs instanceof InlayArticle) {
														((InlayArticle) bs).removePropertyValue(player, soulType);
													}
												}
											}
										}
									}
								} else {
									((InlayArticle) a).removePropertyValue(player, soulType);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 将装备上镶嵌的器灵属性加载到人身上
	 * @param player
	 */
	private void addInlayQiLingArticlePropertyValue(Player player, long[] inlayQiLingArticleIds, int soulType) {
		if (inlayQiLingArticleIds != null && player != null) {
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			if (aem != null && am != null) {
				for (long inlayId : inlayQiLingArticleIds) {
					if (inlayId != -1) {
						ArticleEntity ae = aem.getEntity(inlayId);
						if (ae instanceof QiLingArticleEntity) {
							Article a = am.getArticle(ae.getArticleName());
							if (a instanceof QiLingArticle) {
								// TODO 属性未算
								((QiLingArticle) a).addPropertyValue(player, soulType, ae.getColorType(), (QiLingArticleEntity) ae);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 将装备上镶嵌的器灵属性从人身上卸载
	 * @param player
	 */
	private void removeInlayQiLingArticlePropertyValue(Player player, long[] inlayQiLingArticleIds, int soulType) {
		if (inlayQiLingArticleIds != null && player != null) {
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			if (aem != null && am != null) {
				for (long inlayId : inlayQiLingArticleIds) {
					if (inlayId != -1) {
						ArticleEntity ae = aem.getEntity(inlayId);
						if (ae instanceof QiLingArticleEntity) {
							Article a = am.getArticle(ae.getArticleName());
							if (a instanceof QiLingArticle) {
								((QiLingArticle) a).removePropertyValue(player, soulType, ae.getColorType(), (QiLingArticleEntity) ae);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 增加套装属性
	 */
	public void addSuitPropertyValue(Player player, int[] values) {
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + value);
						break;
					case 1:
						player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + value);
						break;
					case 2:
						player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + value);
						break;
					case 3:
						player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + value);
						break;
					}
				}
			}
		}
	}

	/**
	 * 减少套装属性
	 */
	public void removeSuitPropertyValue(Player player, int[] values) {
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() - value);
						break;
					case 1:
						player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() - value);
						break;
					case 2:
						player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() - value);
						break;
					case 3:
						player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() - value);
						break;
					}
				}
			}
		}
	}

	/**
	 * 处理查询装备鉴定请求
	 */
	public QUERY_EQUIPMENT_JIANDING_RES queryEquipmentJianDingReq(Player player, QUERY_EQUIPMENT_JIANDING_REQ req) {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		QUERY_EQUIPMENT_JIANDING_RES res = null;
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (aem != null && req != null && am != null) {
			ArticleEntity ae = aem.getEntity(req.getEquipmentId());
			if (ae instanceof EquipmentEntity) {
				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.鉴定);
				if (!bln) {
					return null;
				}

				String descript = Translate.空白;
				Equipment e = (Equipment) am.getArticle(ae.getArticleName());
				String[] materials = 根据装备级别得到所需鉴定符(e.getPlayerLevelLimit());
				res = new QUERY_EQUIPMENT_JIANDING_RES(req.getSequnceNum(), descript, materials);
			} else {
				String description = Translate.空白;
				try {
					description = Translate.translateString(Translate.查询装备鉴定请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.空白) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[查询装备鉴定] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[查询装备鉴定] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}

		return res;
	}

	/**
	 * 装备鉴定请求，返回一个确认框
	 * @param player
	 * @param req
	 * @return
	 */
	public void equipmentJianDingReq(Player player, EQUIPMENT_JIANDING_REQ req) {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player != null && req != null && aem != null) {
			long equipmentId = req.getEquipmentId();
			long materialId = req.getMaterialId();
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae instanceof EquipmentEntity) {
				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				if (ee.getEndowments() >= maxEndowments) {
					player.sendError(Translate.鉴定已经完美了);
					return;
				}
				boolean bln = ee.isOprate(player, false, EquipmentEntity.鉴定);
				if (!bln) {
					return;
				}

				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
					player.sendError(Translate.高级锁魂物品不能做此操作);
					return;
				}

				if (ae != null) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					if (isChiBang(e.getEquipmentType())) {
						player.sendError(Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "鉴定" } }));
						return;
					}
				}

				boolean hasMaterial = false;
				if (((EquipmentEntity) ae).getEndowments() != 0) {
					if (materialEntity == null) {
						String description = Translate.空白;
						description = Translate.请放入正确物品;
						try {
							description = Translate.请放入鉴定所需物品;
						} catch (Exception ex) {

						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[鉴定装备] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) +
						// "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[鉴定装备] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					hasMaterial = true;
				}
				boolean hasEquipment = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									hasEquipment = true;
								}
							}
							if (!hasMaterial) {
								int index2 = knapsack.hasArticleEntityByArticleId(materialId);
								if (index2 != -1) {
									hasMaterial = true;
								}
							}
							if (hasEquipment && hasMaterial) {
								break;
							}
						}
					}
				}
				if (hasEquipment && hasMaterial) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					// 未鉴定
					if (((EquipmentEntity) ae).getEndowments() == 0) {
						// 提示用户是否鉴定

						// WindowManager wm = WindowManager.getInstance();
						// MenuWindow mw = wm.createTempMenuWindow(600);
						// String title = Translate.鉴定提示信息;
						// mw.setTitle(title);
						// String desUUB = Translate.确定要开始鉴定吗;
						// try {
						// desUUB = Translate.translateString(Translate.确定要开始鉴定吗未鉴定, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
						// } catch (Exception ex) {
						// ex.printStackTrace();
						// }
						//
						// mw.setDescriptionInUUB(desUUB);
						// Option_JianDingConfirm option = new Option_JianDingConfirm();
						// option.setReq(req);
						// option.setText(Translate.确定);
						// Option[] options = new Option[] { option, new Option_Cancel() };
						// mw.setOptions(options);
						// CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						// player.addMessageToRightBag(creq);
						confirmEquipmentJianDing(player, equipmentId, materialId);
						return;
					} else {
						String[] materials = 根据装备级别得到所需鉴定符(e.getPlayerLevelLimit());
						if (materials != null) {
							for (String material : materials) {
								if (material != null && material.equals(materialEntity.getArticleName())) {
									// 提示用户是否鉴定

									WindowManager wm = WindowManager.getInstance();
									MenuWindow mw = wm.createTempMenuWindow(600);
									String title = Translate.鉴定提示信息;
									mw.setTitle(title);
									String desUUB = Translate.确定要开始鉴定吗;
									if (!ae.isBinded() && materialEntity.isBinded()) {
										try {
											desUUB = Translate.translateString(Translate.确定要开始鉴定吗绑定的, new String[][] { { Translate.ARTICLE_NAME_1, materialEntity.getArticleName() }, { Translate.ARTICLE_NAME_2, ae.getArticleName() } });
										} catch (Exception ex) {
											ex.printStackTrace();
										}
										mw.setDescriptionInUUB(desUUB);
										Option_JianDingConfirm option = new Option_JianDingConfirm();
										option.setReq(req);
										option.setText(Translate.确定);
										Option[] options = new Option[] { option, new Option_Cancel() };
										mw.setOptions(options);
										CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
										player.addMessageToRightBag(creq);
										return;
									}
									confirmEquipmentJianDing(player, equipmentId, materialId);
									return;

								}
							}
						}
					}

				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备鉴定请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[装备鉴定请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (!hasMaterial) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (materialEntity != null ? materialEntity.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备鉴定请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (materialEntity != null ?
						// materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]",
						// start);
						if (logger.isWarnEnabled()) logger.warn("[装备鉴定请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.只有装备可以鉴定;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[装备鉴定请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] [" +
				// (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[装备鉴定请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 确认鉴定装备
	 * @param player
	 * @param equipmentId
	 * @param materialId
	 */
	public void confirmEquipmentJianDing(Player player, long equipmentId, long materialId) {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player != null && aem != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.鉴定);
				if (!bln) {
					return;
				}

				boolean hasMaterial = false;
				if (((EquipmentEntity) ae).getEndowments() != 0) {
					if (materialEntity == null) {
						String description = Translate.空白;
						description = Translate.请放入鉴定所需物品;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认鉴定装备] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) +
						// "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认鉴定装备] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					hasMaterial = true;
				}
				boolean hasEquipment = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									hasEquipment = true;
								}
							}
							if (!hasMaterial) {
								int index2 = knapsack.hasArticleEntityByArticleId(materialId);
								if (index2 != -1) {
									hasMaterial = true;
								}
							}
							if (hasEquipment && hasMaterial) {
								break;
							}
						}
					}
				}
				if (hasEquipment && hasMaterial) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					if (((EquipmentEntity) ae).getEndowments() == 0) {
						// 鉴定成功装备资质赋值
						((EquipmentEntity) ae).setEndowments(getEndowments());
						String description = Translate.鉴定成功装备资质已改变;
						try {
							description = Translate.translateString(Translate.鉴定成功装备资质已改变带物品名, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.LEVEL_1, e.getPlayerLevelLimit() + "" }, { Translate.ENDOWMENTS_1, getEndowmentsString(((EquipmentEntity) ae).getEndowments()) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备鉴定成功, (byte) 1, AnimationManager.动画播放位置类型_装备鉴定, 0, 0);
						if (pareq != null) {
							player.addMessageToRightBag(pareq);
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
						player.addMessageToRightBag(hreq);
						NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate((EquipmentEntity) ae) });
						player.addMessageToRightBag(nreq);
						try {
							EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.鉴定装备次数, 1L });
							EventRouter.getInst().addEvent(evt);
						} catch (Exception eex) {
							PlayerAimManager.logger.error("[目标系统] [鉴定装备统计异常] [" + player.getLogString() + "]", eex);
						}
						if (logger.isInfoEnabled()) {
							// logger.info("[确认鉴定装备] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + ae.getArticleName() + "] [" +
							// ae.getId() + "] [" + description + "]", start);
							if (logger.isInfoEnabled()) logger.info("[确认鉴定装备] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName(), ae.getId(), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						}
						return;
					} else {
						String[] materials = 根据装备级别得到所需鉴定符(e.getPlayerLevelLimit());
						if (materials != null) {
							for (String material : materials) {
								if (material != null && material.equals(materialEntity.getArticleName())) {
									ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(materialId, "鉴定删除", true);

									if (aee != null) {

										// 统计
										ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "鉴定删除", null);

										if (aee.isBinded() && !ee.isBinded()) {
											ae.setBinded(true);
										}
										// 鉴定成功装备资质赋值
										((EquipmentEntity) ae).setEndowments(getEndowments());
										String description = Translate.鉴定成功装备资质已改变;
										try {
											description = Translate.translateString(Translate.鉴定成功装备资质已改变带物品名, new String[][] { { Translate.ARTICLE_ID_1, ae.getId() + "" }, { Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.LEVEL_1, e.getPlayerLevelLimit() + "" }, { Translate.ENDOWMENTS_1, getEndowmentsString(((EquipmentEntity) ae).getEndowments()) } });
										} catch (Exception ex) {
											ex.printStackTrace();
										}
										PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备鉴定成功, (byte) 1, AnimationManager.动画播放位置类型_装备鉴定, 0, 0);
										if (pareq != null) {
											player.addMessageToRightBag(pareq);
										}
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
										player.addMessageToRightBag(hreq);
										NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate((EquipmentEntity) ae) });
										player.addMessageToRightBag(nreq);
										// if (logger.isInfoEnabled()) {
										// logger.info("[确认鉴定装备] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" +
										// ae.getArticleName() + "] [" + ae.getId() + "] [" + description + "]", start);
										if (logger.isInfoEnabled()) logger.info("[确认鉴定装备] [color:" + aee.getColorType() + "] [aeename:" + aee.getArticleName() + "] [materialsnum:" + materials.length + "] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName(), ae.getId(), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
										// }
										return;
									}
								}
							}

						}
					}

				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认鉴定装备] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认鉴定装备] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (!hasMaterial) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (materialEntity != null ? materialEntity.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认鉴定装备] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (materialEntity != null ?
						// materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]",
						// start);
						if (logger.isWarnEnabled()) logger.warn("[确认鉴定装备] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.只有装备可以鉴定;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[确认鉴定装备] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] [" +
				// (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[确认鉴定装备] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 根据铭刻请求返回铭刻响应
	 * @param req
	 * @return
	 */
	public QUERY_EQUIPMENT_INSCRIPTION_RES queryEquipmentInscriptionReq(Player player, QUERY_EQUIPMENT_INSCRIPTION_REQ req) {
		QUERY_EQUIPMENT_INSCRIPTION_RES res = null;
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && req != null && aem != null) {
			ArticleEntity ae = aem.getEntity(req.getEquipmentId());
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.铭刻);
				if (!bln) {
					return null;
				}
				String descript = Translate.空白;
				ArticleManager am = ArticleManager.getInstance();
				Equipment e = (Equipment) am.getArticle(ae.getArticleName());
				String[] materials = 根据装备级别得到所需铭刻符(e.getPlayerLevelLimit());
				res = new QUERY_EQUIPMENT_INSCRIPTION_RES(req.getSequnceNum(), req.getEquipmentId(), descript, materials);
			} else {
				String description = Translate.空白;
				try {
					description = Translate.translateString(Translate.查询装备铭刻请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.空白) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[查询装备铭刻] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[查询装备铭刻] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
		return res;
	}

	/**
	 * 装备铭刻请求，返回一个确认框
	 * @param player
	 * @param req
	 * @return
	 */
	public void equipmentInscriptionReq(Player player, EQUIPMENT_INSCRIPTION_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && req != null && aem != null) {
			long equipmentId = req.getEquipmentId();
			long materialId = req.getMaterialId();
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae instanceof EquipmentEntity && materialEntity != null) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.铭刻);
				if (!bln) {
					return;
				}

				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
					player.sendError(Translate.高级锁魂物品不能做此操作);
					return;
				}

				if (ae != null) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					if (isChiBang(e.getEquipmentType())) {
						player.sendError(Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "铭刻" } }));
						return;
					}
				}

				boolean hasEquipment = false;
				boolean hasMaterial = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((EquipmentEntity) ae).isInscriptionFlag()) {
										String description = Translate.此装备已经铭刻不需要执行此操作;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasEquipment = true;
								}
							}
							if (!hasMaterial) {
								int index2 = knapsack.hasArticleEntityByArticleId(materialId);
								if (index2 != -1) {
									hasMaterial = true;
								}
							}
							if (hasEquipment && hasMaterial) {
								break;
							}
						}
					}
				}
				if (hasEquipment && hasMaterial) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					String[] materials = 根据装备级别得到所需铭刻符(e.getPlayerLevelLimit());
					if (materials != null) {
						for (String material : materials) {
							if (material != null && material.equals(materialEntity.getArticleName())) {
								// 提示用户是否铭刻

								// WindowManager wm = WindowManager.getInstance();
								// MenuWindow mw = wm.createTempMenuWindow(600);
								// String title = Translate.铭刻提示信息;
								// mw.setTitle(title);
								// String desUUB = Translate.确定要开始铭刻吗;
								// if(!ae.isBinded()){
								// try{
								// desUUB = Translate.translateString(Translate.确定要开始铭刻吗绑定的,new String[][]{{Translate.ARTICLE_NAME_1,ae.getArticleName()}});
								// }catch(Exception ex){
								// ex.printStackTrace();
								// }
								// mw.setDescriptionInUUB(desUUB);
								// Option_EquipmentInscriptionConfirm option = new Option_EquipmentInscriptionConfirm();
								// option.setReq(req);
								// option.setText(Translate.确定);
								// Option[] options = new Option[]{option,new Option_Cancel()};
								// mw.setOptions(options);
								// CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getDescriptionInUUB(),options);
								// player.addMessageToRightBag(creq);
								// return;
								// }
								confirmEquipmentInscriptionReq(player, equipmentId, materialId);
								return;

							}
						}
					}
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备铭刻请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[装备铭刻请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (!hasMaterial) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (materialEntity != null ? materialEntity.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备铭刻请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (materialEntity != null ?
						// materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]",
						// start);
						if (logger.isWarnEnabled()) logger.warn("[装备铭刻请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.装备铭刻需要放入装备和铭刻物品;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[装备铭刻请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] [" +
				// (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[装备铭刻请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 确认装备铭刻
	 * @param player
	 * @param req
	 * @return
	 */
	public void confirmEquipmentInscriptionReq(Player player, long equipmentId, long materialId) {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player != null && aem != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae instanceof EquipmentEntity && materialEntity != null) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.铭刻);
				if (!bln) {
					return;
				}

				boolean hasEquipment = false;
				boolean hasMaterial = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((EquipmentEntity) ae).isInscriptionFlag()) {
										String description = Translate.此装备已经铭刻不需要执行此操作;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasEquipment = true;
								}
							}
							if (!hasMaterial) {
								int index2 = knapsack.hasArticleEntityByArticleId(materialId);
								if (index2 != -1) {
									hasMaterial = true;
								}
							}
							if (hasEquipment && hasMaterial) {
								break;
							}
						}
					}
				}
				if (hasEquipment && hasMaterial) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					String[] materials = 根据装备级别得到所需铭刻符(e.getPlayerLevelLimit());
					if (materials != null) {
						for (String material : materials) {
							if (material != null && material.equals(materialEntity.getArticleName())) {
								ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(materialId, "铭刻删除", true);
								if (aee != null) {

									// 统计
									ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "铭刻删除", null);

									if (!ae.isBinded()) {
										ae.setBinded(true);
									}
									// 铭刻成功
									((EquipmentEntity) ae).setInscriptionFlag(true);
									((EquipmentEntity) ae).setCreaterName(player.getName());
									String description = Translate.铭刻完成;
									PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备精炼绑定铭刻成功, (byte) 1, AnimationManager.动画播放位置类型_精炼绑定铭刻, 0, 0);
									if (pareq != null) {
										player.addMessageToRightBag(pareq);
									}
									if (((EquipmentEntity) ae).getStar() > 20) {
										AchievementManager.getInstance().record(player, RecordAction.羽化装备成功次数);
									}
									// 铭刻活动
									if (ae.getColorType() >= 3) {
										try {
											CompoundReturn cr = ActivityManagers.getInstance().getValue(7, player);
											if (cr != null && cr.getBooleanValue()) {
												String articlname = cr.getStringValue();
												int num = cr.getIntValue();
												Article a = ArticleManager.getInstance().getArticle(articlname);
												ArticleEntity articlee = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, 3, 1, true);
												MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { articlee }, new int[] { num }, "铭刻奖励", "恭喜您铭刻成功，系统赠送您高级鉴定符一个。", 0, 0, 0, "铭刻奖励");
											}
										} catch (Exception e2) {
											logger.info("[装备铭刻送鉴定符] [活动] [aname:" + ae.getArticleName() + "] [" + player.getLoving() + "] [异常] [" + e2 + "]");
										}

									}

									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
									player.addMessageToRightBag(hreq);
									NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate((EquipmentEntity) ae) });
									player.addMessageToRightBag(nreq);
									try {
										EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.铭刻装备次数, 1L });
										EventRouter.getInst().addEvent(evt);
									} catch (Exception eex) {
										PlayerAimManager.logger.error("[目标系统] [铭刻装备统计异常] [" + player.getLogString() + "]", eex);
									}
									if (logger.isInfoEnabled()) {
										// logger.info("[确认装备铭刻] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + ae.getArticleName() +
										// "] [" + ae.getId() + "] [" + description + "]", start);
										if (logger.isInfoEnabled()) logger.info("[确认装备铭刻] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName(), ae.getId(), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
									}
									return;
								}
							}
						}
					}
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备铭刻] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备铭刻] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (!hasMaterial) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (materialEntity != null ? materialEntity.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备铭刻] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (materialEntity != null ?
						// materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]",
						// start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备铭刻] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.装备铭刻需要放入装备和铭刻物品;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[确认装备铭刻] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] [" +
				// (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[确认装备铭刻] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 根据摘星请求返回响应
	 * @param req
	 * @return
	 */
	public QUERY_EQUIPMENT_PICKSTAR_RES queryEquipmentPickStarReq(Player player, QUERY_EQUIPMENT_PICKSTAR_REQ req) {
		QUERY_EQUIPMENT_PICKSTAR_RES res = null;
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && req != null && aem != null) {
			ArticleEntity ae = aem.getEntity(req.getEquipmentId());
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.摘星);
				if (!bln) {
					return null;
				}

				if (((EquipmentEntity) ae).getStar() <= 0) {
					String description = Translate.没有星星摘星;
					res = new QUERY_EQUIPMENT_PICKSTAR_RES(req.getSequnceNum(), req.getEquipmentId(), description);
				} else {
					String description = Translate.空白;
					try {
						description = Translate.translateString(Translate.需求金币提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(得到摘星所需绑银(((EquipmentEntity) ae).getStar())) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					res = new QUERY_EQUIPMENT_PICKSTAR_RES(req.getSequnceNum(), req.getEquipmentId(), description);
				}
			} else {
				String description = Translate.空白;
				try {
					description = Translate.translateString(Translate.查询装备摘星请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.空白) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[查询装备摘星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[查询装备摘星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
		return res;
	}

	/**
	 * 装备摘星请求，返回一个确认框
	 * @param player
	 * @param req
	 * @return
	 */
	public void equipmentPickStarReq(Player player, EQUIPMENT_PICKSTAR_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && req != null && aem != null) {
			long equipmentId = req.getEquipmentId();
			ArticleEntity ae = aem.getEntity(equipmentId);
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.摘星);
				if (!bln) {
					return;
				}

				if (((EquipmentEntity) ae).getStar() > 装备炼星需要羽化的等级) {
					player.sendError(Translate.不可以摘星);
					return;
				}

				if (((EquipmentEntity) ae).getStar() <= 0) {
					String description = Translate.没有星星摘星;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}
				int money = 得到摘星所需绑银(((EquipmentEntity) ae).getStar());
				if (!player.bindSilverEnough(money)) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(money) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}
				boolean hasEquipment = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				Article article = 得到摘星物种((EquipmentEntity) ae);
				if (article != null) {
					Knapsack knapsack = player.getKnapsack_common();
					if (knapsack == null) {
						String description = Translate.服务器物品出现错误;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备摘星请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[装备摘星请求] [失败] [knapsack == null] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (knapsack.getEmptyNum() == 0) {
						String description = Translate.背包空间不足;
						try {
							description = Translate.translateString(Translate.背包空间不足提示, new String[][] { { Translate.ARTICLE_NAME_1, ArticleManager.得到背包名字(article.getKnapsackType()) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备摘星请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[装备摘星请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.服务器物品出现错误;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[装备摘星请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[装备摘星请求] [失败] [article==null] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((EquipmentEntity) ae).getStar() == 0) {
										String description = Translate.此装备还没有星级不需要执行此操作;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasEquipment = true;
									break;
								}
							}
						}
					}
				}
				if (hasEquipment) {
					// 提示用户是否铭刻

					WindowManager wm = WindowManager.getInstance();
					MenuWindow mw = wm.createTempMenuWindow(600);
					String title = Translate.摘星提示信息;
					mw.setTitle(title);
					String desUUB = Translate.确定要开始摘星吗;
					mw.setDescriptionInUUB(desUUB);
					Option_EquipmentPickStarConfirm option = new Option_EquipmentPickStarConfirm();
					option.setReq(req);
					option.setText(Translate.确定);
					Option[] options = new Option[] { option, new Option_Cancel() };
					mw.setOptions(options);
					CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
					player.addMessageToRightBag(creq);
					return;
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备摘星请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[装备摘星请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[装备摘星请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[装备摘星请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	public void moveStar(long removeId ,long addId, Player p){
		ArticleEntity removeAe = ArticleEntityManager.getInstance().getEntity(removeId);
		ArticleEntity addAe = ArticleEntityManager.getInstance().getEntity(addId);
		if(removeAe instanceof EquipmentEntity && addAe instanceof EquipmentEntity){
			Article a = ArticleManager.getInstance().getArticle(removeAe.getArticleName());
			if(a == null){
				logger.warn("[装备摘星] [确认失败：物品不存在] ["+removeAe.getArticleName()+"] ["+p.getLogString()+"]");
				return;
			}
			Article a2 = ArticleManager.getInstance().getArticle(addAe.getArticleName());
			if(a2 == null){
				logger.warn("[装备摘星] [确认失败：物品不存在2] ["+addAe.getArticleName()+"] ["+p.getLogString()+"]");
				return;
			}
			
			if(!(a2 instanceof Equipment) || !(a instanceof Equipment)){
				p.sendError(Translate.只能装备进行摘星操作);
				logger.warn("[装备摘星] [确认失败：只能装备进行摘星操作] ["+addAe.getArticleName()+"] ["+p.getLogString()+"]");
				return;
			}
			Equipment aeq = (Equipment)a2;
			Equipment aeq2 = (Equipment)a;
			if(aeq.getSpecial() != 0 || aeq2.getSpecial() != 0){
				p.sendError(Translate.特殊装备不能进行摘星操作);
				logger.warn("[装备摘星] [确认失败：只能装备进行摘星操作2] ["+a2.getName()+"] ["+a.getName()+"]  ["+p.getLogString()+"]");
				return;
			}
			EquipmentEntity eq = (EquipmentEntity)removeAe;
			EquipmentEntity eq2 = (EquipmentEntity)addAe;
			if(!addAe.isBinded()){
				p.sendError(Translate.要继承星的装备必须是绑定的);
				return;
			}
			
			int star = eq.getStar();
			int star2 = eq2.getStar();
			if(star <= 0){
				p.sendError(Translate.该装备还没有炼星);
				logger.warn("[装备摘星] [确认失败：装备没星] [star:"+star+"] ["+removeAe.getArticleName()+"] ["+p.getLogString()+"]");
				return;
			}
			if(star2 >= 20){
				p.sendError(Translate.被转移的星已达最大);
				return;
			}
			if(star2 >= star){
				p.sendError(Translate.被转移的星小于转移的);
				return;
			}
			long cost = costsilver[star - 1];
			if(p.getSilver() < cost){
				p.sendError(Translate.余额不足);
				return;
			}
			
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			String title = Translate.摘星提示标题;
			mw.setTitle(title);
			String desUUB = Translate.确认开始摘星吗;
			mw.setDescriptionInUUB(desUUB);
			Option_EquipmentRemoveStar option = new Option_EquipmentRemoveStar();
			option.setRemoveId(removeId);
			option.setAddId(addId);
			option.setText(Translate.确定);
			Option[] options = new Option[] { option, new Option_Cancel() };
			mw.setOptions(options);
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			p.addMessageToRightBag(creq);
		}else{
			p.sendError(Translate.只能装备摘星);
		}
	}
	
	public long [] costsilver = {250,350,500,800,1200,1800,2500,3500,5000,8000,12000,18000,26000,35000,55000,75000,100000,135000,185000,250000};
	public void queryStarMoney(STAR_MONEY_REQ req,Player p){
		long id = req.getEquipmentId();
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
		if (ae instanceof EquipmentEntity) {
			Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
			if(a == null || !(a instanceof Equipment)){
				logger.warn("[装备摘星] [请求失败：物品不存在] ["+ae.getArticleName()+"] ["+p.getLogString()+"]");
				return;
			}
			EquipmentEntity eq = (EquipmentEntity)ae;
			int star = eq.getStar();
			if(star <= 0){
				p.sendError(Translate.该装备还没有炼星);
				logger.warn("[装备摘星] [请求失败：装备没星] [star:"+star+"] ["+ae.getArticleName()+"] ["+p.getLogString()+"]");
				return;
			}
			long cost = costsilver[star - 1];
			STAR_MONEY_RES res = new STAR_MONEY_RES(req.getSequnceNum(),cost);
			p.addMessageToRightBag(res);
		}else{
			p.sendError(Translate.只能装备摘星);
		}
	}
	
	public void queryStarPage(STAR_HELP_REQ req,Player p){
		String mess = "<f size='34' color='0x78f4ff'>" + Translate.摘星功能信息 + "</f>";
		p.addMessageToRightBag(new STAR_HELP_RES(req.getSequnceNum(),mess,""));
	}
	
	/**
	 * 确认装备摘星
	 * @param player
	 * @param req
	 * @return
	 */
	public void confirmEquipmentPickStarReq(Player player, long equipmentId) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && aem != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);
			if (ae instanceof EquipmentEntity) {
				if (((EquipmentEntity) ae).getStar() <= 0) {
					String description = Translate.没有星星摘星;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}

				if (((EquipmentEntity) ae).getStar() > 装备炼星需要羽化的等级) {
					player.sendError(Translate.不可以摘星);
					return;
				}

				if (ae != null) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					if (isChiBang(e.getEquipmentType())) {
						player.sendError(Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "摘星" } }));
						return;
					}
				}

				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
					player.sendError(Translate.高级锁魂物品不能做此操作);
					return;
				}
				int money = 得到摘星所需绑银(((EquipmentEntity) ae).getStar());
				if (!player.bindSilverEnough(money)) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, money + "" } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}
				boolean hasEquipment = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				Article article = 得到摘星物种((EquipmentEntity) ae);
				if (article != null) {
					Knapsack knapsack = player.getKnapsack_common();
					if (knapsack == null) {
						String description = Translate.服务器物品出现错误;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备摘星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备摘星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (knapsack.getEmptyNum() == 0) {
						String description = Translate.背包空间不足;
						try {
							description = Translate.translateString(Translate.背包空间不足提示, new String[][] { { Translate.ARTICLE_NAME_1, ArticleManager.得到背包名字(article.getKnapsackType()) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备摘星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备摘星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.服务器物品出现错误;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[确认装备摘星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[确认装备摘星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((EquipmentEntity) ae).getStar() == 0) {
										String description = Translate.此装备还没有星级不需要执行此操作;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasEquipment = true;
									break;
								}
							}
						}
					}
				}
				if (hasEquipment) {
					// try {
					// BillingCenter bc = BillingCenter.getInstance();
					// bc.playerExpense(player, money, CurrencyType.BIND_YINZI, ExpenseReasonType.EQUIPMENT_ZHAIXING, "使用绑银装备摘星");
					// } catch (Exception ex) {
					// ex.printStackTrace();
					// if (logger.isWarnEnabled()) logger.warn("[确认装备摘星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ?
					// ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
					// return;
					// }

					((EquipmentEntity) ae).setStar(((EquipmentEntity) ae).getStar() - 1);
					NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate((EquipmentEntity) ae) });
					player.addMessageToRightBag(nreq);
					if (摘星成功率()) {
						try {
							ArticleEntity starEntity = aem.createEntity(article, true, ArticleEntityManager.CREATE_REASON_摘星, player, 0, 1, true);
							if (starEntity == null) {
								String description = Translate.服务器物品出现错误;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								// logger.warn("[确认装备摘星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ?
								// ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
								if (logger.isWarnEnabled()) logger.warn("[确认装备摘星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}

							// 统计
							ArticleStatManager.addToArticleStat(player, null, starEntity, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "装备摘星获得", null);

							PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备摘星成功, (byte) 1, AnimationManager.动画播放位置类型_摘星, 0, 0);
							if (pareq != null) {
								player.addMessageToRightBag(pareq);
							}
							if (player.putToKnapsacks(starEntity, "摘星")) {
								String description = Translate.摘星成功;
								try {
									description = Translate.translateString(Translate.摘星成功获得物品, new String[][] { { Translate.ARTICLE_NAME_1, starEntity.getArticleName() } });
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
								player.addMessageToRightBag(hreq);
								// logger.warn("[确认装备摘星] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ?
								// ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
								if (logger.isWarnEnabled()) logger.warn("[确认装备摘星] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							} else {
								String description = Translate.摘星成功但背包空间不足;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								// logger.warn("[确认装备摘星] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ?
								// ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
								if (logger.isWarnEnabled()) logger.warn("[确认装备摘星] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					} else {
						PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备摘星失败, (byte) 1, AnimationManager.动画播放位置类型_摘星, 0, 0);
						if (pareq != null) {
							player.addMessageToRightBag(pareq);
						}
						String description = Translate.摘星失败;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备摘星] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备摘星] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备摘星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备摘星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[确认装备摘星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[确认装备摘星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	public int 得到摘星所需绑银(int star) {
		return 0;
	}

	public boolean 摘星成功率() {
		boolean ok = false;
		if (random.nextInt(100) < 75) {
			ok = true;
		}
		return ok;
	}

	public Article 得到摘星物种(EquipmentEntity ee) {
		String articleName = null;
		if (ee.getStar() > 0 && ee.getStar() <= starNames.length) {
			articleName = starNames[ee.getStar() - 1];
		}
		if (articleName == null) {
			return null;
		}
		Article article = getArticle(articleName);

		return article;
	}

	/**
	 * 根据摘星请求返回响应
	 * @param req
	 * @return
	 */
	public QUERY_EQUIPMENT_INSERTSTAR_RES queryEquipmentInsertStarReq(Player player, QUERY_EQUIPMENT_INSERTSTAR_REQ req) {
		QUERY_EQUIPMENT_INSERTSTAR_RES res = null;
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && req != null && aem != null) {
			ArticleEntity ae = aem.getEntity(req.getEquipmentId());
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.装星);
				if (!bln) {
					return null;
				}
				String description = Translate.空白;
				try {
					description = Translate.translateString(Translate.需求金币提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(得到摘星所需绑银(((EquipmentEntity) ae).getStar())) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				String starName = 得到装备能装的星((EquipmentEntity) ae);
				res = new QUERY_EQUIPMENT_INSERTSTAR_RES(req.getSequnceNum(), req.getEquipmentId(), description, starName);
			} else {
				String description = Translate.空白;
				try {
					description = Translate.translateString(Translate.查询装备装星请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.空白) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[查询装备装星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[查询装备装星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
		return res;
	}

	/**
	 * 装备装星请求，返回一个确认框
	 * @param player
	 * @param req
	 * @return
	 */
	public void equipmentInsertStarReq(Player player, EQUIPMENT_INSERTSTAR_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && req != null && aem != null) {
			long equipmentId = req.getEquipmentId();
			long materialId = req.getMaterialId();
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae != null && ae instanceof EquipmentEntity && materialEntity != null) {

				if (((EquipmentEntity) ae).getStar() > 装备炼星需要羽化的等级) {
					player.sendError(Translate.不可以附星);
					return;
				}

				if (ae != null) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					if (isChiBang(e.getEquipmentType())) {
						player.sendError(Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "附星" } }));
						return;
					}
				}

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.装星);
				if (!bln) {
					return;
				}

				boolean hasEquipment = false;
				boolean hasMaterial = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((EquipmentEntity) ae).getStar() >= starMaxValue) {
										String description = Translate.装星已经到最大值提示;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasEquipment = true;
								}
							}
							if (!hasMaterial) {
								int index2 = knapsack.hasArticleEntityByArticleId(materialId);
								if (index2 != -1) {
									hasMaterial = true;
								}
							}
							if (hasEquipment && hasMaterial) {
								break;
							}
						}
					}
				}
				if (hasEquipment && hasMaterial) {
					String starName = 得到装备能装的星((EquipmentEntity) ae);
					if (starName != null && starName.equals(materialEntity.getArticleName())) {
						// 提示用户是否铭刻

						WindowManager wm = WindowManager.getInstance();
						MenuWindow mw = wm.createTempMenuWindow(600);
						String title = Translate.装星提示信息;
						mw.setTitle(title);
						String desUUB = Translate.确定要开始装星吗;
						if (!ae.isBinded() && materialEntity.isBinded()) {
							try {
								desUUB = Translate.translateString(Translate.确定要开始装星吗绑定的, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							mw.setDescriptionInUUB(desUUB);
							Option_EquipmentInsertStarConfirm option = new Option_EquipmentInsertStarConfirm();
							option.setReq(req);
							option.setText(Translate.确定);
							Option[] options = new Option[] { option, new Option_Cancel() };
							mw.setOptions(options);
							CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
							player.addMessageToRightBag(creq);
							return;
						}
						confirmEquipmentInsertStarReq(player, equipmentId, materialId);
						return;

					} else {
						String description = Translate.请放入正确物品;
						try {
							description = Translate.translateString(Translate.装备只能够装此种星, new String[][] { { Translate.ARTICLE_NAME_1, starName }, { Translate.COUNT_1, ((EquipmentEntity) ae).getStar() + "" } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备装星请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[装备装星请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备装星请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[装备装星请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (!hasMaterial) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (materialEntity != null ? materialEntity.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备装星请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (materialEntity != null ?
						// materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]",
						// start);
						if (logger.isWarnEnabled()) logger.warn("[装备装星请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				if (materialEntity == null && ae != null && ae instanceof EquipmentEntity) {
					description = Translate.请放入星星;
				} else if (materialEntity != null && ae != null && !(ae instanceof EquipmentEntity)) {
					description = Translate.请放入装备;
				} else {
					description = Translate.请放入装备和星星;
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[装备装星请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + equipmentId + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] [" + materialId + "] [" +
				// description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[装备装星请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), equipmentId, (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), materialId, description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 确认装备装星
	 * @param player
	 * @param req
	 * @return
	 */
	public void confirmEquipmentInsertStarReq(Player player, long equipmentId, long materialId) {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player != null && aem != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae instanceof EquipmentEntity && materialEntity != null) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.装星);
				if (!bln) {
					return;
				}

				if (((EquipmentEntity) ae).getStar() > 装备炼星需要羽化的等级) {
					player.sendError(Translate.不可以附星);
					return;
				}

				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
					player.sendError(Translate.高级锁魂物品不能做此操作);
					return;
				}

				boolean hasEquipment = false;
				boolean hasMaterial = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((EquipmentEntity) ae).getStar() >= starMaxValue) {
										String description = Translate.装星已经到最大值提示;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasEquipment = true;
								}
							}
							if (!hasMaterial) {
								int index2 = knapsack.hasArticleEntityByArticleId(materialId);
								if (index2 != -1) {
									hasMaterial = true;
								}
							}
							if (hasEquipment && hasMaterial) {
								break;
							}
						}
					}
				}
				if (hasEquipment && hasMaterial) {
					String starName = 得到装备能装的星((EquipmentEntity) ae);
					if (starName != null && starName.equals(materialEntity.getArticleName())) {
						ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(materialId, "附星删除", false);
						if (aee != null) {

							// 统计
							ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "附星删除", null);

							if (!ae.isBinded() && materialEntity.isBinded()) {
								ae.setBinded(true);
							}
							// 装星成功
							((EquipmentEntity) ae).setStar(((EquipmentEntity) ae).getStar() + 1);
							String description = Translate.装星完成;
							PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备装星成功, (byte) 1, AnimationManager.动画播放位置类型_装星, 0, 0);
							if (pareq != null) {
								player.addMessageToRightBag(pareq);
							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
							player.addMessageToRightBag(hreq);
							NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate((EquipmentEntity) ae) });
							player.addMessageToRightBag(nreq);
							if (logger.isInfoEnabled()) {
								// logger.info("[确认装备装星] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + ae.getArticleName() + "] [" +
								// ae.getId() + "] [" + description + "]", start);
								if (logger.isInfoEnabled()) logger.info("[确认装备装星] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName(), ae.getId(), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
							}
							return;
						} else {
							String description = Translate.删除物品失败;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							// logger.warn("[确认装备装星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName()
							// : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "id为" + materialId + "的物品没有删除成功]", start);
							if (logger.isWarnEnabled()) logger.warn("[确认装备装星] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品没有删除成功] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, materialId, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
							return;
						}
					} else {
						String description = Translate.请放入正确物品;
						try {
							description = Translate.translateString(Translate.装备只能够装此种星, new String[][] { { Translate.ARTICLE_NAME_1, starName }, { Translate.COUNT_1, ((EquipmentEntity) ae).getStar() + "" } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备装星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备装星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备装星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备装星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (!hasMaterial) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (materialEntity != null ? materialEntity.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备装星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (materialEntity != null ?
						// materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]",
						// start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备装星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				if (materialEntity == null && ae != null && ae instanceof EquipmentEntity) {
					description = Translate.请放入星星;
				} else if (materialEntity != null && ae != null && !(ae instanceof EquipmentEntity)) {
					description = Translate.请放入装备;
				} else {
					description = Translate.请放入装备和星星;
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[确认装备装星] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + equipmentId + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] [" + materialId + "] [" +
				// description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[确认装备装星] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), equipmentId, (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), materialId, description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	public String 得到装备能装的星(EquipmentEntity ee) {
		String articleName = "";
		if (ee.getStar() >= 0 && ee.getStar() < starNames.length) {
			articleName = starNames[ee.getStar()];
		}

		return articleName;
	}

	/**
	 * 装备精炼请求，返回一个确认框
	 * @param player
	 * @param req
	 * @return
	 */
	public void equipmentDevelopReq(Player player, EQUIPMENT_DEVELOP_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && req != null && aem != null) {
			long equipmentId = req.getEquipmentId();
			long materialId = req.getAssistEquipmentId();
			if (equipmentId == materialId) {
				String description = Translate.不能放入同一件装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[装备精炼请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + equipmentId + "] [" + description + "]",
				// start);
				if (logger.isWarnEnabled()) logger.warn("[装备精炼请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), equipmentId, description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity materialEntity = aem.getEntity(materialId);
			if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
				player.sendError(Translate.高级锁魂物品不能做此操作);
				return;
			}
			if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, materialEntity.getId())) {
				player.sendError(Translate.锁魂物品不能做此操作);
				return;
			}
			if (ae != null) {
				ArticleManager am = ArticleManager.getInstance();
				Equipment e = (Equipment) am.getArticle(ae.getArticleName());
				if (isChiBang(e.getEquipmentType())) {
					player.sendError(Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "精炼" } }));
					return;
				}
			}
			if (ae instanceof EquipmentEntity && materialEntity instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee1 = (EquipmentEntity) ae;
				EquipmentEntity ee2 = (EquipmentEntity) materialEntity;
				boolean bln1 = ee1.isOprate(player, false, EquipmentEntity.精炼);
				boolean bln2 = ee2.isOprate(player, false, EquipmentEntity.精炼);
				if (!bln1 || !bln2) {
					return;
				}

				ArticleManager am = ArticleManager.getInstance();
				if (am == null) {
					return;
				}
				Article article1 = am.getArticle(ae.getArticleName());
				if (!(article1 instanceof Equipment)) {
					String description = Translate.只能是装备才能精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[装备精炼请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[装备精炼请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				Article article2 = am.getArticle(materialEntity.getArticleName());
				if (!(article2 instanceof Equipment)) {
					String description = Translate.只能是装备才能被精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[装备精炼请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[装备精炼请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				Equipment e = (Equipment) am.getArticle(ae.getArticleName());
				String equipmentName = e.getDevelopEquipmentName();
				if (equipmentName == null || equipmentName.trim().equals("")) {
					String description = Translate.装备无需精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}
				Article a = am.getArticle(equipmentName);
				if (a == null) {
					String description = Translate.装备无需精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}

				int a1type = ((Equipment) article1).getEquipmentType();
				int a2type = ((Equipment) article2).getEquipmentType();
				if (a1type == EquipmentColumn.EQUIPMENT_TYPE_ChiBang || a2type == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
					logger.warn("翅膀不能精炼");
					return;
				}
				if ((a1type <= 9 && a2type >= 10) || (a1type >= 10 && a2type <= 9)) {
					String description = Translate.人物装备不能和坐骑装备混合精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}
				boolean hasEquipment = false;
				boolean hasMaterial = false;
				boolean horseEquNeedConfirm = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((Equipment) article1).getPlayerLevelLimit() < 80) {
										String description = Translate.只能是80级装备才能精炼;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									if (((EquipmentEntity) ae).getColorType() < equipment_color_紫) {
										String description = Translate.只能是紫色以上装备才能精炼;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									if (((EquipmentEntity) ae).getDevelopEXP() >= developEXPUpValue) {
										String description = Translate.装备精炼经验已满;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasEquipment = true;
								}
							}
							if (!hasMaterial) {
								int index2 = knapsack.hasArticleEntityByArticleId(materialId);
								if (index2 != -1) {
									if (((Equipment) article2).getPlayerLevelLimit() < 80) {
										String description = Translate.只能是80级装备才能被精炼;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									if (((EquipmentEntity) materialEntity).getInlayArticleIds() != null) {
										for (long l : ((EquipmentEntity) materialEntity).getInlayArticleIds()) {
											if (l > 0) {
												String description = Translate.副装备镶嵌了宝石不能用于精炼;
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
												player.addMessageToRightBag(hreq);
												return;
											}
										}
									}
									try {
										HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity((EquipmentEntity) materialEntity);
										if (entity != null) {
											for (int kk=0; kk<entity.getInlayArticleIds().length; kk++) {
												if (entity.getInlayArticleIds()[kk] > 0) {
													String description = Translate.副装备镶嵌了宝石不能用于精炼;
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
													player.addMessageToRightBag(hreq);
													return ;
												}
											}
											for (int kk=0; kk<entity.getInlayColorType().length; kk++) {
												if (entity.getInlayColorType()[kk] >= 0) {
													horseEquNeedConfirm = true;
													break;
												}
											}
										}
									} catch (Exception eee) {
										HorseManager.logger.warn("[精炼] [异常]", eee);
									}
									if (((EquipmentEntity) materialEntity).getInlayQiLingArticleIds() != null) {
										for (long l : ((EquipmentEntity) materialEntity).getInlayQiLingArticleIds()) {
											if (l > 0) {
												String description = Translate.副装备镶嵌了器灵不能用于精炼;
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
												player.addMessageToRightBag(hreq);
												return;
											}
										}
									}
									if (((EquipmentEntity) materialEntity).getColorType() < equipment_color_紫) {
										String description = Translate.只能是紫色以上装备才能被精炼;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasMaterial = true;
								}
							}
							if (hasEquipment && hasMaterial) {
								break;
							}
						}
					}
				}
				if (!((EquipmentEntity) ae).isInscriptionFlag()) {
					String description = Translate.主装备必须铭刻才能精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[装备精炼请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[装备精炼请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				if (hasEquipment && hasMaterial) {
					// 提示用户是否精炼

					WindowManager wm = WindowManager.getInstance();
					MenuWindow mw = wm.createTempMenuWindow(600);
					String title = Translate.精炼提示信息;
					mw.setTitle(title);
					String desUUB = Translate.确定要开始精炼吗;
					if (!ae.isBinded() && materialEntity.isBinded()) {
						try {
							desUUB = Translate.translateString(Translate.确定要开始精炼吗绑定的, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						mw.setDescriptionInUUB(desUUB);
						Option_EquipmentDevelopConfirm option = new Option_EquipmentDevelopConfirm();
						option.setReq(req);
						option.setText(Translate.确定);
						Option[] options = new Option[] { option, new Option_Cancel() };
						mw.setOptions(options);
						CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						player.addMessageToRightBag(creq);
						return;
					}
					if (horseEquNeedConfirm) {
						try {
							desUUB = HorseEquInlayManager.getInst().getTranslate("confirm_devloped_inlay_equ");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						mw.setDescriptionInUUB(desUUB);
						Option_EquipmentDevelopConfirm option = new Option_EquipmentDevelopConfirm();
						option.setReq(req);
						option.setText(Translate.确定);
						Option[] options = new Option[] { option, new Option_Cancel() };
						mw.setOptions(options);
						CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						player.addMessageToRightBag(creq);
						return;
					}
					confirmEquipmentDevelopReq(player, equipmentId, materialId);
					return;
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备精炼请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[装备精炼请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (!hasMaterial) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (materialEntity != null ? materialEntity.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备精炼请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (materialEntity != null ?
						// materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]",
						// start);
						if (logger.isWarnEnabled()) logger.warn("[装备精炼请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入主装备和副装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[装备精炼请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + equipmentId + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] [" + materialId + "] [" +
				// description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[装备精炼请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), equipmentId, (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), materialId, description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		} else {
			logger.error("[精炼出问题了..] [player:" + player + "] [req:" + req + "] [aem:" + aem + "]");
		}
	}

	/**
	 * 确认装备精炼
	 * @param player
	 * @param req
	 * @return
	 */
	public void confirmEquipmentDevelopReq(Player player, long equipmentId, long materialId) {
		logger.info(player.getLogString() + "[开始精炼]");
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player != null && aem != null) {
			if (equipmentId == materialId) {
				String description = Translate.不能放入同一件装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[确认装备精炼] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + equipmentId + "] [" + description + "]",
				// start);
				if (logger.isWarnEnabled()) logger.warn("[确认装备精炼] [失败] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), equipmentId, description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae instanceof EquipmentEntity && materialEntity instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee1 = (EquipmentEntity) ae;
				EquipmentEntity ee2 = (EquipmentEntity) materialEntity;
				boolean bln1 = ee1.isOprate(player, false, EquipmentEntity.精炼);
				boolean bln2 = ee2.isOprate(player, false, EquipmentEntity.精炼);
				if (!bln1 || !bln2) {
					return;
				}

				ArticleManager am = ArticleManager.getInstance();
				if (am == null) {
					return;
				}
				Article article1 = am.getArticle(ae.getArticleName());
				if (!(article1 instanceof Equipment)) {
					String description = Translate.只能是装备才能精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[确认装备精炼] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[确认装备精炼] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				Article article2 = am.getArticle(materialEntity.getArticleName());
				if (!(article2 instanceof Equipment)) {
					String description = Translate.只能是装备才能被精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[确认装备精炼] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[确认装备精炼] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				Equipment e = (Equipment) am.getArticle(ae.getArticleName());
				String equipmentName = e.getDevelopEquipmentName();
				if (equipmentName == null || equipmentName.trim().equals("")) {
					String description = Translate.装备无需精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}
				Article a = am.getArticle(equipmentName);
				if (a == null) {
					String description = Translate.装备无需精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}

				int a1type = ((Equipment) article1).getEquipmentType();
				int a2type = ((Equipment) article2).getEquipmentType();
				if (a1type == EquipmentColumn.EQUIPMENT_TYPE_ChiBang || a2type == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
					logger.warn("翅膀不能精炼");
					return;
				}
				if ((a1type <= 9 && a2type >= 10) || (a1type >= 10 && a2type <= 9)) {
					String description = Translate.人物装备不能和坐骑装备混合精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}
				boolean hasEquipment = false;
				boolean hasMaterial = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((Equipment) article1).getPlayerLevelLimit() < 80) {
										String description = Translate.只能是80级装备才能精炼;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									if (((EquipmentEntity) ae).getColorType() < equipment_color_紫) {
										String description = Translate.只能是紫色以上装备才能精炼;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									if (((EquipmentEntity) ae).getDevelopEXP() >= developEXPUpValue) {
										String description = Translate.装备精炼经验已满;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasEquipment = true;
								}
							}
							if (!hasMaterial) {
								int index2 = knapsack.hasArticleEntityByArticleId(materialId);
								if (index2 != -1) {
									if (((Equipment) article2).getPlayerLevelLimit() < 80) {
										String description = Translate.只能是80级装备才能被精炼;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									if (((EquipmentEntity) materialEntity).getInlayArticleIds() != null) {
										for (long l : ((EquipmentEntity) materialEntity).getInlayArticleIds()) {
											if (l > 0) {
												String description = Translate.副装备镶嵌了宝石不能用于精炼;
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
												player.addMessageToRightBag(hreq);
												return;
											}
										}
									}
									try {
										HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity((EquipmentEntity) materialEntity);
										if (entity != null) {
											for (int kk=0; kk<entity.getInlayArticleIds().length; kk++) {
												if (entity.getInlayArticleIds()[kk] > 0) {
													String description = Translate.副装备镶嵌了宝石不能用于精炼;
													HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
													player.addMessageToRightBag(hreq);
												}
											}
										}
									} catch (Exception eee) {
										HorseManager.logger.warn("[精炼] [异常]", eee);
									}
									if (((EquipmentEntity) materialEntity).getInlayQiLingArticleIds() != null) {
										for (long l : ((EquipmentEntity) materialEntity).getInlayQiLingArticleIds()) {
											if (l > 0) {
												String description = Translate.副装备镶嵌了器灵不能用于精炼;
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
												player.addMessageToRightBag(hreq);
												return;
											}
										}
									}
									if (((EquipmentEntity) materialEntity).getColorType() < equipment_color_紫) {
										String description = Translate.只能是紫色以上装备才能被精炼;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasMaterial = true;
								}
							}
							if (hasEquipment && hasMaterial) {
								break;
							}
						}
					}
				}
				if (!((EquipmentEntity) ae).isInscriptionFlag()) {
					String description = Translate.主装备必须铭刻才能精炼;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[确认装备精炼] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[确认装备精炼] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				if (hasEquipment && hasMaterial) {
					ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(materialId, "精炼删除", true);
					logger.info(player.getLogString() + "[开始精炼] [物品:" + aee + "]");
					if (aee != null) {

						// 统计
						ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "精炼删除", null);

						if (!ae.isBinded() && materialEntity.isBinded()) {
							ae.setBinded(true);
						}
						// 精炼
						int developEXP = 100 / 得到精炼经验((EquipmentEntity) ae, (EquipmentEntity) materialEntity, (Equipment) article1, (Equipment) article2);
						logger.info(player.getLogString() + "[精炼经验] [增加前] [增加经验:" + developEXP + "/" + ((EquipmentEntity) ae).getDevelopEXP() + "] ");
						int oldDevelopEXP = ((EquipmentEntity) ae).getDevelopEXP();
						int newDevelopEXP = oldDevelopEXP + developEXP;
						if (newDevelopEXP < developEXPUpValue) {
							((EquipmentEntity) ae).setDevelopEXP(newDevelopEXP);
						} else {
							((EquipmentEntity) ae).setDevelopEXP(developEXPUpValue);
						}
						logger.info(player.getLogString() + "[精炼经验] [增加后] [增加经验:" + developEXP + "/" + ((EquipmentEntity) ae).getDevelopEXP() + "] ");
						PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备精炼绑定铭刻成功, (byte) 1, AnimationManager.动画播放位置类型_精炼绑定铭刻, 0, 0);
						if (pareq != null) {
							player.addMessageToRightBag(pareq);
						}
						String description = Translate.精炼成功;
						try {
							description = Translate.translateString(Translate.精炼成功提示, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.COUNT_1, (((EquipmentEntity) ae).getDevelopEXP() - oldDevelopEXP) + "" } });
						} catch (Exception ex) {
							logger.error("精炼异常", e);
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
						player.addMessageToRightBag(hreq);
						NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate((EquipmentEntity) ae) });
						player.addMessageToRightBag(nreq);
						EQUIPMENT_DEVELOP_RES res = new EQUIPMENT_DEVELOP_RES(GameMessageFactory.nextSequnceNum());
						player.addMessageToRightBag(res);
						if (logger.isInfoEnabled()) {
							logger.info("[确认装备精炼] [成功] [增加经验:" + developEXP + "/" + ((EquipmentEntity) ae).getDevelopEXP() + "] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName(), ae.getId(), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						}
						return;
					} else {
						String description = Translate.删除物品失败;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备精炼] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "id为" + materialId + "的物品没有删除成功]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备精炼] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品没有删除成功] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, materialId, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备精炼] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备精炼] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (!hasMaterial) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (materialEntity != null ? materialEntity.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							logger.error("[精炼异常]", ex);
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备精炼] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (materialEntity != null ?
						// materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "]",
						// start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备精炼] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入主装备和副装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[确认装备精炼] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + equipmentId + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] [" + materialId + "] [" +
				// description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[确认装备精炼] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), equipmentId, (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), materialId, description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 精炼公式=主装备等级^2/副装备等级^2*4*(主装备颜色等级/副装备颜色等级)
	 * @param ee1
	 * @param ee2
	 * @param a1
	 * @param a2
	 * @return
	 */
	public int 得到精炼经验(EquipmentEntity ee1, EquipmentEntity ee2, Equipment a1, Equipment a2) {
		double exp = a1.getPlayerLevelLimit() * a1.getPlayerLevelLimit() * 4 * ee1.getColorType() / (a2.getPlayerLevelLimit() * a2.getPlayerLevelLimit() * ee2.getColorType());
		if (exp < 1) {
			return 1;
		}
		return (int) exp;
	}

	/**
	 * 根据绑定请求返回绑定响应
	 * @param req
	 * @return
	 */
	public QUERY_EQUIPMENT_BIND_RES queryEquipmentBindReq(Player player, QUERY_EQUIPMENT_BIND_REQ req) {
		QUERY_EQUIPMENT_BIND_RES res = null;
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && req != null && aem != null) {
			ArticleEntity ae = aem.getEntity(req.getEquipmentId());
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.绑定);
				if (!bln) {
					return null;
				}

				String description = Translate.空白;
				try {
					description = Translate.translateString(Translate.装备绑定花费, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(绑定装备所需花费((EquipmentEntity) ae)) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				ArticleManager am = ArticleManager.getInstance();
				Equipment e = (Equipment) am.getArticle(ae.getArticleName());
				res = new QUERY_EQUIPMENT_BIND_RES(req.getSequnceNum(), req.getEquipmentId(), description);
			} else {
				String description = Translate.空白;
				try {
					description = Translate.translateString(Translate.查询装备绑定请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.空白) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[查询装备绑定] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
		return res;
	}

	/**
	 * 装备绑定请求，返回一个确认框
	 * @param player
	 * @param req
	 * @return
	 */
	public void equipmentBindReq(Player player, EQUIPMENT_BIND_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && req != null && aem != null) {
			long equipmentId = req.getEquipmentId();
			// long materialId = req.getMaterialId();
			ArticleEntity ae = aem.getEntity(equipmentId);
			// ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.绑定);
				if (!bln) {
					return;
				}

				int cost = 绑定装备所需花费((EquipmentEntity) ae);
				if (!player.bindSilverEnough(cost)) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.提示装备绑定花费金币不足, new String[][] { { Translate.COUNT_1, cost + "" } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备绑定请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				boolean hasEquipment = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((EquipmentEntity) ae).isBinded()) {
										String description = Translate.此装备已经绑定不需要执行此操作;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasEquipment = true;
									break;
								}
							}
						}
					}
				}
				if (hasEquipment) {
					// ArticleManager am = ArticleManager.getInstance();
					// Equipment e = (Equipment)am.getArticle(ae.getArticleName());
					// // String[] materials = 根据装备级别得到所需绑定符(e.getPlayerLevelLimit());
					// //提示用户是否铭刻
					//
					// WindowManager wm = WindowManager.getInstance();
					// MenuWindow mw = wm.createTempMenuWindow(600);
					// String title = Translate.绑定提示信息;
					// mw.setTitle(title);
					// String desUUB = Translate.确定要开始绑定吗;
					// mw.setDescriptionInUUB(desUUB);
					// Option_EquipmentBindConfirm option = new Option_EquipmentBindConfirm();
					// option.setReq(req);
					// option.setText(Translate.确定);
					// Option[] options = new Option[]{option,new Option_Cancel()};
					// mw.setOptions(options);
					// CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getDescriptionInUUB(),options);
					// player.addMessageToRightBag(creq);
					confirmEquipmentBindReq(player, equipmentId);
					return;
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备绑定请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[装备绑定请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[装备绑定请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[装备绑定请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 确认装备绑定
	 * @param player
	 * @param req
	 * @return
	 */
	public void confirmEquipmentBindReq(Player player, long equipmentId) {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player != null && aem != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);
			// ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.绑定);
				if (!bln) {
					return;
				}

				int cost = 绑定装备所需花费((EquipmentEntity) ae);
				if (!player.bindSilverEnough(cost)) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.提示装备绑定花费金币不足, new String[][] { { Translate.COUNT_1, cost + "" } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[确认装备绑定] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[确认装备绑定] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				boolean hasEquipment = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((EquipmentEntity) ae).isBinded()) {
										String description = Translate.此装备已经绑定不需要执行此操作;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									hasEquipment = true;
									break;
								}
							}
						}
					}
				}
				if (hasEquipment) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					// String[] materials = 根据装备级别得到所需绑定符(e.getPlayerLevelLimit());
					// ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(materialId);
					// 扣钱
					try {
						BillingCenter bc = BillingCenter.getInstance();
						bc.playerExpense(player, cost, CurrencyType.BIND_YINZI, ExpenseReasonType.EQUIPMENT_BIND, "使用绑银装备绑定", VipExpActivityManager.default_add_rmb_expense);
					} catch (Exception ex) {
						ex.printStackTrace();
						if (logger.isWarnEnabled()) logger.warn("[确认装备绑定] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
						return;
					}
					ae.setBinded(true);
					PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备精炼绑定铭刻成功, (byte) 1, AnimationManager.动画播放位置类型_精炼绑定铭刻, 0, 0);
					if (pareq != null) {
						player.addMessageToRightBag(pareq);
					}
					String description = Translate.绑定完成;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
					player.addMessageToRightBag(hreq);
					NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate((EquipmentEntity) ae) });
					player.addMessageToRightBag(nreq);
					if (logger.isInfoEnabled()) {
						// logger.info("[确认装备绑定] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + ae.getArticleName() + "] [" +
						// ae.getId() + "] [" + description + "]", start);
						if (logger.isInfoEnabled()) logger.info("[确认装备绑定] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName(), ae.getId(), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
					return;
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认装备绑定] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认装备绑定] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[确认装备绑定] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[确认装备绑定] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	public int 绑定装备所需花费(EquipmentEntity ee) {
		return 0;
	}

	/**
	 * 装备精炼升级请求，返回一个确认框
	 * @param player
	 * @param req
	 * @return
	 */
	public void equipmentDevelopUpReq(Player player, EQUIPMENT_DEVELOPUP_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (player != null && req != null && aem != null) {
			long equipmentId = req.getEquipmentId();
			ArticleEntity ae = aem.getEntity(equipmentId);
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.升级);
				if (!bln) {
					return;
				}

				ArticleManager am = ArticleManager.getInstance();
				Equipment e = (Equipment) am.getArticle(ae.getArticleName());
				String equipmentName = e.getDevelopEquipmentName();
				if (equipmentName == null || equipmentName.trim().equals("")) {
					String description = Translate.装备无需升级;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}
				Article a = am.getArticle(equipmentName);
				if (a == null) {
					String description = Translate.装备无需升级;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					return;
				}
				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, ae.getId())) {
					player.sendError(Translate.锁魂物品不能做此操作);
					return;
				}
				int cost = 精炼升级花费((EquipmentEntity) ae);
				if (!player.bindSilverEnough(cost)) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.提示精炼升级花费金币不足, new String[][] { { Translate.COUNT_1, cost + "" } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[装备绑定请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[装备绑定请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				boolean hasEquipment = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((EquipmentEntity) ae).getDevelopEXP() < developEXPUpValue) {
										String description = Translate.此装备还没有精炼完成;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									// if (((EquipmentEntity) ae).getLevel() > 0) {
									// String description = Translate.装备升级需要摘除当前星级;
									// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									// player.addMessageToRightBag(hreq);
									// return;
									// }
									if (((EquipmentEntity) ae).getInlayArticleIds() != null) {
										for (long l : ((EquipmentEntity) ae).getInlayArticleIds()) {
											if (l > 0) {
												String description = Translate.装备镶嵌了宝石不能升级;
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
												player.addMessageToRightBag(hreq);
												return;
											}
										}
									}
									if (((EquipmentEntity) ae).getInlayQiLingArticleIds() != null) {
										for (long l : ((EquipmentEntity) ae).getInlayQiLingArticleIds()) {
											if (l > 0) {
												String description = Translate.装备镶嵌了器灵不能升级;
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
												player.addMessageToRightBag(hreq);
												return;
											}
										}
									}
									hasEquipment = true;
									break;
								}
							}
						}
					}
				}
				if (hasEquipment) {

					// 提示用户是否升级

					WindowManager wm = WindowManager.getInstance();
					MenuWindow mw = wm.createTempMenuWindow(600);
					String title = Translate.升级提示信息;
					mw.setTitle(title);
					String desUUB = Translate.确定要开始升级装备吗;
					mw.setDescriptionInUUB(desUUB);
					Option_EquipmentUpConfirm option = new Option_EquipmentUpConfirm();
					option.setReq(req);
					option.setText(Translate.确定);
					Option[] options = new Option[] { option, new Option_Cancel() };
					mw.setOptions(options);
					CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
					player.addMessageToRightBag(creq);
					return;
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[装备升级请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[装备升级请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[装备升级请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[装备升级请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 确认装备精炼升级
	 * @param player
	 * @param req
	 * @return
	 */
	public void confirmEquipmentDevelopUpReq(Player player, long equipmentId) {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (player != null && aem != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.升级);
				if (!bln) {
					return;
				}

				if (ae != null) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					if (isChiBang(e.getEquipmentType())) {
						player.sendError(Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "升级" } }));
						return;
					}
				}

				int cost = 精炼升级花费((EquipmentEntity) ae);
				if (!player.bindSilverEnough(cost)) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.提示精炼升级花费金币不足, new String[][] { { Translate.COUNT_1, cost + "" } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[操作类别:确认装备精炼升级] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[操作类别:确认装备精炼升级] [状态:失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				boolean hasEquipment = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									if (((EquipmentEntity) ae).getDevelopEXP() < developEXPUpValue) {
										String description = Translate.此装备还没有精炼完成;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										return;
									}
									// 已和运营确认限制取消
									// if (PlatformManager.getInstance().isPlatformOf(Platform.台湾, Platform.马来, Platform.韩国)) {
									// if (((EquipmentEntity) ae).getLevel() > 0) {
									// String description = Translate.装备升级需要摘除当前星级;
									// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
									// player.addMessageToRightBag(hreq);
									// return;
									// }
									// }
									if (((EquipmentEntity) ae).getInlayArticleIds() != null) {
										for (long l : ((EquipmentEntity) ae).getInlayArticleIds()) {
											if (l > 0) {
												String description = Translate.装备镶嵌了宝石不能升级;
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
												player.addMessageToRightBag(hreq);
												return;
											}
										}
									}
									if (((EquipmentEntity) ae).getInlayQiLingArticleIds() != null) {
										for (long l : ((EquipmentEntity) ae).getInlayQiLingArticleIds()) {
											if (l > 0) {
												String description = Translate.装备镶嵌了器灵不能升级;
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
												player.addMessageToRightBag(hreq);
												return;
											}
										}
									}
									hasEquipment = true;
									break;
								}
							}
						}
					}
				}
				if (hasEquipment) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					String equipmentName = e.getDevelopEquipmentName();
					if (equipmentName == null || equipmentName.trim().equals("")) {
						String description = Translate.装备无需升级;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						return;
					}
					Article a = am.getArticle(equipmentName);
					if (a == null) {
						String description = Translate.装备无需升级;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						return;
					}

					if (cost > 0) {
						// 扣钱
						try {
							BillingCenter bc = BillingCenter.getInstance();
							bc.playerExpense(player, cost, CurrencyType.BIND_YINZI, ExpenseReasonType.EQUIPMENT_LIANHUA, "使用绑银装备精炼", VipExpActivityManager.default_add_rmb_expense);
						} catch (Exception ex) {
							ex.printStackTrace();
							if (logger.isWarnEnabled()) logger.warn("[操作类别:确认装备精炼升级] [状态:失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
							return;
						}
					}

					ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(equipmentId, "装备升级删除", true);

					if (aee != null && e != null) {

						// 统计
						ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "装备升级删除", null);

						try {
							ee = (EquipmentEntity) aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_装备升级, player, ((EquipmentEntity) ae).getColorType(), 1, true);
							ee.setInscriptionFlag(true);
							ee.setStar(((EquipmentEntity) ae).getStar());
							ee.setCreaterName(player.getName());
							if (player.putToKnapsacks(ee, "装备升级获得")) {
								PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备精炼绑定铭刻成功, (byte) 1, AnimationManager.动画播放位置类型_精炼绑定铭刻, 0, 0);
								if (pareq != null) {
									player.addMessageToRightBag(pareq);
								}
								String description = Translate.装备升级完成;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
								player.addMessageToRightBag(hreq);

								// 统计
								ArticleStatManager.addToArticleStat(player, null, ee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "装备升级获得", null);

								if (logger.isInfoEnabled()) {
									// logger.info("[操作类别:确认装备精炼升级] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + ae.getArticleName()
									// +
									// "] [" + ae.getId() + "] [" + description + "]", start);
									if (logger.isInfoEnabled()) logger.info("[操作类别:确认装备精炼升级] [状态:成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName(), ae.getId(), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								}
							} else {
								String description = Translate.装备升级成功但背包满了;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isInfoEnabled()) {
									// logger.info("[操作类别:确认装备精炼升级] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + ae.getArticleName()
									// +
									// "] [" + ae.getId() + "] [" + description + "]", start);
									if (logger.isInfoEnabled()) logger.info("[操作类别:确认装备精炼升级] [状态:成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName(), ae.getId(), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								}
							}
						} catch (Exception ex) {
							if (logger.isWarnEnabled()) logger.warn("[操作类别:确认装备精炼升级] [状态:失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
							return;
						}
						return;
					}
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[操作类别:确认装备精炼升级] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ?
						// ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[操作类别:确认装备精炼升级] [状态:失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[操作类别:确认装备精炼升级] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[操作类别:确认装备精炼升级] [状态:失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	public int 精炼升级花费(EquipmentEntity ee) {
		int cost = 0;

		return cost;
	}

	/**
	 * 镶嵌
	 * @param player
	 * @param req
	 */
	public void equipmentInlayReq(Player player, EQUIPMENT_INLAY_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && req != null && aem != null && am != null) {
			long equipmentId = req.getEquipmentId();
			long materialId = req.getMaterialId();
			int index = req.getHoleIndex();
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae instanceof EquipmentEntity && materialEntity != null) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.镶嵌);
				if (!bln) {
					return;
				}

				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
					player.sendError(Translate.高级锁魂物品不能做此操作);
					return;
				}

				if (!(am.getArticle(materialEntity.getArticleName()) instanceof InlayArticle)) {
					String description = Translate.镶嵌物品必须为宝石;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[镶嵌] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] ["
					// + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[镶嵌] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				boolean hasEquipment = false;
				boolean hasMaterial = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									hasEquipment = true;
								}
							}
							if (!hasMaterial) {
								int index2 = knapsack.hasArticleEntityByArticleId(materialId);
								if (index2 != -1) {
									hasMaterial = true;
								}
							}
							if (hasEquipment && hasMaterial) {
								break;
							}
						}
					}
				}
				if (hasEquipment && hasMaterial) {
					ee = (EquipmentEntity) ae;
					long[] inlays = ee.getInlayArticleIds();
					int[] inlayColors = ee.getInlayArticleColors();
					InlayArticle ia = (InlayArticle) am.getArticle(materialEntity.getArticleName());

					if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, materialEntity.getId())) {
						player.sendError(Translate.锁魂物品不能做此操作);
						return;
					}

					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					if (ia.getInlayType() > 1) {
						int classlevel = -1;
						BaoShiXiaZiData data = getxiLianData(player, materialId);
						if (data != null) {
							if (data.getNames() != null && data.getNames().length > 0) {
								for (String name : data.getNames()) {
									if (name != null && !name.isEmpty()) {
										Article a = ArticleManager.getInstance().getArticle(name);
										if (a != null && a instanceof InlayArticle) {
											if (classlevel < ((InlayArticle) a).getClassLevel()) {
												classlevel = ((InlayArticle) a).getClassLevel();
											}
										}
									}
								}
							}
						}
						logger.warn("[镶嵌测试] [inlaytype:" + ia.getInlayType() + "] [5] [classlevel:" + classlevel + "] [eclasslimit:" + e.getClassLimit() + "] [" + Arrays.toString(data.getNames()) + "]");
						if (classlevel > e.getClassLimit()) {
							String description = Translate.境界不够不能镶嵌;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							if (logger.isWarnEnabled()) logger.warn("[镶嵌] [镶嵌匣子] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [inlayArticleClassLevel:{}] [eeClassLevel:{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, ia.getClassLevel(), e.getClassLimit(), SystemTime.currentTimeMillis() - start });
							return;
						}
					}
					// 宝石境界限制
					if (ia.getClassLevel() > e.getClassLimit()) {
						String description = Translate.境界不够不能镶嵌;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [inlayArticleClassLevel:{}] [eeClassLevel:{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, ia.getClassLevel(), e.getClassLimit(), SystemTime.currentTimeMillis() - start });
						return;
					}

					if (inlays == null || inlays.length <= index || inlayColors == null || inlayColors.length <= index) {
						String description = Translate.装备不能镶嵌或镶嵌位置错误;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (inlays[index] > 0) {
						String description = Translate.镶嵌位置已有宝石;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[镶嵌] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) +
						// "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (inlayColors[index] != ia.getInlayColorType()) {
						String description = Translate.镶嵌宝石颜色和孔颜色不一致;
						try {
							description = Translate.translateString(Translate.镶嵌宝石颜色和孔颜色不一致描述, new String[][] { { Translate.COLOR_1, getInlayColorString(ia.getInlayColorType()) }, { Translate.COLOR_2, getInlayColorString(inlayColors[index]) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[镶嵌] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) +
						// "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}

					// 提示
					WindowManager wm = WindowManager.getInstance();
					MenuWindow mw = wm.createTempMenuWindow(600);
					String title = Translate.镶嵌提示信息;
					mw.setTitle(title);
					String desUUB = Translate.镶嵌提示信息;
					try {
						InlayArticle inlayA = (InlayArticle)am.getArticle(materialEntity.getArticleName());
						desUUB = Translate.translateString(Translate.确定要镶嵌吗, new String[][] { { Translate.ARTICLE_NAME_1, inlayA.getShowName() }, { Translate.ARTICLE_NAME_2, ae.getArticleName() } });
					} catch (Exception ex) {
						desUUB = Translate.translateString(Translate.确定要镶嵌吗, new String[][] { { Translate.ARTICLE_NAME_1, materialEntity.getShowName() }, { Translate.ARTICLE_NAME_2, ae.getArticleName() } });
						ex.printStackTrace();
					}
					if ((!ae.isBinded() && materialEntity.isBinded()) || (!ae.isBinded() && materialEntity.isRealBinded())) {
						try {
							InlayArticle inlayAA = (InlayArticle)am.getArticle(materialEntity.getArticleName());
							if (ia.getInlayType() > 1) {
								desUUB = Translate.translateString(Translate.匣子中含有绑定宝石, new String[][] { { Translate.ARTICLE_NAME_1, materialEntity.getArticleName() }, { Translate.ARTICLE_NAME_2, ae.getArticleName() } });
							} else {
								desUUB = Translate.translateString(Translate.确定要镶嵌吗绑定的, new String[][] { { Translate.ARTICLE_NAME_1,inlayAA.getShowName() }, { Translate.ARTICLE_NAME_2, ae.getArticleName() } });
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					int protectState = ArticleProtectManager.instance.getBlockState(player, materialEntity.getId());
					if (protectState > 0) {
						desUUB += "\n" + Translate.translateString(Translate.是锁魂的镶嵌会导致也锁魂, new String[][] { { Translate.STRING_1, materialEntity.getArticleName() }, { Translate.STRING_2, ae.getArticleName() } });
					}

					mw.setDescriptionInUUB(desUUB);
					Option_InlayConfirm option = new Option_InlayConfirm();
					option.setReq(req);
					option.setText(Translate.确定);
					Option[] options = new Option[] { option, new Option_Cancel() };
					mw.setOptions(options);
					CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
					player.addMessageToRightBag(creq);
					return;
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[镶嵌] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (!hasMaterial) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (materialEntity != null ? materialEntity.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[镶嵌] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (materialEntity != null ?
						// materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] ["
						// + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备和宝石;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[镶嵌] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白)
				// + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity
				// != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[镶嵌] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 镶嵌确认
	 * @param player
	 * @param req
	 */
	public void confirmEquipmentInlayReq(Player player, long equipmentId, long materialId, int index) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && aem != null && am != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity materialEntity = aem.getEntity(materialId);
			if (ae instanceof EquipmentEntity && materialEntity != null) {
				if (ArticleManager.logger.isWarnEnabled()) {
					ArticleManager.logger.warn("[镶嵌宝石前] " + player.getPlayerPropsString());
				}

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.镶嵌);
				if (!bln) {
					return;
				}

				if (!(am.getArticle(materialEntity.getArticleName()) instanceof InlayArticle)) {
					String description = Translate.镶嵌物品必须为宝石;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[镶嵌确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] ["
					// + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[镶嵌确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				boolean hasEquipment = false;
				boolean hasMaterial = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									hasEquipment = true;
								}
							}
							if (!hasMaterial) {
								int index2 = knapsack.hasArticleEntityByArticleId(materialId);
								if (index2 != -1) {
									hasMaterial = true;
								}
							}
							if (hasEquipment && hasMaterial) {
								break;
							}
						}
					}
				}
				if (hasEquipment && hasMaterial) {
					ee = (EquipmentEntity) ae;
					long[] inlays = ee.getInlayArticleIds();
					int[] inlayColors = ee.getInlayArticleColors();
					InlayArticle ia = (InlayArticle) am.getArticle(materialEntity.getArticleName());

					Equipment e = (Equipment) am.getArticle(ae.getArticleName());

					if (ia.getInlayType() > 1) {
						int classlevel = 0;
						BaoShiXiaZiData data = getxiLianData(player, materialId);
						if (data != null) {
							if (data.getNames() != null && data.getNames().length > 0) {
								for (String name : data.getNames()) {
									if (name != null && !name.isEmpty()) {
										Article a = ArticleManager.getInstance().getArticle(name);
										if (a != null && a instanceof InlayArticle) {
											if (classlevel < ((InlayArticle) a).getClassLevel()) {
												classlevel = ((InlayArticle) a).getClassLevel();
											}
										}
									}
								}
							}
						}
						if (classlevel > e.getClassLimit()) {
							String description = Translate.境界不够不能镶嵌;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							if (logger.isWarnEnabled()) logger.warn("[镶嵌] [镶嵌匣子] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [inlayArticleClassLevel:{}] [eeClassLevel:{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, ia.getClassLevel(), e.getClassLimit(), SystemTime.currentTimeMillis() - start });
							return;
						}
					}

					// 宝石境界限制
					if (ia.getClassLevel() > e.getClassLimit()) {
						String description = Translate.境界不够不能镶嵌;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [inlayArticleClassLevel:{}] [eeClassLevel:{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, ia.getClassLevel(), e.getClassLimit(), SystemTime.currentTimeMillis() - start });
						return;
					}

					if (ae != null) {
						if (isChiBang(e.getEquipmentType())) {
							player.sendError(Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "镶嵌" } }));
							return;
						}
					}

					if (inlays == null || inlays.length <= index || inlayColors == null || inlayColors.length <= index) {
						String description = Translate.装备不能镶嵌或镶嵌位置错误;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[镶嵌确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) +
						// "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (inlays[index] > 0) {
						String description = Translate.镶嵌位置已有宝石;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[镶嵌确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) +
						// "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (inlayColors[index] != ia.getInlayColorType()) {
						String description = Translate.镶嵌宝石颜色和孔颜色不一致;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[镶嵌确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) +
						// "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}

					ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(materialId, "镶嵌", false);
					if (aee != null) {
						inlays[index] = materialId;
						ee.setInlayArticleIds(inlays);
						if ((!ae.isBinded() && materialEntity.isBinded()) || (!ae.isBinded() && materialEntity.isRealBinded())) {
							ae.setBinded(true);
						}

						int protectState = ArticleProtectManager.instance.getBlockState(player, aee.getId());
						if (protectState > 0) {
							// 宝石锁魂了把装备也锁魂
							ArticleProtectManager.instance.blockOne(player, ae, protectState, ArticleProtectDataValues.ArticleProtect_ArticleType_Equpment);
						}

						// 宝石镶嵌成功
						PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备镶嵌成功, (byte) 1, AnimationManager.动画播放位置类型_镶嵌, 0, 0);
						if (pareq != null) {
							player.addMessageToRightBag(pareq);
						}
						String description = Translate.镶嵌完成;
						try {
							InlayArticle inlayAA = (InlayArticle)am.getArticle(materialEntity.getArticleName());
							description = Translate.translateString(Translate.镶嵌完成宝石位置信息, new String[][] { { Translate.ARTICLE_NAME_1, inlayAA.getShowName() }, { Translate.INDEX_1, (index + 1) + "" } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
						player.addMessageToRightBag(hreq);
						NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
						player.addMessageToRightBag(nreq);

						StringBuffer sb = new StringBuffer();
						ee.getInlayInfo(player, sb);
						EQUIPMENT_INLAY_UUB_RES nires = new EQUIPMENT_INLAY_UUB_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), sb.toString());
						player.addMessageToRightBag(nires);

						if (logger.isInfoEnabled()) {
							// logger.info("[镶嵌确认] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
							// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白)
							// + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] [" + index + "] ["+sb.toString()+"]", start);
							if (logger.isInfoEnabled()) logger.info("[操作类别:镶嵌确认] [状态:成功] [username:{}] [id:{}] [name:{}] [ae:{}] [articleId:{}] [道具名字:{}] [道具id:{}] [结果:{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, sb.toString(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						}
						if (ArticleManager.logger.isWarnEnabled()) {
							ArticleManager.logger.warn("[镶嵌宝石后] " + player.getPlayerPropsString());
						}
						try {
							EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.镶嵌宝石次数, 1L });
							EventRouter.getInst().addEvent(evt);
							Article tempA = ArticleManager.getInstance().getArticle(aee.getArticleName());
							if (tempA.getArticleLevel() == 2) {
								EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.镶嵌一颗3级宝石, 1L });
								EventRouter.getInst().addEvent(evt2);
							}

						} catch (Exception eex) {
							PlayerAimManager.logger.error("[目标系统] [统计玩家镶嵌次数异常] [" + player.getLogString() + "]", eex);
						}
						return;
					}

				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[镶嵌确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (!hasMaterial) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (materialEntity != null ? materialEntity.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[镶嵌确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (materialEntity != null ?
						// materialEntity.getArticleName() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] ["
						// + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[镶嵌确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备和宝石;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[镶嵌确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (materialEntity != null ? materialEntity.getArticleName() : Translate.空白) + "] [" +
				// (materialEntity != null ? materialEntity.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[镶嵌确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (materialEntity != null ? materialEntity.getArticleName() : Translate.空白), (materialEntity != null ? materialEntity.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 拆除宝石请求
	 * @param player
	 * @param req
	 */
	public void equipmentOutlayReq(Player player, EQUIPMENT_OUTLAY_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && req != null && aem != null && am != null) {
			long equipmentId = req.getEquipmentId();
			int index = req.getHoleIndex();
			ArticleEntity ae = aem.getEntity(equipmentId);
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.挖宝石);
				if (!bln) {
					return;
				}

				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
					player.sendError(Translate.高级锁魂物品不能做此操作);
					return;
				}

				boolean hasEquipment = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									hasEquipment = true;
									break;
								}
							}
						}
					}
				}
				if (hasEquipment) {
					ee = (EquipmentEntity) ae;
					long[] inlays = ee.getInlayArticleIds();
					if (inlays == null || inlays.length <= index) {
						String description = Translate.挖取宝石位置错误;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[拆除宝石请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[拆除宝石请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (inlays[index] <= 0) {
						String description = Translate.挖取位置上没有宝石;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[拆除宝石请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[拆除宝石请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					long inlayArticleId = inlays[index];
					ArticleEntity inlayEntity = aem.getEntity(inlayArticleId);
					if (inlayEntity != null) {
						Article ia = am.getArticle(inlayEntity.getArticleName());
						if (ia instanceof InlayArticle) {
							// String[] materials = 根据宝石级别得到所需挖取符(((InlayArticle) ia).getArticleLevel());
							// if (materials != null && materials.length > 0) {
							// String material = materials[0];
							// boolean hasMaterial = false;
							// if (knapsacks != null) {
							// for (Knapsack knapsack : knapsacks) {
							// if (knapsack != null) {
							// if (!hasMaterial) {
							// int index2 = knapsack.getArticleCellPos(material);
							// if (index2 != -1) {
							// hasMaterial = true;
							// break;
							// }
							// }
							// }
							// }
							// }
							// if (hasMaterial) {
							// WindowManager wm = WindowManager.getInstance();
							// MenuWindow mw = wm.createTempMenuWindow(600);
							// String title = Translate.挖取提示信息;
							// mw.setTitle(title);
							// String desUUB = Translate.挖取提示信息;
							// try{
							// desUUB = Translate.translateString(Translate.确定要挖取吗,new
							// String[][]{{Translate.ARTICLE_NAME_1,inlayEntity.getArticleName()},{Translate.ARTICLE_NAME_2,material}});
							// }catch(Exception ex){
							// ex.printStackTrace();
							// }
							// mw.setDescriptionInUUB(desUUB);
							// Option_OutlayConfirm option = new Option_OutlayConfirm();
							// option.setReq(req);
							// option.setText(Translate.确定);
							// Option[] options = new Option[]{option,new Option_Cancel()};
							// mw.setOptions(options);
							// CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getDescriptionInUUB(),options);
							// player.addMessageToRightBag(creq);
							confirmEquipmentOutlayReq(player, equipmentId, index);
							return;
							// } else {
							// String description = Translate.没有挖取道具;
							// try {
							// description = Translate.translateString(Translate.没有挖取道具提示, new String[][] { { Translate.ARTICLE_NAME_1, inlayEntity.getArticleName() }, {
							// Translate.ARTICLE_NAME_2, material } });
							// } catch (Exception ex) {
							// ex.printStackTrace();
							// }
							// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							// player.addMessageToRightBag(hreq);
							// logger.warn("[拆除宝石请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName()
							// : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
							// return;
							// }
						}
						// } else {
						// String description = Translate.服务器物品出现错误;
						// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						// player.addMessageToRightBag(hreq);
						// logger.warn("[拆除宝石请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "id为" + inlayArticleId + "的物品不是宝石] [" + index + "]", start);
						// return;
						// }
					} else {
						String description = Translate.服务器物品出现错误;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[拆除宝石请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "id为" + inlayArticleId + "的物品为空] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[拆除宝石请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品为空] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, inlayArticleId, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[拆除宝石请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[拆除宝石请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[拆除宝石请求] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[拆除宝石请求] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 确认拆除宝石
	 * @param player
	 * @param req
	 */
	public void confirmEquipmentOutlayReq(Player player, long equipmentId, int index) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && aem != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);
			if (ae instanceof EquipmentEntity) {
				if (ArticleManager.logger.isWarnEnabled()) {
					ArticleManager.logger.warn("[挖取宝石前] " + player.getPlayerPropsString());
				}
				boolean hasEquipment = false;
				Knapsack[] knapsacks = player.getKnapsacks_common();
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasEquipment) {
								int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
								if (index1 != -1) {
									hasEquipment = true;
									break;
								}
							}
						}
					}
				}
				if (hasEquipment) {
					EquipmentEntity ee = (EquipmentEntity) ae;
					long[] inlays = ee.getInlayArticleIds();
					if (inlays == null || inlays.length <= index) {
						String description = Translate.挖取宝石位置错误;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认拆除宝石] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认拆除宝石] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					if (ae != null) {
						Equipment e = (Equipment) am.getArticle(ae.getArticleName());
						if (isChiBang(e.getEquipmentType())) {
							player.sendError(Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "挖取宝石" } }));
							return;
						}
					}
					if (inlays[index] <= 0) {
						String description = Translate.挖取位置上没有宝石;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认拆除宝石] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认拆除宝石] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					long inlayArticleId = inlays[index];
					ArticleEntity inlayEntity = aem.getEntity(inlayArticleId);
					if (inlayEntity != null) {
						Article ia = am.getArticle(inlayEntity.getArticleName());
						if (ia instanceof InlayArticle) {
							Knapsack ks = player.getKnapsack_common();
							if (ks == null) {
								String description = Translate.服务器物品出现错误;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								// logger.warn("[确认拆除宝石] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ?
								// ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "找不到放置物品的背包] [" + index + "]",
								// start);
								if (logger.isWarnEnabled()) logger.warn("[确认拆除宝石] [失败] [{}] [{}] [{}] [{}] [{}] [{}找不到放置物品的背包] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							if (ks.isFull()) {
								String description = Translate.背包已满;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								// logger.warn("[确认拆除宝石] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ?
								// ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
								if (logger.isWarnEnabled()) logger.warn("[确认拆除宝石] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							// String[] materials = 根据宝石级别得到所需挖取符(((InlayArticle) ia).getArticleLevel());
							// if (materials != null && materials.length >= 0) {
							// String material = materials[0];
							// boolean hasMaterial = false;
							// if (knapsacks != null) {
							// for (Knapsack knapsack : knapsacks) {
							// if (knapsack != null) {
							// if (!hasMaterial) {
							// int index2 = knapsack.getArticleCellPos(material);
							// if (index2 != -1) {
							// hasMaterial = true;
							// break;
							// }
							// }
							// }
							// }
							// }
							// if (hasMaterial) {
							// if (knapsacks != null) {
							// for (Knapsack knapsack : knapsacks) {
							// if (knapsack != null) {
							// // 优先扣除绑定物品
							// int index2 = knapsack.indexOf(material, true);
							// if (index2 != -1) {
							// ArticleEntity aee = knapsack.remove(index2);
							// if (aee != null) {
							inlays[index] = -1;
							ee.setInlayArticleIds(inlays);
							NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
							player.addMessageToRightBag(nreq);
							// if (!inlayEntity.isBinded()) {
							// inlayEntity.setBinded(true);
							// }

							StringBuffer sb = new StringBuffer();
							ee.getInlayInfo(player, sb);
							EQUIPMENT_INLAY_UUB_RES nires = new EQUIPMENT_INLAY_UUB_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), sb.toString());
							player.addMessageToRightBag(nires);
							// try{
							// MailManager mm = MailManager.getInstance();
							// if(mm != null){
							// mm.sendMail(player.getId(), new ArticleEntity[]{inlayEntity}, Translate.宝石, Translate.挖取宝石回复, 0, 0, 0);
							// if(logger.isInfoEnabled()){
							// logger.info("[确认拆除宝石] [成功] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
							// ae.getArticleName():Translate.空白)+"] ["+(ae != null?
							// ae.getId():Translate.空白)+"] ["+(inlayEntity != null? inlayEntity.getArticleName():Translate.空白)+"] ["+(inlayEntity != null?
							// inlayEntity.getId():Translate.空白)+"] ["+index+"]",
							// start);
							// }
							// }
							// }catch(Exception ex){
							// ex.printStackTrace();
							// }
							if (player.putToKnapsacks(inlayEntity, "拆除")) {
								if (logger.isInfoEnabled()) {
									// logger.info("[确认拆除宝石] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ?
									// ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (inlayEntity != null ?
									// inlayEntity.getArticleName() : Translate.空白) + "] [" + (inlayEntity != null ? inlayEntity.getId() : Translate.空白) + "] [" + index + "]",
									// start);
									if (logger.isInfoEnabled()) logger.info("[操作类别:确认拆除宝石] [状态:成功] [username:{}] [id:{}] [name:{}] [ae:{}] [articleId:{}] [道具名字:{}] [道具id:{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (inlayEntity != null ? inlayEntity.getArticleName() : Translate.空白), (inlayEntity != null ? inlayEntity.getId() : Translate.空白), index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								}
							} else {
								if (logger.isInfoEnabled()) {
									// logger.info("[确认拆除宝石] [成功但放置出问题] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ?
									// ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + (inlayEntity != null ?
									// inlayEntity.getArticleName() : Translate.空白) + "] [" + (inlayEntity != null ? inlayEntity.getId() : Translate.空白) + "] [" + index + "]",
									// start);
									if (logger.isInfoEnabled()) logger.info("[确认拆除宝石] [成功但放置出问题] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), (inlayEntity != null ? inlayEntity.getArticleName() : Translate.空白), (inlayEntity != null ? inlayEntity.getId() : Translate.空白), index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								}
							}
							if (ArticleManager.logger.isWarnEnabled()) {
								ArticleManager.logger.warn("[挖取宝石后] " + player.getPlayerPropsString());
							}
							return;
							// }
							// }
							// }
							// }
							// }
							// } else {
							// String description = Translate.没有挖取道具;
							// try {
							// description = Translate.translateString(Translate.没有挖取道具提示, new String[][] { { Translate.ARTICLE_NAME_1, inlayEntity.getArticleName() }, {
							// Translate.ARTICLE_NAME_2, material } });
							// } catch (Exception ex) {
							// ex.printStackTrace();
							// }
							// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							// player.addMessageToRightBag(hreq);
							// logger.warn("[确认拆除宝石] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName()
							// : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
							// return;
							// }
							// } else {
							// String description = Translate.服务器物品出现错误;
							// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							// player.addMessageToRightBag(hreq);
							// logger.warn("[确认拆除宝石] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName()
							// : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "宝石级别对应的挖取物品配置错误] [" + index + "]", start);
							// return;
							// }
						} else {
							String description = Translate.服务器物品出现错误;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							// logger.warn("[确认拆除宝石] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName()
							// : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "id为" + inlayArticleId + "的物品不是宝石] [" + index + "]",
							// start);
							if (logger.isWarnEnabled()) logger.warn("[确认拆除宝石] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品不是宝石] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, inlayArticleId, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
							return;
						}
					} else {
						String description = Translate.服务器物品出现错误;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认拆除宝石] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "id为" + inlayArticleId + "的物品为空] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认拆除宝石] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品为空] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, inlayArticleId, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.请放入正确物品;
					if (!hasEquipment) {
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[确认拆除宝石] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
						// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
						if (logger.isWarnEnabled()) logger.warn("[确认拆除宝石] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[确认拆除宝石] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "] [" + index + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[确认拆除宝石] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, index, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}
	
	public QUERY_NEW_EQUIPMENT_STRONG3_RES queryNewEquipmentStrongReq3(Player player, QUERY_NEW_EQUIPMENT_STRONG3_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		try {
			if (player != null && req != null && aem != null && am != null) {
				long equipmentId = req.getEquipmentId();
				ArticleEntity ae = aem.getEntity(equipmentId);
				int 强化需要羽化石数量 = 0;
				if (ae instanceof EquipmentEntity) {
					String mess = Translate.translateString(Translate.强化需要材料, new String[][] { { Translate.STRING_1, Translate.强化石 } });
					// 特殊装备判断
					EquipmentEntity ee = (EquipmentEntity) ae;
					if (ee.getStar() >= starMaxValue) {	//满级
						player.sendError(Translate.已达到满级);
						mess = Translate.最大星描述;
						mess = "<f size='28' color='0xffFD6B00'>" + mess + "</f>";
						QUERY_NEW_EQUIPMENT_STRONG3_RES rrr = new QUERY_NEW_EQUIPMENT_STRONG3_RES(req.getSequnceNum(), "", equipmentId, 0, mess, new String[0], new int[0], "", 0, 0, new String[0], new int[0]);
						return rrr;
					}
					boolean bln = ee.isOprate(player, false, EquipmentEntity.强化);
					if (!bln) {
						return null;
					}

					强化需要羽化石数量 = 每个等级消耗羽化石的数量[ee.getStar()];

					/** 20星之前只需要强化石，20升21只需要羽化石，21之后需要强化石和精华羽化石,31星装备炼到极限 **/

					if (((EquipmentEntity) ae).getStar() == 装备炼星需要羽化的等级) {
						mess = Translate.translateString(Translate.强化需要材料, new String[][] { { Translate.STRING_1, Translate.羽化石 } });
					}

					if (((EquipmentEntity) ae).getStar() > 装备炼星需要羽化的等级) {
						mess = mess + Translate.translateString(Translate.额外的需要材料, new String[][] { { Translate.STRING_1, 强化需要羽化石数量 + "" } });
					}

					mess = "<f size='28' color='0x78f4ff'>" + mess + "</f>";

					if (((EquipmentEntity) ae).getStar() == starMaxValue) {
						mess = Translate.最大星描述;
						mess = "<f size='28' color='0xffFD6B00'>" + mess + "</f>";
					}

					if (((EquipmentEntity) ae).getStar() <= starMaxValue) {
						String[] strongMaterialName = { "", "", "", "" };
						int[] strongMaterialLuck = { 0, 0, 0, 0, 0 };
						long cost = 0;
						int luck = 0;
						String needname = "";

						Equipment e = (Equipment) am.getArticle(ae.getArticleName());
						strongMaterialName = 根据装备级别得到所需强化符(e.getPlayerLevelLimit());
						strongMaterialLuck = 根据装备强化等级得到不同颜色强化符的成功率分子值_new(ee.getStar());
						cost = strongConsumeMoney(ee);

						if (((EquipmentEntity) ae).getStar() == 装备炼星需要羽化的等级) {
							strongMaterialName = new String[] {};
							luck = (int) (TOTAL_LUCK_VALUE * 0.3);
							needname = 羽化石;
						}
						if (((EquipmentEntity) ae).getStar() > 装备炼星需要羽化的等级) {
							needname = 精华羽化石;
						}
						String[] extra2M = new String[0];
						int[] extra2P = new int[0];
						if (((EquipmentEntity) ae).getStar() >= 神炼符需要强化等级) {
							extra2M = Arrays.copyOf(extra2M, extra2M.length + 2);
							extra2P = Arrays.copyOf(extra2P, extra2P.length + 2);
							extra2M[extra2M.length - 2] = Translate.低级神练符;
							extra2M[extra2M.length - 1] = Translate.高级神练符;
							extra2P[extra2P.length - 2] = AddLuckyCostEnum.SHENLIANFU_DI.getAddRate(ee);
							extra2P[extra2P.length - 1] = AddLuckyCostEnum.SHENLIANFU_GAO.getAddRate(ee);
						}

						String baoDiFu = "";
						int calLevel = ((EquipmentEntity) ae).getStar() - 20;
						if (calLevel >= 6 && calLevel <= 20) {
							baoDiFu = Translate.羽化 + (calLevel - 1) + 级保底符;
						}

						if (logger.isWarnEnabled()) logger.warn("【查询装备强化3】 [成功]  [" + player.getName() + "] [baoDiFu:" + baoDiFu + "] [强化需要羽化石数量:" + 强化需要羽化石数量 + "] [star:" + ((EquipmentEntity) ae).getStar() + "] [strongMaterialName:" + Arrays.toString(strongMaterialName) + "] [strongMaterialLuck:" + Arrays.toString(strongMaterialLuck) + "] [needname:" + needname + "] [luck:" + luck + "] [mess:" + mess + "]");
						QUERY_NEW_EQUIPMENT_STRONG3_RES res_ = new QUERY_NEW_EQUIPMENT_STRONG3_RES(req.getSequnceNum(), baoDiFu, equipmentId, cost, mess, strongMaterialName, strongMaterialLuck, needname, 强化需要羽化石数量, luck, extra2M, extra2P);
						return res_;
					} else {
						String description = Translate.装备已经强化到了上限;
						try {
							description = Translate.translateString(Translate.装备已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[查询装备强化2] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
				} else {
					String description = Translate.空白;
					description = Translate.请放入装备;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[查询装备强化2] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[查询装备强化2] [异常] [player:" + player.getName() + "] " + e + "");
		}
		return null;
	}

	public QUERY_NEW_EQUIPMENT_STRONG2_RES queryNewEquipmentStrongReq2(Player player, QUERY_NEW_EQUIPMENT_STRONG2_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		try {
			if (player != null && req != null && aem != null && am != null) {
				long equipmentId = req.getEquipmentId();
				ArticleEntity ae = aem.getEntity(equipmentId);
				int 强化需要羽化石数量 = 0;
				if (ae instanceof EquipmentEntity) {
					String mess = Translate.translateString(Translate.强化需要材料, new String[][] { { Translate.STRING_1, Translate.强化石 } });
					// 特殊装备判断
					EquipmentEntity ee = (EquipmentEntity) ae;
					boolean bln = ee.isOprate(player, false, EquipmentEntity.强化);
					if (!bln) {
						return null;
					}

					强化需要羽化石数量 = 每个等级消耗羽化石的数量[ee.getStar()];

					/** 20星之前只需要强化石，20升21只需要羽化石，21之后需要强化石和精华羽化石,31星装备炼到极限 **/

					if (((EquipmentEntity) ae).getStar() == 装备炼星需要羽化的等级) {
						mess = Translate.translateString(Translate.强化需要材料, new String[][] { { Translate.STRING_1, Translate.羽化石 } });
					}

					if (((EquipmentEntity) ae).getStar() > 装备炼星需要羽化的等级) {
						mess = mess + Translate.translateString(Translate.额外的需要材料, new String[][] { { Translate.STRING_1, 强化需要羽化石数量 + "" } });
					}

					mess = "<f size='28' color='0x78f4ff'>" + mess + "</f>";

					if (((EquipmentEntity) ae).getStar() == starMaxValue) {
						mess = Translate.最大星描述;
						mess = "<f size='28' color='0xffFD6B00'>" + mess + "</f>";
					}

					if (((EquipmentEntity) ae).getStar() <= starMaxValue) {
						String[] strongMaterialName = { "", "", "", "" };
						int[] strongMaterialLuck = { 0, 0, 0, 0, 0 };
						long cost = 0;
						int luck = 0;
						String needname = "";

						Equipment e = (Equipment) am.getArticle(ae.getArticleName());
						strongMaterialName = 根据装备级别得到所需强化符(e.getPlayerLevelLimit());
						strongMaterialLuck = 根据装备强化等级得到不同颜色强化符的成功率分子值_new(ee.getStar());
						cost = strongConsumeMoney(ee);

						if (((EquipmentEntity) ae).getStar() == 装备炼星需要羽化的等级) {
							strongMaterialName = new String[] {};
							luck = (int) (TOTAL_LUCK_VALUE * 0.3);
							needname = 羽化石;
						}
						if (((EquipmentEntity) ae).getStar() > 装备炼星需要羽化的等级) {
							needname = 精华羽化石;
						}
						if (((EquipmentEntity) ae).getStar() >= 神炼符需要强化等级) {
							strongMaterialName = Arrays.copyOf(strongMaterialName, strongMaterialName.length + 2);
							strongMaterialName[strongMaterialName.length - 2] = Translate.低级神练符;
							strongMaterialName[strongMaterialName.length - 1] = Translate.高级神练符;
						}

						String baoDiFu = "";
						int calLevel = ((EquipmentEntity) ae).getStar() - 20;
						if (calLevel >= 6 && calLevel <= 20) {
							baoDiFu = Translate.羽化 + (calLevel - 1) + 级保底符;
						}

						if (logger.isWarnEnabled()) logger.warn("【查询装备强化2】 [成功]  [" + player.getName() + "] [baoDiFu:" + baoDiFu + "] [强化需要羽化石数量:" + 强化需要羽化石数量 + "] [star:" + ((EquipmentEntity) ae).getStar() + "] [strongMaterialName:" + Arrays.toString(strongMaterialName) + "] [strongMaterialLuck:" + Arrays.toString(strongMaterialLuck) + "] [needname:" + needname + "] [luck:" + luck + "] [mess:" + mess + "]");
						QUERY_NEW_EQUIPMENT_STRONG2_RES res_ = new QUERY_NEW_EQUIPMENT_STRONG2_RES(req.getSequnceNum(), baoDiFu, equipmentId, cost, mess, strongMaterialName, strongMaterialLuck, needname, 强化需要羽化石数量, luck);
						return res_;
					} else {
						String description = Translate.装备已经强化到了上限;
						try {
							description = Translate.translateString(Translate.装备已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[查询装备强化2] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
				} else {
					String description = Translate.空白;
					description = Translate.请放入装备;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[查询装备强化2] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[查询装备强化2] [异常] [player:" + player.getName() + "] " + e + "");
		}
		return null;
	}

	public QUERY_NEW_EQUIPMENT_STRONG_RES queryNewEquipmentStrongReq(Player player, QUERY_NEW_EQUIPMENT_STRONG_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		try {
			if (player != null && req != null && aem != null && am != null) {
				long equipmentId = req.getEquipmentId();
				ArticleEntity ae = aem.getEntity(equipmentId);
				int 强化需要羽化石数量 = 0;
				if (ae instanceof EquipmentEntity) {
					String mess = Translate.translateString(Translate.强化需要材料, new String[][] { { Translate.STRING_1, Translate.强化石 } });
					// 特殊装备判断
					EquipmentEntity ee = (EquipmentEntity) ae;
					boolean bln = ee.isOprate(player, false, EquipmentEntity.强化);
					if (!bln) {
						return null;
					}

					强化需要羽化石数量 = 每个等级消耗羽化石的数量[ee.getStar()];

					/** 20星之前只需要强化石，20升21只需要羽化石，21之后需要强化石和精华羽化石,31星装备炼到极限 **/

					if (((EquipmentEntity) ae).getStar() == 装备炼星需要羽化的等级) {
						mess = Translate.translateString(Translate.强化需要材料, new String[][] { { Translate.STRING_1, Translate.羽化石 } });
					}

					if (((EquipmentEntity) ae).getStar() > 装备炼星需要羽化的等级) {
						mess = mess + Translate.translateString(Translate.额外的需要材料, new String[][] { { Translate.STRING_1, 强化需要羽化石数量 + "" } });
					}

					mess = "<f size='28' color='0x78f4ff'>" + mess + "</f>";

					if (((EquipmentEntity) ae).getStar() == starMaxValue) {
						mess = Translate.最大星描述;
						mess = "<f size='28' color='0xffFD6B00'>" + mess + "</f>";
					}

					if (((EquipmentEntity) ae).getStar() <= starMaxValue) {
						String[] strongMaterialName = { "", "", "", "" };
						int[] strongMaterialLuck = { 0, 0, 0, 0, 0 };
						long cost = 0;
						int luck = 0;
						String needname = "";

						Equipment e = (Equipment) am.getArticle(ae.getArticleName());
						strongMaterialName = 根据装备级别得到所需强化符(e.getPlayerLevelLimit());
						strongMaterialLuck = 根据装备强化等级得到不同颜色强化符的成功率分子值_new(ee.getStar());
						cost = strongConsumeMoney(ee);

						if (((EquipmentEntity) ae).getStar() == 装备炼星需要羽化的等级) {
							strongMaterialName = new String[] {};
							luck = (int) (TOTAL_LUCK_VALUE * 0.3);
							needname = 羽化石;
						}
						if (((EquipmentEntity) ae).getStar() > 装备炼星需要羽化的等级) {
							needname = 精华羽化石;
						}
						if (((EquipmentEntity) ae).getStar() >= 神炼符需要强化等级) {
							strongMaterialName = Arrays.copyOf(strongMaterialName, strongMaterialName.length + 2);
							strongMaterialName[strongMaterialName.length - 2] = Translate.低级神练符;
							strongMaterialName[strongMaterialName.length - 1] = Translate.高级神练符;
						}
						

						if (logger.isWarnEnabled()) logger.warn("【查询装备强化】 [成功]  [" + player.getName() + "] [强化需要羽化石数量:" + 强化需要羽化石数量 + "] [star:" + ((EquipmentEntity) ae).getStar() + "] [strongMaterialName:" + Arrays.toString(strongMaterialName) + "] [strongMaterialLuck:" + Arrays.toString(strongMaterialLuck) + "] [needname:" + needname + "] [luck:" + luck + "] [mess:" + mess + "]");
						QUERY_NEW_EQUIPMENT_STRONG_RES res_ = new QUERY_NEW_EQUIPMENT_STRONG_RES(req.getSequnceNum(), equipmentId, cost, mess, strongMaterialName, strongMaterialLuck, needname, 强化需要羽化石数量, luck);
						return res_;
					} else {
						String description = Translate.装备已经强化到了上限;
						try {
							description = Translate.translateString(Translate.装备已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[查询装备强化] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
				} else {
					String description = Translate.空白;
					description = Translate.请放入装备;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[查询装备强化] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[查询装备强化] [异常] [player:" + player.getName() + "] " + e + "");
		}
		return null;
	}

	/**
	 * 查询装备强化信息
	 * @param player
	 * @param req
	 * @return
	 */
	public QUERY_EQUIPMENT_STRONG_RES queryEquipmentStrongReq(Player player, QUERY_EQUIPMENT_STRONG_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		QUERY_EQUIPMENT_STRONG_RES res = null;
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && req != null && aem != null && am != null) {
			long equipmentId = req.getEquipmentId();
			ArticleEntity ae = aem.getEntity(equipmentId);
			if (ae instanceof EquipmentEntity) {

				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.强化);
				if (!bln) {
					return null;
				}

				if (((EquipmentEntity) ae).getStar() < starMaxValue) {
					res = queryEquipmentById(player, equipmentId, req.getSequnceNum());
				} else {
					String description = Translate.装备已经强化到了上限;
					try {
						description = Translate.translateString(Translate.装备已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[查询装备强化] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
					// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
					if (logger.isWarnEnabled()) logger.warn("[查询装备强化] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[查询装备强化] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() :
				// Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "] [" + description + "]", start);
				if (logger.isWarnEnabled()) logger.warn("[查询装备强化] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
		return res;
	}

	/**
	 * 根据装备id返回装备强化查询信息
	 * 
	 * @param player
	 * @param equipmentId
	 * @param seqNum
	 *            如果seqNum>0，自动使用seqNum，否则从新获取seqNum
	 * @return
	 */
	public QUERY_EQUIPMENT_STRONG_RES queryEquipmentById(Player player, long equipmentId, long seqNum) {
		QUERY_EQUIPMENT_STRONG_RES res = null;
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && aem != null && am != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);
			if (ae instanceof EquipmentEntity) {
				EquipmentEntity ee = (EquipmentEntity) ae;
				if (ee.getStar() >= starMaxValue) {
					return res;
				}
				Equipment e = (Equipment) am.getArticle(ae.getArticleName());
				String[] strongMaterialName = 根据装备级别得到所需强化符(e.getPlayerLevelLimit());

				int[] strongMaterialLuck = 根据装备强化等级得到不同颜色强化符的成功率分子值(ee.getStar());

				// int[] strongMaterialBindCount = new int[strongMaterialName.length];
				// int[] strongMaterialCount = new int[strongMaterialName.length];
				// for (int i = 0; i < strongMaterialName.length; i++) {
				// String articleName = strongMaterialName[i];
				// int count1 = player.countArticleInKnapsacksByName(articleName, true);
				// int count2 = player.countArticleInKnapsacksByName(articleName, false);
				// strongMaterialBindCount[i] = count1;
				// strongMaterialCount[i] = count2;
				// }

				String descript = Translate.空白;
				try {
					descript = Translate.translateString(Translate.需求金币提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				String strongedUUB = Translate.空白;
				if (seqNum > 0) {
					res = new QUERY_EQUIPMENT_STRONG_RES(seqNum, equipmentId, strongConsumeMoney(ee), strongedUUB, strongMaterialName, strongMaterialLuck);
				} else {
					res = new QUERY_EQUIPMENT_STRONG_RES(GameMessageFactory.nextSequnceNum(), equipmentId, strongConsumeMoney(ee), strongedUUB, strongMaterialName, strongMaterialLuck);
				}

				if (logger.isInfoEnabled()) logger.info(" equipmentId:{} descript:{} strongedUUB:{} strongMaterialName:{} strongMaterialLuck:{}", new Object[] { equipmentId, descript, strongedUUB, strongMaterialName[0], strongMaterialLuck[0] });
			}
		}
		return res;
	}

	/**
	 * 装备炼星手续费=装备境界*炼星等级*500文
	 * @return
	 */
	public int strongConsumeMoney(EquipmentEntity ee) {
		int count = 100000;
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(ee.getArticleName());
		if (a instanceof Equipment) {
			count = (((Equipment) a).getClassLimit() + 1) * (ee.getStar() + 1) * 500;
		}
		return count;
	}

	/**
	 * 装备强化申请
	 * @param player
	 * @param req
	 * @return
	 */
	public void newEquipmentStrongReq(Player player, NEW_EQUIPMENT_STRONG_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && aem != null && am != null && req != null) {
			long equipmentId = req.getEquipmentId();
			long[] strongMaterialIds = req.getStrongMaterialID();
			int strongType = req.getStrongType();
			long[] otherids = req.getOtherStrongMaterialID();
			int[] nums = req.getOtherStrongMaterialNum();
			ArticleEntity ae = aem.getEntity(equipmentId);
			ArticleEntity needArticle = null;
			try {
				if (otherids != null && otherids.length > 0) {
					needArticle = aem.getEntity(otherids[0]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (strongMaterialIds == null) {
				String description = Translate.空白;
				description = Translate.请放入强化物品;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			/**
			 * 处理当玩家放上装备后，放入羽化石后，然后换装备的
			 */

			// 特殊装备判断
			EquipmentEntity ee = (EquipmentEntity) ae;
			boolean bln = ee.isOprate(player, false, EquipmentEntity.强化);
			if (!bln) {
				return;
			}

			if (ee != null) {
				Equipment e = (Equipment) am.getArticle(ae.getArticleName());
				if (isChiBang(e.getEquipmentType())) {
					player.sendError(Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "炼星" } }));
					return;
				}
			}

			if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
				player.sendError(Translate.高级锁魂物品不能做此操作);
				return;
			}

			if (ee.getStar() >= starMaxValue) {
				player.sendError(Translate.装备已经强化到了上限);
				return;
			}

			boolean 我是20星 = false;
			int 强化需要羽化石数量 = 每个等级消耗羽化石的数量[ee.getStar()];
			int 实际羽化石数量 = 0;

			if (nums != null && nums.length > 0) {
				for (int num : nums) {
					实际羽化石数量 += num;
				}
			}

			if (实际羽化石数量 < 强化需要羽化石数量) {
				player.sendError(Translate.text_trade_006);
				if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [原因：放入格子中的羽化石数量不足,需要" + 强化需要羽化石数量 + "] [实际羽化石数量:" + 实际羽化石数量 + "] [start:" + ee.getStar() + "] [" + player.getLogString() + "]");
				return;
			}

			if (ee.getStar() == 装备炼星需要羽化的等级) {
				strongMaterialIds = new long[] { -1, -1, -1, -1 };
				我是20星 = true;
				if (needArticle != null) {
					if (!needArticle.getArticleName().equals(Translate.羽化石)) {
						player.sendError(Translate.请放入羽化石);
						return;
					}
				}

				if (player.getKnapsack_common().getArticleCellCount_重叠(羽化石) < 强化需要羽化石数量) {
					player.sendError(Translate.text_trade_006);
					if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [原因： 背包中羽化石石数量不足,需要" + 强化需要羽化石数量 + "] [实际羽化石数量:" + 实际羽化石数量 + "] [start:" + ee.getStar() + "] [" + player.getLogString() + "]");
					return;
				}
			}

			if (ee.getStar() > 装备炼星需要羽化的等级) {
				if (needArticle != null) {
					if (!needArticle.getArticleName().equals(Translate.精华羽化石)) {
						player.sendError(Translate.请放入精华羽化石);
						return;
					}
				}

				if (player.getKnapsack_common().getArticleCellCount_重叠(精华羽化石) < 强化需要羽化石数量) {
					player.sendError(Translate.text_trade_006);
					if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [原因： 背包中精华羽化石石数量不足,需要" + 强化需要羽化石数量 + "] [实际羽化石数量:" + 实际羽化石数量 + "] [start:" + ee.getStar() + "] [" + player.getLogString() + "]");
					return;
				}
			}

			if (strongType == 0) {
				if (!player.bindSilverEnough(strongConsumeMoney(ee))) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			} else if (strongType == 1) {
				if (player.getSilver() < strongConsumeMoney(ee)) {
					String description = Translate.元宝不足;
					try {
						description = Translate.translateString(Translate.银子不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			}

			if (strongMaterialIds.length != strongMaterialMaxNumber && !我是20星) {
				String description = Translate.空白;
				description = Translate.请输入正确数量;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}!={}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds.length, strongMaterialMaxNumber, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}

			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
				if (((EquipmentEntity) ae).getStar() < starMaxValue) {
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					String[] strongMaterialName = 根据装备级别得到所需强化符(e.getPlayerLevelLimit());
					if (ee.getStar() >= 26) {
						strongMaterialName = Arrays.copyOf(strongMaterialName, strongMaterialName.length + 2);
						strongMaterialName[strongMaterialName.length-2] = Translate.低级神练符;
						strongMaterialName[strongMaterialName.length-1] = Translate.高级神练符;
					}
					int[] strongMaterialColorLuck = 根据装备强化等级得到不同颜色强化符的成功率分子值_new(ee.getStar());
					Knapsack[] knapsacks = player.getKnapsacks_common();
					ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
					for (long id : strongMaterialIds) {
						if (id != -1) {
							ArticleEntity strongMaterialEntity = aem.getEntity(id);
							if (strongMaterialEntity == null) {
								String description = Translate.空白;
								description = Translate.请放入强化物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, id, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							boolean isStrongMaterial = false;
							if (strongMaterialName != null) {
								for (String strongName : strongMaterialName) {
									if (strongName != null && strongName.equals(strongMaterialEntity.getArticleName())) {
										isStrongMaterial = true;
										break;
									}
								}
							}
							if (!isStrongMaterial) {
								String description = Translate.请放入正确物品;
								try {
									description = Translate.translateString(Translate.该物品不是需要的物品, new String[][] { { Translate.ARTICLE_NAME_1, strongMaterialEntity.getArticleName() } });
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							boolean hasStrongMaterial = false;
							if (knapsacks != null) {
								for (Knapsack knapsack : knapsacks) {
									if (knapsack != null) {
										int index = knapsack.indexOf(strongMaterialEntity);
										if (index != -1) {
											hasStrongMaterial = true;
											break;
										}
									}
								}
							}
							if (!hasStrongMaterial) {
								String description = Translate.空白;
								description = Translate.请放入强化物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}背包中没有id为{}的物品] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialEntity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							strongMaterialEntitys.add(strongMaterialEntity);
						}
					}
					if (strongMaterialEntitys.isEmpty() && !我是20星) {
						String description = Translate.空白;
						description = Translate.请放入强化物品;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					boolean hasEquipment = false;
					if (knapsacks != null) {
						for (Knapsack knapsack : knapsacks) {
							if (knapsack != null) {
								if (!hasEquipment) {
									int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
									if (index1 != -1) {
										hasEquipment = true;
										break;
									}
								}
							}
						}
					}
					if (hasEquipment) {
						boolean bindedTip = false;
						if (!ee.isBinded()) {
							for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
								if (strongMaterialEntity != null && strongMaterialEntity.isBinded()) {
									bindedTip = true;
									break;
								}
							}
						}
						if (!bindedTip) {
							if (otherids != null && otherids.length > 0) {
								for (long id : otherids) {
									ArticleEntity otherAE = ArticleEntityManager.getInstance().getEntity(id);
									if (otherAE != null && otherAE.isBinded()) {
										bindedTip = true;
										break;
									}
								}
							}
						}
						if (logger.isWarnEnabled()) logger.warn("[装备强化申请test] [otherids:" + (otherids == null ? "null" : Arrays.toString(otherids)) + "] [nums:" + (nums == null ? "null" : Arrays.toString(nums)) + "] [bindedTip:" + bindedTip + "]");
						int totalLuckValue = 0;
						
						for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
							if (strongMaterialEntity != null) {
								try {
									Article a = ArticleManager.getInstance().getArticle(strongMaterialEntity.getArticleName());
									AddLuckyCostEnum ace = AddLuckyCostEnum.valueOf(a.getName_stat(), strongMaterialEntity.getColorType());
									if (ace == null || ace.getAddRate(ee) <= 0) {
										totalLuckValue += strongMaterialColorLuck[strongMaterialEntity.getColorType()];
									} else {
										totalLuckValue += ace.getAddRate(ee);
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
						/*for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
							if (strongMaterialEntity != null) {
								try {
									totalLuckValue += strongMaterialColorLuck[strongMaterialEntity.getColorType()];
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}*/
						if (totalLuckValue > TOTAL_LUCK_VALUE) {
							totalLuckValue = TOTAL_LUCK_VALUE;
						}
						double percent = totalLuckValue * 1.0 / TOTAL_LUCK_VALUE;

						if (percent > 1) {
							percent = 1;
						}
						if (我是20星) {
							percent = 0.3;
							totalLuckValue = (int) (TOTAL_LUCK_VALUE * 0.3);
						}
						String desUUB = Translate.强化提示信息;
						try {
							if (strongType == 0) {
								if (bindedTip && !ee.isBinded()) {
									desUUB = Translate.translateString(Translate.强化不提示信息绑定用金币, new String[][] { { Translate.PERCENT_1, percent * 100 + "" }, { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
								} else {
									confirmNewEquipmentStrongReq(player, req, 我是20星);
									return;
								}
							} else if (strongType == 1) {
								if (bindedTip && !ee.isBinded()) {
									// desUUB = Translate.translateString(Translate.强化提示信息绑定用元宝, new String[][] { { Translate.PERCENT_1, percent * 100 + "" }, {
									// Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
									desUUB = Translate.translateString(Translate.强化提示信息绑定用元宝, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
								} else {
									confirmNewEquipmentStrongReq(player, req, 我是20星);
									return;
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						WindowManager wm = WindowManager.getInstance();
						MenuWindow mw = wm.createTempMenuWindow(600);
						String title = Translate.强化提示信息;
						mw.setTitle(title);
						mw.setDescriptionInUUB(desUUB);
						Option_StrongConfirm option = new Option_StrongConfirm();
						option.setReq_new(req);
						option.setIs20star(我是20星);
						option.setText(Translate.确定);
						Option[] options = new Option[] { option, new Option_Cancel() };
						mw.setOptions(options);
						CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						player.addMessageToRightBag(creq);
						return;
					} else {
						String description = Translate.请放入正确物品;
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.装备已经强化到了上限;
					try {
						description = Translate.translateString(Translate.装备已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	public void equipmentStrongReq(Player player, EQUIPMENT_STRONG_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && aem != null && am != null && req != null) {
			long equipmentId = req.getEquipmentId();
			long[] strongMaterialIds = req.getStrongMaterialID();
			int strongType = req.getStrongType();

			ArticleEntity ae = aem.getEntity(equipmentId);

			// 特殊装备判断
			EquipmentEntity ee = (EquipmentEntity) ae;
			boolean bln = ee.isOprate(player, false, EquipmentEntity.强化);
			if (!bln) {
				return;
			}

			if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
				player.sendError(Translate.高级锁魂物品不能做此操作);
				return;
			}

			if (strongType == 0) {
				if (!player.bindSilverEnough(strongConsumeMoney(ee))) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			} else if (strongType == 1) {
				if (player.getSilver() < strongConsumeMoney(ee)) {
					String description = Translate.元宝不足;
					try {
						description = Translate.translateString(Translate.银子不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			}
			if (strongMaterialIds == null) {
				String description = Translate.空白;
				description = Translate.请放入强化物品;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			if (strongMaterialIds.length != strongMaterialMaxNumber) {
				String description = Translate.空白;
				description = Translate.请输入正确数量;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}!={}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds.length, strongMaterialMaxNumber, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
				// 星级必须铭刻
				// if (!ee.isInscriptionFlag()) {
				// String description = Translate.空白;
				// description = Translate.必须为铭刻装备;
				// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				// player.addMessageToRightBag(hreq);
				// logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getUsername(),player.getId(),player.getName(),(ae != null ?
				// ae.getArticleName() : Translate.空白),(ae != null ? ae.getId() : Translate.空白),description,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
				// return;
				// }
				if (((EquipmentEntity) ae).getStar() < starMaxValue) {
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					String[] strongMaterialName = 根据装备级别得到所需强化符(e.getPlayerLevelLimit());
					int[] strongMaterialColorLuck = 根据装备强化等级得到不同颜色强化符的成功率分子值(ee.getStar());
					Knapsack[] knapsacks = player.getKnapsacks_common();
					ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
					for (long id : strongMaterialIds) {
						if (id != -1) {
							ArticleEntity strongMaterialEntity = aem.getEntity(id);
							if (strongMaterialEntity == null) {
								String description = Translate.空白;
								description = Translate.请放入强化物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, id, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							boolean isStrongMaterial = false;
							if (strongMaterialName != null) {
								for (String strongName : strongMaterialName) {
									if (strongName != null && strongName.equals(strongMaterialEntity.getArticleName())) {
										isStrongMaterial = true;
										break;
									}
								}
							}
							if (!isStrongMaterial) {
								String description = Translate.请放入正确物品;
								try {
									description = Translate.translateString(Translate.该物品不是需要的物品, new String[][] { { Translate.ARTICLE_NAME_1, strongMaterialEntity.getArticleName() } });
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							boolean hasStrongMaterial = false;
							if (knapsacks != null) {
								for (Knapsack knapsack : knapsacks) {
									if (knapsack != null) {
										int index = knapsack.indexOf(strongMaterialEntity);
										if (index != -1) {
											hasStrongMaterial = true;
											break;
										}
									}
								}
							}
							if (!hasStrongMaterial) {
								String description = Translate.空白;
								description = Translate.请放入强化物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}背包中没有id为{}的物品] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialEntity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							strongMaterialEntitys.add(strongMaterialEntity);
						}
					}
					if (strongMaterialEntitys.isEmpty()) {
						String description = Translate.空白;
						description = Translate.请放入强化物品;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					boolean hasEquipment = false;
					if (knapsacks != null) {
						for (Knapsack knapsack : knapsacks) {
							if (knapsack != null) {
								if (!hasEquipment) {
									int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
									if (index1 != -1) {
										hasEquipment = true;
										break;
									}
								}
							}
						}
					}
					if (hasEquipment) {
						boolean bindedTip = false;
						if (!ee.isBinded()) {
							for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
								if (strongMaterialEntity != null && strongMaterialEntity.isBinded()) {
									bindedTip = true;
									break;
								}
							}
						}
						int totalLuckValue = 0;
						for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
							if (strongMaterialEntity != null) {
								try {
									totalLuckValue += strongMaterialColorLuck[strongMaterialEntity.getColorType()];
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
						double percent = totalLuckValue * 1.0 / TOTAL_LUCK_VALUE;
						if (percent > 1) {
							percent = 1;
						}
						// 提示玩家信息
						WindowManager wm = WindowManager.getInstance();
						MenuWindow mw = wm.createTempMenuWindow(600);
						String title = Translate.强化提示信息;
						mw.setTitle(title);
						String desUUB = Translate.强化提示信息;
						try {
							if (strongType == 0) {
								if (bindedTip && !ee.isBinded()) {
									desUUB = Translate.translateString(Translate.强化不提示信息绑定用金币, new String[][] { { Translate.PERCENT_1, percent * 100 + "" }, { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
								} else {
									confirmEquipmentStrongReq(player, equipmentId, strongMaterialIds, req.getSequnceNum(), strongType);
									return;
								}
							} else if (strongType == 1) {
								if (bindedTip && !ee.isBinded()) {
									// desUUB = Translate.translateString(Translate.强化提示信息绑定用元宝, new String[][] { { Translate.PERCENT_1, percent * 100 + "" }, {
									// Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
									desUUB = Translate.translateString(Translate.强化提示信息绑定用元宝, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() }, { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
								} else {
									confirmEquipmentStrongReq(player, equipmentId, strongMaterialIds, req.getSequnceNum(), strongType);
									return;
								}
							}

						} catch (Exception ex) {
							ex.printStackTrace();
						}
						mw.setDescriptionInUUB(desUUB);
						Option_StrongConfirm option = new Option_StrongConfirm();
						option.setReq(req);
						option.setText(Translate.确定);
						Option[] options = new Option[] { option, new Option_Cancel() };
						mw.setOptions(options);
						CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						player.addMessageToRightBag(creq);
						return;
					} else {
						String description = Translate.请放入正确物品;
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.装备已经强化到了上限;
					try {
						description = Translate.translateString(Translate.装备已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 装备强化确认
	 * @param player
	 * @param req
	 * @return
	 */
	public void confirmEquipmentStrongReq(Player player, long equipmentId, long[] strongMaterialIds, long seqNum, int strongType) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && aem != null && am != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);

			// 特殊装备判断
			EquipmentEntity ee = (EquipmentEntity) ae;
			boolean bln = ee.isOprate(player, false, EquipmentEntity.强化);
			if (!bln) {
				return;
			}
			if (strongType == 0) {
				if (!player.bindSilverEnough(strongConsumeMoney(ee))) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			} else if (strongType == 1) {
				if (player.getSilver() < strongConsumeMoney(ee)) {
					String description = Translate.元宝不足;
					try {
						description = Translate.translateString(Translate.银子不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			}
			if (strongMaterialIds == null) {
				String description = Translate.空白;
				description = Translate.请放入强化物品;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			if (strongMaterialIds.length != strongMaterialMaxNumber) {
				String description = Translate.空白;
				description = Translate.请输入正确数量;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}!={}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds.length, strongMaterialMaxNumber, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
				// 星级必须铭刻
				// if (!ee.isInscriptionFlag()) {
				// String description = Translate.空白;
				// description = Translate.必须为铭刻装备;
				// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				// player.addMessageToRightBag(hreq);
				// logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{player.getUsername(),player.getId(),player.getName(),(ae != null ?
				// ae.getArticleName() : Translate.空白),(ae != null ? ae.getId() : Translate.空白),description,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
				// return;
				// }
				if (((EquipmentEntity) ae).getStar() < starMaxValue) {
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					String[] strongMaterialName = 根据装备级别得到所需强化符(e.getPlayerLevelLimit());
					int[] strongMaterialColorLuck = 根据装备强化等级得到不同颜色强化符的成功率分子值(ee.getStar());
					Knapsack[] knapsacks = player.getKnapsacks_common();
					ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
					for (long id : strongMaterialIds) {
						if (id != -1) {
							ArticleEntity strongMaterialEntity = aem.getEntity(id);
							if (strongMaterialEntity == null) {
								String description = Translate.空白;
								description = Translate.请放入强化物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, id, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							boolean isStrongMaterial = false;
							if (strongMaterialName != null) {
								for (String strongName : strongMaterialName) {
									if (strongName != null && strongName.equals(strongMaterialEntity.getArticleName())) {
										isStrongMaterial = true;
										break;
									}
								}
							}
							if (!isStrongMaterial) {
								String description = Translate.请放入正确物品;
								try {
									description = Translate.translateString(Translate.该物品不是需要的物品, new String[][] { { Translate.ARTICLE_NAME_1, strongMaterialEntity.getArticleName() } });
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							boolean hasStrongMaterial = false;
							if (knapsacks != null) {
								for (Knapsack knapsack : knapsacks) {
									if (knapsack != null) {
										int index = knapsack.indexOf(strongMaterialEntity);
										if (index != -1) {
											hasStrongMaterial = true;
											break;
										}
									}
								}
							}
							if (!hasStrongMaterial) {
								String description = Translate.空白;
								description = Translate.请放入强化物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}背包中没有id为{}的物品] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialEntity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							strongMaterialEntitys.add(strongMaterialEntity);
						}
					}
					if (strongMaterialEntitys.isEmpty()) {
						String description = Translate.空白;
						description = Translate.请放入强化物品;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					TransitRobberyManager.logger.error("[使用练星物品1][" + player.getLogString() + "]");
					boolean hasEquipment = false;
					if (knapsacks != null) {
						for (Knapsack knapsack : knapsacks) {
							if (knapsack != null) {
								if (!hasEquipment) {
									int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
									if (index1 != -1) {
										hasEquipment = true;
										break;
									}
								}
							}
						}
					}
					if (hasEquipment) {
						boolean bindedTip = false;
						if (!ee.isBinded()) {
							for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
								if (strongMaterialEntity != null && strongMaterialEntity.isBinded()) {
									bindedTip = true;
									break;
								}
							}
						}
						int totalLuckValue = 0;
						for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
							if (strongMaterialEntity != null) {
								try {
									totalLuckValue += strongMaterialColorLuck[strongMaterialEntity.getColorType()];
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
						double percent = totalLuckValue * 1.0 / TOTAL_LUCK_VALUE;
						if (percent > 1) {
							percent = 1;
						}
						if (knapsacks != null) {
							for (long strongMaterialId : strongMaterialIds) {
								if (strongMaterialId != -1) {
									ArticleEntity aee1 = player.removeArticleEntityFromKnapsackByArticleId(strongMaterialId, "强化删除", true);
									if (aee1 == null) {
										String description = Translate.删除物品不成功;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}mss]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
										TransitRobberyManager.logger.error("[使用练星物品2][" + player.getLogString() + "]");
										return;
									} else {

										// 统计
										ArticleStatManager.addToArticleStat(player, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "强化删除", null);
										// 活跃度统计
										ActivenessManager.getInstance().record(player, ActivenessType.强化装备);
									}
								}
							}
						}

						// 扣费
						try {
							if (strongType == 0) {
								BillingCenter bc = BillingCenter.getInstance();
								bc.playerExpense(player, strongConsumeMoney(ee), CurrencyType.BIND_YINZI, ExpenseReasonType.EQUIPMENT_UPGRADE, "用绑银强化装备", VipExpActivityManager.default_add_rmb_expense);
							} else if (strongType == 1) {
								BillingCenter bc = BillingCenter.getInstance();
								bc.playerExpense(player, strongConsumeMoney(ee), CurrencyType.YINZI, ExpenseReasonType.EQUIPMENT_UPGRADE, "用银子强化装备");
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							TransitRobberyManager.logger.error("[使用练星物品3][" + player.getLogString() + "]", ex);
							if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
							return;
						}
						// 扣费成功，活动开始
						if (strongMaterialEntitys != null && strongMaterialEntitys.size() > 0) {
							try {
								TransitRobberyManager.logger.error("[使用练星物品4][" + player.getLogString() + "]");
								ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
							} catch (Exception e2) {
								TransitRobberyManager.logger.error("[使用赠送活动报错]", e2);
							}
						}

						int resultValue = random.nextInt(TOTAL_LUCK_VALUE) + 1;
						boolean success = false;
						if (totalLuckValue >= resultValue) {
							success = true;
						}
						boolean canStrong = true;
						int oldStar = ee.getStar();
						if (success) {
							ee.setStar(ee.getStar() + 1);
							if (ee.getStar() >= starMaxValue) {
								ee.setStar(starMaxValue);
								canStrong = false;
							}
							if (ee.getOnceMaxStar() < ee.getStar()) {
								int a = ee.getStar() / 2;
								int b = ee.getStar() % 2;
								if (a >= 5 && b == 0) {
									try {
										ChatMessageService cms = ChatMessageService.getInstance();
										ChatMessage msg = new ChatMessage();
										// 恭喜！<国家>的<玩家姓名>经历千辛万苦将<装备名>炼到<星级>。（世界）
										String descri = Translate.translateString(Translate.装备强化系统广播详细, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, ee.getArticleName() }, { Translate.COUNT_1, ee.getStar() + "" } });
										if (a == 5) {
											descri = "<f color='" + COLOR_WHITE + "'>" + descri + "</f>";
										} else if (a == 6) {
											descri = "<f color='" + COLOR_GREEN + "'>" + descri + "</f>";
										} else if (a == 7) {
											descri = "<f color='" + COLOR_BLUE + "'>" + descri + "</f>";
										} else if (a == 8) {
											descri = "<f color='" + COLOR_PURPLE + "'>" + descri + "</f>";
										} else if (a == 9) {
											descri = "<f color='" + COLOR_ORANGE + "'>" + descri + "</f>";
										} else if (a == 10) {
											descri = "<f color='" + COLOR_ORANGE + "'>" + descri + "</f>";

											if (isGive20Xin) {
												String serverName = GameConstants.getInstance().getServerName();
												if (serverName.equals("新功能测试") || serverName.equals("雄霸天下") || serverName.equals("仙界至尊") || serverName.equals("金蛇圣殿") || serverName.equals("百花仙谷") || serverName.equals("傲剑凌云")) {
													if (Give20Xin_ent_time == 0) {
														SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
														Give20Xin_ent_time = format.parse("2013-01-02 23:59:59").getTime();
													}
													long now = System.currentTimeMillis();
													if (now < Give20Xin_ent_time) {
														try {
															Article xing20 = ArticleManager.getInstance().getArticle(Translate.二十星);
															ArticleEntity entity20 = ArticleEntityManager.getInstance().createEntity(xing20, true, ArticleEntityManager.LIANXING_ACTIVITY, player, xing20.getColorType(), 1, true);
															MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { entity20 }, "恭喜获得[星星相映]返礼", "您在[星星相映]活动期间成功将装备炼到20星，特奖励您20级破碎星辰一颗，请查收附件。活动到1月2日结束!", 0, 0, 0, "炼星到20星送20星");
															logger.warn("[炼到20星送星星] [{}] [{}] [{}]", new Object[] { player.getLogString(), entity20.getArticleName(), entity20.getId() });
														} catch (Exception ex) {
															logger.error("[炼星20星送出错] " + player.getLogString(), ex);
														}
													}
												}
											}
										}
										msg.setMessageText(descri);
										cms.sendMessageToSystem(msg);

									} catch (Exception ex) {

									}
								}
								ee.setOnceMaxStar(ee.getStar());
							}
							String description = Translate.恭喜你装备升级成功;
							try {
								description = Translate.translateString(Translate.装备强化成功, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() }, new String[] { Translate.LEVEL_1, ee.getStar() + "" } });
								if (ee.getStar() > 20) {
									description = Translate.translateString(Translate.装备羽化成功, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() }, new String[] { Translate.STRING_1, 获得对应的羽化描述.get(new Integer(ee.getStar())) } });
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
							player.addMessageToRightBag(hreq);
							QUERY_ARTICLE_INFO_RES qres = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), ee.getInfoShow(player));
							player.addMessageToRightBag(qres);
							PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备升级成功, (byte) 1, AnimationManager.动画播放位置类型_武器强化中间孔, 0, 0);
							if (pareq != null) {
								player.addMessageToRightBag(pareq);
							}

							player.successStrongStarCount = player.successStrongStarCount + 1;
							player.failStrongStarCount = 0;
							if (AchievementManager.getInstance() != null) {
								AchievementManager.getInstance().record(player, RecordAction.连续炼星成功次数, player.successStrongStarCount);
								AchievementManager.getInstance().record(player, RecordAction.全身装备最大星级, ee.getStar());
							}
							if (ee.getStar() < 20) {
								try {
									EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.炼星次数, 1L });
									EventRouter.getInst().addEvent(evt2);
								} catch (Exception eex) {
									PlayerAimManager.logger.error("[目标系统] [统计炼星次数异常] [" + player.getLogString() + "]", eex);
								}
							}
							if (ee.getStar() > 20 && ee.isInscriptionFlag()) { // 铭刻才有效
								try {
									AchievementManager.getInstance().record(player, RecordAction.羽化装备成功次数);
								} catch (Exception eex) {
									PlayerAimManager.logger.error("[目标系统] [统计装备羽化成功次数] [异常] [" + player.getLogString() + "]", eex);
								}
							}
						} else {

							// 强化失败降级
							int dropStar = 强化失败后降级();
							if (ee.getStar() <= dropStar) {
								ee.setStar(0);
							} else {
								ee.setStar(ee.getStar() - dropStar);
							}

							String description = Translate.很遗憾装备没有升级成功;
							try {
								description = Translate.translateString(Translate.装备强化失败, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() }, new String[] { Translate.LEVEL_1, ee.getStar() + "" } });
								if (ee.getStar() > 20) {
									description = Translate.translateString(Translate.装备羽化失败, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() }, new String[] { Translate.STRING_1, 获得对应的羽化描述.get(new Integer(ee.getStar())) } });
								}

							} catch (Exception ex) {
								ex.printStackTrace();
							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
							player.addMessageToRightBag(hreq);
							PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备升级失败, (byte) 1, AnimationManager.动画播放位置类型_武器强化中间孔, 0, 0);
							if (pareq != null) {
								player.addMessageToRightBag(pareq);
							}
							player.successStrongStarCount = 0;
							player.failStrongStarCount = player.failStrongStarCount + 1;
							if (AchievementManager.getInstance() != null) {
								AchievementManager.getInstance().record(player, RecordAction.连续炼星失败次数, player.failStrongStarCount);
							}
							if (ee.getStar() < 20) {
								try {
									EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.炼星次数, 1L });
									EventRouter.getInst().addEvent(evt2);
								} catch (Exception eex) {
									PlayerAimManager.logger.error("[目标系统] [统计炼星次数异常] [" + player.getLogString() + "]", eex);
								}
							}
						}
						if (bindedTip) {
							ee.setBinded(true);
						}
						if (strongType == 0 && !ee.isBinded()) {
							ee.setBinded(true);
						}

						// 因为必须是铭刻装备所以必须绑定
						// if (!ee.isBinded()) {
						// ee.setBinded(true);
						// }

						QUERY_EQUIPMENT_STRONG_RES qres = queryEquipmentById(player, equipmentId, 0);
						if (qres != null) {
							player.addMessageToRightBag(qres);
						}

						EQUIPMENT_STRONG_RES res = new EQUIPMENT_STRONG_RES(seqNum, equipmentId, (short) ee.getStar(), canStrong);
						player.addMessageToRightBag(res);
						NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
						player.addMessageToRightBag(nreq);
						if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), oldStar, ee.getStar(), totalLuckValue, resultValue, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					} else {
						String description = Translate.请放入正确物品;
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.装备已经强化到了上限;
					try {
						description = Translate.translateString(Translate.装备已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}
	public void confirmNewEquipmentStrongReq(Player player, NEW_EQUIPMENT_STRONG_REQ req, boolean 是20星) {
		confirmNewEquipmentStrongReq(player, req, 是20星, false);
	}

	public void confirmNewEquipmentStrongReq(Player player, NEW_EQUIPMENT_STRONG_REQ req, boolean 是20星, boolean confirm) {
		// long equipmentId, long[] strongMaterialIds, long seqNum, int strongType
		long equipmentId = req.getEquipmentId();
		byte strongType = req.getStrongType();
		long[] strongMaterialIds = req.getStrongMaterialID();
		long seqNum = req.getSequnceNum();
		long[] otherids = req.getOtherStrongMaterialID();
		int[] nums = req.getOtherStrongMaterialNum();
		if (otherids.length != nums.length) {
			logger.warn("[装备强化确认] [失败] [原因：羽化石id长度和个数长度不一致] [" + player.getLogString() + "]  ");
			return;
		}
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && aem != null && am != null) {
			ArticleEntity ae = aem.getEntity(equipmentId);

			// 特殊装备判断
			EquipmentEntity ee = (EquipmentEntity) ae;
			boolean bln = ee.isOprate(player, false, EquipmentEntity.强化);
			if (!bln) {
				return;
			}
			if (strongType == 0) {
				if (!player.bindSilverEnough(strongConsumeMoney(ee))) {
					String description = Translate.金币不足;
					try {
						description = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			} else if (strongType == 1) {
				if (player.getSilver() < strongConsumeMoney(ee)) {
					String description = Translate.元宝不足;
					try {
						description = Translate.translateString(Translate.银子不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(strongConsumeMoney(ee)) } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
			}

			int 强化需要羽化石数量 = 每个等级消耗羽化石的数量[ee.getStar()];
			int 实际羽化石数量 = 0;

			if (nums != null && nums.length > 0) {
				for (int num : nums) {
					实际羽化石数量 += num;
				}
			}

			if (实际羽化石数量 < 强化需要羽化石数量) {
				player.sendError(Translate.text_trade_006);
				if (logger.isWarnEnabled()) logger.warn("[装备强化申请] [失败] [原因：放入格子中的羽化石数量不足,需要" + 强化需要羽化石数量 + "] [实际羽化石数量:" + 实际羽化石数量 + "] [start:" + ee.getStar() + "] [" + player.getLogString() + "]");
				return;
			}

			if (strongMaterialIds == null) {
				String description = Translate.空白;
				description = Translate.请放入强化物品;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			if (strongMaterialIds.length != strongMaterialMaxNumber && !是20星) {
				String description = Translate.空白;
				description = Translate.请输入正确数量;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}!={}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds.length, strongMaterialMaxNumber, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
				if (((EquipmentEntity) ae).getStar() < starMaxValue) {
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					String[] strongMaterialName = 根据装备级别得到所需强化符(e.getPlayerLevelLimit());
					String[] newStrongMaterialName = Arrays.copyOf(strongMaterialName, strongMaterialName.length);
					if (ee.getStar() >= 26) {			//羽化5才可使用高低神练符
						newStrongMaterialName = Arrays.copyOf(newStrongMaterialName, newStrongMaterialName.length + 2);
						newStrongMaterialName[newStrongMaterialName.length-2] = Translate.低级神练符;
						newStrongMaterialName[newStrongMaterialName.length-1] = Translate.高级神练符;
					}
					int[] strongMaterialColorLuck = 根据装备强化等级得到不同颜色强化符的成功率分子值_new(ee.getStar());
					Knapsack[] knapsacks = player.getKnapsacks_common();
					ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
					ArrayList<ArticleEntity> strongMaterialEntitys2 = new ArrayList<ArticleEntity>();
					if (是20星) {
						strongMaterialIds = new long[] { -1, -1, -1, -1 };
					}
					for (long id : strongMaterialIds) {
						if (id != -1) {
							ArticleEntity strongMaterialEntity = aem.getEntity(id);
							if (strongMaterialEntity == null) {
								String description = Translate.空白;
								description = Translate.请放入强化物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, id, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							boolean isStrongMaterial = false;
							if (newStrongMaterialName != null) {
								for (String strongName : newStrongMaterialName) {
									if (strongName != null && strongName.equals(strongMaterialEntity.getArticleName())) {
										isStrongMaterial = true;
										break;
									}
								}
							}
							if (!isStrongMaterial) {
								String description = Translate.请放入正确物品;
								try {
									description = Translate.translateString(Translate.该物品不是需要的物品, new String[][] { { Translate.ARTICLE_NAME_1, strongMaterialEntity.getArticleName() } });
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							boolean hasStrongMaterial = false;
							if (knapsacks != null) {
								for (Knapsack knapsack : knapsacks) {
									if (knapsack != null) {
										int index = knapsack.indexOf(strongMaterialEntity);
										if (index != -1) {
											hasStrongMaterial = true;
											break;
										}
									}
								}
							}
							if (!hasStrongMaterial) {
								String description = Translate.空白;
								description = Translate.请放入强化物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}背包中没有id为{}的物品] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialEntity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
								return;
							}
							strongMaterialEntitys.add(strongMaterialEntity);
						}
					}
					if (strongMaterialEntitys.isEmpty() && !是20星) {
						String description = Translate.空白;
						description = Translate.请放入强化物品;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					boolean hasEquipment = false;
					if (knapsacks != null) {
						for (Knapsack knapsack : knapsacks) {
							if (knapsack != null) {
								if (!hasEquipment) {
									int index1 = knapsack.hasArticleEntityByArticleId(equipmentId);
									if (index1 != -1) {
										hasEquipment = true;
										break;
									}
								}
							}
						}
					}
					if (hasEquipment) {
						boolean bindedTip = false;
						if (!ee.isBinded()) {
							for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
								if (strongMaterialEntity != null && strongMaterialEntity.isBinded()) {
									bindedTip = true;
									break;
								}
							}
						}
						int calLevel = ((EquipmentEntity) ae).getStar() - 20;
						String currRealBaoDiName = "";
						if (calLevel >= 6 && calLevel <= 20) {
							currRealBaoDiName = Translate.羽化 + (calLevel - 1) + 级保底符;
						}
						boolean isBaoDi = false;
						if (logger.isDebugEnabled()) {
							logger.debug("[name:" + player.getName() + "] [isBaoDi:" + isBaoDi + "] [otherids:" + Arrays.toString(otherids) + "] [currRealBaoDiName:" + currRealBaoDiName + "] [-----1]");
						}
						if (otherids != null && otherids.length > 0) {
							for (long id : otherids) {
								ArticleEntity otherAE = ArticleEntityManager.getInstance().getEntity(id);
								if (logger.isDebugEnabled()) {
									logger.debug("[name:" + player.getName() + "] [isBaoDi:" + isBaoDi + "] [otherids:" + Arrays.toString(otherids) + "] [currRealBaoDiName:" + currRealBaoDiName + "] [otherAE:" + (otherAE != null ? otherAE.getArticleName() : "null") + "]");
								}
								if (otherAE != null && otherAE.getArticleName().equals(currRealBaoDiName)) {
									isBaoDi = true;
								}
								if (otherAE != null && otherAE.isBinded()) {
									bindedTip = true;
								}
							}
						}

						if (logger.isDebugEnabled()) {
							logger.debug("[name:" + player.getName() + "] [isBaoDi:" + isBaoDi + "] [otherids:" + Arrays.toString(otherids) + "] [currRealBaoDiName:" + currRealBaoDiName + "] [----2]");
						}
						if (!confirm && ee.getStar() >= 26 && !isBaoDi) {
							WindowManager wm = WindowManager.getInstance();
							MenuWindow mw = wm.createTempMenuWindow(600);
							Option_BaodiStronger option1 = new Option_BaodiStronger();
							option1.set是20星(是20星);
							option1.setReq(req);
							option1.setText(MinigameConstant.CONFIRM);
							Option_cancel option2 = new Option_cancel();
							option2.setText(MinigameConstant.CANCLE);
							Option[] options = new Option[] {option1, option2};
							mw.setOptions(options);
							String msg = Translate.没有保底符;		
							mw.setDescriptionInUUB(msg);
							CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
							player.addMessageToRightBag(creq);
							return ;
						}

						int totalLuckValue = 0;
						for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
							if (strongMaterialEntity != null) {
								try {
									Article a = ArticleManager.getInstance().getArticle(strongMaterialEntity.getArticleName());
									AddLuckyCostEnum ace = AddLuckyCostEnum.valueOf(a.getName_stat(), strongMaterialEntity.getColorType());
									if (ace == null || ace.getAddRate(ee) <= 0) {
										totalLuckValue += strongMaterialColorLuck[strongMaterialEntity.getColorType()];
									} else {
										totalLuckValue += ace.getAddRate(ee);
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
						int tempLuckNum = 0;
						if (EquipStarManager.是否开启幸运值系统 && ee.getStar() >= 26) {
							EquipExtraData eed = EquipStarManager.getInst().getEntity(ee);
							if (eed != null) {
								if (logger.isDebugEnabled()) {
									logger.debug("[获取装备幸运值] [成功] [" + player.getLogString() + "] [" + ee.getId() + "] [当前星级:"+ee.getStar()+"] [幸运值:" + eed.getLuckNum(ee) + "]");
								}
								tempLuckNum = eed.getLuckNum(ee);
								totalLuckValue += tempLuckNum;
							}
						}
						if (totalLuckValue > TOTAL_LUCK_VALUE) {
							totalLuckValue = TOTAL_LUCK_VALUE;
						}
						double percent = totalLuckValue * 1.0 / TOTAL_LUCK_VALUE;
						if (percent > 1) {
							percent = 1;
						}

						if (是20星) {
							percent = 0.3;
							totalLuckValue = (int) (TOTAL_LUCK_VALUE * 0.3);
						}

						if (knapsacks != null) {

							if (ee.getStar() >= 20 && otherids != null && otherids.length > 0) {

								/**
								 * 新加保底符功能，不改协议，delindex为删除精华羽化石的数量
								 */
								int delindex = 0;
								int delBaoDIFuNums = 0;
								for (int j = 0; j < otherids.length; j++) {
									long id = otherids[j];
									for (int k = 0; k < nums[j]; k++) {
										ArticleEntity delAe = ArticleEntityManager.getInstance().getEntity(id);
										if (delAe == null) {
											continue;
										}
										if (delAe.getArticleName().contains(Translate.羽化石)) {
											if (delindex >= 强化需要羽化石数量) {
												continue;
											}
										}
										if (delAe.getArticleName().contains(Translate.保底符)) {
											if (delBaoDIFuNums >= 1) {
												continue;
											}
										}
										ArticleEntity aee1 = player.removeArticleEntityFromKnapsackByArticleId(id, "强化删除", true);
										if (aee1 == null) {
											String description = Translate.删除物品不成功;
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
											player.addMessageToRightBag(hreq);
											if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [大于20星]  [删除额外的] [强化需要羽化石数量:" + 强化需要羽化石数量 + "] [id:" + id + "] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms2]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
											return;
										} else {
											// 统计
											strongMaterialEntitys2.add(aee1);
											ArticleStatManager.addToArticleStat(player, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "强化删除", null);
											if (delAe.getArticleName().contains(Translate.羽化石)) {
												delindex++;
											} else if (delAe.getArticleName().contains(Translate.保底符)) {
												delBaoDIFuNums++;
											}
										}
									}
								}

							}

							if (!是20星) {
								for (long strongMaterialId : strongMaterialIds) {
									if (strongMaterialId != -1) {
										ArticleEntity aee1 = player.removeArticleEntityFromKnapsackByArticleId(strongMaterialId, "强化删除", true);
										if (aee1 == null) {
											String description = Translate.删除物品不成功;
											HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
											player.addMessageToRightBag(hreq);
											if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
											return;
										} else {
											// 统计
											ArticleStatManager.addToArticleStat(player, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "强化删除", null);
											// 活跃度统计
											ActivenessManager.getInstance().record(player, ActivenessType.强化装备);
											strongMaterialEntitys2.add(aee1);
										}
									}
								}
							}
						}

						// 扣费
						try {
							if (strongType == 0) {
								BillingCenter bc = BillingCenter.getInstance();
								bc.playerExpense(player, strongConsumeMoney(ee), CurrencyType.BIND_YINZI, ExpenseReasonType.EQUIPMENT_UPGRADE, "用绑银强化装备", VipExpActivityManager.default_add_rmb_expense);
							} else if (strongType == 1) {
								BillingCenter bc = BillingCenter.getInstance();
								bc.playerExpense(player, strongConsumeMoney(ee), CurrencyType.YINZI, ExpenseReasonType.EQUIPMENT_UPGRADE, "用银子强化装备", VipExpActivityManager.default_add_rmb_expense);
							}
							// if (strongMaterialEntitys != null && strongMaterialEntitys.size() > 0) {
							// try {
							// ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
							// } catch (Exception e2) {
							// TransitRobberyManager.logger.error("[使用赠送活动报错]", e2);
							// }
							// } else
							if (strongMaterialEntitys2 != null && strongMaterialEntitys2.size() > 0) {
								try {
									ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys2);
								} catch (Exception e2) {
									TransitRobberyManager.logger.error("[使用赠送活动报错]", e2);
								}
							}
							if (ee.getStar() >= 20) {
								try {
									EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.羽化装备次数, 1L });
									EventRouter.getInst().addEvent(evt2);
								} catch (Exception eex) {
									PlayerAimManager.logger.error("[目标系统] [统计购买周月礼包次数异常] [" + player.getLogString() + "]", eex);
								}
							}

						} catch (Exception ex) {
							ex.printStackTrace();
							if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
							return;
						}
						int resultValue = random.nextInt(TOTAL_LUCK_VALUE) + 1;
						boolean success = false;
						if (logger.isInfoEnabled()) {
							logger.info("[练星] [" + player.getLogString() + "] [aeId:" + ae.getId() + "] [随机值:" + resultValue + "] [totalLuckValue:" + totalLuckValue + "] [幸运值:" + tempLuckNum + "]");
						}
						if (totalLuckValue >= resultValue) {
							success = true;
						}
						boolean canStrong = true;
						int oldStar = ee.getStar();
						// logger.warn("[装备强化确认] [hasEquipment] [resultValue:" + resultValue + "] [totalLuckValue:" + totalLuckValue + "] [oldStar:" + oldStar +
						// "] [...............4]");
						if (success) {
							EquipExtraData eed = EquipStarManager.getInst().getEntity(ee);
							if (eed != null) {
								eed.success(ee);
							}
							ee.setStar(ee.getStar() + 1);
							if (ee.getStar() >= starMaxValue) {
								ee.setStar(starMaxValue);
								canStrong = false;
							}
							if (ee.getOnceMaxStar() < ee.getStar()) {
								int a = ee.getStar() / 2;
								int b = ee.getStar() % 2;
								if (ee.getOnceMaxStar() <= 装备炼星需要羽化的等级) {
									if (a >= 5 && b == 0) {
										try {
											ChatMessageService cms = ChatMessageService.getInstance();
											ChatMessage msg = new ChatMessage();
											// 恭喜！<国家>的<玩家姓名>经历千辛万苦将<装备名>炼到<星级>。（世界）
											String descri = Translate.translateString(Translate.装备强化系统广播详细, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, ee.getArticleName() }, { Translate.COUNT_1, ee.getStar() + "" } });
											if (ee.getStar() <= 装备炼星需要羽化的等级) {
												if (a == 5) {
													descri = "<f color='" + COLOR_WHITE + "'>" + descri + "</f>";
												} else if (a == 6) {
													descri = "<f color='" + COLOR_GREEN + "'>" + descri + "</f>";
												} else if (a == 7) {
													descri = "<f color='" + COLOR_BLUE + "'>" + descri + "</f>";
												} else if (a == 8) {
													descri = "<f color='" + COLOR_PURPLE + "'>" + descri + "</f>";
												} else if (a == 9) {
													descri = "<f color='" + COLOR_ORANGE + "'>" + descri + "</f>";
												} else if (a == 10) {
													descri = "<f color='" + COLOR_ORANGE + "'>" + descri + "</f>";

													if (isGive20Xin) {
														String serverName = GameConstants.getInstance().getServerName();
														if (serverName.equals("新功能测试") || serverName.equals("雄霸天下") || serverName.equals("仙界至尊") || serverName.equals("金蛇圣殿") || serverName.equals("百花仙谷") || serverName.equals("傲剑凌云")) {
															if (Give20Xin_ent_time == 0) {
																SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
																Give20Xin_ent_time = format.parse("2013-01-02 23:59:59").getTime();
															}
															long now = System.currentTimeMillis();
															if (now < Give20Xin_ent_time) {
																try {
																	Article xing20 = ArticleManager.getInstance().getArticle(Translate.二十星);
																	ArticleEntity entity20 = ArticleEntityManager.getInstance().createEntity(xing20, true, ArticleEntityManager.LIANXING_ACTIVITY, player, xing20.getColorType(), 1, true);
																	MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { entity20 }, "恭喜获得[星星相映]返礼", "您在[星星相映]活动期间成功将装备炼到20星，特奖励您20级破碎星辰一颗，请查收附件。活动到1月2日结束!", 0, 0, 0, "炼星到20星送20星");
																	logger.warn("[炼到20星送星星] [{}] [{}] [{}]", new Object[] { player.getLogString(), entity20.getArticleName(), entity20.getId() });
																} catch (Exception ex) {
																	logger.error("[炼星20星送出错] " + player.getLogString(), ex);
																}
															}
														}
													}
												}
											}

											msg.setMessageText(descri);
											cms.sendMessageToSystem(msg);

										} catch (Exception ex) {

										}
									}
								} else {
									try {
										ChatMessageService cms = ChatMessageService.getInstance();
										ChatMessage msg = new ChatMessage();
										String descri = Translate.translateString(Translate.装备强化系统广播详细_羽化, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, ee.getArticleName() }, { Translate.STRING_3, 获得对应的羽化描述.get(new Integer(ee.getStar())) } });
										descri = "<f color='" + COLOR_ORANGE_2 + "'>" + descri + "</f>";
										msg.setMessageText(descri);
										cms.sendMessageToSystem(msg);
									} catch (Exception e1) {
										e1.printStackTrace();
										logger.error("21星以上发送世界公告出错");
									}
								}

								ee.setOnceMaxStar(ee.getStar());
							}
							if (ee.getStar() > 20 && ee.isInscriptionFlag()) {
								try {
									AchievementManager.getInstance().record(player, RecordAction.羽化装备成功次数);
								} catch (Exception eex) {
									PlayerAimManager.logger.error("[目标系统] [统计装备羽化成功次数] [异常] [" + player.getLogString() + "]", eex);
								}
							}
							String description = Translate.恭喜你装备升级成功;
							try {
								description = Translate.translateString(Translate.装备强化成功, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() }, new String[] { Translate.LEVEL_1, ee.getStar() + "" } });
								if (ee.getStar() > 20) {
									description = Translate.translateString(Translate.装备羽化成功, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() }, new String[] { Translate.STRING_1, 获得对应的羽化描述.get(new Integer(ee.getStar())) } });
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
							player.addMessageToRightBag(hreq);
							QUERY_ARTICLE_INFO_RES qres = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), ee.getInfoShow(player));
							player.addMessageToRightBag(qres);
							PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备升级成功, (byte) 1, AnimationManager.动画播放位置类型_武器强化中间孔, 0, 0);
							if (pareq != null) {
								player.addMessageToRightBag(pareq);
							}
							QUERY_ARTICLE_RES res = new QUERY_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[0], new com.fy.engineserver.datasource.article.entity.client.PropsEntity[0], new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
							player.addMessageToRightBag(res);
							logger.warn("[装备强化确认] [炼星成功。。。。。。。。。。。。。。。。。。] [" + ee.getArticleName() + "] [" + ee.getStar() + "] [CoreSubSystem.translate(ee):" + CoreSubSystem.translate(ee).getIconId() + "]");
							player.successStrongStarCount = player.successStrongStarCount + 1;
							player.failStrongStarCount = 0;
							if (AchievementManager.getInstance() != null) {
								AchievementManager.getInstance().record(player, RecordAction.连续炼星成功次数, player.successStrongStarCount);
								AchievementManager.getInstance().record(player, RecordAction.全身装备最大星级, ee.getStar());
							}
							if (ee.getStar() < 20) {
								try {
									EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.炼星次数, 1L });
									EventRouter.getInst().addEvent(evt2);
								} catch (Exception eex) {
									PlayerAimManager.logger.error("[目标系统] [统计炼星次数异常] [" + player.getLogString() + "]", eex);
								}
							}
						} else {
							if (20 <= ee.getStar() && ee.getStar() < 26) {
								String description = Translate.很遗憾装备羽化失败;
								try {
									description = Translate.translateString(Translate.装备强化失败, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() }, new String[] { Translate.LEVEL_1, ee.getStar() + "" } });
									if (ee.getStar() > 20) {
										description = Translate.translateString(Translate.装备羽化失败, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() }, new String[] { Translate.STRING_1, 获得对应的羽化描述.get(new Integer(ee.getStar())) } });
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
								player.addMessageToRightBag(hreq);
								PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备升级失败, (byte) 1, AnimationManager.动画播放位置类型_武器强化中间孔, 0, 0);
								if (pareq != null) {
									player.addMessageToRightBag(pareq);
								}
								logger.error("[炼星20星] [不扣星] [name:" + player.getName() + "] [ee.getStar():" + ee.getStar() + "] [description:" + description + "] [=======]");
								return;
							}
							EquipExtraData eed = EquipStarManager.getInst().getEntity(ee);
							if (eed != null) {
								eed.failure(ee, strongMaterialEntitys2);
							}
							// 强化失败降级
							int dropStar = 强化失败后降级();
							if (!isBaoDi) {
								if (ee.getStar() <= dropStar) {
									ee.setStar(0);
								} else {
									ee.setStar(ee.getStar() - dropStar);
								}
							}
							String description = Translate.很遗憾装备没有升级成功;
							try {
								description = Translate.translateString(Translate.装备强化失败, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() }, new String[] { Translate.LEVEL_1, ee.getStar() + "" } });
								if (ee.getStar() > 20) {
									description = Translate.translateString(Translate.装备羽化失败, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() }, new String[] { Translate.STRING_1, 获得对应的羽化描述.get(new Integer(ee.getStar())) } });
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
							player.addMessageToRightBag(hreq);
							PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备升级失败, (byte) 1, AnimationManager.动画播放位置类型_武器强化中间孔, 0, 0);
							if (pareq != null) {
								player.addMessageToRightBag(pareq);
							}
							player.successStrongStarCount = 0;
							player.failStrongStarCount = player.failStrongStarCount + 1;
							if (AchievementManager.getInstance() != null) {
								AchievementManager.getInstance().record(player, RecordAction.连续炼星失败次数, player.failStrongStarCount);
							}
							if (ee.getStar() < 20) {
								try {
									EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.炼星次数, 1L });
									EventRouter.getInst().addEvent(evt2);
								} catch (Exception eex) {
									PlayerAimManager.logger.error("[目标系统] [统计炼星次数异常] [" + player.getLogString() + "]", eex);
								}
							}
						}
						if (bindedTip) {
							ee.setBinded(true);
						}
						if (strongType == 0 && !ee.isBinded()) {
							ee.setBinded(true);
						}

						QUERY_NEW_EQUIPMENT_STRONG_RES qres2 = new QUERY_NEW_EQUIPMENT_STRONG_RES(GameMessageFactory.nextSequnceNum(), equipmentId, strongConsumeMoney(ee), Translate.空白, 根据装备级别得到所需强化符(e.getPlayerLevelLimit()), 根据装备强化等级得到不同颜色强化符的成功率分子值_new(ee.getStar()), 羽化石, 每个等级消耗羽化石的数量[ee.getStar()], 0);
						player.addMessageToRightBag(qres2);
						NEW_EQUIPMENT_STRONG_RES res = new NEW_EQUIPMENT_STRONG_RES(seqNum, equipmentId, (short) ee.getStar(), canStrong);
						player.addMessageToRightBag(res);
						NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
						player.addMessageToRightBag(nreq);
						if (logger.isWarnEnabled()) logger.warn("[操作类别:装备强化确认] [状态:成功] [username:{}] [id:{}] [name:{}] [ae:{}] [articleId:{}] [强化前星级:{}] [强化后星级:{}] [totalLuckValue:{}] [resultValue:{}] [是否保底符保级:{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), oldStar, ee.getStar(), totalLuckValue, resultValue, isBaoDi, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					} else {
						String description = Translate.请放入正确物品;
						try {
							description = Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
				} else {
					String description = Translate.装备已经羽化到了上限;
					try {
						description = Translate.translateString(Translate.装备已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入装备;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[装备强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	public int 强化失败后降级() {
		int result = 0;
		result = ProbabilityUtils.randomProbability(random, new double[] { 0.85, 0.10, 0.5 });
		return result + 1;
	}

	public QUERY_ARTICLE_COMPOSE_RES queryArticleComposeReq(Player player, QUERY_ARTICLE_COMPOSE_REQ req) {
		if (req == null || player == null) {
			return null;
		}
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (am == null || aem == null) {
			return null;
		}
		long articleId = req.getArticleId();
		if (articleId == -1) {
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[物品合成查询] [失败] [user] [{}] [playerid:{}] [{}] [articleId:{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), articleId });
			return null;
		}

		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(articleId);
		int jianding = 是否合成鉴定符(player, ae);
		if (jianding >= 0) {
			QUERY_ARTICLE_COMPOSE_RES res = new QUERY_ARTICLE_COMPOSE_RES(req.getSequnceNum(), -1, JianDingMoney, "");
			return res;
		}
		if (ae != null && ae instanceof EquipmentEntity) {
			EquipmentEntity ee = (EquipmentEntity) ae;
			boolean bln = ee.isOprate(player, false, EquipmentEntity.装备合成);
			if (!bln) {
				return null;
			}
		}
		Knapsack[] knapsacks = player.getKnapsacks_common();
		if (knapsacks == null) {
			return null;
		}
		boolean has = false;
		for (Knapsack knapsack : knapsacks) {
			if (knapsack != null) {
				int index = knapsack.hasArticleEntityByArticleId(articleId);
				if (index >= 0) {
					has = true;
				}
			}
		}
		if (!has) {
			String description = Translate.空白;
			description = Translate.请放入正确物品;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
			player.addMessageToRightBag(hreq);
			return null;
		}

		ae = aem.getEntity(articleId);
		if (ae == null) {
			String description = Translate.空白;
			description = Translate.服务器物品出现错误;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
			player.addMessageToRightBag(hreq);
			return null;
		}
		Article a = am.getArticle(ae.getArticleName());
		if (a == null) {
			String description = Translate.空白;
			description = Translate.服务器物品出现错误;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
			player.addMessageToRightBag(hreq);
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[物品合成查询] [失败] [user] [{}] [playerid:{}] [{}] [没有名为{}的物种]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName() });
			return null;
		}

		if (!(a instanceof ComposeInterface)) {
			String description = Translate.空白;
			description = Translate.物品不能合成;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
			player.addMessageToRightBag(hreq);
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[物品合成查询] [失败] [user] [{}] [playerid:{}] [{}] [{}不能合成]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName() });
			return null;
		}
		ComposeInterface ia = (ComposeInterface) a;
		long tempId = ia.getTempComposeEntityId(player, ae, false);

		boolean isnotice = false;
		Article xiazi = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if (xiazi != null && xiazi instanceof InlayArticle) {
			if (((InlayArticle) xiazi).getInlayType() > 1) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.神匣道具为不可合成道具);
				player.addMessageToRightBag(hreq);
				isnotice = true;
			}
		}

		if (tempId == -1 && !isnotice) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.物品已经不需要合成了);
			player.addMessageToRightBag(hreq);
		}
		int cost = 物品合成消耗银子(ae, 1);
		QUERY_ARTICLE_COMPOSE_RES res = new QUERY_ARTICLE_COMPOSE_RES(req.getSequnceNum(), tempId, cost, ia.getTempComposeEntityDescription(player, ae, false));
		return res;
	}

	/**
	 * 申请物品合成
	 * @param player
	 * @param req
	 */

	public void articleComposeReq(Player player, ARTICLE_COMPOSE_REQ req) {
		if (player == null || req == null) {
			return;
		}
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (aem == null) {
			return;
		}
		long[] articleIds = req.getArticleIds();
		int[] articleCounts = req.getArticleCounts();
		int composeType = req.getComposeType();
		if (articleIds == null || articleCounts == null || articleIds.length != articleCounts.length) {
			String description = Translate.空白;
			description = Translate.物品不能合成;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
			player.addMessageToRightBag(hreq);
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[申请物品合成] [失败] [user] [{}] [playerid:{}] [{}] [{}不能合成]", new Object[] { player.getUsername(), player.getId(), player.getName(), description });
			return;
		}
		ArrayList<ArticleEntity> ael = new ArrayList<ArticleEntity>();
		ArrayList<Integer> acl = new ArrayList<Integer>();
		for (int i = 0; i < articleIds.length; i++) {
			long id = articleIds[i];
			if (id > 0) {
				ArticleEntity ae = aem.getEntity(id);
				if (ae != null) {
					ael.add(ae);
					acl.add(articleCounts[i]);
				}
			}
		}

		int jianding = 是否合成鉴定符(player, ael.get(0));
		if (jianding >= 0) {
			// 鉴定符合成
			// 物品数目是否够
			try {
				int allC = 0;
				for (int i = 0; i < ael.size(); i++) {
					ArticleEntity aa = ael.get(i);
					int count = player.getArticleEntityNum(aa.getId());
					allC += acl.get(i);
					if (count < acl.get(i)) {
						player.sendError(Translate.物品个数不足);
						return;
					}
				}
				if (allC < COMPOSE_NO) {
					player.sendError(Translate.物品个数不足);
					return;
				}
				if (player.getKnapsack_common().getEmptyNum() <= 0) {
					player.sendError(Translate.背包空间不足);
					return;
				}
				if (composeType == 0) {
					long cost = JianDingMoney;
					if (!player.bindSilverEnough(cost)) {
						String result = Translate.金币不足;
						try {
							result = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(cost) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						BillingCenter.银子不足时弹出充值确认框(player, result);
						return;
					}
				} else {
					long cost = JianDingMoney;
					if (player.getSilver() < cost) {
						String result = Translate.元宝不足;
						try {
							result = Translate.translateString(Translate.银子不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(cost) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						BillingCenter.银子不足时弹出充值确认框(player, result);
						return;
					}
				}
				if (!ael.get(0).isBinded()) {
					boolean isBind = false;
					for (ArticleEntity aa : ael) {
						if (aa.isBinded()) {
							isBind = true;
							break;
						}
					}
					if (isBind && composeType != 0) {
						WindowManager wm = WindowManager.getInstance();
						MenuWindow mw = wm.createTempMenuWindow(600);
						String title = Translate.合成提示信息;
						mw.setTitle(title);
						String desUUB = Translate.合成提示信息;
						try {
							if (composeType == 0) {
								desUUB = Translate.translateString(Translate.合成消耗绑银绑定, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(JianDingMoney) } });
							} else if (composeType == 1) {
								desUUB = Translate.translateString(Translate.合成消耗元宝提示绑定信息, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(JianDingMoney) } });
							}
						} catch (Exception ex) {
							logger.warn("鉴定合成:", ex);
						}
						mw.setDescriptionInUUB(desUUB);
						Option_ArticleComposeConfirm option = new Option_ArticleComposeConfirm();
						option.setReq(req);
						option.setText(Translate.确定);
						Option[] options = new Option[] { option, new Option_Cancel() };
						mw.setOptions(options);
						CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
						player.addMessageToRightBag(creq);
					} else {
						confirmArticleComposeReq(player, articleIds, articleCounts, composeType);
					}
				} else {
					confirmArticleComposeReq(player, articleIds, articleCounts, composeType);
				}
			} catch (Exception e) {
				logger.error("", e);
			}
			return;
		}

		int[] counts = new int[acl.size()];
		for (int i = 0; i < acl.size(); i++) {
			counts[i] = acl.get(i);
		}
		ArticleEntity[] aes = ael.toArray(new ArticleEntity[0]);
		for (ArticleEntity ae : aes) {
			if (ae instanceof EquipmentEntity) {
				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.装备合成);
				if (!bln) {
					return;
				}
			}
		}
		String result = 合成合法性判断(player, aes, counts, composeType);
		int count = 0;
		boolean bindedTip = false;
		for (int i = 0; i < aes.length && i < counts.length; i++) {
			ArticleEntity ae = aes[i];
			if (ae == null) {
				result = Translate.请放入正确物品;
				return;
			}
			if (ae.isBinded()) {
				bindedTip = true;
			}
			Article a = getArticle(ae.getArticleName());
			if (!(a instanceof ComposeInterface)) {
				result = Translate.请放入正确物品;
				return;
			}
			count += counts[i];
		}
		if (composeType == 0) {
			bindedTip = true;
		}
		if (count < COMPOSE_NO) {
			result = Translate.物品个数不足;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[申请物品合成] [失败] [user] [{}] [playerid:{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result });
			return;
		}
		count = count / COMPOSE_NO;
		// 主物品
		ArticleEntity mainEntity = aes[0];

		// 如果主物品为绑定，那么不提示绑定信息
		if (mainEntity.isBinded()) {
			bindedTip = false;
		}
		if (result == null) {
			if (!bindedTip) {
				confirmArticleComposeReq(player, articleIds, articleCounts, composeType);
				return;
			}

			// 提示玩家信息
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			String title = Translate.合成提示信息;
			mw.setTitle(title);
			String desUUB = Translate.合成提示信息;
			try {
				if (composeType == 0) {
					desUUB = Translate.translateString(Translate.合成消耗绑银绑定, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(物品合成消耗银子(mainEntity, count)) } });
				} else if (composeType == 1) {
					desUUB = Translate.translateString(Translate.合成消耗元宝提示绑定信息, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(物品合成消耗银子(mainEntity, count)) } });
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			mw.setDescriptionInUUB(desUUB);
			Option_ArticleComposeConfirm option = new Option_ArticleComposeConfirm();
			option.setReq(req);
			option.setText(Translate.确定);
			Option[] options = new Option[] { option, new Option_Cancel() };
			mw.setOptions(options);
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(creq);
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[申请物品合成] [成功发送确认提示] [user] [{}] [playerid:{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
			return;
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[申请物品合成] [失败] [user] [{}] [playerid:{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result });
			return;
		}
	}

	/**
	 * 确认物品合成
	 * 合成后的物品可能是新物品，可能是原有物品颜色升级
	 * 根据参数的物种得到合成后的物体
	 * 如装备合成，是有一个主装备，其余几个副装备，主装备颜色升级
	 * 宝石合成，同样的宝石5个可以合成更高一级的宝石
	 * 一般合成，同样颜色的5个相同物品可以合成更好颜色的这类物品
	 * @param player
	 * @param req
	 */
	public void confirmArticleComposeReq(Player player, long[] articleIds, int[] articleCounts, int composeType) {
		if (player == null) {
			return;
		}
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (aem == null) {
			return;
		}
		if (articleIds == null || articleCounts == null || articleIds.length != articleCounts.length) {
			String description = Translate.空白;
			description = Translate.物品不能合成;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
			player.addMessageToRightBag(hreq);
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[确认物品合成] [失败] [user] [{}] [playerid:{}] [{}] [{}不能合成]", new Object[] { player.getUsername(), player.getId(), player.getName(), description });
			return;
		}
		ArrayList<ArticleEntity> ael = new ArrayList<ArticleEntity>();
		StringBuffer sb = new StringBuffer();
		ArrayList<Integer> acl = new ArrayList<Integer>();
		for (int i = 0; i < articleIds.length; i++) {
			long id = articleIds[i];
			if (id > 0) {
				ArticleEntity ae = aem.getEntity(id);
				if (ae != null) {
					ael.add(ae);
					acl.add(articleCounts[i]);
					sb.append(" ").append(ae.getArticleName()).append(",id:").append(ae.getId()).append(",count:").append(articleCounts[i]);
				}
			}
		}

		int jianding = 是否合成鉴定符(player, ael.get(0));
		if (jianding >= 0) {
			try {
				int allC = 0;
				for (int i = 0; i < ael.size(); i++) {
					ArticleEntity aa = ael.get(i);
					int count = player.getArticleEntityNum(aa.getId());
					allC += acl.get(i);
					if (count < articleCounts[i]) {
						player.sendError(Translate.物品个数不足);
						return;
					}
				}
				// 计算成5倍数
				allC = allC / COMPOSE_NO * COMPOSE_NO;
				if (allC < COMPOSE_NO) {
					player.sendError(Translate.物品个数不足);
					return;
				}
				if (player.getKnapsack_common().getEmptyNum() <= 0) {
					player.sendError(Translate.背包空间不足);
					return;
				}
				if (composeType == 0) {
					long cost = JianDingMoney * allC / COMPOSE_NO;
					if (!player.bindSilverEnough(cost)) {
						String result = Translate.金币不足;
						try {
							result = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(cost) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						BillingCenter.银子不足时弹出充值确认框(player, result);
						return;
					}
				} else {
					long cost = JianDingMoney * allC / COMPOSE_NO;
					if (player.getSilver() < cost) {
						String result = Translate.元宝不足;
						try {
							result = Translate.translateString(Translate.银子不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(cost) } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						BillingCenter.银子不足时弹出充值确认框(player, result);
						return;
					}
				}
				// 扣钱，删除物品，给物品
				boolean isBind = false;
				if (composeType == 0) {
					isBind = true;
					try {
						BillingCenter.getInstance().playerExpense(player, JianDingMoney * allC / COMPOSE_NO, CurrencyType.BIND_YINZI, ExpenseReasonType.EQUIPMENT_COMPOSED, "鉴定符合成");
					} catch (Exception e) {
						logger.error("鉴定符失败B" + player.getLogString(), e);
						return;
					}
				} else {
					try {
						BillingCenter.getInstance().playerExpense(player, JianDingMoney * allC / COMPOSE_NO, CurrencyType.YINZI, ExpenseReasonType.EQUIPMENT_COMPOSED, "鉴定符合成");
					} catch (Exception e) {
						logger.error("鉴定符合成失败" + player.getLogString(), e);
						return;
					}
				}
				int removeNum = allC;
				for (int i = 0; i < ael.size(); i++) {
					for (int j = 0; j < acl.get(i); j++) {
						ArticleEntity re = player.removeArticleEntityFromKnapsackByArticleId(ael.get(i).getId(), "鉴定符合成删除", false);
						if (re == null) {
							player.sendError(Translate.删除物品不成功);
							logger.error("[鉴定符合成删除不成功] [{}] [{}]", new Object[] { player.getLogString(), sb.toString() });
							return;
						} else {
							if (re.isBinded()) {
								isBind = true;
							}
							ArticleStatManager.addToArticleStat(player, null, re, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "合成删除", null);
						}
						removeNum--;
						if (removeNum <= 0) {
							break;
						}
					}
					if (removeNum <= 0) {
						break;
					}
				}
				String name = jianDingFus[jianding + 1];
				Article a = getArticle(name);
				ArticleEntity e = ArticleEntityManager.getInstance().createEntity(a, isBind, ArticleEntityManager.CREATE_REASON_COMPOSE_ARTICLE, player, a.getColorType(), allC / COMPOSE_NO, true);
				player.putToKnapsacks(e, allC / COMPOSE_NO, "鉴定符合成");
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.合成成功);
				player.addMessageToRightBag(hreq);
				ArticleStatManager.addToArticleStat(player, null, e, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, allC / COMPOSE_NO, "合成获得", null);
				logger.warn("[鉴定符合成] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getLogString(), allC / COMPOSE_NO, e.getArticleName(), e.getId(), e.isBinded() });
			} catch (Exception e) {
				logger.error("鉴定符合成出错", e);
			}
			return;
		}

		for (ArticleEntity ae : ael) {
			if (ae instanceof EquipmentEntity) {
				// 特殊装备判断
				EquipmentEntity ee = (EquipmentEntity) ae;
				boolean bln = ee.isOprate(player, false, EquipmentEntity.装备合成);
				if (!bln) {
					return;
				}
			}
		}

		int[] counts = new int[acl.size()];
		for (int i = 0; i < acl.size(); i++) {
			counts[i] = acl.get(i);
		}
		String result = 合成合法性判断(player, ael.toArray(new ArticleEntity[0]), counts, composeType);
		if (result == null) {
			String resultDescription = 进行物品合成消耗且合成(player, ael.toArray(new ArticleEntity[0]), counts, composeType);
			if (resultDescription == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.合成成功);
				player.addMessageToRightBag(hreq);
				if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[确认物品合成] [成功] [user] [{}] [playerid:{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), sb.toString() });
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, resultDescription);
				player.addMessageToRightBag(hreq);
				if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[确认物品合成] [失败] [user] [{}] [playerid:{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), sb.toString(), resultDescription });
			}

			return;
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[确认物品合成] [失败] [user] [{}] [playerid:{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result });
			return;
		}
	}

	/**
	 * 判断是否可以合成，各种合成条件不一样，需要不同处理
	 * 如装备合成，是有一个主装备，其余几个副装备，还有一些其他条件
	 * 宝石合成，同样的宝石5个可以合成更高一级的宝石
	 * 一般合成，同样颜色的5个相同物品可以合成更好颜色的这类物品
	 * composeType为合成类型，0表示合成绑定 1表示合成不绑定，但当合成物品中有绑定物品时合成出来的物品依然绑定
	 * 返回值为null表示合法，返回值不为空表示不合法，返回值为原因
	 * @param ae
	 * @return 返回值为null表示合法，返回值不为空表示不合法，返回值为原因
	 */
	public String 合成合法性判断(Player player, ArticleEntity[] aes, int[] counts, int composeType) {
		String result = null;
		if (aes == null || aes.length == 0 || counts == null || counts.length == 0 || aes.length != counts.length) {
			result = Translate.请放入正确物品;
			return result;
		}
		for (int i = 0; i < aes.length && i < counts.length; i++) {
			int count = player.getArticleEntityNum(aes[i].getId());
			if (count < counts[i]) {
				result = Translate.物品个数不足;
				return result;
			}
		}
		int count = 0;
		for (int i = 0; i < aes.length && i < counts.length; i++) {
			ArticleEntity ae = aes[i];
			if (ae == null) {
				result = Translate.请放入正确物品;
				return result;
			}
			Article a = getArticle(ae.getArticleName());
			if (!(a instanceof ComposeInterface)) {
				result = Translate.请放入正确物品;
				return result;
			}
			count += counts[i];
		}
		if (count < COMPOSE_NO) {
			result = Translate.物品个数不足;
			return result;
		}
		count = count / COMPOSE_NO;
		// 主物品
		ArticleEntity mainEntity = aes[0];
		Article mainArticle = getArticle(mainEntity.getArticleName());
		if (mainArticle == null) {
			result = Translate.服务器物品出现错误;
			return result;
		}
		if (!(mainArticle instanceof ComposeInterface)) {
			result = Translate.物品不能合成;
			return result;
		}
		ComposeInterface ia = (ComposeInterface) mainArticle;
		long tempId = ia.getTempComposeEntityId(player, mainEntity, false);

		boolean isnotice = false;
		Article xiazi = ArticleManager.getInstance().getArticle(mainEntity.getArticleName());
		if (xiazi != null && xiazi instanceof InlayArticle) {
			if (((InlayArticle) xiazi).getInlayType() > 1) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.神匣道具为不可合成道具);
				player.addMessageToRightBag(hreq);
				isnotice = true;
			}
		}

		if (tempId == -1 && !isnotice) {
			result = Translate.物品已经不需要合成了;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return result;
		}
		Knapsack knapsack = player.getKnapsack_common();
		if (knapsack == null) {
			result = Translate.服务器物品出现错误;
			return result;
		}
		if (mainArticle.isOverlap()) {
			if (knapsack.getEmptyNum() <= 0) {
				result = Translate.背包空间不足;
				return result;
			}
		}

		// 主物品为装备
		if (mainEntity instanceof EquipmentEntity) {
			if (!(mainArticle instanceof Equipment)) {
				result = Translate.请放入正确物品;
				return result;
			}
			if (aes.length != COMPOSE_NO) {
				result = Translate.合成数量不对;
				return result;
			}

			if (mainEntity != null) {
				ArticleManager am = ArticleManager.getInstance();
				Equipment e = (Equipment) am.getArticle(mainEntity.getArticleName());
				if (isChiBang(e.getEquipmentType())) {
					result = Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "合成" } });
					return result;
				}
			}

			Equipment mainEquipment = (Equipment) mainArticle;
			// 坐骑装备只能合成到完美紫
			// if (mainEquipment.getEquipmentType() >= 10 && mainEntity.getColorType() >= 4) {
			// result = Translate.坐骑装备只能合成到完美紫;
			// return result;
			// }
			for (int i = 0; i < aes.length && i < counts.length; i++) {
				ArticleEntity ae = aes[i];
				if (!(ae instanceof EquipmentEntity)) {
					result = Translate.请放入正确物品;
					return result;
				}
				if (ae.getColorType() != mainEntity.getColorType()) {
					result = Translate.装备合成需要相同颜色的装备;
					return result;
				}
				if (i == 0) {
					if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
						return Translate.高级锁魂物品不能做此操作;
					}
				} else {
					if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, ae.getId())) {
						return Translate.锁魂物品不能做此操作;
					}
				}
				if (ae != null) {
					ArticleManager am = ArticleManager.getInstance();
					Equipment e = (Equipment) am.getArticle(ae.getArticleName());
					if (isChiBang(e.getEquipmentType())) {
						result = Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "被合成" } });
						return result;
					}
				}

				if (((EquipmentEntity) ae).getInlayArticleIds() != null) {
					for (long l : ((EquipmentEntity) ae).getInlayArticleIds()) {
						if (l > 0) {
							result = Translate.有宝石镶嵌的装备不能合成;
							return result;
						}
					}
				}
				if (((EquipmentEntity) ae).getInlayQiLingArticleIds() != null) {
					for (long l : ((EquipmentEntity) ae).getInlayQiLingArticleIds()) {
						if (l > 0) {
							result = Translate.有器灵镶嵌的装备不能合成;
							return result;
						}
					}
				}
				Article e = getArticle(ae.getArticleName());
				if (!(e instanceof Equipment)) {
					result = Translate.请放入正确物品;
					return result;
				}
				int mainType = mainEquipment.getEquipmentType();
				int otherType = ((Equipment) e).getEquipmentType();
				if (mainType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang || otherType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
					return Translate.chiBang不能合成;
				}
				if (mainType == EquipmentColumn.EQUIPMENT_TYPE_fabao || otherType == EquipmentColumn.EQUIPMENT_TYPE_fabao) {
					return Translate.法宝不能合成;
				}
				if (mainType < 10 && otherType >= 10) {
					result = Translate.坐骑装备不能和人物装备合成;
					return result;
				}
				if (mainType >= 10 && otherType < 10) {
					result = Translate.坐骑装备不能和人物装备合成;
					return result;
				}
				if (((Equipment) e).getPlayerLevelLimit() < (mainEquipment.getPlayerLevelLimit() - 30) || ((Equipment) e).getPlayerLevelLimit() > (mainEquipment.getPlayerLevelLimit() + 30)) {
					result = Translate.装备合成需要主装备与副装备级别差小于30;
					return result;
				}
			}
		} else {
			if (mainArticle instanceof InlayArticle) {
				if (aes.length != COMPOSE_NO) {
					result = Translate.合成数量不对;
					return result;
				}
				for (int i = 0; i < aes.length && i < counts.length; i++) {
					ArticleEntity ae = aes[i];
					if (i == 0) {
						if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ae.getId())) {
							return Translate.锁魂物品不能做此操作;
						}
						// if (!ArticleProtectManager.instance.isCanDo4UnBlock(player, ae.getId())) {
						// return Translate.解魂状态不能做此操作;
						// }
					} else {
						if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, ae.getId())) {
							return Translate.锁魂物品不能做此操作;
						}
					}
					if (!ae.getArticleName().equals(mainEntity.getArticleName())) {
						result = Translate.请放入正确物品;
						return result;
					}
				}
			} else {
				for (int i = 0; i < aes.length && i < counts.length; i++) {
					ArticleEntity ae = aes[i];
					if (!ae.getArticleName().equals(mainEntity.getArticleName()) || ae.getColorType() != mainEntity.getColorType()) {
						result = Translate.请放入正确物品;
						return result;
					}
				}
			}
		}
		if (composeType == 0) {
			int cost = 物品合成消耗银子(mainEntity, count);
			if (!player.bindSilverEnough(cost)) {
				result = Translate.金币不足;
				try {
					result = Translate.translateString(Translate.绑银不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(cost) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				BillingCenter.银子不足时弹出充值确认框(player, result);
				return result;
			}
		} else {
			int cost = 物品合成消耗银子(mainEntity, count);
			if (player.getSilver() < cost) {
				result = Translate.元宝不足;
				try {
					result = Translate.translateString(Translate.银子不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(cost) } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				BillingCenter.银子不足时弹出充值确认框(player, result);
				return result;
			}
		}
		return result;
	}

	/**
	 * 合成物品消耗，合成不同类物品消耗不同
	 * 如装备合成，是有一个主装备，其余几个副装备，还有一些其他条件
	 * 宝石合成，同样的宝石5个可以合成更高一级的宝石
	 * 一般合成，同样颜色的5个相同物品可以合成更好颜色的这类物品
	 * @param ae
	 * @return 返回值为结果
	 */
	public String 进行物品合成消耗且合成(Player player, ArticleEntity[] aes, int[] counts, int composeType) {
		String result = null;
		int count = 0;
		boolean binded = false;
		long protectBlock = -1;
		for (int i = 0; i < aes.length && i < counts.length; i++) {
			ArticleEntity ae = aes[i];
			if (i == 0) {
				if (ArticleProtectManager.instance.getBlockState(player, ae.getId()) > 0) {
					protectBlock = ae.getId();
				}
			}
			if (ae == null) {
				result = Translate.请放入正确物品;
				return result;
			}
			if (ae.isBinded()) {
				binded = true;
			}
			Article a = getArticle(ae.getArticleName());
			if (!(a instanceof ComposeInterface)) {
				result = Translate.请放入正确物品;
				return result;
			}
			count += counts[i];
		}
		if (count < COMPOSE_NO) {
			result = Translate.物品个数不足;
			return result;
		}
		int composeArticleCount = count / COMPOSE_NO;

		// 主物品
		ArticleEntity mainEntity = aes[0];
		Article mainArticle = getArticle(mainEntity.getArticleName());
		if (!(mainArticle instanceof ComposeInterface)) {
			result = Translate.请放入正确物品;
			return result;
		}
		if (mainArticle == null) {
			result = Translate.服务器物品出现错误;
			return result;
		}
		Knapsack knapsack = player.getKnapsack_common();
		if (knapsack == null) {
			result = Translate.服务器物品出现错误;
			return result;
		}
		if (mainArticle.isOverlap()) {
			if (knapsack.getEmptyNum() <= 0) {
				result = Translate.背包空间不足;
				return result;
			}
		}

		// 扣费
		BillingCenter bc = BillingCenter.getInstance();
		if (composeType == 0) {
			binded = true;
			int cost = 物品合成消耗银子(mainEntity, composeArticleCount);
			try {
				bc.playerExpense(player, cost, CurrencyType.BIND_YINZI, ExpenseReasonType.EQUIPMENT_COMPOSED, "用绑银合成装备");
			} catch (Exception e) {
				e.printStackTrace();
				result = Translate.绑银不足;
				BillingCenter.银子不足时弹出充值确认框(player, Translate.绑银不足是否去充值);
				if (logger.isWarnEnabled()) logger.warn("[确认物品合成] [失败] [user] [{}] [playerid:{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result });
				return result;
			}
		} else {
			int cost = 物品合成消耗银子(mainEntity, composeArticleCount);
			try {
				bc.playerExpense(player, cost, CurrencyType.YINZI, ExpenseReasonType.EQUIPMENT_COMPOSED, "用银子合成装备");
			} catch (Exception e) {
				e.printStackTrace();
				result = Translate.银子不足;
				BillingCenter.银子不足时弹出充值确认框(player, Translate.银子不足是否去充值);
				if (logger.isWarnEnabled()) logger.warn("[确认物品合成] [失败] [user] [{}] [playerid:{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result });
				return result;
			}
		}

		boolean removeOK = false;
		int leftCount = count % COMPOSE_NO;
		int consumeCount = count - leftCount;
		int removeCount = 0;
		ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
		for (int i = 0; i < aes.length && i < counts.length; i++) {
			ArticleEntity ae = aes[i];
			// 如果是装备合成，那么不删除主装备1
			if (i == 0 && ae instanceof EquipmentEntity) {
				// 只标记减去一个，并不真正删除
				removeCount++;
				continue;
			}
			if (ae != null) {
				for (int j = 0; j < counts[i]; j++) {
					ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "合成删除", true);
					if (aee == null) {
						result = Translate.删除物品不成功;
						// logger.warn("[确认物品合成] [失败] [user] [" + player.getUsername() + "] [playerid:" + player.getId() + "] [" + player.getName() + "] [" + result +
						// "可能是外挂行为，物品id:" + ae.getId() + "]");
						if (logger.isWarnEnabled()) logger.warn("[确认物品合成] [失败] [user] [{}] [playerid:{}] [{}] [{}可能是外挂行为，物品id:{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result, ae.getId() });
						return result;
					} else {
						strongMaterialEntitys.add(aee);
						removeCount++;
						try { // 2014年12月16日9:40:27  物品合成删除时对不可叠加的物品做标记，修改LastUpdateTime为-1，作为可删除标记
							Article tempA = ArticleManager.getInstance().getArticle(aee.getArticleName());
							if (tempA != null && !tempA.isOverlap()) {
								aee.setLastUpdateTime(-1L);
								if (aee instanceof EquipmentEntity && ((EquipmentEntity) aee).getEnchantData() != null) {
									((EquipmentEntity) aee).getEnchantData().setDelete(true);
								}
							}
						} catch (Exception e) {
						}
					}
					if (removeCount >= consumeCount) {
						removeOK = true;
						break;
					}
				}

				// 统计
				ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, counts[i], "合成删除", null);

				if (removeOK) {
					break;
				}
			}
		}
		if (!removeOK) {
			result = Translate.删除物品不成功;
			return result;
		}

		ArticleEntity resultAe = ((ComposeInterface) mainArticle).getComposeEntity(player, mainEntity, binded, composeArticleCount);
		if (resultAe == null) {
			result = Translate.物品已经不需要合成了;
			return result;
		}
		try {
			ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys, (byte) 1);
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[合成物品] [使用赠送--合成物品--异常] [" + player.getLogString() + "]", e);
		}

		if (resultAe.isBinded()) {
			EnterLimitManager.setValues(player, PlayerRecordType.合成绑定次数);
		}
		// 合成物品为装备的话，不再往背包中放，因为之前没有删除
		if (!(resultAe instanceof EquipmentEntity)) {
			if (protectBlock > 0) {
				ArticleProtectData da = ArticleProtectManager.instance.removeBlock(player, protectBlock);
				if (da != null) {
					ArticleProtectManager.instance.blockOne(player, resultAe, da.getState(), ArticleProtectDataValues.ArticleProtect_ArticleType_Gem);
				}
			}

			for (int i = 0; i < composeArticleCount; i++) {
				if (!player.putToKnapsacks(resultAe, "合成")) {
					result = Translate.背包空间不足;
					return result;
				}
			}
		}

		if (resultAe instanceof EquipmentEntity) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.合成装备次数, 1);
			}
			Knapsack.logger.warn("[合成装备只改变装备颜色] [{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { player.getLogString(), resultAe.getArticleName(), resultAe.getId(), resultAe.getColorType(), resultAe.isBinded() });
		}

		if (mainArticle instanceof InlayArticle) {
			if (resultAe.getArticleName() != null) {
				int resultBaoshiLevel = -1;
				if (resultAe.getArticleName().indexOf(Translate.三级) >= 0) {
					resultBaoshiLevel = 3;
					if (AchievementManager.getInstance() != null) {
						AchievementManager.getInstance().record(player, RecordAction.合成宝石等级, 3);
					}
				}
				if (resultAe.getArticleName().indexOf(Translate.四级) >= 0) {
					resultBaoshiLevel = 4;
					if (AchievementManager.getInstance() != null) {
						AchievementManager.getInstance().record(player, RecordAction.合成宝石等级, 4);
					}
				}
				if (resultAe.getArticleName().indexOf(Translate.五级) >= 0) {
					resultBaoshiLevel = 5;
					if (AchievementManager.getInstance() != null) {
						AchievementManager.getInstance().record(player, RecordAction.合成宝石等级, 5);
					}
				}
				if (resultAe.getArticleName().indexOf(Translate.六级) >= 0) {
					resultBaoshiLevel = 6;
					if (AchievementManager.getInstance() != null) {
						AchievementManager.getInstance().record(player, RecordAction.合成宝石等级, 6);
					}
				}
				if (resultAe.getArticleName().indexOf(Translate.七级) >= 0) {
					resultBaoshiLevel = 7;
					if (AchievementManager.getInstance() != null) {
						AchievementManager.getInstance().record(player, RecordAction.合成宝石等级, 7);
					}
				}
				if (resultAe.getArticleName().indexOf(Translate.八级) >= 0) {
					resultBaoshiLevel = 8;
					if (AchievementManager.getInstance() != null) {
						AchievementManager.getInstance().record(player, RecordAction.合成宝石等级, 8);
					}
				}
				if (resultAe.getArticleName().indexOf(Translate.九级) >= 0) {
					resultBaoshiLevel = 9;
					if (AchievementManager.getInstance() != null) {
						AchievementManager.getInstance().record(player, RecordAction.合成宝石等级, 9);
					}
				}
				{
					// 宝石合成的活动.特殊代码，表示当宝石合成成功后，给予玩家额外的奖励
					try {
						ActivityManager.getInstance().noticeBaoshiCompose(player, resultBaoshiLevel, resultAe.getArticleName());
					} catch (Exception e) {
						ActivitySubSystem.logger.error(player.getLogString() + " [合成宝石:" + resultAe.getArticleName() + "] [通知活动] [异常]", e);
					}
				}
			}
		}
		try {
			Article resultArticle = ArticleManager.getInstance().getArticle(resultAe.getArticleName());
			if (resultArticle instanceof Lasting_For_Compose_Props) {
				if (resultAe.getColorType() == 2) {
					EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.合成一瓶蓝酒, 1L });
					EventRouter.getInst().addEvent(evt2);
				}
			}
		} catch (Exception eex) {
			PlayerAimManager.logger.error("[目标系统] [统计合成蓝异常] [" + player.getLogString() + "]", eex);
		}

		// 统计
		ArticleStatManager.addToArticleStat(player, null, resultAe, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, composeArticleCount, "合成获得", null);

		PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.合成成功, (byte) 1, AnimationManager.动画播放位置类型_武器强化中间孔, 0, 0);
		if (pareq != null) {
			player.addMessageToRightBag(pareq);
		}
		return null;
	}

	public static int 得到宝石等级(String articleName) {
		int result = 0;
		if (articleName.indexOf(Translate.一级) >= 0) {
			result = 1;
		}
		if (articleName.indexOf(Translate.二级) >= 0) {
			result = 2;
		}
		if (articleName.indexOf(Translate.三级) >= 0) {
			result = 3;
		}
		if (articleName.indexOf(Translate.四级) >= 0) {
			result = 4;
		}
		if (articleName.indexOf(Translate.五级) >= 0) {
			result = 5;
		}
		if (articleName.indexOf(Translate.六级) >= 0) {
			result = 6;
		}
		if (articleName.indexOf(Translate.七级) >= 0) {
			result = 7;
		}
		if (articleName.indexOf(Translate.八级) >= 0) {
			result = 8;
		}
		if (articleName.indexOf(Translate.九级) >= 0) {
			result = 9;
		}
		return result;
	}

	public int[] koreaEquipmentCompose = new int[] { 100, 400, 900, 1600, 2500, 3600, 4900, 4900, 4900, 4900, 4900 };
	public int[] koreaBaoshiCompose = new int[] { 100, 400, 900, 1600, 2500, 3600, 4900, 4900, 4900, 4900, 4900 };

	/**
	 * 装备合成手续费=装备境界*装备颜色*100文
	 * 物品合成手续费=物品等级对应的境界等级*物品颜色*50文
	 * @param ae
	 * @param counts
	 * @return
	 */
	public int 物品合成消耗银子(ArticleEntity ae, int counts) {
		int cost = 100000;
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(ae.getArticleName());
		if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			if (ae instanceof EquipmentEntity && a instanceof Equipment) {
				cost = (((Equipment) a).getClassLimit() + 1) * (((EquipmentEntity) ae).getColorType() + 1) * koreaEquipmentCompose[((Equipment) a).getClassLimit()];
			} else if (a instanceof InlayArticle) {
				cost = (((InlayArticle) a).getClassLevel() + 1) * koreaBaoshiCompose[((InlayArticle) a).getClassLevel()];
			} else {
				cost = (a.getArticleLevel() / 20 + 1) * (ae.getColorType() + 1) * 50 * 2;
			}

		} else {
			if (ae instanceof EquipmentEntity && a instanceof Equipment) {
				cost = (((Equipment) a).getClassLimit() + 1) * (((EquipmentEntity) ae).getColorType() + 1) * 100;
			} else if (a instanceof InlayArticle) {
				cost = (a.getArticleLevel() / 20 + 1) * 100;
			} else {
				cost = (a.getArticleLevel() / 20 + 1) * (ae.getColorType() + 1) * 50;
			}
		}
		return cost * counts;
	}

	/**
	 * 加载套装属性到人物身上
	 */
	public void loadSuitProperty(Player player, int star, int color, int suitCount, int soulType, int level, int qilingNum, int baoshiNum, int isMingKe, int[] baoshiLevel, int[] tempStar, int qilin4Purpose, int qilin4Oran) {
		Arrays.sort(tempStar);
		if (logger.isDebugEnabled()) {
			logger.debug(player.getLogString() + "[加载套装属性] [星级:" + star + "] [颜色:" + color + "] [套装数量:" + suitCount + "][最低装备等级:" + level + "] [suitCount:" + suitCount + "] [soulType:" + soulType + "] [level:" + level + "] [qilingNum:" + qilingNum + "] [" + isMingKe + "] [baoshiLevel:" + Arrays.toString(baoshiLevel) + "] [baoshiNum:" + baoshiNum + "] [tempStar:" + Arrays.toString(tempStar) + "]" + "[qilin4Purpose:" + qilin4Purpose + "] [qilin4Oran:" + qilin4Oran + "]");
		}
		if (AchievementManager.getInstance() != null) {
			AchievementManager.getInstance().record(player, RecordAction.装备紫色以上器灵件数, qilin4Purpose);
			AchievementManager.getInstance().record(player, RecordAction.装备橙色以上器灵件数, qilin4Oran);
		}
		if (suitCount >= 4 && tempStar[tempStar.length - 4] >= 4) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.成功穿戴随意4件装备到4星, 1);
			}
			if (tempStar[tempStar.length - 4] >= 5) {
				if (AchievementManager.getInstance() != null) {
					AchievementManager.getInstance().record(player, RecordAction.成功穿戴随意4件装备到5星, 1);
				}
			}
			if (suitCount >= 5 && tempStar[tempStar.length - 5] >= 12) {
				AchievementManager.getInstance().record(player, RecordAction.成功穿戴随意5件装备到12星, 1);
			}
		}
		if (isMingKe > 0) {
			try {
				EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.穿戴10件铭刻装备, isMingKe });
				EventRouter.getInst().addEvent(evt2);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计10件装备铭刻异常] [" + player.getLogString() + "]", eex);
			}
		}
		if (suitCount >= 人物套装开启属性最少件数 && star >= 1) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.成功穿戴1套装备到1星以上, 1);
			}
		}
		if (qilingNum > 0) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.镶嵌器灵4个的装备个数, qilingNum);
			}
		}
		if (baoshiNum >= 24) {
			try {
				Arrays.sort(baoshiLevel);
				EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.镶嵌满一身宝石等级, baoshiLevel[0] });
				EventRouter.getInst().addEvent(evt2);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计10件装备铭刻异常] [" + player.getLogString() + "]", eex);
			}
		}
		if (baoshiNum > 0) {
			try {
				EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.身上装备宝石数量, baoshiNum });
				EventRouter.getInst().addEvent(evt2);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计10件装备铭刻异常] [" + player.getLogString() + "]", eex);
			}
		}
		if (suitCount < 人物套装开启属性最少件数) {
			// logger.info("小于套装最小件数"+人物套装开启属性最少件数+"，不予增加套装效果");
			if (logger.isInfoEnabled()) logger.info(player.getLogString() + "小于套装最小件数{}，不予增加套装效果", new Object[] { 人物套装开启属性最少件数 });
			return;
		}
		if (star > 20) {
			star = 20;
		}
		if (star < 6) {
			// logger.info("小于套装最小星级"+6+"，不予增加套装星级效果");
			if (logger.isInfoEnabled()) logger.info(player.getLogString() + "小于套装最小星级{}，不予增加套装星级效果", new Object[] { 6 });
		} else {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.穿戴1套装备到6星以上, 1);
			}
			// if(star >= 8) {
			// if (AchievementManager.getInstance() != null) {
			// AchievementManager.getInstance().record(player, RecordAction.成功穿戴1套装备到7星以上, 1);
			// }
			// }
			// if(star > 17) {
			// if (AchievementManager.getInstance() != null) {
			// AchievementManager.getInstance().record(player, RecordAction.成功穿戴1套装备到17星以上, 1);
			// }
			// }
		}
		if (color < equipment_color_紫) {
			if (logger.isInfoEnabled()) logger.info(player.getLogString() + "小于套装最小颜色紫色，不予增加套装星级效果");
		}
		if (color >= equipment_color_紫) {
			if (level >= 61) {
				if (AchievementManager.getInstance() != null) {
					AchievementManager.getInstance().record(player, RecordAction.穿戴一套61级以上紫装, 1);
				}
				if (soulType == Soul.SOUL_TYPE_SOUL) {
					AchievementManager.getInstance().record(player, RecordAction.元神穿戴一套61级以上紫装, 1);
					if (level >= 121 && color >= equipment_color_橙) {
						AchievementManager.getInstance().record(player, RecordAction.元神穿戴一套121级橙色以上装备, 1);
					}
				}
			}
			if (color >= equipment_color_完美紫) {
				if (level >= 81) {
					if (AchievementManager.getInstance() != null) {
						AchievementManager.getInstance().record(player, RecordAction.穿戴一套81级以上完美紫装, 1);
					}
				}
			}
			if (color >= equipment_color_橙) {
				if (level >= 101) {
					AchievementManager.getInstance().record(player, RecordAction.本尊穿戴一套101以上橙装, 1);
				}
			}
		}
		if (soulType == Soul.SOUL_TYPE_SOUL) {
			if (level >= 41) {
				if (AchievementManager.getInstance() != null) {
					AchievementManager.getInstance().record(player, RecordAction.元神穿戴一套41级以上装备, 1);
				}
			}
		}
		// 成就
		if (color == equipment_color_完美紫) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.获得一套完美紫, 1);
			}
		}
		if (color == equipment_color_橙) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.获得一套橙装, 1);
			}
		}
		if (color >= equipment_color_绿) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.获得一套绿装, 1);
			}
		}
		if (color >= equipment_color_蓝) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.获得一套蓝色以上装, 1);
			}
		}
		if (color == equipment_color_完美橙) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.获得一套完美橙, 1);
			}
			if (soulType == Soul.SOUL_TYPE_BASE) {
				if (level >= 141) {
					AchievementManager.getInstance().record(player, RecordAction.本尊穿戴一套141级以上的完美橙装备, 1);
					if (level >= 181) {
						AchievementManager.getInstance().record(player, RecordAction.本尊穿戴一套181级以上完美橙装备, 1);
						if (level >= 201) {
							AchievementManager.getInstance().record(player, RecordAction.本尊穿戴一套211级以上完美橙装备, 1);
						}
					}
				}
			} else {
				if (AchievementManager.getInstance() != null) {
					if (level >= 201) {
						AchievementManager.getInstance().record(player, RecordAction.元神穿戴一套211级以上完美橙装备, 1);
					}
				}
			}
		}
		if (star >= 8) {
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.获得星级套的星级, star);
			}
		}
		boolean isCurrSoul = player.isCurrSoul(soulType);
		Soul soul = player.getSoul(soulType);
		// double add = isCurrSoul ? 1 : soul.getAddPercent();
		double add = isCurrSoul ? 1 : (soul.getAddPercent() + player.getUpgradeNums() * 0.01);

		if (soul == null) {
			// logger.error("[玩家元神不存在][" + player.getName() + "]owenerId[" + player.getId() + "]元神类型[" + soulType + "]");
			logger.error("[玩家元神不存在][{}]owenerId[{}]元神类型[{}]", new Object[] { player.getName(), player.getId(), soulType });
			throw new IllegalStateException("玩家元神不存在");
		}

		{
			// 加载粒子效果
			String particle = null;
			int realStar = star / 2;
			if (realStar == 3) {
				particle = 角色avatar光芒光效[0];
			} else if (realStar == 4) {
				particle = 角色avatar光芒光效[1];
			} else if (realStar == 5) {
				particle = 角色avatar光芒光效[2];
			} else if (realStar == 6) {
				particle = 角色avatar光芒光效[3];
			} else if (realStar == 7) {
				particle = 角色avatar光芒光效[4];
			} else if (realStar == 8) {
				particle = 角色avatar光芒光效[5];
			} else if (realStar == 9) {
				particle = 角色avatar光芒光效[6];
			} else if (realStar == 10) {
				particle = 角色avatar光芒光效[7];
			}
			// if(particle != null){
			// player.setSuitBodyParticle(particle);
			// }
		}
		SuitEquipment starSe = getSuitEquipmentByStarAndCareer(star, player.getCareer());
		if (player != null && starSe != null) {
			int[] propertyValues = starSe.getPropertyValue();
			int[] openIndexs = starSe.getOpenPropertyIndexs();
			// int[] openSuitCounts = starSe.getOpenPropertySuitCounts();
			int openPropertyCount = 4;
			// if (openSuitCounts != null) {
			// for (int i = 0; i < openSuitCounts.length; i++) {
			// if (suitCount >= openSuitCounts[i]) {
			// openPropertyCount = i + 1;
			// }
			// }
			// }
			if (propertyValues != null && openIndexs != null) {
				for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
					int openIndex = openIndexs[i];
					int propertyValue = 0;
					if (openIndex < propertyValues.length) {
						propertyValue = propertyValues[openIndex];
						// (星级/2)^2/100*职业取值DFRT180级数值*70%*80%
						propertyValue = (int) ((1l * star * star * propertyValue * 70 * 80) / (4 * 100 * 100 * 100));
					}
					switch (openIndex) {
					case SuitEquipment.FIRE_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + propertyValue);
							} else {
								player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() + (int) (propertyValue * add));
							}
							soul.setFireIgnoreDefenceB(soul.getFireIgnoreDefenceB() + propertyValue);
						}
						break;
					case SuitEquipment.ICE_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + propertyValue);
							} else {
								player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() + (int) (propertyValue * add));
							}
							soul.setBlizzardIgnoreDefenceB(soul.getBlizzardIgnoreDefenceB() + propertyValue);
						}
						break;
					case SuitEquipment.WIND_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + propertyValue);
							} else {
								player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() + (int) (propertyValue * add));
							}
							soul.setWindIgnoreDefenceB(soul.getWindIgnoreDefenceB() + propertyValue);
						}
						break;
					case SuitEquipment.THUNDER_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + propertyValue);
							} else {
								player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() + (int) (propertyValue * add));
							}
							soul.setThunderIgnoreDefenceB(soul.getThunderIgnoreDefenceB() + propertyValue);
						}
						break;
					}
				}
			}
		}

		SuitEquipment colorSe = getSuitEquipmentByColorAndCareer(color, player.getCareer());
		if (player != null && colorSe != null) {
			int[] propertyValues = colorSe.getPropertyValue();
			int[] openIndexs = colorSe.getOpenPropertyIndexs();
			// int[] openSuitCounts = colorSe.getOpenPropertySuitCounts();
			int openPropertyCount = 0;
			if (color == equipment_color_紫) {
				openPropertyCount = 1;
			} else if (color == equipment_color_完美紫) {
				openPropertyCount = 2;
			} else if (color == equipment_color_橙) {
				openPropertyCount = 3;
			} else if (color == equipment_color_完美橙) {
				openPropertyCount = 4;
			}
			// if (openSuitCounts != null) {
			// for (int i = 0; i < openSuitCounts.length; i++) {
			// if (suitCount >= openSuitCounts[i]) {
			// openPropertyCount = i + 1;
			// }
			// }
			// }
			if (propertyValues != null && openIndexs != null) {
				for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
					int openIndex = openIndexs[i];
					int propertyValue = 0;
					if (openIndex < propertyValues.length) {
						propertyValue = propertyValues[openIndex];
						// 装备颜色值^2/36*职业取值DFRT180级数值*30%*80%
						propertyValue = (int) ((1l * color * color * propertyValue * 30 * 80) / (36 * 100 * 100));
					}
					switch (openIndex) {
					case SuitEquipment.FIRE_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + propertyValue);
							} else {
								player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() + (int) (propertyValue * add));
							}
							soul.setFireIgnoreDefenceB(soul.getFireIgnoreDefenceB() + propertyValue);
						}
						break;
					case SuitEquipment.ICE_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + propertyValue);
							} else {
								player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() + (int) (propertyValue * add));
							}
							soul.setBlizzardIgnoreDefenceB(soul.getBlizzardIgnoreDefenceB() + propertyValue);
						}
						break;
					case SuitEquipment.WIND_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + propertyValue);
							} else {
								player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() + (int) (propertyValue * add));
							}
							soul.setWindIgnoreDefenceB(soul.getWindIgnoreDefenceB() + propertyValue);
						}
						break;
					case SuitEquipment.THUNDER_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + propertyValue);
							} else {
								player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() + (int) (propertyValue * add));
							}
							soul.setThunderIgnoreDefenceB(soul.getThunderIgnoreDefenceB() + propertyValue);
						}
						break;
					}
				}
			}
		}
	}

	/**
	 * 卸载套装属性
	 * @param player
	 * @param se
	 * @param openSuitCount
	 */
	public void unloadSuitProperty(Player player, int star, int color, int suitCount, int soulType) {
		if (logger.isDebugEnabled()) {
			logger.debug(player.getLogString() + "[卸载套装属性] [星级:" + star + "] [颜色:" + color + "] [套装数量:" + suitCount + "]");
		}
		if (suitCount < 人物套装开启属性最少件数) {
			// logger.info("小于套装最小件数"+人物套装开启属性最少件数+"，不予卸载套装效果");
			if (logger.isInfoEnabled()) logger.info("小于套装最小件数{}，不予卸载套装效果", new Object[] { 人物套装开启属性最少件数 });
			return;
		}
		if (star < 6) {
			// logger.info("小于套装最小星级"+6+"，不予卸载套装星级效果");
			if (logger.isInfoEnabled()) logger.info("小于套装最小星级{}，不予卸载套装星级效果", new Object[] { 6 });
		}
		if (color < equipment_color_紫) {
			if (logger.isInfoEnabled()) logger.info("小于套装最小颜色紫色，不予卸载套装星级效果");
		}
		boolean isCurrSoul = player.isCurrSoul(soulType);
		Soul soul = player.getSoul(soulType);

		// double add = isCurrSoul ? 1 : soul.getAddPercent();
		double add = isCurrSoul ? 1 : (soul.getAddPercent() + player.getUpgradeNums() * 0.01);
		if (soul == null) {
			// logger.error("[玩家元神不存在][" + player.getName() + "]owenerId[" + player.getId() + "]元神类型[" + soulType + "]");
			logger.error("[玩家元神不存在][{}]owenerId[{}]元神类型[{}]", new Object[] { player.getName(), player.getId(), soulType });
			throw new IllegalStateException("玩家元神不存在");
		}
		{
			// 去除强化粒子效果
			player.setSuitBodyParticle("");
		}
		if (star > 20) {
			star = 20;
		}
		SuitEquipment starSe = getSuitEquipmentByStarAndCareer(star, player.getCareer());
		if (player != null && starSe != null) {
			int[] propertyValues = starSe.getPropertyValue();
			int[] openIndexs = starSe.getOpenPropertyIndexs();
			// int[] openSuitCounts = starSe.getOpenPropertySuitCounts();
			int openPropertyCount = 4;
			// if (openSuitCounts != null) {
			// for (int i = 0; i < openSuitCounts.length; i++) {
			// if (suitCount >= openSuitCounts[i]) {
			// openPropertyCount = i + 1;
			// }
			// }
			// }
			if (propertyValues != null && openIndexs != null) {
				for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
					int openIndex = openIndexs[i];
					int propertyValue = 0;
					if (openIndex < propertyValues.length) {
						propertyValue = propertyValues[openIndex];
						// (星级/2)^2/100*职业取值DFRT180级数值*70%*80%
						propertyValue = (int) ((1l * star * star * propertyValue * 70 * 80) / (4 * 100 * 100 * 100));
					}
					switch (openIndex) {
					case SuitEquipment.FIRE_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() - propertyValue);
							} else {
								player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() - (int) (propertyValue * add));
							}
							soul.setFireIgnoreDefenceB(soul.getFireIgnoreDefenceB() - propertyValue);
						}
						break;
					case SuitEquipment.ICE_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() - propertyValue);
							} else {
								player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() - (int) (propertyValue * add));
							}
							soul.setBlizzardIgnoreDefenceB(soul.getBlizzardIgnoreDefenceB() - propertyValue);
						}
						break;
					case SuitEquipment.WIND_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() - propertyValue);
							} else {
								player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() - (int) (propertyValue * add));
							}
							soul.setWindIgnoreDefenceB(soul.getWindIgnoreDefenceB() - propertyValue);
						}
						break;
					case SuitEquipment.THUNDER_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() - propertyValue);
							} else {
								player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() - (int) (propertyValue * add));
							}
							soul.setThunderIgnoreDefenceB(soul.getThunderIgnoreDefenceB() - propertyValue);
						}
						break;
					}
				}
			}
		}
		SuitEquipment colorSe = getSuitEquipmentByColorAndCareer(color, player.getCareer());
		if (player != null && colorSe != null) {
			int[] propertyValues = colorSe.getPropertyValue();
			int[] openIndexs = colorSe.getOpenPropertyIndexs();
			// int[] openSuitCounts = colorSe.getOpenPropertySuitCounts();
			int openPropertyCount = 0;
			if (color == equipment_color_紫) {
				openPropertyCount = 1;
			} else if (color == equipment_color_完美紫) {
				openPropertyCount = 2;
			} else if (color == equipment_color_橙) {
				openPropertyCount = 3;
			} else if (color == equipment_color_完美橙) {
				openPropertyCount = 4;
			}
			// if (openSuitCounts != null) {
			// for (int i = 0; i < openSuitCounts.length; i++) {
			// if (suitCount >= openSuitCounts[i]) {
			// openPropertyCount = i + 1;
			// }
			// }
			// }
			if (propertyValues != null && openIndexs != null) {
				for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
					int openIndex = openIndexs[i];
					int propertyValue = 0;
					if (openIndex < propertyValues.length) {
						propertyValue = propertyValues[openIndex];
						// 装备颜色值^2/36*职业取值DFRT180级数值*30%*80%
						propertyValue = (int) ((1l * color * color * propertyValue * 30 * 80) / (36 * 100 * 100));
					}
					switch (openIndex) {
					case SuitEquipment.FIRE_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() - propertyValue);
							} else {
								player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() - (int) (propertyValue * add));
							}
							soul.setFireIgnoreDefenceB(soul.getFireIgnoreDefenceB() - propertyValue);
						}
						break;
					case SuitEquipment.ICE_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() - propertyValue);
							} else {
								player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() - (int) (propertyValue * add));
							}
							soul.setBlizzardIgnoreDefenceB(soul.getBlizzardIgnoreDefenceB() - propertyValue);
						}
						break;
					case SuitEquipment.WIND_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() - propertyValue);
							} else {
								player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() - (int) (propertyValue * add));
							}
							soul.setWindIgnoreDefenceB(soul.getWindIgnoreDefenceB() - propertyValue);
						}
						break;
					case SuitEquipment.THUNDER_INDEX:
						if (propertyValue != 0) {
							if (isCurrSoul) {
								player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() - propertyValue);
							} else {
								player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() - (int) (propertyValue * add));
							}
							soul.setThunderIgnoreDefenceB(soul.getThunderIgnoreDefenceB() - propertyValue);
						}
						break;
					}
				}
			}
		}
	}

	public SuitEquipment getSuitEquipmentByStarAndCareer(int star, int career) {
		SuitEquipment se = null;
		if (suitEquipmentMap != null) {
			se = suitEquipmentMap.get(career + "&star" + star);
		}
		return se;
	}

	public SuitEquipment getSuitEquipmentByColorAndCareer(int color, int career) {
		SuitEquipment se = null;
		if (suitEquipmentMap != null) {
			se = suitEquipmentMap.get(career + "&color" + color);
		}
		return se;
	}

	/**
	 * 加载套装属性到坐骑身上
	 */
	public void loadSuitPropertyToHorse(Horse horse, int career, int star, int color, int suitCount) {
		if (suitCount < 马匹套装开启属性最少件数) {
			// logger.info("小于马匹套装最小件数"+马匹套装开启属性最少件数+"，不予增加套装效果");
			if (logger.isInfoEnabled()) logger.info("小于马匹套装最小件数{}，不予增加套装效果", new Object[] { 马匹套装开启属性最少件数 });
			return;
		}
		if (star < 3) {
			// logger.info("小于马匹套装最小星级"+3+"，不予增加套装星级效果");
			if (logger.isInfoEnabled()) logger.info("小于马匹套装最小星级{}，不予增加套装星级效果", new Object[] { 3 });
		}
		if (color < equipment_color_紫) {
			// logger.info("小于马匹套装最小星级"+3+"，不予增加套装星级效果");
			if (logger.isInfoEnabled()) logger.info("小于马匹套装最小星级{}，不予增加套装星级效果", new Object[] { 3 });
		}
		if (star > 20) {
			star = 20;
		}
		SuitEquipment starSe = getSuitEquipmentByStarAndCareer(star, career);
		if (horse != null && starSe != null) {
			int[] propertyValues = starSe.getPropertyValue();
			int[] openIndexs = starSe.getOpenPropertyIndexs();
			// int[] openSuitCounts = starSe.getOpenPropertySuitCounts();
			int openPropertyCount = 4;
			// if (openSuitCounts != null) {
			// for (int i = 0; i < openSuitCounts.length; i++) {
			// if (suitCount >= openSuitCounts[i]) {
			// openPropertyCount = i + 1;
			// }
			// }
			// }
			if (propertyValues != null && openIndexs != null) {
				for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
					int openIndex = openIndexs[i];
					int propertyValue = 0;
					if (openIndex < propertyValues.length) {
						propertyValue = propertyValues[openIndex];
						// (星级/2)^2/100*职业取值DFRT180级数值*70%*20%
						propertyValue = (int) ((1l * star * star * propertyValue * 70 * 20) / (4 * 100 * 100 * 100));
					}
					Player p = null;
					try {
						// p = PlayerManager.getInstance().getPlayer(horse.getOwnerId());
						p = horse.owner;
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					boolean bln = p.isIsUpOrDown();
					if (bln && p.getRidingHorseId() == horse.getHorseId()) {
						float index = (float) (horse.getLastEnergyIndex() * 0.1);
						switch (openIndex) {
						case SuitEquipment.FIRE_INDEX:
							if (propertyValue != 0) {
								p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() - (int) (horse.getFireIgnoreDefence() * index));
								horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + propertyValue);
								p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() + (int) (horse.getFireIgnoreDefence() * index));

								// horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + propertyValue);
								// p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.ICE_INDEX:
							if (propertyValue != 0) {
								p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() - (int) (horse.getBlizzardIgnoreDefence() * index));
								horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + propertyValue);
								p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() + (int) (horse.getBlizzardIgnoreDefence() * index));

								// horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + propertyValue);
								// p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.WIND_INDEX:
							if (propertyValue != 0) {
								p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() - (int) (horse.getWindIgnoreDefence() * index));
								horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + propertyValue);
								p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() + (int) (horse.getWindIgnoreDefence() * index));

								// horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + propertyValue);
								// p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.THUNDER_INDEX:
							if (propertyValue != 0) {
								p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() - (int) (horse.getThunderIgnoreDefence() * index));
								horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + propertyValue);
								p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() + (int) (horse.getThunderIgnoreDefence() * index));

								// horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + propertyValue);
								// p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() + propertyValue);
							}
							break;
						}
					} else {
						switch (openIndex) {
						case SuitEquipment.FIRE_INDEX:
							if (propertyValue != 0) {
								horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.ICE_INDEX:
							if (propertyValue != 0) {
								horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.WIND_INDEX:
							if (propertyValue != 0) {
								horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.THUNDER_INDEX:
							if (propertyValue != 0) {
								horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + propertyValue);
							}
							break;
						}
					}

				}
			}
		}

		SuitEquipment colorSe = getSuitEquipmentByColorAndCareer(color, career);
		if (horse != null && colorSe != null) {
			int[] propertyValues = colorSe.getPropertyValue();
			int[] openIndexs = colorSe.getOpenPropertyIndexs();
			// int[] openSuitCounts = colorSe.getOpenPropertySuitCounts();
			int openPropertyCount = 0;
			if (color == equipment_color_紫) {
				openPropertyCount = 1;
			} else if (color == equipment_color_完美紫) {
				openPropertyCount = 2;
			} else if (color == equipment_color_橙) {
				openPropertyCount = 3;
			} else if (color == equipment_color_完美橙) {
				openPropertyCount = 4;
			}
			// if (openSuitCounts != null) {
			// for (int i = 0; i < openSuitCounts.length; i++) {
			// if (suitCount >= openSuitCounts[i]) {
			// openPropertyCount = i + 1;
			// }
			// }
			// }
			if (propertyValues != null && openIndexs != null) {
				for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
					int openIndex = openIndexs[i];
					int propertyValue = 0;
					if (openIndex < propertyValues.length) {
						propertyValue = propertyValues[openIndex];
						// 装备颜色值^2/36*职业取值DFRT180级数值*30%*20%
						propertyValue = (int) ((1l * color * color * propertyValue * 30 * 20) / (36 * 100 * 100));
					}
					Player p = null;
					try {
						// p = PlayerManager.getInstance().getPlayer(horse.getOwnerId());
						p = horse.owner;
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					boolean bln = p.isIsUpOrDown();
					if (bln && p.getRidingHorseId() == horse.getHorseId()) {
						float index = (float) (horse.getLastEnergyIndex() * 0.1);
						switch (openIndex) {

						case SuitEquipment.FIRE_INDEX:
							if (propertyValue != 0) {
								p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() - (int) (horse.getFireIgnoreDefence() * index));
								horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + propertyValue);
								p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() + (int) (horse.getFireIgnoreDefence() * index));

								// horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + propertyValue);
								// p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.ICE_INDEX:
							if (propertyValue != 0) {
								p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() - (int) (horse.getBlizzardIgnoreDefence() * index));
								horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + propertyValue);
								p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() + (int) (horse.getBlizzardIgnoreDefence() * index));

								// horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + propertyValue);
								// p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.WIND_INDEX:
							if (propertyValue != 0) {
								p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() - (int) (horse.getWindIgnoreDefence() * index));
								horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + propertyValue);
								p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() + (int) (horse.getWindIgnoreDefence() * index));

								// horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + propertyValue);
								// p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.THUNDER_INDEX:
							if (propertyValue != 0) {
								p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() - (int) (horse.getThunderIgnoreDefence() * index));
								horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + propertyValue);
								p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() + (int) (horse.getThunderIgnoreDefence() * index));

								// horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + propertyValue);
								// p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() + propertyValue);
							}
							break;
						}
					} else {
						switch (openIndex) {
						case SuitEquipment.FIRE_INDEX:
							if (propertyValue != 0) {
								horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.ICE_INDEX:
							if (propertyValue != 0) {
								horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.WIND_INDEX:
							if (propertyValue != 0) {
								horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + propertyValue);
							}
							break;
						case SuitEquipment.THUNDER_INDEX:
							if (propertyValue != 0) {
								horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + propertyValue);
							}
							break;
						}
					}

				}
			}
		}
	}

	/**
	 * 卸载坐骑套装属性
	 * @param player
	 * @param se
	 * @param openSuitCount
	 */
	public void unloadSuitPropertyFromHorse(Horse horse, int career, int star, int color, int suitCount) {
		if (suitCount < 马匹套装开启属性最少件数) {
			// logger.info("小于马匹套装最小件数"+马匹套装开启属性最少件数+"，不予增加套装效果");
			if (logger.isInfoEnabled()) logger.info("小于马匹套装最小件数{}，不予增加套装效果", new Object[] { 马匹套装开启属性最少件数 });
			return;
		}
		if (star < 3) {
			// logger.info("小于马匹套装最小星级"+3+"，不予增加套装星级效果");
			if (logger.isInfoEnabled()) logger.info("小于马匹套装最小星级{}，不予增加套装星级效果", new Object[] { 3 });
		}
		if (color < equipment_color_紫) {
			// logger.info("小于马匹套装最小星级"+3+"，不予增加套装星级效果");
			if (logger.isInfoEnabled()) logger.info("小于马匹套装最小星级{}，不予增加套装星级效果", new Object[] { 3 });
		}
		if (star > 20) {
			star = 20;
		}
		SuitEquipment starSe = getSuitEquipmentByStarAndCareer(star, career);
		if (horse != null && starSe != null) {
			int[] propertyValues = starSe.getPropertyValue();
			int[] openIndexs = starSe.getOpenPropertyIndexs();
			// int[] openSuitCounts = starSe.getOpenPropertySuitCounts();
			int openPropertyCount = 4;
			// if (openSuitCounts != null) {
			// for (int i = 0; i < openSuitCounts.length; i++) {
			// if (suitCount >= openSuitCounts[i]) {
			// openPropertyCount = i + 1;
			// }
			// }
			// }
			if (propertyValues != null && openIndexs != null) {
				for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
					int openIndex = openIndexs[i];
					int propertyValue = 0;
					if (openIndex < propertyValues.length) {
						propertyValue = propertyValues[openIndex];
						// (星级/2)^2/100*职业取值DFRT180级数值*70%*20%
						propertyValue = (int) ((1l * star * star * propertyValue * 70 * 20) / (4 * 100 * 100 * 100));
					}
					Player p = null;
					try {
						// p = PlayerManager.getInstance().getPlayer(horse.getOwnerId());
						p = horse.owner;
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					boolean bln = p.isIsUpOrDown();
					if (bln) {
						if (p.getRidingHorseId() != horse.getHorseId()) bln = false;
					}

					if (bln) {
						// int index = horse.getLastEnergyIndex();
						float index = (float) (horse.getLastEnergyIndex() * 0.1);
						switch (openIndex) {
						case SuitEquipment.FIRE_INDEX:
							if (propertyValue != 0) {
								p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() - (int) (horse.getFireIgnoreDefence() * index));
								horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() - propertyValue);
								p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() + (int) (horse.getFireIgnoreDefence() * index));

								// horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() - propertyValue);
								// p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.ICE_INDEX:
							if (propertyValue != 0) {
								p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() - (int) (horse.getBlizzardIgnoreDefence() * index));
								horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() - propertyValue);
								p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() + (int) (horse.getBlizzardIgnoreDefence() * index));

								// horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() - propertyValue);
								// p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.WIND_INDEX:
							if (propertyValue != 0) {
								p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() - (int) (horse.getWindIgnoreDefence() * index));
								horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() - propertyValue);
								p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() + (int) (horse.getWindIgnoreDefence() * index));

								// horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() - propertyValue);
								// p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.THUNDER_INDEX:
							if (propertyValue != 0) {
								p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() - (int) (horse.getThunderIgnoreDefence() * index));
								horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() - propertyValue);
								p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() + (int) (horse.getThunderIgnoreDefence() * index));

								// horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() - propertyValue);
								// p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() - propertyValue);
							}
							break;
						}
					} else {
						switch (openIndex) {
						case SuitEquipment.FIRE_INDEX:
							if (propertyValue != 0) {
								horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.ICE_INDEX:
							if (propertyValue != 0) {
								horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.WIND_INDEX:
							if (propertyValue != 0) {
								horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.THUNDER_INDEX:
							if (propertyValue != 0) {
								horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() - propertyValue);
							}
							break;
						}
					}

				}
			}
		}

		SuitEquipment colorSe = getSuitEquipmentByColorAndCareer(color, career);
		if (horse != null && colorSe != null) {
			int[] propertyValues = colorSe.getPropertyValue();
			int[] openIndexs = colorSe.getOpenPropertyIndexs();
			// int[] openSuitCounts = colorSe.getOpenPropertySuitCounts();
			int openPropertyCount = 0;
			if (color == equipment_color_紫) {
				openPropertyCount = 1;
			} else if (color == equipment_color_完美紫) {
				openPropertyCount = 2;
			} else if (color == equipment_color_橙) {
				openPropertyCount = 3;
			} else if (color == equipment_color_完美橙) {
				openPropertyCount = 4;
			}
			// if (openSuitCounts != null) {
			// for (int i = 0; i < openSuitCounts.length; i++) {
			// if (suitCount >= openSuitCounts[i]) {
			// openPropertyCount = i + 1;
			// }
			// }
			// }
			if (propertyValues != null && openIndexs != null) {
				for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
					int openIndex = openIndexs[i];
					int propertyValue = 0;
					if (openIndex < propertyValues.length) {
						propertyValue = propertyValues[openIndex];
						// 装备颜色值^2/36*职业取值DFRT180级数值*30%*20%
						propertyValue = (int) ((1l * color * color * propertyValue * 30 * 20) / (36 * 100 * 100));
					}
					Player p = null;
					try {
						// p = PlayerManager.getInstance().getPlayer(horse.getOwnerId());
						p = horse.owner;
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					boolean bln = p.isIsUpOrDown();
					if (bln && p.getRidingHorseId() == horse.getHorseId()) {
						// int index = horse.getLastEnergyIndex();
						float index = (float) (horse.getLastEnergyIndex() * 0.1);
						switch (openIndex) {
						case SuitEquipment.FIRE_INDEX:
							if (propertyValue != 0) {
								p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() - (int) (horse.getFireIgnoreDefence() * index));
								horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() - propertyValue);
								p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() + (int) (horse.getFireIgnoreDefence() * index));

								// horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() - propertyValue);
								// p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.ICE_INDEX:
							if (propertyValue != 0) {
								p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() - (int) (horse.getBlizzardIgnoreDefence() * index));
								horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() - propertyValue);
								p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() + (int) (horse.getBlizzardIgnoreDefence() * index));

								// horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() - propertyValue);
								// p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.WIND_INDEX:
							if (propertyValue != 0) {
								p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() - (int) (horse.getWindIgnoreDefence() * index));
								horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() - propertyValue);
								p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() + (int) (horse.getWindIgnoreDefence() * index));

								// horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() - propertyValue);
								// p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.THUNDER_INDEX:
							if (propertyValue != 0) {
								p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() - (int) (horse.getThunderIgnoreDefence() * index));
								horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() - propertyValue);
								p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() + (int) (horse.getThunderIgnoreDefence() * index));

								// horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() - propertyValue);
								// p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() - propertyValue);
							}
							break;
						}
					} else {
						switch (openIndex) {
						case SuitEquipment.FIRE_INDEX:
							if (propertyValue != 0) {
								horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.ICE_INDEX:
							if (propertyValue != 0) {
								horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.WIND_INDEX:
							if (propertyValue != 0) {
								horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() - propertyValue);
							}
							break;
						case SuitEquipment.THUNDER_INDEX:
							if (propertyValue != 0) {
								horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() - propertyValue);
							}
							break;
						}
					}

				}
			}
		}
	}

	/**
	 * 坐骑加载基本属性Player player, int[] values, int star, int endowments, boolean inscriptionFlag, int color, int soulType
	 */
	private void horseAddBasicPropertyValue(Horse horse, int[] values, int star, int endowments, boolean inscriptionFlag, int color) {
		if (horse != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					double jiacheng = 1.0;
					if (star >= 0 && star <= starMaxValue) {
						jiacheng += ReadEquipmentExcelManager.strongValues[star];
					}
					if (inscriptionFlag) {
						jiacheng += ReadEquipmentExcelManager.inscriptionValue;
					}
					if (endowments >= 0 && endowments <= maxEndowments) {
						jiacheng += ReadEquipmentExcelManager.endowmentsValues[endowments];
					} else if (endowments >= ArticleManager.newEndowments) {
						jiacheng += ((double) (endowments - ArticleManager.newEndowments) / 100);
					}
					value = (int) (value * jiacheng);
					Player p = null;
					try {
						// p = PlayerManager.getInstance().getPlayer(horse.getOwnerId());
						p = horse.owner;
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					boolean bln = p.isIsUpOrDown() && horse.isInited(); // 2014年12月11日19:04:33  初始化坐骑时不加属性给玩家
					if (bln && p.getRidingHorseId() == horse.getHorseId()) {
						// int index = horse.getLastEnergyIndex();
						float index = (float) (horse.getLastEnergyIndex() * 0.1);
						switch (i) {
						case 0:
							p.setMaxHPB(p.getMaxHPB() - (int) (horse.getMaxHP() * index));
							horse.setMaxHPB(horse.getMaxHPB() + value);
							p.setMaxHPB(p.getMaxHPB() + (int) (horse.getMaxHP() * index));
							// if(HorseManager.logger.isDebugEnabled()) {
							// HorseManager.logger.debug("[坐骑穿装备修改属性] ["+p.getLogString()+"] ["+p.getMaxHPB()+"] ["+horse.getHorseId()+"] ["+horse.getMaxHP()+"]");
							// }
							break;
						case 1:
							p.setPhyAttackB(p.getPhyAttackB() - (int) (horse.getPhyAttack() * index));
							horse.setPhyAttackB(horse.getPhyAttackB() + value);
							p.setPhyAttackB(p.getPhyAttackB() + (int) (horse.getPhyAttack() * index));
							break;
						case 2:
							p.setMagicAttackB(p.getMagicAttackB() - (int) (horse.getMagicAttack() * index));
							horse.setMagicAttackB(horse.getMagicAttackB() + value);
							p.setMagicAttackB(p.getMagicAttackB() + (int) (horse.getMagicAttack() * index));
							break;
						case 3:
							p.setPhyDefenceB(p.getPhyDefenceB() - (int) (horse.getPhyDefence() * index));
							horse.setPhyDefenceB(horse.getPhyDefenceB() + value);
							p.setPhyDefenceB(p.getPhyDefenceB() + (int) (horse.getPhyDefence() * index));
							break;
						case 4:
							p.setMagicDefenceB(p.getMagicDefenceB() - (int) (horse.getMagicDefence() * index));
							horse.setMagicDefenceB(horse.getMagicDefenceB() + value);
							p.setMagicDefenceB(p.getMagicDefenceB() + (int) (horse.getMagicDefence() * index));

							break;
						}
					} else {
						switch (i) {
						case 0:
							horse.setMaxHPB(horse.getMaxHPB() + value);
							break;
						case 1:
							horse.setPhyAttackB(horse.getPhyAttackB() + value);
							break;
						case 2:
							horse.setMagicAttackB(horse.getMagicAttackB() + value);
							break;
						case 3:
							horse.setPhyDefenceB(horse.getPhyDefenceB() + value);
							break;
						case 4:
							horse.setMagicDefenceB(horse.getMagicDefenceB() + value);
							break;
						}
						// }
					}
				}
			}
		}
	}

	/**
	 * 坐骑卸载基本属性
	 */
	private void horseRemoveBasicPropertyValue(Horse horse, int[] values, int star, int endowments, boolean inscriptionFlag, int color) {

		if (horse != null && values != null) {

			if (horse != null && values != null) {

				for (int i = 0; i < values.length; i++) {
					int value = values[i];
					if (value != 0) {
						double jiacheng = 1.0;
						if (star >= 0 && star <= starMaxValue) {
							jiacheng += ReadEquipmentExcelManager.strongValues[star];
						}
						if (inscriptionFlag) {
							jiacheng += ReadEquipmentExcelManager.inscriptionValue;
						}
						if (endowments >= 0 && endowments <= maxEndowments) {
							jiacheng += ReadEquipmentExcelManager.endowmentsValues[endowments];
						} else if (endowments >= ArticleManager.newEndowments) {
							jiacheng += ((double) (endowments - ArticleManager.newEndowments) / 100);
						}
						value = (int) (value * jiacheng);
						Player p = null;
						try {
							// p = PlayerManager.getInstance().getPlayer(horse.getOwnerId());
							p = horse.owner;
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
						boolean bln = p.isIsUpOrDown();
						if (bln && p.getRidingHorseId() == horse.getHorseId()) {
							// int index = horse.getLastEnergyIndex();
							float index = (float) (horse.getLastEnergyIndex() * 0.1);
							switch (i) {
							case 0:
								p.setMaxHPB(p.getMaxHPB() - (int) (horse.getMaxHP() * index));
								horse.setMaxHPB(horse.getMaxHPB() - value);
								p.setMaxHPB(p.getMaxHPB() + (int) (horse.getMaxHP() * index));
								break;
							case 1:
								p.setPhyAttackB(p.getPhyAttackB() - (int) (horse.getPhyAttack() * index));
								horse.setPhyAttackB(horse.getPhyAttackB() - value);
								p.setPhyAttackB(p.getPhyAttackB() + (int) (horse.getPhyAttack() * index));
								break;
							case 2:
								p.setMagicAttackB(p.getMagicAttackB() - (int) (horse.getMagicAttack() * index));
								horse.setMagicAttackB(horse.getMagicAttackB() - value);
								p.setMagicAttackB(p.getMagicAttackB() + (int) (horse.getMagicAttack() * index));
								break;
							case 3:
								p.setPhyDefenceB(p.getPhyDefenceB() - (int) (horse.getPhyDefence() * index));
								horse.setPhyDefenceB(horse.getPhyDefenceB() - value);
								p.setPhyDefenceB(p.getPhyDefenceB() + (int) (horse.getPhyDefence() * index));
								break;
							case 4:
								p.setMagicDefenceB(p.getMagicDefenceB() - (int) (horse.getMagicDefence() * index));
								horse.setMagicDefenceB(horse.getMagicDefenceB() - value);
								p.setMagicDefenceB(p.getMagicDefenceB() + (int) (horse.getMagicDefence() * index));
								break;
							}
						} else {
							switch (i) {
							case 0:
								horse.setMaxHPB(horse.getMaxHPB() - value);
								break;
							case 1:
								horse.setPhyAttackB(horse.getPhyAttackB() - value);
								break;
							case 2:
								horse.setMagicAttackB(horse.getMagicAttackB() - value);
								break;
							case 3:
								horse.setPhyDefenceB(horse.getPhyDefenceB() - value);
								break;
							case 4:
								horse.setMagicDefenceB(horse.getMagicDefenceB() - value);
								break;
							}
							// }

						}
					}
				}
			}
		}
	}

	/**
	 * 坐骑增加附加属性
	 */
	private void horseAddAdditionPropertyValue(Horse horse, int[] values, int star, int endowments, int color) {

		if (horse != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					// value = (int) (value * (1 + ReadEquipmentExcelManager.starQuanZhong[star]));
					value = (int) (value * (1 + ReadEquipmentExcelManager.starQuanZhong[star]));
					// value = value * lastEnergyIndex / 10;
					Player p = null;
					try {
						// p = PlayerManager.getInstance().getPlayer(horse.getOwnerId());
						p = horse.owner;
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					boolean bln = p.isIsUpOrDown() && horse.isInited(); // 2014年12月11日19:04:00  initHorse时候不给人加属性
					if (bln && p.getRidingHorseId() == horse.getHorseId()) {
						// int index = horse.getLastEnergyIndex();
						float index = (float) (horse.getLastEnergyIndex() * 0.1);
						switch (i) {
						case 0:
							p.setMaxMPB(p.getMaxMPB() - (int) (horse.getMaxMP() * index));
							horse.setMaxMPB(horse.getMaxMPB() + value);
							p.setMaxMPB(p.getMaxMPB() + (int) (horse.getMaxMP() * index));
							break;
						case 1:
							p.setBreakDefenceB(p.getBreakDefenceB() - (int) (horse.getBreakDefence() * index));
							horse.setBreakDefenceB(horse.getBreakDefenceB() + value);
							p.setBreakDefenceB(p.getBreakDefenceB() + (int) (horse.getBreakDefence() * index));
							break;
						case 2:
							p.setHitB(p.getHitB() - (int) (horse.getHit() * index));
							horse.setHitB(horse.getHitB() + value);
							p.setHitB(p.getHitB() + (int) (horse.getHit() * index));
							break;
						case 3:
							p.setDodgeB(p.getDodgeB() - (int) (horse.getDodge() * index));
							horse.setDodgeB(horse.getDodgeB() + value);
							p.setDodgeB(p.getDodgeB() + (int) (horse.getDodge() * index));
							break;
						case 4:
							HorseManager.logger.error("[加属性] [现在p:" + p.getAccurateB() + "] [坐骑:" + (int) (horse.getAccurate() * index) + "] [horseB:" + horse.getAccurateB() + "] [value:" + value + "] [INDEX:" + index + "] [TA:" + horse.getAccurate() + "] [HA:" + horse.getAccurateA() + "] [HB:" + horse.getAccurateB() + "] [HC:" + horse.getAccurateC() + "]");
							p.setAccurateB(p.getAccurateB() - (int) (horse.getAccurate() * index));
							horse.setAccurateB(horse.getAccurateB() + value);
							HorseManager.logger.error("[加属性] [现在p:" + p.getAccurateB() + "] [坐骑:" + (int) (horse.getAccurate() * index) + "] [horseB:" + horse.getAccurateB() + "] [value:" + value + "] [INDEX:" + index + "] [TA:" + horse.getAccurate() + "] [HA:" + horse.getAccurateA() + "] [HB:" + horse.getAccurateB() + "] [HC:" + horse.getAccurateC() + "]");
							p.setAccurateB(p.getAccurateB() + (int) (horse.getAccurate() * index));
							break;
						case 5:
							p.setCriticalHitB(p.getCriticalHitB() - (int) (horse.getCriticalHit() * index));
							horse.setCriticalHitB(horse.getCriticalHitB() + value);
							p.setCriticalHitB(p.getCriticalHitB() + (int) (horse.getCriticalHit() * index));
							break;
						case 6:
							p.setCriticalDefenceB(p.getCriticalDefenceB() - (int) (horse.getCriticalDefence() * index));
							horse.setCriticalDefenceB(horse.getCriticalDefenceB() + value);
							p.setCriticalDefenceB(p.getCriticalDefenceB() + (int) (horse.getCriticalDefence() * index));
							break;
						case 7:
							p.setFireAttackB(p.getFireAttackB() - (int) (horse.getFireAttack() * index));
							horse.setFireAttackB(horse.getFireAttackB() + value);
							p.setFireAttackB(p.getFireAttackB() + (int) (horse.getFireAttack() * index));
							break;
						case 8:
							p.setBlizzardAttackB(p.getBlizzardAttackB() - (int) (horse.getBlizzardAttack() * index));
							horse.setBlizzardAttackB(horse.getBlizzardAttackB() + value);
							p.setBlizzardAttackB(p.getBlizzardAttackB() + (int) (horse.getBlizzardAttack() * index));

							break;
						case 9:
							p.setWindAttackB(p.getWindAttackB() - (int) (horse.getWindAttack() * index));
							horse.setWindAttackB(horse.getWindAttackB() + value);
							p.setWindAttackB(p.getWindAttackB() + (int) (horse.getWindAttack() * index));
							break;
						case 10:
							p.setThunderAttackB(p.getThunderAttackB() - (int) (horse.getThunderAttack() * index));
							horse.setThunderAttackB(horse.getThunderAttackB() + value);
							p.setThunderAttackB(p.getThunderAttackB() + (int) (horse.getThunderAttack() * index));
							break;
						case 11:
							p.setFireDefenceB(p.getFireDefenceB() - (int) (horse.getFireDefence() * index));
							horse.setFireDefenceB(horse.getFireDefenceB() + value);
							p.setFireDefenceB(p.getFireDefenceB() + (int) (horse.getFireDefence() * index));
							break;
						case 12:
							p.setBlizzardDefenceB(p.getBlizzardDefenceB() - (int) (horse.getBlizzardDefence() * index));
							horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() + value);
							p.setBlizzardDefenceB(p.getBlizzardDefenceB() + (int) (horse.getBlizzardDefence() * index));

							break;
						case 13:

							p.setWindDefenceB(p.getWindDefenceB() - (int) (horse.getWindDefence() * index));
							horse.setWindDefenceB(horse.getWindDefenceB() + value);
							p.setWindDefenceB(p.getWindDefenceB() + (int) (horse.getWindDefence() * index));
							break;
						case 14:
							p.setThunderDefenceB(p.getThunderDefenceB() - (int) (horse.getThunderDefence() * index));
							horse.setThunderDefenceB(horse.getThunderDefenceB() + value);
							p.setThunderDefenceB(p.getThunderDefenceB() + (int) (horse.getThunderDefence() * index));
							break;
						}
					} else {
						switch (i) {
						case 0:
							horse.setMaxMPB(horse.getMaxMPB() + value);
							break;
						case 1:
							horse.setBreakDefenceB(horse.getBreakDefenceB() + value);
							break;
						case 2:
							horse.setHitB(horse.getHitB() + value);
							break;
						case 3:
							horse.setDodgeB(horse.getDodgeB() + value);
							break;
						case 4:
							horse.setAccurateB(horse.getAccurateB() + value);
							break;
						case 5:
							horse.setCriticalHitB(horse.getCriticalHitB() + value);
							break;
						case 6:
							horse.setCriticalDefenceB(horse.getCriticalDefenceB() + value);
							break;
						case 7:
							horse.setFireAttackB(horse.getFireAttackB() + value);
							break;
						case 8:
							horse.setBlizzardAttackB(horse.getBlizzardAttackB() + value);
							break;
						case 9:
							horse.setWindAttackB(horse.getWindAttackB() + value);
							break;
						case 10:
							horse.setThunderAttackB(horse.getThunderAttackB() + value);
							break;
						case 11:
							horse.setFireDefenceB(horse.getFireDefenceB() + value);
							break;
						case 12:
							horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() + value);
							break;
						case 13:
							horse.setWindDefenceB(horse.getWindDefenceB() + value);
							break;
						case 14:
							horse.setThunderDefenceB(horse.getThunderDefenceB() + value);
							break;
						}
						// }
					}
				}
			}
		}
	}

	/**
	 * 坐骑减少附加属性
	 */
	private void horseRemoveAdditionPropertyValue(Horse horse, int[] values, int star, int endowments, int color) {
		if (horse != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					value = (int) (value * (1 + ReadEquipmentExcelManager.starQuanZhong[star]));
					// value = value * lastEnergyIndex / 10;
					Player p = null;
					try {
						// p = PlayerManager.getInstance().getPlayer(horse.getOwnerId());
						p = horse.owner;
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					boolean bln = p.isIsUpOrDown();
					if (bln && p.getRidingHorseId() == horse.getHorseId()) {
						// int index = horse.getLastEnergyIndex();
						float index = (float) (horse.getLastEnergyIndex() * 0.1);
						switch (i) {
						case 0:
							p.setMaxMPB(p.getMaxMPB() - (int) (horse.getMaxMP() * index));
							horse.setMaxMPB(horse.getMaxMPB() - value);
							p.setMaxMPB(p.getMaxMPB() + (int) (horse.getMaxMP() * index));
							break;
						case 1:
							p.setBreakDefenceB(p.getBreakDefenceB() - (int) (horse.getBreakDefence() * index));
							horse.setBreakDefenceB(horse.getBreakDefenceB() - value);
							p.setBreakDefenceB(p.getBreakDefenceB() + (int) (horse.getBreakDefence() * index));
							break;
						case 2:
							p.setHitB(p.getHitB() - (int) (horse.getHit() * index));
							horse.setHitB(horse.getHitB() - value);
							p.setHitB(p.getHitB() + (int) (horse.getHit() * index));
							break;
						case 3:
							p.setDodgeB(p.getDodgeB() - (int) (horse.getDodge() * index));
							horse.setDodgeB(horse.getDodgeB() - value);
							p.setDodgeB(p.getDodgeB() + (int) (horse.getDodge() * index));
							break;
						case 4:
							HorseManager.logger.error("[减属性] [现在p:" + p.getAccurateB() + "] [坐骑:" + (int) (horse.getAccurate() * index) + "] [horseB:" + horse.getAccurateB() + "] [value:" + value + "] [INDEX:" + index + "] [TA:" + horse.getAccurate() + "] [HA:" + horse.getAccurateA() + "] [HB:" + horse.getAccurateB() + "] [HC:" + horse.getAccurateC() + "]");
							p.setAccurateB(p.getAccurateB() - (int) (horse.getAccurate() * index));
							horse.setAccurateB(horse.getAccurateB() - value);
							HorseManager.logger.error("[减属性] [现在p:" + p.getAccurateB() + "] [坐骑:" + (int) (horse.getAccurate() * index) + "] [horseB:" + horse.getAccurateB() + "] [value:" + value + "] [INDEX:" + index + "] [TA:" + horse.getAccurate() + "] [HA:" + horse.getAccurateA() + "] [HB:" + horse.getAccurateB() + "] [HC:" + horse.getAccurateC() + "]");
							p.setAccurateB(p.getAccurateB() + (int) (horse.getAccurate() * index));
							break;
						case 5:
							p.setCriticalHitB(p.getCriticalHitB() - (int) (horse.getCriticalHit() * index));
							horse.setCriticalHitB(horse.getCriticalHitB() - value);
							p.setCriticalHitB(p.getCriticalHitB() + (int) (horse.getCriticalHit() * index));
							break;
						case 6:
							p.setCriticalDefenceB(p.getCriticalDefenceB() - (int) (horse.getCriticalDefence() * index));
							horse.setCriticalDefenceB(horse.getCriticalDefenceB() - value);
							p.setCriticalDefenceB(p.getCriticalDefenceB() + (int) (horse.getCriticalDefence() * index));
							break;
						case 7:
							p.setFireAttackB(p.getFireAttackB() - (int) (horse.getFireAttack() * index));
							horse.setFireAttackB(horse.getFireAttackB() - value);
							p.setFireAttackB(p.getFireAttackB() + (int) (horse.getFireAttack() * index));
							break;
						case 8:
							p.setBlizzardAttackB(p.getBlizzardAttackB() - (int) (horse.getBlizzardAttack() * index));
							horse.setBlizzardAttackB(horse.getBlizzardAttackB() - value);
							p.setBlizzardAttackB(p.getBlizzardAttackB() + (int) (horse.getBlizzardAttack() * index));
							break;
						case 9:
							p.setWindAttackB(p.getWindAttackB() - (int) (horse.getWindAttack() * index));
							horse.setWindAttackB(horse.getWindAttackB() - value);
							p.setWindAttackB(p.getWindAttackB() + (int) (horse.getWindAttack() * index));
							break;
						case 10:
							p.setThunderAttackB(p.getThunderAttackB() - (int) (horse.getThunderAttack() * index));
							horse.setThunderAttackB(horse.getThunderAttackB() - value);
							p.setThunderAttackB(p.getThunderAttackB() + (int) (horse.getThunderAttack() * index));
							break;
						case 11:
							p.setFireDefenceB(p.getFireDefenceB() - (int) (horse.getFireDefence() * index));
							horse.setFireDefenceB(horse.getFireDefenceB() - value);
							p.setFireDefenceB(p.getFireDefenceB() + (int) (horse.getFireDefence() * index));
							break;
						case 12:
							p.setBlizzardDefenceB(p.getBlizzardDefenceB() - (int) (horse.getBlizzardDefence() * index));
							horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() - value);
							p.setBlizzardDefenceB(p.getBlizzardDefenceB() + (int) (horse.getBlizzardDefence() * index));
							break;
						case 13:
							p.setWindDefenceB(p.getWindDefenceB() - (int) (horse.getWindDefence() * index));
							horse.setWindDefenceB(horse.getWindDefenceB() - value);
							p.setWindDefenceB(p.getWindDefenceB() + (int) (horse.getWindDefence() * index));
							break;
						case 14:
							p.setThunderDefenceB(p.getThunderDefenceB() - (int) (horse.getThunderDefence() * index));
							horse.setThunderDefenceB(horse.getThunderDefenceB() - value);
							p.setThunderDefenceB(p.getThunderDefenceB() + (int) (horse.getThunderDefence() * index));
							break;
						}
					} else {
						switch (i) {
						case 0:
							horse.setMaxMPB(horse.getMaxMPB() - value);
							break;
						case 1:
							horse.setBreakDefenceB(horse.getBreakDefenceB() - value);
							break;
						case 2:
							horse.setHitB(horse.getHitB() - value);
							break;
						case 3:
							horse.setDodgeB(horse.getDodgeB() - value);
							break;
						case 4:
							horse.setAccurateB(horse.getAccurateB() - value);
							break;
						case 5:
							horse.setCriticalHitB(horse.getCriticalHitB() - value);
							break;
						case 6:
							horse.setCriticalDefenceB(horse.getCriticalDefenceB() - value);
							break;
						case 7:
							horse.setFireAttackB(horse.getFireAttackB() - value);
							break;
						case 8:
							horse.setBlizzardAttackB(horse.getBlizzardAttackB() - value);
							break;
						case 9:
							horse.setWindAttackB(horse.getWindAttackB() - value);
							break;
						case 10:
							horse.setThunderAttackB(horse.getThunderAttackB() - value);
							break;
						case 11:
							horse.setFireDefenceB(horse.getFireDefenceB() - value);
							break;
						case 12:
							horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() - value);
							break;
						case 13:
							horse.setWindDefenceB(horse.getWindDefenceB() - value);
							break;
						case 14:
							horse.setThunderDefenceB(horse.getThunderDefenceB() - value);
							break;
						}
						// }

					}
				}
			}
		}
	}

	// /**
	// * 坐骑将装备上镶嵌的宝石属性加载到坐骑身上
	// */
	// private void horseAddInlayArticlePropertyValue(Horse horse, long[] inlayArticleIds) {
	//
	// if (inlayArticleIds != null && horse != null) {
	// ArticleEntityManager aem = ArticleEntityManager.getInstance();
	// ArticleManager am = ArticleManager.getInstance();
	// if (aem != null && am != null) {
	// for (long inlayId : inlayArticleIds) {
	// if (inlayId != -1) {
	// ArticleEntity ae = aem.getEntity(inlayId);
	// if (ae != null) {
	// Article a = am.getArticle(ae.getArticleName());
	// if (a instanceof InlayArticle) {
	// ((InlayArticle) a).addPropertyValueToHorse(horse);
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// /**
	// * 坐骑将装备上镶嵌的宝石属性从坐骑上卸载
	// */
	// private void horseRemoveInlayArticlePropertyValue(Horse horse, long[] inlayArticleIds) {
	//
	// if (inlayArticleIds != null && horse != null) {
	// ArticleEntityManager aem = ArticleEntityManager.getInstance();
	// ArticleManager am = ArticleManager.getInstance();
	// if (aem != null && am != null) {
	// for (long inlayId : inlayArticleIds) {
	// if (inlayId != -1) {
	// ArticleEntity ae = aem.getEntity(inlayId);
	// if (ae != null) {
	// Article a = am.getArticle(ae.getArticleName());
	// if (a instanceof InlayArticle) {
	// ((InlayArticle) a).removePropertyValueFromHorse(horse);
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// }

	ReadEquipmentExcelManager reem;

	public static String lastProcSheetName;
	public static HSSFRow lastProcRow;

	/**
	 * 初始化物品
	 */
	public void initFile(File file) throws Exception {
		if (file != null && file.isFile() && file.exists()) {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);
			ReadEquipmentExcelManager reem = ReadEquipmentExcelManager.getInstance();
			HashMap<String, SuitEquipment> suitEquipmentMap = new HashMap<String, SuitEquipment>();
			// HashMap<String,Article> articles = new HashMap<String,Article>();
			if (物品文件最大所在sheet >= workbook.getNumberOfSheets()) {
				throw new IllegalStateException("表页签个数为" + workbook.getNumberOfSheets() + ", 程序需要:" + 物品文件最大所在sheet);
			}
			for (int i = 0; i <= 物品文件最大所在sheet; i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				lastProcSheetName = sheet.getSheetName();
				int rows = sheet.getPhysicalNumberOfRows();
				// i==0表示读取的装备，1表示读取套装
				if (i == 物品文件装备所在sheet || i == 特殊装备1所在sheet || i == 特殊装备2所在sheet) {
					// 测试读取装备文件
					ArrayList<Equipment> allEquipmentArticle = new ArrayList<Equipment>();
					ArrayList<Equipment> allSpecialEquipmentArticle1 = new ArrayList<Equipment>();
					ArrayList<Equipment> allSpecialEquipmentArticle2 = new ArrayList<Equipment>();

					ArrayList<Article> allArticle = new ArrayList<Article>();

					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							HSSFCell cell = row.getCell(装备名);
							String name = "";
							try {
								name = (cell.getStringCellValue().trim().trim());
							} catch (Exception ex) {
								throw ex;
							}

							cell = row.getCell(装备名_STAT);
							String name_stat = "";
							try {
								name_stat = (cell.getStringCellValue().trim().trim());
							} catch (Exception ex) {
								throw ex;
							}

							cell = row.getCell(装备类型);//
							int 装备类型 = 0;
							try {
								装备类型 = (int) cell.getNumericCellValue();
							} catch (Exception ex) {
								try {
									装备类型 = Integer.parseInt(cell.getStringCellValue().trim());
								} catch (Exception e) {
									throw e;
								}
							}

							cell = row.getCell(武器类型);//
							int 武器类型 = 0;
							try {
								武器类型 = (int) cell.getNumericCellValue();
							} catch (Exception ex) {
								throw ex;
							}

							cell = row.getCell(物品使用级别);
							int 物品使用级别 = 0;
							try {
								物品使用级别 = (int) cell.getNumericCellValue();
							} catch (Exception ex) {
								throw ex;
							}

							cell = row.getCell(物品数值级别);//
							int 物品数值级别 = 0;
							try {
								物品数值级别 = (int) cell.getNumericCellValue();
							} catch (Exception ex) {
								throw ex;
							}

							cell = row.getCell(门派限制);
							int 门派限制 = 0;
							try {
								门派限制 = (int) cell.getNumericCellValue();
							} catch (Exception ex) {
								try {
									门派限制 = Integer.parseInt(cell.getStringCellValue().trim());
								} catch (Exception e) {

								}
								// throw ex;
							}

							cell = row.getCell(境界限制);
							int 境界限制 = 0;
							try {
								境界限制 = (int) cell.getNumericCellValue();
							} catch (Exception ex) {
								try {
									境界限制 = Integer.parseInt(cell.getStringCellValue().trim());
								} catch (Exception e) {

								}
							}

							cell = row.getCell(耐久);
							int 耐久 = 1;
							try {
								耐久 = (int) cell.getNumericCellValue();
							} catch (Exception ex) {
								// throw ex;
							}

							cell = row.getCell(是否可卖);
							boolean 是否可卖 = false;
							try {
								是否可卖 = cell.getBooleanCellValue();
							} catch (Exception ex) {
								// throw ex;
							}

							cell = row.getCell(卖价);
							int 卖价 = 0;
							try {
								卖价 = (int) cell.getNumericCellValue();
							} catch (Exception ex) {

								try {
									卖价 = Integer.parseInt(cell.getStringCellValue().trim());
								} catch (Exception e) {

								}
							}

							cell = row.getCell(掉落形象);
							String 掉落NPC形象 = null;
							try {
								掉落NPC形象 = cell.getStringCellValue().trim();
							} catch (Exception ex) {
								// throw ex;
							}

							cell = row.getCell(图标);
							String 图标 = "";
							try {
								图标 = getCellContant(cell);
							} catch (Exception ex) {
								throw ex;
							}

							cell = row.getCell(精炼后的装备);
							String 精炼后的装备 = null;
							try {
								精炼后的装备 = cell.getStringCellValue().trim();
							} catch (Exception ex) {

							}

							cell = row.getCell(精炼后的装备_STAT);
							String 精炼后的装备_STAT = null;
							try {
								精炼后的装备_STAT = cell.getStringCellValue().trim();
							} catch (Exception ex) {

							}

							cell = row.getCell(装备avata);
							String avata = null;
							try {
								avata = cell.getStringCellValue().trim();
							} catch (Exception ex) {

							}

							cell = row.getCell(装备强化粒子光效);
							String[] strongParticles = new String[starMaxValue + 1];
							try {
								String[] strongParticless = cell.getStringCellValue().trim().split(";");
								if (strongParticless.length == 1) {
									strongParticless = cell.getStringCellValue().trim().split("；");
								}
								for (int j = 0; j < strongParticless.length; j++) {
									if (strongParticless[j] != null) {
										strongParticles[j] = strongParticless[j];
									}
								}

							} catch (Exception ex) {

							}

							Equipment e = null;
							if (装备类型 == 0) {
								e = new Weapon();
								((Weapon) e).setWeaponType((byte) 武器类型);
							} else {
								e = new Equipment();
							}
							if (name.trim().equals("")) {
								throw new Exception("设置错误存在空名字的物品");
							}
							e.setName(name);
							e.setName_stat(name_stat);
							e.setEquipmentType(装备类型);
							e.setCareerLimit(门派限制);
							e.setClassLimit(境界限制);
							e.setDurability(耐久);
							e.setSailFlag(是否可卖);
							e.setPrice(卖价);
							e.setPlayerLevelLimit(物品使用级别);
							e.setArticleLevel(物品数值级别);
							if (掉落NPC形象 != null) {
								e.setFlopNPCAvata(掉落NPC形象);
							}
							e.setIconId(图标);
							// e.setMaxLevel(maxLevel);
							e.setDevelopEquipmentName(精炼后的装备);
							e.setDevelopEquipmentName_stat(精炼后的装备_STAT);
							if (strongParticles != null) {
								String particleTemp = "";
								for (int j = 0; j < strongParticles.length; j++) {
									if (strongParticles[j] != null) {
										particleTemp = strongParticles[j];
									} else {
										strongParticles[j] = particleTemp;
									}
								}
								e.setStrongParticles(strongParticles);
							}
							if (avata != null) {
								String[] as = new String[ArticleManager.starMaxValue + 1];
								for (int j = 0; j < as.length; j++) {
									as[j] = avata;
								}
								e.setAvata(as);
								byte[] ats = new byte[ArticleManager.starMaxValue + 1];
								int equipmentType = e.getEquipmentType();
								byte fillV = 0;
								if (equipmentType == 0) {
									fillV = Constants.TYPE_WEAPON;
								} else if (equipmentType == 1) {
									fillV = Constants.TYPE_HEAD;
								} else if (equipmentType == 2) {
									fillV = Constants.TYPE_SHOULDER;
								} else if (equipmentType == 3) {
									fillV = Constants.TYPE_CLOTH;
								} else if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {// 翅膀
									fillV = Constants.TYPE_SHOULDER;
								}
								Arrays.fill(ats, fillV);
								e.setAvataType(ats);
							}

							cell = row.getCell(装备故事);
							try {
								String story = (cell.getStringCellValue().trim());
								e.setStroy(story);
							} catch (Exception ex) {

							}

							cell = row.getCell(装备技能);
							try {
								int id = (int) cell.getNumericCellValue();
								e.setSkillId(id);
							} catch (Exception ex) {

							}

							cell = row.getCell(装备有无有效期);
							try {
								boolean isHaveValidDays = cell.getBooleanCellValue();
								e.setHaveValidDays(isHaveValidDays);
							} catch (Exception ex) {

							}

							cell = row.getCell(装备有效期激活方式);
							try {
								byte activationOption = (byte) cell.getNumericCellValue();
								e.setActivationOption(activationOption);
							} catch (Exception ex) {

							}

							cell = row.getCell(装备最大有效期);
							try {
								int maxValidDays = (int) cell.getNumericCellValue();
								e.setMaxValidDays(maxValidDays);
							} catch (Exception ex) {

							}
							
							cell = row.getCell(装备最大有效期 + 1);
							try {
								int getOfLevelPackage = (int) cell.getNumericCellValue();
								e.setGetOfLevelPackage(getOfLevelPackage);
							} catch (Exception ex) {

							}

							if (i == 物品文件装备所在sheet) {
								e.setSpecial((byte) 0);
								allEquipmentArticle.add(e);
							} else if (i == 特殊装备1所在sheet) {
								e.setSpecial((byte) 1);
								allSpecialEquipmentArticle1.add(e);
							} else if (i == 特殊装备2所在sheet) {
								e.setSpecial((byte) 2);
								allSpecialEquipmentArticle2.add(e);
							}
							// System.out.println(e.getName() + "->" + e.getDevelopEquipmentName());
							allArticle.add(e);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allEquipments = allEquipmentArticle.toArray(new Equipment[0]);
					if (i == 特殊装备1所在sheet) {
						this.allSpecialEquipments1 = allSpecialEquipmentArticle1.toArray(new Equipment[0]);
					} else if (i == 特殊装备2所在sheet) {
						this.allSpecialEquipments2 = allSpecialEquipmentArticle2.toArray(new Equipment[0]);
					}
					// this.articles = articles;
				} else if (i == 物品文件套装所在sheet) {
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							int cellNumber = row.getPhysicalNumberOfCells();

							HSSFCell cell = row.getCell((short) 0);
							String name = "";
							try {
								name = cell.getStringCellValue().trim();
							} catch (Exception ex) {
								try {
									name = (int) cell.getNumericCellValue() + "";
								} catch (Exception e) {
									throw e;
								}
							}

							cell = row.getCell((short) 1);
							int level = 0;
							try {
								level = (int) cell.getNumericCellValue();
							} catch (Exception ex) {
								throw ex;
							}

							cell = row.getCell((short) 2);
							int career = 0;
							try {
								career = (int) cell.getNumericCellValue();
							} catch (Exception ex) {
								throw ex;
							}

							// cell = row.getCell((short) 3);
							// String jianshu = "";
							// try {
							// jianshu = cell.getStringCellValue().trim();
							// } catch (Exception ex) {
							// throw ex;
							// }
							//
							// String[] ss = jianshu.split(",");
							// int[] openPropertySuitCounts = new int[ss.length];
							// for (int ii = 0; ii < ss.length; ii++) {
							// openPropertySuitCounts[ii] = Integer.parseInt(ss[ii].trim());
							// }
							SuitEquipment se = new SuitEquipment();
							se.setSuitEquipmentName(career + "&" + name);
							se.setSuitLevel(level);
							// se.setOpenPropertySuitCounts(openPropertySuitCounts);
							if (reem == null || reem.careerExcelDatas == null) {
								throw new Exception("[ReadEquipmentExcelManager] 配置异常1[" + reem + "][]");
							}
							if (reem.careerExcelDatas.length <= career + 1) {
								throw new Exception("[ReadEquipmentExcelManager] 配置异常[" + reem.careerExcelDatas.length + "][" + career + 1 + "]");
							}
							if (reem.careerExcelDatas[career + 1].length <= level) {
								throw new Exception("[ReadEquipmentExcelManager] 配置异常2[" + reem.careerExcelDatas[career + 1].length + "][强化等级:" + level + "]");
							}
							int[] a = reem.careerExcelDatas[career][level];
							int[] p = new int[4];
							p[0] = a[20];
							p[1] = a[21];
							p[2] = a[22];
							p[3] = a[23];
							se.setPropertyValue(p);
							se.setOpenPropertyIndexs(suitPropertyIndexByCareer[career]);
							suitEquipmentMap.put(se.getSuitEquipmentName(), se);
						}
					}
					// "通用职业装备取值","蜀山派装备取值","炼狱宗装备取值","昆仑山装备取值","灵隐阁装备取值","万毒谷装备取值"
					this.suitEquipmentMap = suitEquipmentMap;
				} else if (i == 物品文件宝石所在sheet) {

					ArrayList<InlayArticle> allInlayArticle = new ArrayList<InlayArticle>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							InlayArticle e = new InlayArticle();

							设置物品一般属性(e, row);

							HSSFCell cell = row.getCell(所有物品_物品名_列);
							String name = "";
							try {
								name = (cell.getStringCellValue().trim());
							} catch (Exception ex) {
								throw ex;
							}

							cell = row.getCell(宝石_宝石镶嵌颜色_列);
							int 宝石镶嵌颜色 = 0;
							try {
								宝石镶嵌颜色 = (int) cell.getNumericCellValue();
							} catch (Exception ex) {
								throw ex;
							}

							cell = row.getCell(宝石_合成后的宝石_列);
							String 合成后的宝石 = null;
							try {
								合成后的宝石 = (cell.getStringCellValue().trim());
							} catch (Exception ex) {

							}

							cell = row.getCell(宝石_合成后的宝石_列_STAT);
							String 合成后的宝石_stat = null;
							try {
								合成后的宝石_stat = (cell.getStringCellValue().trim());
							} catch (Exception ex) {

							}

							cell = row.getCell(宝石_境界限制);
							try {
								int 境界限制 = (int) cell.getNumericCellValue();
								e.setClassLevel(境界限制);
							} catch (Exception ex) {

							}

							cell = row.getCell(宝石_类型);
							try {
								int 宝石类型 = (int) cell.getNumericCellValue();
								e.setInlayType(宝石类型);
							} catch (Exception ex) {

							}

							cell = row.getCell(宝石_类型 + 1);
							try {
								String 显示名字 = (cell.getStringCellValue().trim());
								e.setShowName(显示名字);
							} catch (Exception ex) {

							}
							
							e.setInlayColorType(宝石镶嵌颜色);
							e.setComparedArticleName(合成后的宝石);
							e.setComparedArticleName_stat(合成后的宝石_stat);
							int[] propertysValues = new int[reem.inlayArticleExcelDatas[e.getArticleLevel()].length];
							int[] propertyTemp = reem.inlayArticleExcelDatas[e.getArticleLevel()];
							int[] indexs = 得到宝石名称对应的宝石属性位置(e.getIconId());
							for (int j = 0; j < indexs.length; j++) {
								int index = indexs[j];
								for (int k = 0; k < propertyTemp.length; k++) {
									if (k == index) {
										propertysValues[k] = propertyTemp[k];
										break;
									}
								}
							}
							// logger.warn("宝石属性 " + "["+e.getName()+"] ["+e.getArticleLevel()+"]" +Arrays.toString(propertyTemp) + " " + Arrays.toString(propertysValues));
							e.setPropertysValues(propertysValues);

							allInlayArticle.add(e);
							allArticle.add(e);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allInlayArticles = allInlayArticle.toArray(new InlayArticle[0]);
					// this.articles = articles;
				} else if (i == 物品文件一般物品所在sheet) {

					ArrayList<Article> allCommonArticle = new ArrayList<Article>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							Article a = new Article();
							try {
								设置物品一般属性(a, row);
							} catch (Exception e) {
								// logger.info("articleinit" + "物品文件一般物品所在sheet");
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "物品文件一般物品所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allCommonArticle.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allOtherArticles = allCommonArticle.toArray(new Article[0]);
					// this.articles = articles;
				} else if (i == 物品文件可合成仅仅改变颜色物品所在sheet) {

					ArrayList<ComposeOnlyChangeColorArticle> allComposeOnlyChangeColorArticle = new ArrayList<ComposeOnlyChangeColorArticle>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							ComposeOnlyChangeColorArticle a = new ComposeOnlyChangeColorArticle();
							try {
								设置物品一般属性(a, row);
							} catch (Exception e) {
								// logger.error("articleinit" + "物品文件可合成仅仅改变颜色物品所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件可合成仅仅改变颜色物品所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allComposeOnlyChangeColorArticle.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allComposeOnlyChangeColorArticles = allComposeOnlyChangeColorArticle.toArray(new ComposeOnlyChangeColorArticle[0]);
					// this.articles = articles;
				} else if (i == 物品文件给玩家用的瞬间恢复药品所在sheet) {
					ArrayList<SingleProps> allSingleProps = new ArrayList<SingleProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							SingleProps a = new SingleProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
							} catch (Exception e) {
								// logger.error("articleinit" + "物品文件给玩家用的瞬间恢复药品所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件给玩家用的瞬间恢复药品所在sheet" });
								e.printStackTrace();
								throw e;
							}
							int[] values = new int[简单道具能修改的属性数];
							for (int j = 0; j < 简单道具能修改的属性数; j++) {
								HSSFCell cell = row.getCell((j + 简单道具_血_列));
								int value = 0;
								try {
									value = (int) cell.getNumericCellValue();
								} catch (Exception ex) {
									try {
										value = Integer.parseInt(cell.getStringCellValue().trim());
									} catch (Exception e) {

									}
								}
								values[j] = value;
							}
							a.setValues(values);
							allSingleProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allSingleProps = allSingleProps.toArray(new SingleProps[0]);
				} else if (i == 物品文件给宠物用的瞬间恢复药品所在sheet) {
					ArrayList<SingleForPetProps> allSingleForPetProps = new ArrayList<SingleForPetProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							SingleForPetProps a = new SingleForPetProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
							} catch (Exception e) {
								// logger.error("articleinit" + "物品文件给宠物用的瞬间恢复药品所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件给宠物用的瞬间恢复药品所在sheet" });
								e.printStackTrace();
								throw e;
							}
							long[] values = new long[简单宠物道具能修改的属性数];
							for (int j = 0; j < 简单宠物道具能修改的属性数; j++) {
								HSSFCell cell = row.getCell((short) (j + 简单宠物道具_血_列));
								long value = 0;
								try {
									value = (long) cell.getNumericCellValue();
								} catch (Exception ex) {
									try {
										value = Integer.parseInt(cell.getStringCellValue().trim());
									} catch (Exception e) {

									}
								}
								values[j] = value;
							}
							a.setValues(values);
							allSingleForPetProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allSingleForPetProps = allSingleForPetProps.toArray(new SingleForPetProps[0]);
				} else if (i == 物品文件召唤宠物道具所在sheet) {
					ArrayList<PetProps> allPetProps = new ArrayList<PetProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							PetProps a = new PetProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
							} catch (Exception e) {
								// // TODO Auto-generated catch
								// logger.error("articleinit" + "物品文件召唤宠物道具所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件召唤宠物道具所在sheet" });
								e.printStackTrace();
								throw e;
							}
							HSSFCell cell = row.getCell(宠物道具_对应avata_race_列);
							String avataRace = "";
							try {
								avataRace = cell.getStringCellValue().trim();
								a.setAvataRace(avataRace);
							} catch (Exception ex) {
								// throw ex;
							}

							cell = row.getCell(宠物道具_avata_sex_列);
							String avataSex = "";
							try {
								avataSex = cell.getStringCellValue().trim();
								a.setAvataSex(avataSex);
							} catch (Exception ex) {
								// throw ex;
							}

							cell = row.getCell(宠物道具_title_列);
							String title = "";
							try {
								title = cell.getStringCellValue().trim();
								a.setTitle(title);
							} catch (Exception ex) {
								// throw ex;
							}
							cell = row.getCell(宠物道具对应的蛋的名称_title_列);
							title = (cell.getStringCellValue().trim());
							a.setEggAticleName(title);

							cell = row.getCell(宠物道具对应的蛋的名称_title_列_STAT);
							String title_stat = "";
							try {
								title_stat = cell.getStringCellValue().trim();
								a.setTitle_stat(title_stat);
							} catch (Exception ex) {
								// throw ex;
							}

							short scale = 0;
							cell = row.getCell(宠物道具对应大小);
							scale = (short) cell.getNumericCellValue();
							a.setObjectScale(scale);

							int color = -1;
							cell = row.getCell(宠物道具对应颜色);
							color = (int) cell.getNumericCellValue();
							a.setObjectColor(color);

							boolean objectOpacity = false;
							cell = row.getCell(宠物道具是否透明);
							objectOpacity = cell.getBooleanCellValue();
							a.setObjectOpacity(objectOpacity);

							// TODO 宠物的例子效果

							cell = row.getCell(宠物道具_宠物粒子效果);
							if (cell != null) {
								a.setParticleName(cell.getStringCellValue());
							}
							a.talent1skill = getInt(row, 宠物天生技能一);
							a.talent2skill = getInt(row, 宠物天生技能二);
							allPetProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allPetProps = allPetProps.toArray(new PetProps[0]);

				} else if (i == 物品文件宠物蛋道具所在sheet) {
					ArrayList<PetEggProps> allPetEggProps = new ArrayList<PetEggProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							PetEggProps a = new PetEggProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
							} catch (Exception e) {
								// // TODO Auto-generated catch
								// logger.error("articleinit" + "物品文件宠物蛋道具所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件宠物蛋道具所在sheet" });
								e.printStackTrace();
								throw e;
							}
							HSSFCell cell = row.getCell(宠物蛋道具_对应孵化出来的宠物名称_列);
							String petPropsName = "";
							try {
								petPropsName = (cell.getStringCellValue().trim());
								a.setPetArticleName(petPropsName);
							} catch (Exception ex) {
								throw ex;
							}
							cell = row.getCell(宠物蛋道具_对应孵化出来的宠物名称_列_STAT);
							String petPropsName_stat = "";
							try {
								petPropsName_stat = (cell.getStringCellValue().trim());
								a.setPetArticleName_stat(petPropsName_stat);
							} catch (Exception ex) {
								throw ex;
							}
							cell = row.getCell(宠物蛋道具_稀有度_列);
							int 稀有度 = 0;
							try {
								稀有度 = (int) cell.getNumericCellValue();
								a.setRarity(稀有度);
							} catch (Exception ex) {
								try {
									稀有度 = Integer.parseInt(cell.getStringCellValue().trim());
									a.setRarity(稀有度);
								} catch (Exception e) {

								}
							}

							cell = row.getCell(宠物蛋道具_可携带等级_列);
							int 可携带等级 = 0;
							try {
								可携带等级 = (int) cell.getNumericCellValue();
								a.setRealTrainLevel(可携带等级);
							} catch (Exception ex) {
								try {
									可携带等级 = Integer.parseInt(cell.getStringCellValue().trim());
									a.setRealTrainLevel(可携带等级);
								} catch (Exception e) {

								}
							}

							cell = row.getCell(宠物蛋道具_职业倾向_列);
							int 职业倾向 = 0;
							try {
								职业倾向 = (int) cell.getNumericCellValue();
								a.setCareer((byte) 职业倾向);
							} catch (Exception ex) {
								try {
									职业倾向 = Integer.parseInt(cell.getStringCellValue().trim());
									a.setCareer((byte) 职业倾向);
								} catch (Exception e) {

								}
							}
							cell = row.getCell(宠物蛋道具_性格_列);
							int 性格 = 0;
							try {
								性格 = (int) cell.getNumericCellValue();
								a.setCharacter((byte) 性格);
							} catch (Exception ex) {
								try {
									性格 = Integer.parseInt(cell.getStringCellValue().trim());
									a.setCharacter((byte) 性格);
								} catch (Exception e) {

								}
							}
							cell = row.getCell(宠物蛋道具_分类_列);
							int 分类 = 0;
							try {
								分类 = (int) cell.getNumericCellValue();
								// a.setCharacter((byte) 性格);
								a.setType(分类);
							} catch (Exception ex) {
								try {
									分类 = Integer.parseInt(cell.getStringCellValue().trim());
									a.setType(分类);
								} catch (Exception e) {

								}
							}
							allPetEggProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allPetEggProps = allPetEggProps.toArray(new PetEggProps[0]);
				} else if (i == 物品文件宠物食物道具所在sheet) {
					ArrayList<PetFoodArticle> allPetFoodArticleList = new ArrayList<PetFoodArticle>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							PetFoodArticle a = new PetFoodArticle();
							try {
								设置物品一般属性(a, row);
							} catch (Exception e) {
								// // TODO Auto-generated c
								// logger.error("articleinit" + "物品文件宠物食物道具所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件宠物食物道具所在sheet" });
								e.printStackTrace();
								throw e;
							}
							HSSFCell cell = null;

							cell = row.getCell(喂养宠物物品_类型);
							byte 食物类型 = 1;
							try {
								食物类型 = (byte) cell.getNumericCellValue();
							} catch (Exception e) {
								食物类型 = Byte.valueOf(cell.getStringCellValue());
							}
							a.setType(食物类型);

							// cell = row.getCell(喂养宠物物品_增加值_级别);
							// int 宠物物品级别 = (int) cell.getNumericCellValue();
							// a.setFoodLevel(宠物物品级别);

							if (!allPetFoodArticleList.contains(a)) {
								allPetFoodArticleList.add(a);
								allArticle.add(a);
							}
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allPetFoodArticle = allPetFoodArticleList.toArray(new PetFoodArticle[0]);
				} else if (i == 物品文件buff类道具所在sheet) {
					ArrayList<LastingProps> allLastingProps = new ArrayList<LastingProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							LastingProps a = new LastingProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
								设置buff道具属性(a, row);
								a.setArticleCNNName(ReadFileTool.getString(row, buff道具_buff伴生物品名, logger));
								a.setLimitMaps(ReadFileTool.getStringArr(row, buff道具_buff伴生物品名 + 1, logger, ","));
							} catch (Exception e) {
								// // TODO Auto-generated catch
								// logger.error("articleinit" + "物品文件buff类道具所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件buff类道具所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allLastingProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allLastingProps = allLastingProps.toArray(new LastingProps[0]);
				} else if (i == 坐骑道具所在sheet) {

					HashMap<String, HorseProps> map = new HashMap<String, HorseProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							HorseProps a = new HorseProps();

							try {
								设置物品一般属性(a, row);
								设置一般道具属性(a, row);
							} catch (Exception e) {
								// logger.error("articleinit" + "坐骑道具所在sheet");
								logger.error("articleinit{}", new Object[] { "坐骑道具所在sheet" });
								e.printStackTrace();
								throw e;
							}

							HSSFCell cell = null;

							cell = row.getCell(是否是战斗坐骑);
							boolean 是否是战斗 = true;
							try {
								是否是战斗 = cell.getBooleanCellValue();
								a.setFight(是否是战斗);
							} catch (Exception e) {
								// e.printStackTrace();
								a.setFight(是否是战斗);
							}

							cell = row.getCell(是否是飞行坐骑);
							boolean 是否是飞行 = false;
							try {
								是否是飞行 = cell.getBooleanCellValue();
								a.setFly(是否是飞行);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								// e.printStackTrace();
								a.setFly(是否是飞行);
							}

							cell = row.getCell(坐骑道具_坐骑type);
							byte 坐骑类型 = (byte) cell.getNumericCellValue();
							a.setType(坐骑类型);

							cell = row.getCell(坐骑道具_等阶);
							byte 坐骑等阶 = (byte) cell.getNumericCellValue();
							a.setRank(坐骑等阶);

							cell = row.getCell(坐骑道具_速度);
							int 坐骑速度 = (int) cell.getNumericCellValue();
							a.setSpeed(坐骑速度);

							cell = row.getCell(坐骑道具_坐骑最大体力值);
							int 最大体力值 = (int) cell.getNumericCellValue();
							a.setMaxEnergy(最大体力值);

							cell = row.getCell(坐骑道具_对应avata_列);
							String avata = "";
							try {
								avata = cell.getStringCellValue().trim();
								a.setAvata(avata);
							} catch (Exception ex) {
								// throw ex;
							}
							//
							cell = row.getCell(坐骑道具_粒子);
							String horseParticle = "";
							try {
								horseParticle = cell.getStringCellValue().trim();
								a.setHorseParticle(horseParticle);
							} catch (Exception ex) {
								a.setHorseParticle("");
							}
							cell = row.getCell(坐骑道具_是否特殊坐骑);
							boolean specialHorse = false;
							try {
								specialHorse = cell.getBooleanCellValue();
							} catch (Exception e) {
							}
							a.setSpecialHorse(specialHorse);

							String name = a.getName();
							if (name != null) {
								if (map.get(name) == null) {
									allArticle.add(a);
									map.put(name, a);
								}
							}
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.horsePropsMap = map;
				} else if (i == 喂养坐骑物品所在sheet) {

					ArrayList<Article> allArticle = new ArrayList<Article>();
					HashMap<String, HorseFoodArticle> map = new HashMap<String, HorseFoodArticle>();
					for (int r = 1; r < rows; r++) {

						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							HorseFoodArticle a = new HorseFoodArticle();
							try {
								设置物品一般属性(a, row);
							} catch (Exception e) {
								// logger.error("articleinit" + "喂养坐骑物品所在sheet");
								logger.error("articleinit{}", new Object[] { "喂养坐骑物品所在sheet" });
								e.printStackTrace();
								throw e;
							}

							HSSFCell cell = null;
							cell = row.getCell(喂养坐骑物品_增加体力值);
							int 体力值 = (int) cell.getNumericCellValue();
							a.setEnergy(体力值);

							cell = row.getCell(喂养坐骑物品_类型);
							byte 类型 = (byte) cell.getNumericCellValue();
							a.setType(类型);

							String name = a.getName();
							if (name != null) {
								if (map.get(name) == null) {
									allArticle.add(a);
									map.put(name, a);
								}
							}
						}

					}
					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.horseFoodArticleMap = map;
					//
				} else if (i == 道具种类所在sheet) {

					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							PropsCategory a = new PropsCategory();
							HSSFCell cell = null;

							cell = row.getCell(0);
							String name = cell.getStringCellValue().trim();
							a.setCategoryName(name);

							cell = row.getCell(1);
							String name_stat = cell.getStringCellValue().trim();
							a.setCategoryName_stat(name_stat);

							cell = row.getCell(2);
							byte 引导类型 = (byte) cell.getNumericCellValue();
							a.setStalemateType(引导类型);
							cell = row.getCell(3);
							long 引导时间 = 0;
							try {
								引导时间 = (long) cell.getNumericCellValue();
								a.setStalemateTime(引导时间);
							} catch (Exception ex) {
								throw ex;
							}
							cell = row.getCell(4);
							long 间隔时间 = 0;
							try {
								间隔时间 = (long) cell.getNumericCellValue();
								a.setCooldownLimit(间隔时间);
							} catch (Exception ex) {
								throw ex;
							}
							if (this.propsCategoryMap.get(name) == null) {
								this.propsCategoryMap.put(name, a);
							}
						}
					}
				} else if (i == 物品文件防爆包道具所在sheet) {
					ArrayList<KnapsackExpandProps> allKnapsackExpandProps = new ArrayList<KnapsackExpandProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							KnapsackExpandProps a = new KnapsackExpandProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
							} catch (Exception e) {
								// logger.error("articleinit" + "物品文件防爆包道具所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件防爆包道具所在sheet" });
								e.printStackTrace();
								throw e;
							}
							HSSFCell cell = row.getCell(防爆包道具_背包扩展数量_列);
							int expandNum = 0;
							try {
								expandNum = Integer.parseInt(cell.getStringCellValue().trim());
							} catch (Exception ex) {
								try {
									expandNum = (int) cell.getNumericCellValue();
								} catch (Exception e) {

								}
								// throw ex;
							}
							a.setExpandNum(expandNum);
							allKnapsackExpandProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allKnapsackExpandProps = allKnapsackExpandProps.toArray(new KnapsackExpandProps[0]);
				} else if (i == 宠物buff类道具所在sheet) {

					ArrayList<LastingForPetProps> petLastingProps = new ArrayList<LastingForPetProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							LastingForPetProps a = new LastingForPetProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
								设置buff道具属性(a, row);

								// 宠物特殊要求
								HSSFCell cell = row.getCell(32);
								int value = (int) cell.getNumericCellValue();
								a.setValue(value);
							} catch (Exception e) {
								// logger.error("articleinit" + "宠物buff类道具所在sheet");
								logger.error("articleinit{}", new Object[] { "宠物buff类道具所在sheet" });
								e.printStackTrace();
								throw e;
							}
							petLastingProps.add(a);
							allArticle.add(a);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.petLastingForPetProps = petLastingProps;

				} else if (i == 洗髓丹道具所在sheet) {

					ArrayList<ClearSkillPointsProps> allClearSkillPointsProps = new ArrayList<ClearSkillPointsProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							ClearSkillPointsProps a = new ClearSkillPointsProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
								// 洗髓丹道具_返还修法值比例_列
								HSSFCell cell = row.getCell(洗髓丹道具_返还修法值比例_列);
								if (cell != null) {
									a.setReBackRate(cell.getNumericCellValue());
								} else {
									a.setReBackRate(1);
								}
							} catch (Exception e) {
								e.printStackTrace();
								throw e;
							}
							allClearSkillPointsProps.add(a);
							allArticle.add(a);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allClearSkillPointsProps = allClearSkillPointsProps.toArray(new ClearSkillPointsProps[0]);
				} else if (i == 技能书道具所在sheet) {

					ArrayList<BookProps> allBookProps = new ArrayList<BookProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							BookProps a = new BookProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
								HSSFCell cell = row.getCell(技能书_技能id);
								try {
									int skillId = (int) cell.getNumericCellValue();
									a.setSkillId(skillId);
								} catch (Exception ex) {
									try {
										int skillId = Integer.parseInt(cell.getStringCellValue().trim());
										a.setSkillId(skillId);
									} catch (Exception e) {
										throw e;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								throw e;
							}
							allBookProps.add(a);
							allArticle.add(a);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allBookProps = allBookProps.toArray(new BookProps[0]);
				} else if (i == 任务物品所在sheet) {

					ArrayList<Article> allCommonArticle = new ArrayList<Article>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							Article a = new Article();
							try {
								设置物品一般属性(a, row);
							} catch (Exception e) {
								// logger.info("articleinit" + "任务物品所在sheet");
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "任务物品所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allCommonArticle.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allTaskUsedArticles = allCommonArticle.toArray(new Article[0]);
					// this.articles = articles;
				} else if (i == 时装道具所在sheet) {

					ArrayList<AvataProps> allAvataProps = new ArrayList<AvataProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							AvataProps a = new AvataProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
								HSSFCell cell = row.getCell(时装道具_avatasex);
								try {
									String avatasex = cell.getStringCellValue().trim();
									a.setAvata(avatasex);
								} catch (Exception ex) {
									throw ex;
								}
								cell = row.getCell(时装道具_type);
								try {
									byte type = (byte) cell.getNumericCellValue();
									a.setType(type);
								} catch (Exception ex) {
									try {
										byte type = Byte.parseByte(cell.getStringCellValue().trim());
										a.setType(type);
									} catch (Exception e) {
										throw e;
									}
								}
								cell = row.getCell(时装道具_values);
								try {
									String valueStrs = cell.getStringCellValue().trim();
									String[] valuess = valueStrs.split(";");
									if (valuess.length == 1) {
										valuess = valueStrs.split("；");
									}
									int[] values = new int[valuess.length];
									for (int j = 0; j < values.length; j++) {
										values[j] = Integer.parseInt(valuess[j]);
									}
									a.setValues(values);
								} catch (Exception ex) {

								}

								cell = row.getCell(人时装还是坐骑时装);
								try {
									byte type = (byte) cell.getNumericCellValue();
									a.setUseType(type);
								} catch (Exception ex) {

								}

							} catch (Exception e) {
								// logger.info("articleinit" + "时装道具所在sheet");
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "时装道具所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allAvataProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allAvataProps = allAvataProps.toArray(new AvataProps[0]);
					// this.articles = articles;
				} else if (i == 王者之印所在sheet) {

					ArrayList<WangZheZhiYinProps> allWangZheZhiYinProps = new ArrayList<WangZheZhiYinProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							WangZheZhiYinProps a = new WangZheZhiYinProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
							} catch (Exception e) {
								// logger.info("articleinit" + "王者之印所在sheet");
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "王者之印所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allWangZheZhiYinProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allWangZheZhiYinProps = allWangZheZhiYinProps.toArray(new WangZheZhiYinProps[0]);
					// this.articles = articles;
				} else if (i == 召集令所在sheet) {

					ArrayList<ZhaoJiLingProps> allZhaoJiLingProps = new ArrayList<ZhaoJiLingProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							ZhaoJiLingProps a = new ZhaoJiLingProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);

								HSSFCell cell = row.getCell(召集令道具_类型);
								a.setZhaoJiLingType((byte) cell.getNumericCellValue());

								cell = row.getCell(召集令道具_目标城市);
								try {
									String transferGame = cell.getStringCellValue();
									a.setTransferGame(transferGame);
								} catch (Exception ex) {

								}
								cell = row.getCell(召集令道具_是否限制国家);
								boolean countryLimit = true;
								if (cell != null) {
									countryLimit = cell.getBooleanCellValue();
								}
								a.setCountryLimit(countryLimit);

							} catch (Exception e) {
								// logger.info("articleinit" + "召集令所在sheet");
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "召集令所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allZhaoJiLingProps.add(a);
							allArticle.add(a);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allZhaoJiLingProps = allZhaoJiLingProps.toArray(new ZhaoJiLingProps[0]);
				} else if (i == 宝箱所在sheet) {
					ArrayList<PackageProps> allPackageProps = new ArrayList<PackageProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							PackageProps a = new PackageProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
								r = 设置宝箱道具属性(a, sheet, r, rows);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "宝箱所在sheet" }, e);
								e.printStackTrace();
								throw e;
							}
							allPackageProps.add(a);
							allArticle.add(a);
						}
					}
					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allPackageProps = allPackageProps.toArray(new PackageProps[0]);
				} else if (i == 随机宝箱所在sheet) {
					try {
						ArrayList<RandomPackageProps> allRandomPackageProps = new ArrayList<RandomPackageProps>();
						ArrayList<Article> allArticle = new ArrayList<Article>();
						for (int r = 1; r < rows; r++) {
							HSSFRow row = sheet.getRow(r);
							if (row != null) {
								RandomPackageProps a = new RandomPackageProps();
								try {
									设置物品一般属性(a, row);
									设置一般道具属性((Props) a, row);
									r = 设置随机宝箱道具属性(a, sheet, r, rows);
								} catch (Exception e) {
									if (logger.isInfoEnabled()) logger.info("articleinit随机宝箱所在sheet,[行数:" + r + "]", e);
									e.printStackTrace();
									throw e;
								}
								allRandomPackageProps.add(a);
								allArticle.add(a);
								logger.warn("[随机宝箱] [行：" + r + "] [总行：" + rows + "] [物品：" + a.getName() + "]");
							}
						}
						if (allArticles != null) {
							for (Article a : allArticles) {
								allArticle.add(a);
							}
						}
						this.allArticles = allArticle.toArray(new Article[0]);
						this.allRandomPackageProps = allRandomPackageProps.toArray(new RandomPackageProps[0]);
					} catch (Exception e) {
						e.printStackTrace();
						logger.warn("[随机宝箱] [异常：", e);
						throw e;
					}
				} else if (i == 境界所在sheet) {

					ArrayList<JingjieProps> allJingjieProps = new ArrayList<JingjieProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							JingjieProps a = new JingjieProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
							} catch (Exception e) {
								// logger.info("articleinit" + "境界所在sheet");
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "境界所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allJingjieProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allJingjieProps = allJingjieProps.toArray(new JingjieProps[0]);
					// this.articles = articles;
				} else if (i == 仙缘活动所在sheet) {

					ArrayList<Article> allArticle = new ArrayList<Article>();
					HashMap<String, FateActivityProps> map = new HashMap<String, FateActivityProps>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							FateActivityProps fa = new FateActivityProps();
							try {
								设置物品一般属性(fa, row);
								设置一般道具属性(fa, row);
							} catch (Exception e) {
								logger.error("articleinit{}", new Object[] { "仙缘活动所在sheet" });
								e.printStackTrace();
								throw e;
							}
							HSSFCell cell = null;
							cell = row.getCell(activity_type);
							byte type = (byte) cell.getNumericCellValue();
							fa.setType(type);

							cell = row.getCell(activity_avata);
							String avata = "";
							try {
								avata = cell.getStringCellValue().trim();
								fa.setAvata(avata);
							} catch (Exception ex) {
								logger.error("articleinit" + "仙缘活动所在sheet", ex);
							}
							String name = fa.getName();
							if (name != null) {
								if (map.get(name) == null) {
									allArticle.add(fa);
									map.put(name, fa);
								}
							}
						}
					}
					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.fateActivityPropsMap = map;
				} else if (i == 可交换宝物所在sheet) {

					ArrayList<Article> allArticle = new ArrayList<Article>();
					ArrayList<ExchangeArticle> exchangeList = new ArrayList<ExchangeArticle>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							ExchangeArticle ea = new ExchangeArticle();
							try {
								设置物品一般属性(ea, row);
							} catch (Exception e) {
								logger.error("articleinit{}", new Object[] { "可交换宝物所在sheet" });
								e.printStackTrace();
								throw e;
							}

							HSSFCell cell = null;
							cell = row.getCell(可配对的交换物品);

							String 可配对的交换物品name = "";
							try {
								可配对的交换物品name = (cell.getStringCellValue().trim());
							} catch (Exception ex) {
							}
							// String 可配对的交换物品name = cell.getStringCellValue().trim();
							ea.setPartnerArticle(可配对的交换物品name);

							cell = row.getCell(可配对的交换物品_STAT);

							String 可配对的交换物品name_stat = "";
							try {
								可配对的交换物品name_stat = (cell.getStringCellValue().trim());
							} catch (Exception ex) {
							}
							// String 可配对的交换物品name = cell.getStringCellValue().trim();
							ea.setPartnerArticle_stat(可配对的交换物品name_stat);

							cell = row.getCell(配对成功后生成的物品);
							String 配对成功后生成的物品name = "";
							try {
								配对成功后生成的物品name = (cell.getStringCellValue().trim());
							} catch (Exception ex) {
							}
							// String 可配对的交换物品name = cell.getStringCellValue().trim();
							ea.setCreateArticle(配对成功后生成的物品name);

							cell = row.getCell(配对成功后生成的物品_STAT);
							String 配对成功后生成的物品name_stat = "";
							try {
								配对成功后生成的物品name_stat = (cell.getStringCellValue().trim());
							} catch (Exception ex) {
							}
							// String 可配对的交换物品name = cell.getStringCellValue().trim();
							ea.setCreateArticle_stat(配对成功后生成的物品name_stat);

							cell = row.getCell(宝物类型);
							try {
								int articleType = (int) cell.getNumericCellValue();
								ea.setExchangearticleType(articleType);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							try {
								cell = row.getCell(是否还用);
								if (cell != null) {
									boolean bln = cell.getBooleanCellValue();
									ea.setUse(bln);
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							String name = ea.getName();
							if (name != null) {

								if (!exchangeList.contains(ea)) {
									exchangeList.add(ea);
									allArticle.add(ea);
								}
							}
						}
					}
					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.exchangeArticleList = exchangeList;

				} else if (i == 寻宝道具所在sheet) {
					ArrayList<Article> allArticle = new ArrayList<Article>();
					List<ExploreProps> exploreProps = new ArrayList<ExploreProps>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							ExploreProps ep = new ExploreProps();
							try {
								设置物品一般属性(ep, row);
								设置一般道具属性(ep, row);
							} catch (Exception e) {
								logger.error("articleinit{}", new Object[] { "寻宝道具所在sheet" });
								e.printStackTrace();
								throw e;
							}
							if (!exploreProps.contains(ep)) {
								allArticle.add(ep);
								exploreProps.add(ep);
							}
						}

						if (allArticles != null) {
							for (Article a : allArticles) {
								allArticle.add(a);
							}
						}
						this.allArticles = allArticle.toArray(new Article[0]);
						this.explorePropsList = exploreProps;
					}
				} else if (i == 混沌万灵榜对应宝石所在sheet) {

					int 装备名 = 0;
					int 装备名_STAT = 1;
					int 对应的宝石 = 2;
					int 对应的宝石_STAT = 3;
					HashMap<String, SpecialEquipmentMappedStone> mappedStones = new HashMap<String, SpecialEquipmentMappedStone>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							SpecialEquipmentMappedStone mappedStone = new SpecialEquipmentMappedStone();
							HSSFCell cell = null;
							cell = row.getCell(装备名);
							String name = cell.getStringCellValue();
							mappedStone.setEquipmentName(name);

							cell = row.getCell(装备名_STAT);
							String name_stat = cell.getStringCellValue();
							mappedStone.setEquipmentName_stat(name_stat);

							cell = row.getCell(对应的宝石);
							String mappind = cell.getStringCellValue();
							String[] arr = mappind.split(";");
							mappedStone.setStoneNames(arr);
							if (mappedStones.get(name) == null) {
								mappedStones.put(name, mappedStone);
							}

							cell = row.getCell(对应的宝石_STAT);
							String mappind_stat = cell.getStringCellValue();
							String[] arr_stat = mappind_stat.split(";");
							mappedStone.setStoneNames_stat(arr_stat);
						}
					}
					this.mappedStoneMap = mappedStones;
				} else if (i == 古董所在sheet) {

					ArrayList<BottleProps> allBottleProps = new ArrayList<BottleProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							BottleProps a = new BottleProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
								r = 设置古董道具属性(a, sheet, r, rows);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "宝箱所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allBottleProps.add(a);
							allArticle.add(a);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allBottleProps = allBottleProps.toArray(new BottleProps[0]);

				} else if (i == 寻宝藏宝图所在sheet) {

					ArrayList<ExploreResourceMap> allExploreResourceMaps = new ArrayList<ExploreResourceMap>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							ExploreResourceMap er = new ExploreResourceMap();
							try {
								设置物品一般属性(er, row);
								设置一般道具属性((Props) er, row);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "寻宝藏宝图所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allExploreResourceMaps.add(er);
							allArticle.add(er);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allExploreResourceMaps = allExploreResourceMaps.toArray(new ExploreResourceMap[0]);

				} else if (i == 触发任务道具所在Sheet) {

					ArrayList<TaskProps> allTaskProps = new ArrayList<TaskProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							TaskProps a = new TaskProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);

								HSSFCell cell = row.getCell(触发任务道具_任务id);
								String[] strs = cell.getStringCellValue().trim().split(";");
								if (strs.length == 1) {
									strs = cell.getStringCellValue().trim().split("；");
								}
								int[] ids = new int[strs.length];
								for (int j = 0; j < ids.length; j++) {
									ids[j] = Integer.parseInt(strs[j]);
								}
								a.setIds(ids);

								int type = 1;
								cell = row.getCell(触发任务道具_类型);
								if (cell != null) {
									try {
										type = (int) cell.getNumericCellValue();
									} catch (Exception e) {
										e.printStackTrace();
										type = 1;
									}
								}
								a.setType(type);

								logger.warn("[触发任务道具] [" + a.getName() + "] [type:" + type + "] [" + cell + "]");
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "触发任务道具所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allTaskProps.add(a);
							allArticle.add(a);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allTaskProps = allTaskProps.toArray(new TaskProps[0]);

				} else if (i == 物品文件buff_可合成_类道具所在Sheet) {
					ArrayList<Lasting_For_Compose_Props> allLasting_For_Compose_Props = new ArrayList<Lasting_For_Compose_Props>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							Lasting_For_Compose_Props a = new Lasting_For_Compose_Props();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
								设置buff道具属性(a, row);
							} catch (Exception e) {
								// // TODO Auto-generated catch
								// logger.error("articleinit" + "物品文件buff类道具所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件buff类道具所在sheet" });
								e.printStackTrace();
								throw e;
							}

							HSSFCell cell = row.getCell(道具的使用上限);
							int maxLevel = (int) cell.getNumericCellValue();
							a.setUseMaxLevel(maxLevel);

							allLasting_For_Compose_Props.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allLasting_For_Compose_Props = allLasting_For_Compose_Props.toArray(new Lasting_For_Compose_Props[0]);
				} else if (i == 金钱类道具所在Sheet) {
					ArrayList<MoneyProps> allMoneyProps = new ArrayList<MoneyProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							MoneyProps a = new MoneyProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
								a.money = (int) row.getCell(金钱类道具_钱).getNumericCellValue();
								if (row.getCell(金钱类道具_类型) != null) {
									a.type = (byte) row.getCell(金钱类道具_类型).getNumericCellValue();
								}
							} catch (Exception e) {
								// // TODO Auto-generated catch
								// logger.error("articleinit" + "物品文件buff类道具所在sheet");
								logger.error("articleinit{}", new Object[] { "金钱类道具所在sheet" });
								e.printStackTrace();
								throw e;
							}
							allMoneyProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allMoneyProps = allMoneyProps.toArray(new MoneyProps[0]);
				} else if (i == 职业宝箱类道具所在Sheet) {
					ArrayList<CareerPackageProps> allCareerPackageProps = new ArrayList<CareerPackageProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							CareerPackageProps a = new CareerPackageProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
								r = 设置职业宝箱类道具属性(a, sheet, r, rows);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "职业宝箱类道具所在sheet" }, e);
								e.printStackTrace();
								throw e;
							}
							allCareerPackageProps.add(a);
							allArticle.add(a);
						}
					}
					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allCareerPackageProps = allCareerPackageProps.toArray(new CareerPackageProps[0]);
				} else if (i == 洗红名类道具所在Sheet) {
					ArrayList<ClearRedProps> allClearRedProps = new ArrayList<ClearRedProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							ClearRedProps a = new ClearRedProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
							} catch (Exception e) {
								// logger.error("articleinit" + "物品文件给玩家用的瞬间恢复药品所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件给玩家用的红名药品所在sheet" });
								e.printStackTrace();
								throw e;
							}
							a.setType((int) row.getCell(洗红名道具_type).getNumericCellValue());
							a.setValue((double) row.getCell(洗红名道具_value).getNumericCellValue());
							a.setLimit((int) row.getCell(洗红名道具_limit).getNumericCellValue());
							allClearRedProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allClearRedProps = allClearRedProps.toArray(new ClearRedProps[0]);
				} else if (i == 加体力类道具所在Sheet) {
					ArrayList<TiliProps> allTiliProps = new ArrayList<TiliProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							TiliProps a = new TiliProps();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
							} catch (Exception e) {
								// logger.error("articleinit" + "物品文件给玩家用的瞬间恢复药品所在sheet");
								logger.error("articleinit{}", new Object[] { "物品文件给玩家用的体力药品所在sheet" });
								e.printStackTrace();
								throw e;
							}
							a.setValue((int) row.getCell(体力道具_value).getNumericCellValue());
							allTiliProps.add(a);
							allArticle.add(a);
							// articles.put(e.getName(), e);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allTiliProps = allTiliProps.toArray(new TiliProps[0]);
				} else if (i == 器灵物品所在Sheet) {

					ArrayList<QiLingArticle> allQiLingArticles = new ArrayList<QiLingArticle>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							QiLingArticle a = new QiLingArticle();
							try {
								设置物品一般属性(a, row);
							} catch (Exception e) {
								e.printStackTrace();
								throw e;
							}
							byte qilingType = (byte) row.getCell(器灵类型).getNumericCellValue();
							byte equipmentType = (byte) row.getCell(器灵装备限制).getNumericCellValue();
							int qilingTimes = 0;
							try {
								qilingTimes = Integer.parseInt(row.getCell(器灵属性倍数).getStringCellValue());
							} catch (Exception e) {
								qilingTimes = (int) (row.getCell(器灵属性倍数).getNumericCellValue());
							}
							int qilingHunLiangTimes = 0;
							try {
								qilingHunLiangTimes = Integer.parseInt(row.getCell(器灵魂量倍数).getStringCellValue());
							} catch (Exception e) {
								qilingHunLiangTimes = (int) (row.getCell(器灵魂量倍数).getNumericCellValue());
							}
							a.setQilingType(qilingType);
							a.setEquipmentType(equipmentType);
							a.setPropertysValues(器灵各个颜色最大属性值[qilingType]);
							a.setQilingTimes(qilingTimes);
							a.setQilingHunLiangTimes(qilingHunLiangTimes);
							allQiLingArticles.add(a);
							allArticle.add(a);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allQiLingArticle = allQiLingArticles.toArray(new QiLingArticle[0]);

				} else if (i == 采花大盗物品所在Sheet) {

					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							PickFlowerArticle a = new PickFlowerArticle();
							try {
								设置物品一般属性(a, row);
							} catch (Exception e) {
								e.printStackTrace();
								throw e;
							}
							int 类型 = (int) row.getCell(采花物品类型).getNumericCellValue();
							int 基础值 = (int) row.getCell(采花物品基础值).getNumericCellValue();
							String 基础值描述 = row.getCell(采花物品类型描述).getStringCellValue();

							a.setFlowerType(类型);
							a.setScore(基础值);
							a.setFlowerTypeDes(基础值描述);

							allArticle.add(a);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
				} else if (i == 等级随机礼包所在Sheet) {
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							LevelRandomPackage a = new LevelRandomPackage();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性(a, row);
							} catch (Exception e) {
								e.printStackTrace();
								throw e;
							}
							int insideType = (int) row.getCell(所有道具_战斗状态限制_列 + 1).getNumericCellValue();
							int outputNum = (int) row.getCell(所有道具_战斗状态限制_列 + 2).getNumericCellValue();
							Integer[] outputColor = StringTool.string2Array(row.getCell(所有道具_战斗状态限制_列 + 3).getStringCellValue(), ",", Integer.class);
							boolean outputBind = (int) row.getCell(所有道具_战斗状态限制_列 + 4).getNumericCellValue() == 1;
							a.setInsideType(insideType);
							a.setOutputNum(outputNum);
							a.setOutputBind(outputBind);
							a.setRate(outputColor);

							a.init();
							allArticle.add(a);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
				} else if (i == petSkillBook_Sheet) {
					loadPetSkillBooks(rows, sheet);
				} else if (i == robberyPorps_Sheet) {
					loadRobberyProps(rows, sheet);
				} else if (i == 法宝所在Sheet) {
					loadMagicWeapon(rows, sheet);
				} else if (i == 法宝吞噬道具所在Sheet) {
					loadMagicWeaponEat(rows, sheet);
				} else if (i == 附魔道具所在Sheet) {
					loadEnchant(rows, sheet);
				} else if (i == 兽魂道具所在Sheet) {
					loadHuntArticle(rows, sheet);
				} else if (i == 宠物悟性丹所在Sheet) {
					loadPetWuXingProp(sheet);
				} else if (i == 灵髓道具Sheet) {
					loadSoulPithArticle(rows, sheet);
				} else if (i == 仙气葫芦道具Sheet) {
					loadGourdArticle(rows, sheet);
				} else if (i == gainTitleItem_Sheet) {
					loadTitleItems(rows, sheet);
				} else if (i == 器灵丹道具所在Sheet) {
					loadQiLingDan(rows, sheet);
				} else if (i == 打折卡所在Sheet) {
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							DiscountCard a = new DiscountCard();
							try {
								设置物品一般属性(a, row);
							} catch (Exception e) {
								logger.error("[加载道具失败]", e);
								throw e;
							}
							HSSFCell cell = row.getCell(打折的道具名);
							String discountArticleName = "";
							try {
								discountArticleName = (cell.getStringCellValue().trim());
							} catch (Exception ex) {
								logger.error("[加载道具失败]", ex);
								throw ex;
							}
							a.setDiscountArticleName(discountArticleName);

							cell = row.getCell(打折的道具名_STAT);
							String discountArticleName_stat = "";
							try {
								discountArticleName_stat = (cell.getStringCellValue().trim());
							} catch (Exception ex) {
								logger.error("[加载道具失败]", ex);
								throw ex;
							}
							a.setDiscountArticleName_stat(discountArticleName_stat);

							cell = row.getCell(打折的道具颜色);
							int buyArticlecolor = 0;
							try {
								buyArticlecolor = (int) (cell.getNumericCellValue());
							} catch (Exception ex) {
								logger.error("[加载道具失败]", ex);
								throw ex;
							}
							a.setBuyArticlecolor(buyArticlecolor);

							cell = row.getCell(打折的折扣);
							double discountRate = 1;
							try {
								discountRate = (double) (cell.getNumericCellValue());
							} catch (Exception ex) {
								logger.error("[加载道具失败]", ex);
								throw ex;
							}
							a.setDiscountRate(discountRate);

							cell = row.getCell(打折要求购买的道具数量);
							int buyArticleNum = 1;
							try {
								buyArticleNum = (int) (cell.getNumericCellValue());
							} catch (Exception ex) {
								logger.error("[加载道具失败]", ex);
								throw ex;
							}
							a.setBuyArticleNum(buyArticleNum);

							cell = row.getCell(打折的商店名);
							String shopName = "";
							try {
								shopName = (cell.getStringCellValue().trim());
							} catch (Exception ex) {
								throw ex;
							}
							a.setShopName(shopName);

							cell = row.getCell(打折的商店名_STAT);
							String shopName_stat = "";
							try {
								shopName_stat = (cell.getStringCellValue().trim());
							} catch (Exception ex) {
								throw ex;
							}
							a.setShopName_stat(shopName_stat);

							DiscountCardAgent.getInstance().getList().add(a);

							allArticle.add(a);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
				} else if (i == 召唤生物道具所在Sheet) {
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							CallSpriteProp a = new CallSpriteProp();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);

								HSSFCell cell = row.getCell(所有道具_战斗状态限制_列 + 1);// 類型 0怪物1NPC
								if (cell != null) {
									int callType = (int) cell.getNumericCellValue();
									a.setCallType(callType);
								} else {
									throw new Exception(a.getName() + "没有正确的设置类型");
								}
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "职业宝箱类道具所在sheet" }, e);
								e.printStackTrace();
								throw e;
							}
							HSSFCell cell = row.getCell(所有道具_战斗状态限制_列 + 2);// 類型 0怪物1NPC
							if (cell != null) {
								String callMapString = cell.getStringCellValue();
								a.setCallMapLimit(StringTool.string2Array(callMapString, ",", String.class));
							} else {
								throw new Exception(a.getName() + "没有正确的设置类型");
							}
							List<Integer> idList = new ArrayList<Integer>();
							for (int k = 0; k <= BournManager.maxBournLevel; k++) {
								cell = row.getCell(所有道具_战斗状态限制_列 + 2 + k + 1);
								if (cell != null) {
									int id = (int) cell.getNumericCellValue();
									idList.add(id);
								} else {
									idList.add(0);
								}
							}

							a.setIds(idList.toArray(new Integer[0]));
							if ("地灵天书".equals(a.getName_stat())) {
								GlobalLimitManager.getInst().bossIds = idList;
							}
							allArticle.add(a);
						}
					}
					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
				} else if (i == 触发输入框道具所在Sheet) {
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							OpenInputProp a = new OpenInputProp();
							try {
								设置物品一般属性(a, row);
								设置一般道具属性((Props) a, row);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "触发输入框道具所在Sheet" }, e);
								e.printStackTrace();
								throw e;
							}
							HSSFCell cell = row.getCell(所有道具_战斗状态限制_列 + 1);// 广播和飘花的范围
							a.setSendScope((int) cell.getNumericCellValue());
							cell = row.getCell(所有道具_战斗状态限制_列 + 2);// 消息发送的聊天频道
							a.setShowMessageArea((int) cell.getNumericCellValue());
							cell = row.getCell(所有道具_战斗状态限制_列 + 3);// 消息发送的聊天频道
							a.setNeedSendColorfull(cell.getBooleanCellValue());
							cell = row.getCell(所有道具_战斗状态限制_列 + 4);// 飘糖还是花
							a.setColorfullType((int) cell.getNumericCellValue());
							cell = row.getCell(所有道具_战斗状态限制_列 + 5);// 飘糖/花的索引
							a.setColorfullIndex((int) cell.getNumericCellValue());
							cell = row.getCell(所有道具_战斗状态限制_列 + 6);// 窗口描述
							a.setWindowDes(cell.getStringCellValue());

							allArticle.add(a);
						}
					}
					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);

				} else if (i == 挖宝道具所在Sheet) {

					ArrayList<DigProps> allDigProps = new ArrayList<DigProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							DigProps dp = new DigProps();
							try {
								设置物品一般属性(dp, row);
								设置一般道具属性((Props) dp, row);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "挖宝道具所在Sheet" });
								e.printStackTrace();
								throw e;
							}
							allDigProps.add(dp);
							allArticle.add(dp);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allDigProps = allDigProps.toArray(new DigProps[0]);
				} else if (i == 变异宠道具所在Sheet) {

					ArrayList<VariationPetProps> allVariationPetProps = new ArrayList<VariationPetProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							VariationPetProps vpp = new VariationPetProps();
							try {
								设置物品一般属性(vpp, row);
								设置一般道具属性((Props) vpp, row);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "变异宠道具所在Sheet" });
								e.printStackTrace();
								throw e;
							}
							vpp.setPetEggName(row.getCell(变异宠道具_petName).getStringCellValue());
							vpp.setPetEggName_stat(row.getCell(变异宠道具_petNameStat).getStringCellValue());
							vpp.setGeneration((byte) row.getCell(变异宠道具_generation).getNumericCellValue());
							vpp.setVariation((byte) row.getCell(变异宠道具_variation).getNumericCellValue());
							vpp.setTianshenSkill(ReadFileTool.getIntArrByString(row, 变异宠道具_jichuSkills, logger, ";"));
							vpp.setTianshenLvs(ReadFileTool.getIntArrByString(row, 变异宠道具_jichuLvs, logger, ";"));
							// vpp.setTianfuLvs(ReadFileTool.getIntArrByString(row, 变异宠道具_tianfuLvs, logger, ";"));
							vpp.setTianfuSkill(ReadFileTool.getIntArrByString(row, 变异宠道具_tianfuSkills, logger, ";"));
							vpp.setIsopenxiantian(ReadFileTool.getBoolean(row, 变异宠道具_openxiantian, logger));
							vpp.setIsopenhoutian(ReadFileTool.getBoolean(row, 变异宠道具_openhoutian, logger));
							allVariationPetProps.add(vpp);
							allArticle.add(vpp);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allVariationPetProps = allVariationPetProps.toArray(new VariationPetProps[0]);
				} else if (i == 宠物悟性丹所在Sheet) {
					loadPetWuXingProp(sheet);
				} else if (i == 图纸合成规则所在Sheet) {
					loadEquipmentRule(sheet);
				} else if (i == 装备图纸所在Sheet) {
					loadEquipmentTuZhi(sheet);
				} else if (i == 装备套装所在Sheet) {
					loadEquipmentSuit(sheet);
				} else if (i == 翅膀所在Sheet) {

					ArrayList<WingProps> allWingProps = new ArrayList<WingProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							WingProps wingProps = new WingProps();
							try {
								设置物品一般属性(wingProps, row);
								设置一般道具属性((Props) wingProps, row);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "翅膀道具所在Sheet" });
								e.printStackTrace();
								throw e;
							}
							wingProps.setKindId((byte) row.getCell(翅膀道具_kindId).getNumericCellValue());
							allWingProps.add(wingProps);
							allArticle.add(wingProps);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allWingProps = allWingProps.toArray(new WingProps[0]);
				} else if (i == 光效宝石所在Sheet) {

					ArrayList<BrightInlayProps> allBrightInlayProps = new ArrayList<BrightInlayProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							BrightInlayProps tip = new BrightInlayProps();
							try {
								设置物品一般属性(tip, row);
								设置一般道具属性((Props) tip, row);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "光效宝石道具所在Sheet" });
								e.printStackTrace();
								throw e;
							}
							allBrightInlayProps.add(tip);
							allArticle.add(tip);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allBrightInlayProps = allBrightInlayProps.toArray(new BrightInlayProps[0]);
				}
				// else if(i == 宠物灵性道具所在Sheet){
				// ArrayList<Article> allArticle = new ArrayList<Article>();
				// for (int r = 1; r < rows; r++) {
				// HSSFRow row = sheet.getRow(r);
				// if (row == null) {
				// continue;
				// }
				// Article a = new Article();
				// try {
				// 设置物品一般属性(a, row);
				// } catch (Exception e) {
				// e.printStackTrace();
				// throw e;
				// }
				//						
				// allArticle.add(a);
				// logger.warn("[加载宠物飞升道具] [页:"+i+"] [行：" + r + "] [name:" + a.getName() + "]");
				// }
				// if (allArticles != null) {
				// for (Article a : allArticles) {
				// allArticle.add(a);
				// }
				// }
				// this.allArticles = allArticle.toArray(new Article[0]);
				//					
				// }
				else if (i == 减仙婴附体cd道具Sheet) {
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row == null) {
							continue;
						}
						MinusSkillCdTimesProps pp = new MinusSkillCdTimesProps();
						try {
							设置物品一般属性(pp, row);
							long cdTimes = StringTool.getCellValue(row.getCell(23), Long.class);
							int useTimes = StringTool.getCellValue(row.getCell(24), Integer.class);
							pp.minusCDTimes = cdTimes;
							pp.useTimes = useTimes;
						} catch (Exception e) {
							e.printStackTrace();
							throw e;
						}
						allArticle.add(pp);
						logger.warn("减仙婴附体cd道具] [行：" + i + "] [name:" + pp.getName() + "]");
					}
					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
				} else if (i == 坐骑扩阶道具Sheet) {

					ArrayList<HorseUpProps> allHorseUpProps = new ArrayList<HorseUpProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							HorseUpProps horseUpProps = new HorseUpProps();
							try {
								设置物品一般属性(horseUpProps, row);
								设置一般道具属性((Props) horseUpProps, row);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "坐骑扩阶道具所在Sheet" });
								e.printStackTrace();
								throw e;
							}
							horseUpProps.setCanUseLevel((int) row.getCell(坐骑扩阶道具_canUseLevel).getNumericCellValue());
							horseUpProps.setUpLevel((int) row.getCell(坐骑扩阶道具_upLevel).getNumericCellValue());
							upRankLevelNeedArticle.put((int) row.getCell(坐骑扩阶道具_canUseLevel).getNumericCellValue(), horseUpProps.getName());
							allHorseUpProps.add(horseUpProps);
							allArticle.add(horseUpProps);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allHorseUpProps = allHorseUpProps.toArray(new HorseUpProps[0]);
				}else if (i == 宠物饰品道具Sheet) {
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							PetSuitArticle hunshiProps = new PetSuitArticle();
							try {
								设置物品一般属性(hunshiProps, row);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "宠物饰品道具Sheet" });
								e.printStackTrace();
								throw e;
							}
							//所有物品_掉落NPC形象_列
							int character = (int) row.getCell(所有物品_掉落NPC形象_列+1).getNumericCellValue();
							String eggName = null;
							if (row.getCell(所有物品_掉落NPC形象_列+2) != null) {
								eggName = row.getCell(所有物品_掉落NPC形象_列+2).getStringCellValue();
							}
							String effectType = null;
							try {
								effectType = row.getCell(所有物品_掉落NPC形象_列+3).getStringCellValue();
							} catch (Exception e) {
								effectType = ReadFileTool.getInt(row, 所有物品_掉落NPC形象_列+3, logger) + "";//row.getCell(所有物品_掉落NPC形象_列+3).getNumericCellValue() + "";
							}
							String propType = null;
							if (row.getCell(所有物品_掉落NPC形象_列+4) != null) {
								try {
									propType = row.getCell(所有物品_掉落NPC形象_列+4).getStringCellValue();
								} catch (Exception e) {
									propType = ReadFileTool.getInt(row, 所有物品_掉落NPC形象_列+4, logger) + "";//row.getCell(所有物品_掉落NPC形象_列+4).getNumericCellValue() + "";
								}
							}
							String addType = null;
							if (row.getCell(所有物品_掉落NPC形象_列+5) != null) {
								try {
									addType = row.getCell(所有物品_掉落NPC形象_列+5).getStringCellValue();
								} catch (Exception e) {
									addType = ReadFileTool.getInt(row, 所有物品_掉落NPC形象_列+5, logger) + "";//row.getCell(所有物品_掉落NPC形象_列+5).getStringCellValue();
								}
							}
							String addNum = null;
							try {
								addNum = row.getCell(所有物品_掉落NPC形象_列+6).getStringCellValue();
							} catch (Exception e) {
								addNum = ReadFileTool.getInt(row, 所有物品_掉落NPC形象_列+6, logger) + ""; //row.getCell(所有物品_掉落NPC形象_列+6).getStringCellValue();
							}
							int aa = (int) row.getCell(所有物品_掉落NPC形象_列+7).getNumericCellValue();
							hunshiProps.setOtherVar(character, eggName, effectType, propType, addType, addNum, aa);
							
							allArticle.add(hunshiProps);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
				} else if (i == 坐骑魂石道具Sheet) {

					ArrayList<HunshiProps> allHunshiProps = new ArrayList<HunshiProps>();
					ArrayList<Article> allArticle = new ArrayList<Article>();
					for (int r = 1; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {

							HunshiProps hunshiProps = new HunshiProps();
							try {
								设置物品一般属性(hunshiProps, row);
								设置一般道具属性((Props) hunshiProps, row);
							} catch (Exception e) {
								if (logger.isInfoEnabled()) logger.info("articleinit{}", new Object[] { "坐骑魂石道具Sheet" });
								e.printStackTrace();
								throw e;
							}
							hunshiProps.setType((int) row.getCell(坐骑魂石道具_type).getNumericCellValue());
							hunshiProps.setIndex((int) row.getCell(坐骑魂石道具_index).getNumericCellValue());
							allHunshiProps.add(hunshiProps);
							allArticle.add(hunshiProps);
						}
					}

					if (allArticles != null) {
						for (Article a : allArticles) {
							allArticle.add(a);
						}
					}
					this.allArticles = allArticle.toArray(new Article[0]);
					this.allHunshiProps = allHunshiProps.toArray(new HunshiProps[0]);
				}
				
			}
			for (Article article : this.allArticles) {
				allArticleNameMap.put(article.getName(), article);
				allArticleCNNameMap.put(article.getName_stat(), article);
			}
			if (is != null) {
				is.close();
			}
		}
	}

	public void loadBaoShiXiaZiData() throws Exception {
		baoShiClolors.clear();
		if (baoShiColorFile != null && !baoShiColorFile.isEmpty()) {
			File file = new File(baoShiColorFile);
			if (file.isFile() && file.exists()) {
				InputStream is = new FileInputStream(file);
				POIFSFileSystem pss = new POIFSFileSystem(is);
				HSSFWorkbook workbook = new HSSFWorkbook(pss);
				HSSFSheet sheet = workbook.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows();
				for (int r = 2; r < rows; r++) {
					HSSFRow row = sheet.getRow(r);
					if (row != null) {
						HSSFCell cell = row.getCell(0);
						String baoshiname = cell.getStringCellValue();
						cell = row.getCell(1);
						String colors[] = cell.getStringCellValue().trim().split(",");
						// int cs[] = StringToInt(colors);
						double cs[] = StringToDouble(colors);
						if (baoshiname != null && cs != null) {
							baoShiClolors.put(baoshiname, cs);
						}
						logger.warn("[加载宝石匣子颜色数据] [{}] [颜色概率:{}] [cs:{}] [数量:{}]", new Object[] { baoshiname, cell.getStringCellValue(), (cs == null ? "nul" : Arrays.toString(cs)), baoShiClolors.size() });
					}
				}
				return;
			}
		}
		if (logger.isWarnEnabled()) logger.warn("[配置文件不存在] [{}]", new Object[] { baoShiColorFile });
	}

	private void loadSoulPithArticle(int rows, HSSFSheet sheet) throws Exception {
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			SoulPithArticle r = new SoulPithArticle();
			try {
				设置物品一般属性(r, row);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			// r.setAddExp(ReadFileTool.getLong(row, 法宝_使用等级限制, SoulPithManager.logger, 0));
			allArticle.add(r);
			logger.warn("[加载仙气葫芦道具] [行：" + i + "] [name:" + r.getName() + "] []");
		}
		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	private void loadGourdArticle(int rows, HSSFSheet sheet) throws Exception {
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			GourdArticle r = new GourdArticle();
			try {
				设置物品一般属性(r, row);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			r.setAddExp(ReadFileTool.getLong(row, 法宝_使用等级限制, SoulPithManager.logger, 0));
			allArticle.add(r);
			SoulPithConstant.gourdNames = Arrays.copyOf(SoulPithConstant.gourdNames, SoulPithConstant.gourdNames.length + 1);
			SoulPithConstant.gourdNames[SoulPithConstant.gourdNames.length - 1] = r.getName();
			logger.warn("[加载仙气葫芦道具] [行：" + i + "] [name:" + r.getName() + "] []");
		}
		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	private void loadHuntArticle(int rows, HSSFSheet sheet) throws Exception {
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			HuntLifeArticle r = new HuntLifeArticle();
			try {
				设置物品一般属性(r, row);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			r.setAddAttrType(getInt(row, 法宝_使用等级限制));
			allArticle.add(r);
			logger.warn("[加载兽魂道具] [行：" + i + "] [name:" + r.getName() + "] []");
		}
		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	private void loadEnchant(int rows, HSSFSheet sheet) throws Exception {
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			EnchantArticle r = new EnchantArticle();
			try {
				设置物品一般属性(r, row);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			r.setEnchantId(getInt(row, 法宝_使用等级限制));
			r.setEquipmentType(getInt(row, 法宝_数值等级));
			allArticle.add(r);
			logger.warn("[加载附魔道具] [行：" + i + "] [name:" + r.getName() + "] []");
		}
		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	private void loadMagicWeaponEat(int rows, HSSFSheet sheet) throws Exception {
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			MagicWeaponEatProps r = new MagicWeaponEatProps();
			try {
				设置物品一般属性(r, row);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			r.setEatExp(getInt(row, 法宝_使用等级限制));
			magicweaponEatProps.add(r.getName());
			allArticle.add(r);
			logger.warn("[加载法宝] [行：" + i + "] [name:" + r.getName() + "] []");
		}
		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	/**
	 * 法宝相关配置加载
	 * @param rows
	 * @param sheet
	 * @throws Exception
	 */
	private void loadMagicWeapon(int rows, HSSFSheet sheet) throws Exception {
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			MagicWeapon r = new MagicWeapon();
			try {
				设置物品一般属性(r, row);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			r.setLevellimit(this.getInt(row, 法宝_使用等级限制));
			r.setDataLevel(this.getInt(row, 法宝_数值等级));
			r.setClasslimit(this.getInt(row, 法宝_使用境界限制));
			r.setCareertype(this.getByte(row, 法宝_使用职业限制));
			r.setSoultype(this.getByte(row, 法宝_使用元神限制));
			r.setNaijiudu(this.getInt(row, 法宝_耐久));
			r.setChangeLevel(PetGrade.getString(row, 法宝_进化后法宝名, MagicWeaponManager.logger));
			r.setIllution_icons(PetGrade.getString(row, 法宝_进化后法宝名 + 1, MagicWeaponManager.logger));
			r.setIllution_avatars(PetGrade.getString(row, 法宝_进化后法宝名 + 2, MagicWeaponManager.logger));
			r.setIllution_particle(PetGrade.getString(row, 法宝_进化后法宝名 + 3, MagicWeaponManager.logger));
			r.setNpcId(this.getInt(row, 法宝_进化后法宝名 + 4));
			HSSFCell cell = row.getCell(法宝_进化后法宝名 + 5);
			if (cell != null) {
				r.setObtainBind(cell.getBooleanCellValue());
			} else { // 默认不绑定
				r.setObtainBind(false);
			}
			allArticle.add(r);
			logger.warn("[加载法宝] [行：" + i + "] [name:" + r.getName() + "] []");
		}
		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	private void loadQiLingDan(int rows, HSSFSheet sheet) throws Exception {
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row == null) {
				break;
			}
			QiLingDanArticle a = new QiLingDanArticle();
			try {
				设置物品一般属性(a, row);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			a.setAddHunLiang(getInt(row, 器灵丹道具_value));

			allArticle.add(a);
		}

		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	@SuppressWarnings("rawtypes")
	int getInt(HSSFRow row, int cellIdx) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
			logger.error("单元格是null " + cellIdx + " 行 " + row.getRowNum() + " at " + row.getSheet().getSheetName());
			return 0;
		}
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			return (int) cell.getNumericCellValue();
		}
		if (cell.getStringCellValue().trim().length() > 0) {
			return Integer.parseInt(cell.getStringCellValue());
		}
		return 0;
	}

	byte getByte(HSSFRow row, int cellIdx) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
			logger.error("单元格是null " + cellIdx + " 行 " + row.getRowNum() + " at " + row.getSheet().getSheetName());
			return 0;
		}
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			return (byte) cell.getNumericCellValue();
		}
		if (cell.getStringCellValue().trim().length() > 0) {
			return Byte.parseByte(cell.getStringCellValue());
		}
		return 0;
	}

	private void loadTitleItems(int rows, HSSFSheet sheet) throws Exception {
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row == null) {
				break;
			}
			TitleItemProp a = new TitleItemProp();
			try {
				设置物品一般属性(a, row);
				设置一般道具属性(a, row);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			a.titleName = row.getCell(称号道具_ID).getStringCellValue();
			a.titleName_stat = row.getCell(称号道具统计_ID).getStringCellValue();
			allArticle.add(a);
		}

		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	public void loadEquipmentRule(HSSFSheet sheet) throws Exception {
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 1; r < rows; r++) {
			int index = 0;
			HSSFRow row = sheet.getRow(r);
			if (row != null) {
				UpgradeRule ur = new UpgradeRule();
				try {
					HSSFCell cell = row.getCell(index++);
					String ruleName = "";
					int typename = -1;
					String newEquipmentName_stat;
					String newEquipmentName;
					int newEquipmentColor;
					String materialNames[];
					String materialNames_stat[];
					long costSilver;
					String showMess;

					try {
						ruleName = cell.getStringCellValue();
						ur.setRuleName(ruleName);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("规则名：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						typename = (int) cell.getNumericCellValue();
						ur.setTypename(typename);
					} catch (Exception ex) {
						try {
							typename = Integer.parseInt(cell.getStringCellValue().trim());
							ur.setTypename(typename);
						} catch (Exception e) {
							if (logger.isInfoEnabled()) logger.info("规则类型2：{}", new Object[] { r });
							throw e;
						}
						if (logger.isInfoEnabled()) logger.info("规则类型：{}", new Object[] { r });
						throw ex;
					}

					cell = row.getCell(index++);
					try {
						newEquipmentName = cell.getStringCellValue();
						ur.setNewEquipmentName(newEquipmentName);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("新装备名：{}", new Object[] { r });
						if (typename == 0) {
							throw ex;
						}
					}
					cell = row.getCell(index++);
					try {
						newEquipmentName_stat = cell.getStringCellValue();
						ur.setNewEquipmentName_stat(newEquipmentName_stat);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("新装备统计名：{}", new Object[] { r });
						if (typename == 0) {
							throw ex;
						}
					}
					cell = row.getCell(index++);
					try {
						newEquipmentColor = (int) cell.getNumericCellValue();
						if (newEquipmentColor < 0 || newEquipmentColor > 7) {
							if (logger.isInfoEnabled()) logger.info("新装备颜色不合法：{}{}", new Object[] { r, newEquipmentColor });
							throw new Exception("图纸合成新装备规则颜色不合法:" + newEquipmentColor);
						}
						ur.setNewEquipmentColor(newEquipmentColor);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("新装备颜色：{}", new Object[] { r });
						throw ex;
					}

					cell = row.getCell(index++);
					try {
						materialNames = cell.getStringCellValue().split(",");
						ur.setMaterialNames(materialNames);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("材料集：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						materialNames_stat = cell.getStringCellValue().split(",");
						ur.setMaterialNames_stat(materialNames_stat);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("材料统计集：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						String[] materials = cell.getStringCellValue().split(",");
						ur.setMaterialNum(StringToInt(materials));
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("材料统计数量：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						costSilver = (long) cell.getNumericCellValue();
						ur.setCostSilver(costSilver);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("规则消耗银子：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						showMess = cell.getStringCellValue();
						ur.setShowMess(showMess);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("规则描述：{}", new Object[] { r });
						throw ex;
					}
					if (logger.isInfoEnabled()) logger.info("规则加载成功：行：{},规则：{}", new Object[] { r, ur.toString() });
					rules.add(ur);
				} catch (Exception e) {
					if (logger.isInfoEnabled()) logger.info("规则：{}", new Object[] { r });
					e.printStackTrace();
					throw e;
				}
			}
		}
	}

	public void loadEquipmentTuZhi(HSSFSheet sheet) throws Exception {
		int rows = sheet.getPhysicalNumberOfRows();
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row != null) {
				EquipmentTuZhiProp dp = new EquipmentTuZhiProp();
				try {
					设置物品一般属性(dp, row);
					设置一般道具属性((Props) dp, row);
					HSSFCell cell = row.getCell(仙装升级规则名);
					try {
						String ruleName = cell.getStringCellValue();
						dp.setRuleName(ruleName);
						int count = 0;
						for (UpgradeRule rule : rules) {
							if (rule.getRuleName().equals(ruleName)) {
								count++;
							}
						}
						if (count == 0) {
							throw new Exception("加载装备升级图纸出错，没有与之相匹配的规则名:" + ruleName);
						} else if (count > 1) {
							throw new Exception("加载装备升级图纸出错，规则名出现一对多:" + ruleName);
						}
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("图纸规则名：{}", new Object[] { r });
						throw ex;
					}
				} catch (Exception e) {
					if (logger.isInfoEnabled()) logger.info("图纸规则：{}", new Object[] { r });
					e.printStackTrace();
					throw e;
				}
				allArticle.add(dp);
				if (logger.isInfoEnabled()) logger.info("图纸加载成功：行：{},图纸：{}", new Object[] { r, dp.toString() });
			}
		}
		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	public void loadEquipmentSuit(HSSFSheet sheet) throws Exception {
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 1; r < rows; r++) {
			int index = 0;
			HSSFRow row = sheet.getRow(r);
			if (row != null) {
				NewSuitEquipment ur = new NewSuitEquipment();
				try {
					HSSFCell cell = row.getCell(index++);

					try {
						String suitName = cell.getStringCellValue();
						ur.setSuitName(suitName);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装名：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						String suitName_stat = cell.getStringCellValue();
						ur.setSuitName_stat(suitName_stat);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装统计名：{}", new Object[] { r });
						throw ex;
					}

					cell = row.getCell(index++);
					try {
						String[] suitPart = cell.getStringCellValue().split(",");
						ur.setSuitPart(suitPart);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装部件：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						String[] suitPart_stat = cell.getStringCellValue().split(",");
						ur.setSuitPart_stat(suitPart_stat);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装部件统计：{}", new Object[] { r });
						throw ex;
					}

					cell = row.getCell(index++);
					try {
						String[] results = cell.getStringCellValue().split(",");
						ur.setSuitResult(StringToInt(results));
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装效果数：{}", new Object[] { r });
						throw ex;
					}

					cell = row.getCell(index++);
					try {
						String[] results = cell.getStringCellValue().split(",");
						ur.setQixue(StringToLong(results));
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装气血：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						String[] results = cell.getStringCellValue().split(",");
						ur.setNeigong(StringToLong(results));
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装内功：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						String[] results = cell.getStringCellValue().split(",");
						ur.setWaigong(StringToLong(results));
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装外功：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						String[] results = cell.getStringCellValue().split(",");
						ur.setNeifang(StringToLong(results));
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装内防：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						String[] results = cell.getStringCellValue().split(",");
						ur.setWaifang(StringToLong(results));
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装外防：{}", new Object[] { r });
						throw ex;
					}
					cell = row.getCell(index++);
					try {
						String[] results = cell.getStringCellValue().split(",");
						ur.setMinzhong(StringToLong(results));
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("套装命中：{}", new Object[] { r });
						throw ex;
					}
					if (logger.isInfoEnabled()) logger.info("套装：行：{},套装：{}", new Object[] { r, ur.toString() });
					ur.initValue();
					suits.add(ur);
				} catch (Exception e) {
					if (logger.isInfoEnabled()) logger.info("套装：{}", new Object[] { r });
					e.printStackTrace();
					throw e;
				}
			}
		}
	}

	public void loadPetWuXingProp(HSSFSheet sheet) throws Exception {
		int rows = sheet.getPhysicalNumberOfRows();
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row != null) {
				PetWuXingProp dp = new PetWuXingProp();
				try {
					设置物品一般属性(dp, row);
					设置一般道具属性((Props) dp, row);
					HSSFCell cell = row.getCell(增加的悟性丹值);
					try {
						String wuxingstr = cell.getStringCellValue();
						int[] ws = StringToInt(wuxingstr.split(","));
						dp.setWuxings(ws);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("宠物悟性道具加载悟性值：{}", new Object[] { r });
						throw ex;
					}
					try {
						cell = row.getCell(增加的悟性丹几率);
						String flopstr = cell.getStringCellValue();
						int[] fs = StringToInt(flopstr.split(","));
						dp.setJilvs(fs);
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("宠物悟性道具加载悟性几率：{}", new Object[] { r });
						throw ex;
					}
					try {
						cell = row.getCell(可用丹的宠物名);
						String names = cell.getStringCellValue();
						dp.setPetnames(names.split(","));
					} catch (Exception ex) {
						if (logger.isInfoEnabled()) logger.info("宠物悟性道具加载悟性可用丹的宠物名：{}", new Object[] { r });
						throw ex;
					}
				} catch (Exception e) {
					if (logger.isInfoEnabled()) logger.info("宠物悟性道具加载行：{}", new Object[] { r });
					e.printStackTrace();
					throw e;
				}
				allArticle.add(dp);
			}
		}
		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	private int[] StringToInt(String[] a) {
		int[] fs = new int[a.length];
		for (int k = 0; k < a.length; k++) {
			fs[k] = Integer.parseInt(a[k]);
		}
		return fs;
	}

	private double[] StringToDouble(String[] a) {
		double[] fs = new double[a.length];
		for (int k = 0; k < a.length; k++) {
			fs[k] = Double.parseDouble(a[k]);
		}
		return fs;
	}

	private long[] StringToLong(String[] a) {
		long[] fs = new long[a.length];
		for (int k = 0; k < a.length; k++) {
			fs[k] = Long.parseLong(a[k]);
		}
		return fs;
	}

	private void loadRobberyProps(int rows, HSSFSheet sheet) throws Exception {
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			RobberyProps r = new RobberyProps();
			try {
				设置物品一般属性(r, row);
				设置一般道具属性(r, row);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			r.setLiziguangxiao(PetGrade.getString(row, liziguangxiao, TransitRobberyManager.logger));
			r.setBuffName(PetGrade.getString(row, buffname, TransitRobberyManager.logger));
			r.setInvalidTime(getInt(row, invalidtime));
			allArticle.add(r);
		}
		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	private void loadPetSkillBooks(int rows, HSSFSheet sheet) throws Exception {
		ArrayList<Article> allArticle = new ArrayList<Article>();
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row == null) {
				break;
			}
			PetSkillProp a = new PetSkillProp();
			try {
				设置物品一般属性(a, row);
				设置一般道具属性(a, row);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			a.skillId = getInt(row, 技能书_技能id);
			a.skillLv = getInt(row, 技能书_技能等级);
			a.cateKey = PetGrade.getString(row, 技能书_技能分类, logger);
			a.xingGe = PetGrade.getString(row, 技能书_性格, logger);
			allArticle.add(a);
		}
		Pet2SkillCalc.getInst().setAllBooks(allArticle);
		if (allArticles != null) {
			for (Article a : allArticles) {
				allArticle.add(a);
			}
		}
		this.allArticles = allArticle.toArray(new Article[0]);
	}

	public void 设置物品一般属性(Article a, HSSFRow row) throws Exception {
		lastProcRow = row;
		HSSFCell cell = row.getCell(所有物品_物品名_列);
		String name = "";
		name = (cell.getStringCellValue().trim());
		String Nname = name;
		if (name.trim().equals("")) {
			throw new Exception("有空名字物品" + row.getSheet().getSheetName() + row.getRowNum() + "行有错[" + Nname + "]");
		}
		a.setName(name);

		cell = row.getCell(所有物品_物品名_列_STAT);
		String name_stat = "";
		name_stat = (cell.getStringCellValue().trim());
		String Nname_stat = name_stat;
		if (name_stat.trim().equals("")) {
			throw new Exception("有空名字物品" + row.getSheet().getSheetName() + row.getRowNum() + "行有错[" + Nname_stat + "]");
		}
		a.setName_stat(name_stat);

		if (logger.isInfoEnabled()) logger.info("[系统读取文件到] [" + a.getName() + "]");
		cell = row.getCell(所有物品_图标_列);
		String 图标 = "";
		try {
			图标 = cell.getStringCellValue().trim();
			a.setIconId(图标);
		} catch (Exception ex) {
			图标 = (int) cell.getNumericCellValue() + "";
			a.setIconId(图标);
		}
		cell = row.getCell(所有物品_放入哪个包裹_列);
		int 包裹 = -1;
		try {
			包裹 = (int) cell.getNumericCellValue();
			a.setKnapsackType(包裹);
		} catch (Exception ex) {
			try {
				包裹 = Integer.parseInt(cell.getStringCellValue().trim());
				a.setKnapsackType(包裹);
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有物品_绑定方式_列);
		int 绑定方式 = 0;
		try {
			绑定方式 = (int) cell.getNumericCellValue();
			a.setBindStyle((byte) 绑定方式);
		} catch (Exception ex) {
			try {
				绑定方式 = Integer.parseInt(cell.getStringCellValue().trim());
				a.setBindStyle((byte) 绑定方式);
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有物品_物品数值等级_列);
		int 物品数值等级 = 0;
		try {
			物品数值等级 = (int) cell.getNumericCellValue();
			a.setArticleLevel(物品数值等级);
		} catch (Exception ex) {
			try {
				物品数值等级 = Integer.parseInt(cell.getStringCellValue().trim());
				a.setArticleLevel(物品数值等级);
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有物品_物品颜色_列);
		int 物品颜色 = 0;
		try {
			物品颜色 = (int) cell.getNumericCellValue();
			a.setColorType(物品颜色);
		} catch (Exception ex) {
			try {
				物品颜色 = Integer.parseInt(cell.getStringCellValue().trim());
				a.setColorType(物品颜色);
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有物品_可否重叠_列);
		boolean 可否重叠 = false;
		try {
			可否重叠 = cell.getBooleanCellValue();
			a.setOverlap(可否重叠);
		} catch (Exception ex) {

		}
		cell = row.getCell(所有物品_重叠最大数量_列);
		int 重叠最大数量 = 0;
		try {
			重叠最大数量 = (int) cell.getNumericCellValue();
			a.setOverLapNum(重叠最大数量);
		} catch (Exception ex) {
			try {
				重叠最大数量 = Integer.parseInt(cell.getStringCellValue().trim());
				a.setOverLapNum(重叠最大数量);
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有物品_物品一级分类_列);
		String 物品一级分类 = "";
		try {
			物品一级分类 = (cell.getStringCellValue().trim());
			a.set物品一级分类(物品一级分类);
		} catch (Exception ex) {
			// throw ex;
		}

		cell = row.getCell(所有物品_物品一级分类_列_STAT);
		String 物品一级分类_stat = "";
		try {
			物品一级分类_stat = (cell.getStringCellValue().trim());
			a.set物品一级分类_stat(物品一级分类_stat);
		} catch (Exception ex) {
			// throw ex;
		}

		cell = row.getCell(所有物品_物品二级分类_列);
		String 物品二级分类 = "";
		try {
			物品二级分类 = (cell.getStringCellValue().trim());
			a.set物品二级分类(物品二级分类);
		} catch (Exception ex) {
			// throw ex;
		}

		cell = row.getCell(所有物品_物品二级分类_列_STAT);
		String 物品二级分类_stat = "";
		try {
			物品二级分类_stat = (cell.getStringCellValue().trim());
			a.set物品二级分类_stat(物品二级分类_stat);
		} catch (Exception ex) {
			// throw ex;
		}

		cell = row.getCell(所有物品_故事_列);
		String 故事 = null;
		try {
			故事 = (cell.getStringCellValue().trim());
			// 故事 = cell.getStringCellValue().trim();
			a.setStroy(故事);
		} catch (Exception ex) {

		}
		cell = row.getCell(所有物品_描述_列);
		String 描述 = null;
		try {
			描述 = (cell.getStringCellValue().trim());
			a.setDescription(描述);
		} catch (Exception ex) {

		}
		cell = row.getCell(所有物品_使用方式_列);
		String 使用方式 = null;
		try {
			使用方式 = (cell.getStringCellValue().trim());
			a.setUseMethod(使用方式);
		} catch (Exception ex) {

		}
		cell = row.getCell(所有物品_获取方式_列);
		String 获取方式 = null;
		try {
			获取方式 = (cell.getStringCellValue().trim());
			a.setGetMethod(获取方式);
		} catch (Exception ex) {

		}
		cell = row.getCell(所有物品_有无有效期_列);
		boolean 有无有效期 = false;
		try {
			有无有效期 = cell.getBooleanCellValue();
			a.setHaveValidDays(有无有效期);
		} catch (Exception ex) {

		}
		cell = row.getCell(所有物品_激活方式_列);
		int 激活方式 = -1;
		try {
			激活方式 = (int) cell.getNumericCellValue();
			a.setActivationOption((byte) 激活方式);
		} catch (Exception ex) {
			try {
				激活方式 = Integer.parseInt(cell.getStringCellValue().trim());
				a.setActivationOption((byte) 激活方式);
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有物品_最大有效期_列);
		int 最大有效期 = -1;
		try {
			最大有效期 = (int) cell.getNumericCellValue();
			a.setMaxValidDays(最大有效期);
		} catch (Exception ex) {
			try {
				最大有效期 = Integer.parseInt(cell.getStringCellValue().trim());
				a.setMaxValidDays(最大有效期);
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有物品_是否可卖_列);
		boolean 是否可卖 = true;
		try {
			是否可卖 = cell.getBooleanCellValue();
			a.setSailFlag(是否可卖);
		} catch (Exception ex) {

		}
		cell = row.getCell(所有物品_卖价_列);
		int 卖价 = 0;
		try {
			卖价 = (int) cell.getNumericCellValue();
			a.setPrice(卖价);
		} catch (Exception ex) {
			try {
				卖价 = Integer.parseInt(cell.getStringCellValue().trim());
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有物品_掉落NPC形象_列);
		try {
			String avata = cell.getStringCellValue().trim();
			a.setFlopNPCAvata(avata);
		} catch (Exception ex) {

		}
	}

	public static void 设置一般道具属性(Props a, HSSFRow row) throws Exception {
		HSSFCell cell = row.getCell(所有道具_道具类分类类名_列);
		String 道具类分类类名 = "";
		try {
			道具类分类类名 = cell.getStringCellValue().trim();
			a.setCategoryName(道具类分类类名);
		} catch (Exception ex) {
			throw ex;
		}
		cell = row.getCell(所有道具_是否有使用次数限制_列);
		boolean 是否有使用次数限制 = false;
		try {
			是否有使用次数限制 = cell.getBooleanCellValue();
			a.setUsingTimesLimit(是否有使用次数限制);
		} catch (Exception ex) {

		}
		cell = row.getCell(所有道具_使用次数限制_列);
		int 使用次数限制 = -1;
		try {
			使用次数限制 = (int) cell.getNumericCellValue();
			a.setMaxUsingTimes(使用次数限制);
		} catch (Exception ex) {
			try {
				使用次数限制 = Integer.parseInt(cell.getStringCellValue().trim());
				a.setMaxUsingTimes(使用次数限制);
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有道具_使用后不消失_列);
		boolean 使用后不消失 = false;
		try {
			使用后不消失 = cell.getBooleanCellValue();
			a.setUsedUndisappear(使用后不消失);
		} catch (Exception ex) {

		}
		cell = row.getCell(所有道具_玩家等级限制_列);
		int 玩家等级限制 = 0;
		try {
			玩家等级限制 = (int) cell.getNumericCellValue();
			a.setLevelLimit(玩家等级限制);
		} catch (Exception ex) {
			try {
				玩家等级限制 = Integer.parseInt(cell.getStringCellValue().trim());
				a.setLevelLimit(玩家等级限制);
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有道具_玩家境界限制_列);
		int 玩家境界限制 = 0;
		try {
			玩家境界限制 = (int) cell.getNumericCellValue();
			a.setClassLimit(玩家境界限制);
		} catch (Exception ex) {
			try {
				玩家境界限制 = Integer.parseInt(cell.getStringCellValue().trim());
				a.setClassLimit(玩家境界限制);
			} catch (Exception e) {

			}
		}
		cell = row.getCell(所有道具_战斗状态限制_列);
		boolean 战斗状态限制 = false;
		try {
			战斗状态限制 = cell.getBooleanCellValue();
			a.setFightStateLimit(战斗状态限制);
		} catch (Exception ex) {

		}
	}

	public static int 设置宝箱道具属性(PackageProps a, HSSFSheet sheet, int start, int max) throws Exception {
		int num = start;
		ArrayList<String> articleNames = new ArrayList<String>();
		ArrayList<String> articleNames_stat = new ArrayList<String>();
		ArrayList<Integer> counts = new ArrayList<Integer>();
		ArrayList<Integer> articleColors = new ArrayList<Integer>();
		for (; num < max; num++) {
			HSSFRow row = sheet.getRow(num);
			try {
				HSSFCell cell = row.getCell(所有物品_物品名_列);
				String name = cell.getStringCellValue();
				if (name == null) {
					name = "";
				}
				name = name.trim();
				if (!name.equals("") && !name.equals(a.getName())) {
					ArticleProperty[] aps = new ArticleProperty[articleNames.size()];
					for (int j = 0; j < aps.length; j++) {
						aps[j] = new ArticleProperty();
						aps[j].setArticleName(articleNames.get(j));
						aps[j].setCount(counts.get(j));
						aps[j].setColor(articleColors.get(j));
					}
					a.setArticleNames(aps);
					break;
				} else if (!name.equals("")) {
					cell = row.getCell(宝箱道具_物品绑定标记);
					byte bindType = (byte) cell.getNumericCellValue();
					a.setOpenBindType(bindType);
					cell = row.getCell(宝箱道具_广播);
					int sendMessageArticles = (int) cell.getNumericCellValue();
					a.setNeedSendNotice(1 == sendMessageArticles);
					logger.warn("需要广播:[" + name + "]" + a.getName() + ":" + sendMessageArticles);
				}
			} catch (Exception ex) {
			}

			HSSFCell cell = row.getCell(宝箱道具_包裹中的物品名字);
			String articleName = (cell.getStringCellValue().trim());
			articleNames.add(articleName);

			cell = row.getCell(宝箱道具_包裹中的物品名字_STAT);
			String articleName_stat = (cell.getStringCellValue().trim());
			articleNames_stat.add(articleName_stat);

			cell = row.getCell(宝箱道具_包裹中的物品数量);
			int count = (int) cell.getNumericCellValue();
			counts.add(count);

			cell = row.getCell(宝箱道具_包裹中的物品颜色);
			int articleColor = (int) cell.getNumericCellValue();
			articleColors.add(articleColor);

			if (logger.isInfoEnabled()) logger.info("[" + a.getName() + "]宝箱道具_包裹中的物品名字" + articleName + " " + count + " " + articleColor);
		}
		if (num == max) {
			ArticleProperty[] aps = new ArticleProperty[articleNames.size()];
			for (int j = 0; j < aps.length; j++) {
				aps[j] = new ArticleProperty();
				aps[j].setArticleName(articleNames.get(j));
				aps[j].setCount(counts.get(j));
				aps[j].setColor(articleColors.get(j));
			}
			a.setArticleNames(aps);

			ArticleProperty[] aps_stat = new ArticleProperty[articleNames_stat.size()];
			for (int j = 0; j < aps_stat.length; j++) {
				aps_stat[j] = new ArticleProperty();
				aps_stat[j].setArticleName(articleNames_stat.get(j));
				aps_stat[j].setCount(counts.get(j));
				aps_stat[j].setColor(articleColors.get(j));
			}
			a.setArticleNames_stat(aps_stat);
		}
		return num - 1;
	}

	public static int 设置随机宝箱道具属性(RandomPackageProps a, HSSFSheet sheet, int start, int max) throws Exception {
		int num = start;
		ArrayList<String> articleNames = new ArrayList<String>();
		ArrayList<String> articleNames_stat = new ArrayList<String>();
		ArrayList<Integer> probs = new ArrayList<Integer>();
		ArrayList<Integer> counts = new ArrayList<Integer>();
		ArrayList<Integer> articleColors = new ArrayList<Integer>();
		for (; num < max; num++) {
			HSSFRow row = sheet.getRow(num);
			try {
				HSSFCell cell = row.getCell(所有物品_物品名_列);
				String name = (cell.getStringCellValue().trim());
				if (!name.isEmpty() && !name.equals(a.getName())) {
					// if (!name.equals(a.getName())) {
					ArticleProperty[] aps = new ArticleProperty[articleNames.size()];
					for (int j = 0; j < aps.length; j++) {
						aps[j] = new ArticleProperty();
						aps[j].setArticleName(articleNames.get(j));
						if (articleNames_stat.size() > j) {
							aps[j].setArticleName_stat(articleNames_stat.get(j));
						}
						aps[j].setProb(probs.get(j));
						aps[j].setCount(counts.get(j));
						aps[j].setColor(articleColors.get(j));
					}
					a.setApps(aps);

					ArticleProperty[] aps_stat = new ArticleProperty[articleNames_stat.size()];
					for (int j = 0; j < aps_stat.length; j++) {
						aps_stat[j] = new ArticleProperty();
						aps_stat[j].setArticleName(articleNames_stat.get(j));
						aps_stat[j].setArticleName_stat(articleNames_stat.get(j));
						aps_stat[j].setProb(probs.get(j));
						aps_stat[j].setCount(counts.get(j));
						aps_stat[j].setColor(articleColors.get(j));
					}
					a.setApps_stat(aps_stat);
					break;
				} else {
					try {
						byte bindType = 0 ;
						if (a.getOpenBindType() == 0) {
							cell = row.getCell(随机宝箱道具_物品绑定标记);
							bindType = (byte) cell.getNumericCellValue();
							a.setOpenBindType(bindType);
						}
						if (a.getOpenBindType() != 0) {
							logger.info("["+a.getName_stat()+"] [" + a.getOpenBindType() + "] [name:"+name+"] [bindType:" + bindType + "]");
						}
						/*cell = row.getCell(随机宝箱道具_物品绑定标记);
						byte bindType = (byte) cell.getNumericCellValue();
						a.setOpenBindType(bindType);*/

						cell = row.getCell(随机宝箱道具_包裹中的物品广播);
						String str = cell.getStringCellValue().trim();
						a.setSendMessageArticles(str);

						cell = row.getCell(随机宝箱道具_包裹中的物品广播_STAT);
						String str_stat = cell.getStringCellValue().trim();
						a.setSendMessageArticles_stat(str_stat);

						cell = row.getCell(随机宝箱道具_消耗的道具名);
						String costname = cell.getStringCellValue().trim();
						a.setCostName(costname);

						cell = row.getCell(随机宝箱道具_消耗的道具名_STAT);
						String costname_stat = cell.getStringCellValue().trim();
						a.setCostName_stat(costname_stat);

						cell = row.getCell(随机宝箱道具_消耗的道具颜色);
						int costcolor = (int) cell.getNumericCellValue();
						a.setCostColor(costcolor);

						cell = row.getCell(随机宝箱道具_消耗的道具数量);
						int costnum = (int) cell.getNumericCellValue();
						a.setCostNum(costnum);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {

						cell = row.getCell(随机宝箱道具_使用时播放哪个粒子);
						String particleName = cell.getStringCellValue().trim();
						a.setParticleName(particleName);

						cell = row.getCell(随机宝箱道具_播放粒子时长);
						long delayTime = (long) cell.getNumericCellValue();
						a.setDelayTime(delayTime);

						cell = row.getCell(随机宝箱道具_播放位置上);
						double upValue = cell.getNumericCellValue();
						a.setUpValue(upValue);

						cell = row.getCell(随机宝箱道具_播放位置左);
						double leftValue = cell.getNumericCellValue();
						a.setLeftValue(leftValue);

						cell = row.getCell(随机宝箱道具_播放位置前);
						double frontValue = cell.getNumericCellValue();
						a.setFrontValue(frontValue);

						cell = row.getCell(随机宝箱道具_光效可见类型);
						byte canseeType = (byte) cell.getNumericCellValue();
						a.setCanseeType(canseeType);

						cell = row.getCell(随机宝箱道具_世界广播);
						String broadcast = cell.getStringCellValue().trim();
						a.setBroadcast(broadcast);
						logger.warn("[测试======] [" + a.getName() + "] [" + a.getParticleName() + "]");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			HSSFCell cell = row.getCell(随机宝箱道具_包裹中的物品名字);
			String articleName = (cell.getStringCellValue().trim());
			articleNames.add(articleName);

			cell = row.getCell(随机宝箱道具_包裹中的物品名字_STAT);
			String articleName_stat = (cell.getStringCellValue().trim());
			articleNames_stat.add(articleName_stat);

			cell = row.getCell(随机宝箱道具_包裹中的物品几率);
			int prob = (int) cell.getNumericCellValue();
			probs.add(prob);

			cell = row.getCell(随机宝箱道具_包裹中的物品数量);
			int count = (int) cell.getNumericCellValue();
			counts.add(count);

			cell = row.getCell(随机宝箱道具_包裹中的物品颜色);
			int articleColor = (int) cell.getNumericCellValue();
			articleColors.add(articleColor);
		}
		if (num == max) {
			ArticleProperty[] aps = new ArticleProperty[articleNames.size()];
			for (int j = 0; j < aps.length; j++) {
				aps[j] = new ArticleProperty();
				aps[j].setArticleName(articleNames.get(j));
				if (articleNames_stat.size() > j) {
					aps[j].setArticleName_stat(articleNames_stat.get(j));
				}
				aps[j].setProb(probs.get(j));
				aps[j].setCount(counts.get(j));
				aps[j].setColor(articleColors.get(j));
			}
			a.setApps(aps);

			ArticleProperty[] aps_stat = new ArticleProperty[articleNames_stat.size()];
			for (int j = 0; j < aps_stat.length; j++) {
				aps_stat[j] = new ArticleProperty();
				aps_stat[j].setArticleName(articleNames_stat.get(j));
				aps_stat[j].setProb(probs.get(j));
				aps_stat[j].setCount(counts.get(j));
				aps_stat[j].setColor(articleColors.get(j));
			}
			a.setApps_stat(aps_stat);
		}
		return num - 1;
	}

	public static int 设置古董道具属性(BottleProps a, HSSFSheet sheet, int start, int max) throws Exception {
		int num = start;

		int 数组长度 = CliffordManager.数组长度;
		ArticleProperty[][] 开启获得物品概率配置 = new ArticleProperty[数组长度][];
		for (int i = 0; i < 数组长度; i++) {
			开启获得物品概率配置[i] = new ArticleProperty[max - 1];
		}
		for (; num < max; num++) {
			HSSFRow row = sheet.getRow(num);
			try {
				// HSSFCell cell = row.getCell(所有物品_物品名_列);
				// String name = cell.getStringCellValue().trim();
				// if(!name.equals(a.getName())){
				// break;
				// }
			} catch (Exception ex) {

			}

			for (int i = 0; i < 数组长度; i++) {
				HSSFCell cell = row.getCell(i * 6 + 古董道具_包裹中的物品名字);
				String articleName = null;
				try {
					articleName = cell.getStringCellValue().trim();
				} catch (Exception ex) {
					continue;
				}
				开启获得物品概率配置[i][num - start] = new ArticleProperty();
				开启获得物品概率配置[i][num - start].setArticleName(articleName);

				cell = row.getCell(i * 6 + 古董道具_包裹中的物品名字_STAT);
				String articleName_stat = null;
				try {
					articleName_stat = cell.getStringCellValue().trim();
				} catch (Exception ex) {
					continue;
				}
				// 开启获得物品概率配置[i][num - start] = new ArticleProperty();
				开启获得物品概率配置[i][num - start].setArticleName_stat(articleName_stat);

				cell = row.getCell(i * 6 + 古董道具_掉落几率值);
				int getProb = StringTool.getCellValue(cell, int.class);
				// int getProb = (int) cell.getNumericCellValue();
				开启获得物品概率配置[i][num - start].setProb(getProb);

				cell = row.getCell(i * 6 + 古董道具_物品绑定标记);
				boolean binded = cell.getBooleanCellValue();
				开启获得物品概率配置[i][num - start].setBinded(binded);

				cell = row.getCell(i * 6 + 古董道具_包裹中的物品颜色);
				int color = (int) cell.getNumericCellValue();
				开启获得物品概率配置[i][num - start].setColor(color);

				cell = row.getCell(i * 6 + 古董道具_物品的个数);
				int count = (int) cell.getNumericCellValue();
				开启获得物品概率配置[i][num - start].setCount(count);
			}

		}
		a.setArticles(开启获得物品概率配置);
		return num - 1;
	}

	public static void 设置buff道具属性(LastingProps a, HSSFRow row) throws Exception {
		HSSFCell cell = row.getCell(buff道具_种植类型);
		int 种植类型 = 0;
		try {
			种植类型 = (int) cell.getNumericCellValue();
			a.setPlantType(种植类型);
		} catch (Exception ex) {

		}
		cell = row.getCell(buff道具_范围);
		int 范围 = 0;
		try {
			范围 = (int) cell.getNumericCellValue();
			a.setRange(范围);
		} catch (Exception ex) {

		}
		cell = row.getCell(buff道具_buff的名称);
		String buff的名称 = "";
		try {
			buff的名称 = cell.getStringCellValue().trim();
			a.setBuffName(buff的名称);
		} catch (Exception ex) {
			throw ex;
		}

		cell = row.getCell(buff道具_buff的名称_STAT);
		String buff的名称_stat = "";
		try {
			buff的名称_stat = cell.getStringCellValue().trim();
			a.setBuffName_stat(buff的名称_stat);
		} catch (Exception ex) {
			throw ex;
		}

		cell = row.getCell(buff道具_buff级别);
		int buff级别 = 1;
		try {
			buff级别 = (int) cell.getNumericCellValue();
		} catch (Exception ex) {

		}
		a.setBuffLevel(buff级别);
		cell = row.getCell(buff道具_buff持续的时间);
		long buff持续的时间 = 0;
		try {
			buff持续的时间 = (long) cell.getNumericCellValue();
			a.setBuffLastingTime(buff持续的时间);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static int 设置职业宝箱类道具属性(CareerPackageProps a, HSSFSheet sheet, int start, int max) throws Exception {
		int num = start;
		int 数组长度 = 5;
		ArticleProperty[][] 开启获得物品概率配置 = new ArticleProperty[数组长度][];
		ArrayList<ArticleProperty> a0 = new ArrayList<ArticleProperty>();
		ArrayList<ArticleProperty> a1 = new ArrayList<ArticleProperty>();
		ArrayList<ArticleProperty> a2 = new ArrayList<ArticleProperty>();
		ArrayList<ArticleProperty> a3 = new ArrayList<ArticleProperty>();
		ArrayList<ArticleProperty> a4 = new ArrayList<ArticleProperty>();
		{
			HSSFRow row = null;
			HSSFCell cell = null;
			try {
				row = sheet.getRow(start);
				cell = row.getCell(职业包裹类道具_广播);
				String openNotice = (cell.getStringCellValue().trim());
				a.setOpenNotice(openNotice);
			} catch (Exception e) {
			}

			try {
				row = sheet.getRow(start);
				cell = row.getCell(职业包裹类道具_广播_STAT);
				String openNotice_stat = (cell.getStringCellValue().trim());
				a.setOpenNotice_stat(openNotice_stat);
			} catch (Exception e) {
			}

			try {
				cell = row.getCell(职业包裹类道具_广播类型);
				int noticeType = (int) cell.getNumericCellValue();
				a.setNoticeType(noticeType);
			} catch (Exception e) {

			}

		}

		for (; num < max; num++) {
			HSSFRow row = sheet.getRow(num);
			try {
				HSSFCell cell = row.getCell(所有物品_物品名_列);
				String name = (cell.getStringCellValue().trim());
				if (!name.trim().equals("") && !name.equals(a.getName())) {
					开启获得物品概率配置[0] = a0.toArray(new ArticleProperty[0]);
					开启获得物品概率配置[1] = a1.toArray(new ArticleProperty[0]);
					开启获得物品概率配置[2] = a2.toArray(new ArticleProperty[0]);
					开启获得物品概率配置[3] = a3.toArray(new ArticleProperty[0]);
					开启获得物品概率配置[4] = a4.toArray(new ArticleProperty[0]);
					a.setArticleNames(开启获得物品概率配置);
					break;
				} else {
					cell = row.getCell(宝箱道具_物品绑定标记);
					byte bindType = (byte) cell.getNumericCellValue();
					a.setOpenBindType(bindType);
				}

			} catch (Exception ex) {

			}

			/**
			 * 查询是否需要广播
			 */
			boolean isnotice = false;
			try {
				HSSFCell cell = row.getCell(职业包裹类道具_是否广播);
				isnotice = cell.getBooleanCellValue();
			} catch (Exception e) {

			}

			for (int i = 0; i < 数组长度; i++) {
				HSSFCell cell = row.getCell(i * 4 + 职业包裹类道具_包裹中的物品名字);// 28,31,34,37
				String articleName = null;
				try {
					articleName = (cell.getStringCellValue().trim());
				} catch (Exception ex) {
					continue;
				}

				cell = row.getCell(i * 4 + 职业包裹类道具_包裹中的物品名字_STAT);// 28,31,34,37
				String articleName_stat = null;
				try {
					articleName_stat = (cell.getStringCellValue().trim());
				} catch (Exception ex) {
					continue;
				}

				ArticleProperty ap = new ArticleProperty();
				//
				if (isnotice) {
					ap.setNotice(isnotice);
				}

				ap.setArticleName(articleName);
				ap.setArticleName_stat(articleName_stat);
				cell = row.getCell(i * 4 + 职业包裹类道具_包裹中的物品颜色);
				try {
					int color = (int) cell.getNumericCellValue();
					ap.setColor(color);
				} catch (Exception ex) {
					int color = Integer.parseInt(cell.getStringCellValue());
					ap.setColor(color);
				}

				cell = row.getCell(i * 4 + 职业包裹类道具_物品的个数);
				try {
					int count = (int) cell.getNumericCellValue();
					ap.setCount(count);
				} catch (Exception ex) {
					int count = Integer.parseInt(cell.getStringCellValue());
					ap.setCount(count);
				}
				if (i == 0) {
					a0.add(ap);
				} else if (i == 1) {
					a1.add(ap);
				} else if (i == 2) {
					a2.add(ap);
				} else if (i == 3) {
					a3.add(ap);
				} else if (i == 4) {
					a4.add(ap);
				}

			}

		}
		if (num == max) {
			开启获得物品概率配置[0] = a0.toArray(new ArticleProperty[0]);
			开启获得物品概率配置[1] = a1.toArray(new ArticleProperty[0]);
			开启获得物品概率配置[2] = a2.toArray(new ArticleProperty[0]);
			开启获得物品概率配置[3] = a3.toArray(new ArticleProperty[0]);
			开启获得物品概率配置[4] = a4.toArray(new ArticleProperty[0]);
			a.setArticleNames(开启获得物品概率配置);
		}
		return num - 1;
	}

	public static void 设置buff道具属性(Lasting_For_Compose_Props a, HSSFRow row) throws Exception {
		HSSFCell cell = row.getCell(buff_可合成_道具_buff的名称);
		String buff的名称 = "";
		try {
			buff的名称 = cell.getStringCellValue().trim();
			a.setBuffName(buff的名称);
		} catch (Exception ex) {
			throw ex;
		}

		cell = row.getCell(buff_可合成_道具_buff的名称_STAT);
		String buff的名称_stat = "";
		try {
			buff的名称_stat = cell.getStringCellValue().trim();
			a.setBuffName_stat(buff的名称_stat);
		} catch (Exception ex) {
			throw ex;
		}

		cell = row.getCell(buff_可合成_道具_buff级别);
		int buff级别 = 1;
		try {
			buff级别 = (int) cell.getNumericCellValue();
		} catch (Exception ex) {

		}
		a.setBuffLevel(buff级别);
		cell = row.getCell(buff_可合成_道具_buff持续的时间);
		long buff持续的时间 = 0;
		try {
			buff持续的时间 = (long) cell.getNumericCellValue();
			a.setBuffLastingTime(buff持续的时间);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static void 设置buff道具属性(LastingForPetProps a, HSSFRow row) throws Exception {
		HSSFCell cell = row.getCell(buff道具_种植类型);
		int 种植类型 = 0;
		try {
			种植类型 = (int) cell.getNumericCellValue();
			a.setPlantType(种植类型);
		} catch (Exception ex) {

		}
		cell = row.getCell(buff道具_范围);
		int 范围 = 0;
		try {
			范围 = (int) cell.getNumericCellValue();
			a.setRange(范围);
		} catch (Exception ex) {

		}
		cell = row.getCell(buff道具_buff的名称);
		String buff的名称 = "";
		try {
			buff的名称 = cell.getStringCellValue().trim();
			a.setBuffName(buff的名称);
		} catch (Exception ex) {
			throw ex;
		}
		cell = row.getCell(buff道具_buff级别);
		int buff级别 = 1;
		try {
			buff级别 = (int) cell.getNumericCellValue();
		} catch (Exception ex) {

		}
		a.setBuffLevel(buff级别);
		cell = row.getCell(buff道具_buff持续的时间);
		long buff持续的时间 = 0;
		try {
			buff持续的时间 = (long) cell.getNumericCellValue();
			a.setBuffLastingTime(buff持续的时间);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static String getCellContant(HSSFCell cell) {
		if (cell != null) {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				return "";
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
				return "" + cell.getBooleanCellValue();
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
				return "" + cell.getErrorCellValue();
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
				return cell.getCellFormula();
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				return "" + (int) cell.getNumericCellValue();
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				String str = cell.getStringCellValue().trim();
				if (str != null) {
					return str.trim();
				}
			}
		}
		return "";
	}

	public static int[] 鉴定几率 = null;

	static {
		if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			鉴定几率 = new int[] { 15, 35, 75, 95, 100 };
		} else {
			鉴定几率 = new int[] { 20, 45, 75, 90, 100 };
		}
	}

	private int getEndowments() {
		int tempRst = random.nextInt(100);
		int index = 1;
		int low = 0;
		for (int i = 0; i < 鉴定几率.length; i++) {
			if (tempRst >= low && tempRst < 鉴定几率[i]) {
				index = i + 1;
				break;
			}
			low = 鉴定几率[i];
		}
		return index;
		// int result = 1;
		// result = random.nextInt(5) + 1;
		// return result;
	}

	public PropsCategory[] getAllPropsCategory() {

		return this.propsCategoryMap.values().toArray(new PropsCategory[0]);
	}

	public PropsCategory getPropsCategoryByCategoryName(String propsCategoryName) {

		PropsCategory pc = (PropsCategory) this.propsCategoryMap.get(propsCategoryName);

		return pc;
	}

	public ReadEquipmentExcelManager getReem() {
		return reem;
	}

	public void setReem(ReadEquipmentExcelManager reem) {
		this.reem = reem;
	}

	public long getEquipmentEntityRepairPrice(Player player, EquipmentEntity ee) {
		Article a = this.getArticle(ee.getArticleName());
		if (a instanceof Equipment) {
			int allRepairPrice = (a.getColorType() + 1) * a.getPrice() * 40;
			int onePointPrice = allRepairPrice / ((Equipment) a).getDurability();
			if (onePointPrice <= 0) {
				onePointPrice = 1;
			}
			return (int) (onePointPrice * 1l * (((Equipment) a).getDurability() - ee.getDurability()) * Player.得到玩家修理装备时的pk惩罚百分比(player));
		} else {
			return 1000000000;
		}
	}

	/**
	 * 根据articleName今天可以使用的次数 (-1)不是使用道具
	 * [0]=物品最大使用次数
	 * [1]=已经使用次数
	 * @param player
	 * @param articleName
	 * @return
	 */
	public int[] getUseNumsByArticleName(Player player, String articleName) {
		int[] returns = new int[2];
		int useNum = -1;
		Article article = ArticleManager.getInstance().getArticle(articleName);
		if (article != null && article instanceof Props) {
			Props p = (Props) article;
			returns[0] = p.getMaxUsingTimes();
			Map<String, PropsUseRecord> map = player.getPropsUseRecordMap();
			if (map != null) {
				PropsUseRecord pr = map.get(article.get物品二级分类());
				if (pr == null) {
					useNum = 0;
				} else {
					useNum = pr.todayUseTime();
				}
				if (logger.isInfoEnabled()) {
					logger.info("[查询某道具今天使用次数] [" + player.getLogString() + "] [" + articleName + "] [次数:" + useNum + "]");
				}
			}

		} else {
			logger.error("[根据articleName今天可以使用的次数错误] [article" + (article != null ? article.getClass() : "null") + "] [" + player.getLogString() + "] [" + articleName + "]");
		}
		returns[1] = useNum;
		return returns;
	}

	// //////////器灵功能实现(摘取，镶嵌，吞噬，刷属性，刷属性值，)
	public static long 炼器基本花费 = 0;
	public static String 炼器所需物品 = Translate.炼器所需物品;
	public static long[] 炼器锁定属性槽的花费 = new long[] { 100000, 200000, 400000 };
	public static int[] 器灵所有类型 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	public static int 经验洗练消耗 = 10589100;
	public static int 银子洗练消耗 = 10000;
	public static String 经验洗练消耗描述 = Translate.经验洗练消耗描述;
	public static String 银子洗练消耗描述 = Translate.银子洗练消耗描述;
	public static int[] 经验洗练数值增长 = new int[] { 358, 35, 35, 10, 10, 17, 17, 17, 17, 3, 3, 3, 3 };
	public static int[] 经验洗练数值降低 = new int[] { 119, 11, 11, 3, 3, 5, 5, 5, 5, 1, 1, 1, 1 };
	public static int[] 银子洗练数值增长 = new int[] { 717, 71, 71, 21, 21, 35, 35, 35, 35, 6, 6, 6, 6 };
	public static int[] 银子洗练数值降低 = new int[] { 239, 23, 23, 7, 7, 11, 11, 11, 11, 2, 2, 2, 2 };
	public static short 器灵仓库格子数 = 60;
	public static int[] 魂值每个颜色基本值 = new int[] { 100, 200, 500, 800, 1600 };
	public static int[] 魂值最大值 = new int[] { 2500, 12500, 62500, 302500, 1102500 };
	public static int[] 器灵颜色 = new int[] { 0x20FF2A, 0xFF7200, 0xFF7200, 0x9F9F9F, 0x9F9F9F, 0xF01D1D, 0xB8E7E6, 0xF0EE1D, 0xFC00FF, 0xF01D1D, 0xB8E7E6, 0xF0EE1D, 0xFC00FF };
	static byte ANIMATION_LOCATION_TYPE_QILING_XILIAN = 12;// 动画播放位置类型_器灵洗练
	static byte ANIMATION_LOCATION_TYPE_QILING_LIANQI = 13;// 动画播放位置类型_器灵炼器
	static byte ANIMATION_LOCATION_TYPE_QILING_TUNSHI = 14;// 动画播放位置类型_器灵吞噬
	static byte ANIMATION_LOCATION_TYPE_QILING_TUNSHI_ALL = 15;// 动画播放位置类型_器灵全部吞噬

	/**
	 * 第一维表示器灵类型气血 外公修为 法术攻击 物理防御 法术防御 庚金攻击 葵水攻击 离火攻击 乙木攻击 庚金抗性 葵水抗性 离火抗性 乙木抗性
	 * 第二维表示每个类型的5中颜色的最大属性值
	 */
	public static int[][] 器灵各个颜色最大属性值 = new int[][] { { 3588, 7176, 14353, 28706, 57412 }, { 358, 717, 1435, 2870, 5741 }, { 358, 717, 1435, 2870, 5741 }, { 105, 211, 422, 844, 1688 }, { 105, 211, 422, 844, 1688 }, { 89, 179, 358, 717, 2870 }, { 89, 179, 358, 717, 2870 }, { 89, 179, 358, 717, 2870 }, { 89, 179, 358, 717, 2870 }, { 30, 60, 121, 242, 485 }, { 30, 60, 121, 242, 485 }, { 30, 60, 121, 242, 485 }, { 30, 60, 121, 242, 485 } };

	public void 炼器查询(Player player, QUERY_LIANQI_REQ req) {
		QUERY_LIANQI_RES res = new QUERY_LIANQI_RES(req.getSequnceNum(), req.getArticleId(), 炼器所需物品, 炼器基本花费, 炼器锁定属性槽的花费);
		player.addMessageToRightBag(res);
	}

	public void 炼器申请(Player player, EquipmentEntity ee, long materialId, byte[] bindIndexs) {
		if (player == null) {
			return;
		}
		// 万灵榜操作
		{
			boolean bln = ee.isOprate(player, false, EquipmentEntity.装备炼器);
			if (!bln) {
				return;
			}
		}
		String result = 炼器合法性判断(player, ee, materialId, bindIndexs);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		boolean bindNotify = false;
		if (!炼器所需物品.equals("")) {
			ArticleEntity ae = player.getArticleEntity(materialId);
			if (ae.isBinded() && !ee.isBinded()) {
				bindNotify = true;
			}
		}

		//
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		String title = Translate.确定炼器信息;
		mw.setTitle(title);
		String desUUB = Translate.确定炼器信息吗;

		try {
			int count = 0;
			long cost = 炼器基本花费;
			if (bindIndexs != null) {
				count = bindIndexs.length;
				if (bindIndexs.length > 0) {
					cost += 炼器锁定属性槽的花费[bindIndexs.length - 1];
				}
			}
			if (cost <= 0) {
				if (bindNotify) {
					desUUB = Translate.translateString(Translate.您绑定了几个器灵槽使用绑定物品因此装备绑定, new String[][] { { Translate.STRING_1, 炼器所需物品 } });
				} else {
					desUUB = Translate.您绑定了几个器灵槽;
				}
			} else {
				if (bindNotify) {
					desUUB = Translate.translateString(Translate.您绑定了几个器灵槽使用绑定物品因此装备绑定共需花费, new String[][] { { Translate.COUNT_1, count + "" }, { Translate.COUNT_2, BillingCenter.得到带单位的银两(cost) }, { Translate.STRING_1, 炼器所需物品 } });
				} else {
					desUUB = Translate.translateString(Translate.您绑定了几个器灵槽共需花费, new String[][] { { Translate.COUNT_1, count + "" }, { Translate.COUNT_2, BillingCenter.得到带单位的银两(cost) } });
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		mw.setDescriptionInUUB(desUUB);
		Option_LianQiConfirm option = new Option_LianQiConfirm();
		option.ee = ee;
		option.materialId = materialId;
		option.bindIndexs = bindIndexs;
		option.setText(Translate.确定);
		Option[] options = new Option[] { option, new Option_Cancel() };
		mw.setOptions(options);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
		return;

	}

	public void 炼器确定(Player player, EquipmentEntity ee, long materialId, byte[] bindIndexs) {
		if (player == null || ee == null) {
			return;
		}
		// 万灵榜操作
		{
			boolean bln = ee.isOprate(player, false, EquipmentEntity.装备炼器);
			if (!bln) {
				return;
			}
		}
		String result = 炼器合法性判断(player, ee, materialId, bindIndexs);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			if (logger.isInfoEnabled()) {
				logger.info("[炼器] [失败{}] [{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { result, player.getLogString(), ee.getArticleName(), ee.getId(), ee.getColorType(), ee.isBinded() });
			}
			return;
		}

		// 扣除钱
		long cost = 炼器基本花费;
		if (bindIndexs == null) {
			bindIndexs = new byte[0];
		}
		if (bindIndexs != null) {
			if (bindIndexs.length > 0) {
				cost += 炼器锁定属性槽的花费[bindIndexs.length - 1];
			}
		}
		if (cost > 0) {
			BillingCenter bc = BillingCenter.getInstance();
			try {
				bc.playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.LIANQI, "炼器");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.银子不足);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) {
					logger.warn("[炼器] [失败{}] [{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { Translate.银子不足, player.getLogString(), ee.getArticleName(), ee.getId(), ee.getColorType(), ee.isBinded() });
				}
				return;
			}
		}

		if (炼器所需物品 != null && !炼器所需物品.equals("")) {
			ArticleEntity ae = player.getArticleEntity(materialId);
			if (ae == null) {
				String description = Translate.translateString(Translate.没有炼器所需材料, new String[][] { { Translate.STRING_1, 炼器所需物品 } });
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) {
					logger.warn("[炼器] [失败{}] [{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { description, player.getLogString(), ee.getArticleName(), ee.getId(), ee.getColorType(), ee.isBinded() });
				}
				return;
			}
			ArticleEntity removeAe = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "炼器删除", true);
			if (removeAe == null) {
				String description = Translate.translateString(Translate.没有炼器所需材料, new String[][] { { Translate.STRING_1, 炼器所需物品 } });
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) {
					logger.warn("[炼器] [失败{}] [{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { description, player.getLogString(), ee.getArticleName(), ee.getId(), ee.getColorType(), ee.isBinded() });
				}
				return;
			}

			if (ae.isBinded() && !ee.isBinded()) {
				ee.setBinded(true);
				NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
				player.addMessageToRightBag(nreq);
			}
		}

		ArrayList<Integer> 没绑定的位置 = new ArrayList<Integer>();
		ArrayList<Integer> 绑定的类型 = new ArrayList<Integer>();
		ArrayList<Integer> 要随机的类型 = new ArrayList<Integer>();
		for (int i = 0; i < ee.getInlayQiLingArticleTypes().length; i++) {
			boolean has = false;
			for (int index : bindIndexs) {
				if (i == index) {
					has = true;
				}
			}
			if (!has) {
				没绑定的位置.add(i);
			} else {
				绑定的类型.add(ee.getInlayQiLingArticleTypes()[i]);
			}
		}
		for (int a : 器灵所有类型) {
			if (!绑定的类型.contains(a)) {
				要随机的类型.add(a);
			}
		}
		Collections.shuffle(要随机的类型);
		for (int i = 0; i < ee.getInlayQiLingArticleTypes().length && i < 要随机的类型.size(); i++) {
			boolean has = false;
			for (int index : bindIndexs) {
				if (i == index) {
					has = true;
				}
			}
			if (!has) {
				ee.getInlayQiLingArticleTypes()[i] = 要随机的类型.get(i);
			}
		}
		ee.setInlayQiLingArticleTypes(ee.getInlayQiLingArticleTypes());
		if (logger.isWarnEnabled()) {
			logger.warn("[炼器] [成功] [{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { player.getLogString(), ee.getArticleName(), ee.getId(), ee.getColorType(), ee.isBinded() });
		}
		PLAY_ANIMATION_REQ preq = new PLAY_ANIMATION_REQ(GameMessageFactory.nextSequnceNum(), "", "", (byte) 0, ANIMATION_LOCATION_TYPE_QILING_LIANQI, (byte) 0, (byte) 0);
		player.addMessageToRightBag(preq);
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.炼器成功);
		player.addMessageToRightBag(hreq);
		LIANQI_RES res = new LIANQI_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), true);
		player.addMessageToRightBag(res);
		QUERY_EQUIPMENT_QILING_RES res2 = new QUERY_EQUIPMENT_QILING_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), ee.getInlayQiLingArticleTypes(), ee.getInlayQiLingArticleIds(), 装备上的器灵描述(ee));
		player.addMessageToRightBag(res2);
	}

	public String 炼器合法性判断(Player player, EquipmentEntity ee, long materialId, byte[] bindIndexs) {
		if (player == null) {
			return Translate.玩家不能为空;
		}
		if (ee == null) {
			return Translate.装备不能为空;
		}
		Knapsack knapsack = player.getKnapsack_common();
		if (!knapsack.contains(ee)) {
			return Translate.装备必须放入到背包中;
		}
		long[] inlayQiLings = ee.getInlayQiLingArticleIds();
		if (inlayQiLings == null || inlayQiLings.length == 0) {
			return Translate.装备没有器灵孔;
		}
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ee.getId())) {
			return Translate.高级锁魂物品不能做此操作;
		}
		for (long id : inlayQiLings) {
			if (id > 0) {
				return Translate.器灵槽里还有器灵不能进行炼器;
			}
		}

		if (ee != null) {
			ArticleManager am = ArticleManager.getInstance();
			Equipment e = (Equipment) am.getArticle(ee.getArticleName());
			if (isChiBang(e.getEquipmentType())) {
				return Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "炼器" } });
			}
		}
		if (!炼器所需物品.equals("")) {
			if (player.getArticleEntity(materialId) == null) {
				return Translate.translateString(Translate.没有炼器所需材料, new String[][] { { Translate.STRING_1, 炼器所需物品 } });
			}
			ArticleEntity ae = player.getArticleEntity(materialId);
			if (!ae.getArticleName().equals(炼器所需物品)) {
				return Translate.translateString(Translate.炼器需要炼器符, new String[][] { { Translate.STRING_1, 炼器所需物品 } });
			}
		}
		long cost = 炼器基本花费;
		if (bindIndexs != null) {
			if (bindIndexs.length > 3) {
				return Translate.最多可以绑定3个器灵槽;
			}
			if (bindIndexs.length > 0) {
				cost = 炼器锁定属性槽的花费[bindIndexs.length - 1];
			}
			for (byte index : bindIndexs) {
				if (inlayQiLings.length <= index || index < 0) {
					return Translate.不能锁定装备器灵孔范围外的位置;
				}
			}
		}
		if (player.getSilver() + player.getShopSilver() < cost) {
			return Translate.translateString(Translate.银子不足提示, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(cost) } });
		}
		return null;
	}

	public void 查询吞噬(Player player, QUERY_TUNSHI_REQ req) {
		if (player == null) {
			return;
		}
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleEntity ae = aem.getEntity(req.getArticleId());
		String result = 查询吞噬合法性判断(player, ae);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		Article a = this.getArticle(ae.getArticleName());
		if (a instanceof QiLingArticle && ae instanceof QiLingArticleEntity) {
			QiLingArticle ql = (QiLingArticle) a;
			int maxValue = ((QiLingArticleEntity) ae).tunshiValue;
			int[] propValues = 魂值最大值;
			if (propValues != null && propValues.length > ae.getColorType()) {
				maxValue = (int) (propValues[ae.getColorType()] * ql.getQiLingHunLiangTimesFloat());
			}
			QUERY_TUNSHI_RES res = new QUERY_TUNSHI_RES(req.getSequnceNum(), ae.getId(), ((QiLingArticleEntity) ae).tunshiValue, maxValue);
			player.addMessageToRightBag(res);
		}
	}

	public String 查询吞噬合法性判断(Player player, ArticleEntity ae) {
		if (!(ae instanceof QiLingArticleEntity)) {
			return Translate.物品不是器灵;
		}
		if (ae.getColorType() >= 4) {
			return Translate.不需要吞噬了;
		}
		return null;
	}

	public void 吞噬弹窗(Player player, ArticleEntity mainAe, ArticleEntity materialAe, int mainBagType, int materialBagType, int tunshiType) {
		if (player == null) {
			return;
		}
		String result = 吞噬合法性判断(player, mainAe, materialAe, mainBagType, materialBagType, tunshiType);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		if (tunshiType == 0) {
			int count = 吞噬增长的值(mainAe, materialAe);
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			String title = Translate.吞噬提示;
			mw.setTitle(title);
			String desUUB = Translate.translateString(Translate.是否将器灵融合, new String[][] { { Translate.STRING_1, materialAe.getArticleName() }, { Translate.STRING_2, mainAe.getArticleName() }, { Translate.COUNT_1, count + "" } });
			mw.setDescriptionInUUB(desUUB);
			Option_TunshiConfirm option = new Option_TunshiConfirm();
			option.mainAe = mainAe;
			option.mainBagType = mainBagType;
			option.materialAe = materialAe;
			option.materialBagType = materialBagType;
			option.tunshiType = tunshiType;
			option.setText(Translate.确定);
			Option[] options = new Option[] { option, new Option_Cancel() };
			mw.setOptions(options);
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(creq);
			return;
		} else {
			// 全部吞噬
			// 扣除物品，把器灵仓库的器灵依次吞噬，直到主器灵升级满或器灵仓库里没有可用器灵
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			String title = Translate.全部吞噬提示;
			mw.setTitle(title);
			String desUUB = Translate.translateString(Translate.是否将全部器灵融合, new String[][] { { Translate.STRING_2, mainAe.getArticleName() } });
			mw.setDescriptionInUUB(desUUB);
			Option_TunshiConfirm option = new Option_TunshiConfirm();
			option.mainAe = mainAe;
			option.mainBagType = mainBagType;
			option.materialAe = materialAe;
			option.materialBagType = materialBagType;
			option.tunshiType = tunshiType;
			option.setText(Translate.确定);
			Option[] options = new Option[] { option, new Option_Cancel() };
			mw.setOptions(options);
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(creq);
			return;
		}

	}

	public void 吞噬确定(Player player, ArticleEntity mainAe, ArticleEntity materialAe, int mainBagType, int materialBagType, int tunshiType) {
		if (player == null) {
			return;
		}
		String result = 吞噬合法性判断(player, mainAe, materialAe, mainBagType, materialBagType, tunshiType);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		if (tunshiType == 0) {
			// 扣除物品
			Knapsack materialInKnapsack = null;
			if (materialBagType == 0) {
				materialInKnapsack = player.getKnapsack_common();
			} else {
				materialInKnapsack = player.getKnapsacks_QiLing();
			}
			ArticleEntity removeAe = materialInKnapsack.removeByArticleId(materialAe.getId(), "吞噬", true);
			if (removeAe != null) {
				int count = 吞噬增长的值(mainAe, materialAe);
				QiLingArticle ql = (QiLingArticle) ArticleManager.getInstance().getArticle(mainAe.getArticleName());
				((QiLingArticleEntity) mainAe).setTunshiValue(((QiLingArticleEntity) mainAe).getTunshiValue() + count);
				if (((QiLingArticleEntity) mainAe).getTunshiValue() >= 魂值最大值[((QiLingArticleEntity) mainAe).getColorType()] * ql.getQiLingHunLiangTimesFloat()) {
					Article a = ArticleManager.getInstance().getArticle(mainAe.getArticleName());
					if (a instanceof QiLingArticle) {
						// ((QiLingArticleEntity)mainAe).setTunshiValue(((QiLingArticleEntity)mainAe).getTunshiValue() - 魂值最大值[((QiLingArticleEntity)mainAe).getColorType()]);
						((QiLingArticleEntity) mainAe).setColorType(((QiLingArticleEntity) mainAe).getColorType() + 1);
						// 是否有跳跃
						for (int nn = 0; nn < 3; nn++) {
							if (mainAe.getColorType() < 4) {
								if (((QiLingArticleEntity) mainAe).getTunshiValue() >= 魂值最大值[((QiLingArticleEntity) mainAe).getColorType()] * ql.getQiLingHunLiangTimesFloat()) {
									((QiLingArticleEntity) mainAe).setColorType(((QiLingArticleEntity) mainAe).getColorType() + 1);
								} else {
									break;
								}
							} else {
								break;
							}
						}
						// int value = ArticleManager.器灵各个颜色最大属性值[((QiLingArticle)a).getQilingType()][((QiLingArticleEntity)mainAe).getColorType() - 1] + 1;
						// ((QiLingArticleEntity)mainAe).setPropertyValue(value);
						QUERY_ARTICLE_RES res = new QUERY_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[] { CoreSubSystem.translate(mainAe) }, new com.fy.engineserver.datasource.article.entity.client.PropsEntity[0], new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[0]);
						player.addMessageToRightBag(res);
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.恭喜你器灵升级了);
						player.addMessageToRightBag(hreq);
					}
				}
				PLAY_ANIMATION_REQ preq = new PLAY_ANIMATION_REQ(GameMessageFactory.nextSequnceNum(), "", "", (byte) 0, ANIMATION_LOCATION_TYPE_QILING_TUNSHI, (byte) 0, (byte) 0);
				player.addMessageToRightBag(preq);
				String des = Translate.translateString(Translate.吞噬成功增加数值, new String[][] { { Translate.COUNT_1, count + "" } });
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
				player.addMessageToRightBag(hreq);
				Article a = this.getArticle(mainAe.getArticleName());
				if (a instanceof QiLingArticle && mainAe instanceof QiLingArticleEntity) {
					int maxValue = ((QiLingArticleEntity) mainAe).tunshiValue;
					int[] propValues = 魂值最大值;
					if (propValues != null && propValues.length > mainAe.getColorType()) {
						maxValue = (int) (propValues[mainAe.getColorType()] * ql.getQiLingHunLiangTimesFloat());
					}
					QUERY_TUNSHI_RES res = new QUERY_TUNSHI_RES(GameMessageFactory.nextSequnceNum(), mainAe.getId(), ((QiLingArticleEntity) mainAe).tunshiValue, maxValue);
					player.addMessageToRightBag(res);
				}
				logger.warn("[吞噬] [成功] [{}] [main:{}] [{}] [color:{}] [binded:{}] [material:{}] [{}] [color:{}] [binded:{}] [{}]", new Object[] { player.getLogString(), mainAe.getArticleName(), mainAe.getId(), mainAe.getColorType(), mainAe.isBinded(), materialAe.getArticleName(), materialAe.getId(), materialAe.getColorType(), materialAe.isBinded(), des });
				return;
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.扣除物品失败);
				player.addMessageToRightBag(hreq);
				return;
			}
		} else {
			// 全部吞噬
			// 扣除物品，把器灵仓库的器灵依次吞噬，直到主器灵升级满或器灵仓库里没有可用器灵
			Knapsack materialInKnapsack = player.getKnapsacks_QiLing();
			if (materialInKnapsack == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.没有器灵仓库);
				player.addMessageToRightBag(hreq);
				return;
			}
			int allCount = 0;
			Cell[] cells = materialInKnapsack.getCells();
			if (cells == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.器灵仓库出现问题);
				player.addMessageToRightBag(hreq);
				return;
			}
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			QiLingArticle ql = (QiLingArticle) ArticleManager.getInstance().getArticle(mainAe.getArticleName());
			for (int i = 0; i < cells.length; i++) {
				if (mainAe.getColorType() >= 4) {
					break;
				}
				Cell cell = cells[i];
				if (cell != null && cell.entityId > 0) {
					try {
						ArticleEntity tunshiAe = aem.getEntity(cell.entityId);
						if (tunshiAe.getId() != mainAe.getId() && tunshiAe.getColorType() <= mainAe.getColorType()) {
							if (tunshiAe instanceof QiLingArticleEntity) {
								ArticleEntity removeAe = materialInKnapsack.removeByArticleId(tunshiAe.getId(), "吞噬", true);
								if (removeAe != null) {
									int count = 吞噬增长的值(mainAe, tunshiAe);
									allCount += count;
									((QiLingArticleEntity) mainAe).setTunshiValue(((QiLingArticleEntity) mainAe).getTunshiValue() + count);
									String des = Translate.translateString(Translate.吞噬成功增加数值, new String[][] { { Translate.COUNT_1, count + "" } });
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, des);
									player.addMessageToRightBag(hreq);
									logger.warn("[全部吞噬] [成功] [{}] [main:{}] [{}] [color:{}] [binded:{}] [material:{}] [{}] [color:{}] [binded:{}] [{}]", new Object[] { player.getLogString(), mainAe.getArticleName(), mainAe.getId(), mainAe.getColorType(), mainAe.isBinded(), tunshiAe.getArticleName(), tunshiAe.getId(), tunshiAe.getColorType(), tunshiAe.isBinded(), des });
									if (((QiLingArticleEntity) mainAe).getTunshiValue() >= 魂值最大值[((QiLingArticleEntity) mainAe).getColorType()] * ql.getQiLingHunLiangTimesFloat()) {
										Article a = ArticleManager.getInstance().getArticle(mainAe.getArticleName());
										if (a instanceof QiLingArticle) {
											// ((QiLingArticleEntity)mainAe).setTunshiValue(((QiLingArticleEntity)mainAe).getTunshiValue() -
											// 魂值最大值[((QiLingArticleEntity)mainAe).getColorType()]);
											((QiLingArticleEntity) mainAe).setColorType(((QiLingArticleEntity) mainAe).getColorType() + 1);
											// 是否有跳跃
											if (mainAe.getColorType() < 4) {
												if (((QiLingArticleEntity) mainAe).getTunshiValue() >= 魂值最大值[((QiLingArticleEntity) mainAe).getColorType()] * ql.getQiLingHunLiangTimesFloat()) {
													((QiLingArticleEntity) mainAe).setColorType(((QiLingArticleEntity) mainAe).getColorType() + 1);
												}
											}
											// int value = ArticleManager.器灵各个颜色最大属性值[((QiLingArticle)a).getQilingType()][((QiLingArticleEntity)mainAe).getColorType() - 1] + 1;
											// ((QiLingArticleEntity)mainAe).setPropertyValue(value);
											QUERY_ARTICLE_RES res = new QUERY_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[] { CoreSubSystem.translate(mainAe) }, new com.fy.engineserver.datasource.article.entity.client.PropsEntity[0], new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[0]);
											player.addMessageToRightBag(res);
											HINT_REQ hreq2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.恭喜你器灵升级了);
											player.addMessageToRightBag(hreq2);
											break;
										}
									}
								}
							}
						}
					} catch (Exception ex) {
						logger.error("[全部吞噬] [异常，继续吞噬下一个]", ex);
					}
				}
			}

			PLAY_ANIMATION_REQ preq = new PLAY_ANIMATION_REQ(GameMessageFactory.nextSequnceNum(), "", "", (byte) 0, ANIMATION_LOCATION_TYPE_QILING_TUNSHI_ALL, (byte) 0, (byte) 0);
			player.addMessageToRightBag(preq);

			String des = Translate.translateString(Translate.吞噬成功增加数值, new String[][] { { Translate.COUNT_1, allCount + "" } });
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
			player.addMessageToRightBag(hreq);

			Article a = this.getArticle(mainAe.getArticleName());
			if (a instanceof QiLingArticle && mainAe instanceof QiLingArticleEntity) {
				int maxValue = ((QiLingArticleEntity) mainAe).tunshiValue;
				int[] propValues = 魂值最大值;
				if (propValues != null && propValues.length > mainAe.getColorType()) {
					maxValue = (int) (propValues[mainAe.getColorType()] * ql.getQiLingHunLiangTimesFloat());
				}
				QUERY_TUNSHI_RES res = new QUERY_TUNSHI_RES(GameMessageFactory.nextSequnceNum(), mainAe.getId(), ((QiLingArticleEntity) mainAe).tunshiValue, maxValue);
				player.addMessageToRightBag(res);
			}
		}

	}

	public int 吞噬增长的值(ArticleEntity mainAe, ArticleEntity materialAe) {
		int count = 0;
		if (materialAe instanceof QiLingArticleEntity) {
			if (((QiLingArticleEntity) materialAe).getTunshiValue() > 0) {
				count += ((QiLingArticleEntity) materialAe).tunshiValue;
			} else {
				count += 魂值每个颜色基本值[materialAe.getColorType()];
			}
		} else {
			Article a = ArticleManager.getInstance().getArticle(materialAe.getArticleName());
			if (a instanceof QiLingDanArticle) {
				count += ((QiLingDanArticle) a).getAddHunLiang();
			}
		}
		return count;
	}

	public String 吞噬合法性判断(Player player, ArticleEntity mainAe, ArticleEntity materialAe, int mainBagType, int materialBagType, int tunshiType) {
		// 吞噬类型0表示单独吞噬
		if (tunshiType == 0) {
			if (mainAe == null) {
				return Translate.主器灵不能为空;
			}
			if (materialAe == null) {
				return Translate.副器灵不能为空;
			}
			Knapsack mainInKnapsack = null;
			Knapsack materialInKnapsack = null;
			if (mainBagType == 0) {
				mainInKnapsack = player.getKnapsack_common();
			} else {
				mainInKnapsack = player.getKnapsacks_QiLing();
			}
			if (materialBagType == 0) {
				materialInKnapsack = player.getKnapsack_common();
			} else {
				materialInKnapsack = player.getKnapsacks_QiLing();
			}
			if (mainInKnapsack == null || materialInKnapsack == null) {
				return Translate.请正确放入物品;
			}
			if (!mainInKnapsack.contains(mainAe)) {
				return Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, mainAe.getArticleName() } });
			}
			if (!materialInKnapsack.contains(materialAe)) {
				return Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, materialAe.getArticleName() } });
			}

			if (mainAe.getColorType() >= 4) {
				return Translate.主器灵已经升到最高级不需要吞噬了;
			}

			Article materialA = ArticleManager.getInstance().getArticle(materialAe.getArticleName());
			if (materialA instanceof QiLingDanArticle) {
				return null;
			} else {
				if (!(mainAe instanceof QiLingArticleEntity) || !(materialAe instanceof QiLingArticleEntity)) {
					return Translate.请放入器灵;
				}
				if (materialAe.getColorType() > mainAe.getColorType()) {
					return Translate.主器灵颜色不能低于副器灵颜色;
				}
			}

		} else {
			// 全部吞噬
			if (mainAe == null) {
				return Translate.主器灵不能为空;
			}
			Knapsack mainInKnapsack = null;
			if (mainBagType == 0) {
				mainInKnapsack = player.getKnapsack_common();
			} else {
				mainInKnapsack = player.getKnapsacks_QiLing();
			}
			if (mainInKnapsack == null) {
				return Translate.请正确放入物品;
			}
			if (!mainInKnapsack.contains(mainAe)) {
				return Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, mainAe.getArticleName() } });
			}
			if (!(mainAe instanceof QiLingArticleEntity)) {
				return Translate.请放入器灵;
			}
			if (mainAe.getColorType() >= 4) {
				return Translate.主器灵已经升到最高级不需要吞噬了;
			}
			boolean has = false;
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			if (player.getKnapsacks_QiLing() != null) {
				for (int i = 0; i < player.getKnapsacks_QiLing().getCells().length; i++) {
					Cell cell = player.getKnapsacks_QiLing().getCell(i);
					if (cell != null && cell.getEntityId() > 0) {
						ArticleEntity ae = aem.getEntity(cell.getEntityId());
						if (ae != null && ae.getColorType() <= mainAe.getColorType() && ae.getId() != mainAe.getId()) {
							has = true;
						}
					}
				}
			}
			if (!has) {
				return Translate.器灵仓库中没有满足吞噬条件的器灵;
			}
		}

		return null;
	}

	public void 查询洗练(Player player, QUERY_XILIAN_REQ req) {
		if (player == null) {
			return;
		}
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleEntity ae = aem.getEntity(req.getArticleId());
		String result = 查询洗练合法性判断(player, ae);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		Article a = this.getArticle(ae.getArticleName());
		if (a instanceof QiLingArticle && ae instanceof QiLingArticleEntity) {
			int[] range = new int[] { 1, ((QiLingArticleEntity) ae).propertyValue };
			int max = ((QiLingArticle) a).getMaxProperty(ae.getColorType());
			if (max > 0) {
				range[1] = max;
			}
			QUERY_XILIAN_RES res = new QUERY_XILIAN_RES(req.getSequnceNum(), ae.getId(), ((QiLingArticle) a).getQilingType(), "<f color='" + 器灵颜色[((QiLingArticle) a).getQilingType()] + "' size='28'>" + ((QiLingArticleEntity) ae).propertyValue + "</f>", range[0] + "~" + range[1], 经验洗练消耗描述, 银子洗练消耗描述);
			player.addMessageToRightBag(res);
		}
	}

	public String 查询洗练合法性判断(Player player, ArticleEntity ae) {
		if (!(ae instanceof QiLingArticleEntity)) {
			return Translate.物品不是器灵;
		}
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(ae.getArticleName());
		if (!(a instanceof QiLingArticle)) {
			return Translate.物品不是器灵;
		}
		return null;
	}

	public void 器灵洗练(Player player, ArticleEntity ae, byte xilianType) {
		if (player == null) {
			return;
		}
		String result = 器灵洗练合法性判断(player, ae, xilianType);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		// 消耗
		if (xilianType == 0) {
			player.setExp(player.getExp() - 经验洗练消耗);
			GamePlayerManager.logger.warn("[装备洗练消耗经验] [{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { player.getLogString(), ae.getArticleName(), ae.getId(), ae.getColorType(), ae.isBinded() });
		} else {
			BillingCenter bc = BillingCenter.getInstance();
			try {
				bc.playerExpense(player, 银子洗练消耗, CurrencyType.SHOPYINZI, ExpenseReasonType.XILIAN, "洗练");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.银子不足);
				player.addMessageToRightBag(hreq);
				logger.warn("[装备洗练] [失败{}] [{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { Translate.银子不足, player.getLogString(), ae.getArticleName(), ae.getId(), ae.getColorType(), ae.isBinded() });
				return;
			}
		}

		Random random = player.random;
		// 随机数0表示成功
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(ae.getArticleName());
		QiLingArticleEntity qlae = (QiLingArticleEntity) ae;
		QiLingArticle qla = (QiLingArticle) a;
		int maxValue = qla.getMaxProperty(ae.getColorType());
		int minValue = 1;
		// if(ae.getColorType() > 0){
		// minValue = qla.getPropertysValues()[ae.getColorType() - 1] + 1;
		// }
		int type = qla.getQilingType();
		if (type < 0 || type >= 器灵所有类型.length) {
			return;
		}
		int modifyValue = 0;
		if (random.nextInt(2) == 0) {
			int value = 0;
			if (xilianType == 0) {
				value = 经验洗练数值增长[type];
			} else {
				value = 银子洗练数值增长[type];
			}

			if (qlae.propertyValue + value > maxValue) {
				modifyValue = maxValue - qlae.propertyValue;
				qlae.setPropertyValue(maxValue);
			} else {
				qlae.setPropertyValue(qlae.propertyValue + value);
				modifyValue = value;
			}
			if (qlae.propertyValue >= maxValue) {
				try {
					RecordAction action = PlayerAimManager.器灵满级颜色action[qlae.getColorType()];
					if (action != null) {
						EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), action, 1L });
						EventRouter.getInst().addEvent(evt3);
					}
				} catch (Exception eex) {
					PlayerAimManager.logger.error("[目标系统] [统计装备器灵异常] [" + player.getLogString() + "]", eex);
				}
			}
			PLAY_ANIMATION_REQ preq = new PLAY_ANIMATION_REQ(GameMessageFactory.nextSequnceNum(), "", "", (byte) 0, ANIMATION_LOCATION_TYPE_QILING_XILIAN, (byte) 0, (byte) 0);
			player.addMessageToRightBag(preq);
			String des = Translate.translateString(Translate.器灵属性变为, new String[][] { { Translate.STRING_1, ae.getArticleName() }, { Translate.COUNT_1, qlae.propertyValue + "" } });
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
			player.addMessageToRightBag(hreq);
			logger.warn("[洗练] [成功属性升] [{}] [{}] [{}] [color:{}] [binded:{}] [{}]", new Object[] { player.getLogString(), ae.getArticleName(), ae.getId(), ae.getColorType(), ae.isBinded(), des });
		} else {
			int value = 0;
			if (xilianType == 0) {
				value = 经验洗练数值降低[type];
			} else {
				value = 银子洗练数值降低[type];
			}

			if (qlae.propertyValue - value < minValue) {
				modifyValue = -(qlae.propertyValue - minValue);
				qlae.setPropertyValue(minValue);
			} else {
				qlae.setPropertyValue(qlae.propertyValue - value);
				modifyValue = -value;
			}
			PLAY_ANIMATION_REQ preq = new PLAY_ANIMATION_REQ(GameMessageFactory.nextSequnceNum(), "", "", (byte) 0, ANIMATION_LOCATION_TYPE_QILING_XILIAN, (byte) 0, (byte) 0);
			player.addMessageToRightBag(preq);
			String des = Translate.translateString(Translate.器灵属性变为, new String[][] { { Translate.STRING_1, ae.getArticleName() }, { Translate.COUNT_1, qlae.propertyValue + "" } });
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
			player.addMessageToRightBag(hreq);
			logger.warn("[洗练] [成功属性降] [{}] [{}] [{}] [color:{}] [binded:{}] [{}]", new Object[] { player.getLogString(), ae.getArticleName(), ae.getId(), ae.getColorType(), ae.isBinded(), des });
		}
		if (a instanceof QiLingArticle && ae instanceof QiLingArticleEntity) {
			int[] range = new int[] { 1, ((QiLingArticleEntity) ae).propertyValue };
			int max = ((QiLingArticle) a).getMaxProperty(ae.getColorType());
			if (max > 0) {
				range[1] = max;
			}
			String des = "";
			if (modifyValue > 0) {
				des = "<f color='0x00ff00' size='28'>(+" + modifyValue + ")</f>";
			} else if (modifyValue == 0) {
				des = "<f size='28'>(" + modifyValue + ")</f>";
			} else {
				des = "<f color='0xff0000' size='28'>(" + modifyValue + ")</f>";
			}
			QUERY_XILIAN_RES res = new QUERY_XILIAN_RES(GameMessageFactory.nextSequnceNum(), ae.getId(), ((QiLingArticle) a).getQilingType(), "<f color='" + 器灵颜色[((QiLingArticle) a).getQilingType()] + "' size='28'>" + ((QiLingArticleEntity) ae).propertyValue + "</f>" + des, range[0] + "/" + range[1], 经验洗练消耗描述, 银子洗练消耗描述);
			player.addMessageToRightBag(res);
		}
	}

	public String 器灵洗练合法性判断(Player player, ArticleEntity ae, byte xilianType) {
		if (!(ae instanceof QiLingArticleEntity)) {
			return Translate.物品不是器灵;
		}
		if (player.getArticleEntityNum(ae.getId()) <= 0) {
			return Translate.你没有这个物品;
		}
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(ae.getArticleName());
		if (!(a instanceof QiLingArticle)) {
			return Translate.物品不是器灵;
		}
		QiLingArticleEntity qlae = (QiLingArticleEntity) ae;
		QiLingArticle qla = (QiLingArticle) a;
		int maxValue = qla.getMaxProperty(ae.getColorType());
		if (qlae.getPropertyValue() >= maxValue) {
			return Translate.属性数值已经达到最大值不需要洗练了;
		}
		// 经验
		if (xilianType == 0) {
			if (player.getExp() < 经验洗练消耗) {
				return Translate.您的经验不足;
			}
		} else {
			if (player.getSilver() + player.getShopSilver() < 银子洗练消耗) {
				return Translate.您的银子不足;
			}
		}
		return null;
	}

	public void 器灵镶嵌(Player player, EquipmentEntity ee, ArticleEntity ae, int inlayIndex) {
		if (player == null) {
			return;
		}

		// 万灵榜装备操作
		{
			boolean bln = ee.isOprate(player, false, EquipmentEntity.器灵附灵);
			if (!bln) {
				return;
			}
		}
		if (ee != null) {
			ArticleManager am = ArticleManager.getInstance();
			Equipment e = (Equipment) am.getArticle(ee.getArticleName());
			if (isChiBang(e.getEquipmentType())) {
				player.sendError(Translate.translateString(Translate.翅膀不能, new String[][] { { Translate.STRING_1, "附灵" } }));
				return;
			}
		}
		String result = 器灵镶嵌合法性判断(player, ee, ae, inlayIndex);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		if (ae.isBinded() && !ee.isBinded()) {
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			String title = Translate.镶嵌提示;
			mw.setTitle(title);
			String desUUB = Translate.由于镶嵌物品绑定镶嵌后装备将绑定;
			mw.setDescriptionInUUB(desUUB);
			Option_QiLingInlayConfirm option = new Option_QiLingInlayConfirm();
			option.ee = ee;
			option.ae = ae;
			option.inlayIndex = inlayIndex;
			option.setText(Translate.确定);
			Option[] options = new Option[] { option, new Option_Cancel() };
			mw.setOptions(options);
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(creq);
			return;
		}

		器灵镶嵌确认(player, ee, ae, inlayIndex);
	}

	public void 器灵镶嵌确认(Player player, EquipmentEntity ee, ArticleEntity ae, int inlayIndex) {
		if (player == null) {
			return;
		}
		String result = 器灵镶嵌合法性判断(player, ee, ae, inlayIndex);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		if (ArticleManager.logger.isWarnEnabled()) {
			ArticleManager.logger.warn("[镶嵌器灵前] " + player.getPlayerPropsString());
		}

		// 删除背包中的器灵，把器灵id放到装备上
		ArticleEntity removeAe = player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "镶嵌", false);
		if (removeAe != null) {
			ee.getInlayQiLingArticleIds()[inlayIndex] = ae.getId();
			ee.setInlayQiLingArticleIds(ee.getInlayQiLingArticleIds());
			if (ae.isBinded() && !ee.isBinded()) {
				ee.setBinded(true);
				NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(ee) });
				player.addMessageToRightBag(nreq);
			}
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.镶嵌成功);
			player.addMessageToRightBag(hreq);
			QUERY_EQUIPMENT_QILING_RES res = new QUERY_EQUIPMENT_QILING_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), ee.getInlayQiLingArticleTypes(), ee.getInlayQiLingArticleIds(), 装备上的器灵描述(ee));
			player.addMessageToRightBag(res);
			try {
				RecordAction action = PlayerAimManager.器灵颜色action[ae.getColorType()];
				if (action != null) {
					EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), action, 1L });
					EventRouter.getInst().addEvent(evt3);
				}
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计装备器灵异常] [" + player.getLogString() + "]", eex);
			}
			logger.warn("[器灵镶嵌] [成功] [{}] [{}] [{}] [color:{}] [binded:{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { player.getLogString(), ee.getArticleName(), ee.getId(), ee.getColorType(), ee.isBinded(), ae.getArticleName(), ae.getId(), ae.getColorType(), ae.isBinded() });
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn("[镶嵌器灵后] " + player.getPlayerPropsString());
			}
			return;
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.删除物品失败);
			player.addMessageToRightBag(hreq);
			logger.warn("[器灵镶嵌] [失败] [{}] [{}] [{}] [color:{}] [binded:{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { player.getLogString(), ee.getArticleName(), ee.getId(), ee.getColorType(), ee.isBinded(), ae.getArticleName(), ae.getId(), ae.getColorType(), ae.isBinded() });
		}
	}

	public static String 装备上的器灵描述(EquipmentEntity ee) {
		StringBuffer sb = new StringBuffer();
		long[] qilingIds = ee.getInlayQiLingArticleIds();
		int[] qilingTypes = ee.getInlayQiLingArticleTypes();
		if (qilingIds != null && qilingTypes != null) {
			if (qilingTypes.length > 0) {
				int idNums = 0;
				for (int i = 0; i < qilingIds.length; i++) {
					if (qilingIds[i] > 0) {
						idNums++;
					}
				}
				// sb.append("\n<f color='0xFFFF00'>").append("器灵槽:").append(idNums).append("/").append(qilingTypes.length).append("</f>\n");
				// for (int i = 0; i < qilingTypes.length; i++) {
				// String icon = "";
				// if (qilingIds[i] > 0) {
				// icon = "/ui/qiling/add"+(qilingTypes[i])+".png";
				// }else {
				// icon = "/ui/qiling/aslot"+(qilingTypes[i])+".png";
				// }
				// sb.append("<i imagePath='").append(icon).append("'").append("></i>");
				// }
				// sb.append(" \n \n");
				for (int i = 0; i < qilingTypes.length && i < qilingIds.length; i++) {
					sb.append("\n<f color='").append(EquipmentEntity.qilingInfoColor[qilingTypes[i]]).append("'>").append(EquipmentEntity.qilingInfoNames[qilingTypes[i]]).append(":");
					if (qilingIds[i] > 0) {
						QiLingArticleEntity entity = (QiLingArticleEntity) ArticleEntityManager.getInstance().getEntity(qilingIds[i]);

						sb.append("+").append(entity.getPropertyValue());
					} else {
						sb.append("空");
					}
					sb.append("</f>");
				}
			}
		}

		return sb.toString();
	}

	public String 器灵镶嵌合法性判断(Player player, EquipmentEntity ee, ArticleEntity ae, int inlayIndex) {
		if (ee == null || ae == null) {
			return Translate.物品不存在;
		}
		if (!(ae instanceof QiLingArticleEntity)) {
			return Translate.物品不是器灵;
		}
		if (player.getArticleEntityNum(ee.getId()) <= 0) {
			return Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() } });
		}
		if (player.getArticleEntityNum(ae.getId()) <= 0) {
			return Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
		}
		int[] inlayTypes = ee.getInlayQiLingArticleTypes();
		if (inlayTypes.length <= inlayIndex || inlayIndex < 0) {
			return Translate.镶嵌位置错误;
		}
		Article a = this.getArticle(ae.getArticleName());
		if (!(a instanceof QiLingArticle)) {
			return Translate.物品不是器灵;
		}
		int type = inlayTypes[inlayIndex];
		if (ee.getInlayQiLingArticleIds()[inlayIndex] > 0) {
			return Translate.这个位置已经镶嵌了器灵;
		}
		if (type != ((QiLingArticle) a).getQilingType()) {
			return Translate.类型不一致不能镶嵌到这个位置;
		}
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ee.getId())) {
			return Translate.高级锁魂物品不能做此操作;
		}
		Article equip = getArticle(ee.getArticleName());
		if (equip instanceof Equipment) {
			if (((QiLingArticle) a).getEquipmentType() != ((Equipment) equip).getEquipmentType()) {
				return Translate.这个器灵不能镶嵌在这种部位上;
			}
		}

		return null;
	}

	public void 器灵挖取(Player player, EquipmentEntity ee, int outlayIndex) {
		if (player == null) {
			return;
		}
		// 万灵榜装备操作
		{
			boolean bln = ee.isOprate(player, false, EquipmentEntity.器灵摘取);
			if (!bln) {
				return;
			}
		}

		String result = 器灵挖取合法性判断(player, ee, outlayIndex);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		// 挖取器灵放入背包
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		long id = ee.getInlayQiLingArticleIds()[outlayIndex];
		ee.getInlayQiLingArticleIds()[outlayIndex] = 0;
		ee.setInlayQiLingArticleIds(ee.getInlayQiLingArticleIds());
		ArticleEntity ae = aem.getEntity(id);
		if (player.putToKnapsacks(ae, "挖取器灵")) {
			String des = Translate.挖取成功;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
			player.addMessageToRightBag(hreq);
			QUERY_EQUIPMENT_QILING_RES res = new QUERY_EQUIPMENT_QILING_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), ee.getInlayQiLingArticleTypes(), ee.getInlayQiLingArticleIds(), 装备上的器灵描述(ee));
			player.addMessageToRightBag(res);
			logger.warn("[器灵挖取] [成功] [{}] [{}] [{}] [color:{}] [binded:{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { player.getLogString(), ee.getArticleName(), ee.getId(), ee.getColorType(), ee.isBinded(), ae.getArticleName(), id, ae.getColorType(), ae.isBinded() });
			return;
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.挖取失败);
			player.addMessageToRightBag(hreq);
			logger.warn("[器灵挖取] [失败] [{}] [{}] [{}] [color:{}] [binded:{}] [{}] [{}] [color:{}] [binded:{}]", new Object[] { player.getLogString(), ee.getArticleName(), ee.getId(), ee.getColorType(), ee.isBinded(), ae.getArticleName(), id, ae.getColorType(), ae.isBinded() });
			return;
		}
	}

	public String 器灵挖取合法性判断(Player player, EquipmentEntity ee, int outlayIndex) {
		if (ee == null) {
			return Translate.物品不存在;
		}
		if (player.getArticleEntityNum(ee.getId()) <= 0) {
			return Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, ee.getArticleName() } });
		}
		int[] inlayTypes = ee.getInlayQiLingArticleTypes();
		if (inlayTypes.length <= outlayIndex || outlayIndex < 0) {
			return Translate.挖取位置错误;
		}
		if (ee.getInlayQiLingArticleIds()[outlayIndex] <= 0) {
			return Translate.这个位置上没有器灵;
		}
		if (!player.putAllOK(new ArticleEntity[] { ee })) {
			return Translate.背包空间不足;
		}
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, ee.getId())) {
			return Translate.高级锁魂物品不能做此操作;
		}
		return null;
	}

	public static String[] composeJianDingFu = new String[] { Translate.鉴定符, Translate.传奇鉴定符, Translate.传说鉴定符, Translate.神话鉴定符 };
	public static String[] jianDingFus = new String[] { Translate.鉴定符, Translate.传奇鉴定符, Translate.传说鉴定符, Translate.神话鉴定符, Translate.永恒鉴定符 };
	public static long JianDingMoney = 1000L;

	public int 是否合成鉴定符(Player player, ArticleEntity entity) {
		for (int i = 0; i < composeJianDingFu.length; i++) {
			String name = composeJianDingFu[i];
			if (name.equals(entity.getArticleName())) {
				return i;
			}
		}
		return -1;
	}

	public void 新鉴定装备(Player player, NEW_JIANDING_OK_REQ req) {
		try {
			long equipID = req.getEquipmentId();
			long propID = req.getPropId();
			int propCellIndex = req.getCellIndex();
			EquipmentEntity equip = (EquipmentEntity) (ArticleEntityManager.getInstance().getEntity(equipID));
			if (equip == null) {
				player.sendError(Translate.请放入装备);
				return;
			}
			boolean bln = equip.isOprate(player, false, EquipmentEntity.鉴定);
			if (!bln) {
				return;
			}
			ArticleEntity prop = ArticleEntityManager.getInstance().getEntity(propID);
			if (prop == null) {
				player.sendError(Translate.请放入鉴定所需物品);
				return;
			}
			ArticleEntity k_prop = player.getKnapsack_common().getArticleEntityByCell(propCellIndex);
			if (k_prop.getId() != propID) {
				player.sendError(Translate.请放入鉴定所需物品);
				return;
			}
			int needPropIndex = -1;
			for (int i = 0; i < ReadEquipmentExcelManager.endowmentsNewPropNames.length; i++) {
				if (ReadEquipmentExcelManager.endowmentsNewPropNames[i].equals(prop.getArticleName())) {
					needPropIndex = i;
					break;
				}
			}
			if (needPropIndex < 0) {
				player.sendError(Translate.请放入鉴定所需物品);
				return;
			}

			if (equip.getEndowments() < ArticleManager.maxEndowments) {
				player.sendError(Translate.非完美不能鉴定);
				return;
			}

			int equiIndex = -1;
			for (int i = 1; i < ReadEquipmentExcelManager.endowmentsNewValues_int.length; i++) {
				if (equip.getEndowments() - ArticleManager.newEndowments < ReadEquipmentExcelManager.endowmentsNewValues_int[i]) {
					equiIndex = i - 1;
					break;
				}
			}
			if (equiIndex == -1) {
				player.sendError(Translate.鉴定到最高);
				return;
			}
			if (equiIndex != needPropIndex) {
				player.sendError(Translate.鉴定符不匹配);
				return;
			}

			if (!equip.isBinded()) {
				if (prop.isBinded()) {
					MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
					Option_NewJianDing_OK op1 = new Option_NewJianDing_OK();
					op1.setEquip(equip);
					op1.setProp(prop);
					op1.setText(Translate.确定);
					Option_Cancel op2 = new Option_Cancel();
					op2.setText(Translate.取消);
					mw.setOptions(new Option[] { op1, op2 });
					mw.setDescriptionInUUB(Translate.鉴定确定绑定);
					CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
					player.addMessageToRightBag(creq);
					return;
				}
			}
			确定新鉴定(player, equip, prop);

		} catch (Exception e) {
			logger.warn("新鉴定出错", e);
		}
	}

	public boolean isChiBang(int equipmentType) {
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
			return true;
		} else {
			return false;
		}
	}

	public void 确定新鉴定(Player player, EquipmentEntity equip, ArticleEntity prop) {
		try {
			if (player.getArticleEntityNum(equip.getId()) <= 0) {
				player.sendError(Translate.装备不能为空);
				return;
			}
			if (player.getArticleEntityNum(prop.getId()) <= 0) {
				player.sendError(Translate.请放入鉴定所需物品);
				return;
			}

			ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(prop.getId(), "鉴定删除", true);
			if (aee == null) {
				String description = Translate.删除物品不成功;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.error("[新鉴定] [" + description + "] [id:" + prop.getId() + "]");
				return;
			}
			int equiIndex = -1;
			for (int i = 1; i < ReadEquipmentExcelManager.endowmentsNewValues_int.length; i++) {
				if (equip.getEndowments() - ArticleManager.newEndowments < ReadEquipmentExcelManager.endowmentsNewValues_int[i]) {
					equiIndex = i - 1;
					break;
				}
			}
			int value = ReadEquipmentExcelManager.endowmentsNewValues_int[equiIndex + 1] - ReadEquipmentExcelManager.endowmentsNewValues_int[equiIndex];
			Random rm = new Random();
			int add = rm.nextInt(value - 1) + 1;
			int oldEn = equip.getEndowments();
			if (equip.getEndowments() == ArticleManager.maxEndowments) {
				equip.setEndowments(ArticleManager.newEndowments + ReadEquipmentExcelManager.endowmentsNewValues_int[0] + add);
			} else {
				int max = equip.getEndowments() + add;
				if (max > ArticleManager.newEndowments + ReadEquipmentExcelManager.endowmentsNewValues_int[equiIndex + 1]) {
					max = ArticleManager.newEndowments + ReadEquipmentExcelManager.endowmentsNewValues_int[equiIndex + 1];
				}
				equip.setEndowments(max);
			}
			if (prop.isBinded() && !equip.isBinded()) {
				equip.setBinded(true);
			}
			NEW_JIANDING_OK_RES res = new NEW_JIANDING_OK_RES(GameMessageFactory.nextSequnceNum(), equip.getId(), equip.getEndowments());
			player.addMessageToRightBag(res);
			NOTIFY_EQUIPMENT_CHANGE_REQ nreq = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate(equip) });
			player.addMessageToRightBag(nreq);
			// if (prop.getColorType() >= 3 && !prop.isBinded()) {
			// ShopActivityManager.getInstance().noticeUseSuccess(player, prop, 1);
			// }
			ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
			strongMaterialEntitys.add(prop);
			ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
			try {
				EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.鉴定装备次数, 1L });
				EventRouter.getInst().addEvent(evt);
				EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.使用高级鉴定次数, 1L });
				EventRouter.getInst().addEvent(evt2);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [鉴定装备统计异常] [" + player.getLogString() + "]", eex);
			}
			logger.warn("[新鉴定成功] [color:" + prop.getColorType() + "] [aeename:" + prop.getArticleName() + "]  [{}] [{}] [{}] [{}]", new Object[] { player.getLogString(), oldEn + "-" + equip.getEndowments(), equip.getId(), equip.getArticleName() });
		} catch (Exception e) {
			logger.error("确定新鉴定出错:", e);
		}
	}

	public void handleEquipmentUpgrade(Player player, GOD_EQUIPMENT_UPGRADE_REQ req) {
		long tuzhiid = req.getTuZhiId();
		ArticleEntity tz = ArticleEntityManager.getInstance().getEntity(tuzhiid);
		if (tz == null) {
			logger.warn("[图纸升级装备] [失败] [物品不存在:" + tuzhiid + "] [" + player.getLogString() + "]");
			return;
		}

		Article a = ArticleManager.getInstance().getArticle(tz.getArticleName());
		if (a == null || a instanceof EquipmentTuZhiProp == false) {
			logger.warn("[图纸升级装备] [失败] [a:" + (a == null ? "null" : a.getName_stat()) + "] [atype:" + (a == null ? "null" : a.getClass().getSimpleName()) + "] [" + player.getLogString() + "]");
			return;
		}

		List<UpgradeRule> rules = ArticleManager.rules;
		UpgradeRule rule = null;
		for (UpgradeRule r : rules) {
			if (r.getRuleName().equals(((EquipmentTuZhiProp) a).getRuleName())) {
				rule = r;
				break;
			}
		}
		if (rule == null) {
			ArticleManager.logger.warn("[图纸升级装备] [失败] [rule==null] [ruleName:" + ((EquipmentTuZhiProp) a).getRuleName() + "] [propname:" + tz.getArticleName() + "] [" + player.getLogString() + "]");
			return;
		}

		ArticleEntity tempAe = null;
		try {
			Article tempArticle = ArticleManager.getInstance().getArticle(rule.getNewEquipmentName());
			tempAe = DefaultArticleEntityManager.getInstance().getTempEntity(rule.getNewEquipmentName(), true, tempArticle.getColorType());
			if (tempAe == null) {
				tempAe = DefaultArticleEntityManager.getInstance().createTempEntity(tempArticle, true, ArticleEntityManager.图纸合成临时装备, player, rule.getNewEquipmentColor());
			}
			if (tempAe == null) {
				ArticleManager.logger.warn("[图纸升级装备] [失败] [tempAe=null] [newname:" + (rule.getNewEquipmentName() == null ? "null" : rule.getNewEquipmentName()) + "] [ruleName:" + rule.getRuleName() + "] [" + player.getLogString() + "]");
				return;
			}
			if (tempArticle instanceof EnchantArticle) {
				tempAe.setBinded(false);
			}
			String messes[] = new String[rule.getMaterialNames().length];
			String icons[] = new String[rule.getMaterialNames().length];
			for (int i = 0; i < icons.length; i++) {
				Article aa = ArticleManager.getInstance().getArticle(rule.getMaterialNames()[i]);
				if (aa != null) {
					icons[i] = aa.getIconId();
					if (aa instanceof Props) {
						Props p = (Props) aa;
						messes[i] = p.getInfoShow();
					} else {
						messes[i] = aa.getInfoShow();
					}
				}
			}

			GOD_EQUIPMENT_UPGRADE_RES res = new GOD_EQUIPMENT_UPGRADE_RES(req.getSequnceNum(), tz.getId(), tempAe.getId(), rule.getMaterialNames(), icons, messes, rule.getMaterialNum(), (byte) rule.getTypename(), rule.getShowMess(), rule.getCostSilver());
			player.addMessageToRightBag(res);
			ArticleManager.logger.warn("[图纸升级装备] [成功] [图纸:" + tz.getArticleName() + "] [tempArticle:" + tempArticle + "] [names:" + Arrays.toString(rule.getMaterialNames()) + "] [nums:" + Arrays.toString(rule.getMaterialNum()) + "] [ruleName:" + rule.getRuleName() + "] [" + player.getLogString() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 装备升级确认
	 * @param player
	 * @param req
	 */
	public void EquipmentUpgradeConfirm(Player player, GOD_EQUIPMENT_UPGRADE_SURE_REQ req, boolean isBindSure) {
		long start = System.currentTimeMillis();
		long tuzhiid = req.getTuZhiId();
		long materialIds[] = req.getMaterialIds();
		int materialNums[] = req.getMaterialNums();

		if (tuzhiid <= 0 || materialIds == null || materialNums == null) {
			player.sendError(Translate.请放入材料);
			ArticleManager.logger.warn("[图纸升级装备确认] [失败:请放入材料] [tuzhiid：" + tuzhiid + "] [materialNums:" + materialNums + "] [materialIds:" + (materialIds == null ? "null" : materialIds.length) + "] [" + player.getLogString() + "]");
			GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
			player.addMessageToRightBag(res);
			return;
		}
		ArticleManager.logger.warn("测a试1==================" + tuzhiid + "=========materialIds:" + Arrays.toString(materialIds) + "--" + Arrays.toString(materialNums));
		// 客户端传2个老装备判断
		int oldEqNums = 0;
		for (int i = 0; i < materialIds.length; i++) {
			ArticleEntity ae2 = DefaultArticleEntityManager.getInstance().getEntity(materialIds[i]);
			if (ae2 != null && ae2 instanceof EquipmentEntity) {
				oldEqNums++;
			}
		}
		if (oldEqNums >= 2) {
			player.sendError(Translate.装备合成2个老装备提示);
			GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
			player.addMessageToRightBag(res);
			return;
		}

		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(tuzhiid);
		if (ae == null) {
			ArticleManager.logger.warn("[图纸升级装备确认] [失败] [图纸不存在：" + tuzhiid + "] [" + player.getLogString() + "]");
			GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
			player.addMessageToRightBag(res);
			return;
		}
		ArticleManager.logger.warn("测a试2===========================");
		Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if (a == null || a instanceof EquipmentTuZhiProp == false) {
			ArticleManager.logger.warn("[图纸升级装备确认] [失败] [获取图纸出错] [" + player.getLogString() + "]");
			GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
			player.addMessageToRightBag(res);
			return;
		}

		boolean tzIsBind = ae.isBinded();

		EquipmentTuZhiProp tz = (EquipmentTuZhiProp) a;
		EquipmentEntity oldEq = null;

		List<UpgradeRule> rules = ArticleManager.rules;
		UpgradeRule rule = null;
		for (UpgradeRule r : rules) {
			if (r.getRuleName().equals(tz.getRuleName())) {
				rule = r;
				break;
			}
		}
		if (rule == null) {
			ArticleManager.logger.warn("[图纸升级装备确认] [失败] [rule==null] [ruleName:" + ((EquipmentTuZhiProp) a).getRuleName() + "] [tuzhiname:" + ae.getArticleName() + "] [" + player.getLogString() + "]");
			GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
			player.addMessageToRightBag(res);
			return;
		}
		ArticleManager.logger.warn("测a试3===========================");
		if (rule.getCostSilver() > 0) {
			if (rule.getCostSilver() > player.getSilver()) {
				player.sendError(Translate.余额不足);
				GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
				player.addMessageToRightBag(res);
				return;
			}
		}

		// name,num
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < rule.getMaterialNames().length; i++) {
			Integer oldvalue = map.get(rule.getMaterialNames()[i]);
			if (oldvalue == null) {
				map.put(rule.getMaterialNames()[i], 0);
				oldvalue = map.get(rule.getMaterialNames()[i]);
			}
			map.put(rule.getMaterialNames()[i], rule.getMaterialNum()[i] + oldvalue.intValue());
		}

		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			int count = 0;
			for (int i = 0; i < materialIds.length; i++) {
				long id = materialIds[i];
				if (id > 0) {
					ArticleEntity ae2 = DefaultArticleEntityManager.getInstance().getEntity(id);
					if (ae2 != null) {
						if (ae2.getArticleName().equals(key)) {
							count = count + materialNums[i];
						}
					}
				}
			}
			// ArticleManager.logger.warn("[图纸升级装备确认] [测试] [失败] [材料数量不足] [材料："+key+"] [材料分布："+Arrays.toString(rule.getMaterialNames())+"] [数量分布:"+Arrays.toString(rule.getMaterialNum())+"] [数量分布2:"+Arrays.toString(materialNums)+"] [需要数量："+map.get(key).intValue()+"] [客户端传的："+count+"] [ruleName:"+((EquipmentTuZhiProp)a).getRuleName()+"] [tuzhiname:"+ae.getArticleName()+"] ["+player.getLogString()+"]");
			if (count < map.get(key).intValue()) {
				player.sendError(Translate.放入材料数量不足);
				ArticleManager.logger.warn("[图纸升级装备确认] [失败] [材料数量不足] [材料：" + key + "] [需要数量：" + map.get(key).intValue() + "] [nums:" + Arrays.toString(materialNums) + "] [客户端传的：" + count + "] [ruleName:" + ((EquipmentTuZhiProp) a).getRuleName() + "] [tuzhiname:" + ae.getArticleName() + "] [" + player.getLogString() + "]");
				GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
				player.addMessageToRightBag(res);
				return;
			}
		}
		ArticleManager.logger.warn("测a试4===========================");
		Knapsack k = player.getKnapsack_common();
		boolean bindStlyIsSame = true;
		boolean newArticleisBind = false;
		if (k != null) {
			if (k.indexOf(ae) == -1) {
				player.sendError(Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae != null ? ae.getArticleName() : Translate.该物品) } }));
				ArticleManager.logger.warn("[图纸升级装备确认] [失败] [背包中没有图纸：" + tuzhiid + "] [" + player.getLogString() + "]");
				return;
			}

			int index = 0;
			for (long id : materialIds) {

				ArticleEntity ae2 = DefaultArticleEntityManager.getInstance().getEntity(id);
				if (ae2 == null) {
					ArticleManager.logger.warn("[图纸升级装备确认] [失败] [材料不存在：" + id + "] [idlength:" + materialIds.length + "] [" + player.getLogString() + "]");
					GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
					player.addMessageToRightBag(res);
					return;
				}
				if (ae2.isBinded()) {
					newArticleisBind = true;
				}
				if (tzIsBind) {
					newArticleisBind = true;
				}
				if (ae2.isBinded() != tzIsBind) {
					bindStlyIsSame = false;
				}
				if (k.indexOf(ae2) == -1) {
					player.sendError(Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, (ae2 != null ? ae2.getArticleName() : Translate.该物品) } }));
					ArticleManager.logger.warn("[图纸升级装备确认] [失败] [背包中没有材料：" + id + "] [" + player.getLogString() + "]");
					GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
					player.addMessageToRightBag(res);
					return;
				}
				if (k.countArticle(ae2.getArticleName()) < materialNums[index]) {
					player.sendError(Translate.放入材料数量不足);
					ArticleManager.logger.warn("[图纸升级装备确认] [失败] [材料不足：" + id + "] [" + k.countArticle(ae2.getArticleName()) + "] [需要：" + materialNums[index] + "] [" + player.getLogString() + "]");
					GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
					player.addMessageToRightBag(res);
					return;
				}

				if (rule.getTypename() == 0 && index == 0) {
					if (ae2 instanceof EquipmentEntity == false) {
						ArticleManager.logger.warn("[图纸升级装备确认] [失败] [材料中没有装备] [ae2:" + ae2.getArticleName() + "] [" + player.getLogString() + "]");
						GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
						player.addMessageToRightBag(res);
						return;
					}
					oldEq = (EquipmentEntity) ae2;
				}
				// ArticleManager.logger.warn("[图纸升级装备确认] [测试] [失败] [材料中没有老装备] [ae2:"+ae2.getArticleName()+"] [index:"+index+"] [type:"+rule.getTypename()+"] [oldEq:"+oldEq+"] ["+player.getLogString()+"]");
				index++;
			}
		} else {
			ArticleManager.logger.warn("[图纸升级装备确认] [失败] [背包不存在] [" + player.getLogString() + "]");
			GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
			player.addMessageToRightBag(res);
			return;
		}

		if (tz.getTimeType() == 0 && oldEq == null) {
			ArticleManager.logger.warn("[图纸升级装备确认] [失败] [材料中没有老装备] [type:" + tz.getTimeType() + "] [oldEq:" + oldEq + "] [" + player.getLogString() + "]");
			GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
			player.addMessageToRightBag(res);
			return;
		}

		if (oldEq != null) {
			if (oldEq.getColorType() < 6) {
				player.sendError(Translate.完美橙装备才可以升级);
				return;
			}
			ArticleManager.logger.warn("测a试5===========================");
			long[] inlayQiLings = oldEq.getInlayQiLingArticleIds();
			if (inlayQiLings != null) {
				for (long id : inlayQiLings) {
					if (id > 0) {
						player.sendError(Translate.器灵槽里还有器灵不能进行合成);
						GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
						player.addMessageToRightBag(res);
						return;
					}
				}
			}

			if (oldEq.getInlayArticleIds() != null) {
				for (long l : oldEq.getInlayArticleIds()) {
					if (l > 0) {
						player.sendError(Translate.装备镶嵌了宝石不能升级);
						GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
						player.addMessageToRightBag(res);
						return;
					}
				}
			}

			if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, oldEq.getId())) {
				player.sendError(Translate.锁魂物品不能做此操作);
				GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
				player.addMessageToRightBag(res);
				return;
			}

			if (oldEq.isInscriptionFlag() == false) {
				String description = Translate.必须铭刻才能操作;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				return;
			}
		}

		String newArticleName = rule.getNewEquipmentName();
		Article newA = ArticleManager.getInstance().getArticle(newArticleName);
		if (newA == null) {
			ArticleManager.logger.warn("[图纸升级装备确认] [失败] [获取合成新物品信息出错] [" + player.getLogString() + "]");
			GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
			player.addMessageToRightBag(res);
			return;
		}

		ArticleManager.logger.warn("测a试6===========bindStlyIsSame:" + bindStlyIsSame + "======isBindSure:" + isBindSure + "========newArticleisBind:" + newArticleisBind + "==");
		if (isBindSure) {
			try {
				ArticleEntity raee = player.removeArticleEntityFromKnapsackByArticleId(tuzhiid, "装备升级删除", true);
				if (raee == null) {
					ArticleManager.logger.warn("[图纸升级装备确认] [失败] [删除图纸id] [tuzhiid:" + tuzhiid + "] [ruleName:" + ((EquipmentTuZhiProp) a).getRuleName() + "] [tuzhiname:" + ae.getArticleName() + "] [" + player.getLogString() + "]");
					GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
					player.addMessageToRightBag(res);
					return;
				} else {
					ArticleStatManager.addToArticleStat(player, null, raee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "装备升级删除", null);
				}
				player.getKnapsack_common().removeArticleEntity(player, materialIds, materialNums, "图纸合成");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (!bindStlyIsSame) {
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(300);
				mw.setDescriptionInUUB(Translate.装备合成材料绑定提示);
				Option_Equipment_Merge_Sure option = new Option_Equipment_Merge_Sure();
				option.setText(Translate.text_125);
				option.setReq(req);
				Option_Cancel oc = new Option_Cancel();
				oc.setText(Translate.text_126);
				oc.setColor(0xffffff);
				mw.setOptions(new Option[] { option, oc });

				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);
				return;
			} else {
				try {
					ArticleEntity raee = player.removeArticleEntityFromKnapsackByArticleId(tuzhiid, "装备升级删除", true);
					if (raee == null) {
						ArticleManager.logger.warn("[图纸升级装备确认] [失败] [删除图纸id] [tuzhiid:" + tuzhiid + "] [ruleName:" + ((EquipmentTuZhiProp) a).getRuleName() + "] [tuzhiname:" + ae.getArticleName() + "] [" + player.getLogString() + "]");
						GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
						player.addMessageToRightBag(res);
						return;
					} else {
						ArticleStatManager.addToArticleStat(player, null, raee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "装备升级删除", null);
					}
					player.getKnapsack_common().removeArticleEntity(player, materialIds, materialNums, "图纸合成");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		if (rule.getCostSilver() > 0) {
			try {
				BillingCenter bc = BillingCenter.getInstance();
				bc.playerExpense(player, rule.getCostSilver(), CurrencyType.BIND_YINZI, ExpenseReasonType.用图纸升级装备, "用图纸升级装备");
			} catch (Exception ex) {
				player.sendError(Translate.扣费失败);
				ex.printStackTrace();
				if (logger.isWarnEnabled()) logger.warn("[图纸升级装备确认] [扣费失败] [costsilver:" + rule.getCostSilver() + "] [" + player.getLogString() + "]", ex);
				GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
				player.addMessageToRightBag(res);
				return;
			}
		}

		try {
			ArticleManager.logger.warn("测a试7===========================" + newA.getClass().getSimpleName());
			ArticleEntity newAE = ArticleEntityManager.getInstance().createEntity(newA, true, ArticleEntityManager.CREATE_REASON_装备升级, player, rule.getNewEquipmentColor(), 1, true);
			if (newAE instanceof EquipmentEntity) {
				EquipmentEntity newE = (EquipmentEntity) newAE;
				newE.setInscriptionFlag(true);
				if (oldEq != null) {
					newE.setStar(oldEq.getStar());
				}
				newE.setCreaterName(player.getName());
				if (player.putToKnapsacks(newE, "图纸升级装备")) {
					PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备精炼绑定铭刻成功, (byte) 1, AnimationManager.动画播放位置类型_精炼绑定铭刻, 0, 0);
					if (pareq != null) {
						player.addMessageToRightBag(pareq);
					}
					String description = Translate.装备升级完成;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
					player.addMessageToRightBag(hreq);
					GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), newE.getId());
					player.addMessageToRightBag(res);
					ArticleStatManager.addToArticleStat(player, null, newE, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "装备升级获得", null);

					if (logger.isInfoEnabled()) {
						if (logger.isInfoEnabled()) logger.info("[图纸升级装备确认] [成功] [新装备：" + newE.getArticleName() + "] [" + player.getLogString() + "] [耗时：" + (System.currentTimeMillis() - start) + "]");
					}
				} else {
					String description = Translate.背包满通过邮件;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { newE }, new int[] { 1 }, Translate.装备升级完成, Translate.背包满通过邮件, 0, 0, 0, "装备升级");
					if (logger.isInfoEnabled()) {
						if (logger.isInfoEnabled()) logger.info("[图纸升级装备确认] [成功2] [新装备：" + newE.getArticleName() + "] [" + player.getLogString() + "] [耗时：" + (System.currentTimeMillis() - start) + "]");
					}
					ArticleStatManager.addToArticleStat(player, null, newE, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "装备升级获得", null);
					GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), newE.getId());
					player.addMessageToRightBag(res);
				}

				try {
					AchievementManager.getInstance().record(player, RecordAction.使用仙装图纸升级仙装次数);
					end: for (NewSuitEquipment ee : ArticleManager.suits) {
						String suitPart[] = ee.getSuitPart();
						for (String name : suitPart) {
							if (newE.getArticleName().equals(name)) {
								AchievementManager.getInstance().record(player, RecordAction.合成套装属性装备次数);
								break end;
							}
						}
					}
				} catch (Exception e) {
					PlayerAimManager.logger.error("[目标系统] [统计使用仙装图纸升级仙装次数] [异常] [" + player.getLogString() + "]", e);
				}
			} else if (newAE instanceof PropsEntity) {
				PropsEntity newP = (PropsEntity) newAE;
				newP.setBinded(newArticleisBind);
				if (player.putToKnapsacks(newP, "图纸合成翅膀道具")) {
					PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备精炼绑定铭刻成功, (byte) 1, AnimationManager.动画播放位置类型_精炼绑定铭刻, 0, 0);
					if (pareq != null) {
						player.addMessageToRightBag(pareq);
					}
					String description = Translate.翅膀合成成功;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
					player.addMessageToRightBag(hreq);
					GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), newP.getId());
					player.addMessageToRightBag(res);
					ArticleStatManager.addToArticleStat(player, null, newP, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "图纸合成翅膀道具获得", null);

					if (logger.isInfoEnabled()) {
						if (logger.isInfoEnabled()) logger.info("[图纸升级装备确认] [图纸合成翅膀道具] [成功] [新装备：" + newP.getArticleName() + "] [bind:" + newAE.isBinded() + "] [id:" + newP.getId() + "] [" + player.getLogString() + "] [耗时：" + (System.currentTimeMillis() - start) + "]");
					}
				} else {
					String description = Translate.背包满通过邮件;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { newP }, new int[] { 1 }, Translate.翅膀合成成功, Translate.背包满通过邮件, 0, 0, 0, "图纸合成翅膀道具");
					if (logger.isInfoEnabled()) {
						if (logger.isInfoEnabled()) logger.info("[图纸升级装备确认] [图纸合成翅膀道具] [成功2] [新装备：" + newP.getArticleName() + "] [" + player.getLogString() + "] [耗时：" + (System.currentTimeMillis() - start) + "]");
					}
					ArticleStatManager.addToArticleStat(player, null, newP, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "图纸合成翅膀道具获得", null);
					GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), newP.getId());
					player.addMessageToRightBag(res);
				}
			} else if (newA instanceof EnchantArticle) {
				newAE.setBinded(newArticleisBind);
				if (player.putToKnapsacks(newAE, "图纸合成附魔卷轴道具")) {
					PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.装备精炼绑定铭刻成功, (byte) 1, AnimationManager.动画播放位置类型_精炼绑定铭刻, 0, 0);
					if (pareq != null) {
						player.addMessageToRightBag(pareq);
					}
					String description = Translate.合成成功;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, description);
					player.addMessageToRightBag(hreq);
					GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), newAE.getId());
					player.addMessageToRightBag(res);
					ArticleStatManager.addToArticleStat(player, null, newAE, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "图纸合成附魔卷轴道具获得", null);

					if (logger.isInfoEnabled()) {
						if (logger.isInfoEnabled()) logger.info("[图纸升级装备确认] [图纸合成附魔卷轴道具] [成功] [新装备：" + newAE.getArticleName() + "] [bind:" + newAE.isBinded() + "] [id:" + newAE.getId() + "] [" + player.getLogString() + "] [耗时：" + (System.currentTimeMillis() - start) + "]");
					}
				} else {
					String description = Translate.背包满通过邮件;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { newAE }, new int[] { 1 }, Translate.合成成功, Translate.背包满通过邮件, 0, 0, 0, "图纸合成附魔卷轴道具");
					if (logger.isInfoEnabled()) {
						if (logger.isInfoEnabled()) logger.info("[图纸升级装备确认] [图纸合成附魔卷轴道具] [成功2] [新装备：" + newAE.getArticleName() + "] [" + player.getLogString() + "] [耗时：" + (System.currentTimeMillis() - start) + "]");
					}
					ArticleStatManager.addToArticleStat(player, null, newAE, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "图纸合成附魔卷轴道具获得", null);
					GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), newAE.getId());
					player.addMessageToRightBag(res);
				}
			}
		} catch (Exception ex) {
			player.sendError(Translate.合成失败);
			ex.printStackTrace();
			if (logger.isWarnEnabled()) logger.warn("[图纸升级装备确认] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
			GOD_EQUIPMENT_UPGRADE_SURE_RES res = new GOD_EQUIPMENT_UPGRADE_SURE_RES(GameMessageFactory.nextSequnceNum(), -1);
			player.addMessageToRightBag(res);
		}

	}

	/**
	 * 处理装备套装属性
	 * useType:1,穿 2,脱
	 */
	public void handleEquipmentSuit(Player p, EquipmentEntity ee, int soulType, int useType) {
		long now = System.currentTimeMillis();
		if (p == null || ee == null) {
			logger.warn("[处理装备套装属性] [失败] [player:{}] [ee:{}] [soulType:{}] [useType:{}]", new Object[] { p, ee, soulType, useType });
			return;
		}

		Soul soul = p.getSoul(soulType);
		if (soul == null) {
			logger.warn("[处理装备套装属性] [失败] [元神不存在] [元神类型：{}] [玩家信息：{}]", new Object[] { soulType, p.getLogString() });
			return;
		}

		QUERY_ARTICLE_INFO_RES qres2 = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), ee.getId(), ee.getInfoShow(p));
		p.addMessageToRightBag(qres2);

		for (int i = 0; i < p.getEquipmentColumns().getEquipmentArrayByCopy().length; i++) {
			ArticleEntity aee = p.getEquipmentColumns().getEquipmentArrayByCopy()[i];
			if (aee == null || !(aee instanceof EquipmentEntity)) {
				continue;
			}
			EquipmentEntity entity = (EquipmentEntity) aee;
			if (entity != null) {
				Equipment e = (Equipment) ArticleManager.getInstance().getArticle(entity.getArticleName());
				if (e != null && e.getPlayerLevelLimit() >= 220) {
					// System.out.println("[更新背包装备信息1] [装备:"+e.getName()+"] [等级："+e.getPlayerLevelLimit()+"] ["+p.getName()+"]");
					QUERY_ARTICLE_INFO_RES qres = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), entity.getId(), entity.getInfoShow(p));
					p.addMessageToRightBag(qres);
				}
			}
		}

		Knapsack k = p.getKnapsack_common();
		if (k != null) {
			for (Cell c : k.getCells()) {
				if (c != null && c.getEntityId() > 0) {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(c.getEntityId());
					if (ae != null && ae instanceof EquipmentEntity) {
						EquipmentEntity ee2 = (EquipmentEntity) ae;
						Equipment e = (Equipment) ArticleManager.getInstance().getArticle(ee2.getArticleName());
						// System.out.println("[更新背包装备信息2] [装备:"+ae.getArticleName()+"] [等级："+e.getPlayerLevelLimit()+"] ["+p.getName()+"]");
						if (e != null && e.getPlayerLevelLimit() >= 220) {
							QUERY_ARTICLE_INFO_RES qres = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), ee2.getId(), ee2.getInfoShow(p));
							p.addMessageToRightBag(qres);
						}
					}
				}
			}
		}

		NewSuitEquipment currSuit = null; // 该装备影响的套装
		end: for (NewSuitEquipment e : suits) {
			String suitPart[] = e.getSuitPart();
			for (String name : suitPart) {
				if (ee.getArticleName().equals(name)) {
					currSuit = e;
					break end;
				}
			}
		}

		if (currSuit == null) {
			logger.warn("[处理装备套装属性] [失败] [该装备无影响] [装备：{}] [元神类型：{}] [玩家信息：{}]", new Object[] { ee.getArticleName(), soulType, p.getLogString() });
			return;
		}

		int affectsuitNum = 0;
		int currSuitNum = 0;
		boolean isCurrSoul = p.isCurrSoul(soulType);

		ArticleEntity[] aes = soul.getEc().getEquipmentArrayByCopy();
		if (aes == null) {
			logger.warn("[处理装备套装属性] [失败:装备栏实体不存在] [isCurrSoul:" + isCurrSoul + "] [suitpart:" + currSuit.getSuitPart() != null ? Arrays.toString(currSuit.getSuitPart()) : "null" + "] [1穿，2脱，useType:" + useType + "] [currename:" + Arrays.toString(p.getEquipmentColumns().currEquipmentName) + "] [装备：" + ee.getArticleName() + "] [影响数：" + affectsuitNum + "] [当前有效果的套装数：" + currSuitNum + "] [玩家：" + p.getName() + "]");
			return;
		}

		for (String name : currSuit.getSuitPart()) {
			if(name == null){
				continue;
			}
			for (ArticleEntity ae : aes) {
				if (ae != null && ae.getArticleName().equals(name)) {
					affectsuitNum++;
					currSuitNum++;
				}
			}
//			if(useType == 2){
//				if(name.equals(ee.getArticleName())){
//					affectsuitNum++;
//				}
//			}
		}
		
		logger.warn("[处理装备套装属性] [测试2] [1穿，2脱，useType:" + useType + "] [currename:" + (p.getEquipmentColumns() != null ? Arrays.toString(p.getEquipmentColumns().currEquipmentName) : "null" )+ "] [装备：" + ee.getArticleName() + "] [影响数：" + affectsuitNum + "] [当前有效果的套装数：" + currSuitNum + "] [玩家：" + p.getLogString() + "]");
		if (currSuit.getSuitResult() != null && currSuit.getSuitResult().length > 0) {
			if (affectsuitNum < currSuit.getSuitResult()[0]) {
				logger.warn("[处理装备套装属性] [失败] [当前身上装备数不足套装效果] [最小套装数：{}] [当前拥有套装装备数：{}] [装备：{}] [元神类型：{}] [玩家信息：{}]", new Object[] { currSuit.getSuitResult()[0], affectsuitNum, ee.getArticleName(), soulType, p.getLogString() });
				return;
			}

			int index = -1;
			for (int i = 0; i < currSuit.getSuitResult().length; i++) {
				if (currSuit.getSuitResult()[i] == affectsuitNum) {
					index = i;
				}
			}

			if (affectsuitNum == 0 || index < 0) {
				logger.warn("[处理装备套装属性] [失败] [当前套装数不满足触发套装效果] [index:{}] [触发条件数集：{}] [当前拥有套装装备数：{}] [装备：{}] [元神类型：{}] [玩家信息：{}]", new Object[] { index, Arrays.toString(currSuit.getSuitResult()), affectsuitNum, ee.getArticleName(), soulType, p.getLogString() });
				return;
			}

			for (int i = 0; i < currSuit.propValues.length; i++) {
				if (index < currSuit.propValues[i].length) {
					if (currSuit.propValues[i][index] > 0) {
						int value = (int) currSuit.propValues[i][index];
						if (useType == 2) {
							value = (-value);
						}
						int idType = currSuit.propNames[i];
						double add = isCurrSoul ? 1 : (soul.getAddPercent() + p.getUpgradeNums() * 0.01);
						MagicWeaponManager.instance.addAttr2Player(p, idType, value, add, isCurrSoul, soulType);
						logger.warn("[处理装备套装属性] [成功] [属性：{}] [操作类型：{}] [数值：{}] [add：{}] [玩家：{}] [耗时：{}ms]", new Object[] { idType, useType, value, add, p.getLogString(), (System.currentTimeMillis() - now) });
					}
				}
			}

		}

	}

	public BaoShiXiaZiData getxiLianData(Player p, long id) {
		for (BaoShiXiaZiData data : baoShiXiaZiDatas) {
			if (data.getId() == id) {
				return data;
			}
		}
		try {
			BaoShiXiaZiData data = ArticleEntityManager.baoShiXiLianEM.find(id);
			if (data != null) {
				baoShiXiaZiDatas.add(data);
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.warn("[获取宝石匣子数据] [出错] [{}] [宝石id：{}]", new Object[] { (p == null ? "nul" : p.getLogString4Knap()), id });
		return null;
	}

	/**
	 * 宝石匣子出生初始化格子颜色
	 */
	public void initBaoShiXiaZiColor(ArticleEntity ae) {
		Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
		if (a != null && a instanceof InlayArticle) {
			InlayArticle baoshi = (InlayArticle) a;
			int inlayType = baoshi.getInlayType();
			if (inlayType > 1) {
				ae.setBinded(false);// 神匣是非绑定的
				int rcolors[] = getRandomCellColors(ae.getArticleName());
				long ids[] = new long[inlayType];
				if (rcolors != null) {
					BaoShiXiaZiData data = new BaoShiXiaZiData();
					data.setId(ae.getId());
					Arrays.fill(ids, -1l);
					data.setColors(rcolors);
					data.setIds(ids);
					ArticleEntityManager.baoShiXiLianEM.notifyNewObject(data);
					baoShiXiaZiDatas.add(data);
				}
				logger.warn("[宝石匣子出生] [初始化格子颜色数量] [匣子id:{}] [匣子名:{}] [格子数量:{}] [baoShiXiaZiDatas:{}] [随机格子颜色:{}] [ids:{}]", new Object[] { ae.getId(), ae.getArticleName(), inlayType, baoShiXiaZiDatas.size(), Arrays.toString(rcolors), Arrays.toString(ids) });
			}
		}
	}

	public XILIAN_PAGE_RES queryXiLianPage(RequestMessage message, Player player) {
		if (message instanceof XILIAN_PAGE_REQ) {
			XILIAN_PAGE_REQ req = (XILIAN_PAGE_REQ) message;
			long id = req.getBaoShiId();
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
			String mess = "ok";
			if (ae == null) {
				mess = "物品实体不存在";
			} else {
				Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
				if (a == null) {
					mess = "物品不存在";
				} else {
					if (a instanceof InlayArticle == false) {
						mess = "物品类型不对";
					} else {
						if (((InlayArticle) a).getInlayType() <= 1) {
							mess = "不是宝石匣子";
						} else {
							BaoShiXiaZiData data = getxiLianData(player, id);
							if (data == null) {
								mess = "宝石匣子信息不存在";
							} else {
								logger.warn("[请求宝石洗练界面] [成功] [colors:{}] [aename:{}] [id:{}] [{}] [player:{}]", new Object[] { Arrays.toString(data.getColors()), ae.getArticleName(), id, Arrays.toString(data.getIds()), player.getLogString() });
								return new XILIAN_PAGE_RES(req.getSequnceNum(), data.getColors(), data.getIds());
							}
						}
					}
				}
			}
			if (!mess.equals("ok")) {
				logger.warn("[请求宝石洗练界面] [出错:{}] [aename:{}] [id:{}] [{}]", new Object[] { mess, ae.getArticleName(), id, player.getLogString4Knap() });
				return new XILIAN_PAGE_RES(req.getSequnceNum(), new int[1], new long[1]);
			}
		}
		return null;
	}

	public void handle_xilian_bug(long id, Player player) {
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
		String mess = "ok";
		if (ae == null) {
			mess = "物品实体不存在";
		} else {
			Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
			if (a == null) {
				mess = "物品不存在";
			} else {
				if (a instanceof InlayArticle == false) {
					mess = "物品类型不对";
				} else {
					if (((InlayArticle) a).getInlayType() <= 1) {
						mess = "不是宝石匣子";
					} else {
						BaoShiXiaZiData data = getxiLianData(player, id);
						if (data == null) {
							mess = "宝石匣子信息不存在";
						} else {
							logger.warn("[处理客户端bug宝石洗练界面] [成功] [colors:{}] [aename:{}] [id:{}] [{}] [player:{}]", new Object[] { Arrays.toString(data.getColors()), ae.getArticleName(), id, Arrays.toString(data.getIds()), player.getLogString() });
							player.addMessageToRightBag(new XILIAN_PAGE_RES(GameMessageFactory.nextSequnceNum(), data.getColors(), data.getIds()));
							return;
						}
					}
				}
			}
		}
		if (!mess.equals("ok")) {
			logger.warn("[处理客户端bug宝石洗练界面] [出错:{}] [aename:{}] [id:{}] [{}]", new Object[] { mess, ae.getArticleName(), id, player.getLogString4Knap() });
			player.addMessageToRightBag(new XILIAN_PAGE_RES(GameMessageFactory.nextSequnceNum(), new int[1], new long[1]));
		}
	}

	public int[] getRandomCellColors(String articleName) {
		Article a = ArticleManager.getInstance().getArticle(articleName);
		if (a != null && a instanceof InlayArticle) {
			int inlayType = ((InlayArticle) a).getInlayType();
			if (inlayType > 1) {
				if (baoShiClolors.keySet().contains(articleName)) {
					double[] rNums = baoShiClolors.get(articleName);
					if (rNums != null) {
						double count = 0;
						for (int j = 0; j < rNums.length; j++) {
							count += rNums[j];
						}
						if (count > 0) {
							int colors[] = new int[inlayType];
							for (int i = 1; i < colors.length; i++) {
								int randomValue = random.nextInt((int) count);
								int indexnum = 0;
								for (int j = 0; j < rNums.length; j++) {
									indexnum += rNums[j];
									if (indexnum >= randomValue) {
										colors[i] = j;
										break;
									}
								}
							}
							int firstColor = 0;
							String baoxiachuan[] = { "lvse", "chengse", "heise", "lanse", "hongse", "baise", "huangse", "zise" };
							for (int i = 0; i < baoxiachuan.length; i++) {
								if (a.getIconId() != null) {
									if (a.getIconId().contains(baoxiachuan[i])) {
										firstColor = i;
									}
								}
							}
							colors[0] = firstColor;
							return colors;
						}
					}
				}
			}
		}
		return null;
	}

	public XILIAN_REMOVE_RES handleXILIAN_REMOVE_REQ(RequestMessage message, Player player) {
		if (message instanceof XILIAN_REMOVE_REQ) {
			XILIAN_REMOVE_REQ req = (XILIAN_REMOVE_REQ) message;
			long baoshiid = req.getBaoShiId();
			long removebaoshiid = req.getRemovebaoShiId();
			long materials[] = req.getMaterialId();
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(baoshiid);
			ArticleEntity rae = ArticleEntityManager.getInstance().getEntity(removebaoshiid);
			String mess = "--";
			if (ae != null && materials != null && rae != null) {
				BaoShiXiaZiData data = ArticleManager.getInstance().getxiLianData(player, baoshiid);
				if (data != null) {
					long oldmaterials[] = data.getIds();
					int cellIndex = -1;
					long newIds[] = new long[oldmaterials.length];
					int nums = 0;
					for (int i = 0; i < oldmaterials.length; i++) {
						if (i > 0 && oldmaterials[i] > 0) {
							nums++;
						}
						newIds[i] = oldmaterials[i];
						if (oldmaterials[i] == removebaoshiid) {
							cellIndex = i;
						}
					}
					if (cellIndex == 0 && nums > 0) {
						player.sendError(Translate.必须先拿掉副宝石);
						return new XILIAN_REMOVE_RES(message.getSequnceNum(), 0);
					}
					if (cellIndex == -1) {
						logger.warn("[宝石匣子取出宝石] [失败:{}] [baoshiid:{}] [removebaoshiid:{}] [materials:{}] [player:{}]", new Object[] { "匣子没有记录", baoshiid, removebaoshiid, Arrays.toString(materials), player.getLogString() });
						return new XILIAN_REMOVE_RES(message.getSequnceNum(), 0);
					}

					if (player.getKnapsack_common().contains(rae)) {
						logger.warn("[宝石匣子取出宝石] [失败:{}] [baoshiid:{}] [removebaoshiid:{}] [materials:{}] [player:{}]", new Object[] { "背包内已有该宝石", baoshiid, removebaoshiid, Arrays.toString(materials), player.getLogString() });
						return new XILIAN_REMOVE_RES(message.getSequnceNum(), 0);
					}

					boolean isok = false;
					if (!player.getKnapsack_common().isFull() && player.putToKnapsacks(rae, "取出宝石")) {
						isok = true;
					} else {
						try {
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { rae }, Translate.从匣子内取出宝石背包满提示, Translate.从匣子内取出宝石背包满提示, 0, 0, 0, "从匣子内取出宝石");
							isok = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (isok) {
						ArticleStatManager.addToArticleStat(player, null, rae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "取出宝石匣子活动", null);
						newIds[cellIndex] = -1;

						data.setIds(newIds);
						List<String> list = new ArrayList<String>();
						for (long id : newIds) {
							if (id > 0) {
								ArticleEntity baoshiAE = ArticleEntityManager.getInstance().getEntity(id);
								if (baoshiAE != null) {
									list.add(baoshiAE.getArticleName());
								}
							}
						}
						data.setNames(list.toArray(new String[] {}));
						ArticleEntityManager.baoShiXiLianEM.notifyNewObject(data);
						for (BaoShiXiaZiData d : baoShiXiaZiDatas) {
							if (d.getId() == data.getId()) {
								d.setIds(newIds);
								d.setNames(list.toArray(new String[] {}));
							}
						}

						String description = AritcleInfoHelper.generate(player, ae);
						QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(req.getSequnceNum(), ae.getId(), description);
						player.addMessageToRightBag(res);
						logger.warn("[宝石匣子取出宝石] [成功] [baoshiid:{}] [removebaoshiid:{}] [materials:{}] [oldMaterials:{}] [newIds:{}] [{}]", new Object[] { baoshiid, removebaoshiid, Arrays.toString(materials), (oldmaterials == null ? "nul" : Arrays.toString(oldmaterials)), Arrays.toString(newIds), player.getLogString4Knap() });
						return new XILIAN_REMOVE_RES(message.getSequnceNum(), 1);
					} else {
						mess = "放入背包出错";
					}
				} else {
					mess = "宝石数据出错";
				}
			} else {
				mess = "获取物品出错";
			}
			logger.warn("[宝石匣子取出宝石] [失败:{}] [baoshiid:{}] [removebaoshiid:{}] [materials:{}] [player:{}]", new Object[] { mess, baoshiid, removebaoshiid, Arrays.toString(materials), player.getLogString() });
		}
		return new XILIAN_REMOVE_RES(message.getSequnceNum(), 0);
	}

	public XILIAN_PUT_RES handleXILIAN_PUT_REQ(RequestMessage message, Player player) {
		if (message instanceof XILIAN_PUT_REQ) {

			XILIAN_PUT_REQ req = (XILIAN_PUT_REQ) message;
			long baoshiid = req.getBaoShiId();
			long putbaoshiid = req.getPutbaoShiId();
			long materials[] = req.getMaterialId();
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(baoshiid);
			String mess = "--";
			if (ae != null && materials != null) {

				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, putbaoshiid)) {
					player.sendError(Translate.锁魂物品不能做此操作);
					handle_xilian_bug(baoshiid, player);
					return new XILIAN_PUT_RES(message.getSequnceNum(), 0);
				}
				/**
				 * 2015-06-12新加规则: 1.匣子第一个孔必须有宝石，其他剩余的格子才可以放入宝石
				 * 2.放入格子的宝石等级必须小于第一个格子宝石的等级
				 */
				if (materials.length > 0) {
					long firstId = materials[0];
					if (firstId <= 0) {
						player.sendError(Translate.镶嵌顺序不符2);
						handle_xilian_bug(baoshiid, player);
						return new XILIAN_PUT_RES(message.getSequnceNum(), 0);
					} else {
						ArticleEntity baoshiAE = ArticleEntityManager.getInstance().getEntity(firstId);
						if (baoshiAE == null) {
							player.sendError(Translate.镶嵌顺序不符2 + "!");
							handle_xilian_bug(baoshiid, player);
							return new XILIAN_PUT_RES(message.getSequnceNum(), 0);
						} else {
							ArticleEntity putAE = ArticleEntityManager.getInstance().getEntity(putbaoshiid);
							if (putAE != null) {
								Article a = ArticleManager.getInstance().getArticle(baoshiAE.getArticleName());
								Article putA = ArticleManager.getInstance().getArticle(putAE.getArticleName());
								if (a != null && putA != null) {
									if (a.getArticleLevel() < putA.getArticleLevel()) {
										player.sendError(Translate.镶嵌等级不符);
										handle_xilian_bug(baoshiid, player);
										return new XILIAN_PUT_RES(message.getSequnceNum(), 0);
									}
								}
							}

						}
					}
				}

				int cellInde = -1;
				for (int i = 0; i < materials.length; i++) {
					if (materials[i] == putbaoshiid) {
						cellInde = i;
						break;
					}
				}

				if (cellInde == -1) {
					logger.warn("[宝石匣子植入宝石] [失败:客户端数据不对] [baoshiid:{}] [putbaoshiid:{}] [materials:{}] [{}]", new Object[] { baoshiid, putbaoshiid, Arrays.toString(materials), player.getLogString4Knap() });
					return new XILIAN_PUT_RES(message.getSequnceNum(), 0);
				}

				BaoShiXiaZiData data = ArticleManager.getInstance().getxiLianData(player, baoshiid);
				if (data != null) {
					long oldmaterials[] = data.getIds();
					for (long id : oldmaterials) {
						if (id == putbaoshiid) {
							logger.warn("[宝石匣子植入宝石] [失败:相同id] [baoshiid:{}] [putbaoshiid:{}] [materials:{}] [oldMaterials:{}] [{}]", new Object[] { baoshiid, putbaoshiid, Arrays.toString(materials), (oldmaterials == null ? "nul" : Arrays.toString(oldmaterials)), player.getLogString4Knap() });
							return new XILIAN_PUT_RES(message.getSequnceNum(), 0);
						}
					}

					if (oldmaterials.length != materials.length) {
						logger.warn("[宝石匣子植入宝石] [失败:匣子格子长度有变化] [baoshiid:{}] [putbaoshiid:{}] [materials:{}] [oldMaterials:{}] [{}]", new Object[] { baoshiid, putbaoshiid, Arrays.toString(materials), (oldmaterials == null ? "nul" : Arrays.toString(oldmaterials)), player.getLogString4Knap() });
						return new XILIAN_PUT_RES(message.getSequnceNum(), 0);
					}

					if (oldmaterials[cellInde] > 0) {
						logger.warn("[宝石匣子植入宝石] [失败:匣子该位置已有宝石] [baoshiid:{}] [putbaoshiid:{}] [materials:{}] [oldMaterials:{}] [{}]", new Object[] { baoshiid, putbaoshiid, Arrays.toString(materials), (oldmaterials == null ? "nul" : Arrays.toString(oldmaterials)), player.getLogString4Knap() });
						return new XILIAN_PUT_RES(message.getSequnceNum(), 0);
					}

					ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(putbaoshiid, "放入宝石匣子", true);
					if (aee != null) {
						ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "放入宝石匣子删除", null);
						long newIds[] = new long[oldmaterials.length];
						for (int i = 0; i < newIds.length; i++) {
							newIds[i] = oldmaterials[i];
						}
						newIds[cellInde] = putbaoshiid;
						data.setIds(newIds);
						List<String> list = new ArrayList<String>();
						for (long id : newIds) {
							if (id > 0) {
								ArticleEntity baoshiAE = ArticleEntityManager.getInstance().getEntity(id);
								if (baoshiAE != null) {
									list.add(baoshiAE.getArticleName());
								}
							}
						}
						data.setNames(list.toArray(new String[] {}));
						ArticleEntityManager.baoShiXiLianEM.notifyNewObject(data);
						for (BaoShiXiaZiData d : baoShiXiaZiDatas) {
							if (d.getId() == data.getId()) {
								d.setIds(newIds);
								d.setNames(list.toArray(new String[] {}));
							}
						}
						String description = AritcleInfoHelper.generate(player, ae);
						QUERY_ARTICLE_INFO_RES res = new QUERY_ARTICLE_INFO_RES(req.getSequnceNum(), ae.getId(), description);
						player.addMessageToRightBag(res);
						logger.warn("[宝石匣子植入宝石] [成功] [baoshiid:{}] [putbaoshiid:{}] [materials:{}] [oldMaterials:{}] [newIds:{}] [list:{}] [{}]", new Object[] { baoshiid, putbaoshiid, Arrays.toString(materials), (oldmaterials == null ? "nul" : Arrays.toString(oldmaterials)), Arrays.toString(newIds), list, player.getLogString4Knap() });
						return new XILIAN_PUT_RES(message.getSequnceNum(), 1);
					} else {
						mess = "删除物品出错";
					}
				} else {
					mess = "宝石数据出错";
				}
			} else {
				mess = "获取物品出错";
			}
			logger.warn("[宝石匣子植入宝石] [失败:{}] [baoshiid:{}] [materials:{}] [{}]", new Object[] { mess, baoshiid, Arrays.toString(materials), player.getLogString4Knap() });
		}
		return new XILIAN_PUT_RES(message.getSequnceNum(), 0);
	}

	public XILIAN_CONFIRM_RES handleXILIAN_CONFIRM_REQ(RequestMessage message, Player player) {
		if (message instanceof XILIAN_CONFIRM_REQ) {
			XILIAN_CONFIRM_REQ req = (XILIAN_CONFIRM_REQ) message;
			long id = req.getBaoShiId();
			long materialId = req.getMaterialId();
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
			ArticleEntity mae = ArticleEntityManager.getInstance().getEntity(materialId);
			if (ae != null && mae != null) {
				if (!mae.getArticleName().equals(XILIAN_MATERIAL)) {
					player.sendError(Translate.translateString(Translate.背包中没有该物品, new String[][] { { Translate.ARTICLE_NAME_1, XILIAN_MATERIAL } }));
					return new XILIAN_CONFIRM_RES(message.getSequnceNum(), 0, new int[1]);
				}

				ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(materialId, "宝石匣子洗练删除", true);
				if (aee != null) {
					ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "铭刻删除", null);
					BaoShiXiaZiData data = ArticleManager.getInstance().getxiLianData(player, id);
					if (data != null) {
						int rcolors[] = getRandomCellColors(ae.getArticleName());
						int colors[] = data.getColors();
						data.setColors(rcolors);
						logger.warn("[宝石匣子洗练确认] [成功] [id:{}] [aename:{}] [colors:{}] [rcolors:{}] [{}]", new Object[] { id, ae.getArticleName(), (colors == null ? "" : Arrays.toString(colors)), (rcolors == null ? "" : Arrays.toString(rcolors)), player.getLogString4Knap() });
						return new XILIAN_CONFIRM_RES(req.getSequnceNum(), 1, rcolors);
					}
				}
			}
			logger.warn("[宝石匣子洗练确认] [失败] [ae:{}] [mae:{}] [id:{}] [materialId:{}] [{}]", new Object[] { ae == null, mae == null, id, materialId, player.getLogString4Knap() });
		}
		return new XILIAN_CONFIRM_RES(message.getSequnceNum(), 0, new int[1]);
	}

	public static String getArticleInfo4Log(long entityId, int num, String ft) {
		if (entityId < 0) {
			return ft + "SimpleClassName:" + null + "] [" + ft + "ID:" + entityId + "] [" + ft + "ArticleName:] [" + ft + "Num:" + num + "] [" + ft + "Color:-1] [" + ft + "Bind:false] [" + ft + "Overlap:false]";
		}
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(entityId);
		if (ae != null) {
			return ft + "SimpleClassName:" + ae.getClass().getSimpleName() + "] [" + ft + "ID:" + ae.getId() + "] [" + ft + "ArticleName:" + ae.getArticleName() + "] [" + ft + "Num:" + num + "] [" + ft + "Color:" + ae.getColorType() + "] [" + ft + "Bind:" + ae.isBinded() + "] [" + ft + "Overlap:" + ae.isOverlapFlag();
		}
		return ft + "SimpleClassName:" + null + "] [" + ft + "ID:" + entityId + "] [" + ft + "ArticleName:] [" + ft + "Num:" + num + "] [" + ft + "Color:-1] [" + ft + "Bind:false] [" + ft + "Overlap:false]";
	}

	public static Map<Class<?>, String> articleClassTypeMapped = new HashMap<Class<?>, String>();

	static {
		articleClassTypeMapped.put(Equipment.class, "装备");
	}

	public CareerPackageProps[] getAllCareerPackageProps() {
		return allCareerPackageProps;
	}

	public void setAllCareerPackageProps(CareerPackageProps[] allCareerPackageProps) {
		this.allCareerPackageProps = allCareerPackageProps;
	}

	public String getBaoShiColorFile() {
		return this.baoShiColorFile;
	}

	public void setBaoShiColorFile(String baoShiColorFile) {
		this.baoShiColorFile = baoShiColorFile;
	}

	public void doXiLianEquipment(Player player, long equipmentId, long materialId) {
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(equipmentId);
		ArticleEntity ae2 = ArticleEntityManager.getInstance().getEntity(materialId);
		if (ae != null && ae2 != null) {
			if (ae instanceof EquipmentEntity) {
				EquipmentEntity ee = (EquipmentEntity) ae;
				if (ee.getColorType() < 5) {
					ArticleManager.logger.warn(player.getLogString() + "[装备洗炼] [不足橙色] [id:" + ae.getId() + "] [color:" + ee.getColorType() + "]");
					player.sendError(Translate.橙色以下);
					return;
				}
				int propertyNumbers[] = ReadEquipmentExcelManager.getInstance().equipmentColorAdditionPropertyNumbers[ee.getColorType()];
				if (ee.getAddPropInfoShow().getIntValue() == propertyNumbers[propertyNumbers.length - 1]) {
					player.sendError(Translate.洗炼已满);
					ArticleManager.logger.warn(player.getLogString() + "[装备洗炼] [洗练已满] [id:" + ae.getId() + "]");
					return;
				}
				// 放入装备不绑定，但【洗炼符】为绑定，提示会绑定装备
				if (!ee.isBinded() && ae2.isBinded()) {
					ArticleManager.logger.info("[装备洗练] [使用了绑定洗炼符] [装备:" + ee.getArticleName() + "] [id:" + ee.getId() + "] [bind:" + ee.isBinded() + "]");
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(600);
					mw.setDescriptionInUUB(Translate.translateString(Translate.确定要开始洗炼吗绑定的, new String[][] { { Translate.ARTICLE_NAME_1, ae2.getArticleName() }, { Translate.ARTICLE_NAME_2, ae.getArticleName() } }));
					logger.debug("[" + player.getLogString() + "] [装备id:" + equipmentId + "] [洗练] [洗练二次确认:" + Translate.translateString(Translate.确定要开始洗炼吗绑定的, new String[][] { { Translate.ARTICLE_NAME_1, ae2.getArticleName() }, { Translate.ARTICLE_NAME_2, ee.getArticleName() } }) + "]");
					Option_Confirm_Xilian_Equipment o1 = new Option_Confirm_Xilian_Equipment(materialId, ee, true);
					o1.setText(Translate.确定);

					Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
					o2.setText(Translate.取消);
					mw.setOptions(new Option[] { o1, o2 });
					QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					player.addMessageToRightBag(res);
					return;
				}
				ArticleEntity aeDel = player.removeArticleEntityFromKnapsackByArticleId(materialId, "装备洗练删除", false);
				if (aeDel != null) {
					ArticleEntityManager aem = DefaultArticleEntityManager.getInstance();
					DefaultArticleEntityManager daem = (DefaultArticleEntityManager) aem;
					boolean result;
					try {
						result = daem.xilianEquipmentAddProp(ee, ee.isBinded());
						if (result) {
							player.sendError(Translate.洗炼成功);
							player.addMessageToRightBag(new XILIAN_EQUIPMENT_SURE_RES(GameMessageFactory.nextSequnceNum(), result));
							CoreSubSystem.notifyEquipmentChange(player, new EquipmentEntity[] { ee });
							if (logger.isWarnEnabled()) logger.warn("[装备洗练成功] [装备id:" + equipmentId + "] [materialId:" + materialId + "] [" + player.getLogString() + "]");
						} else {
							ArticleManager.logger.error(player.getLogString() + "[洗炼失败] [装备:" + ee.getArticleName() + "] [id:" + ee.getId() + "]");
						}
					} catch (Exception e) {
						ArticleManager.logger.error(player.getLogString() + "装备洗练", e);
						e.printStackTrace();
					}
				} else {
					String description = Translate.未放入装备或者洗炼符;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (logger.isWarnEnabled()) logger.warn("[装备洗练确认] [装备id:" + equipmentId + "] [materialId:" + materialId + "] [" + player.getLogString() + "] [" + description + "]");
				}

			} else {
				player.sendError(Translate.translateString(Translate.查询装备洗炼请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } }));
			}
		} else {
			ArticleManager.logger.warn(player.getLogString() + "[装备洗练确认] [装备id:" + equipmentId + "] [装备为空或者" + Translate.洗炼符 + "为空]");
		}

	}

	// /////////
	// public static void main(String[] args) throws Exception {
	// String fileName = "E://javacode_2//hg-1//game_mieshi_server//conf//game_init_config//article.xls";
	// File file = new File(fileName);
	// ArticleManager am = new ArticleManager();
	// am.test(file);
	// }
}
