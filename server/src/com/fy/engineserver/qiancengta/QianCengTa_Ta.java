package com.fy.engineserver.qiancengta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.authority.Authority;
import com.fy.engineserver.authority.AuthorityAgent;
import com.fy.engineserver.authority.AuthorityConfig;
import com.fy.engineserver.authority.MaxNumAccessedException;
import com.fy.engineserver.authority.TotalNumAccessedException;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_ChongZhi;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.qianCeng.Option_NewQianCengFlush;
import com.fy.engineserver.message.CHANGE_GAME_DISPLAYNAME_REQ;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.ENTER_GAME_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NEWQIANCENGTA_AUTO_PATA_RES;
import com.fy.engineserver.message.NEWQIANCENGTA_OPEN_INFO_REQ;
import com.fy.engineserver.message.NEWQIANCHENGTA_MANUAL_FAIL_RES;
import com.fy.engineserver.message.NEWQIANCHENGTA_MANUAL_MSG_RES;
import com.fy.engineserver.message.NEWQIANCHENGTA_MANUAL_OVER_RES;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.message.QIANCENGTA_OPEN_INFO_REQ;
import com.fy.engineserver.message.QIANCHENGTA_MANUAL_FAIL_RES;
import com.fy.engineserver.message.QIANCHENGTA_MANUAL_MSG_RES;
import com.fy.engineserver.message.QIANCHENGTA_MANUAL_OVER_RES;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.qiancengta.info.QianCengTa_CengInfo;
import com.fy.engineserver.qiancengta.info.QianCengTa_CengMonsterInfo;
import com.fy.engineserver.qiancengta.info.QianCengTa_DaoInfo;
import com.fy.engineserver.qiancengta.info.QianCengTa_TaInfo;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.trade.TradeManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 此对象需要存储
 * 
 * 
 *
 */

@SimpleEntity
public class QianCengTa_Ta implements com.xuanzhi.tools.cache.Cacheable , CacheListener{
	
	private static long AUTO_PA_TA_SPACE = 1*1000;
	
	public static boolean isOpenHard = false;
	public static boolean isOpenGulf = false;

