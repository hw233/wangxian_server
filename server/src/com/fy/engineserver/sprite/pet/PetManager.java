package com.fy.engineserver.sprite.pet;

import static com.fy.engineserver.datasource.language.Translate.合体确定;
import static com.fy.engineserver.datasource.language.Translate.宠物朱雀;
import static com.fy.engineserver.datasource.language.Translate.宠物玄武;
import static com.fy.engineserver.datasource.language.Translate.宠物白虎;
import static com.fy.engineserver.datasource.language.Translate.宠物青龙;
import static com.fy.engineserver.datasource.language.Translate.宠物麒麟;
import static com.fy.engineserver.datasource.language.Translate.强于主宠;
import static com.fy.engineserver.datasource.language.Translate.服务器东海龙宫;
import static com.fy.engineserver.datasource.language.Translate.服务器云波鬼岭;
import static com.fy.engineserver.datasource.language.Translate.服务器云海冰岚;
import static com.fy.engineserver.datasource.language.Translate.服务器国内本地测试;
import static com.fy.engineserver.datasource.language.Translate.服务器太华神山;
import static com.fy.engineserver.datasource.language.Translate.服务器峨嵋金顶;
import static com.fy.engineserver.datasource.language.Translate.服务器无极冰原;
import static com.fy.engineserver.datasource.language.Translate.服务器昆仑圣殿;
import static com.fy.engineserver.datasource.language.Translate.服务器昆华古城;
import static com.fy.engineserver.datasource.language.Translate.服务器桃源仙境;
import static com.fy.engineserver.datasource.language.Translate.服务器炼狱绝地;
import static com.fy.engineserver.datasource.language.Translate.服务器燃烧圣殿;
import static com.fy.engineserver.datasource.language.Translate.服务器玉幡宝刹;
import static com.fy.engineserver.datasource.language.Translate.服务器瑶台仙宫;
import static com.fy.engineserver.datasource.language.Translate.服务器百里沃野;
import static com.fy.engineserver.datasource.language.Translate.服务器福地洞天;
import static com.fy.engineserver.datasource.language.Translate.服务器蓬莱仙阁;
import static com.fy.engineserver.datasource.language.Translate.服务器问天灵台;
import static com.fy.engineserver.datasource.language.Translate.服务器风雪之巅;
import static com.fy.engineserver.datasource.language.Translate.服务器鹊桥仙境;
import static com.fy.engineserver.datasource.language.Translate.服务器黄金海岸;
import static com.fy.engineserver.datasource.language.Translate.未鉴定;
import static com.fy.engineserver.datasource.language.Translate.辅宠;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.activity.vipExpActivity.VipExpActivityManager;
import com.fy.engineserver.animation.AnimationManager;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.PetExperienceManager;
import com.fy.engineserver.core.PetSubSystem;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.PetFoodArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.LastingForPetProps;
import com.fy.engineserver.datasource.article.data.props.PetEggProps;
import com.fy.engineserver.datasource.article.data.props.PetProps;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.data.props.SingleForPetProps;
import com.fy.engineserver.datasource.article.data.props.VariationPetProps;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventPlayerDropPet;
import com.fy.engineserver.event.cate.EventPlayerGainPet;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.petHouse.PetHouseManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_PetMergeConfirm;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.pet.Option_TakeOffOrna;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.PET2_LiZi_RES;
import com.fy.engineserver.message.PET2_QUERY_RES;
import com.fy.engineserver.message.PET_FLY_ANIMATION_RES;
import com.fy.engineserver.message.PET_MERGE_REQ;
import com.fy.engineserver.message.PET_MERGE_RES;
import com.fy.engineserver.message.PET_QUERY_REQ;
import com.fy.engineserver.message.PET_QUERY_RES;
import com.fy.engineserver.message.PET_REPAIR_RES;
import com.fy.engineserver.message.PET_STRONG_REQ;
import com.fy.engineserver.message.PET_STRONG_RES;
import com.fy.engineserver.message.PLAY_ANIMATION_REQ;
import com.fy.engineserver.message.QUERY_ARTICLE_INFO_RES;
import com.fy.engineserver.message.QUERY_PET_FEED_RES;
import com.fy.engineserver.message.QUERY_PET_MERGE_REQ;
import com.fy.engineserver.message.QUERY_PET_MERGE_RES;
import com.fy.engineserver.message.QUERY_PET_STRONG_REQ;
import com.fy.engineserver.message.QUERY_PET_STRONG_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticle;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticleEntity;
import com.fy.engineserver.sprite.pet2.GradePet;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.fy.engineserver.sprite.pet2.Pet2PropCalc;
import com.fy.engineserver.sprite.pet2.Pet2SkillCalc;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ProbabilityUtils;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.vip.VipManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.transport.RequestMessage;

/**
 * 宠物管理器
 * 
 * 
 */
public class PetManager {

	public static Logger logger = LoggerFactory.getLogger(PetManager.class.getName());
	
	/**
	 * 宠物相关统一日志格式
	 * logger.warn("[操作:{}] [结果:{}] [操作人:{}] [宠物信息:{}] [耗时:{}]",new Object[]{"使用宠物蛋","成功",{username,pid,pname,level,vipLevel},{petid,petname},200ms});
	 * "[宠物蛋孵化] [成功] [玩家id:{}] [玩家角色:{}] [玩家账号:{}] [玩家等级:{}] [玩家VIP等级:{}] [物品名:{}] [物品id:{}] [宠物名:{}] [宠物id:{}] [携带等级:{}] [宠物代数:{}] [宠物星级:{}] [耗时:{}ms]", 
	 * new Object[]{player.getId(),player.getName(),player.getUsername(),player.getLevel(),player.getVipLevel(), aee.getArticleName(), eggId,pet.getName(),pet.getId(),pet.getTrainLevel(),pet.getGeneration(),pet.getStarClass(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start}
	 */
	
	public static SimpleEntityManager<Pet> em;
	public static SimpleEntityManager<PetFlyState> em_state;
	public static final byte 随处可见 = 0;
	public static final byte 百里挑一 = 1;
	public static final byte 千载难逢 = 2;
	public static final byte 万年不遇 = 3;
	public static int 宠物最大名字 = 18;
	public static boolean openTestLog = true;

	public static int[] 刷新对应必升个数 = { 2, 4, 6 };

	public static final ReentrantLock lock = new ReentrantLock();
	public static int 宠物大于人物等级 = 10;

	public static String[] 炼妖石names = { Translate.初级炼妖石, Translate.中级炼妖石, Translate.高级炼妖石 };
	public static int[] 炼妖花费 = { 10, 20, 30 };

