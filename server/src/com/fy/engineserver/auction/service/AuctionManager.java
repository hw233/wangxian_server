package com.fy.engineserver.auction.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.auction.Auction;
import com.fy.engineserver.auction.AuctionInfo4Client;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.AuctionSubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.YinPiaoEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.PetEggProps;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecord;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecordManager;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_AuctionCancel;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.AUCTION_LIST_RES;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.smith.ArticleRelationShipManager;
import com.fy.engineserver.smith.MoneyRelationShipManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.trade.TradeManager;
import com.fy.engineserver.trade.requestbuy.RequestOption;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class AuctionManager implements Runnable{
//	public static Logger logger = Logger.getLogger(AuctionSubSystem.class);
public	static Logger logger = LoggerFactory.getLogger(AuctionSubSystem.class);
	
	private final long MAX_PRICE = 999999999L;

	public final int CLIENT_LENGTH = 5;
	
	public final long AUCTION_TIME = 24*60*60*1000;				//拍卖时长
	
	public final int BUY1_TAX = 25;					//买东西收的税
	
	public final int SELL_TAX = 1;					//卖东西收的税
	
	public final int CANCEL_TAX = 2;				//违约金
	
	public final double JINGPAI_TERM = 0.10;			//竞拍价必须高于当前竞拍价的这个比例
	
	public static int AUCTION_MAX_NUM = 15;
	
	protected static AuctionManager instance;
	
	public SimpleEntityManager<Auction> em;
	
	private Hashtable<Long, Auction> auctionMap = new Hashtable<Long, Auction>();
	
	//以物品模板做的列表里面按  一口价做由大到小
	private Hashtable<Article, List<Auction>> auctionMap4Article = new Hashtable<Article, List<Auction>>();
	
	public long auctionMoney;			//拍卖花费的钱,手续费
	public long haveMoney;				//单子上钱
	public long saleMoney;				//给拍卖者发的钱
	public long backJingpaiMoney;		//退竞拍的钱
	public long jingpaiMoney;			//竞拍花的钱
	public long taxMoney;				//税
	public long cancleMoney;			//违约金
	
	public long oldSize;				//原来库里多少单
	public long haveSize;				//有多少单
	public long addSize;				//新加多少单
	public long yikoujiaSize;			//一口价多少单
	public long timeOverSize;			//到期多少单
	
	public Hashtable<Long, Auction> getAuctionMap() {
		return auctionMap;
	}

	public void setAuctionMap(Hashtable<Long, Auction> auctionMap) {
		this.auctionMap = auctionMap;
	}

	public void init() {
		
		logger.warn("拍卖系统启动  开始");
		instance = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Auction.class);
		Auction.setEM(em);
		loadAllAuctionFromDB();
		
		auctionMoney = 0;
		haveMoney = 0;
		saleMoney = 0;
		backJingpaiMoney = 0;
		jingpaiMoney = 0;
		taxMoney = 0;
		cancleMoney = 0;
		
		oldSize = getAuctionMap().size();
		haveSize = oldSize;
		addSize = 0;
		yikoujiaSize = 0;
		timeOverSize = 0;
		
		for (Auction auction : getAuctionMap().values()) {
			haveMoney += auction.getNowPrice();
		}
		jingpaiMoney = haveMoney;
		
		logger.warn("在库里竞拍的钱:" + haveMoney);
		Thread t = new Thread(instance, "Auction");
		t.start();
		logger.warn("拍卖系统启动  完成");
		ServiceStartRecord.startLog(this);
	}
	
	public void destroy(){
		List<Auction> deleteList = new ArrayList<Auction>();
		for (Iterator<Auction> it = getAuctionMap().values().iterator(); it.hasNext();) {
			Auction auction = it.next();
			if(auction.getStatus()==Auction.STATUS_FINISHED){
				deleteList.add(auction);
			}else if(auction.getStatus()==Auction.STATUS_EXPIRED){
				deleteList.add(auction);
			}
		}
		
		for(int i = 0; i<deleteList.size(); i++){
			Auction auction = deleteList.get(i);
			try {
				em.remove(auction);
			} catch (Exception e) {
				logger.error("destroy拍卖纪录出错" + auction.getId(), e);
			}
		}
		if (logger.isWarnEnabled())
			logger.warn("[destroy] [成功] [deleteSize={}]", new Object[]{deleteList.size()});
		em.destroy();
	}
	
	private void loadAllAuctionFromDB() {
		long time = System.currentTimeMillis();
		List<Auction> list = new ArrayList<Auction>();
		try{
			long count = em.count();
			int index = 1;
			while(count > 5000){
				list.addAll(em.query(Auction.class, "", new Object[]{},"", index, index + 5000));
				count -= 5000;
				index += 5000;
			}
			if (count > 0) {
				list.addAll(em.query(Auction.class, "", new Object[]{},"", index, index + count));
			}
		}catch(Exception e){
			logger.error("载入拍卖数据库出错:严重", e);
			return;
		}
		
		
		logger.warn("数据库数据长度={}", new Object[]{list.size()});
		try{
			ArrayList<Long> sellIDs = new ArrayList<Long>();
			ArrayList<Long> maxIDs = new ArrayList<Long>();
			for (Auction auction : list) {
				if (!sellIDs.contains(auction.getSellerId())){
					sellIDs.add(auction.getSellerId());
				}
				if (auction.getMaxPricePlayer() > 0 && !maxIDs.contains(auction.getMaxPricePlayer())) {
					maxIDs.add(auction.getMaxPricePlayer());
				}
				Article article = ArticleManager.getInstance().getArticle(auction.getName());
				if(article!=null){
					getAuctionMap().put(auction.getId(), auction);
					List<Auction> auctions = getAuctionMap4Article().get(article);
					if(auctions==null){
						auctions = new ArrayList<Auction>();
					}
					int index = 0;
					for(int i = 0; i <auctions.size(); i++){
						if(auctions.get(i).getBuyPrice()<auction.getBuyPrice()){
							index = i;
						}
					}
					auctions.add(index, auction);
					getAuctionMap4Article().put(article, auctions);
				}
				
			}
			Hashtable<Long, PlayerSimpleInfo> sellSimInfos = new Hashtable<Long, PlayerSimpleInfo>();
			Hashtable<Long, PlayerSimpleInfo> maxSimInfos = new Hashtable<Long, PlayerSimpleInfo>();
			try{
				sellSimInfos = PlayerSimpleInfoManager.getInstance().getInfoByIds(sellIDs);
				maxSimInfos = PlayerSimpleInfoManager.getInstance().getInfoByIds(maxIDs);
			}catch(Exception e) {
				logger.error("取拍卖玩家出错:" + e);
			}
			
			for (Auction auction : list) {
				PlayerSimpleInfo sell = sellSimInfos.get(auction.getSellerId());
				if (sell == null) {
					auction.setSellName(Translate.未知);
				} else {
					auction.setSellName(sell.getName());
				}
				if (auction.getMaxPricePlayer() > 0) {
					PlayerSimpleInfo max = maxSimInfos.get(auction.getMaxPricePlayer());
					if (max == null) {
						auction.setMaxPricePlayerName(Translate.未知);
					}else {
						auction.setMaxPricePlayerName(max.getName());
					}
				}else {
					auction.setMaxPricePlayerName("");
				}
			}
			
		}catch(Exception e){
			logger.warn("初始化载入出错", e);
		}
		logger.warn("数据库初始化载入完成:耗时:" + (System.currentTimeMillis() - time) + "ms");
	}

	public static AuctionManager getInstance(){
		return instance;
	}
	
	/**
	 * 添加一个新的到当前内存列表中
	 * @param auction
	 * @return
	 */
	public synchronized void addNewAuction(Auction auction){
		if(auction==null){
			return;
		}
		haveSize +=1 ;
		addSize += 1;
		getAuctionMap().put(auction.getId(), auction);
		Article article = ArticleManager.getInstance().getArticle(auction.getName());
		List<Auction> auctions = getAuctionMap4Article().get(article);
		if(auctions==null){
			auctions = new ArrayList<Auction>();
		}
		int index = 0;
		for(int i = 0; i <auctions.size(); i++){
			if(auctions.get(i).getBuyPrice()<auction.getBuyPrice()){
				index = i;
			}
		}
		auctions.add(index, auction);
		getAuctionMap4Article().put(article, auctions);
	}
	
	/**
	 * 创建一个拍卖记录
	 * @param player
	 * @param pagType 包裹类型
	 * @param knapCellIndex 包裹索引
	 * @param entityId 物品ID
	 * @param count 数量
	 * @param startPrice 竞拍价
	 * @param buyPrice 一口价
	 * @return
	 * @throws Exception
	 */
	public synchronized CompoundReturn msg_creatAuction(Player player, int knapCellIndex, long entityId, int count, long startPrice, long buyPrice){
		if (UnitServerFunctionManager.needCloseFunctuin(Function.拍卖)) {
			player.sendError(Translate.合服功能关闭提示);
			return null;
		}
		Knapsack knapsack = player.getKnapsack_common();
		if (player.getLevel() < 10) {
			player.sendError(Translate.text_auction_112);
			return null;
		}
		if (player.isAppStoreChannel() && player.getLevel() < 20) {
			player.sendError(Translate.text_auction_113);
			return null;
		}
		if(knapsack==null){
//			logger.error("创建一个拍卖记录:物品所在包裹不存在  ["+player.getName()+"]  ["+pagType+"]   ["+knapCellIndex+"]  ["+entityId+"]  ["+count+"]   ["+startPrice+"]  ["+buyPrice+"]");
			logger.warn("创建一个拍卖记录:物品所在包裹不存在  [{}]  [{}]   [{}]  [{}]  [{}]   [{}]", new Object[]{player.getName(),knapCellIndex,entityId,count,startPrice,buyPrice});
			return null;
		}
		ArticleEntity articleEntity = knapsack.getArticleEntityByCell(knapCellIndex);
		if(articleEntity==null){
//			logger.error("创建一个拍卖记录:包裹的这个位置是空的 ["+player.getName()+"]  ["+pagType+"]   ["+knapCellIndex+"]  ["+entityId+"]  ["+count+"]   ["+startPrice+"]  ["+buyPrice+"]");
			logger.warn("创建一个拍卖记录:包裹的这个位置是空的 [{}]  [{}]   [{}]  [{}]  [{}]   [{}]", new Object[]{player.getName(),knapCellIndex,entityId,count,startPrice,buyPrice});
			return null;
		}
		if(articleEntity.getId()!=entityId){
//			logger.error("创建一个拍卖记录:包裹的这个位置物品和传过来的物品不是同一个 ["+player.getName()+"]  ["+pagType+"]   ["+knapCellIndex+"]  ["+entityId+"]  ["+count+"]   ["+startPrice+"]  ["+buyPrice+"]");
			logger.warn("创建一个拍卖记录:包裹的这个位置物品和传过来的物品不是同一个 [{}]  [{}]   [{}]  [{}]  [{}]   [{}]", new Object[]{player.getName(),knapCellIndex,entityId,count,startPrice,buyPrice});
			return null;
		}
		if (articleEntity.isBinded() || articleEntity.isRealBinded()) {
			player.sendError(Translate.text_auction_114);
			return null;
		}
		Article article = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
		if(article==null){
//			logger.error("创建一个拍卖记录:物品模板不存在 ["+player.getName()+"]  ["+pagType+"]   ["+knapCellIndex+"]  ["+entityId+"]  ["+count+"]   ["+startPrice+"]  ["+buyPrice+"]");
			logger.warn("创建一个拍卖记录:物品模板不存在 [{}]  [{}]   [{}]  [{}]  [{}]   [{}]", new Object[]{player.getName(),knapCellIndex,entityId,count,startPrice,buyPrice});
			return null;
		}
		
		ArrayList<AuctionInfo4Client> list = (ArrayList<AuctionInfo4Client>)(msg_getSelfAuctionList(player).getObjValue());
		if (list != null && list.size() >= AUCTION_MAX_NUM) {
			player.sendError(Translate.text_auction_115);
			return null;
		}
		
		if (!GreenServerManager.canToOtherPlayer(articleEntity)) {
			player.sendError(Translate.text_trade_023);
			return null;
		}
		
		if (!ArticleProtectManager.instance.isCanDo(player, ArticleProtectDataValues.ArticleProtect_Common, articleEntity.getId())){
			player.sendError(Translate.锁魂物品不能做此操作);
			return null;
		}
		
		if (startPrice<=0||buyPrice<=0|| startPrice > MAX_PRICE || buyPrice > MAX_PRICE) {
			player.sendError(Translate.text_auction_116);
			logger.error("[拍卖] [外挂] [p={}] [startPrice={}] [buyPrice={}]", new Object[]{player.getLogString(), startPrice, buyPrice});
			return null;
		}
		
		if(startPrice>buyPrice){
			player.sendError(Translate.text_auction_100);
			return null;
		}
		if(count<=0){
			player.sendError(Translate.text_auction_105);
			logger.error("[拍卖] [外挂] [p={}] [num={}]", new Object[]{player.getLogString(), count});
			return null;
		}
		if(knapsack.getCell(knapCellIndex).getCount()<count){
			logger.warn("创建一个拍卖记录:数量不足 [{}]  [{}]   [{}]  [{}]  [{}]   [{}]", new Object[]{player.getName(),knapCellIndex,entityId,count,startPrice,buyPrice});
			return null;
		}
		
		//logger.warn("拍卖物品剩余时间-------" + articleEntity.getEndTime());
		if (articleEntity.getEndTime() > 0 && articleEntity.getEndTime() - System.currentTimeMillis() < 24 * 60 * 60 * 1000) {
			player.sendError(Translate.text_auction_117);
			return null;
		}
		
		long tax = buyPrice*SELL_TAX/100;
		if(tax == 0){
			tax = 1;
		}
		if (player.getSilver() + player.getShopSilver() < tax) {
			player.sendError(Translate.text_auction_106);
			return null;
		}
		try {
			BillingCenter.getInstance().playerExpense(player, tax, CurrencyType.SHOPYINZI, ExpenseReasonType.AUCTION, "拍卖手续费");
			auctionMoney += tax;
			NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, tax, NewXiaoFeiActivity.XIAOFEI_TYPE_TAX);
//			ChongZhiActivity.getInstance().doXiaoFeiActivity(player, tax, XiaoFeiServerConfig.XIAOFEI_TONGDAO_JIAOYISHUI);
//			ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(player, tax, MoneyBillBoardActivity.XIAOFEI_TONGDAO_JIAOYISHUI);
			if (!使用新日志格式) {
				if (logger.isWarnEnabled()) logger.warn("[拍卖成功] [{}] [扣手续费{}]", new Object[]{player.getLogString(), tax}); 
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[操作类别:创建拍卖] [状态:成功] [" + player.getLogString4Knap() + "] [手续费:" + tax + "]");
				}
			}
		} catch (NoEnoughMoneyException e) {
			player.sendError(Translate.text_auction_106);
			logger.error("[用户拍卖物品] [失败] [计费失败] [用户:"+player.getUsername()+"] [角色:"+player.getName()+"] ",e);
			return null;
		} catch (BillFailedException e) {
			player.sendError(Translate.text_auction_106);
			logger.error("[用户拍卖物品] [失败] [计费失败] [用户:"+player.getUsername()+"] [角色:"+player.getName()+"] ",e);
			return null;
		}
		
		Auction auction = Auction.createAuction();
		if (auction == null) {
			player.sendError(Translate.text_auction_118);
			return null;
		}
		auction.setArticleEntityId(entityId);
		auction.setName(article.getName());
		auction.setAtype(article.get物品一级分类());
		auction.setAsubtype(article.get物品二级分类());
		auction.setColor(articleEntity.getColorType());
		auction.setCount(count);
		auction.setSellerId(player.getId());
		auction.setSellName(player.getName());
		auction.setMaxPricePlayerName("");
		auction.setStartPrice(startPrice);
		auction.setBuyPrice(buyPrice);
		if(article instanceof Equipment){
			auction.setLevel(((Equipment)article).getPlayerLevelLimit());
		}else if(article instanceof PetEggProps){
			auction.setLevel(((PetEggProps)article).getArticleLevel());
		}else if(article instanceof Props){
			auction.setLevel(((Props)article).getLevelLimit());
		}else{
			auction.setLevel(1);
		}
		//添加到列表中
		addNewAuction(auction);
		em.notifyNewObject(auction);
		knapsack.clearCell(knapCellIndex, count,"拍卖",false);
		ArticleStatManager.addToArticleStat(player, null, articleEntity, ArticleStatManager.OPERATION_交换, tax, ArticleStatManager.YINZI, count, "放入拍卖", null);
		if (!使用新日志格式) {
			if (logger.isWarnEnabled()) logger.warn("拍卖物品:角色:[{}] 角色名字:[{}] 拍卖信息:[{}]", new Object[]{player.getId(), player.getName(), auction.getLogString()});
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[操作类别:创建拍卖] [状态:成功] [" + player.getLogString4Knap() + "] [" + auction.getLogString4Newlog() + "]");
			}
		}

		player.sendError(Translate.text_auction_001);
		return null;
	}
	
	/**
	 * 返回拍卖查询用到得1,2级分类
	 * @return
	 */
	private RequestOption[] options;
	private static int[] mainType = new int[]{
		ArticleManager.物品一级分类_角色装备类, 
		ArticleManager.物品一级分类_马匹装备类, 
		ArticleManager.物品一级分类_角色法宝类, 
//		ArticleManager.物品一级分类_角色翅膀类, 
		ArticleManager.物品一级分类_宝石类, 
//		ArticleManager.物品一级分类_灵髓, 
		ArticleManager.物品一级分类_人物消耗品类, 
		ArticleManager.物品一级分类_宠物消耗品,
//		ArticleManager.物品一级分类_庄园果实类, 
		ArticleManager.物品一级分类_宠物蛋, 
		ArticleManager.物品一级分类_材料类, 
		ArticleManager.物品一级分类_角色技能书类, 
		ArticleManager.物品一级分类_宠物技能书类, 
		ArticleManager.物品一级分类_包裹类,};
	private static int[][] subType= new int[][]{
		{ArticleManager.物品二级分类_武器,ArticleManager.物品二级分类_头,ArticleManager.物品二级分类_肩,ArticleManager.物品二级分类_胸,ArticleManager.物品二级分类_护腕,ArticleManager.物品二级分类_腰带 ,ArticleManager.物品二级分类_鞋 ,ArticleManager.物品二级分类_戒指,ArticleManager.物品二级分类_项链,ArticleManager.物品二级分类_饰品,},
		{ArticleManager.物品二级分类_面甲,ArticleManager.物品二级分类_颈甲,ArticleManager.物品二级分类_体铠,ArticleManager.物品二级分类_鞍铠,ArticleManager.物品二级分类_蹄甲 ,},
		{},
//		{},
		{ArticleManager.物品二级分类_绿宝石,ArticleManager.物品二级分类_橙宝石,ArticleManager.物品二级分类_橙晶石,ArticleManager.物品二级分类_黑金石,ArticleManager.物品二级分类_蓝宝石,ArticleManager.物品二级分类_红宝石,ArticleManager.物品二级分类_白晶石,ArticleManager.物品二级分类_黄宝石,ArticleManager.物品二级分类_紫晶石,ArticleManager.物品二级分类_光效宝石},
//		{ArticleManager.物品二级分类_灵髓宝石, ArticleManager.物品二级分类_仙气葫芦},
		{ArticleManager.物品二级分类_封魔录,ArticleManager.物品二级分类_酒,ArticleManager.物品二级分类_押镖令,ArticleManager.物品二级分类_元气丹,},
		{ArticleManager.物品二级分类_炼妖石,ArticleManager.物品二级分类_宠物内丹,},
//		{ArticleManager.物品二级分类_经验果},
		{ArticleManager.物品二级分类_兽灵,ArticleManager.物品二级分类_精怪,ArticleManager.物品二级分类_神兽,ArticleManager.物品二级分类_仙魔,},
//		{ArticleManager.物品二级分类_圣物,ArticleManager.物品二级分类_蚕丝,ArticleManager.物品二级分类_翅膀图纸},
		{},
		{ArticleManager.物品二级分类_职业_修罗,ArticleManager.物品二级分类_职业_影魅,ArticleManager.物品二级分类_职业_仙心,ArticleManager.物品二级分类_职业_九黎,},
		{},
		{},
	};
	/**
	 * 查询拍卖的分类信息
	 * @return
	 */
	public CompoundReturn msg_getType(){
		if(options==null){
			options = new RequestOption[mainType.length];
			for(int i = 0; i<options.length; i++){
				options[i] = new RequestOption();
				options[i].setFirst(ArticleManager.物品一级分类类名[mainType[i]]);
				String[] secondType = new String[subType[i].length];
				for(int j = 0; j <subType[i].length; j++){
					secondType[j] = ArticleManager.物品二级分类类名[mainType[i]][subType[i][j]];
				}
				options[i].setSecond(secondType);
			}
		}
		CompoundReturn compoundReturn = new CompoundReturn();
		compoundReturn.setObjVlues(options);
		return compoundReturn;
	}
	
	/**
	 * 取拍卖列表
	 * @param mainType
	 * @param subType
	 * @param level
	 * @param maxlevel
	 * @param color
	 * @param name
	 * @return
	 */
	public synchronized CompoundReturn msg_requestAuctionList(String mainType, String subType, int level, int maxlevel, int color, String name, Player checkPlayer){
		//用那个按具体物品做key 的 是因为保证同样道具是按一定顺序排的
		ArrayList<AuctionInfo4Client> list = new ArrayList<AuctionInfo4Client>();
		for(Iterator<List<Auction>> it = getAuctionMap4Article().values().iterator();it.hasNext();){
			List<Auction> auctions = it.next();
			for(Auction auction : auctions){
				ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(auction.getArticleEntityId());
				Article article = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
				if(!mainType.equals("")){
					if(!mainType.equals(article.get物品一级分类())){
						continue;
					}
				}
				if(!subType.equals("")){
					if(!subType.equals(article.get物品二级分类())){
						continue;
					}
				}
				if(level>=0){
					if(auction.getLevel()<level){
						continue;
					}
				}
				if(maxlevel>=0){
					if(auction.getLevel()>maxlevel){
						continue;
					}
				}
				if(color>=0){
					if(articleEntity.getColorType()!=color){
						continue;
					}
				}
				if(!name.equals("")){
					if(articleEntity.getArticleName().indexOf(name)==-1){
						continue;
					}
				}
				
				if (checkPlayer != null && auction.getSellerId() != checkPlayer.getId()) {
					if (ChatMessageService.getInstance().isSilence(auction.getSellerId()) >= 2) {
						continue;
					}
				}
				
				if(auction.getStatus()==Auction.STATUS_NORMAL){
					list.add(auction.createInfo());
				}
			}
		}
		CompoundReturn compoundReturn = new CompoundReturn();
		compoundReturn.setObjValue(list);
		return compoundReturn;
	}
	
	public synchronized void msg_buy(Player player, long id, long price){
		if (player.getLevel() < 10) {
			player.sendError(Translate.text_auction_119);
			return;
		}
		if (player.isAppStoreChannel() && player.getLevel() < 20) {
			player.sendError(Translate.text_auction_120);
			return;
		}
		if (ChatMessageService.getInstance().isSilence(player.getId()) >= 2) {
			return;
		}
		Auction auction = getAuctionMap().get(id);
		if(auction==null||auction.getStatus()!=Auction.STATUS_NORMAL){
			player.sendError(Translate.text_auction_102);
			return;
		}
		
//		if (!PlayerManager.getInstance().isExists(auction.getSellerId())) {
//			player.sendError("拍卖者已经不存在了，不能买了");
//			return;
//		}
		Player sell = null;
		try {
			sell = PlayerManager.getInstance().getPlayer(auction.getSellerId());
		} catch (Exception e2) {
			logger.error("载入拍卖的出售者出错", e2);
		}
		if (sell == null) {
			
			player.sendError(Translate.text_auction_121);
		}
		ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(auction.getArticleEntityId());
		if (articleEntity == null) {
			player.sendError(Translate.物品不存在);
			return;
		}
		
		if (articleEntity.isBinded()) {
			player.sendError(Translate.text_auction_122);
			return;
		}
		
		if(price<=0 || price > MAX_PRICE){
			player.sendError(Translate.text_auction_123);
			logger.error("[竞拍] [外挂] [p={}] [price={}]", new Object[]{player.getLogString(), price});
			return;
		}
		if(price<auction.getStartPrice()||price<=auction.getNowPrice()){
			player.sendError(Translate.text_auction_124);
			return;
		}
		if(auction.getNowPrice()>0&&auction.getNowPrice()*(JINGPAI_TERM+1)>price&&price<auction.getBuyPrice()){
			player.sendError(Translate.text_auction_101);
			return;
		}
		if(auction.getSellName().equals(player.getName())){
			player.sendError(Translate.text_auction_103);
			return;
		}
		if(auction.getMaxPricePlayer()==player.getId()&&price<auction.getBuyPrice()){
			player.sendError(Translate.text_auction_104);
			return;
		}
		long tax = price*BUY1_TAX/100;
		if (tax <= 0) tax = 1;
		if(player.getSilver() < price + tax || (price + tax) < 0 || tax < 0){
			player.sendError(Translate.text_auction_107);
			return;
		}
		long mainMaxID = 0;
		if(auction.getNowPrice()>0){
			long nowTax = auction.getNowPrice()*BUY1_TAX/100;
			if (nowTax <= 0) nowTax = 1;
			try {
				mainMaxID = MailManager.getInstance().sendMail(auction.getMaxPricePlayer(), null, null, Translate.text_auction_108, Translate.text_auction_109, auction.getNowPrice() + nowTax, 0, 0, "竞拍被超过退钱");
				backJingpaiMoney += auction.getNowPrice();
				haveMoney -= auction.getNowPrice();
				taxMoney -= nowTax;
				logger.warn("[竞拍被超过] [退钱成功] [角色ID{}] [钱数{}] [{}]", new Object[]{auction.getMaxPricePlayer(), auction.getNowPrice() + nowTax, auction.getLogString()});
			} catch (Exception e) {
				logger.error("别人竞拍退钱邮件发送错误 buyID=[{}] AuctionID=[{}] price=[{}] 退钱PID=[{}] 退钱数=[{}]", new Object[]{player.getId(), id, price, auction.getMaxPricePlayer(), auction.getNowPrice() + nowTax});
				return;
			}
		}
		if(price>=auction.getBuyPrice()){
			price = auction.getBuyPrice();
			try {
				if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
					BillingCenter.getInstance().playerExpense(player, price, CurrencyType.YINZI, ExpenseReasonType.AUCTION_JINGPAI, "拍卖竞拍费", -1);
					BillingCenter.getInstance().playerExpense(player, tax, CurrencyType.YINZI, ExpenseReasonType.AUCTION_JINGPAI, "拍卖竞拍费");
				} else {
					BillingCenter.getInstance().playerExpense(player, price + tax, CurrencyType.YINZI, ExpenseReasonType.AUCTION_JINGPAI, "拍卖竞拍费");
				}
				jingpaiMoney += price;
				taxMoney += tax;
				NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, tax, NewXiaoFeiActivity.XIAOFEI_TYPE_TAX);
//				ChongZhiActivity.getInstance().doXiaoFeiActivity(player, tax, XiaoFeiServerConfig.XIAOFEI_TONGDAO_JIAOYISHUI);
//				ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(player, tax, MoneyBillBoardActivity.XIAOFEI_TONGDAO_JIAOYISHUI);
				if (!使用新日志格式) {
					if (logger.isWarnEnabled()) logger.warn("[操作类别:一口价] [状态:扣钱成功] [买家={}] [钱数={}] [税={}] [{}]", new Object[]{player.getLogString(), price, tax, auction.getLogString()});
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn("[操作类别:一口价] [状态:扣钱成功] [" + player.getLogString4Knap() + "] [钱数:" + price + "] [税:" + tax + "] [" + auction.getLogString4Newlog() + "]");
					}
				}
			} catch (NoEnoughMoneyException e1) {
				logger.error("竞拍扣钱出错1::  buyID=[{}] AuctionID=[{}] price=[{}]", new Object[]{player.getId(), id, price});
				return;
			} catch (BillFailedException e1) {
				logger.error("竞拍扣钱出错2::  buyID=[{}] AuctionID=[{}] price=[{}]", new Object[]{player.getId(), id, price});
				return;
			}
			
			
			long EntityMailID = 0;
			try {
				EntityMailID = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{articleEntity}
				, new int[]{auction.getCount()}, Translate.text_auction_003, Translate.text_auction_003, 0, 0, 0, "竞拍成功");
				if (!使用新日志格式) {
					if (logger.isWarnEnabled()) logger.warn("[给买家发物品] [成功] [买家{}] [{}]", new Object[]{player.getLogString(), auction.getLogString()});
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn("[操作类别:一口价] [状态:发物品成功] [" + player.getLogString4Knap() + "] [" + auction.getLogString4Newlog() + "]");
					}
				}
			} catch (Exception e) {
				logger.error("竞拍给玩家发物品出错::  buyID=[{}] AuctionID=[{}] price=[{}]", new Object[]{player.getId(), id, price});
				return;
			}
			
			{
				try
				{
					ArticleTradeRecord articleTradeRecord =  new ArticleTradeRecord();
					articleTradeRecord.setSellPlayerId(auction.getSellerId());
					articleTradeRecord.setBuyPlayerId(player.getId());
					
					long[] articleIds = new long[1];
					articleIds[0] = auction.getArticleEntityId();
					articleTradeRecord.setArticleIds(articleIds);
					
					articleTradeRecord.setDesc("竞拍");
					articleTradeRecord.setTradeTime(System.currentTimeMillis());
					
					ArticleTradeRecordManager.getInstance().notifyNew(articleTradeRecord);
				}
				catch(Exception e)
				{
					ArticleTradeRecordManager.logger.error("[创建竞拍交易记录] [失败] [出现未知异常] [sell:"+auction.getSellerId()+"] [buyer:"+player.getId()+"]",e);
				}
			}
			
			
			try {
				if (GreenServerManager.isBindYinZiServer()) {
					Article a = ArticleManager.getInstance().getArticle(Translate.银票);
					YinPiaoEntity entity = (YinPiaoEntity)ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.绿色服, null, a.getColorType(), 1, true);
					entity.setHaveMoney(price);
					MailManager.getInstance().sendMail(auction.getSellerId(), new ArticleEntity[]{entity}, new int[]{1}, Translate.text_auction_001, Translate.text_auction_001, 0, 0, 0, "给卖家发钱");
				}else {
					MailManager.getInstance().sendMail(auction.getSellerId(), null
							, null, Translate.text_auction_001, Translate.text_auction_001, price, 0, 0, "给卖家发钱");
				}
				saleMoney += price;
				if (!使用新日志格式) {
					if (logger.isWarnEnabled()) logger.warn("[给卖家邮件钱] [成功] [钱数={}] [{}]", new Object[]{price, auction.getLogString()});
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn("[操作类别:一口价] [状态:给卖家发钱成功] [钱数:" + price + "] [" + auction.getLogString4Newlog() + "]");
					}
				}
			} catch (Exception e) {
				logger.error("竞拍给玩家卖家发钱出错::  buyID=[{}] AuctionID=[{}] price=[{}]", new Object[]{auction.getSellerId(), id, price});
				return;
			}
			yikoujiaSize += 1;
			haveSize -= 1;
			auction.setStatus(Auction.STATUS_FINISHED);
			AchievementManager.getInstance().record(sell, RecordAction.拍卖次数);
			if (!使用新日志格式) {
				if (logger.isWarnEnabled()) logger.warn("一口价买:卖家:[{}] 卖家名字:[{}] 买家:[{}] 买家名字:[{}] 物品ID：[{}] 物品名称：[{}] 数量：[{}] 拍卖ID：[{}] 价钱：[{}] ", new Object[]{auction.getSellerId(), auction.getSellName(), player.getId(), player.getName(), articleEntity.getId(), articleEntity.getArticleName(), auction.getCount(), auction.getId(), auction.getStartPrice() + "-" + auction.getBuyPrice()});
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[一口价] [成功] [卖家id:" + auction.getSellerId() + "] [卖家名字:" + auction.getName() + "] [买家id:" + player.getId() + "] [买家名字:" + player.getName() + "] [物品id:" + articleEntity.getId() + "] [物品名:" + articleEntity.getArticleName() + "] [数量:" + auction.getCount() + "] [拍卖id:" + auction.getId() + "] [初始价格:" + auction.getStartPrice() + "] [成交价:" + auction.getBuyPrice() + "]");
				}
			}
			ArticleStatManager.addToArticleStat(sell, player, articleEntity, ArticleStatManager.OPERATION_交换, price + tax, ArticleStatManager.YINZI, auction.getCount(), "购买拍卖成功", null);
		
			try {
				MoneyRelationShipManager.getInstance().addMoneyMove(player, sell, (int)price);
			} catch(Throwable e) {
				e.printStackTrace();
			}
			
