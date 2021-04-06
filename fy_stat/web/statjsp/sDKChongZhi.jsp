<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	com.xuanzhi.tools.text.*,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.User,com.sqage.stat.commonstat.manager.UserManager,
	com.sqage.stat.commonstat.manager.Impl.*"
	%>
	<%!
	Object lock = new Object(){};
	ApplicationContext ctx=null;
      %>
  <%
	String startDay = request.getParameter("day");
	String endDay = request.getParameter("endDay");
	String today = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
	if(startDay == null) startDay =today;
	if(endDay == null) endDay =today;
	Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	ArrayList<String> dayList = new ArrayList<String>();
	
	/**
	*获得渠道信息
	**/
		if(ctx==null){
		    ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		    }
		   ChongZhiManagerImpl chongZhiManager= ChongZhiManagerImpl.getInstance();
            UserManagerImpl userManager=(UserManagerImpl)ctx.getBean("UserManager");
            PlayGameManagerImpl playGameManager=(PlayGameManagerImpl)ctx.getBean("PlayGameManager");
           
synchronized(lock){
	while(s.getTime() <= t.getTime() + 3600000){
		String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		dayList.add(day);
		s.setTime(s.getTime() + 24*3600*1000);
	}
}	

	
	//对比各个渠道数据

	ArrayList<String> realChannelList = new ArrayList<String>();
	//某个渠道，注册日期
			
			
			realChannelList.add("3GSDK_MIESHI");
			realChannelList.add("553SDK_MIESHI");
			realChannelList.add("553SDK01_MIESHI");
			realChannelList.add("553SDK02_MIESHI");
			realChannelList.add("553SDK03_MIESHI");
			realChannelList.add("553SDK04_MIESHI");
			realChannelList.add("553SDK05_MIESHI");
			realChannelList.add("553SDK06_MIESHI");
			realChannelList.add("553SDK07_MIESHI");
			realChannelList.add("553SDK08_MIESHI");
			realChannelList.add("553SDK09_MIESHI");
			realChannelList.add("553SDK10_MIESHI");
			realChannelList.add("553SDK11_MIESHI");
			realChannelList.add("553SDK12_MIESHI");
			realChannelList.add("553SDK13_MIESHI");
			realChannelList.add("553SDK14_MIESHI");
			realChannelList.add("553SDK15_MIESHI");
			realChannelList.add("553SDK16_MIESHI");
			realChannelList.add("553SDK17_MIESHI");
			realChannelList.add("553SDK18_MIESHI");
			realChannelList.add("553SDK19_MIESHI");
			realChannelList.add("553SDK20_MIESHI");
			realChannelList.add("553SDK21_MIESHI");
			realChannelList.add("553SDK22_MIESHI");
			realChannelList.add("553SDK23_MIESHI");
			realChannelList.add("553SDK24_MIESHI");
			realChannelList.add("553SDK25_MIESHI");
			realChannelList.add("553SDK26_MIESHI");
			realChannelList.add("553SDK27_MIESHI");
			realChannelList.add("553SDK28_MIESHI");
			realChannelList.add("553SDK29_MIESHI");
			realChannelList.add("553SDK30_MIESHI");
			realChannelList.add("139SDK_MIESHI");
			realChannelList.add("139NEWSDK_MIESHI");
			
			
			realChannelList.add("139SDK01_MIESHI");
			realChannelList.add("139SDK02_MIESHI");
			realChannelList.add("139SDK03_MIESHI");
			realChannelList.add("139SDK04_MIESHI");
			realChannelList.add("139SDK05_MIESHI");
			realChannelList.add("139SDK06_MIESHI");
			realChannelList.add("139SDK07_MIESHI");
			realChannelList.add("139SDK08_MIESHI");
			realChannelList.add("139SDK09_MIESHI");
			
			realChannelList.add("appchinasdk_MIESHI");
			
			realChannelList.add("ADHOME_MIESHI");
			realChannelList.add("ANZHISDK_MIESHI");
			realChannelList.add("ANZHISDK01_MIESHI");
			realChannelList.add("ANZHISDK02_MIESHI");
			
			
			
			realChannelList.add("AMAZON_MIESHI");
			realChannelList.add("AIYOUSDK_MIESHI");
			realChannelList.add("appchina_MIESHI");
			realChannelList.add("APPSTORE_MIESHI_WALI");
			realChannelList.add("APPSTORE_MIESHI_JINSHAN");
			realChannelList.add("APPSTORE_MIESHI");
			realChannelList.add("APPSTORE_MIESHI_ADSAGE");
			realChannelList.add("APPSTORE_MIESHI_TAPJOY");
			realChannelList.add("APPSTORE_MIESHI_PINGGUOYUAN");
			realChannelList.add("APPSTORE_MIESHI_RUANLIE");
			realChannelList.add("APPSTOREGUOJI_MIESHI");
			
			realChannelList.add("APPSTORE_MIESHI_ANWO");
			realChannelList.add("APPSTORE_MIESHI_YOUMI");
			realChannelList.add("appchina02_MIESHI");
			
			realChannelList.add("SINAWEI_MIESHI");
			realChannelList.add("baoruan_MIESHI");
			realChannelList.add("BAORUAN_MIESHI");
			realChannelList.add("BAIDUSDK_MIESHI");
			
			
			realChannelList.add("lenovo_MIESHI");
			realChannelList.add("lenovosdk_MIESHI");
			realChannelList.add("LENOVOYX_MIESHI");
			
            realChannelList.add("DCN_MIESHI");
            realChannelList.add("DCN01_MIESHI");
            realChannelList.add("DCN02_MIESHI");
            realChannelList.add("DCN03_MIESHI");
            realChannelList.add("DCN04_MIESHI");
			realChannelList.add("DCN05_MIESHI");
			realChannelList.add("DCN06_MIESHI");
			realChannelList.add("DCN07_MIESHI");
			realChannelList.add("DCN08_MIESHI");
			realChannelList.add("DCN09_MIESHI");
			realChannelList.add("DCN10_MIESHI");
			realChannelList.add("DCN11_MIESHI");
			realChannelList.add("DCN12_MIESHI");
			realChannelList.add("DCN13_MIESHI");
			realChannelList.add("DCN14_MIESHI");
			realChannelList.add("DCN15_MIESHI");
			realChannelList.add("DCN16_MIESHI");
			realChannelList.add("DCN17_MIESHI");
			realChannelList.add("DCN18_MIESHI");
			realChannelList.add("DCN19_MIESHI");
			realChannelList.add("DCN20_MIESHI");
			realChannelList.add("DCN21_MIESHI");
			realChannelList.add("DCN22_MIESHI");
			realChannelList.add("DCN23_MIESHI");
			realChannelList.add("DCN24_MIESHI");
			realChannelList.add("DCN25_MIESHI");
			realChannelList.add("DCN26_MIESHI");
			realChannelList.add("DCN27_MIESHI");
			realChannelList.add("DCN28_MIESHI");
			realChannelList.add("DCN29_MIESHI");
			realChannelList.add("DCN30_MIESHI");
			
			realChannelList.add("duokuApi_MIESHI");
			realChannelList.add("duokuApi01_MIESHI");
			
			
			realChannelList.add("BAIDUYY_MIESHI");
			realChannelList.add("BAIDUYY01_MIESHI");
			
			
			
			realChannelList.add("KUAIYONGSDK_MIESHI");
			realChannelList.add("lenovonewsdk_MIESHI");
			realChannelList.add("LEDOU_MIESHI");
			realChannelList.add("LENOVOMMNEW_MIESHI");
			
			realChannelList.add("MM_MIESHI");
			realChannelList.add("MUMAYISDK_MIESHI");
			
			realChannelList.add("MEIZUSDK_MIESHI");
			realChannelList.add("MEIZUNEWSDK_MIESHI");
			

			realChannelList.add("NEWUCSDK_MIESHI");
			realChannelList.add("NEWWDJSDK_MIESHI");
			
			realChannelList.add("NEWMMSDK_MIESHI");
			
			realChannelList.add("NEW91SDK_MIESHI");
			realChannelList.add("NEWBAIDUSDK_MIESHI");
			
			realChannelList.add("GOOGLEINAPPBILLING_MIESHI");
			
			realChannelList.add("UC_MIESHI");
			realChannelList.add("UCNEWSDK_MIESHI");
			realChannelList.add("UCSDK_MIESHI");
			realChannelList.add("UC01_MIESHI");
			realChannelList.add("UC02_MIESHI");
			realChannelList.add("UC03_MIESHI");
			realChannelList.add("UC04_MIESHI");
			realChannelList.add("UC05_MIESHI");
			realChannelList.add("UC06_MIESHI");
			realChannelList.add("UC07_MIESHI");
			realChannelList.add("UC08_MIESHI");
			realChannelList.add("UC09_MIESHI");
			realChannelList.add("UC10_MIESHI");
			realChannelList.add("UC11_MIESHI");
			
			realChannelList.add("UC17_MIESHI");
			realChannelList.add("UC18_MIESHI");
			realChannelList.add("UC23_MIESHI");
			realChannelList.add("UNIPAYSDK_MIESHI");
			
			
			realChannelList.add("91ZHUSHOU_MIESHI");
 
            realChannelList.add("feiliu_MIESHI");
			realChannelList.add("feiliu2_MIESHI");
			realChannelList.add("feiliu3_MIESHI");
			realChannelList.add("feiliu4_MIESHI");
			realChannelList.add("feiliu5_MIESHI");
			realChannelList.add("feiliu6_MIESHI");
			
			realChannelList.add("GFENGSDK_MIESHI");
			
			
			realChannelList.add("PPZHUSHOU_MIESHI");
			realChannelList.add("TBTSDK_MIESHI");
			realChannelList.add("TBTNEWSDK_MIESHI");
			
			realChannelList.add("360SDK_MIESHI");
			realChannelList.add("360JIEKOU_MIESHI");
			realChannelList.add("360JIEKOU01_MIESHI");
			realChannelList.add("360SDK01_MIESHI");
			realChannelList.add("360NEWSDK_MIESHI");
			
			
			
			realChannelList.add("XIAOMISDK_MIESHI");
			realChannelList.add("XIAOMISDK02_MIESHI");
			realChannelList.add("SAMSUNGSDK_MIESHI");
			
			realChannelList.add("SINAWEINEW_MIESHI");
			realChannelList.add("SINAWEINEW01_MIESHI");
			realChannelList.add("SINAWEINEW02_MIESHI");
			realChannelList.add("SINAWEINEW03_MIESHI");
			realChannelList.add("SINAWEINEW04_MIESHI");
			realChannelList.add("SINAWEINEW05_MIESHI");
			
			
			realChannelList.add("SOGOU_MIESHI");
			
			realChannelList.add("HUAWEISDK02_MIESHI");
			realChannelList.add("HUAWEISDK_MIESHI");
			realChannelList.add("HUAWEINEWSDK_MIESHI");
			
			
			
			
			realChannelList.add("KUPAI_MIESHI");
			realChannelList.add("KUPAI01_MIESHI");
			realChannelList.add("KUPAI02_MIESHI");
			realChannelList.add("KAOPU_MIESHI");
			
			realChannelList.add("IAPPPAY_MIESHI");
			realChannelList.add("IAPPPAY01_MIESHI");
			realChannelList.add("IAPPPAY02_MIESHI");
			realChannelList.add("IAPPPAY03_MIESHI");
			realChannelList.add("IAPPPAY04_MIESHI");
			realChannelList.add("IAPPPAY05_MIESHI");
			
			realChannelList.add("V3HUAWEISDK_MIESHI");
			realChannelList.add("XIAOMISDK03_MIESHI");
			realChannelList.add("YOUXIQUNSDK_MIESHI");
			realChannelList.add("YIDONGMM_MIESHI");
			realChannelList.add("WALISDK_MIESHI");
			realChannelList.add("WANDOUJIASDK_MIESHI");
			
		float channel_regday_nums[][] = new float[0][0];
		channel_regday_nums = new float[realChannelList.size()][dayList.size()];
      for(int k = 0 ; k < dayList.size() ; k++){
				String _day = dayList.get(k);
				
				ArrayList<String> qudaoshuList = null;
				if(qudaoshuList == null){ 
				qudaoshuList =new ArrayList<String>();
				//qudaoshuList =playGameManager.getGuojiaActivityUserCount(_day);
				qudaoshuList=chongZhiManager.getSDKChongZhi(_day);
				}
		for(int j = 0 ; j < realChannelList.size() ; j++){
			String _channel = realChannelList.get(j);
				for(int l = 0 ; l < qudaoshuList.size() ; l++){
					String ss[] = qudaoshuList.get(l).split(" ");
					if(ss[0].equals(_channel)){
					channel_regday_nums[j][k] =Float.parseFloat(ss[1])/100;
					
					}
				}
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
var mycars=[<% for(int i = 0 ; i < dayList.size() ; i++){ out.print("\""+dayList.get(i)+"\",");} %>"E"];
 function drawRegUserFlotr(){
		    var f = Flotr.draw(
		$('regUserContainer'), [
		<%for(int j = 0 ; j < realChannelList.size() ; j++){
			StringBuffer sb2 = new StringBuffer();
			sb2.append("[");
			for(int k = 0 ; k < channel_regday_nums[j].length ; k++){
				sb2.append("["+k+","+channel_regday_nums[j][k]+"]");
				if(k < channel_regday_nums[j].length -1) sb2.append(",");
			}
			sb2.append("]");
			out.println("{");
			out.println("data:"+sb2.toString()+",");
			out.println("label:'"+realChannelList.get(j)+"'");
			out.println("}");
			if(j < realChannelList.size()-1) out.print(",");
		}
		%>
		],{
			xaxis:{
				noTicks: <%=dayList.size()%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%= dayList.size()%>
			},
			yaxis:{
				tickFormatter: function(n){ return (n);}, // =>
				min: 0,
			},
			legend:{
				position: 'ne', // => position the legend 'south-east'.
				backgroundColor: '#D2E8FF' // => a light blue background color.
			},
			mouse:{
				track: true,
				color: 'purple',
				sensibility: 1, // => distance to show point get's smaller
				trackDecimals: 2,
				trackFormatter: function(obj){ return 'y = ' + obj.y; }
			}
		}
	);
	}

</script>
	</head>
	<body>
		<center>
		<h2 style="font-weight:bold;">
			渠道商支付充值汇总
		</h2>
		<form>开始日期：<input type='text' name='day' value='<%=startDay %>'>
		-- 结束日期：<input type='text' name='endDay' value='<%=endDay %>'>(格式：2012-02-09)
		<input type='submit' value='提交'> </form>
		<br/>
		</center>
		<br/>
		<center>
		    		<h3>渠道商支付充值汇总</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
					
					long[] zonshu=new long[realChannelList.size()];
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("<td>汇总(元)</td></tr>");
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						long count = 0;
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			count+=(long)channel_regday_nums[c][i];
			    			zonshu[c]+=channel_regday_nums[c][i];
			    		}
						
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+channel_regday_nums[c][i]+"</td>");
			    		}
			    		out.print("<td bgcolor='#EEEEBB'>"+count+"</td></tr>");
					}
					
				    out.print("<tr bgcolor='#FFFFFF'><td bgcolor='#EEEEBB'>总和</td>");
					long sum=0;
					for(int h=0;h<zonshu.length;h++)
					{
					out.print("<td bgcolor='#EEEEBB'>"+zonshu[h]+"</td>");
					sum+=(long)zonshu[h];
					}
					out.print("<td bgcolor='#EEEEBB'>"+sum+"</td></tr>");
					out.println("</table><br>");
		    		%>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		   <script>
		  drawRegUserFlotr();
		   </script>
		</center>
		<br>
		<i>渠道商自己有付费接口，玩家通过渠道商的付费接口付费。采用这种方式付费，不扣除付费渠道的手续费用。</i>
		<br>
	</body>
</html>
