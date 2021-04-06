package com.fy.gamegateway.gmaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class GmSystemNoticeManager implements Runnable{
	
	public List<GmSystemNotice> notices;
	public List<GmSystemNotice> rmnotices = new ArrayList<GmSystemNotice>();;
	
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
		List<GmSystemNotice> list2 = new ArrayList<GmSystemNotice>();
 		try {
			list = em.query(GmSystemNotice.class, " state = 0 ", "creattime DESC", 1, 1000);
			list2 = em.query(GmSystemNotice.class, " state = 2 ", "creattime DESC", 1, 1000);
			list.addAll(list2);
			logger.warn("[获得有效的公告配置] [成功] [数量："+list.size()+"] [list2:"+list2.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
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
				logger.warn("[设置每天新系统公告] [成功] [时间区域："+(notice.getDays()!=null?Arrays.toString(notice.getDays()):"") +"] [设置人："+notice.getRecorder()+"] [公告总数:"+notices.size()+"]  [开始时间"+notice.getLimitBeginTime()+"] [结束时间"+notice.getLimitEndTime()+"] [轮播间隔："+notice.getMessLength()+"]" + 
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
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public void run() {
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
						rmnotices.add(gsn);
						continue;
					}
					try{
						if(gsn.getState()==0&&gsn.needSendNotice(now)) {
							gsn.setState(2); 
							logger.warn("[准备发送，等待发送间隔] [是否发送:"+(now - gsn.getLastSendTime() >= gsn.getMessLength())+"]");
							new Thread(this.new SendNoticeMess(gsn),"一个公告设置").start();
						}
					}catch(Exception e){
						logger.warn("[in run...] [是否发送："+gsn.needSendNotice(now)+"] [是否在有效期："+gsn.isvalid(now)+"] [设置人："+gsn.getRecorder()+"] [notices:"+notices.size()+"] [异常]",e);
					}
				}
				for (GmSystemNotice gsn : rmnotices) {
					notices.remove(gsn);
					gsn.setState(1);
					logger.warn("[无效删除] [id:"+gsn.getId()+"] [记录人:"+gsn.getRecorder()+"] [记录时间:"+sdf.format(gsn.getCreattime())+"] [notices:"+notices.size()+"] [rmnotices:"+rmnotices.size()+"]");
					continue;
				}
				rmnotices.clear();
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
			while(true){
				if(runNotice== null || runNotice.getState()==1){
					logger.warn("[公告结束] [无效公告] ["+runNotice+"]");
					break;
				}
				if(System.currentTimeMillis() - runNotice.getLastSendTime() >= runNotice.getMessLength()){
					runNotice.setLastSendTime(System.currentTimeMillis());
				}
				long slTime = 2000;
				if(runNotice.getSentsystemnotice()!=null && runNotice.getSentsystemnotice().equals("sendsystem")){
					for(int i=0;i<runNotice.getCons().length;i++){
						runNotice.sendNotice(runNotice.getCons()[i],0);
						runNotice.sendNotice(runNotice.getCons()[i],1);
						if(runNotice.getMessesLength()>0){
							slTime = runNotice.getMessesLength();
						}else if(runNotice.getMessLength() > 0){
							slTime = runNotice.getMessLength();
						}
						try {
							Thread.sleep(slTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}else{
					for(int i=0;i<runNotice.getCons().length;i++){
						runNotice.sendNotice(runNotice.getCons()[i],0);
						if(runNotice.getMessesLength()>0){
							slTime = runNotice.getMessesLength();
						}else if(runNotice.getMessLength() > 0){
							slTime = runNotice.getMessLength();
						}
						try {
							Thread.sleep(slTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
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
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}
