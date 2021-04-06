package com.fy.engineserver.billboard.special;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.Special_1EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.Special_2EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.equipments.Weapon;
import com.fy.engineserver.datasource.article.data.props.FlopSchemeEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.AritcleInfoHelper;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.CREATE_SPECIALEQUIPMENT_BROADCAST_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_ARTICLE_INFO_RES;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.seal.data.Seal;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class SpecialEquipmentManager implements Runnable {
	

	public static Logger logger = LoggerFactory.getLogger(SpecialEquipmentManager.class);
	public static SimpleEntityManager<SpecialEquipmentBillBoard> em;
	
	public static SpecialEquipmentManager self;
	public static int specialValueADD = 2;
	public static int specialValueDel = 1;
	
	public static int 武器总类 = 7;
	public static int 天榜装备个数 = 44;
	public static String[] 榜单类型 = new String[]{Translate.天榜,Translate.地榜};
	
	
	public static boolean 测试生成野外boss = false;
	
	private String fileName;
	private String diskName;
	
	public static long 产生装备不掉时间  = 1l*12*60*60*1000;
	
	public static byte 普通 = 0;
	public static byte 鸿天帝宝 = 1;
	public static byte 伏天古宝 = 2;
	public static long 宝魂值变化间隔时间 = 10*60*1000;
	private PlayerManager playerManager = PlayerManager.getInstance();
	
	//boss刷新时间间隔一个礼拜
	public static long 生成boss信息间隔 = 1l*7*24*60*60*1000-2*60*60*1000;
//	public static long 生成boss信息间隔 = 1l*5*30*24*60*60*1000-1l*2*60*60*1000;
	//提前12小时通知
	public static long 第一次通知生成boss信息的时间= 2*60*60*1000;
	public static long 第二次通知生成boss信息的时间= 60*60*1000;
	public static long 第三次通知生成boss信息的时间= 10*60*1000;
	
	public static long 副本cd = 1l*7*24*60*60*1000;
	
	public static int 封印等级 = 150;
	
	public boolean bossInit = false;
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000l);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			try{
				if(!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()){
					continue;
				}
				if(SystemTime.currentTimeMillis() - lastUpdateTime > 宝魂值变化间隔时间){
					lastUpdateTime = SystemTime.currentTimeMillis();
					specialValueOprate();
				}
				
				if(!bossInit){
					bossInit = true;
					if(flushBossInfo != null && !flushBossInfo.isEffect() &&flushBossInfo.getAppearMonster() <= SystemTime.currentTimeMillis() && !flushBossInfo.bossDie){
						
						//生成boss
						Game game = null;
						String mapName = flushBossInfo.getAppearMap().getMapName();
						game = GameManager.getInstance().getGameByName(mapName, flushBossInfo.getCountry());
						if(game == null){
							game = GameManager.getInstance().getGameByName(mapName, 0);
						}
						if(game != null){
							
							MemoryMonsterManager mm = (MemoryMonsterManager) MemoryMonsterManager.getMonsterManager();
							Monster monster = mm.createMonster(flushBossInfo.monsterId);
							if(monster != null){
								Point bornPoint = new Point(flushBossInfo.appearMap.x, flushBossInfo.appearMap.y);
								monster.setBornPoint(bornPoint);
								monster.setX(flushBossInfo.appearMap.x);
								monster.setY(flushBossInfo.appearMap.y);
								game.addSprite(monster);
								SpecialEquipmentManager.logger.error("[初始化万灵榜boss] [生成boss成功] [map:"+flushBossInfo.appearMap.getMapName()+"] ["+flushBossInfo.country+"]");
							}else{
								SpecialEquipmentManager.logger.error("[初始化万灵榜npc] [生成boss错误] [monsternull] ["+flushBossInfo.monsterId+"]");
							}
						}else{
							SpecialEquipmentManager.logger.error("[初始化万灵榜npc] [生成boss错误] [game为null] [map:"+flushBossInfo.appearMap.getMapName()+"] ["+flushBossInfo.country+"]");
						}
					}
				}
				
				if(flushBossInfo != null && flushBossInfo.isEffect()){
					flushBossInfo.heartBeat();
					
				}else{
					//生成刷新boss信息实体
					if(flushBossInfo != null){
						long appearBoss = flushBossInfo.getAppearMonster();
						boolean bossDie = flushBossInfo.bossDie;
						long bossDieTime = flushBossInfo.bossDieTime;
						if(SystemTime.currentTimeMillis() - bossDieTime >= 生成boss信息间隔 && bossDie){
							createFlushBossInfo();
						}
					}else{
						Seal seal = SealManager.getInstance().sealMap.get(封印等级);
						if(seal != null){
							if(SystemTime.currentTimeMillis() - seal.getSealCanOpenTime() >= 生成boss信息间隔){
								createFlushBossInfo();
							}
						}else{
							if(测试生成野外boss){
								createFlushBossInfo();
							}
						}
					}
				}
			}catch(Throwable t){
				SpecialEquipmentManager.logger.error("[混沌万灵榜心跳异常]",t);
			}
		}
	}
	private long lastUpdateTime = 0;
	
	DiskCache disk = null;
	
	public long downcityLastCreateEEtime = -1l;
	
	public void init() throws Exception {
		
		
		try {
			loadExcel();
			logger.warn("[混沌万灵榜配置表加载成功]");
			File file = new File(diskName);
			disk = new DefaultDiskCache(file,null,"specialFlushBossInfo", 100L * 365 * 24 * 3600 * 1000L);
			Object o = getDisk().get("bossInfo");
			if( o != null){
				setFlushBossInfo((FlushBossInfo)o);
				logger.warn("[混沌万灵榜初始化刷新boss信息] [成功]");
			}else{
				logger.warn("[混沌万灵榜初始化刷新boss信息] [没有信息]");
			}
			
			Object timeo = getDisk().get("eeCreateTime");
			if(timeo != null){
				downcityLastCreateEEtime = (Long)timeo;
			}
			
			em = SimpleEntityManagerFactory.getSimpleEntityManager(SpecialEquipmentBillBoard.class);
			SpecialEquipmentBillBoard tempsd = null;
			long id = em.nextId();
			tempsd = new SpecialEquipmentBillBoard(id);
			LinkedHashMap<String, List<Long>> special1 = new LinkedHashMap<String, List<Long>>();
			LinkedHashMap<String, List<Long>> special2 = new LinkedHashMap<String, List<Long>>();
			
			Equipment[] et1s = ArticleManager.getInstance().allSpecialEquipments1;
			for(Equipment e : et1s){
				special1.put(e.getName(),new ArrayList<Long>());
			}
			Equipment[] et2s = ArticleManager.getInstance().allSpecialEquipments2;
			for(Equipment e : et2s){
				special2.put(e.getName(),new ArrayList<Long>());
			}
			
			tempsd.setSpecial1Map(special1);
			tempsd.setSpecial2Map(special2);
			
			List<SpecialEquipmentBillBoard>  temp = em.query(SpecialEquipmentBillBoard.class, null, new Object[]{},null, 1, 10);
			
			SpecialEquipmentBillBoard sd;
			if(temp != null && temp.size() > 0){
	
				int max = temp.size();
				for(int num =0;num < max;num++){
					sd = temp.get(num);
					LinkedHashMap<String, List<Long>> map1 = sd.getSpecial1Map();
					LinkedHashMap<String, List<Long>> map2 = sd.getSpecial2Map();
					for(Entry<String, List<Long>> en : map1.entrySet()){
						if(en.getValue().size() > 0){
							List<Long> specialList1 = tempsd.getSpecial1Map().get(en.getKey());
							if(specialList1 != null){
								for(long eqId:en.getValue()){
									if(!specialList1.contains(eqId)){
										specialList1.add(eqId);	
									}
								}
								tempsd.getSpecial1Map().put(en.getKey(), specialList1);
							}else{
								throw new RuntimeException("混沌万灵榜配置表1错误，改了已经生成的装备。现有"+en.getKey()+"新配置表中没有");
							}
						}
					}
					
					for(Entry<String, List<Long>> en : map2.entrySet()){
						if(en.getValue().size() > 0){
							List<Long> specialList2 = tempsd.getSpecial2Map().get(en.getKey());
							if(specialList2 != null){
								for(long eqId:en.getValue()){
									if(!specialList2.contains(eqId)){
										specialList2.add(eqId);	
									}
								}
								tempsd.getSpecial2Map().put(en.getKey(), specialList2);
							}else{
								throw new RuntimeException("混沌万灵榜配置表2错误，改了已经生成的装备。现有"+en.getKey()+"新配置表中没有");
							}
						}
					}
				}
				
//				SpecialEquipmentBillBoard sd =temp.get(0);
				
//				LinkedHashMap<String, List<Long>> map1 = sd.getSpecial1Map();
//				LinkedHashMap<String, List<Long>> map2 = sd.getSpecial2Map();
//				
//				for(Entry<String, List<Long>> en : map1.entrySet()){
//					if(en.getValue().size() > 0){
//						List<Long> specialList1 = tempsd.getSpecial1Map().get(en.getKey());
//						if(specialList1 != null){
//							tempsd.getSpecial1Map().put(en.getKey(), en.getValue());
//						}else{
//							throw new RuntimeException("混沌万灵榜配置表1错误，改了已经生成的装备。现有"+en.getKey()+"新配置表中没有");
//						}
//					}
//				}
//				
//				for(Entry<String, List<Long>> en : map2.entrySet()){
//					if(en.getValue().size() > 0){
//						List<Long> specialList2 = tempsd.getSpecial2Map().get(en.getKey());
//						if(specialList2 != null){
//							tempsd.getSpecial2Map().put(en.getKey(), en.getValue());
//						}else{
//							throw new RuntimeException("混沌万灵榜配置表2错误，改了已经生成的装备。现有"+en.getKey()+"新配置表中没有");
//						}
//					}
//				}
				
				List<Entry<String, List<Long>>> list1 = tempsd.sortSpecial(special1);
				special1.clear();
				for(Entry<String, List<Long>> en : list1){
					special1.put(en.getKey(), en.getValue());
				}
				
				List<Entry<String, List<Long>>> list2 = tempsd.sortSpecial(special2);
				special2.clear();
				for(Entry<String, List<Long>> en : list2){
					special2.put(en.getKey(), en.getValue());
				}
				tempsd.setSpecial1Map(special1);
				tempsd.setSpecial2Map(special2);
				
				setSpecialEquipmentBillBoard(tempsd);
				em.save(tempsd);
				logger.warn("[重启保存新榜成功]");
				for(SpecialEquipmentBillBoard bb:temp){
					try{
						em.remove(bb);
						logger.warn("[重启删除旧榜成功]");
					}catch(Exception e){
						logger.error("[启动删除旧榜错误] [对程序没影响]",e);
					}
				}
				logger.warn("[混沌万灵榜管理从数据库初始化]");
				
			}else{
				
				List<Entry<String, List<Long>>> list1 = tempsd.sortSpecial(special1);
				
				special1.clear();
				for(Entry<String, List<Long>> en : list1){
					special1.put(en.getKey(), en.getValue());
				}
				
				List<Entry<String, List<Long>>> list2 = tempsd.sortSpecial(special2);
				special2.clear();
				for(Entry<String, List<Long>> en : list2){
					special2.put(en.getKey(), en.getValue());
				}
				tempsd.setSpecial1Map(special1);
				tempsd.setSpecial2Map(special2);
				
				em.save(tempsd);
				setSpecialEquipmentBillBoard(tempsd);
				logger.warn("[混沌万灵榜管理初始化数据成功]");
			}
			lastUpdateTime = SystemTime.currentTimeMillis();
			self = this;
			Thread t = new Thread(this);
			t.start();
			if(logger.isInfoEnabled()){
				logger.info("[混沌万灵榜管理初始化]");
			}
		} catch (Exception e) {
			logger.error("[混沌万灵榜管理初始化异常]",e);
			throw e;
		}
		ServiceStartRecord.startLog(this);
	}
	
	public void destroy(){
		try{
			em.destroy();
		}catch(Exception e){
			logger.error("[specialEquipmentManager destroy]",e);
		}
	}
	
	public static SpecialEquipmentManager getInstance(){
		return self;
	}
	
	private SpecialEquipmentBillBoard specialEquipmentBillBoard;

	public SpecialEquipmentBillBoard getSpecialEquipmentBillBoard() {
		return specialEquipmentBillBoard;
	}

	public void setSpecialEquipmentBillBoard(SpecialEquipmentBillBoard specialEquipmentBillBoard) {
		this.specialEquipmentBillBoard = specialEquipmentBillBoard;
	}
	
	private FlushBossInfo flushBossInfo;
	public FlushBossInfo getFlushBossInfo() {
		return flushBossInfo;
	}

	public void setFlushBossInfo(FlushBossInfo flushBossInfo) {
		this.flushBossInfo = flushBossInfo;
	}

	public boolean isSpecialEquipment(Equipment equipment){
		
		if(equipment.getSpecial() != SpecialEquipmentManager.普通){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	public EquipmentEntity createEntity(Equipment equipment,int desc){
		if(ishaving(equipment))
			return null;
		
		ArticleManager am = ArticleManager.getInstance();
		SpecialEquipmentMappedStone mapped = ArticleManager.getInstance().mappedStoneMap.get(equipment.getName());
		
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		try{
//			EquipmentEntity ee = (EquipmentEntity) aem.createEntity(equipment,true, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, ArticleManager.equipmentColorMaxValue, 1, true);
			EquipmentEntity ee = (EquipmentEntity) aem.createEntity(equipment,true,desc, null, ArticleManager.equipmentColorMaxValue, 1, true);
			if(equipment.getSpecial() == 鸿天帝宝){
				if(ee instanceof Special_1EquipmentEntity){
					
	//				ee.setStar(EquipmentEntity.满星);
					ee.setEndowments(EquipmentEntity.满资质);
					ee.setInscriptionFlag(true);
						
					long[] inlays = ee.getInlayArticleIds();
					int[] colors = ee.getInlayArticleColors();
					if(inlays == null || colors == null ||inlays.length == 0 || colors.length == 0){
					}else{
						if(mapped ==  null){
							if(logger.isWarnEnabled())
								logger.warn("[生成混沌万灵榜装备错误] [没有对应宝石对象] ["+equipment.getName()+"] ");
							return null;
						}
						String[] mappedStone = mapped.getStoneNames();
						if(mappedStone.length == colors.length && inlays.length == colors.length){
							int max = inlays.length;
							
							for(int index = 0; index < max; index++){
								String stoneName = mappedStone[index];
								if(am.getArticle(stoneName) != null){
									ArticleEntity ae = aem.createEntity(am.getArticle(stoneName), true, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, am.getArticle(stoneName).getColorType(), 1, true);
									inlays[index] = ae.getId(); 
								}else{
									if(logger.isWarnEnabled()){
										SpecialEquipmentManager.logger.warn("[产生鸿天帝宝错误] [没有宝石] ["+stoneName+"]");
									}
									return null;
								}
							}
							ee.setInlayArticleIds(inlays);
							if(logger.isWarnEnabled()){
								logger.warn("[生成鸿天帝宝镶嵌宝石成功] ["+equipment.getName()+"]");
							}
						}else{
							if(logger.isWarnEnabled())
								logger.warn("[生成鸿天帝宝错误] [宝石不对应] ["+equipment.getName()+"]");
							return null;
						}
					}
					ee.setDirty(true);
					((Special_1EquipmentEntity)ee).setCreateTime(SystemTime.currentTimeMillis());
					
					String st = specialEquipmentBillBoard.putSpecial1(equipment.getName(), ee.getId());
					if(st !=null){
						SpecialEquipmentManager.logger.warn("[产生鸿天帝宝] ["+equipment.getName()+"]");
						
						//发送世界广播
						int rank = this.getRankByEquipmentName(equipment.getName());
						if(rank > 0){
							int realRank = rank% SpecialEquipmentManager.天榜装备个数;
							if(realRank == 0){
								realRank = SpecialEquipmentManager.天榜装备个数;
							}
							//默认天榜
							int billType = 0;
							if(rank > SpecialEquipmentManager.天榜装备个数){
								billType = 1;
							}
							int equpmentShowType = 0;
							int t =	equipment.getEquipmentType();
							if(t == 0){
								//武器
								if(equipment instanceof Weapon){
									//武器类型改为从1开始  0为空手  需要改
									equpmentShowType = (int)((Weapon)equipment).getWeaponType() -1;
								}else{
									if(SpecialEquipmentManager.logger.isWarnEnabled())
										SpecialEquipmentManager.logger.warn("[查询特殊装备1错误] [不是武器类型] ["+equipment.getName()+"]");
								}
							}else{
								equpmentShowType = SpecialEquipmentManager.武器总类+t;
							}
							
							CREATE_SPECIALEQUIPMENT_BROADCAST_RES res = new CREATE_SPECIALEQUIPMENT_BROADCAST_RES(GameMessageFactory.nextSequnceNum(), equipment.getName(), equpmentShowType, billType, realRank);
							
							Player[] ps = playerManager.getOnlinePlayers();
							for(int i= 0;i<ps.length;i++){
								ps[i].addMessageToRightBag(res);
							}
							if(this.logger.isWarnEnabled()){
								SpecialEquipmentManager.logger.warn("[产生鸿天帝宝发送世界通知成功] ["+equipment.getName()+"]");
							}
						}
					}else{
						SpecialEquipmentManager.logger.warn("[产生鸿天帝宝失败] ["+equipment.getName()+"]");
					}
					return ee;
				}else{
					if(logger.isWarnEnabled())
						logger.warn("[生成鸿天帝宝错误] [不是鸿天帝宝] ["+equipment.getName()+"]");
				}
			}else if(equipment.getSpecial() == 伏天古宝){
				
				if(ee instanceof Special_2EquipmentEntity ){
	//				ee.setStar(EquipmentEntity.满星);
					ee.setEndowments(EquipmentEntity.满资质);
					ee.setInscriptionFlag(true);
					
					long[] inlays = ee.getInlayArticleIds();
					int[] colors = ee.getInlayArticleColors();
					
					
					if(inlays == null || colors == null ||inlays.length == 0 || colors.length == 0){
					}else{
						if(mapped ==  null){
							if(logger.isWarnEnabled())
								logger.warn("[生成混沌万灵榜装备伏天古宝错误] [没有对应宝石对象] ["+equipment.getName()+"] ");
							return null;
						}
						String[] mappedStone = mapped.getStoneNames();
						if(mappedStone.length == colors.length && inlays.length == colors.length){
							int max = inlays.length;
							
							for(int index = 0; index < max; index++){
								String stoneName = mappedStone[index];
								if(am.getArticle(stoneName) != null){
									ArticleEntity ae = aem.createEntity(am.getArticle(stoneName), true, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, am.getArticle(stoneName).getColorType(), 1, true);
									inlays[index] = ae.getId(); 
								}else{
									if(logger.isWarnEnabled()){
										SpecialEquipmentManager.logger.warn("[产生伏天古宝错误] [没有宝石] ["+stoneName+"]");
									}
									return null;
								}
							}
							ee.setInlayArticleIds(inlays);
							
						}else{
							if(logger.isWarnEnabled())
								logger.warn("[生成伏天古宝错误] [宝石不对应] ["+equipment.getName()+"]");
							return null;
						}
					}
					((Special_2EquipmentEntity)ee).setCreateTime(SystemTime.currentTimeMillis());
					ee.setDirty(true);
					String st = specialEquipmentBillBoard.putSpecial2(equipment.getName(), ee.getId());
					if(st !=null){
						SpecialEquipmentManager.logger.warn("[产生伏天古宝] ["+equipment.getName()+"]");
					}else{
						SpecialEquipmentManager.logger.warn("[产生伏天古宝失败] ["+equipment.getName()+"]");
					}
					return ee;
				}else{
					if(logger.isWarnEnabled())
						logger.warn("[生成伏天古宝错误] [不是伏天古宝] ["+equipment.getName()+"]");
				}
			}
		}catch(Exception ex){
			SpecialEquipmentManager.logger.error("[生成特殊装备异常]",ex);
		}
		return null;
	}
	
	/**
	 * 特殊装备掉落
	 * @param player
	 */
	public void specialEquipmentEntityDrop(Player player,Fighter caster){
		try{
			if(caster instanceof Player){
				Map<String, List<Long>> map = this.getSpecialEquipmentBillBoard().getSpecial1Map();
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				
				Set<String> set = map.keySet();
				String[] arr = set.toArray(new String[0]);
				int max = arr.length;
				List<Long> list = null;
				ArticleEntity ae = null;
				Special_1EquipmentEntity se1 = null;
				for(int i = 0;i<max;i++){
					list =  map.get(arr[i]);
					if(list != null && list.size() > 0){
						ae = ArticleEntityManager.getInstance().getEntity(list.get(0));
						if( ae != null && ae instanceof Special_1EquipmentEntity){
							boolean testFlag = false;
							se1 = (Special_1EquipmentEntity)ae;
							if(se1.getPlayerId() == player.getId()){
								if(se1.isDrop()){
									boolean drop = player.玩家掉落物品(list.get(0),caster);
									testFlag = drop;
									if(!drop){
										Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
										if(a != null && a instanceof Equipment){
											Equipment eq = (Equipment)a;
											byte equipmentType = -1;
											if(eq instanceof Weapon){
												equipmentType = 0;
											}else{
												equipmentType = (byte)eq.getEquipmentType();
											}
											for(Soul soul :player.getSouls()){
												EquipmentColumn ec = soul.getEc();
												EquipmentEntity ee = (EquipmentEntity) ec.get(equipmentType);
												if(ee != null && ee.getId() == se1.getId()){
													ee = soul.getEc().takeOff(equipmentType, soul.getSoulType());
													if(ee != null){
														drop = true;
														player.人掉落物品(player.currentGame, player, ee, 1, caster);
														break;
													}
													break;
												}
											}
										}
									}
									if(drop){
										try{
											StringBuffer sb = new StringBuffer();
											// 玩家被杀死后邮件通知
											if (caster instanceof Player) {
												sb.append(Translate.translateString(Translate.您被某某杀死了, new String[][] { { Translate.PLAYER_NAME_1, caster.getName() } }));
											} else if (caster instanceof Pet) {
												sb.append(Translate.translateString(Translate.您被某某的宠物杀死了, new String[][] { { Translate.PLAYER_NAME_1, ((Pet) caster).getOwnerName() } }));
											}
											
											if (sb.length() > 0) {
												sb.append("，");
											}

											sb.append(Translate.translateString(Translate.您掉落了某某, new String[][] { { Translate.STRING_1, se1.getArticleName() } }));
											MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[0], Translate.死亡提示邮件, sb.toString(), 0, 0, 0, Translate.死亡掉落万灵榜装备);
										}catch(Exception e){
											SpecialEquipmentManager.logger.error("[装备逃跑异常] ["+se1.getId()+"] ["+se1.getArticleName()+"]",e);
										}
									}
//									specialEquipmentBillBoard.removeSpecial1(se1.getArticleName(), se1.getId());
									se1.setPlayerId(-1);
									if(SpecialEquipmentManager.logger.isWarnEnabled()){
										SpecialEquipmentManager.logger.warn("[玩家死亡掉落特殊装备成功] ["+player.getLogString()+"] ["+se1.getArticleName()+"] [是否真掉落+"+drop+"]");
									}
								}
							}
							if(SpecialEquipmentManager.logger.isWarnEnabled()){
								SpecialEquipmentManager.logger.warn("[玩家死亡掉落特殊装备] [测试] ["+(se1==null?"null":se1.getPlayerId())+"] ["+(se1==null?"null":se1.isDrop())+"] [testFlag:"+testFlag+"] ["+player.getLogString()+"] ["+(se1==null?"null":se1.getArticleName())+"]");
							}
						}
					}
				}
			}else{
				SpecialEquipmentManager.logger.warn("[玩家死亡不是被玩家杀死不掉装备] ["+player.getLogString()+"]");
			}
		}catch(Exception e){
			SpecialEquipmentManager.logger.error("[玩家死亡掉落装备错误] ["+player.getLogString()+"]",e);
		}
	}
	
	public void specialValueOprate(){

		Map<String, List<Long>> map = this.getSpecialEquipmentBillBoard().getSpecial1Map();
		
		Set<String> set = map.keySet();
		String[] arr = set.toArray(new String[0]);
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		int max = arr.length;
		List<Long> list = null;
		for(int i=0;i<max;i++){
			list = map.get(arr[i]);
			if(list !=null && list.size() > 0){
				
				for(int index = 0;index<list.size();index++){
					ArticleEntity ae = aem.getEntity(list.get(index));
					if(ae != null && ae instanceof Special_1EquipmentEntity){
						Special_1EquipmentEntity se1 = (Special_1EquipmentEntity)ae;
						
						long playerId = se1.getPlayerId();
						try {
							if(playerId > 0){
								Player player = null;
								try{
									player = playerManager.getPlayer(playerId);
								}catch (Exception e) {
									e.printStackTrace();
								}
								if(player != null && player.isOnline()){
									se1.addSpecailValue(specialValueADD);
									String description = AritcleInfoHelper.generate(player, ae);
									player.addMessageToRightBag(new QUERY_ARTICLE_INFO_RES(GameMessageFactory.nextSequnceNum(), ae.getId(), description));
								}else{
									if(se1.decreaseSpecailValue(specialValueDel)){
										if(se1.isEscape()){
											if(player != null){
												ArticleEntity ae1 = player.removeFromKnapsacks(se1,"混沌万灵榜", false);
												if(ae1 == null){
													Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
													if(a != null && a instanceof Equipment){
														Equipment eq = (Equipment)a;
														byte equipmentType = -1;
														if(eq instanceof Weapon){
															equipmentType = 0;
														}else{
															equipmentType = (byte)eq.getEquipmentType();
														}
														boolean escape = false;
														for(Soul soul :player.getSouls()){
															EquipmentColumn ec = soul.getEc();
															EquipmentEntity ee = (EquipmentEntity) ec.get(equipmentType);
															if(ee != null && ee.getId() == se1.getId()){
																ae1 = soul.getEc().takeOff(equipmentType, soul.getSoulType());
																escape = true;
																SpecialEquipmentManager.logger.error("[万灵榜装备逃跑] ["+se1.getArticleName()+"] ["+se1.getId()+"] ["+player.getLogString()+"]");
																Knapsack.logger.error("[万灵榜装备逃跑] ["+se1.getArticleName()+"] ["+se1.getId()+"] ["+player.getLogString()+"] ["+(ee != null?ee.getId():"null")+"]");
																break;
															}
														}
														if(!escape){
															SpecialEquipmentManager.logger.error("[万灵榜装备逃跑失败] ["+se1.getArticleName()+"] ["+se1.getId()+"] ["+player.getLogString()+"]");
															Knapsack.logger.error("[万灵榜装备逃跑失败] ["+se1.getArticleName()+"] ["+se1.getId()+"] ["+player.getLogString()+"]");
														}
													}
												}
												if(ae1 != null){
													try{
		//												您的鸿天帝宝由于您长时间离开，舍您而去！请您不要过分悲伤，它还会再次出现寻找有缘人
	//													MailManager.getInstance().sendMail(playerId, new ArticleEntity[0], Translate.装备逃跑提示邮件, "您的鸿天帝宝"+ae1.getArticleName()+"由于您长时间离开，舍您而去！请您不要过分悲伤，它还会再次出现寻找有缘之人", 0, 0, 0, "混沌万灵榜");
														String des1 = Translate.translateString(Translate.您的鸿天帝宝XX由于您长时间离开舍您而去, new String[][]{{Translate.STRING_1,ae1.getArticleName()}});
	//													MailManager.getInstance().sendMail(playerId, new ArticleEntity[0], Translate.装备逃跑提示邮件,Translate.您的鸿天帝宝+ae1.getArticleName()+Translate.由于您长时间离开舍您而去, 0, 0, 0, "混沌万灵榜");
														MailManager.getInstance().sendMail(playerId, new ArticleEntity[0], Translate.装备逃跑提示邮件,des1, 0, 0, 0, "混沌万灵榜");
													}catch(Exception e){
														SpecialEquipmentManager.logger.error("[装备逃跑异常] ["+ae1.getId()+"] ["+ae1.getArticleName()+"]",e);
													}
												}
											}
											String st = specialEquipmentBillBoard.removeSpecial1(se1.getArticleName(), se1.getId());
											if(st != null){
												if(SpecialEquipmentManager.logger.isWarnEnabled()){
													SpecialEquipmentManager.logger.warn("[特殊装备抱魂值操作] [删除装备成功] ["+se1.getArticleName()+"] [装备id:"+se1.getId()+"]");
												}
											}else{
												if(SpecialEquipmentManager.logger.isWarnEnabled()){
													SpecialEquipmentManager.logger.warn("[特殊装备抱魂值操作] [删除装备失败] ["+se1.getArticleName()+"] [装备id:"+se1.getId()+"]");
												}
											}
										}
									}
								}
							}
	//						else{
	//							
	//							String st = specialEquipmentBillBoard.removeSpecial1(se1.getArticleName(), se1.getId());
	//							if(st != null){
	//								if(SpecialEquipmentManager.logger.isWarnEnabled()){
	//									SpecialEquipmentManager.logger.warn("[特殊装备抱魂值操作] [装备没有玩家id] [删除装备成功] ["+se1.getArticleName()+"]");
	//								}
	//							}else{
	//								if(SpecialEquipmentManager.logger.isWarnEnabled()){
	//									SpecialEquipmentManager.logger.warn("[特殊装备抱魂值操作] [装备没有玩家id] [删除装备失败] ["+se1.getArticleName()+"]");
	//								}
	//							}
	//						}
						} catch (Exception e) {
							if(SpecialEquipmentManager.logger.isWarnEnabled()){
								SpecialEquipmentManager.logger.warn("[特殊装备抱魂值操作] ["+se1.getArticleName()+"]",e);
							}
						}
					}
				}
			}
		}
	}
	
	public boolean ishaving(Equipment equipment){
		
		if(equipment.getSpecial() == 鸿天帝宝){
			
			List<Long> list = specialEquipmentBillBoard.getSpecial1Map().get(equipment.getName());
			if(list != null && list.size() != 0){
				SpecialEquipmentManager.logger.warn("[判断是否存在此装备] [已经存在] ["+equipment.getName()+"]");
				return true;
			}else{
				SpecialEquipmentManager.logger.warn("[判断是否存在此装备] [还不存在] ["+equipment.getName()+"]");
			}
		}
		return false;
	}
	
	public static boolean isSpecial(Equipment equipment){
		
		if(equipment.getSpecial() == 普通 ){
			return false;
		}
		return true;
	}

	
	public boolean canFlushBoss(){
		boolean canFlush = false;
		Map<String, List<Long>> map = this.getSpecialEquipmentBillBoard().getSpecial1Map();
		if(map != null){
			for(Entry<String, List<Long>> en : map.entrySet()){
				if(en.getValue() == null || en.getValue().size() ==0){
					canFlush = true;
					break;
				}
			}
		}
		if(!canFlush){
			SpecialEquipmentManager.logger.warn("[时间到刷新boss] [可以刷:"+canFlush+"]");
		}
		return canFlush;
	}
	
	//时间到,生成flushboss的信息  几点刷新 在哪儿刷新
	public void createFlushBossInfo(){
		
		Map<String, List<Long>> map = this.getSpecialEquipmentBillBoard().getSpecial1Map();
		boolean bln = this.canFlushBoss();
		if(bln){
			boolean appear = true;
			String equipmentName = null;
			Random random = new Random();
			while(appear){
				HashMap<String, Integer> equipmentNameMap = this.equipmentMonsterIdMap;
				int index = random.nextInt(equipmentNameMap.size());
				int j = 0;
				for(String st :equipmentNameMap.keySet()){
					if(j == index){
						equipmentName = st;
						break;
					}
					++j;
				}
				
				List<Long> existList = map.get(equipmentName);
				if(existList == null || existList.size() == 0){
					appear = false;
				}
			}
			SpecialEquipmentManager.logger.warn("[时间到刷新boss] [得到装备名:"+equipmentName+"]");
			
			int monsterId = this.equipmentMonsterIdMap.get(equipmentName);
			
			//得到地图名坐标
			int index = random.nextInt(equipmentAppearList.size());
			SpecialEquipmentAppearMap appearMap = equipmentAppearList.get(index);
			if(appearMap != null){
				int randomCountry = random.nextInt(3);
				FlushBossInfo info = new FlushBossInfo(equipmentName,第一次通知生成boss信息的时间, (byte)(randomCountry+1), monsterId,appearMap);
				setFlushBossInfo(info);
				//保存disk
				getDisk().put("bossInfo", info);
				SpecialEquipmentManager.logger.warn("[时间到刷新boss] [生成刷新boos信息] [位置:"+index+"]");
			}else{
				SpecialEquipmentManager.logger.warn("[时间到刷新boss] [得到地图null]");
			}
			
		}else{
//			SpecialEquipmentManager.logger.warn("[时间到刷新boss] [不能刷]");
		}
		
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	public static class SpecialEquipmentAppearMap implements Serializable{
		private static final long serialVersionUID = -3096007923709436361L;
		
		String mapName;
		int x;
		int y;
		public String getMapName() {
			return mapName;
		}
		public void setMapName(String mapName) {
			this.mapName = mapName;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public SpecialEquipmentAppearMap(String mapName, int x, int y) {
			super();
			this.mapName = mapName;
			this.x = x;
			this.y = y;
		}
	}
	
	//野外boss对应关系
	public HashMap<String, Integer> equipmentMonsterIdMap = new HashMap<String, Integer>();
	public List<SpecialEquipmentAppearMap> equipmentAppearList = new ArrayList<SpecialEquipmentAppearMap>();
	
	public void loadExcel() throws Exception{
		
		 File file = new File(fileName);
		 HSSFWorkbook workbook = null;
		
		 InputStream is = new FileInputStream(file);
		 POIFSFileSystem pss = new POIFSFileSystem(is);
		 workbook = new HSSFWorkbook(pss);
		 
		 int equipmentNameIndex = 0;
		 int monsterIdIndex = 1;
		 
		 int mapNameIndex = 0;
		 int mapXIndex = 1;
		 int mapYIndex = 2;
		 
		 
		 int 副本怪物id = 0;
		 int 副本装备名称 = 1;
		 
		 HSSFSheet sheet = null;
		 sheet = workbook.getSheetAt(0);
		 if (sheet == null) throw new RuntimeException("初始化万灵榜装备的装备怪物对应，没有这个配置表");
		 int rows = sheet.getPhysicalNumberOfRows();
		 HSSFRow row = null;
		 HSSFCell cell = null;
		 
		 for(int i= 1;i<rows;i++){
			 row = sheet.getRow(i);
			 if(row != null){
				 cell = row.getCell(equipmentNameIndex);
				 String equipmentName = (cell.getStringCellValue().trim());
				 cell = row.getCell(monsterIdIndex);
				 int monsterId = (int)cell.getNumericCellValue();
				 equipmentMonsterIdMap.put(equipmentName, monsterId);
				 
			 }
		 }

		 sheet = workbook.getSheetAt(1);
		 if (sheet == null) throw new RuntimeException("初始化万灵榜装备怪物掉落的地图对应，没有这个配置表");
		 rows = sheet.getPhysicalNumberOfRows();
		 row = null;
		 cell = null;
		 
		 for(int i= 1;i<rows;i++){
			 row = sheet.getRow(i);
			 if(row != null){
				 cell = row.getCell(mapNameIndex);
				 String mapName = cell.getStringCellValue().trim();
				 cell = row.getCell(mapXIndex);
				 int mapX = (int)cell.getNumericCellValue();
				 cell = row.getCell(mapYIndex);
				 int mapY = (int)cell.getNumericCellValue();

				 SpecialEquipmentAppearMap appearMap = new SpecialEquipmentAppearMap(mapName, mapX, mapY);
				 equipmentAppearList.add(appearMap);
			 }
		 }
		 
		 
		 sheet = workbook.getSheetAt(2);
		 if (sheet == null) throw new RuntimeException("初始化万灵榜装备副本怪物掉落的地图对应，没有这个配置表");
		 rows = sheet.getPhysicalNumberOfRows();
		 row = null;
		 cell = null;
		 
		 for(int i= 1;i<rows;i++){
			 row = sheet.getRow(i);
			 if(row != null){
				
				 cell = row.getCell(副本怪物id);
				 int monsterId = (int)cell.getNumericCellValue();
				 
				 cell = row.getCell(副本装备名称);
				 String equipmentNames = (cell.getStringCellValue().trim());
				 
				 for(Entry<String, Integer> en: equipmentMonsterIdMap.entrySet()){
					 if(en.getValue() == monsterId){
						 throw new RuntimeException("万灵榜配置，副本怪物跟野外boss id一样");
					 }
				 }
				 List<String> list = new ArrayList<String>();
				 for(String st : equipmentNames.split(";")){
					 list.add(st);
				 }
				 downcityEquipmentMap.put(monsterId, list);
			 }
		 }
	}
	public String getDiskName() {
		return diskName;
	}

	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}

	public DiskCache getDisk() {
		return disk;
	}

	public void setDisk(DiskCache disk) {
		this.disk = disk;
	}
	
	//怪物种类id,装备list
	public HashMap<Integer, List<String>> downcityEquipmentMap = new HashMap<Integer, List<String>>();
	
	//是不是指定的怪物，只有指定怪物才会掉落
	public boolean isSpecialMonster(Monster monster){
		boolean isSpecialMonster = false;
		int categoryId = monster.getSpriteCategoryId();
		for(Entry<String, Integer> en : this.equipmentMonsterIdMap.entrySet()){
			if(en.getValue() == categoryId){
				isSpecialMonster =true;
				break;
			}
		}
		
		List<String> list = downcityEquipmentMap.get(categoryId);
		if(list != null && list.size() > 0){
			isSpecialMonster =true;
		}
		return isSpecialMonster;
	}
	
	Random random = new Random();
	public static float 副本掉落万灵榜概率  = 0.0004f;
	
	//指定怪物死亡，产生特殊装备
	public synchronized EquipmentEntity specialMonsterKilledCreateEquipment(Monster monster){
		EquipmentEntity ee = null;
		int categoryId = monster.getSpriteCategoryId();
		if(this.getFlushBossInfo() != null){
			String equipmentName = this.getFlushBossInfo().getEquipmentName();;
			boolean canCreate = false;
			for(Entry<String, Integer> en : this.equipmentMonsterIdMap.entrySet()){
				if(en.getValue() == categoryId && en.getKey().equals(equipmentName)){
					canCreate =true;
					flushBossInfo.bossDie = true;
					flushBossInfo.bossDieTime = SystemTime.currentTimeMillis();
					this.getDisk().put("bossInfo", flushBossInfo);
					
					break;
				}
			}
			
			if(canCreate){
				equipmentName = this.getFlushBossInfo().getEquipmentName();
				Article a = ArticleManager.getInstance().getArticle(equipmentName);
				if(a != null && a instanceof Equipment){
					Equipment e = (Equipment)a;
					ee = this.createEntity(e,ArticleEntityManager.CREATE_REASON_SPRITE_FLOP);
					if(ee != null){
						SpecialEquipmentManager.logger.warn("[野外boss产生万灵榜装备成功] ["+ee.getArticleName()+"] [id:"+ee.getId()+"]");
						return ee;
					}
				}
			}
		}

		List<String> list = downcityEquipmentMap.get(categoryId);
		if(list != null && list.size() > 0){
			
			//cd中
			if(isCD()){
				return null;
			}
			//产生概率
			float randomf = random.nextFloat();
			if(randomf <= 副本掉落万灵榜概率){
				
				int 第几个装备  = random.nextInt(list.size());
				String name = list.get(第几个装备);
				Article a =  ArticleManager.getInstance().getArticle(name);
				if(a != null && a instanceof Equipment){
					Equipment e = (Equipment)a;
					if(!ishaving(e)){
						for(String equipmentName : list){
							a =  ArticleManager.getInstance().getArticle(equipmentName);
							if(a != null && a instanceof Equipment){
								e = (Equipment)a;
								if(!ishaving(e)){
									ee = this.createEntity(e,ArticleEntityManager.CREATE_REASON_SPRITE_FLOP);
									if(ee != null){
										//设置cd
										downcityLastCreateEEtime = SystemTime.currentTimeMillis();
										getDisk().put("eeCreateTime", downcityLastCreateEEtime);
										SpecialEquipmentManager.logger.warn("[副本boss产生万灵榜装备成功] ["+ee.getArticleName()+"] [id:"+ee.getId()+"]");
										return ee;
									}
								}
							}
						}
						SpecialEquipmentManager.logger.warn("[副本boss产生万灵榜装备] [副本掉落的装备都已经产生]");
					}
				}
			}else{
				SpecialEquipmentManager.logger.warn("[副本boss产生万灵榜装备] ["+副本掉落万灵榜概率+"] ["+randomf+"]");
			}
		}
		return null;
	}
	
	private boolean isCD(){
		
		if(downcityLastCreateEEtime > 0 && (SystemTime.currentTimeMillis() - downcityLastCreateEEtime <=  副本cd)){
			SpecialEquipmentManager.logger.warn("[副本boss产生万灵榜装备] [cd中] [上次产生时间] ["+downcityLastCreateEEtime+"]");
			return true;
		}
		return false;
		
	}
	
	
	public void specialMonsterKilled(Monster monster,ArrayList<FlopSchemeEntity.ShareFlopEntity> al, Player[] players,FlopSchemeEntity flopSchemeEntity){
		
		try{
			EquipmentEntity ee = this.specialMonsterKilledCreateEquipment(monster);
			if(ee != null){
				Player owner = monster.getOwner();
				Team team = owner.getTeam();
				FlopSchemeEntity.ShareFlopEntity sfe = new FlopSchemeEntity.ShareFlopEntity(flopSchemeEntity);
				sfe.setEntity(ee);
				sfe.setPickUpFlag(false);
				byte bytes[] = new byte[players.length];
				for (int x = 0; x < players.length; x++) {
					if (team == null) {
						bytes[x] = FlopSchemeEntity.ShareFlopEntity.FREE_PICKUP;
					} else {
						if (team.getAssignRule() == Team.ASSIGN_RULE_BY_FREE) {
							bytes[x] = FlopSchemeEntity.ShareFlopEntity.FREE_PICKUP;
						} else {
							bytes[x] = FlopSchemeEntity.ShareFlopEntity.TEAM_PICKUP;
						}
					}
				}
				sfe.setAssgins(bytes);
				al.add(sfe);
				SpecialEquipmentManager.logger.error("[怪物死亡掉落特殊装备成功] ["+monster.getName()+"] ["+ee.getArticleName()+"] [装备Id:"+ee.getId()+"]");
			}
		}catch(Exception e){
			SpecialEquipmentManager.logger.error("[怪物死亡掉落特殊装备] ["+monster.getName()+"]",e);
		}
		
	}
	
	public int getRankByEquipmentName(String equipmentName){
		
		LinkedHashMap<String, List<Long>> map = this.getSpecialEquipmentBillBoard().getSpecial1Map();
		int i = 1;
		for(Entry<String, List<Long>> en : map.entrySet()){
			if(en.getKey().equals(equipmentName)){
				if(logger.isWarnEnabled()){
					this.logger.warn("[根据名称取排行榜名称] [名次:"+i+"]");
				}
				return i;
			}
			++i;
		}
		this.logger.error("[根据名称取排行榜名称错误] [名次:-1]");
		return -1;
	}
	
	//电视通知
	public static void noticeTelevision(String content){
		ChatMessage msg = new ChatMessage();
		StringBuffer sb = new StringBuffer();
		sb.append("<f color=' ");
		sb.append(ArticleManager.COLOR_PURPLE);
		sb.append("'>");
		sb.append(content);
		sb.append("</f>");
		
		msg.setMessageText(sb.toString());
		try {
			ChatMessageService cms = ChatMessageService.getInstance();
			cms.sendMessageToSystem(msg);
			SpecialEquipmentManager.logger.warn("[发送特殊装备通知成功] ["+content+"]");
		} catch (Exception e) {
			SpecialEquipmentManager.logger.error("[发送特殊装备通知异常] ["+content+"]",e);
		}
		
	}
	
	
	
}