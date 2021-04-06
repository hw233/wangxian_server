package com.fy.engineserver.worldmap;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.Position;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class WorldMapManager {

	private static WorldMapManager self;

	public static WorldMapManager getInstance() {
		return self;
	}

	public void init() throws Exception {
		
		loadFrom(new File(file));
		this.ddc = new DefaultDiskCache(new File(playerExploredMapFile), null, Translate.玩家到过的地图, 100L * 365 * 24 * 3600 * 1000L);
		self = this;
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {

	}

	public String file = "";

	public String playerExploredMapFile = "";

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getPlayerExploredMapFile() {
		return playerExploredMapFile;
	}

	public void setPlayerExploredMapFile(String playerExploredMapFile) {
		this.playerExploredMapFile = playerExploredMapFile;
	}

	public DefaultDiskCache ddc;

	public WorldMapArea[] worldMapAreas;
	
	public WorldMapArea[] worldMapAreas_xj;

	/**
	 * 区域中的所有地图中文名
	 */
	public HashMap<String, String[]> mapCHINANamesInArea = new HashMap<String, String[]>();

	/**
	 * 区域中的所有地图英文名
	 */
	public HashMap<String, String[]> mapENGLISHNamesInArea = new HashMap<String, String[]>();

	/**
	 * 区域中的所有地图英文名
	 */
	public static HashMap<String, MapSingleMonsterInfo> mapEnglishSingleMonsterInfos = new HashMap<String, MapSingleMonsterInfo>();

	/**
	 * 区域中的所有地图中文名
	 */
	public static HashMap<String, MapSingleMonsterInfo> mapChinaSingleMonsterInfos = new HashMap<String, MapSingleMonsterInfo>();

	/**
	 * 玩家探索过的地图
	 */
	public HashMap<Long, ArrayList<String>> playerExploredMapNames = new HashMap<Long, ArrayList<String>>();

	/**
	 * 
	 */
	public HashMap<String, short[]> 地图中NPC的坐标 = new HashMap<String, short[]>();

	/**
	 * 怪物所在的位置
	 */
	public static HashMap<String, Position> monsterPositions = new HashMap<String, Position>();
	public static HashMap<String, HashMap<Integer, Position>> monsterPositions2 = new HashMap<String, HashMap<Integer,Position>>();
	
	public static String 本国国王图标 = "benguo";
	public static String 敌国国王图标 = "diguo";
	public static String 仇人图标 = "chouren";
	public static String 队友图标 = "landian";
	public static String 敌国人图标 = "diren";//没有这个icon
	public static String 本国人图标 = "benguoren";//没有这个icon
	public static String 恶人图标 = "eren";
	public static String 结义图标 = "jieyi";
	public static String 男主角图标 = "nanzhujiao";
	public static String 女主角图标 = "nvzhujiao";
	public static String 师傅图标 = "shifu";
	public static String 徒弟图标 = "tudi";
	public static String 新郎图标 = "xinlang";
	public static String 新娘图标 = "xinniang";
	public static String 车夫图标 = "chefu_L";
	public static String 修理图标 = "xiuli_L";
	public static String 洞府图标 = "dongfu_L";
	public static String 活动图标 = "huodong_L";
	public static String 家族图标 = "lvdian1";
	public static String 驿站图标 = "yizhan_L";
	public static String 刺探图标 = "citan_L";
	public static String 宗派图标 = "landian1";
	public static String 好友图标 = "lvdian";
	public static String[] npc类型图标数组 = new String[]{车夫图标,修理图标,洞府图标,洞府图标};
	public static final int 世界地图地域所在sheet = 0;
	public static final int 区域包含地图所在sheet = 1;
	public static final int 地图上的怪物简单信息所在sheet = 2;
	public static final int 仙界地图地域所在sheet = 3;
	public static final int 仙界地图子地域所在sheet = 4;

	public static final int 世界地图地域_地域名_所在列 = 0;
	public static final int 世界地图地域_XYWidthHeight_所在列 = 1;
	public static final int 世界地图地域_pressPng_所在列 = 2;
	public static final int 世界地图地域_pressPngx_所在列 = 3;
	public static final int 世界地图地域_pressPngy_所在列 = 4;
	public static final int 世界地图地域_pressPngy_x纹理所在列 = 5;
	public static final int 世界地图地域_pressPngy_y纹理所在列 = 6;
	public static final int 世界地图地域_pressPngy_x宽度所在列 = 7;
	public static final int 世界地图地域_pressPngy_y宽度所在列 = 8;
	public static final int 世界地图地域_pressPngy_包括的子地域名所在列 = 9;
	public static final int 区域地图的背景所在纹理图片名 = 10;
	public static final int 区域地图在纹理x = 11;
	public static final int 区域地图在纹理y = 12;
	public static final int 区域地图在纹理宽 = 13;
	public static final int 区域地图在纹理高 = 14;

	public static final int 区域包含地图_areaName_所在列 = 0;
	public static final int 区域包含地图_mapDisplayNames_所在列 = 1;

	public static final int 地图上的怪物简单信息_mapName_所在列 = 0;
	public static final int 地图上的怪物简单信息_monsterNames_所在列 = 1;
	public static final int 地图上的怪物简单信息_monsterLv_所在列 = 2;
	public static final int 地图上的怪物简单信息_monsterx_所在列 = 3;
	public static final int 地图上的怪物简单信息_monstery_所在列 = 4;

	
	public String 仙界地图首界面包括的地域名[] = {Translate.乱雪迷界};
	public void loadFrom(File file) throws Exception {
		long now = System.currentTimeMillis();
		加载仙界地图地域信息();
		loadFromInputStream();
	}

	public boolean 是否是仙界第一次请求的地图(String areaname){
		for(String name:仙界地图首界面包括的地域名){
			if(name.equals(areaname)){
				return true;
			}
		}
		return false;
	}
	
	public boolean 是否是仙界地图(Player p){
		try{
			if (p.getCurrentGame() != null) {
				String mapname = p.getCurrentGame().gi.name;
				for (WorldMapArea wma : worldMapAreas_xj) {
					if (wma != null) {
						if (!"".equals(wma.engilshname)) {
							String names [] = wma.engilshname.split(",");
	//						Game.logger.warn("[获得天界区域地图] [是否是仙界地图] [NAME:"+p.getName()+"] [mapname:"+mapname+"] [names:"+Arrays.toString(names)+"]");
							for (String mapName : names) {
								if (mapname.equals(mapName)) {
									return true;
								}
							}
						} else if (wma.getMapnames() != null) {
							for (String mapName : wma.getMapnames()) {
								if (mapname.equals(mapName)) {
									return true;
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public void 加载仙界地图地域信息() throws Exception {
		InputStream is = new FileInputStream(file);
		long now = System.currentTimeMillis();
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(仙界地图地域所在sheet);
		int rowNum = sheet.getPhysicalNumberOfRows();
		List<WorldMapArea> worldMapAreas = new ArrayList<WorldMapArea>();
		for (int i = 1; i < rowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			
			HSSFCell hc = row.getCell(世界地图地域_地域名_所在列);
			WorldMapArea wma = new WorldMapArea();
			worldMapAreas.add(wma);
			try {
				String name = (hc.getStringCellValue().trim());
				wma.name = name;
			} catch (Exception e) {
				throw e;
			}

			hc = row.getCell(世界地图地域_XYWidthHeight_所在列);
			try {
				String XYWidthHeight = hc.getStringCellValue().trim();
				String[] arrays = XYWidthHeight.split("@");
				if (arrays.length == 1) {
					arrays = XYWidthHeight.split("@");
				}
				short[] xs = new short[arrays.length];
				short[] ys = new short[arrays.length];
				short[] widths = new short[arrays.length];
				short[] heights = new short[arrays.length];
				wma.worldMapAreaX = xs;
				wma.worldMapAreaY = ys;
				wma.worldMapAreaWidth = widths;
				wma.worldMapAreaHeight = heights;
				for (int j = 0; j < arrays.length; j++) {
					String a = arrays[j];
					String[] as = a.split(";");
					if (as.length == 1) {
						as = a.split("；");
					}
					xs[j] = Short.parseShort(as[0]);
					ys[j] = Short.parseShort(as[1]);
					widths[j] = Short.parseShort(as[2]);
					heights[j] = Short.parseShort(as[3]);
				}
			} catch (Exception e) {
				throw e;
			}

			hc = row.getCell(世界地图地域_pressPng_所在列);
			try {
				String pressPng = hc.getStringCellValue().trim();
				wma.pressPng = pressPng;
			} catch (Exception e) {
				throw e;
			}

			hc = row.getCell(世界地图地域_pressPngx_所在列);
			try {
				short pressPngx = (short) hc.getNumericCellValue();
				wma.pressPngx = pressPngx;
			} catch (Exception e) {
				throw e;
			}

			hc = row.getCell(世界地图地域_pressPngy_所在列);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.pressPngy = pressPngy;
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(世界地图地域_pressPngy_x纹理所在列);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.x_wl = pressPngy;
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(世界地图地域_pressPngy_x纹理所在列);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.x_wl = pressPngy;
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(世界地图地域_pressPngy_y纹理所在列);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.y_wl = pressPngy;
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(世界地图地域_pressPngy_x宽度所在列);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.press_kd = pressPngy;
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(世界地图地域_pressPngy_y宽度所在列);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.press_gd = pressPngy;
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(世界地图地域_pressPngy_包括的子地域名所在列);
			try {
				String names = hc.getStringCellValue().trim();
				if(names!=null && names.trim().length()>0){
					wma.mapnames = names.split(",");
				}else{
					wma.mapnames = new String[]{};
				}
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(区域地图的背景所在纹理图片名);
			try {
				String names = hc.getStringCellValue().trim();
				wma.area_bg_image_name = names;
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(区域地图在纹理x);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.area_x_wl = pressPngy;
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(区域地图在纹理y);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.area_y_wl = pressPngy;
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(区域地图在纹理宽);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.area_x_wl_kd = pressPngy;
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(区域地图在纹理高);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.area_y_wl_gd = pressPngy;
			} catch (Exception e) {
				throw e;
			}
		}

		//
		{
			sheet = workbook.getSheetAt(仙界地图子地域所在sheet);
			int rowNums = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rowNums; i++) {
				HSSFRow row = sheet.getRow(i);
				HSSFCell hc = row.getCell(世界地图地域_地域名_所在列);
				WorldMapArea wma = new WorldMapArea();
				worldMapAreas.add(wma);
				try {
					String name = (hc.getStringCellValue().trim());
					wma.name = name;
				} catch (Exception e) {
					throw e;
				}

				hc = row.getCell(世界地图地域_XYWidthHeight_所在列);
				try {
					String XYWidthHeight = hc.getStringCellValue().trim();
					String[] arrays = XYWidthHeight.split("@");
					if (arrays.length == 1) {
						arrays = XYWidthHeight.split("@");
					}
					short[] xs = new short[arrays.length];
					short[] ys = new short[arrays.length];
					short[] widths = new short[arrays.length];
					short[] heights = new short[arrays.length];
					wma.worldMapAreaX = xs;
					wma.worldMapAreaY = ys;
					wma.worldMapAreaWidth = widths;
					wma.worldMapAreaHeight = heights;
					for (int j = 0; j < arrays.length; j++) {
						String a = arrays[j];
						String[] as = a.split(";");
						if (as.length == 1) {
							as = a.split("；");
						}
						xs[j] = Short.parseShort(as[0]);
						ys[j] = Short.parseShort(as[1]);
						widths[j] = Short.parseShort(as[2]);
						heights[j] = Short.parseShort(as[3]);
					}
				} catch (Exception e) {
					throw e;
				}

				hc = row.getCell(世界地图地域_pressPng_所在列);
				try {
					String pressPng = hc.getStringCellValue().trim();
					wma.pressPng = pressPng;
				} catch (Exception e) {
					throw e;
				}

				hc = row.getCell(世界地图地域_pressPngx_所在列);
				try {
					short pressPngx = (short) hc.getNumericCellValue();
					wma.pressPngx = pressPngx;
				} catch (Exception e) {
					throw e;
				}

				hc = row.getCell(世界地图地域_pressPngy_所在列);
				try {
					short pressPngy = (short) hc.getNumericCellValue();
					wma.pressPngy = pressPngy;
				} catch (Exception e) {
					throw e;
				}
				
				hc = row.getCell(世界地图地域_pressPngy_x纹理所在列);
				try {
					short pressPngy = (short) hc.getNumericCellValue();
					wma.x_wl = pressPngy;
				} catch (Exception e) {
					throw e;
				}
				
				hc = row.getCell(世界地图地域_pressPngy_x纹理所在列);
				try {
					short pressPngy = (short) hc.getNumericCellValue();
					wma.x_wl = pressPngy;
				} catch (Exception e) {
					throw e;
				}
				
				hc = row.getCell(世界地图地域_pressPngy_y纹理所在列);
				try {
					short pressPngy = (short) hc.getNumericCellValue();
					wma.y_wl = pressPngy;
				} catch (Exception e) {
					throw e;
				}
				
				hc = row.getCell(世界地图地域_pressPngy_x宽度所在列);
				try {
					short pressPngy = (short) hc.getNumericCellValue();
					wma.press_kd = pressPngy;
				} catch (Exception e) {
					throw e;
				}
				
				hc = row.getCell(世界地图地域_pressPngy_y宽度所在列);
				try {
					short pressPngy = (short) hc.getNumericCellValue();
					wma.press_gd = pressPngy;
				} catch (Exception e) {
					throw e;
				}
				
				hc = row.getCell(世界地图地域_pressPngy_包括的子地域名所在列);
				try {
					String name = hc.getStringCellValue().trim();
					wma.engilshname = name;
				} catch (Exception e) {
					throw e;
				}
				
			}
			
		}
		this.worldMapAreas_xj = worldMapAreas.toArray(new WorldMapArea[0]);
		
		is.close();
	}
	
	public void loadFromInputStream() throws Exception {
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(世界地图地域所在sheet);
		int rowNum = sheet.getPhysicalNumberOfRows();
		List<WorldMapArea> worldMapAreas = new ArrayList<WorldMapArea>();
		HashMap<String, String[]> mapNamesInArea = new HashMap<String, String[]>();
		HashMap<String, MapSingleMonsterInfo> mapChinaSingleMonsterInfos = new HashMap<String, MapSingleMonsterInfo>();
		HashMap<String, MapSingleMonsterInfo> mapEnglishSingleMonsterInfos = new HashMap<String, MapSingleMonsterInfo>();
		for (int i = 1; i < rowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell hc = row.getCell(世界地图地域_地域名_所在列);
			WorldMapArea wma = new WorldMapArea();
			worldMapAreas.add(wma);
			try {
				String name = (hc.getStringCellValue().trim());
				wma.name = name;
			} catch (Exception e) {
				throw e;
			}

			hc = row.getCell(世界地图地域_XYWidthHeight_所在列);
			try {
				String XYWidthHeight = hc.getStringCellValue().trim();
				String[] arrays = XYWidthHeight.split("@");
				if (arrays.length == 1) {
					arrays = XYWidthHeight.split("@");
				}
				short[] xs = new short[arrays.length];
				short[] ys = new short[arrays.length];
				short[] widths = new short[arrays.length];
				short[] heights = new short[arrays.length];
				wma.worldMapAreaX = xs;
				wma.worldMapAreaY = ys;
				wma.worldMapAreaWidth = widths;
				wma.worldMapAreaHeight = heights;
				for (int j = 0; j < arrays.length; j++) {
					String a = arrays[j];
					String[] as = a.split(";");
					if (as.length == 1) {
						as = a.split("；");
					}
					xs[j] = Short.parseShort(as[0]);
					ys[j] = Short.parseShort(as[1]);
					widths[j] = Short.parseShort(as[2]);
					heights[j] = Short.parseShort(as[3]);
				}
			} catch (Exception e) {
				throw e;
			}

			hc = row.getCell(世界地图地域_pressPng_所在列);
			try {
				String pressPng = hc.getStringCellValue().trim();
				wma.pressPng = pressPng;
			} catch (Exception e) {
				throw e;
			}

			hc = row.getCell(世界地图地域_pressPngx_所在列);
			try {
				short pressPngx = (short) hc.getNumericCellValue();
				wma.pressPngx = pressPngx;
			} catch (Exception e) {
				throw e;
			}

			hc = row.getCell(世界地图地域_pressPngy_所在列);
			try {
				short pressPngy = (short) hc.getNumericCellValue();
				wma.pressPngy = pressPngy;
			} catch (Exception e) {
				throw e;
			}
		}

		sheet = workbook.getSheetAt(区域包含地图所在sheet);
		rowNum = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell hc = row.getCell(区域包含地图_areaName_所在列);
			String name = (hc.getStringCellValue().trim());
			hc = row.getCell(区域包含地图_mapDisplayNames_所在列);
			try {
				String mapDisplayNames = (hc.getStringCellValue().trim());
				String[] arrays = mapDisplayNames.split(";");
				if (arrays.length == 1) {
					arrays = mapDisplayNames.split("；");
				}
				mapNamesInArea.put(name, arrays);
			} catch (Exception e) {
				throw e;
			}
		}

		this.worldMapAreas = worldMapAreas.toArray(new WorldMapArea[0]);
		this.mapCHINANamesInArea = mapNamesInArea;
		mapENGLISHNamesInArea.clear();
		for (String s : mapCHINANamesInArea.keySet()) {
			String[] value = mapCHINANamesInArea.get(s);
			String[] englishValues = new String[value.length];
			for (int i = 0; i < value.length; i++) {
				englishValues[i] = transferCHINAToENGLISH(value[i]);
			}
			mapENGLISHNamesInArea.put(s, englishValues);
		}
		is.close();
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

	public synchronized boolean isPlayerExploredMap(long playerId, String mapName) {
		ArrayList<String> list = playerExploredMapNames.get(playerId);
		if (list != null) {
			if (list.contains(mapName)) {
				return true;
			}
		} else {
			if (ddc != null) {
				list = (ArrayList) ddc.get(playerId);
				if (list != null) {
					playerExploredMapNames.put(playerId, list);
				}
				if (list != null && list.contains(mapName)) {
					return true;
				}
			}
		}
		return false;
	}

	public synchronized void setPlayerExploredMap(long playerId, String mapName) {
		ArrayList<String> list = playerExploredMapNames.get(playerId);
		if (list != null) {
			if (!list.contains(mapName)) {
				list.add(mapName);
				ddc.put(playerId, list);
			}
		} else {
			if (ddc != null) {
				list = (ArrayList) ddc.get(playerId);
				if (list == null) {
					list = new ArrayList<String>();
					list.add(mapName);
					ddc.put(playerId, list);
				} else {
					if (!list.contains(mapName)) {
						list.add(mapName);
						ddc.put(playerId, list);
					}
				}
				playerExploredMapNames.put(playerId, list);
			}
		}
	}

	public WorldMapArea[] getWorldMapAreas_xj() {
		return worldMapAreas_xj;
	}

	public void setWorldMapAreas_xj(WorldMapArea[] worldMapAreas_xj) {
		this.worldMapAreas_xj = worldMapAreas_xj;
	}

}
