package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.NpcinfoDao;
import com.sqage.stat.commonstat.entity.Npcinfo;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class NpcinfoDaoImpl implements NpcinfoDao {
	static Logger logger = Logger.getLogger(NpcinfoDaoImpl.class);
	DataSourceManager dataSourceManager;

	@Override
	public boolean addList(ArrayList<Npcinfo> NpcinfoList) {
		Connection con = null;
		PreparedStatement ps = null;
		int i = -1;
		boolean result=true;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			
			ps = con.prepareStatement("insert into STAT_NPCINFO(" +
					"ID," +
					"CREATEDATE," +
					"FENQU," +
					
					"USERNAME," +
					"GAMELEVEL," +
					"NPCNAME," +
					"TASKTYPE," +
					
					"GETYOUXIBI," +
					"GETWUPIN," +
					"GETDAOJU," +
					"AWARD," +
					
					"CAREER," +
					"JIXING," +
					"COLUMN1," +
					"COLUMN2" +
					
					") values (SEQ_STAT_NPCINFO_ID.NEXTVAL,?,?, ?,?,?,?, ?,?,?,?, ?,?,?,?)");
			
			for(Npcinfo npcinfo:NpcinfoList){
				if (npcinfo.getCreateDate() != null) {
					ps.setTimestamp(1, new Timestamp(npcinfo.getCreateDate().getTime()));
				} else {
					ps.setTimestamp(1, null);
				}
			ps.setString(2, npcinfo.getFenQu());
			ps.setString(3, npcinfo.getUserName());
			ps.setInt(4, npcinfo.getGameLevel());
			ps.setString(5, npcinfo.getNpcName());
			ps.setString(6, npcinfo.getTaskType());
			
			ps.setInt(7, npcinfo.getGetYOuXiBi());
			ps.setInt(8, npcinfo.getGetWuPin());
			ps.setInt(9, npcinfo.getGetDaoJu());
			ps.setString(10, npcinfo.getAward());
			
			ps.setString(11, npcinfo.getCareer());
			ps.setString(12, npcinfo.getJixing());
			ps.setString(13, npcinfo.getColumn1());
			ps.setString(14, npcinfo.getColumn2());
			i = ps.executeUpdate();
			if(i<=0){result=false;}
		} 
			con.commit();
			con.setAutoCommit(true);
		}catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
	public ArrayList<String[]> getNpcinfo(String sql, String[] columusEnums) {
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

	public DataSourceManager getDataSourceManager() {
		return dataSourceManager;
	}

	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

}
