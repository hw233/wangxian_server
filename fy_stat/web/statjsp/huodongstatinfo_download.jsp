<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,jxl.format.UnderlineStyle,
	jxl.write.WritableFont,jxl.Workbook,java.net.*"
	%>
	
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
    String gamelevel = request.getParameter("gamelevel");
	String fenqu=request.getParameter("fenqu");
	String task=request.getParameter("task");
	    
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	String spNumber="hongDongShouYi"+startDay+"to"+endDay; 
	        
	if(gamelevel == null) gamelevel = "全部";
	if("0".equals(fenqu)){fenqu=null;}else{spNumber+=fenqu;}
	if("0".equals(task)){task=null;}else{spNumber+=task;}
	
              HuoDonginfoManagerImpl huoDonginfoManager=HuoDonginfoManagerImpl.getInstance();
              String gamelevel_search=null;
              if(!"全部".equals(gamelevel)){ gamelevel_search = gamelevel;spNumber+=gamelevel;}
              HashMap map= huoDonginfoManager.gethuoDonginfoShouYiDetailHashMap(startDay,endDay,fenqu,task,gamelevel_search);
             
          String basePath =getServletContext().getRealPath("/")+"excel";
          File thePath = new File(basePath);
          if (thePath.isDirectory() == false)// 如果该目录不存在，则生成新目录
          {
              thePath.mkdirs();
          }
         // String xls= basePath+"\\"+spNumber+".xls";
          String xls= basePath+"/"+spNumber+".xls";
          File f=new File(xls);
          jxl.write.WritableFont wfc = new jxl.write.WritableFont(WritableFont.ARIAL,6, WritableFont.NO_BOLD, false,
                     UnderlineStyle.NO_UNDERLINE);
          jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(wfc);
                  wcfFC.setWrap(true);   
          jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(f);
              
              String sheet = "活动收益统计";
              jxl.write.WritableSheet ws = wwb.createSheet(sheet, 0);
              jxl.write.Label labelC = new jxl.write.Label(0, 0, "收益物品名称");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(1, 0, "数量");
              ws.addCell(labelC);
            
             Iterator iter = map.entrySet().iterator();
             int i=0;
		           while (iter.hasNext()) 
		           {
		               Map.Entry entry = (Map.Entry) iter.next();
		               Object key = entry.getKey();
		               Object val = entry.getValue();
		               labelC = new jxl.write.Label(0, i+1, key.toString());
                       ws.addCell(labelC);
                       labelC = new jxl.write.Label(1, i+1, val.toString());
                       ws.addCell(labelC);
                       i++;
		            }
             
          wwb.write();
          wwb.close();
		
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
%>