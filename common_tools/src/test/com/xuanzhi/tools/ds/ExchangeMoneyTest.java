package com.xuanzhi.tools.ds;

import junit.framework.*;

/**
 * 
 * @author <a href='mailto:myzdf.bj@gmail.com'>Yugang Wang</a>
 * 2009-1-11
 */
public class ExchangeMoneyTest extends TestCase{

	public void testFor1And5And10() throws Exception{
		int n = 100;
		int r1,r2;
		long t1,t2;
		
		t1 = System.currentTimeMillis();
		r1 = forceLaborFor1And5And10(n);
		t1 = System.currentTimeMillis() - t1;
		
		t2 = System.currentTimeMillis();
		r2 = quickCalculateFor1And5And10(n);
		t2 = System.currentTimeMillis() - t2;
		
		System.out.println("1,5,10: n = "+n+", forceLabor: r = " + r1 + ", t = " + t1);
		System.out.println("1,5,10: n = "+n+", mathatics:  r = " + r2 + ", t = " + t2);
		
		n = 5000;
		
		t1 = System.currentTimeMillis();
		r1 = forceLaborFor1And5And10(n);
		t1 = System.currentTimeMillis() - t1;
		
		t2 = System.currentTimeMillis();
		r2 = quickCalculateFor1And5And10(n);
		t2 = System.currentTimeMillis() - t2;
		
		System.out.println("1,5,10: n = "+n+", forceLabor: r = " + r1 + ", t = " + t1);
		System.out.println("1,5,10: n = "+n+", mathatics:  r = " + r2 + ", t = " + t2);
	}
	
	public void testFor1And5And10And20() throws Exception{
		int n = 100;
		int r1,r2;
		long t1,t2;
		
		t1 = System.currentTimeMillis();
		r1 = forceLaborFor1And5And10And20(n);
		t1 = System.currentTimeMillis() - t1;
		
		t2 = System.currentTimeMillis();
		r2 = quickCalculateFor1And5And10And20(n);
		t2 = System.currentTimeMillis() - t2;
		
		System.out.println("1,5,10,20: n = "+n+", forceLabor: r = " + r1 + ", t = " + t1);
		System.out.println("1,5,10,20: n = "+n+", mathatics:  r = " + r2 + ", t = " + t2);
		
		n = 5000;
		
		t1 = System.currentTimeMillis();
		r1 = forceLaborFor1And5And10And20(n);
		t1 = System.currentTimeMillis() - t1;
		
		t2 = System.currentTimeMillis();
		r2 = quickCalculateFor1And5And10And20(n);
		t2 = System.currentTimeMillis() - t2;
		
		System.out.println("1,5,10,20: n = "+n+", forceLabor: r = " + r1 + ", t = " + t1);
		System.out.println("1,5,10,20: n = "+n+", mathatics:  r = " + r2 + ", t = " + t2);
	}
	
	public int forceLaborFor1And5And10(int n){
		int r = 0;
		
		int c = n/10;
		int b = n/5;
		int d = n;
		
		int x,y,z;
		for(int i = 0 ; i <= c ; i++){
			x = 10 * i;
			for(int j = 0 ; j <= b ; j++){
				y = x + j * 5;
				if(y > n) break;
				for(int k = 0 ; k <= d ; k++){
					z = y + k;
					if(z > n) break;
					
					if(z == n){
						r++;
					}
				}
			}
		}
		return r;
	}
	
	public int quickCalculateFor1And5And10(int n){
		int r = 0;
		int c = n/10;
		for(int i = 0 ; i <= c ; i++){
			int m = n - 10 * i;
			r += m/5 + 1;
		}
		return r;
	}
	
	public int forceLaborFor1And5And10And20(int n){
		int r = 0;
		int a = n/20;
		int c = n/10;
		int b = n/5;
		int d = n;
		int x,y,z,w;
		for(int l = 0 ; l <= a ; l++){
			x = l * 20;
			for(int i = 0 ; i <= c ; i++){
				y = x + i * 10;
				if(y > n) break;
				for(int j = 0 ; j <= b ; j++){
					z = y + j * 5;
					if(z > n) break;
					for(int k = 0 ; k <= d ; k++){
						w = z + k;
						if(w > n) break;
						
						if(w == n){
							r++;
						}
					}
				}
			}
		}
		return r;
	}
	
	public int quickCalculateFor1And5And10And20(int n){
		int r = 0;
		int c = n/20;
		for(int i = 0 ; i <= c ; i++){
			int m = n - 20 * i;
			int d = m/10;
			for(int j = 0 ; j <= d ; j++){
				int x = m - 10 * j;
				r += x/5 + 1;
			}
		}
		return r;
	}
}
