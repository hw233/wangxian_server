<%@ page language="java" import="java.util.*,java.sql.*,com.sqage.stat.commonstat.entity.*,com.xuanzhi.tools.text.*" 
pageEncoding="UTF-8"%>
<%!

public void insertEnterUser(Connection con,PlayGame playGame) throws Exception{

		PreparedStatement ps = null;
		int i = -1;
		try {
			ps = con.prepareStatement("insert into STAT_PLAYGAME(" +
					"ID," +
					"ENTERDATE," +
					//"USERID," +
					"ENTERTIMES," +
					
					"ONLINETIME," +
					"YOUXI," +
					"CHONGZHISHU," +
					
					"XIAOFEISHU," +
					"FUWUQI," +
					"MAXLEVEL," +
					"MINLEVEL," +
					"USERNAME" +
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,?)");
			ps.setDate(1, (java.sql.Date) playGame.getEnterDate());
		//	ps.setLong(2, playGame.getUserId());
			ps.setLong(2, playGame.getEnterTimes());
			
			ps.setLong(3, playGame.getOnLineTime());
			ps.setString(4, playGame.getGame());
			ps.setLong(5, playGame.getChongZhiShu());
			
			ps.setLong(6, playGame.getXiaoFeiShu());
			ps.setString(7, playGame.getFenQu());
			ps.setLong(8, playGame.getMaxLevel());
			ps.setLong(9, playGame.getMinLevel());
			ps.setString(10, playGame.getUserName());
			
			i = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			ps.close();
		}
}
	public void insertUser(Connection con,User user) throws Exception{
			PreparedStatement ps = null;
			int i = -1;
			try {
				ps = con.prepareStatement("insert into STAT_USER(" +
					"ID," +
					"NAME," +
					"QUDAO," +
					
					"YOUXI," +
					"JIXING," +
					"DIDIAN," +
					
					"HAOMA," +
					"IMEI," +
					"REGISTTIME," +
					"UUID," +
					"GUOJIA" +
					") values (SEQ_STAT_COMMON_ID.NEXTVAL,?,?, ?,?,?, ?,?,?,?,?)");
			
			ps.setString(1, user.getName());
			ps.setString(2, user.getQuDao());
			
			ps.setString(3, user.getGame());
			ps.setString(4, user.getJiXing());
			ps.setString(5, user.getDiDian());
			
			ps.setString(6, user.getHaoMa());
			ps.setString(7, user.getImei());
			ps.setDate(8, (java.sql.Date) user.getRegistTime());
			ps.setString(9, user.getUuid());
			ps.setString(10, user.getGuojia());
			i = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			ps.close();
		}
	}
	
	public ArrayList<PlayGame> createPlayGame(ArrayList<User> list,String day,float f,ArrayList<User> enteredUser){
		 ArrayList<PlayGame> al=new  ArrayList<PlayGame>();
		 java.util.Date d = DateUtil.parseDate(day,"yyyy-MM-dd");
		 for(User u:list)
		 {
		 	if(Math.random() <= f){
		 		PlayGame p = new PlayGame();
		 		p.setUserName(u.getName());
		 		p.setGame("三国时代ol");
		 		java.sql.Date dd = new java.sql.Date(d.getTime() + ((int)(23 * Math.random()) ) * 3600 * 1000L);
		 		p.setEnterDate(dd);
		 		p.setChongZhiShu(23L);
		 		p.setFenQu("问鼎中原");
		 		//p.setUserId(u.getId());
		 		p.setMaxLevel(10L);
		 		p.setMinLevel(1L);
		 		p.setOnLineTime(100L);
		 		p.setEnterTimes(3L);
		 		p.setXiaoFeiShu(1L);
		 		
		 		al.add(p);
		 		enteredUser.add(u);
		 	}
		 }
		return al;
	}
	
	public ArrayList<User> randonCreateUser(int num,String regDay){
		ArrayList<User> al = new ArrayList<User>();
		for(int i=0;i<num;i++){
			User user=new User();
			user.setDiDian("beijing");
			user.setHaoMa("13260180229");
			long now=System.currentTimeMillis();
			user.setName(regDay+"_"+now+"_"+i);
			if(i%5==0){ user.setJiXing("nokie");}
			if(i%5==1){ user.setJiXing("lenevo");}
			if(i%5==2){ user.setJiXing("sunxing");}
			if(i%5==3){ user.setJiXing("htc");}
			if(i%5==4){ user.setJiXing("huawei");}
			
			if(i<=num*0.3){user.setQuDao("sina");}
			if(num*0.3<i&&i<=num*0.8){user.setQuDao("qq");}
			if(num*0.8<i&&i<=num*0.9){user.setQuDao("renren");}
			if(num*0.9<i&&i<=num*0.95){user.setQuDao("91");}
			if(num*0.95<i&&i<=num){user.setQuDao("danle");}
			
			user.setGame("三国时代ol");
			user.setImei("00SDFSD-SF-SDF-SDFSDFEWER_"+i);
			user.setUuid("uuid_"+i);
			user.setRegistTime(new java.sql.Date(DateUtil.parseDate(regDay, "yyyy-MM-dd").getTime()));
		
			if(i%6==0){
				user.setGuojia("吴国");
			}else if(i%6==1||i%6==2){
				user.setGuojia("蜀国");
			}else {
				user.setGuojia("魏国");
			}
			al.add(user);
		}
		return al;
		
	}
