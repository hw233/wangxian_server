package com.fy.engineserver.core;

public class TransportData {
	private final int x;
	private final int y;
	private final int halfWidth;
	private final int halfHeight;
	private String targetMapDispalyName;
	private String targetMapName;
	private final int targetMapX;
	private final int targetMapY;
	public String getTargetMapDispalyName() {
		return targetMapDispalyName;
	}
	public int getTransMapNum(){
		if(targetMapDispalyName!=null&&targetMapDispalyName.isEmpty()==false) return 1;
		else
		{
			if(targetMapName1.isEmpty()==false&&targetMapName2.isEmpty()==false&&targetMapName3.isEmpty()==false){
				return 3;
			}
		}
		return 0;
	}
	public void setTargetMapDispalyName(String targetMapDispalyName) {
		this.targetMapDispalyName = targetMapDispalyName;
	}
	
	public static String getXinShouCityMap(int county){
		//昆仑，九州，玩法
		if(county == 1){
			return "kunhuagucheng";
		}else if(county == 2){
			return "shangzhouxianjing";
		}else if(county == 3){
			return "wanfayiji";
		}
		return "";
	}
	
	public static String getCountryName(int county){
		//昆仑，九州，玩法
		if(county == 1){
			return "昆仑";
		}else if(county == 2){
			return "九州";
		}else if(county == 3){
			return "万法";
		}
		return "中立";
	}
	
	public static String getBianGuanMap(int county){
		if(county == 1){
			return "shengtianzhidao";
		}else if(county == 2){
			return "jiuzhoutianlu";
		}else if(county == 3){
			return "fayuandadao";
		}
		return "";
	}
	
	public static int getCountry(String mapName){
		if(mapName.equals("shengtianzhidao") ||mapName.equals("kunhuagucheng") || mapName.equals("kunlunyaosai") || mapName.equals("linshuangcun") || mapName.equals("kunlunshengdian")){
			return 1;
		}else if(mapName.equals("jiuzhoutianlu") ||mapName.equals("shangzhouxianjing") || mapName.equals("jiuzhouyaosai") || mapName.equals("taoyuancun") || mapName.equals("jiuzhoutiancheng")){
			return 2;
		}else if(mapName.equals("fayuandadao") ||mapName.equals("wanfayiji") || mapName.equals("wanfayaosai") || mapName.equals("jiaoshacun") || mapName.equals("wanfazhiyuan")){
			return 3;
		}else if(mapName.equals("miemoshenyu")){
			return 0;
		}
		return -1;
	}
	
	public static String getYaoSaiMap(int county){
		if(county == 1){
			return "kunlunyaosai";
		}else if(county == 2){
			return "jiuzhouyaosai";
		}else if(county == 3){
			return "wanfayaosai";
		}
		return "";
	}
	
	public static String getXinShouCunMap(int county){
		//昆仑，九州，玩法
		if(county == 1){
			return "linshuangcun";
		}else if(county == 2){
			return "taoyuancun";
		}else if(county == 3){
			return "jiaoshacun";
		}
		return "";
	}
	
	public static int [] getFateXY(int county){
		if(county == 1){
			return new int[]{5200,1000};
		}else if(county == 2){
			return new int[]{2988,988};
		}else if(county == 3){
			return new int[]{6115,1172};
		}
		return new int[]{0,0};
	}
	
	public static int [] getMarriageXY(int county){
		if(county == 1){
			return new int[]{1124,4543};
		}else if(county == 2){
			return new int[]{448,2744};
		}else if(county == 3){
			return new int[]{2822,2899};
		}
		return new int[]{0,0};
	}
	
	public static String getMainCityMap(int county){
		if(county == 1){
			return "kunlunshengdian";
		}else if(county == 2){
			return "jiuzhoutiancheng";
		}else if(county == 3){
			return "wanfazhiyuan";
		}
		return "";
	}
	
	
	private String targetMapDispalyName1;
	private String targetMapName1;
	private int targetMapX1;
	private int targetMapY1;
	
	private String targetMapDispalyName2;
	private String targetMapName2;
	private int targetMapX2;
	private int targetMapY2;
	
	private String targetMapDispalyName3;
	private String targetMapName3;
	private int targetMapX3;
	private int targetMapY3;
	
	
	
	public String getTargetMapDispalyName1() {
		return targetMapDispalyName1;
	}

	public void setTargetMapDispalyName1(String targetMapDispalyName1) {
		this.targetMapDispalyName1 = targetMapDispalyName1;
	}

	public String getTargetMapName1() {
		return targetMapName1;
	}

