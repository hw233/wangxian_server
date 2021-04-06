package com.xuanzhi.tools.ds;

import java.util.Random;

import junit.framework.TestCase;

/**
 *  a ---> b ----> c
 * @author <a href='mailto:myzdf.bj@gmail.com'>Yugang Wang</a>
 * 2009-2-13
 */
public class ProbabilityTest2 extends TestCase{
	
	
		
	public void testA(){
		double p1 = 0.2;
		double p2 = 0.1;
		int n = 1000;
		double r = 1;
		double bk_1 = p1;
		
		double count = 0;
		for(int i = 1 ; i <= n  ; i++){
			bk_1 = B(p1,p2,i,bk_1,r);
			r = (1- p1) * r;
			count += p2 * (i+1) * bk_1;
		}
		System.out.println("p1="+p1+",p2="+p2+",avg="+count+",n="+n);
	}
	
	public double B(double p1,double p2,int k,double bk_1,double r){
		if(k == 1){
			return p1;
		}else{
			return (1-p2)* bk_1 + r*p1;
		}
	}
}	
	
	
	
