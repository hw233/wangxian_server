package com.fy.engineserver.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.util.watchdog.ServerMoneyGuard;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.dbpool.ConnectionPool;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerFactoryHelper;

public class ServerWatchDog extends ServerMoneyGuard {

	/**
	 * 兑换比例，一元兑换多少游戏银两
	 */
	public double getExchangeRate() {
		// TODO Auto-generated method stub
		return 50000;
	}

	public String getGameName() {
		// TODO Auto-generated method stub
		return GameConstants.getInstance().getGameName();
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return GameConstants.getInstance().getServerName();
	}

	@Override
	public long scanYingziInDB() {
		// TODO Auto-generated method stub
		ConnectionPool pool = SimpleEntityManagerFactoryHelper.getPool();
		Connection conn = null;
		PreparedStatement pstmt = null;
		if(pool != null){
			try {
				conn = pool.getConnection();
				String sql = "select sum(silver) as amount from player";
				boolean isOracle = true;				
				if(PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
					isOracle = true;
				} else {
					isOracle = false;
				}
				if(!isOracle) {
					sql = "select sum(silver) as amount from PLAYER_S1";
				}
				pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					long amount = rs.getLong("amount");
					return amount;
				} 
			} catch(Exception e) {
				logger.error("[数据库操作发生异常]", e);
			} finally {
				try {
					pstmt.close();
				} catch(Exception ee) {
					ee.printStackTrace();
				}
				try {
					conn.close();
				} catch(Exception ee) {
					ee.printStackTrace();
				}
			}
		} else{
			logger.error("[获得数据库连接失败:连接池为null]");
		}
		return -1;
	}

}
