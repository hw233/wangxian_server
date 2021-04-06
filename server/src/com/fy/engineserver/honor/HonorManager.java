package com.fy.engineserver.honor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.XmlUtil;

public class HonorManager {
	public static final int HONOR_GANG = 0;// [0~100]

	public static final int HONOR_MARRIAGE = 101;// [0~100]

	public static final int HONOR_ID = 1000;

//	public static Logger logger = Logger.getLogger("HonorManager");
public	static Logger logger = LoggerFactory.getLogger("HonorManager");

	File templetesFile;

	File uniqueHonorRecordFile;

	ArrayList<Honor> m_honorList = new ArrayList<Honor>();

	static HonorManager self;

	HashMap<String, HonorTemplete> templetes;

	public static int LOSE_REASON_BE_REPLACED = 0;

	public static int LOSE_REASON_EXPIRATION = 1;

	public static int LOSE_REASON_NOT_MEET_STANDARDS = 2;

	private final String[] LOSE_REASON = { Translate.text_4230,
			Translate.text_4231,
			Translate.text_4232 };

	public static final boolean IS_CLOSED = false;

	DefaultDiskCache ddc = null;

	/**
	 * 称号
	 */
	HashMap<Long, HashMap<String, Honor>> honorsMap = new HashMap<Long, HashMap<String, Honor>>();

	public static HonorManager getInstance() {
		return self;
	}

	public void init() throws Exception {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		this.templetes = new HashMap<String, HonorTemplete>();
		this.loadHonorTempletesData(this.templetesFile);
		this.ddc = new DefaultDiskCache(this.uniqueHonorRecordFile, null,
				"UniqueHonorRecord", 100L * 365 * 24 * 3600 * 1000L);
		HonorManager.self = this;
		System.out.println("[系统初始化] [称号管理器] [初始化完成] [" + getClass().getName()
				+ "] [耗时：" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "毫秒]");
	}

	public HashMap<String, Honor> getHonors(Player player) {
		return honorsMap.get(player.getId());
	}

	public Honor getHonor(Player player, String name) {
		HashMap<String, Honor> honors = this.getHonors(player);
		if (honors != null) {
			return honors.get(name);
		}
		return null;
	}