	public String getLogString(int nandu) {
		int maxDao = -1;
		int maxCeng = -1;
		if (nandu == 0) {
			maxDao = getMaxDao();
			maxCeng = getMaxCengInDao();
		}else if (nandu == 1) {
			maxDao = getMaxHardDao();
			maxCeng = getMaxHardInDao();
		}else if (nandu == 2) {
			maxDao = getMaxGulfDao();
			maxCeng = getMaxGulfInDao();
		}
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(playerId).append("] ");
		sb.append("[难度:").append(nandu).append("] ");
		sb.append("[刷新次数:").append(getTotalFlushDaoTimes2Nandu(nandu)[0]).append(",").append(getTotalFlushDaoTimes2Nandu(nandu)[1]).append(",").append(getTotalFlushDaoTimes2Nandu(nandu)[2]).append(",").append(getTotalFlushDaoTimes2Nandu(nandu)[3]).append(",").append(getTotalFlushDaoTimes2Nandu(nandu)[4]).append(",").append(getTotalFlushDaoTimes2Nandu(nandu)[5]).append(",").append("] ");
		sb.append("[刷新消耗:").append(getTotalCostDaoSilver()[0]).append(",").append(getTotalCostDaoSilver()[1]).append(",").append(getTotalCostDaoSilver()[2]).append(",").append(getTotalCostDaoSilver()[3]).append(",").append(getTotalCostDaoSilver()[4]).append(",").append(getTotalCostDaoSilver()[5]).append(",").append("] ");
		if (nandu == 0) {
			sb.append("[auth:").append(getAuthority()[nandu][0].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_千层塔1道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][1].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_千层塔2道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][2].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_千层塔3道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][3].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_千层塔4道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][4].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_千层塔5道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][5].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_千层塔6道)).append("] ");
		}else if (nandu == 1) {
			sb.append("[auth:").append(getAuthority()[nandu][0].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_困难千层塔1道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][1].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_困难千层塔2道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][2].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_困难千层塔3道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][3].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_困难千层塔4道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][4].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_困难千层塔5道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][5].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_困难千层塔6道)).append("] ");
		}else if (nandu == 2) {
			sb.append("[auth:").append(getAuthority()[nandu][0].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_深渊千层塔1道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][1].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_深渊千层塔2道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][2].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_深渊千层塔3道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][3].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_深渊千层塔4道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][4].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_深渊千层塔5道)).append("] ");
			sb.append("[auth:").append(getAuthority()[nandu][5].usedNum).append("/").append(AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), AuthorityConfig.type_深渊千层塔6道)).append("] ");
		}
		sb.append("[最高道:").append(maxDao).append("] ");
		sb.append("[最高层:").append(maxCeng).append("] ");
		sb.append("[状态:").append(status).append("] ");
		sb.append("[出塔地图:").append(backGameName).append("] ");
		sb.append("[线程Index").append(threadIndex).append("]");
		return sb.toString();
	}
	
	public static Logger logger = LoggerFactory.getLogger(QianCengTa_Ta.class);
	@SimpleId
	protected long playerId;
	
	@SimpleVersion
	private int versiontQCT;
	
	private transient int default_DaoIndex;
	
	//一共已经刷新次数
	private int totalFlushTimes;
	
	public int getTotalFlushTimes() {
		return totalFlushTimes;
	}

	public void setTotalFlushTimes(int totalFlushTimes) {
		this.totalFlushTimes = totalFlushTimes;
		notifyChanager("totalFlushTimes");
	}

	//一共刷新已经花费多少文
	protected long totalCostSilver;
	
	public long getTotalCostSilver() {
		return totalCostSilver;
	}

	public void setTotalCostSilver(long totalCostSilver) {
		this.totalCostSilver = totalCostSilver;
		notifyChanager("totalCostSilver");
	}
	
	private int[] totalFlushDaoTimes = new int[6];
	private int[] totalFlushHardDaoTimes = new int[6];
	private int[] totalFlushGulfDaoTimes = new int[6];
	
	private long[] totalCostDaoSilver = new long[6];

	//玩家通过的最高的道
	protected int maxDao;
	
	public int getMaxDao() {
		return maxDao;
	}

	public void setMaxDao(int maxDao) {
		this.maxDao = maxDao;
		notifyChanager("maxDao");
	}
	
	//玩家通过的困难最高的道
	protected int maxHardDao;
	
	public int getMaxHardDao() {
		return maxHardDao;
	}

	public void setMaxHardDao(int maxHardDao) {
		this.maxHardDao = maxHardDao;
		notifyChanager("maxHardDao");
	}
	
	//玩家通过的深渊最高的道
	protected int maxGulfDao;

	public int getMaxGulfDao() {
		return maxGulfDao;
	}

	public void setMaxGulfDao(int maxGulfDao) {
		this.maxGulfDao = maxGulfDao;
		notifyChanager("maxGulfDao");
	}

	//玩家通过的最高的道的最高层
	protected int maxCengInDao;
	
	public int getMaxCengInDao() {
		return maxCengInDao;
	}

	public void setMaxCengInDao(int maxCengInDao) {
		this.maxCengInDao = maxCengInDao;
		notifyChanager("maxCengInDao");
	}
	
	//玩家通过的困难最高的道的最高层
	protected int maxHardInDao;
	
	public int getMaxHardInDao() {
		return maxHardInDao;
	}

	public void setMaxHardInDao(int maxHardInDao) {
		this.maxHardInDao = maxHardInDao;
		notifyChanager("maxHardInDao");
	}

	//玩家通过的深渊最高的道的最高层
	protected int maxGulfInDao;

	public int getMaxGulfInDao() {
		return maxGulfInDao;
	}

	public void setMaxGulfInDao(int maxGulfInDao) {
		this.maxGulfInDao = maxGulfInDao;
		notifyChanager("maxGulfInDao");
	}

	//到达最高层的时间
	protected long reachMaxCengInDaoTime;
	
	public long getReachMaxCengInDaoTime() {
		return reachMaxCengInDaoTime;
	}

	public void setReachMaxCengInDaoTime(long reachMaxCengInDaoTime) {
		this.reachMaxCengInDaoTime = reachMaxCengInDaoTime;
		notifyChanager("reachMaxCengInDaoTime");
	}
	
	//到达困难最高层的时间
	protected long reachMaxHardTime;

	public long getReachMaxHardTime() {
		return reachMaxHardTime;
	}

	public void setReachMaxHardTime(long reachMaxHardTime) {
		this.reachMaxHardTime = reachMaxHardTime;
		notifyChanager("reachMaxHardTime");
	}
	
	//到达困难最高层的时间
	protected long reachMaxgulfTime;

	public long getReachMaxgulfTime() {
		return reachMaxgulfTime;
	}

	public void setReachMaxgulfTime(long reachMaxgulfTime) {
		this.reachMaxgulfTime = reachMaxgulfTime;
		notifyChanager("reachMaxgulfTime");
	}

	//到达最高层的等级
	protected int reachMaxCengInDaoLevel;
	
	public int getReachMaxCengInDaoLevel() {
		return reachMaxCengInDaoLevel;
	}

	public void setReachMaxCengInDaoLevel(int reachMaxCengInDaoLevel) {
		this.reachMaxCengInDaoLevel = reachMaxCengInDaoLevel;
		notifyChanager("reachMaxCengInDaoLevel");
	}
	
	//到达最高层的等级
	protected int reachMaxHardLevel;

	public int getReachMaxHardLevel() {
		return reachMaxHardLevel;
	}

	public void setReachMaxHardLevel(int reachMaxHardLevel) {
		this.reachMaxHardLevel = reachMaxHardLevel;
		notifyChanager("reachMaxHardLevel");
	}
	
	//到达最高层的等级
	protected int reachMaxGulfLevel;

	public int getReachMaxGulfLevel() {
		return reachMaxGulfLevel;
	}

	public void setReachMaxGulfLevel(int reachMaxGulfLevel) {
		this.reachMaxGulfLevel = reachMaxGulfLevel;
		notifyChanager("reachMaxGulfLevel");
	}

	//每个道的数据
	@SimpleColumn(length=50000)
	private ArrayList<QianCengTa_Dao> daoList = new ArrayList<QianCengTa_Dao>();

	public ArrayList<QianCengTa_Dao> getDaoList() {
		return daoList;
	}

	public void setDaoList(ArrayList<QianCengTa_Dao> daoList) {
		this.daoList = daoList;
	}
	
	//困难道的数据
	@SimpleColumn(length=50000)
	private ArrayList<QianCengTa_Dao> hardDaoList = new ArrayList<QianCengTa_Dao>();

	public void setHardDaoList(ArrayList<QianCengTa_Dao> hardDaoList) {
		this.hardDaoList = hardDaoList;
	}

	public ArrayList<QianCengTa_Dao> getHardDaoList() {
		return hardDaoList;
	}
	
	//深渊道的数据
	@SimpleColumn(length=50000)
	private ArrayList<QianCengTa_Dao> gulfDaoList = new ArrayList<QianCengTa_Dao>();

	public void setGulfDaoList(ArrayList<QianCengTa_Dao> gulfDaoList) {
		this.gulfDaoList = gulfDaoList;
	}

	public ArrayList<QianCengTa_Dao> getGulfDaoList() {
		return gulfDaoList;
	}
	
	private boolean[][] isGetFirst = new boolean[3][6];

	//默认状态
	public static final int STATUS_IDLE = 0;
	//自动爬塔
	public static final int STATUS_AUTO_PA_TA = 1;
	//手动爬塔等待进入状态
	public static final int STATUS_MANUAL_PA_TA_WAITING_ENTER_GAME = 2;
	//手动爬塔战斗状态
	public static final int STATUS_MANUAL_PA_TA_FIGHTING = 3;
	//手动爬塔战斗结束状态
	public static final int STATUS_MANUAL_PA_TA_FIGHTOVER = 4;
	//手动爬塔刷怪开始
	public static final int STATUS_MANUAL_PA_TA_REFMONSTER = 5;
	
	public transient int refMonsterIndex;
	
	//状态, 0 = 空闲，1 = 手动爬塔，2 = 自动爬塔
	transient int status;
	
	public int getStatus() {
		return status;
	}
	
	//上一次状态改变的时间
	transient long lastStatusChangedTime;
	
	private static int[][] authority_Type = new int[][]{
		{AuthorityConfig.type_千层塔1道, AuthorityConfig.type_千层塔2道, AuthorityConfig.type_千层塔3道, AuthorityConfig.type_千层塔4道, AuthorityConfig.type_千层塔5道, AuthorityConfig.type_千层塔6道},
		{AuthorityConfig.type_困难千层塔1道, AuthorityConfig.type_困难千层塔2道, AuthorityConfig.type_困难千层塔3道, AuthorityConfig.type_困难千层塔4道, AuthorityConfig.type_困难千层塔5道, AuthorityConfig.type_困难千层塔6道},
		{AuthorityConfig.type_深渊千层塔1道, AuthorityConfig.type_深渊千层塔2道, AuthorityConfig.type_深渊千层塔3道, AuthorityConfig.type_深渊千层塔4道, AuthorityConfig.type_深渊千层塔5道, AuthorityConfig.type_深渊千层塔6道}
	};
	
	private transient Authority[][] authority;
	
	//难度
	private transient int nandu;
	//是否返回新协议
	private transient boolean isNew = true;
	
	//游戏场景引擎
	transient Game game;
	
	public Game getGame() {
		return game;
	}
	
	public transient ArrayList<Monster> monsterList = new ArrayList<Monster>();
	
	//出塔时回的地图和位置
	transient String backGameName;
	transient int backGameCountry;
	transient int backX;
	transient int backY;
	
	//线程Index
	private transient int threadIndex;
	
	//出塔时回的地图，是否要打开千层塔界面
	transient boolean openQianCengTaUI = false;
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	public Player getPlayer(){
		try{
			PlayerManager pm = PlayerManager.getInstance();
			return pm.getPlayer(playerId);
		}catch(Exception e){
			logger.error("[千层塔] [获取对应的玩家] [失败] [加载玩家失败] [playerId:"+playerId+"]",e);
			return null;
		}
	}
	
	/**
	 * 初始化此塔，此方法在创建此塔
	 * 应该调用此方法
	 */
	public void init(){
		for (int i = 0; i < QianCengTa_TaInfo.daoCount; i++) {
			openDao(0, i);
		}
		for (int i = 0; i < QianCengTa_TaInfo.daoCount; i++) {
			openDao(1, i);
		}
		for (int i = 0; i < QianCengTa_TaInfo.daoCount; i++) {
			openDao(2, i);
		}
		if (logger.isWarnEnabled())
			logger.warn("第一次创建千层塔 [p={}] [daosize={}] [sta={}] [ta={}]", new Object[]{getPlayerId(), daoList.size(), status, getLogString(0)});
	}
	
	public void openDao(int nandu, int daoIndex){
		QianCengTa_Dao dao = new QianCengTa_Dao();
		dao.ta = this;
		dao.setDaoIndex(daoIndex);
		dao.setCurrentCengIndex(0);
		dao.setMaxReachCengIndex(-1);
		QianCengTa_Ceng ceng = new QianCengTa_Ceng();
		ceng.dao = dao;
		ceng.setCengIndex(0);
		dao.getCengList().add(ceng);
		ArrayList<QianCengTa_Dao> daoList = getDaoList2Nandu(nandu);
		daoList.add(dao);
		if (nandu == 0) {
			notifyChanager("daoList");
		}else if (nandu == 1) {
			notifyChanager("hardDaoList");
		}else if (nandu == 2) {
			notifyChanager("gulfDaoList");
		}
		logger.warn("[开启某道] [{}] [{}]", new Object[]{getLogString(nandu), daoIndex});
	}
	
	public void doInitBef(){
		status = STATUS_IDLE;
		lastStatusChangedTime = SystemTime.currentTimeMillis();
		//修正出现的bug
		while(getDaoList().size() > 6){
			getDaoList().remove(getDaoList().size() - 1);
			notifyChanager("daoList");
		}
		while(getHardDaoList().size() > 6){
			getHardDaoList().remove(getHardDaoList().size() - 1);
			notifyChanager("hardDaoList");
		}
		while(getGulfDaoList().size() > 6){
			getGulfDaoList().remove(getGulfDaoList().size() - 1);
			notifyChanager("gulfDaoList");
		}
		
		//设置各个对象transient属性，必须设置
//		ArrayList<QianCengTa_Dao> errDao = new ArrayList<QianCengTa_Dao>();
		for(int i = 0 ; i < getDaoList().size() ; i++){
			QianCengTa_Dao dao = getDaoList().get(i);
			if (dao.getDaoIndex() != i) {
//				errDao.add(dao);
//				break;
				dao.setDaoIndex(i);
				notifyChanager("daoList");
			}
			dao.ta = this;
			if (dao.getHiddenCeng() != null) {
				if (dao.getHiddenCeng().getRewards().size() > 0) {
					dao.getHiddenCeng().rewardStatus = QianCengTa_Ceng.STATUS_奖励未领取;
				}
				dao.getHiddenCeng().dao = dao;
			}
			for (int j = 0; j < dao.getCengList().size(); j++) {
				QianCengTa_Ceng ceng = dao.getCengList().get(j);
				if (ceng.getRewards().size() > 0) {
					ceng.rewardStatus = QianCengTa_Ceng.STATUS_奖励未领取;
				}
				ceng.dao = dao;
			}
		}
//		if (errDao.size() > 0) {
//			logger.error("[千层塔] [道数据异常] [列表中daoIndex不正确] [{}] [{}]", new Object[]{errDao.size(), errDao.get(0).getDaoIndex()});
//		}
//		for (int i = 0; i < errDao.size(); i++) {
//			getDaoList().remove(errDao.get(i));
//		}
//		errDao.clear();
		for(int i = 0 ; i < getHardDaoList().size() ; i++){
			QianCengTa_Dao dao = getHardDaoList().get(i);
			if (dao.getDaoIndex() != i) {
//				errDao.add(dao);
//				continue;
				dao.setDaoIndex(i);
				notifyChanager("hardDaoList");
			}
			dao.ta = this;
			if (dao.getHiddenCeng() != null) {
				if (dao.getHiddenCeng().getRewards().size() > 0) {
					dao.getHiddenCeng().rewardStatus = QianCengTa_Ceng.STATUS_奖励未领取;
				}
				dao.getHiddenCeng().dao = dao;
			}
			for (int j = 0; j < dao.getCengList().size(); j++) {
				QianCengTa_Ceng ceng = dao.getCengList().get(j);
				if (ceng.getRewards().size() > 0) {
					ceng.rewardStatus = QianCengTa_Ceng.STATUS_奖励未领取;
				}
				ceng.dao = dao;
			}
		}
//		for (int i = 0; i < errDao.size(); i++) {
//			getHardDaoList().remove(errDao.get(i));
//		}
//		errDao.clear();
		for(int i = 0 ; i < getGulfDaoList().size() ; i++){
			QianCengTa_Dao dao = getGulfDaoList().get(i);
			if (dao.getDaoIndex() != i) {
//				errDao.add(dao);
//				continue;
				dao.setDaoIndex(i);
				notifyChanager("gulfDaoList");
			}
			dao.ta = this;
			if (dao.getHiddenCeng() != null) {
				if (dao.getHiddenCeng().getRewards().size() > 0) {
					dao.getHiddenCeng().rewardStatus = QianCengTa_Ceng.STATUS_奖励未领取;
				}
				dao.getHiddenCeng().dao = dao;
			}
			for (int j = 0; j < dao.getCengList().size(); j++) {
				QianCengTa_Ceng ceng = dao.getCengList().get(j);
				if (ceng.getRewards().size() > 0) {
					ceng.rewardStatus = QianCengTa_Ceng.STATUS_奖励未领取;
				}
				ceng.dao = dao;
			}
		}
//		for (int i = 0; i < errDao.size(); i++) {
//			getGulfDaoList().remove(errDao.get(i));
//		}
		for (int i = getDaoList().size(); i < QianCengTa_TaInfo.daoCount; i++) {
			openDao(0, i);
		}
		for (int i = getHardDaoList().size(); i < QianCengTa_TaInfo.daoCount; i++) {
			openDao(1, i);
		}
		for (int i = getGulfDaoList().size(); i < QianCengTa_TaInfo.daoCount; i++) {
			openDao(2, i);
		}
		
		
//		//修正bug
//		//之前在打通一道最后一层的时候出错了
//		int size = getDaoList().size();
//		if (size < 6) {
//			QianCengTa_Dao dao = getDaoList().get(size - 1);
//			if (getMaxDao() == dao.getDaoIndex()-1 && getMaxCengInDao() == 23) {
//				openDao(1, dao.getDaoIndex());
//				setMaxDao(dao.getDaoIndex());
//				setMaxCengInDao(-1);
//			}
//		}
//		if (getDaoList().size() == getMaxDao()) {
//			openDao(0, getMaxDao());
//		}
//		if (getHardDaoList().size() == getMaxHardDao()) {
//			openDao(1, getMaxHardDao());
//		}
//		//修正bug
//		for (int i = 0; i < getDaoList().size(); i++) {
//			QianCengTa_Dao dao = getDaoList().get(i);
//			if (dao.getMaxReachCengIndex() == 24) {
//				if (getHardDaoList().size() < i+1) {
//					openDao(1, i);
//					logger.warn("修正玩家千层塔错误: ["+getLogString(1)+"]");
//				}
//			}
//		}
//		for (int i = 0; i < getHardDaoList().size(); i++) {
//			QianCengTa_Dao dao = getHardDaoList().get(i);
//			if (dao.getMaxReachCengIndex() == 24) {
//				if (getGulfDaoList().size() < i+1) {
//					openDao(2, i);
//					logger.warn("修正玩家千层塔错误: ["+getLogString(2)+"]");
//				}
//			}
//		}
	}
	
	private static long AUTO_PA_TA_STOP_TIME = 10*1000;
	
	private transient long GAME_NO_PLAYER_TIME = 0;
	
	private static Random random = new Random();
	
	public synchronized void heartbeat(long now){
		try{
			QianCengTa_TaInfo taInfo = getTaInfo();
			if(game != null){
				game.heartbeat();
				if (game.getNumOfPlayer() <= 0) {
					if (GAME_NO_PLAYER_TIME == 0) {
						GAME_NO_PLAYER_TIME = System.currentTimeMillis();
					}
					if (now - GAME_NO_PLAYER_TIME > 10 * 1000) {
						game = null;
						GAME_NO_PLAYER_TIME = 0;
					}
				}else {
					GAME_NO_PLAYER_TIME = 0;
				}
			}
			
			if (status == STATUS_AUTO_PA_TA){
				if (now - lastStatusChangedTime > AUTO_PA_TA_STOP_TIME) {
					stopAutoPaTa(getPlayer(), getNandu());
					if (logger.isWarnEnabled())
						logger.warn("因为10秒未收到客户端的自动爬塔协议，停止爬塔");
				}
			}
			
			if(status == STATUS_MANUAL_PA_TA_WAITING_ENTER_GAME){
				Player player = getPlayer();
				if(player != null){
					if(now - this.lastStatusChangedTime > 3*60*1000){
						//等待进入游戏超时
						status = STATUS_IDLE;
						lastStatusChangedTime  = now;
						notifyGameFightingOver(player,"等待进入游戏超时");
					}else if(game != null && game.contains(player)){
						//爬塔战斗进行时
						status = STATUS_MANUAL_PA_TA_FIGHTING;
						lastStatusChangedTime  = now;
					}else if(game == null){
						status = STATUS_IDLE;
						lastStatusChangedTime  = now;
					}
				}else{
					status = STATUS_IDLE;
					lastStatusChangedTime  = now;
				}
			}
			//玩家正在战斗状态中
			if(status == STATUS_MANUAL_PA_TA_REFMONSTER){
				try{
					if(game == null){
						status = STATUS_IDLE;
						lastStatusChangedTime  = now;
					}else{
						Player player = getPlayer();
						if(player != null){
							if(game.contains(player) == false){
								notifyGameFightingOver(player,"玩家已经不在塔里");
								game = null;
							}else if(player.isOnline()){
								if (player.isDeath()) {
									notifyGameFightingOver(player,"玩家已经死亡");
									//复活
									player.setHp(player.getMaxHP());
									player.setMp(player.getMaxMP());
									player.setState(Constants.STATE_STAND);
									PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte)0, "", player.getMaxHP(), player.getMaxMP());
									player.addMessageToRightBag(res);
									QianCengTa_CengInfo ci = taInfo.getCengInfo(game.getQianCengTa().dao.getDaoIndex(), game.getQianCengTa().getCengIndex());
									String err =  "<f color='0xf00f00'>"+Translate.text_qiancengta_001+"</f>\n" + ci.getStartInfo();
									notifyGameFightingFail(player, err);
								}else {
									if (lastStatusChangedTime + QianCengTaManager.LAST_TIME < now) {
										//开始刷怪
										QianCengTa_CengInfo ci = taInfo.getCengInfo(game.getQianCengTa().dao.getDaoIndex(), game.getQianCengTa().getCengIndex());
										for (int i = refMonsterIndex; i < ci.getMonsters().size(); i++) {
											QianCengTa_CengMonsterInfo mi = ci.getMonsters().get(i);
											if (lastStatusChangedTime + QianCengTaManager.LAST_TIME + mi.getRefTime() <= now) {
												refMonsterIndex ++;
												MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
												for (int mp = 0; mp < mi.getPoss().size(); mp++){
													Monster monster = mmm.createMonster(mi.getMonsterID());
													int[] pos = mi.getPoss().get(mp);
													if (monster != null) {
														monster.setX(pos[0]);
														monster.setY(pos[1]);
														monster.setBornPoint(new Point(pos[0], pos[1]));
														monster.setHp(monster.getMaxHP());
														monster.setGameNames(game.gi);
														int speedRan = 110 - random.nextInt(20);
														monster.setSpeed(monster.getSpeed() * speedRan / 100);
														game.addSprite(monster);
														monsterList.add(monster);
														logger.warn("[千层塔刷怪] [id:"+monster.getId()+"] [name:"+monster.getName()+"] [xy:"+monster.getX()+"/"+monster.getY()+"] ["+monsterList.size()+"] ["+getPlayer().getName()+"]");
													}
												}
											}
										}
										QianCengTa_Ceng ceng = game.getQianCengTa();
										//判断战斗是否该结束了
										if (ci.getCengTime() > 0) {
											//有时间限制
											if (ceng.getStartTime() + ci.getCengTime() < now) {
												if (ci.getKillMonster().size() > 0) {
													notifyGameFightingOver(player, "时间到了挑战失败");
													String err =  "<f color='0xf00f00'>"+Translate.text_qiancengta_002+"</f>\n" + ci.getStartInfo();
													notifyGameFightingFail(player, err);
													return;
												}else {
													notifyPassCeng(player,game.getQianCengTa());
													return;
												}
											}
										}
										if (ci.getKillMonster().size() > 0) {
											//需要有目标的杀怪
											HashMap<Integer, Integer> kills = new HashMap<Integer, Integer>();
											kills.putAll(ci.getKillMonster());
											for (int i = 0; i < monsterList.size(); i++) {
												if(monsterList.get(i).isDeath()){
													Integer num = kills.get(monsterList.get(i).getSpriteCategoryId());
													if (num != null) {
														num = num -1;
														if (num.intValue() <= 0) {
															kills.remove(monsterList.get(i).getSpriteCategoryId());
														}else{
															kills.put(monsterList.get(i).getSpriteCategoryId(), num);
														}
													}
												}
											}
											if (kills.size() <= 0) {
												notifyPassCeng(player,game.getQianCengTa());
											}
										}else {
											//需要杀所有怪
											boolean hasAliveMonster = false;
											int num = 0;
											for (int i = 0; i < monsterList.size(); i++) {
												if(monsterList.get(i).isDeath() == false){
													hasAliveMonster =  true;
													num += 1;
												}
											}
											
//											if (hasAliveMonster) {
//												hasAliveMonster = false;
//												for (int i = 0; i < game.getLivingObjects().length; i++) {
//													if (game.getLivingObjects()[i] instanceof Monster) {
//														hasAliveMonster = true;
//														break;
//													}
//												}
//											}
											
											//logger.warn("[剩余怪物数目] [{}] [{}] [{}] [{}]", new Object[]{monsterList.size(),num, refMonsterIndex, ci.getMonsters().size()});
											if (hasAliveMonster == false && refMonsterIndex == ci.getMonsters().size()) {
												notifyPassCeng(player,game.getQianCengTa());
											}
										}
									}
								}
							}else{
								notifyGameFightingOver(player,"玩家已经不在线");
								game = null;
							}
						}else{
							status = STATUS_IDLE;
							lastStatusChangedTime  = now;
							game = null;
						}
					}
				}catch(Exception e){
					logger.error("心跳，处理刷怪和战斗结束", e);
				}
			}else if(status == STATUS_MANUAL_PA_TA_FIGHTING){
				if(game == null){
					status = STATUS_IDLE;
					lastStatusChangedTime  = now;
				}else{
					Player player = getPlayer();
					if (now - lastStatusChangedTime > 5*1000) {
						if(player != null){
							if(game.contains(player) == false || player.getCurrentGame() != game){
								notifyGameFightingOver(player,"玩家已经不在塔里");
								game = null;
							}else if(player.isOnline()){
							}else{
								notifyGameFightingOver(player,"玩家已经不在线");
								game = null;
							}
						}else{
							status = STATUS_IDLE;
							lastStatusChangedTime  = now;
						}
					}
				}
			}
			
			if(status == STATUS_MANUAL_PA_TA_FIGHTOVER){
				if(now - this.lastStatusChangedTime >0){
					Player player = getPlayer();
					if(player != null){
						notifyGameFightingOver(player,"领取奖励超时");
					}
				}else if(game == null){
					status = STATUS_IDLE;
					lastStatusChangedTime  = now;
				}
			}
		} catch(Exception e) {
			logger.error("千层塔心跳出错" + getLogString(0), e);
		}
	}
	
	/**
	 * 通知玩家挑战成功
	 * 
	 * @param ceng
	 */
	public void notifyPassCeng(Player player,QianCengTa_Ceng ceng){
		QianCengTa_TaInfo taInfo = getTaInfo();
		status = STATUS_MANUAL_PA_TA_FIGHTOVER;
		lastStatusChangedTime  = System.currentTimeMillis();
		
		ArrayList<QianCengTa_Dao> daoList = getDaoList2Nandu(getNandu());
		
		int daoIndex = daoList.indexOf(ceng.dao);
		int index = ceng.getCengIndex();
		
		if(ceng.dao.getMaxReachCengIndex() < index){
			ceng.dao.setMaxReachCengIndex(index);
		}
		if (index >= 0) {
			ceng.dao.setCurrentCengIndex(index + 1);
			// 活跃度统计
			ActivenessManager.getInstance().record(player, ActivenessType.幽冥幻域);
		}
		if (index < QianCengTa_TaInfo.cengCount - 1 && index >=0) {
			ceng.dao.openCeng();
		}
		if (index <= QianCengTa_TaInfo.cengCount -1 && index >= 0 && nandu == 0) {
			BillboardStatDate bData = BillboardStatDateManager.getInstance().getBillboardStatDate(getPlayerId());
			logger.warn("[千层塔] [排行] [p={}] [oD={}] [oC={}] [nD={}] [nC={}]", new Object[]{getPlayerId(), bData.getJie(), bData.getCeng(), daoIndex, index});
			bData.setJie(daoIndex);
			bData.setCeng(index);
		}
		boolean isOpenNextDao = false;
		RecordAction action = null;
		if(index >= 0 && ceng.dao.getMaxReachCengIndex() >= QianCengTa_TaInfo.cengCount -1){
			QianCengTaManager.getInstance().doPassDaoActivity(getPlayer(), nandu, daoIndex);
			if(daoIndex < QianCengTa_TaInfo.daoCount -1){
				//开启下一道
				isOpenNextDao = true;
				if (nandu == 0) {
//					openDao(nandu, daoIndex + 1);
//					openDao(nandu+1, daoIndex);
					if(daoIndex < PlayerAimManager.幽冥幻域普通action.length) {
						action = PlayerAimManager.幽冥幻域普通action[daoIndex];
					}
					if (isOpenHard) {
						player.send_HINT_REQ(Translate.translateString(Translate.text_qiancengta_开启其他难度塔提示, new String[][]{{Translate.STRING_1, QianCengTa_TaInfo.QianCengNanduNames[nandu+1]},{Translate.STRING_2, Translate.text_qiancengta_DaoNames[daoIndex]}}));
					}
				}else if (nandu == 1) {
//					openDao(nandu+1, daoIndex);
					if(daoIndex < PlayerAimManager.幽冥幻域困难action.length) {
						action = PlayerAimManager.幽冥幻域困难action[daoIndex];
					}
					if (isOpenGulf) {
						player.send_HINT_REQ(Translate.translateString(Translate.text_qiancengta_开启其他难度塔提示, new String[][]{{Translate.STRING_1, QianCengTa_TaInfo.QianCengNanduNames[nandu+1]},{Translate.STRING_2, Translate.text_qiancengta_DaoNames[daoIndex]}}));
					}
				} else if (nandu == 2) {
					if(daoIndex < PlayerAimManager.幽冥幻域深渊action.length) {
						action = PlayerAimManager.幽冥幻域深渊action[daoIndex];
					}
				}
			}else if (daoIndex == QianCengTa_TaInfo.daoCount -1){
				if (nandu == 0) {
//					openDao(nandu+1, daoIndex);
					if(daoIndex < PlayerAimManager.幽冥幻域普通action.length) {
						action = PlayerAimManager.幽冥幻域普通action[daoIndex];
					}
					if (isOpenHard) {
						player.send_HINT_REQ(Translate.translateString(Translate.text_qiancengta_开启其他难度塔提示, new String[][]{{Translate.STRING_1, QianCengTa_TaInfo.QianCengNanduNames[nandu+1]},{Translate.STRING_2, Translate.text_qiancengta_DaoNames[daoIndex]}}));
					}
				}else if (nandu == 1) {
//					openDao(nandu+1, daoIndex);
					if(daoIndex < PlayerAimManager.幽冥幻域困难action.length) {
						action = PlayerAimManager.幽冥幻域困难action[daoIndex];
					}
					if (isOpenGulf) {
						player.send_HINT_REQ(Translate.translateString(Translate.text_qiancengta_开启其他难度塔提示, new String[][]{{Translate.STRING_1, QianCengTa_TaInfo.QianCengNanduNames[nandu+1]},{Translate.STRING_2, Translate.text_qiancengta_DaoNames[daoIndex]}}));
					}
				} else if (nandu == 2) {
					if(daoIndex < PlayerAimManager.幽冥幻域深渊action.length) {
						action = PlayerAimManager.幽冥幻域深渊action[daoIndex];
					}
				}
			}
		}
		try {
			if(action != null) {
				EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), action, 1L});
				EventRouter.getInst().addEvent(evt3);
			}
		} catch (Exception eex) {
			PlayerAimManager.logger.error("[目标系统] [统计通过幽冥幻域异常][" + player.getLogString() + "]", eex);
		}
		if(isOpenNextDao){
			if (nandu == 0) {
				if (daoIndex >= getMaxDao()) {
					setMaxDao(daoIndex + 1);
				}
				setMaxCengInDao(-1);
			}else if (nandu == 1) { 
				if (daoIndex >= getMaxHardDao()) {
					setMaxHardDao(daoIndex+1);
				}
				setMaxHardInDao(-1);
			}else if (nandu == 2) {
				if (daoIndex >= getMaxGulfDao()) {
					setMaxGulfDao(daoIndex+1);
				}
				setMaxGulfInDao(-1);
			}
		}
		int maxDao = 0;
		int maxDaoInCeng = 0;
		if (nandu == 0) {
			maxDao = getMaxDao();
			maxDaoInCeng = getMaxCengInDao();
		}else if (nandu == 1) {
			maxDao = getMaxHardDao();
			maxDaoInCeng = getMaxHardInDao();
		}else if (nandu == 2) {
			maxDao = getMaxGulfDao();
			maxDaoInCeng = getMaxGulfInDao();
		}
		if(maxDao == daoIndex){
			if(maxDaoInCeng < index){
				if (nandu == 0) {
					setMaxCengInDao(index);
					setReachMaxCengInDaoTime(SystemTime.currentTimeMillis());
					setReachMaxCengInDaoLevel(player.getLevel());
				}else if (nandu == 1) {
					setMaxHardInDao(index);
					setReachMaxHardTime(SystemTime.currentTimeMillis());
					setReachMaxHardLevel(player.getLevel());
				}else if (nandu == 2) {
					setMaxGulfInDao(index);
					setReachMaxgulfTime(SystemTime.currentTimeMillis());
					setReachMaxGulfLevel(player.getLevel());
				}
			}
		}
		QianCengTa_Statistics.creatNewStatistics(player, daoIndex, index);
		try {
			ceng.calculateFlopByPaTa();
			QianCengTa_CengInfo ci = taInfo.getCengInfo(ceng.dao.getDaoIndex(), ceng.getCengIndex());
			ResponseMessage res = null;
			if (isNew()) {
				res = new NEWQIANCHENGTA_MANUAL_OVER_RES(GameMessageFactory.nextSequnceNum(), getNandu(), daoIndex, QianCengTa_DaoInfo.QianCengDaoName[daoIndex], ci.getStartInfo(), ceng.getRewardExp(), ceng.getRewardMsg());
			}else {
				res = new QIANCHENGTA_MANUAL_OVER_RES(GameMessageFactory.nextSequnceNum(), daoIndex, QianCengTa_DaoInfo.QianCengDaoName[daoIndex], ci.getStartInfo(), ceng.getRewardExp(), ceng.getRewardMsg());
			}
			player.addMessageToRightBag(res);
		} catch (Exception e) {
			logger.error("notifyPassCeng:生成掉落出错", e);
		}
		
		if (!ceng.dao.isOpenHidden() && ceng.getCengIndex() >= 0) {
			Random ran = new Random();
			int next = QianCengTa_TaInfo.QianCengHideRandom1[daoIndex] + QianCengTa_TaInfo.QianCengHideRandom2[daoIndex] * ceng.getCengIndex();
			if (next > ran.nextInt(QianCengTa_TaInfo.HideRandom)) {
				ceng.dao.setOpenHidden(true);
				QianCengTa_Ceng hiddenCeng = new QianCengTa_Ceng();
				hiddenCeng.dao = ceng.dao;
				hiddenCeng.setCengIndex(-1);
				ceng.dao.setHiddenCeng(hiddenCeng);
				player.send_HINT_REQ(Translate.text_qiancengta_恭喜你开启隐藏层, (byte)2);
				if (logger.isWarnEnabled())
					logger.warn("[隐藏层开启] [p={}] [nandu={}] [daoIndex={}] [ceng={}]", new Object[]{getPlayerId(), getNandu(), daoIndex, index});
			}
		}
		if (getNandu() == 0) {
			notifyChanager("daoList");
		}else if (getNandu() == 1) {
			notifyChanager("hardDaoList");
		}else if (getNandu() == 2) {
			notifyChanager("gulfDaoList");
		}
		
		if(logger.isWarnEnabled())
			logger.warn("[千层塔] [手动爬塔通过] [成功] [OK] [难度:"+getNandu()+"] [status:"+status+"] [reason:--] [daoIndex:"+daoIndex+"] [cengIndex:"+index+"] [" + player.getLogString()+"]");
	}
	
	/**
	 * 挑战某一层结束
	 * @param reason
	 */
	protected void notifyGameFightingOver(Player player,String reason){
		status = STATUS_IDLE;
		this.lastStatusChangedTime = SystemTime.currentTimeMillis();
		if(game == null){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔结束] [失败] [Game为空] [status:"+status+"] [reason:"+reason+"] [daoIndex:--] [cengIndex:--] [" + player.getLogString()+"]");
			return;
		}
		//清楚集合中的怪物
		for(Monster m : monsterList){
			game.removeSprite(m);
		}
		LivingObject[] livs = game.getLivingObjects();
		for (int i = 0; i < livs.length; i++) {
			if (livs[i] instanceof Monster) {
				game.removeSprite((Monster)livs[i]);
			}
		}
		monsterList.clear();
		if(logger.isWarnEnabled())
			logger.warn("[千层塔] [手动爬塔结束] [成功] [OK] [status:"+status+"] [reason:"+reason+"] [daoIndex:--] [cengIndex:--] [" + player.getLogString()+"]");
	}
	
	//挑战失败
	private void notifyGameFightingFail(Player player, String resion){
		QianCengTa_Ceng ceng = game.getQianCengTa();
		ResponseMessage res = null;
		if (isNew()) {
			res = new NEWQIANCHENGTA_MANUAL_FAIL_RES(GameMessageFactory.nextSequnceNum(), getNandu(), ceng.dao.getDaoIndex(), QianCengTa_DaoInfo.QianCengDaoName[ceng.dao.getDaoIndex()], ceng.getCengIndex(), resion);
		}else {
			res = new QIANCHENGTA_MANUAL_FAIL_RES(GameMessageFactory.nextSequnceNum(), ceng.dao.getDaoIndex(), QianCengTa_DaoInfo.QianCengDaoName[ceng.dao.getDaoIndex()], ceng.getCengIndex(), resion);
		}
		player.addMessageToRightBag(res);
		if (logger.isWarnEnabled())
			logger.warn("[千层塔] [挑战失败] [resion={}] [pid={}] [dao={}] [ceng={}]", new Object[]{resion, player.getId(), game.getQianCengTa().dao.getDaoIndex(), game.getQianCengTa().getCengIndex()});
	}
	
	/**
	 * CoreSubSystem收到玩家进入地图的协议，
	 * 此方法用于检测，是否要进入千层塔
	 * 
	 * 返回game对象，标识进入塔，否则返回null
	 * 
	 * @param player
	 */
	public synchronized Game handle_ENTER_GAME_REQ(Player player,ENTER_GAME_REQ req){
		QianCengTa_TaInfo taInfo = getTaInfo();
		if(player.getId() != this.playerId){
			return null;
		}
		if(game == null){
			return null;
		}
		String gameName = req.getGame();
		if(gameName.equals(game.gi.getMapName())){
			int ret = playerPrepareToEnterTa(player);
			if(ret == 0){
				String mapName =  QianCengTa_DaoInfo.QianCengDaoName[game.getQianCengTa().dao.getDaoIndex()] + "●";
				if (game.getQianCengTa().getCengIndex() < 0) {
					mapName = mapName + Translate.text_qiancengta_003;
				} else {
					mapName = mapName + (game.getQianCengTa().getCengIndex() + 1) + Translate.text_qiancengta_004;
				}
				CHANGE_GAME_DISPLAYNAME_REQ req1 = new CHANGE_GAME_DISPLAYNAME_REQ(GameMessageFactory.nextSequnceNum(), game.gi.name, mapName);
				player.addMessageToRightBag(req1);
				QianCengTa_Ceng ceng = game.getQianCengTa();
				ResponseMessage res = null;
				if (isNew()) {
					res = new NEWQIANCHENGTA_MANUAL_MSG_RES(GameMessageFactory.nextSequnceNum(), getNandu(), ceng.dao.getDaoIndex(), QianCengTa_DaoInfo.QianCengDaoName[ceng.dao.getDaoIndex()], ceng.getCengIndex(), taInfo.getCengInfo(ceng.dao.getDaoIndex(), ceng.getCengIndex()).getStartInfo());
				}else {
					res = new QIANCHENGTA_MANUAL_MSG_RES(GameMessageFactory.nextSequnceNum(), ceng.dao.getDaoIndex(), QianCengTa_DaoInfo.QianCengDaoName[ceng.dao.getDaoIndex()], ceng.getCengIndex(), taInfo.getCengInfo(ceng.dao.getDaoIndex(), ceng.getCengIndex()).getStartInfo());
				}
				player.addMessageToRightBag(res);
				logger.warn("[收到entergame] [{}] [{}]", new Object[]{player.getLogString(), getLogString(nandu)});
				return game;
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}
	
	/**
	 * 此方法通知，玩家即将进入塔，此方法在ENTER_GAME_REQ处理过程中调用
	 * @param player
	 * @return
	 */
	synchronized int playerPrepareToEnterTa(Player player){
		
		if(game == null){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [即将进入塔] [失败] [GAME不存在] [status:"+status+"] [daoIndex:--] [cengIndex:--] [" + player.getLogString()+"]");
			return 6;
		}
		QianCengTa_Ceng ceng = game.getQianCengTa();
		ArrayList<QianCengTa_Dao> daoList = getDaoList2Nandu(ceng.getNandu());
		int daoIndex = daoList.indexOf(ceng.dao);
		int cengIndex =ceng.getCengIndex();
		
		if(status != STATUS_MANUAL_PA_TA_WAITING_ENTER_GAME){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [即将进入塔] [失败] [状态不对，不是等待进入状态] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex+"] [" + player.getLogString()+"]");
			return 1;
		}
		
		status = STATUS_MANUAL_PA_TA_FIGHTING;
		lastStatusChangedTime  = SystemTime.currentTimeMillis();

		if(logger.isWarnEnabled())
			logger.warn("[千层塔] [进入塔] [成功] [ok] [tN="+getNandu()+"] [cN="+ceng.getNandu()+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex+"] [" + player.getLogString()+"]");
	
		return 0;
	}

	public int getSize(){
		return 1;
	}

	public long getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	
	public void destroy(){
	}
	
	public void setVersiontQCT(int versiontQCT) {
		this.versiontQCT = versiontQCT;
	}

	public int getVersiontQCT() {
		return versiontQCT;
	}
	
	public void notifyChanager(String propName){
		QianCengTaManager.getInstance().em.notifyFieldChange(this, propName);
	}

	public void setAuthority(Authority[][] authority) {
		this.authority = authority;
	}

	public Authority[][] getAuthority() {
		if (authority == null)
			authority = new Authority[3][6];
			authority[0][0] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_千层塔1道);
			authority[0][1] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_千层塔2道);
			authority[0][2] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_千层塔3道);
			authority[0][3] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_千层塔4道);
			authority[0][4] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_千层塔5道);
			authority[0][5] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_千层塔6道);
			authority[1][0] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_困难千层塔1道);
			authority[1][1] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_困难千层塔2道);
			authority[1][2] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_困难千层塔3道);
			authority[1][3] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_困难千层塔4道);
			authority[1][4] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_困难千层塔5道);
			authority[1][5] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_困难千层塔6道);
			authority[2][0] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_深渊千层塔1道);
			authority[2][1] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_深渊千层塔2道);
			authority[2][2] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_深渊千层塔3道);
			authority[2][3] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_深渊千层塔4道);
			authority[2][4] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_深渊千层塔5道);
			authority[2][5] = AuthorityAgent.getInstance().getAuthority(getPlayer(), AuthorityConfig.type_深渊千层塔6道);
		return authority;
	}

	public void setDefault_DaoIndex(int default_DaoIndex) {
		this.default_DaoIndex = default_DaoIndex;
	}

	public int getDefault_DaoIndex() {
		return default_DaoIndex;
	}

	public long startRefMonster(){
		QianCengTa_TaInfo taInfo = QianCengTa_TaInfo.taInfo;
		status = STATUS_MANUAL_PA_TA_REFMONSTER;
		lastStatusChangedTime = SystemTime.currentTimeMillis();
		refMonsterIndex = 0;
		QianCengTa_Ceng ceng = game.getQianCengTa();
		ceng.setStartTime(SystemTime.currentTimeMillis() + QianCengTaManager.LAST_TIME);
		QianCengTa_CengInfo ci = taInfo.getCengInfo(ceng.dao.getDaoIndex(), ceng.getCengIndex());
		if (logger.isWarnEnabled())
			logger.warn("[千层塔] [开始刷怪] [p={}] [d={}] [dM={}] [c={}]", new Object[]{getPlayer().getLogString(), ceng.dao.getDaoIndex(), ceng.dao.getMaxReachCengIndex(), ceng.getCengIndex()});
		return ci.getCengTime();
	}
	
	public void backPlayerToFormGame(){
		Player p = getPlayer(); 
		if (p.isOnline() && game != null && game.contains(p)) {
			p.setTransferGameCountry(this.backGameCountry);
			game.transferGame(p, new TransportData(0,0,0,0,this.backGameName,this.backX,this.backY));
		}
		game = null;
		status = STATUS_IDLE;
		lastStatusChangedTime = SystemTime.currentTimeMillis();
	}

	@Override
	public void remove(int type) {
		QianCengTaManager.getInstance().removeTa(this);
	}

	public void setThreadIndex(int threadIndex) {
		this.threadIndex = threadIndex;
	}

	public int getThreadIndex() {
		return threadIndex;
	}

	public void setTotalFlushDaoTimes(int[] totalFlushDaoTimes) {
		this.totalFlushDaoTimes = totalFlushDaoTimes;
	}

	public int[] getTotalFlushDaoTimes() {
		return totalFlushDaoTimes;
	}
	
	public int[] getTotalFlushDaoTimes2Nandu(int nandu) {
		if (nandu == 0) {
			return totalFlushDaoTimes;
		}else if (nandu == 1) {
			return totalFlushHardDaoTimes;
		}else if (nandu == 2) {
			return totalFlushGulfDaoTimes;
		}
		return null;
	}
	
	public void setTotalFlushDaoTimes2Index(int nandu, int daoIndex, int value) {
		if (nandu == 0) {
			totalFlushDaoTimes[daoIndex] = value;
			notifyChanager("totalFlushDaoTimes");
		}else if (nandu == 1) {
			totalFlushHardDaoTimes[daoIndex] = value;
			notifyChanager("totalFlushHardDaoTimes");
		}else if (nandu == 2) {
			totalFlushGulfDaoTimes[daoIndex] = value;
			notifyChanager("totalFlushGulfDaoTimes");
		}
	}

	public void setTotalCostDaoSilver(long[] totalCostDaoSilver) {
		this.totalCostDaoSilver = totalCostDaoSilver;
	}

	public long[] getTotalCostDaoSilver() {
		return totalCostDaoSilver;
	}
	
	public void setTotalCostDaoSilver2Index(int daoIndex, long value) {
		totalCostDaoSilver[daoIndex] = value;
		notifyChanager("totalCostDaoSilver");
	}
	
	/**
	 * 
	 * 下面的是新的千层塔代码
	 * 
	 */
	
	
	//获取塔的状态信息
	public RequestMessage getInfo(int nandu, int daoIndex){
		if (!isOpenHard) {
			if (nandu == 1) {
				Player player = getPlayer();
				if(player!=null){
					player.sendError(Translate.千层塔困难暂未开启);
				}
				return null;
			}
		}
		if (!isOpenGulf) {
			if (nandu == 2) {
				Player player = getPlayer();
				if(player!=null){
					player.sendError(Translate.千层塔深渊暂未开启);
				}
				return null;
			}
		}
		Player player2 = getPlayer();
		if(nandu >=1 ){
			if(player2 != null){
				player2.sendError("幽冥幻域困难模式暂不开放");
				return null;
			}
		}
		if (nandu > 2 || nandu < 0) {
			if(player2!=null){
				player2.sendError(Translate.text_qiancengta_难度不存在);
			}
			return null;
		}
		if (nandu == 1) {
			if (maxDao < daoIndex || (maxDao == daoIndex && maxCengInDao < QianCengTa_TaInfo.cengCount - 1)) {
				Player player = getPlayer();
				if(player!=null){
					player.sendError(Translate.translateString(Translate.text_qiancengta_某难度道未开启, new String[][]{{Translate.STRING_1, Translate.text_qiancengta_普通},{Translate.STRING_2, Translate.text_qiancengta_困难}, {Translate.STRING_3, QianCengTa_DaoInfo.QianCengDaoName[daoIndex]}}));
				}
				return null;
			}
		}else if (nandu == 2) {
			int hardIndo = getMaxHardInDao();
			if (hardDaoList != null && hardDaoList.size() > daoIndex) {
				QianCengTa_Dao dao = hardDaoList.get(daoIndex);
				if (dao != null && dao.getMaxReachCengIndex() > hardIndo) {
					hardIndo = dao.getMaxReachCengIndex();
				}
			}
			if (getMaxHardDao() < daoIndex || (getMaxHardDao() == daoIndex && hardIndo < QianCengTa_TaInfo.cengCount - 1)) {
				Player player = getPlayer();
				if(player!=null){
					player.sendError(Translate.translateString(Translate.text_qiancengta_某难度道未开启, new String[][]{{Translate.STRING_1, Translate.text_qiancengta_困难},{Translate.STRING_2, Translate.text_qiancengta_深渊}, {Translate.STRING_3, QianCengTa_DaoInfo.QianCengDaoName[daoIndex]}}));
				}
				return null;
			}
		}
		
		ArrayList<QianCengTa_Dao> daoList = null;
		QianCengTa_TaInfo taInfo = null;
		int maxDao = -1;
		boolean isAuto = true;
		if (nandu == 0) {
			daoList = this.getDaoList();
			maxDao = getMaxDao();
			taInfo = QianCengTa_TaInfo.taInfo;
		}else if (nandu == 1) {
			daoList = this.getHardDaoList();
			maxDao = getMaxHardDao();
			taInfo = QianCengTa_TaInfo.hardTaInfo;
		}else if (nandu == 2) {
			daoList = this.getGulfDaoList();
			maxDao = getMaxGulfDao();
			taInfo = QianCengTa_TaInfo.gulfTaInfo;
			isAuto = false;
		}
		
//		if (nandu == 1 && daoList.size() == 0) {
//			openDao(nandu, 0);
//		}else if (nandu == 2 && daoList.size() == 0) {
//			openDao(nandu, 0);
//		}
		
		if((nandu==0 && daoIndex>maxDao) || daoIndex >= daoList.size()){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, ((daoIndex >= QianCengTa_DaoInfo.QianCengDaoName.length) ? Translate.道不存在 : QianCengTa_DaoInfo.QianCengDaoName[daoIndex] + Translate.尚未开启));
			Player player = getPlayer();
			if(player!=null){
				player.addMessageToRightBag(hreq);
			}
			return null;
		}
		
		QianCengTa_Dao dao = daoList.get(daoIndex);
		if (dao == null) {
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] [getInfo道下标错误] [难度{"+nandu+"}] [status:"+status+"] [daoIndex:"+daoIndex+"]");
			return null;
		}
		int maxReachCengIndex = dao.getMaxReachCengIndex();
		
		int cengIndex = dao.getCurrentCengIndex();
		String daoName = QianCengTa_DaoInfo.QianCengDaoName[daoIndex];
		String[] monster = new String[0];
		String[] rewardRamStr = new String[0];
		String[] rewardStr = new String[0];
		long[] cengRewardIDS = new long[0];
		int[] rewardColors = new int[0];
		String monSb = "";
