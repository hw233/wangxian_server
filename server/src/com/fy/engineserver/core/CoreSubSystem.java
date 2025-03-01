package com.fy.engineserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.AliOrderInfo;
import com.fy.boss.client.AlipayArgs;
import com.fy.boss.client.BossClientService;
import com.fy.boss.client.SavingPageRecord;
import com.fy.boss.client.SavingRecord;
import com.fy.confirm.bean.GameActivity;
import com.fy.confirm.bean.Prize;
import com.fy.confirm.bean.User;
import com.fy.confirm.service.client.ClientService;
import com.fy.confirm.service.message.EXCHANGE_CONFIRMATION_RES;
import com.fy.confirm.service.server.DataManager;
import com.fy.engineserver.achievement.Achievement;
import com.fy.engineserver.achievement.AchievementConf;
import com.fy.engineserver.achievement.AchievementEntity;
import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.GameDataRecord;
import com.fy.engineserver.achievement.LeftMenu;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivityIntroduce;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.CheckAttribute;
import com.fy.engineserver.activity.VipHelpMess;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.base.TimesActivityManager;
import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.activity.clifford.manager.CliffordManager;
import com.fy.engineserver.activity.dice.DiceManager;
import com.fy.engineserver.activity.fairyBuddha.challenge.FairyChallengeManager;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyManager;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureEntity;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureNpc;
import com.fy.engineserver.activity.furnace.FurnaceManager;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchManager;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchScene;
import com.fy.engineserver.activity.pickFlower.FlowerNpc;
import com.fy.engineserver.activity.village.manager.VillageFightManager;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.activity.xianling.XianLingChallengeManager;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.articleEnchant.EnchantManager;
import com.fy.engineserver.articleEnchant.model.EnchantArticle;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.auction.service.AuctionManager;
import com.fy.engineserver.battlefield.BattleField;
import com.fy.engineserver.battlefield.concrete.BattleFieldLineupService;
import com.fy.engineserver.battlefield.concrete.BattleFieldManager;
import com.fy.engineserver.battlefield.concrete.BattleFieldStatData;
import com.fy.engineserver.billboard.Billboard;
import com.fy.engineserver.billboard.BillboardData;
import com.fy.engineserver.billboard.BillboardManager;
import com.fy.engineserver.billboard.Billboards;
import com.fy.engineserver.billboard.concrete.EquipmentBillboards;
import com.fy.engineserver.bourn.BournCfg;
import com.fy.engineserver.bourn.BournManager;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.carbon.devilSquare.instance.DevilSquare;
import com.fy.engineserver.chargeInfo.ChargeInfo;
import com.fy.engineserver.chat.ChatChannelManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.chuangong.ChuanGongManager;
import com.fy.engineserver.cityfight.CityFightManager;
import com.fy.engineserver.compose.ComposeManager;
import com.fy.engineserver.compose.model.TempArtilce;
import com.fy.engineserver.core.client.FunctionNPC;
import com.fy.engineserver.core.event.LeaveGameEvent;
import com.fy.engineserver.core.event.TransferGameEvent;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.AbstractResource;
import com.fy.engineserver.core.res.HelpManager;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BottlePropsEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.data.entity.SoulPithArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.equipments.SuitEquipment;
import com.fy.engineserver.datasource.article.data.equipments.Weapon;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeArticle;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model.ShouHunModel;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.DiscountCard;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.data.props.TaskProps;
import com.fy.engineserver.datasource.article.data.soulpith.GourdArticle;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticle;
import com.fy.engineserver.datasource.article.entity.client.BagInfo4Client;
import com.fy.engineserver.datasource.article.manager.AritcleInfoHelper;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.DiscountCardAgent;
import com.fy.engineserver.datasource.article.manager.UsingPropsAgent.PropsCategoryCoolDown;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.career.CareerThread;
import com.fy.engineserver.datasource.career.SkillInfo;
import com.fy.engineserver.datasource.language.MultiLanguageTranslateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.SkillInfoHelper;
import com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithTraceAndDirectionOrTarget;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffect;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutEffectAndQuickMove;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithMatrix;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithSummonNPC;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithTargetOrPosition;
import com.fy.engineserver.deal.Deal;
import com.fy.engineserver.deal.service.DealCenter;
import com.fy.engineserver.deal.service.concrete.DefaultDealCenter;
import com.fy.engineserver.deal.service.concrete.GameDealCenterProxy;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.downcity.city.CityAction;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.economic.AppStoreSavingLimit;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.economic.charge.ChargeRatio;
import com.fy.engineserver.economic.charge.card.CmccMMChargeMode;
import com.fy.engineserver.economic.thirdpart.migu.entity.SaleRecordManager;
import com.fy.engineserver.enterlimit.AndroidMsgManager;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.PhoneNumMananger;
import com.fy.engineserver.enterlimit.Player_Process;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordData;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.GameSubSystem;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.gm.broadcast.GMAutoSendMail;
import com.fy.engineserver.gm.feedback.FeedBackState;
import com.fy.engineserver.gm.feedback.Feedback;
import com.fy.engineserver.gm.feedback.Reply;
import com.fy.engineserver.gm.feedback.service.FeedbackManager;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.guozhan.Guozhan;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.honor.Honor;
import com.fy.engineserver.honor.HonorManager;
import com.fy.engineserver.hook.PlayerHookManager;
import com.fy.engineserver.hotspot.HotspotManager;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.lineup.EnterServerAgent;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.marriage.MarriageDownCity;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.masterAndPrentice.MasterPrentice;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_Cangku_AddCell;
import com.fy.engineserver.menu.Option_Cangku_AddCell2;
import com.fy.engineserver.menu.Option_Cangku_OpenCangku;
import com.fy.engineserver.menu.Option_Cangku_OpenCangku2;
import com.fy.engineserver.menu.Option_Cangku_ShedingPassword;
import com.fy.engineserver.menu.Option_Cangku_XiugaiPassword;
import com.fy.engineserver.menu.Option_CleanSkillOK;
import com.fy.engineserver.menu.Option_Exchange_Tencent_Silver;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.Option_discount;
import com.fy.engineserver.menu.charge.Option_KoreaTstoreCharge;
import com.fy.engineserver.menu.trade.Option_ShopSilverBuy;
import com.fy.engineserver.message.*;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newtask.NewDeliverTaskManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskCollectionProgressBar;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.prizes.TaskPrize;
import com.fy.engineserver.newtask.service.DeliverTaskManager;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.service.TaskConfig.PrizeType;
import com.fy.engineserver.notice.NoticeManager;
import com.fy.engineserver.oper.LocalServerInfo;
import com.fy.engineserver.operating.activities.ActivityItem;
import com.fy.engineserver.operating.activities.ActivityItemManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerTitles.PlayerTitle;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
import com.fy.engineserver.seal.SealDownCity;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.security.SecuritySubSystem;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.shortcut.ShortcutAgent;
import com.fy.engineserver.smith.RelationShipHelper;
import com.fy.engineserver.smith.waigua.WaiguaManager;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.soulpith.SoulPithManager;
import com.fy.engineserver.sprite.NewUserEnterServerService;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.SoulEquipment4Client;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.monster.MonsterManager;
import com.fy.engineserver.sprite.npc.Collectionable;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.sprite.npc.TaskCollectionNPC;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticle;
import com.fy.engineserver.sprite.pet2.GradePet;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.fy.engineserver.sprite.petdao.PetDao;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.stat.StatData;
import com.fy.engineserver.stat.StatDataUpdateManager;
import com.fy.engineserver.talent.FlyTalentManager;
import com.fy.engineserver.talent.TalentData;
import com.fy.engineserver.tengxun.TengXunDataManager;
import com.fy.engineserver.time.Timer;
import com.fy.engineserver.tournament.manager.TournamentManager;
import com.fy.engineserver.trade.TradeManager;
import com.fy.engineserver.trade.boothsale.service.BoothsaleManager;
import com.fy.engineserver.util.CommonDiskCacheManager;
import com.fy.engineserver.util.CommonSubSystem;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.RandomTool.RandomType;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.fy.engineserver.validate.OtherValidateManager;
import com.fy.engineserver.validate.ValidateAsk;
import com.fy.engineserver.validate.ValidateManager;
import com.fy.engineserver.vip.VipManager;
import com.fy.engineserver.vip.data.VipConf;
import com.fy.engineserver.vip.vipinfo.Record4OrderManager;
import com.fy.engineserver.vip.vipinfo.VipInfoRecordManager;
import com.fy.engineserver.worldmap.MapSingleMonsterInfo;
import com.fy.engineserver.worldmap.WorldMapArea;
import com.fy.engineserver.worldmap.WorldMapManager;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.fy.gamebase.help.HelpMessage;
import com.fy.gamegateway.message.GameMessageFactory;
import com.fy.gamegateway.tools.JacksonManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.stat.client.StatClientService;
import com.xuanzhi.stat.model.PlayerActionFlow;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.WordFilter;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

@CheckAttribute("CoreSubSystem")
public class CoreSubSystem implements GameSubSystem, AchievementConf, MConsole {

	public static Map<Long, Long> autoKnapsack = new ConcurrentHashMap<Long, Long>();

	public static final int GAME_DATA_PACKET_SIZE = 1024;

	public static boolean isStart = true;

	public static boolean openNotice = true;

	public static boolean FORCE_USE_DEFAULT_VIEW = true;

	public static int DEFAULT_PLAYER_VIEWWIDTH = 2000;

	public static int DEFAULT_PLAYER_VIEWHEIGHT = 1200;

	public static int APPSTORE_USER_CHARGE_LIMIT_LEVEL = 10;// appstore用户充值最低等级限制

	public static String[] BOURN_NAMES = Translate.classlevel;// { "练气", "筑基", "开光", "金丹", "元婴", "出窍", "合体", "渡劫", "寂灭", "大乘" };

	public String tagforbid[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", " ", "　", "　" };

	public static String[] allQuestion = Translate.密保问题;

	public static boolean 客户端密保容错 = true;

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/** 文化部审核 */
	@ChangeAble("文化部审核")
	public static boolean beCareful = false;

	// msdk测试审批模式
	@ChangeAble("msdk审核")
	public boolean TENCENT_TEST_MODE_SWITCH = false;
	
	public String ucWdjSavingUrl = "http://119.254.154.201:12111/ucsdkNewResult";
	public String oppoSavingUrl = "http://119.254.154.201:12111/oppoResult";
	public String u8SavingUrl = "http://119.254.154.201:12111/u8Result";
	public String qileSavingUrl = "http://119.254.154.201:12111/hyResult";
	public String huogameSavingUrl = "http://119.254.154.201:12111/jgResult";
	public String huogameSavingUrl2 = "http://119.254.154.201:12111/jgResult2";

	static {
		if (PlatformManager.getInstance().isPlatformOf(Platform.官方, Platform.腾讯)) {
			beCareful = false;
		}
		GameConstants gc = GameConstants.getInstance();
		if (gc != null) {
			if ("pan4".equals(gc.getServerName())) {
				beCareful = false;
			}
		}
	}

	public static HashSet<Long> beCarePlayer = new HashSet<Long>();

	// 所有功能开启状态 0 - 开启 ; 1 - 关闭
	@ChangeAble(value = "祈福是否开启,0-开启,1关闭")
	public static int qifuOpen = 0;

	@ChangeAble(value = "APP充值是否开启")
	public static boolean appChargeOpen = true;
	public static String notice = "暂时无法充值";

	/** 预创建开始时间 */
	private long beforehandCreateStartTime;
	/** 预创建结束时间 */
	private long beforehandCreateEndTime;

	private int[] beforehandCreateCountryNum;

	static {
		if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
			qifuOpen = 1;
		}
	}

	/**
	 * 判断是否含有禁用的标签字符
	 * 
	 * @param name
	 * @return
	 */
	public boolean tagValid(String name) {
		String aname = name.toLowerCase();
		for (String s : tagforbid) {
			if (aname.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}

	private static CoreSubSystem self;

	public static CoreSubSystem getInstance() {
		return self;
	}

	// public static Logger logger = Logger.getLogger(CoreSubSystem.class);
	public static Logger logger = LoggerFactory.getLogger(CoreSubSystem.class);
	// public static Logger gangWareHouseLog =
	// Logger.getLogger(GangWareHouse.class.getName());

	protected GameNetworkFramework framework;
	protected GameManager gm;
	protected ArticleManager am;
	protected ArticleEntityManager aem;
	protected PlayerManager playerManager;
	protected MonsterManager monsterManager;
	protected TaskManager taskManager;
	protected DealCenter dealCenter;
	protected MailManager mailManager;
	protected AuctionManager auctionManager;
	protected NPCManager npcManager;
	protected BossClientService bossClientService;
	protected ChatMessageService chatMessageService;
	protected ChatChannelManager chatChannelManager;
	protected StatClientService statClientService;
	protected File skillFile;
	protected BillboardManager billboardManager;
	protected StatDataUpdateManager statDataUpdateManager;
	protected ChargeInfo chargeInfo;
	protected BournManager bournManager;

	public static long koreaStoreCloseChargeTime = TimeTool.formatter.varChar19.parse("2013-06-04 00:00:00");

	// /** 关闭充值的渠道 */
	// private List<String> colseChargeChannel = new ArrayList<String>();

	public BournManager getBournManager() {
		return bournManager;
	}

	public void setBournManager(BournManager bournManager) {
		this.bournManager = bournManager;
	}

	protected int maxOnlineNum = 1800;

	public int getMaxOnlineNum() {
		return maxOnlineNum;
	}

	public void setMaxOnlineNum(int maxOnlineNum) {
		this.maxOnlineNum = maxOnlineNum;
	}

	public ChargeInfo getChargeInfo() {
		return chargeInfo;
	}

	public void setChargeInfo(ChargeInfo chargeInfo) {
		this.chargeInfo = chargeInfo;
	}

	// protected DefaultDatabaseManager resourceManager;
	//
	// public DefaultDatabaseManager getResourceManager() {
	// return resourceManager;
	// }
	//
	// public void setResourceManager(DefaultDatabaseManager resourceManager) {
	// this.resourceManager = resourceManager;
	// }

	public void setArticleManager(ArticleManager am) {
		this.am = am;
	}

	public ArticleManager getArticleManager() {
		return am;
	}

	public void setAem(ArticleEntityManager aem) {
		this.aem = aem;
	}

	public void setGameManager(GameManager gm) {
		this.gm = gm;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setGameNetworkFramework(GameNetworkFramework framework) {
		this.framework = framework;
	}

	public void setDealCenter(DealCenter dealCenter) {
		this.dealCenter = dealCenter;
	}

	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	public void setAuctionManager(AuctionManager auctionManager) {
		this.auctionManager = auctionManager;
	}

	public void setNpcManager(NPCManager npcManager) {
		this.npcManager = npcManager;
	}

	public void setBossClientService(BossClientService bossClientService) {
		this.bossClientService = bossClientService;
	}

	public void setChatMessageService(ChatMessageService chatMessageService) {
		this.chatMessageService = chatMessageService;
	}

	public void setChatChannelManager(ChatChannelManager chatChannelManager) {
		this.chatChannelManager = chatChannelManager;
	}

	public void setStatClientService(StatClientService statClientService) {
		this.statClientService = statClientService;
	}

	public static boolean closeCharge = false;
	public static String closeChargeNotice = "很抱歉，充值功能暂时未开放。";

	// private boolean inColseList(String channel) {
	// for (String colsed : colseChargeChannel) {
	// if (colsed != null && colsed.equalsIgnoreCase(channel)) {
	// return true;
	// }
	// }
	// return false;
	// }

	public String[] getInterestedMessageTypes() {
		return new String[] { "RESOURCE_DATA_REQ", "ICON_DATA_REQ", "PLAYER_ENTER_REQ", "ENTER_GAME_REQ", "LEAVE_CURRENT_GAME_REQ", "PLAYER_MOVE_REQ", "PLAYER_MOVETRACE_REQ", "SET_POSITION_REQ", "TOUCH_TRANSPORT_REQ", "TARGET_SKILL_REQ", "NONTARGET_SKILL_REQ", "USE_AURASKILL_REQ", "QUERY_ACTIVESKILL_REQ", "QUERY_SKILLINFO_REQ", "QUERY_SKILLINFO_PET_REQ", "ALLOCATE_SKILL_REQ", "CLEAN_SKILL_REQ", "QUERY_CAREER_INFO_REQ", "QUERY_ARTICLE_INFO_REQ", "QUERY_EQUIPMENT_TABLE_REQ", "QUERY_KNAPSACK_REQ", "QUERY_KNAPSACK_FB_REQ", "Fangbao_KNAPSACK_REQ", "ARTICLE_OPRATION_REQ", "HOOK_USE_PROP_REQ", "ARTICLE_OPRATION_MOVE_REQ", "ARTICLE_OPRATION_SPLIT_REQ", "REMOVE_EQUIPMENT_REQ", "SWITCH_SUIT_REQ", "EQUIPMENT_INLAY_UUB_REQ", "EQUIPMENT_INLAY_REQ", "EQUIPMENT_OUTLAY_REQ", "QUERY_EQUIPMENT_INSCRIPTION_REQ", "QUERY_EQUIPMENT_STRONG_REQ", "EQUIPMENT_STRONG_REQ", "QUERY_EQUIPMENT_JIANDING_REQ", "EQUIPMENT_JIANDING_REQ", "EQUIPMENT_INSCRIPTION_REQ", "QUERY_EQUIPMENT_PICKSTAR_REQ", "EQUIPMENT_PICKSTAR_REQ", "EQUIPMENT_INSERTSTAR_REQ", "QUERY_EQUIPMENT_INSERTSTAR_REQ", "EQUIPMENT_DEVELOP_REQ", "EQUIPMENT_DEVELOPUP_REQ", "QUERY_EQUIPMENT_BIND_REQ", "EQUIPMENT_BIND_REQ", "EQUIPMENT_UP_REQ", "QUERY_ARTICLE_COMPOSE_REQ", "ARTICLE_COMPOSE_REQ", "QUERY_FLOPAVAILABLE_REQ", "QUERY_FLOP_SIMPLE_REQ", "PICKUP_FLOP_REQ", "PREPARE_PLAY_DICE_REQ", "PLEASE_PLAY_DICE_REQ", "PLAY_DICE_REQ", "CPATAIN_ASSIGN_REQ", "PICKUP_MONEY_REQ", "QUERY_ARTICLE_REQ", "IMAGE_CLIENT_REQ", "DIALOG_WITHNPC_REQ", "CREATE_DEAL_REQ", "DEAL_AGREE_REQ", "DEAL_DISAGREE_REQ", "DEAL_ADD_ARTICLE_REQ", "DEAL_DELETE_ARTICLE_REQ", "DEAL_MOD_COINS_REQ", "DEAL_CANCEL_REQ", "DEAL_UPDATE_REQ", "DEAL_CREATED_REQ", "DEAL_LOCK_REQ", "QUERY_REPUTE_REQ", "WAREHOUSE_GET_REQ", "WAREHOUSE_GET_CARRY_REQ", "WAREHOUSE_INPUT_PASSWORD_REQ", "WAREHOUSE_SET_PASSWORD_REQ", "WAREHOUSE_MODIFY_PASSWORD_REQ", "WAREHOUSE_MOVE_ARTICLE_REQ", "WAREHOUSE_GETOUT_ARTICLE_REQ", "WAREHOUSE_ARRANGE_REQ", "SHOPS_NAME_GET_REQ", "SHOP_GET_REQ", "SHOP_BUY_REQ", "SHOP_BUYBACK_REQ", "SHOP_SELL_REQ", "SAVE_SHORTCUT_REQ", "QUERY_SHORTCUT_REQ", "QUERY_TOPO_DIAGRAM_REQ", "FUNCTION_NPC_REQ", "CHANGE_SERVER_REQ", "PLAYER_REVIVED_REQ", "PLAYER_REVIVED_INFO_REQ", "SOCIAL_RELATION_MANAGE_REQ", "GET_BLACKLIST_REQ", "ADD_BLACK_REQ", "DEL_BLACK_REQ", "NPC_REPAIR_QUERY_REQ", "NPC_REPAIR_REPAIR_REQ", "REPAIR_CARRY_REQ", "QUERY_EQUIPMENT_UPGRADE_REQ", "EQUIPMENT_UPGRADE_REQ", "QUERY_OTHER_PLAYER_REQ", "QIECUO_INVITE_REQ", "QIECUO_BE_INVITED_RESPONSE_REQ", "QUERY_EQUIPMENT_INLAY_REQ", "PICKUP_CAIJINPC_REQ", "GANG_CONFIRM_CREATE_REQ", "QUERY_SPRITE_TYPE_REQ", "QUERY_WINDOW_REQ", "OPTION_SELECT_REQ", "OPTION_INPUT_REQ", "GET_PLAYER_YUANBAO_REQ", "GANG_CREATE_REQ", "GANG_LIST_REQ", "GANG_INVITE_REQ", "GANG_DETAIL_REQ", "GANG_NOTICE_SET_REQ", "GANG_TITLE_SET_REQ", "GANG_KICKOUT_REQ", "GANG_FORBID_REQ", "GANG_LIST_MEMBER_REQ", "GANG_LEAVE_REQ", "GANG_INVITE_AGREE_REQ", "CARD_SAVING_REQ", "SAVING_HISTORY_REQ", "EXCHANGE_QUERY_MARKET_REQ", "EXCHANGE_PLACE_ORDER_REQ", "EXCHANGE_TAKE_MONEY_REQ", "EXCHANGE_QUERY_ORDER_REQ", "EXCHANGE_CANCEL_ORDER_REQ", "HEARTBEAT_CHECK_REQ", "PICKUP_ALLFLOP_REQ", "EQUIPMENT_COMPOUND_REQ", "EQUIPMENT_DRILL_REQ", "GANGWAREHOUSE_GET_REQ", "GANGWAREHOUSE_PUT_REQ", "GANGWAREHOUSE_TAKE_REQ", "GANGWAREHOUSE_ARRANGE_REQ", "CONTRIBUTION_REQ", "GANG_WARE_HOUSE_JOURNAL_REQ", "GET_BILLBOARD_MENU_REQ", "GET_BILLBOARD_REQ", "USER_LEAVE_SERVER_REQ", "EQUIPMENT_JOIN_BILLBOARD_REQ", "GET_CARD_CHARGE_INFO_REQ", "GET_SMS_CHARGE_INFO_REQ", "SMS_CHARGE_REQ", "NEW_RECOMMEND_REQ", "QUERY_ACTIVITIES_INFO_REQ", "QUERY_ACTIVITIES_DETAIL_REQ", "GET_WHITECONENT_REQ", "GET_PLAYER_BY_ID_REQ", "QUERY_NETWORKFLOW_REQ", "SET_QUEUE_READYNUM_REQ", "EQUIPMENT_RESET_REQ", "MASTERS_AND_PRENTICES_OPERATION_REQ",

		"CHANGE_PKMODE_REQ", "QUERY_CMCC_USER_LOGIN_REQ", "QUERY_CMCC_ChargeUp_REQ", "QUERY_CMCC_Balance_REQ", "QUERY_CMCC_ChargeUpRecord_REQ", "QUERY_CMCC_KEEPLOGIN_REQ", "QUERY_CMCC_AUTHORIZED_REQ", "QUERY_SUIT_INFO_REQ", "REMOVE_AVATAPROPS_REQ", "QUERY_WORLD_MAP_REQ", "QUERY_WORLD_MAP_AREAMAP_REQ", "QUERY_MAP_SEARCH_REQ", "QUERY_GAMEMAP_NPCMONSTER_REQ", "QUERY_GAMEMAP_MOVE_LIVINGOBJECT_REQ", "QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_REQ", "QUERY_WORLD_MAP_AREAMAP_BY_PLAYER_REQ", "COUNTRY_WANGZHEZHIYIN_REQ", "COUNTRY_QIUJIN_REQ", "COUNTRY_JINYAN_REQ", "COUNTRY_JIECHU_QIUJIN_REQ", "COUNTRY_JIECHU_JINYAN_REQ", "QUERY_COUNTRY_MAIN_PAGE_REQ", "QUERY_COUNTRY_VOTE_REQ", "COUNTRY_VOTE_REQ", "COUNTRY_COMMISSION_OR_RECALL_REQ", "COUNTRY_HONOURSOR_OR_CANCEL_REQ", "COUNTRY_COMMEND_OR_CANCEL_REQ", "QUERY_COUNTRY_COMMISSION_OR_RECALL_REQ", "COUNTRY_LINGQU_FENGLU_REQ", "COUNTRY_FAFANG_FENGLU_REQ", "QUERY_COUNTRY_WANGZHEZHIYIN_REQ", "QUERY_COUNTRY_YULINJUN_REQ", "QUERY_COUNTRY_JINKU_REQ", "QUERY_SEAL_REQ", "LEVELUP_REQ", "QUERY_FLOPNPC_REQ",

		"QUERY_HONOR_REQ", "QUERY_HONOR_INFO_REQ", "HONOR_OPRATION_REQ", "QUERY_MASTERS_AND_PRENTICES_REQ", "BATTLEFIELD_ACTION_REQ", "QUERY_BATTLEFIELDLIST_REQ", "QUERY_BATTLEFIELD_INFO_REQ", "EQUIPMENT_INLAY_RESET_REQ", "GANG_MAIL_CREATE_REQ", "OPEN_ARTICLE_COMPOSE_REQ", "INSERT_ARTICLE_COMPOSE_REQ", "QUERY_CROSS_BATTLEFIELDLIST_REQ", "CROSS_BATTLEFIELD_ACTION_REQ", "PLAYER_ENTER_DUELBATTLE_MONI_REQ", "MAGIC_WEAPON_NONTARGET_SKILL_REQ", "MAGIC_WEAPON_TARGET_SKILL_REQ", "QUERY_MAGIC_WEAPON_REQ", "QUERY_MAGIC_WEAPON_BY_INDEX_REQ", "QUERY_MAGIC_WEAPON_UPGRADE_REQ", "MAGIC_WEAPON_UPGRADE_REQ", "MAGIC_WEAPON_MIX_REQ", "QUERY_MAGIC_WEAPON_RECAST_REQ", "MAGIC_WEAPON_RECAST_REQ", "QUERY_MAGIC_TALISMAN_COMPOUND_REQ", "MAGIC_TALISMAN_INSERT_REQ", "MAGIC_TALISMAN_COMPOUND_REQ", "QUERY_MAGIC_WEAPON_BY_INDEX_REQ", "ANCILLARY_MAGIC_WEAPON_INSERT_REQ", "QUERY_ALL_ACHIEVEMENT_SERIES_REQ", "QUERY_ACHIEVEMENT_SERIES_REQ", "QUERY_ACHIEVEMENT_REQ", "RELEASE_MAGIC_WEAPON_REQ", "QUERY_MASTERS_INFO_REQ", "QUERY_PRENTICES_INFO_REQ", "PET_QUERY_BY_ARTICLE_REQ", "HELP_WINDOW_REQ", "PLAYER_ACTIVATION_SOUL_REQ", "PLAYER_SWITCH_SOUL_REQ",

		"QUERY_AUTO_HOOK_USE_PROP_REQ", "ACCEPT_CHUANGONG_ARTICLE_REQ", "APPLY_CHUANGONG_REQ", "FINISH_CHUANGONG_REQ", "FUCK_TRANSPORT_GAME", "BOTTLE_OPEN_REQ", "BOTTLE_PICK_ARTICLE_REQ", "TAKE_TASK_BY_ARTICLE_REQ", "QUERY_INVALID_TIME_REQ", "EXPEND_KNAPSACK_REQ",

		"BOURN_LEVELUP_REQ", "BOURN_ZEZEN_REQ", "BOURN_REFRESH_TASK_REQ", "BOURN_INFO_REQ", "BOURN_OF_TRAINING_REQ", "BOURN_OF_PURIFY_REQ", "CLIFFORD_START_REQ", "CLIFFORD_REQ", "CLIFFORD_LOOKOVER_REQ",

		"WANNA_COLLECTION_REQ", "EXCHANGE_CONFIRMACTION_CODE_REQ", "QUERY_FEEDBACK_LIST_REQ", "QUERY_SPECIAL_FEEDBACK_REQ", "REPLY_SPECIAL_FEEDBACK_REQ", "JUDGE_SPECIAL_FEEDBACK_REQ", "CREATE_FEEDBACK_REQ",

		"GET_DESCRIPTION_BY_ARTICLE_REQ", "HORSE_RIDE_REQ", "GET_EFFECT_NOTICE_REQ", "OBTAIN_NOTICE_SIVLER_REQ", "TOURNAMENT_REWARD_REQ", "TOURNAMENT_TAKE_REWARD_REQ", "TOURNAMENT_REWARD_SELF_REQ", "VIP_REQ", "VIP_PULL_FRIEND_REQ", "GET_VIP_REWARD_REQ", "ACTIVITY_LIST_REQ",

		"REQUEST_ACHIEVEMENT_LIST_REQ", "REQUEST_ACHIEVEMENT_DONE_REQ", "REQUEST_ONE_DONE_ACHIEVEMENT_REQ", "GET_BILLBOARD_MENUS_REQ", "GET_ONE_BILLBOARD_REQ", "GET_PLAYERTITLES_REQ", "SETDEFAULT_PLAYERTITLE_REQ", "SHOP_GET_BY_QUERY_CONDITION_REQ", "QUERY_COUNTRY_GONGGAO_REQ", "QUERY_COUNTRY_QIUJIN_JINYAN_REQ",

		"TAKE_SEAL_TASK_REQ", "SET_PWD_PROTECT_REQ", "QUERY_PWD_PROTECT_REQ", "QUERY_CHARGE_MONEY_CONFIGURE_REQ", "CHARGE_REQ", "QUERY_CHARGE_LIST_REQ", "TENCENT_GET_CHARGE_ORDER_REQ", "QUERY_ORDER_LIST_REQ", "ORDER_STATUS_CHANGE_REQ",

		"AY_ARGS_REQ", "A_GET_ORDERID_REQ", "JIU1_GET_CHARGE_ORDER_REQ", "YINGYONGHUI_GET_CHARGE_ORDER_REQ", "SET_VIEW_PORT", "LENOVO_GET_CHARGE_ORDER_REQ", "APPSTORE_SAVING_VERIFY_REQ", "UCSDK_NOTICE_CHARGE_REQ", "DEFAULT_GET_CHARGEORDER_REQ", "IAPPPAY_GET_CHARGE_ORDER_REQ", "QUERY_VIP_DISPLAY_REQ", 
		"DOWNCITY_PLAYER_STATUS_CHANGE_REQ", "DOWNCITY_CREATE_REQ", "QUERY_TUNSHI_REQ", "TUNSHI_REQ", "QUERY_XILIAN_REQ", "XILIAN_REQ", "QILING_INLAY_REQ", "QILING_OUTLAY_REQ", "QUERY_LIANQI_REQ", "LIANQI_REQ", "QUERY_EQUIPMENT_QILING_REQ", "QUERY_KNAPSACK_QILING_REQ", "KNAPSACK_QILING_MOVE_REQ", "GET_CHARGE_ORDER_MULTIIO_REQ",

		"GET_SOME4ANDROID_REQ", "GET_SOME4ANDROID_1_REQ", "NEW_QUERY_CAREER_INFO_REQ", "QUERY_XINFA_SKILL_IDLIST_REQ", "PLAYER_TITLES_List_REQ", "QUERY_TITLE_PRODUCE_REQ",

		"PLAYER_HOOK_INFO_REQ", "ARTICLEPROTECT_BLOCK_REQ", "REFRESH_CROSS_SHOP_REQ", "ARTICLEPROTECT_UNBLOCK_REQ", "ARTICLEPROTECT_OTHERMSG_REQ", "ANDROID_PROCESS_REQ", "GET_RMB_REWARDMSG_REQ", 
		"GET_RMBREWARD_REQ", "NEW_JIANDING_OK_REQ", "GET_WEEKACTIVITY_REQ", "GET_WEEK_REWARD_REQ", "TRY_BING_PHONENUM_REQ", "TRY_SEND_PHONTNUM_REQ", "BING_PHONENUM_REQ", "TRY_UNBING_PHONENUM_REQ",
		"TRY_SEND_UNBIND_REQ", "UNBIND_PHONENUM_REQ", "PHONENUM_ASK_REQ", "SOUL_MESSAGE_REQ", "SOUL_UPGRADE_REQ", "LOGIN_ACTIVITY_GET_REQ", "NEW_OPTION_SELECT_REQ", "GET_WEEKANDMONTH_INFO_REQ", 
		"GET_WEEKMONTH_REWARD_REQ", "VALIDATE_INFO_RES", "TRY_CLIENT_MSG_A_REQ", "TRY_CLIENT_MSG_I_REQ", "TRY_CLIENT_MSG_W_REQ", "CLIENT_MSG_A_REQ", "CLIENT_MSG_I_REQ", "CLIENT_MSG_W_REQ", "GET_SHOP_REQ", 
		"QUERY_WORLD__XJ_MAP_REQ", "IS_XJ_MAP_REQ", "NEW_EQUIPMENT_STRONG_REQ", "QUERY_NEW_EQUIPMENT_STRONG_REQ", "QUERY_NEW_EQUIPMENT_STRONG2_REQ", "SYNC_MAGICWEAPON_FOR_KNAPSACK_REQ", "QUERY_SHENSHI_REQ",
		"CONFIRM_SHENSHI_REQ", "QUERY_MAGICWEAPON_STRONG_REQ", "MAGICWEAPON_STRONG_REQ", "QUERY_MAGICWEAPON_EAT_REQ", "CONFIRM_MAGICWEAPON_EAT_REQ", "QUERY_MAGICWEAPON_HIDDLE_PROP_REQ", "JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ", 
		"REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ", "GOD_EQUIPMENT_UPGRADE_SURE_REQ", "GOD_EQUIPMENT_UPGRADE_REQ", "QUERY_ARTICLE_EXCHANGE_REQ", "CONFIRM_ARTICLE_EXCHANGE_REQ", "CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ", "COLLECT_MATERIAL_FOR_BOSS_REQ", 
		"SEAL_TASK_INFO_REQ", "ACTIVITY_FIRST_PAGE_REQ", "GET_ACTIVITY_INFO_REQ", "SEAL_TASK_STAT_REQ", "NOTICE_ACTIVITY_STAT_REQ", "NOTICE_ACTIVITY_BUTTON_STAT_REQ", "VIP_2_REQ", "GET_VIP_NOTICE_INFO_REQ", "REFRESH_SHOP_GOODS_REQ", "CAN_REFRESH_SHOP_NAME_REQ", 
		"ACTIVITY_FIRST_PAGE2_REQ", "GET_NEW_SHOP_REQ", "ACTIVITY_EXTEND_REQ", "GET_NEWPLAYERCAREERS_REQ", "GET_SHOUKUI_COMMENSKILLS_REQ", "JUBAO_PLAYER_REQ", "XILIAN_PAGE_REQ", "XILIAN_CONFIRM_REQ", "XILIAN_PUT_REQ", "XILIAN_REMOVE_REQ", "MORE_ARGS_ORDER_STATUS_CHANGE_REQ", 
		"SHOOK_DICE_RESULT_REQ", "DICE_TRANSFER_REQ", "WOLF_SIGN_UP_SURE_REQ", "WOLF_ENTER_REQ", "WOLF_SIGNUP_NUM_REQ", "WOLF_USE_SKILL_REQ", "DICE_SIGN_UP_SURE_REQ", "WOLF_SIGN_UP_QUERY_REQ", "NEW_WAREHOUSE_ARRANGE_REQ", "NEW_WAREHOUSE_INPUT_PASSWORD_REQ", "QUERY_NEW_EQUIPMENT_STRONG3_REQ",
		"QUERY_PLAYER_CREATE_TIME_REQ" ,"MONTH_CARD_FIRST_PAGE_REQ","MONTH_CARD_BUY_REQ","LOGIN_REWARD_REQ","GET_LOGIN_REWARD_REQ","FIRST_CHARGE_STATE_REQ","FIRST_CHARGE_REQ","ACTIVITY_DO_REQ","DAY_PACKAGE_OF_RMB_REQ","CHARGE_LIST_REQ","STAR_HELP_REQ","STAR_MONEY_REQ","STAR_GO_REQ"};
	}

	public String getName() {
		return "CoreSubSystem";
	}

	public static String keyprefix = "LocalServer-";

	private String serverOpentimeFile;

	public String getServerOpentimeFile() {
		return serverOpentimeFile;
	}

	public void setServerOpentimeFile(String serverOpentimeFile) {
		this.serverOpentimeFile = serverOpentimeFile;
	}

	private void readServerInfo() throws Exception {
		// 读取excel表，将开服时间放入到ddc中
		// 在页面上录入数据到ddc中
		File f = new File(serverOpentimeFile);
		if (!f.exists()) {
			logger.warn("[初始化加载服务器开服信息] [没有找到表] [" + serverOpentimeFile + "]");
			return;
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				if (logger.isDebugEnabled()) logger.debug("[初始化加载服务器开服信息] [有空行]");
				continue;
			}
			HSSFCell serverNameCell = row.getCell(0);
			HSSFCell openTimeCell = row.getCell(1);

			try {
				String serverName = (serverNameCell.getStringCellValue());
				String openDateStr = openTimeCell.getStringCellValue();

				if (!StringUtils.isEmpty(serverName) && !StringUtils.isEmpty(openDateStr)) {
					serverName = serverName.trim();
					openDateStr = openDateStr.trim();

					if (GameConstants.getInstance().getServerName().equalsIgnoreCase(serverName)) {
						LocalServerInfo localServerInfo = new LocalServerInfo();

						// 格式化时间
						localServerInfo.serverOpenTime = DateUtil.parseDate(openDateStr, "yyyy-MM-dd HH:mm:ss").getTime();
						localServerInfo.serverName = GameConstants.getInstance().getServerName();
						localServerInfo.canCreatePlayer = true;

						// 存储到diskcache中
						CommonDiskCacheManager commonDiskCacheManager = CommonDiskCacheManager.getInstance();

						if (commonDiskCacheManager != null) {
							DiskCache diskCache = commonDiskCacheManager.getDiskCache();
							String key = keyprefix + serverName;
							if (diskCache.get(key) == null) {
								diskCache.put(key, localServerInfo);
								logger.warn("[初始化加载服务器开服信息] [成功] [" + serverName + "] [" + openDateStr + "]");
							} else {
								logger.warn("[初始化加载服务器开服信息] [缓存中已经存在相同条目] [不更新] [" + serverName + "] [" + openDateStr + "]");
							}
							break;
						} else {
							logger.error("[初始化加载服务器开服信息] [失败] [没有获取到diskcache] [" + serverName + "] [" + openDateStr + "]");
							break;
						}
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("[初始化加载服务器开服信息] [服务器名称和本地配置服务器名称不相等] [" + serverName + "] [" + openDateStr + "] [" + GameConstants.getInstance().getServerName() + "]");
						}
					}
				} else {
					logger.error("[初始化加载服务器开服信息] [出现加载服务器信息不完全的情况] [" + serverName + "] [" + openDateStr + "] [" + GameConstants.getInstance().getServerName() + "]");
				}

			} catch (Exception e) {
				logger.error("[初始化加载服务器开服信息] [读取开服信息表] [出现未知异常] [" + GameConstants.getInstance().getServerName() + "]", e);
			}
		}

	}

	public static boolean isJudgeCanEnter = false;

	public boolean judgeCanEnterServer() {
		if (isJudgeCanEnter) {
			// 读取cache
			DiskCache diskCache = CommonDiskCacheManager.getInstance().getDiskCache();
			LocalServerInfo localServerInfo = (LocalServerInfo) diskCache.get(keyprefix + GameConstants.getInstance().getServerName());
			if (localServerInfo != null) {
				if (localServerInfo.serverOpenTime > 0) {
					long now = System.currentTimeMillis();
					if (now >= localServerInfo.serverOpenTime) {
						if (logger.isInfoEnabled()) {
							logger.info("[判断是否可以进入服务器] [成功] [可以进入服务器] [" + now + "] [" + localServerInfo.serverOpenTime + "]");
						}
						return true;
					} else {
						if (logger.isInfoEnabled()) {
							logger.info("[判断是否可以进入服务器] [成功] [不可以进入服务器] [" + now + "] [" + localServerInfo.serverOpenTime + "]");
						}
						return false;
					}
				} else {
					logger.warn("[判断是否可以进入服务器] [失败] [读取服务器信息中无开服时间] [不可以进入服务器] [" + localServerInfo.serverOpenTime + "]");
					return false;
				}
			} else {
				logger.warn("[判断是否可以进入服务器] [失败] [Cache中无开服信息] [不可以进入服务器]");
				return false;
			}
		} else {
			logger.warn("[判断是否可以进入服务器] [不走判断逻辑] [允许玩家进入游戏服]");
			return true;
		}
	}

	public void init() throws Exception {
		

		self = this;
		// colseChargeChannel.add("QQ_MIESHI");
		MConsoleManager.register(this);

		try {
			// readServerInfo();
			initCallAddress();
			loadBeforehandCreateInfo();
		} catch (Exception e) {
			logger.error("[初始化加载服务器开服信息] [出现未知异常]", e);
		}
		ServiceStartRecord.startLog(this);
	}
	
	public void initCallAddress(){
		String serverName = GameConstants.getInstance().getServerName();
		if(serverName != null && serverName.equals("潘多拉星")){
			ucWdjSavingUrl = "http://124.248.40.21:9110/mieshi_game_boss/ucwdjResult";
		}
	}

	/**
	 * 加载预创建角色信息
	 */
	private void loadBeforehandCreateInfo() {
		Object startObj = CommonDiskCacheManager.getInstance().getProps(CommonSubSystem.预创建角色开始时间);
		Object endObj = CommonDiskCacheManager.getInstance().getProps(CommonSubSystem.预创建角色结束时间);
		Object countryNumObj = CommonDiskCacheManager.getInstance().getProps(CommonSubSystem.预创建角色国家人数设定);
		if (startObj == null) {
			beforehandCreateStartTime = 0;
		} else {
			beforehandCreateStartTime = Long.valueOf(String.valueOf(startObj));
		}
		if (endObj == null) {
			beforehandCreateEndTime = 0;
		} else {
			beforehandCreateEndTime = Long.valueOf(String.valueOf(endObj));
		}
		if (countryNumObj == null) {
			beforehandCreateCountryNum = new int[] { 300, 300, 300, 300 };// 默认值300
			setBeforehandCreateCountryNum(beforehandCreateCountryNum);
		} else {
			beforehandCreateCountryNum = (int[]) countryNumObj;
		}

		logger.warn("[预创建角色] [初始化成功] [开始时间:" + TimeTool.formatter.varChar19.format(beforehandCreateStartTime) + "] [结束时间:" + TimeTool.formatter.varChar19.format(beforehandCreateEndTime) + "] [各个国家人数限制:" + Arrays.toString(beforehandCreateCountryNum) + "]");
	}

	public boolean timeCanBeforehandCreate(long time) {
		return beforehandCreateStartTime <= time && beforehandCreateEndTime >= time;
	}

	public List<Connection> waitingEnterGameConnections = Collections.synchronizedList(new ArrayList<Connection>());

	/**
	 * TODO 如果此方法已经处理了请求消息，但是不需要返回响应，那么最后也会return null<br>
	 * 这样一来，调用者就无法区分出消息是否已经被处理了
	 */
	/*
	 * (non-Javadoc)
	 * @see
	 * com.fy.engineserver.gateway.GameSubSystem#handleReqeustMessage(com
	 * .xuanzhi.tools.transport.Connection,
	 * com.xuanzhi.tools.transport.RequestMessage)
	 */
	/*
	 * (non-Javadoc)
	 * @see com.fy.engineserver.gateway.GameSubSystem#handleReqeustMessage(com.xuanzhi.tools.transport.Connection, com.xuanzhi.tools.transport.RequestMessage)
	 */
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Player player = (Player) conn.getAttachmentData("Player");
		if (player != null) {
			player.setLastRequestTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());

			try {
				WaiguaManager wm = WaiguaManager.getInstance();
				if (wm.isOpenning()) {
					if (wm.isForbid(player.getId())) {
						conn.close();
						WaiguaManager.logger.warn("[因为外挂行为被封] [" + player.getLogString() + "]");
						return null;
					}
					wm.notifyPlayerMessage(player, message.getTypeDescription(), message.getSequnceNum());
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		String username = (String) conn.getAttachmentData(SecuritySubSystem.USERNAME);
		Passport passport = (Passport) conn.getAttachment();

		if (passport == null) {
			if (message instanceof HEARTBEAT_CHECK_REQ) {
				HEARTBEAT_CHECK_RES res = new HEARTBEAT_CHECK_RES(message.getSequnceNum(), SystemTime.currentTimeMillis(), (short) 0);
				return res;
			}
			// logger.warn("[警告:连接上没有账号信息] [断开连接] [" + message.getTypeDescription() + "]");
			if (logger.isWarnEnabled()) logger.warn("[警告:连接上没有账号信息] [断开连接] [{}]", new Object[] { message.getTypeDescription() });
			throw new ConnectionException("警告:连接上没有账号信息，断开连接");

		}
		// if (message.getClass().getName().toUpperCase().contains("BOURN_")) {
		// System.out.println("[收到协议:]" + message.getClass());
		// }
		if (message instanceof RESOURCE_DATA_REQ) {
			RESOURCE_DATA_REQ req = (RESOURCE_DATA_REQ) message;

			String clientType = req.getClientType();
			byte resType = req.getResType();
			String resName = req.getKeyName();
			int oldversion = req.getVersion();

			final Connection player_conn = conn;
			ResourceManager rm = ResourceManager.getInstance();
			AbstractResource p = rm.findNewResource(clientType, resType, resName);
			if (p != null) {
				if (p.getVersion() > oldversion) {
					// 需要更新资源
					byte bytes[] = p.getGameBinaryData();
					int packetNum = bytes.length / GAME_DATA_PACKET_SIZE;
					if (bytes.length - packetNum * GAME_DATA_PACKET_SIZE > 0) {
						packetNum++;
					}
					RESOURCE_DATA_RES res = new RESOURCE_DATA_RES(req.getSequnceNum(), resType, resName, (byte) 0, "OK", bytes.length, packetNum);
					final byte game_data[] = bytes;
					final int packet_num = packetNum;

					framework.sendMessage(player_conn, res, "RESOURCE_DATA_RES");

					for (int index = 0; index < packet_num; index++) {
						int len = game_data.length - GAME_DATA_PACKET_SIZE * index;
						if (len > GAME_DATA_PACKET_SIZE) {
							len = GAME_DATA_PACKET_SIZE;
						}
						byte data[] = new byte[len];
						System.arraycopy(game_data, GAME_DATA_PACKET_SIZE * index, data, 0, data.length);
						RESOURCE_DATA_REQ_PROCESS dataReq = new RESOURCE_DATA_REQ_PROCESS(GameMessageFactory.nextSequnceNum(), resType, resName, GAME_DATA_PACKET_SIZE * index, data);
						framework.sendMessage(player_conn, dataReq, "resource_data_packet_" + index + "," + packet_num);

					}
					if (logger.isWarnEnabled()) logger.warn("[{}] [RESOURCE_DATA_REQ] [{}] [{}] [{}] [packet:{}] [oldver:{}] [newver:{}] [size:{}] [{}] [player:{}] [{}ms]", new Object[] { getName(), (res.getResult() == 0 ? "success" : "failed"), res.getResultString(), resName, res.getPacketNum(), oldversion, p.getVersion(), res.getDataLength(), username, (player == null ? "" : player.getName()), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				} else {
					// 客户端已经最新
					RESOURCE_DATA_RES res = new RESOURCE_DATA_RES(req.getSequnceNum(), resType, resName, (byte) 0, "OK", 0, 0);
					framework.sendMessage(player_conn, res, "RESOURCE_DATA_RES");
				}
			} else {
				// 资源不存在
				RESOURCE_DATA_RES res = new RESOURCE_DATA_RES(req.getSequnceNum(), resType, resName, (byte) 1, Translate.translateString(Translate.数据不存在, new String[][] { { Translate.INDEX_1, rm.getResTypeName(resType) }, { Translate.INDEX_2, resName } }), 0, 0);
				framework.sendMessage(player_conn, res, "RESOURCE_DATA_RES");
				if (logger.isWarnEnabled()) logger.warn("[{}] [RESOURCE_DATA_REQ] [failed] [{}] [{}] [packet:{}] [oldver:{}] [size:{}] [{}] [player:{}] [{}ms]", new Object[] { getName(), res.getResultString(), resName, res.getPacketNum(), oldversion, res.getDataLength(), username, (player == null ? "" : player.getName()), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
			}
			return null;
		} else if (message instanceof HELP_WINDOW_REQ) {
			HELP_WINDOW_REQ req = (HELP_WINDOW_REQ) message;
			HelpManager hm = HelpManager.getInstance();
			HelpMessage[] ms = hm.get(req.getHelpType(), req.getId(), req.getScreenW(), req.getScreenH());

			HELP_WINDOW_RES res = new HELP_WINDOW_RES(req.getSequnceNum(), req.getHelpType(), req.getId(), ms);
			if (logger.isWarnEnabled()) logger.warn("[{}] [HELP_WINDOW_REQ] [{}] [player:{}] [{}ms]", new Object[] { getName(), username, (player == null ? "" : player.getName()), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
			return res;
		} else if (message instanceof ENTER_GAME_REQ) {
			return handle_ENTER_GAME_REQ(conn, player, message, username, startTime);
		} else if (message instanceof PLAYER_MOVE_REQ && player != null) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof PLAYER_MOVETRACE_REQ && player != null) {
			PLAYER_MOVETRACE_REQ req = (PLAYER_MOVETRACE_REQ) message;
			if (player.isBoothState() && req.getMoveTrace4Client().getType() == player.getClassType()) {
				player.sendError(Translate.text_boothsale_012);
				BoothsaleManager.logger.warn("[摆摊移动] [外挂] [{}]", new Object[] { player.getLogString() });
				BoothsaleManager.getInstance().msg_cancelBoothSale(player);
				return null;
			}
			// player.notifyReceivePlayerMove((PLAYER_MOVETRACE_REQ) message);
			pushMessageToGame(player, message, null);
		} else if (message instanceof SET_POSITION_REQ && player != null) {

			player.notifyReceivePlayerMove((SET_POSITION_REQ) message);

			pushMessageToGame(player, message, null);
		} else if (message instanceof TOUCH_TRANSPORT_REQ && player != null) {
			// logger.warn("[TOUCH_TRANSPORT_REQ] [" + player.getId() + "] [" + player.getName() + "] [" + player.getGame() + "]");
			if (logger.isWarnEnabled()) logger.warn("[TOUCH_TRANSPORT_REQ] [{}] [{}] [{}]", new Object[] { player.getId(), player.getName(), player.getGame() });
			pushMessageToGame(player, message, null);
		} else if (message instanceof LEAVE_CURRENT_GAME_REQ && player != null) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof TARGET_SKILL_REQ && player != null) {
			pushMessageToGame(player, message, null);
			if (player.getTimerTaskAgent() != null) {
				player.getTimerTaskAgent().notifyAttacking();
			}
		} else if (message instanceof NONTARGET_SKILL_REQ && player != null) {
			pushMessageToGame(player, message, null);
			if (player.getTimerTaskAgent() != null) {
				player.getTimerTaskAgent().notifyAttacking();
			}
		} else if (message instanceof USE_AURASKILL_REQ && player != null) {

			int auraSkillID = ((USE_AURASKILL_REQ) message).getSkillID();
			if (auraSkillID < 0) {
				player.closeAura();
			} else {
				player.openAura(auraSkillID);
			}
		} else if (message instanceof QUERY_ACTIVESKILL_REQ) {

			CareerManager cm = CareerManager.getInstance();
			int ids[] = new int[Weapon.WEAPONTYPE_NAME.length];
			for (int i = 0; i < ids.length; i++) {
				CommonAttackSkill skill = cm.getCommonAttackSkill((byte) i);
				if (skill != null) {
					ids[i] = skill.getId();
				}
			}

			SEND_ACTIVESKILL_REQ req1 = new SEND_ACTIVESKILL_REQ(GameMessageFactory.nextSequnceNum(), ids, (CommonAttackSkill[]) cm.getSkillsByClass(CommonAttackSkill.class), (SkillWithoutEffect[]) cm.getSkillsByClass(SkillWithoutEffect.class), (SkillWithoutEffectAndQuickMove[]) cm.getSkillsByClass(SkillWithoutEffectAndQuickMove.class), new SkillWithoutTraceAndWithRange[0],// )
			// cm.getSkillsByClass(SkillWithoutTraceAndWithRange.class),
			new SkillWithoutTraceAndWithTargetOrPosition[0],// ) cm.getSkillsByClass(SkillWithoutTraceAndWithTargetOrPosition.class),
			new SkillWithoutTraceAndWithSummonNPC[0], new SkillWithTraceAndDirectionOrTarget[0], new SkillWithoutTraceAndWithMatrix[0],// cm.getSkillsByClass(SkillWithoutTraceAndWithMatrix.class),
			new SkillWithoutTraceAndOnTeamMember[0]);
			player.addMessageToRightBag(req1);

			req1 = new SEND_ACTIVESKILL_REQ(GameMessageFactory.nextSequnceNum(), ids, new CommonAttackSkill[0],// cm.getSkillsByClass(CommonAttackSkill.class),
			new SkillWithoutEffect[0], // cm.getSkillsByClass(SkillWithoutEffect.class),
			new SkillWithoutEffectAndQuickMove[0],// cm.getSkillsByClass(SkillWithoutEffectAndQuickMove.class),
			(SkillWithoutTraceAndWithRange[]) cm.getSkillsByClass(SkillWithoutTraceAndWithRange.class), new SkillWithoutTraceAndWithTargetOrPosition[0],// )
			// cm.getSkillsByClass(SkillWithoutTraceAndWithTargetOrPosition.class),
			new SkillWithoutTraceAndWithSummonNPC[0], new SkillWithTraceAndDirectionOrTarget[0], new SkillWithoutTraceAndWithMatrix[0],// cm.getSkillsByClass(SkillWithoutTraceAndWithMatrix.class),
			new SkillWithoutTraceAndOnTeamMember[0]);
			player.addMessageToRightBag(req1);

			req1 = new SEND_ACTIVESKILL_REQ(GameMessageFactory.nextSequnceNum(), ids, new CommonAttackSkill[0],// cm.getSkillsByClass(CommonAttackSkill.class),
			new SkillWithoutEffect[0], // cm.getSkillsByClass(SkillWithoutEffect.class),
			new SkillWithoutEffectAndQuickMove[0],// cm.getSkillsByClass(SkillWithoutEffectAndQuickMove.class),
			new SkillWithoutTraceAndWithRange[0],// cm.getSkillsByClass(SkillWithoutTraceAndWithRange.class),
			(SkillWithoutTraceAndWithTargetOrPosition[]) cm.getSkillsByClass(SkillWithoutTraceAndWithTargetOrPosition.class), new SkillWithoutTraceAndWithSummonNPC[0], new SkillWithTraceAndDirectionOrTarget[0], new SkillWithoutTraceAndWithMatrix[0],// cm.getSkillsByClass(SkillWithoutTraceAndWithMatrix.class),
			new SkillWithoutTraceAndOnTeamMember[0]);
			player.addMessageToRightBag(req1);

			SkillWithoutTraceAndWithMatrix matr[] = (SkillWithoutTraceAndWithMatrix[]) cm.getSkillsByClass(SkillWithoutTraceAndWithMatrix.class);
			req1 = new SEND_ACTIVESKILL_REQ(GameMessageFactory.nextSequnceNum(), ids, new CommonAttackSkill[0],// cm.getSkillsByClass(CommonAttackSkill.class),
			new SkillWithoutEffect[0], // cm.getSkillsByClass(SkillWithoutEffect.class),
			new SkillWithoutEffectAndQuickMove[0],// cm.getSkillsByClass(SkillWithoutEffectAndQuickMove.class),
			new SkillWithoutTraceAndWithRange[0],// cm.getSkillsByClass(SkillWithoutTraceAndWithRange.class),
			new SkillWithoutTraceAndWithTargetOrPosition[0],// ) cm.getSkillsByClass(SkillWithoutTraceAndWithTargetOrPosition.class),
			new SkillWithoutTraceAndWithSummonNPC[0], new SkillWithTraceAndDirectionOrTarget[0], matr, new SkillWithoutTraceAndOnTeamMember[0]);

			player.addMessageToRightBag(req1);

			SEND_ACTIVESKILL_REQ req2 = new SEND_ACTIVESKILL_REQ(GameMessageFactory.nextSequnceNum(), ids, new CommonAttackSkill[0], new SkillWithoutEffect[0], new SkillWithoutEffectAndQuickMove[0], new SkillWithoutTraceAndWithRange[0], new SkillWithoutTraceAndWithTargetOrPosition[0], (SkillWithoutTraceAndWithSummonNPC[]) cm.getSkillsByClass(SkillWithoutTraceAndWithSummonNPC.class), new SkillWithTraceAndDirectionOrTarget[0], new SkillWithoutTraceAndWithMatrix[0], new SkillWithoutTraceAndOnTeamMember[0]// cm.getSkillsByClass(SkillWithoutTraceAndOnTeamMember.class)
			);
			player.addMessageToRightBag(req2);

			SkillWithoutTraceAndOnTeamMember[] tttt = (SkillWithoutTraceAndOnTeamMember[]) cm.getSkillsByClass(SkillWithoutTraceAndOnTeamMember.class);
			int k = 4;
			for (int i = 0; i < k; i++) {
				int n = tttt.length / k;

				SkillWithoutTraceAndOnTeamMember[] tt = new SkillWithoutTraceAndOnTeamMember[i < k - 1 ? n : (tttt.length - n * (k - 1))];
				System.arraycopy(tttt, i * n, tt, 0, tt.length);
				req2 = new SEND_ACTIVESKILL_REQ(GameMessageFactory.nextSequnceNum(), ids, new CommonAttackSkill[0], new SkillWithoutEffect[0], new SkillWithoutEffectAndQuickMove[0], new SkillWithoutTraceAndWithRange[0], new SkillWithoutTraceAndWithTargetOrPosition[0], new SkillWithoutTraceAndWithSummonNPC[0],// )
				// cm.getSkillsByClass(SkillWithoutTraceAndWithSummonNPC.class),
				new SkillWithTraceAndDirectionOrTarget[0], new SkillWithoutTraceAndWithMatrix[0], tt);
				player.addMessageToRightBag(req2);
			}
			// Very important!
			SEND_ACTIVESKILL_REQ req3 = new SEND_ACTIVESKILL_REQ(GameMessageFactory.nextSequnceNum(), ids, new CommonAttackSkill[0], new SkillWithoutEffect[0], new SkillWithoutEffectAndQuickMove[0], new SkillWithoutTraceAndWithRange[0], new SkillWithoutTraceAndWithTargetOrPosition[0], new SkillWithoutTraceAndWithSummonNPC[0], (SkillWithTraceAndDirectionOrTarget[]) cm.getSkillsByClass(SkillWithTraceAndDirectionOrTarget.class), new SkillWithoutTraceAndWithMatrix[0], new SkillWithoutTraceAndOnTeamMember[0]);

			player.addMessageToRightBag(req3);

			long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			ActiveSkillEntity aes[] = player.getActiveSkillAgent().getInCoolDownSkills();
			for (int i = 0; i < aes.length; i++) {
				ActiveSkillEntity e = aes[i];
				long ll = e.getStartTime() + e.getIntonateTime() + e.getAttackTime() + e.getCooldownTime();
				if (e.getStatus() == ActiveSkillEntity.STATUS_COOLDOWN && ll - now >= 3000) {
					SKILL_CD_MODIFY_REQ req = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), player.getId(), (short) e.getSkill().getId(), (byte) ActiveSkillEntity.STATUS_COOLDOWN, e.getStartTime());
					player.addMessageToRightBag(req);
				}
			}

			return null;

		} else if (message instanceof QUERY_SKILLINFO_REQ) {

			QUERY_SKILLINFO_REQ req = (QUERY_SKILLINFO_REQ) message;
			short id = req.getId();
			CareerManager cm = CareerManager.getInstance();
			Skill skill = cm.getSkillById(id);
			if (skill != null) {
				return new QUERY_SKILLINFO_RES(req.getSequnceNum(), id, req.getDescType(), SkillInfoHelper.generate(player, skill, req.getDescType() == 0));
			}
		} else if (message instanceof QUERY_SKILLINFO_PET_REQ) {

			QUERY_SKILLINFO_PET_REQ req = (QUERY_SKILLINFO_PET_REQ) message;
			PetManager pm = PetManager.getInstance();
			if (pm != null) {
				short id = req.getId();
				long petId = req.getPetId();
				Pet pet = pm.getPet(petId);
				CareerManager cm = CareerManager.getInstance();
				Skill skill = cm.getSkillById(id);
				if (skill != null && pet != null) {
					return new QUERY_SKILLINFO_PET_RES(req.getSequnceNum(), petId, id, SkillInfoHelper.generate(pet, skill, 1, ""));
				}
			}
		} else if (message instanceof ALLOCATE_SKILL_REQ) {

			ALLOCATE_SKILL_REQ req = (ALLOCATE_SKILL_REQ) message;
			player.tryToLearnSkill(req.getSkillId(), false, false);
		} else if (message instanceof CLEAN_SKILL_REQ) {
			CLEAN_SKILL_REQ req = (CLEAN_SKILL_REQ) message;
			Option_CleanSkillOK ok = new Option_CleanSkillOK(req.getSkillId());
			ok.setText(Translate.确定);
			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.取消);
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setDescriptionInUUB(Translate.洗魂确认描述);
			mw.setOptions(new Option[] { ok, cancel });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
			return null;
			// player.tryCleanSkill(req.getSkillId());
		} else if (message instanceof QUERY_CAREER_INFO_REQ) {
			try {
				if (player != null) {
					long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					ActiveSkillEntity aes[] = player.getActiveSkillAgent().getInCoolDownSkills();
					for (int i = 0; i < aes.length; i++) {
						ActiveSkillEntity e = aes[i];
						long ll = e.getStartTime() + e.getIntonateTime() + e.getAttackTime() + e.getCooldownTime();
						if (e.getStatus() == ActiveSkillEntity.STATUS_COOLDOWN && ll - now >= 3000) {
							SKILL_CD_MODIFY_REQ req = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), player.getId(), (short) e.getSkill().getId(), (byte) ActiveSkillEntity.STATUS_COOLDOWN, e.getStartTime());
							player.addMessageToRightBag(req);
						}
					}
				} else {
					logger.error("[QUERY_CAREER_INFO_REQ] [PLAYER NULL]");
				}

				QUERY_CAREER_INFO_REQ req = (QUERY_CAREER_INFO_REQ) message;
				Career career = CareerManager.getInstance().getCareer(req.getCareer());
				if (career != null) {
					CareerThread threads[] = career.getCareerThreads();
					Skill[] skills1 = career.getBasicSkills();
					Skill[] skills2 = threads[0].getSkills();
					Skill[] skills3 = career.getNuqiSkills();
					Skill[] skills4 = career.getXinfaSkills();
					Skill[] skills5 = career.getKingSkills();
					SkillInfo[] basicSkills = new SkillInfo[skills1.length];
					for (int i = 0; i < basicSkills.length; i++) {
						basicSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < skills1.length; i++) {
						if (skills1[i] != null) {
							basicSkills[i].setInfo(player, skills1[i]);
						}
					}
					// TODO 这里将不传完整的进阶技能
					int professorNum = 14;
					SkillInfo[] professorSkills = new SkillInfo[professorNum];
					for (int i = 0; i < professorNum; i++) {
						professorSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < professorNum; i++) {
						if (skills2[i] != null) {
							professorSkills[i].setInfo(player, skills2[i]);
						}
					}
					SkillInfo[] nuqiSkills = new SkillInfo[skills3.length];
					for (int i = 0; i < nuqiSkills.length; i++) {
						nuqiSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < skills3.length; i++) {
						if (skills3[i] != null) {
							nuqiSkills[i].setInfo(player, skills3[i]);
						}
					}
					// TODO 这里将不传心法技能，因为协议长度不够了
					int xinfaNum = 17;
					SkillInfo[] xinfaSkills = new SkillInfo[xinfaNum];
					for (int i = 0; i < xinfaNum; i++) {
						xinfaSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < xinfaNum; i++) {
						if (skills4[i] != null) {
							xinfaSkills[i].setInfo(player, skills4[i]);
						}
					}
					// TODO 新的心法
					SkillInfo[] newXinfaSkills = new SkillInfo[skills4.length - xinfaNum];
					for (int i = 0; i < skills4.length - xinfaNum; i++) {
						newXinfaSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < skills4.length - xinfaNum; i++) {
						if (skills4[xinfaNum + i] != null) {
							newXinfaSkills[i].setInfo(player, skills4[xinfaNum + i]);
						}
					}
					SkillInfo[] kingSkills = new SkillInfo[skills5.length];
					for (int i = 0; i < kingSkills.length; i++) {
						kingSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < skills5.length; i++) {
						if (skills5[i] != null) {
							kingSkills[i].setInfo(player, skills5[i]);
						}
					}
					QUERY_CAREER_INFO_RES res = new QUERY_CAREER_INFO_RES(req.getSequnceNum(), career, basicSkills, professorSkills, nuqiSkills, xinfaSkills, kingSkills);
					QUERY_CAREER_XINFA_INFO_RES res1 = new QUERY_CAREER_XINFA_INFO_RES(req.getSequnceNum(), newXinfaSkills);
					player.addMessageToRightBag(res1);
					logger.warn("QUERY_CAREER_INFO_REQ发送成功 [" + res.getLength() + "] [bs=" + basicSkills.length + "] [ps=" + professorSkills.length + "] [ns=" + nuqiSkills.length + "] [xs=" + xinfaSkills.length + "]");
					return res;
				} else {
					// logger.warn("[" + getName() + "][QUERY_CAREER][fail]");
					if (logger.isWarnEnabled()) logger.warn("[{}][QUERY_CAREER][fail]", new Object[] { getName() });
				}
			} catch (Exception e) {
				logger.error("QUERY_CAREER_INFO_REQ出错", e);
			}
		} else if (message instanceof NEW_QUERY_CAREER_INFO_REQ) {
			return handle_NEW_QUERY_CAREER_INFO_REQ((NEW_QUERY_CAREER_INFO_REQ) message, player);
		} else if (message instanceof QUERY_ARTICLE_REQ) {

			QUERY_ARTICLE_REQ req = (QUERY_ARTICLE_REQ) message;
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			Collection<com.fy.engineserver.datasource.article.entity.client.ArticleEntity> articleList = new ArrayList<com.fy.engineserver.datasource.article.entity.client.ArticleEntity>();
			Collection<com.fy.engineserver.datasource.article.entity.client.PropsEntity> propsList = new ArrayList<com.fy.engineserver.datasource.article.entity.client.PropsEntity>();
			Collection<com.fy.engineserver.datasource.article.entity.client.EquipmentEntity> equipmentList = new ArrayList<com.fy.engineserver.datasource.article.entity.client.EquipmentEntity>();

			long[] ids = req.getArticleIds();
			List<ArticleEntity> list = aem.getEntityByIds(ids);

			for (ArticleEntity entity : list) {
				if (entity == null) {
					if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[物品查询] [ID={}] [物品不存在] [{}] [{}]", new Object[] { "", player.getId(), player.getName() });
				} else if (entity instanceof PropsEntity) {
					com.fy.engineserver.datasource.article.entity.client.PropsEntity p = translate((PropsEntity) entity);
					if (p != null) {
						propsList.add(p);
						if (ArticleManager.logger.isDebugEnabled()) {
							ArticleManager.logger.debug("[物品查询] [道具] [{}] [{}]", new Object[] { p.getName(), p.getId() });
						}
					} else {
						if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[物品查询] [{}] [ID={}] [物品转换失败] [{}] [{}]", new Object[] { entity.getArticleName(), entity.getId(), player.getId(), player.getName() });
					}
				} else if (entity instanceof EquipmentEntity) {
					com.fy.engineserver.datasource.article.entity.client.EquipmentEntity p = translate((EquipmentEntity) entity);
					if (p != null) {
						equipmentList.add(p);
						if (ArticleManager.logger.isDebugEnabled()) {
							ArticleManager.logger.debug("[物品查询] [装备] [{}] [{}]", new Object[] { p.getName(), p.getId() });
						}
					} else {
						if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[物品查询] [{}] [ID={}] [物品转换失败] [{}] [{}]", new Object[] { entity.getArticleName(), entity.getId(), player.getId(), player.getName() });
					}
				} else {
					com.fy.engineserver.datasource.article.entity.client.ArticleEntity p = translate((ArticleEntity) entity);
					if (p != null) {
						articleList.add(p);
						if (ArticleManager.logger.isDebugEnabled()) {
							ArticleManager.logger.debug("[物品查询] [物品] [{}] [{}] [{}]", new Object[] { p.getName(), p.getId(), entity.getClass().getName() });
						}
					} else {
						if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[物品查询] [{}] [ID={}] [物品转换失败] [{}] [{}]", new Object[] { entity.getArticleName(), entity.getId(), player.getId(), player.getName() });
					}
				}
			}
			QUERY_ARTICLE_RES res = new QUERY_ARTICLE_RES(req.getSequnceNum(), articleList.toArray(new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[articleList.size()]), propsList.toArray(new com.fy.engineserver.datasource.article.entity.client.PropsEntity[propsList.size()]), equipmentList.toArray(new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[equipmentList.size()]));
			if (ArticleManager.logger.isDebugEnabled()) {
				ArticleManager.logger.debug("[物品查询] [物品] [协议长度:{}] []", new Object[] { res.getLength() });
			}
			return res;

		} else if (message instanceof FUCK_TRANSPORT_GAME) {
			FUCK_TRANSPORT_GAME req = (FUCK_TRANSPORT_GAME) message;
			String mapName = req.getMapName();
			GameInfo gi = GameManager.getInstance().getGameInfo(mapName);
			TransportData tds[] = gi.getTransports();
			int tX = 200;
			int tY = 200;
			if (tds != null && tds.length > 0) {
				TransportData td = tds[0];
				tX = td.getX() + 50;
				tY = td.getY() + 50;
			}
			TransportData td = new TransportData(0, 0, 0, 0, mapName, "", tX, tY);
			player.getCurrentGame().transferGame(player, td);
		} else if (message instanceof BOTTLE_OPEN_REQ) {
			BOTTLE_OPEN_REQ req = (BOTTLE_OPEN_REQ) message;
			long articleId = req.getArticleId();
			ArticleEntity ae = player.getArticleEntity(articleId);
			if (ae instanceof BottlePropsEntity) {
				((BottlePropsEntity) ae).setOpen(true);
				((BottlePropsEntity) ae).setCanOpenCount(0);
			}
		} else if (message instanceof BOTTLE_PICK_ARTICLE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_INVALID_TIME_REQ) {
			QUERY_INVALID_TIME_REQ req = (QUERY_INVALID_TIME_REQ) message;
			long articleId = req.getArticleId();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			if (aem != null) {
				ArticleEntity ae = aem.getEntity(articleId);
				if (ae != null) {
					Timer timer = ae.getTimer();
					long invalidTime = -1;
					if (timer != null) {
						invalidTime = timer.getEndTime();
					}
					QUERY_INVALID_TIME_RES res = new QUERY_INVALID_TIME_RES(req.getSequnceNum(), articleId, invalidTime);
					player.addMessageToRightBag(res);
				}
			}
		} else if (message instanceof EXPEND_KNAPSACK_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof TAKE_TASK_BY_ARTICLE_REQ) {
			long articleId = ((TAKE_TASK_BY_ARTICLE_REQ) message).getArticleId();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleManager am = ArticleManager.getInstance();
			if (aem != null && am != null) {
				ArticleEntity ae = player.getArticleEntity(articleId);
				if (ae != null) {
					Article a = am.getArticle(ae.getArticleName());
					if (a instanceof TaskProps) {
						int id = 0;
						TaskProps tp = (TaskProps) a;
						int[] ids = tp.getIds();
						if (ids != null && ids.length > 0) {
							if (ids.length > ae.getColorType()) {
								id = ids[ae.getColorType()];
							} else {
								id = ids[ids.length - 1];
							}
						}
						Task task = TaskManager.getInstance().getTask(id);
						if (task != null) {
							if (player.addTaskByServer(task).getBooleanValue()) {
								ArticleEntity en = player.removeArticleEntityFromKnapsackByArticleId(articleId, "接任务删除", true);
								if (en != null) {
									ArticleStatManager.addToArticleStat(player, null, en, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "接任务删除", null);
								}
							} else {
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您不能接取这个任务);
								player.addMessageToRightBag(hreq);
							}
						}
					}
				}
			}
		} else if (message instanceof QUERY_ARTICLE_INFO_REQ) {
			QUERY_ARTICLE_INFO_REQ req = (QUERY_ARTICLE_INFO_REQ) message;
			long articleId = req.getArticleId();
			try {
				ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(articleId);
				String description = "";
				if (articleEntity == null) {
					description = Translate.text_2690;
				} else {
					description = AritcleInfoHelper.generate(player, articleEntity);
				}
				return new QUERY_ARTICLE_INFO_RES(req.getSequnceNum(), articleId, description);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (message instanceof QUERY_OTHER_PLAYER_REQ) {
			QUERY_OTHER_PLAYER_REQ req = (QUERY_OTHER_PLAYER_REQ) message;
			Player findplayer = PlayerManager.getInstance().getPlayer(req.getPlayerId());
			if (findplayer != null) {
				if (findplayer.getUnusedSoul() == null) {
					findplayer.setUnusedSoul(new ArrayList<Soul>());
				}
				SoulEquipment4Client[] clients = new SoulEquipment4Client[findplayer.getUnusedSoul().size() + 1];
				clients[0] = findplayer.getCurrSoul().getSoulEquipment4Client();
				for (int i = 0; i < findplayer.getUnusedSoul().size(); i++) {
					clients[i + 1] = findplayer.getUnusedSoul().get(i).getSoulEquipment4Client();
				}
				// return new QUERY_OTHER_PLAYER_RES(req.getSequnceNum(), findplayer, findplayer.getSouls(), clients,
				// PlayerManager.各个职业各个级别的所有战斗属性理论最大数值[findplayer.getCareer()][findplayer.getLevel() -
				// 1],findplayer.getMaxHPX(),findplayer.getMaxMPX(),findplayer.getPhyAttackX(),findplayer.getMagicDefenceX(),findplayer.getBreakDefenceX(),findplayer.getHitX(),findplayer.getDodgeX(),findplayer.getAccurateX(),findplayer.getPhyAttackX(),findplayer.getMagicAttackX(),findplayer.getFireAttackX(),findplayer.getFireDefenceX(),findplayer.getFireIgnoreDefenceX(),findplayer.getBlizzardAttackX(),findplayer.getBlizzardDefenceX(),findplayer.getBlizzardIgnoreDefenceX(),findplayer.getWindAttackX(),findplayer.getWindDefenceX(),findplayer.getWindIgnoreDefenceX(),findplayer.getThunderAttackX(),findplayer.get);
				return new QUERY_OTHER_PLAYER_RES(req.getSequnceNum(), findplayer, findplayer.getSouls(), clients, PlayerManager.各个职业各个级别的所有战斗属性理论最大数值[findplayer.getCareer()][findplayer.getLevel() - 1]);
			}
		} else if (message instanceof EQUIPMENT_COMPOUND_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof EQUIPMENT_DRILL_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_EQUIPMENT_TABLE_REQ) {

			QUERY_EQUIPMENT_TABLE_REQ req = (QUERY_EQUIPMENT_TABLE_REQ) message;
			// EquipmentColumn ecs = player.getEquipmentColumns();
			// EquipmentEntity ees[] = ecs.getEquipmentArrayByCopy();
			// long[] mainSuit = new long[ees.length];
			// for (int i = 0; i < ees.length; i++) {
			// if (ees[i] != null) {
			// mainSuit[i] = ees[i].getId();
			// } else {
			// mainSuit[i] = -1;
			// }
			// }
			// // ees = ecs[1].getEquipmentArrayByCopy();
			// long[] secondSuit = new long[ees.length];
			// for (int i = 0; i < ees.length; i++) {
			// if (ees[i] != null) {
			// secondSuit[i] = ees[i].getId();
			// } else {
			// secondSuit[i] = -1;
			// }
			// }
			if (player.getUnusedSoul() == null) {
				player.setUnusedSoul(new ArrayList<Soul>());
			}
			SoulEquipment4Client[] clients = new SoulEquipment4Client[player.getUnusedSoul().size() + 1];
			clients[0] = player.getCurrSoul().getSoulEquipment4Client();
			for (int i = 0; i < player.getUnusedSoul().size(); i++) {
				clients[i + 1] = player.getUnusedSoul().get(i).getSoulEquipment4Client();
			}
			return new QUERY_EQUIPMENT_TABLE_RES(req.getSequnceNum(), clients);

		} else if (message instanceof QUERY_KNAPSACK_REQ) {
			Long lastAutoKnapasckTime = autoKnapsack.get(player.getId());
			if (lastAutoKnapasckTime == null) {
				lastAutoKnapasckTime = 0L;
				autoKnapsack.put(player.getId(), lastAutoKnapasckTime);
			}
			if (System.currentTimeMillis() - lastAutoKnapasckTime < TaskManager.getInstance().autoKnapsackSpaceTime) {
				player.sendError(Translate.整理背包太过频繁);
				return null;
			}
			autoKnapsack.put(player.getId(), System.currentTimeMillis());
			QUERY_KNAPSACK_REQ req = (QUERY_KNAPSACK_REQ) message;
			Knapsack[] sack = player.getKnapsacks_common();
			ArticleManager am = ArticleManager.getInstance();
			if (sack == null) {
				// logger.info("[没有包裹] [QUERY_KNAPSACK_REQ] [" + player.getUsername() + "] [" + player.getName() + "] [" + player.getId() + "]");
				if (logger.isInfoEnabled()) logger.info("[没有包裹] [QUERY_KNAPSACK_REQ] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getName(), player.getId() });
				return null;
			}
			byte rt = req.getRequestType();
			if (rt == 1) {
				pushMessageToGame(player, message, null);
			} else {
				try {
					BagInfo4Client[] bagInfo4Client = new BagInfo4Client[sack.length];
					for (int i = 0; i < sack.length; i++) {
						Knapsack knapsack = sack[i];
						bagInfo4Client[i] = new BagInfo4Client();
						bagInfo4Client[i].setBagtype((byte) i);
						if (i >= 3) {
							bagInfo4Client[i].setFangbaomax((short) 0);
						}
						if (knapsack != null) {
							Cell kcs[] = knapsack.getCells();
							long ids[] = new long[kcs.length];
							short counts[] = new short[kcs.length];
							for (int j = 0; j < kcs.length; j++) {
								if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
									ids[j] = kcs[j].getEntityId();
									counts[j] = (short) kcs[j].getCount();
									if (logger.isDebugEnabled()) {
										logger.debug("[获取背包] [test] [成功] ["+i+"] [" + player.getLogString() + "] [" + kcs[j].getEntityId() + "]");
									}
								} else {
									ids[j] = -1;
									counts[j] = 0;
								}
							}
							bagInfo4Client[i].setEntityId(ids);
							bagInfo4Client[i].setCounts(counts);
	
							// 对应类型的防爆包
							// Knapsack knap_fangbao = player.getKnapsack_fangbao();
							// if (knap_fangbao != null) {
							// kcs = knap_fangbao.getCells();
							// ids = new long[kcs.length];
							// counts = new short[kcs.length];
							// for (int j = 0; j < kcs.length; j++) {
							// if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
							// ids[j] = kcs[j].getEntityId();
							// counts[j] = (short) kcs[j].getCount();
							// } else {
							// ids[j] = -1;
							// counts[j] = 0;
							// }
							// }
							// bagInfo4Client[i].setFangbaoEntityId(ids);
							// bagInfo4Client[i].setFangbaoCounts(counts);
							// }
						}
					}
	
					HashMap<String, PropsCategoryCoolDown> map = player.getUsingPropsAgent().getCooldownTable();
					Iterator<String> it = map.keySet().iterator();
					while (it.hasNext()) {
						String categoryName = it.next();
						PropsCategoryCoolDown cd = map.get(categoryName);
						if (cd != null && cd.end >= com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 5000) {
							PROPS_CD_MODIFY_REQ req2 = new PROPS_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), player.getId(), categoryName, cd.start, (byte) 0);
							player.addMessageToRightBag(req2);
						}
					}
					Fangbao_KNAPSACK_RES res = new Fangbao_KNAPSACK_RES(GameMessageFactory.nextSequnceNum(), player.getKnapsack_fangBao_Id());
					player.addMessageToRightBag(res);
					if (logger.isDebugEnabled()) {
						logger.debug("[返回包裹] [QUERY_KNAPSACK_REQ] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getName(), player.getId() });
					}
					if (logger.isDebugEnabled()) {
						logger.debug("[获取背包] [test] [成功] [" + player.getLogString() + "] [" + bagInfo4Client.length + "]");
					}
					return new QUERY_KNAPSACK_RES(req.getSequnceNum(), bagInfo4Client, am.getAllPropsCategory());
				} catch (Exception e) {
					logger.warn("[获取背包] [异常] [" + player.getLogString() + "]", e);
				}
			}
		} else if (message instanceof QUERY_KNAPSACK_FB_REQ) {

			Knapsack sack = player.getKnapsacks_fangBao();
			QUERY_KNAPSACK_FB_RES res = null;
			if (sack == null) {
				res = new QUERY_KNAPSACK_FB_RES(GameMessageFactory.nextSequnceNum(), -1, Player.防爆包最大格子数, new long[0], new short[0]);
			} else {
				Cell[] kcs = sack.getCells();
				long[] ids = new long[kcs.length];
				short[] counts = new short[kcs.length];
				for (int j = 0; j < kcs.length; j++) {
					if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
						ids[j] = kcs[j].getEntityId();
						counts[j] = (short) kcs[j].getCount();
					} else {
						ids[j] = -1;
						counts[j] = 0;
					}
				}
				res = new QUERY_KNAPSACK_FB_RES(GameMessageFactory.nextSequnceNum(), player.getKnapsack_fangBao_Id(), Player.防爆包最大格子数, ids, counts);
			}
			player.addMessageToRightBag(res);
		} else if (message instanceof Fangbao_KNAPSACK_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof ARTICLE_OPRATION_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof HOOK_USE_PROP_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof ARTICLE_OPRATION_MOVE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_SUIT_INFO_REQ) {
			请求星級套(player, message);
		} else if (message instanceof REMOVE_AVATAPROPS_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_WORLD_MAP_REQ) {
			WorldMapManager wmm = WorldMapManager.getInstance();
			if (wmm != null) {
				QUERY_WORLD_MAP_RES res = new QUERY_WORLD_MAP_RES(message.getSequnceNum(), wmm.worldMapAreas);
				player.addMessageToRightBag(res);
			}
		} else if (message instanceof QUERY_WORLD_MAP_AREAMAP_REQ) {
			WorldMapManager wmm = WorldMapManager.getInstance();
			String areaName = ((QUERY_WORLD_MAP_AREAMAP_REQ) message).getAreaName();
			String[] mapNames = wmm.mapENGLISHNamesInArea.get(areaName);
			String[] mapCHINANames = wmm.mapCHINANamesInArea.get(areaName);
			if (mapNames == null || mapCHINANames == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您查询的区域不存在);
				player.addMessageToRightBag(hreq);
				return null;
			}
			boolean[] searched = new boolean[mapNames.length];
			for (int i = 0; i < mapNames.length; i++) {
				searched[i] = wmm.isPlayerExploredMap(player.getId(), mapNames[i]);
			}
			QUERY_WORLD_MAP_AREAMAP_RES res = new QUERY_WORLD_MAP_AREAMAP_RES(message.getSequnceNum(), areaName, mapNames, mapCHINANames, searched);
			player.addMessageToRightBag(res);
		} else if (message instanceof QUERY_MAP_SEARCH_REQ) {
			WorldMapManager wmm = WorldMapManager.getInstance();
			String[] mapNames = ((QUERY_MAP_SEARCH_REQ) message).getMapName();
			boolean[] searched = new boolean[mapNames.length];
			for (int i = 0; i < mapNames.length; i++) {
				searched[i] = wmm.isPlayerExploredMap(player.getId(), mapNames[i]);
			}
			QUERY_MAP_SEARCH_RES res = new QUERY_MAP_SEARCH_RES(message.getSequnceNum(), mapNames, searched);
			player.addMessageToRightBag(res);
		} else if (message instanceof QUERY_GAMEMAP_NPCMONSTER_REQ) {
			WorldMapManager wmm = WorldMapManager.getInstance();
			String mapname = ((QUERY_GAMEMAP_NPCMONSTER_REQ) message).getMapname();
			String[] npcNames = new String[0];
			String[] npcIcons = new String[0];
			int[] functionTypes = new int[0];
			short[] npcx = new short[0];
			short[] npcy = new short[0];
			GameManager gm = GameManager.getInstance();
			Game game = null;
			if (mapname.equals(player.getGame())) {
				game = player.getCurrentGame();
			}
			if (mapname.equals(FaeryConfig.GAME_NAME) || mapname.contains("jiazuditu") || mapname.equals("biyiqiyuan") || mapname.equals("fushengruomeng") || mapname.equals("queqiaoxianjing")) {
				game = player.getCurrentGame();
			}
			if (game == null) {
				game = gm.getGameByName(mapname, player.getCountry());
			}
			if (game == null) {
				game = gm.getGameByName(mapname, CountryManager.中立);
			}
			FunctionNPC[] fns = TaskManager.getInstance().只用于世界地图的npc查询(game, player);
			if (fns != null) {
				npcNames = new String[fns.length];
				npcIcons = new String[fns.length];
				functionTypes = new int[fns.length];
				npcx = new short[fns.length];
				npcy = new short[fns.length];
				for (int i = 0; i < fns.length; i++) {
					npcNames[i] = fns[i].name;
					npcIcons[i] = Sprite.getNpcIcon(fns[i].functionType);
					functionTypes[i] = fns[i].functionType;
					npcx[i] = (short) fns[i].x;
					npcy[i] = (short) fns[i].y;
				}
			}
			String[] monsterNames = new String[0];
			String[] monsterIcons = new String[0];
			int[] monsterLv = new int[0];
			short[] monsterx = new short[0];
			short[] monstery = new short[0];
			if (wmm.mapEnglishSingleMonsterInfos != null && wmm.mapEnglishSingleMonsterInfos.get(mapname) != null) {
				MapSingleMonsterInfo msmi = wmm.mapEnglishSingleMonsterInfos.get(mapname);
				monsterNames = msmi.getMonsterNames();
				monsterLv = msmi.getMonsterLv();
				monsterx = msmi.getMonsterx();
				monstery = msmi.getMonstery();
			}
			QUERY_GAMEMAP_NPCMONSTER_RES res = new QUERY_GAMEMAP_NPCMONSTER_RES(message.getSequnceNum(), mapname, npcNames, functionTypes, npcIcons, npcx, npcy, monsterNames, monsterLv, monsterIcons, monsterx, monstery);
			player.addMessageToRightBag(res);
		} else if (message instanceof QUERY_GAMEMAP_MOVE_LIVINGOBJECT_REQ) {
			String mapname = ((QUERY_GAMEMAP_MOVE_LIVINGOBJECT_REQ) message).getMapname();
			GameManager gm = GameManager.getInstance();
			Game game = player.getCurrentGame();
			if (game == null) {
				return null;
			}
			LivingObject[] los = game.getLivingObjects();
			if (los == null) {
				return null;
			}
			ArrayList<String> playersList = new ArrayList<String>();
			ArrayList<String> playersIconList = new ArrayList<String>();
			ArrayList<Short> playersxList = new ArrayList<Short>();
			ArrayList<Short> playersyList = new ArrayList<Short>();
			ArrayList<Short> ownerCountryxList = new ArrayList<Short>();
			ArrayList<Short> ownerCountryyList = new ArrayList<Short>();
			ArrayList<Short> enemyCountryxList = new ArrayList<Short>();
			ArrayList<Short> enemyCountryyList = new ArrayList<Short>();

			for (int i = 0; i < los.length; i++) {
				LivingObject lo = los[i];
				if (lo instanceof Player) {
					Player p = (Player) lo;

					// 自己
					if (p == player) {
						continue;
					}
					// 国王
					if (p.getCountryPosition() == CountryManager.国王) {
						playersList.add(p.getName());
						playersxList.add((short) p.x);
						playersyList.add((short) p.y);
						if (p.getCountry() == player.getCountry()) {
							playersIconList.add(WorldMapManager.本国国王图标);
						} else {
							playersIconList.add(WorldMapManager.敌国国王图标);
						}
					} else {
						SocialManager socialManager = SocialManager.getInstance();
						if (socialManager != null) {
							try {
								Relation relation = socialManager.getRelationById(player.getId());
								MasterPrentice masterPrentice = relation.getMasterPrentice();
								// 夫妻
								if (relation != null && relation.getCoupleName() != null && relation.getCoupleName().equals(p.getName())) {
									playersList.add(p.getName());
									playersxList.add((short) p.x);
									playersyList.add((short) p.y);
									if (p.getSex() == 0) {
										playersIconList.add(WorldMapManager.新郎图标);
									} else {
										playersIconList.add(WorldMapManager.新娘图标);
									}
									continue;
								}
								// 兄弟
								if (relation != null) {
									int relationship = socialManager.getRelationA2B(player.getId(), p.getId());
									if (relationship == 1) {
										playersList.add(p.getName());
										playersxList.add((short) p.x);
										playersyList.add((short) p.y);
										playersIconList.add(WorldMapManager.结义图标);
										continue;
									}
								}
								// 仇人
								if (relation != null) {
									ArrayList<Long> chourenList = relation.getChourenlist();
									if (chourenList != null) {
										if (chourenList.contains(p.getId())) {
											playersList.add(p.getName());
											playersxList.add((short) p.x);
											playersyList.add((short) p.y);
											playersIconList.add(WorldMapManager.仇人图标);
											continue;
										}
									}
								}
								// 好友
								if (relation != null) {
									ArrayList<Long> friendList = relation.getFriendlist();
									if (friendList != null) {
										if (friendList.contains(p.getId())) {
											playersList.add(p.getName());
											playersxList.add((short) p.x);
											playersyList.add((short) p.y);
											playersIconList.add(WorldMapManager.好友图标);
											continue;
										}
									}
								}
								// 师徒
								if (masterPrentice != null) {
									if (masterPrentice.getMasterId() > 0) {
										if (p.getId() == masterPrentice.getMasterId()) {
											playersIconList.add(WorldMapManager.师傅图标);
											playersList.add(p.getName());
											playersxList.add((short) p.x);
											playersyList.add((short) p.y);
											continue;
										}
									}
									if (masterPrentice.getPrentices() != null) {
										boolean has = false;
										for (Long id : masterPrentice.getPrentices()) {
											if (p.getId() == id) {
												playersIconList.add(WorldMapManager.徒弟图标);
												playersList.add(p.getName());
												playersxList.add((short) p.x);
												playersyList.add((short) p.y);
												has = true;
												break;
											}
										}
										if (has) {
											continue;
										}
									}
								}
							} catch (Exception ex) {

							}
						}
						if (player.getTeamMembers() != null) {
							boolean has = false;
							for (Player pp : player.getTeamMembers()) {
								if (pp != null && pp.getId() == p.getId()) {
									playersIconList.add(WorldMapManager.队友图标);
									playersList.add(p.getName());
									playersxList.add((short) p.x);
									playersyList.add((short) p.y);
									has = true;
									break;
								}
							}
							if (has) {
								continue;
							}
						}

						if (p.getJiazuId() == player.getJiazuId() && player.getJiazuId() > 0) {
							playersIconList.add(WorldMapManager.家族图标);
							playersList.add(p.getName());
							playersxList.add((short) p.x);
							playersyList.add((short) p.y);
							continue;
						}

						if (p.getZongPaiName() == player.getZongPaiName() && player.getZongPaiName() != null && !player.getZongPaiName().trim().equals("")) {
							playersIconList.add(WorldMapManager.宗派图标);
							playersList.add(p.getName());
							playersxList.add((short) p.x);
							playersyList.add((short) p.y);
							continue;
						}

						if (p.getEvil() >= 100) {
							playersIconList.add(WorldMapManager.恶人图标);
							playersList.add(p.getName());
							playersxList.add((short) p.x);
							playersyList.add((short) p.y);
							continue;
						}
						if (p.getCountry() == player.getCountry()) {
							ownerCountryxList.add((short) p.getX());
							ownerCountryyList.add((short) p.getY());
							continue;
						}
					}

					// 敌对国家人员
					if (p.getCountry() != player.getCountry()) {
						enemyCountryxList.add((short) p.getX());
						enemyCountryyList.add((short) p.getY());
					}
				}
			}

			if (game.gi.name.equals(FaeryConfig.GAME_NAME)) {
				List<Long> hookCaveIds = player.getHookPetCaveId();
				if (hookCaveIds != null && hookCaveIds.size() > 0) {
					Faery faery = FaeryManager.getInstance().getFaery(game);
					if (faery != null) {
						for (Cave cave : faery.getCaves()) {
							if (cave != null) {
								if (hookCaveIds.contains(cave.getId())) {
									if (FaeryManager.logger.isInfoEnabled()) {
										FaeryManager.logger.info(player.getLogString() + "[进入地图挂宠信息] [发现存在的宠物挂机CAVEID]");
									}
									CaveBuilding cb = cave.getCaveBuilding(FaeryConfig.CAVE_BUILDING_TYPE_PETHOUSE);
									if (cb != null) {
										if (cb.getNpc() != null) {
											playersList.add(Translate.挂机宠物);
											playersxList.add((short) cb.getNpc().getX());
											playersyList.add((short) cb.getNpc().getY());
											playersIconList.add("hd_boss");
											if (FaeryManager.logger.isInfoEnabled()) {
												FaeryManager.logger.info(player.getLogString() + "[进入地图挂宠信息设置完毕] [caveId:" + cave.getId() + "] [主人:" + cb.getCave().getOwnerName() + "]");
											}
										} else {
											if (FaeryManager.logger.isInfoEnabled()) {
												FaeryManager.logger.info(player.getLogString() + "[进入地图挂宠信息设置完毕] [caveId:" + cave.getId() + "] [宠物房NPC不存在]");
											}
										}
									} else {
										if (FaeryManager.logger.isInfoEnabled()) {
											FaeryManager.logger.info(player.getLogString() + "[进入地图挂宠信息设置完毕] [caveId:" + cave.getId() + "] [宠物房不存在]");
										}
									}
								}
							}
						}
					} else {
						if (FaeryManager.logger.isInfoEnabled()) {
							FaeryManager.logger.info(player.getLogString() + "[进入地图挂宠信息] [仙境不存在]");
						}
					}
				}

			} else {
				if (FaeryManager.logger.isInfoEnabled()) {
					FaeryManager.logger.info(player.getLogString() + "[进入地图挂宠信息] [不是仙府地图] [" + game.gi.name + "]");
				}
			}
			String[] players = playersList.toArray(new String[0]);
			String[] playersIcon = playersIconList.toArray(new String[0]);
			short[] playersx = new short[playersxList.size()];
			for (int i = 0; i < playersxList.size(); i++) {
				playersx[i] = playersxList.get(i).shortValue();
			}
			short[] playersy = new short[playersyList.size()];
			for (int i = 0; i < playersyList.size(); i++) {
				playersy[i] = playersyList.get(i).shortValue();
			}
			short[] ownerCountryx = new short[ownerCountryxList.size()];
			for (int i = 0; i < ownerCountryxList.size(); i++) {
				ownerCountryx[i] = ownerCountryxList.get(i).shortValue();
			}
			short[] ownerCountryy = new short[ownerCountryyList.size()];
			for (int i = 0; i < ownerCountryyList.size(); i++) {
				ownerCountryy[i] = ownerCountryyList.get(i).shortValue();
			}
			short[] enemyCountryx = new short[enemyCountryxList.size()];
			for (int i = 0; i < enemyCountryxList.size(); i++) {
				enemyCountryx[i] = enemyCountryxList.get(i).shortValue();
			}
			short[] enemyCountryy = new short[enemyCountryyList.size()];
			for (int i = 0; i < enemyCountryyList.size(); i++) {
				enemyCountryy[i] = enemyCountryyList.get(i).shortValue();
			}

			QUERY_GAMEMAP_MOVE_LIVINGOBJECT_RES res = new QUERY_GAMEMAP_MOVE_LIVINGOBJECT_RES(message.getSequnceNum(), mapname, players, playersIcon, playersx, playersy, ownerCountryx, ownerCountryy, enemyCountryx, enemyCountryy);
			player.addMessageToRightBag(res);
		} else if (message instanceof QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_REQ) {
			String mapname = ((QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_REQ) message).getMapName();
			GameManager gm = GameManager.getInstance();
			Game game = gm.getGameByName(mapname, CountryManager.中立);
			if (game == null) {
				return null;
			}

			String areaName = "";
			WorldMapManager wmm = WorldMapManager.getInstance();
			if (wmm == null) {
				return null;
			}
			if (wmm.worldMapAreas == null || wmm.worldMapAreas.length == 0) {
				return null;
			}
			for (WorldMapArea wma : wmm.worldMapAreas) {
				if (wma != null) {
					String[] mapNames = wmm.mapENGLISHNamesInArea.get(wma.getName());
					if (mapNames != null) {
						for (String mapName : mapNames) {
							if (mapname.equals(mapName)) {
								areaName = wma.getName();
								break;
							}
						}
					}
					if (!areaName.equals("")) {
						break;
					}
				}
			}
			String[] mapNames = wmm.mapENGLISHNamesInArea.get(areaName);
			String[] mapCHINANames = wmm.mapCHINANamesInArea.get(areaName);
			if (mapNames == null || mapCHINANames == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您查询的区域不存在);
				player.addMessageToRightBag(hreq);
				return null;
			}
			boolean[] searched = new boolean[mapNames.length];
			for (int i = 0; i < mapNames.length; i++) {
				searched[i] = wmm.isPlayerExploredMap(player.getId(), mapNames[i]);
			}
			QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_RES res = new QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_RES(message.getSequnceNum(), areaName, mapNames, mapCHINANames, searched);
			player.addMessageToRightBag(res);
		} else if (message instanceof QUERY_WORLD_MAP_AREAMAP_BY_PLAYER_REQ) {
			Game game = player.getCurrentGame();
			if (game == null) {
				return null;
			}
			String mapname = game.gi.getName();
			String areaName = "";
			WorldMapManager wmm = WorldMapManager.getInstance();
			if (wmm == null) {
				return null;
			}
			if (wmm.worldMapAreas == null || wmm.worldMapAreas.length == 0) {
				return null;
			}
			for (WorldMapArea wma : wmm.worldMapAreas) {
				if (wma != null) {
					String[] mapNames = wmm.mapENGLISHNamesInArea.get(wma.getName());
					if (mapNames != null) {
						for (String mapName : mapNames) {
							if (mapname.equals(mapName)) {
								areaName = wma.getName();
								break;
							}
						}
					}
					if (!areaName.equals("")) {
						break;
					}
				}
			}

			if ("".equals(areaName)) {
				for (WorldMapArea wma : wmm.worldMapAreas_xj) {
					if (wma != null && wma.getMapnames() != null && wma.getMapnames().length > 0) {
						String names[] = wma.getMapnames();
						for (String mapName : names) {
							if (mapname.equals(mapName)) {
								areaName = wma.getName();
								break;
							}
						}
					}
				}
			}
			Game.logger.warn("[获得天界区域地图] [name:" + player.getName() + "] [玩家当前所在地图：" + mapname + "] [世界地图:" + areaName + "] [displayName:" + game.gi.displayName + "] [--2]");
			QUERY_WORLD_MAP_AREAMAP_BY_PLAYER_RES res = new QUERY_WORLD_MAP_AREAMAP_BY_PLAYER_RES(message.getSequnceNum(), areaName, game.gi.getName(), game.gi.displayName);
			player.addMessageToRightBag(res);
		} else if (message instanceof COUNTRY_WANGZHEZHIYIN_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof COUNTRY_QIUJIN_REQ) {
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if (cm != null && pm != null) {
				Player playerB = null;
				try {
					playerB = pm.getPlayer(((COUNTRY_QIUJIN_REQ) message).getPlayerName());
				} catch (Exception ex) {

				}
				cm.囚禁(player, playerB);
			}
		} else if (message instanceof COUNTRY_JINYAN_REQ) {
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if (cm != null && pm != null) {
				Player playerB = null;
				try {
					playerB = pm.getPlayer(((COUNTRY_JINYAN_REQ) message).getPlayerName());
				} catch (Exception ex) {

				}
				cm.禁言(player, playerB);
			}
		} else if (message instanceof COUNTRY_JIECHU_QIUJIN_REQ) {
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if (cm != null && pm != null) {
				Player playerB = null;
				try {
					playerB = pm.getPlayer(((COUNTRY_JIECHU_QIUJIN_REQ) message).getPlayerName());
				} catch (Exception ex) {

				}
				cm.释放(player, playerB);
			}
		} else if (message instanceof COUNTRY_JIECHU_JINYAN_REQ) {
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if (cm != null && pm != null) {
				Player playerB = null;
				try {
					playerB = pm.getPlayer(((COUNTRY_JIECHU_JINYAN_REQ) message).getPlayerName());
				} catch (Exception ex) {

				}
				cm.解禁(player, playerB);
			}
		} else if (message instanceof QUERY_COUNTRY_MAIN_PAGE_REQ) {
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if (cm != null && pm != null) {
				cm.得到国家主页面res(player);
			}
		} else if (message instanceof QUERY_COUNTRY_VOTE_REQ) {
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if (cm != null && pm != null) {
				cm.得到投票结果(player);
			}
		} else if (message instanceof SHOOK_DICE_RESULT_REQ) {
			SHOOK_DICE_RESULT_REQ req = (SHOOK_DICE_RESULT_REQ) message;
			long id = req.getId();
			int fightType = req.getFightType();
			if (VillageFightManager.logger.isWarnEnabled()) VillageFightManager.logger.warn("[村庄战报名] [收到摇骰子协议] [" + player.getLogString() + "] [Id: " + id + "]");
			if (fightType == 1) {
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(id);
				if (jiazu != null) {
					jiazu.setPoint(0 - jiazu.getPoint());
					if (VillageFightManager.logger.isWarnEnabled()) VillageFightManager.logger.warn("[村庄战报名] [收到摇骰子协议] [" + player.getLogString() + "] [jiazuId: " + player.getJiazuId() + "] [point:" + jiazu.getPoint() + "]");
					return null;
				}
			} else if (fightType == 0) {
				ZongPai zongpai = ZongPaiManager.getInstance().getZongPaiById(id);
				if (zongpai != null) {
					zongpai.setPoint(0 - zongpai.getPoint());
					if (CityFightManager.logger.isWarnEnabled()) CityFightManager.logger.warn("[城战报名] [收到摇骰子协议] [" + player.getLogString() + "] [zongpaiName:" + player.getZongPaiName() + "] [zongpaiId: " + zongpai.getId() + "] [point:" + zongpai.getPoint() + "]");
				}
			}
		} else if (message instanceof DICE_TRANSFER_REQ) {
			DICE_TRANSFER_REQ req = (DICE_TRANSFER_REQ) message;
			long pid = req.getPId();
			if (PlayerManager.getInstance().isOnline(pid)) {
				Player p = PlayerManager.getInstance().getPlayer(pid);
				if (p != null && p.getCurrentGame() != null) {
					if (WolfManager.getInstance().isWolfGame(p)) {
						WolfManager.getInstance().playerLeave(p,"离开副本");
						p.getCurrentGame().transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()));
					} else {
						p.getCurrentGame().transferGame(p, new TransportData(0, 0, 0, 0, DiceManager.tranMapName, DiceManager.x, DiceManager.y));
					}
				}
			}
		} else if (message instanceof WOLF_ENTER_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof WOLF_SIGN_UP_QUERY_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof WOLF_SIGNUP_NUM_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof WOLF_USE_SKILL_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof WOLF_SIGN_UP_SURE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof DICE_SIGN_UP_SURE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof COUNTRY_VOTE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof COUNTRY_COMMISSION_OR_RECALL_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof COUNTRY_HONOURSOR_OR_CANCEL_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof COUNTRY_COMMEND_OR_CANCEL_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_COUNTRY_COMMISSION_OR_RECALL_REQ) {
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if (cm != null && pm != null) {
				cm.返回查询任命或罢免国家官员列表(player);
			}
		} else if (message instanceof COUNTRY_LINGQU_FENGLU_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof COUNTRY_FAFANG_FENGLU_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_COUNTRY_WANGZHEZHIYIN_REQ) {
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if (cm != null && pm != null) {
				cm.得到王者之印res(player);
			}
		} else if (message instanceof QUERY_COUNTRY_YULINJUN_REQ) {
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if (cm != null && pm != null) {
				cm.得到御林卫队res(player);
			}
		} else if (message instanceof QUERY_COUNTRY_JINKU_REQ) {
			CountryManager cm = CountryManager.getInstance();
			PlayerManager pm = PlayerManager.getInstance();
			if (cm != null && pm != null) {
				cm.查看金库资金((QUERY_COUNTRY_JINKU_REQ) message, player);
			}
		} else if (message instanceof QUERY_SEAL_REQ) {
			if (player.getSealState()) {
				String[] descriptions = new String[0];
				long[] articleEntityId = new long[0];
				TaskManager tm = TaskManager.getInstance();
				try {
					long taskId = SealManager.getInstance().getSealTaskId(player.getCountry());
					Task task = tm.getTask(taskId);
					int max = task.getDailyTaskMaxNum();
					TaskEntity te = player.getTaskEntity(taskId);
					int leftCount = max;
					if (te != null) {
						leftCount = te.getCycleDeilverInfo().getIntValue();
					}
					int cost = SealManager.得到发布封印任务的费用(max, leftCount);
					String str1 = Translate.translateString(Translate.封印信息1, new String[][] { { Translate.STRING_1, Translate.太白金星 }, { Translate.STRING_2, Translate.冥火令 } });
					String str2 = Translate.translateString(Translate.封印信息2, new String[][] { { Translate.STRING_2, Translate.太白金星 }, { Translate.STRING_1, Translate.冥火令 } });
					String str3 = Translate.translateString(Translate.封印信息3, new String[][] { { Translate.COUNT_1, (max - leftCount + 1) + "" }, { Translate.COUNT_2, (leftCount < 0 ? "0" : leftCount + "") }, { Translate.COUNT_3, BillingCenter.得到带单位的银两(cost) } });
					Calendar calendar = Calendar.getInstance();
					SealManager sealManager = SealManager.getInstance();
					calendar.setTimeInMillis(sealManager.seal.sealCanOpenTime);
					String str = Translate.translateString(Translate.封印开启时间, new String[][] { { Translate.COUNT_1, calendar.get(Calendar.YEAR) + "" }, { Translate.COUNT_2, (calendar.get(Calendar.MONTH) + 1) + "" }, { Translate.COUNT_3, calendar.get(Calendar.DAY_OF_MONTH) + "" }, { Translate.COUNT_4, calendar.get(Calendar.HOUR_OF_DAY) + "" }, { Translate.COUNT_5, calendar.get(Calendar.MINUTE) + "" }, { Translate.STRING_1, CountryManager.得到官职名(CountryManager.国王) } });
					descriptions = new String[] { str1, str2, str3, str };
					ArticleManager am = ArticleManager.getInstance();
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					if (am != null && aem != null) {
						Article a1 = am.getArticle(SealManager.封印物品1);
						Article a2 = am.getArticle(SealManager.封印物品2);
						Article a3 = am.getArticle(SealManager.封印物品3);
						articleEntityId = new long[3];
						if (a1 != null) {
							ArticleEntity ae1 = aem.createTempEntity(a1, false, 0, player, a1.getColorType());
							articleEntityId[0] = ae1.getId();
						}
						if (a2 != null) {
							ArticleEntity ae2 = aem.createTempEntity(a2, false, 0, player, a2.getColorType());
							articleEntityId[1] = ae2.getId();
						}
						if (a3 != null) {
							ArticleEntity ae3 = aem.createTempEntity(a3, false, 0, player, a3.getColorType());
							articleEntityId[2] = ae3.getId();
						}
					}
					QUERY_SEAL_RES res = new QUERY_SEAL_RES(message.getSequnceNum(), taskId, descriptions, articleEntityId);
					player.addMessageToRightBag(res);
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[封印查询] [异常]", ex);
					ex.printStackTrace();
				}
			}
		} else if (message instanceof LEVELUP_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_FLOPNPC_REQ) {
			QUERY_FLOPNPC_REQ req = (QUERY_FLOPNPC_REQ) message;
			long[] ids = req.getNPCId();
			NPCManager nm = MemoryNPCManager.getNPCManager();
			String[] oneClassNames = new String[ids.length];
			int[] colors = new int[ids.length];
			if (nm != null) {
				for (int i = 0; i < ids.length; i++) {
					NPC npc = nm.getNPC(ids[i]);
					if (npc instanceof FlopCaijiNpc) {
						ArticleEntity ae = ((FlopCaijiNpc) npc).getAe();
						oneClassNames[i] = "";
						if (ae != null) {
							ArticleManager am = ArticleManager.getInstance();
							if (am != null) {
								Article a = am.getArticle(ae.getArticleName());
								if (a != null && a.get物品一级分类() != null) {
									oneClassNames[i] = a.get物品一级分类();
								}
							}
							colors[i] = ae.getColorType();
						} else {
							colors[i] = -1;
						}
					} else {
						oneClassNames[i] = "";
						colors[i] = -1;
					}
				}

				QUERY_FLOPNPC_RES res = new QUERY_FLOPNPC_RES(GameMessageFactory.nextSequnceNum(), ids, oneClassNames, colors);
				player.addMessageToRightBag(res);
			}

		} else if (message instanceof CHANGE_PKMODE_REQ) {
			CHANGE_PKMODE_REQ req = (CHANGE_PKMODE_REQ) message;
			byte pkMode = req.getPkMode();
			if (player.getLevel() <= PlayerManager.保护最大级别 && pkMode > Player.和平模式) {
				HINT_REQ hreq = new HINT_REQ(req.getSequnceNum(), (byte) 5, Translate.不能开启其他模式);
				player.addMessageToRightBag(hreq);
				return null;
			}
			try {
				ChestFightManager cInst = ChestFightManager.inst;
				if (cInst != null && cInst.isPlayerInActive(player)) {
					player.setPkMode(Player.全体模式);
					player.sendError(Translate.副本中不允许切换PK模式);
					return null;
				}
			} catch (Exception e) {

			}
			if (pkMode >= Player.和平模式 && pkMode <= Player.善恶模式) {
				player.setPkMode(pkMode);
			}
		} else if (message instanceof REMOVE_EQUIPMENT_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof SWITCH_SUIT_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof EQUIPMENT_INLAY_UUB_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof EQUIPMENT_INLAY_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof EQUIPMENT_OUTLAY_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_EQUIPMENT_INSCRIPTION_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				return am.queryEquipmentInscriptionReq(player, (QUERY_EQUIPMENT_INSCRIPTION_REQ) message);
			}
		} else if (message instanceof EQUIPMENT_INSCRIPTION_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_EQUIPMENT_STRONG_REQ) {
			if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				player.sendError(Translate.请更新最新的客户端再强化);
				return null;
			}
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				return am.queryEquipmentStrongReq(player, (QUERY_EQUIPMENT_STRONG_REQ) message);
			}
		} else if (message instanceof QUERY_NEW_EQUIPMENT_STRONG_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				try {
					return am.queryNewEquipmentStrongReq(player, (QUERY_NEW_EQUIPMENT_STRONG_REQ) message);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		} else if (message instanceof QUERY_NEW_EQUIPMENT_STRONG2_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				try {
					return am.queryNewEquipmentStrongReq2(player, (QUERY_NEW_EQUIPMENT_STRONG2_REQ) message);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		} else if (message instanceof QUERY_NEW_EQUIPMENT_STRONG3_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				try {
					return am.queryNewEquipmentStrongReq3(player, (QUERY_NEW_EQUIPMENT_STRONG3_REQ) message);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		} else if (message instanceof EQUIPMENT_STRONG_REQ) {
			if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				player.sendError(Translate.请更新最新的客户端再强化);
				return null;
			}
			pushMessageToGame(player, message, null);
		} else if (message instanceof NEW_EQUIPMENT_STRONG_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_EQUIPMENT_JIANDING_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				return am.queryEquipmentJianDingReq(player, (QUERY_EQUIPMENT_JIANDING_REQ) message);
			}
		} else if (message instanceof EQUIPMENT_JIANDING_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof NEW_JIANDING_OK_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_EQUIPMENT_PICKSTAR_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				return am.queryEquipmentPickStarReq(player, (QUERY_EQUIPMENT_PICKSTAR_REQ) message);
			}
		} else if (message instanceof EQUIPMENT_PICKSTAR_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_EQUIPMENT_INSERTSTAR_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				return am.queryEquipmentInsertStarReq(player, (QUERY_EQUIPMENT_INSERTSTAR_REQ) message);
			}
		} else if (message instanceof EQUIPMENT_INSERTSTAR_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof EQUIPMENT_DEVELOP_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof EQUIPMENT_DEVELOPUP_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_EQUIPMENT_BIND_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				return am.queryEquipmentBindReq(player, (QUERY_EQUIPMENT_BIND_REQ) message);
			}
		} else if (message instanceof EQUIPMENT_BIND_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof EQUIPMENT_UP_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_ARTICLE_COMPOSE_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			if (am != null) {
				return am.queryArticleComposeReq(player, (QUERY_ARTICLE_COMPOSE_REQ) message);
			}
		} else if (message instanceof ARTICLE_COMPOSE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof EQUIPMENT_RESET_REQ) {

		} else if (message instanceof QUERY_FLOPAVAILABLE_REQ) {

		} else if (message instanceof PREPARE_PLAY_DICE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof CPATAIN_ASSIGN_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof PICKUP_MONEY_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof PICKUP_FLOP_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof PICKUP_ALLFLOP_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof PICKUP_CAIJINPC_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof GOD_EQUIPMENT_UPGRADE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof GOD_EQUIPMENT_UPGRADE_SURE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof CONFIRM_ARTICLE_EXCHANGE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_ARTICLE_EXCHANGE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof COLLECT_MATERIAL_FOR_BOSS_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof SEAL_TASK_INFO_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof ACTIVITY_FIRST_PAGE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof ACTIVITY_FIRST_PAGE2_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof GET_ACTIVITY_INFO_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof NOTICE_ACTIVITY_STAT_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof NOTICE_ACTIVITY_BUTTON_STAT_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof CREATE_DEAL_REQ) {
			if (player.isDeath()) {
				player.sendError(Translate.text_deal_005);
				return null;
			}
			if (player.isFighting()) {
				player.sendError(Translate.text_deal_006);
				return null;
			}
			if (player.isLocked()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的角色处于锁定状态暂时不能参与交易);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.isBoothState()) {
				player.sendError(Translate.text_deal_007);
				return null;
			}

			if (player.isAppStoreChannel()) {
				if (player.getLevel() < 20) {
					player.sendError(Translate.text_deal_008);
					return null;
				}
			}
			try {
				if (RelationShipHelper.checkForbidAndSendMessage(player)) {
					if (RelationShipHelper.logger.isInfoEnabled()) {
						RelationShipHelper.logger.info("[玩家因为打金行为被限制交易] [" + player.getLogString() + "]");
					}
					return null;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}

			if (player.isInCrossServer()) {
				// DealCenter.logger.warn("[交易] [创建] [在跨服服务器，不能交易] [user:" + player.getName() + "] [id:" + player.getId() + "]");
				if (DealCenter.logger.isWarnEnabled()) DealCenter.logger.warn("[交易] [创建] [在跨服服务器，不能交易] [user:{}] [id:{}]", new Object[] { player.getName(), player.getId() });
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您现在身处跨服服务器暂时不能参与交易);
				player.addMessageToRightBag(nreq);
				return null;
			}
			CREATE_DEAL_REQ req = (CREATE_DEAL_REQ) message;
			long withPlayerId = req.getWithPlayerId();

			if (withPlayerId == player.getId()) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您不能与自己交易);
				if (DealCenter.logger.isInfoEnabled()) {
					// DealCenter.logger.info("[交易] [创建] [user:" + player.getUsername() + "] [id:" + player.getId() + "] [name:" + player.getName() + "] [您不能与自己交易！]");
					if (DealCenter.logger.isInfoEnabled()) DealCenter.logger.info("[交易] [创建] [user:{}] [id:{}] [name:{}] [您不能与自己交易！]", new Object[] { player.getUsername(), player.getId(), player.getName() });
				}
				player.addMessageToRightBag(hint);
				return null;
			}
			Player withPlayer = playerManager.getPlayer(withPlayerId);

			if (ChatMessageService.getInstance().isSilence(player.getId()) >= 2) {
				EnterLimitManager.logger.warn("[用户被限制交易] [" + player.getUsername() + "] [" + player.getName() + "] [" + player.getId() + "] [" + player.getLevel() + "] 银子:[" + player.getSilver() + "]");
				return null;
			}

			if (ChatMessageService.getInstance().isSilence(withPlayer.getId()) >= 2) {
				EnterLimitManager.logger.warn("[用户被限制交易] [" + withPlayer.getUsername() + "] [" + withPlayer.getName() + "] [" + withPlayer.getId() + "] [" + withPlayer.getLevel() + "] 银子:[" + withPlayer.getSilver() + "]");
				return null;
			}

			if (withPlayer.isBoothState()) {
				player.sendError(Translate.text_deal_009);
				return null;
			}
			if (withPlayer.isDeath()) {
				player.sendError(Translate.text_deal_010);
				return null;
			}
			if (withPlayer.isFighting()) {
				player.sendError(Translate.text_deal_011);
				return null;
			}
			if (player.getCountry() != withPlayer.getCountry()) {
				player.sendError(Translate.text_deal_012);
				return null;
			}
			if (withPlayer.isAppStoreChannel()) {
				if (withPlayer.getLevel() < 20) {
					player.sendError(Translate.text_deal_008);
					return null;
				}
			}
			if (player.getCurrentGame() != withPlayer.getCurrentGame()) {
				player.sendError(Translate.text_deal_013);
				return null;
			}
			int xW = player.getX() - withPlayer.getX();
			int yW = player.getY() - withPlayer.getY();
			if (xW * xW + yW * yW > 300 * 300) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_deal_013);
				player.addMessageToRightBag(hint);
				return null;
			}
			Deal deal = dealCenter.getDeal(player);
			if (deal != null && deal.getPlayerB() != null && player.getId() == deal.getPlayerB().getId()) {
				// 乙方同意进行交易
				if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - deal.getStartTime() > GameDealCenterProxy.timeout) {
					dealCenter.cancelDeal(deal, null);
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_deal_014);
					player.addMessageToRightBag(hint);
					dealCenter.cancelDeal(deal, null);
					return null;
				}
				deal.setCreateFlagB(true);
				if (deal.isCreateFlagA() && deal.isCreateFlagB()) {
					// 通知双方，可以开始交易
					DEAL_CREATED_REQ notifyReqA = new DEAL_CREATED_REQ(GameMessageFactory.nextSequnceNum(), deal.getPlayerB().getId(), (int) (DefaultDealCenter.DEAL_TAX * 100));
					deal.getPlayerA().addMessageToRightBag(notifyReqA);
					DEAL_CREATED_REQ notifyReqB = new DEAL_CREATED_REQ(GameMessageFactory.nextSequnceNum(), deal.getPlayerA().getId(), (int) (DefaultDealCenter.DEAL_TAX * 100));
					deal.getPlayerB().addMessageToRightBag(notifyReqB);

					// deal.getPlayerA().sendError(Translate.text_deal_015);
					// deal.getPlayerB().sendError(Translate.text_deal_015);

					if (DealCenter.logger.isInfoEnabled() && deal != null) {
						// DealCenter.logger.info("[交易] [开始交易] [playerA:" + (deal.getPlayerA() != null ? deal.getPlayerA().getUsername() : "") + "] [playerAid:" +
						// (deal.getPlayerA() != null ? deal.getPlayerA().getId() : "") + "] [playerAname:" + (deal.getPlayerA() != null ? deal.getPlayerA().getName() : "") +
						// "] [playerB:" + (deal.getPlayerB() != null ? deal.getPlayerB().getUsername() : "") + "] [playerBid:" + (deal.getPlayerB() != null ?
						// deal.getPlayerB().getId() : "") + "] [playerBname:" + (deal.getPlayerB() != null ? deal.getPlayerB().getName() : "") + "]");
						if (DealCenter.logger.isInfoEnabled()) DealCenter.logger.info("[交易] [开始交易] [playerA:{}] [playerAid:{}] [playerAname:{}] [playerB:{}] [playerBid:{}] [playerBname:{}]", new Object[] { (deal.getPlayerA() != null ? deal.getPlayerA().getUsername() : ""), (deal.getPlayerA() != null ? deal.getPlayerA().getId() : ""), (deal.getPlayerA() != null ? deal.getPlayerA().getName() : ""), (deal.getPlayerB() != null ? deal.getPlayerB().getUsername() : ""), (deal.getPlayerB() != null ? deal.getPlayerB().getId() : ""), (deal.getPlayerB() != null ? deal.getPlayerB().getName() : "") });
					}
				}
			} else {
				if (deal != null) {
					if (deal.getStartTime() + GameDealCenterProxy.reqTimeOut < SystemTime.currentTimeMillis()) {
						dealCenter.cancelDeal(deal, player);
					} else {
						player.sendError(Translate.text_deal_002);
						return null;
					}
				}
				if (withPlayer.getDuelFieldState() > 0) {
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2715);
					if (DealCenter.logger.isInfoEnabled()) {
						// DealCenter.logger.info("[交易] [创建] [user:" + player.getUsername() + "] [id:" + player.getId() + "] [name:" + player.getName() + "] [对方在比武赛场，暂时不能交易！]");
						if (DealCenter.logger.isInfoEnabled()) DealCenter.logger.info("[交易] [创建] [user:{}] [id:{}] [name:{}] [对方在比武赛场，暂时不能交易！]", new Object[] { player.getUsername(), player.getId(), player.getName() });
					}
					player.addMessageToRightBag(hint);
					return null;
				}
				if (withPlayer.hiddenTransformPop) {
					player.sendError(Translate.该玩家已经屏蔽交易);
					return null;
				}
				int validateState = OtherValidateManager.getInstance().getValidateState(player, OtherValidateManager.ASK_TYPE_DEAL);
				if (validateState == ValidateManager.VALIDATE_STATE_需要答题) {
					ValidateAsk ask = OtherValidateManager.getInstance().sendValidateAsk(player, OtherValidateManager.ASK_TYPE_DEAL);
					ask.setAskFormParameters(new Object[] { withPlayer });
					return null;
				}
				((GameDealCenterProxy) GameDealCenterProxy.getInstance()).doCreateDeal(player, withPlayer);
			}
		} else if (message instanceof DEAL_ADD_ARTICLE_REQ) {
			if (player.isLocked()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的角色处于锁定状态暂时不能参与交易);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.isInCrossServer()) {
				// DealCenter.logger.warn("[交易] [创建] [在跨服服务器，不能交易] [user:" + player.getName() + "] [id:" + player.getId() + "]");
				if (DealCenter.logger.isWarnEnabled()) DealCenter.logger.warn("[交易] [创建] [在跨服服务器，不能交易] [user:{}] [id:{}]", new Object[] { player.getName(), player.getId() });
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您现在身处跨服服务器暂时不能参与交易);
				player.addMessageToRightBag(nreq);
				return null;
			}
			DEAL_ADD_ARTICLE_REQ req = (DEAL_ADD_ARTICLE_REQ) message;
			int index = req.getIndex();
			byte packageType = req.getPackageType();
			long entityId = req.getEntityId();
			int count = req.getCount();
			if (count < 0) {
				DEAL_ADD_ARTICLE_RES res = new DEAL_ADD_ARTICLE_RES(req.getSequnceNum(), Translate.text_trade_006);
				return res;
			}
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(entityId);
			if (ae == null || ae.isBinded() || ae.isRealBinded()) {
				DEAL_ADD_ARTICLE_RES res = new DEAL_ADD_ARTICLE_RES(req.getSequnceNum(), Translate.text_trade_005);
				return res;
			}
			if (!GreenServerManager.canToOtherPlayer(ae)) {
				DEAL_ADD_ARTICLE_RES res = new DEAL_ADD_ARTICLE_RES(req.getSequnceNum(), Translate.text_trade_023);
				return res;
			}
			try {
				dealCenter.addArticle(player, packageType, index, entityId, count);
				Deal deal = dealCenter.getDeal(player);
				if (DealCenter.logger.isInfoEnabled() && deal != null) {
					// DealCenter.logger.info("[交易] [添加物品] [playerA:" + (deal.getPlayerA() != null ? deal.getPlayerA().getUsername() : "") + "] [playerAid:" + (deal.getPlayerA() !=
					// null ? deal.getPlayerA().getId() : "") + "] [playerAname:" + (deal.getPlayerA() != null ? deal.getPlayerA().getName() : "") + "] [playerB:" +
					// (deal.getPlayerB() != null ? deal.getPlayerB().getUsername() : "") + "] [playerBid:" + (deal.getPlayerB() != null ? deal.getPlayerB().getId() : "") +
					// "] [playerBname:" + (deal.getPlayerB() != null ? deal.getPlayerB().getName() : "") + "] [添加人playerid:" + player.getId() + "] [index:" + index +
					// "] [entityId:" + entityId + "] [count:" + count + "]");
					if (DealCenter.logger.isInfoEnabled()) DealCenter.logger.info("[交易] [添加物品] [playerA:{}] [playerAid:{}] [playerAname:{}] [playerB:{}] [playerBid:{}] [playerBname:{}] [添加人playerid:{}] [index:{}] [entityId:{}] [count:{}]", new Object[] { (deal.getPlayerA() != null ? deal.getPlayerA().getUsername() : ""), (deal.getPlayerA() != null ? deal.getPlayerA().getId() : ""), (deal.getPlayerA() != null ? deal.getPlayerA().getName() : ""), (deal.getPlayerB() != null ? deal.getPlayerB().getUsername() : ""), (deal.getPlayerB() != null ? deal.getPlayerB().getId() : ""), (deal.getPlayerB() != null ? deal.getPlayerB().getName() : ""), player.getId(), index, entityId, count });
				}
				DEAL_ADD_ARTICLE_RES res = new DEAL_ADD_ARTICLE_RES(req.getSequnceNum(), "");
				return res;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				DEAL_ADD_ARTICLE_RES res = new DEAL_ADD_ARTICLE_RES(req.getSequnceNum(), e.getMessage());
				return res;
			}
		} else if (message instanceof DEAL_DELETE_ARTICLE_REQ) {
			DEAL_DELETE_ARTICLE_REQ req = (DEAL_DELETE_ARTICLE_REQ) message;
			int index = req.getIndex();
			dealCenter.deleteArticle(player, index);
		} else if (message instanceof DEAL_MOD_COINS_REQ) {
			// if (player.isLocked()) {
			// HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2712);
			// player.addMessageToRightBag(nreq);
			// return null;
			// }
			// if (player.isInCrossServer()) {
			// // DealCenter.logger.warn("[交易] [创建] [在跨服服务器，不能交易] [user:" + player.getName() + "] [id:" + player.getId() + "]");
			// if (DealCenter.logger.isWarnEnabled()) DealCenter.logger.warn("[交易] [创建] [在跨服服务器，不能交易] [user:{}] [id:{}]", new Object[] { player.getName(), player.getId() });
			// HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2713);
			// player.addMessageToRightBag(nreq);
			// return null;
			// }
			DEAL_MOD_COINS_REQ req = (DEAL_MOD_COINS_REQ) message;
			int coins = req.getCoins();
			if (coins < 0 || coins > 999999999L) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.非法输入);
				player.addMessageToRightBag(hint);
			} else {
				boolean succ = dealCenter.updateCoins(player, coins);
				if (!succ) {
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.余额不足);
					player.addMessageToRightBag(hint);
				}
			}
		} else if (message instanceof DEAL_LOCK_REQ) {
			// if (player.isLocked()) {
			// HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2712);
			// player.addMessageToRightBag(nreq);
			// return null;
			// }
			// if (player.isInCrossServer()) {
			// // DealCenter.logger.warn("[交易] [创建] [在跨服服务器，不能交易] [user:" + player.getName() + "] [id:" + player.getId() + "]");
			// if (DealCenter.logger.isWarnEnabled()) DealCenter.logger.warn("[交易] [创建] [在跨服服务器，不能交易] [user:{}] [id:{}]", new Object[] { player.getName(), player.getId() });
			// HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2713);
			// player.addMessageToRightBag(nreq);
			// return null;
			// }
			Deal deal = dealCenter.getDeal(player);
			if (deal == null) {
				// logger.warn("[deal_not_found] [" + player.getName() + "]");
				if (logger.isWarnEnabled()) logger.warn("[deal_not_found] [{}]", new Object[] { player.getName() });
				return null;
			}
			dealCenter.lockDeal(deal, player, ((DEAL_LOCK_REQ) message).getIsLock());
		} else if (message instanceof DEAL_AGREE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof DEAL_DISAGREE_REQ) {
			Deal deal = dealCenter.getDeal(player);
			if (deal != null) {
				dealCenter.disagreeDeal(deal, player);
			} else {
				player.sendError(Translate.text_deal_017);
			}
		} else if (message instanceof DEAL_CANCEL_REQ) {
			Deal deal = dealCenter.getDeal(player);
			if (deal != null) {
				dealCenter.cancelDeal(deal, player);
			}
		} else if (message instanceof WAREHOUSE_GET_REQ) {
			// if (player.isInCrossServer()) {
			// HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2721);
			// player.addMessageToRightBag(nreq);
			// return null;
			// }
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setTitle(Translate.仓库);
			List<Option> opList = new ArrayList<Option>();
			Option_Cangku_OpenCangku cangku = new Option_Cangku_OpenCangku();
			cangku.setText(Translate.打开仓库);
			opList.add(cangku);
			Option_Cangku_OpenCangku2 ck2 = new Option_Cangku_OpenCangku2();
			ck2.setText(Translate.打开2仓库);
			opList.add(ck2);
			if (player.getCangkuPassword() == null || player.getCangkuPassword().trim().equals("")) {
				Option_Cangku_ShedingPassword sd = new Option_Cangku_ShedingPassword();
				sd.setText(Translate.设置密码);
				opList.add(sd);
			} else {
				Option_Cangku_XiugaiPassword xg = new Option_Cangku_XiugaiPassword();
				xg.setText(Translate.修改密码);
				opList.add(xg);
			}
			if (player.getKnapsacks_cangku().getCells().length < Player.仓库最大格子数量) {
				Option_Cangku_AddCell addCell = new Option_Cangku_AddCell();
				addCell.setText(Translate.仓库扩展);
				opList.add(addCell);
			}
			if (player.getKnapsacks_warehouse().getCells().length < Player.仓库最大格子数量) {
				Option_Cangku_AddCell2 addCell = new Option_Cangku_AddCell2();
				addCell.setText(Translate.仓库2扩展);
				opList.add(addCell);
			}
			mw.setOptions(opList.toArray(new Option[0]));
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, opList.toArray(new Option[0]));
			player.addMessageToRightBag(res);
		} else if (message instanceof WAREHOUSE_GET_CARRY_REQ) {
			// if (player.isInCrossServer()) {
			// HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2721);
			// player.addMessageToRightBag(nreq);
			// return null;
			// }
			VipManager vm = VipManager.getInstance();
			if (vm != null && vm.vip是否开启随身仓库(player)) {
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				mw.setTitle(Translate.仓库);
				List<Option> opList = new ArrayList<Option>();
				Option_Cangku_OpenCangku cangku = new Option_Cangku_OpenCangku();
				cangku.setText(Translate.打开仓库);
				opList.add(cangku);
				Option_Cangku_OpenCangku2 ck2 = new Option_Cangku_OpenCangku2();
				ck2.setText(Translate.打开2仓库);
				opList.add(ck2);
				if (player.getCangkuPassword() == null || player.getCangkuPassword().trim().equals("")) {
					Option_Cangku_ShedingPassword sd = new Option_Cangku_ShedingPassword();
					sd.setText(Translate.设置密码);
					opList.add(sd);
				} else {
					Option_Cangku_XiugaiPassword xg = new Option_Cangku_XiugaiPassword();
					xg.setText(Translate.修改密码);
					opList.add(xg);
				}
				if (player.getKnapsacks_cangku().getCells().length < Player.仓库最大格子数量) {
					Option_Cangku_AddCell addCell = new Option_Cangku_AddCell();
					addCell.setText(Translate.仓库扩展);
					opList.add(addCell);
				}
				if (player.getKnapsacks_warehouse().getCells().length < Player.仓库最大格子数量) {
					Option_Cangku_AddCell2 addCell = new Option_Cangku_AddCell2();
					addCell.setText(Translate.仓库2扩展);
					opList.add(addCell);
				}
				mw.setOptions(opList.toArray(new Option[0]));
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, opList.toArray(new Option[0]));
				player.addMessageToRightBag(res);
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您的vip级别不足);
				player.addMessageToRightBag(hreq);
			}

		} else if (message instanceof WAREHOUSE_INPUT_PASSWORD_REQ) {
			String inputContent = ((WAREHOUSE_INPUT_PASSWORD_REQ) message).getPassword();
			if (player.isInCrossServer()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2721);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.pressWrongPWCount >= Player.MAX_WRONG_NUM) {
				if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - player.pressWrongPWTimePoint < Player.仓库密码输入错误保护持续时间) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您输入的次数过多请稍后再试);
					player.addMessageToRightBag(hreq);
					return null;
				}
			}
			if (inputContent != null && inputContent.trim().equals(player.getCangkuPassword().trim())) {
				player.pressWrongPWCount = 0;
				player.OpenCangku();
			} else {
				player.pressWrongPWCount++;
				player.pressWrongPWTimePoint = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				if (player.pressWrongPWCount >= Player.MAX_WRONG_NUM) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您又输入错了);
					player.addMessageToRightBag(hreq);
					return null;
				}
				String description = Translate.translateString(Translate.密码错误请重新输入密码详细, new String[][] { { Translate.COUNT_1, player.pressWrongPWCount + "" } });
				WAREHOUSE_INPUT_PASSWORD_RES res = new WAREHOUSE_INPUT_PASSWORD_RES(GameMessageFactory.nextSequnceNum(), description);
				player.addMessageToRightBag(res);
			}
		} else if (message instanceof NEW_WAREHOUSE_INPUT_PASSWORD_REQ) {
			NEW_WAREHOUSE_INPUT_PASSWORD_REQ req = (NEW_WAREHOUSE_INPUT_PASSWORD_REQ) message;
			// String inputContent = ((NEW_WAREHOUSE_INPUT_PASSWORD_REQ) message).getPassword();
			String inputContent = req.getPassword();
			int warehouseType = req.getWarehorseType();
			if (player.isInCrossServer()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2721);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.pressWrongPWCount >= Player.MAX_WRONG_NUM) {
				if (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - player.pressWrongPWTimePoint < Player.仓库密码输入错误保护持续时间) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您输入的次数过多请稍后再试);
					player.addMessageToRightBag(hreq);
					return null;
				}
			}
			if (inputContent != null && inputContent.trim().equals(player.getCangkuPassword().trim())) {
				player.pressWrongPWCount = 0;
				if (warehouseType != 6) {
					player.OpenCangku();
				} else {
					player.openCanku2();
				}
			} else {
				player.pressWrongPWCount++;
				player.pressWrongPWTimePoint = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				if (player.pressWrongPWCount >= Player.MAX_WRONG_NUM) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您又输入错了);
					player.addMessageToRightBag(hreq);
					return null;
				}
				String description = Translate.translateString(Translate.密码错误请重新输入密码详细, new String[][] { { Translate.COUNT_1, player.pressWrongPWCount + "" } });
				NEW_WAREHOUSE_INPUT_PASSWORD_RES res = new NEW_WAREHOUSE_INPUT_PASSWORD_RES(GameMessageFactory.nextSequnceNum(), warehouseType, description);
				player.addMessageToRightBag(res);
			}
		} else if (message instanceof WAREHOUSE_SET_PASSWORD_REQ) {
			if (player.isInCrossServer()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2721);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.getCangkuPassword() == null || player.getCangkuPassword().trim().equals("")) {
				WAREHOUSE_SET_PASSWORD_REQ req = (WAREHOUSE_SET_PASSWORD_REQ) message;
				String s = req.getPassword();
				if (s == null || s.trim().equals("")) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.设定密码不能为空);
					player.addMessageToRightBag(hreq);
					return null;
				}
				s = s.trim();
				if (s.length() < 4) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.密码长度最短位数);
					player.addMessageToRightBag(hreq);
					return null;
				}
				if (s.length() > 12) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.密码长度最长位数);
					player.addMessageToRightBag(hreq);
					return null;
				}
				player.setCangkuPassword(s);
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.设定密码成功, new String[][] { { Translate.STRING_1, s } }));
				player.addMessageToRightBag(hreq);
			}

		} else if (message instanceof WAREHOUSE_MODIFY_PASSWORD_REQ) {
			// if (player.isInCrossServer()) {
			// HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2721);
			// player.addMessageToRightBag(nreq);
			// return null;
			// }
			WAREHOUSE_MODIFY_PASSWORD_REQ req = (WAREHOUSE_MODIFY_PASSWORD_REQ) message;
			String old = req.getPassword();
			if (player.getCangkuPassword() != null && !player.getCangkuPassword().trim().equals("") && player.getCangkuPassword().equals(old)) {
				String s = req.getModifyPassword();
				if (s == null || s.trim().equals("")) {
					player.setCangkuPassword("");
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.清除密码成功);
					player.addMessageToRightBag(hreq);
					return null;
				}
				if (s.length() < 4) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.密码长度最短位数);
					player.addMessageToRightBag(hreq);
					return null;
				}
				if (s.length() > 12) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.密码长度最长位数);
					player.addMessageToRightBag(hreq);
					return null;
				}
				player.setCangkuPassword(s);
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.设定密码成功, new String[][] { { Translate.STRING_1, s } }));
				player.addMessageToRightBag(hreq);
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.原始密码错误);
				player.addMessageToRightBag(hreq);
			}
		} else if (message instanceof WAREHOUSE_ARRANGE_REQ) {
			// if (player.isInCrossServer()) {
			// HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2721);
			// player.addMessageToRightBag(nreq);
			// return null;
			// }
			Knapsack knapsack = player.getKnapsacks_cangku();
			if (knapsack != null) {
				knapsack.autoArrange();
				int count = knapsack.getCells().length;
				long[] entityIds = new long[count];
				int[] counts = new int[count];
				for (int j = 0; j < count; j++) {
					Cell cell = knapsack.getCells()[j];
					if (cell != null) {
						entityIds[j] = cell.entityId;
						counts[j] = cell.count;
					}
				}
				WAREHOUSE_GET_RES res = new WAREHOUSE_GET_RES(GameMessageFactory.nextSequnceNum(), false, entityIds, counts);
				player.addMessageToRightBag(res);
			}
		} else if (message instanceof NEW_WAREHOUSE_ARRANGE_REQ) {
			Knapsack knapsack = player.getKnapsacks_warehouse();
			if (knapsack != null) {
				knapsack.autoArrange();
				int count = knapsack.getCells().length;
				long[] entityIds = new long[count];
				int[] counts = new int[count];
				for (int j = 0; j < count; j++) {
					Cell cell = knapsack.getCells()[j];
					if (cell != null) {
						entityIds[j] = cell.entityId;
						counts[j] = cell.count;
					}
				}
				NEW_WAREHOUSE_GET_RES res = new NEW_WAREHOUSE_GET_RES(GameMessageFactory.nextSequnceNum(), 6, true, entityIds, counts);
				// WAREHOUSE_GET_RES res = new WAREHOUSE_GET_RES(GameMessageFactory.nextSequnceNum(), false, entityIds, counts);
				player.addMessageToRightBag(res);
			}
		} else if (message instanceof WAREHOUSE_MOVE_ARTICLE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof WAREHOUSE_GETOUT_ARTICLE_REQ) {

		} else if (message instanceof SHOPS_NAME_GET_REQ) {

			SHOPS_NAME_GET_REQ req = (SHOPS_NAME_GET_REQ) message;

			if (req.getMarketType() == ShopManager.元宝商城类型) {
				String[] 元宝商城右边隐藏的商店的名字 = ShopManager.元宝商城右边隐藏的商店的名字;
				if (player.getVipLevel() > 0) {
					元宝商城右边隐藏的商店的名字 = ShopManager.元宝商城右边隐藏的商店的名字包含vip;
				}
				SHOPS_NAME_GET_RES res = new SHOPS_NAME_GET_RES(GameMessageFactory.nextSequnceNum(), req.getMarketType(), ShopManager.元宝商城左边标签显示的商店的名字, ShopManager.元宝商城左边标签显示的商店的图标, 元宝商城右边隐藏的商店的名字);
				player.addMessageToRightBag(res);
				// 返回第一个商店用于客户端显示
				if (ShopManager.元宝商城左边标签显示的商店的名字 != null && ShopManager.元宝商城左边标签显示的商店的名字.length > 0) {
					String shopName = ShopManager.元宝商城左边标签显示的商店的名字[0];
					ShopManager shopManager = ShopManager.getInstance();
					if (shopManager != null) {
						Shop shop = shopManager.getShop(shopName, player);
						if (shop != null) {
							byte marketType = 0;
							if (ShopManager.元宝商城左边标签显示的商店的名字 != null) {
								for (int i = 0; i < ShopManager.元宝商城左边标签显示的商店的名字.length; i++) {
									String name = ShopManager.元宝商城左边标签显示的商店的名字[i];
									if (shopName.equals(name)) {
										marketType = ShopManager.元宝商城类型;
									}
								}
							}
							if (元宝商城右边隐藏的商店的名字 != null) {
								for (int i = 0; i < 元宝商城右边隐藏的商店的名字.length; i++) {
									String name = 元宝商城右边隐藏的商店的名字[i];
									if (shopName.equals(name)) {
										marketType = ShopManager.元宝商城类型;
									}
								}
							}
							Goods gs[] = shop.getGoods(player);
							long[] coins = null;
							if (shop.shopType == ShopManager.商店_绑银商店) {
								coins = new long[2];
								coins[0] = player.getBindSilver();
								coins[1] = player.getSilver();
							} else if (shop.shopType == ShopManager.商店_资源商店) {
								coins = new long[3];
								Cave cave = FaeryManager.getInstance().getCave(player);
								if (cave != null) {
									ResourceCollection rc = cave.getCurrRes();
									if (rc != null) {
										coins[0] = rc.getFood();
										coins[1] = rc.getWood();
										coins[2] = rc.getStone();
									}
								}

							} else if (shop.shopType == ShopManager.商店_工资商店) {
								coins = new long[1];
								coins[0] = player.getWage();
							} else if (shop.shopType == ShopManager.商店_银子商店) {
								coins = new long[1];
								coins[0] = player.getSilver();
							} else {
								coins = new long[0];
							}
							com.fy.engineserver.shop.client.Goods[] cGoods = ShopManager.translate(player, shop, gs);
							SHOP_GET_RES shopGetRes = new SHOP_GET_RES(GameMessageFactory.nextSequnceNum(), marketType, shopName, shop.getVersion(), shop.shopType, coins, cGoods);
							player.addMessageToRightBag(shopGetRes);
							if (!shopName.equals(Translate.商店名字_历练兑换)) {
								SHOP_OTHER_INFO_RES shopOtherRes = new SHOP_OTHER_INFO_RES(GameMessageFactory.nextSequnceNum(), marketType, shopName, shop.shopType, cGoods);
								player.addMessageToRightBag(shopOtherRes);
							}
						}
					}
				}
			}

		} else if (message instanceof REFRESH_CROSS_SHOP_REQ) {
		} else if (message instanceof SHOP_GET_REQ) {

			SHOP_GET_REQ req = (SHOP_GET_REQ) message;

			ShopManager shopManager = ShopManager.getInstance();
			String shopName = req.getShopName();
			if (shopName.equals(Translate.随身商店)) {
				shopName = ShopManager.得到随身商店名字(player.getLevel());
			}
			Shop shop = shopManager.getShop(shopName, player);
			if (shop == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2776 + shopName + Translate.text_2777);
				player.addMessageToRightBag(hreq);

			} else {
				byte marketType = 0;
				if (ShopManager.元宝商城左边标签显示的商店的名字 != null) {
					for (int i = 0; i < ShopManager.元宝商城左边标签显示的商店的名字.length; i++) {
						String name = ShopManager.元宝商城左边标签显示的商店的名字[i];
						if (shopName.equals(name)) {
							marketType = ShopManager.元宝商城类型;
						}
					}
				}
				String[] 元宝商城右边隐藏的商店的名字 = ShopManager.元宝商城右边隐藏的商店的名字;
				if (player.getVipLevel() > 0) {
					元宝商城右边隐藏的商店的名字 = ShopManager.元宝商城右边隐藏的商店的名字包含vip;
				}
				if (元宝商城右边隐藏的商店的名字 != null) {
					for (int i = 0; i < 元宝商城右边隐藏的商店的名字.length; i++) {
						String name = 元宝商城右边隐藏的商店的名字[i];
						if (shopName.equals(name)) {
							marketType = ShopManager.元宝商城类型;
						}
					}
				}
				Goods gs[] = shop.getGoods(player);
				long[] coins = null;
				if (shop.shopType == ShopManager.商店_绑银商店) {
					coins = new long[2];
					coins[0] = player.getBindSilver();
					coins[1] = player.getSilver();
				} else if (shop.shopType == ShopManager.商店_资源商店) {
					coins = new long[3];
					Cave cave = FaeryManager.getInstance().getCave(player);
					if (cave != null) {
						ResourceCollection rc = cave.getCurrRes();
						if (rc != null) {
							coins[0] = rc.getFood();
							coins[1] = rc.getWood();
							coins[2] = rc.getStone();
						}
					}

				} else if (shop.shopType == ShopManager.商店_工资商店) {
					coins = new long[1];
					coins[0] = player.getWage();
				} else if (shop.shopType == ShopManager.商店_银子商店) {
					coins = new long[1];
					coins[0] = player.getSilver();
				} else if (shop.shopType == ShopManager.商店_功勋商店) {
					coins = new long[1];
					coins[0] = player.getGongxun();
				} else if (shop.shopType == ShopManager.商店_文采商店) {
					coins = new long[1];
					coins[0] = player.getCulture();
				} else {
					coins = new long[0];
				}

				com.fy.engineserver.shop.client.Goods[] cGoods = ShopManager.translate(player, shop, gs);
				SHOP_GET_RES res = new SHOP_GET_RES(GameMessageFactory.nextSequnceNum(), marketType, shopName, shop.getVersion(), shop.shopType, coins, cGoods);
				player.addMessageToRightBag(res);
				if (!shopName.equals(Translate.商店名字_历练兑换)) {
					SHOP_OTHER_INFO_RES shopOtherRes = new SHOP_OTHER_INFO_RES(GameMessageFactory.nextSequnceNum(), marketType, shopName, shop.shopType, cGoods);
					player.addMessageToRightBag(shopOtherRes);
				}
			}
		} else if (message instanceof SHOP_BUY_REQ) {
			// if (player.isLocked()) {
			// HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2778);
			// player.addMessageToRightBag(nreq);
			// return null;
			// }

			try {
				if (RelationShipHelper.checkForbidAndSendMessage(player)) {
					if (RelationShipHelper.logger.isInfoEnabled()) {
						RelationShipHelper.logger.info("[玩家因为打金行为被限制商店购买] [" + player.getLogString() + "]");
					}
					return null;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}


			SHOP_BUY_REQ req = (SHOP_BUY_REQ) message;

			ShopManager shopManager = ShopManager.getInstance();
			String shopName = req.getShopName();
			boolean vipShop = false;
			if (shopName.indexOf("VIP") >= 0) {
				shopName = Translate.VIP商店;
				vipShop = true;
			}
			Shop shop = shopManager.getShop(req.getShopName(), player);
			if (shop == null && vipShop) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您不是vip不能使用vip商店);
				player.addMessageToRightBag(hreq);
				return null;
			}
			if (shop == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.translateString(Translate.商店不存在, new String[][] { { Translate.STRING_1, req.getShopName() } }));
				player.addMessageToRightBag(hreq);

			} else {
				if (GreenServerManager.isBindYinZiServer() && shop.shopType == Shop.SHOP_TYPE_YUANBAO) {
					Option_ShopSilverBuy silver = new Option_ShopSilverBuy(0);
					silver.setShop(shop);
					silver.setGoodsId(req.getGoodsId());
					silver.setAmount(req.getBuynum());
					silver.setText(Translate.使用银子);
					Option_ShopSilverBuy shopsilver = new Option_ShopSilverBuy(1);
					shopsilver.setShop(shop);
					shopsilver.setGoodsId(req.getGoodsId());
					shopsilver.setAmount(req.getBuynum());
					shopsilver.setText(Translate.使用银票);
					MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
					mw.setOptions(new Option[] { silver, shopsilver });
					mw.setTitle(Translate.选择购买使用的货币);
					mw.setDescriptionInUUB(Translate.商城选货币提示 + Translate.银票 + ":" + TradeManager.putMoneyToMyText(player.getShopSilver()) + "\n银子:" + TradeManager.putMoneyToMyText(player.getSilver()));
					QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					player.addMessageToRightBag(res);
				} else {
					Goods g = shop.getGoodsById(req.getGoodsId());
					if (g == null) {
						return null;
					}
					DiscountCard discountCard = DiscountCardAgent.getInstance().getDiscountCard(g.getArticleName(), g.getBundleNum() * req.getBuynum(), shopName, g.getColor());
					boolean hasDiscount = false;
					if (discountCard != null) {
						int hasNum = player.getArticleEntityNum(discountCard.getName());
						if (hasNum > 0) {
							hasDiscount = true;
						}
					}
					if (!hasDiscount) {// 不参与折扣
						shop.buyGoods(player, req.getGoodsId(), req.getBuynum(), 0);
					} else {
						// 参与折扣
						MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
						Option_Cancel cancel = new Option_Cancel();
						cancel.setText(Translate.取消);
						mw.setTitle(Translate.恭喜你享受折扣);
						mw.setDescriptionInUUB(Translate.translateString(Translate.折扣提示, new String[][] { { Translate.STRING_1, discountCard.getName() }, { Translate.STRING_2, BillingCenter.得到带单位的银两(g.getPrice() * req.getBuynum()) }, { Translate.STRING_3, BillingCenter.得到带单位的银两((long) (g.getPrice() * req.getBuynum() * discountCard.getDiscountRate() * TengXunDataManager.instance.getShopAgio2Buy(player))) } }));
						Option_discount option_discount = new Option_discount(shop, g, player, req.getBuynum(), discountCard, 0);
						option_discount.setText(Translate.确定);
						mw.setOptions(new Option[] { option_discount, cancel });
						QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
						player.addMessageToRightBag(res);
						// shop.buyGoods(player, req.getGoodsId(), req.getBuynum(), 0,discountCard.getDiscountRate());
					}
				}
			}
		} else if (message instanceof SHOP_BUYBACK_REQ) {

			SHOP_BUYBACK_REQ req = (SHOP_BUYBACK_REQ) message;

			ShopManager shopManager = ShopManager.getInstance();
			Shop shop = shopManager.getShop(req.getShopName(), player);
			if (shop == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2776 + req.getShopName() + Translate.text_2777);
				player.addMessageToRightBag(hreq);

			} else {
				shop.buyBack(player, req.getArticleEntityId(), req.getAmount());
			}
		} else if (message instanceof SHOP_SELL_REQ) {
			if (player.isLocked()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2779);
				player.addMessageToRightBag(nreq);
				return null;
			}

			SHOP_SELL_REQ req = (SHOP_SELL_REQ) message;

			ShopManager shopManager = ShopManager.getInstance();
			Shop shop = shopManager.getShop(req.getShopName(), player);
			if (shop == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2776 + req.getShopName() + Translate.text_2777);
				player.addMessageToRightBag(hreq);

			} else {
				shop.sellArticleEntity(player, req.getFangbaoFlag(), req.getKnapsackIndex());
			}
		} else if (message instanceof SAVE_SHORTCUT_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_SHORTCUT_REQ) {

			QUERY_SHORTCUT_REQ req = (QUERY_SHORTCUT_REQ) message;

			ShortcutAgent agent = new ShortcutAgent();
			byte[] d = player.getShortcut();
			agent.load(d);
			player.setShortcut(agent.toByteArray());

			Player.sendPlayerAction(player, PlayerActionFlow.行为类型_快捷键, "", 0, new java.util.Date(), GamePlayerManager.isAllowActionStat());
			QUERY_SHORTCUT_RES res = new QUERY_SHORTCUT_RES(req.getSequnceNum(), player.getShortcut());
			return res;
		} else if (message instanceof QUERY_TOPO_DIAGRAM_REQ) { // 拓扑结构
			QUERY_TOPO_DIAGRAM_REQ req = (QUERY_TOPO_DIAGRAM_REQ) message;
			GameInfo gis[] = gm.getGameInfos();
			String mapNames[] = new String[gis.length];
			String displayMapNames[] = new String[gis.length];
			byte neighberNums[] = new byte[gis.length];
			int count = 0;
			for (int i = 0; i < gis.length; i++) {
				mapNames[i] = gis[i].getName();
				displayMapNames[i] = gis[i].displayName;
				int neighberCount=0;
				for(int j=0;j<gis[i].getTransports().length;j++){
					neighberCount+=gis[i].getTransports()[j].getTransMapNum();
				}
				neighberNums[i] = (byte) neighberCount;
				count += neighberNums[i];
			}
			short neighbers[] = new short[count];
			short neighbersType[] = new short[count];
			int index = 0;
			for (int i = 0; i < gis.length; i++) {
				TransportData tds[] = gis[i].getTransports();
				for (int j = 0; j < tds.length; j++) {
					String n = tds[j].getTargetMapName();
					if(tds[j].getTransMapNum()==1){
						for (int l = 0; l < mapNames.length; l++) {
							if (mapNames[l].equals(n)) {
								neighbers[index] = (short) l;
								neighbersType[index] = (short) 0;
								break;
							}
						}
						index++;
					}else if(tds[j].getTransMapNum()==3){
						String targetMap1 = tds[j].getTargetMapName1();
						if(targetMap1!=null&& targetMap1.isEmpty()==false){
							for (int l = 0; l < mapNames.length; l++) {
								if (mapNames[l].equals(targetMap1)) {
									neighbers[index] = (short) l;
									neighbersType[index] = (short) 1;
									break;
								}
							}
							
						}
						index++;
						String targetMap2 = tds[j].getTargetMapName2();
						if(targetMap2!=null&& targetMap2.isEmpty()==false){
							for (int l = 0; l < mapNames.length; l++) {
								if (mapNames[l].equals(targetMap2)) {
									neighbers[index] = (short) l;
									neighbersType[index] = (short) 2;
									break;
								}
							}
							
						}
						index++;
						String targetMap3 = tds[j].getTargetMapName3();
						if(targetMap3!=null&& targetMap3.isEmpty()==false){
							for (int l = 0; l < mapNames.length; l++) {
								if (mapNames[l].equals(targetMap3)) {
									neighbers[index] = (short) l;
									neighbersType[index] = (short) 3;
									break;
								}
							}
						}
						index++;
					}
					
				}
			}
			return new QUERY_TOPO_DIAGRAM_RES(req.getSequnceNum(), mapNames, displayMapNames, neighberNums, neighbers,neighbersType);

		} else if (message instanceof FUNCTION_NPC_REQ) {

			this.pushMessageToGame(player, message, null);

		} else if (message instanceof PLAYER_ENTER_REQ) {

			PLAYER_ENTER_REQ req = (PLAYER_ENTER_REQ) message;

			if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				if (!judgeCanEnterServer()) {
					String tip = "亲爱的仙友,【" + GameConstants.getInstance().getServerName() + "】服务器无法进入,预知开服时间请关注官方网站的开服通知，敬请期待！";

					PLAYER_ENTER_RES res = new PLAYER_ENTER_RES(req.getSequnceNum(), (byte) 12, tip, new Buff[0], 0, 0, new short[0], new Soul[0], 0, 0, PlayerManager.每人每天可以使用绑银);
					logger.warn("[判断用户是否可以进入游戏服] [用户不能进入游戏服]");
					return res;
				}
			}
			long playerId = req.getPlayer();
			Player p = null;
			try {
				p = playerManager.getPlayer(playerId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return handler_playerEnter(req, passport, conn, username, player);

		} else if (message instanceof PLAYER_ACTIVATION_SOUL_REQ) {// 激活元神
			PLAYER_ACTIVATION_SOUL_REQ req = (PLAYER_ACTIVATION_SOUL_REQ) message;
			// if (true) {
			// player.send_HINT_REQ("此功能暂未开启,更多更新,敬请期待");
			// return null;
			// }
			int soulType = req.getSoulType();
			int career = req.getCareer();
			CompoundReturn cr = player.activationSoul(soulType, career);
			String failreason = "";
			PLAYER_ACTIVATION_SOUL_RES res = null;
			if (cr.getBooleanValue()) {
				res = new PLAYER_ACTIVATION_SOUL_RES(req.getSequnceNum(), (byte) 0, failreason);
				PLAYER_SOUL_CHANGE_RES change_RES = new PLAYER_SOUL_CHANGE_RES(GameMessageFactory.nextSequnceNum(), (byte) 1, player.getSoul(soulType));
				player.addMessageToRightBag(change_RES);
				{
					AchievementManager.getInstance().record(player, RecordAction.开启元神);
				}
			} else {
				switch (cr.getIntValue()) {
				case 1:
					failreason = Translate.类型错误;
					break;
				case 2:
					failreason = Translate.元神已经存在不需要重复激活;
					break;
				case 3:
					failreason = Translate.等级不够激活元神;
					break;
				case 4:
					failreason = Translate.只能选择同性别的职业;
					break;
				default:
					break;
				}
				res = new PLAYER_ACTIVATION_SOUL_RES(req.getSequnceNum(), (byte) 1, failreason);
			}
			return res;
		} else if (message instanceof PLAYER_SWITCH_SOUL_REQ) {// 切换元神
			PLAYER_SWITCH_SOUL_REQ req = (PLAYER_SWITCH_SOUL_REQ) message;
			int soulType = req.getSoulType();
			CompoundReturn cr = player.switchSoul(soulType);
			PLAYER_SWITCH_SOUL_RES res = null;
			String failreason = "";
			if (cr.getBooleanValue()) {
				res = new PLAYER_SWITCH_SOUL_RES(req.getSequnceNum(), (byte) 0, failreason, Soul.switchCd);
				{
					// 激活元神
				}
			} else {
				switch (cr.getIntValue()) {
				case 1:
					failreason = Translate.已经是当前元神了;
					break;
				case 2:
					failreason = Translate.切换操作CD中;
					break;
				case 3:
					failreason = Translate.要切换的元神不存在;
					break;
				case 4:
					failreason = Translate.你在骑乘状态不能切换元神;
					break;
				case 5:
					failreason = Translate.你在副本里不能切换元神;
					break;
				case 6:
					failreason = Translate.渡劫中不能切换元神;
					break;
				case 7:
					failreason = Translate.迷城中不能切换元神;
					break;
				case 8:
					failreason = Translate.死亡不能切换元神;
					break;
				case 9:
					failreason = Translate.挑战仙尊不允许切换元神;
					break;
				case 10:
					failreason = Translate.封印不能切换元神;
					break;
				case 11:
					failreason = Translate.仙婴附体中不能操作;
					;
					break;
				default:
					break;
				}
				res = new PLAYER_SWITCH_SOUL_RES(req.getSequnceNum(), (byte) 1, failreason, Soul.switchCd);
			}
			return res;

		} else if (message instanceof BOURN_LEVELUP_REQ) { // 境界升级
			CompoundReturn cr = bournManager.bournlevelUp(player);
			if (BournManager.logger.isWarnEnabled()) {
				BournManager.logger.warn(player.getLogString() + "[境界提升] [" + cr.getBooleanValue() + "] [当前境界等级:" + player.getClassLevel() + "]" + "境界升级返回:" + cr.toString());
			}
			String failreason = "";
			if (cr.getBooleanValue()) {// 升级成功了
				{
					// 统计
					AchievementManager.getInstance().record(player, RecordAction.境界, player.getClassLevel());
				}
				try {
					// 发送电视
					ChatMessage msg = new ChatMessage();

					int tempIndex = player.getClassLevel() / 2;
					if (tempIndex >= ArticleManager.color_article.length) {
						tempIndex = ArticleManager.color_article.length - 1;
					}
					String tempColor = String.valueOf(ArticleManager.color_article[tempIndex]);
					msg.setMessageText(Translate.translateString(Translate.境界升级提示, new String[][] { { Translate.COUNT_1, tempColor }, { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.STRING_2, player.getName() }, { Translate.STRING_3, BOURN_NAMES[player.getClassLevel()] } }));
					ChatMessageService.getInstance().sendMessageToSystem(msg);
					// 通知地图所有人某人境界升级,播粒子
					if (player.getCurrentGame() != null) {
						for (LivingObject lo : player.getCurrentGame().getLivingObjects()) {
							if (lo instanceof Player) {
								Player p = (Player) lo;
								ParticleData[] particleDatas = new ParticleData[1];
								particleDatas[0] = new ParticleData(player.getId(), "境界光效/渡劫雷电", 20000, 0, 0, 1);
								NOTICE_CLIENT_PLAY_PARTICLE_RES client_PLAY_PARTICLE_RES = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
								p.addMessageToRightBag(client_PLAY_PARTICLE_RES);
							}
						}
					}
				} catch (Exception e) {
					AchievementManager.logger.error("[BOURN_LEVELUP_REQ] [发广播异常] [" + player.getLogString() + "]", e);
				}

			} else {
				switch (cr.getIntValue()) {
				case 1:
					failreason = Translate.text_bourn_002;
					break;
				case 2:
					break;
				case 3:
					failreason = Translate.text_bourn_003;
					break;
				case 4:
					failreason = Translate.text_bourn_004;
					break;
				case 5:
					failreason = Translate.text_bourn_005;
					break;
				case 6:
					failreason = Translate.text_bourn_016;
					break;
				case 7:
					failreason = Translate.需要渡劫后才可升境界;
					break;
				}
			}
			if (BournManager.logger.isWarnEnabled()) {
				BournManager.logger.warn("[境界升级提示:" + failreason + "]");
			}
			BOURN_LEVELUP_RES res = new BOURN_LEVELUP_RES(message.getSequnceNum(), cr.getBooleanValue() ? (byte) 0 : (byte) 1, failreason);
			player.addMessageToRightBag(res);

		} else if (message instanceof BOURN_ZEZEN_REQ) { // 打坐
			byte option = ((BOURN_ZEZEN_REQ) message).getOption();
			String failreason = "";
			if (option == 0) { // 开始打坐
				CompoundReturn cr = bournManager.beginZezen(player);
				if (cr.getBooleanValue()) {

				} else {
					switch (cr.getIntValue()) {
					case 1:
						failreason = Translate.text_bourn_006;
						break;
					case 2:
						failreason = Translate.text_bourn_007;
						break;
					}
				}
			} else if (option == 1) {
				bournManager.endZezen(player);
			}
			if (!"".equals(failreason)) {
				player.sendError(failreason);
			}
		} else if (message instanceof BOURN_REFRESH_TASK_REQ) {
			byte option = ((BOURN_REFRESH_TASK_REQ) message).getOption();
			CompoundReturn cr = bournManager.refreshTask(player, option);
			if (cr.getBooleanValue()) {
				if (BournManager.logger.isWarnEnabled()) {
					BournManager.logger.warn(player.getLogString() + "[刷新任务星级{}][任务{}]", new Object[] { cr.getIntValue(), cr.getObjValue() });
				}
				Task task = (Task) cr.getObjValue();
				if (task != null) {
					player.setCurrBournTaskName(task.getName());
					player.setCurrBournTaskStar(cr.getIntValue());
					if (BournManager.logger.isWarnEnabled()) {
						BournManager.logger.warn(player.getLogString() + "[刷新任务星级成功]");
					}
					BOURN_REFRESH_TASK_RES res = new BOURN_REFRESH_TASK_RES(message.getSequnceNum(), player.getCurrBournTaskStar(), task.getId());
					player.addMessageToRightBag(res);
				} else {
					if (BournManager.logger.isWarnEnabled()) {
						BournManager.logger.warn(player.getLogString() + "[刷新任务星级失败]");
					}
					player.sendError(Translate.刷新失败);
				}
			} else {
				String failreason = "";
				switch (cr.getIntValue()) {
				case 1:
					break;
				case 2:
					failreason = Translate.text_bourn_008;
					break;
				case 3:
					failreason = Translate.text_bourn_009;
					break;
				case 4:
				case 5:
					failreason = Translate.text_bourn_010;
					BillingCenter.银子不足时弹出充值确认框(player, Translate.你的银子不足以刷新是否去充值);
					break;
				case 6:
					failreason = Translate.text_bourn_013;
					break;
				case 7:
					failreason = Translate.translateString(Translate.text_bourn_021, new String[][] { { Translate.COUNT_1, String.valueOf(BournManager.SILVER_REFTASK_VIP) } });
					break;
				case 8:
					failreason = Translate.星级已满不能刷新;
					break;
				}
				if (BournManager.logger.isWarnEnabled()) {
					BournManager.logger.warn(player.getLogString() + "[刷新任务星级失败][结果{}]", new Object[] { failreason });
				}
				if (!"".equals(failreason)) {
					player.sendError(failreason);
				}
			}

		} else if (message instanceof BOURN_INFO_REQ) {
			short bournLevel = player.getClassLevel();
			BournCfg cfg = bournManager.getBournCfg(bournLevel);
			if (cfg != null) {
				BOURN_INFO_RES res = new BOURN_INFO_RES(GameMessageFactory.nextSequnceNum(), cfg.getExp(), BournManager.bournDes[bournLevel], BournManager.getInstance().getMaxBournTaskNum(player), player.getLeftZazenTime());
				player.addMessageToRightBag(res);
			} else {
				BournManager.logger.error(player.getLogString() + "[查看境界][境界不存在 等级{}]", new Object[] { bournLevel });
			}
		} else if (message instanceof BOURN_OF_TRAINING_REQ) {
			BournCfg cfg = bournManager.getBournCfg(0);// player.getClassLevel());
			long startTaskId = cfg.getStartTask();
			List<Task> tasks = new ArrayList<Task>();
			List<Byte> _status = new ArrayList<Byte>();// -1 还没接 1 已经接受 2 完成未交付 3 完成
			boolean unAcceptFlag = false;// 任务为连续任务，只要有一个没接取，后面的任务都认为是没接的,减少对后面任务的判断
			Task task = taskManager.getTask(startTaskId);

			while (task != null) {
				tasks.add(task);
				List<Task> nexts = taskManager.getnextTask(task.getName());
				if (nexts != null && nexts.size() > 0) {
					task = nexts.get(0);
				} else {
					task = null;
				}
			}

			NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
			if (ndtm != null && ndtm.isNewDeliverTaskAct) {
				ndtm.batchLoadDeliverTask(player, tasks);
			} else {
				DeliverTaskManager.getInstance().batchLoadDeliverTask(player, tasks);
			}

			for (int i = 0; i < tasks.size(); i++) {
				byte tstatus = -1;
				if (!unAcceptFlag) {
					if (tasks.get(i) != null) {
						tstatus = (byte) player.getTaskStatus(tasks.get(i));
						if (tstatus == -1) {
							unAcceptFlag = true;
							CompoundReturn cr = player.takeOneTask(tasks.get(i), false, null);
							if (cr.getIntValue() == 0) {
								tstatus = (byte) cr.getIntValue();
							}
						}
					}
				}
				_status.add(tstatus);
			}
			byte[] status = new byte[_status.size()];
			for (int i = 0; i < _status.size(); i++) {
				status[i] = _status.get(i);
			}
			BOURN_OF_TRAINING_RES res = new BOURN_OF_TRAINING_RES(GameMessageFactory.nextSequnceNum(), tasks.toArray(new Task[0]), status);
			player.addMessageToRightBag(res);
		} else if (message instanceof BOURN_OF_PURIFY_REQ) {
			if (player.getClassLevel() < BournManager.dailyTaskLevel) {
				return null;
			}
			int star = player.getCurrBournTaskStar();
			Task task = taskManager.getTask(player.getCurrBournTaskName(),player.getCountry());
			boolean hasCollectionTask = player.hasSameCollectionTask(Translate.境界);
			if (hasCollectionTask) {
				return null;
			} else {
				if (task == null || player.getTaskStatus(task) == TaskConfig.TASK_STATUS_DEILVER) {
					List<Integer> resultIndexs = RandomTool.getResultIndexs(RandomType.groupRandom, BournManager.defaultRate, 1);
					star = resultIndexs.get(0);
					task = BournManager.starTaskMap.get(player.getClassLevel())[star][new Random().nextInt(BournManager.starTaskMap.get(player.getClassLevel())[star].length)];
					player.setCurrBournTaskStar(star);
					player.setCurrBournTaskName(task.getName());
					if (BournManager.logger.isInfoEnabled()) {
						BournManager.logger.info(player.getLogString() + "[当前无境界日常任务,自动刷新] [星级{}][任务{}]", new Object[] { star, task });
					}
				}
			}
			long taskId = 0l;
			if (task != null) {
				taskId = task.getId();

			}
			long refreshExpCost = BournManager.getInstance().getBournCfg(player.getClassLevel()).getRefreshTaskExpCost();
			long refreshCoinCost = BournManager.coinCostOfRefresh;

			String des[] = new String[BournManager.starTaskMap.get(player.getClassLevel()).length];
			for (int i = 0; i < BournManager.starTaskMap.get(player.getClassLevel()).length; i++) {
				Task tt = BournManager.starTaskMap.get(player.getClassLevel())[i][0];
				if (tt != null) {
					List<TaskPrize> prizes = tt.getPrizeByType(PrizeType.CLASSLEVEL);
					if (prizes != null && prizes.size() > 0) {
						des[i] = prizes.get(0).getPrizeNum()[0] + Translate.text_bourn_014;
					} else {
						des[i] = Translate.奖励没录入;
					}
				} else {
					des[i] = Translate.任务没录入;
				}
			}
			logger.warn(player.getLogString() + "[境界:" + player.getClassLevel() + "] [" + BournManager.getInstance().getBournCfg(player.getClassLevel()) + "]");
			BOURN_OF_PURIFY_RES res = new BOURN_OF_PURIFY_RES(GameMessageFactory.nextSequnceNum(), taskId, BournManager.purifyDes, player.getCurrBournTaskStar(), refreshExpCost, refreshCoinCost, des);
			player.addMessageToRightBag(res);
		} else if (message instanceof CLIFFORD_START_REQ) {
			this.pushMessageToGame(player, message, null);
		} else if (message instanceof CLIFFORD_REQ) {
			this.pushMessageToGame(player, message, null);
		} else if (message instanceof CLIFFORD_LOOKOVER_REQ) {
			this.pushMessageToGame(player, message, null);
		} else if (message instanceof WANNA_COLLECTION_REQ) {// 想要采集物品
			WANNA_COLLECTION_REQ req = (WANNA_COLLECTION_REQ) message;
			long npcId = req.getNpcId();
			NPC npc = npcManager.getNPC(npcId);
			if (npc != null) {
				try {
					if (npc instanceof TaskCollectionNPC) {
						TaskCollectionNPC tcNPC = (TaskCollectionNPC) npc;
						if (TaskSubSystem.logger.isDebugEnabled()) {
							TaskSubSystem.logger.debug(player.getLogString() + "[想要采集物品][NPC类型:{}][名字:{}][适合任务:{}][读条时间:{}]", new Object[] { npc.getClass(), npc.getName(), Arrays.toString(tcNPC.getTaskNames().toArray(new String[0])), ((TaskCollectionNPC) npc).getCollectionBarTime() });
						}
						if (tcNPC.canCollection(player)) {
							// 可以采集 1启动定时器 2通知客户端可以采集 并返回读条时间
							player.getTimerTaskAgent().createTimerTask(new TaskCollectionProgressBar(player, tcNPC), tcNPC.getCollectionBarTime(), com.fy.engineserver.sprite.TimerTask.type_采集);
							WANNA_COLLECTION_RES res = new WANNA_COLLECTION_RES(message.getSequnceNum(), true, tcNPC.getCollectionBarTime(), npcId, Translate.text_task_045);
							return res;
						} else {
							if (TaskSubSystem.logger.isDebugEnabled()) {
								logger.debug(player.getLogString() + "[想要采集物品][NPC类型:{}][名字:{}][不可采集]", new Object[] { npc.getClass(), npc.getName() });
							}
						}
					} else if (npc instanceof Collectionable) {
//						if (((Collectionable) npc).canCollection(player)) {
//							long time = ((Collectionable) npc).getCollectionBarTime() == 0 ? 2000L : ((Collectionable) npc).getCollectionBarTime();
//							CollectionWormTimeBar bar = new CollectionWormTimeBar(player, (Collectionable) npc);
//							player.getTimerTaskAgent().createTimerTask(bar, ((Collectionable) npc).getCollectionBarTime(), TimerTask.type_采集);
//							WANNA_COLLECTION_RES res = new WANNA_COLLECTION_RES(message.getSequnceNum(), true, time, npcId, Translate.text_task_045);
//							return res;
//						}
					} else if (npc instanceof FlowerNpc) {
					} else if (npc instanceof FairylandTreasureNpc) {
						try {
							FairylandTreasureEntity fte = FairylandTreasureManager.getInstance().fairylandTreasureEntity;
							if (fte != null && fte.isEffect()) {
								Game g = player.getCurrentGame();
								if (g != null) {
									LivingObject[] los = g.getLivingObjects();
									for (LivingObject lo : los) {
										if (lo.getId() == npcId) {
											if (lo instanceof FairylandTreasureNpc) {

												if ((player.getX() - lo.getX()) * (player.getX() - lo.getX()) + (player.getY() - lo.getY()) * (player.getY() - lo.getY()) > FairylandTreasureManager.开宝箱距离) {
													player.sendError(Translate.距离太远);
													return null;
												}

												FairylandTreasureNpc fn = (FairylandTreasureNpc) lo;
												fn.pick(player);
											}
											break;
										}
									}

								}
							} else {
								player.sendError("活动已失效,不能开箱子");
							}
							return null;
						} catch (Exception e) {
							FairylandTreasureManager.logger.error("[仙界宝箱异常] [" + player.getLogString() + "]", e);
						}
					}
				} catch (Exception e) {
					TaskSubSystem.logger.error(player.getLogString() + "[采集异常]", e);
				}
			} else {
				if (logger.isWarnEnabled()) logger.warn(player.getLogString() + "[采集的时候NPC不存在] [NPCID:{}]", new Object[] { npcId });
			}
			return new WANNA_COLLECTION_RES(message.getSequnceNum(), false, 0, 0, "");
		} else if (message instanceof CHANGE_SERVER_REQ) {
			// CHANGE_SERVER_REQ req = (CHANGE_SERVER_REQ) message;
			// if (username != null) {
			// gatewayAddressManager.notifyChangeServer(username, remoteAddr.substring(1, remoteAddr.indexOf(":")), req.getSequnceNum());
			// if (logger.isInfoEnabled()) {
			// // logger.info("[玩家更改服务器] [转发给网关] [" + username + "]");
			// logger.info("[玩家更改服务器] [转发给网关] [{}]", new Object[] { username });
			// }
			// } else {
			// logger.warn("[玩家更改服务器] [转发给网关] [玩家到服务器的连接还没建立]");
			// }
			// return null;
		} else if (message instanceof PLAYER_REVIVED_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof PLAYER_REVIVED_INFO_REQ) {
			long time = (System.currentTimeMillis() - player.getLastRevivedTime());
			long needTime = Player.复活cd时间 - time;
			if (needTime > 0) {
				needTime = needTime + 1000;
			} else {
				needTime = 0;
			}
			PLAYER_REVIVED_INFO_RES res = new PLAYER_REVIVED_INFO_RES(message.getSequnceNum(), needTime, player.reviCostYinzi(player.revivedCount), Player.复活基本银子, (player.revivedCount + 1));
			return res;
		} else if (message instanceof SOCIAL_RELATION_MANAGE_REQ) {
			if (player.isLocked()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2786);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.isInCrossServer()) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2787);
				player.addMessageToRightBag(hint);
				return null;
			}
			return doWithSOCIAL_RELATION_MANAGE_REQ((SOCIAL_RELATION_MANAGE_REQ) message, player);
		} else if (message instanceof NPC_REPAIR_QUERY_REQ) {

		} else if (message instanceof NPC_REPAIR_REPAIR_REQ) {
			this.pushMessageToGame(player, message, null);
		} else if (message instanceof REPAIR_CARRY_REQ) {
			this.pushMessageToGame(player, message, null);
		} else if (message instanceof QIECUO_INVITE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QIECUO_BE_INVITED_RESPONSE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof GET_PLAYER_YUANBAO_REQ) {
			logger.info(player.getLogString() + "[客户端非法调用 ] [GET_PLAYER_YUANBAO_REQ]");
			// GET_PLAYER_YUANBAO_REQ req = (GET_PLAYER_YUANBAO_REQ) message;
			// long yuanbao = bossClientService.getUserYuanbao(player.getUsername());
			// GET_PLAYER_YUANBAO_RES res = new GET_PLAYER_YUANBAO_RES(req.getSequnceNum(), player.getRmbyuanbao(), yuanbao);
			// if (logger.isInfoEnabled()) {
			// // logger.info("[获取玩家RMB元宝] [用户:" + player.getUsername() + "] [角色:" + player.getName() + "] [角色元宝:" + player.getRmbyuanbao() + "] [用户元宝:" + yuanbao + "]");
			// if (logger.isInfoEnabled()) logger.info("[获取玩家RMB元宝] [用户:{}] [角色:{}] [角色元宝:{}] [用户元宝:{}]", new Object[] { player.getUsername(), player.getName(),
			// player.getRmbyuanbao(), yuanbao });
			// }
			// return res;
		} else if (message instanceof QUERY_WINDOW_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof OPTION_SELECT_REQ) {
			// if (CoreSubSystem.beCareful) {
			// //这个代码写的非常特殊，以后有机会尽量修改过来
			// //因为在处理这个按钮的时候player身上没有当前所在地图currentGame对象，然后这个协议又是放到game中处理的
			// //所以为了能处理临时在这加入了处理和检查代码，
			// //最后这个地方也没有调用Option_EnterServer的doSelect是因为
			// //handler_playerEnter里面有部分协议需要在方法返回的协议之后发送的，所以这个地方直接return那个方法调用了。
			// try {
			// OPTION_SELECT_REQ req = (OPTION_SELECT_REQ) message;
			// MenuWindow mw = WindowManager.getInstance().getWindowById(req.getWId());
			// if (mw != null) {
			// // System.out.println("收到协议OPTION_SELECT_REQ");
			// if (req.getIndex() >= 0 && req.getIndex() < mw.getOptions().length) {
			// // System.out.println("文化部    ["+req.getIndex()+"]  ["+mw.getOptions()[req.getIndex()].getClass()+"]");
			// if (mw.getOptions()[req.getIndex()] instanceof Option_EnterServer) {
			// Option_EnterServer opt = (Option_EnterServer)mw.getOptions()[req.getIndex()];
			// logger.warn("[用户同意PK] [{}] [{}] [{}]", new Object[]{opt.getPlayer().getId(), opt.getPlayer().getName(), opt.getPlayer().getLevel()});
			// return handler_playerEnter(opt.getReq(), opt.getPassport(), opt.getConn(), opt.getUsername(), opt.getPlayer());
			// }else if (mw.getOptions()[req.getIndex()] instanceof Option_UnEnterServer) {
			// Option_UnEnterServer opt = (Option_UnEnterServer)mw.getOptions()[req.getIndex()];
			// WindowManager.getInstance().doSelect(null, player, req);
			// logger.warn("[用户点击不同意PK] [{}] [{}] [{}]", new Object[]{opt.getPlayer().getId(), opt.getPlayer().getName(), opt.getPlayer().getLevel()});
			// return null;
			// }
			// }
			// }
			// } catch(Exception e) {
			// logger.error("处理出错:", e);
			// }
			// }
			pushMessageToGame(player, message, null);
		} else if (message instanceof OPTION_INPUT_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof CARD_SAVING_REQ) {

		} else if (message instanceof SAVING_HISTORY_REQ) {
			SAVING_HISTORY_REQ req = (SAVING_HISTORY_REQ) message;
			// TODO 充值记录
			// int pnum = req.getPagenum();
			// int pageindex = req.getPageindex();
			// SavingResult result = bossClientService.getSavingResults(username, pnum, pageindex);
			// if (result != null) {
			//
			// SAVING_HISTORY_RES res = new SAVING_HISTORY_RES(req.getSequnceNum(), result.getSavingRecord(), result.getTotalnum(), result.getPagecount());
			// if (logger.isInfoEnabled()) {
			// // logger.info("[获得充值记录] [成功] [用户:" + username + "] [pnum:" + pnum + "] [pageindex;" + pageindex + "] [返回数量:" + result.getSavingRecord().length + "]");
			// if (logger.isInfoEnabled()) logger.info("[获得充值记录] [成功] [用户:{}] [pnum:{}] [pageindex;{}] [返回数量:{}]", new Object[] { username, pnum, pageindex,
			// result.getSavingRecord().length });
			// }
			// return res;
			// } else {
			// if (logger.isInfoEnabled()) {
			// // logger.info("[获得充值记录] [失败] [用户:" + username + "] [pnum:" + pnum + "] [pageindex;" + pageindex + "]");
			// if (logger.isInfoEnabled()) logger.info("[获得充值记录] [失败] [用户:{}] [pnum:{}] [pageindex;{}]", new Object[] { username, pnum, pageindex });
			// }
			// SAVING_HISTORY_RES res = new SAVING_HISTORY_RES(req.getSequnceNum(), new String[0], 0, 0);
			// return res;
			// }
		} else if (message instanceof EXCHANGE_QUERY_MARKET_REQ) {

		} else if (message instanceof EXCHANGE_PLACE_ORDER_REQ) {

		} else if (message instanceof EXCHANGE_TAKE_MONEY_REQ) {

		} else if (message instanceof EXCHANGE_QUERY_ORDER_REQ) {

		} else if (message instanceof EXCHANGE_CANCEL_ORDER_REQ) {

		} else if (message instanceof HEARTBEAT_CHECK_REQ) {
			HEARTBEAT_CHECK_RES res = new HEARTBEAT_CHECK_RES(message.getSequnceNum(), SystemTime.currentTimeMillis(), (short) 0);
			if (player != null) {
				player.notifyClientHeartBeat((HEARTBEAT_CHECK_REQ) message);
			}
			return res;
		} else if (message instanceof GET_BILLBOARD_MENU_REQ) {
			GET_BILLBOARD_MENU_REQ req = (GET_BILLBOARD_MENU_REQ) message;
			return this.getBillBoardsMenu(req);
		} else if (message instanceof GET_BILLBOARD_REQ) {
			GET_BILLBOARD_REQ req = (GET_BILLBOARD_REQ) message;
			return this.getBillboard(req, player);
		} else if (message instanceof USER_LEAVE_SERVER_REQ) {
			if (player != null && player.getCurrentGame() != null) {
				logger.warn("[收到玩家下线] [player:"+(player==null?"null":player.getLogString())+"] [boothstate:"+(player==null?"null":player.isBoothState())+"] [connState:"+(player.getConn()!=null?player.getConn().getStateString(player.getConn().getState()):"null")+"]");
				if(BoothsaleManager.getInstance().isBoothOffLine(player)){
					return null;
				}
				this.pushMessageToGame(player, message, null);
				Player.sendPlayerAction(player, PlayerActionFlow.行为类型_玩家下线, Translate.text_45, 0, new java.util.Date(), GamePlayerManager.isAllowActionStat());
				logger.warn("[收到玩家下线0] [player:"+(player==null?"null":player.getLogString())+"] [boothstate:"+(player==null?"null":player.isBoothState())+"] [connState:"+(player.getConn()!=null?player.getConn().getStateString(player.getConn().getState()):"null")+"]");
			} else if (player != null) {
				player.leaveServer();
				logger.warn("[收到玩家下线1] [player:"+(player==null?"null":player.getLogString())+"] [boothstate:"+(player==null?"null":player.isBoothState())+"] [connState:"+(player.getConn()!=null?player.getConn().getStateString(player.getConn().getState()):"null")+"]");
				if(!BoothsaleManager.getInstance().isBoothOffLine(player)){
					conn.close();
				}
				Player.sendPlayerAction(player, PlayerActionFlow.行为类型_玩家下线, Translate.text_45, 0, new java.util.Date(), GamePlayerManager.isAllowActionStat());
			} else {
				logger.warn("[收到玩家下线2] [player:"+(player==null?"null":player.getLogString())+"] [boothstate:"+(player==null?"null":player.isBoothState())+"]");// [connState:"+(player.getConn()!=null?player.getConn().getStateString(player.getConn().getState()):"null")+"]");
				if(player != null){
					if(!BoothsaleManager.getInstance().isBoothOffLine(player)){
						conn.close();
					}
				}
			}
		} else if (message instanceof EQUIPMENT_JOIN_BILLBOARD_REQ) {
			if (player.isInCrossServer()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2881);
				player.addMessageToRightBag(nreq);
				return null;
			}
			Logger log = BillboardManager.log;
			EQUIPMENT_JOIN_BILLBOARD_REQ req = (EQUIPMENT_JOIN_BILLBOARD_REQ) message;
			EquipmentBillboards eb = (EquipmentBillboards) this.billboardManager.getBillboards(Translate.text_2311);
			if (eb != null) {
				ArticleEntity ae = player.getArticleEntity(req.getEquipmentId());
				String s = "";
				if (eb.update(player, ae)) {
					s = Translate.text_2882;
				} else {
					s = Translate.text_2883;
				}
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, s);
				player.addMessageToRightBag(hint);
			} else {
				// log.warn("[更新装备排行榜] [失败] [装备榜不存在] [玩家：" + player.getName() + "]");
				if (log.isWarnEnabled()) log.warn("[更新装备排行榜] [失败] [装备榜不存在] [玩家：{}]", new Object[] { player.getName() });
			}
		} else if (message instanceof GET_CARD_CHARGE_INFO_REQ) {
			if (player.isInCrossServer()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2884);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (ChargeInfo.log.isInfoEnabled()) {
				// ChargeInfo.log.info("[申请充值卡充值信息] [玩家：" + player.getName() + "]");
				if (ChargeInfo.log.isInfoEnabled()) ChargeInfo.log.info("[申请充值卡充值信息] [玩家：{}]", new Object[] { player.getName() });
			}
			GET_CARD_CHARGE_INFO_REQ req = (GET_CARD_CHARGE_INFO_REQ) message;
			return new GET_CARD_CHARGE_INFO_RES(req.getSequnceNum(), this.chargeInfo.getCardTypeNames(), this.chargeInfo.getCardTypeIds(), this.chargeInfo.getCardTypeInfos(), this.chargeInfo.getCardFacevalues(), this.chargeInfo.getExchange());
		} else if (message instanceof GET_SMS_CHARGE_INFO_REQ) {
			if (player.isInCrossServer()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2884);
				player.addMessageToRightBag(nreq);
				return null;
			}
			if (player.getLevel() >= 15) {
				if (ChargeInfo.log.isInfoEnabled()) {
					// ChargeInfo.log.info("[申请短信充值信息] [玩家：" + player.getName() + "]");
					if (ChargeInfo.log.isInfoEnabled()) ChargeInfo.log.info("[申请短信充值信息] [玩家：{}]", new Object[] { player.getName() });
				}
				GET_SMS_CHARGE_INFO_REQ req = (GET_SMS_CHARGE_INFO_REQ) message;
				long[] playerChargeData = this.chargeInfo.getPlayerChargeData(player);
				return new GET_SMS_CHARGE_INFO_RES(req.getSequnceNum(), this.chargeInfo.getSmsGateWay(), this.chargeInfo.getSmsInfos(), this.chargeInfo.getSmsExchangeRate(), this.chargeInfo.getSmsFacevalues(), this.chargeInfo.getMsg(), this.chargeInfo.getSmsNum(), this.chargeInfo.getUnitPrice(), (int) playerChargeData[0], (int) playerChargeData[1], (int) playerChargeData[2], this.chargeInfo.getBindingMsg(), this.chargeInfo.getBindingSmsNum());
			} else {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2885);
				player.addMessageToRightBag(hint);
			}
		} else if (message instanceof SMS_CHARGE_REQ) {
			if (player.isInCrossServer()) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2884);
				player.addMessageToRightBag(nreq);
				return null;
			}
			SMS_CHARGE_REQ req = (SMS_CHARGE_REQ) message;
			int unitPrice = req.getUnitPrice();
			int gateway = req.getGateway();
			String msg = req.getMsg();
			String smsNum = req.getSmsNum();
			String userAccount = req.getUserAccount();
			int smsCounts = req.getSmsCounts();
			int vastype = -1;
			if (unitPrice == 100) {
				vastype = 0;
			} else if (unitPrice == 200) {
				vastype = 1;
			}
			if (gateway < 0 || gateway >= this.chargeInfo.GATE_WAY_NAME.length) {
				// ChargeInfo.log.warn("[短信充值] [失败] [不支持的途径] [金额：" + unitPrice + "] [玩家：" + player.getName() + "] [用户:" + player.getUsername() + "] [GATEWAY:" + gateway + "] [msg:"
				// + msg + "] [smsNum:" + smsNum + "] [userAccount:" + userAccount + "]");
				if (ChargeInfo.log.isWarnEnabled()) ChargeInfo.log.warn("[短信充值] [失败] [不支持的途径] [金额：{}] [玩家：{}] [用户:{}] [GATEWAY:{}] [msg:{}] [smsNum:{}] [userAccount:{}]", new Object[] { unitPrice, player.getName(), player.getUsername(), gateway, msg, smsNum, userAccount });
			}

			if (vastype < 0) {
				// ChargeInfo.log.warn("[短信充值] [失败] [金额错误] [金额：" + unitPrice + "] [玩家：" + player.getName() + "] [用户:" + player.getUsername() + "] [GATEWAY:" +
				// this.chargeInfo.GATE_WAY_NAME[gateway] + "] [msg:" + msg + "] [smsNum:" + smsNum + "] [userAccount:" + userAccount + "]");
				if (ChargeInfo.log.isWarnEnabled()) ChargeInfo.log.warn("[短信充值] [失败] [金额错误] [金额：{}] [玩家：{}] [用户:{}] [GATEWAY:{}] [msg:{}] [smsNum:{}] [userAccount:{}]", new Object[] { unitPrice, player.getName(), player.getUsername(), this.chargeInfo.GATE_WAY_NAME[gateway], msg, smsNum, userAccount });
				return null;
			}
			this.chargeInfo.smsCharge(player, vastype, unitPrice, smsCounts);
			if (ChargeInfo.log.isInfoEnabled()) {
				// ChargeInfo.log.info("[短信充值] [成功] [金额：" + unitPrice + "] [玩家：" + player.getName() + "] [用户:" + player.getUsername() + "] [GATEWAY:" +
				// this.chargeInfo.GATE_WAY_NAME[gateway] + "] [msg:" + msg + "] [smsNum:" + smsNum + "] [userAccount:" + userAccount + "]");
				if (ChargeInfo.log.isInfoEnabled()) ChargeInfo.log.info("[短信充值] [成功] [金额：{}] [玩家：{}] [用户:{}] [GATEWAY:{}] [msg:{}] [smsNum:{}] [userAccount:{}]", new Object[] { unitPrice, player.getName(), player.getUsername(), this.chargeInfo.GATE_WAY_NAME[gateway], msg, smsNum, userAccount });
			}
			Player.sendPlayerAction(player, PlayerActionFlow.行为类型_充值, Translate.text_1495, 0, new java.util.Date(), true);
		} else if (message instanceof QUERY_ACTIVITIES_INFO_REQ) {
			QUERY_ACTIVITIES_INFO_RES res = this.getActivitiesInfoRes((QUERY_ACTIVITIES_INFO_REQ) message, player);
			return res;
		} else if (message instanceof QUERY_ACTIVITIES_DETAIL_REQ) {
			QUERY_ACTIVITIES_DETAIL_REQ req = (QUERY_ACTIVITIES_DETAIL_REQ) message;
			long id = req.getActivityId();
			ActivityItem ai = ActivityItemManager.getInstance().getById(id);
			if (ai != null) {
				QUERY_ACTIVITIES_DETAIL_RES res = new QUERY_ACTIVITIES_DETAIL_RES(req.getSequnceNum(), id, ai.getDetail());
				if (ActivityItemManager.getInstance().log.isInfoEnabled()) {
					// ActivityItemManager.getInstance().log.info("[请求运营活动细节] [成功] [ID:" + id + "] [活动：" + ai.getTitle() + "] [玩家：" + player.getName() + "]");
					if (ActivityItemManager.getInstance().log.isInfoEnabled()) ActivityItemManager.getInstance().log.info("[请求运营活动细节] [成功] [ID:{}] [活动：{}] [玩家：{}]", new Object[] { id, ai.getTitle(), player.getName() });
				}
				return res;
			} else {
				// ActivityItemManager.getInstance().log.warn("[请求运营活动细节] [失败] [没有这个活动] [ID:" + id + "]");
				if (ActivityItemManager.getInstance().log.isWarnEnabled()) ActivityItemManager.getInstance().log.warn("[请求运营活动细节] [失败] [没有这个活动] [ID:{}]", new Object[] { id });
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2887);
				player.addMessageToRightBag(hint);
				return null;
			}
		} else if (message instanceof TASK_SEND_ACTION_REQ) {
			// this.pushMessageToGame(player, message, null);
		} else if (message instanceof GET_PLAYER_BY_ID_REQ) {
			this.pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_NETWORKFLOW_REQ) {
			QUERY_NETWORKFLOW_REQ req = (QUERY_NETWORKFLOW_REQ) message;
			// dfa
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(60);
			mw.setTitle(Translate.text_2888);
			mw.setWidth(240);
			mw.setHeight(320);

			StringBuffer sb = new StringBuffer();

			GameNetworkFramework.FLOWSTAT f;
			f = (GameNetworkFramework.FLOWSTAT) conn.getAttachmentData(GameNetworkFramework.FLOWSTAT);
			String delay = "" + conn.getAttachmentData("network.delay");
			StatData sdata = player.getStatData(StatData.STAT_ONLINE_TIME);
			String alltime = Translate.text_2889;
			if (sdata != null) {
				alltime = "" + (sdata.getValue() / 3600000) + Translate.text_146;
			}
			long onlineTime = f.lastSendTime + 1 - f.startTime;
			if (onlineTime < 600000) onlineTime = 600000;
			long onlineTime1 = f.lastReceiveTime + 1 - f.startTime;
			if (onlineTime1 < 600000) onlineTime1 = 600000;

			float s = f.sendBytes * 3600.0f * 1000 / onlineTime / 1024 / 1024;
			float r = f.receiveBytes * 3600.0f * 1000 / onlineTime1 / 1024 / 1024;

			sb.append("[color=" + 0x00ff00 + Translate.text_2890 + ((com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - f.startTime) / 60000) + Translate.text_2891);
			sb.append("[color=" + 0x00ff00 + Translate.text_2892 + String.format("%,.2f", s) + Translate.text_2893);
			sb.append("[color=" + 0x00ff00 + Translate.text_2894 + String.format("%,.2f", r) + Translate.text_2893);
			sb.append("[color=" + 0x00ff00 + Translate.text_2895 + String.format("%,d", f.sendBytes) + Translate.text_2896);
			sb.append("[color=" + 0x00ff00 + Translate.text_2897 + String.format("%,d", f.receiveBytes) + Translate.text_2896);

			sb.append("[color=" + 0x00ff00 + Translate.text_2898 + alltime + "\n");
			sb.append("[color=" + 0x00ff00 + Translate.text_2899 + delay + Translate.text_2900);

			mw.setDescriptionInUUB(sb.toString());
			mw.setShowTask(false);
			Option cancelOption = new Option_Cancel();
			cancelOption.setIconId("172");
			cancelOption.setText(Translate.text_2901);
			mw.setOptions(new Option[] { cancelOption });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(req.getSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
		} else if (message instanceof SET_QUEUE_READYNUM_REQ) {

			if (player.getCurrentGame() != null) {
				this.pushMessageToGame(player, message, null);
			} else {
				SET_QUEUE_READYNUM_REQ req = (SET_QUEUE_READYNUM_REQ) message;
				if (req.getSelectedQueueReadyNum() >= 4) {
					player.setAroundNotifyPlayerNum(0);
					player.send_HINT_REQ(Translate.text_2902);
				} else {
					player.setAroundNotifyPlayerNum(255);
				}
			}

		} else if (message instanceof QUERY_CMCC_AUTHORIZED_REQ || message instanceof QUERY_CMCC_USER_LOGIN_REQ || message instanceof QUERY_CMCC_ChargeUp_REQ || message instanceof QUERY_CMCC_ChargeUpRecord_REQ || message instanceof QUERY_CMCC_Balance_REQ || message instanceof QUERY_CMCC_KEEPLOGIN_REQ) {
			// 移动网游业务平台
			// return Cmcc.handleReqeustMessage(player, conn, message, bossClientService);
		} else if (message instanceof QUERY_HONOR_REQ) {

		} else if (message instanceof QUERY_HONOR_INFO_REQ) {

			QUERY_HONOR_INFO_REQ req = (QUERY_HONOR_INFO_REQ) message;
			String uub = HonorManager.getInstance().getHonorUUB(player, req.getHonorId());
			QUERY_HONOR_INFO_RES res = new QUERY_HONOR_INFO_RES(req.getSequnceNum(), req.getHonorId(), uub);
			player.addMessageToRightBag(res);
		} else if (message instanceof HONOR_OPRATION_REQ) {
			if (player.isInBattleField() && player.getDuelFieldState() == 2) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2903);
				player.addMessageToRightBag(nreq);
				return null;
			}

			HONOR_OPRATION_REQ req = (HONOR_OPRATION_REQ) message;
			int honorId = req.getHonorId();
			byte operation = req.getOperation();
			HonorManager.getInstance().doOperation(player, honorId, operation);
		} else if (message instanceof MASTERS_AND_PRENTICES_OPERATION_REQ) {

		} else if (message instanceof QUERY_MASTERS_AND_PRENTICES_REQ) {

		} else if (message instanceof BATTLEFIELD_ACTION_REQ) {
			// 单服战场模式
			// if(player.isInCrossServer()){
			// DefaultCrossServerAgent.getInstance().handleRequestMessageFromClient(player, message);
			// logger.info("[转发跨服消息] [BATTLEFIELD_ACTION_REQ] [playerName："+player.getName()+"] [playerId："+player.getId()+"] [username:"+player.getUsername()+"]");
			// return null;
			// }
			// BattleFieldLineupService bfls = BattleFieldLineupService.getInstance();
			// bfls.pushMessageToQueue(new PlayerMessagePair(player,message,null));

			if (player.isInBattleField() && player.getDuelFieldState() > 0) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2908);
				player.addMessageToRightBag(req);
				return null;
			}

			BattleFieldLineupService bfls = BattleFieldLineupService.getInstance();
			bfls.pushMessageToQueue(new PlayerMessagePair(player, message, null));

		} else if (message instanceof QUERY_BATTLEFIELDLIST_REQ) {
			// 单服战场模式
			// GameServerManager gsm=GameServerManager.getInstance();
			// if(gsm.isCrossServer(GameConstants.getInstance().getServerName())){
			// HINT_REQ hint=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,"您现在身处跨服服务器，不能进行战场报名！");
			// player.addMessageToRightBag(hint);
			// return null;
			// }
			// BattleFieldLineupService bfls = BattleFieldLineupService.getInstance();
			// BattleItem items[] = bfls.getAvailableBattleItems(player);

			// 跨服战场模式

		}

		else if (message instanceof QUERY_BATTLEFIELD_INFO_REQ) {
			if (player.isInBattleField() && player.getDuelFieldState() > 0) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2908);
				player.addMessageToRightBag(req);
				return null;
			}

			BattleFieldLineupService bfls = BattleFieldLineupService.getInstance();
			if (player.getBattleField() != null) {
				BattleFieldStatData datas[] = bfls.getBattleFieldStatDataByPlayer(player);
				BattleField bf = player.getBattleField();
				if (bf.getState() == BattleField.STATE_OPEN || bf.getState() == BattleField.STATE_FIGHTING) {
					return new QUERY_BATTLEFIELD_INFO_RES(message.getSequnceNum(), bf.getName(), false, "", datas);
				} else {
					if (bf.getBattleFightingType() == BattleField.BATTLE_FIGHTING_TYPE_对战) {
						if (bf.getWinnerSide() == BattleField.BATTLE_SIDE_A) {
							return new QUERY_BATTLEFIELD_INFO_RES(message.getSequnceNum(), bf.getName(), false, bf.getBattleFieldSideNames()[BattleField.BATTLE_SIDE_A] + Translate.text_1778, datas);
						} else if (bf.getWinnerSide() == BattleField.BATTLE_SIDE_B) {
							return new QUERY_BATTLEFIELD_INFO_RES(message.getSequnceNum(), bf.getName(), false, bf.getBattleFieldSideNames()[BattleField.BATTLE_SIDE_B] + Translate.text_1778, datas);
						} else {
							return new QUERY_BATTLEFIELD_INFO_RES(message.getSequnceNum(), bf.getName(), false, Translate.text_1780, datas);
						}
					} else {
						return new QUERY_BATTLEFIELD_INFO_RES(message.getSequnceNum(), bf.getName(), false, "", datas);
					}
				}
			} else {
				// return new QUERY_BATTLEFIELD_INFO_RES(message.getSequnceNum(),"无战场","",new BattleFieldStatData[0]);
				HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2911);
				player.addMessageToRightBag(req2);
			}

		} else if (message instanceof PLAYER_ENTER_DUELBATTLE_MONI_REQ) {

		} else if (message instanceof QUERY_ALL_ACHIEVEMENT_SERIES_REQ) {
			// FIXME 测试成就
			// try{
			// QUERY_ALL_ACHIEVEMENT_SERIES_REQ req = (QUERY_ALL_ACHIEVEMENT_SERIES_REQ)message;
			// int achievementSeriesIds[] = new int[]{ 0,1,2,3,4,5,6 };
			// String[] achievementSeriesNames= new String[] {"任务成就","探索成就","战场成就","副本成就","声望成就","世界事件","光辉事迹"};
			// int[] colors = new int[]{0xffffff,0xffffff,0xffffff,0xffffff,0xffff00,0xffffff,0xffffff};
			// int currentPage = req.getCurrentPage();
			// int linePage = req.getLineCountInPage();
			// int allPage = 100;
			// QUERY_ALL_ACHIEVEMENT_SERIES_RES res = new QUERY_ALL_ACHIEVEMENT_SERIES_RES(req.getSequnceNum(),
			// achievementSeriesIds,achievementSeriesNames,
			// colors,currentPage,allPage);
			// return res;
			// }catch(Throwable e){
			// e.printStackTrace();
			// return null;
			// }
		} else if (message instanceof QUERY_ACHIEVEMENT_SERIES_REQ) {
			// FIXME 测试成就
			// try{
			// QUERY_ACHIEVEMENT_SERIES_REQ req = (QUERY_ACHIEVEMENT_SERIES_REQ)message;
			//
			// String[] achievementSeriesNames= new String[] {"任务成就","探索成就","战场成就","副本成就","声望成就","世界事件","光辉事迹"};
			// int[] colorssss = new int[]{0xffffff,0xffffff,0xffffff,0xffffff,0xffff00,0xffffff,0xffffff};
			// int num = achievementSeriesNames.length;
			// int[] achievementIds = new int[num];
			// String[] achievementNames = new String[num];
			// int[] colors = new int[num];
			// int currentPage = req.getCurrentPage();
			// int linePage = req.getLineCountInPage();
			// int allPage = 100;
			//
			// for( int i=0;i< achievementIds.length;i++){
			// achievementIds[i] = i+req.getAchievementSeriesId()*100;
			// achievementNames[i] = achievementSeriesNames[req.getAchievementSeriesId()]+"-->"+achievementIds[i]+"(未完)";
			// colors[i] = colorssss[req.getAchievementSeriesId()];
			// }
			//
			// QUERY_ACHIEVEMENT_SERIES_RES res = new QUERY_ACHIEVEMENT_SERIES_RES(req.getSequnceNum(),req.getAchievementSeriesId(),
			// achievementIds, achievementNames,colors,currentPage,allPage);
			// return res;
			//
			// }catch(Throwable e){
			// e.printStackTrace();
			// return null;
			// }
		} else if (message instanceof QUERY_ACHIEVEMENT_REQ) {
			// FIXME 测试成就
			// try{
			// QUERY_ACHIEVEMENT_REQ req = (QUERY_ACHIEVEMENT_REQ)message;
			// String[] achievementSeriesNames= new String[] {"任务成就","探索成就","战场成就","副本成就","声望成就","世界事件","光辉事迹"};
			// int achievementId = req.getAchievementId();
			// int seri = achievementId/100;
			//
			// StringBuffer uubS = new StringBuffer(100);
			// uubS.append(achievementSeriesNames[seri]);
			// for( int i=0;i< 10;i++)
			// uubS.append(achievementSeriesNames[seri]+"  描述-->"+i);
			// String uub = uubS.toString();
			//
			// long[] articleIds = new long[]{377,374};
			// String honorName = "新科状元";
			// int honorColor = 0x00ff00;
			// QUERY_ACHIEVEMENT_RES res = new QUERY_ACHIEVEMENT_RES(req.getSequnceNum(),achievementId,uub,articleIds,honorName,honorColor);
			// return res;
			// }catch(Throwable e){
			// e.printStackTrace();
			// return null;
			// }
		} else if (message instanceof QUERY_MASTERS_INFO_REQ) {

		} else if (message instanceof QUERY_PRENTICES_INFO_REQ) {

		} else if (message instanceof QUERY_AUTO_HOOK_USE_PROP_REQ) {
			// String[] roleHpName = ArticleManager.getInstance().getAutoHookPropName(player.getSoulLevel(), ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色药品],
			// ArticleManager.物品二级分类类名_角色药品[ArticleManager.物品二级分类_缓回血量]);
			// String[] roleMpName = ArticleManager.getInstance().getAutoHookPropName(player.getSoulLevel(), ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色药品],
			// ArticleManager.物品二级分类类名_角色药品[ArticleManager.物品二级分类_缓回法力值]);
			// String[] petMpName = ArticleManager.getInstance().getAutoHookPropName(player.getSoulLevel(), ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宠物药品],
			// ArticleManager.物品二级分类类名_宠物药品[ArticleManager.物品二级分类_宠物缓回]);
			// String[] petHappyName = ArticleManager.getInstance().getAutoHookPropName(player.getSoulLevel(), ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宠物药品],
			// ArticleManager.物品二级分类类名_宠物药品[ArticleManager.物品二级分类_宠物寿命]);
			// String[] petLifeName = ArticleManager.getInstance().getAutoHookPropName(player.getSoulLevel(), ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宠物药品],
			// ArticleManager.物品二级分类类名_宠物药品[ArticleManager.物品二级分类_宠物快乐]);
			if (ShopManager.getInstance() != null) {
				Goods[] goods = ShopManager.getInstance().getShop(Translate.挂机商店, player).getGoods(player);
				ArrayList<Integer> roleHpGoodsId = new ArrayList<Integer>();
				ArrayList<String> roleHpName = new ArrayList<String>();
				ArrayList<Integer> roleMpGoodsId = new ArrayList<Integer>();
				ArrayList<String> roleMpName = new ArrayList<String>();
				ArrayList<Integer> petHpGoodsId = new ArrayList<Integer>();
				ArrayList<String> petHpName = new ArrayList<String>();
				ArrayList<Integer> petLifeGoodsId = new ArrayList<Integer>();
				ArrayList<String> petLifeName = new ArrayList<String>();
				ArrayList<Integer> petHappyGoodsId = new ArrayList<Integer>();
				ArrayList<String> petHappyName = new ArrayList<String>();
				for (Goods good : goods) {
					Article article = ArticleManager.getInstance().getArticle(good.getArticleName());
					if (article == null) {
						continue;
					}
					Props prop = null;
					if (article instanceof Props) {
						prop = (Props) article;
					}
					if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_角色药品].equals(article.get物品一级分类())) {
						if (ArticleManager.物品二级分类类名_角色药品[ArticleManager.物品二级分类_缓回血量].equals(article.get物品二级分类()) && prop != null && prop.getLevelLimit() <= player.getLevel()) {
							roleHpGoodsId.add(good.getId());
							roleHpName.add(good.getArticleName());
						} else if (ArticleManager.物品二级分类类名_角色药品[ArticleManager.物品二级分类_缓回法力值].equals(article.get物品二级分类()) && prop != null && prop.getLevelLimit() <= player.getLevel()) {
							roleMpGoodsId.add(good.getId());
							roleMpName.add(good.getArticleName());
						}
					} else if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宠物药品].equals(article.get物品一级分类()) && ArticleManager.物品二级分类类名_宠物药品[ArticleManager.物品二级分类_宠物缓回].equals(article.get物品二级分类()) && prop != null && prop.getLevelLimit() <= player.getLevel()) {
						petHpGoodsId.add(good.getId());
						petHpName.add(good.getArticleName());
					} else if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宠物药品].equals(article.get物品一级分类()) && ArticleManager.物品二级分类类名_宠物药品[ArticleManager.物品二级分类_宠物寿命].equals(article.get物品二级分类())) {
						petLifeGoodsId.add(good.getId());
						petLifeName.add(good.getArticleName());
					} else if (ArticleManager.物品一级分类类名[ArticleManager.物品一级分类_宠物药品].equals(article.get物品一级分类()) && ArticleManager.物品二级分类类名_宠物药品[ArticleManager.物品二级分类_宠物快乐].equals(article.get物品二级分类())) {
						petHappyGoodsId.add(good.getId());
						petHappyName.add(good.getArticleName());
					}
				}
				int[] roleHpID = new int[roleHpGoodsId.size()];
				for (int i = 0; i < roleHpGoodsId.size(); i++) {
					Integer integer = roleHpGoodsId.get(i);
					roleHpID[i] = integer.intValue();
				}
				int[] roleMpID = new int[roleMpGoodsId.size()];
				for (int i = 0; i < roleMpGoodsId.size(); i++) {
					Integer integer = roleMpGoodsId.get(i);
					roleMpID[i] = integer.intValue();
				}
				int[] petHpID = new int[petHpGoodsId.size()];
				for (int i = 0; i < petHpGoodsId.size(); i++) {
					Integer integer = petHpGoodsId.get(i);
					petHpID[i] = integer.intValue();
				}
				int[] petLifeID = new int[petLifeGoodsId.size()];
				for (int i = 0; i < petLifeGoodsId.size(); i++) {
					Integer integer = petLifeGoodsId.get(i);
					petLifeID[i] = integer.intValue();
				}
				int[] petHappyID = new int[petHappyGoodsId.size()];
				for (int i = 0; i < petHappyGoodsId.size(); i++) {
					Integer integer = petHappyGoodsId.get(i);
					petHappyID[i] = integer.intValue();
				}
				QUERY_AUTO_HOOK_USE_PROP_RES res = new QUERY_AUTO_HOOK_USE_PROP_RES(message.getSequnceNum(), Translate.挂机商店, 1, roleHpID, roleHpName.toArray(new String[0]), roleMpID, roleMpName.toArray(new String[0]), petHpID, petHpName.toArray(new String[0]), petHappyID, petHappyName.toArray(new String[0]), petLifeID, petLifeName.toArray(new String[0]));
				return res;
			}
		} else if (message instanceof ACCEPT_CHUANGONG_ARTICLE_REQ) {
			try {
				String result = ChuanGongManager.getInstance().acceptChuangongArticle(player);
				if (!result.equals("")) {
					player.send_HINT_REQ(result);
				}
			} catch (Exception e) {
				ChuanGongManager.logger.error("[领取传功石错误] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] []", e);
			}
			return null;
		} else if (message instanceof APPLY_CHUANGONG_REQ) {
			APPLY_CHUANGONG_REQ req = (APPLY_CHUANGONG_REQ) message;
			Player passive;
			try {
				passive = PlayerManager.getInstance().getPlayer(req.getPlayerId());
				String result = ChuanGongManager.getInstance().applyChuangong(player, passive);
				if (!result.equals("")) {
					player.send_HINT_REQ(result);
				}
			} catch (Exception e) {
				ChuanGongManager.logger.error("[申请传功错误] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] []", e);
			}
			return null;
		} else if (message instanceof FINISH_CHUANGONG_REQ) {

			// FINISH_CHUANGONG_REQ req = (FINISH_CHUANGONG_REQ) message;
			// long cgId = req.getCgId();
			// ChuanGongManager.getInstance().finishChuangong(cgId);
			// return null;
		} else if (message instanceof EXCHANGE_CONFIRMACTION_CODE_REQ) {// 兑换激活码
			EXCHANGE_CONFIRMACTION_CODE_REQ req = (EXCHANGE_CONFIRMACTION_CODE_REQ) message;
			long activityId = req.getActivityId();
			String code = req.getCode();
			String confirmCode =  code;
			// 先得到激活码活动数据,看看包里能否放进去,规则为,有几个奖励至少需要几个空位置
			GameConstants gameConstants = GameConstants.getInstance();
//			QUERY_ALL_ACTIVITY_RES bossRes4Activity = (QUERY_ALL_ACTIVITY_RES) ClientService.getInstance().doQueryActivity(player.getPassport().getRegisterChannel(), gameConstants.getGameName(), gameConstants.getServerName());
//			GameActivity[] activitys = bossRes4Activity.getActivitys();
//			GameActivity activity = null;
//			for (GameActivity ga : activitys) {
//				DataManager.logger.warn("[兑换2] [activityId:"+activityId+"] [code:"+code+"] [getAllCodes:"+ga.getAllCodes()+"] ["+player.getLogString()+"]");
//				if(ga == null || ga.getAllCodes() == null){
//					continue;
//				}
//				if (ga.getAllCodes().contains(confirmCode)) {
//					activity = ga;
//					break;
//				}
//			}
//			if (activity == null) {
//				return new EXCHANGE_CONFIRMACTION_CODE_RES(message.getSequnceNum(), 1, Translate.无效的活动);
//			}
//			activity.initPrizes();

			int emptyCount = player.getKnapsack_common().getEmptyNum();
//			if (activity.getPrizes().length > emptyCount) {
//				return new EXCHANGE_CONFIRMACTION_CODE_RES(message.getSequnceNum(), 1, Translate.你的背包位置不足不能领取);
//			}

			User user = new User(player.getPassport().getLastLoginChannel(), player.getUsername(), "飘渺", gameConstants.getServerName(), emptyCount, player.getName(), player.getLevel());
			EXCHANGE_CONFIRMATION_RES bossRes = (EXCHANGE_CONFIRMATION_RES) ClientService.getInstance().doExchange(code, user, activityId);
			int result = 1;
			String notice = "";
			DataManager.logger.warn("[兑换] [confirmCode:"+confirmCode+"] [sendNotice:"+(bossRes!=null?bossRes.getNotice():"null")+"] [emptyCount:"+emptyCount+"] " +
					"[gameActivity:"+(bossRes != null?bossRes.getGameActivity():"null")+"] ["+player.getLogString()+"]");
			if (bossRes == null) {
				return new EXCHANGE_CONFIRMACTION_CODE_RES(message.getSequnceNum(), result, notice);
			}
			String sendNotice = bossRes.getNotice();
			if ("兑换成功".equals(sendNotice)) {
				sendNotice = Translate.兑换成功;
			} else if ("无效验证码".equals(sendNotice)) {
				sendNotice = Translate.无效验证码;
			} else if ("兑换次数过多".equals(sendNotice)) {
				sendNotice = Translate.兑换次数过多;
			} else if ("无效的活动".equals(sendNotice)) {
				sendNotice = Translate.无效的活动;
			} else if ("活动已过期".equals(sendNotice)) {
				sendNotice = Translate.活动已过期;
			} else if ("渠道不符合".equals(sendNotice)) {
				sendNotice = Translate.渠道不符合;
			} else if (sendNotice.startsWith("活动不匹配")) {
				sendNotice = Translate.活动不匹配;
			}else if ("没空余格子".equals(sendNotice)) {
				sendNotice = "背包放不下";
			}
			// 取得奖励,给玩家发
			GameActivity gameActivity = bossRes.getGameActivity();
			gameActivity.initPrizes();

			Prize[] prizes = gameActivity.getPrizes();
			if (prizes.length > 0 && sendNotice.equals("兑换成功")) {
				for (Prize prize : prizes) {
					Article article = ArticleManager.getInstance().getArticle(prize.getPropName());
					boolean addSuccess = false;
					if (article != null) {
						boolean isEquip = article instanceof Equipment;// 是否是装备,装备有7种颜色,其他5种颜色
						boolean isMagicWeapon = article instanceof MagicWeapon; // 法宝6个颜色
						int color = prize.getColor();
						if (prize.getNum() > 0 && color >= 0 && ((isEquip && color < ArticleManager.color_equipment_Strings.length) || (isMagicWeapon && color < ArticleManager.color_magicweapon_Strings.length) || (!isEquip && color < ArticleManager.color_article_Strings.length))) {
							try {
								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, prize.isBind(), ArticleEntityManager.CREATE_REASON_EXCHANGE_CONFRIMATION_CODE, player, prize.getColor(), (int) prize.getNum(), true);
								player.putToKnapsacks(ae, (int) prize.getNum(), "兑换激活码");
								addSuccess = true;
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
					if (!addSuccess) {
						DataManager.logger.error(player.getLogString() + " [兑换激活码奖励] [物品不存在] [code:" + code + "] [activityId:" + activityId + "] [" + user.toString() + "] [" + prize.toString() + "]");
					}
				}
			}

			EXCHANGE_CONFIRMACTION_CODE_RES res = new EXCHANGE_CONFIRMACTION_CODE_RES(message.getSequnceNum(), bossRes.getResult(), sendNotice);
			return res;
		} else if (message instanceof CREATE_FEEDBACK_REQ) {// 玩家创建一个反馈
			CREATE_FEEDBACK_REQ req = (CREATE_FEEDBACK_REQ) message;
			String content = req.getFcontent();
			int type = req.getFeedbackType();
			String subject = req.getSubject();
			if (subject.equals("")) {
				player.sendError(Translate.玩家反馈主题为空);
				return null;
			}

			if (subject.length() > FeedbackManager.subjectLENGTH) {
				player.sendError(Translate.反馈主题长度大于20字);
				return null;
			}

			if (content.equals("")) {
				player.sendError(Translate.玩家反馈为空);
				return null;
			}
			if (content.length() > FeedbackManager.replyLENGTH) {
				// player.sendError("反馈内容长度大于" + FeedbackManager.replyLENGTH + "字");
				player.sendError(Translate.translateString(Translate.反馈内容长度大于xx, new String[][] { { Translate.STRING_1, FeedbackManager.replyLENGTH + "" } }));
				return null;
			}
			String result = FeedbackManager.getInstance().createFeedBack(player, type, subject, content);
			if (result != null && result.equals("")) {
				player.send_HINT_REQ(Translate.反馈信息成功请耐心等待gm的回答);
				if (FeedbackManager.logger.isWarnEnabled()) FeedbackManager.logger.warn("[玩家创建一个反馈] [" + player.getLogString() + "] [反馈类型:" + type + "]");
			}
		} else if (message instanceof QUERY_FEEDBACK_LIST_REQ) {// 玩家查询自己创建的反馈信息列表

			long[] feedbackList = FeedbackManager.getInstance().getPlayerFeedBackIds(player);
			if (feedbackList != null && feedbackList.length > 0) {

				FeedbackManager fbm = FeedbackManager.getInstance();
				int begin = feedbackList.length - 1;
				List<Feedback> list = new ArrayList<Feedback>();

				List<Feedback> listNeedRead = new ArrayList<Feedback>();
				for (int i = begin; i >= 0; --i) {
					Feedback feedBack = fbm.getFeedBack(feedbackList[i]);
					if (feedBack != null) {
						if (feedBack.getPlayerState() == FeedBackState.新反馈.state || feedBack.getPlayerState() == FeedBackState.未处理.state || feedBack.getPlayerState() == FeedBackState.评价.state) {
							listNeedRead.add(feedBack);
						} else {
							list.add(feedBack);
						}
					} else {
						FeedbackManager.logger.error("[玩家查询反馈信息列表错误] [没有这个信息] [" + player.getLogString() + "] [反馈id:" + feedbackList[i] + "] ");
					}
				}

				int needList = listNeedRead.size();
				if (needList < 100 && list.size() > 0) {
					int beginIndex = 0;
					for (int i = needList; i <= 100; i++) {
						listNeedRead.add(list.get(beginIndex));
						beginIndex++;
						if (beginIndex >= list.size()) {
							break;
						}
					}
				}

				if (listNeedRead != null && listNeedRead.size() > 0) {
					QUERY_FEEDBACK_LIST_RES res = new QUERY_FEEDBACK_LIST_RES(GameMessageFactory.nextSequnceNum(), listNeedRead.toArray(new Feedback[0]));
					player.addMessageToRightBag(res);
					FeedbackManager.logger.warn("[玩家查询反馈信息列完成] [" + player.getLogString() + "]");
					List<Long> listJudge = FeedbackManager.getInstance().queryAllJudgeOne(player);
					if (listJudge != null && listJudge.size() > 0) {
						int length = listJudge.size();
						long[] ids = new long[length];
						for (int i = 0; i < length; i++) {
							ids[i] = listJudge.get(i);
						}
						// 发送所有需要评价的反馈
						FEEDBACK_NOTICE_CLIENT_ALLJUDGE_RES res1 = new FEEDBACK_NOTICE_CLIENT_ALLJUDGE_RES(GameMessageFactory.nextSequnceNum(), ids);
						player.addMessageToRightBag(res1);
						FeedbackManager.logger.warn("[玩家查询反馈信息列完成] [有需要评价] [" + player.getLogString() + "]");
					} else {

						FeedbackManager.logger.warn("[玩家查询反馈信息列完成] [没有需要评价] [" + player.getLogString() + "]");
					}
				} else {
					FeedbackManager.logger.error("[玩家查询反馈信息列表错误] [找不到玩家发表的反馈] [" + player.getLogString() + "]");
				}
			} else {
				player.sendError(Translate.你还没有反馈过消息);
				QUERY_FEEDBACK_LIST_RES res = new QUERY_FEEDBACK_LIST_RES(GameMessageFactory.nextSequnceNum(), new Feedback[0]);
				player.addMessageToRightBag(res);
			}

		} else if (message instanceof QUERY_SPECIAL_FEEDBACK_REQ) {// 玩家查询自己创建的某一反馈详细信息
			QUERY_SPECIAL_FEEDBACK_REQ req = (QUERY_SPECIAL_FEEDBACK_REQ) message;
			long id = req.getId();
			Feedback fb = FeedbackManager.getInstance().getFeedBack(id);
			if (fb != null) {
				if (fb.getPlayerId() == player.getId()) {
					List<Reply> list = fb.getList();
					if (list != null) {
						Reply[] arr = list.toArray(new Reply[0]);
						QUERY_SPECIAL_FEEDBACK_RES res = new QUERY_SPECIAL_FEEDBACK_RES(GameMessageFactory.nextSequnceNum(), arr);
						player.addMessageToRightBag(res);

						FeedbackManager.logger.warn("[玩家查询指定反馈信息成功] [" + player.getLogString() + "] [反馈id:" + id + "]");

						// 发送是否是评价的
						if (fb.isSendJudge() && !fb.isAlreadyJudge()) {
							// 发送评价
							JUDGE_SPECIAL_FEEDBACK_RES res1 = new JUDGE_SPECIAL_FEEDBACK_RES(GameMessageFactory.nextSequnceNum(), id);
							player.addMessageToRightBag(res1);
							FeedbackManager.logger.warn("[玩家查询指定反馈信息成功] [需要评价] [" + player.getLogString() + "] [反馈id:" + id + "]");
						} else {
							// 查看先设成等待反馈 等改客户端时在加查看状态
							if (fb.getPlayerState() != FeedBackState.已关闭.state) {
								// 只有这里是单一设置状态，别处都是成对出现
								fb.setPlayerState(FeedBackState.等待反馈.state);
							}
						}

					}
					FeedbackManager.getInstance().enterGameFeedbackNotice(player);
				} else {
					FeedbackManager.logger.error("[玩家查询指定反馈信息错误] [不是自己的反馈信息] [" + player.getLogString() + "] [反馈id:" + id + "] [发表人:" + fb.getPlayerId() + "]");
				}

			} else {
				FeedbackManager.logger.error("[玩家查询指定反馈信息错误] [没有这个信息] [" + player.getLogString() + "] [反馈id:" + id + "]");
			}

		} else if (message instanceof REPLY_SPECIAL_FEEDBACK_REQ) {// 玩家回复一个反馈
			REPLY_SPECIAL_FEEDBACK_REQ req = (REPLY_SPECIAL_FEEDBACK_REQ) message;
			String content = req.getFcontent();
			if (content.length() > FeedbackManager.replyLENGTH) {
				// player.sendError("你反馈的内容是" + content.length() + "字，超出了最大内容上限的200字");
				player.sendError(Translate.translateString(Translate.你反馈的内容是xx字超出了最大内容上限的200字, new String[][] { { Translate.STRING_1, content.length() + "" } }));
				return null;
			}
			if (content.equals("")) {
				player.sendError(Translate.玩家反馈为空);
				return null;
			}
			long id = req.getId();
			Feedback fb = FeedbackManager.getInstance().getFeedBack(id);
			if (fb != null) {

				FeedbackManager.logger.warn("[查看反馈状态] [" + player.getLogString() + "] [" + fb.getSubject() + "] [player:" + fb.getPlayerState() + "] [gm:" + fb.getGmState() + "]");

				if (fb.getPlayerState() == FeedBackState.已关闭.state || fb.getGmState() == FeedBackState.已关闭.state) {
					FeedbackManager.logger.warn("[玩家反馈已关闭问题] [" + fb.getSubject() + "] [" + player.getLogString() + "]");
					player.sendError(Translate.反馈已关闭玩家不能在此反馈上反馈问题);
					return null;
				}
				Reply r = new Reply();
				r.setFcontent(content);
				fb.playerReply(r);
				player.send_HINT_REQ(Translate.反馈回复完成);
				FeedbackManager.getInstance().enterGameFeedbackNotice(player);
				if (FeedbackManager.logger.isInfoEnabled()) {
					FeedbackManager.logger.info("[反馈回复完成] [" + player.getLogString() + "] []");
				}

			}

		} else if (message instanceof JUDGE_SPECIAL_FEEDBACK_REQ) {// GM要求玩家给他打分,玩家打分
			JUDGE_SPECIAL_FEEDBACK_REQ req = (JUDGE_SPECIAL_FEEDBACK_REQ) message;
			int judge = req.getJudge();
			long id = req.getId();
			Feedback fb = FeedbackManager.getInstance().getFeedBack(id);
			if (!fb.isAlreadyJudge()) {
				fb.setAlreadyJudge(true);
				fb.setJudge(judge);
				player.send_HINT_REQ(Translate.反馈评价完成);
				FeedbackManager.getInstance().enterGameFeedbackNotice(player);
				if (FeedbackManager.logger.isInfoEnabled()) {
					FeedbackManager.logger.info("[反馈评价完成] [" + player.getLogString() + "] []");
				}
			} else {
				if (FeedbackManager.logger.isWarnEnabled()) FeedbackManager.logger.warn("[玩家评价错误] [已经评价过了] [" + player.getLogString() + "] [反馈id:" + id + "]");
			}

		} else if (message instanceof GET_DESCRIPTION_BY_ARTICLE_REQ) {
			GET_DESCRIPTION_BY_ARTICLE_REQ req = (GET_DESCRIPTION_BY_ARTICLE_REQ) message;
			String articleName = req.getArticleName();
			Article article = ArticleManager.getInstance().getArticle(articleName);
			if (article == null) {
				return new GET_DESCRIPTION_BY_ARTICLE_RES(message.getSequnceNum(), "", "", "");
			} else {
				return new GET_DESCRIPTION_BY_ARTICLE_RES(message.getSequnceNum(), article.getName(), article.getIconId() == null ? "" : article.getIconId(), article.getStroy() == null ? article.getName() : article.getStroy());
			}
		} else if (message instanceof HORSE_RIDE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof SYNC_MAGICWEAPON_FOR_KNAPSACK_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_SHENSHI_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof CONFIRM_MAGICWEAPON_EAT_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_MAGICWEAPON_EAT_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof CONFIRM_SHENSHI_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_MAGICWEAPON_HIDDLE_PROP_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_MAGICWEAPON_STRONG_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof MAGICWEAPON_STRONG_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof GET_EFFECT_NOTICE_REQ) {
			NoticeManager.getInstance().getEffectNotice(player);
		} else if (message instanceof OBTAIN_NOTICE_SIVLER_REQ) {
			boolean bln = NoticeManager.getInstance().obtainBindSivler(player);
			if (bln) {
				player.addMessageToRightBag(new OBTAIN_NOTICE_SIVLER_RES(GameMessageFactory.nextSequnceNum()));
			}
		} else if (message instanceof TOURNAMENT_REWARD_REQ) {
			TournamentManager tm = TournamentManager.getInstance();
			TOURNAMENT_REWARD_RES res = new TOURNAMENT_REWARD_RES(message.getSequnceNum(), tm.是否可以领取奖励(player), tm.得到比武奖励());
			return res;
		} else if (message instanceof TOURNAMENT_TAKE_REWARD_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof TOURNAMENT_REWARD_SELF_REQ) {
			TournamentManager tm = TournamentManager.getInstance();
			TOURNAMENT_REWARD_SELF_RES res = new TOURNAMENT_REWARD_SELF_RES(message.getSequnceNum(), tm.得到玩家的奖励(player));
			return res;
		} else if (message instanceof VIP_REQ) {
			VipManager vm = VipManager.getInstance();
			VIP_RES res = new VIP_RES(message.getSequnceNum(), VipManager.vipDatas, VipManager.每分钱对应的银子, vm.是否领取了vip奖励(player));
			return res;
		} else if (message instanceof VIP_2_REQ) {
			VipManager vm = VipManager.getInstance();
			VIP_2_RES res = new VIP_2_RES(message.getSequnceNum(), VipManager.vipDatas2, VipManager.每分钱对应的银子, vm.是否领取了vip奖励(player));
			return res;
		} else if (message instanceof VIP_PULL_FRIEND_REQ) {
			VipManager vm = VipManager.getInstance();
			return vm.vipCallOther(player, (VIP_PULL_FRIEND_REQ) message);
		} else if (message instanceof GET_VIP_REWARD_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof REQUEST_ACHIEVEMENT_LIST_REQ) {// 成就列表
			AchievementManager aManager = AchievementManager.getInstance();
			List<AchievementEntity> list = new ArrayList<AchievementEntity>();

			if (player.achievementEntityMap != null) list.addAll(player.achievementEntityMap.values());

			if (aManager.getAchievementList() == null || aManager.getAchievementList().size() == 0) {
				player.sendError(Translate.系统中不存在任何成就);
				AchievementManager.logger.error(player.getLogString() + "[查询成就列表] [成就不存在]");
				return null;
			}

			return new REQUEST_ACHIEVEMENT_LIST_RES(message.getSequnceNum(), aManager.getAchievementList().size(), list == null ? 0 : list.size(), player.getAchievementDegree(), aManager.getLeftMenus().toArray(new LeftMenu[0]));
		} else if (message instanceof REQUEST_ACHIEVEMENT_DONE_REQ) {// 查看完成成就列表
			REQUEST_ACHIEVEMENT_DONE_REQ req = (REQUEST_ACHIEVEMENT_DONE_REQ) message;
			String mainName = req.getMainName();
			String subName = req.getSubName();
			AchievementManager aManager = AchievementManager.getInstance();
			if (!aManager.getMainSubMap().containsKey(mainName)) {
				player.sendError(Translate.不存在的一级分类 + mainName);
				AchievementManager.logger.error(player.getLogString() + "[查询完成成就列表] [成就一级分类不存在:" + mainName + "]");
				return null;
			}
			HashMap<String, List<Achievement>> subs = aManager.getMainSubMap().get(mainName);
			if (!subs.containsKey(subName)) {
				player.sendError(Translate.不存在的二级分类 + subName);
				AchievementManager.logger.error(player.getLogString() + "[查询完成成就列表] [成就二级分类不存在:" + subName + "]");
				return null;
			}
			List<Achievement> list = subs.get(subName);
			int[] hasNum = new int[list.size()];
			boolean[] isDone = new boolean[list.size()];
			int count = 0;
			for (int i = 0; i < list.size(); i++) {
				Achievement achievement = list.get(i);
				AchievementEntity ae = aManager.getPlayerAchievementEntity(player, achievement);
				if (ae == null) {
					isDone[i] = false;
					GameDataRecord gdr = aManager.getPlayerDataRecord(player, achievement.getAction());
					if (gdr == null) {
						hasNum[i] = 0;
					} else {
						hasNum[i] = (int) gdr.getNum();
						if (AchievementManager.logger.isInfoEnabled()) {
							AchievementManager.logger.info(player.getLogString() + "[查询完成成就] [一级分类:" + mainName + "] [二级分类:" + subName + "] [成就:" + achievement.getName() + "] [完成个数:" + hasNum[i] + "]");
						}
					}
				} else {
					isDone[i] = true;
					count++;
				}
			}
			if (AchievementManager.logger.isInfoEnabled()) {
				AchievementManager.logger.info(player.getLogString() + "[查询完成成就] [一级分类:" + mainName + "] [二级分类:" + subName + "] [成就个数:" + list.size() + "] [完成个数:" + count + "]");
			}
			return new REQUEST_ACHIEVEMENT_DONE_RES(message.getSequnceNum(), mainName, subName, list.toArray(new Achievement[0]), isDone, hasNum);
		} else if (message instanceof REQUEST_ONE_DONE_ACHIEVEMENT_REQ) {
			REQUEST_ONE_DONE_ACHIEVEMENT_REQ req = (REQUEST_ONE_DONE_ACHIEVEMENT_REQ) message;
			int achievementId = req.getAchievementId();
			long targetPlayerId = req.getPlayerId();
			Achievement achievement = AchievementManager.getInstance().getSystemAchievement(achievementId);
			if (achievement == null) {
				player.sendError(Translate.text_achievement_003);
				if (AchievementManager.logger.isInfoEnabled()) {
					AchievementManager.logger.info(player.getLogString() + "[查询玩家的成就] [成就不存在] [目标玩家ID:" + targetPlayerId + "] [成就ID:" + achievementId + "]");
				}
				return null;
			}
			if (!GamePlayerManager.getInstance().isOnline(targetPlayerId)) {
				player.sendError(Translate.text_achievement_005);
				if (AchievementManager.logger.isInfoEnabled()) {
					AchievementManager.logger.info(player.getLogString() + "[查询玩家的成就] [玩家不在线] [目标玩家ID:" + targetPlayerId + "] [成就ID:" + achievementId + "]");
				}
				return null;
			}
			Player targetPlayer = GamePlayerManager.getInstance().getPlayer(targetPlayerId);
			if (targetPlayer == null) {
				player.sendError(Translate.text_achievement_005);
				if (AchievementManager.logger.isInfoEnabled()) {
					AchievementManager.logger.info(player.getLogString() + "[查询玩家的成就] [玩家不存在] [目标玩家ID:" + targetPlayerId + "] [成就ID:" + achievementId + "]");
				}
				return null;
			}
			AchievementEntity ae = AchievementManager.getInstance().getPlayerAchievementEntity(targetPlayer, achievement);
			if (ae == null) {
				player.sendError(Translate.text_achievement_006);
				if (AchievementManager.logger.isInfoEnabled()) {
					AchievementManager.logger.info(player.getLogString() + "[查询玩家的成就] [成就实体不存在] [目标玩家ID:" + targetPlayerId + "] [成就ID:" + achievementId + "]");
				}
				return null;
			}

			return new REQUEST_ONE_DONE_ACHIEVEMENT_RES(message.getSequnceNum(), targetPlayerId, achievementId, AchievementManager.sdf.format(ae.getDeliverTime()), achievement);
		} else if (message instanceof GET_BILLBOARD_MENUS_REQ) {
			BillboardsManager.getInstance().getBillboradMenus(player);
		} else if (message instanceof ACTIVITY_DO_REQ) {
			ACTIVITY_DO_REQ req = (ACTIVITY_DO_REQ)message;
			//全民boss
			if(req.getDoType() == 1){
				if(!BossCityManager.getInstance().isOpen()){
					player.sendError("活动还没开启");
					return null;
				}
				BossCityManager.getInstance().entenRoom(player);
			}
			logger.warn("[执行特殊活动] [type:"+req.getDoType()+"] ["+player.getLogString()+"]");
		}else if (message instanceof GET_ONE_BILLBOARD_REQ) {
			GET_ONE_BILLBOARD_REQ req = (GET_ONE_BILLBOARD_REQ) message;
			String menu = req.getMenu();
			String subMenu = req.getSubMenu();
			if (menu != null && subMenu != null && !menu.equals("") && !subMenu.equals("")) {
				com.fy.engineserver.newBillboard.Billboard bb = BillboardsManager.getInstance().getBillboard(menu, subMenu);
				if (bb != null) {
					bb.getBillboard(player);
				} else {
					BillboardsManager.logger.error("[查看排行榜错误] [没有指定排行榜] [" + player.getLogString() + "] [menu:" + menu + "] [submenu:" + subMenu + "]");
				}
			} else {
				BillboardsManager.logger.error("[查看排行榜错误] [" + player.getLogString() + "] [menu:" + menu + "] [submenu:" + subMenu + "]");
			}
		} else if (message instanceof ACTIVITY_LIST_REQ) {
			List<ActivityIntroduce> fit = ActivityManager.getInstance().getFitActivityIntroduce(player);
			List<ActivityIntroduce> refit = new ArrayList<ActivityIntroduce>();
			// 0x00ff00 绿色//0xff0000红色//0x888888灰色//0xffffff白色
			Calendar now = Calendar.getInstance();
			
			for (int i = 0; i < fit.size(); i++) {
				ActivityIntroduce introduce = fit.get(i);
				if (introduce.getLevelLimit() > player.getLevel()) {
					logger.warn("[查询活动失败] [等级不满足] [活动:"+introduce.getName()+"] [level:"+introduce.getLevelLimit()+"] ["+player.getLogString()+"]");
					refit.add(introduce);
					continue;
				}
				boolean inCycle = introduce.isIncycle(now);
				if (!inCycle) {
					logger.warn("[查询活动失败] [星期区域不满足] [活动:"+introduce.getName()+"] ["+player.getLogString()+"]");
					refit.add(introduce);
					continue;
				}
				if(!introduce.isShowAccordStartAndEndTime()){
					logger.warn("[查询活动失败] [时间不满足] [now:"+System.currentTimeMillis()+"] [活动:"+introduce+"] ["+player.getLogString()+"]");
					refit.add(introduce);
					continue;
				}
			}
			logger.warn("[查询活动] [fit:"+fit.size()+"] [refit:"+refit.size()+"] ["+player.getLogString()+"]");
			fit.removeAll(refit);
			String[] doneDes = new String[fit.size()];
			int[] showColor = new int[fit.size()];
			int[] doneNum = new int[fit.size()];
			int[] maxNum = new int[fit.size()];
			for (int i = 0; i < doneDes.length; i++) {
				doneDes[i] = "";
			}
			for (int i = 0; i < showColor.length; i++) {
				showColor[i] = 0xffffff;
			}
			
			for (int i = 0; i < fit.size(); i++) {
				ActivityIntroduce introduce = fit.get(i);
				//特殊活动处理\
				if(introduce.getId() == 30){
					introduce.setDoType(1);
				}
				if ("".equals(introduce.getTaskGroupName())) {
					doneNum[i] = 0;
					if (logger.isInfoEnabled()) {
						logger.info(player.getLogString() + "[查询活动列表] [直接设置0] [" + introduce.getName() + "] [特殊配置:" + introduce.getTaskGroupName() + "] [最大次数:" + introduce.getDailyNum() + "]");
					}
				}
				if (!"".equals(introduce.getTaskGroupName()) && introduce.getTaskGroupName().toLowerCase().startsWith("s_".toLowerCase())) {
					if (introduce.getTaskGroupName().startsWith(Translate.s_喝酒)) {
						long lastDrinkDate = player.getLastDrinkDate();
						boolean isSameWeek = TimeTool.instance.isSame(lastDrinkDate, SystemTime.currentTimeMillis(), TimeDistance.DAY, 7);
						if(!isSameWeek){
							player.setDrinkTimes(0);
							player.setLastDrinkDate(SystemTime.currentTimeMillis());
						}
						
						String articleName = StringTool.string2Array(introduce.getTaskGroupName(), ",", String.class)[1];
						int[] nums = ArticleManager.getInstance().getUseNumsByArticleName(player, articleName);
						maxNum[i] = VipConf.conf[player.getVipLevel()].vip使用酒的次数 + nums[0] + TimesActivityManager.instacen.getAddNum(player, TimesActivityManager.HEJIU_ACTIVITY);
						doneNum[i] = player.getDrinkTimes();
					} else if (introduce.getTaskGroupName().startsWith(Translate.s_屠魔)) {
						long lastDrinkDate = player.getLastTieDate();
						boolean isSameWeek = TimeTool.instance.isSame(lastDrinkDate, System.currentTimeMillis(), TimeDistance.DAY, 7);
						if(!isSameWeek){
							player.setTieTimes(0);
							player.setLastTieDate(System.currentTimeMillis());
						}
						
						String articleName = StringTool.string2Array(introduce.getTaskGroupName(), ",", String.class)[1];
						int[] nums = ArticleManager.getInstance().getUseNumsByArticleName(player, articleName);
						maxNum[i] = VipConf.conf[player.getVipLevel()].vip使用封魔录的次数 + nums[0] + TimesActivityManager.instacen.getAddNum(player, TimesActivityManager.TUMOTIE_ACTIVITY);
						doneNum[i] = player.getTieTimes();
					} else if (introduce.getTaskGroupName().startsWith(Translate.s_祈福)) {
						doneNum[i] = player.getEveryCliffordCount();
						maxNum[i] = VipManager.getInstance().vip每日增加的祈福使用次数(player) + CliffordManager.每天可以祈福的次数 + TimesActivityManager.instacen.getAddNum(player, TimesActivityManager.QIFU_ACTIVITY);
					} else if (introduce.getTaskGroupName().startsWith(Translate.s_斩妖除魔)) {
						doneNum[i] = player.getTodaySearchePeopleNum();
						maxNum[i] = PeopleSearchManager.dailyNum;
					} else if (introduce.getTaskGroupName().startsWith(Translate.s_仙丹修炼)) {
						doneNum[i] = FurnaceManager.inst.getPlayerEnterTimes(player);
						maxNum[i] = FurnaceManager.inst.getCycleTimes();
					} 
				} else if (!"".equals(introduce.getTaskGroupName())) {
					List<Task> taskList = TaskManager.getInstance().getTaskCollectionsByName(introduce.getTaskGroupName());
					List<Task> taskList2 = new ArrayList<Task>();
					if (taskList != null) {
						for (Task t : taskList) {
							if (t != null) {
								if (t.getCollections().equals(introduce.getTaskGroupName())) {
									taskList2.add(t);
								}
							}
						}
					}
					int[] nums = player.getCycleDeliverInfo(taskList2);
					maxNum[i] = nums[0];
					doneNum[i] = nums[1];
					if (introduce.getTaskGroupName().equals(Translate.果园种植) || introduce.getTaskGroupName().equals(Translate.神农访仙树)) {
						doneNum[i] = doneNum[i] / 8;
					}
				}
				
				if(introduce.getStartXs() != null){
					if(introduce.getStartGames()[player.getCountry() - 1] != null){
						introduce.setStartGame(introduce.getStartGames()[player.getCountry() - 1]);
					}else{
						logger.warn("[设置活动寻路属性] [开始游戏出错] ["+introduce+"] ["+player.getCountry()+"]");
					}
					if(introduce.getStartGameCnNames()[player.getCountry() - 1] != null){
						introduce.setStartGameCnName(introduce.getStartGameCnNames()[player.getCountry() - 1]);
					}else{
						logger.warn("[设置活动寻路属性] [开始游戏cn出错] ["+introduce+"] ["+player.getCountry()+"]");
					}
					introduce.setStartX(introduce.getStartXs()[player.getCountry() - 1]);
					introduce.setStartY(introduce.getStartYs()[player.getCountry() - 1]);
				}
				introduce.setActivityState(introduce.getAState());
				if (maxNum[i] != 0) {
					doneDes[i] += "(" + doneNum[i] + "/" + maxNum[i] + ")";
				}
				if (maxNum[i] == doneNum[i] && maxNum[i] != 0) {
					showColor[i] = 0x00ff00;
				}
				if (introduce.getLevelLimit() > player.getLevel()) {
					showColor[i] = 0x888888;
				}
				logger.warn("[查询活动test] ["+introduce.getName()+"]  [doneDes:"+doneDes[i]+"] ["+player.getName()+"]");
			}
			
			if (logger.isInfoEnabled()) {
				logger.info(player.getLogString() + "[查询活动] [正常返回] [refit:"+refit.size()+"]");
			}
			return new ACTIVITY_LIST_RES(message.getSequnceNum(), fit.toArray(new ActivityIntroduce[0]), showColor, doneDes);
		} else if (message instanceof GET_PLAYERTITLES_REQ) {

			List<PlayerTitle> list = player.getPlayerTitles();
			if (list != null && list.size() > 0) {
				for (PlayerTitle pt : list) {
					int color = PlayerTitlesManager.getInstance().getColorByTitleType(pt.getTitleType());
					String buffName = PlayerTitlesManager.getInstance().getBuffNameByType(pt.getTitleType());
					int buffLevel = PlayerTitlesManager.getInstance().getBuffLevlByType(pt.getTitleType());
					String titleShowName = PlayerTitlesManager.getInstance().getTitleShowNameByType(pt.getTitleType());
					String description = PlayerTitlesManager.getInstance().getDescriptionByType(pt.getTitleType());
					String icon = PlayerTitlesManager.getInstance().getIconByType(pt.getTitleType());
					long lastTime = PlayerTitlesManager.getInstance().getLastTimeByType(pt.getTitleType());

					if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
						String titleName = PlayerTitlesManager.getInstance().getTitleName(pt.getTitleType());
						pt.setTitleName(titleName);
					}
					pt.setColor(color);
					pt.setBuffName(buffName);
					pt.setBuffLevl(buffLevel);
					pt.setTitleShowName(titleShowName);
					pt.setDescription(description);
					pt.setIcon(icon);
					pt.setLastTime(lastTime);
				}

				GET_PLAYERTITLES_RES res = new GET_PLAYERTITLES_RES(GameMessageFactory.nextSequnceNum(), list.toArray(new PlayerTitle[0]));
				player.addMessageToRightBag(res);
				SocialManager.logger.warn("[玩家查看称号成功] [" + player.getLogString() + "]");
			} else {
				player.sendError(Translate.你还没有称号);
				SocialManager.logger.warn("[玩家查看称号] [" + player.getLogString() + "] [玩家还没有称号]");
			}

		} else if (message instanceof SETDEFAULT_PLAYERTITLE_REQ) {

			SETDEFAULT_PLAYERTITLE_REQ req = (SETDEFAULT_PLAYERTITLE_REQ) message;
			int titleType = req.getTitleType();
			player.setPersonTitle(titleType);
		} else if (message instanceof SHOP_GET_BY_QUERY_CONDITION_REQ) {

			SHOP_GET_BY_QUERY_CONDITION_REQ req = (SHOP_GET_BY_QUERY_CONDITION_REQ) message;
			String shopName = req.getShopName();
			byte equipmentType = req.getEquipmentType();

			ShopManager shopManager = ShopManager.getInstance();

			Shop shop = shopManager.getShop(shopName, player);
			if (shop == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2776 + shopName + Translate.text_2777);
				player.addMessageToRightBag(hreq);
			} else {

				if (shop.shopType == ShopManager.商店_历练商店) {
					byte marketType = 0;
					long[] coins = null;
					coins = new long[1];
					coins[0] = player.getLilian();
					Goods gs[] = shop.getGoods(player);
					ArrayList<Goods> goods = new ArrayList<Goods>();
					ArticleManager am = ArticleManager.getInstance();
					for (Goods good : gs) {
						if (good != null) {
							String articleName = good.getArticleName();
							Article a = am.getArticle(articleName);
							if (a instanceof Equipment) {
								Equipment equipment = (Equipment) a;
								if (equipment.getPlayerLevelLimit() <= player.getSoulLevel() && equipment.getCareerLimit() == player.getCareer() && equipment.getEquipmentType() == equipmentType) {
									goods.add(good);
								}
							}
						}
					}
					SHOP_GET_BY_QUERY_CONDITION_RES res = new SHOP_GET_BY_QUERY_CONDITION_RES(message.getSequnceNum(), marketType, shopName, shop.getVersion(), shop.shopType, equipmentType, coins, ShopManager.translate(player, shop, goods.toArray(new Goods[0])));
					return res;
				}
			}
		} else if (message instanceof QUERY_COUNTRY_GONGGAO_REQ) {
			CountryManager cm = CountryManager.getInstance();
			String result = cm.发布国家公告合法性判断(player);
			if (result != null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
				player.addMessageToRightBag(hreq);
				return null;
			}
			QUERY_COUNTRY_GONGGAO_REQ req = (QUERY_COUNTRY_GONGGAO_REQ) message;
			String notice = req.getNotice();
			WordFilter filter = WordFilter.getInstance();
			notice = filter.nvalidAndReplace(notice, 0, "@#\\$%^\\&\\*");
			cm.发布国家公告(player, notice);
			QUERY_COUNTRY_GONGGAO_RES res = new QUERY_COUNTRY_GONGGAO_RES(req.getSequnceNum(), notice);
			return res;
		} else if (message instanceof QUERY_COUNTRY_QIUJIN_JINYAN_REQ) {
			CountryManager cm = CountryManager.getInstance();
			if (cm != null) {
				return cm.得到囚禁和禁言的人(message, player);
			}
		} else if (message instanceof TAKE_SEAL_TASK_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof DOWNCITY_PLAYER_STATUS_CHANGE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof DOWNCITY_CREATE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_TUNSHI_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			am.查询吞噬(player, (QUERY_TUNSHI_REQ) message);
		} else if (message instanceof TUNSHI_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_XILIAN_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			am.查询洗练(player, (QUERY_XILIAN_REQ) message);
		} else if (message instanceof XILIAN_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QILING_INLAY_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QILING_OUTLAY_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_LIANQI_REQ) {
			ArticleManager am = ArticleManager.getInstance();
			am.炼器查询(player, (QUERY_LIANQI_REQ) message);
		} else if (message instanceof LIANQI_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_EQUIPMENT_QILING_REQ) {
			QUERY_EQUIPMENT_QILING_REQ req = (QUERY_EQUIPMENT_QILING_REQ) message;
			long equipmentId = req.getEquipmentId();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleEntity ae = aem.getEntity(equipmentId);
			if (ae instanceof EquipmentEntity) {
				QUERY_EQUIPMENT_QILING_RES res = new QUERY_EQUIPMENT_QILING_RES(req.getSequnceNum(), equipmentId, ((EquipmentEntity) ae).getInlayQiLingArticleTypes(), ((EquipmentEntity) ae).getInlayQiLingArticleIds(), ArticleManager.装备上的器灵描述((EquipmentEntity) ae));
				return res;
			}

		} else if (message instanceof PLAYER_HOOK_INFO_REQ) {
			PlayerHookManager.instance.handle_PLAYER_HOOK_INFO_REQ(player, (PLAYER_HOOK_INFO_REQ) message);
			return null;
		} else if (message instanceof QUERY_KNAPSACK_QILING_REQ) {
			Knapsack sack = player.getKnapsacks_QiLing();
			Cell[] kcs = sack.getCells();
			long[] ids = new long[kcs.length];
			short[] counts = new short[kcs.length];
			for (int j = 0; j < kcs.length; j++) {
				if (kcs[j] != null && kcs[j].getEntityId() != -1 && kcs[j].getCount() > 0) {
					ids[j] = kcs[j].getEntityId();
					counts[j] = (short) kcs[j].getCount();
				} else {
					ids[j] = -1;
					counts[j] = 0;
				}
			}
			QUERY_KNAPSACK_QILING_RES res = new QUERY_KNAPSACK_QILING_RES(message.getSequnceNum(), ArticleManager.器灵仓库格子数, ids, counts);
			return res;

		} else if (message instanceof KNAPSACK_QILING_MOVE_REQ) {
			pushMessageToGame(player, message, null);
		} else if (message instanceof QUERY_PWD_PROTECT_REQ) {// 查看密保信息
			String question = passport.getSecretQuestion();
			question = question == null ? "" : question;
			QUERY_PWD_PROTECT_RES res = new QUERY_PWD_PROTECT_RES(message.getSequnceNum(), question, allQuestion);
			return res;
		} else if (message instanceof SET_PWD_PROTECT_REQ) {
			SET_PWD_PROTECT_REQ req = (SET_PWD_PROTECT_REQ) message;

			String oldAnswer = req.getOldAnswer().trim();
			String oldQuestion = req.getOldQuestion().trim();
			String newQuestion = req.getNewQuestion().trim();
			String newAnswer = req.getNewAnswer().trim();

			String secretQuestion = passport.getSecretQuestion().trim();
			String secretAnswer = passport.getSecretAnswer().trim();
			{
				// modify原有数据
				secretQuestion = secretQuestion == null ? "" : secretQuestion;
				secretAnswer = secretAnswer == null ? "" : secretAnswer;
			}

			if (newQuestion == null || "".equals(newQuestion) || newAnswer == null || "".equals(newAnswer)) {
				logger.error(player.getLogString() + "[修改密保] [失败] [输入的新问题或者新答案错误] [输入的问题:" + oldQuestion + "] [输入的答案:" + oldAnswer + "] [PassPort问题:" + secretQuestion + "] [PassPort答案:" + secretAnswer + "]");
				return new SET_PWD_PROTECT_RES(message.getSequnceNum(), (byte) 1, Translate.输入的新问题或新答案错误);
			}

			if (newAnswer.getBytes().length > 60) {
				logger.error(player.getLogString() + "[修改密保] [失败] [输入的新答案过长:" + newAnswer.getBytes().length + "] [输入的问题:" + oldQuestion + "] [输入的答案:" + oldAnswer + "] [PassPort问题:" + secretQuestion + "] [PassPort答案:" + secretAnswer + "]");
				return new SET_PWD_PROTECT_RES(message.getSequnceNum(), (byte) 1, Translate.输入的新密码过长请重新输入);
			}

			if (!(secretAnswer.equals(oldAnswer) && secretQuestion.equals(oldQuestion))) {// 问题或者答案不匹配
				logger.error(player.getLogString() + "[修改密保] [失败] [输入的旧问题或者旧答案错误] [输入的问题:" + oldQuestion + "] [输入的答案:" + oldAnswer + "] [PassPort问题:" + secretQuestion + "] [PassPort答案:" + secretAnswer + "]");
				return new SET_PWD_PROTECT_RES(message.getSequnceNum(), (byte) 1, Translate.输入的旧问题或旧答案错误);
			}

			if (!isAvailabeQuestion(newQuestion)) {
				logger.error(player.getLogString() + "[修改密保] [失败] [输入的新问题错误] [输入的问题:" + oldQuestion + "] [输入的答案:" + oldAnswer + "] [PassPort问题:" + secretQuestion + "] [PassPort答案:" + secretAnswer + "]");
				return new SET_PWD_PROTECT_RES(message.getSequnceNum(), (byte) 1, Translate.新问题错误);
			}

			// 设置新的数据
			passport.setSecretAnswer(newAnswer);
			passport.setSecretQuestion(newQuestion);
			passport.setLastQuestionSetDate(new Date());

			boolean succ = bossClientService.update(passport);
			if (succ) {
				logger.error(player.getLogString() + "[修改密保] [成功] [输入的问题:" + oldQuestion + "] [输入的答案:" + oldAnswer + "] [PassPort问题:" + secretQuestion + "] [PassPort答案:" + secretAnswer + "]");
				return new SET_PWD_PROTECT_RES(message.getSequnceNum(), (byte) 0, Translate.设置成功);
			} else {
				logger.error(player.getLogString() + "[修改密保] [失败] [update失败] [输入的问题:" + oldQuestion + "] [输入的答案:" + oldAnswer + "] [PassPort问题:" + secretQuestion + "] [PassPort答案:" + secretAnswer + "]");
				return new SET_PWD_PROTECT_RES(message.getSequnceNum(), (byte) 1, Translate.设置成功);
			}
		} else if (message instanceof QUERY_CHARGE_LIST_REQ) {

			if (!EnterLimitManager.canChongZhi(conn)) {
				player.sendError(closeChargeNotice);
				logger.warn("[不能查看充值的MD5] + " + player.getLogString());
				return null;
			}
			if (closeCharge) {
				if (!"".equals(closeChargeNotice)) {
					player.sendError(closeChargeNotice);
				}
				return null;
			}
			String userChannel = passport.getLastLoginChannel();
			ChargeManager chargeManager = ChargeManager.getInstance();
			boolean checkChannel = true;
			if (ChargeManager.getInstance().inWhiteList(passport.getUserName())) {
				checkChannel = false;
			}
			// 网关boss迁移，充值屏蔽
			// if(checkChannel){
			// if(ChargeManager.getInstance().limitChannelChargeList.contains(userChannel)){
			// player.sendError(Translate.暂不开放);
			// }
			// }

			GameConstants constants = GameConstants.getInstance();
			if ((checkChannel && chargeManager.inCloseChanneList(userChannel)) || "百里沃野".equals(constants.getServerName())) {
				player.sendError(Translate.暂不开放);
				return null;
			}
			// if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			// if ("KOREAGOOGLE_MIESHI".equals(userChannel)) {
			// player.sendError("CBT 버전에서는 충전할 수 없습니다");
			// return null;
			// }
			// if (ChargeManager.logger.isInfoEnabled()) {
			// ChargeManager.logger.info(player.getLogString() + "[[查询充值列表] [成功] [平台:" + PlatformManager.getInstance().getPlatform() + "] [渠道:" + userChannel + "] [" +
			// "KOREAGOOGLE_MIESHI".equals(userChannel) + "]");
			// }
			// }
			if (!appChargeOpen) {
				if ("APPSTORE_XUNXIAN".equals(userChannel)) {
					player.sendError(notice);
					ChargeManager.logger.error(player.getLogString() + " [不能充值] [用户渠道:" + userChannel + "] [" + notice + "]");
					return null;
				}
			}
			if ("HUASHENGSDK_XUNXIAN".equals(userChannel)) {
				player.sendError(Translate.暂不开放);
				ChargeManager.logger.error(player.getLogString() + " [HUASHENGSDK_XUNXIAN不能充值] [用户渠道:" + userChannel + "] [" + notice + "]");
				return null;
			}
			// 3.0.0之前的版本 并且渠道是APPSTOREGUOJI_MIESHI的不让充值
//			if ("APPSTOREGUOJI_MIESHI".equals(userChannel)) {
//
//				String programVersion = player.getClientInfo("clientProgramVersion");
//				String[] strings = programVersion.split("\\.");
//				if (strings == null || strings.length == 0 || !"3".equals(strings[0])) {
//					player.sendError("尊敬的用户，您好！因为充值线路问题，目前游戏充值会出现充值成功但无法到账的情况，为了保障您的利益，建议您暂时不要进行充值操作，以免损失。我们正在努力修正此问题，并将于近期更端解决（预计一周内）。新端除了解决目前的充值问题外还将增加更多新功能和乐趣玩法。如您有其它问题，欢迎随时与客服联系。");
//					return null;
//				}
//			}

			List<ChargeMode> list = chargeManager.getChannelChargeModes(userChannel);
			List<ChargeMode> returnList = new ArrayList<ChargeMode>();
			if (list == null || list.size() == 0) {
				player.sendError(Translate.无匹的充值信息请联系GM);
				ChargeManager.logger.error(player.getLogString() + "[查询充值列表] [异常] [无匹配的充值列表] [渠道:" + userChannel + "]");
				return null;
			}
			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[[查询充值列表] [成功] [充值列表长度" + list.size() + "] [渠道:" + userChannel + "] [white:" + checkChannel + "]");
			}

			List<CompoundReturn> cReturn = chargeManager.chargeActivitySpecialConfig();
			boolean isEffecvite = false;
			for (int i = 0; i < cReturn.size(); i++) {
				if (cReturn.get(i) != null && cReturn.get(i).getBooleanValue()) {
					isEffecvite = true;
					for (ChargeMode cMode : list) {
						// 多个活动
						cMode.setFootDescription(cReturn.get(i).getStringValue());
					}
				}
			}
			if (!isEffecvite && !PlatformManager.getInstance().isPlatformOf(Platform.韩国, Platform.台湾)) {
				for (ChargeMode cMode : list) {
					if (cMode instanceof CmccMMChargeMode) {
						//cMode.setFootDescription("请您正确选择充值面额，并保证通畅的网络环境。否则可能造成充值失效，并可能无法找回！");
					} else {
						//cMode.setFootDescription("请您正确选择充值卡面值，不符将可能导致交易失败，可能造成充值卡失效，并可能无法找回！");
					}
				}
			}

			for (ChargeMode c : list) {
				ChargeMode a = (ChargeMode) c.clone();
				returnList.add(a);
				ChargeManager.logger.warn("克隆后:" + a.toString());
			}
			QUERY_CHARGE_LIST_RES res = new QUERY_CHARGE_LIST_RES(message.getSequnceNum(), returnList.toArray(new ChargeMode[0]));
			return res;
		} else if (message instanceof CHARGE_LIST_REQ) {
			ChargeManager.getInstance().queryChargeList((CHARGE_LIST_REQ)message,player);
		}else if (message instanceof QUERY_CHARGE_MONEY_CONFIGURE_REQ) {
			QUERY_CHARGE_MONEY_CONFIGURE_REQ req = (QUERY_CHARGE_MONEY_CONFIGURE_REQ) message;
			ChargeManager chargeManager = ChargeManager.getInstance();

			String modeName = req.getModeName();
			String clientChannel = req.getChannel();
			// 修改了此处
			String userChannel = clientChannel;// passport.getLastLoginChannel();
			if (ChargeManager.logger.isInfoEnabled()) {
				ChargeManager.logger.info(player.getLogString() + "[查询充值信息] [充值modeName:" + modeName + "] [渠道:" + userChannel + "]");
			}

			List<ChargeMode> list = chargeManager.getChannelChargeModes(userChannel);
			if (list == null || list.size() == 0) {
				player.sendError(Translate.无匹的充值信息请联系GM);
				ChargeManager.logger.error(player.getLogString() + "[查询充值信息] [异常] [无匹配的充值列表] [渠道:" + userChannel + "]");
				return null;
			}
			if (ChargeManager.logger.isInfoEnabled()) {
				ChargeManager.logger.info(player.getLogString() + "[查询充值信息] [充值列表长度" + list.size() + "] [渠道:" + userChannel + "]");
			}
			if ("幽恋蝶谷".equals(GameConstants.getInstance().getServerName())) {
				modeName = "AppStoreGuoji";
			}
			ChargeMode chargeMode = null;
			for (ChargeMode temp : list) {
				if (temp.getModeName().equals(modeName)) {
					chargeMode = temp;
					break;
				}
			}

			if (chargeMode == null) {
				player.sendError(Translate.无匹的充值信息请联系GM);
				ChargeManager.logger.error(player.getLogString() + "[查询充值列表] [异常] [无匹配的充值信息] [modeName:" + modeName + "] [渠道:" + userChannel + "]");
				return null;
			}
			List<ChargeMoneyConfigure> configures = chargeMode.getMoneyConfigures(player);
			if (configures == null || configures.size() == 0) {
				player.sendError(Translate.无任何充值方式请联系GM);
				ChargeManager.logger.error(player.getLogString() + "[查询充值列表] [异常] [该充值列表下无任何信息] [modeName:" + modeName + "] [渠道:" + userChannel + "]");
				return null;
			}
			if (ChargeManager.logger.isInfoEnabled()) {
				ChargeManager.logger.info(player.getLogString() + "[查询充值列表] [成功] [modeName:" + modeName + "] [列表长度:" + configures.size() + "] [渠道:" + userChannel + "]");
			}
			return new QUERY_CHARGE_MONEY_CONFIGURE_RES(message.getSequnceNum(), (ChargeMode) chargeMode.clone(), configures.toArray(new ChargeMoneyConfigure[0]));
		} else if (message instanceof CHARGE_REQ) {

			CHARGE_REQ req = (CHARGE_REQ) message;
			ChargeManager chargeManager = ChargeManager.getInstance();

			String channel = req.getChannel();
			String os = req.getOs();
			String modeName = req.getModeName();
			String confId = req.getId();
			String[] chargeParms = req.getChargeParms();

			channel = chargeManager.modifyChannelName(channel);
			// 修改了这里
			String userChannel = channel;// passport.getLastLoginChannel();

			if (channel.equalsIgnoreCase("APPSTORE_XUNXIAN") || channel.equalsIgnoreCase("JIUZHOUAPPSTORE_XUNXIAN") || channel.equalsIgnoreCase("QILEAPPSTORE_XUNXIAN") || channel.equalsIgnoreCase("XUNXIANJUEAPPSTORE_XUNXIAN")){
				String dcode = chargeParms.length > 0 ? chargeParms[0] : "UNKNOWN";
				try {
					if (ChargeManager.useNewChargeInterface) {
						BossClientService.getInstance().appStoreSaving(username, GameConstants.getInstance().getServerName(), channel, req.getModeName(), dcode, new String[] { String.valueOf(player.getId()),confId,"" ,player.getName(),modeName,""});
					} else {
						BossClientService.getInstance().appStoreSaving(username, GameConstants.getInstance().getServerName(), channel, req.getModeName(), dcode);
					}
					AppStoreSavingLimit.notifyUUIDSaving(player, dcode);
					ChargeManager.logger.warn(player.getLogString() + "[AppStore提交充值请求] [成功] [username:" + username + "] [channel:" + channel + "] [modeName:" + modeName + "] [dcode:" + dcode + "] [confId:" + confId + "]");
					return new CHARGE_RES(message.getSequnceNum(), Translate.充值完成请稍候查询充值结果);
				} catch (Exception e) {
					ChargeManager.logger.error(player.getLogString() + "[AppStore提交充值请求] [失败] [username:" + username + "] [channel:" + channel + "] [modeName:" + modeName + "] [dcode:" + dcode + "] [confId:" + confId + "]", e);
					return new CHARGE_RES(message.getSequnceNum(), Translate.充值失败请联系GM);
				} finally {
					// 告知客户端协议已收到
					GameConstants gameConstants = GameConstants.getInstance();
					NOTICE_CLIENT_APP_CHARGE_REQ app_CHARGE_REQ = new NOTICE_CLIENT_APP_CHARGE_REQ(GameMessageFactory.nextSequnceNum(), modeName, gameConstants.getServerName(), player.getId(), new String[0]);
					player.addMessageToRightBag(app_CHARGE_REQ);
					ChargeManager.logger.warn(player.getLogString() + "[AppStore充值通知客户端] [成功] [username:" + username + "] [channel:" + channel + "] [modeName:" + modeName + "] [dcode:" + dcode + "] [confId:" + confId + "]");
				}
			}

			if (chargeParms == null || chargeParms.length == 0) {
				chargeParms = new String[2];
				chargeParms[0] = "";
				chargeParms[1] = "";
			}
			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[提交充值请求] [channel:" + channel + "] [os:" + os + "] [modeName:" + modeName + "] [confId:" + confId + "] [chargeParms:" + Arrays.toString(chargeParms) + "]");
			}

			ChargeMoneyConfigure chargeMoneyConfigure = chargeManager.getChargeMoneyConfigures().get(confId);
			if (chargeMoneyConfigure == null) {
				player.sendError(Translate.无效的充值);
				ChargeManager.logger.error(player.getLogString() + "[充值] [异常] [无效的充值id:" + confId + "] [modeName:" + modeName + "] [渠道:" + userChannel + "]");
				return null;
			}
			ChargeMode cm = chargeMoneyConfigure.getChargeMode(modeName);
			if (chargeMoneyConfigure.getChargeMode() == null || cm == null) {
				player.sendError(Translate.无效的充值);
				ChargeManager.logger.error(player.getLogString() + "[充值] [异常] [充值金额ID与充值名字不匹配] [充值ID:" + confId + "] [modeName:" + modeName + "] [渠道:" + userChannel + "]");
				return null;
			}
			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[提交充值请求] [调用接口前] [channel:" + channel + "] [userChannel:" + userChannel + "] [os:" + os + "] [modeName:" + modeName + "] [confId:" + confId + "] [chargeParms:" + Arrays.toString(chargeParms) + "] [充值卡类型:" + cm.getClass() + "]");
			}
			String notice = cm.doCharge(player, passport, userChannel, os, chargeMoneyConfigure, chargeParms);
			ChargeManager.logger.error(player.getLogString() + "[充值] [提交成功] [充值ID:" + confId + "] [modeName:" + modeName + "] [金额:" + chargeMoneyConfigure.getDenomination() + "分] [" + notice + "] [渠道:" + userChannel + "] [chargeParms:" + Arrays.toString(chargeParms) + "]");
			if (notice == null || "".equals(notice)) {
				notice = Translate.充值申请提交成功;
			}
			return new CHARGE_RES(message.getSequnceNum(), notice);
		} else if (message instanceof TENCENT_GET_CHARGE_ORDER_REQ) {
			long start = SystemTime.currentTimeMillis();
			TENCENT_GET_CHARGE_ORDER_REQ req = (TENCENT_GET_CHARGE_ORDER_REQ) message;

			String confId = req.getId();
			String modeName = req.getModeName();

			GameConstants constants = GameConstants.getInstance();
			int result = 0;
			int goodsNum = 0;
			String orderId = "";
			String cardId = "";
			String savingReason = "";

			ChargeManager chargeManager = ChargeManager.getInstance();
			List<ChargeMode> modeList = chargeManager.getChannelChargeModes("QQ_MIESHI");// .getChannelChargeModes().get("QQ_MIESHI");
			if (modeList == null || modeList.size() == 0) {
				ChargeManager.logger.error(player.getLogString() + "[腾讯充值,请求订单号] [失败] [无匹配的渠道充值] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new TENCENT_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, "无匹配的渠道充值");
			}
			ChargeMoneyConfigure chargeMoneyConfigure = chargeManager.getChargeMoneyConfigures().get(confId);
			if (chargeMoneyConfigure == null) {
				ChargeManager.logger.error(player.getLogString() + "[腾讯充值,请求订单号] [失败] [无效的充值面额] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new TENCENT_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, "无效的充值面额");
			}

			ChargeMode chargeMode = chargeMoneyConfigure.getChargeMode(modeName);
			if (chargeMode == null) {
				ChargeManager.logger.error(player.getLogString() + "[腾讯充值,请求订单号] [失败] [无效的充值方式] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new TENCENT_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, Translate.无效的充值方式);
			}
			try {
				if (ChargeManager.useNewChargeInterface) {
					orderId = BossClientService.getInstance().savingForQQUser(player.getUsername(), goodsNum, modeName, constants.getServerName(), "", new String[] { String.valueOf(player.getId()) });
				} else {

					orderId = BossClientService.getInstance().savingForQQUser(player.getUsername(), goodsNum, modeName, constants.getServerName());
				}
			} catch (Exception e) {
				result = 1;
				orderId = Translate.通信异常请稍后再试;
				ChargeManager.logger.error(player.getLogString() + "[腾讯充值,请求订单号] [异常] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]", e);
			}
			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[腾讯充值,请求订单号] [成功] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "] [订单号:" + orderId + "] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			}
			return new TENCENT_GET_CHARGE_ORDER_RES(message.getSequnceNum(), result, orderId);
		} else if (message instanceof QUERY_ORDER_LIST_REQ) {
			try {
				long start = SystemTime.currentTimeMillis();
				QUERY_ORDER_LIST_REQ req = (QUERY_ORDER_LIST_REQ) message;
				int pageIndex = req.getPageIndex();
				int pageSize = req.getPageSize();
				if (pageIndex < 0 || pageSize <= 0) {
					player.sendError(Translate.非法操作);
					if (ChargeManager.logger.isWarnEnabled()) {
						ChargeManager.logger.warn(player.getLogString() + "[查询充值列表] [输入异常] [pageSize:" + pageSize + "] [pageIndex:" + pageIndex + "]");
					}
					return null;
				}
				BossClientService bossClientService = BossClientService.getInstance();
				SavingPageRecord savingRecord = bossClientService.getSavingRecord(player.getUsername(), pageSize, pageIndex);
				if (savingRecord == null || savingRecord.getRecords() == null || savingRecord.getRecords().length == 0) {
					player.sendError(Translate.暂无充值记录);
					if (ChargeManager.logger.isWarnEnabled()) {
						ChargeManager.logger.warn(player.getLogString() + "[查询充值列表] [没有充值记录] [pageSize:" + pageSize + "] [pageIndex:" + pageIndex + "] [savingRecord:" + savingRecord + "] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
					}
					return null;
				}
				SavingRecord[] records = savingRecord.getRecords();
				List<String> recordLists = new ArrayList<String>();
				for (int i = 0; i < records.length; i++) {
					
					SavingRecord rd = records[i];
					if (rd == null) {
						if (ChargeManager.logger.isWarnEnabled()) {
							ChargeManager.logger.warn(player.getLogString() + "[查询充值列表] [含有空的记录] [第" + i + "个]");
						}
					} else {
						StringBuffer sbf = new StringBuffer();
						sbf.append("<f color='0xFFFF00' size='35'>").append(Translate.充值状态).append(rd.getStatusDesp()).append("</f>\n");
						sbf.append("<f color='0x46DFD0' size='35'>").append(Translate.订单编号).append("</f><f size='35'>").append(rd.getOrderId()).append("</f>\n");
						// sbf.append("<f color='0x46DFD0' size='28'>").append(Translate.充值时间).append("</f><f size='28'>").append(sdf.format(rd.getCreateTime())).append("</f><f color='0x46DFD0' size='28'> 充值金额:</f><f size='28'>").append((rd.getSavingAmount()
						// <= 0) ? "--" : (rd.getSavingAmount() / 100 +
						// "元")).append("</f><f color='0x46DFD0' size='28'> 充值方式</f><f size='28'>").append(rd.getSavingMedium()).append("</f>");
						sbf.append("<f color='0x46DFD0' size='35'>").append(Translate.充值时间).append("</f><f size='35'>").append(sdf.format(rd.getCreateTime())).append("</f><f color='0x46DFD0' size='35'> " + Translate.充值金额 + ":</f><f size='35'>").append(getChargeMoneyDes(rd)).append("</f> "); 
								
//						if(player.getPassport() != null && (player.getPassport().getRegisterChannel().equals("QILEAPPSTORE_XUNXIAN") 
//								|| player.getPassport().getRegisterChannel().equals("AIWANA2PPSTORE_XUNXIAN") || player.getPassport().getRegisterChannel().equals("AIWANAPPSTORE_XUNXIAN"))){
//						}else{
//							sbf.append("<f color='0x46DFD0' size='35'>"+Translate.充值方式 + "</f><f size='35'>").append(modifyChargeModeName(rd.getSavingMedium())).append("</f>");
//						}
						recordLists.add(sbf.toString());
					}
				}
				int totalNum = (int) savingRecord.getTotalPageCount();
				int totalPage = 0;
				totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : 1 + totalNum / pageSize;
				if (ChargeManager.logger.isWarnEnabled()) {
					ChargeManager.logger.warn(player.getLogString() + "[查询充值列表] [成功] [recordLists:"+recordLists+"] [pageSize:" + pageSize + "] [pageIndex:" + pageIndex + "] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
				}
				return new QUERY_ORDER_LIST_RES(message.getSequnceNum(), pageIndex, pageSize, totalPage, recordLists.toArray(new String[0]));
			} catch (Exception e) {
				ChargeManager.logger.error(player.getLogString() + "[查询充值列表] [异常]", e);
				return null;
			}
		} else if (message instanceof AY_ARGS_REQ) {
			AlipayArgs args = BossClientService.getInstance().getAlipayArgs();
			if (args != null) {
				return new A_ARGS_RES(message.getSequnceNum(), args.getPartnerID(), args.getSellerID(), args.getAliPublicKey(), args.getOwnPublicKey(), args.getOwnPrivateKey(), args.getNotifyUrl());
			}
		} else if (message instanceof A_GET_ORDERID_REQ) {
			A_GET_ORDERID_REQ req = (A_GET_ORDERID_REQ) message;
			String channel = req.getChannel();
			String cardId = "";
			String savingReason = "";
			String newChannel = "";
			if (channel != null && channel.contains("@@@@")) {
				try {
					String strs[] = channel.split("@@@@");
					if (strs.length != 3) {
						ChargeManager.logger.error(player.getLogString() + "[通用协议,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [channel:" + channel + "] [newChannel:" + newChannel + "] [cardId:" + cardId + "] [savingReason:" + savingReason + "]");
						return new A_GET_ORDERID_RES(req.getSequnceNum(), "", "", "", 0l);
					}
					for (String s : strs) {
						if (s == null || s.isEmpty()) {
							ChargeManager.logger.error(player.getLogString() + "[通用协议,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [channel:" + channel + "] [newChannel:" + newChannel + "] [cardId:" + cardId + "] [savingReason:" + savingReason + "]");
							return new A_GET_ORDERID_RES(req.getSequnceNum(), "", "", "", 0l);
						}
					}
					newChannel = strs[0];
					cardId = strs[1];
					savingReason = strs[2];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			AliOrderInfo info = null;
			if (ChargeManager.useNewChargeInterface) {
				if (cardId != null && !cardId.isEmpty() && savingReason != null && !savingReason.isEmpty() && newChannel != null && !newChannel.isEmpty()) {
					info = BossClientService.getInstance().getAlipayOrderID(username, channel, GameConstants.getInstance().getServerName(), req.getPAmount(), new String[] { String.valueOf(player.getId()), cardId, savingReason });
				} else {
					info = BossClientService.getInstance().getAlipayOrderID(username, channel, GameConstants.getInstance().getServerName(), req.getPAmount(), new String[] { String.valueOf(player.getId()) });
				}
			} else {
				info = BossClientService.getInstance().getAlipayOrderID(username, channel, GameConstants.getInstance().getServerName(), req.getPAmount());
			}
			if (info != null) {
				if (logger.isWarnEnabled()) {
					logger.warn("[生成支付宝订单] [username:" + username + "] [" + req.getPAmount() + "] [channel:" + channel + "] [servername:" + GameConstants.getInstance().getServerName() + "] [orderID:" + info.getOrderId() + "] [info:" + info.getOrderInfo() + "] [pkey:" + info.getPublicKey() + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
				return new A_GET_ORDERID_RES(req.getSequnceNum(), info.getOrderId(), info.getOrderInfo(), info.getPublicKey(), req.getPAmount());
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[生成支付宝订单] [失败:无法获得订单] [username:" + username + "] [" + req.getPAmount() + "] [channel:" + channel + "] [servername:" + GameConstants.getInstance().getServerName() + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				}
			}
		} else if (message instanceof JIU1_GET_CHARGE_ORDER_REQ) {
			long start = SystemTime.currentTimeMillis();
			JIU1_GET_CHARGE_ORDER_REQ req = (JIU1_GET_CHARGE_ORDER_REQ) message;
			String confId = req.getId();
			String modeName = req.getModeName();
			String os = req.getOs();
			String userChannel = req.getChannel();

			int result = 0;
			int goodsNum = 0;
			String orderId = "";
			String cardId = "";
			String savingReason = "";

			String newChannel = "";
			if (userChannel != null && userChannel.contains("@@@@")) {
				try {
					String strs[] = userChannel.split("@@@@");
					if (strs.length != 3) {
						ChargeManager.logger.error(player.getLogString() + "[91充值,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
						return new JIU1_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, Translate.无效的充值方式, 0);
					}
					for (String s : strs) {
						if (s == null || s.isEmpty()) {
							ChargeManager.logger.error(player.getLogString() + "[91充值,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
							return new JIU1_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, Translate.无效的充值方式, 0);
						}
					}
					newChannel = strs[0];
					cardId = strs[1];
					savingReason = strs[2];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ChargeManager chargeManager = ChargeManager.getInstance();
			List<ChargeMode> modeList = chargeManager.getChannelChargeModes(userChannel);
			if (cardId != null && !cardId.isEmpty() && savingReason != null && !savingReason.isEmpty() && newChannel != null && !newChannel.isEmpty()) {
				modeList = chargeManager.getChannelChargeModes(newChannel);
			}
			if (modeList == null || modeList.size() == 0) {
				ChargeManager.logger.error(player.getLogString() + "[91充值,请求订单号] [失败] [无匹配的渠道充值] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new JIU1_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, "无匹配的渠道充值", 0);
			}
			ChargeMoneyConfigure chargeMoneyConfigure = chargeManager.getChargeMoneyConfigures().get(confId);
			if (chargeMoneyConfigure == null) {
				ChargeManager.logger.error(player.getLogString() + "[91充值,请求订单号] [失败] [无效的充值面额] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new JIU1_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, "无效的充值面额", 0);
			}

			ChargeMode chargeMode = chargeMoneyConfigure.getChargeMode(modeName);
			if (chargeMode == null) {
				ChargeManager.logger.error(player.getLogString() + "[91充值,请求订单号] [失败] [无效的充值方式] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new JIU1_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, Translate.无效的充值方式, 0);
			}
			try {
				GameConstants gameConstants = GameConstants.getInstance();
				if (ChargeManager.useNewChargeInterface) {
					if (cardId != null && !cardId.isEmpty() && savingReason != null && !savingReason.isEmpty() && newChannel != null && !newChannel.isEmpty()) {
						orderId = BossClientService.getInstance().savingFor91User(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), newChannel, os, new String[] { String.valueOf(player.getId()), cardId, savingReason });
					} else {
						orderId = BossClientService.getInstance().savingFor91User(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), userChannel, os, new String[] { String.valueOf(player.getId()) });
					}
				} else {
					orderId = BossClientService.getInstance().savingFor91User(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), userChannel, os);
				}
			} catch (Exception e) {
				result = 1;
				orderId = Translate.通信异常请稍后再试;
				ChargeManager.logger.error(player.getLogString() + "[91充值,请求订单号] [异常] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]", e);
			}
			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[91充值,请求订单号] [成功] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "] [订单号:" + orderId + "] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			}
			return new JIU1_GET_CHARGE_ORDER_RES(message.getSequnceNum(), result, orderId, chargeMoneyConfigure.getDenomination());
		} else if (message instanceof YINGYONGHUI_GET_CHARGE_ORDER_REQ) {
			long start = SystemTime.currentTimeMillis();
			YINGYONGHUI_GET_CHARGE_ORDER_REQ req = (YINGYONGHUI_GET_CHARGE_ORDER_REQ) message;
			String confId = req.getId();
			String modeName = req.getModeName();
			String os = req.getOs();
			String userChannel = req.getChannel();

			int result = 0;
			int goodsNum = 0;
			String orderId = "";
			String cardId = "";
			String savingReason = "";

			String newChannel = "";
			if (userChannel != null && userChannel.contains("@@@@")) {
				try {
					String strs[] = userChannel.split("@@@@");
					if (strs.length != 3) {
						ChargeManager.logger.error(player.getLogString() + "[应用会充值,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
						return new YINGYONGHUI_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, Translate.无效的充值方式, 0, "", 0);
					}
					for (String s : strs) {
						if (s == null || s.isEmpty()) {
							ChargeManager.logger.error(player.getLogString() + "[应用会充值,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
							return new YINGYONGHUI_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, Translate.无效的充值方式, 0, "", 0);
						}
					}
					newChannel = strs[0];
					cardId = strs[1];
					savingReason = strs[2];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ChargeManager chargeManager = ChargeManager.getInstance();
			List<ChargeMode> modeList = chargeManager.getChannelChargeModes(userChannel);
			if (cardId != null && !cardId.isEmpty() && savingReason != null && !savingReason.isEmpty() && newChannel != null && !newChannel.isEmpty()) {
				modeList = chargeManager.getChannelChargeModes(newChannel);
			}
			if (modeList == null || modeList.size() == 0) {
				ChargeManager.logger.error(player.getLogString() + "[应用会充值,请求订单号] [失败] [无匹配的渠道充值] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new YINGYONGHUI_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, "无匹配的渠道充值", 0, "", 0);
			}
			ChargeMoneyConfigure chargeMoneyConfigure = chargeManager.getChargeMoneyConfigures().get(confId);
			if (chargeMoneyConfigure == null) {
				ChargeManager.logger.error(player.getLogString() + "[应用会充值,请求订单号] [失败] [无效的充值面额] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new YINGYONGHUI_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, "无效的充值面额", 0, "", 0);
			}

			ChargeMode chargeMode = chargeMoneyConfigure.getChargeMode(modeName);
			if (chargeMode == null) {
				ChargeManager.logger.error(player.getLogString() + "[应用会充值,请求订单号] [失败] [无效的充值方式] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new YINGYONGHUI_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, Translate.无效的充值方式, 0, "", 0);
			}
			try {
				GameConstants gameConstants = GameConstants.getInstance();
				if (ChargeManager.useNewChargeInterface) {
					if (cardId != null && !cardId.isEmpty() && savingReason != null && !savingReason.isEmpty() && newChannel != null && !newChannel.isEmpty()) {
						orderId = BossClientService.getInstance().savingForAppChinaUser(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), newChannel, os, new String[] { String.valueOf(player.getId()), cardId, savingReason });
					} else {
						orderId = BossClientService.getInstance().savingForAppChinaUser(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), userChannel, os, new String[] { String.valueOf(player.getId()) });
					}
				} else {
					orderId = BossClientService.getInstance().savingForAppChinaUser(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), userChannel, os);
				}
			} catch (Exception e) {
				result = 1;
				orderId = Translate.提交异常稍后再试;
				ChargeManager.logger.error(player.getLogString() + "[应用会充值,请求订单号] [异常] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]", e);
			}
			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[应用会充值,请求订单号] [成功] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "] [订单号:" + orderId + "] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			}
			return new YINGYONGHUI_GET_CHARGE_ORDER_RES(message.getSequnceNum(), result, orderId, chargeMoneyConfigure.getDenomination(), "10021700000002100217", 400009);
		} else if (message instanceof ORDER_STATUS_CHANGE_REQ) {
			ORDER_STATUS_CHANGE_REQ req = (ORDER_STATUS_CHANGE_REQ) message;
			BossClientService.getInstance().notifyOrderStatusChanged(username, req.getOrderId(), req.getChannel(), req.getStatus(), req.getStatusDesp());
		} else if (message instanceof LENOVO_GET_CHARGE_ORDER_REQ) {
			LENOVO_GET_CHARGE_ORDER_REQ req = (LENOVO_GET_CHARGE_ORDER_REQ) message;
			String channel = req.getChannel();
			String os = req.getOs();
			String orderId = null;
			int result = 0;
			String cardId = "";
			String savingReason = "";
			String userChannel = channel;
			String newChannel = "";
			if (userChannel != null && userChannel.contains("@@@@")) {
				try {
					String strs[] = userChannel.split("@@@@");
					if (strs.length != 3) {
						ChargeManager.logger.error(player.getLogString() + "[通用协议,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [userChannel:" + userChannel + "] [os:" + os + "]");
						return new LENOVO_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, "-1");
					}
					for (String s : strs) {
						if (s == null || s.isEmpty()) {
							ChargeManager.logger.error(player.getLogString() + "[通用协议,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [userChannel:" + userChannel + "] [os:" + os + "]");
							return new LENOVO_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, "-1");
						}
					}
					newChannel = strs[0];
					cardId = strs[1];
					savingReason = strs[2];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				if (ChargeManager.useNewChargeInterface) {
					if (cardId != null && !cardId.isEmpty() && savingReason != null && !savingReason.isEmpty() && newChannel != null && !newChannel.isEmpty()) {
						orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, "乐逗", "", "", 0, GameConstants.getInstance().getServerName(), newChannel, os, new String[] { String.valueOf(player.getId()), cardId, savingReason });
					} else {
						orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, "乐逗", "", "", 0, GameConstants.getInstance().getServerName(), channel, os, new String[] { String.valueOf(player.getId()) });
					}
				} else {
					orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, "乐逗", "", "", 0, GameConstants.getInstance().getServerName(), channel, os);

				}
			} catch (Exception e) {
				orderId = Translate.提交异常稍后再试;
				ChargeManager.logger.error(player.getLogString() + "[应用会充值,请求订单号] [失败] ", e);
				result = 1;
			}
			if (orderId == null || "".equals(orderId)) {
				orderId = Translate.提交异常稍后再试;
				result = 1;
			}
			return new LENOVO_GET_CHARGE_ORDER_RES(message.getSequnceNum(), result, orderId);
		} else if (message instanceof APPSTORE_SAVING_VERIFY_REQ) {
			APPSTORE_SAVING_VERIFY_REQ req = (APPSTORE_SAVING_VERIFY_REQ) message;

			String error = "";
			int result = 0;

			if (req.getChannel().indexOf("APPSTOREGUOJI") != -1) {
				// 国际版先去掉限制
			} else {
				if (player.getLevel() < 10 && player.isAppStoreChannel()) {
					result = 1;
					error = Translate.您的等级过低请提升等级至10级后再进行充值;
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, error);
					player.addMessageToRightBag(hint);
					return new APPSTORE_SAVING_VERIFY_RES(req.getSequnceNum(), result, error);
				}
				// 先服务器判断
				if (!AppStoreSavingLimit.levelCanSaving(player)) {
					result = 1;
					error = Translate.您已经到当前等级的充值上限请升级或者改日再充值;
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, error);
					player.addMessageToRightBag(hint);
					if (logger.isInfoEnabled()) {
						logger.info("[appstore充值校验] [结果:" + (result == 0 ? "成功" : "失败") + "] [error:" + error + "] [username:" + username + "] [code:" + req.getVerifyStr() + "] [当前客户端渠道:" + req.getChannel() + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
					}
					return new APPSTORE_SAVING_VERIFY_RES(req.getSequnceNum(), result, error);
				}

				// if (!AppStoreSavingLimit.uuidCanSaving(player, req.getVerifyStr())) {
				// result = 1;
				// error = Translate.此设备今日给过多的帐号充过值请改日再充值;
				// HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, error);
				// player.addMessageToRightBag(hint);
				// if (logger.isInfoEnabled()) {
				// logger.info("[appstore充值校验] [结果:" + (result == 0 ? "成功" : "失败") + "] [error:" + error + "] [username:" + username + "] [code:" + req.getVerifyStr() +
				// "] [当前客户端渠道:" + req.getChannel() + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
				// }
				// return new APPSTORE_SAVING_VERIFY_RES(req.getSequnceNum(), result, error);
				// }

				error = BossClientService.getInstance().appStoreSavingVerify(username, req.getChannel(), req.getVerifyStr());
				result = 0;
				if (error != null && error.length() > 0) {
					result = 1;
					HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, error);
					player.addMessageToRightBag(hint);
				}
			}

			if (logger.isInfoEnabled()) {
				logger.info("[appstore充值校验] [结果:" + (result == 0 ? "成功" : "失败") + "] [error:" + error + "] [username:" + username + "] [code:" + req.getVerifyStr() + "] [当前客户端渠道:" + req.getChannel() + "] [" + (System.currentTimeMillis() - startTime) + "ms]");
			}
			return new APPSTORE_SAVING_VERIFY_RES(req.getSequnceNum(), result, error);
		} else if (message instanceof UCSDK_NOTICE_CHARGE_REQ) {
			long start = SystemTime.currentTimeMillis();
			UCSDK_NOTICE_CHARGE_REQ req = (UCSDK_NOTICE_CHARGE_REQ) message;
			String channel = req.getChannel();
			String os = req.getOs();
			String orderId = req.getOrderId();
			String modifiedChannel = ChargeManager.getInstance().modifyChannelName(channel);
			String chargeMode = "";
			if ("UC_MIESHI".equalsIgnoreCase(modifiedChannel)) {
				chargeMode = "UCSDK充值";
			} else if ("SINAWEI_MIESHI".equalsIgnoreCase(modifiedChannel)) {
				chargeMode = "新浪微币";
			} else if ("UCSDK_MIESHI".equalsIgnoreCase(modifiedChannel)) {
				chargeMode = "UCSDK充值";
			}
			if ("".equals(chargeMode)) {
				ChargeManager.logger.error(player.getLogString() + "[充值回调] [充值方式空] [channel:" + channel + "] [os:" + os + "] [orderId:" + orderId + "] [returns:--] [耗时:" + (SystemTime.currentTimeMillis() - start) + "]");
				return null;
			}
			String returns = null;
			if (ChargeManager.useNewChargeInterface) {
				returns = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, chargeMode, orderId, "", 0, GameConstants.getInstance().getServerName(), channel, os, new String[] { String.valueOf(player.getId()) });
			} else {
				returns = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, chargeMode, orderId, "", 0, GameConstants.getInstance().getServerName(), channel, os);
			}

			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[" + chargeMode + "充值回调] [channel:" + channel + "] [os:" + os + "] [orderId:" + orderId + "] [returns:" + returns + "] [耗时:" + (SystemTime.currentTimeMillis() - start) + "]");
			}
			return new UCSDK_NOTICE_CHARGE_RES(req.getSequnceNum(), 0, returns);
			// ////////////////////////////////////////////
		} else if (message instanceof IAPPPAY_GET_CHARGE_ORDER_REQ) {

			long start = SystemTime.currentTimeMillis();
			IAPPPAY_GET_CHARGE_ORDER_REQ req = (IAPPPAY_GET_CHARGE_ORDER_REQ) message;
			String confId = req.getId();
			String modeName = req.getModeName();
			String os = req.getOs();
			String userChannel = req.getChannel();

			int result = 0;
			int goodsNum = 0;
			String orderId = "";
			String cardId = "";
			String savingReason = "";

			String newChannel = "";
			if (userChannel != null && userChannel.contains("@@@@")) {
				try {
					String strs[] = userChannel.split("@@@@");
					if (strs.length != 3) {
						ChargeManager.logger.error(player.getLogString() + "[爱贝充值,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
						return new IAPPPAY_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, Translate.无效的充值方式, 0, "", 0);
					}
					for (String s : strs) {
						if (s == null || s.isEmpty()) {
							ChargeManager.logger.error(player.getLogString() + "[爱贝充值,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
							return new IAPPPAY_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, Translate.无效的充值方式, 0, "", 0);
						}
					}
					newChannel = strs[0];
					cardId = strs[1];
					savingReason = strs[2];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ChargeManager chargeManager = ChargeManager.getInstance();
			List<ChargeMode> modeList = chargeManager.getChannelChargeModes(userChannel);
			if (cardId != null && !cardId.isEmpty() && savingReason != null && !savingReason.isEmpty() && newChannel != null && !newChannel.isEmpty()) {
				modeList = chargeManager.getChannelChargeModes(newChannel);
			}
			if (modeList == null || modeList.size() == 0) {
				ChargeManager.logger.error(player.getLogString() + "[爱贝充值,请求订单号] [失败] [无匹配的渠道充值] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new IAPPPAY_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, "无匹配的渠道充值", 0, "", 0);
			}
			ChargeMoneyConfigure chargeMoneyConfigure = chargeManager.getChargeMoneyConfigures().get(confId);
			if (chargeMoneyConfigure == null) {
				ChargeManager.logger.error(player.getLogString() + "[爱贝充值,请求订单号] [失败] [无效的充值面额] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new IAPPPAY_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, "无效的充值面额", 0, "", 0);
			}
			ChargeMode chargeMode = chargeMoneyConfigure.getChargeMode(modeName);
			if (chargeMode == null) {
				ChargeManager.logger.error(player.getLogString() + "[爱贝充值,请求订单号] [失败] [无效的充值方式] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]");
				return new IAPPPAY_GET_CHARGE_ORDER_RES(message.getSequnceNum(), 1, Translate.无效的充值方式, 0, "", 0);
			}
			try {
				GameConstants gameConstants = GameConstants.getInstance();
				if (ChargeManager.useNewChargeInterface) {
					if (cardId != null && !cardId.isEmpty() && savingReason != null && !savingReason.isEmpty() && newChannel != null && !newChannel.isEmpty()) {
						orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), newChannel, os, new String[] { String.valueOf(player.getId()), cardId, savingReason });
					} else {
						orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), userChannel, os, new String[] { String.valueOf(player.getId()) });
					}
				} else {
					orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), userChannel, os);
				}
			} catch (Exception e) {
				result = 1;
				orderId = Translate.通信异常请稍后再试;
				ChargeManager.logger.error(player.getLogString() + "[爱贝充值,请求订单号] [异常] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "]", e);
			}
			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[爱贝充值,请求订单号] [成功] [userName:" + player.getUsername() + "] [confId:" + confId + "] [goodsNum:" + goodsNum + "] [modeName:" + modeName + "] [订单号:" + orderId + "] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			}
			return new IAPPPAY_GET_CHARGE_ORDER_RES(message.getSequnceNum(), result, orderId, chargeMoneyConfigure.getDenomination(), "10004200000001100042", 1);

		} else if (message instanceof DEFAULT_GET_CHARGEORDER_REQ) {
			long start = SystemTime.currentTimeMillis();
			DEFAULT_GET_CHARGEORDER_REQ req = (DEFAULT_GET_CHARGEORDER_REQ) message;

			String userChannel = req.getChannel();
			String os = req.getOs();
			String modeName = req.getModeName();
			String confId = req.getId();

			int result = 0;
			String orderId = "";
			String cardId = "";
			String savingReason = "";

			if (userChannel != null && userChannel.contains("UNIPAYSDK_MIESHI")) {
				return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, "此渠道充值已关闭，请联系客服！");
			}

			String newChannel = "";
			if (userChannel != null && userChannel.contains("@@@@")) {
				try {
					String strs[] = userChannel.split("@@@@");
					if (strs.length != 3) {
						ChargeManager.logger.error(player.getLogString() + "[通用协议,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
						return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, Translate.无效的充值方式);
					}
					for (String s : strs) {
						if (s == null || s.isEmpty()) {
							ChargeManager.logger.error(player.getLogString() + "[通用协议,请求订单号] [失败] [特殊串拼接错误] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
							return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, Translate.无效的充值方式);
						}
					}
					newChannel = strs[0];
					cardId = strs[1];
					savingReason = strs[2];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			ChargeManager chargeManager = ChargeManager.getInstance();
			List<ChargeMode> modeList = chargeManager.getChannelChargeModes(userChannel);
			if (cardId != null && !cardId.isEmpty() && savingReason != null && !savingReason.isEmpty() && newChannel != null && !newChannel.isEmpty()) {
				modeList = chargeManager.getChannelChargeModes(newChannel);
			}
			if (modeList == null || modeList.size() == 0) {
				ChargeManager.logger.error(player.getLogString() + "[通用协议,请求订单号] [失败] [无匹配的渠道充值] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
				return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, "无匹配的渠道充值");
			}
			ChargeMoneyConfigure chargeMoneyConfigure = chargeManager.getChargeMoneyConfigures().get(confId);
			if (chargeMoneyConfigure == null) {
				ChargeManager.logger.error(player.getLogString() + "[通用协议,请求订单号] [失败] [无效的充值面额] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
				return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, "无效的充值面额");
			}

			ChargeMode chargeMode = chargeMoneyConfigure.getChargeMode(modeName);
			if (chargeMode == null) {
				ChargeManager.logger.error(player.getLogString() + "[通用协议,请求订单号] [失败] [无效的充值方式] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
				return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, Translate.无效的充值方式);
			}

			if (modeName.equals("KoreaT-STORE") || modeName.equals("KoreaKT")) {// 韩国的T-STORE充值。要提示用户是否要充值
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				mw.setTitle("Tstore");
				mw.setDescriptionInUUB("<f>" + chargeMoneyConfigure.getDescription() + "</f>\n<f> 하시겠습니까?</f>\n정보이용료 <f color='0xff0000'>" + chargeMoneyConfigure.getShowText() + "</f> 부과");
				Option_KoreaTstoreCharge commit_option = new Option_KoreaTstoreCharge(req);
				commit_option.setText(Translate.确定);
				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.取消);
				mw.setOptions(new Option[] { commit_option, cancel });

				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);
				return null;
			}

			try {
				GameConstants gameConstants = GameConstants.getInstance();
				if (ChargeManager.useNewChargeInterface) {
					if (cardId != null && !cardId.isEmpty() && savingReason != null && !savingReason.isEmpty() && newChannel != null && !newChannel.isEmpty()) {
						orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), newChannel, os, new String[] { String.valueOf(player.getId()), cardId, savingReason });
					} else {
						orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), userChannel, os, new String[] { String.valueOf(player.getId()) });
					}
				} else {
					orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, chargeMode.getModeName(), "", "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), userChannel, os);
				}
			} catch (Exception e) {
				result = 1;
				orderId = Translate.通信异常请稍后再试;
				ChargeManager.logger.error(player.getLogString() + "[通用协议,请求订单号] [异常] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]", e);
			}
			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[通用协议,请求订单号] [成功] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [订单号:" + orderId + "] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			}
			return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), result, chargeMoneyConfigure.getDenomination(), orderId);
		} else if (message instanceof GET_CHARGE_ORDER_MULTIIO_REQ) {
			long start = SystemTime.currentTimeMillis();
			GET_CHARGE_ORDER_MULTIIO_REQ req = (GET_CHARGE_ORDER_MULTIIO_REQ) message;
			String userChannel = req.getChannel();
			String os = req.getOs();
			String modeName = req.getModeName();
			String confId = req.getId();
			String functionName = req.getFuntionName();
			String[] muitiParms = req.getMuitiParms();

			if(functionName == null || functionName.isEmpty()){
				ChargeManager.logger.error(player.getLogString() + "[多参数,请求订单号] [失败] [无效的充值行为] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
				return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, Translate.无效的充值行为);
			}
			if(!functionName.equals("每日礼包") && !functionName.equals("购买月卡") && !functionName.equals("充值") ){
				ChargeManager.logger.error(player.getLogString() + "[多参数,请求订单号] [失败] [无效的充值行为2] [userName:" + player.getUsername() + "] [functionName:"+functionName+"] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "]");
				return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, Translate.无效的充值行为);
			}
			
			int result = 0;
			String orderId = "";

			ChargeManager chargeManager = ChargeManager.getInstance();
			List<ChargeMode> modeList = chargeManager.getChannelChargeModes(userChannel);
			if (modeList == null || modeList.size() == 0) {
				ChargeManager.logger.error(player.getLogString() + "[多参数,请求订单号] [失败] [无匹配的渠道充值] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [muitiParms:" + Arrays.toString(muitiParms) + "]");
				return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, "无匹配的渠道充值");
			}
			ChargeMoneyConfigure chargeMoneyConfigure = chargeManager.getChargeMoneyConfigures().get(confId);
			if (chargeMoneyConfigure == null) {
				ChargeManager.logger.error(player.getLogString() + "[多参数,请求订单号] [失败] [无效的充值面额] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [muitiParms:" + Arrays.toString(muitiParms) + "]");
				return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, "无效的充值面额");
			}
			
			String desc = chargeMoneyConfigure.getShort_des();
			if(functionName.equals("每日礼包")){
				if(confId.equals("xunxian_day1") || confId.equals("MQB1") || confId.equals("yxx1") || confId.equals("xunxianqingyuan.01") || confId.equals("meiri1") || confId.equals("qilemeiri1")){
					desc = "购买每日1元礼包";
				}else if(confId.equals("xunxian_day3") || confId.equals("MQB3") || confId.equals("yxx3") || confId.equals("xunxianqingyuan.03") || confId.equals("meiri3")|| confId.equals("qilemeiri3")){
					desc = "购买每日3元礼包";
				}else if(confId.equals("xunxian_day6") || confId.equals("MQB6") || confId.equals("yxx6")|| confId.equals("xunxianqingyuan.06") || confId.equals("meiri6")|| confId.equals("qilemeiri6")){
					desc = "购买每日6元礼包";
				}
			}else if(functionName.equals("购买月卡") ){
				desc = "购买月卡";
				if(confId.equals("app12") || confId.equals("MQB12") || confId.equals("yxx12")|| confId.equals("jiuzhouyueka12")|| confId.equals("qileyueka12")){
					desc = "购买修真卡";
				}else if(confId.equals("app18") || confId.equals("MQB18") || confId.equals("yxx18")|| confId.equals("jiuzhouyueka18")|| confId.equals("qileyueka18")){
					desc = "购买渡劫卡";
				}else if(confId.equals("app30") || confId.equals("MQB30") || confId.equals("yxx30")|| confId.equals("jiuzhouyueka30")|| confId.equals("qileyueka30")){
					desc = "购买飞仙卡";
				}
			}
//			ChargeMode chargeMode = chargeMoneyConfigure.getChargeMode(modeName);
//			if (chargeMode == null) {
//				ChargeManager.logger.error(player.getLogString() + "[多参数,请求订单号] [失败] [无效的充值方式] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [muitiParms:" + Arrays.toString(muitiParms) + "]");
//				return new DEFAULT_GET_CHARGEORDER_RES(message.getSequnceNum(), 1, 0, Translate.无效的充值方式);
//			}
			try {
//				String roleName = "";
//				String productID = "";
//				String productName = "";
//				String productDese = "";
				orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, modeName, "", "", (int) chargeMoneyConfigure.getDenomination(), GameConstants.getInstance().getServerName(), userChannel, os, new String[] { String.valueOf(player.getId()),confId,functionName ,player.getName(),modeName,desc});
			} catch (Exception e) {
				result = 1;
				orderId = Translate.通信异常请稍后再试;
				ChargeManager.logger.error(player.getLogString() + "[多参数,请求订单号] [异常] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [muitiParms:" + Arrays.toString(muitiParms) + "]", e);
			}
			
			if(userChannel != null){
				if(userChannel.toUpperCase().contains("NEWUCSDK") ||userChannel.toUpperCase().contains("NEWWDJSDK")){
					muitiParms = new String[] { ucWdjSavingUrl};
				}
				if(userChannel.toUpperCase().contains("OPPOSDK")){
					muitiParms = new String[] { oppoSavingUrl};
				}
				if(userChannel.toUpperCase().contains("U8SDK")){
					muitiParms = new String[] { u8SavingUrl};
				}
				if(userChannel.toUpperCase().contains("QILEAPPSTORE")){
					muitiParms = new String[] { qileSavingUrl};
				}
				if(userChannel.toUpperCase().contains("HUOGAMEAPPSTORE")){
					muitiParms = new String[] { huogameSavingUrl};
				}
				if(userChannel.toUpperCase().contains("HUOGAME2APPSTORE")){
					muitiParms = new String[] { huogameSavingUrl2};
				}
			}
			String oldOrderId = orderId;
			if(orderId.contains("####")){
				muitiParms = orderId.split("####");
				orderId = muitiParms[0];
				if(userChannel.toUpperCase().contains("HUASHENGSDK_XUNXIAN")){
					orderId = muitiParms[1];
					muitiParms = new String[] { "",muitiParms[2]};
				}
			}

			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + "[多参数,请求订单号] [成功] [userName:" + player.getUsername() + "] [muitiParms:"+(muitiParms!=null?Arrays.toString(muitiParms):"")+"] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [订单号:" + orderId + "] [oldOrderId:"+oldOrderId+"] [savingReason:"+functionName+"] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			}
			return new GET_CHARGE_ORDER_MULTIIO_RES(message.getSequnceNum(), result,desc, chargeMoneyConfigure.getDenomination(), orderId, muitiParms);

		} else if (message instanceof QUERY_VIP_DISPLAY_REQ) {
			return handle_QUERY_VIP_DISPLAY_REQ(player, message);
		} else if (message instanceof ARTICLEPROTECT_UNBLOCK_REQ) {
			ArticleProtectManager.instance.handle_ARTICLEPROTECT_UNBLOCK_REQ(player, (ARTICLEPROTECT_UNBLOCK_REQ) message);
			return null;
		} else if (message instanceof ARTICLEPROTECT_BLOCK_REQ) {
//			ArticleProtectManager.instance.handle_ARTICLEPROTECT_BLOCK_REQ(player, (ARTICLEPROTECT_BLOCK_REQ) message);
			ARTICLEPROTECT_BLOCK_REQ req = (ARTICLEPROTECT_BLOCK_REQ) message;
			ArticleProtectManager.instance.option_block(player, req.getEntityID(), req.getEntityIndex(), req.getPropID(), req.getPropIndex());
			return null;
		} else if (message instanceof ARTICLEPROTECT_OTHERMSG_REQ) {
			ArticleProtectManager.instance.send_ARTICLEPROTECT_OTHERMSG_RES(player);
			return null;
		} else if (message instanceof ANDROID_PROCESS_REQ) {
			handle_ANDROID_PROCESS_REQ(conn, player, message);
		} else if (message instanceof GET_RMB_REWARDMSG_REQ) {
			return NewChongZhiActivityManager.instance.handle_GET_RMB_REWARDMSG_REQ((GET_RMB_REWARDMSG_REQ) message, player);
		} else if (message instanceof GET_RMBREWARD_REQ) {
			return NewChongZhiActivityManager.instance.handle_GET_RMBREWARD_REQ((GET_RMBREWARD_REQ) message, player);
		} else if (message instanceof GET_WEEKANDMONTH_INFO_REQ) {
			return NewChongZhiActivityManager.instance.handle_GET_WEEKANDMONTH_INFO_REQ((GET_WEEKANDMONTH_INFO_REQ) message, player);
		} else if (message instanceof GET_WEEKMONTH_REWARD_REQ) {
			return NewChongZhiActivityManager.instance.handle_GET_WEEKMONTH_REWARD_REQ((GET_WEEKMONTH_REWARD_REQ) message, player);
		} else if (message instanceof GET_WEEKACTIVITY_REQ) {
			return NewChongZhiActivityManager.instance.handle_GET_WEEKACTIVITY_REQ((GET_WEEKACTIVITY_REQ) message, player);
		} else if (message instanceof GET_WEEK_REWARD_REQ) {
			return NewChongZhiActivityManager.instance.handle_GET_WEEK_REWARD_REQ((GET_WEEK_REWARD_REQ) message, player);
		} else if (message instanceof TRY_BING_PHONENUM_REQ) {
			PhoneNumMananger.getInstance().handle_TRY_BING_PHONENUM_REQ(player);
			return null;
		} else if (message instanceof TRY_SEND_PHONTNUM_REQ) {
			PhoneNumMananger.getInstance().handle_TRY_SEND_PHONTNUM_REQ(player, ((TRY_SEND_PHONTNUM_REQ) message).getPhoneNum());
			return null;
		} else if (message instanceof REFRESH_SHOP_GOODS_REQ) {
			try {
				REFRESH_SHOP_GOODS_REQ req = (REFRESH_SHOP_GOODS_REQ) message;
				boolean flag = false;
				int index = 0;
				for (int i = 0; i < ShopManager.可以刷新物品的商店.length; i++) {
					if (req.getShopName().trim().equals(ShopManager.可以刷新物品的商店[i])) {
						flag = true;
						index = i;
						break;
					}
				}
				if (flag) {
					ShopManager shopManager = ShopManager.getInstance();
					Shop shop = shopManager.getShop(req.getShopName().trim(), player);
					Article a = ArticleManager.getInstance().getArticleByCNname(ShopManager.刷新商店物品消耗物品统计名[index]);
					int count = player.countArticleInKnapsacksByName(a.getName());
					if (count < ShopManager.刷新商店物品消耗物品数量[index]) {
						player.sendError(Translate.text_feed_silkworm_006);
						return null;
					}
					for (int i = 0; i < ShopManager.刷新商店物品消耗物品数量[index]; i++) {
						if (!player.removeArticle(a.getName(), "刷新商店物品")) {
							ShopManager.logger.error("[商店] [刷新商店物品] [失败] [删除物品不成功] [" + player.getLogString() + "] [articleName:" + a.getName() + "] [已删除个数：" + i + "]");
							player.sendError(Translate.删除物品失败);
							return null;
						}
					}
					shop.flushTempGoods(player);
					Goods gs[] = shop.getGoods(player);
					if (shop.getTempGoods() != null && shop.getTempGoods().size() > 0) {
						gs = shop.gettempGoods(player);
					}
					com.fy.engineserver.shop.client.Goods[] cGoods = ShopManager.translate(player, shop, gs);
					SHOP_OTHER_INFO_RES shopOtherRes = new SHOP_OTHER_INFO_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, req.getShopName(), shop.shopType, cGoods);
					player.addMessageToRightBag(shopOtherRes);
				} else {
					player.sendError(Translate.商店不可刷新);
				}
			} catch (Exception e) {
				ShopManager.logger.error("[商店] [刷新商店物品] [异常] [" + player.getLogString() + "]", e);
			}
			return null;
		} else if (message instanceof CAN_REFRESH_SHOP_NAME_REQ) {
			CAN_REFRESH_SHOP_NAME_RES resp = new CAN_REFRESH_SHOP_NAME_RES(message.getSequnceNum(), ShopManager.可以刷新物品的商店);
			return resp;
		} else if (message instanceof BING_PHONENUM_REQ) {
			BING_PHONENUM_REQ req = (BING_PHONENUM_REQ) message;
			PhoneNumMananger.getInstance().handle_BING_PHONENUM_REQ(player, req.getPhoneNum(), req.getJiaoYanMa());
			return null;
		} else if (message instanceof TRY_UNBING_PHONENUM_REQ) {
			PhoneNumMananger.getInstance().handle_TRY_UNBING_PHONENUM_REQ(player);
			return null;
		} else if (message instanceof TRY_SEND_UNBIND_REQ) {
			PhoneNumMananger.getInstance().handle_TRY_SEND_UNBIND_REQ(player);
			return null;
		} else if (message instanceof UNBIND_PHONENUM_REQ) {
			UNBIND_PHONENUM_REQ req = (UNBIND_PHONENUM_REQ) message;
			PhoneNumMananger.getInstance().handle_UNBIND_PHONENUM_REQ(player, req.getJiaoYanMa());
			return null;
		} else if (message instanceof QUERY_TITLE_PRODUCE_REQ) {
			QUERY_TITLE_PRODUCE_REQ req = (QUERY_TITLE_PRODUCE_REQ) message;
			String articleCNNName = PlayerTitlesManager.getInstance().getArticleCNNnameByTitleName(req.getTitleName());
			Article article = null;
			if (articleCNNName == null || articleCNNName.equals("")) {
				article = ArticleManager.getInstance().getArticle(req.getTitleName());
			} else {
				article = ArticleManager.getInstance().getArticleByCNname(articleCNNName);
			}
			String desp = "";
			if (article != null) {
				desp = article.getGetMethod();
			}
			QUERY_TITLE_PRODUCE_RES resp = new QUERY_TITLE_PRODUCE_RES(message.getSequnceNum(), req.getTitleName(), desp);
			if (SocialManager.logger.isDebugEnabled()) {
				SocialManager.logger.debug("[QUERY_TITLE_PRODUCE_REQ] [请求称号产出] [" + player.getLogString() + "] [产出方式:" + resp.getDesp() + "] [物品:" + article + "] [req.gettitleName():" + req.getTitleName() + "]");
			}
			return resp;
		} else if (message instanceof PLAYER_TITLES_List_REQ) {
			// List<PlayerTitle> playerTitles = player.getPlayerTitles();
			// Honor[] honors = null;
			// if (playerTitles != null && playerTitles.size() > 0) {
			// honors = new Honor[playerTitles.size()];
			// for (int i = 0; i < honors.length; i++) {
			// if (playerTitles.get(i).getColor() <= 0) {
			// int color = PlayerTitlesManager.getInstance().getColorByTitleType(playerTitles.get(i).getTitleType());
			// String buffName = PlayerTitlesManager.getInstance().getBuffNameByType(playerTitles.get(i).getTitleType());
			// int buffLevel = PlayerTitlesManager.getInstance().getBuffLevlByType(playerTitles.get(i).getTitleType());
			// String titleShowName = PlayerTitlesManager.getInstance().getTitleShowNameByType(playerTitles.get(i).getTitleType());
			// String description = PlayerTitlesManager.getInstance().getDescriptionByType(playerTitles.get(i).getTitleType());
			// String icon = PlayerTitlesManager.getInstance().getIconByType(playerTitles.get(i).getTitleType());
			// long lastTime = PlayerTitlesManager.getInstance().getLastTimeByType(playerTitles.get(i).getTitleType());
			// playerTitles.get(i).setColor(color);
			// playerTitles.get(i).setBuffName(buffName);
			// playerTitles.get(i).setBuffLevl(buffLevel);
			// playerTitles.get(i).setTitleShowName(titleShowName);
			// playerTitles.get(i).setDescription(description);
			// playerTitles.get(i).setIcon(icon);
			// playerTitles.get(i).setLastTime(lastTime);
			// }
			// honors[i] = new Honor();
			// honors[i].setName(playerTitles.get(i).getTitleName());
			// honors[i].setIconId(playerTitles.get(i).getIcon());
			// honors[i].setDesp(playerTitles.get(i).getDescription());
			// honors[i].setId(playerTitles.get(i).getTitleType());
			// honors[i].setColor(playerTitles.get(i).getColor());
			// if (playerTitles.get(i).getLastTime() <= 0) {
			// honors[i].setExpirationTime(-1); // 目前没有时限称号，先写死
			// } else {
			// honors[i].setExpirationTime(playerTitles.get(i).getLastTime() + playerTitles.get(i).getStartTime());
			// }
			// }
			// } else {
			// honors = new Honor[0];
			// }
			PLAYER_TITLES_List_RES resp = handlePlayerTitle(player, message);
			return resp;
		} else if (message instanceof PHONENUM_ASK_REQ) {
			PHONENUM_ASK_REQ req = (PHONENUM_ASK_REQ) message;
			PhoneNumMananger.getInstance().handle_PHONENUM_ASK_REQ(player, req.getPhoneNum(), req.getJiaoYanMa());
			return null;
		} else if (message instanceof SOUL_MESSAGE_REQ) {
			SOUL_MESSAGE_REQ req = (SOUL_MESSAGE_REQ) message;
			// boolean isappserver = false;
			// String servername = GameConstants.getInstance().getServerName();
			// for(String name : ActivityManagers.getInstance().limitSoulServers){
			// if(name.equals(servername)){
			// isappserver = true;
			// break;
			// }
			// }
			if (!ActivityManagers.getInstance().isOpenSoul) {// && isappserver){
				player.sendError("元神系统升级中.");
				return null;
			}
			return player.getSoulMessRes(req.getPlayerid());
		} else if (message instanceof SOUL_UPGRADE_REQ) {
			int currGrade = player.getUpgradeNums();
			if (currGrade >= player.getUpgradeExp().length) {
				CoreSubSystem.logger.warn("[元神升级失败] [升级次数:" + currGrade + "] [" + player.getLogString() + "]");
				return null;
			}
			String currCostExp = player.getUpgradeExp()[currGrade];
			String checkMess = player.upgradeCheak(Long.parseLong(currCostExp));
			if (checkMess != null && checkMess.equals("ok")) {
				if (player.upgradeProperty(Long.parseLong(currCostExp))) {
					player.sendError(Translate.恭喜您属性升级成功);
				} else {
					player.sendError(Translate.属性升级失败);
				}
			} else {
				player.sendError(checkMess);
				CoreSubSystem.logger.warn("[元神升级失败] [可能是外挂] [升级次数:" + currGrade + "] [所需经验:" + currCostExp + "] [原因:" + checkMess + "] [" + player.getLogString() + "]");
			}
		} else if (message instanceof LOGIN_ACTIVITY_GET_REQ) {
			LOGIN_ACTIVITY_GET_REQ req = (LOGIN_ACTIVITY_GET_REQ) message;
			int currReward = req.getArticleIndex();
			com.fy.engineserver.activity.loginActivity.ActivityManagers manager = com.fy.engineserver.activity.loginActivity.ActivityManagers.getInstance();
			manager.doPrizes(player, currReward);
		} else if (message instanceof NEW_OPTION_SELECT_REQ) {
			NEW_OPTION_SELECT_REQ req = (NEW_OPTION_SELECT_REQ) message;
			VipInfoRecordManager.getInstance().handle_NEW_OPTION_SELECT_REQ(req, player, conn);
			Record4OrderManager.getInstance().handle_NEW_OPTION_SELECT_REQ(req, player);
		} else if (message instanceof TRY_CLIENT_MSG_A_REQ) {
			TRY_CLIENT_MSG_A_REQ req = (TRY_CLIENT_MSG_A_REQ) message;
			EnterLimitManager.logger.warn("[收到Android特殊协议] [" + Arrays.toString(req.getMsgs()) + "]");
		} else if (message instanceof TRY_CLIENT_MSG_I_REQ) {
			TRY_CLIENT_MSG_I_REQ req = (TRY_CLIENT_MSG_I_REQ) message;
			EnterLimitManager.logger.warn("[收到IOS特殊协议] [" + Arrays.toString(req.getMsgs()) + "]");
		} else if (message instanceof TRY_CLIENT_MSG_W_REQ) {
			TRY_CLIENT_MSG_W_REQ req = (TRY_CLIENT_MSG_W_REQ) message;
			EnterLimitManager.logger.warn("[收到Win8特殊协议] [" + Arrays.toString(req.getMsgs()) + "]");
		} else if (message instanceof CLIENT_MSG_A_REQ) {
			CLIENT_MSG_A_REQ req = (CLIENT_MSG_A_REQ) message;
			EnterLimitManager.logger.warn("[收到Android特殊协议] [" + Arrays.toString(req.getClientMsgs()) + "]");
		} else if (message instanceof CLIENT_MSG_I_REQ) {
			CLIENT_MSG_I_REQ req = (CLIENT_MSG_I_REQ) message;
			EnterLimitManager.logger.warn("[收到IOS特殊协议] [" + Arrays.toString(req.getClientMsgs()) + "]");
		} else if (message instanceof CLIENT_MSG_W_REQ) {
			CLIENT_MSG_W_REQ req = (CLIENT_MSG_W_REQ) message;
			EnterLimitManager.logger.warn("[收到Win8特殊协议] [" + Arrays.toString(req.getClientMsgs()) + "]");
		} else if (message instanceof GET_SHOP_REQ) {
			处理新的商城(message, player);
		} else if (message instanceof GET_NEW_SHOP_REQ) {
			处理最新的商城(message, player);
		} else if (message instanceof ACTIVITY_EXTEND_REQ) {
			ActivityManagers.getInstance().handle_ACTIVITY_EXTEND_REQ(message, player);
		} else if (message instanceof GET_NEWPLAYERCAREERS_REQ) {
			GET_NEWPLAYERCAREERS_REQ req = (GET_NEWPLAYERCAREERS_REQ) message;
			GET_NEWPLAYERCAREERS_RES resp = new GET_NEWPLAYERCAREERS_RES(message.getSequnceNum(), req.getRequestType(), CareerManager.newCareerFlag);
			return resp;
		} else if (message instanceof GET_SHOUKUI_COMMENSKILLS_REQ) {
			GET_SHOUKUI_COMMENSKILLS_REQ req = (GET_SHOUKUI_COMMENSKILLS_REQ) message;
			GET_SHOUKUI_COMMENSKILLS_RES resp = new GET_SHOUKUI_COMMENSKILLS_RES(message.getSequnceNum(), req.getRequestType(), CareerManager.shoukuiCommenSkillIds);
			return resp;
		} else if (message instanceof QUERY_WORLD__XJ_MAP_REQ) {
			处理仙界世界地图(message, player);
		} else if (message instanceof IS_XJ_MAP_REQ) {
			IS_XJ_MAP_RES res = new IS_XJ_MAP_RES(message.getSequnceNum(), WorldMapManager.getInstance().是否是仙界地图(player));
			player.addMessageToRightBag(res);
		} else if (message instanceof GET_SOME4ANDROID_REQ) {
			GET_SOME4ANDROID_REQ req = (GET_SOME4ANDROID_REQ) message;
			AndroidMsgManager.getInstance().handle_GET_SOME4ANDROID_REQ(req, player);
		} else if (message instanceof GET_SOME4ANDROID_1_REQ) {
			GET_SOME4ANDROID_1_REQ req = (GET_SOME4ANDROID_1_REQ) message;
			AndroidMsgManager.getInstance().handle_GET_SOME4ANDROID_1_REQ(req, player);
		} else if (message instanceof FUNCTION_OPEN_REQ) {
			FUNCTION_OPEN_REQ req = (FUNCTION_OPEN_REQ) message;
			int functionId = req.getFunctionId();
			int stat = 0;
			switch (functionId) {
			case 0:
				stat = qifuOpen;
				break;

			default:
				break;
			}
			return new FUNCTION_OPEN_RES(message.getSequnceNum(), functionId, stat);
		} else if (message instanceof QUERY_XINFA_SKILL_IDLIST_REQ) {
			return CareerManager.getInstance().handle_QUERY_XINFA_SKILL_IDLIST_REQ((QUERY_XINFA_SKILL_IDLIST_REQ) message, player);
		} else if (message instanceof ARTICLE_INFO_REQ) {// 查询物品模板信息
			ARTICLE_INFO_REQ req = (ARTICLE_INFO_REQ) message;
			int[] articleColorTypes = req.getArticleColorType();
			String[] articleNames = req.getArticleName();
			if (articleColorTypes.length > 0 && articleColorTypes.length == articleNames.length) {
				List<String> infos = new ArrayList<String>();
				for (int i = 0; i < articleNames.length; i++) {
					String aName = articleNames[i];
					Article article = ArticleManager.getInstance().getArticle(aName);
					if (article != null) {
						infos.add(article.getInfoShowHead(player, articleColorTypes[i]) + article.getInfoShowBody(player));
					} else {
						infos.add("");
						if (ArticleManager.logger.isWarnEnabled()) {
							ArticleManager.logger.warn(player.getLogString() + "[角色查询物品-物品不存在] [articleName:" + aName + "]");
						}
					}
				}
			} else {
				player.sendError("ERROR-css01");
				return null;
			}
		} else if (message instanceof GET_VIP_NOTICE_INFO_REQ) {
			try {
				GET_VIP_NOTICE_INFO_REQ req = (GET_VIP_NOTICE_INFO_REQ) message;
				List<VipHelpMess> vipMesses = ActivityManagers.getInstance().vipMesses;
				VipHelpMess m = null;

				for (VipHelpMess mess : vipMesses) {
					if (mess.viplevel == player.getVipLevel()) {
						m = mess;
						break;
					}
				}
				if (m != null) {
					GET_VIP_NOTICE_INFO_RES res = new GET_VIP_NOTICE_INFO_RES(req.getSequnceNum(), m.title, m.content);
					player.addMessageToRightBag(res);
				}
				logger.warn("[GET_VIP_NOTICE_INFO_REQ] [viplevel:" + player.getVipLevel() + "] [m:" + (m == null ? "null" : m) + "] [player:" + player.getName() + "]");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (message instanceof JUBAO_PLAYER_REQ) {
			ChatMessageService.getInstance().handle_JUBAO_PLAYER_REQ(message, player);
		} else if (message instanceof XILIAN_PAGE_REQ) {
			return ArticleManager.getInstance().queryXiLianPage(message, player);
		} else if (message instanceof XILIAN_CONFIRM_REQ) {
			return ArticleManager.getInstance().handleXILIAN_CONFIRM_REQ(message, player);
		} else if (message instanceof XILIAN_PUT_REQ) {
			return ArticleManager.getInstance().handleXILIAN_PUT_REQ(message, player);
		} else if (message instanceof XILIAN_REMOVE_REQ) {
			return ArticleManager.getInstance().handleXILIAN_REMOVE_REQ(message, player);
		} else if (message instanceof MORE_ARGS_ORDER_STATUS_CHANGE_REQ) {
			handle_MORE_ARGS_ORDER_STATUS_CHANGE_REQ(passport, player, message);
		} else if(message instanceof QUERY_PLAYER_CREATE_TIME_REQ){
			QUERY_PLAYER_CREATE_TIME_REQ req = (QUERY_PLAYER_CREATE_TIME_REQ)message;
			long pid = req.getPlayerId();
			if(pid == player.getId()){
				return new QUERY_PLAYER_CREATE_TIME_RES(req.getSequnceNum(), pid,player.getCreateTime());
			}else{
				if(PlayerManager.getInstance().isOnline(pid)){
					Player p2 = PlayerManager.getInstance().getPlayer(pid);
					return new QUERY_PLAYER_CREATE_TIME_RES(req.getSequnceNum(), pid,p2.getCreateTime());
				}
			}
		}else if(message instanceof MONTH_CARD_BUY_REQ){
			MONTH_CARD_BUY_REQ req = (MONTH_CARD_BUY_REQ)message;
			//ChargeManager.getInstance().handle_MONTH_CARD_BUY_REQ(req,player);
		}else if(message instanceof MONTH_CARD_FIRST_PAGE_REQ){
			MONTH_CARD_FIRST_PAGE_REQ req = (MONTH_CARD_FIRST_PAGE_REQ)message;
			ChargeManager.getInstance().handle_MONTH_CARD_FIRST_PAGE_REQ(req,player);
		}else if(message instanceof LOGIN_REWARD_REQ){
			LOGIN_REWARD_REQ req = (LOGIN_REWARD_REQ)message;
			ChargeManager.getInstance().handle_LOGIN_REWARD_REQ(req,player);
		}else if(message instanceof GET_LOGIN_REWARD_REQ){
			GET_LOGIN_REWARD_REQ req = (GET_LOGIN_REWARD_REQ)message;
			ChargeManager.getInstance().handle_GET_LOGIN_REWARD_REQ(req,player);
		}else if(message instanceof FIRST_CHARGE_STATE_REQ){
			FIRST_CHARGE_STATE_REQ req = (FIRST_CHARGE_STATE_REQ)message;
			ChargeManager.getInstance().handle_FIRST_CHARGE_STATE_REQ(req,player);
		}else if(message instanceof FIRST_CHARGE_REQ){
			FIRST_CHARGE_REQ req = (FIRST_CHARGE_REQ)message;
			ChargeManager.getInstance().handle_FIRST_CHARGE_REQ(req,player);
		}else if(message instanceof DAY_PACKAGE_OF_RMB_REQ){
			DAY_PACKAGE_OF_RMB_REQ req = (DAY_PACKAGE_OF_RMB_REQ)message;
			ChargeManager.getInstance().handle_DAY_PACKAGE_OF_RMB_REQ(req,player);
		}else if(message instanceof STAR_GO_REQ){
			STAR_GO_REQ req = (STAR_GO_REQ)message;
			ArticleManager.getInstance().moveStar(req.getRemoveId(),req.getAddId(),player);
		}else if(message instanceof STAR_MONEY_REQ){
			ArticleManager.getInstance().queryStarMoney((STAR_MONEY_REQ)message,player);
		}else if(message instanceof STAR_HELP_REQ){
			ArticleManager.getInstance().queryStarPage((STAR_HELP_REQ)message,player);
		}else {
			logger.warn("[interesting-message-but-no-handler] [" + message.getTypeDescription() + "]");
		}
		return null;
	}

	public void handle_MORE_ARGS_ORDER_STATUS_CHANGE_REQ(Passport passport, Player player, RequestMessage message) {

		MORE_ARGS_ORDER_STATUS_CHANGE_REQ req = (MORE_ARGS_ORDER_STATUS_CHANGE_REQ) message;
		try {
			// msdk充值，msdk兑换
			if (req.getArgs() != null) {
				String newArgs[] = new String[req.getArgs().length + 2];
				newArgs[0] = passport.getUserName();
				newArgs[1] = TENCENT_TEST_MODE_SWITCH + "";
				System.arraycopy(req.getArgs(), 0, newArgs, 2, req.getArgs().length);
				String result = BossClientService.getInstance().notifyOrderStatusChanged(player.getUsername(), req.getOrderId(), req.getChannel(), req.getStatus(), req.getStatusDesp(), req.getZoneid(), req.getPfkey(), req.getP_token(), newArgs);
				ChargeManager.logger.warn("[MORE_ARGS_ORDER_STATUS_CHANGE_REQ] [result:" + result + "] [newArgs:" + newArgs.length + "] [" + Arrays.toString(newArgs) + "] [" + player.getName() + "] [orderid:" + req.getOrderId() + "] [channel:" + req.getChannel() + "] [pfkey:" + req.getPfkey() + "] [pftoken:" + req.getP_token() + "] [" + req.getStatus() + "]");

				if (result != null && !result.isEmpty()) {
					String strs[] = result.split("####");
					if (strs.length >= 2) {
						if (strs[0].equals("MSDK充值ok") || strs[0].equals("MSDK兑换ok") || strs[0].equals("米大师兑换ok") || strs[0].equals("米大师充值ok")) {
							// OrderForm order = OrderFormManager.getInstance().getOrderForm(req.getOrderId());
							String openkey = strs[2];
							String pf = strs[3];
							String pay_token = strs[4];
							String zondid = strs[5];
							String pfkey = strs[6];
							String channel = strs[6];
							MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
							String des = Translate.translateString(Translate.用xx腾讯币兑换xx银两, new String[][] { { Translate.COUNT_1, strs[1] }, { Translate.COUNT_2, Long.parseLong(strs[1]) * 50 + "" } });
							mw.setDescriptionInUUB(des);
							Option_Exchange_Tencent_Silver beer = new Option_Exchange_Tencent_Silver(req.getOrderId(), Integer.parseInt(strs[1]));
							beer.setText(Translate.确定);
							beer.setChargeType(strs[0].split("ok")[0]);

							beer.setArgs(openkey, pf, pay_token, zondid, pfkey, channel, req.getStatus(), req.getStatusDesp());

							Option_Cancel cancle = new Option_Cancel();
							cancle.setText(Translate.取消);
							mw.setOptions(new Option[] { beer, cancle });
							QUERY_WINDOW_RES res1 = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
							player.addMessageToRightBag(res1);
							if (openNotice) {
								player.sendError(Translate.用腾讯币兑换银两);
							}
							logger.warn("[接收充值通知] [MSDK通知玩家兑换游戏币] [成功] [result:" + result + "][" + strs[0].split("ok")[0] + "]  [" + player.getLogString() + "] [orderid:" + req.getOrderId() + "]");
							return;
						} else if (strs[0].contains("error")) {
							player.sendError(strs[1]);
						}
					}
				}
				logger.warn("[接收充值通知] [MSDK通知玩家兑换游戏币] [失败] [result:" + result + "] [" + player.getLogString() + "] [orderid:" + req.getOrderId() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通知玩家更新订单状态] [MORE_ARGS_ORDER_STATUS_CHANGE_REQ] [" + player.getLogString() + "] [异常:" + e + "]");
		}

	}

	public ENTER_GAME_RES handle_ENTER_GAME_REQ(Connection conn, Player player, RequestMessage message, String username, long startTime) throws InterruptedException {
		if (player.getUsername().equals("wtx062") && GameConstants.getInstance().getServerName().equals("客户端测试")) {
			try {
				String stacktrace = StringUtil.getStackTrace(Thread.currentThread().getStackTrace());
				GamePlayerManager.logger.warn("[TESTENTER] [core...handle_ENTER_GAME_REQ] [name:"+player.getName()+"\n] "+stacktrace);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TODO
		ENTER_GAME_REQ req = (ENTER_GAME_REQ) message;
		String gameName = req.getGame();

		TransportData tdata = player.getTransportData();
		Game.logger.warn("[testsss] [gameName:" + gameName + "] [player:" + player.getName() + "] [transferGameCountry:"+player.transferGameCountry+"] [playerCountry:"+player.getCountry()+"] [gameCountry:"+player.getCurrentGameCountry()+"] [tdata:" + (tdata!=null?tdata.getTargetMapName():"null") + "]");
		if (tdata != null) {
			try {
				String mapName = tdata.getTargetMapName();
				if(mapName == null || mapName.isEmpty()){
					int cu = player.getCurrentGameCountry();
					if(player.getCurrentGameCountry() == 0){
						cu = player.getCountry();
					}
					
					if(cu == 1){
						mapName = tdata.getTargetMapName1();
					}else if(cu == 2){
						mapName = tdata.getTargetMapName2();
						
					}else if(cu == 3){
						mapName = tdata.getTargetMapName3();
						
					}
				}
				
//				if (!mapName.equals(gameName)) {
//					gameName = mapName;
//					Integer num = EnterLimitManager.playerEnterErrorMapNums.get(player.getId());
//					if (num == null) {
//						num = new Integer(0);
//					}
//					num = num + 1;
//					EnterLimitManager.playerEnterErrorMapNums.put(player.getId(), num);
//					if (Game.logger.isWarnEnabled()) {
//						Game.logger.warn("[地图不匹配] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getLogString(), mapName, req.getGame(), player.getGame(), num });
//					}
//					if (num % 2 == 0) {
//						DENY_USER_REQ denyreq = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", player.getUsername(), Translate.客户端修改地图文件名字, GameConstants.getInstance().getServerName() + Translate.客户端修改地图文件名字, false, true, false, 0, 10 * (num / 2));
//						MieshiGatewayClientService.getInstance().sendMessageToGateway(denyreq);
//						conn.close();
//						return null;
//					} else {
//						player.sendError("您擅自修改了客户端的地图，马上改回来，将要被封号。");
//					}
//				}
			} catch (Exception e) {
				Game.logger.error("", e);
			}
		} 
		
//		else {
//			if (gameName.equals(SeptStationManager.jiazuMapName) || gameName.indexOf("_jz_") > 0 || gameName.indexOf("_jy_") > 0 || gameName.indexOf("_pss_") > 0 || PetDaoManager.getInstance().isPetDao(gameName) || gameName.equals("zhongshenyiji")
//					|| BossCityManager.getInstance().isBossCityGame(gameName) || gameName.equals("wushenyiji") || gameName.equals("kunhuagucheng")|| XianLingChallengeManager.mapNames.contains(gameName)) {
//
//			} else {
//				if (!player.getMapName().equals(gameName)) {
//					Integer num = EnterLimitManager.playerEnterErrorMapNums.get(player.getId());
//					if (num == null) {
//						num = new Integer(0);
//					}
//					num = num + 1;
//					EnterLimitManager.playerEnterErrorMapNums.put(player.getId(), num);
//					Game.logger.warn("[地图不匹配2] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getLogString(), player.getMapName(), gameName, player.getGame(), num });
//					if (num % 2 == 0) {
//						DENY_USER_REQ denyreq = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", player.getUsername(), Translate.客户端修改地图文件名字, GameConstants.getInstance().getServerName() + Translate.客户端修改地图文件名字, false, true, false, 0, 10 * (num / 2));
//						MieshiGatewayClientService.getInstance().sendMessageToGateway(denyreq);
//						conn.close();
//						return null;
//					} else {
//						player.sendError("您擅自修改了客户端的地图，马上改回来，将要被封号。");
//					}
//					gameName = player.getMapName();
//				}
//			}
//		}

		List<Buff> buffs = player.getAllBuffs();
		if (buffs != null) {
			for (Buff buff : buffs) {
				if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName()) && buff.getInvalidTime() > 0) {
					gameName = "jianyu";
					player.transferGameCountry = 0;
					break;
				}
			}
		}

		GameManager gm = GameManager.getInstance();
		// 上面为老但修改

		ENTER_GAME_RES res = null;
		long playerId = req.getPlayer();

		// 判断请求的地图名和当前玩家所在地图是否一致
		boolean jumpFlag = false;
		if (gameName != null && !gameName.equals(player.getGame())) {
			jumpFlag = true;
		}

		if (player != null) {
			try {
				if (ActivityManager.getInstance().isNoticeChargePage(player)) {
					ActivityManager.getInstance().handleChargePage(player);
				}
			} catch (Exception e) {
				e.printStackTrace();
				ActivitySubSystem.logger.warn("[通知玩家充值获取礼包界面] [异常] [玩家:{}] [{}]", new Object[] { player.getLogString(), e });
			}
			player.takeOneTaskForCareer();
			player.noticeShouKuiInfo("上线");
			TalentData data = FlyTalentManager.getInstance().getTalentData(player.getId());
			if (data != null && data.getXyButtonState() == 1) {
				long overTime = data.getCdEndTime() - System.currentTimeMillis();
				if (overTime < 0) {
					overTime = 0;
				}
				if (data.getNbEndTimes() > System.currentTimeMillis()) {
					player.flyState = 1;
				}
				// 已过时间
				long hasCostTime = FlyTalentManager.TALENT_SKILL_CD_TIME - data.getMinusCDTimes() - overTime;
				NOTICE_TALENT_BUTTON_RES noticeRes = new NOTICE_TALENT_BUTTON_RES(GameMessageFactory.nextSequnceNum(), 1, hasCostTime, FlyTalentManager.TALENT_SKILL_CD_TIME - data.getMinusCDTimes());
				player.addMessageToRightBag(noticeRes);
			}
		}

		// TODO:进入地图，如果要实现新手村副本，在这里要做修改
		Game game = null;

		if (Game.logger.isInfoEnabled()) {
			Game.logger.info(player.getLogString() + "[lastGame:{}] [currentgame:{}] [要进入地图:{}] [{}]", new Object[] { player.getLastGame(), player.getCurrentGame(), gameName, player.transferGameCountry });
		}

		if (player.getCurrentGame() != null) {
			if (player.getCurrentGame().gi.getName().equals(gameName) && player.getCurrentGame().country == player.transferGameCountry) {
				BattleFieldManager bm = BattleFieldManager.getInstance();
				DownCityManager dcm = DownCityManager.getInstance();
				if ((bm != null && bm.isBattleFieldGame(gameName)) || (dcm != null && dcm.isDownCityByName(gameName))) {
					// 如果是战场或副本地图，那么不让利用当前地图进入
					if (logger.isWarnEnabled()) logger.warn("[{}] [enter_game] [战场或副本地图，那么不让利用当前地图进入，避免出现跳转错误的情况] [{}] [{}] [{}] [{}]", new Object[] { getName(), (game != null ? "game != null" + game.gi.name : "game == null" + gameName), player.getUsername(), player.getId(), player.getName() });
				} else {
					game = player.getCurrentGame();
					try {
//						if (DisasterManager.getInst().isDisasterMap(game.gi.name)) {
//							game = null;
//						}
						if (XianLingChallengeManager.mapNames.contains(game.gi.name)) {
							XianLingChallengeManager.getInstance().deleteXLChallenge(player);
							game = null;
						}
					} catch (Exception e) {
						if (logger.isErrorEnabled()) logger.error("[enter_game] [异常]", e);
					}
				}
			} else {
				player.getCurrentGame().messageQueue.push(new LeaveGameEvent(player));
				Thread.sleep(500);
				if (player.getCurrentGame() != null) {
					player.leaveGame();
				}
			}
		}
		int setIndex = 0;
		boolean 进入游戏后传送回复活点 = false;

		// 此方法在最前边。。否则会出错-----任何场景都可以传送到天劫中
		if (game == null || (game.gi.name.equals(RobberyConstant.DUJIEMAP) && !TransitRobberyEntityManager.getInstance().isPlayerInRobbery(player.getId()))) {
			if (gameName.equals(RobberyConstant.DUJIEMAP)) {
				game = TransitRobberyManager.getInstance().getRobberyGame(player, gameName);
				if (game == null) {
					进入游戏后传送回复活点 = true;
					setIndex = 1;
					game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
					TransitRobberyEntityManager.getInstance().removeFromRobbering(player.getId());
				}
			} else if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(player.getId())) {
				TransitRobberyEntityManager.getInstance().removeFromRobbering(playerId);
			}
		}
		// 是否为恶魔广场副本
		DevilSquareManager dsm = DevilSquareManager.instance;
		if (game == null) {
			if (dsm != null && DevilSquareManager.carbonMaps.contains(gameName)) {
				if (dsm.isPlayerInDevilSquare(player)) {
					Iterator<Integer> ite = dsm.dsInst.keySet().iterator();
					while (ite.hasNext()) {
						int ii = ite.next();
						DevilSquare ds = dsm.dsInst.get(ii);
						if (ds.playerList.contains(player.getId())) {
							game = dsm.dsInst.get(ii).game;
							break;
						}
					}
					if (game == null) {
						进入游戏后传送回复活点 = true;
						setIndex = 2;
						game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
						dsm.notifyUseTransProp(player);
					}
				} else {
					进入游戏后传送回复活点 = true;
					setIndex = 3;
					game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
					dsm.notifyUseTransProp(player);
				}
			} else if (DevilSquareManager.instance.isPlayerInDevilSquare(player.getId())) {
				DevilSquareManager.instance.notifyUseTransProp(player);
			}
		}
		if (dsm != null && dsm.isPlayerInDevilSquare(player) && !DevilSquareManager.carbonMaps.contains(gameName)) { // 玩家处于副本中传送出去
			dsm.notifyUseTransProp(player);
		}
		if (game == null && XianLingChallengeManager.getInstance().mapNames.contains(gameName)) {
			game = XianLingChallengeManager.getInstance().findGame(player);
			if(game == null){
				进入游戏后传送回复活点 = true;
				game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
			}
		}
		if (XianLingManager.logger.isDebugEnabled()) {
			XianLingManager.logger.debug("[测试地图] [" + player.getLogString() + "] [game:" + game + "]");
		}
		FurnaceManager fInst = FurnaceManager.inst;
		if (game == null) {
			if (fInst != null && fInst.isFurnaceMap(gameName)) {
				game = fInst.findPlayerGame(player);
				if (game == null) {
					setIndex = 4;
					进入游戏后传送回复活点 = true;
					game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
					fInst.notifyPlayerLeaveGame(player);
				}
			} else if (fInst != null) { // 进入的地图不是炼丹地图，需要判断是否玩家离开炼丹地图
				fInst.notifyPlayerLeaveGame(player);
			}
		}
		FairyRobberyManager frmins = FairyRobberyManager.inst;
		if (game == null) {
			if (frmins != null && frmins.isRobberyMap(gameName)) {
				game = frmins.getPlayerRobberyGame(player);
				if (game == null) {
					setIndex = 5;
					进入游戏后传送回复活点 = true;
					game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
					frmins.notifyPlayerLeaveRobbery(player);
				}
			} else if (frmins != null) {
				frmins.notifyPlayerLeaveRobbery(player);
			}
		}
		ChestFightManager cInst = ChestFightManager.inst;
		if (game == null) {
			if (cInst != null && cInst.isChestFightMap(gameName)) {
				game = cInst.getFightGameByPlayer(player);
				if (game == null) {
					setIndex = 6;
					进入游戏后传送回复活点 = true;
					game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
					cInst.notifyLeaveGame(player);
				}
			} else if (cInst != null) {
				cInst.notifyLeaveGame(player);
			}
		}

		if (game == null) {
			if (FairyChallengeManager.getInst().isFairyChallengeMap(gameName)) {
				game = FairyChallengeManager.getInst().getChallengeGmae(player);
				if (game == null) {
					setIndex = 7;
					进入游戏后传送回复活点 = true;
					game = GameManager.getInstance().getGameByName(gameName, player.getCountry());
				} else if (FairyChallengeManager.getInst().getChallengeResult(player) == -2) {
					setIndex = 8;
					进入游戏后传送回复活点 = true;
					game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
					FairyChallengeManager.getInst().notifyPlayerUseTransProp(player);
				}
			} else if (FairyChallengeManager.getInst().isPlayerChallenging(player.getId())) {
				FairyChallengeManager.getInst().notifyPlayerLogin(player);
			}
		}
		// 是否为金猴天灾活动场景
//		if (game == null) {
//			if (DisasterManager.getInst().isDisasterMap(gameName)) {
//				game = DisasterManager.getInst().getPlayerGame(player);
//				if (DisasterManager.logger.isDebugEnabled()) {
//					DisasterManager.logger.debug("[查找空岛大冒险地图] [成功] [" + player.getLogString() + "] [game:" + game + "]");
//				}
//				if (game == null) {
//					setIndex = 10;
//					进入游戏后传送回复活点 = true;
//					game = GameManager.getInstance().getGameByName(gameName, player.getCountry());
//				}
//			}
//		}

		// 是否为四方神兽
//		if (game == null) {
//			// 进入PK场景
//			for (int i = 0; i < SiFangManager.getInstance().pkMapName.length; i++) {
//				if (SiFangManager.getInstance().pkMapName[i].equals(gameName)) {
//					Integer ty = SiFangManager.getInstance().playerGoToMapType.get(player.getId());
//					if (ty != null) {
//						int siFangType = ty.intValue();
//						if (siFangType >= 0 && siFangType < 5) {
//							game = SiFangManager.getInstance().infos[ty.intValue()].getGame();
//							SiFangManager.getInstance().playerGoToMapType.remove(player.getId());
//							SiFangManager.getInstance().infos[siFangType].addPlayerToJiaZuMap(player);
//						} else {
//							setIndex = 11;
//							进入游戏后传送回复活点 = true;
//							game = GameManager.getInstance().getGameByName(gameName, player.getCountry());
//						}
//					} else {
//						setIndex = 12;
//						进入游戏后传送回复活点 = true;
//						game = GameManager.getInstance().getGameByName(gameName, player.getCountry());
//					}
//					break;
//				}
//			}
//			// 进入boss场景
//			for (int i = 0; i < SiFangManager.getInstance().bossMmapName.length; i++) {
//				if (SiFangManager.getInstance().bossMmapName[i].equals(gameName)) {
//					Integer ty = SiFangManager.getInstance().playerGoToBossMap.get(player.getId());
//					if (ty != null) {
//						int siFangType = ty.intValue();
//						if (siFangType >= 0 && siFangType < 5) {
//							game = SiFangManager.getInstance().infos[ty.intValue()].getBossGame();
//							SiFangManager.getInstance().playerGoToBossMap.remove(player.getId());
//						} else {
//							setIndex = 13;
//							进入游戏后传送回复活点 = true;
//							game = GameManager.getInstance().getGameByName(gameName, player.getCountry());
//						}
//					} else {
//						setIndex = 14;
//						进入游戏后传送回复活点 = true;
//						game = GameManager.getInstance().getGameByName(gameName, player.getCountry());
//					}
//					break;
//				}
//			}
//		}

		// 是否为婚礼场景
		if (game == null) {
			MarriageManager mm = MarriageManager.getInstance();
			if (mm != null) {
				if (mm.getMarriageCityName().contains(gameName)) {
					Game hunfang = mm.hunfangGameMap.get(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
					if (hunfang != null && hunfang.gi.name.equals(gameName)) {
						if (MarriageManager.logger.isWarnEnabled()) MarriageManager.logger.warn("进入婚房==={} [mapName={}]", new Object[] { player.getName(), hunfang.gi.name });
						game = hunfang;
					} else {
						Long cityID = MarriageManager.getInstance().getPlayer2CityMap().get(player.getId());
						MarriageDownCity city = null;
						if (cityID != null) {
							city = MarriageManager.getInstance().getDownCityMap().get(cityID);
						}
						if (city != null && city.getState() != MarriageDownCity.STATE_OVER) {
							// MarriageManager.logger.debug("进入结婚场景===" + player.getName());
							if (MarriageManager.logger.isInfoEnabled()) MarriageManager.logger.info("进入结婚场景==={}", new Object[] { player.getName() });
							game = city.getGame();
						} else {
							setIndex = 15;
							MarriageManager.getInstance().getPlayer2CityMap().remove(player.getId());
							进入游戏后传送回复活点 = true;
							game = GameManager.getInstance().getGameByName(gameName, player.getCountry());
						}
					}
				}
			}
		}
		// 判断是否为战场
		if (game == null) {
			BattleFieldManager bm = BattleFieldManager.getInstance();
			if (bm != null && bm.isBattleFieldGame(gameName)) {
				BattleField battle = player.getBattleField();
				if (battle != null && battle.isOpen()) {
					game = battle.getGame();
				} else {
					// 战场不存在或者已经结束，将用户传送会复活点
					game = gm.getGameByName(gameName, CountryManager.中立);
					setIndex = 16;
					进入游戏后传送回复活点 = true;
					// 清除战场的标记
					player.setBattleField(null);
					player.setInBattleField(false);
					if (logger.isWarnEnabled()) logger.warn("[{}] [enter_game1] [战场不存在或者已经结束，将用户传送会复活点] [{}] [{}] [{}] [{}]", new Object[] { getName(), (game != null ? "game != null" + game.gi.name : "game == null" + gameName), player.getUsername(), player.getId(), player.getName() });
				}
			} else {
				// 清除战场的标记
				player.setBattleField(null);
				player.setInBattleField(false);
			}
		}

//		 判断是否为副本
//		if (game == null) {
//			DownCityManager dcm = DownCityManager.getInstance();
//			if (dcm != null && dcm.isDownCityByName(gameName)) {
//				DownCity dc = player.getDownCityProgress(gameName);
//				if (dc != null) {
//					game = dc.getGame();
//				} else {
//					// 副本不存在或者已经结束，将用户传送会复活点
//					game = gm.getGameByName(gameName, CountryManager.中立);
//					setIndex = 17;
//					进入游戏后传送回复活点 = true;
//					if (logger.isWarnEnabled()) logger.warn("[{}] [enter_game] [副本不存在或者已经结束，将用户传送会复活点] [{}] [{}] [{}] [{}]", new Object[] { getName(), (game != null ? "game != null" + game.gi.name : "game == null" + gameName), player.getUsername(), player.getId(), player.getName() });
//				}
//			}
//		}

		if (PetDaoManager.getInstance().isPetDao(gameName)) {
			if (XianLingManager.logger.isDebugEnabled()) {
				XianLingManager.logger.debug("[测试地图22] [" + player.getLogString() + "] [game:" + game + "]");
			}
			game = null;
		}
		if (SealManager.getInstance().isSealDownCity(gameName)) {
			if (XianLingManager.logger.isDebugEnabled()) {
				XianLingManager.logger.debug("[测试地图33] [" + player.getLogString() + "] [game:" + game + "]");
			}
			game = null;
		}

		if (game == null) {
			try {
				Long lasttime = SealManager.playersOfDownCitys.get(new Long(player.getId()));
				if (lasttime == null || lasttime.longValue() > 0) {
					game = 处理封印副本(gameName, player);
				} else {
					game = gm.getGameByName(gameName, CountryManager.中立);
					setIndex = 18;
					进入游戏后传送回复活点 = true;
					SealManager.playersOfDownCitys.put(new Long(player.getId()), new Long(System.currentTimeMillis()));
					if (logger.isWarnEnabled()) logger.warn("[{}] [{}] [enter_game] [副本不存在或者已经结束，将用户传送会复活点] [{}] [{}] [{}] [{}]", new Object[] { getName(), "封印", (game != null ? "game != null" + game.gi.name : "game == null" + gameName), player.getUsername(), player.getId(), player.getName() });
				}
			} catch (Exception e) {
				logger.warn("处理封印副本" + player.getLogString() + "--" + gameName);
			}
		}

		if (game == null) {
			if (gameName.contains(DiceManager.getInstance().mapName)) {
				game = DiceManager.getInstance().getDiceGame(player, gameName);
				if (game == null) {
					game = gm.getGameByName(gameName, CountryManager.中立);
					if (game != null) {
						game.messageQueue.push(new TransferGameEvent(player, new TransportData(0, 0, 0, 0, DiceManager.tranMapName, DiceManager.x, DiceManager.y)));
						player.setGame(DiceManager.tranMapName);
						player.setX(DiceManager.x);
						player.setY(DiceManager.y);
						if (WolfManager.logger.isWarnEnabled()) {
							logger.warn("[骰子迷城] [进入游戏] [成功] [gameName:" + gameName + "] [game:" + (game.gi != null ? game.gi.name : "null") + "] [进入游戏后传送回复活点:" + 进入游戏后传送回复活点 + "] [" + player.getLogString() + "]");
						}
					} else {
						if (WolfManager.logger.isWarnEnabled()) {
							logger.warn("[骰子迷城] [进入游戏] [失败:地图不存在] [gameName:" + gameName + "] [进入游戏后传送回复活点:" + 进入游戏后传送回复活点 + "] [" + player.getLogString() + "]");
						}
					}
				}
			}
		}

		if (gameName.contains(WolfManager.getInstance().mapName)) {
			if (XianLingManager.logger.isDebugEnabled()) {
				XianLingManager.logger.debug("[测试地图44] [" + player.getLogString() + "] [game:" + game + "]");
			}
			game = null;
		}
		if(game == null){
			logger.warn("[boss副本1] [game:"+game+"] [room:"+player.room+"] [gameName:"+gameName+"] ["+BossCityManager.getInstance().isClose()+"] ["+player.getLogString()+"]");
			if(gameName.equals("jiefengBOSStiaozhanditu")){
				if(player.room != null && !BossCityManager.getInstance().isClose()){
					game = player.room.getCity().getGame();
					if(game == null){
						player.room = null;
						进入游戏后传送回复活点 = true;
					}
				}
			}
		}
//		if(gameName.equals("jiefengBOSStiaozhanditu") && BossCityManager.entenP.containsKey(new Long(player.getId()))){
//			game = BossCityManager.getInstance().handerPlayerEnter(player);
//			gameName = TransportData.getMainCityMap(player.getCountry());
//			//进入游戏后传送回复活点 = true;
//		}
		logger.warn("[boss副本2] [game:"+game+"] [room:"+player.room+"] [gameName:"+gameName+"] ["+进入游戏后传送回复活点+"] ["+player.getLogString()+"]");
//		if(game == null){
//			if(player.bcity != null){
//				game = player.bcity.getGame();
//				if(game == null){
//					player.bcity = null;
//				}
//				logger.warn("[家族远征] [game:"+game+"] ["+player.getLogString()+"]");
//			}
//		}
		if(game == null){
			if(DownCityManager2.instance.inSingleCityGame(gameName)){
				if(DownCityManager2.instance.cityMap.containsKey(new Long(player.getId()))){
					CityAction city = DownCityManager2.instance.cityMap.get(new Long(player.getId()));
					if(city != null && city.playerInGame(player) && !city.isEnd()){
						game = city.getGame();
						logger.warn("[单人副本] [city:"+city.getClass().getSimpleName()+"] [game:"+game+"] ["+player.getLogString()+"]");
					}
				}
			}
		}
		
		if (game == null) {
			if (DownCityManager2.instance.inTeamCityGame(gameName)) {
				Team team = player.getTeam();
				if(DownCityManager2.instance.cityMap.containsKey(new Long(player.getId()))){
					if (team != null && team.getCity() != null) {
						game = team.getCity().getGame();
					} 
				}
				logger.warn("[组队副本] [game:"+game+"] [ingame:"+DownCityManager2.instance.cityMap.containsKey(new Long(player.getId()))+"] [team:"+team+"] [city:"+(team!=null?team.getCity():"null")+"] [gameName:"+gameName+"] ["+player.getLogString()+"]");
			}
		}
		
		if(game == null && "zhanmotianyu".equals(gameName)){
			if(DownCityManager2.instance.cityMap.containsKey(new Long(player.getId()))){
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
				if(jiazu != null && jiazu.getCity() != null){
					game = jiazu.getCity().getGame();
				}
			}
			if(game == null){
				String mapName = TransportData.getMainCityMap(player.getCountry());
				logger.warn("[{}] [enter_game] [副本不存在或者已经结束，将用户传送会复活点2] [gameName:{}] [mapName:{}] [{}] [{}] [{}]", 
						new Object[] { getName(), (game != null ? "game != null" + game.gi.name : "game == null" + gameName),mapName, player.getUsername(), player.getId(), player.getName() });
			}
			logger.warn("[远征副本] [game:"+game+"] [gameName:"+gameName+"] ["+DownCityManager2.instance.cityMap.containsKey(new Long(player.getId()))+"] [gameName:"+gameName+"] ["+player.getLogString()+"]");
		}
		
		if (game == null) {
			if (gameName.contains(WolfManager.getInstance().mapName)) {
				game = WolfManager.getInstance().getGame(player, gameName);
				if (game == null) {
					player.setSpeedA(150);
					ResourceManager.getInstance().getAvata(player);
					game = gm.getGameByName(gameName, CountryManager.中立);
					setIndex = 19;
					进入游戏后传送回复活点 = true;
					if (WolfManager.logger.isWarnEnabled()) {
						logger.warn("[小羊快跑] [进入游戏1] [game:" + game + "] [进入游戏后传送回复活点:" + 进入游戏后传送回复活点 + "] [speeda:" + player.getSpeedA() + "] [speed:" + player.getSpeed() + "] [" + player.getLogString() + "]");
					}
				}
			}

			if (WolfManager.logger.isWarnEnabled()) {
			logger.warn("[跨服pk] [进入游戏3] [game:" + game + "] [setIndex:" + setIndex + "] [gameName:" + gameName + "] [进入游戏后传送回复活点:" + 进入游戏后传送回复活点 + "] [" + player.getLogString() + "]");
		}
		}

		if (game == null) {
			try {
				game = 处理宠物迷城(gameName, player);
			} catch (Exception e) {
				logger.warn("处理宠物迷城异常" + player.getLogString() + "--" + gameName);
			}
		}
		logger.warn("[幻境单人副本4] [game:"+game+"] [gameName:"+gameName+"] ["+player.getLogString()+"]");
		if (gameName.contains(SeptStationManager.jiazuMapName)) { // 在家族地图下线
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu != null) {
				gameName = gameName + "_jz_" + jiazu.getJiazuID();
			} else {
				gameName = player.getHomeMapName();
			}
			logger.warn(player.getLogString() + "[要进入家族地图][家族是否存在:{}][要进入的地图:{}]", new Object[] { jiazu != null, gameName });
		}
		gameName = getModifiedGameName4Special(player, gameName);
		
		/** 进入家族驻地判断 */
		if (gameName.indexOf("_jz_") > 0) {
			if (logger.isInfoEnabled()) logger.info(player.getLogString() + "[进入家族地图][{}]", new Object[] { gameName });
			int _index = gameName.lastIndexOf("_");
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu != null) {
				SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
				if (septStation != null && septStation.getGame() != null) {
					game = septStation.getGame();

					MapArea mapArea = game.getGameInfo().getMapAreaByName(Translate.出生点);
					Point location = gm.getLocationInWorldMap(gameName);
					// 允许玩家进入预先设定的跳转目标点
					player.setX(mapArea.getX());
					player.setY(mapArea.getY());

					res = new ENTER_GAME_RES(req.getSequnceNum(), (byte) 0, "OK", (short) player.getX(), (short) player.getY(), (short) location.x, (short) location.y);
					player.setTransportData(null);
					player.setRealMapName(gameName);

					player.setConn(conn);
					player.prepareEnterGame(game);
					game.messageQueue.push(new PlayerMessagePair(player, message, null));
					logger.error(player.getLogString() + "[进入家族地图成功,GAME:{}] [gameName:{}]", new Object[] { game ,gameName});
					{
						// 通知显示/取消家族BOSS按钮
						septStation.noticeJiazuBossButton(player, true);
					}
				} else {
					logger.error(player.getLogString() + "[要进入家族地图][家族驻地是否存在{}]", new Object[] { septStation != null });
					gameName = gameName.substring(0, _index);
					进入游戏后传送回复活点 = true;
					game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
				}
			} else {
				logger.error(player.getLogString() + "[要进入家族地图][家族不存在]");
				gameName = gameName.substring(0, _index);
				进入游戏后传送回复活点 = true;
				game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
			}
		}
		logger.warn("[幻境单人副本5] [game:"+game+"] [gameName:"+gameName+"] ["+player.getLogString()+"]");
		if (gameName.indexOf("_jy_") > 0) { // 家园
			if (FaeryManager.logger.isWarnEnabled()) {
				FaeryManager.logger.warn(player.getLogString() + "[要进入家园] [{}]", new Object[] { gameName });
			}
			int _index = gameName.lastIndexOf("_");
			long faeryId = Long.valueOf(gameName.substring(_index + 1, gameName.length()));

			// if (FaeryManager.getInstance().getKhatamMap().containsKey(player.getId())) { // 仙府被封印了,传出去
			// gameName = player.getResurrectionMapName();
			// game = gm.getGameByName(gameName, player.getCountry());
			// player.setLastX(player.getResurrectionX());
			// player.setLastY(player.getResurrectionY());
			// if (FaeryManager.logger.isWarnEnabled()) {
			// FaeryManager.logger.warn(player.getLogString() + " [要进入仙府,但是被封印了,传出去] [game:"+game+"] [res:"+res+"] [" + player.getResurrectionMapName() + ":(" +
			// player.getResurrectionX() + "," + player.getResurrectionY() + ")]");
			// }
			// } else {
			Faery faery = FaeryManager.getInstance().getFaery(faeryId);
			if (faery != null) {
				try {
					game = faery.getGame();
					String lastRealGameName = player.getRealMapName();

					if (lastRealGameName.indexOf(FaeryManager.GAME_NAME) == -1) {
						MapArea mapArea = game.getGameInfo().getMapAreaByName(Translate.出生点);
						player.setX(mapArea.getX());// tp.getTargetMapX());
						player.setY(mapArea.getY());// tp.getTargetMapY());
					}
					player.setRealMapName(gameName);
					res = new ENTER_GAME_RES(req.getSequnceNum(), (byte) 0, "OK", (short) player.getX(), (short) player.getY(), (short) player.getX(), (short) player.getY());
					player.setTransportData(null);

					player.setConn(conn);
					player.prepareEnterGame(game);
					game.messageQueue.push(new PlayerMessagePair(player, message, null));

					{
						// 如果骑乘的是飞行坐骑,则需要下马
						if (player.isFlying()) {
							player.downFromHorse();
						}
					}
					CAVE_SHOW_TOOLBAR_RES toolbar_RES = new CAVE_SHOW_TOOLBAR_RES(GameMessageFactory.nextSequnceNum());
					player.addMessageToRightBag(toolbar_RES);

					CHANGE_GAME_DISPLAYNAME_REQ displayname_REQ = new CHANGE_GAME_DISPLAYNAME_REQ(GameMessageFactory.nextSequnceNum(), game.gi.name, faery.getName());
					player.addMessageToRightBag(displayname_REQ);
					if (logger.isInfoEnabled()) logger.info(player.getLogString() + " [进入仙府地图] [成功] [并通知把地图名字{} 替换成 {}]", new Object[] { game.gi.name, faery.getName() });
				} catch (Exception e) {
					FaeryManager.logger.error(player.getLogString() + "[进入仙府异常]", e);
				}
			} else {
				gameName = gameName.substring(0, _index);
				进入游戏后传送回复活点 = true;
				game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
				// }
			}
		}
		if (gameName.indexOf("_pss_") > 0) {
			try {
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [要进入寻人boss地图] [" + gameName + "]");
				}
				// 只能进自己的
				PeopleSearchScene peopleSearchScene = PeopleSearchManager.getInstance().getPeopleSearchScene(player);
				if (peopleSearchScene != null) {
					game = peopleSearchScene.getGame();
					peopleSearchScene.setOwnerEnter(true);
					player.setX(PeopleSearchManager.getInstance().getPlayerBournPoint().getX());
					player.setY(PeopleSearchManager.getInstance().getPlayerBournPoint().getY());
					res = new ENTER_GAME_RES(req.getSequnceNum(), (byte) 0, "OK", (short) player.getX(), (short) player.getY(), (short) player.getX(), (short) player.getY());

					player.setRealMapName(gameName);
					player.setTransportData(null);

					player.setConn(conn);
					player.prepareEnterGame(game);
					game.messageQueue.push(new PlayerMessagePair(player, message, null));
					logger.error(player.getLogString() + "[进入地图成功,GAME:{}]", new Object[] { game.getGameInfo().name });
				} else {
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [判断可以进入] [场景不存在了]");
					}
				}
			} catch (Exception e) {
				logger.error(player.getLogString() + "[进入寻人boss地图异常]", e);
			}
		}

		// 末日
//		boolean isMoRi = DoomsdayManager.getInstance().isToDoomsDayGame(gameName);
//		if (isMoRi) {
//			game = DoomsdayManager.getInstance().getDoomsDayGame(player, gameName);
//			if (game == null) {
//				进入游戏后传送回复活点 = true;
//				game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
//			}
//		}

		// 千层塔
		boolean b = QianCengTaManager.getInstance().isInQianCengTanGame(gameName);
		if (b) {
			QianCengTaManager taManager = QianCengTaManager.getInstance();
			QianCengTa_Ta ta = taManager.getTa(playerId);
			if (ta != null) {
				game = ta.handle_ENTER_GAME_REQ(player, req);
				if (game == null) {
					进入游戏后传送回复活点 = true;
					game = GameManager.getInstance().getGameByName(gameName, CountryManager.中立);
				}
			}
		}
		if (res == null) {
			if (game == null) {
				game = gm.getGameByName(gameName, player.transferGameCountry);
				logger.warn(player.getLogString() + "[得到想要进入的地图1:{}] [game:{}] [{}]", new Object[] { gameName, game,player.transferGameCountry });
				if (game == null) {
					game = gm.getGameByName(gameName, CountryManager.中立);
					logger.warn(player.getLogString() + "[得到想要进入的地图2:{}] [game:{}] [{}]", new Object[] { gameName,game, CountryManager.中立 });
				}
				if (game == null) {
					game = gm.getGameByName(gameName, player.getCountry());
					logger.warn(player.getLogString() + "[得到想要进入的地图3:{}] [game:{}] [{}]", new Object[] { gameName, game,player.getCountry() });
				}
			}
			//再加一层
			if(game == null && errorGame){
//				String oldGameName = gameName;
//				gameName = TransportData.getMainCityMap(player.getCountry());
				int count = -1;
				for(int i=0;i<4;i++){
					count = i;
					game = gm.getGameByName(gameName, count);
					if(game != null){
						break;
					}
				}
				
				logger.warn("[进入地图] [找不到地图修复] [gameName:"+gameName+"] [传送国家:"+player.transferGameCountry+"] [玩家国家:"+player.getCountry()+"] [count:"+count+"] ["+player.getLogString()+"]");
			}

			if (game == null) {
				res = new ENTER_GAME_RES(req.getSequnceNum(), (byte) 3, Translate.地图不存在, (short) 0, (short) 0, (short) 0, (short) 0);
				if (logger.isWarnEnabled()) logger.warn(player.getLogString() + "[进入地图][地图不存在{}][gameName:{}]", new Object[] { player.getLastGame(), gameName });
			} else if (player.getId() != playerId) {
				res = new ENTER_GAME_RES(req.getSequnceNum(), (byte) 5, Translate.指定的角色与链接角色不匹配, (short) 0, (short) 0, (short) 0, (short) 0);
				if (logger.isWarnEnabled()) logger.warn("[{}] [指定的角色与链接角色不匹配] [username:{}] [player:{}] [playerId1:{}] [playerId2:{}]", new Object[] { getName(), username, player.getName(), player.getId(), playerId });
			} else if (game.getNumOfPlayer() >= game.gi.maxPlayerNum) {
				res = new ENTER_GAME_RES(req.getSequnceNum(), (byte) 6, Translate.地图中已经达到最大的人数, (short) 0, (short) 0, (short) 0, (short) 0);
			}
		}

		if (res == null) {
			Point location = gm.getLocationInWorldMap(gameName);
			player.setWorldMapX((short) location.x);
			player.setWorldMapY((short) location.y);
			TransportData tp = player.getTransportData();
			// 允许玩家进入预先设定的跳转目标点
			if (tp != null) {
				int targetX = tp.getTargetMapX();
				int targetY = tp.getTargetMapY();
				
				if(tp.getTargetMapName()==null||tp.getTargetMapName().isEmpty()){
					int country = player.getCurrentGameCountry();
					if(country == 0){
						country = player.getCountry();
					}
					if(country==1){
						targetX=tp.getTargetMapX1();
						targetY=tp.getTargetMapY1();
					}
					else if(country==2){
						targetX=tp.getTargetMapX2();
						targetY=tp.getTargetMapY2();
					}
					else if(country==3){
						targetX=tp.getTargetMapX3();
						targetY=tp.getTargetMapY3();
					}
				}
				
				player.setX(targetX);
				player.setY(targetY);

				// 为新手村越界设置
				if (game != null && game.gi != null && game.gi.getMapName() != null && game.gi.getMapName().indexOf(Translate.新手村) >= 0) {
					if (player.getX() < 0 || player.getX() > game.gi.getWidth() || player.getY() < 0 || player.getY() > game.gi.getHeight()) {
						player.setX(265);
						player.setY(335);
					}
				}

				res = new ENTER_GAME_RES(req.getSequnceNum(), (byte) 0, "OK", (short) player.getX(), (short) player.getY(), (short) location.x, (short) location.y);
				player.setTransportData(null);
			} else {
				// 为新手村越界设置
				if (game != null && game.gi != null && game.gi.getMapName() != null && game.gi.getMapName().indexOf(Translate.新手村) >= 0) {
					if (player.getX() < 0 || player.getX() > game.gi.getWidth() || player.getY() < 0 || player.getY() > game.gi.getHeight()) {
						player.setX(265);
						player.setY(335);
					}
				}
				res = new ENTER_GAME_RES(req.getSequnceNum(), (byte) 0, "OK", (short) player.getX(), (short) player.getY(), (short) location.x, (short) location.y);
			}

			player.setConn(conn);

			player.prepareEnterGame(game);
			NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
			if (ndtm != null && ndtm.isNewDeliverTaskAct) {
				ndtm.notifyEnterGame(game, player);
			} else {
				DeliverTaskManager dtm = DeliverTaskManager.getInstance();
				if (dtm != null) {
					dtm.notifyEnterGame(game, player);
				}
			}

			//logger.warn("[幻境单人副本8] [地图传送测试] [服务器传送处理] [playercurrentGameCournty:"+player.getCurrentGameCountry()+"] [gamecountry:"+game.country+"] [目标地图:"+(game.gi!=null?game.gi.name:"nul")+"] ["+player.getName()+"]");
			game.messageQueue.push(new PlayerMessagePair(player, message, null));

			// 如果是跨服，通知客户端跨服组队信息

			if (进入游戏后传送回复活点) {

				game.messageQueue.push(new TransferGameEvent(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY())));
				player.setGame(player.getResurrectionMapName());
				player.setX(player.getResurrectionX());
				player.setY(player.getResurrectionY());
				if (logger.isWarnEnabled()) logger.warn("[{}] [enter_game] [战场不存在或者已经结束，将用户传送会复活点] [{}] [{}] [{}] [{}]", new Object[] { getName(), (game != null ? "game != null" + game.gi.name : "game == null" + gameName), player.getUsername(), player.getId(), player.getName() });

			}
			if (jumpFlag) {
				Player.sendPlayerAction(player, PlayerActionFlow.行为类型_跳转地图成功, (game.getGameInfo() != null ? game.getGameInfo().getMapName() : Translate.text_2689), 0, new java.util.Date(), GamePlayerManager.isAllowActionStat());
			}
		} else {
			player.setTransportData(null);
		}

		if (game != null) {
			player.checkCollectionNPC(game);
		}
		player.setRealMapName(gameName);

		if (HotspotManager.getInstance() != null) {
			HotspotManager.getInstance().sendHotspot2Client(player);
		}
		NoticeManager.getInstance().enterServerSendActivityNotice(player);
		NoticeManager.getInstance().enterServerSendNotice(player);

		player.tolerance4AutoDeliverTask();

		// 国战通知
		Guozhan guozhan = GuozhanOrganizer.getInstance().getPlayerGuozhan(player);
		if (guozhan != null && !player.isIsGuozhan() && !player.isGuozhanLoginNotified() && player.getLevel() >= GuozhanOrganizer.getInstance().getConstants().国战等级限制) {
			// 发送国战通知
			player.setGuozhanLoginNotified(true);
			GuozhanOrganizer.getInstance().sendConfirmFightToPlayer(player, guozhan.getEnemyCountryName(player));
		}

		{
			// 数据统计
			if (game != null) {
				if ("kunhuagucheng".equals(game.gi.name)) {
					AchievementManager.getInstance().record(player, RecordAction.进入昆华古城);
				}else if ("shangzhouxianjing".equals(game.gi.name)) {
					AchievementManager.getInstance().record(player, RecordAction.进入昆华古城);
				}else if ("wanfayiji".equals(game.gi.name)) {
					AchievementManager.getInstance().record(player, RecordAction.进入昆华古城);
				}else if ("kunlunshengdian".equals(game.gi.name)) {
					AchievementManager.getInstance().record(player, RecordAction.进入昆仑圣殿);
				}else if ("jiuzhoutiancheng".equals(game.gi.name)) {
					AchievementManager.getInstance().record(player, RecordAction.进入昆仑圣殿);
				}else if ("wanfazhiyuan".equals(game.gi.name)) {
					AchievementManager.getInstance().record(player, RecordAction.进入昆仑圣殿);
				} else if ("shilianzhitayiceng".equals(game.gi.name)) {
					AchievementManager.getInstance().record(player, RecordAction.进入玲珑塔);
				}
				TransitRobberyManager.getInstance().failRobbery(player, game.gi.name); // 玩家如果正在渡劫出场景就失败
				TransitRobberyManager.getInstance().check4NextRobberyLevel(player.getId());
			}
		}

		// AcrossServerManager.getInstance().testServlet("http://116.213.192.203:8002/ZacrossServerCommunication", "切换地图");

		// TODO:去掉原来为了客户端bug所修改的代码
		// if (player.getTeam() != null) {
		// for(int i = 0 ; i < player.getTeam().getMembers().size() ; i++){
		// Player teamP = player.getTeam().getMembers().get(i);
		// if (teamP.getId() != player.getId()) {
		// AROUND_CHANGE_REQ req1 = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new Player[]{teamP}, new Pet[0], new Sprite[0], new Player[0], new Pet[0], new
		// Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0]);
		// player.addMessageToRightBag(req1);
		// }
		// }
		// }
		ArticleProtectManager.instance.send_ARTICLEPROTECT_MSG_RES(player);
		player.addMessageToRightBag(res);
		player.处理登录弹框();
		
		if (gameName.contains(SeptStationManager.jiazuMapName)) { // 在家族地图下线
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu != null) {
				SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
				NOTICE_CLIENT_UPDATE_MAPRES_REQ res22 = new NOTICE_CLIENT_UPDATE_MAPRES_REQ(GameMessageFactory.nextSequnceNum(), septStation.getMapName2UpLv(), 0, septStation.getMapName(), true);
				player.addMessageToRightBag(res22);
				logger.info(player.getLogString() + "[要进入家族地图][家族是否存在2:{}][要进入的地图:{}][{}]", new Object[] { jiazu != null, gameName ,septStation.getMapName2UpLv()});
			} 
		}
		
		
		if (player.getUsername().equals("wtx062") && GameConstants.getInstance().getServerName().equals("客户端测试")) {
			try {
				String stacktrace = StringUtil.getStackTrace(Thread.currentThread().getStackTrace());
				GamePlayerManager.logger.warn("[TESTENTER] [core...handle_ENTER_GAME_REQ2] [name:"+player.getName()+"\n] "+stacktrace);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (logger.isWarnEnabled()) logger.warn("[{}] [enter_game] [{}] [{}] [{}] [player:{}] [game:{}] [{}ms]", 
				new Object[] { getName(), (res.getResult() == 0 ? "success" : "failed"), res.getResultString(), username, (player == null ? playerId : player.getName()), (game == null ? gameName : game.gi.name+"--"+game.country), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
		return null;
	}

	public void handle_ANDROID_PROCESS_REQ(Connection conn, Player player, RequestMessage message) {

		try {
			ANDROID_PROCESS_REQ req = (ANDROID_PROCESS_REQ) message;
			Player_Process process = new Player_Process();
			process.setPlayerID(player.getId());
			process.setPlatform(req.getPlatform());
			process.setOtherGPU(req.getOtherGPU());
			process.setRealPlatform(req.getRealPlatform());
			process.setAndroidProcesss(req.getAndroidProcesss());
			process.setClientID(req.getClientID());
			process.modifyMoniqi();
			EnterLimitManager.player_process.put(player.getId(), process);
			if (EnterLimitManager.logger.isWarnEnabled()) {
				EnterLimitManager.logger.warn("[收到用户进程信息] [{}] [{}]", new Object[] { player.getLogString(), process.getLogString() });
			}
//			if (EnterLimitManager.isWaiGuaProcessCloseConn) {
//				if (process.isMoniqi()) {
//					Integer num = EnterLimitManager.playerCloseNum.get(player.getId());
//					if (num == null) {
//						num = new Integer(0);
//					}
//					num = num.intValue() + 1;
//					EnterLimitManager.playerCloseNum.put(player.getId(), num);
//					if (EnterLimitManager.logger.isWarnEnabled()) {
//						EnterLimitManager.logger.warn("[外挂进程连接关闭] [{}] [{}] [次数{}]", new Object[] { player.getLogString(), conn.getName(), num });
//					}
//					conn.close();
//					return;
//				}
//			}
			conn.setAttachmentData("ANDROID_PROCESS_REQ", req);
		} catch (Exception e) {
			EnterLimitManager.logger.error("处理用户进程出错 ", e);
		}

	}

	public PLAYER_TITLES_List_RES handlePlayerTitle(Player player, RequestMessage message) {

		List<PlayerTitle> playerTitles = player.getPlayerTitles();
		Honor[] honors = null;
		if (playerTitles != null && playerTitles.size() > 0) {
			honors = new Honor[playerTitles.size()];
			for (int i = 0; i < honors.length; i++) {
				if (playerTitles.get(i).getColor() <= 0) {
					int color = PlayerTitlesManager.getInstance().getColorByTitleType(playerTitles.get(i).getTitleType());
					String buffName = PlayerTitlesManager.getInstance().getBuffNameByType(playerTitles.get(i).getTitleType());
					int buffLevel = PlayerTitlesManager.getInstance().getBuffLevlByType(playerTitles.get(i).getTitleType());
					String titleShowName = PlayerTitlesManager.getInstance().getTitleShowNameByType(playerTitles.get(i).getTitleType());
					String description = PlayerTitlesManager.getInstance().getDescriptionByType(playerTitles.get(i).getTitleType());
					String icon = PlayerTitlesManager.getInstance().getIconByType(playerTitles.get(i).getTitleType());
					long lastTime = PlayerTitlesManager.getInstance().getLastTimeByType(playerTitles.get(i).getTitleType());
					playerTitles.get(i).setColor(color);
					playerTitles.get(i).setBuffName(buffName);
					playerTitles.get(i).setBuffLevl(buffLevel);
					playerTitles.get(i).setTitleShowName(titleShowName);
					playerTitles.get(i).setDescription(description);
					playerTitles.get(i).setIcon(icon);
					playerTitles.get(i).setLastTime(lastTime);
				}
				honors[i] = new Honor();
				honors[i].setName(playerTitles.get(i).getTitleName());
				honors[i].setIconId(playerTitles.get(i).getIcon());
				honors[i].setDesp(playerTitles.get(i).getDescription());
				honors[i].setId(playerTitles.get(i).getTitleType());
				honors[i].setColor(playerTitles.get(i).getColor());
				if (playerTitles.get(i).getLastTime() <= 0) {
					honors[i].setExpirationTime(-1); // 目前没有时限称号，先写死
				} else {
					honors[i].setExpirationTime(playerTitles.get(i).getLastTime() + playerTitles.get(i).getStartTime());
				}
			}
		} else {
			honors = new Honor[0];
		}
		PLAYER_TITLES_List_RES resp = new PLAYER_TITLES_List_RES(message.getSequnceNum(), honors);
		return resp;

	}

	public void 请求星級套(Player player, RequestMessage message) {

		QUERY_SUIT_INFO_REQ req = (QUERY_SUIT_INFO_REQ) message;
		long playerId = -1;
		byte type = req.getSuitType();
		if (type == 2 || type == 3) {
			long horseId = req.getPlayerId();
			Horse h = HorseManager.getInstance().getHorseById(horseId, player);
			if (h != null) {
				playerId = h.getOwnerId();
			}
		} else {
			playerId = req.getPlayerId();
		}
		PlayerManager pm = PlayerManager.getInstance();
		Player p = null;
		try {
			p = pm.getPlayer(playerId);
		} catch (Exception ex) {

		}
		if (p == null) {
			return;
		}
		ArticleManager am = ArticleManager.getInstance();
		byte career = p.getCareer();
		int star = 0;
		int color = 0;
		int nextStar = 6;
		int nextColor = ArticleManager.equipment_color_紫;
		long[] ids = null;
		if (type == 2 || type == 3) {
			// 默认坐骑
			long defaultId = p.getRideHorseId();
			if (defaultId > 0) {
				Horse horse = HorseManager.getInstance().getHorseById(defaultId, p);
				if (horse != null) {
					ids = horse.getEquipmentIds();
					if (HorseManager.logger.isWarnEnabled()) {
						HorseManager.logger.warn("[查询默认坐骑的套装效果] [成功] [" + p.getLogString() + "] [" + defaultId + "]");
					}
				}
			}

		} else {
			EquipmentColumn ec = p.getEquipmentColumns();
			ids = ec.getEquipmentIds();
		}

		if (ids != null) {
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			int starTemp = 0;
			int colorTemp = 0;
			boolean starFlag = false;
			boolean colorFlag = false;
			// 翅膀不算 2013年9月26日3:09:22 康建虎、刘洋
			int equipIDs = ids.length;
			if (type == 2 || type == 3) {
			} else {
				equipIDs = equipIDs - 2;
			}
			for (int i = 0; i < equipIDs; i++) {
				long id = ids[i];
				if (id <= 0) {
					starTemp = 0;
					colorTemp = 0;
					break;
				} else {
					ArticleEntity ae = aem.getEntity(id);
					if (ae instanceof EquipmentEntity) {
						if (!starFlag) {
							starTemp = ((EquipmentEntity) ae).getStar();
							starFlag = true;
						}
						if (((EquipmentEntity) ae).getStar() < starTemp) {
							starTemp = ((EquipmentEntity) ae).getStar();
						}
						if (!colorFlag) {
							colorTemp = ((EquipmentEntity) ae).getColorType();
							colorFlag = true;
						}
						if (((EquipmentEntity) ae).getColorType() < colorTemp) {
							colorTemp = ((EquipmentEntity) ae).getColorType();
						}
					} else {
						starTemp = 0;
						colorTemp = 0;
						break;
					}
				}
			}
			star = starTemp;
			color = colorTemp;
			if (star >= 6) {
				nextStar = star + 2;
			}
			if (color >= ArticleManager.equipment_color_紫) {
				nextColor = color + 1;
			}
		}
		if (star > 20) {
			star = 20;
		}
		// if (nextStar > 20) {
		// nextStar = 20;
		// }
		if (logger.isInfoEnabled()) logger.info("[颜色:{}[{}][职业:{}][star:{}[{}]]]", new Object[] { color, nextColor, career, star, nextStar });
		// 人物星级套
		if (type == 0) {
			StringBuffer sb = new StringBuffer();
			SuitEquipment se = am.getSuitEquipmentByStarAndCareer(star, career);
			if (se != null) {
				int starT = star / 2;
				sb.append(Translate.translateString(Translate.当前星级套装, new String[][] { { Translate.COUNT_1, String.valueOf((star / 2)) } }));
				int[] propertyValues = se.getPropertyValue();
				int[] openIndexs = se.getOpenPropertyIndexs();
				int openPropertyCount = 4;
				if (propertyValues != null && openIndexs != null) {
					for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
						int openIndex = openIndexs[i];
						int propertyValue = 0;
						if (openIndex < propertyValues.length) {
							propertyValue = propertyValues[openIndex];
							// (星级/2)^2/100*职业取值DFRT180级数值*70%*80%
							propertyValue = (int) ((1l * star * star * propertyValue * 70 * 80) / (4 * 100 * 100 * 100));
							if (logger.isInfoEnabled()) logger.info("[个数:{}[{}][职业:{}]]", new Object[] { propertyValue, openIndex, career });
						}
						switch (openIndex) {
						case SuitEquipment.FIRE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方金防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.ICE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方水防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.WIND_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方火防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.THUNDER_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方木防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						}
						sb.append("\n");
					}
				}
			}
			se = am.getSuitEquipmentByStarAndCareer(nextStar, career);
			if (se != null && (nextStar != star)) {
				sb.append(Translate.translateString(Translate.下一级别套装为, new String[][] { { Translate.COUNT_1, String.valueOf(nextStar / 2) } }));
				int[] propertyValues = se.getPropertyValue();
				int[] openIndexs = se.getOpenPropertyIndexs();
				int openPropertyCount = 4;
				if (propertyValues != null && openIndexs != null) {
					for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
						int openIndex = openIndexs[i];
						int propertyValue = 0;
						if (openIndex < propertyValues.length) {
							propertyValue = propertyValues[openIndex];
							if (logger.isInfoEnabled()) logger.info("[个数:{}[{}][职业{}]]", new Object[] { propertyValue, openIndex, career });
							// (星级/2)^2/100*职业取值DFRT180级数值*70%*80%
							propertyValue = (int) ((1l * nextStar * nextStar * propertyValue * 70 * 80) / (4 * 100 * 100 * 100));
							if (logger.isInfoEnabled()) logger.info("[个数:{}[{}][职业{}]]", new Object[] { propertyValue, openIndex, career });
						}
						sb.append("<f color='0x5E5E5E'>");
						switch (openIndex) {

						case SuitEquipment.FIRE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方金防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.ICE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方水防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.WIND_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方火防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.THUNDER_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方木防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;

						// case SuitEquipment.FIRE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方金防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.ICE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方水防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.WIND_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方火防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.THUNDER_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方木防等级" + propertyValue + "\n");
						// // }
						// break;
						}
						sb.append("</f>\n");
					}
				}
			}
			player.addMessageToRightBag(new QUERY_SUIT_INFO_RES(req.getSequnceNum(), type, sb.toString()));
			// 人物颜色套
		} else if (type == 1) {
			StringBuffer sb = new StringBuffer();
			SuitEquipment se = am.getSuitEquipmentByColorAndCareer(color, career);
			if (se != null) {
				sb.append(Translate.translateString(Translate.颜色套装, new String[][] { { Translate.STRING_1, ArticleManager.color_equipment_Strings[color] } }));
				if (logger.isInfoEnabled()) logger.info("[颜色:{}[{}][职业:{}{}]]", new Object[] { color, nextColor, career, sb.toString() });
				int[] propertyValues = se.getPropertyValue();
				int[] openIndexs = se.getOpenPropertyIndexs();
				int openPropertyCount = 0;
				if (color == ArticleManager.equipment_color_紫) {
					openPropertyCount = 1;
				} else if (color == ArticleManager.equipment_color_完美紫) {
					openPropertyCount = 2;
				} else if (color == ArticleManager.equipment_color_橙) {
					openPropertyCount = 3;
				} else if (color == ArticleManager.equipment_color_完美橙) {
					openPropertyCount = 4;
				}
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
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方金防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.ICE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方水防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.WIND_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方火防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.THUNDER_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方木防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;

						// case SuitEquipment.FIRE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方金防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.ICE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方水防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.WIND_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方火防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.THUNDER_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方木防等级" + propertyValue + "\n");
						// // }
						// break;
						}
						sb.append("\n");
					}
				}
			}
			se = am.getSuitEquipmentByColorAndCareer(nextColor, career);
			if (se != null) {
				sb.append(Translate.translateString(Translate.下一级颜色套装, new String[][] { { Translate.STRING_1, ArticleManager.color_equipment_Strings[nextColor] } }));
				int[] propertyValues = se.getPropertyValue();
				int[] openIndexs = se.getOpenPropertyIndexs();
				int openPropertyCount = 0;
				if (nextColor == ArticleManager.equipment_color_紫) {
					openPropertyCount = 1;
				} else if (nextColor == ArticleManager.equipment_color_完美紫) {
					openPropertyCount = 2;
				} else if (nextColor == ArticleManager.equipment_color_橙) {
					openPropertyCount = 3;
				} else if (nextColor == ArticleManager.equipment_color_完美橙) {
					openPropertyCount = 4;
				}
				if (propertyValues != null && openIndexs != null) {
					for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
						int openIndex = openIndexs[i];
						int propertyValue = 0;
						if (openIndex < propertyValues.length) {
							propertyValue = propertyValues[openIndex];
							// 装备颜色值^2/36*职业取值DFRT180级数值*30%*80%
							propertyValue = (int) ((1l * nextColor * nextColor * propertyValue * 30 * 80) / (36 * 100 * 100));
						}
						sb.append("<f color='0x5E5E5E'>");
						switch (openIndex) {

						case SuitEquipment.FIRE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方金防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.ICE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方水防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.WIND_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方火防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.THUNDER_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方木防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;

						// case SuitEquipment.FIRE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方金防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.ICE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方水防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.WIND_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方火防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.THUNDER_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方木防等级" + propertyValue + "\n");
						// // }
						// break;
						}
						sb.append("</f>\n");
					}
				}
			}
			player.addMessageToRightBag(new QUERY_SUIT_INFO_RES(req.getSequnceNum(), type, sb.toString()));
			// 马匹星级套
		} else if (type == 2) {
			StringBuffer sb = new StringBuffer();
			SuitEquipment se = am.getSuitEquipmentByStarAndCareer(star, career);
			if (se != null) {
				sb.append(Translate.translateString(Translate.当前星级套装, new String[][] { { Translate.COUNT_1, String.valueOf((star / 2)) } }));
				int[] propertyValues = se.getPropertyValue();
				int[] openIndexs = se.getOpenPropertyIndexs();
				int openPropertyCount = 4;
				if (propertyValues != null && openIndexs != null) {
					for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
						int openIndex = openIndexs[i];
						int propertyValue = 0;
						if (openIndex < propertyValues.length) {
							propertyValue = propertyValues[openIndex];
							// (星级/2)^2/100*职业取值DFRT180级数值*70%*20%
							propertyValue = (int) ((1l * star * star * propertyValue * 70 * 20) / (4 * 100 * 100 * 100));
						}
						switch (openIndex) {

						case SuitEquipment.FIRE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方金防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.ICE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方水防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.WIND_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方火防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.THUNDER_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方木防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;

						// case SuitEquipment.FIRE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方金防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.ICE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方水防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.WIND_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方火防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.THUNDER_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方木防等级" + propertyValue + "\n");
						// // }
						// break;
						}
						sb.append("\n");
					}
				}
			}
			se = am.getSuitEquipmentByStarAndCareer(nextStar, career);
			if (se != null && (nextStar != star)) {
				sb.append(Translate.translateString(Translate.下一级别套装为, new String[][] { { Translate.COUNT_1, String.valueOf(nextStar / 2) } }));
				int[] propertyValues = se.getPropertyValue();
				int[] openIndexs = se.getOpenPropertyIndexs();
				int openPropertyCount = 4;
				if (propertyValues != null && openIndexs != null) {
					for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
						int openIndex = openIndexs[i];
						int propertyValue = 0;
						if (openIndex < propertyValues.length) {
							propertyValue = propertyValues[openIndex];
							// (星级/2)^2/100*职业取值DFRT180级数值*70%*20%
							propertyValue = (int) ((1l * nextStar * nextStar * propertyValue * 70 * 20) / (4 * 100 * 100 * 100));
						}
						sb.append("<f color='0x5E5E5E'>");
						switch (openIndex) {

						case SuitEquipment.FIRE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方金防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.ICE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方水防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.WIND_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方火防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.THUNDER_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方木防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;

						// case SuitEquipment.FIRE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方金防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.ICE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方水防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.WIND_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方火防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.THUNDER_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方木防等级" + propertyValue + "\n");
						// // }
						// break;
						}
						sb.append("</f>\n");
					}
				}
			}
			player.addMessageToRightBag(new QUERY_SUIT_INFO_RES(req.getSequnceNum(), type, sb.toString()));
			// 马匹颜色套
		} else if (type == 3) {
			StringBuffer sb = new StringBuffer();
			SuitEquipment se = am.getSuitEquipmentByColorAndCareer(color, career);
			if (se != null) {
				sb.append(Translate.translateString(Translate.颜色套装, new String[][] { { Translate.STRING_1, ArticleManager.color_equipment_Strings[color] } }));
				int[] propertyValues = se.getPropertyValue();
				int[] openIndexs = se.getOpenPropertyIndexs();
				int openPropertyCount = 0;
				if (color == ArticleManager.equipment_color_紫) {
					openPropertyCount = 1;
				} else if (color == ArticleManager.equipment_color_完美紫) {
					openPropertyCount = 2;
				} else if (color == ArticleManager.equipment_color_橙) {
					openPropertyCount = 3;
				} else if (color == ArticleManager.equipment_color_完美橙) {
					openPropertyCount = 4;
				}
				if (propertyValues != null && openIndexs != null) {
					for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
						int openIndex = openIndexs[i];
						int propertyValue = 0;
						if (openIndex < propertyValues.length) {
							propertyValue = propertyValues[openIndex];
							// 装备颜色值^2/36*职业取值DFRT180级数值*30%*20%
							propertyValue = (int) ((1l * color * color * propertyValue * 30 * 20) / (36 * 100 * 100));
						}
						switch (openIndex) {

						case SuitEquipment.FIRE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方金防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.ICE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方水防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.WIND_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方火防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.THUNDER_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方木防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;

						// case SuitEquipment.FIRE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方金防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.ICE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方水防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.WIND_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方火防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.THUNDER_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方木防等级" + propertyValue + "\n");
						// // }
						// break;
						}
						sb.append("\n");
					}
				}
			}
			se = am.getSuitEquipmentByColorAndCareer(nextColor, career);
			if (se != null) {
				sb.append(Translate.translateString(Translate.下一级颜色套装, new String[][] { { Translate.STRING_1, ArticleManager.color_equipment_Strings[nextColor] } }));
				// sb.append("下一级别套装为" + ArticleManager.color_equipment_Strings[nextColor] + "套装\n");
				int[] propertyValues = se.getPropertyValue();
				int[] openIndexs = se.getOpenPropertyIndexs();
				int openPropertyCount = 0;
				if (nextColor == ArticleManager.equipment_color_紫) {
					openPropertyCount = 1;
				} else if (nextColor == ArticleManager.equipment_color_完美紫) {
					openPropertyCount = 2;
				} else if (nextColor == ArticleManager.equipment_color_橙) {
					openPropertyCount = 3;
				} else if (nextColor == ArticleManager.equipment_color_完美橙) {
					openPropertyCount = 4;
				}
				if (propertyValues != null && openIndexs != null) {
					for (int i = 0; i < openPropertyCount && i < openIndexs.length; i++) {
						int openIndex = openIndexs[i];
						int propertyValue = 0;
						if (openIndex < propertyValues.length) {
							propertyValue = propertyValues[openIndex];
							// 装备颜色值^2/36*职业取值DFRT180级数值*30%*20%
							propertyValue = (int) ((1l * nextColor * nextColor * propertyValue * 30 * 20) / (36 * 100 * 100));
						}
						sb.append("<f color='0x5E5E5E'>");
						switch (openIndex) {

						case SuitEquipment.FIRE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方金防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.ICE_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方水防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.WIND_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方火防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;
						case SuitEquipment.THUNDER_INDEX:
							// if (propertyValue != 0) {
							sb.append(Translate.translateString(Translate.减少对方木防等级, new String[][] { { Translate.COUNT_1, String.valueOf(propertyValue) } }));
							// }
							break;

						// case SuitEquipment.FIRE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方金防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.ICE_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方水防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.WIND_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方火防等级" + propertyValue + "\n");
						// // }
						// break;
						// case SuitEquipment.THUNDER_INDEX:
						// // if (propertyValue != 0) {
						// sb.append("减少对方木防等级" + propertyValue + "\n");
						// // }
						// break;
						}
						sb.append("</f>\n");
					}
				}
			}
			player.addMessageToRightBag(new QUERY_SUIT_INFO_RES(req.getSequnceNum(), type, sb.toString()));
		}

	}

	public void 处理仙界世界地图(RequestMessage message, Player player) {
		QUERY_WORLD__XJ_MAP_REQ req = (QUERY_WORLD__XJ_MAP_REQ) message;
		WorldMapManager wmm = WorldMapManager.getInstance();
		String areaname = req.getAreaname();
		try {
			WorldMapArea[] worldMapAreas_xj = wmm.worldMapAreas_xj;
			Game.logger.warn("[请求仙界地图] [worldMapAreas_xj:" + worldMapAreas_xj.length + "] [wmm.worldMapAreas_xj:" + wmm.worldMapAreas_xj.length + "] ");
			if (areaname == null || areaname.trim().length() == 0) {
				WorldMapArea[] areas = new WorldMapArea[0];
				List<WorldMapArea> as = new ArrayList<WorldMapArea>();
				for (WorldMapArea mapArea : worldMapAreas_xj) {
					if (mapArea != null) {
						for (int i = 0; i < wmm.仙界地图首界面包括的地域名.length; i++) {
							if (wmm.仙界地图首界面包括的地域名[i].equals(mapArea.name)) {
								as.add(mapArea);
							}
						}
					}
				}

				if (as.size() > 0) {
					areas = as.toArray(new WorldMapArea[] {});
				}

				QUERY_WORLD__XJ_MAP_RES res = new QUERY_WORLD__XJ_MAP_RES(message.getSequnceNum(), 0, "", areas);
				player.addMessageToRightBag(res);
				Game.logger.warn("[请求仙界地图] [areaname==null] [" + player.getName() + "] []");
			} else {
				if (wmm.是否是仙界第一次请求的地图(areaname)) {
					List<WorldMapArea> list = new ArrayList<WorldMapArea>();
					WorldMapArea area = null;
					for (WorldMapArea mapArea : worldMapAreas_xj) {
						for (int i = 0; i < wmm.仙界地图首界面包括的地域名.length; i++) {
							if (areaname.equals(mapArea.name)) {
								area = mapArea;
								break;
							}
						}
					}
					if (area != null) {
						String mapnames[] = area.mapnames;
						for (WorldMapArea mapArea : worldMapAreas_xj) {
							for (String name : mapnames) {
								if (name.equals(mapArea.engilshname)) {
									list.add(mapArea);
								}
							}
						}
					}
					QUERY_WORLD__XJ_MAP_RES res = new QUERY_WORLD__XJ_MAP_RES(message.getSequnceNum(), 1, "", list.toArray(new WorldMapArea[] {}));// (message.getSequnceNum(), "",
					// list.toArray(new
					// WorldMapArea[]{}));
					player.addMessageToRightBag(res);
					Game.logger.warn("[请求仙界地图] [是仙界第一次请求] [" + player.getName() + "] [areaname:" + areaname + "] [list:" + list.size() + "] [worldMapAreas_xj:" + worldMapAreas_xj.length + "] []");
				}
				// else{
				// WorldMapArea [] areas = new WorldMapArea[1];
				// for(WorldMapArea mapArea : worldMapAreas_xj){
				// if(areaname.equals(mapArea.name)){
				// areas[0] = mapArea;
				// break;
				// }
				// }
				// QUERY_WORLD__XJ_MAP_RES res = new QUERY_WORLD__XJ_MAP_RES(message.getSequnceNum(), "",areas);
				// player.addMessageToRightBag(res);
				// Game.logger.warn("[请求仙界地图] [不是仙界第一次请求] ["+player.getName()+"] [areaname:"+areaname+"] [areas:"+areas.length+"] [worldMapAreas_xj:"+worldMapAreas_xj.length+"] []");
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 6月30号更端的
	 * 每个商店中商品是否绑定由先有商品本身去控制
	 * @param message
	 * @param player
	 */
	public void 处理新的商城(RequestMessage message, Player player) {

		long startTime = System.currentTimeMillis();
		GET_SHOP_REQ req = (GET_SHOP_REQ) message;
		String shopName = req.getShopName();

		ShopManager shopManager = ShopManager.getInstance();

		String[] 新商城拥有的商店名字 = ShopManager.新商城拥有的商店名字;
		if (player.getVipLevel() > 0) {
			新商城拥有的商店名字 = ShopManager.新商城拥有的商店名字_VIP;
		}
		if (shopName == null || shopName.trim().length() == 0) {
			shopName = ShopManager.新商城拥有的商店名字[ShopManager.新商城默认显示的商店];
		}
		Shop shop = shopManager.getShop(shopName, player);
		if (shop == null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2776 + shopName + Translate.text_2777);
			player.addMessageToRightBag(hreq);
			return;
		}

		if (Translate.全部道具.equals(shopName)) {
			if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				VipManager.getInstance().noticePlayerRecordVipInfo(player);
				if (VipManager.logger.isInfoEnabled()) {
					VipManager.logger.info("[角色打开商城] [提示记录vip信息] [" + player.getLogString() + "]");
				}
			}
		} else {
			VipManager.logger.info("[打开商店] shopName:" + shopName);
		}

		if (player.getLevel() < 10) {
			player.sendError(Translate.十级之前不能打开商城);
			return;
		}

		Goods gs[] = shop.getGoods(player);
		List<Goods> list = new ArrayList<Goods>();
		List<Goods> sellout = new ArrayList<Goods>();
		List<Goods> sellover = new ArrayList<Goods>();
		for (Goods good : gs) {
			if (good.getFixTimeEndLimit() > 0) {
				if (good.getFixTimeEndLimit() > System.currentTimeMillis()) {
					list.add(good);
				}
			} else {
				list.add(good);
			}
		}

		for (Goods good : list) {
			if (good.getTotalSellNumLimit() > 0) {
				if (good.getTotalSellNum() >= good.getTotalSellNumLimit()) {
					sellout.add(good);
				} else {
					sellover.add(good);
				}
			} else {
				sellover.add(good);
			}
		}
		list.clear();
		list.addAll(sellover);
		list.addAll(sellout);

		com.fy.engineserver.shop.client.Goods[] cGoods = ShopManager.translate(player, shop, list.toArray(new Goods[] {}));
		boolean isOpen = shop.isOpen().getBooleanValue();
		long starttime = shop.isOpen().getLongValue();
		long chargePoints = player.getChargePoints();
		long activity = player.getActivenessInfo().getTotalActiveness();
		if (cGoods.length == 0) {
			isOpen = false;
		}
		ShopManager.logger.warn("[请求新商城] [shopName" + shopName + "] [" + shop.getTimelimits() + "] [玩家：" + player.getName() + "] [sellover:" + sellover.size() + "] [sellout:" + sellout.size() + "] [cGoods:" + cGoods.length + "] [isOpen:" + isOpen + "] [starttime:" + TimeTool.formatter.varChar23.format(starttime) + "] [chargePoints:" + chargePoints + "] [cost:" + (System.currentTimeMillis() - startTime) + "]");
		GET_SHOP_RES res = new GET_SHOP_RES(GameMessageFactory.nextSequnceNum(), shopName, isOpen, starttime, chargePoints, activity, 新商城拥有的商店名字, cGoods);
		player.addMessageToRightBag(res);
	}

	/**
	 * 9月30号更端的
	 * @param message
	 * @param player
	 */
	public void 处理最新的商城(RequestMessage message, Player player) {

		GET_NEW_SHOP_REQ req = (GET_NEW_SHOP_REQ) message;
		String shopName = req.getShopName();

		if (player.getLevel() < 10) {
			player.sendError(Translate.十级之前不能打开商城);
			return;
		}

		ShopManager shopManager = ShopManager.getInstance();

		String[] 新商城拥有的商店名字 = ShopManager.新商城拥有的商店名字;
		if (player.getVipLevel() > 0) {
			新商城拥有的商店名字 = ShopManager.新商城拥有的商店名字_VIP;
		}
		if (shopName == null || shopName.trim().length() == 0) {
			shopName = ShopManager.新商城拥有的商店名字[ShopManager.新商城默认显示的商店];
		}
		Shop shop = shopManager.getShop(shopName, player);
		if (shop == null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2776 + shopName + Translate.text_2777);
			player.addMessageToRightBag(hreq);
			return;
		}

		Goods gs[] = shop.getGoods(player);
		List<Goods> list = new ArrayList<Goods>();
		List<Goods> sellout = new ArrayList<Goods>();
		List<Goods> sellover = new ArrayList<Goods>();
		for (Goods good : gs) {
			if (good.getFixTimeEndLimit() > 0) {
				if (good.getFixTimeEndLimit() > System.currentTimeMillis()) {
					list.add(good);
				}
			} else {
				list.add(good);
			}
		}

		for (Goods good : list) {
			if (good.getTotalSellNumLimit() > 0) {
				if (good.getTotalSellNum() >= good.getTotalSellNumLimit()) {
					sellout.add(good);
				} else {
					sellover.add(good);
				}
			} else {
				sellover.add(good);
			}
		}
		list.clear();
		list.addAll(sellover);
		list.addAll(sellout);

		com.fy.engineserver.shop.client.Goods[] cGoods = ShopManager.translate(player, shop, list.toArray(new Goods[] {}));
		boolean isOpen = shop.isOpen().getBooleanValue();
		long starttime = shop.isOpen().getLongValue();
		long chargePoints = player.getChargePoints();
		long activity = player.getActivenessInfo().getTotalActiveness();
		if (cGoods.length == 0) {
			isOpen = false;
		}
		ShopManager.logger.warn("[请求新商城2] [shopName" + shopName + "] [" + shop.getTimelimits() + "] [玩家：" + player.getName() + "] [sellover:" + sellover.size() + "] [sellout:" + sellout.size() + "] [cGoods:" + cGoods.length + "] [isOpen:" + isOpen + "] [starttime:" + TimeTool.formatter.varChar23.format(starttime) + "] [chargePoints:" + chargePoints + "]");
		GET_NEW_SHOP_RES res = new GET_NEW_SHOP_RES(GameMessageFactory.nextSequnceNum(), shopName, isOpen, starttime, chargePoints, activity, 新商城拥有的商店名字, cGoods);
		player.addMessageToRightBag(res);
	}

	public Game 处理封印副本(String gameName, Player player) {
		Game game = null;
		try {
			if (SealManager.getInstance().isSealDownCity(gameName)) {
				game = PetDaoManager.getInstance().createGame(player, gameName);
				SealDownCity sd = new SealDownCity();
				sd.setGame(game);
				SealManager.getInstance().refreshMonster(game, player);
				SealManager.playersOfDownCitys.put(new Long(player.getId()), new Long(System.currentTimeMillis()));
				SealManager.getInstance().addSealTask(sd);
				if (Game.logger.isWarnEnabled()) {
					Game.logger.warn("[查看破封任务] [进入副本] [成功] [gameName:{}] [{}]", new Object[] { gameName, player.getLogString() });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return game;
		}
		return game;
	}

	public Game 处理宠物迷城(String gameName, Player player) {
		Game game = null;
		PetDaoManager pdm = PetDaoManager.getInstance();
		if (pdm != null && pdm.isPetDao(gameName)) {
			try {
				PetDao pt = PetDaoManager.getInstance().getPetDao(player, gameName);
				if (pt != null) {
					pt.getMc().STAT = 0;
					game = pdm.createGame(player, gameName);
					pt.setGame(game);
					pt.getMc().setP(player);
					// 调用顺序
					pt.getMc().setEndtime(pt.getMc().getContinuetime() + System.currentTimeMillis());
					pdm.addPetDao(pt);
					PetDaoManager.getInstance().refreshBox(pt.getOverBox(), game, player, pt.getTypelevel());
					PetDaoManager.getInstance().refreshMonster(game, pt);
					if (pt.getMc().getKeyovernum() < 0) {
						pt.getMc().setKeyovernum(0);
					}
					if (player.isIsUpOrDown() && player.isFlying()) {
						player.downFromHorse();
					}
					String mes = "<f color='0x78f4ff'>" + PetDaoManager.等级使用物品描述[PetDaoManager.等级索引(pt.getTypelevel())] + Translate.数量 + "：</f>" + pt.getMc().getKeyovernum() + "/" + PetDaoManager.初始给钥匙数 + "\n" + "<f color='0x78f4ff'>" + PetDaoManager.等级消耗物品描述[PetDaoManager.等级索引(pt.getTypelevel())] + Translate.数量 + "：</f>" + pt.getMc().getBoxovernum() + "/30";
					NOTICE_ACTIVITY_STATUS_RES res_ = new NOTICE_ACTIVITY_STATUS_RES(GameMessageFactory.nextSequnceNum(), true, "", 1, Translate.倒计时, mes, (int) ((pt.getMc().getContinuetime()) / 1000));
					player.addMessageToRightBag(res_);
					PetDaoManager.log.warn("[宠物迷城] [申请进入迷城2] [成功] [overboxs:" + (pt.getOverBox() == null ? "null" : pt.getOverBox().size()) + "] [地图:" + gameName + "] [" + player.getLogString() + "] ");
				} else {
					try {
						Game defaultTransferGame = GameManager.getInstance().getGameByName("miemoshenyu", CountryManager.中立);
						player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, "miemoshenyu", defaultTransferGame.gi.name, 1195, 1290));
						PetDaoManager.log.warn("[宠物迷城] [玩家上线] [回城] [" + player.getName() + "]");
					} catch (Exception e) {
						PetDaoManager.log.warn("[宠物迷城心跳] [回城] [异常]" + e);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				PetDaoManager.log.warn("[玩家上线] [异常] [" + e + "] [gameName:" + gameName + "] [name:" + player.getName() + "]");
			}
		}
		return game;
	}

	/**
	 * 是否是有效的问题
	 * 
	 * @param question
	 * @return
	 */
	public static boolean isAvailabeQuestion(String question) {
		if (allQuestion != null) {
			for (String str : allQuestion) {
				if (str.equals(question)) {
					return true;
				}
			}
		}
		return false;
	}

	private ResponseMessage doWithSOCIAL_RELATION_MANAGE_REQ(SOCIAL_RELATION_MANAGE_REQ req, Player player) {

		return null;
	}

	private void pushMessageToGame(Player player, RequestMessage message, Object attachment) {

		Game game = player.getCurrentGame();

		if (game != null) {
			game.messageQueue.push(new PlayerMessagePair(player, message, attachment));
		} else {
			// logger.warn("[" + getName() + "] [玩家" + player.getName() +
			// "不在任何地图上] [指令" + message.getTypeDescription() + "非法] [" +
			if (logger.isWarnEnabled()) logger.warn("[{}] [玩家{}不在任何地图上] [指令{}非法]", new Object[] { getName(), player.getName(), message.getTypeDescription() });
		}
	}

	public boolean handleResponseMessage(Connection conn, RequestMessage req, ResponseMessage message) throws ConnectionException, Exception {
		Player player = (Player) conn.getAttachmentData("Player");
		if (player != null) {
			player.setLastRequestTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		}
		if (message instanceof VALIDATE_INFO_RES) {
			VALIDATE_INFO_RES res = (VALIDATE_INFO_RES) message;
			OtherValidateManager.getInstance().notifyAnswerAsk(player, null, res.getValidateAnswer(), -1);
			return true;
		}
		return false;
	}

	public File getSkillFile() {
		return skillFile;
	}

	public void setSkillFile(File skillFile) {
		this.skillFile = skillFile;
	}

	public MonsterManager getMonsterManager() {
		return monsterManager;
	}

	public void setMonsterManager(MonsterManager spriteManager) {
		this.monsterManager = spriteManager;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public static com.fy.engineserver.datasource.article.entity.client.ArticleEntity translate(ArticleEntity src) {
		ArticleManager am = ArticleManager.getInstance();
		Article article = am.getArticle(src.getArticleName());
		if (article == null) {
			return null;
		}

		byte articleType = article.getArticleType();
		ComposeManager inst = ComposeManager.instance;
		try {
			if (inst != null && inst.compose4ColorArticle != null && inst.compose4OtherLeft != null && inst.compose4OtherRight != null) {
				for (TempArtilce ta : inst.compose4ColorArticle) {
					if (ta.getArticleName().equals(src.getArticleName()) && ta.getArticleColorNum() == src.getColorType()) {
						articleType = 91;
					}
				}
				for (TempArtilce ta : inst.compose4OtherLeft) {
					if (ta.getArticleName().equals(src.getArticleName()) && ta.getArticleColorNum() == src.getColorType()) {
						articleType = 90;
					}
				}
				for (TempArtilce ta : inst.compose4OtherRight) {
					if (ta.getArticleName().equals(src.getArticleName()) && ta.getArticleColorNum() == src.getColorType()) {
						articleType = 91;
					}
				}
			} else {
				ComposeManager.logger.warn("[新合成有问题！！][" + inst + "]");
			}
		} catch (Exception e) {
			ComposeManager.logger.error("[新合成转换类型出异常]", e);
		}
		if (article instanceof EnchantArticle) {
			EnchantArticle ea = (EnchantArticle) article;
			for (int i = 0; i < EnchantManager.开放附魔的位置.length; i++) {
				if (EnchantManager.开放附魔的位置[i] == ea.getEquipmentType()) {
					articleType = EnchantManager.对应物品类型[i];
					break;
				}
			}
		}
		String iconId = article.getIconId();
		if (article instanceof SoulPithArticle) {
			articleType = 97;
			try {
				int tempLv = ((SoulPithArticleEntity) src).getLevel();
				String tempIc = ((SoulPithArticle) article).getLevelDatas().get(tempLv).getCorner();
				iconId = iconId + "," + tempIc;
			} catch (Exception e) {
				SoulPithManager.logger.warn("[拼灵髓icon] [异常] [" + src.getId() + "]", e);
			}
		}
		if (article instanceof GourdArticle) {
			articleType = 98;
		}
		if (article instanceof PetSuitArticle) {
			articleType = 99;
		}
		if (article instanceof HuntLifeArticle) {
			int tempLv = 1;
			try {
				if (src instanceof HuntLifeArticleEntity) {
					HuntLifeArticleEntity he = (HuntLifeArticleEntity) src;
					tempLv = he.getExtraData().getLevel();
					ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(tempLv);
					iconId = model.getIcon1() + "," + iconId + "," + model.getIcon2();
				}
			} catch (Exception e) {
				HuntLifeManager.logger.warn("[拼兽魂icon] [异常] [" + src.getId() + "]", e);
			}
			articleType = 79;
		}

		com.fy.engineserver.datasource.article.entity.client.ArticleEntity dest = new com.fy.engineserver.datasource.article.entity.client.ArticleEntity();
		dest.setId(src.getId());
		dest.setBinded(src.isBinded());
		if(article instanceof InlayArticle){
			dest.setShowName(((InlayArticle)article).getShowName());
		}else{
			dest.setShowName(article.getName());
		}
		dest.setIconId(iconId);
		if (src instanceof NewMagicWeaponEntity) {
			NewMagicWeaponEntity nn = (NewMagicWeaponEntity) src;
			if (nn.icon != null && !nn.icon.isEmpty()) {
				dest.setIconId(nn.icon);
			}
			if (((NewMagicWeaponEntity) src).getMstar() > 20) {
				String iconname = "";
				if (ArticleManager.replaceicon.get(new Integer(((NewMagicWeaponEntity) src).getMstar())) != null) {
					iconname = ArticleManager.replaceicon.get(new Integer(((NewMagicWeaponEntity) src).getMstar()));
				}
				dest.setIconId(dest.getIconId() + "," + iconname);
			}
		}
		dest.setName(article.getName());
		dest.setBindStyle(article.getBindStyle());
		dest.setColorType((byte) src.getColorType());
		dest.setArticleType(articleType);
		dest.setOverlap(article.isOverlap());
		dest.setOverLapNum(article.getOverLapNum());
		dest.setSellPrice(article.getPrice());
		dest.setSequelPrice(article.getValidNeedRuanbao());
		dest.setComposeArticleType(article.getComposeArticleType());
		dest.setKnapsackType(article.getKnapsackType());
		dest.setOneClass(article.get物品一级分类());
		dest.setTwoClass(article.get物品二级分类());
		// if (article.isHaveValidDays() && article.getMaxValidDays() != 0 &&
		// src.getInValidTime() != 0 && src.getInValidTime() <
		// com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {
		// dest.setSequelType(article.getInvalidAfterAction());
		// }
		return dest;
	}

	public static com.fy.engineserver.datasource.article.entity.client.PropsEntity translate(PropsEntity src) {
		ArticleManager am = ArticleManager.getInstance();
		Article article = am.getArticle(src.getArticleName());
		if (article == null) {
			return null;
		}
		Props props = (Props) article;

		com.fy.engineserver.datasource.article.entity.client.PropsEntity dest = new com.fy.engineserver.datasource.article.entity.client.PropsEntity();
		dest.setId(src.getId());
		dest.setBinded(src.isBinded());
		dest.setShowName(props.getName());
		dest.setName(props.getName());
		dest.setIconId(props.getIconId());
		try {
			if (src instanceof PetPropsEntity) {
				PetManager mwm = PetManager.getInstance();
				Pet mw = mwm.getPet(((PetPropsEntity) src).getPetId());
				String icon = "";
				if (mw != null && mw.petProps != null) {
					GradePet conf = Pet2Manager.getInst().findGradePetConf(mw.petProps.getName());
					if (conf != null) {
						if (mw.grade > 4) {// 形象不变
							if (mw.grade < 8) {
								icon = conf.lv4Icon;
							} else {
								icon = conf.lv7Icon;
							}
							if (icon != null && !icon.isEmpty()) {
								dest.setIconId(icon);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ArticleManager.logger.error("[宠物icon变换错误]", e);
		}
		dest.setBindStyle(props.getBindStyle());
		dest.setColorType((byte) src.getColorType());
		dest.setPropsType(props.getPropsType());
		dest.setCategoryName(props.getCategoryName());
		dest.setFightStateLimit(props.isFightStateLimit());
		dest.setArticleType(article.getArticleType());
		dest.setOverlap(article.isOverlap());
		dest.setOverLapNum(article.getOverLapNum());
		dest.setSellPrice(article.getPrice());
		dest.setLevelLimit((short) props.getLevelLimit());
		dest.setSequelPrice(article.getValidNeedRuanbao());
		dest.setComposeArticleType(article.getComposeArticleType());
		dest.setKnapsackType(article.getKnapsackType());
		dest.setOneClass(article.get物品一级分类());
		dest.setTwoClass(article.get物品二级分类());
		// if (article.isHaveValidDays() && article.getMaxValidDays() != 0 &&
		// src.getInValidTime() != 0 && src.getInValidTime() <
		// com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {
		// dest.setSequelType(article.getInvalidAfterAction());
		// }
		return dest;
	}

	public static com.fy.engineserver.datasource.article.entity.client.EquipmentEntity translate(EquipmentEntity src) {
		ArticleManager am = ArticleManager.getInstance();
		Article article = am.getArticle(src.getArticleName());
		if (article == null) {
			return null;
		}
		Equipment equipment = (Equipment) article;

		com.fy.engineserver.datasource.article.entity.client.EquipmentEntity dest = new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity();
		dest.setId(src.getId());
		dest.setBinded(src.isBinded());
		dest.setDurability((byte) src.getDurability());
		dest.setLevel((byte) src.getLevel());
		dest.setShowName(article.getName());
		dest.setName(equipment.getName());
		dest.setIconId(equipment.getIconId());
		if (src.getStar() > 20) {
			String iconname = "";
			if (ArticleManager.replaceicon.get(new Integer(src.getStar())) != null) {
				iconname = ArticleManager.replaceicon.get(new Integer(src.getStar()));
			}
			dest.setIconId(article.getIconId() + "," + iconname);
		}
		dest.setBindStyle(equipment.getBindStyle());
		dest.setEquipmentType((byte) equipment.getEquipmentType());
		dest.setColorType((byte) src.getColorType());
		dest.setArticleType(article.getArticleType());
		dest.setOverlap(article.isOverlap());
		dest.setOverLapNum(article.getOverLapNum());
		dest.setSellPrice(article.getPrice());
		dest.setLevelLimit((short) equipment.getPlayerLevelLimit());
		dest.setInlayArticleColors(src.getInlayArticleColors());
		dest.setInlayArticleIds(src.getInlayArticleIds());
		dest.setCreaterName(src.getCreaterName());
		dest.setInscriptionFlag(src.isInscriptionFlag());
		dest.setBasicPropertyValue(src.getBasicPropertyValue());
		dest.setAdditionPropertyValue(src.getAdditionPropertyValue());
		dest.setEndowments(src.getEndowments());
		dest.setStrongParticles(equipment.getStrongParticles());
		dest.setStar((byte) src.getStar());
		dest.setSequelPrice(article.getValidNeedRuanbao());
		dest.setComposeArticleType(article.getComposeArticleType());
		dest.setDevelopEXP(src.getDevelopEXP());
		dest.setDevelopUpValue(ArticleManager.developEXPUpValue);
		dest.setKnapsackType(article.getKnapsackType());
		dest.setOneClass(article.get物品一级分类());
		dest.setTwoClass(article.get物品二级分类());
		dest.setCareerLimit(equipment.getCareerLimit());
		// if (article.isHaveValidDays() && article.getMaxValidDays() != 0 &&
		// src.getInValidTime() != 0 && src.getInValidTime() <
		// com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {
		// dest.setSequelType(article.getInvalidAfterAction());
		// }
		if (equipment instanceof Weapon) {
			dest.setWeaponType(((Weapon) equipment).getWeaponType());
		}

		return dest;
	}

	public static void notifyEquipmentChange(Player player, EquipmentEntity[] ees) {
		com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] equipmentEntities = new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[ees.length];
		for (int i = 0; i < ees.length; i++) {
			equipmentEntities[i] = translate(ees[i]);
		}
		NOTIFY_EQUIPMENT_CHANGE_REQ req = new NOTIFY_EQUIPMENT_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), equipmentEntities);
		player.addMessageToRightBag(req);
	}

	// yangtao 100330 end

	// yangtao 100407 start
	public BillboardManager getBillboardManager() {
		return billboardManager;
	}

	public void setBillboardManager(BillboardManager billboardManager) {
		this.billboardManager = billboardManager;
	}

	// yangtao 100407 end

	// yangtao 100412 start
	private GET_BILLBOARD_MENU_RES getBillBoardsMenu(GET_BILLBOARD_MENU_REQ req) {
		GET_BILLBOARD_MENU_RES res = null;
		Logger log = BillboardManager.log;
		try {
			Billboards[] billboards = this.billboardManager.getBillboards();
			String[] menu;
			String[] submenu;
			byte[] type;
			if (billboards != null && billboards.length > 0) {
				menu = new String[billboards.length];
				submenu = new String[billboards.length];
				type = new byte[billboards.length];
				for (int i = 0; i < billboards.length; i++) {
					if (billboards[i] != null) {
						menu[i] = billboards[i].getName();
						type[i] = billboards[i].getType();
						String[] ss = billboards[i].getSubmenu();
						submenu[i] = "";
						for (int j = 0; j < ss.length; j++) {
							submenu[i] += ss[j];
							if (j < ss.length - 1) {
								submenu[i] += ":";
							}
						}
					}
				}
				res = new GET_BILLBOARD_MENU_RES(req.getSequnceNum(), menu, submenu, type, BillboardManager.MAX_LINES);
				if (log.isInfoEnabled()) {
					// log.info("[获取排行榜菜单] [成功] [菜单：" + menu.length + "]");
					if (log.isInfoEnabled()) log.info("[获取排行榜菜单] [成功] [菜单：{}]", new Object[] { menu.length });
				}
			} else {
				if (log.isWarnEnabled()) log.warn("[获取排行榜菜单] [失败] [排行榜不存在]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (log.isWarnEnabled()) log.warn("[获取排行榜菜单] [失败] [发生错误] [错误：" + e + "]", e);
		}
		return res;
	}

	private GET_BILLBOARD_RES getBillboard(GET_BILLBOARD_REQ req, Player player) {
		GET_BILLBOARD_RES res = null;
		Logger log = BillboardManager.log;
		try {
			String menu = req.getMenu();
			String submenu = req.getSubmenu();
			BillboardData[] data = this.billboardManager.getBillboardData(menu, submenu);
			if (data != null) {
				String[] rankObj = new String[data.length];
				long[] value = new long[data.length];
				String[] description = new String[data.length];
				long[] id = new long[data.length];
				int[] additionalInfo = new int[data.length];
				int selfRank = 0;
				String[] titles = new String[0];
				Billboards billboards = this.billboardManager.getBillboards(menu);
				byte type = -1;
				if (billboards != null) {
					Billboard billboard = billboards.getBillboard(submenu);
					if (billboard != null) {
						selfRank = billboard.getRank(player.getId());
						titles = billboard.getTitles();
					}
					type = billboards.getType();
				}
				for (int i = 0; i < data.length; i++) {
					rankObj[i] = data[i].getRankingObject();
					value[i] = data[i].getValue();
					description[i] = data[i].getDescription();
					id[i] = data[i].getId();
					additionalInfo[i] = data[i].getAdditionalInfo();
					// if(type==BillboardType.TYPE_PLAYER){
					// String name="";
					// if(this.billboardManager.playerNames.containsKey(""+data[i].getId())){
					// name=this.billboardManager.playerNames.get(""+data[i].getId());
					// }else{
					// log.warn("[获取排行榜] [玩家未找到] [ID:"+data[i].getId()+"]");
					// }
					// rankObj[i]=name;
					// }else if(type==BillboardType.TYPE_GANG){
					// String gangName="";
					// if(this.billboardManager.gangNames.containsKey(""+data[i].getId())){
					// gangName=this.billboardManager.gangNames.get(""+data[i].getId());
					// }else{
					// log.warn("[获取排行榜] [帮会未找到] [ID:"+data[i].getId()+"]");
					// }
					// rankObj[i]=gangName;
					// }
				}
				res = new GET_BILLBOARD_RES(req.getSequnceNum(), titles, rankObj, value, description, id, additionalInfo, selfRank);

				int length = res.getLength();
				if (log.isInfoEnabled()) {
					// log.info("[获取排行榜] [成功] [菜单：" + menu + "] [子菜单：" + submenu
					// + "] [玩家：" + player.getName() + "]" + length);
					if (log.isInfoEnabled()) log.info("[获取排行榜] [成功] [菜单：{}] [子菜单：{}] [玩家：{}]{}", new Object[] { menu, submenu, player.getName(), length });
				}
			} else {
				// log.warn("[获取排行榜] [失败] [此排行榜不存在] [菜单：" + menu + "] [子菜单：" +
				// submenu + "]");
				if (log.isWarnEnabled()) log.warn("[获取排行榜] [失败] [此排行榜不存在] [菜单：{}] [子菜单：{}]", new Object[] { menu, submenu });
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (log.isWarnEnabled()) log.warn("[获取排行榜] [失败] [发生错误] [错误：" + e + "]", e);
		}
		return res;
	}

	public StatDataUpdateManager getStatDataUpdateManager() {
		return statDataUpdateManager;
	}

	public void setStatDataUpdateManager(StatDataUpdateManager statDataUpdateManager) {
		this.statDataUpdateManager = statDataUpdateManager;
	}

	private Player searchPlayer(long playerId, String name) {
		Player p = null;
		// 通过id或者name找到Player对象
		if (playerId >= 0) {
			try {
				p = playerManager.getPlayer(playerId);
			} catch (Exception e) {
				e.printStackTrace();
				// logger.error(e);
				logger.error(" ", e);
			}
		} else if (name != null && name.length() > 0) {
			try {
				p = playerManager.getPlayer(name);
			} catch (Exception e) {
				e.printStackTrace();
				// logger.error(e);
				logger.error(" ", e);
			}
		}
		return p;
	}

	private QUERY_ACTIVITIES_INFO_RES getActivitiesInfoRes(QUERY_ACTIVITIES_INFO_REQ req, Player player) {
		QUERY_ACTIVITIES_INFO_RES res = null;
		try {
			ActivityItem[][] ai = null;
			ArrayList<ActivityItem>[] items = ActivityItemManager.getInstance().getActivityItem(player);
			ai = new ActivityItem[items.length][];
			for (int i = 0; i < ai.length; i++) {
				ai[i] = new ActivityItem[items[i].size()];
				for (int j = 0; j < ai[i].length; j++) {
					ai[i][j] = items[i].get(j);
				}
			}
			res = new QUERY_ACTIVITIES_INFO_RES(req.getSequnceNum(), ai[0], ai[1], ai[2], ai[3], ai[4], ai[5], ai[6]);
			if (ActivityItemManager.log.isInfoEnabled()) {
				// ActivityItemManager.log.info("[申请运营活动列表] [成功] [玩家：" +
				// player.getName() + "]");
				if (ActivityItemManager.log.isInfoEnabled()) ActivityItemManager.log.info("[申请运营活动列表] [成功] [玩家：{}]", new Object[] { player.getName() });
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActivityItemManager.log.error("[申请运营活动列表] [发生错误] [玩家：" + player.getName() + "]", e);
		}
		return res;
	}

	private String getModifiedGameName4Special(Player player, String gameName) {
		if (gameName.indexOf("_") > 0) {
			return gameName;
		} else {
			if (gameName.equals(FaeryConfig.GAME_NAME)) {// 是庄园地图
				String lastRealGameName = player.getRealMapName();
				if (lastRealGameName != null && !"".equals(lastRealGameName)) {
					if (lastRealGameName.indexOf(FaeryConfig.GAME_NAME + "_") > -1) {
						long faeryId = Long.valueOf(lastRealGameName.substring(lastRealGameName.lastIndexOf("_") + 1, lastRealGameName.length()));
						Faery faery = FaeryManager.getInstance().getFaery(faeryId);
						if (faery != null) {
							gameName = lastRealGameName;
						}
					}
				}
			} else if (gameName.contains(SeptStationManager.jiazuMapName)) { // 是家族地图
				String lastRealGameName = player.getRealMapName();
				if (lastRealGameName != null && !"".equals(lastRealGameName)) {
					if (lastRealGameName.indexOf(SeptStationManager.jiazuMapName) > -1) {
						long jiazuId = Long.valueOf(lastRealGameName.substring(lastRealGameName.lastIndexOf("_") + 1, lastRealGameName.length()));
						Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
						if (jiazu != null) {
							SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(jiazuId);
							if (septStation != null) {
								gameName = lastRealGameName;
							}
						}
					}
				}
			} else if (gameName.startsWith(PeopleSearchManager.sceneName)) {// 是寻人BOSS地图
				if (player.getPeopleSearch() != null) {
					if (player.getPeopleSearch().canEnterGame()) {
						String lastRealGameName = player.getPeopleSearchKey();
						gameName = lastRealGameName;
					}
				}
			}
			return gameName;
		}
	}

	private String getChargeMoneyDes(SavingRecord savingRecord) {
		if (savingRecord.getSavingAmount() <= 0) {
			return "--";
		}
		if (MultiLanguageTranslateManager.getInstance().isNeedTranslate()) {// 需要翻译
			Platform platform = PlatformManager.getInstance().getPlatform();
			if (platform == (Platform.台湾)) {
				return BillingCenter.得到带单位的银两(ChargeRatio.DEFAULT_CHARGE_RATIO / 100 * savingRecord.getSavingAmount());
			} else if (platform == (Platform.韩国)) {
				return BillingCenter.得到带单位的银两(ChargeRatio.DEFAULT_CHARGE_RATIO / 100 * savingRecord.getSavingAmount());
			} else {
				return "----";
			}
		} else {
			return savingRecord.getSavingAmount() / 100 + "元";
		}
	}

	private String modifyChargeModeName(String oldModeName) {
		if (oldModeName.contains("支付宝")) {
			return "支付宝";
		} else if (oldModeName.contains("神州付")) {
			return "神州付";
		} else if (oldModeName.contains("移动")) {
			return "移动充值卡";
		} else if (oldModeName.contains("电信")) {
			return "电信充值卡";
		} else if (oldModeName.contains("联通")) {
			return "联通充值卡";
		} else if (oldModeName.contains("财付通")) {
			return "财付通";
		} else if (oldModeName.equals("番茄SDK充值")) {
			return "553充值";
		}
		return oldModeName;
	}

	static String getMoneyShow(long money) {
		StringBuffer sbf = new StringBuffer();

		long fen = money % 10;
		long jiao = money / 10;

		jiao = jiao % 10;
		long yuan = money / 100;
		if (yuan > 0) {
			sbf.append(yuan).append("元");
		}
		if (jiao > 0) {
			sbf.append(jiao).append("角");
		}
		if (fen > 0) {
			sbf.append(fen).append("分");
		}
		return sbf.toString();

	}

	public NEW_QUERY_CAREER_INFO_RES handle_NEW_QUERY_CAREER_INFO_REQ(NEW_QUERY_CAREER_INFO_REQ req, Player player) {
		try {
			long currtime = System.currentTimeMillis();
			if (player != null) {
				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				ActiveSkillEntity aes[] = player.getActiveSkillAgent().getInCoolDownSkills();
				for (int i = 0; i < aes.length; i++) {
					ActiveSkillEntity e = aes[i];
					long ll = e.getStartTime() + e.getIntonateTime() + e.getAttackTime() + e.getCooldownTime();
					if (e.getStatus() == ActiveSkillEntity.STATUS_COOLDOWN && ll - now >= 3000) {
						SKILL_CD_MODIFY_REQ sreq = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), player.getId(), (short) e.getSkill().getId(), (byte) ActiveSkillEntity.STATUS_COOLDOWN, e.getStartTime());
						player.addMessageToRightBag(sreq);
					}
				}
			} else {
				logger.error("[QUERY_CAREER_INFO_REQ] [PLAYER NULL]");
			}

			Career career = CareerManager.getInstance().getCareer(req.getCareer());
			if (career != null) {
				CareerThread threads[] = career.getCareerThreads();
				Skill[] skills1 = career.getBasicSkills();
				Skill[] skills2 = threads[0].getSkills();
				Skill[] skills3 = career.getNuqiSkills();
				Skill[] skills4 = career.getXinfaSkills();
				Skill[] skills5 = career.getKingSkills();
				Skill[] skills6 = career.getBianShenSkills();
				SkillInfo[] basicSkills = new SkillInfo[skills1.length];
				for (int i = 0; i < basicSkills.length; i++) {
					basicSkills[i] = new SkillInfo();
				}
				for (int i = 0; i < skills1.length; i++) {
					if (skills1[i] != null) {
						basicSkills[i].setInfo(player, skills1[i]);
					}
				}
				// TODO:此协议已经彻底满了，进阶技能只能发14个
				int professorNum = 14;
				if (professorNum > skills2.length) {
					professorNum = skills2.length;
				}
				SkillInfo[] professorSkills = new SkillInfo[professorNum];
				for (int i = 0; i < professorNum; i++) {
					professorSkills[i] = new SkillInfo();
				}
				for (int i = 0; i < professorNum; i++) {
					if (skills2[i] != null) {
						professorSkills[i].setInfo(player, skills2[i]);
					}
				}
				// TODO:新进阶
				SkillInfo[] newProfessorSkills = new SkillInfo[skills2.length - professorNum];
				for (int i = 0; i < skills2.length - professorNum; i++) {
					newProfessorSkills[i] = new SkillInfo();
				}
				for (int i = 0; i < skills2.length - professorNum; i++) {
					if (skills2[professorNum + i] != null) {
						newProfessorSkills[i].setInfo(player, skills2[professorNum + i]);
					}
				}
				SkillInfo[] nuqiSkills = new SkillInfo[skills3.length];
				for (int i = 0; i < nuqiSkills.length; i++) {
					nuqiSkills[i] = new SkillInfo();
				}
				for (int i = 0; i < skills3.length; i++) {
					if (skills3[i] != null) {
						nuqiSkills[i].setInfo(player, skills3[i]);
					}
				}
				// TODO 这里将不传心法技能，因为协议长度不够了
				// int xinfaNum = 0;
				// SkillInfo[] xinfaSkills = new SkillInfo[xinfaNum];
				// for (int i = 0; i < xinfaNum; i++) {
				// xinfaSkills[i] = new SkillInfo();
				// }
				// for (int i = 0; i < xinfaNum; i++) {
				// if (skills4[i] != null) {
				// xinfaSkills[i].setInfo(player, skills4[i]);
				// }
				// }
				// TODO 新的心法
				int oneReqNum = 10; // 一条协议发送10个心法技能
				for (int a = 0; a < skills4.length / oneReqNum + 1; a++) {
					int nn = oneReqNum;
					if (oneReqNum * (a + 1) > skills4.length) {
						nn = skills4.length - oneReqNum * a;
					}
					if (nn <= 0) {
						break;
					}
					SkillInfo[] newXinfaSkills = new SkillInfo[nn];
					for (int i = 0; i < nn; i++) {
						newXinfaSkills[i] = new SkillInfo();
					}
					for (int i = 0; i < nn; i++) {
						if (skills4[oneReqNum * a + i] != null) {
							newXinfaSkills[i].setInfo(player, skills4[oneReqNum * a + i]);
						}
					}
					QUERY_CAREER_XINFA_INFO_RES res1 = new QUERY_CAREER_XINFA_INFO_RES(req.getSequnceNum(), newXinfaSkills);
					player.addMessageToRightBag(res1);
					logger.warn("[发送心法] [协议长度:" + res1.getLength() + "] [数目:" + nn + "] [" + a + "]");
				}

				SkillInfo[] kingSkills = new SkillInfo[skills5.length];
				for (int i = 0; i < kingSkills.length; i++) {
					kingSkills[i] = new SkillInfo();
				}
				for (int i = 0; i < skills5.length; i++) {
					if (skills5[i] != null) {
						kingSkills[i].setInfo(player, skills5[i]);
					}
				}
				int bianshenid = 0;
				SkillInfo[] bianShenSkills = new SkillInfo[skills6.length];
				for (int i = 0; i < bianShenSkills.length; i++) {
					bianShenSkills[i] = new SkillInfo();
				}
				for (int i = 0; i < skills6.length; i++) {
					if (skills6[i] != null) {
						if (skills6[i] instanceof ActiveSkill) {
							ActiveSkill sk = (ActiveSkill) skills6[i];
							if (sk.isBianshenBtn()) {
								bianshenid = sk.getId();
							}
						}
						bianShenSkills[i].setInfo(player, skills6[i]);
					}
				}
				NEW_QUERY_CAREER_INFO_RES res = new NEW_QUERY_CAREER_INFO_RES(req.getSequnceNum(), career, basicSkills, professorSkills, nuqiSkills, new SkillInfo[0], kingSkills);
				QUERY_CAREER_JINJIE_INFO_RES res2 = new QUERY_CAREER_JINJIE_INFO_RES(req.getSequnceNum(), newProfessorSkills);
				player.addMessageToRightBag(res2);
				QUERY_CAREER_BIANSHEN_INFO_RES res3 = new QUERY_CAREER_BIANSHEN_INFO_RES(GameMessageFactory.nextSequnceNum(), bianshenid, bianShenSkills);
				player.addMessageToRightBag(res3);
				logger.warn("NEW_QUERY_CAREER_INFO_REQ发送成功 [" + res.getLength() + "] [bianshenid:" + bianshenid + "] [bs=" + basicSkills.length + "] [ps=" + professorSkills.length + "] [bianShenSkills:" + bianShenSkills.length + "] [ns=" + nuqiSkills.length + "] [xs=" + 0 + "] [cost:" + (System.currentTimeMillis() - currtime) + "]");
				return res;
			} else {
				// logger.warn("[" + getName() + "][QUERY_CAREER][fail]");
				if (logger.isWarnEnabled()) logger.warn("[{}][QUERY_CAREER][fail]", new Object[] { getName() });
			}
		} catch (Exception e) {
			logger.error("NEW_QUERY_CAREER_INFO_REQ出错", e);
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(getMoneyShow(101));
		System.out.println(101 % 10);
	}

	public long getBeforehandCreateStartTime() {
		return beforehandCreateStartTime;
	}

	public void setBeforehandCreateStartTime(long beforehandCreateStartTime) {
		this.beforehandCreateStartTime = beforehandCreateStartTime;
		CommonDiskCacheManager.getInstance().put(CommonSubSystem.预创建角色开始时间, beforehandCreateStartTime);
	}

	public long getBeforehandCreateEndTime() {
		return beforehandCreateEndTime;
	}

	public void setBeforehandCreateEndTime(long beforehandCreateEndTime) {
		this.beforehandCreateEndTime = beforehandCreateEndTime;
		CommonDiskCacheManager.getInstance().put(CommonSubSystem.预创建角色结束时间, beforehandCreateEndTime);
	}

	public int[] getBeforehandCreateCountryNum() {
		return beforehandCreateCountryNum;
	}

	public void setBeforehandCreateCountryNum(int[] beforehandCreateCountryNum) {
		this.beforehandCreateCountryNum = beforehandCreateCountryNum;
		CommonDiskCacheManager.getInstance().put(CommonSubSystem.预创建角色国家人数设定, beforehandCreateCountryNum);
	}

	public QUERY_VIP_DISPLAY_RES handle_QUERY_VIP_DISPLAY_REQ(Player player, RequestMessage message) {
		QUERY_VIP_DISPLAY_REQ req = (QUERY_VIP_DISPLAY_REQ) message;
		String channel = null;
		if (player.getPassport() != null) {
			channel = player.getPassport().getLastLoginChannel();
		}
		// if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
		// if ("ST".equals(GameConstants.getInstance().getServerName()) && "KOREATSTORE_MIESHI".equals(channel)) {
		// player.vipDisplay = false;
		// player.setVipLevel(player.getVipLevel());
		// if (VipManager.getInstance() != null) {
		// VipManager.getInstance().设置玩家的vip属性(player);
		// }
		// return new QUERY_VIP_DISPLAY_RES(req.getSequnceNum(), false);
		// }
		// }
		
		if (true) {
			player.vipDisplay = true;
			player.setVipLevel(player.getVipLevel());
			if (VipManager.getInstance() != null) {
				VipManager.getInstance().设置玩家的vip属性(player);
			}
			return new QUERY_VIP_DISPLAY_RES(req.getSequnceNum(), true);
		}
		if (channel != null && channel.toLowerCase().indexOf("appstore") >= 0 && !"APPSTORE_XUNXIAN".equals(channel) && !"KUNLUNAPPSTORE".equals(channel)) {
			player.vipDisplay = false;
			player.setVipLevel(player.getVipLevel());
			if (VipManager.getInstance() != null) {
				VipManager.getInstance().设置玩家的vip属性(player);
			}
			return new QUERY_VIP_DISPLAY_RES(req.getSequnceNum(), false);
		} else {
			player.vipDisplay = true;
			player.setVipLevel(player.getVipLevel());
			if (VipManager.getInstance() != null) {
				VipManager.getInstance().设置玩家的vip属性(player);
			}
			return new QUERY_VIP_DISPLAY_RES(req.getSequnceNum(), true);
		}
	}

	public PLAYER_ENTER_RES handler_playerEnter(PLAYER_ENTER_REQ req, Passport passport, Connection conn, String username, Player player) {
		long playerId = req.getPlayer();
		Player p = null;
		try {
			p = playerManager.getPlayer(playerId);
			if (BoothsaleManager.getInstance() != null) {
				BoothsaleManager.getInstance().boothSaleMap.remove(playerId);
			}
			p.setBoothState(false);
			p.setEndBoothTime(0);
		} catch (Exception e) {
			e.printStackTrace();
			// logger.error(e);
			logger.error(" ", e);
		}
		if (p != null) {
			// 检查是否有外挂
			if (passport.getUserName().equals(p.getUsername()) == false && passport.getNickName().equals(p.getUsername()) == false) {

				if (logger.isWarnEnabled()) logger.warn("[处理PLAYER_ENTER_REQ] [出现外挂] [{}] [登录用户：{}] [角色用户：{}] [{}] [角色上的帐号名与连接上的帐号名不匹配]", new Object[] { conn.getName(), passport.getUserName(), p.getUsername(), p.getName() });
				if (GameNetworkFramework.logger.isWarnEnabled()) GameNetworkFramework.logger.warn("[处理PLAYER_ENTER_REQ] [出现外挂] [{}] [登录用户：{}] [角色用户：{}] [{}] [角色上的帐号名与连接上的帐号名不匹配]", new Object[] { conn.getName(), passport.getUserName(), p.getUsername(), p.getName() });

				PLAYER_ENTER_RES res = new PLAYER_ENTER_RES(req.getSequnceNum(), (byte) 1, Translate.帐号名不匹配, new Buff[0], 0, 0, new short[0], new Soul[0], 0, 0, PlayerManager.每人每天可以使用绑银);
				return res;
			}
			if (SaleRecordManager.getInstance().isRoleInSale(p)) {
				if (logger.isWarnEnabled()) {
					logger.warn("[处理PLAYER_ENTER_REQ] [角色正在出售中] [不允许登陆] [" + p.getLogString() + "]");
				}
				PLAYER_ENTER_RES res = new PLAYER_ENTER_RES(req.getSequnceNum(), (byte) 1, "角色出售中，请使用同账号下满10级的任意其他角色登陆米掌柜进行操作", new Buff[0], 0, 0, new short[0], new Soul[0], 0, 0, PlayerManager.每人每天可以使用绑银);
				return res;
			}

			// 登录成功，检查是否有这个用户的玩家在线，如果有，全部踢掉
			Player cachePlayers[] = playerManager.getCachedPlayers();
			for (Player pp : cachePlayers) {

				if (pp.getUsername().equals(p.getUsername()) == false) continue;

				if (pp.isOnline() && pp.getConn() != conn) {
					if (pp.getCurrentGame() != null) {
						pp.getCurrentGame().getQueue().push(new LeaveGameEvent(pp));
					}
					pp.leaveServer();
					pp.getConn().close();
					if (GamePlayerManager.logger.isWarnEnabled()) GamePlayerManager.logger.warn("[玩家进入服务器] [已有此用户的玩家在线] [踢掉此用户] [用户:{}] [玩家:{}]", new Object[] { username, pp.getName() });
				} else if (pp.isOnline() && pp.getConn() == conn && pp.getId() != p.getId()) {
					if (pp.getCurrentGame() != null) {
						pp.getCurrentGame().getQueue().push(new LeaveGameEvent(pp));
					}
					pp.leaveServer();
					pp.setConn(null);
					if (GamePlayerManager.logger.isWarnEnabled()) GamePlayerManager.logger.warn("[玩家进入服务器] [已有此用户的玩家在线] [踢掉此用户] [用户:{}] [玩家:{}]", new Object[] { username, pp.getName() });
				} else if (pp.getId() != p.getId() && pp.getCurrentGame() != null) {
					pp.getCurrentGame().getQueue().push(new LeaveGameEvent(pp));
					pp.leaveServer();
					if (GamePlayerManager.logger.isWarnEnabled()) GamePlayerManager.logger.warn("[玩家进入服务器] [已有此用户的玩家在游戏中] [踢掉此用户] [用户:{}] [玩家:{}]", new Object[] { username, pp.getName() });
				}
			}
			try {
				NEW_USER_ENTER_SERVER_REQ enterReq = (NEW_USER_ENTER_SERVER_REQ) conn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
				if (enterReq != null) {
					NOTIFY_USER_ENTERSERVER_REQ notify_req = new NOTIFY_USER_ENTERSERVER_REQ(GameMessageFactory.nextSequnceNum(), GameConstants.getInstance().getServerName(), enterReq.getUsername(), enterReq.getSessionId(), enterReq.getClientId(), enterReq.getChannel(), enterReq.getMACADDRESS(), conn.getIdentity().split(":")[0], enterReq.getClientPlatform(), enterReq.getPhoneType(), enterReq.getNetwork(), enterReq.getGpuOtherName());
					MieshiGatewayClientService.getInstance().sendMessageToGateway(notify_req);
					NewUserEnterServerService.userLoginConnections.remove(p.getUsername());
				}
			} catch (Exception e) {
				logger.error("发送新NOTIFY_USER_ENTERSERVER_REQ出错", e);
			}

			p.setPassport(passport);
			conn.setAttachmentData("Player", p);
			p.setConn(conn);

			// 设置玩家的视野
			if (FORCE_USE_DEFAULT_VIEW) {
				p.setViewWidth(DEFAULT_PLAYER_VIEWWIDTH);
				p.setViewHeight(DEFAULT_PLAYER_VIEWHEIGHT);
			} else {
				if (p.getViewWidth() == 0) {
					p.setViewWidth(DEFAULT_PLAYER_VIEWWIDTH);
				}
				if (p.getViewHeight() == 0) {
					p.setViewHeight(DEFAULT_PLAYER_VIEWHEIGHT);
				}
			}

			if (logger.isInfoEnabled()) {
				GamePlayerManager.logger.info("[玩家进入游戏] [成功] [第一次进入:{}] [{}]", new Object[] { p.isFirstEnter(), p.getLogString() });
				if (p.isFirstEnter()) {
					p.setFirstEnter(false);
				}
			}
			p.notifyPlayerEnterServer();

			player = p;
			player.enterServer();
			
			p.setPlayerLastLoginTime(p.getLoginServerTime());
			p.setLoginServerTime(System.currentTimeMillis());
			
			player.setTransportData(null);
			player.setTransferGameCountry(player.getCurrentGameCountry());// 把玩家身上的地图国家标记赋值给他将要去国家地图标记
			player.setLastCheckMailTime(0);
			player.checkNewMail();
			player.checkNextList();
			
			// 腾讯取魔钻
//			/TengXunDataManager.instance.send2getTengXunData(player);
			if (player.getKnapsack_common().size() < 40 + TengXunDataManager.instance.getAddKnapsackSize(player)) {
				player.getKnapsack_common().addCells(TengXunDataManager.instance.getAddKnapsackSize(player));
			}

			// 回调排队接口
			EnterServerAgent agent = EnterServerAgent.getInstance();
			agent.playerEnterCallback(username);

			// 发送日志
			//

			int version1 = 0;
			int version2 = 0;
			WorldMapInfo wmi = gm.getWorldMapInfo();
			if (wmi != null) {
				version1 = wmi.getVersion();
			}
			String gameName = player.getMapName();
			if (gameName != null) {
				GameInfo gi = gm.getGameInfo(gameName);
				if (gi != null) {
					version2 = gi.getVersion();
				}
			}

			short spriteTypes[] = gm.getAllSpriteTypeOnGame(gameName);

			// 给boss发送上线通知
			// bossClientService.playerLogin(p.getUsername(), p.getName(), p.getCountry(), new java.util.Date());

			p.setLeavedServer(false);

			// 调用补偿邮件发送
			GMAutoSendMail gmsendmail = GMAutoSendMail.getInstance();
			if (gmsendmail != null) {
				gmsendmail.sendLogic(p);
			}
			long switchLeft = 0;
			if (player.getLastSwitchSoulTime() != 0) {
				switchLeft = (player.getLastSwitchSoulTime() + Soul.switchCd) - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			}
			switchLeft = switchLeft > 0 ? switchLeft : 0;
			ArrayList<Buff> buffList = new ArrayList<Buff>();
			if (player.getActiveBuffs() != null) {
				for (Buff b : player.getActiveBuffs()) {
					if (b != null && b.isSyncWithClient()) {
						buffList.add(b);
					}
				}
			}
			// 发送腾讯信息协议
//			TengXunDataManager.instance.sendTengXunMsg(player);

			if (EnterLimitManager.isGetPlayerProcess) {
				Player_Process process = EnterLimitManager.player_process.get(player.getId());
				if (process != null) {
					EnterLimitManager.player_process.remove(player.getId());
				}
				PlayerRecordData reData = EnterLimitManager.getPlayerRecordData(player.getId());
				if (reData != null) {
					reData.obj7 = null;
				}
				EnterLimitManager.getInstance().NO_PROCESS_REQNUM.remove(player.getId());
				ANDROID_PROCESS_RES res = new ANDROID_PROCESS_RES(GameMessageFactory.nextSequnceNum());
				player.addMessageToRightBag(res);
			}

			if (EnterLimitManager.isCompareRSA) {
				PlayerRecordData reData = EnterLimitManager.getPlayerRecordData(player.getId());
				if (reData != null) {
					reData.obj1 = null;
					reData.obj6 = null;
				}
				EnterLimitManager.getInstance().NO_RSA_REQNUM.remove(player.getId());
				GET_RSA_DATA_RES res = new GET_RSA_DATA_RES(GameMessageFactory.nextSequnceNum());
				player.addMessageToRightBag(res);
			}

			{
				// 发送 绑定手机号开关
				PhoneNumMananger.getInstance().sendOpenMsg(player);
			}

			{
				// 发送获取android信息
				try {
					if (!AndroidMsgManager.unGetAndroidMsgServerNames.contains(GameConstants.getInstance().getServerName())) {
						AndroidMsgManager.getInstance().sendGetMac(player);
					}
				} catch (Exception e) {
					logger.error("发送检查出错:", e);
				}
			}

			{
				// 发送周活动信息
				try {
					NewChongZhiActivityManager.instance.sendWeekActivityState(player);
				} catch (Exception e) {
					logger.error("发送周活动信息出错", e);
				}
			}

			PLAYER_ENTER_RES res = new PLAYER_ENTER_RES(req.getSequnceNum(), (byte) 0, "", buffList.toArray(new Buff[0]), version1, version2, spriteTypes, player.getSouls(), Soul.switchCd, switchLeft, player.每天可以使用的绑银上限());
			int validateState = OtherValidateManager.getInstance().getValidateState(player, OtherValidateManager.ASK_TYPE_PLAYER_ENTER);
			if (validateState == OtherValidateManager.VALIDATE_STATE_需要答题) {
				ValidateAsk ask = OtherValidateManager.getInstance().sendValidateAsk(player, OtherValidateManager.ASK_TYPE_PLAYER_ENTER);
				ask.setAskFormParameters(new Object[] { res });
				return null;
			}
			if (logger.isInfoEnabled()) logger.info("[] [] [] [PLAYER_ENTER_RES]", new Object[] { player.getUsername(), player.getId(), player.getName() });
			if (logger.isWarnEnabled()) logger.warn("[玩家进入游戏] [成功] [pid:{}] [当前排队的用户数为：{}] [{}:{}] [{}:{}:{}]", new Object[] { playerId, waitingEnterGameConnections.size(), gameName, version2, (conn != null ? conn.getIdentity() : "nul"), (conn != null ? conn.getName() : "nul"), (conn != null ? conn.getRemoteAddress() : "nul") });
			try {
				// System.out.println("PLAYER_PROPERTY_MAX_VALUE_INFO_REQ111");
				PLAYER_PROPERTY_MAX_VALUE_INFO_REQ ppmvir = new PLAYER_PROPERTY_MAX_VALUE_INFO_REQ(GameMessageFactory.nextSequnceNum(), player.getCareer(), PlayerManager.各个职业各个级别的所有战斗属性理论最大数值[player.getCareer()][player.getLevel() - 1]);
				player.addMessageToRightBag(ppmvir);
				// System.out.println("PLAYER_PROPERTY_MAX_VALUE_INFO_REQ222");
			} catch (Exception ex) {
				if (logger.isWarnEnabled()) logger.warn("[玩家进入游戏] [异常] [{}] [{}] [{}]", new Object[] { playerId, player.getUsername(), player.getName() }, ex);
			}
			return res;
		} else {
			if (logger.isWarnEnabled()) logger.warn("[玩家进入游戏] [失败] [找不到id对应的角色] [{}] [当前排队的用户数为：{}]", new Object[] { playerId, waitingEnterGameConnections.size() });
			PLAYER_ENTER_RES res = new PLAYER_ENTER_RES(req.getSequnceNum(), (byte) 1, Translate.找不到对应的角色, new Buff[0], 0, 0, new short[0], new Soul[0], 0, 0, PlayerManager.每人每天可以使用绑银);
			return res;
		}

	}
	
	private String buildParams(Map<String,String> params){
		String[] key = params.keySet().toArray(new String[params.size()]);
		StringBuffer sb = new StringBuffer();
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(params.get(k));
			sb.append("&");
		}
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	public void sendInfoToLeHiHi(Player player,int op){
		try {
//			1：登录；2：升级；3：登出。
			long startTime = System.currentTimeMillis();
			if(player == null || player.getPassport() == null || !player.getPassport().getRegisterChannel().equals("LEHAIHAIAPPSTORE_XUNXIAN")){
				return;
			}
			
			LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
			params.put("username",player.getUsername());
			params.put("token",player.getPassport().getPassWd());
			params.put("serverid","1");
			params.put("servername",GameConstants.getInstance().getServerName());
			params.put("role_id","0");
			params.put("role_name",player.getName());
			params.put("pid","14314");
			params.put("game_level",player.getLevel()+"");
			params.put("op",op+"");
			params.put("client_type","ios");
			params.put("time",(System.currentTimeMillis()/1000)+"");
			
//			str1=client_type+game_level+op+pid+role_id+role_name+serverid+servername+time+token+username\
			String str1 = params.get("client_type")+params.get("game_level")+params.get("op")+params.get("pid")+params.get("role_id")+
					params.get("role_name") + params.get("serverid") + params.get("servername") + params.get("time") + params.get("token") + params.get("username") +
					"83580300BEB790E69AEC915B330395BE";
			String mysign = StringUtil.hash(java.net.URLEncoder.encode(str1));
			params.put("sign",mysign);
			
			String paramString = buildParams(params);
			HashMap headers = new HashMap();
			String content =  "";
			try {
				byte bytes[] = HttpUtils.webPost(new java.net.URL("http://sdk-nscs.btgame01.com/index.php/User/game_user"), paramString.getBytes("utf-8"), headers, 60000, 60000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				content = new String(bytes,encoding);
				
				JacksonManager jm = JacksonManager.getInstance();
				Map map =(Map)jm.jsonDecodeObject(content);
				Integer resultState = (Integer)map.get("state");
				if(resultState == null || resultState != 1){
					logger.info("[给乐嗨嗨发送统计数据] [失败:返回数据状态不对] [queryStr:"+str1+"] [message:"+content+"] [paramString:"+paramString+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
					return;
				}
				logger.info("[给乐嗨嗨发送统计数据] [成功] [op:"+op+"] ["+player.getLogString()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("[给乐嗨嗨发送统计数据] [失败] ["+player.getLogString()+"] [op:"+op+"] [str1:"+str1+"] ["+content+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[给乐嗨嗨发送统计数据] [异常] ["+(player == null?"":player.getLogString())+"]",e);
		}
	}
	
	public static boolean errorGame = true;

	public boolean update(Passport passport) {
		return bossClientService.update(passport);
	}

	@Override
	public String getMConsoleName() {
		return "coreSubsystem";
	}

	@Override
	public String getMConsoleDescription() {
		return "coreSubsystem";
	}
}