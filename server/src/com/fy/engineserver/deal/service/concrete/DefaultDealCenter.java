package com.fy.engineserver.deal.service.concrete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.deal.Deal;
import com.fy.engineserver.deal.ExchangeDeal;
import com.fy.engineserver.deal.service.DealCenter;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecord;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecordManager;
import com.fy.engineserver.exception.KnapsackFullException;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.smith.ArticleRelationShipManager;
import com.fy.engineserver.smith.MoneyRelationShipManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.Transaction_FaceFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 交易中心，每个玩家同一时间只能有一个交易
 * 
 */
public class DefaultDealCenter extends DealCenter {

	public static float DEAL_TAX = 0.05f;				//交易的税
	
	protected Hashtable<String, Deal> dealMap;

	protected int total;

	protected int agreed;

	protected int canceled;

	public void initialize() {
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		dealMap = new Hashtable<String, Deal>();
		self = this;
		ServiceStartRecord.startLog(this);
	}

	@Override
	public Map<String, Deal> getDealMap() {
		return dealMap;
	}

	@Override
	public int getAgreed() {
		return agreed;
	}

	@Override
	public int getCanceled() {
		return canceled;
	}

	@Override
	public int getTotalDeal() {
		return total;
	}

	@Override
	public boolean agreeDeal(Deal deal, Player player) throws KnapsackFullException, Exception {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		deal = getDeal(deal.getId());
		if (deal.getPlayerA().getId() == player.getId()) {
			deal.setAgreedA(true);
		} else {
			deal.setAgreedB(true);
		}
//		if (logger.isInfoEnabled()) logger.info("[agreeDeal] [" + deal.getId() + "][" + player.getUsername() + "] [" + player.getId() + "][" + player.getName() + "]");
		if (logger.isInfoEnabled()) logger.info("[agreeDeal] [{}][{}] [{}][{}]", new Object[]{deal.getId(),player.getUsername(),player.getId(),player.getName()});
		if (deal.isAgreedA() && deal.isAgreedB()) {
			Player playerB = deal.getPlayerB();
			Player playerA = deal.getPlayerA();
			synchronized (playerA.tradeKey) {
				synchronized (playerB.tradeKey) {
					// 完成交易
					// 先检查玩家背包是否和deal内记录一致
					int indexes[] = deal.getIndexesA();
					long entityIds[] = deal.getEntityIdsA();
					int counts[] = deal.getCountsA();
					byte[] pakTypes = deal.getPackageTypeA();
					boolean existTwoSameIndex = false;
					for (int i = 0; i < indexes.length; i++) {
						for (int j = 0; j < indexes.length; j++) {
							if (i != j && indexes[i] == indexes[j] && indexes[i] != -1) {
								existTwoSameIndex = true;
							}
						}
					}
					if (existTwoSameIndex) {
	//					logger.error("[交易失败] [可能是外挂行为:包含相同背包下标的指引] [" + deal.getPlayerA().getUsername() + "][" + deal.getPlayerA().getId() + "] [" + deal.getPlayerA().getName() + "] [格子下标：" + StringUtil.arrayToString(indexes, ",") + "] [数量：" + StringUtil.arrayToString(counts, ",") + "]");
						if (!使用新日志格式) {
							logger.error("[交易失败] [可能是外挂行为:包含相同背包下标的指引] [{}][{}] [{}] [格子下标：{}] [数量：{}]", new Object[]{deal.getPlayerA().getUsername(),deal.getPlayerA().getId(),deal.getPlayerA().getName(),StringUtil.arrayToString(indexes, ","),StringUtil.arrayToString(counts, ",")});
						} else {
							logger.error("[交易失败] [" + deal.getPlayerA().getLogString4Knap() + "] [格子下标:" + StringUtil.arrayToString(indexes, ",") + "] [数量:" + StringUtil.arrayToString(counts, ",") + "] [reason:可能是外挂行为,包含相同背包下标的指引]");
						}
						return false;
					}
					List<String> nameAlist = new ArrayList<String>();
					List<Integer> countAlist = new ArrayList<Integer>();
					List<Integer> colorBlist = new ArrayList<Integer>();
					List<Integer> colorAlist = new ArrayList<Integer>();
					List<Integer> indexAlist = new ArrayList<Integer>();
					for (int i = 0; i < entityIds.length; i++) {
						if (entityIds[i] != -1) {
							Cell cell = deal.getPlayerA().getKnapsacks_common()[pakTypes[i]].getCell(indexes[i]);
							ArticleEntity ae = deal.getPlayerA().getKnapsacks_common()[pakTypes[i]].getArticleEntityByCell(indexes[i]);
							if (ae != null && (ae.isBinded() || !GreenServerManager.canToOtherPlayer(ae))) {
	//							logger.error("[交易失败] [可能是外挂行为:交易了绑定物品] [" + deal.getPlayerA().getUsername() + "][" + deal.getPlayerA().getId() + "] [" + deal.getPlayerA().getName() + "] [格子下标：" + StringUtil.arrayToString(indexes, ",") + "][" + ae.getArticleName() + "] [数量：" + StringUtil.arrayToString(counts, ",") + "]");
								deal.getPlayerA().sendError(Translate.text_deal_004);
								if (!使用新日志格式) {
									logger.error("[交易失败] [可能是外挂行为:交易了绑定物品] [{}][{}] [{}] [格子下标：{}][{}] [数量：{}]", new Object[]{deal.getPlayerA().getUsername(),deal.getPlayerA().getId(),deal.getPlayerA().getName(),StringUtil.arrayToString(indexes, ","),ae.getArticleName(),StringUtil.arrayToString(counts, ",")});
								} else {
									logger.error("[交易失败] [" + deal.getPlayerA().getLogString4Knap() + "] [格子下标:" + StringUtil.arrayToString(indexes, ",") + "] [数量:" + StringUtil.arrayToString(counts, ",") + "] [reason:可能是外挂行为,交易了绑定物品]");
								}
								return false;
							}
							if (cell.getEntityId() != entityIds[i] || cell.getCount() < counts[i]) {
								if (ae != null) player.send_HINT_REQ(deal.getPlayerA().getName(), ae);
								else player.send_HINT_REQ(deal.getPlayerA().getName(), new ArticleEntity());
								if (使用新日志格式) {
									if (logger.isInfoEnabled()) {
										logger.info("[交易失败] ["+ deal.getPlayerA().getLogString4Knap()+"] [reason:装备栏发生变化，交易不成功]");
									}
								}
								throw new Exception("玩家(" + deal.getPlayerA().getUsername() + "," + deal.getPlayerA().getId() + "," + deal.getPlayerA().getName() + ")装备栏发生变化，交易不成功(背包下标[" + indexes[i] + "][物品:" + cell.getEntityId() + "][数量:" + cell.getCount() + "] != 交易[物品:" + entityIds[i] + "][数量:" + counts[i] + "])");
							} else {
								nameAlist.add(ae.getArticleName());
								countAlist.add(counts[i]);
								indexAlist.add(indexes[i]);
								colorAlist.add(ae.getColorType());
							}
						}
					}
					indexes = deal.getIndexesB();
					entityIds = deal.getEntityIdsB();
					counts = deal.getCountsB();
					pakTypes = deal.getPackageTypeB();
					existTwoSameIndex = false;
					for (int i = 0; i < indexes.length; i++) {
						for (int j = 0; j < indexes.length; j++) {
							if (i != j && indexes[i] == indexes[j] && indexes[i] != -1) {
								existTwoSameIndex = true;
							}
						}
					}
					if (existTwoSameIndex) {
	//					logger.error("[交易失败] [可能是外挂行为:包含相同背包下标的指引] [" + deal.getPlayerB().getUsername() + "][" + deal.getPlayerB().getId() + "] [" + deal.getPlayerB().getName() + "] [格子下标：" + StringUtil.arrayToString(indexes, ",") + "] [数量：" + StringUtil.arrayToString(counts, ",") + "]");
						if (!使用新日志格式) {
							logger.error("[交易失败] [可能是外挂行为:包含相同背包下标的指引] [{}][{}] [{}] [格子下标：{}] [数量：{}]", new Object[]{deal.getPlayerB().getUsername(),deal.getPlayerB().getId(),deal.getPlayerB().getName(),StringUtil.arrayToString(indexes, ","),StringUtil.arrayToString(counts, ",")});
						} else {
							logger.error("[交易失败] [" + deal.getPlayerB().getLogString4Knap() + "] [格子下标:" + StringUtil.arrayToString(indexes, ",") + "] [数量:" + StringUtil.arrayToString(counts, ",") + "] [reason:可能是外挂行为,包含相同背包下标的指引]");
						}
						return false;
					}
					List<String> nameBlist = new ArrayList<String>();
					List<Integer> countBlist = new ArrayList<Integer>();
					List<Integer> indexBlist = new ArrayList<Integer>();
					for (int i = 0; i < entityIds.length; i++) {
						if (entityIds[i] != -1) {
							Cell cell = deal.getPlayerB().getKnapsacks_common()[pakTypes[i]].getCell(indexes[i]);
							ArticleEntity ae = deal.getPlayerB().getKnapsacks_common()[pakTypes[i]].getArticleEntityByCell(indexes[i]);
							if (ae != null && (ae.isBinded() || !GreenServerManager.canToOtherPlayer(ae))) {
	//							logger.error("[交易失败] [可能是外挂行为:交易了绑定物品] [" + deal.getPlayerA().getUsername() + "][" + deal.getPlayerA().getId() + "] [" + deal.getPlayerA().getName() + "] [格子下标：" + StringUtil.arrayToString(indexes, ",") + "] [数量：" + StringUtil.arrayToString(counts, ",") + "] [物品:" + ae.getId() + "/" + ae.getArticleName() + "]");
								deal.getPlayerB().sendError(Translate.text_deal_004);
								if (!使用新日志格式) {
									logger.error("[交易失败] [可能是外挂行为:交易了绑定物品] [{}][{}] [{}] [格子下标：{}] [数量：{}] [物品:{}/{}]", new Object[]{deal.getPlayerA().getUsername(),deal.getPlayerA().getId(),deal.getPlayerA().getName(),StringUtil.arrayToString(indexes, ","),StringUtil.arrayToString(counts, ","),ae.getId(),ae.getArticleName()});
								} else {
									logger.error("[交易失败] [" + deal.getPlayerB().getLogString4Knap() + "] [格子下标:" + StringUtil.arrayToString(indexes, ",") + "] [数量:" + StringUtil.arrayToString(counts, ",") + "] [reason:可能是外挂行为,交易了绑定物品]");
								}
								return false;
							}
							if (cell.getEntityId() != entityIds[i] || cell.getCount() < counts[i]) {
								if (ae != null) player.send_HINT_REQ(deal.getPlayerB().getName(), ae);
								else player.send_HINT_REQ(deal.getPlayerB().getName(), new ArticleEntity());
								dealMap.remove(deal.getId());
								if (使用新日志格式) {
									if (logger.isInfoEnabled()) {
										logger.info("[交易失败] ["+ deal.getPlayerB().getLogString4Knap()+"] [reason:装备栏发生变化，交易不成功]");
									}
								}
								throw new Exception("玩家(" + deal.getPlayerB().getName() + ")装备栏发生变化，交易不成功(背包下标[" + indexes[i] + "][物品:" + cell.getEntityId() + "][数量:" + cell.getCount() + "] != 交易[物品:" + entityIds[i] + "][数量:" + counts[i] + "])");
							} else {
								nameBlist.add(ae.getArticleName());
								countBlist.add(counts[i]);
								indexBlist.add(indexes[i]);
								colorBlist.add(ae.getColorType());
							}
						}
					}
					
					if(deal instanceof ExchangeDeal){
						ExchangeDeal edeal = (ExchangeDeal)deal;
						boolean result  = edeal.deal(deal, player);
						if(result){
							dealMap.remove(deal.getId());
							agreed++;
						}
						return result;
					}
					// 先从玩家背包内删除交易物品
					indexes = deal.getIndexesA();
					counts = deal.getCountsA();
					pakTypes = deal.getPackageTypeA();
					for (int i = 0; i < indexes.length; i++) {
						int index = indexes[i];
						if (index != -1) {
							for (int j = 0; j < counts[i]; j++) {
								deal.getPlayerA().getKnapsacks_common()[pakTypes[i]].remove(index,"交易", false);
							}
						}
					}
					indexes = deal.getIndexesB();
					counts = deal.getCountsB();
					pakTypes = deal.getPackageTypeB();
					for (int i = 0; i < indexes.length; i++) {
						int index = indexes[i];
						if (index != -1) {
							for (int j = 0; j < counts[i]; j++) {
								deal.getPlayerB().getKnapsacks_common()[pakTypes[i]].remove(index,"交易", false);
							}
						}
					}
					// 测试各自的背包是否能容纳物品
					boolean putok = true;
					entityIds = deal.getEntityIdsA();
					counts = deal.getCountsA();
					List<ArticleEntity> elistA = new ArrayList<ArticleEntity>();
					for (int i = 0; i < entityIds.length; i++) {
						if (entityIds[i] != -1) {
							ArticleEntity entity = aem.getEntity(entityIds[i]);
							for (int j = 0; j < counts[i]; j++) {
								elistA.add(entity);
							}
						}
					}
					Knapsack[] knapB = playerB.getKnapsacks_common();
					if (!playerB.putAllOK(elistA.toArray(new ArticleEntity[0]))) {
						putok = false;
					}
					entityIds = deal.getEntityIdsB();
					counts = deal.getCountsB();
					List<ArticleEntity> elistB = new ArrayList<ArticleEntity>();
					for (int i = 0; i < entityIds.length; i++) {
						if (entityIds[i] != -1) {
							ArticleEntity entity = aem.getEntity(entityIds[i]);
							for (int j = 0; j < counts[i]; j++) {
								elistB.add(entity);
							}
						}
					}
					Knapsack[] knapA = playerB.getKnapsacks_common();
					if (!playerA.putAllOK(elistB.toArray(new ArticleEntity[0]))) {
						putok = false;
					}
					int coinsA = deal.getCoinsA();
					int coinsB = deal.getCoinsB();
					if (!putok) {
						// 返还各自物品
						ArticleEntity[] entityAs = elistA.toArray(new ArticleEntity[0]);
						for (ArticleEntity AA : entityAs) {
							playerA.putToKnapsacks(AA, "交易");
						}
						ArticleEntity[] entityBs = elistB.toArray(new ArticleEntity[0]);
						for (ArticleEntity BB : entityBs) {
							playerB.putToKnapsacks(BB,"交易");
						}
						player.send_HINT_REQ(Translate.text_deal_003);
						deal.getDstPlayer(player).send_HINT_REQ(Translate.text_deal_003);
						deal.resetCondition(deal.getPlayerA());
						deal.resetCondition(deal.getPlayerB());
						dealMap.remove(deal.getId());
	//					logger.info("[完成交易] [失败] [玩家背包无法装入物品] [" + deal.getId() + "][" + deal.getPlayerA().getUsername() + "][" + deal.getPlayerA().getId() + "] [" + deal.getPlayerA().getName() + "] [" + deal.getPlayerB().getUsername() + "][" + deal.getPlayerB().getId() + "][" + deal.getPlayerB().getName() + "]");
						if (!使用新日志格式) {
							if(logger.isInfoEnabled())
								logger.info("[完成交易] [失败] [玩家背包无法装入物品] [{}][{}][{}] [{}] [{}][{}][{}]", new Object[]{deal.getId(),deal.getPlayerA().getUsername(),deal.getPlayerA().getId(),deal.getPlayerA().getName(),deal.getPlayerB().getUsername(),deal.getPlayerB().getId(),deal.getPlayerB().getName()});
						} else {
							if (logger.isInfoEnabled()) {
								logger.info("[交易完成] [失败] [dealId:"+deal.getId()+"] [" + deal.getPlayerA().getLogString4Knap("A") + "] [" + deal.getPlayerB().getLogString4Knap("B") + "]");
							}
						}
						throw new KnapsackFullException(Translate.text_trade_021);
					} else {
	
						// 游戏币
						BillingCenter billingCenter = BillingCenter.getInstance();
						if (coinsA > 0) {
							int tax = (int)(coinsA*DEAL_TAX);
							if(tax == 0){
								tax = 1;
							}
							if (playerA.getSilver() < coinsA+tax || (coinsA+tax) < 0 || tax < 0) {
								throw new NoEnoughMoneyException(Translate.text_trade_022);
							}
						}
						if (coinsB > 0) {
							int tax = (int)(coinsB*DEAL_TAX);
							if(tax == 0){
								tax = 1;
							}
							
							if (playerB.getSilver() < coinsB+tax || (coinsB+tax) < 0 || tax < 0) {
								throw new NoEnoughMoneyException(Translate.text_trade_022);
							}
						}
						if (coinsA > 0) {
							int tax = (int)(coinsA*DEAL_TAX);
							if(tax == 0){
								tax = 1;
							}
							if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
								billingCenter.playerExpense(playerA, coinsA, CurrencyType.YINZI, ExpenseReasonType.DEAL_OUT, "交易给他人", -1);
								billingCenter.playerExpense(playerA, tax, CurrencyType.YINZI, ExpenseReasonType.DEAL_OUT, "交易给他人");
							} else {
								billingCenter.playerExpense(playerA, coinsA+tax, CurrencyType.YINZI, ExpenseReasonType.DEAL_OUT, "交易给他人");
							}
							NewChongZhiActivityManager.instance.doXiaoFeiActivity(playerA, tax, NewXiaoFeiActivity.XIAOFEI_TYPE_TAX);
//							ChongZhiActivity.getInstance().doXiaoFeiActivity(playerA, tax, XiaoFeiServerConfig.XIAOFEI_TONGDAO_JIAOYISHUI);
//							ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(playerA, tax, MoneyBillBoardActivity.XIAOFEI_TONGDAO_JIAOYISHUI);
							billingCenter.playerSaving(playerB, coinsA, CurrencyType.SHOPYINZI, SavingReasonType.DEAL_GET, "从他人那交易来");
						}
						if (coinsB > 0) {
							int tax = (int)(coinsB*DEAL_TAX);
							if(tax == 0){
								tax = 1;
							}if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
								billingCenter.playerExpense(playerB, coinsB, CurrencyType.YINZI, ExpenseReasonType.DEAL_OUT, "交易给他人", -1);
								billingCenter.playerExpense(playerB, tax, CurrencyType.YINZI, ExpenseReasonType.DEAL_OUT, "交易给他人");
							} else {
								billingCenter.playerExpense(playerB, coinsB+tax, CurrencyType.YINZI, ExpenseReasonType.DEAL_OUT, "交易给他人");
							}
							NewChongZhiActivityManager.instance.doXiaoFeiActivity(playerB, tax, NewXiaoFeiActivity.XIAOFEI_TYPE_TAX);
//							ChongZhiActivity.getInstance().doXiaoFeiActivity(playerB, tax, XiaoFeiServerConfig.XIAOFEI_TONGDAO_JIAOYISHUI);
//							ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(playerB, tax, MoneyBillBoardActivity.XIAOFEI_TONGDAO_JIAOYISHUI);
							billingCenter.playerSaving(playerA, coinsB, CurrencyType.SHOPYINZI, SavingReasonType.DEAL_GET, "从他人那交易来");
						}
						
						// 测试通过，进行交换
						playerB.putAll(elistA.toArray(new ArticleEntity[0]),"交易");
						playerA.putAll(elistB.toArray(new ArticleEntity[0]),"交易");
						//统计
						try{
								Transaction_FaceFlow transaction_FaceFlow = new Transaction_FaceFlow();
								transaction_FaceFlow.setZhenYing(CountryManager.得到国家名(playerA.getCountry()));
								transaction_FaceFlow.setFenQu(GameConstants.getInstance().getServerName());
								{
									HashMap<ArticleEntity, Integer> entityNum = new HashMap<ArticleEntity, Integer>();
									for (int i = 0; i < elistA.size(); i++) {
										Integer num = entityNum.get(elistA.get(i));
										if (num == null) {
											num = 0;
										}
										entityNum.put(elistA.get(i), num + 1);
									}
									StringBuffer sbA = new StringBuffer("");
									for (ArticleEntity en : entityNum.keySet()) {
										int num = entityNum.get(en);
										Article a = ArticleManager.getInstance().getArticle(en.getArticleName());
										sbA.append(a.getName_stat()).append("*").append(ArticleManager.getColorString(a, en.getColorType())).append("*").append(num);
										sbA.append(",");
									}
									String v = sbA.substring(0, sbA.length()-1);
									transaction_FaceFlow.setFdaoJu(v);
									transaction_FaceFlow.setFlevel(""+playerA.getSoulLevel());
									transaction_FaceFlow.setFmoney(coinsA);
									transaction_FaceFlow.setFquDao(playerA.getPassport().getLastLoginChannel());
									transaction_FaceFlow.setFuserName(playerA.getUsername());
									transaction_FaceFlow.setFvip(""+playerA.getVipLevel());
								}
								{
									HashMap<ArticleEntity, Integer> entityNum = new HashMap<ArticleEntity, Integer>();
									for (int i = 0; i < elistB.size(); i++) {
										Integer num = entityNum.get(elistB.get(i));
										if (num == null) {
											num = 0;
										}
										entityNum.put(elistB.get(i), num + 1);
									}
									StringBuffer sbB = new StringBuffer("");
									for (ArticleEntity en : entityNum.keySet()) {
										int num = entityNum.get(en);
										Article a = ArticleManager.getInstance().getArticle(en.getArticleName());
										sbB.append(a.getName_stat()).append("*").append(ArticleManager.getColorString(a, en.getColorType())).append("*").append(num);
										sbB.append(",");
									}
									if (sbB.length() > 1) {
										String v = sbB.substring(0, sbB.length()-1);
										transaction_FaceFlow.setTodaoJu(v);
									} else {
										transaction_FaceFlow.setTodaoJu("");
									}
									transaction_FaceFlow.setToLevel(""+playerB.getSoulLevel());
									transaction_FaceFlow.setToMoney(coinsB);
									transaction_FaceFlow.setToquDao(playerB.getPassport().getLastLoginChannel());
									transaction_FaceFlow.setToUserName(playerB.getUsername());
									transaction_FaceFlow.setToVip(""+playerB.getVipLevel());
								}
								StatClientService.getInstance().sendTransaction_FaceFlow("",transaction_FaceFlow);
								logger.warn("[交易统计] ["+transaction_FaceFlow.getFdaoJu()+"] ["+transaction_FaceFlow.getTodaoJu()+"]");
						}catch(Exception e){
							logger.error("交易统计出错:", e);
						}
						
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < deal.getEntityIdsA().length; i++) {
							sb.append(deal.getEntityIdsA()[i] + ",");
						}
						String entityA = sb.toString();
						if (entityA.length() > 0) {
							entityA = entityA.substring(0, entityA.length() - 1);
						}
						sb = new StringBuffer();
						for (int i = 0; i < deal.getCountsA().length; i++) {
							sb.append(deal.getCountsA()[i] + ",");
						}
						String countsA = sb.toString();
						if (countsA.length() > 0) {
							countsA = countsA.substring(0, countsA.length() - 1);
						}
						sb = new StringBuffer();
						for (int i = 0; i < deal.getEntityIdsB().length; i++) {
							sb.append(deal.getEntityIdsB()[i] + ",");
						}
						String entityB = sb.toString();
						if (entityB.length() > 0) {
							entityB = entityB.substring(0, entityB.length() - 1);
						}
						sb = new StringBuffer();
						for (int i = 0; i < deal.getCountsB().length; i++) {
							sb.append(deal.getCountsB()[i] + ",");
						}
						String countsB = sb.toString();
						if (countsB.length() > 0) {
							countsB = countsB.substring(0, countsB.length() - 1);
						}
						// 删除这个deal
						dealMap.remove(deal.getId());
						agreed++;
	//					logger.info("[完成交易] [成功] [" + deal.getId() + "] [" + deal.getPlayerA().getName() + ":{" + entityA + "/" + countsA + "/" + deal.getCoinsA() + "}] [" + deal.getPlayerB().getName() + ":{" + entityB + "/" + countsB + "/" + deal.getCoinsB() + "}]");
						if (!使用新日志格式) {
							if(logger.isInfoEnabled())
								logger.info("[交易完成] [成功] [交易A方:{}] [交易A方物品:{}] [交易A方物品数量:{}] [交易A方金钱:{}] [交易B方:{}] [交易B方物品:{}] [交易B方物品数量:{}] [交易B方金钱:{}]", new Object[]{deal.getPlayerA().getLogString(),entityA,countsA,deal.getCoinsA(),deal.getPlayerB().getLogString(),entityB,countsB,deal.getCoinsB()});
						} else {
							if (logger.isInfoEnabled()) {
								logger.info("[交易完成] [成功] [" + deal.getPlayerA().getLogString4Knap("A") + "] [A物品:" + entityA + "] [A物品数量:" + countsA + "] [A金钱:" + deal.getCoinsA() + "] [" + deal.getPlayerB().getLogString4Knap("B") + "] [B物品:" + entityB + "] [B物品数量:" + countsB + "] [B金钱:" + deal.getCoinsB() + "]");
							} 
						}
//						if(WhiteListManager.getInstance().isWhiteListPlayer(playerA)){
//							int colors[] = new int[colorAlist.size()];
//							int countss[] = new int[colorAlist.size()];
//							for(int i=0;i<colorAlist.size();i++){
//								colors[i] = colorAlist.get(i);
//								countss[i] = countAlist.get(i);
//							}
//							WhiteListManager.getInstance().addMailRowData(playerA, playerB, com.fy.engineserver.util.whitelist.WhiteListManager.ActionType.当面交易,(long)coinsA, nameAlist.toArray(new String[]{}), colors, countss,"");
//						}
//						if(WhiteListManager.getInstance().isWhiteListPlayer(playerB)){
//							int colors[] = new int[colorBlist.size()];
//							int countss[] = new int[colorBlist.size()];
//							for(int i=0;i<colorBlist.size();i++){
//								colors[i] = colorBlist.get(i);
//								countss[i] = countBlist.get(i);
//							}
//							WhiteListManager.getInstance().addMailRowData(playerB, playerA, com.fy.engineserver.util.whitelist.WhiteListManager.ActionType.当面交易,(long)coinsB, nameBlist.toArray(new String[]{}), colors, countss,"");
//						}
						//银子汇聚
						try {
							if((coinsA > 0 || coinsB > 0)) {
								Player down = null;
								Player up = null;
								int coins = 0;
								if(coinsA > coinsB) {
									down = deal.getPlayerA();
									up = deal.getPlayerB();
									coins = coinsA;
								} else {
									down = deal.getPlayerB();
									up = deal.getPlayerA();
									coins = coinsB;
								}
								MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
								if(msm != null) {
									msm.addMoneyMove(down, up, coins);
								}
								//logger.info("[trace_addMove] [addMove]");
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
						//物品汇聚
						try {
							if(elistA.size() > 0 || elistB.size() > 0) {
								Player down = null;
								Player up = null;
								int amount = 0;
								if(elistA.size() > elistB.size()) {
									down = deal.getPlayerA();
									up = deal.getPlayerB();
									amount = elistA.size();
								} else {
									down = deal.getPlayerB();
									up = deal.getPlayerA();
									amount = elistB.size();
								}
								ArticleRelationShipManager msm = ArticleRelationShipManager.getInstance();
								if(msm != null) {
									msm.addArticleMove(down, up, amount);
								}
								
								//打标记 物品面对面交易
								try
								{
									ArticleTradeRecord articleTradeRecord =  new ArticleTradeRecord();
									articleTradeRecord.setSellPlayerId(playerA.getId());
									articleTradeRecord.setBuyPlayerId(playerB.getId());
									
									long[] articleIdsA =  deal.getEntityIdsA();
									long[] articleIdsB =  deal.getEntityIdsB();
									long[] articleIds = new long[articleIdsA.length+articleIdsB.length];
									System.arraycopy(articleIdsA, 0, articleIds, 0, articleIdsA.length);
									System.arraycopy(articleIdsB, 0, articleIds, articleIdsA.length,articleIdsB.length );
									articleTradeRecord.setArticleIds(articleIds);
									
									articleTradeRecord.setDesc("面对面");
									articleTradeRecord.setTradeTime(System.currentTimeMillis());
									
									ArticleTradeRecordManager.getInstance().notifyNew(articleTradeRecord);
								}
								catch(Exception e)
								{
									ArticleTradeRecordManager.logger.error("[创建面对面交易记录] [失败] [出现未知异常] [playera:"+playerA.getId()+"] [playerb:"+playerB.getId()+"]",e);
								}
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
		
						
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void disagreeDeal(Deal deal, Player player) throws Exception {
		deal = getDeal(deal.getId());
		deal.setLockA(false);
		deal.setLockB(false);
	}

	@Override
	public void cancelDeal(Deal deal, Player player) {
		// 删除这个deal
		dealMap.remove(deal.getId());
		canceled++;
//		if (logger.isInfoEnabled()) logger.info("[cancelDeal] [" + deal.getId() + "][" + player.getUsername() + "][" + player.getId() + "][" + player.getName() + "]");
		if (!使用新日志格式) {
			if (logger.isInfoEnabled()) logger.info("[cancelDeal] [{}][{}][{}][{}]", new Object[]{deal.getId(),player.getUsername(),player.getId(),player.getName()});
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("[操作类别:交易取消] [dealId:" +deal.getId()+ "] ["+player.getLogString4Knap()+"]");
			}
		}
	}

	@Override
	public Deal createDeal(Player playerA, Player playerB) throws Exception {
		// 检查是否已经有这两个人的deal存在，有就返回现有的
		Deal deal = getDeal(playerA, playerB);
		if (deal != null) {
			return deal;
		}
		if (getDeal(playerA) != null || getDeal(playerB) != null) {
			playerA.sendError(Translate.text_deal_002);
			//throw new Exception("用户正在进行交易");
		}
		String id = StringUtil.randomIntegerString(8);
		while (getDeal(id) != null) {
			id = StringUtil.randomIntegerString(8);
		}
		deal = new Deal(playerA, playerB);
		deal.setId(id);
		dealMap.put(id, deal);
		total++;
//		if (logger.isInfoEnabled()) logger.info("[createDeal] [" + deal.getId() + "] [" + playerA.getUsername() + "][" + playerA.getId() + "][" + playerA.getName() + "] [" + playerB.getUsername() + "][" + playerB.getId() + "][" + playerB.getName() + "]");
		if (logger.isInfoEnabled()) logger.info("[createDeal] [{}] [{}][{}][{}] [{}][{}][{}]", new Object[]{deal.getId(),playerA.getUsername(),playerA.getId(),playerA.getName(),playerB.getUsername(),playerB.getId(),playerB.getName()});
		return deal;
	}

	@Override
	public Deal getDeal(String id) {
		return dealMap.get(id);
	}

	@Override
	public Deal getDeal(Player player) {
		// TODO Auto-generated method stub
		Deal deals[] = dealMap.values().toArray(new Deal[0]);
		for (Deal deal : deals) {
			if (deal.getPlayerA().getId() == player.getId() || deal.getPlayerB().getId() == player.getId()) {
				return deal;
			}
		}
		return null;
	}

	@Override
	public Deal getDeal(Player playerA, Player playerB) {
		// TODO Auto-generated method stub
		Deal deals[] = dealMap.values().toArray(new Deal[0]);
		for (Deal deal : deals) {
			if ((deal.getPlayerA().getId() == playerA.getId() && deal.getPlayerB().getId() == playerB.getId()) || (deal.getPlayerA().getId() == playerB.getId() && deal.getPlayerB().getId() == playerA.getId())) {
				return deal;
			}
		}
		return null;
	}

	@Override
	public boolean addArticle(Player player, byte pakType, int index, long entityId, int count) throws Exception {
		// TODO Auto-generated method stub
		if (index == -1 || entityId == -1 || count <= 0) {
			return false;
		}
		if (player.getKnapsack_common().getCell(index).getEntityId() != entityId) {
			return false;
		}
		if (player.getKnapsack_common().getCell(index).getCount() < count) {
			return false;
		}
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, entityId)){
			player.sendError(Translate.锁魂物品不能做此操作);
			return false;
		}
		Deal deal = getDeal(player);
		if (deal != null) {
			long entityIds[] = null;
			if (player.getId() == deal.getPlayerA().getId()) {
				entityIds = deal.getEntityIdsA();
			} else if (player.getId() == deal.getPlayerB().getId()) {
				entityIds = deal.getEntityIdsB();
			}
			boolean hasEmpty = false;
			for (int i = 0; i < entityIds.length; i++) {
				if (entityIds[i] == -1) {
					hasEmpty = true;
				}
			}
			if (hasEmpty) {
				boolean succ = deal.addArticle(player, pakType, index, entityId, count);
				if (succ) {
					deal.setLockB(false);
					deal.setLockA(false);
				}
//				if (logger.isInfoEnabled()) logger.info("[addArticle] [" + player.getUsername() + "][" + player.getId() + "][" + player.getName() + "] [" + index + "] [" + entityId + "] [" + count + "]");
				if (logger.isInfoEnabled()) logger.info("[addArticle] [{}][{}][{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),index,entityId,count});
				return succ;
			} else {
				throw new Exception("用户交易栏已满，不能再加入物品");
			}
		}
		return false;
	}

	@Override
	public boolean deleteArticle(Player player, int index) {
		// TODO Auto-generated method stub
		if (index == -1) {
			return false;
		}
		Deal deal = getDeal(player);
		if (deal != null) {
			boolean succ = deal.deleteArticle(player, index);
			if (succ) {
				deal.setLockB(false);
				deal.setLockA(false);
			}
//			if (logger.isInfoEnabled()) logger.info("[deleteArticle] [" + player.getUsername() + "][" + player.getId() + "][" + player.getName() + "] [" + index + "]");
			if (logger.isWarnEnabled()) logger.info("[deleteArticle] [{}][{}][{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),index});
			return succ;
		}
		return false;
	}

	@Override
	public boolean updateCoins(Player player, int coins) {
		int tax = (int)(coins*DEAL_TAX);
		if(tax == 0 && coins > 0){
			tax = 1;
		}
		if (player.getSilver() < coins + tax || (coins + tax) < 0) {
			return false;
		}
		Deal deal = getDeal(player);
		if (deal != null) {
			if (player.getId() == deal.getPlayerA().getId()) {
				deal.setCoinsA(coins);
			} else {
				deal.setCoinsB(coins);
			}
			logger.warn("[updateCoins] [{}] [{}] [{}] [{}] [A={}] [B={}]", new Object[]{deal.getId(),player.getUsername(),player.getId(),player.getName(), deal.getCoinsA(), deal.getCoinsB()});
			deal.setLockB(false);
			deal.setLockA(false);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateConditions(Deal deal, Player player, int indexes[], long entityIds[], int counts[], int coins) throws Exception {
		// TODO Auto-generated method stub
		// 重置条件
		deal = getDeal(deal.getId());
		if ((deal.getPlayerA().getId() == player.getId() && deal.isAgreedA()) || (deal.getPlayerB().getId() == player.getId() && deal.isAgreedB())) {
			throw new Exception("玩家处于同意状态，无法修改条件，请先取消同意状态");
		}
		deal.resetCondition(player);
		if (player.getId() == deal.getPlayerA().getId()) {
			deal.setIndexesA(indexes);
			deal.setEntityIdsA(entityIds);
			deal.setCountsA(counts);
			deal.setCoinsA(coins);
		} else {
			deal.setIndexesB(indexes);
			deal.setEntityIdsB(entityIds);
			deal.setCountsB(counts);
			deal.setCoinsB(coins);
		}
		deal.setAgreedA(false);
		deal.setAgreedB(false);
		// 通知玩家
//		if (logger.isInfoEnabled()) logger.info("[updateConditions] [" + deal.getId() + "] [" + player.getUsername() + "][" + player.getId() + "][" + player.getName() + "] [" + coins + "]");
		if (logger.isWarnEnabled()) logger.info("[updateConditions] [{}] [{}][{}][{}] [{}]", new Object[]{deal.getId(),player.getUsername(),player.getId(),player.getName(),coins});
		return true;
	}

	@Override
	public void lockDeal(Deal deal, Player player, boolean isLock) {
		if (logger.isWarnEnabled()) logger.warn("[lockDeal] [{}] [{}] [{}] [{}]", new Object[]{deal.getId(),player.getUsername(),player.getId(),player.getName()});
		if (deal.getPlayerA().getId() == player.getId()){
			deal.setLockA(isLock);
		}else{
			deal.setLockB(isLock);
		}
	}

	public static void main(String[] args) {
		System.out.println(450000000 * 5 / 100);
	}
}
