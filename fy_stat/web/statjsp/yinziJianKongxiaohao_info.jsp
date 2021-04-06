<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,com.sqage.stat.commonstat.manager.*,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,com.sqage.stat.model.Channel"
	%>
     <%
              String flag = null;
              flag = request.getParameter("flag");
    
	          String startDay = request.getParameter("day");
	          String endDay = request.getParameter("endDay");
	          String fenqu=request.getParameter("fenqu");
	          //String displayType=request.getParameter("displaytype");
	          //if(displayType==null){ displayType="1"; }
	
	          if("0".equals(fenqu)){fenqu=null;}
	
	          String today = DateUtil.formatDate(new Date(new Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	          if(startDay == null) startDay = today;
	          if(endDay == null) endDay = today;
	
	         Date t = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	         Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	         ArrayList<String> dayList = new ArrayList<String>();

              UserManagerImpl userManager=UserManagerImpl.getInstance();
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
	          Transaction_SpecialManagerImpl transaction_SpecialManager = Transaction_SpecialManagerImpl.getInstance();
	       
	           String subSql="";
	           if(fenqu!=null){  subSql+=" and  t.fenqu ='"+fenqu+"'";  }
	           else{
	           subSql+=" and  t.fenqu not in ('春暖花开','桃花仙岛','天下无双','海纳百川','琼楼金阙','飘渺仙道','万里苍穹','盛世欢歌','修罗转生')"; 
	           }
	           
	           
	            List<Transaction_yinzi> ls_yinDingChanChu=null;
	             List<Transaction_yinzi> ls_ChongZhi=null;
	            
	            
	           
	                  String sql_yinDingChanChu="select '11' createdate,'dd' fenqu,r.name REASONTYPE,'0' ACTION,sum(t.money) money "+
	                   " from stat_gamechongzhi_yinzi t,dt_reasontype r  where  r.name in "+
                            " ('商店购买','银票不足商店购买','配方合成鉴定符合成','装备升级用银子强化装备','宠物合体宠物合体','发送邮件扣费发邮件收费'"
                            +",'祈福家族开炉炼丹','押镖','庄园开地','家族篝火家族篝火','家族捐献家族捐献','进入付费地图进入付费地图','扩展背包扩展背包'"
                            +",'JIEYI_COST加入结义消耗','创建宗派创建宗派','购买家族徽章购买家族徽章','封印任务封印任务'"
                            +",'结婚婚礼规模500000','结婚婚礼规模1000000','结婚婚礼规模2000000','结婚婚礼规模5000000','结婚婚礼规模10000000'"
                            
                            +",'法宝吞噬法宝吞噬扣去手续费','银票不够法宝吞噬法宝吞噬扣去手续费','法宝激活属性法宝隐藏属性刷新','法宝神识法宝神识--0'"
                            +",'法宝神识法宝神识--1','法宝神识法宝神识--2','法宝神识法宝神识--3','法宝神识法宝神识--5','法宝银子法宝加持','银票不够法宝银子法宝加持'"
                            +",'法宝激活属性激活隐藏属性扣去手续费','银票不够装备升级激活隐藏属性费用','装备升级激活隐藏属性费用','装备升级激活隐藏属性扣去手续费'"
                            
                            +",'结婚请求结婚','千层塔刷新千层塔刷新','结婚送花或糖0','结婚送花或糖1'"
                            +",'结婚送花或糖2','结婚结婚补领戒指','结婚求婚送花或糖0','结婚求婚送花或糖1','结婚求婚送花或糖2','结婚强制离婚','刷新任务'"
                            +",'宠物炼化宠物炼化','JIEYI_COST结义消耗','天尊令使用天尊令','家族宣战家族宣战','配方合成用银子合成装备','竞标竞标','炼器炼器'"
                            +",'洗练洗练','城市争夺战城市争夺战','矿战矿战','追加资金追加资金','绑银不足配方合成鉴定符合成','绑银不足使用绑银装备绑定'"
                            +",'绑银不足商店购买','绑银不足使用绑银装备摘星','绑银不足使用绑银强化装备','绑银不足宠物强化强化','绑银不足活动-祭祖'"
                            +",'绑银不足申请家族驻地','绑银不足申请家族','绑银不足修理修理','绑银不足进入庄园仙府开门','绑银不足宠物炼化宠物强化'"
                            +",'绑银不足技能升级技能升级','绑银不足原地复活原地复活','绑银不足配方合成用绑银合成装备','绑银不足取喝酒buff取酒buff'"
                            +",'绑银不足王者之印使用王者之印','祈福祈福','银票不足祈福祈福','交易交易给他人','摆摊在摆摊上购买物品','拍卖竞拍费拍卖竞拍费'"
                            +",'拍卖拍卖手续费','拍卖违约金取消拍卖违约金','求购求购预存费用和手续费','周月周月反馈购买','活动金剪刀活动','活动银剪刀活动'"
                            +",'兑换活动兑换活动扣除银子','交付镖车交付镖车','击杀奸细击杀奸细','金钱道具使用镖玉(蓝)','金钱道具使用镖玉(绿)'"
                            +",'金钱道具使用镖玉(橙)','金钱道具使用镖玉(白)','金钱道具使用镖玉(紫)','金钱道具使用镖银(蓝)','金钱道具使用镖银(紫)'"
                            +",'金钱道具使用镖银(橙)','金钱道具使用镖银(白)','金钱道具使用镖银(绿)','进入宠物迷城进入宠物迷城','接取任务挖宝','大师技能加点skMasterAddPoint',"
	                        +"'大师技能加点skMasterExPoint','副本结束转盘副本转盘抽奖','仙府种植仙府种植-1级百宝树','仙府种植仙府种植-2级百宝树','仙府种植仙府种植-3级百宝树',"
	                        +"'仙府种植仙府种植-4级百宝树','坐骑阶培养新坐骑系统','家族祈福家族修炼','家族祈福家族上香','家族祈福刷新任务','创建战队创建战队花费',"
	                        +"'绑银不足翅膀强化用绑银强化翅膀','家族祈福家族上香','猎命猎命单抽消耗','猎命猎命十连抽消耗','极地寻宝抽奖极地寻宝抽奖','银票不够极地寻宝抽奖极地寻宝抽奖')"+subSql+
	                           " and  t.createdate between '"+startDay+"' and '"+endDay+"'  and  to_char(t.reasontype)=to_char(r.id) group by r.name ";
	                         //System.out.print(sql_yinDingChanChu);
	                         ls_yinDingChanChu=transaction_SpecialManager.getTransaction_yinziBySql(sql_yinDingChanChu);
	                            HashMap<String,String> map=new HashMap<String,String>();
	                       for(int num=0;num<ls_yinDingChanChu.size();num++){
	                            Transaction_yinzi transaction_yinzi= ls_yinDingChanChu.get(num);
	                             map.put(transaction_yinzi.getReasonType(),String.valueOf(transaction_yinzi.getMoney()));
	                             }
	                 
	    
	     //对比各个渠道数据
	       ArrayList<String> realChannelList = new ArrayList<String>();
	          realChannelList.add("类别名称");
	          realChannelList.add("2级分类");
	          realChannelList.add("消耗原因");
	          realChannelList.add("银锭(文)");
	          
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
		<h2 style="font-weight:bold;">银子消耗</h2>
		<form   method="post" id="form1">
		  <input type='hidden' name='flag' value='true'/>
		
		 开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
                     &nbsp;  &nbsp;  &nbsp;  &nbsp;  &nbsp;
                                             分区：<select name="fenqu">
                       <option  value="0">全部</option>
                       <% 
                       for(int i = 0 ; i < fenQuList.size() ; i++){
                       String[] _fenqu = fenQuList.get(i);
                       %>
                        <option value="<%=_fenqu[1] %>" 
                        <%
                        if(_fenqu[1].equals(fenqu)){  out.print(" selected=\"selected\"");  }
                         %>
                          ><%=_fenqu[1] %></option>
                       <%
                       }
                       %>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                
                                                 
		    	<input type='submit' value='提交'></form>
		    	<br/>
		    		
		    		<h3>银子消耗</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'>");
					
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					
					if ("true".equals(flag)) {  //如果是刚打开页面，不查询){
					%>
 
                      <tr bgcolor='#FFFFFF'><td  rowspan='2'>商城消费</td> <td>银子购买</td> <td>商店购买</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("商店购买")==null?"0":map.get("商店购买"))));%></td></tr>  
                                                  <tr bgcolor='#FFFFFF'><td>银票不足消耗银子购买</td><td>银票不足商店购买</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("银票不足商店购买")==null?"0":map.get("银票不足商店购买"))));%></td></tr> 
                                                      
                      <tr bgcolor='#FFFFFF'><td  rowspan='99'>系统消耗</td><td rowspan='57'>功能消耗</td>
                                                      
                                                      <td>合成鉴定符</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("配方合成鉴定符合成")==null?"0":map.get("配方合成鉴定符合成"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>装备炼星</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("装备升级用银子强化装备")==null?"0":map.get("装备升级用银子强化装备"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>宠物合体</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("宠物合体宠物合体")==null?"0":map.get("宠物合体宠物合体"))));%></td></tr>         
                                                      <tr bgcolor='#FFFFFF'><td>发邮件收费</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("发送邮件扣费发邮件收费")==null?"0":map.get("发送邮件扣费发邮件收费"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>家族开炉炼丹</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("祈福家族开炉炼丹")==null?"0":map.get("祈福家族开炉炼丹"))));%></td></tr>                                               
                                                      
                                                      <tr bgcolor='#FFFFFF'><td>押镖消耗</td><td><% 
                                                      
                                                     String yabiao    = map.get("押镖")==null ? "0": map.get("押镖");
                                                     String jiaofb    = map.get("交付镖车交付镖车")==null?"0":map.get("交付镖车交付镖车");
                                                     String biaoyub   = map.get("金钱道具使用镖玉(白)")==null? "0": map.get("金钱道具使用镖玉(白)");
                                                     String biaoyulv  = map.get("金钱道具使用镖玉(绿)")==null? "0":map.get("金钱道具使用镖玉(绿)");
                                                     String biaoyulan = map.get("金钱道具使用镖玉(蓝)")==null? "0":map.get("金钱道具使用镖玉(蓝)");
                                                     String biaoyuz   = map.get("金钱道具使用镖玉(紫)")==null? "0":map.get("金钱道具使用镖玉(紫)");
                                                     String biaoyuc   = map.get("金钱道具使用镖玉(橙)")==null? "0":map.get("金钱道具使用镖玉(橙)");
                                                     
                                                     String biaoyinb    = map.get("金钱道具使用镖银(白)")==null? "0":map.get("金钱道具使用镖银(白)");
                                                     String biaoyinlv   = map.get("金钱道具使用镖银(绿)")==null? "0": map.get("金钱道具使用镖银(绿)");
                                                     String biaoyinlan  = map.get("金钱道具使用镖银(蓝)")==null? "0":map.get("金钱道具使用镖银(蓝)");
                                                     String biaoyinz    = map.get("金钱道具使用镖银(紫)")==null? "0":map.get("金钱道具使用镖银(紫)");
                                                     String biaoyinc    = map.get("金钱道具使用镖银(橙)")==null? "0":map.get("金钱道具使用镖银(橙)");
                                                      
                                                      long yanbao=Long.parseLong(yabiao)-Long.parseLong(jiaofb)-Long.parseLong(biaoyub)
                                                      -Long.parseLong(biaoyulv)-Long.parseLong(biaoyulan)-Long.parseLong(biaoyuz)-Long.parseLong(biaoyuc)
                                                      -Long.parseLong(biaoyinb)-Long.parseLong(biaoyinlv)-Long.parseLong(biaoyinlan)-Long.parseLong(biaoyinz)-Long.parseLong(biaoyinc);

                                                       out.print(StringUtil.addcommas(yanbao));
                                                      %>
                                                      </td></tr> 
                                                      
                                                      <tr bgcolor='#FFFFFF'><td>庄园开地</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("庄园开地")==null?"0":map.get("庄园开地"))));%></td></tr>      
                                                      <tr bgcolor='#FFFFFF'><td>家族篝火</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("家族篝火家族篝火")==null?"0":map.get("家族篝火家族篝火"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>家族捐献</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("家族捐献家族捐献")==null?"0":map.get("家族捐献家族捐献"))));%></td></tr>                                               
                                                      <tr bgcolor='#FFFFFF'><td>进入付费地图</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("进入付费地图进入付费地图")==null?"0":map.get("进入付费地图进入付费地图"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>扩展背包</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("扩展背包扩展背包")==null?"0":map.get("扩展背包扩展背包"))));%></td></tr>    
                                                      <tr bgcolor='#FFFFFF'><td>加入结义</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("JIEYI_COST加入结义消耗")==null?"0":map.get("JIEYI_COST加入结义消耗"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>创建宗派</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("创建宗派创建宗派")==null?"0":map.get("创建宗派创建宗派"))));%></td></tr>                                               
                                                      <tr bgcolor='#FFFFFF'><td>购买家族徽章</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("购买家族徽章购买家族徽章")==null?"0":map.get("购买家族徽章购买家族徽章"))));%></td></tr>  
                                                      
                                                      
                                                      <tr bgcolor='#FFFFFF'><td>封印任务</td><td>
                                                      <% 
                                                      
                                                      String fengyin = map.get("封印任务封印任务")==null?"0":map.get("封印任务封印任务");
                                                      String jishajianxi=map.get("击杀奸细击杀奸细")==null?"0":map.get("击杀奸细击杀奸细");
                                                      long fengyinv=Long.parseLong(fengyin)-Long.parseLong(jishajianxi);
                                                      out.print(StringUtil.addcommas(fengyinv));
                                                      %>
                                                      </td></tr>    

                                                      
                                                      
                                                      <tr bgcolor='#FFFFFF'><td>婚礼规模0</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚婚礼规模500000")==null?"0":map.get("结婚婚礼规模500000"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>婚礼规模1</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚婚礼规模1000000")==null?"0":map.get("结婚婚礼规模1000000"))));%></td></tr>                                               
                                                      <tr bgcolor='#FFFFFF'><td>婚礼规模2</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚婚礼规模2000000")==null?"0":map.get("结婚婚礼规模2000000"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>婚礼规模3</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚婚礼规模5000000")==null?"0":map.get("结婚婚礼规模5000000"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>婚礼规模4</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚婚礼规模10000000")==null?"0":map.get("结婚婚礼规模10000000"))));%></td></tr>   

                                                      <tr bgcolor='#FFFFFF'><td>请求结婚</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚请求结婚")==null?"0":map.get("结婚请求结婚"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>千层塔刷新</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("千层塔刷新千层塔刷新")==null?"0":map.get("千层塔刷新千层塔刷新"))));%></td></tr>                                               
                                                      <tr bgcolor='#FFFFFF'><td>点头像送花或送糖0</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚送花或糖0")==null?"0":map.get("结婚送花或糖0"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>点头像送花或送糖1</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚送花或糖1")==null?"0":map.get("结婚送花或糖1"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>点头像送花或送糖2</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚送花或糖2")==null?"0":map.get("结婚送花或糖2"))));%></td></tr>
                                                      
                                                      <tr bgcolor='#FFFFFF'><td>结婚补领戒指</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚结婚补领戒指")==null?"0":map.get("结婚结婚补领戒指"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>结婚求婚送花或糖0</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚求婚送花或糖0")==null?"0":map.get("结婚求婚送花或糖0"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>结婚求婚送花或糖1</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚求婚送花或糖1")==null?"0":map.get("结婚求婚送花或糖1"))));%></td></tr>                                               
                                                      <tr bgcolor='#FFFFFF'><td>结婚求婚送花或糖2</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚求婚送花或糖2")==null?"0":map.get("结婚求婚送花或糖2"))));%></td></tr>                    
                                                      <tr bgcolor='#FFFFFF'><td>强制离婚</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("结婚强制离婚")==null?"0":map.get("结婚强制离婚"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>刷新任务</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("刷新任务")==null?"0":map.get("刷新任务"))));%></td></tr>                                               
                                                      <tr bgcolor='#FFFFFF'><td>宠物炼化</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("宠物炼化宠物炼化")==null?"0":map.get("宠物炼化宠物炼化"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>结义</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("JIEYI_COST结义消耗")==null?"0":map.get("JIEYI_COST结义消耗"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>使用天尊令</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("天尊令使用天尊令")==null?"0":map.get("天尊令使用天尊令"))));%></td></tr>  
                                                      
                                                      <tr bgcolor='#FFFFFF'><td>家族宣战</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("家族宣战家族宣战")==null?"0":map.get("家族宣战家族宣战"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>用银子合成装备和物品</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("配方合成用银子合成装备")==null?"0":map.get("配方合成用银子合成装备"))));%></td></tr>                                               
                                                     
                                                     
                                                      <tr bgcolor='#FFFFFF'><td>竞标镖局的税</td><td>
                                                      <% 
                                                      String jingbiao= map.get("竞标竞标")==null?"0":map.get("竞标竞标");
                                                      long jingbiaov=(long)(Long.parseLong(jingbiao)*0.03);
                                                      out.print(StringUtil.addcommas(jingbiaov));
                                                      %>
                                                      
                                                      </td></tr>  
                                                      
                                                      
                                                      <tr bgcolor='#FFFFFF'><td>炼器</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("炼器炼器")==null?"0":map.get("炼器炼器"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>洗练</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("洗练洗练")==null?"0":map.get("洗练洗练"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>城市争夺战报名</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("城市争夺战城市争夺战")==null?"0":map.get("城市争夺战城市争夺战"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>矿战报名</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("矿战矿战")==null?"0":map.get("矿战矿战"))));%></td></tr> 
                                                      
                                                      <tr bgcolor='#FFFFFF'><td>镖局追加资金扣税</td><td>
                                                      <% 
                                                      String zj=map.get("追加资金追加资金")==null?"0":map.get("追加资金追加资金");
                                                      long zjv=(long)(Long.parseLong(zj)*0.03);
                                                      out.print(zjv);
                                                      %>
                                                      
                                                      </td></tr>  
                                                      
                                                       <tr bgcolor='#FFFFFF'><td>进入宠物迷城</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("进入宠物迷城进入宠物迷城")==null?"0":map.get("进入宠物迷城进入宠物迷城"))));%></td></tr> 
                                                      
                                                      
                                                      <tr bgcolor='#FFFFFF'><td>接取任务挖宝</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("接取任务挖宝")==null?"0":map.get("接取任务挖宝"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>大师技能加点</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("大师技能加点skMasterAddPoint")==null?"0":map.get("大师技能加点skMasterAddPoint"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>大师技能修炼兑换</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("大师技能加点skMasterExPoint")==null?"0":map.get("大师技能加点skMasterExPoint"))));%></td></tr> 
                                                      
                                                      
                                                      <tr bgcolor='#FFFFFF'><td>副本结束转盘副本转盘抽奖</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("副本结束转盘副本转盘抽奖")==null?"0":map.get("副本结束转盘副本转盘抽奖"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>坐骑阶培养新坐骑系统</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("坐骑阶培养新坐骑系统")==null?"0":map.get("坐骑阶培养新坐骑系统"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>仙府种植仙府种植-1级百宝树</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("仙府种植仙府种植-1级百宝树")==null?"0":map.get("仙府种植仙府种植-1级百宝树"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>仙府种植仙府种植-2级百宝树</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("仙府种植仙府种植-2级百宝树")==null?"0":map.get("仙府种植仙府种植-2级百宝树"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>仙府种植仙府种植-3级百宝树</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("仙府种植仙府种植-3级百宝树")==null?"0":map.get("仙府种植仙府种植-3级百宝树"))));%></td></tr> 
                                                      <tr bgcolor='#FFFFFF'><td>仙府种植仙府种植-4级百宝树</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("仙府种植仙府种植-4级百宝树")==null?"0":map.get("仙府种植仙府种植-4级百宝树"))));%></td></tr> 
                                                      
                                                       <tr bgcolor='#FFFFFF'><td>家族祈福家族上香</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("家族祈福家族上香")==null?"0":map.get("家族祈福家族上香"))));%></td></tr> 
                                                       <tr bgcolor='#FFFFFF'><td>家族祈福刷新任务</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("家族祈福刷新任务")==null?"0":map.get("家族祈福刷新任务"))));%></td></tr> 
                                                       <tr bgcolor='#FFFFFF'><td>创建战队创建战队花费</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("创建战队创建战队花费")==null?"0":map.get("创建战队创建战队花费"))));%></td></tr> 
                                                      
                                                    <tr bgcolor='#FFFFFF'><td>猎命猎命单抽消耗</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("猎命猎命单抽消耗")==null?"0":map.get("猎命猎命单抽消耗"))));%></td></tr> 
                                                     <tr bgcolor='#FFFFFF'><td>猎命猎命十连抽消耗</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("猎命猎命十连抽消耗")==null?"0":map.get("猎命猎命十连抽消耗"))));%></td></tr> 
                                                      

                                                 <tr bgcolor='#FFFFFF'><td rowspan='19'>绑银不足消耗银子</td>
 	                                                                          <td>绑银不足合成鉴定符</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足配方合成鉴定符合成")==null?"0":map.get("绑银不足配方合成鉴定符合成"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足绑绑定装备</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足使用绑银装备绑定")==null?"0":map.get("绑银不足使用绑银装备绑定"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足商店购买</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足商店购买")==null?"0":map.get("绑银不足商店购买"))));%></td></tr>    
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足装备摘星</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足使用绑银装备摘星")==null?"0":map.get("绑银不足使用绑银装备摘星"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足强化装备</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足使用绑银强化装备")==null?"0":map.get("绑银不足使用绑银强化装备"))));%></td></tr>                                               
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足宠物幻化</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足宠物强化强化")==null?"0":map.get("绑银不足宠物强化强化"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足活动-祭祖</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足活动-祭祖")==null?"0":map.get("绑银不足活动-祭祖"))));%></td></tr>      
                     
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足申请家族驻地</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足申请家族驻地")==null?"0":map.get("绑银不足申请家族驻地"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足申请家族</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足申请家族")==null?"0":map.get("绑银不足申请家族"))));%></td></tr>                                               
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足修理装备</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足修理修理")==null?"0":map.get("绑银不足修理修理"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足仙府开门</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足进入庄园仙府开门")==null?"0":map.get("绑银不足进入庄园仙府开门"))));%></td></tr>    
          
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足宠物强化</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足宠物炼化宠物强化")==null?"0":map.get("绑银不足宠物炼化宠物强化"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足技能升级</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足技能升级技能升级")==null?"0":map.get("绑银不足技能升级技能升级"))));%></td></tr>                                               
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足原地复活</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足原地复活原地复活")==null?"0":map.get("绑银不足原地复活原地复活"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足银合成装备</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足配方合成用绑银合成装备")==null?"0":map.get("绑银不足配方合成用绑银合成装备"))));%></td></tr>    
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足取酒buff</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足取喝酒buff取酒buff")==null?"0":map.get("绑银不足取喝酒buff取酒buff"))));%></td></tr>  
                                                      <tr bgcolor='#FFFFFF'><td>绑银不足使用王者之印</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足王者之印使用王者之印")==null?"0":map.get("绑银不足王者之印使用王者之印"))));%></td></tr>                                               
                              
                                                     <tr bgcolor='#FFFFFF'><td>绑银不足翅膀强化用绑银强化翅膀</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("绑银不足翅膀强化用绑银强化翅膀")==null?"0":map.get("绑银不足翅膀强化用绑银强化翅膀"))));%></td></tr>                                               
                                                     <tr bgcolor='#FFFFFF'><td>家族祈福家族上香</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("家族祈福家族上香")==null?"0":map.get("家族祈福家族上香"))));%></td></tr>                                               
                              
                              
                              
                              
                                                  <tr bgcolor='#FFFFFF'><td rowspan='1'>工资不足消耗银子</td>
                                                                        <td>家族祈福家族修炼</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("家族祈福家族修炼")==null?"0":map.get("家族祈福家族修炼"))));%></td></tr>  
                                                 
                              
                              
                                                  <tr bgcolor='#FFFFFF'><td rowspan='2'>吞噬</td>
                                                                        <td>法宝吞噬法宝吞噬扣去手续费</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("法宝吞噬法宝吞噬扣去手续费")==null?"0":map.get("法宝吞噬法宝吞噬扣去手续费"))));%></td></tr>  
                                                  <tr bgcolor='#FFFFFF'><td>银票不够法宝吞噬法宝吞噬扣去手续费</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("银票不够法宝吞噬法宝吞噬扣去手续费")==null?"0":map.get("银票不够法宝吞噬法宝吞噬扣去手续费"))));%></td></tr>  
                                                 
                                                 
                                                  <tr bgcolor='#FFFFFF'><td rowspan='5'>神识</td>
                                                                        <td>法宝神识法宝神识--0</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("法宝神识法宝神识--0")==null?"0":map.get("法宝神识法宝神识--0"))));%></td></tr>    
                                                  <tr bgcolor='#FFFFFF'><td>法宝神识法宝神识--1</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("法宝神识法宝神识--1")==null?"0":map.get("法宝神识法宝神识--1"))));%></td></tr>  
                                                  <tr bgcolor='#FFFFFF'><td>法宝神识法宝神识--2</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("法宝神识法宝神识--2")==null?"0":map.get("法宝神识法宝神识--2"))));%></td></tr>                                               
                                                  <tr bgcolor='#FFFFFF'><td>法宝神识法宝神识--3</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("法宝神识法宝神识--3")==null?"0":map.get("法宝神识法宝神识--3"))));%></td></tr>  
                                                  <tr bgcolor='#FFFFFF'><td>法宝神识法宝神识--5</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("法宝神识法宝神识--5")==null?"0":map.get("法宝神识法宝神识--5"))));%></td></tr>      
                                                  
                                                  
                                                   <tr bgcolor='#FFFFFF'><td rowspan='2'>加持</td>
                                                                         <td>法宝银子法宝加持</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("法宝银子法宝加持")==null?"0":map.get("法宝银子法宝加持"))));%></td></tr>  
                                                  <tr bgcolor='#FFFFFF'><td>银票不够法宝银子法宝加持</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("银票不够法宝银子法宝加持")==null?"0":map.get("银票不够法宝银子法宝加持"))));%></td></tr>                                               
                                                 
                                                 
                                                  <tr bgcolor='#FFFFFF'><td rowspan='4'>激活隐藏属性</td>
                                                                        <td>法宝激活属性激活隐藏属性扣去手续费</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("法宝激活属性激活隐藏属性扣去手续费")==null?"0":map.get("法宝激活属性激活隐藏属性扣去手续费"))));%></td></tr>  
                                                  <tr bgcolor='#FFFFFF'><td>银票不够装备升级激活隐藏属性费用</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("银票不够装备升级激活隐藏属性费用")==null?"0":map.get("银票不够装备升级激活隐藏属性费用"))));%></td></tr>    
                                                  <tr bgcolor='#FFFFFF'><td>装备升级激活隐藏属性费用</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("装备升级激活隐藏属性费用")==null?"0":map.get("装备升级激活隐藏属性费用"))));%></td></tr>  
                                                  <tr bgcolor='#FFFFFF'><td>装备升级激活隐藏属性扣去手续费</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("装备升级激活隐藏属性扣去手续费")==null?"0":map.get("装备升级激活隐藏属性扣去手续费"))));%></td></tr>                                               
                                                 
                                                 
                                                 <tr bgcolor='#FFFFFF'><td rowspan='1'>刷新隐藏属性</td>
                                                                       <td>法宝激活属性法宝隐藏属性刷新</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("法宝激活属性法宝隐藏属性刷新")==null?"0":map.get("法宝激活属性法宝隐藏属性刷新"))));%></td></tr>  
                                                  



                                          <tr bgcolor='#FFFFFF'><td  rowspan='2'>祈福</td><td>祈福</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("祈福祈福")==null?"0":map.get("祈福祈福"))));%></td></tr>  
                                          <tr bgcolor='#FFFFFF'><td>银票祈福</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("银票不足商店购买")==null?"0":map.get("银票不足商店购买"))));%></td></tr> 
                                          
                                          <tr bgcolor='#FFFFFF'><td  rowspan='6'>交易手续费及税收</td>
                                          	               <td>面对面交易扣税</td><td>
                                          	               <% 
                                          	              String jiaoyi= map.get("交易交易给他人")==null?"0":map.get("交易交易给他人");
                                          	              long jiaoyiv=Long.parseLong(jiaoyi)/21;
                                          	              out.print(StringUtil.addcommas(jiaoyiv));
                                          	               %>
                                          	               </td></tr> 
                                               <tr bgcolor='#FFFFFF'><td>摆摊交易扣税</td><td>
                                               <% 
                                               
                                               String baitan=map.get("摆摊在摆摊上购买物品")==null?"0":map.get("摆摊在摆摊上购买物品");
                                               long baitanb=Long.parseLong(baitan)/21;
                                               out.print(StringUtil.addcommas(baitanb));
                                               %>
                                               </td></tr>  
                                               <tr bgcolor='#FFFFFF'><td>拍卖行扣税消耗1</td><td>
                                               <% 
                                               String paimai=map.get("拍卖竞拍费拍卖竞拍费")==null?"0":map.get("拍卖竞拍费拍卖竞拍费");
                                               long paimaiv=Long.parseLong(paimai)/5;
                                               out.print(StringUtil.addcommas(paimaiv));
                                               %>
                                               
                                               </td></tr>  
                                               
                                               
                                               <tr bgcolor='#FFFFFF'><td>拍卖手续费</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("拍卖拍卖手续费")==null?"0":map.get("拍卖拍卖手续费"))));%></td></tr>  
                                               <tr bgcolor='#FFFFFF'><td>取消拍卖违约金</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("拍卖违约金取消拍卖违约金")==null?"0":map.get("拍卖违约金取消拍卖违约金"))));%></td></tr>  
                                               <tr bgcolor='#FFFFFF'><td>求购手续费</td><td>
                                               <% 
                                               String qiugou=map.get("求购求购预存费用和手续费")==null?"0":map.get("求购求购预存费用和手续费");
                                               long qiugouv=Long.parseLong(qiugou)/11;
                                               out.print(StringUtil.addcommas(qiugouv));
                                               %>
                                               
                                               </td></tr>  
                                               
                                  <tr bgcolor='#FFFFFF'><td  rowspan='6'>活动消耗</td><td   rowspan='6'>&nbsp;活动消耗</td>
                                          	               <td>购买周月反馈活动礼包</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("周月周月反馈购买")==null?"0":map.get("周月周月反馈购买"))));%></td></tr> 
                                               <tr bgcolor='#FFFFFF'><td>金剪刀活动</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("活动金剪刀活动")==null?"0":map.get("活动金剪刀活动"))));%></td></tr>  
                                               <tr bgcolor='#FFFFFF'><td>银剪刀活动</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("活动银剪刀活动")==null?"0":map.get("活动银剪刀活动"))));%></td></tr>  
                                               <tr bgcolor='#FFFFFF'><td>银子转换成工资</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("兑换活动兑换活动扣除银子")==null?"0":map.get("兑换活动兑换活动扣除银子"))));%></td></tr>  
                                               <tr bgcolor='#FFFFFF'><td>极地寻宝抽奖极地寻宝抽奖</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("极地寻宝抽奖极地寻宝抽奖")==null?"0":map.get("极地寻宝抽奖极地寻宝抽奖"))));%></td></tr>  
                                               <tr bgcolor='#FFFFFF'><td>银票不够极地寻宝抽奖极地寻宝抽奖</td><td><% out.print(StringUtil.addcommas(Long.parseLong(map.get("银票不够极地寻宝抽奖极地寻宝抽奖")==null?"0":map.get("银票不够极地寻宝抽奖极地寻宝抽奖"))));%></td></tr>  
 
                     
		               <%  
						}
					out.println("</table><br>");
		    		%>
		    		
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
