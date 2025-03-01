package com.sqage.stat.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.commonstat.manager.Impl.Transaction_SpecialManagerImpl;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.timer.Executable;

public class StatGameChongZhiYinPiaoManager implements Executable {

    protected static StatGameChongZhiYinPiaoManager m_self = null;
	protected static final Log logger = LogFactory.getLog(StatGameChongZhiYinPiaoManager.class);
	public static StatGameChongZhiYinPiaoManager getInstance() {
			return m_self;
		}
	  
	public void initialize() throws Exception{
		m_self = this;
		
	}
	
	@Override
	public void execute(String[] args) {
		// TODO Auto-generated method stub
		if(args.length == 0 || args[0].trim().length() == 0) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -1);
			genChannelStat(cal.getTime());
		} else if(args.length == 1) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(args[0]));
			genChannelStat(cal.getTime());
		} else if(args.length == 2) {
			String startdate = args[0];
			String enddate = args[1];
			Calendar cal = Calendar.getInstance();
			Date starttime = DateUtil.parseDate(startdate, "yyyy-MM-dd");
			cal.setTime(starttime);
			while(!DateUtil.formatDate(cal.getTime(), "yyyy-MM-dd").equals(enddate)) {
				genChannelStat(cal.getTime());
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			genChannelStat(cal.getTime());
		}
	}
	
	public void genChannelStat(Date statdate) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(statdate);
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.SECOND, 0);
//		Date starttime = cal.getTime();
//		cal.set(Calendar.HOUR_OF_DAY, 23);
//		cal.set(Calendar.MINUTE, 59);
//		cal.set(Calendar.SECOND, 59);
//		Date endtime = cal.getTime();
    
         Transaction_SpecialManagerImpl transaction_SpecialManager=Transaction_SpecialManagerImpl.getInstance();
         String _day = DateUtil.formatDate(statdate, "yyyy-MM-dd");
         
         String sql=" insert into  stat_gamechongzhi_yinpiao " +
         		" select   to_char(g.time,'YYYY-MM-DD') CREATEDATE,g.fenqu,g.action,g.reasontype,sum(g.money) MONEY " +
         		" from stat_gamechongzhi g " +
         		" where g.currencytype='2' and to_char(g.time,'YYYY-MM-DD')='"+_day+"' " +
         		" group by g.reasontype,g.action,g.fenqu,to_char(g.time,'YYYY-MM-DD') " ;
 //        		"  commit; " ;
         
         //非綁定銀子
         //商店银子   
         //商店銀子
         
         
        // System.out.println("定时任务执行sql："+sql);
         
         logger.info("["+StatGameChongZhiManager.class.getName()+"] 定时任务执行sql："+sql);
         transaction_SpecialManager.excute(sql);

	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
