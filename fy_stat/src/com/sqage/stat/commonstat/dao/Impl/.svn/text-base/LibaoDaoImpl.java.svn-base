package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.LibaoDao;
import com.sqage.stat.commonstat.entity.Libao;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class LibaoDaoImpl implements LibaoDao {
	static Logger logger = Logger.getLogger(LibaoDaoImpl.class);
	DataSourceManager dataSourceManager;
	
	@Override
	public boolean addList(ArrayList<Libao> libaoList) {
		
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		boolean result=true;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_LIBAO(" +
					"ID," +
					"CREATEDATE," +
					"DAOJUNAME," +
					
					"COUNT," +
					"DANJIA," +
					"TYPE," +
					
					"FENQU," +
					"COLUMN1," +
					"COLUMN2" +
					
					") values (SEQ_STAT_LIBAO_ID.NEXTVAL,?,?, ?,?,?, ?,?,?)");
			
			
			for(Libao libao:libaoList){
				if (libao.getCreateDate() != null) {
					ps.setTimestamp(1, new Timestamp(libao.getCreateDate().getTime()));
				} else {
					ps.setTimestamp(1, null);
				}
		
			ps.setString(2, libao.getDaoJuName());
			ps.setLong(3, libao.getCount());
			ps.setLong(4, libao.getDanjia());
			ps.setInt(5, libao.getType());
			ps.setString(6, libao.getFenQu());
			
			ps.setString(7, libao.getColumn1());
			ps.setString(8, libao.getColumn2());
			
			i = ps.executeUpdate();
			if(i<=0){result=false;}
			
		} 
			con.commit();
			con.setAutoCommit(true);
			if(logger.isDebugEnabled()){
				logger.debug("[添加周月礼包信息] [OK] ["+libaoList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			}
		}catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			result=false;
			logger.error("[添加周月礼包信息] [FAIL] ["+libaoList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			
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
	public ArrayList<Libao> getBySql(String sql) {

		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<Libao> libaoList=new ArrayList<Libao>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				Libao libao = new Libao();
				
				libao.setColumn1(rs.getString("COLUMN1"));
				libao.setColumn2(rs.getString("COLUMN2"));
				libao.setCount(rs.getLong("COUNT"));
				libao.setCreateDate(rs.getTimestamp("CREATEDATE"));
				libao.setDanjia(rs.getLong("DANJIA"));
				libao.setDaoJuName(rs.getString("DAOJUNAME"));
				libao.setFenQu(rs.getString("FENQU"));
				libao.setType(rs.getInt("TYPE"));
				
				libaoList.add(libao);
				if(logger.isDebugEnabled()){
					logger.debug("[查询周月礼包信息] [OK] ["+libaoList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
				}
			}
		} catch (SQLException e) {
			logger.error("[查询周月礼包信息] [FAIL] [sql:"+sql+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return libaoList;
	}

	
	@Override
	public ArrayList<String[]> getliBaoData(String sql,String [] strs) {

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

	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}
	
}
