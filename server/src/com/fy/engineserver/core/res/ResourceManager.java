package com.fy.engineserver.core.res;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.props.AvataProps;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.AbstractSummoned;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.sprite.npc.CaveDoorNPC;
import com.fy.engineserver.sprite.npc.GroundNPC;
import com.fy.engineserver.sprite.npc.SurfaceNPC;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.tools.ds.Heap;

/**
 * 资源素材库
 * @author Administrator
 */
public class ResourceManager implements Constants {
	// protected static Logger logger = Logger.getLogger(ResourceManager.class);
	public static Logger logger = LoggerFactory.getLogger(ResourceManager.class);

	private ResourceManager() {

	}

	static ResourceManager self;

	public static ResourceManager getInstance() {
		return self;
	}

	public void init() throws Exception {
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		load();
		System.out.println("[系统初始化] [资源素材库] [初始化完成] [" + getClass().getName() + "] [耗时：" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "毫秒]");
		ServiceStartRecord.startLog(this);
	}

	public void destory() {

	}

	File resourceDir;
	/**
	 * 形象库
	 */
	public final static String appearHead = "/appearance/";
	public final static String appearTail = ".xtl";
	public LinkedHashMap<String, AbstractResource> appears = new LinkedHashMap<String, AbstractResource>();
	/**
	 * 切片库
	 */
	public final static String pngmtlHead = "/pngSlice/";
	public final static String pngmtlTail = ".xmt";

	private LinkedHashMap<String, AbstractResource> pngmtls = new LinkedHashMap<String, AbstractResource>();
	/**
	 * 部件库 ---》动画组
	 */
	public final static String partHead = "/part/";
	public final static String partTail = ".xtl";

	public LinkedHashMap<String, AbstractResource> parts = new LinkedHashMap<String, AbstractResource>();
	/**
	 * 地图库
	 */
	private LinkedHashMap<String, AbstractResource> gamemapsLow = new LinkedHashMap<String, AbstractResource>();// 低内存
	private LinkedHashMap<String, AbstractResource> gamemapsLowMapNameKey = new LinkedHashMap<String, AbstractResource>();// 低内存

	/**
	 * 查找新版本的数据
	 * @param clientType
	 * @param resType
	 * @param key
	 * @param oldversion
	 * @return
	 */
	public AbstractResource findNewResource(String clientType, byte resType, String relatePath) {
		LinkedHashMap<String, AbstractResource> hm = null;

		switch (resType) {
		case AbstractResource.RES_TYPE_APPEAR:
			hm = appears;
			break;
		case AbstractResource.RES_TYPE_PART:
			hm = parts;
			break;
		case AbstractResource.RES_TYPE_PNGMTL:
			hm = pngmtls;
			break;
		case AbstractResource.RES_TYPE_MAP:
			hm = gamemapsLow;
			break;
		}
		if (hm != null) {
			relatePath = relatePath.trim();
			return hm.get(relatePath);
		}
		return null;
	}

	/**
	 * 客户端类型决定使用哪个版本的地图
	 * @param key
	 * @param clientType
	 * @return
	 */
	public GameInfo getGameInfo(String mapName, String clientType) {
		mapName = mapName.trim();
		return (GameInfo) gamemapsLowMapNameKey.get(mapName);
	}

	public GameInfo[] getGameInfos() {
		return gamemapsLow.values().toArray(new GameInfo[0]);
	}

	public String getResTypeName(int resType) {
		switch (resType) {
		case AbstractResource.RES_TYPE_APPEAR:
			return "形象";
		case AbstractResource.RES_TYPE_PART:
			return "部件";
		case AbstractResource.RES_TYPE_PNGMTL:
			return "图片切片";
		case AbstractResource.RES_TYPE_MAP:
			return "地图";
		case AbstractResource.RES_TYPE_PNGRES:
			return "纯图片";
		}
		return "";
	}

