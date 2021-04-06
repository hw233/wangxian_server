package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.dao.BattleMSGDao;
import com.sqage.stat.commonstat.entity.Battle_PlayerStat;
import com.sqage.stat.commonstat.entity.Battle_TeamStat;
import com.sqage.stat.commonstat.entity.Battle_costTime;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class BattleMSGDaoImpl implements BattleMSGDao {
	static Logger logger = Logger.getLogger(BattleMSGDaoImpl.class);
	DataSourceManager dataSourceManager;

	@Override
	public ArrayList<String[]> getBattleMSG(String sql, String[] columusEnums) {

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
	@Override
	public void addBattle_costTimeList(ArrayList<Battle_costTime> battleCostTimeList) {
        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_BATTLE_COSTTIME(" +
					"TYPE," +
					"FENQU," +
					"COSTTIME," +
					"HAOTIANCOUNT," +
					
					"WUHUANGCOUNT," +
					"LINGZUNCOUNT," +
					"GUISHACOUNT," +
					"CREATEDATE," +
					"COLUMN1,"+
					"COLUMN2"+
				") values (?,?,?,?, ?,?,?,?,?,?)");

		    int i = -1;
			int count=0;
		for(Battle_costTime battle_costTime : battleCostTimeList){
			
			ps.setString(1, battle_costTime.getType());
			ps.setString(2, battle_costTime.getFenqu());
			ps.setLong(3, battle_costTime.getCostTime());
			
			ps.setString(4, battle_costTime.getHaoTianCount());
			ps.setString(5, battle_costTime.getWuHuangCount());
			ps.setString(6, battle_costTime.getLingZunCount());
			ps.setString(7, battle_costTime.getGuiShaCount());
			ps.setTimestamp(8, new Timestamp(battle_costTime.getCreateDate()));
			ps.setString(9, battle_costTime.getColumn1());
			ps.setString(10, battle_costTime.getColumn2());
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%600==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			
			if(logger.isDebugEnabled()){logger.debug("对战平台消耗时间"+battleCostTimeList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("对战平台消耗时间数据库异常",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[对战平台消耗时间数据回滚异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[对战平台消耗时间数据信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	public void addBattle_PlayerStatLis(ArrayList<Battle_PlayerStat> battlePlayerStatList) {
        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement("insert into STAT_BATTLE_PLAYERSTAT(" +
					"TYPE," +
					"FENQU," +
					"GONGXUN," +
					"PLAYERCOUNT," +
					
					"CREATEDATE," +
					"COLUMN1," +
					"COLUMN2" +
				") values (?,?,?,?, ?,?,?)");

		    int i = -1;
			int count=0;
		for(Battle_PlayerStat battle_PlayerStat : battlePlayerStatList){
			
			ps.setString(1, battle_PlayerStat.getType());
			ps.setString(2, battle_PlayerStat.getFenqu());
			ps.setString(3, battle_PlayerStat.getGongxun());
			
			ps.setLong(4, battle_PlayerStat.getPlayerCount());
			ps.setTimestamp(5, new Timestamp(battle_PlayerStat.getCreateDate()));
			ps.setString(6, battle_PlayerStat.getColumn1());
			ps.setString(7, battle_PlayerStat.getColumn2());
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%600==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			
			if(logger.isDebugEnabled()){logger.debug("对战个人功勋"+battlePlayerStatList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("对战个人功勋数据库异常",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[对战个人功勋数据回滚异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[对战个人功勋数据信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
	public void addBattle_TeamStatList(ArrayList<Battle_TeamStat> battleTeamStatList) {


        long now=System.currentTimeMillis();
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataSourceManager.getInstance().getConnection();
			con.setAutoCommit(false);
			
			ps = con.prepareStatement("insert into STAT_BATTLE_TEAMSTAT(" +
					"TYPE," +
					"FENQU," +
					"GUOJIA," +
					"GONGXUN," +
					
					"HAOTIANCOUNT," +
					"WUHUANGCOUNT," +
					"LINGZUNCOUNT," +
					"GUISHACOUNT," +
					
					"CREATEDATE," +
					"COLUMN1," +
					"COLUMN2" +
				") values (?,?,?,?, ?,?,?,?, ?,?,?)");

		    int i = -1;
			int count=0;
		for(Battle_TeamStat battle_TeamStat : battleTeamStatList){
			
			ps.setString(1, battle_TeamStat.getType());
			ps.setString(2, battle_TeamStat.getFenqu());
			ps.setString(3, battle_TeamStat.getGuojia());
			
			ps.setString(4, battle_TeamStat.getGongxun());
			ps.setString(5, battle_TeamStat.getHaoTianCount());
			ps.setString(6, battle_TeamStat.getWuHuangCount());
			ps.setString(7, battle_TeamStat.getLingZunCount());
			ps.setString(8, battle_TeamStat.getGuiShaCount());
			ps.setTimestamp(9, new Timestamp(battle_TeamStat.getCreateDate()));
			ps.setString(10, battle_TeamStat.getColumn1());
			ps.setString(11, battle_TeamStat.getColumn2());
			
			i = ps.executeUpdate();
			count++;
			if(count!=0&&count%600==0){con.commit(); }
		}
			con.commit(); 
			con.setAutoCommit(true);
			
			if(logger.isDebugEnabled()){logger.debug("对战平台团队功勋"+battleTeamStatList.size()+"条,[耗时:"+(System.currentTimeMillis()-now)+"ms]");}
		} catch (Exception e) {
			logger.error("对战平台团队功勋数据库异常",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				 logger.error("[对战平台团队功勋数据回滚异常] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
			}
			    logger.error("[对战平台团队功勋数据信息] [FAIL] [cost:"+(System.currentTimeMillis()-now)+"ms]",e);
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
