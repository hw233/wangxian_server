package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.carbon.devilSquare.DevilSquareConstant;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ProbabilityUtils;

/**
 * 特殊道具：随机类包裹
 * 包裹中有一些物品，打开包裹可以随机得到物品，物品数量随机。
 * 
 */
public class RandomPackageProps extends Props implements Gem {

	static Random rd = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	public static Logger logger = LoggerFactory.getLogger(RandomPackageProps.class);
	/**
	 * 包裹中的物品名字以及掉落几率数组
	 */
	private ArticleProperty[] apps;
	private ArticleProperty[] apps_stat;

	private int colorType;

	/**
	 * 0代表绑定，1代表不绑定
	 */
	private byte openBindType;
	/**
	 * 如果开出的物品为sendMessageArticles中的物品，那么就系统广播
	 * sendMessageArticles可以为多个物品，用逗号分隔
	 */
	private String sendMessageArticles;

	private String sendMessageArticles_stat;
	/**
	 * 开启宝箱消耗
	 * @return
	 */
	private String costName;
	private String costName_stat;

	private int costNum;

	private int costColor;

	/**
	 * 开启宝箱播放粒子及广播
	 */
	private String particleName;
	private long delayTime;
	private double upValue;
	private double leftValue;
	private double frontValue;
	private byte canseeType;
	private String broadcast = "";

	public String getParticleName() {
		return particleName;
	}

	public void setParticleName(String particleName) {
		this.particleName = particleName;
	}

	public long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

	public double getUpValue() {
		return upValue;
	}

	public void setUpValue(double upValue) {
		this.upValue = upValue;
	}

	public double getLeftValue() {
		return leftValue;
	}

	public void setLeftValue(double leftValue) {
		this.leftValue = leftValue;
	}

	public double getFrontValue() {
		return frontValue;
	}

	public void setFrontValue(double frontValue) {
		this.frontValue = frontValue;
	}

	public byte getCanseeType() {
		return canseeType;
	}

	public void setCanseeType(byte canseeType) {
		this.canseeType = canseeType;
	}

	public String getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(String broadcast) {
		this.broadcast = broadcast;
	}

	public String getCostName() {
		return costName;
	}

	public ArticleProperty[] getApps_stat() {
		return apps_stat;
	}

	public void setApps_stat(ArticleProperty[] apps_stat) {
		this.apps_stat = apps_stat;
	}

	public String getCostName_stat() {
		return costName_stat;
	}

	public void setCostName_stat(String costName_stat) {
		this.costName_stat = costName_stat;
	}

	public String getSendMessageArticles_stat() {
		return sendMessageArticles_stat;
	}

	public void setSendMessageArticles_stat(String sendMessageArticles_stat) {
		this.sendMessageArticles_stat = sendMessageArticles_stat;
	}

	public void setCostName(String costName) {
		this.costName = costName;
	}

	public int getCostNum() {
		return costNum;
	}

	public void setCostNum(int costNum) {
		this.costNum = costNum;
	}

	public int getCostColor() {
		return costColor;
	}

	public void setCostColor(int costColor) {
		this.costColor = costColor;
	}

	public String getSendMessageArticles() {
		return sendMessageArticles;
	}

