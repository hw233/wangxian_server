package com.sqage.stat.commonstat.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.commonstat.dao.OnlineUserStatDao;
import com.xuanzhi.tools.dbpool.DataSourceManager;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 提供用户在线情况统计
 * 查最高在线和平均在线情况
 * 一个是查一小时60分钟内的用户最高在线峰值
 * 一个是查一小时平均在线用户数
 * 
 * added by liuyang at 2012-05-04
 */

public class OnlineUserStatDaoImpl implements OnlineUserStatDao {
	private static final Log log = LogFactory.getLog(OnlineUserStatDaoImpl.class);


	protected void initDao() {
		// do nothing
	}
	/**
	 * 获得一天每小时最大在线用户数
	 * @param day 查询的日期 不能为空
	 * @param fenqu 游戏分区 可为null 表示不分区
	 * @param qudao 渠道 可为null 表示不分渠道
	 * @return List<Object[]> 返回一个包含Object数组的List，
	 * 在Object数组中 下标：
	 * 				0  为统计的时间 不能为空
	 * 				1 为一小时内的最大用户数
	 */
	public List<Object[]> getMaxUserOnlineNumByHoursInWholeDay(Date day,String fenqu,String qudao) {
		if(day == null)
		{
			throw new IllegalArgumentException("传入的查询日期为"+day+"，查询日期不能为空，必须有值！");
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			List<Object[]> maxNumInfos = new ArrayList<Object[]>();
			String fields = "to_char(so.onlinedate,'yyyy-mm-dd HH24')," + 
							" max(so.usercount) ";
			String groupByCondition = " group by" + 
							   "  to_char(so.onlinedate,'yyyy-mm-dd HH24')" ;
			String whereCondition = "  where" + 
									"       1=1" + 
									"  and" + 
									"       so.onlinedate" + 
									"  between" + 
									"       to_date('"+ DateUtil.formatDate(day, "yyyyMMddhhmmss") + 
									"','yyyymmddhh24miss')" + 
									"  and" + 
									"       to_date('" + DateUtils.addDays(day, 1) +"','yyyymmddhh24miss')" ;
			
			if(qudao != null)
			{
				whereCondition += " and so.qudao = '"+qudao+"' ";
			}
			else
			{
				whereCondition += " and so.qudao != '所有渠道' ";
			}
			
			if( fenqu != null)
			{
				whereCondition += " and so.fenqu = '"+fenqu+"' ";
			}
			
			String sql = "select " + fields  +  
					"  from" + 
					"       stat_onlineusers so" + 
					 whereCondition + 
					 groupByCondition +
					"   order by" + 
					"        to_char(so.onlinedate,'yyyy-mm-dd HH24')";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Object[] os = new Object[2];
				os[0] = rs.getString(1);
				os[1] = rs.getInt(2);
				maxNumInfos.add(os);
			}
		} catch (Throwable e) {
			log.error("getMaxUserOnlineNumByHoursInWholeDay failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return null;
	}
	
	
	
	/**
	 * 获得一天每小时平均在线用户数
	 * @param day 查询的日期 不能为空
	 * @param fenqu 游戏分区 可为null 表示不分区
	 * @param qudao 渠道 可为null 表示不分渠道
	 * @return List<Object[]> 返回一个包含Object数组的List，
	 * 在Object数组中 下标：
	 * 				0  为统计的时间 不能为空
	 * 				1 为一小时内的平均用户数
	 */
	public List<Object[]> getAvgUserOnlineNumByHoursInWholeDay(Date day,String fenqu,String qudao) {
		if(day == null)
		{
			throw new IllegalArgumentException("传入的查询日期为"+day+"，查询日期不能为空，必须有值！");
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			List<Object[]> avgNumInfos = new ArrayList<Object[]>();
			String fields = "to_char(so.onlinedate,'yyyy-mm-dd HH24')," + 
							" round(avg(so.usercount),2) ";
			String groupByCondition = " group by" + 
							   "  to_char(so.onlinedate,'yyyy-mm-dd HH24')" ;
			String whereCondition = "  where" + 
									"       1=1" + 
									"  and" + 
									"       so.onlinedate" + 
									"  between" + 
									"       to_date('"+ DateUtil.formatDate(day, "yyyyMMddhhmmss") + 
									"','yyyymmddhh24miss')" + 
									"  and" + 
									"       to_date('" + DateUtils.addDays(day, 1) +"','yyyymmddhh24miss')" ;
			
			if(qudao != null)
			{
				whereCondition += " and so.qudao = '"+qudao+"' ";
			}
			else
			{
				whereCondition += " and so.qudao != '所有渠道' ";
			}
			
			if( fenqu != null)
			{
				whereCondition += " and so.fenqu = '"+fenqu+"' ";
			}
			
			String sql = "select " + fields  +  
					"  from" + 
					"       stat_onlineusers so" + 
					 whereCondition + 
					 groupByCondition +
					"   order by" + 
					"        to_char(so.onlinedate,'yyyy-mm-dd HH24')";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Object[] os = new Object[2];
				os[0] = rs.getString(1);
				os[1] = rs.getInt(2);
				
				avgNumInfos.add(os);		
			}
		} catch (Throwable e) {
			log.error("getAvgUserOnlineNumByHoursInWholeDay failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return null;
	}
	
	
	/**
	 * 获得一天每小时最大在线用户数和平均在线用户数
	 * @param day 查询的日期 不能为空
	 * @param fenqu 游戏分区 可为null 表示不分区
	 * @param qudao 渠道 可为null 表示不分渠道
	 * @return List<Object[]> 返回一个包含Object数组的List，
	 * 在Object数组中 下标：
	 * 				0  为统计的时间 不能为空
	 * 				1为一小时内的最大用户数
	 * 				2 为一小时内的平均用户数
	 */
	
	
	public List<Object[]> getMaxAndAvgUserOnlineNumByHoursInWholeDay(Date day,String fenqu,String qudao) {
		if(day == null)
		{
			throw new IllegalArgumentException("传入的查询日期为"+day+"，查询日期不能为空，必须有值！");
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			List<Object[]> numInfos = new ArrayList<Object[]>();
			String fields = "to_char(so.onlinedate,'yyyy-mm-dd HH24:mi') as onlinedate," +
							" sum(so.usercount) as usercount " ;
			String groupByCondition = " group by" + 
							   "  to_char(so.onlinedate,'yyyy-mm-dd HH24:mi')" ;
			String whereCondition = "  where" + 
									"       1=1" + 
									"  and" + 
									"       so.onlinedate" + 
									"  >= " + 
									"       to_date('"+ DateUtil.formatDate(day, "yyyyMMddHHmmss") + 
									"','yyyymmddhh24miss')" + 
									"  and so.onlinedate < " + 
									"       to_date('" + DateUtil.formatDate(DateUtils.addDays(day, 1),"yyyyMMddHHmmss") +"','yyyymmddhh24miss')" ;
			
			if(qudao != null)
			{
				whereCondition += " and so.qudao = '"+qudao+"' ";
			}
			else
			{
				whereCondition += " and so.qudao != '所有渠道' ";
			}
			
			if( fenqu != null)
			{
				whereCondition += " and so.fenqu = '"+fenqu+"' ";
			}
			
			String sql = "select " + fields  +  
					"  from" + 
					"       stat_onlineusers so" + 
					 whereCondition + 
					 groupByCondition +
					"   order by" + 
					"        to_char(so.onlinedate,'yyyy-mm-dd HH24')";
			
			String sqlWrapper = "select " +
					"  to_char(to_date(t.onlinedate,'yyyy-mm-dd HH24:mi'),'yyyy-mm-dd HH24')," +
					" max(t.usercount), " + 
					"   round(avg(t.usercount),2) " + 
					"from " + 
					"( " + 
				 sql +
					" ) t " + 
					"group by to_char(to_date(t.onlinedate,'yyyy-mm-dd HH24:mi'),'yyyy-mm-dd HH24') " +
					" order by to_char(to_date(t.onlinedate, 'yyyy-mm-dd HH24:mi'),'yyyy-mm-dd HH24')";
			
	log.debug("本次传入的参数day为："+day+",本次执行的sql为：" + sqlWrapper  );		
			pstmt = con.prepareStatement(sqlWrapper);
			ResultSet rs = pstmt.executeQuery();
	//System.out.println("得到的结果集为" +rs.getFetchSize() + "条" );		
			while(rs.next()) {
				Object[] os = new Object[3];
				os[0] = rs.getString(1);
				os[1] = rs.getInt(2); //最大用户数
				os[2] = rs.getInt(3); //平均用户数
				//log.info("现在进入rs循环当中，此时得到的日期为"+(Date)os[0]+"，最大用户数为"+os[1]+",平均用户数为"+os[2]);		
				numInfos.add(os);	
			}
			log.debug("马上要返回的结果为：" +numInfos);
			rs.close();
			return numInfos;
		} catch (Throwable e) {
			log.error("getMaxAndAvgUserOnlineNumByHoursInWholeDay failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}

		return null;
	}
	
	
	public Connection getConnection() throws SQLException {
		return DataSourceManager.getInstance().getConnection();
	}
}
