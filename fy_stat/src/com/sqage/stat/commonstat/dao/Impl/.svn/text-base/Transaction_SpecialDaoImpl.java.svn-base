package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.sqage.stat.commonstat.dao.Transaction_SpecialDao;
import com.sqage.stat.commonstat.entity.Transaction_Special;
import com.sqage.stat.commonstat.entity.Transaction_yinzi;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class Transaction_SpecialDaoImpl implements Transaction_SpecialDao {
	static Logger logger = Logger.getLogger(Transaction_SpecialDaoImpl.class);
	DataSourceManager dataSourceManager;
	@Override
	public Transaction_Special add(Transaction_Special transaction) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_TRANSACTION_SPECIAL(" +
					"ID," +
					"CREATEDATE," +
					"FENQU," +
					"TRANSACTIONTYPE," +
					
					"FDAOJUNAME," +
					"FUSERNAME," +
					"FYINZI," +
					"FUUID," +
					"FQUDAO," +
					"FPLAYERNAME,"+
					"FJIAZHI,"+
					
					"TODAOJUNAME,"+
					"TOUSERNAME,"+
					"TOYINZI,"+
					"TOUUID,"+
					"TOQUDAO,"+
					"TOPLAYERNAME,"+
					"TOJIAZHI,"+
					
					"FREGISTTIME,"+
					"FCREATPLAYERTIME,"+
					"FLEVEL,"+
					"FVIP,"+
					"FTOTALMONEY,"+
					
					"TOREGISTTIME,"+
					"TOCREATPLAYERTIME,"+
					"TOLEVEL,"+
					"TOVIP,"+
					"TOTOTALMONEY"+
					
					") values (SEQ_STAT_TRANSACTION.NEXTVAL,?,?,? ,?,?,?,?,?,?,? ,?,?,?,?,?,?,?  ,?,?,?,?,?, ?,?,?,?,?)");
			
			
			if(transaction.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(transaction.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, transaction.getFenQu());
			ps.setString(3, transaction.getTransactionType());
			
			ps.setString(4, transaction.getFdaoJuName());
			ps.setString(5, transaction.getFuserName());
			ps.setLong(6, transaction.getFyinzi());
			ps.setString(7, transaction.getFuuId());
			ps.setString(8, transaction.getFquDao());
			ps.setString(9, transaction.getFplayerName());
			ps.setLong(10, transaction.getFjiazhi());
			
			ps.setString(11, transaction.getTodaoJuName());
			ps.setString(12, transaction.getToUserName());
			ps.setLong(13, transaction.getToyinzi());
			ps.setString(14, transaction.getTouuId());
			ps.setString(15, transaction.getToquDao());
			ps.setString(16, transaction.getToPlayername());
			ps.setLong(17, transaction.getTojiazhi());
			
			
			ps.setTimestamp(18, new Timestamp(transaction.getfRegisttime().getTime()));
			ps.setTimestamp(19, new  Timestamp(transaction.getfCreatPlayerTime().getTime()));
			ps.setString(20, transaction.getfLevel());
			ps.setString(21, transaction.getfVip());
			ps.setLong(22, transaction.getfTotalMoney());
	
			ps.setTimestamp(23, new Timestamp(transaction.getToRegisttime().getTime()));
			ps.setTimestamp(24, new Timestamp(transaction.getToCreatPlayerTime().getTime()));
			ps.setString(25,transaction.getToLevel());
			ps.setString(26, transaction.getToVip());
			ps.setLong(27, transaction.getToTotalMoney());
			  
			i = ps.executeUpdate();
			logger.info("[添加异常交易信息] [OK] "+transaction.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			
		
		    } catch (Exception e) {
		    	logger.error("[添加异常交易信息] [FAIL] "+transaction.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		    } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? transaction : null;
	}

	@Override
	public List<Transaction_Special> getBySql(String sql) {

		Connection con = null;
		PreparedStatement ps = null;
		List<Transaction_Special> transactionList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				Transaction_Special transaction=new Transaction_Special();
				
				transaction.setCreateDate(rs.getTimestamp("CREATEDATE"));
				transaction.setFdaoJuName(rs.getString("FDAOJUNAME"));
				transaction.setFenQu(rs.getString("FENQU"));
				transaction.setFjiazhi(rs.getLong("FJIAZHI"));
				transaction.setFplayerName(rs.getString("FPLAYERNAME"));
				transaction.setFquDao(rs.getString("FQUDAO"));
				transaction.setFuserName(rs.getString("FUSERNAME"));
				transaction.setFuuId(rs.getString("FUUID"));
				transaction.setFyinzi(rs.getLong("FYINZI"));
				transaction.setId(rs.getLong("id"));
				
				transaction.setTodaoJuName(rs.getString("TODAOJUNAME"));
				transaction.setTojiazhi(rs.getLong("TOJIAZHI"));
				transaction.setToPlayername(rs.getString("TOPLAYERNAME"));
				transaction.setToquDao(rs.getString("TOQUDAO"));
				transaction.setToUserName(rs.getString("TOUSERNAME"));
				transaction.setTouuId(rs.getString("TOUUID"));
				transaction.setToyinzi(rs.getLong("TOYINZI"));
				transaction.setTransactionType(rs.getString("TRANSACTIONTYPE"));
				
				transaction.setfRegisttime(rs.getTimestamp("FREGISTTIME"));
				transaction.setfCreatPlayerTime(rs.getTimestamp("FCREATPLAYERTIME"));
				transaction.setfLevel(rs.getString("FLEVEL"));
				transaction.setfVip(rs.getString("FVIP"));
				transaction.setfTotalMoney(rs.getLong("FTOTALMONEY"));
				
				transaction.setToRegisttime(rs.getTimestamp("TOREGISTTIME"));
				transaction.setToCreatPlayerTime(rs.getTimestamp("TOCREATPLAYERTIME"));
				transaction.setToLevel(rs.getString("TOLEVEL"));
				transaction.setToVip(rs.getString("TOVIP"));
				transaction.setToTotalMoney(rs.getLong("TOTOTALMONEY"));
				
				transactionList.add(transaction);
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
		return transactionList;
	}

	/**
	 * 执行sql
	 */
	@Override
	public boolean excute(String sql) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			   ps = con.prepareStatement(sql);
			   i =ps.executeUpdate();
			   con.commit();
			   con.setAutoCommit(true);
			   logger.info("[数据库定时执行sql:] [OK] "+sql+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			   //System.out.println("[数据库定时执行sql:] [OK] "+sql+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			   
		} catch (SQLException e) {
				logger.error("[数据库定时执行sql:] [FAIL] "+sql+" [cost:"+(System.currentTimeMillis()-now)+"ms]"+e.getStackTrace(),e);
				e.printStackTrace(System.out);  
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
	public List<Transaction_yinzi> getTransaction_yinziBySql(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		List<Transaction_yinzi> transaction_yinziList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				Transaction_yinzi transaction_yinzi=new Transaction_yinzi();
				
				transaction_yinzi.setAction(rs.getString("ACTION"));
				transaction_yinzi.setCreateDate(rs.getString("CREATEDATE"));
				transaction_yinzi.setFenQu(rs.getString("FENQU"));
				transaction_yinzi.setMoney(rs.getLong("MONEY"));
				transaction_yinzi.setReasonType(rs.getString("REASONTYPE"));
				transaction_yinziList.add(transaction_yinzi);
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
		return transaction_yinziList;
	}
	
	
	
	
	@Override
	public List<String[]> getTransactionSpec_Total(String startDateStr, String endDateStr,String fenQu) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		String subSql="";
		if(fenQu!=null){ subSql= fenQu ;}
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			String sql=" select to_char(t.createdate,'YYYY-MM-DD') datestr,sum( case when t.fyinzi > t.toyinzi then t.fyinzi else t.toyinzi end ) total," +
					" sum(t.fyinzi) fyinzi,sum(t.toyinzi) toyinzi from stat_transaction_special t " +
					" where t.createdate between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and " +
					" to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +subSql+
							" group by to_char(t.createdate,'YYYY-MM-DD') " +
					" order by to_char(t.createdate,'YYYY-MM-DD')";

			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [4];
				
				String day=rs.getString("datestr");
				String total=rs.getString("total");
				String fyinzi=rs.getString("fyinzi");
				String toyinzi=rs.getString("toyinzi");
				
				strs[0]=day;
				strs[1]=total;
				strs[2]=fyinzi;
				strs[3]=toyinzi;
				ls.add(strs);
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
		return ls;
	}
	
	@Override
	public List<String[]> getTransactionSpec_Total_fenQu2(String startDateStr, String endDateStr,String fenQu) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		String subSql="";
		if(fenQu!=null){ subSql=fenQu;}
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			String sql=" select to_char(t.createdate,'YYYY-MM-DD') datestr,t.fenqu,sum( case when t.fyinzi > t.toyinzi then t.fyinzi else t.toyinzi end ) total " +
					" from stat_transaction_special t " +
					" where t.createdate between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
					" and to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +subSql+
					" group by to_char(t.createdate,'YYYY-MM-DD'),t.fenqu";

			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [4];
				
				String day=rs.getString("datestr");
				String total=rs.getString("fenqu");
				String fyinzi=rs.getString("total");
				
				strs[0]=day;
				strs[1]=total;
				strs[2]=fyinzi;
				ls.add(strs);
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
		return ls;
	}
	
	

	@Override
	public List<String[]> getTransactionSpec_Total_fenqu(String startDateStr, String endDateStr, String fenQu) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		String subSql="";
		
		try {
			con = dataSourceManager.getInstance().getConnection();
//			String sql=" select t.fenqu fenqu,sum( case when t.fyinzi > t.toyinzi then t.fyinzi else t.toyinzi end ) total," +
//					" sum(t.fyinzi) fyinzi,sum(t.toyinzi) toyinzi from stat_transaction_special t " +
//					" where t.createdate between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and " +
//					" to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') group by t.fenqu " ;
			
			
			String sql=" select fenq.name fenqu,mm.total,mm.fyinzi,mm.toyinzi from (select f.name from stat_fenqu f order by f.seq) fenq " +
					"  left join " +
					" ( " +
					" select t.fenqu fenqu,sum( case when t.fyinzi > t.toyinzi then t.fyinzi else t.toyinzi end ) total," +
					"  sum(t.fyinzi) fyinzi,sum(t.toyinzi) toyinzi from stat_transaction_special t  " +
					"  where t.createdate between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and " +
					" to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') group by t.fenqu ) mm  on fenq.name=mm.fenqu";

			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [4];
				
				String day=rs.getString("fenqu");
				String total=rs.getString("total");
				String fyinzi=rs.getString("fyinzi");
				String toyinzi=rs.getString("toyinzi");
				
				strs[0]=day;
				strs[1]= (total==null ?"0":total);
				strs[2]= (fyinzi==null ?"0":fyinzi);
				strs[3]= (toyinzi==null ?"0":toyinzi);
				ls.add(strs);
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
		return ls;
	}

	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}
	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

}