	static private void load(File[] fs, String rootAbsoluteDir, int type, String restype, LinkedHashMap<String, AbstractResource> hm) throws Exception {
		if (fs == null) return;
		for (File f : fs) {
			try {
				String relatePath = f.getAbsolutePath().substring(rootAbsoluteDir.length());
				if (logger.isDebugEnabled()) {
					logger.debug("[初始化数据时] [{}] [{}] [{}] ", new Object[] { restype, f.getPath(), relatePath });
				}
				switch (type) {
				case AbstractResource.RES_TYPE_APPEAR: {
					Appearance a = new Appearance();
					a.load(f, relatePath);
					hm.put(relatePath, a);
					break;
				}
				case AbstractResource.RES_TYPE_PART: {
					Part a = new Part();
					a.load(f, relatePath);
					hm.put(relatePath, a);
					break;
				}
				case AbstractResource.RES_TYPE_PNGMTL: {
					PngMaterial a = new PngMaterial();
					a.load(f, relatePath);
					hm.put(relatePath, a);
					break;
				}
				case AbstractResource.RES_TYPE_MAP: {
					GameInfo a = new GameInfo();
					a.load(f, relatePath);
					hm.put(relatePath, a);
					break;
				}
				default:
					if (logger.isDebugEnabled()) {
						logger.debug("[初始化数据时] [错误] [{}] [{}] [{}] ", new Object[] { restype, f.getPath(), relatePath });
					}
					break;
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled()) logger.error("[初始化数据时出错] [" + restype + "] [" + f.getPath() + "][]", e);
				throw e;
			}
		}
	}

