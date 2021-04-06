package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.SeedNpc;

/**
 * 召唤种子NPC道具
 *
 */
public class SeedNPCProps extends Props{

	public static boolean canPlant = true;
	/**
	 * 地图名限制
	 */
	private String gameLimits;
	
	/**
	 * 区域名限制
	 */
	private String mapAreaLimits;

	public String getGameLimits() {
		return gameLimits;
	}

	public void setGameLimits(String gameLimits) {
		this.gameLimits = gameLimits;
	}

	public String getMapAreaLimits() {
		return mapAreaLimits;
	}

	public void setMapAreaLimits(String mapAreaLimits) {
		this.mapAreaLimits = mapAreaLimits;
	}
	private int afterRipeImage;
	
	private String fruitName;

	/**
	 * 种子NPCid
	 */
	private int seedNPCId;
	
	/**
	 * 种子成熟时长单位分钟
	 */
	private int ripeTime;
	
	/**
	 * 种植者采摘时长单位分
	 */
	private int personalPickupLastingTime;

	public String getFruitName() {
		return fruitName;
	}

	public void setFruitName(String fruitName) {
		this.fruitName = fruitName;
	}

	public int getSeedNPCId() {
		return seedNPCId;
	}

	public void setSeedNPCId(int seedNPCId) {
		this.seedNPCId = seedNPCId;
	}

	public int getRipeTime() {
		return ripeTime;
	}

	public void setRipeTime(int ripeTime) {
		this.ripeTime = ripeTime;
	}

	public int getPersonalPickupLastingTime() {
		return personalPickupLastingTime;
	}

	public void setPersonalPickupLastingTime(int personalPickupLastingTime) {
		this.personalPickupLastingTime = personalPickupLastingTime;
	}

	public int getAfterRipeImage() {
		return afterRipeImage;
	}

	public void setAfterRipeImage(int afterRipeImage) {
		this.afterRipeImage = afterRipeImage;
	}

