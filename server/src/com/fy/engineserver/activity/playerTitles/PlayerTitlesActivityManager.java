package com.fy.engineserver.activity.playerTitles;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.playerTitles.model.PlayerTitleActivityModel;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.playerTitles.PlayerTitle;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet2.PetGrade;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ServerFit4Activity;

public class PlayerTitlesActivityManager {
	///配表路径
	private String fileName;
	
	public static PlayerTitlesActivityManager instance;
	
	public Map<Integer, PlayerTitleActivityModel> ptMap = new HashMap<Integer, PlayerTitleActivityModel>();
	
	public void init() throws Exception {
		
		loadFile();
		ServiceStartRecord.startLog(this);
	}
	private PlayerTitlesActivityManager() {
		instance = this;
	}
	
	public static void main(String[] args) throws Exception {
		PlayerTitlesActivityManager p = new PlayerTitlesActivityManager();
		p.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//activity//playerTitlesActivity.xls");
		p.init();
	}
	
	public void exchangeTitle(Player p, int type) {
		PlayerTitleActivityModel model = ptMap.get(type);
		if(model == null) {
			TransitRobberyManager.logger.error("[兑换称号活动] [错误] [不存在类型:" + type + "]");
			p.sendError(Translate.服务器出现错误);
			return ;
		}
		long currentTime = System.currentTimeMillis();
		if(!model.getFit().thiserverFit()) {
			p.sendError(Translate.无效的活动);
			return;
		}
		if(model.getStartTime() > currentTime || model.getEndTime() < currentTime) {
			p.sendError(Translate.活动还未开启);
			return;
		}
		List<PlayerTitle> tl = p.getPlayerTitles();
		for(PlayerTitle ptl : tl) {
			if(ptl.getTitleName().equals(model.getTargetTitleName())) {
				p.sendError(Translate.已经拥有此称号);
				return;
			}
		}
		int count = p.countArticleInKnapsacksByName(model.getNeedArticle());
		if(count < model.getNeedNum()) {
			p.sendError(Translate.text_trade_006);
			return;
		}
		for(int i=0; i<model.getNeedNum(); i++) {
			ArticleEntity ae = p.removeArticleByNameColorAndBind(model.getNeedArticle(), model.getNeedColorType(), BindType.BOTH, "兑换称号活动", true);
			if(ae == null) {
				p.sendError(Translate.删除物品失败);
				TransitRobberyManager.logger.error("[兑换称号活动] [错误] [删除物品失败] [已删除个数:" + i + "] [物品名:" + model.getNeedArticle() + "] [颜色:" + model.getNeedColorType() + "][" + p.getLogString() + "]");
				return ;
			}
		}
		boolean result = PlayerTitlesManager.getInstance().addTitle(p, model.getTargetTitleName(), true);
		if(result) {
			p.sendError(Translate.兑换成功);
		} 
		TransitRobberyManager.logger.warn("[兑换称号活动] [成功] [兑换结果:" + result + "] [" + p.getLogString() +"] [获得称号:" + model.getTargetTitleName() + "]");
	}
	
	private void loadFile() throws Exception {
		File f = new File(getFileName());
		if(!f.exists()){
			throw new Exception(f.getName() + "配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);			
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			int tempIndex = 0;
			PlayerTitleActivityModel pm = new PlayerTitleActivityModel();
			pm.setId(getInt(row, tempIndex++));
			String startTime = PetGrade.getString(row, tempIndex++, TransitRobberyManager.logger);
			String endTime = PetGrade.getString(row, tempIndex++, TransitRobberyManager.logger);
			String platForm = PetGrade.getString(row, tempIndex++, TransitRobberyManager.logger);
			String openServers = PetGrade.getString(row, tempIndex++, TransitRobberyManager.logger);
			String notOpenServers = PetGrade.getString(row, tempIndex++, TransitRobberyManager.logger);
			ServerFit4Activity fit = new ServerFit4Activity(platForm, openServers, notOpenServers);
			pm.setFit(fit);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			pm.setStartTime(format.parse(startTime).getTime());
			pm.setEndTime(format.parse(endTime).getTime());
			pm.setNeedArticle(PetGrade.getString(row, tempIndex++, TransitRobberyManager.logger));
			pm.setNeedColorType(getInt(row, tempIndex++));
			pm.setNeedNum(getInt(row, tempIndex++));
			pm.setTargetTitleName(PetGrade.getString(row, tempIndex++, TransitRobberyManager.logger));
			ptMap.put(pm.getId(), pm);
//			System.out.println(pm);
		}
	}
	
	public static int getInt(HSSFRow row, int cellIdx) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
			return 0;
		}
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			return (int) cell.getNumericCellValue();
		}
		if(cell.getStringCellValue().trim().length()>0){
			return Integer.parseInt(cell.getStringCellValue());
		}
		return 0;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
