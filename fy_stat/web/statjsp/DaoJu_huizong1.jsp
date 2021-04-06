<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,jxl.format.UnderlineStyle,jxl.write.WritableFont,jxl.Workbook,java.net.*"
	%>
	<%! 
	 /**
        * 把字符串续写到目标文件里，在原来文件里追加。
        */
        public boolean writing(String cont, File dist) {
         try {
          BufferedWriter writer = new BufferedWriter(new FileWriter(dist,true));
          writer.write(cont);
          writer.flush();
          writer.close();
          return true;
         } catch (IOException e) {
          e.printStackTrace();
          return false;
         }
        }
	 %>
    <%
     String flag = request.getParameter("flag");
	 String startDay = request.getParameter("day");
	 if(startDay == null)  startDay = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	
     if ("true".equals(flag)) {  //如果是刚打开页面，不产生下载文件
      
      ///////////////////////下载文件 ////////////////////////////////
        String spNumber=startDay+"daoJuDeShi";
        String basePath =getServletContext().getRealPath("/")+"excel";
          File thePath = new File(basePath);
          if (thePath.isDirectory() == false)// 如果该目录不存在，则生成新目录
          {
              thePath.mkdirs();
          }
         // String xls= basePath+"\\"+spNumber+".xls";
          String xls= basePath+"/"+spNumber+".txt";
          File f=new File(xls);
          
         System.out.println("f.exists():"+f.exists());
         if(!f.exists()){
         DaoJuManagerImpl daoJuManager=DaoJuManagerImpl.getInstance();
         List<String []> ls=daoJuManager.getDaoJuHuiZong(startDay);
              for(int j = 0; j < ls.size();j++){
                  String [] strs=(String [])ls.get(j);
                  String contentStr=strs[0]+"\t"+strs[1]+"\t"+strs[2]+"\t"+strs[3]+"\t"+strs[4]+"\t"+strs[5]+"\t"+strs[6]+"\t"+strs[7]+"\t"+strs[8]+"\r\n";
                  writing(contentStr,f);
              }
              }
         // request.setAttribute("num",spNumber);
         // return mapping.findForward("download");	
        
	//String num = request.getAttribute("num").toString();
    //response.setContentType("application/x-download");//设置为下载application/x-download
    response.reset();
    response.setContentType("binary/octet-stream");
    String filenamedownload ="/excel/"+spNumber+".txt";//即将下载的文件的相对路径
    String filenamedisplay = spNumber+".txt";//下载文件时显示的文件保存名称
    
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
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			道具汇总下载
		</h2>
		<form name="form1" id="form1" method="post" >
		<input type='hidden' name='flag' value='true'/>
		下载统计日期：<input type='text' name='day' value='<%=startDay %>'>(格式：2012-02-09)
		  <br/><br/>
		    		<input type='submit' value='提交'>
		    		</form>
		</center>
		<br>
		  <i>道具汇总下载页面</i>
		<br>
	</body>
</html>
