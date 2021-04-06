package com.fy.boss.gm.npcnotice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.gm.newfeedback.FeedbackState;
import com.fy.boss.gm.npcnotice.BoardItem;
import com.fy.boss.gm.npcnotice.NpcNoticeManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.DateUtil;

/**
 * NPC公告
 * @author wtx
 *
 */
public class NpcNoticeManager {

	public static Logger log = Logger.getLogger(NpcNoticeManager.class);
	
	private static NpcNoticeManager self;
	
	public SimpleEntityManager<BoardItem> sem;
	
	public static NpcNoticeManager getInstance(){
		return self;
	}
	
	public void init(){
		self = this;
		try{
			sem = SimpleEntityManagerFactory.getSimpleEntityManager(BoardItem.class);
			System.out.println("[NpcNoticeManager] [NPC公告] [初始化成功]");
		}catch(Exception e){
			System.out.println("[NpcNoticeManager ] [NPC公告] [初始化异常]"+e);
		}
	}

	public boolean addNotice(BoardItem item){
		long now = System.currentTimeMillis();
		try {
			long id = sem.nextId();
			item.setId(id);
			sem.save(item);
			log.warn("[NPC公告] [添加新公告] [成功] ["+item+"] [耗时："+(System.currentTimeMillis()-now)+"]");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("[NPC公告] [添加新公告] [异常] ["+item+"] [耗时："+(System.currentTimeMillis()-now)+"]",e);
			return false;
		}
	}
	
	public boolean updateNotice(BoardItem item){
		long now = System.currentTimeMillis();
		try {
			sem.flush(item);
			log.warn("[NPC公告] [更新公告] [成功] ["+item+"] [耗时："+(System.currentTimeMillis()-now)+"]");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("[NPC公告] [更新公告] [异常] ["+item+"] [耗时："+(System.currentTimeMillis()-now)+"]",e);
			return false;
		}
	}
	
	/**
	 * 获得某个服务器有效的公告
	 * @param servername
	 * @return
	 */
	public List<BoardItem> getNotices(String servername){
		long now = System.currentTimeMillis();
		List<BoardItem> list = new ArrayList<BoardItem>();
		try {
			String querySql = "servername=? and beginShowTime<=to_date(?,'yyyy-mm-dd hh24:mi:ss') and endShowTime>=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			if("mysql".equalsIgnoreCase(SimpleEntityManagerFactory.getDbType())){
				querySql = "servername=? and beginShowTime<=str_to_date(?,'%Y-%m-%d %H:%i:%S') and endShowTime>=str_to_date(?,'%Y-%m-%d %H:%i:%S')";
			}
			list = sem.query(BoardItem.class, querySql, new Object[]{servername,DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")},"beginShowTime desc", 1, 3000);
			log.warn("[通过服务器获得公告记录] [OK] [服务器："+servername+"] [数量:"+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("[通过服务器获得公告记录] [异常] [服务器："+servername+"] [数量:"+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public List<BoardItem> getNotices(Date starttime,Date endtime){
		long now = System.currentTimeMillis();
		List<BoardItem> list = new ArrayList<BoardItem>();
		try {
			String querySql = "beginShowTime<=to_date(?,'yyyy-mm-dd hh24:mi:ss') and endShowTime>=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
			if("mysql".equalsIgnoreCase(SimpleEntityManagerFactory.getDbType())){
				querySql = "beginShowTime<=str_to_date(?,'%Y-%m-%d %H:%i:%S') and endShowTime>=str_to_date(?,'%Y-%m-%d %H:%i:%S')";
			}
			list = sem.query(BoardItem.class, querySql, new Object[]{DateUtil.formatDate(starttime,"yyyy-MM-dd HH:mm:ss"),DateUtil.formatDate(endtime,"yyyy-MM-dd HH:mm:ss")},"beginShowTime desc", 1, 3000);
			log.warn("[通过开始结束获得公告记录] [OK] [数量:"+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("[通过开始结束获得公告记录] [异常] [数量:"+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public BoardItem getNoticesById(long id){
		long now = System.currentTimeMillis();
		List<BoardItem> list = new ArrayList<BoardItem>();
		try {
			list  = sem.query(BoardItem.class, " id = "+id, "beginShowTime desc", 1, 100);
			log.warn("[通过id获得公告记录] [成功] [id："+id+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("[通过id获得公告记录] [异常] [id："+id+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
}
