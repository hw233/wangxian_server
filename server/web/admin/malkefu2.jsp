<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.io.*,com.xuanzhi.tools.text.*"%>
<%!
	
	
	Random random = new Random(System.currentTimeMillis());
	
	
	//在A上做加法运行
	void matrix_add(double[][] A,double a,double [][]B,double b){
		for(int i = 0 ; i < A.length ; i++){
			for(int j = 0 ; j < A[0].length ; j++){
				A[i][j] = A[i][j] * a + B[i][j]*b;
			}
		}
	}
	
	//得到一个新的矩阵
	double[][] matrix_multi(double[][] A,double [][]B){
		
		int m = A.length;
		int n = A[0].length;
		if(B.length != n) throw new IllegalArgumentException("矩阵不能相乘！");
		int p = B[0].length;
		
		double [][] C = new double[m][p];
		for(int i = 0 ; i < A.length ; i++){
			for(int j = 0 ; j < p ; j++){
				double d = 0;
				for(int k = 0 ; k < n ; k++){
					d+= A[i][k] * B[k][j];
				}
				C[i][j] = d;
			}
		}
		return C;
	}
	
	String format_matrix(double[][]A,String title){
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>\n");
		sb.append("<tr align='center'><td>"+title+"</td></tr>");
		sb.append("<tr align='center'><td>\n");
		sb.append("<table border='0' cellpadding='0' cellspacing='1' width='100%' bgcolor='#000000' align='center'>\n");
		sb.append("<tr bgcolor='#DDEEFF' align='center'>");
		for(int i = 0 ; i <= A[0].length ; i++){
			if(i > 0){
				sb.append("<td>"+(i-1)+"</td>");
			}else{
				sb.append("<td>&nbsp;&nbsp;&nbsp;</td>");
			}
		}
		sb.append("</tr>");
		
		for(int i = 0 ; i < A.length ; i++){
			sb.append("<tr bgcolor='#FFFFFF' align='center'>");
			for(int j = 0 ; j <= A[0].length ; j++){
				if(j > 0){
					sb.append("<td>"+format_double(A[i][j-1],2)+"</td>");
				}else{
					sb.append("<td bgcolor='#DDEEFF'>"+i+"</td>");
				}
			}
			sb.append("</tr>");
			
		}
		sb.append("</table>");
		sb.append("</td></tr>\n");
		sb.append("</table>");
		
		return sb.toString();
		
	}
	
	//n标识小数点后几位
	String format_double(double d,int n){
		if(Math.abs(d) < 0.000001) return "";
		String s = String.valueOf((long)(d * 1000000));
		while(s.length() < 6){
			s = "0"+s;
		}
		if(s.length() == 6){
			s = "0."+s;
		}else{
			s = s.substring(0,s.length()-6) + "." + s.substring(s.length()-6);
		}
		int k = s.indexOf(".");
		if(k == -1) return s;
		String s1 = s.substring(0,k);
		String s2 = s.substring(k+1);
		if(s2.length() <= n) return s;
		s2 = s2.substring(0,n);
		return s1 + "." + s2;
	}
	
		
	//模拟一步转移
	int simulate_one_step(double[][] P2,int startLevel){
		double d[] = P2[startLevel];
		double p = random.nextDouble();
		double k = 0;
		int endLevel = -1;
		for(int i = 0 ; i < d.length ; i++){
			if(p >= k && p < k + d[i]){
				endLevel = i;
				break;
			}
			k += d[i];
		}
		if(endLevel == -1){
			endLevel = startLevel;
		}
		return endLevel;
	}
	
	double[][] expectation_value(double[][] P){
		double f[] = new double[P.length-1];
		for(int x = 0 ; x < f.length ; x++){
			double q = 0;
			for(int i = 0; i <= x-1 ; i++){
				double p = 0;
				for(int j = 0 ; j <= i ; j++){
					p +=P[x][j];
				}
				q += p * f[i];
			}
			f[x] = (1 + q)/P[x][x+1];
		}
		double[][] ev = new double[P.length][P[0].length];
		for(int i = 0 ; i < P.length ; i++){
			for(int j = 0 ; j < P[0].length ; j++){
				if(i>=j){
					ev[i][j] = 0;
				}else{
					for(int k = i ; k < j ; k++){
						ev[i][j] += f[k];
					}
				}
			}
		}
		
		return ev;
	}
	
	//计算从i级别升到j级别，恰好用了k的概率矩阵Q
	
	ArrayList<double[][]> cal_Q(double[][]P,int n){
		ArrayList<double[][]> qlist = new ArrayList<double[][]>();
		
		int k = 1;
		while(k < n){
			double[][] Q = new double[P.length][P[0].length];
			for(int i = 0 ; i < P.length ; i++){
				for(int j = 0 ; j < P[0].length ; j++){
					if(i == j){
						Q[i][j] = 0;
					}else if(k == 1){
						Q[i][j] = P[i][j];
					}else{
						double[][] R = qlist.get(k-2);
						
						for(int l = 0 ; l < P.length ; l++){
							if(l != j){
								Q[i][j] +=  P[i][l] * R[l][j];
							}
						}
					}
				}
			}
			qlist.add(Q);
			k++;
			
		}
		
		return qlist;
	}
	
	
