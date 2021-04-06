package com.fy.engineserver.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.DaoJuFlow;
import com.sqage.stat.model.TransactionFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.watchdog.FileWatchdog;

public class ArticleStatManager {

	public static final byte RMB_YUANBAO = 6;

	public static final byte BIND_YINZI = 0;

	public static final byte YINZI = 1;

	public static final byte GONGZI = 2;

	public static final byte ZIYUAN = 3;

	public static final byte BANGPAIZIJIN = 4;

	public static final byte ARTICLES = 5;

	public static final String CURRENCY_DESP[] = new String[] { Translate.绑定银子, Translate.非绑定银子, Translate.工资, Translate.资源, Translate.帮派资金, Translate.物品兑换 };

	public static List<String> needStatNames = new ArrayList<String>();

	public static boolean useOldstat = false;// 是否使用老的方式做统计

	public static String getCurrencyDesp(byte currencyType) {
		try {
			return CURRENCY_DESP[currencyType];
		} catch (Exception e) {
			return Translate.未知;
		}
	}

	public static String[] 需要统计的物品二级分类 = new String[] { Translate.绿宝石, Translate.橙宝石, Translate.橙晶石, Translate.黑金石, Translate.蓝宝石, Translate.红宝石, Translate.白晶石, Translate.黄宝石, Translate.紫晶石, Translate.酒, Translate.封魔录 };

