package com.fy.boss.gm.newfeedback.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.gm.newfeedback.FeedbackPlayerState;
import com.fy.boss.gm.newfeedback.FeedbackState;
import com.fy.boss.gm.newfeedback.GmZanliState;
import com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class NewFeedbackStateManager {
	
	public static Logger logger = Logger.getLogger(NewFeedbackStateManager.class);
	
	private static NewFeedbackStateManager self;
	
	public SimpleEntityManager<FeedbackState> sem;
	
	public SimpleEntityManager<FeedbackPlayerState> psem;
	
	public SimpleEntityManager<GmZanliState> zlsem;
	
	public List<FeedbackPlayerState> pstate = new ArrayList<FeedbackPlayerState>();
	
	public List<FeedbackState> states = new ArrayList<FeedbackState>();
	
	public static NewFeedbackStateManager getInstance(){
		return self;
	}
	
	public void init(){
		long now = System.currentTimeMillis();
		self = this;
		sem = SimpleEntityManagerFactory.getSimpleEntityManager(FeedbackState.class);
		psem = SimpleEntityManagerFactory.getSimpleEntityManager(FeedbackPlayerState.class);
		zlsem = SimpleEntityManagerFactory.getSimpleEntityManager(GmZanliState.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String recordid = sdf.format(new Date());
		pstate = getPlayerStateByRecordId(recordid);
		states = getFeedbacks(recordid);
		System.out.println("[NewFeedbackStateManager] [recordid:"+recordid+"] [pstate:"+pstate.size()+"] [states:"+states.size()+"] [初始化] [成功] [耗时："+(System.currentTimeMillis()-now)+"ms] ");
	}
	
	/**
	 * 保存暂停信息
	 * @throws Exception 
	 */
	public void saveZanliInfo(GmZanliState bean) throws Exception{
		long id = zlsem.nextId();
		if(bean.getId() == 0){
			bean.setId(id);
		}
		zlsem.save(bean);
	}
	/**
	 * 获取暂离记录
	 * @param gmNum
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception 
	 */
	public List<GmZanliState> getZanli(String gmNum,long begin, long end,int start, int length) throws Exception{
		List<GmZanliState> list = new ArrayList<GmZanliState>();
		StringBuilder sb = new StringBuilder("");
		if(StringUtils.isNotBlank(gmNum)){
			sb.append(" and gmnum = '"+gmNum+"' ");
		}
		if(begin != 0 ){
			sb.append(" and zanlibegintime >= "+begin);
		}
		if(end != 0 ){
			sb.append(" and zanlibegintime <= "+end);
		}
		String where = "";
		if(sb.length()>0){
			where = sb.substring(5);
		}
		logger.info("********" + where);
		list = zlsem.query(GmZanliState.class, where, "zanlibegintime desc", start, length);
		
		return list;
	}
	
	public List<FeedbackPlayerState> getPlayerStateByRecordId(String recordid){
		long now = System.currentTimeMillis();
		List<FeedbackPlayerState> list = new ArrayList<FeedbackPlayerState>();
		try {
			list = psem.query(FeedbackPlayerState.class, " recordid = '"+recordid+"'", "recordtime desc", 1, 1000);
			logger.warn("[获得一天的记录] [OK] [天："+recordid+"] [数量:"+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得一天的记录] [异常] [天："+recordid+"] [数量:"+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public boolean isAddNewPlayerState(String recordid){
		if(pstate.size()>0){
			for(FeedbackPlayerState state:pstate){
				if(recordid.equals(state.getRecordid())){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean addPlayerState(FeedbackPlayerState st){
		long now = System.currentTimeMillis();
		try {
			long id = psem.nextId();
			st.setId(id);
			if(!pstate.contains(st)){
				pstate.add(st);
			}
			psem.save(st);
			logger.warn("[添加新的玩家统计] [OK] [id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[添加新的玩家统计] [异常] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return false;
	}
	
	//-------------------------------------------------------------
	public boolean addNewState(FeedbackState fb){
		long now = System.currentTimeMillis();
		fb.setRecordTime(now);
		try {
			long id = sem.nextId();
			fb.setId(id);
			if(!states.contains(fb)){
				states.add(fb);
			}
			sem.save(fb);
			logger.warn("[反馈统计] [新消息-保存] [成功] [id:"+id+"] [GM："+fb.getGmnum()+"] [玩家帐号："+fb.getUsername()+"] [角色名："+fb.getPlayername()+"] [标题："+fb.getTitle()+"] [服务器："+fb.getServername()+"] [耗时："+(System.currentTimeMillis()-now)+"]ms");
			return true;	
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[反馈统计] [新消息-保存] [异常] [[GM："+fb.getGmnum()+"] [玩家帐号："+fb.getUsername()+"] [角色名："+fb.getPlayername()+"] [标题："+fb.getTitle()+"] [服务器："+fb.getServername()+"] [耗时："+(System.currentTimeMillis()-now)+"]ms",e);
		}
		return false;
	}
	
	public boolean isaddNewState(String recordid,String gmname){
		if(states.size()>0){
			for(FeedbackState state:states){
				if(recordid.equals(state.getStateid()) && gmname.equals(state.getGmnum())){
					return false;
				}
			}
		}
		return true;
	}
	
	//获得一天的记录
	public List<FeedbackState> getFeedbacks(String stateid){
		long now = System.currentTimeMillis();
		List<FeedbackState> list = new ArrayList<FeedbackState>();
		try {
			list  = sem.query(FeedbackState.class, " stateid = '"+stateid+"'", "recordTime desc", 1, 1000);
			logger.warn("[获得改天所有GM的统计] [成功] [stateid："+stateid+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得改天所有GM的统计] [异常] [stateid："+stateid+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	//通过时间段和GM编号获得统计
	public List<FeedbackState> getStatesByStateidAndGmNum(String starttime,String endtime,String gmnum){
		long now = System.currentTimeMillis();
		List<FeedbackState> list = new ArrayList<FeedbackState>();
		try {
			list = sem.query(FeedbackState.class, " stateid >= '"+starttime+"' AND stateid <= '"+endtime+"' AND gmnum = '"+gmnum+"'", "recordTime desc", 1, 1000);
			logger.warn("[通过时间段和GM编号获得记录] [成功] [开始时间："+starttime+"] [结束时间："+endtime+"] [GM编号："+gmnum+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过时间段和GM编号获得记录] [异常] [开始时间："+starttime+"] [结束时间："+endtime+"] [GM编号："+gmnum+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	//通过时间段和GM编号获得统计
	public List<FeedbackState> getStatesByStateid(String starttime,String endtime){
		long now = System.currentTimeMillis();
		List<FeedbackState> list = new ArrayList<FeedbackState>();
		try {
			list = sem.query(FeedbackState.class, " stateid >= '"+starttime+"' AND stateid <= '"+endtime+"' ", "recordTime desc", 1, 10000);
			logger.warn("[通过时间段和GM编号获得记录] [成功] [开始时间："+starttime+"] [结束时间："+endtime+"]  [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过时间段和GM编号获得记录] [异常] [开始时间："+starttime+"] [结束时间："+endtime+"]  [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public List<FeedbackState> getStatesByStateidAndUsername(String starttime,String endtime,String username){
		long now = System.currentTimeMillis();
		List<FeedbackState> list = new ArrayList<FeedbackState>();
		try {
			list = sem.query(FeedbackState.class, " stateid >= '"+starttime+"' AND stateid <= '"+endtime+"' AND username = '"+username+"'", "recordTime desc", 1, 1000);
			logger.warn("[通过时间段和玩家帐号获得记录] [成功] [开始时间："+starttime+"] [结束时间："+endtime+"] [玩家帐号："+username+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过时间段和玩家帐号获得记录] [异常] [开始时间："+starttime+"] [结束时间："+endtime+"] [玩家帐号："+username+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	//--------------------------------------------------------------------------------
	
	
	//后面为统计优化前
	public long 获得统计数(String gmnum,long starttime,long endtime,String typename){
		long now = System.currentTimeMillis();
		List<FeedbackState> list = new ArrayList<FeedbackState>();
		try {
			list  = sem.query(FeedbackState.class, " gmnum = '"+gmnum+"' AND recordTime >="+starttime+" AND recordTime <="+endtime+" AND "+typename+" > 0", "recordTime desc", 1, 5000);
			logger.warn("[反馈统计] [获得统计数] ["+typename+"] [成功] [GM编号："+gmnum+"] [开始时间："+starttime+"] [结束时间："+endtime+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[反馈统计] [获得统计数] ["+typename+"] [异常] [GM编号："+gmnum+"] [开始时间："+starttime+"] [结束时间："+endtime+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list.size();
	}
	
	public int 获得评价数(String gmnum,long starttime,long endtime,String typename,int select){
		long now = System.currentTimeMillis();
		List<FeedbackState> list = new ArrayList<FeedbackState>();
		try {
			list  = sem.query(FeedbackState.class, " gmnum = '"+gmnum+"' AND recordTime >="+starttime+" AND recordTime <="+endtime+" AND "+typename+" ="+select, "recordTime desc", 1, 5000);
			logger.warn("[反馈统计] [获得评价数] ["+typename+"] ["+select+"] [成功] [GM编号："+gmnum+"] [开始时间："+starttime+"] [结束时间："+endtime+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[反馈统计] [获得评价数] ["+typename+"] ["+select+"] [异常] [GM编号："+gmnum+"] [开始时间："+starttime+"] [结束时间："+endtime+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list.size();
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public String 获得开始时间(String gmnum,long starttime,long endtime){
		long now = System.currentTimeMillis();
		List<FeedbackState> list = new ArrayList<FeedbackState>();
		try {
			list  = sem.query(FeedbackState.class, " gmnum = '"+gmnum+"' AND recordTime >="+starttime+" AND recordTime <="+endtime+" AND startWorkTime > 0", "recordTime desc", 1, 5000);
			logger.warn("[反馈统计] [获得开始时间] [成功] [GM编号："+gmnum+"] [开始时间："+starttime+"] [结束时间："+endtime+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[反馈统计] [获得开始时间] [异常] [GM编号："+gmnum+"] [开始时间："+starttime+"] [结束时间："+endtime+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		StringBuffer sb = new StringBuffer();
		if(list.size()>0){
			for(FeedbackState fb:list){
				sb = sb.append(sdf.format(fb.getStartWorkTime())+"<br>");
			}
		}
		return sb.toString();
	}
	
	public String 获得注销时间(String gmnum,long starttime,long endtime){
		long now = System.currentTimeMillis();
		List<FeedbackState> list = new ArrayList<FeedbackState>();
		try {
			list  = sem.query(FeedbackState.class, " gmnum = '"+gmnum+"' AND recordTime >="+starttime+" AND recordTime <="+endtime+" AND zhuxiaoTime > 0", "recordTime desc", 1, 5000);
			logger.warn("[反馈统计] [获得注销时间] [成功] [GM编号："+gmnum+"] [开始时间："+starttime+"] [结束时间："+endtime+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[反馈统计] [获得注销时间] [异常] [GM编号："+gmnum+"] [开始时间："+starttime+"] [结束时间："+endtime+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		StringBuffer sb = new StringBuffer();
		if(list.size()>0){
			
			for(FeedbackState fb:list){
				sb = sb.append(sdf.format(fb.getZhuxiaoTime()) +"<br><hr>");
			}
		}
		return sb.toString();
	}
	
	public int 获得交互条数(String gmnum,long starttime,long endtime){
		long now = System.currentTimeMillis();
		List<FeedbackState> list = new ArrayList<FeedbackState>();
		try {
			list  = sem.query(FeedbackState.class, " gmnum = '"+gmnum+"' AND recordTime >="+starttime+" AND recordTime <="+endtime+" AND talknum > 0", "recordTime desc", 1, 5000);
			logger.warn("[反馈统计] [获得交互条数] [成功] [GM编号："+gmnum+"] [开始时间："+starttime+"] [结束时间："+endtime+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[反馈统计] [获得交互条数] [异常] [GM编号："+gmnum+"] [开始时间："+starttime+"] [结束时间："+endtime+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		int talknum = 0;
		if(list.size()>0){
			for(FeedbackState fb : list){
				if(fb.getTalknum()>0){
					talknum = talknum + fb.getTalknum();
				}
			}
		}
		return talknum;
	}

	public List<FeedbackPlayerState> getPstate() {
		return pstate;
	}

	public void setPstate(List<FeedbackPlayerState> pstate) {
		this.pstate = pstate;
	}

	public List<FeedbackState> getStates() {
		return states;
	}

	public void setStates(List<FeedbackState> states) {
		this.states = states;
	}

	
}
