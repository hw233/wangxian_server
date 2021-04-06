package com.xuanzhi.tools.simplejpa.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

/**
 * 分区管理
 * 
 * 最重要的是管理分区列表
 * 分区列表是从数据库中收集的
 * 
 *
 */
public class MysqlSectionManager<T> {
	static Logger logger = LoggerFactory.getLogger(SimpleEntityManager.class);
	protected MysqlSection sections[];
	
	//可能为null
	protected MysqlSection nextIdleSection;
	
	protected boolean duringCreateIdleSection = false;
	protected Object createIdleSectionLock = new Object();
	protected Object switchIdleSectionLock = new Object();
	SimpleEntityManagerMysqlImpl<T> em;
	
	int maxRowNumForTable;
	
	MysqlSectionManager(SimpleEntityManagerMysqlImpl<T> em,int maxRowNum){
		this.em = em;
		maxRowNumForTable = maxRowNum;
	}
	
	public MysqlSection getNextIdleSection(){
		return nextIdleSection;
	}
	
	public MysqlSection[] getSections(){
		return sections;
	}
	
	public MysqlSection selectSectionById(long id){
		MysqlSection [] ss = sections;
		for(int i = 0 ; i < ss.length ; i++){
			if(ss[i].minId <= id && id <= ss[i].maxId )
				return ss[i];
		}
		return null;
	}
	
	public synchronized MysqlSection insertSectionById(long id){
		MysqlSection [] ss = sections;
		if(ss.length == 0) return null;
		for(int i = 0 ; i < ss.length ; i++){
			if(id <= ss[i].maxId){
				if(ss[i].minId > id)
					ss[i].minId = id;
				return ss[i];
			}
		}
		if(ss[ss.length-1].maxId < id){
			ss[ss.length-1].maxId = id;
		}
		if(ss[ss.length-1].minId > id){
			ss[ss.length-1].minId = id;
		}
		return ss[ss.length-1];
	}
	
