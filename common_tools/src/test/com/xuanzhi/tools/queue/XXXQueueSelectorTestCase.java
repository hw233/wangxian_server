package com.xuanzhi.tools.queue;

import junit.framework.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
public class XXXQueueSelectorTestCase extends TestCase{

	public XXXQueueSelectorTestCase(){
		super();
	}
	
	public void testA() throws Exception{
		int n = 100;
		final int m = 300000;
		final XXXQueueSelector selector = new XXXQueueSelector(n);
		final XXXSelectableQueue queues[] = new XXXSelectableQueue[n];
		for(int i = 0 ; i < n ; i++){
			queues[i] = new XXXSelectableQueue(m);
			queues[i].setAttachment(i);
			queues[i].register(selector);
		}
		
		int t = 10000;
		for(int i = 0 ; i < t ; i++){
			int k = (int)(Math.random() * n);
			queues[k].push("");
		}
		
		
		for(int i = 0 ; i < selector.xxxCount.length; i++){
			System.out.print("LAYER["+i+"]: ");
			for(int j = 0 ; j < selector.xxxCount[i].length ; j++){
				System.out.print(String.format("%4d", selector.xxxCount[i][j]));
			}
			System.out.println();
		}
		int count = 0;
		while(count < t){
			XXXSelectableQueue q = selector.select(1000);
			if(q != null){
				while(q.pop() != null){
					count++;
				}
			}
			System.out.println("count = " + count);
		}
		
		
		
				
		
	}

}
