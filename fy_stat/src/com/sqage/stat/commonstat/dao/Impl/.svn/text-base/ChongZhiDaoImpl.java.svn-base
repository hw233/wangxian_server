package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.ChongZhiDao;
import com.sqage.stat.commonstat.entity.ChongZhi;
import com.xuanzhi.tools.dbpool.DataSourceManager;
import com.xuanzhi.tools.text.DateUtil;

public class ChongZhiDaoImpl implements ChongZhiDao{
	
	static Logger logger = Logger.getLogger(ChongZhiDaoImpl.class);
	
	DataSourceManager dataSourceManager;
	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

	@Override
	public boolean addList(ArrayList<ChongZhi> chongZhiList) {
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		boolean result=true;
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_CHONGZHI(" +
					"ID," +
					"USERNAME," +
					"TIME," +
					"GAME," +
					
					"FENQU," +
					"GAMELEVEL," +
					"MONEY," +
					
					"TYPE," +
					"QUDAOID," +
					"QUDAO," +
					"CARDTYPE," +
					"COST," +
					"JIXING," +
					
					"MODELTYPE," +
					"VIP," +
					"REGISTDATE," +
					"PLAYNAME," +
					"COLUM1" +
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?,?, ?,?,?, ?,?,?, ?,? ,?,  ?,?,?,?,?)");
			
			for(ChongZhi chongZhi:chongZhiList){
			ps.setString(1, chongZhi.getUserName());
			ps.setTimestamp(2, new Timestamp(chongZhi.getTime().getTime()));
			ps.setString(3, chongZhi.getGame());
			ps.setString(4, chongZhi.getFenQu());
			ps.setString(5, chongZhi.getGameLevel());
			ps.setLong(6, chongZhi.getMoney());
			
			ps.setString(7, chongZhi.getType());
			ps.setString(8, chongZhi.getQuDaoId());
			ps.setString(9, chongZhi.getQuDao());
			ps.setString(10, chongZhi.getCardType());
			ps.setString(11, chongZhi.getCost());
			ps.setString(12, chongZhi.getJixing());
			
			ps.setString(13, chongZhi.getModelType());
			ps.setString(14, chongZhi.getVip());
			ps.setTimestamp(15, new Timestamp(chongZhi.getRegistDate().getTime()));
			ps.setString(16, chongZhi.getPlayName());
			ps.setString(17, chongZhi.getColum1());
			
			i = ps.executeUpdate();
			if(i<=0){result=false;}
		}
			con.setAutoCommit(true);
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			} finally {
				try {
					if(ps != null) ps.close();
					if (con != null) con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			
		}
		return result;
	}