%><%
	String regDay = request.getParameter("regday");	
	String endDay = request.getParameter("endday");	
	
	if(regDay == null) regDay = DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd");
	if(endDay == null) endDay = DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd");
	int num = 100;
	try{
		num = Integer.parseInt(request.getParameter("num"));
	}catch(Exception e){}
	boolean submit = "true".equals( request.getParameter("submitted")	);
%>	

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>插入测试数据</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  
  <body><br>
  <h1>插入测试数据</h1>
  <form>
  		<input type='hidden' name='submitted' value='true'/>
  		注册日期：<input type='text' name='regday' value='<%= regDay %>'>
  		结束日期：<input type='text' name='endday' value='<%= endDay %>'>
  		数量：<input type='text' name='num' value='<%= num %>'>
  		<input type='submit' value='提交'/>
  </form>
  <%
  if(submit){
  
  int e_num=200;
  int times=0;
  int yushu=0;
  
  if(e_num>=num){e_num=num; times=1;
     }else{
  times=num/e_num;
  yushu=num%e_num;
  }
 
  for(int t=0;t<=times;t++){
    if(t==times){e_num=yushu;}
		ArrayList<User> al = randonCreateUser(e_num,regDay);
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@221.179.174.52:1521:ora10g","commonstat","commonstat");
		try{
			for(int i = 0 ; i < al.size() ; i++){
				User u = al.get(i);
				long ll = System.currentTimeMillis();
				insertUser(conn,u);
				
				//out.println("用户["+u.getName()+"]擦入成功,耗时"+(System.currentTimeMillis() - ll)+"ms！<br>");
				System.out.println("用户["+u.getName()+"]擦入成功,耗时"+(System.currentTimeMillis() - ll)+"ms！<br>");
			}
			java.util.Date startDay = DateUtil.parseDate(regDay,"yyyy-MM-dd");
			java.util.Date overDay = DateUtil.parseDate(endDay,"yyyy-MM-dd");
			long nDay = (overDay.getTime() - startDay.getTime())/(24 * 3600 * 1000);
			
			ArrayList<User> al2 = al;
			for(int i = 0 ; i <= nDay;i++)
			{
				String day = DateUtil.formatDate(new java.util.Date(startDay.getTime() + 24 * 3600 * 1000 * i),"yyyy-MM-dd");
				ArrayList<User> ret = new ArrayList<User> ();
				
				float rate = 0;
				if(i == 0) 
					rate = 0.9f;
				else if(i == 1) 
					rate = 0.5f;
				else
					rate = 0.5f + (i-1)*0.01f;
				if(rate > 0.99f) rate = 0.99f;
				
				ArrayList<PlayGame> pal = createPlayGame(al2,day,rate,ret);
				for(int ii = 0 ; ii < pal.size() ; ii++){
					PlayGame u = pal.get(ii);
					long ll = System.currentTimeMillis();
					insertEnterUser(conn,u);
					
					//out.println("用户进入游戏["+u.getUserName()+","+day+"]擦入成功,耗时"+(System.currentTimeMillis() - ll)+"ms！<br>");
					System.out.println("用户进入游戏["+u.getUserName()+","+day+"]擦入成功,耗时"+(System.currentTimeMillis() - ll)+"ms！<br>");
				}
				al2 = ret;
			}
		}catch(Exception e){
			throw e;
		}finally{
			conn.close();
		}
	}
	}
   %>
  <br></body>
</html>
