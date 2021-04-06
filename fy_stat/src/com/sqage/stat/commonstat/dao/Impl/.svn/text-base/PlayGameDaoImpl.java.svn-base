package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.PlayGameDao;
import com.sqage.stat.commonstat.entity.FuMo;
import com.sqage.stat.commonstat.entity.PlayGame;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class PlayGameDaoImpl implements PlayGameDao {
	static Logger logger = Logger.getLogger(PlayGameDaoImpl.class);

	DataSourceManager dataSourceManager;
	@Override
	public PlayGame add(PlayGame playGame) {
        long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_PLAYGAME(" +
					"ID," +
					"ENTERDATE," +
					//"USERID," +
					"ENTERTIMES," +
					
					"ONLINETIME," +
					"GAME," +
					"CHONGZHISHU," +
					
					"XIAOFEISHU," +
					"FENQU," +
					"MAXLEVEL," +
					"MINLEVEL," +
					"USERNAME," +
					
					"YUANBAOCOUNT,"+
					"YOUXIBI," +
					"QUDAO,"+
					"JIXING,"+
					
					"ZHIYE," +
					"COLUMN1,"+
					"COLUMN2"+
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,? ,?,?,?, ?, ?,?,?)");
			if (playGame.getEnterDate() != null) {
				ps.setDate(1, new java.sql.Date(playGame.getEnterDate().getTime()));
			} else {
				ps.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
			}
			//ps.setDate(1, new java.sql.Date(playGame.getEnterDate().getTime()));
			//ps.setLong(2, playGame.getUserId());
			ps.setLong(2, playGame.getEnterTimes()==null ? 0L : playGame.getEnterTimes());
			
			ps.setLong(3, playGame.getOnLineTime()==null ? 0L :playGame.getOnLineTime());
			ps.setString(4, playGame.getGame());
			ps.setLong(5, playGame.getChongZhiShu()==null ? 0L : playGame.getChongZhiShu());
			
			ps.setLong(6, playGame.getXiaoFeiShu()==null ? 0L : playGame.getXiaoFeiShu());
			ps.setString(7, playGame.getFenQu());
			ps.setLong(8, playGame.getMaxLevel()==null ? 0L : playGame.getMaxLevel());
			ps.setLong(9, playGame.getMinLevel()==null ? 0L : playGame.getMinLevel());
			ps.setString(10, playGame.getUserName());
			
			ps.setLong(11, playGame.getYuanBaoCount()==null ? 0L : playGame.getYuanBaoCount());
			ps.setLong(12, playGame.getYouXiBi()==null ? 0L : playGame.getYouXiBi());
			ps.setString(13, playGame.getQuDao());
			ps.setString(14, playGame.getJixing());
			
			ps.setString(15, playGame.getZhiye());
			ps.setString(16, playGame.getColumn1());
			ps.setString(17, playGame.getColumn2());
			
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[添加进入游戏信息] [OK]" +playGame.toString() +"[cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		    } catch (Exception e) {
			   logger.error("[添加进入游戏信息] [Fail]"+playGame.toString() +"[cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		   } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? playGame : null;
	}
	public boolean addPlayGameList(ArrayList<PlayGame> playGameList) {
		long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		boolean result=true;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_PLAYGAME(" +
					"ID," +
					"ENTERDATE," +
					//"USERID," +
					"ENTERTIMES," +
					
					"ONLINETIME," +
					"GAME," +
					"CHONGZHISHU," +
					
					"XIAOFEISHU," +
					"FENQU," +
					"MAXLEVEL," +
					"MINLEVEL," +
                    "USERNAME," +
					
					"YUANBAOCOUNT,"+
					"YOUXIBI," +
					"QUDAO,"+
					"JIXING,"+
					
					"ZHIYE," +
					"COLUMN1,"+
					"COLUMN2"+
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,? ,?,? ,?,?, ?,?,?)");
			
			for(PlayGame playGame:playGameList){
				if (playGame.getEnterDate() != null) {
					ps.setDate(1, new java.sql.Date(playGame.getEnterDate().getTime()));
				} else {
					ps.setDate(1, null);
				}

			//ps.setLong(2, playGame.getUserId());
			ps.setLong(2, playGame.getEnterTimes()==null ? 0L : playGame.getEnterTimes());
			
			ps.setLong(3, playGame.getOnLineTime()==null ? 0L : playGame.getOnLineTime());
			ps.setString(4, playGame.getGame());
			ps.setLong(5, playGame.getChongZhiShu()==null ? 0L : playGame.getChongZhiShu());
			
			ps.setLong(6, playGame.getXiaoFeiShu()==null ? 0L : playGame.getXiaoFeiShu());
			ps.setString(7, playGame.getFenQu());
			ps.setLong(8, playGame.getMaxLevel()==null ? 0L: playGame.getMaxLevel());
			ps.setLong(9, playGame.getMinLevel()==null ? 0L :playGame.getMinLevel());
			ps.setString(10, playGame.getUserName());
			
			ps.setLong(11, playGame.getYuanBaoCount()==null ? 0L : playGame.getYuanBaoCount());
			ps.setLong(12, playGame.getYouXiBi()==null ? 0L : playGame.getYouXiBi());
			ps.setString(13, playGame.getQuDao());
			ps.setString(14, playGame.getJixing());
			
			ps.setString(15, playGame.getZhiye());
			ps.setString(16, playGame.getColumn1());
			ps.setString(17, playGame.getColumn2());
			
			i = ps.executeUpdate();
			if(i <=0){result=false;}
			}
			con.commit();
			con.setAutoCommit(true);
			if(logger.isDebugEnabled()){
				logger.debug("[添加进入游戏信息] [OK] [" +playGameList.size() +"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			result=false;
			logger.debug("[多条添加进入游戏信息] [OK] [" +playGameList.size() +"条] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (Exception e) {
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
			ps = con.prepareStatement("delete from STAT_PLAYGAME where ID=?");
			ps.setLong(1, id);
			i = ps.executeUpdate();
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
		return i > 0;
	}

	@Override
	public PlayGame getById(Long id) {

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from STAT_PLAYGAME where id=?");
			ps.setLong(1, id);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if (rs.next()) {
				PlayGame playGame = new PlayGame();

				playGame.setChongZhiShu(rs.getLong("CHONGZHISHU"));
				playGame.setEnterDate(rs.getDate("ENTERDATE"));
				playGame.setEnterTimes(rs.getLong("ENTERTIMES"));
				playGame.setFenQu(rs.getString("FENQU"));

				playGame.setId(rs.getLong("ID"));
				playGame.setMaxLevel(rs.getLong("MAXLEVEL"));
				playGame.setMinLevel(rs.getLong("MINLEVEL"));
				playGame.setOnLineTime(rs.getLong("ONLINETIME"));

				playGame.setUserId(rs.getLong("USERID"));
				playGame.setXiaoFeiShu(rs.getLong("XIAOFEISHU"));
				playGame.setGame(rs.getString("GAME"));
				playGame.setUserName(rs.getString("USERNAME"));
				
				playGame.setYuanBaoCount(rs.getLong("YUANBAOCOUNT"));
				playGame.setYouXiBi(rs.getLong("YOUXIBI"));
				playGame.setQuDao(rs.getString("QUDAO"));
				playGame.setJixing(rs.getString("JIXING"));

				return playGame;
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
		return null;
	}
	
	@Override
	public ArrayList<String[]> getPlayGameData(String sql, String[] strs) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[strs.length];
				for(int i=0;i<strs.length;i++){
					vales[i]=rs.getString(strs[i]);
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
	

	
	
	 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public boolean merge(ArrayList<PlayGame> playGameList) {
        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			
			 String sql = " merge into STAT_PLAYGAME t " +
	    		" using (select ? as username,? as fenq from dual) newData " +
	    		" ON (t.username=newData.username and t.fenqu=newData.fenq  and " +
	    		" t.enterdate between to_date(?,'YYYY-MM-DD hh24:mi:ss')" +
	    		" and  to_date(?,'YYYY-MM-DD hh24:mi:ss')) " +
	    		" WHEN MATCHED THEN " +
	    		" UPDATE SET t.ENTERTIMES= t.ENTERTIMES+1,t.ONLINETIME=t.ONLINETIME+?,t.MAXLEVEL=?"+
	            " WHEN NOT MATCHED THEN " +
		        " INSERT (" +
		        " ID,ENTERDATE,ENTERTIMES,ONLINETIME,GAME,CHONGZHISHU,XIAOFEISHU," +
				"FENQU,MAXLEVEL,MINLEVEL,USERNAME,YUANBAOCOUNT,YOUXIBI," +
				"QUDAO,JIXING,ZHIYE,COLUMN1,COLUMN2"+
				
				") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,? ,?,?,?, ?, ?,?,?)";
		 
		ps = con.prepareStatement(sql);
	    int i = -1;
		int count=0;
	for(PlayGame playGame : playGameList){
		String key=playGame.getFenQu()+playGame.getUserName();
		//System.out.println("-------------------"+"key:"+key);
		ps.setString(1,playGame.getUserName());
		ps.setString(2,playGame.getFenQu());
		String startday=sf.format(playGame.getEnterDate())+" 00:00:00";
		String endday  =sf.format(playGame.getEnterDate())+" 23:59:59";
		ps.setString(3,startday);
		ps.setString(4,endday);
		
		ps.setLong(5, playGame.getOnLineTime()==null ? 0L : playGame.getOnLineTime());
		ps.setLong(6, playGame.getMaxLevel()==null ? 0L : playGame.getMaxLevel());
		
		
		if (playGame.getEnterDate() != null) {
			ps.setDate(7, new java.sql.Date(playGame.getEnterDate().getTime()));
		} else {
			ps.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
		}
		ps.setLong(8, playGame.getEnterTimes()==null ? 0L : playGame.getEnterTimes());
		ps.setLong(9, playGame.getOnLineTime()==null ? 0L :playGame.getOnLineTime());
		ps.setString(10, playGame.getGame());
		ps.setLong(11, playGame.getChongZhiShu()==null ? 0L : playGame.getChongZhiShu());
		
		ps.setLong(12, playGame.getXiaoFeiShu()==null ? 0L : playGame.getXiaoFeiShu());
		ps.setString(13, playGame.getFenQu());
		ps.setLong(14, playGame.getMaxLevel()==null ? 0L : playGame.getMaxLevel());
		ps.setLong(15, playGame.getMinLevel()==null ? 0L : playGame.getMinLevel());
		ps.setString(16, playGame.getUserName());
		
		ps.setLong(17, playGame.getYuanBaoCount()==null ? 0L : playGame.getYuanBaoCount());
		ps.setLong(18, playGame.getYouXiBi()==null ? 0L : playGame.getYouXiBi());
		ps.setString(19, playGame.getQuDao()==null ? "null" : playGame.getQuDao());
		ps.setString(20, playGame.getJixing()==null ? "null" : playGame.getJixing());
		
		ps.setString(21, playGame.getZhiye()==null ? "null" : playGame.getZhiye());
		ps.setString(22, playGame.getColumn1());
		ps.setString(23, playGame.getColumn2());
			
			
			
			
			
			
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%200==0){con.commit(); }
		   }
			con.commit(); 
			con.setAutoCommit(true);
			   
			if(logger.isDebugEnabled()){logger.debug("玩家进入游戏"+playGameList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("玩家进入游戏异常",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[玩家进入游戏异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[玩家进入游戏异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	
	}
	public ArrayList<String> getQuDaoReg_LoginUserCount(String dateStr){
 		ArrayList<String> levelUserCount=new ArrayList<String>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String key=null;
		String value=null;
//		String sql="select t.qudao qudao,count(distinct(t.name)) qudaonum from stat_user t,stat_playgame p where " +
//		" to_char(p.enterdate,'YYYY-MM-DD')=to_char(t.registtime,'YYYY-MM-DD') and p.username=t.name " +
//		" and to_char(p.enterdate,'YYYY-MM-DD')= '"+dateStr+"'  group by t.qudao";
		
		String sql=" select t.qudao qudao, count(distinct(t.name)) qudaonum from stat_user t " +
		" where t.registtime > to_date('"+dateStr+"','YYYY-MM-DD') and t.registtime < to_date('"+dateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and t.name in (" +
		" select p.username from stat_playgame p where p.enterdate>= to_date('"+dateStr+"','YYYY-MM-DD') " +
				" and p.enterdate <  to_date('"+dateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')) " +
						" group by t.qudao";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			while(rs.next()){
			key = rs.getString("qudao");
			value=rs.getString("qudaonum");
			levelUserCount.add(key+" "+value);
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
		return levelUserCount;
	}
	
	
	public ArrayList<String> getQuDaoRetainUserCount_createplayer(String registDateStr,String statdateStr,String fenqu,String jixing)
	{
		String subSql="";
		if(fenqu!=null&&!"".equals(fenqu)){
			subSql+=" and p.fenqu='"+fenqu+"'";
		}
		if(jixing!=null&&!"".equals(jixing)){
			subSql+=" and p.jixing='"+jixing+"'";
		}
		
		ArrayList<String> levelUserCount=new ArrayList<String>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String key=null;
		String value=null;
		
		String sql=" select p.qudao qudao, count(distinct(p.username)) qudaonum  from stat_playgame p " +
				"where p.enterdate >= to_date('"+statdateStr+"','YYYY-MM-DD') and  p.enterdate < to_date('"+statdateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +subSql+
				" and p.username in " +
				" ( select u.name from stat_user u,Stat_Playgame P where  u.registtime > to_date('"+registDateStr+"','YYYY-MM-DD') " +
						" and u.registtime< to_date('"+registDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')  " +
						 "and p.enterdate between to_date('"+registDateStr+"','YYYY-MM-DD') and to_date('"+registDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
						 "and u.name=p.username" +
						
								") "+
				" group by p.qudao";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			while(rs.next()){
			key = rs.getString("qudao");
			value=rs.getString("qudaonum");
			levelUserCount.add(key+" "+value);
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
		return levelUserCount;
	}
	
	
	public ArrayList<String> getQuDaoRetainUserCount(String registDateStr,String statdateStr,String fenqu,String jixing)
	{
		String subSql="";
		if(fenqu!=null&&!"".equals(fenqu)){
			subSql+=" and p.fenqu='"+fenqu+"'";
		}
		if(jixing!=null&&!"".equals(jixing)){
			subSql+=" and p.jixing='"+jixing+"'";
		}
		
		ArrayList<String> levelUserCount=new ArrayList<String>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String key=null;
		String value=null;

//	String sql=" select p.qudao qudao, count(distinct(p.username)) qudaonum  from STAT_PLAYGAME_TEMP2 p " +
//		"where to_char(p.enterdate, 'YYYY-MM-DD') = '"+statdateStr+"' " +subSql+
//		" and p.username in " +
//		" ( select u.name from stat_user u  where to_char(u.registtime, 'YYYY-MM-DD') = '"+registDateStr+"')" +
//		" group by p.qudao";
		
		String sql=" select p.qudao qudao, count(distinct(p.username)) qudaonum  from STAT_PLAYGAME p " +
				"where p.enterdate >= to_date('"+statdateStr+"','YYYY-MM-DD') and p.enterdate < to_date('"+statdateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +subSql+
				" and p.username in " +
				" ( select u.name from stat_user u  where u.registtime > to_date('"+registDateStr+"','YYYY-MM-DD') and u.registtime<to_date('"+registDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') )" +
				" group by p.qudao";
		
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			while(rs.next()){
			key = rs.getString("qudao");
			value=rs.getString("qudaonum");
			levelUserCount.add(key+" "+value);
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
		return levelUserCount;
	}
	
	public ArrayList<String> getFenQuRetainUserCount(String registDateStr,String statdateStr,String qudao)
	{
		ArrayList<String> levelUserCount=new ArrayList<String>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String key=null;
		String value=null;
		String subSql="";
		if(qudao!=null&&!"".equals(qudao)){
			subSql+=" and qudao='"+qudao+"'";
		}
		
		String sql="select  u.fenqu qudao,count(distinct(u.name)) qudaonum from stat_playgame p,stat_user u where " +
		        " p.username=u.name and   u.registtime > to_date('"+registDateStr+"','YYYY-MM-DD') and  u.registtime < to_date('"+registDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +
				" and  p.enterdate >= to_date('"+statdateStr+"','YYYY-MM-DD') and  " +
				" p.enterdate < to_date('"+statdateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') group by u.fenqu";

		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			while(rs.next()){
			key = rs.getString("qudao");
			value=rs.getString("qudaonum");
			levelUserCount.add(key+" "+value);
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
		return levelUserCount;
	}
	
	@Override
	public ArrayList<String> getSepFenQuRetainUserCount(String registDateStr, String statdateStr, String fenqu,String quDao,String jixing) {
		ArrayList<String> levelUserCount=new ArrayList<String>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String key=null;
		String value=null;
		String subSql="";
		if(fenqu!=null&&!"".equals(fenqu)){
			subSql+=" and p.fenqu='"+fenqu+"'";
		}
		if(quDao!=null&&!"".equals(quDao)){
			subSql+=" and p.quDao='"+quDao+"'";
		}
		
		if(jixing!=null&&!"".equals(jixing)){
			subSql+=" and p.jixing='"+jixing+"'";
		}
		
		String sql="select  count(distinct(u.name)) qudaonum from STAT_PLAYGAME_TEMP2 p,stat_user u where " +
		        " p.username=u.name and   u.registtime > to_date('"+registDateStr+"','YYYY-MM-DD') and  " +
		        " u.registtime < to_date('"+registDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +
				" and  p.enterdate >= to_date('"+statdateStr+"','YYYY-MM-DD') and  " +
				" p.enterdate < to_date('"+statdateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "+subSql;

		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			while(rs.next()){
			value=rs.getString("qudaonum");
			levelUserCount.add(value);
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
		return levelUserCount;
	}
	public HashMap<String,Integer> getQuDaoRetainUserCount_reg(String registDateStr,String statdateStr,String fenqu,String jixing)
	{
		String subSql="";
//		if(fenqu!=null&&!"".equals(fenqu)){
//			subSql+=" and u.fenqu='"+fenqu+"'";
//		}
		if(jixing!=null&&!"".equals(jixing)){
			subSql+=" and u.jixing='"+jixing+"'";
		}
		
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String key=null;
		Integer value=0;
		String sql=" select u.qudao qudao, count(distinct(u.name)) qudaonum  from stat_user u " +
				" where u.registtime >to_date('"+registDateStr+"','YYYY-MM-DD') and u.registtime < to_date('"+registDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
						" group by u.qudao";

		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			while(rs.next()){
			key = rs.getString("qudao");
			value=rs.getInt("qudaonum");
			map.put(key, value);
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
		return map;
	}
	
	public ArrayList<String> getQuDaoActivityOldUserCount(String dateStr,String fenQu)
	{
		ArrayList<String> levelUserCount=new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String key=null;
		String value=null;
		String subsql="";
		if(fenQu!=null&&!"".equals(fenQu)){ subsql+=" and p.fenqu='"+fenQu+"'";}
		String sql="select t.qudao qudao,count(distinct(t.name)) qudaonum from stat_user t,stat_playgame p " +
				" where 1=1 " +subsql+
				" and to_char(p.enterdate,'YYYY-MM-DD') <> to_char(t.registtime,'YYYY-MM-DD') and " +
		        " p.username=t.name and p.enterdate >= to_date('"+dateStr+"','YYYY-MM-DD') and " +
		        		" p.enterdate< to_date('"+dateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') group by t.qudao";
		//System.out.println(sql);
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			while(rs.next()){
			key = rs.getString("qudao");
			value=rs.getString("qudaonum");
			levelUserCount.add(key+" "+value);
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
		return levelUserCount;
	}
	
	public ArrayList<String> getQuDaoActivityUserCount(String  dateStr) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String> levelUserCount=new ArrayList();
		ResultSet rs=null;
		String key=null;
		String value=null;
//		String sql = "select b.qudao qudao, count(distinct(b.name)) qudaonum from stat_playgame a, stat_user b where " +
//				"to_char(enterdate,'YYYY-MM-DD')='"+dateStr+"' and a.username = b.name group by b.qudao";
		
		String sql = " select a.qudao qudao, count(distinct(a.username)) qudaonum from stat_playgame a where " +
		" a.enterdate >= to_date('"+dateStr+"','YYYY-MM-DD') and a.enterdate < to_date('"+dateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +
				" group by a.qudao";
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			while(rs.next()){
			key = rs.getString("qudao");
			value=rs.getString("qudaonum");
			levelUserCount.add(key+" "+value);
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
		return levelUserCount;
	}
	
	public ArrayList<String> getGuojiaActivityUserCount(String  dateStr) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String> levelUserCount=new ArrayList();
		ResultSet rs=null;
		String key=null;
		String value=null;

		String sql="select b.guojia guojia , count(*) guojianum from stat_playgame a, stat_user b " +
				"where a.enterdate between to_date('"+dateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  " +
						" to_date('"+dateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')  and a.username||a.fenqu=b.name||b.fenqu " +
								"	and b.guojia  is not null group by b.guojia";
		
//		String sql="select a.game guojia , count(distinct(a.username)) guojianum from stat_playgame a " +
//		" where  a.enterdate between to_date('"+dateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  to_date('"+dateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
//				"  group by a.game";

		
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			while(rs.next()){
			key = rs.getString("guojia");
			value=rs.getString("guojianum");
			levelUserCount.add(key+" "+value);
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
		return levelUserCount;
	}
	
	@Override
	public List<PlayGame> getBySql(String sql) {

		Connection con = null;
		PreparedStatement ps = null;
		List<PlayGame> playGameList=null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			//ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			playGameList=new ArrayList();
			while(rs.next()) {
				PlayGame playGame = new PlayGame();
				
				playGame.setChongZhiShu(rs.getLong("CHONGZHISHU"));
				playGame.setEnterDate(rs.getDate("ENTERDATE"));
				playGame.setEnterTimes(rs.getLong("ENTERTIMES"));
				playGame.setFenQu(rs.getString("FENQU"));
				
				playGame.setId(rs.getLong("ID"));
				playGame.setMaxLevel(rs.getLong("MAXLEVEL"));
				playGame.setMinLevel(rs.getLong("MINLEVEL"));
				playGame.setOnLineTime(rs.getLong("ONLINETIME"));
				
				playGame.setUserId(rs.getLong("USERID"));
				playGame.setXiaoFeiShu(rs.getLong("XIAOFEISHU"));
				playGame.setGame(rs.getString("GAME"));
				playGame.setUserName(rs.getString("USERNAME"));
				
				playGame.setYuanBaoCount(rs.getLong("YUANBAOCOUNT"));
				playGame.setYouXiBi(rs.getLong("YOUXIBI"));
				
				playGameList.add(playGame);
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
		return playGameList;
	}

	@Override
	public boolean update(PlayGame playGame) {
        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_PLAYGAME set "+
					"ENTERDATE=?," +
					//"USERID=?," +
					"ENTERTIMES=?," +
					
					"ONLINETIME=?," +
					"GAME=?," +
					"CHONGZHISHU=?," +
					
					"XIAOFEISHU=?," +
					"FENQU=?," +
					"MAXLEVEL=?," +
					"MINLEVEL=?, " +
					"USERNAME=?," +
					
					"YUANBAOCOUNT=?," +
					"YOUXIBI=? " +
					 
					" where ID=?");
			if (playGame.getEnterDate() != null) {
				ps.setDate(1, new java.sql.Date(playGame.getEnterDate().getTime()));
			} else {
				ps.setDate(1, null);
			}

			//ps.setLong(2, playGame.getUserId());
			ps.setLong(2, playGame.getEnterTimes()==null ? 0L : playGame.getEnterTimes());
			
			ps.setLong(3, playGame.getOnLineTime()==null ? 0L : playGame.getOnLineTime());
			ps.setString(4,playGame.getGame());
			ps.setLong(5,playGame.getChongZhiShu()==null ? 0L : playGame.getChongZhiShu());
			
			ps.setLong(6, playGame.getXiaoFeiShu()==null ? 0L : playGame.getXiaoFeiShu());
			ps.setString(7,playGame.getFenQu());
			ps.setLong(8,playGame.getMaxLevel()==null ? 0L : playGame.getMaxLevel());
			ps.setLong(9, playGame.getMinLevel()==null ? 0L : playGame.getMinLevel());
			ps.setString(10, playGame.getUserName());
			
			ps.setLong(11, playGame.getYuanBaoCount()==null ? 0L : playGame.getYuanBaoCount());
			ps.setLong(12, playGame.getYouXiBi()==null ? 0L :  playGame.getYouXiBi());
			
			ps.setLong(13,playGame.getId());
			
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[更新进入游戏信息] [OK] "+playGame.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			   }
		    } catch (Exception e) {
			   logger.error("[更新进入游戏信息] [FAIL] "+playGame.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		   } finally {
			 try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i > 0;
	}
	
	public boolean updatePlayGameList(ArrayList<PlayGame> playGameList) {
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		boolean result=true;
		try {
			con = dataSourceManager.getInstance().getConnection();
			for(PlayGame playGame:playGameList){
			ps = con.prepareStatement("update STAT_PLAYGAME set "+
					"ENTERDATE=?," +
					//"USERID=?," +
					"ENTERTIMES=?," +
					
					"ONLINETIME=?," +
					"GAME=?," +
					"CHONGZHISHU=?," +
					
					"XIAOFEISHU=?," +
					"FENQU=?," +
					"MAXLEVEL=?," +
					"MINLEVEL=?, " +
					"USERNAME=?," +
					
					
					"YUANBAOCOUNT=?," +
					"YOUXIBI=? " +
					
					" where ID=?");
				if (playGame.getEnterDate() != null) {
					ps.setDate(1, new java.sql.Date(playGame.getEnterDate().getTime()));
				} else {
					ps.setDate(1, null);
				}
			//ps.setLong(2, playGame.getUserId());
			ps.setLong(2, playGame.getEnterTimes()==null ? 0L : playGame.getEnterTimes());
			
			ps.setLong(3, playGame.getOnLineTime()==null ? 0L : playGame.getOnLineTime());
			ps.setString(4,playGame.getGame());
			ps.setLong(5,playGame.getChongZhiShu()==null ? 0L : playGame.getChongZhiShu());
			
			ps.setLong(6, playGame.getXiaoFeiShu()==null ? 0L : playGame.getXiaoFeiShu());
			ps.setString(7,playGame.getFenQu());
			ps.setLong(8,playGame.getMaxLevel()==null ? 0L : playGame.getMaxLevel());
			ps.setLong(9, playGame.getMinLevel()==null ? 0L : playGame.getMinLevel());
			ps.setString(10, playGame.getUserName());
			
			ps.setLong(11, playGame.getYuanBaoCount()==null ? 0L : playGame.getYuanBaoCount());
			ps.setLong(12, playGame.getYouXiBi()==null ? 0L :  playGame.getYouXiBi());
			ps.setLong(13,playGame.getId());
			
			i =ps.executeUpdate();
			if(i<=0){result=false;}
			if(ps != null) ps.close();
			}
			
		} catch (Exception e) {
			result=false;
			e.printStackTrace();
		} finally {
			try {
				if(con != null) con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 获取进入的用户数分天(排重)
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
		public List<String []> getEnterGameUserCount_paichong_day(String startDateStr, String endDateStr,String quDao,String fenqu,String jixing) {
			Connection con = null;
			PreparedStatement ps = null;
			ArrayList<String []> ls=new ArrayList<String []>();
			String subSql="";
			if(quDao!=null&&!quDao.isEmpty()){ subSql+="  and t.qudao in ('"+quDao+"')";}
			if(fenqu!=null&&!fenqu.isEmpty()){ subSql+=" and t.fenqu='"+fenqu+"'";}
			if(jixing!=null&&!jixing.isEmpty()) { subSql+=" and t.jixing ='"+jixing+"'";}
			try {
				con = dataSourceManager.getInstance().getConnection();
				String sql=" select to_char(t.enterdate,'YYYY-MM-DD') day,count(t.username) count from stat_playgame t";
				sql+=" where t.enterdate >= to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and " +
						     " t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')"+subSql +
						     		" group by to_char(t.enterdate,'YYYY-MM-DD') order by to_char(t.enterdate,'YYYY-MM-DD')";
				ps = con.prepareStatement(sql);
				ps.execute();
				ResultSet rs = ps.getResultSet();
				while (rs.next()) {
					String [] strs=new String [2];
					String day=rs.getString("day");
					Long count=rs.getLong("count");
					strs[0]=day;
					strs[1]=count.toString();
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
	
	/**
	 * 获取进入的用户数(排重)
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
		public Long getEnterGameUserCount_paichong(String startDateStr, String endDateStr,String quDao,String fenqu,String jixing) {

			Connection con = null;
			PreparedStatement ps = null;
			Long count=null;
			String subSql="";
			if(quDao!=null&&!quDao.isEmpty()){ subSql+="  and t.qudao in ('"+quDao+"')";}
			if(fenqu!=null&&!fenqu.isEmpty()){ subSql+=" and t.fenqu='"+fenqu+"'";}
			if(jixing!=null&&!jixing.isEmpty()) { subSql+=" and t.jixing ='"+jixing+"'";}
			try {
				con = dataSourceManager.getInstance().getConnection();
				String sql=" select count(distinct(t.username)) count from stat_playgame t";
				sql+=" where t.enterdate >= to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and " +
						   " t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')"+subSql;
				ps = con.prepareStatement(sql);
				ps.execute();
				ResultSet rs = ps.getResultSet();
				if (rs.next()) {
					count=rs.getLong("count");
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
			return count;
		}
/**
 * 获取进入的用户数
 * @param startDateStr
 * @param endDateStr
 * @return
 */
	public Long getEnterGameUserCount(String startDateStr, String endDateStr,String quDao,String fenqu,String jixing) {

		Connection con = null;
		PreparedStatement ps = null;
		Long count=null;
		String subSql="";
		if(quDao!=null&&!quDao.isEmpty())   { subSql+="  and t.qudao in ('"+quDao+"')";}
		if(fenqu!=null&&!fenqu.isEmpty())   { subSql+=" and t.fenqu='"+fenqu+"'";}
		if(jixing!=null&&!jixing.isEmpty()) { subSql+=" and t.jixing ='"+jixing+"'";}
		try {
			con = dataSourceManager.getInstance().getConnection();
			String sql=" select count(distinct(t.username||t.fenqu)) count from stat_playgame t";
			sql+=" where t.enterdate >= to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and " +
					   " t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')"+subSql;
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if (rs.next()) {
				count=rs.getLong("count");
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
		return count;
	}
	/**
	 *dateStr天 注册并登陆的用户数
	 * @param dateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getReg_LoginUserCount_temp2(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		Long value=null;
		String subSql="";
		if(qudao!=null&&!qudao.isEmpty()){   subSql+=" and p.qudao in ('"+qudao+"')";}
		if(fenQu!=null&&!fenQu.isEmpty()){   subSql+=" and p.fenqu='"+fenQu+"'";}
		if(jixing!=null&&!jixing.isEmpty()) {subSql+=" and p.jixing='"+jixing+"'";}
		
		String sql="select count(distinct(p.username)) count " +
				" from stat_user t,stat_playgame_temp2 p " +
				" where to_char(p.enterdate,'YYYY-MM-DD')=to_char(t.registtime,'YYYY-MM-DD') and p.username=t.name and p.enterdate " +
		       " >= to_date('"+startDateStr+"','YYYY-MM-DD') and p.enterdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" + subSql;
		

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
	 *dateStr天 注册并登陆的用户数
	 * @param dateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getReg_LoginUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		Long value=null;
		String subSql="";
		if(qudao!=null&&!qudao.isEmpty())   { subSql+=" and p.qudao in ('"+qudao+"')";}
		if(fenQu!=null&&!fenQu.isEmpty())   { subSql+=" and p.fenqu='"+fenQu+"'";}
		if(jixing!=null&&!jixing.isEmpty()) { subSql+=" and p.jixing='"+jixing+"'";}
		
		String sql="select count(distinct(p.username)) count " +
				" from stat_user t,stat_playgame p " +
				" where to_char(p.enterdate,'YYYY-MM-DD')=to_char(t.registtime,'YYYY-MM-DD') and p.username=t.name and p.enterdate " +
		        " >= to_date('"+startDateStr+"','YYYY-MM-DD') and  p.enterdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +subSql;
		

//		 String sql =" select count(distinct(u.name)) count " +
//	 		" from stat_user u " +
//	 		" where to_char(u.registtime,'YYYY-MM-DD') = to_char(u.creat_player_time,'YYYY-MM-DD') " +
//	 		" and to_char(u.creat_player_time,'YYYY-MM-DD') between '"+startDateStr+"' and '"+endDateStr+"' " +
//	 		" and u.fenqu is not null "+subSql ;
	 
		
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
	 * 平均在线时间
	 * @param dateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getAvgOnLineTime(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		Long value=null;
		String subSql="";
		if(qudao!=null&&!qudao.isEmpty()){subSql+=" and t.qudao in ('"+qudao+"')";}
		if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";}
		if(jixing!=null&&!jixing.isEmpty()) {subSql+=" and t.jixing='"+jixing+"'";}
		
		String sql=" select count(t.username) usercount,sum(t.onlinetime) onlinetime " +
				" from stat_playgame t where t.enterdate >= to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
					" and t.enterdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')"+subSql ;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			if(rs.next()){
			Long usercount=rs.getLong("usercount");
			Long onlinetime=rs.getLong("onlinetime");
			if(usercount!=null&&onlinetime!=null&&usercount!=0)
			{
				value=onlinetime/usercount;
			}
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
	 * 剩余元宝数量
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getLeftYuanbaoCount(String startDateStr,String qudao,String fenQu,String game){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		Long value=null;
		String subSql="";
		if(qudao!=null&&!qudao.isEmpty()){subSql+=" and t.qudao='"+qudao+"'";}
		if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";}
		//if(game!=null&&!game.isEmpty()) {subSql+=" and t.game='"+game+"'";}
		
		String sql=" select sum(max(t.yuanbaocount)) count " +
				" from stat_playgame t where t.enterdate < to_date('"+startDateStr+"','YYYY-MM-DD')" +subSql
		        +" group by t.username" ;
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
	 * 剩余游戏币数量
	 * @param startDateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getLeftYouXIBiCount(String startDateStr,String qudao,String fenQu,String game){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		Long value=null;
		String subSql="";
		if(qudao!=null&&!qudao.isEmpty()){subSql+=" and t.qudao='"+qudao+"'";}
		if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";}
		//if(game!=null&&!game.isEmpty()) {subSql+=" and t.game='"+game+"'";}
		
		String sql=" select sum(max(t.YOUXIBI)) count " +
				" from stat_playgame t where t.enterdate < to_date('"+startDateStr+"', 'YYYY-MM-DD')" +subSql
		           +" group by t.username" ;
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
	
	
	public List<String[]> getZaiXianShiChangFenBu_new(String startDateStr, String qudao, String fenQu,String time) {
		
		String subSql="";
		String subSql_new="";
		Long minute_ms=30*60000L;
		if(time!=null){ minute_ms=Long.parseLong(time)*60000; }
		
		if(fenQu!=null&&!"".equals(fenQu)){ subSql+=" and t.fenqu in ('"+fenQu+"') ";
		                                    subSql_new+=" and p.fenqu in ('"+fenQu+"') ";}
		
		String subsql_new=" select distinct(p.username) count from stat_user t, stat_playgame p	 where" +
				" to_char(p.enterdate,'YYYY-MM-DD') = to_char(t.registtime,'YYYY-MM-DD') and p.username = t.name" +subSql_new+
		        " and p.enterdate >= to_date('"+startDateStr+"', 'YYYY-MM-DD') and  p.enterdate < to_date('"+startDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')";
		
		String sql=" select trunc(t.onlinetime/"+minute_ms+") ontlinetime,count(trunc(t.onlinetime/"+minute_ms+")) usercount " +
				" from Stat_Playgame t  where t.enterdate >= to_date('"+startDateStr+"','YYYY-MM-DD') " +
				" and t.enterdate < to_date('"+startDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +
				" and t.username in ("+subsql_new+")"+ subSql+
				" group by trunc(t.onlinetime/"+minute_ms+") order by ontlinetime";
		
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
				vales[0]=rs.getString("ontlinetime");
				vales[1]=rs.getString("usercount");
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
	public List<String[]> getZaiXianShiChangFenBu(String startDateStr, String qudao, String fenQu,String time) {
		
		String subSql="";
		Long minute_ms=30*60000L;
		if(fenQu!=null&&!"".equals(fenQu)){ subSql+=" and t.fenqu in ('"+fenQu+"') ";}
		
		if(time!=null){ minute_ms=Long.parseLong(time)*60000; }
		String sql=" select trunc(t.onlinetime/"+minute_ms+") ontlinetime,count(trunc(t.onlinetime/"+minute_ms+")) usercount " +
				" from Stat_Playgame t  where t.enterdate >= to_date('"+startDateStr+"','YYYY-MM-DD') " +
				" and t.enterdate < to_date('"+startDateStr+"23:59:59','YYYY-MM-DD hh24:mi:ss')" +subSql+
				" group by trunc(t.onlinetime/"+minute_ms+") order by ontlinetime";
		
//		String subsql_new=" select distinct(p.username) count from stat_user t, stat_playgame p	 where " +
//				" to_char(p.enterdate,'YYYY-MM-DD') = to_char(t.registtime,'YYYY-MM-DD') and p.username = t.name" +
//				"  and p.enterdate> to_date('"+startDateStr+"', 'YYYY-MM-DD') and p.enterdate < to_date('"+startDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')";
		
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
				vales[0]=rs.getString("ontlinetime");
				vales[1]=rs.getString("usercount");
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
	public List<String[]> getPlayerLevelFenBu_sum(String sql) {
		
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
				vales[0]=rs.getString("head");
				vales[1]=rs.getString("l1");
				vales[2]=rs.getString("l2");
				vales[3]=rs.getString("l3");
				
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
	public List<String[]> getPlayerLevelFenBu(String startRegStr, String endRegStr, String statDateStr, String fenQu, String sex, String nation) {

		String subSql="";
		String psql="";
		if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and u.fenqu='"+fenQu+"'"; psql=" and p.fenqu ='"+fenQu+"'";}
		if(sex!=null&&!"".equals(sex)){subSql+=" and u.sex='"+sex+"'";}
		if(nation!=null&&!"".equals(nation)){subSql+=" and u.guojia='"+nation+"'";}
		
		String sql=" select t.mlevel, count(t.mlevel) count from (" +
				" select p.username, max(p.maxlevel) mlevel from stat_playgame p where 1=1 " +psql+
				" and  p.username in (" +
				" select u.name from stat_user u where " +
				"  u.registtime > to_date('"+startRegStr+"','YYYY-MM-DD') and  u.registtime< to_date('"+endRegStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +subSql+
				" )" +
				" and p.enterdate < to_date('"+statDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
				" group by p.username " +
				" ) t group by t.mlevel " +
				" order by t.mlevel";
		
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
				vales[0]=rs.getString("mlevel");
				vales[1]=rs.getString("count");
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
	public List<String[]> getLostPlayerLevelFenBu(String startRegStr, String endRegStr, String fenQu, String qudao, String nation, String dayNum) {

		String subSql="";
		String psql="";
		if(fenQu!=null&&!"".equals(fenQu)) { subSql=" and p.fenqu ='"+fenQu+"'";}
		if(qudao!=null&&!"".equals(qudao)) { subSql=" and p.qudao ='"+qudao+"'";}
		
		if(nation!=null&&!"".equals(nation)){psql+=" and u.guojia='"+nation+"'";}
		
		String sql="select mlevel,count(*) count from  " +
				" ( " +
				" select p.username,p.fenqu,max(p.maxlevel) mlevel,floor(sysdate-max(p.enterdate)) from stat_playgame p " +
				" where p.username in " +
				" ( " +
				" select u.name from stat_user u where u.registtime > to_date('"+startRegStr+"','YYYY-MM-DD') " +
				" and u.registtime <  to_date('"+endRegStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +psql+
				" ) " +subSql+
				" group by p.username,p.fenqu " +
				" having sysdate-max(p.enterdate) >"+dayNum+ 
				" ) tt group by tt.mlevel order by tt.mlevel ";
		
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
				vales[0]=rs.getString("mlevel");
				vales[1]=rs.getString("count");
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
	
	
	////////////////////////////周统计信息Start///////////////
	@Override
	public String active_pay_AVGTimes(String startDateStr, String endDateStr, String fenQu,String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql=" and c.fenqu+='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql=" and c.qudao+='"+qudao+"'";}
		
		String baseSql=getBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select count(distinct(c.username)) count, count(c.username) times from stat_chongzhi c " +
				" where c.time between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
				" and to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")" +subSql;
		String result=getDbFloatValue(sql);
		return result;
	}
	@Override
	public String active_pay_Money(String startDateStr, String endDateStr, String fenQu,String qudao) {

		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		String baseSql=getBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select sum(c.money)/100 count from stat_chongzhi c " +
				" where c.time between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss')" +
				" and to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")" +subSql;
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_pay_userCount(String startDateStr, String endDateStr, String fenQu,String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		
		String baseSql=getBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select count(distinct(c.username)) count from stat_chongzhi c " +
				" where c.time between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss')" +
				" and to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")" +subSql;
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_userAVGOnLineTime(String startDateStr, String endDateStr, String fenQu,String qudao) {
		String subsql="";
		if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
	    if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
		
        String baseSql="select t.username,count(t.id) times,sum(t.onlinetime) time" +
        		" from stat_playgame t"+
        		" where t.enterdate between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
        		" and  to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +subsql+
        		" group by t.username " +
        		" having count(t.id) > 2 and  sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 39 ";
		
        String sql="select avg(time) count from ("+baseSql+")";
		String resultStr=getDbStringValue(sql);
		
		java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
		if(isEmpty(resultStr)){
			resultStr="0";
		}else{
			float ss=Float.parseFloat(resultStr)/(60*60*1000);
			resultStr=df.format(ss);
		}
		return resultStr;
	}
	@Override
	public String active_userCount(String startDateStr, String endDateStr, String fenQu,String qudao) {
		String baseSql=getBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql="select count(*) count from ("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){
			result="0";
		}
		return result;
	}
	
	@Override
	public String active_userLoginTimes(String startDateStr, String endDateStr, String fenQu,String qudao) {

		String subsql="";
		if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
		
        String baseSql="select t.username,count(t.id) times,sum(t.onlinetime) time" +
        		" from stat_playgame t" +
                " where t.enterdate between to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss')" +
        	    " and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')"+subsql+
        		" group by t.username " +
        		" having count(t.id) > 2 and sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 39 ";
		
        String sql="select avg(times) count from ("+baseSql+")";
    	java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
		String resultStr=getDbStringValue(sql);
		if(isEmpty(resultStr)){
			resultStr="0";
		}else{
			resultStr=df.format(Float.parseFloat(resultStr));
		}
		return resultStr;
	
	}
	
	/////////////周留存用户统计相关  start ///////////////////////////////////////////


	@Override
	public String active_pay_AVGTimes_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		
		String baseSql=getWeekLiuCunUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select count(distinct(c.username)) count, count(c.username) times from stat_chongzhi c " +
				" where 1=1 " +subSql+
				" and c.time between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")";
		String result=getDbFloatValue(sql);
		return result;
	}
	@Override
	public String active_pay_Money_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {

		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		String baseSql=getWeekLiuCunUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select sum(c.money)/100 count from stat_chongzhi c " +
				" where 1=1 " +subSql+
				" and c.time between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_pay_userCount_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		
		String baseSql=getWeekLiuCunUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select count(distinct(c.username||c.fenqu)) count from stat_chongzhi c " +
				" where 1=1 " +subSql+
				" and c.time between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}

	
	@Override
	public String active_userAVGOnLineTime_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {

		String subsql="";
		if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
	    if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
		
	    String beforebaseSql="select t.username||t.fenqu " +
		" from stat_playgame t where 1 = 1 " +subsql+
		" and t.enterdate between to_date('"+startDateStr+"', 'YYYY-MM-DD')-6 and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')-6" +
		" group by t.username||t.fenqu " +
		" having count(t.enterdate || t.username||t.fenqu) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) >39 ";
	    
        String baseSql="select t.username||t.fenqu,count(t.enterdate || t.username||t.fenqu) times,sum(t.onlinetime) time" +
        		" from stat_playgame t where 1 = 1 " +subsql+
	            " and t.username||t.fenqu in ("+beforebaseSql+")"+
        		" and  t.enterdate between to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.enterdate and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
        		" group by t.username||t.fenqu " +
        		" having count(t.enterdate || t.username||t.fenqu) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 39 ";
		
        String sql="select avg(time) count from ("+baseSql+")";
		String resultStr=getDbStringValue(sql);
		
		java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
		if(isEmpty(resultStr)){
			resultStr="0";
		}else{
			float ss=Float.parseFloat(resultStr)/(60*60*1000);
			resultStr=df.format(ss);
		}
		return resultStr;
	}
	@Override
	public String active_userCount_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String baseSql=getWeekLiuCunUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql="select count(*) count from ("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){
			result="0";
		}
		return result;
	}
	@Override
	public String active_userLoginTimes_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {

		String subsql="";
		if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
		
		String beforebaseSql="select t.username||t.fenqu " +
		" from stat_playgame t where 1 = 1 " +subsql+
		" and trunc(t.enterdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD')-6 and to_date('"+endDateStr+"', 'YYYY-MM-DD')-6" +
		" group by t.username||t.fenqu " +
		" having count(t.enterdate || t.username||t.fenqu) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 39 ";

        String baseSql="select t.username||t.fenqu,count(t.enterdate || t.username||t.fenqu) times,sum(t.onlinetime) time" +
        		" from stat_playgame t where 1 = 1 " +subsql+
                " and  t.username||t.fenqu in ("+beforebaseSql+")"+
        		" and  t.enterdate between to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.enterdate and to_date('"+endDateStr+" 23;59:59', 'YYYY-MM-DD hh24:mi:ss')" +
        		" group by t.username||t.fenqu " +
        		" having count(t.enterdate || t.username||t.fenqu) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 39 ";
		
        String sql="select avg(times) count from ("+baseSql+")";
    	java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
		String resultStr=getDbStringValue(sql);
		if(isEmpty(resultStr)){
			resultStr="0";
		}else{
			resultStr=df.format(Float.parseFloat(resultStr));
		}
		return resultStr;
	
	}
	///////////////////////周留存用户统计相关  end/////////////////////////////////
	
	///////////////周回流活跃用户信息统计 start/////////////////////////////////
	///////////////周回流活跃用户定义：W0活跃，W1不活跃，W2活跃，则视为W2的回流活跃用户
	@Override
	public String active_left_youxibi_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String active_left_yuanbao_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String active_pay_AVGTimes_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		
		String baseSql=getWeekHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select count(distinct(c.username)) count, count(c.username) times from stat_chongzhi c " +
				" where 1=1 " +subSql+
				" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")";
		String result=getDbFloatValue(sql);
		return result;
	}
	@Override
	public String active_pay_Money_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {

		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		String baseSql=getWeekHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select sum(c.money)/100 count from stat_chongzhi c " +
				" where 1=1 " +subSql+
				" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  c.time  < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_pay_userCount_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		
		String baseSql=getWeekHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select count(distinct(c.username||c.fenqu)) count from stat_chongzhi c " +
				" where 1=1 " +subSql+
				" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_spend_money_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		String baseSql=getWeekHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select  sum(c.danjia*c.daojunum) count from stat_daoju c " +
				" where 1=1 " +subSql+
				" and c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.createdate< to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			    " and c.gettype='GET' and c.position like '商店购买%'"+
				" and c.username in("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_spend_userCount_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		String baseSql=getWeekHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select count(distinct(c.username)) count from stat_daoju c " +
				" where 1=1 " +subSql+
				" and c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  c.createdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			    " and c.gettype='GET' and c.position like '商店购买%'"+
				" and c.username in("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_userAVGOnLineTime_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {

		String subsql="";
		if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
	    if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
		
	    String beforebaseSql="select t.username||t.fenqu " +
		" from stat_playgame t where 1 = 1 " +subsql+
		" and trunc(t.enterdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD')-6 and to_date('"+endDateStr+"', 'YYYY-MM-DD')-6" +
		" group by t.username||t.fenqu " +
		" having count(t.enterdate || t.username||t.fenqu) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 5 ";
        
        String beforebeforebaseSql="select t.username||t.fenqu " +
		" from stat_playgame t where 1 = 1 " +subsql+
		" and trunc(t.enterdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD')-13 and to_date('"+endDateStr+"', 'YYYY-MM-DD')-13" +
		" group by t.username||t.fenqu " +
		" having count(t.enterdate || t.username||t.fenqu) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 5 ";
        
        String baseSql="select t.username||t.fenqu,count(t.enterdate || t.username||t.fenqu) times,sum(t.onlinetime) time" +
        		" from stat_playgame t where 1 = 1 " +subsql+
	            " and t.username||t.fenqu not in ("+beforebaseSql+") and t.username||t.fenqu in ("+beforebeforebaseSql+")"+
        		" and t.enterdate >= to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
        		" group by t.username||t.fenqu " +
        		" having count(t.enterdate || t.username||t.fenqu) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 5 ";
		
        String sql="select avg(time) count from ("+baseSql+")";
		String resultStr=getDbStringValue(sql);
		
		java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
		if(isEmpty(resultStr)){
			resultStr="0";
		}else{
			float ss=Float.parseFloat(resultStr)/(60*60*1000);
			resultStr=df.format(ss);
		}
		return resultStr;
	}
	@Override
	public String active_userCount_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String baseSql=getWeekHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql="select count(*) count from ("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){
			result="0";
		}
		return result;
	}
	@Override
	public String active_userLoginTimes_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {

		String subsql="";
		if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
		
		String beforebaseSql="select t.username||t.fenqu " +
		" from stat_playgame t where 1 = 1 " +subsql+
		" and trunc(t.enterdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD')-6 and to_date('"+endDateStr+"', 'YYYY-MM-DD')-6" +
		" group by t.username||t.fenqu " +
		" having count(t.enterdate || t.username||t.fenqu) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 5 ";
          
          
          String beforebeforebaseSql="select t.username||t.fenqu " +
  		" from stat_playgame t where 1 = 1 " +subsql+
  		" and trunc(t.enterdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD')-13 and to_date('"+endDateStr+"', 'YYYY-MM-DD')-13" +
  		" group by t.username||t.fenqu " +
  		" having count(t.enterdate || t.username||t.fenqu) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 5 ";

        
        String baseSql="select t.username||t.fenqu,count(t.enterdate || t.username||t.fenqu) times,sum(t.onlinetime) time" +
        		" from stat_playgame t where 1 = 1 " +subsql+
                " and  t.username||t.fenqu not in ("+beforebaseSql+")"+
                " and  t.username||t.fenqu in ("+beforebeforebaseSql+")"+
        		" and  t.enterdate >= to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
        		" group by t.username||t.fenqu " +
        		" having count(t.enterdate || t.username||t.fenqu) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 5 ";
		
        String sql="select avg(times) count from ("+baseSql+")";
    	java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
		String resultStr=getDbStringValue(sql);
		if(isEmpty(resultStr)){
			resultStr="0";
		}else{
			resultStr=df.format(Float.parseFloat(resultStr));
		}
		return resultStr;
	
	}
     ///////////////周回流活跃用户信息统计  end/////////////////////////////////
	
	////////////////////本周激活用  start//////////////////////////////////////////
	@Override
	public String active_left_youxibi_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String active_left_yuanbao_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String active_pay_AVGTimes_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select count(distinct(c.username)) count, count(c.username) times from stat_chongzhi c " +
				" where 1=1 " +subSql+
				" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")";
		String result=getDbFloatValue(sql);
		return result;
	}
	@Override
	public String active_pay_Money_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select sum(c.money)/100 count from stat_chongzhi c " +
				" where 1=1 " +subSql+
				" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_pay_userCount_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select count(distinct(c.username||c.fenqu)) count from stat_chongzhi c " +
				" where 1=1 " +subSql+
				" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  c.time< to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and c.username in("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_spend_money_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select  sum(c.danjia*c.daojunum) count from stat_daoju c " +
				" where 1=1 " +subSql+
				" and c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD  hh24:mi:ss') and c.createdate < to_date('"+endDateStr+"23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			    " and c.gettype='GET' and c.position like '商店购买%'"+
				" and c.username in("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_spend_userCount_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
		String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql=" select count(distinct(c.username)) count from stat_daoju c " +
				" where 1=1 " +subSql+
				" and c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.createdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			    " and c.gettype='GET' and c.position like '商店购买%'"+
				" and c.username in("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){ result="0"; }
		return result;
	}
	@Override
	public String active_userAVGOnLineTime_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and p.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and p.qudao='"+qudao+"'";}
		    String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		    
		    String basesql="select  p.username||p.fenqu,sum(p.onlinetime) time from stat_playgame p ";
		      basesql+=" where 1=1 " +subSql+
				" and p.enterdate >= to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and p.enterdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +
				" and p.username in("+baseSql+") " +
				" group by (p.username||p.fenqu)";
		String sql="select avg(time)/3600000 count from ("+basesql+")";
		String resultStr=getDbStringValue(sql);
		java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
		if(isEmpty(resultStr)){
			resultStr="0";
		}else{
			resultStr=df.format(Float.parseFloat(resultStr));
		}
		return resultStr;
	}
	@Override
	public String active_userCount_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql="select count(*) count from ("+baseSql+")";
		String result=getDbStringValue(sql);
		if(isEmpty(result)){
			result="0";
		}
		return result;
	}
	@Override
	public String active_userLoginTimes_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		String subSql="";
		if(!isEmpty(fenQu)){subSql+=" and p.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subSql+=" and p.qudao='"+qudao+"'";}
		
		String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String sql="select count(distinct(p.username||p.fenqu)) count, count(distinct(p.username||p.fenqu||trunc(p.enterdate))) times " +
				" from stat_playgame p where 1=1" +subSql+
				" and p.enterdate >= to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  p.enterdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
				" and p.username in("+baseSql+")";
		return getDbFloatValue(sql);
	}
	
   ////////////////////本周激活用  end//////////////////////////////////////////
	
	
	
	/**
	 * 本周激活的用户名称。定义：以前注册，本周内创建角色的用户
	 */
	private String getWeekJiHuoUserBaseSql(String startDateStr, String endDateStr, String fenQu,String qudao) {
		String subsql="";
		if(!isEmpty(fenQu)){subsql+=" and u.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subsql+=" and u.qudao='"+qudao+"'";}
     
       String sql=" select u.name from stat_user  u where 1=1 and " +subsql+
       		" u.registtime < to_date('"+startDateStr+"', 'YYYY-MM-DD')" +
       		" group by u.name having min(u.creat_player_time) > to_date('"+startDateStr+"', 'YYYY-MM-DD') " +
       				"and  min(u.creat_player_time) < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')";
		return sql;
	  }
	

	/**
	 * 获得周回流活跃用户的名称
	 * 周活跃用户定义：等级大于5级，1周内独立登录3次或以上，或累计在线时长达到6小时或以上
	 * 周回流活跃用户定义：W0活跃，W1不活跃，W2活跃，则视为W2的回流活跃用户
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	private String getWeekHuiLiuUserBaseSql(String startDateStr, String endDateStr, String fenQu,String qudao) {
		String sql=getBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String subsql="";
		if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
		
		/////上周有效用户名称
		String beforeSql=" select t.username" +
				" from stat_playgame t where 1 = 1" +subsql+
				" and trunc(t.enterdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD')-6 and to_date('"+endDateStr+"', 'YYYY-MM-DD')-6 " +
				" group by t.username " +
				" having count(distinct(t.enterdate ||t.fenqu)) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 5";
		

		  		/////上上周有效用户名称
		String beforeBeforeSql=" select t.username" +
		  				" from stat_playgame t where 1 = 1" +subsql+
		  				" and trunc(t.enterdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD')-13 and to_date('"+endDateStr+"', 'YYYY-MM-DD')-13 " +
		  				" group by t.username " +
		  				" having count(distinct(t.enterdate ||t.fenqu)) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 5";
		  		
		String basesql="select temp.username from ("+sql+") temp where temp.username in ("+beforeBeforeSql+") and temp.username not in ("+beforeSql+")";       
		return basesql;
	}
	
	/**
	 * 获得活跃用户周留存用户的名称
	 * 周活跃用户定义：等级大于5级，1周内独立登录3次或以上，或累计在线时长达到6小时或以上
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	private String getWeekLiuCunUserBaseSql(String startDateStr, String endDateStr, String fenQu,String qudao) {
		String sql=getBaseSql(startDateStr,endDateStr,fenQu,qudao);
		String subsql="";
		if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
		
		/////有效用户名称
		String beforeSql="select t.username" +
				" from stat_playgame t where 1 = 1" +subsql+
				" and t.enterdate between to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss')-6 and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')-6 " +
				" group by t.username " +
				" having count(distinct(t.enterdate ||t.fenqu)) > 2 or sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 39";
		
		String basesql="select temp.username from ("+sql+") temp where temp.username in ("+beforeSql+")";       
		return basesql;
	}
	
	/**
	 * 获得周活跃用户的名称
	 * 周活跃用户定义：等级大于40级，1周内独立登录3次或以上，并累计在线时长达到6小时或以上
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	private String getBaseSql(String startDateStr, String endDateStr, String fenQu,String qudao) {
		String subsql="";
		if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
		if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
		
		/////有效用户名称
		String baseSql="select t.username" +
				" from stat_playgame t"+
		        " where t.enterdate >= to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and" +
				"  t.enterdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +subsql+
				" group by t.username " +
				" having count(t.id) > 2 and sum(t.onlinetime) > 6 * 60 * 60 * 1000-1 and max(t.maxlevel) > 39";
		return baseSql;
	}
	
	
	private String getDbFloatValue(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		Float count=0F;
		Float times=0F;
		Float value=0F;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			if(rs.next()){
				count=rs.getFloat("count");
				times=rs.getFloat("times");
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
		java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
		String result="";
		if(count!=null&&count!=0)
		{
			result=df.format(times/count);
		}
		return result;
	}
	
	private String getDbStringValue(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String value="";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			rs =  ps.getResultSet();
			if(rs.next()){
				value=rs.getString("count");
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
	 * 如果字符串为空，返回true，否则，返回false
	 * @param str
	 * @return
	 */
	public boolean isEmpty(String str){
		boolean result=true;
		if(str!=null&&!"".equals(str)){
			result=false;
		}
		return result;
	}
	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

























//////////////////////////// 月统计信息Start///////////////
@Override
public String active_pay_MAVGTimes(String startDateStr, String endDateStr, String fenQu,String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	
	String baseSql=getBaseSql_Month(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count, count(c.username) times from stat_chongzhi c " +
			" where 1=1 " +subSql+
			" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")";
	String result=getDbFloatValue(sql);
	return result;
}
@Override
public String active_pay_MMoney(String startDateStr, String endDateStr, String fenQu,String qudao) {

	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getBaseSql_Month(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select sum(c.money)/100 count from stat_chongzhi c " +
			" where 1=1 " +subSql+
			" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_pay_MuserCount(String startDateStr, String endDateStr, String fenQu,String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	
	String baseSql=getBaseSql_Month(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count from stat_chongzhi c " +
			" where 1=1 " +subSql+
			" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_MuserAVGOnLineTime(String startDateStr, String endDateStr, String fenQu,String qudao) {
	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu in ('"+fenQu+"')";}
    if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	
    String baseSql="select t.username||t.fenqu,count(t.enterdate || t.username||t.fenqu) times,sum(t.onlinetime) time" +
    		" from stat_playgame t where 1 = 1 " +subsql+
    		" and t.enterdate >= to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
    		" group by t.username||t.fenqu " +
    		" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
	
    String sql="select avg(time) count from ("+baseSql+")";
	String resultStr=getDbStringValue(sql);
	
	java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
	if(isEmpty(resultStr)){
		resultStr="0";
	}else{
		float ss=Float.parseFloat(resultStr)/(60*60*1000);
		resultStr=df.format(ss);
	}
	return resultStr;
}
@Override
public String active_MuserCount(String startDateStr, String endDateStr, String fenQu,String qudao) {
	String baseSql=getBaseSql_Month(startDateStr,endDateStr,fenQu,qudao);
	String sql="select count(*) count from ("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){
		result="0";
	}
	return result;
}

@Override
public String active_MuserLoginTimes(String startDateStr, String endDateStr, String fenQu,String qudao) {

	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	
    String baseSql="select t.username||t.fenqu,count(t.enterdate || t.username||t.fenqu) times,sum(t.onlinetime) time" +
    		" from stat_playgame t where 1 = 1 " +subsql+
    		" and t.enterdate >= to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
    		" group by t.username||t.fenqu " +
    		" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
	
    String sql="select avg(times) count from ("+baseSql+")";
	java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
	String resultStr=getDbStringValue(sql);
	if(isEmpty(resultStr)){
		resultStr="0";
	}else{
		resultStr=df.format(Float.parseFloat(resultStr));
	}
	return resultStr;

}

@Override
public String active_left_Myouxibi(String startDateStr, String endDateStr, String fenQu,String qudao) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String active_left_Myuanbao(String startDateStr, String endDateStr, String fenQu,String qudao) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String active_spend_Mmoney(String startDateStr, String endDateStr, String fenQu,String qudao) {

	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	
	String baseSql=getBaseSql_Month(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select  sum(c.danjia*c.daojunum) count from stat_daoju c " +
			" where 1=1 " +subSql+
			" and c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.createdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
		    " and c.gettype='GET' and c.position like '商店购买%'"+
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_spend_MuserCount(String startDateStr, String endDateStr, String fenQu,String qudao) {

	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	
	String baseSql=getBaseSql_Month(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count from stat_daoju c " +
			" where 1=1 " +subSql+
			" and c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.createdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
		    " and c.gettype='GET' and c.position like '商店购买%'"+
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}

/////////////周留存用户统计相关  start ///////////////////////////////////////////



@Override
public String active_left_youxibi_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String active_left_yuanbao_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String active_pay_AVGTimes_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	
	String baseSql=getMonthLiuCunUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count, count(c.username) times from stat_chongzhi c " +
			" where 1=1 " +subSql+
			" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  c.time  < to_date('"+endDateStr+" 23;59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")";
	String result=getDbFloatValue(sql);
	return result;
}
@Override
public String active_pay_Money_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {

	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getMonthLiuCunUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select sum(c.money)/100 count from stat_chongzhi c " +
			" where 1=1 " +subSql+
			" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_pay_userCount_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	
	String baseSql=getMonthLiuCunUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count from stat_chongzhi c " +
			" where 1=1 " +subSql+
			" and c.time > to_date('"+startDateStr+"00:00:00','YYYY-MM-DD hh24:mi:ss') and c.time  < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_spend_money_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getMonthLiuCunUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select  sum(c.danjia*c.daojunum) count from stat_daoju c " +
			" where 1=1 " +subSql+
			" and c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.createdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
		    " and c.gettype='GET' and c.position like '商店购买%'"+
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_spend_userCount_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getMonthLiuCunUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count from stat_daoju c " +
			" where 1=1 " +subSql+
			" and c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.createdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
		    " and c.gettype='GET' and c.position like '商店购买%'"+
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_userAVGOnLineTime_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {

	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu in ('"+fenQu+"')";}
    if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	
    String beforebaseSql="select t.username||t.fenqu " +
	" from stat_playgame t where 1 = 1 " +subsql+
	" and trunc(t.enterdate) between add_months(to_date('"+startDateStr+"', 'YYYY-MM-DD'),-1) and add_months(to_date('"+endDateStr+"', 'YYYY-MM-DD'),-1)" +
	" group by t.username||t.fenqu " +
	" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1  ";
    
    String baseSql="select t.username||t.fenqu,count(t.enterdate || t.username||t.fenqu) times,sum(t.onlinetime) time" +
    		" from stat_playgame t where 1 = 1 " +subsql+
            " and t.username||t.fenqu in ("+beforebaseSql+")"+
    		" and t.enterdate >= to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
    		" group by t.username||t.fenqu " +
    		" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1  ";
	
    String sql="select avg(time) count from ("+baseSql+")";
	String resultStr=getDbStringValue(sql);
	
	java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
	if(isEmpty(resultStr)){
		resultStr="0";
	}else{
		float ss=Float.parseFloat(resultStr)/(60*60*1000);
		resultStr=df.format(ss);
	}
	return resultStr;
}
@Override
public String active_userCount_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String baseSql=getMonthLiuCunUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql="select count(*) count from ("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){
		result="0";
	}
	return result;
}
@Override
public String active_userLoginTimes_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {

	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	
	String beforebaseSql="select t.username||t.fenqu " +
	" from stat_playgame t where 1 = 1 " +subsql+
	" and trunc(t.enterdate) between add_months(to_date('"+startDateStr+"', 'YYYY-MM-DD'),-1) and add_months(to_date('"+endDateStr+"', 'YYYY-MM-DD'),-1) " +
	" group by t.username||t.fenqu " +
	" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1  ";

    String baseSql="select t.username||t.fenqu,count(t.enterdate || t.username||t.fenqu) times,sum(t.onlinetime) time" +
    		" from stat_playgame t where 1 = 1 " +subsql+
            " and  t.username||t.fenqu in ("+beforebaseSql+")"+
    		" and  t.enterdate  >= to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
    		" group by t.username||t.fenqu " +
    		" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1  ";
	
    String sql="select avg(times) count from ("+baseSql+")";
	java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
	String resultStr=getDbStringValue(sql);
	if(isEmpty(resultStr)){
		resultStr="0";
	}else{
		resultStr=df.format(Float.parseFloat(resultStr));
	}
	return resultStr;

}
///////////////////////周留存用户统计相关  end/////////////////////////////////

///////////////周回流活跃用户信息统计 start/////////////////////////////////
///////////////周回流活跃用户定义：W0活跃，W1不活跃，W2活跃，则视为W2的回流活跃用户
@Override
public String active_left_youxibi_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String active_left_yuanbao_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String active_pay_AVGTimes_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	
	String baseSql=getMonthHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count, count(c.username) times from stat_chongzhi c " +
			" where c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")"+subSql;
	String result=getDbFloatValue(sql);
	return result;
}
@Override
public String active_pay_Money_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {

	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getMonthHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select sum(c.money)/100 count from stat_chongzhi c " +
			" where c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+") +subSql";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_pay_userCount_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	
	String baseSql=getMonthHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count from stat_chongzhi c " +
			" where 1=1 " +subSql+
			" and c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_spend_money_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getMonthHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select  sum(c.danjia*c.daojunum) count from stat_daoju c " +
			" where c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
			" and  c.createdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
		    " and c.gettype='GET' and c.position like '商店购买%'"+subSql+
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_spend_userCount_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getMonthHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count from stat_daoju c " +
			" where 1=1 " +subSql+
			" and  c.createdate > to_date('"+startDateStr+"','YYYY-MM-DD') and  c.createdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
		    " and c.gettype='GET' and c.position like '商店购买%'"+
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_userAVGOnLineTime_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {

	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
    if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	
    String beforebaseSql="select t.username||t.fenqu " +
	" from stat_playgame t where 1 = 1 " +subsql+
	" and trunc(t.enterdate) between add_months(to_date('"+startDateStr+"', 'YYYY-MM-DD'),-1) and add_months(to_date('"+endDateStr+"', 'YYYY-MM-DD'),-1)" +
	" group by t.username||t.fenqu " +
	" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1  ";
    
    String beforebeforebaseSql="select t.username||t.fenqu " +
	" from stat_playgame t where 1 = 1 " +subsql+
	" and trunc(t.enterdate) between add_months(to_date('"+startDateStr+"', 'YYYY-MM-DD'),-2) and add_months(to_date('"+endDateStr+"', 'YYYY-MM-DD'),-2)" +
	" group by t.username||t.fenqu " +
	" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
    
    String baseSql="select t.username||t.fenqu,count(t.enterdate || t.username||t.fenqu) times,sum(t.onlinetime) time" +
    		" from stat_playgame t where 1 = 1 " +subsql+
            " and t.username||t.fenqu not in ("+beforebaseSql+") and t.username||t.fenqu in ("+beforebeforebaseSql+")"+
    		" and  t.enterdate  >= to_date('"+startDateStr+"', 'YYYY-MM-DD') and  t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
    		" group by t.username||t.fenqu " +
    		" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
	
    String sql="select avg(time) count from ("+baseSql+")";
	String resultStr=getDbStringValue(sql);
	
	java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
	if(isEmpty(resultStr)){
		resultStr="0";
	}else{
		float ss=Float.parseFloat(resultStr)/(60*60*1000);
		resultStr=df.format(ss);
	}
	return resultStr;
}
@Override
public String active_userCount_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String baseSql=getMonthHuiLiuUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql="select count(*) count from ("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){
		result="0";
	}
	return result;
}
@Override
public String active_userLoginTimes_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {

	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	
	String beforebaseSql="select t.username||t.fenqu " +
	" from stat_playgame t where 1 = 1 " +subsql+
	" and trunc(t.enterdate) between add_months(to_date('"+startDateStr+"', 'YYYY-MM-DD'),-1) and add_months(to_date('"+endDateStr+"', 'YYYY-MM-DD'),-1)" +
	" group by t.username||t.fenqu " +
	" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
      
      
      String beforebeforebaseSql="select t.username||t.fenqu " +
		" from stat_playgame t where 1 = 1 " +subsql+
		" and trunc(t.enterdate) between add_months(to_date('"+startDateStr+"', 'YYYY-MM-DD'),-2) and add_months(to_date('"+endDateStr+"', 'YYYY-MM-DD'),-2) " +
		" group by t.username||t.fenqu " +
		" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";

    
    String baseSql="select t.username||t.fenqu,count(t.enterdate || t.username||t.fenqu) times,sum(t.onlinetime) time" +
    		" from stat_playgame t where 1 = 1 " +subsql+
            " and  t.username||t.fenqu not in ("+beforebaseSql+")"+
            " and  t.username||t.fenqu in ("+beforebeforebaseSql+")"+
    		" and  t.enterdate >= to_date('"+startDateStr+"', 'YYYY-MM-DD') and t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')" +
    		" group by t.username||t.fenqu " +
    		" having count(t.enterdate || t.username||t.fenqu) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
	
    String sql="select avg(times) count from ("+baseSql+")";
	java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
	String resultStr=getDbStringValue(sql);
	if(isEmpty(resultStr)){
		resultStr="0";
	}else{
		resultStr=df.format(Float.parseFloat(resultStr));
	}
	return resultStr;

}
 ///////////////周回流活跃用户信息统计  end/////////////////////////////////

////////////////////本周激活用  start//////////////////////////////////////////
@Override
public String active_left_youxibi_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String active_left_yuanbao_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String active_pay_AVGTimes_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count, count(c.username) times from stat_chongzhi c " +
			" where c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.time< to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")"+subSql;
	String result=getDbFloatValue(sql);
	return result;
}
@Override
public String active_pay_Money_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select sum(c.money)/100 count from stat_chongzhi c " +
			" where c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")"+ subSql;
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_pay_userCount_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count from stat_chongzhi c where " +
			" c.time > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.time < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
			" and c.username in("+baseSql+")" + subSql;
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_spend_money_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select  sum(c.danjia*c.daojunum) count from stat_daoju c where " +
			"  c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.createdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
		    " and c.gettype='GET' and c.position like '商店购买%'"+subSql+
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_spend_userCount_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and c.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and c.qudao='"+qudao+"'";}
	String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql=" select count(distinct(c.username)) count from stat_daoju c where " +
			" c.createdate > to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and c.createdate  < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +
		    " and c.gettype='GET' and c.position like '商店购买%'"+subSql+
			" and c.username in("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){ result="0"; }
	return result;
}
@Override
public String active_userAVGOnLineTime_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
	
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and p.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and p.qudao='"+qudao+"'";}
	    String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	    
	    String basesql="select  p.username||p.fenqu,sum(p.onlinetime) time from stat_playgame p "+
	        " where p.enterdate >= to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and  p.enterdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss')" +
			" and p.username in("+baseSql+") " +subSql+
			" group by (p.username||p.fenqu)";
	String sql="select avg(time)/3600000 count from ("+basesql+")";
	String resultStr=getDbStringValue(sql);
	java.text.DecimalFormat   df   =   new   java.text.DecimalFormat( "#0.00 "); 
	if(isEmpty(resultStr)){
		resultStr="0";
	}else{
		resultStr=df.format(Float.parseFloat(resultStr));
	}
	return resultStr;
}
@Override
public String active_userCount_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql="select count(*) count from ("+baseSql+")";
	String result=getDbStringValue(sql);
	if(isEmpty(result)){
		result="0";
	}
	return result;
}
@Override
public String active_userLoginTimes_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
	String subSql="";
	if(!isEmpty(fenQu)){subSql+=" and p.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subSql+=" and p.qudao='"+qudao+"'";}
	
	String baseSql=getWeekJiHuoUserBaseSql(startDateStr,endDateStr,fenQu,qudao);
	String sql="select count(distinct(p.username||p.fenqu)) count, count(distinct(p.username||p.fenqu||trunc(p.enterdate))) times " +
			" from stat_playgame p where " +
			" p.enterdate >= to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and p.enterdate < to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') "
			+subSql+
			" and p.username in("+baseSql+")";
	return getDbFloatValue(sql);
}

////////////////////本周激活用  end//////////////////////////////////////////



///**
// * 本月激活的用户名称。定义：以前注册，本周内创建角色的用户
// */
//private String getMonthJiHuoUserBaseSql(String startDateStr, String endDateStr, String fenQu,String qudao) {
//	String subsql="";
//	if(!isEmpty(fenQu)){subsql=" and u.fenqu='"+fenQu+"'";}
//	if(!isEmpty(qudao)){subsql+=" and u.qudao='"+qudao+"'";}
//   String sql= "select distinct(u.name)  from stat_user u " +
//   		 "where 1=1 " +subsql+
//   		 " and trunc(u.creat_player_time)  between to_date('"+startDateStr+"','YYYY-MM-DD') " +
//   		" and to_date('"+endDateStr+"','YYYY-MM-DD')  and trunc(u.registtime)< to_date('"+startDateStr+"','YYYY-MM-DD')";
//	return sql;
//  }


/**
 * 获得月回流活跃用户的名称
 * 月活跃用户定义：1月内独立登录12次或以上，或累计在线时长达到24小时或以上
 * 月回流活跃用户定义：W0活跃，W1不活跃，W2活跃，则视为W2的回流活跃用户
 * @param startDateStr
 * @param endDateStr
 * @param fenQu
 * @return
 */
private String getMonthHuiLiuUserBaseSql(String startDateStr, String endDateStr, String fenQu,String qudao) {
	String sql=getBaseSql_Month(startDateStr,endDateStr,fenQu,qudao);
	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu='"+fenQu+"'";}
	if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	
	/////上月有效用户名称
	String beforeSql=" select t.username from stat_playgame t where " +
			" trunc(t.enterdate) between add_months(to_date('"+startDateStr+"', 'YYYY-MM-DD'),-1) " +
			" and add_months(to_date('"+endDateStr+"', 'YYYY-MM-DD'),-1) " +subsql+
			" group by t.username " +
			" having count(distinct(t.enterdate ||t.fenqu)) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
	

	  		/////上上月有效用户名称
	String beforeBeforeSql=" select t.username from stat_playgame t where " +
	  				" trunc(t.enterdate) between add_months(to_date('"+startDateStr+"', 'YYYY-MM-DD'),-2) " +
	  				" and add_months(to_date('"+endDateStr+"', 'YYYY-MM-DD'),-2) "+subsql+
	  				" group by t.username " +
	  				" having count(distinct(t.enterdate ||t.fenqu)) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
	  		
	String basesql="select temp.username from ("+sql+") temp where temp.username in ("+beforeBeforeSql+") and temp.username not in ("+beforeSql+")";       
	return basesql;
}

/**
 * 获得月留存用户的名称
 * 月活跃用户定义：1月内独立登录12次或以上，或累计在线时长达到24小时或以上
 * @param startDateStr
 * @param endDateStr
 * @param fenQu
 * @return
 */
private String getMonthLiuCunUserBaseSql(String startDateStr, String endDateStr, String fenQu,String qudao) {
	String sql=getBaseSql_Month(startDateStr,endDateStr,fenQu,qudao);
	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	
	/////有效用户名称
	String beforeSql="select t.username from stat_playgame t where" +
			" trunc(t.enterdate) between add_months(to_date('"+startDateStr+"', 'YYYY-MM-DD'),-1) " +
			" and add_months(to_date('"+endDateStr+"', 'YYYY-MM-DD'),-1) " +subsql+
			" group by t.username " +
			" having count(distinct(t.enterdate ||t.fenqu)) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
	
	String basesql="select temp.username from ("+sql+") temp where temp.username in ("+beforeSql+")";       
	return basesql;
}

/**
 * 获得月活跃用户的名称
 * 月活跃用户定义：1月内独立登录12次或以上，或累计在线时长达到24小时或以上
 * @param startDateStr
 * @param endDateStr
 * @param fenQu
 * @return
 */
private String getBaseSql_Month(String startDateStr, String endDateStr, String fenQu,String qudao) {
	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	
	/////有效用户名称
	String baseSql=" select t.username" +
			" from stat_playgame t where "+
			" t.enterdate  >= to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and" +
			" t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +subsql+
			" group by t.username " +
			" having count(distinct(t.enterdate ||t.fenqu)) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
	return baseSql;
}

/**
 * 获得指定时间内登录过的用户的数量
 * @param startDateStr
 * @param endDateStr
 * @param fenQu
 * @return
 */
public String getMonthLoginUsercout(String startDateStr, String endDateStr, String fenQu,String qudao,String jixing) {
	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu in ('"+fenQu+"')";}
	if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	if(!isEmpty(jixing)){subsql+=" and t.jixing='"+jixing+"'";}
	
	/////有效用户名称
	String baseSql="select count(*) count from ( select t.username||t.fenqu" +
			" from STAT_PLAYGAME_TEMP2 t where "+
			" t.enterdate  >= to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and" +
			" t.enterdate < to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +subsql+
			" group by t.username||t.fenqu )";
			//" having count(distinct(t.enterdate ||t.fenqu)) > 11 or sum(t.onlinetime) > 24 * 60 * 60 * 1000-1 ";
	return getDbStringValue(baseSql);
}
@Override
public  String getLiuCun(String startDate, String endDate, String startDateStat, String endDateStat, String fenQu, String qudao, String jixing) {
	
	String subsql="";
	if(!isEmpty(fenQu)){subsql+=" and t.fenqu ='"+fenQu+"'";}
	if(!isEmpty(qudao)){subsql+=" and t.qudao='"+qudao+"'";}
	if(!isEmpty(jixing)){subsql+=" and t.jixing='"+jixing+"'";}
	
	String sql="select count(distinct(t.username||t.fenqu)) count from stat_playgame_temp2 t  where t.enterdate >to_date('"+startDateStat+"','YYYY-MM-DD') " +
			" and t.enterdate< to_date('"+endDateStat+"','YYYY-MM-DD') "+subsql+" and t.username " +
			" in " +
			" ( select t.username FROM stat_playgame_temp2 t where t.enterdate >to_date('"+startDate+"','YYYY-MM-DD') " +
			" and t.enterdate< to_date('"+endDate+"','YYYY-MM-DD') )"+subsql;
	
	return getDbStringValue(sql);
}


}