	@Override
	public ChongZhi addUnRecorde(ChongZhi chongZhi) {
		long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_CHONGZHI_UNRECORD(" +
					"ID," +
					"USERNAME," +
					"TIME," +
					"GAME," +
					
					"FENQU," +
					"GAMELEVEL," +
					"MONEY," +
					
					"TYPE," +
					"QUDAOID," +
					"QUDAO," +
					
					"CARDTYPE," +
					"COST," +
					"JIXING," +
					
					"MODELTYPE," +
					"VIP," +
					"REGISTDATE," +
					"PLAYNAME," +
					"COLUM1" +
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?,?, ?,?,? ,?,?,?, ?,? ,?   ,?,?,?,?,?)");
			
			ps.setString(1, chongZhi.getUserName());
			ps.setTimestamp(2, new Timestamp(chongZhi.getTime().getTime()));
			ps.setString(3, chongZhi.getGame());
			ps.setString(4, chongZhi.getFenQu());
			ps.setString(5, chongZhi.getGameLevel());
			ps.setLong(6, chongZhi.getMoney());
			
			ps.setString(7, chongZhi.getType());
			ps.setString(8, chongZhi.getQuDaoId());
			ps.setString(9, chongZhi.getQuDao());
			ps.setString(10, chongZhi.getCardType());
			ps.setString(11, chongZhi.getCost());
			ps.setString(12, chongZhi.getJixing());
			
			ps.setString(13, chongZhi.getModelType());
			ps.setString(14, chongZhi.getVip());
			ps.setTimestamp(15, new Timestamp(chongZhi.getRegistDate().getTime()));
			ps.setString(16, chongZhi.getPlayName());
			ps.setString(17, chongZhi.getColum1());
			
			i = ps.executeUpdate();
			
				logger.info("[添加用户充值] [OK] "+chongZhi.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			System.out.println("[添加用户充值] [fail] "+chongZhi.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]"+e);
			e.printStackTrace();
			logger.error("[添加用户充值] [fail] "+chongZhi.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? chongZhi : null;
	}
	
	@Override
	public ChongZhi add(ChongZhi chongZhi) {
		long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_CHONGZHI(" +
					"ID," +
					"USERNAME," +
					"TIME," +
					"GAME," +
					
					"FENQU," +
					"GAMELEVEL," +
					"MONEY," +
					
					"TYPE," +
					"QUDAOID," +
					"QUDAO," +
					
					"CARDTYPE," +
					"COST," +
					"JIXING," +
					
					"MODELTYPE," +
					"VIP," +
					"REGISTDATE," +
					"PLAYNAME," +
					"COLUM1" +
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?,?, ?,?,? ,?,?,?, ?,? ,?   ,?,?,?,?,?)");
			
			ps.setString(1, chongZhi.getUserName());
			ps.setTimestamp(2, new Timestamp(chongZhi.getTime().getTime()));
			ps.setString(3, chongZhi.getGame());
			ps.setString(4, chongZhi.getFenQu());
			ps.setString(5, chongZhi.getGameLevel());
			ps.setLong(6, chongZhi.getMoney());
			
			ps.setString(7, chongZhi.getType());
			ps.setString(8, chongZhi.getQuDaoId());
			ps.setString(9, chongZhi.getQuDao());
			ps.setString(10, chongZhi.getCardType());
			ps.setString(11, chongZhi.getCost());
			ps.setString(12, chongZhi.getJixing());
			
			ps.setString(13, chongZhi.getModelType());
			ps.setString(14, chongZhi.getVip());
			ps.setTimestamp(15, new Timestamp(chongZhi.getRegistDate().getTime()));
			ps.setString(16, chongZhi.getPlayName());
			ps.setString(17, chongZhi.getColum1());
			
			i = ps.executeUpdate();
			
				logger.info("[添加用户充值] [OK] "+chongZhi.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			System.out.println("[添加用户充值] [fail] "+chongZhi.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]"+e);
			e.printStackTrace();
			logger.error("[添加用户充值] [fail] "+chongZhi.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? chongZhi : null;
	}

	@Override
	public boolean deleteById(Long id) {
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("delete from STAT_CHONGZHI where ID=?");
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
	public ChongZhi getById(Long id) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from STAT_CHONGZHI where id=?");
			ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			if(rs.next()) {
				
				ChongZhi chongZhi = new ChongZhi();
				chongZhi.setFenQu(rs.getString("FENQU"));
				chongZhi.setGame(rs.getString("GAME"));
				chongZhi.setGameLevel(rs.getString("GAMELEVEL"));
				chongZhi.setId(rs.getLong("ID"));
				chongZhi.setMoney(rs.getLong("MONEY"));
				chongZhi.setTime(rs.getTimestamp("TIME"));
				chongZhi.setUserName(rs.getString("USERNAME"));
				
				chongZhi.setType(rs.getString("TYPE"));
				chongZhi.setQuDao(rs.getString("QUDAO"));
				chongZhi.setQuDaoId(rs.getString("QUDAOID"));
				chongZhi.setCardType(rs.getString("CARDTYPE"));
				
				chongZhi.setCost(rs.getString("COST"));
				chongZhi.setJixing(rs.getString("JIXING"));
				
				chongZhi.setModelType(rs.getString("MODELTYPE"));
				chongZhi.setColum1(rs.getString("COLUM1"));
				chongZhi.setPlayName(rs.getString("PLAYNAME"));
				chongZhi.setRegistDate(rs.getTimestamp("REGISTDATE"));
				chongZhi.setVip(rs.getString("VIP"));
				
				return chongZhi;
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

	/**
	 * 
	 * @param sql
	 * @param cost    false 扣除手续费 true不扣手续费
	 * @return
	 */
	public ArrayList<ChongZhi>  getBySql_registtime(String sql,String cost) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<ChongZhi> chongzhiList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				
				ChongZhi chongZhi = new ChongZhi();
				chongZhi.setFenQu(rs.getString("FENQU"));
				chongZhi.setGame(rs.getString("GAME"));
				chongZhi.setGameLevel(rs.getString("GAMELEVEL"));
				chongZhi.setId(rs.getLong("ID"));
				if("false".equals(cost)){
					long meny=rs.getLong("MONEY");
					String costt=rs.getString("COST");
					
					chongZhi.setMoney(meny-(long)Float.parseFloat(costt));
				}else
				  { chongZhi.setMoney(rs.getLong("MONEY"));
				  }
				
				chongZhi.setTime(rs.getTimestamp("TIME"));
				chongZhi.setUserName(rs.getString("USERNAME"));
				
				chongZhi.setType(rs.getString("TYPE"));
				chongZhi.setQuDao(rs.getString("QUDAO"));
				chongZhi.setQuDaoId(rs.getString("QUDAOID"));
				chongZhi.setCardType(rs.getString("CARDTYPE"));
				
				chongZhi.setCost(rs.getString("COST"));
				chongZhi.setJixing(rs.getString("JIXING"));
				
				chongZhi.setModelType(rs.getString("MODELTYPE"));
				chongZhi.setColum1(rs.getString("COLUM1"));
				chongZhi.setPlayName(rs.getString("PLAYNAME"));
				chongZhi.setRegistDate(rs.getTimestamp("REGISTDATE"));
				chongZhi.setVip(rs.getString("VIP"));
				
				chongzhiList.add(chongZhi);
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
		return chongzhiList;
	}
	
	
	
	
	@Override
	public ArrayList<ChongZhi>  getBySql(String sql) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<ChongZhi> chongzhiList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				
				ChongZhi chongZhi = new ChongZhi();
				chongZhi.setFenQu(rs.getString("FENQU"));
				chongZhi.setGame(rs.getString("GAME"));
				chongZhi.setGameLevel(rs.getString("GAMELEVEL"));
				chongZhi.setId(rs.getLong("ID"));
				chongZhi.setMoney(rs.getLong("MONEY"));
				chongZhi.setTime(rs.getTimestamp("TIME"));
				chongZhi.setUserName(rs.getString("USERNAME"));
				
				chongZhi.setType(rs.getString("TYPE"));
				chongZhi.setQuDao(rs.getString("QUDAO"));
				chongZhi.setQuDaoId(rs.getString("QUDAOID"));
				chongZhi.setCardType(rs.getString("CARDTYPE"));
				
				chongZhi.setCost(rs.getString("COST"));
				chongZhi.setJixing(rs.getString("JIXING"));
				
				chongZhi.setModelType(rs.getString("MODELTYPE"));
				chongZhi.setColum1(rs.getString("COLUM1"));
				chongZhi.setPlayName(rs.getString("PLAYNAME"));
				chongZhi.setRegistDate(rs.getTimestamp("REGISTDATE"));
				chongZhi.setVip(rs.getString("VIP"));
				
				chongzhiList.add(chongZhi);
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
		return chongzhiList;
	}


/**
 * 充值人数
 * @param startDateStr
 * @param endDateStr
 * @param qudao
 * @param fenQu
 * @param game
 * @return
 */
public Long getChongZhiUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing){
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs=null;
	Long value=null;
	String subSql="";
	if(qudao!=null&&!qudao.isEmpty()){subSql+=" and t.qudao in ('"+qudao+"')";}
	if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";}
	if(jixing!=null&&!jixing.isEmpty()){subSql+=" and t.jixing='"+jixing+"'";}
	
	String sql="select count(distinct(T.FENQU||t.username)) count  from stat_chongzhi t";
	           sql+=" where 1=1 " +subSql+
			  " and  t.time  > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')";
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
 * 以前没有付过费的用户数
 * @param startDateStr
 * @param endDateStr
 * @param qudao
 * @param fenQu
 * @param game
 * @return
 */
public Long getUnAheadChongZhiUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing){
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs=null;
	Long value=0L;
	String subSql="";
	//String subSql_sub="";
	if(qudao!=null&&!qudao.isEmpty()){subSql+="  and t.qudao in ('"+qudao+"')";}
	                     //subSql_sub+="  and tt.qudao in ('"+qudao+"')";    }
	if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";
	                     //subSql_sub+=" and tt.fenqu='"+fenQu+"'";
	                     }
	if(jixing!=null&&!jixing.isEmpty()){subSql+=" and t.jixing='"+jixing+"'";}

	
	
	String sql="  select count(distinct(T.FENQU||t.username))  count  from stat_chongzhi t";
	            sql+=" where 1=1 " +subSql+
			    " and  t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time < to_date('"+endDateStr+" 23;59:59', 'YYYY-MM-DD hh24:mi:ss') " +
			    " and t.username not in ("
              + " select tt.username from stat_chongzhi tt where  tt.time <to_date('"+startDateStr+"', 'YYYY-MM-DD') )";
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
 * 付费且当天登陆的用户。
 * @param startDateStr
 * @param endDateStr
 * @param qudao
 * @param fenQu
 * @param game
 * @return
 */
public Long getChongZhi_LoginUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String game){
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs=null;
	Long value=null;
	String subSql="";
	if(qudao!=null&&!qudao.isEmpty()){subSql+="  and tt.qudao='"+qudao+"'";}
	if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and tt.fenqu='"+fenQu+"'";}
	
	String sql="  select count(distinct(tt.username)) count from stat_chongzhi tt, stat_playgame p";
	         sql+=" where tt.username = p.username and to_char(tt.time,'YYYY-MM-DD')= to_char(p.enterdate,'YYYY-MM-DD') " +subSql+
			      " and tt.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and tt.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')";
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
 * 充值数,包括手续费
 * @param startDateStr
 * @param endDateStr
 * @param qudao
 * @param fenQu
 * @param game
 * @return
 */
public Long getChongZhiCount_includeCost(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing){
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs=null;
	Long value=null;
	String subSql="";
	if(qudao!=null&&!qudao.isEmpty()){subSql+=" and t.qudao in ('"+qudao+"')";}
	if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";}
	if(jixing!=null&&!jixing.isEmpty()){subSql+=" and t.jixing='"+jixing+"'";}
	
	String sql="select sum(t.money) count from stat_chongzhi t";
	       sql+=" where 1=1 " +subSql+
			" and  t.time  >= to_date('"+startDateStr+"', 'YYYY-MM-DD') t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')";
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
 * 充值数 ，扣除手续费
 * @param startDateStr
 * @param endDateStr
 * @param qudao
 * @param fenQu
 * @param game
 * @return
 */
public Long getChongZhiCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing){
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs=null;
	Long value=null;
	String subSql="";
	if(qudao!=null&&!qudao.isEmpty()){subSql+=" and t.qudao in ('"+qudao+"')";}
	if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";}
	if(jixing!=null&&!jixing.isEmpty()){subSql+=" and t.jixing='"+jixing+"'";}
	
	String sql="select sum(t.money)-sum(t.cost) count from stat_chongzhi t";
	       sql+=" where 1=1 " +subSql+
			" and  t.time >= to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time< to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')";
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

public List<String[]> getModeTypeDateChongZhi(String startDateStr, String endDateStr,String qudao) {

	String sql="select to_char(t.time,'YYYY-MM-DD') dated,t.modeltype,sum(t.money) money from stat_chongzhi t " +
			"  where t.time between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and t.qudao='"+qudao+"' group by t.modeltype,to_char(t.time,'YYYY-MM-DD')";
	
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
			vales[0]=rs.getString("dated");
			vales[1]=rs.getString("modeltype");
			vales[2]=rs.getString("money")==null? "0" : rs.getString("money");
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

public List<String[]> getModeTypeChongZhi(String startDateStr, String endDateStr,String qudao,String type,String cardType,String fenQu) {
	String subsql=" ";
	if(qudao!=null&&!"".equals(qudao))     {subsql+=" and t.qudao='"+qudao+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and t.fenqu='"+fenQu+"' ";}
	if(type!=null&&!"".equals(type))           {subsql+=" and t.type='"+type+"' ";}
	if(cardType!=null&&!"".equals(cardType))           {subsql+=" and t.cardtype='"+cardType+"' ";}
	
	String sql="select  t.MODELTYPE,count(t.username) times,count(distinct(t.username)) users,sum(t.money) money from stat_chongzhi t " +
	        " where t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +subsql+
			" group by t.MODELTYPE order by sum(t.money) desc";
	
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
			vales[0]=rs.getString("MODELTYPE");
			vales[1]=rs.getString("times");
			vales[2]=rs.getString("users");
			vales[3]=rs.getString("money")==null? "0" : rs.getString("money");
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






public List<String[]> getFenQuChongZhi(String startDateStr, String endDateStr,String qudaoid,String type,String cardType,String fenQu) {
	String subsql=" ";
	if(qudaoid!=null&&!"".equals(qudaoid))     {subsql+=" and t.qudao='"+qudaoid+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and t.fenqu='"+fenQu+"' ";}
	if(type!=null&&!"".equals(type))           {subsql+=" and t.type='"+type+"' ";}
	if(cardType!=null&&!"".equals(cardType))           {subsql+=" and t.cardtype='"+cardType+"' ";}
	
	String sql="select  t.fenqu,count(t.username) times,count(distinct(t.username)) users,sum(t.money) money from stat_chongzhi t " +
	        " where t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +subsql+
			" group by t.fenqu order by sum(t.money) desc";
	
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
			vales[0]=rs.getString("fenqu");
			vales[1]=rs.getString("times");
			vales[2]=rs.getString("users");
			vales[3]=rs.getString("money")==null? "0" : rs.getString("money");
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
 * 获得充值金额超过100元的用户
 * @param startDateStr
 * @param endDateStr
 * @param qudaoid
 * @param type
 * @param cardType
 * @param fenQu
 * @return
 */

public List<String[]> getChongZhiUser(String startDateStr,String lastday,String qudao,String fenQu,String userName,String money) {
	String subsql=" ";
	if(qudao!=null&&!"".equals(qudao))     {subsql+=" and c.qudao='"+qudao+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and c.fenqu='"+fenQu+"' ";}
	
	String sql1="select c.username,count(c.id) usercount,sum(c.money) money from stat_chongzhi c " +
			" where c.time > to_date('"+lastday+"','YYYY-MM-DD') and c.time < to_date('"+startDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "+subsql+
			" group by c.username having sum(c.money)>9999" +
			" order by sum(c.money) desc";

	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String[]> resultlist=new ArrayList<String[]>();
	try {
		con = dataSourceManager.getInstance().getConnection();
		ps = con.prepareStatement(sql1);
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		while(rs.next()) {
			String[] vales=new String[5];
			vales[0]=rs.getString("username");
			vales[1]=rs.getString("usercount");
			vales[2]=rs.getString("money");
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
public List<String[]> getQuDaoChongZhi(String startDateStr, String endDateStr,String qudaoid,String type,String cardType,String fenQu) {
	String subsql=" ";
	if(qudaoid!=null&&!"".equals(qudaoid))     {subsql+=" and t.qudao='"+qudaoid+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and t.fenqu='"+fenQu+"' ";}
	if(type!=null&&!"".equals(type))           {subsql+=" and t.type='"+type+"' ";}
	if(cardType!=null&&!"".equals(cardType))           {subsql+=" and t.cardtype='"+cardType+"' ";}
	
	String czsql="select t.qudao,sum(t.money) money,count(distinct(t.username)) usercount from stat_chongzhi t where  " +
			" t.time between to_date('"+startDateStr+"', 'YYYY-MM-DD') and  to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +subsql+
			" group by t.qudao order by money desc ";

//	String registsql ="  select t.qudao, count(distinct t.username) chongzhiusercount,sum(t.money) money " +
//			" from stat_chongzhi t,( " +
//			" select  uu.name  from stat_user uu where  uu.registtime between" +
//			"  to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and" +
//			"  to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') ) mm " +
//			" where t.time between to_date('"+startDateStr+"', 'YYYY-MM-DD') and" +
//			"   to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +subsql+
//					" and t.username = mm.name " +
//					"  group by t.qudao";
//	
//	String sql="select qdt.*,czq.chongzhiusercount,czq.money rmoney from ("+czsql+")qdt " +
//			"left join  ("+registsql+") czq  on qdt.qudao=czq.qudao " +
//					" order by qdt.money desc";
	
	return queryQuDaoChongZhi(czsql);
}

public List<String[]> queryQuDaoChongZhi(String sql) {

	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String[]> resultlist=new ArrayList<String[]>();
	try {
		con = dataSourceManager.getInstance().getConnection();
		ps = con.prepareStatement(sql);
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		while(rs.next()) {
			String[] vales=new String[5];
			vales[0]=rs.getString("qudao");
			vales[1]=rs.getString("money");
			vales[2]=rs.getString("usercount");
			
			//vales[3]=rs.getString("chongzhiusercount")==null? "0" : rs.getString("chongzhiusercount");
			//vales[4]=rs.getString("rmoney")==null ? "0" : rs.getString("rmoney");
			
			
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
 * 按充值金额分部
 * @param startDateStr
 * @param endDateStr
 * @param qudaoid
 * @param fenQu
 * @param type
 * @param cardType
 * @param username
 * @param money
 * @param game
 * @return
 */

public ArrayList<String []> getChongZhiJinEFenbu(String startDateStr, String endDateStr, String qudaoid, String fenQu, String type,String cardType, String username, String money, String game) {
	String subsql=" ";
	if(qudaoid!=null&&!"".equals(qudaoid))     {subsql+=" and t.qudao='"+qudaoid+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and t.fenqu='"+fenQu+"' ";}
	if(type!=null&&!"".equals(type))           {subsql+=" and t.type='"+type+"' ";}
	if(cardType!=null&&!"".equals(cardType))           {subsql+=" and t.cardtype='"+cardType+"' ";}
	if(username!=null&&!"".equals(username))   {subsql+=" and t.username='"+username+"' ";}
	if(money!=null&&!"".equals(money))         {subsql+=" and t.money='"+money+"' ";}
	
	 String sql="select t.username,t.money,count(t.username) count,sum(t.money) summoney from stat_chongzhi t " +
	 		" where 1=1 " +subsql+
	 		" and  t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')"+
	 		" group by t.username,t.money";
	return queryChongZhiJinEFenbu(sql);
}

private ArrayList<String []> queryChongZhiJinEFenbu(String sql) {

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
			vales[0]=rs.getString("username");
			vales[1]=rs.getString("money");
			vales[2]=rs.getString("count");
			vales[3]=rs.getString("summoney");
			
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
public long getChongZhi_totalMoney_unRecord(String startDateStr, String endDateStr, String qudaoid, String fenQu, 
		String type,String cardType, String username, String money, String jixing,String modeType) {
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs=null;
	Long value=null;
	
	String subsql=" ";
	if(qudaoid!=null&&!"".equals(qudaoid))     {subsql+=" and t.qudao='"+qudaoid+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and t.fenqu='"+fenQu+"' ";}
	if(type!=null&&!"".equals(type))           {subsql+=" and t.type='"+type+"' ";}
	if(cardType!=null&&!"".equals(cardType))   {subsql+=" and t.cardtype='"+cardType+"' ";}
	if(username!=null&&!"".equals(username))   {subsql+=" and t.username='"+username+"' ";}
	if(money!=null&&!"".equals(money))         {subsql+=" and t.money='"+money+"' ";}
	if(jixing!=null&&!"".equals(jixing))       {subsql+=" and t.jixing='"+jixing+"' ";}
	if(modeType!=null&&!"".equals(modeType))       {subsql+=" and t.MODELTYPE='"+modeType+"' ";}

	String sql=	" select sum(t.money) count from STAT_CHONGZHI_UNRECORD t " +
		" where 1=1 " +subsql+
		" and t.time between to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and  to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') ";
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
public long getChongZhi_totalMoney(String startDateStr, String endDateStr, String qudaoid, String fenQu, 
		String type,String cardType, String username, String money, String jixing,String modeType) {
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs=null;
	Long value=null;
	
	String subsql=" ";
	if(qudaoid!=null&&!"".equals(qudaoid))     {subsql+=" and t.qudao='"+qudaoid+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and t.fenqu='"+fenQu+"' ";}
	if(type!=null&&!"".equals(type))           {subsql+=" and t.type='"+type+"' ";}
	if(cardType!=null&&!"".equals(cardType))   {subsql+=" and t.cardtype='"+cardType+"' ";}
	if(username!=null&&!"".equals(username))   {subsql+=" and t.username='"+username+"' ";}
	if(money!=null&&!"".equals(money))         {subsql+=" and t.money='"+money+"' ";}
	if(jixing!=null&&!"".equals(jixing))       {subsql+=" and t.jixing='"+jixing+"' ";}
	if(modeType!=null&&!"".equals(modeType))       {subsql+=" and t.MODELTYPE='"+modeType+"' ";}

	String sql=	" select sum(t.money) count from stat_chongzhi t " +
		" where 1=1 " +subsql+
		" and t.time between to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and  to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') ";
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
public ArrayList<String[]> getVipChongzhi(String startDateStr, String beginmoney, String endmoney, String qudao, String fenQu, String jixing) {
	StringBuffer subsqlc=new StringBuffer();
	StringBuffer subsqlch=new StringBuffer();
	if(qudao!=null){
		subsqlc.append("  and c.qudao='"+qudao+"'");
		subsqlch.append(" and ch.qudao='"+qudao+"'");
		//c.qudao='' and c.fenqu='' and c.jixing=''
	}
	if(fenQu!=null){
		subsqlc.append("  and c.fenqu='"+fenQu+"'");
		subsqlch.append(" and ch.fenqu='"+fenQu+"'");
	}
	if(jixing!=null){
		subsqlc.append("  and c.jixing='"+jixing+"'");
		subsqlch.append(" and ch.jixing='"+jixing+"'");
	}
	
	String sql=" select  count(distinct(ch.username||ch.fenqu)) count,sum(ch.money) money from" +
			" stat_chongzhi ch where ch.time > to_date('"+startDateStr+"','YYYY-MM-DD') and ch.time < to_date('"+startDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +subsqlch.toString()+
			" and ch.username||ch.fenqu in ( select c.username||c.fenqu from stat_chongzhi c  " +
			" where  to_char(c.time,'YYYY-MM-DD')<'"+startDateStr+"' " +subsqlc.toString()+
			" group by c.username||c.fenqu having "+beginmoney+"-1 <sum(c.money)";
	
	if(endmoney!=null){sql+=" and sum(c.money)<"+endmoney;}
	sql+=" )";
	

	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String[]> resultlist=new ArrayList<String[]>();
	try {
		con = dataSourceManager.getInstance().getConnection();
		ps = con.prepareStatement(sql);
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		while(rs.next()) {
			String[] vales=new String[2];
			if(rs.getString("count") !=null) { vales[0]=rs.getString("count"); }else{ vales[0]="0";}
			if(rs.getString("money") !=null) { vales[1]=rs.getString("money"); }else{ vales[1]="0";}
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
public ArrayList<ChongZhi> getChongZhi_unrecord(String startDateStr, String endDateStr, String qudaoid, String fenQu, 
		String type,String cardType, String username, String money, String jixing,String modeType) {
	String subsql=" ";
	if(qudaoid!=null&&!"".equals(qudaoid))     {subsql+=" and t.qudao='"+qudaoid+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and t.fenqu='"+fenQu+"' ";}
	if(type!=null&&!"".equals(type))           {subsql+=" and t.type='"+type+"' ";}
	if(cardType!=null&&!"".equals(cardType))   {subsql+=" and t.cardtype='"+cardType+"' ";}
	if(username!=null&&!"".equals(username))   {subsql+=" and t.username='"+username+"' ";}
	if(money!=null&&!"".equals(money))         {subsql+=" and t.money='"+money+"' ";}
	if(jixing!=null&&!"".equals(jixing))       {subsql+=" and t.jixing='"+jixing+"' ";}
	if(modeType!=null&&!"".equals(modeType))       {subsql+=" and t.MODELTYPE='"+modeType+"' ";}
	
	String sql=
		" select t.* from STAT_CHONGZHI_UNRECORD t " +
		" where 1=1 " +subsql+
		" and  t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +
				"order by t.time desc";
	
	return getBySql_registtime(sql,"true");
}

@Override
public ArrayList<ChongZhi> getChongZhi(String startDateStr, String endDateStr, String qudaoid, String fenQu, 
		String type,String cardType, String username, String money, String jixing,String modeType) {
	String subsql=" ";
	if(qudaoid!=null&&!"".equals(qudaoid))     {subsql+=" and t.qudao='"+qudaoid+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and t.fenqu='"+fenQu+"' ";}
	if(type!=null&&!"".equals(type))           {subsql+=" and t.type='"+type+"' ";}
	if(cardType!=null&&!"".equals(cardType))   {subsql+=" and t.cardtype='"+cardType+"' ";}
	if(username!=null&&!"".equals(username))   {subsql+=" and t.username='"+username+"' ";}
	if(money!=null&&!"".equals(money))         {subsql+=" and t.money='"+money+"' ";}
	if(jixing!=null&&!"".equals(jixing))       {subsql+=" and t.jixing='"+jixing+"' ";}
	if(modeType!=null&&!"".equals(modeType))       {subsql+=" and t.MODELTYPE='"+modeType+"' ";}
	
	String sql=
		" select t.* from stat_chongzhi t " +
		" where 1=1 " +subsql+
		" and  t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +
				"order by t.time desc";
	
	return getBySql_registtime(sql,"true");
}

@Override
public List<ChongZhi> getChongZhi_cost(String startDateStr, String endDateStr, String qudao, String fenQu, String type, String cardType,
		String username, String money, String jixing) {
	String subsql=" ";
	if(qudao!=null&&!"".equals(qudao))     {subsql+=" and t.qudao='"+qudao+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and t.fenqu='"+fenQu+"' ";}
	if(type!=null&&!"".equals(type))           {subsql+=" and t.type='"+type+"' ";}
	if(cardType!=null&&!"".equals(cardType))   {subsql+=" and t.cardtype='"+cardType+"' ";}
	if(username!=null&&!"".equals(username))   {subsql+=" and t.username='"+username+"' ";}
	if(money!=null&&!"".equals(money))         {subsql+=" and t.money='"+money+"' ";}
	if(jixing!=null&&!"".equals(jixing))       {subsql+=" and t.jixing='"+jixing+"' ";}
	
	String sql=
		" select t.* from stat_chongzhi t where 1=1 " +subsql+
		" and  t.time> to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time< to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +
				"order by t.time desc";
	
	return getBySql_registtime(sql,"false");
}

@Override
public long getChongZhi_totalMoney_cost(String startDateStr, String endDateStr, String qudaoid, String fenQu, String type, String cardType,
		String username, String money, String jixing) {

	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs=null;
	Long value=null;
	
	String subsql=" ";
	if(qudaoid!=null&&!"".equals(qudaoid))     {subsql+=" and t.qudao='"+qudaoid+ "'";}
	if(fenQu!=null&&!"".equals(fenQu))         {subsql+=" and t.fenqu='"+fenQu+"' ";}
	if(type!=null&&!"".equals(type))           {subsql+=" and t.type='"+type+"' ";}
	if(cardType!=null&&!"".equals(cardType))   {subsql+=" and t.cardtype='"+cardType+"' ";}
	if(username!=null&&!"".equals(username))   {subsql+=" and t.username='"+username+"' ";}
	if(money!=null&&!"".equals(money))         {subsql+=" and t.money='"+money+"' ";}
	if(jixing!=null&&!"".equals(jixing))       {subsql+=" and t.jixing='"+jixing+"' ";}

	String sql=	" select sum(t.money)-sum(t.cost) count from stat_chongzhi t " +
		" where 1=1 " +subsql+
		" and  t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and  t.time<  to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') ";
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
 * 充值分布
 */
    public List<String[]> getChongZhifenbu(String startDateStr, String endDateStr, String level, String fenQu, String money, String game) {
    	
    	String subsql=" ";
    	if(level!=null&&!"".equals(level))     {subsql+=" and t.gamelevel in ("+level+") ";}
    	if(fenQu!=null&&!"".equals(fenQu))     {subsql+=" and t.fenqu ='"+fenQu+"' ";}
    	if(money!=null&&!"".equals(money))     {subsql+=" and  t.money >='"+money+"' ";}
    	
    	String sql=
    	 " select t.gamelevel gamelevel,count(t.id) count,sum(t.money) moneycount,avg(t.money) avgmoney from stat_chongzhi t " +
    	 " where 1 = 1  " +subsql+
    	 " and  t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time< to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
    	 " group by t.gamelevel";
    	
    	return queryChongzhiFenBu(sql);
    }
    
    
    /**
     * 包括充值金额区间分布
     * @param startDateStr
     * @param endDateStr
     * @param level
     * @param fenQu
     * @param money
     * @param game
     * 50元一个等级
     * @return
     */
    public List<String[]> getChongZhi_jineQuJian_fenbu(String startDateStr, String endDateStr, String level, String fenQu, String money,String jiXing) {
    	
    	String subsql=" ";
    	if(level!=null&&!"".equals(level))     {subsql+=" and t.gamelevel in ("+level+") ";}
    	if(fenQu!=null&&!"".equals(fenQu))     {subsql+=" and t.fenqu ='"+fenQu+"' ";}
    	if(money!=null&&!"".equals(money))     {subsql+=" and t.money >='"+money+"' ";}
    	if(jiXing!=null&&!"".equals(jiXing))   {subsql+=" and t.jixing='"+jiXing+"'";}
    	
    	String sql=
    		" select trunc(temp.money/5000) money,count(temp.money/5000) count,sum(temp.money)/100 summoney from " +
    		" (" +
    		" select t.username, sum(t.money) money" +
    		" from stat_chongzhi t where 1=1 "+subsql+
    		" and t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')"+
    		" group by t.username " +
    		" ) temp group by trunc(temp.money/5000) order by  trunc(temp.money/5000)";
    		 
    	return queryChongZhi_jineQuJian_fenbu(sql);
    }

	private List<String[]> queryChongZhi_jineQuJian_fenbu(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist = new ArrayList<String[]>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String[] vales = new String[3];
				vales[0] = rs.getString("money");
				vales[1] = rs.getString("count");
				vales[2] = rs.getString("summoney");

				resultlist.add(vales);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) ps.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultlist;
}

/**
 * 包括充值分布和新增充值分布
 * @param startDateStr
 * @param endDateStr
 * @param level
 * @param fenQu
 * @param money
 * @param game
 * @return
 */
public List<String[]> getChongZhifenbu_(String startDateStr, String endDateStr, String level, String fenQu, String money, String game) {
	
	String subsql=" ";
	if(level!=null&&!"".equals(level))     {subsql+=" and t.gamelevel in ("+level+") ";}
	if(fenQu!=null&&!"".equals(fenQu))     {subsql+=" and t.fenqu ='"+fenQu+"' ";}
	if(money!=null&&!"".equals(money))     {subsql+=" and  t.money >='"+money+"' ";}
	
	String sql=
	"select old.gamelevel ogamelevel,old.usernum ousernum,old.summoney osummoney,old.avgmoney oavgmoney,new.usernum,new.summoney,new.avgmoney from" +
	"	(" +
	" select t.gamelevel gamelevel,count(t.id) usernum,sum(t.money) summoney,avg(t.money) avgmoney " +
	" from stat_chongzhi t" +
	"	where 1 = 1"+subsql+
	" and  t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and" +
	"  t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') group by t.gamelevel" +
	" ) old" +
	" LEFT JOIN" +
	" (" +
	" select t.gamelevel gamelevel,count(t.id) usernum ,sum(t.money) summoney,avg(t.money) avgmoney " +
	" from stat_chongzhi t " +
	" where 1 = 1"+subsql+
	 " and  t.time  > to_date('"+startDateStr+"', 'YYYY-MM-DD') and" +
	 "  t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') and t.username not in" +
	 " ( select distinct t.username" +
	 "  from stat_chongzhi t " +
	 " where 1 = 1"+subsql+
	 " and  t.time <to_date('"+startDateStr+"', 'YYYY-MM-DD'))group by t.gamelevel" +
	 " ) new" +
	 "  on old.gamelevel=new.gamelevel order by old.gamelevel";
	
	return queryChongzhiFenBu(sql);
}

@Override
/**
 * 新增充值分部
 */
public List<String[]> getNewChongZhifenbu(String startDateStr, String endDateStr, String level, String fenQu, String money, String game) {

	String subsql=" ";
	if(level!=null&&!"".equals(level))     {subsql+=" and t.gamelevel in ("+level+") ";}
	if(fenQu!=null&&!"".equals(fenQu))     {subsql+=" and t.fenqu ='"+fenQu+"' ";}
	if(money!=null&&!"".equals(money))     {subsql+=" and  t.money= >='"+money+"' ";}
	
	String sql=
	 "select t.gamelevel gamelevel,count(t.id) count,sum(t.money) moneycount,avg(t.money) avgmoney from stat_chongzhi t where 1 = 1"+subsql+
	 " and  t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time < to_date('"+endDateStr+"23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +
	 " and t.username not in" +
	 " ( select distinct t.username from stat_chongzhi t " +
	 " where 1 = 1"+subsql+" and  t.time <to_date('"+startDateStr+"', 'YYYY-MM-DD')" +
	 ")group by t.gamelevel";
	       
	return queryChongzhiFenBu(sql);
}
public ArrayList<String[]>  queryChongzhiFenBu(String sql) {

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
			vales[0]=rs.getString("ogamelevel");
			vales[1]=rs.getString("ousernum");
			vales[2]=rs.getString("osummoney");
			vales[3]=rs.getString("oavgmoney");
			vales[4]=rs.getString("usernum");
			vales[5]=rs.getString("summoney");
			vales[6]=rs.getString("avgmoney");
			
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
 * K日千人收益
 * @param registstartDateStr
 * @param registEndDateStr
 * @param startDateStr
 * @param endDateStr
 * @param qudaoid
 * @param game
 * @return
 */
public  ArrayList<String[]> getKRi_QianRenShouyi(String registstartDateStr, String registEndDateStr,String dayNum,String qudaoid) {
	  
	String subsql=" ";
	String rsubsql="";
	if(qudaoid!=null&&!"".equals(qudaoid))     {subsql+=" and ch.qudao='"+qudaoid+"' "; rsubsql=" and u.qudao='"+qudaoid+"'";}
	
	 ArrayList<String[]> result=new ArrayList();
	 Date t = DateUtil.parseDate(registEndDateStr, "yyyy-MM-dd");
	  	Date s = DateUtil.parseDate(registstartDateStr, "yyyy-MM-dd");
	  	ArrayList<String> dayList = new ArrayList<String>();
	  int daycount=	Integer.parseInt(dayNum);
	  	  while (s.getTime() <= t.getTime() + 3600000) {
	  			String day = DateUtil.formatDate(s, "yyyy-MM-dd");
	  			//dayList.add(day);
	  			s.setTime(s.getTime() + 24 * 3600 * 1000);
	  			if(s.getTime() + daycount*24 * 3600 * 1000>System.currentTimeMillis()){break;}
	  			dayList.add(day);
	  		}
	
	for(String day:dayList){
	String moneysql=
		"select trunc(u.registtime) regtime,count(distinct u.name) registCount,count(ch.username) chongzhiusercount," +
		" sum(ch.money) money ,"+dayNum+" daynum from stat_chongzhi ch, stat_user u where 1 = 1 " +subsql+
		" and u.name = ch.username " +
		" and  u.registtime > to_date('"+day+"', 'YYYY-MM-DD') and u.registtime<to_date('"+day+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')	and " +
		"  ch.time  > to_date('"+day+"', 'YYYY-MM-DD') and ch.time < to_date('"+day+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')+"+dayNum+
		" group by trunc(u.registtime)  ";
	
	String registUser="select to_date('"+day+"','YYYY-MM-DD') dateStr,count(distinct(u.name)) registCount from " +
			" stat_user u where 1=1 " +rsubsql+
			" and  u.registtime >to_date('"+day+"','YYYY-MM-DD') and u.registtime < to_date('"+day+" 23:59:59','YYYY-MM-DD hh24:mi:ss')";
	
	String sql="select utmp.registCount,utmp.dateStr ttime,temp.* from " +
			"(" +
			registUser +
			") utmp" +
			" left join " +
			"(" +
			moneysql +
			") temp" +
			"	on utmp.dateStr=temp.regtime";

	if(logger.isDebugEnabled()){
		logger.debug("[K日千人收益sql：] ["+sql+"] ");
	}
	result.addAll(queryQianRenShouyi(sql));
	}
	return result;
}

public  ArrayList<String[]> getKriShouyi(String registstartDateStr,String registEndDateStr, String startDateStr, String endDateStr, String qudao, String game) {

	String subsql="";
	if(qudao!=null&&!"".equals(qudao))     {subsql+=" and u.qudao='"+qudao+"' ";}
	String sql=
	 "select trunc(ch.time) time, count (distinct u.name) registCount, count(distinct ch.username) chongzhiusercount,sum(ch.money) money," +
	 " trunc(trunc(ch.time)-to_date( '"+registEndDateStr+" ', 'yyyy-mm-dd ')) daynum " +
	 		" from stat_chongzhi ch,stat_user u where 1=1 " +subsql+
	 		" and u.name=ch.username and " +
	        "  u.registtime > to_date('"+registstartDateStr+"', 'YYYY-MM-DD') and" +
	        " u.registtime < to_date('"+registEndDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') ";
	        
	        if(startDateStr!=null&&endDateStr!=null){
	        	sql+=" and" +
	                 " ch.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and" +
	                 " ch.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " ;
	                }
	        sql+=" group by trunc(ch.time)";
	        
	    return queryQianRenShouyi(sql);
}


@Override
public  ArrayList<String[]> getQianRenShouyi(String registstartDateStr,String registEndDateStr, String startDateStr, String endDateStr, String qudao, String game) {
	  
	String subsql1 = "";
	String subsql2 = " ";
	if (qudao != null && !"".equals(qudao)) {
		subsql1 += " and u.qudao='" + qudao + "' ";

		if (startDateStr != null && endDateStr != null) {
			subsql2 += " and c.time > to_date('" + startDateStr + " 00:00:00','YYYY-MM-DD hh24:mi:ss') " + " and c.time < to_date('" + endDateStr
					+ " 23:59:59','YYYY-MM-DD hh24:mi:ss') ";
		}
		subsql2 += " and c.qudao='" + qudao + "' ";
	}
    
    String sql=" select trunc(c.time) ttime," +
    		"(select count(distinct u.name) registCount from stat_user u " +
		" where u.registtime > to_date('"+registstartDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and u.registtime < to_date('"+registEndDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
		subsql1 +
				") registCount,count(distinct(c.username)) chongzhiusercount,sum(c.money) money," +
		" trunc(trunc(c.time)-to_date('"+registEndDateStr+"', 'yyyy-mm-dd')) daynum " +
		" from stat_chongzhi c where 1=1 " ;
    if(startDateStr!=null&&endDateStr!=null){
    	sql+=" and   c.time  > to_date('"+startDateStr+"','YYYY-MM-DD') and c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" ;
                 }
       sql +=subsql2+
		" and c.username in ( " +
		" select u.name from stat_user u " +
		" where u.registtime > to_date('"+registstartDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and u.registtime < to_date('"+registEndDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " 
		+ subsql1+" ) group by trunc(c.time) order by trunc(c.time)";
	
	
    
	       
	    return queryQianRenShouyi(sql);
}

public ArrayList<String[]>  queryQianRenShouyi(String sql) {

	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String[]> resultlist=new ArrayList<String[]>();
	try {
		con = dataSourceManager.getInstance().getConnection();
		ps = con.prepareStatement(sql);
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		while(rs.next()) {
			String[] vales=new String[5];
			vales[0]=rs.getString("ttime").substring(0,10);
			vales[1]=rs.getString("registCount");
			vales[2]=rs.getString("chongzhiusercount");
			vales[3]=rs.getString("money");
			vales[4]=rs.getString("daynum");
			
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
public ArrayList<String> getTypes(Date date) {

	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String> fenquList=new ArrayList();
	try {
		con = dataSourceManager.getInstance().getConnection();
		ps = con.prepareStatement("select distinct(t.type) type from stat_chongzhi t");
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		
		while(rs.next()) {
			String fenqu=rs.getString("type");
			if(!"".equals(fenqu)&&fenqu!=null){
				fenquList.add(fenqu);
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
	return fenquList;
}

@Override
public ArrayList<String> getCardTypes() {

	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String> fenquList=new ArrayList();
	try {
		con = dataSourceManager.getInstance().getConnection();
		ps = con.prepareStatement("select distinct(t.CARDTYPE) type from stat_chongzhi t");
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		
		while(rs.next()) {
			String fenqu=rs.getString("type");
			if(!"".equals(fenqu)&&fenqu!=null){
				fenquList.add(fenqu);
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
	return fenquList;
}

@Override
public ArrayList<String> getSDKChongZhi(String startDateStr) {
	String subSql=" and to_char(s.time,'YYYY-MM-DD') = '"+startDateStr+"' ";
	String sql="select s.qudao,sum(s.money) money from stat_chongzhi s where 1=1 " +subSql+
			" and  s.type not in ('支付宝','易宝','神州付','天猫商城','网站充值','Wap支付宝') group by s.qudao";
			
			
	return querySDKChongZhi(sql);
}

private ArrayList<String> querySDKChongZhi(String sql) {

	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String> resultlist=new ArrayList<String>();
	try {
		con = dataSourceManager.getInstance().getConnection();
		ps = con.prepareStatement(sql);
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		while(rs.next()) {
			String vales="";
			vales=rs.getString("qudao")+" "+rs.getString("money");
		
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
public ArrayList<String> getSqageChongZhi_nocost(String startDateStr) {
	
	String subSql=" and to_char(s.time, 'YYYY-MM-DD') ='"+startDateStr+"' ";
	
	String sql="select s.type,sum(s.money) money from stat_chongzhi s where 1=1 " +subSql+
			" and s.type  in ('支付宝','易宝','神州付','天猫商城','网站充值','Wap支付宝')  group by s.type";
			
	return querySqageChongZhi(sql);
}

@Override
public ArrayList<String> getSqageChongZhi(String startDateStr) {
	
	String subSql=" and to_char(s.time, 'YYYY-MM-DD') ='"+startDateStr+"' ";
	
	String sql="select s.type,sum(s.money)-sum(s.cost) money from stat_chongzhi s where 1=1 " +subSql+
			" and s.type  in ('支付宝','易宝','神州付','天猫商城','网站充值','Wap支付宝')  group by s.type";
			
	return querySqageChongZhi(sql);
}

private ArrayList<String> querySqageChongZhi(String sql) {
	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String> resultlist=new ArrayList<String>();
	try {
		con = dataSourceManager.getInstance().getConnection();
		ps = con.prepareStatement(sql);
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		while(rs.next()) {
			String  vales="";
			
			
			vales=rs.getString("type")+ " "+ rs.getString("money");
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
public Long getAppChina_null_pay(String startDateStr, String endDateStr, String qudao, String fenQu) {

	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs=null;
	Long value=null;
	String subSql="";
	if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";}
	
	String sql="select sum(t.money)-sum(t.cost) count from stat_chongzhi t";
	       sql+=" where t.qudao ='null' and t.username in ( select distinct(u.name) from stat_user u where u.qudao='appchina')" +subSql+
			" and  t.time > to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.time < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')";
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
public ArrayList<String[]> getNewPlayerChongzhi(String startDateStr, String qudao, String fenQu, String jixing) {
	StringBuffer subsqlc=new StringBuffer();
	StringBuffer subsqlch=new StringBuffer();
	if(qudao!=null){
		subsqlc.append("  and c.qudao='"+qudao+"'");
		subsqlch.append(" and ch.qudao='"+qudao+"'");
		//c.qudao='' and c.fenqu='' and c.jixing=''
	}
	if(fenQu!=null){
		subsqlc.append("  and c.fenqu='"+fenQu+"'");
		subsqlch.append(" and ch.fenqu='"+fenQu+"'");
	}
	if(jixing!=null){
		subsqlc.append("  and c.jixing='"+jixing+"'");
		subsqlch.append(" and ch.jixing='"+jixing+"'");
	}
	
	String sql=" select count(distinct(ch.username)) usercount, count(distinct(ch.username||ch.fenqu)) playercount,sum(ch.money) money from" +
			" stat_chongzhi ch where ch.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
			" and  ch.time < to_date('"+startDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +subsqlch.toString()+
			" and ch.username||ch.fenqu not in " +
			" ( select c.username||c.fenqu from stat_chongzhi c where  c.time < to_date('"+startDateStr+"','YYYY-MM-DD')" +subsqlc.toString()+
			" )";

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
			if(rs.getString("usercount") !=null) { vales[0]=rs.getString("usercount"); }else{ vales[0]="0";}
			if(rs.getString("playercount") !=null) { vales[1]=rs.getString("playercount"); }else{ vales[1]="0";}
			if(rs.getString("money") !=null) { vales[2]=rs.getString("money"); }else{ vales[2]="0";}
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
public ArrayList<String[]> getOldPlayerChongzhi(String startDateStr, String qudao, String fenQu, String jixing) {

	StringBuffer subsqlc=new StringBuffer();
	StringBuffer subsqlch=new StringBuffer();
	if(qudao!=null){
		subsqlc.append("  and c.qudao='"+qudao+"'");
		subsqlch.append(" and ch.qudao='"+qudao+"'");
		//c.qudao='' and c.fenqu='' and c.jixing=''
	}
	if(fenQu!=null){
		subsqlc.append("  and c.fenqu='"+fenQu+"'");
		subsqlch.append(" and ch.fenqu='"+fenQu+"'");
	}
	if(jixing!=null){
		subsqlc.append("  and c.jixing='"+jixing+"'");
		subsqlch.append(" and ch.jixing='"+jixing+"'");
	}
	
	String sql=" select count(ch.username) usercount, count(distinct(ch.username||ch.fenqu)) playercount,sum(ch.money) money from" +
			" stat_chongzhi ch where ch.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
			" and ch.time < to_date('"+startDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +subsqlch.toString()+
			" and ch.username||ch.fenqu in " +
			" ( select c.username||c.fenqu from stat_chongzhi c where  c.time <to_date('"+startDateStr+"','YYYY-MM-DD') " +subsqlc.toString()+
			" )";

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
			if(rs.getString("usercount") !=null) { vales[0]=rs.getString("usercount"); }else{ vales[0]="0";}
			if(rs.getString("playercount") !=null) { vales[1]=rs.getString("playercount"); }else{ vales[1]="0";}
			if(rs.getString("money") !=null) { vales[2]=rs.getString("money"); }else{ vales[2]="0";}
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
public ArrayList<String[]> getRegistEnterChongzhi(String startDateStr, String qudao, String fenQu, String jixing) {

	StringBuffer subsqlc=new StringBuffer();
	StringBuffer subsqlch=new StringBuffer();
	if(qudao!=null){
		subsqlc.append("  and c.qudao='"+qudao+"'");
		//subsqlch.append(" and ch.qudao='"+qudao+"'");
		//c.qudao='' and c.fenqu='' and c.jixing=''
	}
	if(fenQu!=null){
		subsqlc.append("  and c.fenqu='"+fenQu+"'");
		//subsqlch.append(" and ch.fenqu='"+fenQu+"'");
	}
	if(jixing!=null){
		subsqlc.append("  and c.jixing='"+jixing+"'");
		//subsqlch.append(" and ch.jixing='"+jixing+"'");
	}
	
	String sql="select count(c.username) usercount,count(distinct(c.username||c.fenqu)) playercount,sum(c.money) money " +
			" from stat_chongzhi c where  c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
			" and c.time < to_date('"+startDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +subsqlc+
					" and c.username in " +
			" (select u.name from stat_user u where u.registtime > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
			" and u.registtime < to_date('"+startDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss'))";

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
			if(rs.getString("usercount") !=null)   { vales[0]=rs.getString("usercount"); }    else { vales[0]="0";}
			if(rs.getString("playercount") !=null) { vales[1]=rs.getString("playercount"); }  else { vales[1]="0";}
			if(rs.getString("money") !=null)       { vales[2]=rs.getString("money"); }        else { vales[2]="0";}
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
public ArrayList<String[]> chongZhiJianKong(String startDateStr, String qudao, String fenQu) {
	StringBuffer subsqlc=new StringBuffer();
	if(qudao!=null){
		subsqlc.append("  and c.qudao='"+qudao+"'");
	}
	if(fenQu!=null){
		subsqlc.append("  and c.fenqu='"+fenQu+"'");
	}
	String sql="select  to_char(c.time,'YYYY-MM-DD hh24') time,sum(c.money) money from stat_chongzhi c " +
			" where c.time between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
			" and to_date('"+startDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +subsqlc.toString()+
			" group by to_char(c.time,'YYYY-MM-DD hh24')";

	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String[]> resultlist=new ArrayList<String[]>();
	try {
		con = dataSourceManager.getInstance().getConnection();
		ps = con.prepareStatement(sql);
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		while(rs.next()) {
			String[] vales=new String[2];
			vales[0]=rs.getString("time");
			vales[1]=rs.getString("money");
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
public ArrayList<String[]> getChongZhiInfo(String sql, String[] columusEnums) {

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



}