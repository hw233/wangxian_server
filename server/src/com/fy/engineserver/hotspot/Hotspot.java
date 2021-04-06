package com.fy.engineserver.hotspot;

//热点基础数据，由excel载入
public class Hotspot {
	
	public static final int OPENTYPE_LEVEL = 0;			//开启条件类型  等级
	public static final int OPENTYPE_HOTSPOT = 1;		//开启条件类型  其他热点
	
	public static final int OVERTYPE_LEVEL = 0;			//完成条件类型  等级
	public static final int OVERTYPE_TASK = 1;			//完成条件类型  任务
	public static final int OVERTYPE_MAP = 2;			//完成条件类型  地图
	public static final int OVERTYPE_DATI = 3;			//完成条件类型  答题
	public static final int OVERTYPE_宠物升级 = 4;			//完成条件类型  宠物升级
	
	//id
	private int id;
	//在热点列表中的位置
	private int pos;
	//标题
	private String title;
	//图标
	private String iconName;
	//开启条件类型
	private int[] openType;
	//开启条件参数
	private String[] openValue;
	
	//完成条件类型
	private int overType;
	//完成条件参数
	private String overValue;
	
	//完整内容
	private String msg;
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getIconName() {
		return iconName;
	}
	public void setOpenType(int[] openType) {
		this.openType = openType;
	}
	public int[] getOpenType() {
		return openType;
	}
	public void setOpenValue(String[] openValue) {
		this.openValue = openValue;
	}
	public String[] getOpenValue() {
		return openValue;
	}
	public void setOverType(int overType) {
		this.overType = overType;
	}
	public int getOverType() {
		return overType;
	}
	public void setOverValue(String overValue) {
		this.overValue = overValue;
	}
	public String getOverValue() {
		return overValue;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getPos() {
		return pos;
	}
	
}
