package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.StatUserGuideDao;
import com.sqage.stat.commonstat.entity.StatUserGuide;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class StatUserGuideDaoImpl implements StatUserGuideDao {

	static Logger logger = Logger.getLogger(StatUserGuideDaoImpl.class);
	DataSourceManager dataSourceManager;
	
	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

	@Override
	public StatUserGuide add(StatUserGuide statUserGuide) {
		long now =System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_USER_GUIDE(" +
					"USERNAME," +
					"PLAYERNAME," +
					"QUDAO," +
					"REGISTTIME," +
					"FENQU," +
					"CREATEDATE," +
					
					"S1," +
					"S2," +
					"S3," +
					"S4," +
					"S5," +
					"S6," +
					"S7," +
					"S8," +
					"S9," +
					"S10," +
					"S11," +
					"S12," +
					"S13," +
					"S14," +
					"S15," +
					"S16," +
					"S17," +
					"S18," +
					"S19," +
					"S20," +
					"S21," +
					"S22," +
					"S23," +
					"S24," +
					"S25," +
					"LEEPSTEP," +
					"JIXING" +
					") values (?,?,?,?,?,?,  ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ? ,?)");
			
			ps.setString(1, statUserGuide.getUserName());
			ps.setString(2, statUserGuide.getPlayerName());
			ps.setString(3, statUserGuide.getQuDao());
			ps.setTimestamp(4, new Timestamp(statUserGuide.getRegistTime().getTime()));
			ps.setString(5, statUserGuide.getFenQu());
			ps.setTimestamp(6, new Timestamp(statUserGuide.getCreateDate().getTime()));

			ps.setString(7,  statUserGuide.getS1());
			ps.setString(8,  statUserGuide.getS2());
			ps.setString(9,  statUserGuide.getS3());
			ps.setString(10, statUserGuide.getS4());
			ps.setString(11, statUserGuide.getS5());
			
			ps.setString(12, statUserGuide.getS6());
			ps.setString(13, statUserGuide.getS7());
			ps.setString(14, statUserGuide.getS8());
			ps.setString(15, statUserGuide.getS9());
			ps.setString(16, statUserGuide.getS10());
			
			ps.setString(17, statUserGuide.getS11());
			ps.setString(18, statUserGuide.getS12());
			ps.setString(19, statUserGuide.getS13());
			ps.setString(20, statUserGuide.getS14());
			ps.setString(21, statUserGuide.getS15());
			
			ps.setString(22, statUserGuide.getS16());
			ps.setString(23, statUserGuide.getS17());
			ps.setString(24, statUserGuide.getS18());
			ps.setString(25, statUserGuide.getS19());
			ps.setString(26, statUserGuide.getS20());
			
			ps.setString(27, statUserGuide.getS21());
			ps.setString(28, statUserGuide.getS22());
			ps.setString(29, statUserGuide.getS23());
			ps.setString(30, statUserGuide.getS24());
			ps.setString(31, statUserGuide.getS25());
			
			ps.setString(32, statUserGuide.getLeepStep());
			
			ps.setString(33, statUserGuide.getJixing());
			
			i = ps.executeUpdate();
			
			if(logger.isDebugEnabled()){
				logger.debug("[新手引导] [OK] "+statUserGuide.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			logger.error("[新手引导] [FAIL] "+statUserGuide.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			e.printStackTrace();
        } finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? statUserGuide : null;
	}

	@Override
	public ArrayList<StatUserGuide> getBySql(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<StatUserGuide> statUserGuideList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				
				StatUserGuide statUserGuide = new StatUserGuide();
				
				statUserGuide.setCreateDate(rs.getTimestamp("CREATEDATE"));
				statUserGuide.setFenQu(rs.getString("FENQU"));
				statUserGuide.setLeepStep(rs.getString("LEEPSTEP"));
				statUserGuide.setPlayerName(rs.getString("PLAYERNAME"));
				statUserGuide.setQuDao(rs.getString("QUDAO"));
				statUserGuide.setRegistTime(rs.getTimestamp("REGISTTIME"));
				statUserGuide.setUserName(rs.getString("USERNAME"));
				
				statUserGuide.setS1(rs.getString("S1"));
				statUserGuide.setS2(rs.getString("S2"));
				statUserGuide.setS3(rs.getString("S3"));
				statUserGuide.setS4(rs.getString("S4"));
				statUserGuide.setS5(rs.getString("S5"));
				
				statUserGuide.setS6(rs.getString("S6"));
				statUserGuide.setS7(rs.getString("S7"));
				statUserGuide.setS8(rs.getString("S8"));
				statUserGuide.setS9(rs.getString("S9"));
				statUserGuide.setS10(rs.getString("S10"));
				
				statUserGuide.setS11(rs.getString("S11"));
				statUserGuide.setS12(rs.getString("S12"));
				statUserGuide.setS13(rs.getString("S13"));
				statUserGuide.setS14(rs.getString("S14"));
				statUserGuide.setS15(rs.getString("S15"));
				
				statUserGuide.setS16(rs.getString("S16"));
				statUserGuide.setS17(rs.getString("S17"));
				statUserGuide.setS18(rs.getString("S18"));
				statUserGuide.setS19(rs.getString("S19"));
				statUserGuide.setS20(rs.getString("S20"));
				
				statUserGuide.setS21(rs.getString("S21"));
				statUserGuide.setS22(rs.getString("S22"));
				statUserGuide.setS23(rs.getString("S23"));
				statUserGuide.setS24(rs.getString("S24"));
				statUserGuide.setS25(rs.getString("S25"));
				statUserGuide.setJixing(rs.getString("JIXING"));
				
				statUserGuideList.add(statUserGuide);
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
		return statUserGuideList;
	
	}

	@Override
	public List<StatUserGuide> getByUserNameAndFenQu(String userName, String fenQu) {
	    String sql="select * from stat_user_guide t where t.username='"+userName+"' and t.fenqu='"+fenQu+"'";
	    return getBySql(sql);
	}

	@Override
	public boolean update(String userName,String fenQu,String step, String action) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			
			String sql="update STAT_USER_GUIDE set "+step+"=?";
			if("2".equals(action)){sql+=", leepstep=?";}
			sql+=" where username=? and fenqu=?";
			
			ps = con.prepareStatement(sql);
			ps.setString(1, action);
			if("2".equals(action)){	
				ps.setString(2, step);
			    ps.setString(3, userName);
			    ps.setString(4, fenQu);
			    }else{
			    	 ps.setString(2, userName);
					 ps.setString(3, fenQu);
			    }
			
			i =ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[新手引导更新] [OK] ["+userName+"] ["+fenQu+"] ["+step+":"+action+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		   } catch (SQLException e) {
			   logger.error("[新手引导更新] [FAIL] ["+userName+"] ["+fenQu+"] ["+step+":"+action+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
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
	public boolean delete(String userName, String fenQu) {
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("delete from stat_user_guide t where t.username=? and t.fenqu=?");
			ps.setString(1, userName);
			ps.setString(2, fenQu);
			
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
	public Map<String,String> search(String regStartDateStr, String regEndDateStr, String statStartdateStr, String statEnddateStr, String quDao, String fenQu) {
		Connection con = null;
		PreparedStatement ps = null;
		Map<String, String> map=new HashMap();
		
			String subSql="";
			if(regStartDateStr!=null&&regEndDateStr!=null){subSql+=" and trunc(t.registtime) between to_date('"+regStartDateStr+"','YYYY-MM-DD') and to_date('"+regEndDateStr+"','YYYY-MM-DD')";}
		    if(statStartdateStr!=null&&statEnddateStr!=null){subSql+="  and trunc(t.createdate) between  to_date('"+statStartdateStr+"','YYYY-MM-DD') and to_date('"+statEnddateStr+"','YYYY-MM-DD')";}
		    if(quDao!=null){subSql+=" and t.qudao='"+quDao+"'";}
		    if(fenQu!=null){subSql+=" and t.fenqu='"+fenQu+"' ";}
		
		String sql="select " +
				" count(case  s1 when '1' then 0 end) ts1, count(case s2  when '1' then 0 end) ts2, count(case s3  when '1' then 0 end) ts3," +
				" count(case s4  when '1' then 0 end) ts4, count(case s5  when '1' then 0 end) ts5, count(case s6  when '1' then 0 end) ts6," +
				" count(case s7  when '1' then 0 end) ts7, count(case s8  when '1' then 0 end) ts8, count(case s9  when '1' then 0 end) ts9," +
				" count(case s10 when '1' then 0 end) ts10,count(case s11 when '1' then 0 end) ts11,count(case s12 when '1' then 0 end) ts12," +
				" count(case s13 when '1' then 0 end) ts13,count(case s14 when '1' then 0 end) ts14,count(case s15 when '1' then 0 end) ts15," +
				" count(case s16 when '1' then 0 end) ts16,count(case s17 when '1' then 0 end) ts17,count(case s18 when '1' then 0 end) ts18," +
				" count(case s19 when '1' then 0 end) ts19,count(case s20 when '1' then 0 end) ts20,count(case s21 when '1' then 0 end) ts21," +
				" count(case s22 when '1' then 0 end) ts22,count(case s23 when '1' then 0 end) ts23,count(case s24 when '1' then 0 end) ts24," +
				" count(s25) ts25," +
				
				" count(case  s1 when '2' then 0 end) ls1, count(case s2  when '2' then 0 end) ls2, count(case s3  when '2' then 0 end) ls3," +
				" count( case s4 when '2' then 0 end) ls4, count(case s5  when '2' then 0 end) ls5, count(case s6  when '2' then 0 end) ls6," +
				" count( case s7 when '2' then 0 end) ls7, count(case s8  when '2' then 0 end) ls8, count(case s9  when '2' then 0 end) ls9," +
				" count(case s10 when '2' then 0 end) ls10,count(case s11 when '2' then 0 end) ls11,count(case s12 when '2' then 0 end) ls12," +
				" count(case s13 when '2' then 0 end) ls13,count(case s14 when '2' then 0 end) ls14,count(case s15 when '2' then 0 end) ls15," +
				" count(case s16 when '2' then 0 end) ls16,count(case s17 when '2' then 0 end) ls17,count(case s18 when '2' then 0 end) ls18," +
				" count(case s19 when '2' then 0 end) ls19,count(case s20 when '2' then 0 end) ls20,count(case s21 when '2' then 0 end) ls21," +
				" count(case s22 when '2' then 0 end) ls22,count(case s23 when '2' then 0 end) ls23,count(case s24 when '2' then 0 end) ls24," +
				" count(s25) ls25  from stat_user_guide t where 1=1 "+subSql;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			if(rs.next()) {
				map.put("TS1", rs.getString("TS1"));
				map.put("TS2", rs.getString("TS2"));
				map.put("TS3", rs.getString("TS3"));
				map.put("TS4", rs.getString("TS4"));
				map.put("TS5", rs.getString("TS5"));
				map.put("TS6", rs.getString("TS6"));
				map.put("TS7", rs.getString("TS7"));
				map.put("TS8", rs.getString("TS8"));
				map.put("TS9", rs.getString("TS9"));
				map.put("TS10", rs.getString("TS10"));
				map.put("TS11", rs.getString("TS11"));
				map.put("TS12", rs.getString("TS12"));
				map.put("TS13", rs.getString("TS13"));
				map.put("TS14", rs.getString("TS14"));
				map.put("TS15", rs.getString("TS15"));
				map.put("TS16", rs.getString("TS16"));
				map.put("TS17", rs.getString("TS17"));
				map.put("TS18", rs.getString("TS18"));
				map.put("TS19", rs.getString("TS19"));
				map.put("TS20", rs.getString("TS20"));
				map.put("TS21", rs.getString("TS21"));
				map.put("TS22", rs.getString("TS22"));
				map.put("TS23", rs.getString("TS23"));
				map.put("TS24", rs.getString("TS24"));
				map.put("TS25", rs.getString("TS25"));
				
				
				map.put("LS1", rs.getString("LS1"));
				map.put("LS2", rs.getString("LS2"));
				map.put("LS3", rs.getString("LS3"));
				map.put("LS4", rs.getString("LS4"));
				map.put("LS5", rs.getString("LS5"));
				map.put("LS6", rs.getString("LS6"));
				map.put("LS7", rs.getString("LS7"));
				map.put("LS8", rs.getString("LS8"));
				map.put("LS9", rs.getString("LS9"));
				map.put("LS10", rs.getString("LS10"));
				map.put("LS11", rs.getString("LS11"));
				map.put("LS12", rs.getString("LS12"));
				map.put("LS13", rs.getString("LS13"));
				map.put("LS14", rs.getString("LS14"));
				map.put("LS15", rs.getString("LS15"));
				map.put("LS16", rs.getString("LS16"));
				map.put("LS17", rs.getString("LS17"));
				map.put("LS18", rs.getString("LS18"));
				map.put("LS19", rs.getString("LS19"));
				map.put("LS20", rs.getString("LS20"));
				map.put("LS21", rs.getString("LS21"));
				map.put("LS22", rs.getString("LS22"));
				map.put("LS23", rs.getString("LS23"));
				map.put("LS24", rs.getString("LS24"));
				map.put("LS25", rs.getString("LS25"));
				
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
		return map;
	
	}
	
}
