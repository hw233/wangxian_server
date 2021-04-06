package com.xuanzhi.tools.ds;

import junit.framework.*;

public class SequnceTest extends TestCase {

	
	public void testA(){
		
		long a = 0x00ff43798545934f5L;
		long b = 0x000ef45004804ff54L;
		long c = 0x00f0ef4500f480454L;
		long d = 0x0ff00098655678345L;
		
		SequenceGenerator sg = new SequenceGenerator(a,b,c,d);
		
		int n = 1000;
		
		long now = System.currentTimeMillis();

		for(int i = 0 ; i < n ; i++){
			long r = sg.next();
			System.out.println("seq["+i+"]=0x" + Long.toHexString(r));
			
			if(i > 10 && sg.get_Index() == -1){
				sg.set(2*i+1,a*i,b*i*i,c^i,d^i);
			}
		}
		
		System.out.println("total cost "+((System.currentTimeMillis()-now))+" ms,avg cost "+((System.currentTimeMillis()-now)/n)+" ms");
	}
	

	
}
