<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.economic.SavingReasonType"%>
<%@page import="com.fy.engineserver.economic.CurrencyType"%>
<%@page import="com.fy.engineserver.economic.SavingFailedException"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.*"%>
<%@page
	import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

</head>
	<%
<<<<<<< local
		String pwd = request.getParameter("pwd");
		if(pwd!=null){
			if(!pwd.equals("6yhnmju78ik,")){
				out.print("<font color='red'>无效</font>");
				out.print("<form><input type='text' name='pwd'><input type='submit' value='确认身份'></form>");
=======
		Map<String ,String[]> map = new HashMap<String ,String[]>();
		String 	[] 巍巍昆仑 = {"1100000000006246402 200000","1100000000006145025 200000","110000000000614025 200000"};
		map.put("国内本地测试", 巍巍昆仑);
		
		String 	[] 缘定三生 = {"1349000000000006719 200000","1349000000000004786 200000"};
		map.put("缘定三生", 缘定三生);
		
		String 	[] 儒战天下 = {"1347000000000102667 200000","1347000000000025071 400000"};
		map.put("儒战天下", 儒战天下);
		
		String mailTitle = "系统邮件";
		String mailContent = "亲爱的玩家，九宫道境玩法在2013年12月26日维护后出现异常，无法正常体验，特退还在这期间玩家进入九宫道境的花费。请您尽快提取附件中银两，以免造成损失。";
		
		//
		String currservername = GameConstants.getInstance().getServerName();
		if(currservername!=null){
			String [] messes = map.get(currservername);
			if(messes!=null && messes.length>0){
				for(int i=0,len=messes.length;i<len;i++){
					if(messes[i]!=null){
						String [] mess = messes[i].split(" ");
						if(mess==null || mess.length!=2){
							out.print("[腾讯九宫补偿银子] [出错：格式不对] ["+messes[i]+"]");
							ActivitySubSystem.logger.warn("[腾讯九宫补偿银子] [出错：格式不对] ["+messes[i]+"]");
							continue;
						}
						long pid = Long.parseLong(mess[0]);
						long silver = Long.parseLong(mess[1]);
						
						Player p =null;
						try{
							p = PlayerManager.getInstance().getPlayer(pid);
						}catch(Exception e){
							out.print("[腾讯九宫补偿银子] [出错：玩家不存在] [id:"+pid+"] ["+messes[i]+"]");
							ActivitySubSystem.logger.warn("[腾讯九宫补偿银子] [出错：玩家不存在] [id:"+pid+"] ["+messes[i]+"]");
							continue;
						}
						
						try {
							MailManager.getInstance().sendMail(p.getId(),new ArticleEntity[0],new int[0],mailTitle,mailContent,silver,0L,0L,"腾讯九宫补偿银子");							
							out.print("[腾讯九宫补偿银子] [银子："+silver+"] [服务器："+currservername+"] [账号："+p.getUsername()+"] [角色："+p.getName()+"] [id:"+p.getId()+"]<br>");
							ActivitySubSystem.logger.warn("[腾讯九宫补偿银子] [银子："+silver+"] [服务器："+currservername+"] [账号："+p.getUsername()+"] [角色："+p.getName()+"] [id:"+p.getId()+"]");
						} catch (Exception e) {
							e.printStackTrace();
							out.print("[腾讯九宫补偿银子] [出错：发银子出错] [id:"+pid+"] ["+messes[i]+"]");
							ActivitySubSystem.logger.warn("[腾讯九宫补偿银子] [出错：发银子出错] [id:"+pid+"] ["+messes[i]+"]");
							continue;
						}
					}
				}
>>>>>>> other
			}else{
<<<<<<< local
				Map<String ,String[]> map = new HashMap<String ,String[]>();
				
		 		String 	[] 霸气乾坤 = {"1105000000000069485 800000",
		 				"1105000000000014058 1000000",
		 				"1105000000000121226 200000",
		 				"1105000000000150379 200000",
		 				"1105000000000053364 200000",
		 				"1105000000000235749 200000",
		 				"1105000000000239565 200000",
		 				"1105000000000240707 200000",
		 				"1105000000000053294 800000",
		 				"1105000000000092075 200000",
		 				"1105000000000059154 800000",
		 				"1105000000000164406 400000",
		 				"1105000000000234632 200000",};
		 		map.put("霸气乾坤", 霸气乾坤);
				
		 		String 	[] 昆仑圣殿 = {"1103000000000164341 200000",
		 				"1102000000000125783 800000",
		 				"1103000000000101417 200000",
		 				"1103000000000048181 200000",
		 				"1102000000000127112 800000",
		 				"1103000000000078637 200000",
		 				"1103000000000074399 200000",
		 				"1103000000000055201 400000",
		 				"1102000000000134869 200000",
		 				"1102000000000061642 200000",
		 				"1103000000000104741 200000",
		 				"1103000000000011328 400000",
		 				"1103000000000134230 200000",
		 				"1103000000000077142 200000",
		 				"1102000000000048259 200000",
		 				"1102000000000001960 600000",
		 				"1103000000000106534 200000",
		 				"1102000000000057965 200000",
		 				"1102000000000118279 200000",
		 				"1103000000000019160 1200000",
		 				"1103000000000099524 200000",
		 				"1103000000000008938 1200000",
		 				"1102000000000140341 200000",
		 				"1102000000000015619 800000",
		 				"1102000000000131222 200000",
		 				"1103000000000031720 200000",
		 				"1103000000000118880 200000",
		 				"1103000000000066732 200000",
		 				"1103000000000015187 200000",
		 				"1102000000000088728 200000",
		 				"1103000000000046795 800000",
		 				"1103000000000154088 200000",
		 				"1102000000000057408 200000",
		 				"1103000000000120807 200000",
		 				"1102000000000129138 200000",
		 				"1102000000000031169 200000",};
		 		map.put("昆仑圣殿", 昆仑圣殿);
		 		
		 		String 	[] 太虚幻境 = {"1103000000000084939 800000",
		 				"1103000000000071810 200000",
		 				"1103000000000044571 200000",
		 				"1103000000000086015 200000",
		 				"1101000000000153683 200000",
		 				"1103000000000128209 200000",
		 				"1101000000000074089 800000",
		 				"1101000000000064951 200000",
		 				"1103000000000038226 800000",
		 				"1101000000000008526 200000",
		 				"1101000000000165677 200000",
		 				"1101000000000064935 200000",
		 				"1101000000000070901 800000",
		 				"1103000000000103456 200000",
		 				"1101000000000172094 600000",
		 				"1103000000000085903 200000",
		 				"1103000000000032765 400000",
		 				"1101000000000117405 600000",
		 				"1101000000000072154 200000",
		 				"1101000000000158737 200000",
		 				"1101000000000005321 600000",
		 				"1103000000000024909 800000",
		 				"1101000000000077399 200000",
		 				"1103000000000044360 200000",
		 				"1103000000000060104 1600000",
		 				"1101000000000036654 800000",
		 				"1101000000000040642 800000",
		 				"1101000000000092070 400000",
		 				"1101000000000094279 200000",};
				map.put("太虚幻境", 太虚幻境);
				
				String 	[] 万象更新 = {"1109000000000075863 200000",
						"1108000000000060903 200000",
						"1108000000000054133 400000",
						"1108000000000082655 800000",
						"1108000000000001385 200000",
						"1108000000000006417 800000",
						"1108000000000076184 800000",
						"1108000000000067699 200000",
						"1108000000000074737 400000",
						"1108000000000007706 800000",};
				map.put("万象更新", 万象更新);
				
				String 	[] 烟雨青山 = {"1107000000000172133 200000",
						"1107000000000117269 200000",
						"1107000000000107744 200000",
						"1107000000000163032 200000",
						"1107000000000131914 400000",
						"1111000000000071839 200000",
						"1107000000000006067 1200000",
						"1107000000000118050 200000",
						"1107000000000169239 200000",
						"1107000000000034523 200000",
						"1107000000000141314 200000",
						"1107000000000046544 400000",
						"1107000000000098701 2400000",
						"1111000000000032862 800000",
						"1111000000000063702 200000",
						"1107000000000013897 1000000",
						"1107000000000035364 200000",
						"1111000000000081021 400000",
						"1107000000000102456 200000",
						"1111000000000048338 200000",
						"1107000000000089223 200000",
						"1107000000000002317 800000",
						"1107000000000058771 200000",
						"1111000000000011664 200000",
						"1111000000000086039 200000",
						"1107000000000010809 200000",
						"1107000000000043007 800000",
						"1111000000000041352 200000",
						"1107000000000068321 800000",
						"1107000000000051137 800000",
						"1107000000000033847 200000",
						"1107000000000012259 800000",
						"1107000000000164474 200000",};
				map.put("烟雨青山", 烟雨青山);
				
				String 	[] 河东狮吼 = {"1117000000000005346 200000",
						"1117000000000001212 200000",
						"1117000000000010621 200000",
						"1117000000000001430 400000",
						"1117000000000006021 200000",
						"1117000000000004016 200000",
						"1117000000000007638 200000",
						"1117000000000001236 200000",
						"1117000000000001371 200000",
						"1117000000000001227 400000",
						"1117000000000002987 200000",
						"1117000000000002335 200000",
						"1117000000000001355 200000",
						"1117000000000007389 200000",
						"1117000000000004055 400000",
						"1117000000000002067 200000",
						"1117000000000003633 200000",
						"1117000000000002941 200000",};
				map.put("河东狮吼", 河东狮吼);
				
				
		 		String 	[] 战无不胜 = {"1116000000000004649 200000",
		 				"1116000000000003250 200000",
		 				"1116000000000007040 200000",
		 				"1116000000000012291 200000",
		 				"1116000000000007033 200000",
		 				"1116000000000004810 200000",
		 				"1116000000000004244 200000",
		 				"1116000000000004163 200000",
		 				"1116000000000008025 200000",
		 				"1116000000000006145 200000",
		 				"1116000000000015086 200000",
		 				"1116000000000012112 200000",
		 				"1116000000000019821 200000",
		 				"1116000000000003366 200000",
		 				"1116000000000013657 200000",
		 				"1116000000000005518 200000",
		 				"1116000000000003111 400000",
		 				"1116000000000006733 200000",
		 				"1116000000000004349 200000",
		 				"1116000000000016102 400000",
		 				"1116000000000010171 200000",
		 				"1116000000000003539 200000",
		 				"1116000000000003131 200000",
		 				"1116000000000009517 200000",
		 				"1116000000000005368 200000",};
				map.put("战无不胜", 战无不胜);
				
				
				String 	[] 众仙归来 = {"1114000000000003074 200000","1114000000000185656 200000",
						"1114000000000005848 200000",
						"1114000000000154786 400000",
						"1114000000000215492 200000",
						"1114000000000212804 200000",
						"1114000000000081009 200000",
						"1114000000000194266 200000",
						"1114000000000114653 200000",
						"1114000000000005192 200000",
						"1114000000000188388 200000",
						"1114000000000013814 200000",
						"1114000000000067252 400000",
						"1114000000000070480 200000",
						"1114000000000122154 200000",
						"1114000000000007939 200000",
						"1114000000000161817 200000",
						"1114000000000204376 200000",
						"1114000000000158776 200000",
						"1114000000000206556 200000",
						"1114000000000127456 200000",
						"1114000000000065562 200000",
						"1114000000000092978 400000",
						"1114000000000031721 200000",
						"1114000000000005813 200000",
						"1114000000000170858 200000",
						"1114000000000148142 200000",
						"1114000000000004449 200000",
						"1114000000000130122 200000",
						"1114000000000145791 200000",
						"1114000000000085032 200000",
						"1114000000000007436 200000",
						"1114000000000187695 200000",
						"1114000000000085334 200000",
						"1114000000000220128 200000",
						"1114000000000025173 400000",
						"1114000000000209157 200000",
						"1114000000000125208 200000",
						"1114000000000221836 200000",
						"1114000000000015705 200000",
						"1114000000000176031 200000",
						"1114000000000005653 400000",
						"1114000000000186256 200000",
						"1114000000000218929 200000",
						"1114000000000131314 400000",
						"1114000000000199741 200000",
						"1114000000000008326 200000",
						"1114000000000098190 200000",
						"1114000000000218960 200000",
						"1114000000000158339 200000",
						"1114000000000218388 200000",};
				map.put("众仙归来", 众仙归来);
				
				String 	[] 三界奇缘 = {"1115000000000002074 200000",
						"1115000000000004423 400000",
						"1115000000000137497 200000",
						"1115000000000053081 200000",
						"1115000000000151783 200000",
						"1115000000000190446 400000",
						"1115000000000221934 200000",
						"1115000000000059583 200000",
						"1115000000000227371 200000",
						"1115000000000242686 200000",
						"1115000000000167249 200000",
						"1115000000000099715 200000",
						"1115000000000250228 200000",
						"1115000000000240262 200000",
						"1115000000000049549 200000",
						"1115000000000253969 200000",
						"1115000000000174705 200000",
						"1115000000000196905 200000",
						"1115000000000004368 200000",
						"1115000000000071026 200000",
						"1115000000000234215 200000",
						"1115000000000197428 200000",
						"1115000000000171457 200000",
						"1115000000000006754 200000",
						"1115000000000003739 200000",
						"1115000000000258244 200000",
						"1115000000000244093 400000",
						"1115000000000169785 200000",
						"1115000000000108118 200000",
						"1115000000000045752 200000",
						"1115000000000224090 200000",
						"1115000000000193043 200000",
						"1115000000000011292 200000",
						"1115000000000057933 200000",};
				map.put("三界奇缘", 三界奇缘);
				
		 		String 	[] 巍巍昆仑 = {"1100000000006246402 200000","1100000000006145025 200000","110000000000614025 200000"};
				map.put("国内本地测试", 巍巍昆仑);
				
				String mailTitle = "系统邮件";
				String mailContent = "亲爱的玩家，九宫道境玩法在2013年12月26日维护后出现异常，无法正常体验，特退还在这期间玩家进入九宫道境的花费。请您尽快提取附件中银两，以免造成损失。";
				
				//
				String currservername = GameConstants.getInstance().getServerName();
				if(currservername!=null){
					String [] messes = map.get(currservername);
					if(messes!=null && messes.length>0){
						for(int i=0,len=messes.length;i<len;i++){
							if(messes[i]!=null){
								String [] mess = messes[i].split(" ");
								if(mess==null || mess.length!=2){
									out.print("[腾讯九宫补偿银子] [出错：格式不对] ["+messes[i]+"]");
									ActivitySubSystem.logger.warn("[腾讯九宫补偿银子] [出错：格式不对] ["+messes[i]+"]");
									continue;
								}
								long pid = Long.parseLong(mess[0]);
								long silver = Long.parseLong(mess[1]);
								
								Player p =null;
								try{
									p = PlayerManager.getInstance().getPlayer(pid);
								}catch(Exception e){
									out.print("[腾讯九宫补偿银子] [出错：玩家不存在] [id:"+pid+"] ["+messes[i]+"]");
									ActivitySubSystem.logger.warn("[腾讯九宫补偿银子] [出错：玩家不存在] [id:"+pid+"] ["+messes[i]+"]");
									continue;
								}
								
								try {
									MailManager.getInstance().sendMail(p.getId(),new ArticleEntity[0],new int[0],mailTitle,mailContent,silver,0L,0L,"腾讯九宫补偿银子");							
									out.print("[腾讯九宫补偿银子] [银子："+silver+"] [服务器："+currservername+"] [账号："+p.getUsername()+"] [角色："+p.getName()+"] [id:"+p.getId()+"]<br>");
									ActivitySubSystem.logger.warn("[腾讯九宫补偿银子] [银子："+silver+"] [服务器："+currservername+"] [账号："+p.getUsername()+"] [角色："+p.getName()+"] [id:"+p.getId()+"]");
								} catch (Exception e) {
									e.printStackTrace();
									out.print("[腾讯九宫补偿银子] [出错：发银子出错] [id:"+pid+"] ["+messes[i]+"]");
									ActivitySubSystem.logger.warn("[腾讯九宫补偿银子] [出错：发银子出错] [id:"+pid+"] ["+messes[i]+"]");
									continue;
								}
							}
						}
					}else{
						out.print("[腾讯九宫补偿银子] [服务器没有数据] [map:"+map.size()+"] ["+currservername+"]");	
						ActivitySubSystem.logger.warn("[腾讯九宫补偿银子] [服务器没有数据] [map:"+map+"] ["+currservername+"]");	
					}
				}else{
					out.print("获取服务器名字出错；");
				}
=======
				out.print("[腾讯九宫补偿银子] [服务器没有数据] [map:"+map.size()+"] ["+currservername+"]");	
				ActivitySubSystem.logger.warn("[腾讯九宫补偿银子] [服务器没有数据] [map:"+map+"] ["+currservername+"]");	
>>>>>>> other
			}
		}else{
<<<<<<< local
			out.print("<form><input type='text' name='pwd'><input type='submit' value='确认身份'></form>");
=======
			out.print("获取服务器名字出错；");
>>>>>>> other
		}
	%>
<<<<<<< local
	
	
=======
>>>>>>> other
</body>
</html>
