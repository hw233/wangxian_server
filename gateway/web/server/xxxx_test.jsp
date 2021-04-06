<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.thirdpartner.uc.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils;"%><%!
	
	public static class TTTTT{
		public int aNum;
		public int bNum;
		public int lockNum;
		public int resultNum;
		public double p;
		
		public TTTTT(int a,int b,int l, int r){
			aNum = a;
			bNum = b;
			lockNum = l;
			resultNum = r;
			if(lockNum > aNum) lockNum = aNum;
		}
	}

	Random r = new Random(System.currentTimeMillis());
	double[][] 概率 = new double[][]{
            {0.41,0.56},
            {0.37,0.52},
            {0.33,0.48},
            {0.29,0.44},
            {0.25,0.40},
            {0.21,0.36},
            {0.17,0.32},
            {0.13,0.28},
            {0.09,0.24},
            {0.05,0.20},
	};
/**
	 * pp为概率矩阵
	 * lockNum 锁定技能个数，主技能个数，副技能个数
	 */
	public int test(double[][] pp,int lockNum,int aNum,int bNum){

		int c = lockNum;
		
		for(int i = 0 ; i < aNum - lockNum ; i++){
			double d = r.nextDouble();
			if(d <= pp[c][1]){
				c++;
				if(c>=pp.length) return c;
			}
		}
		for(int i = 0 ; i < bNum ; i++){
			double d = r.nextDouble();
			if(d <= pp[c][0]){
				c++;
				if(c>=pp.length) return c;
			}
		}
		return c;
	}

	public double calculateP(int aNum,int bNum,int lockNum,int resultNum){
		int n = 10000;
		int c = 0;
		for(int i = 0 ; i < n ; i++){
			int r = test(概率,lockNum,aNum,bNum);
			if(r>=resultNum){
				c++;
			}
		}
		return c*1.0/n;
	}
	
%>
<%
	ArrayList<TTTTT> al = new ArrayList<TTTTT>();
	
		for(int a = 4 ; a <= 9 ; a++){
			for(int b = 4 ; b<= 9 ; b++){
				for(int r = 4 ; r <= 10 ; r++){
				TTTTT t = new TTTTT(a,b,3,r);
				if(a+b>=r){
					al.add(t);
					t.p = calculateP(t.aNum,t.bNum,t.lockNum,t.resultNum);
				}
			}
		}
	}
	
%><html>
<head>
</head>
<body>
<center>
<h2></h2><br><a href="./xxxx_test.jsp">刷新此页面</a><br>
<br>
<br/>
<h2>宠物技能模拟概率</h2>
概率矩阵<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>技能孔</td>
	<td>副宠技能保留概率</td>
	<td>主宠技能保留概率</td></tr>
<%
	for(int i = 0 ; i < 概率.length ; i++){
		out.println("<tr bgcolor='#00FFFF' align='center'><td>"+(i+1)+"</td><td>"+概率[i][0]+"</td><td>"+概率[i][1]+"</td></tr>");
	}	
%>
</table>	

模拟结果<br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
	<td>主宠技能个数</td>
	<td>副宠技能个数</td>
	<td>主宠技能锁定个数</td>
	<td>目标技能最小个数</td>
	<td>概率</td>
	</tr>
<%
	for(TTTTT t : al){
		out.println("<tr bgcolor='#00FFFF' align='center'>");
		out.println("<td>"+t.aNum+"</td>");
		out.println("<td>"+t.bNum+"</td>");
		out.println("<td>"+t.lockNum+"</td>");
		out.println("<td>"+t.resultNum+"</td>");
		int kk = (int)(t.p * 1000);
		out.println("<td>"+(kk/10.0f)+"%</td>");
		out.println("</tr>");
	}
%>
</table>


</center>
</body>
</html> 