	static {
		if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			炼妖花费 = new int[] { 100, 200, 300 };
		}
	}

	public long[] 炼妖石ids = null;

	int maxHappyness = 1000;
	int maxLifeTime = 1000;
	public static int 疲劳快乐值 = 0;

	protected static PetManager self;

	boolean isStart;
	
	// 孵化所需金币
	public int 孵化所需金币两 = 0;
	public int 孵化所需金币文 = 0;
	// public int 鉴定需要绑银两 = 10;
	// public int 鉴定需要绑银文 = 10*1000;
	public int 强化所需绑银两 = 10;
	public int 强化所需绑银文 = 10 * 1000;
	// 显示
	// public int 炼妖需要银子两 = 10;
	// public int 炼妖需要银子文 = 10 * 1000;

	public static int 宠物最大技能数 = 10;

	public static final String[] 跟随模式 = { Translate.跟随模式被动式, Translate.跟随模式主动式, Translate.跟随模式跟随式 };

	public LruMapCache mCache = new LruMapCache(32 * 1024 * 1024, 60 * 60 * 1000);

	public List<PetFlyState> states = Collections.synchronizedList(new ArrayList<PetFlyState>());
	public static List<PetEatPropRule> eatRules = new ArrayList<PetEatPropRule>();
	public static List<PetEatProp2Rule> eat2Rules = new ArrayList<PetEatProp2Rule>();
	public static Map<Integer,PetFlySkillInfo> petFlySkills = new LinkedHashMap<Integer,PetFlySkillInfo>();
	private List<PetCellInfo> cellInfos = new ArrayList<PetCellInfo>();
	
	public static PetManager getInstance() {
		return self;
	}

	public static final byte 公 = 0;
	public static final byte 母 = 1;
	public static final String[] 宠物性别 = new String[] { Translate.性别雄, Translate.性别雌 };

	public static String 得到宠物性别名(int sex) {
		if (sex >= 0 && sex < 宠物性别.length) {
			return 宠物性别[sex];
		}
		return null;
	}

	public static final String[] 宠物职业 = new String[] { Translate.宠物职业外攻型, Translate.宠物职业内攻型, Translate.宠物职业敏捷型, Translate.宠物职业毒伤型 };

	public static String 得到宠物职业名(int category) {
		if (category >= 0 & category < 宠物职业.length) {
			return 宠物职业[category];
		}
		return null;
	}

	public static final String[] 宠物性格 = new String[] { Translate.宠物性格忠诚, Translate.宠物性格精明, Translate.宠物性格谨慎, Translate.宠物性格狡诈 };

	public static String 得到宠物性格名(int category) {
		if (category >= 0 & category < 宠物性格.length) {
			return 宠物性格[category];
		}
		return null;
	}

	public static final String[] 宠物品质 = new String[] { Translate.宠物品质普品, Translate.宠物品质灵品, Translate.宠物品质仙品, Translate.宠物品质神品, Translate.宠物品质圣品 };
	public static final String[] 宠物新品质 = new String[] { Translate.宠物品质凡兽, Translate.宠物品质灵兽, Translate.宠物品质仙兽, Translate.宠物品质神兽, Translate.宠物品质圣兽 };

	public static String 得到宠物品质名(boolean isIdentity, int category) {
		if (!isIdentity) {
			return 未鉴定;
		}
		if (category >= 0 & category < 宠物品质.length) {
			return 宠物品质[category];
		}
		return null;
	}

	public static final String[] 宠物品质颜色 = new String[] { "0xffffff", "0x00ff00", "0x0000ff", "0xE706EA", "0xFDA700" };

	public static String 得到宠物品质颜色值(boolean isIdentity, int category) {
		if (!isIdentity) {
			return "0xffffff";
		}
		if (category >= 0 & category < 宠物品质颜色.length) {
			return 宠物品质颜色[category];
		}
		return "0xffffff";
	}

	public static final String[] 宠物稀有度 = new String[] { Translate.宠物稀有度随处可见, Translate.宠物稀有度百里挑一, Translate.宠物稀有度千载难逢, Translate.宠物稀有度万年不遇 };

	public static String 得到宠物稀有度名(int category) {
		if (category >= 0 & category < 宠物稀有度.length) {
			return 宠物稀有度[category];
		}
		return null;
	}

	public static final String[] 宠物稀有度颜色 = new String[] { "0xffffff", "0x0000ff", "0xFDA700", "0xFDA700" };

	public static String 得到宠物稀有度颜色值(int category) {
		if (category >= 0 & category < 宠物稀有度颜色.length) {
			return 宠物稀有度颜色[category];
		}
		return "0xffffff";
	}

	public static final String[] 宠物成长品质 = new String[] { Translate.成长品质普通, Translate.成长品质一般, Translate.成长品质优秀, Translate.成长品质卓越, Translate.成长品质完美 };

	public static String 得到宠物成长品质名(boolean isIdentity, int category) {
		if (!isIdentity) {
			return 未鉴定;
		}
		if (category >= 0 & category < 宠物成长品质.length) {
			return 宠物成长品质[category];
		}
		return null;
	}

	public static final String[] 宠物品阶 = new String[] { Translate.宠物原始, Translate.坐骑一阶, Translate.坐骑二阶, Translate.坐骑三阶, Translate.坐骑四阶, Translate.坐骑五阶, Translate.坐骑六阶, Translate.宠物终阶 };

	public static final String[] 宠物成长品质颜色 = new String[] { "0xffffff", "0x00ff00", "0x0000ff", "0xE706EA", "0xFDA700" };

	public static String 得到宠物成长品质颜色值(boolean isIdentity, int category) {
		if (!isIdentity) {
			return "0xffffff";
		}
		if (category >= 0 & category < 宠物成长品质颜色.length) {
			return 宠物成长品质颜色[category];
		}
		return "0xffffff";
	}

	private String fileName;

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// {携带等级
	// {性格，技能Ids}} 忠诚 精明 谨慎 狡诈 (0,1,2,3)
	private Map<Integer, Map<Integer, List<Integer>>> petSkillMap = new HashMap<Integer, Map<Integer, List<Integer>>>();

	private Map<Integer, PetSkillReleaseProbability> map = new HashMap<Integer, PetSkillReleaseProbability>();

	public static int 性格c = 0;
	public static int 携带等级c = 1;
	public static int 技能idc = 2;
	public static int 性格对应攻击c = 3;
	public static int 非性格对应攻击c = 4;
	public static int 性格对应buffc = 5;
	public static int 非性格对应buffc = 6;

	public void loadPetSkill() throws Exception {

		File file = new File(fileName);
		HSSFWorkbook workbook = null;

		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = null;
		sheet = workbook.getSheetAt(0);
		if (sheet == null) return;
		int rows = sheet.getPhysicalNumberOfRows();

		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row != null) {

				String 性格 = row.getCell(性格c).getStringCellValue().trim();
				int 携带等级 = (int) row.getCell(携带等级c).getNumericCellValue();
				int 技能id = (int) row.getCell(技能idc).getNumericCellValue();

				double 性格对应攻击 = -1;
				double 非性格对应攻击 = -1;
				double 性格对应buff = -1;
				double 非性格对应buff = -1;

				HSSFCell cell = row.getCell(性格对应攻击c);
				if (cell != null) {

					性格对应攻击 = cell.getNumericCellValue();

					if (性格对应攻击 > 0) {
						// 是技能
						cell = row.getCell(非性格对应攻击c);
						非性格对应攻击 = cell.getNumericCellValue();
						if (非性格对应攻击 > 0) {

						} else {
							throw new RuntimeException("宠物技能配置错误,line:" + r + "col" + 非性格对应攻击c);
						}

					} else {
						// 是buff
						cell = row.getCell(性格对应buffc);
						性格对应buff = cell.getNumericCellValue();
						if (性格对应buff > 0) {
							cell = row.getCell(非性格对应buffc);
							非性格对应buff = cell.getNumericCellValue();
							if (非性格对应buff > 0) {

							} else {
								throw new RuntimeException("宠物技能配置错误,line:" + r + "col" + 非性格对应攻击c);
							}
						} else {
							throw new RuntimeException("宠物技能配置错误,line:" + r + "col" + 性格对应buffc);
						}

					}
				} else {
					throw new RuntimeException("宠物技能配置错误,line:" + r + "col" + 性格对应攻击c);
				}

				Map<Integer, List<Integer>> map2 = petSkillMap.get(携带等级);
				if (map2 == null) {
					map2 = new HashMap<Integer, List<Integer>>();
					petSkillMap.put(携带等级, map2);
				}

				// 忠诚 精明 谨慎 狡诈 (0,1,2,3)
				int character = -1;
				if (性格.equals(Translate.忠诚)) {
					character = 0;
					List<Integer> list = map2.get(0);
					if (list == null) {
						list = new ArrayList<Integer>();
						list.add(技能id);
						map2.put(0, list);
					} else {
						list.add(技能id);
					}
				} else if (性格.equals(Translate.精明)) {
					character = 1;
					List<Integer> list = map2.get(1);
					if (list == null) {
						list = new ArrayList<Integer>();
						list.add(技能id);
						map2.put(1, list);
					} else {
						list.add(技能id);
					}
				} else if (性格.equals(Translate.谨慎)) {
					character = 2;
					List<Integer> list = map2.get(2);
					if (list == null) {
						list = new ArrayList<Integer>();
						list.add(技能id);
						map2.put(2, list);
					} else {
						list.add(技能id);
					}
				} else if (性格.equals(Translate.狡诈)) {
					character = 3;
					List<Integer> list = map2.get(3);
					if (list == null) {
						list = new ArrayList<Integer>();
						list.add(技能id);
						map2.put(3, list);
					} else {
						list.add(技能id);
					}
				} else {
					throw new RuntimeException("宠物技能性格配置错误[性格:" + 性格 + "] [{" + Translate.忠诚 + "}{" + Translate.精明 + "}{" + Translate.谨慎 + "}{" + Translate.狡诈 + "}],line:" + r);
				}

				PetSkillReleaseProbability pp = new PetSkillReleaseProbability();
				pp.setSkillId(技能id);
				pp.setCharacter(character);
				if (性格对应攻击 > 0 && 非性格对应攻击 > 0) {
					pp.setMatch((int) (性格对应攻击 * 1000));
					pp.setNoMatch((int) (非性格对应攻击 * 1000));
				} else if (性格对应buff > 0 && 非性格对应buff > 0) {
					pp.setMatch((int) (性格对应buff * 1000));
					pp.setNoMatch((int) (非性格对应buff * 1000));
				} else {
					throw new RuntimeException("宠物技能配置错误,line:" + r);
				}

				if (map.get(技能id) != null) {
					PetManager.logger.error("宠物技能配置错误,技能有宠物" + 技能id);
				} else {
					map.put(技能id, pp);
				}
			}
		}

	}

	/**
	 * 宠物初始化
	 */
	public void init() throws Exception {
		
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		this.loadPetSkill();
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Pet.class);
		em_state = SimpleEntityManagerFactory.getSimpleEntityManager(PetFlyState.class);

		得到炼妖的三种石头Ids();
		if (PetManager.logger.isWarnEnabled()) {
			PetManager.logger.warn("[petManager初始化成功] []");
		}
		startNoticePetDigestEnd();
		ServiceStartRecord.startLog(this);
	}

	/**
	 * 虚拟机关闭时调用
	 */
	public void destroy() {

		em.destroy();
		em_state.destroy();
		logger.warn("[petManager destory]");
	}

	public boolean open = true;

	/**
	 * 创建一个宠物
	 * @param pet
	 * @return
	 */
	public Pet createPet(Pet pet) throws Exception {
		long id = em.nextId();
		pet.setId(id);
		mCache.put(pet.getId(), pet);
		em.notifyNewObject(pet);
		if (logger.isWarnEnabled()) logger.warn("[创建宠物] [{}]", new Object[] { (pet == null ? "NULL" : pet.getLogString()) });
		return pet;
	}
	
	public void savePetFlyState(PetFlyState state,long pid,Player player){
		try{ 
			boolean isNewObject = false;
			if(state.getId() <= 0){
				isNewObject = true;
				state.setId(pid);
				em_state.notifyNewObject(state);
				states.add(state);
			}else{
				em_state.save(state);
				for(PetFlyState s : PetManager.getInstance().states){
					if(s != null){
						if(s.getId() == state.getId()){
							s.setPointRecord(state.getPointRecord());
							s.setAnimation(state.getAnimation());
							s.setTempPoint(state.getTempPoint());
							s.setFlyState(state.getFlyState());
							s.setSkillId(state.getSkillId());
							s.setLingXingPoint(state.getLingXingPoint());
							s.setHistoryLingXingPoint(state.getHistoryLingXingPoint());
							s.setQianNengPoint(state.getQianNengPoint());
							s.setUseTimes(state.getUseTimes());
							s.setEatNums(state.getEatNums());
							s.setXiaoHuaDate(state.getXiaoHuaDate());
						}
					}
				}
			}
			if(logger.isWarnEnabled()){
				logger.warn("[创建宠物飞升状态] [完成] [{}] [总数:{}] [宠物id:{}] [玩家:{}] [信息:{}]",new Object[]{(isNewObject?"创建新数据":"更新数据"),states.size(), pid,(player==null?"nul":player.getLogString()),state});
			}
		}catch(Exception e){
			e.printStackTrace();
			if(logger.isWarnEnabled()){
				logger.warn("[创建宠物飞升状态] [异常] [总数:{}] [宠物id:{}] [玩家:{}] [信息:{}] [{}]",new Object[]{states.size(),pid,player.getLogString(),state,e});
			}
		}
	}
	
	
	public PetFlyState getPetFlyState(long petId,String reason){
		List<PetFlyState> slist = states;
		for(PetFlyState s : slist){
			if(s != null && s.getId() == petId){
				return s;
			}
		}
		
		try {
			PetFlyState state = em_state.find(petId);
			if(logger.isWarnEnabled()){
				logger.warn("[查询宠物飞升状态数据] [成功] [总数:{}] [宠物id:{}] [reason:{}]",new Object[]{states.size(),petId,reason});
			}
			if(state != null){
				states.add(state);
				return state;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(logger.isWarnEnabled()){
				logger.warn("[查询宠物飞升状态数据] [异常] [总数:{}] [宠物id:{}] [reason:{}] [{}]",new Object[]{states.size(),petId,reason,e});
			}
		}
		return null;
	}
	
	class NoticePetDigestEnd implements Runnable{

		@Override
		public void run() {
			while(isStart){
				try {
					Thread.sleep(5*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				noticePetDigestEnd();
			}
		} 
	}
	
	public void startNoticePetDigestEnd(){
		isStart = true;
		new Thread(new NoticePetDigestEnd(), "宠物易筋丹消化").start();
	}
	
	public void endNoticePetDigestEnd(Player player){
		isStart = false;
	}
	public static void popConfirmWindow2(Player p, Pet pet, long ornaId , long oldOrnaId) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_TakeOffOrna option1 = new Option_TakeOffOrna();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setPetId(pet.getId());
		option1.setOrnaId(ornaId);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(ornaId);
		String str = String.format(Translate.脱饰品确认, pet.getName(), ae.getArticleName());
		if (oldOrnaId > 0) {
			ArticleEntity ae2 = ArticleEntityManager.getInstance().getEntity(oldOrnaId);
			str = String.format(Translate.脱饰品确认2, ae.getArticleName(), ae2.getArticleName(), ae2.getArticleName());
		}
		mw.setDescriptionInUUB(str);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	/**
	 * 宠物穿饰品(装备脱下就消失！！)
	 * @param player
	 * @param petId
	 * @param ornaId  饰品id
	 * @return
	 */
	public String putOnOrnaments(Player player, long petId, long ornaId, boolean confirm) {
		try {
			if (ornaId <= 0) {
				return "";
			}
			Knapsack petKnap = player.getPetKnapsack();
			Pet pet = this.getPet(petId);
			if (pet == null) {
				return "宠物不存在";
			}
			ArticleEntity petAe = ArticleEntityManager.getInstance().getEntity(pet.getPetPropsId());
			if (petAe == null || !petKnap.contains(petAe)) {
				return "宠物道具不在玩家背包内";
			}
			if (player.getActivePetId() == petId) {
				return Translate.出站宠物不可做此操作;
			}
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(ornaId);
			PetSuitArticle paa = null;
			if (ae instanceof PetSuitArticleEntity) {
				paa = (PetSuitArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
			} else {
				return "不是宠物饰品";
			}
			if (pet.getLevel() < paa.getPetLevelLimit()) {
				return Translate.等级不足;
			}
			if (!confirm) {		//需要二次确认
				popConfirmWindow2(player, pet, ornaId, pet.getOrnaments()[0]);
				return "";
			}
			synchronized (player.tradeKey) {
				Knapsack bag = player.getKnapsack_common();
				if (!bag.contains(ae)) {
					return Translate.物品不存在;
				}
				ArticleEntity resultAe = bag.removeByArticleId(ae.getId(), "宠物穿饰品", false);
				if (resultAe == null) {
					return Translate.删除物品不成功;
				}
				long oldOrnaId = pet.getOrnaments()[0];
				pet.getOrnaments()[0] = ae.getId();
				em.notifyFieldChange(pet, "ornaments");
				logger.warn("[宠物穿饰品] [成功] [" + player.getLogString() + "] [petId:" + petId + "] [oldOrnaId:" + oldOrnaId + "] [newId:" + pet.getOrnaments()[0] + "]");
				/*if (oldOrnaId > 0) {
					ArticleEntity oldAe = ArticleEntityManager.getInstance().getEntity(oldOrnaId);
					if (oldAe != null) {
						bag.put(oldAe, "宠物脱饰品");
						logger.warn("[宠物脱饰品] [成功] [" + player.getLogString() + "] [petId:" + petId + "] [oldOrnaId:" + oldOrnaId + "]");
					}
				}*/
			}
			pet.init();
			long seq = GameMessageFactory.nextSequnceNum();
			PET_QUERY_REQ oldReq = new PET_QUERY_REQ(seq, pet.getId());
			PET_QUERY_RES oldRes = PetSubSystem.getInstance().handle_PET_QUERY_REQ(null, oldReq, player);
			PET2_QUERY_RES res = Pet2Manager.getInst().makeNewPetQueryRes(seq, oldRes);
			PET2_LiZi_RES ret = Pet2Manager.getInst().makePetInfoWithLizi(pet, seq, res);
			player.addMessageToRightBag(ret);
			return null;
		} catch (Exception e) {
			logger.warn("[宠物穿灵饰] [异常] [" + player.getLogString() + "] [petId:" + petId + "] [ornaId:" + ornaId + "]", e);
			return "error"; 
		}
	}
	
	/**
	 * 通知宠物易筋丹消化完成
	 */
	public void noticePetDigestEnd(){
		Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
		for(Player player : ps){
			if(player != null && player.getPetKnapsack() != null){
				Cell cells[] = player.getPetKnapsack().getCells();
				if(cells != null){
					for(Cell cell : cells){
						if(cell != null && !cell.isEmpty()){
							ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
							if(ae != null && ae instanceof PetPropsEntity){
								PetPropsEntity pe = (PetPropsEntity)ae;
								if(pe.getPetId() <= 0){
									continue;
								}
								Pet pet = PetManager.getInstance().getPet(pe.getPetId());
								if(pet != null){
									PetFlyState state = PetManager.getInstance().getPetFlyState(pet.getId(), player);
									if(state != null){
										int getQianNengValue = 0;
										if(state.getXiaoHuaDate() > 0 && state.getXiaoHuaDate() <= System.currentTimeMillis()){
											state.setLingXingPoint(state.getLingXingPoint()+10);
											try {
												if (state.getLingXingPoint() == 50) {
													//通知飞升动画
														String nowAvata = "";
														if(pet.getAvata() != null && pet.getAvata().length > 0){
															nowAvata = pet.getAvata()[0];
														}
														String flyAvata = "";
														ArticleEntity artE = player.getArticleEntity(pet.getPetPropsId());
														if (artE != null && artE instanceof PetPropsEntity) {
															PetPropsEntity entity = (PetPropsEntity) artE;
															GradePet gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
															if(gradePet != null){
																flyAvata = gradePet.flyAvata;
																pet.setIcon(gradePet.flyIcon);
															}
														}
														PET_FLY_ANIMATION_RES res = new PET_FLY_ANIMATION_RES(GameMessageFactory.nextSequnceNum(), pet.getId(), pet.getName(),nowAvata, flyAvata);
														player.addMessageToRightBag(res);
													AchievementManager.getInstance().record(player, RecordAction.使用易筋丹使宠物灵性达到50);
												}
											} catch (Exception e) {
												PlayerAimManager.logger.warn("[目标系统] [统计宠物使用易筋丹50次数] [异常] [" + player.getLogString() + "]", e);
											}
//											if(state.getHistoryLingXingPoint() < state.getLingXingPoint()){
												state.setHistoryLingXingPoint(state.getLingXingPoint());
												state.setQianNengPoint(state.getQianNengPoint()+50);
												getQianNengValue = 50;
//											}
											state.setXiaoHuaDate(0);
											PetManager.getInstance().savePetFlyState(state, pet.getId(), player);
											if(getQianNengValue > 0){
												player.sendError(Translate.translateString(Translate.消耗易筋丹成功, new String[][] { { Translate.COUNT_1, String.valueOf(10) }, { Translate.COUNT_2, String.valueOf(50) } }));
											}else{
												player.sendError(Translate.translateString(Translate.消耗易筋丹成功2, new String[][] { { Translate.COUNT_1, String.valueOf(10) } }));
											}
											logger.warn("[玩家宠物道具易筋丹消化完毕] [{}] [{}]",new Object[]{state,player.getLogString()});
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public PetFlyState getPetFlyState(long petId,Player player){
		for(PetFlyState s : states){
			if(s != null && (s.getId() == petId)){
				return s;
			}
		}
		
		try {
			PetFlyState state = em_state.find(petId);
			if(state != null){
				states.add(state);
				return state;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(logger.isWarnEnabled()){
				logger.warn("[查询宠物飞升状态数据] [异常] [总数:{}] [宠物id:{}] [玩家:{}] [{}]",new Object[]{states.size(),petId,(player==null?"nul":player.getLogString()),e});
			}
		}
		return null;
	}

	/**
	 * 通过一个宠物蛋，创建一个孵化后的宠物
	 * @param props
	 * @return
	 */
	public Pet createPetByProps(PetEggProps props, PetProps petProps) throws Exception {
		int showTrainLevel = props.getArticleLevel();
		int trainLevel = props.getRealTrainLevel();
		int rarity = props.getRarity();

		Pet pet = new Pet();
		pet.createRandom(true, 0);
		pet.setTrainLevel(showTrainLevel);
		pet.setRealTrainLevel(trainLevel);
		pet.setMaxLevle(PetExperienceManager.getInstance().maxLevel);
		pet.setMaxHappyness(maxHappyness);
		pet.setMaxLifeTime(maxLifeTime);
		pet.setHappyness(maxHappyness);
		pet.setLifeTime(maxLifeTime);
		pet.setRarity((byte) rarity);

		String 种类 = props.get物品二级分类();
		pet.setPetSort(种类);
		pet.setCategory(props.getName());
		pet.setTitle(petProps.getTitle());
		pet.setAvataRace(petProps.getAvataRace());
		pet.setAvataSex(petProps.getAvataSex());
		boolean 是否变异 = PetPropertyUtility.随机获得一代是否变异(rarity);
		if (是否变异) {
			int 变异等级 = PetPropertyUtility.随机获得一代变异等级(rarity);
			pet.setVariation((byte) (变异等级 + 1));
		}
		pet.setLevel(1);
		pet.setCareer((byte) (props.getCareer() - 1));
		pet.setSex((byte) new java.util.Random().nextInt(2));
		// pet.setCharacter((byte)PetPropertyUtility.随机获得性格());
		pet.setCharacter((byte) props.getCharacter());
		// 一代宠
		int generation = 0;
		pet.setGeneration((byte) generation);
		if (rarity == 万年不遇) {
			pet.setGeneration((byte) 1);
			pet.setVariation((byte) 5);
			logger.debug("PetManager.createPetByProps: 强制万年不遇为 二代五变 {}", petProps.getName());
		}
		pet.petProps = petProps;
		pet.setName(petProps.getName());
		pet.setIdentity(false);
		// 产生技能
		pet.bindSkill();
		Pet2SkillCalc.getInst().calc1TalentSkill(pet, true);
		pet.init();
		pet.setHp(pet.getMaxHP());
		pet = this.createPet(pet);
		pet.initPetFlyAvata("宠物孵化");
		if (logger.isWarnEnabled()) logger.warn("[通过宠物蛋孵化宠物] [稀有度:" + rarity + "] [宠物蛋道具：" + props.getName() + "] [宠物道具：" + petProps.getName() + "] [" + (pet == null ? "NULL" : pet.getLogString()) + "]");
		// logger.info("[通过宠物蛋孵化宠物] [宠物蛋道具：{}] [宠物道具：{}] [{}]", new Object[]{props.getName(),petProps.getName(),(pet==null?"NULL":pet.getLogString())});
		return pet;
	}

	/**
	 * 通过一对父母宠繁殖产生一个二代宠
	 * @param props
	 * @return
	 */
	public Pet createPetByParent(Pet petA, Pet petB) throws Exception {

		int randomInt = random.nextInt(2);
		Pet temp = null;
		if (randomInt == 0) {
			temp = petA;
		} else {
			temp = petB;
		}

		int showTrainLevel = temp.getTrainLevel();
		int trainLevel = temp.getRealTrainLevel();
		int rarity = temp.getRarity();

		Pet pet = new Pet();
		pet.createRandom(true, 0);
		pet.setTrainLevel(showTrainLevel);
		pet.setRealTrainLevel(trainLevel);
		pet.setRarity((byte) rarity);
		boolean 是否变异 = PetPropertyUtility.随机获得二代是否变异(petA.getBreededTimes(), petB.getBreededTimes());
		if (是否变异) {
			int 变异等级 = PetPropertyUtility.随机获得二代变异等级(rarity);
			pet.setVariation((byte) (变异等级 + 1));
		}

		pet.setMaxLevle(petA.getMaxLevle());
		pet.setMaxHappyness(maxHappyness);
		pet.setMaxLifeTime(maxLifeTime);
		pet.setHappyness(maxHappyness);
		pet.setLifeTime(maxLifeTime);

		pet.setCategory(temp.getCategory());
		pet.setPetSort(temp.getPetSort());
		pet.setTitle(temp.getTitle());

		pet.setLevel(1);
		pet.setCareer((byte) (temp.getCareer()));
		pet.setSex(temp.getSex());
		// pet.setCharacter((byte)PetPropertyUtility.随机获得性格());
		pet.setCharacter(temp.getCharacter());
		// 成长度
		pet.setGrowthClass((byte) PetPropertyUtility.获得繁殖子宠物的成长率等级(petA, petB));

		// 二代宠
		int generation = 1;
		pet.setGeneration((byte) generation);
		pet.setIdentity(true);
		pet.setCategory(temp.getCategory());
		pet.setPetSort(temp.getPetSort());
		pet.setIdentity(true);
		if (temp.petProps != null) {
			pet.setAvataRace(temp.petProps.getAvataRace());
			pet.setAvataSex(temp.petProps.getAvataSex());
		} else {// 此处代码，繁殖后会复制进阶形象
			pet.setAvataRace(temp.getAvataRace());
			pet.setAvataSex(temp.getAvataSex());
		}
		// //为了孵化出的宠物气血是max的0.5
		// pet.setHp(-1);

		pet.bindSkill();

		int quality = PetPropertyUtility.判定宠物品质(pet, true);
		pet.setQuality((byte) quality);

		// 计算二代天生技能。
		Pet2SkillCalc.getInst().calc2TalentSkill(petA, petB, pet);
		pet.init();
		pet.setHp(pet.getMaxHP());
		pet = this.createPet(pet);

		if (logger.isWarnEnabled()) {
			logger.warn("[通过父母创建宠物] [" + pet.getLogString() + "] [稀有度:" + rarity + "] [母:" + temp.getLogString() + "]");
		}
		return pet;
	}

	/**
	 * 通过id从缓存得到宠物
	 * @param id
	 * @return
	 */
	public Pet getPetInMemory(long id) {
		Pet pet = (Pet) mCache.get(id);
		if (pet == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("[在缓存中没有找到宠物] [" + id + "]");
			}
		}
		return pet;
	}

	/**
	 * 通过id得到宠物
	 * @param id
	 * @return
	 */
	public Pet getPet(long id) {
		Pet pet = (Pet) mCache.get(id);
		if (pet == null) {
			try {
				pet = em.find(id);
			} catch (Exception e) {
				logger.error("[获得宠物异常] [petId:" + id + "]", e);
			}
			if (pet != null) {
				pet.init();
				mCache.put(pet.getId(), pet);
				if (logger.isDebugEnabled()) {
					logger.debug("[通过id载入宠物] [{}]", pet.getLogString());
				}
			} else {
				logger.error("[获得宠物错误] [没有宠物] [id:" + id + "]");
				// try {
				// throw new RuntimeException("数据库中没有找到宠物");
				// } catch (Exception e) {
				// logger.error("[获得宠物异常] [petId:" + id + "]", e);
				// }
			}
		} else {
			logger.debug("PetManager.getPet: find in cache {} {}", id, pet.getName());
		}
		return pet;
	}

	/**
	 * 宠物数据删除
	 * @param pet
	 */
	public void deletePet(Player player, Pet pet) {
		if (PetManager.logger.isWarnEnabled()) {
			PetManager.logger.warn("[玩家放生一个宠物] [{}] {}",new Object[]{player.getLogString4Knap(),pet.getLogOfInterest()});
		}
		EventPlayerDropPet evt = new EventPlayerDropPet(player);
		evt.pet = pet;
		EventRouter.getInst().addEvent(evt);
		this.mCache.remove(pet.getId());
		pet.setDelete(true);
	}

	// ///////////////////////////////////////////////////
	// 以下开始和宠物相关的游戏逻辑
	// //////////////////////////////////////////////////
	/**
	 * 孵化宠物
	 * 
	 * 孵化宠物需要2种道具，一是宠物蛋道具，其上记录了能孵出的宠物道具的名称
	 * 其次是对应的宠物道具，根据宠物道具名称找到对应的宠物道具，从而创建宠物
	 * 
	 */
	public Pet breedingPet(PropsEntity entity, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String result = "";
		if (player.getBindSilver() < 孵化所需金币文) {
			PetManager.logger.error("[宠物蛋孵化] [错误：玩家没有足够的金币] [" + player.getLogString() + "]");
			result = Translate.孵化失败没有足够的金币;
			player.sendError(result);
			return null;
		} else if (player.getPetKnapsack().isFull()) {

			PetManager.logger.error("[宠物蛋孵化] [错误：玩家宠物背包没有空格子] [" + player.getLogString() + "]");
			result = Translate.孵化失败宠物背包没有空格子;
			player.sendError(result);
			return null;

		} else {
			String articleName = entity.getArticleName();
			Article article = ArticleManager.getInstance().getArticle(articleName);
			if (article != null && article instanceof PetEggProps) {
				PetEggProps props = (PetEggProps) article;
				long petId = ((PetEggPropsEntity) entity).getPetId();
				if (petId > 0) { //封印过的蛋
					Pet	pet = PetManager.getInstance().getPet(petId);
					if(pet!=null){
						if(player.getLevel()<pet.getTrainLevel()){
							PetManager.logger.error("[宠物蛋孵化] [老蛋] [错误：玩家等级不够宠物的携带等级] [plevel:"+player.getLevel()+" ] [petlevel:"+pet.getTrainLevel()+"] [" + player.getLogString() + "]");
							result = Translate.孵化失败级别不够携带孵化的宠物;
							player.sendError(result);
							return null;
						}
					}
				}else{	//新蛋
					int trainLevel = props.getArticleLevel();
					// int trainLevel = props.getTrainLevel();
					if (player.getLevel() < trainLevel) {
						PetManager.logger.error("[宠物蛋孵化] [错误：玩家等级不够宠物的携带等级]  [plevel:"+player.getLevel()+" ] [trainLevel:"+trainLevel+"] [" + player.getLogString() + "]");
						result = Translate.孵化失败级别不够携带孵化的宠物;
						player.sendError(result);
						return null;
					}
				}
			} else {
				PetManager.logger.error("[宠物蛋孵化] [物品不存在或者物品不是宠物蛋物品] [" + player.getLogString() + "] [物品name:" + articleName + "]");
				result = Translate.孵化失败;
				player.sendError(result);
				return null;
			}
		}
		// 开始孵化，首先创建一个宠物道具
		String articleName = entity.getArticleName();
		Article article = ArticleManager.getInstance().getArticle(articleName);
		if (article != null && article instanceof PetEggProps) {
			if ("金宝蛇皇蛋".equals(article.getName_stat()) && player.getVipLevel() < 12) {
				player.sendError(Pet2Manager.getInst().getConfStr("vipPetLimit"));
				return null;
			}
			PetEggProps props = (PetEggProps) article;
			String petArticleName = props.getPetArticleName();
			Article petArticle = ArticleManager.getInstance().getArticle(petArticleName);
			if (petArticle instanceof PetProps) {
				try {
					PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager.getInstance().createEntity(petArticle, false, ArticleEntityManager.CREATE_REASON_PET_BREED, player, 0, 1, true);
					ppe.setEggArticle(props.getName());
					Pet pet = null;
					long petId = ((PetEggPropsEntity) entity).getPetId();
					if (petId > 0) {
						pet = PetManager.getInstance().getPet(petId);
						if (pet == null) {
							// logger.error("[再次孵化] [错误] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+entity.getArticleName()+"] ["+entity.getId()+"]");
							logger.error("[再次孵化] [错误] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), entity.getArticleName(), entity.getId() });
							return null;
						}
						if(pet.getTrainLevel()>player.getLevel()){
							player.sendError(Translate.角色等级不足);
							return null;
						}
					} else {
						// 其次，创建一个宠物实体
						pet = this.createPetByProps(props, (PetProps) petArticle);

						if (pet.getRarity() == 百里挑一) {
							AchievementManager.getInstance().record(player, RecordAction.宠物获得百里挑一次数);
							if (AchievementManager.logger.isWarnEnabled()) {
								AchievementManager.logger.warn("[成就统计宠物获得百里挑一次数] [" + player.getLogString() + "]");
							}
						} else if (pet.getRarity() == 千载难逢) {
							AchievementManager.getInstance().record(player, RecordAction.宠物获得千载难逢次数);
							if (AchievementManager.logger.isWarnEnabled()) {
								AchievementManager.logger.warn("[成就统计宠物获得千载难逢次数] [" + player.getLogString() + "]");
							}
						}

						pet.setName(petArticleName);

						AchievementManager.getInstance().record(player, RecordAction.孵化宠物次数);
						if (AchievementManager.logger.isWarnEnabled()) {
							AchievementManager.logger.warn("[成就统计孵化宠物次数] [" + player.getLogString() + "]");
						}

						// 不光是实力变态 获得一只变异宠物
						// 天才仙宠梦 获得一只5级变异宠物
						if (pet.getVariation() > 0) {
							AchievementManager.getInstance().record(player, RecordAction.宠物变异等级, pet.getVariation());
							if (AchievementManager.logger.isWarnEnabled()) {
								AchievementManager.logger.warn("[成就统计宠物变异等级] [" + player.getLogString() + "] [" + pet.getVariation() + "]");
							}
						}

						getPetAchievement(player, props);

					}

					ppe.setPetId(pet.getId());
					// ppe.setPetColorType(pet.getQuality());
					pet.setPetPropsId(ppe.getId());
					pet.setOwnerId(player.getId());
					pet.setCountry(player.getCountry());

					// 宠物孵化出来是被动攻击
					pet.setActivationType(0);

					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					// PetPropsEntity ppe = (PetPropsEntity)aem.getEntity(pet.getPetPropsId());
					ArticleManager am = ArticleManager.getInstance();

					Article art = am.getArticle(ppe.getArticleName());
					String icon = art.getIconId();
					if (icon != null) {
						pet.setIcon(icon);
					}
					if (logger.isWarnEnabled()) {
						logger.warn("[玩家孵化得到一个宠物] [" + player.getLogString() + "] [宠物:" + pet.getLogString() + "] [" + (pet.getGeneration() + 1) + "代" + pet.getVariation() + "变]");
					}
					int skillNum = 0;
					if (pet.getSkillIds() != null) {
						skillNum = pet.getSkillIds().length;
					}
					AchievementManager.getInstance().record(player, RecordAction.宠物技能个数, skillNum);
					if (AchievementManager.logger.isWarnEnabled()) {
						AchievementManager.logger.warn("[成就统计宠物技能个数] [" + player.getLogString() + "]");
					}

					pet.init();
					// 计算宠物排行榜积分
					pet.setPetScore();

					// 把宠物道具放入玩家的宠物背包
					player.putToKnapsacks(ppe, "宠物");
					ArticleStatManager.addToArticleStat(player, null, ppe, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "宠物孵化获得", null);
					// 添加或得宠物的事件。
					EventPlayerGainPet pge = new EventPlayerGainPet(player);
					pge.eggProp = props;
					pge.petArticle = (PetProps) petArticle;
					EventRouter.getInst().addEvent(pge);
					//
					return pet;
				} catch (Exception e) {
					logger.error("[孵化宠物] [异常] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [" + entity.getArticleName() + "] [" + entity.getId() + "]", e);
				}
			} else {
				// logger.error("[孵化宠物] [失败：宠物蛋配置的宠物物种有误] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+entity.getArticleName()+"] ["+entity.getId()+"] [宠物物种:"+petArticleName+"]",
				// start);
				logger.error("[孵化宠物] [失败：宠物蛋配置的宠物物种有误] [{}] [{}] [{}] [{}] [{}] [宠物物种:{}] [{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(), entity.getArticleName(), entity.getId(), petArticleName, petArticle.getClass(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		} else {
			// logger.error("[孵化宠物] [失败：宠物道具有误] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+entity.getArticleName()+"] ["+entity.getId()+"]", start);
			logger.error("[孵化宠物] [失败：宠物道具有误] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(), entity.getArticleName(), entity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
		}
		return null;
	}

	/**
	 * 孵化宠物
	 * 
	 * 纯复制breedingPet()方法,孵化出来的宠物不放入宠物栏,为使用获取变异宠物而写.
	 * 
	 */
	public Pet breedingVariationPet(PropsEntity entity, VariationPetProps vpprops, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String result = "";
		if (player.getPetKnapsack().isFull()) {

			PetManager.logger.error("[使用道具获得变异宠] [错误：玩家宠物背包没有空格子] [" + player.getLogString() + "]");
			result = Translate.孵化失败宠物背包没有空格子;
			player.sendError(result);
			return null;

		} else {
			String articleName = vpprops.getPetEggName();
			Article article = ArticleManager.getInstance().getArticle(articleName);
			if (article != null && article instanceof PetEggProps) {
				PetEggProps props = (PetEggProps) article;
				int trainLevel = props.getArticleLevel();
				// int trainLevel = props.getTrainLevel();
				if (player.getLevel() < trainLevel) {
					PetManager.logger.error("[使用道具获得变异宠] [错误：玩家等级不够宠物的携带等级] [" + player.getLogString() + "]");
					result = Translate.孵化失败级别不够携带孵化的宠物;
					player.sendError(result);
					return null;
				}
			} else {
				PetManager.logger.error("[使用道具获得变异宠] [物品不存在或者物品不是宠物蛋物品] [" + player.getLogString() + "] [物品name:" + articleName + "]");
				result = Translate.孵化失败;
				player.sendError(result);
				return null;
			}
		}
		// 开始孵化，首先创建一个宠物道具
		String articleName = vpprops.getPetEggName();
		Article article = ArticleManager.getInstance().getArticle(articleName);
		if (article != null && article instanceof PetEggProps) {
			if ("金宝蛇皇蛋".equals(article.getName_stat()) && player.getVipLevel() < 12) {
				player.sendError(Pet2Manager.getInst().getConfStr("vipPetLimit"));
				return null;
			}
			PetEggProps props = (PetEggProps) article;
			String petArticleName = props.getPetArticleName();
			Article petArticle = ArticleManager.getInstance().getArticle(petArticleName);
			if (petArticle instanceof PetProps) {
				try {
					PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager.getInstance().createEntity(petArticle, false, ArticleEntityManager.CREATE_REASON_PET_BREED, player, 0, 1, true);
					ppe.setEggArticle(props.getName());
					Pet pet = null;
					// long petId = ((PetEggPropsEntity) entity).getPetId();
					// if (petId > 0) {
					// pet = PetManager.getInstance().getPet(petId);
					// if (pet == null) {
					// logger.error("[再次孵化] [错误] [{}] [{}] [{}] [{}] [{}]", new Object[] { player.getId(), player.getName(), player.getUsername(), entity.getArticleName(),
					// entity.getId() });
					// return null;
					// }
					// } else {
					// 其次，创建一个宠物实体
					pet = this.createPetByProps(props, (PetProps) petArticle);
					pet.setName(petArticleName);
					pet.setGeneration(vpprops.getGeneration());
					pet.setVariation(vpprops.getVariation());
					if (vpprops.getTianshenSkill() != null && vpprops.getTianshenSkill().length > 0) {
						int[] tianshenSkLv = new int[vpprops.getTianshenSkill().length];
						Arrays.fill(tianshenSkLv, 1);
						pet.setSkillIds(vpprops.getTianshenSkill());
						if (vpprops.getTianshenLvs() == null || vpprops.getTianshenLvs().length != vpprops.getTianshenSkill().length) {
							pet.setActiveSkillLevels(tianshenSkLv);
						} else {
							pet.setActiveSkillLevels(vpprops.getTianshenLvs());
						}
					}
					if (vpprops.getTianfuSkill() != null && vpprops.getTianfuSkill().length > 0) {
						int[] tianfuLv = new int[vpprops.getTianfuSkill().length];
						Arrays.fill(tianfuLv, 1);
						pet.setTianFuSkIds(vpprops.getTianfuSkill());
//						if (vpprops.getTianfuLvs() == null || vpprops.getTianfuLvs().length != vpprops.getTianfuSkill().length) {
							pet.setTianFuSkIvs(tianfuLv);
//						} else {
//							pet.setTianFuSkIvs(vpprops.getTianfuLvs());
//						}
					}
					if (vpprops.isIsopenxiantian()) {
						pet.setTalent1Skill(((PetProps)petArticle).talent1skill);
					}
					if (vpprops.isIsopenhoutian()) {
						pet.setTalent2Skill(((PetProps)petArticle).talent2skill);
					}
					PetManager.logger.warn("[使用道具获得变异宠] [" + player.getLogString() + "] [强制宠物为" + (vpprops.getGeneration() + 1) + "代" + vpprops.getVariation() + "变] [基础技能:" + Arrays.toString(vpprops.getTianshenSkill()) + "] "
							+ "[等级:"+Arrays.toString(vpprops.getTianshenLvs())+"] [天赋技能:"
							+ Arrays.toString(vpprops.getTianfuSkill()) + "] [是否开先天:" + vpprops.isIsopenxiantian() + "] [是否开后台:" + vpprops.isIsopenhoutian() + "]");
					 
					// }

					ppe.setPetId(pet.getId());
					// ppe.setPetColorType(pet.getQuality());
					pet.setPetPropsId(ppe.getId());
					pet.setOwnerId(player.getId());
					pet.setCountry(player.getCountry());

					// 宠物孵化出来是被动攻击
					pet.setActivationType(0);

					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleManager am = ArticleManager.getInstance();

					Article art = am.getArticle(ppe.getArticleName());
					String icon = art.getIconId();
					if (icon != null) {
						pet.setIcon(icon);
					}
					if (logger.isWarnEnabled()) {
						logger.warn("[玩家孵化得到一个宠物] [" + player.getLogString() + "] [宠物:" + pet.getLogString() + "] [" + (pet.getGeneration() + 1) + "代" + pet.getVariation() + "变]");
					}
					int skillNum = 0;
					if (pet.getSkillIds() != null) {
						skillNum = pet.getSkillIds().length;
					}

					pet.init();
					// 计算宠物排行榜积分
					pet.setPetScore();

					// // 把宠物道具放入玩家的宠物背包
					// player.putToKnapsacks(ppe, "宠物");
					// ArticleStatManager.addToArticleStat(player, null, ppe, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "宠物孵化获得", null);
					// //添加或得宠物的事件。
					// EventPlayerGainPet pge = new EventPlayerGainPet(player);
					// pge.eggProp = props;
					// pge.petArticle = (PetProps) petArticle;
					// EventRouter.getInst().addEvent(pge);
					//
					return pet;
				} catch (Exception e) {
					logger.error("[使用道具获得变异宠] [异常] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] [" + entity.getArticleName() + "] [" + entity.getId() + "]", e);
				}
			} else {
				logger.error("[使用道具获得变异宠] [失败：宠物蛋配置的宠物物种有误] [{}] [{}] [{}] [{}] [{}] [宠物物种:{}] [{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(), entity.getArticleName(), entity.getId(), petArticleName, petArticle.getClass(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		} else {
			logger.error("[使用道具获得变异宠] [失败：宠物道具有误] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(), entity.getArticleName(), entity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
		}
		return null;
	}

	/**
	 * 鉴定宠物的品质、宠物成长品质
	 * 
	 * 初始值+宠物类型+宠物变异+成长初值+成长率
	 * 
	 * 宠物品质 决定条件 X值
	 * 普兽（白色）
	 * 灵兽（绿色）
	 * 仙兽（蓝色）
	 * 神兽（紫色）
	 * 圣兽（橙色）
	 * 
	 * 鉴定规则：1、宠物品质鉴定后，当宠物进行还童后，宠物品质需从新鉴定 2、二代宠物获得后，宠物品质需从新鉴定
	 * 
	 * 
	 * 
	 */
	public String identifyQualityAndGrowthClass(Player player, Pet pet) {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		// if(player.getBindSilver() < 鉴定需要绑银) {
		// logger.error("[宠物鉴定] [金币不够] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+pet.getId()+"]", startTime);
		// logger.error("[宠物鉴定] [金币不够] [{}] [{}] [{}] [{}] [{}ms]", new
		// Object[]{player.getId(),player.getName(),player.getUsername(),pet.getId(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-startTime});
		// throw new NoEnoughMoneyException();
		// }
		if (pet.isIdentity() == true) {
			// 如果成长等级大于0，认为已经鉴定过，也就是说如果鉴定还是0是，玩家还可以鉴定
			// logger.error("[宠物鉴定] [宠物已经鉴定过] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+pet.getId()+"]", startTime);
			logger.error("[宠物鉴定] [宠物已经鉴定过] [{}] [{}] [{}] [petname:{}] [{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(),pet.getName(), pet.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime });
			return Translate.text_pet_25;
		}
		// BillingCenter bcenter = BillingCenter.getInstance();
		// try {
		//
		// bcenter.playerExpense(player, 鉴定需要绑银, CurrencyType.BIND_YINZI, ExpenseReasonType.PET_IDENTIFY, Translate.宠物鉴定);
		// } catch (Exception e) {
		// // logger.error("[宠物鉴定] [金币不够] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+pet.getId()+"]", startTime);
		// logger.error("[宠物鉴定] [金币不够] [{}] [{}] [{}] [{}] [{}ms]", new
		// Object[]{player.getId(),player.getName(),player.getUsername(),pet.getId(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-startTime});
		// throw new NoEnoughMoneyException();
		// }

		int growthClass = 0;

		if (pet.getEggType() == 0) {
			// 任务得到的宠物蛋默认是普通
			growthClass = PetPropertyUtility.随机获得成长率等级(pet.getRealTrainLevel());
		}

		pet.setGrowthClass((byte) growthClass);
		int quality = PetPropertyUtility.判定宠物品质(pet, true);
		pet.setQuality((byte) quality);

		pet.setStrengthA(PetPropertyUtility.计算力量值A(pet));
		pet.setDexterityA(PetPropertyUtility.计算身法值A(pet));
		pet.setSpellpowerA(PetPropertyUtility.计算灵力值A(pet));
		pet.setConstitutionA(PetPropertyUtility.计算耐力值A(pet));
		pet.setDingliA(PetPropertyUtility.计算定力值A(pet));

		pet.strengthQuality = PetPropertyUtility.得到实际力量资质(pet, true, 2);
		pet.dexterityQuality = PetPropertyUtility.得到实际身法资质(pet, true, 2);
		pet.spellpowerQuality = PetPropertyUtility.得到实际灵力资质(pet, true, 2);
		pet.constitutionQuality = PetPropertyUtility.得到实际耐力资质(pet, true, 2);
		pet.dingliQuality = PetPropertyUtility.得到实际定力资质(pet, true, 2);

		pet.showStrengthQuality = PetPropertyUtility.得到实际力量资质(pet, true, 2);
		pet.showDexterityQuality = PetPropertyUtility.得到实际身法资质(pet, true, 2);
		pet.showSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(pet, true, 2);
		pet.showConstitutionQuality = PetPropertyUtility.得到实际耐力资质(pet, true, 2);
		pet.showDingliQuality = PetPropertyUtility.得到实际定力资质(pet, true, 2);

		// pet.growth = (int) Math.rint((PetPropertyUtility.获得成长值(pet) / (100 * 100)));
		pet.setIdentity(true);
		pet.reinitFightingProperties(true);
		Pet2PropCalc.getInst().calcFightingAtt(pet, Pet2Manager.log);
		pet.initTitle();
		ArticleEntity entity = player.getArticleEntity(pet.getPetPropsId());
		if (entity != null && entity instanceof PetPropsEntity) {
			PetPropsEntity ppe = (PetPropsEntity) entity;
			// ppe.setPetColorType(pet.getQuality());
		}
		// logger.info("[宠物鉴定] [成功] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+pet.getId()+"] [品质:"+pet.getQuality()+"] [成长等级:"+pet.getGrowthClass()+"]",
		// startTime);
		try {
			EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.宠物鉴定次数, 1L});
			EventRouter.getInst().addEvent(evt);
		} catch (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [统计宠物鉴定异常] [" + player.getLogString() + "]", e);
		}
//		if (logger.isWarnEnabled()) logger.warn("[宠物鉴定] [成功] [玩家id:{}] [角色:{}] [账号:{}] [petname:{}] [petid:{}] [品质:{}] [成长等级:{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(),pet.getName(), pet.getId(), pet.getQuality(), pet.getGrowthClass(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime });
		if (logger.isWarnEnabled()) logger.warn("[宠物鉴定] [成功] [{}] {} [{}ms]", new Object[] { player.getLogString4Knap(), pet.getLogOfInterest(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime });
		return "";
	}

	// public static String[] 根骨丹名称 = new String[]{"一级根骨丹","二级根骨丹","三级根骨丹","四级根骨丹", "五级根骨丹",
	// "六级根骨丹","七级根骨丹","八级根骨丹","九级根骨丹","十级根骨丹"};

	// /**
	// * 合成根骨丹
	// * @param player
	// * @param entities
	// * @throws GenguDanLevelErrorException
	// * @throws Exception
	// */
	// public int[] compositeGenguDan(Player player, String targetName, String sourceName, int num) throws GenguDanLevelErrorException,
	// KnapsackFullException,PlayerHasNoArticleEntityException,ArticleNotFoundException {
	// synchronized(player) {
	// int targetIndex = -1;
	// int sourceIndex = -1;
	// for(int i=0; i<根骨丹名称.length; i++) {
	// if(根骨丹名称[i].equals(targetName)) {
	// targetIndex = i;
	// }
	// if(根骨丹名称[i].equals(sourceName)) {
	// sourceIndex = i;
	// }
	// }
	// if(targetIndex == -1 || sourceIndex == -1) {
	// throw new PlayerHasNoArticleEntityException();
	// }
	// if(sourceIndex >= targetIndex) {
	// throw new GenguDanLevelErrorException();
	// }
	// int trueNum = player.getArticleEntityNum(sourceName);
	// if(trueNum < num) {
	// throw new PlayerHasNoArticleEntityException();
	// }
	// //计算从原始到目标等级，需要多少根骨丹
	// int needNum = (int)Math.pow(2, targetIndex-sourceIndex);
	// int resultNum = num / needNum;
	// int remaining = num % needNum;
	// int totalNeed = needNum * resultNum;
	//
	// if(resultNum == 0) {
	// return new int[]{0,remaining};
	// }
	//
	// ArticleEntityManager aem = ArticleEntityManager.getInstance();
	// ArticleManager amanager = ArticleManager.getInstance();
	// Article targetArticle = amanager.getArticle(targetName);
	// Article sourceArticle = amanager.getArticle(sourceName);
	// if(targetArticle == null || sourceArticle == null) {
	// throw new ArticleNotFoundException();
	// }
	// List<ArticleEntity> elist = new ArrayList<ArticleEntity>();
	// for(int i=0; i<resultNum; i++) {
	// ArticleEntity entity = aem.createEntity(targetArticle, false, ArticleEntityManager.CREATE_REASON_COMPOSE_ARTICLE, player, 0);
	// elist.add(entity);
	// }
	// boolean putOK = player.putAllOK(elist.toArray(new ArticleEntity[0]));
	// if(!putOK) {
	// throw new KnapsackFullException();
	// }
	// //删除玩家背包里的物品
	// List<ArticleEntity> removed = new ArrayList<ArticleEntity>();
	// for(int i=0; i<totalNeed; i++) {
	// ArticleEntity entity = player.getArticleEntity(sourceName);
	// if(entity != null) {
	// player.removeArticleEntityFromKnapsackByArticleId(entity.getId());
	// removed.add(entity);
	// } else {
	// //归还已经删除的物品
	// player.putAll(removed.toArray(new ArticleEntity[0]));
	// throw new PlayerHasNoArticleEntityException();
	// }
	// }
	// //把合成好的物品放进背包
	// player.putAll(elist.toArray(new ArticleEntity[0]));
	// return new int[]{resultNum, remaining};
	// }
	// }

	/**
	 * 宠物合成查询
	 * @return
	 */
	public QUERY_PET_MERGE_RES queryPetMergeReq(Player player, QUERY_PET_MERGE_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		long petEntityId = req.getPetEntityId();
		QUERY_PET_MERGE_RES res = null;
		ArticleEntity entity = player.getArticleEntity(petEntityId);
		if (entity == null) {
			PetManager.logger.error("[宠物合成查询失败] [宠物蛋道具为null] [" + petEntityId + "]");
			return null;
		}
		if (!(entity instanceof PetPropsEntity)) {

			// logger.error("[宠物合体查询] [失败：宠物物品错误] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [A:"+petEntityId+"] ", start);
			logger.error("[宠物合体查询] [失败：宠物物品错误] [{}] [{}] [{}] [A:{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(), petEntityId, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.不是宠物);
			player.addMessageToRightBag(hreq);
			return null;

		}
		Article a = ArticleManager.getInstance().getArticle(entity.getArticleName());
		if (a != null) {
			PetProps p = (PetProps) a;
			if (p != null) {
				PetEggProps eggProps = (PetEggProps) ArticleManager.getInstance().getArticle(p.getEggAticleName());
				if (eggProps != null) {

					// int trainLevel = eggProps.getTrainLevel();
					int trainLevel = eggProps.getArticleLevel();
					res = new QUERY_PET_MERGE_RES(req.getSequnceNum(), petEntityId, 根据宠物等级得到绑定技能花费描述数组(trainLevel));
					if (logger.isInfoEnabled()) {
						logger.info("[宠物合成查询成功] [" + player.getLogString() + "] [宠物蛋名：" + entity.getArticleName() + "] [蛋id:" + entity.getId() + "]");
					}
				} else {
					PetManager.logger.error("[宠物合成查询失败] [宠物蛋道具为null] [" + player.getLogString() + "] [" + entity.getArticleName() + "] [" + entity.getId() + "]");
				}
			} else {
				PetManager.logger.error("[宠物合成查询失败] [宠物道具为null] [" + player.getLogString() + "] [" + entity.getArticleName() + "] [" + entity.getId() + "]");
			}
		} else {
			PetManager.logger.error("[宠物合成查询失败] [宠物物种为null] [" + player.getLogString() + "] [" + entity.getArticleName() + "] [" + entity.getId() + "]");
		}
		return res;
	}

	public static final int[][] 宠物绑定技能花费 = new int[][] { { 50, 100, 150 }, { 50, 100, 150 }, { 50, 100, 150 }, { 50, 100, 150 }, { 50, 100, 150 }, { 50, 100, 150 } };
	public static final int[] 宠物级别对应的绑定技能花费下标 = new int[] { 1, 45, 90, 135, 180, 220 };

	// 根据携带等级不同
	public String[] 根据宠物等级得到绑定技能花费描述数组(int level) {
		int index = 0;
		for (int i : 宠物级别对应的绑定技能花费下标) {
			if (level == i) {
				break;
			}
			++index;
		}
		if (index < 宠物绑定技能花费.length) {
			int[] nums = 宠物绑定技能花费[index];

			String[] strs = new String[3];
			// strs[0] = Translate.translateString(Translate.宠物绑定x技能花费, new String[][]{{Translate.COUNT_1,nums[0]+""},{Translate.COUNT_2,1+""}});
			// strs[1] = Translate.translateString(Translate.宠物绑定x技能花费, new String[][]{{Translate.COUNT_1,nums[1]+""},{Translate.COUNT_2,2+""}});
			// strs[2] = Translate.translateString(Translate.宠物绑定x技能花费, new String[][]{{Translate.COUNT_1,nums[2]+""},{Translate.COUNT_2,3+""}});
			strs[0] = Translate.translateString(Translate.宠物绑定x技能花费, new String[][] { { Translate.COUNT_1, nums[0] + "" } }) + Translate.两 + "0";
			strs[1] = Translate.translateString(Translate.宠物绑定x技能花费, new String[][] { { Translate.COUNT_1, nums[1] + "" } }) + Translate.两 + "0";
			strs[2] = Translate.translateString(Translate.宠物绑定x技能花费, new String[][] { { Translate.COUNT_1, nums[2] + "" } }) + Translate.两 + "0";
			return strs;
		}

		return new String[0];
	}

	/**
	 * 宠物合体
	 * 从副宠的技能栏内继承技能
	 * @param player
	 * @param petEntityA
	 * @param petEntityB
	 */
	public void mergePet(Player player, PET_MERGE_REQ req) {

		long petEntityAId = req.getPetEntityAId();
		long petEntityBId = req.getPetEntityBId();
		int[] skillIds = req.getPetASkillId();
		String result = 宠物合体条件判断(player, petEntityAId, petEntityBId, skillIds);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		} else {
			// 提示玩家信息

			PetPropsEntity entityA = (PetPropsEntity) player.getArticleEntity(petEntityAId);
			PetPropsEntity entityB = (PetPropsEntity) player.getArticleEntity(petEntityBId);

			Pet petA = PetManager.getInstance().getPet(entityA.getPetId());
			Pet petB = PetManager.getInstance().getPet(entityB.getPetId());

			String des = "";
			if (petA.getQualityScore() < petB.getQualityScore()) {
				des = 辅宠 + "【<f color='0x00ff00'>" + entityB.getArticleName() + "</f>：<f color='" + 得到宠物品质颜色值(petB.isIdentity(), petB.getQuality()) + "'>" + 得到宠物品质名(petB.isIdentity(), petB.getQuality()) + "</f>（<f color='0x00ff00'>" + petB.getQualityScore() + "</f>）】" + 强于主宠 + "【<f color='0x00ff00'>" + entityA.getArticleName() + "</f>：<f color='" + 得到宠物品质颜色值(petB.isIdentity(), petB.getQuality()) + "'>" + 得到宠物品质名(petA.isIdentity(), petB.getQuality()) + "</f>（<f color='0x00ff00'>" + petA.getQualityScore() + "</f>）】" + Translate.合成后辅宠会消失你确定合体;
			}
			// if(petA.getRarity() < petB.getRarity()){
			// des = 辅宠+"<f color='#f7e354'>【"
			// +petB.getLevel()+级+":"+entityB.getArticleName()+"("+PetManager.得到宠物稀有度名(petB.getRarity())+")"
			// +"】</f>的<f color='#f7e354'>"+稀有度
			// +"</f>"+强于主宠
			// +"<f color='#f7e354'>"+petA.getLevel()+级+":"+entityA.getArticleName()+"("+PetManager.得到宠物稀有度名(petA.getRarity())+")"
			// +"】</f>，" + 合成后辅宠会消失你确定合体;
			// }else if(petA.getQuality() < petB.getQuality()){
			// des = 辅宠+"<f color='#f7e354'>【"
			// +petB.getLevel()+级+":"+entityB.getArticleName()+"("+PetManager.得到宠物品质名(petB.isIdentity(),petB.getQuality())+")"
			// +"】</f>的<f color='#f7e354'>"+品质
			// +"</f>"+强于主宠
			// +"<f color='#f7e354'>"+petA.getLevel()+级+":"+entityA.getArticleName()+"("+PetManager.得到宠物品质名(petA.isIdentity(),petA.getQuality())+")"
			// +"】</f>，" + 合成后辅宠会消失你确定合体;
			// }else if(petA.getGrowthClass() < petB.getGrowthClass()){
			// des = 辅宠+"<f color='#f7e354'>【"
			// +petB.getLevel()+级+":"+entityB.getArticleName()
			// +"】</f>的<f color='#f7e354'>"+成长率
			// +"</f>"+强于主宠
			// +"<f color='#f7e354'>"+petA.getLevel()+级+":"+entityA.getArticleName()
			// +"】</f>，" + 合成后辅宠会消失你确定合体;
			// }else{
			// int strengthQualityInitA = (int) Math.rint((1.0 * PetPropertyUtility.得到力量初值(petA, true, 2) / 100));
			// int dexterityQualityInitA = (int) Math.rint((1.0 * PetPropertyUtility.得到身法初值(petA, true, 2) / 100));
			// int spellpowerQualityInitA = (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(petA, true, 2) / 100));
			// int constitutionQualityInitA = (int) Math.rint((1.0 * PetPropertyUtility.得到耐力初值(petA, true, 2) / 100));
			// int dingliQualityInitA = (int) Math.rint((1.0 * PetPropertyUtility.得到定力初值(petA, true, 2) / 100));
			//
			// int showStrengthQualityA = PetPropertyUtility.得到实际力量资质(petA, true, 2);
			// int showDexterityQualityA = PetPropertyUtility.得到实际身法资质(petA, true, 2);
			// int showSpellpowerQualityA = PetPropertyUtility.得到实际灵力资质(petA, true, 2);
			// int showConstitutionQualityA = PetPropertyUtility.得到实际耐力资质(petA, true, 2);
			// int showDingliQualityA = PetPropertyUtility.得到实际定力资质(petA, true, 2);
			//				
			// int strengthQualityInitB = (int) Math.rint((1.0 * PetPropertyUtility.得到力量初值(petB, true, 2) / 100));
			// int dexterityQualityInitB = (int) Math.rint((1.0 * PetPropertyUtility.得到身法初值(petB, true, 2) / 100));
			// int spellpowerQualityInitB = (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(petB, true, 2) / 100));
			// int constitutionQualityInitB = (int) Math.rint((1.0 * PetPropertyUtility.得到耐力初值(petB, true, 2) / 100));
			// int dingliQualityInitB = (int) Math.rint((1.0 * PetPropertyUtility.得到定力初值(petB, true, 2) / 100));
			//				
			// int showStrengthQualityB = PetPropertyUtility.得到实际力量资质(petB, true, 2);
			// int showDexterityQualityB = PetPropertyUtility.得到实际身法资质(petB, true, 2);
			// int showSpellpowerQualityB = PetPropertyUtility.得到实际灵力资质(petB, true, 2);
			// int showConstitutionQualityB = PetPropertyUtility.得到实际耐力资质(petB, true, 2);
			// int showDingliQualityB = PetPropertyUtility.得到实际定力资质(petB, true, 2);
			//				
			// int petAscore = this.getScoreByProperty(strengthQualityInitA, dexterityQualityInitA, spellpowerQualityInitA, constitutionQualityInitA, dingliQualityInitA,
			// showStrengthQualityA, showDexterityQualityA, showSpellpowerQualityA, showConstitutionQualityA, showDingliQualityA);
			// int petBscore = this.getScoreByProperty(strengthQualityInitB, dexterityQualityInitB, spellpowerQualityInitB, constitutionQualityInitB, dingliQualityInitB,
			// showStrengthQualityB, showDexterityQualityB, showSpellpowerQualityB, showConstitutionQualityB, showDingliQualityB);
			//				
			// if(petAscore < petBscore){
			// des = 辅宠+"<f color='#f7e354'>【"
			// +petB.getLevel()+级+":"+entityB.getArticleName()
			// +"】</f>的<f color='#f7e354'>"+基础属性
			// +"</f>"+强于主宠
			// +"<f color='#f7e354'>"+petA.getLevel()+级+":"+entityA.getArticleName()
			// +"】</f>，" + 合成后辅宠会消失你确定合体;
			// }
			// }

			if (!des.equals("")) {
				WindowManager wm = WindowManager.getInstance();
				MenuWindow mw = wm.createTempMenuWindow(600);
				mw.setTitle(合体确定);
				mw.setDescriptionInUUB(des);

				Option_PetMergeConfirm option = new Option_PetMergeConfirm();
				option.setReq(req);
				option.setText(Translate.确定);

				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.取消);

				mw.setOptions(new Option[] { option, cancel });
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);

				logger.warn("[合体副宠好于主宠] [" + player.getLogString() + "] [A:" + petEntityAId + "] [B:" + petEntityBId + "] [" + des + "]");

			} else {
				confirmMergePetReq(player, petEntityAId, petEntityBId, skillIds);
			}
			return;
		}
	}

	public static String[] 老服务器名 = { 服务器风雪之巅, 服务器桃源仙境, 服务器昆仑圣殿, 服务器昆华古城, 服务器玉幡宝刹, 服务器问天灵台, 服务器百里沃野, 服务器太华神山, 服务器燃烧圣殿, 服务器炼狱绝地, 服务器东海龙宫, 服务器瑶台仙宫, 服务器峨嵋金顶, 服务器福地洞天, 服务器黄金海岸, 服务器无极冰原, 服务器云海冰岚, 服务器云波鬼岭, 服务器鹊桥仙境, 服务器蓬莱仙阁, 服务器国内本地测试 };

	public double[][] 得到宠物合体概率() {
		boolean 老服 = false;
		GameConstants gc = GameConstants.getInstance();
		String serverName = gc.getServerName();
		if (serverName != null) {
			for (String st : 老服务器名) {
				if (gc.getServerName().equals(st)) {
					老服 = true;
					break;
				}
			}
		} else {
			logger.error("[宠物合体判断服务器] [服务器为null]");
		}
		if (老服) {
			if (logger.isInfoEnabled()) {
				logger.info("[判断服务器] [" + serverName + "] [老服]");
			}
			return PetPropertyUtility.技能合体概率;
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("[判断服务器] [" + serverName + "] [新服]");
			}
			return PetPropertyUtility.新服技能合体概率;
		}

	}

	/**
	 * 宠物合体确认
	 * 从副宠的技能栏内继承技能, skillIds 是绑定的技能。
	 * @param player
	 * @param petEntityA
	 * @param petEntityB
	 */
	public void confirmMergePetReq(Player player, long petEntityAId, long petEntityBId, int[] skillIds) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		synchronized (player.tradeKey) {
			String result = 宠物合体条件判断(player, petEntityAId, petEntityBId, skillIds);
			if (result != null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
				player.addMessageToRightBag(hreq);
				return;
			} else {

				double[][] 技能概率 = 得到宠物合体概率();

				ArticleEntity ae = player.getArticleEntity(petEntityAId);
				// 扣费
				BillingCenter bc = BillingCenter.getInstance();
				if (bc != null) {
					PetPropsEntity ppe = (PetPropsEntity) ae;
					long petId = ppe.getPetId();
					Pet pet = PetManager.getInstance().getPet(petId);
					if (pet == null) {
						return;
					}

					if (skillIds.length > 0) {
						int cost = 宠物合体花费(pet.getTrainLevel(), skillIds.length) * 1000;

						try {
							bc.playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.PET_MERGER, "宠物合体");
						} catch (Exception e) {
							player.sendError(Translate.银子不足);
							PetManager.logger.error("[宠物合体花费异常] [" + player.getLogString() + "] []", e);
							return;
						}
					}
				} else {
					return;
				}

				// ArticleEntity ae = player.getArticleEntity(petEntityAId);
				Map<Integer, Integer> skId2lv = new LinkedHashMap<Integer, Integer>();
				ArticleEntity removeAe = player.removeArticleEntityFromKnapsackByArticleId(petEntityBId, "宠物合体", true);
				if (removeAe != null) {
					// List<Integer> skillIdList = new ArrayList<Integer>();
					long petId = ((PetPropsEntity) ae).getPetId();
					long removePetId = ((PetPropsEntity) removeAe).getPetId();
					Pet mainPet = getPet(petId);
					Pet removePet = getPet(removePetId);
					/*
					 * if (skillIds != null) {
					 * for (int skillId : skillIds) {
					 * if (skillIdList.indexOf(skillId) < 0) {
					 * skillIdList.add(skillId);
					 * }
					 * }
					 * }
					 */
					StringBuffer sb1 = new StringBuffer();
					StringBuffer sb2 = new StringBuffer();
					if (mainPet != null && removePet != null) {
						int[] mainPetSkArr = mainPet.getSkillIds();
						if (mainPetSkArr == null) mainPetSkArr = new int[0];
						putBindId2Lv(skId2lv, mainPet, skillIds);		//先把绑定的技能放到列表中
						putId2lv(skId2lv, removePet, null);
						putId2lv(skId2lv, mainPet, skillIds);// 以主宠为主。
						Iterator<Integer> it = skId2lv.keySet().iterator();
						int cnt = 0;
						while (it.hasNext()) {
							if (cnt >= 宠物最大技能数) {
								break;
							}
							Integer key = it.next();
							if (skillIds != null && ArrayUtils.indexOf(skillIds, key) >= 0) {
								// 不过滤绑定技能。
								cnt++;
								continue;
							}
							double[] ds = 技能概率[cnt];
							double baseSeed = ds[0];
							if (ArrayUtils.indexOf(mainPetSkArr, key) >= 0) {
								baseSeed = ds[1];// 属于主宠物的技能，继承概率大些。
							}
							boolean exist = ProbabilityUtils.randomProbability(random, baseSeed);
							if (!exist) {
								it.remove();
								continue;
							}
							cnt++;
						}
						/*
						 * int[] mainSkillIds = mainPet.getSkillIds();
						 * if (mainSkillIds != null) {
						 * for (int skillId : mainSkillIds) {
						 * sb1.append(skillId+";");
						 * int num = 0;
						 * if (skillIdList.size() < 宠物最大技能数) {
						 * num = skillIdList.size();
						 * }else{
						 * if (logger.isWarnEnabled()) {
						 * logger.warn("[宠物合体达到宠物最大技能数] [主宠:" + mainPet.getLogString() + "] ["+player.getLogString()+"]");
						 * }
						 * break;
						 * }
						 * double[] ds = 技能概率[num];
						 * boolean exist = ProbabilityUtils.randomProbability(random, ds[1]);
						 * if (exist && skillIdList.indexOf(skillId) < 0) {
						 * if (skillIdList.size() >= 宠物最大技能数) {
						 * if (logger.isWarnEnabled()) {
						 * logger.warn("[宠物合体达到宠物最大技能数] [主宠:" + mainPet.getLogString() + "] ["+player.getLogString()+"]");
						 * }
						 * break;
						 * }
						 * skillIdList.add(skillId);
						 * }
						 * }
						 * }
						 * int[] removePetSkillIds = removePet.getSkillIds();
						 * if (removePetSkillIds != null) {
						 * for (int skillId : removePetSkillIds) {
						 * sb2.append(skillId+";");
						 * int num = 0;
						 * if (skillIdList.size() < 宠物最大技能数) {
						 * num = skillIdList.size();
						 * }else{
						 * if (logger.isWarnEnabled()) {
						 * logger.warn("[宠物合体达到宠物最大技能数] [主宠:" + mainPet.getLogString() + "] ["+player.getLogString()+"]");
						 * }
						 * break;
						 * }
						 * double[] ds = 技能概率[num];
						 * boolean exist = ProbabilityUtils.randomProbability(random, ds[0]);
						 * // boolean exist = ProbabilityUtils.randomProbability(random,0.5);
						 * if (exist && skillIdList.indexOf(skillId) < 0) {
						 * if (skillIdList.size() >= 宠物最大技能数) {
						 * if (logger.isWarnEnabled()) {
						 * logger.warn("[宠物合体达到宠物最大技能数] [主宠:" + mainPet.getLogString() + "] ["+player.getLogString()+"]");
						 * }
						 * break;
						 * }
						 * skillIdList.add(skillId);
						 * }
						 * }
						 * }
						 * int[] newSkillIds = new int[skillIdList.size()];
						 * for (int i = 0; i < newSkillIds.length; i++) {
						 * newSkillIds[i] = skillIdList.get(i);
						 * }
						 */
						int fixLen = Math.min(10, skId2lv.size());
						int[] newSkillIds = new int[fixLen];
						Integer kArr[] = new Integer[skId2lv.size()];
						skId2lv.keySet().toArray(kArr);
						int[] skillLevels = new int[fixLen];
						for (int i = 0; i < fixLen; i++) {
							newSkillIds[i] = kArr[i];
							skillLevels[i] = skId2lv.get(kArr[i]);
						}
						mainPet.setSkillIds(newSkillIds);
						mainPet.setActiveSkillLevels(skillLevels);

						AchievementManager.getInstance().record(player, RecordAction.宠物技能个数, newSkillIds.length);
						if (AchievementManager.logger.isWarnEnabled()) {
							AchievementManager.logger.warn("[成就统计宠物技能个数] [" + player.getLogString() + "]");
						}

						// 最合适才最好
						// 十全十美仙宠
						int matchNum = 0;
						for (int skillId : newSkillIds) {
							Map<Integer, PetSkillReleaseProbability> map = PetManager.getInstance().getMap();
							if (map != null) {
								PetSkillReleaseProbability pb = map.get(skillId);
								if (pb != null) {
									if (pb.getCharacter() == mainPet.getCharacter()) {
										++matchNum;
									}
								}
							}
						}
						if (matchNum > 0) {
							AchievementManager.getInstance().record(player, RecordAction.宠物对应性格技能数量, matchNum);
							if (AchievementManager.logger.isWarnEnabled()) {
								AchievementManager.logger.warn("[成就统计宠物对应性格技能数量] [" + player.getLogString() + "] [" + matchNum + "]");
							}
						}
						/*
						 * // 设置技能级别
						 * int[] skillLevels = new int[newSkillIds.length];
						 * for (int i = 0; i < skillIds.length; i++) {
						 * skillLevels[i] = 1;
						 * }
						 * mainPet.setActiveSkillLevels(skillLevels);
						 */
						if (PetManager.logger.isWarnEnabled()) {
							PetManager.logger.warn("[宠物合体设置技能级别成功] [" + mainPet.getLogString() + "] [" + player.getLogString() + "]");
						}
						int quality = PetPropertyUtility.判定宠物品质(mainPet, true);

						// 计算宠物排行榜积分
						mainPet.setPetScore();
						mainPet.setQuality((byte) quality);
						mainPet.initTitle();
						removePet.setDelete(true);

						PET_MERGE_RES res = new PET_MERGE_RES(GameMessageFactory.nextSequnceNum(), "");
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.宠物合体成功);
						player.addMessageToRightBag(hreq);
						player.addMessageToRightBag(res);

						StringBuffer sb = new StringBuffer();
						if (skillIds.length > 0) {
							for (int id : skillIds) {
								sb.append("id:" + id);
							}
						}

						StringBuffer now = new StringBuffer();
						if (mainPet.getSkillIds() != null && mainPet.getSkillIds().length > 0) {
							for (int id : mainPet.getSkillIds()) {
								now.append("id:" + id);
							}
						}

						AchievementManager.getInstance().record(player, RecordAction.宠物合体次数);
						if (AchievementManager.logger.isWarnEnabled()) {
							AchievementManager.logger.warn("[成就统计宠物合体次数] [" + player.getLogString() + "]");
						}

//						if (logger.isWarnEnabled()) {
//							logger.warn("[宠物合体确认] [成功] [" + player.getLogString() + "] [主宠道具:"+ae.getArticleName()+"] [主宠道具id:" + petEntityAId + "] [负宠道具:"+removeAe.getArticleName()+"] [负宠道具id:" + petEntityBId + "] [绑定技能:" + sb.toString() + "] [main:" + sb1.toString() + "] [remove:" + sb2.toString() + "] [nowskills:" + now.toString() + "]");
//						}
						if (logger.isWarnEnabled()) {
							logger.warn("[宠物合体确认] [成功] [" + player.getLogString4Knap() + "] "+mainPet.getLogOfInterest()+" [主宠道具:"+ae.getArticleName()+"] [主宠道具id:" + petEntityAId + "] [负宠道具:"+removeAe.getArticleName()+"] [负宠道具id:" + petEntityBId + "] [绑定技能:" + sb.toString() + "] [main:" + sb1.toString() + "] [remove:" + sb2.toString() + "] [nowskills:" + now.toString() + "]");
						}
					}
				} else {
					logger.error("[宠物合体确认失败] [副宠不存在] [" + player.getLogString() + "] [副宠Id:" + petEntityBId + "]");
				}
			}
		}
	}
	
	public void putBindId2Lv(Map<Integer, Integer> map, Pet pet, int[] skillIds) {
		int ids[] = pet.getSkillIds();
		if (ids == null) {
			return;
		}
		int[] lvArr = pet.getActiveSkillLevels();
		if (lvArr == null) {
			return;
		}
		for (int i = 0; i < ids.length; i++) {
			boolean exist = false;
			if (skillIds != null && skillIds.length > 0) {			
				for (int k=0; k<skillIds.length; k++) {
					if (skillIds[k] == ids[i]) {
						exist = true;
						break;
					}
				}
			}
			if (exist && i < lvArr.length) {			//只放入绑定的技能
				map.put(ids[i], lvArr[i]);
			}
		}
	}

	public void putId2lv(Map<Integer, Integer> map, Pet pet, int[] skillIds) {
		int ids[] = pet.getSkillIds();
		if (ids == null) {
			return;
		}
		int[] lvArr = pet.getActiveSkillLevels();
		if (lvArr == null) {
			return;
		}
		for (int i = 0; i < ids.length; i++) {
			boolean exist = false;
			if (skillIds != null && skillIds.length > 0) {			//绑定的技能首先加入列表中
				for (int k=0; k<skillIds.length; k++) {
					if (skillIds[k] == ids[i]) {
						exist = true;
						break;
					}
				}
			}
			if (!exist && i < lvArr.length) {
				map.put(ids[i], lvArr[i]);
			}
		}
	}

	private String 宠物合体条件判断(Player player, long entityIdA, long entityIdB, int[] ids) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		String description = null;
		ArticleEntity entityA = player.getArticleEntity(entityIdA);
		ArticleEntity entityB = player.getArticleEntity(entityIdB);
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, entityA.getId())) {
			return Translate.高级锁魂物品不能做此操作;
		}
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, entityB.getId())) {
			return Translate.锁魂物品不能做此操作;
		}
		if (entityA == null || !(entityA instanceof PetPropsEntity) || entityB == null || !(entityB instanceof PetPropsEntity)) {
			description = Translate.必须都是宠物才能合体;
			// logger.error("[宠物合体] [失败] ["+description+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [A:"+entityIdA+"] [B:"+entityIdB+"]", start);
			logger.error("[宠物合体] [失败] [{}] [{}] [{}] [{}] [A:{}] [B:{}] [{}ms]", new Object[] { description, player.getId(), player.getName(), player.getUsername(), entityIdA, entityIdB, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return description;
		}

		PetProps pa = (PetProps) ArticleManager.getInstance().getArticle(entityA.getArticleName());
		PetProps pb = (PetProps) ArticleManager.getInstance().getArticle(entityB.getArticleName());

		PetEggProps eggPropsa = (PetEggProps) ArticleManager.getInstance().getArticle(pa.getEggAticleName());
		PetEggProps eggPropsb = (PetEggProps) ArticleManager.getInstance().getArticle(pb.getEggAticleName());

		// if(eggPropsb.getTrainLevel() < eggPropsa.getTrainLevel()){
		// description = Translate.副宠级别不能小于主宠级别;
		// // logger.error("[宠物合体] [失败] ["+description+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [A:"+entityIdA+"] [B:"+entityIdB+"]", start);
		// logger.error("[宠物合体] [失败] [{}] [{}] [{}] [{}] [A:{}] [B:{}] [{}ms]", new
		// Object[]{description,player.getId(),player.getName(),player.getUsername(),entityIdA,entityIdB,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
		// return description;
		// }
		if (ids != null) {
			if (ids.length > 3) {
				description = Translate.最多可以绑定3个技能;
				// logger.error("[宠物合体] [失败] ["+description+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [A:"+entityIdA+"] [B:"+entityIdB+"]", start);
				logger.error("[宠物合体] [失败] [{}] [{}] [{}] [{}] [A:{}] [B:{}] [{}ms]", new Object[] { description, player.getId(), player.getName(), player.getUsername(), entityIdA, entityIdB, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return description;
			}
		}
		long aid = ((PetPropsEntity) entityA).getPetId();
		long bid = ((PetPropsEntity) entityB).getPetId();
		if (player.getActivePetId() == aid || player.getActivePetId() == bid) {
			description = Translate.出战的宠物不能合体;
			// logger.error("[宠物合体] [失败] ["+description+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [A:"+entityIdA+"][B:"+entityIdB+"]", start);
			logger.error("[宠物合体] [失败] [{}] [{}] [{}] [{}] [A:{}][B:{}] [{}ms]", new Object[] { description, player.getId(), player.getName(), player.getUsername(), entityIdA, entityIdB, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return description;
		}
		Pet ap = getPet(aid);
		Pet bp = getPet(bid);
		
		for(long pid : player.getPetCell()){
			if(pid == aid || pid == bid){
				return Translate.助战的宠物不能合体;
			}
		}
		
		if(PetHouseManager.getInstance().petIsStore(player, ap)){
			return Translate.挂机中的宠物不能封印;
		}

		if(PetHouseManager.getInstance().petIsStore(player, bp)){
			return Translate.挂机中的宠物不能封印;
		}

		if (ap == null || bp == null) {
			description = Translate.服务器出现错误;
			// logger.error("[宠物合体] [失败] ["+description+"主宠或副宠不存在] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [A:"+entityIdA+"] [B:"+entityIdB+"]",
			// start);
			logger.error("[宠物合体] [失败] [{}主宠或副宠不存在] [{}] [{}] [{}] [A:{}] [B:{}] [{}ms]", new Object[] { description, player.getId(), player.getName(), player.getUsername(), entityIdA, entityIdB, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return description;
		}

		if (ap.getHookInfo() != null || bp.getHookInfo() != null) {
			return Translate.所选宠物有正在宠物房挂机;
		}
		int[] mainIds = ap.getSkillIds();
		if (ids != null && ids.length != 0) {
			if (mainIds == null || mainIds.length == 0) {
				description = Translate.主宠没有绑定技能中的技能;
				// logger.error("[宠物合体] [失败] ["+description+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [A:"+entityIdA+"] [B:"+entityIdB+"]", start);
				logger.error("[宠物合体] [失败] [{}] [{}] [{}] [{}] [A:{}] [B:{}] [{}ms]", new Object[] { description, player.getId(), player.getName(), player.getUsername(), entityIdA, entityIdB, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return description;
			}
			for (long id : ids) {
				boolean has = false;
				for (long mainId : mainIds) {
					if (mainId == id) {
						has = true;
						break;
					}
				}
				if (!has) {
					description = Translate.主宠没有绑定技能中的技能;
					// logger.error("[宠物合体] [失败] ["+description+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [A:"+entityIdA+"] [B:"+entityIdB+"]",
					// start);
					logger.error("[宠物合体] [失败] [{}] [{}] [{}] [{}] [A:{}] [B:{}] [{}ms]", new Object[] { description, player.getId(), player.getName(), player.getUsername(), entityIdA, entityIdB, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return description;
				}
			}
		}
		int count = 0;
		if (ids != null) {
			count = ids.length;
		}
		if (count > 0) {
			// int cost = 宠物合体花费(eggPropsa.getTrainLevel(), count);
			int cost = 宠物合体花费(eggPropsa.getArticleLevel(), count);
			if (player.getSilver() + player.getShopSilver() < cost * 1000) {
				description = "";
				try {
					description = Translate.translateString(Translate.宠物合体需要花费, new String[][] { { Translate.COUNT_1, cost + "" } });
				} catch (Exception e) {
					PetManager.logger.error("[宠物合体需要花费] [" + player.getLogString() + "]", e);
				}
				return description;
			}
		}
		return description;
	}

	/**
	 * 
	 * @param level
	 *            携带等级
	 * @param count
	 *            技能个数
	 * @return
	 */
	public int 宠物合体花费(int level, int count) {

		int index = 0;
		for (int i : 宠物级别对应的绑定技能花费下标) {
			if (level == i) {
				break;
			}
			++index;
		}
		if (index < 宠物绑定技能花费.length) {
			int[] nums = 宠物绑定技能花费[index];
			return nums[count - 1];
		}

		return 0;

	}

	// /**
	// * 宠物还童
	// * @param player
	// * @param pet
	// * @param entity
	// */
	// public String returnChild(Player player, Pet pet)
	// throws GenerationErrorException,BreedTimesErrorException,StatusErrorException,PlayerHasNoArticleEntityException {
	// if(pet.getGeneration() != 0) {
	// // throw new GenerationErrorException();
	// return Translate.text_pet_39;
	// }
	// if(pet.getBreededTimes() > 0) {
	// throw new BreedTimesErrorException();
	// }
	// //宠物不能为出战状态
	// if(pet.isSummoned()) {
	// throw new StatusErrorException();
	// }
	// String articleName = bigChildArticle;
	// //根据携带等级判断用哪种还童丹
	// // if(pet.getTrainLevel() <= div_TrainLevel){
	// // articleName = smallChildArticle;
	// // }else{
	// // articleName = bigChildArticle;
	// // }
	// ArticleEntity entity = player.getArticleEntity(articleName);
	// if(entity == null){
	// throw new PlayerHasNoArticleEntityException();
	// }
	//
	// //删除玩家物品，顺便判断玩家有无道具
	// ArticleEntity ae = player.removeArticleEntityFromKnapsackByArticleId(entity.getId());
	// if(ae == null) {
	// throw new PlayerHasNoArticleEntityException();
	// }
	//
	// //清空所有获得的属性点
	// pet.setStrengthS(0);
	// pet.setDexterityS(0);
	// pet.setSpellpowerS(0);
	// pet.setConstitutionS(0);
	// pet.setDingliS(0);
	// pet.setUnAllocatedPoints(0);
	//
	// //等级变为1级
	// pet.setLevel(1);
	//
	// //随机获得成长度
	// int growthClass = PetPropertyUtility.随机获得成长率等级(pet.getTrainLevel());
	// pet.setGrowthClass((byte)growthClass);
	// int quality = PetPropertyUtility.判定宠物品质(pet,true);
	// pet.setQuality((byte)quality);
	//
	// // System.out.println("petinit" + growthClass);
	// pet.init();
	//
	// player.send_HINT_REQ(pet.getName()+"还童成功，消耗1个"+articleName);
	// PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.宠物还童成功, (byte)1, AnimationManager.动画播放位置类型_宠物还童, 0, 0);
	// if (pareq != null) {
	// player.addMessageToRightBag(pareq);
	// // logger.warn("[宠物还童] [成功] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]");
	// logger.warn("[宠物还童] [成功] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName()});
	// }
	// return;
	// }

	/**
	 * 喂养宠物
	 * @param player
	 * @param pet
	 * @param entity
	 */
	public String feedPet(Player player, PetPropsEntity petEntity, long foodId, int num) {
		long time = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		long petid = petEntity.getPetId();
		Pet pet = this.getPet(petid);
		if (pet == null) {
			logger.error("[宠物喂养] [失败：玩家没有宠物物品] [" + player.getLogString() + "] [" + petid + "]");
			return Translate.text_pet_30;
		}
		boolean isDeduct = false;
		synchronized (player.tradeKey) {
			for (int i = 0; i < num; i++) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(foodId);
				if (ae == null) {
					if (logger.isWarnEnabled()) logger.warn("[喂养宠物错误] [" + player.getLogString() + "] [" + foodId + "]");
				}
				Article article = ArticleManager.getInstance().getArticle(ae.getArticleName());
				if (article instanceof PetFoodArticle || article instanceof LastingForPetProps || article instanceof SingleForPetProps) {

					if (article instanceof PetFoodArticle) {
						if (((PetFoodArticle) article).addProperty(pet, ae, player)) {
							Object o = player.removeArticleEntityFromKnapsackByArticleId(foodId, "宠物喂养", true);
							if (o == null) {
								// PetManager.logger.error("[宠物喂养] [删除物品失败] ["+player+"] ["+foodId+"] [] ["+time+"]");
								PetManager.logger.error("[宠物喂养] [删除物品失败] [{}] [{}] [] [{}]", new Object[] { player, foodId, time });
								return Translate.喂养宠物删除宠物食物失败;
							}
						} else {
							PetFoodArticle pf = ((PetFoodArticle) article);
							if (pf.getType() == 1) {
								return Translate.此宠物快乐值以达到最高;
							} else if (pf.getType() == 2) {
								return Translate.此宠物寿命值以达到最高;
							}
						}
					} else if (article instanceof LastingForPetProps) {

						LastingForPetProps pp = (LastingForPetProps) article;
						BuffTemplateManager btm = BuffTemplateManager.getInstance();
						BuffTemplate bt = btm.getBuffTemplateByName(pp.getBuffName());
						if (bt == null) {
							if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[使用道具] [BUFF道具] [失败] [" + player.getName() + "]  [" + pp.getBuffName() + "] [buff不存在]");
							return Translate.喂养宠物持续回血没有buff;
						}
						boolean used = false;
						// Pet pet = PetManager.getInstance().getPet(player.getActivePetId());
						if (pet != null) {
							if (player.getActivePetId() != pet.getId()) {
								// return Translate.喂养宠物持续回血此宠物不是出战状态;

								if (pet.getHp() == pet.getMaxHP()) {
									return Translate.此宠物气血已经达到最大;
								}
								int value = pp.getValue();
								pet.setHp(pet.getHp() + value);
								if (pet.getHp() < 0) {
									pet.setHp(0);
								} else if (pet.getHp() > pet.getMaxHP()) {
									pet.setHp(pet.getMaxHP());
								}

							} else {
								String result = pp.plantBuffOnLivingObject(bt, pet, player);
								if (result.equals("")) {
									used = true;
								} else {
									return result;
								}
								if (!used) {
									if (PetManager.logger.isDebugEnabled()) PetManager.logger.debug("[没有指定宠物不能使用缓回药] [" + player.getLogString() + "] [" + article.getName() + "]");
									return Translate.translateString(Translate.没有指定宠物不能使用, new String[][] { { Translate.STRING_1, article.getName() } });
								}
							}
						}

						if (ArticleManager.logger.isDebugEnabled()) {
							// ArticleManager.logger.debug("[使用道具] [BUFF道具] [成功] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+this.getName()+"]");
							if (ArticleManager.logger.isDebugEnabled()) ArticleManager.logger.debug("[使用道具] [BUFF道具] [成功] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getName(), player.getId(), article.getName() });
						}

						Object o = player.removeArticleEntityFromKnapsackByArticleId(foodId, "宠物", true);
						if (o == null) {
							// PetManager.logger.error("[宠物喂养] [删除物品失败] ["+player+"] ["+foodId+"] [] ["+time+"]");
							PetManager.logger.error("[宠物喂养] [删除物品失败] [{}] [{}] [] [{}]", new Object[] { player, foodId, time });
							return Translate.喂养宠物删除宠物持续回血道具失败;
						}

					} else if (article instanceof SingleForPetProps) {
						
						boolean isTianshengdan = Pet2Manager.getInst().isTianShengDan(ae);
						SingleForPetProps sp = (SingleForPetProps) article;
						if (isTianshengdan) {
							if (!Pet2Manager.getInst().check4TianSheng(player, pet, ae)) {
								if (PetManager.logger.isDebugEnabled()) {
									PetManager.logger.debug("[宠物喂养] [开启先后天丹] [使用物品失败] [宠物不能使用此道具] [" + player.getLogString() + "] [petId:" + pet.getId() + "] [aeId:" + ae.getId() + "]");
								}
								return "stopTip";
							}
							Object tempAe = player.removeArticleEntityFromKnapsackByArticleId(foodId, "宠物", true);
							if (tempAe == null) {
								// PetManager.logger.error("[宠物喂养] [删除物品失败] ["+player+"] ["+foodId+"] [] ["+time+"]");
								PetManager.logger.error("[宠物喂养] [开启天后天丹] [删除物品失败] [{}] [{}] [] [{}]", new Object[] { player, foodId, time });
								return Translate.text_feed_silkworm_006;
							}
							isDeduct = true;
							// 0 不是洗天生技能的药物； 1 药物和宠物不匹配 2 吃掉了。
							int changeRet = Pet2Manager.getInst().changeTianSheng(player, pet, ae);
							if (changeRet == 0) {
							} else if (changeRet == 1) {
								return "stopTip";
							} else if (changeRet == 2) {
	//							ArticleEntity o = player.removeArticleEntityFromKnapsackByArticleId(foodId, "宠物", true);
	//							PetManager.logger.info("PetManager.feedPet: remove {}", o);
								return "stopTip";
							} else {
								player.sendError(Translate.未知错误 + changeRet);
								return "stopTip";
							}
						}
						String result = null;
						if (ae instanceof SingleForPetPropsEntity) {
							if (pet.getLevel() >= PetExperienceManager.maxLevel) {
								return result = String.format(Translate.petLevelOver220, getLevelDes(PetExperienceManager.maxLevel));
							}
							lock.lock();
							try {
								long entityExp = ((SingleForPetPropsEntity) ae).getValues()[1];
								sp.setTempExp(entityExp);
								if (PetManager.logger.isWarnEnabled()) {
									PetManager.logger.warn("[使用挂机得到的宠物经验丹] [喂养] [" + player.getLogString() + "] [" + sp.getName() + "] [经验值:" + sp.getTempExp() + "] [物品经验:" + entityExp + "]");
								}
								result = sp.changePetProperty(player, pet);
							} catch (Exception e) {
								PetManager.logger.error("[使用挂机得到的宠物经验丹] [" + player.getLogString() + "] [" + sp.getName() + "]", e);
							} finally {
								lock.unlock();
							}
						} else {
							result = sp.changePetProperty(player, pet);
						}	

						if (!isDeduct && result != null && result.equals("")) {
							Object o = player.removeArticleEntityFromKnapsackByArticleId(foodId, "宠物", true);
							if (o == null) {
								// PetManager.logger.error("[宠物喂养] [删除物品失败] ["+player+"] ["+foodId+"] [] ["+time+"]");
								PetManager.logger.error("[宠物喂养] [删除物品失败] [{}] [{}] [] [{}]", new Object[] { player, foodId, time });
								return Translate.喂养宠物删除宠物瞬回道具失败;
							}
							if (ArticleManager.logger.isDebugEnabled()) {
								// ArticleManager.logger.debug("[使用道具] [简单道具] [成功] ["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"] [直接改变宠物属性]");
								if (ArticleManager.logger.isDebugEnabled()) ArticleManager.logger.debug("[使用道具] [简单道具] [成功] [{}] [{}] [直接改变宠物属性]", new Object[] { player.getName(), article.getName() });
							}
						} else {
							return result;
						}
					}
					// else if(article instanceof PetWuXingProp){
					// PetWuXingProp p = (PetWuXingProp)article;
					// String result = p.canUse(player);
					// if(result==null){
					// p.use(player.getCurrentGame(), player, ae);
					// }else{
					// player.sendError(result);
					// return result;
					// }
					// }
				} else {
					logger.error("[宠物喂养] [失败：玩家没有喂养宠物物品] [" + player.getLogString() + "] [" + foodId + "]");
					return Translate.text_pet_31;
				}
			}
		}
		return "";
	}
	
	public static String getLevelDes(int level) {
		if (level <= 220) {
			return level + "";
		} else {
			int a = level - 220;
			return Translate.仙 + a;
		}
	}

	/**
	 * 喂养时打开背包显示可喂养的食物
	 * @param p
	 * @param message
	 * @return feedType; 0 1 2 3 经验 0血 1 快乐 2寿命 3经验
	 */
	public QUERY_PET_FEED_RES query_pet_food(Player p, RequestMessage message) {

		List<String> names = new ArrayList<String>();
		List<Byte> types = new ArrayList<Byte>();
		List<Integer> values = new ArrayList<Integer>();

		Knapsack[] knapsacks = p.getKnapsacks_common();
		for (Knapsack knapsack : knapsacks) {
			Cell[] cells = knapsack.getCells();
			for (int i = 0; i < cells.length; i++) {
				ArticleEntity ae = knapsack.getArticleEntityByCell(i);
				if (ae != null) {
					ArticleManager am = ArticleManager.getInstance();
					Article a = am.getArticle(ae.getArticleName());
					if (a != null) {
						if (a instanceof PetFoodArticle) {
							PetFoodArticle ac = (PetFoodArticle) a;
							names.add(ac.getName());
							types.add(ac.getType());
							values.add(ac.getValueByColor(ae.getColorType()));
						} else if (a instanceof LastingForPetProps) {
							LastingForPetProps ac = (LastingForPetProps) a;
							names.add(ac.getName());
							types.add((byte) 0);
							values.add(ac.getValue());
						} else if (a instanceof SingleForPetProps) {
							SingleForPetProps sp = (SingleForPetProps) a;
							names.add(sp.getName());
							if (sp.getValues()[0] > 0) {
								types.add((byte) 0);
								values.add((int) sp.getValues()[0]);
							} else {
								types.add((byte) 3);
								if (ae instanceof SingleForPetPropsEntity) {
									values.add((int) ((SingleForPetPropsEntity) ae).getValues()[1]);
								} else {
									values.add((int) sp.getValues()[1]);
								}
							}
						}
						// else if(a instanceof ){
						// petwx = (PetWuXingProp)a;
						// names.add(petwx.getName());
						// types.add((byte) 3);
						// values.add(0); //万峰说没用这个值
						// }
					}
				}
			}
		}

		Byte[] bytes = types.toArray(new Byte[0]);
		Integer[] value = values.toArray(new Integer[0]);
		byte[] b = new byte[bytes.length];
		int[] i = new int[bytes.length];
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < bytes.length; j++) {
			b[j] = bytes[j];
			i[j] = value[j];
			sb.append(":" + value[j]);
		}
//		if (logger.isWarnEnabled()) {
//			logger.warn("[查询宠物食物成功] [玩家:" + p.getLogString() + "] [" + sb.toString() + "]");
//		}
		if (logger.isWarnEnabled()) {
			logger.warn("[查询宠物食物成功] [{}] [{}]",new Object[]{p.getLogString4Knap(), sb.toString()});
		}
		QUERY_PET_FEED_RES res = new QUERY_PET_FEED_RES(message.getSequnceNum(), names.toArray(new String[0]), b, i);
		return res;
	}

	/**
	 * 二维数组
	 * 第一维为升级符数组下标
	 * 第二维为宠物级别范围(MIN,MAX)
	 */
	public static final int[][] 宠物级别对应的升级符数组下标 = new int[][] { { 0, 45 }, { 46, 300 } };

	public static final String[][] 宠物强化符数组 = new String[][] { { Translate.初级育兽丹, Translate.中级育兽丹 }, { Translate.中级育兽丹 } };

	/**
	 * 二维数组
	 * 第一维为升级符成功率数组下标
	 * 第二维为宠物强化星级范围(MIN,MAX)
	 */
	public static final int[][] 宠物星级对应的升级符成功率数组下标 = new int[][] { { 0, 0 }, { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 5, 5 }, { 6, 6 }, { 7, 7 }, { 8, 8 }, { 9, 9 } };

	// 不同颜色强化符在不同星级时的强化成功率分子，分母为TOTAL_LUCK_VALUE
	public static final int[] 宠物白色强化符成功率数组 = new int[] { 10000, 2500, 1111, 625, 400, 278, 204, 156, 123, 100 };
	public static final int[] 宠物绿色强化符成功率数组 = new int[] { 10000, 10000, 4444, 2500, 1600, 1111, 816, 625, 494, 400 };
	public static final int[] 宠物蓝色强化符成功率数组 = new int[] { 10000, 10000, 10000, 5625, 3600, 2500, 1837, 1406, 1111, 900 };
	public static final int[] 宠物紫色强化符成功率数组 = new int[] { 10000, 10000, 10000, 10000, 6400, 4444, 3265, 2500, 1975, 1600 };
	public static final int[] 宠物橙色强化符成功率数组 = new int[] { 10000, 10000, 10000, 10000, 10000, 6944, 5102, 3906, 3086, 2500 };

	/**
	 * 总的幸运基数
	 */
	public static final int TOTAL_LUCK_VALUE = 10000;
	
	public static boolean isTest = false;

	public String[] 根据宠物级别得到所需强化符(int level) {
		// for(int i = 0; i < 宠物级别对应的升级符数组下标.length; i++){
		// if(level >= 宠物级别对应的升级符数组下标[i][0] && level <= 宠物级别对应的升级符数组下标[i][1]){
		// return 宠物强化符数组[i];
		// }
		// }
		String[] st = { Translate.宠物强化物品 };
		return st;
	}

	// 根骨丹等级 白色=1，绿色=2 蓝色 紫色 橙色 根骨丹等级^2*4/幻化等级^2
	public static int[] 根骨丹等级 = { 1, 2, 3, 4, 5 };

	/**
	 * 返回分子值，分母为TOTAL_LUCK_VALUE 扩大10000
	 * @param level
	 * @return
	 */
	public int[] 根据宠物强化等级得到不同颜色强化符的成功率分子值(int level) {

		// 根骨丹等级^2*4/幻化等级^2
		int f1 = (int) (((1f * (根骨丹等级[0] * 根骨丹等级[0] * 4)) / (level * level)) * 10000);
		int f2 = (int) (((1f * (根骨丹等级[1] * 根骨丹等级[1] * 4)) / (level * level)) * 10000);
		int f3 = (int) (((1f * (根骨丹等级[2] * 根骨丹等级[2] * 4)) / (level * level)) * 10000);
		int f4 = (int) (((1f * (根骨丹等级[3] * 根骨丹等级[3] * 4)) / (level * level)) * 10000);
		int f5 = (int) (((1f * (根骨丹等级[4] * 根骨丹等级[4] * 4)) / (level * level)) * 10000);
		int[] strong = { f1, f2, f3, f4, f5 };
		return strong;
	}

	public static final int starMaxValue = 20;
	public static final int strongMaterialMaxNumber = 4;
	public static final Random random = new Random(SystemTime.currentTimeMillis());

	/**
	 * 查询宠物强化信息
	 * @param player
	 * @param req
	 * @return
	 */
	public QUERY_PET_STRONG_RES queryPetStrongReq(Player player, QUERY_PET_STRONG_REQ req) {

		QUERY_PET_STRONG_RES res = null;
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && req != null && aem != null && am != null) {
			long id = req.getPetEntityId();
			PetPropsEntity ppe = null;
			ArticleEntity ae1 = aem.getEntity(id);
			if (ae1 != null && ae1 instanceof PetPropsEntity) {
				ppe = (PetPropsEntity) aem.getEntity(id);
			} else {
				logger.error("[查询宠物强化信息错误] [宠物id:" + id + "] [" + player.getLogString() + "] [" + (ae1 != null ? ae1.getClass().getName() : "null") + "]");
			}
			if (ppe == null) {
				PetManager.logger.error("[查询宠物强化信息错误] [" + player.getLogString() + "] [宠物道具id:" + id + "]");
				return null;
			}
			PetManager petManager = PetManager.getInstance();
			Pet pet = petManager.getPet(ppe.getPetId());
			if (pet != null) {
				if (pet.getStarClass() < starMaxValue) {
					res = queryPetByArticleEntityId(player, id);
				} else {

					String[] strongMaterialName = 根据宠物级别得到所需强化符(pet.getRealTrainLevel());
					res = new QUERY_PET_STRONG_RES(GameMessageFactory.nextSequnceNum(), id, "", "", strongMaterialName, new int[0]);
					// String description = Translate.宠物已经强化到了上限;
					// try{
					// description = Translate.translateString(Translate.宠物已经强化到了顶级, new String[][]{{Translate.ARTICLE_NAME_1,ae.getArticleName()}});
					// }catch(Exception ex){
					// ex.printStackTrace();
					// }
					// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, description);
					// player.addMessageToRightBag(hreq);

					if (logger.isWarnEnabled()) logger.warn("[查询宠物强化] [已经到达上限] [" + player.getLogString() + "] [" + pet.getName() + "]");
				}
			} else {
				String description = Translate.空白;
				description = Translate.请放入正确物品;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				if (logger.isWarnEnabled()) logger.warn("[查询宠物强化] [失败] [" + player.getLogString() + "] [" + description + "]");
			}
		}
		return res;
	}

	/**
	 * 根据宠物id返回宠物强化查询信息
	 * 
	 * @param player
	 * @param id
	 * @return
	 */
	public QUERY_PET_STRONG_RES queryPetByArticleEntityId(Player player, long id) {
		QUERY_PET_STRONG_RES res = null;
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && aem != null && am != null) {
			ArticleEntity aee = aem.getEntity(id);
			PetPropsEntity ae = null;
			if (aee instanceof PetPropsEntity) {
				ae = (PetPropsEntity) aee;
			}
			if (ae == null) {
				return null;
			}
			Props p = (Props) am.getArticle(ae.getArticleName());
			if (p == null) {
				return null;
			}
			PetManager petManager = PetManager.getInstance();
			Pet pet = petManager.getPet(ae.getPetId());

			if (pet.getStarClass() >= starMaxValue) {
				return res;
			}
			String[] strongMaterialName = 根据宠物级别得到所需强化符(p.getLevelLimit());
			int[] strongMaterialLuck = 根据宠物强化等级得到不同颜色强化符的成功率分子值(pet.getStarClass() + 1);

			int[] strongMaterialBindCount = new int[strongMaterialName.length];
			int[] strongMaterialCount = new int[strongMaterialName.length];
			for (int i = 0; i < strongMaterialName.length; i++) {
				String articleName = strongMaterialName[i];
				int count1 = player.countArticleInKnapsacksByName(articleName, true);
				int count2 = player.countArticleInKnapsacksByName(articleName, false);
				strongMaterialBindCount[i] = count1;
				strongMaterialCount[i] = count2;
			}

			String descript = Translate.空白;
			try {
				descript = Translate.translateString(Translate.需求绑银提示, new String[][] { { Translate.COUNT_1, 强化所需绑银两 * (pet.getStarClass() + 1) + "" } });
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			String strongedUUB = Translate.空白;

			int[] tempLuck = new int[strongMaterialLuck.length];
			for (int i = 0; i < strongMaterialLuck.length; i++) {
				tempLuck[i] = (int) (strongMaterialLuck[i] * 0.9);
			}
			res = new QUERY_PET_STRONG_RES(GameMessageFactory.nextSequnceNum(), id, descript, strongedUUB, strongMaterialName, tempLuck);

			if (logger.isInfoEnabled()) {
				logger.info("[宠物强化查询] [幻化] [强化概率] [" + player.getLogString4Knap() + "] [" + pet.getLogOfInterest() + "] [" + strongMaterialName[0] + "] [白色:+" + strongMaterialLuck[0] + "] [绿色:+" + strongMaterialLuck[1] + "] [蓝色:+" + strongMaterialLuck[2] + "] [紫色:+" + strongMaterialLuck[3] + "] [橙色:+" + strongMaterialLuck[4] + "] ");
			}
		}
		return res;
	}

	/**
	 * 宠物强化申请
	 * @param player
	 * @param req
	 * @return
	 */
	public void petStrongReq(Player player, PET_STRONG_REQ req) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		if (player != null && aem != null && am != null && req != null) {
			long petEntityId = req.getPetEntityId();
			long[] strongMaterialIds = req.getStrongMaterialID();

			ArticleEntity aee = aem.getEntity(petEntityId);
			PetPropsEntity ae = null;
			if (aee instanceof PetPropsEntity) {
				ae = (PetPropsEntity) aee;
			}
			if (ae == null) {
				return;
			}
			if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, aee.getId())) {
				player.sendError(Translate.锁魂物品不能做此操作);
				return;
			}
			Props p = (Props) am.getArticle(ae.getArticleName());
			if (p == null) {
				return;
			}
			PetManager petManager = PetManager.getInstance();
			Pet pet = petManager.getPet(ae.getPetId());
			if (pet != null && pet.getHookInfo() != null) {
				player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
				return;
			}
			for(long pid : player.getPetCell()){
				if(pid == pet.getId()){
					player.sendError(Translate.助战中的宠物不能封印);
					return;
				}
			}
			
			if(PetHouseManager.getInstance().petIsStore(player, pet)){
				player.sendError(Translate.挂机中的宠物不能封印);
				return; 
			}

			int strongCost = 强化所需绑银文 * (pet.getStarClass() + 1);
			if (!player.bindSilverEnough(strongCost)) {

				if (player.getBindSilver() >= strongCost) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.绑银使用已达到上限);
					player.addMessageToRightBag(hreq);
				} else {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.银子不足);
					player.addMessageToRightBag(hreq);
				}
				return;
			}

			// 强化改为消费绑银
			if (player.getBindSilver() + player.getShopSilver() + player.getSilver() < 强化所需绑银文 * (pet.getStarClass() + 1)) {
				String description = "";
				try {
					// description = Translate.translateString(Translate.绑银不足提示, new String[][]{{Translate.COUNT_1,强化所需绑银两*(pet.getStarClass()+1)+""}});
					description = Translate.translateString(Translate.宠物操作银子不足提示, new String[][] { { Translate.COUNT_1, 强化所需绑银两 * (pet.getStarClass() + 1) + "" } });
				} catch (Exception ex) {
					PetManager.logger.error("[宠物强化需要花费] [" + player.getLogString() + "]", ex);
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null? ae.getArticleName():Translate.空白)+"] ["+(ae !=
				// null? ae.getId():Translate.空白)+"] ["+description+","+strongMaterialIds+"的物品为空]", start);
				if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}

			if (strongMaterialIds == null) {
				String description = Translate.空白;
				description = Translate.请放入正确物品;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null? ae.getArticleName():Translate.空白)+"] ["+(ae !=
				// null? ae.getId():Translate.空白)+"] ["+description+","+strongMaterialIds+"的物品为空]", start);
				if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			if (strongMaterialIds.length != strongMaterialMaxNumber) {
				String description = Translate.空白;
				description = Translate.请放入正确物品;
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null? ae.getArticleName():Translate.空白)+"] ["+(ae !=
				// null? ae.getId():Translate.空白)+"] ["+description+","+strongMaterialIds.length+"!="+strongMaterialMaxNumber+"]", start);
				if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}!={}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds.length, strongMaterialMaxNumber, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				return;
			}
			if (pet.getStarClass() < starMaxValue) {
				String[] strongMaterialName = 根据宠物级别得到所需强化符(p.getLevelLimit());
				int[] strongMaterialColorLuck = 根据宠物强化等级得到不同颜色强化符的成功率分子值(pet.getStarClass() + 1);
				Knapsack[] knapsacks = player.getKnapsacks_common();
				ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
				StringBuffer logSt = new StringBuffer();
				for (long id : strongMaterialIds) {
					logSt.append("[" + id + "]");
					if (id != -1) {
						ArticleEntity strongMaterialEntity = aem.getEntity(id);
						if (strongMaterialEntity == null) {
							String description = Translate.空白;
							description = Translate.请放入正确物品;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
							// ae.getArticleName():Translate.空白)+"] ["+(ae != null? ae.getId():Translate.空白)+"] ["+description+"id为"+id+"的物品为空]", start);
							if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, id, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
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
								PetManager.logger.error("[宠物强化放入正确物品] [" + player.getLogString() + "]", ex);
							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
							// ae.getArticleName():Translate.空白)+"] ["+(ae != null? ae.getId():Translate.空白)+"] ["+description+"]", start);
							if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
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
							description = Translate.请放入正确物品;
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
							// ae.getArticleName():Translate.空白)+"] ["+(ae != null? ae.getId():Translate.空白)+"] ["+description+"背包中没有id为"+strongMaterialEntity.getId()+"的物品]",
							// start);
							if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}背包中没有id为{}的物品] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialEntity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
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
					// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null? ae.getArticleName():Translate.空白)+"] ["+(ae
					// != null? ae.getId():Translate.空白)+"] ["+description+"]", start);
					if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				boolean hasPET = false;
				if (knapsacks != null) {
					for (Knapsack knapsack : knapsacks) {
						if (knapsack != null) {
							if (!hasPET) {
								int index1 = knapsack.hasArticleEntityByArticleId(petEntityId);
								if (index1 != -1) {
									hasPET = true;
									break;
								}
							}
						}
					}
				}
				if (hasPET) {
					int totalLuckValue = 0;
					for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
						if (strongMaterialEntity != null) {
							try {
								totalLuckValue += strongMaterialColorLuck[strongMaterialEntity.getColorType()];
							} catch (Exception ex) {
								PetManager.logger.error("[宠物强化计算几率] [" + player.getLogString() + "]", ex);
							}
						}
					}
					// //提示玩家信息
					// WindowManager wm = WindowManager.getInstance();
					// MenuWindow mw = wm.createTempMenuWindow(600);
					// String title = Translate.强化提示信息;
					// mw.setTitle(title);
					// String desUUB = Translate.宠物强化确认信息;
					// mw.setDescriptionInUUB(desUUB);
					// Option_PetStrongConfirm option = new Option_PetStrongConfirm();
					// option.setReq(req);
					// option.setText(Translate.确定);
					//
					// mw.setOptions(new Option[]{option,new Option_Cancel()});
					//
					// CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),mw.getDescriptionInUUB(),mw.getOptions());
					// player.addMessageToRightBag(creq);
					// return;

					if (logger.isWarnEnabled()) logger.warn("[" + player.getLogString() + "] [宠物强化前数值] [" + pet.getLogString() + "] [" + totalLuckValue + "] [" + logSt.toString() + "] [宠物星等:" + pet.getStarClass() + "]");
					synchronized (player.tradeKey) {
						if (knapsacks != null) {
							for (long strongMaterialId : strongMaterialIds) {
								if (strongMaterialId != -1) {
									ArticleEntity aee1 = player.removeArticleEntityFromKnapsackByArticleId(strongMaterialId, "宠物强化", true);
									if (aee1 == null) {
										String description = Translate.删除物品不成功;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) {
											logger.warn("[宠物强化确认] [失败] [" + player.getLogString() + "] [删除物品错误] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "]");
										}
										return;
									}
									ArticleStatManager.addToArticleStat(player, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "强化删除", null);
								}
							}
						}
					}

					// 扣费
					try {
						int countLack = 强化所需绑银文 * (pet.getStarClass() + 1);
						BillingCenter bc = BillingCenter.getInstance();
						if (player.getBindSilver() >= countLack) {
							bc.playerExpense(player, countLack, CurrencyType.BIND_YINZI, ExpenseReasonType.PET_REPAIR, "宠物强化");
							if (logger.isWarnEnabled()) logger.warn("[宠物强化成功] [消耗绑银文:" + countLack + "] [" + player.getLogString() + "]");
						} else {
							// String bangyinLack = Translate.translateString(Translate.宠物强化消费绑银不足消费银子, new String[][] { { Translate.COUNT_1, (countLack / 1000) + "" }, {
							// Translate.COUNT_2, ((countLack - player.getBindSilver()) / 1000) + "" } });
							// player.addMessageToRightBag(new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, bangyinLack));
							// long bangyin = player.getBindSilver();
							// long yinzi = countLack - player.getBindSilver();
							// bc.playerExpense(player, yinzi, CurrencyType.YINZI, ExpenseReasonType.PET_REPAIR, Translate.宠物强化);
							bc.playerExpense(player, countLack, CurrencyType.BIND_YINZI, ExpenseReasonType.PET_REPAIR, "宠物强化");
							if (logger.isWarnEnabled()) logger.warn("[宠物强化成功] [消耗绑银文:" + countLack + "] [消耗银子:" + countLack + "] [" + player.getLogString() + "] [" + pet.getLogString() + "]");
						}

					} catch (Exception ex) {
						logger.error("[宠物强化确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
						return;
					}

					//
					if (strongMaterialEntitys != null && strongMaterialEntitys.size() > 0) {
						try {
							// ArticleEntity aee2 = strongMaterialEntitys.get(0);
							// boolean isallbind = true;
							// for(ArticleEntity aae : strongMaterialEntitys){
							// if(aae!=null && aae.isBinded()){
							// isallbind = false;
							// break;
							// }
							// }
							// if(isallbind){
							// ShopActivityManager.getInstance().noticeUseSuccess(player, aee2, strongMaterialEntitys.size());
							// }
							ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}

					int resultValue = random.nextInt(TOTAL_LUCK_VALUE) + 1;
					boolean success = false;
					if (totalLuckValue >= resultValue) {
						success = true;
					}
					boolean canStrong = true;
					int oldStar = pet.getStarClass();

					AchievementManager.getInstance().record(player, RecordAction.宠物幻化次数);
					ActivenessManager.getInstance().record(player, ActivenessType.强化宠物);
					if (AchievementManager.logger.isWarnEnabled()) {
						AchievementManager.logger.warn("[成就统计宠物幻化次数] [" + player.getLogString() + "]");
					}
					PetManager.logger.warn("[宠物强化] [" + player.getLogString() + "] [" + pet.getLogString() + "] [总值:" + totalLuckValue + "] [随机数:" + resultValue + "]");
					if (success) {
						pet.setStarClass((byte) (pet.getStarClass() + 1));
//						if (pet.getStarClass() > pet.getAlreadyMaxStar()) {
							AchievementManager.getInstance().record(player, RecordAction.宠物最大星数, pet.getStarClass());
							if (AchievementManager.logger.isWarnEnabled()) {
								AchievementManager.logger.warn("[成就统计宠物最大星数] [" + player.getLogString() + "]");
							}
//						}

						// 世界广播
						this.strongSendMessage(pet, player);

						if (pet.getStarClass() >= starMaxValue) {
							pet.setStarClass((byte) starMaxValue);
							canStrong = false;
						}
						String description = Translate.宠物强化成功;
						try {
							description = Translate.translateString(Translate.宠物强化成功, new String[][] { new String[] { Translate.LEVEL_1, pet.getStarClass() + "" } });
						} catch (Exception ex) {
							PetManager.logger.error("[宠物强化成功] [" + player.getLogString() + "]", ex);
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						QUERY_ARTICLE_INFO_RES qres = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), ae.getId(), ae.getInfoShow(player));
						player.addMessageToRightBag(qres);

						pet.init();

						PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.宠物强化成功, (byte) 1, AnimationManager.动画播放位置类型_宠物强化升星, 0, 0);
						if (pareq != null) {
							player.addMessageToRightBag(pareq);
							logger.warn("[强化成功播放动画] [" + player.getLogString() + "]");
						}
					} else {

						// 强化失败降级
						int dropStar = 强化失败后降级();
						if (logger.isWarnEnabled()) {
							logger.warn("[宠物强化失败降级] [" + player.getLogString() + "] [" + pet.getLogString() + "] [降星:" + dropStar + "]");
						}
						if (pet.getStarClass() <= dropStar) {
							pet.setStarClass((byte) 0);
						} else {
							pet.setStarClass((byte) (pet.getStarClass() - dropStar));
						}

						String description = Translate.很遗憾宠物没有强化成功;
						try {
							description = Translate.translateString(Translate.宠物强化失败, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() }, new String[] { Translate.LEVEL_1, pet.getStarClass() + "" } });
						} catch (Exception ex) {
							PetManager.logger.error("[宠物强化失败] [" + player.getLogString() + "]", ex);
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);

						PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.宠物强化失败, (byte) 1, AnimationManager.动画播放位置类型_宠物强化升星, 0, 0);
						if (pareq != null) {
							player.addMessageToRightBag(pareq);
							logger.warn("[强化失败播放动画] [" + player.getLogString() + "]");
						}

					}

					QUERY_PET_STRONG_RES qres = queryPetByArticleEntityId(player, petEntityId);
					if (qres != null) {
						player.addMessageToRightBag(qres);
					}

					PET_STRONG_RES res = new PET_STRONG_RES(GameMessageFactory.nextSequnceNum(), petEntityId, (short) pet.getStarClass(), canStrong);
					player.addMessageToRightBag(res);
//					if (logger.isWarnEnabled()) logger.warn("[宠物强化后] [成功] [" + player.getLogString() + "] [" + pet.getLogString() + "] [" + oldStar + "] [" + pet.getStarClass() + "] [成功率:" + totalLuckValue + "] [实际随机数:" + resultValue + "]");
					if (logger.isWarnEnabled()) 
						logger.warn("[宠物强化后] [操作类别:幻化] [状态:成功] [" + player.getLogString4Knap() + "] [" + pet.getLogOfInterest() + "] [强化前星级:"+oldStar+"] [强化后星级"+pet.getStarClass() +"] [成功率:" + totalLuckValue + "] [实际随机数:" + resultValue + "]");
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
					// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null? ae.getArticleName():Translate.空白)+"] ["+(ae
					// != null? ae.getId():Translate.空白)+"] ["+description+"]", start);
					if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [" + player.getLogString() + "] [" + description + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]");
					return;
				}
			} else {
				String description = Translate.宠物已经强化到了上限;
				try {
					description = Translate.translateString(Translate.宠物已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
				player.addMessageToRightBag(hreq);
				// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null? ae.getArticleName():Translate.空白)+"] ["+(ae !=
				// null? ae.getId():Translate.空白)+"] ["+description+"]", start);
				if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			}
		}
	}

	/**
	 * 宠物强化确认
	 * @param player
	 * @param req
	 * @return
	 */
	public void confirmPetStrongReq(Player player, long petEntityId, long[] strongMaterialIds, long seqNum, int strongType) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		synchronized (player.tradeKey) {
			if (player != null && aem != null && am != null) {
				ArticleEntity aee = aem.getEntity(petEntityId);
				PetPropsEntity ae = null;
				if (aee instanceof PetPropsEntity) {
					ae = (PetPropsEntity) aee;
				}
				if (ae == null) {
					return;
				}
				Props p = (Props) am.getArticle(ae.getArticleName());
				if (p == null) {
					return;
				}
				PetManager petManager = PetManager.getInstance();
				Pet pet = petManager.getPet(ae.getPetId());

				if (player.getBindSilver() < 强化所需绑银文 * (pet.getStarClass() + 1)) {
					String description = "";
					try {
						description = Translate.translateString(Translate.宠物操作银子不足提示, new String[][] { { Translate.COUNT_1, 强化所需绑银两 * (pet.getStarClass() + 1) + "" } });
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null? ae.getArticleName():Translate.空白)+"] ["+(ae
					// !=
					// null? ae.getId():Translate.空白)+"] ["+description+","+strongMaterialIds+"的物品为空]", start);
					if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}

				if (strongMaterialIds == null) {
					String description = Translate.空白;
					description = Translate.请放入正确物品;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null? ae.getArticleName():Translate.空白)+"] ["+(ae
					// !=
					// null? ae.getId():Translate.空白)+"] ["+description+","+strongMaterialIds+"的物品为空]", start);
					if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				if (strongMaterialIds.length != strongMaterialMaxNumber) {
					String description = Translate.空白;
					description = Translate.请放入正确物品;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null? ae.getArticleName():Translate.空白)+"] ["+(ae
					// !=
					// null? ae.getId():Translate.空白)+"] ["+description+","+strongMaterialIds.length+"!="+strongMaterialMaxNumber+"]", start);
					if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{},{}!={}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialIds.length, strongMaterialMaxNumber, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					return;
				}
				if (pet.getStarClass() < starMaxValue) {
					String[] strongMaterialName = 根据宠物级别得到所需强化符(p.getLevelLimit());
					int[] strongMaterialColorLuck = 根据宠物强化等级得到不同颜色强化符的成功率分子值(pet.getStarClass() + 1);
					Knapsack[] knapsacks = player.getKnapsacks_common();
					ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
					for (long id : strongMaterialIds) {
						if (id != -1) {
							ArticleEntity strongMaterialEntity = aem.getEntity(id);
							if (strongMaterialEntity == null) {
								String description = Translate.空白;
								description = Translate.请放入正确物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
								// ae.getArticleName():Translate.空白)+"] ["+(ae != null? ae.getId():Translate.空白)+"] ["+description+"id为"+id+"的物品为空]", start);
								if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}id为{}的物品为空] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, id, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
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
								// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
								// ae.getArticleName():Translate.空白)+"] ["+(ae != null? ae.getId():Translate.空白)+"] ["+description+"]", start);
								if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
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
								description = Translate.请放入正确物品;
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
								player.addMessageToRightBag(hreq);
								// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
								// ae.getArticleName():Translate.空白)+"] ["+(ae != null? ae.getId():Translate.空白)+"] ["+description+"背包中没有id为"+strongMaterialEntity.getId()+"的物品]",
								// start);
								if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}背包中没有id为{}的物品] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, strongMaterialEntity.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
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
						// logger.warn("[宠物强化申请] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
						// ae.getArticleName():Translate.空白)+"] ["+(ae
						// != null? ae.getId():Translate.空白)+"] ["+description+"]", start);
						if (logger.isWarnEnabled()) logger.warn("[宠物强化申请] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					}
					boolean hasPET = false;
					if (knapsacks != null) {
						for (Knapsack knapsack : knapsacks) {
							if (knapsack != null) {
								if (!hasPET) {
									int index1 = knapsack.hasArticleEntityByArticleId(petEntityId);
									if (index1 != -1) {
										hasPET = true;
										break;
									}
								}
							}
						}
					}
					if (hasPET) {

						int totalLuckValue = 0;
						for (ArticleEntity strongMaterialEntity : strongMaterialEntitys) {
							if (strongMaterialEntity != null) {
								try {
									totalLuckValue += strongMaterialColorLuck[strongMaterialEntity.getColorType()];
								} catch (Exception ex) {
									PetManager.logger.error("[宠物强化计算几率] [" + player.getLogString() + "]", ex);
								}
							}
						}

						if (knapsacks != null) {
							for (long strongMaterialId : strongMaterialIds) {
								if (strongMaterialId != -1) {
									ArticleEntity aee1 = player.removeArticleEntityFromKnapsackByArticleId(strongMaterialId, "宠物强化", true);
									if (aee1 == null) {
										String description = Translate.删除物品不成功;
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
										player.addMessageToRightBag(hreq);
										// logger.warn("[宠物强化确认] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
										// ae.getArticleName():Translate.空白)+"] ["+(ae != null? ae.getId():Translate.空白)+"] ["+description+"]", start);
										if (logger.isWarnEnabled()) logger.warn("[宠物强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
										return;
									}
								}
							}
						}

						// 扣费
						try {
							BillingCenter bc = BillingCenter.getInstance();
							bc.playerExpense(player, 强化所需绑银文 * (pet.getStarClass() + 1), CurrencyType.BIND_YINZI, ExpenseReasonType.PET_STRONG, "宠物强化");
						} catch (Exception ex) {
							ex.printStackTrace();
							if (logger.isWarnEnabled()) logger.warn("[宠物强化确认] [失败] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "] [" + (ae != null ? ae.getArticleName() : Translate.空白) + "] [" + (ae != null ? ae.getId() : Translate.空白) + "]", ex);
							return;
						}

						int resultValue = random.nextInt(TOTAL_LUCK_VALUE) + 1;
						boolean success = false;
						if (totalLuckValue >= resultValue) {
							success = true;
						}
						boolean canStrong = true;
						int oldStar = pet.getStarClass();

						AchievementManager.getInstance().record(player, RecordAction.宠物幻化次数);
						ActivenessManager.getInstance().record(player, ActivenessType.强化宠物);
						if (AchievementManager.logger.isWarnEnabled()) {
							AchievementManager.logger.warn("[成就统计宠物幻化次数] [" + player.getLogString() + "]");
						}

						if (success) {
							pet.setStarClass((byte) (pet.getStarClass() + 1));
							if (pet.getStarClass() >= starMaxValue) {
								pet.setStarClass((byte) starMaxValue);
								canStrong = false;
							}
							String description = Translate.宠物强化成功;
							try {
								description = Translate.translateString(Translate.宠物强化成功, new String[][] { new String[] { Translate.LEVEL_1, pet.getStarClass() + "" } });
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);
							QUERY_ARTICLE_INFO_RES qres = new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), ae.getId(), ae.getInfoShow(player));
							player.addMessageToRightBag(qres);

							PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.宠物强化成功, (byte) 1, AnimationManager.动画播放位置类型_宠物强化升星, 0, 0);
							if (pareq != null) {
								player.addMessageToRightBag(pareq);
								// logger.warn("[强化成功播放动画] [] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]");
								if (logger.isWarnEnabled()) logger.warn("[强化成功播放动画] [] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
							}
						} else {

							// 强化失败降级
							int dropStar = 强化失败后降级();
							if (pet.getStarClass() <= dropStar) {
								pet.setStarClass((byte) 0);
							} else {
								pet.setStarClass((byte) (pet.getStarClass() - dropStar));
							}

							String description = Translate.很遗憾宠物没有强化成功;
							try {
								description = Translate.translateString(Translate.宠物强化失败, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() }, new String[] { Translate.LEVEL_1, pet.getStarClass() + "" } });
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
							player.addMessageToRightBag(hreq);

							PLAY_ANIMATION_REQ pareq = AnimationManager.组织播放动画协议(AnimationManager.宠物强化失败, (byte) 1, AnimationManager.动画播放位置类型_宠物强化升星, 0, 0);
							if (pareq != null) {
								player.addMessageToRightBag(pareq);
								// logger.warn("[强化失败播放动画] [] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]");
								if (logger.isWarnEnabled()) logger.warn("[强化失败播放动画] [] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
							}

						}

						QUERY_PET_STRONG_RES qres = queryPetByArticleEntityId(player, petEntityId);
						if (qres != null) {
							player.addMessageToRightBag(qres);
						}

						PET_STRONG_RES res = new PET_STRONG_RES(seqNum, petEntityId, (short) pet.getStarClass(), canStrong);
						player.addMessageToRightBag(res);
						// logger.warn("[宠物强化确认] [成功] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
						// ae.getArticleName():Translate.空白)+"] ["+(ae
						// != null? ae.getId():Translate.空白)+"] ["+oldStar+"] ["+pet.getStarClass()+"] ["+totalLuckValue+"] ["+resultValue+"]", start);
						if (logger.isWarnEnabled()) logger.warn("[宠物强化确认] [幻化] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), oldStar, pet.getStarClass(), totalLuckValue, resultValue, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						return;
					} else {
						String description = Translate.宠物已经强化到了上限;
						try {
							description = Translate.translateString(Translate.宠物已经强化到了顶级, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } });
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						player.addMessageToRightBag(hreq);
						// logger.warn("[宠物强化确认] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null?
						// ae.getArticleName():Translate.空白)+"] ["+(ae
						// != null? ae.getId():Translate.空白)+"] ["+description+"]", start);
						if (logger.isWarnEnabled()) logger.warn("[宠物强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
				} else {
					String description = Translate.空白;
					description = Translate.请放入正确物品;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					// logger.warn("[宠物强化确认] [失败] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+(ae != null? ae.getArticleName():Translate.空白)+"] ["+(ae
					// !=
					// null? ae.getId():Translate.空白)+"] ["+description+"]", start);
					if (logger.isWarnEnabled()) logger.warn("[宠物强化确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}ms]", new Object[] { player.getUsername(), player.getId(), player.getName(), (ae != null ? ae.getArticleName() : Translate.空白), (ae != null ? ae.getId() : Translate.空白), description, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				}
			}
		}
	}

	public int 强化失败后降级() {
		int result = 0;
		result = ProbabilityUtils.randomProbability(random, new double[] { 0.80, 0.15, 0.5 });
		return result + 1;
	}

	/**
	 * 宠物封印
	 * @param entity
	 * @param p
	 */
	public boolean petSeal(PetPropsEntity entity, Player p) {

		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		long petId = entity.getPetId();

		if (petId == p.getActivePetId()) {
			p.sendError(Translate.pet_fight);
			return false;
		}
		Pet pet = this.getPet(petId);
		if (pet != null && pet.getHookInfo() != null) {
			p.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
			return false;
		}
		
		for(long pid : p.getPetCell()){
			if(pid == pet.getId()){
				p.sendError(Translate.助战中的宠物不能封印);
				return false;
			}
		}
		
		if(PetHouseManager.getInstance().petIsStore(p, pet)){
			p.sendError(Translate.挂机中的宠物不能封印);
			return false; 
		}
		
		Article petEgg = ArticleManager.getInstance().getArticle(entity.getEggArticle());
		if (!(petEgg instanceof PetEggProps)) {
			// logger.warn("[宠物封印失败] [没有对应的宠物蛋物种]["+entity.getEggArticle()+"] ["+p.getUsername()+"] ["+p.getId()+"] ["+p.getName()+"]", start);
			if (logger.isWarnEnabled()) logger.warn("[宠物封印失败] [没有对应的宠物蛋物种][{}] [{}] [{}] [{}] [{}ms]", new Object[] { entity.getEggArticle(), p.getUsername(), p.getId(), p.getName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			// p.sendError(Translate.宠物封印失败背包已满);
			return false;

		}
		try {
			PetEggPropsEntity pep = (PetEggPropsEntity) ArticleEntityManager.getInstance().createEntity((PetEggProps) petEgg, false, ArticleEntityManager.CREATE_REASON_PET_SEAL, p, 0, 1, true);
			pep.setPetId(petId);
			if (pet.isIdentity()) {
				switch (pet.getQuality()) {
				case 0:
					pep.setColorType(0);
					break;
				case 1:
					pep.setColorType(1);
					break;
				case 2:
					pep.setColorType(2);
					break;
				case 3:
					pep.setColorType(3);
					break;
				case 4:
					pep.setColorType(4);
					break;
				default:
					break;

				}
			} else {
				pep.setColorType(0);
			}

			boolean bln = p.putToKnapsacks(pep, "宠物");
			if (!bln) {
				// logger.warn("[宠物封印失败] [把生成的宠物蛋没有放入包裹] ["+p.getUsername()+"] ["+p.getId()+"] ["+p.getName()+"]", start);
				if (logger.isWarnEnabled()) logger.warn("[宠物封印失败] [把生成的宠物蛋没有放入包裹] [{}] [{}] [{}] [{}ms]", new Object[] { p.getUsername(), p.getId(), p.getName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				p.sendError(Translate.宠物封印失败背包已满);
				return false;
			} else {
				ArticleStatManager.addToArticleStat(p, null, pep, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "宠物封印获得宠物蛋", null);
				ArticleEntity ae = p.removeArticleEntityFromKnapsackByArticleId(entity.getId(), "宠物封印", false);
				if (ae == null) {
					// logger.warn("[宠物封印失败] [删除宠物道具] ["+p.getUsername()+"] ["+p.getId()+"] ["+p.getName()+"]", start);
					if (logger.isWarnEnabled()) {
						logger.warn("[宠物封印失败] [删除宠物道具失败] [{}] [{}] [{}] [{}ms]", new Object[] { p.getUsername(), p.getId(), p.getName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					}
					p.sendError(Translate.宠物封印失败背包已满);
					return false;
				}
				ArticleStatManager.addToArticleStat(p, null, pep, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "宠物封印删除宠物", null);
				pet.setPetPropsId(pep.getId());
				if (ArticleProtectManager.instance.getBlockState(p, pep.getId()) > 0) {
					ArticleProtectManager.instance.send_ARTICLEPROTECT_MSG_RES(p);
				}
//				PetManager.logger.warn("[封印宠物成功] [" + p.getLogString() + "] [宠物道具:"+entity.getArticleName()+"] [" + pet.getLogString() + "]");
				if(PetManager.logger.isWarnEnabled()){
					PetManager.logger.warn("[封印宠物] [成功] [{}] {} [宠物封印后道具:{}] [id:{}]",new Object[]{p.getLogString4Knap(),pet.getLogOfInterest(),pep.getArticleName(),pep.getId()});
				}
				return true;
			}
		} catch (Exception ex) {
			PetManager.logger.error("[宠物封印] [" + p.getLogString() + "]", ex);
		}
		return false;
	}

	// 要是玩家身上没有炼妖石，生成一个，为了可以显示泡泡
	private long repairArticleId = -1;

	// 为了生成一个炼妖石
	private Object syn = new Object();

	public long[] 得到炼妖的三种石头Ids() throws Exception {

		if (炼妖石ids == null) {
			炼妖石ids = new long[3];
			for (int i = 0; i < 炼妖石names.length; i++) {
				String st = 炼妖石names[i];
				Article a = ArticleManager.getInstance().getArticle(st);
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_TEMP_ARTICLE, null, 0, 1, false);
				if (ae != null) {
					炼妖石ids[i] = ae.getId();
				}
			}
		}
		return 炼妖石ids;

	}

	private int[] 得到炼妖的三个vip级别() {
		int 中级 = VipManager.getInstance().vip开启中级炼妖级别();
		int 高级 = VipManager.getInstance().vip开启高级炼妖级别();
		int[] levels = new int[] { 0, 中级, 高级 };
		return levels;
	}

	/**
	 * 
	 * @param player
	 * @param pet
	 * @param force
	 * @param consume
	 *            是否消费银子(true)
	 * @return
	 */
	public String repairPet(Player player, Pet pet, boolean force, int flushLevel, boolean consume) {

		if (flushLevel < 0 || flushLevel > 2) {
			return null;
		}

		ArticleEntity entity = null;
		Article repairArticle = null;
		synchronized (player.tradeKey) {
			if (force) {

				String articleName = 炼妖石names[flushLevel];

				int 炼妖需要银子文 = 炼妖花费[flushLevel] * 1000;
				int 炼妖需要银子两 = 炼妖花费[flushLevel];

				// 宠物不能为出战状态
				if (pet.isSummoned()) {
					return Translate.text_pet_37;
				}
				if (!pet.isIdentity()) {
					return Translate.text_炼化宠物必须是鉴定过的宠物;
				}

				for(long pid : player.getPetCell()){
					if(pid == pet.getId()){
						player.sendError(Translate.助战中的宠物不能封印);
						return null;
					}
				}
				
				if(PetHouseManager.getInstance().petIsStore(player, pet)){
					player.sendError(Translate.挂机中的宠物不能封印);
					return null; 
				}
				
				if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_High, pet.getPetPropsId())) {
					player.sendError(Translate.高级锁魂物品不能做此操作);
					return null;
				}

				ArticleEntity ae = null;
				if (!consume) {
					// 用炼妖石炼化
					entity = player.getArticleEntity(articleName);

					if (entity == null) {
						return Translate.translateString(Translate.text_缺少炼妖物品, new String[][] { { Translate.ARTICLE_NAME_1, articleName } });
					} else {
						ae = player.removeArticleEntityFromKnapsackByArticleId(entity.getId(), "宠物炼化", true);
						if (ae != null) {
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "宠物炼妖删除", null);
						}
					}
				} else {
					// 用炼妖石炼化
					entity = player.getArticleEntity(articleName);

					if (entity == null) {
						// return Translate.translateString(Translate.text_缺少炼妖物品, new String[][]{{Translate.ARTICLE_NAME_1,articleName}});
						// 用钱炼化
						if (player.getSilver() + player.getShopSilver() < 炼妖需要银子文) {

							String description = "";
							try {
								description = Translate.translateString(Translate.宠物操作银子不足提示, new String[][] { { Translate.COUNT_1, 炼妖需要银子两 + "" } });
								BillingCenter.银子不足时弹出充值确认框(player, description);
								PetManager.logger.error("[炼妖银子不足] [" + player.getLogString() + "] [" + player.getSilver() + "]");
								return null;
							} catch (Exception ex) {
								PetManager.logger.error("[炼妖判断银子异常] [" + player.getLogString() + "] [" + player.getSilver() + "]", ex);
							}
							return description;
						} else {
							BillingCenter bc = BillingCenter.getInstance();
							try {
								bc.playerExpense(player, 炼妖需要银子文, CurrencyType.SHOPYINZI, ExpenseReasonType.PET_REPAIR, "宠物炼化", VipExpActivityManager.lianyao_expends_activity);
								NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, 炼妖需要银子文, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
//								if (logger.isWarnEnabled()) logger.warn("[宠物炼妖成功] [消耗银子文:" + 炼妖需要银子文 + "] [" + player.getLogString() + "]");
								if (logger.isWarnEnabled()) 
									logger.warn("[宠物炼妖] [成功] [{}] {} [消耗银子文:{}]",new Object[]{player.getLogString4Knap(),pet.getLogOfInterest(), 炼妖需要银子文});
							} catch (Exception ex) {
								String result = Translate.扣费失败;
								return result;
							}
						}
					} else {
						ae = player.removeArticleEntityFromKnapsackByArticleId(entity.getId(), "宠物", true);
						if (ae != null) {
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "宠物炼妖删除", null);
						}
					}
				}

				int growthClass = PetPropertyUtility.随机获得成长率等级(pet.getRealTrainLevel());
				pet.setTempGrowthClass((byte) growthClass);
				pet.setReplace(false);
				int achieveStat = pet.createRandom(false, flushLevel);

				pet.setRepairNum(pet.getRepairNum() + 1);

				if (achieveStat >= 0) {
					AchievementManager.getInstance().record(player, RecordAction.宠物炼妖次数);
					if (AchievementManager.logger.isWarnEnabled()) {
						AchievementManager.logger.warn("[成就统计宠物炼妖次数] [" + player.getLogString() + "]");
					}
					if (achieveStat > 0) {
						AchievementManager.getInstance().record(player, RecordAction.宠物炼妖属性满值个数, achieveStat);
						if (AchievementManager.logger.isWarnEnabled()) {
							AchievementManager.logger.warn("[成就统计宠物炼妖属性满值个数] [" + player.getLogString() + "]");
						}
					}
				}
				if (logger.isWarnEnabled()) {
					logger.warn("[强制宠物成功] [" + player.getLogString() + "] [" + pet.getLogString() + "]");
				}
			}
		}

		int strengthQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到力量初值(pet, true, 2) / 100));
		int dexterityQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到身法初值(pet, true, 2) / 100));
		int spellpowerQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, true, 2) / 100));
		int constitutionQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到耐力初值(pet, true, 2) / 100));
		int dingliQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到定力初值(pet, true, 2) / 100));

		int minStrengthQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到力量初值(pet, true, 1) / 100));
		int minDexterityQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到身法初值(pet, true, 1) / 100));
		int minSpellpowerQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, true, 1) / 100));
		int minConstitutionQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到耐力初值(pet, true, 1) / 100));
		int minDingliQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到定力初值(pet, true, 1) / 100));

		int maxStrengthQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到力量初值(pet, true, 0) / 100));
		int maxDexterityQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到身法初值(pet, true, 0) / 100));
		int maxSpellpowerQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, true, 0) / 100));
		int maxConstitutionQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到耐力初值(pet, true, 0) / 100));
		int maxDingliQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到定力初值(pet, true, 0) / 100));

		int showStrengthQuality = PetPropertyUtility.得到实际力量资质(pet, true, 2);
		int showDexterityQuality = PetPropertyUtility.得到实际身法资质(pet, true, 2);
		int showSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(pet, true, 2);
		int showConstitutionQuality = PetPropertyUtility.得到实际耐力资质(pet, true, 2);
		int showDingliQuality = PetPropertyUtility.得到实际定力资质(pet, true, 2);

		int showMinStrengthQuality = PetPropertyUtility.得到实际力量资质(pet, true, 1);
		int showMinDexterityQuality = PetPropertyUtility.得到实际身法资质(pet, true, 1);
		int showMinSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(pet, true, 1);
		int showMinConstitutionQuality = PetPropertyUtility.得到实际耐力资质(pet, true, 1);
		int showMinDingliQuality = PetPropertyUtility.得到实际定力资质(pet, true, 1);

		int showMaxStrengthQuality = PetPropertyUtility.得到实际力量资质(pet, true, 0);
		int showMaxDexterityQuality = PetPropertyUtility.得到实际身法资质(pet, true, 0);
		int showMaxSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(pet, true, 0);
		int showMaxConstitutionQuality = PetPropertyUtility.得到实际耐力资质(pet, true, 0);
		int showMaxDingliQuality = PetPropertyUtility.得到实际定力资质(pet, true, 0);

		byte tempGrowthClass = 0;
		byte tempQuality = 0;
		int tempstrengthQualityInit = 0;
		int tempdexterityQualityInit = 0;
		int tempspellpowerQualityInit = 0;
		int tempconstitutionQualityInit = 0;
		int tempdingliQualityInit = 0;

		int tempminStrengthQualityInit = 0;
		int tempminDexterityQualityInit = 0;
		int tempminSpellpowerQualityInit = 0;
		int tempminConstitutionQualityInit = 0;
		int tempminDingliQualityInit = 0;

		int tempmaxStrengthQualityInit = 0;
		int tempmaxDexterityQualityInit = 0;
		int tempmaxSpellpowerQualityInit = 0;
		int tempmaxConstitutionQualityInit = 0;
		int tempmaxDingliQualityInit = 0;

		int tempshowStrengthQuality = 0;
		int tempshowDexterityQuality = 0;
		int tempshowSpellpowerQuality = 0;
		int tempshowConstitutionQuality = 0;
		int tempshowDingliQuality = 0;

		int tempshowMinStrengthQuality = 0;
		int tempshowMinDexterityQuality = 0;
		int tempshowMinSpellpowerQuality = 0;
		int tempshowMinConstitutionQuality = 0;
		int tempshowMinDingliQuality = 0;

		int tempshowMaxStrengthQuality = 0;
		int tempshowMaxDexterityQuality = 0;
		int tempshowMaxSpellpowerQuality = 0;
		int tempshowMaxConstitutionQuality = 0;
		int tempshowMaxDingliQuality = 0;

		int maxscore = this.getScoreByProperty(maxStrengthQualityInit, maxDexterityQualityInit, maxSpellpowerQualityInit, maxConstitutionQualityInit, maxDingliQualityInit, showMaxStrengthQuality, showMaxDexterityQuality, showMaxSpellpowerQuality, showMaxConstitutionQuality, showMaxDingliQuality);
		int realScore = this.getScoreByProperty(strengthQualityInit, dexterityQualityInit, spellpowerQualityInit, constitutionQualityInit, dingliQualityInit, showStrengthQuality, showDexterityQuality, showSpellpowerQuality, showConstitutionQuality, showDingliQuality);

		int score = (int) ((1f * realScore / maxscore) * 1000 * (PetPropertyUtility.获得携带等级索引(pet.getRealTrainLevel()) + 1));

		int tempscore = 0;
		int realTempscore = 0;
		if (!pet.isReplace()) {
			tempGrowthClass = pet.getTempGrowthClass();
			tempQuality = (byte) PetPropertyUtility.判定宠物品质(pet, false);
			tempstrengthQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到力量初值(pet, false, 2) / 100));
			tempdexterityQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到身法初值(pet, false, 2) / 100));
			tempspellpowerQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, false, 2) / 100));
			tempconstitutionQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到耐力初值(pet, false, 2) / 100));
			tempdingliQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到定力初值(pet, false, 2) / 100));

			tempminStrengthQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到力量初值(pet, false, 1) / 100));
			tempminDexterityQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到身法初值(pet, false, 1) / 100));
			tempminSpellpowerQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, false, 1) / 100));
			tempminConstitutionQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到耐力初值(pet, false, 1) / 100));
			tempminDingliQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到定力初值(pet, false, 1) / 100));

			tempmaxStrengthQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到力量初值(pet, false, 0) / 100));
			tempmaxDexterityQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到身法初值(pet, false, 0) / 100));
			tempmaxSpellpowerQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, false, 0) / 100));
			tempmaxConstitutionQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到耐力初值(pet, false, 0) / 100));
			tempmaxDingliQualityInit = (int) Math.rint((1.0 * PetPropertyUtility.得到定力初值(pet, false, 0) / 100));

			tempshowStrengthQuality = PetPropertyUtility.得到实际力量资质(pet, false, 2);
			tempshowDexterityQuality = PetPropertyUtility.得到实际身法资质(pet, false, 2);
			tempshowSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(pet, false, 2);
			tempshowConstitutionQuality = PetPropertyUtility.得到实际耐力资质(pet, false, 2);
			tempshowDingliQuality = PetPropertyUtility.得到实际定力资质(pet, false, 2);

			tempshowMinStrengthQuality = PetPropertyUtility.得到实际力量资质(pet, false, 1);
			tempshowMinDexterityQuality = PetPropertyUtility.得到实际身法资质(pet, false, 1);
			tempshowMinSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(pet, false, 1);
			tempshowMinConstitutionQuality = PetPropertyUtility.得到实际耐力资质(pet, false, 1);
			tempshowMinDingliQuality = PetPropertyUtility.得到实际定力资质(pet, false, 1);

			tempshowMaxStrengthQuality = PetPropertyUtility.得到实际力量资质(pet, false, 0);
			tempshowMaxDexterityQuality = PetPropertyUtility.得到实际身法资质(pet, false, 0);
			tempshowMaxSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(pet, false, 0);
			tempshowMaxConstitutionQuality = PetPropertyUtility.得到实际耐力资质(pet, false, 0);
			tempshowMaxDingliQuality = PetPropertyUtility.得到实际定力资质(pet, false, 0);

			realTempscore = this.getScoreByProperty(tempstrengthQualityInit, tempdexterityQualityInit, tempspellpowerQualityInit, tempconstitutionQualityInit, tempdingliQualityInit, tempshowStrengthQuality, tempshowDexterityQuality, tempshowSpellpowerQuality, tempshowConstitutionQuality, tempshowDingliQuality);
			tempscore = (int) ((1f * realTempscore / maxscore) * 1000 * (PetPropertyUtility.获得携带等级索引(pet.getRealTrainLevel()) + 1));
		}
		pet.tempyaohunScore = tempscore;
		pet.yaohunScore = score;
		try {
			EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.宠物最大妖魂值, score});
			EventRouter.getInst().addEvent(evt2);
		} catch (Exception eex) {
			PlayerAimManager.logger.error("[目标系统] [统计宠物最大炼妖值异常][" + player.getLogString() + "]", eex);
		}

		PET_REPAIR_RES res;
		res = new PET_REPAIR_RES(GameMessageFactory.nextSequnceNum(), 炼妖石ids, 炼妖石names, 得到炼妖的三个vip级别(), 炼妖花费, pet.getRepairNum(), score, pet.getGrowthClass(), pet.getQuality(), strengthQualityInit, dexterityQualityInit, spellpowerQualityInit, constitutionQualityInit, dingliQualityInit, minStrengthQualityInit, minDexterityQualityInit, minSpellpowerQualityInit, minConstitutionQualityInit, minDingliQualityInit, maxStrengthQualityInit, maxDexterityQualityInit, maxSpellpowerQualityInit, maxConstitutionQualityInit, maxDingliQualityInit, showStrengthQuality, showDexterityQuality, showSpellpowerQuality, showConstitutionQuality, showDingliQuality, showMinStrengthQuality, showMinDexterityQuality, showMinSpellpowerQuality, showMinConstitutionQuality, showMinDingliQuality, showMaxStrengthQuality, showMaxDexterityQuality, showMaxSpellpowerQuality, showMaxConstitutionQuality, showMaxDingliQuality, pet.isReplace(), tempscore, tempGrowthClass, tempQuality, tempstrengthQualityInit, tempdexterityQualityInit, tempspellpowerQualityInit, tempconstitutionQualityInit, tempdingliQualityInit, tempminStrengthQualityInit, tempminDexterityQualityInit, tempminSpellpowerQualityInit, tempminConstitutionQualityInit, tempminDingliQualityInit, tempmaxStrengthQualityInit, tempmaxDexterityQualityInit, tempmaxSpellpowerQualityInit, tempmaxConstitutionQualityInit, tempmaxDingliQualityInit, tempshowStrengthQuality, tempshowDexterityQuality, tempshowSpellpowerQuality, tempshowConstitutionQuality, tempshowDingliQuality, tempshowMinStrengthQuality, tempshowMinDexterityQuality, tempshowMinSpellpowerQuality, tempshowMinConstitutionQuality, tempshowMinDingliQuality, tempshowMaxStrengthQuality, tempshowMaxDexterityQuality, tempshowMaxSpellpowerQuality, tempshowMaxConstitutionQuality, tempshowMaxDingliQuality);
		player.addMessageToRightBag(res);
		
		if (logger.isInfoEnabled()) {
			logger.info("[发送炼化宠物返回协议成功] [炼妖] [" + player.getLogString() + "] [" + pet.getName() + "] [分数对比:" + score + "+:" + tempscore + "] [max分:" + maxscore + "] [真实得分:" + realScore + "] [炼化后得分:" + realTempscore + "][品质:" + pet.getQuality() + "] [成长率:" + pet.getGrowthClass() + "] [temp品质:" + tempQuality + "] [temp成长率:" + tempGrowthClass + "][最大灵力初值：" + (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, true, 0) / 100)) + "] [最小灵力：" + (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, true, 1) / 100)) + "] [灵力：" + (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, true, 2) / 100)) + "] " + "[temp最大灵力初值：" + (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, false, 0) / 100)) + "] [temp最小灵力：" + (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, false, 1) / 100)) + "] [temp灵力：" + (int) Math.rint((1.0 * PetPropertyUtility.得到灵力初值(pet, false, 2) / 100)) + "]");
		}
		return "";
	}

	private int getScoreByProperty(int strengthQualityInit, int dexterityQualityInit, int spellpowerQualityInit, int constitutionQualityInit, int dingliQualityInit, int showStrengthQuality, int showDexterityQuality, int showSpellpowerQuality, int showConstitutionQuality, int showDingliQuality) {
		return strengthQualityInit + dexterityQualityInit + spellpowerQualityInit + constitutionQualityInit + dingliQualityInit + showStrengthQuality + showDexterityQuality + showSpellpowerQuality + showConstitutionQuality + showDingliQuality;
	}

	public String replacePet(Player player, Pet pet) {

		if (!pet.isReplace()) {
			// 已经炼化过
			pet.setReplace(true);
			float[] temp = pet.getBeforeReplaceRandom();
			pet.setRealRandom(temp.clone());
			pet.setGrowthClass(pet.getTempGrowthClass());
			int quality = PetPropertyUtility.判定宠物品质(pet, true);
			pet.setQuality((byte) quality);
			pet.init();
			pet.yaohunScore = pet.tempyaohunScore;
			try {
				EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.宠物最大妖魂值, pet.yaohunScore});
				EventRouter.getInst().addEvent(evt2);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计宠物最大炼妖值异常][" + player.getLogString() + "]", eex);
			}
			if (logger.isWarnEnabled()) logger.warn("[炼化宠物后替换成功] [" + player.getLogString() + "] [" + pet.getName() + "]");
		} else {
			return Translate.text_你还没有炼化;
		}
		return null;
	}

	public String opratePetAllocatePoint(Pet pet, byte oprate) {

		if (oprate == 0) {
			// 确定
			if (pet.getTempPoints() != null) {
				pet.confirmUnAllocate();
				return "";
			} else {
				return Translate.宠物加点确定没有分配点;
			}

		} else if (oprate == 1) {
			// 取消
			if (pet.getTempPoints() != null) {
				pet.cancleUnAllocate();
				return "";
			} else {
				return Translate.宠物加点取消没有分配点;
			}

		} else if (oprate == 2) {
			// 关闭窗口
			pet.cancleUnAllocate();
		}
		return null;
	}

	public void setPetSkillMap(Map<Integer, Map<Integer, List<Integer>>> petSkillMap) {
		this.petSkillMap = petSkillMap;
	}

	public Map<Integer, Map<Integer, List<Integer>>> getPetSkillMap() {
		return petSkillMap;
	}

	public Map<Integer, PetSkillReleaseProbability> getMap() {
		return map;
	}

	public void setMap(Map<Integer, PetSkillReleaseProbability> map) {
		this.map = map;
	}

	public void strongSendMessage(Pet pet, Player player) {
		if (pet.getStarClass() >= Pet.幻化广播的等级) {
			if (pet.getStarClass() >= pet.getAlreadyMaxStar()) {
				pet.setAlreadyMaxStar(pet.getStarClass());
				// 发送广播世界
				ChatMessageService cms = ChatMessageService.getInstance();
				int messageStar = 0;
				if (pet.getStarClass() % 2 == 0) {
					messageStar = pet.getStarClass();
				} else {
					return;
				}
				ChatMessage msg = new ChatMessage();
				// 恭喜！<国家>的<玩家姓名>经历千辛万苦将<宠物名>炼到<星级>。（世界）
				String des = Translate.translateString(Translate.恭喜xx国的xx人经历千辛万苦将xx宠物炼到xx星等, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, pet.getName() }, { Translate.COUNT_1, messageStar + "" } });

				int level = pet.getStarClass() / 2;
				int colorLevel = level - 5;
				StringBuffer sb = new StringBuffer();
				if (colorLevel >= 0) {
					if (colorLevel < ArticleManager.pet_strong_color.length) {
						sb.append("<f color=' ");
						sb.append(ArticleManager.pet_strong_color[colorLevel]);
						sb.append("'>");
						sb.append(des);
						sb.append("</f>");
					} else {
						sb.append("<f color=' ");
						sb.append(ArticleManager.pet_strong_color[ArticleManager.pet_strong_color.length - 1]);
						sb.append("'>");
						sb.append(des);
						sb.append("</f>");
					}
				}
				msg.setMessageText(sb.toString());
				try {
					cms.sendMessageToSystem(msg);
					if (PetManager.logger.isDebugEnabled()) PetManager.logger.debug("[宠物幻化世界广播] [" + player.getLogString() + "] [" + pet.getLogString() + "] [幻化到的星等:" + pet.getStarClass() + "]");
				} catch (Exception e) {
					PetManager.logger.error("[宠物幻化世界广播错误] [" + player.getLogString() + "] [" + pet.getLogString() + "] [幻化到的星等:" + pet.getStarClass() + "]", e);
				}
			}
		}
	}

	public static String[] horseNames = { 宠物青龙, 宠物玄武, 宠物朱雀, 宠物白虎, 宠物麒麟 };
	public static RecordAction[] RecordActionNames = { RecordAction.获得青龙宠物, RecordAction.获得玄武宠物, RecordAction.获得朱雀宠物, RecordAction.获得白虎宠物, RecordAction.获得麒麟宠物 };

	public void getPetAchievement(Player player, PetEggProps petProps) {
		try {
			boolean bln = false;
			String propsName = petProps.getName();
			int j = 0;
			for (int i = 0; i < horseNames.length; i++) {
				if (propsName.contains(horseNames[i])) {
					bln = true;
					j = i;
					break;
				}
			}
			RecordAction ra = RecordActionNames[j];
			if (bln) {
				AchievementManager.getInstance().record(player, ra);
				if (AchievementManager.logger.isWarnEnabled()) {
					AchievementManager.logger.warn("[成就统计获得圣兽] [" + player.getLogString() + "] [" + propsName + "]");
				}

				// AchievementManager.getInstance().record(player,RecordAction.获得所有圣兽);
				// if(AchievementManager.logger.isWarnEnabled()){
				// AchievementManager.logger.warn("[成就统计获得圣兽累计] ["+player.getLogString()+"] ["+propsName+"]");
				// }
			} else {
				if (AchievementManager.logger.isDebugEnabled()) {
					AchievementManager.logger.debug("[成就统计获得圣兽错误] [" + player.getLogString() + "] [" + propsName + "]");
				}
			}
		} catch (Exception e) {
			HorseManager.logger.error("[使用宠物记录玩家成就异常] [" + player.getLogString() + "] [" + petProps.getName() + "]", e);
		}
	}

	public List<PetCellInfo> getCellInfos() {
		return cellInfos;
	}

	public void setCellInfos(List<PetCellInfo> cellInfos) {
		this.cellInfos = cellInfos;
	}
	

}
