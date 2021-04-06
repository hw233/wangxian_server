package com.fy.engineserver.chuangong;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.chuangong.Option_Chuangong_Agree;
import com.fy.engineserver.menu.chuangong.Option_Chuangong_Disagree;
import com.fy.engineserver.message.AGREE_CHUANGONG_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.sprite.horse.dateUtil.DateFormat;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ServiceStartRecord;

public class ChuanGongManager {
	
	public static int CHUANGONGNUM = 3;
	public static String CHUANGONGARTICLE = Translate.传功石;
	public static int CHUANGONGTIME = 10 * 1000;
	
	public static String 传功指定区域  = "";
	public static int 传功等级  = 80;
	public static float 非师徒关系  = 1.2f;
	public static int 被传功等级  = 10;
	
	private static ChuanGongManager cgManager = null;
	//	public static Logger logger = Logger.getLogger(ChuanGongManager.class);
	public	static Logger logger = LoggerFactory.getLogger(ChuanGongManager.class);
	public Map<Long, Chuangong> map = new Hashtable<Long, Chuangong>();
	
	// 修法值 80 以后修法值
	public List<Integer> energyList  = new ArrayList<Integer>();
	// 1 - 79 获得的经验值
	public List<Integer> experienceList  = new ArrayList<Integer>();
	
	private String fileName = "";
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	private void load() throws Exception {
		long now = System.currentTimeMillis();
		 File file = new File(fileName);
		 HSSFWorkbook workbook = null;
		 try {
			 InputStream is = new FileInputStream(file);
			 POIFSFileSystem pss = new POIFSFileSystem(is);
			 workbook = new HSSFWorkbook(pss);
			 HSSFSheet sheet = null;
			 sheet = workbook.getSheetAt(0);
			 if (sheet == null) {
				 throw new RuntimeException();
			 }
			 int rows = sheet.getPhysicalNumberOfRows();
			 HSSFRow row = null;
			 HSSFCell cell = null;
			 for(int i= 1;i<rows;i++){
				 
				 row = sheet.getRow(i);
				 cell = row.getCell(1);
				 int energy = (int)cell.getNumericCellValue();
				 energyList.add(energy);
				 
			 }
			 
			 sheet = workbook.getSheetAt(1);
			 if (sheet == null) {
				 throw new RuntimeException();
			 }
			 rows = sheet.getPhysicalNumberOfRows();
			 row = null;
			 cell = null;
			 for(int i= 1;i<rows;i++){
				 
				 row = sheet.getRow(i);
				 cell = row.getCell(1);
				 int energy = (int)cell.getNumericCellValue();
				 experienceList.add(energy);
				 
			 }
			 
			 ChuanGongManager.logger.error("[初始化传功数值成功] []");
			 
		 }catch(Exception e){
			 ChuanGongManager.logger.error("[初始化传功数值错误] []",e);
		 }
		
	}
	
	public void init() throws Exception{
		
		
		this.load();
		cgManager = this;
		ServiceStartRecord.startLog(this);
		
	}
	
	public static ChuanGongManager getInstance(){
		return cgManager;
	}
	
	public String acceptChuangongArticle(Player active){
		String result ="";
		if(active.getLevel() < 传功等级){
			result = Translate.text_领取传功石级别;
			return result;
		}
		String nowTime = DateFormat.getSimpleDay(new Date());
		String lastTime = DateFormat.getSimpleDay(new Date(active.getLastAcceptChuangongArticle()));
		if(nowTime.equals(lastTime)){
			result = Translate.text_一天只能领取一次;
		}else{
			Article a = ArticleManager.getInstance().getArticle(CHUANGONGARTICLE);
			for(int i=0;i<CHUANGONGNUM;i++){
				try{
					ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_CHUANGONGSHI, active, 0, 1, true);
					if(entity == null){
						logger.error("[得到传功石错误] ["+active.getLogString()+"] [得到传功石为null]");
					}
					if(active.putToKnapsacks(entity,"传功")){
						
						ArticleStatManager.addToArticleStat(active, null, entity, ArticleStatManager.OPERATION_物品获得和消耗, 0,(byte)0, 1, "领取传功石", null);
						if(logger.isDebugEnabled()){
							logger.debug("[得到传功石] ["+active.getLogString()+"] []");
						}
					}else{
						logger.error("[得到传功石错误] ["+active.getLogString()+"] []");
						return Translate.text_包裹空间不足;
					}
				}catch(Exception ex){
					ChuanGongManager.logger.error("[领取传功石异常] ["+active.getLogString()+"]",ex);
				}
			}
			active.setLastAcceptChuangongArticle(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			active.send_HINT_REQ(Translate.text_得到传功石 );
		}
		return result;
	}
	
