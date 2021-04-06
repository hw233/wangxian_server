package com.fy.engineserver.compose;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.compose.model.ChangeColorCompose;
import com.fy.engineserver.compose.model.Compose4Other;
import com.fy.engineserver.compose.model.TempArtilce;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.compose.Option_confirmCompose;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;

public class ComposeManager {
	public static Logger logger = LoggerFactory.getLogger(ComposeManager.class);
	public static ComposeManager instance;
	/** 配置文件路径 */
	private String fileName;
	/** 颜色强化(key=article_needArticleColor) */
	public Map<String, ChangeColorCompose> colorComposeMap;
	/** 合成新物品 */
	public Set<Compose4Other> compose4OtherList;
	/** 合成改变颜色的物品--存起来方便设置类型告诉客户端 */
	public List<TempArtilce> compose4ColorArticle ;
	/** 门票合成规定有左右限制区分 */
	public List<TempArtilce> compose4OtherLeft;
	/** 门票合成规定有左右限制区分 */
	public List<TempArtilce> compose4OtherRight;
	/**  */
	public long[] colorProps ;
	public long[] ticketProps ;
	
	public static void main(String[] args) throws Exception {
		ComposeManager cm = new ComposeManager();
		cm.fileName = "E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//compose//articleCompose.xls";
		cm.init();
	}
	
