package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.HuoDonginfoDao;
import com.sqage.stat.commonstat.entity.HuoDonginfo;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class HuoDonginfoDaoImpl implements HuoDonginfoDao {
	static Logger logger = Logger.getLogger(HuoDonginfoDaoImpl.class);
	DataSourceManager dataSourceManager;
	@Override
	public HuoDonginfo add(HuoDonginfo huoDonginfo) {
		long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_HUODONG_INFO(" +
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
					"JIXING"+
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,? ,?,  ?)");
			
			if(huoDonginfo.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(huoDonginfo.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, huoDonginfo.getFenQu());
			
			ps.setString(3, huoDonginfo.getUserName());
			ps.setString(4, huoDonginfo.getGameLevel());
			ps.setString(5, huoDonginfo.getTaskName());
			
			ps.setString(6, huoDonginfo.getTaskType());
			ps.setString(7, huoDonginfo.getStatus());
			ps.setLong(8, huoDonginfo.getGetYOuXiBi());
			ps.setLong(9, huoDonginfo.getGetWuPin());
			ps.setLong(10, huoDonginfo.getGetDaoJu());
			ps.setString(11, huoDonginfo.getAward());
			ps.setString(12, huoDonginfo.getJixing());
			
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[添加活动] [OK] "+huoDonginfo.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (Exception e) {
			   logger.error("[添加活动] [FAIL] "+huoDonginfo.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		   } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? huoDonginfo : null;
	
	
	}
	
	
	public boolean addList(List<HuoDonginfo> ls)
	{
		long now =System.currentTimeMillis();
		boolean result=true;
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_HUODONG_INFO(" +
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
					"JIXING"+
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,? ,?,  ?)");
	  for(int count=0;count<ls.size();count++){
			HuoDonginfo	huoDonginfo=ls.get(count);
			if(huoDonginfo.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(huoDonginfo.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, huoDonginfo.getFenQu());
			
			ps.setString(3, huoDonginfo.getUserName());
			ps.setString(4, huoDonginfo.getGameLevel());
			ps.setString(5, huoDonginfo.getTaskName());
			
			ps.setString(6, huoDonginfo.getTaskType());
			ps.setString(7, huoDonginfo.getStatus());
			ps.setLong(8, huoDonginfo.getGetYOuXiBi());
			ps.setLong(9, huoDonginfo.getGetWuPin());
			ps.setLong(10, huoDonginfo.getGetDaoJu());
			ps.setString(11, huoDonginfo.getAward());
			ps.setString(12, huoDonginfo.getJixing());
			
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){logger.debug("[添加活动] [OK] "+huoDonginfo.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");}
			if(count!=0&&count%100==0){con.commit();}
		   }
	      con.commit();
		  con.setAutoCommit(true);
		    } catch (Exception e) {
		    	result=false;
				logger.error("[添加活动] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    	try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		   } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
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
			ps = con.prepareStatement("delete from STAT_HUODONG_INFO where ID=?");
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
	public HuoDonginfo getById(Long id) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from STAT_HUODONG_INFO where id=?");
			ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			if(rs.next()) {
				HuoDonginfo huoDonginfo = new HuoDonginfo();
				
				huoDonginfo.setCreateDate(rs.getTimestamp("CREATEDATE"));
				huoDonginfo.setFenQu(rs.getString("FENQU"));
				huoDonginfo.setGameLevel(rs.getString("GAMELEVEL"));
				huoDonginfo.setGetDaoJu(rs.getInt("GETDAOJU"));
				
				huoDonginfo.setGetWuPin(rs.getInt("GETWUPIN"));
				huoDonginfo.setGetYOuXiBi(rs.getInt("GETYOUXIBI"));
				huoDonginfo.setId(rs.getLong("id"));
				
				huoDonginfo.setStatus(rs.getString("SATUS"));
				huoDonginfo.setTaskName(rs.getString("TASKNAME"));
				huoDonginfo.setTaskType(rs.getString("TASKTYPE"));
				huoDonginfo.setUserName(rs.getString("USERNAME"));
				huoDonginfo.setAward(rs.getString("AWARD"));
				huoDonginfo.setJixing(rs.getString("JIXING"));
				
				return huoDonginfo;
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
	public List<HuoDonginfo> getBySql(String sql) {

		Connection con = null;
		PreparedStatement ps = null;
		List<HuoDonginfo> huoDonginfoList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				HuoDonginfo huoDonginfo = new HuoDonginfo();
				
				huoDonginfo.setCreateDate(rs.getTimestamp("CREATEDATE"));
				huoDonginfo.setFenQu(rs.getString("FENQU"));
				huoDonginfo.setGameLevel(rs.getString("GAMELEVEL"));
				huoDonginfo.setGetDaoJu(rs.getInt("GETDAOJU"));
				
				huoDonginfo.setGetWuPin(rs.getInt("GETWUPIN"));
				huoDonginfo.setGetYOuXiBi(rs.getInt("GETYOUXIBI"));
				huoDonginfo.setId(rs.getLong("id"));
				
				huoDonginfo.setStatus(rs.getString("SATUS"));
				huoDonginfo.setTaskName(rs.getString("TASKNAME"));
				huoDonginfo.setTaskType(rs.getString("TASKTYPE"));
				huoDonginfo.setUserName(rs.getString("USERNAME"));
				huoDonginfo.setAward(rs.getString("AWARD"));
				huoDonginfo.setJixing(rs.getString("JIXING"));
				
				huoDonginfoList.add(huoDonginfo);
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
		return huoDonginfoList;
	}

	@Override
	public boolean update(HuoDonginfo huoDonginfo) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_HUODONG_INFO set "+
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
					"AWARD=?, "+
					"JIXING=? "+
					
					" where ID=?");
			if(huoDonginfo.getCreateDate()!=null){
				ps.setTimestamp(1, new Timestamp(huoDonginfo.getCreateDate().getTime()));}
			else{
				ps.setTimestamp(1,null);
				}
			ps.setString(2, huoDonginfo.getFenQu());
			ps.setString(3, huoDonginfo.getUserName());
			
			ps.setString(4, huoDonginfo.getGameLevel());
			ps.setString(5,huoDonginfo.getTaskName());
			ps.setString(6, huoDonginfo.getTaskType());
			
			ps.setString(7, huoDonginfo.getStatus());
			ps.setInt(8, huoDonginfo.getGetYOuXiBi());
			ps.setInt(9, huoDonginfo.getGetWuPin());
			ps.setInt(10, huoDonginfo.getGetDaoJu());
		    ps.setString(11,huoDonginfo.getAward());
		    ps.setString(12,huoDonginfo.getJixing());
		    
			ps.setLong(13, huoDonginfo.getId());
			
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[更新活动] [OK] "+huoDonginfo.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (SQLException e) {
			    logger.error("[更新活动] [FAIL] "+huoDonginfo.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	public boolean updateList(ArrayList<HuoDonginfo> huoDonginfoList) {
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		boolean result=true;
		int count=0;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("update STAT_HUODONG_INFO set "+
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
					
					"AWARD=?, "+
					"JIXING=? "+
					" where ID=?");
			
			for(HuoDonginfo huoDonginfo:huoDonginfoList){
			if(huoDonginfo.getCreateDate()!=null){
				ps.setTimestamp(1, new Timestamp(huoDonginfo.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, huoDonginfo.getFenQu());
			ps.setString(3, huoDonginfo.getUserName());
			
			ps.setString(4, huoDonginfo.getGameLevel());
			ps.setString(5,huoDonginfo.getTaskName());
			ps.setString(6, huoDonginfo.getTaskType());
			
			ps.setString(7, huoDonginfo.getStatus());
			ps.setInt(8, huoDonginfo.getGetYOuXiBi());
			ps.setInt(9, huoDonginfo.getGetWuPin());
			ps.setInt(10, huoDonginfo.getGetDaoJu());
			ps.setString(11,huoDonginfo.getAward());
			ps.setString(12,huoDonginfo.getJixing());
			ps.setLong(13, huoDonginfo.getId());
			
			i =ps.executeUpdate();
			if(count!=0&&count%100==0){con.commit();}
			}
			
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
		    try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			result=false;
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.setAutoCommit(true);
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 获取活动的接受和完成信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param taskName
	 * @param tasktype
	 * @return
	 */
  public List<String[]> gethuoDonginfoStat(String startDateStr, String endDateStr, String fenQu,String taskName,String tasktype,String status)
   {
	   String subSql="";
	   if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu = '"+fenQu+"' ";}
	   if(taskName!=null&&!"".equals(taskName)){subSql+=" and t.taskname = '"+taskName+"' ";}
	   if(tasktype!=null&&!"".equals(tasktype)){subSql+=" and t.tasktype = '"+tasktype+"' ";}
	   

	   String jieshousql=" select t.taskname, count(t.id) tasknum" +
	  		" from STAT_HUODONG_INFO t where 1=1" +
	  		" and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and " +
	  		" to_date('"+endDateStr+"', 'YYYY-MM-DD') " +subSql+
	  		" group by t.taskname";
	   String wanchengsql=" select t.taskname, count(t.id) tasknum" +
	  		" from STAT_HUODONG_INFO t where 1=1" +
	  		" and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and " +
	  		" to_date('"+endDateStr+"', 'YYYY-MM-DD') " +subSql+
	  		" and t.SATUS = '"+status+"' "+
	  		" group by t.taskname";
	   
	   String sql="select jieshou.taskname ,jieshou.tasknum jtasknum,wancheng.tasknum from ("+jieshousql+") jieshou left join ("+wanchengsql+") wancheng on jieshou.taskname=wancheng.taskname";
	   return queryhuoDonginfoStat(sql);
   }
	
  public ArrayList<String[]>  queryhuoDonginfoStat(String sql) {

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
   * 完成活动收益
   * @param startDateStr
   * @param endDateStr
   * @param fenQu
   * @param taskName
   * @param gamelevel
   * @return
   */
  public List<String[]> gethuoDonginfoShouYi(String startDateStr, String endDateStr, String fenQu,String taskName,String gamelevel)
  {
	   String subSql="";
	   if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu = '"+fenQu+"' ";}
	   if(taskName!=null&&!"".equals(taskName)){subSql+=" and t.taskname = '"+taskName+"' ";}
	   if(gamelevel!=null&&!"".equals(gamelevel)){subSql+=" and t.gamelevel in ("+gamelevel+") ";}


	   String sql=
	   "select aa.*,bb.wanchengnum from " +
	   " ( select t.taskname," +
	   " count(distinct(t.username)) usernum," +
	   " sum(t.getyouxibi) youxibi," +
	   "  sum(t.getwupin) wupin," +
	   " sum(t.getdaoju) daoju," +
	   " count(t.id) jieshou" +
	   " from STAT_HUODONG_INFO t where 1=1 "+subSql+
	     " and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and  to_date('"+endDateStr+"', 'YYYY-MM-DD') "+
	  
	    " group by t.taskname" +
	    " ) aa" +
	    "   left join " +
	    " ( select t.taskname," +
	    "  count(t.id) wanchengnum" +
	    "  from STAT_HUODONG_INFO t where 1=1" +subSql+
	    "  and t.satus='完成'" +
	    "  and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and to_date('"+endDateStr+"', 'YYYY-MM-DD') " +
	    " group by t.taskname" +
	    "  ) bb" +
	    " on aa.taskname=bb.taskname";
	   return queryhuoDonginfoShouYi(sql);
  }
  
  
  public List<HuoDonginfo> gethuoDonginfoShouYiDetail(String startDateStr, String endDateStr, String fenQu,String taskName,String gamelevel)
  {
	  String subSql="";
	   if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu = '"+fenQu+"' ";}
	   if(taskName!=null&&!"".equals(taskName)){subSql+=" and t.taskname = '"+taskName+"' ";}
	   if(gamelevel!=null&&!"".equals(gamelevel)){subSql+=" and t.gamelevel in ("+gamelevel+") ";}
	   
	   String sql=" select * from STAT_HUODONG_INFO t where 1=1 "+subSql
	             +" and trunc(t.createdate) between" +
	   		      " to_date('"+startDateStr+"', 'YYYY-MM-DD') and  to_date('"+endDateStr+"', 'YYYY-MM-DD') ";
	  return getBySql(sql);
  }
  private List<String[]> queryhuoDonginfoShouYi(String sql) {

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
	 * 获取活动
	 * @param date
	 * @return
	 */
	public ArrayList<String> gethuoDonginfos(java.util.Date date) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String> taskTypeList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select distinct(t.taskname) taskname from STAT_HUODONG_INFO t");
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
	 * 获取活动类型
	 * @param date
	 * @return
	 */
	public ArrayList<String> gethuoDonginfoType(java.util.Date date) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String> taskTypeList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select distinct(t.tasktype) taskType from STAT_HUODONG_INFO t");
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
