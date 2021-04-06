package com.fy.engineserver.core.res;


import static com.fy.engineserver.datasource.language.MultiLanguageTranslateManager.languageTranslate;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Navigator;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Polygon;
import com.fy.engineserver.core.g2d.Road;
import com.fy.engineserver.core.g2d.SignPost;
import com.fy.engineserver.datasource.language.MultiLanguageTranslateManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;

public class GameMap extends AbstractResource {

	public String name, displayName = "";
	public int mapwidth, mapheight;
	// short cellw,cellh;
	// short row,col;
	// 碰撞，导航
	public Navigator navigator;
	// 地图中局部区域的名称
	// 局部区域的位置和宽高
	public MapArea[] mapAreas;
	public MapPolyArea[] mapPolyAreas;
	// 地图中的功能性NPC
	// 传送点
	private TransportData[] transports = new TransportData[0];

	public TransportData[] getTransports() {
		return transports;
	}

	// 禁止PVP 玩家不能被玩家攻击
	// 客户端：滚动文字提示禁止攻击
	protected boolean limitPVP = false;
	// 禁止决斗 自动拒绝决斗请求
	// 服务端：自动拒绝决斗请求-
	protected boolean limitQIECUO = false;
	// 禁止飞行 不能处于飞行状态 飞行道具类型
	protected boolean limitFLY = false;
	// 禁止骑马 不能骑马
	protected boolean limitMOUNT = false;

	// 这个可以用DiTuTiLiNPC，DiTuPetNPC，DiTuWanBaoNPC设置
	public boolean 禁止使用召唤玩家道具;

	// 0为正常地图，1为宠物岛时间地图，2为挂机时间地图，3为万宝阁时间地图，4为仙蒂时间地图
	public byte 时间地图类型 = 0;

	public static final byte TIME_MAP_TYPE_PET = 1;

	public static final byte TIME_MAP_TYPE_TILI = 2;

	public static final byte TIME_MAP_TYPE_WANBAO = 3;
	
	public static final byte TIME_MAP_TYPE_XIANDI = 4;
	public static final byte TIME_MAP_TYPE_BAGUAXIANQUE = 7;
	/** 以后新加的限制地图类型，用此类型来区分 */
	public static final byte TIME_MAP_TYPE_LIMITDITU_START = 5;
	public static final byte TIME_MAP_TYPE_LIMITDITU_END = 6;
	
	public static void main(String[] args) {
		FileFilter gamemapFilter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().endsWith(".xmd");
			}
		};
		File f = new File("I:\\work\\Hg\\fy_server\\fy_server\\conf\\game_init_config\\bindata\\map\\lowMap\\kunlunshengdian.xmd");
//		File[] fs = dir.listFiles(gamemapFilter);		
//		for (File f : fs) {
	    	FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				DataInputStream dis = new DataInputStream(fis);	
				GameMap map=new GameMap();
				map.load(dis);
				MapPolyArea[] mpas=map.getMapPolyAreaByPoint(9601, 1291);
				for(int i=0;i<mpas.length;i++){
					System.out.println(mpas[i].getName()+","+mpas[i].getType());
				}
