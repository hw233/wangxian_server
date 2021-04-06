package com.fy.engineserver.datasource.article.data.entity;

import java.io.Serializable;

import com.fy.engineserver.articleEnchant.EnchantManager;
import com.fy.engineserver.articleEnchant.model.EnchantArticle;
import com.fy.engineserver.articleEnchant.model.EnchantModel;
import com.fy.engineserver.articleProtect.ArticleProtectData;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.articleProtect.PlayerArticleProtectData;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.articles.PetFoodArticle;
import com.fy.engineserver.datasource.article.data.props.BrightInlayProps;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.time.Timer;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 物品实体
 * 
 */
@SimpleEntity
@SimpleIndices( { @SimpleIndex(name = "ArticleEntity_ANAME", members = { "articleName" }, compress = 1), @SimpleIndex(name = "ArticleEntity_cType", members = { "colorType" }, compress = 1), @SimpleIndex(members = { "status" }, compress = 1), @SimpleIndex(name = "ArticleEntity_A_C_D", members = { "articleName", "colorType", "binded" }, compress = 3) })
public class ArticleEntity implements Cacheable, CacheListener, Serializable {

	public static final long serialVersionUID = 44582301231293L;

	/**
	 * 报名号
	 */
	public static final String SIGNUPNUMBER_KEY = "signUpNumber";

	/**
	 * 玩家id
	 */
	public static final String PLAYERID_KEY = "playerId";
	// 法宝属性key
	public static final String MAGIC_WEAPON_KEY = "magicweaponid";
	// 宠物id属性key
	public static final String PET_ID_KEY = "petid";
	// 宠物颜色key
	public static final String PET_COLOR_KEY = "colortype";

	public static final byte STATE_DELETE = 10;

	public ArticleEntity() {

	}

	@Override
	protected void finalize() throws Throwable {

	}

	public ArticleEntity(long id) {
		this();
		this.id = id;
		this.dirty = true;
	}

	/**
	 * 实体的标识，所有物品实体都有唯一的标识
	 */
	@SimpleId
	long id;

	@SimpleVersion
	protected int version2;

	/**
	 * 物品实体对应的物种的名字
	 */
	@SimpleColumn(name = "aName", length = 64)
	String articleName;

	/**
	 * 物品实体对应的显示的名字
	 */
	String showName;

	/**
	 * 计时器
	 * 对于物品有有效期的，就有这个变量，当此变量值为null时表明该物品没有有效期
	 */
	Timer timer;

	/**
	 * 这个变量用于检索用，如果为0代表没有时间限制
	 */
	long endTime;

	/**
	 * 装备颜色
	 */
	@SimpleColumn(name = "cType")
	protected int colorType;

	/**
	 * 数据不用存储，标记是否给客户端发过续费标记，false表示没有给客户端发过消息，true表示已经给客户端发过消息
	 */
	boolean sentMessageFlag;

	/**
	 * 是否是遗弃的物品
	 */
	boolean abandoned;

	boolean taskArticle;

	transient protected boolean dirty;

	protected long LastUpdateTime = 0;

	/**
	 * 是否已经绑定
	 */
	boolean binded = false;

	/**
	 * 物品状态为DELETE时且引用计数为0时才进行删除操作
	 */
	byte status;

	/**
	 * 引用计数，此数据在创建的时候设置，当玩家每删除一个的时候，引用计数减少一，只有引用计数减少到0的时候，才可以真正的删除
	 */
	long referenceCount = -1;

	/**
	 * 只用于temp物品，其他的逻辑慎用
	 */
	boolean tempEntityFlag;

	// 表明这个物品是否可以重叠
	// 此变量是新加的变量，数据库默认为false
	boolean overlapFlag;

	// 只用于统计，有一些物品需要统计是什么系统产生的
	// 如金钱道具，这样可以知道货币产生方式
	// String createReason = "";

	public boolean isOverlapFlag() {
		return overlapFlag;
	}

	public void setOverlapFlag(boolean overlapFlag) {
		this.overlapFlag = overlapFlag;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
		saveData("status");
	}

	public boolean isTempEntityFlag() {
		return tempEntityFlag;
	}

	public void setTempEntityFlag(boolean tempEntityFlag) {
		this.tempEntityFlag = tempEntityFlag;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
		saveData("colorType");
	}

	public boolean isSentMessageFlag() {
		return sentMessageFlag;
	}

	public void setSentMessageFlag(boolean sentMessageFlag) {
		this.sentMessageFlag = sentMessageFlag;
		saveData("sentMessageFlag");
	}