	/**
	 * 摘种子NPC上面的物品
	 * @param player
	 * @param npcId
	 */
//	public static void pickSeedNpc(Player player,long npcId){
//		if(player == null){
//			return;
//		}
//		ArticleManager am = ArticleManager.getInstance();
//		ArticleEntityManager aem = ArticleEntityManager.getInstance();
//		SeedNPCManager snm = SeedNPCManager.getInstance();
//		if(am == null || aem == null){
//			ArticleManager.logger.warn("[采摘种子NPC] [失败] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [物品管理器或物体管理器为空]");
//			return;
//		}
//
//		NPCManager nm = MemoryNPCManager.getNPCManager();
//		NPC npc = nm.getNPC(npcId);
//		if(npc == null){
//			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.translate.text_3806);
//			player.addMessageToRightBag(hreq);
//			ArticleManager.logger.warn("[采摘种子NPC] [失败] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [SeedNpc为空]");
//			return;
//		}
//		if(npc instanceof SeedNpc){
//			SeedNpc seedNpc = (SeedNpc)npc;
//			String fruit = seedNpc.getFruitName();
//			if(fruit == null || fruit.trim().equals("")){
//				fruit = Translate.translate.text_3807;
//			}
//			Article a = am.getArticle(fruit);
//			if(a == null){
//				ArticleManager.logger.warn("[采摘种子NPC] [失败] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [没有设置摘的物品] [fruitName="+fruit+"] [seedNpc] [plantPlayerId:"+seedNpc.getPlantPlayerId()+"] [plantPlayerLevel:"+seedNpc.getPlantPlayerLevel()+"] [seedNpcName:"+seedNpc.getName()+"]");
//				return;
//			}
//			if(player.getId() != seedNpc.getPlantPlayerId()){
//				if(player.getLevel() > 30 && seedNpc.getPlantPlayerLevel() <= 30){
//					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.translate.text_3808+player.getLevel()+Translate.translate.text_3809);
//					player.addMessageToRightBag(hreq);
//					ArticleManager.logger.warn("[采摘种子NPC] [失败] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [您不能采摘30级以下的人种植的植物] [player.getLevel():"+player.getLevel()+"] [seedNpc.getPlantPlayerLevel():"+seedNpc.getPlantPlayerLevel()+"] [seedNpc] [plantPlayerId:"+seedNpc.getPlantPlayerId()+"] [plantPlayerLevel:"+seedNpc.getPlantPlayerLevel()+"] [seedNpcName:"+seedNpc.getName()+"]");
//					return;
//				}
//				if(player.getLevel() <= 30 && seedNpc.getPlantPlayerLevel() > 30){
//					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.translate.text_3808+player.getLevel()+Translate.translate.text_3810);
//					player.addMessageToRightBag(hreq);
//					ArticleManager.logger.warn("[采摘种子NPC] [失败] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [您不能采摘31级以上的人种植的植物] [player.getLevel():"+player.getLevel()+"] [seedNpc.getPlantPlayerLevel():"+seedNpc.getPlantPlayerLevel()+"] [seedNpc] [plantPlayerId:"+seedNpc.getPlantPlayerId()+"] [plantPlayerLevel:"+seedNpc.getPlantPlayerLevel()+"] [seedNpcName:"+seedNpc.getName()+"]");
//					return;
//				}
//			}
//			if(seedNpc.getCurrentState() == SeedNpc.UN_RIPE_STATE){
//				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.translate.text_3811);
//				player.addMessageToRightBag(hreq);
//				ArticleManager.logger.warn("[采摘种子NPC] [失败] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [还没成熟呢] [seedNpc] [plantPlayerId:"+seedNpc.getPlantPlayerId()+"] [plantPlayerLevel:"+seedNpc.getPlantPlayerLevel()+"] [seedNpcName:"+seedNpc.getName()+"]");
//				return;
//			}else if(seedNpc.getCurrentState() == SeedNpc.RIPE_STATE_AND_PLANTPLAYER_ONLY){
//				
//				boolean ok = false;
//				if(player.getId() == ((SeedNpc)npc).getPlantPlayerId() || player.getConnubialId() == ((SeedNpc)npc).getPlantPlayerId()){
//					ok = true;
//				}else{
//					PlayerManager pm = PlayerManager.getInstance();
//					long lastTime = ((SeedNpc)npc).getRipeTime() + ((SeedNpc)npc).getPersonalPickupLastingTime() - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//					if(pm != null){
//						try {
//							Player plant = pm.getPlayer(((SeedNpc)npc).getPlantPlayerId());
//							if(plant != null){
//								/**
//								 * 帮众 对 帮众 ： 90分钟
//
//									帮众 对 帮主 ： 90分钟
//									
//									种植者的好友 ：120分钟
//									
//									帮主 对 帮众 ： 90分钟
//									
//									徒弟 对 师父 ： 30分钟
//									
//									师父 对 徒弟 ： 20分钟
//									
//									夫妻 之间 ： 0分钟
//								 */
//								long gangTogang = 90*60*1000;
//								long gangLeaderTogang = 90*60*1000;
//								long plantsfriends = 120*60*1000;
//								long stuToteacher = 30*60*1000;
//								long teacherTostu = 20*60*1000;
//								boolean exist = false;
//								if(player.getMastersAndPrentices() != null && player.getMastersAndPrentices().size() != 0){
//									if(player.getLevel() < 40){
//										for(String idStr : player.getMastersAndPrentices()){
//											if(idStr != null && idStr.equals((""+plant.getId()))){
//												lastTime = ((SeedNpc)npc).getRipeTime() + stuToteacher - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//												exist = true;
//												break;
//											}
//										}
//									}else{
//										for(String idStr : player.getMastersAndPrentices()){
//											if(idStr != null && idStr.equals((""+plant.getId()))){
//												lastTime = ((SeedNpc)npc).getRipeTime() + teacherTostu - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//												exist = true;
//												break;
//											}
//										}
//									}
//								}
//								if(!exist){
//									if(player.getGangTitle() == GangTitle.BANGZHU){
//										if(player.getGangName() != null && !player.getGangName().equals("") && player.getGangName().equals(plant.getGangName())){
//											lastTime = ((SeedNpc)npc).getRipeTime() + gangLeaderTogang - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//											exist = true;
//										}
//									}
//								}
//								if(!exist){
//									if(plant.getFriendlist() != null && plant.getFriendlist().size() != 0){
//										for(Long friendId : plant.getFriendlist()){
//											if(friendId == player.getId()){
//												lastTime = ((SeedNpc)npc).getRipeTime() + plantsfriends - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//												exist = true;
//												break;
//											}
//										}
//									}
//								}
//								if(!exist){
//									if(player.getGangName() != null && !player.getGangName().equals("") && player.getGangName().equals(plant.getGangName())){
//										lastTime = ((SeedNpc)npc).getRipeTime() + gangTogang - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//										exist = true;
//									}
//								}
//							}
//						}catch(Exception ex){
//							ex.printStackTrace();
//						}
//					}
//					if(lastTime <= 0){
//						ok = true;
//					}
//				}
//				if(ok){
//					ArticleEntity ae = null;
//					int reason = ArticleEntityManager.CREATE_REASON_SEED_NPC_PICK;
//
//					boolean bind = false;
//					if(a.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP || a.getBindStyle() == Article.BINDTYPE_PICKUP){
//						bind = true;
//					}
//					if(a instanceof Equipment){
//						ae = aem.createEntity((Equipment)a, bind,reason,player);
//					}else if(a instanceof Props){
//						ae = aem.createEntity((Props)a, bind,reason,player);
//					}else{
//						ae = aem.createEntity(a, bind,reason,player);
//					}
//					if(player.getKnapsack() != null && ae != null){
//						if(player.getKnapsack().put(ae)){
//							if(ArticleManager.logger.isInfoEnabled()){
//								ArticleManager.logger.info("[采摘种子NPC] [成功] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [得到物品:"+ae.getArticleName()+"] ["+ae.getId()+"] [seedNpc] [plantPlayerId:"+seedNpc.getPlantPlayerId()+"] [plantPlayerLevel:"+seedNpc.getPlantPlayerLevel()+"] [seedNpcName:"+seedNpc.getName()+"]");
//							}
//							player.send_HINT_REQ(Translate.translate.text_3812, ae);
//							//如果不是本人摘得花，那么给种植者发邮件
//							if(player.getId() != ((SeedNpc)npc).getPlantPlayerId()){
//								MailManager mm = MailManager.getInstance();
//								try {
//									if(mm != null){
//										mm.sendMail(((SeedNpc)npc).getPlantPlayerId(), null, Translate.translate.text_3813+player.getName()+Translate.translate.text_3814, Translate.translate.text_3813+player.getName()+Translate.translate.text_3815, 0, 0, 0);	
//									}
//								} catch (Exception e) {
//									e.printStackTrace();
//									ArticleManager.logger.error("采摘种子发信异常 e:"+StringUtil.getStackTrace(e));
//								}
//							}
//							npc.setAlive(false);
//							nm.removeNPC(npc);
//							
//							JiaYuanManager jym = JiaYuanManager.getInstance();
//							if(jym.isJiaYuanMapName(player.getGame())){
//								jym.notifySeedNpcPlaceOrPickUp(player.getCurrentGame());
//							}else{
//								//从种子NPC管理器中把种子去除
//								if(snm != null && snm.npcMap != null && snm.npcMap.keySet() != null){
//									try{
//										boolean exist = false;
//										Object o = null;
//										for(Long intKey : snm.npcMap.keySet()){
//											if(npc.getId() == intKey){
//												o = intKey;
//												exist = true;
//												break;
//											}
//										}
//										if(exist){
//											snm.npcMap.remove(o);
//											snm.write();
//										}
//									}catch(Exception ex){
//										ArticleManager.logger.warn(StringUtil.getStackTrace(ex));
//									}
//								}
//							}
//							
//
//							//发送统计日志
//							int sortint[] = ArticleEntityManager.getCategoryOfArticle(a);
//							String sort = Translate.translate.text_1736;
//							String subsort = Translate.translate.text_1736;
//							try {
//								sort = ArticleEntityManager.物品一级分类[sortint[0]];
//								subsort = ArticleEntityManager.物品二级分类[sortint[0]][sortint[1]];
//							} catch(Exception e) {}
//							int alevel = a.getArticleLevel();
//							String color = ArticleEntityManager.getColorOfArticle(a);
//							String material = ArticleEntityManager.getMaterialOfArticle(a);
//							ArticleBornFlow flow = new ArticleBornFlow();
//							flow.setArticlename(a.getName());
//							flow.setBornreason(ArticleEntityManager.CREATE_REASON_DESPS[ArticleEntityManager.CREATE_REASON_SEED_NPC_PICK]);
//							flow.setLevel(alevel);
//							flow.setSort(sort);
//							flow.setSubsort(subsort);
//							flow.setCount(1);
//							flow.setColor(color);
//							flow.setMaterial(material);
//							flow.setPlayername(player.getName());
//							flow.setUsername(player.getUsername());
//							flow.setCareer(CareerManager.careerNames[player.getCareer()]);
//							flow.setCreatetime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
//							String map = player.getGame();
//							if(map == null || map.length() == 0) {
//								map = player.getLastGame();
//								if(map == null || map.length() == 0) {
//									map = Translate.translate.text_1211;
//								}
//							}
//							flow.setMap(map);
//							flow.setNowlevel(player.getLevel());
//							flow.setPolcamp(Player.POLITICALCAMP[player.getPoliticalCamp()]);
//							flow.setServer(GameConstants.getInstance().getServerName());
//							StatClientService.getInstance().sendArticleBornFlow(flow);
//						
//						}else{
//							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.translate.text_3816);
//							player.addMessageToRightBag(hreq);
//							ArticleManager.logger.warn("[采摘种子NPC] [失败] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [背包已满请清理后再采] [seedNpc] [plantPlayerId:"+seedNpc.getPlantPlayerId()+"] [plantPlayerLevel:"+seedNpc.getPlantPlayerLevel()+"] [seedNpcName:"+seedNpc.getName()+"]");
//						}
//					}
//
//				}else{
//					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.translate.text_3817);
//					player.addMessageToRightBag(hreq);
//					ArticleManager.logger.warn("[采摘种子NPC] [失败] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [你还不能采摘] [seedNpc] [plantPlayerId:"+seedNpc.getPlantPlayerId()+"] [plantPlayerLevel:"+seedNpc.getPlantPlayerLevel()+"] [seedNpcName:"+seedNpc.getName()+"]");
//					return;
//				}
//			}else if(seedNpc.getCurrentState() == SeedNpc.RIPE_STATE_AND_ALLPLAYER_CAN){
//				ArticleEntity ae = null;
//				int reason = ArticleEntityManager.CREATE_REASON_SEED_NPC_PICK;
//
//				boolean bind = false;
//				if(a.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP || a.getBindStyle() == Article.BINDTYPE_PICKUP){
//					bind = true;
//				}
//				if(a instanceof Equipment){
//					ae = aem.createEntity((Equipment)a, bind,reason,player);
//				}else if(a instanceof Props){
//					ae = aem.createEntity((Props)a, bind,reason,player);
//				}else{
//					ae = aem.createEntity(a, bind,reason,player);
//				}
//				if(player.getKnapsack() != null && ae != null){
//					if(player.getKnapsack().put(ae)){
//						if(ArticleManager.logger.isInfoEnabled()){
//							ArticleManager.logger.info("[采摘种子NPC] [成功] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [得到物品:"+ae.getArticleName()+"] ["+ae.getId()+"] [seedNpc] [plantPlayerId:"+seedNpc.getPlantPlayerId()+"] [plantPlayerLevel:"+seedNpc.getPlantPlayerLevel()+"] [seedNpcName:"+seedNpc.getName()+"]");
//						}
//						player.send_HINT_REQ(Translate.translate.text_3812, ae);
//						//如果不是本人摘得花，那么给种植者发邮件
//						if(player.getId() != ((SeedNpc)npc).getPlantPlayerId()){
//							MailManager mm = MailManager.getInstance();
//							try {
//								if(mm != null){
//									mm.sendMail(((SeedNpc)npc).getPlantPlayerId(), null, Translate.translate.text_3813+player.getName()+Translate.translate.text_3814, Translate.translate.text_3813+player.getName()+Translate.translate.text_3815, 0, 0, 0);	
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//								ArticleManager.logger.error("采摘种子发信异常 e:"+StringUtil.getStackTrace(e));
//							}
//						}
//						npc.setAlive(false);
//						nm.removeNPC(npc);
//						
//						JiaYuanManager jym = JiaYuanManager.getInstance();
//						if(jym.isJiaYuanMapName(player.getGame())){
//							jym.notifySeedNpcPlaceOrPickUp(player.getCurrentGame());
//						}else{
//							//从种子NPC管理器中把种子去除
//							if(snm != null && snm.npcMap != null && snm.npcMap.keySet() != null){
//								try{
//									boolean exist = false;
//									Object o = null;
//									for(Long intKey : snm.npcMap.keySet()){
//										if(npc.getId() == intKey){
//											o = intKey;
//											exist = true;
//											break;
//										}
//									}
//									if(exist){
//										snm.npcMap.remove(o);
//										snm.write();
//									}
//								}catch(Exception ex){
//									ArticleManager.logger.warn(StringUtil.getStackTrace(ex));
//								}
//							}
//						}
//
//						//发送统计日志
//						int sortint[] = ArticleEntityManager.getCategoryOfArticle(a);
//						String sort = Translate.translate.text_1736;
//						String subsort = Translate.translate.text_1736;
//						try {
//							sort = ArticleEntityManager.物品一级分类[sortint[0]];
//							subsort = ArticleEntityManager.物品二级分类[sortint[0]][sortint[1]];
//						} catch(Exception e) {}
//						int alevel = a.getArticleLevel();
//						String color = ArticleEntityManager.getColorOfArticle(a);
//						String material = ArticleEntityManager.getMaterialOfArticle(a);
//						ArticleBornFlow flow = new ArticleBornFlow();
//						flow.setArticlename(a.getName());
//						flow.setBornreason(ArticleEntityManager.CREATE_REASON_DESPS[ArticleEntityManager.CREATE_REASON_SEED_NPC_PICK]);
//						flow.setLevel(alevel);
//						flow.setSort(sort);
//						flow.setSubsort(subsort);
//						flow.setCount(1);
//						flow.setColor(color);
//						flow.setMaterial(material);
//						flow.setPlayername(player.getName());
//						flow.setUsername(player.getUsername());
//						flow.setCareer(CareerManager.careerNames[player.getCareer()]);
//						flow.setCreatetime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
//						String map = player.getGame();
//						if(map == null || map.length() == 0) {
//							map = player.getLastGame();
//							if(map == null || map.length() == 0) {
//								map = Translate.translate.text_1211;
//							}
//						}
//						flow.setMap(map);
//						flow.setNowlevel(player.getLevel());
//						flow.setPolcamp(Player.POLITICALCAMP[player.getPoliticalCamp()]);
//						flow.setServer(GameConstants.getInstance().getServerName());
//						StatClientService.getInstance().sendArticleBornFlow(flow);
//					
//					}else{
//						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.translate.text_3816);
//						player.addMessageToRightBag(hreq);
//						ArticleManager.logger.warn("[采摘种子NPC] [失败] [user:"+player.getUsername()+"] [playerId:"+player.getId()+"] ["+player.getName()+"] [背包已满请清理后再采] [seedNpc] [plantPlayerId:"+seedNpc.getPlantPlayerId()+"] [plantPlayerLevel:"+seedNpc.getPlantPlayerLevel()+"] [seedNpcName:"+seedNpc.getName()+"]");
//					}
//				}
//			}
//		}
//	}

	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity ae){
//		if(!super.use(game,player,ae)){
//			return false;
//		}
//
//		if(game != null && game.getGameInfo() != null){
//			NPCManager nm = MemoryNPCManager.getNPCManager();
//			SeedNPCManager snm = SeedNPCManager.getInstance();
//			if(nm != null && snm != null){
//				//事先定好的id
//				NPC npc = ((MemoryNPCManager)nm).createNPC(seedNPCId);
//				if(npc instanceof SeedNpc){
//					npc.setX(player.getX());
//					npc.setY(player.getY());
//					npc.setAlive(true);
//					npc.setGameName(game.getGameInfo().getName());
//					((SeedNpc)npc).setPlantPlayerId(player.getId());
//					((SeedNpc)npc).setPlantPlayerLevel(player.getLevel());
//					((SeedNpc)npc).setPlantTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
//					//成熟时间点
//					((SeedNpc)npc).setRipeTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+ripeTime*60000);
//					((SeedNpc)npc).setPersonalPickupLastingTime(personalPickupLastingTime*60000);
//					((SeedNpc)npc).setAfterRipeImage(afterRipeImage);
//					((SeedNpc)npc).setFruitName(fruitName);
//					npc.setName(player.getName()+Translate.translate.text_3818);
//
//					try {
//						JiaYuanManager jym = JiaYuanManager.getInstance();
//						if(jym.isJiaYuanMapName(game.getGameInfo().getMapName())){
//							game.addSprite(npc);
//							jym.notifySeedNpcPlaceOrPickUp(game);
//						}else{
//							if(snm.npcMap == null){
//								HashMap<Long,SeedNpc> npcMap = new HashMap<Long,SeedNpc>();
//								snm.setNpcMap(npcMap);
//							}
//							game.addSprite(npc);
//							snm.npcMap.put(npc.getId(), (SeedNpc)npc);
//							snm.write();
//						}
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						if(ArticleManager.logger.isDebugEnabled()){
//							ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败] [保存出现异常] [user]["+player.getUsername()+"]["+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"]");
//						}
//						return false;
//					}
//					if(ArticleManager.logger.isDebugEnabled()){
//						ArticleManager.logger.debug("[使用道具] [NPC召唤] [成功] [位置:"+game.getGameInfo().getName()+"("+player.getX()+","+player.getY()+")"+"][user]["+player.getUsername()+"]["+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"]");
//					}
//					//统计
//					Player.sendPlayerAction(player, PlayerActionFlow.行为类型_种植, this.getName(), 0, new Date(), true);
//				}else{
//					if(ArticleManager.logger.isDebugEnabled()){
//						ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败] [NPC不是SeedNpc] [user]["+player.getUsername()+"]["+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"]");
//					}
//					return false;
//				}
//			}else{
//				if(ArticleManager.logger.isDebugEnabled()){
//					ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败][NPCManager或SeedNPCManager为空] [user]["+player.getUsername()+"]["+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"]");
//				}
//				return false;
//			}
//		}else{
//			if(ArticleManager.logger.isDebugEnabled()){
//				ArticleManager.logger.debug("[使用道具] [NPC召唤] [失败][game为空或gameInfo为空] [user]["+player.getUsername()+"]["+player.getId()+"]["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"]");
//			}
//			return false;
//		}
		return true;
	}
	@Override
	public String canUse(Player p) {
		// TODO Auto-generated method stub
		if(!canPlant){
			return Translate.text_3819;
		}
		String resultStr = super.canUse(p);
		if(resultStr == null){
			float x = p.getX();
			float y = p.getY();
			int fanwei = 36;
			Game game = p.getCurrentGame();
			if(game != null){
				//种子npc的种植限制
				if(gameLimits != null && !gameLimits.trim().equals("")){
					String[] gLimits = gameLimits.split(",");
					if(gLimits != null && gLimits.length == 1){
						gLimits = gameLimits.split("，");
					}
					
					if(gLimits != null){
						boolean ok = false;
						for(String gLimit : gLimits){
							if(gLimit != null && gLimit.trim().equals(game.gi.getMapName())){
								ok = true;
								break;
							}
						}
						if(ok){
							if(mapAreaLimits != null && !mapAreaLimits.trim().equals("")){
								String[] areaLimits = mapAreaLimits.split(",");
								if(areaLimits != null && areaLimits.length == 1){
									areaLimits = mapAreaLimits.split("，");
								}
								if(areaLimits != null){
									boolean yes = false;
									MapArea mapArea = game.gi.getMapAreaByPoint(x, y);
									for(String area : areaLimits){
										if(area != null && mapArea != null && area.trim().equals(mapArea.name)){
											yes = true;
											break;
										}
									}
									if(!yes){
										resultStr = Translate.text_3756+this.name+Translate.text_3820+mapAreaLimits+Translate.text_3821;
									}
								}
							}
						}else{
							resultStr = Translate.text_3756+this.name+Translate.text_3822+gameLimits+Translate.text_3823;
						}
					}
					
				}

				if(resultStr != null){
					return resultStr;
				}

				boolean notFar = false;
				LivingObject[] livingObjects = game.getLivingObjects();
				if(livingObjects != null){
					for(LivingObject livingObject : livingObjects){
						if(livingObject instanceof SeedNpc){
							if(((SeedNpc)livingObject).getPlantPlayerId() == p.getId()){
								notFar = false;
								resultStr = Translate.text_3824;
								break;
							}
							if((livingObject.getX()- x) < fanwei && (livingObject.getX()- x) > -fanwei && (livingObject.getY()- y) < fanwei && (livingObject.getY()- y) > -fanwei){
								notFar = true;
								break;
							}
						}
					}
				}
				if(notFar){
					resultStr = Translate.text_3825;
				}
			}else{
				resultStr = Translate.text_3826+name;
			}
		}
		return resultStr;
	}
}
