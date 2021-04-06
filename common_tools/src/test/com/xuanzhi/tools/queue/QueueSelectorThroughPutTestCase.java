package com.xuanzhi.tools.queue;

import junit.framework.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class QueueSelectorThroughPutTestCase extends TestCase{

	public QueueSelectorThroughPutTestCase(){
		super();
	}
	int queueNum = 1000;
	int beatheartNum = 50;
	int selectThreadNum = 50;
	int queueMaxSize = 1000;
	//每次心跳在每个队列上发送多少个消息
	int messageNum = 10; 
	
	int handleMessageCost = 10000;
	
	
	public static void main(String args[]) throws Exception{
		QueueSelectorThroughPutTestCase qqq = new QueueSelectorThroughPutTestCase();
		
		for(int i = 0 ; i < args.length ; i++){
			if(args[i].equals("-h")){
				System.out.println("Usage  : java -cp . QueueSelectorThroughPutTestCase -q queuenum -b beatheartNum -s selectThreadNum -n messageNum -m queueMaxSize -c handleMessageCost");
				System.out.println("Author : myzdf");
				System.out.println("Version: 0.1");
				
				System.exit(0);
			}else if(args[i].equals("-q") && i < args.length -1){
				qqq.queueNum = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-b") && i < args.length -1){
				qqq.beatheartNum = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-s") && i < args.length -1){
				qqq.selectThreadNum = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-n") && i < args.length -1){
				qqq.messageNum = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-m") && i < args.length -1){
				qqq.queueMaxSize = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-c") && i < args.length -1){
				qqq.handleMessageCost = Integer.parseInt(args[i+1]);
				i++;
			}
		}
		qqq.testThroughPut();
	}
	
	
	public void testThroughPut() throws Exception{
		
		
		final QueueSelector selector = new QueueSelector(1,500);
		final DefaultSelectableQueue queues[] = new DefaultSelectableQueue[queueNum];
		for(int i = 0 ; i < queueNum ; i++){
			queues[i] = new DefaultSelectableQueue(queueMaxSize);
			queues[i].setAttachment(i);
			queues[i].register(selector);
		}
		
		MySelectThread threads[] = new MySelectThread[selectThreadNum];
		for(int i = 0 ; i < selectThreadNum ; i++){
			threads[i] = new MySelectThread(selector,handleMessageCost);
			Thread t = new Thread(threads[i],"SelectThread-"+i);
			t.start();
			
		}
		
		//心跳线程
		MySendThread sendthreads[] = new MySendThread[beatheartNum];
		for(int i = 0 ; i < beatheartNum ; i++){
			sendthreads[i] = new MySendThread(i,i * queueNum/beatheartNum,queueNum/beatheartNum,messageNum,queues);
			Thread t = new Thread(sendthreads[i],"SendThread-"+i);
			t.start();
		}
		

		long lastTP[] = new long[301];
		long startTime = System.currentTimeMillis();
		System.out.println("Usage  : java -cp . QueueSelectorThroughPutTestCase -q queuenum -b beatheartNum -s selectThreadNum -n messageNum -m queueMaxSize");
		System.out.println("Author : myzdf");
		System.out.println("Version: 0.1");
		
		System.out.println("                            ===========  吞吐量测试  ===========                    ");
		System.out.println(String.format("队列数量：%d 每次心跳产生消息数量：%d 心跳线程：%d 发送线程：%d 队列最大容量：%d 处理消息花费：%d",
				this.queueNum,this.messageNum,this.beatheartNum,this.selectThreadNum,this.queueMaxSize,this.handleMessageCost));
		
		System.out.println("               队列积压                   1秒        60秒      	 120秒       300秒      平均");
		System.out.println("==============================================================================================");
		
		int count = 0;
		while(true){
			
			count++;
			
			if(count % 19 == 0){
				System.out.println("               队列积压                   1秒        60秒      	 120秒       300秒      平均");
				System.out.println("==============================================================================================");
			}
			
			for(int i = lastTP.length -1; i > 0 ; i--){
				lastTP[i] = lastTP[i-1];
			}
			lastTP[0] = MySelectThread.throughPut;
			//System.out.print("\r                                                                                                 \r");
			System.out.print("accumulate: "+String.format("%10d", selector.size())+" throughput: ");
			
			System.out.print(String.format(" %10d", lastTP[0] - lastTP[1]));
			System.out.print(String.format(" %10d", (lastTP[0] - lastTP[60])/60));
			System.out.print(String.format(" %10d", (lastTP[0] - lastTP[120])/120));
			System.out.print(String.format(" %10d", (lastTP[0] - lastTP[300])/300));
			System.out.print(String.format(" %10d", MySelectThread.throughPut * 1000/(System.currentTimeMillis() - startTime)));
			System.out.println();
			
			try{
				Thread.sleep(1000);
			}catch(Exception e){
				
			}
		}
	}
	

	static class MySelectThread implements Runnable{
		
		
		static long throughPut = 0;
		
		synchronized static void throughput(){
			throughPut++;
		}
		
		QueueSelector selector ;
		int handleMessageCost;
		
		public MySelectThread(QueueSelector selector,int handleMessageCost){
			this.selector = selector;
			this.handleMessageCost = handleMessageCost;
		}
		
		public void run() {
			while(Thread.currentThread().isInterrupted() == false){
				Collection<SelectableQueue> set = selector.select(1000);
				if(set != null){
					Iterator<SelectableQueue> it = set.iterator();
					while(it.hasNext()){
						SelectableQueue queue = it.next();
						Message m = null;
						while( (m = (Message)queue.pop()) != null){

							//模拟消息处理
							for(int i = 0 ; i < handleMessageCost ; i++){
								m.i = m.i + m.j + m.k;
							}
							throughput();
						}
						queue.returnToSelector();
					}
				}
			}
		}
	}
	
	static class MySendThread implements Runnable{

		int index;
		int offset;
		int num;
		int messageNum;
		DefaultSelectableQueue queues[] = null;
		
		public MySendThread(int index,int offset,int num,int messageNum,DefaultSelectableQueue[] queues){
			this.index = index;
			this.offset = offset;
			this.num = num;
			this.messageNum = messageNum;
			this.queues = queues;
		}
		
		public void run() {
			// TODO Auto-generated method stub
			while(Thread.currentThread().isInterrupted() == false){
				long ll = System.currentTimeMillis();
				
				for(int i = offset ; i < offset+num ; i++){
					DefaultSelectableQueue dq = queues[i];
					for(int j = 0 ; j < messageNum ; j++){
						Message m = new Message(index,i,j);
						dq.push(m, 100000);
					}
				}
				
				ll = System.currentTimeMillis() - ll;
				if(ll < 100){
					try{
						Thread.sleep(100-ll);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	static class Message {
		int i,j,k;
		public Message(int i,int j,int k){
			this.i = i;//thread index
			this.j = j;//queue index
			this.k = k;//message index
		}
	}

}