	public static String[] 需要统计的物品 = new String[] { Translate.银砖, Translate.银锭, Translate.财神的绑银袋, Translate.鉴定符, Translate.传奇鉴定符, Translate.传说鉴定符, Translate.神话鉴定符, Translate.永恒鉴定符, Translate.铭刻符, Translate.强化石, Translate.育兽丹, Translate.炼妖石, Translate.古董, Translate.碎银子, Translate.银两, Translate.银块, Translate.银条, Translate.银锭, Translate.银砖, Translate.普通红包, Translate.白银红包, Translate.黄金红包, Translate.白金红包, Translate.钻石红包, Translate.镖银白, Translate.镖银绿, Translate.镖银蓝, Translate.镖银紫, Translate.镖银橙, Translate.镖金白, Translate.镖金绿, Translate.镖金蓝, Translate.镖金紫, Translate.镖金橙, Translate.镖玉白, Translate.镖玉绿, Translate.镖玉蓝, Translate.镖玉紫, Translate.镖玉橙, Translate.地狱狼王蛋, Translate.青丘妖狐蛋, Translate.绝世花妖蛋, Translate.霸刀魔王蛋, Translate.黑风熊精蛋, Translate.金甲游神蛋, Translate.擎天牛神蛋, Translate.六耳猕猴蛋, Translate.麒麟圣祖蛋, Translate.白虎战神蛋, Translate.青龙斗神蛋, Translate.玄武水神蛋, Translate.朱雀火神蛋, Translate.九阴魔蝎蛋, Translate.青翼蝠王蛋, Translate.乱舞蝶妖蛋, Translate.罗刹鬼王蛋, Translate.嗜血剑魂蛋, Translate.千年参妖蛋, Translate.雷霆混元至圣蛋, Translate.大龙人蛋, Translate.爱的炫舞, Translate.浪漫今生, Translate.碧虚青鸾, Translate.八卦仙蒲, Translate.梦瞳仙鹤, Translate.乾坤葫芦, Translate.金极龙皇, Translate.焚焰火扇, Translate.深渊魔章, Translate.碧虚青鸾1天, Translate.碧虚青鸾7天, Translate.碧虚青鸾体验, Translate.青鸾翎羽, Translate.朱雀火羽, Translate.战火残片, Translate.才子印记, Translate.武圣印记, Translate.武圣印记碎片, Translate.青龙精魂, Translate.朱雀精魂, Translate.白虎精魂, Translate.玄武精魂, Translate.麒麟精魂, Translate.猛志固常在, Translate.五岳倒为轻, Translate.日曜贪狼盔, Translate.兽面吞火铠, Translate.血魂肩铠, Translate.紫铖护手, Translate.紫铖战靴, Translate.紫铖腰带, Translate.天启戒, Translate.王天古玉, Translate.太极图, Translate.天地为一朝, Translate.蚩尤瀑布碎, Translate.火曜破军盔, Translate.魔麟饕餮铠, Translate.皇鳞肩铠, Translate.遁龙护手, Translate.遁龙战靴, Translate.遁龙腰带, Translate.暝天戒, Translate.辟邪石玉, Translate.破天图, Translate.彩凤双飞翼, Translate.风萧易水寒, Translate.东极炫金罩, Translate.东极炫金衫, Translate.东极炫金肩, Translate.东极炫金手, Translate.东极炫金鞋, Translate.赤血, Translate.噬灵戒, Translate.浮世印, Translate.转空, Translate.明月照九州, Translate.承影无形杀, Translate.青冥海狱罩, Translate.青冥海狱衫, Translate.青冥海狱肩, Translate.青冥海狱手, Translate.天妖灭魂鞋, Translate.残雪, Translate.刺尨戒, Translate.苍天印, Translate.玄曦, Translate.点睛龙破壁, Translate.玄天斩灵洛, Translate.小北炎极头, Translate.小北炎极袄, Translate.小北炎极垫肩, Translate.离火手套, Translate.离火轻履, Translate.离火坠饰, Translate.火熔晶, Translate.火熔项链, Translate.逆央镜, Translate.泼墨碧丹青, Translate.血晶摩诃惑, Translate.海水江崖头, Translate.海水江崖袄, Translate.海水江崖垫肩, Translate.葵水手套, Translate.葵水轻履, Translate.葵水坠饰, Translate.婆罗戒, Translate.婆罗项链, Translate.葵水境, Translate.一岁一枯荣, Translate.本来无一物, Translate.乾坤地理蛇兜, Translate.乾坤地理裙, Translate.千幻蛛巾, Translate.千幻蝎腕, Translate.千幻道履, Translate.千幻腰带, Translate.封魔戒, Translate.落魂钟, Translate.毒王鼎, Translate.凤歌楚狂人, Translate.醉古堂剑扫, Translate.九黎太虚蛇兜, Translate.九黎太虚衫, Translate.蛟龙蛛巾, Translate.蛟龙蝎腕, Translate.蛟龙木履, Translate.蛟龙腰带, Translate.万魔戒, Translate.坤木蛊, Translate.星罗盘, Translate.朱雀斧, Translate.武圣禅杖, Translate.战火甲胄, Translate.朱雀刺, Translate.武圣轮, Translate.战火衫, Translate.朱雀笔, Translate.武圣剑, Translate.战火袄, Translate.朱雀幡, Translate.武圣杖, Translate.战火蟒衣, Translate.白玫瑰, Translate.棒棒糖, Translate.宝石仙袋, Translate.宝石仙袋2级, Translate.初级炼妖石, Translate.福星宝石袋3级, Translate.福星宝石袋4级, Translate.福星宝石袋5级, Translate.福星宝石袋6级, Translate.高级鲁班令, Translate.鬼斧神工令, Translate.红玫瑰, Translate.蓝色妖姬, Translate.鲁班令, Translate.免罪金牌, Translate.巧克力, Translate.软糖, Translate.体力丹, Translate.田地令, Translate.万里法宝鉴定符, Translate.香火, Translate.新福星宝石袋2级, Translate.押镖令, Translate.火鸡肉, Translate.酒票, Translate.帖券, Translate.火鸡蛋, Translate.感恩首充礼, Translate.火鸡大餐礼券, Translate.火鸡大餐, Translate.蛮荒女皇蛋, Translate.玄瞳金晶蛋, Translate.九尾雪狐蛋, Translate.绝品圣宠精魄, Translate.梦瞳仙鹤, Translate.仙鹤羽毛, Translate.飞行坐骑礼包3天, Translate.炼器符, Translate.一品元气真丹初级, Translate.二品元气真丹初级, Translate.三品元气真丹初级, Translate.四品元气真丹初级, Translate.五品元气真丹初级, Translate.六品元气真丹初级, Translate.一品元气真丹中级, Translate.二品元气真丹中级, Translate.三品元气真丹中级, Translate.四品元气真丹中级, Translate.五品元气真丹中级, Translate.六品元气真丹中级, Translate.一品元气玄丹初级, Translate.二品元气玄丹初级, Translate.三品元气玄丹初级, Translate.四品元气玄丹初级, Translate.五品元气玄丹初级, Translate.六品元气玄丹初级, Translate.一品元气玄丹中级, Translate.二品元气玄丹中级, Translate.三品元气玄丹中级, Translate.四品元气玄丹中级, Translate.五品元气玄丹中级, Translate.六品元气玄丹中级, Translate.混元伏魔鼎, Translate.混元镇邪鼎, Translate.混天封妖鼎, Translate.混天至尊鼎, Translate.洪荒古符, Translate.混元伏魔残片, Translate.混元镇邪残片, Translate.混天封妖残片, Translate.混天至尊残片, Translate.洪荒古残卷, Translate.紫铖战靴, Translate.东极炫金鞋, Translate.离火轻履, Translate.千幻道履, Translate.王天古玉, Translate.浮世印, Translate.火熔项链, Translate.落魂钟, Translate.紫铖护手, Translate.东极炫金手, Translate.离火手套, Translate.千幻蝎腕, Translate.遁龙护手, Translate.青冥海狱手, Translate.葵水手套, Translate.蛟龙蝎腕, Translate.遁龙腰带, Translate.残雪, Translate.葵水坠饰, Translate.蛟龙腰带, Translate.辟邪石玉, Translate.苍天印, Translate.婆罗项链, Translate.坤木蛊, Translate.楠木, Translate.铸铁, Translate.胶黏剂, Translate.贡献卡, Translate.方舟通行证, Translate.头等舱钥匙, Translate.船长室钥匙, Translate.神秘钥匙, Translate.炼器符, Translate.高级海鲜大礼包, Translate.超级海鲜大礼包, Translate.盐烤青花鱼, Translate.海鲜烩饭, Translate.焗烤龙虾, Translate.惊涛鱼排, Translate.香浓炖鱼, Translate.秘制鲅鱼, Translate.红烧鲍翅燕, Translate.鲍汁鸡腿菇, Translate.碳烧元贝, Translate.干烧桂鱼, Translate.海参鹅掌煲, Translate.盐烤青花鱼高级, Translate.海鲜烩饭高级, Translate.焗烤龙虾高级, Translate.惊涛鱼排高级, Translate.香浓炖鱼高级, Translate.秘制鲅鱼高级, Translate.红烧鲍翅燕高级, Translate.鲍汁鸡腿菇高级, Translate.碳烧元贝高级, Translate.干烧桂鱼高级, Translate.海参鹅掌煲高级, Translate.仙鹤羽毛, Translate.头等舱金牌礼包普通, Translate.头等舱银牌礼包普通, Translate.头等舱铜牌礼包普通, Translate.头等舱击杀礼包普通, Translate.头等舱参与礼包普通, Translate.船长室金牌礼包普通, Translate.船长室银牌礼包普通, Translate.船长室铜牌礼包普通, Translate.船长室击杀礼包普通, Translate.船长室参与礼包普通, Translate.头等舱金牌礼包优质, Translate.头等舱银牌礼包优质, Translate.头等舱铜牌礼包优质, Translate.头等舱击杀礼包优质, Translate.头等舱参与礼包优质, Translate.船长室金牌礼包优质, Translate.船长室银牌礼包优质, Translate.船长室铜牌礼包优质, Translate.船长室击杀礼包优质, Translate.船长室参与礼包优质, Translate.头等舱金牌礼包顶级, Translate.头等舱银牌礼包顶级, Translate.头等舱铜牌礼包顶级, Translate.头等舱击杀礼包顶级, Translate.头等舱参与礼包顶级, Translate.船长室金牌礼包顶级, Translate.船长室银牌礼包顶级, Translate.船长室铜牌礼包顶级, Translate.船长室击杀礼包顶级, Translate.船长室参与礼包顶级, Translate.圣诞袜子, Translate.圣诞Q宠饼干, Translate.圣诞礼盒, Translate.VIP1体验卡1天, Translate.VIP2体验卡1天, Translate.VIP3体验卡1天, Translate.VIP4体验卡1天, Translate.VIP5体验卡1天, Translate.VIP6体验卡1天, Translate.VIP7体验卡1天, Translate.涅槃百宝囊, Translate.杀戮秘籍修罗, Translate.普度秘籍修罗, Translate.杀阵秘籍修罗, Translate.惊雷秘籍修罗, Translate.夺命秘籍修罗, Translate.坠天秘籍仙心, Translate.冰陵秘籍仙心, Translate.耀天秘籍仙心, Translate.天诛秘籍仙心, Translate.雪葬秘籍仙心, Translate.鬼幕秘籍影魅, Translate.遁形秘籍影魅, Translate.鬼刺秘籍影魅, Translate.断喉秘籍影魅, Translate.夺魂秘籍影魅, Translate.血祭秘籍九黎, Translate.反噬秘籍九黎, Translate.万蛊秘籍九黎, Translate.噬灵秘籍九黎, Translate.惊魂秘籍九黎, Translate.飞行坐骑碎片, Translate.五品元气真丹初级, Translate.锁魂符, Translate.高级锁魂符, Translate.解魂石, Translate.红朝天龙, Translate.如意锦囊, Translate.易筋丹, Translate.鞭炮, Translate.礼花, Translate.青苹果, Translate.水晶苹果, Translate.天使之翼, Translate.心动之恋, Translate.水晶之恋, Translate.倾城之恋, };

