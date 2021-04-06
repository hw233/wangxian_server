package com.fy.engineserver.activity.fairyBuddha;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.res.Avata;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.FAIRY_SHOW_ELECTORS_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.fy.engineserver.util.config.ServerFit4Activity;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.WordFilter;

public class FairyBuddhaManager implements EventProc {
	// TODO 配logback.xml
	public static Logger logger = LoggerFactory.getLogger(FairyBuddhaManager.class);
	public static FairyBuddhaManager instance;
	private Map<Byte, FairyBuddhaInfo> fairyBuddhaMap = new Hashtable<Byte, FairyBuddhaInfo>();// 上周选出的仙尊(启动服务器时从ddc读到内存)
	// private Map<Byte, List<FairyBuddhaInfo>> electorMap = new Hashtable<Byte, List<FairyBuddhaInfo>>();// 本周参选仙尊的玩家(启动服务器时从ddc读到内存)
	private List<Long> voterIdList = new Vector<Long>();// <"投票",Set<投票者id>>(启动服务器时从ddc读到内存)
	private List<Long> worshipIdList = new Vector<Long>();// <"膜拜",Set<膜拜者id>>(启动服务器时从ddc读到内存)

	private List<VoteAward> voteAwardList = new ArrayList<VoteAward>(); // 投票奖励
	private List<ThankAward> thankAwardList = new ArrayList<ThankAward>(); // 答谢奖励
	private List<WorshipAward> worshipAwardList = new ArrayList<WorshipAward>(); // 膜拜奖励
	private List<DefaultFairyNpcData> defaultNpcList = new ArrayList<DefaultFairyNpcData>();// 默认仙尊npc数据

	private String diskFile;
	public DiskCache disk = null;// <"年_周_仙尊",Map<仙尊职业,仙尊>>,<"年_周_参选者",Map<参选者职业,List<参选者>>>,<"年_周__膜拜奖励等级_仙尊id",byte>,<"年_周__答谢奖励等级_仙尊id",byte>,<"年_周",int(选没选过仙尊,有值表示选过,没值表示没选过)>,<"历届仙尊",List<FairybuddhaInfo>>
	public static final String KEY_参选者 = "_参选者"; // 使用时拼写成"年_周__参选者"
	public static final String KEY_仙尊 = "_仙尊"; // 使用时拼写成"年_周__仙尊"
	public static final String KEY_膜拜奖励等级 = "_膜拜奖励等级"; // 使用时拼写成"年_周_膜拜奖励等级_仙尊id"
	public static final String KEY_答谢奖励等级 = "_答谢奖励等级"; // 使用时拼写成"年_周_答谢奖励等级_仙尊id"
	public static final String KEY_已选过仙尊 = "_已选过仙尊"; // 使用时拼写成"年_周_已选过仙尊"
	public static final String KEY_历届仙尊 = "历届仙尊";
	public static int LIMITLEVEL = 40;// 投票限制等级
	public static int MAX_ELECTORS = 50;// 每个职业最大的入榜数量
	public static boolean open = true;

	public List<StatueForFairyBuddha> statueList = new ArrayList<StatueForFairyBuddha>();// 当选的仙尊形象

	private String filePath;

	public static FairyBuddhaManager getInstance() {
		return instance;
	}

	public Map<Byte, FairyBuddhaInfo> getFairyBuddhaMap() {
		return fairyBuddhaMap;
	}

	public void setFairyBuddhaMap(Map<Byte, FairyBuddhaInfo> fairyBuddhaMap) {
		this.fairyBuddhaMap = fairyBuddhaMap;
	}

	// public Map<Byte, List<FairyBuddhaInfo>> getElectorMap() {
	// return electorMap;
	// }
	//
	// public void setElectorMap(Map<Byte, List<FairyBuddhaInfo>> electorMap) {
	// this.electorMap = electorMap;
	// }

	public List<Long> getVoterIdList() {
		return voterIdList;
	}

	public void setVoterIdList(List<Long> voterIdList) {
		this.voterIdList = voterIdList;
	}

	public List<Long> getWorshipIdList() {
		return worshipIdList;
	}

	public void setWorshipIdList(List<Long> worshipIdList) {
		this.worshipIdList = worshipIdList;
	}

	public List<VoteAward> getVoteAwardList() {
		return voteAwardList;
	}

	public void setVoteAwardList(List<VoteAward> voteAwardList) {
		this.voteAwardList = voteAwardList;
	}

	public List<ThankAward> getThankAwardList() {
		return thankAwardList;
	}

	public void setThankAwardList(List<ThankAward> thankAwardList) {
		this.thankAwardList = thankAwardList;
	}

	public List<WorshipAward> getWorshipAwardList() {
		return worshipAwardList;
	}

	public void setWorshipAwardList(List<WorshipAward> worshipAwardList) {
		this.worshipAwardList = worshipAwardList;
	}

	public List<DefaultFairyNpcData> getDefaultNpcList() {
		return defaultNpcList;
	}

	public void setDefaultNpcList(List<DefaultFairyNpcData> defaultNpcList) {
		this.defaultNpcList = defaultNpcList;
	}

	public List<StatueForFairyBuddha> getStatueList() {
		return statueList;
	}

