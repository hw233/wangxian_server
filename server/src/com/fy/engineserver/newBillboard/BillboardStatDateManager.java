package com.fy.engineserver.newBillboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class BillboardStatDateManager {

	public static SimpleEntityManager<BillboardStatDate> em;
	
	public static SimpleEntityManager<BillboardInfo> em_info;
	
	private static BillboardStatDateManager instance = null;
	
	public static BillboardStatDateManager getInstance(){
		return instance;
	}
	
	public static List<ActivityBillboard> configs = new ArrayList<ActivityBillboard>();
	
	public LruMapCache mCache = new LruMapCache(32 * 1024 * 1024, 60 * 60 * 1000);
	
	static{
		Set<String> set_韩国活动 = new HashSet<String>();
		Set<String> notopen_韩国活动 = new HashSet<String>();
		ActivityBillboard activity_韩国活动 = new ActivityBillboard(TimeTool.formatter.varChar19.parse("2013-06-19 00:00:00"), TimeTool.formatter.varChar19.parse("2013-08-20 23:59:59"), new Platform[] {Platform.韩国}, notopen_韩国活动, set_韩国活动);
		configs.add(activity_韩国活动);
	}
	
	public void init(){
		
		instance = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(BillboardStatDate.class);
		em_info = SimpleEntityManagerFactory.getSimpleEntityManager(BillboardInfo.class);
		BillboardsManager.logger.error("[排行榜统计数据manager初始化成功]");
		ServiceStartRecord.startLog(this);
	}
	
	public void destroy(){
		em.destroy();
		em_info.destroy();
		BillboardsManager.logger.warn("[BillboardStatDateManager destory]");
	}
	
	/**
	 * 通过id得到
	 * @param id
	 * @return
	 */
	public synchronized BillboardStatDate getBillboardStatDate(long id) {
		BillboardStatDate date = (BillboardStatDate) mCache.get(id);
		if (date == null) {
			try {
				date = em.find(id);
				if(date != null){
					mCache.put(date.getId(), date);
					if(BillboardsManager.logger.isWarnEnabled()){
						BillboardsManager.logger.warn("[得到排行榜统计数据] ["+id+"]");
					}
				}else{
					date = new BillboardStatDate();
					date.setId(id);
					PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(id);
					if(info != null){
						if(info.getSex() == 1){
							date.setFlowersNum(0);
						}else{
							date.setSweetsNum(0);
						}
						date.setCountry(info.getCountry());
						mCache.put(date.getId(), date);
						em.save(date);
						BillboardsManager.logger.error("[获得排行榜统计数据没有] [新建统计数据] [Id:" + id + "]");
					}
				}
			} catch (Exception e) {
				BillboardsManager.logger.error("[获得排行榜统计数据异常] [Id:" + id + "]", e);
			}
		}
		return date;
	}
	
	public List<BillboardStatDate> queryDataByProperty(long[] ids,int top,String property){
		try {
			if(ids != null && ids.length > 0){
				StringBuffer sb = new StringBuffer();
				sb.append("id = "+ids[0]+" ");
				long id = 0;
				for(int i= 1;i<ids.length;i++){
					sb.append("OR ");
					sb.append("id = "+ids[i]+" ");
				}
				long[] idd = BillboardStatDateManager.em.queryIds(BillboardStatDate.class, sb.toString(), property +" desc",1,top);
				List<BillboardStatDate> list = new ArrayList<BillboardStatDate>();
				for(long id1 : idd){
					BillboardStatDate data = em.find(id1);
					if(data != null){
						list.add(data);
					}
				}
				BillboardsManager.logger.warn("[根据给定id查询前几名成功] ["+property+"] ["+idd.length+"]");
				return list;
			}
		} catch (Exception e) {
			BillboardsManager.logger.error("[根据给定id查询前几名异常] ["+property+"]",e);
		}
		return null;
	}
	
	public List<BillboardInfo> getInfoByKey(long starttime,long endtime){
		long now = System.currentTimeMillis();
		List<BillboardInfo> list = new ArrayList<BillboardInfo>();
		try {
			list = em_info.query(BillboardInfo.class, " starttime >=" +starttime+" AND endtiem <= "+endtime, "value desc", 1, 100);
			BillboardsManager.logger.warn("[通过key获得某小时的排行数据] [成功] [长度："+list.size()+"] [starttime:"+TimeTool.formatter.varChar23.format(starttime)+"] [endtime:"+TimeTool.formatter.varChar23.format(endtime)+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			BillboardsManager.logger.warn("[通过key获得某小时的排行数据] [异常] [长度："+list.size()+"] [starttime:"+TimeTool.formatter.varChar23.format(starttime)+"] [endtime:"+TimeTool.formatter.varChar23.format(endtime)+"]  [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public BillboardInfo getInfoByKey(String name,long pid){
		long now = System.currentTimeMillis();
		List<BillboardInfo> list = new ArrayList<BillboardInfo>();
		try {
			list = em_info.query(BillboardInfo.class, " keyname = '"+name+"' AND pid = "+pid,"value desc", 1, 100);
			BillboardsManager.logger.warn("[通过key-pid获得某小时的排行数据] [成功] [长度："+list.size()+"] [name:"+name+"] [pid:"+pid+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			BillboardsManager.logger.warn("[通过key-pid获得某小时的排行数据] [异常] [长度："+list.size()+"] [name:"+name+"] [pid:"+pid+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public void saveInfo(BillboardInfo info){
		try {
			long id = em_info.nextId();
			info.setId(id);
			em_info.save(info);
			BillboardsManager.logger.warn("[存储排行榜活动信息] [成功] ["+info+"]");
		} catch (Exception e) {
			e.printStackTrace();
			BillboardsManager.logger.warn("[存储排行榜活动信息] [异常] ["+info+"]",e);
		}
	}
	
	public static String getKeyName(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
		return sdf.format(System.currentTimeMillis());
	}
}

/**
 * 一些特殊排行榜活动
 * 
 *
 */
class ActivityBillboard{
	
	public ActivityBillboard(long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
	}
	
	public long startTime;
	
	public long endTime;
	
	public Platform[] platforms;
	
	public Set<String> notOpenServers;
	
	public Set<String> openServers;
	
	public boolean isEffective(){
		long now = SystemTime.currentTimeMillis();
		if(startTime > now || endTime < now){
			return false;
		}
		if (!PlatformManager.getInstance().isPlatformOf(platforms)) {
			return false;
		}
		GameConstants gc = GameConstants.getInstance();
		if (gc == null || notOpenServers.contains(gc.getServerName())) {
			return false;
		}
		if(openServers.contains(gc.getServerName())){
			return true;
		}
		/**开放的服务器为空，该平台都符合**/
		if(openServers.size()==0){
			return true;
		}
		return false;
	}
	
}
