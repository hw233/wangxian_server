package com.fy.engineserver.jiazu.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.constants.InitialPlayerConstant;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.faery.service.QuerySelect;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuCreateException;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuMember4Client;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_WAREHOUSE_RES;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.date.charm.YuanZhenBillboard;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.util.MessageReflectHelper;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.transport.Message;

enum JiazuListOrder {
	name,
	createTime,
	prosperity;
}

public class JiazuManager implements Runnable {

	public static SimpleEntityManager<JiazuMember> memberEm;
	public static SimpleEntityManager<Jiazu> jiazuEm;
	public static SimpleEntityManager<SeptStation> septstationEm;

	// 家族仓库最大数目
	public static int WAREHOUSE_MAX = 100;

	public static Object creat_lock_jiazu = new Object();
	public static Object creat_lock_jiazumember = new Object();

	public final static int SILKWORM_STATUS_TASK = 0;// 养蚕任务期
	public final static int SILKWORM_STATUS_GROWN = 1;// 养蚕成长期
	public final static int SILKWORM_STATUS_PICKUP = 2;// 养蚕摘取期

	public final static int CONTRIBUTION_ADD_BY_BUILDINGTASK = 5;// 建设任务增加的贡献度
	public final static int CONTRIBUTION_ADD_BY_JIAZU_SILVERCAR = 20;// 家族运镖增加的贡献度
	public final static int CONTRIBUTION_ADD_BY_ATTACK_JIAZUBOSS = 100;// 参加攻击家族boss增加的贡献度
	public final static int CONTRIBUTION_ADD_BY_KILL_JIAZUBOSS = 100;// 家族boss最后一击增加的贡献度

	public final static long moneyExchangeContribution = 1000 * 10;// 10两银子兑换一点贡献度

	// BlockingQueue<Pair<Object, Object>> queue = new LinkedBlockingQueue<Pair<Object, Object>>();
	// JiazuDao dao;
	boolean useObjectDb = true;
	private String jiaoSaveFile;
	private boolean enableDebug = false;
	public static Logger logger = JiazuSubSystem.logger;

	public final static int LRU_CACHE_SIZE = 1000;

	public static HashMap<Integer, JiazuTitle> jiazuTitles = new HashMap<Integer, JiazuTitle>();

	private Map<Long, Jiazu> jiazuLruCacheByID = new LinkedHashMap<Long, Jiazu>();

	private Map<String, Jiazu> jiazuLruCacheByName = new LinkedHashMap<String, Jiazu>();

	private Map<Long, Set<JiazuMember>> jiazuMemberLruCacheByID = new LinkedHashMap<Long, Set<JiazuMember>>();

	boolean checkRun = true;
	public static long GAME_SLEEP_TIME = 200;
	public static long MAX_HANDLE_TASKS = 20;
	
	private int nid;
	public synchronized int nextId(){
		return nid++;
	}
	
