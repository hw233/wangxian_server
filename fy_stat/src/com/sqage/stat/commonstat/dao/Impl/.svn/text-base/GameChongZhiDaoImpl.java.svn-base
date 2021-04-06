package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.GameChongZhiDao;
import com.sqage.stat.commonstat.entity.GameChongZhi;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class GameChongZhiDaoImpl implements GameChongZhiDao {
	static Logger logger = Logger.getLogger(GameChongZhiDaoImpl.class);

   DataSourceManager dataSourceManager;
	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

	@Override
	public List<GameChongZhi> getByGameChongZhi(GameChongZhi gameChongZhi) {
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		
		Integer action=gameChongZhi.getAction();
		String currencuType=gameChongZhi.getCurrencyType();
		String fenQu=gameChongZhi.getFenQu();
		//String game=gameChongZhi.getGame();
		String gameLevel=gameChongZhi.getGameLevel();
		//Long money=gameChongZhi.getMoney();
		String quDao=gameChongZhi.getQuDao();
		String reasonType=gameChongZhi.getReasonType();
		Date time=gameChongZhi.getTime();
		String userName=gameChongZhi.getUserName();
		String jixing =gameChongZhi.getJixing();
		
		String subSql="";
		if(action!=null)       { subSql+=" and t.action='"+action+"'";}
		if(currencuType!=null) { subSql+=" and t.currencytype='"+currencuType+"'";}
		if(fenQu!=null)        { subSql+=" and t.fenqu='"+fenQu+"'";}
		if(gameLevel!=null){subSql+=" and t.gamelevel='"+gameLevel+"'";}
		
		if(quDao!=null){subSql+=" and t.qudao='"+quDao+"'";}
		if(reasonType!=null){subSql+=" and t.reasontype='"+reasonType+"'";}
		if(time!=null){subSql+=" and trunc(t.time)=to_date('"+sf.format(time)+"','YYYY-MM-DD')";}
		if(userName!=null){subSql+=" and t.username='"+userName+"'";}
		if(jixing!=null){subSql+=" and t.jixing ='"+jixing+"'";}
		
		String sql="select * from stat_gamechongzhi t where 1=1 "+subSql;
		
//		if(logger.isDebugEnabled()){
//			logger.debug("[获取游戏充值信息:"+sql+"] ");
//		}
		return getBySql(sql);
	}
	

	@Override
	public ArrayList<String[]> getGameChongZhiYinZi(String startDateStr) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		//String sql="select * from stat_gamechongzhi_yinzi t where t.createdate='"+startDateStr+"'";
		
		String sql="select t.createdate,t.fenqu,t.action,r.name reasontype,t.money from stat_gamechongzhi_yinzi t,dt_reasontype r " +
				" where t.createdate='"+startDateStr+"' and t.reasontype=r.id";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[5];
				vales[0]=rs.getString("CREATEDATE");
				vales[1]=rs.getString("FENQU");
				vales[2]=rs.getString("ACTION");
				vales[3]=rs.getString("REASONTYPE");
				vales[4]=rs.getString("MONEY");
		
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
	public ArrayList<String[]> getGameChongZhiYinPiao(String startDateStr) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		//String sql="select * from stat_gamechongzhi_yinzi t where t.createdate='"+startDateStr+"'";
		
		String sql="select t.createdate,t.fenqu,t.action,r.name,t.money from stat_gamechongzhi_yinpiao t,dt_reasontype r " +
				" where t.createdate='"+startDateStr+"' and t.reasontype=r.id";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[5];
				vales[0]=rs.getString("CREATEDATE");
				vales[1]=rs.getString("FENQU");
				vales[2]=rs.getString("ACTION");
				vales[3]=rs.getString("REASONTYPE");
				vales[4]=rs.getString("MONEY");
		
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
	public boolean update(GameChongZhi gameChongZhi) {
		Long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_GAMECHONGZHI set "+
					"USERNAME=?," +
					"TIME=?," +
					"GAME=?," +
					
					"FENQU=?," +
					"GAMELEVEL=?," +
					"MONEY=?," +
					
					"ACTION=?," +
					"CURRENCYTYPE=?," +
					"REASONTYPE=?," +
					"QUDAO=?," +
					"JIXING=?" +
					
					" where ID=?");
			ps.setString(1, gameChongZhi.getUserName());
			ps.setTimestamp(2, new Timestamp(gameChongZhi.getTime().getTime()));
			ps.setString(3, gameChongZhi.getGame());
			ps.setString(4, gameChongZhi.getFenQu());
			ps.setString(5, gameChongZhi.getGameLevel());
			ps.setLong(6, gameChongZhi.getMoney());
			
			ps.setInt(7, gameChongZhi.getAction());
			ps.setString(8, gameChongZhi.getCurrencyType());
			ps.setString(9, gameChongZhi.getReasonType());
			ps.setString(10, gameChongZhi.getQuDao());;
			ps.setString(11, gameChongZhi.getJixing());;
			
			ps.setLong(12, gameChongZhi.getId());
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[更新用户游戏充值] [OK] "+gameChongZhi.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (SQLException e) {
			logger.error("[更新用户游戏充值] [fail] "+gameChongZhi.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null&&!con.isClosed())
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0;
	}

	public boolean addList(ArrayList<GameChongZhi> gameChongZhiList) {
		//logger.info("GameChongZhiList添加数据前数据条数"+gameChongZhiList.size());
		long now = System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		int count=0;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_GAMECHONGZHI(" +
					"ID," +
					"USERNAME," +
					"TIME," +
					"GAME," +
					
					"FENQU," +
					"GAMELEVEL," +
					"MONEY," +
					
					"ACTION," +
					"CURRENCYTYPE," +
					"REASONTYPE," +
					"QUDAO," +
					"JIXING" +
					
					") values (SEQ_GAMECHONGZHI_ID.NEXTVAL,?,?,?, ?,?,? ,?,?,?,? ,?)");
			
		for(GameChongZhi gameChongZhi:gameChongZhiList){
			
			ps.setString(1, gameChongZhi.getUserName());
			ps.setTimestamp(2, new Timestamp(gameChongZhi.getTime().getTime()));
			ps.setString(3, gameChongZhi.getGame());
			ps.setString(4, gameChongZhi.getFenQu());
			ps.setString(5, gameChongZhi.getGameLevel());
			ps.setLong(6, gameChongZhi.getMoney());
			ps.setInt(7, gameChongZhi.getAction());
			ps.setString(8, gameChongZhi.getCurrencyType());
			ps.setString(9, gameChongZhi.getReasonType());
			ps.setString(10, gameChongZhi.getQuDao());
			ps.setString(11, gameChongZhi.getJixing());
			i = ps.executeUpdate();
			count++;
			if(count!=0&&(count%500==0)){con.commit();}
			}
		    //logger.info("GameChongZhiList添加数据后数据条数"+gameChongZhiList.size());
		   con.commit();
		   con.setAutoCommit(true);
		  
		   } catch (Exception e) {
			logger.error("[添加用户游戏充值] [fail] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			e.printStackTrace(System.out);
			try {
				con.rollback();
			} catch (SQLException e1) {
				logger.error("[添加用户游戏充值] [fail] [回滚出现异常] [cost:"+(System.currentTimeMillis()-now)+"ms]",e1);
			}
		  } finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//return i > 0 ? gameChongZhi : null;
		return true;
	}
	public boolean addList_jifen(ArrayList<GameChongZhi> gameChongZhiList) {
		logger.info("GameChongZhiList添加数据前数据条数"+gameChongZhiList.size());
		long now = System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		int count=0;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_GAMECHONGZHI_JIFEN(" +
					"ID," +
					"USERNAME," +
					"TIME," +
					"GAME," +
					
					"FENQU," +
					"GAMELEVEL," +
					"MONEY," +
					
					"ACTION," +
					"CURRENCYTYPE," +
					"REASONTYPE," +
					"QUDAO," +
					"JIXING" +
					
					") values (SEQ_GAMECHONGZHI_ID.NEXTVAL,?,?,?, ?,?,? ,?,?,?,? ,?)");
			
		for(GameChongZhi gameChongZhi:gameChongZhiList){
			
			ps.setString(1, gameChongZhi.getUserName());
			ps.setTimestamp(2, new Timestamp(gameChongZhi.getTime().getTime()));
			ps.setString(3, gameChongZhi.getGame());
			ps.setString(4, gameChongZhi.getFenQu());
			ps.setString(5, gameChongZhi.getGameLevel());
			ps.setLong(6, gameChongZhi.getMoney());
			ps.setInt(7, gameChongZhi.getAction());
			ps.setString(8, gameChongZhi.getCurrencyType());
			ps.setString(9, gameChongZhi.getReasonType());
			ps.setString(10, gameChongZhi.getQuDao());
			ps.setString(11, gameChongZhi.getJixing());
			i = ps.executeUpdate();
			count++;
			if(count!=0&&(count%100==0)){con.commit();}
			}
		    //logger.info("GameChongZhiList添加数据后数据条数"+gameChongZhiList.size());
		   con.commit();
		   con.setAutoCommit(true);
		  
		   } catch (Exception e) {
			logger.error("[添加用户游戏充值] [fail] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			e.printStackTrace(System.out);
			try {
				con.rollback();
			} catch (SQLException e1) {
				logger.error("[添加用户游戏充值] [fail] [回滚出现异常] [cost:"+(System.currentTimeMillis()-now)+"ms]",e1);
			}
		  } finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//return i > 0 ? gameChongZhi : null;
		return true;
	}
	@Override
	public GameChongZhi add(GameChongZhi gameChongZhi) {
		long now = System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_GAMECHONGZHI(" +
					"ID," +
					"USERNAME," +
					"TIME," +
					"GAME," +
					
					"FENQU," +
					"GAMELEVEL," +
					"MONEY," +
					
					"ACTION," +
					"CURRENCYTYPE," +
					"REASONTYPE," +
					"QUDAO," +
					"JIXING" +
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?,?, ?,?,? ,?,?,?,? ,?)");
			
			ps.setString(1, gameChongZhi.getUserName());
			ps.setTimestamp(2, new Timestamp(gameChongZhi.getTime().getTime()));
			ps.setString(3, gameChongZhi.getGame());
			ps.setString(4, gameChongZhi.getFenQu());
			ps.setString(5, gameChongZhi.getGameLevel());
			ps.setLong(6, gameChongZhi.getMoney());
			
			ps.setInt(7, gameChongZhi.getAction());
			ps.setString(8, gameChongZhi.getCurrencyType());
			ps.setString(9, gameChongZhi.getReasonType());
			ps.setString(10, gameChongZhi.getQuDao());
			
			ps.setString(11, gameChongZhi.getJixing());
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[添加用户游戏充值] [OK] "+gameChongZhi.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		   } catch (Exception e) {
			logger.error("[添加用户游戏充值] [fail] "+gameChongZhi.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		  } finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? gameChongZhi : null;
	}

	@Override
	public boolean deleteById(Long id) {


		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("delete from STAT_GAMECHONGZHI where ID=?");
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
	public GameChongZhi getById(Long id) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from STAT_GAMECHONGZHI where id=?");
			ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			if(rs.next()) {
				GameChongZhi gameChongZhi = new GameChongZhi();
				
				gameChongZhi.setAction(rs.getInt("ACTION"));
				gameChongZhi.setCurrencyType(rs.getString("CURRENCYTYPE"));
				gameChongZhi.setFenQu(rs.getString("FENQU"));
				gameChongZhi.setGame(rs.getString("GAME"));
				
				gameChongZhi.setGameLevel(rs.getString("GAMELEVEL"));
				gameChongZhi.setId(rs.getLong("ID"));
				gameChongZhi.setMoney(rs.getLong("MONEY"));
				gameChongZhi.setQuDao(rs.getString("QUDAO"));
				
				gameChongZhi.setReasonType(rs.getString("REASONTYPE"));
				gameChongZhi.setTime(rs.getTimestamp("TIME"));
				gameChongZhi.setUserName(rs.getString("USERNAME"));
				gameChongZhi.setJixing(rs.getString("JIXING"));
				
				
				return gameChongZhi;
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
	public ArrayList<GameChongZhi> getBySql(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<GameChongZhi> gameChongZhiList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				GameChongZhi gameChongZhi = new GameChongZhi();
				
				gameChongZhi.setAction(rs.getInt("ACTION"));
				gameChongZhi.setCurrencyType(rs.getString("CURRENCYTYPE"));
				gameChongZhi.setFenQu(rs.getString("FENQU"));
				gameChongZhi.setGame(rs.getString("GAME"));
				
				gameChongZhi.setGameLevel(rs.getString("GAMELEVEL"));
				gameChongZhi.setId(rs.getLong("ID"));
				gameChongZhi.setMoney(rs.getLong("MONEY"));
				gameChongZhi.setQuDao(rs.getString("QUDAO"));
				
				gameChongZhi.setReasonType(rs.getString("REASONTYPE"));
				gameChongZhi.setTime(rs.getTimestamp("TIME"));
				gameChongZhi.setUserName(rs.getString("USERNAME"));
				gameChongZhi.setJixing(rs.getString("JIXING"));
				
				gameChongZhiList.add(gameChongZhi);
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
		return gameChongZhiList;
	}

	@Override
	public ArrayList<String []> getCurrencyType() {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> currencyTypeList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from dt_currencytype t");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String [] reslut=new String[2];
				String id=rs.getString("id");
				String name=rs.getString("name");
				reslut[0]=id;
				reslut[1]=name;
					
				currencyTypeList.add(reslut);
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
		return currencyTypeList;
	}

	@Override
	public ArrayList<String []> getReasontypeType() {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> reasontypeTypeList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from dt_reasontype t");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String [] result=new String[2];
				String id=rs.getString("id");
				String name=rs.getString("name");
				result[0]=id;
				result[1]=name;
					reasontypeTypeList.add(result);
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
		return reasontypeTypeList;
	}
	
	
	
	public List<String[]> getChongZhiStat_reasontype(String startDateStr,String endDateStr,String fenQu,String gameLevel, String currencyType,String reasontype,String action,String username) {
		String subSql=" ";

		if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and tt.fenqu='"+fenQu+"' ";}
		if(gameLevel!=null&&!gameLevel.isEmpty()){subSql+=" and tt.gamelevel in ("+gameLevel+")";}
		if(currencyType!=null&&!currencyType.isEmpty()){subSql+=" and tt.currencytype='"+currencyType+"' " ;}
		if(reasontype!=null&&!reasontype.isEmpty()){subSql+=" and tt.reasontype='"+reasontype+"' " ;}
		if(action!=null&&!action.isEmpty()){subSql+=" and tt.action='"+action+"' ";}
		if(username!=null&&!username.isEmpty()){subSql+=" and tt.username='"+username+"'";}
		
		String sql="select sum(tt.money) money,dt.name currencytype from stat_gamechongzhi tt,dt_reasontype dt " +
				" where 1=1 " +subSql+
				" and tt.time between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
				" and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') and tt.reasontype=dt.id" +
				" group by dt.name order by dt.name desc";
		return queryChongZhiStat(sql);
	}
	
	
	
	@Override
	public List<String[]> getChongZhiStat(String startDateStr,String endDateStr,String fenQu,String gameLevel, String currencyType,String reasontype,String action) {
		String subSql=" ";

		if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and tt.fenqu in ('"+fenQu+"') ";}
		if(gameLevel!=null&&!gameLevel.isEmpty()){subSql+=" and tt.gamelevel in ("+gameLevel+")";}
		if(currencyType!=null&&!currencyType.isEmpty()){subSql+=" and tt.currencytype='"+currencyType+"' " ;}
		if(reasontype!=null&&!reasontype.isEmpty()){subSql+=" and tt.reasontype='"+reasontype+"' " ;}
		if(action!=null&&!action.isEmpty()){subSql+=" and tt.action='"+action+"' ";}
		
		String sql="select sum(tt.money) money,dt.name currencytype from stat_gamechongzhi tt,dt_currencytype dt " +
				" where 1=1 " +subSql+
				" and tt.time between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
				" and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') and tt.currencytype=dt.id" +
				" group by dt.name ";
		return queryChongZhiStat(sql);
	}
	 private List<String[]> queryChongZhiStat(String sql) {

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
					vales[0]=rs.getString("currencytype");
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
		public List<String[]> getPlayerActionWatch(String startDateStr, String endDateStr, String startNum, String endNum, String fenQu, String quDao,
				String gameLevel, String currencyType, String reasontype, String action) {

			String subSql=" ";
			
			if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"' ";}
			//if(gameLevel!=null&&!gameLevel.isEmpty()){subSql+=" and tt.gamelevel in ("+gameLevel+")";}
			if(quDao!=null&&!"".equals(quDao)){subSql+=" and t.qudao='"+quDao+"'";}
			if(currencyType!=null&&!currencyType.isEmpty()){subSql+=" and  t.currencytype='"+currencyType+"' " ;}
			if(reasontype!=null&&!reasontype.isEmpty()){subSql+=" and t.reasontype='"+reasontype+"' " ;}
			if(action!=null&&!action.isEmpty()){subSql+="and t.action='"+action+"'";}
			
			
			String sql="select * from (" +
					" select m.*,ROWNUM rn from " +
					"(" +
					"select t.username, sum(t.money) money" +
					"  from stat_gamechongzhi t " +
					" where 1=1" +subSql+
					"  and t.time between to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
					" group by t.username" +
					" order by  sum(t.money) desc" +
					" ) m" +
					" )tt" +
					" where tt.rn between "+startNum+" and "+endNum+"";
			
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
					vales[0]=rs.getString("username");
					vales[1]=rs.getString("money");
					vales[2]=rs.getString("rn");
					
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