	public static final byte OPERATION_物品获得和消耗 = 0;
	public static final byte OPERATION_交换 = 1;

	public static List<ArticleStatConfig> unStatCofig = new ArrayList<ArticleStatConfig>();

	// {Translate.时装类,Translate.任务类,Translate.商品类};

	static {
		unStatCofig.add(new ArticleStatConfig(Translate.时装类, null));
		unStatCofig.add(new ArticleStatConfig(Translate.任务类, null));
		unStatCofig.add(new ArticleStatConfig(Translate.宠物药品, new String[] { Translate.宠物缓回, Translate.宠物瞬回 }));
		unStatCofig.add(new ArticleStatConfig(Translate.角色药品, new String[] { Translate.缓回血量, Translate.缓回法力值 }));
	}

	// 按照二级分类来屏蔽，由策划提供一级/二级分类来确定是否统计
	public static boolean isNeedStatArticle(Article article) {
		if (article == null) {
			return false;
		}
		if (useOldstat) {
			boolean needStat = needStatNames.contains(article.getName());
			if (ArticleManager.logger.isInfoEnabled()) {
				ArticleManager.logger.info("[物品统计] [" + article.getName() + "] [是否需要统计：" + needStat + "]");
			}
			return needStat;
		}
		for (ArticleStatConfig config : unStatCofig) {
			if (config.inConfig(article)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 物品流向统计 fromPlayer toPlayer可以为空
	 * 
	 * @param fromPlayer
	 * @param fromPlayerUsername
	 * @param fromPlayerLevel
	 * @param toPlayer
	 * @param toPlayerUsername
	 * @param toPlayerLevel
	 * @param ae
	 * @param type
	 * @param price
	 * @param moneyType
	 * @param number
	 * @param reason
	 * @param shopName
	 */
	public static void addToArticleStat(Player fromPlayer, Player toPlayer, ArticleEntity ae, byte statType, long moneyPerArticle, byte moneyType, long number, String reason, String shopName) {
		try {
			if (TestServerConfigManager.isTestServer()) {
				return;
			}
			ArticleManager am = ArticleManager.getInstance();
			Article article = am.getArticle(ae.getArticleName());
			if (isNeedStatArticle(article)) {
				StatClientService statClientService = StatClientService.getInstance();
				String reasonStr = reason;
				if (reasonStr != null && reasonStr.length() >= 30) {
					reasonStr = reasonStr.substring(0, 30);
				}
				String moneyTypeStr = getCurrencyDesp(moneyType);

				String shopNameStr = shopName;
				String fromPlayerStr = "";
				String toPlayerStr = "";
				String fromPlayerLevelStr = "";
				String toPlayerLevelStr = "";
				String jixing = "";
				String bindType = "" + ae.isBinded();
				String color = "" + ArticleManager.getColorString(article, ae.getColorType());
				String articleLevel = null;
				if (article instanceof Equipment) {
					articleLevel = "" + ((Equipment) article).getPlayerLevelLimit();
				} else if (article instanceof Props) {
					articleLevel = "" + ((Props) article).getLevelLimit();
				} else {
					articleLevel = "" + article.getArticleLevel();
				}
				if (fromPlayer != null) {
					fromPlayerStr = fromPlayer.getUsername();
					fromPlayerLevelStr = "" + fromPlayer.getLevel();
				}
				if (toPlayer != null) {
					toPlayerStr = toPlayer.getUsername();
					toPlayerLevelStr = "" + toPlayer.getLevel();
				}
				if (reasonStr == null) {
					reasonStr = "";
				}
				if (moneyTypeStr == null) {
					moneyTypeStr = "";
				}
				if (shopNameStr == null) {
					shopNameStr = "";
				}
				if (fromPlayerStr == null) {
					fromPlayerStr = "";
				}
				if (toPlayerStr == null) {
					toPlayerStr = "";
				}
				if (statClientService != null) {
					if (statType == OPERATION_物品获得和消耗) {
						DaoJuFlow daoJuFlow = new DaoJuFlow();
						daoJuFlow.setJixing(jixing);
						daoJuFlow.setCreateDate(System.currentTimeMillis());
						daoJuFlow.setDanJia(moneyPerArticle);// 购买道具的单价
						String articleName = article.getName_stat();// ae.getArticleName();
						if (!useOldstat) {
							if (article instanceof Equipment) {
								if (((Equipment) article).getEquipmentType() <= EquipmentColumn.EQUIPMENT_TYPE_FOR_PLAYER) {
									int classLevel = ((Equipment) article).getClassLimit();// 境界限制
									articleName = "角色装备(" + classLevel * 20 + "~" + (classLevel + 1) * 20 + ")";
								}else if(((Equipment) article).getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_ChiBang){
									articleName = "翅膀";
								} else {// 坐骑装备
									articleName = "坐骑装备(" + ((Equipment) article).getPlayerLevelLimit() + ")";
								}
								if (ArticleManager.logger.isDebugEnabled()) {
									ArticleManager.logger.debug("[数据统计] [装备] [" + article.getName() + ">" + articleName + "]");
								}
							}
						}
						daoJuFlow.setDaoJuName(articleName);
						daoJuFlow.setDaoJuNum(number);// 购买道具的数量

						daoJuFlow.setFenQu(GameConstants.getInstance().getServerName());

						daoJuFlow.setGetType(reasonStr);
						daoJuFlow.setHuoBiType(moneyTypeStr);

						daoJuFlow.setGameLevel(fromPlayerLevelStr);
						daoJuFlow.setUserName(fromPlayerStr);

						daoJuFlow.setId(ae.getId());// 如果道具没有id，设为0
						daoJuFlow.setPosition(shopNameStr);// 购买道具的位置，比如商店购买

						daoJuFlow.setBindType(bindType);
						daoJuFlow.setDaoJuColor(color);
						daoJuFlow.setDaoJuLevel(articleLevel);
						if (fromPlayer != null) {
							daoJuFlow.setVip(Long.valueOf(String.valueOf(fromPlayer.getVipLevel())));
							daoJuFlow.setGuojia(CountryManager.得到国家名(fromPlayer.getCountry()));
						}
						if (!TestServerConfigManager.isTestServer()) {
							statClientService.sendDaoJuFlow("", daoJuFlow);
						}
					} else if (statType == OPERATION_交换) {
						TransactionFlow flow = new TransactionFlow();
						flow.setJixing(jixing);
						flow.setCreateDate(System.currentTimeMillis());
						flow.setDanjia((int) moneyPerArticle);
						flow.setDaoJuName(ae.getArticleName());
						flow.setDaojunum((int) number);

						flow.setFenQu(GameConstants.getInstance().getServerName());

						flow.setFgameLevel(fromPlayerLevelStr);// 发出道具玩家的级别
						flow.setFuserName(fromPlayerStr);// 发出道具玩家的用户名

						flow.setId(ae.getId());

						flow.setToGameLevel(toPlayerLevelStr);// 接收道具玩家的级别
						flow.setToUserName(toPlayerStr);// 接收道具玩家的名称

						flow.setBindType(bindType);
						flow.setDaoJuColor(color);
						flow.setDaoJuLevel(articleLevel);
						if (fromPlayer != null) {
							flow.setFvip(String.valueOf(fromPlayer.getVipLevel()));
							flow.setFguoJia(CountryManager.得到国家名(fromPlayer.getCountry()));
						} else {
							flow.setFvip("");
							flow.setFguoJia("");
						}
						if (toPlayer != null) {
							flow.setTovip(String.valueOf(toPlayer.getVipLevel()));
							flow.setToguoJia(CountryManager.得到国家名(toPlayer.getCountry()));
						} else {
							flow.setTovip("");
							flow.setToguoJia("");
						}
						String articleName = ae.getArticleName();
						if (!useOldstat) {
							if (article instanceof Equipment) {
								if (((Equipment) article).getEquipmentType() <= EquipmentColumn.EQUIPMENT_TYPE_FOR_PLAYER) {
									int classLevel = ((Equipment) article).getClassLimit();// 境界限制
									articleName = "角色装备(" + classLevel * 20 + "~" + (classLevel + 1) * 20 + ")";
								} else {// 坐骑装备
									articleName = "坐骑装备(" + ((Equipment) article).getPlayerLevelLimit() + ")";
								}
								if (ArticleManager.logger.isDebugEnabled()) {
									ArticleManager.logger.debug("[数据统计] [装备] [" + article.getName() + ">" + articleName + "]");
								}
							}
						}
						flow.setDaoJuName(articleName);
						flow.setTransactionType(reasonStr);// 道具交换的方式
						if (!TestServerConfigManager.isTestServer()) {
							statClientService.sendTransactionFlow("", flow);
						}
					}
				}
			}
		} catch (Exception ex) {
			ArticleManager.logger.error("[物品统计异常]", ex);
		}
	}

	/**
	 * 物品流向统计 fromPlayer toPlayer可以为空
	 * 
	 * @param fromPlayer
	 * @param fromPlayerUsername
	 * @param fromPlayerLevel
	 * @param toPlayer
	 * @param toPlayerUsername
	 * @param toPlayerLevel
	 * @param ae
	 * @param type
	 * @param price
	 * @param moneyType
	 * @param number
	 * @param reason
	 * @param shopName
	 */
	public static void addToArticleStat(Player fromPlayer, String fromPlayerUsername, int fromPlayerLevel, Player toPlayer, String toPlayerUsername, int toPlayerLevel, String articleName, int articleLevel, String colorName, boolean binded, byte statType, long moneyPerArticle, byte moneyType, long number, String reason, String shopName) {
		try {
			if (TestServerConfigManager.isTestServer() ) {
				return;
			}
			ArticleManager am = ArticleManager.getInstance();
			Article article = am.getArticle(articleName);
			if (isNeedStatArticle(article)) {

				if (!useOldstat && article != null) {
					if (article instanceof Equipment) {
						if (((Equipment) article).getEquipmentType() <= EquipmentColumn.EQUIPMENT_TYPE_FOR_PLAYER) {
							int classLevel = ((Equipment) article).getClassLimit();// 境界限制
							articleName = "角色装备(" + classLevel * 20 + "~" + (classLevel + 1) * 20 + ")";
						} else {// 坐骑装备
							articleName = "坐骑装备(" + ((Equipment) article).getPlayerLevelLimit() + ")";
						}
						if (ArticleManager.logger.isDebugEnabled()) {
							ArticleManager.logger.debug("[数据统计] [装备] [" + article.getName() + ">" + articleName + "]");
						}
					}
				}

				StatClientService statClientService = StatClientService.getInstance();
				String reasonStr = reason;
				String moneyTypeStr = "";

				String jixing = "";
				String shopNameStr = shopName;
				String fromPlayerStr = fromPlayerUsername;
				String toPlayerStr = toPlayerUsername;
				String fromPlayerLevelStr = "" + fromPlayerLevel;
				String toPlayerLevelStr = "" + toPlayerLevel;
				String bindType = "" + binded;
				String color = "" + colorName;
				String articleLevelStr = "" + articleLevel;
				if (fromPlayer != null) {
					fromPlayerStr = fromPlayer.getUsername();
					fromPlayerLevelStr = "" + fromPlayer.getLevel();
				}
				if (toPlayer != null) {
					toPlayerStr = toPlayer.getUsername();
					toPlayerLevelStr = "" + toPlayer.getLevel();
				}
				if (reasonStr == null) {
					reasonStr = "";
				}
				if (shopNameStr == null) {
					shopNameStr = "";
				}
				if (fromPlayerStr == null) {
					fromPlayerStr = "";
				}
				if (toPlayerStr == null) {
					toPlayerStr = "";
				}
				if (articleName == null) {
					articleName = "";
				}

				if (statClientService != null) {
					if (statType == OPERATION_物品获得和消耗) {
						DaoJuFlow daoJuFlow = new DaoJuFlow();
						daoJuFlow.setJixing(jixing);
						daoJuFlow.setCreateDate(System.currentTimeMillis());
						daoJuFlow.setDanJia(moneyPerArticle);// 购买道具的单价
						daoJuFlow.setDaoJuName(articleName);
						daoJuFlow.setDaoJuNum(number);// 购买道具的数量

						daoJuFlow.setFenQu(GameConstants.getInstance().getServerName());

						daoJuFlow.setGetType(reasonStr);
						daoJuFlow.setHuoBiType(moneyTypeStr);

						daoJuFlow.setGameLevel(fromPlayerLevelStr);
						daoJuFlow.setUserName(fromPlayerStr);

						daoJuFlow.setBindType(bindType);
						daoJuFlow.setDaoJuColor(color);
						daoJuFlow.setDaoJuLevel(articleLevelStr);

						daoJuFlow.setPosition(shopNameStr);// 购买道具的位置，比如商店购买
						if (!TestServerConfigManager.isTestServer()) {
							statClientService.sendDaoJuFlow("", daoJuFlow);
						}
					} else if (statType == OPERATION_交换) {
						TransactionFlow flow = new TransactionFlow();

						flow.setJixing(jixing);
						flow.setCreateDate(System.currentTimeMillis());
						// flow.setDanjia(price);
						flow.setDaoJuName(articleName);
						// flow.setDaojunum(number);

						flow.setFenQu(GameConstants.getInstance().getServerName());

						flow.setFgameLevel(fromPlayerLevelStr);// 发出道具玩家的级别
						flow.setFuserName(fromPlayerStr);// 发出道具玩家的用户名

						flow.setToGameLevel(toPlayerLevelStr);// 接收道具玩家的级别
						flow.setToUserName(toPlayerStr);// 接收道具玩家的名称

						flow.setBindType(bindType);
						flow.setDaoJuColor(color);
						flow.setDaoJuLevel(articleLevelStr);

						flow.setTransactionType(reasonStr);// 道具交换的方式
						if (!TestServerConfigManager.isTestServer()) {
							statClientService.sendTransactionFlow("", flow);
						}
					}
				}
			}
		} catch (Exception ex) {
			ArticleManager.logger.error("[物品统计异常]", ex);
		}
	}

	public File filePath;

	public void reLoad() throws Exception {
		long now = System.currentTimeMillis();
		BufferedReader buffer = null;
		try {
			List<String> set = new ArrayList<String>();
			buffer = new BufferedReader(new FileReader(filePath));
			String line = null;
			while ((line = buffer.readLine()) != null) {
				if (line.trim().length() > 0 && !set.contains(line.trim())) {
					set.add(line.trim());
				}
			}
			needStatNames = set;
			ArticleManager.logger.warn("[需要统计物品] [重新加载] [成功] [条数：" + needStatNames.size() + "] [耗时：" + (System.currentTimeMillis() - now) + "]");
		} catch (Exception e) {
			e.printStackTrace();
			ArticleManager.logger.warn("[需要统计物品] [重新加载] [异常] [条数：" + needStatNames.size() + "] [耗时：" + (System.currentTimeMillis() - now) + "]", e);
			throw new Exception();
		} finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void init() throws Exception {
		
		try {
			long now = System.currentTimeMillis();
			reLoad();
			MyFileWatchDog dog = new MyFileWatchDog();
			dog.setDaemon(true);
			dog.setName("ArticleStat-Watchlog");
			dog.addFile(filePath);
			dog.start();
		} catch (Exception e) {
			System.out.println("[系统初始化] [ArticleStatManager] [异常" + e + "]");
			ArticleManager.logger.error("[ArticleStatManager] [系统初始化] [异常]", e);
			throw new Exception();
		}
		ServiceStartRecord.startLog(this);
	}

	public class MyFileWatchDog extends FileWatchdog {
		public void doOnChange(File file) {
			try {
				long now = System.currentTimeMillis();
				reLoad();
				ArticleManager.logger.warn("[重新加载统计物品] [物品统计] [初始化完成] [需要统计物品数:" + needStatNames.size() + "] [" + getClass().getName() + "] [耗时：" + (System.currentTimeMillis() - now) + "毫秒]");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public File getFilePath() {
		return filePath;
	}

	public void setFilePath(File filePath) {
		this.filePath = filePath;
	}

}
