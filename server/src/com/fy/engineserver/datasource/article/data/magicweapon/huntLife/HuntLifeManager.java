package com.fy.engineserver.datasource.article.data.magicweapon.huntLife;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.client.Shouhun4Client;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.client.TempLuckModel;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model.HuntArticleModel;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model.HuntBaseModel;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model.ShouHunModel;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.huntLife.Option_ConfirmChouJiang;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.TAKE_SHOUHUN_LUCK_REQ;
import com.fy.engineserver.message.TAKE_SHOUHUN_LUCK_RES;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.ResponseMessage;

public class HuntLifeManager {
	public static Logger logger = MagicWeaponManager.logger;
	
	public static HuntLifeManager instance;
	//0气血  1攻击  2外防  3内防  4暴击   5命中   6闪避   7破甲   8精准   9免暴     //攻击需要根据玩家职业增加对应的法攻或外攻
	public static final int[] 命格属性类型 = new int[]{0,1,2,3,4,5,6,7,8,9};		//按照顺序开启,每个格子只能镶嵌对应属性的命格
	public static String[] 对应兽魂道具名 = Translate.对应增加属性描述;		
	public static String[] 对应增加属性描述 = Translate.对应增加属性描述;		
	public static int[] 等级对应物品颜色 = new int[]{0,1,2,3,3,3,4,4,4,4};		
	/** 基础配置 */
	public HuntBaseModel baseModel;
	/** 兽魂等级配置 key=level*/
	public Map<Integer, ShouHunModel> shouhunModels = new LinkedHashMap<Integer, ShouHunModel>();
	/** 抽取物品model  key=物品id */
	public Map<Integer, HuntArticleModel> articleMaps = new LinkedHashMap<Integer, HuntArticleModel>();
	
	public Map<Integer, String> translate = new HashMap<Integer, String>();
	
	public static volatile List<Long> tempIdList = new ArrayList<Long>();		//单抽弹窗确认
	public static volatile List<Long> tempIdList2 = new ArrayList<Long>();		//十连抽弹窗确认
	
	public long allNeedExp = 0;
	
	public int onceTotalProb = 0;				//单抽概率总和
	public int tenTotalProb = 0;				//十连抽概率总和
	public int tenMustProb = 0;					//十连抽必产概率总和
	
	public static final int 十连抽次数 = 10;	//有可能改为11个
	
	public static final int 兽魂满级 = 9;
	
	public static int 紫色最多产出个数 = 1;			//除去必出紫色道具
	
	private String fileName;
	
	public void init() throws Exception {
		instance = this;
		loadFile();
		initAllNeexExp();
	}
	
	public static void main(String[] args) throws Exception {
		HuntLifeManager manager = new HuntLifeManager();
		manager.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//huntLife//huntLife.xls");
		manager.loadFile();
	}
	
	public static void popConfirmWindow2(Player p, int takeType ) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_ConfirmChouJiang option1 = new Option_ConfirmChouJiang();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setChoujiangType(takeType);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		long cost = 0;
		if (takeType == 0) {
			cost = HuntLifeManager.instance.baseModel.getOnceCost();
		} else {
			cost = HuntLifeManager.instance.baseModel.getTenCost();
		}
		String str = String.format(Translate.猎命抽奖确认, BillingCenter.得到带单位的银两(cost));
		mw.setDescriptionInUUB(str);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	
	public Shouhun4Client createModel4Client(Player player, int attrType) {
		if (player.getHuntLifr() == null) {
			Shouhun4Client result = new Shouhun4Client(-1, 1, 0, 0, attrType, 0,0);
			return result;
		}
		long articleId = player.getHuntLifr().getHuntDatas()[attrType];
		int level = 1;
		long currentExp = 0;
		long needExp = 0;
		int attrNum = 0;
		int colorType = 0;
		if (articleId > 0) {
			HuntLifeArticleEntity ae = (HuntLifeArticleEntity) DefaultArticleEntityManager.getInstance().getEntity(articleId);
			level = ae.getExtraData().getLevel();
			currentExp = ae.getExtraData().getExps();
			ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(level);
			needExp = model.getExp4Up();
			attrNum = model.getAddAttrNums()[attrType];
			colorType = ae.getColorType();
			if (level == 兽魂满级) {
				currentExp = needExp;
			}
		}
		Shouhun4Client result = new Shouhun4Client(articleId, level, currentExp, needExp, attrType, attrNum, colorType);
		return result;
	}
	//类型    等级（最低在最前）  经验（最低在最前）
	
