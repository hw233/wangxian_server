package com.fy.engineserver.activity.shop;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;

/**
 * 商店活动管理器
 * 
 */
public class ShopActivityManager {

//	private List<ShopActivity> shopActivities = new ArrayList<ShopActivity>();

	private static ShopActivityManager instance;
	
	public File dataFile;
	private String fileName;
	
	public static int 商店买送活动标签  = 0;
	public static int 物品使用赠送活动 = 1;

	public enum RepayType {
		指定数量(0), // 配置几个送几个
		匹配购买数量(1);// 买几个送几个
		
		public int type;
		
		RepayType(int type) {
			this.type = type;
		}
	}

	public void init() throws Exception {
		
		long now = System.currentTimeMillis();
		loadActivityFile();
		instance = this;
		ServiceStartRecord.startLog(this);
	}

	public static ShopActivityManager getInstance() {
		return instance;
	}
	
	public static void main(String[] args) throws Exception {
		ShopActivityManager s = new ShopActivityManager();
		s.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//activity//shopActivities.xls");
		s.loadActivityFile();
	}
	
	/**
	 * 读配表
	 * @throws Exception 
	 */
	private void loadActivityFile() throws Exception {
		File f = new File(fileName);
		if(!f.exists()){
			throw new Exception("shopActivities.xls配表不存在! " + f.getAbsolutePath());
		}
		f = new File(ConfigServiceManager.getInstance().getFilePath(f));
		if (!f.exists()) {
			throw new Exception("配置文件不存在");
		}
		
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(商店买送活动标签);			
		int rows = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> tempShopList = new ArrayList<BaseActivityInstance>();
		List<BaseActivityInstance> tempUseList = new ArrayList<BaseActivityInstance>();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			ShopActivityOfBuyAndGive temp = initShopActivityOfBuyAndGive(row);
			TransitRobberyManager.logger.info(temp.toString());
//			shopActivities.add(temp);
			tempShopList.add(temp);
		}
		
