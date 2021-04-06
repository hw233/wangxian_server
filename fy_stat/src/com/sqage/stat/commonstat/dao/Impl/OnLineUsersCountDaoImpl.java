package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.OnLineUsersCountDao;
import com.sqage.stat.commonstat.entity.OnLineUsersCount;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class OnLineUsersCountDaoImpl implements OnLineUsersCountDao {
	static Logger logger = Logger.getLogger(OnLineUsersCountDaoImpl.class);

	DataSourceManager dataSourceManager;
	  
	@Override
	public boolean addList(ArrayList<OnLineUsersCount> onLineUsersCountList) {
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		boolean result=true;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into stat_onlineusers(" +
					"ID," +
					"ONLINEDATE," +
					"GAME," +
					
					"FENQU," +
					"USERCOUNT," +
					"QUDAO," +
					"JIXING" +
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?)");
			
			for(OnLineUsersCount onLineUsersCount:onLineUsersCountList){
				if (onLineUsersCount.getOnlineTime() != null) {
					ps.setTimestamp(1, new Timestamp(onLineUsersCount.getOnlineTime().getTime()));
				} else {
					ps.setTimestamp(1, null);
				}
		
			ps.setString(2, onLineUsersCount.getGame());
			ps.setString(3, onLineUsersCount.getFenQu());
			ps.setLong(4, onLineUsersCount.getUserCount());
			ps.setString(5, onLineUsersCount.getQuDao());
			ps.setString(6, onLineUsersCount.getJixing());
			i = ps.executeUpdate();
			if(i<=0){result=false;}
			
		} 
			con.commit();
			con.setAutoCommit(true);
		}catch (SQLException e) {
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
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public OnLineUsersCount add(OnLineUsersCount onLineUsersCount) {
       long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into stat_onlineusers(" +
					"ID," +
					"ONLINEDATE," +
					"GAME," +
					
					"FENQU," +
					"USERCOUNT," +
					"QUDAO," +
					"JIXING" +
					
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?)");
			if (onLineUsersCount.getOnlineTime() != null) {
				ps.setTimestamp(1, new Timestamp(onLineUsersCount.getOnlineTime().getTime()));
			} else {
				ps.setTimestamp(1, null);
			}
			ps.setString(2, onLineUsersCount.getGame());
			ps.setString(3, onLineUsersCount.getFenQu());
			ps.setLong(4, onLineUsersCount.getUserCount());
			ps.setString(5, onLineUsersCount.getQuDao());
			ps.setString(6, onLineUsersCount.getJixing());
			
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[添加在线用户信息] [OK] "+onLineUsersCount.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		  } catch (SQLException e) {
			    logger.error("[添加在线用户信息] [FAIL] "+onLineUsersCount.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		  } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? onLineUsersCount : null;
	}

	@Override
	public boolean deleteById(Long id) {

		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("delete from STAT_OnLineUsersCount where ID=?");
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
	public OnLineUsersCount getById(Long id) {

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from STAT_OnLineUsersCount where id=?");
			ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			if(rs.next()) {
			
				OnLineUsersCount onLineUsersCount = new OnLineUsersCount();
				
				onLineUsersCount.setId(rs.getLong("ID"));
				onLineUsersCount.setFenQu(rs.getString("FENQU"));
				onLineUsersCount.setOnlineTime(rs.getTimestamp("ONLINEDATE"));
				onLineUsersCount.setGame(rs.getString("YOUXI"));
				onLineUsersCount.setUserCount(rs.getLong("USERCOUNT"));
				onLineUsersCount.setQuDao(rs.getString("QUDAO"));
				onLineUsersCount.setJixing(rs.getString("JIXING"));
				return onLineUsersCount;
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
	public List<OnLineUsersCount> getBySql(String sql) {
		
		Connection con = null;
		PreparedStatement ps = null;
		List onLineUserList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			//ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				OnLineUsersCount onLineUsersCount = new OnLineUsersCount();
				
				onLineUsersCount.setId(rs.getLong("ID"));
				onLineUsersCount.setFenQu(rs.getString("FENQU"));
				onLineUsersCount.setOnlineTime(rs.getTimestamp("ONLINEDATE"));
				onLineUsersCount.setGame(rs.getString("GAME"));
				onLineUsersCount.setUserCount(rs.getLong("USERCOUNT"));
				onLineUsersCount.setQuDao(rs.getString("QUDAO"));
				onLineUsersCount.setJixing(rs.getString("JIXING"));
				
				onLineUserList.add(onLineUsersCount);
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
		return onLineUserList;
	}

	@Override
	public boolean update(OnLineUsersCount onLineUsersCount) {
		long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_OnLineUsersCount set "+
					"ONLINETIME=?," +
					"GAME=?," +
					"FENQU=?," +
					"USERCOUNT=?," +
					"QUDAO=?, " +
					"JIXING=? " +
					
					" where ID=?");
			if (onLineUsersCount.getOnlineTime() != null) {
				ps.setTimestamp(1, new Timestamp(onLineUsersCount.getOnlineTime().getTime()));
			} else {
				ps.setDate(1, null);
			}
			ps.setString(2, onLineUsersCount.getGame());
			ps.setString(3, onLineUsersCount.getFenQu());
			
			ps.setLong(4,onLineUsersCount.getUserCount());
			ps.setString(5,onLineUsersCount.getQuDao());
			
			ps.setString(6,onLineUsersCount.getJixing());
			ps.setLong(7, onLineUsersCount.getId());
			
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[更新在线用户信息] [OK] "+onLineUsersCount.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (SQLException e) {
			    logger.error("[更新在线用户信息] [FAIL] "+onLineUsersCount.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	 *平均每分钟在线人数
	 * @param dateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getAvgOnlineUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing){
		ArrayList<String> levelUserCount=new ArrayList<String>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		Long value=null;
		String subSql="";
		if(qudao!=null&&!qudao.isEmpty()){subSql+=" and t.qudao in ('"+qudao+"')";}
		if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";}
		if(jixing!=null&&!jixing.isEmpty()) {subSql+=" and t.jixing='"+jixing+"'";}
		
		String sql= "select avg(mm.count) count from ( select trunc(t.onlinedate,'mi'), " +
				"sum(t.usercount) count  from stat_onlineusers t where 1 = 1 " +subSql+
				" and t.onlinedate between to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and" +
				" to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') and t.qudao !='所有渠道'" +
				"  group by trunc(t.onlinedate,'mi')) mm" ;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			if(rs.next()){
			value=rs.getLong("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	 *最高在线人数
	 * @param dateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getMaxOnlineUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing){
		ArrayList<String> levelUserCount=new ArrayList<String>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		Long value=null;
		String subSql="";
		if(qudao!=null&&!qudao.isEmpty()){subSql+=" and t.qudao in ('"+qudao+"')";}
		if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";}
		if(jixing!=null&&!jixing.isEmpty()) {subSql+=" and t.jixing='"+jixing+"'";}

		String sql="select max(mm.count) count from (	select trunc(t.onlinedate,'mi'), sum(t.usercount) count" +
				"  from stat_onlineusers t" +
				"	 where 1 = 1" +subSql+
				"   and trunc(t.onlinedate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and" +
				"   to_date('"+endDateStr+"', 'YYYY-MM-DD') and t.qudao !='所有渠道' " +
				" group by trunc(t.onlinedate,'mi')) mm";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			if(rs.next()){
			value=rs.getLong("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	
	
	@Override
	public boolean updateChongZhiQuDao(String oldQuDao, String userName, String newQuDao) {
		long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update stat_chongzhi set "+
					"qudao=? " +
					" where username=? and qudao=?");
			ps.setString(1, newQuDao);
			ps.setString(2, userName);
			ps.setString(3,oldQuDao);
			
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[更新充值渠道信息] [OK] [oldQuDao] ["+oldQuDao+"] [newQuDao] ["+newQuDao+"] [userName] ["+userName+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (SQLException e) {
		    	i=-1;
			    logger.error("[更新在线用户信息] [FAIL] [oldQuDao] ["+oldQuDao+"] [newQuDao] ["+newQuDao+"] [userName] ["+userName+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i >= 0;
	}

	@Override
	public boolean updatePlayGameQuDao(String oldQuDao, String userName, String newQuDao) {

		long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update stat_playgame set "+
					"qudao=? " +
					" where username=? and qudao=?");
			ps.setString(1, newQuDao);
			ps.setString(2, userName);
			ps.setString(3,oldQuDao);
			
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[更新进入游戏用户渠道信息] [OK] [oldQuDao] ["+oldQuDao+"] [newQuDao] ["+newQuDao+"] [userName] ["+userName+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (SQLException e) {
		    	i=-1;
			    logger.error("[更新进入游戏用户渠道信息] [FAIL] [oldQuDao] ["+oldQuDao+"] [newQuDao] ["+newQuDao+"] [userName] ["+userName+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i >= 0;
	}

	@Override
	public boolean updateRegistQuDao(String oldQuDao, String userName, String newQuDao) {
		long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update stat_user set "+
					"qudao=? " +
					" where name=? and qudao=?");
			ps.setString(1, newQuDao);
			ps.setString(2, userName);
			ps.setString(3,oldQuDao);
			
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
			
				logger.debug("[更新用户注册渠道信息] [OK] [oldQuDao] ["+oldQuDao+"] [newQuDao] ["+newQuDao+"] [userName] ["+userName+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (SQLException e) {
		    	i=-1;
			    logger.error("[更新用户注册渠道信息] [FAIL] [oldQuDao] ["+oldQuDao+"] [newQuDao] ["+newQuDao+"] [userName] ["+userName+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i >= 0;
	}

	@Override
	public ArrayList<String[]> getOnlineInfo(String sql, String[] columusEnums) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[columusEnums.length];
				for(int i=0;i<columusEnums.length;i++){
					vales[i]=rs.getString(columusEnums[i]);
				}
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
