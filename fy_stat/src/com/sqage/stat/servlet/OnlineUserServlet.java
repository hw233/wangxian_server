package com.sqage.stat.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.manager.Impl.OnLineUsersCountManagerImpl;
import com.sqage.stat.httputil.jsonutil.JacksonManager;

public class OnlineUserServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		long now =System.currentTimeMillis();
		String serverName = req.getParameter("serverName");//服务器
		String startDay = req.getParameter("startTime");
		String endDay = req.getParameter("endTime");
		List<String []> onLineUsersCountList=null;
		String subsql="";
		if(serverName!=null)
		{
			 serverName=URLDecoder.decode(serverName,"utf-8");
			
			//String content=URLDecoder.decode(URLEncoder.encode(serverName,"ISO-8859-1"),"utf-8");
//			System.out.println("content:ios--utf-8:"+URLDecoder.decode(URLEncoder.encode(content,"ISO-8859-1"),"utf-8"));
			
			subsql=" and so.fenqu ='"+serverName+"'";
		}
		Logger logger = Logger.getLogger(OnlineUserServlet.class);
			
		String startmin=startDay.substring(11,16).replace(":", "-");
		String endmin=endDay.substring(11,16).replace(":", "-");
		
			String sql="select nn.fenqu,mm.timestamp,nn.usercount from (select p.timestamp from dt_timestamp p where p.type=1 "
				+" and p.timestamp between '"+startmin+"' and '"+endmin+"') mm "
				+" left join ( select so.fenqu,to_char(so.onlinedate,'HH24-mi') as onlinedate,sum(so.usercount) as usercount "
				+" from stat_onlineusers so where so.onlinedate between to_date('"+startDay+":00','YYYY-MM-DD hh24:mi:ss') "
				+" and to_date('"+endDay+":59','YYYY-MM-DD hh24:mi:ss') "+subsql+" group by so.fenqu,to_char(so.onlinedate,'HH24-mi') "
				+" order by so.fenqu,to_char(so.onlinedate,'HH24-mi') "
				+" ) nn   on mm.timestamp =nn.onlinedate";
			
            System.out.println("sql:"+sql);
            if(logger.isDebugEnabled()){ logger.debug(" sql:"+sql );}
            OnLineUsersCountManagerImpl OnLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
            String[] columns= new String[3];
                 columns[0]="fenqu";
                 columns[1]="timestamp";
                 columns[2]="usercount";
                 
                 onLineUsersCountList =OnLineUsersCountManager.getOnlineInfo(sql,columns);
		     //logger.info("[access dbDate success] [OK] ["+OnLineUsersCountList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		       
		     JacksonManager jsmgr= JacksonManager.getInstance();
		     PrintWriter out = resp.getWriter();
		     HashMap<String, List<String[]>> mapson=listToMap(onLineUsersCountList);
		     HashMap<String,Object> ls=listToMap(mapson);
		     
		     String jsonStr=jsmgr.toJsonString(ls);
		    // out.print( URLEncoder.encode(jsonStr,"utf-8"));
		     System.out.println("jsonStr:"+jsonStr);
		     out.print( URLEncoder.encode(jsonStr,"utf-8"));
		     if(logger.isDebugEnabled()){ logger.debug("[access dbDate success] [OK] ["+onLineUsersCountList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");}
		    resp.setContentType("text/html;charset=UTF-8");
		    out.close(); 
		//resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected HashMap<String, List<String[]>> listToMap(List<String []> onLineUsersCountList)
      {
		List<String[]> fenquList = new ArrayList<String[]>(); // 装每个分区的数据分开
		HashMap<String, List<String[]>> mapson = new HashMap<String, List<String[]>>();
		String tempFenqu = "";// 临时存放分区的名称
		Iterator<?> it = onLineUsersCountList.iterator();

		while (it.hasNext()) {
		
			String[] strs = (String[]) it.next();
			if(strs[0]!=null&&!"".equals(strs[0])){
			if (tempFenqu.equals(strs[0])) {
				fenquList.add(strs);
			} else {
				if(!"".equals(tempFenqu)){ mapson.put(tempFenqu, fenquList);    }
				tempFenqu = strs[0];
				fenquList = new ArrayList<String[]>(); // 装每个分区的数据分开
				fenquList.add(strs);
			}
			}
			if (fenquList != null && fenquList.size() != 0) {
				mapson.put(tempFenqu, fenquList);
			}
		}
		return mapson;
 
     }
	
	protected HashMap<String,Object>  listToMap(HashMap<String, List<String[]>> mapson)
	{
		List<Object> ls=new ArrayList<Object>();
		HashMap<String,Object> resutlMap =new HashMap<String,Object>();
		Iterator<Map.Entry<String, List<String[]>>> iterator = mapson.entrySet().iterator();//所有数据
		int num=0;
		List<String> timeLs=new ArrayList<String>();
		
		while (iterator.hasNext()) {
			Map.Entry<String, List<String[]>> entry = iterator.next();//获取一个服务器的数据
			String fenqu=entry.getKey();
			List<String[]> datas=entry.getValue();
			
			
			HashMap<String,Object> jsonMap =new HashMap<String,Object>();
			jsonMap.put("title", fenqu);                              //放入分区
			List<String> dataLs=new ArrayList<String>();
			for(int i=0;i<datas.size();i++){
				String[] data=datas.get(i);
				String fenq=data[0];
				String onlinedate=data[1];
				if(num==0){   timeLs.add(onlinedate);  }
				String usercount=data[2];
				dataLs.add(usercount);
			}
			num++;
			jsonMap.put("list", dataLs);
			ls.add(jsonMap);
		}
		resutlMap.put("dataList", ls);
		HashMap<String,Object> timeMap =new HashMap<String,Object>();
//		timeMap.put("xAxisObj", timeLs);
		resutlMap.put("xAxisObj", timeLs);
//		ls.add(timeMap);
		return resutlMap;
	}
	
//	/**
//	 * 把时间转换每隔10一个段的串
//	 * @param starttimeStr
//	 * @param endtimeStr
//	 * @return
//	 * @throws ParseException
//	 */
//	protected  HashMap<String,String> timeStr(String starttimeStr,String endtimeStr) throws ParseException
//	{
//		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		   //String currentDate = sdf.format(new Date());
//		   Date startDate = sdf.parse(starttimeStr);
//		   Date endDate = sdf.parse(endtimeStr);
//		   HashMap<String, String> timeMap = new HashMap<String, String>();
//		   while(startDate.before(endDate))
//		   {
//			   Calendar cal = Calendar.getInstance();
//				cal.setTime(startDate);
//				cal.add(Calendar.MINUTE, 10);
//				startDate = cal.getTime();
//				String hh_minit=DateUtil.formatDate(startDate, "HH:mm"); 
//				timeMap.put(hh_minit.replace(":", "-"), hh_minit);
//		   }
//		
//		return timeMap;
//	}
}