//			try{
//				Player buyPlayer = PlayerManager.getInstance().getPlayer(auction.getSellerId());
//				if(buyPlayer != null){
//					if(WhiteListManager.getInstance().isWhiteListPlayer(player)){
//						WhiteListManager.getInstance().addMailRowData(player, buyPlayer, com.fy.engineserver.util.whitelist.WhiteListManager.ActionType.竞拍,price, new String[]{articleEntity.getArticleName()}, new int[]{articleEntity.getColorType()}, new int[]{auction.getCount()},"");
//					}
////					if(WhiteListManager.getInstance().isWhiteListPlayer(buyPlayer)){
////						WhiteListManager.getInstance().addMailRowData(buyPlayer, player, com.fy.engineserver.util.whitelist.WhiteListManager.ActionType.拍卖,price, new String[]{articleEntity.getArticleName()}, new int[]{articleEntity.getColorType()}, new int[]{auction.getCount()});
////					}
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
			
			try {
				ArticleRelationShipManager.getInstance().addArticleMove(sell, player, auction.getCount());
			} catch(Throwable e) {
				e.printStackTrace();
			}
			
		}else{
			try {
				if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
					BillingCenter.getInstance().playerExpense(player, price, CurrencyType.YINZI, ExpenseReasonType.AUCTION_JINGPAI, "拍卖竞拍费", -1);
					BillingCenter.getInstance().playerExpense(player, tax, CurrencyType.YINZI, ExpenseReasonType.AUCTION_JINGPAI, "拍卖竞拍费");
				} else {
					BillingCenter.getInstance().playerExpense(player, price + tax, CurrencyType.YINZI, ExpenseReasonType.AUCTION_JINGPAI, "拍卖竞拍费");
				}
				jingpaiMoney += price;
				haveMoney += price;
				taxMoney += tax;
				NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, tax, NewXiaoFeiActivity.XIAOFEI_TYPE_TAX);