	public void setSendMessageArticles(String sendMessageArticles) {
		this.sendMessageArticles = sendMessageArticles;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public byte getOpenBindType() {
		return openBindType;
	}

	public void setOpenBindType(byte openBindType) {
		this.openBindType = openBindType;
	}

	public ArticleProperty[] getApps() {
		return apps;
	}

	public void setApps(ArticleProperty[] apps) {
		this.apps = apps;
	}

	/**
	 * 打开包裹方法(重写父类方法)
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity aee) {
		if (!super.use(game, player, aee)) {
			return false;
		}
		// 消耗品判断
		int isremove = 0;
		boolean removeflag = false;
		if (costName != null && !"".equals(costName)) {
			isremove = 1;
			if (!player.articleIsExist(costName)) {
				player.sendError(Translate.translateString(Translate.开启宝箱提示, new String[][] { { Translate.STRING_1, costName } }));
				return false;
			}
			if (player.getKnapsack_common().countArticle(costName) < costNum) {
				player.sendError("需要消耗的物品数量不足！");
				return false;
			}
		}

		// 播放粒子效果
		if (particleName != null) {
			ParticleData[] particleDatas = new ParticleData[1];
			particleDatas[0] = new ParticleData(player.getId(), particleName, delayTime, upValue, leftValue, frontValue);
			NOTICE_CLIENT_PLAY_PARTICLE_RES client_PLAY_PARTICLE_RES = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
			// TODO 判断粒子要给哪些人看
			String sendBroadcast="";
			if (canseeType == 0) {
				player.addMessageToRightBag(client_PLAY_PARTICLE_RES);
				ArticleManager.logger.warn("[" + player.getLogString() + "[使用了" + aee.getArticleName() + "] [播放粒子效果]");
			} else if (canseeType == 1) {
				LivingObject[] livingObjs = player.getCurrentGame().getLivingObjects();
				try {
					sendBroadcast = Translate.translateString(Translate.某人使用某物品, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, aee.getArticleName() } })+broadcast;
					ChatMessageService.getInstance().sendMessageToSystem(sendBroadcast);
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (LivingObject lo : livingObjs) {
					if (lo instanceof Player) {
						Player p = (Player) lo;
						p.addMessageToRightBag(client_PLAY_PARTICLE_RES);
						if(p.getId()==player.getId()){
							ArticleManager.logger.warn("[" + player.getLogString() + "[使用了" + aee.getArticleName() + "] [播放粒子效果] [广播:"+sendBroadcast+"]");
						}
					}
				}
			} else if (canseeType == 2) {
				Player[] onlinePlayers = GamePlayerManager.getInstance().getOnlinePlayers();
				try {
					sendBroadcast = Translate.translateString(Translate.某人使用某物品, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, aee.getArticleName() } })+broadcast;
					ChatMessageService.getInstance().sendMessageToSystem(sendBroadcast);
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (Player p : onlinePlayers) {
					p.addMessageToRightBag(client_PLAY_PARTICLE_RES);
					if(p.getId()==player.getId()){
						ArticleManager.logger.warn("[" + player.getLogString() + "[使用了" + aee.getArticleName() + "] [播放粒子效果] [广播:"+sendBroadcast+"]");
					}
				}
			}
		} 

		// 往玩家背包中放物品
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (am != null && aem != null && apps != null && apps.length != 0) {
			double[] probabilitys = new double[apps.length];
			double countValue = 0;
			for (int i = 0; i < apps.length; i++) {
				if (apps[i] != null) {
					countValue += apps[i].prob;
				}
			}
			for (int i = 0; i < apps.length; i++) {
				if (apps[i] != null) {
					probabilitys[i] = apps[i].prob * 1.0 / countValue;
				}
			}
			int index = ProbabilityUtils.randomProbability(player.random, probabilitys);
			ArticleProperty appTemp = apps[index];
			if (appTemp != null) {
				String s = appTemp.getArticleName_stat();
				if (s != null) {
					int maxOutNums = ActivityManagers.getInstance().getMaxOutNumsByArticleName(this.getName_stat(), s);
					if(maxOutNums > 0){
						int hasOutNums = ActivityManagers.getInstance().getHasOutNumsByArticleName(s);
						if(hasOutNums >= maxOutNums){
							s = ActivityManagers.LAST_ARTICLE_NAME;
							ArticleManager.logger.warn("[使用礼包] [有产量限制] [礼包:{}] [开出道具:{}] [道具替换:{}] [已经产出:{}]",new Object[]{s,this.getName(),s,maxOutNums,hasOutNums});
						}else{
							ArticleManager.logger.warn("[使用礼包] [有产量限制] [礼包:{}] [开出道具:{}] [限制数量:{}] [已经产出:{}]",new Object[]{this.getName(),s,maxOutNums,hasOutNums});
						}
					}
					Article a = am.getArticleByCNname(s);
					if (a != null) {
						ArticleEntity ae = null;
						int reason = ArticleEntityManager.CREATE_REASON_USE_PACKAGEPROPS;
						boolean bind = false;
						if (a.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP || a.getBindStyle() == Article.BINDTYPE_PICKUP) {
							bind = true;
						}
						if (openBindType == 0) {
							bind = true;
						}
						if (a.isOverlap()) {
							try {
								ae = aem.createEntity(a, bind, reason, player, appTemp.color, appTemp.count, true);
								for (int i = 0; i < appTemp.count; i++) {
									if (!player.putToKnapsacks(ae, "随机包裹")) {
										MailManager mm = MailManager.getInstance();
										if (mm != null) {
											ArticleEntity[] entities = new ArticleEntity[] { ae };
											int[] counts = new int[] { 1 };
											String title = Translate.系统邮件提示;
											String content = Translate.由于您的背包已满您得到的部分物品通过邮件发送;
											try {
												mm.sendMail(player.getId(), entities, counts, title, content, 0, 0, 0, "使用" + this.getName());
												if (isremove == 1) {
													removeflag = true;
												}
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, content);
												player.addMessageToRightBag(hreq);
												if (logger.isWarnEnabled()) logger.warn("[邮件] [成功] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
											} catch (Exception e) {
												e.printStackTrace();
												if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[邮件] [异常] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() }, e);
												if (logger.isWarnEnabled()) logger.warn("[邮件] [异常] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() }, e);
											}
										}
									} else {
										if (isremove == 1) {
											removeflag = true;
										}
									}
								}
								if (removeflag) {
									player.removeArticle(costName, "开启钥匙消耗诸天宝箱");
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else {
							for (int i = 0; i < appTemp.count; i++) {

								try {
									ae = aem.createEntity(a, bind, reason, player, appTemp.color, 1, true);
									if (!player.putToKnapsacks(ae, "随机包裹")) {
										MailManager mm = MailManager.getInstance();
										if (mm != null) {
											ArticleEntity[] entities = new ArticleEntity[] { ae };
											int[] counts = new int[] { 1 };
											String title = Translate.系统邮件提示;
											String content = Translate.由于您的背包已满您得到的部分物品通过邮件发送;
											try {
												mm.sendMail(player.getId(), entities, counts, title, content, 0, 0, 0, "使用" + this.getName());
												if (isremove == 1) {
													removeflag = true;
												}
												HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, content);
												player.addMessageToRightBag(hreq);
												if (logger.isWarnEnabled()) logger.warn("[邮件] [成功] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() });
											} catch (Exception e) {
												e.printStackTrace();
												if (MailManager.logger.isWarnEnabled()) MailManager.logger.warn("[邮件] [异常] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() }, e);
												if (logger.isWarnEnabled()) logger.warn("[邮件] [异常] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName() }, e);
											}
										}
									} else {
										if (isremove == 1) {
											removeflag = true;
										}
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
							if (removeflag) {
								player.removeArticle(costName, "开启钥匙消耗诸天宝箱");
							}
						}

						if (ae != null) {
							if(maxOutNums > 0){
								ActivityManagers.getInstance().updateArticleOutNum(s, appTemp.count);	
							}
							// 统计
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, appTemp.count, "随机礼包获得", null);
							String showName = ae.getArticleName();
							if(a instanceof InlayArticle){
								showName = ((InlayArticle)a).getShowName();
							}
							
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您得到了物品, new String[][] { { Translate.STRING_1, showName }, { Translate.COUNT_1, appTemp.count + "" } }));
							player.addMessageToRightBag(hreq);
							if (sendMessageArticles != null && sendMessageArticles.length() > 0) {
								String[] names = sendMessageArticles.split(",");
								if (names != null && names.length == 1) {
									names = sendMessageArticles.split("，");
								}
								boolean flag = false;
								for(String str1 : DevilSquareConstant.boxarticlename) {
									if(str1.equals(this.getName_stat())) {
										flag = true;
										break;
									}
								}
								if(names != null){
									for(String str : names){
										if(str != null && str.trim().equals(ae.getArticleName())){
											try {
												if(!flag) {
													ChatMessageService cm = ChatMessageService.getInstance();
													ChatMessage msg = new ChatMessage();
													msg.setSort(ChatChannelType.SYSTEM);
													String des = Translate.translateString(Translate.恭喜玩家幸运开出物品, new String[][]{{Translate.PLAYER_NAME_1,player.getName()},{Translate.STRING_1,this.name},{Translate.STRING_2,str}});
													msg.setMessageText(des);
													cm.sendMessageToSystem(msg);
												} else {
													DevilSquareManager.instance.notifyPlayerGetProp(player, ae, this);
												}
												if(logger.isWarnEnabled())
													logger.warn("[系统公告] [随机宝箱] [幸运开出大奖] [user] ["+player.getUsername()+"] [playerid:"+player.getId()+"] ["+player.getName()+"] [恭喜玩家"+player.getName()+"在"+this.getName()+"内幸运的开出了"+str+"] [ae:"+ae.getId()+"][" + flag + "]");
											} catch (Exception e1) {
												e1.printStackTrace();
											}
											break;
										}
									}
								}
							}
						}
					}
				}

			}

		}
		try {
			ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
			strongMaterialEntitys.add(aee);
			ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[使用赠送活动] [使用11随机礼包] [" + player.getLogString() + "]", e);
		}
		
		return true;
	}

	public String getComment() {
		StringBuffer sb = new StringBuffer();
		// sb.append("随机获得下列中的一件:\n");
		// if(money != 0){
		// sb.append("游戏币,\n");
		// }
		// if(bindyuanbao != 0){
		// sb.append("绑定元宝,\n");
		// }
		// if(rmbyuanbao != 0){
		// sb.append("人民币元宝,\n");
		// }
		// if(apps != null){
		// for(ArticleProperty app : apps){
		// if(app != null && app.getArticleName() != null){
		// sb.append(app.getArticleName()+",\n");
		// }
		// }
		// }
		// if(sb.indexOf(",") > 0){
		// return sb.substring(0, sb.lastIndexOf(","));
		// }
		return sb.toString();
	}
}
