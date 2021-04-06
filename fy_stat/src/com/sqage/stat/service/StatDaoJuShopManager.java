package com.sqage.stat.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.commonstat.manager.Impl.Transaction_SpecialManagerImpl;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.timer.Executable;

public class StatDaoJuShopManager implements Executable {

    protected static StatDaoJuShopManager m_self = null;
	protected static final Log logger = LogFactory.getLog(StatDaoJuShopManager.class);
	public static StatDaoJuShopManager getInstance() {
			return m_self;
		}
	  
	public void initialize() throws Exception{
		m_self = this;
		logger.info("["+StatDaoJuShopManager.class.getName()+"] [initialized]");
	}
	
	@Override
	public void execute(String[] args) {
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
         Transaction_SpecialManagerImpl transaction_SpecialManager=Transaction_SpecialManagerImpl.getInstance();
         String _day = DateUtil.formatDate(statdate, "yyyy-MM-dd");
         
         String sql=" insert into stat_daoju_shop " +
         		" select t.fenqu,t.daojuname,to_char(t.createdate,'YYYY-MM-DD') createdate,t.position," +
         		" sum(t.daojunum) DAOJUNUM,sum(t.danjia) TOTALMONEY,t.vip,t.guojia " +
         		" from stat_daoju t where to_char(t.createdate,'YYYY-MM-DD') ='"+_day+"' " +
         		" and t.position is not null " +
         		" group by to_char(t.createdate,'YYYY-MM-DD'),t.fenqu,t.daojuname,t.position,t.vip,t.guojia";
  
         System.out.println("定时任务执行sql："+sql);
         transaction_SpecialManager.excute(sql);
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
