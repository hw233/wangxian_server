package com.sqage.stat.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.mapping.Map;

import com.sqage.stat.commonstat.manager.Impl.ChongZhiManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.PlayGameManagerImpl;
import com.sqage.stat.httputil.jsonutil.JacksonManager;

public class Vip8TransferServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		long now =System.currentTimeMillis();
		String type = req.getParameter("type");////充值信息 1  用户登录信息  2
		String startDay = req.getParameter("startTime");
		String endDay = req.getParameter("endTime");
		String content=req.getParameter("serverName_username");
		List<String []> chongZhiList=null;
		String subsql="";
		if(content!=null)
		{
			content=URLDecoder.decode(content,"utf-8");

//			//content=URLDecoder.decode(URLEncoder.encode(content,"ISO-8859-1"),"utf-8");
//			System.out.println("content:ios--utf-8:"+URLDecoder.decode(URLEncoder.encode(content,"ISO-8859-1"),"utf-8"));
			subsql=content.replace(" ", "").replace(",", "").replace(";", "','");
		}

		Logger logger = Logger.getLogger(Vip8TransferServlet.class);
		if("1".equals(type)){//充值信息
		  ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
          String sql="select  c.username,c.fenqu,sum(c.money)/100 money,to_char(max(c.time),'YYYY-MM-DD hh24:mi:ss') lasttime from stat_chongzhi c "
            +" where c.time between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') ";
          sql += " and c.fenqu||c.username in ('"+subsql+"')"; 
          
           sql +=" group by c.username,c.fenqu "
            +" order by sum(c.money) desc";
            
            System.out.println("sql:"+sql);
            if(logger.isDebugEnabled()){ logger.debug(" sql:"+sql );}
 		   
            String[] columns= new String[4];
                 columns[0]="fenqu";
                 columns[1]="username";
                 columns[2]="lasttime";
                 //columns[3]="playname";
                 columns[3]="money";
                
		     chongZhiList=chongZhiManager.getChongZhiInfo(sql,columns);
		     logger.info("[access dbDate success] [OK] ["+chongZhiList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		        }else if ("2".equals(type)){//用户登录信息
			
			 String sql1="select t.fenqu,t.username,to_char(t.enterdate,'YYYY-MM-DD') enterdate,round(sum(t.onlinetime)/1000/60) fenzhong  " +
			 		" from stat_playgamevip t  where t.enterdate  " +
			 		" between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss')  and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss')  " +
			 		" and t.fenqu||t.username in ('"+subsql+"')  " +
			 		" group by t.fenqu,t.username,to_char(t.enterdate,'YYYY-MM-DD') ";
			 System.out.println("sql1:"+sql1);
			 if(logger.isDebugEnabled()){ logger.debug(" sql1:"+sql1 );}
		            String[] columns= new String[4];
		                 columns[0]="fenqu";
		                 columns[1]="username";
		                 columns[2]="enterdate";
		                 columns[3]="fenzhong";
		             chongZhiList= PlayGameManagerImpl.getInstance().getPlayGameData(sql1,columns);
		             logger.info("[access dbDate success] [OK] ["+chongZhiList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
		        }
		     JacksonManager jsmgr= JacksonManager.getInstance();
		     PrintWriter out = resp.getWriter();
		     
		     HashMap map=listToMap(chongZhiList,type);
		     String jsonStr=jsmgr.toJsonString(map);
		     out.print(jsonStr);
		    // out.print(jsmgr.toJsonString(chongZhiList));
		     if(logger.isDebugEnabled()){ logger.debug("[access dbDate success] [OK] ["+chongZhiList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");}
		     
		    //System.out.println(jsmgr.toJsonString(chongZhiList)); 
		 // response.setHeader("Content-Type","text/html;charset=UTF-8");// 发送消息头，通知浏览器以UTF-8打开内容
		    resp.setContentType("text/html;charset=UTF-8");
		    out.close(); 
		//resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected HashMap<String, HashMap<String, String>> listToMap(List<String[]> chongZhiList,String type)
	{
		Iterator<?> it=chongZhiList.iterator();
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		while(it.hasNext()){
			String[] strs=(String [])it.next();
				HashMap<String, String> mapson = new HashMap<String, String>();
				mapson.put("servername", strs[0]);
				mapson.put("username", strs[1]);
				
			if ("2".equals(type)) {
				mapson.put("lastLoginTime", strs[2]);
			} else {
				mapson.put("lastRechargeTime", strs[2]);
			}
			if ("2".equals(type)) {
				mapson.put("onlinetimes", strs[3]);
			} else {
				mapson.put("money", strs[3]);
			}
			String key="";
			if("1".equals(type)){
				    String timeStr=strs[2].substring(0, 10);
			        key=strs[0] +"_"+ strs[1]+"_"+timeStr;
			}else{
			    key=strs[0] +"_"+ strs[1]+"_"+strs[2];
			}
			
			map.put(key, mapson);
		}
		return map;
	}
}