	private void load() throws Exception {
		boolean sucess = true;
		File dir;
		File fs[];
		appears.clear();
		parts.clear();
		pngmtls.clear();
		gamemapsLow.clear();
		gamemapsLowMapNameKey.clear();

		// /////////////////////////形象 ,部件，切片///////////////////////////////////////////////////////////////////////
		{
			File newFile = new File(ConfigServiceManager.getInstance().getFilePath(resourceDir));
			FileInputStream fis = new FileInputStream(newFile.getAbsolutePath() + File.separator + "bin.xtl");
			DataInputStream dis = new DataInputStream(fis);
			int size = dis.readInt();
			for (int i = 0; i < size; i++) {
				Appearance ap = new Appearance();
				ap.load(dis);
				ap.relatePath = appearHead + Appearance.getName(ap.getRace(), ap.getSex()) + appearTail;
				appears.put(ap.relatePath, ap);
			}

			size = dis.readInt();
			for (int i = 0; i < size; i++) {
				PngMaterial ap = new PngMaterial();
				ap.load(dis);
				ap.relatePath = pngmtlHead + ap.name + pngmtlTail;
				pngmtls.put(ap.relatePath, ap);
			}

			size = dis.readInt();
			for (int i = 0; i < size; i++) {
				Part ap = new Part();
				ap.load(dis);
				ap.relatePath = partHead + ap.name + partTail;
				parts.put(partHead + ap.name + partTail, ap);
			}

			fis.close();
		}
		// ///////////////////////////GameMap/////////////////////////////////////////////////////////////////
		FileFilter gamemapFilter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().endsWith(".xmd");
			}
		};
		File newFile = new File(ConfigServiceManager.getInstance().getFilePath(resourceDir));
		dir = new File(newFile.getAbsolutePath() + File.separator + "map" + File.separator + "lowMap");
		fs = dir.listFiles(gamemapFilter);
		load(fs, newFile.getAbsolutePath(), AbstractResource.RES_TYPE_MAP, "地图低版本", gamemapsLow);

		Iterator<AbstractResource> it = gamemapsLow.values().iterator();
		while (it.hasNext()) {
			GameInfo gi = (GameInfo) it.next();
			gamemapsLowMapNameKey.put(gi.getName(), gi);
		}
		if (!sucess) throw new Exception("资源初始化失败");
	}

	public File getResourceDir() {
		return resourceDir;
	}

	public void setResourceDir(File resourceDir) {
		this.resourceDir = resourceDir;
	}

	
	
	
	
	/**
	 * 获取玩家的avata 完整部件名
	 * @param player
	 * @return Avata
	 */
	public static String avata_foot_par = "传送点光效/中式八卦法阵";
	public Avata getAvata(Player player) {
		Avata a = new Avata();
		String race = player.getAvataRace();
		if (race.isEmpty() || race.equalsIgnoreCase("ren")) {
			race = Constants.race_human;
		}
		int career = player.getCareer() - 1;
		String sex = sex_career[career];

		player.setAvataRace(race);
		player.setAvataSex(sex);

		if(player.isSpecialHorse()&&player.isIsUpOrDown()){
			long horseId = player.getRidingHorseId();
			Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
			if(horse!=null){
				a.avata = new String[]{ ResourceManager.partHead+horse.getAvataKey()+ResourceManager.partTail};
				a.avataType = new byte[] { TYPE_BODY };
				player.setAvata(a.avata);
				player.setAvataType(a.avataType);
				if (logger.isErrorEnabled()) logger.error("[ResourceManager] [获取特殊飞行坐骑AVATA] [裸体成功] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getName(), horse.getAvataKey(),a.avata.toString()});
//				player.modifyShouAvata();
				TransitRobberyManager.getInstance().replacePlayerAvata(player, a);
				return a;	
			}else{
				if (logger.isErrorEnabled()) logger.error("[ResourceManager] [获取特殊飞行坐骑AVATA] [裸体失败] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getName(),horseId });
				
			}
			
			
		}
		String keyAppear = appearHead + Appearance.getName(race, sex) + appearTail;
		Appearance appear = (Appearance) appears.get(keyAppear);
		if (appear == null) {
			if (logger.isErrorEnabled()) logger.error("[ResourceManager] [获取AVATA] [失败] [{}] [{}] [{}][{}]", new Object[] { player.getUsername(), player.getName(), keyAppear, appears.size() });
			if (logger.isDebugEnabled()) {
				Iterator<String> keys = appears.keySet().iterator();
				while (keys.hasNext()) {
					if (logger.isDebugEnabled()) logger.debug("[ResourceManager] [仅有形象]  [{}]", new Object[] { keys.next() });
				}
			}

			a.avata = new String[] { partHead + Appearance.getName(race, sex) + partTail };
			a.avataType = new byte[] { TYPE_BODY };
			player.setAvata(a.avata);
			player.setAvataType(a.avataType);
//			player.modifyShouAvata();
			TransitRobberyManager.getInstance().replacePlayerAvata(player, a);
			return a;
		}
		HashMap<String, Part> avatas = new HashMap<String, Part>();
		// 裸体的AVATA
		Part p = appear.getBestFitPart(TYPE_BODY, appear.getName());
		if (p != null) {
			avatas.put(p.relatePath, p);
		} else {
			if (logger.isErrorEnabled()) logger.error("[ResourceManager] [获取AVATA] [裸体失败] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getName(), keyAppear });
		}
		// 礼服,面具
		long shizhuangId = player.getAvataPropsId();
		int shizhuangPartType = -1;
		if (shizhuangId > 0) {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(shizhuangId);

			if (ae != null) {

				Article ar = ArticleManager.getInstance().getArticle(ae.getArticleName());
				if (ar != null && ar instanceof AvataProps) {
					AvataProps ap = (AvataProps) ar;
					String as = ap.getAvata();

					switch (ap.getType()) {
					case AvataProps.TYPE_HEAD:
						shizhuangPartType = TYPE_HEAD;
						break;
					case AvataProps.TYPE_MARRY:
						shizhuangPartType = TYPE_CLOTH;
						break;
					case AvataProps.TYPE_WEAPON:
						shizhuangPartType = TYPE_WEAPON;
						break;
					}
					if (shizhuangPartType >= 0 && !as.isEmpty()) {
						p = appear.getBestFitPart(shizhuangPartType, as);
						if (p != null) {
							avatas.put(p.relatePath, p);
						}
					}
				}
			}
		}

		// 装备的AVATA
		EquipmentColumn ec = player.getEquipmentColumns();
		long ids[] = ec.getEquipmentIds();
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] > 0) {
				ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(ids[i]);
				if (ae != null && ae instanceof EquipmentEntity) {
					EquipmentEntity ee = (EquipmentEntity) ae;
					Equipment ep = (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());
					if (ep == null) {
						continue;
					}
					byte at = ep.getCurAvataType(ee);
					if (at != shizhuangPartType) {
						String as = ep.getCurAvata(ee);
						if (as != null && !as.isEmpty()) {
							p = appear.getBestFitPart(at, as);
							if (p != null) {
								avatas.put(p.relatePath, p);

								if (p.getPartTypeInt() == TYPE_WEAPON) {
									String gx = null;
									if (player.getCareer() == CAREER_DOULUO) {
										gx = "fuzi";
									}
									if (player.getCareer() == CAREER_GUISHA) {
										gx = "bishou";
									}
									if (player.getCareer() == CAREER_LINGZUN) {
										gx = "gx";
									}
									if (player.getCareer() == CAREER_WUHUANG) {
										gx = "gx";
									}
									if (player.getCareer() == CAREER_SHOUKUI) {
										gx = "liandao";
									}
									if (gx != null) {
										p = appear.getBestFitPart(TYPE_WEAPON_GX, gx);
										if (p != null) {
											avatas.put(p.relatePath, p);
										}
									}
								}
							}else {
								if (ep.getCurAvata(ee) != null && ep.getCurAvata(ee).length() > 0) {
									String aas = "";
									if (race.equals(race_human)) {
										if (at == TYPE_WEAPON) {
											if (player.getCareer() == CAREER_DOULUO) {
												aas = "baojian00";
											}else if (player.getCareer() == CAREER_GUISHA) {
												aas = "shuanglun09";
											}else if (player.getCareer() == CAREER_LINGZUN) {
												aas = "maobi09";
											}else if (player.getCareer() == CAREER_WUHUANG) {
												aas = "fazhang09";
											}
										}else if (at == TYPE_CLOTH) {
											aas = "yifu04";
										}
									}else if (race.equals(race_human_new) && !仙界无默认武器) {
										if (at == TYPE_WEAPON) {
											if (player.getCareer() == CAREER_DOULUO) {
												aas = "changzhang01";
											}else if (player.getCareer() == CAREER_GUISHA) {
												aas = "shuangdao01";
											}else if (player.getCareer() == CAREER_LINGZUN) {
												aas = "fazhang01";
											}else if (player.getCareer() == CAREER_WUHUANG) {
												aas = "mozhang01";
											}
										}else if (at == TYPE_CLOTH) {
											aas = "yifu01";
										}
									}
									p = appear.getBestFitPart(at, aas);
									if (p != null) {
										avatas.put(p.relatePath, p);

										if (p.getPartTypeInt() == TYPE_WEAPON) {
											String gx = null;
											if (player.getCareer() == CAREER_DOULUO) {
												gx = "fuzi";
											}
											if (player.getCareer() == CAREER_GUISHA) {
												gx = "bishou";
											}
											if (player.getCareer() == CAREER_LINGZUN) {
												gx = "gx";
											}
											if (player.getCareer() == CAREER_WUHUANG) {
												gx = "gx";
											}
											if (gx != null) {
												p = appear.getBestFitPart(TYPE_WEAPON_GX, gx);
												if (p != null) {
													avatas.put(p.relatePath, p);
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
		if (player.isIsUpOrDown()) {
			// 玩家骑着马
			long horseId = player.getRidingHorseId();
			Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
			if (horse != null) {
				p = getHorseAvataForPlayer(horse, player);
				if (p != null) {
					avatas.put(p.relatePath, p);
				}else {
					player.setSuitFootParticle(avata_foot_par);
				}
			}
		}else if (player.getSuitFootParticle().equals(avata_foot_par)) {
			player.setSuitFootParticle("");
		}
		// 状态影响Avata

		// 排序
		Heap heap = new Heap(new Comparator<Part>() {
			public int compare(Part o1, Part o2) {
				int t1 = o1.getPartTypeInt();
				int t2 = o2.getPartTypeInt();
				if (t1 < t2) return -1;
				else if (t1 > t2) return 1;
				return 0;
			}
		});
		Iterator<Part> it = avatas.values().iterator();
		while (it.hasNext()) {
			heap.insert(it.next());
		}
		int size = heap.size();
		a.avata = new String[size];
		a.avataType = new byte[size];

		for (int i = 0; i < size; i++) {
			p = (Part) heap.extract();
			a.avata[i] = p.relatePath;
			a.avataType[i] = p.getPartTypeInt();
		}

		player.setAvata(a.avata);
		player.setAvataType(a.avataType);

		if (logger.isErrorEnabled()) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < a.avata.length; i++) {
				sb.append("," + a.avata[i]);
			}

			logger.error(" [获取AVATA] [成功] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getName(), sb.toString(), player.isFlying() });
		}
//		player.modifyShouAvata(); 
		try {
			TransitRobberyManager.getInstance().replacePlayerAvata(player, a);
		}catch (Exception e){
			GamePlayerManager.logger.warn("[ResourceManager] [测试角色不加载]",e);
			e.printStackTrace();
		}
		return a;
	}
	
	public static String defaultXZJWeapon = "wq";
	public static boolean 仙界无默认武器 = true;

	public static final int 物品图标 = 0;
	public static final int buff图标 = 1;
	public static final int 其他图标 = 2;

	public byte[] getIconPngBytes(int iconType, String icon) {
		byte[] data = null;
		String id = icon.trim();
		if (!id.isEmpty()) {
			StringBuffer tmp_sb = new StringBuffer(64);
			tmp_sb.setLength(0);
			tmp_sb.append("/icon/big/").append(id).append(".png");

			try {
				FileInputStream fis = new FileInputStream(tmp_sb.toString());
				DataInputStream dis = new DataInputStream(fis);
				int len = dis.available();
				data = new byte[len];
				dis.read(data);
				fis.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return data;
	}

	public Part getHorseAvataForPlayer(Horse horse, Player player) {
		String race = player.getAvataRace();
		if (race.isEmpty() || race.equalsIgnoreCase("ren")) {
			race = Constants.race_human;
		}
		Horse2Manager.logger.warn("[新坐骑系统] [错误:没有获取到数4] [AvataKey:"+horse.getAvataKey()+"] ");
		int career = player.getCareer() - 1;
		String sex = Constants.sex_career[career];
		// String sex = player.getSex() == 0 ? Constants.sex_male : Constants.sex_female;
		String keyAppear = appearHead + Appearance.getName(race, sex) + appearTail;
		Appearance appear = (Appearance) appears.get(keyAppear);
		if (appear == null || horse.getAvataKey().isEmpty()) {
			logger.error("[ResourceManager] [获取Horse{}] [找不到玩家形象,或找不到马avata，失败] [{}] [{}] [{}] [{}][{}] [{}]", new Object[] { horse.getAvataKey(), horse.getNewAvatarKey(), player.getUsername(), player.getName(), keyAppear, appears.size() });
			return null;
		}
		// 裸体的AVATA
		Part p = appear.getBestFitPart(TYPE_HORSE, horse.getAvataKey());
		if (p == null) {
			if (horse.getNewAvatarKey() != null && !horse.getNewAvatarKey().isEmpty()) {
				p = appear.getBestFitPart(TYPE_HORSE, horse.getNewAvatarKey());
			}
			if (p == null) {
				if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取Horse {} {}] [失败] [{}][]", new Object[] { horse.getAvataKey(), horse.getNewAvatarKey() ,keyAppear });
				return null;
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("[ ResourceManager ] [获取Horse] [成功] [{}] [{}] [{}] [{}]", new Object[] { horse.getAvataKey(), p.relatePath ,horse.getHorseName(),player.getName()});
		}
		horse.setAvata(p.relatePath);
		return p;
	}

	public Avata getAvata(Sprite sprite) {
		Avata a = new Avata();
		if (sprite instanceof GroundNPC) {
			setAvata((GroundNPC) sprite);
			a.avata = sprite.getAvata();
			a.avataType = sprite.getAvataType();
			return a;
		}

		String race = sprite.getAvataRace();
		String sex = sprite.getAvataSex();
		String keyAppear = appearHead + Appearance.getName(race, sex) + appearTail;
		Appearance appear = (Appearance) appears.get(keyAppear);

		if (race.equals("diwu")) {
			// NPC配置了地物部件
			String[] ps = sex.split(":");
			if (ps.length >= 1) {
				a.avata = new String[] { (partHead + ps[0] + partTail) };
				a.avataType = new byte[] { TYPE_BUILDING };
				sprite.setAvata(a.avata);
				sprite.setAvataType(a.avataType);
				if (ps.length >= 2) {
					sprite.setAvataAction(ps[1]);
				}
				if (logger.isInfoEnabled()) {
					logger.info("[ ResourceManager ] [获取AVATA] [sprite] [NPC配置了地物部件] [成功] [avataRace,avataSex :{}]", new Object[] { sprite.getName(), keyAppear });
				}
				return a;
			} else {
				if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [sprite] [NPC配置了地物部件] [失败] [avataRace,avataSex 错误:{}]", new Object[] { sprite.getName(), keyAppear });
				return a;
			}
		}

		if (appear == null) {
			if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [sprite] [{}] [失败] [avataRace,avataSex 错误:{}]", new Object[] { sprite.getName(), keyAppear });
			a.avata = new String[] { partHead + Appearance.getName(sprite.getAvataRace(), sprite.getAvataSex()) + partTail };
			a.avataType = new byte[] { TYPE_BODY };
			sprite.setAvata(a.avata);
			sprite.setAvataType(a.avataType);
			return a;
		}
		HashMap<String, Part> avatas = new HashMap<String, Part>();

		// 裸体的AVATA
		Part p = appear.getBestFitPart(TYPE_BODY, appear.getName());
		if (p != null) {
			avatas.put(p.relatePath, p);
		} else {
			if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [sprite] [{}] [裸体失败] [{}]", new Object[] { sprite.getName(), keyAppear });
		}
		//
		//NPC装备avata
		String puton =sprite.getPutOnEquipmentAvata();
		if( puton.length()> 0){
//			if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [sprite] [{}] [裸体失败] [{}] [getPutOnEquipmentAvata:{}]", new Object[] { sprite.getName(), keyAppear,puton });
		
			String pus[] = puton.split(";");
			if( pus.length == 2){
				pus= new String[] {pus[0],pus[1],""};
			}
			if( pus.length!=3) pus = puton.split("；");
			if( pus.length == 2){
				pus= new String[] {pus[0],pus[1],""};
			}
			if( pus.length == 3){
				byte[] ats= new byte[]{TYPE_WEAPON,TYPE_CLOTH,TYPE_HORSE };
				for( int i=0;i< 3;i++){
					byte at = ats[i];
					String as = pus[i];
					if (as != null && !as.isEmpty()) {
						p = appear.getBestFitPart(at, as);
						if (p != null) {
							avatas.put(p.relatePath, p);

							if (p.getPartTypeInt() == TYPE_WEAPON) {
								String gx = null;
								if (sex.equalsIgnoreCase("douluo") ) {
									gx = "fuzi";
								}
								else if (sex.equalsIgnoreCase("guisha")) {
									gx = "bishou";
								}
								else if (sex.equalsIgnoreCase("lingzun")) {
									gx = "gx";
								}
								else if (sex.equalsIgnoreCase("wuhuang")) {
									gx = "gx";
								}
								if (gx != null) {
									p = appear.getBestFitPart(TYPE_WEAPON_GX, gx);
									if (p != null) {
										avatas.put(p.relatePath, p);
									}
								}
							}
						}else{
							if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [sprite] [{}] [裸体失败] [{}] [getPutOnEquipmentAvata:{} partType={},partkey={}]", new Object[] { sprite.getName(), keyAppear,puton,partTypes[at],as });
							
						}
					}
				}
			}else{
				if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [sprite] [{}] [裸体失败] [{}] [getPutOnEquipmentAvata:{} length={}]", new Object[] { sprite.getName(), keyAppear,puton,pus.length });
			}
		}
		// 排序
		Heap heap = new Heap(new Comparator<Part>() {
			public int compare(Part o1, Part o2) {
				int t1 = o1.getPartTypeInt();
				int t2 = o2.getPartTypeInt();
				if (t1 < t2) return -1;
				else if (t1 > t2) return 1;
				return 0;
			}
		});
		Iterator<Part> it = avatas.values().iterator();
		while (it.hasNext()) {
			heap.insert(it.next());
		}
		int size = heap.size();
		a.avata = new String[size];
		a.avataType = new byte[size];

		for (int i = 0; i < size; i++) {
			p = (Part) heap.extract();
			a.avata[i] = p.relatePath;
			a.avataType[i] = p.getPartTypeInt();
		}

		sprite.setAvata(a.avata);
		sprite.setAvataType(a.avataType);

		return a;
	}

	public Avata getAvata(AbstractSummoned sprite) {
		Avata a = new Avata();
		String race = sprite.getAvataRace();
		String sex = sprite.getAvataSex();
		String ef = sprite.getEffectType();
		a.avata = new String[] { partHead + ef + partTail };
		a.avataType = new byte[] { TYPE_SKILL_EFFECT };
		sprite.setAvata(a.avata);
		sprite.setAvataType(a.avataType);

		String keyAppear = appearHead + Appearance.getName(race, sex) + appearTail;
		Appearance appear = (Appearance) appears.get(keyAppear);
		if (appear == null) {
			if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [AbstractSummoned] [失败] [avataRace,avataSex 错误:{}] ", new Object[] { keyAppear });
			return a;
		}
		Part p = (Part) parts.get(a.avata[0]);
		if (p == null) {
			if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [AbstractSummoned] [失败] [{}]", new Object[] { a.avata[0] });
		}
		return a;
	}

	/**
	 * 可跟随NPC
	 * grade表示各个级别显示不同形象
	 * -1表示被摧毁
	 * @param sprite
	 */
	// public void setAvata(BiaoCheNpc sprite) {
	// // System.out.println("设置了跟随NPC的avata.等级:" + sprite.getGrade());
	// }

	/**
	 * 地表NPC
	 */
	public void setAvata(SurfaceNPC surface) {
		Sprite sprite = (Sprite) surface;
		int grade = surface.getGrade();
		String avs[] = surface.getAvatas();
		// race ,partkey, lv0,lv1,lv2
		if (avs == null) {
			JiazuSubSystem.logger.error("surface:avataNUll" + surface.getClass());
			return;
		}
		if (avs.length < 3) {
			if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [SurfaceNPC] [{}] [grade:{}] [失败] [getAvatas().length<3]", new Object[] { sprite.getName(), grade });
			return;
		}
		sprite.setAvataSex("");
		sprite.setAvataRace(avs[0]);
		String partkey = partHead + avs[1] + partTail;
		String action = avs[2];
		if (grade + 2 < avs.length) {
			action = avs[grade + 2];
		} else {
			action = avs[avs.length - 1];
		}
		Part p = null;
		if (parts.containsKey(partkey)) {
			p = (Part) parts.get(partkey);
		}
		if (p != null) {
			sprite.setAvata(new String[] { p.relatePath });
			sprite.setAvataType(new byte[] { p.getPartTypeInt() });
			sprite.setAvataAction(action);
			if (logger.isInfoEnabled()) logger.info("[ ResourceManager ] [获取AVATA] [SeptStationNPC] [{}] [grade:{}] [成功] [part: {}] [action: {}]", new Object[] { sprite.getName(), grade, partkey, action });
			return;
		}
		if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [SeptStationNPC] [{}] [grade:{}] [失败] [part: {}]", new Object[] { sprite.getName(), grade, partkey });
	}

	/**
	 * 地物形式NPC
	 * @param sprite
	 */
	public void setAvata(GroundNPC sprite) {
		int grade = sprite.getGrade();
		String avs[] = sprite.getAvatas();
		if (sprite instanceof CaveDoorNPC) {
			CaveDoorNPC doorNPC = (CaveDoorNPC) sprite;
			if (doorNPC.isClosed()) {
				avs = doorNPC.getAvatas();
			} else {
				avs = doorNPC.getAvatas1();
			}
		}
		if (sprite.isInBuilding()) {
			String avsbuild[] = sprite.getBuildingAvatas();
			if (avsbuild != null && avsbuild.length >= 3) {
				avs = avsbuild;
			}
		}

		// race ,partkey, lv0,lv1,lv2
		if (avs.length < 3) {
			if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [SeptStationNPC] [{}] [grade:{}] [失败] [getAvatas().length<3]", new Object[] { sprite.getName(), grade });
			return;
		}
		sprite.setAvataSex("");
		sprite.setAvataRace("diwu");

		String partkey = partHead + avs[1] + partTail;
		String action = avs[2];
		if (grade + 2 < avs.length) {
			action = avs[grade + 2];
		} else {
			action = avs[avs.length - 1];
		}

		Part p = null;
		if (parts.containsKey(partkey)) {
			p = (Part) parts.get(partkey);
		}
		if (p != null) {
			sprite.setAvata(new String[] { p.relatePath });
			sprite.setAvataType(new byte[] { p.getPartTypeInt() });
			sprite.setAvataAction(action);
			if (logger.isInfoEnabled()) logger.info("[ ResourceManager ] [获取AVATA] [SeptStationNPC] [{}] [grade:{}] [成功] [part: {}] [action: {}]", new Object[] { sprite.getName(), grade, partkey, action });

			return;
		}
		if (logger.isErrorEnabled()) logger.error("[ ResourceManager ] [获取AVATA] [SeptStationNPC] [{}] [grade:{}] [失败] [part: {}] [avs:{}]", 
				new Object[] { sprite.getName(), grade, partkey ,Arrays.toString(avs)});

	}

	public Avata getAvataLivingObject(LivingObject o) {
		if (o instanceof Player) {
			return getAvata((Player) o);
		}
		if (o instanceof GroundNPC) {
			setAvata((GroundNPC) o);
			Avata a = new Avata();
			a.avata = ((GroundNPC) o).getAvata();
			a.avataType = ((GroundNPC) o).getAvataType();
			return a;
		}
		if (o instanceof Sprite) {
			return getAvata((Sprite) o);
		}
		if (o instanceof AbstractSummoned) {
			return getAvata((AbstractSummoned) o);
		}
		// 需要放到Sprite前面
		// if (o instanceof SeptStationNPC) {
		// setAvata((SeptStationNPC) o);
		// }
		return null;
	}

	public static void main(String[] args) {
		// String race = "ren";
		// if (race.isEmpty()) {
		// race = Constants.race_human;
		// }
		// String sex = Constants.sex_male;
		// String keyAppear = appearHead + Appearance.getName(race, sex) + appearTail;
		// Appearance appear = new Appearance();
		// appear.setRace(race);
		// appear.setSex(sex);
		// String key = "laohu01";
		// Part p = appear.getBestFitPart(TYPE_HORSE, key);
		// if( p == null){
		// System.out.println("[ ResourceManager ] [获取Horse"+key+"] [失败] [" + keyAppear + "][]");
		// return null;
		// }
		// horse.setAvata(p.relatePath);
		// return p;
	}

}
