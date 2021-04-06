package com.fy.engineserver.activity.fireActivity;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.fateActivity.FateManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;

public class FireManager {

	private FateManager fateManager;

	private String fireActivityName = Translate.家族篝火实体;
	public static Logger logger = LoggerFactory.getLogger(FireManager.class);

	private static FireManager self = null;

	public static FireManager getInstance() {
		return self;
	}
	public static int[] 家族经验特殊日期 = new int[]{2012,8,18};
	
	public static int[][] 指定日期 = {家族经验特殊日期,{2012,8,29},{2012,8,30},{2012,9,1},{2012,9,2},{2012,9,3}};
	
	
	public FireActivityNpcEntity createFireActivityNpc(SeptStation septStation) {
		FireActivityNpcEntity fne = new FireActivityNpcEntity(septStation
				.getId());
		fne.setSs(septStation);
		fne.setFireActivityTemplate(fireActivityName);
		fne.init(septStation);
		return fne;
	}

	
	/**
	 * 创建普通篝火npc
	 * 
	 * @return
	 */
	public CommonFireEntity createCommonFireNpc(CommonFireActivityNpc npcc) {

		for(int i=1;i<=3;i++){
			Map<String, FireActivityTemplate> map = FireManager.getInstance().fireMap2.get(new Integer(i));
			if(map == null ||map.size() == 0){
				logger.error("[fireMap 为null,i="+i+"]");
				return null;
			}
			
			CommonFireEntity commonEntity = new CommonFireEntity();
			commonEntity.setX(npcc.getX());
			commonEntity.setY(npcc.getY());
			commonEntity.setBufferName("药品回血");
			commonEntity.setDistance(400);
			commonEntity.setExpInterval((int) (2 * 1000));
			commonEntity.setLastUpdateTime(SystemTime.currentTimeMillis());
			npcc.setCommonFireEntity(commonEntity);
			if(FireManager.logger.isWarnEnabled()){
				FireManager.logger.warn("[初始化普通篝火npc] [mapName:"+npcc.getGameName()+"] [country:"+npcc.getCountry()+"] [x:"+npcc.getX()+"] [y:"+npcc.getY()+"]");
			}
			
//			for (Entry<String, FireActivityTemplate> en : map.entrySet()) {
//				FireActivityTemplate ft = en.getValue();
//				if (ft.getFireType() == 0) {
//					String mapName = ft.getMapName().trim();
//					Game game = GameManager.getInstance().getGameByName(mapName, i);
//					boolean success = false;
//					if (game != null) {
//						LivingObject[] los = game.getLivingObjects();
//						for (LivingObject lo : los) {
//							if (lo instanceof CommonFireActivityNpc) {
//								CommonFireActivityNpc commonNpc = (CommonFireActivityNpc) lo;
//
//								CommonFireEntity commonEntity = new CommonFireEntity();
//								commonEntity.setX(commonNpc.getX());
//								commonEntity.setY(commonNpc.getY());
//								commonEntity.setBufferName(ft.getBufferName());
//								commonEntity.setDistance(ft.getDistance());
//								commonEntity.setExpInterval((int) (ft.getExpInterval() * 1000));
//								commonEntity.setLastUpdateTime(SystemTime.currentTimeMillis());
//								commonNpc.setCommonFireEntity(commonEntity);
//								npcc.setCommonFireEntity(commonEntity);
//								success = true;
//								if(FireManager.logger.isWarnEnabled()){
//									FireManager.logger.warn("[初始化普通篝火npcing] [mapName:"+mapName+"] ["+game.country+"] [x:"+commonNpc.getX()+"] [y:"+commonNpc.getY()+"] [distance:"+ft.getDistance()+"]");
//								}
//								break;
//							}
//						}
//					}
//					if (game != null && success) {
//						if(FireManager.logger.isWarnEnabled()){
//							FireManager.logger.warn("[初始化普通篝火npc成功] [mapName:"+mapName+"] [country:"+i+"]");
//						}
//					} else if (game == null) {
//						FireManager.logger.error("[初始化普通篝火npc错误] [没有这个game,地图name:"+ mapName + "] [country:"+i+"]");
//					} else {
//						FireManager.logger.error("[初始化普通篝火npc错误] [这个地图没有篝火npc] [mapName:"+mapName+"] [country:"+i+"]");
//					}
//				}
//			}
			
		}

		return null;
	}

	public void init() throws Exception{
		
		try {
			
			loadExcel();
			self = this;
//			createCommonFireNpc();
			FireManager.logger.error("[初始化普通篝火活动的经验数值成功]");
		} catch (Exception e) {
			FireManager.logger.error("[初始化普通篝火活动的经验数值错误]",e);
			throw e;
		}
		ServiceStartRecord.startLog(this);

	}

	public FateManager getFateManager() {
		return fateManager;
	}

	public void setFateManager(FateManager fateManager) {
		this.fateManager = fateManager;
	}

	private Map<Integer, CommonFireExpTemplate> commonFireExpMap = new HashMap<Integer, CommonFireExpTemplate>();
	public Map<String, FireActivityTemplate> fireMap = new HashMap<String, FireActivityTemplate>();
	public Map<Integer,Map<String, FireActivityTemplate>> fireMap2 = new HashMap<Integer, Map<String,FireActivityTemplate>>();
	private String fileName;