		long tempId = 3000;			//用来区分活动使用数量存储
		sheet = workbook.getSheetAt(物品使用赠送活动);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			ArticleActivityOfUseAndGive temp = initArticleActivityOfUseAndGive(row, tempId++);
//			TransitRobberyManager.logger.info(temp.toString());
//			shopActivities.add(temp);
//			System.out.println(temp.getInfoShow());
			tempUseList.add(temp);
		}
		
		AllActivityManager.instance.add2AllActMap(AllActivityManager.shopBuyAct, tempShopList);
		AllActivityManager.instance.add2AllActMap(AllActivityManager.useGiveAct, tempUseList);
	}
	/**
	 * 用送活动页签初始化
	 * @param row
	 * @return
	 * @throws Exception 
	 */
	private ArticleActivityOfUseAndGive initArticleActivityOfUseAndGive(HSSFRow row, long tempId) throws Exception {
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";
		int replayType = 0;		
		String buyArtName = "";
		int buyArtColor = 1;
		boolean buyActBind = false;
		int needBuyNum = 1;
		
		String giveArtName = "";
		int giveArtColor = 1;
		boolean giveActBind = false;
		int giveNum = 1;
		byte useType = 0;
		
		String mailTitle = "";
		String mailContent = "";
		
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		startTime = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		endTime = cell.getStringCellValue().trim();
		
		cell = row.getCell(rowNum++);
		platForm = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
//		serverName = cell.getStringCellValue().trim();
		if(cell != null) {
			openServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		if(cell != null) {
			notOpenServerName = cell.getStringCellValue().trim();
		}
		TransitRobberyManager.logger.info("平台:" + platForm + "=开启服务器:" + openServerName + "=不开启服务器：" + notOpenServerName);
		
		cell = row.getCell(rowNum++);
		replayType = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		if(cell != null) {
			useType = (byte) cell.getNumericCellValue();
		}
		
		cell = row.getCell(rowNum++);
		buyArtName = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		buyArtColor = Integer.parseInt(cell.getStringCellValue().trim());
		cell = row.getCell(rowNum++);
		boolean withOutBind = false;
		try {
			buyActBind = cell.getBooleanCellValue();
		} catch (Exception e) {
			buyActBind = false;
			String tempBind = cell.getStringCellValue().trim();
			if(tempBind.toLowerCase().equals("both")) {
				withOutBind = true;
			}
		}
		cell = row.getCell(rowNum++);
		needBuyNum = (int) cell.getNumericCellValue();
		
		cell = row.getCell(rowNum++);
		giveArtName = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		giveArtColor = Integer.parseInt(cell.getStringCellValue().trim());
		cell = row.getCell(rowNum++);
		giveActBind = cell.getBooleanCellValue();
		cell = row.getCell(rowNum++);
		giveNum = (int) cell.getNumericCellValue();
		
		cell = row.getCell(rowNum++);
		mailTitle = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		mailContent = cell.getStringCellValue();
		
		ActivityProp needBuyProp = new ActivityProp(buyArtName, buyArtColor, needBuyNum, buyActBind);
		needBuyProp.setWithOutBind(withOutBind);
		ActivityProp giveProp = new ActivityProp(giveArtName, giveArtColor, giveNum, giveActBind);
		
		ArticleActivityOfUseAndGive aaou = new ArticleActivityOfUseAndGive(startTime, endTime, platForm, openServerName, notOpenServerName);
		aaou.setOtherVar(getRepayType(replayType), needBuyProp, giveProp, mailTitle, mailContent, tempId, useType);
		return aaou;
	}
	
	/**
	 * 初始化商店买送活动页签初始化
	 * @param row
	 * @throws Exception 
	 */
	private ShopActivityOfBuyAndGive initShopActivityOfBuyAndGive(HSSFRow row) throws Exception {
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";
		String shopName = "";
		int replayType = 0;		
		int buyType = 0;		
		String buyArtName = "";
		int buyArtColor = 1;
		boolean buyActBind = false;
		int needBuyNum = 1;
		
		String giveArtName = "";
		int giveArtColor = 1;
		boolean giveActBind = false;
		int giveNum = 1;
		int buyGoodsId = 0;
		
		String mailTitle = "";
		String mailContent = "";
		
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		startTime = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		endTime = cell.getStringCellValue().trim();
		
		cell = row.getCell(rowNum++);
		platForm = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		if(cell != null) {
			openServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		if(cell != null) {
			notOpenServerName = cell.getStringCellValue().trim();
		}
		
		cell = row.getCell(rowNum++);
		shopName = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		replayType = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		buyType = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		if(cell != null) {
			buyGoodsId = (int) cell.getNumericCellValue();
		}
		
		cell = row.getCell(rowNum++);
		buyArtName = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		buyArtColor = (int) Integer.parseInt(cell.getStringCellValue().trim());
		cell = row.getCell(rowNum++);
		buyActBind = cell.getBooleanCellValue();
		cell = row.getCell(rowNum++);
		needBuyNum = (int) cell.getNumericCellValue();
		
		cell = row.getCell(rowNum++);
		giveArtName = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		giveArtColor = Integer.parseInt(cell.getStringCellValue().trim());
		cell = row.getCell(rowNum++);
		giveActBind = cell.getBooleanCellValue();
		cell = row.getCell(rowNum++);
		giveNum = (int) cell.getNumericCellValue();
		
		cell = row.getCell(rowNum++);
		mailTitle = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		mailContent = cell.getStringCellValue();
		
		ActivityProp needBuyProp = new ActivityProp(buyArtName, buyArtColor, needBuyNum, buyActBind);
		needBuyProp.setGoodsId(buyGoodsId);
		ActivityProp giveProp = new ActivityProp(giveArtName, giveArtColor, giveNum, giveActBind);
		
		ShopActivityOfBuyAndGive saob = new ShopActivityOfBuyAndGive(startTime, endTime, platForm, openServerName, notOpenServerName);
		saob.setOtherVar(getRepayType(replayType), shopName, needBuyProp, giveProp, mailTitle, mailContent);
		saob.setUseGoodsId((buyType==1));
		
		
		return saob;
	}
	
	private RepayType getRepayType(int type) throws Exception{
		for(RepayType r : RepayType.values()) {
			if(r.type == type) {
				return r;
			}
		}
		throw new Exception("买送类型填写错误:" + type);
	}

	public void noticeBuySuccess(Player player, Shop shop, Goods goods, int buyNum) {
		AllActivityManager.instance.notifySthHappened(AllActivityManager.shopBuyAct, player, shop, goods, buyNum);
	}
	/**
	 * 重写通知方法，之前不满足配表需求
	 * @param player
	 * @param articles
	 */
	public void noticeUseSuccess(Player player, ArrayList<ArticleEntity> articles) {
		AllActivityManager.instance.notifySthHappened(AllActivityManager.useGiveAct, player, articles, (byte)0);
	}
	/**
	 * 重写通知方法，之前不满足配表需求
	 * @param player
	 * @param articles
	 * @param useType  (使用类型，0使用 1合成)
	 */
	public void noticeUseSuccess(Player player, ArrayList<ArticleEntity> articles, byte useType) {
		AllActivityManager.instance.notifySthHappened(AllActivityManager.useGiveAct, player, articles, useType);
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
}