//				ChongZhiActivity.getInstance().doXiaoFeiActivity(player, tax, XiaoFeiServerConfig.XIAOFEI_TONGDAO_JIAOYISHUI);
//				ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(player, tax, MoneyBillBoardActivity.XIAOFEI_TONGDAO_JIAOYISHUI);
			} catch (NoEnoughMoneyException e1) {
				logger.error("竞拍扣钱出错::  buyID=[{}] AuctionID=[{}] price=[{}]", new Object[]{player.getId(), id, price});
				return;
			} catch (BillFailedException e1) {
				logger.error("竞拍扣钱出错::  buyID=[{}] AuctionID=[{}] price=[{}]", new Object[]{player.getId(), id, price});
				return;
			}
			long oldPrice = auction.getNowPrice();
			auction.setNowPrice(price);
			auction.addPricePlayer(player.getId());
			auction.setMaxPricePlayerName(player.getName());
			if (!使用新日志格式) {
				if (logger.isWarnEnabled()) logger.warn("竞拍买:卖家:[{}] 卖家名字:[{}] 买家:[{}] 买家名字:[{}] 物品ID：[{}] 数量：[{}] 拍卖ID：[{}] 价钱：[{}] 出价:[{}] ", new Object[]{auction.getSellerId(), auction.getSellName(), player.getId(), player.getName(), auction.getArticleEntityId(), auction.getCount(), auction.getId(), auction.getStartPrice() + "-" + oldPrice, price});
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[竞拍] [成功] [卖家Id:" + auction.getSellerId() + "] [卖家名字:" + auction.getSellName() + "] [买家id:" + player.getId() + "] [买家名字:" + player.getName() + "] [物品id:" + auction.getArticleEntityId() + "] [数量:" + auction.getCount() + "] [拍卖id:" + auction.getId() + "] [价钱:" + auction.getStartPrice() + "] [原竞拍价:"+oldPrice+"] [出价:" + price + "]");
				} 
			}

		}
	}
	
	public static boolean 使用新日志格式 = true;
	
	/**
	 * 取自己的拍卖列表
	 * @param player
	 * @return
	 */
	public CompoundReturn msg_getSelfAuctionList(Player player){
		ArrayList<AuctionInfo4Client> list = new ArrayList<AuctionInfo4Client>();
		for(Iterator<List<Auction>> it = getAuctionMap4Article().values().iterator();it.hasNext();){
			List<Auction> auctions = it.next();
			for(Auction auction : auctions){
				if(auction.getStatus()==Auction.STATUS_NORMAL&&auction.getSellerId()==player.getId()){
					list.add(auction.createInfo());
				}
			}
		}
		CompoundReturn compoundReturn = new CompoundReturn();
		compoundReturn.setObjValue(list);
		return compoundReturn;
	}
	
	/**
	 * 取消拍卖
	 * @param player
	 * @param id
	 */
	public synchronized void msg_cancelAuction(Player player, long id){
		Auction auction = getAuctionMap().get(id);
		if(auction==null){
			player.sendError(Translate.text_auction_125);
			return;
		}
		if(auction.getStatus()!=Auction.STATUS_NORMAL){
			player.sendError(Translate.text_auction_126);
			return;
		}
		if(player.getId()!=auction.getSellerId()){
			player.sendError(Translate.text_auction_127);
			logger.error("[取消拍卖] [外挂] [p={}] [id={}]", new Object[]{player.getLogString(), id});
			return;
		}
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setTitle(Translate.text_auction_004);
		long cancelM = auction.getBuyPrice()*CANCEL_TAX/100;
		if(cancelM == 0){
			cancelM = 1;
		}
		mw.setDescriptionInUUB(Translate.text_auction_128+TradeManager.putMoneyToMyText(cancelM) + Translate.text_auction_129);
		Option_AuctionCancel rb = new Option_AuctionCancel(id);
		Option_Cancel cancel = new Option_Cancel();
		rb.setText(Translate.确定);
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { rb, cancel });
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(creq);
	}
	
	public synchronized void realCancel(Player player, long id){
		Auction auction = getAuctionMap().get(id);
		if(auction==null){
			player.sendError(Translate.text_auction_125);
			return;
		}
		if(auction.getStatus()!=Auction.STATUS_NORMAL){
			player.sendError(Translate.text_auction_126);
			return;
		}
		if(player.getId()!=auction.getSellerId()){
			player.sendError(Translate.text_auction_127);
			return;
		}
		long cancelM = auction.getBuyPrice()*CANCEL_TAX/100;
		if(cancelM == 0){
			cancelM = 1;
		}
		if(player.getSilver() + player.getShopSilver() < cancelM || cancelM < 0){
			player.sendError(Translate.text_auction_111);
			return;
		}
		try {
			BillingCenter.getInstance().playerExpense(player, cancelM, CurrencyType.SHOPYINZI, ExpenseReasonType.AUCTION_WEIYUE, "取消拍卖违约金");
			cancleMoney += cancelM;
			NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, cancelM, NewXiaoFeiActivity.XIAOFEI_TYPE_TAX);
//			ChongZhiActivity.getInstance().doXiaoFeiActivity(player, cancelM, XiaoFeiServerConfig.XIAOFEI_TONGDAO_JIAOYISHUI);
//			ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(player, cancelM, MoneyBillBoardActivity.XIAOFEI_TONGDAO_JIAOYISHUI);
			if (!使用新日志格式) {
				if (logger.isWarnEnabled()) logger.warn("[取消拍卖] [成功] [取消费用{}] [{}] [{}]", new Object[]{cancelM, player.getLogString(), auction.getLogString()});
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[取消拍卖] [成功] [" + player.getLogString4Knap() + "] [" + auction.getLogString4Newlog() + "] [取消费用:" + cancelM + "]");
				}
			}
		} catch (NoEnoughMoneyException e1) {
			logger.error("取消竞拍扣钱出错::  buyID=[{}] AuctionID=[{}] price=[{}]", new Object[]{player.getId(), id, cancelM});
			return;
		} catch (BillFailedException e1) {
			logger.error("取消竞拍扣钱出错::  buyID=[{}] AuctionID=[{}] price=[{}]", new Object[]{player.getId(), id, cancelM});
			return;
		}
		if(auction.getMaxPricePlayer()>0){
			try {
				MailManager.getInstance().sendMail(auction.getMaxPricePlayer(), null, null, Translate.text_auction_108, Translate.text_auction_110, auction.getNowPrice(), 0, 0, "手动取消拍卖给竞拍的人退钱");
				backJingpaiMoney += auction.getNowPrice();
				haveMoney -= auction.getNowPrice();
				if (logger.isWarnEnabled()) logger.warn("[取消拍卖] [退竞拍钱] [成功] [退钱{}] [ID={}] [{}]", new Object[]{auction.getNowPrice(), auction.getMaxPricePlayer(), auction.getLogString()});
			} catch (Exception e) {
				logger.error("拍卖取消给竞拍退钱邮件发送错误" + auction.getLogString(), e);
				return;
			}
		}
		ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(auction.getArticleEntityId());
		try {
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{articleEntity}
			, new int[]{auction.getCount()}, Translate.text_auction_004, Translate.text_auction_004, 0, 0, 0, "手动取消拍卖归还物品");
			if (logger.isWarnEnabled()) logger.warn("[取消拍卖] [邮件物品] [成功] [{}]", new Object[]{auction.getLogString()});
		} catch (Exception e) {
			logger.warn("终止拍卖给玩家发邮件失败：" + auction.getLogString(), e);
			return;
		}
		ArticleStatManager.addToArticleStat(player, null, articleEntity, ArticleStatManager.OPERATION_交换, cancelM, ArticleStatManager.YINZI, auction.getCount(), "拍卖取消", null);
		auction.setStatus(Auction.STATUS_EXPIRED);
		if (logger.isWarnEnabled()) logger.warn("取消拍卖:角色:[{}] 角色名字:[{}] 物品ID：[{}] 物品名称：[{}] 数量：[{}] 拍卖ID：[{}] 价钱：[{}] ", new Object[]{player.getId(), player.getName(), articleEntity.getId(), articleEntity.getArticleName(), auction.getCount(), auction.getId(), auction.getStartPrice() + "-" + auction.getBuyPrice()});
		CompoundReturn compoundReturn = AuctionManager.getInstance().msg_getSelfAuctionList(player);
		try{
			int startNum = 0;
			int len = CLIENT_LENGTH;
			ArrayList<AuctionInfo4Client> arrayList = (ArrayList<AuctionInfo4Client>)compoundReturn.getObjValue();
			if(arrayList.size()<=0){
				player.addMessageToRightBag(new AUCTION_LIST_RES(GameMessageFactory.nextSequnceNum(), 0, 0, new AuctionInfo4Client[0], BUY1_TAX));
			}
			AuctionInfo4Client[] auctionInfo4Clients = arrayList.toArray(new AuctionInfo4Client[0]);
			if(auctionInfo4Clients.length<=startNum){
				if(auctionInfo4Clients.length<len){
					startNum = 0;
					len = auctionInfo4Clients.length;
				}else{
					startNum = auctionInfo4Clients.length-len;
					if(startNum<0){
						startNum = 0;
					}
				}
			}else{
				if(auctionInfo4Clients.length<startNum+len){
					len = auctionInfo4Clients.length-startNum;
				}
			}
			if(len>0){
				AuctionInfo4Client[] returnInfo = new AuctionInfo4Client[len];
				System.arraycopy(auctionInfo4Clients, startNum, returnInfo, 0,len);
				player.addMessageToRightBag(new AUCTION_LIST_RES(GameMessageFactory.nextSequnceNum(), startNum, auctionInfo4Clients.length, returnInfo, BUY1_TAX));
			}else{
				player.addMessageToRightBag(new AUCTION_LIST_RES(GameMessageFactory.nextSequnceNum(), 0, 0, new AuctionInfo4Client[0], BUY1_TAX));
			}
		}catch(Exception e){
			logger.warn("查询自己拍卖纪录出错realCancel:", e);
		}
	}
	
	public CompoundReturn msg_getSelfBuyList(Player player){
		ArrayList<AuctionInfo4Client> list = new ArrayList<AuctionInfo4Client>();
		for(Iterator<List<Auction>> it = getAuctionMap4Article().values().iterator();it.hasNext();){
			List<Auction> auctions = it.next();
			for(Auction auction : auctions){
				if(auction.getStatus()==Auction.STATUS_NORMAL&&auction.getPricePlayer().contains(player.getId())){
					list.add(auction.createInfo());
				}
			}
		}
		CompoundReturn compoundReturn = new CompoundReturn();
		compoundReturn.setObjValue(list);
		return compoundReturn;
	}
	
	private long logTime = 0;
	public void run() {
		/** 要删除的列表 */
		List<Auction> deleteList = new ArrayList<Auction>();
		while (true) {
			try {
				Thread.sleep(10000);
				if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
					continue;
				}
				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				if (logTime < now) {
					logTime = now + 1 * 60 * 1000;
					if (logger.isWarnEnabled()) {
						logger.warn("[拍卖钱统计] [a={}] [h={}] [s={}] [b={}] [j={}] [t={}] [c={}]", new Object[]{auctionMoney, haveMoney, saleMoney, backJingpaiMoney, jingpaiMoney, taxMoney, cancleMoney});
					}
				}
				Auction[] auctions = getAuctionMap().values().toArray(new Auction[0]);
				for (Auction auction : auctions) {
					if(auction.getEndDate()<=now&&auction.getStatus()==Auction.STATUS_NORMAL){
						timeOverSize += 1;
						haveSize -= 1;
						auction.setStatus(Auction.STATUS_EXPIRED);
						if(auction.getMaxPricePlayer()>0){
							//有竞拍者
							
							ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(auction.getArticleEntityId());
							
							if (articleEntity == null) {
								logger.error("[拍卖超时退错误] [有人竞拍] [entity不存在] [auction=" + auction.getLogString() + "]");
								continue;
							}
							
							Article article = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
							if (article == null) {
								logger.error("[拍卖超时退错误] [有人竞拍] [Article不存在] [auction=" + auction.getLogString() + "]");
								continue;
							}
							
							try {
								MailManager.getInstance().sendMail(auction.getMaxPricePlayer(), new ArticleEntity[]{articleEntity}, new int[]{auction.getCount()}, Translate.text_auction_003, Translate.text_auction_003, 0, 0, 0, "拍卖到期给竞拍者发物品");
								if (logger.isWarnEnabled()) logger.warn("[拍卖到期] [有人竞拍 ] [给竞拍者发物品] [{}]", new Object[]{auction.getLogString()});
							} catch (Exception e) {
								if (logger.isWarnEnabled()) logger.warn("拍卖物品超时,给竞拍者发物品出错:playerID=[{}] EntityID=[{}] NUM=[{}]", new Object[]{auction.getMaxPricePlayer(),auction.getArticleEntityId(), auction.getCount()});
							}
							try {
								if (GreenServerManager.isBindYinZiServer()) {
									Article a = ArticleManager.getInstance().getArticle(Translate.银票);
									YinPiaoEntity entity = (YinPiaoEntity)ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.绿色服, null, a.getColorType(), 1, true);
									entity.setHaveMoney(auction.getNowPrice());
									MailManager.getInstance().sendMail(auction.getSellerId(), new ArticleEntity[]{entity}, new int[]{1}, Translate.text_auction_001, Translate.text_auction_001, 0, 0, 0, "拍卖到期给拍卖者发钱");
								}else {
									MailManager.getInstance().sendMail(auction.getSellerId(), null, null, Translate.text_auction_001, Translate.text_auction_001, auction.getNowPrice(), 0, 0, "拍卖到期给拍卖者发钱");
								}
								saleMoney += auction.getNowPrice();
								haveMoney -= auction.getNowPrice();
								if (logger.isWarnEnabled()) logger.warn("[拍卖到期] [有人竞拍 ] [给拍卖者发钱] [{}]", new Object[]{auction.getLogString()});
							} catch (Exception e) {
								if (logger.isWarnEnabled()) logger.warn("拍卖物品超时,给拍卖者邮寄钱出错:playerID=[{}] Money=[{}]", new Object[]{auction.getSellerId(), auction.getNowPrice()});
							}
							long tax = auction.getNowPrice() * BUY1_TAX / 100;
							if (tax <= 0) {
								tax = 1;
							}
							PlayerSimpleInfo buy = PlayerSimpleInfoManager.getInstance().getInfoById(auction.getMaxPricePlayer());
							PlayerSimpleInfo sell = PlayerSimpleInfoManager.getInstance().getInfoById(auction.getSellerId());
							if (buy != null && sell != null) {
								ArticleStatManager.addToArticleStat(null, sell.getUsername(), sell.getLevel(), null, buy.getUsername(), buy.getLevel(), articleEntity.getArticleName(), article.getArticleLevel(), ArticleManager.getColorString(article, article.getColorType()), articleEntity.isBinded(), ArticleStatManager.OPERATION_交换, auction.getNowPrice() + tax, ArticleStatManager.YINZI, auction.getCount(), "购买拍卖成功", null);
							}else {
								logger.error("[拍卖到期统计] [失败] [因为买卖简单Player取不到] ["+auction.getLogString()+"]");
							}
							if (logger.isWarnEnabled()) logger.warn("竞拍物品到期发货:[买家:{}] [卖家:{}] [拍卖信息:{}] ", new Object[]{auction.getMaxPricePlayer(), auction.getSellerId(), auction.getLogString()});
						
							try {
								Player purchaser = PlayerManager.getInstance().getPlayer(auction.getMaxPricePlayer());
								Player seller = PlayerManager.getInstance().getPlayer(auction.getSellerId());
								MoneyRelationShipManager.getInstance().addMoneyMove(purchaser, seller, (int)(auction.getNowPrice()));
							} catch(Throwable e) {
								e.printStackTrace();
							}
							
							try {
								Player purchaser = PlayerManager.getInstance().getPlayer(auction.getMaxPricePlayer());
								Player seller = PlayerManager.getInstance().getPlayer(auction.getSellerId());
								ArticleRelationShipManager.getInstance().addArticleMove(seller, purchaser, auction.getCount());
							} catch(Throwable e) {
								e.printStackTrace();
							}
						}else{
							//超时  退物品
							ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(auction.getArticleEntityId());
							
							if (articleEntity == null) {
								logger.error("[拍卖超时退错误] [无人竞拍] [entity不存在] [auction=" + auction.getLogString() + "]");
								continue;
							}
							
							Article article = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
							if (article == null) {
								logger.error("[拍卖超时退错误] [无人竞拍] [Article不存在] [auction=" + auction.getLogString() + "]");
								continue;
							}
							
							try {
								MailManager.getInstance().sendMail(auction.getSellerId(), new ArticleEntity[]{ArticleEntityManager.getInstance().getEntity(auction.getArticleEntityId())}, new int[]{auction.getCount()}, Translate.text_auction_002, Translate.text_auction_002, 0, 0, 0, "给拍卖者退还物品");
							} catch (Exception e) {
								if (logger.isWarnEnabled()) logger.warn("拍卖物品超时退还物品出错:playerID=[{}] EntityID=[{}] NUM=[{}]" + e, new Object[]{auction.getSellerId(),auction.getArticleEntityId(), auction.getCount()});
							}
							PlayerSimpleInfo sell = PlayerSimpleInfoManager.getInstance().getInfoById(auction.getSellerId());
							if (sell != null) {
								ArticleStatManager.addToArticleStat(null, sell.getUsername(), sell.getLevel(), null, null, 0, articleEntity.getArticleName(), article.getArticleLevel(), ArticleManager.getColorString(article, article.getColorType()), articleEntity.isBinded(), ArticleStatManager.OPERATION_交换, 0, ArticleStatManager.YINZI, auction.getCount(), "拍卖过期", null);
							}else {
								logger.error("[拍卖到期统计] [失败] [因为简单Playersell取不到] ["+auction.getLogString()+"]");
							}
							if (logger.isWarnEnabled()) logger.warn("流拍物品到期:[卖家:{}] [卖家Name:{}] [拍卖信息:{}]", new Object[]{auction.getSellerId(), auction.getSellName(), auction.getLogString()});
						}
						deleteList.add(auction);
					}else if(auction.getStatus()==Auction.STATUS_FINISHED){
						deleteList.add(auction);
						if (!使用新日志格式) {
							if (logger.isWarnEnabled()) logger.warn("[操作类别:拍卖卖完] [状态:删除] [{}]", new Object[]{auction.getLogString()});
						} else {
							if (logger.isWarnEnabled()) {
								logger.warn("[操作类别:拍卖卖完] [状态:删除] [" + auction.getLogString4Newlog() + "]");
							}
						}
					}else if(auction.getStatus()==Auction.STATUS_EXPIRED){
						deleteList.add(auction);
						if (!使用新日志格式) {
							if (logger.isWarnEnabled()) logger.warn("[拍卖取消或过期] [删除] [{}]", new Object[]{auction.getLogString()});
						} else {
							if (logger.isWarnEnabled()) {
								logger.warn("[操作类型:拍卖取消或过期] [状态:删除] [" + auction.getLogString4Newlog() + "]");
							}
						}
					}
				}
				
				for(int i = 0; i<deleteList.size(); i++){
					Auction auction = deleteList.get(i);
					getAuctionMap().remove(auction.getId());
					Article article = ArticleManager.getInstance().getArticle(auction.getName());
					getAuctionMap4Article().get(article).remove(auction);
					try{
						em.remove(auction);
					}catch(Exception e) {
						logger.warn("AuctionManager.run:", e);
					}
				}
				
				deleteList.clear();
			} catch (Exception e) {
				logger.warn("AuctionManager.run:", e);
			}
		}
	}

	public void setAuctionMap4Article(Hashtable<Article, List<Auction>> auctionMap4Article) {
		this.auctionMap4Article = auctionMap4Article;
	}

	public Hashtable<Article, List<Auction>> getAuctionMap4Article() {
		return auctionMap4Article;
	}
	
}
