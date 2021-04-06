package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.dbpool.DataSourceManager;

public class Stat_base_onlineDaoImpl {
	static Logger logger = Logger.getLogger(Stat_base_onlineDaoImpl.class);
	DataSourceManager dataSourceManager;
	
	public List<String[]> getStat_base_online(String dateStr) {

		Connection con = null;
		PreparedStatement ps = null;
		ArrayList<String[]> resultlist=new ArrayList<String[]>();
		try {
			con = dataSourceManager.getInstance().getConnection();
			String sql="select t.*, t.rowid from STAT_BASE_ONLINE t where t.time_index='"+dateStr+"'";
			ps = con.prepareStatement(sql);
			ps.execute();
			ResultSet rs =  ps.getResultSet();
			while(rs.next()) {
				String[] vales=new String[11];
				
				vales[0]=rs.getString("TIME_INDEX");
				vales[1]=rs.getString("ACTIVEUSERCOUNT");
				vales[2]=rs.getString("LOGINTIMES");
				vales[3]=rs.getString("AVGONLINETIME");
				vales[4]=rs.getString("PAYUSERCOUNT");
				vales[5]=rs.getString("PAYMONEYCOUNT");
				vales[6]=rs.getString("PAYTIMES");
				vales[7]=rs.getString("CONSUMPTIONUSERCOUNT");
				vales[8]=rs.getString("CONSUMPTIONMONEY");
				vales[9]=rs.getString("YUANBAOKUCUN");
				vales[10]=rs.getString("YOUXIBIKUCUN");
				//vales[11]=rs.getString("HUOYAOFUFEIRENSHU");
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
	 public boolean addStat_base_online(String[] data) {

			Connection con = null;
			PreparedStatement ps = null;
			long now=System.currentTimeMillis();
			int i = -1;
			try {
				con = dataSourceManager.getInstance().getConnection();
				ps = con.prepareStatement("insert into STAT_BASE_ONLINE(" +
						"TIME_INDEX," +
						
						"ACTIVEUSERCOUNT," +
						"LOGINTIMES," +
						"AVGONLINETIME," +
						"PAYUSERCOUNT," +
						"PAYMONEYCOUNT," +
						
						"PAYTIMES," +
						"CONSUMPTIONUSERCOUNT," +
						"CONSUMPTIONMONEY," +
						"YUANBAOKUCUN," +
						"YOUXIBIKUCUN" +
						") values (?, ?,?,?,?,?, ?,?,?,?,? )");
				
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
				
				i = ps.executeUpdate();
				if(logger.isDebugEnabled()){
					logger.debug("[周/月活跃用户固定数据添加] [table:STAT_BASE_ONLINE] [OK] ["+data[0]+"] [cost:"+(System.currentTimeMillis()-now)+"ms]");
				}
			} catch (Exception e) {
				 logger.error("[周/月固定数据添加] [table:STAT_BASE_ONLINE] [fail] ["+data[0]+"] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}
	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}
	 
	 
}
