/**
 * 
 */
package com.fy.engineserver.economic.thirdpart.migu;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.auction.AuctionInfo4Client;
import com.fy.engineserver.auction.service.AuctionManager;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.MoneyProps;
import com.fy.engineserver.datasource.article.data.props.PetEggProps;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.AritcleInfoHelper;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecord;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecordManager;
import com.fy.engineserver.economic.thirdpart.migu.entity.SaleRecord;
import com.fy.engineserver.economic.thirdpart.migu.entity.SaleRecordManager;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.marriage.MarriageInfo;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.VALIDATE_DEVICE_INFO_REQ;
import com.fy.engineserver.message.VALIDATE_DEVICE_INFO_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.fy.boss.message.BossMessageFactory;
import com.fy.boss.message.MIGU_COMMUNICATE_REQ;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.Transfer_PlatformFlow;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 这个类主要负责获取一切米谷想获得的信息
 *
 */
public class MiGuTradeServiceWorker {
	
	public static org.slf4j.Logger logger = MiguSaleService.logger;
	
	public static int MIGU_SELL_TAX = 5;
	
	public static BillingCenter billingCenter = BillingCenter .getInstance();
	
	/**
	 * 1 普通道具 2 宝石  3 宠物蛋 4 法宝
	 */
	
	public static int PUTONG_DAOJU_TYPE = 1;
	public static int BAOSHI_TYPE = 2;
	public static int CHONGWU_DAN_TYPE = 3;
	public static int FABAO_TYPE = 4;
	public static int CHIBANG = 5;
	
	
	public static long 银锭最低交易额度 = 1*1000*1000l;
	public static long 银锭最高交易额度 = 100*1000*1000l;
	
	public static int 游戏道具最低单笔交易额 = 10 * 100;
	public static int 游戏角色上架最低金额 = 10 * 100;
	
	public static long[] 角色交易最高携带银两额度 = new long[]{10*1000*1000,30*1000*1000,50*1000*1000};
	
	public static long 获取角色交易最多银两数(int level) {
		int index = 0;
		if (level < 150) {
			index = 0; 
		} else if (level < 190) {
			index = 1;
		} else {
			index = 2;
		}
		return 角色交易最高携带银两额度[index];
	}
	
	/**
	 * 验证设备合法性
	 * @param infos
	 * @return
	 */
	public static String validateDevice4Salerole(String clientId, String username, String mac){
		if (username == null || username.isEmpty()) {
			return "数据异常";
		}
		if (mac == null) {
			mac = "";
		}
		if (clientId == null) {
			clientId = "";
		}
		String[] opInfo = new String[3];
		opInfo[0] = "VALIDATE_DEVICE_FOR_SALE_ROLE";
		opInfo[1] = username;
		opInfo[2] = clientId;
		MieshiGatewayClientService gatewayClientService = MieshiGatewayClientService.getInstance();
		
		VALIDATE_DEVICE_INFO_REQ req = new VALIDATE_DEVICE_INFO_REQ(GameMessageFactory.nextSequnceNum(), opInfo);
		try {
			VALIDATE_DEVICE_INFO_RES res = (VALIDATE_DEVICE_INFO_RES) gatewayClientService.sendMessageAndWaittingResponse(req, 60 * 1000);
			if (res == null) {
				logger.warn("[米谷交易] [验证设备合法性] [出错] [网关返回协议为null] ["+username+"]");
				return "验证合法设备失败";
			}
			String[] infoes = res.getInfos();
			if(infoes.length > 0)
			{
				if("true".equals(infoes[0]))
				{
					logger.warn("[米谷交易] [验证设备合法性] [成功] [是合法设备] ["+username+"]");
					return null;
				} else {
					logger.warn("[米谷交易] [验证设备合法性] [失败] [result:"+infoes[0]+"] ["+username+"]");
					return infoes[0];
				}
				
			}
		} catch (Exception e) {
			logger.error("[米谷交易] [验证设备合法性] [出错] [出现未知异常] ["+username+"]",e);
		}
		return "验证合法设备异常";
	}
	/**
	 * 验证用户的设备合法性
	 * 0 是无效
	 * 1 是有效
	 */
	public static int validateDevice(String[] infos)
	{
		String username = infos[0];
		MieshiGatewayClientService gatewayClientService = MieshiGatewayClientService.getInstance();
		
		VALIDATE_DEVICE_INFO_REQ req = new VALIDATE_DEVICE_INFO_REQ(GameMessageFactory.nextSequnceNum(), infos);
		try {
			VALIDATE_DEVICE_INFO_RES res = (VALIDATE_DEVICE_INFO_RES) gatewayClientService.sendMessageAndWaittingResponse(req, 60 * 1000);
			if (res == null) {
				logger.warn("[米谷交易] [验证设备合法性] [出错] [网关返回协议为null] ["+username+"]");
				return 0;
			}

			String[] infoes = res.getInfos();
			if (infoes == null) {
				logger.warn("[米谷交易] [验证设备合法性] [出错] [返回infos为null] ["+username+"]");
				return 0;
			}
			
			if(infoes.length > 0)
			{
				if("true".equals(infoes[0]))
				{
					logger.warn("[米谷交易] [验证设备合法性] [成功] [是合法设备] ["+username+"]");
					return 1;
				}
				else
				{
					logger.warn("[米谷交易] [验证设备合法性] [失败] [不是合法设备] ["+username+"]");
					return 0;
				}
			}
			else
			{
				logger.warn("[米谷交易] [验证设备合法性] [出错] [返回infos长度为0] ["+username+"]");
				return 0;
			}
		}
		catch(Exception e)
		{
			logger.error("[米谷交易] [验证设备合法性] [出错] [出现未知异常] ["+username+"]",e);
			return 0;
		}
			
	}
	
	
/**
 * 根据player查询可出售的所有物品列表
 * @param p 角色
 * @param type 物品的类型：1 普通道具 2 宝石  3 宠物蛋 4 法宝
 * @return
 */
	public static List<SimpleArticle> queryArticles(Player p,int type)
	{
		
		
		/**
		 * 首先拿到player身上的背包
		 * 将背包中的物品做一次判断，看是否可售，可售则放入物品列表中
		 */
		List<SimpleArticle> lst = new ArrayList<MiGuTradeServiceWorker.SimpleArticle>();
		if(p == null)
		{
			logger.warn("[查询装备] [失败] [fail] [角色为空]");
			return lst;
		}
		
		
		Knapsack knapsack = p.getKnapsack_common();
		
		Cell[] cells = knapsack.getCells();
		
		for(int i=0; i<cells.length; i++)
		{
			Cell cell = cells[i];
			ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
			if(articleEntity != null)
			{
				if(isCanPublish(p, articleEntity, i, cell.count))
				{
					SimpleArticle simpleArticle = buildSimpleArticle(i, articleEntity,cell.count);
					if(logger.isInfoEnabled())
					{
						logger.info("[查询装备] [装备可以发布] ["+articleEntity.getArticleName()+"] ["+articleEntity.getId()+"] ["+cell.count+"] ["+i+"]");
					}
					lst.add(simpleArticle);
				}
				else
				{
					logger.warn("[查询装备] [装备不可以发布] ["+articleEntity.getArticleName()+"] ["+articleEntity.getId()+"] ["+cell.count+"] ["+i+"]");
				}
			}
			else
			{
				logger.warn("[查询装备] [失败] [没有找到任何装备] ["+cell.getEntityId()+"] ["+cell.count+"] ["+i+"]");
			}
		}
		
		return lst;
		
	}
	
	
	public static String getArticleInfo(Player player,long articleId)
	{

		ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(articleId);
		String description = "";
		if (articleEntity == null) {
			description = Translate.text_2690;
		} else {
			description = AritcleInfoHelper.generate(player, articleEntity,true);
		}
		
		return description;
	}
	/**
	 * 上架角色
	 * @param player
	 */
	public static boolean doSale4Player(Player player, SaleRecord saleRecord) {
		try {
			SaleRecordManager saleRecordManager = SaleRecordManager.getInstance();
			saleRecordManager.createSaleRecord(saleRecord);
			return true;
		} catch (Exception e) {
			logger.warn("[上架角色] [创建订单] [异常] [" + player.getLogString() + "] [recordId:" + saleRecord.getId() + "]", e );
		}
		return false;
	}
	
	/**
	 * 这个方法主要是根据米谷那边的定价进行一些规则的进一步制定，在这里会将公示期返回给米谷
	 * 同时会将交易情况储存到数据库中
	 * @param player
	 * @param lst
	 * @return
	 */
	
