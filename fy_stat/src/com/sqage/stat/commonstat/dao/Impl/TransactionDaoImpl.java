package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.TransactionDao;
import com.sqage.stat.commonstat.entity.DaoJu;
import com.sqage.stat.commonstat.entity.Transaction;
import com.sqage.stat.language.MultiLanguageManager;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class TransactionDaoImpl implements TransactionDao {
	static Logger logger = Logger.getLogger(PlayGameDaoImpl.class);
	DataSourceManager dataSourceManager;
	@Override
	public Transaction add(Transaction transaction) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_TRANSACTION(" +
					"ID," +
					"CREATEDATE," +
					"FENQU," +
					
					"TRANSACTIONTYPE," +
					"DAOJUNAME," +
					"DANJIA," +
					
					"DAOJUNUM," +
					"FUSERNAME," +
					"FGAMELEVEL," +
					"TOUSERNAME,"+
					"TOGAMELEVEL,"+
					
					"DAOJUCOLOR,"+
					"DAOJULEVEL,"+
					"BINDTYPE,"+
					"JIXING,"+
					
					"FVIP,"+
					"TOVIP,"+
					"FGUOJIA,"+
					"TOGUOJIA"+
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,?  ,?,?,?, ?,?,?,?, ?)");
			
			if(transaction.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(transaction.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, transaction.getFenQu());
			
			ps.setString(3, transaction.getTransactionType());
			ps.setString(4, transaction.getDaoJuName());
			ps.setInt(5, transaction.getDanjia());
			
			ps.setInt(6, transaction.getDaojunum());
			ps.setString(7, transaction.getFuserName());
			ps.setString(8, transaction.getFgameLevel());
			ps.setString(9, transaction.getToUserName());
			ps.setString(10, transaction.getToGameLevel());
			
			ps.setString(11, transaction.getDaoJuColor());
			ps.setString(12, transaction.getDaoJuLevel());
			ps.setString(13, transaction.getBindType());
			ps.setString(14, transaction.getJixing());
			
			ps.setString(15, transaction.getFvip());
			ps.setString(16, transaction.getTovip());
			ps.setString(17, transaction.getFguoJia());
			ps.setString(18, transaction.getToguoJia());
			
			i = ps.executeUpdate();
			
			if(logger.isDebugEnabled()){
				logger.debug("[添加交换道具信息] [OK] "+transaction.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (Exception e) {
		    	logger.error("[添加交换道具信息] [FAIL] "+transaction.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	public void addList(ArrayList<Transaction> transactionList) {
        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_TRANSACTION(" +
					"ID," +
					"CREATEDATE," +
					"FENQU," +
					
					"TRANSACTIONTYPE," +
					"DAOJUNAME," +
					"DANJIA," +
					
					"DAOJUNUM," +
					"FUSERNAME," +
					"FGAMELEVEL," +
					"TOUSERNAME,"+
					"TOGAMELEVEL,"+
					
					"DAOJUCOLOR,"+
					"DAOJULEVEL,"+
					"BINDTYPE,"+
					"JIXING,"+
					
					"FVIP,"+
					"TOVIP,"+
					"FGUOJIA,"+
					"TOGUOJIA"+
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,?  ,?,?,?, ?,?,?,?, ?)");
		    int i = -1;
			int count=0;
			
		for(Transaction transaction : transactionList){
			if(transaction.getCreateDate()!=null){
				ps.setTimestamp(1, new Timestamp(transaction.getCreateDate().getTime()));}
				else{ ps.setTimestamp(1,null);	}
				ps.setString(2, transaction.getFenQu());
				
				ps.setString(3, transaction.getTransactionType());
				ps.setString(4, transaction.getDaoJuName());
				ps.setInt(5, transaction.getDanjia());
				
				ps.setInt(6, transaction.getDaojunum());
				ps.setString(7, transaction.getFuserName());
				ps.setString(8, transaction.getFgameLevel());
				ps.setString(9, transaction.getToUserName());
				ps.setString(10, transaction.getToGameLevel());
				
				ps.setString(11, transaction.getDaoJuColor());
				ps.setString(12, transaction.getDaoJuLevel());
				ps.setString(13, transaction.getBindType());
				ps.setString(14, transaction.getJixing());
				
				ps.setString(15, transaction.getFvip());
				ps.setString(16, transaction.getTovip());
				ps.setString(17, transaction.getFguoJia());
				ps.setString(18, transaction.getToguoJia());
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%500==0){ con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			if(logger.isDebugEnabled()){
				logger.debug("开始批量插入交易数据"+transactionList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");
				}
			logger.info("开始批量插入交易数据"+transactionList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");
			
		} catch (Exception e) {
			logger.error("开始批量插入交易数据",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[添加批量插入交易数据异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[添加批量插入交易数据] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean deleteById(Long id) {

		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("delete from STAT_TRANSACTION where ID=?");
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
	public Transaction getById(Long id) {

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from STAT_TRANSACTION where id=?");
			ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			if(rs.next()) {
				Transaction transaction=new Transaction();
				
				transaction.setCreateDate(rs.getTimestamp("CREATEDATE"));
				transaction.setDanjia(rs.getInt("DANJIA"));
				transaction.setDaoJuName(rs.getString("DAOJUNAME"));
				transaction.setDaojunum(rs.getInt("DAOJUNUM"));
				
				transaction.setFenQu(rs.getString("FENQU"));
				transaction.setFgameLevel(rs.getString("FGAMELEVEL"));
				transaction.setFuserName(rs.getString("FUSERNAME"));
				transaction.setId(id);
				transaction.setToGameLevel(rs.getString("TOUSERNAME"));
				
				transaction.setToUserName(rs.getString("TOGAMELEVEL"));
				transaction.setTransactionType(rs.getString("TRANSACTIONTYPE"));
				
				transaction.setDaoJuColor(rs.getString("DAOJUCOLOR"));
				transaction.setDaoJuLevel(rs.getString("DAOJULEVEL"));
				transaction.setBindType(rs.getString("BINDTYPE"));
				transaction.setJixing(rs.getString("JIXING"));
				
				transaction.setJixing(rs.getString("FVIP"));
				transaction.setTovip(rs.getString("TOVIP"));
				transaction.setToguoJia(rs.getString("FGUOJIA"));
				transaction.setToguoJia(rs.getString("TOGUOJIA"));
				
				return transaction;
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
	public List<Transaction> getBySql(String sql) {

		Connection con = null;
		PreparedStatement ps = null;
		List<Transaction> transactionList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				Transaction transaction=new Transaction();
				
				transaction.setCreateDate(rs.getTimestamp("CREATEDATE"));
				transaction.setDanjia(rs.getInt("DANJIA"));
				transaction.setDaoJuName(rs.getString("DAOJUNAME"));
				transaction.setDaojunum(rs.getInt("DAOJUNUM"));
				
				transaction.setFenQu(rs.getString("FENQU"));
				transaction.setFgameLevel(rs.getString("FGAMELEVEL"));
				transaction.setFuserName(rs.getString("FUSERNAME"));
				transaction.setId(rs.getLong("ID"));
				transaction.setToGameLevel(rs.getString("TOUSERNAME"));
				
				transaction.setToUserName(rs.getString("TOGAMELEVEL"));
				transaction.setTransactionType(rs.getString("TRANSACTIONTYPE"));
				
				transaction.setDaoJuColor(rs.getString("DAOJUCOLOR"));
				transaction.setDaoJuLevel(rs.getString("DAOJULEVEL"));
				transaction.setBindType(rs.getString("BINDTYPE"));
				transaction.setJixing(rs.getString("JIXING"));
				transaction.setJixing(rs.getString("FVIP"));
				transaction.setTovip(rs.getString("TOVIP"));
				transaction.setToguoJia(rs.getString("FGUOJIA"));
				transaction.setToguoJia(rs.getString("TOGUOJIA"));
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

	@Override
	public boolean update(Transaction transaction) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_TRANSACTION set "+
					"CREATEDATE=?," +
					"FENQU=?," +
					
					"TRANSACTIONTYPE=?," +
					"DAOJUNAME=?," +
					"DANJIA=?," +
					
					"DAOJUNUM=?," +
					"FUSERNAME=?," +
					"FGAMELEVEL=?," +
					"TOUSERNAME=?,"+
					"TOGAMELEVEL=?,"+
					
					"DAOJUCOLOR=?,"+
					"DAOJULEVEL=?,"+
					"BINDTYPE=?,"+
					"JIXING=?,"+
					
					"FVIP=?,"+
					"TOVIP=?,"+
					"FGUOJIA=?,"+
					"TOGUOJIA=?"+
				
					" where ID=?");
			if(transaction.getCreateDate()!=null){
			ps.setTimestamp(1,  new Timestamp(transaction.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, transaction.getFenQu());
			
			ps.setString(3, transaction.getTransactionType());
			ps.setString(4, transaction.getDaoJuName());
			ps.setInt(5,transaction.getDanjia());
			
			ps.setInt(6, transaction.getDaojunum());
			ps.setString(7, transaction.getFuserName());
			ps.setString(8, transaction.getFgameLevel());
			ps.setString(9, transaction.getToUserName());
			ps.setString(10, transaction.getToGameLevel());
			
			ps.setString(11, transaction.getDaoJuColor());
			ps.setString(12, transaction.getDaoJuLevel());
			ps.setString(13, transaction.getBindType());
			ps.setString(14, transaction.getJixing());
			
			ps.setString(15, transaction.getFvip());
			ps.setString(16, transaction.getTovip());
			ps.setString(17, transaction.getFguoJia());
			ps.setString(18, transaction.getToguoJia());
			
			ps.setLong(19, transaction.getId());
			i =ps.executeUpdate();
			

			if(logger.isDebugEnabled()){
				logger.debug("[更新交换道具信息] [OK] "+transaction.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (SQLException e) {
				logger.error("[更新交换道具信息] [FAIL] "+transaction.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	public boolean updateList(ArrayList<Transaction> transactionList) {
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		boolean result=true;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("update STAT_TRANSACTION set "+
					"CREATEDATE=?," +
					"FENQU=?," +
					
					"TRANSACTIONTYPE=?," +
					"DAOJUNAME=?," +
					"DANJIA=?," +
					
					"DAOJUNUM=?," +
					"FUSERNAME=?," +
					"FGAMELEVEL=?," +
					"TOUSERNAME=?,"+
					"TOGAMELEVEL=?"+
					
					"DAOJUCOLOR=?,"+
					"DAOJULEVEL=?,"+
					"BINDTYPE=?,"+
					
					"DAOJUCOLOR=?,"+
					"DAOJULEVEL=?,"+
					"BINDTYPE=?,"+
					"JIXING=?,"+

					"FVIP=?,"+
					"TOVIP=?,"+
					"FGUOJIA=?,"+
					"TOGUOJIA=?"+
					
					" where ID=?");
			for(Transaction transaction:transactionList){
			
			if(transaction.getCreateDate()!=null){
			ps.setTimestamp(1,  new Timestamp(transaction.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, transaction.getFenQu());
			
			ps.setString(3, transaction.getTransactionType());
			ps.setString(4, transaction.getDaoJuName());
			ps.setInt(5,transaction.getDanjia());
			
			ps.setInt(6, transaction.getDaojunum());
			ps.setString(7, transaction.getFuserName());
			ps.setString(8, transaction.getFgameLevel());
			ps.setString(9, transaction.getToUserName());
			ps.setString(10, transaction.getToGameLevel());
			
			ps.setString(11, transaction.getDaoJuColor());
			ps.setString(12, transaction.getDaoJuLevel());
			ps.setString(13, transaction.getBindType());
            ps.setString(14, transaction.getJixing());
			
            ps.setString(15, transaction.getFvip());
			ps.setString(16, transaction.getTovip());
			ps.setString(17, transaction.getFguoJia());
			ps.setString(18, transaction.getToguoJia());
			
			ps.setLong(19, transaction.getId());
			i =ps.executeUpdate();
			if(i<=0){result=false;}
			}
			con.commit();
			con.setAutoCommit(true);
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
	 * 获取交易类型
	 * @param date
	 * @return
	 */
	public ArrayList<String> getTransactiontype(java.util.Date date) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String> transactiontypeList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select distinct(t.transactiontype) transactiontype from stat_transaction t");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String transactiontype=rs.getString("transactiontype");
				if(transactiontype!=null&&!"".equals(transactiontype)){
					transactiontypeList.add(transactiontype);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			    con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return transactiontypeList;
	}
	@Override
	public ArrayList<String[]> getDaoJuJiaoYi(String startDateStr, String endDateStr, String fenQu) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		String 放入拍卖=MultiLanguageManager.translateMap.get("放入拍卖");
		String 购买拍卖成功=MultiLanguageManager.translateMap.get("购买拍卖成功");
		String 快速出售=MultiLanguageManager.translateMap.get("快速出售");
		String 摆摊出售=MultiLanguageManager.translateMap.get("摆摊出售");
		
        String subSql="";
        if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu='"+fenQu+"'";}
		String sql=" select to_char(t.createdate,'YYYY-MM-DD') datestr," +
				" sum(case when  t.transactiontype in ('"+放入拍卖+"','"+购买拍卖成功+"') then t.daojunum  else 0 end ) fabu," +
				" sum(case when  t.transactiontype in ('"+购买拍卖成功+"','"+快速出售+"','"+摆摊出售+"')  then  t.daojunum  else 0 end ) jiaoyi ," +
				" sum(case when  t.transactiontype in ('"+购买拍卖成功+"','"+快速出售+"','"+摆摊出售+"')  then  t.daojunum*t.danjia  else 0 end ) jine " +
				" from stat_transaction t where to_char(t.createdate,'YYYY-MM-DD') between '"+startDateStr+"' and '"+endDateStr +"'"+ subSql+
				" group by to_char(t.createdate,'YYYY-MM-DD') order by to_char(t.createdate,'YYYY-MM-DD')";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[4];
				vales[0]=rs.getString("datestr");
				vales[1]=rs.getString("fabu");
				vales[2]=rs.getString("jiaoyi");
				vales[3]=rs.getString("jine");
				
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

	public ArrayList<String[]> getJiuHuiZong(String startDateStr,String jiuName) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		String sql="select fenq.name,jiu.num,jiu.jine,jiu.jiage from " +
				" (select * from stat_fenqu f order by f.seq) fenq " +
				" left join ( select  t.fenqu,sum(t.daojunum) num,sum(t.danjia*t.daojunum) jine,round(sum(t.danjia*t.daojunum)/sum(t.daojunum)) jiage " +
				" from stat_transaction t where t.transactiontype='快速出售' and t.daojucolor='白色'  " +
				" and t.daojuname = '"+jiuName+"' and to_char(t.createdate,'YYYY-MM-DD')='"+startDateStr+"' " +
				" group by t.fenqu) jiu on fenq.name=jiu.fenqu";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[4];
				vales[0]=rs.getString("name");
				vales[1]=rs.getString("num");
				vales[2]=rs.getString("jine");
				vales[3]=rs.getString("jiage");
				
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
	
	@Override
	public ArrayList<String[]> getJiaoYiHuiZong(String startDateStr) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		String sql="select  t.fenqu,t.transactiontype,t.daojucolor, to_char(t.createdate,'YYYY-MM-DD') createdate," +
				" t.daojuname,sum(t.daojunum) daojunum,sum(t.danjia) totalMoney " +
				" from stat_transaction t where to_char(t.createdate,'YYYY-MM-DD')='"+startDateStr+"' " +
				" group by t.fenqu,t.transactiontype,t.daojucolor, to_char(t.createdate,'YYYY-MM-DD'),t.daojuname";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[7];
				vales[0]=rs.getString("fenqu");
				vales[1]=rs.getString("transactiontype");
				vales[2]=rs.getString("daojucolor");
				vales[3]=rs.getString("createdate");
				vales[4]=rs.getString("daojuname");
				vales[5]=rs.getString("daojunum");
				vales[6]=rs.getString("totalMoney");
				
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

	@Override
	public ArrayList<String[]> getDaoJuGetType(String startDateStr, String endDateStr, String fenQu) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		
		String 放入拍卖=MultiLanguageManager.translateMap.get("放入拍卖");
		String 购买拍卖成功=MultiLanguageManager.translateMap.get("购买拍卖成功");
		String 快速出售=MultiLanguageManager.translateMap.get("快速出售");
		String 摆摊出售=MultiLanguageManager.translateMap.get("摆摊出售");
        String subSql="";
        if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu='"+fenQu+"'";}
		String sql=" select t.transactiontype," +
				" sum(case when  t.transactiontype in ('"+放入拍卖+"','"+购买拍卖成功+"') then t.daojunum  else 0 end ) fabu," +
				" sum(case when  t.transactiontype in ('"+购买拍卖成功+"','"+快速出售+"','"+摆摊出售+"')  then  t.daojunum  else 0 end ) jiaoyi ," +
				" sum(case when  t.transactiontype in ('"+购买拍卖成功+"','"+快速出售+"','"+摆摊出售+"')  then  t.daojunum*t.danjia  else 0 end ) jine " +
				" from stat_transaction t where to_char(t.createdate,'YYYY-MM-DD') between '"+startDateStr+"' and '"+endDateStr +"'"+ subSql+
				" group by t.transactiontype";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[4];
				vales[0]=rs.getString("transactiontype");
				vales[1]=rs.getString("fabu");
				vales[2]=rs.getString("jiaoyi");
				vales[3]=rs.getString("jine");
				
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

	@Override
	public ArrayList<String[]> getDaoJuName(String startDateStr, String endDateStr, String fenQu) {


		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		
		String 放入拍卖=MultiLanguageManager.translateMap.get("放入拍卖");
		String 购买拍卖成功=MultiLanguageManager.translateMap.get("购买拍卖成功");
		String 快速出售=MultiLanguageManager.translateMap.get("快速出售");
		String 摆摊出售=MultiLanguageManager.translateMap.get("摆摊出售");
        String subSql="";
        if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu='"+fenQu+"'";}
		String sql=" select t.daojuName," +
				" sum(case when  t.transactiontype in ('"+放入拍卖+"','"+购买拍卖成功+"') then t.daojunum  else 0 end ) fabu," +
				" sum(case when  t.transactiontype in ('"+购买拍卖成功+"','"+快速出售+"','"+摆摊出售+"')  then  t.daojunum  else 0 end ) jiaoyi ," +
				" sum(case when  t.transactiontype in ('"+购买拍卖成功+"','"+快速出售+"','"+摆摊出售+"')  then  t.daojunum*t.danjia  else 0 end ) jine " +
				" from stat_transaction t where to_char(t.createdate,'YYYY-MM-DD') between '"+startDateStr+"' and '"+endDateStr+"'" + subSql+
				" group by t.daojuName";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[4];
				vales[0]=rs.getString("daojuName");
				vales[1]=rs.getString("fabu");
				vales[2]=rs.getString("jiaoyi");
				vales[3]=rs.getString("jine");
				
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

	
	@Override
	public ArrayList<String[]> getDaoJuinfo(String sql, String[] columusEnums) {
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