	/**
	 * 物品合成--
	 * @param p
	 * @param composeType
	 * @param costType
	 * @param materiaIds
	 * @return
	 * @throws Exception
	 */
	public List<ArticleEntity> composeArticle(Player p, int composeType, int costType, long[] materiaIds, boolean isPop) throws Exception {
		synchronized (p) {
			String validityResult = commonValidity(p,composeType, costType, materiaIds);
			if(validityResult != null) {
				p.sendError(validityResult);
				if(logger.isInfoEnabled()) {
					logger.info("[合成失败] [失败原因:" + validityResult + "][" + p.getLogString() + "]");
				}
				return null;
			}
			List<ArticleEntity> resultList = null;
			if(composeType == ComposeConstant.compost4Color) {
				resultList = compose4Color(p, composeType, costType, materiaIds, isPop);
			} else if (composeType == ComposeConstant.compost4Article) {
				resultList = compose4OthrerArticle(p, composeType, costType, materiaIds, isPop);
			}
			if(resultList != null && resultList.size() > 0) {
				this.put2Bag(p, resultList);
//				String str = Translate.translateString(Translate.合成成功了N次, new String[][] {{Translate.STRING_1, String.valueOf(resultList.size())}});
//				p.sendError(str);
			}
			return resultList;
		}
	}
	/**
	 * 合成新的物品
	 * @param p
	 * @param composeType
	 * @param costType
	 * @param materiaIds
	 * @return
	 */
	public List<ArticleEntity> compose4OthrerArticle(Player p, int composeType, int costType, long[] materiaIds, boolean isPop) {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		List<ArticleEntity> tempArticle = new ArrayList<ArticleEntity>();
		int colorType = -1;
		boolean tempBind = false;
		for(int i=0; i<materiaIds.length; i++) {
			ArticleEntity strongMaterialEntity = aem.getEntity(materiaIds[i]);
			Knapsack bag = p.getKnapsack_common();
			if(strongMaterialEntity == null) {
				p.sendError(Translate.服务器数据异常);
				return null;
			}
			if(colorType == -1) {
				colorType = strongMaterialEntity.getColorType();
			} else if(colorType != strongMaterialEntity.getColorType()) {
				p.sendError(Translate.放入材料不对);
				return null;
			}
			if(bag == null || bag.indexOf(strongMaterialEntity.getArticleName(), strongMaterialEntity.getColorType()) < 0) {
				p.sendError(Translate.背包材料数量不足);
				return null;
			}
			if(isPop && strongMaterialEntity.isBinded() && tempBind) {
				popBindNotice(p, composeType, costType, materiaIds);
				return null;
			}
			if(!strongMaterialEntity.isBinded()) {
				tempBind = true;
			}
			tempArticle.add(strongMaterialEntity);
		}
		Compose4Other coo = null;
		int composeNum = 0;				//可合成的数量
		for(Compose4Other co : compose4OtherList) {
			if(co.getNeedArticleNum() > tempArticle.size()) {
				continue;
			}
			for(int i=0; i<co.getNeedArticles().size(); i++) {
				int artNum = 0;
				for(int j=0; j<tempArticle.size(); j++) {
					if(tempArticle.get(j).getArticleName().equals(co.getNeedArticles().get(i).getArticleName()) && tempArticle.get(j).getColorType() == co.getNeedArticles().get(i).getArticleColorNum()) {
						artNum++;
					}
				}
				if(artNum <= 0) {										//不存在合成所需物品
					break;
				}
				if(i == 0 || artNum < composeNum) {
					composeNum = artNum;
				}
			}
			if(composeNum > 0) {						//得出最少可合成数量如果大于1则材料够
				coo = co;
				break;
			}
		}
		if(coo == null) {
			p.sendError(Translate.放入材料不对);
			return null;
		}
		if(coo.getCostNum() > 0) {
			BillingCenter bc = BillingCenter.getInstance();
			for(int i=0;i<composeNum; i++) {
				try{
					bc.playerExpense(p, coo.getCostNum(), coo.getCostType(), ExpenseReasonType.物品合成, "新合成");
				}catch(Exception e) {
					composeNum = i;					//玩家身上银子只够合成的数量
					logger.warn("[物品合成] [失败]", e);
					if(composeNum <= 0) {
						return null;
					}
				}
			}
		}
		boolean resultBind = coo.isBind();
		int createCount = 0;
		for(int i=0; i<composeNum; i++) {
			for(int j=0; j<coo.getNeedArticles().size(); j++) {
				try{
					ArticleEntity aee1 = p.removeArticleByNameColorAndBind(coo.getNeedArticles().get(j).getArticleName(), coo.getNeedArticles().get(j).getArticleColorNum(), BindType.BOTH, "新和成-", true);
					if (aee1 == null) {
						String description = Translate.删除物品不成功;
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
						p.addMessageToRightBag(hreq);
						logger.error("[新合成] [合成失败2，删除的物品不存在][" + p.getLogString() + "] [物品名:" + coo.getNeedArticles().get(j).getArticleName() + "] [颜色:" + coo.getNeedArticles().get(j).getArticleColorNum() + "]");
						return null;
					} else {
						ArticleStatManager.addToArticleStat(p, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "合成恶魔城堡门票删除", null);
						if(aee1.isBinded()) {
							resultBind = true;
						}
					}
				} catch(Exception e) {
					logger.error("[新合成] [合成失败，出异常][" + p.getLogString() + "] [物品名:" + coo.getNeedArticles().get(j).getArticleName() + "] [颜色:" + coo.getNeedArticles().get(j).getArticleColorNum() + "]", e);
					return null;
				}
			}
		}
		for(int i=0; i<composeNum; i++) {
			int probbly = p.random.nextInt(ComposeConstant.THOUSANDS);
			if(coo.getSuccessRate() >= probbly) {				//合成成功
				createCount++;
			} else {
				if(logger.isErrorEnabled()) {
					logger.info("[新合成] [合成：" + coo.getArticleName() + "&&颜色：" + coo.getArticleColor() + "失败] [" + p.getLogString() + "]");
				}
			}
		}
		if(createCount > 0) {
			List<ArticleEntity> resultList = new ArrayList<ArticleEntity>();
			ArticleManager am = ArticleManager.getInstance();
			Article a = am.getArticle(coo.getArticleName());
			ArticleEntity aee = null;
			try {
				aee = aem.createEntity(a, resultBind, ArticleEntityManager.新合成, p, coo.getArticleColor(), createCount, true);
				ArticleStatManager.addToArticleStat(p, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, createCount, "合成恶魔城堡门票创建", null);
				for(int i=0; i<createCount; i++) {
					if(aee != null) {
						resultList.add(aee);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("[新合成,出异常] [" + p.getLogString() + "]", e);
			}
			return resultList;
		}
		if(logger.isInfoEnabled()) {
			logger.info("[合成失败] [" + p.getLogString() + "]");
		}
		return null;
	}
	
	public void popBindNotice(Player player, int composeType, int costType, long[] materiaIds) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		mw.setDescriptionInUUB(Translate.合成后绑定);
		Option_confirmCompose option1 = new Option_confirmCompose();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setComposeType(composeType);
		option1.setCostType(costType);
		option1.setMateriaIds(materiaIds);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
	}
	
	/**
	 * 颜色合成
	 * @param p
	 * @param composeType
	 * @param costType
	 * @param materiaIds
	 * @return
	 * @throws Exception
	 */
	public List<ArticleEntity> compose4Color(Player p, int composeType, int costType, long[] materiaIds, boolean isPop) throws Exception {
		if(logger.isInfoEnabled()) {
			logger.info("[颜色合成] [" + p.getLogString() + "] [" + materiaIds.length + "] [" + materiaIds[0] + "]");
		}
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		List<ArticleEntity> tempArticle = new ArrayList<ArticleEntity>();
		int colorType = -1;
		for(int i=0; i<materiaIds.length; i++) {
			ArticleEntity strongMaterialEntity = aem.getEntity(materiaIds[i]);
			Knapsack bag = p.getKnapsack_common();
			if(strongMaterialEntity == null) {
				p.sendError(Translate.服务器数据异常);
				return null;
			}
			if(colorType == -1) {
				colorType = strongMaterialEntity.getColorType();
			} else if(colorType != strongMaterialEntity.getColorType()) {
				p.sendError(Translate.放入材料不对);
				return null;
			}
			if(bag == null || bag.indexOf(strongMaterialEntity.getArticleName(), strongMaterialEntity.getColorType()) < 0) {
				p.sendError(Translate.背包材料数量不足);
				return null;
			}
			tempArticle.add(strongMaterialEntity);
		}
		if(logger.isInfoEnabled()) {
			logger.info("[颜色合成22] [" + p.getLogString() + "] [" + tempArticle.size() + "] [" + tempArticle.get(0) + "]");
		}
		ArticleEntity temp = tempArticle.get(0);
		ChangeColorCompose cc = colorComposeMap.get(temp.getArticleName() + "_" + temp.getColorType());
		if(cc == null) {
			p.sendError(Translate.不可合成);
			return null;
		}
		int count = p.getArticleEntityNum(materiaIds[0]);
		if(count < tempArticle.size()) {
			p.sendError(Translate.背包材料数量不足);
			return null;
		}
		if(tempArticle.size() < cc.getNeedArticleNum()) {
			p.sendError(Translate.放入材料数量不足);
			return null;
		}
		int composeNum = tempArticle.size() / cc.getNeedArticleNum();			//最大可合成数量
		if(logger.isInfoEnabled()) {
			logger.info("[颜色合成22] [" + p.getLogString() + "] [" + composeNum + "] [" + cc.getNeedArticleNum() + "]");
		}
		if(cc.getCostNum() > 0) {
			BillingCenter bc = BillingCenter.getInstance();
			for(int i=0;i<composeNum; i++) {
				try{
					bc.playerExpense(p, cc.getCostNum(), cc.getCostType(), ExpenseReasonType.物品合成, "新合成");
				}catch(NoEnoughMoneyException e) {
					composeNum = i;					//玩家身上银子只够合成的数量
					logger.warn("[物品合成] [失败]", e);
					if(composeNum <= 0) {
						return null;
					}
					break;
				} catch(Exception e) {
					composeNum = i;					//玩家身上银子只够合成的数量
					logger.warn("[物品合成] [失败]", e);
					if(composeNum <= 0) {
						return null;
					}
					break;
				}
			}
		}
		int costNum = composeNum * cc.getNeedArticleNum();			//总共消耗的物品数量
		if(logger.isInfoEnabled()) {
			logger.info("[颜色合成33] [" + p.getLogString() + "] [" + costNum + "] [" + cc.getNeedArticleNum() + "][" + composeNum + "]");
		}
		int createCount = 0;
		for(int i=0; i<composeNum; i++) {
			int probbly = p.random.nextInt(ComposeConstant.THOUSANDS);
			if(cc.getSuccessRate() >= probbly) {				//合成成功
				createCount++;
			} else {
				if(logger.isInfoEnabled()) {
					logger.info("[新合成] [合成" + temp.getArticleName() + "&&--" + temp.getColorType() + "失败] [" + p.getLogString() + "]");
				}
			}
		}
		boolean resultBind = cc.isBind();
//		String str = Translate.translateString(Translate.合成失败了N次, new String[][] {{Translate.STRING_1, String.valueOf((composeNum-createCount))}});
//		p.sendError(str);
		ArticleEntity aee1 = null;
		for(int i=0; i<costNum; i++) {
			try{
				aee1 = p.removeArticleEntityFromKnapsackByArticleId(materiaIds[i], "合成恶魔城堡门票", true);
				if (aee1 == null) {
					String description = Translate.删除物品不成功;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					p.addMessageToRightBag(hreq);
					logger.error("[新合成] [合成失败，删除的物品不存在][" + p.getLogString() + "] [已删除数量:" + i + "] [总共需要删除数量:" + costNum + "]");
					return null;
				} else {
					ArticleStatManager.addToArticleStat(p, null, aee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "合成恶魔城堡门票删除", null);
					if(aee1.isBinded()) {
						resultBind = true;
					}
				}
			} catch(Exception e) {
				logger.error("[新合成] [合成失败，出异常][" + p.getLogString() + "] [已删除数量:" + i + "] [总共需要删除数量:" + costNum + "]", e);
				return null;
			}
		}
		if(createCount > 0) {
			List<ArticleEntity> resultList = new ArrayList<ArticleEntity>();
			ArticleManager am = ArticleManager.getInstance();
			Article a = am.getArticle(temp.getArticleName());
			ArticleEntity aee = null;
			try {
				aee = aem.createEntity(a, resultBind, ArticleEntityManager.新合成, p, temp.getColorType()+1, createCount, true);
				ArticleStatManager.addToArticleStat(p, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, createCount, "合成恶魔城堡门票", null);
				for(int i=0; i<createCount; i++) {
					resultList.add(aee);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("[新合成,出异常] [" + p.getLogString() + "]", e);
			}
			String str = Translate.translateString(Translate.合成成功了N次, new String[][] {{Translate.STRING_1, String.valueOf(costNum)}, {Translate.STRING_2, aee1.getArticleName()}, {Translate.STRING_3, createCount+""}, {Translate.STRING_4, aee.getArticleName()}});
			p.sendError(str);
			return resultList;
		}
		if(logger.isInfoEnabled()) {
			logger.info("[合成失败] [" + p.getLogString() + "]");
		}
		String str = Translate.translateString(Translate.合成失败了N次, new String[][] {{Translate.STRING_1, String.valueOf((costNum))}, {Translate.STRING_2, aee1.getArticleName()}});
		p.sendError(str);
		return null;
	}
	
	/**
	 * 将合成成功的物品放入玩家背包s
	 * @param p
	 * @param atList
	 */
	public void put2Bag(Player p, List<ArticleEntity> atList) {
		for(int i=0; i<atList.size(); i++) {
			if(!p.putToKnapsacks(atList.get(i), "新合成")) {
				p.sendError(Translate.背包空间不足);
				if(logger.isInfoEnabled()) {
					logger.info("[新合成] [背包空间不足发邮件] [" + p.getLogString() + "]");
				}
				try{
					MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[] { atList.get(i) }, new int[] { Integer.valueOf(1) }, Translate.系统, Translate.合成获得物品, 0L, 0L, 0L, "合成获得物品");
				} catch(Exception e) {
					logger.error("[新合成][合成物品没有放入背包且发邮件出异常] [" + p.getLogString() + "][物品名:" + atList.get(i).getArticleName() + "&&颜色:" + atList.get(i).getColorType() + "&&是否绑定" + atList.get(i).isBinded() +"]");
				}
			}
		}
	}
	
	/**
	 * 通用合法性检查
	 * @return
	 * @throws Exception 
	 */
	public String commonValidity(Player p, int composeType, int costType, long[] materiaIds) throws Exception {
		if(materiaIds == null || materiaIds.length <= 0) {
			return Translate.请放入合成所需材料;
		}
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if(aem == null) {
			throw new Exception("[合成物品出错] [ArticleEntityManager未初始化！！]");
		}
		Knapsack bag = p.getKnapsack_common();
		if(bag == null){
			logger.error("取得的玩家背包是null {}", p.getName());
			return "背包为空";
		}
		for(int i=0; i<materiaIds.length; i++) {
			ArticleEntity strongMaterialEntity = aem.getEntity(materiaIds[i]);
			
			if(strongMaterialEntity == null) {
				return Translate.合成所需材料不存在;
			} else {
				int nn = bag.countArticle(strongMaterialEntity.getArticleName());
				if(nn <= 0) {
					return Translate.放入材料数量不足;
				}
			}
		}
		if(composeType != ComposeConstant.compost4Color && composeType != ComposeConstant.compost4Article) {
			return Translate.合成类型不对;
		}
		if(costType != ComposeConstant.costBindSiliver && costType != ComposeConstant.costSiliver) {
			return Translate.合成花费类型不对;
		}
		Knapsack knapsack = p.getKnapsack_common();
		if(knapsack.getEmptyNum() <= 0) {
			return Translate.背包空间不足;
		}
		return null;
	}
	
	public void init() throws Exception {
		
		instance = this;
		loadComposeFile();
		ServiceStartRecord.startLog(this);
	}
	
	private void loadComposeFile() throws Exception {
		File f = new File(fileName);
		if(!f.exists()){
			throw new Exception(fileName + "配表不存在! " + f.getAbsolutePath());
		}
		
		colorComposeMap = new LinkedHashMap<String, ChangeColorCompose>();
		compose4OtherList = new LinkedHashSet<Compose4Other>();
		compose4ColorArticle = new ArrayList<TempArtilce>();
		compose4OtherLeft = new ArrayList<TempArtilce>();
		compose4OtherRight = new ArrayList<TempArtilce>();
		
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);				//只改变颜色合成配置	
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				ChangeColorCompose temp = getChangeColorCompose(row);
				colorComposeMap.put(temp.getArticleName() + "_" + temp.getNeedArticleColor(), temp);
				TempArtilce ta = new TempArtilce(temp.getArticleName(), temp.getNeedArticleColor());
				if(!compose4ColorArticle.contains(ta)) {
					compose4ColorArticle.add(ta);
				}
//				System.out.println(temp);
			}catch(Exception e) {
				throw new Exception("composeArticle.xls,sheet0第" + (i+1) + "行异常！！", e);
			}
		}
		colorProps = new long[colorComposeMap.size()];
		int ii = 0;
		for(ChangeColorCompose cc : colorComposeMap.values()) {
			Article a = ArticleManager.getInstance().getArticle(cc.getArticleName());
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.恶魔广场, null, (cc.getNeedArticleColor()+1));
			colorProps[ii] = ae.getId();
			if(logger.isDebugEnabled()) {
				logger.debug("[生产物品] [ " + ae.getArticleName() + "] [" + ae.getId() + "][" + ae.getColorType() + "]");
			}
			ii++;
		}
		
		sheet = workbook.getSheetAt(1);						//特定物品合成新物品
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				Compose4Other temp = getCompose4Other(row);
				compose4OtherList.add(temp);
				if(temp.getNeedArticleNum() == 2) {
					compose4OtherLeft.add(temp.getNeedArticles().get(0));
					compose4OtherRight.add(temp.getNeedArticles().get(1));
				}
