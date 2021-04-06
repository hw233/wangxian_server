package com.fy.engineserver.cityfight;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.cityfight.citydata.CityData;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.cityfight.Option_CityFight_ShenqingQueren;
import com.fy.engineserver.menu.cityfight.Option_CityFight_quedingfangqizhanlingchengshi;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.SEND_CITYFIGHT_STATE_REQ;
import com.fy.engineserver.message.SHOOK_DICE_RES;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.npc.CityFightNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class CityFightManager implements Runnable {

	private static CityFightManager self;

	public static CityFightManager getInstance() {
		return self;
	}

	public static Logger logger = LoggerFactory.getLogger(CityFightManager.class.getName());

	private CityFightManager() {

	}

	public void init() throws Exception {
		
		long start = System.currentTimeMillis();
		self = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(CityData.class);
		long[] ids = em.queryIds(CityData.class, "");
		if (ids.length > 0) {
			long id = ids[ids.length - 1];
			cd = em.find(id);
		} else {
			cd = new CityData();
			cd.setId(em.nextId());
			em.flush(cd);
		}
		if (cd == null) {
			throw new Exception("没有城战数据");
		}
		Thread thread = new Thread(this, "CityFightManager");
		thread.start();
		Calendar calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_YEAR);
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {
		long start = System.currentTimeMillis();
		em.destroy();
		System.out.println("[CityFightManager] [销毁] [完毕] [耗时:" + (System.currentTimeMillis() - start) + "ms]");
	}

	public static String[] 可占领的城市 = new String[] { "kunlunshengdian", "kunhuagucheng", "kunlunyaosai", "miemoshenyu"
														,"jiuzhoutiancheng","shangzhouxianjing","jiuzhouyaosai"
														,"wanfazhiyuan","wanfayiji","wanfayaosai"};
	public static String[] 可占领的城市汉化 = new String[] { Translate.昆仑圣殿, Translate.昆华古城, Translate.昆仑要塞, Translate.灭魔神域
														,Translate.九州天成, Translate.上州仙境, Translate.九州要塞,
														Translate.万法之源, Translate.万法遗迹, Translate.万法要塞,};

	public String 得到可占领的城市汉化(String cityName) {
		for (int i = 0; i < 可占领的城市.length; i++) {
			if (可占领的城市[i].equals(cityName)) {
				return 可占领的城市汉化[i];
			}
		}
		return cityName;
	}

	public static String[] 可占领的国家城市汉化 = new String[] { Translate.昆仑圣殿, Translate.昆华古城, Translate.昆仑要塞 };
	public static String[][] 可占领的国家城市汉化2 = new String[][] { {Translate.昆仑圣殿, Translate.昆华古城, Translate.昆仑要塞},
		{Translate.九州天成, Translate.上州仙境, Translate.九州要塞},{Translate.万法之源, Translate.万法遗迹, Translate.万法要塞}};
	public static String[] 可占领的中立城市汉化 = new String[] { Translate.灭魔神域 };
	public static long[] 可以领取的银子 = new long[] { 800000, 500000, 500000, 500000,800000, 500000, 500000,800000, 500000, 500000 };
	public static String 王城 = "kunlunshengdian";
	public static String 王城2 = "jiuzhoutiancheng";
	public static String 王城3 = "wanfazhiyuan";
	public static String 王城汉化 = Translate.昆仑圣殿;
	public static String 王城汉化2 = Translate.九州天成;
	public static String 王城汉化3 = Translate.万法之源;
	public static String 中立城市 = "miemoshenyu";
	public static int[] npc出现的位置 = new int[] {3275,1680};
	public static int[][] 玩家出现的位置 = new int[][] { { 1088,2749 }, {5156,1272}, {2751,1467 } };// 数组第一位为防守方，第二位为进攻方，第三位为其他玩家
	public static byte 进攻方 = 1;
	public static byte 防守方 = 2;
	public static byte 其他方 = 0;
	public static int npcId = 90000000;
	public static int npcId2 = 90000001;
	public static int npcId3 = 90000002;
	public static int npc存活时间 = 1800000;
	public static int 领取奖励时间 = 19;// 小时
//	public static String[] 战斗地图 = new String[] { "zhuchengmijing","xinshoucunmijing","yaosaimijing","miemoshenyumijng" };
	public static String[] 战斗地图 = new String[] {"zhuchengmijing","xinshoucunmijing","yaosaimijing","miemoshenyumijing","zhuchengmijing","xinshoucunmijing","yaosaimijing","zhuchengmijing","xinshoucunmijing","yaosaimijing"};
	public static long 消耗 = 1000000;
	public static int 申请时间 = 18;
	public static int 申请时间1 = 0;
	public static int 繁荣度 = 100;
	public static int 开战时 = 19;
	public static int 开战分 = 30;
	public static int 关战时 = 20;
	public static int 关战分 = 0;

	public static int 开启中立城市的级别 = 150;

	public static int SHOOK_TIME = 5; // 单位：s

	public CityData cd;
	public SimpleEntityManager<CityData> em;

	public SimpleEntityManager<CityData> getEm() {
		return em;
	}

	public void setEm(SimpleEntityManager<CityData> em) {
		this.em = em;
	}

	public synchronized void 申请城市争夺战(int country, String cityName, Player player) {
		String result = 申请城市争夺战合法性判断(country, cityName, player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		mw.setTitle(Translate.申请攻占城市);
		mw.setDescriptionInUUB(Translate.translateString(Translate.攻占城市需要交纳银子繁荣度, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(消耗) }, { Translate.COUNT_2, 繁荣度 + "" } }));
		Option_CityFight_ShenqingQueren option = new Option_CityFight_ShenqingQueren();
		option.setText(Translate.申请攻占城市);
		option.setColor(0xFFFFFF);
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.取消);
		cancel.setColor(0xFFFFFF);
		mw.setOptions(new Option[] { option, cancel });
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);
	}

	public synchronized void 确认申请城市争夺战(int country, String cityName, Player player) {
		String result = 申请城市争夺战合法性判断(country, cityName, player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		BillingCenter billingCenter = BillingCenter.getInstance();
		try {
			billingCenter.playerExpense(player, 消耗, CurrencyType.SHOPYINZI, ExpenseReasonType.CITY_FIGHT, "城市争夺战");
		} catch (Exception ex) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.银子不足);
			player.addMessageToRightBag(hreq);
			logger.warn("[确认申请攻占城市] [异常] [" + player.getLogString() + "]", ex);
			return;
		}
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
		zp.setFanrongdu(zp.getFanrongdu() - 繁荣度);

		if (country == 0) {
			if (cd.getAttackCity_0_country_map().get(cityName) == null) {
				cd.getAttackCity_0_country_map().put(cityName, new ArrayList<Long>());
			}
			cd.getAttackCity_0_country_map().get(cityName).add(zp.getId());
			cd.setAttackCity_0_country_map(cd.getAttackCity_0_country_map());
		} else if (country == 1) {
			if (cd.getAttackCity_1_country_map().get(cityName) == null) {
				cd.getAttackCity_1_country_map().put(cityName, new ArrayList<Long>());
			}
			cd.getAttackCity_1_country_map().get(cityName).add(zp.getId());
			cd.setAttackCity_1_country_map(cd.getAttackCity_1_country_map());
		} else if (country == 2) {
			if (cd.getAttackCity_2_country_map().get(cityName) == null) {
				cd.getAttackCity_2_country_map().put(cityName, new ArrayList<Long>());
			}
			cd.getAttackCity_2_country_map().get(cityName).add(zp.getId());
			cd.setAttackCity_2_country_map(cd.getAttackCity_2_country_map());
		} else if (country == 3) {
			if (cd.getAttackCity_3_country_map().get(cityName) == null) {
				cd.getAttackCity_3_country_map().put(cityName, new ArrayList<Long>());
			}
			cd.getAttackCity_3_country_map().get(cityName).add(zp.getId());
			cd.setAttackCity_3_country_map(cd.getAttackCity_3_country_map());
		}

		shookDice(player);
		// if(country == 0){
		// cd.getAttackCity_0_country().put(cityName, zp.getId());
		// cd.setAttackCity_0_country(cd.getAttackCity_0_country());
		// }else if(country == 1){
		// cd.getAttackCity_1_country().put(cityName, zp.getId());
		// cd.setAttackCity_1_country(cd.getAttackCity_1_country());
		// }else if(country == 2){
		// cd.getAttackCity_2_country().put(cityName, zp.getId());
		// cd.setAttackCity_2_country(cd.getAttackCity_2_country());
		// }else if(country == 3){
		// cd.getAttackCity_3_country().put(cityName, zp.getId());
		// cd.setAttackCity_3_country(cd.getAttackCity_3_country());
		// }
		// int index = 0;
		// for (int i = 0; i < 可占领的城市.length; i++) {
		// if (可占领的城市[i].equals(cityName)) {
		// index = i;
		// }
		// }
		//
		// result = Translate.translateString(Translate.恭喜您申请攻占成功, new String[][] { { Translate.STRING_1, 可占领的城市汉化[index] } });
		// HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
		// player.addMessageToRightBag(hreq);
		//
		// try {
		// String description = Translate.translateString(Translate.贵盟盟主申请了攻占城市, new String[][] { { Translate.STRING_1, 得到可占领的城市汉化(cityName) }, { Translate.STRING_2, 开战时 + "" }, {
		// Translate.STRING_3, 开战分 + "" } });
		// Player[] ps = PlayerManager.getInstance().getOnlineInZongpai(zp.getId());
		// if (ps != null) {
		// for (Player p : ps) {
		// HINT_REQ hhreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
		// p.addMessageToRightBag(hhreq);
		// }
		// }
		// } catch (Exception ex) {
		//
		// }
		//
		// String des = Translate.translateString(Translate.某盟申请了攻占城市, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.STRING_2,
		// zp.getZpname() }, { Translate.STRING_3, 得到可占领的城市汉化(cityName) }, { Translate.STRING_4, 开战时 + "" }, { Translate.STRING_5, 开战分 + "" } });
		// if (des != null) {
		// ChatMessageService cms = ChatMessageService.getInstance();
		// if (cms != null) {
		// ChatMessage msg = new ChatMessage();
		// msg.setMessageText(des);
		// try {
		// cms.sendMessageToCountry(country, msg);
		// cms.sendMessageToCountry(country, msg);
		// cms.sendMessageToCountry2(country, msg);
		// cms.sendMessageToCountry2(country, msg);
		// } catch (Exception e) {
		// if (logger.isWarnEnabled()) logger.warn("[发送战斗结束消息] [异常]", e);
		// }
		// }
		// }
		//
		// Map<String, Long> map = null;
		// if (country == 0) {
		// map = cd.getHoldCity_0_country();
		// } else if (country == 1) {
		// map = cd.getHoldCity_1_country();
		// } else if (country == 2) {
		// map = cd.getHoldCity_2_country();
		// } else if (country == 3) {
		// map = cd.getHoldCity_3_country();
		// }
		// if (map != null) {
		// Long id = map.get(cityName);
		// if (id != null && id > 0) {
		// ZongPai zpp = zpm.getZongPaiById(id);
		// if (zpp != null) {
		// String description = Translate.translateString(Translate.某宗派挑战您所占领的城市, new String[][] { { Translate.STRING_1, zp.getZpname() }, { Translate.STRING_2,
		// 得到可占领的城市汉化(cityName) }, { Translate.STRING_3, 开战时 + "" }, { Translate.STRING_4, 开战分 + "" } });
		// MailManager mm = MailManager.getInstance();
		// if (mm != null) {
		// try {
		// mm.sendMail(zpp.getMasterId(), new ArticleEntity[0], Translate.城战信息, description, 0, 0, 0, "");
		// } catch (Exception e) {
		//
		// }
		// }
		// try {
		// Player[] ps = PlayerManager.getInstance().getOnlineInZongpai(zpp.getId());
		// if (ps != null) {
		// for (Player p : ps) {
		// HINT_REQ hhreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
		// p.addMessageToRightBag(hhreq);
		// }
		// }
		// } catch (Exception ex) {
		//
		// }
		// }
		// }
		// }

		logger.warn("[确认申请攻占城市] [成功] [{}] [{}] [宗派id:{}] [国家:{}]", new Object[] { player.getLogString(), result, zp.getId(), country });
	}

	Calendar calendar = Calendar.getInstance();

	public String 申请城市争夺战合法性判断(int country, String cityName, Player player) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.城战)) {
			return Translate.合服功能关闭提示;
		}
		calendar.setTimeInMillis(System.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour <= 申请时间1) {
			return Translate.请与每日某点后申请;
		}
		if (hour >= 申请时间) {
			return Translate.translateString(Translate.请与每日某点前申请, new String[][] { { Translate.COUNT_1, 申请时间 + "" } });
		}
		if (country != CountryManager.中立 && country != player.getCountry()) {
			return Translate.不能占领别国的城市;
		}
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		if (zpm == null) {
			return Translate.服务器出现错误;
		}
		ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
		if (zp == null) {
			return Translate.你还没有宗派只有宗派族长才能占领;
		}
		if (zp.getMasterId() != player.getId()) {
			return Translate.你不是宗派族长只有宗派族长才能占领;
		}

		boolean hasCity = false;
		for (String str : 可占领的城市) {
			if (str.equals(cityName)) {
				hasCity = true;
				break;
			}
		}
		if (!hasCity) {
			return Translate.这个城市不能攻占;
		}

		if ((country == 0 && !中立城市.equals(cityName)) || (中立城市.equals(cityName) && country != 0)) {
			return Translate.这个城市不是中立城市;
		}
		SealManager sm = SealManager.getInstance();
		if (sm != null) {
			if (中立城市.equals(cityName) && sm.getSealLevel() < 开启中立城市的级别) {
				return Translate.中立城市必须二次封印开放后才能抢夺;
			}
		}

		Map<String, Long> map = null;
		if (country == 0) {
			map = cd.getHoldCity_0_country();
		} else if (country == 1) {
			map = cd.getHoldCity_1_country();
		} else if (country == 2) {
			map = cd.getHoldCity_2_country();
		} else if (country == 3) {
			map = cd.getHoldCity_3_country();
		}
		boolean 一个城也没有占 = true;
		if (cd.getHoldCity_0_country() != null && cd.getHoldCity_0_country().keySet() != null) {
			for (String key : cd.getHoldCity_0_country().keySet()) {
				if (cd.getHoldCity_0_country().get(key) != null && cd.getHoldCity_0_country().get(key) > 0) {
					一个城也没有占 = false;
					break;
				}
			}
		}
		if (一个城也没有占) {
			if (map != null && map.keySet() != null) {
				for (String key : map.keySet()) {
					if (map.get(key) != null && map.get(key) > 0) {
						一个城也没有占 = false;
						break;
					}
				}
			}
		}
		
		 if (country == 1) {
			 if (一个城也没有占 && !cityName.equals(王城)) {
					return Translate.translateString(Translate.必须先申请王城, new String[][] { { Translate.STRING_1, 王城汉化 } });
				}
		} else if (country == 2) {
			if (一个城也没有占 && !cityName.equals(王城2)) {
				return Translate.translateString(Translate.必须先申请王城, new String[][] { { Translate.STRING_1, 王城汉化2 } });
			}
		} else if (country == 3) {
			if (一个城也没有占 && !cityName.equals(王城3)) {
				return Translate.translateString(Translate.必须先申请王城, new String[][] { { Translate.STRING_1, 王城汉化3 } });
			}
		}
		
		if (map != null && map.keySet() != null) {
			for (String key : map.keySet()) {
				if (map.get(key) != null && map.get(key) == zp.getId()) {
					return Translate.您已经占领了城市;
				}
			}
		}
		Hashtable<String, List<Long>> prepareMap = null;
		prepareMap = cd.getAttackCity_0_country_map();
		if (prepareMap != null && prepareMap.keySet() != null) {
			for (String key : prepareMap.keySet()) {
				if (prepareMap.get(key) != null && prepareMap.get(key).contains(zp.getId())) {
					return Translate.您已经申请攻占城市;
				}
			}
		}
		prepareMap = cd.getAttackCity_1_country_map();
		if (prepareMap != null && prepareMap.keySet() != null) {
			for (String key : prepareMap.keySet()) {
				if (prepareMap.get(key) != null && prepareMap.get(key).contains(zp.getId())) {
					return Translate.您已经申请攻占城市;
				}
			}
		}
		prepareMap = cd.getAttackCity_2_country_map();
		if (prepareMap != null && prepareMap.keySet() != null) {
			for (String key : prepareMap.keySet()) {
				if (prepareMap.get(key) != null && prepareMap.get(key).contains(zp.getId())) {
					return Translate.您已经申请攻占城市;
				}
			}
		}
		prepareMap = cd.getAttackCity_3_country_map();
		if (prepareMap != null && prepareMap.keySet() != null) {
			for (String key : prepareMap.keySet()) {
				if (prepareMap.get(key) != null && prepareMap.get(key).contains(zp.getId())) {
					return Translate.您已经申请攻占城市;
				}
			}
		}

		// if (country == 0) {
		// map = cd.getAttackCity_0_country();
		// } else if (country == 1) {
		// map = cd.getAttackCity_1_country();
		// } else if (country == 2) {
		// map = cd.getAttackCity_2_country();
		// } else if (country == 3) {
		// map = cd.getAttackCity_3_country();
		// }
		// if (map != null) {
		// if (map.get(cityName) != null && map.get(cityName) > 0) {
		// return Translate.已经有人先申请了攻占这个城市;
		// }
		// }
		if (zp.getFanrongdu() < 繁荣度) {
			return Translate.translateString(Translate.需要繁荣度, new String[][] { { Translate.COUNT_1, 繁荣度 + "" } });
		}
		if (player.getSilver() + player.getShopSilver() < 消耗) {
			return Translate.translateString(Translate.攻占城市需要交纳银子, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(消耗) } });
		}
		return null;
	}

	public void 玩家上线摇骰子(int country, String cityName, Player player) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.城战)) {
			return;
		}
		calendar.setTimeInMillis(System.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour <= 申请时间1) {
			return;
		}
		if (hour >= 申请时间) {
			return;
		}
		if (country != CountryManager.中立 && country != player.getCountry()) {
			return;
		}
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		if (zpm == null) {
			return;
		}
		ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
		if (zp == null) {
			return;
		}
		if (zp.getMasterId() != player.getId()) {
			return;
		}

		boolean hasCity = false;
		for (String str : 可占领的城市) {
			if (str.equals(cityName)) {
				hasCity = true;
				break;
			}
		}
		if (!hasCity) {
			return;
		}

		if ((country == 0 && !中立城市.equals(cityName)) || (中立城市.equals(cityName) && country != 0)) {
			return;
		}
		SealManager sm = SealManager.getInstance();
		if (sm != null) {
			if (中立城市.equals(cityName) && sm.getSealLevel() < 开启中立城市的级别) {
				return;
			}
		}

		Map<String, Long> map = null;
		if (country == 0) {
			map = cd.getHoldCity_0_country();
		} else if (country == 1) {
			map = cd.getHoldCity_1_country();
		} else if (country == 2) {
			map = cd.getHoldCity_2_country();
		} else if (country == 3) {
			map = cd.getHoldCity_3_country();
		}
		boolean 一个城也没有占 = true;
		if (cd.getHoldCity_0_country() != null && cd.getHoldCity_0_country().keySet() != null) {
			for (String key : cd.getHoldCity_0_country().keySet()) {
				if (cd.getHoldCity_0_country().get(key) != null && cd.getHoldCity_0_country().get(key) > 0) {
					一个城也没有占 = false;
					break;
				}
			}
		}
		if (一个城也没有占) {
			if (map != null && map.keySet() != null) {
				for (String key : map.keySet()) {
					if (map.get(key) != null && map.get(key) > 0) {
						一个城也没有占 = false;
						break;
					}
				}
			}
		}
		
		 if (country == 1) {
			 if (一个城也没有占 && !cityName.equals(王城)) {
					return;
				}
		} else if (country == 2) {
			if (一个城也没有占 && !cityName.equals(王城2)) {
				return;
			}
		} else if (country == 3) {
			if (一个城也没有占 && !cityName.equals(王城3)) {
				return;
			}
		}
		 
		if (map != null && map.keySet() != null) {
			for (String key : map.keySet()) {
				if (map.get(key) != null && map.get(key) == zp.getId()) {
					return;
				}
			}
		}
		map = cd.getAttackCity_0_country();
		if (map != null && map.keySet() != null) {
			for (String key : map.keySet()) {
				if (map.get(key) != null && map.get(key) == zp.getId()) {
					shookDice(player);
				}
			}
		}

		map = cd.getAttackCity_1_country();
		if (map != null && map.keySet() != null) {
			for (String key : map.keySet()) {
				if (map.get(key) != null && map.get(key) == zp.getId()) {
					shookDice(player);
				}
			}
		}

		map = cd.getAttackCity_2_country();
		if (map != null && map.keySet() != null) {
			for (String key : map.keySet()) {
				if (map.get(key) != null && map.get(key) == zp.getId()) {
					shookDice(player);
				}
			}
		}

		map = cd.getAttackCity_3_country();
		if (map != null && map.keySet() != null) {
			for (String key : map.keySet()) {
				if (map.get(key) != null && map.get(key) == zp.getId()) {
					shookDice(player);
				}
			}
		}
	}

	/**
	 * 报名截止，按筛子点数对申请者排序，给报名的家族发通知邮件，并在世界和家族频道发广播
	 */
	public void noticePrePareZongpais(int country) {
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		ChatMessageService cms = ChatMessageService.getInstance();
		Hashtable<String, List<Long>> attackCityMap = null;
		if (country == 0) {
			attackCityMap = cd.getAttackCity_0_country_map();
		} else if (country == 1) {
			attackCityMap = cd.getAttackCity_1_country_map();
		} else if (country == 2) {
			attackCityMap = cd.getAttackCity_2_country_map();
		} else if (country == 3) {
			attackCityMap = cd.getAttackCity_3_country_map();
		}
		logger.warn("[城战报名公布报名结果] [country:"+country+"] [attackCityMap:"+(attackCityMap.keySet()!=null?attackCityMap.keySet().size():"null")+"]");
		for (String cityName : attackCityMap.keySet()) {
			Collections.sort(attackCityMap.get(cityName), order); // 对各城市的申请者按骰子点数倒序排列
			List<Long> zongpaiList = attackCityMap.get(cityName);
			if (zongpaiList != null && zongpaiList.size() > 0) {
				StringBuffer sbf = new StringBuffer();
				if (zongpaiList.size() >= 1) {
					sbf.append(Translate.城战报名结果 + "</f>\n<f>" + Translate.第一名);
					ZongPai zongpai = zpm.getZongPaiById(zongpaiList.get(0));
					if (zongpai != null) {
						sbf.append(Translate.translateString(Translate.骰子点数, new String[][] { { Translate.STRING_1, zongpai.getZpname() }, { Translate.COUNT_1, zongpai.getPoint() + "" } }));
					} else {
						logger.warn("[城市争夺战] [宗派不存在] [cityName:" + cityName + "] [zongpaiId:" + zongpaiList.get(0) + "] [zongpaiName:" + zongpai.getZpname() + "]");
					}
				}
				if (zongpaiList.size() >= 2) {
					sbf.append("</f>\n<f>" + Translate.第二名);
					ZongPai zongpai = zpm.getZongPaiById(zongpaiList.get(1));
					if (zongpai != null) {
						sbf.append(Translate.translateString(Translate.骰子点数, new String[][] { { Translate.STRING_1, zongpai.getZpname() }, { Translate.COUNT_1, zongpai.getPoint() + "" } }));
					} else {
						logger.warn("[城市争夺战] [宗派不存在] [cityName:" + cityName + "] [zongpaiId:" + zongpaiList.get(1) + "]");
					}
				}
				if (zongpaiList.size() >= 3) {
					sbf.append("</f>\n<f>" + Translate.第三名);
					ZongPai zongpai = zpm.getZongPaiById(zongpaiList.get(2));
					if (zongpai != null) {
						sbf.append(Translate.translateString(Translate.骰子点数, new String[][] { { Translate.STRING_1, zongpai.getZpname() }, { Translate.COUNT_1, zongpai.getPoint() + "" } }));
					} else {
						logger.warn("[城市争夺战] [宗派不存在] [cityName:" + cityName + "] [zongpaiId:" + zongpaiList.get(2) + "]");
					}
				}
				String msg = "";
				for (int i = 0; i < zongpaiList.size(); i++) {
					ZongPai zp = zpm.getZongPaiById(zongpaiList.get(i));
					if (zp != null) {
						try {
							if (i == 0) {
								msg = Translate.translateString(Translate.城战申请结果通知, new String[][] { { zp.getZpname() } });
								// 被宣战宗派：宗派频道广播，邮件通知，宣战宗派：邮件通知
								String notice = sbf.toString() + Translate.translateString("</f>\n<f>" + Translate.城战排行成功, new String[][] { { Translate.COUNT_1, i + 1 + "" }, { Translate.COUNT_2, zp.getPoint() + "" } });

								String description = Translate.translateString(Translate.贵盟盟主申请了攻占城市, new String[][] { { Translate.STRING_1, 得到可占领的城市汉化(cityName) }, { Translate.STRING_2, 开战时 + "" }, { Translate.STRING_3, 开战分 + "" } });
								Player[] ps = PlayerManager.getInstance().getOnlineInZongpai(zp.getId());
								if (ps != null) {
									for (Player p : ps) {
										HINT_REQ hhreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
										p.addMessageToRightBag(hhreq);
									}
								}
								Player player = PlayerManager.getInstance().getPlayer(zp.getMasterId());
								if (player == null) {
									return;
								}
								String des = Translate.translateString(Translate.某盟申请了攻占城市, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(player.getCountry()) }, { Translate.STRING_2, zp.getZpname() }, { Translate.STRING_3, 得到可占领的城市汉化(cityName) }, { Translate.STRING_4, 开战时 + "" }, { Translate.STRING_5, 开战分 + "" } });
								if (des != null) {
									if (cms != null) {
										ChatMessage cmsg = new ChatMessage();
										// 设置为宗派频道
										cmsg.setSort(ChatChannelType.ZONG);
										cmsg.setMessageText(des);
										try {
											cms.sendMessageToZong(zp, cmsg);
											// cms.sendMessageToCountry(country, cmsg);
											// cms.sendMessageToCountry2(country, cmsg);
											MailManager.getInstance().sendMail(zp.getMasterId(), new ArticleEntity[0], new int[0], Translate.系统, notice, 0, 0, 0, "城战报名结果通知");
										} catch (Exception e) {
											if (logger.isWarnEnabled()) logger.warn("[发送战斗结束消息] [异常]", e);
										}
									}
								}

								Map<String, Long> map = null;
								if (country == 0) {
									map = cd.getHoldCity_0_country();
									cd.getAttackCity_0_country().put(cityName, zongpaiList.get(0));
								} else if (country == 1) {
									map = cd.getHoldCity_1_country();
									cd.getAttackCity_1_country().put(cityName, zongpaiList.get(0));
								} else if (country == 2) {
									map = cd.getHoldCity_2_country();
									cd.getAttackCity_2_country().put(cityName, zongpaiList.get(0));
								} else if (country == 3) {
									map = cd.getHoldCity_3_country();
									cd.getAttackCity_3_country().put(cityName, zongpaiList.get(0));
								}
								if (map != null) {
									Long id = map.get(cityName);
									if (id != null && id > 0) {
										ZongPai zpp = zpm.getZongPaiById(id);
										if (zpp != null) {
											String description2 = Translate.translateString(Translate.某宗派挑战您所占领的城市, new String[][] { { Translate.STRING_1, zp.getZpname() }, { Translate.STRING_2, 得到可占领的城市汉化(cityName) }, { Translate.STRING_3, 开战时 + "" }, { Translate.STRING_4, 开战分 + "" } });
											MailManager mm = MailManager.getInstance();
											if (mm != null) {
												try {
													mm.sendMail(zpp.getMasterId(), new ArticleEntity[0], Translate.城战信息, description2, 0, 0, 0, "");
												} catch (Exception e) {

												}
											}
											try {
												Player[] ps2 = PlayerManager.getInstance().getOnlineInZongpai(zpp.getId());
												if (ps2 != null) {
													for (Player p : ps2) {
														HINT_REQ hhreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description2);
														p.addMessageToRightBag(hhreq);
													}
												}
											} catch (Exception ex) {

											}
										}
									}
								}
							} else { // 申请失败的宗派邮件通知
								String notice = sbf.toString() + Translate.translateString("</f>\n<f>" + Translate.城战排行失败, new String[][] { { Translate.COUNT_1, i + 1 + "" }, { Translate.COUNT_2, zp.getPoint() + "" } });
								// 给宗长发邮件
								try {
									MailManager.getInstance().sendMail(zp.getMasterId(), new ArticleEntity[0], new int[0], Translate.系统, notice, 0, 0, 0, "城战报名结果通知");
								} catch (Exception e) {
									if (logger.isWarnEnabled()) logger.warn("[城战报名结果] [邮件通知异常] [zongpaiId: " + zongpaiList.get(0) + "] [zongpaiName: " + zp.getZpname() + "] [cityName:" + cityName + " ]", e);
									e.printStackTrace();
								}
							}
							// 所有申请过的宗派：宗派频道通知
							cms.sendMessageToZongPai(zp, msg);

						} catch (Exception ex) {

						}
					}
				}
			}

		}

	}

	public void shookDice(Player player) {
		// 摇骰子
		Random random = new Random();
		int point1 = random.nextInt(5) + 1;
		int point2 = random.nextInt(5) + 1;
		int point3 = random.nextInt(5) + 1;
		int point = point1 + point2 + point3;
		ZongPai zongpai = ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId());
		zongpai.setPoint(-point);
		player.addMessageToRightBag(new SHOOK_DICE_RES(GameMessageFactory.nextSequnceNum(), zongpai.getId(), 0, point1, point2, point3, SHOOK_TIME));
		if (logger.isWarnEnabled()) logger.warn("[城战报名] [发送摇骰子协议] [" + player.getLogString() + "] [zongpaiId: " + zongpai.getId() + "] [point:" + point + "]");
	}

	/**
	 * 按骰子点数对申请者排序
	 */
	private static Comparator<Long> order = new Comparator<Long>() {

		@Override
		public int compare(Long o1, Long o2) {
			ZongPai zongpai1 = ZongPaiManager.getInstance().getZongPaiById(o1);
			ZongPai zongpai2 = ZongPaiManager.getInstance().getZongPaiById(o2);
			if (zongpai1 != null && zongpai2 != null) {
				if (zongpai1.getPoint() < 0) {
					zongpai1.setPoint(0);
				}
				if (zongpai2.getPoint() < 0) {
					zongpai2.setPoint(0);
				}
				if (zongpai1.getPoint() > zongpai2.getPoint()) {
					return -1;
				} else {
					return 1;
				}
			}
			return -1;
		}

	};

	public boolean running = true;
	private long lastHeartbeat;
	private boolean 开战 = false;
	private boolean 广播1 = false;
	private boolean 广播2 = false;
	private boolean 广播3 = false;
	private int day = 0;
	public static boolean 公布报名结果 = false;

	public void run() {
		Calendar calendar = Calendar.getInstance();
		while (running) {
			try {
				Thread.sleep(5000);

				long heartbeatTime = System.currentTimeMillis();
				if (heartbeatTime - lastHeartbeat > 10000) {
					lastHeartbeat = heartbeatTime;
					calendar.setTimeInMillis(heartbeatTime);
				}
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				int nowDay = calendar.get(Calendar.DAY_OF_YEAR);
				if (hour == 申请时间 && !公布报名结果) {
					noticePrePareZongpais(0);
					noticePrePareZongpais(1);
					noticePrePareZongpais(2);
					noticePrePareZongpais(3);
					公布报名结果 = true;
				}
				// 开战前广播
				if (!广播1) {
					if (hour == 开战时 && minute == (开战分 - 1)) {
						广播1 = true;
						String description = Translate.城市争夺战将于1分钟后开启;
						ChatMessageService cms = ChatMessageService.getInstance();
						if (cms != null) {
							ChatMessage msg = new ChatMessage();
							msg.setMessageText(description);
							try {
								cms.sendMessageToSystem(msg);
								cms.sendMessageToSystem(msg);
								cms.sendMessageToWorld(msg);
								if (logger.isWarnEnabled()) {
									logger.warn("[城市开始前广播] [成功] [{}]", new Object[] { description });
								}
							} catch (Exception e) {

							}
						}
					}
				}
				if (!广播2) {
					if (hour == 开战时 && minute == (开战分 - 2)) {
						广播2 = true;
						String description = Translate.城市争夺战将于2分钟后开启;
						ChatMessageService cms = ChatMessageService.getInstance();
						if (cms != null) {
							ChatMessage msg = new ChatMessage();
							msg.setMessageText(description);
							try {
								cms.sendMessageToSystem(msg);
								cms.sendMessageToSystem(msg);
								cms.sendMessageToWorld(msg);
								if (logger.isWarnEnabled()) {
									logger.warn("[城市开始前广播] [成功] [{}]", new Object[] { description });
								}
							} catch (Exception e) {

							}
						}
					}
				}
				if (!广播3) {
					if (hour == 开战时 && minute == (开战分 - 3)) {
						广播3 = true;
						String description = Translate.城市争夺战将于3分钟后开启;
						ChatMessageService cms = ChatMessageService.getInstance();
						if (cms != null) {
							ChatMessage msg = new ChatMessage();
							msg.setMessageText(description);
							try {
								cms.sendMessageToSystem(msg);
								cms.sendMessageToSystem(msg);
								cms.sendMessageToWorld(msg);
								if (logger.isWarnEnabled()) {
									logger.warn("[城市开始前广播] [成功] [{}]", new Object[] { description });
								}
							} catch (Exception e) {

							}
						}
					}
				}
				// 开战
				if (!开战) {
					if (hour == 开战时 && minute == 开战分) {
						开战();
						String description = Translate.城市争夺战开启;
						ChatMessageService cms = ChatMessageService.getInstance();
						if (cms != null) {
							ChatMessage msg = new ChatMessage();
							msg.setMessageText(description);
							try {
								cms.sendMessageToSystem(msg);
								cms.sendMessageToSystem(msg);
								cms.sendMessageToWorld(msg);
								if (logger.isWarnEnabled()) {
									logger.warn("[城市开始广播] [成功] [{}]", new Object[] { description });
								}
							} catch (Exception e) {

							}
						}
					}
				}

				if (day != nowDay) {
					day = nowDay;
					切换天的操作();
				}
				if (hour >= 开战时 && hour <= 关战时) {
					synchronized (this) {
						if (!kicks.isEmpty() && !kickPlayerGames.isEmpty() && !kickTimes.isEmpty()) {
							for (int i = kicks.size() - 1; i >= 0; i--) {
								if (kicks.get(i) != null && kicks.get(i) && kickPlayerGames.get(i) != null && kickTimes.get(i) != null && heartbeatTime - kickTimes.get(i) >= 20000) {
									kickPlayer(kickPlayerGames.get(i));
									kickTimes.remove(i);
									kicks.remove(i);
									kickPlayerGames.remove(i);
								}
							}
						}
					}
				}
			} catch (Throwable ex) {
				logger.error("[CityFightManager] [心跳异常]", ex);
			}
		}
	}

	private void 开战() {
		开战 = true;

		Hashtable<String, Long> attackMap = cd.getAttackCity_0_country();
		Hashtable<String, Long> map = cd.getHoldCity_0_country();
		开战操作(attackMap, map, 0);

		attackMap = cd.getAttackCity_1_country();
		map = cd.getHoldCity_1_country();
		开战操作(attackMap, map, 1);

		attackMap = cd.getAttackCity_2_country();
		map = cd.getHoldCity_2_country();
		开战操作(attackMap, map, 2);

		attackMap = cd.getAttackCity_3_country();
		map = cd.getHoldCity_3_country();
		开战操作(attackMap, map, 3);
	}

	/**
	 * 如果没有占领者，申请者直接胜利，否则创建出npc
	 * @param attackMap
	 * @param map
	 * @param country
	 */
	public void 开战操作(Hashtable<String, Long> attackMap, Hashtable<String, Long> map, int country) {
		// 开战为在线人设置颜色
		{
			logger.warn("[开战操作test] [attackMap:"+attackMap.size()+"] [attackMap22:"+attackMap+"] [map:"+map.size()+"] [map22:"+map+"] [country:"+country+"]");
			try {
				if (attackMap != null && attackMap.keySet() != null && map != null) {
					for (String key : attackMap.keySet()) {
						if (attackMap.get(key) != null && attackMap.get(key) > 0) {
							if (map.get(key) != null && map.get(key) > 0) {
								设置宗派在线玩家的side(attackMap.get(key));
								设置宗派在线玩家的side(map.get(key));
								String 汉化城市名 = "";
								for (int i = 0; i < 可占领的城市.length; i++) {
									if (可占领的城市[i].equals(key)) {
										汉化城市名 = 可占领的城市汉化[i];
										break;
									}
								}
								城战开始和结束通知(attackMap.get(key), map.get(key), (byte) 0, attackMap.get(key), 汉化城市名);
							}
						}
					}
				}
			} catch (Exception ex) {
				logger.warn("[开战操作test] [ex:"+ex+"]",ex);
			}
			// try{
			// if(map != null && map.keySet() != null){
			// for(String key : map.keySet()){
			// if(map.get(key) != null && map.get(key) > 0){
			// 设置宗派在线玩家的side(map.get(key));
			// }
			// }
			// }
			// }catch(Exception ex){
			//				
			// }
		}
		if (!attackMap.isEmpty()) {
			if (map.isEmpty()) {
				for (String key : attackMap.keySet()) {
					if (attackMap.get(key) != null) {
						通知攻击方胜利(country, key, attackMap.get(key));
						Player[] ps = PlayerManager.getInstance().getOnlineInZongpai(attackMap.get(key));
						if (ps != null) {
							for (Player p : ps) {
								// 活跃度统计
								ActivenessManager.getInstance().record(p, ActivenessType.城市争夺战);
							}
						}
					}
				}
			} else {
				int level = 200;
				BillboardsManager bbm = BillboardsManager.getInstance();
				if (bbm != null) {
					level = bbm.getMaxLevelByBillBoard();
				}
				for (String key : attackMap.keySet()) {
					if (attackMap.get(key) != null && attackMap.get(key) > 0) {
						if (map.get(key) != null && map.get(key) > 0) {
							// 创建NPC
							GameManager gm = GameManager.getInstance();
							if (gm != null) {
								int index = -1;
								for (int i = 0; i < 可占领的城市.length; i++) {
									if (可占领的城市[i].equals(key)) {
										index = i;
									}
								}
								if (index >= 0) {
									Game game = gm.getGameByName(战斗地图[index], country);
									if (game != null) {
										NPCManager npcManager = MemoryNPCManager.getNPCManager();
										int nnId = npcId;
										if (level >= 260) {
											nnId = npcId2;
										}
										if (level >= 280) {
											nnId = npcId3;
										}
										NPC npc = npcManager.createNPC(nnId);
										if (npc instanceof CityFightNPC) {
											CityFightNPC n = (CityFightNPC) npc;
											n.setLevel(level);
											((MemoryNPCManager) npcManager).设置sprite属性值(n);
											n.bornTime = System.currentTimeMillis();
											n.diduiZPId = attackMap.get(key);
											n.standCountry = country;
											n.cityName = key;
											n.setX(npc出现的位置[0]);
											n.setY(npc出现的位置[1]);
											game.addSprite(n);
											logger.warn("[刷新NPC] [成功] [npcid:"+nnId+"] [cityName:" + n.cityName + "] [n.standCountry:" + n.standCountry + "] [n.diduiZPId:" + n.diduiZPId + "] [level:" + n.getLevel() + "] [hp:" + n.getHp() + "/" + n.getMaxHP() + "]");
										} else {
											logger.error("[刷新NPC] [错误] [npc不是CityFightFlushNPC]");
										}
									} else {
										logger.error("[刷新NPC] [错误] [game==null] [战斗地图[index]:" + 战斗地图[index] + "] [country:" + country + "]");
									}
								} else {
									logger.error("[刷新NPC] [错误] [index:" + index + "]");
								}
							}
						} else {
							logger.error("[刷新NPC] [通知攻击方胜利] [value2:" + map.get(key) + "]");
							通知攻击方胜利(country, key, attackMap.get(key));
						}
					}else{
						logger.error("[刷新NPC] [错误22] [value:" + attackMap.get(key) + "]");
					}
				}
			}
		}
	}

	public void 切换天的操作() {
		开战 = false;
		广播1 = false;
		广播2 = false;
		广播3 = false;
		公布报名结果 = false;
		// 清楚每天的报名
		cd.getAttackCity_0_country().clear();
		cd.setAttackCity_0_country(cd.getAttackCity_0_country());
		cd.getAttackCity_0_country_map().clear();
		cd.setAttackCity_0_country_map(cd.getAttackCity_0_country_map());

		cd.getAttackCity_1_country().clear();
		cd.setAttackCity_1_country(cd.getAttackCity_1_country());
		cd.getAttackCity_1_country_map().clear();
		cd.setAttackCity_1_country_map(cd.getAttackCity_1_country_map());

		cd.getAttackCity_2_country().clear();
		cd.setAttackCity_2_country(cd.getAttackCity_2_country());
		cd.getAttackCity_2_country_map().clear();
		cd.setAttackCity_2_country_map(cd.getAttackCity_2_country_map());

		cd.getAttackCity_3_country().clear();
		cd.setAttackCity_3_country(cd.getAttackCity_3_country());
		cd.getAttackCity_3_country_map().clear();
		cd.setAttackCity_3_country_map(cd.getAttackCity_3_country_map());

		// 清楚上一天领取奖励的记录(国家)
		cd.getTodayTakeZhongLiReward().clear();
		cd.setTodayTakeZhongLiReward(cd.getTodayTakeZhongLiReward());

		// 清楚上一天领取奖励的记录(中立)
		cd.getTodayTakeCountryReward().clear();
		cd.setTodayTakeCountryReward(cd.getTodayTakeCountryReward());
		kickTimes.clear();
		kicks.clear();
		kickPlayerGames.clear();
	}

	public synchronized void 领取奖励(int country, Player player) {
		String result = 领取奖励合法性判断(country, player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		Hashtable<String, Long> map = null;
		if (country == 0) {
			map = cd.getHoldCity_0_country();
		} else if (country == 1) {
			map = cd.getHoldCity_1_country();
		} else if (country == 2) {
			map = cd.getHoldCity_2_country();
		} else if (country == 3) {
			map = cd.getHoldCity_3_country();
		}
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
		String cityName = null;
		if (map != null && !map.isEmpty()) {
			for (String key : map.keySet()) {
				Long id = map.get(key);
				if (id != null && id == zp.getId()) {
					cityName = key;
				}
			}
		}

		ArrayList<Long> list = null;

		if (cityName != null) {
			int index = -1;
			for (int i = 0; i < 可占领的城市.length; i++) {
				if (可占领的城市[i].equals(cityName)) {
					index = i;
				}
			}
			if (index >= 0) {
				long yinzi = 可以领取的银子[index];

				// 给银子
				if (country == 0) {
					list = cd.getTodayTakeZhongLiReward();
					if (list == null) {
						list = new ArrayList<Long>();
					}
					list.add(zp.getId());
					cd.setTodayTakeZhongLiReward(list);
				} else {
					list = cd.getTodayTakeCountryReward();
					if (list == null) {
						list = new ArrayList<Long>();
					}
					list.add(zp.getId());
					cd.setTodayTakeCountryReward(list);
				}

				BillingCenter bc = BillingCenter.getInstance();
				try {
					bc.playerSaving(player, yinzi, CurrencyType.SHOPYINZI, SavingReasonType.CITY_FIGHT, "占领城市给的钱");
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您领取了银子, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(yinzi) } }));
					player.addMessageToRightBag(hreq);
					logger.warn("[领取城市奖励银子] [成功] [" + player.getLogString() + "] [country:" + country + "] [zpId:" + zp.getId() + "] [yinzi:" + yinzi + "]");
				} catch (Exception ex) {
					logger.error("[领取城市奖励银子] [异常] [" + player.getLogString() + "]", ex);
				}
			}
		}
	}

	public String 领取奖励合法性判断(int country, Player player) {
		calendar.setTimeInMillis(System.currentTimeMillis());
		if (calendar.get(Calendar.HOUR_OF_DAY) >= 领取奖励时间) {
			return Translate.translateString(Translate.只能在每天的时间领取奖励, new String[][] { { Translate.COUNT_1, 领取奖励时间 + "" } });
		}
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		if (zpm == null) {
			return Translate.服务器出现错误;
		}
		ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
		if (zp == null) {
			return Translate.只有占领城市的宗派宗主才能领取奖励;
		}
		if (zp.getMasterId() != player.getId()) {
			return Translate.只有占领城市的宗派宗主才能领取奖励;
		}

		Hashtable<String, Long> map = null;
		if (country == 0) {
			map = cd.getHoldCity_0_country();
		} else if (country == 1) {
			map = cd.getHoldCity_1_country();
		} else if (country == 2) {
			map = cd.getHoldCity_2_country();
		} else if (country == 3) {
			map = cd.getHoldCity_3_country();
		}

		ArrayList<Long> list = null;

		if (country == 0) {
			list = cd.getTodayTakeZhongLiReward();
			if (list == null) {
				list = new ArrayList<Long>();
				cd.setTodayTakeZhongLiReward(list);
			}
		} else {
			list = cd.getTodayTakeCountryReward();
			if (list == null) {
				list = new ArrayList<Long>();
				cd.setTodayTakeCountryReward(list);
			}
		}

		boolean has = false;
		if (map != null && !map.isEmpty()) {
			for (String key : map.keySet()) {
				Long id = map.get(key);
				if (id != null && id == zp.getId()) {
					has = true;
					if (list.contains(zp.getId())) {
						return Translate.您已经领取了今日奖励;
					}
				}
			}
		} else {
			return Translate.只有占领城市的宗派宗主才能领取奖励;
		}

		if (!has) {
			return Translate.只有占领城市的宗派宗主才能领取奖励;
		}

		return null;
	}

	public boolean 是否是城战防守一方(Player player) {
		byte country = player.getCountry();

		Hashtable<String, Long> table = null;
		if (country == 0) {
			table = cd.getHoldCity_0_country();
		} else if (country == 1) {
			table = cd.getHoldCity_1_country();
		} else if (country == 2) {
			table = cd.getHoldCity_2_country();
		} else if (country == 3) {
			table = cd.getHoldCity_3_country();
		}
		if (table != null) {
			Long zpId = table.get(王城);
			if(zpId == null || zpId.longValue() <= 0){
				zpId = table.get(王城2);
			}
			if(zpId == null || zpId.longValue() <= 0){
				zpId = table.get(王城3);
			}
			if (zpId != null) {
				ZongPaiManager zpm = ZongPaiManager.getInstance();
				if (zpm != null) {
					ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
					if (zp != null && zp.getId() == zpId.longValue()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 能否进入秘境
	 * @param country
	 * @param cityName
	 * @param player
	 * @return
	 */
	public boolean canGoIntoMijing(int country, String cityName, Player player) {
		calendar.setTimeInMillis(System.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if ((hour == 开战时 && minute >= 开战分) || (hour == 关战时 && minute < 关战分)) {
			return true;
		}
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		if (zpm == null) {
			return false;
		}
		ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
		if (zp == null) {
			return false;
		}
		Map<String, Long> map = null;
		if (country == 0) {
			map = cd.getHoldCity_0_country();
		} else if (country == 1) {
			map = cd.getHoldCity_1_country();
		} else if (country == 2) {
			map = cd.getHoldCity_2_country();
		} else if (country == 3) {
			map = cd.getHoldCity_3_country();
		}
		Long id = map.get(cityName);
		if (id != null && zp.getId() == id) {
			return true;
		}
		return false;
	}

	Random random = new Random();

	/**
	 * 把玩家传送进入城市争夺战地图
	 * @param game
	 *            当前地图
	 * @param transferGameName
	 *            想要传送的地图
	 * @param cityName
	 *            城市名
	 * @param country
	 * @param player
	 */
	public void 传送进地图(Game game, String transferGameName, String cityName, int country, Player player) {
		if (canGoIntoMijing(country, cityName, player)) {
			int side = 得到进入玩家的side(cityName, country, player);
			int x = 玩家出现的位置[side][0] + random.nextInt(100) - 50;
			int y = 玩家出现的位置[side][1] + random.nextInt(100) - 50;
			player.setTransferGameCountry(country);
			game.transferGame(player, new TransportData(0, 0, 0, 0, transferGameName, x, y));
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.现在还不能进入地图);
			player.addMessageToRightBag(hreq);
		}
		calendar.setTimeInMillis(System.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if ((hour == 开战时 && minute >= 开战分) || (hour == 关战时 && minute < 关战分)) {
			// 活跃度统计
			ActivenessManager.getInstance().record(player, ActivenessType.城市争夺战);
		}
	}

	public int 得到进入玩家的side(String cityName, int country, Player player) {
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		if (zpm != null) {
			ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
			if (zp != null) {
				Hashtable<String, Long> map = null;
				if (country == 0) {
					map = cd.getHoldCity_0_country();
				} else if (country == 1) {
					map = cd.getHoldCity_1_country();
				} else if (country == 2) {
					map = cd.getHoldCity_2_country();
				} else if (country == 3) {
					map = cd.getHoldCity_3_country();
				}
				if (map != null) {
					if (map.get(cityName) != null && map.get(cityName) == zp.getId()) {
						return 防守方;
					}
				}

				if (country == 0) {
					map = cd.getAttackCity_0_country();
				} else if (country == 1) {
					map = cd.getAttackCity_1_country();
				} else if (country == 2) {
					map = cd.getAttackCity_2_country();
				} else if (country == 3) {
					map = cd.getAttackCity_3_country();
				}
				if (map != null && map.keySet() != null) {
					if (map.get(cityName) != null && map.get(cityName) == zp.getId()) {
						return 进攻方;
					}
				}
			}
		}
		return 其他方;
	}

	public void 设置玩家side(Player player) {
		calendar.setTimeInMillis(System.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if ((hour == 开战时 && minute >= 开战分) || (hour == 关战时 && minute < 关战分)) {
			ZongPaiManager zpm = ZongPaiManager.getInstance();
			if (zpm != null) {
				ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());

				if (zp != null) {

					Hashtable<String, Long> attackMap = cd.getAttackCity_0_country();
					Hashtable<String, Long> holdMap = cd.getHoldCity_0_country();
					boolean ok = 设置side(player, zp.getId(), attackMap, holdMap);

					if (!ok) {
						attackMap = cd.getAttackCity_1_country();
						holdMap = cd.getHoldCity_1_country();
						ok = 设置side(player, zp.getId(), attackMap, holdMap);
					}

					if (!ok) {
						attackMap = cd.getAttackCity_2_country();
						holdMap = cd.getHoldCity_2_country();
						ok = 设置side(player, zp.getId(), attackMap, holdMap);
					}

					if (!ok) {
						attackMap = cd.getAttackCity_3_country();
						holdMap = cd.getHoldCity_3_country();
						ok = 设置side(player, zp.getId(), attackMap, holdMap);
					}

					if (!ok) {
						if (player.getCityFightSide() != 其他方) {
							player.setCityFightSide(其他方);
						}
					}
					return;
				}
			}
		}
		if (player.getCityFightSide() != 其他方) {
			player.setCityFightSide(其他方);
		}
	}

	public boolean 设置side(Player player, long zpId, Hashtable<String, Long> attackMap, Hashtable<String, Long> holdMap) {
		{
			if (attackMap != null && attackMap.keySet() != null) {
				for (String key : attackMap.keySet()) {
					if (attackMap.get(key) != null && attackMap.get(key) == zpId) {
						if (holdMap != null && holdMap.get(key) != null && holdMap.get(key) > 0 && holdMap.get(key) != zpId) {
							if (player.getCityFightSide() != 进攻方) {
								player.setCityFightSide(进攻方);
							}
							return true;
						}
					}
				}
			}
		}

		{
			if (holdMap != null && holdMap.keySet() != null) {
				for (String key : holdMap.keySet()) {
					if (holdMap.get(key) != null && holdMap.get(key) == zpId) {
						if (attackMap != null && attackMap.get(key) != null && attackMap.get(key) > 0 && attackMap.get(key) != zpId) {
							if (player.getCityFightSide() != 防守方) {
								player.setCityFightSide(防守方);
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void 设置宗派在线玩家的side(long zpId) {
		Player[] ps = PlayerManager.getInstance().getOnlineInZongpai(zpId);
		if (ps != null) {
			for (Player p : ps) {
				设置玩家side(p);
			}
		}
	}

	public void 通知攻击方胜利(int country, String cityName, long attackZPId) {

		Hashtable<String, Long> map = null;
		if (country == 0) {
			map = cd.getAttackCity_0_country();
		} else if (country == 1) {
			map = cd.getAttackCity_1_country();
		} else if (country == 2) {
			map = cd.getAttackCity_2_country();
		} else if (country == 3) {
			map = cd.getAttackCity_3_country();
		}
		if (map != null) {
			Long id = map.get(cityName);
			Long lostId = null;
			if (id != null && id == attackZPId) {
				Hashtable<String, Long> maps = null;
				if (country == 0) {
					maps = cd.getHoldCity_0_country();
					if (maps == null) {
						maps = new Hashtable<String, Long>();
					}
					lostId = maps.get(cityName);
					maps.put(cityName, attackZPId);
					cd.setHoldCity_0_country(maps);
					logger.warn("[城市争夺战] [攻击方胜利] [" + country + "] [" + cityName + "] [winId:" + attackZPId + "] [lostId:" + lostId + "]");
				} else if (country == 1) {
					maps = cd.getHoldCity_1_country();
					if (maps == null) {
						maps = new Hashtable<String, Long>();
					}
					lostId = maps.get(cityName);
					maps.put(cityName, attackZPId);
					cd.setHoldCity_1_country(maps);
					logger.warn("[城市争夺战] [攻击方胜利] [" + country + "] [" + cityName + "] [winId:" + attackZPId + "] [lostId:" + lostId + "]");
				} else if (country == 2) {
					maps = cd.getHoldCity_2_country();
					if (maps == null) {
						maps = new Hashtable<String, Long>();
					}
					lostId = maps.get(cityName);
					maps.put(cityName, attackZPId);
					cd.setHoldCity_2_country(maps);
					logger.warn("[城市争夺战] [攻击方胜利] [" + country + "] [" + cityName + "] [winId:" + attackZPId + "] [lostId:" + lostId + "]");
				} else if (country == 3) {
					maps = cd.getHoldCity_3_country();
					if (maps == null) {
						maps = new Hashtable<String, Long>();
					}
					lostId = maps.get(cityName);
					maps.put(cityName, attackZPId);
					cd.setHoldCity_3_country(maps);
					logger.warn("[城市争夺战] [攻击方胜利] [" + country + "] [" + cityName + "] [winId:" + attackZPId + "] [lostId:" + lostId + "]");
				}

				ZongPaiManager zpm = ZongPaiManager.getInstance();
				if (zpm != null) {
					ZongPai zp = zpm.getZongPaiById(attackZPId);
					if (zp != null) {
						try {
							Player player = PlayerManager.getInstance().getPlayer(zp.getMasterId());
							zpm.seizeCity(player, cityName);
							logger.warn("[城战胜利] [成功] [宗派id:{}] [{}] [cityName:{}] [country:{}]", new Object[] { attackZPId, player.getLogString(), cityName, country });
						} catch (Exception ex) {
							logger.error("[城战胜利] [异常] [宗派id:" + attackZPId + "] [cityName:" + cityName + "] [country:" + country + "]", ex);
						}
					} else {
						logger.error("[城战胜利] [失败] [宗派id:" + attackZPId + "] [宗派为空] [cityName:" + cityName + "] [country:" + country + "]");
					}
				} else {
					logger.error("[城战胜利] [异常] [宗派id:" + attackZPId + "] [宗派管理器空] [cityName:" + cityName + "] [country:" + country + "]");
				}

				// 发消息
				if (zpm != null) {
					String description = null;
					int index = 0;
					for (int i = 0; i < 可占领的城市.length; i++) {
						if (可占领的城市[i].equals(cityName)) {
							index = i;
						}
					}
					if (lostId != null && lostId > 0) {
						String lostZPName = "";
						String winZPName = "";
						ZongPai winZp = zpm.getZongPaiById(attackZPId);
						if (winZp != null) {
							winZPName = winZp.getZpname();
						}
						ZongPai lostZp = zpm.getZongPaiById(lostId);
						if (lostZp != null) {
							lostZPName = lostZp.getZpname();
						}
						description = Translate.translateString(Translate.A击败了B获得了城市, new String[][] { { Translate.STRING_1, winZPName }, { Translate.STRING_2, lostZPName }, { Translate.STRING_3, 可占领的城市汉化[index] } });

						ChatMessageService cms = ChatMessageService.getInstance();
						try {
							cms.sendMessageToZongPai5(lostZp, description);
							cms.sendMessageToZongPai5(winZp, description);
						} catch (Exception e) {

						}

						// 设置玩家颜色
						{
							try {
								Player[] ps = PlayerManager.getInstance().getOnlineInZongpai(lostId);
								if (ps != null) {
									for (Player p : ps) {
										if (p.getCityFightSide() != 其他方) {
											p.setCityFightSide(其他方);
										}
									}
								}
							} catch (Exception ex) {
								logger.error("[城战完毕给玩家设置颜色] [异常] [zpId:" + lostId + "]", ex);
							}
						}
					} else {
						String winZPName = "";
						ZongPai winZp = zpm.getZongPaiById(attackZPId);
						if (winZp != null) {
							winZPName = winZp.getZpname();
						}
						description = Translate.translateString(Translate.A胜利了获得了城市, new String[][] { { Translate.STRING_1, winZPName }, { Translate.STRING_3, 可占领的城市汉化[index] } });

						ChatMessageService cms = ChatMessageService.getInstance();
						try {
							cms.sendMessageToZongPai5(winZp, description);
						} catch (Exception e) {

						}
					}

					// 设置玩家颜色
					{
						try {
							Player[] ps = PlayerManager.getInstance().getOnlineInZongpai(attackZPId);
							if (ps != null) {
								for (Player p : ps) {
									if (p.getCityFightSide() != 其他方) {
										p.setCityFightSide(其他方);
									}
								}
							}
						} catch (Exception ex) {
							logger.error("[城战完毕给玩家设置颜色] [异常] [zpId:" + attackZPId + "]", ex);
						}
					}

					if (description != null) {
						ChatMessageService cms = ChatMessageService.getInstance();
						if (cms != null) {
							ChatMessage msg = new ChatMessage();
							msg.setMessageText(description);
							try {
								cms.sendMessageToCountry(country, msg);
								cms.sendMessageToCountry(country, msg);
								cms.sendMessageToCountry2(country, msg);
								cms.sendMessageToCountry2(country, msg);

							} catch (Exception e) {
								if (logger.isWarnEnabled()) logger.warn("[发送战斗结束消息] [异常]", e);
							}
						}
					}
				}
			}
			String 汉化城市名 = "";
			for (int i = 0; i < 可占领的城市.length; i++) {
				if (可占领的城市[i].equals(cityName)) {
					汉化城市名 = 可占领的城市汉化[i];
					break;
				}
			}
			城战开始和结束通知(attackZPId, lostId, (byte) 1, attackZPId, 汉化城市名);
			logger.warn("[城市争夺战] [攻击方胜利] [" + country + "] [" + cityName + "] [" + attackZPId + "]");
		} else {
			logger.warn("[城市争夺战] [攻击方胜利] [系统出现问题] [" + country + "] [" + cityName + "] [" + attackZPId + "]");
		}
	}

	public void 通知攻击方失败(int country, String cityName, long attackZPId) {

		Hashtable<String, Long> map = null;
		if (country == 0) {
			map = cd.getAttackCity_0_country();
		} else if (country == 1) {
			map = cd.getAttackCity_1_country();
		} else if (country == 2) {
			map = cd.getAttackCity_2_country();
		} else if (country == 3) {
			map = cd.getAttackCity_3_country();
		}
		if (map != null) {
			Long id = map.get(cityName);
			Long winId = null;
			if (id != null && id == attackZPId) {
				Hashtable<String, Long> maps = null;
				if (country == 0) {
					maps = cd.getHoldCity_0_country();
					if (maps == null) {
						maps = new Hashtable<String, Long>();
					}
					winId = maps.get(cityName);
				} else if (country == 1) {
					maps = cd.getHoldCity_1_country();
					if (maps == null) {
						maps = new Hashtable<String, Long>();
					}
					winId = maps.get(cityName);
				} else if (country == 2) {
					maps = cd.getHoldCity_2_country();
					if (maps == null) {
						maps = new Hashtable<String, Long>();
					}
					winId = maps.get(cityName);
				} else if (country == 3) {
					maps = cd.getHoldCity_3_country();
					if (maps == null) {
						maps = new Hashtable<String, Long>();
					}
					winId = maps.get(cityName);
				}

				// 发消息
				int index = 0;
				ZongPaiManager zpm = ZongPaiManager.getInstance();
				if (zpm != null) {
					String description = null;

					for (int i = 0; i < 可占领的城市.length; i++) {
						if (可占领的城市[i].equals(cityName)) {
							index = i;
						}
					}
					if (winId != null && winId > 0) {
						String lostZPName = "";
						String winZPName = "";
						ZongPai lostZp = zpm.getZongPaiById(attackZPId);
						if (lostZp != null) {
							lostZPName = lostZp.getZpname();
						}
						ZongPai winZp = zpm.getZongPaiById(winId);
						if (winZp != null) {
							winZPName = winZp.getZpname();
						}
						description = Translate.translateString(Translate.A成功防守住了B的攻击, new String[][] { { Translate.STRING_1, winZPName }, { Translate.STRING_2, lostZPName }, { Translate.STRING_3, 可占领的城市汉化[index] } });
						ChatMessageService cms = ChatMessageService.getInstance();
						try {
							cms.sendMessageToZongPai5(winZp, description);
							cms.sendMessageToZongPai5(lostZp, description);
						} catch (Exception e) {

						}
						// 设置玩家颜色
						{
							try {
								Player[] ps = PlayerManager.getInstance().getOnlineInZongpai(winId);
								if (ps != null) {
									for (Player p : ps) {
										if (p.getCityFightSide() != 其他方) {
											p.setCityFightSide(其他方);
										}
									}
								}
							} catch (Exception ex) {
								logger.error("[城战完毕给玩家设置颜色] [异常] [zpId:" + winId + "]", ex);
							}
						}
					} else {
						String lostZPName = "";
						ZongPai lostZp = zpm.getZongPaiById(attackZPId);
						if (lostZp != null) {
							lostZPName = lostZp.getZpname();
						}
						description = Translate.translateString(Translate.B失败了, new String[][] { { Translate.STRING_2, lostZPName }, { Translate.STRING_3, 可占领的城市汉化[index] } });
						ChatMessageService cms = ChatMessageService.getInstance();
						try {
							cms.sendMessageToZongPai5(lostZp, description);
						} catch (Exception e) {

						}
					}

					// 设置玩家颜色
					{
						try {
							Player[] ps = PlayerManager.getInstance().getOnlineInZongpai(attackZPId);
							if (ps != null) {
								for (Player p : ps) {
									if (p.getCityFightSide() != 其他方) {
										p.setCityFightSide(其他方);
									}
								}
							}
						} catch (Exception ex) {
							logger.error("[城战完毕给玩家设置颜色] [异常] [zpId:" + attackZPId + "]", ex);
						}
					}

					if (description != null) {
						ChatMessageService cms = ChatMessageService.getInstance();
						if (cms != null) {
							ChatMessage msg = new ChatMessage();
							msg.setMessageText(description);
							try {
								cms.sendMessageToCountry(country, msg);
								cms.sendMessageToCountry(country, msg);
								cms.sendMessageToCountry2(country, msg);
								cms.sendMessageToCountry2(country, msg);
							} catch (Exception e) {
								if (logger.isWarnEnabled()) logger.warn("[发送战斗结束消息] [异常]", e);
							}
						}
					}
				}
			}
			String 汉化城市名 = "";
			for (int i = 0; i < 可占领的城市.length; i++) {
				if (可占领的城市[i].equals(cityName)) {
					汉化城市名 = 可占领的城市汉化[i];
					break;
				}
			}
			城战开始和结束通知(winId, attackZPId, (byte) 1, attackZPId, 汉化城市名);
			logger.warn("[城市争夺战] [攻击方失败] [" + country + "] [" + cityName + "] [" + attackZPId + "]");
		}
	}

	/**
	 * 当type为0时，winId和lostId为对战双方，当type为1时，winId和lostId为胜负双方
	 * @param winId
	 * @param lostId
	 * @param type
	 */
	public void 城战开始和结束通知(Long winerId, Long losterId, byte type, long attackId, String cityName) {
		long winId = -1;
		long lostId = -1;
		String description = "";
		String sideAName = Translate.无;
		String sideAIcon = "";
		String sideBName = Translate.无;
		String sideBIcon = "";
		if (winerId != null) {
			winId = winerId;
			if (attackId == winerId) {
				sideAIcon = "hd_gong";
			} else {
				sideAIcon = "hd_shou";
			}
		}
		if (losterId != null) {
			lostId = losterId;
			if (attackId == losterId) {
				sideBIcon = "hd_gong";
			} else {
				sideBIcon = "hd_shou";
			}
		}

		PlayerManager pm = PlayerManager.getInstance();
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		ZongPai winZp = zpm.getZongPaiById(winId);
		if (winZp != null) {
			sideAName = winZp.getZpname();
			description = Translate.translateString(Translate.宗派获得管理权, new String[][] { { Translate.STRING_1, sideAName }, { Translate.STRING_2, cityName } });
		}
		ZongPai lostZp = zpm.getZongPaiById(lostId);
		if (lostZp != null) {
			sideBName = lostZp.getZpname();
		}
		SEND_CITYFIGHT_STATE_REQ req = new SEND_CITYFIGHT_STATE_REQ(GameMessageFactory.nextSequnceNum(), type, description, sideAName, sideAIcon, sideBName, sideBIcon);
		if (winZp != null) {
			Player[] ps = pm.getOnlineInZongpai(winId);
			if (ps != null) {
				for (Player p : ps) {
					p.addMessageToRightBag(req);
					if (type == 1) {
						if (AchievementManager.getInstance() != null) {
							AchievementManager.getInstance().record(p, RecordAction.获得城市争夺战胜利, 1);
						}
					}
					logger.warn("[城战开始和结束通知] [type:+" + type + "] [winerId:" + winerId + "] [losterId:" + losterId + "] [attackId:" + attackId + "] [cityName:" + cityName + "] [" + p.getLogString() + "]");
				}
			}
		}
		if (lostZp != null) {
			Player[] ps = pm.getOnlineInZongpai(lostId);
			if (ps != null) {
				for (Player p : ps) {
					p.addMessageToRightBag(req);
					logger.warn("[城战开始和结束通知] [type:+" + type + "] [winerId:" + winerId + "] [losterId:" + losterId + "] [attackId:" + attackId + "] [cityName:" + cityName + "] [" + p.getLogString() + "]");
				}
			}
		}
	}

	public boolean 是否在城战地图(Player p) {
		for (String key : 战斗地图) {
			if (key.equals(p.getGame())) {
				return true;
			}
		}
		return false;
	}

	public String 得到城战地图(String cityName) {
		for (int i = 0; i < 可占领的城市.length; i++) {
			if (可占领的城市[i].equals(cityName)) {
				return 战斗地图[i];
			}
		}
		return null;
	}

	public synchronized void 放弃城市(int country, Player player) {
		String result = 放弃城市合法性判断(country, player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		Hashtable<String, Long> map = null;
		if (country == 0) {
			map = cd.getHoldCity_0_country();
		} else if (country == 1) {
			map = cd.getHoldCity_1_country();
		} else if (country == 2) {
			map = cd.getHoldCity_2_country();
		} else if (country == 3) {
			map = cd.getHoldCity_3_country();
		}
		String cityName = null;
		ZongPaiManager zpm = ZongPaiManager.getInstance();

		ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());

		if (map != null && map.keySet() != null) {
			for (String key : map.keySet()) {
				if (map.get(key) == zp.getId()) {
					cityName = key;
				}
			}
		}
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		mw.setTitle(Translate.申请放弃城市);
		mw.setDescriptionInUUB(Translate.translateString(Translate.您真的要放弃城市吗, new String[][] { { Translate.STRING_1, 得到可占领的城市汉化(cityName) } }));
		Option_CityFight_quedingfangqizhanlingchengshi option = new Option_CityFight_quedingfangqizhanlingchengshi();
		option.setText(Translate.确定放弃城市);
		option.setColor(0xFFFFFF);
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.取消);
		cancel.setColor(0xFFFFFF);
		mw.setOptions(new Option[] { option, cancel });
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);
	}

	public synchronized void 确定放弃城市(int country, Player player) {
		String result = 放弃城市合法性判断(country, player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		Hashtable<String, Long> map = null;
		if (country == 0) {
			map = cd.getHoldCity_0_country();
		} else if (country == 1) {
			map = cd.getHoldCity_1_country();
		} else if (country == 2) {
			map = cd.getHoldCity_2_country();
		} else if (country == 3) {
			map = cd.getHoldCity_3_country();
		}
		String cityName = null;
		ZongPaiManager zpm = ZongPaiManager.getInstance();

		ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());

		if (map != null && map.keySet() != null) {
			for (String key : map.keySet()) {
				if (map.get(key) == zp.getId()) {
					cityName = key;
				}
			}
		}

		if (cityName != null) {
			map.remove(cityName);
			if (country == 0) {
				cd.setHoldCity_0_country(map);
			} else if (country == 1) {
				cd.setHoldCity_1_country(map);
			} else if (country == 2) {
				cd.setHoldCity_2_country(map);
			} else if (country == 3) {
				cd.setHoldCity_3_country(map);
			}

			if (cityName.equals(王城) || cityName.equals(王城2) || cityName.equals(王城3)) {
				CountryManager cm = CountryManager.getInstance();
				cm.罢免国王(player.getCountry());
				// 系统滚动广播
				// 广播
				String description = Translate.translateString(Translate.放弃国王, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, 得到可占领的城市汉化(cityName) }, { Translate.STRING_3, CountryManager.得到官职名(CountryManager.国王) }, { Translate.STRING_4, CountryManager.得到国家名(country) } });
				ChatMessageService cms = ChatMessageService.getInstance();
				if (cms != null) {
					ChatMessage msg = new ChatMessage();
					msg.setMessageText(description);
					try {
						cms.sendRoolMessageToSystem(msg);
						if (logger.isWarnEnabled()) {
							logger.warn("[放弃城市，放弃国王] [成功] [{}] [{}]", new Object[] { player.getLogString(), description });
						}
					} catch (Exception e) {
						if (logger.isWarnEnabled()) {
							logger.warn("[放弃城市，放弃国王] [异常]", e);
						}
					}
				}
			}

			// 广播
			String description = Translate.translateString(Translate.宗派放弃城市, new String[][] { { Translate.STRING_1, zp.getZpname() }, { Translate.STRING_2, 得到可占领的城市汉化(cityName) }, { Translate.STRING_3, CountryManager.得到国家名(country) } });
			ChatMessageService cms = ChatMessageService.getInstance();
			if (cms != null) {
				ChatMessage msg = new ChatMessage();
				msg.setMessageText(description);
				try {
					cms.sendMessageToSystem(msg);
					cms.sendMessageToSystem(msg);
					cms.sendMessageToWorld(msg);
					if (logger.isWarnEnabled()) {
						logger.warn("[放弃城市] [成功] [{}] [{}]", new Object[] { player.getLogString(), description });
					}
				} catch (Exception e) {
					if (logger.isWarnEnabled()) {
						logger.warn("[放弃城市] [异常]", e);
					}
				}
			}

		}
	}

	public String 放弃城市合法性判断(int country, Player player) {
		Hashtable<String, Long> map = null;
		if (country == 0) {
			map = cd.getHoldCity_0_country();
		} else if (country == 1) {
			map = cd.getHoldCity_1_country();
		} else if (country == 2) {
			map = cd.getHoldCity_2_country();
		} else if (country == 3) {
			map = cd.getHoldCity_3_country();
		}
		String cityName = null;
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		if (zpm == null) {
			return Translate.服务器出现错误;
		}
		ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
		if (zp == null) {
			return Translate.您不是宗族族长不能执行此操作;
		}
		if (zp.getMasterId() != player.getId()) {
			return Translate.您不是宗族族长不能执行此操作;
		}
		if (map != null && map.keySet() != null) {
			for (String key : map.keySet()) {
				if (map.get(key) == zp.getId()) {
					cityName = key;
				}
			}
		}
		if (cityName == null) {
			return Translate.您在这个国家没有占领城市;
		}
		return null;
	}

	public String 得到占领城市及帮派信息(int country) {
		StringBuffer sb = new StringBuffer();
		sb.append("<f>").append(CountryManager.得到国家名(country)).append(Translate.城市信息一览).append("</f>\n<f>");
		if (country == 0) {
			for (String cityName : 可占领的中立城市汉化) {
				sb.append(cityName).append(" ");
			}
		} else {
			String ctitys [] = 可占领的国家城市汉化2[country-1];
			for (String cityName : ctitys) {
				sb.append(cityName).append(" ");
			}
		}
		sb.append("</f>\n<f>");
		sb.append("----------------------</f>\n");
		Hashtable<String, Long> map = null;
		if (country == 0) {
			map = cd.getHoldCity_0_country();
		} else if (country == 1) {
			map = cd.getHoldCity_1_country();
		} else if (country == 2) {
			map = cd.getHoldCity_2_country();
		} else if (country == 3) {
			map = cd.getHoldCity_3_country();
		}
		boolean has1 = false;
		if (map != null && map.keySet() != null) {
			ZongPaiManager zpm = ZongPaiManager.getInstance();
			if (zpm != null) {
				sb.append("<f>").append(Translate.被占领的城市).append("</f>\n");
				for (String key : map.keySet()) {
					Long id = map.get(key);
					sb.append("<f>");
					if (id != null && id > 0) {
						ZongPai zp = zpm.getZongPaiById(id);
						if (zp != null) {
							has1 = true;
							sb.append(" ").append(得到可占领的城市汉化(key)).append(" ").append(zp.getZpname()).append("宗派").append(Translate.占领的宗派).append("</f>\n");
						}
					}
				}
			}
		}
		if (has1 == false) {
			sb.append("<f>").append(Translate.无宗派占领城市 + "</f>\n");
		}
		sb.append("<f>").append("----------------------</f>\n");
		if (country == 0) {
			map = cd.getAttackCity_0_country();
		} else if (country == 1) {
			map = cd.getAttackCity_1_country();
		} else if (country == 2) {
			map = cd.getAttackCity_2_country();
		} else if (country == 3) {
			map = cd.getAttackCity_3_country();
		}
		boolean has2 = false;
		if (map != null && map.keySet() != null) {
			ZongPaiManager zpm = ZongPaiManager.getInstance();
			if (zpm != null) {
				sb.append("<f>").append(Translate.被申请攻打的城市).append("</f>\n<f>");
				for (String key : map.keySet()) {
					Long id = map.get(key);
					if (id != null && id > 0) {
						ZongPai zp = zpm.getZongPaiById(id);
						if (zp != null) {
							has2 = true;
							sb.append(" ").append(得到可占领的城市汉化(key)).append(" ").append(zp.getZpname()).append("宗派").append(Translate.申请攻打的宗派).append("</f>\n");
						}
					}
				}
			}
		}
		if (has2 == false) {
			sb.append(Translate.无宗派申请攻打城市);
		}
		return sb.toString();
	}

	ArrayList<Boolean> kicks = new ArrayList<Boolean>();
	ArrayList<Game> kickPlayerGames = new ArrayList<Game>();
	ArrayList<Long> kickTimes = new ArrayList<Long>();

	public synchronized void notifyGameKickPlayer(Game game) {
		kicks.add(true);
		kickPlayerGames.add(game);
		kickTimes.add(System.currentTimeMillis());
	}

	public void kickPlayer(Game game) {
		// 把玩家踢出地图
		{
			try {
				LivingObject[] los = game.getLivingObjects();
				for (LivingObject lo : los) {
					if (lo instanceof Player) {
						((Player) lo).setTransferGameCountry(((Player) lo).getCountry());
						game.transferGame((Player) lo, new TransportData(0, 0, 0, 0, ((Player) lo).getResurrectionMapName(), ((Player) lo).getResurrectionX(), ((Player) lo).getResurrectionY()));
					}
				}
			} catch (Exception ex) {

			}
		}
	}

	public boolean 是否参加王城城战(Player player) {
		byte country = player.getCountry();

		Hashtable<String, Long> table = null;
		if (country == 0) {
			table = cd.getAttackCity_0_country();
		} else if (country == 1) {
			table = cd.getAttackCity_1_country();
		} else if (country == 2) {
			table = cd.getAttackCity_2_country();
		} else if (country == 3) {
			table = cd.getAttackCity_3_country();
		}
		if (table != null) {
			Long zpId = table.get(王城);
			if(zpId == null || zpId.longValue() <= 0){
				zpId = table.get(王城2);
			}
			if(zpId == null || zpId.longValue() <= 0){
				zpId = table.get(王城3);
			}
			if (zpId != null) {
				ZongPaiManager zpm = ZongPaiManager.getInstance();
				if (zpm != null) {
					ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
					if (zp != null && zp.getId() == zpId.longValue()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean 是否参加城战(Player player, String type) {
		byte country = player.getCountry();

		Hashtable<String, Long> table = null;
		if (country == 0) {
			table = cd.getAttackCity_0_country();
		} else if (country == 1) {
			table = cd.getAttackCity_1_country();
		} else if (country == 2) {
			table = cd.getAttackCity_2_country();
		} else if (country == 3) {
			table = cd.getAttackCity_3_country();
		}
		if (table != null) {
			Long zpId = table.get(type);
			if (zpId != null) {
				ZongPaiManager zpm = ZongPaiManager.getInstance();
				if (zpm != null) {
					ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
					if (zp != null && zp.getId() == zpId.longValue()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String 得到城战守方信息(String mapName) {
		String mess = "";
		Hashtable<String, Long> table = null;
		for (int i = 0; i < 4; i++) {
			table = cd.getHoldCity_0_country();
			mess = i + "--" + (table != null ? table.get(mapName) : "0");
		}
		return mess;
	}

	public boolean 是否是城战防守一方(Player player, String type) {
		byte country = player.getCountry();

		Hashtable<String, Long> table = null;
		if (country == 0) {
			table = cd.getHoldCity_0_country();
		} else if (country == 1) {
			table = cd.getHoldCity_1_country();
		} else if (country == 2) {
			table = cd.getHoldCity_2_country();
		} else if (country == 3) {
			table = cd.getHoldCity_3_country();
		}
		if (table != null) {
			Long zpId = table.get(type);
			if (zpId != null) {
				ZongPaiManager zpm = ZongPaiManager.getInstance();
				if (zpm != null) {
					ZongPai zp = zpm.getZongPaiByPlayerId(player.getId());
					if (zp != null && zp.getId() == zpId.longValue()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
