<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,java.util.*,
	com.xuanzhi.tools.text.*,com.sqage.stat.commonstat.entity.ChongZhi,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel,jxl.format.UnderlineStyle,
	jxl.write.WritableFont,jxl.Workbook,java.net.*"
	%>
  <%
	String startDay = request.getParameter("day");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	String username = request.getParameter("username");
	
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	
	/**
	*获得渠道信息
	**/
	          ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              //ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的分区信息
              List<Channel> channelList = cmanager.getChannels();//渠道信息
             
               String spNumber="wanjianQingDan"+startDay;
		      String sql="select t.* from stat_user t where trunc(t.registtime)=to_date('"+startDay+"','YYYY-MM-DD')";
		     
		      if(!"0".equals(qudao)){sql+=" and t.qudao='"+qudao+"' ";    spNumber+=qudao;}
	          if(!"0".equals(fenqu)){sql+=" and t.fenqu='"+fenqu+"'";     spNumber+=fenqu;}
	          if(username != null&&!"全部".equals(username)) {sql+=" and t.name='"+username+"'";    spNumber+=username;}
		      List<User> userList = userManager.getBySql(sql);
		 
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
          
              
              String sheet = "玩家信息清单";
              jxl.write.WritableSheet ws = wwb.createSheet(sheet, 0);
              jxl.write.Label labelC = new jxl.write.Label(0, 0, "名称");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(1, 0, "渠道");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(2, 0, "机型");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(3, 0, "地点");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(4, 0, "号码");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(5, 0, "IMEI");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(6, 0, "注册时间");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(7, 0, "UUID");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(8, 0, "国家");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(9, 0, "角色名");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(10, 0, "创建角色时间");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(11, 0, "分区");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(12, 0, "性别");
              ws.addCell(labelC);
              
              
             
              for(int j = 0; j < userList.size();j++){
                  User user = (User)userList.get(j);
                  labelC = new jxl.write.Label(0, j+1, user.getName());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(1, j+1, user.getQuDao());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(2, j+1, user.getJiXing());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(3, j+1, user.getDiDian());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(4, j+1, user.getHaoMa());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(5, j+1, user.getImei());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(6, j+1, user.getRegistTime().toString());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(7, j+1, user.getUuid());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(8, j+1, user.getGuojia());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(9, j+1, user.getPlayerName());
                  ws.addCell(labelC);
                  if(user.getCreatPlayerTime()!=null&&user.getCreatPlayerTime().toString().compareTo("2012-02-08")>0){
                  labelC = new jxl.write.Label(10, j+1, user.getCreatPlayerTime().toString());
                  ws.addCell(labelC);
                  }
                  labelC = new jxl.write.Label(11, j+1, user.getFenQu());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(12, j+1, user.getSex());
                  ws.addCell(labelC);
              }
              
              
              
          wwb.write();
          wwb.close();
         // request.setAttribute("num",spNumber);
         // return mapping.findForward("download");	
        
	//String num = request.getAttribute("num").toString();
   // response.setContentType("application/x-download");//设置为下载application/x-download
    //String filenamedownload ="/excel/"+spNumber+".xls";//即将下载的文件的相对路径
    //String filenamedisplay = spNumber+".xls";//下载文件时显示的文件保存名称
    //filenamedisplay = URLEncoder.encode(filenamedisplay,"UTF-8");
    //response.addHeader("Content-Disposition","attachment;filename=" + filenamedisplay);
    
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

