package com.sqage.stat.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sqage.stat.commonstat.manager.Impl.ChongZhiManagerImpl;
import com.sqage.stat.httputil.jsonutil.JacksonManager;

public class Caiwu_rechargeChannelServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		long now =System.currentTimeMillis();
		String time = req.getParameter("time");
		List<String []> chongZhiList=null;
		Logger logger = Logger.getLogger(Caiwu_rechargeTypeServlet.class);
		
			String sql="select t.QUDAO,sum(t.money) money,sum(t.cost) cost  from stat_chongzhi t where t.time between " +
					"to_date('"+time+" 00:00:00','YYYY-MM-DD hh24:mi:ss') and " +
					"to_date('"+time+" 23:59:59','YYYY-MM-DD hh24:mi:ss')  " +
					//" and t.type in ('支付宝','易宝','神州付','天猫商城','网站充值','Wap支付宝')" +
							"group by t.QUDAO";
			
            if(logger.isDebugEnabled()){ logger.debug(" sql:"+sql );}
            String[] columns= new String[3];
                 columns[0]="QUDAO";
                 columns[1]="money";
                 columns[2]="cost";
                
                 ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
                 chongZhiList=chongZhiManager.getChongZhiInfo(sql,columns);
		       
		     JacksonManager jsmgr= JacksonManager.getInstance();
		     PrintWriter out = resp.getWriter();
		     HashMap<String,Object> ls=listToMap(chongZhiList);
		     
		     String jsonStr=jsmgr.toJsonString(ls);
		    // System.out.println("jsonStr:"+jsonStr);
		     out.print( jsonStr);
		     //out.print( URLEncoder.encode(jsonStr,"utf-8"));
		     if(logger.isDebugEnabled()){ logger.debug("[access dbDate success] [OK] ["+chongZhiList.size()+"条] [cost:"+(System.currentTimeMillis()-now)+"ms]");}
		    resp.setContentType("text/html;charset=UTF-8");
		    out.close(); 
		//resp.sendRedirect("/");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected  HashMap<String,Object> listToMap(List<String []> chongZhiList)
	{
		HashMap<String,Object> resutlMap =new HashMap<String,Object>();
		
		List dataList=new ArrayList<HashMap<String,Object>>();
		if(chongZhiList!=null){
		for(int i=0;i<chongZhiList.size();i++)
		{
			String[] sts=chongZhiList.get(i);
			String type=sts[0];
			String money=sts[1];
			String cost=sts[2];
			
			HashMap<String,Object> oneDate =new HashMap<String,Object>();
			oneDate.put("CHANNELNAME", type);
			oneDate.put("MONEY", money);
			oneDate.put("TONGDAOFEI", cost);
			
			dataList.add(oneDate);
		}
		}
		resutlMap.put("list", dataList);
		resutlMap.put("success", "true");
		resutlMap.put("msg", chongZhiList.size()==0?"查询数据为空，请检检查你的参数":"查询数据正常");
		
		return resutlMap;
	}
}