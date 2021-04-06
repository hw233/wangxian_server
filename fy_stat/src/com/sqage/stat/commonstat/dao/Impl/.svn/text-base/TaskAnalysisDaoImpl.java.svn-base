package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.TaskAnalysisDao;
import com.sqage.stat.commonstat.entity.TaskAnalysis;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class TaskAnalysisDaoImpl implements TaskAnalysisDao {
	static Logger logger = Logger.getLogger(TaskAnalysisDaoImpl.class);
	DataSourceManager dataSourceManager;
	
	@Override
	public TaskAnalysis add(TaskAnalysis taskAnalysis) {
        long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_TASK_ANALYSIS(" +
					"ID," +
					"CREATEDATE," +
					"FENQU," +
					
					"TASKNAME," +
					"TASKTYPE," +
					"TASKGROUP,"+
					"PRETASKGROUP,"+
					"CAREER,"+
					
					"ACCEPTCOUNT,"+
					"FINISHCOUNT,"+
					"NEXTACCEPTCOUNT,"+
					"JIXING"+
					
					") values (SEQ_STAT_TASK_ANALYSIS.NEXTVAL,?,?, ?,?,?,?,?, ?,?,?, ?)");
			
			if(taskAnalysis.getCreateDate()!=null){	
			ps.setTimestamp(1, new Timestamp(taskAnalysis.getCreateDate().getTime()));
			}else{
				ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			}
			ps.setString(2, taskAnalysis.getFenQu());
			ps.setString(3, taskAnalysis.getTaskName());
			ps.setString(4, taskAnalysis.getTaskType());
			ps.setString(5, taskAnalysis.getTaskGroup());
			ps.setString(6, taskAnalysis.getPreTaskGroup());
			ps.setString(7, taskAnalysis.getCareer());
			ps.setLong(8, taskAnalysis.getAcceptCount());
			ps.setLong(9, taskAnalysis.getFinishCount());
			ps.setLong(10,taskAnalysis.getNextAcceptCount());
			ps.setString(11,taskAnalysis.getJixing());
			
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[添加任务流失信息] [OK] "+taskAnalysis.toString()+"[cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (Exception e) {
			   logger.error("[添加任务流失信息] [OK] "+taskAnalysis.toString()+"[cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? taskAnalysis : null;
	
	
	}

	@Override
	public synchronized boolean addList(ArrayList<TaskAnalysis> taskAnalysisList) {
        boolean result=false;
        long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_TASK_ANALYSIS(" +
					"ID," +
					"CREATEDATE," +
					"FENQU," +
					
					"TASKNAME," +
					"TASKTYPE," +
					"TASKGROUP,"+
					"PRETASKGROUP,"+
					"CAREER,"+
					
					"ACCEPTCOUNT,"+
					"FINISHCOUNT,"+
					"NEXTACCEPTCOUNT,"+
					"JIXING"+
					
					") values (SEQ_STAT_TASK_ANALYSIS.NEXTVAL,?,?, ?,?,?,?,?, ?,?,?, ?)");
			
			for(TaskAnalysis taskAnalysis:taskAnalysisList){
			
			  if(taskAnalysis.getCreateDate()!=null){	
				ps.setTimestamp(1, new Timestamp(taskAnalysis.getCreateDate().getTime()));}
				ps.setString(2, taskAnalysis.getFenQu());
				ps.setString(3, taskAnalysis.getTaskName());
				ps.setString(4, taskAnalysis.getTaskType());
				ps.setString(5, taskAnalysis.getTaskGroup());
				ps.setString(6, taskAnalysis.getPreTaskGroup());
				ps.setString(7, taskAnalysis.getCareer());
				ps.setLong(8, taskAnalysis.getAcceptCount());
				ps.setLong(9, taskAnalysis.getFinishCount());
				ps.setLong(10,taskAnalysis.getNextAcceptCount());
				ps.setString(11,taskAnalysis.getJixing());
			
			    ps.executeUpdate();
			  if(logger.isDebugEnabled()){logger.debug("[添加任务流失信息] [OK] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");	}
			}
			  con.commit();
			  con.setAutoCommit(true);
			  result=true;
		    } catch (Exception e) {
			   logger.error("[添加任务信息] [FAIL] ["+taskAnalysisList.size()+"条信息] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			try {
				ps.close();
			   if(con != null) { con.close();}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	return result;
	}

	@Override
	public List<TaskAnalysis> getBySql(String sql) {

		Connection con = null;
		PreparedStatement ps = null;
		List<TaskAnalysis> taskAnalysisList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				TaskAnalysis taskAnalysis = new TaskAnalysis();
				
				taskAnalysis.setAcceptCount(rs.getLong("ACCEPTCOUNT"));
				taskAnalysis.setCareer(rs.getString("CAREER"));
				taskAnalysis.setCreateDate(rs.getTimestamp("CREATEDATE"));
				taskAnalysis.setFenQu(rs.getString("FENQU"));
				
				taskAnalysis.setFinishCount(rs.getLong("FINISHCOUNT"));
				taskAnalysis.setId(rs.getLong("id"));
				taskAnalysis.setNextAcceptCount(rs.getLong("NEXTACCEPTCOUNT"));
				taskAnalysis.setPreTaskGroup(rs.getString("PRETASKGROUP"));
				
				taskAnalysis.setTaskGroup(rs.getString("TASKGROUP"));
				taskAnalysis.setTaskName(rs.getString("TASKNAME"));
				taskAnalysis.setTaskType(rs.getString("TASKTYPE"));
				taskAnalysis.setJixing(rs.getString("JIXING"));
				
				taskAnalysisList.add(taskAnalysis);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return taskAnalysisList;
	}

	/**
	 * 更新接受数
	 * @param taskAnalysis
	 * @return
	 */
	public synchronized boolean  updateAcceptCount(TaskAnalysis taskAnalysis) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_TASK_ANALYSIS set "+
					" ACCEPTCOUNT=?,"+
					" NEXTACCEPTCOUNT=? "+
					" where ID=?");
			ps.setLong(1, taskAnalysis.getAcceptCount());
			ps.setLong(2, taskAnalysis.getNextAcceptCount());
			ps.setLong(3, taskAnalysis.getId());
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[更新任务流失信息accept] [OK] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (SQLException e) {
		    	logger.error("[更新任务流失信息accept] [FAIL] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			 try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0;
	}
	
	
	public synchronized boolean  updateAcceptCountList(List<TaskAnalysis> taskAnalysisList) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		TaskAnalysis taskAnalysis=null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("update STAT_TASK_ANALYSIS set "+
					" ACCEPTCOUNT=?,"+
					" NEXTACCEPTCOUNT=? "+
					" where ID=?");
			for(int index=0;index<taskAnalysisList.size();index++)
			{
			taskAnalysis=taskAnalysisList.get(index);
			ps.setLong(1, taskAnalysis.getAcceptCount());
			ps.setLong(2, taskAnalysis.getNextAcceptCount());
			ps.setLong(3, taskAnalysis.getId());
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[更新任务流失信息accept] [OK] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
			}
			con.setAutoCommit(true);
			con.commit();
		    } catch (SQLException e) {
		    	logger.error("[更新任务流失信息accept] [FAIL] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			 try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0;
	}
	/**
	 * 更新完成任务数
	 * @param taskAnalysis
	 * @return
	 */
	public synchronized boolean  updateFinishCount(TaskAnalysis taskAnalysis) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_TASK_ANALYSIS set "+
					" FINISHCOUNT=?"+
					" where ID=?");
			ps.setLong(1, taskAnalysis.getFinishCount());
			ps.setLong(2, taskAnalysis.getId());
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[更新任务流失信息finish] [OK] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (SQLException e) {
		    	logger.error("[更新任务流失信息finish] [FAIL] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			 try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0;
	}
	/**
	 * 更新完成任务列表
	 * @param taskAnalysisList
	 * @return
	 */
	public synchronized boolean  updateFinishCountList(List<TaskAnalysis> taskAnalysisList) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		TaskAnalysis taskAnalysis=null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("update STAT_TASK_ANALYSIS set "+
					" FINISHCOUNT=?"+
					" where ID=?");
			for(int index=0;index<taskAnalysisList.size();index++)
			{
		    taskAnalysis=taskAnalysisList.get(index);
			ps.setLong(1, taskAnalysis.getFinishCount());
			ps.setLong(2, taskAnalysis.getId());
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[更新任务流失信息finish] [OK] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
			}
			con.setAutoCommit(true);
			con.commit();
		    } catch (SQLException e) {
		    	logger.error("[更新任务流失信息finish] [FAIL] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			 try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0;
	}
	
	@Override
	public synchronized boolean  update(TaskAnalysis taskAnalysis) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_TASK_ANALYSIS set "+
					"CREATEDATE=?," +
					"FENQU=?," +
					"TASKNAME=?," +
					
					"TASKTYPE=?," +
					"TASKGROUP=?,"+
					"PRETASKGROUP=?,"+
					"CAREER=?,"+
					
					"ACCEPTCOUNT=?,"+
					"FINISHCOUNT=?,"+
					"NEXTACCEPTCOUNT=?, "+
					"JIXING=? "+
					
					" where ID=?");
			if(taskAnalysis.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(taskAnalysis.getCreateDate().getTime()));
			}
			ps.setString(2, taskAnalysis.getFenQu());
			ps.setString(3,taskAnalysis.getTaskName());
			ps.setString(4, taskAnalysis.getTaskType());
			
			ps.setString(5, taskAnalysis.getTaskGroup());
			ps.setString(6, taskAnalysis.getPreTaskGroup());
			ps.setString(7, taskAnalysis.getCareer());
			
			ps.setLong(8, taskAnalysis.getAcceptCount());
			ps.setLong(9, taskAnalysis.getFinishCount());
			ps.setLong(10, taskAnalysis.getNextAcceptCount());
			ps.setString(11, taskAnalysis.getJixing());
		
			ps.setLong(12, taskAnalysis.getId());
			
			i =ps.executeUpdate();
			
			if(logger.isDebugEnabled()){
				logger.debug("[更新任务流失信息] [OK] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}

		    } catch (SQLException e) {
		    	logger.error("[更新任务流失信息] [FAIL] "+taskAnalysis.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			 try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0;
	}

	@Override
	public boolean updateList(ArrayList<TaskAnalysis> taskAnalysisList) {
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		boolean result=true;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("update STAT_TASK_ANALYSIS set "+
					"CREATEDATE=?," +
					"FENQU=?," +
					"TASKNAME=?," +
					
					"TASKTYPE=?," +
					"TASKGROUP=?,"+
					"PRETASKGROUP=?,"+
					"CAREER=?,"+
					
					"ACCEPTCOUNT=?,"+
					"FINISHCOUNT=?,"+
					"NEXTACCEPTCOUNT=?, "+
					"JIXING=? "+
					
					" where ID=?");
			for(TaskAnalysis taskAnalysis:taskAnalysisList){
			if(taskAnalysis.getCreateDate()!=null){	ps.setTimestamp(1, new Timestamp(taskAnalysis.getCreateDate().getTime()));}
				ps.setString(2, taskAnalysis.getFenQu());
				ps.setString(3,taskAnalysis.getTaskName());
				ps.setString(4, taskAnalysis.getTaskType());
				
				ps.setString(5, taskAnalysis.getTaskGroup());
				ps.setString(6, taskAnalysis.getPreTaskGroup());
				ps.setString(7, taskAnalysis.getCareer());
				
				ps.setLong(8, taskAnalysis.getAcceptCount());
				ps.setLong(9, taskAnalysis.getFinishCount());
				ps.setLong(10, taskAnalysis.getNextAcceptCount());
				ps.setString(11, taskAnalysis.getJixing());
				ps.setLong(12, taskAnalysis.getId());
			
			i =ps.executeUpdate();
			if(i<=0){result=false;}
			}
			con.commit();
		} catch (SQLException e) {
			result=false;
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			    if(con != null){ con.close();}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	
	@Override
	public List<String[]> getTaskStat(String startDateStr, String endDateStr, String fenQu, String tasktype) {
		
		String subSql="";
		
		if(fenQu!=null&&!"".equals(fenQu)){ subSql+=" and t.fenqu='"+fenQu+"'  ";  }
		if(tasktype!=null&&!"".equals(tasktype)){ subSql+="  and t.tasktype='"+tasktype+"'";  }
		
		String sql="select t.taskgroup,sum(t.acceptcount) acceptcount,sum(t.finishcount) finishcount,sum(t.nextacceptcount) nextacceptcount " +
				" from stat_task_analysis t where 1=1 "+subSql+" and to_char(t.createdate,'YYYY-MM-DD') between '"+startDateStr+"' and '"+endDateStr+"'" +
				" group by t.taskgroup";
		
		
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[4];
				vales[0]=rs.getString("taskgroup");
				vales[1]=rs.getString("acceptcount");
				vales[2]=rs.getString("finishcount");
				vales[3]=rs.getString("nextacceptcount");
				
				resultlist.add(vales);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultlist;
	}

	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}
}
