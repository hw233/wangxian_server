package com.fy.boss.gm.gmuser.server;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.gm.gmuser.QuestionQuery;
import com.fy.boss.gm.gmuser.TransferQuestion;
import com.fy.boss.gm.gmuser.server.TransferQuestionManager;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * 问题传递处理
 * @author wtx
 *
 */
public class TransferQuestionManager{

	private static TransferQuestionManager self;
	
	public static List<TransferQuestion> questions = new ArrayList<TransferQuestion>();
	
	public static List<QuestionQuery> records = new ArrayList<QuestionQuery>();
	
	public SimpleEntityManager<TransferQuestion> sem;

	public SimpleEntityManager<QuestionQuery> sem2;
	
//	public static List<TransferQuestion> newQuestions  = new ArrayList<TransferQuestion>();
	
	private DefaultDiskCache ddc;
	
	public static Logger logger = Logger.getLogger(TransferQuestionManager.class);
	
	public static TransferQuestionManager getInstance(){
		return self;
	}
	
	public File dataFile;
	
	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	//初始化
	public void init() throws Exception{
		long now = System.currentTimeMillis();
		self = this;
		sem = SimpleEntityManagerFactory.getSimpleEntityManager(TransferQuestion.class);
		sem2 = SimpleEntityManagerFactory.getSimpleEntityManager(QuestionQuery.class);
		questions = getQuestionsByState(0,1,2000);
		records = getQuestionsByState2(0,1,2000);
		ddc = new DefaultDiskCache(dataFile, null,"事件最大编号", 100L * 365 * 24 * 3600 * 1000L, false, false);
		Calendar cl = Calendar.getInstance();
		cl.clear();
		cl.setTime(new Date());
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH);
		int day = cl.get(Calendar.DAY_OF_MONTH);
		String daystr = "";
		String monthstr = "";
		daystr = ""+day;
		if(day<10){
			daystr = "0"+day;
		}
		if(month<9){
			monthstr = "0"+(month+1);
		}else{
			monthstr = ""+(month+1);
		}
		
