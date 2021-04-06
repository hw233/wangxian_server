package com.xuanzhi.tools.queue;

import junit.framework.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
public class QueueSelectorTestCase extends TestCase{

	public QueueSelectorTestCase(){
		super();
	}
	
	public void testA() throws Exception{
		int n = 5000;
		final int m = 300000;
		final QueueSelector selector = new QueueSelector(5,500);
		final DefaultSelectableQueue queues[] = new DefaultSelectableQueue[n];
		for(int i = 0 ; i < n ; i++){
			queues[i] = new DefaultSelectableQueue(m);
			queues[i].setAttachment(i);
			queues[i].register(selector);
			
		}
		long ll = System.currentTimeMillis();
		int x = 10;
		int total = 0;
		for(int i = 0 ; i < x ; i++){
			Iterator<SelectableQueue> it = selector.selectingSet.iterator();
			while(it.hasNext()){
				SelectableQueue sq = it.next();
				Integer k = (Integer)sq.getAttachment();
				total += k;
			}
		}
		System.out.println("loop cost "+((System.currentTimeMillis() -ll)*1.0f/x)+" ms");
		
		final DefaultQueue dq = new DefaultQueue(m);
		final CountDownLatch latch = new CountDownLatch(10);
		for(int i = 0 ; i < 10 ; i++){
			Thread t = new Thread(new Runnable(){
				public void run(){
					long l = System.currentTimeMillis();
					int count = 0;
					while(Thread.currentThread().isInterrupted() == false){
						Collection<SelectableQueue> set = selector.select(5000L);
						count ++;
						if(set != null){
							Iterator<SelectableQueue> it = set.iterator();
							while(it.hasNext()){
								SelectableQueue queue = it.next();
								Object[] o = new Object[queue.size()];
								for(int i = 0 ; i < o.length ; i++){
									o[i] = queue.pop();
								}
								for(int i = 0 ; i < o.length ; i++){
									dq.push(o[i]);
								}
								queue.returnToSelector();
							}
							if(dq.size() == m)
								System.out.println(""+Thread.currentThread().getName()+",count="+count+",dq.size="+dq.size()+", cost "+(System.currentTimeMillis() - l)+" ms");
						}
						if(dq.size() == m) break;
					}
					latch.countDown();
					selector.wakeup();
				}
			},"Thread-"+(i+1));
			t.start();
		}
		int i = 0 ;
		
		long l = System.currentTimeMillis();
		while(i < m){
			int j = (int)(Math.random() * n);
			queues[j].push(""+j+"->"+queues[j].size());
			i++;
		}
		System.out.println("push " + m + " elements cost "+(System.currentTimeMillis() - l)+" ms");
		
		latch.await();
		
		System.out.println("selector.preReadySet.size()=" + selector.preReadySet.size());
		for(i = 0 ; i < n ; i++){
			if(queues[i].size() > 0){
				System.out.print("QUEUE["+i+","+(queues[i].nextTimeout - System.currentTimeMillis())+"ms,"+(selector.selectingSet.contains(queues[i]))+","+(selector.preReadySet.find(queues[i]))+"]:");
				while(!queues[i].isEmpty()){
					System.out.print(queues[i].pop()+",");
				}
				System.out.println();
		//		LinkedList<String> log = selector.logger.get(queues[i]);
		//		Iterator<String> it = log.iterator();
		//		while(it.hasNext()){
		//			System.out.println(it.next());
		//		}
			}
		}
		assertEquals(m,dq.size());
		
		
	}

}
