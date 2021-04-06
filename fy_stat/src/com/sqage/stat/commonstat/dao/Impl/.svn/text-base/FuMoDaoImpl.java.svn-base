package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.FuMoDao;
import com.sqage.stat.commonstat.entity.FuMo;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class FuMoDaoImpl implements FuMoDao {
	static Logger logger = Logger.getLogger(FuMoDaoImpl.class);
	DataSourceManager dataSourceManager;
	
	@Override
	public boolean addUseFoMoList(ArrayList<FuMo> fuMoList) {

        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			 String sql = " merge into STAT_FUMO t " +
		    		" using (select ? as data from dual) newData " +
		    		" ON (t.fenqu||t.username||t.COLUMN2=newData.data and t.type='1') " +
		    		" WHEN MATCHED THEN " +
		    		" UPDATE SET t.CREATETIME= ?,t.FOMOWUPINNAME=?"+
		            " WHEN NOT MATCHED THEN " +
    		        " INSERT (USERNAME,CREATETIME,TYPE,FENQU,FOMOWUPINNAME,COLUMN1,COLUMN2)" +
    		        " VALUES (?,?,?,?,  ?,?,?) ";
			 
			ps = con.prepareStatement(sql);
		    int i = -1;
			int count=0;
		for(FuMo fuMo : fuMoList){
			ps.setString(1,fuMo.getFenQu()+fuMo.getUserName()+fuMo.getColumn2());
			ps.setTimestamp(2, new Timestamp(fuMo.getCreateTime().getTime()));
			ps.setString(3, fuMo.getFoMoWuPinName());
			
			ps.setString(4, fuMo.getUserName());
			ps.setTimestamp(5, new Timestamp(fuMo.getCreateTime().getTime()));
			ps.setString(6, fuMo.getType());
			ps.setString(7, fuMo.getFenQu());
			ps.setString(8, fuMo.getFoMoWuPinName());
			
			ps.setString(9, fuMo.getColumn1());
			ps.setString(10, fuMo.getColumn2());
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%100==0){con.commit(); }
		   }
			con.commit(); 
			con.setAutoCommit(true);
			   
			if(logger.isDebugEnabled()){logger.debug("开始批量附魔信息"+fuMoList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("插入附魔信息异常",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[插入附魔信息异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[插入附魔信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	
	@Override
	public boolean addFinishFuMoList(ArrayList<FuMo> fuMoList) {

        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
		   String sql=" update  stat_fumo  f set f.USEDTIME=?,f.type='2' " +
		   		       " where f.username=? and f.fenqu=? and f.COLUMN2=? and f.type='1'";
			ps = con.prepareStatement(sql);
		    int i = -1;
			int count=0;
		for(FuMo fuMo : fuMoList){
			
			ps.setTimestamp(1, new Timestamp(fuMo.getCreateTime().getTime()));   //佛魔结束时间
			ps.setString(2, fuMo.getUserName());
			ps.setString(3, fuMo.getFenQu());
			ps.setString(4, fuMo.getColumn2());
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%100==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			if(logger.isDebugEnabled()){logger.debug("开始批量附魔信息"+fuMoList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入附魔信息异常",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[插入附魔信息异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[插入附魔信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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

	@Override
	public ArrayList<FuMo> getBySql(String sql) {
		long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<FuMo> fuMoList=new ArrayList<FuMo>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				FuMo fuMo = new FuMo();
				
				fuMo.setColumn1(rs.getString("COLUMN1"));
				fuMo.setColumn2(rs.getString("COLUMN2"));
				
				fuMo.setUserName(rs.getString("USERNAME"));
				fuMo.setFenQu(rs.getString("FENQU"));
				fuMo.setCreateTime(rs.getTimestamp("CREATETIME"));
				fuMo.setType(rs.getString("TYPE"));
				fuMo.setFoMoWuPinName(rs.getString("FOMOWUPINNAME"));
				fuMo.setUsedTime(rs.getTimestamp("USEDTIME"));
			
				fuMoList.add(fuMo);
				if(logger.isDebugEnabled()){
					logger.debug("[查询附魔信息] [OK] ["+fuMoList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
				}
			}
		} catch (SQLException e) {
			logger.error("[查询附魔信息] [FAIL] [sql:"+sql+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			
		} finally {
			try {
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fuMoList;
	}

	@Override
	public ArrayList<String[]> getFuMoData(String sql, String[] strs) {

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