		if(ddc.get(year+""+monthstr+daystr)!=null){
			questionDayMaxNum = (Integer)ddc.get(year+monthstr+daystr)+1;
		}
		logger.warn("[获得重启前的最大编号] [num:"+questionDayMaxNum+"] [ddckey:"+(year+""+monthstr+daystr)+"] [ddcvalue:"+(Integer)ddc.get(year+monthstr+daystr)+"] [year:"+year+"] [month:"+month+"] [day:"+day+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		System.out.println("[问题传递管理器初始化] [成功] ["+TransferQuestionManager.class.getName()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
	}

	public List<TransferQuestion> getQuestionsByState(int state,long start,long end){
		long now =  System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>(); 
		try {
			list = sem.query(TransferQuestion.class, " handleState = "+state, "recordTime desc", start, end);
			logger.warn("[通过处理状态获得问题] [成功] [状态："+state+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过处理状态获得问题] [异常] [状态："+state+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			
		}
		
		return list;
	}
	
	public List<TransferQuestion> handleBugForSameEventid(){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>(); 
		String begintime = "2013-01-07";
		try {
			list = sem.query(TransferQuestion.class, " recordTime >= '"+begintime+"' ", "recordTime desc", 1, 500);
			
			logger.warn("[处理串的BUG] [listsize："+list.size()+"] [成功] [] [==========]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[处理串的BUG] [listsize："+list.size()+"] [异常] [] [==========]",e);
		}
		return list;
	}
	
	public List<TransferQuestion> getQuestionsByBuMeng(String bumeng,long start,long end){
		long now =  System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>(); 
		try {
			list = sem.query(TransferQuestion.class, " handlOtherBuMeng = '"+bumeng+"'", "recordTime desc", start, end);
			logger.warn("[获得已经解决的问题] [成功] [部门："+bumeng+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得已经解决的问题] [异常] [部门："+bumeng+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			
		}
		
		return list;
	}
	/**
	 * 通过部门获得消息
	 * @param bumeng
	 * @param start
	 * @param end
	 * @return
	 */
	public List<TransferQuestion> getQuestionsByBuMengBetweenTime(String bumeng,String begintime,String endtime,long start,long end){
		long now =  System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>(); 
		try {
			list = sem.query(TransferQuestion.class, " recordTime >= '"+begintime+"' AND recordTime <= '"+endtime+"' AND handlOtherBuMeng = '"+bumeng+"'", "recordTime desc", start, end);
			logger.warn("[获得已经解决的问题] [成功] [部门："+bumeng+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得已经解决的问题] [异常] [部门："+bumeng+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			
		}
		
		return list;
	}
	
	/**
	 * 事件id，201210230001-201210230999
	 * @return
	 */
	private int questionDayMaxNum = 1;
	
	public synchronized String getEventId(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String time = sdf.format(new Date());
		Integer issave = (Integer)ddc.get(time);
		String eventid = ""; 
		if(issave==null){
			questionDayMaxNum = 1;
			ddc.put(time, questionDayMaxNum);
			eventid = time+questionDayMaxNum++;
			logger.warn("[第一天设置事件编号] [事件编号："+eventid+"] [time:"+time+"] [下一次编号："+questionDayMaxNum+"]");
		}else{
			ddc.put(time, questionDayMaxNum);
			eventid = time + questionDayMaxNum++;
			logger.warn("[获得事件编号] [事件编号："+eventid+"] [time:"+time+"] [下一次编号："+questionDayMaxNum+"]");
		}
		return eventid;
	}
	
	public synchronized boolean addQuestion(TransferQuestion question){
		long now = System.currentTimeMillis();
		try {
			long id = sem.nextId();
			question.setId(id);
			question.setEventid(getEventId());
			sem.save(question);
			questions.add(question);
			//
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time2 = sdf.format(new Date());	
			QuestionQuery questionquery =  new QuestionQuery();
			questionquery.setEventid(question.getEventid());
			questionquery.setHandleer(question.getHandler());
			questionquery.setHandledoor(question.getHandlBuMeng());
			questionquery.setHandletime(time2);
			questionquery.setHandletodoor(question.getHandlOtherBuMeng());
			questionquery.setQuestionmess(question.getQuestionMess());
			addRecord(questionquery);
			logger.warn("[提交新问题] [成功] [提交人："+question.getHandler()+"] [事件编号："+question.getEventid()+"] [数量："+questions.size()+"] [提交时间："+question.getRecordTime()+"] [玩家账号："+question.getUsername()+"] [服务器："+question.getServerName()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[提交新问题] [异常] [提交人："+question.getHandler()+"] [事件编号："+question.getEventid()+"] [数量："+questions.size()+"] [提交时间："+question.getRecordTime()+"] [玩家账号："+question.getUsername()+"] [服务器："+question.getServerName()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			return false;
		}
	}
	
	
	//把全的给见秦
	public void handledate(String name){
		List<TransferQuestion> list = getQuestionsByBuMen(name);
		try{
			if(list.size()>0){
				for(TransferQuestion qq:list){
					qq.setHandlOtherBuMeng("张剑琴");
				}
			}
			logger.warn("[部门转移] [成功] [数量："+list.size()+"] [name:"+name+"]");
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[部门转移] [成功] [数量："+list.size()+"] [name:"+name+"]",e);
		}
		
		
	}
	
	
	public List<TransferQuestion> getQuestionsByYiJiType(String type){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		for (TransferQuestion qq:questions) {
			if(qq.getQuestionType1().equals(type)){
				list.add(qq);
			}
		}
		logger.warn("[通过一级分类获得问题] [成功] [类型："+type+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		return list;
	}
	
	/**
	 * 查询登录后台的用户所属部门和当前问题所在部门一致
	 * @param bumennaem
	 * @return
	 */
	public List<TransferQuestion> getQuestionsByBuMen(String bumennaem){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		try{
//			for (TransferQuestion qq:questions) {
//				if(qq.getHandlOtherBuMeng()!=null&&qq.getHandleState()==0){
//					if(qq.getHandlOtherBuMeng().equals(bumennaem)||qq.getHandlOtherBuMeng().equals("默认所有")){
//						list.add(qq);
//					}
//				}
//			}
			list = sem.query(TransferQuestion.class, " handlOtherBuMeng = '"+bumennaem+"'", "eventid asc", 1, 5000);
			
			logger.warn("[通过部门获得问题] [成功] [部门类型："+bumennaem+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[通过部门获得问题] [异常] [部门类型："+bumennaem+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		
		return list;
	}
	
	public List<TransferQuestion> getQuestionsByBuMen2(String bumennaem,int state){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		try{
			list = sem.query(TransferQuestion.class, " handleState = "+state+" AND handlOtherBuMeng = '"+bumennaem+"'", "eventid asc", 1, 5000);
			logger.warn("[通过部门获得问题2] [成功] [部门类型："+bumennaem+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[通过部门获得问题2] [异常] [部门类型："+bumennaem+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		
		return list;
	}
	
	public List<TransferQuestion> getQuestionsViplevel(String viplevel){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		try{
			list = sem.query(TransferQuestion.class, " viplevel >= '"+viplevel+"'", "viplevel desc", 1, 5000);
			logger.warn("[通过VIP等级获得问题2] [成功] [VIP等级："+viplevel+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[通过VIP等级获得问题2] [异常] [VIP等级："+viplevel+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		
		return list;
	}
	
	//清理
	public boolean delQuestionById(long id){
		List<TransferQuestion> rem1 = new ArrayList<TransferQuestion>();
		List<QuestionQuery> rem2 = new ArrayList<QuestionQuery>();
		if(questions.size()>0){
			for(TransferQuestion qq:questions){
				if(qq.getId()==id){
					if(qq.getEventid()!=null&&qq.getEventid().trim().length()>0){
						for(QuestionQuery record:records){
							if(record.getEventid()!=null&&record.getEventid().equals(qq.getEventid())){
								record.setState(1);
								rem2.add(record);
							}
						}
					}
					qq.setHandleState(1);
					qq.setHandlOtherBuMeng("已处理");
					rem1.add(qq);
				}
			}
			if(questions.removeAll(rem1)&&records.removeAll(rem2)){
				logger.warn("[GM回复玩家问题] [完毕] [id:"+id+"]");
				return true;
			}
			
		}
		return false;
	}
	
	public List<TransferQuestion> getQuestionsByBuMenAndYiJi(String bumennaem,String type){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		for (TransferQuestion qq:questions) {
			if(qq.getHandlBuMeng().equals(bumennaem)&&qq.getQuestionType1().equals(type)){
				list.add(qq);
			}
		}
		logger.warn("[通过部门和一级分类获得问题] [成功] [一级类型："+type+"] [部门类型："+bumennaem+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		return list;
	}
	
	public TransferQuestion getQuestionById(long id){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		try {
			list = sem.query(TransferQuestion.class, " id = "+id, "recordTime desc", 1, 10);
			logger.warn("[通过id获得问题] [成功] [id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过id获得问题] [异常] [id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		if(list.size()>0){
			return list.get(0);
		}
		return null;
		
	}
	
	//返回问题当前的处理人
	public String[] getTransferQuestions(){
		List<String> list = new ArrayList<String>();
		if(questions.size()>0){
			for (int i=0;i<questions.size();i++) {
				TransferQuestion qq = questions.get(i);
				if(qq.getCurrHadler()!=null&&qq.getCurrHadler().trim().length()>0&&qq.getHandlOtherBuMeng()!=null&&qq.getHandlOtherBuMeng().trim().length()>0){
					String mess = qq.getId()+"@#$^&*"+qq.getCurrHadler()+"@#$^&*"+qq.getHandlOtherBuMeng();
					list.add(mess);
				}
			}
		}
		return list.toArray(new String[]{});
	}
	
	public String[] getTransferQuestionsdel(){
		long now = System.currentTimeMillis();
		List<String> list = new ArrayList<String>();
		if(questions.size()>0){
			for (int i=0;i<questions.size();i++) {
				TransferQuestion qq = questions.get(i);
				//创建时间大于当前时间10分钟的，isnewquestion=old
				String [] times = qq.getRecordTime().split(" ");
				String [] nyd = times[0].split("-");
				String [] sfm = times[1].split(":");
				Calendar cl = Calendar.getInstance();
				cl.clear();
				cl.set(Integer.parseInt(nyd[0]), Integer.parseInt(nyd[1])-1, Integer.parseInt(nyd[2]), Integer.parseInt(sfm[0]), Integer.parseInt(sfm[1]),Integer.parseInt(sfm[2]));
				
				if(qq.getIsDelete()!=null&&qq.getIsDelete().equals("yes")){
					if(qq.getIsDelete().equals("yes")){
						String mess = qq.getId()+"@#$^&*"+qq.getCurrHadler()+"@#$^&*"+qq.getGameName()+"@#$^&*"+qq.getServerName()+"@#$^&*"+qq.getUsername()+
								"@#$^&*"+qq.getViplevel()+"@#$^&*"+qq.getQuestionType1()+"@#$^&*"+qq.getQuestionType2()+"@#$^&*"+qq.getRecordTime()+
								"@#$^&*"+qq.getHandlOtherBuMeng()+"@#$^&*"+qq.getEventid()+"@#$^&*"+qq.getIsDelete();
						
						list.add(mess);
//						logger.warn("[定时器获得删除消息yes] [成功] [mess："+mess+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
					}
					if(System.currentTimeMillis()-qq.getCommitTime()>30*1000){
						qq.setIsDelete("no");
//						logger.warn("[定时器设置删除消息] [成功] [time："+qq.getCommitTime()+"] [username:"+qq.getUsername()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
					}
				}
			}
		}
		return list.toArray(new String[]{});
	}
	
	public String[] getTransferQuestions2(){
		long now = System.currentTimeMillis();
		List<String> list = new ArrayList<String>();
		if(questions.size()>0){
			for (int i=0;i<questions.size();i++) {
				TransferQuestion qq = questions.get(i);
				//创建时间大于当前时间10分钟的，isnewquestion=old
//				logger.warn("[定时器新到消息1] [成功] [time："+qq.getRecordTime()+"] [qq.getIsNewQuestion():"+qq.getIsNewQuestion()+"] [username:"+qq.getUsername()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
				String [] times = qq.getRecordTime().split(" ");
				String [] nyd = times[0].split("-");
				String [] sfm = times[1].split(":");
				Calendar cl = Calendar.getInstance();
				cl.clear();
				cl.set(Integer.parseInt(nyd[0]), Integer.parseInt(nyd[1])-1, Integer.parseInt(nyd[2]), Integer.parseInt(sfm[0]), Integer.parseInt(sfm[1]),Integer.parseInt(sfm[2]));
				
				if(qq.getIsNewQuestion()!=null&&qq.getIsNewQuestion().equals("new")){
					if(System.currentTimeMillis()-cl.getTimeInMillis()>60*60*1000){
						qq.setIsNewQuestion("old");
						logger.warn("[定时器设置新到消息new] [成功] [time："+qq.getRecordTime()+"]  [username:"+qq.getUsername()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
					}
					if(qq.getIsNewQuestion().equals("new")){
						String mess = qq.getId()+"@#$^&*"+qq.getCurrHadler()+"@#$^&*"+qq.getGameName()+"@#$^&*"+qq.getServerName()+"@#$^&*"+qq.getUsername()+
								"@#$^&*"+qq.getViplevel()+"@#$^&*"+qq.getQuestionType1()+"@#$^&*"+qq.getQuestionType2()+"@#$^&*"+qq.getRecordTime()+
								"@#$^&*"+qq.getHandlOtherBuMeng()+"@#$^&*"+qq.getEventid()+"@#$^&*"+qq.getIsDelete();
						list.add(mess);
//						logger.warn("[定时器获得新消息new] [成功] [mess："+mess+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
					}
				}
			
			}
		}
		return list.toArray(new String[]{});
	}
	
	public void setQuestionMess(long id){
		long now = System.currentTimeMillis();
		for (TransferQuestion qq:questions) {
			if(qq.getId()==id){
				qq.setCurrHadler("--");
				logger.warn("[离开页面] [成功] [id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			}
		}
	}
	
	//通过事件id获得所有的递交过程
	public List<QuestionQuery> getQueriesByEventId(String eventid){
		long now = System.currentTimeMillis();
		List<QuestionQuery> list = new ArrayList<QuestionQuery>();
//		if(records.size()>0){
//			for(QuestionQuery qq:records){
//				if(qq.getEventid()!=null&&qq.getEventid().equals(eventid)){
//					list.add(qq);
//				}
//			}
//		}
		try {
			list = sem2.query(QuestionQuery.class, " eventid = '"+eventid+"'", "handletime desc", 1, 100);
			logger.warn("[通过事件id获得递交记录] [成功] [数量："+list.size()+"] [id:"+eventid+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过事件id获得递交记录] [异常] [数量："+list.size()+"] [id:"+eventid+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;  
		
	}
	
	
	public List<TransferQuestion> getQueriesByGmName(String gmname,String begintime,String endtime){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		try {
			list = sem.query(TransferQuestion.class, " recordTime >= '"+begintime+"' AND recordTime <= '"+endtime+"' AND handler = '"+gmname+"' AND handlOtherBuMeng = '已处理'", "recordTime desc", 1, 1000);
			logger.warn("[通过GM名称获得递交记录] [成功] [数量："+list.size()+"] [GM名称:"+gmname+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过GM名称获得递交记录] [异常] [数量："+list.size()+"] [GM名称:"+gmname+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;  
		
	}
	
	public List<TransferQuestion> getQueriesByUserName(String username){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		try {
			list = sem.query(TransferQuestion.class, " username = '"+username+"' AND handlOtherBuMeng = '已处理'", "recordTime desc", 1, 1000);
			logger.warn("[通过GM名称获得递交记录] [成功] [数量："+list.size()+"] [玩家账号:"+username+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过GM名称获得递交记录] [异常] [数量："+list.size()+"] [玩家账号:"+username+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;  
		
	}
	
	public List<QuestionQuery> getQuestionsByState2(int state,int start, int end){
		long now = System.currentTimeMillis();
		List<QuestionQuery> list = new ArrayList<QuestionQuery>();
		try {
			list = sem2.query(QuestionQuery.class, " state = "+state, "handletime desc", start, end);
			logger.warn("[通过处理状态获得问题记录] [成功] [状态："+state+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过处理状态获得问题记录] [成功] [状态："+state+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			
		}
		return list;
	}
	
	public List<TransferQuestion> getQuestionsByUsername(String username,int start, int end){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		try {
			list = sem.query(TransferQuestion.class, " username = '"+username+"'", "recordTime desc", start, end);
			logger.warn("[通过玩家账号获得问题记录] [成功] [账号："+username+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过玩家账号获得问题记录] [成功] [账号："+username+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			
		}
		return list;
	}
	
	public List<TransferQuestion> getQuestionsByEventid(String eventid,int start, int end){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		try {
			list = sem.query(TransferQuestion.class, " eventid = '"+eventid+"'", "recordTime desc", start, end);
			logger.warn("[通过事件编号获得问题记录] [成功] [账号："+eventid+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过事件编号获得问题记录] [成功] [账号："+eventid+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			
		}
		return list;
	}
	
	public List<TransferQuestion> getQuestionsByTelephone(String telephone,int start, int end){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		try {
			list = sem.query(TransferQuestion.class, " telephone = '"+telephone+"'", "recordTime desc", start, end);
			logger.warn("[通过联系电话获得问题记录] [成功] [电话："+telephone+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过联系电话获得问题记录] [成功] [电话："+telephone+"] [开始标："+start+"] [结束标："+end+"] [数量："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			
		}
		return list;
	}
	
	public synchronized boolean addRecord(QuestionQuery record){
		long now = System.currentTimeMillis();
		try {
			long id = sem2.nextId();
			record.setId(id);
			record.setEventid(record.getEventid());
			sem2.save(record);
			records.add(record);
			logger.warn("[提交新问题记录] [成功] [提交人："+record.getHandleer()+"] [事件ID："+record.getEventid()+"] [数量："+records.size()+"] [提交时间："+record.getHandletime()+"]  [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[提交新问题记录] [异常] [提交人："+record.getHandleer()+"] [事件ID："+record.getEventid()+"] [数量："+records.size()+"] [提交时间："+record.getHandletime()+"]  [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
			return false;
		}
	}
	/**
	 * 获得一天的统计数
	 * @return
	 */
	public long getNumberByDay(String beginday,String endday){
		long now = System.currentTimeMillis();
		long count = 0;
		try {
			count = sem.count(TransferQuestion.class, " recordTime >= '"+beginday+"' AND recordTime <= '"+endday+"'");
			logger.warn("[获得一天扭转信息数量] [成功] [起始时间："+beginday+"] [结束时间："+endday+"] [数量："+count+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得一天扭转信息数量] [异常] [起始时间："+beginday+"] [结束时间："+endday+"] [数量："+count+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return count;
	}
	/**
	 * 根据提交人获得信息
	 * @param beginday
	 * @param endday
	 * @return
	 */
	public List<TransferQuestion> getNumberByCommiter(String commiter,String begintime,String endtime,long start,long end){
		long now = System.currentTimeMillis();
		List<TransferQuestion> list = new ArrayList<TransferQuestion>();
		try {
			list = sem.query(TransferQuestion.class, " recordTime >= '"+begintime+"' AND recordTime <= '"+endtime+"' AND handler = '"+commiter+"'", "recordTime desc", start, end);
			logger.warn("[通过提交人获得扭转信息数量] [成功] [提交人："+commiter+"] [开始数："+start+"] [数量："+list.size()+"] [长度："+end+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过提交人获得扭转信息数量] [异常] [提交人"+commiter+"] [开始数："+start+"] [数量："+list.size()+"] [长度："+end+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public SimpleEntityManager<TransferQuestion> getSem() {
		return sem;
	}

	public void setSem(SimpleEntityManager<TransferQuestion> sem) {
		this.sem = sem;
	}

	public SimpleEntityManager<QuestionQuery> getSem2() {
		return sem2;
	}

	public void setSem2(SimpleEntityManager<QuestionQuery> sem2) {
		this.sem2 = sem2;
	}
	
	public List<TransferQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<TransferQuestion> questions) {
		this.questions = questions;
	}
	
	public void destroy () {
		if (sem != null)  {
			sem.destroy();
		}
		if (sem2 != null)  {
			sem2.destroy();
		}
	}
	
}
