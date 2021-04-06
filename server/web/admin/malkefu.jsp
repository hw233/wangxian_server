<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.io.*,com.xuanzhi.tools.text.*"%>
<%!
	File file = new File("/home/game/resin/webapps/game_server/WEB-INF/_malkefu.data");

	int maxLevel = 10;
	int precision = 2;
	//转移矩阵
	double[][] P = new double[maxLevel+1][maxLevel+1];
	
	Random random = new Random(System.currentTimeMillis());
	
	public void saveFile(int maxLevel,double [][]P){
		try{
			DataOutputStream di = new DataOutputStream(new FileOutputStream(file));
			di.writeInt(maxLevel);
			for(int i = 0 ; i < P.length ; i++){
				for(int j = 0 ; j < P.length ; j++){
					di.writeDouble(P[i][j]);
				}
			}
			di.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void jspInit(){
		
		if(file.exists()){
			try{
				DataInputStream di = new DataInputStream(new FileInputStream(file));
				maxLevel = di.readInt();
				P = new double[maxLevel+1][maxLevel+1];
				for(int i = 0 ; i < P.length ; i++){
					for(int j = 0 ; j < P.length ; j++){
						P[i][j] = di.readDouble();
					}
				}
				di.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			for(int i = 0 ; i < P.length ; i++){
				if(i>0 && i < P.length -1){
					P[i][i] = i*1.0/maxLevel;
					P[i][i+1] = (maxLevel-i)*0.7/maxLevel;
					P[i][i-1] = 1 - P[i][i] - P[i][i+1];
				}else if(i == 0){
					P[i][i] = i*1.0/maxLevel;
					P[i][i+1] = (maxLevel-i)*1.0/maxLevel;
				}else{
					P[i][i] = 1;
					P[i][i-1] = 0;
				}
			}
		}
	}
	
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
					sb.append("<td>"+format_double(A[i][j-1],precision)+"</td>");
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
	
	//rate 提升的成功率   ratetype == 0 表示从低等级开始减失败率，否则从高等级
	double[][] calP2(double rate,int ratetype){
		double[][] P2 = new double[P.length][P[0].length];
		
		for(int i = 0 ; i < P.length ; i++){
			for(int j = 0 ; j < P[0].length ; j++){
				P2[i][j] = P[i][j];
			}
		}
		
		for(int i = 0 ; i < P2.length -1 ; i++){
			double d = P2[i][i+1] + rate;
			if(d > 1) d = 1;
			double r = d - P2[i][i+1];
			P2[i][i+1] = d;
			if(ratetype == 0){
				for(int j = 0 ; j <= i ; j++){
					if(r < 0.00001) break;
					if(P2[i][j] >= r){
						P2[i][j] = P2[i][j] - r;
						r = 0;
					}else{
						r = r - P2[i][j];
						P2[i][j] = 0;
					}
				}
			}else{
				for(int j = i ; j >= 0 ; j--){
					if(r < 0.00001) break;
					if(P2[i][j] >= r){
						P2[i][j] = P2[i][j] - r;
						r = 0;
					}else{
						r = r - P2[i][j];
						P2[i][j] = 0;
					}
				}
			}
		}
		return P2;
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
	double rate = 0.1;
	int ratetype = 0;
	
	ArrayList<double[][]> calResult =  new ArrayList<double[][]>();
	ArrayList<double[][]> calResult2 =  new ArrayList<double[][]>();
	
	
	int simulateTimes = 100;
	int startLevel = 0;
	int endLevel = 10;
	try{
		startLevel = Integer.parseInt(request.getParameter("startLevel"));
		endLevel = Integer.parseInt(request.getParameter("endLevel"));
		simulateTimes = Integer.parseInt(request.getParameter("simulateTimes"));
	}catch(Exception e){}
	int stepCounts[] = new int[simulateTimes];
	int stepCounts2[] = new int[simulateTimes];
	
	String action = request.getParameter("action");
	if(action == null) action = "";
	if(action.equals("init_p2")){
		
		double p1 = Double.parseDouble(request.getParameter("p1"));
		double p2 = Double.parseDouble(request.getParameter("p2"));
		double q2 = Double.parseDouble(request.getParameter("q2"));
		
		for(int i = 0 ; i < P.length -1 ; i++){
			P[i][i] = p1 + i * (p2 - p1)/(P.length-2);
			P[i][i+1] = (1-p1) + i * (q2 - (1-p1))/(P.length-2);
			if(i > 0)
				P[i][i-1] = 1 - P[i][i] - P[i][i+1];
		}
		P[P.length-1][P.length-1] = 1.0;
		
		saveFile(maxLevel,P);
		
	}else if(action.equals("init_p")){
		int l = Integer.parseInt(request.getParameter("max_level"));
		if(l != maxLevel){
			maxLevel = l;
			P = new double[maxLevel+1][maxLevel+1];
		}
		precision = Integer.parseInt(request.getParameter("precision"));
		
		Enumeration e = request.getParameterNames();
		while(e.hasMoreElements()){
			String s = (String)e.nextElement();
			String v = request.getParameter(s);
			if(v.trim().length() == 0) v = "0";
			if(s.startsWith("P_")){
				s = s.substring(2);
				String ss[] = s.split("_");
				int i = Integer.parseInt(ss[0]);
				int j = Integer.parseInt(ss[1]);
				if(i < P.length && j < P[0].length){
					P[i][j] = Double.parseDouble(v);
				}
			}
		}
		
		for(int i = 0 ; i < P.length ; i++){
			double d = 0;
			for(int j = 0 ; j < P[0].length  ; j++){
				if(j != i)
					d += P[i][j];
			}
			P[i][i] = 1 - d;
		}
		
		saveFile(maxLevel,P);
		
	}else if(action.equals("huitu")){
		step = Integer.parseInt(request.getParameter("step"));
		rate = Double.parseDouble(request.getParameter("rate"));
		ratetype = Integer.parseInt(request.getParameter("ratetype"));
		
	}
	else if(action.equals("cal")){
		step = Integer.parseInt(request.getParameter("step"));
		rate = Double.parseDouble(request.getParameter("rate"));
		ratetype = Integer.parseInt(request.getParameter("ratetype"));
		
		double[][] P2 = calP2(rate,ratetype);
		
		calResult.add(P);
		
		for(int i = 0 ; i < step ; i++){
			double[][] Q = calResult.get(i);
			calResult.add(matrix_multi(Q,P));
		}
		
		calResult2.add(P2);
		for(int i = 0 ; i < step ; i++){
			double[][] Q = calResult2.get(i);
			calResult2.add(matrix_multi(Q,P2));
		}
		
		
	}else if(action.equals("simulate")){
		
		ratetype = Integer.parseInt(request.getParameter("ratetype"));
		if(startLevel < 0 || startLevel > maxLevel) startLevel = maxLevel;
		if(endLevel < 0 || endLevel > maxLevel) endLevel = maxLevel;
		stepCounts = new int[simulateTimes];
		stepCounts2 = new int[simulateTimes];
			
		double[][] P2 = calP2(rate,ratetype);
		
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
		
		for(int i = 0 ;i < simulateTimes ; i++){
			int sl = startLevel;
			int stepCount = 0;
			while(sl != endLevel){
				sl = simulate_one_step(P2,sl);
				stepCount ++;
				if(stepCount > 1000000){
					break;
				}
			}
			stepCounts2[i] = stepCount;
		}
	}else if(action.equals("huitu")){
		
	}
%>	
<%@include file="IPManager.jsp" %><html><head>
<!--[if IE]><script language="javascript" type="text/javascript" src="/game_server/js/flotr/lib/excanvas.js"></script><![endif]-->
<script language="javascript" type="text/javascript" src="/game_server/js/flotr/lib/prototype-1.6.0.2.js"></script>
<script language="javascript" type="text/javascript" src="/game_server/js/flotr/flotr-0.2.0-alpha.js"></script>
</HEAD>
<BODY <% if(action.equals("huitu")){out.print("onload='draw_Flotr();'");}%> >
<h2>装备强化模型演算</h2>
<h4>初始化设定：</h4>
<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>
<tr align='center'><td>
<form id='f' name='f'>
<input type='hidden' name='action' value='init_p'>
请输入装备强化的最大等级：<input type='text' name='max_level' size='5' value='<%=maxLevel %>'>&nbsp;&nbsp;
概率矩阵显示的精度：<input type='text' name='precision' size='5' value='<%=precision %>'>&nbsp;&nbsp;
<br>
请输入转移矩阵，矩阵(i,j)表示装备从i级一步强化到j级的概率<br>

<%
StringBuffer sb = new StringBuffer();
sb.append("<table border='0' cellpadding='0' cellspacing='1'  bgcolor='#000000'>\n");
sb.append("<tr bgcolor='#DDEEFF' align='center'>");
for(int i = 0 ; i <= P[0].length ; i++){
	if(i > 0){
		sb.append("<td>"+(i-1)+"</td>");
	}else{
		sb.append("<td>&nbsp;&nbsp;&nbsp;</td>");
	}
}
sb.append("</tr>");

for(int i = 0 ; i < P.length ; i++){
	sb.append("<tr bgcolor='#FFFFFF' align='center'>");
	for(int j = 0 ; j <= P[0].length ; j++){
		if(j > 0){
			if(j -1 < i + 2)
				sb.append("<td><input type='text' name='P_"+i+"_"+(j-1)+"' size='5' value='"+format_double(P[i][j-1],precision)+"'></td>");
			else
				sb.append("<td>"+format_double(P[i][j-1],precision)+"</td>");
		}else{
			sb.append("<td bgcolor='#DDEEFF'>"+i+"</td>");
		}
	}
	sb.append("</tr>");
	
}
sb.append("</table>");
out.print(sb);

%>
<input type='submit' value='提  交'>
</form>
</td><td>
<form id='f3' name='f3'>
快速设定初始矩阵：<br>
<input type='hidden' name='action' value='init_p2'>
&nbsp;&nbsp;请输入矩阵(0,0)位置值：<input type='text' name='p1' size='5' value='<%= P[0][0] %>'>&nbsp;&nbsp;<br>
&nbsp;&nbsp;请输入矩阵(<%= maxLevel-1 %>,<%=maxLevel-1 %>)位置值：<input type='text' name='p2' size='5' value='<%= P[maxLevel-1][maxLevel-1] %>'>&nbsp;&nbsp;<br>
&nbsp;&nbsp;请输入矩阵(<%= maxLevel-1 %>,<%=maxLevel %>)位置值：<input type='text' name='q2' size='5' value='<%=P[maxLevel-1][maxLevel] %>'>&nbsp;&nbsp;<br>
<input type='submit' value='快速设定'>
</form>
<br>
</td></tr>
</table>
<hr/>
<br>
<form name='ff'>
<input type='hidden' name='action' value='cal'>
<input type='hidden' name='startLevel' size='5' value='<%= startLevel %>'>
<input type='hidden' name='endLevel' size='5' value='<%= endLevel %>'>
<input type='hidden' name=simulateTimes size='5' value='<%= simulateTimes %>'>

请输入演算的步数：<input type='text' name='step' size='5' value='<%= step %>'>&nbsp;&nbsp;&nbsp;
成功率整体提高：<input type='text' name='rate' size='5' value='<%= rate %>'>&nbsp;&nbsp;&nbsp;
<input type='radio' name='ratetype' value='0' <%= ratetype==0?"checked='true'":""%> >先减低等级失败率&nbsp;&nbsp;&nbsp;
<input type='radio' name='ratetype' value='1' <%= ratetype==1?"checked='true'":""%> >先减高等级失败率&nbsp;&nbsp;&nbsp;
<input type='submit' value='演  算'>
</form>
<hr/>

<br>
<form name='ff4'>
<input type='hidden' name='action' value='huitu'>
<input type='hidden' name='startLevel' size='5' value='<%= startLevel %>'>
<input type='hidden' name='endLevel' size='5' value='<%= endLevel %>'>
<input type='hidden' name=simulateTimes size='5' value='<%= simulateTimes %>'>
请输入分布图的最大坐标（次数）：<input type='text' name='step' size='5' value='<%= step %>'>&nbsp;&nbsp;&nbsp;
成功率整体提高：<input type='text' name='rate' size='5' value='<%= rate %>'>&nbsp;&nbsp;&nbsp;
<input type='radio' name='ratetype' value='0' <%= ratetype==0?"checked='true'":""%> >先减低等级失败率&nbsp;&nbsp;&nbsp;
<input type='radio' name='ratetype' value='1' <%= ratetype==1?"checked='true'":""%> >先减高等级失败率&nbsp;&nbsp;&nbsp;
<input type='submit' value='绘制分布图'>
</form>
<hr/>

<form name='fff'>
<input type='hidden' name='action' value='simulate'>
请输入模拟起始等级：<input type='text' name='startLevel' size='5' value='<%= startLevel %>'>&nbsp;&nbsp;&nbsp;
模拟停止等级：<input type='text' name='endLevel' size='5' value='<%= endLevel %>'>&nbsp;&nbsp;&nbsp;
模拟的次数：<input type='text' name=simulateTimes size='5' value='<%= simulateTimes %>'>&nbsp;&nbsp;&nbsp;<br>
<input type='radio' name='ratetype' value='0' <%= ratetype==0?"checked='true'":""%> >先减低等级失败率&nbsp;&nbsp;&nbsp;
<input type='radio' name='ratetype' value='1' <%= ratetype==1?"checked='true'":""%> >先减高等级失败率&nbsp;&nbsp;&nbsp;

<input type='submit' value='模   拟'>
</form>
<hr>
<% if(action.equals("huitu")){ 
	out.println("绘图：");
	ArrayList<double[][]> QLIST = cal_Q(P,step);
	ArrayList<double[][]> QLIST2 = cal_Q(calP2(rate,ratetype),step);
	
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
	
	double[][] ev2 = new double[P.length][P.length];
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			
			for(int k = 0 ; k < QLIST2.size() ; k++){
				double[][] d = QLIST2.get(k);
				ev2[i][j] +=  (k+1) * d[i][j];
			}
		}
	}
	
	double[][] dv2 = new double[P.length][P.length];
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			
			for(int k = 0 ; k < QLIST2.size() ; k++){
				double[][] d = QLIST2.get(k);
				dv2[i][j] +=  (k+1 - ev2[i][j])*(k+1 - ev2[i][j]) * d[i][j];
			}
		}
	}
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			dv2[i][j] = Math.sqrt(dv2[i][j]);
		}
	}
	
	out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
	out.println("<tr><td>"+format_matrix(ev1,"未调整的【i->j】强化次数期望矩阵")+"</td><td>"+format_matrix(ev2,"经过调整的【i->j】强化次数期望矩阵")+"</td></tr>");
	
	out.println("</table><br>");
	 
	
	out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
	out.println("<tr><td>"+format_matrix(dv1,"未调整的【i->j】强化次数标准方差矩阵")+"</td><td>"+format_matrix(dv2,"经过调整的【i->j】强化次数标准方差矩阵")+"</td></tr>");
	
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
			out.println("},{\n");
			
			//
			sbb = new StringBuffer();
			for(int k = 0 ; k < QLIST2.size() ; k++){
				double [][] d = QLIST2.get(k);
				
				if(d[i][j] < 0.001){
					break;
				}
				
				sbb.append("["+(k+1)+","+d[i][j]+"]");
				if(k < QLIST2.size() -1){
					sbb.append(",");
				}
			}
			out.println("data: [ "+sbb.toString()+" ],\n");
			out.println("label: \"调整后\",\n");
			out.println("lines: {show: true, fill: false},\n points: {show: false}\n");
			
			out.println("},{\n");
			sbb = new StringBuffer();
			dd = 0;
			for(int k = 0 ; k < QLIST2.size() ; k++){
				double [][] d = QLIST2.get(k);
				if(k>= ev2[i][j] - dv2[i][j] && k < ev2[i][j] + dv2[i][j]){
					sbb.append("["+(k+1)+","+d[i][j]+"]");
					dd += d[i][j];
					
					if(d[i][j] < 0.001){
						break;
					}
					
					
					if(k+1 < ev2[i][j] + dv2[i][j]){
						sbb.append(",");
					}
				}
			}
			out.println("data: [ "+sbb.toString()+" ],\n");
			out.println("label: \"调整后期望～方差概率和"+format_double(dd,2)+"\",\n");
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
			out.println("},{\n");
			//
			sbb = new StringBuffer();
			for(int k = 0 ; k < QLIST2.size() ; k++){
				double [][] d = QLIST2.get(k);
				
				if(k > 100 && d[i][j] < 0.0001){
					break;
				}
				
				sbb.append("["+(k+1)+","+d[i][j]+"]");
				if(k < QLIST2.size() -1){
					sbb.append(",");
				}
			}
			out.println("data: [ "+sbb.toString()+" ],\n");
			out.println("label: \"调整后\",\n");
			out.println("lines: {show: true, fill: false},\n points: {show: false}\n");
			
			out.println("},{\n");
			sbb = new StringBuffer();
			dd = 0;
			for(int k = 0 ; k < QLIST2.size() ; k++){
				double [][] d = QLIST2.get(k);
				if(k>= ev2[i][j] - dv2[i][j] && k < ev2[i][j] + dv2[i][j]){
					sbb.append("["+(k+1)+","+d[i][j]+"]");
					dd += d[i][j];
					
					if(k > 100 && d[i][j] < 0.0001){
						break;
					}
					
					
					if(k+1 < ev2[i][j] + dv2[i][j]){
						sbb.append(",");
					}
				}
			}
			out.println("data: [ "+sbb.toString()+" ],\n");
			out.println("label: \"调整后期望～方差概率和"+format_double(dd,2)+"\",\n");
			out.println("lines: {show: false, fill: true},\n points: {show: false}\n");
			out.println("}\n");
			
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
	out.println("<tr><td>"+format_matrix(expectation_value(P),"未调整的【i->j】强化次数期望矩阵")+"</td><td>"+format_matrix(expectation_value(calP2(rate,ratetype)),"经过调整的【i->j】强化次数期望矩阵")+"</td></tr>");
	out.println("</table><br>");
	

	//
	out.println("演算结果（基于k步恰巧强化成功的概率矩阵，此方法可计算方差）：");
	ArrayList<double[][]> QLIST = cal_Q(P,step);
	ArrayList<double[][]> QLIST2 = cal_Q(calP2(rate,ratetype),step);
	
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
	
	double[][] ev2 = new double[P.length][P.length];
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			
			for(int k = 0 ; k < QLIST2.size() ; k++){
				double[][] d = QLIST2.get(k);
				ev2[i][j] +=  (k+1) * d[i][j];
			}
		}
	}
	
	double[][] dv2 = new double[P.length][P.length];
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			
			for(int k = 0 ; k < QLIST2.size() ; k++){
				double[][] d = QLIST2.get(k);
				dv2[i][j] +=  (k+1 - ev2[i][j])*(k+1 - ev2[i][j]) * d[i][j];
			}
		}
	}
	for(int i = 0 ; i < P.length ; i++){
		for(int j = 0 ; j < P.length ; j++){
			dv2[i][j] = Math.sqrt(dv2[i][j]);
		}
	}
	
	out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
	out.println("<tr><td>"+format_matrix(ev1,"未调整的【i->j】强化次数期望矩阵")+"</td><td>"+format_matrix(ev2,"经过调整的【i->j】强化次数期望矩阵")+"</td></tr>");
	
	out.println("</table><br>");
	 
	
	out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
	out.println("<tr><td>"+format_matrix(dv1,"未调整的【i->j】强化次数标准方差矩阵")+"</td><td>"+format_matrix(dv2,"经过调整的【i->j】强化次数标准方差矩阵")+"</td></tr>");
	
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
		
		double [][] Y = new double[calResult2.size()][P.length];
		for(int i = 0 ; i < Y.length ; i++){
			double[][] d = calResult2.get(i);
			for(int j = 0 ; j < Y[0].length ; j++){
				Y[i][j] = 0;
				for(int k = 0 ; k < d[0].length ; k++){
					Y[i][j] += d[j][k] * k;
				}
			}
		}
		
		out.println("<table border='0' cellpadding='0' cellspacing='0' width='100%' bgcolor='#FFFFFF' align='center'>");
		out.println("<tr><td>"+format_matrix(X,"未调整的期望矩阵")+"</td><td>"+format_matrix(Y,"经过调整的期望矩阵")+"</td></tr>");
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
	int average2 = 0;
	for(int i  = 0 ; i < stepCounts2.length ; i++){
		average2+=stepCounts2[i];
	}

%>

模拟结果：从<%=startLevel %>级到<%=endLevel %>级，模拟<%=simulateTimes %>次<br>
原始的矩阵需要强化次数均值为<%= format_double(average1*1.0/simulateTimes,2) %>，调整后的矩阵需要强化次数均值为<%= format_double(average2*1.0/simulateTimes,2) %>
<br>
<table border='0' cellpadding='0' cellspacing='1' bgcolor='#000000' align='center'>
<tr bgcolor='#DDEEFF' align='center'><td>模拟次数</td><td>原始矩阵强化的步数</td><td>调整后矩阵强化的步数</td></tr>
<%
	for(int i = 0 ; i < simulateTimes ; i++){
		%><tr bgcolor='#FFFFFF' align='center'><td><%= i+1 %></td><td><%= stepCounts[i] %></td><td><%= stepCounts2[i] %></td></tr><%
	}
%>
</table>
<% } %>


</BODY></html>
