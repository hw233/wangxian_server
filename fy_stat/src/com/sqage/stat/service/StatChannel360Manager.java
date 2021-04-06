package com.sqage.stat.service;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.commonstat.manager.Impl.ChongZhiManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.OnLineUsersCountManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.timer.Executable;

public class StatChannel360Manager implements Executable {

    protected static StatChannel360Manager m_self = null;
	protected static final Log logger = LogFactory.getLog(StatChannel360Manager.class);
	public static StatChannel360Manager getInstance() {
			return m_self;
		}
	  
	public void initialize() throws Exception{
		m_self = this;
		logger.info("["+StatChannel360Manager.class.getName()+"] [initialized]");
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
    
		 PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
		 OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
         ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
         
         String _day = DateUtil.formatDate(statdate, "yyyy-MM-dd");
		 String day = DateUtil.formatDate(statdate, "yyyyMMdd");
		
//		 String qudao="360SDK_MIESHI";
//		 Long dau_pv =playGameManager.getEnterGameUserCount(_day,_day,qudao,null,null);//独立登陆
//		 Long dau_uv=playGameManager.getReg_LoginUserCount(_day,_day,qudao,null,null);//独立登陆（当天
//		 Long new_pay=chongZhiManager.getUnAheadChongZhiUserCount(_day,_day,qudao,null,null);//新付费人数
//		 Long max_pcu=onLineUsersCountManager.getMaxOnlineUserCount(_day,_day,qudao,null,null);//最高在线人数
//		String contentStr= "day="+day+"&appkey=360SDK_MIESHI" +
//				" &dau_pv="+dau_pv+"& dau_uv="+dau_uv+"&new_pay="+new_pay+"&max_pcu="+max_pcu;
//        String url="http://dev.app.360.cn/mobileapi/saveapi";
//        System.out.print("contentStr="+contentStr);
//		send(contentStr,url);
		
		/////////////////////////////360JIEKOU_MIESHI渠道信息///////////////
		 String qudao="360JIEKOU_MIESHI";
		 Long dau_pv2 =playGameManager.getEnterGameUserCount(_day,_day,qudao,null,null);//独立登陆
		 Long dau_uv2=playGameManager.getReg_LoginUserCount(_day,_day,qudao,null,null);//独立登陆（当天
		 Long new_pay2=chongZhiManager.getUnAheadChongZhiUserCount(_day,_day,qudao,null,null);//新付费人数
		 Long max_pcu2=onLineUsersCountManager.getMaxOnlineUserCount(_day,_day,qudao,null,null);//最高在线人数
		 String contentStr2= "day="+day+"&appkey=27d0f6f656017c38ae8df11b3b66a2b4" +
				"&dau_pv="+dau_pv2+"& dau_uv="+dau_uv2+"&new_pay="+new_pay2+"&max_pcu="+max_pcu2;
        String url2="http://dev.app.360.cn/mobileapi/saveapi";
        System.out.print("contentStr="+contentStr2);
        byte[] result=send(contentStr2,url2);
        logger.info("给360发送日报结果:"+new String(result));
	}
	
	public byte[] send(String contentstr,String urlStr)
	{ 
		Map headers = new HashMap();
		URL url;
		byte[] resContent=null;
		try {
		   url = new URL(urlStr+"?"+contentstr);
		//String content = URLEncoder.encode(contentstr,"utf-8");
		String content = URLEncoder.encode("","utf-8");
		resContent = HttpUtils.webPost(url,content.getBytes("utf-8"), headers, 5000, 5000);
		    logger.info("给360发送日报日志:"+"返回结果:"+new String(resContent));
		} catch (Exception e) {
			logger.info("给360发送日报日志:"+"[fail]",e);
		}
		return resContent;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
