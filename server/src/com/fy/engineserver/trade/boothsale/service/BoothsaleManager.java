package com.fy.engineserver.trade.boothsale.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.core.res.MapPolyArea;
import com.fy.engineserver.dajing.DajingStudioManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecord;
import com.fy.engineserver.economic.thirdpart.migu.entity.ArticleTradeRecordManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.green.GreenServerManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.BOOTHSALE_LEAVE_BOOTH_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.smith.ArticleRelationShipManager;
import com.fy.engineserver.smith.MoneyRelationShipManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.trade.Message;
import com.fy.engineserver.trade.TradeManager;
import com.fy.engineserver.trade.abnormalCount.TradeAbnormalManager;
import com.fy.engineserver.trade.boothsale.BoothSale;
import com.fy.engineserver.trade.boothsale.SoftLink4Package;
import com.fy.engineserver.trade.exceptions.ArticleHasBindedException;
import com.fy.engineserver.trade.exceptions.CellEmptyException;
import com.fy.engineserver.trade.exceptions.EntityNotFoundException;
import com.fy.engineserver.trade.exceptions.NoEnoughNumberException;
import com.fy.engineserver.trade.exceptions.NoEnoughPositionException;
import com.fy.engineserver.trade.exceptions.OutOfIndexException;
import com.fy.engineserver.trade.exceptions.WrongNumberException;
import com.fy.engineserver.trade.exceptions.WrongPriceException;
import com.fy.engineserver.trade.exceptions.WrongStateException;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;

public class BoothsaleManager {// implements SaleAction {

	private static BoothsaleManager instance;
//	public static Logger logger = Logger.getLogger(BoothsaleManager.class);
public	static Logger logger = LoggerFactory.getLogger(BoothsaleManager.class);

//买的税
	public static int BUY_TAX = 5;

	/** 摆摊的最小等级 */
	public static int LV_MIN = 20;

	/** 摆摊的默认格数 */
	public static int DEFAULT_SIZE = 20;
	/** 广告语长度 */
	public static int MAX_LEN = 14;
	/** 广告语显示间隔 秒 */
	public static int cycle = 3;
	/** 当前服务器上的摆摊信息 <角色ID,摊位> */
	public Hashtable<Long, BoothSale> boothSaleMap = new Hashtable<Long, BoothSale>();

	private String configFile;
	
	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public static BoothsaleManager getInstance() {
		return instance;
	}

	int DAY = 0;
	int MONEY = 1;
	
	private Map<Integer, Long> boothConfig = new HashMap<Integer, Long>();
	
	public void initFile() throws Exception{
		File file = new File(configFile);
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);

