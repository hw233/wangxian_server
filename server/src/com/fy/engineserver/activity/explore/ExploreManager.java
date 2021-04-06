package com.fy.engineserver.activity.explore;


import static com.fy.engineserver.datasource.language.Translate.其他;
import static com.fy.engineserver.datasource.language.Translate.国内寻宝物品;
import static com.fy.engineserver.datasource.language.Translate.国外寻宝物品;
import static com.fy.engineserver.datasource.language.Translate.藏宝图;
import static com.fy.engineserver.datasource.language.Translate.铲子;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.articleExchange.ExchangeInterface;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.ExchangeArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.ExchangeArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.ExploreProps;
import com.fy.engineserver.datasource.article.data.props.ExploreResourceMap;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.newtask.ActivityTaskExp;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ServiceStartRecord;

public class ExploreManager {

	private final static byte 国内 = 0;
	private final static byte 国外 = 1;
	private static ExploreManager instance;
	public static Logger logger = LoggerFactory.getLogger(ExploreManager.class);
	private String fileName;

	public static int 寻宝兑换次数  = 10;
	public void init()throws Exception {
		
		instance = this;

		loadExcel();
		logger.info("ExploreManager init");
		ServiceStartRecord.startLog(this);
	}

	public static ExploreManager getInstance() {
		return instance;
	}

	private List<ExploreExpTemplate> expList = new ArrayList<ExploreExpTemplate>();
	private List<ExploreTemplate> exploreList = new ArrayList<ExploreTemplate>();

