package com.fy.engineserver.jiazu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.fy.engineserver.activity.luckfruit.ForLuckFruitManager;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.city.CityAction;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.septbuilding.entity.SeptBuildingEntity;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.ForLuckFruitNPC;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 
 * 一个家族
 * 
 * 
 * 
 */
@SimpleEntity
@SimpleIndices({ @SimpleIndex(members = { "country", "name" }), @SimpleIndex(members = { "prosperity" }) })
public class Jiazu {
	/**
	 * 家族ID
	 */
	@SimpleId
	private long jiazuID;
	@SimpleVersion
	private int version;
	/**
	 * 家族名称
	 */
	@SimpleColumn(length = 40)
	private String name;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 家族徽章ID
	 */
	private long iconID;
	/**
	 * 家族驻地ID
	 */
	private long baseID;
	/**
	 * 人口上限
	 */
	private static final int[] PEOPLE_NUM_MAX = new int[] {15,15,20,20,25,25,30,30,35,35,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40,40};

	public static float K1 = 0.1f;
	public static float k2 = 1.5f;
	public static float k3 = 0.3f;

	/**
	 * 级别
	 */
	@SimpleColumn(name = "jiazuLevel")
	private int level = 0;

	/**
	 * 家族标语
	 */
	@SimpleColumn(length = 300)
	private String slogan = "";;

	/**
	 * 
	 * 家族密码(族长方可查看)
	 */
	@SimpleColumn(length = 20)
	private String jiazuPassword;

	/** 密码提示 */
	@SimpleColumn(length = 80)
	private String jiazuPasswordHint;

	/**
	 * 家族灵石
	 */
	private long jiazuMoney;

	/**
	 * 家族领地个数
	 */
	private int landNum;

	/**
	 * 家族灵脉值
	 */
	private long constructionConsume;

	/**
	 * 家族仓库
	 */
	@SimpleColumn(length = 12000)
	private ArrayList<Cell> wareHouse = new ArrayList<Cell>();

	/**
	 * 家族驻地的繁荣度,没有驻地的为0
	 */
	private int prosperity;

	// 用于排行榜更改繁荣度时间 下降不变
	private long updateProsperityTime;
	/**
	 * 家族状态   0为正常   -1为限制一些功能
	 */
	private byte jiazuStatus;

	/**
	 * 上一次副族长投票成为族长的时间
	 * 
	 */
	private long lastVoteTime;

	private String[] titleAlias;
	private transient ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
	/** 成员是否有变化 ,变化的时候就重新加载信息,不做存储的依据 */
	private transient boolean memberModify = false;
	/** 家族所属国家 */
	private byte country;
	/** 最后一次召集家族成员时间 */
	private long lastCallinTime;
	/** 当天召集次数 */
	private long cycleCallinTimes;
	/** 总召集次数 */
	private long totalCallinTimes;
	/** 最后一次发布工资仪式时间 */
	private long lastSalaryCeremonyTime;
	/** 当天发布工资仪式次数 */
	private long cycleSalaryCeremonyTimes;
	/** 总发布工资仪式次数 */
	private long totalSalaryCeremonyTimes;
	/** 本周召唤BOSS次数 */
	private int currentWeekCallbossTimes;
	/** 最后一次召唤boss时间 */
	private long lastCallbossTime;
	/** 最后一次计算工资时间 */
	private long lastCalculateSalaryTime;
	/** 申请矿战的骰子点数 */
	private int point;

	// 活动数据
	/******************************** 养蚕 ********************************/
	/** 是否在活动期间 */
	private transient boolean inSilkworm = false;
	/** 最后一次发布养蚕任务时间 */
	private transient long lastReleaseSilkwormTime;

	/** 养蚕任务结束时间 */
	private transient long silkwormDoTaskEndTime;
	/** 桑树长大时间 */
	private transient long silkwormGroupUpTime;
	/** 大便消失时间 */
	private transient long silkwormDisappear;

	private transient int silkwormStatus;
	/** 养蚕任务总次数 */
	private int totalReleaseSilkwormTimes;
	/** 当天养蚕任务总次数 */
	private int currentReleaseSilkwormTimes;
	/** 养蚕活动任务完成次数统计 */
	private transient int[] feedDeliverNum = new int[3];
	/** 当天炼丹次数 */
	private int currentReleaseLianDan;
	/** 最后一次炼丹时间 */
	private transient long lastReleaseLianDanTime;

	/********************************* 祝福果树 *******************************/
	private int[] forLuckFriutNums = new int[5];// 每个颜色有多少个

	/** 家族最后一次押镖时间 */
	private long lastJiazuSilverCartime;
	
	//远征boss级别id
	private int bossLevel = 1;
	private long causeDamage;
	private long costTime;
	private String maxDamagePlayer;
	private int weekOfy;
	private String bossName;
	private double damageProp;
	
	private float liveness;
	
	//家族副本
	private transient CityAction city;
	private boolean hasKillBoss;
	
	
	//BOSS:伤害百分比:所用时间:家族:最强输出
	public void 通知家族远征(String bossName,int bossLevel,double damage,long costt,String reason){
		try {
			setWeekOfy(1);
			setBossName(bossName);
			setBossLevel(bossLevel);
			setDamageProp(damage);
			setCostTime(costt);
//			setMaxDamagePlayer(maxPlayerda);
			JiazuManager.jiazuEm.save(this);
			BillboardsManager.logger.warn("[通知家族远征排行榜] ["+reason+"] [boss级别:"+bossLevel+"] [bossName:"+bossName+"] [伤害百分比:"+damage+"] [耗时:"+costt+"] [家族信息:"+getLogString()+"]");
		} catch (Exception e) {
			e.printStackTrace();
			BillboardsManager.logger.warn("[通知家族远征排行榜] ["+reason+"] [异常] [bossLevel:"+bossLevel+"] [bossName:"+bossName+"] [伤害百分比:"+damage+"] [耗时:"+costt+"] [家族信息:"+getLogString()+"]",e);
		}
	}
	
