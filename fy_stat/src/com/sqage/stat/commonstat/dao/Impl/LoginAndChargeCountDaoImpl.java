package com.sqage.stat.commonstat.dao.Impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.commonstat.dao.LoginAndChargeCountDao;
import com.xuanzhi.tools.dbpool.DataSourceManager;
import com.xuanzhi.tools.text.DateUtil;

public class LoginAndChargeCountDaoImpl implements LoginAndChargeCountDao 
{
	private static final Log log = LogFactory.getLog(LoginAndChargeCountDao.class);
	/**
	 * 统计指定时间段内的登陆用户数，和指定时间的流失人数
	 * 返回object数组
	 * 在object数组中，下标
	 * 		0：Integer 登陆用户数
	 * 		1:Integer 流失数
	 */
	@Override
	public Object[] countLoginAndRunOff(String beginDate, String endDate,Integer afterDay,String fenqu, String qudao) throws Exception
	{
		long startTime = System.currentTimeMillis();
		Object[] info = new Object[2];
		String afterDayBeginDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),1),"yyyy-MM-dd");	
		String afterDayEndDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),afterDay),"yyyy-MM-dd");
		
		String sql  = "select count(distinct sp.username) " + 
				" from  stat_playgame sp " + 
				" where sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " + 
				" union all " + 
				" select count(distinct ssp.username) from  stat_playgame ssp " + 
				" where ssp.enterdate between to_date(?,'yyyy-MM-dd') and  to_date(?,'yyyy-MM-dd') " + 
				" and not exists " + 
				"    ( select 1 from stat_playgame spp where " + 
				"        spp.username = ssp.username " + 
				"        and spp.enterdate  between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " + 
				"        and spp.fenqu = ssp.fenqu )";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			if(con != null)
			{
				if(StringUtils.isEmpty(fenqu) && StringUtils.isEmpty(qudao))
				{
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, beginDate);
					pstmt.setString(2, endDate);
					pstmt.setString(3, beginDate);
					pstmt.setString(4, endDate);
					pstmt.setString(5, afterDayBeginDate);
					pstmt.setString(6, afterDayEndDate);
				}
				else if( !StringUtils.isEmpty(qudao) )
				{
					sql = "select count(distinct sp.username) from stat_playgame sp,stat_user su   " + 
									" where sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')   " + 
									" and sp.username = su.name and su.qudao = ?   " + 
									" union all   " + 
									" select count(distinct ssp.username) from stat_playgame ssp,stat_user ssu   " + 
									" where  ssp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')   " + 
									" and ssp.username = ssu.name and ssu.qudao = ?   " + 
									" and not exists   " + 
									"    ( select 1 from stat_playgame spp   " + 
									"        where spp.username = ssp.username   " + 
									"        and spp.enterdate between  to_date(?,'yyyy-MM-dd')   " + 
									"        and to_date(?,'yyyy-MM-dd') and  spp.fenqu = ssp.fenqu )";

						if(!StringUtils.isEmpty(fenqu))
						{
							sql = "select count(distinct sp.username) from stat_playgame sp,stat_user su    " + 
									" where sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')    " + 
									" and sp.username = su.name and su.qudao = ? and sp.fenqu =  ?    " + 
									" union all    " + 
									" select count(distinct ssp.username) from stat_playgame ssp,stat_user ssu    " + 
									" where ssp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')    " + 
									" and ssp.username = ssu.name and ssu.qudao = ? and  ssp.fenqu = ?    " + 
									" and not exists    " + 
									"    ( select 1 from stat_playgame spp    " + 
									"        where spp.username = ssp.username    " + 
									"        and spp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')    " + 
									"        and ssp.fenqu = spp.fenqu )";
						}
						
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, beginDate);
						pstmt.setString(2, endDate);
						pstmt.setString(3, qudao); //渠道
						
						if(StringUtils.isEmpty(fenqu))
						{
							pstmt.setString(4, beginDate); //开始日期
							pstmt.setString(5, endDate); //结束日期
							pstmt.setString(6, qudao); //是渠道 
							pstmt.setString(7, afterDayBeginDate);//是afterDayBeginDate
							pstmt.setString(8, afterDayEndDate); //是afterDayEndDate
						}
						else
						{
							pstmt.setString(4, fenqu); //分区 
							pstmt.setString(5, beginDate); //是开始日期 
							pstmt.setString(6, endDate); //是结束日期
							pstmt.setString(7, qudao);//是渠道
							pstmt.setString(8, fenqu); //是分区
							pstmt.setString(9, afterDayBeginDate); //是afterDayBeginDate
							pstmt.setString(10, afterDayEndDate); //是afterDayEndDate
						}
				}
				else if(fenqu != null) //查询分区 但是不分渠道查询
				{
					sql = "select count(distinct sp.username) from stat_playgame sp    " + 
							" where sp.enterdate between to_date(?,'yyyy-MM-dd') and  to_date(?,'yyyy-MM-dd') and sp.fenqu = ?    " + 
							" union all    " + 
							" select count(distinct ssp.username) from  stat_playgame ssp    " + 
							" where ssp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')    " + 
							" and ssp.fenqu = ?    " + 
							" and not exists " +
							"     ( select 1 from stat_playgame spp where spp.username = ssp.username    " + 
							"        and spp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " +
							"        and spp.fenqu = ssp.fenqu )";
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, beginDate);
					pstmt.setString(2, endDate);
					pstmt.setString(3, fenqu); //分区
					pstmt.setString(4, beginDate); //开始日期
					pstmt.setString(5, endDate);//结束日期
					pstmt.setString(6, fenqu); //分区
					pstmt.setString(7, afterDayBeginDate); //afterDayBeginDate
					pstmt.setString(8, afterDayEndDate); //afterDayEndDate
					ResultSet rs = pstmt.executeQuery();
				}
				
				ResultSet rs = pstmt.executeQuery();
				
				int i = 0;
				while(rs.next())
				{
					info[i] = rs.getInt(1);
					i++;
				}
				rs.close();
			}
			else
			{
				throw new IllegalArgumentException("没有获取到数据库连接！");
			}
			return info;
		} catch (SQLException e) {
			log.error("获取连接报错！执行的sql是：" + sql , e);
		}
		finally 
		{
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return null;
	}

	/**
	 * 统计时间段内的流失用户的信息
	 * @param beginDate
	 * @param endDate
	 * @param fenqu
	 * @param qudao
	 * @return Map key:用户名 value:等级
	 * added by liuyang at 2012-05-14
	 */
	@Override
	public Map<String, Integer> countRunOff(String beginDate, String endDate, Integer afterDay,
			String fenqu, String qudao) 
	{
		long startTime = System.currentTimeMillis();
		if(beginDate == null || endDate == null)
			throw new NullArgumentException("参数beginDate的值:"+beginDate+"和endDate的值:"+endDate+"不能为空！");
		
		
		String afterDayBeginDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),1),"yyyy-MM-dd");	
		String afterDayEndDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),afterDay),"yyyy-MM-dd");
		
		if(afterDayBeginDate == null || afterDayEndDate == null)
		{
			throw new IllegalArgumentException("结束日期格式无法被解析，传入的结束日期值为：" + endDate);
		}
		
		Map<String, Integer> infos = new HashMap<String, Integer>();
		
		String sql  = "select sp.username, max(sp.maxlevel)    " + 
						" from  stat_playgame sp where    " + 
						" not exists (select 1 from stat_playgame s    " + 
						"        where s.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')    " + 
						"        and s.username = sp.username and s.fenqu = sp.fenqu    " + 
						"       )    " + 
						" and sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')    " + 
						" group by sp.username";

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			if(con != null)
			{
				if(StringUtils.isEmpty(fenqu) && StringUtils.isEmpty(qudao))
				{
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, afterDayBeginDate);
					pstmt.setString(2, afterDayEndDate);
					pstmt.setString(3, beginDate);
					pstmt.setString(4, endDate);
				}
				else if( !StringUtils.isEmpty(qudao) )
				{
					sql = "select sp.username,max(sp.maxlevel) from  stat_playgame sp,stat_user su    " + 
									" where    " + 
									" not exists " + 
									"      ( select 1 from stat_playgame s    " + 
									"        where s.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " + 
									"        and s.username = sp.username and s.fenqu = sp.fenqu    " + 
									"       )    " + 
									" and sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " + 
									" and sp.username = su.name and su.qudao = ? " + 
									" group by sp.username";

						if(!StringUtils.isEmpty(fenqu))
						{
							sql = "select sp.username, max(sp.maxlevel)    " + 
									" from  stat_playgame sp,stat_user su    " + 
									" where    " + 
									" not exists    " + 
									"      ( select 1 from stat_playgame s    " + 
									"        where s.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')    " + 
									"        and s.username = sp.username and s.fenqu = sp.fenqu    " + 
									"       )    " + 
									" and sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " +
									" and sp.username = su.name and su.qudao = ? and sp.fenqu = ?    " + 
									" group by sp.username";
						}
						
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, afterDayBeginDate);
						pstmt.setString(2, afterDayEndDate);
						pstmt.setString(3, beginDate);
						pstmt.setString(4, endDate);
					
						if(StringUtils.isEmpty(fenqu))
						{
							pstmt.setString(5, qudao); //渠道
						}
						else
						{
							pstmt.setString(5, qudao); //渠道
							pstmt.setString(6, fenqu); //分区
						}
				}
				else if(fenqu != null) //查询分区 但是不分渠道查询
				{
					sql = "select sp.username,max(sp.maxlevel) " + 
							" from  stat_playgame sp " + 
							" where " + 
							" not exists " + 
							"      ( select 1 from stat_playgame s " + 
							"        where s.enterdate between to_date(?,'yyyy-MM-dd') " + 
							"        and to_date(?,'yyyy-MM-dd') and s.username = sp.username " + 
							"        and s.fenqu = sp.fenqu " + 
							"       ) " + 
							" and sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " + 
							" and sp.fenqu = ? group by sp.username";
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, afterDayBeginDate);
					pstmt.setString(2, afterDayEndDate);
					pstmt.setString(3, beginDate);
					pstmt.setString(4, endDate);
					pstmt.setString(5, fenqu);
				}
			}
			else
			{
				throw new IllegalArgumentException("没有获取到数据库连接！");
			}
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				String key = rs.getString(1);
				Integer value = rs.getInt(2);
				infos.put(key, value);
			}
			
			rs.close();
			if(log.isDebugEnabled())
			{
				log.debug("[统计时间段内的流失用户的信息] [执行sql] [成功] [sql:"+sql+"] [查询出的结果数:"+infos.size()+"] [开始时间:"+beginDate+"] [结束时间:"+endDate+"] [afterDay:"+afterDay+"] [分区:"+fenqu+"] [渠道:"+qudao+"] [afterDayBeginDate:"+afterDayBeginDate+"] [afterDayEndDate:"+afterDayEndDate+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return infos;
			
		} catch (SQLException e) {
			log.error("获取连接报错！执行的sql是：" + sql , e);
		}
		finally 
		{
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return null;
	}
	
	/**
	 * 统计时间段内的流失用户的信息
	 * @param beginDate
	 * @param endDate
	 * @param fenqu
	 * @param qudao
	 * @return Map key:用户名 value:等级
	 * added by liuyang at 2012-05-14
	 */
	@Override
	public Map<Integer, Integer> countRunOffLevelSpread(String beginDate, String endDate, Integer afterDay,String fenqu, String qudao)  throws Exception
	{
		long startTime = System.currentTimeMillis();
		String afterDayBeginDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),1),"yyyy-MM-dd");	
		String afterDayEndDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),afterDay),"yyyy-MM-dd");
		Map<Integer, Integer> infos = new LinkedHashMap<Integer, Integer>();
		String sql  = "select t.mlevel,count(*) from " +
				" (select max(sp.maxlevel) as mlevel from  stat_playgame sp where " + 
						" not exists " + 
						"      ( select 1 from stat_playgame s " + 
						"        where s.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " + 
						"        and s.username = sp.username and s.fenqu = sp.fenqu  " + 
						"       )    " + 
						" and sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " + 
						" group by sp.username " +
						" ) t group by t.mlevel order by t.mlevel";

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			if(con != null)
			{
				if(StringUtils.isEmpty(fenqu) && StringUtils.isEmpty(qudao))
				{
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, afterDayBeginDate);
					pstmt.setString(2, afterDayEndDate);
					pstmt.setString(3, beginDate);
					pstmt.setString(4, endDate);
				}
				else if( !StringUtils.isEmpty(qudao) )
				{
					sql = "select t.mlevel,count(*) from " +
							" (select max(sp.maxlevel) as mlevel " + 
									" from  stat_playgame sp,stat_user su " + 
									" where    " + 
									" not exists    " + 
									"      ( select 1 from stat_playgame s " + 
									"        where s.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " + 
									"        and s.username = sp.username and s.fenqu = sp.fenqu    " + 
									"       )    " + 
									" and sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " + 
									" and sp.username = su.name and  su.qudao = ? " + 
									" group by sp.username" +
									" ) t group by t.mlevel order by t.mlevel";

						if(!StringUtils.isEmpty(fenqu))
						{
							sql ="select t.mlevel,count(*) from " +
									"( select    max(sp.maxlevel)  as mlevel " +
									" from  stat_playgame sp,stat_user su " +
									" where    " + 
									" not exists    " + 
									"      ( select 1 from stat_playgame s    " + 
									"        where s.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')    " + 
									"        and s.username = sp.username and s.fenqu = sp.fenqu    " + 
									"       )    " + 
									" and sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd')    " + 
									" and sp.username = su.name and su.qudao = ? and sp.fenqu = ?    " + 
									" group by sp.username " +
									" ) t group by t.mlevel order by t.mlevel";
						}
						
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, afterDayBeginDate);
						pstmt.setString(2, afterDayEndDate);
						pstmt.setString(3, beginDate);
						pstmt.setString(4, endDate);
					
						if(StringUtils.isEmpty(fenqu))
						{
							pstmt.setString(5, qudao); //渠道
						}
						else
						{
							pstmt.setString(5, qudao); //渠道
							pstmt.setString(6, fenqu); //分区
						}
				}
				else if(fenqu != null) //查询分区 但是不分渠道查询
				{
					sql = "select t.mlevel,count(*) from " +
							"( select max(sp.maxlevel) as mlevel from  stat_playgame sp where " + 
							" not exists    " + 
							"      ( select 1 from stat_playgame s where " + 
							"         s.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " + 
							"        and s.username = sp.username and s.fenqu = sp.fenqu " + 
							"       ) " + 
							" and sp.enterdate between to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " +
							" and sp.fenqu = ?  group by sp.username" +
							" ) t group by t.mlevel order by t.mlevel";
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, afterDayBeginDate);
					pstmt.setString(2, afterDayEndDate);
					pstmt.setString(3, beginDate);
					pstmt.setString(4, endDate);
					pstmt.setString(5, fenqu);
				}
			}
			else
			{
				throw new IllegalArgumentException("没有获取到数据库连接！");
			}
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				Integer key = rs.getInt(1);
				Integer value = rs.getInt(2);
				infos.put(key, value);
			}
			rs.close();
			if(log.isDebugEnabled())
			{
				log.debug("[统计时间段内的流失用户的信息] [执行sql] [成功] [sql:"+sql+"] [查询出的结果数:"+infos.size()+"] [开始时间:"+beginDate+"] [结束时间:"+endDate+"] [afterDay:"+afterDay+"] [分区:"+fenqu+"] [渠道:"+qudao+"] [afterDayBeginDate:"+afterDayBeginDate+"] [afterDayEndDate:"+afterDayEndDate+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return infos;
		} catch (SQLException e) {
			log.error("获取连接报错！执行的sql是：" + sql , e);
		}
		finally 
		{
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return null;
	}
	
	/**
	 * 统计时间段内的登陆人数和充值人数等信息
	 * @param beginDate
	 * @param endDate
	 * @param afterDay  结束时间后的天数
	 * @param fenqu
	 * @param qudao
	 * @return Object[]
	 * 			下标:
	 * 				0  充值人数
	 * 				1  充值后连续几天未登录人数
	 * 				2 充值后连续几天未付费人数
	 * added by liuyang at 2012-05-15
	 */
	@Override
	public Object[] countLoginAndCharge(String beginDate, String endDate,
			Integer afterDay, String fenqu, String qudao) throws Exception
	{
		long startTime = System.currentTimeMillis();
		if(beginDate == null || endDate == null)
			throw new NullArgumentException("参数beginDate的值:"+beginDate+"和endDate的值:"+endDate+"不能为空！");
		
		String formatedBeginDate = DateUtil.formatDate(DateUtil.parseDate(beginDate, "yyyy-MM-dd"), "yyyyMMddHHmmss");
		String formatedEndDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),1), "yyyyMMddHHmmss");
		
		String yyyyAfterDayBeginDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),1),"yyyy-MM-dd");	
		String yyyyAfterDayEndDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),afterDay),"yyyy-MM-dd");	
		
		if(formatedBeginDate == null || formatedEndDate == null)
		{
			throw new IllegalArgumentException("传入的日期格式 beginDate:[" +beginDate+"],endDate:["+endDate+"]格式不正确！");
		}
		
		String afterDayBeginDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),1),"yyyyMMddHHmmss");	
		String afterDayEndDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),afterDay+1),"yyyyMMddHHmmss");
		
		if(afterDayBeginDate == null || afterDayEndDate == null)
		{
			throw new IllegalArgumentException("结束日期格式无法被解析，传入的结束日期值为：" + endDate);
		}
		
		Object[] info = new Object[3];
		
		String UNION_ALL = " union all ";
		String appendCondition = "";
		
		//统计充值人数的sql
		String chargeUserNumSql = "select count(distinct sc.username)    " +
				" from stat_chongzhi sc    " + 
				" where sc.time >= to_date(?,'yyyyMMddHH24miss')    " + 
				" and sc.time < to_date(?,'yyyyMMddHH24miss')    ";
		
		//统计充值后未登陆人数sql
		String afterChargeNotLoginSql = 
			"select count(distinct sc.username)    " + 
				" from stat_chongzhi sc where sc.time >= to_date(?,'yyyyMMddHH24miss') and sc.time < to_date(?,'yyyyMMddHH24miss')    " + 
				" and not exists    " + 
				"    (  select 1 from stat_playgame sp    " + 
				"        where  sp.username = sc.username    " + 
				"        and sp.enterdate between to_date(?,'yyyy-MM-dd') and    " + 
				"        to_date(?,'yyyy-MM-dd') " +
				//" and sp.fenqu = sc.fenqu    " + 
				"    ) ";
		
		//统计充值后连续几天未再次充值的人数的sql
		String afterChargeNotChargeAgainSql = 
			"select count(distinct sc.username) from stat_chongzhi sc    " + 
				" where sc.time >= to_date(?,'yyyyMMddHH24miss') and sc.time < to_date(?,'yyyyMMddHH24miss')    " + 
				" and not exists    " + 
				"    ( select 1 from stat_chongzhi scc    " + 
				"        where scc.username = sc.username    " + 
				"        and  scc.time >= to_date(?,'yyyyMMddHH24miss') and scc.time < to_date(?,'yyyyMMddHH24miss')    " + 
				//"        and scc.fenqu = sc.fenqu    " + 
				"    )";
		
		if( !StringUtils.isEmpty(fenqu) )
		{
			appendCondition += " and sc.fenqu = ?";
		}
		
		if( !StringUtils.isEmpty(qudao) )
		{
			appendCondition += " and sc.qudao = ?";
		}

		chargeUserNumSql += appendCondition;
		afterChargeNotLoginSql += appendCondition;
		afterChargeNotChargeAgainSql += appendCondition;
		
			
		String sql = chargeUserNumSql + UNION_ALL + afterChargeNotLoginSql + UNION_ALL + afterChargeNotChargeAgainSql;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			
			if(con != null)
			{
				pstmt = con.prepareStatement(sql);
				
				if(StringUtils.isEmpty(fenqu) && StringUtils.isEmpty(qudao))
				{
					pstmt.setString(1, formatedBeginDate); //beginDate
					pstmt.setString(2, formatedEndDate); //endDate
					pstmt.setString(3, formatedBeginDate); //beginDate
					pstmt.setString(4, formatedEndDate); //endDate
					pstmt.setString(5, yyyyAfterDayBeginDate); //afterDayBeginDate
					pstmt.setString(6, yyyyAfterDayEndDate); //afterDayEndDate
					pstmt.setString(7, formatedBeginDate); //beginDate
					pstmt.setString(8, formatedEndDate); //endDate
					pstmt.setString(9, afterDayBeginDate); //afterDayBeginDate
					pstmt.setString(10, afterDayEndDate); //afterDayEndDate
				}
				else if( !StringUtils.isEmpty(fenqu) )
				{
						if(!StringUtils.isEmpty(qudao))
						{
							pstmt.setString(1, formatedBeginDate); 
							pstmt.setString(2, formatedEndDate); 
							pstmt.setString(3, fenqu); 
							pstmt.setString(4, qudao); 
							pstmt.setString(5, formatedBeginDate); 
							pstmt.setString(6, formatedEndDate); 
							pstmt.setString(7, yyyyAfterDayBeginDate); 
							pstmt.setString(8, yyyyAfterDayEndDate); 
							pstmt.setString(9, fenqu); 
							pstmt.setString(10, qudao);
							pstmt.setString(11, formatedBeginDate); 
							pstmt.setString(12, formatedEndDate); 
							pstmt.setString(13, afterDayBeginDate); 
							pstmt.setString(14, afterDayEndDate); 
							pstmt.setString(15, fenqu); 
							pstmt.setString(16, qudao); 
							
						}
						else
						{
							pstmt.setString(1, formatedBeginDate); 
							pstmt.setString(2, formatedEndDate); 
							pstmt.setString(3, fenqu); 
							pstmt.setString(4, formatedBeginDate); 
							pstmt.setString(5, formatedEndDate); 
							pstmt.setString(6, yyyyAfterDayBeginDate); 
							pstmt.setString(7, yyyyAfterDayEndDate); 
							pstmt.setString(8, fenqu); 
							pstmt.setString(9, formatedBeginDate); 
							pstmt.setString(10, formatedEndDate); 
							pstmt.setString(11, afterDayBeginDate);
							pstmt.setString(12, afterDayEndDate); 
							pstmt.setString(13, fenqu); 
						}
				}
				else if(qudao != null) //查询分区 但是不分渠道查询
				{
	
					pstmt.setString(1, formatedBeginDate); 
					pstmt.setString(2, formatedEndDate); 
					pstmt.setString(3, qudao); 
					pstmt.setString(4, formatedBeginDate); 
					pstmt.setString(5, formatedEndDate); 
					pstmt.setString(6, yyyyAfterDayBeginDate); 
					pstmt.setString(7, yyyyAfterDayEndDate); 
					pstmt.setString(8, qudao); 
					pstmt.setString(9, formatedBeginDate); 
					pstmt.setString(10, formatedEndDate); 
					pstmt.setString(11, afterDayBeginDate);
					pstmt.setString(12, afterDayEndDate); 
					pstmt.setString(13, qudao); 
				}
			}
			else
			{
				throw new IllegalArgumentException("没有获取到数据库连接！");
			}
			
			ResultSet rs = pstmt.executeQuery();
			int i = 0;
			while(rs.next())
			{
				info[i] = rs.getInt(1);
				i++;
			}
			
			rs.close();
			if(log.isInfoEnabled())
				log.info("[统计时间段内的流失用户的信息] [成功] [OK] [开始日期:"+beginDate+"] [结束日期:"+endDate+"] [结束日期后时间段:"+afterDay+"] [格式化后的开始日期:"+formatedBeginDate+"] [格式化后的结束日期:"+formatedEndDate+"] [结束日期后几日的开始日期:"+yyyyAfterDayBeginDate+"] [结束日期后几日的结束日期:"+yyyyAfterDayEndDate+"] [游戏分区:"+fenqu+"] [渠道:"+qudao+"]  [查询充值人数的SQL:"+chargeUserNumSql+"] [查询充值后未登陆用户的SQL:"+afterChargeNotLoginSql+"] [查询充值后连续n天没有充值的人数的SQL:"+afterChargeNotChargeAgainSql+"] [得到的info数组:"+"info[0]<->"+info[0]+"@info[1]<->"+info[1]+"@info[2]<->"+info[2]+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			return info;
			
		} catch (SQLException e) {
			//// TODO Auto-generated catch block
			log.error("获取连接报错！执行的sql是：" + sql , e);
		}
		finally 
		{
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return null;
	}
	
	
	/**
	 * 统计时间段内的未登录人数的等级分布和n天未充值人数前次充值金额的人数分布
	 * @param beginDate
	 * @param endDate
	 * @param afterDay  结束时间后的天数
	 * @param fenqu
	 * @param qudao
	 * @return Object[]
	 * 			下标:
	 * 				0  Map key 等级  value 人数
	 * 				1  Map key 金额  value 人数
	 * added by liuyang at 2012-05-16
	 */
	@Override
	public Object[] countNotLoginAndChargeSpread(String beginDate, String endDate,
			Integer afterDay, String fenqu, String qudao) throws Exception
	{
		long startTime = System.currentTimeMillis();
		if(beginDate == null || endDate == null)
			throw new NullArgumentException("参数beginDate的值:"+beginDate+"和endDate的值:"+endDate+"不能为空！");
		
		String formatedBeginDate = DateUtil.formatDate(DateUtil.parseDate(beginDate, "yyyy-MM-dd"), "yyyyMMddHHmmss");
		String formatedEndDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),1), "yyyyMMddHHmmss");
		
		String yyyyAfterDayBeginDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),1),"yyyy-MM-dd");	
		String yyyyAfterDayEndDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),afterDay),"yyyy-MM-dd");	
		
		if(formatedBeginDate == null || formatedEndDate == null)
		{
			throw new IllegalArgumentException("传入的日期格式 beginDate:[" +beginDate+"],endDate:["+endDate+"]格式不正确！");
		}
		
		String afterDayBeginDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),1),"yyyyMMddHHmmss");	
		String afterDayEndDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(endDate, "yyyy-MM-dd"),afterDay+1),"yyyyMMddHHmmss");
		
		if(afterDayBeginDate == null || afterDayEndDate == null)
		{
			throw new IllegalArgumentException("结束日期格式无法被解析，传入的结束日期值为：" + endDate);
		}
		
		Object[] info = new Object[2];
		Map<Integer,Integer> levelNumMap = new LinkedHashMap<Integer, Integer>();
		Map<BigDecimal,Integer> moneyNumMap = new LinkedHashMap<BigDecimal, Integer>();
		
		
		String appendCondition = "";
		
		//统计未登录等级
		String statLevelSubSql = 
			" select max(sp.maxlevel) as mlevel " + 
				"    from  stat_playgame sp,stat_chongzhi sc " + 
				"    where sp.username = sc.username and  " + 
				"    not exists    " + 
				"          ( select 1 from stat_playgame s    " + 
				"            where s.enterdate between  to_date(?,'yyyy-MM-dd') and to_date(?,'yyyy-MM-dd') " +
				"            and s.username = sc.username and  s.fenqu = sc.fenqu " + 
				"           )    " + 
				"    and sc.time >= to_date(?,'yyyyMMddHH24miss') and " + 
				"    sc.time < to_date(?,'yyyyMMddHH24miss') " ;
		
		String levelSqlWrapHead = "select t.mlevel as mlevel,count(*) as levelNum from (";
		String levelSqlWrapTail = " group by sp.username ) t group by t.mlevel order by t.mlevel";
		
		//统计每个人的充值金额
		String statChargeMoneyByPersonSubSql =
			"select sum(sc.money) as money " + 
				" from stat_chongzhi sc where sc.time >= to_date(?,'yyyyMMddHH24miss') " + 
				" and sc.time < to_date(?,'yyyyMMddHH24miss')    " + 
				" and not exists    " + 
				"    ( select 1 from stat_chongzhi scc    " + 
				"        where scc.username = sc.username    " + 
				"        and scc.time >= to_date(?,'yyyyMMddHH24miss') and scc.time < to_date(?,'yyyyMMddHH24miss') " + 
				"        and scc.fenqu = sc.fenqu    " + 
				"    ) ";
		
		String moneySqlWrapHead = "select t.money as money,count(*) as moneynum from (";
		String moneySqlWrapTail = 	" group by sc.username  ) t group by t.money order by t.money desc";	
		
		if( !StringUtils.isEmpty(fenqu) )
		{
			appendCondition += " and sc.fenqu = ?";
		}
		
		if( !StringUtils.isEmpty(qudao) )
		{
			appendCondition += " and sc.qudao = ?";
		}

		statLevelSubSql += appendCondition;
		statChargeMoneyByPersonSubSql += appendCondition;
		
			
		String sql = levelSqlWrapHead + statLevelSubSql + levelSqlWrapTail;
		String sql1 = moneySqlWrapHead + statChargeMoneyByPersonSubSql + moneySqlWrapTail;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			
			if(con != null)
			{
				//执行等级分布sql查询
				pstmt = con.prepareStatement(sql);
				
				if(StringUtils.isEmpty(fenqu) && StringUtils.isEmpty(qudao))
				{
					pstmt.setString(1, yyyyAfterDayBeginDate); 
					pstmt.setString(2, yyyyAfterDayEndDate); 
					pstmt.setString(3, formatedBeginDate); 
					pstmt.setString(4, formatedEndDate);

				}
				else if( !StringUtils.isEmpty(fenqu) )
				{
						if(!StringUtils.isEmpty(qudao))
						{
							pstmt.setString(1, yyyyAfterDayBeginDate); 
							pstmt.setString(2, yyyyAfterDayEndDate); 
							pstmt.setString(3, formatedBeginDate); 
							pstmt.setString(4, formatedEndDate);
							pstmt.setString(5, fenqu);
							pstmt.setString(6, qudao);
						}
						else
						{
							pstmt.setString(1, yyyyAfterDayBeginDate); 
							pstmt.setString(2, yyyyAfterDayEndDate); 
							pstmt.setString(3, formatedBeginDate); 
							pstmt.setString(4, formatedEndDate);
							pstmt.setString(5, fenqu);
						}
				}
				else if(qudao != null) //查询渠道 但是不分分区查询
				{
	
					pstmt.setString(1, yyyyAfterDayBeginDate); 
					pstmt.setString(2, yyyyAfterDayEndDate); 
					pstmt.setString(3, formatedBeginDate); 
					pstmt.setString(4, formatedEndDate);
					pstmt.setString(5, qudao);
				}
				
				ResultSet rs = pstmt.executeQuery();
				int i = 0;
				while(rs.next())
				{
					levelNumMap.put(rs.getInt(1), rs.getInt(2));				
				}
				info[i] = levelNumMap;
				i++;
				
				rs.close();
				
				//执行金额分布查询
				pstmt = con.prepareStatement(sql1);
				
				if(StringUtils.isEmpty(fenqu) && StringUtils.isEmpty(qudao))
				{
					pstmt.setString(1, formatedBeginDate); 
					pstmt.setString(2, formatedEndDate); 
					pstmt.setString(3, afterDayBeginDate); 
					pstmt.setString(4, afterDayEndDate);

				}
				else if( !StringUtils.isEmpty(fenqu) )
				{
						if(!StringUtils.isEmpty(qudao))
						{
							pstmt.setString(1, formatedBeginDate); 
							pstmt.setString(2, formatedEndDate); 
							pstmt.setString(3, afterDayBeginDate); 
							pstmt.setString(4, afterDayEndDate);
							pstmt.setString(5, fenqu);
							pstmt.setString(6, qudao);
							
						}
						else
						{
							pstmt.setString(1, formatedBeginDate); 
							pstmt.setString(2, formatedEndDate); 
							pstmt.setString(3, afterDayBeginDate); 
							pstmt.setString(4, afterDayEndDate);
							pstmt.setString(5, fenqu);
						}
				}
				else if(qudao != null) //查询分区 但是不分渠道查询
				{
					pstmt.setString(1, formatedBeginDate); 
					pstmt.setString(2, formatedEndDate); 
					pstmt.setString(3, afterDayBeginDate); 
					pstmt.setString(4, afterDayEndDate);
					pstmt.setString(5, qudao);
				}
				
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					moneyNumMap.put(rs.getBigDecimal(1), rs.getInt(2));
				}
				info[i] = moneyNumMap;
				rs.close();
			}
			else
			{
				throw new IllegalArgumentException("没有获取到数据库连接！");
			}
			
			if(log.isInfoEnabled())
				log.info("[查询未登录和未充值的流失人数详情] [成功] [OK] [开始日期:"+beginDate+"] [结束日期:"+endDate+"] [结束日期后时间段:"+afterDay+"] [格式化后的开始日期:"+formatedBeginDate+"] [格式化后的结束日期:"+formatedEndDate+"] [结束日期后几日的开始日期:"+yyyyAfterDayBeginDate+"] [结束日期后几日的结束日期:"+yyyyAfterDayEndDate+"] [游戏分区:"+fenqu+"] [渠道:"+qudao+"]  [查询游戏玩家等级分布的SQL:"+sql+"] [查询充值情况的SQL:"+sql1+"] [游戏玩家等级分布情况的MapSize:"+levelNumMap.size()+"] [充值人数情况的MapSize:"+moneyNumMap.size()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			
			return info;	
		}
		catch (SQLException e) {
			//// TODO Auto-generated catch block
			log.error("获取连接报错！执行的sql是：" + sql , e);
		}
		finally 
		{
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return null;
	}

	public Connection getConnection() throws SQLException 
	{
		return DataSourceManager.getInstance().getConnection();
		//Connection conn= DriverManager.getConnection("jdbc:oracle:thin:@221.179.174.52:1521:ora10g","commonstat","commonstat");
		//return  conn;
	}
}
