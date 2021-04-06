package com.sqage.stat.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.commonstat.manager.Impl.Transaction_SpecialManagerImpl;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.timer.Executable;

public class StatPlayGameGuiZongManager implements Executable {

    protected static StatPlayGameGuiZongManager m_self = null;
	protected static final Log logger = LogFactory.getLog(StatPlayGameGuiZongManager.class);
	public static StatPlayGameGuiZongManager getInstance() {
			return m_self;
		}
	  
	public void initialize() throws Exception{
		m_self = this;
		logger.info("["+StatPlayGameGuiZongManager.class.getName()+"] [initialized]");
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
         
         String sql=" select to_char(g.time,'YYYY-MM-DD') time,g.fenqu,sum(g.money) money,g.action,g.reasontype " +
         		" from stat_gamechongzhi g where g.time between to_date('"+_day+" 00:00:00' ,'YYYY-MM-DD hh24:mi:ss') " +
         		" and to_date('"+_day+" 23:59:59' ,'YYYY-MM-DD hh24:mi:ss') " +
         		" group by to_char(g.time,'YYYY-MM-DD'),g.fenqu,g.action,g.reasontype;";
  
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
