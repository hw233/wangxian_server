<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.GameChongZhi,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
    com.sqage.stat.commonstat.manager.Impl.*,jxl.format.UnderlineStyle,jxl.write.WritableFont,jxl.Workbook,java.net.*"
	%>
	
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String huoBiType=request.getParameter("huoBiType");
	String fenqu=request.getParameter("fenqu");
	String reasontype=request.getParameter("reasontype");
	String gamelevel = request.getParameter("gamelevel");
	
	String startNum= request.getParameter("startNum");
	String endNum=request.getParameter("endNum");
	String quDao="";
	String action=request.getParameter("action");
	
	String username=request.getParameter("username");
	
	
	if(huoBiType==null||"0".equals(huoBiType)){huoBiType="元宝";}
	if("0".equals(fenqu)){fenqu=null;}
	if("0".equals(reasontype)){reasontype=null;}
	if(gamelevel == null) gamelevel = "全部";
	if(startNum == null) startNum = "0";
	if(endNum == null) endNum = "0";
	if(action == null) action = "0";
	
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	
	/**
	*获得渠道信息
	**/
	ArrayList<String []> huoBiTypeList=null;
	ArrayList<String []> reasontypeist=null;
	
              //UserManagerImpl userManager=UserManagerImpl.getInstance();
              GameChongZhiManagerImpl gameChongZhiManager=GameChongZhiManagerImpl.getInstance();
              huoBiTypeList=gameChongZhiManager.getCurrencyType();
              reasontypeist=gameChongZhiManager.getReasontypeType();
             // ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
             
              String gamelevel_search=null;
              if(!"全部".equals(gamelevel)){gamelevel_search=gamelevel;}
              
		       //List goumaiList=gameChongZhiManager.getPlayerActionWatch(startDay,endDay,startNum,endNum,fenqu,quDao,gamelevel_search,huoBiType,reasontype,action);
		      
		      String sql=" select * from stat_gamechongzhi t where trunc(t.time) between to_date('"+startDay+"', 'YYYY-MM-DD') and to_date('"+endDay+"', 'YYYY-MM-DD')" +
				" and  t.username='" +username+"'";
				List userDetailList =gameChongZhiManager.getBySql(sql);
				
				String spNumber=username+startDay+"To"+endDay;
				
		  String basePath =getServletContext().getRealPath("/")+"excel";
          File thePath = new File(basePath);
          if (thePath.isDirectory() == false)// 如果该目录不存在，则生成新目录
          {
              thePath.mkdirs();
          }
          String xls= basePath+"/"+spNumber+".xls";
          File f=new File(xls);
          jxl.write.WritableFont wfc = new jxl.write.WritableFont(WritableFont.ARIAL,6, WritableFont.NO_BOLD, false,
                     UnderlineStyle.NO_UNDERLINE);
          jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(wfc);
                  wcfFC.setWrap(true);   
          jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(f);
              
              String sheet = username+"游戏币清单";
              jxl.write.WritableSheet ws = wwb.createSheet(sheet, 0);
              jxl.write.Label labelC = new jxl.write.Label(0, 0, "用户名");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(1, 0, "产生时间");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(2, 0, "分区");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(3, 0, "游戏级别");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(4, 0, "金额");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(5, 0, "得失类型");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(6, 0, "货币类型");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(7, 0, "产生原因");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(8, 0, "渠道");
              ws.addCell(labelC);
              
                 for(int i = 0 ; i < userDetailList.size() ; i++){
				
				  GameChongZhi gameChongZhi=(GameChongZhi)userDetailList.get(i);
				
				  labelC = new jxl.write.Label(0, i+1, gameChongZhi.getUserName());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(1, i+1, gameChongZhi.getTime().toString());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(2, i+1, gameChongZhi.getFenQu());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(3, i+1, gameChongZhi.getGameLevel());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(4, i+1, gameChongZhi.getMoney().toString());
                  ws.addCell(labelC);
                  
                  
                  labelC = new jxl.write.Label(5, i+1, gameChongZhi.getAction()==0?"获取":"消耗");
                  ws.addCell(labelC);
                  
                  
                  labelC = new jxl.write.Label(6, i+1, gameChongZhi.getCurrencyType());
                  ws.addCell(labelC);
                  
                  labelC = new jxl.write.Label(7, i+1, gameChongZhi.getReasonType());
                  ws.addCell(labelC);
                  
                  labelC = new jxl.write.Label(8, i+1, gameChongZhi.getQuDao());
                  ws.addCell(labelC);
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