	/**
	 * 申请传功
	 * @param player
	 * @param playerA
	 * @return
	 */
	public String applyChuangong(Player active,Player passive){
		
		String result = this.chuangongCheck(active, passive);
		if(result.equals("")){
			//请求对方传功
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			
			mw.setTitle(Translate.text_传功申请);
			String des = Translate.translateString(Translate.text_xx请求与你传功, new String[][]{{Translate.PLAYER_NAME_1,active.getName()}});
			mw.setDescriptionInUUB(des);

			Option_Chuangong_Agree agree = new Option_Chuangong_Agree();
			agree.setText(Translate.text_62);
			agree.setColor(0xffffff);
			agree.setActive(active);

			Option_Chuangong_Disagree cancle = new Option_Chuangong_Disagree();
			cancle.setText(Translate.text_364);
			cancle.setColor(0xffffff);
			agree.setActive(active);

			mw.setOptions(new Option[] {agree ,cancle});
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			passive.addMessageToRightBag(res);
			
//			active.send_HINT_REQ("你已向"+passive.getName()+"发出传功申请");
			active.send_HINT_REQ(Translate.translateString(Translate.你已向xx发出传功申请,new String[][]{{Translate.STRING_1,passive.getName()}}));
			logger.warn("[请求传功成功] [active:"+(active != null ?active.getLogString():"null")+"] [passive:"+(passive != null ?passive.getLogString():"null")+"]");
		}else{
			logger.warn("[请求传功失败] [active:"+(active != null ?active.getLogString():"null")+"] [passive:"+(passive != null ?passive.getLogString():"null")+"] ["+result+"]");
		}
		return result;
	}
	
	
	private String chuangongCheck(Player active,Player passive){
		String result = "";
		if(active.getLevel() < 传功等级){
			result = Translate.text_申请传功级别条件;
		}else if(passive.getLevel() <= 被传功等级){
//			result = "对方等级小于"+被传功等级+"级，不能传功";
			result = Translate.translateString(Translate.对方等级小于xx级不能传功, new String[][]{{Translate.COUNT_1,被传功等级+""}});
		}else if (passive.getLevel() > 传功等级) {
			result = Translate.translateString(Translate.对方等级大于xx级不能传功, new String[][]{{Translate.COUNT_1,传功等级+""}});
		}else if(active.getLevel() <= passive.getLevel()){
			result = Translate.text_对方级别比你高不能传功;
		}else if(active.getCountry() != passive.getCountry()){
			result = Translate.text_传功必须是同一个国家的俩个人;
		}else if(active.isChuangonging()){
			result = Translate.text_自己正在传功;
		}else if(passive.isChuangonging()){
			result = Translate.text_对方正在传功;
		}else if(Player.根据罪恶值得到玩家的罪恶类型(active) > 0){
			result = Translate.text_自己红名不能进行传功;
		}else if(Player.根据罪恶值得到玩家的罪恶类型(passive) > 0){
			result = Translate.text_对方红名不能进行传功;
		}else{
			String nowTime = DateFormat.getSimpleDay(new Date());
			String lastTime = DateFormat.getSimpleDay(new Date(active.getLastChuangongTime()));
			if(nowTime.equals(lastTime)){
				if(active.getChuangongNum() >= CHUANGONGNUM ){
					result = Translate.text_每天只能传功三次你已经完成;
					return result;
				}
			}
			String lastAcceptTime = DateFormat.getSimpleDay(new Date(passive.getLastAcceptChuangongTime()));
			if(nowTime.equals(lastAcceptTime)){
				result = Translate.text_对方今天已经接受过传功;
			}
			int entityNum = active.getArticleEntityNum(CHUANGONGARTICLE);
			if(entityNum < 1){
				result = Translate.text_你没有传功石;
			}
		}
		
		if(!checkSpecialArea(active).equals("")){
			result= Translate.translateString(Translate.text_传功必须要在指定区域自己不在指定区域, new String[][]{{Translate.STRING_1,传功指定区域}});
		}else if(!checkSpecialArea(passive).equals("")){
			result= Translate.translateString(Translate.text_传功必须要在指定区域对方不在指定区域, new String[][]{{Translate.STRING_1,传功指定区域}});
		}
		return result;
	}
	
