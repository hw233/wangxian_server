package com.sqage.stat.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.language.MultiLanguageManager;
import com.xuanzhi.tools.mail.JavaMailUtils;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.timer.Executable;

public class DailySendEmailManager implements Executable {

    protected static DailySendEmailManager m_self = null;
    
	protected static final Log logger = LogFactory.getLog(DailySendEmailManager.class);
	public static DailySendEmailManager getInstance() {
			return m_self;
		}
	  
	public void init() throws Exception{
		m_self = this;
		logger.info("["+DailySendEmailManager.class.getName()+"] [initialized]");
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
	
		sendEmail();
	}
	
	public void sendEmail()
	{
		try {
			//InternetAddress[] to=InternetAddress.parse("xuyong@sqage.com,yanchao@sqage.com,zhangjianqin@sqage.com");
			
			   InternetAddress[] to=InternetAddress.parse("yangxiaoliang@sqage.com,zhangshanlin@sqage.com,wangxu@sqage.com");
			   InternetAddress[] cc=InternetAddress.parse("jieyunheng@sqage.com,jiyatai@sqage.com");
			   //InternetAddress[] bcc=null;
			   InternetAddress[] bcc=InternetAddress.parse("zhanghuanyu@sqage.com");
			   
			   String dailyDay = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
			   String dailyExcelBase=MultiLanguageManager.translateMap.get("dailyExcelBase");
			   //String dailyExcelBase="D:/sever/apache-tomcat-6.0.16.other/webapps/stat_common_mieshi/excel/daily";   //日报存放目录
			   String xls= dailyExcelBase+"/"+dailyDay+"daily.xls";//日报文件名称
			   //String xls= "D:\\2014-12-10"+"daily.xls";//日报文件名称
			   String[] attachments={xls};
			   String content="<font color='blue'>你好:<br>&nbsp;&nbsp;&nbsp;&nbsp;这是忘仙日报，详细信息请参考附件。<br><br>&nbsp;&nbsp;&nbsp;&nbsp;本邮件和邮寄信息均由系统自动生成,目前在测试和完善阶段，如果有反馈信息，请和技术人员联系。<br>" +
			   		" <br><br><br><br>&nbsp;&nbsp;&nbsp;&nbsp;---介运恒</font>" ;
			    JavaMailUtils.sendMail("smtp.126.com", to, cc, bcc, dailyDay+"忘仙日报", content,"text/html;charset=gbk",
					attachments, "lucky@126.com", "lucky5tar", "123123123");
			
		   } catch (Exception e) {
			   logger.error("定时日报发送异常",e);
			e.printStackTrace();
		}
	}
	
	

public static void main(String[] args) throws ParseException
   {
	DailySendEmailManager dailySendEmailManager=new DailySendEmailManager();
	dailySendEmailManager.sendEmail();
	}
}
