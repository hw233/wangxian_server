package com.xuanzhi.tools.mem;

import java.util.ArrayList;
import java.util.HashMap;

import com.xuanzhi.tools.ds.AvlTree;
import com.xuanzhi.tools.ds.Heap;
import com.xuanzhi.tools.ds.AvlTree.TreeNode;
import com.xuanzhi.tools.mem.ObjectCreationTracker.CallTrace;

/**
 * 对象读和写的跟踪
 *
 */
public class HeartBeatTracker {
	

	public static class HeartBeatItem{
		long start;
		long end;
		
		String clazz;
	
		String threadName;

		public long getStart() {
			return start;
		}

		public long getEnd() {
			return end;
		}

		public String getClazz() {
			return clazz;
		}

		public String getThreadName() {
			return threadName;
		}
	}
	
	//对OperationItem进行排序
	AvlTree tree = new AvlTree(new java.util.Comparator<HeartBeatItem>(){
		public int compare(HeartBeatItem o1, HeartBeatItem o2) {
			if(o1 == o2) return 0;
			if(o1.start < o2.start){
				return -1;
			}else if(o1.start > o2.start){
				return 1;
			}else if(o1.end < o2.end){
				return -1;
			}else if(o1.end > o2.end){
				return 1;
			}
			if(o1.clazz.compareTo(o2.clazz) < 0){
				return -1;
			}else if(o1.clazz.compareTo(o2.clazz) > 0){
				return 1;
			}
			
			return 0;
		}
		
	});
	
	Heap heap = new Heap(new java.util.Comparator<HeartBeatItem>(){
		public int compare(HeartBeatItem o1, HeartBeatItem o2) {
			if(o1 == o2) return 0;
			if(o1.end < o2.end){
				return -1;
			}else if(o1.end > o2.end){
				return 1;
			}else if(o1.start < o2.start){
				return -1;
			}else if(o1.start > o2.start){
				return 1;
			}
		
			if(o1.clazz.compareTo(o2.clazz) < 0){
				return -1;
			}else if(o1.clazz.compareTo(o2.clazz) > 0){
				return 1;
			}
			
			return 0;
		}
		
	});
	
	public int trackDurationInSeconds = 10 * 60;
	public String name;
	
	public int getTrackerItemNum(){
		return tree.size();
	}
	private void cutTimeoutItems(long now){
		
		HeartBeatItem o = (HeartBeatItem)heap.peek();
		while(o != null && now - o.end > trackDurationInSeconds * 1000){
			heap.extract();
			tree.remove(o);
			o = (HeartBeatItem)heap.peek();
		}
	}
	
	synchronized void operateEnd(String description,long costTime){
		long now = System.currentTimeMillis();
		cutTimeoutItems(now);
		if(costTime < 1) costTime = 1;
		HeartBeatItem oi = new HeartBeatItem();
		oi.clazz = description;
		oi.start = now - costTime;
		oi.end = now;
		oi.threadName = Thread.currentThread().getName();
		tree.insert(oi);
		heap.insert(oi);
	}
	
	public synchronized ArrayList<HeartBeatItem> getStatOperationItems(long start,long end){
		long now = System.currentTimeMillis();
		cutTimeoutItems(now);

		ArrayList<HeartBeatItem> al = new ArrayList<HeartBeatItem>();
		TreeNode tn = tree.minimum();
		while(tn != null){
			HeartBeatItem oi = (HeartBeatItem)tn.getObject();
		
				if(oi.end < start || oi.start > end){
				}else{
					al.add(oi);
				}
			tn = tree.next(tn);
		}
		return al;
	}
	
	/**
	 * 统计某一个操作，一个时间间隔内的最大数量
	 * 如果时间间隔为1分钟，那么统计的是，这一分钟内这种操作并发的最大数量
	 * @param action
	 * @param stepInSeconds
	 */
	public synchronized float[] getStatConcurrentNum(int stepInSeconds){
		if(stepInSeconds < 1) stepInSeconds = 1;
		long now = System.currentTimeMillis();
		cutTimeoutItems(now);
		
		long startTime = now - trackDurationInSeconds * 1000;
		int n = trackDurationInSeconds/stepInSeconds;
		if(n * stepInSeconds < trackDurationInSeconds){
			n++;
		}
		float ret[] = new float[n];
		
		TreeNode tn = tree.minimum();
		while(tn != null){
			HeartBeatItem oi = (HeartBeatItem)tn.getObject();

				int from = (int)((oi.start - startTime)/1000/stepInSeconds);
				int to = (int)((oi.end - startTime)/1000/stepInSeconds);
				
				if(from == to){
					if(from >= 0 && from < n)
						ret[from] += 1.0f*(oi.end - oi.start)/1000/stepInSeconds;
				}else{
					if(from >= 0 && from < n){
						long kk = startTime + (from+1)*stepInSeconds*1000  - oi.start;
						ret[from] +=1.0f*kk/1000/stepInSeconds;
					}
					if(to >= 0 && to < n){
						long kk = oi.end - (startTime + (to)*stepInSeconds*1000);
						ret[to] +=1.0f*kk/1000/stepInSeconds;
					}
					for(int i = from+1 ; i < to ; i++){
						if(i >= 0 && i < n)
							ret[i] += 1;
					}
				}
			
			tn = tree.next(tn);
		}
		return ret;
	}
	
	
	
}
