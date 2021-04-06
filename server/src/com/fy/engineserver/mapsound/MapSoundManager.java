package com.fy.engineserver.mapsound;



import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SEND_MAP_SOUND_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;

public class MapSoundManager {

	private static MapSoundManager self;
	
	public static MapSoundManager getInstance(){
		return self;
	}
	
	private MapSoundManager(){
		
	}

	public String fileName = "";

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void init() throws Exception{
		
		long now = SystemTime.currentTimeMillis();
		loadFrom(new FileInputStream(fileName));
		self = this;
		ServiceStartRecord.startLog(this);
	}
	
	HashMap<String,String> mapSoundMap = new HashMap<String,String>();
	HashMap<String,String> displayMapSoundMap = new HashMap<String,String>();
	HashMap<String,Integer> soundPlayCount = new HashMap<String,Integer>();

	public void 根据mapName得到地图音乐回复(Player player,String mapName){
		String sound = mapSoundMap.get(mapName);
		int playCount = 0;
		Integer count = soundPlayCount.get(mapName);
		if(count != null){
			playCount = count;
		}
		if(sound != null){
			SEND_MAP_SOUND_REQ req = new SEND_MAP_SOUND_REQ(GameMessageFactory.nextSequnceNum(), mapName, sound, playCount);
			player.addMessageToRightBag(req);
		}
	}
	

	public void loadFrom(InputStream is) throws Exception{
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HashMap<String,String> mapSoundMap = new HashMap<String,String>();
		HashMap<String,String> displayMapSoundMap = new HashMap<String,String>();
		HashMap<String,Integer> soundPlayCount = new HashMap<String,Integer>();
		HSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 1; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);
            if(row != null){
            	HSSFCell hc = row.getCell(0);
            	String displayName = null;
            	try{
            		displayName = (hc.getStringCellValue().trim());
            		
            	}catch(Exception ex){

            	}
            	
            	if(displayName != null){
                	hc = row.getCell(1);
                	try{
                		String sound = hc.getStringCellValue().trim();
                		displayMapSoundMap.put(displayName, sound);
                		
                		mapSoundMap.put(transferCHINAToENGLISH(displayName), sound);
                	}catch(Exception ex){

                	}
                	
                	hc = row.getCell(2);
                	try{
                		int value = (int)hc.getNumericCellValue();
                		soundPlayCount.put(transferCHINAToENGLISH(displayName), value);
                	}catch(Exception ex){
                		try{
                    		int value = Integer.parseInt(hc.getStringCellValue().trim());
                    		soundPlayCount.put(transferCHINAToENGLISH(displayName), value);
                		}catch(Exception e){
                			
                		}

                	}
            	}
            }
		}
		this.mapSoundMap = mapSoundMap;
		this.displayMapSoundMap = displayMapSoundMap;
		this.soundPlayCount = soundPlayCount;
	}

	public String transferCHINAToENGLISH(String str) {
		GameManager gm = GameManager.getInstance();
		for (GameInfo gif : gm.getGameInfos()) {
			if (gif.displayName.equals(str)) {
				return gif.name;
			}
		}
		return str;
	}

}
