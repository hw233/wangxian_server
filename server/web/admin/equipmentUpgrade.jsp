<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.io.*,com.xuanzhi.tools.text.*,com.fy.engineserver.datasource.props.*"%>
<%!
	File file = new File("/home/game/resin/webapps/game_server/WEB-INF/conf/_malkefu.data");
EquipmentUpgradeMamager eum = EquipmentUpgradeMamager.getInstnace();
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
<%
 double[][] p = eum.getP();
if(p != null){
	String s = format_matrix(p,"p");
	out.println(s);
}
double[][] p5 = eum.getP5();
if(p5 != null){
	String s = format_matrix(p5,"p5");
	out.println(s);
}
double[][] p10 = eum.getP10();
if(p10 != null){
	String s = format_matrix(p10,"p10");
	out.println(s);
}
double[][] p15 = eum.getP15();
if(p15 != null){
	String s = format_matrix(p15,"p15");
	out.println(s);
}
%>

</BODY></html>