	public double getDamageProp() {
		return damageProp;
	}

	public void setDamageProp(double damageProp) {
		this.damageProp = damageProp;
		notifyFieldChange("damageProp");
	}

	public String getMaxDamagePlayer() {
		return maxDamagePlayer;
	}

	public void setMaxDamagePlayer(String maxDamagePlayer) {
		this.maxDamagePlayer = maxDamagePlayer;
		notifyFieldChange("maxDamagePlayer");
	}

	public int getWeekOfy() {
		return weekOfy;
	}

	public void setWeekOfy(int weekOfy) {
		this.weekOfy = weekOfy;
		notifyFieldChange("weekOfy");
	}

	public String getBossName() {
		return bossName;
	}

	public void setBossName(String bossName) {
		this.bossName = bossName;
		notifyFieldChange("bossName");
	}

	public boolean isHasKillBoss() {
		return hasKillBoss;
	}

	public void setHasKillBoss(boolean hasKillBoss) {
		this.hasKillBoss = hasKillBoss;
		notifyFieldChange("hasKillBoss");
	}

	public CityAction getCity() {
		return city;
	}

	public void setCity(CityAction city) {
		this.city = city;
	}
	

	/** 炼丹炉位的NPC */
	private transient ForLuckFruitNPC[] fruitNPCs = new ForLuckFruitNPC[ForLuckFruitManager.maxTreeNum];
	// 活动数据
	/**
	 * 给客户端的数据
	 */
	private transient ArrayList<JiazuMember4Client> member4Clients = new ArrayList<JiazuMember4Client>();
	private transient List<JiazuMember> members = new ArrayList<JiazuMember>();

	public long getLastVoteTime() {
		return lastVoteTime;
	}

	public void setLastVoteTime(long lastVoteTime) {
		this.lastVoteTime = lastVoteTime;
		notifyFieldChange("lastVoteTime");
	}

	public boolean isMemberModify() {
		return memberModify;
	}

	public void setMemberModify(boolean memberModify) {
		this.memberModify = memberModify;
	}

	/**
	 * 用户申请的加入家族列表
	 */
	private HashMap<Long, Long> requestMap = new HashMap<Long, Long>();
	/** 已获得的家族徽章 */

	private ArrayList<Integer> bedges = new ArrayList<Integer>();
	/** 正在使用的徽章ID */
	private int usedBedge;

	public long getJiazuMoney() {
		return jiazuMoney;
	}

	public void setJiazuMoney(long jiazuMoney) {
		this.jiazuMoney = jiazuMoney;
		notifyFieldChange("jiazuMoney");
	}
	

	public int getBossLevel() {
		return bossLevel;
	}

	public void setBossLevel(int bossLevel) {
		this.bossLevel = bossLevel;
		notifyFieldChange("bossLevel");
	}

	public float getLiveness() {
		return liveness;
	}

	public void setLiveness(float liveness) {
		this.liveness = liveness;
		notifyFieldChange("liveness");
	}

	public long getCauseDamage() {
		return causeDamage;
	}

	public void setCauseDamage(long causeDamage) {
		this.causeDamage = causeDamage;
		notifyFieldChange("causeDamage");
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
		notifyFieldChange("costTime");
	}

	public String[] getTitleAlias() {
		return titleAlias;
	}

	public void setTitleAlias(String[] titleAlias) {
		this.titleAlias = titleAlias;
		notifyFieldChange("titleAlias");
	}

	public void setMemberSet(Set<Long> memberSet) {
		this.memberSet = memberSet;
		notifyFieldChange("memberSet");
	}

	/** 申请驻地花费 */
	public static final long REQUEST_BASE_MONEY = 200 * 1000;
	public static final String REQUEST_BASE_ARTICLE = Translate.族令;
	public static int CREATE_BASE_LESS_NUM = 1;
	public static final int DAILY_CALLIN_MAX = 2;
	public static final int DAILY_SALAEY_MAX = 1;

	public int getProsperity() {
		return prosperity;
	}

	public void setProsperity(int prosperity) {
		this.prosperity = prosperity;
		notifyFieldChange("prosperity");
	}