	public void processMessage(Message msg, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (enableDebug) {
			if (MessageReflectHelper.isContainMenuWindow(msg)) {
				MenuWindow m = MessageReflectHelper.getMenuWindow(msg);
				if (m.getOptions() != null && m.getOptions().length > 0) {
					Option op[] = m.getOptions();
					boolean isjiazOption = false;
					for (Option o : op) {
						if (o.getClass().getName().matches("(?i).*jiazu.*")) {
							isjiazOption = true;
							break;
						}
					}
					if (isjiazOption == false) return;
					if (logger.isInfoEnabled()) logger.info("[家族消息测试] [addMessageToRightBag] [窗口] [玩家名称:{}] [玩家ID:{}] [窗口 title:{}] [窗口 description:{}] [{}ms]", new Object[] { player.getName(), player.getId(), m.getTitle(), m.getDescriptionInUUB(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					int i = 0;
					for (Option o : op) {
						if (logger.isInfoEnabled()) logger.info("[家族消息测试] [addMessageToRightBag] [窗口] [option] [玩家名称:{}] [玩家ID:{}] [option 总个数:{}] [当前option:{}] [optionText:{}] [optionID: {}] [optionOid:{}] [{}ms]", new Object[] { player.getName(), player.getId(), op.length, (i++), o.getText(), o.getOptionId(), o.getOId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
						if (o instanceof Option_Cancel) continue;
						else {
							o.doSelect(null, player);
						}
					}
					
				}
			} else {
				if (!msg.getClass().getName().matches("(?i).*jiazu.*")) return;
				String content = MessageReflectHelper.getMessageInfo(msg);
				// logger.info("[家族消息测试] [addMessageToRightBag] [消息] [玩家名称:" + player.getName() + "] [玩家ID:" + player.getId() + "] " + content, start);
				if (logger.isInfoEnabled()) logger.info("[家族消息测试] [addMessageToRightBag] [消息] [玩家名称:{}] [玩家ID:{}] {}", new Object[] { player.getName(), player.getId(), content, start });

			}
		}
	}

	public boolean isEnableDebug() {
		return enableDebug;
	}

	public void setEnableDebug(boolean enableDebug) {
		this.enableDebug = enableDebug;
	}

	// private TimerTask task = new TimerTask() {
	// @Override
	// public void run() {
	// // int count = (int) getJiazuCount();
	// // int pagesize = 40;
	// // int pageNum = count / 20;
	// // if (count % pagesize != 0) pageNum++;
	// // for (int i = 0; i < pageNum; i++) {
	// // List<Jiazu> jiazuList = //getJiazuByOrder(null, i, pagesize, 1, true);
	// // for (Jiazu jiazu : jiazuList) {
	// //
	// // jiazu.calculateSalary();
	// // }
	// // }
	// for (Iterator<Long> itor = jiazuLruCacheByID.keySet().iterator(); itor.hasNext();) {
	// Jiazu jiazu = jiazuLruCacheByID.get(itor.next());
	// if (jiazu != null) {
	// jiazu.calculateSalary();
	// if (JiazuSubSystem.logger.isWarnEnabled()) {
	// JiazuSubSystem.logger.warn("[计算家族工资完毕:{}]", new Object[] { jiazu.getName() });
	// }
	// }
	// }
	// }
	// };
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	Thread thread;

	public void init() {
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		memberEm = SimpleEntityManagerFactory.getSimpleEntityManager(JiazuMember.class);
		jiazuEm = SimpleEntityManagerFactory.getSimpleEntityManager(Jiazu.class);
		septstationEm = SimpleEntityManagerFactory.getSimpleEntityManager(SeptStation.class);

		self = this;

		// 初始化家族职务index与jiazutitle的映射
		for (JiazuTitle jiazuTitle : JiazuTitle.values()) {
			jiazuTitles.put(jiazuTitle.ordinal(), jiazuTitle);
		}

		QuerySelect<Jiazu> querySelect = new QuerySelect<Jiazu>();
		List<Jiazu> allJiazu = null;
		try {
			allJiazu = querySelect.selectAll(Jiazu.class, null, new Object[] {}, null, jiazuEm);
		} catch (Exception e) {
			logger.error("[获取所有家族异常]", e);
		}

		if (allJiazu != null) {
			for (Jiazu jiazu : allJiazu) {
//				self.jiazuLruCacheByName.put(jiazu.getName(), jiazu);
//				self.jiazuLruCacheByID.put(jiazu.getJiazuID(), jiazu);
				if (jiazu.getLastCalculateSalaryTime() == 0) {
					// 此处这里设置,维护不能跨越发工资
					jiazu.setLastCalculateSalaryTime(now);
				}
				jiazu.initMember();
				try {
					if (jiazu.getMembers() != null && jiazu.getMembers().size() > 0) {
						self.jiazuLruCacheByName.put(jiazu.getName(), jiazu);
						self.jiazuLruCacheByID.put(jiazu.getJiazuID(), jiazu);
					} else {
						tempDeleteJiazu.add(jiazu.getJiazuID());
					}
				} catch (Exception e) {
					self.jiazuLruCacheByName.put(jiazu.getName(), jiazu);
					self.jiazuLruCacheByID.put(jiazu.getJiazuID(), jiazu);
				}
			}
		}
		running = true;
		thread = new Thread(this, "家族工资线程");
		thread.start();
		ServiceStartRecord.startLog(this);
	}
	public boolean testJiaZuReward = false;
	public List<Long> tempDeleteJiazu = new ArrayList<Long>();

	public void run() {
		while (running) {
			try {
				Thread.sleep(60 * 1000);

				Long[] ids = jiazuLruCacheByID.keySet().toArray(new Long[0]);
				for (int i = 0; i < ids.length; i++) {
					Jiazu jiazu = jiazuLruCacheByID.get(ids[i]);
					if (jiazu != null) {
						jiazu.calculateSalary(SystemTime.currentTimeMillis());
						jiazu.sendReward();
					}
				}
				JiazuManager2.instance.livenessReward();
			} catch (Throwable e) {
				logger.warn("[发工资线程] [出现未知异常]", e);
			}
		}
		logger.warn("[发工资线程停止运行]");
	}

	
	public void 清理远征排行(){
		try {
			long startTime = SystemTime.currentTimeMillis();
			long[] ids = jiazuEm.queryIds(Jiazu.class, " bossLevel > ? AND weekOfy > ? ",new Object[]{0,0},"bossLevel desc",1,1111);
			for(long id : ids){
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(id);
				jiazu.setWeekOfy(0);
			}
			YuanZhenBillboard bb = (YuanZhenBillboard)BillboardsManager.getInstance().getBillboard("家族远征", "远征排行");
			BillboardDate[] bbds = new BillboardDate[0];
			bb.setDates(bbds);
			BillboardsManager.logger.error("[清空远征排行] [ids:" +( ids!=null?ids.length:"null") + "] [cost:"+(SystemTime.currentTimeMillis()-startTime)+"]");
		} catch (Exception e) {
			e.printStackTrace();
			BillboardsManager.logger.error("[清空远征排行] [异常]",e);
		}
	}
	
	
	/**
	 * 系统关闭时调用
	 */
	public void destroy() {
		long start = SystemTime.currentTimeMillis();
		System.out.println("[系统关闭] [家族控制类] [关闭] [保存家族开始]");
		for (Jiazu jiazu : this.jiazuLruCacheByID.values()) {
			jiazu.doOnDestroy();
		}
		memberEm.destroy();
		jiazuEm.destroy();
		septstationEm.destroy();
		System.out.println("[系统关闭] [家族控制类] [关闭] [保存家族完毕] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
	}

	public Jiazu createJiazu(Player player, String jiazuName, String slogan) throws JiazuCreateException {
		if (this.getJiazu(jiazuName) == null) {
			synchronized (creat_lock_jiazu) {
				if (this.getJiazu(jiazuName) == null) {
					Jiazu jiazu = null;
					try {
						jiazu = new Jiazu();
						jiazu.setJiazuID(JiazuManager.jiazuEm.nextId());
						jiazu.setCreateTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						jiazu.setLevel(0);
						jiazu.setLastCalculateSalaryTime(SystemTime.currentTimeMillis());
						String[] titles = new String[JiazuTitle.values().length];
						for (int i = 0; i < JiazuTitle.values().length; i++) {
							titles[i] = JiazuTitle.values()[i].getName();
						}
						jiazu.setTitleAlias(titles);
						jiazu.setName(jiazuName);
						jiazu.setSlogan(slogan == null ? "" : slogan);
						JiazuMember member = null;
						try {
							member = this.createJiauMember(jiazu, player, JiazuTitle.master);
						} catch (Exception e) {
							logger.error(player.getLogString() + "[创建家族成员异常]", e);
						}
						jiazu.addMemberID(member.getJiazuMemID());
						// jiazu.initMember();
						jiazu.setMemberModify(true);
						// 把从player扣除的金币作为家族的起始资金
						jiazu.addJiazuMoney(InitialPlayerConstant.JAIZU_CREATE_EXPENSE);
						this.jiazuLruCacheByID.put(jiazu.getJiazuID(), jiazu);
						this.jiazuLruCacheByName.put(jiazu.getName(), jiazu);

						player.setJiazuId(jiazu.getJiazuID());
						jiazu.setJiazuPassword("");
						jiazuEm.notifyNewObject(jiazu);
						if (logger.isInfoEnabled()) {
							logger.info(player.getLogString() + "[创建家族] [成功] [家族:{}] [{}]", new Object[] { jiazu.getJiazuID(), member.toString() });
						}
					} catch (Exception e) {
						logger.error("[创建家族异常]", e);
					}
					return jiazu;

				}
			}
		}
		JiazuCreateException e = new JiazuCreateException();
		e.setMessage(Translate.text_jiazu_016);// Translate.translate.text_6095);
		throw e;
	}

	public long getJiazuCount() {
		long count = 0;
		try {
			count = jiazuEm.count();
		} catch (Exception e) {
			logger.error("[获取家族数量] [异常]", e);
		}
		return count;
	}

	public long getJiazuCount(String nameFilter) {
		List<Jiazu> result = null;
		try {
			result = jiazuEm.query(Jiazu.class, " name like '%" + nameFilter + "%'", null, 1, 100);
		} catch (Exception e) {
			logger.error("[模糊查询家族] [异常] [name:" + nameFilter + "]", e);
		}
		return result == null ? 0 : result.size();
	}

	public JiazuMember createJiauMember(Jiazu jiazu, Player player, JiazuTitle jiazuTitle) throws Exception {
		synchronized (creat_lock_jiazumember) {
			JiazuMember member = new JiazuMember();
			member.setJiazuMemID(memberEm.nextId());

			member.setJiazuID(jiazu.getJiazuID());
			member.setPlayerID(player.getId());
			member.setTitle(jiazuTitle);
			member.setTitleIndex(jiazuTitle.ordinal());
			if (this.jiazuMemberLruCacheByID.get(member.getJiazuID()) != null) {
				Set<JiazuMember> set = this.jiazuMemberLruCacheByID.get(member.getJiazuID());
				set.add(member);
			} else {
				Set<JiazuMember> set = this.getJiazuMember(member.getJiazuID());
				if (set == null) set = new HashSet<JiazuMember>();
				set.add(member);
				this.jiazuMemberLruCacheByID.put(member.getJiazuID(), set);
			}
			player.setJiazuId(jiazu.getJiazuID());
			player.setJiazuName(jiazu.getName());
			jiazu.addMemberID(member.getJiazuMemID());

			memberEm.flush(member);
			{
				// 统计
				AchievementManager.getInstance().record(player, RecordAction.加入家族次数);
			}
			return member;
		}
	}

	public boolean isUseObjectDb() {
		return useObjectDb;
	}

	public void setUseObjectDb(boolean useObjectDb) {
		this.useObjectDb = useObjectDb;
	}

	private static JiazuManager self = null;

	public static JiazuManager getInstance() {
		return self;
	}

	public String getJiaoSaveFile() {
		return jiaoSaveFile;
	}

	public void setJiaoSaveFile(String jiaoSaveFile) {
		this.jiaoSaveFile = jiaoSaveFile;
	}

	/**
	 * 
	 * run方法，主要从blockingqueue之中取出数据,对于不需要结果方法，比如insert,update，delete方法，
	 * 直接调用相应的dao的insert,update,delete,
	 * 对于需要返回值的操作，比如select,需要继承CallbackJiazu类,覆写execute方法
	 */
	private boolean running = true;

	public Jiazu getJiazu(long id) {
		if (this.jiazuLruCacheByID.containsKey(id)) {
			return this.jiazuLruCacheByID.get(id);
		} else {
			if (tempDeleteJiazu.contains(id)) {
				return null;
			}
			Jiazu jiazu = null;
			try {
				jiazu = jiazuEm.find(id);
			} catch (Exception e) {
				logger.error("[通过ID获得家族失败] [ID:" + id + "]", e);
			}

			if (jiazu != null) {
				this.jiazuLruCacheByID.put(id, jiazu);
				this.jiazuLruCacheByName.put(jiazu.getName(), jiazu);
			}
			return jiazu;
		}
	}

	/**
	 * 同步删除家族
	 * 
	 * @param jiazup
	 */
	public synchronized void removeJiazu(Jiazu jiazu) {
		this.jiazuLruCacheByID.remove(jiazu.getJiazuID());
		this.jiazuLruCacheByName.remove(jiazu.getName());
		Set<JiazuMember> memset = this.jiazuMemberLruCacheByID.get(jiazu.getJiazuID());
		for (JiazuMember mem : memset) {
			try {
				memberEm.remove(mem);
			} catch (Exception e) {
				logger.error("[删除家族成员异常] [家族:" + jiazu.getName() + "]", e);
			}
		}
		jiazuMemberLruCacheByID.remove(jiazu.getJiazuID());
		try {
			jiazuEm.remove(jiazu);
		} catch (Exception e) {
			logger.error("[删除家族异常] [家族:" + jiazu.getName() + "]", e);
		}
	}

	public Jiazu getJiazu(String name) {
		if (this.jiazuLruCacheByName.containsKey(name)) {
			if (JiazuSubSystem.logger.isInfoEnabled()) JiazuSubSystem.logger.info("cache contains jiazu " + name);
			return this.jiazuLruCacheByName.get(name);
		} else {
			List<Jiazu> list = null;
			try {
				list = jiazuEm.query(Jiazu.class, " name = '" + name + "'", null, 1, 2);
			} catch (Exception e) {
				logger.error("[通过名字获得家族] [异常]", e);
			}
			Jiazu jiazu = null;
			if (!list.isEmpty()) {
				jiazu = list.get(0);
			}
			if (jiazu != null && tempDeleteJiazu.contains(jiazu.getJiazuID())) {
				return null;
			}
			if (jiazu != null) {
				this.jiazuLruCacheByID.put(jiazu.getJiazuID(), jiazu);
				this.jiazuLruCacheByName.put(jiazu.getName(), jiazu);
			}
			return jiazu;
		}

	}

	/**
	 * 通过职务获得家族成员
	 * 
	 * @param jiazuId
	 * @param jiazuTitles
	 * @return
	 */
	public ArrayList<JiazuMember> getJiazuMember(long jiazuId, JiazuTitle... jiazuTitles) {
		if (this.jiazuMemberLruCacheByID.containsKey(jiazuId)) {
			Set<JiazuMember> memList = this.jiazuMemberLruCacheByID.get(jiazuId);
			ArrayList<JiazuMember> returnList = new ArrayList<JiazuMember>();
			for (JiazuMember member : memList) {
				for (JiazuTitle title : jiazuTitles) {
					if (title.equals(member.getTitle())) {
						returnList.add(member);
					}
				}
			}
			return returnList;
		}
		return null;
	}

	public synchronized JiazuMember getJiazuMember(long playerid, long jiazuID) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Set<JiazuMember> memSet = null;
		if (this.jiazuMemberLruCacheByID.containsKey(jiazuID)) {
			memSet = this.jiazuMemberLruCacheByID.get(jiazuID);
		} else {
			memSet = getJiazuMember(jiazuID);
			this.jiazuMemberLruCacheByID.put(jiazuID, memSet);
		}
		if (memSet == null) {
			logger.error("[家族成员不存在] [家族ID：{}] [playerID:{}] [{}ms]", new Object[] { jiazuID, playerid, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return null;
		}
		for (JiazuMember mem : memSet) {
			if (mem.getPlayerID() == playerid) return mem;
		}
		logger.error("[家族成员不存在] [家族ID：{}] [playerID:{}] [memsize:{}] [{}ms]", new Object[] { jiazuID, playerid, memSet.size(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
		return null;

	}

	public synchronized Set<JiazuMember> getJiazuMember(long jiazuID) {
		if (this.jiazuMemberLruCacheByID.containsKey(jiazuID)) {
			Set<JiazuMember> memList = this.jiazuMemberLruCacheByID.get(jiazuID);
			return memList;
		} else {
			QuerySelect<JiazuMember> querySelect = new QuerySelect<JiazuMember>();
			List<JiazuMember> list = null;
			try {
				list = querySelect.selectAll(JiazuMember.class, " jiazuID = ?", new Object[] { jiazuID }, null, memberEm);
			} catch (Exception e) {
				logger.error("[获取家族成员失败] [家族:{}]", new Object[] { jiazuID }, e);
			}
			HashSet<JiazuMember> set = new HashSet<JiazuMember>();
			set.addAll(list);
			this.jiazuMemberLruCacheByID.put(jiazuID, set);
			if (logger.isInfoEnabled()) {
				logger.info("[查询家族成员] [家族:{}] [数量:{}] [cache:{}]", new Object[] { jiazuID, set.size(), jiazuMemberLruCacheByID.get(jiazuID) });
			}
			return set;
		}
	}

	/**
	 * 根据关键查询合乎条件的家族
	 * 
	 * @param keyword
	 *            家族名
	 * @param page
	 *            页数
	 * @param pageNum
	 *            个数
	 * @param order
	 *            排序的列
	 * @param isAsc
	 *            是否升序
	 * @param country
	 * @return
	 */
	public List<Jiazu> getJiazuByOrder(String keyword, int page, int pageNum, int order, boolean isAsc, byte country) {
		JiazuListOrder sortOrder = JiazuListOrder.name;
		List<Jiazu> list = new ArrayList<Jiazu>();
		if (order == 1) {
			sortOrder = JiazuListOrder.createTime;
		} else if (order == 2) {
			sortOrder = JiazuListOrder.prosperity;
		}
		int start = page * pageNum + 1;
		int end = start + pageNum;
		if (sortOrder == JiazuListOrder.name) {
			try {
				list = jiazuEm.query(Jiazu.class, " country = " + country + " and name like '%" + keyword + "%'", "level DESC ", start, end);
			} catch (Exception e) {
				logger.error("[按条件查询家族异常] [keyword:" + keyword + "] [page:" + page + "] [pageNum:" + pageNum + "] [order:" + order + "] [isAsc:" + isAsc + "]", e);
			}
			return list;
		} else if (sortOrder == JiazuListOrder.createTime) {
			try {
				list = jiazuEm.query(Jiazu.class, " country = " + country + " and name like '%" + keyword + "%'", "level DESC ," + sortOrder + " " + (isAsc ? "ASC" : "DESC"), start, end);
			} catch (Exception e) {
				logger.error("[按条件查询家族异常] [keyword:" + keyword + "] [page:" + page + "] [pageNum:" + pageNum + "] [order:" + order + "] [isAsc:" + isAsc + "]", e);
			}

			return list;
		}
		// TODO 按照家族驻地的繁荣度来排序
		else {

			return null;
		}

	}

	public boolean removeMember(long playerID, Jiazu jiazu) throws Exception {
		// Set<JiazuMember> set = this.jiazuMemberLruCacheByID.get(jiazu.getJiazuID());
		// if (set != null) {
		// JiazuMember target = null;
		// for (JiazuMember mem : set) {
		// if (mem.getPlayerID() == playerID) {
		// target = mem;
		// break;
		// }
		// }
		// if (target != null) {
		// set.remove(target);
		// }
		// }
		return jiazu.removeOneMember(playerID);
	}

	/**
	 * 
	 * 家族族长禅让
	 * 
	 * @param master
	 * @param viceMaster
	 * @param jiazuId
	 * @return
	 */
	public void resignMaster(JiazuMember master, JiazuMember viceMaster, long jiazuId) {
		viceMaster.setTitle(JiazuTitle.master);
		master.setTitle(JiazuTitle.civilian);
		// return this.dao.saveJiazuMember(master) && this.dao.saveJiazuMember(viceMaster);

	}

	// 查看家族仓库
	public void lookWareHouse(Player player) {
		Jiazu jiazu = getJiazu(player.getJiazuId());
		if (jiazu == null) {
			player.sendError(Translate.您还没有家族);
			return;
		}
		boolean isMaster = false;
		if (jiazu.getJiazuMaster() == player.getId()) {
			isMaster = true;
		}
		long[] entityids = new long[jiazu.getWareHouse().size()];
		int[] entityNums = new int[jiazu.getWareHouse().size()];
		for (int i = 0; i < entityids.length; i++) {
			Cell cell = jiazu.getWareHouse().get(i);
			if (cell == null) {
				continue;
			}
			entityids[i] = cell.getEntityId();
			entityNums[i] = cell.getCount();
		}

		Set<JiazuMember> members = getJiazuMember(jiazu.getJiazuID());
		String[] jiazuPlayerName = new String[members.size()];
		int i = 0;
		for (JiazuMember jm : members) {

			if (jm == null) {
				continue;
			}
			PlayerSimpleInfo p = null;
			try {
				p = PlayerSimpleInfoManager.getInstance().getInfoById(jm.getPlayerID());
			} catch (Exception e) {
				logger.error("lookWareHouse:", e);
				jiazuPlayerName[i] = Translate.未知;
				continue;
			}
			if (p != null) {
				jiazuPlayerName[i] = p.getName();
			} else {
				jiazuPlayerName[i] = Translate.未知;
			}
			i++;
		}

		JIAZU_WAREHOUSE_RES res = new JIAZU_WAREHOUSE_RES(GameMessageFactory.nextSequnceNum(), isMaster, WAREHOUSE_MAX, entityids, entityNums, jiazuPlayerName);
		player.addMessageToRightBag(res);
		if (logger.isWarnEnabled()) logger.warn("[查看家族仓库] [p={}] [j={}] [isMaster={}] [eS={}] [pS={}]", new Object[] { player.getLogString(), jiazu.getJiazuID(), isMaster, entityids.length, jiazuPlayerName.length });
	}

	// 分配物品给家族的其他人
	public boolean msg_putEntity2Player(Player player, int index, long entityID, int num, String pName) {
		Jiazu jiazu = getJiazu(player.getJiazuId());
		if (num <= 0) {
			logger.error("[家族物品分配num] [外挂] [{}] [{}] [{}] [{}]", new Object[] { player.getLogString(), index, num, pName });
			player.sendError(Translate.数目输入错误);
			return false;
		}
		if (index < 0 || index >= WAREHOUSE_MAX) {
			logger.error("[家族物品分配index] [外挂] [{}] [{}] [{}] [{}]", new Object[] { player.getLogString(), index, num, pName });
			player.sendError(Translate.那个格子不存在);
			return false;
		}
		if (jiazu == null) {
			player.sendError(Translate.您还没有家族);
			return false;
		}
		if (jiazu.getJiazuMaster() != player.getId()) {
			player.sendError(Translate.您不是族长不能操作);
			return false;
		}
		if (jiazu.getWareHouse().size() <= index || index < 0) {
			player.sendError(Translate.此格子上没有物品);
			return false;
		}
		Cell cell = jiazu.getWareHouse().get(index);
		if (cell == null) {
			player.sendError(Translate.此格子上没有物品);
			jiazu.getWareHouse().remove(index);
			jiazuEm.notifyFieldChange(jiazu, "wareHouse");
			return false;
		}

		if (cell.getCount() < num) {
			player.sendError(Translate.数目输入错误);
			return false;
		}

		Player other = null;
		try {
			other = PlayerManager.getInstance().getPlayer(pName);
		} catch (Exception e) {
			if (logger.isInfoEnabled()) logger.info("分配物品的时候玩家不存在" + pName, e);
			player.sendError(Translate.玩家不存在);
			return false;
		}

		if (other.getJiazuId() != jiazu.getJiazuID()) {
			player.sendError(Translate.不能给非自己家族的玩家分配);
			return false;
		}
		ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
		if (entity == null) {
			logger.error("[分配物品不存在] [jiD={}] [index={}] [num={}]", new Object[] { jiazu.getJiazuID(), index, num });
			player.sendError(Translate.物品不存在);
			jiazu.getWareHouse().remove(index);
			jiazuEm.notifyFieldChange(jiazu, "wareHouse");
			return false;
		}
		int oldNum = cell.getCount();
		jiazu.putWareHouseToPlayer(other, index, num);
		if (logger.isWarnEnabled()) logger.warn("[家族物品分配] [成功] [p={}] [o={}] [j={}] [E={}] [oldNum={}] [num={}]", new Object[] { player.getLogString(), other.getLogString(), jiazu.getJiazuID() + "~" + jiazu.getName(), entity.getId() + "~" + entity.getArticleName() + "~" + entity.getColorType(), oldNum, num });
		return true;
	}

	/**
	 * 获取一个家族的剩余的职位的个数
	 * 
	 * @param jiazu
	 * @param title
	 * @return
	 */
	public int getTitleLeft(Jiazu jiazu, JiazuTitle title) {
		int left = 0;
		if (title == JiazuTitle.civilian) {
			left = jiazu.maxMember() - jiazu.getMemberNum();
		} else {
			int maxnum = JiazuTitle.getTitleNumMax(title, jiazu.getLevel());
			int count = 0;
			for (JiazuMember4Client jiazuMember : jiazu.getMember4Clients()) {
				if (jiazuMember.getTitle() == title.ordinal()) {
					count++;
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("[家族等级:{}] [职位:{}] [上限:{}] [已有:{}]", new Object[] { jiazu.getLevel(), title, maxnum, count });
			}
			if (maxnum > count) return maxnum - count;
		}
		return left;

	}

	public Map<Long, Jiazu> getJiazuLruCacheByID() {
		return jiazuLruCacheByID;
	}

	public void setJiazuLruCacheByID(Map<Long, Jiazu> jiazuLruCacheByID) {
		this.jiazuLruCacheByID = jiazuLruCacheByID;
	}

	public void addTitleAlias(String str[], Jiazu jiazu) {
		jiazu.setTitleAlias(str);
		// this.update(jiazu, DbOperation.update_jiazu);
	}

	public static Comparator<JiazuMember4Client> orderByTitle = new Comparator<JiazuMember4Client>() {
		public int compare(JiazuMember4Client o1, JiazuMember4Client o2) {
			return o1.getTitle() - o2.getTitle();
		};
	};

	public static void main(String[] args) {
	}

	public void removePlayer(Player p) {
		if (p.getJiazuId() > 0) {
			Jiazu jiazu = getJiazu(p.getJiazuId());
			if (jiazu != null) {
				jiazu.removeOneMember(p.getId());
			}
		}
	}

	public Map<String, Jiazu> getJiazuLruCacheByName() {
		return jiazuLruCacheByName;
	}

	public void setJiazuLruCacheByName(Map<String, Jiazu> jiazuLruCacheByName) {
		this.jiazuLruCacheByName = jiazuLruCacheByName;
	}

	public Map<Long, Set<JiazuMember>> getJiazuMemberLruCacheByID() {
		return jiazuMemberLruCacheByID;
	}

	public void setJiazuMemberLruCacheByID(Map<Long, Set<JiazuMember>> jiazuMemberLruCacheByID) {
		this.jiazuMemberLruCacheByID = jiazuMemberLruCacheByID;
	}

}