	public void loadExcel() throws Exception {

		this.exploreList.clear();

		// 0 fengxuezhidian xxx,bbb 100,20 100,200
		int 寻宝index = 0;
		int 寻宝地图 = 1;
		int 寻宝地图片段 = 2;
		int 寻宝地图x = 3;
		int 寻宝地图y = 4;
		int 范围 = 5;
		int 显示地图名 = 6;
		int 国内国外 = 7;
		
		int 物品名 = 0;
		int 任务id = 1;
		int 经验id = 2;
		
		File file = new File(fileName);
		HSSFWorkbook workbook = null;
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = null;
		sheet = workbook.getSheetAt(0);
		if (sheet == null)
			return;
		int rows = sheet.getPhysicalNumberOfRows();

		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				ExploreTemplate et = new ExploreTemplate();
				HSSFCell cell = row.getCell(寻宝index);
				int index = (int) cell.getNumericCellValue();
				et.setIndex(index);

				cell = row.getCell(寻宝地图);
				String exploreMapName = cell.getStringCellValue();
				et.setMapName(exploreMapName);

				cell = row.getCell(寻宝地图片段);
				String mapSegmentName = cell.getStringCellValue();

				cell = row.getCell(寻宝地图x);
				int mapX = (int) cell.getNumericCellValue();

				cell = row.getCell(寻宝地图y);
				int mapY = (int) cell.getNumericCellValue();

				et.setMapSegmentNames(mapSegmentName);
				Point point = new Point(mapX, mapY);
				et.setPoints(point);
				cell = row.getCell(范围);
				int range = (int) cell.getNumericCellValue();
				et.setRange(range);

				cell = row.getCell(显示地图名);
				String showMap = cell.getStringCellValue();
				et.setShowMap(showMap);
				
				cell = row.getCell(国内国外);
				int inOut = (int) cell.getNumericCellValue();
				et.setInOut(inOut);
				
				exploreList.add(et);
			}
		}
		
		
		sheet = workbook.getSheetAt(1);
		if (sheet == null){
			throw new RuntimeException("没有寻宝经验配置表");
		}
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				ExploreExpTemplate eet = new ExploreExpTemplate();
				HSSFCell cell = row.getCell(物品名);
				String articleName = (cell.getStringCellValue());
				eet.setArticleName(articleName);

				cell = row.getCell(任务id);
				long taskId = (long)cell.getNumericCellValue();
				eet.setTaskId(taskId);
				
				cell = row.getCell(经验id);
				int expId = (int)cell.getNumericCellValue();
				eet.setExpId(expId);
				
				expList.add(eet);
			}
		}
	}

	private Random random = new Random();

	/**
	 * 接到寻宝任务的时候随机得到一个寻宝实体
	 * 
	 * @param inOrOut
	 *            国内0国外1
	 * @param player
	 * @return
	 */
	public ExploreEntity randomGetExploreEntity(byte inOrOut, Player player) {

		if (player.getExploreEntity() != null) {
			logger.warn("[随机寻宝实体错误] [玩家正在进行] [" + player.getLogString()+ "]");
			return null;
		}
		ExploreEntity ee = null;
		List<ExploreTemplate> list = ExploreManager.getInstance().getExploreList();
		int max = list.size();
		ExploreTemplate et = null;
		boolean ok = false;
		int i = 1;
		while(!ok){
			i++;
			if (max > 1) {
				int index = random.nextInt(max);
				et = list.get(index);
			} else {
				et = list.get(0);
			}
			if(inOrOut == 国内){
				if(et.getInOut() == 0 || et.getInOut() == 1){
					ok = true;
				}
			}else{
				if(et.getInOut() == 0 || et.getInOut() == 2){
					ok = true;
				}
			}
			if(i >= 10000){
				ok = true;
				ExploreManager.logger.error("[获取寻宝配置错误] [死循环] ["+player.getLogString()+"]");
			}
		}

		if (et != null) {
			ee = new ExploreEntity();
			ee.setMapName(et.getMapName());
			ee.setMapSegmentName(et.getMapSegmentNames());
			ee.setPoint(et.getPoints());
			ee.setRange(et.getRange());
			ee.setShowMap(et.getShowMap());
			if (inOrOut == 国内) {
				ee.setCountry(player.getCountry());
			} else {
				boolean bln = true;
				while (bln) {
					int country = random.nextInt(4);
					if (country != player.getCountry() && country != 0) {
						ee.setCountry((byte) country);
						bln = false;
					}
					if (logger.isDebugEnabled()) {
						logger.debug("[随机国外寻宝] [" + country + "] ["+ player.getLogString() + "]");
					}
				}
			}

			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			
			ArticleManager am = ArticleManager.getInstance();
			Article a = ArticleManager.getInstance().getArticle(铲子);
			//删除任务给的铲子
			Knapsack k = player.getKnapsack_common();
			ArticleEntity remove1 = null;
			for(Cell c:k.getCells()){
				if(c != null && c.getEntityId() > 0){
					ArticleEntity ae = aem.getEntity(c.getEntityId());
					if(ae != null ){
						String article = ae.getArticleName();
						if(am.getArticle(article) instanceof ExploreProps){
							remove1 = k.removeByArticleId(ae.getId(),"寻宝", true);
							ArticleStatManager.addToArticleStat(player, null, remove1, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "寻宝删除", null);
							break;
						}
					}
				}
			}
			if(remove1 == null){
				Knapsack fangbao = player.getKnapsack_fangbao();
				for(Cell c:fangbao.getCells()){
					if(c != null && c.getEntityId() > 0){
						ArticleEntity ae = aem.getEntity(c.getEntityId());
						if(ae != null ){
							String article = ae.getArticleName();
							if(am.getArticle(article) instanceof ExploreProps){
								remove1 = fangbao.removeByArticleId(ae.getId(),"寻宝", true);
								ArticleStatManager.addToArticleStat(player, null, remove1, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "寻宝删除", null);
								break;
							}
						}
					}
				}
				if(remove1 != null){
					if (a != null && a instanceof ExploreProps) {
						try{
							ArticleEntity ae = aem.createEntity((ExploreProps) a, true, ArticleEntityManager.CREATE_REASON_explore,player, 1, 1, true);
							boolean chanzi = player.getKnapsack_fangbao().put(ae,"寻宝");
							ee.setPropsId(ae.getId());
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "寻宝获得", null);
							logger.warn("[随机寻宝实体成功] [放入铲子到防爆背包] [" + player.getLogString()+ "] [成功:"+chanzi+"]");
						}catch(Exception ex){
							logger.error("[随机寻宝实体异常] [放入铲子到防爆背包] [" + player.getLogString()+ "]",ex);
						}
					} else {
						logger.warn("[随机寻宝实体错误] [不是寻宝道具] [" + player.getLogString()+ "]");
						return null;
					}
				}else{
					logger.error("[随机寻宝实体错误] [删除铲子错误] [没有找到铲子] [" + player.getLogString() + "]");
					return null;
				}
			}else{
				if (a != null && a instanceof ExploreProps) {
					try{
						ArticleEntity ae = aem.createEntity((ExploreProps) a, true, ArticleEntityManager.CREATE_REASON_explore,player, 1, 1, true);
						player.putToKnapsacks(ae,"寻宝");
						ee.setPropsId(ae.getId());
						
						ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "寻宝获得", null);
						logger.warn("[随机寻宝实体成功] [放入铲子到普通背包] [" + player.getLogString()+ "]");
					}catch(Exception ex){
						logger.error("[随机寻宝实体异常] [放入铲子到普通背包] [" + player.getLogString()+ "]",ex);
					}
				} else {
					logger.warn("[随机寻宝实体错误] [不是寻宝道具] [" + player.getLogString()+ "]");
					return null;
				}
			}
			
			//删除任务给的藏宝图
			a = ArticleManager.getInstance().getArticle(藏宝图);
			k = player.getKnapsack_common();
			ArticleEntity remove2 = null;
			for(Cell c:k.getCells()){
				if(c != null && c.getEntityId() > 0){
					ArticleEntity ae = aem.getEntity(c.getEntityId());
					if(ae != null && ae instanceof ExploreResourceMapEntity){
						remove2 = k.removeByArticleId(ae.getId(),"寻宝", true);
						ArticleStatManager.addToArticleStat(player, null, remove2, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "寻宝删除", null);
						break;
					}
				}
			}
			if(remove2 == null){
				Knapsack fangbao = player.getKnapsack_fangbao();
				for(Cell c:fangbao.getCells()){
					if(c != null && c.getEntityId() > 0){
						ArticleEntity ae = aem.getEntity(c.getEntityId());
						if(ae != null && ae instanceof ExploreResourceMapEntity){
							remove2 = fangbao.removeByArticleId(ae.getId(),"寻宝", true);
							ArticleStatManager.addToArticleStat(player, null, remove2, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "寻宝删除", null);
							break;
						}
					}
				}
				if(remove2 != null){
					if (a != null && a instanceof ExploreResourceMap) {
						try{
							ExploreResourceMapEntity erm = (ExploreResourceMapEntity) aem.createEntity((ExploreResourceMap) a, true, ArticleEntityManager.CREATE_REASON_explore, player,1, 1, true);
							boolean cangbaotu = player.getKnapsack_fangbao().put(erm,"寻宝");
							ee.setResourceMapId(erm.getId());
							player.setExploreEntity(ee);
							ArticleStatManager.addToArticleStat(player, null, erm, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "寻宝获得", null);
							logger.warn("[随机寻宝实体成功] [放入藏宝图到防爆背包] [" + player.getLogString()+ "] [成功:"+cangbaotu+"]");
						}catch(Exception ex){
							logger.warn("[随机寻宝实体异常] [放入藏宝图到防爆背包] [" + player.getLogString()+ "]",ex);
						}
					} else {
						logger.warn("[随机寻宝实体错误] [不是藏宝图道具] [" + player.getLogString()+ "]");
						return null;
					}
				}else{
					logger.error("[随机寻宝实体错误] [删除藏宝图错误] [没有找到藏宝图] [" + player.getLogString() + "]");
					return null;
				}
			}else{
				if (a != null && a instanceof ExploreResourceMap) {
					try{
						ExploreResourceMapEntity erm = (ExploreResourceMapEntity) aem.createEntity((ExploreResourceMap) a, true, ArticleEntityManager.CREATE_REASON_explore, player,1, 1, true);
						player.putToKnapsacks(erm,"寻宝");
						ee.setResourceMapId(erm.getId());
						player.setExploreEntity(ee);
						ArticleStatManager.addToArticleStat(player, null, erm, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "寻宝获得", null);
						if (logger.isDebugEnabled()) {
							logger.debug("[随机寻宝实体success] [" + player.getLogString() + "]");
						}
						logger.warn("[随机寻宝实体成功] [放入藏宝图到普通背包] [" + player.getLogString()+ "]");
					}catch(Exception ex){
						logger.warn("[随机寻宝实体异常] [不是寻宝藏宝图] [" + player.getLogString()+ "]",ex);
					}
				} else {
					logger.warn("[随机寻宝实体错误] [不是寻宝藏宝图] [" + player.getLogString()+ "]");
					return null;
				}
				
			}
		} else {
			logger.warn("[随机寻宝实体错误] [et为null] [" + player.getLogString() + "]");
		}
		return ee;
	}

	public boolean giveUpExplore(Player player) {
		if (player.getExploreEntity() != null) {

			ExploreEntity ee = player.getExploreEntity();
			player.setExploreEntity(null);
			long propsId = ee.getPropsId();
			Object o = player.getKnapsack_common().removeByArticleId(propsId,"寻宝", true);
			if(o == null){
				o = player.getKnapsack_fangbao().removeByArticleId(propsId,"寻宝", true);
			}
			if(o == null){
				o = player.getKnapsacks_cangku().removeByArticleId(propsId,"寻宝", true);
			}
			long resourceId = ee.getResourceMapId();
			Object o2 = player.getKnapsack_common().removeByArticleId(resourceId,"寻宝", true);
			if(o2 == null){
				o2 = player.getKnapsack_fangbao().removeByArticleId(resourceId,"寻宝", true);
			}
			if(o2 == null){
				o2 = player.getKnapsacks_cangku().removeByArticleId(resourceId,"寻宝", true);
			}
			if (o != null && o2 != null) {
				
				ArticleStatManager.addToArticleStat(player, null, (ArticleEntity)o, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "放弃寻宝活动删除", null);
				ArticleStatManager.addToArticleStat(player, null, (ArticleEntity)o2, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "放弃寻宝活动删除", null);
				logger.warn("[放弃寻宝success] [" + player.getLogString()+ "] []");
				return true;
			} else {
				logger.warn("[放弃寻宝] [删除寻宝道具失败] [" + player.getLogString()+ "] [] ");
			}
		} else {
			logger.warn("[放弃寻宝错误] [玩家没有寻宝实体] [" + player.getLogString()+ "] [] ");
		}
		return false;
	}

	/**
	 * 查找那些物品可以兑换
	 * 
	 * @param player
	 * @return 没有找着 返回-1 有返回物品id
	 */
	public long queryExchangeArticle(Player player, int type) {

		// List<Long> list = new ArrayList<Long>();
		Knapsack knapsack = player.getKnapsack_common();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		Cell[] cells = knapsack.getCells();
		for (Cell cell : cells) {
			if(cell == null){
				continue;
			}
			ArticleEntity ae = aem.getEntity(cell.getEntityId());
			if (ae != null) {
				Article a = am.getArticle(ae.getArticleName());
				if (a != null) {
					if (type <= 3) {
						if (a instanceof ExchangeArticle) {
							ExchangeArticle ea = (ExchangeArticle) a;
							if (ea.getExchangearticleType() == type) {
								return ae.getId();
							}
						}
					} else {

						if (type == 4) {
							if (a.get物品一级分类().equals(其他)) {
								if (a.get物品二级分类().equals(国内寻宝物品)) {
									return ae.getId();
								}
							}
						} else if (type == 5) {
							if (a.get物品一级分类().equals(其他)) {
								if (a.get物品二级分类().equals(国外寻宝物品)) {
									return ae.getId();
								}
							}
						}

					}
				}
			}
		}
		return -1;

	}

	/**
	 * 查找那些物品可以交换
	 * 
	 * @param player
	 * @return 没有找着 返回-1 有返回物品id
	 */
	public List<Long> queryCanExchangeArticle(Player player) {

		List<Long> list = new ArrayList<Long>();
		Knapsack knapsack = player.getKnapsack_common();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		Cell[] cells = knapsack.getCells();
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			if(cell != null){
				ArticleEntity ae = aem.getEntity(cell.getEntityId());
				if (ae != null) {
					if (ae instanceof ExchangeInterface) {
						Article a = am.getArticle(ae.getArticleName());
						if(a instanceof ExchangeArticle){
							if(((ExchangeArticle)a).getExchangearticleType() > 3){
								continue;
							}
							list.add(ae.getId());
						}else{
							logger.warn("[查询可交换物品错误] [" + player.getLogString() + "] [不是可交换物品]");
						}
					}
				}
			}else{
				ArticleManager.logger.error("[{}] [第{}个cell为空]",new Object[]{player.getLogString(),i});
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("[查询可交换物品成功] [" + player.getLogString() + "] [物品个数:"+ list.size() + "]");
		}
		return list;
	}

	
	//今天可以兑换不可以
	public boolean todayCanConvert(Player player){
		
		long last = player.getLastExploreChangeTime();
		if(last > 0){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(last);
			int lastd = cal.get(Calendar.DAY_OF_YEAR);
			
			cal.setTimeInMillis(SystemTime.currentTimeMillis());
			int nowd = cal.get(Calendar.DAY_OF_YEAR);
			if(lastd == nowd){
				if(player.getExploreChangeNum() >= 寻宝兑换次数){
					ExploreManager.logger.warn("[寻宝兑换不可在兑换] ["+player.getLogString()+"] ["+player.getLastExploreChangeTime()+"] ["+player.getExploreChangeNum()+"]");
					return false;
				}else{
					return true;
				}
			}else{
				player.setExploreChangeNum(0);
				ExploreManager.logger.warn("[寻宝兑换不是同一天] ["+player.getLogString()+"] ["+player.getLastExploreChangeTime()+"] ["+SystemTime.currentTimeMillis()+"]");
			}
		}else{
			player.setExploreChangeNum(0);
		}
		return true;
	}
	
	public void convertUpdatePlayer(Player player){
		
		player.setLastExploreChangeTime(SystemTime.currentTimeMillis());
		player.setExploreChangeNum(player.getExploreChangeNum()+1);
		
		ExploreManager.logger.warn("[寻宝兑换设置时间] ["+player.getLogString()+"] ["+player.getLastExploreChangeTime()+"] ["+player.getExploreChangeNum()+"]");
		
	}
	
	// 左0 右1 上2 下3 国内全4 国外全5

	public void convertFromArticle(Player player, int type) {

		if (type > 5 || type < 0) {
			if (logger.isWarnEnabled()) {
				logger.warn("[物品兑换错误] [" + player.getLogString()+ "] [宝物类型不对:" + type + "]");
			}
		}
		
		if(player.getCurrentGame() == null){
			return ;
		}
		if(player.getCurrentGame().country != player.getCountry()){
			player.send_HINT_REQ(Translate.只能在本国的npc处兑换);
			return ;
		}
		
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();

		long canConvert = this.queryExchangeArticle(player, type);
		if (canConvert == -1) {
			if (type == 0) {
				player.sendError(Translate.text_兑换0);
			} else if (type == 1) {
				player.sendError(Translate.text_兑换1);
			} else if (type == 2) {
				player.sendError(Translate.text_兑换2);
			} else if (type == 3) {
				player.sendError(Translate.text_兑换3);
			} else if (type == 4) {
				player.sendError(Translate.text_兑换4);
			} else if (type == 5) {
				player.sendError(Translate.text_兑换5);
			}
			logger.warn("[物品兑换错误] [" + player.getLogString() + "] [玩家没有对应的物品] ["+type+"]");
			return;
		}

		ArticleEntity ae = aem.getEntity(canConvert);
		if (ae != null && ae instanceof ExchangeArticleEntity) {
			ExchangeArticleEntity eae = (ExchangeArticleEntity)ae;
			Article a = am.getArticle(ae.getArticleName());
				
			List<ExploreExpTemplate> list = this.getExpList();
			boolean bln = false;
			for(ExploreExpTemplate eet : list){
				if(eet.getArticleName().trim().equals(a.getName())){
					if(eae.getTaskId() == eet.getTaskId()){
						bln = true;
						ActivityTaskExp activityTaskExp = TaskManager.getInstance().activityPrizeMap.get(eet.getExpId());
						if(activityTaskExp != null){
							long exp = activityTaskExp.getExpPrize()[player.getLevel()-1];
							player.addExp(exp,ExperienceManager.ADDEXP_REASON_EXPLOREACTIVITY);
							CountryManager.getInstance().addExtraExp(player, exp);
						}else{
							ExploreManager.logger.warn("[寻宝兑换道具失败] [没有指定经验模板] [经验id:"+eet.getExpId()+"] ["+player.getLogString()+"] ["+ae.getArticleName()+"]");
						}
						break;
					}
				}
			}
			if(bln){
				ExploreManager.logger.warn("[寻宝兑换道具成功] ["+player.getLogString()+"] ["+ae.getArticleName()+"]");
				player.removeArticleEntityFromKnapsackByArticleId(canConvert,"寻宝兑换", true);
				logger.warn("[物品兑换成功] [" + player.getLogString() + "] ["+ a.getName() + "]");
			}else{
				ExploreManager.logger.warn("[寻宝兑换道具失败] ["+player.getLogString()+"] ["+ae.getArticleName()+"]");
			}

		} else {
			logger.warn("[物品兑换错误] [" + player.getLogString() + "] [物品不存在] ["+ canConvert + "]");
		}

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<ExploreTemplate> getExploreList() {
		return exploreList;
	}

	public void setExploreList(List<ExploreTemplate> exploreList) {
		this.exploreList = exploreList;
	}

	public List<ExploreExpTemplate> getExpList() {
		return expList;
	}

	public void setExpList(List<ExploreExpTemplate> expList) {
		this.expList = expList;
	}


}