	public synchronized void addRequest(long playerID) {
		// 申请列表只维护10个,族长或者其它有权限人民要进行处理申请
		try {
			Player player = PlayerManager.getInstance().getPlayer(playerID);

			if (requestMap.size() < 10) {
				requestMap.put(playerID, com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				notifyFieldChange("requestMap");
				ChatMessageService.getInstance().sendMessageToJiazu(this, player.getName() + Translate.text_6367, Translate.系统);
			} else {
				ChatMessageService.getInstance().sendMessageToJiazu(this, Translate.translateString(Translate.申请列表已满, new String[][] { { Translate.STRING_1, getName() } }), Translate.系统);
			}
		} catch (Exception e) {
			JiazuSubSystem.logger.error("[playerID:" + playerID + "] [申请加入家族:" + getJiazuID() + "] [异常]", e);
		}
	}

	public HashMap<Long, Long> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(HashMap<Long, Long> requestMap) {
		this.requestMap = requestMap;
		notifyFieldChange("requestMap");
	}

	public synchronized void removeRequest(long playerId) {
		if (JiazuSubSystem.logger.isDebugEnabled()) {
			JiazuSubSystem.logger.debug("[移除玩家]{} 前 数量:{}", new Object[] { playerId, requestMap.size() });
		}
		requestMap.remove(Long.valueOf(playerId));
		if (JiazuSubSystem.logger.isDebugEnabled()) {
			JiazuSubSystem.logger.debug("[移除玩家]{} 后 数量:{}", new Object[] { playerId, requestMap.size() });
		}
		notifyFieldChange("requestMap");
	}

	/**
	 * 本周的工资的总和
	 * 
	 */
	private long salarySum;

	/**
	 * 本周的发送工资的剩余量
	 */
	private long salaryLeft;

	public long getSalarySum() {
		return salarySum;
	}

	/**
	 * 人均基本工资
	 */
	public static int SALARY_BASE = 2500000;
	public static int MAX_CONTRIBUTION = 1250;
	public static double totalSalayRate = 0.8;

	/**
	 * 单个玩家本周发放工资的上限?
	 * 
	 */
	private long singleMemberSalaryMax;

	/**
	 * 历史贡献度
	 */
	private long totalcontributionSum;

	/**
	 * 上周贡献度之和
	 */
	private int contributionSum;

	public int getContributionSum() {
		return contributionSum;
	}

	public void setContributionSum(int contributionSum) {
		this.contributionSum = contributionSum;
		notifyFieldChange("contributionSum");
	}

	// TODO 计算本周工资的方法
	/**
	 * 每周一的凌晨开始调用 计算本周的工资<BR/>
	 * 家族工资计算：<BR/>
	 * 家族总工资= int(家族总人口*主建筑等级/10*人均基础工资)<BR/>
	 * 家族实际总工资= int(（1+家族建筑加成）*家族总工资)<BR/>
	 * 个人基础工资= 人均基础工资*主建筑等级/10*if（（贡献度加成/基本贡献比例）＞＝1,1，if（贡献度加成/基本贡献比例）＜1，（贡献度加成/基本贡献比例））<BR/>
	 * 个人工资上限= 家族实际总工资/家族总人口<BR/>
	 * ---------------------------------参数定义----------------------------------------<BR/>
	 * 人均基础工资 5,000,000<BR/>
	 * 家族建筑加成= 家族建筑等级总和/90<BR/>
	 * 贡献度加成= 玩家贡献度/家族总贡献度<BR/>
	 * 基本贡献比例= 1/当前家族等级人口上限<BR/>
	 * ---------------------------------以上废弃---------------------------------<BR/>
	 * 个人工资上限=(个人贡献度/1250) * (2500000 * (1 + (建筑等级之和/90)))
	 * 家族可发总工资=(∑个人工资) * 0.8;
	 */
	
	public static long testTime = 0;
	
	private int [] rewardMonry = {200000,300000,500000,800000,1200000,1800000,2000000,2500000,3000000,4000000};
	
	/**
	 * 周日0点发放。进家族超过24小时才有奖励
	 */
	public void sendReward(){
		if(!JiazuManager.getInstance().testJiaZuReward){
			Calendar cal = Calendar.getInstance();
			int d = cal.get(Calendar.DAY_OF_WEEK);
			int h = cal.get(Calendar.HOUR_OF_DAY);
			if (h != 0) return;
			if (d != Calendar.SUNDAY) return;
		}
		
		if(level <= 0){
			JiazuSubSystem.logger.warn("[家族资金发放] [失败:等级不符] ["+this.getLogString()+"]");
			return;
		}
		
		SeptStation station = SeptStationManager.getInstance().getSeptStationById(this.baseID);
		if (station == null) {
			JiazuSubSystem.logger.warn("[家族资金发放] [失败:没有驻地不放奖励] ["+this.getLogString()+"]");
			return;
		}
		
		int salary = 0;
		if(0 < level && level <= 10){
			salary = rewardMonry[level - 1];
		}else if(level > 10){
			salary = rewardMonry[rewardMonry.length-1];
		}
		Set<JiazuMember> memList = JiazuManager.getInstance().getJiazuMember(this.jiazuID);
		for (JiazuMember mem : memList) {
			if(mem == null){
				continue;
			}
			if (JiazuSubSystem.logger.isWarnEnabled()) {
				JiazuSubSystem.logger.warn("[家族资金发放] [成功] [level:"+level+"] [是否领取:"+mem.isPaid()+"] [玩家id:"+mem.getPlayerID()+"] [资金:"+salary+"] ["+this.getLogString()+"]");
			}
			if(mem.isPaid()){
				continue;
			}
			mem.addSalary(salary);
			try {
				MailManager.getInstance().sendMail(mem.getPlayerID(), new ArticleEntity[]{}, "家族资金发放", "感谢您本周为家族的贡献，这是族长发放给您这周的工资，可以前往家族领地家族工资管理处查看，工资可以在家族工资商店购买自己喜欢的物品噢！~", 0, 0, 0, "家族资金发放");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void calculateSalary(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		int d2 = cal.get(Calendar.DAY_OF_WEEK);

		if (d2 == Calendar.SUNDAY) return; // 周日不发工资
		Set<JiazuMember> memList = JiazuManager.getInstance().getJiazuMember(this.jiazuID);

		for (JiazuMember mem : memList) {
			if(mem.isPaid()){
				mem.setPaid(false);
				if (JiazuSubSystem.logger.isWarnEnabled()) {
					JiazuSubSystem.logger.warn(this.getLogString() + "[计算家族工资] [家族ID:" + getJiazuID() + "] [成员id:" + mem.getJiazuMemID() + "] [贡献度:" + mem.getLastWeekContribution() + "] [工资上限:" + mem.getCurrentWeekMaxSalary() + "]");
				}
			}
		}
//		initMember4Client();
	}

	public synchronized void reduceForLuck(int color, int num) {
		if (forLuckFriutNums[color] < num) {

		} else {
			forLuckFriutNums[color] = forLuckFriutNums[color] - num;
			notifyFieldChange("forLuckFriutNums");
		}
	}

	/**
	 * 得到某个人的工资 <BR/>
	 * 个人基础工资= 人均基础工资*主建筑等级/10*贡献度加成/基本贡献比例<BR/>
	 * 个人工资上限= 家族实际总工资/家族总人口<BR/>
	 * -----------------------------变量定义----------------------------------<BR/>
	 * 贡献度加成= 玩家贡献度/家族总贡献度<BR/>
	 * 基本贡献比例= 1/当前家族等级人口上限<BR/>
	 * <b>-----------------------------以上废弃----------------------------------</b><BR/>
	 */
	public long getSinglePlayerSalary(long playerID) {
		JiazuManager jiazuManager = JiazuManager.getInstance();
		JiazuMember member = jiazuManager.getJiazuMember(playerID, this.jiazuID);
		SeptStation station = SeptStationManager.getInstance().getSeptStationById(this.baseID);
		if (station == null) {
			return 0;
		}
		if (member == null) {
			return 0;
		} else {
			return member.getCurrentWeekMaxSalary();
		}
	}

	public void setSalarySum(long salarySum) {
		this.salarySum = salarySum;
		notifyFieldChange("salarySum");
	}

	public long getSalaryLeft() {
		return salaryLeft;
	}

	public void setSalaryLeft(long salaryLeft) {
		this.salaryLeft = salaryLeft;
		notifyFieldChange("salaryLeft");
	}

	public final int[] CONSTRUCTION_CONSUME_MAX = new int[] { 0, 1000, 3000, 4000, 5000, 7000 };

	private Set<Long> memberSet = new HashSet<Long>();

	public Jiazu() {

	}

	public void addMemberID(long memberID) {
		this.memberSet.add(memberID);
		notifyFieldChange("memberSet");
	}

	/**
	 * 是否包含这个player
	 * 
	 * @param playerID
	 *            玩家ID
	 * @return true 包含
	 * 
	 */
	public boolean isContainPlayer(long playerID) {
		JiazuManager jiazuManager = JiazuManager.getInstance();
		Set<JiazuMember> jiazuList = jiazuManager.getJiazuMember(this.jiazuID);
		for (JiazuMember mem : jiazuList) {
			if (mem.getPlayerID() == playerID) return true;
		}
		return false;
	}

	public boolean removeOneMember(long playerId) {
		JiazuManager jiazuManager = JiazuManager.getInstance();
		Set<JiazuMember> memberList = jiazuManager.getJiazuMember(this.jiazuID);
		for (JiazuMember mem : memberList) {
			if (mem.getPlayerID() == playerId) {
				this.memberSet.remove(mem.getJiazuMemID());
				jiazuManager.getJiazuMemberLruCacheByID().get(getJiazuID()).remove(mem);
				setMemberModify(true);
				try {
					JiazuManager.memberEm.remove(mem);
				} catch (Exception e) {
					JiazuManager.logger.error("[删除一个家族成员异常] [家族:" + getJiazuID() + "] ", e);
					return false;
				}
				notifyFieldChange("memberSet");
				return true;
			}
		}
		return false;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
		notifyFieldChange("slogan");
	}

	public long getJiazuID() {
		return jiazuID;
	}

	public void setJiazuID(long jiazuID) {
		this.jiazuID = jiazuID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		notifyFieldChange("name");
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
		notifyFieldChange("createTime");
	}

	public long getIconID() {
		return iconID;
	}

	public void setIconID(long iconID) {
		this.iconID = iconID;
		notifyFieldChange("iconID");
	}

	public long getBaseID() {
		return baseID;
	}

	public void setBaseID(long baseID) {
		this.baseID = baseID;
		notifyFieldChange("baseID");
	}

	public int getLevel() {
		if(level > 30){
			level = 30;
			notifyFieldChange("level");
		}
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		notifyFieldChange("level");
	}

	public long getTotalcontributionSum() {
		return totalcontributionSum;
	}

	public void setTotalcontributionSum(long totalcontributionSum) {
		this.totalcontributionSum = totalcontributionSum;
		notifyFieldChange("totalcontributionSum");
	}

	/**
	 * 获取这个称号在这个家族的个性化的名称
	 * 
	 * @param title
	 * @return
	 */
	public String getTitleName(JiazuTitle title) {
		if (this.titleAlias == null || this.titleAlias[title.ordinal()] == null) {
			return title.getName();
		} else {
			return this.titleAlias[title.ordinal()];
		}
	}

	/**
	 * 取得家族的人员的个数
	 * 
	 * @return
	 */
	public int getMemberNum() {
		return this.memberSet.size();
	}

	public Set<Long> getMemberSet() {
		return this.memberSet;

	}

	public String getJiazuPassword() {
		return jiazuPassword;
	}

	public void setJiazuPassword(String jiazuPassword) {
		this.jiazuPassword = jiazuPassword;
		notifyFieldChange("jiazuPassword");
	}

	/**
	 * @param money
	 */
	public String addJiazuMoney(long money) {
		if (money < 0) {
			return null;
		}
		rwlock.writeLock().lock();
		try {
			SeptStation ss = SeptStationManager.getInstance().getSeptStationById(this.getBaseID());
			long mon = jiazuMoney + money;
			if (ss != null) {
				if (jiazuMoney >= ss.getInfo().getMaxCoin()) {
					JiazuManager2.logger.warn("[新家族] [家族资金超过限制] [新增家族资金: " + money + "] [原有资金:" + jiazuMoney + "] [家族等级: " + this.getLevel() + "] [上限:" + ss.getInfo().getMaxCoin() + "] [家族id:" + this.getJiazuID() + "] [驻地id:" + this.getBaseID() + "]");
					return Translate.家族资金已超出上限;
				}
				if (mon >= ss.getInfo().getMaxCoin()) {
					JiazuManager2.logger.warn("[新家族] [家族资金超过限制] [新增家族资金: " + money + "] [原有资金:" + jiazuMoney + "] [家族等级: " + this.getLevel() + "] [上限:" + ss.getInfo().getMaxCoin() + "] [家族id:" + this.getJiazuID() + "] [驻地id:" + this.getBaseID() + "]");
					mon = ss.getInfo().getMaxCoin();
				}
			}
			this.setJiazuMoney(mon);
		} finally {
			rwlock.writeLock().unlock();
		}
		return null;
	}

	public boolean consumeJiazuMoney(long money) {
		if (money < 0) return false;
		rwlock.writeLock().lock();
		try {
			if (this.jiazuMoney < money) {
				rwlock.writeLock().unlock();
				return false;
			}
			this.setJiazuMoney(jiazuMoney - money);
//			try {
//				if (JiazuManager2.instance.noticeNum.get(this.getLevel()) != null) {
//					int num = JiazuManager2.instance.noticeNum.get(this.getLevel());
//					if (this.getJiazuMoney() < num) {
//						JiazuManager2.instance.noticeJizuMondeyNotEnough(this);
//					}
//				}
//			} catch (Exception e) {
//				JiazuManager2.logger.error("[新家族] [发送家族资金不足提醒] [异常] [" + this.getLogString() + "]", e);
//			}
		} finally {
			if (rwlock.isWriteLocked()) {
				rwlock.writeLock().unlock();
			}
		}
		return true;
	}
	
	public String addMoneyAndConsumeWithoutLimit(long money, long consume) {
		if (money < 0 || consume < 0) {
			return "传入数据错误";
		}
		rwlock.writeLock().lock();
		try {
			long mon = jiazuMoney + money;
			long consum = constructionConsume + consume;
			this.setJiazuMoney(mon);
			this.setConstructionConsume(consum);
			return null;
		} finally {
			rwlock.writeLock().unlock();
		}
	}
	

	public String addContructionConsume(long num) {
		if (num < 0) return null;
		long mon = constructionConsume + num;
		SeptStation ss = SeptStationManager.getInstance().getSeptStationById(this.getBaseID());
		if (ss != null) {
			if (constructionConsume >= ss.getInfo().getMaxSprint()) {
				JiazuManager2.logger.warn("[新家族] [家族灵脉值超过限制] [新增家族灵脉值: " + num + "] [原有灵脉值:" + constructionConsume + "] [家族等级: " + this.getLevel() + "] [上限:" + ss.getInfo().getMaxSprint() + "] [家族id:" + this.getJiazuID() + "] [驻地id:" + this.getBaseID() + "]");
				return Translate.家族资金已超出上限;
			}
			if (mon >= ss.getInfo().getMaxSprint()) {
				JiazuManager2.logger.warn("[新家族] [家族灵脉值超过限制] [新增家族灵脉值: " + num + "] [原有灵脉值:" + constructionConsume + "] [家族等级: " + this.getLevel() + "] [上限:" + ss.getInfo().getMaxSprint() + "] [家族id:" + this.getJiazuID() + "] [驻地id:" + this.getBaseID() + "]");
				mon = ss.getInfo().getMaxSprint();
			}
		}

		this.setConstructionConsume(mon);
		return null;
	}

	public boolean consumeConstructionConsume(long num) {
		if (num < 0) return false;
		rwlock.writeLock().lock();
		try {
			if (this.constructionConsume < num) {
				rwlock.writeLock().unlock();
				return false;
			} else {
				this.setConstructionConsume(constructionConsume - num);
			}
		} finally {
			rwlock.writeLock().unlock();
		}
		return true;
	}

	public boolean consumeJiazuMoneyAndConstructionConsume(long jiazuMoney, long constructionConsume) {
		if (jiazuMoney < 0 || constructionConsume < 0) return false;
		rwlock.writeLock().lock();
		try {
			if (this.jiazuMoney < jiazuMoney || this.constructionConsume < constructionConsume) {
				rwlock.writeLock().unlock();
				return false;
			} else {
				if (jiazuMoney > 0) {
					this.setJiazuMoney((this.jiazuMoney - jiazuMoney));
				}
				if (constructionConsume > 0) {
					this.setConstructionConsume(this.constructionConsume - constructionConsume);
				}
				try {
					if (JiazuManager2.instance.noticeNum.get(this.getLevel()) != null) {
						int num = JiazuManager2.instance.noticeNum.get(this.getLevel());
						if (this.getJiazuMoney() < num) {
							JiazuManager2.instance.noticeJizuMondeyNotEnough(this);
						}
					}
				} catch (Exception e) {
					JiazuManager2.logger.error("[新家族] [发送家族资金不足提醒] [异常] [" + this.getLogString() + "]", e);
				}
			}
		} finally {
			rwlock.writeLock().unlock();
		}
		return true;
	}

	public int getLandNum() {
		return landNum;
	}

	public void setLandNum(int landNum) {
		this.landNum = landNum;
		notifyFieldChange("landNum");
	}

	public String toString() {
		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer buffer = new StringBuffer();
		buffer.append("家族ID:").append(this.jiazuID).append("\r\n");
		buffer.append("家族名称:").append(this.name).append("\r\n");
		buffer.append("创建时间:").append(format.format(new java.util.Date(this.createTime))).append("\r\n");
		buffer.append("家族徽章ID:").append(this.iconID).append("\r\n");
		buffer.append("家族驻地ID:").append(this.baseID).append("\r\n");
		buffer.append("级别:").append(this.level).append("\r\n");
		buffer.append("家族标语:").append(this.slogan).append("\r\n");
		buffer.append("家族密码:").append(this.jiazuPassword).append("\r\n");
		buffer.append("家族灵石:").append(this.jiazuMoney).append("\r\n");
		buffer.append("家族领地个数:").append(this.landNum).append("\r\n");
		buffer.append("家族灵脉值:").append(this.constructionConsume).append("\r\n");
		buffer.append("家族驻地的繁荣度:").append(this.prosperity).append("\r\n");
		buffer.append("本周的工资的总和:").append(this.salarySum).append("\r\n");
		buffer.append("本周的工资的剩余:").append(this.salaryLeft).append("\r\n");
		buffer.append("单个玩家本周发放工资的上限:").append(this.singleMemberSalaryMax).append("\r\n");
		buffer.append("贡献度之和:").append(this.contributionSum).append("\r\n");
		buffer.append("玩家Member ID:").append("\r\n");
		int i = 0;
		for (Long id : this.memberSet) {
			if (++i % 10 == 0) buffer.append("\r\n");
			buffer.append(id + "\t");
		}

		return buffer.toString();
	}

	public long getSingleMemberSalaryMax() {
		return singleMemberSalaryMax;
	}

	public void setSingleMemberSalaryMax(long singleMemberSalaryMax) {
		this.singleMemberSalaryMax = singleMemberSalaryMax;
		notifyFieldChange("singleMemberSalaryMax");
	}

	public long getJiazuMaster() {
		ArrayList<JiazuMember> list = JiazuManager.getInstance().getJiazuMember(jiazuID, JiazuTitle.master);
		if (list == null || list.size() == 0) return -1;
		JiazuMember jm = list.get(0);
		return jm.getPlayerID();
	}

	public void setJiazuPasswordHint(String jiazuPasswordHint) {
		this.jiazuPasswordHint = jiazuPasswordHint;
		notifyFieldChange("jiazuPasswordHint");
	}

	public String getJiazuPasswordHint() {
		return jiazuPasswordHint;
	}

	private long zongPaiId = -1;

	public void setZongPaiId(long zongPaiId) {
		this.zongPaiId = zongPaiId;
		notifyFieldChange("zongPaiId");
	}

	public long getZongPaiId() {
		return this.zongPaiId;
	}

	public static void main(String[] args) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT-8"));
		System.out.println(c.get(Calendar.WEEK_OF_YEAR));
		c.add(Calendar.DAY_OF_YEAR, -1);
		System.out.println(c.get(Calendar.WEEK_OF_YEAR));
	}

	public void setMember4Clients(ArrayList<JiazuMember4Client> member4Clients) {
		this.member4Clients = member4Clients;
	}

	public ArrayList<JiazuMember4Client> getMember4Clients() {
		if (member4Clients.size() == 0 || memberModify) {
			initMember4Client();
			memberModify = false;
		}
		return member4Clients;
	}

	public ArrayList<Integer> getBedges() {
		return bedges;
	}

	public void setBedges(ArrayList<Integer> bedges) {
		this.bedges = bedges;
		notifyFieldChange("bedges");
	}

	public int getUsedBedge() {
		return usedBedge;
	}

	public void setUsedBedge(int usedBedge) {
		this.usedBedge = usedBedge;
		notifyFieldChange("usedBedge");
	}

	public String getTitleDes(JiazuTitle title) {
		return Translate.translateString(Translate.家族title, new String[][] { { Translate.STRING_1, getName() }, { Translate.STRING_2, titleAlias[title.ordinal()] } });// getName()
																																										// + " 的 " +
																																										// titleAlias[title.ordinal()];
	}

	public long getLastReleaseSilkwormTime() {
		return lastReleaseSilkwormTime;
	}

	public void setLastReleaseSilkwormTime(long lastReleaseSilkwormTime) {
		this.lastReleaseSilkwormTime = lastReleaseSilkwormTime;
	}

	public int getTotalReleaseSilkwormTimes() {
		return totalReleaseSilkwormTimes;
	}

	public void setTotalReleaseSilkwormTimes(int totalReleaseSilkwormTimes) {
		this.totalReleaseSilkwormTimes = totalReleaseSilkwormTimes;
		notifyFieldChange("totalReleaseSilkwormTimes");
	}

	public int getCurrentReleaseSilkwormTimes() {
		return currentReleaseSilkwormTimes;
	}

	public void setCurrentReleaseSilkwormTimes(int currentReleaseSilkwormTimes) {
		this.currentReleaseSilkwormTimes = currentReleaseSilkwormTimes;
		notifyFieldChange("currentReleaseSilkwormTimes");
	}

	public int[] getFeedDeliverNum() {
		return feedDeliverNum;
	}

	public void setFeedDeliverNum(int[] feedDeliverNum) {
		this.feedDeliverNum = feedDeliverNum;
		notifyFieldChange("feedDeliverNum");
	}

	public int[] getForLuckFriutNums() {
		return forLuckFriutNums;
	}

	public void setForLuckFriutNums(int[] forLuckFriutNums) {
		this.forLuckFriutNums = forLuckFriutNums;
		notifyFieldChange("forLuckFriutNums");
	}

	public ForLuckFruitNPC[] getFruitNPCs() {
		return fruitNPCs;
	}

	public void setFruitNPCs(ForLuckFruitNPC[] fruitNPCs) {
		this.fruitNPCs = fruitNPCs;
	}

	public long getConstructionConsume() {
		return constructionConsume;
	}

	public void setConstructionConsume(long constructionConsume) {
		this.constructionConsume = constructionConsume;
		notifyFieldChange("constructionConsume");
	}

	public void doOnDestroy() {
		for (ForLuckFruitNPC npc : getFruitNPCs()) {
			if (npc != null) {
				npc.autoHarvest();
				if (JiazuManager.logger.isInfoEnabled()) JiazuManager.logger.info("[系统关闭自动回收家族的祝福果树][家族id:{}名字:{}]", new Object[] { getJiazuID(), getName() });
			}
		}
	}

	public synchronized void initMember() {
		Set<JiazuMember> set = JiazuManager.getInstance().getJiazuMember(jiazuID);
		List<JiazuMember> result = new ArrayList<JiazuMember>();
		result.addAll(set);
		setMembers(result);
	}

	public synchronized void initMember4Client() {
		try {
			ArrayList<JiazuMember4Client> member4Clients = new ArrayList<JiazuMember4Client>();
			for (JiazuMember jm : JiazuManager.getInstance().getJiazuMember(jiazuID)) {
				if (jm == null) {
					continue;
				}
				JiazuMember4Client jiazuMember4Client = new JiazuMember4Client(jm);
				if (jiazuMember4Client.getPlayerId() != 0) {
					member4Clients.add(jiazuMember4Client);
				}
			}
			if (getTitleAlias() == null) {
				String[] titles = new String[JiazuTitle.values().length];
				for (int i = 0; i < JiazuTitle.values().length; i++) {
					titles[i] = JiazuTitle.values()[i].getName();
				}
				setTitleAlias(titles);
			}

			Collections.sort(member4Clients);

			setMember4Clients(member4Clients);
		} catch (Exception e) {
			JiazuSubSystem.logger.error("[加载家族异常]", e);
		}
	}

	public boolean isFull() {
		return getMemberSet().size() >= PEOPLE_NUM_MAX[getLevel()<0?0:getLevel()];
	}

	/**
	 * 得到家族当前在线的成员的ID
	 * @return
	 */
	public ArrayList<Player> getOnLineMembers() {
		PlayerManager playerManager = GamePlayerManager.getInstance();
		ArrayList<Player> onLines = new ArrayList<Player>();
		Set<JiazuMember> members = JiazuManager.getInstance().getJiazuMember(jiazuID);
		for (JiazuMember jiazuMember : members) {
			if (playerManager.isOnline(jiazuMember.getPlayerID())) {
				try {
					Player player = playerManager.getPlayer(jiazuMember.getPlayerID());
					if (player != null) {
						onLines.add(player);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return onLines;
	}

	/**
	 * 得到正在建造的建筑
	 * 
	 * @return
	 */
	public SeptBuildingEntity getInbuildingEntity() {
		SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(getJiazuID());
		if (septStation != null) {
			for (SeptBuildingEntity sbe : septStation.getBuildingEntities()) {
				if (sbe.isInBuild()) {
					return sbe;
				}
			}
		}
		return null;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
		notifyFieldChange("country");
	}

	public List<JiazuMember> getMembers() {
		return members;
	}

	public void setMembers(List<JiazuMember> members) {
		this.members = members;
	}

	public void notifyFieldChange(String fieldName) {
		JiazuManager.jiazuEm.notifyFieldChange(this, fieldName);
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getLastCallinTime() {
		return lastCallinTime;
	}

	public void setLastCallinTime(long lastCallinTime) {
		this.lastCallinTime = lastCallinTime;
		notifyFieldChange("lastCallinTime");
	}

	public long getCycleCallinTimes() {
		return cycleCallinTimes;
	}

	public void setCycleCallinTimes(long cycleCallinTimes) {
		this.cycleCallinTimes = cycleCallinTimes;
		notifyFieldChange("cycleCallinTimes");
	}

	public long getTotalCallinTimes() {
		return totalCallinTimes;
	}

	public void setTotalCallinTimes(long totalCallinTimes) {
		this.totalCallinTimes = totalCallinTimes;
		notifyFieldChange("totalCallinTimes");
	}

	public long getLastSalaryCeremonyTime() {
		return lastSalaryCeremonyTime;
	}

	public void setLastSalaryCeremonyTime(long lastSalaryCeremonyTime) {
		this.lastSalaryCeremonyTime = lastSalaryCeremonyTime;
		notifyFieldChange("lastSalaryCeremonyTime");
	}

	public long getCycleSalaryCeremonyTimes() {
		return cycleSalaryCeremonyTimes;
	}

	public void setCycleSalaryCeremonyTimes(long cycleSalaryCeremonyTimes) {
		this.cycleSalaryCeremonyTimes = cycleSalaryCeremonyTimes;
		notifyFieldChange("cycleSalaryCeremonyTimes");
	}

	public long getTotalSalaryCeremonyTimes() {
		return totalSalaryCeremonyTimes;
	}

	public void setTotalSalaryCeremonyTimes(long totalSalaryCeremonyTimes) {
		this.totalSalaryCeremonyTimes = totalSalaryCeremonyTimes;
		notifyFieldChange("totalSalaryCeremonyTimes");
	}

	public boolean isInSilkworm() {
		return inSilkworm;
	}

	public void setInSilkworm(boolean inSilkworm) {
		this.inSilkworm = inSilkworm;
	}

	public long getSilkwormDoTaskEndTime() {
		return silkwormDoTaskEndTime;
	}

	public void setSilkwormDoTaskEndTime(long silkwormDoTaskEndTime) {
		this.silkwormDoTaskEndTime = silkwormDoTaskEndTime;
	}

	public long getSilkwormGroupUpTime() {
		return silkwormGroupUpTime;
	}

	public void setSilkwormGroupUpTime(long silkwormGroupUpTime) {
		this.silkwormGroupUpTime = silkwormGroupUpTime;
	}

	public long getSilkwormDisappear() {
		return silkwormDisappear;
	}

	public void setSilkwormDisappear(long silkwormDisappear) {
		this.silkwormDisappear = silkwormDisappear;
	}

	public int getSilkwormStatus() {
		return silkwormStatus;
	}

	public void setSilkwormStatus(int silkwormStatus) {
		this.silkwormStatus = silkwormStatus;
	}

	public int getCurrentWeekCallbossTimes() {
		return currentWeekCallbossTimes;
	}

	public void setCurrentWeekCallbossTimes(int currentWeekCallbossTimes) {
		this.currentWeekCallbossTimes = currentWeekCallbossTimes;
		notifyFieldChange("currentWeekCallbossTimes");
	}

	public long getLastCallbossTime() {
		return lastCallbossTime;
	}

	public void setLastCallbossTime(long lastCallbossTime) {
		this.lastCallbossTime = lastCallbossTime;
		notifyFieldChange("lastCallbossTime");
	}

	public void setWareHouse(ArrayList<Cell> wareHouse) {
		this.wareHouse = wareHouse;
	}

	public ArrayList<Cell> getWareHouse() {
		return wareHouse;
	}

	// 添加物品 最多40个

	public synchronized boolean addToWareHouse(ArticleEntity entity, int num) {
		boolean isADD = false;
		ArrayList<Cell> delete = new ArrayList<Cell>();
		for (int i = 0; i < wareHouse.size(); i++) {
			Cell cell = wareHouse.get(i);
			ArticleEntity ent = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
			if (ent == null) {
				if (JiazuManager.logger.isWarnEnabled()) JiazuManager.logger.warn("[家族仓库物品不存在] [jd={}] [Eid={}] [num={}]", new Object[] { getJiazuID(), cell.getEntityId(), cell.getCount() });
				delete.add(cell);
				continue;
			}
			Article article = ArticleManager.getInstance().getArticle(ent.getArticleName());
			if (article == null) {
				if (JiazuManager.logger.isWarnEnabled()) JiazuManager.logger.warn("[家族仓库物品模板不存在] [jd={}] [Eid={}] [Ename={}] [num={}]", new Object[] { getJiazuID(), cell.getEntityId(), ent.getArticleName(), cell.getCount() });
				delete.add(cell);
				continue;
			}
			// 名字一样，颜色一样，绑定一样，可以堆叠 未超过堆叠数量
			if (ent.getArticleName().equals(entity.getArticleName()) && ent.getColorType() == entity.getColorType() && ent.isBinded() == entity.isBinded() && article.isOverlap() && cell.getCount() + num <= article.getOverLapNum()) {
				isADD = true;
				int oldCound = cell.getCount();
				cell.setCount(cell.getCount() + num);
				if (JiazuManager.logger.isWarnEnabled()) JiazuManager.logger.warn("[家族仓库堆叠添加] [jd={}] [o={}] [Eid={}] [Ename={}] [num={}] [wS={}]", new Object[] { getJiazuID(), oldCound + "~" + i, entity.getId(), entity.getArticleName(), num, wareHouse.size() });
				notifyFieldChange("wareHouse");
				return true;
			}
		}

		for (int i = 0; i < delete.size(); i++) {
			wareHouse.remove(delete.get(i));
		}
		delete.clear();

		if (!isADD) {
			if (wareHouse.size() >= JiazuManager.WAREHOUSE_MAX) {
				if (JiazuManager.logger.isWarnEnabled()) JiazuManager.logger.warn("[家族仓库满了] [jd={}] [Eid={}] [Ename={}] [num={}]", new Object[] { getJiazuID(), entity.getId(), entity.getArticleName(), num });
				return false;
			}
			Cell newcell = new Cell();
			newcell.setEntityId(entity.getId());
			newcell.setCount(num);
			wareHouse.add(newcell);
			ArticleEntityManager.getInstance().putToRealSaveCache(entity);
			notifyFieldChange("wareHouse");
			if (JiazuManager.logger.isWarnEnabled()) JiazuManager.logger.warn("[家族仓库添加] [jd={}] [Eid={}] [Ename={}] [num={}] [wS={}]", new Object[] { getJiazuID(), entity.getId(), entity.getArticleName(), num, wareHouse.size() });
		}
		return true;
	}

	// 分配物品给他人
	public synchronized void putWareHouseToPlayer(Player player, int index, int num) {
		Cell cell = wareHouse.get(index);
		if (cell == null) {
			if (JiazuManager.logger.isWarnEnabled()) JiazuManager.logger.warn("[发物品] [家族仓库cell出错] [jd={}] [index={}] [num={}]", new Object[] { getJiazuID(), index, num });
			wareHouse.remove(cell);
			notifyFieldChange("wareHouse");
			return;
		}
		ArticleEntity ent = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
		if (ent == null) {
			if (JiazuManager.logger.isWarnEnabled()) JiazuManager.logger.warn("[发物品] [家族仓库物品不存在] [jd={}] [Eid={}] [num={}]", new Object[] { getJiazuID(), cell.getEntityId(), cell.getCount() });
			wareHouse.remove(cell);
			notifyFieldChange("wareHouse");
			return;
		}
		try {
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ent }, new int[] { num }, Translate.家族, "", 0, 0, 0, Translate.家族);
		} catch (Exception e) {
			JiazuManager.logger.error("家族发物品出错:[" + player.getId() + "] [" + cell.getEntityId() + "] [" + cell.getCount() + "] [", e);
			return;
		}

		if (cell.getCount() > num) {
			cell.setCount(cell.getCount() - num);
		} else {
			wareHouse.remove(cell);
		}
		notifyFieldChange("wareHouse");
	}

	public long getUpdateProsperityTime() {
		return updateProsperityTime;
	}

	public void setUpdateProsperityTime(long updateProsperityTime) {
		this.updateProsperityTime = updateProsperityTime;
		notifyFieldChange("updateProsperityTime");
	}

	// 用于排行
	private long warScore;

	public long getWarScore() {
		return warScore;
	}

	public void setWarScore(long warScore) {
		this.warScore = warScore;
		notifyFieldChange("warScore");
	}

	public long getLastCalculateSalaryTime() {
		return lastCalculateSalaryTime;
	}

	public void setLastCalculateSalaryTime(long lastCalculateSalaryTime) {
		this.lastCalculateSalaryTime = lastCalculateSalaryTime;
		notifyFieldChange("lastCalculateSalaryTime");
	}

	public long getLastJiazuSilverCartime() {
		return lastJiazuSilverCartime;
	}

	public void setLastJiazuSilverCartime(long lastJiazuSilverCartime) {
		this.lastJiazuSilverCartime = lastJiazuSilverCartime;
		notifyFieldChange("lastJiazuSilverCartime");
	}

	public int maxMember() {
		if (getLevel() == 0) {
			return PEOPLE_NUM_MAX[0];
		}
		if (this.getBaseID() <= 0) {
			return PEOPLE_NUM_MAX[0];
		}
		SeptStation septStation = SeptStationManager.getInstance().getSeptStationById(getBaseID());
		if (septStation != null) {
			int level = septStation.getBuildingLevel(BuildingType.仙灵洞);
			if (level >= 0) {
				return PEOPLE_NUM_MAX[level];
			}
		}
		return PEOPLE_NUM_MAX[0];
	}

	public String getLogString() {
		return " [家族:" + this.getJiazuID() + "/" + this.getName()+ "/" + this.getLevel() + "] ";
	}

	public byte getJiazuStatus() {
		return jiazuStatus;
	}

	public void setJiazuStatus(byte jiazuStatus) {
		this.jiazuStatus = jiazuStatus;
		notifyFieldChange("jiazuStatus");
	}

	public int getCurrentReleaseLianDan() {
		return currentReleaseLianDan;
	}

	public void setCurrentReleaseLianDan(int currentReleaseLianDan) {
		this.currentReleaseLianDan = currentReleaseLianDan;
		notifyFieldChange("currentReleaseLianDan");
	}

	public long getLastReleaseLianDanTime() {
		return lastReleaseLianDanTime;
	}

	public void setLastReleaseLianDanTime(long lastReleaseLianDanTime) {
		this.lastReleaseLianDanTime = lastReleaseLianDanTime;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
		notifyFieldChange("point");
	}
	
}
