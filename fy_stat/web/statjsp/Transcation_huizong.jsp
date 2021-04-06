<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,jxl.format.UnderlineStyle,jxl.write.WritableFont,jxl.Workbook,java.net.*"
	%>
    <%
     String flag = request.getParameter("flag");
	 String startDay = request.getParameter("day");
	 if(startDay == null)  startDay = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	
     if ("true".equals(flag)) {  //如果是刚打开页面，不产生下载文件
      TransactionManagerImpl transactionManager=TransactionManagerImpl.getInstance();
      List<String []> ls=transactionManager.getJiaoYiHuiZong(startDay);
      
      
      
      ///////////////////////下载文件 ////////////////////////////////
        String spNumber=startDay+"交易";
        String basePath =getServletContext().getRealPath("/")+"excel";
          File thePath = new File(basePath);
          if (thePath.isDirectory() == false)// 如果该目录不存在，则生成新目录
          {
              thePath.mkdirs();
          }
         // String xls= basePath+"\\"+spNumber+".xls";
          String xls= basePath+"/"+spNumber+".xls";
          File f=new File(xls);
          jxl.write.WritableFont wfc = new jxl.write.WritableFont(WritableFont.ARIAL,6, WritableFont.NO_BOLD, false,UnderlineStyle.NO_UNDERLINE);
          jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(wfc);
                  wcfFC.setWrap(true);   
          jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(f);
          
              String sheet = "交易汇总";
              jxl.write.WritableSheet ws = wwb.createSheet(sheet, 0);
              jxl.write.Label labelC = new jxl.write.Label(0, 0, "分区");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(1, 0, "交易类型");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(2, 0, "道具颜色");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(3, 0, "时间");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(4, 0, "道具名称");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(5, 0, "道具数量");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(6, 0, "总金额");
              ws.addCell(labelC);
          
             
              for(int j = 0; j < ls.size();j++){
                  String [] strs=(String [])ls.get(j);
                  
                  labelC = new jxl.write.Label(0, j+1, strs[0]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(1, j+1, strs[1]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(2, j+1, strs[2]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(3, j+1, strs[3]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(4, j+1, strs[4]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(5, j+1, strs[5]);
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(6, j+1, strs[6]);
                  ws.addCell(labelC);
                 
              }
          wwb.write();
          wwb.close();
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

     <script language="JavaScript">
         function searchGetType(dataStr) {
		     // document.getElementById("daojuname").value=daojuname;
		      $('form1').action="daojuTranscationGetType.jsp";
		      $('form1').submit();
	        }
    </script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			交易汇总下载
		</h2>
		<form name="form1" id="form1" method="post" >
		<input type='hidden' name='flag' value='true'/>
		下载统计日期：<input type='text' name='day' value='<%=startDay %>'>(格式：2012-02-09)
		  <br/><br/>
		    		<input type='submit' value='提交'>
		    		</form>
		</center>
		<br>
		  <i>交易汇总下载页面</i>
		<br>
	</body>
</html>