	private String checkSpecialArea(Player player){
		
//		if(player.getCurrentMapAreaName() != null && player.getCurrentMapAreaName().equals(传功指定区域)){
//			return "";
//		}else{
//			return "xx";
//		}
		return "";
	}
	
	
	private String chuangongCheckPassive(Player active,Player passive){
		String result = "";
		if(passive.getLevel() >= 传功等级){
//			result = "等级大于"+传功等级+"不能接受传功";
			result = Translate.translateString(Translate.等级大于XX不能接受传功, new String[][]{{Translate.COUNT_1,传功等级+""}});
		}else
		if(active.getLevel() <= passive.getLevel()){
			result = Translate.text_对方级别比你低不能接受传功;
		}else if(active.getCountry() != passive.getCountry()){
			result = Translate.text_传功必须是同一个国家的俩个人;
		}else if(active.isChuangonging()){
			result =  Translate.text_对方正在传功;
		}else if(passive.isChuangonging()){
			result = Translate.text_自己正在传功;
		}else if(Player.根据罪恶值得到玩家的罪恶类型(active) > 0){
			result = Translate.text_对方红名不能接受传功;
		}else if(Player.根据罪恶值得到玩家的罪恶类型(passive) > 0){
			result = Translate.text_自己红名不能接受传功;
		}else{
			String nowTime = DateFormat.getSimpleDay(new Date());
			String lastTime = DateFormat.getSimpleDay(new Date(active.getLastChuangongTime()));
			if(nowTime.equals(lastTime)){
				if(active.getChuangongNum() >= CHUANGONGNUM ){
					result = Translate.text_每天只能传功三次对方已经完成;
					return result;
				}
			}
			String lastAcceptTime = DateFormat.getSimpleDay(new Date(passive.getLastAcceptChuangongTime()));
			if(nowTime.equals(lastAcceptTime)){
				result = Translate.text_自己今天已经接受过传功;
			}
			int entityNum = active.getArticleEntityNum(CHUANGONGARTICLE);
			if(entityNum < 1){
				result = Translate.text_对方没有传功石;
			}
		}
		
		if(!checkSpecialArea(active).equals("")){
			result= Translate.translateString(Translate.text_传功必须要在指定区域对方不在指定区域, new String[][]{{Translate.STRING_1,传功指定区域}});
		}else if(!checkSpecialArea(passive).equals("")){
			result= Translate.translateString(Translate.text_传功必须要在指定区域自己不在指定区域, new String[][]{{Translate.STRING_1,传功指定区域}});
		}
		return result;
	}
	
	/**
	 * 对方同意传功
	 * @param p1
	 * @param p2
	 */
	public synchronized String agreeChuangong(Player passive,Player active){
		String result = chuangongCheckPassive(active,passive);
		if(result.equals("")){
			
			Chuangong cg = new Chuangong(passive.getId(), active, passive);
			boolean b = active.getTimerTaskAgent().createSpecialTimerTaks(new ChuanGongProgressBar(active,cg.getId()), ChuanGongManager.CHUANGONGTIME, TimerTask.type_传功, active, passive);
			if (b) {
				if(active.isIsUpOrDown()){
					active.downFromHorse();
				}
				if(passive.isIsUpOrDown()){
					passive.downFromHorse();
				}
				map.put(cg.getId(), cg);
				passive.setChuangonging(true);
				active.setChuangonging(true);
				passive.setChuangongState(true);
				active.setChuangongState(true);
				AGREE_CHUANGONG_RES res = new AGREE_CHUANGONG_RES(GameMessageFactory.nextSequnceNum(), cg.getId(), CHUANGONGTIME ,true);
				active.addMessageToRightBag(res);
				passive.addMessageToRightBag(res);
				ArticleEntity ae = active.getArticleEntity(CHUANGONGARTICLE);
				if(ae != null){
					active.removeFromKnapsacks(ae, "传功", true);
					ArticleStatManager.addToArticleStat(active, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "传功删除", null);
				}
				logger.warn("[同意传功成功] [active:"+(active != null ?active.getLogString():"null")+"] [passive:"+(passive != null ?passive.getLogString():"null")+"]");
			} else {
				
				logger.warn("[同意传功失败] [创建传功读条失败] [active:"+(active != null ?active.getLogString():"null")+"] [passive:"+(passive != null ?passive.getLogString():"null")+"] ["+result+"]");
			}
		}else{
			logger.warn("[同意传功失败] [active:"+(active != null ?active.getLogString():"null")+"] [passive:"+(passive != null ?passive.getLogString():"null")+"] ["+result+"]");
		}
		return result;
	}
	
	/**
	 * 对方不同意传功
	 * @param p1
	 * @param p2
	 */
	public void disAgreeChuangong(Player passive,Player active){
		if(passive != null && active != null){
			passive.send_HINT_REQ(Translate.translateString(Translate.text_你拒绝了xx的传功请求, new String[][]{{Translate.PLAYER_NAME_1,active.getName()}}));
			active.send_HINT_REQ(Translate.translateString(Translate.text_xx拒绝了你的传功请求, new String[][]{{Translate.PLAYER_NAME_1,passive.getName()}}));
			logger.warn("[拒绝传功成功] [active:"+(active != null ?active.getLogString():"null")+"] [passive:"+(passive != null ?passive.getLogString():"null")+"]");
		}else{
			logger.info("[拒绝传功] [失败] [passive == null || active == null]");
		}
	}

	public List<Integer> getEnergyList() {
		return energyList;
	}

	public void setEnergyList(List<Integer> energyList) {
		this.energyList = energyList;
	}

	public List<Integer> getExperienceList() {
		return experienceList;
	}

	public void setExperienceList(List<Integer> experienceList) {
		this.experienceList = experienceList;
	}
	
}
