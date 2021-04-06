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
public class ReadAndWriteOperationTracker {
	

	
	public static class CallTrace{
		public StackTraceElement[] elements;
		
		protected transient int callTraceIndex;
		
		public int getCallTraceIndex(){
			return callTraceIndex;
		}
		public static String toString(StackTraceElement[] e){
			StringBuffer sb = new StringBuffer();
			for(int i = 0 ; i < e.length ;i++){
				sb.append(e[i].getClassName()+"#" + e[i].getMethodName()+"\n");
			}
			return sb.toString();
		}
	}
	
	public static class OperationItem{
		long start;
		long end;
		
		String clazz;
		//读还是写
		int action;
		
		int num;
		long bytes;
		CallTrace ct;
		
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

		public int getAction() {
			return action;
		}

		public int getNum() {
			return num;
		}

		public long getBytes(){
			return bytes;
		}
		public CallTrace getCt() {
			return ct;
		}

		public String getThreadName() {
			return threadName;
		}
		
		
	}
	
	//对OperationItem进行排序
	AvlTree tree = new AvlTree(new java.util.Comparator<OperationItem>(){
		public int compare(OperationItem o1, OperationItem o2) {
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
			if(o1.action < o2.action){
				return -1;
			}else if(o1.action > o2.action){
				return 1;
			}
			
			if(o1.action < o2.action){
				return -1;
			}else if(o1.action > o2.action){
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
	
	Heap heap = new Heap(new java.util.Comparator<OperationItem>(){
		public int compare(OperationItem o1, OperationItem o2) {
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
			if(o1.action < o2.action){
				return -1;
			}else if(o1.action > o2.action){
				return 1;
			}
			
			if(o1.action < o2.action){
				return -1;
			}else if(o1.action > o2.action){
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
	public ArrayList<CallTrace> callTraceList = new ArrayList<CallTrace>();
	public HashMap<String,CallTrace> callTraceMap = new HashMap<String,CallTrace>();
	public String name;
	
	public int getTrackerItemNum(){
		return tree.size();
	}
	private void cutTimeoutItems(long now){
		
		OperationItem o = (OperationItem)heap.peek();
		while(o != null && now - o.end > trackDurationInSeconds * 1000){
			heap.extract();
			tree.remove(o);
			o = (OperationItem)heap.peek();
		}
	}
	
	synchronized void operateEnd(String description,int action,int num,long bytes,long costTime){
		long now = System.currentTimeMillis();
		cutTimeoutItems(now);
		if(costTime < 1) costTime = 1;
		OperationItem oi = new OperationItem();
		StackTraceElement[] eles = Thread.currentThread().getStackTrace();
		oi.action = action;
		oi.clazz = description;
		oi.start = now - costTime;
		oi.end = now;
		oi.num = num;
		oi.bytes = bytes;
		
		String key = CallTrace.toString(eles);
		CallTrace ct = callTraceMap.get(key);
		if(ct == null){
			ct = new CallTrace();
			ct.elements = eles;
			ct.callTraceIndex = callTraceList.size();
			callTraceList.add(ct);
			callTraceMap.put(key, ct);
		}
		oi.ct = ct;
		oi.threadName = Thread.currentThread().getName();
		tree.insert(oi);
		heap.insert(oi);
	}
	
	public synchronized ArrayList<OperationItem> getStatOperationItems(int action,long start,long end){
		long now = System.currentTimeMillis();
		cutTimeoutItems(now);

		ArrayList<OperationItem> al = new ArrayList<OperationItem>();
		TreeNode tn = tree.minimum();
		while(tn != null){
			OperationItem oi = (OperationItem)tn.getObject();
			if(oi.action == action){
				if(oi.end < start || oi.start > end){
				}else{
					al.add(oi);
				}
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
	public synchronized float[] getStatConcurrentNum(int action,int stepInSeconds){
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
			OperationItem oi = (OperationItem)tn.getObject();
			if(oi.action == action){
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
			}
			tn = tree.next(tn);
		}
		return ret;
	}
	
	/**
	 * 统计某一个操作，一个时间间隔内的操作对象的总和
	 * 如果时间间隔为1分钟，那么统计的是，这一分钟内这种操作操作对象的总和
	 * @param action
	 * @param stepInSeconds
	 */
	public synchronized float[] getStatOperateObjectNum(int action,int stepInSeconds){
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
			OperationItem oi = (OperationItem)tn.getObject();
			
			if(oi.action == action){
				int from = (int)((oi.start - startTime)/1000/stepInSeconds);
				int to = (int)((oi.end - startTime)/1000/stepInSeconds);
				
				if(from == to){
					if(from >= 0 && from < n)
						ret[from] += oi.num;
				}else{
					if(from >= 0 && from < n){
						long kk = startTime + (from+1)*stepInSeconds*1000  - oi.start;
						ret[from] +=oi.num * 1.0f * kk/(oi.end - oi.start);
					}
					if(to >= 0 && to < n){
						long kk = oi.end - (startTime + (to)*stepInSeconds*1000);
						ret[to] += oi.num * 1.0f * kk/(oi.end - oi.start);
					}
					for(int i = from+1 ; i < to ; i++){
						if(i >= 0 && i < n)
							ret[i] += oi.num * 1.0f * stepInSeconds*1000/(oi.end - oi.start);
					}
				}
			}
			tn = tree.next(tn);
		}
		return ret;
	}
	
	/**
	 * 统计某一个操作，一个时间间隔内的操作对象的总和
	 * 如果时间间隔为1分钟，那么统计的是，这一分钟内这种操作操作对象的总和
	 * @param action
	 * @param stepInSeconds
	 */
	public synchronized float[] getStatOperateDataBytes(int action,int stepInSeconds){
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
			OperationItem oi = (OperationItem)tn.getObject();
			
			if(oi.action == action){
				int from = (int)((oi.start - startTime)/1000/stepInSeconds);
				int to = (int)((oi.end - startTime)/1000/stepInSeconds);
				
				if(from == to){
					if(from >= 0 && from < n)
						ret[from] += oi.bytes;
				}else{
					if(from >= 0 && from < n){
						long kk = startTime + (from+1)*stepInSeconds*1000  - oi.start;
						ret[from] +=oi.bytes * 1.0f * kk/(oi.end - oi.start);
					}
					if(to >= 0 && to < n){
						long kk = oi.end - (startTime + (to)*stepInSeconds*1000);
						ret[to] += oi.bytes * 1.0f * kk/(oi.end - oi.start);
					}
					for(int i = from+1 ; i < to ; i++){
						if(i >= 0 && i < n)
							ret[i] += oi.bytes * 1.0f * stepInSeconds*1000/(oi.end - oi.start);
					}
				}
			}
			tn = tree.next(tn);
		}
		return ret;
	}
}
