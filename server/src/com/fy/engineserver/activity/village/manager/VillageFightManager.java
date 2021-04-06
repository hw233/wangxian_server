package com.fy.engineserver.activity.village.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.village.data.OreNPCData;
import com.fy.engineserver.activity.village.data.VillageData;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiaZuLivenessType;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.villagefight.Option_VillageFight_ShenqingQueren;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.SEND_VILLAGE_CIRCLE_DIS_REQ;
import com.fy.engineserver.message.SEND_VILLAGE_CIRCLE_REQ;
import com.fy.engineserver.message.SEND_VILLAGE_STATE_REQ;
import com.fy.engineserver.message.SHOOK_DICE_RES;
import com.fy.engineserver.sprite.CountdownAgent;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.sprite.npc.OreNPC;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class VillageFightManager implements Runnable {

	private static VillageFightManager self;

	private boolean running;

	private VillageFightManager() {

	}

	public static Logger logger = LoggerFactory.getLogger(VillageFightManager.class);

	public static VillageFightManager getInstance() {
		return self;
	}

	NPCManager nm;
	GameManager gm;
	public SimpleEntityManager<VillageData> em;

	public SimpleEntityManager<VillageData> getEm() {
		return em;
	}

	public void setEm(SimpleEntityManager<VillageData> em) {
		this.em = em;
	}

	public NPCManager getNm() {
		return nm;
	}

	public void setNm(NPCManager nm) {
		this.nm = nm;
	}

	public GameManager getGm() {
		return gm;
	}

	public void setGm(GameManager gm) {
		this.gm = gm;
	}

	/**
	 * 矿所在的地图名字
	 */
//	public static String[] countryMapName = new String[] { "kunlunyaosai", "yaosai", "bianjing" };
	public static String[][] countryMapNames = new String[][] {{}, {"shengtianzhidao", "kunlunyaosai", "bianjing"},{"jiuzhoutianlu", "jiuzhouyaosai", "bianjing"},{"fayuandadao", "wanfayaosai", "bianjing"} };
	
//	昆仑地图名{昆仑佳梦关，昆仑要塞，昆仑边境}, 昆仑坐标x,y:{[1,1],[2,2],[3,3]}，昆仑进攻方复活点x,y{[1,1],[2,2],[3,3]}，昆仑防御方复活点x,y{[1,1],[2,2],[3,3]}
//	九州地图名{九州佳梦关，九州要塞，九州边境}, 九州坐标x,y:{[1,1],[2,2],[3,3]}，九州进攻方复活点x,y{[1,1],[2,2],[3,3]}，九州防御方复活点x,y{[1,1],[2,2],[3,3]}
//	万法地图名{万法佳梦关，万法要塞，万法边境}, 万法坐标x,y:{[1,1],[2,2],[3,3]}，万法进攻方复活点x,y{[1,1],[2,2],[3,3]}，万法防御方复活点x,y{[1,1],[2,2],[3,3]}
	
	
	// static {
	// if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
	// countryMapName = new String[] { "jiamengguan", "yaosai", "bianjing" };
	// } else {
	// countryMapName = new String[] { "jiamengguan", "yaosai", "bianjing" };
	//
	// }
	// VillageFightManager.logger.warn(PlatformManager.getInstance().getPlatform().toString() + ":" + Arrays.toString(countryMapName));
	// }

	public static int SHOOK_TIME = 10;// 单位s

	/**
	 * 贫富矿的categoryId
	 */
	private int[] npcCategorys = new int[] { 500000001, 500000002, 500000003 };
	private String[] npcCategoryNames = new String[] { Translate.灵眼, Translate.灵泉, Translate.灵脉 };

	public int 得到矿的类型id(byte 矿类型) {
		if (矿类型 >= 0 && 矿类型 < npcCategorys.length) {
			return npcCategorys[矿类型];
		}
		return -1;
	}

	public String 得到矿的名字(byte 矿类型) {
		if (矿类型 >= 0 && 矿类型 < npcCategorys.length) {
			return npcCategoryNames[矿类型];
		}
		return "";
	}

	/**
	 * 矿所在位置
	 */
//	public final int[][] points = new int[][] { { 1937, 2171 }, { 5262, 2008 }, { 3541, 626 }, { 3447, 3896 }, { 1814, 3599 }, { 5, 58 } };
	public final int[][][] points2 = new int[][][] {{}, {{5206,4606},{7183,3890},{3134,1919},{4116,9492},{5236,4001},{510,5866}} , 
													{{11225,6610},{1052,7567},{8050,1404},{3872,8918},{5236,4001},{510,5866}} , 
													{{10137,3638},{1580,6119},{8937,970},{8017,9536},{5236,4001},{510,5866}}};

	/**
	 * 矿周围的复活点
	 * 第二维数组中的下标分别表示防守方和进攻方的复活点
	 */
	private int[][] relivePoints = new int[][] { { 756,2182, 4579,933 }, { 756,2182, 4579,933 }, { 9502,4158, 6706,5374 }, {  9502,4158, 6706,5374 }, { 1225,2282, 3742,4175 }, { 1225,2282, 3742,4175} };
	private int[][] relivePoints2 = new int[][] { { 877,2012, 4077,2142 }, { 877,2012, 4077,2142 }, { 9169,3886, 7337,5198}, {  9169,3886, 7337,5198 }, { 1225,2282, 3742,4175 }, { 1225,2282, 3742,4175} };
	private int[][] relivePoints3 = new int[][] { { 837,2933, 4030,2060 }, { 837,2933, 4030,2060 }, { 9512,4176, 6880,4897 }, {  9512,4176, 6880,4897 }, { 1225,2282, 3742,4175 }, { 1225,2282, 3742,4175} };
	//国家，灵矿，进攻方防守方
//	private int[][][] relivePoints = new int[][][] { { {756,2182},{},{},{},{},{} }, { {},{},{},{},{},{} }, { {},{},{},{},{},{} }};
	public static final byte 矿_大_灵脉 = 2;
	public static final byte 矿_中_灵泉 = 1;
	public static final byte 矿_小_灵眼 = 0;

	public static final int 大矿钱 = 500000;
	public static final int 中矿钱 = 300000;
	public static final int 小矿钱 = 100000;

	public static final String 大矿_BUFF_NAME = Translate.大矿_BUFF_NAME;
	public static final String 中矿_BUFF_NAME = Translate.中矿_BUFF_NAME;
	public static final String 小矿_BUFF_NAME = Translate.小矿_BUFF_NAME;
	public static final long buff_持续时长 = 10000;

	public static int 每天最早申请时间 = 12;
	public static int 每天最晚申请时间 = 17;
	
	public static int 每天战斗开始时间 = 19;
	public static int 每天战斗开始时间_分 = 19;
	/**
	 * 单位分
	 */
	public static int 战斗持续时间 = 20;
	public static int 申请矿战花费 = 300000;

	public static int 大圈半径 = 800;

	public static int cooldown = 10000;

	public static int 占领所需时间 = 30000;

	/**
	 * 同时开战
	 */
	public static boolean 开战;

	public ArrayList<OreNPC> oreNPCList = new ArrayList<OreNPC>();

	/**
	 * 战争结束时需要把复活点设置回去
	 */
	public ArrayList<Long> 设置复活点的玩家列表 = new ArrayList<Long>();

	Calendar calendar = Calendar.getInstance();

	public void destroy() {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			em.flush(vd);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[VillageFightManager] [Destroy] [异常]");
		}
		em.destroy();
		System.out.println("[VillageFightManager] [Destroy] 调用destroy方法, cost " + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) + " ms");
	}

	public void init() throws Exception {
		
		em = SimpleEntityManagerFactory.getSimpleEntityManager(VillageData.class);
		long now = SystemTime.currentTimeMillis();
		long[] ids = em.queryIds(VillageData.class, "");
		if (ids != null && ids.length > 0) {
			vd = em.find(ids[ids.length - 1]);
		}
		if (vd == null) {
			创建新的村庄数据();
			em.save(vd);
			vd.setDirty(false);
			System.out.println("[村庄争夺战数据管理器] [生成新数据] [" + (SystemTime.currentTimeMillis() - now) + "ms]");
		}

		// 已有数据容错加字段
		if (vd.prepareToFightJiazus == null) {
			for (int i = CountryManager.国家A; i <= CountryManager.国家C; i++) {
				Map<Integer, List<Long>> jiazuMaps = new HashMap<Integer, List<Long>>();
				int [] [] points = points2[i];
				for (int k = 0; k < points.length; k++) {
					jiazuMaps.put(k, new ArrayList<Long>());
				}
				vd.prepareToFightJiazus.put((byte) i, jiazuMaps);
			}
			if (logger.isInfoEnabled()) {
				logger.info("[村庄争夺战] [init时容错]");
			}
		}

		Calendar calendar = Calendar.getInstance();
		this.hour = calendar.get(Calendar.HOUR_OF_DAY);
		this.minute = calendar.get(Calendar.MINUTE);

		在地图上摆放矿NPC("init");
		running = true;
		Thread thread = new Thread(this, "VillageFightManager");
		thread.start();
		self = this;
		ServiceStartRecord.startLog(this);
	}

	private void 创建新的村庄数据() throws Exception {
		vd = new VillageData();
		vd.id = em.nextId();
		byte[] oreType = new byte[6];
		oreType[0] = 矿_大_灵脉;
		oreType[1] = 矿_中_灵泉;
		oreType[2] = 矿_中_灵泉;
		oreType[3] = 矿_小_灵眼;
		oreType[4] = 矿_小_灵眼;
		oreType[5] = 矿_小_灵眼;
		vd.setOreType(oreType);
		// Hashtable<Byte, OreNPCData[]> countryOres = new Hashtable<Byte, OreNPCData[]>();
		for (int i = CountryManager.国家A; i <= CountryManager.国家C; i++) {
			int [] [] points = points2[i];
			OreNPCData[] oreNPCDatas = new OreNPCData[points.length];
			for (int j = 0; j < oreNPCDatas.length; j++) {
				OreNPCData ond = new OreNPCData();
				ond.setArrayIndex(j);
				ond.setCountry((byte) i);
				ond.setX(points[j][0]);
				ond.setY(points[j][1]);
				if(i == 1){
					ond.setRelivePoints(relivePoints[j]);
				}else if(i == 2){
					ond.setRelivePoints(relivePoints2[j]);
				}else if(i == 3){
					ond.setRelivePoints(relivePoints3[j]);
				}
				if (j < 2) {
					ond.setMapName(countryMapNames[i][0]);
				}
				if (j >= 2 && j < 4) {
					ond.setMapName(countryMapNames[i][1]);
				}
				if (j >= 4) {
					ond.setMapName(countryMapNames[i][2]);
				}
				oreNPCDatas[j] = ond;
			}
			vd.getCountryOres().put((byte) i, oreNPCDatas);
			vd.countryPrepareToFightOres.put((byte) i, new long[points.length]);
			Map<Integer, List<Long>> jiazuMaps = new HashMap<Integer, List<Long>>();
			for (int k = 0; k < points.length; k++) {
				jiazuMaps.put(k, new ArrayList<Long>());
			}
			vd.prepareToFightJiazus.put((byte) i, jiazuMaps);
		}
	}

	public void 在地图上摆放矿NPC(String reason) {
		oreNPCList.clear();
		for (Byte country : vd.countryOres.keySet()) {
			OreNPCData[] oreNPCDatas = vd.countryOres.get(country);
			for (int i = 0; i < oreNPCDatas.length; i++) {
				OreNPCData oreNPCData = oreNPCDatas[i];
				Game game = gm.getGameByName(oreNPCData.getMapName(), country);
				if (game == null && oreNPCData.getMapName().contains("bianjing")) {
					oreNPCData.setMapName("bianjing");
					game = gm.getGameByName(oreNPCData.getMapName(), country);
				}

				for (Game g : gm.getGames()) {
					logger.warn("地图列表:" + g.gi.name + "," + g.gi.displayName + "," + g.country);
				}
				// 创建NPC
				// 因为数组下标对应贫富矿类型数组下标vd.oreType
				int categoryId = 得到矿的类型id(vd.oreType[i]);
				NPC npc = nm.createNPC(categoryId);
				npc.setX(oreNPCData.getX());
				npc.setY(oreNPCData.getY());
				npc.setCountry(oreNPCData.getCountry());
				// 还有一些其他信息
				if (npc instanceof OreNPC) {
					((OreNPC) npc).arrayIndex = i;
					((OreNPC) npc).jiazuId = oreNPCData.getJiazuId();
					if (oreNPCData.getNewName() != null && !oreNPCData.getNewName().trim().equals("")) {
						((OreNPC) npc).setName(oreNPCData.getNewName());
					}
					try {
						if(oreNPCData.getJiazuId() > 0){
							Jiazu jiazu = JiazuManager.getInstance().getJiazu(oreNPCData.getJiazuId());
							if (jiazu != null) {
								((OreNPC) npc).setTitle(Translate.translateString(Translate.某某家族的矿, new String[][] { { Translate.STRING_1, jiazu.getName() } }));
							}
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[在地图上摆放矿NPC] [异常]",ex);
					}
					oreNPCList.add((OreNPC) npc);
					logger.warn("[在地图上摆放矿NPC,成功] ["+reason+"] [map:"+oreNPCData.getMapName()+"] [jiazuid:"+((OreNPC) npc).jiazuId+"] [country:"+country+"] [index:"+((OreNPC) npc).arrayIndex+"] [npc:"+npc.getName()+"] [x,y:"+(npc.getX()+"/"+npc.getY())+"] [npc数:"+oreNPCList.size()+"]");
				} else {
					if (logger.isWarnEnabled()) logger.warn("[在地图上摆放矿NPC] [异常] [不是OreNPC]");
				}
				if (game == null) {
					logger.warn("[在地图上摆放矿NPC,地图不存在:"+ oreNPCData.getMapName()+"/"+country);
					System.out.println("在地图上摆放矿NPC,地图不存在:[" + oreNPCData.getMapName() + "] [country:" + country + "]");
				}
				game.addSprite(npc);
			}
		}
	}

	public VillageData vd;

	public synchronized void 申请攻打灵矿(Player player, int arrayIndex) {
		if (player == null) {
			return;
		}
		String result = 申请攻打灵矿合法性判断(player, arrayIndex);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			if (logger.isInfoEnabled()) {
				logger.info("[申请攻打灵矿] [失败] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result });
			}
			return;
		}

		// 如果已经申请一个矿，那么提示是否放弃那个矿申请这个矿
		long[] prepareJiazuIds = vd.countryPrepareToFightOres.get(player.getCountry());
		for (long l : prepareJiazuIds) {
			if (l == player.getJiazuId()) {
				// 确认
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				mw.setTitle(Translate.申请攻打灵矿);
				mw.setDescriptionInUUB(Translate.translateString(Translate.您已经申请攻打另一处矿, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(申请矿战花费) } }));
				Option_VillageFight_ShenqingQueren option = new Option_VillageFight_ShenqingQueren();
				option.setText(Translate.申请攻打灵矿);
				option.setColor(0xFFFFFF);
				option.arrayIndex = arrayIndex;
				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.取消);
				cancel.setColor(0xFFFFFF);
				mw.setOptions(new Option[] { option, cancel });
				CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
				player.addMessageToRightBag(req);
				return;
			}
		}

		// 确认
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.申请攻打灵矿);
		mw.setDescriptionInUUB(Translate.translateString(Translate.申请攻打灵矿花费确认, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(申请矿战花费) } }));
		Option_VillageFight_ShenqingQueren option = new Option_VillageFight_ShenqingQueren();
		option.setText(Translate.申请攻打灵矿);
		option.setColor(0xFFFFFF);
		option.arrayIndex = arrayIndex;
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.取消);
		cancel.setColor(0xFFFFFF);
		mw.setOptions(new Option[] { option, cancel });
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);

	}

	public synchronized void 申请攻打灵矿确认(Player player, int arrayIndex) {
		if (player == null) {
			return;
		}
		String result = 申请攻打灵矿合法性判断(player, arrayIndex);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			if (logger.isInfoEnabled()) {
				logger.info("[申请攻打灵矿确认] [失败] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), result });
			}
			return;
		}

		// 扣钱
		BillingCenter bc = BillingCenter.getInstance();
		try {
			bc.playerExpense(player, 申请矿战花费, CurrencyType.SHOPYINZI, ExpenseReasonType.VILLAGE_FIGHT, "矿战");
		} catch (Exception ex) {
			if (logger.isWarnEnabled()) logger.warn("[申请攻打灵矿确认] [失败] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() }, ex);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.扣费失败);
			player.addMessageToRightBag(hreq);
			return;
		}

		// 加入到备选列表
		Hashtable<Byte, Map<Integer, List<Long>>> prepareToFightJiazus = vd.prepareToFightJiazus;
		if (prepareToFightJiazus == null) {

			prepareToFightJiazus = new Hashtable<Byte, Map<Integer, List<Long>>>();
		}
		Map<Integer, List<Long>> jiazuMaps = prepareToFightJiazus.get(player.getCountry());
		if (jiazuMaps == null) {
			jiazuMaps = new HashMap<Integer, List<Long>>();
		}

		List<Long> jiazuList = new ArrayList<Long>();
		if (jiazuMaps.containsKey(arrayIndex)) {
			jiazuList = jiazuMaps.get(arrayIndex);
		}
		jiazuList.add(player.getJiazuId());
		jiazuMaps.put(arrayIndex, jiazuList);
		prepareToFightJiazus.put(player.getCountry(), jiazuMaps);
		vd.setPrepareToFightJiazus(prepareToFightJiazus);
		vd.setDirty(true);

		shookDice(player);

	}

	public String 申请攻打灵矿合法性判断(Player player, int arrayIndex) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.矿战)) {
			return Translate.合服功能关闭提示;
		}
		if (player.getSilver() < 申请矿战花费) {
			return Translate.translateString(Translate.申请攻打灵矿需要花费, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(申请矿战花费) } });
		}
		calendar.setTimeInMillis(SystemTime.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 每天最早申请时间) {
			return Translate.translateString(Translate.每天最早申请时间, new String[][] { { Translate.COUNT_1, 每天最早申请时间 + "" } });
		}
		if (hour >= 每天最晚申请时间) {
			return Translate.translateString(Translate.每天最晚申请时间, new String[][] { { Translate.COUNT_1, 每天最晚申请时间 + "" } });
		}
		JiazuManager jm = JiazuManager.getInstance();
		if (jm == null) {
			return Translate.服务器出现错误;
		}
		Jiazu jiazu = jm.getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return Translate.您没有家族;
		}
		if (jiazu.getJiazuMaster() != player.getId()) {
			return Translate.您不是家族族长;
		}
		long[] prepareJiazuIds = vd.countryPrepareToFightOres.get(player.getCountry());
		OreNPCData[] onds = vd.countryOres.get(player.getCountry());
		if (arrayIndex < 0 || arrayIndex >= prepareJiazuIds.length && arrayIndex >= onds.length) {
			return Translate.您申请的矿不存在;
		}
		if (onds[arrayIndex].getJiazuId() == player.getJiazuId()) {
			return Translate.醒醒这是你的矿;
		}
		if (vd.prepareToFightJiazus != null && vd.prepareToFightJiazus.get(player.getCountry()) != null && vd.prepareToFightJiazus.get(player.getCountry()).get(arrayIndex) != null) {
			if (vd.prepareToFightJiazus.get(player.getCountry()).get(arrayIndex).contains(player.getJiazuId())) {
				return Translate.您已经申请攻打了此矿;
			}
		}

		if (vd.prepareToFightJiazus != null) {
			for (Byte country : vd.prepareToFightJiazus.keySet()) {
				Map<Integer, List<Long>> jiazuMaps = vd.prepareToFightJiazus.get(country);
				if (jiazuMaps != null) {
					for (Integer oreIndex : jiazuMaps.keySet()) {
						if (jiazuMaps.get(oreIndex) != null && jiazuMaps.get(oreIndex).contains(jiazu.getJiazuID())) {
							if (country == player.getJiazuId()) {
								return Translate.您已经申请攻打了此矿;
							} else {
								return Translate.您已经申请攻打其它矿;
							}
						}
					}
				}
			}

		}

		return null;
	}

	public void 玩家上线弹摇骰子界面(Player player, int arrayIndex) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.矿战)) {
			return;
		}
		calendar.setTimeInMillis(SystemTime.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 每天最早申请时间) {
			return;
		}
		if (hour >= 每天最晚申请时间) {
			return;
		}
		JiazuManager jm = JiazuManager.getInstance();
		if (jm == null) {
			return;
		}
		Jiazu jiazu = jm.getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return;
		}
		if (jiazu.getJiazuMaster() != player.getId()) {
			return;
		}
		long[] prepareJiazuIds = vd.countryPrepareToFightOres.get(player.getCountry());
		OreNPCData[] onds = vd.countryOres.get(player.getCountry());
		if (arrayIndex < 0 || arrayIndex >= prepareJiazuIds.length && arrayIndex >= onds.length) {
			return;
		}
		if (vd.prepareToFightJiazus != null && vd.prepareToFightJiazus.get(player.getCountry()) != null && vd.prepareToFightJiazus.get(player.getCountry()).get(arrayIndex) != null) {
			if (vd.prepareToFightJiazus.get(player.getCountry()).get(arrayIndex).contains(player.getJiazuId())) {
				shookDice(player);
			}
		}

	}

	/**
	 * 报名截止，按筛子点数对申请者排序，给报名的家族发通知邮件，并在世界和家族频道发广播
	 */
	public void noticePrePareJiazus() {
		JiazuManager jm = JiazuManager.getInstance();
		ChatMessageService cms = ChatMessageService.getInstance();
		MailManager mm = MailManager.getInstance();
		if (vd.prepareToFightJiazus != null || vd.prepareToFightJiazus.size() == 0) {
			for (Byte country : vd.prepareToFightJiazus.keySet()) {
				Map<Integer, List<Long>> jiazuMap = vd.prepareToFightJiazus.get(country);
				if (jiazuMap != null) {
					for (Integer oreIndex : jiazuMap.keySet()) {
						Collections.sort(jiazuMap.get(oreIndex), order); // 对各矿的申请者按骰子点数倒序排列
						List<Long> jiazuList = jiazuMap.get(oreIndex);
						logger.warn("[村庄战报名结果] [jiazuList:"+(jiazuList!=null?jiazuList.size():"null")+"] [oreIndex:"+oreIndex+"] [country:"+country+"]");
						if (jiazuList != null && jiazuList.size() > 0) {
							StringBuffer sbf = new StringBuffer();
							if (jiazuList.size() >= 1) {
								sbf.append(Translate.矿战报名结果 + "</f>\n<f>" + Translate.第一名);
								Jiazu jiazu = jm.getJiazu(jiazuList.get(0));
								if (jiazu != null) {
									sbf.append(Translate.translateString(Translate.骰子点数, new String[][] { { Translate.STRING_1, jiazu.getName() }, { Translate.COUNT_1, jiazu.getPoint() + "" } }));
								} else {
									if (logger.isWarnEnabled()) logger.warn("[村庄战报名结果] [家族不存在] [jiazuId: " + jiazuList.get(0) + "] [country:" + country + " ] [oreIndex: " + oreIndex + "]");
								}
							}
							if (jiazuList.size() >= 2) {
								sbf.append("</f>\n<f>" + Translate.第二名);
								Jiazu jiazu = jm.getJiazu(jiazuList.get(1));
								if (jiazu != null) {
									sbf.append(Translate.translateString(Translate.骰子点数, new String[][] { { Translate.STRING_1, jiazu.getName() }, { Translate.COUNT_1, jiazu.getPoint() + "" } }));
								} else {
									if (logger.isWarnEnabled()) logger.warn("[村庄战报名结果] [家族不存在] [jiazuId: " + jiazuList.get(0) + "] [country:" + country + " ] [oreIndex: " + oreIndex + "]");
								}
							}
							if (jiazuList.size() >= 3) {
								sbf.append("</f>\n<f>" + Translate.第三名);
								Jiazu jiazu = jm.getJiazu(jiazuList.get(2));
								if (jiazu != null) {
									sbf.append(Translate.translateString(Translate.骰子点数, new String[][] { { Translate.STRING_1, jiazu.getName() }, { Translate.COUNT_1, jiazu.getPoint() + "" } }));
								} else {
									if (logger.isWarnEnabled()) logger.warn("[村庄战报名结果] [家族不存在] [jiazuId: " + jiazuList.get(0) + "] [country:" + country + " ] [oreIndex: " + oreIndex + "]");
								}
							}
							String msg = "";
							for (int i = 0; i < jiazuList.size(); i++) {
								Jiazu jiazu2 = jm.getJiazu(jiazuList.get(i));
								if (jiazu2 != null) {
									if (i == 0) {
										msg = Translate.translateString(Translate.矿战申请结果通知, new String[][] { { Translate.STRING_1, jiazu2.getName() } });
										// 被宣战家族：家族频道广播，邮件通知，宣战家族：邮件通知
										String notice = sbf.toString() + Translate.translateString("</f>\n<f>" + Translate.矿战排行成功, new String[][] { { Translate.COUNT_1, i + 1 + "" }, { Translate.COUNT_2, jiazu2.getPoint() + "" } });
										try {
											OreNPCData[] countryOres = vd.countryOres.get(country);
											OreNPCData ond = countryOres[oreIndex];
											if (ond.getJiazuId() > 0) {
												Jiazu jiazu = jm.getJiazu(ond.getJiazuId()); // 当前占领矿的家族
												String description = Translate.translateString(Translate.某帮申请向贵帮发起村庄战, new String[][] { { Translate.STRING_1, jiazu2.getName() }, { Translate.COUNT_1, 每天战斗开始时间 + "" } });
												cms.sendMessageToJiazu(jiazu, description, "");
												// cms.sendMessageToSystem(msg);
												Player jiazuMaster = PlayerManager.getInstance().getPlayer(jiazu.getJiazuMaster());
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
												jiazuMaster.addMessageToRightBag(hreq);
												mm.sendMail(jiazu.getJiazuMaster(), new ArticleEntity[0], description, description, 0, 0, 0, "");
											}
											mm.sendMail(jiazu2.getJiazuMaster(), new ArticleEntity[0], Translate.系统, notice, 0, 0, 0, "矿战报名结果通知");

										} catch (Exception ex) {
											if (logger.isWarnEnabled()) logger.warn("[申请攻打灵矿确认发消息] [异常] [{}]", new Object[] { jiazu2.getLogString() }, ex);
										}

										long[] prepareJiazuIds = vd.countryPrepareToFightOres.get(country);
										for (int j = 0; j < prepareJiazuIds.length; j++) {
											if (prepareJiazuIds[j] == jiazu2.getJiazuID()) { // 原来的代码，看意思是为一个家族只能申请占领一处矿多加一层保障
												prepareJiazuIds[j] = 0;
											}
										}
										prepareJiazuIds[oreIndex] = jiazu2.getJiazuID(); // 加入到备战列表
										vd.setDirty(true);
									} else { // 申请失败的家族邮件通知
										String notice = sbf.toString() + Translate.translateString("</f>\n<f>" + Translate.矿战排行失败, new String[][] { { Translate.COUNT_1, i + 1 + "" }, { Translate.COUNT_2, jiazu2.getPoint() + "" } });
										// 给族长发邮件
										try {
											MailManager.getInstance().sendMail(jiazu2.getJiazuMaster(), new ArticleEntity[0], new int[0], Translate.系统, notice, 0, 0, 0, "矿战报名结果通知");
										} catch (Exception e) {
											if (logger.isWarnEnabled()) logger.warn("[村庄战报名结果] [邮件通知异常] [jiazuId: " + jiazuList.get(0) + "] [country:" + country + " ] [oreIndex: " + oreIndex + "]", e);
											e.printStackTrace();
										}
									}

									// 所有申请过的家族：家族频道通知
									cms.sendMessageToJiazu(jiazu2, msg, "");

								} else {
									if (logger.isWarnEnabled()) logger.warn("[村庄战报名结果] [家族不存在] [jiazuId: " + jiazuList.get(0) + "] [country:" + country + " ] [oreIndex: " + oreIndex + "]");
								}
							}
						}
					}
				}
			}
		}else{
			logger.warn("[村庄战报名结果] [没有家族申请攻打]");
		}
	}

	public void shookDice(Player player) {
		// 摇骰子
		Random random = new Random();
		int point1 = random.nextInt(5) + 1;
		int point2 = random.nextInt(5) + 1;
		int point3 = random.nextInt(5) + 1;
		int point = point1 + point2 + point3;
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		jiazu.setPoint(-point); // 每次摇骰子这个点数可能发生改变，以最后一次为准（正常情况下最多也应该只有两个数：一次刚报完名，一次因报名后掉线没看见摇骰子界面而在玩家下次上线的时候重新摇）
		player.addMessageToRightBag(new SHOOK_DICE_RES(GameMessageFactory.nextSequnceNum(), player.getJiazuId(), 1, point1, point2, point3, SHOOK_TIME));
		if (logger.isWarnEnabled()) logger.warn("[村庄战报名] [发送摇骰子协议] [" + player.getLogString() + "] [jiazuId: " + player.getJiazuId() + "] [point:" + point + "]");
	}

	/**
	 * 按骰子点数对申请者排序
	 */
	private static Comparator<Long> order = new Comparator<Long>() {

		@Override
		public int compare(Long o1, Long o2) {
			Jiazu jiazu1 = JiazuManager.getInstance().getJiazu(o1);
			Jiazu jiazu2 = JiazuManager.getInstance().getJiazu(o2);
			if (jiazu1 != null && jiazu2 != null) {
				if (jiazu1.getPoint() < 0) {
					jiazu1.setPoint(0);
				}
				if (jiazu2.getPoint() < 0) {
					jiazu2.setPoint(0);
				}
				if (jiazu1.getPoint() <= jiazu2.getPoint()) {
					return 1;
				} else {
					return -1;
				}
			}
			return -1;
		}

	};

	/**
	 * 当圆圈变化时，发给本地图玩家圆圈变化提示
	 */
	public void 圆圈变化时给地图上的玩家提示() {
		GameManager gm = GameManager.getInstance();
		if (gm != null) {
			for (int k = CountryManager.国家A; k <= CountryManager.国家C; k++) {
				String [] countryMapName = countryMapNames[k];
				int [] [] points = points2[k];
				for (int i = 0; i < countryMapName.length; i++) {
					String mapName = countryMapName[i];
//					for (int j = 1; j <= CountryManager.国家C; j++) {
						Game game = gm.getGameByName(mapName, k);
						if (game != null) {
							LivingObject[] los = game.getLivingObjects();
							if (los != null) {
								logger.warn("[圆圈变化时给地图上的玩家提示] [开战:"+开战+"] [==================]");
								if (开战) {
									int[] xs = new int[2];
									int[] ys = new int[2];
									int range = 大圈半径;

									int index = i * 2;
									xs[0] = points[index][0];
									xs[1] = points[index + 1][0];
									ys[0] = points[index][1];
									ys[1] = points[index + 1][1];
									range = 大圈半径;

									for (LivingObject lo : los) {
										if (lo instanceof Player) {
											Player player = (Player) lo;
											SEND_VILLAGE_CIRCLE_REQ req = new SEND_VILLAGE_CIRCLE_REQ(GameMessageFactory.nextSequnceNum(), xs, ys, range);
											player.addMessageToRightBag(req);
										}
									}
								} else {
									for (LivingObject lo : los) {
										if (lo instanceof Player) {
											Player player = (Player) lo;
											SEND_VILLAGE_CIRCLE_DIS_REQ req = new SEND_VILLAGE_CIRCLE_DIS_REQ(GameMessageFactory.nextSequnceNum());
											player.addMessageToRightBag(req);
										}
									}
								}

							}
						}
//					}
				}
			}
		}

	}

	public void 给进入游戏的人提示(Game game, Player player) {
		if (开战) {
			for (int k = CountryManager.国家A; k <= CountryManager.国家C; k++) {
				int [] [] points = points2[k];
				String [] countryMapName = countryMapNames[k];
				for (int i = 0; i < countryMapName.length; i++) {
					String mapName = countryMapName[i];
					if (mapName.equals(game.gi.getName())) {
						int index = i * 2;
						int[] xs = new int[2];
						xs[0] = points[index][0];
						xs[1] = points[index + 1][0];
						int[] ys = new int[2];
						ys[0] = points[index][1];
						ys[1] = points[index + 1][1];
						int range = 大圈半径;
						SEND_VILLAGE_CIRCLE_REQ req = new SEND_VILLAGE_CIRCLE_REQ(GameMessageFactory.nextSequnceNum(), xs, ys, range);
						player.addMessageToRightBag(req);
						return;
					}
				}
			}
		}
	}

	/**
	 * 如果处于村庄战状态，进入地图后把回城点设置成当前
	 * @param game
	 * @param player
	 */
	public void 给进入地图的村庄战玩家设置复活点(Game game, Player player) {
		if (开战 && vd != null) {
			if (player.getJiazuId() > 0) {
				if (设置复活点的玩家列表.contains(player.getJiazuId())) {
					return;
				}
				for (int k = CountryManager.国家A; k <= CountryManager.国家C; k++) {
					String [] countryMapName = countryMapNames[k];
					for (int i = 0; i < countryMapName.length; i++) {
						String mapName = countryMapName[i];
						if (mapName.equals(game.gi.getName())) {
							int index = i * 2;
							OreNPCData[] onds = vd.getCountryOres().get(game.country);
							if (onds != null) {
								long[] prepareFightJiazuIds = vd.countryPrepareToFightOres.get(game.country);
								OreNPCData ond1 = onds[index];
								OreNPCData ond2 = onds[index + 1];
								if (ond1 != null) {
									if (ond1.getJiazuId() == player.getJiazuId()) {
										if (ond1.getRelivePoints() != null) {
											player.村庄战临时复活点地图名 = player.getResurrectionMapName();
											player.村庄战临时复活点x = player.getResurrectionX();
											player.村庄战临时复活点y = player.getResurrectionY();
											player.setResurrectionMapName(game.gi.getName());
											player.setResurrectionX(ond1.getRelivePoints()[0]);
											player.setResurrectionY(ond1.getRelivePoints()[1]);
											设置复活点的玩家列表.add(player.getId());
											倒计时(player);
										} else {
											if (logger.isWarnEnabled()) logger.warn("[村庄战设置复活点] [失败] [{}] [{}] [{}] [index:{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), index });
										}
										return;
									}
									if (prepareFightJiazuIds != null) {
										long id1 = prepareFightJiazuIds[index];
										if (id1 == player.getJiazuId()) {
											if (ond1.getRelivePoints() != null) {
												player.村庄战临时复活点地图名 = player.getResurrectionMapName();
												player.村庄战临时复活点x = player.getResurrectionX();
												player.村庄战临时复活点y = player.getResurrectionY();
												player.setResurrectionMapName(game.gi.getName());
												player.setResurrectionX(ond1.getRelivePoints()[2]);
												player.setResurrectionY(ond1.getRelivePoints()[3]);
												设置复活点的玩家列表.add(player.getId());
												倒计时(player);
											} else {
												if (logger.isWarnEnabled()) logger.warn("[村庄战设置复活点] [失败] [{}] [{}] [{}] [index:{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), index });
											}
											return;
										}
									}
								}
								if (ond2 != null) {
									if (ond2.getJiazuId() == player.getJiazuId()) {
										if (ond2.getRelivePoints() != null) {
											player.村庄战临时复活点地图名 = player.getResurrectionMapName();
											player.村庄战临时复活点x = player.getResurrectionX();
											player.村庄战临时复活点y = player.getResurrectionY();
											player.setResurrectionMapName(game.gi.getName());
											player.setResurrectionX(ond2.getRelivePoints()[0]);
											player.setResurrectionY(ond2.getRelivePoints()[1]);
											设置复活点的玩家列表.add(player.getId());
											倒计时(player);
										} else {
											if (logger.isWarnEnabled()) logger.warn("[村庄战设置复活点] [失败] [{}] [{}] [{}] [index:{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), index + 1 });
										}
										return;
									}
									if (prepareFightJiazuIds != null) {
										long id2 = prepareFightJiazuIds[index + 1];
										if (id2 == player.getJiazuId()) {
											if (ond2.getRelivePoints() != null) {
												player.村庄战临时复活点地图名 = player.getResurrectionMapName();
												player.村庄战临时复活点x = player.getResurrectionX();
												player.村庄战临时复活点y = player.getResurrectionY();
												player.setResurrectionMapName(game.gi.getName());
												player.setResurrectionX(ond2.getRelivePoints()[2]);
												player.setResurrectionY(ond2.getRelivePoints()[3]);
												设置复活点的玩家列表.add(player.getId());
												倒计时(player);
											} else {
												if (logger.isWarnEnabled()) logger.warn("[村庄战设置复活点] [失败] [{}] [{}] [{}] [index:{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), index + 1 });
											}
											return;
										}
									}
								}
							}

							return;
						}
					}
				}
			}
		}
	}

	public void 倒计时(Player player) {
		if (player != null && player.getCountdownAgent() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 每天战斗开始时间, 战斗持续时间, 0);
			long invalidTime = calendar.getTimeInMillis();
			player.getCountdownAgent().countdownMap.put(CountdownAgent.COUNT_TYPE_VILLAGE, invalidTime);
			player.getCountdownAgent().通知玩家倒计时();
		}
	}

	public void 把复活点还原() {
		if (设置复活点的玩家列表 != null) {
			for (long l : 设置复活点的玩家列表) {
				try {
					Player p = PlayerManager.getInstance().getPlayer(l);
					if (p.村庄战临时复活点地图名 != null && !p.村庄战临时复活点地图名.trim().equals("")) {
						p.setResurrectionMapName(p.村庄战临时复活点地图名);
						p.setResurrectionX(p.村庄战临时复活点x);
						p.setResurrectionY(p.村庄战临时复活点y);
					}
				} catch (Exception ex) {

				}
			}
			设置复活点的玩家列表.clear();
		}
	}

	public void 开战时给在地图中的人设置复活点() {
		GameManager gm = GameManager.getInstance();
		if (gm != null) {
			for (int k = CountryManager.国家A; k <= CountryManager.国家C; k++) {
				String [] countryMapName = countryMapNames[k];
				logger.warn("[开战时给在地图中的人设置复活点] [countryMapName:"+Arrays.toString(countryMapName)+"] [============]");
				for (int i = 0; i < countryMapName.length; i++) {
					String mapName = countryMapName[i];
//					for (int j = 1; j <= CountryManager.国家C; j++) {
						Game game = gm.getGameByName(mapName, k);
						if (game != null) {
							LivingObject[] los = game.getLivingObjects();
							if (los != null) {
								for (LivingObject lo : los) {
									if (lo instanceof Player) {
										给进入地图的村庄战玩家设置复活点(game, (Player) lo);
									}
								}
							}
						}
//					}
				}
			}
		}
	}

	public boolean 每天凌晨更新数据(boolean week) {
		Hashtable<Byte, long[]> maps = vd.countryPrepareToFightOres;
		for (Byte b : maps.keySet()) {
			long[] ids = maps.get(b);
			for (int i = 0; i < ids.length; i++) {
				ids[i] = 0;
			}
			vd.countryPrepareToFightOres.put(b, ids);
		}
		Hashtable<Byte, Map<Integer, List<Long>>> prepareToFightJiazus = vd.prepareToFightJiazus;
		for (Byte country : prepareToFightJiazus.keySet()) {
			Map<Integer, List<Long>> jiazuMaps = prepareToFightJiazus.get(country);
			for (int index : jiazuMaps.keySet()) {
				jiazuMaps.put(index, new ArrayList<Long>());
			}
			vd.prepareToFightJiazus.put(country, jiazuMaps);
		}
		vd.everyDayGetBindSilverPlayerIdMap.clear();
		vd.everyDayGetBuffPlayerIdMap.clear();
		vd.everyDayGetJiazuZiJinJiazuIdMap.clear();
		vd.everyDayGetSilverJiazuIdMap.clear();
		if (week) {
			ArrayList<Byte> byteList = new ArrayList<Byte>();
			for (byte b : vd.oreType) {
				byteList.add(b);
			}
			Collections.shuffle(byteList);
			for (int i = 0; i < vd.oreType.length; i++) {
				vd.oreType[i] = byteList.get(i);
			}
			vd.setOreType(vd.oreType);

			// 先清除地图上的矿npc
			for (Byte country : vd.countryOres.keySet()) {
				OreNPCData[] oreNPCDatas = vd.countryOres.get(country);
				for (int i = 0; i < oreNPCDatas.length; i++) {
					OreNPCData oreNPCData = oreNPCDatas[i];
					if(oreNPCData != null){
						Jiazu jiazu = JiazuManager.getInstance().getJiazu(oreNPCData.getJiazuId());
						if(i == 0){
							JiazuManager2.instance.addLiveness(jiazu, JiaZuLivenessType.灵矿争夺_蓝矿);
						}else if(i == 1){
							JiazuManager2.instance.addLiveness(jiazu, JiaZuLivenessType.灵矿争夺_紫矿);
						}else if(i == 2){
							JiazuManager2.instance.addLiveness(jiazu, JiaZuLivenessType.灵矿争夺_橙矿);
						}
					}
					Game game = gm.getGameByName(oreNPCData.getMapName(), country);
					LivingObject[] los = game.getLivingObjects();
					for (int j = 0; j < los.length; j++) {
						if (los[j] instanceof OreNPC) {
							game.removeSprite((Sprite) los[j]);
						}
					}
				}
			}

			在地图上摆放矿NPC("凌晨刷新");
		}
		vd.setDirty(true);
		logger.warn("[村庄战每日凌晨刷新] [week:" + week + "]");
		return true;
	}

	public void 给家族玩家发送弹窗提示() {
		VillageFightManager vfm = VillageFightManager.getInstance();
		if (vfm == null) {
			return;
		}
		long prepareFightJiazuId = 0;
		try {
			logger.warn("[给家族玩家发送弹窗提示] [===========]");
			for (int j = 0; j <= CountryManager.国家C; j++) {
				OreNPCData[] onds = vfm.vd.countryOres.get((byte) j);
				if (onds == null || onds.length <= 0) {
					logger.warn("[新家族] [灵矿战开始提示] [没有获取到对应OreNPCData数组] [country: " + j + "]");
					continue;
				}
				for (int kk = 0; kk < onds.length; kk++) {
					prepareFightJiazuId = vfm.vd.countryPrepareToFightOres.get((byte) j)[onds[kk].getArrayIndex()];
					if (prepareFightJiazuId <= 0) {
						logger.warn("[新家族] [灵矿战开始提示] [没有获取到对应prepareFightJiazuId小于等于0] [country: " + j + "]");
						continue;
					}
					JiazuManager2.instance.noticeLingKuangZhanStart(prepareFightJiazuId, onds[kk].getCountry(), onds[kk].getMapName(), onds[kk].getX(), onds[kk].getY());
				}
			}

		} catch (Exception ex) {
			JiazuManager2.logger.error("[新家族] [灵矿战开始弹窗通知] [异常]", ex);
		}
	}

	public void 给玩家发送开战信息() {
		ChatMessageService cms = ChatMessageService.getInstance();
		if (cms != null) {
			ChatMessage msg = new ChatMessage();
			String description = Translate.争夺灵矿战已经开始;
			msg.setMessageText(description);
			try {
				cms.sendMessageToSystem(msg);
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("[发送开战消息] [异常]", e);
			}
		}
		if (oreNPCList != null && vd != null) {
			for (OreNPC on : oreNPCList) {
				if (on != null) {
					long prepareFightJiazuId = 0;
					try {
						prepareFightJiazuId = this.vd.countryPrepareToFightOres.get(on.country)[on.arrayIndex];
						if (prepareFightJiazuId > 0) {
							村庄站开始和结束通知(on.jiazuId, prepareFightJiazuId, (byte) 0);
						}
					} catch (Exception ex) {
						logger.warn("[给玩家发送开战信息1] [失败] [oreNPCList:"+oreNPCList+"] [vd:"+vd+"]");
					}
				}
			}
		}else{
			logger.warn("[给玩家发送开战信息2] [失败] [oreNPCList:"+oreNPCList+"] [vd:"+vd+"]");
		}
	}

	public int hour;
	int minute;
	public static boolean 公布报名结果 = false;

	@Override
	public void run() {
		Calendar calendar = Calendar.getInstance();
		while (running) {
			try {
				Thread.sleep(5000);
				if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
					continue;
				}
				calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				int day = calendar.get(Calendar.DAY_OF_WEEK);
				int second = calendar.get(Calendar.SECOND);
				if (hour == 每天最晚申请时间 && second > 10 && !公布报名结果) {
					公布报名结果 = true;
					noticePrePareJiazus();
				}
				if (!开战 && isValid(hour, minute)) {
					开战 = true;
					给game设置大圈提示标记(true);
					开战时给在地图中的人设置复活点();
					圆圈变化时给地图上的玩家提示();
					给玩家发送开战信息();
					给家族玩家发送弹窗提示();
				}
				if (开战 && (hour == 每天战斗开始时间 && minute >= 战斗持续时间)) {
					开战 = false;
					公布报名结果 = false;
					给game设置大圈提示标记(false);
					开战结束处理();
				}
				if (this.hour != hour) {
					this.hour = hour;
					if (hour == 0) {
						if (day == 2) {
							每天凌晨更新数据(true);
						} else {
							每天凌晨更新数据(false);
						}
					}
				}
				if (vd.dirty) {
					em.flush(vd);
					vd.setDirty(false);
				}
			} catch (Throwable ex) {
				logger.error("[村庄战心跳] [异常]", ex);
			}
		}
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		System.out.println(day);
	}

	/**
	 * 玩家当前是否村庄战，是否在自己的村庄战范围内
	 * @param player
	 * @return
	 */
	public boolean 是否需要受到惩罚(Player player, Player target) {
		if (开战 && vd != null) {
			if (player.getCurrentGame() != null && player.getCurrentGame().country == player.getCountry()) {
				Hashtable<Byte, OreNPCData[]> countryOres = vd.countryOres;
				Hashtable<Byte, long[]> countryPrepareToFightOres = vd.countryPrepareToFightOres;
				if (countryOres != null && countryPrepareToFightOres != null) {
					for (int i = 1; i < CountryManager.国家C; i++) {
						int [] [] points = points2[i];
						OreNPCData[] onds = countryOres.get((byte) i);
						long[] ids = countryPrepareToFightOres.get((byte) i);
						if (onds != null && ids != null) {
							for (int j = 0; j < onds.length && j < ids.length; j++) {
								OreNPCData ond = onds[j];
								long prepareId = ids[j];
								if (ond != null) {
									if (ond.getJiazuId() > 0 && prepareId > 0) {
										if (player.getJiazuId() == ond.getJiazuId() || player.getJiazuId() == prepareId) {
											if (player.getCurrentGame().gi.getName().equals(countryMapNames[ond.getCountry()][j / 2])) {
												long length1 = (points[j][0] - player.getX()) * (points[j][0] - player.getX()) + (points[j][1] - player.getY()) * (points[j][1] - player.getY());
												long length2 = (points[j][0] - target.getX()) * (points[j][0] - target.getX()) + (points[j][1] - target.getY()) * (points[j][1] - target.getY());
												int length = 大圈半径 * 大圈半径;
												if (length1 <= length && length2 <= length) {
													if (player.getJiazuId() == ond.getJiazuId() && target.getJiazuId() == prepareId) {
														return false;
													}
													if (player.getJiazuId() == prepareId && target.getJiazuId() == ond.getJiazuId()) {
														return false;
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
			}
		}
		return true;
	}

	public void 给game设置大圈提示标记(boolean flag) {
		GameManager gm = GameManager.getInstance();
		for (int k = CountryManager.国家A; k <= CountryManager.国家C; k++) {
			String [] countryMapName = countryMapNames[k];
			for (int i = 0; i < countryMapName.length; i++) {
				String mapName = countryMapName[i];
//				for (int j = 0; j <= CountryManager.国家C; j++) {
					Game game = gm.getGameByName(mapName, k);
					logger.warn("[给game设置大圈提示标记] [flag:"+flag+"] [国家:"+k+"] [地图:"+mapName+"] [game:"+game+"] [=================]");
					if (game != null) {
						game.村庄战争提示 = flag;
					}
//				}
			}
		}
	}

	public boolean isValid(int hour, int minute) {
		if (hour == 每天战斗开始时间 && minute < 战斗持续时间) {
			return true;
		}
		return false;
	}

	public void 开战结束处理() {
		if (oreNPCList != null && vd != null) {
			JiazuManager jm = JiazuManager.getInstance();
			logger.warn("[开战结束处理] [================]");
			for (OreNPC on : oreNPCList) {
				if (on != null) {
					long oldId = on.jiazuId;
					long winId = on.得到占领结果();// 只有经过战斗的结果才大于0
					if (VillageFightManager.logger.isWarnEnabled()) VillageFightManager.logger.warn("[得到占领结果] [{}] [{}] [{}]", new Object[] { on.getCountry(), on.arrayIndex, winId });
					if (winId > 0) {
						Jiazu jiazu = jm.getJiazu(winId);
						try {
							if (oldId != winId) {
								村庄站开始和结束通知(winId, oldId, (byte) 1);
							} else {
								村庄站开始和结束通知(winId, (long) 0, (byte) 1);
							}
						} catch (Exception ex) {

						}
						OreNPCData[] ond = vd.countryOres.get(on.getCountry());
						if (on.arrayIndex >= 0 && on.arrayIndex < ond.length) {
							ond[on.arrayIndex].setJiazuId(winId);
							ond[on.arrayIndex].setNewName("");
							if (jiazu != null) {
								String title = Translate.translateString(Translate.某某家族的矿, new String[][] { { Translate.STRING_1, jiazu.getName() } });
								on.setTitle(title);
								on.jiazuId = winId;
								on.setName(得到矿的名字(vd.oreType[on.arrayIndex]));
							}
							vd.setDirty(true);
						}
						ChatMessageService cms = ChatMessageService.getInstance();
						if (cms != null) {
							try {

								String description = Translate.恭喜贵帮赢得村庄战的胜利;
								cms.sendMessageToJiazu(jiazu, description, "");
								cms.sendMessageToJiazu(jiazu, description, "");

								MailManager mm = MailManager.getInstance();
								mm.sendMail(jiazu.getJiazuMaster(), new ArticleEntity[0], description, description, 0, 0, 0, "");
							} catch (Exception ex) {
								if (logger.isWarnEnabled()) logger.warn("[开战结束处理发消息] [异常]", ex);
							}

						}

					}
					if (logger.isWarnEnabled()) {
						logger.warn("[矿战结束] [矿id:{}] [胜利帮派id:{}] [原占有帮派id:{}]", new Object[] { on.getId(), winId, oldId });
					}
					// 活跃度统计
					Set<JiazuMember> members = JiazuManager.getInstance().getJiazuMember(winId);
					if (members != null) {
						for (JiazuMember m : members) {
							PlayerManager pm = PlayerManager.getInstance();
							if (pm != null) {
								try {
									Player p = pm.getPlayer(m.getPlayerID());
									ActivenessManager.getInstance().record(p, ActivenessType.灵矿争夺);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}

				}
			}
			// 如果有帮派占领两个矿，那么自动放弃之前占领的矿，保存他申请占领的矿
			for (Byte b : vd.countryOres.keySet()) {
				OreNPCData[] ond = vd.countryOres.get(b);
				long[] prepareFightJiazuIds = vd.countryPrepareToFightOres.get(b);
				if (ond != null) {
					for (int i = 0; i < ond.length; i++) {
						boolean hasDoubleId = false;
						long doubleIds = ond[i].getJiazuId();
						for (int j = 0; j < ond.length; j++) {
							if (i != j) {
								long id = ond[j].getJiazuId();
								if (doubleIds == id && id != 0) {
									hasDoubleId = true;
									break;
								}
							}
						}
						if (hasDoubleId) {
							for (int j = 0; j < ond.length; j++) {
								long id = ond[j].getJiazuId();
								if (doubleIds == id) {
									if (prepareFightJiazuIds[j] != doubleIds) {
										// 发消息
										ond[j].setJiazuId(0);
										ond[j].setNewName("");
										for (OreNPC on : oreNPCList) {
											if (on != null && on.arrayIndex == j && on.getCountry() == b) {
												on.setTitle("");
												on.jiazuId = 0;
												on.newName = "";
												on.setName(得到矿的名字(vd.oreType[j]));
												if (logger.isWarnEnabled()) logger.warn("[开战结束处理，由于有一个帮派占领了两个矿，发消息] [{}]", new Object[] { on.getId() });
											}
										}
										vd.setDirty(true);
										ChatMessageService cms = ChatMessageService.getInstance();
										if (cms != null) {
											try {
												Jiazu jiazu = jm.getJiazu(id);
												String description = Translate.恭喜贵帮赢得村庄战的胜利占有两个矿的处理;
												cms.sendMessageToJiazu(jiazu, description, "");
												cms.sendMessageToJiazu(jiazu, description, "");
												Player p = PlayerManager.getInstance().getPlayer(jiazu.getJiazuMaster());
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
												p.addMessageToRightBag(hreq);
												MailManager mm = MailManager.getInstance();
												mm.sendMail(p.getId(), new ArticleEntity[0], description, description, 0, 0, 0, "");
											} catch (Exception ex) {
												if (logger.isWarnEnabled()) logger.warn("[开战结束处理发消息] [异常]", ex);
											}
										}
										for (OreNPC on : oreNPCList) {
											if (on != null && on.getCountry() == b && on.arrayIndex == j) {
												on.setTitle("");
												on.jiazuId = 0;
												on.setTitle("");
												on.newName = "";
												break;
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
		try {
			for (Byte b : vd.countryPrepareToFightOres.keySet()) {
				long[] ids = vd.countryPrepareToFightOres.get(b);
				if (ids != null) {
					for (int i = 0; i < ids.length; i++) {
						ids[i] = 0;
					}
				}
				vd.countryPrepareToFightOres.put(b, ids);
			}
			for (Byte b : vd.countryPrepareToFightOres.keySet()) {
				long[] ids = vd.countryPrepareToFightOres.get(b);
				if (ids != null) {
					for (int i = 0; i < ids.length; i++) {
						if (logger.isWarnEnabled()) logger.warn("[开战结束处理] [{}] [{}]", new Object[] { b, ids[i] });
					}
				}
			}
		} catch (Exception ex) {
			if (logger.isWarnEnabled()) logger.warn("[开战结束处理] [异常]", ex);
		}

		把复活点还原();
		圆圈变化时给地图上的玩家提示();
		vd.setDirty(true);
		if (logger.isInfoEnabled()) {
			logger.info("[开战结束处理] [正常]");
		}
	}

	/**
	 * 当type为0时，winId和lostId为对战双方，当type为1时，winId和lostId为胜负双方
	 * @param winId
	 * @param lostId
	 * @param type
	 */
	public void 村庄站开始和结束通知(Long winerId, Long losterId, byte type) {
		long winId = -1;
		long lostId = -1;
		if (winerId != null) {
			winId = winerId;
		}
		if (losterId != null) {
			lostId = losterId;
		}
		String description = "";
		String sideAName = Translate.无;
		String sideAIcon = "";
		String sideBName = Translate.无;
		String sideBIcon = "";
		PlayerManager pm = PlayerManager.getInstance();
		JiazuManager jm = JiazuManager.getInstance();
		Jiazu winJiazu = jm.getJiazu(winId);
		if (winJiazu != null) {
			sideAName = winJiazu.getName();
			description = Translate.translateString(Translate.某家族获得矿战胜利, new String[][] { { Translate.STRING_1, sideAName } });
		}
		Jiazu lostJiazu = jm.getJiazu(lostId);
		if (lostJiazu != null) {
			sideBName = lostJiazu.getName();
		}
		SEND_VILLAGE_STATE_REQ req = new SEND_VILLAGE_STATE_REQ(GameMessageFactory.nextSequnceNum(), type, description, sideAName, sideAIcon, sideBName, sideBIcon);
		if (winJiazu != null) {
			winJiazu.setWarScore(winJiazu.getWarScore() + 200);
			try {
				ChatMessageService cms = ChatMessageService.getInstance();
				cms.sendMessageToJiazu_dianshi(winJiazu, Translate.家族取得灵矿战的胜利获得200武力值);
			} catch (Exception ex) {

			}
			Player[] ps = pm.getOnlinePlayerByJiazu(winJiazu);
			if (ps != null) {
				for (Player p : ps) {
					p.addMessageToRightBag(req);
					if (type == 1) {
						if (AchievementManager.getInstance() != null) {
							AchievementManager.getInstance().record(p, RecordAction.获得村战胜利, 1);
						}
					}
				}
			}
		}
		if (lostJiazu != null) {
			Player[] ps = pm.getOnlinePlayerByJiazu(lostJiazu);
			if (ps != null) {
				for (Player p : ps) {
					p.addMessageToRightBag(req);
				}
			}
		}
	}

}
