package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sqage.stat.commonstat.dao.UserDao;
import com.sqage.stat.commonstat.entity.User;
import com.xuanzhi.tools.dbpool.DataSourceManager;
import com.xuanzhi.tools.text.DateUtil;

import org.apache.log4j.*;

public class UserDaoImpl implements UserDao {
	
	static Logger logger = Logger.getLogger(UserDaoImpl.class);
	
	DataSourceManager dataSourceManager;
	
	
	
	@Override
	public List<User> getByUserName(String userName) {
		Connection con = null;
		PreparedStatement ps = null;
		List<User> userList=null;
		long now =System.currentTimeMillis();
		try {
			con = dataSourceManager.getInstance().getConnection();
			String sql=" select * from stat_user u where u.name=? ";
			ps = con.prepareStatement(sql);
			ps.setString(1, userName);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			userList=new ArrayList();
			while(rs.next()) {
				User user = new User();
				
				user.setDiDian(rs.getString("DIDIAN"));
				user.setHaoMa(rs.getString("HAOMA"));
				user.setId(rs.getLong("ID"));
				user.setImei(rs.getString("IMEI"));
				user.setJiXing(rs.getString("JIXING"));
				user.setName(rs.getString("NAME"));
				
				user.setQuDao(rs.getString("QUDAO"));
				user.setRegistTime(rs.getTimestamp("REGISTTIME"));
				user.setUuid(rs.getString("UUID"));
				user.setGame(rs.getString("GAME"));
				user.setGuojia(rs.getString("GUOJIA"));
				
				user.setPlayerName(rs.getString("PLAYERNAME"));
				user.setCreatPlayerTime(rs.getTimestamp("CREAT_PLAYER_TIME"));
				user.setFenQu(rs.getString("FENQU"));
				user.setQuDaoId(rs.getString("QUDAOID"));
				user.setSex(rs.getString("SEX"));
				userList.add(user);
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
//		if(logger.isDebugEnabled()){
//			logger.debug("查询user用时："+(System.currentTimeMillis()-now)/1000+"s");
//		}
		logger.info("查询user用时："+(System.currentTimeMillis()-now)/1000+"s");
		return userList;
	
	}

	@Override
	
	/**
	 * 获取渠道
	 */
	public ArrayList<String> getQudao(java.util.Date date) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String> qudaoList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select t.key qudao  from channel t ");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String qudao=rs.getString("qudao");
				if(!"".equals(qudao)&&qudao!=null){
				    qudaoList.add(qudao);
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
		return qudaoList;
	}
	
	@Override
	public boolean addFenQu(String fenqu) {
		long now = System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_FENQU(" +
					"SEQ," +
					"NAME,"+
					"STARTDATA"+
					") values (SEQ_DT_STAT_FENQU.NEXTVAL,?,?)");
			
			ps.setString(1, fenqu);
			ps.setString(2, DateUtil.formatDateSafely(new Date(), "YYYY-MM-DD"));
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[插入分区] [OK] "+ fenqu+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			    logger.error("[插入分区] [FAIL] "+ fenqu+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? true : false;
	}

	
	
	
	@Override
	public boolean updateFenQu(String fenqu, String dateStr) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delFenQu(String fenquname) {
	       long now=System.currentTimeMillis();
			Connection con = null;
			PreparedStatement ps = null;
			int i = -1;
			try {
				con = dataSourceManager.getInstance().getConnection();
				ps = con.prepareStatement("delete from STAT_FENQU where NAME='"+fenquname+"'");
				i = ps.executeUpdate();
				if(logger.isDebugEnabled()){
				logger.debug("[删除用户] [OK] ["+fenquname+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
				}
			} catch (SQLException e) {
				logger.error("[删除用户] [fail] ["+fenquname+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	 * 获取所有分区
	 */
	public ArrayList<String []> getFenQu(java.util.Date date) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> fenquList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			//ps = con.prepareStatement("select t.name  fenqu,t.seq,t.STARTDATA from stat_fenqu t order by t.seq");
			ps = con.prepareStatement("select t.name  fenqu,t.seq,t.STARTDATA from stat_fenqu t  order by t.name");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String fenqu=rs.getString("fenqu");
				String seq=rs.getString("seq");
				String startdata=rs.getString("STARTDATA");
				String [] result=new String[3];
				if(!"".equals(fenqu)&&fenqu!=null){
					result[1]=fenqu;
					result[0]=seq;
					result[2]=startdata;
					fenquList.add(result);
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

	
	
	/**
	 * 根据不同状态获取分区信息
	 * 服务器状态： 0正常    1合并掉     2 测试服
	 * @param type
	 * @return
	 */
	@Override
	public ArrayList<String[]> getFenQuByStatus(String status) {


		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> fenquList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select t.name  fenqu,t.seq,t.STARTDATA from stat_fenqu t where t.status='"+status+"'  order by t.seq");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String fenqu=rs.getString("fenqu");
				String seq=rs.getString("seq");
				String startdata=rs.getString("STARTDATA");
				String [] result=new String[3];
				if(!"".equals(fenqu)&&fenqu!=null){
					result[1]=fenqu;
					result[0]=seq;
					result[2]=startdata;
					fenquList.add(result);
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
	public ArrayList<String[]> getFenQu_Group(Date date) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> fenquList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select t.fenqugroupid,g.name gname,t.fenquid,f.name fname,f.startdata " +
					" from dt_fenqugroupmap t ,dt_fenqugroup g,stat_fenqu f " +
					" where t.fenqugroupid=g.id and t.fenquid=f.seq");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String fenqugroupid=rs.getString("fenqugroupid");
				String gname=rs.getString("gname");
				String fenquid=rs.getString("fenquid");
				String fname=rs.getString("fname");
				String startdata=rs.getString("startdata");
				
				String [] result=new String[5];
				    
				    result[0]=fenqugroupid;
					result[1]=gname;
					result[2]=fenquid;
					result[3]=fname;
					result[4]=startdata;
					
					fenquList.add(result);
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
	public ArrayList<String[]> getFenQuGroup(Date date) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> fenquList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from dt_fenqugroup t");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String id=rs.getString("id");
				String name=rs.getString("name");
				String [] result=new String[2];
				if(name!=null&&!"".equals(name)){
					result[1]=name;
					result[0]=id;
					fenquList.add(result);
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
	public boolean addCurrencyType(String currencyType) {

		long now = System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into DT_CURRENCYTYPE(" +
					"id," +
					"NAME"+
					") values (SEQ_DT_CURRENCYTYPE.NEXTVAL,?)");
			
			ps.setString(1, currencyType);
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[插入货币类型] [OK] "+ currencyType+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			    logger.error("[插入货币类型] [FAIL] "+ currencyType+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? true : false;
	}

	@Override
	public boolean addDaoJuColor(String daoJuColor) {

		long now = System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into DT_DAOJUCOLOR(" +
					"id," +
					"NAME"+
					") values (SEQ_DT_DAOJUCOLOR.NEXTVAL,?)");
			
			ps.setString(1, daoJuColor);
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[插入道具颜色] [OK] "+ daoJuColor+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			    logger.error("[插入道具颜色] [FAIL] "+ daoJuColor+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? true : false;
	}

	@Override
	public boolean addDaoJuName(String daoJuName) {

		long now = System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into DT_DAOJUNAME(" +
					"id," +
					"NAME"+
					") values (SEQ_DT_DAOJUNAME.NEXTVAL,?)");
			
			ps.setString(1, daoJuName);
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[插入道具名称] [OK] "+ daoJuName+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			    logger.error("[插入道具名称] [FAIL] "+ daoJuName+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? true : false;
	}

	@Override
	public boolean addGetType(String getType) {

		long now = System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into DT_GETTYPE(" +
					"id," +
					"NAME"+
					") values (SEQ_DT_GETTYPE.NEXTVAL,?)");
			
			ps.setString(1, getType);
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[插入获取方式] [OK] "+ getType+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			    logger.error("[插入获取方式] [FAIL] "+ getType+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? true : false;
	}

	@Override
	public boolean addReasonType(String reasonType) {

		long now = System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into DT_REASONTYPE(" +
					"id," +
					"NAME"+
					") values (SEQ_DT_REASONTYPE.NEXTVAL,?)");
			
			ps.setString(1, reasonType);
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[插入获取原因] [OK] "+ reasonType+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			    logger.error("[插入获取原因] [FAIL] "+ reasonType+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? true : false;
	}

	@Override
	public ArrayList<String[]> getCurrencyType() {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> fenquList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select t.id,t.name from DT_CURRENCYTYPE t order by t.id");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String id=rs.getString("id");
				String name=rs.getString("name");
				String [] result=new String[2];
				if(!"".equals(name)&&name!=null){
					result[1]=name;
					result[0]=id;
					fenquList.add(result);
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
	public ArrayList<String[]> getDaoJuColor() {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> fenquList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select t.id,t.name from DT_DAOJUCOLOR t order by t.id");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String id=rs.getString("id");
				String name=rs.getString("name");
				String [] result=new String[2];
				if(!"".equals(name)&&name!=null){
					result[1]=name;
					result[0]=id;
					fenquList.add(result);
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
	public ArrayList<String[]> getDaoJuName() {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> fenquList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select t.id,t.name from DT_DAOJUNAME t order by t.id");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String id=rs.getString("id");
				String name=rs.getString("name");
				String [] result=new String[2];
				if(!"".equals(name)&&name!=null){
					result[1]=name;
					result[0]=id;
					fenquList.add(result);
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
	public ArrayList<String[]> getGetType() {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> fenquList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select t.id,t.name from DT_GETTYPE t order by t.id");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String id=rs.getString("id");
				String name=rs.getString("name");
				String [] result=new String[2];
				if(!"".equals(name)&&name!=null){
					result[1]=name;
					result[0]=id;
					fenquList.add(result);
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
	public ArrayList<String[]> getReasonType() {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> fenquList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select t.id,t.name from DT_REASONTYPE t order by t.id");
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String id=rs.getString("id");
				String name=rs.getString("name");
				String [] result=new String[2];
				if(!"".equals(name)&&name!=null){
					result[1]=name;
					result[0]=id;
					fenquList.add(result);
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
	public ArrayList<String[]> getZhuCeUserChongzhi_Mon(String regStartDateStr, String regEndDateStr,
			String statStartdateStr,String statEnddateStr,String quDao,String zhouqi, String jiXing) {
		Connection con = null;
		PreparedStatement ps = null;
		if(regStartDateStr.compareTo(statStartdateStr)>0){statStartdateStr=regStartDateStr;}
		
		ArrayList<String []> fenquList=new ArrayList();
		String subsql="";
		String csubsql="";
		if(quDao!=null) { subsql=" and u.qudao ='"+quDao+"'";   csubsql+=" and c.qudao='"+quDao+"'";} 
		if(jiXing!=null){ subsql+=" and u.jixing='"+jiXing+"'"; csubsql+=" and c.jixing='"+jiXing+"'"; }
		
		String dateformat="YYYY-MM";
		if("0".equals(zhouqi)){dateformat="YYYY-MM-DD";}
		if("1".equals(zhouqi)){dateformat="yyyy-iw";}
		if("2".equals(zhouqi)){dateformat="yyyy-MM";}
		
		String sql="select to_char(c.time,'"+dateformat+"') date1,sum(c.money) money" +
				" from stat_chongzhi c where c.time between  to_date('"+statStartdateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
				" and  to_date('"+statEnddateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +csubsql+
				" and c.username in ( select u.name from stat_user u " +
				" where u.registtime between to_date('"+regStartDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss')" +
				" and to_date('"+regEndDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') " +subsql+
				" ) group by to_char(c.time,'"+dateformat+"') order by to_char(c.time,'"+dateformat+"')";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String fenqu=rs.getString("date1");
				String seq=rs.getString("money");
				String [] result=new String[2];
				if(!"".equals(fenqu)&&fenqu!=null){
					result[1]=fenqu;
					result[0]=seq;
					fenquList.add(result);
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
	public User add(User user) {

		long now = System.currentTimeMillis();
		
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_USER(" +
					"ID," +
					"NAME," +
					"QUDAO," +
					
					"GAME," +
					"JIXING," +
					"DIDIAN," +
					
					"HAOMA," +
					"IMEI," +
					"REGISTTIME," +
					"UUID," +
					"GUOJIA," +
					
					"PLAYERNAME," +
					"CREAT_PLAYER_TIME," +
					"FENQU," +
					"QUDAOID,"+
					"SEX"+
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,? ,?,?,?,?, ?)");
			
			ps.setString(1, user.getName());
			ps.setString(2, user.getQuDao());
			
			ps.setString(3, user.getGame());
			ps.setString(4, user.getJiXing());
			ps.setString(5, user.getDiDian());
			
			ps.setString(6, user.getHaoMa());
			ps.setString(7, user.getImei());
			if (user.getRegistTime() != null) {
				ps.setTimestamp(8, new Timestamp(user.getRegistTime().getTime()));
			} else {
				ps.setTimestamp(8, null);
			}
			ps.setString(9, user.getUuid());
			ps.setString(10, user.getGuojia());
			
			ps.setString(11, user.getPlayerName());
			if (user.getCreatPlayerTime() != null) {
				ps.setTimestamp(12, new Timestamp(user.getCreatPlayerTime().getTime()));
			} else {
				ps.setTimestamp(12, null);
			}
			ps.setString(13, user.getFenQu());
			ps.setString(14, user.getQuDaoId());
			ps.setString(15, user.getSex());
			
			i = ps.executeUpdate();
			
			if(logger.isDebugEnabled()){
				logger.debug("[插入用户] [OK] "+user.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			    logger.error("[插入用户] [FAIL] "+user.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? user : null;
	}
	public boolean addUserList(ArrayList<User> userList) {

		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		boolean result=true;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_USER(" +
					"ID," +
					"NAME," +
					"QUDAO," +
					
					"GAME," +
					"JIXING," +
					"DIDIAN," +
					
					"HAOMA," +
					"IMEI," +
					"REGISTTIME," +
					"UUID," +
					"GUOJIA," +
					
					"PLAYERNAME," +
					"CREAT_PLAYER_TIME," +
					"FENQU," +
					"QUDAOID,"+
					
					"SEX"+
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,?  ,?,?,?,?)");
			for(User user:userList){
			ps.setString(1, user.getName());
			ps.setString(2, user.getQuDao());
			
			ps.setString(3, user.getGame());
			ps.setString(4, user.getJiXing());
			ps.setString(5, user.getDiDian());
			
			ps.setString(6, user.getHaoMa());
			ps.setString(7, user.getImei());
				if (user.getRegistTime() != null) {	ps.setTimestamp(8, new Timestamp(user.getRegistTime().getTime()));
				} else { ps.setTimestamp(8, null); }
			ps.setString(9, user.getUuid());
			ps.setString(10, user.getGuojia());
			
			ps.setString(11, user.getPlayerName());
				if (user.getCreatPlayerTime() != null) { ps.setTimestamp(12, new Timestamp(user.getCreatPlayerTime().getTime()));
				} else { ps.setTimestamp(12, null);	}
			
			ps.setString(13, user.getFenQu());
			ps.setString(14, user.getQuDaoId());
			ps.setString(15, user.getSex());
			
			i = ps.executeUpdate();
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

	@Override
	public boolean deleteById(Long id) {
       long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("delete from STAT_USER where ID=?");
			ps.setLong(1, id);
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
			logger.debug("[删除用户] [OK] ["+id+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (SQLException e) {
			logger.error("[删除用户] [fail] ["+id+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	public User getById(Long id) {

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from STAT_USER where id=?");
			ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			if(rs.next()) {
				User user = new User();
				
				user.setDiDian(rs.getString("DIDIAN"));
				user.setHaoMa(rs.getString("HAOMA"));
				user.setId(rs.getLong("ID"));
				user.setImei(rs.getString("IMEI"));
				user.setJiXing(rs.getString("JIXING"));
				user.setName(rs.getString("NAME"));
				
				user.setQuDao(rs.getString("QUDAO"));
				user.setRegistTime(rs.getTimestamp("REGISTTIME"));
				user.setUuid(rs.getString("UUID"));
				user.setGame(rs.getString("GAME"));
				user.setGuojia(rs.getString("GUOJIA"));
				
				user.setPlayerName(rs.getString("PLAYERNAME"));
				user.setCreatPlayerTime(rs.getTimestamp("CREAT_PLAYER_TIME"));
				user.setFenQu(rs.getString("FENQU"));
				user.setQuDaoId(rs.getString("QUDAOID"));
				user.setSex(rs.getString("SEX"));
			
				return user;
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
	public List<User> getBySql(String sql) {

		Connection con = null;
		PreparedStatement ps = null;
		List<User> userList=null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			//ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			userList=new ArrayList();
			while(rs.next()) {
				User user = new User();
				
				user.setDiDian(rs.getString("DIDIAN"));
				user.setHaoMa(rs.getString("HAOMA"));
				user.setId(rs.getLong("ID"));
				user.setImei(rs.getString("IMEI"));
				user.setJiXing(rs.getString("JIXING"));
				user.setName(rs.getString("NAME"));
				
				user.setQuDao(rs.getString("QUDAO"));
				user.setRegistTime(rs.getTimestamp("REGISTTIME"));
				user.setUuid(rs.getString("UUID"));
				user.setGame(rs.getString("GAME"));
				user.setGuojia(rs.getString("GUOJIA"));
				
				user.setPlayerName(rs.getString("PLAYERNAME"));
				user.setCreatPlayerTime(rs.getTimestamp("CREAT_PLAYER_TIME"));
				user.setFenQu(rs.getString("FENQU"));
				user.setQuDaoId(rs.getString("QUDAOID"));
				user.setSex(rs.getString("SEX"));
				userList.add(user);
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
		return userList;
	}

	public boolean updateList(ArrayList<User> userList) {
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		boolean result=true;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("update STAT_USER set "+
					"NAME=?," +
					"QUDAO=?," +
					"GAME=?," +
					
					"JIXING=?," +
					"DIDIAN=?," +
					"HAOMA=?," +
					
					"IMEI=?," +
					"REGISTTIME=?," +
					"UUID=?," +
					"GUOJIA=?," +
					
					"PLAYERNAME=?, " +
					"CREAT_PLAYER_TIME=?, " +
					"FENQU=?," +
					"QUDAOID=?,"+
					"SEX=?"+
					
					" where ID=?");
			for(User user:userList){
			
			ps.setString(1, user.getName());
			ps.setString(2, user.getQuDao());
			ps.setString(3, user.getGame());
			
			ps.setString(4, user.getJiXing());
			ps.setString(5,user.getDiDian());
			ps.setString(6, user.getHaoMa());
			
			ps.setString(7, user.getImei());
				if (user.getRegistTime() != null) {	ps.setTimestamp(8, new Timestamp(user.getRegistTime().getTime()));
				} else { ps.setTimestamp(8, null);	}
			
			ps.setString(9, user.getUuid());
			ps.setString(10, user.getGuojia());
			
			ps.setString(11, user.getPlayerName());
				if (user.getCreatPlayerTime() != null) { ps.setTimestamp(12, new Timestamp(user.getCreatPlayerTime().getTime()));
				} else { ps.setTimestamp(12, null);	}
			ps.setString(13, user.getFenQu());
			ps.setString(14, user.getQuDaoId());
			ps.setString(15, user.getSex());
			ps.setLong(16, user.getId());
			
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
			if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public boolean update(User user) {
        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_USER set "+
					"NAME=?," +
					"QUDAO=?," +
					"GAME=?," +
					
					"JIXING=?," +
					"DIDIAN=?," +
					"HAOMA=?," +
					
					"IMEI=?," +
					"REGISTTIME=?," +
					"UUID=?," +
					"GUOJIA=?," +
					
					"PLAYERNAME=?, " +
					"CREAT_PLAYER_TIME=?," +
					"FENQU=?," +
					"QUDAOID=?,"+
					
					"SEX=?"+
					" where ID=?");
			
			ps.setString(1, user.getName());
			ps.setString(2, user.getQuDao());
			ps.setString(3, user.getGame());
			
			ps.setString(4, user.getJiXing());
			ps.setString(5,user.getDiDian());
			ps.setString(6, user.getHaoMa());
			
			ps.setString(7, user.getImei());
			if (user.getRegistTime() != null) {
				ps.setTimestamp(8, new Timestamp(user.getRegistTime().getTime()));
			} else {
				ps.setTimestamp(8, null);
			}
			ps.setString(9, user.getUuid());
			ps.setString(10, user.getGuojia());
			
			ps.setString(11, user.getPlayerName());
			
			if (user.getCreatPlayerTime() != null) {
				ps.setTimestamp(12, new Timestamp(user.getCreatPlayerTime().getTime()));
			} else {
				ps.setTimestamp(12, null);
			}
			ps.setString(13, user.getFenQu());
			ps.setString(14, user.getQuDaoId());
			ps.setString(15, user.getSex());
			
			ps.setLong(16, user.getId());
			
			i =ps.executeUpdate();
			
			if(logger.isDebugEnabled()){
				logger.debug("[更新用户] [OK] "+user.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (SQLException e) {
			    logger.error("[更新用户] [fail] "+user.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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

	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

	
	public ArrayList<String> getQudaoRegistUserCount(String dateStr)
	{
		ArrayList<String> levelUserCount=new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String key=null;
		String value=null;
	String sql="select tt.qudao qudao,count(distinct(tt.name)) qudaonum from stat_user tt where trunc(tt.registtime)=to_date('"+dateStr+"','YYYY-MM-DD') group by qudao";
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
	return levelUserCount;	
		
	}
	
	
	@Override
	/**
	 * 获取注册但是没有创建角色的用户数
	 * @param statDateStr 开始日期
	 * @param endDateStr  终止日期
	 * @return
	 */
	public Long getZhuCeNOCreatPlayer(String startDateStr, String endDateStr,String quDao,String game) {

		Connection con = null;
		PreparedStatement ps = null;
		Long count=null;
		String subSql="";
		if(quDao!=null&&!quDao.isEmpty()){ subSql=" and t.qudao='"+quDao+"'";}
		if(game!=null&&!game.isEmpty()) { subSql=" and t.game ='"+game+"'";}
		try {
			con = dataSourceManager.getInstance().getConnection();
			String sql="select count(distinct t.name) count from stat_user t where t.playername is null " +subSql+
			" and trunc(t.registtime) between to_date('"+startDateStr+"','YYYY-MM-DD') " +
			" and to_date('"+endDateStr+"', 'YYYY-MM-DD')";
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
	
	
	
@Override
   /**
    * 获得有注册用户数
    */
	public Long getRegistUerCount(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
	Connection con = null;
	PreparedStatement ps = null;
	Long count=null;
	String subSql="";
	if(qudao!=null&&!qudao.isEmpty()){subSql+=" and t.qudao in ('"+qudao+"')";}
	
	if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and t.fenqu='"+fenQu+"'";}
	if(jixing!=null&&!jixing.isEmpty()){subSql+=" and t.jixing='"+jixing+"'";}
	try {
		con = dataSourceManager.getInstance().getConnection();
		String sql="select count(distinct(t.name)) count from stat_user t " +
		"where t.registtime between  to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss')" +
				" and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')"+subSql;
		
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
 * 获得有效用户数平均在线时长
 * 有效用户定义：达到5级且在线达到15分钟以上，包括15分钟
 * @param startDateStr开始日期
 * @param endDateStr终止日期
 * @return
 */
	public Long getYouXiaoUerAVGOnLineTime(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing) {

		Connection con = null;
		PreparedStatement ps = null;
		Long count=null;
		String subSqlu="";
		String subSqlp="";
		if(qudao!=null&&!qudao.isEmpty()){subSqlu+=" and u.qudao in ('"+qudao+"')";}
		if(fenQu!=null&&!fenQu.isEmpty()){subSqlp+=" and p.fenqu='"+fenQu+"'";}
		if(jixing!=null&&!jixing.isEmpty()){subSqlp+=" and p.jixing='"+jixing+"'";}
		try {
			con = dataSourceManager.getInstance().getConnection();
			
			String sql="select avg(onlinetime) count from( select p.username,max(p.maxlevel) maxlevel,sum(p.onlinetime) onlinetime " +
			  		" from stat_playgame  p where p.username in( select u.name" +
			  		" from stat_user u  where 1=1  "+subSqlu+" and u.registtime between " +
			  		" to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and  to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') ) and" +
			  		" maxlevel>4 and onlinetime>900000 " +
			  		" and p.enterdate between" +
			  		" to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and" +
			  		" to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss')"
			  		+subSqlp+" group by p.username )";
			
			
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if (rs.next()) {
				count=(long)rs.getFloat("count");
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
 * 获得有效用户数
 * 有效用户定义：达到5级且在线达到15分钟以上，包括15分钟
 * @param startDateStr开始日期
 * @param endDateStr终止日期
 * @return
 */
	public Long getYouXiaoUerCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing) {

		Connection con = null;
		PreparedStatement ps = null;
		Long count=null;
		String subSqlu="";
		String subSqlp="";
		if(qudao!=null&&!qudao.isEmpty()){subSqlu+=" and u.qudao in ('"+qudao+"')";}
		if(fenQu!=null&&!fenQu.isEmpty()){subSqlp+=" and p.fenqu='"+fenQu+"'";}
		if(jixing!=null&&!jixing.isEmpty()){subSqlp+=" and p.jixing='"+jixing+"'";}
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			
			String sql="select count(*) count from( select p.username,max(p.maxlevel) maxlevel,sum(p.onlinetime) onlinetime " +
			  		" from stat_playgame  p where " + 
			  		"  p.enterdate   between to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and  to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') "+
			  		" and p.username in( select  u.name" +
			  		" from stat_user u  where 1=1  "+subSqlu+" and u.registtime between " +
			  		" to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') and  to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') ) and" +
			  		"  maxlevel>10 and onlinetime>600000 "+subSqlp+" group by p.username )";
			
			
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
	
	
	@Override
public List<String[]> getstat_common(String dateStr) {

	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String[]> resultlist=new ArrayList<String[]>();
	try {
		con = dataSourceManager.getInstance().getConnection();
		String sql="select t.*, t.rowid from stat_base_day t where t.time_index='"+dateStr+"'";
		ps = con.prepareStatement(sql);
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		while(rs.next()) {
			String[] vales=new String[17];
			
			vales[0]=rs.getString("TIME_INDEX");
			vales[1]=rs.getString("REGISTUSERCOUNT");
			vales[2]=rs.getString("EFFECTIVE_REGUSERCOUNT");
			vales[3]=rs.getString("WEIJIHOUUSERCOUNT");
			vales[4]=rs.getString("DULIJINRUCOUNT");
			vales[5]=rs.getString("DULIJINRUCOUNT_TODAY");
			vales[6]=rs.getString("MEIFENZHONGPINJUNZAIXIAN");
			vales[7]=rs.getString("MEIFENZHONGZUIGAOZAIXIAN");
			vales[8]=rs.getString("MEIRENPINJUNZAIXIAN");
			vales[9]=rs.getString("FUFEIRENSHU");
			vales[10]=rs.getString("XINFUFEIRENSHU");
			vales[11]=rs.getString("HUOYAOFUFEIRENSHU");
			vales[12]=rs.getString("CHONGZHIJINE");
			vales[13]=rs.getString("ZHUCEARPU");
			vales[14]=rs.getString("FUFEIARPU");
			vales[15]=rs.getString("YUANBAOKUCUN");
			vales[16]=rs.getString("YOUXIBIKUCUN");
			
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
public List<String[]> getstat_common(String dateStr,String orderIndex) {

	Connection con = null;
	PreparedStatement ps = null;
	ArrayList<String[]> resultlist=new ArrayList<String[]>();
	try {
		con = dataSourceManager.getInstance().getConnection();
		
		String sql="select t.*, t.rowid from stat_base_day t where t.time_index='"+dateStr+"'";
		if(orderIndex!=null)
		{
			sql+=" order by t."+orderIndex +" desc";
			
		}
		ps = con.prepareStatement(sql);
		ps.execute();
		ResultSet rs =  ps.getResultSet();
		while(rs.next()) {
			String[] vales=new String[17];
			
			vales[0]=rs.getString("TIME_INDEX");
			vales[1]=rs.getString("REGISTUSERCOUNT");
			vales[2]=rs.getString("EFFECTIVE_REGUSERCOUNT");
			vales[3]=rs.getString("WEIJIHOUUSERCOUNT");
			vales[4]=rs.getString("DULIJINRUCOUNT");
			vales[5]=rs.getString("DULIJINRUCOUNT_TODAY");
			vales[6]=rs.getString("MEIFENZHONGPINJUNZAIXIAN");
			vales[7]=rs.getString("MEIFENZHONGZUIGAOZAIXIAN");
			vales[8]=rs.getString("MEIRENPINJUNZAIXIAN");
			vales[9]=rs.getString("FUFEIRENSHU");
			vales[10]=rs.getString("XINFUFEIRENSHU");
			vales[11]=rs.getString("HUOYAOFUFEIRENSHU");
			vales[12]=rs.getString("CHONGZHIJINE");
			vales[13]=rs.getString("ZHUCEARPU");
			vales[14]=rs.getString("FUFEIARPU");
			vales[15]=rs.getString("YUANBAOKUCUN");
			vales[16]=rs.getString("YOUXIBIKUCUN");
			
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

	@Override
	public List<String[]> getJueSeGuoJiaFenBu(String startDateStr, String endDateStr,String flag, String qudao, String fenQu) {
		
		String subSql="";
		if(qudao!=null&&!qudao.isEmpty()){subSql+=" and p.qudao in ('"+qudao+"')";}
		if(fenQu!=null&&!fenQu.isEmpty()){subSql+=" and p.fenqu='"+fenQu+"'";}
		
		if(flag!=null)
		{
			subSql+= " and p.username  in ( " +
				" select t.name from stat_user t where t.registtime between  to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss')" +
				" and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +
				"  ) ";
		}
		
		String sql=" select  p.game guojia,count(p.game) count from stat_playgame p where p.enterdate between  to_date('"+startDateStr+" 00:00:00', 'YYYY-MM-DD hh24:mi:ss') " +
		" and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') " +subSql+
		" group by p.game";

		
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
				vales[0]=rs.getString("guojia");
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
   public boolean addstat_common(String dateStr, String[] data) {

	Connection con = null;
	PreparedStatement ps = null;
	int i = -1;
	try {
		con = dataSourceManager.getInstance().getConnection();
		ps = con.prepareStatement("insert into STAT_BASE_DAY(" +
				"ID," +
				"TIME_INDEX," +
				"REGISTUSERCOUNT," +
				
				"EFFECTIVE_REGUSERCOUNT," +
				"WEIJIHOUUSERCOUNT," +
				"DULIJINRUCOUNT," +
				
				"DULIJINRUCOUNT_TODAY," +
				"MEIFENZHONGPINJUNZAIXIAN," +
				"MEIFENZHONGZUIGAOZAIXIAN," +
				"MEIRENPINJUNZAIXIAN," +
				"FUFEIRENSHU," +
				
				"XINFUFEIRENSHU," +
				"HUOYAOFUFEIRENSHU," +
				"CHONGZHIJINE," +
				"ZHUCEARPU,"+
				
				"FUFEIARPU," +
				"YUANBAOKUCUN," +
				"YOUXIBIKUCUN"+
				") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,? ,?,?,?,? ,?,?,?)");
		
		ps.setString(1, data[0]);
		ps.setString(2, data[1]);
		
		ps.setString(3, data[2]);
		ps.setString(4, data[3]);
		ps.setString(5, data[4]);
		
		ps.setString(6, data[5]);
		ps.setString(7, data[6]);
		ps.setString(8, data[7]);
		ps.setString(9, data[8]);
		ps.setString(10,data[9]);
		
		ps.setString(11, data[10]);
		ps.setString(12, data[11]);
		ps.setString(13, data[12]);
		ps.setString(14, data[13]);
		
		ps.setString(15, data[14]);
		ps.setString(16, data[15]);
		ps.setString(17, data[16]);
		
		i = ps.executeUpdate();
	} catch (Exception e) {
		logger.error("日报缓存添加异常:"+data[0]+data[1]+data[2]+data[3]+data[4]+data[5]+data[6]+data[7]+data[8]+data[9]+data[10]+data[11]+data[12]+data[13]+data[14]+data[15]+data[16],e);
	} finally {
		try {
			if(ps != null) ps.close();
			if(con != null) con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	return i > 0 ? true : false;
}
	

	@Override
	public List<String[]> getregestUserLevel(String regStartDateStr, String regEndDateStr, String statStartdateStr, String quDao, String fenQu) {
		
		String subSql="";
		if(quDao!=null&&!"".equals(quDao)){ subSql+="  and p.qudao='"+quDao+"' ";}
		if(fenQu!=null&&!"".equals(fenQu)){ subSql+="  and p.fenqu='"+fenQu+"' ";}
		
		String sql="select m.llevel,count(username) count from (" +
				" select  p.username,max(p.maxlevel) llevel from stat_playgame p where 1=1 " +subSql+
				" and to_char(p.enterdate,'YYYY-MM-DD') <='"+statStartdateStr+"' and p.username in (" +
				" select u.name from stat_user u where to_char(u.registtime,'YYYY-MM-DD') between '"+regStartDateStr+"' and '"+regEndDateStr+"') " +
				" group by p.username ) m group by m.llevel order by m.llevel";
		
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
				vales[0]=rs.getString("llevel");
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserDaoImpl userDaoImpl=new UserDaoImpl();
		
		
		User user=new User();
		user.setDiDian("beijing");
		user.setHaoMa("13260180229");
		user.setName("jieyh");
		user.setJiXing("lenevo");
		userDaoImpl.add(user);
		//userDaoImpl.getById(1L);

	}

}