	private boolean disableCheckDataBase = false;
	/**
	 * 初始化，
	 * 
	 * 需要从数据库中收集分区信息，建立分区列表
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception{
		sections = new MysqlSection[0];
		long startTime = System.currentTimeMillis();
		long now = startTime;
		Connection conn = null;
		try{
			conn = em.getConnection();
			DatabaseMetaData md = conn.getMetaData();
		//检查主表
			ResultSet rs = md.getTables(null, null, em.mde.primaryTable+"_S%", null);
			
			ArrayList<String> tableNameList = new ArrayList<String>();
			while(rs.next()){
				String tablename = rs.getString(3);
				String s = tablename.substring((em.mde.primaryTable+"_S").length());
				try{
					Integer.parseInt(s);
					tableNameList.add(tablename);
				}catch(Exception e){
					
				}
			}
			rs.close();
			if(tableNameList.size() == 0){
				this.createNewIdleSection();
			}else{
				
				Collections.sort(tableNameList, new Comparator<String>(){
					public int compare(String o1, String o2) {
						String s1 = o1.substring((em.mde.primaryTable+"_S").length());
						String s2 = o2.substring((em.mde.primaryTable+"_S").length());
						int k1 = Integer.parseInt(s1);
						int k2 = Integer.parseInt(s2);
						if(k1 < k2) return -1;
						if(k1 > k2) return 1;
						return 0;
					}
					
				});
				Statement stmt = conn.createStatement();
				int tableRowNums[] = new int[tableNameList.size()];
				long tableMaxIds[] = new long[tableNameList.size()];
				long tableMinIds[] = new long[tableNameList.size()];
				
				for(int i = 0 ; i < tableNameList.size() ; i++){
					 startTime = System.currentTimeMillis();
					String tn = tableNameList.get(i);
					String s2 = tn.substring((em.mde.primaryTable+"_S").length());
					String sql = "select count(*) from " + tn;
					rs = stmt.executeQuery(sql);
					rs.next();
					
					int count = rs.getInt(1);
					
					tableRowNums[i] = count;
					System.out.println("[SimpleEntityManager] ["+em.mde.cl.getName()+"] [分区:S"+s2+"] [正在检查数据库] [查询表的行数] [表:"+tn+"] [行数:"+count+"] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					
					rs.close();
					
					startTime = System.currentTimeMillis();
					sql = "select max("+em.mde.id.columnNameForMysql+"),min("+em.mde.id.columnNameForMysql+") from " + tn;
					rs = stmt.executeQuery(sql);
					long maxid = -1;
					long minid = -1;
					if(rs.next()){
						maxid = rs.getLong(1);
						minid = rs.getLong(2);
						tableMaxIds[i] = maxid;
						tableMinIds[i] = minid;
					}
					System.out.println("[SimpleEntityManager] ["+em.mde.cl.getName()+"] [分区:S"+s2+"] [正在检查数据库] [查询表最大的ID] [表:"+tn+"] [最大的ID:"+maxid+"] [最小的ID:"+minid+"] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
					
					rs.close();
					
					
					
				}
				
				 startTime = System.currentTimeMillis();
				
				for(int i = 0 ; i < tableMaxIds.length - 1 ; i++){
					if(tableMaxIds[i] >= tableMinIds[i+1] && tableMaxIds[i+1] > 0){
						System.out.println("[SimpleEntityManager] ["+em.mde.cl.getName()+"] [正在检查数据库] [检查分区之间的逻辑关系] [出现错误，后续的分区MAXID小于或等于前面的分区MAXID] [前置分区："+tableNameList.get(i)+"] [后续分区："+tableNameList.get(i+1)+"] [耗时："+(System.currentTimeMillis()- startTime)+"ms]");
						throw new Exception("后续的分区MINID小于或等于前面的分区MAXID");
					}
				}
				
				if(tableRowNums[tableRowNums.length-1] == 0){
					sections = new MysqlSection[tableRowNums.length-1];
					for(int i = 0 ; i < tableRowNums.length-1 ; i ++){
						sections[i] = new MysqlSection();
						
						String tn = tableNameList.get(i);
						String s2 = tn.substring((em.mde.primaryTable+"_S").length());
						sections[i].sectionIndex = Integer.parseInt(s2);
						
						sections[i].currentRowNum = tableRowNums[i];
						sections[i].maxId = tableMaxIds[i];
						sections[i].minId = tableMinIds[i];
						sections[i].status = MysqlSection.SECION_STATUS_CLOSE;
						if(!disableCheckDataBase)
						{
							em.checkDataBase(sections[i], em.mde.indexListForMySql, em.mde.secondTablesForMySql);
						}
					}
					if(sections.length > 0){
						sections[sections.length-1].status = MysqlSection.SECION_STATUS_OPEN;
					}
					
					nextIdleSection = new MysqlSection();
					String tn = tableNameList.get(tableRowNums.length-1);
					String s2 = tn.substring((em.mde.primaryTable+"_S").length());
					nextIdleSection.sectionIndex = Integer.parseInt(s2);
					nextIdleSection.currentRowNum = 0;
					nextIdleSection.maxId = Long.MIN_VALUE;
					nextIdleSection.minId = Long.MAX_VALUE;
					nextIdleSection.status = MysqlSection.SECION_STATUS_IDLE;
					if(!disableCheckDataBase)
					{
						em.checkDataBase(nextIdleSection, em.mde.indexListForMySql, em.mde.secondTablesForMySql);
					}

				}else{
					sections = new MysqlSection[tableRowNums.length];
					for(int i = 0 ; i < tableRowNums.length ; i ++){
						sections[i] = new MysqlSection();
						
						String tn = tableNameList.get(i);
						String s2 = tn.substring((em.mde.primaryTable+"_S").length());
						sections[i].sectionIndex = Integer.parseInt(s2);
						
						sections[i].currentRowNum = tableRowNums[i];
						sections[i].maxId = tableMaxIds[i];
						sections[i].minId = tableMinIds[i];
						sections[i].status = MysqlSection.SECION_STATUS_CLOSE;
						if(!disableCheckDataBase)
						{
							em.checkDataBase(sections[i], em.mde.indexListForMySql, em.mde.secondTablesForMySql);
						}
					}
					sections[sections.length-1].status = MysqlSection.SECION_STATUS_OPEN;
					nextIdleSection = null;
				}
			}
			
			checkSection(true);
			
			//检查各个分区，主表和副表的行数是否一致，不一致表示
			//某个副表是在运行一段时期后，新创建出来的，
			//那么会有很多数据，在主表中有记录，在副表中没有记录
			//这种情况下，需要在更新副表的数据前，先检查是否需要插入数据
				Statement stmt = conn.createStatement();
				for(int i = 0 ; i < sections.length ; i++){
					long lll = System.currentTimeMillis();
					MysqlSection section = sections[i];
					section.needCheckExistBeforeUpdateInSecondTables = new boolean[em.mde.secondTablesForMySql.size()];

					for(int j = 0 ; j < em.mde.secondTablesForMySql.size() ; j++){
						String tn = em.mde.secondTablesForMySql.get(j);
						
						String sql = "select count(1),max("+em.mde.id.columnNameForMysql+"_SEC_"+(j+1)+"),min("+em.mde.id.columnNameForMysql+"_SEC_"+(j+1)+") from " + tn+"_"+section.getName();
						rs = stmt.executeQuery(sql);
						long maxid = -1;
						long minid = -1;
						long rownum = -1;
						if(rs.next()){
							rownum = rs.getLong(1);
							maxid = rs.getLong(2);
							minid = rs.getLong(3);
						}
						if(section.currentRowNum == rownum && section.maxId == maxid && section.minId == minid){
							section.needCheckExistBeforeUpdateInSecondTables[j] = false;
						}else{
							section.needCheckExistBeforeUpdateInSecondTables[j] = true;
						}
						rs.close();
						System.out.println("[SimpleEntityManager] ["+em.mde.cl.getName()+"] [正在检查数据库] [分区:"+section.getName()+"] [副表：T"+(j+1)+"] ["+(section.needCheckExistBeforeUpdateInSecondTables[j]?"更新前需要检查是否存在":"更新前不需要检查存在性")+"] [主表："+section.currentRowNum+","+section.minId+","+section.maxId+"] [副表T"+(j+1)+":"+rownum+","+minid+","+maxid+"] [耗时:"+(System.currentTimeMillis() - lll)+"]");

					}
				}
			
		
			System.out.println("[SimpleEntityManager] ["+em.mde.cl.getName()+"] [正在检查数据库] [分区检查完毕] [耗时："+(System.currentTimeMillis()- now)+"ms]");

		}catch(Exception e){
			System.out.println("[SimpleEntityManager] ["+em.mde.cl.getName()+"] [正在检查数据库] [分区检查失败，出现异常] [耗时："+(System.currentTimeMillis()- now)+"ms]");

			throw e;
		}finally{
			if(conn != null){
				conn.close();
			}
		}
	}
	
	private void checkSection(boolean initialing){
		if(sections.length == 0 && this.nextIdleSection != null){
			MysqlSection s = nextIdleSection;
			MysqlSection ss[] = new MysqlSection[]{s};
			s.status = MysqlSection.SECION_STATUS_OPEN;
			nextIdleSection = null;
			this.sections = ss;
		}else{
			MysqlSection section = sections[sections.length-1];
			
			if(maxRowNumForTable > 0 && section.currentRowNum > maxRowNumForTable/2 && this.nextIdleSection == null){
				if(initialing){
					createNewIdleSection();
				}else{
					if(duringCreateIdleSection) return;
					Thread t = new Thread(new Runnable(){
						public void run(){
							createNewIdleSection();
						}
					},em.mde.cl.getName()+"-CreateNewIdleSection-Thread");
					t.start();
				}
			}
			
			
			if(maxRowNumForTable > 0 && section.currentRowNum >= maxRowNumForTable && this.nextIdleSection != null ){
				synchronized(switchIdleSectionLock){
					section = sections[sections.length-1];
					if(section.currentRowNum >= maxRowNumForTable && this.nextIdleSection != null ){
						MysqlSection s = nextIdleSection;
						MysqlSection ss[] = new MysqlSection[sections.length+1];
						System.arraycopy(sections, 0, ss, 0, sections.length);
						ss[sections.length] = s;
						s.status = MysqlSection.SECION_STATUS_OPEN;
						section.status = MysqlSection.SECION_STATUS_CLOSE;
						
						nextIdleSection = null;
						sections = ss;
						
						if(logger.isWarnEnabled()){
							logger.warn("[完成分区的切换] [从分区"+section.getName()+"切换到分区"+s.getName()+"] [老分区的行数:"+section.currentRowNum+"] [新分区的行数:"+s.currentRowNum+"]");
						}
					}
				}
			}
		}
		
		
		
	}
	
	private void createNewIdleSection(){
		if(nextIdleSection != null){
			throw new java.lang.IllegalStateException("存在空闲的分区，不能创建新的空闲分区");
		}
		if(duringCreateIdleSection) return;
		synchronized(createIdleSectionLock){
			if(duringCreateIdleSection) return;
			duringCreateIdleSection = true;
		}
		long now = System.currentTimeMillis();
		try{
			MysqlSection section = new MysqlSection();
			section.currentRowNum = 0;
			section.sectionIndex = sections.length + 1;
			section.maxId = Long.MIN_VALUE;
			section.minId = Long.MAX_VALUE;
			section.status = MysqlSection.SECION_STATUS_IDLE;
			
			if(!disableCheckDataBase)
			{
				em.checkDataBase(section, em.mde.indexListForMySql, em.mde.secondTablesForMySql);
			}
			
			nextIdleSection = section;
			System.out.println("[SimpleEntityManager] ["+em.mde.cl.getName()+"] [创建分区完毕] [分区:"+nextIdleSection.getName()+"] [耗时："+(System.currentTimeMillis()- now)+"ms]");

		}catch(Throwable e){	
			System.out.println("**************************************************************");
			System.out.println("*********     创建新的分区失败，非常严重的错误！            **********");
			System.out.println("*********     "+com.xuanzhi.tools.text.DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") +"      **********");
			System.out.println("**************************************************************");
			e.printStackTrace(System.out);
		}finally{
			synchronized(createIdleSectionLock){
				duringCreateIdleSection = false;
			}
		}
		
	}
	
	public void notifyInsertOneRow(MysqlSection section,long id){
		synchronized(section){
			section.currentRowNum++;
			if(section.maxId < id) section.maxId  = id;
			if(section.minId > id) section.minId  = id;
		}
		checkSection(false);
	}
	
	
	
	public void notifyRemoveOneRow(MysqlSection section){
		synchronized(section){
			section.currentRowNum--;
		}
	}
	
	
}
