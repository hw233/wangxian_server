package com.fy.engineserver.core.res;

import java.io.DataInputStream;

public class Appearance extends AbstractResource{	
	private String race ="";
	private String sex = "";
	
	/**
	 * 有哪些动作
	 */
	private String actions[] = new String[0] ;
	/**
	 * 有哪些方向
	 */
	private String directions[] = new String[0];
	/**
	 * 有哪些部件
	 */
	private String partTypes[] = new String[0];
	
	

	@Override
	public void load(DataInputStream is) throws Exception {

		version = is.readInt();
		race = is.readUTF();
		sex = is.readUTF();
		short numactions = is.readShort();	
		actions = new String[numactions];
		for( int i=0;i< actions.length;i++){
			actions[i] = is.readUTF() ;
		}
		short numdirection = is.readShort();
		directions = new String[numdirection];
		for( int i=0;i< directions.length;i++){
			directions[i] = is.readUTF() ;
		}
		
		short numpart = is.readShort() ;
		partTypes = new String[numpart];
 		for( int i=0;i< partTypes.length;i++){
			partTypes[i] =  is.readUTF();
		}
 		

	}
	
	public String getName(){
		return getName(race,sex);
	}
	/**
	部件索引方式：		部件				动画索引方式
	裸体				种族+性别			动作+方向+坐骑
	武器				名+种族+性别		动作+方向+坐骑
	身				名+种族+性别		动作+方向+坐骑
	肩				名+种族+性别		动作+方向+坐骑
	头				名+种族+性别		动作+方向+坐骑
	坐骑				名+种族+性别		动作+方向
	状态				“状态”+种族+性别	状态名
	光效				名				第一个动画
	技能后效			名				动作+方向
	其他				名				名
	部件和动画的索引支持通配符*，并且长度优先配置			
	*/
	
	private String[] getPartName(int partType,String partKeyName){
		if( partType == TYPE_STATUS){
			partKeyName = "状态";
		}
		switch( partType){
		case TYPE_BODY:return new String[]{ getName() };
		case TYPE_WEAPON:
		case TYPE_WEAPON_GX:
		case TYPE_CLOTH:
		case TYPE_SHOULDER:	
		case TYPE_HEAD:
		case TYPE_HORSE:
		case TYPE_STATUS:
			return new String[]{ partKeyName+"_"+getName(),partKeyName+"_"+race+"_x",partKeyName+"_x_"+sex,partKeyName+"_x_x"};
		case TYPE_LIGHT_EFFECT:	
		case TYPE_SKILL_EFFECT:
		case TYPE_BUILDING:
		case TYPE_UI:
		case TYPE_SHADE:	
		case TYPE_OTHER:
		default:
			return new String[]{ partKeyName };
		}		
	}
	/**
	 * 获取最合适的部件
	 * @param partType
	 * @param partKeyName
	 * @return
	 */
	public Part getBestFitPart(int partType,String partKeyName){
		ResourceManager rm = ResourceManager.getInstance();
		String partFullName[] = getPartName(partType, partKeyName);
		for( String s: partFullName){
			String key = ResourceManager.partHead+s+ResourceManager.partTail;
			if( rm.parts.containsKey(key)){
				return (Part)rm.parts.get(key);
			}
		}
		StringBuffer sb = new StringBuffer();
		for( String s: partFullName){
			sb.append("["+s+"]");
		}
		return null;
	}
	

	
	
	public String getRace() {
		return race;
	}
	
	public void setRace(String race) {
		this.race = race;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return sex;
	}

	public String[] getActions() {
		return actions;
	}
	
	public String[] getDirections() {
		return directions;
	}
	
	public String[] getPartTypes() {
		return partTypes;
	}
	
	public int[] getPartTypeInt(){
		int data[] = new int[partTypes.length];
		for( int i=0;i< partTypes.length;i++){
			data[i] = getPartTypeInt(partTypes[i]);		
		}
		return data;
	}
	
	
	
	
	public static byte getPartTypeInt(String s){
		for( byte j = 0;j< Constants.partTypes.length;j++ ){
			if( Constants.partTypes[j].equals(s)){
				return j;
			}
		}
		return -1;
	}
	public static String getName(String race,String sex){
		if( sex.length()<1 || race == "diwu") return race;
		
		return race+"_"+sex;
	}
	
	
	
	
}
