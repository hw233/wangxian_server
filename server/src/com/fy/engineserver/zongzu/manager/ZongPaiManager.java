package com.fy.engineserver.zongzu.manager;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.XX加入本宗派;
import static com.fy.engineserver.datasource.language.Translate.XX离开本宗派;
import static com.fy.engineserver.datasource.language.Translate.translateString;
import static com.fy.engineserver.datasource.language.Translate.xx把宗派禅让给了你请查看邮件;
import static com.fy.engineserver.datasource.language.Translate.xx解散了;
import static com.fy.engineserver.datasource.language.Translate.你不是宗主只能宗主可以使用;
import static com.fy.engineserver.datasource.language.Translate.你宗派中的人都不在线;
import static com.fy.engineserver.datasource.language.Translate.你把宗派禅让给了xx;
import static com.fy.engineserver.datasource.language.Translate.你现在是混元至圣不能进行禅让;
import static com.fy.engineserver.datasource.language.Translate.你还没有宗派;
import static com.fy.engineserver.datasource.language.Translate.使用宗主令;
import static com.fy.engineserver.datasource.language.Translate.创建宗派需要xx您银子不足;
import static com.fy.engineserver.datasource.language.Translate.增加宗派繁荣度xx;
import static com.fy.engineserver.datasource.language.Translate.宗主令;
import static com.fy.engineserver.datasource.language.Translate.无;
import static com.fy.engineserver.datasource.language.Translate.离开了XX;
import static com.fy.engineserver.datasource.language.Translate.空;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageItem;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.chat.ChatMessageTask;
import com.fy.engineserver.cityfight.CityFightManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.society.unite.Option_ZongPai_zhaoji;
import com.fy.engineserver.menu.society.zongpai.Option_Zongpai_Invite_agree;
import com.fy.engineserver.menu.society.zongpai.Option_Zongpai_Invite_disagree;
import com.fy.engineserver.menu.society.zongpai.Option_Zongpai_Leave_Confirm;
import com.fy.engineserver.message.DISMISS_ZONGPAI_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.message.LEAVE_ZONGPAI_APPLY_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.ZONGPAI_INFO_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.septbuilding.entity.JiazuBedge;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class ZongPaiManager {
	
	private UnitedServerManager unitedManager;

	public static int 上线增加繁荣度 = 1;
	public static int 说话增加繁荣度 = 1;
	public static int 每天增加繁荣度最大 = 500;
	public static int 创建宗派花费 = 5 * 1000 * 1000; // 5锭
	public static String 创建宗派花费锭 = Translate.translateString(Translate.创建宗派花费锭, new String[][]{{Translate.COUNT_1, (创建宗派花费/1000/1000)+""}}); // 5锭

	/**
	 * 宗派名最大限制
	 */
	public static int ZONGPAINAME_MAX_LENGTH = 18;
	/**
	 * 宗派宣言最大限制
	 */
	public static int ZONGPAIDECALRATION_MAX_LENGTH = 30;
	/**
	 * 宗派密码最大限制
	 */
	public static int ZONGPAI_PASSWROD_MAX = 18;
	public static int ZONGPAI_PASSWROD_MIN = 1;
	/**
	 * 宗派密码提示最大限制
	 */
	public static int ZONGPAI_PASSWRODHINT_NUM = 18;

	/**
	 * 宗派家族数量最大限制
	 */
	public static int ZONGPAI_MAX_JIAZU_NUM = 5;

	public static Logger logger = LoggerFactory.getLogger(ZongPaiManager.class);
	public static SimpleEntityManager<ZongPai> em;

	private ConcurrentHashMap<Long, ZongPai> zongPaiMap = new ConcurrentHashMap<Long, ZongPai>();
	private static ZongPaiManager self = null;

	public static ZongPaiManager getInstance() {
		return self;
	}

	public String tagforbid[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", " ", "　", "　" };
	public String tagforbid_korea[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", "　", "　" };

	private ChatMessageService chatMessageService;
	private PlayerManager playerManager;

	/**
	 * 城市名(非中立城市 城市_国家) 宗派id
	 */
	private HashMap<String, Long> citySeizeMap = new HashMap<String, Long>();

	public String obtainCityByPlayer(Player player, String cityName) {

		String[] citys = CityFightManager.可占领的城市;
		boolean valid = false;
		for (String st : citys) {
			if (st.equals(cityName)) {
				valid = true;
				break;
			}
		}
		if (valid) {
			if (cityName.equals(CityFightManager.中立城市)) {
				return cityName;
			} else {
				return cityName + "_" + player.getCountry();
			}
		} else {
			ZongPaiManager.logger.error("[占领城市获得城市名错误] [" + player.getLogString() + "] [" + cityName + "]");

		}
		return null;
	}

	/**
	 * 判断是否含有禁用的标签字符
	 * 
	 * @param name
	 * @return true 不含有(可用)
	 */
	private boolean tagValid(String name) {
		String aname = name.toLowerCase();
		String [] temp = null;
		if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			temp = tagforbid_korea;
		} else {
			temp = tagforbid;
		}
		for (String s : temp) {
			if (aname.indexOf(s) != -1) {
				logger.warn("输入有违禁字符,输入的[" + name + "] 匹配到的[" + s + "]");
				return false;
			}
		}
		return true;
	}

	// 存储占领城市的宗派
	private String diskFile;
	DiskCache disk = null;

	public void init() {
		
		self = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(ZongPai.class);

		File file = new File(diskFile);
		disk = new DefaultDiskCache(file, null, "seizeCity", 100L * 365 * 24 * 3600 * 1000L);

		if (disk.get("seizeCity") != null) {
			citySeizeMap = (HashMap<String, Long>) disk.get("seizeCity");
		} else {
			citySeizeMap = new HashMap<String, Long>();
		}
		if (UnitServerFunctionManager.isActvity(UnitServerFunctionManager.ACTIVITY_ZHONGPAI_MONEY)) {
			创建宗派花费 = (int)(创建宗派花费 * UnitServerFunctionManager.ZONGPAI_ZHEKOU);
			创建宗派花费锭 = Translate.translateString(Translate.创建宗派花费锭, new String[][]{{Translate.COUNT_1, ((float)创建宗派花费/1000/1000)+""}});
		}
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {
		em.destroy();
	}

	/**
	 * 创建宗派
	 * @param player
	 * @param zongpaiName
	 * @return
	 */
	public void createZongPai(Player player, ZongPai zongpai) {

		try {
			em.save(zongpai);
			this.zongPaiMap.put(zongpai.getId(), zongpai);

			if (logger.isWarnEnabled()) {
				logger.warn("[创建宗派成功] [" + player.getLogString() + "] [" + zongpai.getLogString() + "]");
			}
		} catch (Exception e) {
			logger.error("[创建宗派错误] [" + player.getLogString() + "]", e);
		}

	}

	/**
	 * 空为没有，返回的是城市名_国家
	 * @param zp
	 * @return
	 */
	public String getCityNameByZongPai(ZongPai zp) {
		String name = zp.getSeizeCity();
		if (name != null) return name;
		return "";
	}

	/**
	 * 占领城市
	 * @param player
	 * @return
	 */
	public synchronized String seizeCity(Player player, String cityName) {

		String realCityName = this.obtainCityByPlayer(player, cityName);
		if (realCityName == null) {
			return null;
		}
		String result = "";
		try {
			long jiazuId = player.getJiazuId();
			if (jiazuId != -1) {
				Jiazu jz = JiazuManager.getInstance().getJiazu(jiazuId);
				long zpId = jz.getZongPaiId();
				if (zpId != -1) {
					ZongPai zp = this.getZongPaiById(zpId);
					if (zp != null) {
						if (zp.getMasterId() != player.getId()) {
							result = Translate.你不是宗派族长只有宗派族长才能占领;
						} else {

							if (citySeizeMap.get(realCityName) != null) {
								long seizeZpId = citySeizeMap.get(realCityName);
								ZongPai seizeZp = this.getZongPaiById(seizeZpId);
								if (seizeZp != null) {
									seizeZp.setSeizeCity("");
								}
							}

							if (zp.getSeizeCity() != null && !zp.getSeizeCity().equals("")) {
								citySeizeMap.remove(realCityName);
								zp.setSeizeCity("");
							}
							// 占领
							zp.setSeizeCity(realCityName);
							this.citySeizeMap.put(realCityName, zpId);

							this.getDisk().put("seizeCity", citySeizeMap);
							if (ZongPaiManager.logger.isWarnEnabled()) {
								ZongPaiManager.logger.warn("[修改占领城市map] [" + player.getLogString() + "] [" + cityName + "]");
							}
							if (cityName.equals(CityFightManager.王城) || cityName.equals(CityFightManager.王城2) || cityName.equals(CityFightManager.王城3)) {
								try {
									CountryManager.getInstance().占领王城(player);
									ZongPaiManager.logger.error("[占领王城] [" + player.getLogString() + "] [" + zp.getLogString() + "]");
								} catch (Exception e) {
									ZongPaiManager.logger.error("[占领王城] [" + player.getLogString() + "]", e);
								}
							}
						}
					}
				} else {
					result = Translate.你还没有宗派只有宗派族长才能占领;
				}
			} else {
				result = Translate.你还没有家族只有宗派族长才能占领;
			}
		} catch (Exception e) {
			logger.error("[占领城市异常] [" + (player != null ? player.getName() : "") + "]", e);
		}
		return result;
	}

	/**
	 * 宗派名,宣言验证
	 * @param p
	 * @param name
	 * @return
	 */
	public String zongPaiNameValidate(String name, String declaration) {

		String result = "";
		if (name.getBytes().length > ZONGPAINAME_MAX_LENGTH) {
			result = Translate.宗派名太长;
		} else if (declaration.getBytes().length > ZONGPAIDECALRATION_MAX_LENGTH) {
			result = Translate.宗派宣言太长;
		} 
		else if(name != null &&  name.trim().length() <= 0)
		{
			result = Translate.name含有违禁字符;
		}
		else {
			boolean bln = tagValid(name);
			if (bln) {
				ZongPai zp = null;
				List<ZongPai> temp = null;
				try {
					temp = em.query(ZongPai.class, "zpname = ?", new Object[] { name.trim() }, null, 1, 10);
				} catch (Exception e) {
					logger.error("[宗派名验证异常] [" + name + "]");
				}
				if (temp != null && temp.size() > 0) {
					zp = temp.get(0);
				}
				if (zp != null) {
					this.zongPaiMap.put(zp.getId(), zp);
					result = Translate.此宗派名字已经存在;
				}
			} else {
				result = Translate.name含有违禁字符;
			}

			bln = tagValid(declaration);
			if (!bln) {
				result = Translate.declaration含有违禁字符;
			} else {
				if (logger.isInfoEnabled()) logger.info("[宗派名验证成功]");
			}
		}
		return result;
	}

	/**
	 * 宗派宣言验证
	 * @param p
	 * @param name
	 * @return
	 */
	public String zongPaiDeclarationValidate(String declaration) {

		String result = "";
		if (declaration.getBytes().length > ZONGPAIDECALRATION_MAX_LENGTH) {
			result = Translate.宗派宣言太长;
		} else {
			boolean bln = tagValid(declaration);
			if (!bln) {
				result = Translate.declaration含有违禁字符;
			} else {
				if (logger.isInfoEnabled()) logger.info("[宗派宣言验证成功]");
			}
		}
		return result;
	}

	/**
	 * 宗主邀请加入宗派
	 * @param player
	 * @param playerA
	 */
	public void addToZongPai(Player player, Player playerA) {

		String result = this.邀请加入宗派判断(player, playerA);
		if (result != "") {
			player.send_HINT_REQ(result);
		} else {
			Jiazu jz = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			long id = jz.getZongPaiId();
			ZongPai zp = this.getZongPaiById(id);
			if (zp != null) {
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				mw.setTitle(Translate.邀请加入宗派);
				String description = Translate.translateString(Translate.xx邀请你加入宗派xx, new String[][] { { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_1, zp.getZpname() } });
				mw.setDescriptionInUUB(description);

				Option_Zongpai_Invite_agree option = new Option_Zongpai_Invite_agree();
				option.setZp(zp);
				option.setText(Translate.确定);
				Option_Zongpai_Invite_disagree cancel = new Option_Zongpai_Invite_disagree();
				cancel.setP(player);
				cancel.setText(Translate.取消);
				mw.setOptions(new Option[] { option, cancel });
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				playerA.addMessageToRightBag(res);
				if (logger.isInfoEnabled()) {
					logger.info("[宗主发送宗派邀请成功] [" + player.getLogString() + "] [对方:" + playerA.getLogString() + "]");
				}
			} else {
				logger.error("[邀请加入宗派失败] [" + player.getLogString() + "] [zp null]");
			}
		}

	}

	/**
	 * 同意加入宗派
	 * @param p
	 * @param zp
	 */
	public void agreeInvite(Player p, ZongPai zp) {

		synchronized (zp) {
			String result = this.被邀请家族加入宗派判断(p, zp);
			if (result != "") {
				p.send_HINT_REQ(result);
				return;
			}
			long jiazuId = p.getJiazuId();
			zp.getJiazuIds().add(jiazuId);
			zp.setJiazuIds(zp.getJiazuIds());
			Jiazu jz = JiazuManager.getInstance().getJiazu(jiazuId);
			jz.setZongPaiId(zp.getId());
			try {
				em.save(zp);
			} catch (Exception e2) {
				logger.error("[同意加入宗派异常] [" + p.getLogString() + "]", e2);
				return;
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[族长同意加入宗派成功] [" + p.getLogString() + "] [" + zp.getLogString() + "]");
			}

			Player master1;
			try {
				setAllZongPaiName(jz, zp.getZpname());
				Player[] ps = playerManager.getOnlineInZongpai(zp.getId());
				for (Player p1 : ps) {
					ZONGPAI_INFO_RES res1 = queryZongPaiInfo(p1);
					p1.addMessageToRightBag(res1);
				}
				// 宗派频道说话
				// 宗派频道说话
				ChatMessage mes = new ChatMessage();
				mes.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				mes.setMessageText(translateString(XX加入本宗派, new String[][] { { STRING_1, jz.getName() } }));
				// mes.setMessageText(jz.getName()+Translate.加入本宗派);
				mes.setSenderId(-1);
				mes.setSenderName("");
				mes.setSort(ChatChannelType.ZONG);
				ChatMessageItem item = new ChatMessageItem();
				mes.setAccessoryItem(item);
				ChatMessageTask task = new ChatMessageTask();
				mes.setAccessoryTask(task);
				chatMessageService.sendMessageToZong(zp, mes);

				// chatMessageService.sendMessageToZong(zp, message);
				// 家族频道说话
			} catch (Exception e1) {
				if (logger.isWarnEnabled()) logger.warn("[同意加入宗派异常] [" + p.getLogString() + "]", e1);
			}
		}
	}

	/**
	 * 不同意加入宗派
	 * @param p
	 * @param zp
	 */
	public void disagreeInvite(Player p, Player playerA) {

		if (playerA.isOnline()) {
			String description = Translate.translateString(Translate.xx拒绝了你的加入宗派邀请, new String[][] { { Translate.PLAYER_NAME_1, p.getName() } });
			playerA.send_HINT_REQ(description);

			if (logger.isWarnEnabled()) {
				logger.warn("[族长拒绝了加入宗派] [" + p.getLogString() + "]");
			}
		}
	}

	/**
	 * 解散宗派确认
	 * @param p
	 */
	public void dismissZongPaiConfirm(Player p) {

		String result = this.宗主身份判断(p);
		if (result != "") {
			p.send_HINT_REQ(result);
		} else {
			ZongPai zp = this.getZongPaiByPlayerId(p.getId());
			if (zp == null) {
				logger.error("[解散宗派错误] [" + p.getLogString() + "] [zpnull]");
				return;
			}
			// 解散
			List<Long> jiazuIds = zp.getJiazuIds();
			JiazuManager jm = JiazuManager.getInstance();
			Player[] ps = playerManager.getOnlineInZongpai(zp.getId());
			DISMISS_ZONGPAI_RES res = new DISMISS_ZONGPAI_RES(GameMessageFactory.nextSequnceNum());
			for (Player p1 : ps) {
				p1.addMessageToRightBag(res);
			}
			// 宗派频道说话
			ChatMessage mes = new ChatMessage();
			mes.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			mes.setMessageText(translateString(xx解散了, new String[][] { { STRING_1, zp.getZpname() } }));
			// mes.setMessageText(zp.getZpname()+解散了);
			mes.setSenderId(-1);
			mes.setSenderName("");
			mes.setSort(ChatChannelType.ZONG);
			ChatMessageItem item = new ChatMessageItem();
			mes.setAccessoryItem(item);
			ChatMessageTask task = new ChatMessageTask();
			mes.setAccessoryTask(task);
			chatMessageService.sendMessageToZong(zp, mes);

			// 世界广播
			// <宗派名称>宗派已经在修仙界除名！（宗派）
			String des = Translate.translateString(Translate.宗派已经在修仙界除名, new String[][] { { Translate.STRING_1, zp.getZpname() } });

			try {
				chatMessageService.sendMessageToZongPai(zp, des);
				if (logger.isInfoEnabled()) {
					logger.info("[解散宗派发送解散通知] [" + p.getLogString() + "] [" + zp.getZpname() + "]");
				}
			} catch (Exception e) {
				logger.error("[解散宗派发送解散通知错误] [" + p.getLogString() + "] [" + zp.getZpname() + "]", e);
			}

			for (long id : jiazuIds) {
				Jiazu jz = jm.getJiazu(id);
				if (jz != null) {
					setAllZongPaiName(jz, "");
					jz.setZongPaiId(-1);
				}
			}

			try {
				em.remove(zp);
				if (logger.isWarnEnabled()) {
					logger.warn("[解散宗派成功] [" + p.getLogString() + "] [" + zp.getLogString() + "]");
				}
			} catch (Exception e) {
				logger.warn("[解散宗派异常] [" + p.getLogString() + "]", e);
			}
		}
	}

	/**
	 * 离开宗派申请
	 * @param p
	 */
	public void leaveFromZongPaiApply(Player p) {

		String result = jiazuMasterValidate(p);

		if (result.equals("")) {
			ZongPai zp = this.getZongPaiByPlayerId(p.getId());
			if (zp != null) {

				if (zp.getMasterId() == p.getId()) {
					result = Translate.宗主不能离开;
				} else {
					if (zp.getJiazuIds().contains(p.getJiazuId())) {
						WindowManager windowManager = WindowManager.getInstance();
						MenuWindow mw = windowManager.createTempMenuWindow(600);
						mw.setTitle(Translate.离开宗派);
						String description = Translate.translateString(Translate.你的家族确定要离开xx宗派吗, new String[][] { { Translate.STRING_1, zp.getZpname() } });
						mw.setDescriptionInUUB(description);

						Option_Zongpai_Leave_Confirm option = new Option_Zongpai_Leave_Confirm();
						option.setText(Translate.确定);
						Option_Cancel cancel = new Option_Cancel();
						cancel.setText(Translate.取消);
						mw.setOptions(new Option[] { option, cancel });
						QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
						p.addMessageToRightBag(res);
						// logger.info("[离开宗派申请成功] [] ["+p.getId()+"] ["+p.getName()+"] ["+p.getUsername()+"] []");
						if (logger.isInfoEnabled()) {
							logger.info("[离开宗派申请成功] [" + p.getLogString() + "]");
						}
						return;
					}
				}
			}
		} else {
			p.send_HINT_REQ(result);
		}

	}

	/**
	 * 离开宗派确认
	 * @param p
	 */
	public void leaveFromZongPaiConfirm(Player p) {

		String result = jiazuMasterValidate(p);
		if (result.equals("")) {
			ZongPai zp = this.getZongPaiByPlayerId(p.getId());
			if (zp != null) {
				if (zp.getJiazuIds().contains(p.getJiazuId())) {
					Jiazu jz = JiazuManager.getInstance().getJiazu(p.getJiazuId());
					if (jz != null) {
						LEAVE_ZONGPAI_APPLY_RES res = new LEAVE_ZONGPAI_APPLY_RES(GameMessageFactory.nextSequnceNum(), p.getJiazuId());
						Player[] ps = playerManager.getOnlineInZongpai(zp.getId());
						for (Player p1 : ps) {
							p1.addMessageToRightBag(res);
						}

						jz.setZongPaiId(-1);
						setAllZongPaiName(jz, "");
						zp.getJiazuIds().remove(p.getJiazuId());
						zp.setJiazuIds(zp.getJiazuIds());

						// 宗派频道说话
						// 宗派频道说话
						ChatMessage mes = new ChatMessage();
						mes.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						mes.setMessageText(translateString(XX离开本宗派, new String[][] { { STRING_1, jz.getName() } }));
						// mes.setMessageText(jz.getName()+离开本宗派);
						mes.setSenderId(-1);
						mes.setSenderName("");
						mes.setSort(ChatChannelType.ZONG);
						ChatMessageItem item = new ChatMessageItem();
						mes.setAccessoryItem(item);
						ChatMessageTask task = new ChatMessageTask();
						mes.setAccessoryTask(task);
						chatMessageService.sendMessageToZong(zp, mes);

						// chatMessageService.sendMessageToJiazu(jz, 离开了+zp.getZpname(), "");
						chatMessageService.sendMessageToJiazu(jz, translateString(离开了XX, new String[][] { { STRING_1, zp.getZpname() } }), "");
					}

					// logger.info("[离开宗派成功] [] ["+p.getId()+"] ["+p.getName()+"] ["+p.getUsername()+"] []");
					if (logger.isInfoEnabled()) {
						logger.info("[离开宗派成功] [" + p.getLogString() + "]");
					}
					return;
				}
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("[离开宗派成功] [" + p.getLogString() + "] [" + result + "]");
			}
			p.send_HINT_REQ(result);
		}

	}

	/**
	 * 逐出宗派确认
	 * @param p
	 * @param id
	 */
	public String kickFromZongPaiConfirm(Player p, long jiazuId) {

		String result = this.宗主身份判断(p);
		if (result != "") {
			return result;
		} else {
			if (p.getJiazuId() == jiazuId) {
				result = Translate.宗主不能被踢出;
				return result;
			}
			ZongPai zp = this.getZongPaiByPlayerId(p.getId());
			Jiazu jz = JiazuManager.getInstance().getJiazu(jiazuId);
			if (jz != null) {
				LEAVE_ZONGPAI_APPLY_RES res = new LEAVE_ZONGPAI_APPLY_RES(GameMessageFactory.nextSequnceNum(), jiazuId);
				Player[] ps = playerManager.getOnlineInZongpai(zp.getId());
				for (Player p1 : ps) {
					p1.addMessageToRightBag(res);
				}
				setAllZongPaiName(jz, "");
				jz.setZongPaiId(-1);
				zp.getJiazuIds().remove(jiazuId);
				zp.setJiazuIds(zp.getJiazuIds());

				// 宗派频道说话
				// 宗派频道说话
				ChatMessage mes = new ChatMessage();
				mes.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				mes.setMessageText(translateString(XX离开本宗派, new String[][] { { STRING_1, jz.getName() } }));
				// mes.setMessageText(jz.getName()+离开本宗派);
				mes.setSenderId(-1);
				mes.setSenderName("");
				mes.setSort(ChatChannelType.ZONG);
				ChatMessageItem item = new ChatMessageItem();
				mes.setAccessoryItem(item);
				ChatMessageTask task = new ChatMessageTask();
				mes.setAccessoryTask(task);
				chatMessageService.sendMessageToZong(zp, mes);

				chatMessageService.sendMessageToJiazu(jz, translateString(离开了XX, new String[][] { { STRING_1, zp.getZpname() } }), "");
				// chatMessageService.sendMessageToJiazu(jz, 离开了+zp.getZpname(), "");

				// logger.info("[逐出宗派成功] [] ["+p.getId()+"] ["+p.getName()+"] ["+p.getUsername()+"] []");
				if (logger.isInfoEnabled()) {
					logger.info("[逐出宗派成功] [" + p.getLogString() + "] [jiazuid:" + jiazuId + "]");
				}
				return "";
			}

		}
		return null;
	}

	/**
	 * 
	 * @param master
	 *            宗主
	 * @param other
	 *            另外一个人
	 */
	public void setPlayerAsZongPaiMaster(Player master, Player other) {

		try {

			if (master.getCountryPosition() == CountryManager.国王) {
				master.send_HINT_REQ(你现在是混元至圣不能进行禅让);
				return;
			}
			ZongPai zp = this.getZongPaiByPlayerId(master.getId());
			if (zp != null) {
				if (zp.getMasterId() == master.getId()) {

					zp.setMasterId(other.getId());
					// master.send_HINT_REQ("你把宗派禅让给了"+other.getName());
					master.send_HINT_REQ(translateString(你把宗派禅让给了xx, new String[][] { { STRING_1, other.getName() } }));
					if (other.isOnline()) {
						// other.sendError(master.getName()+"把宗派禅让给了你，请查看邮件");
						other.sendError(translateString(xx把宗派禅让给了你请查看邮件, new String[][] { { STRING_1, master.getName() } }));
					}

					// 发邮件
					String content = Translate.translateString(Translate.宗派禅让邮件内容, new String[][] { { Translate.PLAYER_NAME_1, master.getName() }, { Translate.STRING_1, zp.getPassword() }, { Translate.STRING_2, zp.getPasswordHint() } });
					MailManager.getInstance().sendMail(other.getId(), null, Translate.宗派禅让邮件通知, content, 0, 0, 0, "");

					if (logger.isWarnEnabled()) {
						logger.warn("[转让宗派成功] [" + master.getLogString() + "] [现在宗主id:" + other.getId() + "]");
					}

					ChatMessage mes = new ChatMessage();
					mes.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					String text = Translate.translateString(Translate.xx把宗派禅让给了xx, new String[][] { { Translate.PLAYER_NAME_1, master.getName() }, { Translate.PLAYER_NAME_2, other.getName() } });
					mes.setMessageText(text);
					mes.setSenderId(-1);
					mes.setSenderName("");
					mes.setSort(ChatChannelType.ZONG);
					ChatMessageItem item = new ChatMessageItem();
					mes.setAccessoryItem(item);
					ChatMessageTask task = new ChatMessageTask();
					mes.setAccessoryTask(task);
					// 宗派频道说话
					chatMessageService.sendMessageToZong(zp, mes);

					logger.info("[禅让宗派频道说话] [" + master.getLogString() + "] [现在宗主:" + other.getLogString() + "]");

				} else {
					ZongPaiManager.logger.error("[设置某人成为宗主错误] [不是宗主] [" + master.getLogString() + "]");
				}
			} else {
				ZongPaiManager.logger.error("[设置某人成为宗主错误] [没有宗族] [" + master.getLogString() + "]");
			}
		} catch (Exception e) {
			ZongPaiManager.logger.error("[设置某人成为宗主异常] [" + master.getLogString() + "] [" + other.getLogString() + "]", e);
		}

	}

	/**
	 * 禅让宗派
	 * @param p
	 * @param id
	 */
	public void demiseZongPai(Player p, long jiazuId) {

		String result = this.宗主身份判断(p);
		if (result != "") {
			p.send_HINT_REQ(result);
		} else {
			if (p.getCountryPosition() == CountryManager.国王) {
				p.send_HINT_REQ(你现在是混元至圣不能进行禅让);
				return;
			}
			// 禅让
			Jiazu jz = JiazuManager.getInstance().getJiazu(jiazuId);
			if (jz != null) {
				long masterId = jz.getJiazuMaster();
				if (masterId == p.getId()) {
					p.send_HINT_REQ(Translate.不能禅让给宗主);
					return;
				}

				try {
					Player master1 = playerManager.getPlayer(masterId);
					ZongPai zp = this.getZongPaiByPlayerId(p.getId());
					zp.setMasterId(masterId);
					List<Long> ids = zp.getJiazuIds();
					List<Long> nowIds = new ArrayList<Long>();
					nowIds.add(jz.getJiazuID());
					nowIds.add(p.getJiazuId());
					for (int i = 1; i < ids.size(); i++) {
						if (ids.get(i) == jiazuId) {
							continue;
						}
						nowIds.add(ids.get(i));
					}
					zp.setJiazuIds(nowIds);
					zp.setJiazuIds(zp.getJiazuIds());

					p.send_HINT_REQ(Translate.禅让成功);

					if (master1.isOnline()) {
						// master1.sendError(p.getName()+"xx把宗派禅让给了你");
						master1.sendError(translateString(xx把宗派禅让给了你请查看邮件, new String[][] { { STRING_1, p.getName() } }));
					}

					// 发邮件
					String content = Translate.translateString(Translate.宗派禅让邮件内容, new String[][] { { Translate.PLAYER_NAME_1, p.getName() }, { Translate.STRING_1, zp.getPassword() }, { Translate.STRING_2, zp.getPasswordHint() } });
					MailManager.getInstance().sendMail(masterId, null, Translate.宗派禅让邮件通知, content, 0, 0, 0, "");

					if (logger.isWarnEnabled()) {
						logger.warn("[禅让宗派成功] [" + p.getLogString() + "] [" + masterId + "]");
					}

					Player[] ps = playerManager.getOnlineInZongpai(zp.getId());
					for (Player p1 : ps) {
						if (p1.getId() != masterId) {

							ZONGPAI_INFO_RES ress = this.queryZongPaiInfo(p1);
							if (ress != null) {
								p1.addMessageToRightBag(ress);
							}
						}
					}

					ChatMessage mes = new ChatMessage();
					mes.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					String text = Translate.translateString(Translate.xx把宗派禅让给了xx, new String[][] { { Translate.PLAYER_NAME_1, p.getName() }, { Translate.PLAYER_NAME_2, master1.getName() } });
					mes.setMessageText(text);
					mes.setSenderId(-1);
					mes.setSenderName("");
					mes.setSort(ChatChannelType.ZONG);
					ChatMessageItem item = new ChatMessageItem();
					mes.setAccessoryItem(item);
					ChatMessageTask task = new ChatMessageTask();
					mes.setAccessoryTask(task);
					// 宗派频道说话
					chatMessageService.sendMessageToZong(zp, mes);
					// logger.info("[禅让宗派频道说话] [] ["+p.getId()+"] ["+p.getName()+"] ["+p.getUsername()+"] []");
					if (logger.isInfoEnabled()) {
						logger.info("[禅让宗派频道说话] [] [{}] [{}] [{}] []", new Object[] { p.getId(), p.getName(), p.getUsername() });
					}
				} catch (Exception e) {
					logger.error("[禅让宗派异常] [" + p.getLogString() + "]", e);
				}
			}
		}

	}

	/**
	 * 修改宣言
	 * @param p
	 * @param message
	 */
	public String updateDeclaration(Player p, String declaration) {

		String result = this.宗主身份判断(p);
		if (!result.equals("")) {
			return result;
		} else {
			// 修改
			ZongPai zp = this.getZongPaiByPlayerId(p.getId());
			zp.setDeclaration(declaration);

			// logger.error("[修改宗派宗旨成功] ["+declaration+"] ["+p.getId()+"] ["+p.getName()+"] ["+p.getUsername()+"] []");
			logger.error("[修改宗派宗旨成功] [{}] [{}] [{}] [{}] []", new Object[] { declaration, p.getId(), p.getName(), p.getUsername() });

			ChatMessage mes = new ChatMessage();
			mes.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			mes.setMessageText(Translate.宗派宣言修改了);
			mes.setSenderId(-1);
			mes.setSenderName("");
			mes.setSort(ChatChannelType.ZONG);
			ChatMessageItem item = new ChatMessageItem();
			mes.setAccessoryItem(item);
			ChatMessageTask task = new ChatMessageTask();
			mes.setAccessoryTask(task);
			// 宗派频道说话
			chatMessageService.sendMessageToZong(zp, mes);

		}
		return "";
	}

	private String 邀请加入宗派判断(Player player, Player playerA) {

		if (player.getJiazuId() == -1) {
			return Translate.只有宗主才能够邀请;
		}
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return Translate.只有宗主才能够邀请;
		}
		if (jiazu.getJiazuMaster() != player.getId()) {
			return Translate.只有宗主才能够邀请;
		}
		ZongPai zp = this.getZongPaiByPlayerId(player.getId());
		if (zp == null) {
			return Translate.只有宗主才能够邀请;
		}
		if (zp.getMasterId() != player.getId()) {
			return Translate.只有宗主才能够邀请;
		}

		if (zp.getJiazuNum() >= ZONGPAI_MAX_JIAZU_NUM) {
			return Translate.宗派家族数量已达最大;
		}

		Jiazu jiazu1 = JiazuManager.getInstance().getJiazu(playerA.getJiazuId());
		if (jiazu1 == null) {
			return Translate.对方没有家族;
		}
		if (jiazu1.getZongPaiId() != -1) {
			return Translate.对方已经加入了其他宗派无法邀请;
		}
		if (jiazu1.getJiazuMaster() != playerA.getId()) {
			return Translate.对方不是家族族长不能被邀请;
		}

		if (player.getCountry() != playerA.getCountry()) {
			return Translate.对方跟你不是一个国家;
		}
		return "";
	}

	private String 被邀请家族加入宗派判断(Player player, ZongPai zp) {

		long jiazuId = player.getJiazuId();
		Jiazu jz = JiazuManager.getInstance().getJiazu(jiazuId);
		if (jz == null || jz.getZongPaiId() != -1 || jz.getJiazuMaster() != player.getId()) {
			return Translate.加入宗派错误;
		}

		if (zp.getJiazuNum() >= ZONGPAI_MAX_JIAZU_NUM) {
			return Translate.宗派家族数量已达最大;
		}

		if (zp.getJiazuIds().contains(jiazuId)) {
			return Translate.已经在宗派中;
		}

		long masterId = zp.getMasterId();
		try {
			Player master = PlayerManager.getInstance().getPlayer(masterId);
			if (master.getCountry() != player.getCountry()) {
				return Translate.对方跟你不是一个国家;
			}
		} catch (Exception e) {
			ZongPaiManager.logger.error("[取宗派宗主错误] [" + player.getLogString() + "] [宗主id:" + masterId + "]", e);
		}
		return "";
	}

	/**
	 * 根据playerId 得到宗派
	 * @param id
	 * @return
	 */
	public ZongPai getZongPaiByPlayerId(long id) {
		try {
			Player p = this.playerManager.getPlayer(id);
			if (p.getJiazuId() != -1) {
				Jiazu jz = JiazuManager.getInstance().getJiazu(p.getJiazuId());
				if (jz == null) {
					if (logger.isWarnEnabled()) logger.warn("[查询宗派错误] [没有家族] [" + id + "] ");
					return null;
				}
				ZongPai zp = this.getZongPaiById(jz.getZongPaiId());
				return zp;
			}
		} catch (Exception e) {
			ZongPaiManager.logger.error("[得到宗派] [" + id + "]", e);
		}
		return null;
	}

	private String jiazuMasterValidate(Player p) {

		String result = "";
		long id = p.getJiazuId();
		if (id > 0) {
			Jiazu jz = JiazuManager.getInstance().getJiazu(id);
			if (jz != null) {
				if (jz.getJiazuMaster() == p.getId()) {
					return result;
				} else {
					result = Translate.不是家族族长;
				}
			} else {
				result = Translate.你没有家族;
			}
		} else {
			result = Translate.你没有家族;
		}

		return result;
	}

	public String 宗主身份判断(Player p) {
		ZongPai zp = this.getZongPaiByPlayerId(p.getId());
		String result = "";
		if (zp == null) {
			result = Translate.不是宗主;
		} else if (zp.getMasterId() != p.getId()) {
			result = Translate.不是宗主;
		}
		return result;
	}

	public ZongPai getZongPaiById(long zongpaiId) {

		ZongPai zp = null;
		zp = this.zongPaiMap.get(zongpaiId);
		if (zp == null) {
			try {
				zp = em.find(zongpaiId);
			} catch (Exception e) {
				logger.error("[查询宗派异常]", e);
			}
			if (zp != null) {
				this.zongPaiMap.put(zp.getId(), zp);
				return zp;
			}
			logger.error("[查询宗派失败] [宗派null]");
		}
		return zp;
	}

	// 返回 "" 条件符合 字符串为错误提示 null 为钱不够 弹出提示框
	public String 创建宗派合法性判断(Player player) {

		if (player == null) {
			return Translate.人不能为空;
		}
		long jiazuId = player.getJiazuId();
		if (jiazuId <= -1) {
			return Translate.身份不符请成为族长以后再来申请建立宗派;
		}
		JiazuManager jm = JiazuManager.getInstance();
		Jiazu jiazu = jm.getJiazu(jiazuId);
		if (jiazu == null) {
			return Translate.身份不符请成为族长以后再来申请建立宗派;
		}
		// System.out.println("jiazumasterId" + jiazu.getJiazuMaster());
		if (jiazu.getJiazuMaster() != player.getId()) {
			return Translate.身份不符请成为族长以后再来申请建立宗派;
		}
		if(player.getVipLevel() < 11){
			return "需要vip等级达到11级以上才可以创建";
		}
		if (jiazu.getZongPaiId() != -1) {
			if (this.getZongPaiById(jiazu.getZongPaiId()).getMasterId() == player.getId()) {
				return Translate.你已经是宗主不能在创建宗派;
			} else {
				return Translate.请先离开您现在的宗派再来申请建立宗派;
			}
		}
		if (player.getSilver() < 创建宗派花费) {
			String description = translateString(创建宗派需要xx您银子不足, new String[][] { { STRING_1, 创建宗派花费锭 } });

			BillingCenter.银子不足时弹出充值确认框(player, description);
			ZongPaiManager.logger.error("[创建宗派银子不足] [" + player.getLogString() + "] [" + player.getSilver() + "]");
			return null;
		}
		return "";
	}

	/**
	 * 一个人离开家族，有宗派的话离开宗派(主要用设置头上称号)
	 * @param player
	 * @param jiazuId
	 */
	public void oneLeaveJiazuUpdateZongpai(Player player, long jiazuId) {

		Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
		if (jiazu != null) {
			ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(jiazu.getZongPaiId());
			if (zp != null) {
				player.setZongPaiName("");
				if (logger.isWarnEnabled()) {
					logger.warn("[某人离开宗派] [" + player.getLogString() + "] [" + zp.getZpname() + "]");
				}
			}
		}
	}

	public void setAllZongPaiName(Jiazu jiazu, String zongPaiName) {

		// List<Player> ps = jiazu.getOnLineMembers();
		// for(Player p : ps){
		// p.setZongPaiName(zongPaiName);
		// }
		try {
			long jiazuId = jiazu.getJiazuID();
			Player[] ps = playerManager.getCachedPlayers();
			for (Player player : ps) {
				if (player.getJiazuId() == jiazuId) {
					player.setZongPaiName(zongPaiName);
				}
			}
		} catch (Exception e) {
			JiazuManager.logger.error("[解散家族设置宗派name异常] [" + jiazu.getLogString() + "] [zpName:" + zongPaiName + "]", e);
		}
	}

	public ZONGPAI_INFO_RES queryZongPaiInfo(Player player) {

		ZongPai zp = this.getZongPaiByPlayerId(player.getId());
		if (zp == null) {
			player.sendError(你还没有宗派);
		} else {
			long masterId = zp.getMasterId();
			// Player master1 = null;

			PlayerSimpleInfo simplePlayer = PlayerSimpleInfoManager.getInstance().getInfoById(masterId);

			// try {
			// master1 = playerManager.getPlayer(masterId);
			// } catch (Exception e2) {
			// logger.error("[查询宗派异常] ["+player.getLogString()+"] [宗主null]" , e2);
			// }

			String masterName = 无;
			if (simplePlayer != null) {
				masterName = simplePlayer.getName();
			}

			List<Long> jiazuIdArr = zp.getJiazuIds();
			List<Long> realJiazuIdArr = new ArrayList<Long>();
			JiazuManager jm = JiazuManager.getInstance();
			for (long id : jiazuIdArr) {
				Jiazu jz = jm.getJiazu(id);
				if (jz == null) {
					logger.error("[查询家族错误] [家族null] [" + player.getLogString() + "] [" + id + "]");
				} else {
					realJiazuIdArr.add(id);
				}
			}
			jiazuIdArr = realJiazuIdArr;
			long[] jiazuids = new long[jiazuIdArr.size()];
			String[] jiazuNames = new String[jiazuIdArr.size()];
			String[] jiazuBar = new String[jiazuIdArr.size()];
			long[] playerids = new long[jiazuIdArr.size()];
			String[] playerNames = new String[jiazuIdArr.size()];
			byte[] level = new byte[jiazuIdArr.size()];
			int i = 0;
			int onlineNum = 0;
			for (Long id : jiazuIdArr) {
				Jiazu jz = jm.getJiazu(id);
				if (jz == null) {
					logger.error("[查询家族错误] [家族null] [" + player.getLogString() + "]");
					break;
				}

				jiazuids[i] = id;
				jiazuNames[i] = jz.getName();
				int bedgeId = jz.getUsedBedge();
				JiazuBedge bedge = SeptStationMapTemplet.getInstance().getBedge(bedgeId);
				if (bedge == null) {
					jiazuBar[i] = "";
				} else {
					jiazuBar[i] = bedge.getResName();
				}

				long jiazuMasterId = jz.getJiazuMaster();
				// Player master = null;
				PlayerSimpleInfo master = null;
				try {
					// master = this.playerManager.getPlayer(jiazuMaster);
					master = PlayerSimpleInfoManager.getInstance().getInfoById(jiazuMasterId);
				} catch (Exception e) {
					ZongPaiManager.logger.error("[查询宗派异常] [族长null] [" + player.getLogString() + "]", e);
				}
				String 族长name = 空;
				if (master != null) {
					族长name = master.getName();
				}
				playerids[i] = jiazuMasterId;
				playerNames[i] = 族长name;
				level[i] = (byte) jz.getLevel();
				i++;
			}
			onlineNum = playerManager.getOnlineInZongpai(zp.getId()).length;
			if (logger.isInfoEnabled()) {
				logger.info("[查询宗派成功] [" + player.getLogString() + "]");
			}
			ZONGPAI_INFO_RES res = new ZONGPAI_INFO_RES(GameMessageFactory.nextSequnceNum(), zp.getZpname(), masterName, zp.getDeclaration(), onlineNum, zp.getFanrongdu(), jiazuids, jiazuNames, jiazuBar, level, playerids, playerNames);
			return res;
		}
		return null;
	}

	public void 上线增加繁荣度(Player player) {
		try {
			ZongPai zp = this.getZongPaiByPlayerId(player.getId());
			if (zp == null) {
				return;
			}

			if (player.getLoginAddProsperityTime() > 0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(player.getLoginAddProsperityTime());
				int oldy = calendar.get(Calendar.MONTH);
				int oldd = calendar.get(Calendar.DAY_OF_YEAR);

				calendar.setTimeInMillis(SystemTime.currentTimeMillis());
				int newy = calendar.get(Calendar.MONTH);
				int newd = calendar.get(Calendar.DAY_OF_YEAR);

				if (oldy == newy && oldd == newd) {
					if (logger.isInfoEnabled()) {
						logger.info("[上线增加宗派繁荣度] [今天已经增加] [" + player.getLogString() + "] [" + zp.getLogString() + "]");
					}
					return;
				} else {
					player.setLoginAddProsperityTime(SystemTime.currentTimeMillis());
					增加繁荣度(zp, player, 上线增加繁荣度);
					if (logger.isInfoEnabled()) {
						logger.info("[上线增加宗派繁荣度] [" + player.getLogString() + "] [" + zp.getLogString() + "]");
					}
				}
			} else {
				player.setLoginAddProsperityTime(SystemTime.currentTimeMillis());
				增加繁荣度(zp, player, 上线增加繁荣度);
				if (logger.isInfoEnabled()) {
					logger.info("[上线增加宗派繁荣度] [" + player.getLogString() + "] [" + zp.getLogString() + "]");
				}
			}
		} catch (Exception e) {
			logger.info("[上线增加宗派繁荣度异常] [" + player.getLogString() + "]", e);
		}
	}

	public void 说话增加繁荣度(Player player) {
		try {
			ZongPai zp = this.getZongPaiByPlayerId(player.getId());
			if (zp == null) {
				return;
			}
			if (player.getSpeakAddProsperityTime() > 0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(player.getSpeakAddProsperityTime());
				int oldy = calendar.get(Calendar.MONTH);
				int oldd = calendar.get(Calendar.DAY_OF_YEAR);

				calendar.setTimeInMillis(SystemTime.currentTimeMillis());
				int newy = calendar.get(Calendar.MONTH);
				int newd = calendar.get(Calendar.DAY_OF_YEAR);

				if (oldy == newy && oldd == newd) {
					if (logger.isInfoEnabled()) {
						logger.info("[说话增加宗派繁荣度] [今天已经增加] [" + player.getLogString() + "] [" + zp.getLogString() + "]");
					}
					return;
				} else {
					player.setSpeakAddProsperityTime(SystemTime.currentTimeMillis());
					增加繁荣度(zp, player, 说话增加繁荣度);
					if (logger.isInfoEnabled()) {
						logger.info("[说话增加宗派繁荣度] [" + player.getLogString() + "] [" + zp.getLogString() + "]");
					}
				}
			} else {
				player.setSpeakAddProsperityTime(SystemTime.currentTimeMillis());
				增加繁荣度(zp, player, 说话增加繁荣度);
				if (logger.isInfoEnabled()) {
					logger.info("[说话增加宗派繁荣度] [" + player.getLogString() + "] [" + zp.getLogString() + "]");
				}
			}
		} catch (Exception e) {
			logger.info("[说话增加宗派繁荣度异常] [" + player.getLogString() + "]", e);
		}

	}

	// 玩家可以增加繁荣度 ,判断今天繁荣度是否到达上限
	public void 增加繁荣度(ZongPai zongPai, Player player, int num) {
		synchronized (zongPai) {
			boolean add = false;
			long lastTime = zongPai.getLastAddProsperityTime();
			if (lastTime > 0) {

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(zongPai.getLastAddProsperityTime());
				int oldy = cal.get(Calendar.MONTH);
				int oldd = cal.get(Calendar.DAY_OF_YEAR);

				cal.setTimeInMillis(SystemTime.currentTimeMillis());
				int newy = cal.get(Calendar.MONTH);
				int newd = cal.get(Calendar.DAY_OF_YEAR);

				int nowNum = 0;
				if (oldy == newy && oldd == newd) {
					nowNum = zongPai.getDayAddProsperityNum();
				} else {
					zongPai.setLastAddProsperityTime(SystemTime.currentTimeMillis());
					zongPai.setDayAddProsperityNum(0);
				}
				if (nowNum < 每天增加繁荣度最大) {
					add = true;
					zongPai.setDayAddProsperityNum(zongPai.getDayAddProsperityNum() + num);
					zongPai.setFanrongdu(zongPai.getFanrongdu() + num);
				}

			} else {
				zongPai.setLastAddProsperityTime(SystemTime.currentTimeMillis());
				zongPai.setDayAddProsperityNum(zongPai.getDayAddProsperityNum() + num);
				zongPai.setFanrongdu(zongPai.getFanrongdu() + num);
				add = true;
			}

			if (add) {
				// player.send_HINT_REQ("增加宗派繁荣度"+num+"点");
				player.send_HINT_REQ(translateString(增加宗派繁荣度xx, new String[][] { { STRING_1, num + "" } }));
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[增加宗派繁荣度] [增加(true):" + add + "] [" + player.getLogString() + "] [" + zongPai.getLogString() + "]");
			}
		}
	}

	public String checkUseCall(Player player) {

		Jiazu jiazu = null;
		ZongPai zongPai = null;
		long zongPaiId = -1;
		long masterId = -1;
		long jiazuId = player.getJiazuId();
		if (jiazuId > 0) {
			jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
		}

		if (jiazu != null) {
			zongPaiId = jiazu.getZongPaiId();
			if (zongPaiId > 0) {
				zongPai = ZongPaiManager.getInstance().getZongPaiById(zongPaiId);
			}
		} else {
			ZongPaiManager.logger.warn("[使用宗主令错误] [" + player.getLogString() + "] [家族null] [" + jiazuId + "]");
		}

		if (zongPai != null) {
			masterId = zongPai.getMasterId();
		} else {
			ZongPaiManager.logger.warn("[使用宗主令错误] [" + player.getLogString() + "] [宗派null] [" + zongPaiId + "]");
		}

		if (masterId == player.getId()) {

			Player[] ps = playerManager.getOnlineInZongpai(zongPai.getId());
			if (ps != null && ps.length > 1) {
				return null;
			} else {
				return 你宗派中的人都不在线;
			}
		} else {
			ZongPaiManager.logger.warn("[使用宗主令错误] [" + player.getLogString() + "] [不是宗主] [" + masterId + "]");
			return 你不是宗主只能宗主可以使用;
		}
	}

	public boolean 使用宗主令(Player player, String articleName) {

		String result = this.checkUseCall(player);
		if (result == null) {
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setTitle(宗主令);
			mw.setDescriptionInUUB(使用宗主令);

			Option_ZongPai_zhaoji option = new Option_ZongPai_zhaoji();
			option.setText(Translate.召唤);
			option.setArticleName(articleName);
			option.setInvite(player);

			Option_UseCancel cancel = new Option_UseCancel();
			cancel.setText(Translate.取消);
			mw.setOptions(new Option[] { option, cancel });

			INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, 宗主令, Translate.召唤, Translate.取消, new byte[0]);
			player.addMessageToRightBag(res);
		} else {
			player.sendError(result);
		}
		return false;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setChatMessageService(ChatMessageService chatMessageService) {
		this.chatMessageService = chatMessageService;
	}

	public String getDiskFile() {
		return diskFile;
	}

	public void setDiskFile(String diskFile) {
		this.diskFile = diskFile;
	}

	public DiskCache getDisk() {
		return disk;
	}

	public void setDisk(DiskCache disk) {
		this.disk = disk;
	}

	public void setUnitedManager(UnitedServerManager unitedManager) {
		this.unitedManager = unitedManager;
	}

	public UnitedServerManager getUnitedManager() {
		return unitedManager;
	}
}
