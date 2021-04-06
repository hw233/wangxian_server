package com.fy.engineserver.playerTitles;

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

import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;

public class PlayerTitlesManager {

	
	private static PlayerTitlesManager self;
	private String fileName;

	private List<PlayerTitleTemplate> list = new ArrayList<PlayerTitleTemplate>();

	public static int[] colors = new int[]{0x00ffffff,0x00e6028d,0x00ff8400};
	
	public void init()throws Exception{
		
		self = this;
		this.loadExcel();
		System.out.println("[称号初始化完成]");
		ServiceStartRecord.startLog(this);
	}
	
	public static PlayerTitlesManager getInstance(){
		return self;
	}
	
	/**
	 * 增加配置的称号
	 * @param player
	 * @param type
	 * @param bln
	 * @return
	 */
	public boolean addTitle(Player player,String type,boolean bln){
		
		int key = this.getKeyByType(type);
		String value = this.getValueByType(type);
		int color = this.getColorByType(type);
		String buffName = this.getBuffNameByType(type);
		int buffLevl = this.getBuffLevlByType(type);
		String titleShowName = this.getTitleShowNameByType(type);
		String description = this.getDescriptionByType(type);
		String icon = this.getIconByType(type);
		long lastTime = this.getLastTimeByType(type);
		SocialManager.logger.warn("[增加称号测试] [key:"+key+"] [value:"+value+"] [color:"+color+"] [buffName:"+buffName+"] [buffLevl:"+buffLevl+"] [titleShowName:"+titleShowName+"] [description"+description+"] [icon:"+icon+"]");
		if(key >= 0 && value != null){
			player.addTitle(key, value,color,bln, buffName, buffLevl, titleShowName, description, icon, lastTime);
			if(SocialManager.logger.isWarnEnabled()){
				SocialManager.logger.warn("[增加称号成功] ["+type+"] [key:"+key+"] [value:"+value+"] [color:"+color+"] [持续时间:"+lastTime+"] ["+player.getLogString()+"]");
			}
			return true;
		}else{
			SocialManager.logger.error("[增加称号错误] ["+type+"] [key:"+key+"] [value:"+value+"] [color:"+color+"] ["+player.getLogString()+"]");
			return false;
		}
	}
	
	
	public boolean addSpecialTitle(Player player,String type,String name,boolean bln){
		
		int key = this.getKeyByType(type);
		if(key >= 0 && name != null && !name.equals("")){
			int color = this.getColorByType(type);
			String buffName = this.getBuffNameByType(type);
			int buffLevl = this.getBuffLevlByType(type);
			String titleShowName = this.getTitleShowNameByType(type);
			String description = this.getDescriptionByType(type);
			String icon = this.getIconByType(type);
			long lastTime = this.getLastTimeByType(type);
			player.addTitle(key, name,color,bln,buffName,buffLevl, titleShowName, description, icon, lastTime);
			if(SocialManager.logger.isWarnEnabled()){
				SocialManager.logger.warn("[增加称号成功] ["+type+"] [key:"+key+"] [name:"+name+"] [color:"+color+"] ["+player.getLogString()+"]");
			}
			return true;
		}else{
			SocialManager.logger.error("[增加称号错误] ["+type+"] [key:"+key+"] ["+player.getLogString()+"] [name:"+name+"]");
			return false;
		}
	}
	
	
	public boolean removeTitle(Player player,String type){
		
		int key = this.getKeyByType(type);
		if(key >= 0){
			player.removePersonTitle(key);
			return true;
		}else{
			SocialManager.logger.error("[删除称号错误] ["+type+"] [key:"+key+"] ["+player.getLogString()+"]");
			return false;
		}
	}
	
	
	public int getKeyByType(String type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getKey().equals(type)){
				return ptt.getTitleType();
			}
		}
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得称号类型根据key错误] ["+type+"]");
		}
		return -1;
	}
	public int getColorByType(String type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getKey().equals(type)){
				return ptt.getTitleColor();
			}
		}
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得称号颜色根据key错误] ["+type+"]");
		}
		return -1;
	}
	
	public String getArticleCNNnameByTitleName(String titleName) {
		if (titleName == null) {
			return "";
		}
		for(PlayerTitleTemplate ptt : list){
			if(titleName.equals(ptt.getTitleName())){
				return ptt.getArticleCNNName();
			}
		}
		return "";
	}
	
	public String getTitleName(int type){
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getTitleType() == type){
				return ptt.getTitleName();
			}
		}
		
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得titleName根据key错误] ["+type+"]");
		}
		return "";
	}
	public long getLastTimeByType(int type){
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getTitleType() == type){
				return ptt.getLastTime();
			}
		}
		
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得getLastTimeByType根据key错误] ["+type+"]");
		}
		return 0L;
	}
	public String getIconByType(int type){
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getTitleType() == type){
				return ptt.getIcon();
			}
		}
		
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得getIconByType根据key错误] ["+type+"]");
		}
		return "";
	}
	public String getDescriptionByType(int type){
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getTitleType() == type){
				return ptt.getDescription();
			}
		}
		
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得getDescriptionByType根据key错误] ["+type+"]");
		}
		return "";
	}
	public String getTitleShowNameByType(int type){
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getTitleType() == type){
				return ptt.getTitleShowName();
			}
		}
		
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得getTitleShowNameByType根据key错误] ["+type+"]");
		}
		return "";
	}
	public String getBuffNameByType(int type){
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getTitleType() == type){
				return ptt.getBuffName();
			}
		}
			
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得buff名根据key错误] ["+type+"]");
		}
		return "";
	}
	
	public int getBuffLevlByType(int type){
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getTitleType() == type){
				return ptt.getBuffLevl();
			}
		}
		
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得buff级别根据key错误] ["+type+"]");
		}
		return -1;
	}
	public String getIconByType(String type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getKey().equals(type)){
				return ptt.getIcon();
			}
		}
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得getIconByType根据key错误] ["+type+"]");
		}
		return "";
	}
	public long getLastTimeByType(String type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getKey().equals(type)){
				return ptt.getLastTime();
			}
		}
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得ggetLastTimeByType根据key错误] ["+type+"]");
		}
		return 0L;
	}
	public String getDescriptionByType(String type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getKey().equals(type)){
				return ptt.getDescription();
			}
		}
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得getDescriptionByType根据key错误] ["+type+"]");
		}
		return "";
	}
	public String getTitleShowNameByType(String type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getKey().equals(type)){
				return ptt.getTitleShowName();
			}
		}
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得getTitleShowNameByType根据key错误] ["+type+"]");
		}
		return "";
	}
	public String getTitleName(String type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getKey().equals(type)){
				return ptt.getTitleName();
			}
		}
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得titleName根据key错误] ["+type+"]");
		}
		return "";
	}
	public String getBuffNameByType(String type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getKey().equals(type)){
				return ptt.getBuffName();
			}
		}
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得buff名根据key错误] ["+type+"]");
		}
		return "";
	}
	public int getBuffLevlByType(String type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getKey().equals(type)){
				return ptt.getBuffLevl();
			}
		}
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得buff级别根据key错误] ["+type+"]");
		}
		return -1;
	}
	
	public String getValueByType(String type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getKey().equals(type)){
				return ptt.getTitleName();
			}
		}
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得称号名称根据key错误] ["+type+"]");
		}
		return null;
	}
	
	
	public int getColorByTitleType(int type){
		
		for(PlayerTitleTemplate ptt : list){
			if(ptt.getTitleType() == type){
				return ptt.getTitleColor();
			}
		}
		
		if(SocialManager.logger.isWarnEnabled()){
			SocialManager.logger.warn("[获得称号颜色根据类型错误] ["+type+"]");
		}
		return -1;
	}
	
	
	public void loadExcel() throws Exception {
		File f = new File(fileName);
		String fName = ConfigServiceManager.getInstance().getFilePath(f);
		f = new File(fName);
		if (!f.exists()) {
			throw new Exception(fName + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(fName);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);

		HSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		
		int 类型 = 0;
		int 类型id = 1;
		int 称号名 = 2;
		int 触发buff名 = 3;
		int buff级别 = 4;
		int 颜色 = 5;
		int 称号显示名 = 6;
		int 称号持续时间 = 7;
		int icon = 8;
		int 描述 = 9;
		
		
		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row != null) {
				
				HSSFCell cell = row.getCell(类型);
				String key = "";
				try {
					key = cell.getStringCellValue().trim();
				} catch (Exception ex) {
					System.out.println("类型PlayerTitlesManager.row:"+r);
					throw ex;
				}
				cell = row.getCell(类型id);
				int type = -1;
				try {
					type = (int)cell.getNumericCellValue();
				} catch (Exception ex) {
					System.out.println("类型id PlayerTitlesManager.row:"+r);
					throw ex;
				}
				
				cell = row.getCell(称号名);
				String value = "";
				try {
					value = (cell.getStringCellValue().trim());
				} catch (Exception ex) {
//					throw ex;
				}
				cell = row.getCell(触发buff名);
				String buffName = "";
				try {
					buffName = (cell.getStringCellValue().trim());
				} catch (Exception ex) {
//					throw ex;
				}
				cell = row.getCell(buff级别);
				int bufflev = 0;
				try {
					bufflev = (int)cell.getNumericCellValue();
				} catch (Exception ex) {
//					throw ex;
				}
				
				cell = row.getCell(颜色);
				int color = 0;
				try {
					color = (int)cell.getNumericCellValue();
				} catch (Exception ex) {
					System.out.println("颜色PlayerTitlesManager.row:"+r);
					throw ex;
				}

				PlayerTitleTemplate ptt = new PlayerTitleTemplate();
				ptt.setKey(key);
				ptt.setTitleType(type);
				ptt.setTitleName(value);
				ptt.setBuffName(buffName);
				ptt.setBuffLevl(bufflev);
				ptt.setTitleColor(colors[color]);
				ptt.setTitleShowName(ReadFileTool.getString(row, 称号显示名, SocialManager.logger));
				ptt.setDescription(ReadFileTool.getString(row, 描述, SocialManager.logger));
				ptt.setIcon(ReadFileTool.getString(row, icon, SocialManager.logger));
				ptt.setLastTime(ReadFileTool.getLong(row, 称号持续时间, SocialManager.logger, 0L));
				if (ptt.getLastTime() > 0) {
					ptt.setLastTime(ptt.getLastTime() * 1000);			//转换为毫秒
				}
				ptt.setArticleCNNName(ReadFileTool.getString(row, 描述+1, SocialManager.logger));
				if(!list.contains(ptt)){
					this.list.add(ptt);
				}
				
			}
		}
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	public List<PlayerTitleTemplate> getList() {
		return list;
	}

	public void setList(List<PlayerTitleTemplate> list) {
		this.list = list;
	}


	
	

	public static class PlayerTitleTemplate{
		//用于策划   这个唯一  对应一个type (int) 存在player上
		private String key;
		private String titleName;
		private int titleType;
		private String buffName;
		private int buffLevl;
		private int titleColor;
		private String titleShowName;
		private long lastTime;
		private String description;
		private String icon;
		private String articleCNNName;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getTitleName() {
			return titleName;
		}
		public void setTitleName(String titleName) {
			this.titleName = titleName;
		}
		public int getTitleType() {
			return titleType;
		}
		public void setTitleType(int titleType) {
			this.titleType = titleType;
		}
		public int getTitleColor() {
			return titleColor;
		}
		public void setTitleColor(int titleColor) {
			this.titleColor = titleColor;
		}
		public String getBuffName() {
			return buffName;
		}
		public void setBuffName(String buffName) {
			this.buffName = buffName;
		}
		public int getBuffLevl() {
			return buffLevl;
		}
		public void setBuffLevl(int buffLevl) {
			this.buffLevl = buffLevl;
		}
		public String getTitleShowName() {
			return titleShowName;
		}
		public void setTitleShowName(String titleShowName) {
			this.titleShowName = titleShowName;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public long getLastTime() {
			return lastTime;
		}
		public void setLastTime(long lastTime) {
			this.lastTime = lastTime;
		}
		public String getArticleCNNName() {
			return articleCNNName;
		}
		public void setArticleCNNName(String articleCNNName) {
			this.articleCNNName = articleCNNName;
		}
		
	}
	
	
	
	public static void main(String[] args) throws Exception{
		
		
		System.out.println(1l*0xffff8400);
//		PlayerTitlesManager ptm = new PlayerTitlesManager();
//		ptm.setFileName("F:\\config\\playerTitle.xls");
//		ptm.loadExcel();
		
		
	}
	
	
}