	public static void sort(long[] arr){
		synchronized (arr) {
			TempHuntArticle2[] aa = new TempHuntArticle2[arr.length];
		    for(int i=0;i<arr.length;i++){
		    	int level = -1;
		    	long exp = -1;
		    	int addType = -1;
		    	if (arr[i] > 0) {
		    		HuntLifeArticleEntity hae = (HuntLifeArticleEntity) DefaultArticleEntityManager.getInstance().getEntity(arr[i]);
		    		level = hae.getExtraData().getLevel();
		    		exp = hae.getExtraData().getExps();
		    		HuntLifeArticle a = (HuntLifeArticle) ArticleManager.getInstance().getArticle(hae.getArticleName());
		    		addType = a.getAddAttrType();
		    	}
		    	aa[i] = new TempHuntArticle2(arr[i], level, exp, addType);
		    }
		    sort(aa);
		    for (int i=0; i<arr.length; i++) {
		    	arr[i] = aa[i].articleId;
		    }
		}
	}
	
	public static void sort(TempHuntArticle2[] arr) {
		for(int i=0;i<arr.length-1;i++){
			for(int j=i+1;j<arr.length;j++){
				if (arr[i].articleId <= 0) {
					TempHuntArticle2 temp=arr[i];
					arr[i]=arr[j];
					arr[j]=temp;
				} else if (arr[i].articleId > 0 && arr[j].articleId > 0) {
					if (arr[i].addType > arr[j].addType) {
						TempHuntArticle2 temp=arr[i];
						arr[i]=arr[j];
						arr[j]=temp;
					} else if (arr[i].addType == arr[j].addType) {
						if (arr[i].level > arr[j].level) {
							TempHuntArticle2 temp=arr[i];
							arr[i]=arr[j];
							arr[j]=temp;
						}else if (arr[i].level == arr[j].level) {
							if (arr[i].exp > arr[j].exp) {
								TempHuntArticle2 temp=arr[i];
								arr[i]=arr[j];
								arr[j]=temp;
							}
						}
					}
				}
			}
		}
	}
	
	public static void sort(TempHuntArticle[]arr){
		for(int i=0;i<arr.length-1;i++){
			for(int j=i+1;j<arr.length;j++){
				if(arr[i].level<arr[j].level){
					TempHuntArticle temp=arr[i];
					arr[i]=arr[j];
					arr[j]=temp;
				} else if (arr[i].level == arr[j].level && arr[i].exp < arr[j].exp) {
					TempHuntArticle temp=arr[i];
					arr[i]=arr[j];
					arr[j]=temp;
				}
			}
		}
	}
	
	public long getExpByLevel(int level) {
		if (level <= 1) {
			return 0;
		}
		long result = 0;
		Iterator<Integer> ito = shouhunModels.keySet().iterator();
		while(ito.hasNext()) {
			int key = ito.next();
			ShouHunModel m = shouhunModels.get(key);
			if (m.getLevel() < level) {
				result += m.getExp4Up();
			}
		}
		return result;
	}
	
