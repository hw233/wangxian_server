<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,jxl.format.UnderlineStyle,jxl.write.WritableFont,jxl.Workbook,java.net.*"
	%>
    <%!
	Object lock = new Object(){};
      %>
    <%
     String flag = request.getParameter("flag");
     System.out.println("flag:"+flag);
     String startDay = request.getParameter("day");
	 String endDay = request.getParameter("endDay");
	
	 if(startDay == null)  startDay = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	 if(endDay == null) endDay = startDay;
	 
	 Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	 Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	 ArrayList<String> dayList = new ArrayList<String>();
	
	 synchronized(lock){
 	        while(s.getTime() <= t.getTime() + 3600000){
		       String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		       dayList.add(day);
		       s.setTime(s.getTime() + 24*3600*1000);
	        }
          }	
	  String spNumber=startDay+"TO"+endDay+"jiu";
	  
     if ("true".equals(flag)) {  //如果是刚打开页面，不产生下载文件
        TransactionManagerImpl transactionManager=TransactionManagerImpl.getInstance();
        
      
      ///////////////////////下载文件 ////////////////////////////////
        
        String basePath =getServletContext().getRealPath("/")+"excel";
          File thePath = new File(basePath);
          if (thePath.isDirectory() == false)// 如果该目录不存在，则生成新目录
          {
              thePath.mkdirs();
          }
          String xls= basePath+"/"+spNumber+".xls";
          System.out.println("xls:"+xls);
          File f=new File(xls);
          
         if(!f.exists()){
         String[]  jiuArray={"杏花村","屠苏酒","妙沁药酒"};
         
         
        jxl.write.WritableFont wfc = new jxl.write.WritableFont(WritableFont.ARIAL,6, WritableFont.NO_BOLD, false,
                     UnderlineStyle.NO_UNDERLINE);
          jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(wfc);
                  wcfFC.setWrap(true);   
          jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(f);
          
          for( int count=0;count<jiuArray.length;count++){
          //////////////////平均价格 start////////////
             String jiuName=jiuArray[count];
              jxl.write.WritableSheet ws = wwb.createSheet(jiuName+"平均价格", 0);
              jxl.write.Label labelC = new jxl.write.Label(0, 0, "分区");
              ws.addCell(labelC);
              for(int dayNum=0;dayNum<dayList.size();dayNum++){
                labelC = new jxl.write.Label(dayNum+1, 0, dayList.get(dayNum));
              ws.addCell(labelC);
           }
         for(int dayNum=0;dayNum<dayList.size();dayNum++){
              List<String []> ls=transactionManager.getJiuHuiZong(dayList.get(dayNum),jiuName);
              
              for(int j = 0; j < ls.size();j++){
                  String[] jius=(String [])ls.get(j);
                  if(dayNum==0)
                  {
                  labelC = new jxl.write.Label(0, j+1, jius[0]);
                  ws.addCell(labelC);
                  }
                  labelC = new jxl.write.Label(dayNum+1, j+1, jius[3]);
                  ws.addCell(labelC);
              }
              }
              //////////////////价格 end//////////// 
              
            //////////////////数量start////////////
              jxl.write.WritableSheet ws2 = wwb.createSheet(jiuName+"数量", 0);
               labelC = new jxl.write.Label(0, 0, "分区");
              ws2.addCell(labelC);
              for(int dayNum=0;dayNum<dayList.size();dayNum++){
                labelC = new jxl.write.Label(dayNum+1, 0, dayList.get(dayNum));
              ws2.addCell(labelC);
           }
         for(int dayNum=0;dayNum<dayList.size();dayNum++){
              List<String []> ls=transactionManager.getJiuHuiZong(dayList.get(dayNum),jiuName);
              
              for(int j = 0; j < ls.size();j++){
                  String[] jius=(String [])ls.get(j);
                  if(dayNum==0)
                  {
                  labelC = new jxl.write.Label(0, j+1, jius[0]);
                  ws2.addCell(labelC);
                  }
                  labelC = new jxl.write.Label(dayNum+1, j+1, jius[1]);
                  ws2.addCell(labelC);
              }
              }
              //////////////////数量 end//////////// 
            }
              
              
          wwb.write();
          wwb.close();
              }
         // request.setAttribute("num",spNumber);
         // return mapping.findForward("download");	
        
	//String num = request.getAttribute("num").toString();
    //response.setContentType("application/x-download");//设置为下载application/x-download
    response.reset();
    response.setContentType("binary/octet-stream");
    String filenamedownload ="/excel/"+spNumber+".xls";//即将下载的文件的相对路径
    String filenamedisplay = spNumber+".xls";//下载文件时显示的文件保存名称
    
    if(request.getHeader("User-Agent").toLowerCase().indexOf("firefox")>0){
    filenamedisplay=new String(filenamedisplay.getBytes("UTF-8"),"ISO8859-1");//firefox 解决中文名字乱码
    }
    else if(request.getHeader("User-Agent").toUpperCase().indexOf("MSIE")>0){
    filenamedisplay = URLEncoder.encode(filenamedisplay,"UTF-8");  //IE 解决中文名字乱码
    }
    response.addHeader("Content-Disposition","attachment;filename=" +filenamedisplay);
   	out.clear();
	out = pageContext.pushBody();
    try
    {
        RequestDispatcher dispatcher = application.getRequestDispatcher(filenamedownload);
        if(dispatcher != null)
        {
            dispatcher.forward(request,response);
        }
       response.flushBuffer();
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally
    {
   
    }		   
      
     }
  %>
		
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="../css/style.css" />
		<link rel="stylesheet" href="../css/atalk.css" />
		<script language="javascript" type="text/javascript" src="../js/flotr/lib/prototype-1.6.0.2.js"></script>
		<script language="javascript" type="text/javascript" src="../js/flotr/flotr-0.2.0-alpha.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/comfile/js/calendar.js"></script>
		
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			交易汇总下载
		</h2>
		<form name="form1" id="form1" method="post" >
		<input type='hidden' name='flag' value='true'/>
		 
		  开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:80px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 
		  <br/><br/>
		    		<input type='submit' value='提交'>
		    		</form>
		</center>
		<br>
		  <i>交易汇总下载页面</i>
		<br>
	</body>
</html>
