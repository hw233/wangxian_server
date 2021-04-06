<%@ page contentType="text/html;charset=UTF-8"%>
<%@include file="../inc.jsp"%>
<%@ page
	import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache,org.slf4j.*,java.io.*,java.util.*,com.xuanzhi.tools.text.*,
	org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,
	com.sqage.stat.commonstat.dao.UserDao,com.sqage.stat.commonstat.entity.*,com.sqage.stat.commonstat.manager.*,
	com.sqage.stat.commonstat.manager.Impl.UserManagerImpl,com.sqage.stat.commonstat.manager.Impl.*,com.sqage.stat.service.*,
	com.sqage.stat.model.Channel,java.sql.*"
	%>
	<%!
	Object lock = new Object(){};
      %>
     <%
              String flag = null;
              flag = request.getParameter("flag");
    
	          String startDay = request.getParameter("day");
	          String endDay = request.getParameter("endDay");
	          String type=request.getParameter("type");
	          String fenqu=request.getParameter("fenqu");
	           String money=request.getParameter("money");
	          if("0".equals(money)||money==null){money="50000";}
	          
	          String displayType=request.getParameter("displaytype");
	          if(displayType==null){ displayType="1"; }
	
	          if("0".equals(fenqu)){fenqu=null;}
	
	          String today = DateUtil.formatDate(new java.util.Date(new java.util.Date().getTime()-24*60*60*1000),"yyyy-MM-dd");
	          if(startDay == null) startDay = today;
	          if(endDay == null) endDay = today;
	
	         java.util.Date tt = DateUtil.parseDate(endDay,"yyyy-MM-dd");
	         String endDay_kucun = DateUtil.formatDate(new java.util.Date(tt.getTime()+24*60*60*1000),"yyyy-MM-dd");
	         java.util.Date t= DateUtil.parseDate(endDay_kucun,"yyyy-MM-dd");
	         java.util.Date s = DateUtil.parseDate(startDay,"yyyy-MM-dd");
	         ArrayList<String> dayList = new ArrayList<String>();
	         
	          synchronized(lock){
	         while(s.getTime() <= t.getTime() + 3600000){
		       String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		       dayList.add(day);
		       s.setTime(s.getTime() + 24*3600*1000);
	           }
	           
             }
	/**
	*获得渠道信息
	**/
	          ChannelManager cmanager = ChannelManager.getInstance();
              UserManagerImpl userManager=UserManagerImpl.getInstance();
              PlayGameManagerImpl playGameManager=PlayGameManagerImpl.getInstance();
              OnLineUsersCountManagerImpl onLineUsersCountManager=OnLineUsersCountManagerImpl.getInstance();
              ChongZhiManagerImpl chongZhiManager=ChongZhiManagerImpl.getInstance();
              
              ArrayList<String []> fenQuList= userManager.getFenQu(null);//获得现有的渠道信息
	          Transaction_SpecialManagerImpl transaction_SpecialManager = Transaction_SpecialManagerImpl.getInstance();
	       
	       
	       
	       
	       
	       
	       
	       String pingBi_fenqu="春暖花开','";
	       
	       
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@116.213.142.188:1521:ora11g","stat_mieshi","stat_mieshi");
		try{
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("select distinct(t.fenqu) name from stat_yinzikucun t where  to_Char(t.createdate,'YYYY-MM-DD')"
				+" between '"+startDay+"' and '"+endDay+"' and t.column1=1 and t.column2=1 group  by t.fenqu ,to_Char(t.createdate,'YYYY-MM-DD') "
				+"having  sum(t.count)/1000000 < 100") ;
			ps.execute();
			ResultSet rs = ps.getResultSet();
			out.print("存在库存不足100锭的服：");
			int num=0;
			while (rs.next()) {
			num++;
			String nn=rs.getString("name");
			pingBi_fenqu+=nn+"','";
			if(num%10!=0){
			out.print(nn+"||");
			}else
			{
			out.print(nn+"||<br>");
			}
		     } 
		      ps.close();
		     
		     String s2="select t.fenqu name,count(t.fenqu) count from stat_yinzikucun t where  to_Char(t.createdate,'YYYY-MM-DD')"
				+" between '"+startDay+"' and '"+endDay_kucun+"' and t.column1=1 and t.column2=1  group  by t.fenqu ";
		     ps = conn.prepareStatement(s2) ;
				
				//System.out.println("s2:"+s2);
			ps.execute();
			rs = ps.getResultSet();
			while (rs.next()) {
			num++;
			String nn=rs.getString("name");
			int count=rs.getInt("count");
			//System.out.println("count "+count+"dayList.size() "+dayList.size());
			if(count!=dayList.size()){
			pingBi_fenqu+=nn+"','";
			
			if(num%10!=0){
			out.print(nn+"||");
			}else
			{
			out.print(nn+"||<br>");
			}
			}
		     } 
		     
		     
		     
		     
		     }catch (SQLException e) {
			e.printStackTrace();
		} finally{
			ps.close();
			conn.close();
		}
		}catch(Exception e){
			throw e;
		}finally{
			conn.close();
		}
	
	       pingBi_fenqu+="桃花仙岛','天下无双','海纳百川','琼楼金阙','飘渺仙道','万里苍穹','盛世欢歌','修罗转生";
	       
	       
	       
	       
	       
	       
	       
	       
	       
	           String subSql="";
	           String ysubSql="";
	           if(fenqu!=null){
	           subSql+= "  and  t.fenqu ='"+fenqu+"'";
	           ysubSql+=" and  y.fenqu ='"+fenqu+"'";
	           }else
	           {
	            subSql +=" and  t.fenqu not in ('"+pingBi_fenqu+"')";
	            ysubSql+=" and  y.fenqu not in ('"+pingBi_fenqu+"')";
	           }
	           
	            List<Transaction_yinzi> ls_SuiHouLiuTong=null;
	            List<Transaction_yinzi> ls_YinDingChanChu=null;
	            List<Transaction_yinzi> ls_YinDingChongZhi=null;
	            List<Transaction_yinzi> ls_YinDingXiaoHao=null;
	            List<Transaction_yinzi> ls_Sui1=null;
	            List<Transaction_yinzi> ls_Sui2=null;
	            List<Transaction_yinzi> ls_qita=null;
	            List<Transaction_yinzi> ls_yinziKuCun=null;
	            
	            
	            
	           if("1".equals(displayType)){
	           String sql_SuiHouLiuTong="select t.createdate,'dd' fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinzi t,dt_reasontype r "
	                            +" where  r.name in ('摆摊在摆摊出售了东西','交易获取从他人那交易来','求购求购出售物品','邮件获取给卖家发钱') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay_kucun+"' "+subSql+"  and  to_char(t.reasontype)=to_char(r.id)  group by t.createdate order by t.createdate";
	                         ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	       
	                       String sql_Sui1="select t.createdate,'dd' fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinzi t,dt_reasontype r "
	                            +" where   r.name in ('摆摊在摆摊出售了东西','交易获取从他人那交易来') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay_kucun+"' "+subSql+"  and to_char(t.reasontype)=to_char(r.id)  group by t.createdate order by t.createdate";
	                        // ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	                         
	                       String sql_Sui2="select t.createdate,'dd' fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinzi t,dt_reasontype r "
	                            +" where r.name in ('求购求购出售物品') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay_kucun+"' "+subSql+"  and to_char(t.reasontype)=to_char(r.id) group by t.createdate order by t.createdate";
	                        // ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	                       ls_Sui1=transaction_SpecialManager.getTransaction_yinziBySql(sql_Sui1);
	                       ls_Sui2=transaction_SpecialManager.getTransaction_yinziBySql(sql_Sui2);
	       
               String sql_YinDingChanChu="select t.createdate,'dd' fenqu,'银锭产出' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinzi t,dt_reasontype r "
                                +" where  r.name in ('邮件获取系统补单','邮件获取充值返利','邮件获取充值活动','充值活动奖励累计充值奖励返利','邮件获取GM工具补发',"
                            +"'邮件获取后台发送-系统奖励！','邮件获取后台发送-系统邮件','金钱道具使用碎银子','金钱道具使用银两','金钱道具使用银块',"
                            +"'金钱道具使用银条','金钱道具使用银锭','金钱道具使用银砖','竞技奖励银子竞技奖励银子','占领城市给的钱占领城市给的钱',"
                            +"'占领矿给的钱占领矿给的钱','10级奖励银子10级奖励银子','10级奖励银子20级奖励银子','10级奖励银子30级奖励银子',"
                            +"'10级奖励银子40级奖励银子','10级奖励银子50级奖励银子','10级奖励银子60级奖励银子','10级奖励银子70级奖励银子',"
                            +"'10级奖励银子80级奖励银子','10级奖励银子90级奖励银子')  and  t.createdate between '"+startDay+"' and '"+endDay_kucun+"' "+subSql+"  and to_char(t.reasontype)=to_char(r.id) group by t.createdate order by t.createdate";
	                         ls_YinDingChanChu=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingChanChu);
	    
	                            
	                            String sql_YinDingChongZhi="select to_char(t.time,'YYYY-MM-DD') createdate ,'dd' fenqu,'银锭充值' REASONTYPE,'0' ACTION,round(sum(t.money)) money from stat_chongzhi t "+
	                              " where to_char(t.time,'YYYY-MM-DD') between '"+startDay+"' and  '"+endDay_kucun+"' " +subSql
	                              +" group by to_char(t.time,'YYYY-MM-DD')";
	                            
	                          ls_YinDingChongZhi=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingChongZhi);
	                 
	           String sql_YinDingXiaoHao="select t.createdate,'dd' fenqu,'银锭消耗' REASONTYPE,'0' ACTION,"
	           
	           +"sum(case "
	           +"when r.name in ('商店购买','银票不足商店购买','配方合成鉴定符合成','装备升级用银子强化装备','宠物合体宠物合体','发送邮件扣费发邮件收费'"
	           +",'祈福家族开炉炼丹','押镖','庄园开地','家族篝火家族篝火','家族捐献家族捐献','进入付费地图进入付费地图','扩展背包扩展背包'"
	           +",'JIEYI_COST加入结义消耗','创建宗派创建宗派','购买家族徽章购买家族徽章','封印任务封印任务','结婚婚礼规模白首同心-5-一心一意对戒(男)~一心一意对戒(女)~'"
	           +",'结婚婚礼规模琴瑟和弦-10-三生缱绻对戒(男)~三生缱绻对戒(女)~','结婚婚礼规模福禄鸳鸯-20-比翼双飞对戒(男)~比翼双飞对戒(女)~'"
	           +",'结婚婚礼规模天造地设-50-三生相守对戒(男)~三生相守对戒(女)~','结婚婚礼规模龙凤呈祥-100-山盟海誓对戒(男)~山盟海誓对戒(女)~'"
	           +",'结婚婚礼规模500000','结婚婚礼规模5000000','结婚婚礼规模1000000','结婚婚礼规模2000000','结婚婚礼规模10000000'"
	           +",'结婚请求结婚','千层塔刷新千层塔刷新','结婚送花或糖0','结婚送花或糖1','结婚送花或糖2','结婚结婚补领戒指','结婚求婚送花或糖0'"
	           +",'结婚求婚送花或糖1','结婚求婚送花或糖2','结婚强制离婚','刷新任务','宠物炼化炼化','JIEYI_COST结义消耗','天尊令使用天尊令'"
	           +",'家族宣战家族宣战','配方合成用银子合成装备','炼器炼器','洗练洗练','城市争夺战城市争夺战','矿战矿战','绑银不足配方合成鉴定符合成'"
	           +",'绑银不足使用绑银装备绑定','绑银不足商店购买','绑银不足使用绑银装备摘星','绑银不足使用绑银强化装备','绑银不足宠物强化强化'"
	           +",'绑银不足活动-祭祖','绑银不足申请家族驻地','绑银不足申请家族','绑银不足修理修理','绑银不足进入庄园仙府开门','绑银不足宠物炼化宠物强化'"
	           +",'绑银不足技能升级技能升级','绑银不足原地复活原地复活','绑银不足配方合成用绑银合成装备','绑银不足取喝酒buff取酒buff'"
	           +",'绑银不足王者之印使用王者之印','祈福祈福','银票不足祈福祈福','拍卖拍卖手续费','拍卖违约金取消拍卖违约金','周月周月反馈购买'"
	           +",'活动金剪刀活动','活动银剪刀活动','兑换活动兑换活动扣除银子','进入宠物迷城进入宠物迷城','接取任务挖宝','大师技能加点skMasterAddPoint',"
	           +"'大师技能加点skMasterExPoint') then t.money "
	           
	           +"when r.name in ('交付镖车交付镖车','金钱道具使用镖玉(白)','金钱道具使用镖玉(绿)','金钱道具使用镖玉(蓝)','金钱道具使用镖玉(紫)'"
	           +",'金钱道具使用镖玉(橙)','金钱道具使用镖银(白)','金钱道具使用镖银(绿)','金钱道具使用镖银(蓝)','金钱道具使用镖银(紫)','金钱道具使用镖银(橙)'"
	           +",'金钱道具使用镖金(白)','金钱道具使用镖金(绿)','金钱道具使用镖金(蓝)','金钱道具使用镖金(紫)','金钱道具使用镖金(橙)','击杀奸细击杀奸细') then -t.money "
	           +"when r.name in ('竞标竞标','追加资金追加资金') then t.money*0.03 "
	           +"when r.name in ('交易交易给他人','摆摊在摆摊上购买物品') then round(t.money/21) "
	           +"when r.name in ('拍卖竞拍费拍卖竞拍费') then round(t.money*0.2) "
	           +"when r.name in ('求购求购预存费用和手续费') then round(t.money/11)  end) money "
	           
	           +"from stat_gamechongzhi_yinzi t,dt_reasontype r "
	                            +" where  t.createdate between '"+startDay+"' and '"+endDay_kucun+"'"+subSql
	                            +"  and to_char(t.reasontype)=to_char(r.id) group by t.createdate order by t.createdate";
	                           ls_YinDingXiaoHao = transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingXiaoHao);
	                  
	                  String sql_yinziKuCun="select to_char(y.createdate,'YYYY-MM-DD') createdate,'dd' fenqu,'qit' REASONTYPE,'0' ACTION,sum(y.count) money "
                               +" from stat_yinzikucun y where to_char(y.createdate,'YYYY-MM-DD') between '"+startDay+"' and '"+endDay_kucun+"' and ((y.column1='1' and y.column2=1 ) or y.column2=3 or y.column2=4 ) "+ysubSql
                               +" group by to_char(y.createdate,'YYYY-MM-DD')";
	                          ls_yinziKuCun  = transaction_SpecialManager.getTransaction_yinziBySql(sql_yinziKuCun);
	                 
	                 }
	                 else if("0".equals(displayType))
	                 {
	                 
	           String sql_SuiHouLiuTong="select 'dd' createdate,t.fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinzi t,dt_reasontype r "
	                            +" where   r.name in ('摆摊在摆摊出售了东西','交易获取从他人那交易来','求购求购出售物品','邮件获取给卖家发钱') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay_kucun+"' "+subSql+"  and to_char(t.reasontype)=to_char(r.id) group by t.fenqu order by t.fenqu";
	                           ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	       
	                      String sql_Sui1="select 'dd' createdate,t.fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinzi t,dt_reasontype r "
	                            +" where r.name in ('摆摊在摆摊出售了东西','交易获取从他人那交易来') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay_kucun+"' "+subSql+"  and to_char(t.reasontype)=to_char(r.id) group by t.fenqu";
	                         //ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	                         
	                      String sql_Sui2="select 'dd' createdate,t.fenqu,'税后流通' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinzi t,dt_reasontype r "
	                            +" where  r.name in ('求购求购出售物品') "
	                            +" and  t.createdate between '"+startDay+"' and '"+endDay_kucun+"' "+subSql+"  and to_char(t.reasontype)=to_char(r.id) group by t.fenqu ";
	                         //ls_SuiHouLiuTong=transaction_SpecialManager.getTransaction_yinziBySql(sql_SuiHouLiuTong);
	       
	                       ls_Sui1=transaction_SpecialManager.getTransaction_yinziBySql(sql_Sui1);
	                       ls_Sui2=transaction_SpecialManager.getTransaction_yinziBySql(sql_Sui2);
	       
	       
               String sql_YinDingChanChu="select 'dd' createdate,t.fenqu,'银锭产出' REASONTYPE,'0' ACTION,sum(t.money) money from stat_gamechongzhi_yinzi t,dt_reasontype r "
                                +" where r.name in  ('邮件获取系统补单','邮件获取充值返利','邮件获取充值活动','充值活动奖励累计充值奖励返利','邮件获取GM工具补发',"
                            +"'邮件获取后台发送-系统奖励！','邮件获取后台发送-系统邮件','金钱道具使用碎银子','金钱道具使用银两','金钱道具使用银块',"
                            +"'金钱道具使用银条','金钱道具使用银锭','金钱道具使用银砖','竞技奖励银子竞技奖励银子','占领城市给的钱占领城市给的钱',"
                            +"'占领矿给的钱占领矿给的钱','10级奖励银子10级奖励银子','10级奖励银子20级奖励银子','10级奖励银子30级奖励银子',"
                            +"'10级奖励银子40级奖励银子','10级奖励银子50级奖励银子','10级奖励银子60级奖励银子','10级奖励银子70级奖励银子',"
                            +"'10级奖励银子80级奖励银子','10级奖励银子90级奖励银子')  and  t.createdate between '"+startDay+"' and '"+endDay_kucun+"' "+subSql+"   and to_char(t.reasontype)=to_char(r.id)  group by t.fenqu order by t.fenqu";
	                           ls_YinDingChanChu=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingChanChu);
	    
	         
	                     String sql_YinDingChongZhi="select 'dd' createdate ,t.fenqu fenqu,'银锭充值' REASONTYPE,'0' ACTION,round(sum(t.money)) money from stat_chongzhi t "+
	                              " where to_char(t.time,'YYYY-MM-DD') between '"+startDay+"' and  '"+endDay_kucun+"' " +subSql
	                              +" group by  t.fenqu";
	                            
	                           ls_YinDingChongZhi=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingChongZhi);
	                 
	           String sql_YinDingXiaoHao="select 'dd' createdate,t.fenqu,'银锭消耗' REASONTYPE,'0' ACTION,"
	           
	           
	             +"sum(case "
	           +"when r.name in ('商店购买','银票不足商店购买','配方合成鉴定符合成','装备升级用银子强化装备','宠物合体宠物合体','发送邮件扣费发邮件收费'"
	           +",'祈福家族开炉炼丹','押镖','庄园开地','家族篝火家族篝火','家族捐献家族捐献','进入付费地图进入付费地图','扩展背包扩展背包'"
	           +",'JIEYI_COST加入结义消耗','创建宗派创建宗派','购买家族徽章购买家族徽章','封印任务封印任务','结婚婚礼规模白首同心-5-一心一意对戒(男)~一心一意对戒(女)~'"
	           +",'结婚婚礼规模500000','结婚婚礼规模5000000','结婚婚礼规模1000000','结婚婚礼规模2000000','结婚婚礼规模10000000'"
	           +",'结婚婚礼规模琴瑟和弦-10-三生缱绻对戒(男)~三生缱绻对戒(女)~','结婚婚礼规模福禄鸳鸯-20-比翼双飞对戒(男)~比翼双飞对戒(女)~'"
	           +",'结婚婚礼规模天造地设-50-三生相守对戒(男)~三生相守对戒(女)~','结婚婚礼规模龙凤呈祥-100-山盟海誓对戒(男)~山盟海誓对戒(女)~'"
	           +",'结婚婚礼规模500000','结婚婚礼规模5000000','结婚婚礼规模1000000','结婚婚礼规模2000000','结婚婚礼规模10000000'"
	           +",'结婚请求结婚','千层塔刷新千层塔刷新','结婚送花或糖0','结婚送花或糖1','结婚送花或糖2','结婚结婚补领戒指','结婚求婚送花或糖0'"
	           +",'结婚求婚送花或糖1','结婚求婚送花或糖2','结婚强制离婚','刷新任务','宠物炼化炼化','JIEYI_COST结义消耗','天尊令使用天尊令'"
	           +",'家族宣战家族宣战','配方合成用银子合成装备','炼器炼器','洗练洗练','城市争夺战城市争夺战','矿战矿战','绑银不足配方合成鉴定符合成'"
	           +",'绑银不足使用绑银装备绑定','绑银不足商店购买','绑银不足使用绑银装备摘星','绑银不足使用绑银强化装备','绑银不足宠物强化强化'"
	           +",'绑银不足活动-祭祖','绑银不足申请家族驻地','绑银不足申请家族','绑银不足修理修理','绑银不足进入庄园仙府开门','绑银不足宠物炼化宠物强化'"
	           +",'绑银不足技能升级技能升级','绑银不足原地复活原地复活','绑银不足配方合成用绑银合成装备','绑银不足取喝酒buff取酒buff'"
	           +",'绑银不足王者之印使用王者之印','祈福祈福','银票不足祈福祈福','拍卖拍卖手续费','拍卖违约金取消拍卖违约金','周月周月反馈购买'"
	           +",'活动金剪刀活动','活动银剪刀活动','兑换活动兑换活动扣除银子','进入宠物迷城进入宠物迷城','接取任务挖宝','大师技能加点skMasterAddPoint',"
	           +"'大师技能加点skMasterExPoint') then t.money "
	           
	           +"when r.name in ('交付镖车交付镖车','金钱道具使用镖玉(白)','金钱道具使用镖玉(绿)','金钱道具使用镖玉(蓝)','金钱道具使用镖玉(紫)','金钱道具使用镖玉(橙)'"
	           +",'金钱道具使用镖银(白)','金钱道具使用镖银(绿)','金钱道具使用镖银(蓝)','金钱道具使用镖银(紫)','金钱道具使用镖银(橙)'"
	           +",'金钱道具使用镖金(白)','金钱道具使用镖金(绿)','金钱道具使用镖金(蓝)','金钱道具使用镖金(紫)','金钱道具使用镖金(橙)','击杀奸细击杀奸细') then -t.money "
	           +"when r.name in ('竞标竞标','追加资金追加资金') then t.money*0.03 "
	           +"when r.name in ('交易交易给他人','摆摊在摆摊上购买物品') then round(t.money/21) "
	           +"when r.name in ('拍卖竞拍费拍卖竞拍费') then round(t.money*0.2) "
	           +"when r.name in ('求购求购预存费用和手续费') then round(t.money/11)  end) money " 
	             
	             
	             +"from stat_gamechongzhi_yinzi t,dt_reasontype r "
	                            +" where  t.createdate between '"+startDay+"' and '"+endDay_kucun+"'"+subSql
	                            +"  and to_char(t.reasontype)=to_char(r.id) group by t.fenqu order by t.fenqu";
	                           ls_YinDingXiaoHao=transaction_SpecialManager.getTransaction_yinziBySql(sql_YinDingXiaoHao);
	                
	               
	               
	                String sql_yinziKuCun="select 'dd' createdate,y.fenqu,'qit' REASONTYPE,'0' ACTION,sum(y.count) money "
                               +" from stat_yinzikucun y where to_char(y.createdate,'YYYY-MM-DD') between '"+startDay+"' and '"+endDay_kucun+"' and ((y.column1='1' and y.column2=1 ) or y.column2=3 or y.column2=4 )  "+ysubSql
                               +" group by y.fenqu";
                               System.out.println("sql_yinziKuCun:"+sql_yinziKuCun);
	                          ls_yinziKuCun  = transaction_SpecialManager.getTransaction_yinziBySql(sql_yinziKuCun);
	                           
	                 }

	    
	     //对比各个渠道数据
	       ArrayList<String> realChannelList = new ArrayList<String>();
	          realChannelList.add("税后流通(锭)");
	          realChannelList.add("流通税收(锭)");
	          realChannelList.add("银锭产出(锭)");
	          realChannelList.add("银锭充值(锭)");
	          realChannelList.add("银锭消耗(锭)");
	          realChannelList.add("充值库存");
	          
	         
	       ArrayList<String> xiang = new ArrayList<String>();
	          xiang.add("实际库存变化");
	          xiang.add("预计库存变化");
	     synchronized(lock){
	         while(s.getTime() <= t.getTime() + 3600000){
		       String day = DateUtil.formatDate(s,"yyyy-MM-dd");
		       dayList.add(day);
		       s.setTime(s.getTime() + 24*3600*1000);
	           }
             }
             if("0".equals(displayType)){
              if(fenqu!=null){
               dayList.clear(); dayList.add(fenqu); 
               }else{
                 dayList.clear();
                  for(int num=0;num<fenQuList.size();num++){
                  String [] ls=fenQuList.get(num);
                  dayList.add(ls[1]);}
                }}
	     long channel_regday_nums[][] = new long[realChannelList.size()][dayList.size()];
	     
	     long tuxing[][] = new long[xiang.size()][dayList.size()];
	     long mintarget=0;
	     int lenth=(15>dayList.size())? dayList.size():15;
        if ("true".equals(flag)) {//如果是刚打开页面，不查询
         if("1".equals(displayType)){
      
			for( int g=0;g< ls_SuiHouLiuTong.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_SuiHouLiuTong.get(g);
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            	if(_day.equals(transaction_yinzi.getCreateDate())){
			              channel_regday_nums[0][k]=transaction_yinzi.getMoney()/1000000;
			           }
			           }
			       }
			       
			    for( int g=0;g< ls_Sui1.size();g++){
			       Transaction_yinzi transaction_yinzi1=(Transaction_yinzi)ls_Sui1.get(g);
			       Transaction_yinzi transaction_yinzi2=(Transaction_yinzi)ls_Sui2.get(g);
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            	if(_day.equals(transaction_yinzi1.getCreateDate())){
			              channel_regday_nums[1][k]+=(long)(transaction_yinzi1.getMoney()/20)/1000000;
			               }
			           
			            if(_day.equals(transaction_yinzi2.getCreateDate())){
			              channel_regday_nums[1][k]+=(long)(transaction_yinzi2.getMoney()/10)/1000000;
			               }
			           }
			       }
			       
			for( int g=0;g< ls_YinDingChanChu.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingChanChu.get(g);
			        for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			       channel_regday_nums[2][k]=transaction_yinzi.getMoney()/1000000;
			       }
			       }
			       }
			for( int g=0;g< ls_YinDingChongZhi.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingChongZhi.get(g);
			          for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			       channel_regday_nums[3][k]=transaction_yinzi.getMoney()/2000;
			       }
			       }
			       }
			for( int g=0;g< ls_YinDingXiaoHao.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingXiaoHao.get(g);
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			       channel_regday_nums[4][k]=transaction_yinzi.getMoney()/1000000;
			       }
			       }
			       }
			       
			      
			    for( int g=0;g< ls_yinziKuCun.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_yinziKuCun.get(g);
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		            if(_day.equals(transaction_yinzi.getCreateDate())){
			         channel_regday_nums[5][k]=transaction_yinzi.getMoney()/1000000;
			        // channel_regday_nums[5][k]=0;
			       }
			       }
			       }
			       
			       
			 } else if ("0".equals(displayType)){
          
			     for( int g=0;g< ls_SuiHouLiuTong.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_SuiHouLiuTong.get(g);
			       
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi.getFenQu())){
			           channel_regday_nums[0][k]=transaction_yinzi.getMoney()/1000000;
			         }
			         }
			       }
			       
			     for( int g1=0;g1< ls_Sui1.size();g1++){
			       Transaction_yinzi transaction_yinzi1=(Transaction_yinzi)ls_Sui1.get(g1);
			       
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi1.getFenQu())){
			           channel_regday_nums[1][k]+=(long)(transaction_yinzi1.getMoney()/20)/1000000;
			         }
			         }
			       }
			       for( int g2=0;g2< ls_Sui2.size();g2++){
			       Transaction_yinzi transaction_yinzi2=(Transaction_yinzi)ls_Sui2.get(g2);
			         for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
			         if(_day.equals(transaction_yinzi2.getFenQu())){
			           channel_regday_nums[1][k]+=(long)(transaction_yinzi2.getMoney()/10)/1000000;
			         }
			         }
			       }
			       
			       
			       
			     for( int g=0;g< ls_YinDingChanChu.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingChanChu.get(g);
			        for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi.getFenQu())){
			          channel_regday_nums[2][k]=transaction_yinzi.getMoney()/1000000;
			         }
			         }
			       }
			     for( int g=0;g< ls_YinDingChongZhi.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingChongZhi.get(g);
			        for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi.getFenQu())){
			       channel_regday_nums[3][k]=transaction_yinzi.getMoney()/2000;
			        }
			        }
			       }
			     for( int g=0;g< ls_YinDingXiaoHao.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_YinDingXiaoHao.get(g);
			        for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi.getFenQu())){
			       channel_regday_nums[4][k]=transaction_yinzi.getMoney()/1000000;
			       }
			       }
			       }
			     
			         
			      for( int g=0;g< ls_yinziKuCun.size();g++){
			       Transaction_yinzi transaction_yinzi=(Transaction_yinzi)ls_yinziKuCun.get(g);
			        for(int k = 0 ; k < dayList.size() ; k++){
		            	String _day = dayList.get(k);
		              if(_day.equals(transaction_yinzi.getFenQu())){
			           channel_regday_nums[5][k]=transaction_yinzi.getMoney()/1000000;
			           }
			          }
			         }
			         
			   }
			   
			   
			   
			   for(int k = 0 ; k < dayList.size() ; k++){
			          if(k < dayList.size()-1&&k!=0){
			          
			            Long gg=channel_regday_nums[5][k+1] - channel_regday_nums[5][k];
			            
			            if(k>1){
			            if(channel_regday_nums[5][k]==0||channel_regday_nums[5][k+1]==0){
			            tuxing[0][k]=0;
			            }else{
			            tuxing[0][k]=(gg>Long.parseLong(money)||gg< -Long.parseLong(money))   ? tuxing[0][k-1] : gg;
			            }
			            }else{
			            tuxing[0][k]=0;
			            }
			            }
			            if(mintarget>tuxing[0][k]){mintarget=tuxing[0][k];}
			          
			           Long temp=channel_regday_nums[2][k] + channel_regday_nums[3][k] - channel_regday_nums[4][k];
			           if(k>1){
			            tuxing[1][k]= (temp > Long.parseLong(money)||temp< -Long.parseLong(money)) ? tuxing[1][k-1] : temp;
			            }else{
			            tuxing[1][k]=0;
			            }
			            if(mintarget>tuxing[1][k]){mintarget=tuxing[1][k];}
			            
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
    <script type="text/javascript" language="javascript">
	      function search() {
		      $('form1').action="yinzixiaohao_info_detail.jsp";
		      $('form1').submit();
	        }
	        
	        
	      function search2() {
		      $('form1').action="yinzichanchu_info_detail.jsp";
		      $('form1').submit();
	        }
	        	
	        
	   
         </script>
<script language="JavaScript">
   var mycars=[<% for(int i = 0 ; i < dayList.size() ; i++){ out.print("\""+dayList.get(i)+"\",");} %>"E"];
       function drawRegUserFlotr(){
		    var f = Flotr.draw(
		$('regUserContainer'), [
		<%for(int j = 0 ; j < xiang.size() ; j++){
			StringBuffer sb2 = new StringBuffer();
			sb2.append("[");
			for(int kk = 0 ; kk < tuxing[j].length ; kk++){
				sb2.append("["+kk+","+tuxing[j][kk]+"]");
				if(kk < tuxing[j].length -1) sb2.append(",");
			}
			sb2.append("]");
			out.println("{");
			out.println("data:"+sb2.toString()+",");
			out.println("label:'"+xiang.get(j)+"'");
			out.println("}");
			if(j < xiang.size()-1) out.print(",");
		}
		%>
		],{
			xaxis:{
				noTicks: <%= lenth%>,	// Display 7 ticks.	
				tickFormatter: function(n){ return mycars[Math.floor(n)];}, // =>
				min: 0,
				max: <%= dayList.size()%>
			},
			yaxis:{
				tickFormatter: function(n){ return (n);}, // =>
				min: <%=mintarget%>,
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
		<h2 style="font-weight:bold;">银子流通统计</h2>
		<form   method="post" id="form1">
		  <input type='hidden' name='flag' value='true'/>
		    
		     开始日期： <input type="text" name="day" id="day" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=startDay %>"/>&nbsp;到
		  结束日期： <input type="text" name="endDay" id="endDay" class="nqz_input" style="width:100px;" readonly="readonly" onfocus="new Calendar(false,'yyyy-MM-dd').showTimePicker(this);" value="<%=endDay %>"/>
		 &nbsp;&nbsp;&nbsp;&nbsp;
		  抹平幅度<input type='text' name='money' value='<%=money %>'>（如果振幅超过这个值，就用前一天的值替带）
		    <br/><br/>
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
                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                
                                                     展示方式：
                   <input type="radio" <% if(displayType==null||"1".equals(displayType)){out.print("checked");} %>  name="displaytype" value="1" />时间
                   <input type="radio" <% if("0".equals(displayType)){out.print("checked");} %>  name="displaytype" value="0" />分区
                    &nbsp;&nbsp;&nbsp;&nbsp;
		    	    <input type='submit' value='提交'></form>
		    	
		    	<br/>
		    	
		    	
		    	<a href="yinzixiaohao_info_qufenqu.jsp">剔除库存断档的服</a>  
		    	       |<a href="yinzixiaohao_info_shaiXuan.jsp">筛选超标服务器</a>  
		    	       |<a href="yinzixiaohao_info_shaiXuan_fenqu.jsp">各个服务器数据</a>   
		    		   |<a href="yinzixiaohao_info.jsp">银子消耗</a>   
		    		   |<a href="yinzixiaohao_info_old.jsp">银子消耗(旧版)</a>
		    		   |<a href="yinpiaoxiaohao_info.jsp">银票消耗</a>
		    		   |  <a href="yinpiaoxiaohao_info_old.jsp">银票消耗(老版)</a>|
		    		
		    		<h3>统计总表数据（日报）</h3>
		    		<%
		    		out.println("<table id='test2' align='center' width='90%' cellpadding='1' bgcolor='#000000' cellspacing='1' border='0'>");
					out.print("<tr bgcolor='#EEEEBB'><td>日期</td>");
					
					long[] huizong=new long[realChannelList.size()];
		    		for(int c = 0 ; c < realChannelList.size() ; c++){
		    			out.print("<td>"+realChannelList.get(c)+"</td>");
		    		}
					out.println("</tr>");
					
					if ("true".equals(flag)) {  //如果是刚打开页面，不查询){
					
					for(int i = 0 ; i < dayList.size() ; i++){
						out.print("<tr bgcolor='#FFFFFF'><td>"+dayList.get(i)+"</td>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+StringUtil.addcommas(channel_regday_nums[c][i])+"</td>");
			    			
			    			huizong[c]+=channel_regday_nums[c][i];
			    		}
						out.println("</tr>");
					}
					
					out.print("<tr bgcolor='#EEEEBB'><td>汇总</td>");
						for(int c = 0 ; c < realChannelList.size() ; c++){
			    			out.print("<td>"+StringUtil.addcommas(huizong[c])+"</td>");
			    		}
						out.println("</tr>");
						}
					out.println("</table><br>");
		    		%>
		    		
		    		<table>
		    		
		    		<tr>
		    		<%
		    		out.print("<tr><td>日期</td>");
		    		 for(int k = 0 ; k < dayList.size() ; k++){
			            out.print("<td>"+dayList.get(k)+"</td>");
			            }
			            out.print("</tr>");
		    		out.print("<tr> <td>库存变化</td>");
		    		 for(int k = 0 ; k < dayList.size() ; k++){
			            out.print("<td>"+tuxing[0][k]+"</td>");
			            }
			            out.print("</tr>");
			            out.print("<tr><td>预计变化</td>");
			          for(int k = 0 ; k < dayList.size() ; k++){
			            out.print("<td>"+tuxing[1][k]+"</td>");
			            }
			             out.print("</tr>");
		    		 %>
		    		
		    		</tr>
		    		
		    		</table>
		    		
		    		<div id="regUserContainer" style="width:100%;height:400px;display:block;"></div>
		   <script>
		         drawRegUserFlotr();
		   </script>
		</center>
		<br>
		<i>这里的用户都是指进入游戏的用户</i>
		<br>
	</body>
</html>