//				System.out.println(temp);
			}catch(Exception e) {
				throw new Exception("composeArticle.xls,sheet1第" + (i+1) + "行异常！！", e);
			}
		}
		ticketProps = new long[compose4OtherList.size()];
		int iii = 0;
		for(Compose4Other cc : compose4OtherList) {
			Article a = ArticleManager.getInstance().getArticle(cc.getArticleName());
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.恶魔广场, null, cc.getArticleColor());
			ticketProps[iii] = ae.getId();
			iii++;
		}
	}
	
	private ChangeColorCompose getChangeColorCompose(HSSFRow row) {
		ChangeColorCompose cc = new ChangeColorCompose();
		
		String articleName;
		int articleNum;
		boolean isBind;
		int maxColorNum;
		int successRate;
		int needArticleColor;
		int needArticleNum;
		
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		articleName = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		articleNum = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		isBind = cell.getBooleanCellValue();
		cell = row.getCell(rowNum++);
		int costType = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		int costNum = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		maxColorNum = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		successRate = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		needArticleColor = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		needArticleNum = (int) cell.getNumericCellValue();
		
		cc.setArticleName(articleName);
		cc.setTargetNum(articleNum);
		cc.setBind(isBind);
		cc.setCostType(costType);
		cc.setCostNum(costNum);
		cc.setMaxValueNum(maxColorNum);
		cc.setNeedArticleColor(needArticleColor);
		cc.setNeedArticleNum(needArticleNum);
		cc.setSuccessRate(successRate);
		
		return cc;
	}
	
	private Compose4Other getCompose4Other(HSSFRow row) {
		Compose4Other co = new Compose4Other();
		
		String articleName;
		int articleColor;
		int  articleNum;
		boolean isBind;
		int successRate;
		int needArticleNum;
		
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		articleName = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		articleColor = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		articleNum = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		isBind = cell.getBooleanCellValue();
		cell = row.getCell(rowNum++);
		int costType = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		int costNum = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		successRate = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		needArticleNum = (int) cell.getNumericCellValue();
		List<TempArtilce> needArtilceList = new ArrayList<TempArtilce>();
		for(int i=0; i<needArticleNum; i++) {
			cell = row.getCell(rowNum++);
			String needArticleName = cell.getStringCellValue();
			cell = row.getCell(rowNum++);
			int needArticleColor = (int) cell.getNumericCellValue();
			TempArtilce ta = new TempArtilce(needArticleName, needArticleColor);
			needArtilceList.add(ta);
		}
		
		co.setArticleName(articleName);
		co.setArticleColor(articleColor);
		co.setArticleNum(articleNum);
		co.setBind(isBind);
		co.setCostType(costType);
		co.setCostNum(costNum);
		co.setNeedArticleNum(needArticleNum);
		co.setNeedArticles(needArtilceList);
		co.setSuccessRate(successRate);
		return co;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
