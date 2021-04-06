package com.fy.boss.gm.korea;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.directwebremoting.util.Logger;

import com.fy.boss.gm.korea.KoreaTranslateManager;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 韩国
 * @author wtx
 *
 */
public class KoreaTranslateManager {
	
//	public static final int 登录页面 = 0;
//	public static final int 内容页面 = 1;
//	public static final int 服务器列表 = 2;
//	public static final int 功能列表 = 3;
//	public static final int 修改密码客户端信息 = 4;
//	public static final int 通行证查询 = 5;
//	public static final int 扭转已处理查询 = 6;
//	public static final int 处理问题 = 7;
//	public static final int 综合查询问题 = 8;
//	public static final int 提交新问题 = 9;
//	public static final int GM问题反馈 = 10;
//	public static final int 邮件系统_排队 = 11;
//	public static final int 邮件系统_设置 = 12;
//	public static final int 组长设置GM编号 = 13;
//	public static final int 邮件系统_统计_查询 = 14;
//	public static final int 邮件系统_统计 = 15;
//	public static final int 处理问题_vip = 16;
//	public static final int 批量发送活动公告 = 17;
//	public static final int 批量发送系统公告 = 18;
//	public static final int 批量发送登录公告 = 19;
//	public static final int 定时发送公告 = 20;
//	public static final int 充值记录查询 = 22;
	
	public static Logger logger = Logger.getLogger(KoreaTranslateManager.class);
	
	private List<Integer> allSheetName = new ArrayList<Integer>();
	
	/**china and korea compare**/
	private Map<String, String> translateMap = new ConcurrentHashMap<String, String>();
	
	private String filename;
	
	private static KoreaTranslateManager self;
	
	public static KoreaTranslateManager getInstance(){
		return self;
	}
	
	public void init(){
		self = this;
		for(int i=0;i<2;i++){
			allSheetName.add(new Integer(i));
		}
		initMap();
		System.out.println("KoreaTranslateManager 初始化成功......");
	}
	
	private void initMap(){
		try{
			File file = new File(getFilename());
			Workbook book = Workbook.getWorkbook(file);
			for(int i=0;i<allSheetName.size();i++){
				Sheet sheet = book.getSheet(i);
				int allRows = sheet.getRows();
				for(int j=0;j<allRows;j++){
					Cell[] cells = sheet.getRow(j);
					if(cells!=null){
						int index = 0;
						translateMap.put(cells[index++].getContents(), cells[index].getContents());
					}else{
						continue;
					}
				}
			}
		}catch(Throwable e){
			e.printStackTrace();
			System.out.println(e);
		}
	}

	public List<Integer> getAllSheetName() {
		return allSheetName;
	}

	public void setAllSheetName(List<Integer> allSheetName) {
		this.allSheetName = allSheetName;
	}

	public Map<String, String> getTranslateMap() {
		return translateMap;
	}

	public void setTranslateMap(Map<String, String> translateMap) {
		this.translateMap = translateMap;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	
}