	public static List<SimpleArticle> doSale( Player player,List<SaleRecord> lst)
	{
		/**
		 * 首先验证销售是否合法
		 * 然后从背包中将数据删除
		 * 然后将销售记录放入数据库
		 * 最后返回给米谷合法的article
		 */
		Knapsack knapsack = player.getKnapsack_common();
		SaleRecordManager saleRecordManager = SaleRecordManager.getInstance();
		List<SimpleArticle> simpleArticles = new ArrayList<MiGuTradeServiceWorker.SimpleArticle>();
		Iterator<SaleRecord> it = lst.iterator();
		
		while(it.hasNext())
		{
			//了解saleDay的含义
			
			SaleRecord saleRecord = it.next();
			
			if(saleRecord.getArticleId() != 200000l)
			{
				ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(saleRecord.getArticleId());
				boolean isCanSale = isCanSale(player, articleEntity, saleRecord.getCellIndex(), saleRecord.getGoodsCount(), saleRecord.getPayMoney(), 0);
				if(isCanSale)
				{
					int unSaleOrderNum = MiGuTradeServiceWorker.getSaleRecordNum4NoSaled4Article(player.getId());
					boolean deductSilver = false;
					if(unSaleOrderNum >= MiGuTradeServiceWorker.LIMIT_UNSALE_ARTICLE_ORDER_NUM)
					{
						deductSilver = true;
					}
					
					boolean isCanPub = true;
					
					if(deductSilver)
					{
						synchronized (player) {
							try
							{
								billingCenter.playerExpense(player, MiGuTradeServiceWorker.物品挂单费用, CurrencyType.YINZI, ExpenseReasonType.米谷交易, "米谷玩家挂卖物品收取手续费");
							}
							catch(Exception e)
							{
								logger.error("[米谷通讯] [扣除物品挂单费用] [失败] [出现未知异常] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"]",e);
								isCanPub = false;
							}
						}
					}
					Cell cell = knapsack.getCell(saleRecord.getCellIndex());
					if (MiguSaleService.复制物品bug修复 ) {
						if (cell == null || cell.getEntityId() <= 0 || cell.getCount() <= 0) {
							it.remove();
							logger.warn("[米谷通讯] [物品挂单] [对应格子没有道具] [" + player.getLogString() + "] [saleRecord.getCellIndex():" + saleRecord.getCellIndex() + "]");
							continue;
						}
					}
					SimpleArticle simpleArticle = buildSimpleArticle(saleRecord.getCellIndex(), articleEntity,cell.count);
					if(isCanPub)
					{
						synchronized (player) {
							int clearResult = knapsack.clearCell(saleRecord.getCellIndex(), "米谷交易", false);
							if (MiguSaleService.复制物品bug修复 && clearResult <= 0) {
								it.remove();
								logger.warn("[米谷通讯] [物品挂单] [挂单商品格子出现错误] [" + player.getLogString() + "] [clearResult:" + clearResult + "] [saleRecord.getCellIndex():" + saleRecord.getCellIndex() + "]");
								continue;
							}
						}
						saleRecordManager.createSaleRecord(saleRecord);
						simpleArticles.add(simpleArticle);
					}
					else
					{
						it.remove();
						logger.warn("[米谷通讯] [物品挂单] [出现异常不能允许挂单的情况] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"] ["+saleRecord.getId()+"] ["+saleRecord.getArticleId()+"]");
						continue;
					}
					
					//通知统计系统
					try
					{
						StatClientService statClientService = StatClientService.getInstance();
						 Transfer_PlatformFlow flow=new Transfer_PlatformFlow();
			  			 
			  			 flow.setArticleColor(simpleArticle.color);
			  			 flow.setArticleId(simpleArticle.id+"");
			  			 flow.setArticleName(simpleArticle.articleName);
			  			 flow.setBuyPassportChannel("");
			  			 flow.setBuyPassportId("");
			  			 flow.setBuyPassportName("");
			  			 flow.setBuyPlayerId("");
			  			 flow.setBuyPlayerLevel("");
			  			 flow.setBuyPlayerName("");
			  			 flow.setBuyPlayerVipLevel("");
			  			 
			  			 flow.setCellIndex(simpleArticle.cellIndex+"");
			  			 flow.setCreateTime(System.currentTimeMillis());
			  			 
			  			 flow.setGoodsCount(saleRecord.getGoodsCount()+"");
			  			 flow.setId(saleRecord.getId()+"");
			  			 flow.setPayMoney(saleRecord.getPayMoney());
			  			 flow.setResponseResult(saleRecord.getResponseResult()+"");
			  			 flow.setSellPassportChannel(saleRecord.getUserChannel());
			  			 flow.setSellPassportId(saleRecord.getSellPassportId()+"");
			  			 flow.setSellPassportName(player.getPassport().getUserName());
			  			 flow.setSellPlayerId(saleRecord.getSellPlayerId()+"");
			  			 flow.setSellPlayerLevel(player.getLevel()+"");
			  			 flow.setSellPlayerName(player.getName());
			  			 flow.setSellVipLevel(player.getVipLevel()+"");
			  			 flow.setServerName(GameConstants.getInstance().getServerName());
			  			 flow.setTradeMoney(0);
			  			 flow.setTradeType(saleRecord.getTradeType()+"");
			  			 
			  			 if(deductSilver && isCanPub)
			  			 {
			  				 flow.setArticleSalePaySilver(MiGuTradeServiceWorker.物品挂单费用+"");
			  			 }
			  			 else
			  			 {
			  				 flow.setArticleSalePaySilver(0+"");
			  			 }
			  			 flow.setCancelSaleSilver("0");
			  			 
			  			 flow.setColumn1("0");
			  			 flow.setColumn2("");
			  			 flow.setColumn3("");
			  			 flow.setColumn4("");
			  			 
			  			 
			           statClientService.sendTransfer_PlatformFlow("",flow);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			else
			{
				int countSale = MiGuTradeServiceWorker.getSaleRecordNum4NoSaled4Silver(player.getId());
				if( countSale >= MiGuTradeServiceWorker.LIMIT_UNSALE_SILVER_ORDER_NUM)
				{
					logger.warn("[接受米谷通知] [银两定价] [银两挂单数量已经超过限制] [跳过] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [已经挂单数量:"+countSale+"]");
					continue;
				}
				
				saleRecordManager.createSaleRecord(saleRecord);
				
				try {
					long prePaySilver = MiGuTradeServiceWorker.getPrePaySilver(saleRecord.getGoodsCount());
					saleRecord.setPrepayfee(prePaySilver);
					saleRecordManager.update(saleRecord);
					MiGuTradeServiceWorker.billingCenter.playerExpense(player,saleRecord.getGoodsCount() + prePaySilver, CurrencyType.YINZI, ExpenseReasonType.米谷交易, "米谷玩家挂卖银锭");
				
					//通知统计系统
					try
					{
						StatClientService statClientService = StatClientService.getInstance();
						 Transfer_PlatformFlow flow=new Transfer_PlatformFlow();
			  			 
			  			 flow.setArticleColor("");
			  			 flow.setArticleId("");
			  			 flow.setArticleName("银两");
			  			 flow.setBuyPassportChannel("");
			  			 flow.setBuyPassportId("");
			  			 flow.setBuyPassportName("");
			  			 flow.setBuyPlayerId("");
			  			 flow.setBuyPlayerLevel("");
			  			 flow.setBuyPlayerName("");
			  			 flow.setBuyPlayerVipLevel("");
			  			 
			  			 flow.setCellIndex("");
			  			 flow.setCreateTime(System.currentTimeMillis());
			  			 
			  			 flow.setGoodsCount(saleRecord.getGoodsCount()+"");
			  			 flow.setId(saleRecord.getId()+"");
			  			 flow.setPayMoney(saleRecord.getPayMoney());
			  			 flow.setResponseResult(saleRecord.getResponseResult()+"");
			  			 flow.setSellPassportChannel(saleRecord.getUserChannel());
			  			 flow.setSellPassportId(saleRecord.getSellPassportId()+"");
			  			 flow.setSellPassportName(player.getPassport().getUserName());
			  			 flow.setSellPlayerId(saleRecord.getSellPlayerId()+"");
			  			 flow.setSellPlayerLevel(player.getLevel()+"");
			  			 flow.setSellPlayerName(player.getName());
			  			 flow.setSellVipLevel(player.getVipLevel()+"");
			  			 flow.setServerName(GameConstants.getInstance().getServerName());
			  			 flow.setTradeMoney(0);
			  			 flow.setTradeType(saleRecord.getTradeType()+"");
			  			 
			  			 flow.setArticleSalePaySilver(0+"");
			  			 flow.setCancelSaleSilver("0");
			  			 
			  			 flow.setColumn1(prePaySilver+"");
			  			 flow.setColumn2("");
			  			 flow.setColumn3("");
			  			 flow.setColumn4("");
			  			 
			  			 
			           statClientService.sendTransfer_PlatformFlow("",flow);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				
				} catch (NoEnoughMoneyException e) {
					logger.error("[米谷通讯] [扣除物品挂单费用] [失败] [银两不足] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"]");
					it.remove();
					continue;
				} catch (BillFailedException e) {
					logger.error("[米谷通讯] [扣除物品挂单费用] [失败] [充值失败] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"]");
					it.remove();
					continue;
				}
				catch (Exception e) {
					logger.error("[米谷通讯] [扣除物品挂单费用] [失败] [出现未知异常] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"]");
					it.remove();
					continue;
				}
				
	
	
				
			}
		}
		
		return simpleArticles;
	}
	
	/**
	 * 这是为了根据米谷传来的字符串拼装出销售记录，然后储存
	 * @param saleString
	 * @return
	 */
	public static List<SaleRecord> parseSaleString(String saleString)
	{
		//TODO:需要协商一下真正的具体格式
		return null;
	}
	
	
	/**
	 * 是否可发布，告知米谷可出售的物品列表
	 * 在这里没有考虑税的概念，也没有考虑用户的银两和税的比较
	 * @param player
	 * @param articleEntity
	 * @param cellIndex
	 * @param count
	 * @return
	 */
	public static long MIGU_TRADE_LIMIT_TIME = 3*24*3600*1000l;
	
	public static String[] 不可发布道具名 = new String[]{"仙气钥匙","精致仙气钥匙","稀有仙气钥匙"};
	
	public static boolean isCanPublish(Player player, ArticleEntity articleEntity,int cellIndex,long count)
	{
		//TODO:注意公示期的判断，需要再添加物品的有效期 > 公示期+出售期 物品的有效期至少超过两个星期
		
		Knapsack knapsack = player.getKnapsack_common();
		if (player.getLevel() < 打开米谷需要等级) {
			return false;
		}
		if (player.isAppStoreChannel() && player.getLevel() < 打开米谷需要等级) {
			return false;
		}
		if(knapsack==null){
			return false;
		}
		
		ArticleEntity ae = knapsack.getArticleEntityByCell(cellIndex);
		if(ae==null){
			return false;
		}
		if (ae.isBinded() || ae.isRealBinded()) {
			return false;
		}
		
		for (int i=0; i<不可发布道具名.length; i++) {
			if (不可发布道具名[i].equalsIgnoreCase(ae.getArticleName())) {
				return false;
			}
		}
		if(ae.getId() != articleEntity.getId())
		{
			return false;
		}
		
		Article article = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
		if(article==null){
			return false;
		}
		
		//银子类商品不能交易
		if(article instanceof MoneyProps)
		{
			return false;
		}
		
		if (!GreenServerManager.canToOtherPlayer(articleEntity)) {
			return false;
		}
		
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, articleEntity.getId())){
			return false;
		}
		
		if(count <= 0)
		{
			return false;
		}
		
		if(knapsack.getCell(cellIndex).getCount()<count){
			return false;
		}
		
		if (articleEntity.getEndTime() > 0 && articleEntity.getEndTime() - System.currentTimeMillis() <= 23 * 24 * 60 * 60 * 1000) {
			return false;
		}
		
		
//		List<ArticleTradeRecord> lst =	MiGuTradeServiceWorker.queryTradeRecordeByArticleId4Migu(player.getId(), articleEntity.getId());
//		
//		
//		for(int i = 0; i < lst.size(); i++)
//		{
//			
//			if(lst.get(i) != null)
//			{
//				long[] articleIds = lst.get(i).getArticleIds();
//				
//				if(articleIds != null && articleIds.length > 0)
//				{
//					
//					for(long id : articleIds)
//					{
//						if(id == articleEntity.getId())
//						{
//								if ((System.currentTimeMillis() - lst.get(i).getTradeTime()) <=  MIGU_TRADE_LIMIT_TIME)
//								{
//									return false;
//								}
//						}
//					}
//				}
//			}
//		
//		}
		
		
		
		logger.warn("[装备是否可发布] [成功] [ok] ["+player.getLogString()+"] ["+articleEntity.getId()+"] ["+articleEntity.getArticleName()+"] ["+cellIndex+"] ["+count+"]");
		
		return true;
	}
	
	public static boolean isCanSale(Player player, ArticleEntity articleEntity, int cellIndex,long count,long salePrice,int saleDay)
	{
		//TODO:需要加一些具体的规则限制，要比发布更加细化的一个规则
		
		boolean isPublish = isCanPublish(player, articleEntity, cellIndex, count);
		if(isPublish)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean validata4salerole(String saleusername,String platform,String channel) {
		try {
			if (saleusername == null || saleusername.isEmpty()) {
				return false;
			}
			if(!saleusername.equalsIgnoreCase(GameConstants.getInstance().getServerName()))
			{
				logger.warn("[购买角色] [验证角色] [当前服务器和传入服务器不匹配] ["+GameConstants.getInstance().getServerName()+"] ["+saleusername+"]");
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.warn("[购买角色] [验证异常] [" + saleusername + "] [" + platform + "] [" + channel + "]", e);
		}
		return false;
	}
	
	/**
	 * 此方法用来验证用户名和角色id是否匹配
	 * @param username
	 * @param roleId
	 * @return
	 */
	public static boolean validateRole(String username,long roleId,String serverName,String platform,String channel)
	{
			//TODO 加日志 加一个判断设备，需要判断设备是否是注册时的设备，如果不是，是否是最近30天一直在用的
		try
		{
			if(StringUtils.isEmpty(username))
			{
				return false;
			}
			
			if(StringUtils.isEmpty(serverName))
			{
				return false;
			}
			
			if(!serverName.equalsIgnoreCase(GameConstants.getInstance().getServerName()))
			{
				logger.warn("[米谷通讯] [验证角色] [当前服务器和传入服务器不匹配] ["+GameConstants.getInstance().getServerName()+"] ["+serverName+"] ["+roleId+"]");
				return false;
			}
			//验证当前的平台是否是现在的平台
			if(!GameConstants.getInstance().getPlatForm().equalsIgnoreCase(platform))
			{
				logger.warn("[米谷通讯] [验证角色] [当前平台和传入平台不匹配] ["+GameConstants.getInstance().getPlatForm()+"] ["+platform+"] ["+roleId+"]");
				return false;
			}
			//验证当前服务器的玩家账号和角色id是否匹配

			PlayerManager playerManager = PlayerManager.getInstance();
			Player[] players = playerManager.getPlayerByUser(username);
			if(players == null || players.length <= 0)
			{
				logger.warn("[米谷通讯] [没有获得任何角色] ["+roleId+"]");
				return false;
			}
			
			for(int i=0; i<players.length; i++)
			{
				Player player = players[i];
				if(player == null)
				{
					logger.warn("[米谷通讯] [验证角色] [出现空角色] ["+roleId+"]");
					continue;
				}
				else
				{
					if( roleId == player.getId())
					{
						logger.info("[米谷通讯] [验证角色] [成功] ["+username+"] ["+roleId+"] ["+platform+"] ["+channel+"]");
						return true;
					}
					else
					{
						logger.warn("[米谷通讯] [验证角色] [角色id和游戏服当前角色id不匹配] ["+roleId+"] ["+player.getId()+"]");
						continue;
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.warn("[验证角色] [失败] [出现未知异常] ["+username+"] ["+roleId+"] ["+platform+"] ["+channel+"]",e);
			return false;
		}
		logger.warn("[验证角色] [失败] [可能是编程逻辑问题] ["+username+"] ["+roleId+"] ["+platform+"] ["+channel+"]");
		return false;
	}
	
	
	public static SimpleArticle buildSimpleArticle(int cellIndex,ArticleEntity articleEntity,int articleCount)
	{
		ArticleManager articleManager = ArticleManager.getInstance();
		Article article = articleManager.getArticle(articleEntity.getArticleName());
		
		SimpleArticle simpleArticle = new SimpleArticle();
			
		simpleArticle.id = articleEntity.getId();
		simpleArticle.articleName = articleEntity.getArticleName();
		simpleArticle.iconName = article.getIconId();
		simpleArticle.maxValidDays = article.getMaxValidDays();
		simpleArticle.articleEntityType = article.getArticleType();
		if(article instanceof Equipment){
			simpleArticle.articleLevel = ((Equipment)article).getPlayerLevelLimit();
		}else if(article instanceof PetEggProps){
			simpleArticle.articleLevel = ((PetEggProps)article).getArticleLevel();
		}else if(article instanceof Props){
			simpleArticle.articleLevel = ((Props)article).getLevelLimit();
		}else{
			simpleArticle.articleLevel = 1;
		}
		
		
		
		simpleArticle.cellIndex = cellIndex;
		simpleArticle.color = ArticleManager.getColorString(article, articleEntity.getColorType());
		
		simpleArticle.count = articleCount;
		//如果是宝石
		if(article instanceof InlayArticle)
		{
			simpleArticle.type = BAOSHI_TYPE;
		}
		//如果是宠物蛋
		else if(article instanceof PetEggProps)
		{
			simpleArticle.type = CHONGWU_DAN_TYPE;
		}
		//如果是法宝
		else if(article instanceof MagicWeapon)
		{
			MagicWeapon magicWeapon = (MagicWeapon) article;
			simpleArticle.career = CareerManager.careerNameByType(magicWeapon.getCareertype());
			simpleArticle.type = FABAO_TYPE;
		}
		else if(article.getArticleType() == Article.ARTICLE_TYPE_WING)
		{
			simpleArticle.type = CHIBANG;
		}
		else 
		{
			if(article instanceof Equipment)
			{
				Equipment equipment = (Equipment) article;
				simpleArticle.career = CareerManager.careerNameByType(equipment.getCareerLimit());
			}
			simpleArticle.type = 1;
		}
		
		return simpleArticle;
	}
	
	
	/**
	 * 创建一个简单物品对象，这个道具对象用来负责给米谷
	 * 它舍弃了一些信息，只是简单的提供米谷需要的信息而已
	 */
	public static class SimpleArticle
	{
		public long id;
		public String articleName;
		public String iconName;
		public long maxValidDays;
		public int articleLevel;
		public int type;
		public int cellIndex;
		public long publishTime;
		public String career="";
		public String color="";
		public int count = 1;
		public int articleEntityType;
		
	}
	
	
	
	/**
	 * 创建一个简单道具对象，这个道具对象用来负责给米谷
	 * 它舍弃了一些信息，只是简单的提供米谷需要的信息而已
	 */
	public static class SimpleProps extends SimpleArticle
	{
		
	}
	
	/**
	 * 创建一个简单装备对象，这个道具对象用来负责给米谷
	 * 它舍弃了一些信息，只是简单的提供米谷需要的信息而已
	 */
	public static class SimpleEquip extends SimpleArticle
	{
		public int durability;
		public String career;
	}
	
	public static class SimpleSaleInfo
	{
		public String recordid;
		public long equiponlyid;
		public long showtime;
		public int cellIndex;
		public int isValidDevice;
	}
	
	public static class SimpleSaleInfo4Role {
		public String recordid;
		public long showtime;
		public String reason;
	}
	
	public static long 内部交易公示期基准 = 3*24*3600*1000l;
	
	public static long 角色交易公示期时间 = 24 * 60 * 60L;
	
	public static SimpleSaleInfo4Role buildSimpleSaleInfo4Role(SaleRecord saleRecord,String[]otherInfos,String reason) {
		SimpleSaleInfo4Role simpleSaleInfo = new SimpleSaleInfo4Role();
		simpleSaleInfo.recordid = saleRecord.getId()+"";
		simpleSaleInfo.showtime = 角色交易公示期时间;
		simpleSaleInfo.reason = reason;
		return simpleSaleInfo;
	}
	
	public static SimpleSaleInfo buildSimpleSaleInfo(SaleRecord saleRecord,String[]otherInfos)
	{
		SimpleSaleInfo simpleSaleInfo = new SimpleSaleInfo();
		simpleSaleInfo.recordid = saleRecord.getId()+"";
		simpleSaleInfo.equiponlyid = saleRecord.getArticleId();
		if(simpleSaleInfo.equiponlyid != 200000l)
		{
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(simpleSaleInfo.equiponlyid);
			
			List<ArticleTradeRecord> lst =  queryTradeRecordeByArticleId4NotMigu(saleRecord.getSellPlayerId(), ae.getId());
			
			boolean addBaseShowTime = false;
			long gapTime = 0l;
			int validateDeviceResult  = 0;
			String username = otherInfos[0];
			String mac = otherInfos[1];
			String[] infos = new String[]{username,mac};
			validateDeviceResult = validateDevice(infos);
			
			
			if(lst != null && lst.size() > 0)
			{
				long lastTradeTime = 0l;
				for(ArticleTradeRecord articleTradeRecord : lst)
				{
					long tradeTime = articleTradeRecord.getTradeTime();
					if(tradeTime > lastTradeTime)
					{
						lastTradeTime  = tradeTime;
					}
				}
		
				if(lastTradeTime != 0)
				{
					gapTime = System.currentTimeMillis() - lastTradeTime;
					if(gapTime <= 内部交易公示期基准)
					{
						addBaseShowTime = true;
					}
				}
			}
			
			simpleSaleInfo.showtime = getShowTime(ae);
			
			if(validateDeviceResult == 1)
			{
				if(addBaseShowTime)
				{
					simpleSaleInfo.showtime =simpleSaleInfo.showtime + 内部交易公示期基准/1000-gapTime/1000;
				}
			}
			else if(validateDeviceResult == 0)
			{
				simpleSaleInfo.showtime = simpleSaleInfo.showtime + 内部交易公示期基准/1000;
			}
			simpleSaleInfo.isValidDevice = validateDeviceResult;
		}
		simpleSaleInfo.cellIndex=saleRecord.getCellIndex();
		
		return simpleSaleInfo;
	}
	

	
	public static long getShowTime(ArticleEntity ae)
	{
		ArticleManager articleManager = ArticleManager.getInstance();
		Article article = ArticleManager.getInstance().getArticle(ae.getArticleName());
		
		if(article instanceof InlayArticle)
		{
			return 6*60*60;
		}
		//如果是宠物蛋
		else if(article instanceof PetEggProps)
		{
			return 8*60*60;
		}
		//如果是法宝
		else if(article instanceof MagicWeapon)
		{
			return 6*60*60;
		}
		//如果是翅膀
		else if(article.getArticleType() == Article.ARTICLE_TYPE_WING)
		{
			return 6*60*60;
		}
		else
		{
			return 3*60*60;
		}
	}
	
	public static long 清除buff时间 = 60*1000;
	/**
	 * 角色挂单后的一些操作
	 * @param player
	 */
	public static void afterSealRole(Player player) {
		//清楚buff
		long now = System.currentTimeMillis();
//		if (player.getBuffs() != null && player.getBuffs().size() > 0) {
//			for (Buff b : player.getBuffs()) {
//				if (now >= (b.getInvalidTime()+清除buff时间)) {
//					b.setInvalidTime(0);
//				}
//			}
//		}
		
		//给所有好友发邮件
		Relation r = SocialManager.getInstance().getRelationById(player.getId());
		if (r != null) {
			List<Long> friends = r.getFriendlist();
			if (friends != null && friends.size() > 0) {
				for (long id : friends) {
					if (id > 0) {
						try {
							MailManager.getInstance().sendMail(id, new ArticleEntity[0]
							, new int[0], "好友角色出售", "您的好友【" + player.getName() + "】正在进行角色交易!", 0, 0, 0, "米谷出售角色通知好友");
						} catch (Exception e){
							
						} 
					}
				}
			}
			if (r.getMarriageId() > 0) {
				MarriageInfo info = MarriageManager.getInstance().getMarriageInfoById(r.getMarriageId());
				if (info != null) {
					long id = info.getHoldA() == player.getId() ? info.getHoldB() : info.getHoldA();
					try {
						MailManager.getInstance().sendMail(id, new ArticleEntity[0]
						, new int[0], "配偶角色出售", "您的配偶【" + player.getName() + "】正在进行角色交易!", 0, 0, 0, "米谷出售角色通知好友");
					} catch (Exception e){
						
					} 
				}
			}
		}
	}
	
	/**
	 * 验证出售角色  (考虑是否要把所有本服正在出售的角色订单缓存)
	 * @param player
	 * @return  true为可出售
	 */
	public static String validataGivePrice4Player(Player player) {
		try {
			if (!SaleRecordManager.getInstance().isRoleInSale(player)) {			//是否挂单中			还需要加入米谷中是否存在未下架出售的订单
				if (player.getLevel() >= SaleRecordManager.出售角色最低等级) {
					long sil = 获取角色交易最多银两数(player.getLevel());
					if (player.getSilver() <= sil) {
						if (CountryManager.getInstance().官职(player.getCountry(),player.getId()) < 0) {
							if (checkMails(player)) {
								if (checkAuction(player)) {
//									if (checkCavePet(player)) {
										if (!player.isInBoothSale()) {
											if (player.flyState != 1) {
												return null;
											} else {
												return "需要等仙婴附体状态结束后才可继续出售角色";
											}
										} else {
											return "需要结束摆摊才可继续出售角色";
										}
//									} else {
//										return "仙府宠物必须取出才可上架";
//									}
								} else {
									return "有正在拍卖的物品";
								}
							} else {
								return "必须取出带附件的邮件后才可出售角色";
							}
						} else {
							return "国家官员不可出售角色";
						}
					} else {
						return "角色携带银两数过多,当前等级最高可携带银两" + BillingCenter.得到带单位的银两(sil);
					}
				} else {
					return "角色等级低于110不可出售";
				}
			}
			return "该角色已在出售列表";
		} catch (Exception e) {
			logger.warn("[验证角色是否正在出售] [异常] [" + player.getLogString() + "]", e);
			return "服务器异常";
		}
	}
	
	public static boolean checkCavePet(Player player) {
		if (player.getCaveId() <= 0) {
			return true;
		}
		Cave cave = FaeryManager.getInstance().getCave(player);
		if (cave == null) {
			return true;
		}
		if (cave.getPethouse() != null && cave.getPethouse().getStorePets() != null) {
			for (int i=0; i<cave.getPethouse().getStorePets().length; i++) {
				if (cave.getPethouse().getStorePets()[i] > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean checkAuction(Player player) {
		try {
			CompoundReturn compoundReturn = AuctionManager.getInstance().msg_getSelfAuctionList(player);
			ArrayList<AuctionInfo4Client> arrayList = (ArrayList<AuctionInfo4Client>)compoundReturn.getObjValue();
			if (arrayList.size() > 0) {
				return false;
			}
		} catch (Exception e) {
			logger.warn("[检测拍卖] [" + player.getLogString() + "]", e);
		}
		return true;
	}
	
	public static boolean checkMails(Player player) {
		List<Mail> mails = MailManager.getInstance().getAllMails(player, 0, 100);
		if (mails != null && mails.size() > 0) {
			for (Mail mail : mails) {
				if (mail.getCells() != null && mail.getCells().length > 0) {
					for (Cell c : mail.getCells()) {
						if (c.getEntityId() > 0 && c.getCount() > 0) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	
	public static boolean validateGivePriceArticleEntity(long articleId,int type,int cellIndex,Player player)
	{
		ArticleEntityManager articleEntityManager = ArticleEntityManager.getInstance();
		ArticleManager articleManager = ArticleManager.getInstance();
		ArticleEntity ae = articleEntityManager.getEntity(articleId);
		Article article = ArticleManager.getInstance().getArticle(ae.getArticleName());

		if(article instanceof InlayArticle)
		{
			if(type != BAOSHI_TYPE)
			{
				logger.warn("[米谷交易] [道具验证] [失败] [道具传入类型和真实类型不匹配] ["+type+"] ["+articleId+"] ["+article.getName()+"]");
				return false;
			}
		}
		//如果是宠物蛋
		else if(article instanceof PetEggProps)
		{
			if(type != CHONGWU_DAN_TYPE)
			{
				logger.warn("[米谷交易] [道具验证] [失败] [道具传入类型和真实类型不匹配] ["+type+"] ["+articleId+"] ["+article.getName()+"]");
				return false;
			}
		}
		//如果是法宝
		else if(article instanceof MagicWeapon)
		{
			if(type != FABAO_TYPE)
			{
				logger.warn("[米谷交易] [道具验证] [失败] [道具传入类型和真实类型不匹配] ["+type+"] ["+articleId+"] ["+article.getName()+"]");
				return false;
			}
		}
		//如果是翅膀
		else if(article.getArticleType() == Article.ARTICLE_TYPE_WING)
		{
			if(type != CHIBANG)
			{
				logger.warn("[米谷交易] [道具验证] [失败] [道具传入类型和真实类型不匹配] ["+type+"] ["+articleId+"] ["+article.getName()+"] ["+article.getArticleType()+"]");
				return false;
			}
		}
		else
		{
			if(type != PUTONG_DAOJU_TYPE)
			{
				logger.warn("[米谷交易] [道具验证] [失败] [道具传入类型和真实类型不匹配] ["+type+"] ["+articleId+"] ["+article.getName()+"] ["+article.getArticleType()+"]");
				return false;
			}
		}

		Knapsack knapsack = player.getKnapsack_common();
		if(knapsack==null){
			logger.warn("[米谷交易] [道具验证] [失败] [背包为空] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"]");
			return false;
		}

		ArticleEntity aae = knapsack.getArticleEntityByCell(cellIndex);
		if(aae==null){
			logger.warn("[米谷交易] [道具验证] [失败] [从格子下标中没有拿到对应的道具] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"] ["+cellIndex+"] ["+articleId+"]");
			return false;
		}
		if (aae.isBinded()) {
			logger.warn("[米谷交易] [道具验证] [失败] [道具是绑定的] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"] ["+cellIndex+"] ["+articleId+"]");
			return false;
		}

		if(aae.getId() != articleId)
		{
			logger.warn("[米谷交易] [道具验证] [失败] [获取的道具id和背包中道具id不一致] ["+articleId+"] ["+aae.getId()+"] ["+player.getId()+"] ["+player.getUsername()+"] ["+player.getName()+"] ["+cellIndex+"]");
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 验证银两
	 * 
	 */
	public static double PRE_PAY_FEE_RATIO = 0.05;
	
	public static boolean validateGivePrice4Silver(long articleId,long count,Player player)
	{
		if(articleId == 200000l)
		{
			if(player == null)
			{
				return false;
			}
			
			//银两最低是一锭
			
			if(count  < 银锭最低交易额度)
			{
				return false;
			}
			
			//银两最高是100锭
			if(count > 银锭最高交易额度)
			{
				return false;
			}
			
			synchronized (player) {
				
				if(player.getSilver() <  Math.round(count*(1+PRE_PAY_FEE_RATIO)))
				{
					return false;
				}
			}
				
			
			return true;
		}
		return false;
	}
	
	public static long getPrePaySilver(long silvercount)
	{
		return Math.round(silvercount*PRE_PAY_FEE_RATIO);
	}
	
	public static int 账号角色个数限制 = 100;
	/** 渠道相同判定，暂不开启，因为渠道太乱，很多渠道都可以使用其他渠道的包登陆! */
	public static boolean isopenchannelcheck = false;
	
	public static String  buyArticle(long saleId,long sellPlayerId,long buyPlayerId,String tradeprice, String saleprice,String buyerusername,String selledservername,boolean buyrole,String selledroleid,String selledusername)
	{
		//TODO 要加更严格的安全验证才可以
		if(sellPlayerId == buyPlayerId && !buyrole)				//购买角色 议价会出现出售者id和购买者id相同情况
		{
			return "";
		}
		
		
		Player player = null;
		Player sellPlayer = null;
		if (!buyrole) {				//购买角色不需要判断角色信息  有可能为跨服
			try {
				player = PlayerManager.getInstance().getPlayer(buyPlayerId);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(player == null)
			{
				logger.warn("[米谷通讯] [买物品] [失败] [购买人不是本服的角色] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] ["+tradeprice+"]");
			}
			
			if(player.getLevel() < 40)
			{
				logger.warn("[米谷通讯] [买物品] [失败] [购买人等级不够] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] ["+tradeprice+"]");
			}
			
		}
		try {
			sellPlayer = PlayerManager.getInstance().getPlayer(sellPlayerId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.warn("[米谷通讯] [买物品] [失败] [出售角色不存在] [" + sellPlayerId + "]", e1);
		}
		if(sellPlayer == null && !buyrole)			//角色交易有可能存在跨服上架
		{
			logger.warn("[米谷通讯] [买物品] [失败] [出售者不是本服的角色] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] ["+tradeprice+"]");
			return "";
		}
		
		if(tradeprice == null)
		{
			logger.warn("[米谷通讯] [买物品] [失败] [无手续费传入] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] ["+tradeprice+"]");
			return "";
		}
		long tp  = 0l;
		try
		{
			Double tpd = Double.parseDouble(tradeprice);
			
			tp = (long)(tpd * 100);
			
		}
		catch(Exception e)
		{
			logger.error("[米谷通讯] [买物品] [失败] [手续费不符合转换格式] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] ["+tradeprice+"]",e);
			return "";
		}
		
		
		SaleRecord saleRecord = SaleRecordManager.getInstance().getSaleRecord(saleId);
		if(saleRecord != null)
		{
			if(saleRecord.getSellPlayerId() == sellPlayerId)
			{
				if (saleRecord.getTradeType() == SaleRecord.ROLE_TRADE) {
					try {
						Player[] players = GamePlayerManager.getInstance().getPlayerByUser(buyerusername);
						if (players!= null && players.length >= 账号角色个数限制) {
							logger.error("[米谷通讯] [购买角色] [失败] [买家账号下角色过多] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] ["+tradeprice+"]");
							return "";
						}
						if (!(saleRecord.getArticleId()+"").equalsIgnoreCase(selledroleid)) {
							logger.error("[米谷通讯] [购买角色] [失败] [传过来的上架角色id和商品id不同] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] ["+tradeprice+"] [" + saleRecord.getArticleId() + "] [" + selledroleid + "]");
							return "";
						}
					} catch (Exception e) {
						logger.warn("[查询账号角色数量] [异常] [" + buyerusername + "]", e);
					}
				}
				synchronized (saleRecord) {
					if(saleRecord.getResponseResult() == SaleRecord.RESPONSE_NOBACK )
					{
						saleRecord.setResponseResult(SaleRecord.RESPONSE_SUCC);
						saleRecord.setResponseTime(System.currentTimeMillis());
						saleRecord.setTradeMoney(tp);
						saleRecord.setMemo3(buyerusername);
					}
					else
					{
						logger.warn("[米谷通讯] [买物品] [失败] [此订单已经交易过了] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] ["+saleRecord.getResponseResult()+"]");
						return "";
					}
				}
					
				saleRecord.setBuyPlayerId(buyPlayerId);
				Passport pp = null;
				String buyerChannel = null;
				try {
					String un = (player == null ? buyerusername : player.getUsername());
					pp = BossClientService.getInstance().getPassportByUserName(un);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(pp != null)
				{
					saleRecord.setBuyPassportId(pp.getId());
					buyerChannel = pp.getLastLoginChannel();
					if (buyerChannel == null || buyerChannel.isEmpty()) {
						buyerChannel = pp.getRegisterChannel();
					}
				}
				else
				{
					logger.warn("[米谷通讯] [买物品] [失败] [购买人不是飘渺寻仙曲的用户] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"]");
					return "";
				}
				String sellChannel = null;
				pp = null;
				try {
					String sluser = sellPlayer == null ? selledusername : sellPlayer.getUsername();
					pp = BossClientService.getInstance().getPassportByUserName(sluser);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(pp == null)
				{
					logger.warn("[米谷通讯] [买物品] [失败] [出售者不是飘渺寻仙曲的用户] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"]");
					return "";
				}
				sellChannel = pp.getLastLoginChannel();
				if (sellChannel == null || sellChannel.isEmpty()) {
					sellChannel = pp.getRegisterChannel();
				}
				
				try {
					SaleRecordManager.getInstance().update(saleRecord);
					
					
					//通知统计系统
					try
					{
						if (!buyrole) {			//购买的商品不是角色的时候用以下逻辑
							StatClientService statClientService = StatClientService.getInstance();
							 Transfer_PlatformFlow flow=new Transfer_PlatformFlow();
							 
				  			 flow.setBuyPassportChannel(player.getPassport().getLastLoginChannel());
				  			 flow.setBuyPassportId(player.getPassport().getId()+"");
				  			 flow.setBuyPassportName(player.getPassport().getUserName());
				  			 flow.setBuyPlayerId(player.getId()+"");
				  			 flow.setBuyPlayerLevel(player.getLevel()+"");
				  			 flow.setBuyPlayerName(player.getName());
				  			 flow.setBuyPlayerVipLevel(player.getVipLevel()+"");
				  			 
				  			 flow.setId(saleRecord.getId()+"");
				  			 flow.setResponseResult(saleRecord.getResponseResult()+"");
				  			 flow.setTradeMoney(saleRecord.getTradeMoney());
				  			 flow.setColumn1(saleRecord.getPrepayfee()+"");
				  			 flow.setServerName(GameConstants.getInstance().getServerName());
				  			 flow.setColumn2(System.currentTimeMillis()+"");
				  			 statClientService.sendTransfer_PlatformFlow("",flow);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
					
					if(saleRecord.getArticleId() != 200000l)
					{
						if (saleRecord.getTradeType() == SaleRecord.ROLE_TRADE) {			//是购买角色
							try {
								String sellCref = SaleRecordManager.getInstance().channelReflect.get(sellChannel);
								String buyCref = SaleRecordManager.getInstance().channelReflect.get(buyerChannel);
								if (!isopenchannelcheck || (sellCref != null && sellCref.equalsIgnoreCase(buyCref))) {
									Player player1 = GamePlayerManager.getInstance().getPlayer(saleRecord.getArticleId());
									player1.setCangkuPassword("");		//清除仓库密码
									String oldUserName = player1.getUsername();
									player1.setUsername(buyerusername);
									((GamePlayerManager)GamePlayerManager.getInstance()).em.flush(player1);
									logger.warn("[米谷通讯] [购买角色] [成功] [oldUserName:" + oldUserName + "] [newusername:" + player1.getUsername() + "] [saleId:" + saleRecord.getId() + "] [sellChannel:" + sellChannel + "] [buyerChannel:"+buyerChannel+"] [" + player1.getLogString() + "]");
								} else {
									logger.warn("[米谷通讯] [购买角色] [失败] [渠道不同] [sellChannel:" + sellChannel + "] [buyerChannel:" + buyerChannel + "] [" + saleId + "] [sellCref:" + sellCref + "] [buyCref:" + buyCref + "]");
								}
//								String title = "成功购买到角色:";
//								String content = "恭喜，您在官方交易助手中成功购买角色！";
								/*MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[0]
								, new int[0], title, content, 0, 0, 0, "购买米谷角色成功");*/
							} catch (Exception e) {
								logger.warn("[米谷通讯] [购买角色] [失败] [出现未知异常] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] [aeId:"+saleRecord.getArticleId()+"]",e);
								return "";
							}
						} else {	//走以前逻辑
							ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(saleRecord.getArticleId());
							
							//发邮件给买家
							long EntityMailID = 0;
							try {
								String title = "成功购买到"+articleEntity.getArticleName();
								String content = "恭喜，您在官方交易助手中成功购买"+articleEntity.getArticleName()+"*"+saleRecord.getGoodsCount()+",请查收附件！";
								EntityMailID = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{articleEntity}
								, new int[]{(int) saleRecord.getGoodsCount()}, title, content, 0, 0, 0, "购买米谷物品成功");
							} catch (Exception e) {
								logger.warn("[米谷通讯] [买物品] [失败] [出现未知异常] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"]",e);
								return "";
							}
							
							//发邮件给卖家
							try {
								
								long gainMoney = saleRecord.getPayMoney() - saleRecord.getTradeMoney();
								
								String title =articleEntity.getArticleName()+"出售成功";
								
								BigDecimal b = new BigDecimal(saleRecord.getPayMoney());
								BigDecimal big100 = new BigDecimal(100);
								b = b.divide(big100);
								
								BigDecimal decimal = new BigDecimal (gainMoney );
								decimal = decimal.divide(big100);
								
								String content = "恭喜，您在官方交易助手寄售的"+articleEntity.getArticleName()+"*"+saleRecord.getGoodsCount()+",已成功售出。本次交易金额为"+b.doubleValue()+"元，	扣除"+tradeprice+"元服务费,"+decimal.doubleValue()+"元已经打入您官方交易助手的账户余额。为保障交易安全，此交易费在3*24小时考察期之后可以使用及提现。";
								EntityMailID = MailManager.getInstance().sendMail(sellPlayer.getId(), new ArticleEntity[0]
								, new int[0], title, content, 0, 0, 0, "出售米谷物品成功");
							} catch (Exception e) {
								logger.warn("[米谷通讯] [买物品] [失败] [出现未知异常] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"]",e);
								return "";
							}
							
							{
								try
								{
									long[] articleIds = new long[1];
									articleIds[0] = articleEntity.getId();
									createArticleTrade(saleRecord.getSellPlayerId(), saleRecord.getBuyPlayerId(), articleIds, "migu");
								}
								catch(Exception e)
								{
									logger.warn("[米谷通讯] [创建交易记录] [失败] [出现未知异常] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"]",e);
								}
							}
						}	
					}
					else
					{
						synchronized (player) {
							
//							billingCenter.playerExpense(sellPlayer, saleRecord.getGoodsCount(), CurrencyType.YINZI, SavingReasonType.米谷交易, "米谷玩家卖出银锭");
							try
							{
								billingCenter.playerSaving(player, saleRecord.getGoodsCount(), CurrencyType.YINZI, SavingReasonType.米谷交易, "米谷玩家买入银锭");
							}
							catch(Exception e)
							{
								logger.warn("[米谷通讯] [买物品] [增加银两] [失败] [出现未知异常] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"]",e);
								return "";
							}
							
							//发邮件给买家
							long EntityMailID = 0;
							try {
								String title = "成功购买银锭";
								String content = "恭喜，您在官方交易助手中成功购买银两"+ BillingCenter.得到带单位的银两(saleRecord.getGoodsCount())+"，请查收！";
								EntityMailID = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[0]
								, new int[0], title, content, 0, 0, 0, "购买米谷银锭成功");
							} catch (Exception e) {
								logger.warn("[米谷通讯] [买物品] [失败] [出现未知异常] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"]",e);
								return "";
							}
							
							//发邮件给卖家
							try {
								
								long gainMoney = saleRecord.getPayMoney() - saleRecord.getTradeMoney();
								
								String title ="成功出售银锭";
								
								BigDecimal b = new BigDecimal(saleRecord.getPayMoney());
								BigDecimal big100 = new BigDecimal(100);
								b = b.divide(big100);
								
								BigDecimal decimal = new BigDecimal (gainMoney );
								decimal = decimal.divide(big100);
								
								String content = "恭喜，您在官方交易助手成功售出银锭"+BillingCenter.得到带单位的银两(saleRecord.getGoodsCount())+"。本次交易金额为"+b.doubleValue()+"元，	扣除"+tradeprice+"元服务费,"+decimal.doubleValue()+"元已经打入您官方交易助手的账户余额。为保障交易安全，此交易费在3*24小时考察期之后可以使用及提现。";
								EntityMailID = MailManager.getInstance().sendMail(sellPlayer.getId(), new ArticleEntity[0]
								, new int[0], title, content, 0, 0, 0, "出售米谷银锭成功");
							} catch (Exception e) {
								logger.warn("[米谷通讯] [买物品] [失败] [出现未知异常] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"]",e);
								return "";
							}
						}
					}
					
					
					if(logger.isInfoEnabled())
						logger.info("[米谷通讯] [买物品] [成功] [ok] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] ["+saleRecord.getArticleId()+"] ["+saleRecord.getGoodsCount()+"]");
					return saleRecord.getId()+"";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		else
		{
			logger.warn("[米谷通讯] [买物品] [失败] [无此交易记录或者手续费为0] ["+buyPlayerId+"] ["+sellPlayerId+"] ["+saleId+"] ["+tp+"]");
			return "";
		}
		return "";
	}
	
	public static double CANCEL_SALE_FEE_RATIO = 0.01;
	
	public static String cancelSell(long saleId,long sellPlayerId,String reason,String username)
	{
		Player player = null;
		try {
			player = PlayerManager.getInstance().getPlayer(sellPlayerId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(player == null && (username==null || username.isEmpty()))
		{
			logger.warn("[米谷通讯] [撤销出售] [失败] [撤销人不是本服的角色] ["+sellPlayerId+"] ["+saleId+"] ["+reason+"]");
		}
		
		Passport pp = null;
		try {
			String uname = player != null ? player.getUsername() : username;
			pp = BossClientService.getInstance().getPassportByUserName(uname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(pp == null)
		{
			logger.warn("[米谷通讯] [撤销出售] [失败] [撤销者不是飘渺寻仙曲的用户] ["+sellPlayerId+"] ["+saleId+"] ["+reason+"]");
			return "";
		}
		
		
		
		SaleRecord saleRecord = SaleRecordManager.getInstance().getSaleRecord(saleId);
		if(saleRecord != null)
		{
			if(saleRecord.getSellPlayerId() == sellPlayerId)
			{
				synchronized (saleRecord) {
					if(saleRecord.getResponseResult() == SaleRecord.RESPONSE_NOBACK)
					{
						saleRecord.setResponseResult(SaleRecord.BACK_MONEY_SUCC);
						saleRecord.setMemo1(reason);				
					}
					else
					{
						logger.warn("[米谷通讯] [撤销出售] [失败] [此订单已经交易过了] ["+sellPlayerId+"] ["+saleId+"] ["+reason+"] ["+saleRecord.getResponseResult()+"]");
						return "";
					}
				}
				
				
				try {
					SaleRecordManager.getInstance().update(saleRecord);
					
					

					long cancelSaleSilver = 0l;
					
					boolean isInvalidPlayer = MiGuTradeServiceWorker.isInvalidPlayer(player); 
					
					if(saleRecord.getArticleId() != 200000l)
					{
						if (saleRecord.getTradeType() == SaleRecord.ROLE_TRADE) {			//出售角色撤单
							if (logger.isInfoEnabled()) {
								logger.info("[米谷通讯] [出售角色下架] [成功] [" + sellPlayerId + "] [" + saleId + "] [" + reason + "]");
							}
						} else {
							ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(saleRecord.getArticleId());
							
							//发邮件给玩家
							long EntityMailID = 0;
							if(!"2".equals(reason))
							{
								try {
									String title = "米掌柜退还撤单道具";
									String content = "您撤销了道具寄售，寄售的道具"+articleEntity.getArticleName()+"*"+saleRecord.getGoodsCount()+",请查收！";
									
									EntityMailID = MailManager.getInstance().sendMail(sellPlayerId, new ArticleEntity[]{articleEntity}
									, new int[]{(int) saleRecord.getGoodsCount()}, title, content, 0, 0, 0, "撤销米谷交易道具");
								} catch (Exception e) {
									logger.warn("[米谷通讯] [撤销出售] [失败] [出现未知异常] ["+sellPlayerId+"] ["+saleId+"] ["+reason+"]",e);
									return "";
								}
							}
							else 
							{
								try {
									String title = "米掌柜退还到期道具";
									String content = "您寄售的道具"+articleEntity.getArticleName()+"*"+saleRecord.getGoodsCount()+"已经到期下架,请查收！";
									
									EntityMailID = MailManager.getInstance().sendMail(sellPlayerId, new ArticleEntity[]{articleEntity}
									, new int[]{(int) saleRecord.getGoodsCount()}, title, content, 0, 0, 0, "米谷交易道具到期下架");
								} catch (Exception e) {
									logger.warn("[米谷通讯] [道具自动下架] [失败] [出现未知异常] ["+sellPlayerId+"] ["+saleId+"] ["+reason+"]",e);
									return "";
								}
							}
						}
					} 
					else
					{
						synchronized (player) {
							
							
							long EntityMailID = 0;
							if(!"2".equals(reason) )
							{
								try {
									cancelSaleSilver = Math.round( (saleRecord.getGoodsCount()*CANCEL_SALE_FEE_RATIO));
									billingCenter.playerSaving(player,saleRecord.getGoodsCount()+saleRecord.getPrepayfee()- cancelSaleSilver, CurrencyType.YINZI, SavingReasonType.米谷交易, "米掌柜退还撤单银锭");
									
									
									String title = "米掌柜退还撤单银锭";
									long shengyuyinliang = saleRecord.getGoodsCount() + saleRecord.getPrepayfee() - Math.round( (saleRecord.getGoodsCount()*CANCEL_SALE_FEE_RATIO));
									//发送通知	
									String content = "您撤销了银锭寄售，其中寄售银两"+BillingCenter.得到带单位的银两(saleRecord.getGoodsCount())+"，预存手续费"+BillingCenter.得到带单位的银两(saleRecord.getPrepayfee())+"。扣除撤单服务费"+BillingCenter.得到带单位的银两(Math.round( (saleRecord.getGoodsCount()*CANCEL_SALE_FEE_RATIO)))+"，剩余"+BillingCenter.得到带单位的银两(shengyuyinliang)+"，请查收！";
									EntityMailID = MailManager.getInstance().sendMail(sellPlayerId, new ArticleEntity[0]
									, new int[0], title, content, 0, 0, 0, "撤销米谷交易银锭");
								} catch (Exception e) {
									logger.warn("[米谷通讯] [撤销出售] [失败] [出现未知异常] ["+sellPlayerId+"] ["+saleId+"] ["+reason+"]",e);
									return "";
								}
							}
							else
							{
								
								/*➢	银锭寄售到期后系统发给卖家的邮件：
							     邮件标题：米掌柜退还到期银锭    
							     发件人：米掌柜
							内容：您寄售的银锭XX锭，已经到期下架，您出售的银锭XX锭，以及您预存的XX锭手续费全额返还，请查收！
							*/
								try {
									billingCenter.playerSaving(player,saleRecord.getGoodsCount()+saleRecord.getPrepayfee(), CurrencyType.YINZI, SavingReasonType.米谷交易, "米掌柜到期退还银锭");
									
									String title = "米掌柜退还到期银锭";
									String content = "您寄售的银两"+BillingCenter.得到带单位的银两(saleRecord.getGoodsCount())+"，已经到期下架，您出售的银两"+BillingCenter.得到带单位的银两(saleRecord.getGoodsCount())+",以及您预存的"+BillingCenter.得到带单位的银两( saleRecord.getPrepayfee())+"手续费全额返还，请查收！";
									EntityMailID = MailManager.getInstance().sendMail(sellPlayerId, new ArticleEntity[0]
									, new int[0], title, content, 0, 0, 0, "撤销米谷交易银锭");
								} catch (Exception e) {
									logger.warn("[米谷通讯] [撤销出售] [失败] [出现未知异常] ["+sellPlayerId+"] ["+saleId+"] ["+reason+"]",e);
									return "";
								}
							}
						
						}
					}
					
					//通知统计系统
					try
					{
						StatClientService statClientService = StatClientService.getInstance();
						 Transfer_PlatformFlow flow=new Transfer_PlatformFlow();
			  			 flow.setId(saleRecord.getId()+"");
			  			 flow.setResponseResult(saleRecord.getResponseResult()+"");
			  			 flow.setTradeMoney(saleRecord.getTradeMoney());
			  			 flow.setCancelSaleSilver(cancelSaleSilver+"");
			  			 flow.setColumn1("0");
			  			 flow.setServerName(GameConstants.getInstance().getServerName());
			  			 
			  			 statClientService.sendTransfer_PlatformFlow("",flow);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
					if(logger.isInfoEnabled())
						logger.info("[米谷通讯] [撤销出售] [成功] [ok] ["+sellPlayerId+"] ["+saleId+"] ["+reason+"] ["+saleRecord.getArticleId()+"] ["+saleRecord.getGoodsCount()+"]");
					return saleRecord.getId()+"";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				if (username.equals(saleRecord.getMemo2()) && saleRecord.getTradeType() == SaleRecord.ROLE_TRADE) {			//出售角色撤单
					try {
						synchronized (saleRecord) {
							if(saleRecord.getResponseResult() == SaleRecord.RESPONSE_NOBACK)
							{
								saleRecord.setResponseResult(SaleRecord.BACK_MONEY_SUCC);
								saleRecord.setMemo1(reason);				
							}
							else
							{
								logger.warn("[米谷通讯] [撤销出售] [失败] [此订单已经交易过了] ["+sellPlayerId+"] ["+saleId+"] ["+reason+"] ["+saleRecord.getResponseResult()+"]");
								return "";
							}
						}
						try {
							SaleRecordManager.getInstance().update(saleRecord);
							if (logger.isInfoEnabled()) {
								logger.info("[米谷通讯] [出售角色下架] [成功] [" + sellPlayerId + "] [" + saleId + "] [" + reason + "] [" + username + "]");
							}
							return saleRecord.getId()+"";
						} catch (Exception e) {
							logger.warn("[米谷通讯] [出售角色下架] [异常] [" + sellPlayerId + "] [" + saleId + "] [" + reason + "] [" + username + "]", e);
						}
					} catch (Exception e1) {
						logger.warn("[米谷通讯] [出售角色下架] [异常][" + sellPlayerId + "] [" + saleId + "] [" + reason + "] [" + username + "]", e1);
					}
				}
			}
		}
		else
		{
			logger.warn("[米谷通讯] [撤销出售] [失败] [无此交易记录] ["+sellPlayerId+"] ["+saleId+"] ["+reason+"]");
			return "";
		}
		return "";
	}
	
	
	/**
	 * 获取玩家身上的银两 以文做单位
	 */
	public static long getSilverByPlayer(long playerId)
	{
		try
		{
			Player player = PlayerManager.getInstance().getPlayer(playerId);
			if(player == null)
			{
				logger.warn("[米谷通讯] [查询玩家身上银两] [失败] [无此用户] ["+playerId+"]");
				return 0l;
			}
			
			long silverNum = 0l;
			
			synchronized (player) {
				silverNum = player.getSilver();
			}
			if(logger.isInfoEnabled())
			{
				logger.info("[米谷通讯] [查询玩家身上银两] [成功] ["+playerId+"] ["+player.getUsername()+"] ["+player.getName()+"] ["+silverNum+"]");
			}
			
			return silverNum;
			
		}
		catch(Exception e)
		{
			logger.warn("[米谷通讯] [查询玩家身上银两] [失败] [出现未知异常] ["+playerId+"]",e);
			return 0l;
		}
	}
	
	public static int getSaleRecordNum4NoSaled4Silver(long playerId)
	{
		int count = 0;
		try {
			count = SaleRecordManager.getInstance().getNoSaledCount4Silver(playerId);
		} catch (Exception e) {
			logger.error("[米谷通讯] [查询玩家挂银锭单数量] [失败] [出现未知异常] ["+playerId+"]",e);
		}
		
		return count;
	}
	
	public static int getSaleRecordNum4NoSaled4Article(long playerId)
	{
		int count = 0;
		try {
			count = SaleRecordManager.getInstance().getNoSaledCount4Article(playerId);
		} catch (Exception e) {
			logger.error("[米谷通讯] [查询玩家挂物品单数量] [失败] [出现未知异常] ["+playerId+"]",e);
		}
		
		return count;
	}
	
	public static void createArticleTrade(long sellerId,long buyerId,long[] articleIds,String tradeDesc)
	{
		try
		{
			ArticleTradeRecord articleTradeRecord =  new ArticleTradeRecord();
			articleTradeRecord.setSellPlayerId(sellerId);
			articleTradeRecord.setBuyPlayerId(buyerId);
			
			articleTradeRecord.setArticleIds(articleIds);
			
			articleTradeRecord.setDesc(tradeDesc);
			articleTradeRecord.setTradeTime(System.currentTimeMillis());
			
			ArticleTradeRecordManager.getInstance().notifyNew(articleTradeRecord);
		}
		catch(Exception e)
		{
			ArticleTradeRecordManager.logger.error("[创建交易记录] [失败] [出现未知异常] [saler:"+sellerId+"] [purchaser:"+buyerId+"]",e);
		}
		
	}
	/** 针对查询物品交易   */
	public static boolean 调整米谷 = true;		
	
	public static List <ArticleTradeRecord> queryTradeRecordeByArticleId4NotMigu(long playerId,long articleId)
	{
		List<ArticleTradeRecord> lst  =  new ArrayList<ArticleTradeRecord>();
		try {
			//交易中存在物品交换的情况不好记录谁是卖家谁是买家（改动比较大，需要记录出售的哪些道具id，和记录购买的哪些道具id，分别放入不同的数组，无端增加复杂性）
			//的情况，比如双方物品交换，所以需要把sellplayerId和buyPlayerId都作为条件
			/**
			 * 这里前提是传入的参数肯定是获得指定物品的人才会调用此方法查询
			 * 如果一个卖家卖出了这个物品过，还调用了这个方法，那可能是有问题的
			 */
			List<ArticleTradeRecord> allList =  null;
			if (!调整米谷) {
				allList = ArticleTradeRecordManager.getInstance().queryForWhere("(buyPlayerId=? or sellPlayerId = ? ) and desc <> 'migu'", new Object[]{playerId,playerId});
			} else {
				long threeDay = System.currentTimeMillis() - 内部交易公示期基准;
				allList = ArticleTradeRecordManager.getInstance().queryForWhere("(buyPlayerId=? or sellPlayerId = ? ) and desc <> 'migu' and tradeTime > ?", new Object[]{playerId,playerId,threeDay});
			}
			for(ArticleTradeRecord tradeRecord : allList)
			{
				long[] articleIds = tradeRecord.getArticleIds();
				if(articleIds != null)
				{
					if(articleIds.length > 0)
					{
						for(long id : articleIds)
						{
							if(articleId == id)
							{
								lst.add(tradeRecord);
							}
						}
					}
					else
					{
						ArticleTradeRecordManager.logger.warn("[查询交易记录] [失败] [articleids长度为0] [player:"+playerId+"] [article:"+articleId+"]");
					}
				}
				else
				{
					ArticleTradeRecordManager.logger.warn("[查询交易记录] [失败] [articleids为null] [player:"+playerId+"] [article:"+articleId+"]");
				}
			}
			
		} catch (Exception e) {
			ArticleTradeRecordManager.logger.error("[查询交易记录] [失败] [出现未知异常] [player:"+playerId+"] [article:"+articleId+"]",e);
		}
		
		return lst;
	}
	
	
	public static List <ArticleTradeRecord> queryTradeRecordeByArticleId4Migu(long playerId,long articleId)
	{
		List<ArticleTradeRecord> lst  =  new ArrayList<ArticleTradeRecord>();
		try {
			List<ArticleTradeRecord> allList =  ArticleTradeRecordManager.getInstance().queryForWhere("buyPlayerId=? and desc = 'migu'", new Object[]{playerId});
			for(ArticleTradeRecord tradeRecord : allList)
			{
				long[] articleIds = tradeRecord.getArticleIds();
				if(articleIds != null)
				{
					if(articleIds.length > 0)
					{
						for(long id : articleIds)
						{
							if(articleId == id)
							{
								lst.add(tradeRecord);
							}
						}
					}
					else
					{
						logger.warn("[查询交易记录] [失败] [articleids长度为0] [player:"+playerId+"] [article:"+articleId+"]");
					}
				}
				else
				{
					logger.warn("[查询交易记录] [失败] [articleids为null] [player:"+playerId+"] [article:"+articleId+"]");
				}
			}
			
		} catch (Exception e) {
			ArticleTradeRecordManager.logger.error("[查询交易记录] [失败] [出现未知异常] [player:"+playerId+"] [article:"+articleId+"]",e);
		}
		
		return lst;
	}
	
	
	
	public static int LIMIT_UNSALE_SILVER_ORDER_NUM = 10;
	public static int LIMIT_UNSALE_ARTICLE_ORDER_NUM = 5;
	
	public static int 物品挂单费用 = 10 * 1000;
	
	public static boolean 是否限制服务器 = true;
	public static boolean 是否限制渠道 = true;
	public static String[] 可开放米谷交易助手的服务器 = new String[]{"ceshia","潘多拉星","潘多拉星333","国内本地测试","马踏星河","决战巅峰","傲视遮天","虎啸山河","踏天之道","乱世群雄","梦醉江湖","剑雨潇湘",
		"七星耀月","云罗山脉","诛仙剑阵","浩气凛然","穿云裂空","腾蛇乘雾","神游四海","冰霜之心",
		"六界仙尊","五重星阵","长空剑舞","仙魔之殇","玄星云海","瑶光灵鹤","五世仙缘","天极五刃","冰焰双绝","星耀乾坤","万仙之王","碧涟梦瑶","魔剑逆鳞","仙侣豪杰","星辰天穹","影魅天惊","豁然之境","万世神兵","天苍穹月","天脉传说"
		,"云波鬼岭", "黄金海岸", "九天仙梦", "瑞草溪亭", "太玄仙遁", "祥云罩顶","众星捧月","上古龙魂","行兵布阵", "魂飘神荡"
		,"猛虎深山","另有洞天","龙骧虎步","腰金衣紫","驿路梅花","燕骏千金","鹰瞵虎视","玉树琼枝","仙液琼浆","蹑影藏形","卬头阔步","仙风道骨","吁天呼地","淹旬旷月"
		,"所向皆靡","逾闲荡检","幻影修罗","万象森罗","潜德秘行","独霸一方","望风而逃","当风秉烛","神妙莫测","雄霸中原","空谷幽兰","天悬地隔","途遥日暮", "霞法宝强化石映", "藏器待时","洪炉燎发", "穆如清风"
		,"西窗剪烛", "石城汤池", "善文能武", "事预则立","碧空如洗", "神领意造","烈火真金", "转海回天", "青翠欲滴", "廓然无累","山公刀载", "一剑诛仙", "情比金坚", "太极玄真", "追风逐月", "笑傲苍穹"
		,"情比金坚", "太极玄真", "追风逐月", "笑傲苍穹", "边城浪子", "九龙巡天", "仙路尘缘", "大光明镜", "余韵流风","冰封神殿", "御剑红尘","星光灿烂","拓天霸王","阡陌云间","日月明尊","燕语莺声", "芳华如梦","云海仙域"
		,"鱼游春水","月上海棠", "雕栏玉砌", "物华天宝", "慈航普渡", "紫电青霜", "繁花似锦","剑气箫心","风月无涯","圣帝明王", "冰魂雪魄", "技压群雄", "七彩纷呈", "秋水落霞", "月夜花朝", "锦囊妙计", "巧计连环", "八门金锁", "傲剑奇兵", "星月相伴"
		,"森罗鬼尊", "妖花邪君", "血魔修罗", "天墓之魂", "诚至金开", "渊渟岳立", "八荒蛮者", "花落半歌", "夏花依旧", "且听风吟", "落日余晖", "福寿康宁", "冰灵焰草", "青木剑诀", "古族圣地"
		,"云锦天章", "蛟龙出海", "神凤展翅", "赤霄追云", "仙凡逍遥", "醉舞狂歌", "群龙聚首", "仙姿玉质", "空城旧梦", "云中仙鹤", "正明公道", "跃马弯弓", "冰壶秋月", "红尘百戏", "暗香疏影", "金鸡迎福", "渔海樵山"
		,"青竹丹枫", "风清月白", "凤髓龙肝", "星梦高飞", "黄龙吐翠", "破浪乘风", "飘零书剑"
		
	};
	
	public static boolean 是否可以上架 = false;
	/**  没有此限制代表所有开放交易助手的服务器都开放角色交易 */
	public static boolean 是否开启角色交易服务器限制 = false;
	public static String[] 可开放角色交易的服务器 = new String[]{"潘多拉星","潘多拉星2","国内本地测试","马踏星河","豁然之境","雄霸中原"};
	public static String[] 不显示米谷的渠道 = new String[]{"APPSTOREGUOJI_MIESHI","WP8_557NET_MIESHI","WP8_MIESHI","WP8_WPAPPS_MIESHI","WIN8STORE_MIESHI"};

	public static boolean platFormIsOpen()
	{
		PlatformManager platformManager = PlatformManager.getInstance();
		if(platformManager.isPlatformOf(Platform.官方))
		{
			return true;
		}
		
		return false;
		
	}
	
	public static boolean isOpenSaleRole() {
		String servername = GameConstants.getInstance().getServerName();
		if (!是否开启角色交易服务器限制) {
			for(String sn : MiGuTradeServiceWorker.可开放米谷交易助手的服务器)
			{
				if(sn.equals(servername))
				{
					return true;
				}
			}
			return false;
		}
		for (int i=0; i<可开放角色交易的服务器.length; i++) {
			if (可开放角色交易的服务器[i].equalsIgnoreCase(servername)) {
				return true;
			}
		}
		return false;
	}
	
	public static int 打开米谷需要等级 = 10;
	public static boolean isLimitMigu = false;
	
	public static long closeTime = Timestamp.valueOf("2017-08-06 00:00:00").getTime();
	
	public static boolean isOpenMigu(String channel,Player player)
	{
		long now = System.currentTimeMillis();
		if (now > closeTime) {
			return false;
		}
		String servername = GameConstants.getInstance().getServerName();
		boolean limitServer = MiGuTradeServiceWorker.是否限制服务器;
		boolean limitChannel = MiGuTradeServiceWorker.是否限制渠道;
		boolean isopenserver = false;
		boolean isopenchannel = true;//
		
		if(limitServer)
		{
			for(String sn : MiGuTradeServiceWorker.可开放米谷交易助手的服务器)
			{
				if(sn.equals(servername))
				{
					isopenserver = true;
					break;
				}
			}
		}
		else
		{
			isopenserver = true;
		}
		
		if(limitChannel)
		{
			for(String cn : MiGuTradeServiceWorker.不显示米谷的渠道)
			{
				if(cn.equals(channel))
				{
					isopenchannel = false;
					break;
				}
			}
		}
		else
		{
			isopenchannel = true;
		}
		
		
		if(isopenserver && isopenchannel && player.getLevel() >= 打开米谷需要等级 && platFormIsOpen() )
		{
			return true && !isLimitMigu;
		}
		else
		{
			return false;
		}
	}

	//验证是否是封禁用户
	public static boolean validateIsDenyUser(String username)
	{
		
		String[] infos = new String[]{"QUERY_DENYUSER",username};
		
		MieshiGatewayClientService gatewayClientService = MieshiGatewayClientService.getInstance();
		
		VALIDATE_DEVICE_INFO_REQ req = new VALIDATE_DEVICE_INFO_REQ(GameMessageFactory.nextSequnceNum(), infos);
		try {
			VALIDATE_DEVICE_INFO_RES res = (VALIDATE_DEVICE_INFO_RES) gatewayClientService.sendMessageAndWaittingResponse(req, 60 * 1000);
			if (res == null) {
				logger.warn("[米谷交易] [查询是否是封禁用户] [出错] [网关返回协议为null] ["+username+"]");
				return true;
			}

			String[] infoes = res.getInfos();
			if (infoes == null) {
				logger.warn("[米谷交易] [查询是否是封禁用户] [出错] [返回infos为null] ["+username+"]");
				return true;
			}
			
			if(infoes.length > 0)
			{
				if("true".equals(infoes[0]))
				{
					logger.warn("[米谷交易] [查询是否是封禁用户] [成功] [是封禁用户] ["+username+"]");
					return true;
				}
				else
				{
					logger.warn("[米谷交易] [查询是否是封禁用户] [成功] [不是封禁用户] ["+username+"]");
					return false;
				}
			}
			else
			{
				logger.warn("[米谷交易] [查询是否是封禁用户] [出错] [返回infos长度为0] ["+username+"]");
				return true;
			}
		}
		catch(Exception e)
		{
			logger.error("[米谷交易] [查询是否是封禁用户] [出错] [出现未知异常] ["+username+"]",e);
			return true;
		}
			
	}
	
	
	public static boolean isInvalidPlayer(Player player)
	{
		if (player == null) {
			logger.warn("[米谷交易] [是否是非法的用户] [失败] [角色为空]");
			return true;
		}
		try
		{
			String username = player.getUsername();
			boolean isDenyUser  = validateIsDenyUser(username);
			int silenceType =  ChatMessageService.getInstance().isSilence(player.getId());
			boolean isSilence  =  false;
			
			if((silenceType==2) || (silenceType == 1))
			{
				isSilence = true;
			}
			
			boolean isEnterLimit = EnterLimitManager.getInstance().inEnterLimit(username, player.getConn());
			
			if(logger.isInfoEnabled())
			{
				logger.info("[米谷交易] [是否是非法的用户] [成功] [isDenyUser:"+isDenyUser+"] [isEnterLimit:"+isEnterLimit+"] [isSilence:"+isSilence+"] ["+username+"]");
			}
		
			return isDenyUser || isSilence || isEnterLimit;
		}
		catch(Exception e)
		{
			logger.warn("[米谷交易] [是否是非法的用户] [失败] [出现未知异常] ["+(player==null?"null":player.getUsername())+"]",e);
			return true;
		}
		
	}
	
	/**
	 * 往boss发送玩家登陆的信息
	 * 	//boss需要的信息有玩家的用户名，服务器，角色，角色id,进入时间
	 * 
	 */
	
	public static final String OP_ENTER_SERVER_INFO = "enterserver_query";
	public static final String OP_GET_MIGU_TOKEN_INFO = "getmigutoken_query";
	
	
	/**
	 * @param player
	 * @param op
	 */
	public static void notifyBossPlayerEnter(Player player,String op)
	{
		long now = System.currentTimeMillis();
		try
		{
			if(player == null)
			{
				logger.warn("[米谷交易] [向boss发送玩家信息] [失败] [角色为空] ["+op+"]");
				return;
			}
			
			//构造要发送的信息数组，然后发送
			String[] infos = new String[]{op,player.getUsername(),GameConstants.getInstance().getServerName(),
					player.getName(),player.getId()+"",player.getEnterServerTime()+""};
			
			MIGU_COMMUNICATE_REQ req = new MIGU_COMMUNICATE_REQ(BossMessageFactory.nextSequnceNum(), infos);
			BossClientService.getInstance().sendMessageAndWithoutResponse(req);
			if(logger.isInfoEnabled())
			{
				logger.info("[米谷交易] [向boss发送玩家信息] [成功] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+op+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		}
		catch(Exception e)
		{
			logger.error("[米谷交易] [向boss发送玩家信息] [失败] [出现未知异常] ["+player.getUsername()+"] ["+player.getName()+"] ["+player.getId()+"] ["+op+"]",e);
		}
	
		
		
		
	}
	
	
	
	
}
