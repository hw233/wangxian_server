package com.fy.engineserver.guozhan;

import java.io.Serializable;
import java.util.ArrayList;

import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;

/**
 *
 * 
 * @version 创建时间：May 3, 2012 3:12:38 PM
 * 
 */
public class Constants implements Serializable {
	
	public static final long serialVersionUID = 33497495348934343L;
	
	public int 击杀功勋值表[] = new int[]{
		100,75,50,25,10
	};

	public int 提前通知时间 = 30; //分
	
	public String 开始宣战时间 = "0:00";
	
	public String 结束宣战时间 = "18:00";
	
	public String 国战开启时间 = "20:00";
	
	public int 国战初始时长 = 90; //分
	
	public int 国战等级限制 = 40;
	
	public int 延长国战时间次数 = 2;
	
	public int 延长国战时长 = 15;
	
	public int 延长国战时长消耗资源 = 6000;
	
	public int 国战拉人次数 = 3;
	
	public int 国战拉人消耗资源 = 9000;
	
	public int 国战加血次数 = 2;
	
	public double 国战每次加血量 = 0.15;
	
	public int 国战加血消耗资源 = 6000;
	
	public int 连胜时间间隙 = 30; //秒
	
	public int 连胜播报次数[] = new int[]{2, 5, 10, 15, 20, 40, 60, 100, 150, 200, 300, 500, 1000, 2000, 5000, 8000, 10000};
	
	public boolean 是否需要连胜播报(int liansheng) {
		for(int i=0; i<连胜播报次数.length; i++) {
			if(liansheng == 连胜播报次数[i]) {
				return true;
			}
		}
		return false;
	}
	
	public String attackerBornMap = "bianjing";
	
	public String defenderLoseBornMap = "kunlunshengdian";
	
	public Object NPC_边城守将A_data[] = new Object[]{"jiamengguan", 800000005, 2046, 261,1};
	public Object NPC_边城守将B_data[] = new Object[]{"yaosai", 800000004, 966 , 446,2};
	public Object NPC_王城戌卫官_data[] = new Object[]{"kunlunshengdian", 800000003, 2308 , 2536,3};
	public Object NPC_龙象卫长_data[] = new Object[]{"kunlunshengdian", 800000002, 2153 , 1273,4};
	public Object NPC_龙象释帝_data[] = new Object[]{"kunlunshengdian", 800000001, 1469 , 814,5};
	
	int [][] bianguanxy = {{1858,3390},{1242,2320},{1881,2829}};
	int [][] mainCityxy1 = {{4600,5559},{4149,5592},{8892,750}};
	int [][] mainCityxy2 = {{4083,2921},{3858,2630},{3878,3626}};
	int [][] mainCityxy3 = {{2661,2021},{2494,1387},{2226,996}};
	int [][] yaosaiXy = {{7904,5077},{1971,2199},{2173,1346}};
	public String getBianGuan(int countryId){
		return TransportData.getBianGuanMap(countryId);
	}
	public int[] getBianGuanXY(int countryId){
		return bianguanxy[countryId-1];
	}
	
	public String getYaoSai(int countryId){
		return TransportData.getYaoSaiMap(countryId);
	}
	public int[] getYaoSaiXY(int countryId){
		return yaosaiXy[countryId-1];
	}
	public String getMainString(int countryId){
		return TransportData.getMainCityMap(countryId);
	}
	public int[] getMainXY1(int countryId){
		return mainCityxy1[countryId-1];
	}
	public int[] getMainXY2(int countryId){
		return mainCityxy2[countryId-1];
	}
	public int[] getMainXY3(int countryId){
		return mainCityxy3[countryId-1];
	}
	
	public String 控制面板说明 = Translate.国战控制面板说明;
		
	public ArrayList<FastMessage> publicMessages;
	
	public Constants() {
		publicMessages = new ArrayList<FastMessage>();
		FastMessage mes1 = new FastMessage("hd_jingong", Translate.text_3865, Translate.国战攻击口号);
		FastMessage mes2 = new FastMessage("hd_fangyu", Translate.国战防御, Translate.国战防御口号);
		FastMessage mes3 = new FastMessage("hd_chetuei", Translate.国战撤退, Translate.国战撤退口号);
		publicMessages.add(mes1);
		publicMessages.add(mes2);
		publicMessages.add(mes3);
	}
	
}
