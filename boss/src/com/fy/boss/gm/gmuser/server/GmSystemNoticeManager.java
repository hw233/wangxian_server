package com.fy.boss.gm.gmuser.server;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;

import com.fy.boss.gm.gmuser.GmSystemNotice;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class GmSystemNoticeManager implements Runnable{
	
	public List<GmSystemNotice> notices;
	
	public SimpleEntityManager<GmSystemNotice> em;
	
	public static Logger logger = Logger.getLogger(GmSystemNoticeManager.class);

	private static GmSystemNoticeManager self;
	
	public Thread thread;
	
	public boolean start = true;
	
	public static String uname = "wangtianxin";
	
	public static String nped = "123321";
	
	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public void init() throws Exception{
		long now = System.currentTimeMillis();
		self = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(GmSystemNotice.class);
		notices = new ArrayList<GmSystemNotice>();
		thread = new Thread(this, "系统定时发公告");
		notices = getNoticesConfig();
		thread.start();
		System.out.println("GmSystemNoticeManager初始化成功,---notices.size="+notices.size()+"---耗时:"+(System.currentTimeMillis()-now));
	}
	
	/**
	 * 获得有效的公告配置信息
	 * @return
	 */
	public List<GmSystemNotice> getNoticesConfig(){
		long now = System.currentTimeMillis();
		List<GmSystemNotice> list = new ArrayList<GmSystemNotice>();
 		try {
			list = em.query(GmSystemNotice.class, " state != 1 ", "creattime DESC", 1, 1000);
			logger.warn("[获得有效的公告配置] [成功] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得有效的公告配置] [异常] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
 		return list;
	}
	
	
	public GmSystemNotice getNoticesConfig(long id){
		long now = System.currentTimeMillis();
		List<GmSystemNotice> list = new ArrayList<GmSystemNotice>();
 		try {
			list = em.query(GmSystemNotice.class, " id = "+id, " lastSendTime DESC", 1, 1000);
			logger.warn("[通过id获得有效的公告配置] [成功] [id:"+id+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过id获得有效的公告配置] [异常] [id:"+id+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
 		if(list.size()>0){
 			return list.get(0);
 		}
 		return null;
	}
	
	public boolean addConfig(GmSystemNotice notice){
		long now = System.currentTimeMillis();
		try {
			long id = em.nextId();
			notice.setId(id);
			em.save(notice);
			notices.add(notice);
			try{
				logger.warn("[设置每天新系统公告] [成功] [时间区域："+notice.getDays() +"] [设置人："+notice.getRecorder()+"] [noticeslength:"+notices.size()+"]  [开始时间"+notice.getLimitBeginTime()+"] [结束时间"+notice.getLimitEndTime()+"] [轮播间隔："+notice.getMessLength()+"]" + 
						"[消息间隔："+notice.getMessesLength()+"]" + "[内容："+Arrays.toString(notice.getCons())+"] 【耗时："+(System.currentTimeMillis()-now)+"ms】");
				return true;
			}catch(Exception e){
				logger.warn("[addConfig] [异常]",e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[设置新系统公告] [异常] [设置人："+notice.getRecorder()+"] [间隔："+notice.getMessLength()+"]" +"[内容："+Arrays.toString(notice.getCons())+"]",e);
		}
		return false;
	}

	@Override
	public void run() {
		int count = 0;
		while(start){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (notices!=null && notices.size()>0) {
				long now = System.currentTimeMillis();
				for (GmSystemNotice gsn : notices) {
					if(!gsn.isvalid(now)){
						logger.warn("【无效删除】【设置人："+gsn.getRecorder()+"】");
						gsn.setState(1); 
					}
					try{
						if(gsn.getState()==0&&gsn.needSendNotice(now)) {
							logger.warn("[threadnum:"+count+"] [gsn.getLastSendTime():"+gsn.getLastSendTime()+"]");
							if(now - gsn.getLastSendTime() >= gsn.getMessLength()){
								new Thread(this.new SendNoticeMess(gsn),"一个公告设置").start();
								gsn.setLastSendTime(System.currentTimeMillis());
							}
						}
					}catch(Exception e){
						logger.warn("[in run...] [是否发送："+gsn.needSendNotice(now)+"] [是否在有效期："+gsn.isvalid(now)+"] [设置人："+gsn.getRecorder()+"] [notices:"+notices.size()+"] [异常]",e);
					}
					
				}
			}
		}
		
	}
	
	class SendNoticeMess implements Runnable{
		
		public  GmSystemNotice runNotice;
		
		public SendNoticeMess(GmSystemNotice notice){
			this.runNotice = notice;
		}
		@Override
		public void run() {
			if(runNotice.getSentsystemnotice()!=null && runNotice.getSentsystemnotice().equals("sendsystem")){
				for(int i=0;i<runNotice.getCons().length;i++){
					runNotice.sendNotice(runNotice.getCons()[i],0);
					runNotice.sendNotice(runNotice.getCons()[i],1);
					if(runNotice.getMessesLength()>0){
						try {
							Thread.sleep(runNotice.getMessesLength());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else{
						continue;
					}
				}
			}else{
				for(int i=0;i<runNotice.getCons().length;i++){
					runNotice.sendNotice(runNotice.getCons()[i],0);
					if(runNotice.getMessesLength()>0){
						try {
							Thread.sleep(runNotice.getMessesLength());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else{
						continue;
					}
				}
			}
			
		}
		public GmSystemNotice getRunNotice() {
			return runNotice;
		}
		public void setRunNotice(GmSystemNotice runNotice) {
			this.runNotice = runNotice;
		}
		
	}
	
	public static long 获得毫秒数(String time){
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long times = 0;
		try {
			times =  dateformat.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return times;
	}
	
	public void destroy () {
		if (em != null)  {
			em.destroy();
		}
	}
	
	public List<GmSystemNotice> getNotices() {
		return notices;
	}

	public void setNotices(List<GmSystemNotice> notices) {
		this.notices = notices;
	}

	public static GmSystemNoticeManager getInstance(){
		return self;
	}
	
	public String filename;
	
	public Map<String, List<String>> getMaps(){
		File file = new File(filename);
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			//工作簿--工作表--单元格
			Workbook work = Workbook.getWorkbook(file);
			Sheet sheet = work.getSheet(0);
			int rows = sheet.getRows();
			int cols = sheet.getColumns();
			List<String> list = null;
			String key = "";
			for(int i=0;i<rows;i++){
				list = new ArrayList<String>();
				for(int j=0;j<cols;j++){
					if(j==0){
						/**getCell(a,b)a,b横坐标，纵坐标**/
						key = sheet.getCell(j, i).getContents();
					}else{
						if(!"".equals(sheet.getCell(j, i).getContents().trim())){
							list.add(sheet.getCell(j, i).getContents().trim());
						}
					}
				}
				map.put(key, list);
			}
			work.close();
		} catch (BiffException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
		return map;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}