//				ArrayList<String> v = getStringArrayFromGameMapStream(dis);
//				
//				
//				System.out.println("地图文件:"+f.getName());
//				for( int i=0;i< v.size();i++){
//					System.out.print(v.get(i)+",");
//				}
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
	}
	public static ArrayList<String> getStringArrayFromGameMapStream(DataInputStream is)throws Exception {
		ArrayList<String> v = new ArrayList<String>();
		is.skipBytes(3*4);
		v.add(is.readUTF());
		v.add(is.readUTF());

		// 碰撞，导航点，到航线
		short polygonCount = is.readShort();
		for (int i = 0; i < polygonCount; i++) {
			int pointCount = is.readShort();
			is.skipBytes(pointCount*4);
		}
		// 读取导航图中的路标
		int signPostCount = is.readShort();
		is.skipBytes(signPostCount*4);

		// 读取导航图中的路线
		short roadCount = is.readShort();
		is.skipBytes(roadCount*6);
		
		// 地图中的区域
		int numMapAreas = is.readShort();
		for (int i = 0; i < numMapAreas; i++) {			
			v.add(is.readUTF());
			is.skipBytes(10);
		}
		int numstring = is.readShort();
		for (int i = 0; i < numstring; i++) {
			is.readUTF();
		}

		// 传送点
		short numtransport = is.readShort();
		for (int i = 0; i < numtransport; i++) {
			is.skipBytes(18);
			v.add(is.readUTF());
			v.add(is.readUTF());
			is.skipBytes(4);
		}
		// 远景层
		is.skipBytes(8);
		skipGameMapLayer(is);
		// 装饰层
		skipGameMapLayer(is);
		// 阴影层
		skipGameMapLayer(is);
		// 地物层
		skipGameMapLayer(is);
		// 平铺层
		skipGameMapLayer(is);
		// 光效层
		skipGameMapLayer(is);
		// 多边形区域
		try {
			numMapAreas = is.readShort();
			for (int i = 0; i < numMapAreas; i++) {
				v.add(is.readUTF());
				is.skipBytes(2);
				int pointCount = is.readShort();
				is.skipBytes(pointCount*4);				
			}
		} catch (Exception e) {
		}
		return v;
	}
	public static ArrayList<String> getSendClientFromGameMapStream(DataInputStream is)throws Exception {
		ArrayList<String> v = new ArrayList<String>();
		is.skipBytes(3*4);
		v.add(is.readUTF());
		v.add(is.readUTF());

		// 碰撞，导航点，到航线
		short polygonCount = is.readShort();
		for (int i = 0; i < polygonCount; i++) {
			int pointCount = is.readShort();
			is.skipBytes(pointCount*4);
		}
		// 读取导航图中的路标
		int signPostCount = is.readShort();
		is.skipBytes(signPostCount*4);

		// 读取导航图中的路线
		short roadCount = is.readShort();
		is.skipBytes(roadCount*6);
		
		// 地图中的区域
		int numMapAreas = is.readShort();
		for (int i = 0; i < numMapAreas; i++) {			
			is.readUTF();
			is.skipBytes(10);
		}
		int numstring = is.readShort();
		for (int i = 0; i < numstring; i++) {
			is.readUTF();
		}

		// 传送点
		short numtransport = is.readShort();
		for (int i = 0; i < numtransport; i++) {
			is.skipBytes(18);
			v.add(is.readUTF());
			v.add(is.readUTF());
			is.skipBytes(4);
		}
		// 远景层
		is.skipBytes(8);
		skipGameMapLayer(is);
		// 装饰层
		skipGameMapLayer(is);
		// 阴影层
		skipGameMapLayer(is);
		// 地物层
		skipGameMapLayer(is);
		// 平铺层
		skipGameMapLayer(is);
		// 光效层
		skipGameMapLayer(is);
		// 多边形区域
//		try {
//			numMapAreas = is.readShort();
//			for (int i = 0; i < numMapAreas; i++) {
//				v.add(is.readUTF());
//				is.skipBytes(2);
//				int pointCount = is.readShort();
//				is.skipBytes(pointCount*4);				
//			}
//		} catch (Exception e) {
//		}
		return v;
	}
	public void load(DataInputStream is) throws Exception {
		version = is.readInt();
		mapwidth = is.readInt();
		mapheight = is.readInt();
		name = is.readUTF();
		displayName = MultiLanguageTranslateManager.languageTranslate(is.readUTF());
		// 碰撞，导航点，到航线
		short polygonCount = is.readShort();
		Polygon[] polygons = new Polygon[polygonCount];
		for (int i = 0; i < polygonCount; i++) {
			int pointCount = is.readShort();
			Point[] points = new Point[pointCount];
			for (int j = 0; j < pointCount; j++) {
				short x = is.readShort();
				short y = is.readShort();
				points[j] = new Point(x, y);
			}
			polygons[i] = new Polygon(points);
		}
		// 读取导航图中的路标
		int signPostCount = is.readShort();
		SignPost[] signPosts = new SignPost[signPostCount];
		for (short i = 0; i < signPostCount; i++) {
			short x = is.readShort();
			short y = is.readShort();
			signPosts[i] = new SignPost(x, y, i);
		}

		// 读取导航图中的路线
		short roadCount = is.readShort();
		Road[] roads = new Road[roadCount];
		for (int i = 0; i < roadCount; i++) {
			short index1 = is.readShort();
			short index2 = is.readShort();
			short length = is.readShort();
			roads[i] = new Road(signPosts[index1], signPosts[index2], length);
			signPosts[index1].addRoad(roads[i]);
			signPosts[index2].addRoad(roads[i]);
		}
		navigator = new Navigator(signPosts, roads, polygons);

		// 地图中的区域
		int numMapAreas = is.readShort();
		mapAreas = new MapArea[numMapAreas];
		for (int i = 0; i < numMapAreas; i++) {
			mapAreas[i] = new MapArea();
			mapAreas[i].name = is.readUTF();
//			TaskSubSystem.logger.error("mapAreas.name:" + mapAreas[i].name);
			mapAreas[i].name = languageTranslate(mapAreas[i].name);
			mapAreas[i].x = is.readShort();
			mapAreas[i].y = is.readShort();
			mapAreas[i].width = is.readShort();
			mapAreas[i].height = is.readShort();
			mapAreas[i].type = is.readShort();
		}
		int numstring = is.readShort();
		String arrayString[] = new String[numstring];
		for (int i = 0; i < numstring; i++) {
			arrayString[i] = is.readUTF();
		}

		// 传送点
		short numtransport = is.readShort();
		transports = new TransportData[numtransport];
		for (int i = 0; i < numtransport; i++) {
			is.readByte();
			is.readShort();// part
			is.readShort();// anim
			short x = is.readShort();
			short y = is.readShort();
			is.readByte();// transform
			is.readInt();//
			short areaW = is.readShort();
			short areaH = is.readShort();
			String targetMapDisplay = is.readUTF();
			String targetMap = is.readUTF();
			short targetX = is.readShort();
			short targetY = is.readShort();
			String targetMapDisplay1="";
			
			String targetMap1 = "";
			short targetX1 = 0;
			short targetY1 = 0;
			
			String targetMapDisplay2 = "";
			String targetMap2 = "";
			short targetX2 = 0;
			short targetY2 = 0;
			
			String targetMapDisplay3 = "";
			String targetMap3="";
			short targetX3 = 0;
			short targetY3 =0;
			
			boolean isHaveExpetion=false;
			try{
			targetMapDisplay1 = is.readUTF();
			targetMap1 = is.readUTF();
			targetX1 = is.readShort();
			targetY1 = is.readShort();
			
			targetMapDisplay2 = is.readUTF();
			targetMap2 = is.readUTF();
			targetX2 = is.readShort();
			targetY2 = is.readShort();
			
			targetMapDisplay3 = is.readUTF();
			targetMap3 = is.readUTF();
			targetX3 = is.readShort();
			targetY3 = is.readShort();
			}catch(Exception e){
				
				System.out.println("load error mapname="+name);
				e.printStackTrace();
				isHaveExpetion=true;
			}

			if(isHaveExpetion){ 
				mapPolyAreas = new MapPolyArea[0];
				transports=new TransportData[0];
				return;
			}else{
				transports[i] = new TransportData(x, y, areaW, areaH, targetMap, targetMapDisplay, targetX, targetY);
				transports[i].setCarrerTransportData(targetMap1,targetMapDisplay1,targetX1,targetY1,targetMap2,targetMapDisplay2,targetX2,targetY2,targetMap3,targetMapDisplay3,targetX3,targetY3);
			}
		}
		// 远景层
		is.skipBytes(8);
		skipGameMapLayer(is);
		// 装饰层
		skipGameMapLayer(is);
		// 阴影层
		skipGameMapLayer(is);
		// 地物层
		skipGameMapLayer(is);
		// 平铺层
		skipGameMapLayer(is);
		// 光效层
		skipGameMapLayer(is);
		// 多边形区域
		try {
			numMapAreas = is.readShort();
			mapPolyAreas = new MapPolyArea[numMapAreas];
			for (int i = 0; i < numMapAreas; i++) {
				mapPolyAreas[i] = new MapPolyArea();
				mapPolyAreas[i].name = is.readUTF();
				TaskSubSystem.logger.error("mapPolyAreas.name:" + mapPolyAreas[i].name);
				mapPolyAreas[i].name = languageTranslate(mapPolyAreas[i].name);
				mapPolyAreas[i].type = is.readShort();

				int pointCount = is.readShort();
				Point[] points = new Point[pointCount];
				for (int j = 0; j < pointCount; j++) {
					short x = is.readShort();
					short y = is.readShort();
					points[j] = new Point(x, y);
				}
				mapPolyAreas[i].poly = new Polygon(points);
			}
		} catch (Exception e) {
			mapPolyAreas = new MapPolyArea[0];
//			if (Game.logger.isErrorEnabled()) Game.logger.error("[GameMap][{}][polyAreaNum:{}]\n", new Object[] { name, mapPolyAreas.length }, e);
			// throw e;
		}
//		if (Game.logger.isInfoEnabled()) Game.logger.info("[GameMap][{}][polyAreaNum:{}]\n", new Object[] { name, mapPolyAreas.length });
	}

	static private void skipGameMapLayer(DataInputStream is) throws IOException {
		int numobj = is.readShort();
		for (int i = 0; i < numobj; i++) {
			byte type = is.readByte();
			if (type == 0) {// OBJ_TYPE_PNG)
				is.skipBytes(2 * 4 + 1);
			} else if (type == 3) {// OBJ_TYPE_PNGRES_RECT
				is.skipBytes(2 * 5 + 1);
			} else {// OBJ_TYPE_ANIM
				is.skipBytes(2 * 4 + 1 + 4);
			}
		}
	}

	public String getMapName() {
		// TODO Auto-generated method stub
		return name;
	}

	// public int getCellW() {
	// // TODO Auto-generated method stub
	// return cellw;
	// }
	//
	// public int getCellH() {
	// // TODO Auto-generated method stub
	// return cellh;
	// }
	public int getWidth() {
		return mapwidth;// cellw*col;
	}

	public int getHeight() {
		return mapheight;// cellh*row;
	}

	public boolean isLimitPVP() {
		return limitPVP;
	}

	public boolean isLimitQIECUO() {
		return limitQIECUO;
	}

	public boolean isLimitFLY() {
		return limitFLY;
	}

	public boolean isLimitMOUNT() {
		return limitMOUNT;
	}

	public void setLimitPVP(boolean pvp) {
		limitPVP = pvp;
	}

	public void setLimitQIECUO(boolean qiecuo) {
		limitQIECUO = qiecuo;
	}

	public void setLimitFLY(boolean canfly) {
		limitFLY = canfly;
	}

	public void setLimitMOUNT(boolean canMountMa) {
		limitMOUNT = canMountMa;
	}

	public MapArea[] getMapAreas() {
		return mapAreas;
	}

	public MapArea getMapAreaByPoint(float x, float y) {
		for (int i = 0; mapAreas != null && i < mapAreas.length; i++) {
			if (mapAreas[i].x <= x && mapAreas[i].y <= y && mapAreas[i].x + mapAreas[i].width > x && mapAreas[i].y + mapAreas[i].height > y) {
				return mapAreas[i];
			}
		}
		return null;
	}

	public MapPolyArea[] getMapPolyAreaByPoint(float x, float y) {
		List<MapPolyArea> list = new ArrayList<MapPolyArea>();
		for (int i = 0; mapPolyAreas != null && i < mapPolyAreas.length; i++) {
			MapPolyArea ma = mapPolyAreas[i];
			if (ma != null && ma.poly != null && ma.poly.isPointInside((int) x, (int) y)) {
				list.add(ma);
			}
		}
		return list.toArray(new MapPolyArea[0]);
	}

	/**
	 * 检查位置是否在指定类型的多边形碰撞内
	 * @param x
	 * @param y
	 * @param areaType
	 * @return
	 */
	public boolean isInPolyArea(float x, float y, int areaType) {
		for (int i = 0; mapPolyAreas != null && i < mapPolyAreas.length; i++) {
			MapPolyArea ma = mapPolyAreas[i];
			if (ma != null && ma.type == areaType && ma.poly != null && ma.poly.isPointInside((int) x, (int) y)) {
				return true;
			}
		}
		return false;
	}

	public MapArea getMapAreaByName(String name) {
		for (int i = 0; i < mapAreas.length; i++) {
			if (mapAreas[i].name.equals(name)) {
				return mapAreas[i];
			}
		}
		return null;
	}

	public MapPolyArea getMapPolyAreaByName(String name) {
		for (int i = 0; i < mapPolyAreas.length; i++) {
			if (mapPolyAreas[i].name.equals(name)) {
				return mapPolyAreas[i];
			}
		}
		return null;
	}

	public MapArea[] getMapAreasByName(String name) {
		List<MapArea> mas = new ArrayList<MapArea>();
		for (int i = 0; i < mapAreas.length; i++) {
			if (mapAreas[i].name.equals(name)) {
				mas.add(mapAreas[i]);
			}
		}
		return mas.toArray(new MapArea[0]);
	}
}
