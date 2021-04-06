package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.TaskinfoDao;
import com.sqage.stat.commonstat.entity.Taskinfo;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class TaskinfoDaoImpl implements TaskinfoDao {
	static Logger logger = Logger.getLogger(TaskinfoDaoImpl.class);

	DataSourceManager dataSourceManager;
	@Override
	public Taskinfo add(Taskinfo taskinfo) {
        long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_TASK_INFO(" +
					"ID," +
					"CREATEDATE," +
					"FENQU," +
					
					"USERNAME," +
					"GAMELEVEL," +
					"TASKNAME," +
					
					"TASKTYPE," +
					"SATUS," +
					"GETYOUXIBI," +
					"GETWUPIN," +
					"GETDAOJU,"+
					"AWARD,"+
					
					"TASKGROUP,"+
					"PRETASKGROUP,"+
					"CAREER, "+
					"JIXING "+
					
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,?,? ,?,?,?, ?)");
			
			if(taskinfo.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(taskinfo.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, taskinfo.getFenQu());
			
			ps.setString(3, taskinfo.getUserName());
			ps.setString(4, taskinfo.getGameLevel());
			ps.setString(5, taskinfo.getTaskName());
			
			ps.setString(6, taskinfo.getTaskType());
			ps.setString(7, taskinfo.getStatus());
			ps.setLong(8, taskinfo.getGetYOuXiBi());
			ps.setLong(9, taskinfo.getGetWuPin());
			ps.setLong(10, taskinfo.getGetDaoJu());
			ps.setString(11, taskinfo.getAward());
			
			ps.setString(12, taskinfo.getTaskGroup());
			ps.setString(13, taskinfo.getPreTaskGroup());
			ps.setString(14, taskinfo.getCareer());
			ps.setString(15, taskinfo.getJixing());
			
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[添加任务信息] [OK] "+taskinfo.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (Exception e) {
			   logger.error("[添加任务信息] [FAIL] "+taskinfo.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? taskinfo : null;
	}

	@Override
	public boolean addList(ArrayList<Taskinfo> taskinfoList) {

        boolean result=false;
        long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_TASK_INFO(" +
					"ID," +
					"CREATEDATE," +
					"FENQU," +
					
					"USERNAME," +
					"GAMELEVEL," +
					"TASKNAME," +
					
					"TASKTYPE," +
					"SATUS," +
					"GETYOUXIBI," +
					"GETWUPIN," +
					"GETDAOJU,"+
					"AWARD,"+
					
					"TASKGROUP,"+
					"PRETASKGROUP,"+
					"CAREER, "+
					"JIXING "+
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,? ,? ,?,?,?, ?)");
			for(Taskinfo taskinfo:taskinfoList){
			if(taskinfo.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(taskinfo.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, taskinfo.getFenQu());
			
			ps.setString(3, taskinfo.getUserName());
			ps.setString(4, taskinfo.getGameLevel());
			ps.setString(5, taskinfo.getTaskName());
			
			ps.setString(6, taskinfo.getTaskType());
			ps.setString(7, taskinfo.getStatus());
			ps.setLong(8, taskinfo.getGetYOuXiBi());
			ps.setLong(9, taskinfo.getGetWuPin());
			ps.setLong(10, taskinfo.getGetDaoJu());
			ps.setString(11, taskinfo.getAward());
			
			ps.setString(12, taskinfo.getTaskGroup());
			ps.setString(13, taskinfo.getPreTaskGroup());
			ps.setString(14, taskinfo.getCareer());
			ps.setString(15, taskinfo.getJixing());
			
			ps.executeUpdate();
//			  if(logger.isDebugEnabled()){
//					logger.debug("[添加任务信息] [OK] "+taskinfo.toString()+" ["+taskinfoList.size()+"条信息] [cost:"+(System.currentTimeMillis()-now)+"ms]");
//				}
			  
			}
			  con.commit();
			  con.setAutoCommit(true);
			  result=true;
		    } catch (Exception e) {
			   logger.error("[添加任务信息] [FAIL] ["+taskinfoList.size()+"条信息] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			try {
				if(ps != null) ps.close();
			    if(con != null) { con.close();}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	return result;
	}

	@Override
	public boolean deleteById(Long id) {
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("delete from STAT_TASK_INFO where ID=?");
			ps.setLong(1, id);
			i = ps.executeUpdate();
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
		return i > 0;
	}

	@Override
	public Taskinfo getById(Long id) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from STAT_TASK_INFO where id=?");
			ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			if(rs.next()) {
				Taskinfo taskinfo = new Taskinfo();
				
				taskinfo.setCreateDate(rs.getTimestamp("CREATEDATE"));
				taskinfo.setFenQu(rs.getString("FENQU"));
				taskinfo.setGameLevel(rs.getString("GAMELEVEL"));
				taskinfo.setGetDaoJu(rs.getInt("GETDAOJU"));
				
				taskinfo.setGetWuPin(rs.getInt("GETWUPIN"));
				taskinfo.setGetYOuXiBi(rs.getInt("GETYOUXIBI"));
				taskinfo.setId(rs.getLong("id"));
				
				taskinfo.setStatus(rs.getString("SATUS"));
				taskinfo.setTaskName(rs.getString("TASKNAME"));
				taskinfo.setTaskType(rs.getString("TASKTYPE"));
				taskinfo.setUserName(rs.getString("USERNAME"));
				taskinfo.setAward(rs.getString("AWARD"));
				
				taskinfo.setTaskGroup(rs.getString("TASKGROUP"));
				taskinfo.setPreTaskGroup(rs.getString("PRETASKGROUP"));
				taskinfo.setCareer(rs.getString("CAREER"));
				taskinfo.setJixing(rs.getString("JIXING"));
				
				return taskinfo;
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
		return null;
	}

	@Override
	public List<Taskinfo> getBySql(String sql) {

		Connection con = null;
		PreparedStatement ps = null;
		List<Taskinfo> taskInfoList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				Taskinfo taskinfo = new Taskinfo();
				
				taskinfo.setCreateDate(rs.getTimestamp("CREATEDATE"));
				taskinfo.setFenQu(rs.getString("FENQU"));
				taskinfo.setGameLevel(rs.getString("GAMELEVEL"));
				taskinfo.setGetDaoJu(rs.getInt("GETDAOJU"));
				
				taskinfo.setGetWuPin(rs.getInt("GETWUPIN"));
				taskinfo.setGetYOuXiBi(rs.getInt("GETYOUXIBI"));
				taskinfo.setId(rs.getLong("id"));
				
				taskinfo.setStatus(rs.getString("SATUS"));
				taskinfo.setTaskName(rs.getString("TASKNAME"));
				taskinfo.setTaskType(rs.getString("TASKTYPE"));
				taskinfo.setUserName(rs.getString("USERNAME"));
				taskinfo.setAward(rs.getString("AWARD"));
				
				taskinfo.setTaskGroup(rs.getString("TASKGROUP"));
				taskinfo.setPreTaskGroup(rs.getString("PRETASKGROUP"));
				taskinfo.setCareer(rs.getString("CAREER"));
				taskinfo.setJixing(rs.getString("JIXING"));
				
				taskInfoList.add(taskinfo);
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
		return taskInfoList;
	}

	@Override
	public boolean update(Taskinfo taskinfo) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_TASK_INFO set "+
					"CREATEDATE=?," +
					"FENQU=?," +
					"USERNAME=?," +
					
					"GAMELEVEL=?," +
					"TASKNAME=?," +
					"TASKTYPE=?," +
					
					"SATUS=?," +
					"GETYOUXIBI=?," +
					"GETWUPIN=?," +
					"GETDAOJU=?,"+
					"AWARD=?,"+
					
					"TASKGROUP=?,"+
					"PRETASKGROUP=?,"+
					"CAREER=?, "+
					"JIXING=? "+
					
					" where ID=?");
			if(taskinfo.getCreateDate()!=null){
				ps.setTimestamp(1, new Timestamp(taskinfo.getCreateDate().getTime()));}
			else{
				ps.setTimestamp(1,null);
				}
			ps.setString(2, taskinfo.getFenQu());
			ps.setString(3, taskinfo.getUserName());
			
			ps.setString(4, taskinfo.getGameLevel());
			ps.setString(5,taskinfo.getTaskName());
			ps.setString(6, taskinfo.getTaskType());
			
			ps.setString(7, taskinfo.getStatus());
			ps.setInt(8, taskinfo.getGetYOuXiBi());
			ps.setInt(9, taskinfo.getGetWuPin());
			ps.setInt(10, taskinfo.getGetDaoJu());
			ps.setString(11, taskinfo.getAward());
			
			ps.setString(12, taskinfo.getTaskGroup());
			ps.setString(13, taskinfo.getPreTaskGroup());
			ps.setString(14, taskinfo.getCareer());
			ps.setString(15, taskinfo.getJixing());
		
			ps.setLong(16, taskinfo.getId());
			
			i =ps.executeUpdate();
			
			if(logger.isDebugEnabled()){
				logger.debug("[更新任务信息] [OK] "+taskinfo.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (SQLException e) {
			    logger.error("[更新任务信息] [FAIL] "+taskinfo.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	public boolean updateList(ArrayList<Taskinfo> taskinfoList) {
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		boolean result=true;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("update STAT_TASK_INFO set "+
					"CREATEDATE=?," +
					"FENQU=?," +
					"USERNAME=?," +
					
					"GAMELEVEL=?," +
					"TASKNAME=?," +
					"TASKTYPE=?," +
					
					"SATUS=?," +
					"GETYOUXIBI=?," +
					"GETWUPIN=?," +
					"GETDAOJU=?, "+
					"AWARD=?,"+
					
					"TASKGROUP=?,"+
					"PRETASKGROUP=?,"+
					"CAREER=?, "+
					"JIXING=? "+
					
					" where ID=?");
			for(Taskinfo taskinfo:taskinfoList){
			if(taskinfo.getCreateDate()!=null){
				ps.setTimestamp(1, new Timestamp(taskinfo.getCreateDate().getTime()));}
			else{
				ps.setTimestamp(1,null);
				}
			ps.setString(2, taskinfo.getFenQu());
			ps.setString(3, taskinfo.getUserName());
			
			ps.setString(4, taskinfo.getGameLevel());
			ps.setString(5,taskinfo.getTaskName());
			ps.setString(6, taskinfo.getTaskType());
			
			ps.setString(7, taskinfo.getStatus());
			ps.setInt(8, taskinfo.getGetYOuXiBi());
			ps.setInt(9, taskinfo.getGetWuPin());
			ps.setInt(10, taskinfo.getGetDaoJu());
			ps.setString(11, taskinfo.getAward());
			
			ps.setString(12, taskinfo.getTaskGroup());
			ps.setString(13, taskinfo.getPreTaskGroup());
			ps.setString(14, taskinfo.getCareer());
			ps.setString(15, taskinfo.getJixing());
			
			ps.setLong(16, taskinfo.getId());
			
			i =ps.executeUpdate();
			if(i<=0){result=false;}
			}
			con.setAutoCommit(true);
			con.commit();
		} catch (SQLException e) {
			result=false;
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			    con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 获取任务的接受和完成信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param taskName
	 * @param tasktype
	 * @return
	 */
  public List<String[]> getTaskStat(String startDateStr, String endDateStr, String fenQu,String taskName,String tasktype,String status)
   {
	   String subSql="";
	   if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu = '"+fenQu+"' ";}
	   if(taskName!=null&&!"".equals(taskName)){subSql+=" and t.taskname = '"+taskName+"' ";}
	   if(tasktype!=null&&!"".equals(tasktype)){subSql+=" and t.tasktype = '"+tasktype+"' ";}
	   

	   String jieshousql=" select t.taskname, count(t.id) tasknum" +
	  		" from stat_task_info t where 1=1" +
	  		" and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and " +
	  		" to_date('"+endDateStr+"', 'YYYY-MM-DD') " +subSql+
	  		" group by t.taskname";
	   String wanchengsql=" select t.taskname, count(t.id) tasknum" +
	  		" from stat_task_info t where 1=1" +
	  		" and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and " +
	  		" to_date('"+endDateStr+"', 'YYYY-MM-DD') " +subSql+
	  		" and t.SATUS = '"+status+"' "+
	  		" group by t.taskname";
	   
	   String sql="select jieshou.taskname ,jieshou.tasknum jtasknum,wancheng.tasknum from ("+jieshousql+") jieshou left join ("+wanchengsql+") wancheng on jieshou.taskname=wancheng.taskname";
	   return queryTaskStat(sql);
   }
	
  public ArrayList<String[]>  queryTaskStat(String sql) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[3];
				vales[0]=rs.getString("taskname");
				vales[1]=rs.getString("jtasknum");
				vales[2]=rs.getString("tasknum");
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
  /**
   * 完成任务收益
   * @param startDateStr
   * @param endDateStr
   * @param fenQu
   * @param taskName
   * @param gamelevel
   * @return
   */
  public List<String[]> getTaskShouYi(String startDateStr, String endDateStr, String fenQu,String taskName,String gamelevel)
  {
	   String subSql="";
	   if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu = '"+fenQu+"' ";}
	   if(taskName!=null&&!"".equals(taskName)){subSql+=" and t.taskname = '"+taskName+"' ";}
	   if(gamelevel!=null&&!"".equals(gamelevel)){subSql+=" and t.tasktype =t.gamelevel in ("+gamelevel+") ";}


	   String sql=
	   "select aa.*,bb.wanchengnum from " +
	   " ( select t.taskname," +
	   " count(distinct(t.username)) usernum," +
	   " sum(t.getyouxibi) youxibi," +
	   "  sum(t.getwupin) wupin," +
	   " sum(t.getdaoju) daoju," +
	   " count(t.id) jieshou" +
	   " from stat_task_info t where 1=1 "+subSql+
	     " and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and  to_date('"+endDateStr+"', 'YYYY-MM-DD') "+
	  
	    " group by t.taskname" +
	    " ) aa" +
	    "   left join " +
	    " ( select t.taskname," +
	    "  count(t.id) wanchengnum" +
	    "  from stat_task_info t where 1=1" +subSql+
	    "  and t.satus='完成'" +
	    "  and trunc(t.createdate) between to_date('+startDateStr+', 'YYYY-MM-DD') and to_date('"+endDateStr+"', 'YYYY-MM-DD') " +
	    " group by t.taskname" +
	    "  ) bb" +
	    " on aa.taskname=bb.taskname";
	   
	   return queryTaskShouYi(sql);
  }
  
  private List<String[]> queryTaskShouYi(String sql) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[7];
				
				vales[0]=rs.getString("taskname");
				vales[1]=rs.getString("usernum");
				vales[2]=rs.getString("youxibi");
				vales[3]=rs.getString("wupin");
				vales[4]=rs.getString("daoju");
				vales[5]=rs.getString("jieshou");
				vales[6]=rs.getString("wanchengnum");
				
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

  /**
	 * 获取任务
	 * @param date
	 * @return
	 */
	public ArrayList<String> gettasks(java.util.Date date) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String> taskTypeList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select distinct(t.taskname) taskname from stat_task_info t");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String taskname=rs.getString("taskname");
				if(taskname!=null&&!"".equals(taskname)){
					taskTypeList.add(taskname);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return taskTypeList;
	}
  
  
/**
	 * 获取任务类型
	 * @param date
	 * @return
	 */
	public ArrayList<String> gettaskType(java.util.Date date) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String> taskTypeList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select distinct(t.tasktype) taskType from stat_task_info t");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String taskType=rs.getString("taskType");
				if(taskType!=null&&!"".equals(taskType)){
					taskTypeList.add(taskType);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return taskTypeList;
	}
	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

}
