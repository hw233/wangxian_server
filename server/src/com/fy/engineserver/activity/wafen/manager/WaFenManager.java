package com.fy.engineserver.activity.wafen.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Common;
import com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Private;
import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel;
import com.fy.engineserver.activity.wafen.model.FenMuArticleModel;
import com.fy.engineserver.activity.wafen.model.FenMuModel;
import com.fy.engineserver.activity.wafen.model.WaFenActivity;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.wafen.Option_ConfirmExchange;
import com.fy.engineserver.menu.wafen.Option_ConfirmWafen4Ten;
import com.fy.engineserver.menu.wafen.Option_UseChanziType;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class WaFenManager {
	public static Logger logger = ActivitySubSystem.logger;

	public static WaFenManager instance;

	private String fileName;
	private String diskFile; // 名字使用活动结束时间拼接出来(spring中只配置表名头信息，会动态加上活动结束时间与.ddc)
	public DiskCache disk = null;

	/** 个人坟坑信息 (key=playerId) */
	public Map<Long, WaFenInstance4Private> privateMaps = new Hashtable<Long, WaFenInstance4Private>();// disk中key=活动结束时间
	/** 共有坟坑信息 (key=坟墓name) */
	public Map<String, WaFenInstance4Common> commonMaps = new Hashtable<String, WaFenInstance4Common>();// disk中key=default
	/** 坟墓model(key=坟墓名) */
	public LinkedHashMap<String, FenMuModel> fenmuMap = new LinkedHashMap<String, FenMuModel>(); // 读配表(顺序)
	
	public static String[] articleName = new String[]{Translate.银铲子,Translate.金铲子,Translate.琉璃铲子};
	public static String[] articleName2 = new String[]{Translate.白银铲子,Translate.黄金铲子,Translate.彩虹琉璃铲};		//替代物
	
	public static final byte yinChanZi = 0;
	public static final byte jinChanZi = 1;
	public static final byte liuliChanZi = 2;
	public static final String key = "default";
	public static String privateKey = "";
	
	public static boolean 挖坟客户端容错开关  = true;
	
	public Object lock = new Object();		//用于挖取全服共有物品时的锁
	
	public static void main(String[] args) throws Exception {
		WaFenManager m = new WaFenManager();
		m.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//activity//wafenActivity.xls");
		m.initFile();
	}

	public void init() throws Exception {
		
		instance = this;
		this.initFile();
		this.initDisk();
		ServiceStartRecord.startLog(this);
	}
	/**
	 * 验证
	 * @param player
	 * @param fenmuName
	 * @param fendiIndex
	 * @param costType
	 * @param fmm
	 * @param wfip
	 * @return
	 */
	public String verification4Private(String fenmuName, int fendiIndex, byte costType, FenMuModel fmm, WaFenInstance4Private wfip) {
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.wafenActivity);
		if (!cr.getBooleanValue()) {
			return Translate.活动未开启;
		}
		List<FenDiModel> list = wfip.getOpenFendiMaps().get(fenmuName);
		if (list != null && list.size() > 0) {
			for (FenDiModel fdm : list) {
				if (fdm.getIndex() == fendiIndex) {
					return Translate.已经挖过此坟坑;
				}
			}
		}
		if (!wfip.canDig(fenmuName)) {
			return Translate.前一个坟墓没挖完;
		}
		int times = wfip.getDigTimes(fenmuName);
		List<Integer[]> cost = fmm.getCostByTimes(times);
		boolean flag = false;
		if (costType >= 0) {
			for (int i=0; i< cost.get(0).length; i++) {
				if (costType == cost.get(0)[i]) {
					flag = true;
					if (wfip.getChanziNum(costType) < cost.get(1)[i]) {
						return Translate.铲子不足;
					}
					break;
				}
			}
			if (!flag) {
				return Translate.铲子类型错误;
			}
		}
		if (fendiIndex >= fmm.getTotalNum()) {
			return Translate.坟地索引不对;
		}
		return null;
	}
	public String verification4Common(String fenmuName, int fendiIndex, byte costType, FenMuModel fmm, WaFenInstance4Common wfip, WaFenInstance4Private wfip2) {
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.wafenActivity);
		if (!cr.getBooleanValue()) {
			return Translate.活动未开启;
		}
		List<FenDiModel> list = wfip.getFendiList();
		if (list != null && list.size() > 0) {
			for (FenDiModel fdm : list) {
				if (fdm.getIndex() == fendiIndex) {
					return Translate.已经挖过此坟坑;
				}
			}
		}
		if (!wfip2.canDig(fenmuName)) {
			return Translate.前一个坟墓没挖完;
		}
		int times = wfip.getTimes();
		List<Integer[]> cost = fmm.getCostByTimes(times);
		if (costType >= 0) {
			boolean flag = false;
			for (int i=0; i< cost.get(0).length; i++) {
				if (costType == cost.get(0)[i]) {
					flag = true;
					if (wfip2.getChanziNum(costType) < cost.get(1)[i]) {
						return Translate.铲子不足;
					}
					break;
				}
			}
			if (!flag) {
				return Translate.铲子类型错误;
			}
		}
		if (fendiIndex >= fmm.getTotalNum()) {
			return Translate.坟地索引不对;
		}
		return null;
	}
	/**
	 * 兑换铲子
	 * @param player
	 * @param confirm
	 */
	public int exchangeChanZi(Player player, byte exchangeType, boolean confirm) {
		if (exchangeType > 2 || exchangeType < 0) {
			return 0;
		}
		synchronized (player) {
			WaFenInstance4Private wfip = privateMaps.get(player.getId());
			if (wfip == null) {
				wfip = new WaFenInstance4Private();
				wfip.setPlayerId(player.getId());
			}
			Knapsack bag = player.getKnapsack_common();
			int count = bag.countArticle(articleName[exchangeType]);
			int count2 = bag.countArticle(articleName2[exchangeType]);
			if (count <= 0 && count2 <= 0) {
				player.sendError(String.format(Translate.背包中没有物品, articleName[exchangeType], articleName2[exchangeType]));
				return 0;
			}
			if (!confirm) {
				//弹窗
				popConfirmWindow1(player,exchangeType, count, articleName[exchangeType], count2, articleName2[exchangeType]);
				return 0;
			}
			for (int i=0; i<count; i++) {
				if (!player.removeArticle(articleName[exchangeType], "兑换铲子")) {
					player.sendError(Translate.删除物品不成功);
					logger.error("[挖坟活动] [兑换铲子] [已经兑换数量:" + (i) + "] [物品名:"+articleName[exchangeType]+"] [" + player.getLogString() + "]");
					return 0;
				}
			}
			for (int i=0; i<count2; i++) {
				if (!player.removeArticle(articleName2[exchangeType], "兑换铲子")) {
					player.sendError(Translate.删除物品不成功);
					logger.error("[挖坟活动] [兑换铲子] [已经兑换数量:" + (i) + "] [物品名:"+articleName2[exchangeType]+"] [" + player.getLogString() + "]");
					return 0;
				}
			}
			int total = 0;
			if (count > 0) {
				total += count;
			} 
			if (count2 > 0) {
				total += count2;
			}
			wfip.saveChanZi(exchangeType, total);
			privateMaps.put(player.getId(), wfip);
			logger.warn("[挖坟活动] [兑换铲子] [成功] [兑换类型:" + exchangeType + "] [剩余数量:"+wfip.getChanziNum(exchangeType)+"] [消耗物品:" + articleName[exchangeType] + "] [消耗数量:" + count + "] [消耗物品:" + articleName2[exchangeType] + "] [消耗数量:" + count2 + "] [" + player.getLogString() + "]");
			disk.put(privateKey, (Serializable) privateMaps);
			player.sendError(Translate.兑换成功);
			return wfip.getChanziNum(exchangeType);
		}
	}
	
	/**
	 * 一键领取奖励
	 * @param player
	 * @param fenmuName
	 * @return
	 */
	public boolean receiveRewards(Player player, String fenmuName) {
		FenMuModel fmm = fenmuMap.get(fenmuName);
		if (fmm == null) {
			logger.error("[挖坟活动] [没有对应配置的坟墓] [" + player.getLogString() + "] [fenmuName:" + fenmuName + "]"); 
			return false;
		}
		if (fmm.getShareType() != 0) {
			player.sendError(Translate.没有可领取的奖励);
			return false;
		}
		synchronized (player) {
			WaFenInstance4Private wfip = privateMaps.get(player.getId());
			if (wfip == null) {
				player.sendError(Translate.没有可领取的奖励);
				return false;
			}
			try {
				List<FenDiModel> list = wfip.getOpenFendiMaps().get(fenmuName);
				List<FenMuArticleModel> articleList = new ArrayList<FenMuArticleModel>();
				if (list != null && list.size() > 0) {
					for (FenDiModel fdm : list) {
						if (fdm.getReciveType() == 1) {
							fdm.setReciveType((byte) 2);
							FenMuArticleModel fmam = fmm.getArticleMap().get(fdm.getArticleId());
							fmam.bind = fdm.isBind();
							articleList.add(fmam);
						}
					}
				}
				if (articleList.size() <= 0) {
					player.sendError(Translate.没有可领取的奖励);
					return false;
				}
				wfip.getOpenFendiMaps().put(fenmuName, list);
				privateMaps.put(player.getId(), wfip);
				disk.put(privateKey, (Serializable) privateMaps);
				ActivityProp[] props = new ActivityProp[articleList.size()];
				for (int i=0; i<articleList.size(); i++) {
					props[i] = new ActivityProp(articleList.get(i).getArticleCNNName(), articleList.get(i).getColorType(), articleList.get(i).getNum(), articleList.get(i).bind);
				}
				List<Player> players = new ArrayList<Player>();
				players.add(player);
				ActivityManager.sendMailForActivity(players, props,Translate.系统, Translate.挖坟活动, "挖坟活动");
//				for (FenMuArticleModel fmam : articleList) {
//					Article a = ArticleManager.getInstance().getArticleByCNname(fmam.getArticleCNNName());
//					ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, fmam.bind, ArticleEntityManager.挖坟活动获得, player, 
//							fmam.getColorType(), 1, true);
//					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { fmam.getNum() }, Translate.系统, Translate.挖坟活动, 0L, 0L, 0L, "挖坟活动");
//					logger.warn("[挖坟活动] [领取奖励成功] [" + player.getLogString() + "] [物品信息:" + ae.getId() + " " + ae.getArticleName() + "]"); 	
//				}
				player.sendError(Translate.领取成功);
				return true;
			} catch (Exception e) {
				logger.error("[挖坟活动] [一键领取奖励] [异常] [" + player.getLogString() + "] [fenmuName:" + fenmuName + "]", e);
			}
		}
		return false;
	}
	
	public int[] wafen4Ten(Player player, String fenmuName, boolean confirm) {
		FenMuModel fmm = fenmuMap.get(fenmuName);
		if (fmm == null) {
			logger.error("[挖坟活动] [没有对应配置的坟墓] [" + player.getLogString() + "] [fenmuName:" + fenmuName + "]"); 
			return null;
		}
		if (fmm.getShareType() != 0) {
			player.sendError(Translate.全服共有坟墓不可十连挖);
			return null;
		}
		synchronized (player) {
			WaFenInstance4Private wfip = privateMaps.get(player.getId());
			if (wfip != null) {
				int costNum = 1;
				int times = wfip.getDigTimes(fenmuName);
				List<Integer[]> cost = fmm.getCostByTimes(times);
				if (cost == null) {
					player.sendError(Translate.本章已挖完);
					return null;
				}
				for (int i=0; i< cost.get(0).length; i++) {
					if (jinChanZi == cost.get(0)[i]) {
						costNum = cost.get(1)[i];
						break;
					}
				}
				if (!wfip.canDig(fenmuName)) {
					player.sendError(Translate.前一个坟墓没挖完);
					return null;
				}
				if (wfip.getLeftJinChanzi() < (costNum * 10)) {
					
					String str = Translate.translateString(Translate.十连挖需要金铲子数量, new String[][] { { Translate.COUNT_1, (costNum * 10)+"" }, {Translate.STRING_1, Translate.金铲子}});
//					String str = String.format(Translate.十连挖需要金铲子数量, (costNum * 10));
					player.sendError(Translate.铲子不足 + str);
					return null;
				}
				if (!confirm) {
					popConfirmWindow4(player, fenmuName);
					return null;
				}
				List<Integer> rl = wfip.getFendiIndex4Ten(fenmuName, player);
				if (rl != null) {
					int[] result = new int[rl.size()];
					for (int i=0; i<rl.size(); i++) {
						waFen(player, fenmuName, rl.get(i), (byte)1);
						result[i] = rl.get(i);
					}
					player.sendError(Translate.十连挖成功);
					return result;
				}
			} else {
				player.sendError(Translate.请先兑换铲子);
			}
		}
		return null;
	}
	
	public static String[] fenmuColor = new String[]{"0xff0000ff", "0xffE706EA", "0xffFDA700", "0xCDCD00"};
	public static String[] aColor = new String[]{"0xff00ff00", "0xff0000ff", "0xffE706EA", "0xffFDA700"};
	
	/**
	 * 挖坟
	 * @param player
	 * @param fenmuId
	 * @param fendiIndex
	 * @param costType   消耗类型(-1代表需要弹窗确认)
	 * @return   获取物品的临时id
 	 */
	public long waFen(Player player, String fenmuName, int fendiIndex, byte costType) {
		FenMuModel fmm = fenmuMap.get(fenmuName);
		if (fmm == null) {
			logger.error("[挖坟活动] [没有对应配置的坟墓] [" + player.getLogString() + "] [fenmuName:" + fenmuName + "]"); 
			return -1;
		}
		
		boolean bind = costType == yinChanZi;				//只有银铲子是绑定的
		if (fmm.getShareType() == 0) {				//私有坟墓	
			synchronized (player) {
				WaFenInstance4Private wfip = privateMaps.get(player.getId());
				if (wfip == null) {
					player.sendError(Translate.请先兑换铲子);
					return -1;
				}
				String result = this.verification4Private(fenmuName, fendiIndex, costType, fmm, wfip);
				if (result != null) {
					player.sendError(result);
					return -1;
				} 
				if (costType < 0) {
					//弹窗二次确认
					popConfirmWindow2(player, fenmuName, fendiIndex);
					return -1;
				}
				int times = wfip.getDigTimes(fenmuName);
				List<FenDiModel> opened = wfip.getOpenFendiMaps().get(fenmuName);
				if (opened == null) {
					opened = new ArrayList<FenDiModel>();
				}
				FenMuArticleModel resultId = fmm.getResultByType(player, costType, times, opened);
				if (resultId != null) {
					List<Integer[]> cost = fmm.getCostByTimes(times);
					boolean flag = false;
					for (int i=0; i< cost.get(0).length; i++) {
						if (costType == cost.get(0)[i]) {
							if (wfip.expenseChanZi(costType, cost.get(1)[i])) {
								flag = true;
							}
							break;
						}
					}
					if (flag) {
						FenDiModel fdm = new FenDiModel();
						fdm.setArticleId(resultId.getId());
						fdm.setIndex(fendiIndex);
						fdm.setBind(bind);
						fdm.setReciveType((byte) 1);		//标记为未领取
						opened.add(fdm);
						wfip.getOpenFendiMaps().put(fenmuName, opened);
						wfip.setTimes(wfip.getTimes() + 1);
						privateMaps.put(player.getId(), wfip);
						disk.put(privateKey, (Serializable) privateMaps);
						try {
							ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(resultId.getTempArticleEntityId());
							String fC = "0xff0000ff";
							if (fmm.getId() > 0 && fmm.getId() <= fenmuColor.length) {
								fC = fenmuColor[fmm.getId() -1];
							}
							String articleColor = "0xffffffff";
							if (resultId.getColorType() > 0 && resultId.getColorType() <= aColor.length) {
								articleColor = aColor[resultId.getColorType() - 1];
							}
							if (fmm.getWaFenArticleId() != null && fmm.getWaFenArticleId().contains(resultId.getId())) {
								WaFenManager.sendWordMsg(String.format(Translate.挖坟世界公告, player.getName(), fC, fmm.getName(), articleColor, ae.getArticleName()));
							} else if (resultId.getRadioType() != 0) {
								WaFenManager.sendWordMsg(String.format(Translate.挖坟世界公告, player.getName(), fC, fmm.getName(), articleColor, ae.getArticleName()));
							}
						} catch (Exception e) {
							logger.error("[挖坟活动] [挖到必产物品世界公告] [异常] [" + player.getLogString() + "] ", e );
						}
						logger.warn("[挖坟活动] [成功] [" + player.getLogString() + "] [fenmuName:" + fenmuName + "] [fendiIndex:" + fendiIndex + "] [resultId :" + resultId + "]");
						return resultId.getTempArticleEntityId();
					}
				} else {
					logger.warn("[挖坟活动] [私有坟墓] [没有获取到可以得到的物品] [" + player.getLogString() + "] [fenmuName:" + fenmuName + "] [fendiIndex:" + fendiIndex + "] [times :" + times + "]");
				}
			}
		} else if (fmm.getShareType() == 1) {								//共有坟墓
			synchronized (lock) {				
				WaFenInstance4Private wfip = privateMaps.get(player.getId());
				WaFenInstance4Common common = commonMaps.get(fenmuName);
				if (common == null) {
					common = new WaFenInstance4Common();
				}
				if (wfip == null) {
					player.sendError(Translate.请先兑换铲子);
					return -1;
				}
				String result = this.verification4Common(fenmuName, fendiIndex, costType, fmm, common, wfip);
				if (result != null) {
					player.sendError(result);
					return -1;
				}
				if (costType < 0) {
					//弹窗二次确认
					popConfirmWindow3(player, fenmuName, fendiIndex);
					return -1;
				}
				
				int times = wfip.getDigTimes(fenmuName);
				List<FenDiModel> cList = common.getFendiList();
				if (cList == null) {
					cList = new ArrayList<FenDiModel>();
				}
				FenMuArticleModel resultId = fmm.getResultByType(player, costType, times, cList);
				if (resultId != null) {
					List<Integer[]> cost = fmm.getCostByTimes(times);
					boolean flag = false;
					for (int i=0; i< cost.get(0).length; i++) {
						if (costType == cost.get(0)[i]) {
							if (wfip.expenseChanZi(costType, cost.get(1)[i])) {
								flag = true;
							}
							break;
						}
					}
					if (flag) {
						FenDiModel fdm = new FenDiModel();
						fdm.setArticleId(resultId.getId());
						fdm.setIndex(fendiIndex);
						fdm.setReciveType((byte) 2);		//标记为已领取
						fdm.setBind(bind);
						cList.add(fdm);
						common.setFendiList(cList);
						common.setTimes(common.getTimes() + 1);
						privateMaps.put(player.getId(), wfip);
						disk.put(privateKey, (Serializable) privateMaps);
						commonMaps.put(fenmuName, common);
						disk.put(key, (Serializable) commonMaps);
						Article a = ArticleManager.getInstance().getArticleByCNname(resultId.getArticleCNNName());
						try {
							ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, bind, ArticleEntityManager.挖坟活动获得, player, 
									 resultId.getColorType(), 1, true);
							this.put2Bag(player, ae, resultId.getNum());
							try {
								if (fmm.getWaFenArticleId() != null && fmm.getWaFenArticleId().contains(resultId.getId())) {
									WaFenManager.sendWordMsg(String.format(Translate.挖坟世界公告, player.getName(), fmm.getName(), ae.getArticleName()));
								}
							} catch (Exception e) {
								logger.error("[挖坟活动] [挖到必产物品世界公告] [异常] [" + player.getLogString() + "] ", e );
							}
							logger.warn("[挖坟活动]  [共有坟墓] [成功] [" + player.getLogString() + "] [fenmuName:" + fenmuName + "] [fendiIndex:" + fendiIndex + "] [resultId :" + resultId + "]");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error("[挖坟活动] [共有坟墓] [异常] [" + player.getLogString() + "] [获取的物品:" + resultId + "]", e);
						}
						return resultId.getTempArticleEntityId();
					}
				} else {
					logger.warn("[挖坟活动] [没有获取到可以得到的物品] [" + player.getLogString() + "] [fenmuName:" + fenmuName + "] [fendiIndex:" + fendiIndex + "] [times :" + times + "]");
				}
			}
		}
		return -1;
	}
	
	private void popConfirmWindow4(Player p, String fenmuName) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_ConfirmWafen4Ten option1 = new Option_ConfirmWafen4Ten();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setFenmuName(fenmuName);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = String.format(Translate.十连挖二次确认, Translate.金铲子);
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	private void popConfirmWindow1(Player p, byte exchangeType, int costNum, String articleName, int costNum2, String articleName2) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_ConfirmExchange option1 = new Option_ConfirmExchange();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setExchangeType(exchangeType);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = Translate.translateString(Translate.确认兑换铲子, new String[][] { { Translate.COUNT_1, costNum+"" }, { Translate.STRING_1, articleName},{ Translate.COUNT_2, costNum2+""},{Translate.STRING_2, articleName2} });
//		String msg = String.format(Translate.确认兑换铲子, costNum, articleName, costNum2, articleName2);
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	
	private void popConfirmWindow3(Player p, String fenmuName, int fendiIndex) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_UseChanziType option1 = new Option_UseChanziType();
		option1.setText(Translate.text_62);
		option1.setChanziType(liuliChanZi);
		option1.setFenmuName(fenmuName);
		option1.setFendiIndex(fendiIndex);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(Translate.text_364);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = Translate.需要消耗琉璃铲子;
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	private void popConfirmWindow2(Player p, String fenmuName, int fendiIndex) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_UseChanziType option1 = new Option_UseChanziType();
		option1.setText(String.format(Translate.使用银铲子, Translate.银铲子));
		option1.setChanziType((byte)0);
		option1.setFenmuName(fenmuName);
		option1.setFendiIndex(fendiIndex);
		Option_UseChanziType option2 = new Option_UseChanziType();
		option2.setText(String.format(Translate.使用金铲子, Translate.金铲子));
		option2.setChanziType((byte)1);
		option2.setFenmuName(fenmuName);
		option2.setFendiIndex(fendiIndex);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = Translate.使用哪种铲子;
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	
	public void put2Bag(Player p, ArticleEntity ae, int num) {
		if(!p.putToKnapsacks(ae, num, "挖坟活动")) {
			p.sendError(Translate.背包空间不足);
			if(logger.isInfoEnabled()) {
				logger.info("[挖坟活动] [背包空间不足发邮件] [" + p.getLogString() + "]");
			}
			try{
				MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[] { ae }, new int[] { num }, Translate.系统, Translate.副本转盘, 0L, 0L, 0L, "副本结束转盘");
			} catch(Exception e) {
				logger.error("[挖坟活动] [挖坟活动发奖异常] [" + p.getLogString() + "][物品名:" + ae.getArticleName() + "&&颜色:" + ae.getColorType() + "&&是否绑定" + ae.isBinded() +"]");
			}
		}
	}

	/**
	 * 初始化discache
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	private void initDisk() throws IOException {
		List<BaseActivityInstance> list = AllActivityManager.instance.allActivityMap.get(AllActivityManager.wafenActivity);
		String fileKey = null;
		long openTime = 0;
		long now = System.currentTimeMillis();
		if (list != null && list.size() > 0) {
			for (BaseActivityInstance bi : list) {
				if (bi.getServerfit().thiserverFit() && bi.getEndTime() >= now) {
					fileKey = bi.getEndTime() + "";
					openTime = bi.getStartTime();
					break;
				}
			}
		}

		if (fileKey != null) {
			String df = diskFile + fileKey + ".ddc";
			File file = new File(df);
			if (!file.exists()) {
				file.createNewFile();
			}
			disk = new DefaultDiskCache(file, null, "fairybuddha", 100L * 365 * 24 * 3600 * 1000L);
			privateKey = fileKey;
			privateMaps = (Map<Long, WaFenInstance4Private>) disk.get(fileKey);
			if (privateMaps == null) {
				logger.warn("[挖坟活动] [初始化privateMaps] [失败] [disk中没有读取到信息] [活动开始时间:" + (new Timestamp(openTime) + "]"));
				privateMaps = new Hashtable<Long, WaFenInstance4Private>();
			}
			commonMaps = (Map<String, WaFenInstance4Common>) disk.get(key);
			if (commonMaps == null) {
				logger.warn("[挖坟活动] [初始化commonMaps] [失败] [disk中没有读取到信息] [活动开始时间:" + (new Timestamp(openTime) + "]"));
				commonMaps = new Hashtable<String, WaFenInstance4Common>();
			}
			logger.warn("[挖坟活动] [初始化disk] [成功] [fileName:" + df + "] [活动结束时间:" + (new Timestamp(Long.parseLong(fileKey))) + "]");
		} else {
			logger.warn("[挖坟活动] [初始化disk] [失败] [配表中没有开启或即将开启的挖坟活动] [" + list + "]");
		}
	}
	
	
	public static void sendWordMsg(String msg, byte type){
		ChatMessage msgA = new ChatMessage();
		msgA.setMessageText(msg);
		try {
			if (type != 1) {
				ChatMessageService.getInstance().sendMessageToSystem(msgA);
			}
			ChatMessageService.getInstance().sendHintMessageToSystem(msgA);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ChatMessageService.logger.error("[世界公告] [异常] ", e);
		}
	}
	public static void sendWordMsg(String msg){
		sendWordMsg(msg, (byte)-1);
	}

	/**
	 * 初始化配表
	 * @throws Exception
	 */
	private void initFile() throws Exception {
		File f = new File(fileName);
		f = new File(ConfigServiceManager.getInstance().getFilePath(f));
		if(!f.exists()){
			throw new Exception(fileName + " 配表不存在! " + f.getAbsolutePath());
		}
		List<String> tempList = new ArrayList<String>();
		List<String> tempList2 = new ArrayList<String>();
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		int totalSheetNum = workbook.getNumberOfSheets();
		HSSFSheet sheet = workbook.getSheetAt(0); // 挖坟活动基础配置1
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				int index = 0;
				String startTime = ReadFileTool.getString(row, index++, logger);
				String endTime = ReadFileTool.getString(row, index++, logger);
				String platForms = ReadFileTool.getString(row, index++, logger);
				String openServerNames = ReadFileTool.getString(row, index++, logger);
				String notOpenServers = ReadFileTool.getString(row, index++, logger);
				WaFenActivity activity = new WaFenActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
				AllActivityManager.instance.add2AllActMap(AllActivityManager.wafenActivity, activity);
			} catch (Exception e) {
				throw new Exception("[" + fileName + "] [" + sheet.getSheetName() + "] [" + "第" + (i + 1) + "行异常！！]", e);
			}
		}

		sheet = workbook.getSheetAt(1); // 挖坟活动基础配置2
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				int index = 0;
				FenMuModel fmm = new FenMuModel();
				fmm.setId(ReadFileTool.getInt(row, index++, logger));
				fmm.setName(ReadFileTool.getString(row, index++, logger));
				fmm.setShareType(ReadFileTool.getByte(row, index++, logger));
				fmm.setTotalNum(ReadFileTool.getInt(row, index++, logger));
				fmm.setWidth(ReadFileTool.getInt(row, index++, logger));
				fmm.setHeight(ReadFileTool.getInt(row, index++, logger));
				fmm.setOpenTimes4Cost(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				String costTypes = ReadFileTool.getString(row, index++, logger);
				String costNums = ReadFileTool.getString(row, index++, logger);
				String[] temp = costTypes.split("\\|");
				String[] temp2 = costNums.split("\\|");
				List<Integer[]> costType = new ArrayList<Integer[]>();
				List<Integer[]> costNum = new ArrayList<Integer[]>();
				for (int ii=0; ii<temp.length; ii++) {
					String[] tt = temp[ii].split(",");
					Integer[] ig = new Integer[tt.length];
					for (int iii=0; iii<tt.length; iii++) {
						ig[iii] = Integer.parseInt(tt[iii]);
					}
					costType.add(ig);
				}
				for (int ii=0; ii<temp2.length; ii++) {
					String[] tt = temp2[ii].split(",");
					Integer[] ig = new Integer[tt.length];
					for (int iii=0; iii<tt.length; iii++) {
						ig[iii] = Integer.parseInt(tt[iii].split("\\.")[0]);
					}
					costNum.add(ig);
				}
				fmm.setCostType(costType);
				fmm.setCostNum(costNum);
				List<Integer> mList = new ArrayList<Integer>();
				int[] articles = ReadFileTool.getIntArrByString(row, index++, logger, ",");
				for (int ii=0; ii<articles.length; ii++) {
					mList.add(articles[ii]);
				}
				fmm.setWaFenArticleId(mList);
				if (fmm.getOpenTimes4Cost().length != fmm.getCostType().size() || fmm.getOpenTimes4Cost().length != fmm.getCostNum().size()) {
					throw new Exception("[次数对应格子消耗类型或者数量不一致]");
				}
				fmm.setHelpInfo(ReadFileTool.getString(row, index++, logger));
				fenmuMap.put(fmm.getName(), fmm);
			} catch (Exception e) {
				throw new Exception("[" + fileName + "] [" + sheet.getSheetName() + "] [" + "第" + (i + 1) + "行异常！！]", e);
			}
		}

		for (int i = 2; i < totalSheetNum; i++) { // 循环读取坟墓
			sheet = workbook.getSheetAt(i);
			String fenmuName = sheet.getSheetName();
			FenMuModel fmm = fenmuMap.get(fenmuName);
			rows = sheet.getPhysicalNumberOfRows();
			if (fmm == null) {
				throw new Exception("[坟墓名配置错误] [" + fenmuName + "]");
			}
			List<Integer[]> cc = fmm.getCostType();
			List<Integer> tl = new ArrayList<Integer>();
			LinkedHashMap<Integer, FenMuArticleModel> articleMap = new LinkedHashMap<Integer, FenMuArticleModel>();
			for (Integer[] kk : cc ) {
				for (Integer ig : kk) {
					if (!tl.contains(ig)) {
						tl.add(ig);
					}
				}
			}
			for(int j=1; j<rows; j++) {
				HSSFRow row = sheet.getRow(j);
				if(row == null){
					continue;
				}
				try{
					int index = 0;
					FenMuArticleModel famm = new FenMuArticleModel();
					famm.setId(ReadFileTool.getInt(row, index++, logger));
					famm.setArticleCNNName(ReadFileTool.getString(row, index++, logger));
					famm.setColorType(ReadFileTool.getInt(row, index++, logger));
					famm.setNum(ReadFileTool.getInt(row, index++, logger));
					famm.setMaxNum(ReadFileTool.getInt(row, index++, logger));
					famm.setOpenTimes4Probabbly(ReadFileTool.getIntArrByString(row, index, logger, ","));
					index++;
					if (tl.contains(0)) {
						famm.setProbabbly4Yin(ReadFileTool.getIntArrByString(row, index, logger, ","));
						if (famm.getOpenTimes4Probabbly().length != famm.getProbabbly4Yin().length) {
							throw new Exception("[次数与银铲子概率不匹配]");
						}
					}
					index++;
					if (tl.contains(1)) {
						famm.setProbabbly4Jin(ReadFileTool.getIntArrByString(row, index, logger, ","));
						if (famm.getOpenTimes4Probabbly().length != famm.getProbabbly4Yin().length) {
							throw new Exception("[次数与金铲子概率不匹配]");
						}
					}
					index++;
					if (tl.contains(2)) {
						famm.setProbabbly4LiuLi(ReadFileTool.getIntArrByString(row, index, logger, ","));
						if (famm.getOpenTimes4Probabbly().length != famm.getProbabbly4LiuLi().length) {
							throw new Exception("[次数与琉璃铲子概率不匹配]");
						}
					}
					index++;
					famm.setRadioType(ReadFileTool.getInt(row, index, logger));
					Article a = ArticleManager.getInstance().getArticleByCNname(famm.getArticleCNNName());
					if (a != null) {
						ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, famm.bind, ArticleEntityManager.活动奖励临时物品, 
								null, famm.getColorType());
						famm.setTempArticleEntityId(ae.getId());
						articleMap.put(famm.getId(), famm);
					} else  {
						tempList.add(famm.getArticleCNNName());
					}
//					System.out.println(famm.getId());
				}catch(Exception e) {
					throw new Exception(fileName + "第" + (j+1) + "行异常！！[" + sheet.getSheetName() + "]", e);
				}
			}
			fenmuMap.get(fenmuName).setArticleMap(articleMap);
			for (Integer tempId : fenmuMap.get(fenmuName).getWaFenArticleId()) {
				if (!fenmuMap.get(fenmuName).getArticleMap().containsKey(tempId)) {
//					if (tempList.size() > 0) {
//						throw new Exception("[配置物品不存在] [物品统计名:" + tempList + "]");
//					}
					tempList2.add(fenmuName + "_" + tempId);
//					throw new Exception("["+fenmuName+"] [必产物品id没有在对应坟墓下配置] [" + tempId + "]");
				}
			}
//			System.out.println(fenmuMap.get(fenmuName));
		}
		if (tempList.size() > 0) {
			throw new Exception("[配置物品不存在] [物品统计名:" + tempList + "]");
		}
//		if (tempList2.size() > 0) {
//			throw new Exception("[必产物品id没有在对应坟墓下配置] [" + tempList2 + "]");
//		}
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDiskFile() {
		return diskFile;
	}

	public void setDiskFile(String diskFile) {
		this.diskFile = diskFile;
	}
}