	public static void sendMsg2World(String str) {
		try {
			ChatMessageService cms = ChatMessageService.getInstance();
			ChatMessage msg = new ChatMessage();
			msg.setMessageText(str);
			cms.sendMessageToSystem(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void loadFile() throws Exception {
		File f = new File(getFileName());
		if(!f.exists()){
			throw new Exception(getFileName() + "配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		
		HSSFSheet sheet = workbook.getSheetAt(0);			
		int rows = sheet.getPhysicalNumberOfRows();
		
		baseModel = new HuntBaseModel();
		HSSFRow row2 = sheet.getRow(1);
		baseModel.setOnceCost(ReadFileTool.getLong(row2, 0, logger, 0L) * 1000L);
		baseModel.setTenCost(ReadFileTool.getLong(row2, 1, logger, 0L) * 1000L);
		baseModel.setInterverFreeTime(ReadFileTool.getLong(row2, 2, logger, 0L));
		baseModel.setKongNums(ReadFileTool.getIntArrByString(row2, 3, logger, ","));
		baseModel.setNeedLevels(ReadFileTool.getIntArrByString(row2, 4, logger, ","));
		baseModel.setMustGiveArticles(ReadFileTool.getIntArrByString(row2, 5, logger, ","));
		baseModel.setProb(ReadFileTool.getIntArrByString(row2, 6, logger, ","));
		
		sheet = workbook.getSheetAt(1);			//兽魂等级配置
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				ShouHunModel model = this.getShouHunModel(row);
				shouhunModels.put(model.getLevel(), model);
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		
		List<String> tempStr = new ArrayList<String>();
		
		sheet = workbook.getSheetAt(2);			//抽奖物品
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				HuntArticleModel model = this.getHuntArticleModel(row);
				if (model.getArticleCNNName() != null && !model.getArticleCNNName().isEmpty()) {
					articleMaps.put(model.getId(), model);
					if (ArticleManager.getInstance().getArticleByCNname(model.getArticleCNNName()) == null) {
						tempStr.add(model.getArticleCNNName());
					}
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		sheet = workbook.getSheetAt(3);			//描述
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				translate.put(ReadFileTool.getInt(row, 0, logger), ReadFileTool.getString(row, 1, logger));
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		if (tempStr.size() > 0 || articleMaps.get(单抽物品id) == null || articleMaps.get(十连抽物品id) == null) {
			StringBuffer sb = new StringBuffer();
			if (tempStr.size() > 0) {
				sb.append("物品不存在:" + tempStr);
			}
			if (articleMaps.get(单抽物品id) == null) {
				sb.append("单抽物品id不存在");
			}
			if (articleMaps.get(十连抽物品id) == null) {
				sb.append("十连抽物品id不存在");
			}
			
			throw new Exception("物品不存在:" + tempStr);
		}
		if (baseModel.getMustGiveArticles().length != baseModel.getProb().length) {
			throw new Exception("必产物品和概率个数不统一");
		}
	}
	
	private HuntArticleModel getHuntArticleModel(HSSFRow row) {
		int index = 0;
		HuntArticleModel model = new HuntArticleModel();
		model.setId(ReadFileTool.getInt(row, index++, logger));
		model.setArticleCNNName(ReadFileTool.getString(row, index++, logger));
		model.setColorType(ReadFileTool.getInt(row, index++, logger));
		model.setBind(ReadFileTool.getBoolean(row, index++, logger));
		model.setArticleNums(ReadFileTool.getInt(row, index++, logger));
		model.setProb4once(ReadFileTool.getInt(row, index++, logger));
		model.setProb4te(ReadFileTool.getInt(row, index++, logger));
		model.setLevel(ReadFileTool.getInt(row, index++, logger));
		model.setTemp(ReadFileTool.getInt(row, index++, logger));
		return model;
	}
	
	
	private ShouHunModel getShouHunModel(HSSFRow row) {
		int index = 0;
		ShouHunModel model = new ShouHunModel();
		model.setLevel(ReadFileTool.getInt(row, index++, logger));
		model.setExp4Up(ReadFileTool.getLong(row, index++, logger, 0L));
		model.setExp4Give(ReadFileTool.getLong(row, index++, logger, 0L));
		model.setAddAttrNums(ReadFileTool.getIntArrByString(row, index++, logger, ","));;
		model.setIcon1(ReadFileTool.getString(row, index++, logger));
		model.setIcon2(ReadFileTool.getString(row, index++, logger));
		return model;
	}
	
	public void initAllNeexExp() {
		for (ShouHunModel m : shouhunModels.values()) {
			allNeedExp += m.getExp4Up();
		}
		for (HuntArticleModel m : articleMaps.values()) {
			if (m.getProb4once() > 0) {
				onceTotalProb += m.getProb4once();
			}
			if (m.getProb4te() > 0) {
				tenTotalProb += m.getProb4te();
			}
		}
		for (int i=0; i<baseModel.getProb().length; i++) {
			tenMustProb += baseModel.getProb()[i];
		}
		logger.warn("[初始化总需要经验] [allNeedExp:" + allNeedExp + "] [onceTotalProb:" + onceTotalProb + "] [tenTotalProb:" + tenTotalProb + "] [tenMustProb:" + tenMustProb + "]");
	}
	
	/**
	 * 单抽  (有免费抽奖)
	 * @param player
	 */
	public ResponseMessage onceHunt(Player player, TAKE_SHOUHUN_LUCK_REQ req) {
		long now = System.currentTimeMillis();
		long cost = baseModel.getOnceCost();
//		Knapsack bag = player.getKnapsack_common();
//		if (bag.getEmptyNum() < 1) {
//			player.sendError("背包剩余空间不足");
//			return null;
//		}
		if (player.countEmptyCell4ShouhunKnap() < 1) {
			player.sendError(Translate.兽魂仓库已满);
			return null;
		}
		boolean free = false;
		HuntLifeEntity he = player.getHuntLifr();
		free = (now - he.getLastTaskFreeTime()) >= baseModel.getInterverFreeTime();
		if (!free) {
			if (player.getShopSilver() + player.getSilver() < cost) {
				player.sendError(Translate.银子不足);
				return null;
			}
			try {
				BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.猎命, "猎命单抽消耗");
//				player.sendError(String.format(Translate.消耗银子X, BillingCenter.得到带单位的银两(cost)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				player.sendError(Translate.银子不足);
				return null;
			} 
		} else {
			he.setLastTaskFreeTime(now);
			if (logger.isInfoEnabled()) {
				logger.info("[兽魂单抽] [免费] [" + player.getLogString() + "]");
			}
		}
		int ran = player.random.nextInt(onceTotalProb);
		int tempProb = 0;
		int resultArticleId = -1;
		Iterator<Integer> ito = articleMaps.keySet().iterator();
		while (ito.hasNext()) {
			int key = ito.next();
			HuntArticleModel ham = articleMaps.get(key);
			if (ham.getProb4once() > 0) {
				tempProb += ham.getProb4once();
				if (ran <= tempProb) {
					resultArticleId = ham.getId();
					break;
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("[测试单抽概率] [ran:" + ran + "] [" + player.getLogString() + "]");
		}
		if (resultArticleId > 0) {
			HuntArticleModel ham = articleMaps.get(resultArticleId);
			HuntArticleModel ham2 = articleMaps.get(单抽物品id);
			TempLuckModel tm = this.put2Bag(player, ham, free);				//免费抽取的物品绑定
			TempLuckModel tm2 = this.put2Bag(player, ham2, free);			//购买赠送物品   需要单独实现
			if (tm == null || tm2 == null) {
				return null;
			}
			TAKE_SHOUHUN_LUCK_RES resp = new TAKE_SHOUHUN_LUCK_RES(req.getSequnceNum(), req.getTakeType(), tm2.getArticleId(), tm2.getArticleName(), 
					tm2.getColorType(), tm2.getColorType(), new long[]{tm.getArticleId()}, new String[]{tm.getArticleName()}, new int[]{tm.getColorType()}, new int[]{tm.getNum()});
			return resp;
		} else {
			logger.warn("[猎命单抽] [失败] [没有随机到获得物品] [" + player.getLogString4Knap() + "] [ran:" + ran + "]");
		}
		return null;
	}
	
	public static int 单抽物品id = 10000;
	public static int 十连抽物品id = 10001;
	
	private TempLuckModel put2Bag(Player player, HuntArticleModel ham) {
		return put2Bag(player, ham, false);
	}
	
	private TempLuckModel put2Bag(Player player, HuntArticleModel ham, boolean bind) {
		Knapsack bag = player.getKnapsack_common();
		Article a = ArticleManager.getInstance().getArticleByCNname(ham.getArticleCNNName());
		try {
			boolean bd = bind ? true : ham.isBind();
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, bd, ArticleEntityManager.猎命抽奖, player, ham.getColorType(),
					ham.getArticleNums(), ham.getLevel(), true);
			if (ae instanceof HuntLifeArticleEntity) {
				player.putArticle2ShouhunKnap((HuntLifeArticleEntity) ae);
			} else {
				boolean result = bag.putToEmptyCell(ae.getId(), ham.getArticleNums(), "猎命单抽");
				if (!result) {
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] {ae}, new int[] { ham.getArticleNums() }, Translate.系统, Translate.兽魂抽奖邮件内容, 0L, 0L, 0L, "兽魂抽奖获得");
				}
			}
			if (ham.getTemp() == 1) {
				sendMsg2World(String.format(Translate.抽奖世界公告, player.getName(), ae.getArticleName()));
			}
			EnchantEntityManager.sendArticleStat(player, ae, "兽魂抽奖获得");
			logger.warn("[兽魂抽奖] [成功] [" + player.getLogString4Knap() + "] [是否免费:"+bind+"] [" + ham.getLogString() + "]");
			return new TempLuckModel(ae.getId(), ae.getArticleName(),ae.getColorType(), ham.getArticleNums());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("[兽魂抽奖] [失败] [创建物品异常] [" + player.getLogString() + "] [" + ham.getLogString() + "]", e);
		}
		return null;
	}
	/**
	 * 十连抽
	 * @param player
	 */
	public ResponseMessage tenHunt(Player player, TAKE_SHOUHUN_LUCK_REQ req) {
		long cost = baseModel.getTenCost();
		if (player.getShopSilver() + player.getSilver() < cost) {
			player.sendError(Translate.银子不足);
			return null;
		}
//		Knapsack bag = player.getKnapsack_common();
//		if (bag.getEmptyNum() < (十连抽次数+1)) {
//			player.sendError("背包剩余空间不足");
//			return null;
//		}
		if (player.countEmptyCell4ShouhunKnap() < 十连抽次数) {
			player.sendError(Translate.兽魂仓库已满);
			return null;
		}
		try {
			BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.猎命, "猎命十连抽消耗");
//			player.sendError(String.format(Translate.消耗银子X, BillingCenter.得到带单位的银两(cost)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			player.sendError(Translate.银子不足);
			return null;
		}
		List<Integer> resultList = new ArrayList<Integer>();
		int ran = 0;
		int tempRan = 0;
		int tempNum = 0;
		while (resultList.size() < (十连抽次数-1)) {
			tempRan = 0;
			ran = player.random.nextInt(tenTotalProb);
			Iterator<Integer> ito = articleMaps.keySet().iterator();
			while (ito.hasNext()) {
				int key = ito.next();
				HuntArticleModel ham = articleMaps.get(key);
				if (ham.getProb4once() > 0) {
					tempRan += ham.getProb4te();
					if (ran <= tempRan) {
						if (ham.getColorType() >= 3 && tempNum >= 紫色最多产出个数) {
							continue;
						}
						if (ham.getColorType() >= 3) {
							tempNum ++;
						}
						resultList.add(ham.getId());
						logger.warn("[猎命十连抽] [成功] [其他物品] [" + player.getLogString() + "] [ran:" + ran + "] [" + ham.getLogString() + "]");
						break;
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("[测试十连抽概率] [ran:" + ran + "] [" + player.getLogString() + "]");
			}
		}
		ran = player.random.nextInt(tenMustProb);
		tempRan = 0;
		for (int i=0; i<baseModel.getProb().length; i++) {
			tempRan += baseModel.getProb()[i];
			if (ran <= tempRan) {
				resultList.add(baseModel.getMustGiveArticles()[i]);
				HuntArticleModel ham = articleMaps.get(baseModel.getMustGiveArticles()[i]);
				logger.warn("[猎命十连抽] [成功] [必产物品] [" + player.getLogString4Knap() + "] [ran:" + ran + "] [" + ham.getLogString() + "]");
				break;
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("[猎命十连抽] [抽取出的id:" + resultList + "] [" + player.getLogString() + "]");
		}
		HuntArticleModel ham2 = articleMaps.get(十连抽物品id);
		TempLuckModel tm2 = this.put2Bag(player, ham2);
		long[] ids = new long[resultList.size()];
		String[] names = new String[resultList.size()];
		int[] colors = new int[resultList.size()];
		int[] nums  = new int[resultList.size()];
		for (int i=0; i<resultList.size(); i++) {
			if (resultList.get(i) > 0) {
				HuntArticleModel ham = articleMaps.get(resultList.get(i));
				TempLuckModel tm = this.put2Bag(player, ham);
				ids[i] = tm.getArticleId();
				names[i] = tm.getArticleName();
				colors[i] = tm.getColorType();
				nums[i] = tm.getNum();
			}
		}
		TAKE_SHOUHUN_LUCK_RES resp = new TAKE_SHOUHUN_LUCK_RES(req.getSequnceNum(), req.getTakeType(), tm2.getArticleId(), tm2.getArticleName(), 
				tm2.getColorType(), tm2.getColorType(), ids, names, colors, nums);
		return resp;
		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