	/**
	 * 得到一个称号
	 * 
	 * @param h
	 */
	public void gainHonor(Player player, Honor h) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		HashMap<String, Honor> honors = this.getHonors(player);
		if (honors == null) {
			synchronized (player) {
				honors = this.getHonors(player);
				if (honors == null) {
					honors = new HashMap<String, Honor>();
					this.honorsMap.put(player.getId(), honors);
				}
			}
		}
		honors.put(h.getName(), h);
		if (logger.isInfoEnabled()) {
//logger.info("[称号] [得到称号] [" + h.getName() + "] [玩家："
//+ player.getName() + "] [玩家ID：" + player.getId()
//+ "] [玩家帐号：" + player.getUsername() + "]", start);
if(logger.isInfoEnabled())
	logger.info("[称号] [得到称号] [{}] [玩家：{}] [玩家ID：{}] [玩家帐号：{}] [{}ms]", new Object[]{h.getName(),player.getName(),player.getId(),player.getUsername(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
		}
	}

	/**
	 * 失去一个称号
	 * 
	 * @param h
	 * @param reason
	 */
	public void loseHonor(Player player, Honor h, int reason) {
		if (HonorManager.IS_CLOSED) {
			return;
		}
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		HashMap<String, Honor> honors = this.getHonors(player);
		if (honors == null) {
			return;
		}
		if (honors.containsKey(h.getName())) {
			honors.remove(h.getName());
			if (logger.isInfoEnabled()) {
//logger.info("[称号] [失去称号] [" + h.getName() + "][原因："
//+ HonorManager.getInstance().getLoseReason(reason)
//+ "] [玩家：" + player.getName() + "] [玩家ID："
//+ player.getId() + "] [玩家帐号：" + player.getUsername()
//+ "]", start);
if(logger.isInfoEnabled())
	logger.info("[称号] [失去称号] [{}][原因：{}] [玩家：{}] [玩家ID：{}] [玩家帐号：{}] [{}ms]", new Object[]{h.getName(),HonorManager.getInstance().getLoseReason(reason),player.getName(),player.getId(),player.getUsername(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			}
		}
	}

	/**
	 * 得到玩家的一个称号
	 * 
	 * @param player
	 * @param honorid
	 * @return
	 */
	public Honor getHonor(Player player, int honorid) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		HashMap<String, Honor> honors = this.getHonors(player);
		if (honors == null) {
			return null;
		}
		Honor hs[] = honors.values().toArray(new Honor[0]);
		for (Honor h : hs) {
			if (h.getId() == honorid) {
				return h;
			}
		}
		return null;
	}

	/**
	 * 得到UUB格式的称号
	 * 
	 * @param player
	 * @param honorid
	 * @return
	 */
	public String getHonorUUB(Player player, int honorid) {
		Honor h = getHonor(player, honorid);
		if (h != null) {
			return h.getDesp();
		}
		return null;
	}

	/**
	 * 称号的操作
	 * 
	 * @param player
	 * @param honorId
	 * @param operation
	 */
	public void doOperation(Player player, int honorId, byte operation) {
		try {
			Honor h = getHonor(player, honorId);
			if (h != null) {
				if (operation == 0) {
					h.doEquip(player);
				} else if (operation == 1) {
						h.doUnEquip(player);
				}
			} 
		} catch (Exception e) {
			logger.error("[称号] [操作称号失败][错误：" + e + "]", e);
		}
	}

	/**
	 * 加载称号配置
	 * @param file
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	private void loadHonorTempletesData(File file)
			throws FileNotFoundException, Exception {
		Document doc = XmlUtil.load(new FileInputStream(file));
		Element root = doc.getDocumentElement();
		Element[] templetes = XmlUtil.getChildrenByName(root, "templete");
		for (Element e : templetes) {
			HonorTemplete ht = new HonorTemplete();
			ht.setName(XmlUtil.getAttributeAsString(e, "name", TransferLanguage
					.getMap()));
			ht.setSort(XmlUtil.getAttributeAsString(e, "sort", TransferLanguage
					.getMap()));
			ht.setSortId(XmlUtil.getAttributeAsInteger(e, "sortId"));
			ht.setBuffName(XmlUtil.getAttributeAsString(e, "buff", "",
					TransferLanguage.getMap()));
			ht.setColor(XmlUtil.getAttributeAsInteger(e, "color"));
			ht.setDesp(XmlUtil.getAttributeAsString(e, "description", "",
					TransferLanguage.getMap()));
			ht.setIconId(XmlUtil.getAttributeAsInteger(e, "iconId"));
			ht.setUnique(XmlUtil.getAttributeAsBoolean(e, "isUnique"));
			ht.setUsefulLife(XmlUtil.getAttributeAsInteger(e, "usefulLife"));

			this.addHonorTemplete(ht);
		}
	}

	/**
	 * 添加一个称号模板数据
	 * 
	 * @param ht
	 */
	private void addHonorTemplete(HonorTemplete ht) {
		if (!this.templetes.containsKey(ht.getName())) {
			this.templetes.put(ht.getName(), ht);
			if (logger.isInfoEnabled()) {
//				logger.info("[称号] [添加模板] [" + ht.getName() + "]");
				if(logger.isInfoEnabled())
					logger.info("[称号] [添加模板] [{}]", new Object[]{ht.getName()});
			}
			// System.out.println("[称号] [添加模板] ["+ht.getName()+"]");
		} else {
//			logger.warn("[称号] [添加模板失败] [称号名称重复] [" + ht.getName() + "]");
			if(logger.isWarnEnabled())
				logger.warn("[称号] [添加模板失败] [称号名称重复] [{}]", new Object[]{ht.getName()});
			// System.out.println("[称号] [添加模板失败] [称号名称重复] ["+ht.getName()+"]");
		}
	}

	public HonorTemplete getHonorTemplete(String name) {
		return this.templetes.get(name);
	}

	/**
	 * 得到一个称号
	 * 
	 * @param player
	 * @param ht
	 */
	public void gainHonor(Player player, HonorTemplete ht) {
	}

	private int getNewId(Player player) {
		HashMap<String, Honor> honors = this.getHonors(player);
		if (honors == null) {
			return 0;
		}
		Honor hs[] = honors.values().toArray(new Honor[0]);
		int id = 0;
		for (Honor h : hs) {
			if (h.getId() >= id) {
				id = h.getId() + 1;
			}
		}
		return id;
	}

	/**
	 * 检测玩家身上有无过期称号，如果有就去掉
	 * 
	 * @param player
	 */
	public void checkHonorExpirationTime(Player player) {
		if (HonorManager.IS_CLOSED) {
			return;
		}
		long ct = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		HashMap<String, Honor> honors = this.getHonors(player);
		if (honors == null) {
			return;
		}
		Honor hs[] = honors.values().toArray(new Honor[0]);
		ArrayList<Honor> expiredHonor = new ArrayList<Honor>();
		for (Honor h : hs) {
			if (h.getExpirationTime() > 0 && h.getExpirationTime() < ct) {
				expiredHonor.add(h);
			}
		}

		for (Honor h : expiredHonor) {
			loseHonor(player, h, HonorManager.LOSE_REASON_EXPIRATION);
		}
	}

	public static void main(String[] args) {
		// HonorManager hm=new HonorManager();
		// try {
		// hm.init();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public String getLoseReason(int reason) {
		if (reason >= 0 && reason < this.LOSE_REASON.length) {
			return this.LOSE_REASON[reason];
		} else {
			return Translate.text_4243;
		}
	}

	public File getTempletesFile() {
		return templetesFile;
	}

	public void setTempletesFile(File templetesFile) {
		this.templetesFile = templetesFile;
	}

	public void gainHonor(Player player, String honorName) {
		if (HonorManager.IS_CLOSED) {
			return;
		}
		try {
			HonorTemplete ht = this.getHonorTemplete(honorName);
			if (ht != null) {
				this.gainHonor(player, ht);
			} else {
//logger.warn("[称号] [获得称号失败，没有这个称号] [" + honorName + "] [玩家："
//+ player.getName() + "] [玩家ID：" + player.getId()
//+ "] [玩家帐号：" + player.getUsername() + "]");
if(logger.isWarnEnabled())
	logger.warn("[称号] [获得称号失败，没有这个称号] [{}] [玩家：{}] [玩家ID：{}] [玩家帐号：{}]", new Object[]{honorName,player.getName(),player.getId(),player.getUsername()});
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public File getUniqueHonorRecordFile() {
		return uniqueHonorRecordFile;
	}

	public void setUniqueHonorRecordFile(File uniqueHonorRecordFile) {
		this.uniqueHonorRecordFile = uniqueHonorRecordFile;
	}

	/**
	 * 检查玩家身上是否有称号BUFF，但称号已经失去
	 */
	public void checkHonorBuff(Player player) {
		try {
			String[] honorNames = { Translate.text_4244,
					Translate.text_2360,
					Translate.text_4245 };
			BuffTemplate bt = null;
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			for (int i = 0; i < honorNames.length; i++) {
				HonorTemplete ht = HonorManager.getInstance().getHonorTemplete(
						honorNames[i]);
				if (ht != null) {
					Honor honor = HonorManager.getInstance().getHonor(player,
							ht.getName());
					if (honor == null) {
						String buffName = ht.getBuffName();
						if (buffName != null && buffName.length() > 0) {
							bt = btm.getBuffTemplateByName(buffName);
							if (bt != null) {
								Buff buff = player.getBuff(bt.getId());
								if (buff != null) {
									buff.setInvalidTime(System
											.currentTimeMillis());
//logger.warn("[称号] [由于称号失去，取出称号BUFF] [BUFF："
//+ bt.getName() + "] [称号："
//+ ht.getName() + "] [玩家："
//+ player.getName() + "] [玩家ID："
//+ player.getId() + "]");
if(logger.isWarnEnabled())
	logger.warn("[称号] [由于称号失去，取出称号BUFF] [BUFF：{}] [称号：{}] [玩家：{}] [玩家ID：{}]", new Object[]{bt.getName(),ht.getName(),player.getName(),player.getId()});
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//logger.warn("[称号] [由于称号失去，取出称号BUFF] [出现错误] [玩家：" + player.getName()
//+ "] [玩家ID：" + player.getId() + "]");
if(logger.isWarnEnabled())
	logger.warn("[称号] [由于称号失去，取出称号BUFF] [出现错误] [玩家：{}] [玩家ID：{}]", new Object[]{player.getName(),player.getId()});
		}
	}
}
