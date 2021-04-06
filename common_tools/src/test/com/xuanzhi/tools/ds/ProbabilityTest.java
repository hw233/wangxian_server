package com.xuanzhi.tools.ds;

import java.util.Random;

import junit.framework.TestCase;

public class ProbabilityTest extends TestCase{

	public void testA(){
		int n = 100000;
		p2 = 0.2;
		p3 = 0.1;
		Object start = b;
		Object goal = d;
		int total = 0;
		for(int i = 0 ; i < n ; i++){
			int count = 0;
			Object o = start;
			while(o != goal){
				o = F1(o);
				count++;
			}
			total += count;
		}
		System.out.println("start=" +start+",goal="+goal+ ",n="+n+",total="+total+",avg="+(total*1.0f/n)+",g="+g(p2,p3));
	}
	
	public void testE(){
		int n = 100000;
		p1 = 0.8;
		p2 = 0.64;
		p3 = 0.456;
		p4 = 0.1;
		Object start = a;
		Object goal = e;
		int total = 0;
		for(int i = 0 ; i < n ; i++){
			int count = 0;
			Object o = start;
			while(o != goal){
				o = F1(o);
				count++;
			}
			total += count;
		}
		System.out.println("start=" +start+",goal="+goal+ ",n="+n+",total="+total+",avg="+(total*1.0f/n) + ",f="+f(p1,p2,p3,p4)+",x="+x(p1,p2,p3,p4));
	}

	/**
	 * 通过数学计算得到的公式：
	 *   Sigema(kEk) = 1/p1 + Sigema(Bk) + Sigema(Ck) + Sigema(Dk) + Sigema(Ek)
	 *   
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return
	 */
	public static double g(double p1,double p2){
		return 1/p1 + 1/p2 + (1-p2)/(2*p1*p2);
	}

	
	/**
	 * 通过数学计算得到的公式：
	 *   Sigema(kEk) = 1/p1 + Sigema(Bk) + Sigema(Ck) + Sigema(Dk) + Sigema(Ek)
	 *   
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return
	 */
	public static double f(double p1,double p2,double p3,double p4){
		return 1/p1 + (1-p3+p4+3*p3*p4)/(4*p2*p3*p4) + (1+p4)/(2*p3*p4) + 1/p4;
	}
	
	public static double x(double p1,double p2,double p3,double p4){
		return 1/p1 + 1/p2 + 1/p3 + (1 - p3)/(2*p2*p3) +  1/p4 + (1-p4)/(2*p4)*(1/p3 + (1 - p3)/(2*p2*p3));
	}

	//public static double x(double p1,double p2,double p3,double p4){
	//	return 1/p1 + 1/p2 + (1 - p3)/(2*p2) + 1/p3 + (1 - p4)/(2*p3) + 1/p4;
	//}

	static Object a = new Object(){
		public String toString(){
			return "A";
		}
	};
	static Object b = new Object(){
		public String toString(){
			return "B";
		}
	};
	static Object c = new Object(){
		public String toString(){
			return "C";
		}
	};
	static Object d = new Object(){
		public String toString(){
			return "D";
		}
	};
	static Object e = new Object(){
		public String toString(){
			return "E";
		}
	};
	static double p1 = 0.7;
	static double p2 = 0.5;
	static double p3 = 0.3;
	static double p4 = 0.1;
		
	static Random r = new Random(System.currentTimeMillis());
	
	public static Object F1(Object o){
		if(o == a){
			if(r.nextDouble() < p1){
				return b;
			}else{
				return a;
			}
		}else if(o == b){
			if(r.nextDouble() < p2){
				return c;
			}else{
				return b;
			}
		}else if(o == c){
			if(r.nextDouble() < p3){
				return d;
			}else{
				if(r.nextDouble() < 0.5)
					return c;
				else
					return b;
			}
		}else if(o == d){
			if(r.nextDouble() < p4){
				return e;
			}else{
				if(r.nextDouble() < 0.5)
					return d;
				else
					return c;
			}
		}
		else{
			throw new RuntimeException("invalid argument!");
		}
		
	}
}