%><% 
	int step = 1000;
	int ratetype = 0;
	try{
		ratetype = Integer.parseInt(request.getParameter("ratetype"));
	}catch(Exception e){}
	double P[][] = null;
	
	EquipmentUpgradeMamager em = EquipmentUpgradeMamager.getInstnace();
	if(ratetype == 0){
		P = em.P;
	}else if(ratetype == 1){
		P = em.P10;
	}else if(ratetype == 2){
		P = em.P15;
	}else if(ratetype == 3){
		P = em.PPB;
	}
	
	ArrayList<double[][]> calResult =  new ArrayList<double[][]>();
	
	
	int simulateTimes = 100;
	int startLevel = 0;
	int endLevel = 10;
	try{
		startLevel = Integer.parseInt(request.getParameter("startLevel"));
		endLevel = Integer.parseInt(request.getParameter("endLevel"));
		simulateTimes = Integer.parseInt(request.getParameter("simulateTimes"));
	}catch(Exception e){}
	int stepCounts[] = new int[simulateTimes];
	
	String action = request.getParameter("action");
	if(action == null) action = "";
	

	if(action.equals("cal")){
		step = Integer.parseInt(request.getParameter("step"));
		
		calResult.add(P);
		
		for(int i = 0 ; i < step ; i++){
			double[][] Q = calResult.get(i);
			calResult.add(matrix_multi(Q,P));
		}
		
	}else if(action.equals("simulate")){
		

		if(startLevel < 0 || startLevel > 16) startLevel = 16;
		if(endLevel < 0 || endLevel > 16) endLevel = 16;
		stepCounts = new int[simulateTimes];
			
		
		for(int i = 0 ;i < simulateTimes ; i++){
			int sl = startLevel;
			int stepCount = 0;
			while(sl != endLevel){
				sl = simulate_one_step(P,sl);
				stepCount ++;
				if(stepCount > 1000000){
					break;
				}
			}
			stepCounts[i] = stepCount;
		}
		
		
	}else if(action.equals("huitu")){
		
	}
%>	
<%@page import="com.fy.engineserver.datasource.props.EquipmentUpgradeMamager"%>
<%@include file="IPManager.jsp" %><html><head>
<!--[if IE]><script language="javascript" type="text/javascript" src="/game_server/js/flotr/lib/excanvas.js"></script><![endif]-->
<script language="javascript" type="text/javascript" src="/game_server/js/flotr/lib/prototype-1.6.0.2.js"></script>
<script language="javascript" type="text/javascript" src="/game_server/js/flotr/flotr-0.2.0-alpha.js"></script>
</HEAD>
<BODY>
<h2>装备强化模型演算</h2>
<h4>初始化设定：</h4>
<form name="xxxxx">
<select name='ratetype'>
<option value='0' <%= ratetype==0?"selected":"" %> >原始转移矩阵</option>
<option value='1' <%= ratetype==1?"selected":"" %> >幸运符</option>
<option value='2' <%= ratetype==2?"selected":"" %> >超级幸运符</option>
<option value='3' <%= ratetype==3?"selected":"" %> >保级幸运符</option>
</select>
<input type='submit' value='提交'>
</form>
<%= format_matrix(P,"转移矩阵") %>
<hr/>
<br>
<form name='ff'>
<input type='hidden' name='action' value='cal'>
<input type='hidden' name='ratetype' value='<%=ratetype %>'>
<input type='hidden' name='startLevel' size='5' value='<%= startLevel %>'>
<input type='hidden' name='endLevel' size='5' value='<%= endLevel %>'>
<input type='hidden' name=simulateTimes size='5' value='<%= simulateTimes %>'>
请输入演算的步数：<input type='text' name='step' size='5' value='<%= step %>'>&nbsp;&nbsp;&nbsp;
<input type='submit' value='演  算'>
</form>
<hr/>