	public void setStatueList(List<StatueForFairyBuddha> statueList) {
		this.statueList = statueList;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDiskFile() {
		return diskFile;
	}

	public void setDiskFile(String diskFile) {
		this.diskFile = diskFile;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 加入投票榜
	 * @param p
	 */
	public void joinElector(Player p, int soulType) {
		try {
			byte career = p.getSoul(soulType).getCareer();
			// Soul soul = p.getCurrSoul();
			Avata a = ResourceManager.getInstance().getAvata(p);
			ResourceManager.getInstance().getAvata(p);
			// String[] avata = p.getAvata();
			// if (career == 5 && p.isShouStatus() && p.oldAvata != null) {
			// avata = p.oldAvata;
			// }
			StatueForFairyBuddha statue = new StatueForFairyBuddha(p.getName(), career, p.getMaxHP(), p.getPhyAttack(), p.getMagicAttack(), a.avata, p.getAvataRace(), p.getAvataSex(), a.avataType);
			// 下面这个"000"是跟客户端约定的,不要擅自更改
			FairyBuddhaInfo fbi = new FairyBuddhaInfo(p.getId(), p.getName(), career, p.getSoulLevel(), p.getCountry(), 0, 0, "000", new Vector<Voter>(), new Vector<Voter>(), statue);
			Map<Byte, List<FairyBuddhaInfo>> electorMap = getCurrentElectorMap(getKey(0) + KEY_参选者);
			if (electorMap.get(career) == null) {
				electorMap.put(career, new Vector<FairyBuddhaInfo>());
			}
			electorMap.get(career).add(fbi);
			Collections.sort(electorMap.get(career), order);
			disk.put(getKey(0) + KEY_参选者, (Serializable) electorMap);
			logger.warn("[" + p.getLogString() + "] [仙尊] [加入投票榜] [soulType:" + soulType + "] [职业:" + career + "] [国家:" + p.getCountry() + "]");
		} catch (Exception e) {
			FairyBuddhaManager.logger.warn("[" + p.getLogString() + "] [仙尊] [加入投票榜异常]" + e);
			e.printStackTrace();
		}
	}

	/**
	 * 加入上期投票榜
	 * @param p
	 */
	public void joinLastElector(Player p, int soulType) {
		try {
			byte career = p.getSoul(soulType).getCareer();
			Soul soul = p.getCurrSoul();
			StatueForFairyBuddha statue = new StatueForFairyBuddha(p.getName(), career, soul.getMaxHp(), soul.getPhyAttack(), soul.getMagicAttack(), p.getAvata(), p.getAvataRace(), p.getAvataSex(), p.getAvataType());
			// 下面这个"000"是跟客户端约定的,不要擅自更改
			FairyBuddhaInfo fbi = new FairyBuddhaInfo(p.getId(), p.getName(), career, p.getSoulLevel(), p.getCountry(), 0, 0, "000", new Vector<Voter>(), new Vector<Voter>(), statue);
			Map<Byte, List<FairyBuddhaInfo>> electorMap = getCurrentElectorMap(getKey(-1) + KEY_参选者);
			if (electorMap.get(career) == null) {
				electorMap.put(career, new Vector<FairyBuddhaInfo>());
			}
			electorMap.get(career).add(fbi);
			Collections.sort(electorMap.get(career), order);
			disk.put(getKey(-1) + KEY_参选者, (Serializable) electorMap);
			logger.warn("[" + p.getLogString() + "] [仙尊] [加入上期投票榜] [soulType:" + soulType + "] [职业:" + career + "] [国家:" + p.getCountry() + "]");
		} catch (Exception e) {
			FairyBuddhaManager.logger.warn("[" + p.getLogString() + "] [仙尊] [加入上期投票榜异常]" + e);
			e.printStackTrace();
		}
	}

	public Map<Byte, List<FairyBuddhaInfo>> getCurrentElectorMap(String key) {
		if (null != disk.get(key)) {
			Map<Byte, List<FairyBuddhaInfo>> currentElectorMap = (Map<Byte, List<FairyBuddhaInfo>>) disk.get(key);
			return currentElectorMap;
		}
		return new Hashtable<Byte, List<FairyBuddhaInfo>>();
	}

	/**
	 * 查看投票榜时获得玩家身份
	 * @param p
	 * @param career
	 *            仙尊职业
	 * @return 查看者身份:0-普通,1-竞选者,2-仙尊,3-本期既是仙尊又是参选者
	 */
	public int getPlayerRole(Player p, byte career, Map<Byte, Vector<FairyBuddhaInfo>> electorMap, int cycle) {
		if (p.getCurrSoul().getCareer() == career) {
			if (fairyBuddhaMap.size() > 0) {
				FairyBuddhaInfo fbi = fairyBuddhaMap.get(career);
				if (fbi != null) {
					if (p.getId() == fbi.getId()) {
						if (cycle == 0) {
							if (electorMap.size() > 0) {
								List<FairyBuddhaInfo> fbiList = electorMap.get(career);
								if (fbiList != null && fbiList.size() > 0) {
									for (FairyBuddhaInfo fbInfo : fbiList) {
										if (p.getId() == fbInfo.getId()) {
											return 3;
										}
									}
								}
							}
						}
						return 2;
					}
				}
			}
			if (electorMap.size() > 0) {
				List<FairyBuddhaInfo> fbiList = electorMap.get(career);
				if (fbiList != null && fbiList.size() > 0) {
					for (FairyBuddhaInfo fbInfo : fbiList) {
						if (p.getId() == fbInfo.getId()) {
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 根据职业获取当前该职业在投票榜中的现有人数
	 * @param career
	 * @return
	 */
	public int getElectorNumByCareer(byte career) {
		Map<Byte, List<FairyBuddhaInfo>> electorMap = getCurrentElectorMap(getKey(0) + KEY_参选者);
		if (electorMap.get(career) != null) {
			return electorMap.get(career).size();
		}
		return 0;
	}

	/**
	 * 获取年周
	 * @return "年_周",i=0:本周;i=1:下周;i=-1:上周
	 */
	public String getKey(int i) {
		Calendar c = Calendar.getInstance();
		if ((c.get(c.WEEK_OF_YEAR) + i) < 0) {
			// TODO 注意跨年的时候
			// return (c.get(c.YEAR)-1) + "_" + c.get(c.);
		}
		return c.get(c.YEAR) + "_" + (c.get(c.WEEK_OF_YEAR) + i);
	}

	/**
	 * 获取年周
	 * @return "年_日_时",i=0:本小时;i=1:下小时;i=-1:上小时
	 */
	// public String getKey(int i) {
	// Calendar c = Calendar.getInstance();
	// if ((c.get(c.HOUR_OF_DAY) + i) < 0) {
	// return c.get(c.YEAR) + "_" + (c.get(c.DAY_OF_YEAR) - 1) + "_" + "23";
	// } else {
	// return c.get(c.YEAR) + "_" + c.get(c.DAY_OF_YEAR) + "_" + (c.get(c.HOUR_OF_DAY) + i);
	// }
	// }

	// public String getKey(int i) {
	// Calendar c = Calendar.getInstance();
	// int min = c.get(c.MINUTE) / 30;
	// if ((min + i) < 0) {
	// return c.get(c.YEAR) + "_" + c.get(c.DAY_OF_YEAR) + "_" + (c.get(c.HOUR_OF_DAY) - 1) + "_" + "1";
	// } else {
	// return c.get(c.YEAR) + "_" + c.get(c.DAY_OF_YEAR) + "_" + c.get(c.HOUR_OF_DAY) + "_" + (min + i);
	// }
	// }

	// public String getKey(int i) {
	// Calendar c = Calendar.getInstance();
	// int min = c.get(c.MINUTE) / 5;
	// if ((c.get(c.HOUR_OF_DAY) + i) < 0) {
	// return c.get(c.YEAR) + "_" + (c.get(c.DAY_OF_YEAR) - 1) + "_" + "23";
	// } else {
	// return c.get(c.YEAR) + "_" + c.get(c.DAY_OF_YEAR) + "_" + c.get(c.HOUR_OF_DAY) + "_" + (min + i);
	// }
	// }

	// public String getKey(int i) {
	// Calendar c = Calendar.getInstance();
	// int min = c.get(c.MINUTE) / 10;
	// if ((min + i) < 0) {
	// return c.get(c.YEAR) + "_" + c.get(c.DAY_OF_YEAR) + "_" + (c.get(c.HOUR_OF_DAY) - 1) + "_" + "5";
	// } else {
	// return c.get(c.YEAR) + "_" + c.get(c.DAY_OF_YEAR) + "_" + c.get(c.HOUR_OF_DAY) + "_" + (min + i);
	// }
	// }

	/**
	 * 判断玩家是否在投票榜里,在返回true,不在返回false
	 * @param id
	 * @return
	 */
	public boolean inElectors(long id, byte career) {
		Map<Byte, List<FairyBuddhaInfo>> electorMap = getCurrentElectorMap(getKey(0) + KEY_参选者);
		if (electorMap != null) {
			List<FairyBuddhaInfo> electors = electorMap.get(career);
			if (electors != null && electors.size() > 0) {
				for (FairyBuddhaInfo fbi : electors) {
					if (id == fbi.getId()) {
						FairyBuddhaManager.logger.warn("[" + id + "] [仙尊] [玩家在投票榜内]");
						return true;
					}
				}
			}
		}
		FairyBuddhaManager.logger.warn("[" + id + "] [仙尊] [没有参选者]");
		return false;
	}

	/**
	 * 投票榜排序
	 */
	private static Comparator<FairyBuddhaInfo> order = new Comparator<FairyBuddhaInfo>() {

		@Override
		public int compare(FairyBuddhaInfo o1, FairyBuddhaInfo o2) {
			if (o1.getScore() < o2.getScore()) {
				return 1;
			} else if (o1.getScore() == o2.getScore()) {
				if (TimeTool.formatter.varChar19.parse(o1.getLastChangeTime()) > TimeTool.formatter.varChar19.parse(o2.getLastChangeTime())) {
					return 1;
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		}

	};
	/**
	 * 查看投票记录排序
	 */
	private static Comparator<Voter> orderVoter = new Comparator<Voter>() {

		@Override
		public int compare(Voter o1, Voter o2) {
			if (TimeTool.formatter.varChar19.parse(o1.getTime()) < TimeTool.formatter.varChar19.parse(o2.getTime())) {
				return 1;
			} else {
				return -1;
			}
		}

	};

	/**
	 * 选举
	 * @param voter
	 *            投票者
	 * @param elector
	 *            被投票者id
	 * @return
	 */
	public String doVote(Player voter, long electorId, byte career) {
		try {
			Map<Byte, List<FairyBuddhaInfo>> electorMap = getCurrentElectorMap(getKey(0) + KEY_参选者);
			FairyBuddhaInfo elector = getElectorById(electorId, career, electorMap);
			if (elector == null) {
				logger.warn("[" + voter.getLogString() + "] [仙尊] [您要投票的玩家不在投票榜内]");
				return Translate.不在投票榜内;
			} else {
				Calendar c = Calendar.getInstance();
				// if (!openVote) {
				// logger.warn("[" + voter.getLogString() + "] [仙尊] [非投票时间]");
				// return Translate.不是投票时间;
				// }
				// if (c.get(c.DAY_OF_WEEK) == 1) {
				// return Translate.不是投票时间;
				// }
				if (voter.getCurrSoul().getCareer() != elector.getCareer()) {
					logger.warn("[" + voter.getLogString() + "] [仙尊] [投票失败] [非本职业:投票者职业" + voter.getCurrSoul().getCareer() + "被投票者职业" + elector.getCareer() + "]");
					return Translate.非本职业不能投票;
				} else if (voter.getLevel() < LIMITLEVEL) {
					logger.warn("[" + voter.getLogString() + "] [仙尊] [投票失败] [等级不足]");
					return Translate.等级不够不能投票;
				} else if (voterIdList.size() > 0) {
					if (voterIdList.contains(voter.getId())) {
						logger.warn("[" + voter.getLogString() + "] [仙尊] [投票失败] [本期已投过]");
						return Translate.已经投过票;
					}
				}

				String notice = "";
				String notice2 = "";
				VoteAward va = getRightVoteAward(voteAwardList);
				if (va != null) {
					StringBuffer sbf = new StringBuffer();
					StringBuffer sbf2 = new StringBuffer();
					ActivityProp[] props = va.getProps();
					ArticleEntity[] aeArr = new ArticleEntity[props.length];
					for (int i = 0; i < props.length; i++) {
						Article a = ArticleManager.getInstance().getArticleByCNname(props[i].getArticleCNName());
						if (a != null) {
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, props[i].isBind(), ArticleEntityManager.仙尊投票, voter, props[i].getArticleColor(), props[i].getArticleNum(), true);
							if (ae != null) {
								aeArr[i] = ae;
								sbf.append(ae.toClickString() + "*" + props[i].getArticleNum() + ".");
								sbf2.append(ae.toColorString() + "*" + props[i].getArticleNum() + ".");
							} else {
								FairyBuddhaManager.logger.warn(voter.getLogString() + "[仙尊] [投票创建奖励物品失败] [物品名:" + props[i].getArticleCNName() + "]");
								return Translate.出现异常;
							}
						}
					}
					List<Player> players = new ArrayList<Player>();
					players.add(voter);
					ActivityManager.sendMailForActivity(players, props, Translate.投票奖励邮件标题, Translate.投票奖励邮件内容, "仙尊投票奖励");
					int voterLevel = voter.getCurrSoul().getGrade();
					/** 投票得经验奖励,经验奖励公式：获得经验=int(6*（玩家等级+1）^3+((玩家等级+2)^1.5)*1.5+10000*(玩家等级/10)^2.1) */
					double addExp = Math.floor(6 * Math.pow((voterLevel + 1), 3) + (Math.pow(voterLevel + 2, 1.5)) * 1.5 + 10000 * Math.pow(voterLevel / 10, 2.1));
					voter.addExp((long) addExp, ExperienceManager.仙尊投票);
					/** 投票得绑银奖励,绑银奖励公式：获得绑银=int((100+(玩家等级-39)^1.2)*0.4) 单位：两 */
					double addBindSilver = Math.floor(0.4 * (100 + Math.pow(voterLevel - 39, 1.2))) * 1000;
					BillingCenter.getInstance().playerSaving(voter, (long) addBindSilver, CurrencyType.BIND_YINZI, SavingReasonType.仙尊投票奖励, "仙尊");
					notice = Translate.translateString(Translate.投票成功提示, new String[][] { { Translate.STRING_1, (long) addExp + "" }, { Translate.STRING_2, BillingCenter.得到带单位的银两((long) addBindSilver) }, { Translate.STRING_3, sbf2.toString() } });
					notice2 = Translate.translateString(Translate.投票成功通知, new String[][] { { Translate.STRING_2, BillingCenter.得到带单位的银两((long) addBindSilver) }, { Translate.STRING_3, sbf.toString() } });
					int score = elector.getScore();
					Player p = PlayerManager.getInstance().getPlayer(electorId);
					if (p != null) {
						elector.setScore(score + (int) Math.floor(voter.getCurrSoul().getGrade() / 40));
						FairyBuddhaManager.logger.warn(p.getLogString() + "[仙尊] [投票得分] [得分:" + Math.floor(p.getCurrSoul().getGrade() / 40) + "] [得分后:" + elector.getScore() + "]");
					}
				}
				voterIdList.add(voter.getId());
				String time = sdf.format(System.currentTimeMillis());
				elector.getVoters().add(new Voter(voter.getId(), voter.getName(), time));
				elector.setLastChangeTime(time);
				Collections.sort(electorMap.get(career), order);// 参选者排序
				Collections.sort(elector.getVoters(), orderVoter); // 投票者排序
				disk.put(getKey(0) + KEY_参选者, (Serializable) electorMap);
				logger.warn("[" + voter.getLogString() + "] [仙尊] [投票后更新参选者信息,key:" + (getKey(0) + KEY_参选者) + "]");
				logger.warn("[" + voter.getLogString() + "] [仙尊] [投票成功，" + notice + "]");
				voter.sendWinNotice(notice);
				voter.sendNotice(notice2);
				Collections.sort(electorMap.get(career), order);
				// 这个返回是跟客户端约定的,不要随意更改
				return "000";
			}
		} catch (Exception e) {
			logger.warn("[" + voter.getLogString() + "] [仙尊]" + e);
			e.printStackTrace();
			return Translate.投票失败;
		}

	}

	/**
	 * 膜拜
	 * @param worshipper
	 *            膜拜者
	 * @param fairyBuddha
	 *            被膜拜的仙尊
	 * @return
	 */
	public String doWorship(Player worshipper, byte career, long fairyBuddhaId) {
		try {
			FairyBuddhaInfo fairyBuddha = getFariyBuddhaById(career, fairyBuddhaId);
			if (fairyBuddha != null) {
				if (worshipper.getCurrSoul().getCareer() != career) {
					logger.warn("[" + worshipper.getLogString() + "] [仙尊] [膜拜失败] [非本职业:膜拜者职业" + worshipper.getCurrSoul().getCareer() + "仙尊职业" + fairyBuddha.getCareer() + "]");
					return Translate.非本职业不能膜拜;
				} else if (worshipper.getLevel() < LIMITLEVEL) {
					logger.warn("[" + worshipper.getLogString() + "] [仙尊] [膜拜失败] [等级不足]");
					return Translate.等级不够不能膜拜;
				} else if (worshipIdList.size() > 0) {
					if (worshipIdList.contains(worshipper.getId())) {
						logger.warn("[" + worshipper.getLogString() + "] [仙尊] [膜拜失败] [已膜拜过]");
						return Translate.已经膜拜过;
					}
				}

				// TODO 奖励膜拜者,仙尊计数
				WeekdayAward wda = getRightWorshipAward(fairyBuddhaId);
				String notice = "";
				String notice2 = "";
				if (wda != null) {
					StringBuffer sbf = new StringBuffer();
					StringBuffer sbf2 = new StringBuffer();
					ActivityProp[] props = wda.getProps();
					if (props != null && props.length > 0) {
						for (int i = 0; i < props.length; i++) {
							Article a = ArticleManager.getInstance().getArticleByCNname(props[i].getArticleCNName());
							if (a != null) {
								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, props[i].isBind(), ArticleEntityManager.仙尊投票, worshipper, props[i].getArticleColor(), props[i].getArticleNum(), true);
								if (ae != null) {
									sbf.append(ae.toClickString() + "*" + props[i].getArticleNum() + ".");
									sbf2.append(ae.toColorString() + "*" + props[i].getArticleNum() + ".");
								} else {
									FairyBuddhaManager.logger.warn(worshipper.getLogString() + "[仙尊] [膜拜创建奖励物品失败] [物品名:" + props[i].getArticleCNName() + "]");
									return Translate.出现异常;
								}
							}
						}
						List<Player> players = new ArrayList<Player>();
						players.add(worshipper);
						ActivityManager.sendMailForActivity(players, props, Translate.膜拜奖励邮件标题, Translate.膜拜奖励邮件内容, "仙尊投票奖励");
					}
					int worshipperLevel = worshipper.getCurrSoul().getGrade();
					/** 膜拜得经验奖励公式:获得经验=int((6*（玩家等级+1）^3+((玩家等级+2)^1.5)*1.5+10000*(玩家等级/10)^2.1)*XXX/7) */

					FairyBuddhaManager.logger.warn("[仙尊] [膜拜奖励] [玩家等级:" + worshipperLevel + "] [系数:" + wda.getExpMul() + "]");
					double addExp = Math.floor((6 * Math.pow((worshipperLevel + 1), 3) + (Math.pow(worshipperLevel + 2, 1.5)) * 1.5 + 10000 * Math.pow(worshipperLevel / 10, 2.1)) * wda.getExpMul() / 7);
					worshipper.addExp((long) addExp, ExperienceManager.仙尊投票);
					/** 膜拜得绑银奖励,绑银奖励公式：获得绑银=int((100+(玩家等级-39)^1.2)*0.6*XXX/7) 单位：两 */
					double addBindSilver = Math.floor((0.6 * (100 + Math.pow(worshipper.getCurrSoul().getGrade() - 39, 1.2))) * wda.getSilverMul() / 7) * 1000;
					BillingCenter.getInstance().playerSaving(worshipper, (long) addBindSilver, CurrencyType.BIND_YINZI, SavingReasonType.仙尊膜拜奖励, "仙尊");
					notice = Translate.translateString(Translate.膜拜成功提示, new String[][] { { Translate.STRING_1, (long) addExp + "" }, { Translate.STRING_2, BillingCenter.得到带单位的银两((long) addBindSilver) }, { Translate.STRING_3, sbf2.toString() } });
					notice2 = Translate.translateString(Translate.膜拜成功通知, new String[][] { { Translate.STRING_2, BillingCenter.得到带单位的银两((long) addBindSilver) }, { Translate.STRING_3, sbf.toString() } });
					int score = fairyBuddha.getBeWorshipped();
					Player p = PlayerManager.getInstance().getPlayer(fairyBuddhaId);
					if (p != null) {
						fairyBuddha.setBeWorshipped(score + 1);
						FairyBuddhaManager.logger.warn(p.getLogString() + "[仙尊] [被膜拜得分] [得分后:" + fairyBuddha.getBeWorshipped() + "]");
					}
				}

				worshipIdList.add(worshipper.getId());
				String time = sdf.format(System.currentTimeMillis());
				if (isSameDay(time, fairyBuddha.getLastChangeTime())) {
					fairyBuddha.getWorshippers().add(new Voter(worshipper.getId(), worshipper.getName(), time));
				} else {
					Vector<Voter> worshippers = new Vector<Voter>();
					worshippers.add(new Voter(worshipper.getId(), worshipper.getName(), time));
					fairyBuddha.setWorshippers(worshippers);

				}
				// fairyBuddha.setLastChangeTime(time);//这个取消记录了,因为玩家既是仙尊又在投票榜里的时候,无法区分最后一次投票和膜拜这俩时间
				Collections.sort(fairyBuddha.getWorshippers(), orderVoter); // 排序
				disk.put(getKey(0) + KEY_仙尊, (Serializable) fairyBuddhaMap);
				logger.warn("[" + worshipper.getLogString() + "] [仙尊] [膜拜后更新仙尊信息,key:" + (getKey(0) + KEY_仙尊) + "]");
				logger.warn("[" + worshipper.getLogString() + "] [仙尊] [膜拜成功," + notice + "]");
				worshipper.sendWinNotice(notice);
				worshipper.sendNotice(notice2);
				return null;
			} else {
				logger.warn("[" + worshipper.getLogString() + "] [仙尊] [膜拜失败] [当前无仙尊]");
				return Translate.无仙尊提示;
			}
		} catch (Exception e) {
			logger.warn("[" + worshipper.getLogString() + "] [仙尊]" + e);
			e.printStackTrace();
			return Translate.出现异常;
		}

	}

	/**
	 * 判断两个"yyyy-MM-dd HH:mm:ss"格式的时间是否为同一天
	 * @param time1
	 * @param time2
	 * @return
	 */
	public boolean isSameDay(String time1, String time2) {
		return time1.split(" ")[0].equals(time2.split(" ")[0]);
	}

	/**
	 * 设置宣言
	 * @param electorId
	 * @param declaration
	 */
	public String changeDeclaration(long electorId, String declaration) {
		try {
			Player p = PlayerManager.getInstance().getPlayer(electorId);
			Map<Byte, List<FairyBuddhaInfo>> electorMap = getCurrentElectorMap(getKey(0) + KEY_参选者);
			FairyBuddhaInfo fbi = getElectorById(electorId, p.getCurrSoul().getCareer(), electorMap);
			if (p != null && fbi != null) {
				if (inElectors(electorId, p.getCurrSoul().getCareer())) {
					if (declaration.equals(fbi.getDeclaration())) {
						return Translate.宣言跟之前相同;
					}
					WordFilter filter = WordFilter.getInstance();
					boolean valid = filter.cvalid(declaration, 0) && filter.evalid(declaration, 1) && tagValid(declaration);
					if (!valid) {
						return Translate.宣言含有非法或敏感字符;
					}
					getElectorById(electorId, p.getCurrSoul().getCareer(), electorMap).setDeclaration(declaration);
					logger.warn("[仙尊] [设置宣言] [成功] [新宣言:" + declaration + "]");
					disk.put(getKey(0) + KEY_参选者, (Serializable) electorMap);
					logger.warn("[仙尊] [设置宣言] [成功] [设置新宣言后更新仙尊信息:" + (getKey(0) + KEY_参选者) + "]");
					return Translate.设置宣言成功;
				}
			}
		} catch (Exception e) {
			logger.error("[仙尊] [设置宣言] [异常:" + e + "]");
			e.printStackTrace();
		}
		return Translate.设置宣言失败;
	}

	public String tagforbid[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", " ", "　", "　" };
	public String tagforbid_korea[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", "　", "　" };

	/**
	 * 判断是否含有禁用的标签字符
	 * 
	 * @param name
	 * @return
	 */
	public boolean tagValid(String name) {
		String aname = name.toLowerCase();
		String[] temp = null;
		if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			temp = tagforbid_korea;
		} else {
			temp = tagforbid;
		}
		for (String s : temp) {
			if (aname.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据id获得被投票者信息
	 * @param id
	 * @return
	 */
	public FairyBuddhaInfo getElectorById(long id, byte career, Map<Byte, List<FairyBuddhaInfo>> electorsMap) {
		List<FairyBuddhaInfo> electorList = electorsMap.get(career);
		if (electorList != null) {
			for (FairyBuddhaInfo fbi : electorList) {
				if (fbi.getId() == id) {
					return fbi;
				}
			}
		}
		return null;
	}

	/**
	 * 根据职业和玩家id获得仙尊信息
	 * @param career
	 * @param id
	 * @return
	 */
	public FairyBuddhaInfo getFariyBuddhaById(byte career, long id) {
		FairyBuddhaInfo fbi = fairyBuddhaMap.get(career);
		if (fbi != null && fbi.getId() == id) {
			return fbi;
		}
		return null;
	}

	/**
	 * 根据time获得年_周
	 * @param time
	 * @return
	 */
	public String getYear_Week(long time) {
		Calendar c = Calendar.getInstance();
		return c.get(c.YEAR) + "_" + c.get(c.WEEK_OF_YEAR);
	}

	/**
	 * 根据time获得年_上周
	 * @param time
	 * @return
	 */
	public String getYear_LastWeek(long time) {
		Calendar c = Calendar.getInstance();
		return c.get(c.YEAR) + "_" + (c.get(c.WEEK_OF_YEAR) - 1);
	}

	/**
	 * 获取投票奖励
	 * @return
	 */
	public VoteAward getRightVoteAward(List<VoteAward> list) {
		if (list.size() > 0) {
			for (VoteAward va : list) {
				if (va.getSf4a().thiserverFit()) {
					long now = System.currentTimeMillis();
					if (TimeTool.formatter.varChar19.parse(va.getStartTime()) < now && now < TimeTool.formatter.varChar19.parse(va.getEndTime())) {
						return va;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取答谢奖励
	 * @return
	 */
	public ThankAward getRightThankAward(List<ThankAward> list, long fairBuddhaId) {
		if (list.size() > 0) {
			for (ThankAward ta : list) {
				if (ta.getSf4a().thiserverFit()) {
					long now = System.currentTimeMillis();
					if (TimeTool.formatter.varChar19.parse(ta.getStartTime()) < now && now < TimeTool.formatter.varChar19.parse(ta.getEndTime())) {
						return ta;
					}
				}
			}
			// 获取不到就返回list中的第一条,防止影响发协议
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获得答谢奖励物品名
	 * @param ta
	 * @param fairBuddhaId
	 * @return
	 */
	public String getThankAwardArticle(ThankAward ta, long fairBuddhaId) {
		String key = getKey(0) + KEY_答谢奖励等级 + "_" + fairBuddhaId;
		if (disk.get(key) != null) {
			byte level = (Byte) disk.get(key);
			return ta.getArticleCNNames()[level];
		}
		return null;
	}

	/**
	 * 获得答谢奖励物品单价
	 * @param ta
	 * @param fairBuddhaId
	 * @return
	 */
	public long getThankAwardPrice(ThankAward ta, long fairBuddhaId) {
		String key = getKey(0) + KEY_答谢奖励等级 + "_" + fairBuddhaId;
		if (disk.get(key) != null) {
			byte level = (Byte) disk.get(key);
			return ta.getPrice()[level];
		}
		return ta.getPrice()[0];
	}

	/**
	 * 获取仙尊要答谢的投票者列表
	 * @param id
	 * @return
	 */
	public List<Voter> getVoters(long id, byte career) {
		Map<Byte, List<FairyBuddhaInfo>> electorMap = getCurrentElectorMap(getKey(-1) + KEY_参选者);
		if (electorMap != null) {
			FairyBuddhaInfo elector = getElectorById(id, career, electorMap);
			if (elector != null) {
				if (elector.getVoters() != null) {
					return elector.getVoters();
				}
			}
		}
		return null;
	}

	public WorshipAward getRightWorshipAward() {
		if (worshipAwardList.size() > 0) {
			for (WorshipAward wa : worshipAwardList) {
				if (wa.getSf4a().thiserverFit()) {
					long now = System.currentTimeMillis();
					if (TimeTool.formatter.varChar19.parse(wa.getStartTime()) < now && now < TimeTool.formatter.varChar19.parse(wa.getEndTime())) {
						return wa;
					}
				}
			}
			// 如果找不到正确的时间段,就取list里面的第一条
			return worshipAwardList.get(0);
		}
		return null;
	}

	public WeekdayAward getRightWorshipAward(long fairyBuddhaId) {
		if (worshipAwardList.size() > 0) {
			byte levelKey = 0;
			String key = getKey(0) + KEY_膜拜奖励等级 + "_" + fairyBuddhaId;
			if (disk.get(key) != null) {
				levelKey = (Byte) disk.get(key);
			}
			WorshipAward wa = getRightWorshipAward();
			Map<Byte, WeekdayAward> weeddayAwardMap = wa.getLevelAward().get(levelKey);
			return getWeekdayAwardByDay(weeddayAwardMap);
		}
		return null;
	}

	/**
	 * 获取膜拜奖励
	 * @param weeddayAwardMap
	 * @return
	 */
	public WeekdayAward getWeekdayAwardByDay(Map<Byte, WeekdayAward> weeddayAwardMap) {
		if (weeddayAwardMap != null) {
			Calendar c = Calendar.getInstance();
			WeekdayAward wda = weeddayAwardMap.get((byte) c.get(c.DAY_OF_WEEK));
			if (wda != null) {
				return wda;
			}
		}
		return null;
	}

	public void loadFile() throws Exception {
		File file = new File(getFilePath());
		file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		try {
			HSSFWorkbook hssfWorkbook = null;
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			hssfWorkbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = null;

			/** 投票奖励 */
			sheet = hssfWorkbook.getSheetAt(0);
			if (sheet == null) return;
			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					String startTime = cell.getStringCellValue();
					String endTime = row.getCell(index++).getStringCellValue();
					String platform = row.getCell(index++).getStringCellValue();
					cell = row.getCell(index++);
					String openServerNames = "";
					if (cell != null) {
						openServerNames = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String limitServerNames = "";
					if (cell != null) {
						limitServerNames = cell.getStringCellValue();
					}
					String[] awardCNNames = row.getCell(index++).getStringCellValue().split(",");
					String[] awardColors = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
					String[] awardNums = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
					String[] awardBinds = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
					ActivityProp[] props = new ActivityProp[awardCNNames.length];
					if (awardCNNames.length != awardColors.length || awardCNNames.length != awardNums.length || awardCNNames.length != awardBinds.length) {
						new IllegalStateException("[exchangeActivity.xls] [sheet4] [奖励物品名字、颜色、数量、是否绑定数组长度不一致]");
					} else {
						for (int j = 0; j < awardCNNames.length; j++) {
							props[j] = new ActivityProp(awardCNNames[j], Integer.parseInt(awardColors[j]), Integer.parseInt(awardNums[j]), Boolean.parseBoolean(awardBinds[j]));
						}
					}
					VoteAward va = new VoteAward(startTime, endTime, new ServerFit4Activity(platform, openServerNames, limitServerNames), props);
					voteAwardList.add(va);
				}

			}

			/** 答谢奖励 */
			sheet = hssfWorkbook.getSheetAt(1);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					String startTime = cell.getStringCellValue();
					String endTime = row.getCell(index++).getStringCellValue();
					String platform = row.getCell(index++).getStringCellValue();
					cell = row.getCell(index++);
					String openServerNames = "";
					if (cell != null) {
						openServerNames = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String limitServerNames = "";
					if (cell != null) {
						limitServerNames = cell.getStringCellValue();
					}
					String[] awardCNNames = row.getCell(index++).getStringCellValue().split(",");
					Long[] prices = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ",", Long.class);
					long[] price = new long[prices.length];
					for (int j = 0; j < prices.length; j++) {
						price[j] = prices[j];
					}
					ThankAward ta = new ThankAward(startTime, endTime, new ServerFit4Activity(platform, openServerNames, limitServerNames), awardCNNames, price);
					thankAwardList.add(ta);
				}
			}

			/** 膜拜奖励 */
			sheet = hssfWorkbook.getSheetAt(2);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			WorshipAward wa = null;
			byte level = 0;
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell != null) {
						wa = new WorshipAward();
						String startTime = cell.getStringCellValue();
						wa.setStartTime(startTime);
						String endTime = row.getCell(index++).getStringCellValue();
						wa.setEndTime(endTime);
						String platform = row.getCell(index++).getStringCellValue();
						cell = row.getCell(index++);
						String openServerNames = "";
						if (cell != null) {
							openServerNames = cell.getStringCellValue();
						}
						cell = row.getCell(index++);
						String limitServerNames = "";
						if (cell != null) {
							limitServerNames = cell.getStringCellValue();
						}
						wa.setSf4a(new ServerFit4Activity(platform, openServerNames, limitServerNames));
						level = (byte) row.getCell(index++).getNumericCellValue();
						String awardName = row.getCell(index++).getStringCellValue();
						Map<Byte, String> awardNameMap = new HashMap<Byte, String>();
						awardNameMap.put(level, awardName);
						wa.setAwardNameMap(awardNameMap);
						String des = row.getCell(index++).getStringCellValue();
						Map<Byte, String> desMap = new HashMap<Byte, String>();
						desMap.put(level, des);
						wa.setDesMap(desMap);
						long price = (long) row.getCell(index++).getNumericCellValue();
						Map<Byte, Long> priceMap = new HashMap<Byte, Long>();
						priceMap.put(level, price);
						wa.setPriceMap(priceMap);
						byte dayOfWeek = (byte) row.getCell(index++).getNumericCellValue();
						double expMul = row.getCell(index++).getNumericCellValue();
						double silverMul = row.getCell(index++).getNumericCellValue();
						WeekdayAward wda = new WeekdayAward(dayOfWeek, expMul, silverMul, null);
						cell = row.getCell(index++);
						if (cell != null) {
							String[] awardCNNames = cell.getStringCellValue().split(",");
							String[] awardColors = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
							String[] awardNums = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
							String[] awardBinds = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
							ActivityProp[] props = new ActivityProp[awardCNNames.length];
							if (awardCNNames.length != awardColors.length || awardCNNames.length != awardNums.length || awardCNNames.length != awardBinds.length) {
								new IllegalStateException("[exchangeActivity.xls] [sheet4] [奖励物品名字、颜色、数量、是否绑定数组长度不一致]");
							} else {
								for (int j = 0; j < awardCNNames.length; j++) {
									props[j] = new ActivityProp(awardCNNames[j], Integer.parseInt(awardColors[j]), Integer.parseInt(awardNums[j]), Boolean.parseBoolean(awardBinds[j]));
								}
							}
							wda.setProps(props);
						}
						Map<Byte, WeekdayAward> weekdayAwardMap = new HashMap<Byte, WeekdayAward>();
						weekdayAwardMap.put(dayOfWeek, wda);
						Map<Byte, Map<Byte, WeekdayAward>> levelAward = new HashMap<Byte, Map<Byte, WeekdayAward>>();
						levelAward.put(level, weekdayAwardMap);
						wa.setLevelAward(levelAward);
						worshipAwardList.add(wa);

					} else {
						index = 5;
						cell = row.getCell(index++);
						if (cell != null) {
							level = (byte) cell.getNumericCellValue();
							String awardName = row.getCell(index++).getStringCellValue();
							wa.getAwardNameMap().put(level, awardName);
							String des = row.getCell(index++).getStringCellValue();
							wa.getDesMap().put(level, des);
							long price = (long) row.getCell(index++).getNumericCellValue();
							wa.getPriceMap().put(level, price);

							byte dayOfWeek = (byte) row.getCell(index++).getNumericCellValue();
							double expMul = row.getCell(index++).getNumericCellValue();
							double silverMul = row.getCell(index++).getNumericCellValue();
							WeekdayAward wda = new WeekdayAward(dayOfWeek, expMul, silverMul, null);
							cell = row.getCell(index++);
							if (cell != null) {
								String[] awardCNNames = cell.getStringCellValue().split(",");
								String[] awardColors = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
								String[] awardNums = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
								String[] awardBinds = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
								ActivityProp[] props = new ActivityProp[awardCNNames.length];
								if (awardCNNames.length != awardColors.length || awardCNNames.length != awardNums.length || awardCNNames.length != awardBinds.length) {
									new IllegalStateException("[exchangeActivity.xls] [sheet4] [奖励物品名字、颜色、数量、是否绑定数组长度不一致]");
								} else {
									for (int j = 0; j < awardCNNames.length; j++) {
										props[j] = new ActivityProp(awardCNNames[j], Integer.parseInt(awardColors[j]), Integer.parseInt(awardNums[j]), Boolean.parseBoolean(awardBinds[j]));
									}
								}
								wda.setProps(props);
							}
							Map<Byte, WeekdayAward> weekdayAwardMap = new HashMap<Byte, WeekdayAward>();
							weekdayAwardMap.put(dayOfWeek, wda);
							wa.getLevelAward().put(level, weekdayAwardMap);
						} else {
							index = 9;
							byte dayOfWeek = (byte) row.getCell(index++).getNumericCellValue();
							double expMul = row.getCell(index++).getNumericCellValue();
							double silverMul = row.getCell(index++).getNumericCellValue();
							WeekdayAward wda = new WeekdayAward(dayOfWeek, expMul, silverMul, null);
							cell = row.getCell(index++);
							if (cell != null) {
								String[] awardCNNames = cell.getStringCellValue().split(",");
								String[] awardColors = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
								String[] awardNums = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
								String[] awardBinds = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
								ActivityProp[] props = new ActivityProp[awardCNNames.length];
								if (awardCNNames.length != awardColors.length || awardCNNames.length != awardNums.length || awardCNNames.length != awardBinds.length) {
									new IllegalStateException("[exchangeActivity.xls] [sheet4] [奖励物品名字、颜色、数量、是否绑定数组长度不一致]");
								} else {
									for (int j = 0; j < awardCNNames.length; j++) {
										props[j] = new ActivityProp(awardCNNames[j], Integer.parseInt(awardColors[j]), Integer.parseInt(awardNums[j]), Boolean.parseBoolean(awardBinds[j]));
									}
								}
								wda.setProps(props);
							}
							Map<Byte, WeekdayAward> weekdayAwardMap = wa.getLevelAward().get(level);
							weekdayAwardMap.put(dayOfWeek, wda);
							wa.getLevelAward().put(level, weekdayAwardMap);
						}
					}

				}
				logger.warn("[仙尊] [膜拜奖励第" + i + "行]");
			}

			/** 答谢奖励 */
			sheet = hssfWorkbook.getSheetAt(3);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					byte career = (byte) cell.getNumericCellValue();
					int npcId = (int) row.getCell(index++).getNumericCellValue();
					int pointX = (int) row.getCell(index++).getNumericCellValue();
					int pointY = (int) row.getCell(index++).getNumericCellValue();
					String mapName = row.getCell(index++).getStringCellValue();
					String title = row.getCell(index++).getStringCellValue();
					String horseCNNmae = row.getCell(index++).getStringCellValue();
					DefaultFairyNpcData dfnd = new DefaultFairyNpcData(career, npcId, pointX, pointY, mapName, title, horseCNNmae);
					defaultNpcList.add(dfnd);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void main(String[] args) {
		FairyBuddhaManager fba = new FairyBuddhaManager();
		// try {
		// fba.loadFile();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// File file = new File("D://fairyBuddha.ddc");
		// DiskCache disk = new DefaultDiskCache(file, null, "fairybuddha", 100L * 365 * 24 * 3600 * 1000L);
		// Map<Byte, FairyBuddhaInfo> fairyBuddhaMap = new Hashtable<Byte, FairyBuddhaInfo>();
		// FairyBuddhaInfo aa = new FairyBuddhaInfo();
		// StatueForFairyBuddha aaa = new StatueForFairyBuddha();
		// aaa.setAvataSex("111111");
		// aa.setStatue(aaa);
		// fairyBuddhaMap.put((byte)1, aa);
		// disk.put("aaaaaaaaaa", (Serializable) fairyBuddhaMap);
		// System.out.println(disk.get("aaaaaaaaaa"));
		// Calendar c = Calendar.getInstance();
		// String str1 = c.get(c.YEAR) + "_" + c.get(c.DAY_OF_YEAR) + "_" + (c.get(c.HOUR_OF_DAY) + 0);
		// String key = str1 + KEY_膜拜奖励等级 + "_" + "1502000000000000001";
		// System.out.println(key);
		// System.out.println(disk.get(key));

		System.out.println(fba.getKey(0));

	}

	/**
	 * 放置仙尊雕像
	 */
	public void putStatue() {
		for (StatueForFairyBuddha statue : statueList) {
			// FairyBuddhaManager.logger.warn("[默认仙尊数据] [statue.getAvata():" + Arrays.toString(statue.getAvatas()) + "] [statue.getAvataType():" +
			// Arrays.toString(statue.getAvataType()) + "]");
			if (fairyBuddhaMap.size() > 0) {
				FairyBuddhaInfo fbi = fairyBuddhaMap.get(statue.getCareer());
				StatueForFairyBuddha sffb = null;
				if (fbi != null) {
					sffb = fairyBuddhaMap.get(statue.getCareer()).getStatue();
					try {
						if (sffb != null) {
							sffb.putNpcLike(statue.getCategoryId(), fbi);
							logger.warn("[放置仙尊雕像] [" + fbi.getLogString() + "] [" + sffb.toString() + "]");
						}
					} catch (Exception e) {
						logger.warn("[放置仙尊雕像] [异常] [playerId:" + fbi.getId() + "]", e);
					}
				} else {
					putNpcStat(statue, statue.getDefaultNpc());
					setDefaultAvata(statue);
				}
			} else {
				putNpcStat(statue, statue.getDefaultNpc());
				setDefaultAvata(statue);
			}
		}

	}

	public void setDefaultAvata(StatueForFairyBuddha statue) {
		Game game = GameManager.getInstance().getGameByName("miemoshenyu", 0);
		for (LivingObject ll : game.getLivingObjects()) {
			if (ll instanceof NPC) {
				if (((NPC) ll).getId() == statue.getDefaultNpc().getId()) {
					((NPC) ll).setAvata(statue.getAvatas());
					((NPC) ll).setAvataType(statue.getAvataType());
					logger.warn("[无仙尊当选放置仙尊雕像] [statue:" + statue.getNpcName() + "]");
				}
			}
		}
	}

	public void putDefaultNPC() {
		for (StatueForFairyBuddha statue : statueList) {
			NPC newNpc = MemoryNPCManager.getNPCManager().createNPC(statue.getCategoryId());
			if (newNpc == null) {
				throw new IllegalStateException("雕像配置错误,NPC不存在:" + statue.getCategoryId());
			}
			putNpcStat(statue, newNpc);
		}
	}

	public void putNpcStat(StatueForFairyBuddha statue, NPC newNpc) {
		Game game = GameManager.getInstance().getGameByName(statue.getMapName(), 0);
		if (game != null) {
			FairyBuddhaManager.logger.warn("[放置默认仙尊] [" + statue.toString() + "] [" + newNpc.getWindowId() + "]");

			newNpc.setAvata(statue.getAvatas());
			newNpc.setAvataRace(statue.getAvataRace());
			newNpc.setAvataSex(statue.getAvataSex());
			newNpc.setAvataType(statue.getAvataType());

			// newNpc.setLevel(player.getLevel());

			newNpc.setX(statue.getPoint().getX());
			newNpc.setY(statue.getPoint().getY());
			ResourceManager.getInstance().getAvata(newNpc);
			// String[] avatas = statue.getAvatas();

			// StringBuffer sbf = new StringBuffer();
			// for (int ik = (avatas.length <= 2 ? 0 : 1); ik < avatas.length; ik++) {
			// String avata = avatas[ik];
			// String value = avata.split("\\/")[2].split("\\_")[0];
			// if (!"gx".equals(value)) {
			// sbf.append(avata.split("\\/")[2].split("\\_")[0]).append("；");
			// }
			// }
			// newNpc.setPutOnEquipmentAvata(sbf.toString());
			// ResourceManager.getInstance().getAvata(newNpc);
			newNpc.setObjectScale((short) 1200);
			if (statue.getParticleName() != null && !"".equals(statue.getParticleName())) {
				newNpc.setParticleName(statue.getParticleName());
				newNpc.setParticleX((short) 0);
				newNpc.setParticleY((short) 0);
			}
			ResourceManager rm = ResourceManager.getInstance();
			rm.getAvata(newNpc);
			newNpc.setTitle("<f color='" + ArticleManager.COLOR_ORANGE_2 + "'>" + statue.getTitle() + "</f>");
			newNpc.setNameColor(statue.getNameColor());
			newNpc.setName(statue.getNpcName());
			statue.setDefaultNpc(newNpc);
			WindowManager.getInstance().getWindowMap().get(newNpc.getWindowId()).setDescriptionInUUB(Translate.仙尊窗口默认宣言);
			game.addSprite(newNpc);
			FairyBuddhaManager.logger.warn("[放置默认仙尊] [npcName:" + statue.getNpcName() + "]");
		}
	}

	public void initStatue() throws Exception {
		MemoryNPCManager ma = (MemoryNPCManager) MemoryNPCManager.getNPCManager();
		for (DefaultFairyNpcData d : defaultNpcList) {
			NPC npc = ma.createNPC(d.getNpcId());

			if (npc == null) {
				throw new Exception("仙尊默认npc不存在");
			} else {
				FairyBuddhaManager.logger.warn("[设置默认仙尊数据] [cat :" + d.getNpcId() + "]");
				statueList.add(new StatueForFairyBuddha(new Point(d.getPointX(), d.getPointY()), d.getMapName(), npc.getName(), d.getNpcId(), npc.getTitle(), ArticleManager.COLOR_WHITE, npc.getParticleName(), d.getCareer(), npc.getAvata(), npc.getAvataRace(), npc.getAvataSex(), npc.getAvataType()));
				// FairyBuddhaManager.logger.warn("[默认仙尊数据] [npc.getAvata():" + Arrays.toString(npc.getAvata()) + "] [npc.getAvataType():" + Arrays.toString(npc.getAvataType()) +
				// "]");

			}
		}
	}

	public void readDataFromDisk2Memory() {
		if (disk.get(getKey(0) + KEY_仙尊) != null) {
			FairyBuddhaManager.logger.warn("[读取仙尊到内存] [key:" + (getKey(0) + KEY_仙尊) + "]");
			fairyBuddhaMap = (Map<Byte, FairyBuddhaInfo>) disk.get(getKey(0) + KEY_仙尊);
			FairyBuddhaManager.logger.warn("[读取仙尊到内存] [fairyBuddhaMap长度:" + fairyBuddhaMap.size() + "]");
			for (byte career : fairyBuddhaMap.keySet()) {
				FairyBuddhaInfo fbi = fairyBuddhaMap.get(career);
				if (fbi != null) {
					Vector<Voter> worshippers = fbi.getWorshippers();
					if (worshippers != null && worshippers.size() > 0) {
						for (Voter worshiper : worshippers) {
							worshipIdList.add(worshiper.getId());
						}
					}
				}
			}
		}
		if (disk.get(getKey(0) + KEY_参选者) != null) {
			Map<Byte, List<FairyBuddhaInfo>> electorMap = (Map<Byte, List<FairyBuddhaInfo>>) disk.get(getKey(0) + KEY_参选者);
			for (byte career : electorMap.keySet()) {
				List<FairyBuddhaInfo> electors = electorMap.get(career);
				if (electors != null && electors.size() > 0) {
					for (FairyBuddhaInfo fbi : electors) {
						Vector<Voter> voters = fbi.getVoters();
						if (voters != null && voters.size() > 0) {
							for (Voter voter : voters) {
								voterIdList.add(voter.getId());
							}
						}
					}
				}
			}
		}
		// Calendar c = Calendar.getInstance();
		// if (c.get(c.DAY_OF_WEEK) == 1) {
		// readElectors(-1);
		// } else {
		// readElectors(0);
		// }
	}

	/**
	 * 从ddc读取参选者到内存
	 * @param i
	 *            =0:本周;i=1:下周;i=-1:上周
	 */
	// public void readElectors(int i) {
	// if (disk.get(getKey(i) + KEY_参选者) != null) {
	// FairyBuddhaManager.logger.warn("[读取参选者到内存] [key:" + (getKey(i) + KEY_参选者) + "]");
	// electorMap = (Map<Byte, List<FairyBuddhaInfo>>) disk.get(getKey(i) + KEY_参选者);
	// for (byte career : electorMap.keySet()) {
	// List<FairyBuddhaInfo> electors = electorMap.get(career);
	// if (electors != null && electors.size() > 0) {
	// FairyBuddhaManager.logger.warn("[读取参选者到内存] [职业:" + career + "] [electors长度 :" + electors.size() + "]");
	// for (FairyBuddhaInfo fbi : electors) {
	// Vector<Voter> voters = fbi.getVoters();
	// if (voters != null && voters.size() > 0) {
	// for (Voter voter : voters) {
	// voterIdList.add(voter.getId());
	// }
	// }
	// }
	// }
	// }
	// }
	// }

	/**
	 * 产生新仙尊
	 */
	public void newFairyBuddha() {
		logger.warn("[仙尊] [进入方法:更新仙尊列表]");
		try {
			// 旧仙尊卸任
			if (fairyBuddhaMap.size() > 0) {
				for (Byte career : fairyBuddhaMap.keySet()) {
					DefaultFairyNpcData dfnd = getDefaultNpcStatuByCareer(career);
					if (fairyBuddhaMap.get(career) != null) {
						FairyBuddhaInfo fbi = fairyBuddhaMap.get(career);
						Player p = PlayerManager.getInstance().getPlayer(fbi.getId());
						if (p != null) {
							PlayerTitlesManager.getInstance().removeTitle(p, dfnd.getTitle());
						}
						logger.warn("[" + p.getLogString() + "] [仙尊] [卸任]");
					}
				}
			}

			if (null != disk.get(KEY_历届仙尊)) {
				logger.warn("[历届仙尊] [准备卸任]");
				ArrayList<FairyBuddhaInfo> allFairybuddhas = (ArrayList<FairyBuddhaInfo>) disk.get(KEY_历届仙尊);
				for (FairyBuddhaInfo fbi : allFairybuddhas) {
					DefaultFairyNpcData dfnd = getDefaultNpcStatuByCareer(fbi.getCareer());
					Player p = PlayerManager.getInstance().getPlayer(fbi.getId());
					if (p != null) {
						boolean succ = PlayerTitlesManager.getInstance().removeTitle(p, dfnd.getTitle());
						if (succ) {
							if (logger.isWarnEnabled()) {
								logger.warn("[" + p.getLogString() + "] [历届仙尊] [卸任]");
							}
						} else {
							if (logger.isWarnEnabled()) {
								logger.warn("[" + p.getLogString() + "] [历届仙尊] [已经卸任过了]");
							}
						}
					}
				}
			}

		} catch (Exception e) {
			logger.warn("[仙尊] [卸任混元至圣异常]" + e);
			e.printStackTrace();
		}
		try {
			fairyBuddhaMap = new Hashtable<Byte, FairyBuddhaInfo>();
			disk.put(getKey(0) + KEY_已选过仙尊, 0);// 标记下本期选过仙尊了
			resetWorshipOption();// 重置膜拜按钮
			Map<Byte, List<FairyBuddhaInfo>> electorMap = getCurrentElectorMap(getKey(-1) + KEY_参选者);
			if (electorMap.size() > 0) {
				for (Byte career : electorMap.keySet()) {
					List<FairyBuddhaInfo> electors = electorMap.get(career);
					DefaultFairyNpcData dfnd = getDefaultNpcStatuByCareer(career);

					// 新仙尊产生
					if (dfnd != null) {
						if (electors != null && electors.size() > 0) {
							fairyBuddhaMap.put(career, electors.get(0));

							// KEY_历届仙尊,保存所有当选过仙尊的人,当超过30个的时候,保留30个,清除最早当选的那些.
							if (disk.get(KEY_历届仙尊) == null) {
								disk.put(KEY_历届仙尊, new ArrayList<FairyBuddhaInfo>());
							}
							ArrayList<FairyBuddhaInfo> allFairybuddhas = (ArrayList<FairyBuddhaInfo>) disk.get(KEY_历届仙尊);
							allFairybuddhas.add(electors.get(0));
							if (allFairybuddhas.size() > 30) {
								for (int i = 0; i < allFairybuddhas.size() - 30; i++) {
									allFairybuddhas.remove(allFairybuddhas.get(0));
								}
							}
							disk.put(KEY_历届仙尊, allFairybuddhas);
							String key = getKey(0) + KEY_膜拜奖励等级 + "_" + electors.get(0).getId();
							disk.put(key, (byte) 0);
							setWorshipOption(career, (byte) 0);
							Player p = PlayerManager.getInstance().getPlayer(electors.get(0).getId());
							if (p != null) {
								Article a = ArticleManager.getInstance().getArticleByCNname(dfnd.getHorseCNName());
								if (a != null) {
									try {
										ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.仙尊奖励, p, a.getColorType(), 3, true);
										if (ae != null) {
											MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, Translate.获得仙尊坐骑标题, Translate.获得仙尊坐骑内容, 0, 0, 0, "当选仙尊获得坐骑");
											if (logger.isWarnEnabled()) {
												logger.warn("[" + p.getLogString() + "] [仙尊] [发坐骑成功]");
											}
										}
									} catch (Throwable e) {
										if (logger.isErrorEnabled()) {
											logger.error("[" + p.getLogString() + "] [仙尊] [发坐骑异常]" + e);
										}
										e.printStackTrace();
									}

								} else {
									if (logger.isErrorEnabled()) {
										logger.error(p.getLogString() + "[仙尊] [发坐骑异常] [物品不存在] [" + dfnd.getHorseCNName() + "]");
									}
								}
								boolean bln = PlayerTitlesManager.getInstance().addTitle(p, dfnd.getTitle(), true);
								if (bln) {
									if (logger.isWarnEnabled()) {
										logger.warn("[" + p.getLogString() + "] [仙尊] [加称号成功]");
									}
								} else {
									if (logger.isWarnEnabled()) {
										logger.warn("[" + p.getLogString() + "] [仙尊] [加称号失败]");
									}
								}
								try {
									String msg = Translate.translateString(Translate.仙尊公告, new String[][] { { Translate.STRING_1, p.getName() }, { Translate.STRING_2, dfnd.getTitle() } });
									ChatMessageService.getInstance().sendMessageToSystem(msg);
									ChatMessage notice = new ChatMessage();
									notice.setMessageText(Translate.translateString(Translate.仙尊公告, new String[][] { { Translate.STRING_1, p.getName() }, { Translate.STRING_2, dfnd.getTitle() } }));
									ChatMessageService.getInstance().sendHintMessageToSystem(notice);
									if (logger.isWarnEnabled()) {
										logger.warn("[" + p.getLogString() + "] [仙尊] [发送世界公告成功]" + msg);
									}
								} catch (Exception e) {
									logger.warn("[" + p.getLogString() + "] [仙尊] [发送世界公告异常]" + e);
									e.printStackTrace();
								}
							}
						} else {
							setWorshipOption(career, (byte) -1);
						}
					}
				}
			}
			disk.put(getKey(0) + KEY_仙尊, (Serializable) fairyBuddhaMap);
			logger.warn("[仙尊] [更新混元至圣列表]");
			// openVote = false;
		} catch (Exception e) {
			logger.warn("[仙尊] [更新混元至圣列表异常]" + e);
			e.printStackTrace();
		}

	}

	/**
	 * 根据职业获取对应的默认仙尊数据
	 * @param career
	 * @return
	 */
	public DefaultFairyNpcData getDefaultNpcStatuByCareer(byte career) {
		for (DefaultFairyNpcData d : defaultNpcList) {
			if (d.getCareer() == career) {
				return d;
			}
		}
		return null;
	}

	/**
	 * 清空膜拜记录
	 */
	public void clearWorship() {
		worshipIdList.clear();
		if (fairyBuddhaMap.size() > 0) {
			for (Byte career : fairyBuddhaMap.keySet()) {
				FairyBuddhaInfo fbi = fairyBuddhaMap.get(career);
				if (fbi != null) {
					fbi.setWorshippers(new Vector<Voter>());
				}
			}
		}
		disk.put(getKey(0) + KEY_仙尊, (Serializable) fairyBuddhaMap);
		logger.warn("[仙尊] [清空膜拜记录]");
	}

	/**
	 * 
	 * @param player
	 * @param career
	 * @param cycle
	 *            0表示当期,-1表示上一期
	 */
	public void send_FAIRY_SHOW_ELECTORS_RES(Player player, byte career, int cycle) {
		String key = getKey(cycle) + KEY_参选者;
		if (logger.isWarnEnabled()) {
			logger.warn("[" + player.getLogString() + "] [仙尊] [查看投票榜] [key:" + key + "]");
		}
		Map<Byte, Vector<FairyBuddhaInfo>> electorMapTemp = (Map<Byte, Vector<FairyBuddhaInfo>>) disk.get(key);
		if (electorMapTemp != null) {
			List<FairyBuddhaInfo> fbiList = electorMapTemp.get(career);
			if (fbiList == null) {
				fbiList = new Vector<FairyBuddhaInfo>();
			}
			if (fbiList.size() > 0) {
				if (cycle == 0) {
					boolean[] showButton = new boolean[4];
					// 本期查看者身份:0-普通,1-竞选者,2-仙尊
					int role = getPlayerRole(player, career, electorMapTemp, cycle);
					if (logger.isWarnEnabled()) {
						logger.warn("[" + player.getLogString() + "] [仙尊] [查看本期投票榜] [角色:" + role + "]");
					}
					switch (role) {
					case 0:
						showButton = new boolean[] { true, true, false, false };
						break;
					case 1:
						showButton = new boolean[] { true, true, true, true };
						break;
					case 2:
						showButton = new boolean[] { true, true, false, false };
						break;
					case 3:
						showButton = new boolean[] { true, true, true, true };
						break;

					default:
						break;
					}
					if (logger.isWarnEnabled()) {
						logger.warn("[" + player.getLogString() + "] [仙尊] [查看本期投票榜] [按钮:" + Arrays.toString(showButton) + "]");
					}
					FAIRY_SHOW_ELECTORS_RES res = new FAIRY_SHOW_ELECTORS_RES(GameMessageFactory.nextSequnceNum(), fbiList.toArray(new FairyBuddhaInfo[0]), showButton, cycle, career);
					player.addMessageToRightBag(res);
				} else if (cycle == -1) {
					boolean[] showButton = new boolean[2];
					// 查看者身份:0-普通,1-竞选者,2-仙尊
					int role = getPlayerRole(player, career, electorMapTemp, cycle);
					if (logger.isWarnEnabled()) {
						logger.warn("[" + player.getLogString() + "] [仙尊] [查看上期投票榜] [角色:" + role + "]");
					}
					switch (role) {
					case 0:
						showButton = new boolean[] { false, false };
						break;
					case 1:
						showButton = new boolean[] { false, true };
						break;
					case 2:
						showButton = new boolean[] { true, true };
						break;

					default:
						break;
					}
					if (logger.isWarnEnabled()) {
						logger.warn("[" + player.getLogString() + "] [仙尊] [查看上期投票榜] [按钮:" + Arrays.toString(showButton) + "]");
					}
					FAIRY_SHOW_ELECTORS_RES res = new FAIRY_SHOW_ELECTORS_RES(GameMessageFactory.nextSequnceNum(), fbiList.toArray(new FairyBuddhaInfo[0]), showButton, cycle, career);
					player.addMessageToRightBag(res);
				}
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[" + player.getLogString() + "] [仙尊] [查看投票榜为空] [职业:" + career + "]");
				}
				if (cycle == 0) {
					player.sendError(Translate.无人参选);
				} else if (cycle == -1) {
					player.sendError(Translate.上期无人参选);
				}
			}
		} else {
			if (cycle == 0) {
				player.sendError(Translate.无人参选);
			} else if (cycle == -1) {
				player.sendError(Translate.上期无人参选);
			}
		}
	}

	/**
	 * @param fbi
	 *            设置膜拜奖励的仙尊
	 * @param level
	 *            膜拜奖励等级
	 */
	public void setWorshipOption(byte career, byte level) {
		String awardName = "";
		for (StatueForFairyBuddha sffb : statueList) {
			if (sffb.getCareer() == career) {
				logger.warn("[仙尊] [设置膜拜按钮] --------------1");
				MenuWindow mw = WindowManager.getInstance().getWindowById(sffb.getDefaultNpc().getWindowId());
				if (null != mw) {
					logger.warn("[仙尊] [设置膜拜按钮] --------------2");
					Option[] options = mw.getOptions();
					for (Option o : options) {
						if (o.getText().matches(".*>" + Translate.膜拜 + ".*")) {
							logger.warn("[仙尊] [设置膜拜按钮]  [原按钮:" + o.getText() + "]--------------3");
							if (logger.isWarnEnabled()) {
								logger.warn("[仙尊] [设置膜拜按钮] [职业:" + career + "] level:" + (level + 1));
							}
							WorshipAward wa = getRightWorshipAward();
							if (wa != null && level >= 0) {
								Map<Byte, String> awardNameMap = wa.getAwardNameMap();
								if (awardNameMap != null) {
									awardName = awardNameMap.get(level);
								}
								o.setText("<f size='28' color='#ffff00'  textUnderLine='true' onclick='true' enaleBgcolor='false'>" + Translate.膜拜 + "(" + awardName + ")</f>");
							} else {
								o.setText("<f size='28' color='#ffff00'  textUnderLine='true' onclick='true' enaleBgcolor='false'>" + Translate.膜拜 + "</f>");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 仙尊初始化设置膜拜按钮
	 */
	public void initWorshipOption() {
		try {
			if (logger.isWarnEnabled()) {
				FairyBuddhaManager.logger.warn("[仙尊] [初始化设置膜拜按钮]");
			}
			for (byte key : fairyBuddhaMap.keySet()) {
				if (fairyBuddhaMap.get(key) != null) {
					FairyBuddhaInfo fbi = fairyBuddhaMap.get(key);
					String worshipLevelKey = getKey(0) + KEY_膜拜奖励等级 + "_" + fbi.getId();
					FairyBuddhaManager.logger.warn("[仙尊] [膜拜奖励等级key:" + worshipLevelKey + "]");
					if (disk.get(worshipLevelKey) != null) {
						byte worshipLevel = (Byte) disk.get(worshipLevelKey);
						setWorshipOption(fbi.getCareer(), worshipLevel);
					}
				}
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("[仙尊] [初始化设置膜拜按钮异常]" + e);
			}
			e.printStackTrace();
		}
	}

	public void resetWorshipOption() {
		if (logger.isWarnEnabled()) {
			logger.warn("[仙尊] [重置膜拜按钮]");
		}
		for (StatueForFairyBuddha sffb : statueList) {
			MenuWindow mw = WindowManager.getInstance().getWindowById(sffb.getDefaultNpc().getWindowId());
			if (null != mw) {
				logger.warn("[仙尊] [重置膜拜按钮]----1[npcId:" + sffb.getCategoryId() + "] [npcName:" + sffb.getNpcName() + "]");
				Option[] options = mw.getOptions();
				for (Option o : options) {
					logger.warn("[仙尊] [重置膜拜按钮]----2[npcId:" + sffb.getCategoryId() + "] [o.getText:" + o.getText() + "]");
					if (o.getText().matches(".*>" + Translate.膜拜 + ".*")) {
						logger.warn("[仙尊] [重置膜拜按钮]----3");
						o.setText("<f size='28' color='#ffff00'  textUnderLine='true' onclick='true' enaleBgcolor='false'>" + Translate.膜拜 + "</f>");
					}
				}
			}
		}
	}

	/**
	 * 玩家改名引起的操作
	 * @param player
	 * @param oldName
	 */
	public void notifyPlayerChanageName(Player player, String oldName) {
		for (StatueForFairyBuddha sffb : statueList) {
			if (sffb.getNpcName().equals(oldName)) {
				sffb.setNpcName(player.getName());
				GamePlayerManager.logger.warn(player.getLogString() + "[角色改名] [更新仙尊相关] [雕像信息] [oldName:" + oldName + "]");
			}
			if (sffb.getDefaultNpc() != null) {
				String npcName = sffb.getDefaultNpc().getName();
				String[] name = npcName.split("●");
				if (name.length > 1 && name[0].equals(oldName)) {
					sffb.getDefaultNpc().setName(player.getName() + "●" + name[1]);
					GamePlayerManager.logger.warn(player.getLogString() + "[角色改名] [更新仙尊相关] [雕像默认npc] [oldName:" + oldName + "]");
				}
			}
		}

		// 本周仙尊
		changeFairyBuddhaName(fairyBuddhaMap, player, oldName, 0);
		disk.put(getKey(0) + KEY_仙尊, (Serializable) fairyBuddhaMap);
		// 上周仙尊
		Map<Byte, FairyBuddhaInfo> fairyBuddhaMapLastWeek = (Map<Byte, FairyBuddhaInfo>) disk.get(getKey(-1) + KEY_仙尊);
		if (fairyBuddhaMapLastWeek != null && fairyBuddhaMapLastWeek.size() > 0) {
			changeFairyBuddhaName(fairyBuddhaMapLastWeek, player, oldName, -1);
			disk.put(getKey(-1) + KEY_仙尊, (Serializable) fairyBuddhaMapLastWeek);
		}

		// 本周竞选者
		Map<Byte, List<FairyBuddhaInfo>> electorMap = (Map<Byte, List<FairyBuddhaInfo>>) disk.get(getKey(0) + KEY_参选者);
		if (electorMap != null && electorMap.size() > 0) {
			changeElectorName(electorMap, player, oldName);
			disk.put(getKey(0) + KEY_参选者, (Serializable) electorMap);
		}
		// 上周竞选者
		Map<Byte, List<FairyBuddhaInfo>> electorMapLastWeek = (Map<Byte, List<FairyBuddhaInfo>>) disk.get(getKey(-1) + KEY_参选者);
		if (electorMapLastWeek != null && electorMapLastWeek.size() > 0) {
			changeElectorName(electorMapLastWeek, player, oldName);
			disk.put(getKey(-1) + KEY_参选者, (Serializable) electorMapLastWeek);
		}

		// 历届仙尊
		List<FairyBuddhaInfo> allFairybuddhas = (ArrayList<FairyBuddhaInfo>) disk.get(KEY_历届仙尊);
		if (allFairybuddhas != null && allFairybuddhas.size() > 0) {
			for (FairyBuddhaInfo fbi : allFairybuddhas) {
				changeName(fbi, player, oldName);
			}
		}
		disk.put(KEY_历届仙尊, (Serializable) allFairybuddhas);

	}

	/**
	 * 改名涉及的仙尊修改
	 * @param fairyBuddhaMap
	 * @param player
	 * @param oldName
	 * @param nowCycle
	 *            0:本周期,1:上周期
	 */
	public void changeFairyBuddhaName(Map<Byte, FairyBuddhaInfo> fairyBuddhaMap, Player player, String oldName, int nowCycle) {
		boolean changeName = false;
		if (fairyBuddhaMap != null) {
			for (Byte type : fairyBuddhaMap.keySet()) {
				FairyBuddhaInfo fbi = fairyBuddhaMap.get(type);
				changeName(fbi, player, oldName);
				GamePlayerManager.logger.warn(player.getLogString() + "[角色改名] [更新仙尊相关] [" + (nowCycle == 0 ? "本周期" : "上周期") + "] [oldName:" + oldName + "]");
			}
		}

		// 如果仙尊改名了,那么摆放的仙尊npc也要更新下
		if (changeName && nowCycle == 0) {
			putStatue();
		}
	}

	public void changeElectorName(Map<Byte, List<FairyBuddhaInfo>> electorMap, Player player, String oldName) {
		if (electorMap != null) {
			for (Byte type : electorMap.keySet()) {
				List<FairyBuddhaInfo> fbiList = electorMap.get(type);
				for (FairyBuddhaInfo fbi : fbiList) {
					changeName(fbi, player, oldName);
					GamePlayerManager.logger.warn(player.getLogString() + "[角色改名] [更新仙尊相关] [oldName:" + oldName + "]");
				}
			}
		}
	}

	public void changeName(FairyBuddhaInfo fbi, Player player, String oldName) {
		if (fbi != null) {
			if (fbi.getName().equals(oldName)) {
				fbi.setName(player.getName());
				GamePlayerManager.logger.warn(player.getLogString() + "[角色改名] [更新仙尊相关] [仙尊] [oldName:" + oldName + "]");
			}
			Vector<Voter> voters = fbi.getVoters();
			if (voters != null) {
				for (Voter v : voters) {
					if (v.getName().equals(oldName)) {
						v.setName(player.getName());
						GamePlayerManager.logger.warn(player.getLogString() + "[角色改名] [更新仙尊相关] [投票者] [oldName:" + oldName + "]");
					}
				}
			}
			Vector<Voter> worshippers = fbi.getWorshippers();
			if (worshippers != null) {
				for (Voter w : worshippers) {
					if (w.getName().equals(oldName)) {
						w.setName(player.getName());
						GamePlayerManager.logger.warn(player.getLogString() + "[角色改名] [更新仙尊相关] [膜拜者] [oldName:" + oldName + "]");
					}
				}
			}
			if (fbi.getStatue() != null) {
				StatueForFairyBuddha sffb = fbi.getStatue();
				if (sffb.getNpcName().equals(oldName)) {
					sffb.setNpcName(player.getName());
					GamePlayerManager.logger.warn(player.getLogString() + "[角色改名] [更新仙尊相关] [雕像信息] [oldName:" + oldName + "]");
				}
			}
		}
	}

	public void init() throws Exception {
		
		long start = System.currentTimeMillis();
		instance = this;
//		loadFile();
		File file = new File(diskFile);
		disk = new DefaultDiskCache(file, null, "fairybuddha", 100L * 365 * 24 * 3600 * 1000L);
		readDataFromDisk2Memory();
		initStatue();
		putDefaultNPC();
		if (disk.get(getKey(0) + KEY_已选过仙尊) == null) {
			newFairyBuddha();
			if (logger.isWarnEnabled()) {
				logger.warn("[仙尊] [服务器启动选仙尊] [key:" + getKey(0) + KEY_已选过仙尊 + "]");
			}
		}
		putStatue();
		doReg();
		initWorshipOption();
		ServiceStartRecord.startLog(this);
	}

	@Override
	public void doReg() {
		EventRouter.register(Event.SERVER_DAY_CHANGE, this);
		EventRouter.register(Event.FAIRY_CHALLENGE_RESULT, this);
//		EventRouter.register(Event.SERVER_5MINU_CHANGE, this);
//		EventRouter.register(Event.SERVER_MINU_CHANGE, FairyBuddhaManager.instance);
	}

	@Override
	public void proc(Event evt) {
		Object[] obj = null;
		EventWithObjParam evtWithObj = null;
		switch (evt.id) {
		// 测试周期1小时
		// case Event.SERVER_5MINU_CHANGE:
		// clearWorship(); // 清空膜拜记录
		// Calendar c = Calendar.getInstance();
		// if (c.get(c.MINUTE) == 0) {
		// newFairyBuddha(); // 产生新仙尊
		// putStatue(); // 摆放新仙尊
		// voterIdList.clear(); // 清空投票限制
		// if (logger.isWarnEnabled()) {
		// logger.warn("[仙尊] [清空投票限制]");
		// }
		// }
		// break;
		// case Event.SERVER_MINU_CHANGE:
		// clearWorship(); // 清空膜拜记录
		// Calendar c = Calendar.getInstance();
		// if (c.get(c.MINUTE) % 10 == 0) {
		// newFairyBuddha(); // 产生新仙尊
		// putStatue(); // 摆放新仙尊
		// voterIdList.clear(); // 清空投票限制
		// if (logger.isWarnEnabled()) {
		// logger.warn("[仙尊] [清空投票限制]");
		// }
		// }
		// break;

		// 正式
		case Event.SERVER_DAY_CHANGE:
			clearWorship(); // 清空膜拜记录
			Calendar c = Calendar.getInstance();
			if (c.get(c.DAY_OF_WEEK) == 1) {
				String key=getKey(-1);
				if(key.equals("2016_1")){
					Map<Byte, Vector<FairyBuddhaInfo>> electorMapTemp = (Map<Byte, Vector<FairyBuddhaInfo>>) disk.get("2015_1" + KEY_参选者);
					disk.put(key + KEY_参选者, (Serializable) electorMapTemp);
				}
				newFairyBuddha(); // 产生新仙尊
				putStatue(); // 摆放新仙尊
				voterIdList.clear(); // 清空投票限制
				if (logger.isWarnEnabled()) {
					logger.warn("[仙尊] [清空投票限制]");
				}
			}
			break;

		case Event.FAIRY_CHALLENGE_RESULT:
			evtWithObj = (EventWithObjParam) evt;
			obj = (Object[]) evtWithObj.param;
			Player player = (Player) obj[0];
			byte result = (Byte) obj[1];
			int soulType = (Integer) obj[2];
			if (result > 0) {
				joinElector(player, soulType);
				// player.guideDeclare = true;
			}
			break;
		}

	}
}