	public void loadExcel() throws Exception{
		
		this.fireMap.clear();
		this.fireMap2.clear();
		commonFireExpMap.clear();
		
		int 人物級別 = 0;
		int 酒1 = 1;
		int 酒2 = 2;
		int 酒3 = 3;
		int 酒4 = 4;

		File file = new File(fileName);
		HSSFWorkbook workbook = null;

		InputStream is;
		is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = null;
		sheet = workbook.getSheetAt(0);
		if (sheet == null)
			return;
		int rows = sheet.getPhysicalNumberOfRows();

		for (int r = 1; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row != null) {
				HSSFCell cell = row.getCell(人物級別);
				int level = (int) (cell.getNumericCellValue());

				cell = row.getCell(酒1);
				long exp1 = (long) (cell.getNumericCellValue());
				
				cell = row.getCell(酒2);
				long exp2 = (long) (cell.getNumericCellValue());
				
				cell = row.getCell(酒3);
				long exp3 = (long) (cell.getNumericCellValue());

				cell = row.getCell(酒4);
				long exp4 = (long) (cell.getNumericCellValue());

				CommonFireExpTemplate expTemplate = new CommonFireExpTemplate(level, exp1, exp2, exp3, exp4);
				commonFireExpMap.put(level, expTemplate);
				logger.warn("读取: ["+r+"]");
			}
		}


		sheet = workbook.getSheetAt(1);
		if (sheet == null) return;
		rows = sheet.getPhysicalNumberOfRows();
		// 家族篝火实体	100	家族篝火	1000	0	20	350001;350002	1	1	1310	1140
		int 篝火_实体名  = 0;
		int 篝火_产生作用距离  = 1;
		int 篝火_产生buffer  = 2;
		int 篝火_持续时间  = 3;
		int 篝火_额外增加持续时间  = 4;
		int 篝火_经验变化间隔  = 5;
		int 篝火_npcIds  = 6;
		int 篝火_点火次数  = 7;
		int 篝火_加柴次数  = 8;
		int 篝火_npcx  = 9;
		int 篝火_npcy  = 10;
		int 篝火_类型 = 11;
		int 篝火_地图 = 12;
		
		for(int i=1;i<rows;i++){
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				FireActivityTemplate ft = new FireActivityTemplate();
				HSSFCell cell = row.getCell(篝火_实体名);
				String fireName = cell.getStringCellValue();
				ft.setFireName(fireName);
				
				cell = row.getCell(篝火_产生作用距离);
				int distance = (int)cell.getNumericCellValue();
				ft.setDistance(distance);
				
				cell = row.getCell(篝火_产生buffer);
				String bufferName = (cell.getStringCellValue());
				ft.setBufferName(bufferName);
				
				cell = row.getCell(篝火_持续时间);
				int lastTime = (int)cell.getNumericCellValue();
				ft.setLastTime(lastTime);
				
				cell = row.getCell(篝火_额外增加持续时间 );
				int extraLastTime = (int)cell.getNumericCellValue();
				ft.setExtraLastTime(extraLastTime);
				
//				cell = row.getCell(篝火_经验变化间隔);
				double expInterval = ReadFileTool.getDouble(row, 篝火_经验变化间隔, logger);
//				int expInterval = (int)cell.getNumericCellValue();
				ft.setExpInterval(expInterval);
				
				cell = row.getCell(篝火_npcIds);
				String npcs = cell.getStringCellValue();
				if(npcs.contains(";")){
					String[] arr = npcs.split(";");
					int length = arr.length;
					int [] npcIds = new int[length];
					
					for(int j = 0;j<length;j++){
						npcIds[j] = Integer.parseInt(arr[j]);
					}
					
					ft.setNpcIds(npcIds);
				}else{
					throw new Exception();
				}
				cell = row.getCell(篝火_点火次数);
				int fireNum = (int)cell.getNumericCellValue();
				ft.setFireNum(fireNum);
				
				cell = row.getCell(篝火_加柴次数);
				int addWoodNum = (int)cell.getNumericCellValue();
				ft.setAddWoodNum(addWoodNum);
				
				cell = row.getCell(篝火_npcx);
				int npcx = (int)cell.getNumericCellValue();
				ft.setX(npcx);
				
				cell = row.getCell(篝火_npcy);
				int npcy = (int)cell.getNumericCellValue();
				ft.setY(npcy);
				
				cell = row.getCell(篝火_类型);
				int fireType = (int)cell.getNumericCellValue();
				ft.setFireType(fireType);
				
				cell = row.getCell(篝火_地图);
				String mapName = cell.getStringCellValue();
				ft.setMapName(mapName);
				
				cell = row.getCell(篝火_地图+1);
				int country = (int)cell.getNumericCellValue();
				
				fireMap.put(ft.getFireName(), ft);
				
				Map<String, FireActivityTemplate> map2 = fireMap2.get(new Integer(country));
				if(map2 == null){
					map2 = new HashMap<String, FireActivityTemplate>();
				}
				map2.put(ft.getFireName(), ft);
				fireMap2.put(country, map2);
			}
		}
		
		
		
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<Integer, CommonFireExpTemplate> getCommonFireExpMap() {
		return commonFireExpMap;
	}

	public void setCommonFireExpMap(
			Map<Integer, CommonFireExpTemplate> commonFireExpMap) {
		this.commonFireExpMap = commonFireExpMap;
	}

	
	//电视通知
	public static void noticeTelevision(String content,Player player){
		ChatMessage msg = new ChatMessage();
		msg.setMessageText(content);
		try {
			ChatMessageService cms = ChatMessageService.getInstance();
			cms.sendMessageToSystem(msg);
			logger.warn("[喝橙酒电视] ["+content+"] ["+player.getLogString()+"]");
		} catch (Exception e) {
			logger.error("[喝橙酒电视异常] ["+content+"] ["+player.getLogString()+"]",e);
		}
		
	}
	
}
