package com.xuanzhi.tools.listcache.concrete;

import com.xuanzhi.tools.listcache.ListCache;

import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.*;
import java.util.*;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.CacheListener;

import com.xuanzhi.tools.listcache.event.*;
/**
 *
 */
public class LruListCache implements ListCache ,Cacheable,CacheListener,PropertyChangeListener{
	
	public static PrintStream logger = System.err;
	
	protected int totalSize = 0;
	protected int cachedSize = 0;
	
	protected int maxCachedSize = 1024 * 128;
	protected int maxBlockSize = 1024;
	protected int minBlockSize = 4;
	protected long maxBlockLifeTime = 30 * 60 * 1000L;
	protected int maxBlockNum = 512;
	
	protected boolean initialized = false;
	
	protected AvlTree avlTree;
	
	protected String name;
	protected String property;
	protected Comparator comparator;
	
	/**
	 * 构造函数。
	 * 请注意第一个参数和第二个参数，非常重要，整个ListCache依据这两个参数运行。
	 * 其中，comparator为比较器，比较两个对象的大小，比较器的方法compareTo(Object o1,Object o2)中的
	 * 两个参数，第一个就是排序列表中的对象，第二个对象可以是排序列表中的对象，也可以个这个对象的一个包裹OldObject.
	 * 所以，在实现这个比较方法的时候，一定要注意这种情况。
	 * OldObject代表的是，Object的值已经发生了变化，但是需要用变化前的值来比较。
	 * 典型的实现如下：
	 * 
	 * 			ListCache lc = new LruListCache(new Comparator(){
	 * 				public int compareTo(Object o1,Object o2){
	 * 					ABC a1 = (ABC)o1;	
	 * 					if(o2 instanceof OldValue){
	 * 						return a1.getViewCount() - (int)(((OldValue)o2).oldValue)
	 * 					}else{
	 * 						ABC a2 = (ABC)o2;
	 * 						return a1.getViewCount() - a2.getViewCount();
	 * 					}
	 * 				}
	 * 			},"viewCount","");
	 * 			lc.initialize(128*512,1800000L,1024,2,512);
	 * 
	 * 另外，在ABC的实现内中，需要通知ListCache，这个对象的属性值发生了变化，以方便ListCache自动调整排序的顺序。
	 * 通知ListCache的方法如下：
	 * 
	 * 		public class ABC{
	 * 			protected String id;
	 * 			protected int viewCount;
	 * 
	 * 			public void setViewCount(int c){
	 * 				int oldC = viewCount;
	 * 				if( oldC!= c){
	 * 					viewCount = c;
	 * 					ListCacheEventHandler.fire(new PropertyChangeEvent(this,"viewCount",oldC,"*"));	
	 * 				}
	 * 			}
	 * 
	 * 			public int hashCode(){
	 * 				return this.id.hashCode();
	 * 			}
	 * 
	 * 			public boolean equals(Object o){
	 * 				return id.equals(((ABC)o).id);
	 * 			}
	 * 		}
	 * 注意，PropertyChangeEvent构造函数中的Property字符串和ListCache
	 * 中的Property字符串必须一致，否则属性的变化通知不到要通知的ListCache。
	 * 同时，一个对象可能存在与成千上万个ListCache中，所以ListCacheEventHandler.fire方法
	 * 会根据用户指定的nameScope去搜索对应的ListCache，并逐一通知到所有ListCache。
	 * 当nameScope设置为*时，这个PropertyChangeEvent会通知到所有具有相同property的ListCache.
	 * 
	 * 
	 * @param comparator 比较器，这个比较器决定了排序的规则，什么对象排在前面，什么对象排在后面。
	 * @param property 对象的属性，表明这个排序的规则是根据这个属性的大小来排序的，
	 * 					property可以设置为null，表明ListCache不关心对象的属性值变化。也就是说，当对象属性值发生变化时，
	 * 					即使调用PropertyChangeHandler.fire方法，ListCache也不会调整顺序。
	 * 
	 * @param name ListCache命名空间中的名字，可以为null，设置为null的含义类似property为null的含义，也就是ListCache不关心
	 *             对象发生的变化。当名称不为null时，名称一般采用"xxxx.xxxx.xxx.xxx"来命名。
	 *             比如video.list.all，表明是全部视频对象的列表，当某个视频的属性发生变化的时候，
	 *             可以通过设置PropertyChangeEvent构造函数中的nameScope来指定哪些ListCache将得到更新。
	 *             详细说明请参见PropertyChangeEvent类。
	 */
	public LruListCache(Comparator comparator,String property,String name){
		this.comparator = comparator;
		this.property = property;
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public String getProperty(){
		return property;
	}
	
	public AvlTree getAvlTree(){
		return avlTree;
	}
	
	/**
	 * 块的访问列表，最久没有访问记录的块在队列的尾部
	 */	
	protected AccessList lastAccessedList;
	
	public void initailize(int maxCachedSize,long maxBlockLifeTime,int maxBlockSize,int minBlockSize,int maxBlockNum){
		if(initialized){
			throw new IllegalStateException("ListCache already initailized.");
		}
		
		this.maxBlockSize = maxBlockSize;
		this.maxCachedSize = maxCachedSize;
		this.maxBlockLifeTime = maxBlockLifeTime;
		this.minBlockSize = minBlockSize;
		this.maxBlockNum = maxBlockNum;
		
		avlTree = new AvlTree();
		lastAccessedList = new AccessList();
		initialized = true;
		
		ListCacheEventHandler.addListener(this);
	}


	public int getBlockNum(){
		if(avlTree == null) return 0;
		return avlTree.size();
	}
	
	public int getTotalSize() {
		return totalSize;
	}
	
	public void setTotalSize(int ts){
		totalSize = ts;
	}

	public int getCachedSize() {
		return cachedSize;
	}

	public int getMaxCachedSize() {
		return maxCachedSize;
	}

	public int getMaxBlockSize() {
		return maxBlockSize;
	}
	
	public int getMinBlockSize(){
		return minBlockSize;
	}

	public long getMaxBlockLifeTime() {
		return maxBlockLifeTime;
	}

	public synchronized int push(Object[] objs, int start, int offset,int size) {
		if(!initialized){
			throw new IllegalStateException("ListCache hasn't initailized.");
		}
		this.deleteExpiredBlock();
		this.cullCacheForCachedSize();
		this.cullCacheForBlockNum();
		
		if(size < 1)
			return 0;
		if(size > this.maxBlockSize){
			int c = 0;
			for(int i = 0 ; i < size ; i+=maxBlockSize){
				int num = maxBlockSize;
				if(i + num > size) num = size - i;
				c+=push(objs,start + i,offset + i,num);
			}
			return c;
		}
		
		Object [] objIds = new Object[size];
		for(int i = 0 ;  i < size  ; i++)
			objIds[i] = objs[offset + i];
		
		int conflict = 0;
		if(avlTree.isEmpty()){   //空 
			BlockTreeNode bn = avlTree.insert(comparator,objIds,start,size);
			AccessListNode aln = new AccessListNode(bn,null,null);
			bn.aln = aln;
			lastAccessedList.addFirst(aln);
			cachedSize += size;
		}else{
			
				BlockTreeNode bn1 = avlTree.find(start);
				BlockTreeNode node = null;
				if(bn1 == null){ // start位置在第一个块之前
					node = avlTree.minimum();
				}else if(bn1.start + bn1.size > start){ //start位置包含在块中
					node = bn1;
				}else{  // start位置位于两个块之间
					node = avlTree.next(bn1);
				}
				if(node == null){ // start位置在最后一个块之后
					BlockTreeNode bn = avlTree.insert(comparator,objIds,start,size);
					AccessListNode aln = new AccessListNode(bn,null,null);
					bn.aln = aln;
					lastAccessedList.addFirst(aln);
					cachedSize += size;
				}else{
					int k = 0; // 当前objIds的游标
					int n = 0; // objIds列表与avlTree中块重叠计数器或者不重叠计数器
					
					while(node != null && k < size){
						
						n = 0;
						while(start + k < node.start && k < size){ // 遍历两个块之间的部分，即不重叠计数
							k++;
							n++;
						}
						
						if(n > 0){ // 两个块之间的部分
							Object ids[] = new Object[n];
							for(int i = 0 ; i < n ; i++)
								ids[i] = objIds[k-n+i];
							if(n >= this.minBlockSize ){
								BlockTreeNode bn = avlTree.insert(comparator,ids,start+k-n,ids.length);
								//BlockNode bn = bl.addBefore(node,new BlockNode(comparator,ids,start+k-n,ids.length,null,null,maxBlockLifeTime));
								AccessListNode aln = new AccessListNode(bn,null,null);
								bn.aln = aln;
								lastAccessedList.addFirst(aln);
								cachedSize += n;
							}
							else if(start + k == node.start){ //向下合并
								Object newIds[] = new Object[n+node.size];
								for(int i = 0 ; i < n ; i++)
									newIds[i] = ids[i];
								for(int i = 0 ; i < node.size ; i++)
									newIds[n+i] = node.ids[i];
								node.ids = newIds;
								node.start = node.start - n;
								node.size = node.size + n;
								cachedSize += n;
							}else{ 
								BlockTreeNode previous = avlTree.previous(node);
								if(previous != null && (start + k - n == previous.start + previous.size)){//向上合并
									Object newIds[] = new Object[n+previous.size];
									for(int i = 0 ; i < previous.size ; i++)
										newIds[i] = previous.ids[i];
									for(int i = 0 ; i < n ; i++)
										newIds[previous.size + i] = ids[i];
									
									previous.ids = newIds;
									previous.size = previous.size + n;
									cachedSize += n;
								}else{
									//块太小，小于设定的最小块，直接丢弃
								}
							}
						}
						
						while(start + k < node.start + node.size && k < size){ //重叠部分计数，并检查冲突
							if(!objIds[k].equals(node.ids[start + k - node.start]))
								conflict ++;
							node.ids[start + k - node.start] = objIds[k];
							k++;
						}
						
						if(node.aln != null){
							node.aln.remove();
							lastAccessedList.addFirst(node.aln);
						}
						
						if(k >= size) break;
						
						node = avlTree.next(node);
					}// end while(node != null && k < size)
					
					if(k < size){ // 最后一部分
						n = size - k;
						k = size;
						
						Object ids[] = new Object[n];
						for(int i = 0 ; i < n ; i++)
							ids[i] = objIds[k-n+i];
						if(n >= this.minBlockSize ){
							BlockTreeNode bn = avlTree.insert(comparator,ids,start+k-n,ids.length);
							AccessListNode aln = new AccessListNode(bn,null,null);
							bn.aln = aln;
							lastAccessedList.addFirst(aln);
							cachedSize += n;
						}else{ 
							BlockTreeNode previous = avlTree.maximum();
							if(start + k - n == previous.start + previous.size){//向上合并
								Object newIds[] = new Object[n+previous.size];
								for(int i = 0 ; i < previous.size ; i++)
									newIds[i] = previous.ids[i];
								for(int i = 0 ; i < n ; i++)
									newIds[previous.size + i] = ids[i];
								
								previous.ids = newIds;
								previous.size = previous.size + n;
								cachedSize += n;
							}
						}
					}
				}
		}	
		
		return conflict;
	}

	public synchronized boolean isCovered(int start, int size) {
		if(!initialized){
			throw new IllegalStateException("ListCache hasn't initailized.");
		}
		this.deleteExpiredBlock();
		if(totalSize > 0 && size > totalSize - start)
			size = totalSize - start;
		
		int count = 0;
		BlockTreeNode node = avlTree.find(start);
		while(node != null){
			if(node.start >= start + size) break;
			int s = 0;
			if(start > node.start) s = start - node.start;
			count += Math.min(node.size - s,start+size-node.start-s);
			node = avlTree.next(node);
		}
		if(count == size) 
			return true;
		else
			return false;
	}

	public synchronized Object[] get(int start, int size) {
		if(!initialized){
			throw new IllegalStateException("ListCache hasn't initailized.");
		}
		this.deleteExpiredBlock();
		if(size == 0) return new Object[0];
		ArrayList list = new ArrayList(size);
		
		BlockTreeNode node = avlTree.find(start);
		if(node == null) node = avlTree.minimum();
		
		while(node != null){
			if(node.start >= start + size) break;
			
			int s = 0;
			if(start > node.start) s = start - node.start;
			for(int i = s ; i < node.size &&  i < start + size - node.start; i++){
				list.add(node.ids[i]);
			}
			if(node.aln != null){
				node.aln.remove();
				lastAccessedList.addFirst(node.aln);
			}
			node = avlTree.next(node);
		}
		return list.toArray();
	}

	
	public synchronized boolean remove(Object obj) {
		if(!initialized){
			throw new IllegalStateException("ListCache hasn't initailized.");
		}
		boolean b = false;
		BlockTreeNode node = avlTree.find(obj);
		
		if(node != null && node.start == 0){
			if(node.compareTo(obj) < 0)
				return b;
		}
		
		if(node == null){
			if(totalSize > 0)
				totalSize --;
			return b;
		}
		
		int k = node.compareTo(obj);
		if( k < 0){
			BlockTreeNode bn = node;
			while(bn != null){
				bn.start = bn.start -1;
				bn = avlTree.next(bn);
			}
		}
		if( k == 0){
			b = node.delete(obj);
			if(b){
				BlockTreeNode bn = avlTree.next(node);
				while(bn != null){
					bn.start = bn.start -1;
					bn = avlTree.next(bn);
				}
				cachedSize --;
			}
			if(node.size < this.minBlockSize){
				BlockTreeNode next = avlTree.next(node);
				if(next != null && node.start + node.size == next.start){//向下合并
					Object newIds[] = new Object[node.size + next.size];
					for(int i = 0 ; i < node.size ; i++)
						newIds[i] = node.ids[i];
					for(int i = 0 ; i < next.size ; i++)
						newIds[node.size + i] = next.ids[i];
					next.ids = newIds;
					next.size = node.size + next.size;
					next.start = node.start;
					avlTree.remove(node);
					if(node.aln != null)
						node.aln.remove();
				}else{
					BlockTreeNode previous = avlTree.previous(node);
					if(previous != null && previous.start + previous.size == node.start){//向上合并
						Object newIds[] = new Object[node.size + previous.size];
						for(int i = 0 ; i < previous.size ; i++)
							newIds[i] = previous.ids[i];
						for(int i = 0 ; i < node.size ; i++)
							newIds[previous.size + i] = node.ids[i];
						previous.ids = newIds;
						previous.size = node.size + previous.size;
						avlTree.remove(node);
						if(node.aln != null)
							node.aln.remove();
					}else{
						avlTree.remove(node);
						if(node.aln != null)
							node.aln.remove();
						cachedSize = cachedSize - node.size;
					}
				}
			}
		}
		
		if(totalSize > 0)
			totalSize --;
		
		return b;
	}

	public boolean contains(Object o){
		if(!initialized){
			return false;
		}
		
		BlockTreeNode node = avlTree.find(o);
		if(node == null) return false;
		int k = node.compareTo(o);
		if(k == 0){
			return node.isContains(o);
		}
		return false;
	}
	
	public synchronized void add(Object obj) {
		if(!initialized){
			throw new IllegalStateException("ListCache hasn't initailized.");
		}
		
		this.cullCacheForCachedSize();
		this.cullCacheForBlockNum();
		
		BlockTreeNode node = avlTree.find(obj);
		if(node != null){
			int k = node.compareTo(obj);
			if( k < 0){
				BlockTreeNode bn = node;
				while(bn != null){
					bn.start = bn.start + 1;
					bn = avlTree.next(bn);
				}
				if(node.start == 1){
					node.insertFirst(obj);
					cachedSize ++;
					node.start = 0;
				}
			}

			if( k == 0){
				boolean b = node.insert(obj);
				if(b){
					BlockTreeNode bn = avlTree.next(node);
					while(bn != null){
						bn.start = bn.start +1;
						bn = avlTree.next(bn);
					}
					cachedSize ++;
					if(node.size > this.maxBlockSize){//块拆分
						Object newIds[] = new Object[node.size - node.size/2];
						for(int i = 0 ; i < newIds.length ; i++)
							newIds[i] = node.ids[node.size/2 + i];
						Object ids[] = node.ids; 
						int start = node.start;
						int size = node.size/2;
						
						node.start = node.start + node.size/2;
						node.size = node.size - node.size/2;
						node.ids = newIds;
						
						BlockTreeNode newNode = avlTree.insert(comparator,ids,start,size); 
						
						AccessListNode aln = new AccessListNode(newNode,null,null);
						newNode.aln = aln;
						lastAccessedList.addFirst(aln);
						if(node.aln != null){
							node.aln.remove();
							lastAccessedList.addFirst(node.aln);
						}
							
					}
				}
			}
		}else{ // node == null
			node = avlTree.maximum();
			if(node != null && node.size + node.start == totalSize){
				node.insertEnd(obj);
				cachedSize ++;
			}
		}
		if(totalSize > 0)
			totalSize++;
	}
	
	public void update(Object obj,String property,Object oldValue){
		if(property != null && property.equals(this.property)){
			OldObject oo = new OldObject(obj,oldValue,property);
			remove(oo);
			add(obj);
		}
	}
	
	/**
	 * 清空Cache内的内容
	 */
	public synchronized void clear(){
		avlTree.clear();
		this.totalSize = 0;
		this.cachedSize = 0;
	}

	
	
	
	protected synchronized void deleteExpiredBlock() {
        if (this.maxBlockLifeTime <= 0) {
            return;
        }
        AccessListNode node = lastAccessedList.getLast();
        long expireTime = System.currentTimeMillis() - this.maxBlockLifeTime;

        while(node != null && expireTime > node.timestamp) {
        	node.remove();
        	
        	avlTree.remove(node.block);
        	
        	this.cachedSize = this.cachedSize - node.block.size;
            node = lastAccessedList.getLast();
            
            
        }
    }
	
    /**
     * Removes objects from cache if the cache is too full. "Too full" is
     * defined as within 3% of the maximum cache size. Whenever the cache is
     * is too big, the least frequently used elements are deleted until the
     * cache is at least 10% empty.
     */
    protected final synchronized void cullCacheForCachedSize() {
        //See if the cache size is within 3% of being too big. If so, clean out
        //cache until it's 10% free.
        if (this.cachedSize >= this.maxCachedSize * .97) {
            //First, delete any old entries to see how much memory that frees.
        	deleteExpiredBlock();
            int desiredSize = (int)(maxCachedSize * .90);
	
			AccessListNode node = lastAccessedList.getLast();
				
            while (node != null && cachedSize > desiredSize) {
            	node.remove();
            	avlTree.remove(node.block);
               	this.cachedSize = this.cachedSize - node.block.size;
                node = lastAccessedList.getLast();
            }
        }
    }

    /**
     * Removes objects from cache if the cache is too full. "Too full" is
     * defined as within 3% of the maximum cache size. Whenever the cache is
     * is too big, the least frequently used elements are deleted until the
     * cache is at least 10% empty.
     */
    protected final synchronized void cullCacheForBlockNum() {
        //See if the cache size is within 3% of being too big. If so, clean out
        //cache until it's 10% free.
        if (this.getBlockNum()  >= this.maxBlockNum * 1.1) {
            //First, delete any old entries to see how much memory that frees.
        	deleteExpiredBlock();
            int desiredNum = (int)(maxBlockNum * .90);
	
			AccessListNode node = lastAccessedList.getLast();
				
            while (node != null && getBlockNum() > desiredNum) {
            	node.remove();
            	avlTree.remove(node.block);
       
            	
            	this.cachedSize = this.cachedSize - node.block.size;
                node = lastAccessedList.getLast();
            }
        }
    }

    public int getSize(){
    	return 1;
    }

 	public void propertyChanged(PropertyChangeEvent event) {
		Object obj = event.getSource();
		if(property != null && event.getOldValue() != null){
			update(obj,property,event.getOldValue());
		}
	}
 	
 	public void objectCreated(ObjectCreateEvent event){
 		String pp[] = event.getProperties();
 		boolean b = false;
 		for(int i = 0 ; !b && i < pp.length ; i++){
 			if(pp[i].equals(this.property))
 				b = true;
 		}
 		if(b){
 			this.add(event.getSource());
 		}
 	}
 	
 	public void objectDeleted(ObjectDeleteEvent event){
 		String pp[] = event.getProperties();
 		boolean b = false;
 		for(int i = 0 ; !b && i < pp.length ; i++){
 			if(pp[i].equals(this.property))
 				b = true;
 		}
 		if(b){
 			this.remove(event.getSource());
 		}
 	}

	public void remove(int arg0) {
		ListCacheEventHandler.removeListener(this);
	}
}