	public void setTargetMapName1(String targetMapName1) {
		this.targetMapName1 = targetMapName1;
	}

	public int getTargetMapX1() {
		return targetMapX1;
	}

	public void setTargetMapX1(int targetMapX1) {
		this.targetMapX1 = targetMapX1;
	}

	public int getTargetMapY1() {
		return targetMapY1;
	}

	public void setTargetMapY1(int targetMapY1) {
		this.targetMapY1 = targetMapY1;
	}

	public String getTargetMapDispalyName2() {
		return targetMapDispalyName2;
	}

	public void setTargetMapDispalyName2(String targetMapDispalyName2) {
		this.targetMapDispalyName2 = targetMapDispalyName2;
	}

	public String getTargetMapName2() {
		return targetMapName2;
	}

	public void setTargetMapName2(String targetMapName2) {
		this.targetMapName2 = targetMapName2;
	}

	public int getTargetMapX2() {
		return targetMapX2;
	}

	public void setTargetMapX2(int targetMapX2) {
		this.targetMapX2 = targetMapX2;
	}

	public int getTargetMapY2() {
		return targetMapY2;
	}

	public void setTargetMapY2(int targetMapY2) {
		this.targetMapY2 = targetMapY2;
	}

	public String getTargetMapDispalyName3() {
		return targetMapDispalyName3;
	}

	public void setTargetMapDispalyName3(String targetMapDispalyName3) {
		this.targetMapDispalyName3 = targetMapDispalyName3;
	}

	public String getTargetMapName3() {
		return targetMapName3;
	}

	public void setTargetMapName3(String targetMapName3) {
		this.targetMapName3 = targetMapName3;
	}

	public int getTargetMapX3() {
		return targetMapX3;
	}

	public void setTargetMapX3(int targetMapX3) {
		this.targetMapX3 = targetMapX3;
	}

	public int getTargetMapY3() {
		return targetMapY3;
	}

	public void setTargetMapY3(int targetMapY3) {
		this.targetMapY3 = targetMapY3;
	}

	public int getHalfWidth() {
		return halfWidth;
	}

	public int getHalfHeight() {
		return halfHeight;
	}

	//设置特定国家传送点信息
	public void setCarrerTransportData(String targetMapName1,String targetMapDisplayName1, int targetMapX1, int targetMapY1,String targetMapName2,String targetMapDisplayName2, int targetMapX2, int targetMapY2,String targetMapName3,String targetMapDisplayName3, int targetMapX3, int targetMapY3){
		this.targetMapDispalyName1=targetMapDisplayName1;
		this.targetMapName1=targetMapName1;
		this.targetMapX1=targetMapX1;
		this.targetMapY1=targetMapY1;
		
		this.targetMapDispalyName2=targetMapDisplayName2;
		this.targetMapName2=targetMapName2;
		this.targetMapX2=targetMapX2;
		this.targetMapY2=targetMapY2;
		
		this.targetMapDispalyName3=targetMapDisplayName3;
		this.targetMapName3=targetMapName3;
		this.targetMapX3=targetMapX3;
		this.targetMapY3=targetMapY3;
	}
	
	public TransportData(int x, int y, int halfWidth, int halfHeight,
			String targetMapName,String targetMapDisplayName, int targetMapX, int targetMapY) {
		this.x = x;
		this.y = y;
		this.halfWidth = halfWidth;
		this.halfHeight = halfHeight;
		this.targetMapName = targetMapName;
		this.targetMapDispalyName = targetMapDisplayName;
		this.targetMapX = targetMapX;
		this.targetMapY = targetMapY;
	}
	public TransportData(int x, int y, int halfWidth, int halfHeight,
			String targetMapName, int targetMapX, int targetMapY) {
		this.x = x;
		this.y = y;
		this.halfWidth = halfWidth;
		this.halfHeight = halfHeight;
		this.targetMapName = targetMapName;
		this.targetMapDispalyName = targetMapName;
		this.targetMapX = targetMapX;
		this.targetMapY = targetMapY;
	}
	public int getTargetMapX() {
		return targetMapX;
	}

	public int getTargetMapY() {
		return targetMapY;
	}

	public boolean isTouched(int x, int y) {
		int dx = this.x - x;
		int dy = this.y - y;
		return -halfWidth <= dx && dx <= halfWidth && -halfHeight <= dy && dy <= halfHeight;
	}

	public String getTargetMapName() {
		return targetMapName;
	}

	public int getX() {
		return x;
	}

	public  int getY() {
		return y;
	}

	public void setTargetMapName(String targetMapName) {
		this.targetMapName = targetMapName;
	}
}
