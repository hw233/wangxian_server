package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.YinZiKuCunDao;
import com.sqage.stat.commonstat.entity.YinZiKuCun;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class YinZiKuCunDaoImpl implements YinZiKuCunDao {
	static Logger logger = Logger.getLogger(YinZiKuCunDaoImpl.class);
	DataSourceManager dataSourceManager;

	@Override
	public ArrayList<YinZiKuCun> getBySql(String sql) {
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<YinZiKuCun> ls=new ArrayList<YinZiKuCun>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			
			while(rs.next()) {
				YinZiKuCun yinZiKuCun = new YinZiKuCun();
				yinZiKuCun.setId(rs.getLong("ID"));
				yinZiKuCun.setColumn1(rs.getString("COLUMN1"));
				yinZiKuCun.setColumn2(rs.getString("COLUMN2"));
				
				yinZiKuCun.setColumn3(rs.getString("COLUMN3"));
				yinZiKuCun.setColumn4(rs.getString("COLUMN4"));
				yinZiKuCun.setColumn5(rs.getString("COLUMN5"));
				
				yinZiKuCun.setCount(rs.getLong("COUNT"));
				yinZiKuCun.setCreateDate(rs.getDate("CREATEDATE"));
				yinZiKuCun.setFenQu(rs.getString("FENQU"));
				
				ls.add(yinZiKuCun);
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
		return ls;
	}

	@Override
	public void addList(ArrayList<YinZiKuCun> yinZiKuCunList) {

        Long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		if(logger.isDebugEnabled()){logger.debug("开始批量插入数据库银子库存"+yinZiKuCunList.size()+"条");}
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_YINZIKUCUN(" +
					"ID," +
					"CREATEDATE," +
					"count," +
					"FENQU," +
					"column1," +
					"column2," +
					
					"column3," +
					"column4," +
					"column5" +
					
					") values (SEQ_STAT_YINZIKUCUN.NEXTVAL,?,?, ?,?,? ,?,?,?)");

		    int i = -1;
			int count=0;
		for(YinZiKuCun yinZiKuCun : yinZiKuCunList){
			if(yinZiKuCun.getCreateDate()!=null){
			ps.setTimestamp(1, new Timestamp(yinZiKuCun.getCreateDate().getTime()));}
			else{ ps.setTimestamp(1,null);	}
			ps.setLong(2, yinZiKuCun.getCount());
			ps.setString(3, yinZiKuCun.getFenQu());
			ps.setString(4, yinZiKuCun.getColumn1());
			ps.setString(5, yinZiKuCun.getColumn2());
			
			ps.setString(6, yinZiKuCun.getColumn3());
			ps.setString(7, yinZiKuCun.getColumn4());
			ps.setString(8, yinZiKuCun.getColumn5());
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%100==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			logger.error("插入银子库存数据库异常",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[添加银子库存信息回滚异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[添加银子库存信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
		} finally {
			try {
				ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

}