	public void saveData(String fileName) {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if (aem != null && aem.em != null) {
			aem.em.notifyFieldChange(this, fileName);
		}
	}

	public boolean isBinded() {
		return binded;
	}

	public boolean isRealBinded() {
		Article a = ArticleManager.getInstance().getArticle(articleName);
		boolean isbind = false;
		if (a != null && a instanceof InlayArticle) {
			if (((InlayArticle) a).getInlayType() > 1) {
				BaoShiXiaZiData data = ArticleManager.getInstance().getxiLianData(null, id);
				if (data != null) {
					long ids[] = data.getIds();
					if (ids != null && ids.length > 0) {
						for (long id : ids) {
							if (id > 0) {
								ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
								if (ae != null) {
									if (ae.isBinded()) {
										isbind = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		return isbind;
	}

	public void setBinded(boolean binded) {
		this.binded = binded;
		saveData("binded");
		if (binded) {
			if (this instanceof EquipmentEntity) {

			} else if (this instanceof PropsEntity) {

			} else {
				ArticleManager am = ArticleManager.getInstance();
				if (am != null) {
					Article a = am.getArticle(this.articleName);
					if (a.isOverlap() && a.getBindStyle() == Article.BINDTYPE_USE) {
						String stacktrace = StringUtil.getStackTrace(Thread.currentThread().getStackTrace());
						if (ArticleEntityManager.log.isWarnEnabled()) ArticleEntityManager.log.warn("[可堆叠的物品] [使用绑定] [{}] [{}] [binded:{}] \n{}", new Object[] { id, articleName, binded, stacktrace });
					}
				}
			}
		}
	}

	public boolean isTaskArticle() {
		return taskArticle;
	}

	public void setTaskArticle(boolean taskArticle) {
		this.taskArticle = taskArticle;
		saveData("taskArticle");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
		saveData("articleName");
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
		saveData("showName");
	}

	public boolean isAbandoned() {
		return abandoned;
	}

	public void setAbandoned(boolean abandoned) {
		this.abandoned = abandoned;
		saveData("abandoned");
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		if (ArticleEntityManager.log.isDebugEnabled()) {
			ArticleEntityManager.log.debug("[{}] [{}] [dirty:{}] [{}] [{}]", new Object[] { this.getArticleName(), this.getId(), dirty, StringUtil.getStackTrace(Thread.currentThread().getStackTrace()) });
		}
		this.dirty = dirty;
	}

	public long getLastUpdateTime() {
		return LastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		LastUpdateTime = lastUpdateTime;
		saveData("LastUpdateTime");
	}

	public void remove(int type) {
		// TODO Auto-generated method stub
		if (type == CacheListener.LIFT_TIMEOUT || type == CacheListener.SIZE_OVERFLOW) {
			ArticleEntityManager.getInstance().entityTimeoutFromCache(this, type);
		}
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
		saveData("timer");
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
		saveData("endTime");
	}

	/**
	 * 玩家获得物品详细介绍信息
	 * @return
	 */
	public String getInfoShow(Player player) {
		return getCommonInfoShow(player);
	}

	public synchronized long getReferenceCount() {
		return referenceCount;
	}

	public synchronized void setReferenceCount(long referenceCount) {
		this.referenceCount = referenceCount;
		saveData("referenceCount");
	}

	// public String getCreateReason() {
	// return createReason;
	// }
	// public void setCreateReason(String createReason) {
	// this.createReason = createReason;
	// saveData("createReason");
	// }
	/**
	 * 玩家获得物品详细介绍信息
	 * @return
	 */
	public String getCommonInfoShow(Player player) {
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			return Translate.物品 + ":" + articleName;
		}
		Article a = am.getArticle(articleName);
		StringBuffer sb = new StringBuffer();
		// sb.append("<f color='0xFFFF00'>").append(Article.KNAP_TYPE_NAME[a.getKnapsackType()]).append("</f>");
		if (isBinded() /* && (a instanceof EnchantArticle == false) */) {
			sb.append("\n<f color='0xFFFF00' size='34'>").append(Translate.已绑定).append("</f>");
		}

		if (a instanceof BrightInlayProps) {
			sb.append("\n<f color='0x00FF00' size='34'>").append(Translate.光效宝石).append("</f>");
		}

		PlayerArticleProtectData data = ArticleProtectManager.instance.getPlayerData(player);
		if (data != null) {
			ArticleProtectData da = data.getProtectData(this);
			if (da != null) {
				String daString = null;
				if (da.getState() == ArticleProtectDataValues.ArticleProtect_State_Common) {
					daString = Translate.以锁魂;
				} else if (da.getState() == ArticleProtectDataValues.ArticleProtect_State_High) {
					daString = Translate.以高级锁魂;
				}
				if (da.getRemoveTime() > 0) {
					daString = Translate.正在解魂;
					long haveTime = da.getRemoveTime() - System.currentTimeMillis();
					if (haveTime < 0) {
						haveTime = 0;
					}
					daString += ":" + (haveTime / 1000 / 60 / 60) + Translate.小时 + (haveTime / 1000 / 60 % 60) + Translate.分钟;
				}
				if (daString != null) {
					sb.append("\n <f color='0xFFFF00' size='34'>").append(daString).append("</f>");
				}
			}
		}

		if (timer != null) {
			sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.有效期).append("：");
			long time = timer.getEndTime() - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			long day = time / 1000 / 60 / 60 / 24;
			long hour = time / 1000 / 60 / 60 % 24;
			if (day > 0) {
				sb.append(day).append(Translate.text_jiazu_114);
			}
			if (hour > 0) {
				sb.append(hour).append(Translate.text_小时);
			}
			if (day <= 0 && hour <= 0) {
				sb.append(Translate.text_不足1小时);
			}
			sb.append("</f>");
		}

		if (a instanceof EnchantArticle && a.getStroy() != null && !a.getStroy().trim().equals("")) {
			sb.append("\n<f color='0xFFFFFF' size='34'>").append(a.getStroy()).append("</f>");
		}
		EnchantModel model = null;
		if (a instanceof EnchantArticle) {
			int enchantId = ((EnchantArticle) a).getEnchantId();
			model = EnchantManager.instance.modelMap.get(enchantId);
			if (model != null) {
				sb.append("\n<f size='34'>" + String.format(Translate.需要装备等级, model.getEquiptLevelLimit() + "") + "</f>");
			}
		}
		if (a instanceof Props) {
			Props prop = (Props) a;
			sb.append("\n<f color='0x00FF00' size='34'>").append(Translate.境界限定).append(":").append(PlayerManager.classlevel[prop.getClassLimit()]).append("</f>");
		}
		if (a.getDescription() != null && !a.getDescription().trim().equals("")) {
			sb.append("\n<f color='0x00FF00' size='34'>").append(a.getDescription()).append("</f>");
		}
		if (model != null) {
			sb.append("\n<f size='34'>" + Translate.灵性).append(":").append(model.getDurable()).append("/").append(model.getDurable()).append("</f>");
		}
		if (model == null && a.getStroy() != null && !a.getStroy().trim().equals("")) {
			sb.append("\n<f color='0xFFFFFF' size='34'>").append(a.getStroy()).append("</f>");
		}
		if (a instanceof InlayArticle) {
			sb.append("\n<f color='0x00FF00' size='34'>").append(Translate.镶嵌境界限定).append(":").append(PlayerManager.classlevel[((InlayArticle) a).getClassLevel()]).append("</f>");
			if (((InlayArticle) a).getInlayType() > 1) {
				BaoShiXiaZiData shenXia = ArticleManager.getInstance().getxiLianData(player, id);
				if (shenXia != null) {
					String names[] = shenXia.getNames();
					if (names != null && names.length > 0) {
						for (String name : names) {
							if (name != null && !name.isEmpty()) {
								Article baoshi = ArticleManager.getInstance().getArticle(name);
								if (baoshi != null && baoshi instanceof InlayArticle) {
									int[] values = ((InlayArticle) baoshi).getPropertysValues();
									if (values != null) {
										for (int i = 0; i < values.length; i++) {
											int value = values[i];
											if (value != 0) {
												switch (i) {
												case 0:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.最大血量).append("+").append(value).append("</f>");
													break;
												case 1:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.物理攻击).append("+").append(value).append("</f>");
													break;
												case 2:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.法术攻击).append("+").append(value).append("</f>");
													break;
												case 3:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.物理防御).append("+").append(value).append("</f>");
													break;
												case 4:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.法术防御).append("+").append(value).append("</f>");
													break;
												case 5:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.闪躲).append("+").append(value).append("</f>");
													break;
												case 6:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.免暴).append("+").append(value).append("</f>");
													break;
												case 7:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.命中).append("+").append(value).append("</f>");
													break;
												case 8:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.暴击).append("+").append(value).append("</f>");
													break;
												case 9:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.精准).append("+").append(value).append("</f>");
													break;
												case 10:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.破甲).append("+").append(value).append("</f>");
													break;
												case 11:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金攻击).append("+").append(value).append("</f>");
													break;
												case 23:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金攻击).append("+").append(value).append("</f>");
													break;
												case 26:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金攻击).append("+").append(value).append("</f>");
													break;
												case 12:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金抗性).append("+").append(value).append("</f>");
													break;
												case 24:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金抗性).append("+").append(value).append("</f>");
													break;
												case 27:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金抗性).append("+").append(value).append("</f>");
													break;
												case 13:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金减抗).append("+").append(value).append("</f>");
													break;
												case 25:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金减抗).append("+").append(value).append("</f>");
													break;
												case 28:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金减抗).append("+").append(value).append("</f>");
													break;
												case 14:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水攻击).append("+").append(value).append("</f>");
													break;
												case 29:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水攻击).append("+").append(value).append("</f>");
													break;
												case 32:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水攻击).append("+").append(value).append("</f>");
													break;
												case 15:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水抗性).append("+").append(value).append("</f>");
													break;
												case 30:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水抗性).append("+").append(value).append("</f>");
													break;
												case 33:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水抗性).append("+").append(value).append("</f>");
													break;
												case 16:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水减抗).append("+").append(value).append("</f>");
													break;
												case 31:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水减抗).append("+").append(value).append("</f>");
													break;
												case 34:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水减抗).append("+").append(value).append("</f>");
													break;
												case 17:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火攻击).append("+").append(value).append("</f>");
													break;
												case 41:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火攻击).append("+").append(value).append("</f>");
													break;
												case 44:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火攻击).append("+").append(value).append("</f>");
													break;
												case 18:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火抗性).append("+").append(value).append("</f>");
													break;
												case 42:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火抗性).append("+").append(value).append("</f>");
													break;
												case 45:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火抗性).append("+").append(value).append("</f>");
													break;
												case 19:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火减抗).append("+").append(value).append("</f>");
													break;
												case 43:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火减抗).append("+").append(value).append("</f>");
													break;
												case 46:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火减抗).append("+").append(value).append("</f>");
													break;
												case 20:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木攻击).append("+").append(value).append("</f>");
													break;
												case 35:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木攻击).append("+").append(value).append("</f>");
													break;
												case 38:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木攻击).append("+").append(value).append("</f>");
													break;
												case 21:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木抗性).append("+").append(value).append("</f>");
													break;
												case 36:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木抗性).append("+").append(value).append("</f>");
													break;
												case 39:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木抗性).append("+").append(value).append("</f>");
													break;
												case 22:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木减抗).append("+").append(value).append("</f>");
													break;
												case 37:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木减抗).append("+").append(value).append("</f>");
													break;
												case 40:
													sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木减抗).append("+").append(value).append("</f>");
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
			}
			int[] values = ((InlayArticle) a).getPropertysValues();
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					int value = values[i];
					if (value != 0) {
						switch (i) {
						case 0:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.最大血量).append("+").append(value).append("</f>");
							break;
						case 1:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.物理攻击).append("+").append(value).append("</f>");
							break;
						case 2:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.法术攻击).append("+").append(value).append("</f>");
							break;
						case 3:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.物理防御).append("+").append(value).append("</f>");
							break;
						case 4:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.法术防御).append("+").append(value).append("</f>");
							break;
						case 5:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.闪躲).append("+").append(value).append("</f>");
							break;
						case 6:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.免暴).append("+").append(value).append("</f>");
							break;
						case 7:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.命中).append("+").append(value).append("</f>");
							break;
						case 8:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.暴击).append("+").append(value).append("</f>");
							break;
						case 9:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.精准).append("+").append(value).append("</f>");
							break;
						case 10:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.破甲).append("+").append(value).append("</f>");
							break;
						case 11:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金攻击).append("+").append(value).append("</f>");
							break;
						case 23:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金攻击).append("+").append(value).append("</f>");
							break;
						case 26:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金攻击).append("+").append(value).append("</f>");
							break;
						case 12:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金抗性).append("+").append(value).append("</f>");
							break;
						case 24:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金抗性).append("+").append(value).append("</f>");
							break;
						case 27:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金抗性).append("+").append(value).append("</f>");
							break;
						case 13:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金减抗).append("+").append(value).append("</f>");
							break;
						case 25:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金减抗).append("+").append(value).append("</f>");
							break;
						case 28:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.庚金减抗).append("+").append(value).append("</f>");
							break;
						case 14:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水攻击).append("+").append(value).append("</f>");
							break;
						case 29:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水攻击).append("+").append(value).append("</f>");
							break;
						case 32:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水攻击).append("+").append(value).append("</f>");
							break;
						case 15:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水抗性).append("+").append(value).append("</f>");
							break;
						case 30:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水抗性).append("+").append(value).append("</f>");
							break;
						case 33:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水抗性).append("+").append(value).append("</f>");
							break;
						case 16:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水减抗).append("+").append(value).append("</f>");
							break;
						case 31:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水减抗).append("+").append(value).append("</f>");
							break;
						case 34:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.葵水减抗).append("+").append(value).append("</f>");
							break;
						case 17:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火攻击).append("+").append(value).append("</f>");
							break;
						case 41:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火攻击).append("+").append(value).append("</f>");
							break;
						case 44:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火攻击).append("+").append(value).append("</f>");
							break;
						case 18:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火抗性).append("+").append(value).append("</f>");
							break;
						case 42:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火抗性).append("+").append(value).append("</f>");
							break;
						case 45:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火抗性).append("+").append(value).append("</f>");
							break;
						case 19:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火减抗).append("+").append(value).append("</f>");
							break;
						case 43:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火减抗).append("+").append(value).append("</f>");
							break;
						case 46:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.离火减抗).append("+").append(value).append("</f>");
							break;
						case 20:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木攻击).append("+").append(value).append("</f>");
							break;
						case 35:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木攻击).append("+").append(value).append("</f>");
							break;
						case 38:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木攻击).append("+").append(value).append("</f>");
							break;
						case 21:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木抗性).append("+").append(value).append("</f>");
							break;
						case 36:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木抗性).append("+").append(value).append("</f>");
							break;
						case 39:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木抗性).append("+").append(value).append("</f>");
							break;
						case 22:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木减抗).append("+").append(value).append("</f>");
							break;
						case 37:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木减抗).append("+").append(value).append("</f>");
							break;
						case 40:
							sb.append("\n<f color='0x00e4ff' size='34'>").append(Translate.乙木减抗).append("+").append(value).append("</f>");
							break;
						}
					}
				}
			}
		}
		if (a instanceof PetFoodArticle) {
			PetFoodArticle pfa = (PetFoodArticle) a;
			if (pfa.getType() == 1) {
				sb.append("\n<f color='0xFFFFFF' size='34'>").append(Translate.可增加快乐值).append(pfa.getValueByColor(this.getColorType())).append("</f>");
			} else if (pfa.getType() == 2) {
				sb.append("\n<f color='0xFFFFFF' size='34'>").append(Translate.可增加寿命值).append(pfa.getValueByColor(this.getColorType())).append("</f>");
			}

		}
		if (a.getUseMethod() != null && !a.getUseMethod().trim().equals("")) {
			sb.append("\n<f color='0xFFFF00' size='34'>").append(a.getUseMethod()).append("</f>");
		}
		if (a.getGetMethod() != null && !a.getGetMethod().trim().equals("")) {
			sb.append("\n<f color='0xFFFF00' size='34'>").append(a.getGetMethod()).append("</f>");
		}
		return sb.toString();
	}

	public int getSize() {
		// TODO Auto-generated method stub

		int size = 0;
		size += CacheSizes.sizeOfObject(); // overhead of object
		size += CacheSizes.sizeOfLong(); // id
		size += CacheSizes.sizeOfString(articleName); // articleName
		size += CacheSizes.sizeOfLong(); // inValidTime
		size += CacheSizes.sizeOfBoolean(); // taskArticle
		size += CacheSizes.sizeOfBoolean(); // dirty
		size += CacheSizes.sizeOfBoolean(); // abandoned
		size += CacheSizes.sizeOfLong(); // lastUpdateTime

		return size;
	}

	public String toClickString() {
		Article a = ArticleManager.getInstance().getArticle(this.getArticleName());
		if (a != null) {
			int colorValue = ArticleManager.getColorValue(a, this.getColorType());
			// "<f onclick='articleEntity' onclickType='2' entityId='%s' color='%lld'>%s</f>";
			return "<f onclick='articleEntity' onclickType='2' entityId='" + getId() + "' color='" + colorValue + "'>" + getArticleName() + "</f>";
		} else {
			return getArticleName();
		}
	}

	public String toColorString() {
		Article a = ArticleManager.getInstance().getArticle(this.getArticleName());
		if (a != null) {
			int colorValue = ArticleManager.getColorValue(a, this.getColorType());
			return "<f color='" + colorValue + "'>" + getArticleName() + "</f>";
		} else {
			return getArticleName();
		}
	}
}