<form name='ff4'>
<input type='hidden' name='action' value='huitu'>
<input type='hidden' name='ratetype' value='<%=ratetype %>'>
<input type='hidden' name='startLevel' size='5' value='<%= startLevel %>'>
<input type='hidden' name='endLevel' size='5' value='<%= endLevel %>'>
<input type='hidden' name=simulateTimes size='5' value='<%= simulateTimes %>'>
请输入分布图的最大坐标（次数）：<input type='text' name='step' size='5' value='<%= step %>'>&nbsp;&nbsp;&nbsp;
<input type='submit' value='绘制分布图'>
</form>
<hr/>
<br>

<hr/>

<form name='fff'>
<input type='hidden' name='action' value='simulate'>
<input type='hidden' name='ratetype' value='<%=ratetype %>'>
请输入模拟起始等级：<input type='text' name='startLevel' size='5' value='<%= startLevel %>'>&nbsp;&nbsp;&nbsp;
模拟停止等级：<input type='text' name='endLevel' size='5' value='<%= endLevel %>'>&nbsp;&nbsp;&nbsp;
模拟的次数：<input type='text' name=simulateTimes size='5' value='<%= simulateTimes %>'>&nbsp;&nbsp;&nbsp;<br>

<input type='submit' value='模   拟'>
</form>
<hr>
<% if(action.equals("huitu")){ 
	out.println("绘图：");
	ArrayList<double[][]> QLIST = cal_Q(P,step);
	
	double[][] ev1 = new double[P.length][P.length];
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			
			for(int k = 0 ; k < QLIST.size() ; k++){
				double[][] d = QLIST.get(k);
				ev1[i][j] +=  (k+1) * d[i][j];
			}
		}
	}
	
	double[][] dv1 = new double[P.length][P.length];
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			
			for(int k = 0 ; k < QLIST.size() ; k++){
				double[][] d = QLIST.get(k);
				dv1[i][j] +=  (k+1 - ev1[i][j])*(k+1 - ev1[i][j]) * d[i][j];
			}
		}
	}
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			dv1[i][j] = Math.sqrt(dv1[i][j]);
		}
	}
	

	out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
	out.println("<tr><td>"+format_matrix(ev1,"未调整的【i->j】强化次数期望矩阵")+"</td></tr>");
	
	out.println("</table><br>");
	 
	
	out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
	out.println("<tr><td>"+format_matrix(dv1,"未调整的【i->j】强化次数标准方差矩阵")+"</td></tr>");
	
	out.println("</table><br>");
	
	out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
	out.println("<tr>");
	int count = 0;
	for(int i = 0 ; i < P.length -1; i++){
		for(int j = i+1 ; j < i+2 ; j++){
			out.println("<td>从"+i+"级别到"+j+"级别k步成功概率分布<br><div id='container_"+i+"_"+j+"' style='width:600px;height:300px;'></div></td>");
			count ++;
			if(count % 2 == 0){
				out.println("</tr><tr>");
			}
		}
	}
	for(int i = 0 ; i < P.length -1;){
		for(int j = i+4 ; j < i+5 ; j++){
			out.println("<td>从"+i+"级别到"+j+"级别k步成功概率分布<br><div id='container_"+i+"_"+j+"' style='width:600px;height:300px;'></div></td>");
			count ++;
			if(count % 2 == 0){
				out.println("</tr><tr>");
			}
		}
		i = i + 4;
	}
	out.println("</tr></table><br>");
	
	out.println("<script>\n");
	out.println("function draw_Flotr(){\n");
	out.println("var f;\n");
	
	for(int i = 0 ; i < P.length -1; i++){
		for(int j = i+1 ; j < i+2 ; j++){
			
			String id = "container_"+i+"_"+j+"";
			out.println("f =  Flotr.draw($('"+id+"'),[\n");
			out.println("{\n");
			//
			StringBuffer sbb = new StringBuffer();
			for(int k = 0 ; k < QLIST.size() ; k++){
				double [][] d = QLIST.get(k);
				sbb.append("["+(k+1)+","+d[i][j]+"]");
				if(d[i][j] < 0.001){
					break;
				}
				if(k < QLIST.size() -1){
					sbb.append(",");
				}
			}
			
			double ddd = 0;
			for(int k = 0 ; k < QLIST.size() ; k++){
				double [][] d = QLIST.get(k);
				
				if(k <= ev1[i][j]){
					ddd += d[i][j];
				}
			}
			
			out.println("data: [ "+sbb.toString()+" ],\n");
			out.println("label: \"未调整,小于均值"+format_double(ev1[i][j],1)+"成功的概率"+format_double(ddd,2)+"\",\n");
			out.println("lines: {show: true, fill: false},\n points: {show: false}\n");
			out.println("},{\n");
			
			sbb = new StringBuffer();
			double dd = 0;
			for(int k = 0 ; k < QLIST.size() ; k++){
				double [][] d = QLIST.get(k);
				
				if(k>= ev1[i][j] - dv1[i][j] && k < ev1[i][j] + dv1[i][j]){
					sbb.append("["+(k+1)+","+d[i][j]+"]");
					dd += d[i][j];
					
					if(d[i][j] < 0.001){
						break;
					}
					
					if(k+1 < ev1[i][j] + dv1[i][j]){
						sbb.append(",");
					}
				}
			}
			
			
			
			
			out.println("data: [ "+sbb.toString()+" ],\n");
			out.println("label: \"未调整期望～方差概率和"+format_double(dd,2)+"\",\n");
			out.println("lines: {show: false, fill: true},\n points: {show: false}\n");
			out.println("}\n");
			
				
			out.println("],{legend:{position: 'ne'}}\n");
			out.println(");");
		}
	}
	
	for(int i = 0 ; i < P.length -1;){
		for(int j = i+4 ; j < i+5 ; j++){
			String id = "container_"+i+"_"+j+"";
			out.println("f =  Flotr.draw($('"+id+"'),[\n");
			out.println("{\n");
			//
			StringBuffer sbb = new StringBuffer();
			for(int k = 0 ; k < QLIST.size() ; k++){
				double [][] d = QLIST.get(k);
				sbb.append("["+(k+1)+","+d[i][j]+"]");
				if(k > 100 && d[i][j] < 0.0001){
					break;
				}
				if(k < QLIST.size() -1){
					sbb.append(",");
				}
			}
			
			out.println("data: [ "+sbb.toString()+" ],\n");
			out.println("label: \"未调整\",\n");
			out.println("lines: {show: true, fill: false},\n points: {show: false}\n");
			out.println("},{\n");
			
			sbb = new StringBuffer();
			double dd = 0;
			for(int k = 0 ; k < QLIST.size() ; k++){
				double [][] d = QLIST.get(k);
				
				if(k>= ev1[i][j] - dv1[i][j] && k < ev1[i][j] + dv1[i][j]){
					sbb.append("["+(k+1)+","+d[i][j]+"]");
					dd += d[i][j];
					
					if(k > 100 && d[i][j] < 0.0001){
						break;
					}
					
					if(k+1 < ev1[i][j] + dv1[i][j]){
						sbb.append(",");
					}
				}
			}
			out.println("data: [ "+sbb.toString()+" ],\n");
			out.println("label: \"未调整期望～方差概率和"+format_double(dd,2)+"\",\n");
			out.println("lines: {show: false, fill: true},\n points: {show: false}\n");
			out.println("}\n");
			//
			
			
			out.println("],{legend:{position: 'ne'}}\n");
			out.println(");");
		}
		i = i + 4;
	}
	out.println("}");
	out.println("</script>\n");
 }else if(action.equals("cal")){ %>

演算结果（基于期望的方程计算而得）:
<%
	out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
	out.println("<tr><td>"+format_matrix(expectation_value(P),"未调整的【i->j】强化次数期望矩阵")+"</td></tr>");
	out.println("</table><br>");
	

	//
	out.println("演算结果（基于k步恰巧强化成功的概率矩阵，此方法可计算方差）：");
	ArrayList<double[][]> QLIST = cal_Q(P,step);

	
	double[][] ev1 = new double[P.length][P.length];
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			
			for(int k = 0 ; k < QLIST.size() ; k++){
				double[][] d = QLIST.get(k);
				ev1[i][j] +=  (k+1) * d[i][j];
			}
		}
	}
	
	double[][] dv1 = new double[P.length][P.length];
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			
			for(int k = 0 ; k < QLIST.size() ; k++){
				double[][] d = QLIST.get(k);
				dv1[i][j] +=  (k+1 - ev1[i][j])*(k+1 - ev1[i][j]) * d[i][j];
			}
		}
	}
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			dv1[i][j] = Math.sqrt(dv1[i][j]);
		}
	}
	
	
	
	out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
	out.println("<tr><td>"+format_matrix(ev1,"未调整的【i->j】强化次数期望矩阵")+"</td></tr>");
	
	out.println("</table><br>");
	 
	
	out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
	out.println("<tr><td>"+format_matrix(dv1,"未调整的【i->j】强化次数标准方差矩阵")+"</td></tr>");
	
	out.println("</table><br>");
	
