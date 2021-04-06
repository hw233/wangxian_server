package com.fy.engineserver.tengxun;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.tengXun.TengXunActivityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.TX_GAMELEVEL_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.boss.client.BossClientService;
import com.fy.boss.platform.qq.QQUserInfo;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class TengXunDataManager implements Runnable {
	
	private PlatformManager platformManager;
	
	public static boolean isTengXunServer = PlatformManager.getInstance().platformOf(Platform.腾讯);
	
	public static int SHOP_MOZUAN_AGIO = 80;				//百分之
	public static int SHOP_NO_AGIO = 100;					//百分之
	
	public static String agioPng = "ui/gem1.png";
	
	public static Logger logger = LoggerFactory.getLogger(TengXunDataManager.class);
	
	public static boolean isRunable = true;
	public static long runTime = 1000L;
	
	public static TengXunDataManager instance;

	private HashMap<Long, TengXunData> txDatas = new HashMap<Long, TengXunData>();
	
	public static long txLoginInfoClean = 1000L * 60 * 60 * 5;			//info移除时间
	public HashMap<String, TengXunLoginInfo> txLoginInfos = new HashMap<String, TengXunLoginInfo>();

	public String WEEK_LIBAO_NAME = Translate.蓝钻会员VIP礼包;			//每周礼包
	public static String WEEK_LIBAO_SETS = "礼包领取数据";
	int week = 0;
	public HashSet<Long> week_libao_sets = new HashSet<Long>();
	
	private String diskCacthPath;
	public DiskCache txDiskCatch;				//关于腾讯的一些礼包和活动信息基本都存里面
	
	
	public void init() {
		
		instance = this;
		if (!isTengXunServer){
			return;
		}
		
		EnterLimitManager.isCompareRSA = false;
		
		txDiskCatch = new DefaultDiskCache(new File(getDiskCacthPath()), "腾讯相关diskcatch", 1000L * 60 * 60 * 24 * 7);
		
		TengXunActivityManager.instance.init();
		
		Calendar calendar = Calendar.getInstance();
		week = calendar.get(Calendar.WEEK_OF_YEAR);
		
		week_libao_sets = (HashSet<Long>) txDiskCatch.get(WEEK_LIBAO_SETS + week);
		if (week_libao_sets == null) {
			week_libao_sets = new HashSet<Long>();
			txDiskCatch.put(WEEK_LIBAO_SETS + week, week_libao_sets);
		}
		
		
		new Thread(this).start();
		
		System.out.println("腾讯系统初始化完成");
		ServiceStartRecord.startLog(this);
	}
	
	public void destroy() {
		
	}
	
	@Override
	public void run() {
		while(isRunable) {
			try{
				long now = System.currentTimeMillis();
				try{
					for (Iterator<String> it = txLoginInfos.keySet().iterator(); it.hasNext();) {
						String key = it.next();
						TengXunLoginInfo info = txLoginInfos.get(key);
						if (now - info.lastGetTimne > txLoginInfoClean) {
							it.remove();
						}
					}
				}catch(Exception e) {
					logger.error("腾讯魔钻run   移除info出错", e);
				}
				
				if (TengXunActivityManager.instance != null) {
					TengXunActivityManager.instance.heatBeat();
				}
				
				long endTime = System.currentTimeMillis();
				if (endTime - now >= runTime) {
				}else {
					try{
						Thread.sleep(runTime - endTime + now);
					}catch(Exception e) {
						logger.error("腾讯魔钻run   sleep出错", e);
					}
				}
			}catch(Exception e) {
				logger.error("腾讯魔钻run出错", e);
			}
		}
	}
	
	/**
	 * 领取礼包
	 * @param player
	 */
	public void opt_getWeekLiBao(Player player) {
		Calendar calendar = Calendar.getInstance();
		int nowweek = calendar.get(Calendar.WEEK_OF_YEAR);
		if (week != nowweek) {
			week_libao_sets.clear();
			week = nowweek;
			txDiskCatch.put(WEEK_LIBAO_SETS + week, week_libao_sets);
		}
		if (!isGameLevel(player.getId())) {
			player.sendError("您还未开通蓝钻业务，不能领取此礼包。");
			return;
		}
		if (week_libao_sets.contains(player.getId())) {
			player.sendError("您已经领取过此礼包，不能再领取。");
			return;
		}
		Article a = ArticleManager.getInstance().getArticle(WEEK_LIBAO_NAME);
		if (a != null) {
			try{
				ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.TENGXUN_WEEK_LIBAO, null, a.getColorType(), 1, true);
				MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, "蓝钻用户领取礼包", "蓝钻用户领取礼包", 0, 0, 0, "腾讯蓝钻每周礼包");
				week_libao_sets.add(player.getId());
				txDiskCatch.put(WEEK_LIBAO_SETS + week, week_libao_sets);
				
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(200);
				mw.setDescriptionInUUB("玩游戏享特权，拿礼包遨仙界！欢迎您来到宏大奇幻的《飘渺寻仙曲》修真世界。作为一位尊贵的蓝钻用户，您将享受到飘渺寻仙曲为您提供的超级礼包特权，每周均可免费领取蓝钻游戏礼包一份，畅玩飘渺寻仙曲，自在无边，！（您的礼包将通过邮件的形势为您发放，请您注意查收邮箱）");
				CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), new Option[0]);
				player.addMessageToRightBag(req);
				
				logger.warn("[魔钻用户领取礼包] [{}] [{}] [{}]", new Object[]{player.getLogString(), entity.getId(), entity.getArticleName()});
			}catch(Exception e) {
				logger.error("发送邮件失败", e);
				logger.error("[每周礼包发送邮件失败] [{}] [{}] [{}]", new Object[]{player.getLogString(), WEEK_LIBAO_NAME});
			}
		}else {
			logger.error("[每周礼包物品不存在] [{}] [{}]", new Object[]{player.getLogString(), WEEK_LIBAO_NAME});
		}
	}
	
	public void putTXLoginInfo(String userName, String password) {
		if (!isTengXunServer) {
			return;
		}
		TengXunLoginInfo info = new TengXunLoginInfo();
		info.userName = userName;
		info.password = password;
		logger.warn("[用户登录] [{}] [{}]", new Object[]{userName, password});
		info.lastGetTimne = System.currentTimeMillis();
		txLoginInfos.put(userName, info);
	}
	
	public TengXunLoginInfo getTengXunLoginInfo(Player player) {
		TengXunLoginInfo info = txLoginInfos.get(player.getUsername());
		if (info != null) {
			info.lastGetTimne = System.currentTimeMillis();
			return info;
		}
		return null;
	}
	
	/**
	 * 返回的是 100的比例的
	 * @param player
	 * @return
	 */
	public int getShopAgio(Player player) {
		if (isGameLevel(player.getId())) {
			return SHOP_MOZUAN_AGIO;
		}else {
			return SHOP_NO_AGIO;
		}
	}
	
	/**
	 * 给具体购买物品的地方用
	 * @param player
	 * @return
	 */
	public float getShopAgio2Buy(Player player) {
		if (!isTengXunServer) {
			return (float)SHOP_NO_AGIO / 100;
		}
		if (isGameLevel(player.getId())) {
			return (float)SHOP_MOZUAN_AGIO / 100;
		}else {
			return (float)SHOP_NO_AGIO / 100;
		}
	}
	
	public static int ADD_VITALITY = 10;//10;
	public int getAddVitality (Player player) {
		try{
			if (isGameLevel(player.getId())) {
				return ADD_VITALITY;
			}
			return 0;
		}catch(Exception e) {
			logger.error("[得到魔钻多加体力出错]", e);
		}
		return 0;
	}
	
	public static int ADD_KNAPSACK_SIZE = 10;
	public int getAddKnapsackSize(Player player) {
		try{
			if (isGameLevel(player.getId())) {
				return ADD_KNAPSACK_SIZE;
			}
			return 0;
		}catch(Exception e) {
			logger.error("[得到魔钻多开背包出错]", e);
		}
		return 0;
	}
	
	//得到腾讯信息，如果没有就去boss取
	public TengXunData getTengXunData(long pid) {
		boolean isOnline = PlayerManager.getInstance().isOnline(pid);
		if (isOnline) {
			TengXunData data = txDatas.get(pid);
			if (data == null) {
				return null;
			}
			return data;
		}
		return null;
	}
	
	public void send2getTengXunData(Player player) {
		try{
			long startTime = System.currentTimeMillis();
			if (!isTengXunServer){
				return;
			}
			boolean isOnline = PlayerManager.getInstance().isOnline(player.getId());
			if (!isOnline) {
				return;
			}
			TengXunLoginInfo info = getTengXunLoginInfo(player);
			if (info == null) {
				logger.error("[没有info] [{}]", new Object[]{player.getLogString()});
				return;
			}
			String userName = info.userName;
			String password = info.password;
			QQUserInfo qqinfo = BossClientService.getInstance().getQQUserInfo4MoZuan(userName, password, player.getPassport().getLastLoginChannel());
			if (qqinfo != null) {
				TengXunData data = new TengXunData(player.getId());
				data.gamevip = qqinfo.isMozuan();
				data.gamevipLevel = qqinfo.getMozuanDengji();
				txDatas.put(player.getId(), data);
				logger.warn("[取QQ魔钻等级] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), userName, password, data.gamevip, data.gamevipLevel, System.currentTimeMillis() - startTime});
			}else {
				logger.error("[未取到QQ魔钻] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), userName, password, System.currentTimeMillis() - startTime});
			}
		}catch(Exception e) {
			logger.error("取腾讯魔钻信息出错" + player.getId() + "," + player.getUsername() + "," + player.getName(), e);
		}
	}
	
	public boolean isGameLevel(long pid) {
		TengXunData data = getTengXunData(pid);
		if (data != null) {
			return data.gamevip;
		}
		return false;
	}
	
	public int getGameLevel(long pid) {
		TengXunData data = getTengXunData(pid);
		if (data != null) {
			if (data.gamevip) {
				return data.gamevipLevel;
			}
		}
		return -1;
	}
	
	public void sendTengXunMsg(Player player) {
		try{
			if (!isTengXunServer){
				return;
			}
			TX_GAMELEVEL_RES res = new TX_GAMELEVEL_RES(GameMessageFactory.nextSequnceNum(), getGameLevel(player.getId()), getShopAgio(player), agioPng);
			player.addMessageToRightBag(res);
		}catch(Exception e) {
			logger.error("发送腾讯数据出错", e);
		}
	}
	
	public void setTxDatas(HashMap<Long, TengXunData> txDatas) {
		this.txDatas = txDatas;
	}

	public HashMap<Long, TengXunData> getTxDatas() {
		return txDatas;
	}

	public void setDiskCacthPath(String diskCacthPath) {
		this.diskCacthPath = diskCacthPath;
	}

	public String getDiskCacthPath() {
		return diskCacthPath;
	}

	public void setPlatformManager(PlatformManager platformManager) {
		this.platformManager = platformManager;
	}

	public PlatformManager getPlatformManager() {
		return platformManager;
	}

	

}
