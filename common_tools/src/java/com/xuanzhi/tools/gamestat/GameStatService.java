package com.xuanzhi.tools.gamestat;

import java.util.ArrayList;
import java.util.Date;

import com.xuanzhi.tools.text.DateUtil;
import java.io.*;

/**
 * 针对某个服务器存储"玩家能力","其他各种数据"数据
 * 
 * 以文件的形式存储数据
 * dataRootPath
 *        /yyyymmdd
 *             /functionKey_userType.stat
 *             
 *
 */
public class GameStatService {

	public static final int 非R = 0;
	public static final int 小R = 1;
	public static final int 中R = 2;
	public static final int 大R = 3;
	public static final int 超级大R = 4;
	public static final int 全部用户 = 5;
	
	public static String userTypeNames[] = new String[]{"非R","小R","中R","大R","超级大R","全部用户"};
	
	String dataRootPath ;
	

	public void setDataRootPath(String path){
		dataRootPath = path;
	}
	
	static GameStatService self;
	
	public static GameStatService getInstance(){
		return self;
	}
	
	public void init(){
		//检查100天前的数据目录，删除这些目录及目录中的数据文件。
		
		self = this;
	}
	
	public static class Data{
		String functionKey;
		String day;
		int userType;
		Object[] datas;
	}
	
	ArrayList<Data> cacheData = new ArrayList<Data> ();
	/**
	 * 获得某个功能的数据
	 * 
	 * 要求 functionKey 必须是英文且可以作为文件目录，一般就用数据类的类名
	 *     day的格式为 yyyy-mm-dd
	 *     userType String userTypeNames[] = new String[]{"非R","小R","中R","大R","超级大R"};
	 * @param functionKey
	 * @param day
	 * @param userType
	 * @return
	 */
	public Object[] loadData(String functionKey,String day,int userType) throws Exception{
		
		if(dataRootPath == null) throw new Exception("dataRootPath not config.");
		
		for(Data d : cacheData){
			if(d.functionKey.equals(functionKey) && d.day.equals(day) && d.userType == userType){
				return d.datas;
			}
		}
		Date date = DateUtil.parseDate(day, "yyyy-MM-dd");
		String dayStr = DateUtil.formatDate(date, "yyyyMMdd");
		
		String fileName = dataRootPath + "/" + dayStr + "/" + functionKey+"_"+userType+".stat";
		File file = new File(fileName);
		if(file.exists() == false) return new Object[0];
		
		java.io.ObjectInputStream input = new java.io.ObjectInputStream(new FileInputStream(file));
		Object[] datas = (Object[])input.readObject();
		input.close();
		
		if(file.length() > 102400){
			Data d = new Data();
			d.datas = datas;
			d.functionKey = functionKey;
			d.day = day;
			d.userType = userType;
			
			cacheData.add(d);
			if(cacheData.size() > 32){
				cacheData.remove(0);
			}
		}
		return datas;
	}
	
	/**
	 * 获得某个功能的数据
	 * 
	 * 要求 functionKey 必须是英文且可以作为文件目录
	 *     day的格式为 yyyy-mm-dd
	 *     userType String userTypeNames[] = new String[]{"非R","小R","中R","大R","超级大R"};
	 * @param functionKey
	 * @param day
	 * @param userType
	 * @return
	 */
	public void saveData(String functionKey,String day,int userType,Object[] datas) throws Exception{
		if(dataRootPath == null) throw new Exception("dataRootPath not config.");
		for(Data d : cacheData){
			if(d.functionKey.equals(functionKey) && d.day.equals(day) && d.userType == userType){
				d.datas = datas;
			}
		}
		Date date = DateUtil.parseDate(day, "yyyy-MM-dd");
		String dayStr = DateUtil.formatDate(date, "yyyyMMdd");
		
		String fileName = dataRootPath + "/" + dayStr + "/" + functionKey+"_"+userType+".stat";
		File dir = new File( dataRootPath + "/" + dayStr + "/");
		dir.mkdirs();
		
		File file = new File(fileName);
		
		java.io.ObjectOutputStream output = new java.io.ObjectOutputStream(new FileOutputStream(file));
		output.writeObject(datas);
		output.close();
	}
}