%>

<hr/>
演算结果，横向为各级别的装备，纵向为强化多少次，矩阵值为装备强化等级的期望值：<br><br>
<%
	
	
	if(calResult.size() == 0){
		
		double [][] X = new double[calResult.size()][P.length];
		for(int i = 0 ; i < X.length ; i++){
			double[][] d = calResult.get(i);
			for(int j = 0 ; j < X[0].length ; j++){
				X[i][j] = 0;
				for(int k = 0 ; k < d[0].length ; k++){
					X[i][j] += d[j][k] * k;
				}
			}
		}
		
		
		
		out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
		out.println("<tr><td>"+format_matrix(X,"未调整的期望矩阵")+"</td></tr>");
		out.println("</table>");
	}
%>
<hr/>
n步转移矩阵：<br>
<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>
<tr>
<%
//	for(int i = 0 ; i < calResult.size() ; i++){
//		if(i > 0 && i % 3 == 0){
//			out.println("</tr><tr>");
//		}
//		double[][] A = calResult.get(i);
//		String s = format_matrix(A,"第"+(i+1)+"步转移矩阵");
//		out.println("<td>"+s+"</td>");
//		
//	}


%></tr>
</table>
<br/>
k步恰巧强化成功的概率矩阵：<br>
<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>
<tr>
<%
	//for(int i = 0 ; i < QLIST.size() ; i++){
	//	if(i > 0 && i % 3 == 0){
	//		out.println("</tr><tr>");
	//	}
	//	double[][] A = QLIST.get(i);
	//	String s = format_matrix(A,"第"+(i+1)+"步恰巧强化成功的概率矩");
	//	out.println("<td>"+s+"</td>");
	//	
	//}


%></tr>
</table>
<% }else if(action.equals("simulate")){ 
	int average1 = 0;
	for(int i  = 0 ; i < stepCounts.length ; i++){
		average1+=stepCounts[i];
	}
	

%>

模拟结果：从<%=startLevel %>级到<%=endLevel %>级，模拟<%=simulateTimes %>次<br>
原始的矩阵需要强化次数均值为<%= format_double(average1*1.0/simulateTimes,2) %> %>
<br>
<table border='0' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#DDEEFF' align='center'><td>模拟次数</td><td>原始矩阵强化的步数</td><td>调整后矩阵强化的步数</td></tr>
<%
	for(int i = 0 ; i < simulateTimes ; i++){
		%><tr bgcolor='#FFFFFF' align='center'><td><%= i+1 %></td><td><%= stepCounts[i] %></td></tr><%
	}
%>
</table>
<% } %>


</BODY></html>