//		String rewardSb = "";
		if (cengIndex < QianCengTa_TaInfo.cengCount) {
			monster = taInfo.getCengInfo(daoIndex,cengIndex).getMonsterNames();
			for (int i = 0; i < monster.length; i++) {
				monSb = monSb + monster[i] + ",";
			}
			rewardStr = taInfo.getCengInfo(daoIndex, cengIndex).getRewardNames();
			rewardRamStr = taInfo.getCengInfo(daoIndex, cengIndex).getRewardRandomNames();
			rewardColors = taInfo.getCengInfo(daoIndex, cengIndex).getRewardcolorTypes();
			cengRewardIDS = taInfo.getCengInfo(daoIndex, cengIndex).getCengRewardIDs();
//			for (int i = 0; i < rewardStr.length; i++) {
//				rewardSb = rewardSb + rewardStr[i] + ",";
//			}
		}else if (cengIndex == QianCengTa_TaInfo.cengCount) {
			rewardStr = taInfo.getCengInfo(daoIndex, 24).getRewardNames();
			rewardRamStr = taInfo.getCengInfo(daoIndex, 24).getRewardRandomNames();
			rewardColors = taInfo.getCengInfo(daoIndex, 24).getRewardcolorTypes();
			cengRewardIDS = taInfo.getCengInfo(daoIndex, 24).getCengRewardIDs();
		}
		boolean[] isOpen = new boolean[daoList.size()];
		for (int i = 0; i < isOpen.length; i++) {
			isOpen[i] = true;
		}
		boolean ifHaveR = false;
		for (int i= 0 ; i < dao.getCengList().size(); i++) {
			if (dao.getCengList().get(i).getRewards().size() > 0) {
				ifHaveR = true;
				break;
			}
		}
		if (dao.getHiddenCeng() != null && dao.getHiddenCeng().getRewards().size() > 0) {
			ifHaveR = true;
		}
		boolean isOpenHidden = dao.isOpenHidden();
		if (dao.getHiddenCeng()!= null && dao.getHiddenCeng().rewardStatus == QianCengTa_Ceng.STATUS_奖励未领取) {
			isOpenHidden = false;
		}
		setDefault_DaoIndex(daoIndex);
		setNandu(nandu);
		RequestMessage req = null;
		if (isNew()) {
			long[] ids = taInfo.daoList.get(daoIndex).getTempEntityIds();
			long[] fids = taInfo.daoList.get(daoIndex).getFirstEntityIDs();
			int[] fNums = taInfo.daoList.get(daoIndex).getFirstRewardArticleNums();
			req = new NEWQIANCENGTA_OPEN_INFO_REQ(GameMessageFactory.nextSequnceNum(), nandu, isAuto, isOpen, daoIndex, daoName, cengIndex, QianCengTa_TaInfo.cengCount,isOpenHidden, maxReachCengIndex, monster, cengRewardIDS, getAuthority()[nandu][daoIndex].getUsedNum(), getAuthority()[nandu][daoIndex].getConfig().defaultNum, AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), authority_Type[nandu][daoIndex]), taInfo.getCostSilver(getTotalFlushDaoTimes2Nandu(nandu)[daoIndex]), status==STATUS_AUTO_PA_TA, ifHaveR, ids, fids, fNums, isGetFirst[nandu][daoIndex]);
			logger.warn("[新Info] [{}] [{}]", new Object[]{getPlayer().getLogString(), getLogString(0)});
		}else {
			req = new QIANCENGTA_OPEN_INFO_REQ(GameMessageFactory.nextSequnceNum(), isOpen, daoIndex, daoName, cengIndex, QianCengTa_TaInfo.cengCount,isOpenHidden, maxReachCengIndex, monster, rewardRamStr, rewardStr, rewardColors, getAuthority()[nandu][daoIndex].getUsedNum(), getAuthority()[nandu][daoIndex].getConfig().defaultNum, AuthorityAgent.getInstance().getAuthorityMaxNum(getPlayer(), authority_Type[nandu][daoIndex]), taInfo.getCostSilver(getTotalFlushDaoTimes2Nandu(nandu)[daoIndex]), status==STATUS_AUTO_PA_TA, ifHaveR);
			logger.warn("[QIANCENGTA_OPEN_INFO_REQ] [{}] [{}]", new Object[]{getPlayer().getLogString(), getLogString(0)});
		}
		if(logger.isWarnEnabled()){
			logger.warn("[获取塔信息] [成功] {} {} [难度:{}] [道层:{}/{}] [道开启:{}] [隐藏:{}] [道最高层:{}] [奖励:{}]", new Object[]{getPlayer().getLogString(), getLogString(nandu), nandu, daoIndex, cengIndex, isOpen.length, isOpenHidden, maxReachCengIndex, ifHaveR});
		}
		return req;
	}
	
	/**
	 * 通过难度 道 层 得到  层的info
	 * @param nandu
	 * @param daoIndex
	 * @param cengIndex
	 * @return
	 */
	public QianCengTa_CengInfo getCengInfo(int nandu, int daoIndex, int cengIndex) {
		if (nandu == 0) {
			return QianCengTa_TaInfo.taInfo.getCengInfo(daoIndex, cengIndex);
		}else if (nandu == 1) {
			return QianCengTa_TaInfo.hardTaInfo.getCengInfo(daoIndex, cengIndex);
		}else if (nandu == 2) {
			return QianCengTa_TaInfo.gulfTaInfo.getCengInfo(daoIndex, cengIndex);
		}
		return null;
	}
	
	public synchronized int msg_flush(Player player, int nandu,int daoIndex){
		if (nandu < 0 || nandu > 2) {
			return 2;
		}
		QianCengTa_TaInfo taInfo = null;
		if (nandu == 0) {
			taInfo = QianCengTa_TaInfo.taInfo;
		}else if (nandu == 1) {
			taInfo = QianCengTa_TaInfo.hardTaInfo;
		}else if (nandu == 2) {
			taInfo = QianCengTa_TaInfo.gulfTaInfo;
		}
		if (daoIndex < 0 || daoIndex > 5) {
			logger.error("[刷新下标不正确] [{}] [{}] [{}] [难度{}]", new Object[]{player.getLogString(), daoIndex, getLogString(nandu), nandu});
		}
		if(player == null){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动刷新] [失败] [状态不对，玩家已经在爬塔] [难度"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"]");
			return 5;
		}
		if(status != STATUS_IDLE){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动刷新] [失败] [状态不对，玩家已经在爬塔] [难度"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
			return 1;
		}
		
		QianCengTa_Dao dao = getDao(nandu, daoIndex);
		
		if(dao==null){
			logger.warn("[千层塔] [手动刷新] [失败] [道不存在] [status:"+status+"] [难度"+nandu+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
			return 2;
		}
		
		if (dao.getCurrentCengIndex() <= 0) {
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动刷新] [失败] [对应道还没有一层是手动打通过] [难度"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
			player.sendError(Translate.text_qiancengta_您还未打过这道塔不需要刷新);
			return 3;
		}
		
		if(dao.getMaxReachCengIndex() < 0){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动刷新] [失败] [对应道还没有一层是手动打通过] [难度"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
			return 3;
		}
		boolean isMax = false;
		boolean isFree = true;
		try {
			authority[nandu][daoIndex].canUse();
		} catch (TotalNumAccessedException e) {
			isFree = false;
		} catch (MaxNumAccessedException e) {
			isMax = true;
		}
		if(isMax){
			if(logger.isWarnEnabled()) {
				logger.warn("[千层塔] [手动刷新] [失败] [超过今日最大上限] [难度"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
			}
			if (player.getVipLevel() >= 10) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_qiancengta_刷新超过今日最大上限);
				player.addMessageToRightBag(nreq);
			}else {
				WindowManager wm = WindowManager.getInstance();
				MenuWindow mw = wm.createTempMenuWindow(600);
				mw.setDescriptionInUUB(Translate.text_qiancengta_VIP等级不够充值);
				Option_ChongZhi option1 = new Option_ChongZhi();
				option1.setText(Translate.去充值);
				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.取消);
				Option[] options = new Option[] {option1, cancel };
				mw.setOptions(options);
				CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
				player.addMessageToRightBag(creq);
			}
			return 4;
		}
		
		if(getAuthority()[nandu][daoIndex].getUsedNum() < 0){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动刷新] [失败] [刷新次数为负数] [难度"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
			return 6;
		}
		boolean ifHaveR = false;
		for (int i= 0 ; i < dao.getCengList().size(); i++) {
			QianCengTa_Ceng ceng = dao.getCengList().get(i);
			if (ceng.getRewards().size() > 0 && ceng.rewardStatus == QianCengTa_Ceng.STATUS_奖励未领取) {
				ifHaveR = true;
				break;
			}
		}
		String nS = "";
		if (nandu == 0) {
			nS = Translate.text_qiancengta_普通;
		}else if (nandu == 1) {
			nS = Translate.text_qiancengta_困难;
		}else if (nandu == 2) {
			nS = Translate.text_qiancengta_深渊;
		}
		String dN = Translate.text_qiancengta_DaoNames[daoIndex];
		
		if (ifHaveR) {
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			String msg = Translate.text_qiancengta_005;
			if (!isFree) {
				msg = msg + Translate.text_qiancengta_006 + Translate.translateString(Translate.text_qiancengta_007, new String[][]{{Translate.STRING_1, nS}, {Translate.STRING_2, dN}}) + TradeManager.putMoneyToMyText(taInfo.getCostSilver(getTotalFlushDaoTimes2Nandu(nandu)[daoIndex]));
			}else {
				msg = msg + Translate.translateString(Translate.text_qiancengta_106, new String[][]{{Translate.STRING_1, nS}, {Translate.STRING_2, dN}});
			}
			mw.setDescriptionInUUB(msg);
			Option_NewQianCengFlush flush = new Option_NewQianCengFlush(nandu, daoIndex);
			Option_Cancel cancel = new Option_Cancel();
			flush.setText(Translate.确定);
			cancel.setText(Translate.取消);
			mw.setOptions(new Option[] { flush, cancel });
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
			player.addMessageToRightBag(creq);
		}else {
			if (!isFree) {
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				mw.setDescriptionInUUB(Translate.translateString(Translate.text_qiancengta_007, new String[][]{{Translate.STRING_1, nS}, {Translate.STRING_2, dN}})+TradeManager.putMoneyToMyText(taInfo.getCostSilver(getTotalFlushDaoTimes2Nandu(nandu)[daoIndex]))+Translate.text_qiancengta_006);
				Option_NewQianCengFlush flush = new Option_NewQianCengFlush(nandu, daoIndex);
				Option_Cancel cancel = new Option_Cancel();
				flush.setText(Translate.确定);
				cancel.setText(Translate.取消);
				mw.setOptions(new Option[] { flush, cancel });
				CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
				player.addMessageToRightBag(creq);
			}else{
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				mw.setDescriptionInUUB(Translate.translateString(Translate.text_qiancengta_026, new String[][]{{Translate.STRING_1, nS}, {Translate.STRING_2, dN}}));
				Option_NewQianCengFlush flush = new Option_NewQianCengFlush(nandu, daoIndex);
				Option_Cancel cancel = new Option_Cancel();
				flush.setText(Translate.确定);
				cancel.setText(Translate.取消);
				mw.setOptions(new Option[] { flush, cancel });
				CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
				player.addMessageToRightBag(creq);
			}
		}
		return 0;
	}
	
	/**
	 * 手动刷新
	 * 返回值定义：
	 * 		0，标识，手动刷新成功
	 *      1，标识，状态不对，玩家已经在爬塔
	 *      2，标识，道下标越界
	 *      3，标识，对应道还没有一层是手动打通过
	 *      4，标识，超过今日最大上限
	 *      5，标识，塔错误，玩家不存在
	 *      6，标识，刷新次数为负数
	 *      7，标识，玩家银子不足
	 *      8，标识，有层的奖励未领取
	 * @param daoIndex
	 * @return
	 */
	public synchronized int ope_flush(Player player, int nandu,int daoIndex){
		if (nandu < 0 || nandu > 2) {
			return 2;
		}
		QianCengTa_TaInfo taInfo = null;
		if (nandu == 0) {
			taInfo = QianCengTa_TaInfo.taInfo;
		}else if (nandu == 1) {
			taInfo = QianCengTa_TaInfo.hardTaInfo;
		}else if (nandu == 2) {
			taInfo = QianCengTa_TaInfo.gulfTaInfo;
		}
		if (daoIndex < 0 || daoIndex > 5) {
			logger.error("[刷新千层塔] [道下标不正确] [{}] [{}] [{}]", new Object[]{player.getLogString(), daoIndex, getLogString(nandu)});
		}
		if(player == null){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动刷新] [失败] [状态不对，玩家已经在爬塔] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"]");
			return 5;
		}
		if(status != STATUS_IDLE){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动刷新] [失败] [状态不对，玩家已经在爬塔] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
			return 1;
		}
		
		QianCengTa_Dao dao = getDao(nandu, daoIndex);
		
		if(dao==null){
			return 2;
		}
		
		if(dao.getMaxReachCengIndex() < 0){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动刷新] [失败] [对应道还没有一层是手动打通过] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
			return 3;
		}
		boolean isFree = true;
		boolean isMax = false;
		try {
			authority[nandu][daoIndex].canUse();
		} catch (TotalNumAccessedException e1) {
			isFree = false;
		} catch (MaxNumAccessedException e1) {
			isMax = true;
		}
		if(isMax){
			if(logger.isWarnEnabled()){
				logger.warn("[千层塔] [手动刷新] [失败] [超过今日最大上限] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
			}
			HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_qiancengta_刷新超过今日最大上限);
			player.addMessageToRightBag(nreq);
			return 4;
		}
		
		if(getAuthority()[nandu][daoIndex].getUsedNum() < 0){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动刷新] [失败] [刷新次数为负数] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
			return 6;
		}
		
		if(isFree){
			setTotalFlushDaoTimes2Index(nandu, daoIndex, 1);
			setTotalCostDaoSilver2Index(daoIndex, 0);
			AuthorityAgent.getInstance().notifyPlayerUsed(player, authority_Type[nandu][daoIndex]);
			dao.flush();
			if (logger.isWarnEnabled())
				logger.warn("[千层塔] [免费刷新] [成功] [p={}] [dIndex={}] [dcu={}] [dmx={}] [st={}] [次数={}]", new Object[]{player.getLogString(), dao.getDaoIndex(), dao.getCurrentCengIndex(), dao.getMaxReachCengIndex(), status, getAuthority()[nandu][daoIndex].getUsedNum()});
		}else{
			//需要扣除玩家的银子
			BillingCenter bc = BillingCenter.getInstance();
			if(bc == null){
				if(logger.isWarnEnabled()){
					logger.warn("[千层塔] [手动刷新] [失败] [BillingCenter为空] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
				}
				return 7;
			}
			try {
				long m = taInfo.getCostSilver(getTotalFlushDaoTimes2Nandu(nandu)[daoIndex]);
				bc.playerExpense(player, m, CurrencyType.SHOPYINZI, ExpenseReasonType.QIANCENGTA_FLUSH, "千层塔刷新");
				QianCengTaManager.getInstance().doRefDaoActivity(player, nandu, m);
			} catch (NoEnoughMoneyException e) {
				if(logger.isWarnEnabled())
					logger.warn("[千层塔] [手动刷新] [失败] [余额不足] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]");
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_qiancengta_刷新需要的银子不够);
				player.addMessageToRightBag(nreq);
				return 7;
			} catch (BillFailedException e) {
				if(logger.isWarnEnabled())
					logger.warn("[千层塔] [手动刷新] [失败] [扣费出现异常] [status:"+status+"] [daoIndex:"+daoIndex+"] [current:"+getAuthority()[nandu][daoIndex].getUsedNum()+"] [" + player.getLogString()+"]",e);
				return 7;
			}
			setTotalCostDaoSilver2Index(daoIndex, totalCostDaoSilver[daoIndex] + taInfo.getCostSilver(getTotalFlushDaoTimes2Nandu(nandu)[daoIndex]));
			setTotalFlushDaoTimes2Index(nandu, daoIndex, getTotalFlushDaoTimes2Nandu(nandu)[daoIndex] + 1);
			AuthorityAgent.getInstance().addAuthorityNum(authority_Type[nandu][daoIndex], player);
			AuthorityAgent.getInstance().notifyPlayerUsed(player, authority_Type[nandu][daoIndex]);
			dao.flush();
			if (nandu == 0) {
				notifyChanager("daoList");
			}else if (nandu == 1) {
				notifyChanager("hardDaoList");
			}else if (nandu == 2) {
				notifyChanager("gulfDaoList");
			}
			status = STATUS_IDLE;
			lastStatusChangedTime = SystemTime.currentTimeMillis();
			if (logger.isWarnEnabled())
				logger.warn("[千层塔] [手动刷新] [成功] [p={}] [dIndex={}] [dcu={}] [dmx={}] [m={}] [st={}] [次数={}]", new Object[]{player.getLogString(), dao.getDaoIndex(), dao.getCurrentCengIndex(), dao.getMaxReachCengIndex(), taInfo.getCostSilver(getTotalFlushDaoTimes2Nandu(nandu)[daoIndex]), status, getAuthority()[nandu][daoIndex].getUsedNum()});
		}
		RequestMessage openInfo = getInfo(nandu, daoIndex);
		if(openInfo!=null){
			player.addMessageToRightBag(openInfo);
		}
		return 0;
	}
	
	public ArrayList<QianCengTa_Dao> getDaoList2Nandu(int nandu) {
		ArrayList<QianCengTa_Dao> daoList = null;
		if (nandu == 0) {
			daoList = this.getDaoList();
		}else if (nandu == 1) {
			daoList = this.getHardDaoList();
		}else if (nandu == 2) {
			daoList = this.getGulfDaoList();
		}
		return daoList;
	}
	
	public QianCengTa_Dao getDao(int nandu, int daoIndex){
		ArrayList<QianCengTa_Dao> daoList = getDaoList2Nandu(nandu);
		if(daoIndex < 0||daoIndex >= daoList.size()){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [获取状态信息] [道数组下标越界] [难度"+nandu+"]  [daoIndex:"+daoIndex+"] [" + daoList.size() +"]");
			return null;
		}
		return daoList.get(daoIndex);
	}
	
	/**
	 * 自动爬塔
	 * 玩家发起自动爬塔的请求，此请求可以是离线发起
	 * 返回值定义：
	 * 		0，标识，自动爬塔开始
	 *      1，标识，状态不对，玩家已经在爬塔
	 *      2，标识，道下标越界
	 *      3，标识，对应道还没有一层是手动打通过
	 *      4，标识，已经达到本道的最高层，刷新后才能自动爬
	 *      5，标识，塔错误，玩家不存在
	 *      6,标识，自动爬塔要爬的层有奖励未领取
	 *      7 塔计算奖励出错
	 *      8 自动爬塔速度过快
	 *      9 深渊不能自动爬塔
	 * @param daoIndex
	 * @return
	 */
	
	public synchronized int autoPaTa(Player player, int nandu,int daoIndex){
		if(player == null){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [自动爬塔] [失败] [状态不对，玩家已经在爬塔] [难度:"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"]");
			return 5;
		}
		if(status != STATUS_IDLE && status != STATUS_AUTO_PA_TA){
			player.sendError(Translate.text_qiancengta_008);
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [自动爬塔] [失败] [状态不对，玩家已经在爬塔] [难度:"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [" + player.getLogString()+"]");
			return 1;
		}
		if (nandu == 2) {
			player.sendError(Translate.text_qiancengta_深渊难度不能自动爬塔);
			return 9;
		}
		QianCengTa_Dao dao = getDao(nandu, daoIndex);
		
		if(dao == null){
			player.sendError(Translate.text_qiancengta_009);
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [自动爬塔] [失败] [道下标错误] [难度:"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [" + player.getLogString()+"]");
			return 2;
		}
		
		
		if(dao.getMaxReachCengIndex() < 0){
			player.sendError(Translate.text_qiancengta_010);
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [自动爬塔] [失败] [对应道还没有一层是手动打通过] [难度:"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+dao.getCurrentCengIndex() +"] [cengIndex:"+dao.getMaxReachCengIndex() +"] [" + player.getLogString()+"]");
			return 3;
		}
		
		if(dao.getCurrentCengIndex() < 0 || dao.getCurrentCengIndex() > dao.getMaxReachCengIndex()){
			player.sendError(Translate.text_qiancengta_010);
			status =STATUS_IDLE;
			lastStatusChangedTime = SystemTime.currentTimeMillis();
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [自动爬塔] [失败] [已经达到本道的最高层，刷新后才能自动爬] [难度:"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+dao.getCurrentCengIndex() +"] [cengIndex:"+dao.getMaxReachCengIndex() +"] [" + player.getLogString()+"]");
			return 4;
		}
		
		if (status == STATUS_AUTO_PA_TA && SystemTime.currentTimeMillis() - lastStatusChangedTime < AUTO_PA_TA_SPACE) {
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [自动爬塔] [失败] [怀疑是外挂] [难度:"+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+dao.getCurrentCengIndex() +"] [cengIndex:"+dao.getMaxReachCengIndex() +"] [" + player.getLogString()+"] [" + (SystemTime.currentTimeMillis() - lastStatusChangedTime) + "]");
			status =STATUS_IDLE;
			lastStatusChangedTime = SystemTime.currentTimeMillis();
			return 8;
		}

		setNandu(nandu);
		status = STATUS_AUTO_PA_TA;
		lastStatusChangedTime = SystemTime.currentTimeMillis();
		
		QianCengTa_Ceng ceng = dao.getCeng(dao.getCurrentCengIndex());
		if (ceng == null) {
			ceng = new QianCengTa_Ceng();
			ceng.setCengIndex(dao.getCurrentCengIndex());
			ceng.dao = dao;
			dao.getCengList().add(ceng);
		}
		ceng.setNandu(nandu);
		
		try {
			ceng.calculateFlopByPaTa();
		} catch (Exception e) {
			logger.error("自动爬塔计算奖励出错:[" + dao.getCurrentCengIndex() + "] [" + dao.getCengList().size() + "]", e);
			return 7;
		}
		
		dao.setCurrentCengIndex(dao.getCurrentCengIndex() + 1);
		//活跃度统计
		ActivenessManager.getInstance().record(player, ActivenessType.幽冥幻域);
		if (dao.getCurrentCengIndex() < QianCengTa_TaInfo.cengCount) {
			dao.openCeng();
		}
		
		if (!dao.isOpenHidden()) {
			Random ran = new Random();
			int next = QianCengTa_TaInfo.QianCengHideRandom1[daoIndex] + QianCengTa_TaInfo.QianCengHideRandom2[daoIndex] * ceng.getCengIndex();
			if (next > ran.nextInt(QianCengTa_TaInfo.HideRandom)) {
				dao.setOpenHidden(true);
				QianCengTa_Ceng hiddenCeng = new QianCengTa_Ceng();
				hiddenCeng.setNandu(nandu);
				hiddenCeng.dao = dao;
				hiddenCeng.setCengIndex(-1);
				dao.setHiddenCeng(hiddenCeng);
				player.send_HINT_REQ(Translate.text_qiancengta_恭喜你开启隐藏层, (byte)2);
				if (logger.isWarnEnabled())
					logger.warn("[隐藏层开启] [p={}] [daoIndex={}] [ceng={}] [难度={}]", new Object[]{getPlayerId(), daoIndex, ceng.getCengIndex(), nandu});
			}
		}
		if (nandu == 0) {
			notifyChanager("daoList");
		}else if (nandu == 1) {
			notifyChanager("hardDaoList");
		}else if (nandu == 2) {
			notifyChanager("gulfDaoList");
		}
		NEWQIANCENGTA_AUTO_PATA_RES res = new NEWQIANCENGTA_AUTO_PATA_RES(GameMessageFactory.nextSequnceNum(), nandu, 1, 0, "");
		player.addMessageToRightBag(res);
		
		if (dao.getCurrentCengIndex() > dao.getMaxReachCengIndex() || dao.getCurrentCengIndex() == QianCengTa_TaInfo.cengCount) {
			status = STATUS_IDLE;
			lastStatusChangedTime = SystemTime.currentTimeMillis();
			if (logger.isWarnEnabled())
				logger.warn("[千层塔] [自动爬塔停止] [p={}] [dapIndex={}] [max={}] [curr={}]", new Object[]{getPlayerId(), daoIndex, dao.getMaxReachCengIndex(), dao.getCurrentCengIndex()});
		}
		
		RecordAction action = null;
		try {
			if(ceng.dao.getMaxReachCengIndex() >= QianCengTa_TaInfo.cengCount -1){
				if(daoIndex < QianCengTa_TaInfo.daoCount -1){
					if (nandu == 0) {
//						openDao(nandu, daoIndex + 1);
//						openDao(nandu+1, daoIndex);
						if(daoIndex < PlayerAimManager.幽冥幻域普通action.length) {
							action = PlayerAimManager.幽冥幻域普通action[daoIndex];
						}
					}else if (nandu == 1) {
//						openDao(nandu+1, daoIndex);
						if(daoIndex < PlayerAimManager.幽冥幻域困难action.length) {
							action = PlayerAimManager.幽冥幻域困难action[daoIndex];
						}
					}else if (nandu == 2) {
						if(daoIndex < PlayerAimManager.幽冥幻域深渊action.length) {
							action = PlayerAimManager.幽冥幻域深渊action[daoIndex];
						}
					}
				}else if (daoIndex == QianCengTa_TaInfo.daoCount -1){
					if (nandu == 0) {
//						openDao(nandu+1, daoIndex);
						if(daoIndex < PlayerAimManager.幽冥幻域普通action.length) {
							action = PlayerAimManager.幽冥幻域普通action[daoIndex];
						}
					}else if (nandu == 1) {
//						openDao(nandu+1, daoIndex);
						if(daoIndex < PlayerAimManager.幽冥幻域困难action.length) {
							action = PlayerAimManager.幽冥幻域困难action[daoIndex];
						}
					}else if (nandu == 2) {
						if(daoIndex < PlayerAimManager.幽冥幻域深渊action.length) {
							action = PlayerAimManager.幽冥幻域深渊action[daoIndex];
						}
					}
				}
			}
		} catch (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [统计通过幽冥幻域(自动爬塔)异常][" + player.getLogString() + "]", e);
		}
		try {
			if (PlayerAimManager.logger.isDebugEnabled()) {
				PlayerAimManager.logger.debug("[自动爬塔] [千层塔] [目标统计] [ceng.dao.getMaxReachCengIndex():" + ceng.dao.getMaxReachCengIndex() + "] [难度:"+nandu+"] [daoIndex:"+daoIndex+"]  [CC="+ceng.getCengIndex()+"] [cengIndex:"+dao.getCurrentCengIndex() +"] [" + player.getLogString() + "]" );
			}
			if(action != null) {
				EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), action, 1L});
				EventRouter.getInst().addEvent(evt3);
				if (PlayerAimManager.logger.isDebugEnabled()) {
					PlayerAimManager.logger.debug("[自动爬塔] [千层塔] [目标统计] [actionType:" + action.getType() + "] [" + player.getLogString() + "]");
				}
			}
		} catch (Exception eex) {
			PlayerAimManager.logger.error("[目标系统] [统计通过幽冥幻域异常][" + player.getLogString() + "]", eex);
		}
		
		if(logger.isWarnEnabled())
			logger.warn("[千层塔] [自动爬塔] [成功] [ok] [status:"+status+"] [难度:"+nandu+"] [daoIndex:"+daoIndex+"] [CC="+ceng.getCengIndex()+"] [cengIndex:"+dao.getCurrentCengIndex() +"] [cengIndex:"+dao.getMaxReachCengIndex() +"] [" + player.getLogString()+"]");
	
		return 0;
	}
	
	/**
	 * 玩家触发，停止自动爬塔
	 */
	public synchronized void stopAutoPaTa(Player player, int nandu){
		if(player == null) return;
		if(status != STATUS_AUTO_PA_TA){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [停止自动爬塔] [失败] [状态不对，玩家没有在自动爬塔] [难度:"+nandu+"] [status:"+status+"] [" + player.getLogString()+"]");
		}else{
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [停止自动爬塔] [成功] [难度:"+nandu+"] [status:"+status+"] [" + player.getLogString()+"]");
			status = STATUS_IDLE;
			lastStatusChangedTime = SystemTime.currentTimeMillis();
		}
	}
	
	/**
	 * 手动爬塔
	 * 玩家发起自动爬塔的请求，此请求可以是离线发起
	 * 返回值定义：
	 * 		0，标识，成功，调用者需要发送协议让玩家跳地图，有效期为3分钟，如果3分钟内玩家没有进入塔，自动恢复层空闲状态
	 *      1，标识，状态不对，玩家已经在爬塔
	 *      2，标识，道下标越界
	 *      3，标识，本道还没有开启
	 *      4，标识，已经达到本道的最高层，刷新后才能自动爬
	 *      5，标识，塔错误，玩家不存在
	 *      6，标识，玩家不在线
	 *      7，标识，对应的层信息不存在
	 *      8，标识，对应的地图不存在
	 *      9，标识，玩家不在任何一张地图上
	 *      10，标识，出现未知错误
	 *      11，标识，对应的层有奖励未领取
	 * @param daoIndex
	 * @return
	 */
	public synchronized int manualPaTa(Player player, int nandu,int daoIndex, int cengIndex){
		ArrayList<QianCengTa_Dao> daoList = getDaoList2Nandu(nandu);
		if(player == null){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] [状态不对，玩家已经在爬塔] ["+nandu+"] [status:"+status+"] [cengIndex:"+cengIndex +"] [daoIndex:"+daoIndex+"]");
			return 5;
		}
		if(status != STATUS_IDLE){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] [状态不对，玩家已经在爬塔] ["+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex +"] [" + player.getLogString()+"]");
			return 1;
		}
		
		if(daoIndex < 0 || daoIndex >=daoList.size()){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] [道下标错误] ["+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex +"] [" + player.getLogString()+"]");
			return 2;
		}
		
		if (player.getLevel() < QianCengTa_TaInfo.QianCengTaLevel[daoIndex]) {
			player.sendError(Translate.text_qiancengta_需要 + (QianCengTa_TaInfo.QianCengTaLevel[daoIndex]) + Translate.text_qiancengta_级才能挑战 + QianCengTa_DaoInfo.QianCengDaoName[daoIndex]);
			return 2;
		}
		
		QianCengTa_Dao dao = getDao(nandu, daoIndex);
		
		if (nandu > 0) {
			QianCengTa_Dao lastDao = getDao(nandu-1, daoIndex);
			if (lastDao.getMaxReachCengIndex() != QianCengTa_TaInfo.cengCount - 1) {
				player.sendError(Translate.text_qiancengta_您还未打通上个难度的这道);
				return 2;
			}
		}else if (daoIndex > 0 && nandu == 0) {
			QianCengTa_Dao lastDao = getDao(nandu, daoIndex-1);
			if (lastDao.getMaxReachCengIndex() != QianCengTa_TaInfo.cengCount - 1) {
				player.sendError(Translate.text_qiancengta_您还未打通上个难度的这道);
				return 2;
			}
		}
		
		if(dao.getCurrentCengIndex() >= QianCengTa_TaInfo.cengCount && cengIndex >= 0){
			player.sendError(Translate.text_qiancengta_011);
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] [本道已经达到最高层] ["+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [" + player.getLogString()+"]");
			return 4;
		}
		
		if(player.isOnline() == false){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] [玩家不在线] ["+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex +"] [" + player.getLogString()+"]");
			return 6;
		}
		
		if (cengIndex <= dao.getMaxReachCengIndex() && cengIndex >= 0 && nandu < 2) {
			player.sendError(Translate.text_qiancengta_012);
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] [应该自动爬塔] ["+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [maxCeng:"+ dao.getMaxReachCengIndex() +"] [cengIndex:"+ cengIndex +"] [" + player.getLogString()+"]");
			return 4;
		}
		
		QianCengTa_Ceng ceng = null;
		if (cengIndex < 0) {
			ceng = dao.getHiddenCeng();
			if (ceng == null) {
				player.sendError(Translate.text_qiancengta_隐藏层未开启);
				logger.error("[千层塔] [手动爬塔隐藏层不存在] [{}] [{}]",new Object[]{player.getLogString(), getLogString(0)});
				return 4;
			}
		}else {
			ceng = dao.getCeng(dao.getCurrentCengIndex());
			if (ceng == null) {
				ceng = new QianCengTa_Ceng();
				ceng.setCengIndex(dao.getCurrentCengIndex());
				dao.getCengList().add(ceng);
			}
		}
		ceng.dao = dao;
		ceng.setNandu(nandu);
		
		if (cengIndex != ceng.getCengIndex()) {
			player.sendError(Translate.text_qiancengta_爬塔数据不正确);
			logger.error("[千层塔] [手动爬塔] [失败] ["+nandu+"] [C={}] [CC={}] [DMC={}] [D={}] [DC={}] [p={}]", new Object[]{cengIndex, ceng.getCengIndex(), dao.getMaxReachCengIndex(), dao.getDaoIndex(), dao.getCurrentCengIndex(), player.getLogString()});
			return 4;
		}
		
		QianCengTa_CengInfo ci = getCengInfo(nandu, dao.getDaoIndex(), ceng.cengIndex);
		
		if(ci == null){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] ["+nandu+"] [对应的层("+dao.getCurrentCengIndex()+")信息不存在] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex +"] [" + player.getLogString()+"]");
			return 7;
		}
		
		if(ceng.rewardStatus == QianCengTa_Ceng.STATUS_奖励未领取){
			player.sendError(Translate.text_qiancengta_005);
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] [对应的层有奖励未领取] ["+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+ceng.getCengIndex() +"] [" + player.getLogString()+"]");
			return 11;
		}
		
		setNandu(nandu);
		
		GameInfo gi = null;
		GameManager gm = GameManager.getInstance();
		if(gm != null){
			gi = gm.getGameInfo(ci.getCengMapName());
		}
		if(gi == null){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] ["+nandu+"] [对应的地图("+ci.getCengMapName()+")不存在] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex +"] [" + player.getLogString()+"]");
			return 8;
		}
		
		if(player.getCurrentGame() == null){
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [失败] [玩家不在任何一张地图上] ["+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex +"] [" + player.getLogString()+"]");
			return 9;
		}
		String oldN = "";
		if (game != null){
			oldN = game.gi.name;
		}
		
		if (player.isFlying()) {
			player.downFromHorse();
		}
		logger.warn("[千层塔] [手动爬塔地图] ["+nandu+"] [d={}] [g={}] [old={}] [new={}] [{}]", new Object[]{ceng.dao, game!= null, oldN ,gi.name, getLogString(0)});
		if (game != null && game.gi.name.equals(gi.name)) {
			monsterList.clear();
			game.setQianCengTa(ceng);
			
			if (game.contains(player)) {
				status = STATUS_MANUAL_PA_TA_FIGHTING;
				lastStatusChangedTime  = SystemTime.currentTimeMillis();
			}else {
				status = STATUS_MANUAL_PA_TA_WAITING_ENTER_GAME;
				lastStatusChangedTime = SystemTime.currentTimeMillis();
			}
			
			String mapName =  QianCengTa_DaoInfo.QianCengDaoName[game.getQianCengTa().dao.getDaoIndex()] + "●";
			mapName = mapName + (game.getQianCengTa().getCengIndex() + 1) + Translate.text_qiancengta_004;
			CHANGE_GAME_DISPLAYNAME_REQ req1 = new CHANGE_GAME_DISPLAYNAME_REQ(GameMessageFactory.nextSequnceNum(), game.gi.name, mapName);
			player.addMessageToRightBag(req1);
			ResponseMessage res = null;
			if (isNew()) {
				res = new NEWQIANCHENGTA_MANUAL_MSG_RES(GameMessageFactory.nextSequnceNum(), getNandu(), ceng.dao.getDaoIndex(), QianCengTa_DaoInfo.QianCengDaoName[ceng.dao.getDaoIndex()], ceng.getCengIndex(), getCengInfo(nandu, ceng.dao.getDaoIndex(), ceng.getCengIndex()).getStartInfo());
			}else {
				res = new QIANCHENGTA_MANUAL_MSG_RES(GameMessageFactory.nextSequnceNum(), ceng.dao.getDaoIndex(), QianCengTa_DaoInfo.QianCengDaoName[ceng.dao.getDaoIndex()], ceng.getCengIndex(), getCengInfo(nandu, ceng.dao.getDaoIndex(), ceng.getCengIndex()).getStartInfo());
			}
			player.addMessageToRightBag(res);
			
			MapArea area = game.gi.getMapAreaByName(Translate.千层塔);
			int bornX = 300;
			int bornY = 300;
			if (area != null) {
				Random random = new Random();
				bornX = area.getX()+random.nextInt(area.getWidth());
				bornY = area.getY() + random.nextInt(area.getHeight());
			}
			TransportData transportData = new TransportData(0,0,0,0,ci.getCengMapName(),bornX,bornY);
			
			//提示玩家跳地图，进入塔
			player.getCurrentGame().transferGame(player, transportData);
			
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [相同地图成功] [ok] ["+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex +"] [层"+ceng.cengIndex+"] [" + player.getLogString()+"]");
			return 0;
		}else {
			game = new Game(gm,gi);
			
			try {
				game.init();
				game.setQianCengTa(ceng);
				
			} catch (Exception e) {
				game = null;
				monsterList.clear();
				if(logger.isWarnEnabled())
					logger.warn("[千层塔] [手动爬塔] [失败] ["+nandu+"] [初始化地图("+ci.getCengMapName()+")出现异常] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex +"] [" + player.getLogString()+"]",e);
				return 10;
			}
			
			
			status = STATUS_MANUAL_PA_TA_WAITING_ENTER_GAME;
			lastStatusChangedTime = SystemTime.currentTimeMillis();
			
			boolean isInQianCengMap = false;
			
			for (int i = 0; i < QianCengTa_TaInfo.QianCengMapNames.length; i++) {
				if (QianCengTa_TaInfo.QianCengMapNames[i].equals(player.getCurrentGame().gi.getName())) {
					isInQianCengMap = true;
					break;
				}
			}
			
			if (!isInQianCengMap) {
				this.backGameName = player.getCurrentGame().gi.getName();
				this.backGameCountry = player.getCurrentGame().country;
				this.backX = player.getX();
				this.backY = player.getY();
			}
			
			MapArea area = game.gi.getMapAreaByName(Translate.千层塔);
			int bornX = 300;
			int bornY = 300;
			if (area != null) {
				Random random = new Random();
				bornX = area.getX()+random.nextInt(area.getWidth());
				bornY = area.getY() + random.nextInt(area.getHeight());
			}
			TransportData transportData = new TransportData(0,0,0,0,ci.getCengMapName(),bornX,bornY);
			
			//提示玩家跳地图，进入塔
			player.getCurrentGame().transferGame(player, transportData);
			
			if(logger.isWarnEnabled())
				logger.warn("[千层塔] [手动爬塔] [成功] [ok] ["+nandu+"] [status:"+status+"] [daoIndex:"+daoIndex+"] [cengIndex:"+cengIndex +"] [" + player.getLogString()+"]");
			
		}
		
		return 0;
	}
	
	public String getFirstRewards(Player player, int nandu, int daoIndex) {
		if (isGetFirst[nandu][daoIndex] == true) {
			return Translate.text_qiancengta_领取过首次奖励;
		}
		ArrayList<QianCengTa_Dao> daoList = null;
		QianCengTa_TaInfo tainfo = null;
		if (nandu == 0) {
			daoList = getDaoList();
			tainfo = QianCengTa_TaInfo.taInfo;
		}else if (nandu == 1) {
			daoList = getHardDaoList();
			tainfo = QianCengTa_TaInfo.hardTaInfo;
		}else if (nandu == 2) {
			daoList = getGulfDaoList();
			tainfo = QianCengTa_TaInfo.gulfTaInfo;
		}
		if (daoList.size() < daoIndex) {
			return Translate.text_qiancengta_您还未打通这道;
		}
		QianCengTa_Dao dao = daoList.get(daoIndex);
		if (dao.getMaxReachCengIndex() < QianCengTa_TaInfo.cengCount - 1) {
			return Translate.text_qiancengta_您还未打通这道;
		}
		QianCengTa_DaoInfo daoInfo = tainfo.daoList.get(daoIndex);
		if (player.getKnapsack_common().getEmptyNum() < daoInfo.getFirstRewardArticleNames().length) {
			return Translate.你的背包位置不足不能领取;
		}
		for (int i = 0; i < daoInfo.getFirstRewardArticleNames().length; i++) {
			try {
				
				Article a = ArticleManager.getInstance().getArticle(daoInfo.getFirstRewardArticleNames()[i]);
				ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, daoInfo.getFirstRewardArticleBinds()[i], ArticleEntityManager.CREATE_REASON_QIANCENGTA, player, daoInfo.getFirstRewardArticleColors()[i], daoInfo.getFirstRewardArticleNums()[i], true);
				player.putToKnapsacks(entity, daoInfo.getFirstRewardArticleNums()[i], "千层塔首通奖励");
			} catch(Exception e) {
				logger.warn("领取首次奖励出错 " + player.getLogString(), e);
			}
		}
		isGetFirst[nandu][daoIndex] = true;
		player.sendError(Translate.千层塔领取首次领取奖励成功);
		notifyChanager("isGetFirst");
		return "";
	}
	
	public QianCengTa_TaInfo getTaInfo () {
		if (getNandu() == 0) {
			return QianCengTa_TaInfo.taInfo;
		}else if (getNandu() == 1) {
			return QianCengTa_TaInfo.hardTaInfo;
		}else if (getNandu() == 2) {
			return QianCengTa_TaInfo.gulfTaInfo;
		}
		return null;
	}

	public void setNandu(int nandu) {
		this.nandu = nandu;
	}

	public int getNandu() {
		return nandu;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setIsGetFirst(boolean[][] isGetFirst) {
		this.isGetFirst = isGetFirst;
	}

	public boolean[][] getIsGetFirst() {
		return isGetFirst;
	}

	public void setTotalFlushHardDaoTimes(int[] totalFlushHardDaoTimes) {
		this.totalFlushHardDaoTimes = totalFlushHardDaoTimes;
	}

	public int[] getTotalFlushHardDaoTimes() {
		return totalFlushHardDaoTimes;
	}

	public void setTotalFlushGulfDaoTimes(int[] totalFlushGulfDaoTimes) {
		this.totalFlushGulfDaoTimes = totalFlushGulfDaoTimes;
	}

	public int[] getTotalFlushGulfDaoTimes() {
		return totalFlushGulfDaoTimes;
	}
	
}
