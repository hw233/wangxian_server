<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.sqage.stat.commonstat.entity.ChongZhi,com.sqage.stat.commonstat.dao.UserDao,
	com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,com.sqage.stat.commonstat.manager.Impl.*,
	com.sqage.stat.service.*,com.sqage.stat.model.Channel,jxl.format.UnderlineStyle,jxl.write.WritableFont,jxl.Workbook,java.net.*"
	%>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String qudao=request.getParameter("qudao");
	String fenqu=request.getParameter("fenqu");
	String type=request.getParameter("type");
	String cardtype=request.getParameter("cardtype");
	String username = request.getParameter("username");
	String money = request.getParameter("money");
	String jixing = request.getParameter("jixing");
	String modeType = request.getParameter("modeType");
	
	
	List<Channel> channelList = (ArrayList<Channel>)session.getAttribute("QUDAO_LIST");
	ArrayList<String []> fenQuList  =  (ArrayList<String []>) session.getAttribute("FENQU_LIST");
	ArrayList<String> typeList=  (ArrayList<String>) session.getAttribute("TYPE_LIST");
	ArrayList<String> cardtypeList =  (ArrayList<String>) session.getAttribute("CARDTYPE_LIST");
	
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay = today;
	if(endDay == null) endDay = today;
	
	String spNumber=startDay+"to"+endDay;
	
    if("0".equals(qudao)){qudao=null;}else{spNumber+=qudao;}
	if("0".equals(fenqu)){fenqu=null;}else{spNumber+=fenqu;}
	if("0".equals(type)){type=null;}  else{spNumber+=type;}
	if("0".equals(jixing)){jixing=null;}
	if("0".equals(modeType)){modeType=null;}
	if("0".equals(cardtype)){cardtype=null;}else{spNumber+=cardtype;}
	if(username == null) {username = "全部";}else if(!"全部".equals(username)){spNumber+=username;}
	if(money == null) {money = "全部";} else if(!"全部".equals(money)){spNumber+=money;}
	
	          ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
             
              if(channelList==null || channelList.size() == 0)
	           {
		         channelList = cmanager.getChannels();//渠道信息
		         session.removeAttribute("QUDAO_LIST");
		         session.setAttribute("QUDAO_LIST", channelList);
	           }
	         if(fenQuList==null)
	           {
		         fenQuList= userManager.getFenQu(null);//获得现有的分区信息
		         session.removeAttribute("FENQU_LIST");
		         session.setAttribute("FENQU_LIST", fenQuList);
	           }
	         
	         if(typeList==null){
	              typeList=chongZhiManager.getTypes(null);//获得现有的充值类型
	              session.removeAttribute("TYPE_LIST");
	              session.setAttribute("TYPE_LIST", typeList);
	           }
	         if(cardtypeList==null){
	              cardtypeList=chongZhiManager.getCardTypes();//获得现有的充值卡类型
	              session.removeAttribute("CARDTYPE_LIST");
	              session.setAttribute("CARDTYPE_LIST", cardtypeList);
	           }
	           
              String money_search=null;
              String username_search=null;
              if(!"全部".equals(username)){username_search=username;}
              if(!"全部".equals(money)){money_search=money;}
		     List chongZhiList=chongZhiManager.getChongZhi(startDay,endDay,qudao,fenqu,type,cardtype,username_search,money_search,jixing,modeType);
		   
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
          
              String sheet = "chongzhi";
              jxl.write.WritableSheet ws = wwb.createSheet(sheet, 0);
              jxl.write.Label labelC = new jxl.write.Label(0, 0, "名称");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(1, 0, "充值时间");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(2, 0, "分区");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(3, 0, "级别");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(4, 0, "付费金额");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(5, 0, "付费类型");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(6, 0, "支付卡类型");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(7, 0, "渠道");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(8, 0, "支付渠道手续费");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(9, 0, "平台");
              ws.addCell(labelC);
              labelC = new jxl.write.Label(10, 0, "设备型号");
              ws.addCell(labelC);
              
              
             
              for(int j = 0; j < chongZhiList.size();j++){
                  ChongZhi chognzhi=(ChongZhi)chongZhiList.get(j);
                  
                  labelC = new jxl.write.Label(0, j+1, chognzhi.getUserName());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(1, j+1, chognzhi.getTime().toString());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(2, j+1, chognzhi.getFenQu());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(3, j+1, chognzhi.getGameLevel());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(4, j+1, chognzhi.getMoney().toString());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(5, j+1, chognzhi.getType());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(6, j+1, chognzhi.getCardType());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(7, j+1, chognzhi.getQuDao());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(8, j+1, chognzhi.getCost());
                  ws.addCell(labelC);
                  labelC = new jxl.write.Label(9, j+1, chognzhi.getJixing());
                  ws.addCell(labelC);
                   labelC = new jxl.write.Label(10, j+1, chognzhi.getModelType());
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
%>
