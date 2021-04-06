package com.sqage.stat.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.manager.Impl.ChongZhiManagerImpl;
import com.sqage.stat.commonstat.manager.Impl.UserManagerImpl;
import com.sqage.stat.httputil.jsonutil.JacksonManager;
import com.sqage.stat.model.Channel;
import com.sqage.stat.service.ChannelManager;

public class VipTransferServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		long now =System.currentTimeMillis();
		String startDay = req.getParameter("day");
		String endDay = req.getParameter("endDay");
		String vip=req.getParameter("vip");
		String moneyStart = req.getParameter("moneyStart");
		String moneyEnd = req.getParameter("moneyEnd");
		String qudao=req.getParameter("qudao");
		String fenqu=req.getParameter("fenqu");
//		if(fenqu!=null){
//			System.out.println(fenqu);
//			System.out.println(URLDecoder.decode(fenqu,"utf-8"));
//		}
		Logger logger = Logger.getLogger(VipTransferServlet.class);
		List<Channel> channelList = (ArrayList<Channel>)req.getSession().getAttribute("QUDAO_LIST");
		ArrayList<String []> fenQuList  =  (ArrayList<String []>) req.getSession().getAttribute("FENQU_LIST");
		
		  if(channelList==null || channelList.size() == 0)
		           {
		             ChannelManager cmanager = ChannelManager.getInstance();
			         channelList = cmanager.getChannels();//渠道信息
			         req.getSession().removeAttribute("QUDAO_LIST");
			         req.getSession().setAttribute("QUDAO_LIST", channelList);
		           }
		  if(fenQuList==null)
          {
			 UserManagerImpl userManager=UserManagerImpl.getInstance();
	         fenQuList= userManager.getFenQuByStatus("0");//获得现有的分区信息
	         req.getSession().removeAttribute("FENQU_LIST");
	         req.getSession().setAttribute("FENQU_LIST", fenQuList);
          }
		
		  ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
          String sql="select  c.username,c.fenqu,c.playname,c.qudao,sum(c.money)/100 money,max(c.vip) vip from stat_chongzhi c "
            +" where c.time between to_date('"+startDay+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and to_date('"+endDay+" 23:59:59','YYYY-MM-DD hh24:mi:ss') ";
           
          if(qudao!=null&&!"0".equals(qudao)){sql += " and c.qudao = '"+qudao+"'"; }
          //if(fenqu!=null&&!"0".equals(fenqu)){sql += " and c.fenqu = '"+URLDecoder.decode(URLEncoder.encode(fenqu,"ISO-8859-1"),"utf-8")+"'"; }
          if(fenqu!=null&&!"0".equals(fenqu)){sql += " and c.fenqu = '"+fenqu+"'"; }
          
           sql +=" group by c.username,c.fenqu,c.playname,c.qudao  having max(c.vip)="+vip
            +" and sum(c.money)/100 between "+moneyStart+" and "+moneyEnd
            +" order by sum(c.money) desc";
            
           // System.out.println("sql:"+sql);
            String[] columns= new String[6];
                 columns[0]="username";
                 columns[1]="fenqu";
                 columns[2]="playname";
                 columns[3]="qudao";
                 columns[4]="money";
                 columns[5]="vip";
		     List<String []> chongZhiList=chongZhiManager.getChongZhiInfo(sql,columns);
		     logger.info("[access dbDate success] [OK] ["+chongZhiList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");
			    
		     JacksonManager jsmgr= JacksonManager.getInstance();
		     PrintWriter out = resp.getWriter();
		     out.print(jsmgr.toJsonString(chongZhiList));
		     
		    System.out.println(jsmgr.toJsonString(chongZhiList)); 
		    
		 // response.setHeader("Content-Type","text/html;charset=UTF-8");// 发送消息头，通知浏览器以UTF-8打开内容
		    resp.setContentType("text/html;charset=UTF-8");
		    out.close(); 
		//resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

}
