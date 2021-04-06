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

import com.sqage.stat.commonstat.dao.DaoJuDao;
import com.sqage.stat.commonstat.entity.DaoJu;
import com.sqage.stat.language.MultiLanguageManager;
import com.xuanzhi.tools.dbpool.DataSourceManager;

/**
 * 
 *
 */
public class DaoJuDaoImpl implements DaoJuDao {
	static Logger logger = Logger.getLogger(DaoJuDaoImpl.class);
	DataSourceManager dataSourceManager;
	
	public void addList(ArrayList<DaoJu> daoJuList) {
        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_DAOJU(" +
					"ID," +
					"CREATEDATE," +
					"USERNAME," +
					
					"FENQU," +
					"GAMELEVEL," +
					"DAOJUNAME," +
					
					"HUOBITYPE," +
					"DANJIA," +
					"DAOJUNUM," +
					"GETTYPE,"+
					"POSITION,"+
					
					"DAOJUCOLOR,"+
					"DAOJULEVEL,"+
					"BINDTYPE,"+
					"JIXING,"+
					
					"VIP,"+
					"GUOJIA"+
					
					") values (SEQ_STAT_DAOJU_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?, ?,?,? ,? ,?,?,?)");

		    int i = -1;
			int count=0;
		for(DaoJu daoJu : daoJuList){
			if(daoJu.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(daoJu.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, daoJu.getUserName());
			
			ps.setString(3, daoJu.getFenQu());
			ps.setString(4, daoJu.getGameLevel());
			ps.setString(5, daoJu.getDaoJuName());
			
			ps.setString(6, daoJu.getHuoBiType());
			ps.setLong(7, daoJu.getDanJia());
			ps.setLong(8, daoJu.getDaoJuNum());
			ps.setString(9, daoJu.getGetType());
			ps.setString(10, daoJu.getPosition());
			
			ps.setString(11, daoJu.getDaoJuColor());
			ps.setString(12, daoJu.getDaoJuLevel());
			ps.setString(13, daoJu.getBindType());
			
			ps.setString(14, daoJu.getJixing());
			ps.setLong(15, daoJu.getVip());
			ps.setString(16, daoJu.getGuojia());
			
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%600==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			
			if(logger.isDebugEnabled()){logger.debug("开始批量插入数据库道具"+daoJuList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("插入道具数据库异常",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[添加道具信息回滚异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[添加道具信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	public void addDaoJu_MoHuList(ArrayList<DaoJu> daoJuMoHuList) {
        Long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into stat_daoju_mohu(" +
					"ID," +
					"CREATEDATE," +
					"FENQU," +
					"DAOJUNAME," +
					
					"HUOBITYPE," +
					"DAOJUNUM," +
					"GETTYPE,"+
					"POSITION,"+
					
					"DAOJUCOLOR,"+
					"DAOJULEVEL,"+
					"DAOJUDANJIA,"+
					"vip"+
					
					") values (SEQ_STAT_DAOJU_ID.NEXTVAL, ?,?,?, ?,?,?, ?,?,?,?,?)");

		    int i = -1;
			int count=0;
		for(DaoJu daoJu : daoJuMoHuList){
			if(daoJu.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(daoJu.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, daoJu.getFenQu());
			ps.setString(3, daoJu.getDaoJuName());
			
			ps.setString(4, daoJu.getHuoBiType());
			ps.setLong(5, daoJu.getDaoJuNum());
			ps.setString(6, daoJu.getGetType());
			ps.setString(7, daoJu.getPosition());
			
			ps.setString(8, daoJu.getDaoJuColor());
			ps.setString(9, daoJu.getDaoJuLevel());
			ps.setLong(10, daoJu.getDanJia());
			ps.setLong(11, daoJu.getVip());
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%100==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			if(logger.isDebugEnabled()){logger.debug("开始批量插入粗略数据库道具"+daoJuMoHuList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("插入道具数据库异常",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[添加粗略道具信息回滚异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[添加粗略道具信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	public DaoJu add(DaoJu daoJu) {
        Long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("insert into STAT_DAOJU(" +
					"ID," +
					"CREATEDATE," +
					"USERNAME," +
					
					"FENQU," +
					"GAMELEVEL," +
					"DAOJUNAME," +
					
					"HUOBITYPE," +
					"DANJIA," +
					"DAOJUNUM," +
					"GETTYPE,"+
					"POSITION,"+
					
					"DAOJUCOLOR,"+
					"DAOJULEVEL,"+
					"BINDTYPE,"+
					"JIXING,"+
					"VIP,"+
					"GUOJIA"+
					
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?, ?,?,? ,? ,?,? ,?)");
			
			if(daoJu.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(daoJu.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setString(2, daoJu.getUserName());
			
			ps.setString(3, daoJu.getFenQu());
			ps.setString(4, daoJu.getGameLevel());
			ps.setString(5, daoJu.getDaoJuName());
			
			ps.setString(6, daoJu.getHuoBiType());
			ps.setLong(7, daoJu.getDanJia());
			ps.setLong(8, daoJu.getDaoJuNum());
			ps.setString(9, daoJu.getGetType());
			ps.setString(10, daoJu.getPosition());
			
			ps.setString(11, daoJu.getDaoJuColor());
			ps.setString(12, daoJu.getDaoJuLevel());
			ps.setString(13, daoJu.getBindType());
			
			ps.setString(14, daoJu.getJixing());
			ps.setLong(15, daoJu.getVip());
			ps.setString(16, daoJu.getGuojia());
			
			i = ps.executeUpdate();
			if(logger.isDebugEnabled()){
				logger.debug("[添加道具信息] [OK] " +daoJu.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			    logger.error("[添加道具信息] [FAIL] "+daoJu.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i > 0 ? daoJu : null;
	}

	@Override
	public boolean deleteById(Long id) {
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("delete from STAT_DAOJU where ID=?");
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
	public DaoJu getById(Long id) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select * from STAT_DAOJU where id=?");
			ps.setLong(1, id);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			if(rs.next()) {
				DaoJu daoJu = new DaoJu();
				
				daoJu.setCreateDate(rs.getTimestamp("CREATEDATE"));
				daoJu.setDanJia(rs.getLong("DANJIA"));
				daoJu.setDaoJuName(rs.getString("DAOJUNAME"));
				daoJu.setDaoJuNum(rs.getLong("DAOJUNUM"));
				
				daoJu.setFenQu(rs.getString("FENQU"));
				daoJu.setGameLevel(rs.getString("GAMELEVEL"));
				daoJu.setGetType(rs.getString("GETTYPE"));
				
				daoJu.setHuoBiType(rs.getString("HUOBITYPE"));
				daoJu.setId(rs.getLong("ID"));
				daoJu.setUserName(rs.getString("USERNAME"));
				daoJu.setPosition(rs.getString("POSITION"));
				
				daoJu.setBindType(rs.getString("BINDTYPE"));
				daoJu.setDaoJuColor(rs.getString("DAOJUCOLOR"));
				daoJu.setDaoJuLevel(rs.getString("DAOJULEVEL"));
				
				daoJu.setJixing(rs.getString("JIXING"));
			
				return daoJu;
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
	public List<DaoJu> getBySql(String sql) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		List<DaoJu> daoJuList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				DaoJu daoJu = new DaoJu();
				
				daoJu.setCreateDate(rs.getTimestamp("CREATEDATE"));
				daoJu.setDanJia(rs.getLong("DANJIA"));
				daoJu.setDaoJuName(rs.getString("DAOJUNAME"));
				daoJu.setDaoJuNum(rs.getLong("DAOJUNUM"));
				
				daoJu.setFenQu(rs.getString("FENQU"));
				daoJu.setGameLevel(rs.getString("GAMELEVEL"));
				daoJu.setGetType(rs.getString("GETTYPE"));
				
				daoJu.setHuoBiType(rs.getString("HUOBITYPE"));
				daoJu.setId(rs.getLong("ID"));
				daoJu.setUserName(rs.getString("USERNAME"));
				daoJu.setPosition(rs.getString("POSITION"));
				daoJu.setJixing(rs.getString("JIXING"));
				daoJuList.add(daoJu);
			}
			if(logger.isDebugEnabled()){
				logger.debug("[查询道具信息] [OK] [查询:"+ daoJuList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
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
		return daoJuList;
	}

	@Override
	public List<DaoJu> getByDaoJu(DaoJu daoJu) {
		
		Date createDate=daoJu.getCreateDate();
		Long danJia=daoJu.getDanJia();
		String daoJuName=daoJu.getDaoJuName();
		Long daoJuNum=daoJu.getDaoJuNum();
		String fenQu=daoJu.getFenQu();
		String gameLevel=daoJu.getGameLevel();
		String getType=daoJu.getGetType();
		
		String huoBiType=daoJu.getHuoBiType();
		//Long id=daoJu.getId();
		String position=daoJu.getPosition();
		String userName=daoJu.getUserName();

		String daoJuColor=daoJu.getDaoJuColor();//道具颜色
		String daoJuLevel=daoJu.getDaoJuLevel();//道具级别
		String bindType=daoJu.getBindType();// 道具绑定类型
		String jixing =daoJu.getJixing();
		
		
		String subSql="";
		 SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
         
		if(createDate!=null){ subSql+=" and trunc(t.createdate)=to_date('"+sf.format(createDate)+"','YYYY-MM-DD')";}
		if(daoJuName!=null){  subSql+=" and t.daojuname='"+daoJuName+"'";}
		if(fenQu!=null)    {  subSql+=" and t.FENQU='"+fenQu+"'";}
		if(gameLevel!=null){  subSql+=" and t.GAMELEVEL='"+gameLevel+"'";}
		if(getType!=null)  {  subSql+=" and t.GETTYPE='"+getType+"'";}
		
		if(huoBiType!=null){  subSql+=" and t.HUOBITYPE='"+huoBiType+"'";}
		if(position!=null) {  subSql+=" and t.POSITION='"+position+"'";}
		if(userName!=null) {  subSql+=" and t.USERNAME='"+userName+"'";}
		if(fenQu!=null)    {  subSql+=" and t.FENQU='"+fenQu+"'";}
		if(danJia!=null)   {  subSql+=" and t.DANJIA='"+danJia+"'";}
		
		if(daoJuColor!=null)  {  subSql+=" and t.DAOJUCOLOR='"+daoJuColor+"'";}
		if(daoJuLevel!=null)  {  subSql+=" and t.DAOJULEVEL='"+daoJuLevel+"'";}
		if(bindType!=null)    {  subSql+=" and t.BINDTYPE='"+bindType+"'";}
		
		if(bindType!=null)    {  subSql+=" and t.BINDTYPE='"+bindType+"'";}
		if(jixing!=null)      {   subSql+=" and t.JIXING='"+jixing+"'";}
		
		String sql="select * from stat_daoju t where 1=1 "+subSql;
//		if(logger.isDebugEnabled()){
//			logger.debug("[获取道具信息:"+sql+"] ");
//		}
		return getBySql(sql);
	}

	@Override
	public boolean update(DaoJu daoJu) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i =-1;
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("update STAT_DAOJU set "+
					"CREATEDATE=?," +
					"USERNAME=?," +
					
					"FENQU=?," +
					"GAMELEVEL=?," +
					"DAOJUNAME=?," +
					
					"HUOBITYPE=?," +
					"DANJIA=?," +
					"DAOJUNUM=?," +
					"GETTYPE=?,"+
					"POSITION=?,"+
					
					"DAOJUCOLOR=?,"+
					"DAOJULEVEL=?,"+
					"BINDTYPE=?,"+
					"JIXING=?"+
					
					" where ID=?");
			if(daoJu.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(daoJu.getCreateDate().getTime()));}
			else{ps.setTimestamp(1, null);}
			ps.setString(2, daoJu.getUserName());
			
			ps.setString(3, daoJu.getFenQu());
			ps.setString(4, daoJu.getGameLevel());
			ps.setString(5,daoJu.getDaoJuName());
			
			ps.setString(6, daoJu.getHuoBiType());
			ps.setLong(7, daoJu.getDanJia());
			ps.setLong(8, daoJu.getDaoJuNum());
			ps.setString(9, daoJu.getGetType());
			ps.setString(10, daoJu.getPosition());
			
			ps.setString(11, daoJu.getDaoJuColor());
			ps.setString(12, daoJu.getDaoJuLevel());
			ps.setString(13, daoJu.getBindType());
			ps.setString(14, daoJu.getJixing());
			
			ps.setLong(15, daoJu.getId());
			i =ps.executeUpdate();
			
			if(logger.isDebugEnabled()){
				logger.debug("[更新道具信息] [OK] "+daoJu.toString()+" [cost:"+(System.currentTimeMillis()-now)+"ms]");
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
		return i > 0;
	}

	@Override
	public boolean updateList(ArrayList<DaoJu> daoJuList) {
		Connection con = null;
		PreparedStatement ps = null;
		long now=System.currentTimeMillis();
		
		int i =-1;
		boolean result=true;
		int count=0;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("update STAT_DAOJU set "+
					"CREATEDATE=?," +
					"USERNAME=?," +
					
					"FENQU=?," +
					"GAMELEVEL=?," +
					"DAOJUNAME=?," +
					
					"HUOBITYPE=?," +
					"DANJIA=?," +
					"DAOJUNUM=?," +
					"GETTYPE=?,"+
					"POSITION=?,"+
					
					"DAOJUCOLOR=?,"+
					"DAOJULEVEL=?,"+
					"BINDTYPE=?,"+
					"JIXING=?"+
					
					" where ID=?");
			
		for(DaoJu daoJu:daoJuList){
			if(daoJu.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(daoJu.getCreateDate().getTime()));}
			else{ps.setTimestamp(1, null);}
			ps.setString(2, daoJu.getUserName());
			
			ps.setString(3, daoJu.getFenQu());
			ps.setString(4, daoJu.getGameLevel());
			ps.setString(5,daoJu.getDaoJuName());
			
			ps.setString(6, daoJu.getHuoBiType());
			ps.setLong(7, daoJu.getDanJia());
			ps.setLong(8, daoJu.getDaoJuNum());
			ps.setString(9, daoJu.getGetType());
			ps.setString(10, daoJu.getPosition());
			
			ps.setString(11, daoJu.getDaoJuColor());
			ps.setString(12, daoJu.getDaoJuLevel());
			ps.setString(13, daoJu.getBindType());
			
            ps.setString(14, daoJu.getJixing());
			ps.setLong(15, daoJu.getId());
			i =ps.executeUpdate();
			count++;
			if(count>100){ con.commit(); count=0; }
			
			
		}
		    con.setAutoCommit(true);
			con.commit();
			if(logger.isDebugEnabled()){
				logger.debug("[更新道具信息] [OK] [更新："+daoJuList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (SQLException e) {
			result=false;
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			if(con != null&&!con.isClosed()) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public List<String[]> getDaoJuImportant(String startDateStr, String endDateStr, String fenQu, String daojuname, String daojuColor) {
		Connection con = null;
		PreparedStatement ps = null;
		long now=System.currentTimeMillis();
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		
		String subSql=" and t.createdate between '"+startDateStr+"' and '"+endDateStr+"'";
		if(fenQu!=null){subSql+=" and t.fenqu='"+fenQu+"'";}
		if(daojuname!=null){ subSql+=" and  n.name='"+daojuname+"'";}
		if(daojuColor!=null){subSql+=" and t.daojucolor='"+daojuColor+"'";}
		
//		String 职业礼包获得=MultiLanguageManager.translateMap.get("职业礼包获得");
//		String 提取邮件家族=MultiLanguageManager.translateMap.get("提取邮件，家族");
//		String 商店购买=MultiLanguageManager.translateMap.get("商店购买");
//		String 任务=MultiLanguageManager.translateMap.get("任务");
//		String 礼包获得=MultiLanguageManager.translateMap.get("礼包获得");
//		String 开瓶子获得=MultiLanguageManager.translateMap.get("开瓶子获得");
//		String 合成获得=MultiLanguageManager.translateMap.get("合成获得");
//		String 怪物掉落拾取=MultiLanguageManager.translateMap.get("怪物掉落拾取");
//		String 使用持续性道具=MultiLanguageManager.translateMap.get("使用持续性道具");
//		String 商店出售删除=MultiLanguageManager.translateMap.get("商店出售删除");
//		String 合成删除=MultiLanguageManager.translateMap.get("合成删除");
//		String 丢弃=MultiLanguageManager.translateMap.get("丢弃");
//		String 到期删除=MultiLanguageManager.translateMap.get("到期删除");
//		String 死亡掉落=MultiLanguageManager.translateMap.get("死亡掉落");
		
		
		String 职业礼包获得="职业礼包获得";
		String 提取邮件家族="提取邮件，家族";
		String 商店购买="商店购买";
		String 任务="任务";
		String 礼包获得="礼包获得";
		String 开瓶子获得="开瓶子获得";
		String 合成获得="合成获得";
		String 怪物掉落拾取="怪物掉落拾取";
		String 使用持续性道具="使用持续性道具";
		String 商店出售删除="商店出售删除";
		String 合成删除="合成删除";
		String 丢弃="丢弃";
		String 到期删除="到期删除";
		String 死亡掉落="死亡掉落";

		//国服
		String sql="select huoqu.createdate,huoqu.count huoqu,shiyong.count shiyong,huishou.count huishou,diaoluo.count diaoluo from " +
				" ( select t.createdate,sum(t.daojunum) count from stat_daoju_deshi t,dt_daojuname n,dt_gettype g " + 
				" where  (g.name = '"+职业礼包获得+"' or g.name ='"+提取邮件家族+"' or g.name ='"+商店购买+"' or g.name ='"+任务+"' " +
				" or g.name ='"+礼包获得+"' or g.name ='"+开瓶子获得+"'  or g.name ='"+合成获得+"' or g.name ='"+怪物掉落拾取+"')  " +subSql
				+" and t.daojuname=n.id and t.gettype=g.id"+
				" group by t.createdate" +
				")  huoqu " +
				" left join " +
				" ( select t.createdate,sum(t.daojunum) count from stat_daoju_deshi t,dt_daojuname n,dt_gettype g where  g.name ='"+使用持续性道具+"' "
				+ subSql+" and t.daojuname=n.id and t.gettype=g.id "+
				" group by t.createdate " +
				" ) shiyong " +
				" on huoqu.createdate=shiyong.createdate " +
				" left join " +
				" ( select t.createdate,sum(t.daojunum) count from stat_daoju_deshi t,dt_daojuname n,dt_gettype g where ( g.name = '"+商店出售删除+"' or  g.name ='"+合成删除+"' " +
				"  or  g.name ='"+丢弃+"' or  g.name ='"+到期删除+"') " +subSql+" and t.daojuname=n.id and t.gettype=g.id "+
				" group by t.createdate ) huishou " +
				"  on huoqu.createdate=huishou.createdate " +
				" left join " +
				" ( select t.createdate,sum(t.daojunum) count from stat_daoju_deshi t,dt_daojuname n,dt_gettype g " +
				" where   g.name ='"+死亡掉落+"' "+subSql+" and t.daojuname=n.id and t.gettype=g.id group by t.createdate " +
						") diaoluo " +
				" on huoqu.createdate=diaoluo.createdate order by huoqu.createdate";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				
				String[] vales=new String[5];
				vales[0]=rs.getString("createdate");
				vales[1]=rs.getString("huoqu");
				vales[2]=rs.getString("shiyong");
				vales[3]=rs.getString("huishou");
				vales[4]=rs.getString("diaoluo");
				
				resultlist.add(vales);
			}
			if(logger.isDebugEnabled()){
				logger.debug("[查询重要道具信息] [OK] [查询："+resultlist.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
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
	 * 使用道具的玩家的vip
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param daojuname
	 * @param daojuColor
	 * @return
	 */
	
	public List<String[]> getDaoJuByVip(String startDateStr, String endDateStr, String fenQu, String daojuname, String daojuColor) {
		Connection con = null;
		PreparedStatement ps = null;
		long now =System.currentTimeMillis();
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		
		String subSql=" and t.createdate between '"+startDateStr+"' and '"+endDateStr+"'";
		if(fenQu!=null){subSql+=" and t.fenqu='"+fenQu+"'";}
		if(daojuname!=null){ subSql+=" and  n.name='"+daojuname+"'";}
		if(daojuColor!=null){subSql+=" and t.daojucolor='"+daojuColor+"'";}
		//String 使用持续性道具=MultiLanguageManager.translateMap.get("使用持续性道具");
		String 使用持续性道具="使用持续性道具";
		String sql="select t.vip,sum(t.daojunum) daojunum from stat_daoju_deshi t,dt_gettype g,dt_daojuname n g " +
				" where g.name='"+使用持续性道具+"' " +subSql+
				" and to_char(t.gettype)=to_char(g.id) and to_char(t.daojuname)=to_char(n.id) and " +
				" group by t.vip order by t.vip";
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				
				String[] vales=new String[2];
				vales[0]=rs.getString("vip");
				vales[1]=rs.getString("daojunum");
				
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
		if(logger.isDebugEnabled()){
			logger.debug("[查询使用道具的玩家的vip] [OK] [查询："+resultlist.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		}
		return resultlist;
	}

	
	
	
	@Override
	public ArrayList<String[]> getDaoJushop(String startDateStr) {
		Connection con = null;
		PreparedStatement ps = null;
		long now =System.currentTimeMillis();
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		//String sql="select * from stat_daoju_shop t where t.createdate ='"+startDateStr+"'";
		//台服
//		String sql=" select t.* " +
//				" from stat_daoju_shop t where t.createdate ='"+startDateStr+"'";
		//国服
		String sql=" select t.fenqu,n.name daojuname,t.createdate,t.position,t.daojunum,t.totalmoney,t.vip,t.guojia " +
		" from stat_daoju_shop t,dt_daojuname n where t.createdate ='"+startDateStr+"' and t.daojuname=n.id ";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				
				String[] vales=new String[8];
				vales[0]=rs.getString("FENQU");
				vales[1]=rs.getString("DAOJUNAME");
				vales[2]=rs.getString("CREATEDATE");
				vales[3]=rs.getString("POSITION");
				vales[4]=rs.getString("DAOJUNUM");
				vales[5]=rs.getString("TOTALMONEY");
				vales[6]=rs.getString("VIP");
				vales[7]=rs.getString("GUOJIA");
				
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
		if(logger.isDebugEnabled()){
			logger.debug("[查询商店购买道具信息] [OK] [查询："+resultlist.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		}
		return resultlist;
	}

	@Override
	public ArrayList<String[]> getDaoJuHuiZong(String startDateStr) {
		Connection con = null;
		PreparedStatement ps = null;
		long now=System.currentTimeMillis();
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		//String sql="select * from stat_daoju_deshi t where t.createdate ='"+startDateStr+"'";
		
		
		 //国服
		String sql=" select t.fenqu,n.name daojuname,t.daojucolor,t.createdate,t.daojunum,t.totalmoney,g.name gettype,t.vip,t.guojia " +
				" from stat_daoju_deshi t,dt_daojuname n,dt_gettype g " +
				" where t.createdate ='"+startDateStr+"' and t.daojuname=n.id and to_char(t.gettype)=to_char(g.id)";
		  
		
//		//台服
//		String sql=" select * " +
//		" from stat_daoju_deshi t " +
//		" where t.createdate ='"+startDateStr+"' ";
		  
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				
				String[] vales=new String[9];
				vales[0]=rs.getString("FENQU");
				vales[1]=rs.getString("DAOJUNAME");
				vales[2]=rs.getString("DAOJUCOLOR");
				vales[3]=rs.getString("CREATEDATE");
				vales[4]=rs.getString("DAOJUNUM");
				vales[5]=rs.getString("TOTALMONEY");
				vales[6]=rs.getString("GETTYPE");
				
				vales[7]=rs.getString("VIP");
				vales[8]=rs.getString("GUOJIA");
				
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
		if(logger.isDebugEnabled()){
			logger.debug("[查询道具汇总信息] [OK] [查询："+resultlist.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		}
		return resultlist;
	}

	/**
	 * 获取道具列表
	 */
	public ArrayList<String> getDaoju(java.util.Date date) {
        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String> daoJuList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select t.name daojuname  from dt_daojuname  t");
		
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String daoju=rs.getString("daojuname");
				if(!"".equals(daoju)&&daoju!=null){
					daoJuList.add(daoju);
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
		if(logger.isDebugEnabled()){
			logger.debug("[查询道具信息] [OK] [查询："+daoJuList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		}
		return daoJuList;
	}
	/**
	 * 获取货币种类
	 * @param date
	 * @return
	 */
	public ArrayList<String> getHuoBiType(java.util.Date date) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String> huoBiTypeList=new ArrayList();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement("select t.name huobitype  from dt_currencytype t where t.name is not null");
			
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				String huobitype=rs.getString("huobitype");
				if(huobitype!=null&&!"".equals(huobitype)){
					huoBiTypeList.add(huobitype);
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
		return huoBiTypeList;
	}
	
	/**
	 * 获取有颜色道具信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param huobitype
	 * @param gamelevel
	 * @param daojuname
	 *  @param gettype//道具获取类型，购买，使用，赠送等
	 * @return
	 */
	public List<String[]> getColorDaoJuXiaoHao(String startDateStr, String endDateStr, String fenQu, String huobitype,String gamelevel,String daojuname,String gettype,String username)
	{
		long now=System.currentTimeMillis();
		String subSql=" "; 
		 if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and g.fenqu in ('"+fenQu+"')";}
		 if(huobitype!=null&&!"".equals(huobitype)){subSql+=" and g.huobitype='"+huobitype+"'";}
		 if(gamelevel!=null&&!"".equals(gamelevel)){subSql+=" and g.gamelevel  in ("+gamelevel+")";}
		 if(daojuname!=null&&!"".equals(daojuname)){subSql+=" and g.daojuname='"+daojuname+"'";}
		 if(gettype!=null&&!"".equals(gettype)){    subSql+=" and g.gettype in ('"+gettype+"') ";}
		 if(username!=null&&!"".equals(username)){  subSql+=" and g.username='"+username+"' ";}
		String colorDaoJu=MultiLanguageManager.translateMap.get("colorDaoJu");
		String sql="select g.daojuname,g.daojucolor,count(distinct(g.username)) usercount,sum(g.daojunum) daojunum,sum(g.daojunum*g.danjia) money from stat_daoju g " +
				" where g.daojuname in ('"+colorDaoJu+"') " +
				"and trunc(g.createdate) between to_date('"+startDateStr+"','YYYY-MM-DD') and to_date('"+endDateStr+"', 'YYYY-MM-DD')"+subSql+
				"group by g.daojuname,g.daojucolor order by g.daojuname";
		
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
				vales[0]=rs.getString("daojuname");
				vales[1]=rs.getString("daojucolor");
				vales[2]=rs.getString("usercount");
				vales[3]=rs.getString("daojunum");
				vales[4]=rs.getString("money");
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
		if(logger.isDebugEnabled()){
			logger.debug("[查询获取有颜色道具信息] [OK] [查询："+resultlist.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		}
		return resultlist;
	}
	
	
	
	
	/**
	 * 获取道具消耗
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param huobitype
	 * @param gamelevel
	 * @param daojuname
	 *  @param gettype//道具获取类型，购买，使用，赠送等
	 * @return
	 */
	public List<String[]> getDaoJuXiaoHao(String startDateStr, String endDateStr, String fenQu, String huobitype,String gamelevel,String daojuname,String gettype,String username)
	{
		String subSql=" "; 
		 if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu in ('"+fenQu+"')";}
		 if(huobitype!=null&&!"".equals(huobitype)){subSql+=" and t.huobitype='"+huobitype+"'";}
		 if(gamelevel!=null&&!"".equals(gamelevel)){subSql+=" and t.gamelevel  in ("+gamelevel+")";}
		 if(daojuname!=null&&!"".equals(daojuname)){subSql+=" and n.name='"+daojuname+"'";}
		 if(gettype!=null&&!"".equals(gettype)){    subSql+=" and g.name in ('"+gettype+"') ";}
		 //if(username!=null&&!"".equals(username)){  subSql+=" and t.username='"+username+"' ";}
		 
		String sql="select n.name daojuname ,max(t.danjia) danjia,sum(t.daojunum) daojunum,sum(t.danjia *t.daojunum) zongjia" +
				" from stat_daoju t,dt_daojuname n,dt_gettype g " +
				" where t.createdate between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
				" and to_date('"+endDateStr+" 23:59:59', 'YYYY-MM-DD hh24:mi:ss') and t.gettype=g.id  and t.daojuname=n.id "+subSql+
				" group by n.name order by n.name";
		

		
		
	    return queryDaoJuGouMai(sql);	
	}
	/**
	 * 获取购买道具信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param huobitype
	 * @param gamelevel
	 * @param daojuname
	 *  @param gettype//道具获取类型，购买，使用，赠送等
	 * @return
	 */
	public List<String[]> getDaoJuGouMai(String startDateStr, String endDateStr, String fenQu, String huobitype,String gamelevel,String daojuname,String gettype,String username)
	{
		String subSql=" "; 
		 if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu in ('"+fenQu+"')";}
		 if(huobitype!=null&&!"".equals(huobitype)){subSql+=" and t.huobitype='"+huobitype+"'";}
		 if(gamelevel!=null&&!"".equals(gamelevel)){subSql+=" and t.gamelevel  in ("+gamelevel+")";}
		 if(daojuname!=null&&!"".equals(daojuname)){subSql+=" and d.name='"+daojuname+"'";}
		 if(gettype!=null&&!"".equals(gettype)){    subSql+=" and g.name in ('"+gettype+"') ";}
		 if(username!=null&&!"".equals(username)){  subSql+=" and t.username='"+username+"' ";}
		 
//		String sql="select d.name daojuname,t.danjia danjia,sum(t.daojunum) daojunum, sum(t.danjia *t.daojunum) zongjia" +
//				" from stat_daoju t,dt_daojuname d,dt_gettype g" +
//				" where t.createdate  between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
//				" and to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') and t.daojuname=d.id"+subSql+
//				" group by d.name,t.danjia order by d.name";
		
		
		String sql="select d.name daojuname,'11' danjia,sum(t.daojunum) daojunum, sum(t.danjia) zongjia" +
		" from stat_daoju t,dt_daojuname d,dt_gettype g" +
		" where t.createdate  between to_date('"+startDateStr+" 00:00:00','YYYY-MM-DD hh24:mi:ss') " +
		" and to_date('"+endDateStr+" 23:59:59','YYYY-MM-DD hh24:mi:ss') and t.daojuname=d.id"+subSql+
		" group by d.name order by d.name";


		
		
	    return queryDaoJuGouMai(sql);	
	}
	public ArrayList<String[]>  queryDaoJuGouMai(String sql) {
        long now =System.currentTimeMillis();
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
				vales[0]=rs.getString("daojuname");
				vales[1]=rs.getString("danjia");
				vales[2]=rs.getString("daojunum");
				vales[3]=rs.getString("zongjia");
				
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
		
		if(logger.isDebugEnabled()){
			logger.debug("[查询道具购买信息] [OK] [查询："+resultlist.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		}
		return resultlist;
	}
	/**
	 * 获取消耗分布信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param huobitype
	 * @param gamelevel
	 * @param daojuname
	 * @param gettype
	 * @return
	 */
	public ArrayList<String[]> getDaoJuXiaoHaoFenBu(String startDateStr, String endDateStr, String fenQu, String huobitype,String gamelevel,String daojuname,String gettype)
	{
		String subSql=" "; 
		 if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu='"+fenQu+"'";}
		 if(huobitype!=null&&!"".equals(huobitype)){subSql+=" and t.huobitype='"+huobitype+"'";}
		 if(gamelevel!=null&&!"".equals(gamelevel)){subSql+=" and t.gamelevel  in ("+gamelevel+")";}
		 if(daojuname!=null&&!"".equals(daojuname)){subSql+=" and t.daojuname='"+daojuname+"'";}
		 if(gettype!=null&&!"".equals(gettype)){subSql+=" and t.gettype in ('"+gettype+"') ";}
		
		String oldSql=" select    yaobao.gamelevel,yaobao.yuanbao,user1.usernum from" +
				"  (select tt.gamelevel,sum(tt.yuanbao) yuanbao from " +
				"( select t.gamelevel gamelevel,t.daojuname,sum(t.daojunum)*avg(t.danjia) yuanbao" +
				" from stat_daoju t " +
				"where 1 = 1 " +subSql+
				"and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and to_date('"+endDateStr+"', 'YYYY-MM-DD') "+
		         "group by t.gamelevel,t.daojuname" +
		    		")tt group by tt.gamelevel) yaobao" +
		    		" left join" +
		    		" (select t.gamelevel gamelevel,count(distinct(t.username)) usernum" +
		    		" from stat_daoju t " +
		    		" where 1 = 1 " +subSql+
		    		" and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and " +
		    		" to_date('"+endDateStr+"', 'YYYY-MM-DD') " +
		    		" group by t.gamelevel) user1" +
		    		"  on yaobao.gamelevel=user1.gamelevel";
		    
		    
		    String newSql=
		 " select    yaobao.gamelevel,yaobao.yuanbao,user1.usernum from" +
		 " (select tt.gamelevel,sum(tt.yuanbao) yuanbao from " +
		 "( select t.gamelevel gamelevel,t.daojuname,sum(t.daojunum)*avg(t.danjia) yuanbao" +
		 " from stat_daoju t " +
		 " where 1 = 1 " +subSql+
		 " and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and to_date('"+endDateStr+"', 'YYYY-MM-DD') " +
		 " and t.username not in (" +
		 " select distinct tn.username " +
		 " from stat_daoju tn  where 1 = 1" +subSql+
		 " and trunc(tn.createdate)<to_date('"+startDateStr+"', 'YYYY-MM-DD')"+
		 "   )" +
		 		" group by t.gamelevel,t.daojuname" +
		 		" )tt group by tt.gamelevel) yaobao" +
		 		" left join" +
		 		" (select t.gamelevel gamelevel,count(distinct(t.username)) usernum" +
		 		" from stat_daoju t where 1 = 1 " +subSql+
		 		" and trunc(t.createdate) between to_date('"+startDateStr+"', 'YYYY-MM-DD') and to_date('"+endDateStr+"', 'YYYY-MM-DD') " +
		 		" and t.username not in (" +
		 		" select distinct tn.username " +
		 		" from stat_daoju tn  " +
		 		" where 1 = 1" +subSql+
		 		" and trunc(tn.createdate)<to_date('"+startDateStr+"', 'YYYY-MM-DD')" +
		 		" )" +
		 		" group by t.gamelevel) user1" +
		 		" on yaobao.gamelevel=user1.gamelevel";
		 
		 
		 String sql="select old.gamelevel,old.yuanbao oyuanbao,old.usernum ousernum,new.yuanbao,new.usernum" +
		 		" from ("+oldSql+") old left join ("+newSql+") new on old.gamelevel=new.gamelevel";
		 return queryDaoJuXiaoHaoFenBu(sql);
	}
	private ArrayList<String[]> queryDaoJuXiaoHaoFenBu(String sql) {
		long now =System.currentTimeMillis();
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
				vales[0]=rs.getString("gamelevel");
				vales[1]=rs.getString("ousernum");
				vales[2]=rs.getString("oyuanbao");
				vales[3]=rs.getString("usernum");
				vales[4]=rs.getString("yuanbao");
				
				
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
		if(logger.isDebugEnabled()){
			logger.debug("[查询道具消耗分布信息] [OK] [查询："+resultlist.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		}
		return resultlist;
	}
	
	   //国服
	String 商城道具="宝石竹清(1级)','宝石枳香(1级)','宝石幽橘(1级)','宝石湛天(1级)','宝石墨轮(1级)','宝石炎焚(1级)','宝石混沌(1级)','宝石无相(1级)','宝石魔渊(1级)','宝石竹清(2级)','宝石枳香(2级)','宝石幽橘(2级)','宝石湛天(2级)','宝石墨轮(2级)','宝石炎焚(2级)','宝石混沌(2级)','宝石无相(2级)','宝石魔渊(2级)','宝石竹清(3级)','宝石枳香(3级)','宝石幽橘(3级)','宝石湛天(3级)','宝石墨轮(3级)','宝石炎焚(3级)','宝石混沌(3级)','宝石无相(3级)','宝石魔渊(3级)','宝石竹清(4级)','宝石枳香(4级)','宝石幽橘(4级)','宝石湛天(4级)','宝石墨轮(4级)','宝石炎焚(4级)','宝石混沌(4级)','宝石无相(4级)','宝石魔渊(4级)','宝石竹清(5级)','宝石枳香(5级)','宝石幽橘(5级)','宝石湛天(5级)','宝石墨轮(5级)','宝石炎焚(5级)','宝石混沌(5级)','宝石无相(5级)','宝石魔渊(5级)','宝石竹清(6级)','宝石枳香(6级)','宝石幽橘(6级)','宝石湛天(6级)','宝石墨轮(6级)','宝石炎焚(6级)','宝石混沌(6级)','宝石无相(6级)','宝石魔渊(6级)','宝石竹清(7级)','宝石枳香(7级)','宝石幽橘(7级)','宝石湛天(7级)','宝石墨轮(7级)','宝石炎焚(7级)','宝石混沌(7级)','宝石无相(7级)','宝石魔渊(7级)','宝石竹清(8级)','宝石枳香(8级)','宝石幽橘(8级)','宝石湛天(8级)','宝石墨轮(8级)','宝石炎焚(8级)','宝石混沌(8级)','宝石无相(8级)','宝石魔渊(8级)','宝石竹清(9级)','宝石枳香(9级)','宝石幽橘(9级)','宝石湛天(9级)','宝石墨轮(9级)','宝石炎焚(9级)','宝石混沌(9级)','宝石无相(9级)','宝石魔渊(9级)','鉴定符','铭刻符','炼星符','灵兽内丹','炼妖石";
	String 银子类道具="碎银子','银两','银块','银条','银锭','银砖','普通红包','白银红包','黄金红包','白金红包','钻石红包','镖银(白)','镖银(绿)','镖银(蓝)','镖银(紫)','镖银(橙)','镖金(白)','镖金(绿)','镖金(蓝)','镖金(紫)','镖金(橙)','镖玉(白)','镖玉(绿)','镖玉(蓝)','镖玉(紫)','镖玉(橙)";
	String 宠物蛋="地狱狼王蛋','青丘妖狐蛋','绝世花妖蛋','霸刀魔王蛋','黑风熊精蛋','金甲游神蛋','擎天牛神蛋','六耳猕猴蛋','麒麟圣祖蛋','白虎战神蛋','青龙斗神蛋','玄武水神蛋','朱雀火神蛋','九阴魔蝎蛋','青翼蝠王蛋','乱舞蝶妖蛋','罗刹鬼王蛋','嗜血剑魂蛋','千年参妖蛋','雷霆天尊蛋','大龙人蛋";
	String 飞行坐骑="爱的炫舞','浪漫今生','碧虚青鸾','八卦仙蒲','梦瞳仙鹤','乾坤葫芦','金极龙皇','焚焰火扇','深渊魔章','碧虚青鸾(1天)','碧虚青鸾(7天)','碧虚青鸾(体验)";
	String 重要碎片="青鸾翎羽','朱雀火羽','战火残片','才子印记','武圣印记','武圣印记碎片','青龙精魂','朱雀精魂','白虎精魂','玄武精魂','麒麟精魂";
	String 万灵榜装备="装备名称','猛志固常在','五岳倒为轻','日曜贪狼盔','兽面吞火铠','血魂肩铠','紫铖护手','紫铖战靴','紫铖腰带','天启戒','王天古玉','太极图','天地为一朝','蚩尤瀑布碎','火曜破军盔','魔麟饕餮铠','皇鳞肩铠','遁龙护手','遁龙战靴','遁龙腰带','暝天戒','辟邪石玉','破天图','彩凤双飞翼','风萧易水寒','东极炫金罩','东极炫金衫','东极炫金肩','东极炫金手','东极炫金鞋','赤血','噬灵戒','浮世印','转空','明月照九州','承影无形杀','青冥海狱罩','青冥海狱衫','青冥海狱肩','青冥海狱手','天妖灭魂鞋','残雪','刺尨戒','苍天印','玄曦','点睛龙破壁','玄天斩灵洛','小北炎极头','小北炎极袄','小北炎极垫肩','离火手套','离火轻履','离火坠饰','火熔晶','火熔项链','逆央镜','泼墨碧丹青','血晶摩诃惑','海水江崖头','海水江崖袄','海水江崖垫肩','玄冰手套','玄冰轻履','玄冰坠饰','婆罗戒','婆罗项链','玄冰境','一岁一枯荣','本来无一物','乾坤地理蛇兜','乾坤地理裙','千幻蛛巾','千幻蝎腕','千幻道履','千幻腰带','封魔戒','落魂钟','毒王鼎','凤歌楚狂人','醉古堂剑扫','九黎太虚蛇兜','九黎太虚衫','蛟龙蛛巾','蛟龙蝎腕','蛟龙木履','蛟龙腰带','万魔戒','坤木蛊','星罗盘','朱雀斧','武圣禅杖','战火甲胄','朱雀刺','武圣轮','战火衫','朱雀笔','武圣剑','战火袄','朱雀幡','武圣杖','战火蟒衣";
	String 酒="杏花村','屠苏酒','妙沁药酒";
	String 屠魔帖="屠魔帖●降魔','屠魔帖●逍遥','屠魔帖●霸者','屠魔帖●朱雀','屠魔帖●水晶','屠魔帖●倚天','屠魔帖●青虹','屠魔帖●赤霄','屠魔帖●震天','屠魔帖●天罡";
	String 古董="古董";
	
//	//台服
//	String 商城道具="寶石無相(1級)','寶石無相(2級)','寶石無相(3級)','寶石無相(4級)','寶石無相(5級)','寶石無相(6級)','寶石無相(7級)','寶石無相(8級)','寶石無相(9級)','寶石混沌(1級)','寶石混沌(2級)','寶石混沌(3級)','寶石混沌(4級)','寶石混沌(5級)','寶石混沌(6級)','寶石混沌(7級)','寶石混沌(8級)','寶石混沌(9級)','寶石魔淵(1級)','寶石魔淵(2級)','寶石魔淵(3級)','寶石魔淵(4級)','寶石魔淵(5級)','寶石魔淵(6級)','寶石魔淵(7級)','寶石魔淵(8級)','寶石魔淵(9級)','寶石墨輪(1級)','寶石墨輪(2級)','寶石墨輪(3級)','寶石墨輪(4級)','寶石墨輪(5級)','寶石墨輪(6級)','寶石墨輪(7級)','寶石墨輪(8級)','寶石墨輪(9級)','寶石炎焚(1級)','寶石炎焚(2級)','寶石炎焚(3級)','寶石炎焚(4級)','寶石炎焚(5級)','寶石炎焚(6級)','寶石炎焚(7級)','寶石炎焚(8級)','寶石幽橘(1級)','寶石幽橘(2級)','寶石幽橘(3級)','寶石幽橘(4級)','寶石幽橘(5級)','寶石幽橘(6級)','寶石幽橘(7級)','寶石幽橘(8級)','寶石幽橘(9級)','寶石湛天(1級)','寶石湛天(2級)','寶石湛天(3級)','寶石湛天(4級)','寶石湛天(5級)','寶石湛天(6級)','寶石湛天(7級)','寶石湛天(8級)','寶石湛天(9級)','寶石竹清(1級)','寶石竹清(2級)','寶石竹清(3級)','寶石竹清(4級)','寶石竹清(5級)','寶石竹清(6級)','寶石竹清(7級)','寶石竹清(8級)','寶石竹清(9級)','寶石枳香(1級)','寶石枳香(2級)','寶石枳香(3級)','寶石枳香(4級)','寶石枳香(5級)','寶石枳香(6級)','寶石枳香(7級)','寶石枳香(8級)','寶石枳香(9級)','鑒定符','炼妖石','灵兽内丹','銘刻符','煉星符";
//	String 银子类道具="碎银子','银砖','銀兩','銀塊','銀條','銀錠','普通紅包','白銀紅包','黃金紅包','白金紅包','鑽石紅包','鏢玉(紫)','鏢玉(綠)','鏢玉(藍)','鏢玉(橙)','鏢玉(白)','鏢銀(紫)','鏢銀(綠)','鏢銀(藍)','鏢銀(橙)','鏢銀(白)','鏢金(紫)','鏢金(綠)','鏢金(藍)','鏢金(橙)','鏢金(白)";
//	String 宠物蛋="朱雀火神蛋','玄武水神蛋','玄瞳金晶蛋','嗜血劍魂蛋','擎天牛神蛋','青翼蝠王蛋','青丘妖狐蛋','青龍鬥神蛋','千年參妖蛋','麒麟聖祖蛋','蠻荒女皇蛋','羅刹鬼王蛋','亂舞蝶妖蛋','六耳獼猴蛋','雷霆天尊蛋','絕世花妖蛋','九陰魔蠍蛋','旌星角瑞蛋','金甲遊神蛋','黑風熊精蛋','地獄狼王蛋','白虎戰神蛋','霸刀魔王蛋";
//	String 飞行坐骑="愛的炫舞','浪漫今生','碧虛青鸞','八卦仙蒲','夢瞳仙鶴','乾坤葫蘆','金極龍皇','焚焰火扇','深淵魔章','碧虛青鸞','碧虛青鸞(7天)";
//	String 重要碎片="青鸞翎羽','朱雀火羽','戰火殘片','才子印記','武聖印記','武聖印記碎片','青龍精魂','朱雀精魂','白虎精魂','玄武精魂','麒麟精魂";
//	String 万灵榜装备="裝備名稱','猛志固常在','五嶽倒為輕','日曜貪狼盔','獸面吞火鎧','血魂肩鎧','紫鋮護手','紫鋮戰靴','紫鋮腰帶','天啟戒','王天古玉','太極圖','天地為一朝','蚩尤瀑布碎','火曜破軍盔','魔麟饕餮鎧','皇鱗肩鎧','遁龍護手','遁龍戰靴','遁龍腰帶','暝天戒','辟邪石玉','破天圖','彩鳳雙飛翼','風蕭易水寒','東極炫金罩','東極炫金衫','東極炫金肩','東極炫金手','東極炫金鞋','赤血','噬靈戒','浮世印','轉空','明月照九州','承影無形殺','青冥海獄罩','青冥海獄衫','青冥海獄肩','青冥海獄手','天妖滅魂鞋','殘雪','刺尨戒','蒼天印','玄曦','點睛龍破壁','玄天斬靈洛','小北炎極頭','小北炎極襖','小北炎極墊肩','離火手套','離火輕履','離火墜飾','火熔晶','火熔項鏈','逆央鏡','潑墨碧丹青','血晶摩訶惑','海水江崖頭','海水江崖襖','海水江崖墊肩','玄冰手套','玄冰輕履','玄冰墜飾','婆羅戒','婆羅項鏈','玄冰境','一歲一枯榮','本來無一物','乾坤地理蛇兜','乾坤地理裙','千幻蛛巾','千幻蠍腕','千幻道履','千幻腰帶','封魔戒','落魂鐘','毒王鼎','鳳歌楚狂人','醉古堂劍掃','九黎太虛蛇兜','九黎太虛衫','蛟龍蛛巾','蛟龍蠍腕','蛟龍木履','蛟龍腰帶','萬魔戒','坤木蠱','星羅盤','朱雀斧','武聖禪杖','戰火甲胄','朱雀刺','武聖輪','戰火衫','朱雀筆','武聖劍','戰火襖','朱雀幡','武聖杖','戰火蟒衣";
//	String 酒="杏花村','屠蘇酒','妙沁藥酒";
//	String 屠魔帖="屠魔帖●霸者','屠魔帖●赤霄','屠魔帖●降魔','屠魔帖●青虹','屠魔帖●水晶','屠魔帖●天罡','屠魔帖●倚天','屠魔帖●震天','屠魔帖●朱雀','屠魔帖●逍遙";
//	String 古董="古董";
	
	@Override
	public ArrayList<String[]> getSensitiveDaoJu(String startDateStr, String endDateStr, String fenQu, String jixing, String daoJuType) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		
        String subSql="";
        if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu='"+fenQu+"'";}
        if(jixing!=null&&!"".equals(jixing)){subSql+=" and t.jixing='"+jixing+"'";}
        
        if(daoJuType!=null&&"商城道具".equals(daoJuType)){subSql+=" and n.name in ('"+MultiLanguageManager.translateMap.get("商城道具")+"')";  }else
        if(daoJuType!=null&&"银子类道具".equals(daoJuType)){subSql+=" and n.name in ('"+MultiLanguageManager.translateMap.get("银子类道具")+"')";  }else
        if(daoJuType!=null&&"宠物蛋".equals(daoJuType)){subSql+=" and n.name in ('"+MultiLanguageManager.translateMap.get("宠物蛋")        +"')";}else
        if(daoJuType!=null&&"飞行坐骑".equals(daoJuType)){subSql+=" and n.name in ('"+MultiLanguageManager.translateMap.get("飞行坐骑")+"')";}else
        if(daoJuType!=null&&"重要碎片".equals(daoJuType)){subSql+=" and n.name in ('"+MultiLanguageManager.translateMap.get("重要碎片")+"')";}else
        
        if(daoJuType!=null&&"万灵榜装备".equals(daoJuType)){subSql+=" and n.name in ('"+MultiLanguageManager.translateMap.get("万灵榜装备")+"')";}else
        if(daoJuType!=null&&"酒".equals(daoJuType)){subSql+=" and n.name in ('"+MultiLanguageManager.translateMap.get("酒")+"')";}else
        if(daoJuType!=null&&"屠魔帖".equals(daoJuType)){subSql+=" and n.name in ('"+MultiLanguageManager.translateMap.get("屠魔帖")+"')";}
        else {subSql+=" and n.name in ('"+MultiLanguageManager.translateMap.get("古董")+"')";}
        
        String get=MultiLanguageManager.translateMap.get("get");
        String lost=MultiLanguageManager.translateMap.get("lost");
        
//        //国服   
//		String sql="select n.name daojuname,sum(case when  t.gettype in ('任务','怪物掉落拾取','合成获得','礼包获得','祈福获得','开瓶子获得','商店购买','宠物繁殖获得','职业礼包获得','结婚坐骑','运镖','参加家族运镖获取','宠物封印获得宠物蛋') then  t.daojunum  else 0 end ) get," +
//				" sum(case when  g.name in ('使用持续性道具','商店出售删除','强化删除','开瓶子删除','使用道具删除','丢弃','合成删除','铭刻删除','鉴定删除','死亡掉落','到期删除 ','宠物封印删除宠物')  then  t.daojunum  else 0 end ) lost " +
//				" from stat_daoju_deshi t ,dt_gettype g,dt_daojuname n where  t.createdate between '"+startDateStr+"' and '"+endDateStr+"'"+subSql+
//              " and g.id=t.gettype and n.id=t.daojuname"+
//				" group by n.name";
		
		
		
//	   // 台服
//			String sql="select n.name daojuname,sum(case when  g.name in ('任務','怪物掉落拾取','合成獲得','禮包獲得','祈福獲得','開瓶子獲得','商店購買','寵物繁殖獲得','職業禮包獲得','結婚坐騎','運鏢','參加家族運鏢獲取','寵物封印獲得寵物蛋') then  t.daojunum  else 0 end ) get," +
//					" sum(case when  g.name in ('使用持續性道具','商店出售刪除','強化刪除','開瓶子刪除','使用道具刪除','丟棄','合成刪除','銘刻刪除','鑒定刪除','死亡掉落','到期刪除 ','寵物封印刪除寵物')  then  t.daojunum  else 0 end ) lost " +
//					" from stat_daoju_deshi t,dt_gettype g,dt_daojuname n where t.createdate between '"+startDateStr+"' and '"+endDateStr+"' " +subSql+
//					" and g.id=t.gettype and n.id=t.daojuname " +
//					" group by n.name";
		
			String sql="select n.name daojuname,sum(case when  g.name in ('"+get+"') then  t.daojunum  else 0 end ) get," +
			" sum(case when  g.name in ('"+lost+"')  then  t.daojunum  else 0 end ) lost " +
			" from stat_daoju_deshi t,dt_gettype g,dt_daojuname n where t.createdate between '"+startDateStr+"' and '"+endDateStr+"' " +subSql+
			" and g.id=t.gettype and n.id=t.daojuname " +
			" group by n.name";
		
		
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[3];
				vales[0]=rs.getString("daojuname");
				vales[1]=rs.getString("get");
				vales[2]=rs.getString("lost");
				
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
	
	public ArrayList<String[]> getSensitiveKaiPingHuoDe(String startDateStr, String endDateStr, String fenQu, String jixing, String getType) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
        String subSql="";
        if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu='"+fenQu+"'";}
        if(jixing!=null&&!"".equals(jixing)){subSql+=" and t.jixing='"+jixing+"'";}
        
        String 开瓶子获得=MultiLanguageManager.translateMap.get("开瓶子获得");
        
        String sql=" select n.name daojuname,sum(t.daojunum) count from stat_daoju_deshi t,dt_gettype g,dt_daojuname n " +
        		" where t.createdate between '"+startDateStr+"' and '"+endDateStr+"' " +
        		" and g.name like '%"+开瓶子获得+"%' and t.gettype=g.id and t.daojuname=n.id group by  n.name" ;
        
        
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[3];
				vales[0]=rs.getString("daojuname");
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
	public ArrayList<String[]> getSensitiveDaoJuReasonType(String startDateStr, String endDateStr, 
			String fenQu, String jixing, String daoJuType) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		
        String subSql="";
        if(fenQu!=null&&!"".equals(fenQu)){subSql+=" and t.fenqu='"+fenQu+"'";}
        if(jixing!=null&&!"".equals(jixing)){subSql+=" and t.jixing='"+jixing+"'";}
        if(daoJuType!=null&&!"".equals(daoJuType)){subSql+=" and n.name='"+daoJuType+"'";}
        String get=MultiLanguageManager.translateMap.get("get");   //'任务','怪物掉落拾取','合成获得','礼包获得','祈福获得','开瓶子获得','商店购买','宠物繁殖获得','职业礼包获得','结婚坐骑','运镖','参加家族运镖获取','宠物封印获得宠物蛋'
        String lost=MultiLanguageManager.translateMap.get("lost"); //'使用持续性道具','商店出售删除','强化删除','开瓶子删除','使用道具删除','丢弃','合成删除','铭刻删除','鉴定删除','死亡掉落','到期删除 ','宠物封印删除宠物'
        
		String sql="select   g.name gettype,sum(case when  g.name in ('"+get+"') then  t.daojunum  else 0 end ) get," +
				" sum(case when  g.name in ('"+lost+"')  then  t.daojunum  else 0 end ) lost " +
				" from stat_daoju_deshi t,dt_gettype g,dt_daojuname n " +
				" where t.createdate between '"+startDateStr+"' and '"+endDateStr+"'"+subSql+"" +
						" group by g.name";
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[3];
				vales[0]=rs.getString("gettype");
				vales[1]=rs.getString("get");
				vales[2]=rs.getString("lost");
				
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
	public ArrayList<String[]> getGoDong_YinKuai(String startDateStr, String endDateStr, String fenQu) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		String subSql="";
		if(fenQu!=null){ subSql=fenQu;}
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			
			String sql="select gudong.createdate,gudong.gudongnum,gudongdiushi.gudongdiushinum,yinkuai.yinkuainum,yinkuaiduishi.yinkuaiduishinum from " +
					" ( select  t.createdate ,sum(t.daojunum) gudongnum from stat_daoju_deshi t,dt_daojuname n,dt_gettype g  " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"'" +
					" and  n.name='古董' and (g.name='任务' or g.name='提取邮件，斩妖降魔' or g.name='参加家族运镖获取') and t.daojucolor ='绿色' " +subSql+
					" group by  t.createdate ) gudong " +
					" left join ( " +
					" select  t.createdate ,sum(t.daojunum) gudongdiushinum from stat_daoju_deshi t,dt_daojuname n,dt_gettype g  " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"'" +
					" and  n.name='古董' and g.name='开瓶子删除' and t.daojucolor ='绿色' " +subSql+
					" group by  t.createdate ) gudongdiushi on gudong.createdate=gudongdiushi.createdate " +
					" left join ( " +
					" select t.createdate,sum(t.daojunum) yinkuainum from stat_daoju_deshi t ,dt_daojuname n,dt_gettype g  " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"'" +
					" and g.name='开瓶子获得' and n.name='银块' and t.daojucolor='白色' " +subSql+
					" group by t.createdate ) yinkuai  on gudong.createdate=yinkuai.createdate " +
					" left join ( " +
					" select t.createdate,sum(t.daojunum) yinkuaiduishinum from stat_daoju_deshi t ,dt_daojuname n,dt_gettype g  " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"'" +
					"and g.name='使用道具删除' and n.name='银块' and t.daojucolor='白色'" +subSql+
					" group by t.createdate ) yinkuaiduishi " +
					" on gudong.createdate=yinkuaiduishi.createdate order by gudong.createdate";

			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [5];
				
				String createdate=rs.getString("createdate");
				String gudongnum=rs.getString("gudongnum");
				String gudongdiushinum=rs.getString("gudongdiushinum");
				String yinkuainum=rs.getString("yinkuainum");
				String yinkuaiduishinum=rs.getString("yinkuaiduishinum");
				
				strs[0]=createdate;
				strs[1]=gudongnum;
				strs[2]=gudongdiushinum;
				strs[3]=yinkuainum;
				strs[4]=yinkuaiduishinum;
				
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
	public ArrayList<String[]> getGoDong_YinKuai_fenqu(String startDateStr, String endDateStr, String fenQu) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		String subSql="";
		if(fenQu!=null){ subSql=fenQu;}
		
		try {
			con = dataSourceManager.getInstance().getConnection();
			
			String sql="select gudong.fenqu,gudong.gudongnum,gudongdiushi.gudongdiushinum,yinkuai.yinkuainum,yinkuaiduishi.yinkuaiduishinum from " +
					" ( select  t.fenqu ,sum(t.daojunum) gudongnum from stat_daoju_deshi t,dt_daojuname n,dt_gettype g  " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"'" +
					" and  n.name='古董' and (g.name='任务' or g.name='提取邮件，斩妖降魔' or g.name='参加家族运镖获取') and t.daojucolor ='绿色' " +subSql+
					" group by  t.fenqu ) gudong " +
					" left join ( " +
					" select  t.fenqu ,sum(t.daojunum) gudongdiushinum from stat_daoju_deshi t,dt_daojuname n,dt_gettype g  " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"'" +
					" and  n.name='古董' and g.name='开瓶子删除' and t.daojucolor ='绿色' " +subSql+
					" group by  t.fenqu ) gudongdiushi on gudong.fenqu=gudongdiushi.fenqu " +
					" left join ( " +
					" select t.fenqu,sum(t.daojunum) yinkuainum from stat_daoju_deshi t ,dt_daojuname n,dt_gettype g  " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"'" +
					" and g.name='开瓶子获得' and n.name='银块' and t.daojucolor='白色' " +subSql+
					" group by t.fenqu ) yinkuai  on gudong.fenqu=yinkuai.fenqu " +
					" left join ( " +
					" select t.fenqu,sum(t.daojunum) yinkuaiduishinum from stat_daoju_deshi t ,dt_daojuname n,dt_gettype g  " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"'" +
					"and g.name='使用道具删除' and n.name='银块' and t.daojucolor='白色'" +subSql+
					" group by t.fenqu ) yinkuaiduishi " +
					" on gudong.fenqu=yinkuaiduishi.fenqu order by gudong.fenqu";

			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [5];
				
				String fenqu=rs.getString("fenqu");
				String gudongnum=rs.getString("gudongnum");
				String gudongdiushinum=rs.getString("gudongdiushinum");
				String yinkuainum=rs.getString("yinkuainum");
				String yinkuaiduishinum=rs.getString("yinkuaiduishinum");
				
				strs[0]=fenqu;
				strs[1]=gudongnum;
				strs[2]=gudongdiushinum;
				strs[3]=yinkuainum;
				strs[4]=yinkuaiduishinum;
				
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
	public ArrayList<String[]> getGoDong_DiuShi(String startDateStr, String endDateStr, String fenQu) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		String subSql="";
		if(fenQu!=null){ subSql=fenQu;}
		try {
			con = dataSourceManager.getInstance().getConnection();
			String sql=" select  t.createdate datestr, t.fenqu,sum(t.daojunum) total from stat_daoju_deshi t,dt_daojuname n,dt_gettype g " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"' " +
					" and n.name='古董' and g.name='开瓶子删除' and t.daojucolor ='绿色' " +subSql+
					" group by  t.createdate, t.fenqu ";

			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [3];
				
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
	public ArrayList<String[]> getGoDong_HuoDe(String startDateStr, String endDateStr, String fenQu) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		String subSql="";
		if(fenQu!=null){ subSql=fenQu;}
		try {
			con = dataSourceManager.getInstance().getConnection();
			String sql=" select  t.createdate datestr, t.fenqu,sum(t.daojunum) total from stat_daoju_deshi t,dt_daojuname n,dt_gettype g " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"' " +
					" and  n.name='古董' and (g.name='任务' or g.name='提取邮件，斩妖降魔' or g.name='参加家族运镖获取') and t.daojucolor ='绿色' " +subSql+
					" group by  t.createdate, t.fenqu ";

			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [3];
				
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
	public ArrayList<String[]> getYinKuai_DiuShi(String startDateStr, String endDateStr, String fenQu) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		String subSql="";
		if(fenQu!=null){ subSql=fenQu;}
		try {
			con = dataSourceManager.getInstance().getConnection();
			String sql=" select  t.createdate datestr, t.fenqu,sum(t.daojunum) total from stat_daoju_deshi t,dt_daojuname n,dt_gettype g " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"' " +
					" and g.name='使用道具删除' and n.name='银块' and t.daojucolor='白色' " +subSql+
					" group by  t.createdate, t.fenqu ";

			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [3];
				
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
	public ArrayList<String[]> getYinKuai_HuoDe(String startDateStr, String endDateStr, String fenQu) {

	

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		String subSql="";
		if(fenQu!=null){ subSql=fenQu;}
		try {
			con = dataSourceManager.getInstance().getConnection();
			String sql=" select  t.createdate datestr, t.fenqu,sum(t.daojunum) total from stat_daoju_deshi t,dt_daojuname n,dt_gettype g " +
					" where to_char(t.daojuname)=to_char(n.id) and to_char(t.gettype)=to_char(g.id) and t.createdate between '"+startDateStr+"' and '"+endDateStr+"' " +
					" and g.name='开瓶子获得' and n.name='银块' and t.daojucolor='白色' " +subSql+
					" group by  t.createdate, t.fenqu ";

			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [3];
				
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
	public ArrayList<String[]> getDaoJu_MoHu(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [4];
				
				String fengyin=rs.getString("fengyin");
				String count=rs.getString("count");
				String money=rs.getString("money");
				String avg=rs.getString("avg");
				
				
				strs[0]=fengyin;
				strs[1]=count;
				strs[2]=money;
				strs[3]=avg;
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
	public ArrayList<String[]> getJiFeninfo(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String []> ls=new ArrayList<String []>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				String [] strs=new String [5];
				
				String fengyin=rs.getString("fengyin");
				String count=rs.getString("count");
				String money=rs.getString("money");
				String avg=rs.getString("avg");
				String kucun=rs.getString("kucun");
				
				strs[0]=fengyin;
				strs[1]=count;
				strs[2]=money;
				strs[3]=avg;
				strs[4]=kucun;
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