		HSSFSheet sheet = workbook.getSheetAt(0);
		if (sheet == null) return;
		int rows = sheet.getPhysicalNumberOfRows();
		
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row != null) {
				HSSFCell cell = row.getCell(DAY);
				int level = (int) (cell.getNumericCellValue());
				cell = row.getCell(MONEY);
				long exp = (long) (cell.getNumericCellValue());
				boothConfig.put(level, exp);
			}
		}
		logger.warn("[booth加载成功] ["+boothConfig+"]");
	}
	
	/**
	 * 开始摆摊
	 * @param player
	 */
	public void beginBoothSale(Player player) {
		boothSaleMap.put(player.getId(), player.getBoothSale());
	}

	/**
	 * 开始离线摆摊
	 * @throws  
	 * @throws Exception 
	 * @throws Exception 
	 */
	public void beginBoothSaleOffline(Player player,int hour,long costMoney) {
		if(player == null || hour <= 0 || costMoney <= 0 || hour > 24){
			logger.error("[离线摆摊] [失败:参数错误] [player:{}] [hour:{}] [costMoney:{}]",new Object[]{(player==null?"null":player.getLogString()),hour,costMoney});
			return;
		}
		if(player.getBoothName() == null || player.getBoothName().isEmpty() || player.getBoothSale() == null){
			logger.error("[离线摆摊] [失败:摆摊信息错误] [boothName:{}] [BoothSale:{}] [player:{}]",new Object[]{player.getBoothName(),player.getBoothSale(),player.getLogString()});
			return;
		}
		try {
			BillingCenter.getInstance().playerExpense(player, costMoney, CurrencyType.BIND_YINZI, ExpenseReasonType.BOOTH, "离线摆摊");
			player.setEndBoothTime(System.currentTimeMillis() + hour * 60 * 60 * 1000L);
			player.sendError(Translate.离线摆摊成功);
			logger.error("[离线摆摊] [成功] [摊位:{}] [时长:{}] [花费:{}] [结束日期:{}] [player:{}]",new Object[]{player.getBoothName(),hour,costMoney,TimeTool.formatter.varChar23.format(player.getEndBoothTime()), player.getLogString()});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[离线摆摊] [异常] [摊位:{}] [时长:{}] [花费:{}] [结束日期:{}] [player:{}] [{}]",new Object[]{player.getBoothName(),hour,costMoney,TimeTool.formatter.varChar23.format(player.getEndBoothTime()), player.getLogString(),e});
		} 
	}
	
	public boolean isBoothOffLine(Player p){
		return System.currentTimeMillis() < p.getEndBoothTime();
	}
	
	
	/**
	 * 取消摆摊(收摊)
	 * @param player
	 */
	public synchronized CompoundReturn msg_cancelBoothSale(Player player) {
		if (!player.isBoothState()) {
			return new CompoundReturn().setByteValue((byte) 0).setStringValue("");
		}
		player.setTradeState(TradeManager.TRADE_STATE_NO);
		if(player.getBoothSale() != null){
			for(int i = 0; i < player.getBoothSale().getObserver().size(); i++){
				BOOTHSALE_LEAVE_BOOTH_RES res = new BOOTHSALE_LEAVE_BOOTH_RES(GameMessageFactory.nextSequnceNum());
				Player see = null;
				long seeID = player.getBoothSale().getObserver().get(i);
				if (PlayerManager.getInstance().isOnline(seeID)) {
					try {
						see = PlayerManager.getInstance().getPlayer(seeID);
					} catch (Exception e) {
						BoothsaleManager.logger.error("取摆摊观察者出错:" + e);
					}
					if (see != null) {
						if (see.getSeeBoothSale() == player.getId()) {
							see.setSeeBoothSale(0);
							see.addMessageToRightBag(res);
						}
					}
				}
			}
		}
		player.getBoothSale().getObserver().clear();
		player.setBoothState(false);
		player.setEndBoothTime(0);
		player.setBoothName("");
		boothSaleMap.remove(player.getId());
		if (logger.isInfoEnabled()) logger.info("[取消摆摊] [成功] [pID={}] [pName={}] [isBooth={}]", new Object[]{player.getId(), player.getName(), player.isBoothState()});
		return new CompoundReturn().setByteValue((byte) 0).setStringValue("");
	}

	/**
	 * 设置摊位名称和广告语
	 * @param player
	 * @param advertising
	 * @return
	 */
	public CompoundReturn msg_setNameAdvertising(Player player, String setsalername, String advertising) {
		byte result = 0;
		String info = "";
		boolean succ = true;
		if (player.getBoothSale() == null || player.getBoothSale().getLink() == null) {
			player.sendError(Translate.text_boothsale_003);
			return null;
		}
		for (int i = 0 ; i < player.getBoothSale().getLink().length; i++) {
			SoftLink4Package link4Package = player.getBoothSale().getLink()[i];
			if (link4Package.getIndexOfPackage() >= 0 && link4Package.getEntityId() > 0 ) {
				Cell cell = player.getBoothSale().getCell2Pag(link4Package);
				if (cell == null) {
					player.sendError(Translate.text_boothsale_004);
					link4Package.setIndexOfPackage(-1);
					link4Package.setEntityId(-1);
					link4Package.setPrice(0);
					return null;
				}
				if (cell.getEntityId() != link4Package.getEntityId()) {
					player.sendError(Translate.text_boothsale_004);
					link4Package.setIndexOfPackage(-1);
					link4Package.setEntityId(-1);
					link4Package.setPrice(0);
					return null;
				}
				ArticleEntity en = ArticleEntityManager.getInstance().getEntity(cell.getEntityId());
				if (en == null) {
					player.sendError(Translate.text_boothsale_005);
					return null;
				}
				if (en.isBinded() || en.isRealBinded()) {
					player.sendError(Translate.text_boothsale_006);
					return null;
				}
			}
		}
		
		if (isLegalAdvertising(player, setsalername)) {
			player.getBoothSale().setsalername(setsalername);
			player.setBoothName(setsalername);
			player.getBoothSale().setAdvertising(modifyAdvertising(advertising));
			result = 0;
			info = player.getBoothSale().getAdvertising();
			player.setTradeState(TradeManager.TRADE_STATE_BOOOTH);
			player.setBoothState(true);
			beginBoothSale(player);
			if (logger.isWarnEnabled()) logger.warn("[摆摊] [成功] [pid={}] [pName={}] [招牌={}] [{}]", new Object[]{player.getId(), player.getName(), setsalername, player.getBoothSale().getLogString()});
		} else {
			result = 1;
			succ = false;
		}
		return new CompoundReturn().setByeValue(result).setStringValue(info).setBooleanValue(succ);
	}

	public CompoundReturn mes_changeAdvertising(Player player, String advertising){
		byte result = 0;
		boolean succ = true;
		if (isLegalAdvertising(player, advertising)) {
			player.getBoothSale().setAdvertising(modifyAdvertising(advertising));
			advertising = player.getBoothSale().getAdvertising();
			if (logger.isInfoEnabled()) logger.info("[摆摊] [修改招牌] [pid={}] [pName={}] [招牌={}] [{}]", new Object[]{player.getId(), player.getName(), advertising, player.getBoothSale().getLogString()});
			result = 0;
		}else{
			result = 1;
			succ = false;
		}
		return new CompoundReturn().setByeValue(result).setStringValue(advertising).setBooleanValue(succ);
	}
	
	/**
	 * 留言
	 * @param from
	 * @param to
	 * @param message
	 * @throws WrongStateException
	 */
	public void msg_leaveWord(Player from, Player to, String content) throws WrongStateException {
		if (from.isInBoothSale() || !to.isInBoothSale()) {
			throw new WrongStateException();
		}
		if (logger.isInfoEnabled()) logger.info("[摆摊] [留言] [formID={}] [fromName={}] [toID={}] [toName={}] [content={}]", new Object[]{from.getId(), from.getName(), to.getId(), to.getName(), content});
		if(isBoothOffLine(to)){
			try {
				String mailContentString = content;
				if(from != null){
					mailContentString = from.getName()+"："+content;
				}
				MailManager.getInstance().sendMail(to.getId(), new ArticleEntity[]{}, Translate.离线摆摊邮件标题, mailContentString, 0, 0, 0, "离线摆摊邮件");
				if (logger.isInfoEnabled()) 
					logger.info("[摆摊] [留言:离线摆摊发送邮件] [formID={}] [fromName={}] [toID={}] [toName={}] [mailContentString={}]", new Object[]{from.getId(), from.getName(), to.getId(), to.getName(), mailContentString});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Message message = Message.createTalkMessage(from, to, content);
		to.getBoothSale().noticeChange(null, new Message[] { message });
	}

	/**
	 * 创建默认的摊位
	 * @param player
	 * @return
	 */
	public static BoothSale createDefalutBoothSale(Player player) {
		BoothSale boothSale = new BoothSale();
		boothSale.setOwner(player.getId());
		SoftLink4Package[] items = new SoftLink4Package[DEFAULT_SIZE];
//		byte[] falgs = new byte[DEFAULT_SIZE];
		for (int i = 0; i < DEFAULT_SIZE; i++) {
			items[i] = new SoftLink4Package( -1, -1, -1);
//			falgs[i] = 0;
		}
		boothSale.setLink(items);
//		boothSale.setChangeFalg(falgs);
		return boothSale;
	}

	public synchronized CompoundReturn msg_cancel(Player saler, int index) throws OutOfIndexException, EntityNotFoundException {
		BoothSale boothSale = saler.getBoothSale();
		if(index<0 || index > boothSale.getLink().length){
			throw new OutOfIndexException();
		}
		SoftLink4Package saleItem = boothSale.getItem(index);
		Cell cell = boothSale.getCell2Pag(saleItem);
		if (cell == null) {
			throw new OutOfIndexException();
		}
		ArticleEntityManager am = ArticleEntityManager.getInstance();
		ArticleEntity entity = am.getEntity(cell.getEntityId());

		if (entity == null) {
			throw new EntityNotFoundException();
		}
		if (logger.isWarnEnabled()) logger.warn("[摆摊] [取消出售] [pid={}] [pName={}] [index={}] [entityID={}] [entityIndex={}] [price={}]", new Object[]{saler.getId(), saler.getName(), index, saleItem.getEntityId(), saleItem.getIndexOfPackage(), saleItem.getPrice()});
		saler.getBoothSale().remove(index);
		saler.getBoothSale().noticeChange(new int[] { index }, null);
		return CompoundReturn.createCompoundReturn().setByteValue((byte)0).setIntValues(new int[]{index, saleItem.getPackageType(), saleItem.getIndexOfPackage()});
	}

	public synchronized CompoundReturn msg_trade(Player purchaser, Player saler, int index, int num, long entityID) throws OutOfIndexException, EntityNotFoundException, NoEnoughNumberException, NoEnoughMoneyException, NoEnoughPositionException, WrongStateException {
		// 是否能购买物品
		synchronized (saler.tradeKey) {
			if (!canBuy(purchaser, saler, index, num, entityID)) {
				return null;
			}
			doRelBuy(purchaser, saler, index, num);
		}
		return new CompoundReturn();
	}

	public boolean canBuy(Player purchaser, Player saler, int index, int num, long entityID) throws OutOfIndexException, NoEnoughNumberException, EntityNotFoundException, NoEnoughPositionException, NoEnoughMoneyException, WrongStateException {
		if (!saler.isInBoothSale()) {
			throw new WrongStateException();
		}
		BoothSale boothSale = saler.getBoothSale();
		if(index<0 || index > boothSale.getLink().length){
			logger.error("[是否可以买] [外挂] [p={}] [index={}]", new Object[]{saler.getLogString(), index});
			throw new OutOfIndexException();
		}
		if (num <= 0 || num > 999) {
			logger.error("[是否可以买] [外挂] [p={}] [num={}]", new Object[]{saler.getLogString(), num});
			return false;
		}
		SoftLink4Package saleItem = boothSale.getItem(index);
		Cell cell = boothSale.getCell2Pag(saleItem);
		if (cell == null || cell.getCount() < num) {
			throw new NoEnoughNumberException();
		}

		ArticleEntityManager am = ArticleEntityManager.getInstance();
		ArticleEntity entity = am.getEntity(cell.getEntityId());
		if (entity == null) {
			throw new EntityNotFoundException();
		}
		if (cell.getEntityId() != saleItem.getEntityId()) {
			purchaser.sendError(Translate.text_boothsale_007);
			return false;
		}
		if (entityID != -1000) {
			if (entity.getId() != entityID) {
				purchaser.sendError(Translate.text_boothsale_007);
				return false;
			}
		}
		if (entity.isBinded()) {
			purchaser.sendError(Translate.text_boothsale_008);
			return false;
		}
		if (!purchaser.canAddArticle(entity)) {
			throw new NoEnoughPositionException();
		}
		// 钱不够
		long duty = accountDuty(saler, saleItem.getPrice(), num);
		if (purchaser.getSilver() < duty+saleItem.getPrice()*num || (duty+saleItem.getPrice()*num) < 0 || duty < 0) {
			throw new NoEnoughMoneyException();
		}
		return true;
	}

	private synchronized void doRelBuy(Player purchaser, Player saler, int index, int num) {
		SoftLink4Package item = saler.getBoothSale().getItem(index);
		Cell cell = saler.getBoothSale().getCell2Pag(item);
		ArticleEntityManager am = ArticleEntityManager.getInstance();
		ArticleEntity entity = am.getEntity(cell.getEntityId());
		//买家操作
		long duty = accountDuty(saler, item.getPrice(), num);
		long price = item.getPrice()*num;
		try {
			if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
				BillingCenter.getInstance().playerExpense(purchaser, price, CurrencyType.YINZI, ExpenseReasonType.BOOTH, "在摆摊上购买物品", -1);
				BillingCenter.getInstance().playerExpense(purchaser, duty, CurrencyType.YINZI, ExpenseReasonType.BOOTH, "在摆摊上购买物品");
			} else {
				BillingCenter.getInstance().playerExpense(purchaser, price+duty, CurrencyType.YINZI, ExpenseReasonType.BOOTH, "在摆摊上购买物品");
			}
			NewChongZhiActivityManager.instance.doXiaoFeiActivity(purchaser, duty, NewXiaoFeiActivity.XIAOFEI_TYPE_TAX);
//			ChongZhiActivity.getInstance().doXiaoFeiActivity(purchaser, duty, XiaoFeiServerConfig.XIAOFEI_TONGDAO_JIAOYISHUI);
//			ChongZhiActivity.getInstance().doXiaoFeiMoneyActivity(purchaser, duty, MoneyBillBoardActivity.XIAOFEI_TONGDAO_JIAOYISHUI);
			if (logger.isWarnEnabled()) logger.warn("[摆摊] [购买扣钱] [buyid={}] [buyName={}] [salerid={}] [salerName={}] [index={}] [entityID={}] [entityIndex={}] [price={}] [buynum={}] [money={}]", new Object[]{purchaser.getId(), purchaser.getName(), saler.getId(), saler.getName(), index, item.getEntityId(), item.getIndexOfPackage(), item.getPrice(), num, price+duty});
		} catch (NoEnoughMoneyException e) {
			if(logger.isWarnEnabled())
				logger.warn("买家扣钱出错：", e);
			return;
		} catch (BillFailedException e) {
			if(logger.isWarnEnabled())
				logger.warn("买家扣钱出错：", e);
			return;
		}
//		purchaser.setRmbyuanbao(purchaser.getRmbyuanbao()-(duty+price));
		//卖家操作
		try {
			BillingCenter.getInstance().playerSaving(saler, price, CurrencyType.SHOPYINZI, SavingReasonType.BOOTH, "在摆摊出售了东西");
			if (logger.isWarnEnabled()) logger.warn("[摆摊] [卖家得钱] [buyid={}] [buyName={}] [salerid={}] [salerName={}] [index={}] [entityID={}] [entityIndex={}] [price={}] [buynum={}] [money={}]", new Object[]{purchaser.getId(), purchaser.getName(), saler.getId(), saler.getName(), index, item.getEntityId(), item.getIndexOfPackage(), item.getPrice(), num, price});
		} catch (SavingFailedException e) {
			if(logger.isWarnEnabled())
				logger.warn("卖家加钱出错：", e);
			return;
		}
		
		for(int i = num;--i>=0;){
			purchaser.putToKnapsacks(entity,"摆摊");
		}
		
//		try{
//			if(WhiteListManager.getInstance().isWhiteListPlayer(purchaser)){
//				WhiteListManager.getInstance().addMailRowData(purchaser, saler, com.fy.engineserver.util.whitelist.WhiteListManager.ActionType.求购,(price+duty), new String[]{entity.getArticleName()}, new int[]{entity.getColorType()}, new int[]{num},"");
//			}
////			if(WhiteListManager.getInstance().isWhiteListPlayer(saler)){
////				WhiteListManager.getInstance().addMailRowData(saler, purchaser, com.fy.engineserver.util.whitelist.WhiteListManager.ActionType.摆摊出售,price, new String[]{entity.getArticleName()}, new int[]{entity.getColorType()}, new int[]{num});
////			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		saler.getKnapsack_common().clearCell(item.getIndexOfPackage(), num, "摆摊出售", false);
//		if(cell.getCount()>num){
//			saler.getKnapsack_common().updateCell(item.getIndexOfPackage(), cell.getEntityId(), cell.getCount()-num, "摆摊");
//		}else{
//			saler.getKnapsack_common().clearCell(item.getIndexOfPackage(), "摆摊出售", false);
////			saler.getKnapsack_common().updateCell(item.getIndexOfPackage(), null);
//		}
		
		//摆摊做标记
		{
			try
			{
				ArticleTradeRecord articleTradeRecord =  new ArticleTradeRecord();
				articleTradeRecord.setSellPlayerId(saler.getId());
				articleTradeRecord.setBuyPlayerId(purchaser.getId());
				
				long[] articleIds = new long[1];
				articleIds[0] = entity.getId();
				articleTradeRecord.setArticleIds(articleIds);
				
				articleTradeRecord.setDesc("摆摊");
				articleTradeRecord.setTradeTime(System.currentTimeMillis());
				
				ArticleTradeRecordManager.getInstance().notifyNew(articleTradeRecord);
			}
			catch(Exception e)
			{
				ArticleTradeRecordManager.logger.error("[创建摆摊交易记录] [失败] [出现未知异常] [saler:"+saler.getId()+"] [purchaser:"+purchaser.getId()+"]",e);
			}
			
			
		}
		
		
		
		SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = da.format(new Date(SystemTime.currentTimeMillis()));
		ArticleStatManager.addToArticleStat(saler, purchaser, entity, ArticleStatManager.OPERATION_交换, price+duty, ArticleStatManager.YINZI, num, "摆摊出售", null);
		TradeAbnormalManager.getInstance().isNormalBoothsale(saler, purchaser, entity, num, price);
		DajingStudioManager.getInstance().notify_摆摊交易银锭(saler, purchaser, price);
		if (logger.isWarnEnabled()) logger.warn("[摆摊] [交易] [buyid={}] [buyName={}] [salerid={}] [salerName={}] [index={}] [entityID={}] [entityName={}] [color={}] [entityIndex={}] [price={}] [buynum={}] [money={}] [{}]", new Object[]{purchaser.getId(), purchaser.getName(), saler.getId(), saler.getName(), index, item.getEntityId(), entity.getArticleName(), entity.getColorType(), item.getIndexOfPackage(), item.getPrice(), num, saler.getBoothSale().getLogString()});
		String msg = time+Translate.text_boothsale_009+"<f color='0x00ccca'>"+purchaser.getName()+"</f>"+num+Translate.个+entity.getArticleName()+Translate.text_boothsale_010+price+Translate.文;
		saler.getBoothSale().noticeChange(new int[] { index }, new Message[] {
				Message.createTradeMessage(purchaser, saler, msg)});
		
		if(isBoothOffLine(saler)){
			try {
				MailManager.getInstance().sendMail(saler.getId(), new ArticleEntity[]{}, Translate.离线摆摊邮件购买标题, msg, 0, 0, 0, "离线摆摊出售成功");
				if (logger.isWarnEnabled()) 
					logger.warn("[摆摊] [交易:离线交易] [buyid={}] [buyName={}] [salerid={}] [salerName={}] [index={}] [entityID={}] [entityName={}] [color={}] [entityIndex={}] [price={}] [buynum={}] [money={}] [{}]", new Object[]{purchaser.getId(), purchaser.getName(), saler.getId(), saler.getName(), index, item.getEntityId(), entity.getArticleName(), entity.getColorType(), item.getIndexOfPackage(), item.getPrice(), num, saler.getBoothSale().getLogString()});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			MoneyRelationShipManager.getInstance().addMoneyMove(purchaser, saler, (int)price);
		} catch(Throwable e) {
			e.printStackTrace();
		}
		
		try {
			ArticleRelationShipManager.getInstance().addArticleMove(saler, purchaser, num);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择物品出售
	 * @param saler
	 * @param bagType
	 * @param index
	 * @param num
	 * @param perPrice
	 * @return
	 * @throws OutOfIndexException
	 * @throws CellEmptyException
	 * @throws ArticleHasBindedException
	 * @throws NoEnoughNumberException
	 * @throws NoEnoughPositionException
	 * @throws WrongPriceException
	 * @throws WrongNumberException
	 */
	public synchronized CompoundReturn msg_selectSale(Player saler, int index, int num, long perPrice) throws OutOfIndexException, CellEmptyException, ArticleHasBindedException, NoEnoughNumberException, NoEnoughPositionException, WrongPriceException, WrongNumberException {
		int bagType = 0;
		Knapsack knapsack = saler.getKnapsack_common();
		if (knapsack == null) {
			return null;
		}
		if (num <= 0 || num > 999) {
			logger.error("[摆摊出售物品] [外挂] [p={}] [num={}]", new Object[]{saler.getLogString(), num});
			throw new WrongNumberException();
		}
		if (perPrice <= 0 || perPrice > 999999999) {
			logger.error("[摆摊出售物品] [外挂] [p={}] [price={}]", new Object[]{saler.getLogString(), perPrice});
			throw new WrongPriceException();
		}
		if (index < 0 || index > knapsack.size()) {
			throw new OutOfIndexException();
		}

		if (!currPlaceCanBooth(saler)) {
//			logger.error("[玩家选择出售位置错误][角色:" + saler.getName() + "]地点[" + saler.getCurrentMapAreaName() + "]");
			if(logger.isWarnEnabled())
				logger.warn("[玩家选择出售位置错误][角色:{}]地点[{}]", new Object[]{saler.getName(),saler.getCurrentMapAreaName()});
			return CompoundReturn.createCompoundReturn().setByteValue((byte) 1).setStringValue(Translate.text_trade_000).setBooleanValue(false);
		}
		Cell cell = knapsack.getCell(index);
		if (cell == null || cell.isEmpty()) {
			throw new CellEmptyException();
		}

		if (cell.getCount() < num) {
			throw new NoEnoughNumberException();
		}
		long entityId = cell.getEntityId();

		ArticleEntityManager am = ArticleEntityManager.getInstance();
		ArticleEntity entity = am.getEntity(entityId);

		if (entity.isBinded() || entity.isRealBinded()) {
			throw new ArticleHasBindedException();
		}
		
		if (!GreenServerManager.canToOtherPlayer(entity)) {
			saler.sendError(Translate.text_trade_023);
			return null;
		}
		
		if (!ArticleProtectManager.instance.isCanDo(saler, ArticleProtectDataValues.ArticleProtect_Common, entity.getId())){
			saler.sendError(Translate.锁魂物品不能做此操作);
			return null;
		}

		BoothSale boothSale = saler.getBoothSale();
		int newIndex = -1;
		if (boothSale.isFull()) {
			throw new NoEnoughPositionException();
		}
		
		for (int i = 0; i < boothSale.getLink().length; i++) {
			SoftLink4Package link = boothSale.getLink()[i];
			if (link != null ) {
				if (link.getPackageType() == bagType && link.getIndexOfPackage() == index) {
					if (link.getEntityId() > 0){
						saler.sendError(Translate.text_boothsale_011);
						return null;
					}
				}
			}
		}
		
		synchronized (cell) {
			if (cell.getCount() < num) {
				throw new NoEnoughNumberException();
			}
			SoftLink4Package saleItem = new SoftLink4Package(bagType, index, perPrice);
			saleItem.setEntityId(entityId);
			newIndex = boothSale.addSaleItem(saleItem);
		}
		if (logger.isWarnEnabled()) logger.warn("[摆摊] [放入物品] [salerid={}] [salerName={}] [Boothindex={}] [entityID={}] [entityName={}] [color={}] [entityIndex={}] [price={}] [num={}] [{}]", new Object[]{saler.getId(), saler.getName(), newIndex, entity.getId(), entity.getArticleName(), entity.getColorType(), index, perPrice, num, saler.getBoothSale().getLogString()});
		saler.getBoothSale().noticeChange(new int[] { newIndex}, null);
		return CompoundReturn.createCompoundReturn().setIntValues(new int[]{newIndex, cell.getCount()}).setLongValues(new long[]{cell.getEntityId(), perPrice});
	}

	/**
	 * 广告语是否合法
	 * @param player
	 * @param advertising
	 * @return
	 */
	public boolean isLegalAdvertising(Player player, String advertising) {
		if (advertising.getBytes().length > 24) {
			return false;
		}
		return true;
	}

	public String modifyAdvertising(String advertising) {
		return advertising.length() > MAX_LEN ? advertising.substring(0, MAX_LEN) : advertising;
	}

	/**
	 * 判断玩家所在的区域是否可以摆摊
	 * @param player
	 * @return
	 */
	public CompoundReturn msg_canBoothSale(Player player) {
		// TODO 当前区域能否摆摊
		if (!currPlaceCanBooth(player)) {
//			logger.error("[玩家摆摊位置错误][角色:" + player.getName() + "]地点[" + player.getCurrentMapAreaName() + "]");
			if(logger.isInfoEnabled())
				logger.info("[玩家摆摊位置错误][角色:{}]地点[{}]", new Object[]{player.getName(),player.getCurrentMapAreaName()});
			return CompoundReturn.createCompoundReturn().setByteValue((byte) 1).setStringValue(Translate.text_trade_000).setBooleanValue(false);
		}
		boolean bResult = true;
		String info = "";
		byte result = 0;
		if (player.getLevel() < LV_MIN) {
			bResult = false;
			result = 1;
			info = Translate.text_trade_001 + LV_MIN;
		}
		return CompoundReturn.createCompoundReturn().setByteValue(result).setStringValue(info).setBooleanValue(bResult);
	}

	/**
	 * 当前地点是否能摆摊
	 * @param place
	 * @return
	 */
	public boolean currPlaceCanBooth(Player player) {
		MapPolyArea[] areas = player.getCurrentGame().getGameInfo().getMapPolyAreaByPoint(player.getX(), player.getY());
		for (int i = 0; i< areas.length;i++) {
			if (areas[i] != null&&areas[i].type == 2) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 据算税率
	 * @param player
	 * @param perPrice
	 * @param num
	 * @return
	 */
	public static long accountDuty(Player player, long perPrice, int num) {
		long tax = perPrice * num * BUY_TAX/100;
		if(tax == 0){
			tax = 1;
		}
		return tax;
	}
	
	public Map<Integer, Long> getBoothConfig() {
		return boothConfig;
	}

	void init() throws Exception {
		
		instance = this;
		initFile();
		ServiceStartRecord.startLog(this);
	}

	void destroy() {

	}

}
